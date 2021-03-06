package ins.sino.claimcar.lossprop.web.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import ins.framework.service.CodeTranService;
import ins.framework.web.AjaxResult;
import ins.framework.web.bind.annotation.FormModel;
import ins.platform.common.service.facade.CodeDictService;
import ins.platform.common.util.CodeTranUtil;
import ins.platform.common.util.ConfigUtil;
import ins.platform.common.web.util.WebUserUtils;
import ins.platform.saa.service.facade.SaaUserPowerService;
import ins.platform.saa.vo.SaaUserPowerVo;
import ins.platform.utils.DataUtils;
import ins.platform.vo.PrpLConfigValueVo;
import ins.platform.vo.SysCodeDictVo;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.certify.service.CertifyIlogService;
import ins.sino.claimcar.certify.service.CertifyService;
import ins.sino.claimcar.check.service.CheckHandleService;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.PrpLCheckCarInfoVo;
import ins.sino.claimcar.check.vo.PrpLCheckCarVo;
import ins.sino.claimcar.claim.service.ClaimTaskService;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.claim.service.InterfaceAsyncService;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.common.claimtext.service.ClaimTextService;
import ins.sino.claimcar.common.claimtext.vo.PrpLClaimTextVo;
import ins.sino.claimcar.common.losscharge.service.LossChargeService;
import ins.sino.claimcar.common.losscharge.vo.PrpLDlossChargeVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.WfFlowQueryService;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.flow.vo.WfTaskSubmitVo;
import ins.sino.claimcar.ilog.defloss.vo.LIlogRuleResVo;
import ins.sino.claimcar.ilog.rule.service.IlogRuleService;
import ins.sino.claimcar.ilogFinalpowerInfo.vo.IlogFinalPowerInfoVo;
import ins.sino.claimcar.losscar.service.DeflossHandleService;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarInfoVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.lossperson.service.PersTraceService;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceMainVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceVo;
import ins.sino.claimcar.lossprop.service.PropLossHandleService;
import ins.sino.claimcar.lossprop.service.PropLossService;
import ins.sino.claimcar.lossprop.service.PropTaskService;
import ins.sino.claimcar.lossprop.vo.DefCommonVo;
import ins.sino.claimcar.lossprop.vo.DeflossActionVo;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropFeeVo;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropMainVo;
import ins.sino.claimcar.lossprop.vo.SubmitNextVo;
import ins.sino.claimcar.manager.service.ManagerService;
import ins.sino.claimcar.manager.vo.PrpdCheckBankMainVo;
import ins.sino.claimcar.mobilecheck.service.SendMsgToMobileService;
import ins.sino.claimcar.other.service.AcheckService;
import ins.sino.claimcar.other.service.AcheckTaskService;
import ins.sino.claimcar.other.service.AssessorService;
import ins.sino.claimcar.other.vo.PrpLAcheckVo;
import ins.sino.claimcar.other.vo.PrpLAssessorFeeVo;
import ins.sino.claimcar.other.vo.PrpLAssessorMainVo;
import ins.sino.claimcar.platform.service.CertifyToPaltformService;
import ins.sino.claimcar.platform.service.LossToPlatformService;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistService;
import ins.sino.claimcar.regist.service.ScheduleTaskService;
import ins.sino.claimcar.regist.vo.PrpLCItemKindVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.rule.service.ClaimRuleApiService;
import ins.sino.claimcar.schedule.vo.PrpLScheduleDefLossVo;
import ins.sino.claimcar.verifyclaim.service.VerifyClaimService;


@Controller
@RequestMapping(value="/proploss")
public class PropLossAction {	
	private static Logger logger = LoggerFactory.getLogger(PropLossAction.class);
	@Autowired
	private PropLossService propLossService;
	@Autowired
	private PropLossHandleService propLossHandleService;
	@Autowired
	CodeDictService codeDictService;
	@Autowired
	CodeTranService codeTranService;
	@Autowired
	ClaimTextService claimTextService;
	@Autowired
	PolicyViewService policyViewService;
	@Autowired
	LossChargeService lossCharService;
	@Autowired
	WfTaskHandleService wfTaskHandleService;
	@Autowired
	ClaimTaskService claimTaskService;
	@Autowired
	LossToPlatformService lossToPlatformService;
	@Autowired
	ClaimRuleApiService claimRuleApiService;
	@Autowired
	CheckHandleService checkHandleService;
	@Autowired
	RegistService registService;
	@Autowired
	CheckTaskService checkTaskService;
	
	@Autowired
    LossCarService lossCarService;
	
	@Autowired
	private AssessorService assessorService;
	
	@Autowired
	private CompensateTaskService compensateTaskService;
	@Autowired
    private SendMsgToMobileService sendMsgToMobileService;
	@Autowired
	InterfaceAsyncService interfaceAsyncService;
	@Autowired
	SaaUserPowerService saaUserPowerService;
	@Autowired
	PersTraceService persTraceService;
	@Autowired
	WfFlowQueryService wfFlowQueryService;
	@Autowired
	CertifyIlogService certifyIlogService;
	@Autowired
	private CertifyService certifyService;
	@Autowired
	PropTaskService propTaskService;
	@Autowired
    CertifyToPaltformService certifyToPaltformService;
    @Autowired
    VerifyClaimService verifyClaimService;
    @Autowired
    ScheduleTaskService scheduleTaskService;
    @Autowired
    private IlogRuleService ilogRuleService;
    @Autowired
	private DeflossHandleService deflossHandleService;
    
    @Autowired
	ManagerService managerService;
    @Autowired
	private AcheckService acheckService;
    
    @Autowired
    private AcheckTaskService acheckTaskService;
	/**
	 * ?????????????????????
	 */
	@RequestMapping(value="/initPropVerifyLoss.do")
	public ModelAndView initPropVerifyLoss(Double flowTaskId){	
		ModelAndView mv=new ModelAndView();
		DeflossActionVo deflossVo=new DeflossActionVo();
		DefCommonVo commonVo=new DefCommonVo();
		PrpLWfTaskVo taskVo = wfTaskHandleService.queryTask(flowTaskId);//?????????????????????
		commonVo.setHandleStatus(taskVo.getHandlerStatus());
		deflossVo.setRegistNo(taskVo.getRegistNo());
		deflossVo.setBusinessId(Long.parseLong(taskVo.getHandlerIdKey()));
		deflossVo.setTaskVo(taskVo);
		SysUserVo userVo = WebUserUtils.getUser();
		deflossVo = propLossHandleService.initPropVerify(deflossVo,userVo);
		
		PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.ASSESSORFEE,WebUserUtils.getComCode());
		String configValue = "0";
		if(configValueVo!=null){
			configValue = configValueVo.getConfigValue();
		}
		
		String registNo="";//?????????
		String insuredName="";//????????????
		String driverName="";//?????????
		String frameNo="";//??????????????????
		Date registTime=null;//????????????
       if(deflossVo.getLossPropMainVo()!=null){
    	   registNo=deflossVo.getLossPropMainVo().getRegistNo();
    	   List<PrpLCMainVo> prpLCMainVos= checkHandleService.getPolicyAllInfo(registNo);
    	   if(prpLCMainVos!=null && prpLCMainVos.size()>0){
    		   //????????????????????????????????????????????????????????????????????????????????????????????????
    		   for(PrpLCMainVo prpM:prpLCMainVos){
    			   if(!"1101".equals(prpM.getRiskCode())){
    				   insuredName=prpM.getInsuredName();
    				   break;
    				 }else{
    					 insuredName=prpM.getInsuredName(); 
    				 }
    		   }
    	   }
    	   //??????????????????????????????
    	   PrpLRegistVo prpLRegistVo =registService.findRegistByRegistNo(registNo);
    	   if(prpLRegistVo!=null){
    		   driverName=prpLRegistVo.getDriverName();
    		   registTime=prpLRegistVo.getReportTime();
    		  }
    	   //???????????????????????????
    	   List<PrpLCheckCarVo>  prpLCheckCarVos=checkTaskService.findCheckCarVo(registNo);
    	   if(prpLCheckCarVos!=null && prpLCheckCarVos.size()>0){
    		   for(PrpLCheckCarVo prpLCheckCarVo:prpLCheckCarVos){
    			   if(1==prpLCheckCarVo.getSerialNo()){
    				   PrpLCheckCarInfoVo carInfoVo=checkTaskService.findPrpLCheckCarInfoVoById(prpLCheckCarVo.getCarid());
    				   if(carInfoVo!=null){
    					   frameNo=carInfoVo.getFrameNo();
    				   }
    			   }
    		   }
    	   }
    	   
       }
       //??????????????????
       List<PrpLDlossCarMainVo> carMainVos= lossCarService.findLossCarMainByRegistNo(registNo);
       String lossCarSign="0";//??????????????????????????????????????????
       if(carMainVos!=null && carMainVos.size()>0){
    	   for(PrpLDlossCarMainVo vo:carMainVos){
    		   if("1".equals(vo.getUnderWriteFlag())){
    			   lossCarSign="1";
    			   break;
    		   }
    	   }
       }
       
       //??????????????????????????????????????????
       String compensateSign="0";//?????????????????????????????????
        List<PrpLCompensateVo> compensates=compensateTaskService.findCompensatevosByRegistNo(registNo);
     	   if(compensates!=null && compensates.size()>0){
     		   for(PrpLCompensateVo vo:compensates){
     	        if("N".equals(vo.getCompensateType())){
     	        	compensateSign="1";
     	        	break;
     	        }
     			      
     	         }
     	      
     	   }
     	//???????????????????????????????????????
   		String assessSign="0";
   		List<PrpLAssessorFeeVo> listFeeVo=assessorService.findPrpLAssessorFeeVoByRegistNo(registNo);
   		if(listFeeVo!=null && listFeeVo.size()>0){
   			for(PrpLAssessorFeeVo vo :listFeeVo){
				PrpLAssessorMainVo assessMainVo=assessorService.findAssessorMainVoById(vo.getAssessMainId());
				if(assessMainVo!=null && "3".equals(assessMainVo.getUnderWriteFlag())){
					assessSign="1";
					break;
				}
			}
   		}
   		
        //???????????????ILOG????????????????????????
        String nodeCode=deflossVo.getTaskVo().getSubNodeCode();
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
		mv.addObject("roleFlag",roleFlag);
		String ruleNodeCode="VLProp";
		ruleNodeCode=FlowNode.valueOf(taskVo.getSubNodeCode()).getUpperNode();
   		mv.addObject("verifyLevel", deflossVo.getLossPropMainVo().getVerifyLevel());
   		mv.addObject("maxLevel", deflossVo.getLossPropMainVo().getMaxLevel());
   		mv.addObject("currentLevel", taskVo.getSubNodeCode().split("LV")[1]);
   		
   		int currencyLevel = Integer.parseInt(taskVo.getSubNodeCode().split("LV")[1]);
		if( deflossVo.getLossPropMainVo().getVerifyLevel()!=null && currencyLevel >= deflossVo.getLossPropMainVo().getVerifyLevel()){
			mv.addObject("verifyPassFlag", true);
		}else{
			mv.addObject("verifyPassFlag", false);
		}
		
		
   		
   		mv.addObject("assessSign",assessSign);
    	mv.addObject("compensateSign",compensateSign);
        mv.addObject("lossCarSign",lossCarSign);
        mv.addObject("insuredName",insuredName);
        mv.addObject("driverName",driverName);
        mv.addObject("frameNo",frameNo);
        mv.addObject("registTime",registTime);
		mv.addObject("configValue", configValue);  //???????????????
		
		mv.addObject("existBackLower",taskVo.getTaskInNode().startsWith("VLProp_LV"));//??????????????????????????????
		mv.addObject("lossCarVos",deflossVo.getLossCarMainList());
		mv.addObject("lossPropVos",deflossVo.getLossPropFeeVos());
		mv.addObject("lossPersTraceList",deflossVo.getLossPersTraceList());
		mv.addObject("prpLdlossPropMainVo",deflossVo.getLossPropMainVo());
		
		mv.addObject("lossPropMainVo",deflossVo.getLossPropMainVo());
		mv.addObject("claimTextVos",deflossVo.getClaimTextVos());
		mv.addObject("claimTextVo",deflossVo.getClaimTextVo());
		mv.addObject("lossChargeVos",deflossVo.getLossChargeVos());
		mv.addObject("handleStatus", taskVo.getHandlerStatus());
		mv.addObject("taskVo", taskVo);
		mv.addObject("ruleNodeCode", ruleNodeCode);
		mv.addObject("lossPropMainVos",deflossVo.getLossPropMainVos());
		mv.setViewName("propLoss/propVerify/PropVerify_Edit");
		return mv;
	}
	/*
	 * ??????????????????
	 */
	@RequestMapping(value="/propVerifyLossView.do")
	public ModelAndView propVerifyLossView(String lossId){
		ModelAndView mv=new ModelAndView();
		PrpLdlossPropMainVo prpLdlossPropMainVo = propLossService.findVoByKey(Long.valueOf(lossId));
		PrpLWfTaskVo taskVo = wfTaskHandleService.findEndTask(prpLdlossPropMainVo.getRegistNo(), lossId,FlowNode.VLProp).get(0);
		DefCommonVo commonVo=new DefCommonVo();
		DeflossActionVo deflossVo=new DeflossActionVo();
		commonVo.setHandleStatus(taskVo.getHandlerStatus());
		deflossVo.setRegistNo(taskVo.getRegistNo());
		deflossVo.setBusinessId(Long.parseLong(taskVo.getHandlerIdKey()));
		deflossVo.setTaskVo(taskVo);
		SysUserVo userVo = WebUserUtils.getUser();
		deflossVo = propLossHandleService.initPropVerify(deflossVo,userVo);
		
		PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.ASSESSORFEE,WebUserUtils.getComCode());
		String configValue = "0";
		if(configValueVo!=null){
			configValue = configValueVo.getConfigValue();
		}
		mv.addObject("configValue", configValue);  //???????????????
		
		mv.addObject("lossCarVos",deflossVo.getLossCarMainList());
		mv.addObject("lossPropVos",deflossVo.getLossPropFeeVos());
		mv.addObject("lossPersTraceList",deflossVo.getLossPersTraceList());
		mv.addObject("prpLdlossPropMainVo",deflossVo.getLossPropMainVo());
		
		mv.addObject("lossPropMainVo",deflossVo.getLossPropMainVo());
		mv.addObject("claimTextVos",deflossVo.getClaimTextVos());
		mv.addObject("claimTextVo",deflossVo.getClaimTextVo());
		mv.addObject("lossChargeVos",deflossVo.getLossChargeVos());
		mv.addObject("handleStatus", taskVo.getHandlerStatus());
		mv.addObject("taskVo", taskVo);
		mv.addObject("lossPropMainVos",deflossVo.getLossPropMainVos());
		mv.setViewName("propLoss/propVerify/PropVerify_Edit");
		return mv;
	}
	
	
	/**
	 * ?????????????????? ?????? ??????
	 */
	@RequestMapping(value="/propModifyLaunch")
	@ResponseBody
	public AjaxResult propModifyLaunch(){
		AjaxResult ajaxResult=new AjaxResult();
		ajaxResult.setStatus(org.apache.http.HttpStatus.SC_OK);
		//???????????????????????? ??????????????????????????????????????????
	//	ajaxResult.setStatusText("1111111111111111");
		return ajaxResult;
	}
	
	
	
	/**
	 * ???????????????????????????
	 */
	public void deflossAdditional(PrpLScheduleDefLossVo scheduleDefLosVo,PrpLdlossPropMainVo propMainVo){
		//1.?????????????????????????????????2.??????????????????????????????
		//??????????????????????????????????????????????????????????????????????????????????????????
		//???????????????????????????????????????????????????????????????????????????????????????????????????
		Long addDeflossId=scheduleDefLosVo.getAddDeflossId();
		PrpLdlossPropMainVo propMainVoOra=propLossService.findPropMainVoById(addDeflossId);//??????????????????????????? ?????????
		propMainVo.setDeflossSourceFlag(CodeConstants.ScheduleDefSource.SCHEDULEADD);
		propMainVo.setRegistNo(propMainVoOra.getRegistNo());
		propMainVo.setLicense(propMainVoOra.getLicense());
		propMainVo.setLossType(propMainVoOra.getLossType());
		propMainVo.setLossState("00");
		propMainVo.setReLossPropId(propMainVoOra.getId());
		propMainVo.setCaseTaskFlag(propMainVoOra.getCaseTaskFlag());
		propMainVo.setScheduleTaskId(scheduleDefLosVo.getId());
		List<PrpLdlossPropFeeVo> propFeeVos=new ArrayList<PrpLdlossPropFeeVo>();
		PrpLdlossPropFeeVo propFeeVo=new PrpLdlossPropFeeVo();
		propFeeVo.setLossItemName(scheduleDefLosVo.getItemsContent());//??????????????????
		propFeeVo.setRegistNo(propMainVo.getRegistNo());
		propFeeVo.setRiskCode(propMainVo.getRiskCode());
		propFeeVos.add(propFeeVo);
		propMainVo.setPrpLdlossPropFees(propFeeVos);//??????????????????
		//1,???????????????????????????????????? 
		//2,???????????????????????????????????? ??????????????????,??????????????????????????????????????????????????????????????????????????????????????????????????????
		//3,?????????????????? ?????????????????????
	}
	
	
	/**
	 * ????????? ?????????????????? ????????????
	 */
	public  List<PrpLDlossCarInfoVo> initLossCarInformation(String registNo){
		//???????????????????????????
		return null;
	}
	
	/**
	 * ???????????????????????????
	 */
	@RequestMapping(value = "/acceptDefloss") 
	@ResponseBody
	public AjaxResult acceptDefloss(String flowTaskId){
		String flag = "1";
		DeflossActionVo deflossVo=new DeflossActionVo();
		SysUserVo sysUserVo = WebUserUtils.getUser();
		try{
			//wfTaskHandleService.acceptTask(Double.parseDouble(flowTaskId),assignUser,assignCom);
		    PrpLdlossPropMainVo propMainVo = propLossHandleService.acceptDefloss(Double.valueOf(flowTaskId),deflossVo,sysUserVo);
		    PrpLWfTaskVo prpLWfTaskVo = sendMsgToMobileService.isMobileCaseAcceptInClaim(propMainVo.getRegistNo(),new BigDecimal(flowTaskId));
            if(prpLWfTaskVo != null){ //????????????
                prpLWfTaskVo.setHandlerStatus("0"); //?????????
                prpLWfTaskVo.setWorkStatus("0");  // ????????????
                prpLWfTaskVo.setMobileNo(propMainVo.getSerialNo().toString());
                prpLWfTaskVo.setMobileName(propMainVo.getLicense());
                prpLWfTaskVo.setMobileOperateType(CodeConstants.MobileOperationType.PROPLOSSACCEPT);
                String url = SpringProperties.getProperty("MClaimPlatform_URL_IN");
                interfaceAsyncService.packMsg(prpLWfTaskVo,url);
            }
		}catch (Exception e){
			e.printStackTrace();
			flag = e.getMessage();
		}
		AjaxResult ajaxResult = new AjaxResult();
		ajaxResult.setStatus(HttpStatus.SC_OK);
		ajaxResult.setData(flag);
		return ajaxResult;
	}
	
	
	/**
	 * ?????????????????????
	 */
	@RequestMapping(value="/initPropCertainLoss.do")
	public ModelAndView initPropCertainLoss(Double flowTaskId){
		
		ModelAndView mv = new ModelAndView();
		DeflossActionVo deflossVo = new DeflossActionVo();
		SysUserVo sysUserVo = WebUserUtils.getUser();
		PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(flowTaskId);
		deflossVo = propLossHandleService.initPropDefloss(deflossVo,flowTaskId,sysUserVo);
	
		//?????????
		Map<String,String> cusMap=propLossHandleService.getPayCustomMap(deflossVo.getRegistNo());
		//?????? ????????????
//		Map<String,String> intermStanderMap = propLossHandleService.getIntermStanders("0036");
		//??????????????????????????????
		String flag = propLossService.judgeRecLoss(wfTaskVo);
		//??????????????????????????????????????????????????????????????????
//		if(!"0".equals(deflossVo.getCommonVo().getAcceptFlag())){
//			DeflossActionVo deflossActionVo = new DeflossActionVo();
//			propLossHandleService.acceptDefloss(Double.valueOf(flowTaskId),deflossActionVo,sysUserVo);
//		}
		
		//???????????????????????????????????????
		String assessSign="0";
		List<PrpLAssessorFeeVo> listFeeVo=assessorService.findPrpLAssessorFeeVoByRegistNo(deflossVo.getRegistNo());
		if(listFeeVo!=null && listFeeVo.size()>0){
			for(PrpLAssessorFeeVo vo :listFeeVo){
				PrpLAssessorMainVo assessMainVo=assessorService.findAssessorMainVoById(vo.getAssessMainId());
				if(assessMainVo!=null && "3".equals(assessMainVo.getUnderWriteFlag())){
					assessSign="1";
					break;
				}
			}
		}
		mv.addObject("oldClaim", deflossVo.getRegistVo().getFlag());
		mv.addObject("assessSign",assessSign);
		mv.addObject("wfTaskVo",wfTaskVo);
		mv.addObject("kindMap", deflossVo.getKindMap());
		mv.addObject("intermMap",deflossVo.getIntermMap());
		mv.addObject("prpLdlossPropMainVo",deflossVo.getLossPropMainVo());
		mv.addObject("claimTextVo",deflossVo.getClaimTextVo());
		mv.addObject("claimTextVos", deflossVo.getClaimTextVos());
		mv.addObject("lossChargeVos",deflossVo.getLossChargeVos());
		mv.addObject("commonVo",deflossVo.getCommonVo());
		mv.addObject("cusMap",cusMap);
		mv.addObject("oldClaim",deflossVo.getRegistVo().getFlag());
		mv.addObject("feeTypeCode", propLossHandleService.getFeeTypeCode());
		//????????????
		mv.addObject("lossCarVos",deflossVo.getLossCarMainList());
		mv.addObject("lossPropVos",deflossVo.getLossPropFeeVos());
		mv.addObject("lossPersTraceList",deflossVo.getLossPersTraceList());
		mv.addObject("nodeCode",wfTaskVo.getNodeCode());
		mv.addObject("flag",flag);
		mv.addObject("lossPropMainVos",deflossVo.getLossPropMainVos());
		mv.setViewName("propLoss/propDefloss/DeflossEdit");
		return mv;
	}
	
	/**
	 * ??????????????????,??????????????????
	 */
	@RequestMapping(value="/viewPropCertainLoss")
	public ModelAndView viewPropCertainLoss(Long businessId,String registNo,String status){
		PrpLdlossPropMainVo propMainVo=null;
		//List<PrpLdlossPropMainVo> propMainVos=null;//??????????????????
		List<PrpLClaimTextVo> claimTextVos=null;
		//PrpLClaimTextVo claimTextVo=null;
		// ??????businessId??????????????????
        if(businessId!=null){
        	propMainVo=propLossService.findPropMainVoById(businessId);
        	//claimTextVos=propLossService.findClaimTextVoByBussTaskId(propMainVo.getId());
        }
        // ????????????????????????????????????
        if(registNo!=null && !"".equals(registNo)){
        	//propMainVos=propTaskService.findAllPropMainVoByRegistNo(registNo);
        }
		//????????????????????????
		//?????????????????????????????????
		//??????????????????????????????????????????
		ModelAndView mv=new ModelAndView();
		mv.addObject("prpLdlossPropMainVo", propMainVo);
		mv.addObject("claimTextVos", claimTextVos);
		mv.setViewName("propLoss/propDeflossView/propDeflossView");
		return mv;
	}
	/**
	 * ???????????? ??????????????????
	 */
	@RequestMapping(value="/initSubRisk.ajax")
	public ModelAndView initSubRisk(String registNo,String[] kindCodes) { 
		ModelAndView mv = new ModelAndView();
		List<PrpLCItemKindVo> itemKinds = propLossHandleService.initSubRisks(registNo,kindCodes);
		mv.addObject("itemKinds",itemKinds);
		mv.setViewName("propLoss/propDefloss/SubRiskDialog");
		return mv; 
	}
	
	/**
	 * ?????????????????????????????????????????????
	 */
	@RequestMapping(value="/initChargeType.ajax")
	public ModelAndView initChargeType(String chargeCodes,String intermFlag) { 
		List<String> chargeCodeList = new ArrayList<String>();
		if(chargeCodes!=null && !"".equals(chargeCodes)){
			String[] chargeArray = chargeCodes.split(",");
			chargeCodeList = Arrays.asList(chargeArray);
		}
		//?????????????????????????????????
//		if("0".equals(intermFlag)){
//			chargeCodeList.add("13");
//		}
		ModelAndView mv = new ModelAndView();
		List<SysCodeDictVo> sysCodes = codeDictService.findCodeListByQuery("ChargeCode",chargeCodeList);
		
		mv.addObject("sysCodes",sysCodes);
		mv.setViewName("loss-common/ChargeDialog");
		return mv; 
	}
	
	@RequestMapping(value="/loadChargeTr.ajax")
	public ModelAndView loadChargeTr(int size,String[] chargeTypes,String registNo,String intermCode) { 
		ModelAndView mv = new ModelAndView();
		
		List<PrpLDlossChargeVo> lossChargeVos = propLossHandleService.loadChargeTr(chargeTypes,registNo,intermCode);
		Map<String,String> kindMap = propLossHandleService.getPolicyKindMap(registNo);
		Map<String,String> customMap = propLossHandleService.getPayCustomMap(registNo);
		
		mv.addObject("kindMap",kindMap);
		mv.addObject("lossChargeVos",lossChargeVos);
		mv.addObject("size",size);
		mv.addObject("cusMap",customMap);
		mv.setViewName("loss-common/DeflossEdit_Charge_Tr");
		return mv; 
	}

	
	/**
	 * ?????????????????????????????????
	 */
	@RequestMapping(value="/initProp_Item")
	public ModelAndView initProp_Item(@RequestParam(value="registNo")String registNo,int size,String flag){	
		PrpLdlossPropMainVo prpLdlossPropMainVo=new PrpLdlossPropMainVo();		
		ModelAndView mv = new ModelAndView();
		PrpLdlossPropFeeVo feeVo=new PrpLdlossPropFeeVo();
		//?????????????????????0
		BigDecimal recyclePrice = new BigDecimal(0);
		feeVo.setRecyclePrice(recyclePrice);
		//???????????????????????????
		feeVo.setRegistNo(registNo);
		feeVo.setValidFlag("0");//?????????????????????????????????????????????????????????
		List<PrpLdlossPropFeeVo> lists=new ArrayList<PrpLdlossPropFeeVo>();
		lists.add(feeVo);
		prpLdlossPropMainVo.setPrpLdlossPropFees(lists);
		//mv.addObject("prpLdlossPropMainVo", propLossService.findVoByRegistNo("620522"));
		
		mv.addObject("feeTypeCode", propLossHandleService.getFeeTypeCode());
		mv.addObject("size", size);
		mv.addObject("flag", flag);
		mv.addObject("prpLdlossPropMainVo", prpLdlossPropMainVo);
		mv.setViewName("propLoss/propDefloss/DeflossEdit_DamageItem_Tr");//PropLoss_Item_Tr
		return mv;
	}
	

	/** ?????????????????? */
	@RequestMapping(value="/savePropVerifyLoss.do")
	@ResponseBody
	public AjaxResult savePropVerifyLoss(
			@FormModel(value="lossPropMainVo")PrpLdlossPropMainVo lossPropMainVo,
            @FormModel("claimTextVo") PrpLClaimTextVo claimTextVo,
            @FormModel("lossChargeVos") List<PrpLDlossChargeVo> lossChargeVos){

		AjaxResult ajaxResult=new AjaxResult();
		try{
			DeflossActionVo deflossVo =new DeflossActionVo();
			deflossVo.setLossPropMainVo(lossPropMainVo);
			deflossVo.setClaimTextVo(claimTextVo);
			deflossVo.setLossChargeVos(lossChargeVos);
			SysUserVo sysUserVo =WebUserUtils.getUser();
			propLossHandleService.savePropVerify(deflossVo,sysUserVo);
			
			String flowTaskId=lossPropMainVo.getFlowTaskId().toString();
			String returnStr=deflossVo.getLossPropMainVo().getId()+","+flowTaskId;
			ajaxResult.setStatus(HttpStatus.SC_OK);
			ajaxResult.setData(returnStr);
		}catch(Exception e){
			ajaxResult.setStatusText(e.getMessage());
			e.printStackTrace();
		}
		return ajaxResult;
	}
	
	
	/**
	 * ??????????????????
	 */
	@RequestMapping(value="/viewPropVerifyLoss.do")
	public ModelAndView viewPropVerifyLoss(){
		
		return null;
	}
	
	@RequestMapping(value = "/submitNextNode.do")
	public ModelAndView submitNextNode(@FormModel("nextVo")SubmitNextVo nextVo,HttpSession session) throws Exception{
		
//		String status = (String)session.getAttribute("checkSubmit_status");
//		if("doing".equals(status)) {
//			//?????????????????????????????????
//			throw new IllegalArgumentException("??????????????????????????????????????????????????????????????????????????????");
//		}
//		session.setAttribute("checkSubmit_status","doing");
		/*try{
			Thread.sleep(3000);
		}catch (Exception e){
			e.printStackTrace();
		}*/
		SysUserVo sysUserVo = WebUserUtils.getUser();
		ModelAndView mv = new ModelAndView();
		String lossMainId = nextVo.getTaskInKey();
		PrpLdlossPropMainVo lossPropMainVo = propLossService.findPropMainVoById(Long.parseLong(lossMainId));
        
		
		//?????????????????????
         if(FlowNode.DLProp.equals(nextVo.getCurrentNode())|| FlowNode.DLPropAdd.equals(nextVo.getCurrentNode())){
        	 acheckTaskService.addCheckFeeTaskOfDprop(lossPropMainVo, sysUserVo, "0");
         }
       //????????????????????????????????????????????????????????????--18886
//		if((CodeConstants.AuditStatus.AUDIT.equals(lossPropMainVo.getAuditStatus()))){
//		   //????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
//		    int maxLevel=lossPropMainVo.getMaxLevel();//?????????
//		    String subNodeCode="VLProp_LV"+maxLevel;//??????????????????????????????
//		    PrpLWfTaskVo  prpLWfTaskInVo =wfTaskHandleService.findWftaskInByRegistnoAndSubnode(lossPropMainVo.getRegistNo(),subNodeCode);
//	           if(prpLWfTaskInVo!=null){
//				   if(wfTaskHandleService.existTaskByNodeCode(lossPropMainVo.getRegistNo(),FlowNode.ChkBig,null,"0")){
//								throw new IllegalArgumentException("???????????????????????????????????????????????????");
//							}
//						}
//		
//	           }
		
		lossPropMainVo.setVerifyLevel(nextVo.getSubmitLevel());
		lossPropMainVo.setMaxLevel(nextVo.getMaxLevel());
		//?????????????????????????????????????????? ???????????????????????????
        //????????????
		String url = SpringProperties.getProperty("MClaimPlatform_URL_IN");
        PrpLWfTaskVo prpLWfTaskVo = sendMsgToMobileService.isMobileCaseAcceptInClaim(lossPropMainVo.getRegistNo(),new BigDecimal(nextVo.getFlowTaskId()));
		if(prpLWfTaskVo != null &&  (FlowNode.valueOf(nextVo.getCurrentNode()).getUpperNode().equals(FlowNode.DLoss.name()) ||
                FlowNode.valueOf(nextVo.getCurrentNode()).getUpperNode().equals(FlowNode.DLCar.name()))){ //????????????
		    prpLWfTaskVo.setHandlerStatus("3"); //?????????
            prpLWfTaskVo.setWorkStatus("3");  // ??????
            prpLWfTaskVo.setMobileNo(lossPropMainVo.getSerialNo().toString());
            prpLWfTaskVo.setMobileName(lossPropMainVo.getLicense());
            prpLWfTaskVo.setMobileOperateType(CodeConstants.MobileOperationType.PROPLOSSSUBMIT);
            interfaceAsyncService.packMsg(prpLWfTaskVo,url);
		}
		if(prpLWfTaskVo != null && CodeConstants.AuditStatus.BACKLOSS.equals(lossPropMainVo.getAuditStatus())){ //????????????
		    prpLWfTaskVo.setHandlerStatus("0"); //?????????
            prpLWfTaskVo.setWorkStatus("6");  // ??????
            prpLWfTaskVo.setMobileNo(lossPropMainVo.getSerialNo().toString());
            prpLWfTaskVo.setMobileName(lossPropMainVo.getLicense());
            prpLWfTaskVo.setMobileOperateType(CodeConstants.MobileOperationType.PROPVERILOSSBACK);
            interfaceAsyncService.packMsg(prpLWfTaskVo,url);
		}
//		List<PrpLWfTaskVo> taskVoList = propLossHandleService.submitNextNode(lossPropMainVo,nextVo,sysUserVo);
		List<PrpLWfTaskVo> taskVoList = new ArrayList<PrpLWfTaskVo>();
		try{
			propLossHandleService.savePropLoss(lossPropMainVo, nextVo, sysUserVo);
			taskVoList = propLossHandleService.submitTask(lossPropMainVo, nextVo, sysUserVo);
		}catch (Exception e){
			logger.error("?????????"+ (lossPropMainVo == null ? null : lossPropMainVo.getRegistNo())+ "????????????????????????",e);
		}
		
		if(CodeConstants.AuditStatus.SUBMITVLOSS.equals(lossPropMainVo.getAuditStatus())||
				"1".equals(nextVo.getAutoLossFlag())){
			try{
				//?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
				String imageUrl = SpringProperties.getProperty("YX_QUrl")+"?";
				SysUserVo sysUser = new SysUserVo();
				sysUser.setUserCode(lossPropMainVo.getHandlerCode());
			    String userName = CodeTranUtil.transCode("UserCode", lossPropMainVo.getHandlerCode());
			    if(userName != null && userName.equals(lossPropMainVo.getHandlerCode())){
			    	userName = scheduleTaskService.findPrpduserByUserCode(lossPropMainVo.getHandlerCode(),"").getUserName(); 
			    }
			    sysUser.setUserName(userName);
			    sysUser.setComCode(sysUserVo.getComCode());
				interfaceAsyncService.getReqImageNum(sysUser, CodeConstants.APPROLE, lossPropMainVo.getRegistNo(), "", imageUrl,CodeConstants.APPNAMECLAIM,CodeConstants.APPCODECLAIM);
				interfaceAsyncService.getReqCheckUserImageNum(sysUser, CodeConstants.APPROLE, lossPropMainVo.getRegistNo(),imageUrl,CodeConstants.APPNAMECLAIM,CodeConstants.APPCODECLAIM);
			}catch(Exception e){
				logger.info("????????????????????????????????????????????????????????????=============", e);
			}
			try{
				claimTaskService.updateClaimFee(lossPropMainVo.getRegistNo(),nextVo.getTaskInUser(),FlowNode.VLProp);// ????????????
			}catch (Exception e){
				logger.error("?????????"+ (lossPropMainVo == null ? null : lossPropMainVo.getRegistNo())+ "?????????????????????",e);
				throw new IllegalStateException("???????????????????????????");
			}
			
			//?????????????????????
			interfaceAsyncService.sendLossToPlatform(lossPropMainVo.getRegistNo(),null);
			
		}
		if(lossPropMainVo!=null){//????????????
			PrpLRegistVo prpLRegistVo=registService.findRegistByRegistNo(lossPropMainVo.getRegistNo());
			if(prpLRegistVo!=null && "1".equals(prpLRegistVo.getSelfClaimFlag())){  
				Boolean flag=certifyIlogService.validAllVLossPass(lossPropMainVo.getRegistNo());
				if(flag){
					interfaceAsyncService.sendClaimResultToSelfClaim(lossPropMainVo.getRegistNo(), sysUserVo,"5","2","");
				}
				
			}
		}
		//??????????????????????????????????????????Ilog
		PrpLConfigValueVo configValueIlogVo = ConfigUtil.findConfigByCode(CodeConstants.ILOGVALIDFLAG,WebUserUtils.getComCode());
		PrpLConfigValueVo configValueIRuleVo = ConfigUtil.findConfigByCode(CodeConstants.RULEVALIDFLAG,WebUserUtils.getComCode());
		if("1".equals(configValueIlogVo.getConfigValue()) && "0".equals(configValueIRuleVo.getConfigValue())){
		   Boolean flag=certifyIlogService.validAllVLossPass(lossPropMainVo.getRegistNo());
			if(flag){
				LIlogRuleResVo resVo=certifyIlogService.sendAutoCertifyRule(lossPropMainVo.getRegistNo(),sysUserVo,new BigDecimal(nextVo.getFlowTaskId()),nextVo.getCurrentNode());
				if(resVo!=null && "1".equals(resVo.getUnderwriterflag()) && certifyService.isPassPlatform(lossPropMainVo.getRegistNo())){
					 WfTaskSubmitVo submitVo=certifyIlogService.autoCertify(lossPropMainVo.getRegistNo(),sysUserVo);//????????????
						// ???????????????
					 try{
						 certifyToPaltformService.certifyToPaltform(lossPropMainVo.getRegistNo(),null); 
					 }catch(Exception e){
						 logger.error("?????????="+(lossPropMainVo == null ? null : lossPropMainVo.getRegistNo()) + "?????????????????????????????????-------------->",e); 
					 }
					 	// ??????ilog???????????????????????????
					 	boolean NotExistObj = compensateTaskService.adjustNotExistObj(lossPropMainVo.getRegistNo());
						if("1".equals(configValueIlogVo.getConfigValue()) && StringUtils.isNotBlank(submitVo.getFlowId())&& !NotExistObj){
	                        //==============??????????????????
							SysUserVo userVo = WebUserUtils.getUser();
                            String registNo = lossPropMainVo.getRegistNo();
                            LIlogRuleResVo ruleResVo = certifyIlogService.sendAutoCertifyRule(registNo,userVo,submitVo.getFlowTaskId(),submitVo.getCurrentNode().toString());
                            /** ???????????????????????? start **/
                            String finalPowerFlag =  SpringProperties.getProperty("FINALPOWERFLAG");
            	        	boolean finalAutoPass = true;
            	        	if ("1".equals(finalPowerFlag)) {
            	        		IlogFinalPowerInfoVo powerInfoVo = ilogRuleService.findByUserCode(userVo.getUserCode());
            	        		if (powerInfoVo != null) {
            	        			BigDecimal gradePower = powerInfoVo.getGradeAmount();
            	        			if (gradePower != null) {
            	        				// ???????????????
            	        				BigDecimal sumAmount = BigDecimal.ZERO;
            	        				// ??????????????????
            	        				List<PrpLDlossCarMainVo> losscarMainList = deflossHandleService.findLossCarMainByRegistNo(registNo);
            	        				// ??????????????????
            	        				List<PrpLDlossPersTraceMainVo> losspersTraceList = deflossHandleService.findlossPersTraceMainByRegistNo(registNo);
            	        				// ??????????????????
            	        				List<PrpLdlossPropMainVo> propmianList = propTaskService.findPropMainListByRegistNo(registNo);
            	        				if (losscarMainList != null && losscarMainList.size() > 0) {
            	        					for (PrpLDlossCarMainVo vo : losscarMainList) {
            	        						sumAmount = sumAmount.add(new BigDecimal(vo.getSumVeriLossFee() == null ? "0" : vo.getSumVeriLossFee().toString()));
            	        					}
            	        				}
            	        				
            	        				if (losspersTraceList != null && losspersTraceList.size() > 0) {
            	        					for (PrpLDlossPersTraceMainVo vo : losspersTraceList) {
            	        						if (vo.getPrpLDlossPersTraces() != null && vo.getPrpLDlossPersTraces().size() > 0) {
            	        							for (PrpLDlossPersTraceVo traceVo : vo.getPrpLDlossPersTraces()) {
            	        								sumAmount = sumAmount.add(DataUtils.NullToZero(traceVo.getSumVeriDefloss()));
            	        							}
            	        						}
            	        					}
            	        				}
            	        				
            	        				if (propmianList != null && propmianList.size() > 0) {
            	        					for (PrpLdlossPropMainVo vo : propmianList) {
            	        						sumAmount = sumAmount.add(new BigDecimal(vo.getSumVeriLoss() == null ? "0" : vo.getSumVeriLoss().toString()));
            	        					}
            	        				}
            	        				
            	        				if (sumAmount.compareTo(gradePower) == 1) {
            	        					finalAutoPass = false;
            	        				}
            	        			}
            	        		} else {
            	        			finalAutoPass = false;
            	        		}
            	        	}
							/** ????????????????????????  end  **/
                            
                            if("1".equals(ruleResVo.getUnderwriterflag()) && finalAutoPass){//??????????????????
                                List<PrpLWfTaskVo> prpLWfTaskVoList = wfTaskHandleService.findInTaskByOther(registNo,null,FlowNode.Compe.toString());
                                if(prpLWfTaskVoList!=null&&prpLWfTaskVoList.size()>0){
                                    for(PrpLWfTaskVo taskVo1:prpLWfTaskVoList){
                                        PrpLCompensateVo compVo = compensateTaskService.autoCompTask(taskVo1,userVo);
                                        Boolean autoVerifyFlag = false;
                                        
                                        Map<String,Object> params = new HashMap<String,Object>();
                                        params.put("registNo",compVo.getRegistNo());
                                        if(taskVo1.getTaskId() == null){
                                        	 params.put("taskId",BigDecimal.ZERO.doubleValue());
                                        }else{
                                            params.put("taskId",taskVo1.getTaskId().doubleValue());
                                        }
                                        String isSubmitHeadOffice = managerService.isSubmitHeadOffice(params);
                                        WfTaskSubmitVo nextVo1 = compensateTaskService.getCompensateSubmitNextVo(compVo.getCompensateNo(),taskVo1.getTaskId().doubleValue(),taskVo1,userVo,autoVerifyFlag,isSubmitHeadOffice);
                                        if(nextVo1.getSubmitLevel()==0){
                                            autoVerifyFlag = true;
                                        }
                                        if(autoVerifyFlag){
                                            // ?????????????????????true????????????????????????????????????
                                            Long uwNotionMainId = verifyClaimService.autoVerifyClaimEndCase(userVo,compVo);
                                            //??????????????????
                                            verifyClaimService.autoVerifyClaimToFlowEndCase(userVo, compVo,uwNotionMainId);
                                            //??????????????????????????????
                                            try{
                                                verifyClaimService.sendCompensateToPayment(uwNotionMainId);
                                            }catch(Exception e){
                       						 	logger.error("?????????="+(lossPropMainVo == null ? null : lossPropMainVo.getRegistNo()) +"??????????????????????????????????????????????????????????????????-------------->",e); 
                                            }
                                        }
                                        
                                    }
                                }
                            }
                            //==============??????????????????
						}
				}
			}
		}
		//?????????????????????
		if(FlowNode.DLProp.equals(nextVo.getNextNode())){
    	   PrpLAcheckVo prpLAcheckVo =acheckService.findPrpLAcheckVo(lossPropMainVo.getRegistNo(),"2", "0","");
    	   if(prpLAcheckVo!=null){
    			prpLAcheckVo.setUnderWriteFlag("7");//??????
    			acheckService.updatePrpLAcheck(prpLAcheckVo);
    	   }
    		
		}
//		session.removeAttribute("checkSubmit_status");
		mv.addObject("userCode",WebUserUtils.getUserCode());
		mv.setViewName("loss-common/NextTaskVeiw");
		mv.addObject("registNo",nextVo.getRegistNo());
		mv.addObject("wfTaskVoList",taskVoList);
		return mv;
	}
	
	

	/**
	 * ?????????????????????????????????
	 * @CreateTime 2015???12???1??? ??????2:36:25
	 * @author lichen
	 */
	@RequestMapping(value="/saveOrUpdatePropCertainLoss")
	@ResponseBody
	public AjaxResult saveOrUpdatePropCertainLoss(
			@FormModel(value="prpLdlossPropMainVo") PrpLdlossPropMainVo prpLdlossPropMainVo,
			@FormModel(value="claimTextVo") PrpLClaimTextVo claimTextVo,
            @FormModel(value="lossChargeVos") List<PrpLDlossChargeVo> lossChargeVos){
		AjaxResult ajaxResult=new AjaxResult();
		try{
			DeflossActionVo deflossVo =new DeflossActionVo();
			deflossVo.setLossPropMainVo(prpLdlossPropMainVo);
			deflossVo.setClaimTextVo(claimTextVo);
			deflossVo.setLossChargeVos(lossChargeVos);
			//????????????
			String retData ="";
			Map<String,String> resultMap = new  HashMap<String,String>();
			if("submitLoss".equals(prpLdlossPropMainVo.getAuditStatus())){
				retData = propLossHandleService.validDefloss(deflossVo);
			}
			if(StringUtils.isNotBlank(retData)){
				ajaxResult.setStatus(HttpStatus.SC_OK);
				resultMap.put("error","1");
				resultMap.put("message",retData);
				ajaxResult.setData(resultMap);
			}else{
				//deflossVo.setSaveType(model);
				SysUserVo sysUserVo = WebUserUtils.getUser();
				propLossHandleService.saveOrUpdateDefloss(deflossVo,sysUserVo);
				//?????????????????????????????????????????? ???????????????????????????
	            //????????????
	             PrpLWfTaskVo prpLWfTaskVo = sendMsgToMobileService.isMobileCaseAcceptInClaim(prpLdlossPropMainVo.getRegistNo(),new BigDecimal(prpLdlossPropMainVo.getFlowTaskId().toString()));
	             if(prpLWfTaskVo != null){ //????????????
	                 prpLWfTaskVo.setHandlerStatus("2"); //?????????
	                 prpLWfTaskVo.setWorkStatus("2");  // ????????????
	                 prpLWfTaskVo.setMobileNo(prpLdlossPropMainVo.getSerialNo().toString());
	                 prpLWfTaskVo.setMobileName(prpLdlossPropMainVo.getLicense());
	                 prpLWfTaskVo.setMobileOperateType(CodeConstants.MobileOperationType.PROPLOSSSAVE);
	                 String url = SpringProperties.getProperty("MClaimPlatform_URL_IN");
	                 interfaceAsyncService.packMsg(prpLWfTaskVo,url);
	             }
				//??????????????????????????????
				String flowTaskId = prpLdlossPropMainVo.getFlowTaskId().toString();
		
				ajaxResult.setStatus(org.apache.http.HttpStatus.SC_OK);
				resultMap.put("error","0");
				resultMap.put("id",deflossVo.getLossPropMainVo().getId()+"");
				resultMap.put("flowTaskId",flowTaskId);
				ajaxResult.setData(resultMap);
			}
		}catch(Exception e){
			ajaxResult.setStatusText(e.getMessage());
			e.printStackTrace();
		}
		
		return ajaxResult;
	}
	
	@RequestMapping(value = "/submitNextPage.do")
	public ModelAndView submitNextPage(Long lossMainId,String flowTaskId,String currentNode,String saveType) throws Exception{//?????? ??????Id,flowTaskId,currentNode
		ModelAndView mv = new ModelAndView();	
		SysUserVo userVo = WebUserUtils.getUser();
		PrpLdlossPropMainVo prplossPropMainVo =propLossService.findPropMainVoById(lossMainId);
		Map<String,Object> params = new HashMap<String,Object>();
		if(flowTaskId == null){
			params.put("taskId",BigDecimal.ZERO.doubleValue());
		}else{

			params.put("taskId",Double.valueOf(flowTaskId));
		}
		params.put("registNo",prplossPropMainVo.getRegistNo());
		String isSubmitHeadOffice = managerService.isSubmitHeadOffice(params);
		SubmitNextVo nextVo=propLossHandleService.organizeNextVo(lossMainId, flowTaskId, currentNode, saveType,userVo,isSubmitHeadOffice);
		
		nextVo.setTaskInUser(WebUserUtils.getUserCode());
		//??????????????????????????????????????????
		nextVo.setAssignCom(WebUserUtils.getComCode());
		nextVo.setAssignUser(WebUserUtils.getUserCode());
		nextVo.setAuditStatus(saveType);
		mv.addObject("nextVo",nextVo);
		mv.addObject("nextNodeMap",nextVo.getNodeMap());
		mv.setViewName("loss-common/PropSubmitNextPage");
		
		return mv;
		
	}
	
	/**
	 * ????????????????????????????????????????????????
	 * @CreateTime 2015???12???3??? ??????2:28:07
	 * @author lichen
	 */
	@RequestMapping(value="/saveView")
	public ModelAndView saveView(Double flowTaskId,Long businessId){	
		//??????????????? ???????????????
		//???????????????????????????
		ModelAndView mv=new ModelAndView();
		mv.addObject("flowTaskId", flowTaskId);
		mv.addObject("businessId", businessId);
		//?????????????????????????????????ID
		mv.addObject("userCode", WebUserUtils.getUserCode());
		return mv;
	}
	
	/**
	 * ????????????????????????
	 * @CreateTime 2015???12???11??? ??????11:54:23
	 * @author lichen
	 */
	@RequestMapping(value="/savePropPerson")
	@ResponseBody
	public AjaxResult savePropPerson(Double flowTaskId,Long businessId,String userCode){
		AjaxResult ajaxResult=new AjaxResult();
		try{
		//?????????????????????
			PrpLdlossPropMainVo lossPropMainVo=propLossService.findPropMainVoById(businessId);
			PrpLWfTaskVo taskVo = wfTaskHandleService.queryTask(flowTaskId);//????????????taskVo
			WfTaskSubmitVo submitVo=new WfTaskSubmitVo();
			submitVo.setFlowId(taskVo.getFlowId());
			submitVo.setFlowTaskId(new BigDecimal(flowTaskId));
			submitVo.setComCode(lossPropMainVo.getComCode());
			submitVo.setTaskInUser(lossPropMainVo.getHandlerName());
			submitVo.setTaskInKey(taskVo.getTaskInKey());
			submitVo.setAssignCom(WebUserUtils.getComCode());
			submitVo.setAssignUser(WebUserUtils.getUserCode());
			submitVo.setCurrentNode(FlowNode.DLProp);
			submitVo.setNextNode(FlowNode.VLProp);
			
			//wfTaskHandleService.submitLossProp(lossPropMainVo, submitVo)
			
			ajaxResult.setStatus(org.apache.http.HttpStatus.SC_OK);
		}catch(Exception e){
			ajaxResult.setStatusText(e.getMessage());
			e.printStackTrace();
		}
		return ajaxResult;
	}
	
	/**
	 * 
	 * @CreateTime 2015???12???11??? ??????11:57:24
	 * @author lichen
	 */
	@RequestMapping(value="/closeView")
	public ModelAndView closeView(){
		//????????????????????????
		
		//?????????????????????
		ModelAndView mv=new ModelAndView();
		mv.setViewName("propLoss/closeView");
		return mv;
	}
	
	@RequestMapping(value="/tempSaveSuccessView")
	public ModelAndView tempSaveView(){
		
		//????????????????????????
		//?????????????????????
		ModelAndView mv=new ModelAndView();
		mv.setViewName("proploss/TempSaveSuccessView");
		return mv;
	}
	
	/** @author Luwei : ????????????????????? */
	@RequestMapping(value="/loadPayCusInfo.ajax")
	@ResponseBody
	public AjaxResult loadPayCusInfo(Long payId){
		AjaxResult ajaxResult = new AjaxResult();
		Map<String,String> payMap = null;
		try{
			payMap = propLossHandleService.getReplacePayMap(payId);
			ajaxResult.setData(payMap);
			ajaxResult.setStatus(HttpStatus.SC_OK);
		}
		catch(Exception e){
			e.printStackTrace();
			ajaxResult.setStatusText(e.getMessage());
		}
		return ajaxResult;
	}
	
	@RequestMapping(value="/deflossView.do")
	public ModelAndView deflossView(String lossId){
		
		ModelAndView mv = new ModelAndView();
		String sindex="1";
		DeflossActionVo deflossVo = propLossHandleService.deflossView(lossId);
	
		//?????????
		Map<String,String> cusMap=propLossHandleService.getPayCustomMap(deflossVo.getRegistNo());
//		//?????? ????????????
//		Map<String,String> intermStanderMap = propLossHandleService.getIntermStanders("0036");
		
		mv.addObject("kindMap", deflossVo.getKindMap());
		mv.addObject("intermMap",deflossVo.getIntermMap());
		mv.addObject("prpLdlossPropMainVo",deflossVo.getLossPropMainVo());
		mv.addObject("claimTextVo",deflossVo.getClaimTextVo());
		mv.addObject("claimTextVos", deflossVo.getClaimTextVos());
		mv.addObject("lossChargeVos",deflossVo.getLossChargeVos());
		mv.addObject("commonVo",deflossVo.getCommonVo());
		mv.addObject("cusMap",cusMap);
		mv.addObject("feeTypeCode", propLossHandleService.getFeeTypeCode());
		//????????????
		mv.addObject("lossCarVos",deflossVo.getLossCarMainList());
		mv.addObject("lossPropVos",deflossVo.getLossPropFeeVos());
		mv.addObject("lossPersTraceList",deflossVo.getLossPersTraceList());
		mv.addObject("sindex",sindex);
//		mv.addObject("nodeCode",wfTaskVo.getNodeCode());
//		mv.addObject("lossPropMainVos",deflossVo.getLossPropMainVos());
		mv.setViewName("propLoss/propDefloss/DeflossEdit");
		return mv;
	}
	
	
	/**
	 * ????????????id ??????????????????
	 * @param lossId
	 * @return
	 */
	@RequestMapping(value = "/VerifyLossView.do")
	public ModelAndView VerifyLossView(String registNo,String lossId) {
		ModelAndView mv=new ModelAndView();
		DeflossActionVo deflossVo=new DeflossActionVo();
		DefCommonVo commonVo=new DefCommonVo();
		//?????????????????????????????????
		PrpLWfTaskVo taskVo = wfTaskHandleService.findEndTask(registNo, lossId,FlowNode.VLProp).get(0);
		
		commonVo.setHandleStatus(taskVo.getHandlerStatus());
		deflossVo.setRegistNo(taskVo.getRegistNo());
		deflossVo.setBusinessId(Long.parseLong(taskVo.getHandlerIdKey()));
		deflossVo.setTaskVo(taskVo);
		SysUserVo userVo = WebUserUtils.getUser();
		propLossHandleService.initPropVerify(deflossVo,userVo);
		
		PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.ASSESSORFEE,WebUserUtils.getComCode());
		String configValue = "0";
		if(configValueVo!=null){
			configValue = configValueVo.getConfigValue();
		}
		mv.addObject("configValue", configValue);  //???????????????
		
		mv.addObject("lossCarVos",deflossVo.getLossCarMainList());
		mv.addObject("lossPropVos",deflossVo.getLossPropFeeVos());
		mv.addObject("lossPersTraceList",deflossVo.getLossPersTraceList());
		mv.addObject("prpLdlossPropMainVo",deflossVo.getLossPropMainVo());
		
		mv.addObject("lossPropMainVo",deflossVo.getLossPropMainVo());
		mv.addObject("claimTextVos",deflossVo.getClaimTextVos());
		mv.addObject("claimTextVo",deflossVo.getClaimTextVo());
		mv.addObject("lossChargeVos",deflossVo.getLossChargeVos());
		mv.addObject("handleStatus", taskVo.getHandlerStatus());
		mv.addObject("taskVo", taskVo);
		mv.addObject("lossPropMainVos",deflossVo.getLossPropMainVos());
		mv.setViewName("propLoss/propVerify/PropVerify_Edit");
		return mv;
	}
	
}
