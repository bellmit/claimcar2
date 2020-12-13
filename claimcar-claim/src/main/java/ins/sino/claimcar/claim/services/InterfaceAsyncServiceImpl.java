package ins.sino.claimcar.claim.services;

import ins.platform.common.util.ConfigUtil;
import ins.platform.vo.PrpLConfigValueVo;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.carchild.service.CarchildService;
import ins.sino.claimcar.carchild.vo.RegistInformationVo;
import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;
import ins.sino.claimcar.carplatform.vo.CiClaimPlatformLogVo;
import ins.sino.claimcar.certify.service.CertifyIlogService;
import ins.sino.claimcar.ciitc.service.CompeInterfaceService;
import ins.sino.claimcar.claim.service.ClaimTaskService;
import ins.sino.claimcar.claim.service.ClaimToReinsuranceService;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.claim.service.DlclaimTaskService;
import ins.sino.claimcar.claim.service.InterfaceAsyncService;
import ins.sino.claimcar.claim.vo.*;
import ins.sino.claimcar.claimcarYJ.service.ClaimcarYJService;
import ins.sino.claimcar.claimjy.service.JyCleanDataService;
import ins.sino.claimcar.claimjy.service.JyTaskService;
import ins.sino.claimcar.claimjy.service.JyZeroNoticeService;
import ins.sino.claimcar.claimjy.service.PrpLRegistToLossService;
import ins.sino.claimcar.claimjy.vo.JyResVo;
import ins.sino.claimcar.endcase.vo.PrpLEndCaseVo;
import ins.sino.claimcar.flow.service.PayCustomService;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.genilex.ReportInfoService;
import ins.sino.claimcar.genilex.dlossService.ClaimJxService;
import ins.sino.claimcar.hnbxrest.service.ReceiveResultService;
import ins.sino.claimcar.hnbxrest.vo.ReceiveauditingresultVo;
import ins.sino.claimcar.manager.service.ManagerService;
import ins.sino.claimcar.manager.vo.PrpLPayCustomVo;
import ins.sino.claimcar.manager.vo.PrpdIntermMainVo;
import ins.sino.claimcar.middlestagequery.service.ClaimToMiddleStageOfCaseService;
import ins.sino.claimcar.mobilecheck.service.SendMsgToMobileService;
import ins.sino.claimcar.other.service.AcheckService;
import ins.sino.claimcar.other.service.AssessorService;
import ins.sino.claimcar.other.vo.PrpLAcheckMainVo;
import ins.sino.claimcar.other.vo.PrpLAcheckVo;
import ins.sino.claimcar.other.vo.PrpLAssessorFeeVo;
import ins.sino.claimcar.other.vo.PrpLAssessorMainVo;
import ins.sino.claimcar.other.vo.PrpLAssessorVo;
import ins.sino.claimcar.other.vo.PrpLCheckFeeVo;
import ins.sino.claimcar.newpayment.service.ClaimToNewPaymentService;
import ins.sino.claimcar.payment.service.ClaimToPaymentDetailService;
import ins.sino.claimcar.payment.service.ClaimToPaymentService;
import ins.sino.claimcar.platform.service.CertifyToPaltformService;
import ins.sino.claimcar.platform.service.CheckToPlatformService;
import ins.sino.claimcar.platform.service.ClaimToPaltformService;
import ins.sino.claimcar.platform.service.LossToPlatformService;
import ins.sino.claimcar.platform.service.RegistToPaltformService;
import ins.sino.claimcar.realtimequery.service.VehicleInfoQueryService;
import ins.sino.claimcar.regist.service.FounderCustomService;
import ins.sino.claimcar.regist.service.PolicyInfoRegisterService;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.schedule.service.ScheduleService;
import ins.sino.claimcar.schedule.vo.PrpLScheduleDefLossVo;
import ins.sino.claimcar.schedule.vo.PrpLScheduleTaskVo;
import ins.sino.claimcar.trafficplatform.service.CarRiskImagesUpdateService;
import ins.sino.claimcar.trafficplatform.service.CheckToWarnService;
import ins.sino.claimcar.trafficplatform.service.ClaimToWarnService;
import ins.sino.claimcar.trafficplatform.service.RegistToCarRiskPaltformService;
import ins.sino.claimcar.trafficplatform.service.SdpoliceCaseService;
import ins.sino.claimcar.trafficplatform.service.SdpoliceService;
import ins.sino.claimcar.trafficplatform.service.SzpoliceRegistService;
import ins.sino.claimcar.selfHelpClaimCar.service.SelfHelpClaimResultService;
import ins.sino.claimcar.sunyardimage.service.ImagesDownLoadService;
import ins.sino.claimcar.sunyardimage.vo.response.ResNumRootVo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import com.alibaba.dubbo.config.annotation.Service;

/**
 * 本类实现接口的异步调用 不阻塞主线程 没有返回值
 * 
 * <pre>
 * 异步发送接口报文
 * </pre>
 * @author ★niuqiang
 */
@Async("asyncExecutor")
@Service(protocol = { "dubbo" }, validation = "true", registry = { "default" })
@Path("interfaceAsyncService")
public class InterfaceAsyncServiceImpl implements InterfaceAsyncService {
	
	private Logger logger = LoggerFactory.getLogger(InterfaceAsyncServiceImpl.class);
	
	@Autowired
	ClaimToPaymentService claimToPaymentService;
	@Autowired
	ClaimToReinsuranceService claimToReinsuranceService;
	@Autowired
	ClaimTaskService claimTaskService;
	@Autowired
	CompensateTaskService compensateTaskService;
	
	@Autowired
	ClaimToPaymentDetailService claimToPaymentDetailService;
	@Autowired
	ClaimToNewPaymentService claimToNewPaymentService;
	@Autowired
	CertifyToPaltformService certifyToPaltformService;
    @Autowired
    ScheduleService scheduleService;
    
	@Autowired
	CheckToPlatformService checkToPlatformService;
	@Autowired
	ClaimToPaltformService claimToPaltformService;
	@Autowired
	LossToPlatformService lossToPlatformService;
	@Autowired
	RegistToPaltformService registToPaltformService;
	@Autowired
	FounderCustomService founderCustomService;
	@Autowired
    ClaimInvoiceService claimInvoiceService;
	@Autowired
	PayCustomService payCustomService;
	@Autowired
	SendMsgToMobileService sendMsgToMobileService;
	@Autowired
	ReceiveResultService receiveResultService;
	@Autowired
	CarchildService carchildService;
	@Autowired
	WfTaskHandleService wfTaskHandleService;
	@Autowired
	PolicyViewService policyViewService;
    @Autowired
    RegistQueryService registQueryService;
    @Autowired
    ManagerService managerService;
	@Autowired
    SdpoliceService sdpoliceService;
    @Autowired
    ReportInfoService reportInfoService; 
    @Autowired 
	SdpoliceCaseService sdpoliceCaseService;
	@Autowired
    CertifyIlogService certifyIlogService;
    @Autowired
    RegistToCarRiskPaltformService registToCarRiskPaltformService;
    @Autowired
    ClaimToWarnService claimToWarnService;
    @Autowired
    ClaimJxService claimJxService;
    @Autowired
    CheckToWarnService checkToWarnService;
    @Autowired
    CarRiskImagesUpdateService carRiskImagesUpdateService;
	@Autowired
	SzpoliceRegistService szpoliceRegistService;
    @Autowired
    JyTaskService jyTaskService;
    @Autowired
    JyCleanDataService jyCleanDataService;
    @Autowired
    JyZeroNoticeService jyZeroNoticeService;
	@Autowired
	PrpLRegistToLossService prpLRegistToLossService;
    @Autowired
    CompeInterfaceService compeInterfaceService;
	@Autowired
	SelfHelpClaimResultService selfHelpClaimResultService;
	@Autowired
	ImagesDownLoadService imagesDownLoadService;
	@Autowired
	AssessorService assessorService;
	@Autowired
	AcheckService acheckService;
	@Autowired
	ClaimcarYJService claimcarYJService;
	@Autowired
	DlclaimTaskService dlclaimTaskService;
    @Autowired
    ClaimToMiddleStageOfCaseService claimToMiddleStageOfCaseService;
	@Autowired
	PolicyInfoRegisterService policyInfoRegisterService;
	@Autowired
	VehicleInfoQueryService vehicleInfoQueryService;
	// 公估费送发票
	@Override
	public void assessorToInvoice(PrpLAssessorMainVo assessorMainVo,SysUserVo userVo) throws Exception{
		List<PrpLAssessorFeeVo> assessorFeeLsit = assessorMainVo.getPrpLAssessorFees();
		if(assessorFeeLsit!=null && assessorFeeLsit.size() > 0){
			for(PrpLAssessorFeeVo assessorFeeVo : assessorFeeLsit){
				if(assessorFeeVo.getAmount()!=null && assessorFeeVo.getAmount().compareTo(BigDecimal.ZERO)==1){
					Boolean result = claimToPaymentService.pushAssessorFee(assessorFeeVo, userVo);
					//公估费送发票失败，记录日志，方便查找原因
					if(!result){
						logger.info("报案号： " + assessorFeeVo.getRegistNo() + "公估费表ID： " + assessorFeeVo.getId() + " 该笔公估费送发票失败！！！ ");
					}
				}
			}
		}
	}

	// 公估费送收付
	@Override
	public void assessorToPayment(PrpLAssessorMainVo assessorMainVo) throws Exception{
		List<PrpLAssessorFeeVo> assessorFeeLsit = assessorMainVo.getPrpLAssessorFees();
		if(assessorFeeLsit!=null && assessorFeeLsit.size() > 0){
			for(PrpLAssessorFeeVo assessorFeeVo : assessorFeeLsit){
				if(assessorFeeVo.getAmount()!=null && assessorFeeVo.getAmount().compareTo(BigDecimal.ZERO)==1){
					// claimToPaymentDetailService.assessorToPayment(assessorMainVo,assessorFeeVo);
					claimToNewPaymentService.assessorToNewPayment(assessorMainVo, assessorFeeVo);
				}
			}
		}
	}

	// 公估费送再保
	@Override
	public void assessorToReins(PrpLAssessorMainVo assessorMainVo)
			throws Exception {
		List<PrpLAssessorFeeVo> assessorFeeLsit = assessorMainVo.getPrpLAssessorFees();
		if (assessorFeeLsit != null && assessorFeeLsit.size() > 0) {
			for (PrpLAssessorFeeVo assessorFeeVo : assessorFeeLsit) {
				PrpLClaimVo claimVo = claimTaskService.findClaimVoByClaimNo(assessorFeeVo.getClaimNo());
				PrpLCompensateVo prpLCompensateVo = compensateTaskService.findCompByClaimNo(assessorFeeVo.getClaimNo());
				if(claimVo!=null&& !"".equals(claimVo.getCaseNo())){ // 如果结案 按照 重开-已决-重开 送公估费
					claimToReinsuranceService.asseFeeToReinsOut(claimVo, prpLCompensateVo, assessorFeeVo);
				}
			}
		}
	}

	    // 查勘费送发票
		@Override
		public void checkFeeToInvoice(PrpLAcheckMainVo checkMainVo,SysUserVo userVo) throws Exception{
			List<PrpLCheckFeeVo>  checkFeeLsit= checkMainVo.getPrpLCheckFees();
			if(checkFeeLsit!=null && checkFeeLsit.size() > 0){
				for( PrpLCheckFeeVo checkFeeVo : checkFeeLsit){
					if(checkFeeVo.getAmount()!=null && checkFeeVo.getAmount().compareTo(BigDecimal.ZERO)==1){
						Boolean result = claimToPaymentService.pushCheckFee(checkFeeVo, userVo);
					}
				}
			}
		}

		// 查勘费送收付
		@Override
		public void checkFeeToPayment(PrpLAcheckMainVo checkMainVo) throws Exception{
			List<PrpLCheckFeeVo>  checkFeeLsit= checkMainVo.getPrpLCheckFees();
			if(checkFeeLsit!=null && checkFeeLsit.size() > 0){
				for(PrpLCheckFeeVo checkFeeVo : checkFeeLsit){
					if(checkFeeVo.getAmount()!=null && checkFeeVo.getAmount().compareTo(BigDecimal.ZERO)==1){
						// claimToPaymentDetailService.checkFeeToPayment(checkMainVo,checkFeeVo);
						claimToNewPaymentService.checkFeeToNewPayment(checkMainVo, checkFeeVo);
					}
				}
			}
		}
		
		@Override
		public void checkFeeToReins(PrpLAcheckMainVo prpLAcheckMainVo)throws Exception {
			List<PrpLCheckFeeVo> checkFeeLsit= prpLAcheckMainVo.getPrpLCheckFees();
			if (checkFeeLsit != null && checkFeeLsit.size() > 0) {
				for (PrpLCheckFeeVo checkFeeVo : checkFeeLsit) {
					PrpLClaimVo claimVo = claimTaskService.findClaimVoByClaimNo(checkFeeVo.getClaimNo());
					PrpLCompensateVo prpLCompensateVo = compensateTaskService.findCompByClaimNo(checkFeeVo.getClaimNo());
					if(claimVo!=null&& !"".equals(claimVo.getCaseNo())){ // 如果结案 按照 重开-已决-重开 送查勘费
						claimToReinsuranceService.checkFeeToReinsOut(claimVo, prpLCompensateVo, checkFeeVo);
					}
				}
			}
		}
    
    @Override
    public void certifyToPaltform(String registNo,String riskCode){
    	certifyToPaltformService.certifyToPaltform(registNo, riskCode);
    }
    
    @Override
    public void sendCheckToPlatformCI(String registNo) throws Exception{
    	checkToPlatformService.sendCheckToPlatformCI(registNo);
    }
    
    @Override
    public void sendCheckToPlatformBI(String registNo) throws Exception{
    	checkToPlatformService.sendCheckToPlatformBI(registNo);
    }
    
    @Override
    public void sendCancelToPaltformRe(String registNo,String policyNo,String cancelType){
    	claimToPaltformService.sendCancelToPaltformRe(registNo, policyNo, cancelType);
    }
    
    @Override
    public void reOpenAppToPaltform(String endCaseNo){
    	claimToPaltformService.reOpenAppToPaltform(endCaseNo);
    }
    
    @Override
    public void sendLossToPlatform(String registNo,String riskCode){
    	lossToPlatformService.sendLossToPlatform(registNo, riskCode);
    }
    
    @Override
    public void sendRegistToPlatform(String registNo){
    	registToPaltformService.sendRegistToPlatform(registNo);
    }
    
    @Override
    public void sendSdRiskWarning(String registNo, SysUserVo userVo,Map<String,String> map) {
    	registToCarRiskPaltformService.sendRegistToCarRiskPlatform(registNo, userVo,map);
    }
    @Override
    public void sendClaimToPaltform(CiClaimPlatformLogVo logVo){
    	claimToPaltformService.sendClaimToPaltform(logVo);
    }
    
    @Override
    public void carRegistToFounder(String registNo) throws Exception{
    	founderCustomService.carRegistToFounder(registNo);
    }
    
    @Override
    public void scheduleInfoToFounder(PrpLScheduleTaskVo taskVo,String scheduleType) throws Exception{
    	founderCustomService.scheduleInfoToFounder(taskVo, scheduleType);
    }
    
    @Override
    public void registCancelToFounder(String registNo) throws Exception{
    	founderCustomService.registCancelToFounder(registNo);
    }
    
    @Override
    public void PolicyRelationToFounder(String registNo,String conPolicyNo,String isConnect) throws Exception{
    	founderCustomService.PolicyRelationToFounder(registNo, conPolicyNo, isConnect);
    }
    
//    @Override
//    public void cancelDflossTaskToFounder(PrpLScheduleTaskVo taskVo,Long examineId) throws Exception{
//    	founderCustomService.cancelDflossTaskToFounder(taskVo, examineId);
//    }
    
    @Override
    public void prePayToPayment(PrpLCompensateVo compensateVo) throws Exception{
    	// claimToPaymentDetailService.prePayToPayment(compensateVo);
    	claimToNewPaymentService.prePayToNewPayment(compensateVo);
    }

	@Override
	public void prePayToNewPaymentPingAn(PrpLCompensateVo compensateVo,String registNo) throws Exception{
		// claimToPaymentDetailService.prePayToPayment(compensateVo);
		claimToNewPaymentService.prePayToNewPaymentPingAn(compensateVo,registNo);
	}
	@Override
	public void pingAnPrePayToPayment(PrpLCompensateVo compensateVo,List<PrpLPrePayVo> prePayVos) throws Exception{
		claimToPaymentDetailService.pingAnPrePayToPayment(compensateVo,prePayVos);
	}

    @Override
    public void padPayToPayment(PrpLPadPayMainVo padPayMainVo) throws Exception{
    	// claimToPaymentDetailService.padPayToPayment(padPayMainVo);
		claimToNewPaymentService.padPayToNewPayment(padPayMainVo);
    }

	@Override
	public void padPayToNewPaymentPingAn(PrpLPadPayMainVo padPayMainVo,String registNo) throws Exception{
		// claimToPaymentDetailService.padPayToPayment(padPayMainVo);
		claimToNewPaymentService.padPayToNewPaymentPingAn(padPayMainVo,registNo);
	}

    @Override
    public void compensateToPayment(PrpLCompensateVo compensateVo) throws Exception{
    	// claimToPaymentDetailService.compensateToPayment(compensateVo);
		claimToNewPaymentService.compensateToNewPayment(compensateVo);
    }
	@Override
	public void compensateToPaymentPingAn(PrpLCompensateVo compensateVo,String registNo) throws Exception{
		// claimToPaymentDetailService.compensateToPayment(compensateVo);
		claimToNewPaymentService.compensateToNewPaymentPingAn(compensateVo,registNo);
	}
    @Override
    public void updatePrePayToPayment(PrpLCompensateVo compensateVo) throws Exception{
    	claimToPaymentDetailService.updatePrePayToPayment(compensateVo);
    }
    
    @Override
    public void TransDataForClaimVo(PrpLClaimVo claimVo,
			List<PrpLClaimKindHisVo> kindHisVoList,ClaimInterfaceLogVo claimInterfaceLogVo) throws Exception{
    	claimToReinsuranceService.TransDataForClaimVo(claimVo, kindHisVoList, claimInterfaceLogVo);
    }
    
    @Override
    public void TransDataForCompensateVo(PrpLClaimVo claimVo,PrpLCompensateVo prpLCompensate) throws Exception{
    	claimToReinsuranceService.TransDataForCompensateVo(claimVo, prpLCompensate);
    }
    
    @Override
    public void TransDataForReinsCaseVo(String businessType,
			PrpLClaimVo claimVo,ClaimInterfaceLogVo claimInterfaceLogVo) throws Exception{
    	claimToReinsuranceService.TransDataForReinsCaseVo(businessType, claimVo, claimInterfaceLogVo);
    }
    
    @Override
    public void pushCharge(String compensateNo){
    	Boolean result = claimInvoiceService.pushCharge(compensateNo,null);
    	
    }
	@Override
	public void pingAnPushCharge(PrpLCompensateVo compeVo){
		Boolean result = claimInvoiceService.pingAnPushCharge(compeVo,null);

	}
    @Override
    public void pushPreCharge(String compensateNo){
    	Boolean result = claimInvoiceService.pushPreCharge(compensateNo,null);
    }
	@Override
	public void reassignmentes(PrpLScheduleTaskVo prpLScheduleTaskVo,
			String schType, List<PrpLScheduleDefLossVo> prpLScheduleDefLosses,
			String url) throws Exception {
		scheduleService.reassignmentes(prpLScheduleTaskVo, schType, prpLScheduleDefLosses, url);
		
	}
	
	/*    //理赔调度提交/改派提交接口（理赔请求快赔系统）
	    @Override
	    public void reassignmentes(PrpLScheduleTaskVo prpLScheduleTaskVo,String schType,List<PrpLScheduleDefLossVo> prpLScheduleDefLosses,String url) throws Exception {
	        scheduleService.reassignmentes(prpLScheduleTaskVo,schType,prpLScheduleDefLosses,url);
	    }*/
    @Override
    public void installInsuredrela(PrpLPayCustomVo payCustomVo,SysUserVo userVo) {
        payCustomService.installInsuredrela(payCustomVo,userVo);
    }
    
    @Override
    public void packMsg(PrpLWfTaskVo wfTakVo,String url){
    	sendMsgToMobileService.packMsg(wfTakVo, url);
    }

	@Override
	public void receivecpsresult(String registNo, String status,SysUserVo userVo){
		
		receiveResultService.receivecpsresult(registNo, status, userVo);
	}
	
    @Override
	public void sendCheckSubmitToPlatform( String registNo ) throws Exception{
    	checkToPlatformService.sendCheckSubmitToPlatform(registNo);
    }

    @Override
    public void receiveauditingresult(ReceiveauditingresultVo receiveauditingresultVo){
    	receiveResultService.receiveauditingresult(receiveauditingresultVo);
    }
    
    @Override
    public void receivegusunresult(String registNo,SysUserVo sysUserVo){
    	receiveResultService.receivegusunresult(registNo,sysUserVo);
    }

    @Override
    public void organizationAndSendCTorMTA(PrpLClaimCancelVo pClaimCancelVo,PrpLWfTaskVo prpLWfTaskVo,SysUserVo userVo) throws Exception{
        carchildService.organizationCTorMTA(pClaimCancelVo,prpLWfTaskVo,userVo);
    }
    @Override
    public void sendClaimCancelRestoreCTorMTA(String registNo,SysUserVo userVo) throws Exception {
        carchildService.sendClaimCancelRestoreCTorMTA(registNo,userVo);
    }
    @Override
    public void sendRegistCTorMTA(RegistInformationVo registInformationVo) throws Exception {
        PrpLScheduleTaskVo prpLScheduleTaskVo  = registInformationVo.getPrpLScheduleTaskVo();
        List<PrpLScheduleDefLossVo> prpLScheduleDefLossVoList = prpLScheduleTaskVo.getPrpLScheduleDefLosses();
        List<PrpLCMainVo> prpLCMainVoList = policyViewService.getPolicyAllInfo(prpLScheduleTaskVo.getRegistNo());
        PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(prpLScheduleTaskVo.getRegistNo());
        PrpdIntermMainVo prplIntermMainVo = managerService.findIntermByUserCode(prpLScheduleTaskVo.getScheduledUsercode());
        PrpLConfigValueVo configValueMTACheckVo = ConfigUtil.findConfigByCode(CodeConstants.MTACheck,prpLRegistVo.getComCode());
        PrpLConfigValueVo configValueCTCheckVo = ConfigUtil.findConfigByCode(CodeConstants.CTCheck,prpLRegistVo.getComCode());
        String cTMTACheck = "0";
        if(prplIntermMainVo != null && "0003".equals(prplIntermMainVo.getIntermCode())){
            if(configValueCTCheckVo != null && "1".equals(configValueCTCheckVo.getConfigValue())){
                cTMTACheck = "1";
            }
        }else if(prplIntermMainVo != null && "0005".equals(prplIntermMainVo.getIntermCode())){
            if(configValueMTACheckVo != null && "1".equals(configValueMTACheckVo.getConfigValue())){
                cTMTACheck = "1";
            }
        }
        if(prplIntermMainVo != null && "1".equals(cTMTACheck)){
			// 根据prpLScheduleTaskVoList的id查工作流表
            List<PrpLWfTaskVo> prpLWfTaskVoResult = new ArrayList<PrpLWfTaskVo>();
            for(PrpLScheduleDefLossVo vo : prpLScheduleDefLossVoList){
                List<PrpLWfTaskVo> prpLWfTaskVoList = wfTaskHandleService.findInTaskByOther(vo.getRegistNo(),vo.getId().toString(),"DLoss");
                prpLWfTaskVoResult.addAll(prpLWfTaskVoList);
            }
            carchildService.sendRegistInformation(prplIntermMainVo,prpLRegistVo,prpLCMainVoList,prpLWfTaskVoResult,registInformationVo);
        }
    }

    @Override
    public void sendRegistToCarRiskPlatform(String registNo,SysUserVo userVo,Map<String,String> map)  {
        registToCarRiskPaltformService.sendRegistToCarRiskPlatform(registNo,userVo,map); 
    }
    @Override
    public void sendRegistToCarRiskPlatformRe(String registNo,String policyNo,SysUserVo userVo) {
        registToCarRiskPaltformService.sendRegistToCarRiskPlatformRe(registNo,policyNo,userVo);
    }
    @Override
    public void carRiskImagesUpdate(String registNo,SysUserVo userVo,String riskCode,String claimSequenceNo) {
        carRiskImagesUpdateService.carRiskImagesUpdate(registNo,userVo,riskCode,claimSequenceNo);
    }
	@Override
	public void sendRegistForGenilex(PrpLRegistVo vo,SysUserVo userVo,PrpLCMainVo prpLCMainVo) {
		PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.GENILEX,"");
        if("1".equals(configValueVo.getConfigValue())){
        	reportInfoService.organizaForReport(vo, userVo, prpLCMainVo);
        }
	}
	@Override
	public void sendCarLossForGenilex(String registNo, String taskId,
			SysUserVo userVo) {
		PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.GENILEX,"");
        if("1".equals(configValueVo.getConfigValue())){
        	claimJxService.sendDlossInfor(registNo,taskId, userVo);
        }
	}

	public void sendDlossRegister(PrpLCMainVo prpLCMainVo,SysUserVo userVo) throws Exception {
		sdpoliceService.sendDlossRegister(prpLCMainVo, userVo);
	}
	@Override
	public void sendCaseCancleRegister(PrpLCMainVo prpLCMainVo, String cancleType,String reason, SysUserVo userVo)throws Exception {		
		sdpoliceCaseService.sendCaseCancleRegister(prpLCMainVo, cancleType, reason, userVo);
	}
	@Override
	public void SendReopenCaseRegister(String endCaseNo, String policyNo,SysUserVo userVo) throws Exception {
		sdpoliceCaseService.sendReopenCaseRegister(endCaseNo, policyNo, userVo);	
	}
    @Override
	public void claimToWarn(String registNo,String policyNo) {
    	claimToWarnService.claimToWarn(registNo,policyNo);
    }
    
    @Override
    public void sendFalseCaseToEWByRegist(String cancelReason, String registNo,String policyNo){
    	claimToWarnService.sendFalseCaseToEWByRegist(cancelReason, registNo,policyNo);
    }
    @Override
    public void checkToWarn(Long checkTaskId,Long flowTaskId,String policyNo){
    	try{
    		checkToWarnService.checkToWarn(checkTaskId, flowTaskId,policyNo);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    
    @Override
    public void sendFalseCaseToEWByCancel(String handleIdKey){
    	claimToWarnService.sendFalseCaseToEWByCancel(handleIdKey); 
    }
	@Override
	public void sendRegistInfoToSZJB(String registNo) {
		szpoliceRegistService.sendRegistInfoToSZJB(registNo);
	}

	@Override
	public void sendClaimResultToSelfClaim(String registNo, SysUserVo userVo,String status, String nodeFlag,String policyNo) {
		// TODO Auto-generated method stub
		selfHelpClaimResultService.sendSelfHelpClaimResult(registNo, userVo, status, nodeFlag,policyNo);
	}
    
	@Override
	public JyResVo sendTaskInfoService(String registNo, String dmgVhclId,
			String operationLink, String operationResults,String subNodeCode, SysUserVo userVo) {
		return jyTaskService.sendTaskInfoService(registNo, dmgVhclId, operationLink, operationResults,subNodeCode, userVo);
	}
	@Override
	public JyResVo sendCleanDataService(String registNo, String dmgVhclId,
			SysUserVo userVo) {
		return jyCleanDataService.sendCleanDataService(registNo, dmgVhclId, userVo);
	}
	@Override
	public JyResVo sendZeroNoticeService(String registNo, String dmgVhclId,
			SysUserVo userVo) {
		return jyZeroNoticeService.sendZeroNoticeService(registNo, dmgVhclId, userVo);
	}

	@Override
	public String sendRegistDataToJy(String registNo) {
		return prpLRegistToLossService.sendRegistXmlData(registNo);
	}
	
	@Override
	public void reqByRegist(PrpLRegistVo registVo,SysUserVo userVo,String reportType) throws Exception{
		compeInterfaceService.reqByRegist(registVo, userVo, reportType);
	}
	
	@Override
	public void reqByEndCase(PrpLEndCaseVo endCaseVo,SysUserVo userVo){
		compeInterfaceService.reqByEndCase(endCaseVo, userVo);
	}
	
	@Override
	public void reqByCancel(PrpLClaimVo claimVo,SysUserVo userVo){
		compeInterfaceService.reqByCancel(claimVo, userVo);
	}
    @Override
    public void pushPadPay(PrpLPadPayMainVo padPayMainVo){
    	Boolean result = claimInvoiceService.pushPadPay(padPayMainVo,null);
    }

	@Override
	public ResNumRootVo getReqImageNum(SysUserVo user, String role,
			String bussNo, String assessorId, String url, String appName,
			String appCode) {
		List<PrpLAssessorVo> assessorVos = assessorService.findListLAssessorVo(bussNo, user.getUserCode());
		ResNumRootVo resNumRootVo =null;
		if(assessorVos!=null && assessorVos.size()>0){
			resNumRootVo = imagesDownLoadService.getReqImageNum(user, role, bussNo, assessorId, url, appName, appCode);
		}
		
        Integer sumImgs = 0;
		if(resNumRootVo != null && resNumRootVo.getResReturnDataVo() != null
				&& resNumRootVo.getResReturnDataVo().getSumImgs() != null){
			sumImgs = Integer.valueOf(resNumRootVo.getResReturnDataVo().getSumImgs());
		}
		if(assessorVos != null && assessorVos.size() > 0){
			for(PrpLAssessorVo assessorVo : assessorVos){
				assessorVo.setSumImgs(sumImgs);
				assessorService.updatePrpLAssessor(assessorVo);
			}
		}
		return resNumRootVo;
	}

	@Override
	public void getReqCheckUserImageNum(SysUserVo user, String role,String bussNo,String url, String appName,String appCode) {
		List<PrpLAcheckVo> acheckVos=acheckService.findListLAcheckVo(bussNo, user.getUserCode());
		ResNumRootVo resNumRootVo =null;
		if(acheckVos!=null && acheckVos.size()>0){
			resNumRootVo = imagesDownLoadService.getReqImageNum(user, role, bussNo,"", url, appName, appCode);
		}
		
        Integer sumImgs = 0;
		if(resNumRootVo != null && resNumRootVo.getResReturnDataVo() != null
				&& resNumRootVo.getResReturnDataVo().getSumImgs() != null){
			sumImgs = Integer.valueOf(resNumRootVo.getResReturnDataVo().getSumImgs());
		}
		if(acheckVos != null && acheckVos.size() > 0){
			for(PrpLAcheckVo acheckVo : acheckVos){
				acheckVo.setSumImgs(sumImgs);
				acheckService.updatePrpLAcheck(acheckVo);
			}
		}
		
	}

	@Override
	public void claimcarYJAskPriceAdd(Long id, SysUserVo userVo) {
		claimcarYJService.claimcarYJAskPriceAdd(id, userVo);
		
	}

	@Override
	public void SendControlExpert(String registNo,SysUserVo userVo,String licenseNo, String lossCarMainId, String nodeFlag,String url) {
		dlclaimTaskService.SendControlExpert(registNo,userVo,licenseNo, lossCarMainId, nodeFlag,url);
	}
	
	/**
	 * 送再保冲销接口
	 * @param claimVo
	 * @param prpLCompensate
	 * @throws Exception
	 */
	@Override
	public void transDataForWashTransaction(PrpLClaimVo claimVo,
			PrpLCompensateVo prpLCompensate) throws Exception {
		claimToReinsuranceService.transDataForWashTransaction(claimVo, prpLCompensate);
	}

	@Override
		public void middleStageQuerys(String registNo, String node)
				throws Exception {
			claimToMiddleStageOfCaseService.middleStageQuery(registNo, node);

	}

	@Override 
	public void policyInfoRegister(String registNo, String type,SysUserVo userVo) {
		policyInfoRegisterService.policyInfoRegister(registNo, type, userVo);
		
	}

	@Override
	public void sendRealTimeInfoQuery(PrpLWfTaskVo wfTaskVo,SysUserVo userVo) throws Exception {


		// 反欺诈车辆信息
		//平台优化开关
		PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.sendVehicleQuery, userVo.getComCode());
		if (configValueVo != null && "1".equals(configValueVo.getConfigValue())) {
			vehicleInfoQueryService.sendVehicleInfoQuery(wfTaskVo,configValueVo);
		}

		// 反欺诈人员信息查询失败
		//平台优化开关
		PrpLConfigValueVo configValueVo1 = ConfigUtil.findConfigByCode(CodeConstants.sendPersonQuery, userVo.getComCode());
		if (configValueVo1 != null && "1".equals(configValueVo1.getConfigValue())) {
			vehicleInfoQueryService.sendReportPhoneInfoQuery(wfTaskVo.getRegistNo(),configValueVo);
		}

		// 反欺诈报案电话信息查询失败
		//平台优化开关
		PrpLConfigValueVo configValueVo2 = ConfigUtil.findConfigByCode(CodeConstants.sendReportPhoneQuery, userVo.getComCode());
		if (configValueVo2 != null && "1".equals(configValueVo2.getConfigValue())) {
			vehicleInfoQueryService.sendPersonInfoQuery(wfTaskVo.getRegistNo(),configValueVo);
		}

	}
}