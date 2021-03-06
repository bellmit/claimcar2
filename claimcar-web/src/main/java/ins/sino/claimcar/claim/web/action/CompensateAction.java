package ins.sino.claimcar.claim.web.action;

import freemarker.core.ParseException;
import ins.framework.service.CodeTranService;
import ins.framework.utils.Beans;
import ins.framework.web.AjaxResult;
import ins.framework.web.bind.annotation.FormModel;
import ins.platform.common.util.ConfigUtil;
import ins.platform.common.web.util.WebUserUtils;
import ins.platform.saa.service.facade.SaaUserPowerService;
import ins.platform.saa.vo.SaaUserPowerVo;
import ins.platform.utils.DataUtils;
import ins.platform.vo.PrpLConfigValueVo;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.CompensateType;
import ins.sino.claimcar.CodeConstants.HandlerStatus;
import ins.sino.claimcar.CodeConstants.KINDCODE;
import ins.sino.claimcar.certify.service.CertifyPubService;
import ins.sino.claimcar.certify.vo.PrpLCertifyMainVo;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.PrpLCheckCarVo;
import ins.sino.claimcar.check.vo.PrpLCheckDutyVo;
import ins.sino.claimcar.check.vo.PrpLCheckVo;
import ins.sino.claimcar.check.vo.PrplcaseStateinfoVo;
import ins.sino.claimcar.claim.service.*;
import ins.sino.claimcar.claim.vo.*;
import ins.sino.claimcar.common.claimtext.service.ClaimTextService;
import ins.sino.claimcar.common.claimtext.vo.PrpLClaimTextVo;
import ins.sino.claimcar.common.losscharge.service.LossChargeService;
import ins.sino.claimcar.constant.Risk;
import ins.sino.claimcar.endcase.service.EndCasePubService;
import ins.sino.claimcar.endcase.vo.PrpLEndCaseVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.AssignService;
import ins.sino.claimcar.flow.service.PayCustomService;
import ins.sino.claimcar.flow.service.WfFlowQueryService;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.AMLVo;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.flow.vo.WfSimpleTaskVo;
import ins.sino.claimcar.flow.vo.WfTaskSubmitVo;
import ins.sino.claimcar.hnbxrest.service.HnfastPayInfoService;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.lossperson.service.PersTraceDubboService;
import ins.sino.claimcar.lossperson.service.PersTraceService;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersInjuredVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceFeeVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceMainVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceVo;
import ins.sino.claimcar.lossprop.service.PropTaskService;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropMainVo;
import ins.sino.claimcar.manager.service.ManagerService;
import ins.sino.claimcar.manager.vo.PrpLPayCustomVo;
import ins.sino.claimcar.middlestagequery.service.ClaimToMiddleStageOfCaseService;
import ins.sino.claimcar.other.service.AssessorService;
import ins.sino.claimcar.other.vo.PrpLAssessorFeeVo;
import ins.sino.claimcar.other.vo.PrpLAssessorMainVo;
import ins.sino.claimcar.pay.service.PadPayService;
import ins.sino.claimcar.realtimequery.service.RealTimeQueryService;
import ins.sino.claimcar.realtimequery.service.VehicleInfoQueryService;
import ins.sino.claimcar.realtimequery.vo.PrpLRealTimeQueryVo;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.PrpLCMainService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.service.RegistService;
import ins.sino.claimcar.regist.service.ScheduleTaskService;
import ins.sino.claimcar.regist.vo.PrpLCInsuredVo;
import ins.sino.claimcar.regist.vo.PrpLCItemKindVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLClaimDeductVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.regist.vo.PrpLcCarDeviceVo;
import ins.sino.claimcar.subrogation.service.SubrogationService;
import ins.sino.claimcar.subrogation.vo.PrpLPlatLockVo;
import ins.sino.claimcar.subrogation.vo.PrpLSubrogationMainVo;
import ins.sino.claimcar.subrogation.vo.PrpLSubrogationPersonVo;
import ins.sino.claimcar.verifyclaim.service.VerifyClaimService;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.ehcache.search.aggregator.Sum;

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




/**
 * @author lanlei
 *
 */
@Controller
@RequestMapping("/compensate")
public class CompensateAction {

	@Autowired
	private RegistQueryService registQueryService;
	@Autowired
	private ClaimService claimService;
	@Autowired
	private PolicyViewService policyViewService;
	@Autowired
	private PadPayService padPayService;
	@Autowired
	private ManagerService managerService;
	@Autowired
	private LossCarService lossCarService;
	@Autowired
	private LossChargeService lossChargeService;
	@Autowired
	private PropTaskService propTaskService;
	@Autowired
	private PersTraceDubboService persTraceDubboService;
	@Autowired
	private CheckTaskService checkTaskService;
	@Autowired
	private WfTaskHandleService wfTaskHandleService;
	@Autowired
	private CompensateTaskService compensateTaskService;
	@Autowired
	private CodeTranService codeTranService;
	@Autowired
	private SubrogationService subrogationService;
	@Autowired
	private CompensateHandleService compHandleService;
	@Autowired
	private PersTraceDubboService persTraceService;
	@Autowired
	private EndCasePubService endCasePubService;
	@Autowired
	private ClaimTextService claimTextService;
	@Autowired
	private AssignService assignService;
	@Autowired
	private CertifyPubService certifyPubService;
	@Autowired
	private WfFlowQueryService wfFlowQueryService;
	@Autowired
	private PersTraceService persTraceServices;
	@Autowired
	private VerifyClaimService verifyClaimService;
	@Autowired
    private AssessorService assessorService;
    @Autowired
    PrpLCMainService prpLCMainService;
    @Autowired
	private PayCustomService payCustomService;
    @Autowired
	HnfastPayInfoService hnfastPayInfoService;
    @Autowired
    SaaUserPowerService saaUserPowerService;
    @Autowired
    ScheduleTaskService scheduleTaskService;
    @Autowired
    ClaimToMiddleStageOfCaseService claimToMiddleStageOfCaseService;
    @Autowired
    RegistService registService;
    @Autowired
    ClaimTaskService claimTaskService;
    @Autowired
    VehicleInfoQueryService vehicleInfoQueryService;
    @Autowired
    RealTimeQueryService realTimeQueryService;
	@Autowired
	InterfaceAsyncService interfaceAsyncService;
	private static Logger logger = LoggerFactory.getLogger(CompensateAction.class);

	/**
	 * ?????????????????????????????????
	 * 
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/compensateEdit.do")
	public ModelAndView CompensateEdit(Double flowTaskId) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
 		SysUserVo userVo = WebUserUtils.getUser();
		CompensateActionVo actionVo = compHandleService.compensateEdit(flowTaskId,userVo); 
		String registNo = actionVo.getCompensateVo().getRegistNo();
		//?????????????????????????????????????????????????????????????????????????????????
		List<PrpLEndCaseVo> listVos = endCasePubService.queryAllByRegistNo(registNo);
		if(listVos!=null && listVos.size() >0){
			modelAndView.addObject("endCaseFlag", "1");//?????????
		}else{
			modelAndView.addObject("endCaseFlag", "0");//?????????
		}
	
		if(actionVo.getCompFlag().equals(CompensateType.COMP_BI)){
			modelAndView.addObject("claimKindMList", actionVo.getClaimKindMList());
			modelAndView.addObject("cprcCase", actionVo.isCprcCase());
			modelAndView.addObject("claimDeductVos", actionVo.getClaimDeductVos());
			modelAndView.addObject("deviceMap", actionVo.getDeviceMap());
		}
		
		PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.STARFLAG);

		if(configValueVo!=null && "1".equals(configValueVo.getConfigValue()) && "3".equals(actionVo.getWfTaskVo().getHandlerStatus()) ){
//			if(padPayMainVo.getPolicePhone() != null){
//				padPayMainVo.setPolicePhone(DataUtils.replacePrivacy(padPayMainVo.getPolicePhone()));
//			}
			if(actionVo.getPaymentVoList() != null && actionVo.getPaymentVoList().size()>0){
				for(int i=0; i<actionVo.getPaymentVoList().size();i++){
					if(actionVo.getPaymentVoList().get(i).getAccountNo() != null){
						actionVo.getPaymentVoList().get(i).setAccountNo(DataUtils.replacePrivacy(actionVo.getPaymentVoList().get(i).getAccountNo()));
					}
				}
			}
			
			if(actionVo.getPrpLChargees() != null && actionVo.getPrpLChargees().size()>0){
				for(int i=0; i<actionVo.getPrpLChargees().size();i++){
					if(actionVo.getPrpLChargees().get(i).getAccountNo() != null){
						actionVo.getPrpLChargees().get(i).setAccountNo(DataUtils.replacePrivacy(actionVo.getPrpLChargees().get(i).getAccountNo()));
					}
				}
			}
		}
		List<PrpLCheckDutyVo> prpLCheckDutyVoList = checkTaskService.findCheckDutyByRegistNo(registNo);
		//?????????????????????????????????????????????????????????;?????????????????????????????????????????????????????????;?????????????????????????????????????????????????????????????????????????????????
		String dutyShowFlag = "N";
		if("1101".equals(actionVo.getCompensateVo().getRiskCode())){
			dutyShowFlag = "Y";// ????????????????????????
		}else{
			List<PrpLClaimVo> claimVoList = claimService.findClaimListByRegistNo(registNo,"1"); 
			if(claimVoList!=null && claimVoList.size()==1 && !"1101".equals(claimVoList.get(0).getRiskCode())){
				// ?????????????????? ?????????????????????
				dutyShowFlag = "Y";
			}
		}
		
		//??????????????????
		String lossCarSign="0";//??????????????????????????????????????????
		if(actionVo.getCompensateVo()!=null){
		    List<PrpLDlossCarMainVo> carMainVos= lossCarService.findLossCarMainByUnderWriteFlag(registNo,"6","0");
	        if(carMainVos!=null && carMainVos.size()>0){
	    	    for(PrpLDlossCarMainVo vo:carMainVos){
	    		    if("1".equals(vo.getUnderWriteFlag())){
	    		    	lossCarSign="1";
	    		    	break;
	    		    }
	    		}
	    	}
		}
         
		
		 //??????????????????????????????????????????
        String compensateSign="0";//?????????????????????????????????
        if(actionVo.getCompensateVo()!=null){
        	List<PrpLCompensateVo> compensates=compensateTaskService.findCompensatevosByRegistNo(registNo);
     	    if(compensates!=null && compensates.size()>0){
     		    for(PrpLCompensateVo vo:compensates){
     		    	if("N".equals(vo.getCompensateType())){
     		    		compensateSign="1";
     		    		break;
     		    	}
     	        }
     		}
        }

	       //????????????
	    PrpLCertifyMainVo certifyMainVo = certifyPubService.findPrpLCertifyMainVoByRegistNo(registNo);
	    String isFraud = "0"; // ???????????? ???
	    String fraudLogo = "03";  //????????????  ???????????? 
	    String isRefuse = "0";  //????????????
	    String refuseReason = "";
	    String compType = "0";
		if (certifyMainVo != null) {
			isFraud = certifyMainVo.getIsFraud();
			fraudLogo = certifyMainVo.getFraudLogo();
			if (actionVo.getCompFlag().equals(CompensateType.COMP_CI)) {
				isRefuse = certifyMainVo.getIsJQFraud();
				compType = "1";
			} else if (actionVo.getCompFlag().equals(CompensateType.COMP_BI)) {
				isRefuse = certifyMainVo.getIsSYFraud();
				compType = "2";
			}
			if (CodeConstants.ValidFlag.VALID.equals(actionVo.getDwPersFlag())) {
				actionVo.getCompensateVo().setRecoveryFlag(
						CodeConstants.ValidFlag.VALID);
			}
			refuseReason = certifyMainVo.getFraudRefuseReason();
		}
		//????????????????????????
		
		PrplcomcontextVo prplcomContextVo=compHandleService.findPrplcomcontextByCompensateNo(actionVo.getCompensateVo().getCompensateNo(),"C");
		if(prplcomContextVo!=null){
			 modelAndView.addObject("prplcomContextVo",prplcomContextVo);
		}else{
			prplcomContextVo=new PrplcomcontextVo();
			modelAndView.addObject("prplcomContextVo",prplcomContextVo);
		}
	   
	
	    //????????????????????????  
	    String isMinorInjuryCases="";
	    if(actionVo.getCompensateVo()!=null){
	    	List<PrpLDlossPersTraceMainVo> traceMains=persTraceServices.findPersTraceMainVo(registNo);
	    	if(traceMains!=null && traceMains.size()>0){
	    		for(PrpLDlossPersTraceMainVo traceMainVo:traceMains){
	    			if(!"1101".equals(actionVo.getCompensateVo().getRiskCode())){
	    				isMinorInjuryCases=traceMainVo.getIsMinorInjuryCases();
	    			}else if("1101".equals(actionVo.getCompensateVo().getRiskCode())){
	    				isMinorInjuryCases=traceMainVo.getIsMinorInjuryCases();
	    			}else{
	    				
	    			}
	    		}
	    	}	
	    }
	    
	    if(StringUtils.isBlank(isMinorInjuryCases)){
	    	isMinorInjuryCases="0";
	    }
	    //??????????????????
	    PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(registNo);
	    if(StringUtils.isNotBlank(prpLRegistVo.getPrpLRegistExt().getLicenseNo())){
	    	modelAndView.addObject("licenseNoSummary",prpLRegistVo.getPrpLRegistExt().getLicenseNo());
	    }
	    modelAndView.addObject("compType",compType);  
	    
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
	  	if(StringUtils.isBlank(actionVo.getCompensateVo().getinsuredPhone())){
	  	  actionVo.getCompensateVo().setinsuredPhone(compensateTaskService.findInsuredPhone(registNo));
	  	}
	  	modelAndView.addObject("assessSign",assessSign);
        modelAndView.addObject("isMinorInjuryCases",isMinorInjuryCases); 
	    modelAndView.addObject("compType",compType);  
	    modelAndView.addObject("refuseReason",refuseReason);  
	    modelAndView.addObject("isRefuse",isRefuse);  
	  	List<PrpLCMainVo> prpLCMains = prpLCMainService.findPrpLCMainsByRegistNo(registNo);
	  	PrpLCMainVo prpLCMain = new PrpLCMainVo();
	  	if(prpLCMains != null && prpLCMains.size() == 2){
            for(PrpLCMainVo vo : prpLCMains){
               if("12".equals(vo.getRiskCode().substring(0, 2))){//?????????
                    prpLCMain = vo;
                }
             }
	       }else{
	             prpLCMain = prpLCMains.get(0);
	    }
	  	String policyNo=prpLCMain.getPolicyNo();
	  	if(prpLCMains!=null && prpLCMains.size()>0){
	  		 for(PrpLCMainVo vo : prpLCMains){
	  			 if(actionVo.getCompFlag().equals(CompensateType.COMP_BI) && !"1101".equals(vo.getRiskCode()) ){
	  				policyNo=vo.getPolicyNo();
	  			 }
	  			if(actionVo.getCompFlag().equals(CompensateType.COMP_CI) && "1101".equals(vo.getRiskCode()) ){
	  				policyNo=vo.getPolicyNo();
	  			 }
	  		 }
	  	}
	  	List<PrpLClaimVo> claimVos = claimService.findClaimListByRegistNo(registNo);
	  	String claimNo="";
	  	String riskCode="";
	  	if(claimVos!=null && claimVos.size()>0){
	  		 for(PrpLClaimVo vo : claimVos){
	  			 if(actionVo.getCompFlag().equals(CompensateType.COMP_BI) && !"1101".equals(vo.getRiskCode()) ){
	  				claimNo=vo.getClaimNo();
	  				riskCode=vo.getRiskCode();
	  			 }
	  			if(actionVo.getCompFlag().equals(CompensateType.COMP_CI) && "1101".equals(vo.getRiskCode()) ){
	  				claimNo=vo.getClaimNo();
	  				riskCode=vo.getRiskCode();
	  			 }
	  		 }
	  	}
	  	//?????????????????????????????? ????????????150???????????????150??????
	  	if(actionVo.getLossPropList()!=null && actionVo.getLossPropList().size()>0){
	  		for(PrpLLossPropVo vo:actionVo.getLossPropList()){
	  			if(StringUtils.isNotBlank(vo.getLossName()) && vo.getLossName().length()>=150){
	  				String str=vo.getLossName().substring(0, 149);
	  				vo.setLossName(str+"???");
	  			}
	  		}
	  	}
	  	
	  	//????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
	  	PrpLDlossPersTraceMainVo persTraceMainVo=new PrpLDlossPersTraceMainVo();
  		List<PrpLDlossPersTraceMainVo> persTracelist=persTraceService.findPrpLDlossPersTraceMainVoListByRegistNoDesc(registNo);
  		if(persTracelist!=null && persTracelist.size()>0){
  				persTraceMainVo=persTracelist.get(0);
  				if(StringUtils.isBlank(actionVo.getCompensateVo().getRecoveryFlag()) && "05".equals(persTraceMainVo.getCaseProcessType())){
  					actionVo.getCompensateVo().setRecoveryFlag("1");
  				}
  				actionVo.getCompensateVo().setPisderoAmout(persTraceMainVo.getIsDeroVerifyAmout());//??????????????????????????????????????????????????????
  				//????????????????????????
  				actionVo.getCompensateVo().setInSideDeroPersonFlag(persTraceMainVo.getInSideDeroFlag());
  		}
  	   //????????????????????????????????????????????????????????????????????????
		String reOpenFlag="0";//0-????????????1-?????????
		String risk="1101";
		if(actionVo.getCompensateVo()!=null && StringUtils.isNotBlank(actionVo.getCompensateVo().getRiskCode())){
			risk=actionVo.getCompensateVo().getRiskCode();
		}
		PrpLWfTaskVo taskVo2=wfTaskHandleService.findOutWfTaskVo(FlowNode.ReOpen.name(),registNo,risk);
		if(taskVo2!=null){
			if(actionVo.getWfTaskVo().getTaskInTime().getTime()>=taskVo2.getTaskOutTime().getTime()){
				reOpenFlag="1";
			}
		}
		
		String claimCompleteFlag="0";//?????????????????????????????????????????????????????????0,1???????????????1(???????????????)???
		if("0".equals(reOpenFlag)){
			List<PrpLWfTaskVo> wfTaskVos=wfTaskHandleService.findTaskOutVo(registNo,FlowNode.Compe.name());
			if(wfTaskVos!=null && wfTaskVos.size()>0){
				for(PrpLWfTaskVo vo:wfTaskVos){
					if("3".equals(vo.getWorkStatus())){
						String backFlag="0";//????????????????????????????????????0-???????????????1-?????????
						List<PrpLWfTaskVo> wftaskins=wfTaskHandleService.findTaskInVo(registNo,vo.getCompensateNo(),FlowNode.Compe.name());
						if(wftaskins!=null && wftaskins.size()>0){
							for(PrpLWfTaskVo invo:wftaskins){
								if("6".equals(invo.getWorkStatus())){
									backFlag="1";
								}
							}
						}
						if("0".equals(backFlag)){
						PrpLCompensateVo compensateVo=compensateTaskService.findCompByPK(vo.getCompensateNo());
						  if(!vo.getCompensateNo().equals(actionVo.getCompensateVo().getCompensateNo()) && !"3".equals(actionVo.getWfTaskVo().getWorkStatus())){
							if(compensateVo!=null && !"7".equals(compensateVo.getUnderwriteFlag())){
								  actionVo.getCompensateVo().setCisderoAmout(compensateVo.getCisderoAmout());
								  actionVo.getCompensateVo().setInSideDeroFlag(compensateVo.getInSideDeroFlag());
							    }
							}
						  //??????????????????
						  if(compensateVo!=null && !"7".equals(compensateVo.getUnderwriteFlag())){
							  claimCompleteFlag="1";
							  break; 
						  }
						  
						}
					}
						
				}
			}
		
		}
		String subrogationLock = "1";
		PrpLSubrogationMainVo prpLSubrogationMainVo = subrogationService.find(registNo);
		if("1".equals(prpLSubrogationMainVo.getSubrogationFlag()) && prpLSubrogationMainVo.getPrpLSubrogationPersons().size()==0){//????????????????????????????????????????????????
            boolean isLock = compHandleService.subrogationIsLock(prpLSubrogationMainVo,policyNo);
            if(!isLock){
                subrogationLock = "0";
            }
        }
		//???????????????
		if(StringUtils.isEmpty(actionVo.getCompensateVo().getCompensateNo())){
			modelAndView.addObject("coinsSize",0);  
		}else{
			List<PrpLCoinsVo> prpLCoinsList = compensateTaskService.findPrpLCoinsByCompensateNo(actionVo.getCompensateVo().getCompensateNo());
			modelAndView.addObject("coinsSize",prpLCoinsList.size());  
		}
		
		//?????????????????????????????????
		
		logger.info(" ????????????" + registNo + " ???????????????" + actionVo.getCompensateVo().getCompensateNo() + " ??????????????????" + actionVo.getCompensateVo().getLcText() + "\n" );
		
		modelAndView.addObject("payrefFlag",actionVo.getPayrefFlag()); 
		modelAndView.addObject("oldClaim",actionVo.getRegistVo().getFlag());
        modelAndView.addObject("subrogationLock",subrogationLock); 
		modelAndView.addObject("reOpenFlag",reOpenFlag);  
		modelAndView.addObject("claimCompleteFlag",claimCompleteFlag); 
	    modelAndView.addObject("riskCode",riskCode);
	  	modelAndView.addObject("policyNo",policyNo);
	  	modelAndView.addObject("claimNo",claimNo);
	    modelAndView.addObject("prpLCMain",prpLCMain); 
	    modelAndView.addObject("isMinorInjuryCases",isMinorInjuryCases); 
	    modelAndView.addObject("isFraud",isFraud);  
	    modelAndView.addObject("fraudLogo",fraudLogo);  
	    modelAndView.addObject("dwPersFlag",actionVo.getDwPersFlag());
	    modelAndView.addObject("compensateSign",compensateSign);  
		modelAndView.addObject("lossCarSign",lossCarSign);
		modelAndView.addObject("dutyShowFlag",dutyShowFlag);
		modelAndView.addObject("prpLCheckDutyVoList",prpLCheckDutyVoList);
		modelAndView.addObject("flag", actionVo.getCompFlag());
		modelAndView.addObject("prpLPadPayPersons", actionVo.getPadPayPersons());
		modelAndView.addObject("prpLPrePayP", actionVo.getPrpLPrePayP());
		modelAndView.addObject("prpLPrePayF", actionVo.getPrpLPrePayF());
		modelAndView.addObject("flowTaskId", flowTaskId);
		modelAndView.addObject("kindForChaMap", actionVo.getKindForChaMap());
		modelAndView.addObject("kindForOthMap", actionVo.getKindForOthMap());
		modelAndView.addObject("prpLWfTaskVo", actionVo.getWfTaskVo());
		modelAndView.addObject("handlerStatus", actionVo.getWfTaskVo().getHandlerStatus());
		modelAndView.addObject("prpLCompensate", actionVo.getCompensateVo());
	   //modelAndView.addObject("prpLKindAmtSummaries", prpLKindAmtSummaries);
		modelAndView.addObject("prpLPaymentVos", actionVo.getPaymentVoList());
		modelAndView.addObject("prpLLossItems", actionVo.getLossItemVoList());
		modelAndView.addObject("prpLLossProps", actionVo.getLossPropList());
		modelAndView.addObject("prpLLossPersons", actionVo.getLossPersonList());
		modelAndView.addObject("prpLChargeVos", actionVo.getPrpLChargees());
		modelAndView.addObject("otherLossProps", actionVo.getOtherLossProps());
		modelAndView.addObject("buJiState", "1");
		modelAndView.addObject("quantity", actionVo.getPersonCount());
		modelAndView.addObject("dwFlag", actionVo.getDwFlag());
		modelAndView.addObject("qfLicenseMap", actionVo.getQfLicenseMap());
		modelAndView.addObject("unitamount", actionVo.getUnitamount());
		modelAndView.addObject("userVo", userVo);
		
		//??????????????????????????????????????????????????????????????????????????????
		String policeInfoFlag="0";//????????????
		String policyComCode = policyViewService.getPolicyComCode(registNo);
		if(policyComCode.startsWith("62")){
			policeInfoFlag = "1";
			String carRiskUrl = SpringProperties.getProperty("CARRISK_URL");
	        String carRiskUserName = SpringProperties.getProperty("SDWARN_YMUSER");
	        String carRiskPassWord = SpringProperties.getProperty("SDWARN_YMPW");
	        modelAndView.addObject("comparePicURL",carRiskUrl);
	        String claimPeriod = "05";
	        modelAndView.addObject("claimPeriod",claimPeriod);
	        modelAndView.addObject("carRiskUserName",carRiskUserName);
	        modelAndView.addObject("carRiskPassWord",carRiskPassWord);
	        //??????????????????
	        String claimSequenceNo = "";
	        if(prpLCMain != null ){//??????????????????
	            if(StringUtils.isNotBlank(prpLCMain.getClaimSequenceNo())){
	                claimSequenceNo = prpLCMain.getClaimSequenceNo();
	               modelAndView.addObject("claimSequenceNo",claimSequenceNo);
	            }
	        }
		}
		modelAndView.addObject("policeInfoFlag",policeInfoFlag);
				
		
        //???????????????ILOG????????????????????????
        String nodeCode=actionVo.getWfTaskVo().getSubNodeCode();
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
		PrpLConfigValueVo configValuesingVo = ConfigUtil.findConfigByCode(CodeConstants.newBillDate);
		boolean showFlag=compensateTaskService.findsingcompay(actionVo.getCompensateVo().getCompensateNo(),DateBillString(configValuesingVo.getConfigValue()));
		modelAndView.addObject("showFlag",showFlag ? "1":"0" );//??????????????????????????????
		modelAndView.addObject("roleFlag",roleFlag);
		modelAndView.addObject("isGbFlag", StringUtils.isNotBlank(actionVo.getRegistVo().getIsGBFlag()) ? actionVo.getRegistVo().getIsGBFlag() : "0");
		modelAndView.setViewName("compensate/CompensateEdit");
		return modelAndView;
		
	}
	
	
	/**
	 * 
	 * <pre></pre>
	 * @param prpLCompensate
	 * @param prpLCheckDutyVoList
	 * @return
	 * @modified:
	 * ???WLL(2016???12???26??? ??????5:37:34): <br>
	 */
	@RequestMapping("/initCompensateLossCarInfo.ajax")
	@ResponseBody
	public ModelAndView initCompensateLossCarInfo(
			@FormModel("prpLCompensate") PrpLCompensateVo prpLCompensate,
			@FormModel("prpLCheckDutyVoList") List<PrpLCheckDutyVo> prpLCheckDutyVoList, 
			@FormModel("dwFlag") String dwFlag,
			@FormModel("flag") String flag){
		ModelAndView modelAndView = new ModelAndView();
		SysUserVo userVo = WebUserUtils.getUser();
		// ????????????CheckDuty
		checkTaskService.saveOrUpdateCheckDutyList(prpLCheckDutyVoList);
		prpLCheckDutyVoList = checkTaskService.findCheckDutyByRegistNo(prpLCompensate.getRegistNo());
		checkTaskService.saveCheckDutyHis(prpLCompensate.getRegistNo(),FlowNode.Compe+prpLCompensate.getCompensateNo());
		// ??????????????????????????????
		List<PrpLLossItemVo> PrpLLossItemVoList = compHandleService.getCarLossInfo(prpLCompensate,prpLCheckDutyVoList,userVo);
		
		modelAndView.addObject("dwFlag",dwFlag);
		modelAndView.addObject("prpLCompensate",prpLCompensate);
		modelAndView.addObject("flag",flag);
		modelAndView.addObject("riskCode",prpLCompensate.getRiskCode());
		modelAndView.addObject("prpLLossItems", PrpLLossItemVoList);
		modelAndView.setViewName("compensate/CompensateEdit_LossTables_Car");
		return modelAndView;
	}
	
	/**
	 * ????????????????????????
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/saveCompensateEdit.do")
	@ResponseBody
	public AjaxResult saveCompensateEdit(
			@FormModel("flowTaskId") Double flowTaskId,
			@FormModel("prpLCompensate") PrpLCompensateVo prpLCompensate,
			@FormModel("prpLLossItem") List<PrpLLossItemVo> prpLLossItemVoList,
			@FormModel("prpLLossProp") List<PrpLLossPropVo> prpLLossPropVoList,
			@FormModel("otherLoss") List<PrpLLossPropVo> otherLossList,
			@FormModel("prpLLossPerson") List<PrpLLossPersonVo> prpLLossPersonVoList,
			@FormModel("prpLCharge") List<PrpLChargeVo> prpLChargeVoList,
			@FormModel("prpLPayment") List<PrpLPaymentVo> prpLPaymentVoList,
			@FormModel("claimDeductVo") List<PrpLClaimDeductVo> claimDeductVoList,
			@FormModel("prpLPlatLockVo") List<PrpLPlatLockVo> prpLPlatLockVoList,
			@FormModel("prpLCheckDutyVoList") List<PrpLCheckDutyVo> prpLCheckDutyVoList,
			@FormModel("subrogationPersonVo") List<PrpLSubrogationPersonVo> subrogationPersonVoList,
			@FormModel("prplcomContextVo") PrplcomcontextVo prplcomcontextVo) {
		
		AjaxResult ajaxResult = new AjaxResult();
		try {
			//?????????????????????????????????????????????
			if(compHandleService.saveBeforeCheck(prpLPaymentVoList)){
				throw new IllegalArgumentException("???????????????????????????????????????????????????");
			}
			//????????????????????????????????????????????????????????????
			if(compHandleService.isAmontEqual(prpLPaymentVoList,prpLCompensate)){
				throw new IllegalArgumentException("?????????????????????????????????????????????????????????");
			}
			Map<String,Object> itemMap = new HashMap<String,Object>();
			itemMap.put("prpLLossItemVoList",prpLLossItemVoList);
			itemMap.put("prpLLossPropVoList",prpLLossPropVoList);
			itemMap.put("prpLCompensate",prpLCompensate);
			itemMap.put("prpLLossPersonVoList",prpLLossPersonVoList);
			//??????????????????
			compHandleService.validData(itemMap);
			
			//???????????????????????????????????????????????????????????????????????????
			PrpLWfTaskVo taskVo = wfTaskHandleService.findTaskIn(flowTaskId);
			if("1".equals(taskVo.getFlag())){
				throw new IllegalArgumentException("??????????????????????????????????????????<br/>");
			}else{
				//??????????????????????????????
				wfTaskHandleService.updateTaskInFlag(flowTaskId,"1");
				if (Risk.isDQZ(prpLCompensate.getRiskCode())
						&& prpLLossItemVoList != null
						&& !prpLLossItemVoList.isEmpty()) {
					for (PrpLLossItemVo lossItemVo : prpLLossItemVoList) {
						if (lossItemVo != null
								&& "4".equals(lossItemVo.getPayFlag())) {
							if (StringUtils.isNotBlank(lossItemVo
									.getInsuredComCode())) {
								lossItemVo.setNoDutyPayFlag("1");
							}
						}
					}
				}
				//???????????????????????????????????????????????????????????????????????????????????????
				checkData(prpLLossPersonVoList);
				
				SysUserVo userVo = WebUserUtils.getUser();
				prpLCompensate.setInSideDeroFlag(prpLCompensate.getInSideDeroFlagValue());
				Date currentDate = new Date();
				
				logger.info("compHandleService.saveCompensateEdit start...?????????="+taskVo.getRegistNo());
				prpLCompensate = compHandleService.saveCompensateEdit(flowTaskId, prpLCompensate,
						prpLLossItemVoList, prpLLossPropVoList, otherLossList,
						prpLLossPersonVoList, prpLChargeVoList, prpLPaymentVoList,
						claimDeductVoList, prpLPlatLockVoList,userVo,prpLCheckDutyVoList);
				logger.info("compHandleService.saveCompensateEdit end, ?????? "+( System.currentTimeMillis()-currentDate.getTime() )+
						"??????,????????????="+prpLCompensate.getCompensateNo());
				currentDate = new Date();
				logger.info("compHandleService.saveOrUpdatePrplcomcontext start...?????????="+taskVo.getRegistNo());
				//??????????????????????????????
				if(prplcomcontextVo!=null && prplcomcontextVo.getId()!=null){
					PrplcomcontextVo prplcomcontextVo1=compHandleService.findPrplcomcontextById(prplcomcontextVo.getId());
					Beans.copy().excludeNull().from(prplcomcontextVo).to(prplcomcontextVo1);
					compHandleService.saveOrUpdatePrplcomcontext(prplcomcontextVo1);
				}else{
					prplcomcontextVo.setCompensateNo(prpLCompensate.getCompensateNo());
					prplcomcontextVo.setCreateTime(new Date());
					prplcomcontextVo.setCreateUser(userVo.getUserCode());
					prplcomcontextVo.setNodeSign("C");
					compHandleService.saveOrUpdatePrplcomcontext(prplcomcontextVo);
				
				}
				logger.info("compHandleService.saveOrUpdatePrplcomcontext end, ?????? "+( System.currentTimeMillis()-currentDate.getTime() )+
						"??????,????????????="+prpLCompensate.getCompensateNo());
				
				
				if(subrogationPersonVoList!=null&&subrogationPersonVoList.size()>0){
					// ??????????????????????????????
					subrogationService.saveSubrogationPers(subrogationPersonVoList);
				}
				compHandleService.syncInsuredPhone(prpLCompensate.getRegistNo(),prpLCompensate.getinsuredPhone());
				// ????????????   dubbo?????????????????????????????????????????????????????????action?????????????????????????????????
				PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(flowTaskId);
				currentDate = new Date();
				logger.info("wfTaskHandleService.tempSaveTaskByComp start...????????????="+wfTaskVo.getCompensateNo());
				if(StringUtils.isBlank(wfTaskVo.getCompensateNo())){
					wfTaskHandleService.tempSaveTaskByComp(wfTaskVo.getTaskId().doubleValue(),prpLCompensate.getCompensateNo(),userVo.getUserCode(),userVo.getComCode());
				}
				logger.info("wfTaskHandleService.tempSaveTaskByComp end, ?????? "+( System.currentTimeMillis()-currentDate.getTime() )+"??????");
				
			}
			ajaxResult.setStatus(HttpStatus.SC_OK);
			ajaxResult.setData(prpLCompensate.getCompensateNo());
		} catch (Exception e) {
			logger.info("??????????????????saveCompensateEdit  doing...flowTaskId="+flowTaskId, e);
			ajaxResult.setStatusText(e.getMessage());
		}finally{
			try{
				wfTaskHandleService.updateTaskInFlag(flowTaskId,"0");
			}catch(Exception e1){
				logger.info("wfTaskHandleService.updateTaskInFlag", e1);
			}
		}

		return ajaxResult;
	}
	
	/**
	 * ???????????????????????????????????????????????????????????????????????????????????????
	 * @param prpLLossPersonVoList
	 */
	public void checkData(List<PrpLLossPersonVo> prpLLossPersonVoList){
		if(prpLLossPersonVoList!=null && prpLLossPersonVoList.size()>0){
			for(PrpLLossPersonVo lossPerson:prpLLossPersonVoList){
				if(lossPerson.getPrpLLossPersonFees()!=null && lossPerson.getPrpLLossPersonFees().size()>0){
					BigDecimal sumRealPay = new BigDecimal(0);
					for(PrpLLossPersonFeeVo lossPersonFee:lossPerson.getPrpLLossPersonFees()){
						sumRealPay = sumRealPay.add(NullToZero(lossPersonFee.getFeeRealPay()));
					}
					if(NullToZero(lossPerson.getSumRealPay()).compareTo(sumRealPay)!=0){
						//feeRealPay_1????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
						PrpLLossPersonFeeVo lossPersonFee_1 = lossPerson.getPrpLLossPersonFees().get(0);
						BigDecimal data_1 = NullToZero(lossPersonFee_1.getFeeLoss()).
								subtract(NullToZero(lossPersonFee_1.getFeeOffLoss())).subtract(NullToZero(lossPersonFee_1.getBzPaidLoss()));
						BigDecimal data_2 = NullToZero(lossPerson.getSumLoss()).
								subtract(NullToZero(lossPerson.getSumOffLoss())).subtract(NullToZero(lossPerson.getBzPaidLoss()));
						BigDecimal feeRealPay_1 = data_1.divide(data_2, 2, RoundingMode.HALF_UP).multiply(NullToZero(lossPerson.getSumRealPay()));
						BigDecimal feeRealPay_2 = lossPerson.getSumRealPay().subtract(feeRealPay_1);
						
						lossPerson.getPrpLLossPersonFees().get(0).setFeeRealPay(feeRealPay_1);
						lossPerson.getPrpLLossPersonFees().get(1).setFeeRealPay(feeRealPay_2);
					}
				}
			}
		}
	}

	/**
	 * ????????????
	 * 
	 * @param compensateNo
	 * @param flowTaskId
	 * @return
	 */
	@RequestMapping("/compCancel.do")
	@ResponseBody
	public AjaxResult compCancel(String claimNo) {
		AjaxResult ajaxResult = new AjaxResult();
		// PrpLCompensateVo compVo =
		// compensatateService.findCompByPK(compensateNo);

		// ???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
		/* if (compVo != null) { */
		PrpLEndCaseVo vo = endCasePubService.findEndCaseByClaimNo(claimNo);
		PrpLClaimVo claimVo = claimService.findByClaimNo(claimNo);
		List<PrpLClaimVo> claimVoList = claimService.findClaimListByRegistNo(claimVo.getRegistNo());
		boolean flags = false;
		if (vo != null) {// ??????
			ajaxResult.setStatusText("1");
		} else {
			if(claimVoList.size()==1){
				ajaxResult.setStatusText("0");
			}else{
				if ("12".equals(claimVo.getRiskCode().substring(0, 2))) {// ??????
					flags = wfTaskHandleService.existCancelByNAndH(
							claimVo.getRegistNo(), "CompeCI");
					if (flags) {
						ajaxResult.setStatusText("11");
					} else {
						ajaxResult.setStatusText("0");
					}
	
				} else if ("11".equals(claimVo.getRiskCode().substring(0, 2))) {// ??????
					flags = wfTaskHandleService.existCancelByNAndH(
							claimVo.getRegistNo(), "CompeBI");
					if (flags) {
						ajaxResult.setStatusText("12");
					} else {
						ajaxResult.setStatusText("0");
					}
				} else {
					ajaxResult.setStatusText("0");
				}
			
			}
		}
		/* } */
		/*
		 * if (compVo != null && flowTaskId != null) {
		 * compensatateService.cancelCompensates(compVo, flowTaskId);
		 * ajaxResult.setStatus(HttpStatus.SC_OK); }else if(compVo == null &&
		 * flowTaskId != null){
		 * compensatateService.cancelCompensate(flowTaskId);
		 * ajaxResult.setStatus(HttpStatus.SC_OK); }else{
		 * ajaxResult.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR); }
		 */
		ajaxResult.setStatus(HttpStatus.SC_OK);
		return ajaxResult;
	}

	/**
	 * ????????????
	 * 
	 * @param compensateNo
	 * @param flowTaskId
	 * @return
	 */
	@RequestMapping("/compCancels.do")
	@ResponseBody
	public AjaxResult compCancels(String compensateNo, BigDecimal flowTaskId,
			String claimNo) {
		logger.info("?????????" + claimNo + ",?????????flowTaskId=" + flowTaskId +"??????????????????");
		AjaxResult ajaxResult = new AjaxResult();
		PrpLCompensateVo compVo = compensateTaskService.findPrpLCompensateVoByPK(compensateNo);
		SysUserVo sysUserVo = WebUserUtils.getUser();
		if (compVo != null && flowTaskId != null) {
			logger.info("?????????" + claimNo + "???flowtaskid=" + flowTaskId  + "??????????????????" + compensateNo + "??????????????????????????????");
			compensateTaskService.cancelCompensates(compVo, flowTaskId,sysUserVo);
			ajaxResult.setStatus(HttpStatus.SC_OK);
		} else if (compVo == null && flowTaskId != null) {
			logger.info("?????????" + claimNo + "???flowtaskid=" + flowTaskId  + "????????????????????????");
			compensateTaskService.cancelCompensates(flowTaskId,sysUserVo);
			ajaxResult.setStatus(HttpStatus.SC_OK);
		} else {
			ajaxResult.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		}
		ajaxResult.setStatus(HttpStatus.SC_OK);
		logger.info("?????????" + claimNo + ",?????????flowTaskId=" + flowTaskId +"??????????????????");
		return ajaxResult;
	}

	/**
	 * ????????????????????????
	 * 
	 * @param compensateNo
	 * @param flowTaskId
	 * @return
	 */
	@RequestMapping("/compCancelInit.do")
	@ResponseBody
	public ModelAndView compCancelInit(String registNo) {
		List<PrpLDlossCarMainVo> carVos = lossCarService
				.findLossCarMainByRegistNo(registNo);// ????????????
		if (carVos != null && carVos.size() > 0) {
			for (PrpLDlossCarMainVo vo : carVos) {
				if (vo.getSerialNo() > 1) {
					vo.setRemark("?????????");
				} else {
					String remark = codeTranService.transCode(
							"DefLossItemType", vo.getSerialNo().toString());
					vo.setRemark(remark);
				}
			}
		}
		List<PrpLDlossPersTraceMainVo> voList = persTraceDubboService
				.findPersTraceMainVoList(registNo);
		if (voList != null && voList.size() > 0) {
			for (PrpLDlossPersTraceMainVo vo : voList) {
				List<PrpLDlossPersTraceVo> persTraceVos = persTraceDubboService.findPersTraceVo(vo.getRegistNo(), vo.getId());
				String remark = "";
				if(persTraceVos!=null && persTraceVos.size() > 0){
					for (PrpLDlossPersTraceVo persTraceVo : persTraceVos) {
						remark += codeTranService.transCode("LossItemType",persTraceVo.getPrpLDlossPersInjured().getLossItemType())
								+ "-"
								+ persTraceVo.getPrpLDlossPersInjured().getPersonName() + ";";
					}
				}
				vo.setRemark(remark);
			}
		}

		List<PrpLdlossPropMainVo> propVos = propTaskService
				.findPrpLdlossPropMainVoListByCondition(registNo, null, null,
						null);
		/*
		 * List<PrpLdlossPropMainVo> propVoList = new
		 * ArrayList<PrpLdlossPropMainVo>(); for(PrpLdlossPropMainVo
		 * vo:propVos){ if(vo.getSerialNo() > 1){ propVoList.add(vo); } }
		 */
		if (propVos != null && propVos.size() > 0) {
			for (PrpLdlossPropMainVo vo : propVos) {

				if (vo.getSerialNo() > 1) {
					vo.setRemark("?????????");
				} else if (vo.getSerialNo() == 1) {
					vo.setRemark("?????????");
				} else if (vo.getSerialNo() == 0) {
					vo.setRemark("?????????");
				}
			}
		}
		List<PrpLDlossPersInjuredVo> vos = persTraceDubboService
				.findPersInjuredByRegistNo(registNo);
		String remark = "";
		if (vos != null && vos.size() > 0) {
			for (PrpLDlossPersInjuredVo vo : vos) {
				remark = codeTranService.transCode("LossItemType",
						vo.getLossItemType())
						+ "-" + vo.getPersonName();
				vo.setPersonName(remark);
			}
		}
		ModelAndView mv = new ModelAndView();
		mv.addObject("carVos", carVos);
		mv.addObject("propVos", propVos);
		mv.addObject("persInjuredVo", voList);
		mv.setViewName("compensate/cancelInit");
		return mv;
	}

	// ????????????
	@RequestMapping(value = "/compCancelSubmit.do", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult compCancelSubmit(Long[] persInjuredVo, Long[] propVos,
			Long[] carVos, String remarks, String compensateNo,
			BigDecimal flowTaskId) throws Exception{
		logger.info("?????????flowTaskId=" + flowTaskId +"?????????????????????????????????");
		AjaxResult ajaxResult = new AjaxResult();
		SysUserVo sysUserVo = WebUserUtils.getUser();
		String retStr = "";
		PrpLCompensateVo compVo = compensateTaskService.findCompByPK(compensateNo);
		PrpLWfTaskVo prpLWfTaskVo = wfTaskHandleService.findTaskIn(flowTaskId.doubleValue());
		String registNo = prpLWfTaskVo.getRegistNo();		
		//???????????? ???????????????????????? ?????????????????????????????? ?????????????????????
		boolean notAllow = false;
		List<PrpLdlossPropMainVo> prpLdlossPropMainVos = propTaskService.findPropMainListByRegistNo(registNo);
		if(prpLdlossPropMainVos==null){
			List<PrpLDlossPersTraceMainVo> prpLDlossPersTraceMainVos = persTraceService.findPersTraceMainVoList(registNo);
			if(prpLDlossPersTraceMainVos==null){
				if(carVos.length==0){
					List<PrpLDlossCarMainVo> prpLDlossCarMainVos = lossCarService.findLossCarMainByRegistNo(registNo);
					if(prpLDlossCarMainVos!=null){
						for (PrpLDlossCarMainVo prpLDlossCarMainVo : prpLDlossCarMainVos) {
							if("1".equals(prpLDlossCarMainVo.getReCheckFlag())){
								notAllow = true;
							}else {
								notAllow = false;
								break;
							}
						}
					}
				}
			}
		}
		if(notAllow){
			ajaxResult.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			ajaxResult.setStatusText("???????????????????????????????????????????????????????????????????????????????????????????????????????????????");
			return ajaxResult;
		}
		if (compVo != null && flowTaskId != null) {
			logger.info("????????????" + compensateNo + ",?????????flowTaskId=" + flowTaskId + "??????????????????" + compensateNo + "??????????????????????????????");
			compensateTaskService.cancelCompensates(compVo, flowTaskId,sysUserVo);
			ajaxResult.setStatus(HttpStatus.SC_OK);
		} else if (compVo == null && flowTaskId != null) {
			logger.info("?????????flowTaskId=" + flowTaskId + "????????????????????????");
			compensateTaskService.cancelCompensates(flowTaskId,sysUserVo);
			ajaxResult.setStatus(HttpStatus.SC_OK);
		} else {
			ajaxResult.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		}
		try {
			// ??????????????????
			for (int i = 0; i < carVos.length; i++) {
				
				sysUserVo.setUserCode(WebUserUtils.getUserCode());
				sysUserVo.setUserName(WebUserUtils.getUserName());
				sysUserVo.setComCode(WebUserUtils.getComCode());
//				sysUserVo.setComName(WebUserUtils.getComName());
				String flags = lossCarService.carModifyLaunch(carVos[i],
						sysUserVo);

				// ????????????PrpLDlossCarMainVo findLossCarMainById
				PrpLDlossCarMainVo vo = lossCarService
						.findLossCarMainById(carVos[i]);
				if (!flags.equals("ok")) {
					if (vo.getSerialNo() > 1) {
						retStr = retStr + "?????????";
					} else {
						String remark = codeTranService.transCode(
								"DefLossItemType", vo.getSerialNo().toString());
						retStr = retStr + remark;
					}
					retStr = retStr + "(" + vo.getLicenseNo() + ")" + flags;
				}
				PrpLClaimTextVo claimTextVo = new PrpLClaimTextVo();
				claimTextVo.setBussTaskId(carVos[i]);
				claimTextVo.setBigNode(FlowNode.DLCar.toString());
				claimTextVo.setNodeCode(FlowNode.DLCarMod.toString());
				claimTextVo.setBussNo(vo.getRegistNo());
				claimTextVo.setRegistNo(vo.getRegistNo());
				claimTextVo.setTextType("2");
				claimTextVo.setDescription(remarks);
				claimTextVo.setOperatorCode(WebUserUtils.getUserCode());
				claimTextVo.setOperatorName(WebUserUtils.getUserName());
				claimTextVo.setCreateTime(new Date());
				claimTextVo.setUpdateTime(new Date());
				claimTextVo.setCreateUser(WebUserUtils.getUserCode());
				claimTextVo.setUpdateUser(WebUserUtils.getUserCode());
				claimTextVo.setFlag("1");
				claimTextVo.setRemark(remarks);
				claimTextVo.setStatus("??????????????????");
				claimTextVo.setInputTime(new Date());
				claimTextVo.setComCode(WebUserUtils.getComCode());
				claimTextVo.setSumLossFee(vo.getSumVeriLossFee());
				claimTextVo.setOpinionCode("??????????????????");
//				claimTextVo.setComName(WebUserUtils.getComName());
				claimTextService.saveOrUpdte(claimTextVo);
			}

			// ????????????????????????
			for (int i = 0; i < propVos.length; i++) {
				sysUserVo.setUserCode(WebUserUtils.getUserCode());
				sysUserVo.setUserName(WebUserUtils.getUserName());
				sysUserVo.setComCode(WebUserUtils.getComCode());
//				sysUserVo.setComName(WebUserUtils.getComName());
				String flags = propTaskService.propModifyLaunch(propVos[i],
						sysUserVo);
				// ????????????
				PrpLdlossPropMainVo vo = propTaskService
						.findPropMainVoById(propVos[i]);

				if (!flags.equals("ok")) {
					/*
					 * if(vo.getSerialNo()>1){ retStr = retStr+"?????????"; }else{
					 * String remark =
					 * codeTranService.transCode("DefLossItemType"
					 * ,vo.getSerialNo().toString()); retStr = retStr+remark; }
					 */
					if (vo.getSerialNo() > 1) {
						retStr = retStr + "?????????";
					} else if (vo.getSerialNo() == 1) {
						retStr = retStr + "?????????";
					} else if (vo.getSerialNo() == 0) {
						retStr = retStr + "?????????";
					}
					retStr = retStr + "(" + vo.getLicense() + ")" + flags;
				}
				PrpLClaimTextVo claimTextVo = new PrpLClaimTextVo();
				claimTextVo.setBussTaskId(propVos[i]);
				claimTextVo.setBigNode(FlowNode.DLProp.toString());
				claimTextVo.setNodeCode(FlowNode.DLPropMod.toString());
				claimTextVo.setBussNo(vo.getRegistNo());
				claimTextVo.setRegistNo(vo.getRegistNo());
				claimTextVo.setTextType("2");
				claimTextVo.setDescription(remarks);
				claimTextVo.setOperatorCode(WebUserUtils.getUserCode());
//				claimTextVo.setOperatorName(WebUserUtils.getComName());
				claimTextVo.setComCode(WebUserUtils.getComCode());
//				claimTextVo.setComName(WebUserUtils.getComName());
				claimTextVo.setOperatorName(WebUserUtils.getUserName());
				claimTextVo.setCreateTime(new Date());
				claimTextVo.setUpdateTime(new Date());
				claimTextVo.setCreateUser(WebUserUtils.getUserCode());
				claimTextVo.setUpdateUser(WebUserUtils.getUserCode());
				claimTextVo.setFlag("1");
				claimTextVo.setRemark(remarks);
				claimTextVo.setStatus("??????????????????");
				claimTextVo.setSumLossFee(vo.getSumVeriLoss()); 
				claimTextVo.setInputTime(new Date());
				claimTextVo.setOpinionCode("??????????????????");
				claimTextService.saveOrUpdte(claimTextVo);
			}

			// ????????????
			for (int i = 0; i < persInjuredVo.length; i++) {
				PrpLDlossPersTraceMainVo persTraceMainVo = persTraceService
						.findPersTraceMainVoById(persInjuredVo[i]);
				List<PrpLDlossPersTraceVo> persTraceVos = persTraceService
						.findPersTraceVo(persTraceMainVo.getRegistNo(),
								persInjuredVo[i]);
				persTraceMainVo.setPrpLDlossPersTraces(persTraceVos);

				if (wfTaskHandleService.existTaskByNodeCode(
						persTraceMainVo.getRegistNo(), FlowNode.PLNext,
						persTraceMainVo.getId().toString(), "0")) {
					retStr = retStr + "????????????????????????????????????????????????";
				}

				// ??????????????????????????????
				PrpLWfTaskVo taskVo = null;
				List<PrpLWfTaskVo> taskVo1 = wfTaskHandleService.findEndTask(persTraceMainVo.getRegistNo(),
						persInjuredVo[i].toString(), FlowNode.PLNext);
				
				if(taskVo1==null || taskVo1.size()==0){
				List<PrpLWfTaskVo> taskVo2 = wfTaskHandleService.findEndTask(persTraceMainVo.getRegistNo(),
						persInjuredVo[i].toString(), FlowNode.PLFirst);
					taskVo = taskVo2.get(0);
				}else{
					taskVo = taskVo1.get(0);
				}
				List<PrpLWfTaskVo> taskVo3 = wfTaskHandleService.findEndTask(persTraceMainVo.getRegistNo(),
						persInjuredVo[i].toString(), FlowNode.PLoss);
				
				WfTaskSubmitVo taskSubmitVo = new WfTaskSubmitVo();
				taskSubmitVo.setCurrentNode(FlowNode.valueOf(taskVo
						.getNodeCode()));
				taskSubmitVo.setFlowId(taskVo3.get(0).getFlowId());
				taskSubmitVo.setFlowTaskId(taskVo3.get(0).getTaskId());
				taskSubmitVo.setNextNode(FlowNode.PLNext);
				taskSubmitVo.setComCode(WebUserUtils.getComCode());
				taskSubmitVo.setTaskInUser(WebUserUtils.getUserCode());
				taskSubmitVo.setTaskInKey(persTraceMainVo.getId().toString());
				// ??????????????????
				taskSubmitVo.setAssignCom(taskVo.getHandlerCom());
				taskSubmitVo.setAssignUser(taskVo.getHandlerUser());

				WfSimpleTaskVo simpleTaskVo = new WfSimpleTaskVo();
				simpleTaskVo.setRegistNo(persTraceMainVo.getRegistNo());
				simpleTaskVo
						.setHandlerIdKey(persTraceMainVo.getId().toString());

				PrpLWfTaskVo wfTaskVo = wfTaskHandleService.addSimpleTask(
						simpleTaskVo, taskSubmitVo);

				// ??????????????????
				persTraceMainVo.setUnderwriteFlag("1");
				persTraceMainVo.setFlag("3");

				persTraceService.saveOrUpdatePersTraceMain(persTraceMainVo);
				// ????????????

				PrpLClaimTextVo claimTextVo = new PrpLClaimTextVo();
				claimTextVo.setBussTaskId(persInjuredVo[i]);
				claimTextVo.setBigNode(FlowNode.PLoss.toString());
				claimTextVo.setNodeCode(FlowNode.Compe.toString());
				claimTextVo.setBussNo(persTraceMainVo.getRegistNo());
				claimTextVo.setRegistNo(persTraceMainVo.getRegistNo());
				claimTextVo.setTextType("2");
				claimTextVo.setDescription(remarks);
				claimTextVo.setOperatorCode(WebUserUtils.getUserCode());
//				claimTextVo.setOperatorName(WebUserUtils.getComName());
				claimTextVo.setComCode(WebUserUtils.getComCode());
//				claimTextVo.setComName(WebUserUtils.getComName());
				claimTextVo.setOperatorName(WebUserUtils.getUserName());
				claimTextVo.setCreateTime(new Date());
				claimTextVo.setUpdateTime(new Date());
				claimTextVo.setCreateUser(WebUserUtils.getUserCode());
				claimTextVo.setUpdateUser(WebUserUtils.getUserCode());
				claimTextVo.setFlag("1");
				claimTextVo.setRemark(remarks);
				claimTextVo.setStatus("??????????????????");
				claimTextVo.setInputTime(new Date());
				claimTextService.saveOrUpdte(claimTextVo);
			}
		} catch (Exception e) {
			ajaxResult.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			ajaxResult.setStatusText(e.getMessage());
			logger.error("flowTaskId=" + flowTaskId  + "????????????????????????????????????",e);
		}
		/*
		 * mv.addObject("persInjuredVo",persInjuredVo);
		 * mv.setViewName("compensate/cancelInit");
		 */

		// ajaxResult.setStatus(HttpStatus.SC_OK);
		ajaxResult.setStatusText(retStr);
		logger.info("?????????flowTaskId=" + flowTaskId +"?????????????????????????????????");
		return ajaxResult;
	}

	/**
	 * ??????????????????
	 * 
	 * @return
	 * @modified: ???XMSH(2016???2???25??? ??????10:53:37): <br>
	 */
	@RequestMapping("/loadSubmitChargeAdjust.ajax")
	@ResponseBody
	public ModelAndView loadSubmitChargeAdjust() {
		ModelAndView mav = new ModelAndView();
		SubmitNextVo nextVo = new SubmitNextVo();
		nextVo.setNextNode(FlowNode.PLNext.name());
		nextVo.setNextName(FlowNode.PLNext.getName());

		nextVo.setCurrentName(FlowNode.PLChargeMod.getName());
		nextVo.setCurrentNode(FlowNode.PLChargeMod.name());
		// nextVo.setFlowId(flowId);
		// ??????????????????????????????
		nextVo.setAssignUser(WebUserUtils.getUserName());
		nextVo.setAssignCom(WebUserUtils.getComCode());
		nextVo.setComCode(WebUserUtils.getComCode());

		mav.addObject("nextVo", nextVo);
		mav.setViewName("compensate/SubmitChargeMod");
		/*
		 * mav.addObject("LoadPagePath",
		 * "lossperson/persTraceChargeAdjust/SubmitChargeMod.jspf");
		 * mav.setViewName("app/LoadAjaxPage");
		 */
		return mav;
	}

	// ????????????
	@RequestMapping(value = "/submitNextNode.do")
	@ResponseBody
	public AjaxResult submitNextNode(Long[] persInjuredVo, String remarks) throws Exception {
		AjaxResult ajaxResult = new AjaxResult();
		try {
			if(persInjuredVo.length > 0){
			PrpLDlossPersTraceMainVo persTraceMainVo = persTraceService.findPersTraceMainVoById(persInjuredVo[0]);
			List<PrpLDlossPersTraceVo> persTraceVos = persTraceService.findPersTraceVo(persTraceMainVo.getRegistNo(),persInjuredVo[0]);
			persTraceMainVo.setPrpLDlossPersTraces(persTraceVos);

			if (wfTaskHandleService.existTaskByNodeCode(
					persTraceMainVo.getRegistNo(), FlowNode.PLNext,
					persTraceMainVo.getId().toString(), "0")) {
				throw new Exception("???????????????????????????????????????????????????????????????");
			}

			// ??????????????????????????????
		/*	PrpLWfTaskVo taskVo = wfTaskHandleService.findEndTask(
					persTraceMainVo.getRegistNo(), persTraceMainId.toString(),
					FlowNode.PLoss).get(0);*/
			PrpLWfTaskVo taskVo = null;
			List<PrpLWfTaskVo> taskVo1 = wfTaskHandleService.findEndTask(persTraceMainVo.getRegistNo(),
					persInjuredVo[0].toString(), FlowNode.PLNext);
			
			if(taskVo1==null || taskVo1.size()==0){
			List<PrpLWfTaskVo> taskVo2 = wfTaskHandleService.findEndTask(persTraceMainVo.getRegistNo(),
					persInjuredVo[0].toString(), FlowNode.PLFirst);
				taskVo = taskVo2.get(0);
			}else{
				taskVo = taskVo1.get(0);
			}

			WfTaskSubmitVo taskSubmitVo = new WfTaskSubmitVo();
			taskSubmitVo.setCurrentNode(FlowNode.valueOf(taskVo.getNodeCode()));
			taskSubmitVo.setFlowId(taskVo.getFlowId());
			taskSubmitVo.setFlowTaskId(taskVo.getTaskId());
			taskSubmitVo.setNextNode(FlowNode.PLCharge);
			taskSubmitVo.setComCode(WebUserUtils.getComCode());
			taskSubmitVo.setTaskInUser(WebUserUtils.getUserCode());
			taskSubmitVo.setTaskInKey(persTraceMainVo.getId().toString());
			// ??????????????????
			taskSubmitVo.setAssignCom(taskVo.getHandlerCom());
			taskSubmitVo.setAssignUser(taskVo.getHandlerUser());

			WfSimpleTaskVo simpleTaskVo = new WfSimpleTaskVo();
			simpleTaskVo.setRegistNo(persTraceMainVo.getRegistNo());
			simpleTaskVo.setHandlerIdKey(persTraceMainVo.getId().toString());

			PrpLWfTaskVo wfTaskVo = wfTaskHandleService.addSimpleTask(
					simpleTaskVo, taskSubmitVo);

			// ??????????????????
			persTraceMainVo.setUnderwriteFlag("1");
			persTraceService.saveOrUpdatePersTraceMain(persTraceMainVo);
			
			
			// ????????????

			PrpLClaimTextVo claimTextVo = new PrpLClaimTextVo();
			claimTextVo.setBussTaskId(persInjuredVo[0]);
			claimTextVo.setBigNode(FlowNode.PLoss.toString());
			claimTextVo.setNodeCode(FlowNode.PLCharge.toString());
			claimTextVo.setBussNo(persTraceMainVo.getRegistNo());
			claimTextVo.setRegistNo(persTraceMainVo.getRegistNo());
			claimTextVo.setTextType("2");
			claimTextVo.setDescription(remarks);
			claimTextVo.setOperatorCode(WebUserUtils.getUserCode());
			claimTextVo.setComCode(WebUserUtils.getComCode());
			claimTextVo.setOperatorName(WebUserUtils.getUserName());
			claimTextVo.setCreateTime(new Date());
			claimTextVo.setUpdateTime(new Date());
			claimTextVo.setCreateUser(WebUserUtils.getUserCode());
			claimTextVo.setUpdateUser(WebUserUtils.getUserCode());
			claimTextVo.setFlag("1");
			claimTextVo.setRemark(remarks);
			claimTextVo.setStatus("??????????????????");
			claimTextVo.setInputTime(new Date());
			claimTextService.saveOrUpdte(claimTextVo);
			ajaxResult.setStatus(HttpStatus.SC_OK);
			}
		} catch (Exception e) {
			ajaxResult.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			ajaxResult.setStatusText(e.getMessage());
			e.printStackTrace();
		}
		return ajaxResult;
	}

	/**
	 * ??????????????????
	 * <pre></pre>
	 * @param compensateId
	 * @param flowTaskId
	 * @param claimNo
	 * @param sumAmtNow ??????????????????
	 * @return
	 * @throws Exception 
	 * @modified:
	 * ???WLL(2017???4???24??? ??????8:07:38): <br>
	 */
	@RequestMapping("/initNextTaskView.do")
	@ResponseBody
	public ModelAndView initNextTaskView(String compensateId, Double flowTaskId,String claimNo,String registNo,BigDecimal sumAmtNow) throws Exception {
		PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(flowTaskId);
		ModelAndView mv = new ModelAndView();
		WfTaskSubmitVo nextVo = new WfTaskSubmitVo();
		SysUserVo userVo = WebUserUtils.getUser();
		Boolean autoVerifyFlag = false;
		
		//??????????????????????????????
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("registNo",registNo);
		params.put("taskId",flowTaskId);
		String isSubmitHeadOffice = managerService.isSubmitHeadOffice(params);
		nextVo = compensateTaskService.getCompensateSubmitNextVo(compensateId,flowTaskId,wfTaskVo,userVo,autoVerifyFlag,isSubmitHeadOffice);
		
		if(nextVo.getSubmitLevel()==0){
			autoVerifyFlag = true;//TODO ????????????autoVerifyFlag???????????? ??????????????????api???????????????
		}
		mv.addObject("nextVo", nextVo);
		mv.addObject("compensateNo",wfTaskVo.getCompensateNo());
		mv.addObject("registNo",registNo);
//		mv.addObject("nextNodeMap", FlowNode.valueOf(nextNode).getName());
		mv.addObject("nextNodeMap", nextVo.getNextNode().getName());
		mv.addObject("autoVerifyFlag",autoVerifyFlag);
		mv.setViewName("compensate/NextTaskView");
		return mv;
	}

	/**
	 * ???????????????????????????
	 * @param request
	 * @return
	 */
	@RequestMapping("/submitCompensateEdit.do")
	@ResponseBody
	public ModelAndView submitCompensateEdit(
			@FormModel("nextVo") WfTaskSubmitVo nextVo,
			@FormModel("autoVerifyFlag") String autoVerifyFlag) {
		ModelAndView mv = new ModelAndView();
		SysUserVo userVo = WebUserUtils.getUser();
		nextVo.setHandleruser(userVo.getUserCode());
		PrpLWfTaskVo taskVo = wfTaskHandleService.queryTask(nextVo.getFlowTaskId().doubleValue());
		PrpLWfTaskVo upTaskVo = wfTaskHandleService.queryTask(taskVo.getUpperTaskId().doubleValue());
		PrpLCompensateVo prpLCompensateVo = compensateTaskService.findPrpLCompensateVoByPK(nextVo.getTaskInKey());
		PrpLWfTaskVo wfTaskVo = compensateTaskService.submitCompeWfTaskVo(prpLCompensateVo,upTaskVo,nextVo,autoVerifyFlag,userVo);

		//================
		if("true".equals(autoVerifyFlag.trim())){
              // ?????????????????????true????????????????????????????????????
              Long uwNotionMainId = verifyClaimService.autoVerifyClaimEndCase(userVo,prpLCompensateVo);
              //??????????????????
              verifyClaimService.autoVerifyClaimToFlowEndCase(userVo, prpLCompensateVo,uwNotionMainId);
              //??????????????????????????????
              try{
                  verifyClaimService.sendCompensateToPayment(uwNotionMainId);
              }catch(Exception e){
                  e.printStackTrace();
              }
          }

		//??????????????????????????????rabbitMq???????????????????????????
		claimToMiddleStageOfCaseService.middleStageQuery(wfTaskVo.getRegistNo(), "Compe");
		//??????????????????????????????????????????????????????????????????
		if(wfTaskVo != null){
			List<PrpLClaimVo> claimList = claimTaskService.findClaimListByRegistNo(wfTaskVo.getRegistNo(), "1");
			if(claimList != null && claimList.size() > 0){
				if(claimList.size() == 1){
					//?????? ??????????????????????????????????????????
					try {
						interfaceAsyncService.sendRealTimeInfoQuery(wfTaskVo,userVo);
					} catch (Exception e) {
						logger.info("??????????????????????????????", e);
						e.printStackTrace();
						throw new IllegalArgumentException("??????????????????????????????" + e.getMessage());
					}
				}else{
					//??????+?????? ????????????????????????????????????
					int curTimes = prpLCompensateVo.getTimes();
					String curClaimNo = prpLCompensateVo.getClaimNo();
					String otherClaimNo = "";
					for (PrpLClaimVo prpLClaimVo : claimList) {
						if(!curClaimNo.equals(prpLClaimVo.getClaimNo())){
							otherClaimNo = prpLClaimVo.getClaimNo();
						}
					}
					//??????????????????????????????????????????
					List<PrpLCompensateVo> otherList = compensateTaskService.findPrplCompensateByRegistNoAndClaimNo(prpLCompensateVo.getRegistNo(),otherClaimNo);
					if(otherList != null && otherList.size() > 0){
						//??????????????????????????????????????????1--??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
						//                  2--????????????????????????????????????????????????????????????????????????????????????, ?????????????????????????????????????????????????????????????????????????????????????????????????????????
						//                  3--????????????????????????????????????????????????????????????????????????????????????????????????????????????UNDERWRITEFLAG??????
						
						//?????????times
						int otherMaxtimes = 0;
						String underwriteFlag = "";
						for (PrpLCompensateVo prpLCompensateVo2 : otherList) {
							if(prpLCompensateVo2.getTimes() > otherMaxtimes){
								otherMaxtimes = prpLCompensateVo2.getTimes();
								underwriteFlag = prpLCompensateVo2.getUnderwriteFlag();
							}
						}
						if(curTimes > otherMaxtimes){
							try {
								interfaceAsyncService.sendRealTimeInfoQuery(wfTaskVo,userVo);
							} catch (Exception e) {
								logger.info("??????????????????????????????", e);
								e.printStackTrace();
								throw new IllegalArgumentException("??????????????????????????????" + e.getMessage());
							}
						}else if(curTimes == otherMaxtimes){
							if(StringUtils.isNotBlank(underwriteFlag)){
								// 0   ?????????????????????2 ????????????   7 ?????? 
								if("0".equals(underwriteFlag) || "2".equals(underwriteFlag)|| "7".equals(underwriteFlag)){
									try {
										interfaceAsyncService.sendRealTimeInfoQuery(wfTaskVo,userVo);
									} catch (Exception e) {
										logger.info("??????????????????????????????", e);
										e.printStackTrace();
										throw new IllegalArgumentException("??????????????????????????????" + e.getMessage());
									}
								}else{
									List<PrpLRealTimeQueryVo> findPrpLRealTimeQueryVo = realTimeQueryService.findPrpLRealTimeQueryVo(wfTaskVo.getRegistNo());
									if(findPrpLRealTimeQueryVo == null || findPrpLRealTimeQueryVo.size() == 0){
										try {
											interfaceAsyncService.sendRealTimeInfoQuery(wfTaskVo,userVo);
										} catch (Exception e) {
											logger.info("??????????????????????????????", e);
											e.printStackTrace();
											throw new IllegalArgumentException("??????????????????????????????" + e.getMessage());
										}
									}
								}
							}
						}else{
							List<PrpLRealTimeQueryVo> findPrpLRealTimeQueryVo = realTimeQueryService.findPrpLRealTimeQueryVo(wfTaskVo.getRegistNo());
							if(findPrpLRealTimeQueryVo == null || findPrpLRealTimeQueryVo.size() == 0){
								try {
									interfaceAsyncService.sendRealTimeInfoQuery(wfTaskVo,userVo);
								} catch (Exception e) {
									logger.info("??????????????????????????????", e);
									e.printStackTrace();
									throw new IllegalArgumentException("??????????????????????????????" + e.getMessage());
								}
							}
						}
					}else{
						//????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
						// ?????????????????????
						try {
							interfaceAsyncService.sendRealTimeInfoQuery(wfTaskVo,userVo);
						} catch (Exception e) {
							logger.info("??????????????????????????????", e);
							e.printStackTrace();
							throw new IllegalArgumentException("??????????????????????????????" + e.getMessage());
						}
					}
				}
			}
		}

		//==========
		mv.setViewName("redirect:/compensate/nextTaskView.do");
		mv.addObject("registNo", prpLCompensateVo.getRegistNo());
		mv.addObject("taskId", wfTaskVo.getTaskId());
		mv.addObject("compensateNo",prpLCompensateVo.getCompensateNo());
        
		return mv;
	}

	//????????????
	private void setAssignUser( WfTaskSubmitVo nextVo) {
		SysUserVo assUserVo = assignService.execute(nextVo.getNextNode(),WebUserUtils.getComCode(),WebUserUtils.getUser(), "");
		if (assUserVo == null) {
			throw new IllegalArgumentException(nextVo.getNextNode().getName() + "???????????????");
		}
		nextVo.setAssignUser(assUserVo.getUserCode());
		nextVo.setAssignCom(assUserVo.getComCode());
	}

	@RequestMapping(value = "/nextTaskView.do")
	public ModelAndView nextTaskView(String taskId, String registNo,String compensateNo)
			throws Exception {
		ModelAndView mv = new ModelAndView();
		String userCode = WebUserUtils.getUserCode();
		PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(Double.valueOf(taskId));
		// ???????????????????????????
		PrpLWfTaskVo wfTaskVoBIInfo = wfTaskHandleService.findWftaskInByRegistnoAndSubnode(registNo, "CompeBI");
		BigDecimal flowTaskId =  null;
		if(wfTaskVoBIInfo!=null){
			flowTaskId = wfTaskVoBIInfo.getTaskId();
		}
		if (!"3".equals(wfTaskVo.getHandlerStatus())
				&& userCode.equals(wfTaskVo.getAssignUser())
				&& !wfTaskVo.getSubNodeCode().startsWith("Compe")) {

			wfTaskVo.setQuickFlag("1");
		} else {
			wfTaskVo.setQuickFlag("0");
		}
		PrpLCheckCarVo p = checkTaskService.findCheckCarBySerialNo(registNo, 1);
		String itemName = p.getPrpLCheckCarInfo().getLicenseNo();
		mv.addObject("itemName", itemName);
		mv.setViewName("compensate/compSubmitSuccess");
		mv.addObject("registNo", registNo);
		mv.addObject("userCode", WebUserUtils.getUserCode());//
		mv.addObject("wfTaskVo", wfTaskVo);
		mv.addObject("flowTaskId", flowTaskId);
		mv.addObject("compensateNo", compensateNo);
		return mv;

	}

	/**
	 * ???????????????
	 * 
	 * @return
	 */
	@RequestMapping("/getAllRate.do")
	@ResponseBody
	public AjaxResult compensateGetAllRate(
			@FormModel("prpLCompensate") PrpLCompensateVo prpLCompensate,
			@FormModel("claimDeductVo") List<PrpLClaimDeductVo> claimDeductVoList,
			@FormModel("prpLLossItem") List<PrpLLossItemVo> prpLLossItemVoList,
			@FormModel("prpLLossProp") List<PrpLLossPropVo> prpLLossPropVoList,
			@FormModel("otherLoss") List<PrpLLossPropVo> otherLossList,
			@FormModel("prpLLossPerson") List<PrpLLossPersonVo> prpLLossPersonVoList,
			@FormModel("prpLCheckDutyVoList") List<PrpLCheckDutyVo> prpLCheckDutyVoList) {

		Long start = System.currentTimeMillis();
		AjaxResult ajaxResult = new AjaxResult();
		try {
			
			// ??????checkduty?????????
			checkTaskService.saveOrUpdateCheckDutyList(prpLCheckDutyVoList);
			checkTaskService.saveCheckDutyHis(prpLCompensate.getRegistNo(),FlowNode.Compe+prpLCompensate.getCompensateNo());
			
			prpLLossPropVoList.addAll(otherLossList);
			CompensateVo compensateVo = compHandleService.compensateGetAllRate(prpLCompensate,claimDeductVoList,
					prpLLossItemVoList,prpLLossPropVoList,prpLLossPersonVoList);
			
			Map<String, BigDecimal> addKindRateMap = new HashMap<String, BigDecimal>();// ???????????????
			Map<String, BigDecimal> dutyKindRateMap = new HashMap<String, BigDecimal>();// ???????????????
			Map<String, BigDecimal> absKindRateMap = new HashMap<String, BigDecimal>();// ???????????????

			Long end = System.currentTimeMillis();
			System.out.println((end - start) / 1000);

			for (PrpLLossItemVo itemVo : compensateVo.getPrpLLossItemVoList()) {
				if (addKindRateMap.get(itemVo.getKindCode()) == null) {
					addKindRateMap.put(itemVo.getKindCode(),itemVo.getDeductAddRate());
				}
				if (itemVo.getKindCode().equals(KINDCODE.KINDCODE_A)
						|| itemVo.getKindCode().equals(KINDCODE.KINDCODE_B)
						|| itemVo.getKindCode().equals(KINDCODE.KINDCODE_X)) {

					if (dutyKindRateMap.get(itemVo.getKindCode()) == null) {
						dutyKindRateMap.put(itemVo.getKindCode(),itemVo.getDeductDutyRate());
					}
					if (absKindRateMap.get(itemVo.getKindCode()) == null) {
						absKindRateMap.put(itemVo.getKindCode(),new BigDecimal("0"));
					}
				}else if (itemVo.getKindCode().equals(KINDCODE.KINDCODE_A1)){
					if (dutyKindRateMap.get(itemVo.getKindCode()) == null) {
						dutyKindRateMap.put(itemVo.getKindCode(),itemVo.getDeductDutyRate());
					}
					if (absKindRateMap.get(itemVo.getKindCode()) == null) {
						List<PrpLCItemKindVo> voList = policyViewService.findItemKindVos(prpLCompensate.getPolicyNo(), prpLCompensate.getRegistNo(), KINDCODE.KINDCODE_M3);
						if(voList!=null && voList.size()>0){
							absKindRateMap.put(itemVo.getKindCode(),voList.get(0).getValue());
						}else{
							absKindRateMap.put(itemVo.getKindCode(),new BigDecimal("0"));
						}
					}
				}else {
					if (dutyKindRateMap.get(itemVo.getKindCode()) == null) {
						dutyKindRateMap.put(itemVo.getKindCode(),new BigDecimal("0"));
					}
					if (absKindRateMap.get(itemVo.getKindCode()) == null) {
						absKindRateMap.put(itemVo.getKindCode(),itemVo.getDeductDutyRate());
					}
				}
			}

			for (PrpLLossPropVo itemVo : compensateVo.getPrpLLossPropVoList()) {
				if (addKindRateMap.get(itemVo.getKindCode()) == null) {
					addKindRateMap.put(itemVo.getKindCode(),itemVo.getDeductAddRate());
				}
				if (itemVo.getKindCode().equals(KINDCODE.KINDCODE_A)
						|| itemVo.getKindCode().equals(KINDCODE.KINDCODE_B)
						|| itemVo.getKindCode().equals(KINDCODE.KINDCODE_X)) {

					if (dutyKindRateMap.get(itemVo.getKindCode()) == null) {
						dutyKindRateMap.put(itemVo.getKindCode(),itemVo.getDeductDutyRate());
					}
					if (absKindRateMap.get(itemVo.getKindCode()) == null) {
						absKindRateMap.put(itemVo.getKindCode(),new BigDecimal("0"));
					}
				} else {
					if (dutyKindRateMap.get(itemVo.getKindCode()) == null) {
						dutyKindRateMap.put(itemVo.getKindCode(),new BigDecimal("0"));
					}
					if (absKindRateMap.get(itemVo.getKindCode()) == null) {
						absKindRateMap.put(itemVo.getKindCode(),itemVo.getDeductDutyRate());
					}
				}
			}

			for (PrpLLossPersonVo itemVo : compensateVo.getPrpLLossPersonVoList()) {
				if (addKindRateMap.get(itemVo.getKindCode()) == null) {
					addKindRateMap.put(itemVo.getKindCode(),itemVo.getDeductAddRate());
				}
				if (itemVo.getKindCode().equals(KINDCODE.KINDCODE_B)
						|| itemVo.getKindCode().equals(KINDCODE.KINDCODE_D11)
						|| itemVo.getKindCode().equals(KINDCODE.KINDCODE_D12)) {

					if (dutyKindRateMap.get(itemVo.getKindCode()) == null) {
						dutyKindRateMap.put(itemVo.getKindCode(),itemVo.getDeductDutyRate());
					}
					if (absKindRateMap.get(itemVo.getKindCode()) == null) {
						absKindRateMap.put(itemVo.getKindCode(),new BigDecimal("0"));
					}
				} else {
					if (dutyKindRateMap.get(itemVo.getKindCode()) == null) {
						dutyKindRateMap.put(itemVo.getKindCode(),new BigDecimal("0"));
					}
					if (absKindRateMap.get(itemVo.getKindCode()) == null) {
						absKindRateMap.put(itemVo.getKindCode(),itemVo.getDeductDutyRate());
					}
				}
			}

			// ??????????????????
			Map<String, Map> returnMap = new HashMap<String, Map>();
			returnMap.put("addKindRateMap", addKindRateMap);
			returnMap.put("dutyKindRateMap", dutyKindRateMap);
			returnMap.put("absKindRateMap", absKindRateMap);
			System.out.println(returnMap);
			ajaxResult.setData(returnMap);
			ajaxResult.setStatus(HttpStatus.SC_OK);
		} catch (Exception e) {
			e.printStackTrace();
			ajaxResult.setStatusText(e.getMessage());
		}
		return ajaxResult;
	}

	/**
	 * ??????????????????
	 * 
	 * @return
	 */
	@RequestMapping(value = "/acceptCompeTask.do")
	@ResponseBody
	public AjaxResult acceptCompeTask(Double flowTaskId) throws ParseException {
		AjaxResult ajaxResult = new AjaxResult();
		try {
			PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(flowTaskId);
			String compeNo = wfTaskVo.getHandlerIdKey();
			if (FlowNode.Compe.equals(wfTaskVo.getNodeCode())) {
				if (HandlerStatus.INIT.equals(wfTaskVo.getHandlerStatus())) {// ???????????????
					// // ????????????
					// wfTaskHandleService.acceptTask(flowTaskId,SecurityUtils.getUserCode(),SecurityUtils.getComCode());
					// ????????????????????????
					wfTaskHandleService.tempSaveTask(flowTaskId, compeNo,WebUserUtils.getUserCode(),WebUserUtils.getComCode());

				} else {// ????????????
					throw new IllegalArgumentException("?????????????????????????????????"
							+ wfTaskVo.getHandlerUser());
				}
			} else {
				throw new IllegalArgumentException("??????????????????");
			}
			ajaxResult.setStatus(HttpStatus.SC_OK);
			ajaxResult.setData(flowTaskId);
		} catch (Exception e) {
			e.printStackTrace();
			ajaxResult.setStatus(HttpStatus.SC_EXPECTATION_FAILED);
		}
		return ajaxResult;
	}

	/**
	 * ???????????????????????????????????????????????????ajax??????
	 * 
	 * @param infoSize
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/addRowInfo.ajax")
	@ResponseBody
	public ModelAndView addRowInfo(String bodyName, String flag, Integer size,
			String kindStr, String deductType, String handlerStatus,String dwFlag,String qfLicenseStr,String registNo)
			throws ParseException {
		ModelAndView mv = new ModelAndView();
		Map<String, String> kindMap = new HashMap<String, String>();
		PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(registNo);
		if (bodyName.equals("PayInfoTbody")) {// ?????????????????????????????????????????????
			List<PrpLChargeVo> prpLCharges = new ArrayList<PrpLChargeVo>();
			PrpLChargeVo prpLCharge = new PrpLChargeVo();
			prpLCharges.add(prpLCharge);
			// infoSize
			// kindCodeList ???????????????????????????List
			if (flag.equals(CompensateType.COMP_BI) && kindStr != null) {
				kindMap = handleKindMap(kindStr);
				mv.addObject("kindMap", kindMap);
			}
			mv.addObject("deductType", deductType);
			mv.addObject("prpLCharges", prpLCharges);
			mv.addObject("licenseNo",prpLRegistVo.getPrpLRegistExt().getLicenseNo()+"?????????");
			mv.setViewName("compensate/CompensateEdit_PayInfos_Pay");
		}
		if (bodyName.equals("PayCustomTbody")) {// ??????????????????????????????????????????
			List<PrpLPaymentVo> prpLPaymentVos = new ArrayList<PrpLPaymentVo>();
			PrpLPaymentVo prpLPaymentVo = new PrpLPaymentVo();
			prpLPaymentVos.add(prpLPaymentVo);
			Map<String,String> qfLicenseMap = new HashMap<String,String>();
			if(qfLicenseStr.length()>2){
				// ????????????????????????(??????{}?????????)??????Map????????????
				qfLicenseMap = handleKindMap(qfLicenseStr);
			}
			// ????????????List
			mv.addObject("prpLPaymentVos", prpLPaymentVos);
			mv.addObject("dwFlag",dwFlag);
			mv.addObject("qfLicenseMap", qfLicenseMap);
			mv.addObject("licenseNoSummary",prpLRegistVo.getPrpLRegistExt().getLicenseNo());
			mv.setViewName("compensate/CompensateEdit_PayInfos_Pers");
		}
		if (bodyName.equals("OtherLossTbody")) {// ?????????????????????????????????????????????
			List<PrpLLossPropVo> prpLLossPropVos = new ArrayList<PrpLLossPropVo>();
			PrpLLossPropVo prpLLossPropVo = new PrpLLossPropVo();
			prpLLossPropVos.add(prpLLossPropVo);
			if (flag.equals(CompensateType.COMP_BI) && kindStr != null) {
				kindMap = handleKindMap(kindStr);
				mv.addObject("kindMap", kindMap);
			}
			mv.addObject("prpLLossPropVos", prpLLossPropVos);
			mv.setViewName("compensate/CompensateEdit_LossTables_OtheLo");
		}
		mv.addObject("size", size);
		mv.addObject("flag", flag);
		mv.addObject("handlerStatus", handlerStatus);
		return mv;
	}

	@RequestMapping(value = "/initOtherProp.ajax")
	@ResponseBody
	public ModelAndView initOtherProp(String registNo, String kindCodes,
			String kindArray, boolean cprcCase) {
		ModelAndView mv = new ModelAndView();// {K1=????????????????????????????????????????????????,
												// K2=???????????????????????????????????????????????????}
		Map<String, String> kindMap = handleKindMap(kindArray);
		// ?????????????????????????????????
//		PrpLCItemKindVo itemKindVo = policyViewService.findItemKindByKindCode(
//				registNo, CodeConstants.KINDCODE.KINDCODE_NT);
//		if (itemKindVo != null) {
//			kindMap.put(itemKindVo.getKindCode(), itemKindVo.getKindName());
//		}

		if (!StringUtils.isBlank(kindCodes)) {
			String[] kinds = kindCodes.split(",");
			Set<String> haveSet = new HashSet<String>();
			for (String kindCode : kinds) {
				if (!cprcCase
						&& kindCode.equals(CodeConstants.KINDCODE.KINDCODE_X)) {
					continue;
				}
				haveSet.add(kindCode);
			}

			Iterator<Entry<String, String>> it = kindMap.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, String> entry = it.next();
				String key = entry.getKey();
				if (haveSet.contains(key)) {
					it.remove();
				}
			}
		}
	   
		Map<String, String> showMap = new HashMap<String, String>();
       if(kindMap!=null && kindMap.size()>0){//???????????????????????????????????????
    	   Iterator<Entry<String, String>> its = kindMap.entrySet().iterator();
    	   while(its.hasNext()){
    		   Map.Entry<String, String> entry = its.next();
    		   //?????????????????????????????????????????????????????????????????????,???????????????????????????????????????????????????????????????????????????????????????????????????????????????
    		   //??????????????????????????????????????????????????????????????????????????????????????????,?????????????????????????????????0??????????????????????????????
    		   if(!entry.getValue().contains("???????????????????????????") && !entry.getKey().contains(CodeConstants.KINDCODE.KINDCODE_BG) 
    				   && !entry.getKey().contains(CodeConstants.KINDCODE.KINDCODE_D12G) && !entry.getKey().contains(CodeConstants.KINDCODE.KINDCODE_AG)
    				   && !entry.getKey().contains(CodeConstants.KINDCODE.KINDCODE_D11G) && !entry.getKey().contains(CodeConstants.KINDCODE.KINDCODE_GG)
    				   && !entry.getKey().contains(CodeConstants.KINDCODE.KINDCODE_BP) && !entry.getKey().contains(CodeConstants.KINDCODE.KINDCODE_D11P) &&
    				   !entry.getKey().contains(CodeConstants.KINDCODE.KINDCODE_D12P) && !entry.getKey().contains(CodeConstants.KINDCODE.KINDCODE_DP)){
    			   showMap.put(entry.getKey(),entry.getValue());
    		   }
    	   }
        }
		mv.addObject("showMap", showMap);
		mv.setViewName("compensate/CompensateEdit_SubRiskDialog");

		return mv;
	}
	
	@RequestMapping(value = "/coinsInfoEdit.ajax")
	@ResponseBody
	public ModelAndView coinsInfoEdit(
			@FormModel("flowTaskId") String flowTaskId,
			@FormModel("prpLCompensate") PrpLCompensateVo prpLCompensate,
			@FormModel("prpLCharge") List<PrpLChargeVo> prpLChargeVoList){
		
		ModelAndView mv = new ModelAndView();
		if(StringUtils.isNotBlank(prpLCompensate.getCompensateNo())){
			mv.addObject("isCompensateNo", "1");
		}else{
			mv.addObject("isCompensateNo", "0");
		}
		//?????????????????????????????????????????????????????????
		if(StringUtils.isEmpty(flowTaskId)){
			PrpLWfTaskVo compeTaskVo = wfTaskHandleService.findEndTask(prpLCompensate.getRegistNo(), prpLCompensate.getCompensateNo(), FlowNode.Compe).get(0);

			List<PrpLCoinsVo> prpLCoinsList = compensateTaskService.findPrpLCoinsByCompensateNo(prpLCompensate.getCompensateNo());
			if(prpLCoinsList!=null && prpLCoinsList.size()>0){
				//????????????????????????????????????
				for(PrpLCoinsVo coinsVo:prpLCoinsList){
					coinsVo.setShareAmt(BigDecimal.ZERO.subtract(coinsVo.getShareAmt() !=null ? coinsVo.getShareAmt():BigDecimal.ZERO));
				}
				mv.addObject("prpLCoinsList", prpLCoinsList);
				mv.addObject("coinsFlag", prpLCoinsList.get(0).getCoinsFlag());
				mv.addObject("calculateType", prpLCoinsList.get(0).getCalculateType());
			}
			mv.addObject("handlerStatus", compeTaskVo.getHandlerStatus());
			mv.addObject("nodeCode", FlowNode.CompeWf.name());
		}else{//????????????
			PrpLWfTaskVo taskVo = wfTaskHandleService.queryTask(Double.valueOf(flowTaskId));
			PrpLCMainVo prpLCmainVo = policyViewService.getPolicyInfo(prpLCompensate.getRegistNo(), prpLCompensate.getPolicyNo());
			
			if(("0".equals(taskVo.getHandlerStatus()) || "2".equals(taskVo.getHandlerStatus())) && !FlowNode.VClaim.name().equals(taskVo.getNodeCode()) ){
				//???????????????????????????
				List<PrpLCoinsVo> ywCoinsList = compensateTaskService.findPrpLCoins(prpLCompensate.getPolicyNo());
				List<PrpLCoinsVo> prpLCoinsList = new ArrayList<PrpLCoinsVo>();
				if(ywCoinsList!=null && ywCoinsList.size()>0){
					for(PrpLCoinsVo ywCoinsVo:ywCoinsList){
						//??????
						PrpLCoinsVo payCoinsVo = new PrpLCoinsVo();
						Beans.copy().from(ywCoinsVo).to(payCoinsVo);
						BigDecimal hundred = new BigDecimal(100);
						//??????????????????
						BigDecimal shareAmt = BigDecimal.ZERO;
						if("1".equals(payCoinsVo.getCoinsType())){//??????
							shareAmt = prpLCompensate.getSumPaidAmt();
						}else{//??????
							//?????????????????????
							BigDecimal ourRate = hundred.subtract(payCoinsVo.getCoinsRate());
							shareAmt = prpLCompensate.getSumPaidAmt().multiply(payCoinsVo.getCoinsRate()).divide(ourRate, BigDecimal.ROUND_HALF_UP);
						}
						payCoinsVo.setShareAmt(shareAmt);
						payCoinsVo.setPayReason("P60");
						payCoinsVo.setCoinsFlag(prpLCmainVo.getCoinsFlag());
						payCoinsVo.setPolicyNo(prpLCmainVo.getPolicyNo());
						payCoinsVo.setCompensateNo(prpLCompensate.getCompensateNo());
						prpLCoinsList.add(payCoinsVo);
						
						//??????
						if(prpLChargeVoList!=null && prpLChargeVoList.size()>0){
							for(PrpLChargeVo chargeVo:prpLChargeVoList){
								PrpLCoinsVo feeCoinsVo = new PrpLCoinsVo();
								Beans.copy().from(ywCoinsVo).to(feeCoinsVo);
								//??????????????????
								BigDecimal shareFee = BigDecimal.ZERO;
								if("1".equals(feeCoinsVo.getCoinsType())){
									shareFee = chargeVo.getFeeAmt();
								}else{
									BigDecimal ourFeeRate = hundred.subtract(feeCoinsVo.getCoinsRate());
									shareFee = chargeVo.getFeeAmt().divide(ourFeeRate).multiply(feeCoinsVo.getCoinsRate()).setScale(2,BigDecimal.ROUND_HALF_UP);
								}
								feeCoinsVo.setShareAmt(shareFee);
								feeCoinsVo.setPayReason(CodeConstants.TransChargeToPayTypeMap.get(chargeVo.getChargeCode()));
								feeCoinsVo.setCoinsFlag(prpLCmainVo.getCoinsFlag());
								feeCoinsVo.setPolicyNo(prpLCmainVo.getPolicyNo());
								feeCoinsVo.setCompensateNo(prpLCompensate.getCompensateNo());
								prpLCoinsList.add(feeCoinsVo);
							}
						}
					}
				}
				if(prpLCoinsList!=null && prpLCoinsList.size()>0){
					mv.addObject("prpLCoinsList", prpLCoinsList);
					mv.addObject("coinsFlag", prpLCoinsList.get(0).getCoinsFlag());
					mv.addObject("calculateType", prpLCoinsList.get(0).getCalculateType());
				}
			}else{
				List<PrpLCoinsVo> prpLCoinsList = compensateTaskService.findPrpLCoinsByCompensateNo(taskVo.getHandlerIdKey());
				if(prpLCoinsList!=null && prpLCoinsList.size()>0){
					mv.addObject("prpLCoinsList", prpLCoinsList);
					mv.addObject("coinsFlag", prpLCoinsList.get(0).getCoinsFlag());
					mv.addObject("calculateType", prpLCoinsList.get(0).getCalculateType());
				}
			}
			mv.addObject("handlerStatus", taskVo.getHandlerStatus());
			mv.addObject("nodeCode", taskVo.getNodeCode());
		}
		
		mv.setViewName("compensate/CompensateEdit_Coins");
		
		return mv;
	}
	
	@RequestMapping(value = "/saveCoins.ajax")
	@ResponseBody
	public AjaxResult saveCoins(@FormModel("prpLCoins") List<PrpLCoinsVo> prpLCoinsVoList){
		AjaxResult ajaxResult = new AjaxResult();
		
		compensateTaskService.savePrpLCoins(prpLCoinsVoList);
		
		ajaxResult.setStatus(HttpStatus.SC_OK);
		return ajaxResult;
	}

	@RequestMapping(value = "/loadOtherPropTr.ajax")
	public ModelAndView loadOtherPropTr(int size, String registNo,
			String policyNo, String[] kindCodes, boolean cprcCase,String riskCode) {
		ModelAndView mv = new ModelAndView();
		List<PrpLLossPropVo> otherLossProps = new ArrayList<PrpLLossPropVo>();

		boolean xFlag = false;

		if (kindCodes != null) {
			for (String kindCode : kindCodes) {
				PrpLLossPropVo prpLLossPropVo = new PrpLLossPropVo();
				prpLLossPropVo.setKindCode(kindCode);
				prpLLossPropVo.setRiskCode(riskCode);
				if (CodeConstants.KINDCODE.KINDCODE_T.equals(kindCode)
						|| CodeConstants.KINDCODE.KINDCODE_C.equals(kindCode)
						|| CodeConstants.KINDCODE.KINDCODE_RF.equals(kindCode)) {

					PrpLCItemKindVo itemKindVo = policyViewService
							.findItemKindByKindCode(registNo, kindCode);// ??????????????????????????????????????????
					prpLLossPropVo.setUnitPrice(itemKindVo.getUnitAmount());
				}
				otherLossProps.add(prpLLossPropVo);

				if (CodeConstants.KINDCODE.KINDCODE_X.equals(kindCode)) {
					xFlag = true;
				}
			}
		}
		if (xFlag) {
			PrpLCMainVo cmainVo = policyViewService
					.getPrpLCMainByRegistNoAndPolicyNo(registNo, policyNo);
			List<PrpLcCarDeviceVo> carDeviceList = cmainVo.getPrpLcCarDevices();
			if (carDeviceList != null && !carDeviceList.isEmpty()) {
				Map<String, String> deviceMap = new HashMap<String, String>();
				for (PrpLcCarDeviceVo carDeviceVo : carDeviceList) {
					deviceMap.put(carDeviceVo.getDeviceId().toString(),
							carDeviceVo.getDeviceName());

					mv.addObject("deviceMap", deviceMap);
				}
			}
		}

		mv.addObject("otherLossProps", otherLossProps);
		mv.addObject("size", size);
		mv.addObject("cprcCase", cprcCase);
		mv.setViewName("compensate/CompensateEdit_LossTables_OtheLo");

		return mv;
	}

	/**
	 * ????????????????????????
	 * 
	 * @param registNo
	 * @return
	 */
	@RequestMapping(value = "/getLimitAgreeAmt.ajax")
	@ResponseBody
	public AjaxResult checkKindAmt(String registNo) {
		AjaxResult ajaxResult = new AjaxResult();
		List<PrpLCItemKindVo> cItemKindList = new ArrayList<PrpLCItemKindVo>();
		cItemKindList = registQueryService
				.findCItemKindListByRegistNo(registNo);
		BigDecimal limitAmt = BigDecimal.ZERO;
		for (PrpLCItemKindVo cItemKindVo : cItemKindList) {
			// TODO ???????????????????????????????????????????????????????????????????????? ?????????????????????
			limitAmt = limitAmt.add(cItemKindVo.getAmount());
		}
		ajaxResult.setData(limitAmt);
		ajaxResult.setStatus(HttpStatus.SC_OK);
		return ajaxResult;
	}

	/**
	 * ???????????????????????????
	 *
	 * @param registNo
	 * @return
	 */
	@RequestMapping(value = "/getBzLimitAgreeAmt.ajax")
	@ResponseBody
	public AjaxResult getBzLimitAgreeAmt(String registNo,Boolean isCiIndemDuty) {
		AjaxResult ajaxResult = new AjaxResult();

		String ciindemDuty;
		if (isCiIndemDuty) {
			ciindemDuty = CodeConstants.DutyType.CIINDEMDUTY_Y;//??????
		}else{
			ciindemDuty = CodeConstants.DutyType.CIINDEMDUTY_N;// ??????
		}

		BigDecimal limitAmt = BigDecimal.ZERO;
		List<PrpLDLimitVo> prpLDLimitVoList = claimService.findPrpLDLimitList(ciindemDuty,registNo);
		if (prpLDLimitVoList != null && prpLDLimitVoList.size() > 0){
			for (PrpLDLimitVo prpLDLimitVo : prpLDLimitVoList){
				limitAmt = limitAmt.add(prpLDLimitVo.getLimitFee());
			}
		}
		ajaxResult.setData(limitAmt);
		ajaxResult.setStatus(HttpStatus.SC_OK);
		return ajaxResult;
	}

	/**
	 * ????????????????????????????????????
	 * 
	 * @param registNo
	 * @return
	 */
	@RequestMapping(value = "/getHisSumLoss.ajax")
	@ResponseBody
	public AjaxResult getHisSumLoss(String registNo) {
		AjaxResult ajaxResult = new AjaxResult();
		// List<PrpLCompensateVo> compList =
		// compensatateService.findCompByRegistNo(registNo);
		// BigDecimal sumLoss = BigDecimal.ZERO;
		String index = "0";
		/*
		 * for(PrpLCompensateVo compVo:compList){
		 * if(compVo.getCompensateType().equals("N")
		 * &&compVo.getUnderwriteFlag()!=null
		 * &&compVo.getUnderwriteFlag().equals
		 * (CodeConstants.UnderWriteFlag.MANAL_UNDERWRITE)){
		 * sumLoss.add(compVo.getSumLoss()); } }
		 */
		/*
		 * List<PrpLPayCustomVo> payCusVoList =
		 * managerService.findPayCustomVoByRegistNo(registNo);
		 * for(PrpLPayCustomVo payCus:payCusVoList){
		 * if(payCus.getPayObjectKind().equals("2") &&
		 * payCus.getFlag().equals("1")){ //sumLoss = new BigDecimal( -1);//
		 * sumLoss=-1?????????????????????????????? index="1";//sumLoss=1????????????????????????????????? } }
		 */
		ajaxResult.setData(index);
		ajaxResult.setStatus(HttpStatus.SC_OK);
		return ajaxResult;
	}

	/**
	 * ?????????????????????????????????????????????????????????
	 * 
	 * @return
	 * @author lanlei
	 */
	@RequestMapping(value = "/checkKindAmt.ajax")
	@ResponseBody
	public AjaxResult checkKindAmt(
			@FormModel("prpLCompensate") PrpLCompensateVo prpLCompensate,
			@FormModel("prpLLossItem") List<PrpLLossItemVo> prpLLossItemVoList,
			@FormModel("prpLLossProp") List<PrpLLossPropVo> prpLLossPropVoList,
			@FormModel("prpLLossPerson") List<PrpLLossPersonVo> prpLLossPersonVoList,
			@FormModel("prpLCharge") List<PrpLChargeVo> prpLChargeVoList) {
		AjaxResult ar = new AjaxResult();
		// ?????????????????????????????????????????????Map
		Map<String, BigDecimal> kindAmtMap = compensateTaskService.getCompKindAmtMap(prpLLossItemVoList, prpLLossPropVoList,
						prpLLossPersonVoList, prpLChargeVoList);
		// ?????????????????????????????????????????????
		List<PrpLCItemKindVo> cItemKindList = new ArrayList<PrpLCItemKindVo>();
		cItemKindList = registQueryService.findCItemKindListByRegistNo(prpLCompensate.getRegistNo());
		// ???????????????????????????
		for (PrpLCItemKindVo cItemKind : cItemKindList) {
			if (kindAmtMap.containsKey(cItemKind.getKindCode())) {
				if (kindAmtMap.get(cItemKind.getKindCode()).compareTo(
						cItemKind.getAmount()) == 1) {
					String kindName = codeTranService.transCode("KindCode",
							cItemKind.getKindCode());
					// ar.setData(kindName+"?????????????????????????????????");
					// System.out.println(cItemKind.getKindCode()+cItemKind.getAmount()+"=???????????????=="+kindAmtMap.get(cItemKind.getKindCode()));
					// TODO ????????????????????????
					// return ar;
				}
			}
		}
		System.out.println(kindAmtMap);
		ar.setData("OK");
		return ar;
	}

	/**
	 * ???????????????????????????
	 * 
	 * @param kindStr
	 * @return
	 */
	private Map<String, String> handleKindMap(String kindStr) {
		Map<String, String> kindMap = new HashMap<String, String>();
		kindStr = kindStr.replace("{", "");
		kindStr = kindStr.replace("}", "");
		kindStr = kindStr.replace(" ", "");

		String[] kindArr = kindStr.split(",");
		if (kindArr.length > 0) {
			for (String temp : kindArr) {
				String[] tempArr = temp.split("=");
				kindMap.put(tempArr[0], tempArr[1]);
			}
		}

		return kindMap;
	}

	/**
	 * ????????????
	 */
	@RequestMapping("/calculateCI.do")
	@ResponseBody
	public AjaxResult calculateCI(
			@FormModel("prpLCompensate") PrpLCompensateVo prpLCompensate,
			@FormModel("prpLLossItem") List<PrpLLossItemVo> prpLLossItemVoList,
			@FormModel("prpLLossProp") List<PrpLLossPropVo> prpLLossPropVoList,
			@FormModel("prpLLossPerson") List<PrpLLossPersonVo> prpLLossPersonVoList,
			@FormModel("prpLCheckDutyVoList") List<PrpLCheckDutyVo> prpLCheckDutyVoList) {
		AjaxResult ajaxResult = new AjaxResult();
		try {
			// ??????checkduty?????????
			checkTaskService.saveOrUpdateCheckDutyList(prpLCheckDutyVoList);
			checkTaskService.saveCheckDutyHis(prpLCompensate.getRegistNo(),FlowNode.Compe+prpLCompensate.getCompensateNo());
			
			// List??????
			prpLLossItemVoList.removeAll(Collections.singleton(null));
			prpLLossPropVoList.removeAll(Collections.singleton(null));
			prpLLossPersonVoList.removeAll(Collections.singleton(null));
			// ????????????????????????
			int noDutyNum = 0;
			List<PrpLLossItemVo> noDutyLossItemVoList = new ArrayList<PrpLLossItemVo>();
			Iterator<PrpLLossItemVo> iterator = prpLLossItemVoList.iterator();
			while (iterator.hasNext()) {
				PrpLLossItemVo lossItemVo = iterator.next();
				if ("4".equals(lossItemVo.getPayFlag())) {
//					if ("1".equals(lossItemVo.getNoDutyPayFlag())) {
						noDutyNum++;// ?????????????????????1
//					}
					noDutyLossItemVoList.add(lossItemVo);
					iterator.remove();
				}
			}
			String flag = "1";
			if (!"1101".equals(prpLCompensate.getRiskCode())) {
				flag = "2";
			}
			// ??????????????????
			if (!"2".equals(prpLCompensate.getCaseType())) {
				PrpLRegistVo registVo = registQueryService.findByRegistNo(prpLCompensate.getRegistNo());

				List<PrpLDlossCarMainVo> lossCarMainVos = lossCarService.findLossCarMainByRegistNo(prpLCompensate.getRegistNo());

				if (lossCarMainVos != null && !lossCarMainVos.isEmpty()) {
					for (PrpLDlossCarMainVo carMainVo : lossCarMainVos) {
						if (carMainVo.getSerialNo() == 1
								&& compensateTaskService.checkLossState(carMainVo.getLossState(), CompensateType.PREFIX_CI)) {
							//?????????????????????????????????????????????????????????????????????????????????????????????
							PrpLLossItemVo lossItemVo = compHandleService.processLossCarVoList(carMainVo, null,"BZ", flag);
							prpLLossItemVoList.add(lossItemVo);
							
//							if (CodeConstants.ReportType.CI.equals(registVo.getReportType())) {
//								PrpLLossItemVo lossItemVo = compHandleService.processLossCarVoList(carMainVo, null, "BZ");
//								prpLLossItemVoList.add(lossItemVo);
//
//							} else {// ??????A????????????
//								if (CodeConstants.LossType.CAR_LOSS.equals(carMainVo.getLossFeeType())) {
//
//									PrpLLossItemVo lossItemVo = compHandleService.processLossCarVoList(carMainVo, null, "BZ");
//									prpLLossItemVoList.add(lossItemVo);
//								}
//							}
						}
						//??????????????????????????????
						BigDecimal sumLossFee =  DataUtils.NullToZero(carMainVo.getSumVeriLossFee())
								.add(DataUtils.NullToZero(carMainVo.getSumVeriRescueFee()));
						// ?????????????????? ????????????????????????
						if (carMainVo.getSerialNo() > 1
								&& BigDecimal.ZERO.compareTo(sumLossFee)==0
								&& compensateTaskService.checkLossState(carMainVo.getLossState(), CompensateType.PREFIX_CI)) {
							PrpLLossItemVo lossItemVo = compHandleService.processLossCarVoList(carMainVo, null,"BZ", flag);
							prpLLossItemVoList.add(lossItemVo);
						}
					}
				}

				// ????????????????????????
				List<PrpLdlossPropMainVo> lossPropMainVos = propTaskService.findPropMainListByRegistNo(prpLCompensate.getRegistNo());
				List<PrpLdlossPropMainVo> reLossPropMainVos = new ArrayList<PrpLdlossPropMainVo>();
				if (lossPropMainVos != null && !lossPropMainVos.isEmpty()) {
					for (PrpLdlossPropMainVo propMainVo : lossPropMainVos) {
						if (propMainVo.getSerialNo() == 1
								&& compensateTaskService.checkLossState(propMainVo.getLossState(), CompensateType.PREFIX_CI)) {
							reLossPropMainVos.add(propMainVo);
						}
					}
				}
				PrpLCMainVo prplcMainVo = policyViewService
						.getPrpLCMainByRegistNoAndPolicyNo(
								prpLCompensate.getRegistNo(),
								prpLCompensate.getPolicyNo());
				List<PrpLLossPropVo> reLossPropList = compHandleService.processLossPropVoList(
						reLossPropMainVos, prplcMainVo.getPrpCItemKinds(),
						CompensateType.COMP_CI);
				prpLLossPropVoList.addAll(reLossPropList);

				// ????????????????????????
				List<PrpLDlossPersTraceMainVo> reLossPersTraceMainVos = compensateTaskService.getValidLossPersTraceMain(
								prpLCompensate.getRegistNo(), CompensateType.COMP_CI);
				List<PrpLDlossPersTraceVo> lossPersTraceList = new ArrayList<PrpLDlossPersTraceVo>();
				if (reLossPersTraceMainVos != null) {
					// ?????????????????????????????????????????????PrpLDlossPersTrace
					for (PrpLDlossPersTraceMainVo lossPersTraceMain : reLossPersTraceMainVos) {
						if (!compensateTaskService.checkLossState(lossPersTraceMain.getLossState(), CompensateType.PREFIX_CI)) {
							continue;
						}
						List<PrpLDlossPersTraceVo> persTraceList = lossPersTraceMain
								.getPrpLDlossPersTraces();
						if (persTraceList != null && !persTraceList.isEmpty()) {
							for (PrpLDlossPersTraceVo lossPersTrace : lossPersTraceMain
									.getPrpLDlossPersTraces()) {
								if (lossPersTrace.getPrpLDlossPersInjured()
										.getSerialNo() == 1
										&& lossPersTrace.getValidFlag().equals(
												"1")) {
									lossPersTraceList.add(lossPersTrace);
								}
							}
						}
					}
				}
				List<PrpLLossPersonVo> reLossPersonList = compHandleService.processLossPersonVoList(
						lossPersTraceList, CompensateType.COMP_CI);
				prpLLossPersonVoList.addAll(reLossPersonList);

			}

			if (noDutyLossItemVoList != null && !noDutyLossItemVoList.isEmpty()) {
				for (PrpLLossItemVo noDutyItemVo : noDutyLossItemVoList) {
					for (PrpLLossItemVo lossItemVo : prpLLossItemVoList) {
						if (noDutyItemVo.getItemName().equals(lossItemVo.getItemName())) {
							lossItemVo.setNoDutyPayFlag(noDutyItemVo.getNoDutyPayFlag());
						}
					}
				}
			}

			CompensateListVo compensateListVo = compensateTaskService.calCulator(prpLCompensate, prpLLossItemVoList,
					prpLLossPropVoList,prpLLossPersonVoList,CodeConstants.CompensateKind.CI_COMPENSATE);
			// compensateList??????3???????????????(???????????????)?????????(???)???????????????(???)

			List<ThirdPartyLossInfo> thirdPartyLossInfolist = new ArrayList<ThirdPartyLossInfo>(
					0);
			// ?????????????????????????????????
			thirdPartyLossInfolist = compensateTaskService.getThirdPartyLossInfolistBz(
					compensateListVo, "compensate");

			Map<String, Object> resultMap = new HashMap<String, Object>();// ?????????Map?????????itemLossIdAndAmtMap???????????????
			// Map ?????????DlossId???PersonId(??????????????????)??????????????????SumRealPay??????
			Map<String, Double> itemLossIdAndAmtMap = new HashMap<String, Double>();
			String compText = "";

			//
			String preExpType = "";
			for (ThirdPartyLossInfo thirdPartyLossInfo : thirdPartyLossInfolist) {
				String key = "";
				if (CodeConstants.InsteadFlag.NODUTY_INSTEAD
						.equals(thirdPartyLossInfo.getInsteadFlag())) {
					key = "noduty=" + thirdPartyLossInfo.getSerialNo();
				} else {
					if ("car".equals(thirdPartyLossInfo.getExpType())) {
						key = "car=" + thirdPartyLossInfo.getSerialNo();// ?????????getLicenseNo????????????DlossId
					} else if ("prop".equals(thirdPartyLossInfo.getExpType())) {
						key = "prop=" + thirdPartyLossInfo.getSerialNo();
					} else if ("person".equals(thirdPartyLossInfo.getExpType())) {
						if ("?????????".equals(thirdPartyLossInfo.getLossFeeName())) {
							key = "med=" + thirdPartyLossInfo.getLossIndex();
						} else if ("????????????".equals(thirdPartyLossInfo
								.getLossFeeName())) {
							key = "pers=" + thirdPartyLossInfo.getLossIndex();
						}
					}

				}
				if (itemLossIdAndAmtMap.size() > 0 && itemLossIdAndAmtMap.containsKey(key)) {
					// ?????????????????????????????????
					Double temp = itemLossIdAndAmtMap.get(key) + thirdPartyLossInfo.getSumRealPay();
					itemLossIdAndAmtMap.put(key, temp);
				} else {
					itemLossIdAndAmtMap.put(key,thirdPartyLossInfo.getSumRealPay());
				}
				// logger.debug("=====000===="+thirdPartyLossInfo.getLicenseNo()+","+thirdPartyLossInfo.getLossName()+","+thirdPartyLossInfo.getSumRealPay());
				// logger.debug("=====010===="+thirdPartyLossInfo.getBzCompensateText()+"=====010end====");
				if (thirdPartyLossInfo.getBzCompensateText() != null) {

					compText += thirdPartyLossInfo.getBzCompensateText();
				}
			}
			// }

			// ???????????????????????????
			if (itemLossIdAndAmtMap.get("noduty=1") != null) {
				Double sumNodutyLoss = itemLossIdAndAmtMap.get("noduty=1");
				Double nodDutyLoss =0D;
				for (PrpLLossItemVo lossItemVo : noDutyLossItemVoList) {
					if ("1".equals(lossItemVo.getNoDutyPayFlag())) {
						itemLossIdAndAmtMap.put("noduty=" + lossItemVo.getItemId(),sumNodutyLoss / noDutyNum);
						nodDutyLoss = nodDutyLoss + sumNodutyLoss / noDutyNum;
					} else {
						itemLossIdAndAmtMap.put("noduty=" + lossItemVo.getItemId(), 0d);
					}
				}

				if (nodDutyLoss > 0) {
					compText += "\r\n\r\n????????? :?????????????????? = " + nodDutyLoss;
				}
			}
			logger.info("?????????=" + (prpLCompensate == null ? null : prpLCompensate.getRegistNo()) + "????????????");
			this.saveBzLeftAmount(compensateListVo.getLeftAmountMap(),
					prpLCompensate.getRegistNo(), CompensateType.COMP_CI);

			resultMap.put("itemLossIdAndAmtMap", itemLossIdAndAmtMap);
			if (StringUtils.isBlank(compText)) {
				compText = "???????????????????????????";
			}
			resultMap.put("compText", compText);
			logger.debug("===Map1===" + itemLossIdAndAmtMap.toString());

			ajaxResult.setData(resultMap);
			ajaxResult.setStatus(HttpStatus.SC_OK);
		} catch (Exception e) {
			logger.error("??????????????????,?????????registno="+ (prpLCompensate == null ? null :prpLCompensate.getRegistNo()) ,e);
			ajaxResult.setStatusText(e.getMessage());
		}

		return ajaxResult;
	}

	@RequestMapping("/calculateBI.do")
	@ResponseBody
	public AjaxResult calculateBI(
			@FormModel("prpLCompensate") PrpLCompensateVo prpLCompensate,
			@FormModel("claimDeductVo") List<PrpLClaimDeductVo> claimDeductVoList,
			@FormModel("prpLLossItem") List<PrpLLossItemVo> prpLLossItemVoList,
			@FormModel("prpLLossProp") List<PrpLLossPropVo> prpLLossPropVoList,
			@FormModel("otherLoss") List<PrpLLossPropVo> otherLossList,
			@FormModel("prpLLossPerson") List<PrpLLossPersonVo> prpLLossPersonVoList) {

		AjaxResult ajaxResult = new AjaxResult();
		try {
			prpLLossPropVoList.addAll(otherLossList);
			// List??????
			// prpLLossItemVoList.removeAll(Collections.singleton(null));
			// prpLLossPropVoList.removeAll(Collections.singleton(null));
			// prpLLossPersonVoList.removeAll(Collections.singleton(null));
			
			CompensateVo compensateResult = compHandleService.calculateBI(prpLCompensate, claimDeductVoList,
					prpLLossItemVoList,prpLLossPropVoList, otherLossList, prpLLossPersonVoList);
			
			Map<String, Double> itemLossIdAndAmtMap = new HashMap<String, Double>();
			Map<String, Double> deductOffMap = new HashMap<String, Double>();// ????????????
			String key = "";
			Map<String, Object> resultMap = new HashMap<String, Object>();// ?????????Map?????????itemLossIdAndAmtMap???????????????
			PrpLCompensateVo compensateVo = compensateResult.getPrpLCompensateVo();
			List<PrpLLossItemVo> prpLLossItemVos = compensateResult
					.getPrpLLossItemVoList();// car
			List<PrpLLossPropVo> PrpLLossPropVos = compensateResult
					.getPrpLLossPropVoList();// ???
			List<PrpLLossPersonVo> PrpLLossPersonVos = compensateResult
					.getPrpLLossPersonVoList();// ???

			for (PrpLLossItemVo prpLLossItemVo : prpLLossItemVos) {
				if (CodeConstants.KINDCODE.KINDCODE_A.equals(prpLLossItemVo
						.getKindCode())||CodeConstants.KINDCODE.KINDCODE_A1.equals(prpLLossItemVo
						.getKindCode())|| CodeConstants.KINDCODE.KINDCODE_B
						.equals(prpLLossItemVo.getKindCode())) {
					if (CodeConstants.PayFlagType.INSTEAD_PAY
							.equals(prpLLossItemVo.getPayFlag())) {// ??????
						key = "recovery=" + prpLLossItemVo.getItemId();
						// ????????????Map
						itemLossIdAndAmtMap.put(key, prpLLossItemVo.getSumRealPay().doubleValue());
						// ????????????Map
						deductOffMap.put(key, prpLLossItemVo.getDeductOffAmt().doubleValue());
					} else {
						key = "car=" + prpLLossItemVo.getItemId();

						if (itemLossIdAndAmtMap.containsKey(key)) {
							// ?????????????????????????????????
							// BigDecimalnew
							// BigDecimal(prpLLossItemVo.getSumRealPay());
							Double temp = itemLossIdAndAmtMap.get(key)
									+ prpLLossItemVo.getSumRealPay()
											.doubleValue();
							itemLossIdAndAmtMap.put(key, temp);
						} else {
							itemLossIdAndAmtMap.put(key, prpLLossItemVo
									.getSumRealPay().doubleValue());
						}

						if (deductOffMap.containsKey(key)) {
							Double temp = deductOffMap.get(key)
									+ prpLLossItemVo.getDeductOffAmt()
											.doubleValue();
							deductOffMap.put(key, temp);
						} else {
							deductOffMap.put(key, prpLLossItemVo
									.getDeductOffAmt().doubleValue());
						}
					}
				} else {
					key = "car=" + prpLLossItemVo.getKindCode();
					if (itemLossIdAndAmtMap.containsKey(key)) {
						// ?????????????????????????????????
						// BigDecimalnew
						// BigDecimal(prpLLossItemVo.getSumRealPay());
						Double temp = itemLossIdAndAmtMap.get(key)
								+ prpLLossItemVo.getSumRealPay().doubleValue();
						itemLossIdAndAmtMap.put(key, temp);
					} else {
						itemLossIdAndAmtMap.put(key, prpLLossItemVo
								.getSumRealPay().doubleValue());
					}

					if (deductOffMap.containsKey(key)) {
						Double temp = deductOffMap.get(key)
								+ prpLLossItemVo.getDeductOffAmt()
										.doubleValue();
						deductOffMap.put(key, temp);
					} else {
						deductOffMap.put(key, prpLLossItemVo.getDeductOffAmt()
								.doubleValue());
					}

				}
			}

			for (PrpLLossPropVo prpLLossPropVo : PrpLLossPropVos) {
				if ("1".equals(prpLLossPropVo.getPropType())) {
					key = "prop=" + prpLLossPropVo.getItemId();
				} else {
					key = "other=" + prpLLossPropVo.getKindCode();
				}

				if (itemLossIdAndAmtMap.containsKey(key)) {
					// ?????????????????????????????????
					Double temp = itemLossIdAndAmtMap.get(key)
							+ prpLLossPropVo.getSumRealPay().doubleValue();
					itemLossIdAndAmtMap.put(key, temp);
				} else {
					itemLossIdAndAmtMap.put(key, prpLLossPropVo.getSumRealPay()
							.doubleValue());
				}

				if (deductOffMap.containsKey(key)) {
					Double temp = itemLossIdAndAmtMap.get(key)
							+ prpLLossPropVo.getDeductOffAmt().doubleValue();
					deductOffMap.put(key, temp);
				} else {
					deductOffMap.put(key, prpLLossPropVo.getDeductOffAmt()
							.doubleValue());
				}
			}

			for (PrpLLossPersonVo prpLLossPersonVo : PrpLLossPersonVos) {
				key = "person";

				List<PrpLLossPersonFeeVo> prpLLossPersonFeeVos = prpLLossPersonVo
						.getPrpLLossPersonFees();
				for (PrpLLossPersonFeeVo prpLLossPersonFeeVo : prpLLossPersonFeeVos) {
					if (prpLLossPersonFeeVo.getLossItemNo().equals("02")) {
						key = "pers=" + prpLLossPersonVo.getPersonId();
					}
					if (prpLLossPersonFeeVo.getLossItemNo().equals("03")) {
						key = "med=" + prpLLossPersonVo.getPersonId();
					}
					if (itemLossIdAndAmtMap.containsKey(key)) {
						// ?????????????????????????????????
						Double temp = itemLossIdAndAmtMap.get(key)
								+ prpLLossPersonFeeVo.getFeeRealPay()
										.doubleValue();
						itemLossIdAndAmtMap.put(key, temp);
					} else {
						if (prpLLossPersonFeeVo.getFeeRealPay() != null) {// ????????????????????????
							itemLossIdAndAmtMap.put(key, prpLLossPersonFeeVo
									.getFeeRealPay().doubleValue());
						} else {
							itemLossIdAndAmtMap.put(key, (double) 0);
						}
					}

					if (deductOffMap.containsKey(key)) {
						Double temp = deductOffMap.get(key)
								+ prpLLossPersonFeeVo.getDeductOffAmt()
										.doubleValue();
						itemLossIdAndAmtMap.put(key, temp);
					} else {
						if (prpLLossPersonFeeVo.getFeeRealPay() != null) {// ????????????????????????
							deductOffMap.put(key, prpLLossPersonFeeVo
									.getDeductOffAmt().doubleValue());
						} else {
							deductOffMap.put(key, (double) 0);
						}
					}
				}

			}
			// ?????????????????????
			String compText = compensateVo.getLcText();
			if (StringUtils.isBlank(compText)) {
				compText = "???????????????????????????";
			}

			logger.debug(compensateVo.getLcText());
			logger.debug("itemLossIdAndAmtMap" + itemLossIdAndAmtMap.toString());
			logger.debug("deductOffMap======" + deductOffMap.toString());

			resultMap.put("compText", compText);
			resultMap.put("itemLossIdAndAmtMap", itemLossIdAndAmtMap);
			resultMap.put("deductOffMap", deductOffMap);
			ajaxResult.setStatus(HttpStatus.SC_OK);
			ajaxResult.setData(resultMap);
		} catch (Exception e) {
			e.printStackTrace();
			ajaxResult.setStatusText(e.getMessage());
		}

		return ajaxResult;
	}

	@RequestMapping("/bIGetBZAmt.do")
	@ResponseBody
	public AjaxResult bIGetBZAmt(
			@FormModel("prpLCompensate") PrpLCompensateVo prpLCompensate,
			@FormModel("prpLLossItem") List<PrpLLossItemVo> prpLLossItemVoList,
			@FormModel("prpLLossProp") List<PrpLLossPropVo> prpLLossPropVoList,
			@FormModel("otherLoss") List<PrpLLossPropVo> otherLossList,
			@FormModel("prpLLossPerson") List<PrpLLossPersonVo> prpLLossPersonVoList) {
		AjaxResult ajaxResult = new AjaxResult();
		try {
			prpLLossPropVoList.addAll(otherLossList);
			boolean existSelf = false;//??????????????????????????????????????????
			// ???????????? A???B?????????????????????
			Iterator<PrpLLossItemVo> iterator = prpLLossItemVoList.iterator();
			while (iterator.hasNext()) {
				PrpLLossItemVo lossItemVo = iterator.next();
				if (!(CodeConstants.KINDCODE.KINDCODE_A.equals(lossItemVo
						.getKindCode()) || CodeConstants.KINDCODE.KINDCODE_B
						.equals(lossItemVo.getKindCode()) || CodeConstants.KINDCODE.KINDCODE_A1
						.equals(lossItemVo.getKindCode()))) {
					iterator.remove();
				}
				if("1".equals(lossItemVo.getItemId())){
					existSelf = true;
				}
			}
			
			List<PrpLDlossCarMainVo> lossCarMainVos = lossCarService.findLossCarMainByRegistNo(prpLCompensate.getRegistNo());
			PrpLCMainVo cmainVo = policyViewService.getPolicyInfo(prpLCompensate.getRegistNo(),prpLCompensate.getPolicyNo());
			List<String> itemKindMainCodeList = new ArrayList<String>();
			if(cmainVo != null && cmainVo.getPrpCItemKinds() != null && cmainVo.getPrpCItemKinds().size() > 0 ){
				for(PrpLCItemKindVo kindVo : cmainVo.getPrpCItemKinds()){
					if("Y".equals(kindVo.getCalculateFlag())){
						itemKindMainCodeList.add(kindVo.getKindCode());
					}
				}
			}
			
			if (lossCarMainVos != null && !lossCarMainVos.isEmpty()) {
				for (PrpLDlossCarMainVo carMainVo : lossCarMainVos) {
					if (carMainVo.getSerialNo() == 1 && !existSelf
							&& compensateTaskService.checkLossState(carMainVo.getLossState(), CompensateType.PREFIX_BI)) {
						//????????????????????????????????????????????????????????????
						String kindCodeSelf = "";
						if(itemKindMainCodeList.contains("A")){
							kindCodeSelf = KINDCODE.KINDCODE_A;
						}else{
							kindCodeSelf = itemKindMainCodeList.get(0);
						}
						PrpLLossItemVo lossItemVo = compHandleService.processLossCarVoList(carMainVo, null,kindCodeSelf, "2");
						prpLLossItemVoList.add(lossItemVo);
					}
					//??????????????????????????????
					BigDecimal sumLossFee =  DataUtils.NullToZero(carMainVo.getSumVeriLossFee())
							.add(DataUtils.NullToZero(carMainVo.getSumVeriRescueFee()));
					// ?????????????????? ????????????????????????
					if (carMainVo.getSerialNo() > 1
							&& BigDecimal.ZERO.compareTo(sumLossFee)==0
							&& compensateTaskService.checkLossState(carMainVo.getLossState(), CompensateType.PREFIX_BI)) {
						String kindCodeThird = "";
						if(itemKindMainCodeList.contains("B")){
							kindCodeThird = KINDCODE.KINDCODE_B;
						}else{
							kindCodeThird = itemKindMainCodeList.get(0);
						}
						PrpLLossItemVo lossItemVo = compHandleService.processLossCarVoList(carMainVo, null,kindCodeThird, "2");
						prpLLossItemVoList.add(lossItemVo);
					}
				}
			}

			CompensateListVo compensateListVo = compensateTaskService.calCulator(
					prpLCompensate, prpLLossItemVoList, prpLLossPropVoList,
					prpLLossPersonVoList,
					CodeConstants.CompensateKind.BI_COMPENSATE);
			List<CompensateExp> returnCompensateExpList = compensateListVo.getCompensateList();

			List<ThirdPartyLossInfo> thirdPartyLossInfolist = new ArrayList<ThirdPartyLossInfo>(
					0);
			// ?????????????????????????????????
			thirdPartyLossInfolist = compensateTaskService.getThirdPartyLossInfolistBz(
					compensateListVo, "compensate");

			Map<String, Object> resultMap = new HashMap<String, Object>();// ?????????Map?????????itemLossIdAndAmtMap???????????????
			// Map ?????????DlossId???PersonId(??????????????????)??????????????????SumRealPay??????
			// Map<String,Double> itemLossIdAndAmtMap = new HashMap<String,
			// Double>();
			Map<String, Double> itemLossIdMap = new HashMap<String, Double>();// ???????????????
			Map<String, Double> itemAmtMap = new HashMap<String, Double>();// ??????????????????
			Map<String, Double> NoDutyCarPay = new HashMap<String, Double>();// ??????????????????
			String compText = "";

			for (ThirdPartyLossInfo thirdPartyLossInfo : thirdPartyLossInfolist) {
				String key = "";
				if ("car".equals(thirdPartyLossInfo.getExpType())) {
					key = "car=" + thirdPartyLossInfo.getLicenseNo();// ?????????getLicenseNo????????????DlossId
				} else if ("prop".equals(thirdPartyLossInfo.getExpType())) {
					key = "prop=" + thirdPartyLossInfo.getLicenseNo();
				} else if ("person".equals(thirdPartyLossInfo.getExpType())) {
					if ("?????????".equals(thirdPartyLossInfo.getLossFeeName())) {
						key = "med=" + thirdPartyLossInfo.getLossIndex();
					} else if ("????????????".equals(thirdPartyLossInfo
							.getLossFeeName())) {
						key = "pers=" + thirdPartyLossInfo.getLossIndex();
					}
				}

				if (CodeConstants.InsteadFlag.NODUTY_INSTEAD
						.equals(thirdPartyLossInfo.getInsteadFlag())) {
					if (thirdPartyLossInfo.getSerialNo() == 1) {
						String dutyKey = "noduty="
								+ thirdPartyLossInfo.getSerialNo();
						if (NoDutyCarPay.containsKey(dutyKey)) {
							Double temp1 = NoDutyCarPay.get(dutyKey)
									+ thirdPartyLossInfo.getNoDutyCarPay();
							NoDutyCarPay.put(dutyKey, temp1);
						} else {
							NoDutyCarPay.put(dutyKey,
									thirdPartyLossInfo.getNoDutyCarPay());
						}
					} else {
						String dutyKey = "car="
								+ thirdPartyLossInfo.getLicenseNo();
						if (itemLossIdMap.containsKey(dutyKey)) {
							Double temp1 = NoDutyCarPay.get(dutyKey)
									+ thirdPartyLossInfo.getNoDutyCarPay();
							itemLossIdMap.put(dutyKey, temp1);
						} else {
							itemLossIdMap.put(dutyKey,
									thirdPartyLossInfo.getNoDutyCarPay());
						}
					}

				}
				if ("car".equals(thirdPartyLossInfo.getExpType())
						|| "prop".equals(thirdPartyLossInfo.getExpType())) {
					double sumLoss = thirdPartyLossInfo.getSumLoss();
					double rescueFee = thirdPartyLossInfo.getRescueFee();
					double lossRate = (sumLoss - rescueFee) / sumLoss;
					if(sumLoss==0.0){
						lossRate=0.0;
					}
					double sumRealPay = thirdPartyLossInfo.getSumRealPay();
					if (("car".equals(thirdPartyLossInfo.getExpType()) || "prop".equals(thirdPartyLossInfo.getExpType()))
							&& thirdPartyLossInfo.getNoDutyCarPay() > 0) {
						sumRealPay = sumRealPay
								- thirdPartyLossInfo.getNoDutyCarPay();
						if (sumRealPay < 0){
							sumRealPay = 0.0;
						}
					}

					double realPay = sumRealPay * lossRate;
					double rescueFeePay = sumRealPay - realPay;
					// ???????????????
					if (itemLossIdMap.containsKey(key)) {
						Double temp1 = itemLossIdMap.get(key) + realPay;
						itemLossIdMap.put(key, temp1);
					} else {
						itemLossIdMap.put(key, realPay);
					}
					// ??????????????????
					if (itemAmtMap.containsKey(key)) {
						Double temp1 = itemAmtMap.get(key) + rescueFeePay;
						itemAmtMap.put(key, temp1);
					} else {
						itemAmtMap.put(key, rescueFeePay);
					}

				} else {// ??????
						// ???????????????
					if (itemLossIdMap.containsKey(key)) {
						Double temp1 = itemLossIdMap.get(key)
								+ thirdPartyLossInfo.getSumRealPay();
						itemLossIdMap.put(key, temp1);
					} else {
						itemLossIdMap.put(key,
								thirdPartyLossInfo.getSumRealPay());
					}
				}

				logger.debug("=====000====" + thirdPartyLossInfo.getLicenseNo()
						+ "," + thirdPartyLossInfo.getLossName() + ","
						+ thirdPartyLossInfo.getSumRealPay());
				logger.debug("=====010===="
						+ thirdPartyLossInfo.getBzCompensateText()
						+ "=====010end====");

				compText += thirdPartyLossInfo.getBzCompensateText();

			}

			logger.info("?????????=" + (prpLCompensate == null ? null : prpLCompensate.getRegistNo()) + "????????????????????????");
			this.saveBzLeftAmount(compensateListVo.getLeftAmountMap(),
					prpLCompensate.getRegistNo(), CompensateType.COMP_BI);

			resultMap.put("itemLossIdMap", itemLossIdMap);
			resultMap.put("itemAmtMap", itemAmtMap);
			resultMap.put("NoDutyCarPay", NoDutyCarPay);
			resultMap.put("compText", compText);
			// logger.debug("===Map1==="+itemLossIdAndAmtMap.toString());
			logger.debug("===itemLossIdMap===" + itemLossIdMap.toString());
			logger.debug("===itemAmtMap===" + itemAmtMap.toString());
			logger.debug("===NoDutyCarPay===" + NoDutyCarPay.toString());
			ajaxResult.setData(resultMap);
			ajaxResult.setStatus(HttpStatus.SC_OK);
		} catch (Exception e) {
			e.printStackTrace();
			ajaxResult.setStatusText(e.getMessage());
		}

		return ajaxResult;

	}

	/**
	 * ??????????????????????????????
	 * 
	 * @param leftAmountMap
	 * @param registNo
	 * @param calType
	 * @modified: ???YangKun(2016???6???29??? ??????3:41:10): <br>
	 */
	private void saveBzLeftAmount(Map<String, Double> leftAmountMap,
			String registNo, String calType) {
		logger.info("?????????" + registNo + "??????????????? "+ calType + "??????????????????????????????????????????(????????????)" );
		List<PrpLCheckDutyVo> checkDutyList = checkTaskService.findCheckDutyByRegistNo(registNo);
		checkDutyList = compensateTaskService.calcCheckDutyList(leftAmountMap,checkDutyList,calType);
		checkTaskService.saveCheckDutyList(checkDutyList);
		logger.info("?????????" + registNo + "??????????????? "+ calType + "??????????????????????????????????????????(????????????)" );
	}

	// ????????????????????????
	@RequestMapping("/showKindAmtSum.ajax")
	public ModelAndView showKindAmtSum(
			@FormModel("prpLCharge") List<PrpLChargeVo> prpLChargeVoList,
			@FormModel("prpLLossItem") List<PrpLLossItemVo> prpLLossItemVoList,
			@FormModel("prpLLossProp") List<PrpLLossPropVo> prpLLossPropVoList,
			@FormModel("prpLLossPerson") List<PrpLLossPersonVo> prpLLossPersonVoList) {
		// AjaxResult ajaxResult = new AjaxResult();
		ModelAndView modelAndView = new ModelAndView();
		List<PrpLKindAmtSummaryVo> kindAmtSumList = new ArrayList<PrpLKindAmtSummaryVo>();
		PrpLKindAmtSummaryVo kAmtCar = new PrpLKindAmtSummaryVo();
		PrpLKindAmtSummaryVo kAmtBi1 = new PrpLKindAmtSummaryVo();
		PrpLKindAmtSummaryVo kAmtBi2 = new PrpLKindAmtSummaryVo();
		PrpLKindAmtSummaryVo kAmtBi3 = new PrpLKindAmtSummaryVo();
		PrpLKindAmtSummaryVo kAmtPersDuty = new PrpLKindAmtSummaryVo();
		// ??????
		BigDecimal carSumLoss = new BigDecimal(0);
		BigDecimal carSumRealPay = new BigDecimal(0);
		BigDecimal biSumLoss = new BigDecimal(0);
		BigDecimal biSumRealPay = new BigDecimal(0);
		BigDecimal biSumLoss2 = new BigDecimal(0);
		BigDecimal biSumRealPay2 = new BigDecimal(0);
		BigDecimal biSumLoss3 = new BigDecimal(0);
		BigDecimal biSumRealPay3 = new BigDecimal(0);
		BigDecimal persDutySumLoss = new BigDecimal(0);
		BigDecimal persDutySumRealPay = new BigDecimal(0);
		/*
		 * kAmtCar.setLossKind(CodeConstants.ExpType.CAR);
		 * kAmtProp.setLossKind(CodeConstants.ExpType.PROP);
		 * kAmtPers.setLossKind(CodeConstants.ExpType.PERSON);
		 * kAmtCar.setKindCode(CodeConstants.KINDCODE.KINDCODE_B);
		 * kAmtProp.setKindCode(CodeConstants.KINDCODE.KINDCODE_B);
		 * kAmtPers.setKindCode(CodeConstants.KINDCODE.KINDCODE_B);
		 */
		String riskCode = "";
		for (PrpLLossItemVo item : prpLLossItemVoList) {
			riskCode = item.getRiskCode();
			if (CodeConstants.KINDCODE.KINDCODE_A.equals(item.getKindCode())) {
				kAmtCar.setKindCode(CodeConstants.KINDCODE.KINDCODE_A);
				// kAmtCar.setLossKind("1");
				carSumLoss = carSumLoss.add(item.getSumLoss());
				carSumRealPay = carSumRealPay.add(item.getSumRealPay());
			} else if (CodeConstants.KINDCODE.KINDCODE_B.equals(item
					.getKindCode())) {
				kAmtBi1.setKindCode(CodeConstants.KINDCODE.KINDCODE_B);
				kAmtBi1.setLossKind("1");
				biSumLoss = biSumLoss.add(item.getSumLoss());
				biSumRealPay = biSumRealPay.add(item.getSumRealPay());
			} else if (CodeConstants.KINDCODE.KINDCODE_D12.equals(item
					.getKindCode())) {
				kAmtPersDuty.setKindCode(CodeConstants.KINDCODE.KINDCODE_D12);
				// kAmtPersDuty.setLossKind("3");
				persDutySumLoss = persDutySumLoss.add(item.getSumLoss());
				persDutySumRealPay = persDutySumRealPay.add(item
						.getSumRealPay());
			}
		}
		kAmtBi1.setSumLoss(biSumLoss);
		kAmtBi1.setSumRealPay(biSumRealPay);
		kindAmtSumList.add(kAmtBi1);
		for (PrpLLossPropVo prop : prpLLossPropVoList) {
			if (CodeConstants.KINDCODE.KINDCODE_A.equals(prop.getKindCode())) {
				kAmtCar.setKindCode(CodeConstants.KINDCODE.KINDCODE_A);
				// kAmtCar.setLossKind("1");
				carSumLoss = carSumLoss.add(prop.getSumLoss());
				carSumRealPay = carSumRealPay.add(prop.getSumRealPay());
			} else if (CodeConstants.KINDCODE.KINDCODE_B.equals(prop
					.getKindCode())) {
				kAmtBi2.setKindCode(CodeConstants.KINDCODE.KINDCODE_B);
				kAmtBi2.setLossKind("2");
				biSumLoss2 = biSumLoss2.add(prop.getSumLoss());
				biSumRealPay2 = biSumRealPay2.add(prop.getSumRealPay());
			} else if (CodeConstants.KINDCODE.KINDCODE_D12.equals(prop
					.getKindCode())) {
				kAmtPersDuty.setKindCode(CodeConstants.KINDCODE.KINDCODE_D12);
				// kAmtPersDuty.setLossKind("3");
				persDutySumLoss = persDutySumLoss.add(prop.getSumLoss());
				persDutySumRealPay = persDutySumRealPay.add(prop
						.getSumRealPay());
			}
		}
		kAmtBi2.setSumLoss(biSumLoss2);
		kAmtBi2.setSumRealPay(biSumRealPay2);
		kindAmtSumList.add(kAmtBi2);
		for (PrpLLossPersonVo personVo : prpLLossPersonVoList) {
			if (CodeConstants.KINDCODE.KINDCODE_A
					.equals(personVo.getKindCode())) {
				kAmtCar.setKindCode(CodeConstants.KINDCODE.KINDCODE_A);
				// kAmtCar.setLossKind("1");
				carSumLoss = carSumLoss.add(personVo.getSumLoss());
				carSumRealPay = carSumRealPay.add(personVo.getSumRealPay());
			} else if (CodeConstants.KINDCODE.KINDCODE_B.equals(personVo
					.getKindCode())) {
				kAmtBi3.setKindCode(CodeConstants.KINDCODE.KINDCODE_B);
				kAmtBi3.setLossKind("3");
				biSumLoss3 = biSumLoss3.add(personVo.getSumLoss());
				biSumRealPay3 = biSumRealPay3.add(personVo.getSumRealPay());
			} else if (CodeConstants.KINDCODE.KINDCODE_D12.equals(personVo
					.getKindCode())) {
				kAmtPersDuty.setKindCode(CodeConstants.KINDCODE.KINDCODE_D12);
				// kAmtPersDuty.setLossKind("3");
				persDutySumLoss = persDutySumLoss.add(personVo.getSumLoss());
				persDutySumRealPay = persDutySumRealPay.add(personVo
						.getSumRealPay());
			}
		}
		// ????????????

		kAmtCar.setSumLoss(carSumLoss);
		kAmtCar.setSumRealPay(carSumRealPay);
		kAmtBi3.setSumLoss(biSumLoss3);
		kAmtBi3.setSumRealPay(biSumRealPay3);
		kAmtPersDuty.setSumLoss(persDutySumLoss);
		kAmtPersDuty.setSumRealPay(persDutySumRealPay);
		/*
		 * for(PrpLChargeVo chargeVo:prpLChargeVoList){
		 * 
		 * }
		 */
		/*
		 * ajaxResult.setData(kAmtCar); ajaxResult.setData(kAmtBi);
		 * ajaxResult.setData(kAmtPersDuty);
		 */
		kindAmtSumList.add(kAmtCar);
		kindAmtSumList.add(kAmtBi3);
		kindAmtSumList.add(kAmtPersDuty);
		// ajaxResult.setStatus(HttpStatus.SC_OK);
		modelAndView.addObject("kAmtCar", kAmtCar);
		modelAndView.addObject("prpLKindAmtSummaryList", kindAmtSumList);

		modelAndView.addObject("kAmtBi1", kAmtBi1);
		modelAndView.addObject("kAmtBi2", kAmtBi2);
		modelAndView.addObject("kAmtBi3", kAmtBi3);
		int k = 0;
		BigDecimal SumRealPayAll = new BigDecimal(0);
		for (PrpLKindAmtSummaryVo kindAmtSummaryVo : kindAmtSumList) {
			if (CodeConstants.KINDCODE.KINDCODE_B.equals(kindAmtSummaryVo
					.getKindCode())) {
				k = k + 1;
				SumRealPayAll = SumRealPayAll.add(kindAmtSummaryVo
						.getSumRealPay());
			}
		}
		int i = SumRealPayAll.compareTo(BigDecimal.ZERO);
		if (i == 0) {
			SumRealPayAll = new BigDecimal(0);
		}
		System.out.println("kkk==+" + SumRealPayAll);
		modelAndView.addObject("kAmtPersDuty", kAmtPersDuty);
		modelAndView.addObject("SumRealPayAll", SumRealPayAll);
		modelAndView.addObject("k", k);
		modelAndView.addObject("riskCode", riskCode);
		System.out.println("prpLKindAmtSummaryList=="
				+ kindAmtSumList.toString());
		modelAndView.setViewName("compensate/CompensateEdit_KindAmt");
		return modelAndView;

	}

	/**
	 * ?????????????????????
	 * 
	 * @param infoSize
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/addSelect.ajax")
	@ResponseBody
	public ModelAndView addSelect(String policyNo, String registNo, Integer size)
			throws ParseException {
		ModelAndView mv = new ModelAndView();
		/*
		 * List<PrpLcCarDeviceVo>
		 * prpLcCarDeviceVoList=registQueryService.findPrplccardeviceByRiskCode
		 * (riskCode);
		 */
		PrpLCMainVo cMainVo = policyViewService
				.getPrpLCMainByRegistNoAndPolicyNo(registNo, policyNo);
		if (cMainVo != null) {
			mv.addObject("prpLcCarDeviceVoList", cMainVo.getPrpLcCarDevices());
		}
		mv.addObject("size", size);
		mv.setViewName("compensate/CompensateEdit_LossTables_OtheLoCar");
		return mv;
	}

	/**
	 * ?????????????????????
	 * 
	 * @param infoSize
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/addText.ajax")
	@ResponseBody
	public ModelAndView addText(String policyNo, String registNo, Integer size)
			throws ParseException {
		ModelAndView mv = new ModelAndView();
		/*
		 * List<PrpLcCarDeviceVo>
		 * prpLcCarDeviceVoList=registQueryService.findPrplccardeviceByRiskCode
		 * (riskCode);
		 */
		PrpLCMainVo cMainVo = policyViewService
				.getPrpLCMainByRegistNoAndPolicyNo(registNo, policyNo);
		mv.addObject("prpLcCarDeviceVoList", cMainVo.getPrpLcCarDevices());
		mv.addObject("size", size);
		mv.setViewName("compensate/CompensateEdit_LossTables_OtheLoText");
		return mv;
	}

	/**
	 * ?????????????????????????????????????????????
	 * 
	 * @param list
	 * @return
	 *//*
	@RequestMapping(value = "/testData.ajax")
	@ResponseBody
	public AjaxResult testData(String claimNo, String registNo) {
		AjaxResult ajaxResult = new AjaxResult();
		double sum = 0.00;
		
		List<PrpLCompensateVo> compVos2 = new ArrayList<PrpLCompensateVo>();
		List<PrpLCompensateVo> compVos1 = compensateTaskService.findCompensatevosByclaimNo(claimNo);
		if (compVos1 != null && compVos1.size() > 0) {
			for (PrpLCompensateVo prpLCompensateVo : compVos1) {
				if (!(prpLCompensateVo.getUnderwriteFlag().equals("7"))) {
					compVos2.add(prpLCompensateVo);
				
				}
			}
		}
		
		if (payIdlist != null && payIdlist.size() > 0) {
			//?????????????????????
			Set<Long> payIdSet= new HashSet<Long>(payIdlist);
			
			for (Long payId : payIdSet) {
				PrpLPayCustomVo payCustomVo = managerService.findPayCustomVoById(payId);
				//if (!( payCustomVo.getFlag().equals("1")) && !"7".equals(payCustomVo.getPayObjectKind())) { //????????????????????????????????????????????????
		    if (!(payCustomVo.getFlag().equals("1")) && (!"7".equals(payCustomVo.getPayObjectKind())
                        && !"4".equals(payCustomVo.getPayObjectKind()) && !"8".equals(payCustomVo.getPayObjectKind()))) { //4????????????,7?????????????????????????????????
	                
					if (compVos2 != null && compVos2.size() > 0) {
						for (PrpLCompensateVo prpLCompensateVo : compVos2) {
							for (PrpLPaymentVo prpLPaymentVo : prpLCompensateVo.getPrpLPayments()) {

								if (prpLPaymentVo.getPayeeId().longValue() == payId.longValue()) {

									sum = sum+(prpLPaymentVo.getSumRealPay().doubleValue());
                               
								}

							}
						}

					}

					// ??????????????????10000???
					if (sum >= 10000) {

						keylist.add(payCustomVo.getPayeeName());

					}
//					System.out.print("yyyyyy" + keylist.get(0));
					sum = 0.00;
				}

			}

		}

		ajaxResult.setData(keylist);
		ajaxResult.setStatus(HttpStatus.SC_OK);
		return ajaxResult;
	}*/

	/**
	 * ?????????????????????????????????????????????
	 * 
	 * @param list
	 * @return
	 */
	@RequestMapping(value = "/payInfor.ajax")
	@ResponseBody
	public AjaxResult payInfor(String claimNo, String registNo) {
		AjaxResult ajaxResult = new AjaxResult();
		double sum = 0.00;
		String sign="0";
		String infor="0";
		String flag="0";//???????????????
		List<PrpLCompensateVo> compVos2 = new ArrayList<PrpLCompensateVo>();
		List<PrpLCompensateVo> compVos1 = compensateTaskService.findCompensatevosByclaimNo(claimNo);
		// ??????????????????
		if (compVos1 != null && compVos1.size() > 0) {
			for (PrpLCompensateVo prpLCompensateVo : compVos1) {
				if (!(prpLCompensateVo.getUnderwriteFlag().equals("7"))) {
					compVos2.add(prpLCompensateVo);
					
				}
			}
		}
		
		List<PrpLPayCustomVo> prplcustomVos=managerService.findPayCustomVoByRegistNo(registNo);
		List<Long> payIds=new ArrayList<Long>();
		if(prplcustomVos!=null && prplcustomVos.size()>0){
			for(PrpLPayCustomVo vo:prplcustomVos){
				if(!"7".equals(vo.getPayObjectKind())){//????????????????????????
					payIds.add(vo.getId());
				}
			}
				
			
		}
		//??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????10000???????????????????????????
		//sign=1????????????????????????????????????????????????(??????)???
		if(payIds!=null && payIds.size()>0){
			if(compVos2!=null && compVos2.size()>0){
				   for (PrpLCompensateVo prpLCompensateVo : compVos2) {
					   if("1".equals(sign)){
					   break;
				       }
					 for (PrpLPaymentVo PrpLPaymentVo : prpLCompensateVo.getPrpLPayments()) {
						if(payIds.contains(PrpLPaymentVo.getPayeeId())){
							sign="1";
							break;
						}
					}
				}
			
		}
			
				if("1".equals(sign)){
					 for (PrpLCompensateVo prpLCompensateVo : compVos2) {
						 sum = sum+(prpLCompensateVo.getSumAmt().doubleValue());
					 }
				}
				List<PrpLFxqFavoreeVo> reeLists=payCustomService.findPrpLFxqFavoreeVoByclaimNo(claimNo);
				if(reeLists!=null && reeLists.size()>0){
					flag=reeLists.get(0).getFlag();
				}
					// ??????????????????10000???
					if (sum >= 10000 && !"1".equals(flag)){
                       infor="1";
					}

				
				}

		

		ajaxResult.setData(infor);
		ajaxResult.setStatus(HttpStatus.SC_OK);
		return ajaxResult;
	}
	/*
	 * niuqiang
	 * ??????????????????
	 */
	@RequestMapping(value = "/showTaxInfos.ajax")
	public ModelAndView ShowBankInfo(HttpServletRequest request) {
		
		String index=request.getParameter("index");
		String taxRate=request.getParameter("taxRate");
		String taxVlaue=request.getParameter("taxVlaue");
		String noTaxVlaue=request.getParameter("noTaxVlaue");
		ModelAndView mv = new ModelAndView();
		mv.addObject("index", index);
		mv.addObject("taxRate", taxRate);
		mv.addObject("taxVlaue", taxVlaue);
		mv.addObject("noTaxVlaue", noTaxVlaue);
	     mv.setViewName("compensate/compensateEdit_ShowTaxInfos");

		return mv;
	}
	
	//????????????????????????
	@RequestMapping(value = "/isSummary.do", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult IsSummary(Long[] payeeId) {
		AjaxResult ajaxResult = new AjaxResult();
		//???????????????
		if(payeeId.length>0){
			for(int i = 0; i<payeeId.length;i++){
				PrpLPayCustomVo paycustomVo = managerService.findPayCustomVoById(payeeId[i]);
				if(StringUtils.isNotBlank(paycustomVo.getSummary())){//?????????
					ajaxResult.setData("1");
				}else{
					ajaxResult.setData("0");
					ajaxResult.setStatus(HttpStatus.SC_OK);
					return ajaxResult;
				}
			}
		
		}else{
			ajaxResult.setData("1");
		}
		
		ajaxResult.setStatus(HttpStatus.SC_OK);
		return ajaxResult;
	}
	
	
	/**
	 * ??????????????????
	 * 
	 * @param compensateNo
	 * @param flowTaskId
	 * @return
	 */
	@RequestMapping("/backCerti.do")
	@ResponseBody
	public AjaxResult backCerti(String registNo,BigDecimal flowTaskId) {
		logger.info("?????????" + registNo + "???flowtaskid=" + flowTaskId  + "??????????????????????????????" );
		AjaxResult ajaxResult = new AjaxResult();
		ajaxResult.setStatusText("0");//?????????
		
		SysUserVo sysUserVo = WebUserUtils.getUser();
		List<PrpLWfTaskVo> voBIList = wfFlowQueryService.findPrpWfTaskVo(registNo, "Compe", "CompeBI");
		List<PrpLWfTaskVo> voCIList = wfFlowQueryService.findPrpWfTaskVo(registNo, "Compe", "CompeCI");
		List<PrpLWfTaskVo> voList = new ArrayList<PrpLWfTaskVo>();
		if(voBIList!=null && !voBIList.isEmpty()){
			for(int i = voBIList.size() - 1; i >= 0; i--){
				if(!(CodeConstants.HandlerStatus.INIT.equals(voBIList.get(i).getHandlerStatus()) || 
						CodeConstants.HandlerStatus.DOING.equals(voBIList.get(i).getHandlerStatus())||
						CodeConstants.HandlerStatus.START.equals(voBIList.get(i).getHandlerStatus()))){
					if(i==0&&voBIList.size()==1&&CodeConstants.HandlerStatus.CANCEL.equals(voBIList.get(i).getHandlerStatus())){
						continue;//in????????????????????? out??????????????????????????????????????????
					}
					voBIList.remove(i);//?????????????????????????????????
				}
			}
			if(voBIList.isEmpty()){
				ajaxResult.setStatusText("???????????????????????????????????????????????????!");//????????????
				ajaxResult.setData("2");
				return ajaxResult;			
			}
			voList.addAll(voBIList);
		}
		if(voCIList!=null && !voCIList.isEmpty()){
			for(int i = voCIList.size() - 1; i >= 0; i--){
				if(!(CodeConstants.HandlerStatus.INIT.equals(voCIList.get(i).getHandlerStatus()) || 
						CodeConstants.HandlerStatus.DOING.equals(voCIList.get(i).getHandlerStatus())||
						CodeConstants.HandlerStatus.START.equals(voCIList.get(i).getHandlerStatus()))){
					if(i==0&&voBIList.size()==1&&CodeConstants.HandlerStatus.CANCEL.equals(voCIList.get(i).getHandlerStatus())){
						continue;//in????????????????????? out??????????????????????????????????????????
					}
					voCIList.remove(i);//?????????????????????????????????
				}
			}
			if(voCIList.isEmpty()){
				ajaxResult.setStatusText("???????????????????????????????????????????????????!");//????????????
				ajaxResult.setData("2");
				return ajaxResult;			
			}
			voList.addAll(voCIList);
		}
		String backFlags = "0";
		for(PrpLWfTaskVo vo : voList){
			PrpLCompensateVo compVo = null;
			if(StringUtils.isNotBlank(vo.getCompensateNo())){
				compVo = compensateTaskService.findPrpLCompensateVoByPK(vo.getCompensateNo());
			}
			if (compVo != null && vo.getTaskId()!=null) {
				logger.info("?????????" + registNo + "????????????TaskId= "+  vo.getTaskId() + "??????????????????=" +compVo.getCompensateNo() + "???????????????????????????");
				compensateTaskService.cancelCompensates(compVo, vo.getTaskId(),sysUserVo);
				ajaxResult.setStatus(HttpStatus.SC_OK);
				backFlags = "2";
			} else if (compVo == null && vo.getTaskId() != null) {
				logger.info("?????????" + registNo + "????????????TaskId= "+  vo.getTaskId() + "???????????????????????????");
				compensateTaskService.cancelCompensates(vo.getTaskId(),sysUserVo);
				ajaxResult.setStatus(HttpStatus.SC_OK);
				backFlags = "2";
			} else {
				ajaxResult.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
				backFlags = "1";
				break;
			}
			
		}
		if("2".equals(backFlags)){
			//??????????????????start====
			PrpLCheckVo checkVo=checkTaskService.findCheckVoByRegistNo(registNo);
			PrpLCertifyMainVo certifyMainVo = certifyPubService.findPrpLCertifyMainVoByRegistNo(registNo);
			BigDecimal taskId=this.findTaskByCertify(registNo,String.valueOf(checkVo.getId()),String.valueOf(certifyMainVo.getId()));
			wfTaskHandleService.moveOutToIn(taskId);
			//????????????????????????????????????
			PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(taskId.doubleValue());
			if("AUTO".equals(wfTaskVo.getHandlerUser())){
			    wfTaskVo.setHandlerUser("");
                wfTaskVo.setAssignUser("");
                wfTaskHandleService.updateTaskIn(wfTaskVo);
			}
			//??????????????????end=====
			ajaxResult.setStatusText("????????????!");//????????????
			ajaxResult.setData("1");
		}else{
			ajaxResult.setStatusText("????????????!");//????????????
			ajaxResult.setData("2");
		}
		ajaxResult.setStatus(HttpStatus.SC_OK);
		logger.info("?????????" + registNo + "???flowtaskid=" + flowTaskId  + "????????????????????????" );
		return ajaxResult;
	}
	
	public BigDecimal findTaskByCertify(String registNo,String checkId,String certifyId){
		List<PrpLWfTaskVo> wfTaskVoList = wfTaskHandleService.findEndTask(registNo, checkId, FlowNode.Certi);
		if(wfTaskVoList.size()<=0){
			List<PrpLWfTaskVo> wfTaskList = wfTaskHandleService.findEndTask(registNo, certifyId, FlowNode.Certi);
			if(wfTaskList.size()>0)
				return wfTaskList.get(0).getTaskId();
			else return null;
		}else
			return wfTaskVoList.get(0).getTaskId();
	}
	
	@RequestMapping("/thisToCompenView.do")
	@ResponseBody
	public AjaxResult prePayToClaimView(String registNo,String sign){//sign?????????????????????????????????0????????????1?????????
		AjaxResult ajax=new AjaxResult();
		BigDecimal taskId=null;
		List<PrpLWfTaskVo> prpLWfTaskVos =null;
		try{
			if("0".equals(sign)){
				prpLWfTaskVos =wfTaskHandleService.findWftaskByRegistNoAndNodeCode(registNo,FlowNode.CompeBI.toString());
			}else{
				prpLWfTaskVos =wfTaskHandleService.findWftaskByRegistNoAndNodeCode(registNo,FlowNode.CompeCI.toString());
			}
			
		     if(prpLWfTaskVos!=null && prpLWfTaskVos.size()>0){
		    	 for(PrpLWfTaskVo prpVo:prpLWfTaskVos){
		    		 if("2".equals(prpVo.getHandlerStatus()) || "3".equals(prpVo.getHandlerStatus()) || "0".equals(prpVo.getHandlerStatus())){
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
	
	
	   /**
     * ????????????????????????
     * 
     * @param compensateNo
     * @param flowTaskId
     * @return
     
    @RequestMapping("/customerIden.do")
    @ResponseBody
    public ModelAndView customerIden() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("compensate/CompensateEdit_CustomerIden");
        return mv;
    }
    */
    
    @RequestMapping(value = "/isFrostFlag.ajax")
    @ResponseBody
    public AjaxResult isFrostFlag(String payeeList, String registNo) {
        AjaxResult ajaxResult = new AjaxResult();
        double sum = 0.00;
        List<Long> payIdlist = new ArrayList<Long>();
        String[] payArray = payeeList.split(",");
        List<String> keylist = new ArrayList<String>();
        List<PrpLCompensateVo> compVos2 = new ArrayList<PrpLCompensateVo>();
        List<PrpLCompensateVo> compVos1 = compensateTaskService.findCompensatevosByRegistNo(registNo);
        // ??????????????????
        if (payArray != null && payArray.length > 0) {
            for (int i = 0; i < payArray.length; i++) {
                payIdlist.add(Long.valueOf(payArray[i]));
            }
        }
        if (compVos1 != null && compVos1.size() > 0) {
            for (PrpLCompensateVo prpLCompensateVo : compVos1) {
                if (!(prpLCompensateVo.getUnderwriteFlag().equals("7"))) {
                    compVos2.add(prpLCompensateVo);
                
                }
            }
        }
        
        if (payIdlist != null && payIdlist.size() > 0) {
            Set<Long> payIdSet= new HashSet<Long>(payIdlist);
            
            for (Long payId : payIdSet) {
                PrpLPayCustomVo payCustomVo = managerService.findPayCustomVoById(payId);
                if ("1".equals(payCustomVo.getFrostFlag())) {//??????
                    keylist.add(payCustomVo.getPayeeName());
                }
            }
        }
        
        ajaxResult.setData(keylist);
        ajaxResult.setStatus(HttpStatus.SC_OK);
        return ajaxResult;
    }
    
    /**
     * ??????????????????
     * @param payeeList
     * @param registNo
     * @return
     * @modified:
     * ???zhujunde(2017???6???25??? ??????2:51:29): <br>
     */
    @RequestMapping(value = "/getFxUrl.ajax")
    @ResponseBody
    public AjaxResult getFxUrl(String payeeList, String registNo) {
        AjaxResult ajaxResult = new AjaxResult();
        SysUserVo userVo = WebUserUtils.getUser();
        String amlUrl = SpringProperties.getProperty("dhic.aml.url");
        logger.info("amlUrl============================"+amlUrl);
        FxqCrmRiskService crmRiskService = new FxqCrmRiskService(amlUrl);
        String url = crmRiskService.getAddRiskUrl(userVo.getUserCode());
        ajaxResult.setData(url);
        ajaxResult.setStatus(HttpStatus.SC_OK);
        return ajaxResult;
    }
    
    private static BigDecimal NullToZero(BigDecimal strNum) {
		if(strNum==null){
			return new BigDecimal("0");
		}
		return strNum;
	}
    /**
     * 
     * @param registNo
     * @return
     */
    @RequestMapping(value = "/validCaseState.ajax")
    @ResponseBody
    public AjaxResult validCaseState(String registNo){
    	AjaxResult ajax=new AjaxResult();
    	//0-?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
    	//1-???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
    	String sign="3";//
    	PrplcaseStateinfoVo caseStateInfoVo=hnfastPayInfoService.findPrplcaseStateinfoVoByRegistNo(registNo);
    	PrpLRegistVo prpregistVo=registQueryService.findByRegistNo(registNo);
    	if(prpregistVo!=null){
    		if("1".equals(prpregistVo.getIsQuickCase())){
    			List<PrpLDlossCarMainVo> carMianList=lossCarService.findLossCarMainByRegistNo(registNo);
    			if(carMianList!=null && carMianList.size()>0){
    				for(PrpLDlossCarMainVo vo:carMianList){
    					if("1".equals(vo.getDeflossCarType())&& "0".equals(vo.getOffLineHanding())){
    						if(caseStateInfoVo==null || caseStateInfoVo.getId()==null){
    							sign="0";
    						}else{
    							if(!"1".equals(caseStateInfoVo.getStatus())){
    							   sign="1";
    							}
    						}
    						
    					}
    				}
    			}
    		}
    	}
    	ajax.setData(sign);
    	ajax.setStatus(HttpStatus.SC_OK);
    	return ajax;
    }
    

    /**
     * ????????????????????????
     */
    @RequestMapping(value = "/vaxInfor.ajax")
    @ResponseBody
    public AjaxResult vaxInfor(String claimNo,String registNo,String policyNo,String feeList,String payList){
    	AjaxResult ajax=new AjaxResult();
    	AMLVo amlVo = new AMLVo();
		String sign="3";//1-?????????????????????2-??????????????????
        PrpLPayCustomVo prpLPayCustomVo = new PrpLPayCustomVo();
        PrpLPayFxqCustomVo prpLPayFxqCustomVo = new PrpLPayFxqCustomVo();
        PrpLFxqFavoreeVo prpLFxqFavoreeVo = new PrpLFxqFavoreeVo();
        List<PrpLPayFxqCustomVo> prpLPayFxqCustomVoList=new ArrayList<PrpLPayFxqCustomVo>();
        List<PrpLFxqFavoreeVo> prpLFxqFavoreeVoList=new ArrayList<PrpLFxqFavoreeVo>();
        Long mainId=null;
        List<PrpLPayCustomVo>  custmVos=payCustomService.findPayCustomVoByRegistNo(registNo);
        if(custmVos!=null && custmVos.size()>0){
        	prpLPayCustomVo=custmVos.get(0);
        	mainId=prpLPayCustomVo.getId();
        }
        if(StringUtils.isNotBlank(claimNo)){
        	prpLPayFxqCustomVoList = payCustomService.findPrpLPayFxqCustomVoByclaimNo(claimNo);
        	if(prpLPayFxqCustomVoList!=null && prpLPayFxqCustomVoList.size()>0){
                prpLPayFxqCustomVo = prpLPayFxqCustomVoList.get(0);
            }
        	
        	 prpLFxqFavoreeVoList = payCustomService.findPrpLFxqFavoreeVoByclaimNo(claimNo);
            if(prpLFxqFavoreeVoList!=null && prpLFxqFavoreeVoList.size()>0){
                prpLFxqFavoreeVo = prpLFxqFavoreeVoList.get(0);
            }
            if((prpLPayFxqCustomVoList==null && prpLFxqFavoreeVoList==null) || (prpLPayFxqCustomVoList.size()==0 && prpLFxqFavoreeVoList.size()==0)){
            	 if(mainId!=null){
                 	prpLPayCustomVo = payCustomService.findPayCustomVoById(mainId);
                     prpLPayFxqCustomVoList = payCustomService.findPrpLPayFxqCustomVoByPayId(mainId);
                     if(prpLPayFxqCustomVoList!=null && prpLPayFxqCustomVoList.size()>0){
                         prpLPayFxqCustomVo = prpLPayFxqCustomVoList.get(0);
                     }
                      prpLFxqFavoreeVoList = payCustomService.findPrpLFxqFavoreeVoByPayId(mainId);
                     if(prpLFxqFavoreeVoList!=null && prpLFxqFavoreeVoList.size()>0){
                         prpLFxqFavoreeVo = prpLFxqFavoreeVoList.get(0);
                     }
                     sign="1";
                 }
            }else{
            	sign="2";
            }
        }else{
        	 if(mainId!=null){
             	prpLPayCustomVo = payCustomService.findPayCustomVoById(mainId);
                prpLPayFxqCustomVoList = payCustomService.findPrpLPayFxqCustomVoByPayId(mainId);
                 if(prpLPayFxqCustomVoList!=null && prpLPayFxqCustomVoList.size()>0){
                     prpLPayFxqCustomVo = prpLPayFxqCustomVoList.get(0);
                 }
                 prpLFxqFavoreeVoList = payCustomService.findPrpLFxqFavoreeVoByPayId(mainId);
                 if(prpLFxqFavoreeVoList!=null && prpLFxqFavoreeVoList.size()>0){
                     prpLFxqFavoreeVo = prpLFxqFavoreeVoList.get(0);
                 }
                 sign="1";
             }
        }
        //???????????????
        if("1".equals(sign)){
        	amlVo.setAuthorityName(prpLPayCustomVo.getAuthorityName());
            amlVo.setAuthorityNo(prpLPayCustomVo.getAuthorityNo());
            
        }else if("2".equals(sign)){
        	amlVo.setAuthorityName(prpLPayFxqCustomVo.getAuthorityName());
            amlVo.setAuthorityNo(prpLPayFxqCustomVo.getAuthorityNo());
              
        }
        
        //?????????
        if(prpLFxqFavoreeVo!=null){
        	amlVo.setFavoreeIdentifyCode(prpLFxqFavoreeVo.getFavoreeIdentifyCode());
        	amlVo.setFavoreeName(prpLFxqFavoreeVo.getFavoreeName());
        }
        
        //????????????
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
        
        //?????????
       String [] payarry=payList.split(",");
       String [] feearry=feeList.split(",");
       List<PrpLPayCustomVo> voList=new ArrayList<PrpLPayCustomVo>();
       List<Long> pidList=new ArrayList<Long>();
       if(payarry!=null && payarry.length>0){
    	   for(int i=0;i<payarry.length;i++){
    		   if(StringUtils.isNotBlank(payarry[i])){
    			   pidList.add(Long.valueOf(payarry[i]));
    		   }
    		   
    	   }
       }
       if(feearry!=null && feearry.length>0){
    	   for(int i=0;i<feearry.length;i++){
    		   if(StringUtils.isNotBlank(feearry[i])){
    		   pidList.add(Long.valueOf(feearry[i]));
    		   }
    	   }
       }
       List<Long> listids=new ArrayList<Long>();
       if(pidList!=null && pidList.size()>0){
    	   Set<Long> hashids=new HashSet<Long>(pidList);
    	   for(Long id:hashids){
    		   listids.add(id);
    	   }
    	   if(hashids!=null && hashids.size()>0){
    		  voList= payCustomService.findPrpLPayCustomVoByIds(listids);
    	   }
       }
       	
       
        SysUserVo userVo = WebUserUtils.getUser();
   	    String amlUrl = SpringProperties.getProperty("dhic.aml.saveurl");
        logger.info("amlUrl============================"+amlUrl);
       
           String nameList="";
       	   FxqCrmRiskService crmRiskService = new FxqCrmRiskService(amlUrl);
       	   
       		try {
       			if(StringUtils.isNotBlank(amlVo.getIdentifyNumber())){
       				CrmRiskInfoVo  crmRiskInfoVo=crmRiskService.getCustRiskInfo(amlVo.getIdentifyNumber(),amlVo.getInsuredName(), amlVo.getIdentifyNumber(),"",userVo.getUserCode());
       				if(crmRiskInfoVo!=null && "1".equals(crmRiskInfoVo.getIsFreeze())){
       					nameList="????????????[ "+crmRiskInfoVo.getCustName()+" ]";
       				}
       		     }
       				if(StringUtils.isNotBlank(amlVo.getAuthorityNo())){
       				 if(!amlVo.getAuthorityNo().equals(amlVo.getIdentifyNumber())){
       					  CrmRiskInfoVo  crmRiskInfoVo=crmRiskService.getCustRiskInfo(amlVo.getAuthorityNo(),amlVo.getAuthorityName(), amlVo.getAuthorityNo(),"",userVo.getUserCode());
           				  if(crmRiskInfoVo!=null && "1".equals(crmRiskInfoVo.getIsFreeze())){
           					  nameList=nameList+"?????????[ "+crmRiskInfoVo.getCustName()+" ]";
           				   }
       					}
       				}
       		   
       				if(StringUtils.isNotBlank(amlVo.getFavoreeIdentifyCode())){
       					if(!amlVo.getFavoreeIdentifyCode().equals(amlVo.getIdentifyNumber()) && !amlVo.getFavoreeIdentifyCode().equals(amlVo.getAuthorityNo())){
       					  CrmRiskInfoVo  crmRiskInfoVo=crmRiskService.getCustRiskInfo(amlVo.getFavoreeIdentifyCode(),amlVo.getFavoreeName(),amlVo.getFavoreeIdentifyCode(),"",userVo.getUserCode());
           				   if(crmRiskInfoVo!=null && "1".equals(crmRiskInfoVo.getIsFreeze())){
           					  nameList=nameList+"?????????[ "+crmRiskInfoVo.getCustName()+" ]";
           				   }
       					}
       				}
       				
       				//????????????????????????
       				String payname="";
       				if(voList!=null && voList.size()>0){
       					for(PrpLPayCustomVo vo:voList){
       						if(StringUtils.isNotBlank(vo.getCertifyNo())){
       							if(!vo.getCertifyNo().equals(amlVo.getIdentifyNumber()) && !vo.getCertifyNo().equals(amlVo.getAuthorityNo()) && !vo.getCertifyNo().equals(amlVo.getFavoreeIdentifyCode())){
       							  CrmRiskInfoVo  crmRiskInfoVo=crmRiskService.getCustRiskInfo(vo.getCertifyNo(),vo.getPayeeName(),vo.getCertifyNo(),"",userVo.getUserCode());
       							  if(crmRiskInfoVo!=null && "1".equals(crmRiskInfoVo.getIsFreeze())){
       								payname=payname+crmRiskInfoVo.getCustName()+",";
       	           				   }
       							}	
       						}
       					}
       					if(StringUtils.isNotBlank(payname)){
								nameList=nameList+"?????????[ "+payname+" ]";
							}
       				}
       			ajax.setData(nameList);
       			ajax.setStatus(200);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
       	  
       	    
            
    	return ajax;
    }
    


    /**
     * ??????????????????????????????
     */
    @RequestMapping(value = "/isControlDuty.ajax")
    @ResponseBody
    public AjaxResult isControlDuty(String registNo){
        AjaxResult ajax=new AjaxResult();
        List<PrpLCMainVo> prpLCMainVos = prpLCMainService.findPrpLCMainsByRegistNo(registNo);
        if(prpLCMainVos!=null&&prpLCMainVos.size()>1){
            List<PrpLWfTaskVo> prpLWfTaskVos = wfTaskHandleService.findInTaskByOther(registNo,"","Compe");
            if(prpLWfTaskVos!=null&&prpLWfTaskVos.size()<2){
                PrpLCheckDutyVo prpLCheckDutyVo = checkTaskService.findCheckDuty(registNo,1);
                ajax.setData(prpLCheckDutyVo);
                ajax.setStatus(200);
            }   
        }
        return ajax;
    }
   
    
 /**
     * ????????????????????????????????????????????????????????????
     * @param registNo
     * @param compensateNo
     * @return
     */
  @RequestMapping(value="/isControlIsderoAmout.ajax")
  @ResponseBody
  public AjaxResult isControlIsderoAmout(String registNo,String compensateNo){
	     AjaxResult ajax=new AjaxResult();  
    	String claimCompleteFlag="0";//?????????????????????????????????????????????????????????0,1???????????????1(???????????????)???
		List<PrpLWfTaskVo> wfTaskVos=wfTaskHandleService.findTaskOutVo(registNo,FlowNode.Compe.name());
		if(wfTaskVos!=null && wfTaskVos.size()>0){
			for(PrpLWfTaskVo vo:wfTaskVos){
				if("3".equals(vo.getWorkStatus())){
					String backFlag="0";//????????????????????????????????????0-???????????????1-?????????
					List<PrpLWfTaskVo> wftaskins=wfTaskHandleService.findTaskInVo(registNo,vo.getCompensateNo(),FlowNode.Compe.name());
					if(wftaskins!=null && wftaskins.size()>0){
						for(PrpLWfTaskVo invo:wftaskins){
							if("6".equals(invo.getWorkStatus())){
								backFlag="1";
							}
						}
					}
					if("0".equals(backFlag)){
					  if(!vo.getCompensateNo().equals(compensateNo)){
						PrpLCompensateVo compensateVo=compensateTaskService.findCompByPK(vo.getCompensateNo());
						   if(compensateVo!=null && !"7".equals(compensateVo.getUnderwriteFlag())){
							   ajax.setStatusText(compensateVo.getCisderoAmout()+"");
							   if(StringUtils.isNotBlank(compensateVo.getInSideDeroFlag())){
								   ajax.setStatus(Long.valueOf(compensateVo.getInSideDeroFlag()));
							   }
							   claimCompleteFlag="1";
							   break;
							   
						    }
						}
					  
					 
					}
				}
					
				
			}
			
		}
		ajax.setData(claimCompleteFlag);
		return ajax;
	}
  
    /**
     * ?????????????????????????????????????????????????????????
     * @param resp
     * @param compensateNo
     * @return
     * @throws Exception
     */
	@RequestMapping("/compensateEdits.do")
	public ModelAndView reinsuranceToCompensate(HttpServletResponse resp, String compensateNo) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		
		String registNo1 = null;
		Double flowTaskId = null;
		PrpLCompensateVo voo = compensateTaskService.findCompByPK(compensateNo);
		if(voo != null && !voo.equals("")){
			registNo1 = voo.getRegistNo();
		}
		
		List<PrpLWfTaskVo> taskVo = wfTaskHandleService.findOutTaskVo(registNo1, compensateNo, "Compe");
		if(taskVo != null && taskVo.size() > 0){
			flowTaskId = taskVo.get(0).getTaskId() == null ? 0d : taskVo.get(0).getTaskId().doubleValue();
		}else{
			resp.setCharacterEncoding("UTF-8");
			resp.setContentType("Text/plain");
			PrintWriter out = resp.getWriter();
			if(registNo1 != null && !registNo1.equals("")){
				out.print("??????????????????????????????????????????????????????????????????");
			}else{
				out.print("???????????????????????????????????????");
			}
			out.close();
			return null;
		}
		
		SysUserVo userVo = WebUserUtils.getUser();
		CompensateActionVo actionVo = compHandleService.compensateEdit(flowTaskId,userVo); 
		String registNo = actionVo.getCompensateVo().getRegistNo();
		//?????????????????????????????????????????????????????????????????????????????????
		List<PrpLEndCaseVo> listVos = endCasePubService.queryAllByRegistNo(registNo);
		if(listVos!=null && listVos.size() >0){
			modelAndView.addObject("endCaseFlag", "1");//?????????
		}else{
			modelAndView.addObject("endCaseFlag", "0");//?????????
		}
	
		if(actionVo.getCompFlag().equals(CompensateType.COMP_BI)){
			modelAndView.addObject("claimKindMList", actionVo.getClaimKindMList());
			modelAndView.addObject("cprcCase", actionVo.isCprcCase());
			modelAndView.addObject("claimDeductVos", actionVo.getClaimDeductVos());
			modelAndView.addObject("deviceMap", actionVo.getDeviceMap());
		}
		
		PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.STARFLAG);

		if(configValueVo!=null && "1".equals(configValueVo.getConfigValue()) && "3".equals(actionVo.getWfTaskVo().getHandlerStatus()) ){
			if(actionVo.getPaymentVoList() != null && actionVo.getPaymentVoList().size()>0){
				for(int i=0; i<actionVo.getPaymentVoList().size();i++){
					if(actionVo.getPaymentVoList().get(i).getAccountNo() != null){
						actionVo.getPaymentVoList().get(i).setAccountNo(DataUtils.replacePrivacy(actionVo.getPaymentVoList().get(i).getAccountNo()));
					}
				}
			}
			
			if(actionVo.getPrpLChargees() != null && actionVo.getPrpLChargees().size()>0){
				for(int i=0; i<actionVo.getPrpLChargees().size();i++){
					if(actionVo.getPrpLChargees().get(i).getAccountNo() != null){
						actionVo.getPrpLChargees().get(i).setAccountNo(DataUtils.replacePrivacy(actionVo.getPrpLChargees().get(i).getAccountNo()));
					}
				}
			}
		}
		List<PrpLCheckDutyVo> prpLCheckDutyVoList = checkTaskService.findCheckDutyByRegistNo(registNo);
		//?????????????????????????????????????????????????????????;?????????????????????????????????????????????????????????;?????????????????????????????????????????????????????????????????????????????????
		String dutyShowFlag = "N";
		if("1101".equals(actionVo.getCompensateVo().getRiskCode())){
			dutyShowFlag = "Y";// ????????????????????????
		}else{
			List<PrpLClaimVo> claimVoList = claimService.findClaimListByRegistNo(registNo,"1"); 
			if(claimVoList!=null && claimVoList.size()==1 && !"1101".equals(claimVoList.get(0).getRiskCode())){
				// ?????????????????? ?????????????????????
				dutyShowFlag = "Y";
			}
		}
		
		//??????????????????
		String lossCarSign="0";//??????????????????????????????????????????
		if(actionVo.getCompensateVo()!=null){
		    List<PrpLDlossCarMainVo> carMainVos= lossCarService.findLossCarMainByUnderWriteFlag(registNo,"6","0");
	        if(carMainVos!=null && carMainVos.size()>0){
	    	    for(PrpLDlossCarMainVo vo:carMainVos){
	    		    if("1".equals(vo.getUnderWriteFlag())){
	    		    	lossCarSign="1";
	    		    	break;
	    		    }
	    		}
	    	}
		}
       
		
		 //??????????????????????????????????????????
      String compensateSign="0";//?????????????????????????????????
      if(actionVo.getCompensateVo()!=null){
      	List<PrpLCompensateVo> compensates=compensateTaskService.findCompensatevosByRegistNo(registNo);
   	    if(compensates!=null && compensates.size()>0){
   		    for(PrpLCompensateVo vo:compensates){
   		    	if("N".equals(vo.getCompensateType())){
   		    		compensateSign="1";
   		    		break;
   		    	}
   	        }
   		}
      }

	       //????????????
	    PrpLCertifyMainVo certifyMainVo = certifyPubService.findPrpLCertifyMainVoByRegistNo(registNo);
	    String isFraud = "0"; // ???????????? ???
	    String fraudLogo = "03";  //????????????  ???????????? 
	    String isRefuse = "0";  //????????????
	    String refuseReason = "";
	    String compType = "0";
		if (certifyMainVo != null) {
			isFraud = certifyMainVo.getIsFraud();
			fraudLogo = certifyMainVo.getFraudLogo();
			if (actionVo.getCompFlag().equals(CompensateType.COMP_CI)) {
				isRefuse = certifyMainVo.getIsJQFraud();
				compType = "1";
			} else if (actionVo.getCompFlag().equals(CompensateType.COMP_BI)) {
				isRefuse = certifyMainVo.getIsSYFraud();
				compType = "2";
			}
			if (CodeConstants.ValidFlag.VALID.equals(actionVo.getDwPersFlag())) {
				actionVo.getCompensateVo().setRecoveryFlag(
						CodeConstants.ValidFlag.VALID);
			}
			refuseReason = certifyMainVo.getFraudRefuseReason();
		}
		//????????????????????????
		
		PrplcomcontextVo prplcomContextVo=compHandleService.findPrplcomcontextByCompensateNo(actionVo.getCompensateVo().getCompensateNo(),"C");
		if(prplcomContextVo!=null){
			 modelAndView.addObject("prplcomContextVo",prplcomContextVo);
		}else{
			prplcomContextVo=new PrplcomcontextVo();
			modelAndView.addObject("prplcomContextVo",prplcomContextVo);
		}
	   
	
	    //????????????????????????  
	    String isMinorInjuryCases="";
	    if(actionVo.getCompensateVo()!=null){
	    	List<PrpLDlossPersTraceMainVo> traceMains=persTraceServices.findPersTraceMainVo(registNo);
	    	if(traceMains!=null && traceMains.size()>0){
	    		for(PrpLDlossPersTraceMainVo traceMainVo:traceMains){
	    			if(!"1101".equals(actionVo.getCompensateVo().getRiskCode())){
	    				isMinorInjuryCases=traceMainVo.getIsMinorInjuryCases();
	    			}else if("1101".equals(actionVo.getCompensateVo().getRiskCode())){
	    				isMinorInjuryCases=traceMainVo.getIsMinorInjuryCases();
	    			}else{
	    				
	    			}
	    		}
	    	}	
	    }
	    
	    if(StringUtils.isBlank(isMinorInjuryCases)){
	    	isMinorInjuryCases="0";
	    }
	    //??????????????????
	    PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(registNo);
	    if(StringUtils.isNotBlank(prpLRegistVo.getPrpLRegistExt().getLicenseNo())){
	    	modelAndView.addObject("licenseNoSummary",prpLRegistVo.getPrpLRegistExt().getLicenseNo());
	    }
	    modelAndView.addObject("compType",compType);  
	    
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
	  	if(StringUtils.isBlank(actionVo.getCompensateVo().getinsuredPhone())){
	  	  actionVo.getCompensateVo().setinsuredPhone(compensateTaskService.findInsuredPhone(registNo));
	  	}
	  	modelAndView.addObject("assessSign",assessSign);
      modelAndView.addObject("isMinorInjuryCases",isMinorInjuryCases); 
	    modelAndView.addObject("compType",compType);  
	    modelAndView.addObject("refuseReason",refuseReason);  
	    modelAndView.addObject("isRefuse",isRefuse);  
	  	List<PrpLCMainVo> prpLCMains = prpLCMainService.findPrpLCMainsByRegistNo(registNo);
	  	PrpLCMainVo prpLCMain = new PrpLCMainVo();
	  	if(prpLCMains != null && prpLCMains.size() == 2){
          for(PrpLCMainVo vo : prpLCMains){
             if("12".equals(vo.getRiskCode().substring(0, 2))){//?????????
                  prpLCMain = vo;
              }
           }
	       }else{
	             prpLCMain = prpLCMains.get(0);
	    }
	  	String policyNo=prpLCMain.getPolicyNo();
	  	if(prpLCMains!=null && prpLCMains.size()>0){
	  		 for(PrpLCMainVo vo : prpLCMains){
	  			 if(actionVo.getCompFlag().equals(CompensateType.COMP_BI) && !"1101".equals(vo.getRiskCode()) ){
	  				policyNo=vo.getPolicyNo();
	  			 }
	  			if(actionVo.getCompFlag().equals(CompensateType.COMP_CI) && "1101".equals(vo.getRiskCode()) ){
	  				policyNo=vo.getPolicyNo();
	  			 }
	  		 }
	  	}
	  	List<PrpLClaimVo> claimVos = claimService.findClaimListByRegistNo(registNo);
	  	String claimNo="";
	  	String riskCode="";
	  	if(claimVos!=null && claimVos.size()>0){
	  		 for(PrpLClaimVo vo : claimVos){
	  			 if(actionVo.getCompFlag().equals(CompensateType.COMP_BI) && !"1101".equals(vo.getRiskCode()) ){
	  				claimNo=vo.getClaimNo();
	  				riskCode=vo.getRiskCode();
	  			 }
	  			if(actionVo.getCompFlag().equals(CompensateType.COMP_CI) && "1101".equals(vo.getRiskCode()) ){
	  				claimNo=vo.getClaimNo();
	  				riskCode=vo.getRiskCode();
	  			 }
	  		 }
	  	}
	  	//?????????????????????????????? ????????????150???????????????150??????
	  	if(actionVo.getLossPropList()!=null && actionVo.getLossPropList().size()>0){
	  		for(PrpLLossPropVo vo:actionVo.getLossPropList()){
	  			if(StringUtils.isNotBlank(vo.getLossName()) && vo.getLossName().length()>=150){
	  				String str=vo.getLossName().substring(0, 149);
	  				vo.setLossName(str+"???");
	  			}
	  		}
	  	}
	  	
	  	//????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
	  	PrpLDlossPersTraceMainVo persTraceMainVo=new PrpLDlossPersTraceMainVo();
		List<PrpLDlossPersTraceMainVo> persTracelist=persTraceService.findPrpLDlossPersTraceMainVoListByRegistNoDesc(registNo);
		if(persTracelist!=null && persTracelist.size()>0){
				persTraceMainVo=persTracelist.get(0);
				if(StringUtils.isBlank(actionVo.getCompensateVo().getRecoveryFlag()) && "05".equals(persTraceMainVo.getCaseProcessType())){
					actionVo.getCompensateVo().setRecoveryFlag("1");
				}
				actionVo.getCompensateVo().setPisderoAmout(persTraceMainVo.getIsDeroVerifyAmout());//??????????????????????????????????????????????????????
				//????????????????????????
				actionVo.getCompensateVo().setInSideDeroPersonFlag(persTraceMainVo.getInSideDeroFlag());
		}
	   //????????????????????????????????????????????????????????????????????????
		String reOpenFlag="0";//0-????????????1-?????????
		String claimCompleteFlag="0";//?????????????????????????????????????????????????????????0,1???????????????1(???????????????)???
		if("0".equals(reOpenFlag)){
			List<PrpLWfTaskVo> wfTaskVos=wfTaskHandleService.findTaskOutVo(registNo,FlowNode.Compe.name());
			if(wfTaskVos!=null && wfTaskVos.size()>0){
				for(PrpLWfTaskVo vo:wfTaskVos){
					if("3".equals(vo.getWorkStatus())){
						String backFlag="0";//????????????????????????????????????0-???????????????1-?????????
						List<PrpLWfTaskVo> wftaskins=wfTaskHandleService.findTaskInVo(registNo,vo.getCompensateNo(),FlowNode.Compe.name());
						if(wftaskins!=null && wftaskins.size()>0){
							for(PrpLWfTaskVo invo:wftaskins){
								if("6".equals(invo.getWorkStatus())){
									backFlag="1";
								}
							}
						}
						if("0".equals(backFlag)){
						PrpLCompensateVo compensateVo=compensateTaskService.findCompByPK(vo.getCompensateNo());
						  if(!vo.getCompensateNo().equals(actionVo.getCompensateVo().getCompensateNo()) && !"3".equals(actionVo.getWfTaskVo().getWorkStatus())){
							if(compensateVo!=null && !"7".equals(compensateVo.getUnderwriteFlag())){
								  actionVo.getCompensateVo().setCisderoAmout(compensateVo.getCisderoAmout());
								  actionVo.getCompensateVo().setInSideDeroFlag(compensateVo.getInSideDeroFlag());
							    }
							}
						  //??????????????????
						  if(compensateVo!=null && !"7".equals(compensateVo.getUnderwriteFlag())){
							  claimCompleteFlag="1";
							  break; 
						  }
						  
						}
					}
						
				}
			}
		
		}
		String subrogationLock = "1";
		PrpLSubrogationMainVo prpLSubrogationMainVo = subrogationService.find(registNo);
		if("1".equals(prpLSubrogationMainVo.getSubrogationFlag()) && prpLSubrogationMainVo.getPrpLSubrogationPersons().size()==0){//????????????????????????????????????????????????
          boolean isLock = compHandleService.subrogationIsLock(prpLSubrogationMainVo,policyNo);
          if(!isLock){
              subrogationLock = "0";
          }
      }
		//???????????????
		if(StringUtils.isEmpty(actionVo.getCompensateVo().getCompensateNo())){
			modelAndView.addObject("coinsSize",0);  
		}else{
			List<PrpLCoinsVo> prpLCoinsList = compensateTaskService.findPrpLCoinsByCompensateNo(actionVo.getCompensateVo().getCompensateNo());
			modelAndView.addObject("coinsSize",prpLCoinsList.size());  
		}
		
		modelAndView.addObject("payrefFlag",actionVo.getPayrefFlag()); 
		modelAndView.addObject("oldClaim",actionVo.getRegistVo().getFlag());
      modelAndView.addObject("subrogationLock",subrogationLock); 
		modelAndView.addObject("reOpenFlag",reOpenFlag);  
		modelAndView.addObject("claimCompleteFlag",claimCompleteFlag); 
	    modelAndView.addObject("riskCode",riskCode);
	  	modelAndView.addObject("policyNo",policyNo);
	  	modelAndView.addObject("claimNo",claimNo);
	    modelAndView.addObject("prpLCMain",prpLCMain); 
	    modelAndView.addObject("isMinorInjuryCases",isMinorInjuryCases); 
	    modelAndView.addObject("isFraud",isFraud);  
	    modelAndView.addObject("fraudLogo",fraudLogo);  
	    modelAndView.addObject("dwPersFlag",actionVo.getDwPersFlag());
	    modelAndView.addObject("compensateSign",compensateSign);  
		modelAndView.addObject("lossCarSign",lossCarSign);
		modelAndView.addObject("dutyShowFlag",dutyShowFlag);
		modelAndView.addObject("prpLCheckDutyVoList",prpLCheckDutyVoList);
		modelAndView.addObject("flag", actionVo.getCompFlag());
		modelAndView.addObject("prpLPadPayPersons", actionVo.getPadPayPersons());
		modelAndView.addObject("prpLPrePayP", actionVo.getPrpLPrePayP());
		modelAndView.addObject("prpLPrePayF", actionVo.getPrpLPrePayF());
		modelAndView.addObject("flowTaskId", flowTaskId);
		modelAndView.addObject("kindForChaMap", actionVo.getKindForChaMap());
		modelAndView.addObject("kindForOthMap", actionVo.getKindForOthMap());
		modelAndView.addObject("prpLWfTaskVo", actionVo.getWfTaskVo());
		modelAndView.addObject("handlerStatus", actionVo.getWfTaskVo().getHandlerStatus());
		modelAndView.addObject("prpLCompensate", actionVo.getCompensateVo());
	   //modelAndView.addObject("prpLKindAmtSummaries", prpLKindAmtSummaries);
		modelAndView.addObject("prpLPaymentVos", actionVo.getPaymentVoList());
		modelAndView.addObject("prpLLossItems", actionVo.getLossItemVoList());
		modelAndView.addObject("prpLLossProps", actionVo.getLossPropList());
		modelAndView.addObject("prpLLossPersons", actionVo.getLossPersonList());
		modelAndView.addObject("prpLChargeVos", actionVo.getPrpLChargees());
		modelAndView.addObject("otherLossProps", actionVo.getOtherLossProps());
		modelAndView.addObject("buJiState", "1");
		modelAndView.addObject("quantity", actionVo.getPersonCount());
		modelAndView.addObject("dwFlag", actionVo.getDwFlag());
		modelAndView.addObject("qfLicenseMap", actionVo.getQfLicenseMap());
		modelAndView.addObject("unitamount", actionVo.getUnitamount());
		modelAndView.addObject("userVo", userVo);
		modelAndView.addObject("isGbFlag", StringUtils.isNotBlank(actionVo.getRegistVo().getIsGBFlag()) ? actionVo.getRegistVo().getIsGBFlag() : "0");
		//??????????????????????????????????????????????????????????????????????????????
		String policeInfoFlag="0";//????????????
		String policyComCode = policyViewService.getPolicyComCode(registNo);
		if(policyComCode.startsWith("62")){
			policeInfoFlag = "1";
			String carRiskUrl = SpringProperties.getProperty("CARRISK_URL");
	        String carRiskUserName = SpringProperties.getProperty("SDWARN_YMUSER");
	        String carRiskPassWord = SpringProperties.getProperty("SDWARN_YMPW");
	        modelAndView.addObject("comparePicURL",carRiskUrl);
	        String claimPeriod = "05";
	        modelAndView.addObject("claimPeriod",claimPeriod);
	        modelAndView.addObject("carRiskUserName",carRiskUserName);
	        modelAndView.addObject("carRiskPassWord",carRiskPassWord);
	        //??????????????????
	        String claimSequenceNo = "";
	        if(prpLCMain != null ){//??????????????????
	            if(StringUtils.isNotBlank(prpLCMain.getClaimSequenceNo())){
	                claimSequenceNo = prpLCMain.getClaimSequenceNo();
	               modelAndView.addObject("claimSequenceNo",claimSequenceNo);
	            }
	        }
		}
		modelAndView.addObject("policeInfoFlag",policeInfoFlag);
				
		
      //???????????????ILOG????????????????????????
      String nodeCode=actionVo.getWfTaskVo().getSubNodeCode();
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
		modelAndView.addObject("roleFlag",roleFlag);
		modelAndView.setViewName("compensate/CompensateEdit");
		return modelAndView;
		
	}
	
	@RequestMapping(value="/unlockCompensate.do")
	public ModelAndView unlockCompensate(){
		ModelAndView mv=new ModelAndView();
		
		mv.setViewName("compensate/unlockcompensate");
		return mv;
		
		
	}
	
	 /**
     * ???????????????
     */
    @RequestMapping(value = "/updateHandler.ajax")
    @ResponseBody
    public AjaxResult updateHandler(String registNo,String handler1Code,String nodeCode){
        AjaxResult ajax=new AjaxResult();
        String subNodeCode="CompeBI";
        if("1".equals(nodeCode)){
        	subNodeCode="CompeBI";
        }else{
        	subNodeCode="CompeCI";
        }
        PrpLWfTaskVo prpLWfTaskVo=wfTaskHandleService.findWftaskInByRegistnoAndSubnode(registNo, subNodeCode);
        if(prpLWfTaskVo==null){
        	ajax.setStatus(500);
        	ajax.setStatusText("????????????????????????????????????");
        }else{
        	SysUserVo sysUserVo=scheduleTaskService.findPrpduserByUserCode(handler1Code, "1");
        	if(sysUserVo==null || StringUtils.isBlank(sysUserVo.getUserCode())){
        		ajax.setStatus(500);
            	ajax.setStatusText("???????????????????????????");
        	}else{
        		PrpLRegistVo PrpLRegistVo=registQueryService.findByRegistNo(registNo);
        		prpLWfTaskVo.setAssignUser(sysUserVo.getUserCode());
            	prpLWfTaskVo.setAssignCom(PrpLRegistVo.getComCode());
            	prpLWfTaskVo.setHandlerCom(PrpLRegistVo.getComCode());
            	prpLWfTaskVo.setHandlerUser(sysUserVo.getUserCode());
            	prpLWfTaskVo.setAcceptTime(new Date());
            	wfTaskHandleService.updateTaskIn(prpLWfTaskVo);
            	ajax.setStatus(HttpStatus.SC_OK);
        	}
        	
        	
        }
        return ajax;
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
