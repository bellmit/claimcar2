/******************************************************************************
 * CREATETIME : 2016年1月12日 上午10:17:04
 ******************************************************************************/
package ins.sino.claimcar.lossperson.web.action;

import ins.framework.service.CodeTranService;
import ins.framework.utils.Beans;
import ins.framework.web.AjaxResult;
import ins.framework.web.bind.annotation.FormModel;
import ins.platform.common.service.facade.CodeDictService;
import ins.platform.common.util.CodeTranUtil;
import ins.platform.common.util.ConfigUtil;
import ins.platform.common.web.util.WebUserUtils;
import ins.platform.saa.service.facade.SaaUserPowerService;
import ins.platform.saa.vo.SaaUserPowerVo;
import ins.platform.sysuser.service.facade.SysUserService;
import ins.platform.utils.DataUtils;
import ins.platform.vo.PrpLConfigValueVo;
import ins.platform.vo.SysCodeDictVo;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.AuditStatus;
import ins.sino.claimcar.CodeConstants.CheckClass;
import ins.sino.claimcar.CodeConstants.WorkStatus;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.PrpLCheckDutyVo;
import ins.sino.claimcar.claim.service.ClaimTaskService;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.claim.service.InterfaceAsyncService;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.common.claimtext.service.ClaimTextService;
import ins.sino.claimcar.common.claimtext.vo.PrpLClaimTextVo;
import ins.sino.claimcar.common.losscharge.service.LossChargeService;
import ins.sino.claimcar.common.losscharge.vo.PrpLDlossChargeVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.AssignService;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.losscar.service.DeflossHandleService;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.lossperson.service.PersTraceHandleService;
import ins.sino.claimcar.lossperson.service.PersTraceService;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersExtVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceMainVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceVo;
import ins.sino.claimcar.lossperson.vo.SubmitNextVo;
import ins.sino.claimcar.manager.service.ManagerService;
import ins.sino.claimcar.manager.vo.PrpdIntermMainVo;
import ins.sino.claimcar.mobilecheck.service.SendMsgToMobileService;
import ins.sino.claimcar.other.service.AcheckService;
import ins.sino.claimcar.other.service.AssessorDubboService;
import ins.sino.claimcar.other.service.AssessorService;
import ins.sino.claimcar.other.vo.PrpLAcheckVo;
import ins.sino.claimcar.other.vo.PrpLAssessorFeeVo;
import ins.sino.claimcar.other.vo.PrpLAssessorMainVo;
import ins.sino.claimcar.platform.service.LossToPlatformService;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.service.ScheduleTaskService;
import ins.sino.claimcar.regist.vo.PrpLCItemKindVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
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

/**
 * 人伤跟踪审核和费用审核
 * @author ★XMSH
 */
@Controller
@RequestMapping("/persTraceVerify")
public class PersTraceVerifyAction {

	private static Logger logger = LoggerFactory.getLogger(PersTraceVerifyAction.class);

	@Autowired
	private PersTraceService persTraceService;
	@Autowired
	private ClaimTextService claimTextSerVice;
	@Autowired
	private LossChargeService lossChargeService;
	@Autowired
	private WfTaskHandleService wfTaskHandleService;
	@Autowired
	private CheckTaskService checkTaskService;
	@Autowired
	private CodeDictService codeDictService;
	@Autowired
	private CodeTranService codeTranService;
	@Autowired
	private PolicyViewService policyViewService;
	@Autowired
	private RegistQueryService registQueryService;
	@Autowired
	private ClaimTaskService claimTaskService;
	@Autowired
	private LossToPlatformService lossToPlatformService;
	@Autowired
	private AssignService assignService;
	@Autowired
	private AssessorDubboService assessorService;
	@Autowired
	private LossCarService lossCarService;
	@Autowired
	private PersTraceHandleService persTraceHandleService;
	@Autowired
	private DeflossHandleService deflossHandleService;
	@Autowired
	private CompensateTaskService compensateTaskService;
	
	@Autowired
	private AssessorService assessorServices;
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SaaUserPowerService saaUserPowerService;
	@Autowired
	private ManagerService managerService;
	@Autowired
	InterfaceAsyncService interfaceAsyncService;
	@Autowired
	ScheduleTaskService scheduleTaskService;

    @Autowired
	private AcheckService acheckService;
	@Autowired
	SendMsgToMobileService sendMsgToMobileService;
	/**
	 * 人伤跟踪审核数据初始化
	 * @param request
	 * @param registNo
	 * @param riskCode
	 * @return
	 * @modified: ☆XMSH(2016年1月12日 上午10:27:43): <br>
	 */
	@RequestMapping(value = "/personTraceVerify.do", method = RequestMethod.GET)
	public ModelAndView personTraceVerify(Double flowTaskId) {
		logger.info("人伤跟踪审核数据初始化======flowTaskId:===="+flowTaskId);
		String rodeNode="PLCharge";
		PrpLWfTaskVo taskVo = wfTaskHandleService.queryTask(flowTaskId);
		rodeNode=FlowNode.valueOf(taskVo.getSubNodeCode()).getUpperNode();
		String registNo = taskVo.getRegistNo();
		String handlerStatus = taskVo.getHandlerStatus();
		PrpLDlossPersTraceMainVo prpLDlossPersTraceMainVo = null;// 人伤主表
		List<PrpLDlossPersTraceVo> persTraceVos = null;// 人员跟踪表
		PrpLClaimTextVo prpLClaimTextVo = null;// 当前节点意见
		List<PrpLDlossChargeVo> prpLDlossChargeVos = null;// 费用赔款信息
		List<PrpLClaimTextVo> prpLClaimTextVos = null; // 意见列表
		int persTracesNum = 0;// 首次跟踪人伤数目为0
		List<String> injuredParts = new ArrayList<String>();// 选中的受伤部位
		PrpLCheckDutyVo prpLCheckDutyVo = null;// 车辆事故责任表
		String isPLBigEnd = "";// 大案审核是否通过
		ModelAndView mav = new ModelAndView();
		SysUserVo userVo = WebUserUtils.getUser();
		String intermediaryFlag = "";
		String intermCode ="";

		prpLDlossPersTraceMainVo = persTraceService.findPersTraceMainVoById(Long.decode(taskVo.getHandlerIdKey()));
		PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
		
		/*
		List<PrpLWfTaskVo>  prpLWfTaskList = wfTaskHandleService.findPrpLWfTaskOutByRegistNo(taskVo.getRegistNo());
		PrpLWfTaskVo prpLWfTaskVo =findPrpLWfTaskVo(prpLWfTaskList,taskVo.getUpperTaskId());
		String userCode = prpLWfTaskVo.getHandlerUser();
        SysUserVo user = sysUserService.findByUserCode(userCode);
        prpLDlossPersTraceMainVo.setPlfName(user.getUserName());// 跟踪人姓名
        prpLDlossPersTraceMainVo.setPlfCertiCode(user.getIdentifyNumber());// 跟踪人身份证号
		*/
		if(StringUtils.isBlank(prpLDlossPersTraceMainVo.getVerifyCertiCode())){
			prpLDlossPersTraceMainVo.setVerifyCertiCode(userVo.getIdentifyNumber());
		}

		if("0".equals(handlerStatus)){// 审核未处理，点击变成正在处理
			wfTaskHandleService.tempSaveTask(flowTaskId,prpLDlossPersTraceMainVo.getId().toString(),
					WebUserUtils.getUserCode(),WebUserUtils.getComCode());
		}

		if("1".equals(prpLDlossPersTraceMainVo.getMajorcaseFlag())){// 人伤大案审核是否审核通过
			List<PrpLWfTaskVo> wfChkBigTaskVoList = wfTaskHandleService.findEndTask(registNo,null,FlowNode.PLBig);
			if(wfChkBigTaskVoList==null||wfChkBigTaskVoList.size()==0){
				isPLBigEnd = "N";
			}else{
				isPLBigEnd = "Y";
			}
		}

		prpLClaimTextVo = claimTextSerVice.findClaimTextByNode(prpLDlossPersTraceMainVo.getId(),
				taskVo.getSubNodeCode(),"0");
		prpLClaimTextVos = claimTextSerVice.findClaimTextList(prpLDlossPersTraceMainVo.getId(),
				prpLDlossPersTraceMainVo.getRegistNo(),FlowNode.PLoss.name());
		prpLDlossChargeVos = lossChargeService
				.findLossChargeVos(prpLDlossPersTraceMainVo.getId(),FlowNode.PLoss.name());
		persTraceVos = persTraceService.findPersTraceVo(registNo,prpLDlossPersTraceMainVo.getId());
		if(persTraceVos!=null){
			persTracesNum = persTraceVos.size();
			for(PrpLDlossPersTraceVo persTraceVo:persTraceVos){
				List<PrpLDlossPersExtVo> persExtVos = persTraceVo.getPrpLDlossPersInjured().getPrpLDlossPersExts();
				String injuredPart = "";
				for(PrpLDlossPersExtVo persExt:persExtVos){
					injuredPart += persExt.getInjuredPart()+",";
				}
				injuredParts.add(injuredPart);
			}
		}

		// 费用险别
		List<SysCodeDictVo> dictVos = new ArrayList<SysCodeDictVo>();
		List<PrpLCItemKindVo> itemKinds = policyViewService.findItemKinds(registNo,"Y");
		for(PrpLCItemKindVo itemKind:itemKinds){
			SysCodeDictVo dictVo = new SysCodeDictVo();
			dictVo.setCodeCode(itemKind.getKindCode());
			dictVo.setCodeName(itemKind.getKindName());
			dictVos.add(dictVo);
		}
		mav.addObject("dictVos",dictVos);

		// 查询其他节点的交强险责任类型和事故责任比例
		prpLCheckDutyVo = checkTaskService.findCheckDuty(registNo,1);
		if(prpLCheckDutyVo==null||prpLCheckDutyVo.getId()==null){
			prpLCheckDutyVo = new PrpLCheckDutyVo();
			prpLCheckDutyVo.setRegistNo(registNo);
			prpLCheckDutyVo.setSerialNo(1);
		}

		if(prpLClaimTextVo==null||prpLClaimTextVo.getId()==null){
			prpLClaimTextVo = new PrpLClaimTextVo();
			prpLClaimTextVo.setRegistNo(registNo);
			prpLClaimTextVo.setTextType(CodeConstants.ClaimText.OPINION);
			prpLClaimTextVo.setNodeCode(taskVo.getSubNodeCode());
		}

		if(WorkStatus.END.equals(handlerStatus)||WorkStatus.BACK.equals(handlerStatus)){// 已处理或已退回
			if(prpLClaimTextVos!=null && prpLClaimTextVos.size()>0){
				prpLClaimTextVo = prpLClaimTextVos.get(prpLClaimTextVos.size()-1);
			}
			
		}
		//脱敏处理
		if(CodeConstants.WorkStatus.END.equals(taskVo.getWorkStatus())){
			//reportorPhone
			PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.STARFLAG);
			if(configValueVo!=null && "1".equals(configValueVo.getConfigValue()) 
					&& persTraceVos != null && persTraceVos.size() > 0){//开关
				for(PrpLDlossPersTraceVo vo : persTraceVos){
					vo.getPrpLDlossPersInjured().setCertiCode(DataUtils.replacePrivacy(vo.getPrpLDlossPersInjured().getCertiCode()));
					vo.getPrpLDlossPersInjured().setPhoneNumber(DataUtils.replacePrivacy(vo.getPrpLDlossPersInjured().getPhoneNumber()));
				}
			}
		}
		// 标的车和三者车牌号
		Map<String,String> dataSourceMap = checkTaskService.getCarLossParty(registNo);
		// 修改bug，增加单独调度出来的三者车辆
		List<PrpLDlossCarMainVo> carMainVoList = deflossHandleService.findLossCarMainByRegistNo(registNo);
		if(carMainVoList!=null&& !carMainVoList.isEmpty()){
			for(PrpLDlossCarMainVo carMainVo:carMainVoList){
				Integer serialNo = carMainVo.getSerialNo();
				if( !dataSourceMap.isEmpty()&& !dataSourceMap.containsKey(serialNo.toString())){
					String serialNoStr = serialNo==1 ? "标的车" : "三者车";
					dataSourceMap.put(serialNo.toString(),serialNoStr+"("+carMainVo.getLicenseNo()+")");
				}
			}
		}
       
		mav.addObject("registNo",registNo);
		mav.addObject("handlerStatus",handlerStatus);
		mav.addObject("flowTaskId",flowTaskId);
		mav.addObject("flowNodeCode",taskVo.getSubNodeCode());
		mav.addObject("flowNodeName",taskVo.getTaskName());
		mav.addObject("dataSourceMap",dataSourceMap);

		//ConfigUtil configUtil = new ConfigUtil(); 
		PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.ASSESSORFEE,WebUserUtils.getComCode());
		String configValue = "0";
		if(configValueVo!=null){
			configValue = configValueVo.getConfigValue();
		}
		
		mav.addObject("configValue", configValue);  //公估费开关
		
		prpLDlossPersTraceMainVo = persTraceService.calculateSumAmt(prpLDlossPersTraceMainVo);
		
	    
		 //核损清单打印
	       List<PrpLDlossCarMainVo> carMainVos= lossCarService.findLossCarMainByRegistNo(registNo);
	       String lossCarSign="0";//核损清单打印按钮是否置灰标志
	       if(carMainVos!=null && carMainVos.size()>0){
	    	   for(PrpLDlossCarMainVo vo:carMainVos){
	    		   if("1".equals(vo.getUnderWriteFlag())){
	    			   lossCarSign="1";
	    			   break;
	    		   }
	    	   }
	       }
	       
	       //理算计算书打印和赔款收据打印
	       String compensateSign="0";//页面按钮是否置灰的标志
	        List<PrpLCompensateVo> compensates=compensateTaskService.findCompensatevosByRegistNo(registNo);
	     	   if(compensates!=null && compensates.size()>0){
	     		   for(PrpLCompensateVo vo:compensates){
	     	        if("N".equals(vo.getCompensateType())){
	     	        	compensateSign="1";
	     	        	break;
	     	        }
	     			      
	     	         }
	     	      
	     	   }
	    	   
	    //页面公估费查看按钮是否亮显
	   	String assessSign="0";
	   	List<PrpLAssessorFeeVo> listFeeVo=assessorServices.findPrpLAssessorFeeVoByRegistNo(registNo);
	   	if(listFeeVo!=null && listFeeVo.size()>0){
	   		for(PrpLAssessorFeeVo vo :listFeeVo){
				PrpLAssessorMainVo assessMainVo=assessorServices.findAssessorMainVoById(vo.getAssessMainId());
				if(assessMainVo!=null && "3".equals(assessMainVo.getUnderWriteFlag())){
					assessSign="1";
					break;
				}
			}
	   	}
	  //重开赔案后的人伤不显示是否减损等选项
  		String reOpenFlag="0";//0-重开前，1-重开后
  		PrpLWfTaskVo taskVo2=wfTaskHandleService.findOutWfTaskVo(FlowNode.ReOpen.name(),registNo);
  		if(taskVo2!=null){
  			if(taskVo.getTaskInTime().getTime()>=taskVo2.getTaskOutTime().getTime()){
  				reOpenFlag="1";
  			}
  		}
  		if(prpLDlossPersTraceMainVo != null){
  			if(StringUtils.isNotBlank(prpLDlossPersTraceMainVo.getOperatorCode())){
  				PrpdIntermMainVo intermMainVo = managerService.findIntermByUserCode(prpLDlossPersTraceMainVo.getOperatorCode());
  				boolean isIntermediary = intermMainVo==null?false:true;
  				if(isIntermediary){
  					intermediaryFlag = CheckClass.CHECKCLASS_Y;
  					intermCode = intermMainVo.getIntermCode();
  				} else{
  					intermediaryFlag = CheckClass.CHECKCLASS_N;
  				}
  			}
  		}
	  	mav.addObject("reOpenFlag",reOpenFlag);
	   	mav.addObject("intermediaryFlag",intermediaryFlag);
	   	mav.addObject("intermCode",intermCode);
	   	mav.addObject("assessSign",assessSign);
        mav.addObject("compensateSign",compensateSign);
	    mav.addObject("lossCarSign",lossCarSign);
		mav.addObject("prpLDlossPersTraceMainVo",prpLDlossPersTraceMainVo);
		mav.addObject("prpLDlossPersTraces",persTraceVos);
		mav.addObject("prpLCheckDutyVo",prpLCheckDutyVo);
		mav.addObject("injuredParts",injuredParts);
		mav.addObject("lossChargeVos",prpLDlossChargeVos);
		mav.addObject("prpLClaimTextVo",prpLClaimTextVo);
		mav.addObject("prpLClaimTextVos",prpLClaimTextVos);
		mav.addObject("persTracesNum",persTracesNum);
		mav.addObject("isPLBigEnd",isPLBigEnd);
		mav.addObject("tabPageNo",0);
		mav.addObject("taskVo",taskVo);
		
		String comCode = userVo.getComCode();
		List<PrpLCMainVo> cMainVoList = policyViewService.findPrpLCMainVoListByRegistNo(registNo);
		if(cMainVoList != null && !cMainVoList.isEmpty()){
			comCode = cMainVoList.get(0).getComCode();
		}
		mav.addObject("comCode",comCode);//保单机构
        //判断是否有ILOG规则信息查看权限
        String nodeCode=taskVo.getSubNodeCode();
        String roleFlag="0";
		String grades="";
		SaaUserPowerVo saaUserPowerVo=saaUserPowerService.findUserPower(userVo.getUserCode());		
		List<FlowNode> flowNodeList=FlowNode.valueOf(nodeCode).getNextLevelNodes();
		for(FlowNode flowNode:flowNodeList){
			grades=grades+flowNode.getRoleCode()+",";
		}
		
		if(saaUserPowerVo!=null&&saaUserPowerVo.getRoleList().size()>0){			
			for(String gradeId:saaUserPowerVo.getRoleList()){
				if(grades.indexOf(gradeId)>-1){
					roleFlag="1";
					break;
				}
			}
		}
		mav.addObject("roleFlag",roleFlag);

		Boolean types = wfTaskHandleService.existTaskByNodeCode(registNo,FlowNode.Claim,registNo,"0");
		mav.addObject("types",types);
		mav.addObject("rodeNode", rodeNode);
		mav.addObject("registVo",registVo);
		mav.setViewName("lossperson/persTraceVerify/PersTraceVerify");

		return mav;
	}
	
	/**
	 * 根据upperTaskId查询跟踪任务
	 * <pre></pre>
	 * @param prpLWfTaskList
	 * @param upperTaskId
	 * @return
	 * @modified:
	 * ☆LinYi(2017年8月25日 下午1:26:52): <br>
	 */
	private PrpLWfTaskVo findPrpLWfTaskVo(List<PrpLWfTaskVo> prpLWfTaskList,BigDecimal upperTaskId) {
	    PrpLWfTaskVo prpLWfTask = new PrpLWfTaskVo();
	    if(prpLWfTaskList != null&&prpLWfTaskList.size() > 0){
            for(PrpLWfTaskVo prpLWfTaskVo:prpLWfTaskList){
                if(upperTaskId.equals(prpLWfTaskVo.getTaskId())){
                    if((!"PLFirst".equals(prpLWfTaskVo.getSubNodeCode())||!"PLNext".equals(prpLWfTaskVo.getSubNodeCode()))&&!"3".equals(prpLWfTaskVo.getWorkStatus())){
                        findPrpLWfTaskVo(prpLWfTaskList,prpLWfTaskVo.getUpperTaskId());  
                    }
                    else {
                        Beans.copy().from(prpLWfTaskVo).to(prpLWfTask);
                        break;
                    }
                }     
            }
        }
        return prpLWfTask;
    }
	
	/**
	 * 返回工作流表的taskid
	 * @param registNo
	 * @return 
	 */
	@RequestMapping(value = "/pLChargeView.do", method = RequestMethod.POST)
	@ResponseBody
    public AjaxResult pLChargeView(String registNo){
		AjaxResult ajaxResult =new AjaxResult();
		BigDecimal taskId=null;
		try{
		//先查PrpLwfTaskIn表
		List<PrpLWfTaskVo> prpLWfTaskVoList =wfTaskHandleService.findPrpLWfTaskInByRegistNo(registNo);
		if(prpLWfTaskVoList !=null&&prpLWfTaskVoList.size()>0){
			for(PrpLWfTaskVo prpLWfTaskVo:prpLWfTaskVoList){
				if(prpLWfTaskVo.getSubNodeCode().contains("PLCharge")){
					taskId=prpLWfTaskVo.getTaskId();
					
				}
			}
		}
		//查PrpLwfTaskOut表
		if(taskId==null){
			List<PrpLWfTaskVo> prpLWfTaskVoList1 =wfTaskHandleService.findPrpLWfTaskOutByRegistNo(registNo);
	          if(prpLWfTaskVoList1 !=null&&prpLWfTaskVoList1.size()>0){
				for(PrpLWfTaskVo prpLWfTaskVo:prpLWfTaskVoList1){
					if(prpLWfTaskVo.getSubNodeCode().contains("PLCharge") && !(prpLWfTaskVo.getWorkStatus().equals("7"))){
						taskId=prpLWfTaskVo.getTaskId();
						
					}
				}
			}
		}
		
		
			ajaxResult.setStatus(HttpStatus.SC_OK);
			ajaxResult.setData(taskId);
		}
		catch(Exception e){
			e.printStackTrace();
			ajaxResult.setStatus(HttpStatus.SC_EXPECTATION_FAILED);
		}
		
		return ajaxResult;
		
	}
	
	@RequestMapping(value = "/saveOrSubmit.do", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult saveOrSubmit(	@FormModel("prpLDlossPersTraceMainVo") PrpLDlossPersTraceMainVo prpLDlossPersTraceMainVo,
									@FormModel("prpLDlossPersTraceVos") List<PrpLDlossPersTraceVo> prpLDlossPersTraceVos,
									@FormModel("lossChargeVos") List<PrpLDlossChargeVo> prpLDlossChargeVos,
									@FormModel("prpLClaimTextVo") PrpLClaimTextVo prpLClaimTextVo,
									@FormModel("submitNextVo") SubmitNextVo submitNextVo) {
		AjaxResult ajaxResult = new AjaxResult();//audit
		try{
			SysUserVo userVo = WebUserUtils.getUser();
			String registNo = prpLDlossPersTraceMainVo.getRegistNo();
			
			if("audit".equals(submitNextVo.getAuditStatus())){
				//判断人伤跟踪审核或者人伤费用审核提交下一级时，如果当前为分公司最高级时，大案审核未通过，不能提交到总公司
			     PrpLDlossPersTraceMainVo  Vo=persTraceService.findPersTraceMainVoById(prpLDlossPersTraceMainVo.getId());
				 int maxLevel=Vo.getMaxLevel();//最高级
				String subNodeCode="";
				String subNodeCode1="PLVerify_LV"+maxLevel;//分公司人伤跟踪审核的最高级PLVerify_LV*
				String subNodeCode2="PLCharge_LV"+maxLevel;//分公司人伤费用审核的最高级PLCharge_LV*
				if(subNodeCode1.equals(submitNextVo.getCurrentNode())){
					subNodeCode=subNodeCode1;
				}
				if(subNodeCode2.equals(submitNextVo.getCurrentNode())){
					subNodeCode=subNodeCode2;
				}
				//判断分公司能处理的最高级节点是不是正在处理
				PrpLWfTaskVo  prpLWfTaskInVo =wfTaskHandleService.findWftaskInByRegistnoAndSubnode(prpLDlossPersTraceMainVo.getRegistNo(),subNodeCode);
				
			   if(prpLWfTaskInVo!=null){
				   if(wfTaskHandleService.existTaskByNodeCode(prpLDlossPersTraceMainVo.getRegistNo(),FlowNode.PLBig,null,"0")){
							throw new IllegalArgumentException("人伤大案审核未提交，人伤审核不能提交！");
					}
				}
			}
			
			String auditStatus = persTraceHandleService.saveOrSubmitPLVerify(prpLDlossPersTraceMainVo,
					prpLDlossPersTraceVos,prpLDlossChargeVos,prpLClaimTextVo,submitNextVo,userVo);
			logger.info("报案号={},人伤提交审核状态为auditStatus={}",(prpLDlossPersTraceMainVo == null? null : prpLDlossPersTraceMainVo.getRegistNo()),auditStatus);
		
			//移动端案件理赔处理要通知移动端
			String flowTaskId = submitNextVo.getFlowTaskId();
			String url = SpringProperties.getProperty("MClaimPlatform_URL_IN");
			PrpLWfTaskVo prplWfTaskVo = sendMsgToMobileService.isMobileCaseAcceptInClaim(registNo, new BigDecimal(flowTaskId));
			if(prplWfTaskVo != null){
				if(StringUtils.isNotBlank(auditStatus) && "backPLNext".equals(auditStatus)){
					 List<PrpLWfTaskVo> upWfTaskVos = wfTaskHandleService.findPrpLWfTaskInTimeDescByRegistNo(registNo);
					 if(upWfTaskVos != null && upWfTaskVos.size() > 0){
						 prplWfTaskVo.setTaskId(upWfTaskVos.get(0).getTaskId());
					 }
					
					prplWfTaskVo.setOriginalTaskId(prplWfTaskVo.getUpperTaskId());
					prplWfTaskVo.setHandlerStatus("0");
					prplWfTaskVo.setWorkStatus("6");
					prplWfTaskVo.setMobileNo("-1");
					prplWfTaskVo.setMobileOperateType(CodeConstants.MobileOperationType.PLOSSLOSSBACK);
					interfaceAsyncService.packMsg(prplWfTaskVo, url);
				}
			}
			
			try{
				if(AuditStatus.SUBMITCHARGE.equals(auditStatus)){
					//调用影像系统“影像资料统计接口”，查询该工号在该任务中上传的影像数量并保存（异步执行）
					String imageUrl = SpringProperties.getProperty("YX_QUrl")+"?";
					SysUserVo sysUserVo = new SysUserVo();
					PrpLDlossPersTraceMainVo  persTraceMainVo = persTraceService.findPersTraceMainVoById(prpLDlossPersTraceMainVo.getId());
					sysUserVo.setUserCode(persTraceMainVo.getCreateUser());
					
				    String userName = CodeTranUtil.transCode("UserCode", persTraceMainVo.getCreateUser());
				    if(userName != null && userName.equals(persTraceMainVo.getCreateUser())){
				    	userName = scheduleTaskService.findPrpduserByUserCode(persTraceMainVo.getCreateUser(),"").getUserName(); 
				    }
				    sysUserVo.setUserName(userName);
				    sysUserVo.setComCode(userVo.getComCode());
					interfaceAsyncService.getReqImageNum(sysUserVo, CodeConstants.APPROLE, prpLDlossPersTraceMainVo.getRegistNo(), "", imageUrl,CodeConstants.APPNAMECLAIM,CodeConstants.APPCODECLAIM);
					interfaceAsyncService.getReqCheckUserImageNum(sysUserVo, CodeConstants.APPROLE, prpLDlossPersTraceMainVo.getRegistNo(), imageUrl,CodeConstants.APPNAMECLAIM,CodeConstants.APPCODECLAIM);
				}
				
			}catch(Exception e){
				logger.info("人伤调用影像系统影像资料统计接口报错=============", e);
			}
			try{
				if(AuditStatus.SUBMITVERIFY.equals(auditStatus) || AuditStatus.SUBMITCHARGE.equals(auditStatus)){// 审核通过时提交立案刷新
					checkTaskService.saveCheckDutyHis(registNo,"人伤跟踪"+auditStatus);		
					logger.info("报案号={}, 人伤跟踪进行刷立案",(prpLDlossPersTraceMainVo == null ? null: prpLDlossPersTraceMainVo.getRegistNo()));
					claimTaskService.updateClaimFee(registNo,userVo.getUserCode(), FlowNode.valueOf(submitNextVo.getCurrentNode()));// 刷新立案
				}
			}catch(Exception e){
//				throw new Exception("刷新立案失败："+e.getMessage());
				logger.error("报案号"+ (prpLDlossPersTraceMainVo == null ? null : prpLDlossPersTraceMainVo.getRegistNo()) + "人伤提交刷新立案失败：",e);
			}
			ajaxResult.setData(auditStatus);
			ajaxResult.setStatus(HttpStatus.SC_OK);
			//退回定损回写，
			if(StringUtils.isNotBlank(submitNextVo.getNextNode())){
				if(FlowNode.PLNext.name().equals(submitNextVo.getNextNode()) && submitNextVo.getCurrentNode().contains("PLCharge")){
					PrpLAcheckVo prpLAcheckVo =acheckService.findPrpLAcheckVo(prpLDlossPersTraceMainVo.getRegistNo(),"3", "0","");
					if(prpLAcheckVo!=null){
						prpLAcheckVo.setUnderWriteFlag("7");//退回
						acheckService.updatePrpLAcheck(prpLAcheckVo);
					}
				}
			}
			
		}
		catch(Exception e){
			ajaxResult.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			ajaxResult.setStatusText(e.getMessage());
			logger.error("报案号"+ (prpLDlossPersTraceMainVo == null? null : prpLDlossPersTraceMainVo.getRegistNo()) +"人伤提交失败,",e);
		}
		return ajaxResult;
	}

	/**
	 * 激活注销跟踪人员
	 * @param id
	 * ,validFlag
	 * @return
	 * @modified: ☆XMSH(2016年1月16日 下午3:19:16): <br>
	 */
	@RequestMapping(value = "/ActiveOrCancel.do")
	@ResponseBody
	public AjaxResult ActiveOrCancel(String id,String validFlag) {
		String flag = "1";
		try{
			persTraceHandleService.ActiveOrCancelPersTrace(id,validFlag);
		}
		catch(Exception e){
			e.printStackTrace();
			flag = "0";
		}
		AjaxResult ajaxResult = new AjaxResult();
		ajaxResult.setStatus(HttpStatus.SC_OK);
		ajaxResult.setData(flag);

		return ajaxResult;
	}
}
