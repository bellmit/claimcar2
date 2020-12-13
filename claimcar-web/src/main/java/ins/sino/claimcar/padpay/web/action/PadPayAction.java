/******************************************************************************
* CREATETIME : 2016年3月4日 下午2:45:18
******************************************************************************/
package ins.sino.claimcar.padpay.web.action;

import freemarker.core.ParseException;
import ins.framework.web.AjaxResult;
import ins.framework.web.bind.annotation.FormModel;
import ins.platform.common.util.ConfigUtil;
import ins.platform.common.web.util.WebUserUtils;
import ins.platform.sysuser.service.facade.SysUserService;
import ins.platform.utils.DataUtils;
import ins.platform.vo.PrpLConfigValueVo;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.check.vo.PrpLCheckDutyVo;
import ins.sino.claimcar.claim.service.ClaimService;
import ins.sino.claimcar.claim.service.CompensateHandleServiceIlogService;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.claim.vo.PrpLPadPayMainVo;
import ins.sino.claimcar.claim.vo.PrpLPadPayPersonVo;
import ins.sino.claimcar.constant.Risk;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.AssignService;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.AMLVo;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.ilog.defloss.vo.LIlogRuleResVo;
import ins.sino.claimcar.lossperson.service.PersTraceDubboService;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersInjuredVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceMainVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceVo;
import ins.sino.claimcar.manager.service.ManagerService;
import ins.sino.claimcar.manager.vo.PrpLPayCustomVo;
import ins.sino.claimcar.pay.service.PadPayService;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.PrpLCMainService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLCInsuredVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.rule.service.ClaimRuleApiService;
import ins.sino.claimcar.rule.vo.VerifyClaimRuleVo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sinosoft.insaml.apiclient.FxqCrmRiskService;
import com.sinosoft.insaml.povo.vo.CrmRiskInfoVo;

/** 垫付Action
 * @author ★Luwei
 */
@Controller
@RequestMapping("/padPay")
public class PadPayAction {
	private static Logger logger = LoggerFactory.getLogger(PadPayAction.class);
	
	@Autowired
	private ManagerService managerService;

	@Autowired
	ClaimService claimService;
	
	@Autowired
	PadPayService padPayService;
	
	@Autowired
	WfTaskHandleService wfTaskHandleService;
	
	@Autowired
	ClaimRuleApiService claimRuleApiService;
	
	@Autowired
	AssignService assignService;
	
	@Autowired
	SysUserService sysUserService;
    @Autowired
    PrpLCMainService prpLCMainService;
    @Autowired
	private RegistQueryService registQueryService;
    @Autowired
    PersTraceDubboService persTraceDubboService;
    @Autowired
	PolicyViewService policyViewService;
	
    @Autowired
    CompensateHandleServiceIlogService conpensateHandleServiceIlogService;
	/** 垫付发起验证
	 * @param registNo
	 * @modified: ☆Luwei(2016年3月22日 下午3:32:39): <br>
	 */
	@RequestMapping("/padPayTaskVlaid.do")
	@ResponseBody
	public AjaxResult padPayTaskVlaid(String registNo) {
		AjaxResult ajaxResult = new AjaxResult();
		String retData = padPayService.padPayTaskVlaid(registNo);
		ajaxResult.setData(retData);
		ajaxResult.setStatus(HttpStatus.SC_OK);
		return ajaxResult;
	}
	
	/** 垫付处理
	 * ☆Luwei(2016年3月4日 下午2:50:17): <br>
	 */
	@RequestMapping("/padPayEdit.do")
	public ModelAndView padPayEdit(String registNo,String taskInKey,String flowTaskId) {
		logger.info("报案号={},垫付处理，工作流flowTaskId={}",registNo,flowTaskId);
		ModelAndView modelAndView = new ModelAndView();
		PrpLWfTaskVo wfTaskVo = null;
		String claimNo = "";
		List<PrpLClaimVo> claimVos = claimService.findClaimListByRegistNo(registNo);
		if(claimVos!=null&&claimVos.size()>0){
			for(PrpLClaimVo claim:claimVos){
				if(Risk.DQZ.equals(claim.getRiskCode())){
					claimNo = claim.getClaimNo();
				}
			}
		}
		
		if(StringUtils.isNotBlank(flowTaskId)){
			wfTaskVo = wfTaskHandleService.queryTask(Double.parseDouble(flowTaskId));
			modelAndView.addObject("handlerStatus",wfTaskVo.getHandlerStatus());
		}
		
		//基本信息
		PrpLClaimVo claimVo = padPayService.getClaimNo(claimNo);
		PrpLCMainVo cMainVo = padPayService.initCMainInfo(registNo);
		
		PrpLRegistVo registVo = padPayService.getRegistVo(registNo);
		PrpLCheckDutyVo checkDutyVo = padPayService.queryCheckDuty(registNo);
		String colorCode = padPayService.getColorCode(registNo);
		
		//垫付信息
		PrpLPadPayMainVo padPayMainVo = padPayService.initPadPay(registNo,claimNo,wfTaskVo);
		if(padPayMainVo.getNoticeDate() == null){
			padPayMainVo.setNoticeDate(new Date());
		}
		List<PrpLPadPayPersonVo> padPayPersonVos = new ArrayList<PrpLPadPayPersonVo>();
		if(padPayMainVo.getPrpLPadPayPersons() != null && padPayMainVo.getPrpLPadPayPersons().size() > 0){
		    for(PrpLPadPayPersonVo prpLPadPayPersonVo:padPayMainVo.getPrpLPadPayPersons()){
	            PrpLPayCustomVo customVo = managerService.findPayCustomVoById(prpLPadPayPersonVo.getPayeeId());//获取收款人信息
	            prpLPadPayPersonVo.setPayObjectKind(customVo.getPayObjectKind());
	            PrpLDlossPersInjuredVo persInjuredVo = new PrpLDlossPersInjuredVo();
	            PrpLDlossPersTraceVo prpLDlossPersTraceVo = new PrpLDlossPersTraceVo();
	            PrpLDlossPersTraceMainVo persMainVo = new PrpLDlossPersTraceMainVo();
	            if(StringUtils.isNotBlank(prpLPadPayPersonVo.getPersonName())){
                    Long id = Long.valueOf(prpLPadPayPersonVo.getPersonName()).longValue();
                    persInjuredVo = persTraceDubboService.findPersInjuredByPK(id);
                    prpLDlossPersTraceVo = persTraceDubboService.findPersTraceByPK(id);
                    persMainVo = persTraceDubboService.findPersTraceMainByPk(prpLDlossPersTraceVo.getPersTraceMainId());
                }
	            if(!"7".equals(persMainVo.getUnderwriteFlag())){//任务没有注销
	                if(!"0".equals(prpLDlossPersTraceVo.getValidFlag())){//有效
	                    String idNo = "1".equals(persInjuredVo.getCertiType())
	                            ?persInjuredVo.getCertiCode():"";
	                    prpLPadPayPersonVo.setPersonIdfNo(idNo);
	                    prpLPadPayPersonVo.setPayeeName(customVo.getPayeeName());
	                    prpLPadPayPersonVo.setAccountNo(customVo.getAccountNo());
	                    prpLPadPayPersonVo.setBankName(customVo.getBankOutlets());
	                    if(prpLPadPayPersonVo.getSummary()==null || "".equals(prpLPadPayPersonVo.getSummary())){
	                        prpLPadPayPersonVo.setSummary(registVo.getPrpLRegistExt().getLicenseNo()+"赔款");
	                    }
	                    padPayPersonVos.add(prpLPadPayPersonVo);
	                }
	            }
	        }
		}
	    
//		else{
//			if(wfTaskVo == null && padPayMainVo != null){
//				List<PrpLWfTaskVo> oldTaskVoList = wfTaskHandleService.findEndTask
//						(registNo,null,FlowNode.PadPay);
//				if(oldTaskVoList != null && oldTaskVoList.size() >0){
//					wfTaskVo = oldTaskVoList.get(0);
//					modelAndView.addObject("handlerStatus",wfTaskVo.getHandlerStatus());
//				}
//			}
//		}
		
		//人员姓名
		Map<String,String> perNameMap = padPayService.getPersonNameMap(registNo,claimNo);
		Map<String,String> licenseNoMap = padPayService.getLicenseNo(registNo);//车牌号
		
		PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.STARFLAG);

		if(configValueVo!=null && "1".equals(configValueVo.getConfigValue()) && wfTaskVo != null && "3".equals(wfTaskVo.getHandlerStatus())){
			if(padPayMainVo.getPolicePhone() != null){
				padPayMainVo.setPolicePhone(DataUtils.replacePrivacy(padPayMainVo.getPolicePhone()));
			}
			if(padPayPersonVos != null && padPayPersonVos.size()>0){
				for(int i=0; i<padPayPersonVos.size();i++){
					if(padPayPersonVos.get(i).getAccountNo() != null){
						padPayPersonVos.get(i).setAccountNo(DataUtils.replacePrivacy(padPayPersonVos.get(i).getAccountNo()));
					}
				}
			}
		}

		modelAndView.addObject("cMainVo",cMainVo);
		modelAndView.addObject("claimVo",claimVo);
		modelAndView.addObject("wfTaskVo",wfTaskVo);
		modelAndView.addObject("checkDutyVo",checkDutyVo);
		modelAndView.addObject("colorCode",colorCode);
		modelAndView.addObject("registVo",registVo);
		modelAndView.addObject("padPayMainVo",padPayMainVo);
		modelAndView.addObject("padPayPersonVos",padPayPersonVos);
		modelAndView.addObject("licenseNoMap",licenseNoMap);
		modelAndView.addObject("perNameMap",perNameMap);

		//查询垫付限额，查询医疗费用的有责限额
		double padLimitAmount = padPayService.getPadLimitAmount(registNo);
		modelAndView.addObject("padLimitAmount",padLimitAmount);
		//反洗钱
	    List<PrpLCMainVo> prpLCMains = prpLCMainService.findPrpLCMainsByRegistNo(registNo);
        PrpLCMainVo prpLCMain = new PrpLCMainVo();
        if(prpLCMains != null && prpLCMains.size() == 2){
            for(PrpLCMainVo vo : prpLCMains){
               if("12".equals(vo.getRiskCode().substring(0, 2))){//取商业
                    prpLCMain = vo;
                }
             }
           }else{
                 prpLCMain = prpLCMains.get(0);
        }
        modelAndView.addObject("prpLCMain",prpLCMain);
        SysUserVo userVo = WebUserUtils.getUser();
        modelAndView.addObject("userVo", userVo);
		modelAndView.setViewName("padPay/PadPayEdit");
		return modelAndView;
	}
	
	/*
	 * 通过报案号查询垫付处理的flowtaskId
	 */
	@RequestMapping("/padPayTaskId.do")
	public ModelAndView padPayTaskId(String registNo,String taskInKey){
		String flowTaskId=null;
	   //查PrpLwfTaskOut
	   List<PrpLWfTaskVo> wfTaskVoList1 = 
			   wfTaskHandleService.findPrpLWfTaskOutByRegistNo(registNo);
	         if(wfTaskVoList1 != null && wfTaskVoList1.size() > 0){
				for(PrpLWfTaskVo prpLWfTaskVo : wfTaskVoList1){
					if(prpLWfTaskVo.getSubNodeCode().equals("PadPay") 
							&& !(prpLWfTaskVo.getWorkStatus().equals("7"))){
						flowTaskId=prpLWfTaskVo.getTaskId().toString();
					}
				}
			}
		return padPayEdit(registNo,taskInKey,flowTaskId);
	}
	
	/**
	 * 通过报案号查询该报案号下是否过发起垫付
	 */
	@RequestMapping(value = "/padPayView.ajax")
	@ResponseBody
	public AjaxResult padPayView(String registNo) {
		// wfTaskHandleService.findEndTask(registNo,null,FlowNode.Chk);
		AjaxResult ajaxResult = new AjaxResult();
		String flowTaskId = null;
		try{
			List<PrpLWfTaskVo> prpLWfTaskVoList1 = wfTaskHandleService.findPrpLWfTaskOutByRegistNo(registNo);
			if(prpLWfTaskVoList1!=null&&prpLWfTaskVoList1.size()>0){
				for(PrpLWfTaskVo prpLWfTaskVo:prpLWfTaskVoList1){
					if(prpLWfTaskVo.getSubNodeCode().equals("PadPay")&& !( prpLWfTaskVo.getWorkStatus().equals("7") )&& !( prpLWfTaskVo
							.getHandlerStatus().equals("9") )){
						flowTaskId = prpLWfTaskVo.getTaskId().toString();

					}
				}
			}
			ajaxResult.setStatus(HttpStatus.SC_OK);
			ajaxResult.setData(flowTaskId);
		}
		catch(Exception e){
			ajaxResult.setStatus(HttpStatus.SC_EXPECTATION_FAILED);
		}
		return ajaxResult;
	}
	
	/** 发起垫付初始化
	 * ☆Luwei(2016年3月4日 下午2:50:17): <br>
	 */
	@RequestMapping("/padPayInit.do")
	public ModelAndView padPayInit(String registNo) {
		return padPayEdit(registNo,"","");
	}
	
	
	
	/** 垫付 保存 */
	@RequestMapping("/savePadPay")
	@ResponseBody
	public AjaxResult savePadPay(@FormModel("padPayMainVo") PrpLPadPayMainVo padPayMainVo,
		@FormModel("padPayPersonVo") List<PrpLPadPayPersonVo> padPayPersonVos,String flowTaskId) {
		AjaxResult ajaxResult = new AjaxResult();
		try{
			//判断非被保险人支付例外是否为空
			if(padPayService.saveBeforeCheck(padPayPersonVos)){
				throw new IllegalArgumentException("收款人为非被保险人，请填写例外原因");
			}
			String userCode = WebUserUtils.getUserCode();
			String comCode = WebUserUtils.getComCode();

			// 组织业务数据
			padPayMainVo.setPrpLPadPayPersons(padPayPersonVos);
			PrpLPadPayMainVo vo = padPayService.save(padPayMainVo,userCode,comCode);
			// 把垫付任务变为正在处理
			if(StringUtils.isNotBlank(flowTaskId)){
				String compeNo = vo.getCompensateNo();
				wfTaskHandleService.tempSaveTask(Double.parseDouble(flowTaskId),compeNo,
						WebUserUtils.getUserCode(),WebUserUtils.getComCode());
			}
			ajaxResult.setStatus(HttpStatus.SC_OK);
			ajaxResult.setData(true);	
		}catch(Exception e){
			logger.error("垫付保存失败,错误信息",e);
			ajaxResult.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			ajaxResult.setStatusText(e.getMessage());
		}
	
		return ajaxResult;
	}
	
	/**	 垫付申请提交(发起)
	 * @param registNo
	 * @param claimNo
	 * @return
	 * @modified:
	 * ☆Luwei(2016年4月20日 上午10:45:45): <br>
	 */
	@RequestMapping("/advPadPay.ajax")
	@ResponseBody
	public AjaxResult advPadPay(String registNo,String claimNo) {
		AjaxResult ajaxResult = new AjaxResult();
		String comCode = WebUserUtils.getComCode();
		String userCode = WebUserUtils.getUserCode();
		// ，生成工作流节点..
		String result = padPayService.LaunchPadPayTask(registNo,claimNo,comCode,userCode);
		ajaxResult.setStatus(HttpStatus.SC_OK);
		ajaxResult.setData(result);
		return ajaxResult;
	}
	
	/**	 异步获取收款人信息	**/
	@RequestMapping("/getPayCustom.ajax")
	@ResponseBody
	public AjaxResult getPayCustom(Long id) {
		AjaxResult ajaxResult = new AjaxResult();
		Map<String,String> map = padPayService.getPayCustom(id);
		ajaxResult.setData(map);
		ajaxResult.setStatus(HttpStatus.SC_OK);
		return ajaxResult;
	}
	 
	 
	@RequestMapping("/getPersonName.ajax")
	@ResponseBody
	public AjaxResult getPersonName(Long id) {
		AjaxResult ajaxResult = new AjaxResult();
		Map<String,String> map = padPayService.getPersonName(id);
		ajaxResult.setData(map);
		ajaxResult.setStatus(HttpStatus.SC_OK);
		return ajaxResult;
	}
	
	/** 添加垫付信息行的ajax请求
	 * @return
	 */
	@RequestMapping("/addPersRow.ajax")
	@ResponseBody
	public ModelAndView addPersRow(int per_index,int personVoSize,String registNo,String claimNo,String handlerStatus) {
		ModelAndView modelAndView = new ModelAndView();

		Map<String,String> perNameMap = padPayService.getPersonNameMap(registNo,claimNo);// 人员姓名
		Map<String,String> licenseNoMap = padPayService.getLicenseNo(registNo);// 车牌号
		List<PrpLPadPayPersonVo> list = new ArrayList<PrpLPadPayPersonVo>();
		PrpLPadPayPersonVo personVo = new PrpLPadPayPersonVo();
		//摘要默认值为标的车牌号+赔款
		PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(registNo);
		personVo.setSummary(prpLRegistVo.getPrpLRegistExt().getLicenseNo()+"赔款");
		list.add(personVo);

		modelAndView.addObject("padPayPersonVos",list);
		modelAndView.addObject("size",personVoSize);
		modelAndView.addObject("pe_Idx",personVoSize);
		modelAndView.addObject("perNameMap",perNameMap);
		modelAndView.addObject("licenseNoMap",licenseNoMap);
	    modelAndView.addObject("handlerStatus",handlerStatus);
		modelAndView.setViewName("padPay/PadPayEdit_PersonTr");
		return modelAndView;
	}
	
	/** 垫付提交初始化
	 * @return
	 * ☆Luwei(2016年3月9日 下午5:53:44): <br>
	 * @throws Exception 
	 */
	@RequestMapping("/initPadPaySubmit.do")
	public ModelAndView initPadPaySubmit(String flowTaskId) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		
		PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(Double.parseDouble(flowTaskId));
		String registNo = wfTaskVo.getRegistNo();
		String claimNo = wfTaskVo.getTaskInKey();
		PrpLPadPayMainVo padPayVo = padPayService.getPadPayInfo(registNo,claimNo);

		VerifyClaimRuleVo verifyClaimRuleVo = new VerifyClaimRuleVo();
		verifyClaimRuleVo.setComCode(wfTaskVo.getComCode());
		verifyClaimRuleVo.setClassCode("11");
		verifyClaimRuleVo.setRiskCode(Risk.DQZ);

		Double fee = 0.0;
		List<PrpLPadPayPersonVo> perVoLost = padPayVo.getPrpLPadPayPersons();
		for (PrpLPadPayPersonVo perVo : perVoLost) {
			if (perVo.getCostSum() != null) {
				fee += perVo.getCostSum()==null?0D:perVo.getCostSum().doubleValue();
			}
		}
		verifyClaimRuleVo.setSumFee(fee);
        PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.ILOGVALIDFLAG,WebUserUtils.getComCode());
        String nextNode = null;
        int level = 0;
        if("1".equals(configValueVo.getConfigValue())){//使用ilog
            //ilog规则引擎start====================
            SysUserVo userVo = WebUserUtils.getUser();
            
          //判断是否总公司审核过
            Map<String,Object> params = new HashMap<String,Object>();
            params.put("registNo",registNo);
            if(flowTaskId == null){
            	  params.put("taskId",BigDecimal.ZERO.doubleValue());
            } else{
                params.put("taskId",Double.valueOf(flowTaskId));
            }
            String isSubmitHeadOffice = managerService.isSubmitHeadOffice(params);
            
            LIlogRuleResVo vPriceResVo = conpensateHandleServiceIlogService.organizaForPadPay(padPayVo,"2","02",new BigDecimal(flowTaskId),FlowNode.PadPay.name(),userVo,isSubmitHeadOffice);
            if(Integer.parseInt(vPriceResVo.getMinUndwrtNode()) > Integer.parseInt(vPriceResVo.getMaxUndwrtNode())){
                level = Integer.parseInt(vPriceResVo.getMaxUndwrtNode());
            }else{
                level = Integer.parseInt(vPriceResVo.getMinUndwrtNode());
            }
            
            modelAndView.addObject("level", level);
            modelAndView.addObject("padId", padPayVo.getId());
            modelAndView.addObject("flowTaskId", flowTaskId);
            
            nextNode = "VClaim_CI_LV"+level;
            int maxLevel = Integer.parseInt(vPriceResVo.getMaxUndwrtNode());
            boolean haveUser = false;
            for(int i=level;i<maxLevel;i++){
                haveUser = assignService.existsGradeUser(FlowNode.valueOf(nextNode), WebUserUtils.getComCode());
                if(!haveUser){
                    nextNode = "VClaim_CI_LV"+(i+1);
                }else{
                    break;
                }
            }
            //ilog规则引擎end====================
        }//else{
        PrpLConfigValueVo configRuleValueVo = ConfigUtil.findConfigByCode(CodeConstants.RULEVALIDFLAG,WebUserUtils.getComCode());
        if("1".equals(configRuleValueVo.getConfigValue())){
            VerifyClaimRuleVo vcRuleVo = claimRuleApiService.compToVClaim(verifyClaimRuleVo);
            
            if(vcRuleVo.getBackLevel() > vcRuleVo.getMaxLevel()){
                level = vcRuleVo.getMaxLevel();
            }else{
                level = vcRuleVo.getBackLevel();
            }
            
            modelAndView.addObject("level", level);
            modelAndView.addObject("padId", padPayVo.getId());
            modelAndView.addObject("flowTaskId", flowTaskId);
            
            nextNode = "VClaim_CI_LV"+level;
            boolean haveUser = false;
            for(int i=level;i<vcRuleVo.getMaxLevel();i++){
                haveUser = assignService.existsGradeUser(FlowNode.valueOf(nextNode), WebUserUtils.getComCode());
                if(!haveUser){
                    nextNode = "VClaim_CI_LV"+(i+1);
                }else{
                    break;
                }
            }
        }
		
		//设置轮询岗人员
		SysUserVo assUserVo = null;
		PrpLWfTaskVo oldTaskVo = padPayService.findTaskIn(registNo, padPayVo.getCompensateNo(),FlowNode.valueOf(nextNode));
		if(oldTaskVo != null){
			assUserVo = new SysUserVo();
			assUserVo.setUserCode(oldTaskVo.getHandlerUser());
			SysUserVo sysUserVo = sysUserService.findByUserCode(oldTaskVo.getHandlerUser());
			assUserVo.setUserName(sysUserVo.getUserName());
		}else{
			assUserVo = assignService.execute(FlowNode.valueOf(nextNode),WebUserUtils.getComCode(),WebUserUtils.getUser(), "");
		}
		modelAndView.addObject("assUserVo",assUserVo);
		modelAndView.setViewName("padPay/PadPayEdit_Submit");
		return modelAndView;
	}
	
	/** 垫付处理提交
	 * @return
	 * ☆Luwei(2016年3月9日 下午5:53:44): <br>
	 * @throws Exception 
	 */
	@RequestMapping("/submit_padPay.ajax")
	@ResponseBody
	public AjaxResult submit_padPay(String taskId,Long padId,Integer level,
	                                String nextUserCode,String nextComCode) throws Exception {
		AjaxResult ajaxResult = new AjaxResult();
		String comCode = WebUserUtils.getComCode();
		String userCode = WebUserUtils.getUserCode();
		padPayService.submitPadPay(taskId,padId,level,
				comCode,userCode,nextUserCode,nextComCode);
		ajaxResult.setStatus(HttpStatus.SC_OK);
		return ajaxResult;
	}
	
	/** 垫付 删除人员信息 */
	@RequestMapping("/dropPersonLoss.ajax")
	@ResponseBody
	public AjaxResult dropPersonLoss(Long id) throws ParseException {
		AjaxResult ajaxResult = new AjaxResult();
		// 删除
		padPayService.dropPerByPK(id);
		ajaxResult.setStatus(HttpStatus.SC_OK);
		return ajaxResult;
	}
	
	/**
	 * 垫付页面验证反洗钱人员是否被冻结
	 * @param list
	 * @return
	 */
	@RequestMapping(value = "/vaxInfor.ajax")
	@ResponseBody
	public AjaxResult vaxInfor(String policyNo,String registNo) {
		AjaxResult ajaxResult = new AjaxResult();
		 //被保险人
		SysUserVo userVo=WebUserUtils.getUser();
		AMLVo amlVo = new AMLVo();
        List<PrpLCMainVo> cmainVos= prpLCMainService.findPrpLCMainsByRegistNo(registNo);
        if(cmainVos!=null && cmainVos.size()>0){
        	for(PrpLCMainVo vo:cmainVos){
        		if(StringUtils.isNotBlank(policyNo) && policyNo.equals(vo.getPolicyNo())){
        			 List<PrpLCInsuredVo> PrpLCInsuredList = vo.getPrpCInsureds();
        		            for(PrpLCInsuredVo cInsured:PrpLCInsuredList){
        		                if("1".equals(cInsured.getInsuredFlag())){
        		                	
        		                    amlVo.setInsuredName(cInsured.getInsuredName());
        		                    amlVo.setIdentifyNumber(cInsured.getIdentifyNumber());
        		                    
        		                }
        		               
        		            }
        		        }
        	}
        }

        
        String amlUrl = SpringProperties.getProperty("dhic.aml.saveurl");
           String nameList="";
       	   FxqCrmRiskService crmRiskService = new FxqCrmRiskService(amlUrl);
       	if(StringUtils.isNotBlank(amlVo.getIdentifyNumber())){
				
				try {
					CrmRiskInfoVo crmRiskInfoVo = crmRiskService.getCustRiskInfo(amlVo.getIdentifyNumber(),amlVo.getInsuredName(), amlVo.getIdentifyNumber(),"",userVo.getUserCode());
					if(crmRiskInfoVo!=null && "1".equals(crmRiskInfoVo.getIsFreeze())){
						nameList="被保险人[ "+crmRiskInfoVo.getCustName()+" ],";
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
		     }
		
		ajaxResult.setData(nameList);
		ajaxResult.setStatus(HttpStatus.SC_OK);
		return ajaxResult;
	}
	
	/*
	 * 垫付注销
	 * taskId
	 */
	@RequestMapping(value = "/padPayCancel.do", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult padPayCancel(String taskId) {
		AjaxResult ajaxResult = new AjaxResult();
		SysUserVo user = WebUserUtils.getUser();
		try{
			padPayService.padPayCancel(taskId,user);
			ajaxResult.setStatus(HttpStatus.SC_OK);
		} catch(Exception e){
			logger.info("user:" + user + "操作 taskid == " + taskId + "的垫付注销失败！", e);
			ajaxResult.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			ajaxResult.setStatusText(e.getMessage());
			e.printStackTrace();
		}
		return ajaxResult;
	}
	
	
}
