package ins.sino.claimcar.claim.web.action;

import ins.framework.web.AjaxResult;
import ins.framework.web.bind.annotation.FormModel;
import ins.platform.common.service.facade.CodeDictService;
import ins.platform.common.service.facade.CodeTranService;
import ins.platform.common.util.CodeTranUtil;
import ins.platform.common.util.ConfigUtil;
import ins.platform.common.web.util.WebUserUtils;
import ins.platform.sysuser.service.facade.SysUserService;
import ins.platform.utils.DataUtils;
import ins.platform.vo.PrpLConfigValueVo;
import ins.platform.vo.SysCodeDictVo;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.CompensateFlag;
import ins.sino.claimcar.CodeConstants.PayStatus;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.PrpLCheckDutyVo;
import ins.sino.claimcar.check.vo.PrpLCheckVo;
import ins.sino.claimcar.claim.service.ClaimService;
import ins.sino.claimcar.claim.service.CompensateHandleServiceIlogService;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claim.vo.PrpLPrePayVo;
import ins.sino.claimcar.claim.vo.SubmitNextVo;
import ins.sino.claimcar.constant.Risk;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.AssignService;
import ins.sino.claimcar.flow.service.WfFlowQueryService;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.flow.vo.WfTaskSubmitVo;
import ins.sino.claimcar.ilog.defloss.vo.LIlogRuleResVo;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.lossperson.service.PersTraceDubboService;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceMainVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceVo;
import ins.sino.claimcar.lossprop.service.PropTaskService;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropMainVo;
import ins.sino.claimcar.manager.service.ManagerService;
import ins.sino.claimcar.pay.service.PrePayHandleService;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLCItemKindVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.rule.service.ClaimRuleApiService;
import ins.sino.claimcar.rule.vo.VerifyClaimRuleVo;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author lanlei
 */
@Controller
@RequestMapping("/prePay")
public class PrePayAction {
	@Autowired
	private ManagerService managerService;
	@Autowired
	CompensateTaskService  compensateTaskService;
	@Autowired
	PolicyViewService policyViewService;
	@Autowired
	RegistQueryService registQueryService;
	@Autowired
	ClaimService claimService;
	@Autowired
	WfTaskHandleService wfTaskHandleService;
	@Autowired
	WfFlowQueryService wfFlowQueryService;
	@Autowired
	CheckTaskService checkTaskService;
	
	@Autowired
	ClaimRuleApiService claimRuleApiService;
	@Autowired
	LossCarService lossCarService;
	@Autowired
	PropTaskService propTaskService;
	@Autowired
	PersTraceDubboService persTraceDubboService;
	@Autowired
	CodeTranService codeTranService;
	@Autowired
	AssignService assignService;
	@Autowired
	CodeDictService codeDictService;
	@Autowired
	PrePayHandleService prePadHandleService;
	@Autowired
	SysUserService sysUserService;

    @Autowired
    CompensateHandleServiceIlogService conpensateHandleServiceIlogService;
    
    private Logger  logger = LoggerFactory.getLogger(PrePayAction.class); 
	/**
	 * ??????????????????
	 * @param registNo
	 * @return
	 * @modified: ???XMSH(2016???3???28??? ??????9:57:11): <br>
	 */
	@RequestMapping("/prePayApply.do")
	public ModelAndView prePayApply(String registNo) {
		ModelAndView modelAndView = new ModelAndView();

		PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);

		List<PrpLClaimVo> claimVos = claimService.findClaimListByRegistNo(registNo);

		List<PrpLCompensateVo> compensateCIVos = null;// ????????????????????????
		List<PrpLCompensateVo> compensateBIVos = null;// ????????????????????????

		// boolean existPrePayTask = false;// ??????????????????????????????

		for(PrpLClaimVo claimVo:claimVos){
			FlowNode currentNode = null;
			FlowNode nextNode = null;

			if("1101".equals(claimVo.getRiskCode())){
				currentNode = FlowNode.ClaimCI;
				nextNode = FlowNode.PrePayCI;

				compensateCIVos = compensateTaskService.findCompListByClaimNo(claimVo.getClaimNo(),"Y");
			}else{
				currentNode = FlowNode.ClaimBI;
				nextNode = FlowNode.PrePayBI;

				compensateBIVos = compensateTaskService.findCompListByClaimNo(claimVo.getClaimNo(),"Y");
			}
			//????????????????????????????????????
			if("1234".contains(registVo.getIsGBFlag())){
				claimVo.setFlag("1");
			}else{
				// ???????????????????????????????????????????????????
				boolean existPrePayTask = wfTaskHandleService.existTaskByNodeCode(claimVo.getRegistNo(),nextNode,
						claimVo.getClaimNo(),"0");

				if(existPrePayTask){
					claimVo.setFlag("1");
				}else{
					claimVo.setFlag("0");
				}
			}
			
		}
		ConfigUtil configUtil = new ConfigUtil(); 
		PrpLConfigValueVo configValueVo = configUtil.findConfigByCode(CodeConstants.STARFLAG);

		if(configValueVo!=null && "1".equals(configValueVo.getConfigValue())){
		//	registVo.insuredPhone 
			registVo.setInsuredPhone(DataUtils .replacePrivacy(registVo.getInsuredPhone()));
			registVo.setLinkerPhone(DataUtils .replacePrivacy(registVo.getLinkerPhone()));
		//	registVo.linkerMobile
			String[] mobiles = registVo.getLinkerMobile().split(",");
			StringBuffer str = new StringBuffer();
			for (int i = 0; i < mobiles.length; i++) {
				str .append(DataUtils.replacePrivacy(mobiles[i]));
				str.append(",");
			}
			registVo.setLinkerMobile(str.substring(0, str.lastIndexOf(",")).toString());
		}
		
		modelAndView.addObject("registVo",registVo);
		modelAndView.addObject("claimVos",claimVos);
		modelAndView.addObject("compensateCIVos",compensateCIVos);
		modelAndView.addObject("compensateBIVos",compensateBIVos);
		modelAndView.setViewName("prePay/PrePayApply");

		return modelAndView;
	}

	/**
	 * ??????????????????
	 * 
	 * <pre></pre>
	 * @param claimNoArr
	 * @return
	 * @modified: ???XMSH(2016???4???16??? ??????4:38:39): <br>
	 */
	@RequestMapping("/prePayTaskApply.do")
	@ResponseBody
	public AjaxResult prePayTaskApply(String[] claimNoArr) {
		AjaxResult ajaxResult = new AjaxResult();
		
		try{
			if(claimNoArr!=null && claimNoArr.length>0){
				for(int i=0;i<=claimNoArr.length-1;i++){
					PrpLClaimVo prpLClaimVo=claimService.findByClaimNo(claimNoArr[i]);
					List<PrpLWfTaskVo>  wftaskIns=wfFlowQueryService.findTaskVoForInByClaimNo(claimNoArr[i],FlowNode.Cancel.name());
					if(prpLClaimVo!=null && ((wftaskIns!=null && wftaskIns.size()>0) || "0".equals(prpLClaimVo.getValidFlag()))){
						throw new IllegalArgumentException("???claimNo="+claimNoArr[i]+"????????????????????????????????????????????????????????????");
					}
				}
			}
			
			SysUserVo userVo = WebUserUtils.getUser();
			prePadHandleService.prePayTaskApply(claimNoArr,userVo);
			ajaxResult.setStatus(HttpStatus.SC_OK);
		}
		catch(Exception e){
			// TODO Auto-generated catch block
			ajaxResult.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			ajaxResult.setStatusText(e.getMessage());
			e.printStackTrace();
		}

		// modelAndView.addObject("wfTaskVoList",wfTaskVoList);
		return ajaxResult;
	}

	/**
	 * ????????????
	 * @param flowTaskId
	 * @param claimNo
	 * @return
	 * @modified: ???XMSH(2016???3???30??? ??????10:39:05): <br>
	 */
	@SuppressWarnings("null")
	@RequestMapping(value = "/prePayApplyEdit.do", method = RequestMethod.GET)
	public ModelAndView prePayApplyEdit(Double flowTaskId) {
		ModelAndView modelAndView = new ModelAndView();

		PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(flowTaskId);

		String prePayNo = wfTaskVo.getCompensateNo();// ?????????
		String claimNo = wfTaskVo.getClaimNo();// ?????????
		String handlerStatus = wfTaskVo.getHandlerStatus();
		PrpLClaimVo prpLClaimVo = claimService.findByClaimNo(claimNo);

		// ????????????????????????????????????
		List<PrpLCompensateVo> compensateVos = compensateTaskService.findCompListByClaimNo(claimNo,"Y");

		// ???????????????
		BigDecimal sumAllAmt = BigDecimal.ZERO;
		if(compensateVos!=null&&compensateVos.size()>0){
			for(PrpLCompensateVo compVo:compensateVos){
				if("1".equals(compVo.getUnderwriteFlag())){
					sumAllAmt = sumAllAmt.add(compVo.getSumAmt());
				}
			}
		}

		// ??????????????????
		PrpLCMainVo prpLCMainVo = policyViewService.getPolicyInfo(prpLClaimVo.getRegistNo(),prpLClaimVo.getPolicyNo());

		// ??????????????????
		PrpLRegistVo prpLregistVo = registQueryService.findByRegistNo(prpLClaimVo.getRegistNo());

		List<PrpLPrePayVo> prePayPVos = null;
		List<PrpLPrePayVo> prePayFVos = null;
		PrpLCompensateVo compensateVo = compensateTaskService.findCompByPK(prePayNo);
		if(compensateVo==null||compensateVo.getCompensateNo()==null){
			PrpLCheckVo checkVo = checkTaskService.findCheckVoByRegistNo(prpLClaimVo.getRegistNo());
			PrpLCheckDutyVo prpLCheckDutyVo = checkTaskService.findCheckDuty(prpLClaimVo.getRegistNo(),1);
			compensateVo = new PrpLCompensateVo();

			String caseType = "1";
			if("1".equals(checkVo.getIsClaimSelf())){
				caseType = "2";
			}else if("1".equals(checkVo.getIsSubRogation())){
				caseType = "3";
			}

			compensateVo.setCompensateNo(wfTaskVo.getCompensateNo());// ?????????????????????
			compensateVo.setClaimNo(claimNo);
			compensateVo.setRegistNo(prpLClaimVo.getRegistNo());
			compensateVo.setPolicyNo(prpLClaimVo.getPolicyNo());
			compensateVo.setRiskCode(prpLClaimVo.getRiskCode());
			compensateVo.setMakeCom(prpLCMainVo.getMakeCom());
			compensateVo.setComCode(prpLCMainVo.getComCode());
			compensateVo.setCaseType(caseType);
			if("1101".equals(prpLClaimVo.getRiskCode())){
				if("0".equals(prpLCheckDutyVo.getCiDutyFlag())){
					compensateVo.setIndemnityDuty("0");
					compensateVo.setIndemnityDutyRate(new BigDecimal(0));
				}else{
					compensateVo.setIndemnityDuty("1");
					compensateVo.setIndemnityDutyRate(new BigDecimal(100));
				}
			}else{
				compensateVo.setIndemnityDuty(prpLCheckDutyVo.getIndemnityDuty());
				compensateVo.setIndemnityDutyRate(prpLCheckDutyVo.getIndemnityDutyRate());
			}
		}else{
			prePayPVos = compensateTaskService.getPrePayVo(prePayNo,"P");
			prePayFVos = compensateTaskService.getPrePayVo(prePayNo,"F");
		}
		
		
		
		Map<String,String> kindMap = new HashMap<String,String>();
		for(PrpLCItemKindVo itemKind:prpLCMainVo.getPrpCItemKinds()){
			if("Y".equals(itemKind.getCalculateFlag())&& !itemKind.getKindCode().endsWith("M")){//??????????????????
				kindMap.put(itemKind.getKindCode(),itemKind.getKindName());
			}
		}
		modelAndView.addObject("kindMap",kindMap);

		// ????????????????????????
		List<SysCodeDictVo> lossInfoList = new ArrayList<SysCodeDictVo>();
		List<PrpLDlossCarMainVo> lossCarMainVos = lossCarService.findLossCarMainByRegistNo(prpLClaimVo.getRegistNo());
		if(lossCarMainVos!=null&&lossCarMainVos.size()>0){
			for(PrpLDlossCarMainVo lossCarMainVo:lossCarMainVos){
				if("1".equals(lossCarMainVo.getUnderWriteFlag())){// ??????????????????????????????
					SysCodeDictVo dictVo = new SysCodeDictVo();
					String codeCode = "car_"+lossCarMainVo.getSerialNo()+"_"+lossCarMainVo.getId();
					String codeName = "";
					if(lossCarMainVo.getSerialNo()==0){
						codeName = "??????";
					}else if(lossCarMainVo.getSerialNo()==1){
						codeName = "?????????("+lossCarMainVo.getLicenseNo()+")";
					}else{
						codeName = "?????????("+lossCarMainVo.getLicenseNo()+")";
					}
					dictVo.setCodeCode(codeCode);
					dictVo.setCodeName(codeName);
					lossInfoList.add(dictVo);
				}
			}
		}

		// ????????????????????????
		List<PrpLdlossPropMainVo> lossPropMainVos = propTaskService.findPropMainListByRegistNo(prpLClaimVo
				.getRegistNo());
		if(lossPropMainVos!=null&&lossPropMainVos.size()>0){
			for(PrpLdlossPropMainVo propMainVo:lossPropMainVos){
				if("1".equals(propMainVo.getUnderWriteFlag())){// ????????????????????????
					SysCodeDictVo dictVo = new SysCodeDictVo();
					String codeCode = "prop_"+propMainVo.getSerialNo()+"_"+propMainVo.getId();
					String codeName = "";
					if(propMainVo.getSerialNo()==0){
						codeName = "?????????";
					}else if(propMainVo.getSerialNo()==1){
						codeName = "?????????("+propMainVo.getLicense()+")";
					}else{
						codeName = "?????????("+propMainVo.getLicense()+")";
					}

					dictVo.setCodeCode(codeCode);
					dictVo.setCodeName(codeName);
					lossInfoList.add(dictVo);
				}
			}
		}

		// ????????????????????????
		List<PrpLDlossPersTraceMainVo> persTraceMainVos = persTraceDubboService.findPersTraceMainVoList(prpLClaimVo.getRegistNo());
		if(persTraceMainVos!=null&&persTraceMainVos.size()>0){
			for(PrpLDlossPersTraceMainVo persTraceMainVo:persTraceMainVos){
			    if(!"7".equals(persTraceMainVo.getUnderwriteFlag())){
			     // ???????????????????????????????????????       ?????????  ????????????????????????   ??????241   
	                if(persTraceMainVo.getPrpLDlossPersTraces()!=null&&persTraceMainVo.getPrpLDlossPersTraces().size()>0){
	                    for(PrpLDlossPersTraceVo lossPersTraceVo:persTraceMainVo.getPrpLDlossPersTraces()){
	                        if("1".equals(lossPersTraceVo.getValidFlag())){
	                            SysCodeDictVo dictVo = new SysCodeDictVo();
	                            String codeCode = "pers_"+lossPersTraceVo.getPrpLDlossPersInjured().getSerialNo()+"_"+lossPersTraceVo
	                                    .getId();
	                            String codeName = "";
	                            String lossItemType = codeTranService.transCode("LossItemType",lossPersTraceVo
	                                    .getPrpLDlossPersInjured().getLossItemType());
	                            codeName = lossItemType+"("+lossPersTraceVo.getPrpLDlossPersInjured().getLicenseNo()+")"+"("+lossPersTraceVo
	                                    .getPrpLDlossPersInjured().getPersonName()+")";
	                            dictVo.setCodeCode(codeCode);
	                            dictVo.setCodeName(codeName);
	                            lossInfoList.add(dictVo);
	                        }
	                    }
	                }
			    }
			}
		}
		if(lossInfoList.size()==0){
			SysCodeDictVo dictVo = new SysCodeDictVo();
			lossInfoList.add(dictVo);
		}

	//	ConfigUtil configUtil = new ConfigUtil(); 
		PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.STARFLAG);

		if(configValueVo!=null && "1".equals(configValueVo.getConfigValue())){
		//	registVo.insuredPhone 
			//prpLregistVo.setInsuredPhone(DataUtils .replacePrivacy(prpLregistVo.getInsuredPhone()));
		//	registVo.linkerMobile
			String[] mobiles = prpLregistVo.getLinkerMobile().split(",");
			StringBuffer str = new StringBuffer();
			for (int i = 0; i < mobiles.length; i++) {
				str .append(DataUtils.replacePrivacy(mobiles[i]));
				str.append(",");
			}
			prpLregistVo.setLinkerMobile(str.substring(0, str.lastIndexOf(",")).toString());

		}
		List<PrpLPrePayVo>   prePayListAll = new ArrayList<PrpLPrePayVo>();
		List<PrpLPrePayVo>  prePayListSingle = null;
		for(int i=0; i< compensateVos.size(); i++){
			prePayListSingle = compensateTaskService.queryPrePay(compensateVos.get(i).getCompensateNo());
			if(prePayListSingle!= null && prePayListSingle.size() >0){
				for(int j=0;j<prePayListSingle.size();j++){
					PrpLPrePayVo	pay = prePayListSingle.get(j);
					if(pay != null){
						prePayListAll.add(pay);
					}
				}
			}
		}
		BigDecimal sumPeiKuan =  BigDecimal.ZERO;
		BigDecimal sumFeiYong =  BigDecimal.ZERO;
		for(int i=0; i<prePayListAll.size();i++){
			if("P".equals(prePayListAll.get(i).getFeeType())){
				sumPeiKuan	= sumPeiKuan.add(prePayListAll.get(i).getPayAmt());
			}else{
				sumFeiYong = sumFeiYong.add(prePayListAll.get(i).getPayAmt());
			}
		}
		
			
//		BigDecimal sumPeiKuan =  BigDecimal.ZERO;
//		BigDecimal sumFeiYong =  BigDecimal.ZERO;
		modelAndView.addObject("PeiKuan", sumPeiKuan);
		modelAndView.addObject("FeiYong", sumFeiYong);
		modelAndView.addObject("lossInfoList",lossInfoList);
		modelAndView.addObject("flowTaskId",flowTaskId);
		modelAndView.addObject("handlerStatus",handlerStatus);
		modelAndView.addObject("sumAllAmt",sumAllAmt);
		modelAndView.addObject("prePayPVos",prePayPVos);
		modelAndView.addObject("prePayFVos",prePayFVos);
		modelAndView.addObject("compensateVo",compensateVo);
		modelAndView.addObject("compensateVos",compensateVos);
		modelAndView.addObject("prpLCMainVo",prpLCMainVo);
		modelAndView.addObject("prpLregistVo",prpLregistVo);
		modelAndView.addObject("prpLClaimVo",prpLClaimVo);
		modelAndView.addObject("wfTaskVo",wfTaskVo);
		PrpLConfigValueVo configValuesingVo = ConfigUtil.findConfigByCode(CodeConstants.newBillDate);
		boolean showFlag=compensateTaskService.findsingcompay(compensateVo.getCompensateNo(),DateBillString(configValuesingVo.getConfigValue()));
		modelAndView.addObject("showFlag",showFlag ? "1":"0" );//??????????????????????????????
        //?????????
        SysUserVo userVo = WebUserUtils.getUser();
        modelAndView.addObject("userVo", userVo);
		modelAndView.setViewName("prePay/PrePayApplyEdit");

		return modelAndView;
	}

	/**
	 * ???????????????
	 * @param compensateVo
	 * @param prePayPVos
	 * @param prePayFVos
	 * @param submitNextVo
	 * @return
	 * @modified: ???XMSH(2016???4???18??? ??????5:47:44): <br>
	 */
	@RequestMapping(value = "/saveOrSubmit.do", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult saveOrSubmit(@FormModel("compensateVo") PrpLCompensateVo compensateVo,
									@FormModel("prePayPVos") List<PrpLPrePayVo> prePayPVos,
									@FormModel("prePayFVos") List<PrpLPrePayVo> prePayFVos,
									@FormModel("submitNextVo") SubmitNextVo submitNextVo
									) {
	//feeType--???????????????????????????
		AjaxResult ajaxResult = new AjaxResult();

		try{
			SysUserVo userVo = WebUserUtils.getUser();
			//?????????????????????????????????????????????
			if(prePadHandleService.saveBeforeCheck(prePayPVos)){
				throw new IllegalArgumentException("???????????????????????????????????????????????????");
			}
			String auditStatus = prePadHandleService.saveOrSubmit(compensateVo, prePayPVos, prePayFVos, submitNextVo,userVo);
			
			ajaxResult.setStatus(HttpStatus.SC_OK);
			ajaxResult.setData(auditStatus);
		}
		catch(Exception e){
			ajaxResult.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			ajaxResult.setStatusText(e.getMessage());
			e.printStackTrace();
			String registNo = compensateVo == null ? "" : compensateVo.getRegistNo();
			String compensateNo = compensateVo == null ? "" : compensateVo.getCompensateNo();
			logger.info("????????????" + registNo + " ???????????????" + compensateNo + " ??????????????????????????????", e);
		}

		return ajaxResult;
	}

	/**
	 * ????????????????????????ajax??????
	 * @return
	 */
	@RequestMapping("/addPreRow.ajax")
	public ModelAndView addRowInfo(String registNo,String policyNo,String bodyName,int size,String handlerStatus) {
		String tName = bodyName;
		ModelAndView modelAndView = new ModelAndView();
		if(tName.equals("IndemnityTbody")){// ??????IndemnityTbody
			List<PrpLPrePayVo> list = new ArrayList<PrpLPrePayVo>();
			PrpLPrePayVo prePayVo = new PrpLPrePayVo();
			//??????????????????
			String licenseNo = "";
	        PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(registNo);
	        if(StringUtils.isNotBlank(prpLRegistVo.getPrpLRegistExt().getLicenseNo())){
	            licenseNo = prpLRegistVo.getPrpLRegistExt().getLicenseNo();
	        }
			prePayVo.setSummary(licenseNo+"??????");
			list.add(prePayVo);
			modelAndView.addObject("prePayPVos", list);
			modelAndView.setViewName("prePay/PrePayApplyEdit_IndemnityTr");
		}
//		if(tName.equals("FeeTbody")){// ??????FeeTbody
//			modelAndView.setViewName("prePay/PrePayApplyEdit_FeeTr");
//		}

		Map<String,String> kindMap = new HashMap<String,String>();
		PrpLCMainVo prpLCMainVo = policyViewService.getPrpLCMainByRegistNoAndPolicyNo(registNo,policyNo);
		for(PrpLCItemKindVo itemKind:prpLCMainVo.getPrpCItemKinds()){
			if( !CodeConstants.NOSUBRISK_MAP.containsKey(itemKind.getKindCode())&& !itemKind.getKindCode()
					.endsWith("M")){
				kindMap.put(itemKind.getKindCode(),itemKind.getKindName());
			}
		}
		modelAndView.addObject("kindMap",kindMap);
		modelAndView.addObject("handlerStatus",handlerStatus);
		// ????????????????????????
		List<SysCodeDictVo> lossInfoList = new ArrayList<SysCodeDictVo>();
		SysCodeDictVo dictVo1 = new SysCodeDictVo();
		dictVo1.setCodeCode("");
		dictVo1.setCodeName("");
		lossInfoList.add(dictVo1);
		List<PrpLDlossCarMainVo> lossCarMainVos = lossCarService.findLossCarMainByRegistNo(registNo);
		if(lossCarMainVos!=null&&lossCarMainVos.size()>0){
			for(PrpLDlossCarMainVo lossCarMainVo:lossCarMainVos){
				if("1".equals(lossCarMainVo.getUnderWriteFlag())){// ??????????????????????????????
					SysCodeDictVo dictVo = new SysCodeDictVo();
					String codeCode = "car_"+lossCarMainVo.getSerialNo()+"_"+lossCarMainVo.getId();
					String codeName = "";
					if(lossCarMainVo.getSerialNo()==0){
						codeName = "??????";
					}else if(lossCarMainVo.getSerialNo()==1){
						codeName = "?????????("+lossCarMainVo.getLicenseNo()+")";
					}else{
						codeName = "?????????("+lossCarMainVo.getLicenseNo()+")";
					}
					dictVo.setCodeCode(codeCode);
					dictVo.setCodeName(codeName);
					lossInfoList.add(dictVo);
				}
			}
		}

		// ????????????????????????
		List<PrpLdlossPropMainVo> lossPropMainVos = propTaskService.findPropMainListByRegistNo(registNo);
		if(lossPropMainVos!=null&&lossPropMainVos.size()>0){
			for(PrpLdlossPropMainVo propMainVo:lossPropMainVos){
				if("1".equals(propMainVo.getUnderWriteFlag())){// ????????????????????????
					SysCodeDictVo dictVo = new SysCodeDictVo();
					String codeCode = "prop_"+propMainVo.getSerialNo()+"_"+propMainVo.getId();
					String codeName = "";
					if(propMainVo.getSerialNo()==0){
						codeName = "?????????";
					}else if(propMainVo.getSerialNo()==1){
						codeName = "?????????("+propMainVo.getLicense()+")";
					}else{
						codeName = "?????????("+propMainVo.getLicense()+")";
					}

					dictVo.setCodeCode(codeCode);
					dictVo.setCodeName(codeName);
					lossInfoList.add(dictVo);
				}
			}
		}

		// ????????????????????????
		List<PrpLDlossPersTraceMainVo> persTraceMainVos = persTraceDubboService.findPersTraceMainVoList(registNo);
		if(persTraceMainVos!=null&&persTraceMainVos.size()>0){
			for(PrpLDlossPersTraceMainVo persTraceMainVo:persTraceMainVos){
			    if(!"7".equals(persTraceMainVo.getUnderwriteFlag())){
		             // ???????????????????????????????????????    ?????????  ????????????????????????   ??????241   
	                if(persTraceMainVo.getPrpLDlossPersTraces()!=null&&persTraceMainVo.getPrpLDlossPersTraces().size()>0){
	                    for(PrpLDlossPersTraceVo lossPersTraceVo:persTraceMainVo.getPrpLDlossPersTraces()){
	                        if("1".equals(lossPersTraceVo.getValidFlag())){
	                            SysCodeDictVo dictVo = new SysCodeDictVo();
	                            String codeCode = "pers_"+lossPersTraceVo.getPrpLDlossPersInjured().getSerialNo()+"_"+lossPersTraceVo
	                                    .getId();
	                            String codeName = "";
	                            String lossItemType = codeTranService.transCode("LossItemType",lossPersTraceVo
	                                    .getPrpLDlossPersInjured().getLossItemType());
	                            codeName = lossItemType+"("+lossPersTraceVo.getPrpLDlossPersInjured().getLicenseNo()+")"+"("+lossPersTraceVo
	                                    .getPrpLDlossPersInjured().getPersonName()+")";
	                            dictVo.setCodeCode(codeCode);
	                            dictVo.setCodeName(codeName);
	                            lossInfoList.add(dictVo);
	                        }
	                    }
	                }
			    }
			}
		}
		

		modelAndView.addObject("lossInfoList",lossInfoList);
       
		modelAndView.addObject("index",size);
		return modelAndView;
	}
	
	@RequestMapping(value = "/initChargeType.ajax")
	@ResponseBody
	public ModelAndView initChargeType(String chargeCodes) {
		List<String> chargeCodeList = new ArrayList<String>();
		//???????????????????????????????????????
		if(chargeCodes!=null&& !"".equals(chargeCodes)){
			chargeCodes+=",14,99";
		}else{
			chargeCodes = "14,99";
		}
		
		String[] chargeArray = chargeCodes.split(",");
		chargeCodeList = Arrays.asList(chargeArray);

		ModelAndView mav = new ModelAndView();
		List<SysCodeDictVo> sysCodes = codeDictService.findCodeListByQuery("ChargeCode",chargeCodeList);
		mav.addObject("sysCodes",sysCodes);
		mav.setViewName("prePay/ChargeDialog");
		return mav;
	}

	/**
	 * ????????????????????????????????????
	 * 
	 * @param feePayInfoSize
	 * @param registNo
	 * @return
	 * @modified: ???XMSH(2016???1???9??? ??????10:22:49): <br>
	 */
	@RequestMapping(value = "/loadChargeTr.ajax")
	@ResponseBody
	public ModelAndView loadChargeTr(int size,String[] chargeTypes,String registNo,String policyNo) {
		ModelAndView mv = new ModelAndView();
		
		List<PrpLPrePayVo> prePayFVos = new ArrayList<PrpLPrePayVo>();

		Map<String,String> kindMap = new HashMap<String,String>();
		PrpLCMainVo prpLCMainVo = policyViewService.getPrpLCMainByRegistNoAndPolicyNo(registNo,policyNo);
		for(PrpLCItemKindVo itemKind:prpLCMainVo.getPrpCItemKinds()){
			if( "Y".equals(itemKind.getCalculateFlag())&& !itemKind.getKindCode()
					.endsWith("M")){
				kindMap.put(itemKind.getKindCode(),itemKind.getKindName());
			}
		}
		mv.addObject("kindMap",kindMap);
		
		//??????????????????
		String licenseNo = "";
        PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(registNo);
        if(StringUtils.isNotBlank(prpLRegistVo.getPrpLRegistExt().getLicenseNo())){
            licenseNo = prpLRegistVo.getPrpLRegistExt().getLicenseNo();
        }
        
		for(String chargeCode : chargeTypes){
			PrpLPrePayVo prePayVo = new PrpLPrePayVo();
			prePayVo.setChargeCode(chargeCode);
			String summary = licenseNo + CodeTranUtil.transCode("ChargeCode",chargeCode);
			prePayVo.setSummary(summary);
			prePayFVos.add(prePayVo);
		}
		
		mv.addObject("prePayFVos",prePayFVos);
		mv.addObject("size",size);
		mv.setViewName("prePay/PrePayApplyEdit_FeeTr");
		return mv;
	}

	/**
	 * ???????????????????????????
	 * @return
	 * @modified: ???XMSH(2016???4???18??? ??????4:11:30): <br>
	 */
	@RequestMapping("/loadSubmitNext.ajax")
	public ModelAndView loadSubmitNext() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("prePay/SubmitNext");

		modelAndView.addObject("assignUser",WebUserUtils.getUserName());
		return modelAndView;
	}

	/**
	 * ????????????????????????
	 * @return
	 * @modified: ???XMSH(2016???4???18??? ??????4:09:32): <br>
	 */
	@RequestMapping("/loadSubmitVClaimNext.ajax")
	public ModelAndView loadSubmitVClaimNext(String currentNode,String auditStatus,String riskCode,String comCode,
												double sumAmt,String registNo,String compensateNo) throws Exception {

		ModelAndView mav = new ModelAndView();
		SubmitNextVo nextVo = new SubmitNextVo();
		// nextVo.setNextNode(FlowNode.VClaim.name());
		// nextVo.setNextName(FlowNode.VClaim.getName());

		VerifyClaimRuleVo verifyClaimRuleVo = new VerifyClaimRuleVo();
		verifyClaimRuleVo.setComCode(comCode);
		verifyClaimRuleVo.setClassCode(riskCode.substring(0,2));
		verifyClaimRuleVo.setRiskCode(riskCode);
		BigDecimal sumYAmt = new BigDecimal(0);
		sumYAmt = sumYAmt.add(new BigDecimal(sumAmt));
		if(sumAmt < 0){
			sumAmt = sumAmt*(-1);
		}
		//???????????????????????????????????????????????????
		BigDecimal sendToIlogAmt = new BigDecimal(sumAmt);
		List<PrpLCompensateVo> compensateVos = compensateTaskService.findOppCompensates(compensateNo,CompensateFlag.prePay);
		if(compensateVos != null && !compensateVos.isEmpty()){
			for(PrpLCompensateVo compensateVo : compensateVos){
				sendToIlogAmt = sendToIlogAmt.add(compensateVo.getSumAmt().multiply(new BigDecimal(-1)));
			}
		}
		//????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
		verifyClaimRuleVo.setSumAmt((sendToIlogAmt == null? null : sendToIlogAmt.doubleValue()));
        PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.ILOGVALIDFLAG,comCode);
        String nextNode = null;
        Boolean isFirstPrePayWf = false;
        if("1".equals(configValueVo.getConfigValue())){//??????ilog
          //ilog????????????start====================
            SysUserVo userVo = WebUserUtils.getUser();
            List<PrpLWfTaskVo> prpLWfTaskVoList = new ArrayList<PrpLWfTaskVo>();
            PrpLWfTaskVo prpLWfTaskVo = new PrpLWfTaskVo();
            String callNode = "01";
            if(currentNode.contains("PrePayWf")){
            	prpLWfTaskVoList = wfTaskHandleService.findOutTaskVo(registNo,compensateNo,FlowNode.PrePay.name());//??????
            	if(prpLWfTaskVoList == null ||  prpLWfTaskVoList.size() < 1){
                    //??????
                    prpLWfTaskVoList = wfTaskHandleService.findInTaskVo(registNo,compensateNo,FlowNode.PrePayWf.name());
            	} else{
            		isFirstPrePayWf = true;
            	}
                if(sumAmt > 0){
                    sumAmt = sumAmt*(-1);
                    sumYAmt = new BigDecimal(0);
                    sumYAmt = sumYAmt.add(new BigDecimal(sumAmt));
                }
                callNode = "03";//????????????
            }else{
                prpLWfTaskVoList = wfTaskHandleService.findInTaskVo(registNo,compensateNo,FlowNode.PrePay.name());
            }
            
            if(prpLWfTaskVoList != null && prpLWfTaskVoList.size() > 0){
                prpLWfTaskVo = prpLWfTaskVoList.get(0);
            }
            prpLWfTaskVo.setRiskCode(riskCode);
            prpLWfTaskVo.setComCode(comCode);
            List<PrpLCompensateVo> compensateVoList = compensateTaskService.findCompListByClaimNo(prpLWfTaskVo.getClaimNo(),"Y");
            
            if(compensateVoList != null && compensateVoList.size() > 0){
                for(PrpLCompensateVo compensateVo : compensateVoList){
                    sumYAmt = sumYAmt.add(DataUtils.NullToZero(compensateVo.getSumAmt()));
                }
            }
            
            prpLWfTaskVo.setMoney(sumYAmt);
         
            //??????????????????????????????
            Map<String,Object> params = new HashMap<String,Object>();
            params.put("registNo",registNo);
            params.put("currentNode",currentNode);
            BigDecimal taskId = BigDecimal.ZERO;
            if(!isFirstPrePayWf){
            	taskId = prpLWfTaskVo.getTaskId();
            }
            if(taskId == null){
                params.put("taskId",BigDecimal.ZERO.doubleValue());
            } else{
                params.put("taskId",taskId.doubleValue());
            }
            String isSubmitHeadOffice = managerService.isSubmitHeadOffice(params);
            
            LIlogRuleResVo vPriceResVo = conpensateHandleServiceIlogService.organizaForPrePay(prpLWfTaskVo,"2",callNode,FlowNode.valueOf(currentNode).name(),userVo,isSubmitHeadOffice);
            nextVo.setSubmitLevel(Integer.parseInt(vPriceResVo.getMinUndwrtNode()));
            Integer backLevel = nextVo.getSubmitLevel();
            if(backLevel>=9){
                backLevel = Integer.parseInt(vPriceResVo.getMaxUndwrtNode());
            }
            
            if("1101".equals(riskCode)){
                nextNode = "VClaim_CI_LV"+backLevel;
            }else{
                nextNode = "VClaim_BI_LV"+backLevel;
            }
            int maxLevel = Integer.parseInt(vPriceResVo.getMaxUndwrtNode());
            boolean haveUser = false;
            for(int i = nextVo.getSubmitLevel(); i< maxLevel;i++){
                haveUser = assignService.existsGradeUser(FlowNode.valueOf(nextNode), WebUserUtils.getComCode());
                if(!haveUser){
                    if("1101".equals(riskCode)){
                        nextNode = "VClaim_CI_LV"+(i+1);
                    }else{
                        nextNode = "VClaim_BI_LV"+(i+1);
                    }
                }else{
                    break;
                }
            }
            //ilog????????????end====================
        }//else{
        PrpLConfigValueVo configRuleValueVo = ConfigUtil.findConfigByCode(CodeConstants.RULEVALIDFLAG,comCode);
        if("1".equals(configRuleValueVo.getConfigValue())){
            VerifyClaimRuleVo vcRuleVo = claimRuleApiService.compToVClaim(verifyClaimRuleVo);
            Integer backLevel = vcRuleVo.getBackLevel();
            if(backLevel>=9){
                backLevel = vcRuleVo.getMaxLevel();
            }
            if("1101".equals(riskCode)){
                nextNode = "VClaim_CI_LV"+backLevel;
            }else{
                nextNode = "VClaim_BI_LV"+backLevel;
            }
            boolean haveUser = false;
            for(int i=backLevel;i<vcRuleVo.getMaxLevel();i++){
                haveUser = assignService.existsGradeUser(FlowNode.valueOf(nextNode), WebUserUtils.getComCode());
                if(!haveUser){
                    if("1101".equals(riskCode)){
                        nextNode = "VClaim_CI_LV"+(i+1);
                    }else{
                        nextNode = "VClaim_BI_LV"+(i+1);
                    }
                }else{
                    break;
                }
            }
        }

        
		nextVo.setNextNode(FlowNode.valueOf(nextNode).name());
		nextVo.setNextName(FlowNode.valueOf(nextNode).getName());
		nextVo.setCurrentName(FlowNode.valueOf(currentNode).getName());
		nextVo.setCurrentNode(FlowNode.valueOf(currentNode).name());
		nextVo.setAuditStatus(auditStatus);
		// nextVo.setFlowId(flowId);
		
		// ??????????????????????????????  ////////////////////////////////////////
		PrpLWfTaskVo oldTaskVo = prePadHandleService.findTaskIn(registNo, compensateNo, FlowNode.valueOf(nextNode));
		if(oldTaskVo != null && nextNode.equals(oldTaskVo.getYwTaskType())){
			nextVo.setAssignUser(oldTaskVo.getHandlerUser());
			nextVo.setAssignCom(oldTaskVo.getHandlerCom());
		}else{
		SysUserVo assUserVo = assignService.execute(FlowNode.valueOf(nextNode),WebUserUtils.getComCode(),WebUserUtils.getUser(), "");
			if (assUserVo != null) {
				nextVo.setAssignUser(assUserVo.getUserCode());
				nextVo.setAssignCom(assUserVo.getComCode());
			}
		}
		nextVo.setComCode(WebUserUtils.getComCode());
		mav.addObject("nextVo",nextVo);

		mav.setViewName("prePay/SubmitVClaimNext");

		return mav;
	}

	/**
	 * ????????????????????????
	 * @param registNo
	 * @return
	 * @modified: ???XMSH(2016???4???18??? ??????8:11:18): <br>
	 */
	@RequestMapping("/prePayWriteOff.do")
	public ModelAndView prePayWriteOff(String registNo,String prePayNo,String claimNo) {
		ModelAndView modelAndView = new ModelAndView();

		PrpLClaimVo prpLClaimVo = claimService.findByClaimNo(claimNo);
		String handlerStatus = "0";
		String workStatus = "0";

		// ????????????????????????????????????
		List<PrpLCompensateVo> compensateVos = compensateTaskService.findCompListByClaimNo(claimNo,"Y");

		// ???????????????
		BigDecimal sumAllAmt = new BigDecimal(0);
		if(compensateVos!=null&&compensateVos.size()>0){
			for(PrpLCompensateVo compVo:compensateVos){
				sumAllAmt = sumAllAmt.add(compVo.getSumAmt());
			}
		}

		// ??????????????????
		PrpLCMainVo prpLCMainVo = policyViewService.getPolicyInfo(prpLClaimVo.getRegistNo(),prpLClaimVo.getPolicyNo());

		// ??????????????????
		PrpLRegistVo prpLregistVo = registQueryService.findByRegistNo(prpLClaimVo.getRegistNo());

		List<PrpLPrePayVo> prePayPVos = null;
		List<PrpLPrePayVo> prePayFVos = null;
		PrpLCompensateVo compensateVo = compensateTaskService.findCompByPK(prePayNo);

	/*	String oppoNo = compensateVo.getPrpLCompensateExt().getOppoCompensateNo();
		if(compensateVo.getPrpLCompensateExt()!=null&&StringUtils.isNotBlank(oppoNo)){
			handlerStatus = "3";
			workStatus = "3";
			FlowNode nodeCode = Risk.DQZ.equals(prpLClaimVo.getRiskCode()) ? FlowNode.PrePayWfCI : FlowNode.PrePayWfBI;
			List<PrpLWfTaskVo> oldTaskVoList = wfTaskHandleService.findEndTask(registNo, oppoNo, nodeCode);
			if(!oldTaskVoList.isEmpty()){
				return prePayWf(oldTaskVoList.get(0).getTaskId().toString());
			}
		}
*/		
		BigDecimal sumAmt = BigDecimal.ZERO;
		try{
			//??????????????????????????????
			prePayPVos =  compensateTaskService.getPrePayWriteVo(prePayNo,"P");
			//??????????????????????????????
			prePayFVos =  compensateTaskService.getPrePayWriteVo(prePayNo,"F");
			
			if(prePayPVos != null && !prePayPVos.isEmpty())
			{
				for(PrpLPrePayVo prePayPVo: prePayPVos){
					sumAmt = sumAmt.add(prePayPVo.getPayAmt());
					prePayPVo.setMaxAmt(prePayPVo.getPayAmt());
				}		
			}
			
			if(prePayFVos != null && !prePayFVos.isEmpty()){
				for(PrpLPrePayVo prePayFVo: prePayFVos){
					sumAmt = sumAmt.add(prePayFVo.getPayAmt());
					prePayFVo.setMaxAmt(prePayFVo.getPayAmt());
				}
				
			}
		}catch(Exception e){
			logger.error("?????????="+ prePayNo + ",?????????" + claimNo + "??????????????????????????????????????????????????????",e);
		}
//		ConfigUtil configUtil = new ConfigUtil(); 
			PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.STARFLAG);

			if(configValueVo!=null && "1".equals(configValueVo.getConfigValue())){
			//	registVo.insuredPhone 
				//prpLregistVo.setInsuredPhone(DataUtils .replacePrivacy(prpLregistVo.getInsuredPhone()));
			//	registVo.linkerMobile
				String[] mobiles = prpLregistVo.getLinkerMobile().split(",");
				StringBuffer str = new StringBuffer();
				for (int i = 0; i < mobiles.length; i++) {
					str .append(DataUtils.replacePrivacy(mobiles[i]));
					str.append(",");
				}
				prpLregistVo.setLinkerMobile(str.substring(0, str.lastIndexOf(",")).toString());
				if(prpLregistVo.getLinkerMobile() != null){
					prpLregistVo.setLinkerPhone(DataUtils.replacePrivacy(prpLregistVo.getLinkerMobile()));
				}
			}
	
		modelAndView.addObject("sumAmt",sumAmt);
		modelAndView.addObject("sumAllAmt",sumAllAmt);
		modelAndView.addObject("handlerStatus",handlerStatus);
		modelAndView.addObject("workStatus",workStatus);
		modelAndView.addObject("prePayPVos",prePayPVos);
		modelAndView.addObject("prePayFVos",prePayFVos);
		modelAndView.addObject("compensateVo",compensateVo);
		modelAndView.addObject("compensateVos",compensateVos);
		modelAndView.addObject("prpLCMainVo",prpLCMainVo);
		modelAndView.addObject("prpLregistVo",prpLregistVo);
		modelAndView.addObject("prpLClaimVo",prpLClaimVo);
		modelAndView.addObject("wfNode","PrePayWf");
		modelAndView.setViewName("prePay/PrePayWriteOff");

		return modelAndView;
	}

	@RequestMapping("/prePayWf.do")
	public ModelAndView prePayWf(String flowTaskId) {
		ModelAndView modelAndView = new ModelAndView();

		PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(Double.parseDouble(flowTaskId));

		String prePayNo = wfTaskVo.getCompensateNo();// ?????????
		String claimNo = wfTaskVo.getClaimNo();// ?????????
		String handlerStatus = wfTaskVo.getHandlerStatus();
		String workStatus = wfTaskVo.getWorkStatus();

		PrpLClaimVo prpLClaimVo = claimService.findByClaimNo(claimNo);

		// ????????????????????????????????????
		List<PrpLCompensateVo> compensateVos = compensateTaskService.findCompListByClaimNo(claimNo,"Y");

		// ???????????????
		BigDecimal sumAllAmt = new BigDecimal(0);
		if(compensateVos!=null&&compensateVos.size()>0){
			for(PrpLCompensateVo compVo:compensateVos){
				sumAllAmt = sumAllAmt.add(compVo.getSumAmt());
			}
		}

		// ??????????????????
		PrpLCMainVo prpLCMainVo = policyViewService.getPolicyInfo(prpLClaimVo.getRegistNo(),prpLClaimVo.getPolicyNo());

		// ??????????????????
		PrpLRegistVo prpLregistVo = registQueryService.findByRegistNo(prpLClaimVo.getRegistNo());

		List<PrpLPrePayVo> prePayPVos = null;
		List<PrpLPrePayVo> prePayFVos = null;
		PrpLCompensateVo compensateVo = compensateTaskService.findCompByPK(prePayNo);
		prePayPVos = compensateTaskService.getPrePayVo(prePayNo,"P");
		prePayFVos = compensateTaskService.getPrePayVo(prePayNo,"F");
		List<PrpLPrePayVo> oldPrePayPVos = null;
		List<PrpLPrePayVo> oldPrePayFVos = null;
		if(compensateVo.getPrpLCompensateExt()!= null && StringUtils.isNotBlank(compensateVo.getPrpLCompensateExt().getOppoCompensateNo())){
			try{
				oldPrePayPVos = compensateTaskService.getRemainPrePayWriteVo(compensateVo.getPrpLCompensateExt().getOppoCompensateNo(),"P");
				oldPrePayFVos = compensateTaskService.getRemainPrePayWriteVo(compensateVo.getPrpLCompensateExt().getOppoCompensateNo(),"F");
				if(prePayPVos != null && !prePayPVos.isEmpty() && oldPrePayPVos != null && !oldPrePayPVos.isEmpty()){
					for(PrpLPrePayVo prpLPrePayVo :prePayPVos){
						for(PrpLPrePayVo oldPrpLPrePayVo:oldPrePayPVos){
							if(oldPrpLPrePayVo.getChargeCode().equals(prpLPrePayVo.getChargeCode())
									&& (oldPrpLPrePayVo.getLossName().equals(prpLPrePayVo.getLossName()))){
								prpLPrePayVo.setMaxAmt(oldPrpLPrePayVo.getPayAmt());
							}
						}
					}
				}
				if(prePayFVos != null && !prePayFVos.isEmpty()&& oldPrePayFVos != null && !oldPrePayFVos.isEmpty()){

					for(PrpLPrePayVo oldPrpLPrePayVo:oldPrePayFVos){
						for(PrpLPrePayVo prpLPrePayVo :prePayFVos){
							if(oldPrpLPrePayVo.getChargeCode().equals(prpLPrePayVo.getChargeCode())){
								prpLPrePayVo.setMaxAmt(oldPrpLPrePayVo.getPayAmt());
							}
						}
					}
				}
			}catch(Exception e){
				logger.error("?????????="+ prePayNo + ",?????????" + claimNo + "??????????????????????????????????????????????????????",e);
			}
		}
//		ConfigUtil configUtil = new ConfigUtil(); 
			PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.STARFLAG);

			if(configValueVo!=null && "1".equals(configValueVo.getConfigValue())){
			//	registVo.insuredPhone 
				//prpLregistVo.setInsuredPhone(DataUtils .replacePrivacy(prpLregistVo.getInsuredPhone()));
			//	registVo.linkerMobile
				String[] mobiles = prpLregistVo.getLinkerMobile().split(",");
				StringBuffer str = new StringBuffer();
				for (int i = 0; i < mobiles.length; i++) {
					str .append(DataUtils.replacePrivacy(mobiles[i]));
					str.append(",");
				}
				prpLregistVo.setLinkerMobile(str.substring(0, str.lastIndexOf(",")).toString());
				if(prpLregistVo.getLinkerMobile() != null){
					prpLregistVo.setLinkerPhone(DataUtils.replacePrivacy(prpLregistVo.getLinkerMobile()));
				}
			}
		//????????????????????????
		try{
			if(compensateVo != null && compensateVo.getPrpLCompensateExt() != null && compensateVo.getPrpLCompensateExt().getOppoCompensateNo() != null){
				List<Map<String,Object>>  list = compensateTaskService.getPrePayWfHis(compensateVo.getPrpLCompensateExt().getOppoCompensateNo());
				modelAndView.addObject("writeOffList",list);
			}
		}catch(Exception e){
			logger.error("??????????????????????????????",e);
			modelAndView.addObject("writeOffList",null);
		}
		modelAndView.addObject("sumAllAmt",sumAllAmt);
		modelAndView.addObject("handlerStatus",handlerStatus);
		modelAndView.addObject("flowTaskId",flowTaskId);
		modelAndView.addObject("workStatus",workStatus);
		modelAndView.addObject("prePayPVos",prePayPVos);
		modelAndView.addObject("prePayFVos",prePayFVos);
		modelAndView.addObject("compensateVo",compensateVo);
		modelAndView.addObject("compensateVos",compensateVos);
		modelAndView.addObject("prpLCMainVo",prpLCMainVo);
		modelAndView.addObject("prpLregistVo",prpLregistVo);
		modelAndView.addObject("prpLClaimVo",prpLClaimVo);
		modelAndView.setViewName("prePay/PrePayWriteOff");

		return modelAndView;
	}

	@RequestMapping(value = "/submitPrePayWriteOff.do", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult submitPrePayWriteOff(@FormModel("compensateVo") PrpLCompensateVo compVo,@FormModel("prePayFVo") List<PrpLPrePayVo> prePayFVos,
	                                       @FormModel("prePayPVo") List<PrpLPrePayVo> prePayPVos,
											@FormModel("submitNextVo") SubmitNextVo submitNextVo) {
		AjaxResult ajaxResult = new AjaxResult();
		try{
			SysUserVo userVo = WebUserUtils.getUser();
			 prePadHandleService.submitPrePayWriteOff(compVo, submitNextVo,userVo,prePayPVos,prePayFVos);
			ajaxResult.setStatus(HttpStatus.SC_OK);
		}
		catch(Exception e){
			ajaxResult.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			ajaxResult.setStatusText(e.getMessage());
			e.printStackTrace();
		}

		return ajaxResult;
	}

	@RequestMapping(value = "/submitPrePayWf.do", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult submitPrePayWf(@FormModel("compensateVo") PrpLCompensateVo compVo,@FormModel("prePayFVo") List<PrpLPrePayVo> prePayFVos,
                                     @FormModel("prePayPVo") List<PrpLPrePayVo> prePayPVos,
										@FormModel("submitNextVo") SubmitNextVo submitNextVo) {
		AjaxResult ajaxResult = new AjaxResult();

		SysUserVo userVo = WebUserUtils.getUser();
		try{
			PrpLCompensateVo oldCompVo = compensateTaskService.findCompByPK(compVo.getCompensateNo());
			//????????????????????????
			oldCompVo = prePadHandleService.updatePrePayWf(oldCompVo,prePayFVos,prePayPVos,userVo);
			PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(Double.valueOf(submitNextVo.getFlowTaskId()));

			WfTaskSubmitVo submiVClaimVo = new WfTaskSubmitVo();

			submiVClaimVo.setFlowTaskId(wfTaskVo.getTaskId());
			submiVClaimVo.setTaskInKey(wfTaskVo.getTaskInKey());
			submiVClaimVo.setTaskInUser(WebUserUtils.getUserCode());
			submiVClaimVo.setFlowId(wfTaskVo.getFlowId());
			// submiVClaimVo.setCurrentNode(FlowNode.VClaim);
			submiVClaimVo.setComCode(policyViewService.getPolicyComCode(oldCompVo.getRegistNo()));
			if("1101".equals(oldCompVo.getRiskCode())){
				submiVClaimVo.setCurrentNode(FlowNode.PrePayWfCI);
			}else{
				submiVClaimVo.setCurrentNode(FlowNode.PrePayWfBI);
			}
			submiVClaimVo.setNextNode(FlowNode.valueOf(submitNextVo.getNextNode()));
			
			//SysUserVo userVo = assignService.execute(submiVClaimVo.getNextNode(),WebUserUtils.getComCode());

			submiVClaimVo.setAssignUser(submitNextVo.getAssignUser());
			submiVClaimVo.setAssignCom(submitNextVo.getAssignCom());

			wfTaskHandleService.submitPrepay(oldCompVo,submiVClaimVo);

			ajaxResult.setStatus(HttpStatus.SC_OK);
		}
		catch(Exception e){
			ajaxResult.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			ajaxResult.setStatusText(e.getMessage());
			e.printStackTrace();
		}

		return ajaxResult;
	}

	@RequestMapping(value = "/prePayCancel.do", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult prePayCancel(String taskId) {
		AjaxResult ajaxResult = new AjaxResult();
		SysUserVo user = WebUserUtils.getUser();
		try{
			compensateTaskService.prePayCancel(taskId,user);
			ajaxResult.setStatus(HttpStatus.SC_OK);
		}
		catch(Exception e){
			ajaxResult.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			ajaxResult.setStatusText(e.getMessage());
			e.printStackTrace();
		}
		return ajaxResult;
	}
	
	@RequestMapping("/prePayToClaimView.do")
	@ResponseBody
	public AjaxResult prePayToClaimView(String registNo,String sign){//sign?????????????????????????????????0????????????1?????????
		AjaxResult ajax=new AjaxResult();
		BigDecimal taskId=null;
		try{
			List<PrpLWfTaskVo> prpLWfTaskVos =wfTaskHandleService.findWftaskByRegistNoAndNodeCode(registNo,FlowNode.Claim.toString());
		     if(prpLWfTaskVos!=null && prpLWfTaskVos.size()>0){
		    	 for(PrpLWfTaskVo prpVo:prpLWfTaskVos){
		    		 if(sign.equals("0") && "ClaimBI".equals(prpVo.getSubNodeCode())){
		    			 taskId=prpVo.getTaskId();
		    			 break;
		    		 }
		    		 if(sign.equals("1") && "ClaimCI".equals(prpVo.getSubNodeCode())){
		    			 taskId=prpVo.getTaskId();
		    			 break;
		    		 }
		    	 }
		    	
			  
		     }
		     ajax.setData(taskId);
		     ajax.setStatus(HttpStatus.SC_OK);
		
		  }catch(Exception e){
			ajax.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			e.printStackTrace();
		 }
		return ajax;
		
	}
	
	@RequestMapping(value = "/isCanWriteOff.ajax", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult isCanWriteOff(@FormModel("compensateNo") String compensateNo,
									@FormModel("registNo") String registNo) {
		AjaxResult ajaxResult = new AjaxResult();
		List<PrpLPrePayVo> prePayVos = compensateTaskService.findPrePayList(compensateNo);
		if(prePayVos != null && prePayVos.size() > 0){
			for(PrpLPrePayVo vo : prePayVos){
				if(PayStatus.PAID.equals(vo.getPayStatus())){
					ajaxResult.setData("??????????????????????????????????????????????????????");
					ajaxResult.setStatus(HttpStatus.SC_OK);
					return ajaxResult;
				}
			}
		}		
		ajaxResult.setData("1");
		ajaxResult.setStatus(HttpStatus.SC_OK);
		return ajaxResult;
		
	}
    
	 /**
		 * ??????????????????
		 * Date ???????????? String??????
		 * @param strDate
		 * @return
		 * @throws ParseException2020-05-27??14:45:02
		 */
		private static Date DateBillString(String strDate){
			Date date=null;
			SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			  if(strDate!=null){
				  try {
					date=format.parse(strDate);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return date;
		}

}
