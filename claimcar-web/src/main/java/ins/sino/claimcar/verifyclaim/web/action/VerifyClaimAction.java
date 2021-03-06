package ins.sino.claimcar.verifyclaim.web.action;

import freemarker.core.ParseException;
import ins.framework.utils.Beans;
import ins.framework.web.AjaxResult;
import ins.framework.web.bind.annotation.FormModel;
import ins.platform.common.service.facade.CodeTranService;
import ins.platform.common.util.ConfigUtil;
import ins.platform.common.web.util.WebUserUtils;
import ins.platform.saa.service.facade.SaaUserPowerService;
import ins.platform.saa.vo.SaaUserPowerVo;
import ins.platform.utils.DataUtils;
import ins.platform.vo.PrpLConfigValueVo;
import ins.platform.vo.SysCodeDictVo;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.HandlerStatus;
import ins.sino.claimcar.CodeConstants.VClaimType;
import ins.sino.claimcar.CodeConstants.VerifyClaimTask;
import ins.sino.claimcar.certify.service.CertifyPubService;
import ins.sino.claimcar.certify.service.CertifyService;
import ins.sino.claimcar.certify.vo.PrpLCertifyMainVo;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.PrpLCheckCarVo;
import ins.sino.claimcar.check.vo.PrpLCheckDutyVo;
import ins.sino.claimcar.claim.service.ClaimCancelRecoverService;
import ins.sino.claimcar.claim.service.ClaimSummaryService;
import ins.sino.claimcar.claim.service.ClaimTaskService;
import ins.sino.claimcar.claim.service.CompensateHandleService;
import ins.sino.claimcar.claim.service.CompensateHandleServiceIlogService;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.claim.service.InterfaceAsyncService;
import ins.sino.claimcar.claim.vo.PrpLChargeVo;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.claim.vo.PrpLCoinsVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claim.vo.PrpLFxqFavoreeVo;
import ins.sino.claimcar.claim.vo.PrpLLossItemVo;
import ins.sino.claimcar.claim.vo.PrpLLossPersonVo;
import ins.sino.claimcar.claim.vo.PrpLLossPropVo;
import ins.sino.claimcar.claim.vo.PrpLPadPayMainVo;
import ins.sino.claimcar.claim.vo.PrpLPadPayPersonVo;
import ins.sino.claimcar.claim.vo.PrpLPaymentVo;
import ins.sino.claimcar.claim.vo.PrpLPrePayVo;
import ins.sino.claimcar.claim.vo.PrpLcancelTraceVo;
import ins.sino.claimcar.claim.vo.PrplcomcontextVo;
import ins.sino.claimcar.constant.Risk;
import ins.sino.claimcar.endcase.service.EndCasePubService;
import ins.sino.claimcar.endcase.vo.PrpLEndCaseVo;
import ins.sino.claimcar.endcase.vo.PrpLReCaseVo;
import ins.sino.claimcar.endcase.vo.PrpLuwNotionMainVo;
import ins.sino.claimcar.endcase.vo.PrpLuwNotionVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.AssignService;
import ins.sino.claimcar.flow.service.PayCustomService;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpDNodeVo;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.ilog.defloss.vo.LIlogRuleResVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarInfoVo;
import ins.sino.claimcar.lossperson.service.PersTraceDubboService;
import ins.sino.claimcar.lossperson.service.PersTraceService;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersInjuredVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceMainVo;
import ins.sino.claimcar.manager.service.ManagerService;
import ins.sino.claimcar.manager.vo.PrpLPayCustomVo;
import ins.sino.claimcar.middlestagequery.service.ClaimToMiddleStageOfCaseService;
import ins.sino.claimcar.other.service.AssessorService;
import ins.sino.claimcar.other.vo.PrpLAssessorFeeVo;
import ins.sino.claimcar.other.vo.PrpLAssessorMainVo;
import ins.sino.claimcar.pay.service.PadPayService;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLCItemKindVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLClaimDeductVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.rule.service.ClaimRuleApiService;
import ins.sino.claimcar.rule.vo.VerifyClaimRuleVo;
import ins.sino.claimcar.verifyclaim.service.VerifyClaimService;
import ins.sino.claimcar.verifyclaim.vo.SubmitNextVo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sinosoft.insaml.apiclient.FxqAPIPanel;
import com.sinosoft.insaml.apiclient.FxqCrmRiskService;
import com.sinosoft.insaml.povo.vo.BussRiskInfoAuditVo;

@Controller
@RequestMapping("/verifyClaim")
public class VerifyClaimAction {

	private static Logger logger = LoggerFactory.getLogger(VerifyClaimAction.class);
	
	@Autowired
	VerifyClaimService verifyClaimService;
	@Autowired
	PolicyViewService policyViewService;
	@Autowired
	RegistQueryService registQueryService;
	@Autowired
	WfTaskHandleService wfTaskHandleService;
	@Autowired
	CompensateTaskService compeTaskService;
	@Autowired
	ClaimSummaryService claimSummaryService;
	@Autowired
	ClaimTaskService claimTaskService;
	@Autowired
	CheckTaskService checkTaskService;
	@Autowired
	ManagerService managerService;
	@Autowired
	PersTraceDubboService persTraceDubboService;
	@Autowired
	AssignService assignService;
	@Autowired
	ClaimRuleApiService claimRuleApiService;
	@Autowired
	ClaimCancelRecoverService claimCancelRecoverService;
	@Autowired
	CodeTranService codeTranService;
	@Autowired
	private PayCustomService payCustomService;
	@Autowired
	private PersTraceService persTraceServices;
	@Autowired
	private CertifyPubService certifyPubService;
	@Autowired
    InterfaceAsyncService interfaceAsyncService;
    @Autowired
	private AssessorService assessorService;
    @Autowired
	private CompensateHandleService compHandleService;
    @Autowired
    SaaUserPowerService saaUserPowerService;
    @Autowired
    private CertifyService certifyService;
    @Autowired
    CompensateTaskService compensateTaskService;
    @Autowired
    ClaimTaskService claimService;
    @Autowired
    EndCasePubService endCasePubService;
    @Autowired
    CompensateHandleServiceIlogService compensateHandleServiceIlogService;
    @Autowired
    private PadPayService padPayService;
    @Autowired
    CompensateTaskService compesateService;
    @Autowired
    ClaimToMiddleStageOfCaseService claimToMiddleStageOfCaseService;
    
	private String setTaskType(String taskType){
		if((FlowNode.CompeCI.equals(taskType)||FlowNode.CompeWfCI.equals(taskType))){//??????
			taskType = VerifyClaimTask.COMPE_CI;
		}else if((FlowNode.CompeBI.equals(taskType))||FlowNode.CompeWfBI.equals(taskType)){
			taskType = VerifyClaimTask.COMPE_BI;
		}else if((FlowNode.PrePayCI.equals(taskType)||FlowNode.PrePayWfCI.equals(taskType)
				||FlowNode.PrePay.equals(taskType))){
			taskType = VerifyClaimTask.PREPAY_CI;
		}else if((FlowNode.PrePayBI.equals(taskType)||FlowNode.PrePayWfBI.equals(taskType))){
			taskType = VerifyClaimTask.PREPAY_BI;
		}else if(FlowNode.PadPay.equals(taskType)){//??????
			taskType = VerifyClaimTask.PADPAY;
		}else{//???????????????
			taskType = VerifyClaimTask.CANCEL;
		}
		return taskType;
	}
	
	/**
	 * ???????????????
	 * @param flowTaskId
	 * @return
	 * @modified:
	 * ???Luwei(2016???6???2??? ??????5:01:03): <br>
	 */
	@RequestMapping("/verifyClaimEdit.do")
	public ModelAndView verifyClaimEdit(Double flowTaskId){
		ModelAndView modelAndView = new ModelAndView();
		logger.debug("flowTaskId:"+flowTaskId);
 		PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(flowTaskId);
 		if(wfTaskVo == null){
			throw new IllegalArgumentException("???????????????????????????");
		}
 		
 	    Boolean isInitFlag = CodeConstants.HandlerStatus.INIT.equals(wfTaskVo.getHandlerStatus())?true:false;
 		
		String registNo = wfTaskVo.getRegistNo();//
		String taskType = wfTaskVo.getTaskInNode();//????????????
		String compeNo = wfTaskVo.getHandlerIdKey();//????????????
		//String taskInkey = wfTaskVo.getTaskInKey();//taskInkey
		//String node = wfTaskVo.getNodeCode();
		String flag = "2";// ???????????????????????????????????????????????????1-?????????2-??????
		
		
		
		// verifyType-->??????  ???1-??????????????????????????????2-??????????????????????????????3-??????????????????????????????4-??????????????????????????????5-?????????????????????6-???????????????
		//???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
		
		PrpLuwNotionMainVo vo = verifyClaimService.findUwNotionMainByRegistNo(wfTaskVo);
		PrpLClaimVo claimVo = claimTaskService.findClaimVoByClaimNo(wfTaskVo.getClaimNo());
		
		if(!FlowNode.VClaim.equals(taskType.substring(0,6))){//????????????
			taskType = setTaskType(taskType);
		}else{
			if(vo != null){
				taskType = vo.getPolicyType();
			}else{
				taskType = setTaskType(wfTaskVo.getYwTaskType());
			}
		}
		String node = wfTaskVo.getYwTaskType().toString();
		if(FlowNode.CancelAppJuPei.name().equals(node)){
			flag = "1101".equals(claimVo.getRiskCode()) ? "1" : "2";//???????????????????????????
		}else{
			flag = node.substring(node.length()-2,node.length()).equals("BI") ? "2" : "1";
		}
//		if((FlowNode.CompeCI.equals(taskType)||FlowNode.CompeWfCI.equals(taskType))){//??????
//			taskType = VerifyClaimTask.COMPE_CI; flag = "1";
//		}else if((FlowNode.CompeBI.equals(taskType))||FlowNode.CompeWfBI.equals(taskType)){
//			taskType = VerifyClaimTask.COMPE_BI; flag = "2";
//		}else if((FlowNode.PrePayCI.equals(taskType)||FlowNode.PrePayWfCI.equals(taskType)
//				||FlowNode.PrePay.equals(taskType))){
//			taskType = VerifyClaimTask.PREPAY_CI; flag = "1";
//		}else if((FlowNode.PrePayBI.equals(taskType)||FlowNode.PrePayWfBI.equals(taskType))){
//			taskType = VerifyClaimTask.PREPAY_BI; flag = "2";
//		}else if(FlowNode.PadPay.equals(taskType)){//??????
//			taskType = VerifyClaimTask.PADPAY; flag = "1";
//		}else if(FlowNode.VClaim.equals(taskType.substring(0,6))){//????????????
//			if(vo != null){
//				taskType = vo.getPolicyType();
//				String node = wfTaskVo.getYwTaskType().toString();
//				flag = node.substring(node.length()-2,node.length()).equals("BI") ? "2" : "1";
//			}
//		}else{//???????????????
//			taskType = VerifyClaimTask.CANCEL;
//		}
		
		Double ve_amount = 0.0;
		//????????????
		PrpLCMainVo policyInfo = null;
		List<PrpLCMainVo> policyInfos = policyViewService.getPolicyAllInfo(registNo);
		for(PrpLCMainVo policy : policyInfos){
			if("1".equals(flag)&&Risk.DQZ.equals(policy.getRiskCode())){
				policyInfo = policy;//??????
			}
			if("2".equals(flag)&&!Risk.DQZ.equals(policy.getRiskCode())){
				policyInfo = policy;//??????
			}
			if(policyInfo == null && FlowNode.CancelAppJuPei.name().equals(node) && claimVo != null){
				if(claimVo.getRiskCode().equals(policy.getRiskCode())){
					policyInfo = policy;
				}
			}
		}
		
		String nodeText = "";//???????????????????????????
		String nodeTextTitle="";//????????????????????????????????????
		String reportsText="";//?????????????????????????????????????????????
		String reportsTextTitle="";//?????????????????????????????????????????????????????????
		/*//????????????
		prpLDlossPersTraceVoList=persTraceDubboService.findPrpLDlossPersTraceVoListByRegistNo(registNo);
		if(prpLDlossPersTraceVoList!=null&&prpLDlossPersTraceVoList.size()>0){
			prpLDlossPersTraceVo =prpLDlossPersTraceVoList.get(0);
		}*/
		PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
		int riskInfoSize = claimSummaryService.claimNumber(policyInfo.getPolicyNo());
		modelAndView.addObject("riskInfoSize",riskInfoSize);
		
		//?????????????????????
		PrpLDlossCarInfoVo dlossCarInfoVo = verifyClaimService.getDlossCarInfo(registNo);
		modelAndView.addObject("dlossCarInfoVo", dlossCarInfoVo);
		
		//
		PrpLCheckDutyVo dutyVo=checkTaskService.findCheckDuty(registNo,1);
		//dutyVo.getIndemnityDuty();
		modelAndView.addObject("duty", dutyVo);
		
		
		// ?????????
		List<PrpLClaimDeductVo> deductVos = new ArrayList<PrpLClaimDeductVo>();
		List<PrpLClaimDeductVo> claimDeductVoList = registQueryService.findClaimDeductVoByRegistNo(registNo);
		if(claimDeductVoList!=null&&claimDeductVoList.size()>0){
			Map<String,String> deductCondCode = new HashMap<String,String>();
			for(PrpLClaimDeductVo claimDeductVo:claimDeductVoList){
				String code = claimDeductVo.getDeductCondCode();
				boolean reflag = true;
				for(String key:deductCondCode.keySet()){
					if(code.equals(key)){
						reflag = false;
					}
				}
				if(reflag){
					deductVos.add(claimDeductVo);
				}
				// ??????
				deductCondCode.put(code,code);	
			}
		}
		modelAndView.addObject("claimDeductVos", deductVos);

		if(taskType.equals("5")||taskType.equals("1")){//????????????
			//??????
			PrpLCheckCarVo checkCarVo = verifyClaimService.getCheckCarInfo(registNo);
			PrpLPadPayMainVo padPayMainVo = verifyClaimService.getPadPayByCompeNo(registNo,compeNo);
			Long costNum = 0L;
			if(padPayMainVo!=null){
				List<PrpLPadPayPersonVo> padPayPerVos = padPayMainVo.getPrpLPadPayPersons();
				for(PrpLPadPayPersonVo padPayPerVo:padPayPerVos){
					costNum+=padPayPerVo.getCostSum().longValue();
					PrpLDlossPersInjuredVo PersInjured = persTraceDubboService.findPersInjuredByPK(Long.valueOf(padPayPerVo.getPersonName()));
					padPayPerVo.setInjuredName(PersInjured.getPersonName());
				}
				nodeText = padPayMainVo.getRemark();
				reportsText=padPayMainVo.getRescueReport();
				nodeTextTitle="????????????";
				reportsTextTitle="?????????????????????????????????";
				
				modelAndView.addObject("costNum", costNum);
				modelAndView.addObject("padPayPersonVos", padPayPerVos);
			}
			ve_amount = costNum.doubleValue();
			//??????
			modelAndView.addObject("checkCarVo", checkCarVo.getPrpLCheckCarInfo());
			modelAndView.addObject("padPayMainVo", padPayMainVo);
			
		}
		
		
		// ??????
		List<PrpLPrePayVo> prePayPVos = null;
		List<PrpLPrePayVo> prePayFVos = null;
		PrpLCompensateVo compeVo = null;
		if("3".equals(taskType)||"4".equals(taskType)){
			compeVo = compeTaskService.findPrpLCompensateVoByPK(compeNo);
			if(compeVo!=null){
			
			   reportsText=compeVo.getRescueReport();
				nodeTextTitle="????????????";
				reportsTextTitle="?????????????????????????????????";
			}
			prePayPVos = compeTaskService.getPrePayVo(compeNo,"P");
			prePayFVos = compeTaskService.getPrePayVo(compeNo,"F");
			
			// ????????????????????????????????????
			List<PrpLCompensateVo> compensateVos = compeTaskService.findCompListByClaimNo(compeVo.getClaimNo(),"Y");

			// ???????????????
			BigDecimal sumAllAmt = BigDecimal.ZERO;
			if(compensateVos!=null&&compensateVos.size()>0){
				for(PrpLCompensateVo compVo:compensateVos){
					sumAllAmt = sumAllAmt.add(compVo.getSumAmt());
				}
			}
			
			modelAndView.addObject("sumAllAmt",sumAllAmt);
			ve_amount = sumAllAmt.doubleValue();
		}
		// ??????
		//modelAndView.addObject("compeYVo",compeYVo);
		modelAndView.addObject("prePayPVos",prePayPVos);
		modelAndView.addObject("prePayFVos",prePayFVos);
				

		//??????
		if(taskType.equals("1")||taskType.equals("2")){//??????
			compeVo = compeTaskService.findPrpLCompensateVoByPK(compeNo);
			if(compeVo!=null){
				nodeText = compeVo.getRemark();
				nodeTextTitle="????????????";
				modelAndView.addObject("impairmentType",compeVo.getImpairmentType());//????????????
				modelAndView.addObject("fraudType",compeVo.getFraudType());//????????????					
			}
			
			//??????????????????
			List<PrpLLossItemVo> LossItemVos = new ArrayList<PrpLLossItemVo>();
			List<PrpLLossItemVo> LossItemVos_W = new ArrayList<PrpLLossItemVo>();
			List<PrpLLossItemVo> lossItems = compeVo.getPrpLLossItems();
			if(lossItems!=null&&lossItems.size()>0){
				for(PrpLLossItemVo lossItem : lossItems){
					if("4".equals(lossItem.getPayFlag())){//????????????
						LossItemVos_W.add(lossItem);
					}else{
						LossItemVos.add(lossItem);
					}
				}
			}
			modelAndView.addObject("LossItemVos", LossItemVos);
			modelAndView.addObject("LossItemVos_W", LossItemVos_W);//????????????-????????????
			
			
			//??????????????????
			List<PrpLLossPropVo> lossPropVos = compeVo.getPrpLLossProps();
			modelAndView.addObject("lossPropVos", lossPropVos);
			// ??????????????????
			List<PrpLLossPersonVo> lossPersonVos = compeVo.getPrpLLossPersons();
			modelAndView.addObject("lossPersonVos",lossPersonVos);
			//??????????????????????????????
			List<PrpLLossPropVo> LossPropOthVos = new ArrayList<PrpLLossPropVo>();
			if(lossPropVos!=null&&lossPropVos.size()>0){
				for(PrpLLossPropVo lossPropVo : lossPropVos){
					if("9".equals(lossPropVo.getPropType())){
						LossPropOthVos.add(lossPropVo);
					}
				}
			}
			modelAndView.addObject("LossPropOthVos",LossPropOthVos);
			
			
			
			
			//????????????????????????
			List<PrpLChargeVo> prpLCharges = compeVo.getPrpLCharges();
			for(PrpLChargeVo prpLChargeVo:prpLCharges){
				PrpLPayCustomVo payCustomVo = managerService.findPayCustomVoById(prpLChargeVo.getPayeeId());
				// ?????????voCustom??????????????????
				prpLChargeVo.setPayeeName(payCustomVo.getPayeeName());
				prpLChargeVo.setAccountNo(payCustomVo.getAccountNo());
				prpLChargeVo.setBankName(payCustomVo.getBankName());
				prpLChargeVo.setPayeeIdfNo(payCustomVo.getCertifyNo());
			}
			/*//????????????BZ
			//???????????????bz
			List<PrpLChargeVo> prpLChargees = new ArrayList<PrpLChargeVo>();
			for(PrpLChargeVo PrpLChargeVo:prpLCharges){
				if(flag.equals("1")){
					if(PrpLChargeVo.getKindCode().equals("BZ")){
						prpLChargees.add(PrpLChargeVo);
					}
				}else{
					if(!(PrpLChargeVo.getKindCode().equals("BZ"))){
						prpLChargees.add(PrpLChargeVo);
					}
				}
			}*/
			modelAndView.addObject("chargeVos", prpLCharges);
			
			// ?????????????????? 1-??? 0-???
			String caseFlag = compeVo.getCaseType();
			modelAndView.addObject("caseFlag",caseFlag);
			
			ve_amount = compeVo.getSumAmt()==null ? 0.0 : compeVo.getSumAmt().doubleValue();
			
			String prePaidFlag = "0";
			if(compeVo!=null){
				BigDecimal sumPre = DataUtils.NullToZero(compeVo.getSumPreAmt()).add(DataUtils.NullToZero(compeVo.getSumPreFee()));
				if(sumPre.compareTo(BigDecimal.ZERO)==1 ){//??????????????????????????????
					prePaidFlag ="1";
				}
				
			}
			//??????????????????
			Map<String,List<PrpLPrePayVo>> PrePayMap;
			try{
				PrePayMap = compeTaskService.getPrePayMap
						(compeVo.getClaimNo(),compeVo.getRiskCode().substring(0,2),prePaidFlag);
				modelAndView.addObject("prpLPrePayP", PrePayMap.get("prePay"));
				modelAndView.addObject("prpLPrePayF", PrePayMap.get("preFee"));
			}catch(Exception e){
				logger.error("??????????????????????????????",e);
			}
			
			// ??????
			PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.STARFLAG);
			//?????????
			for(PrpLPaymentVo payMentVo:compeVo.getPrpLPayments()){
				PrpLPayCustomVo payCustomVo = managerService.findPayCustomVoById(payMentVo.getPayeeId());
				// ?????????voCustom??????????????????
				payMentVo.setPayeeName(payCustomVo.getPayeeName());
				if(configValueVo!=null && "1".equals(configValueVo.getConfigValue())){
				payMentVo.setAccountNo(DataUtils.replacePrivacy(payCustomVo.getAccountNo()));
				}else{
					payMentVo.setAccountNo(payCustomVo.getAccountNo());
				}
				payMentVo.setBankName(payCustomVo.getBankOutlets());
			}
		}
		
		
		
		if(taskType.equals("6")){
			if(wfTaskVo!=null){
	              PrpLcancelTraceVo prpLcancelTraceVo =	claimCancelRecoverService.findByClaimNo(wfTaskVo.getClaimNo());
	              modelAndView.addObject("prpLcancelTraceVo",prpLcancelTraceVo); 
	             
	              }

	          		
			 nodeTextTitle="??????/???????????????";//????????????????????????????????????
		}
			// ??????
			PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.STARFLAG);
			if(configValueVo!=null && "1".equals(configValueVo.getConfigValue())){
				//?????????????????????????????????????????????
				registVo.setReportorPhone(DataUtils.replacePrivacy(registVo.getReportorPhone()));
				if(compeVo!=null && compeVo.getPrpLCharges() != null &&  compeVo.getPrpLCharges().size()>0){
					for(int i=0; i< compeVo.getPrpLCharges().size();i++){
						if( compeVo.getPrpLCharges().get(i).getAccountNo() != null){
							compeVo.getPrpLCharges().get(i).setAccountNo(DataUtils.replacePrivacy( compeVo.getPrpLCharges().get(i).getAccountNo()));
							compeVo.getPrpLCharges().get(i).setPayeeIdfNo(DataUtils.replacePrivacy(compeVo.getPrpLCharges().get(i).getPayeeIdfNo()));
						}
					}
				}
			}
			
			
		   
		modelAndView.addObject("compeVo", compeVo);
		
		//?????????????????????
		List<PrpLCItemKindVo> claimKindMList = compeTaskService.getDeductOffKindList(registNo);
		modelAndView.addObject("claimKindMList", claimKindMList);
		
		
		// ?????????????????? 1-??? 0-???
		//String subRogationFlag = "0";
		
		//????????????
		//List<PrpLuwNotionVo> uwNotionVos = verifyClaimService.findUwNotion(registNo,compeNo,taskType);
		//PrpLuwNotionMainVo uwNotionMainVo = verifyClaimService.findUwNotion(registNo,compeNo,taskType);
		
		wfTaskVo.setRiskCode(policyInfo.getRiskCode());
		//????????????????????????
	    String isMinorInjuryCases="";
	    	List<PrpLDlossPersTraceMainVo> traceMains=persTraceServices.findPersTraceMainVo(registNo);
	    	if(traceMains!=null && traceMains.size()>0){
	    		for(PrpLDlossPersTraceMainVo traceMainVo:traceMains){
	    			if(!"1101".equals(wfTaskVo.getRiskCode())){
	    				isMinorInjuryCases=traceMainVo.getIsMinorInjuryCases();
	    			}else if("1101".equals(wfTaskVo.getRiskCode())){
	    				isMinorInjuryCases=traceMainVo.getIsMinorInjuryCases();
	    			}else{
	    				
	    			}
	    		}
	    	}
	    	
	  if(StringUtils.isBlank(isMinorInjuryCases)){
		  isMinorInjuryCases="0";
	  }
	//???????????????????????????????????????
	String assessSign="0";
	List<PrpLAssessorFeeVo> listFeeVo=assessorService.findPrpLAssessorFeeVoByRegistNo(registNo);
	if(listFeeVo!=null && listFeeVo.size()>0){
		for(PrpLAssessorFeeVo vo1 :listFeeVo){
			PrpLAssessorMainVo assessMainVo=assessorService.findAssessorMainVoById(vo1.getAssessMainId());
			if(assessMainVo!=null && "3".equals(assessMainVo.getUnderWriteFlag())){
				assessSign="1";
				break;
			}
		}
	}
	//?????????????????????????????????????????????????????????0-????????????1-??????
	String fxqSignShow="0";
	String safeInfo="";
	if(StringUtils.isNotBlank(compeNo)){
		if(!compeNo.startsWith("Y") && !compeNo.startsWith("D") && ("CompeBI".equals(wfTaskVo.getYwTaskType()) || "CompeCI".equals(wfTaskVo.getYwTaskType()))){
			fxqSignShow="1";
			safeInfo=fxInfoShow(compeNo);
		}
	}
	
	    modelAndView.addObject("safeInfo",safeInfo);
	    modelAndView.addObject("fxqSignShow",fxqSignShow);
	    modelAndView.addObject("assessSign",assessSign);
	    modelAndView.addObject("isMinorInjuryCases",isMinorInjuryCases);
		modelAndView.addObject("policyInfo", policyInfo);
		modelAndView.addObject("registVo", registVo);
		modelAndView.addObject("flag", flag);
		modelAndView.addObject("compensateNo", compeNo);
		modelAndView.addObject("policyType", taskType);
		
		String taskTypeName = FlowNode.valueOf(wfTaskVo.getYwTaskType()).getName();
		modelAndView.addObject("taskTypeName", taskTypeName);
	
		modelAndView.addObject("uwNotionMainVo", vo);
		String recoveryRadio = compeVo!=null ? compeVo.getRecoveryFlag() : "";
		modelAndView.addObject("recoveryRadio", vo==null?recoveryRadio:vo.getRecoveries());
		modelAndView.addObject("ve_amount", ve_amount);
		
		
		// ????????????????????????
		if(CodeConstants.HandlerStatus.INIT.equals(wfTaskVo.getHandlerStatus())){
			String userCode = WebUserUtils.getUserCode();
			String comCode = WebUserUtils.getComCode();
			wfTaskVo = wfTaskHandleService.tempSaveTask(flowTaskId,compeNo,userCode,comCode);
		}
		
		if(StringUtils.isEmpty(wfTaskVo.getClaimNo()) && compeVo != null){
			wfTaskVo.setClaimNo(compeVo.getClaimNo());
		}
		PrpLCertifyMainVo certifyMainVo = certifyPubService.findPrpLCertifyMainVoByRegistNo(registNo);
		String isRefuse = "0"; //????????????
		String isFraud = "0";
		if (certifyMainVo != null) {
			if ("1".equals(flag))
				isRefuse = certifyMainVo.getIsJQFraud();
			if ("2".equals(flag))
				isRefuse = certifyMainVo.getIsSYFraud();
			isFraud = certifyMainVo.getIsFraud();
		}
		//????????????????????????????????????
		PrplcomcontextVo prplcomcontextCVo=compHandleService.findPrplcomcontextByCompensateNo(compeNo,"C");
		if(prplcomcontextCVo!=null){
			modelAndView.addObject("prplcomContextCVo",prplcomcontextCVo);
		}else{
			prplcomcontextCVo=new PrplcomcontextVo();
			modelAndView.addObject("prplcomContextCVo",prplcomcontextCVo);
		}
		//????????????????????????????????????
		PrplcomcontextVo prplcomcontextHVo=compHandleService.findPrplcomcontextByCompensateNo(compeNo,"H");
		if(prplcomcontextHVo!=null){
			modelAndView.addObject("prplcomContextVo",prplcomcontextHVo);
		}else{
			prplcomcontextHVo=new PrplcomcontextVo();
			modelAndView.addObject("prplcomContextVo",prplcomcontextHVo);
		}
		
		 //??????????????????????????????????????????????????????????????????
		//????????????????????????????????????????????????????????????????????????
		String reOpenFlag="0";//0-????????????1-?????????
		String pisderoAmout="0";//??????????????????
		String cisderoAmout="0";//??????????????????
		String inSideDeroFlag="0";//???????????????????????????0--??????1--???
		String inSideDeroPersonFlag="0";//???????????????????????????0--??????1--???
		String personPisderoVerifyAmout="0";//??????????????????(??????)
		String carCisderoVerifyAmout="0";//??????????????????(??????)
		String risk="1101";
		if(compeVo!=null && StringUtils.isNotBlank(compeVo.getRiskCode())){
				risk=compeVo.getRiskCode();
		}
		PrpLWfTaskVo taskVo2=wfTaskHandleService.findOutWfTaskVo(FlowNode.ReOpen.name(),registNo,risk);
		if(taskVo2!=null){
			if(wfTaskVo.getTaskInTime().getTime()>=taskVo2.getTaskOutTime().getTime()){
				if(!"1".equals(isRefuse)){//????????????
					reOpenFlag="1";
				}
				
			}
		}
		
		//??????????????????????????????????????????????????????
		if(StringUtils.isNotBlank(compeNo) && compeNo.startsWith("7")){
			PrpLCompensateVo compenVo=compeTaskService.findCompByPK(compeNo);
			if(compenVo.getPrpLCompensateExt()!=null && StringUtils.isNotBlank(compenVo.getPrpLCompensateExt().getOppoCompensateNo())){
				PrpLCompensateVo compenLinkVo=compeTaskService.findCompByPK(compenVo.getPrpLCompensateExt().getOppoCompensateNo());
				if(compenLinkVo.getCreateTime().getTime()<taskVo2.getTaskOutTime().getTime()){
					reOpenFlag="0";
				}
			}
		}
		String verifyClaimCompleteFlag="0";//?????????????????????????????????????????????????????????0,1???????????????1(???????????????)???
		if("0".equals(reOpenFlag)){
			if(StringUtils.isNotBlank(compeNo) && compeNo.startsWith("7")){
				if(compeVo!=null){
					pisderoAmout=DataUtils.NullToZero(compeVo.getPisderoAmout())+"";
					cisderoAmout=DataUtils.NullToZero(compeVo.getCisderoAmout())+"";
					inSideDeroFlag=compeVo.getInSideDeroFlag();
					inSideDeroPersonFlag=compeVo.getInSideDeroPersonFlag();
					carCisderoVerifyAmout = DataUtils.NullToZero(compeVo.getCarCisderoVerifyAmout())+"";
					personPisderoVerifyAmout = DataUtils.NullToZero(compeVo.getPersonPisderoVerifyAmout())+"";
					
					List<PrpLCompensateVo> compeVoList = compensateTaskService.queryCompensate(registNo,"N");
					if(compeVoList != null && compeVoList.size() > 0 ){
						for(PrpLCompensateVo verifyCompeVo : compeVoList){
							if(CodeConstants.UnderWriteFlag.MANAL_UNDERWRITE.equals(verifyCompeVo.getUnderwriteFlag())){
								verifyClaimCompleteFlag="1";
								carCisderoVerifyAmout = DataUtils.NullToZero(verifyCompeVo.getCarCisderoVerifyAmout())+"";
								personPisderoVerifyAmout = DataUtils.NullToZero(verifyCompeVo.getPersonPisderoVerifyAmout())+"";
								break;
							}
						}
					}
				}
			}
		}
	String compenFlag="0";//???????????????0-???????????????????????????1-?????????????????????
	if(StringUtils.isNotBlank(compeNo) && compeNo.startsWith("7")){
		compenFlag="1";
	}
	
	    modelAndView.addObject("inSideDeroFlag",inSideDeroFlag);
	    modelAndView.addObject("inSideDeroPersonFlag",inSideDeroPersonFlag);
	    modelAndView.addObject("verifyClaimCompleteFlag",verifyClaimCompleteFlag);
		modelAndView.addObject("compenFlag",compenFlag);
		modelAndView.addObject("pisderoAmout",pisderoAmout);
		modelAndView.addObject("cisderoAmout",cisderoAmout);
		//?????????????????????
		if(isInitFlag||CodeConstants.HandlerStatus.DOING.equals(wfTaskVo.getHandlerStatus())){
			PrpLWfTaskVo wfTaskVerifyVo = wfTaskHandleService.queryTask(wfTaskVo.getUpperTaskId().doubleValue());
			if(!CodeConstants.UnderWriteFlag.MANAL_UNDERWRITE.equals(verifyClaimCompleteFlag) &&
					FlowNode.Compe.equals(wfTaskVerifyVo.getNodeCode())){
				if(CodeConstants.HandlerStatus.DOING.equals(wfTaskVo.getHandlerStatus())){
					PrpLCompensateVo compenVo=compeTaskService.findCompByPK(compeNo);
					if(compenVo!=null && compenVo.getPersonPisderoVerifyAmout()!=null){
						modelAndView.addObject("personPisderoVerifyAmout", new BigDecimal(personPisderoVerifyAmout));
					}else{
						modelAndView.addObject("personPisderoVerifyAmout", new BigDecimal(pisderoAmout));
					}
					if(compenVo!=null && compenVo.getCarCisderoVerifyAmout()!=null){
						modelAndView.addObject("carCisderoVerifyAmout", new BigDecimal(carCisderoVerifyAmout));
					}else{
						modelAndView.addObject("carCisderoVerifyAmout", new BigDecimal(cisderoAmout));
					}
					
				}else{
					modelAndView.addObject("personPisderoVerifyAmout", new BigDecimal(pisderoAmout));
					modelAndView.addObject("carCisderoVerifyAmout", new BigDecimal(cisderoAmout));
				}
			}else{
				modelAndView.addObject("personPisderoVerifyAmout",new BigDecimal(personPisderoVerifyAmout));
				modelAndView.addObject("carCisderoVerifyAmout",new BigDecimal(carCisderoVerifyAmout));
			}
		}else{
			modelAndView.addObject("personPisderoVerifyAmout",new BigDecimal(personPisderoVerifyAmout));
			modelAndView.addObject("carCisderoVerifyAmout",new BigDecimal(carCisderoVerifyAmout));
		}
		modelAndView.addObject("reOpenFlag",reOpenFlag);
		
		modelAndView.addObject("isFraud",isFraud);
	    modelAndView.addObject("isRefuse",isRefuse); 
		modelAndView.addObject("taskType",taskType);
		modelAndView.addObject("nodeText",nodeText);
		
		modelAndView.addObject("nodeTextTitle", nodeTextTitle);
		modelAndView.addObject("reportsText", reportsText);
		modelAndView.addObject("reportsTextTitle", reportsTextTitle);
		modelAndView.addObject("wfTaskVo", wfTaskVo);//???????????????
		modelAndView.addObject("endorseSize", policyViewService.findPolicyEndorseInfo(registNo).size());
		
		//??????????????????????????????????????????????????????????????????????????????
		String policeInfoFlag="0";//????????????
		String policyComCode = policyViewService.getPolicyComCode(registNo);
		if(policyComCode.startsWith("62")){
			policeInfoFlag = "1";
			//??????????????????
	        String claimSequenceNo = "";
	        if(policyInfos != null && policyInfos.size() > 1){//??????????????????
	            for(PrpLCMainVo lCMainVo : policyInfos){
	                if("12".equals(lCMainVo.getRiskCode().substring(0, 2))){
	                    if(StringUtils.isNotBlank(lCMainVo.getClaimSequenceNo())){
	                        claimSequenceNo = lCMainVo.getClaimSequenceNo();
	                        modelAndView.addObject("claimSequenceNo",claimSequenceNo);
	                    }
	                }
	            }  
	        }else if(policyInfos != null && policyInfos.size() == 1){
	            PrpLCMainVo lCMainVo = policyInfos.get(0);
	            if(StringUtils.isNotBlank(lCMainVo.getClaimSequenceNo())){
	                claimSequenceNo = lCMainVo.getClaimSequenceNo();
	                modelAndView.addObject("claimSequenceNo",claimSequenceNo);
	            }
	        }
	        String carRiskUrl = SpringProperties.getProperty("CARRISK_URL");
	        String carRiskUserName = SpringProperties.getProperty("SDWARN_YMUSER");
            String carRiskPassWord = SpringProperties.getProperty("SDWARN_YMPW");
	        modelAndView.addObject("comparePicURL",carRiskUrl);
	        String claimPeriod = "05";
	        modelAndView.addObject("claimPeriod",claimPeriod);
            modelAndView.addObject("carRiskUserName",carRiskUserName);
            modelAndView.addObject("carRiskPassWord",carRiskPassWord);
		}
		modelAndView.addObject("policeInfoFlag",policeInfoFlag);
		
        //???????????????ILOG????????????????????????
        String nodeCode=wfTaskVo.getSubNodeCode();
        String roleFlag="0";
        SysUserVo userVo = WebUserUtils.getUser();
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
        
		modelAndView.setViewName("verifyClaim/VerifyClaimEdit");
		return modelAndView;
	}
	

	/**
	 * ???????????????????????????????????????????????????ajax??????
	 * @param infoSize
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/addRowInfo.ajax")
	@ResponseBody
	public ModelAndView addRowInfo(String bodyName, String flag)
			throws ParseException {
		ModelAndView mv = new ModelAndView();
		logger.debug(bodyName);
		if (bodyName.equals("PayInfoTbody")) {// ?????????????????????????????????????????????
			// ????????????povo???_Pay???????????????
			// ????????????????????????VO
			// ?????????List???
			// ?????????_Pay???????????????????????????
			mv.setViewName("verifyClaim/VerifyClaimEdit_PayInfos_Pay");
		}
		if (bodyName.equals("PayCustomTbody")) {// ??????????????????????????????????????????
			mv.setViewName("verifyClaim/VerifyClaimEdit_PayInfos_Pers");
		}
		if (bodyName.equals("OtherLossTbody")) {// ?????????????????????????????????????????????
			mv.setViewName("verifyClaim/VerifyClaimEdit_LossTables_OtheLo");
		}
		// mv.addObject("size", size);
		mv.addObject("flag", flag);
		return mv;
	}

	/**
	 * <pre>??????????????????????????????????????????</pre>
	 * @param taskId,uwNotionMainId
	 * @param currentNode,nextNode,action
	 * @modified:
	 * ???Luwei(2016???8???26??? ??????6:20:09): <br>
	 */
	@RequestMapping(value = "/verifyClaimSubmit.do")
	@ResponseBody
	public AjaxResult verifyClaimSubmit(Double taskId,Long uwNotionMainId,String currentNode,
	                                    String nextNode,String action,String compeWfZeroFlag,String payIds,String handle,Double amount,String verifyText,String handleText,
	                                    BigDecimal personPisderoVerifyAmout,BigDecimal carCisderoVerifyAmout,String compensateNo) {
		Long currentDate = System.currentTimeMillis();
		AjaxResult ajaxResult = new AjaxResult();
		SysUserVo userVo = WebUserUtils.getUser();
		String comCode = WebUserUtils.getComCode();
		String userCode = WebUserUtils.getUserCode();
		PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(taskId.doubleValue());
		PrpLRegistVo prpLRegistVo=null;
		if(wfTaskVo!=null){
		   prpLRegistVo= registQueryService.findByRegistNo(wfTaskVo.getRegistNo());
		}
		PrpLCompensateVo prpLCompensateVo = compesateService.queryCompByPK(compensateNo);
		if(prpLCompensateVo != null){
			prpLCompensateVo.setPersonPisderoVerifyAmout(personPisderoVerifyAmout);
			prpLCompensateVo.setCarCisderoVerifyAmout(carCisderoVerifyAmout);
			compHandleService.updatePrpLCompensate(prpLCompensateVo);
		}
		boolean haveUser = true;
		String policyNo="";
		try {// ???????????????
			if (VClaimType.VC_AUDIT.equals(action)) {
				// ??????????????????????????????????????????????????????
				if(prpLCompensateVo!=null){
					existUserByNextNode(ajaxResult,wfTaskVo,nextNode,haveUser,prpLCompensateVo.getComCode());
				}else{
					if(prpLRegistVo!=null){
						existUserByNextNode(ajaxResult,wfTaskVo,nextNode,haveUser,prpLRegistVo.getComCode());
					}
				}
				
			}
			if (haveUser) {
				if (wfTaskVo != null&& HandlerStatus.DOING.equals(wfTaskVo.getHandlerStatus())) {
					//????????????
					logger.info("registNo: "+wfTaskVo.getRegistNo()+" ,??????????????????...??????: "+( System.currentTimeMillis()-currentDate )+"??????");
					List<PrpLWfTaskVo> wfTaskVoList = verifyClaimService.verifyClaimSubmit
							(wfTaskVo, uwNotionMainId,currentNode, nextNode, action, userVo,compeWfZeroFlag);
					logger.info("registNo: "+wfTaskVo.getRegistNo()+" ,??????????????????...??????: "+( System.currentTimeMillis()-currentDate )+"??????");
					//????????????
					verifyClaimService.verifyToEndcasSubmit
					(wfTaskVo, uwNotionMainId,currentNode, nextNode, action, userVo,compeWfZeroFlag);
					logger.info("registNo: "+wfTaskVo.getRegistNo()+" ,??????????????????...??????: "+( System.currentTimeMillis()-currentDate )+"??????");
					
					//??????????????????????????????rabbitMq???????????????????????????
					claimToMiddleStageOfCaseService.middleStageQuery(wfTaskVo.getRegistNo(), "EndCas");

					
					//????????????????????????yzy
					PrpLuwNotionMainVo mainVo=verifyClaimService.finduwNotionByPK(uwNotionMainId);
					if(mainVo!=null){
						policyNo=mainVo.getPolicyNo();
						mainVo.setRemark(handleText);
						PrpLuwNotionVo uwNotionVo = new PrpLuwNotionVo();
						uwNotionVo.setPrpLuwNotionMain(mainVo);
						uwNotionVo.setHandle(handle);
						uwNotionVo.setHandleText(handleText);
						uwNotionVo.setVerifyText(verifyText);
						if(amount != null){
							uwNotionVo.setAmount(new BigDecimal(amount));
						}else{
							uwNotionVo.setAmount(BigDecimal.ZERO);
						}
						uwNotionVo.setAuditor(userCode);
						uwNotionVo.setComCode(comCode);
						uwNotionVo.setPubTime(new Date());
						uwNotionVo.setValidFlag(CodeConstants.ValidFlag.VALID);
						List<PrpLuwNotionVo> listNotionVos=mainVo.getPrpLuwNotions();
						if(listNotionVos!=null && listNotionVos.size()>0){
							listNotionVos.add(uwNotionVo);
						}else{
							listNotionVos=new ArrayList<PrpLuwNotionVo>();
							listNotionVos.add(uwNotionVo);
						}
						mainVo.setPrpLuwNotions(listNotionVos);
						verifyClaimService.updatePrpLuwNotionMainVo(mainVo);
					}
					ajaxResult.setStatus(HttpStatus.SC_OK);
					//??????prpLpaycustom??????status??????
					if(VClaimType.VC_ADOPT.equals(action)){
					   List<Long> payList=new ArrayList<Long>();
					      if(StringUtils.isNotBlank(payIds)){
						     String [] pays=payIds.split(",");
						   for(int i=0;i<pays.length;i++){
						       payList.add(Long.valueOf(pays[i]));
							}
						payCustomService.setStatus(payList);
						"".equals("1");
					  }
					}
					
					if (!VClaimType.VC_ADOPT.equals(action)) {
						ajaxResult.setData(wfTaskVoList.get(0).getTaskId());
						if(nextNode.equals("Certi")){
						    Map<String,Object> datas = new HashMap<String,Object>();
						    datas.put("CertiNodeCode","Certi");
						    datas.put("assignUser",wfTaskVoList.get(0).getAssignUser());
						    datas.put("taskInUser",wfTaskVoList.get(0).getTaskInUser());
						    ajaxResult.setDatas(datas);
						}
					} else {
						//??????????????????????????????
						verifyClaimService.sendCompensateToPayment(uwNotionMainId);
						
						//?????????????????????????????????????????????????????????????????????
						verifyClaimService.sendVatBackSumAmountNTToReins(wfTaskVo.getRegistNo(),wfTaskVo.getRiskCode());
						
					}
				} else {
					throw new IllegalArgumentException("??????????????????????????????" + wfTaskVo.getHandlerUser() + ",????????????" 
							+ "-->??????????????????????????????????????????");
				}
			}
			
		} catch (Exception e) {
			logger.error("registNo: "+wfTaskVo.getRegistNo()+" ,verifyClaimSubmit error: ",e);
			ajaxResult.setStatusText("???????????????????????????" + e);
			ajaxResult.setStatus(HttpStatus.SC_EXPECTATION_FAILED);
		}
		
		PrpLRegistVo registVo = registQueryService.findByRegistNo(wfTaskVo.getRegistNo());
		try{
			// ??????????????????????????????????????????--CancelAppJuPei
			if(registVo!=null&&"1".equals(registVo.getIsQuickCase())){
				if(wfTaskVo!=null&&"CancelAppJuPei".equals(wfTaskVo.getTaskInNode())){
					interfaceAsyncService.receivecpsresult(wfTaskVo.getRegistNo(),"4",userVo);
				}
			}
		}catch(Exception e){
			logger.error("interfaceAsyncService.receivecpsresult error: ",e);
		}
			
		// ??????????????????????????????????????????--CancelAppJuPei
		if(registVo!=null&&"1".equals(registVo.getSelfClaimFlag())){
			if(wfTaskVo!=null&&"CancelAppJuPei".equals(wfTaskVo.getTaskInNode())){
				interfaceAsyncService.sendClaimResultToSelfClaim(wfTaskVo.getRegistNo(),userVo,"4","3",policyNo);
			}
		}
		return ajaxResult;
	}
	
	//??????????????????????????????????????????????????????
	private void existUserByNextNode(AjaxResult ajaxResult,PrpLWfTaskVo wfTaskVo,
	    String nextNode,boolean haveUser,String comCode){
		int nextLevel = Integer.valueOf(nextNode.split("_LV")[1]);
		if (nextLevel < 9) {
			VerifyClaimRuleVo ruleVo = getVerifyMaxLevel(wfTaskVo,null);
			int maxLevel = ruleVo.getMaxLevel();// ??????????????????
			haveUser = assignService.existsGradeUser(FlowNode.valueOf(nextNode), comCode);
			if (!haveUser) {
				String msg = nextLevel == maxLevel ? "??????????????????????????????" : "??????????????????????????????????????????";
				ajaxResult.setStatusText(FlowNode.valueOf(nextNode).getName() + msg);
				ajaxResult.setStatus(HttpStatus.SC_EXPECTATION_FAILED);
			}
		}
	}
	
	/**
	 * ????????????????????????
	 * @param uwNotionVo
	 * @modified:???Luwei(2016???5???31??? ??????5:35:42): <br>
	 */
	@RequestMapping(value = "/saveVerifyClaim.do")  
	@ResponseBody
	public AjaxResult saveVerifyClaim(@FormModel("uwNotionVo") PrpLuwNotionVo uwNotionVo,
	                                  @FormModel("uwNotionMainVo") PrpLuwNotionMainVo uwNotionMainVo,
	                                  @FormModel("prplcomContextVo") PrplcomcontextVo prplcomcontextVo){
		AjaxResult ajaxResult = new AjaxResult();
		SysUserVo userVo = WebUserUtils.getUser();
        try{
            String taskType = uwNotionMainVo.getPolicyType();
    		PrpLCompensateVo compeVo = null;
    		//???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????= ??????+??????+??????????????????????????????
    	    if(!VClaimType.VC_RETURN.equals(uwNotionVo.getHandle()) && (VerifyClaimTask.COMPE_CI.equals(taskType)|| VerifyClaimTask.COMPE_BI.equals(taskType))){
    	    	compeVo = compensateTaskService.findPrpLCompensateVoByPK(prplcomcontextVo.getCompensateNo());
    	    	/*String accountIsSame = compensateTaskService.validAccountIsSame(compeVo,userVo);
    	    	if(accountIsSame!= null){
    	    		throw new IllegalArgumentException("???????????????????????????" + accountIsSame);   
    	    	}*/
    	        if(compensateTaskService.validateAmoutIsSame(compeVo) != null){
    	        	throw new IllegalArgumentException("???????????????????????????"+compensateTaskService.validateAmoutIsSame(compeVo));
    	        }
    	    }
    	    //?????????????????????????????????????????????
    	    if(uwNotionMainVo!=null && (uwNotionMainVo.getPisderoAmout()!=null || uwNotionMainVo.getCisderoAmout()!=null)){
    	    	PrpLCompensateVo prpLCompensateVo = compesateService.queryCompByPK(prplcomcontextVo.getCompensateNo());
        		if(prpLCompensateVo != null){
        			if(uwNotionMainVo.getPisderoAmout()!=null){
        				prpLCompensateVo.setPersonPisderoVerifyAmout(uwNotionMainVo.getPisderoAmout());
        			}
        			if(uwNotionMainVo.getCisderoAmout()!=null){
        				prpLCompensateVo.setCarCisderoVerifyAmout(uwNotionMainVo.getCisderoAmout());
        			}
        			
        			compHandleService.updatePrpLCompensate(prpLCompensateVo);
        		}
    	    }
    	    
    		
        } catch(Exception e){
        	logger.error("???????????????????????????",e);
			ajaxResult.setStatus(HttpStatus.SC_EXPECTATION_FAILED);
			ajaxResult.setStatusText(e.getMessage());
			return ajaxResult;
        }
		String comCode = WebUserUtils.getComCode();
		String userCode = WebUserUtils.getUserCode();
		
		// ??????????????????
		if(uwNotionMainVo.getTaskId() == null){
			throw new IllegalArgumentException("???????????????????????????");
		}
		Double flowTaskId = uwNotionMainVo.getTaskId().doubleValue();
		PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(flowTaskId);
		Long uwNotionMainId = verifyClaimService.saveVerifyClaim
				(uwNotionMainVo,uwNotionVo,wfTaskVo,comCode,userCode,"1");
		if(wfTaskVo!=null){
			//??????????????????????????????
			if(prplcomcontextVo!=null && prplcomcontextVo.getId()!=null){
				
			}else{
				prplcomcontextVo.setCompensateNo(wfTaskVo.getCompensateNo());
				prplcomcontextVo.setCreateTime(new Date());
				prplcomcontextVo.setCreateUser(userCode);
				prplcomcontextVo.setNodeSign("H");
			
			}
			//??????????????????????????????
		if(wfTaskVo!=null && StringUtils.isNotBlank(wfTaskVo.getCompensateNo())&& !wfTaskVo.getCompensateNo().startsWith("Y") && !wfTaskVo.getCompensateNo().startsWith("D") && ("CompeBI".equals(wfTaskVo.getYwTaskType()) || "CompeCI".equals(wfTaskVo.getYwTaskType()))){
			 if(prplcomcontextVo!=null && prplcomcontextVo.getId()!=null){
				PrplcomcontextVo prplcomcontextVo1=compHandleService.findPrplcomcontextById(prplcomcontextVo.getId());
				Beans.copy().excludeNull().from(prplcomcontextVo).to(prplcomcontextVo1);
				try {
					compHandleService.saveOrUpdatePrplcomcontext(prplcomcontextVo1);
				} catch (Exception e) {
					logger.error("??????????????????",e);
				}
			}else{
				prplcomcontextVo.setCompensateNo(wfTaskVo.getCompensateNo());
				prplcomcontextVo.setCreateTime(new Date());
				prplcomcontextVo.setCreateUser(userCode);
				prplcomcontextVo.setNodeSign("H");
				try {
					compHandleService.saveOrUpdatePrplcomcontext(prplcomcontextVo);
				} catch (Exception e) {
					logger.error("??????????????????",e);
				}
			
			}
		}
			
		}
		
		ajaxResult.setStatus(HttpStatus.SC_OK);
		ajaxResult.setData(uwNotionMainId);
		return ajaxResult;
	}
	
	private List<SysCodeDictVo> setAuditNextNode(String currentNode,int maxLevel){
		List<SysCodeDictVo> dataSourceList = new ArrayList<SysCodeDictVo>();
		String[] nodeName = currentNode.split("_LV");
		String level = nodeName[1];
		int currentLevel = Integer.parseInt(level);
		if(currentLevel >= maxLevel && currentLevel<9){
			FlowNode nextNode = FlowNode.valueOf(nodeName[0]+"_LV9");
			SysCodeDictVo dictVo = new SysCodeDictVo();
			dictVo.setCodeCode(nextNode.name());
			dictVo.setCodeName(nextNode.getName());
			dataSourceList.add(dictVo);
		}else if(currentLevel>=1&&currentLevel<maxLevel){
			for(int i = maxLevel; currentLevel<i; currentLevel++ ){
				FlowNode nextNode = FlowNode.valueOf(nodeName[0]+"_LV"+( currentLevel+1 ));
					SysCodeDictVo dictVo = new SysCodeDictVo();
					dictVo.setCodeCode(nextNode.name());
					dictVo.setCodeName(nextNode.getName());
					dataSourceList.add(dictVo);
				// nextNodeMap.put(nextNode.name(),nextNode.getName());
			}
		}else if(currentLevel>=9&&currentLevel<12){// ???????????????????????????
			int nextLevel = currentLevel+1;
			FlowNode nextNode = FlowNode.valueOf(nodeName[0]+"_LV"+nextLevel);
			SysCodeDictVo dictVo = new SysCodeDictVo();
			dictVo.setCodeCode(nextNode.name());
			dictVo.setCodeName(nextNode.getName());
			dataSourceList.add(dictVo);
		}
		return dataSourceList;
	}
	
	private List<SysCodeDictVo> setReturnNextNode(String currentNode,int maxLevel,PrpLuwNotionMainVo uwNotionMainVo,Double taskId,PrpLWfTaskVo wfTaskVo){
		List<SysCodeDictVo> dataSourceList = new ArrayList<SysCodeDictVo>();
		
		String taskInNode = uwNotionMainVo.getTaskInNode();
		PrpLClaimVo claimVo = claimTaskService.findClaimVoByClaimNo(uwNotionMainVo.getClaimNo());
		String riskCode = claimVo.getRiskCode();
		String node = Risk.DQZ.equals(riskCode) ? "VClaim_CI_LV" : "VClaim_BI_LV";
		
		String[] nodeName = currentNode.split("_LV");
		int currentLevel = Integer.parseInt(nodeName[1]);
		if(currentLevel>=1&&currentLevel<=maxLevel){
			List<PrpDNodeVo> nodeList = wfTaskHandleService.findLowerNode(taskId,currentNode,nodeName[0]);
			for(PrpDNodeVo nodeVo:nodeList){
				SysCodeDictVo dictVo = new SysCodeDictVo();
				dictVo.setCodeCode(nodeVo.getNodeCode().equals("VClaim") ? node+"1" : nodeVo.getNodeCode());
				dictVo.setCodeName(nodeVo.getNodeName().equals("??????") ? "??????_??????" : nodeVo.getNodeName());
				dataSourceList.add(dictVo);
			}
			//??????????????????ilog---start============
			/*SysCodeDictVo dictVo1 = new SysCodeDictVo();
			dictVo1.setCodeCode(FlowNode.valueOf(taskInNode).toString());
			dictVo1.setCodeName(FlowNode.valueOf(taskInNode).getName());
			dataSourceList.add(dictVo1);*/
            SysCodeDictVo dictVo2 = setReturnNextNodeByIlog(wfTaskVo,taskInNode);
            if(dictVo2 != null){
                dataSourceList.add(dictVo2);
            }
            //ilog---end============
		}else if(currentLevel>9&&currentLevel<=12){// ???????????????????????????
			int returnLevel = (currentLevel-1);
			String returnNode = node+returnLevel+"";
			SysCodeDictVo dictVo1 = new SysCodeDictVo();
			dictVo1.setCodeCode(FlowNode.valueOf(returnNode).toString());
			dictVo1.setCodeName(FlowNode.valueOf(returnNode).getName());
			dataSourceList.add(dictVo1);
		}else if(currentLevel==9){
			List<PrpDNodeVo> nodeList = wfTaskHandleService.findLowerNode(taskId,currentNode,nodeName[0]);
			SysCodeDictVo dictVo1 = new SysCodeDictVo();
			dictVo1.setCodeCode(nodeList.get(0).getNodeCode());
			dictVo1.setCodeName(nodeList.get(0).getNodeName());
			if(nodeList.size() > 1){
				for(int i=1;i<nodeList.size();i++){
					if(nodeList.get(i).getNodeCode().startsWith("VClaim_")){
						int Level_1 = Integer.parseInt(dictVo1.getCodeCode().split("_LV")[1]);
						int Level_2 = Integer.parseInt(nodeList.get(i).getNodeCode().split("_LV")[1]);
						if(Level_1 < Level_2){
							dictVo1.setCodeCode(nodeList.get(i).getNodeCode());
							dictVo1.setCodeName(nodeList.get(i).getNodeName());
						}
					}
				}
			}
			dataSourceList.add(dictVo1);
		}else{
		    //ilog---start============
			/*SysCodeDictVo dictVo1 = new SysCodeDictVo();
			dictVo1.setCodeCode(FlowNode.valueOf(taskInNode).toString());
			dictVo1.setCodeName(FlowNode.valueOf(taskInNode).getName());
			dataSourceList.add(dictVo1);*/
            SysCodeDictVo dictVo2 = setReturnNextNodeByIlog(wfTaskVo,taskInNode);
            if(dictVo2 != null){
                dataSourceList.add(dictVo2);
            }
            //ilog---end============
		}
		return dataSourceList;
	}
	
	/**
	 * ?????????????????????,???????????????????????????
	 * @param taskId
	 * @param action
	 * @return
	 * @modified:
	 * ???Luwei(2016???5???23??? ??????11:28:43): <br>
	 */
	@RequestMapping("/loadSubmitPage.ajax")
	public ModelAndView loadSubmitAudit(Double taskId,String action,Long uwNotionMainId,String cisderoAmout,String fraudType,String impairmentType){
		ModelAndView mav = new ModelAndView();
		PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(taskId);
		PrpLuwNotionMainVo uwNotionMainVo = verifyClaimService.finduwNotionByPK(uwNotionMainId);
		String taskInNode = uwNotionMainVo.getTaskInNode();
		
		SubmitNextVo nextVo = new SubmitNextVo();
		List<SysCodeDictVo> dataSourceList = new ArrayList<SysCodeDictVo>();
		
		String currentNode = wfTaskVo.getSubNodeCode();
		currentNode = "VClaim".equals(currentNode) ? "VClaim_CI_LV1" : currentNode;
		
		VerifyClaimRuleVo ruleVo = getVerifyMaxLevel(wfTaskVo,action);//?????????
		int maxLevel = ruleVo.getMaxLevel();//?????????????????????
		int backLevel = ruleVo.getBackLevel();//?????????????????????
		
		String currentName = FlowNode.valueOf(currentNode).getName();
		if(VClaimType.VC_AUDIT.equals(action)){//????????????
			
			dataSourceList = setAuditNextNode(currentNode,maxLevel);
			String handlerIdKey = wfTaskVo.getHandlerIdKey();
			if(StringUtils.isNotBlank(handlerIdKey)&&handlerIdKey.startsWith("7")){
				PrpLCompensateVo compensateVo = compeTaskService.findCompByPK(handlerIdKey);
				if(compensateVo!=null){
					//?????????????????????????????????????????????
					if(StringUtils.isNotBlank(fraudType)){
					    compensateVo.setFraudType(fraudType);
					} else {
						compensateVo.setFraudType(null);
					}
					if(StringUtils.isNotBlank(impairmentType)){	
						compensateVo.setImpairmentType(impairmentType);
					} else {
						compensateVo.setImpairmentType(null);
					}
					compHandleService.updatePrpLCompensate(compensateVo);
				}
				
			}
			
			
			mav.addObject("dataSourceList",dataSourceList);
			mav.setViewName("verifyClaim/VerifyClaimEdit_initSubmit");
			
		}else if(VClaimType.VC_RETURN.equals(action)){//??????
			
			dataSourceList = setReturnNextNode(currentNode,maxLevel,uwNotionMainVo,taskId,wfTaskVo);
			//???????????????????????????????????????
			/*String handlerIdKey = wfTaskVo.getHandlerIdKey();
			PrpLCompensateVo compensateVo = compeTaskService.findCompByPK(handlerIdKey);
			if(compensateVo!=null){
				fxqRiskAuditInfo(compensateVo.getCompensateNo(),compensateVo.getPolicyNo(),WebUserUtils.getUser());
			}*/
			String handlerIdKey = wfTaskVo.getHandlerIdKey();
			if(StringUtils.isNotBlank(handlerIdKey)&&handlerIdKey.startsWith("7")){
				PrpLCompensateVo compensateVo = compeTaskService.findCompByPK(handlerIdKey);
				if(compensateVo!=null){
					//?????????????????????????????????????????????
					if(StringUtils.isNotBlank(fraudType)){
					    compensateVo.setFraudType(fraudType);
					} else {
						compensateVo.setFraudType(null);
					}
					if(StringUtils.isNotBlank(impairmentType)){	
						compensateVo.setImpairmentType(impairmentType);
					} else {
						compensateVo.setImpairmentType(null);
					}
					compHandleService.updatePrpLCompensate(compensateVo);
				}
				
			}
			
			mav.addObject("dataSourceList",dataSourceList);
			mav.setViewName("verifyClaim/VerifyClaimEdit_initSubmit");
			
		}else if(VClaimType.VC_ADOPT.equals(action)){//????????????
			try{
				//???????????????????????????????????????
				String handlerIdKey = wfTaskVo.getHandlerIdKey();
				PrpLCompensateVo compensateVo = compeTaskService.findCompByPK(handlerIdKey);
				if(compensateVo!=null){
					//?????????????????????????????????????????????
					if(StringUtils.isNotBlank(fraudType)){
					    compensateVo.setFraudType(fraudType);
					}else {
						compensateVo.setFraudType(null);
					}
					if(StringUtils.isNotBlank(impairmentType)){	
						compensateVo.setImpairmentType(impairmentType);
					} else {
						compensateVo.setImpairmentType(null);
					}
					fxqRiskAuditInfo(compensateVo.getCompensateNo(),compensateVo.getPolicyNo(),WebUserUtils.getUser());
					compHandleService.updatePrpLCompensate(compensateVo);
				}
				
				
				mav.addObject("currentLevel",currentNode.split("_LV")[1]);
				mav.addObject("backLevel",backLevel);
				mav.addObject("cisderoAmout",cisderoAmout);
				mav.addObject("recFlag",uwNotionMainVo.getRecoveries());
				mav.addObject("taskInNode",taskInNode);
				mav.setViewName("verifyClaim/VerifyClaimEdit_initAdopt");
			}catch(Exception e){
				mav.addObject("backLevel",0);
				mav.setViewName("verifyClaim/VerifyClaimEdit_initAdopt");
//				throw new IllegalArgumentException(e);
			}
		}else{
			throw new IllegalArgumentException("???????????????action");
		}
		
		nextVo.setCurrentName(currentName);
		nextVo.setCurrentNode(currentNode);
		// nextVo.setFlowId(flowId);
		// ??????????????????????????????
		nextVo.setAuditStatus(action);
		nextVo.setAssignUser(WebUserUtils.getUserName());
		nextVo.setAssignCom(WebUserUtils.getComCode());
		nextVo.setComCode(WebUserUtils.getComCode());
		
		mav.addObject("nextVo",nextVo);
		mav.addObject("currentName",currentName);
		mav.addObject("action",action);
		mav.addObject("taskId",taskId);
		mav.addObject("uwNotionMainId",uwNotionMainId);
		return mav;
	}
	
	private VerifyClaimRuleVo getVerifyMaxLevel(PrpLWfTaskVo wfTaskVo,String action){
		VerifyClaimRuleVo ruleVo = new VerifyClaimRuleVo();
		ruleVo.setComCode(WebUserUtils.getComCode());
		int currentLevel = Integer.valueOf(wfTaskVo.getSubNodeCode().split("_LV")[1]);
		if(currentLevel>8){//???????????????????????????8 ????????????????????????
			ruleVo.setTopComp("1");
		}
		if(wfTaskVo == null || !"VClaim".equals(wfTaskVo.getNodeCode())){
			throw new IllegalArgumentException("?????????????????????????????????");
		}
		String taskInNode = wfTaskVo.getYwTaskType();
		String handlerIdKey = wfTaskVo.getHandlerIdKey();
		PrpLCompensateVo compensateVo = compeTaskService.findCompByPK(handlerIdKey);
		SysUserVo userVo = WebUserUtils.getUser();
		if(FlowNode.CompeCI.equals(taskInNode)||FlowNode.CompeWfCI.equals(taskInNode)
				||FlowNode.CompeBI.equals(taskInNode)||FlowNode.CompeWfBI.equals(taskInNode)){//????????????
			//????????????
			if(compensateVo != null){
				ruleVo.setClassCode(Risk.DQZ.equals(compensateVo.getRiskCode()) ? "11" : "12");
				ruleVo.setRiskCode(compensateVo.getRiskCode());
				Double sumAmt = Math.abs(DataUtils.NullToZero(compensateVo.getSumPaidAmt().abs())
						.add(DataUtils.NullToZero(compensateVo.getSumPaidFee().abs())).doubleValue());
				ruleVo.setSumAmt(sumAmt);
	            PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.ILOGVALIDFLAG,userVo.getComCode());
                if("1".equals(configValueVo.getConfigValue())){//??????ilog
                    //ilog????????????
                    LIlogRuleResVo vPriceResVo = null;
                    try{
                        if(FlowNode.CompeCI.equals(taskInNode)||FlowNode.CompeBI.equals(taskInNode)){
                            List<PrpLWfTaskVo> wfTaskVoList=wfTaskHandleService.findEndTask(wfTaskVo.getRegistNo(),handlerIdKey, FlowNode.Compe);
                            PrpLWfTaskVo lWfTaskVo = wfTaskVoList.get(0);
                            String isSubmitHeadOffice = CodeConstants.CommonConst.FALSE;
                    		if(VClaimType.VC_ADOPT.equals(action)){//?????????????????????????????????????????????????????????????????????
	                            Map<String,Object> params = new HashMap<String,Object>();
	                    		params.put("registNo",lWfTaskVo.getRegistNo());
	                    		if(lWfTaskVo.getTaskId() == null){
		                    		params.put("taskId",BigDecimal.ZERO.doubleValue());
	                    		} else{
		                    		params.put("taskId",lWfTaskVo.getTaskId().doubleValue());
	                    		}
	                    		if(FlowNode.CompeCI.equals(taskInNode)||FlowNode.CompeBI.equals(taskInNode)){
	                      			params.put("currentNode",FlowNode.CompeBI.getUpperNode());
	                      		}
	                    		isSubmitHeadOffice = managerService.isSubmitHeadOffice(params);	
                    		}
                            vPriceResVo = compensateHandleServiceIlogService.organizaForCompensate(handlerIdKey,"2","00",lWfTaskVo.getTaskId(),FlowNode.VClaim.name(),userVo,lWfTaskVo,isSubmitHeadOffice);
                        }
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                   
                    if(FlowNode.CompeWfCI.equals(taskInNode)||FlowNode.CompeWfBI.equals(taskInNode)){
                        try{
                            List<PrpLWfTaskVo> wfTaskVoList=wfTaskHandleService.findEndTask(wfTaskVo.getRegistNo(),handlerIdKey, FlowNode.CompeWf);
                            PrpLWfTaskVo lWfTaskVo = wfTaskVoList.get(0);
                            String isSubmitHeadOffice = CodeConstants.CommonConst.FALSE;
                    		if(VClaimType.VC_ADOPT.equals(action)){//?????????????????????????????????????????????????????????????????????
	                            Map<String,Object> params = new HashMap<String,Object>();
	                    		params.put("registNo",lWfTaskVo.getRegistNo());
	                    		if(lWfTaskVo.getTaskId() == null){
		                    		params.put("taskId",BigDecimal.ZERO.doubleValue());
	                    		} else{
		                    		params.put("taskId",lWfTaskVo.getTaskId().doubleValue());
	                    		}
	                    		if(FlowNode.CompeWfCI.equals(taskInNode)||FlowNode.CompeWfBI.equals(taskInNode)){
	                      			params.put("currentNode",FlowNode.CompeWfBI.getUpperNode());
	                      		}
	                    		isSubmitHeadOffice = managerService.isSubmitHeadOffice(params);	
                    		}
                            vPriceResVo = compensateHandleServiceIlogService.organizaForCompensate(handlerIdKey,"2","04",lWfTaskVo.getTaskId(),FlowNode.VClaim.name(),userVo,lWfTaskVo,isSubmitHeadOffice);
                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                    ruleVo.setBackLevel(Integer.parseInt(vPriceResVo.getMinUndwrtNode()));
                    ruleVo.setMaxLevel(Integer.parseInt(vPriceResVo.getMaxUndwrtNode()));
                }
			}
		}else if(FlowNode.PrePayCI.equals(taskInNode)||FlowNode.PrePayWfCI.equals(taskInNode)
					||FlowNode.PrePayBI.equals(taskInNode)||FlowNode.PrePayWfBI.equals(taskInNode)){//????????????
			//????????????
			ruleVo.setRiskCode(compensateVo.getRiskCode());
			ruleVo.setClassCode(Risk.DQZ.equals(compensateVo.getRiskCode()) ? "11" : "12");
			ruleVo.setSumAmt(DataUtils.NullToZero(compensateVo.getSumAmt().abs()).doubleValue());
			PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.ILOGVALIDFLAG,userVo.getComCode());
            if("1".equals(configValueVo.getConfigValue())){//??????ilog
               //ilog????????????
               LIlogRuleResVo vPriceResVo = null;
               String callNode = "01";
               if(FlowNode.PrePayCI.equals(taskInNode)||FlowNode.PrePayBI.equals(taskInNode)){
                  List<PrpLWfTaskVo> wfTaskVoList = wfTaskHandleService.findEndTask(wfTaskVo.getRegistNo(),handlerIdKey, FlowNode.PrePay);
                  PrpLWfTaskVo lWfTaskVo = null;
                  if(wfTaskVoList != null && wfTaskVoList.size() > 0 ){
                      lWfTaskVo = wfTaskVoList.get(0);
                  }else{
                      List<PrpLWfTaskVo> wfTaskVos = wfTaskHandleService.findEndTask(wfTaskVo.getRegistNo(),wfTaskVo.getClaimNo(), FlowNode.PrePay);
                      if(wfTaskVos != null && wfTaskVos.size() > 0 ){
                          lWfTaskVo = wfTaskVos.get(0);
                      }else{
                          List<PrpLWfTaskVo> lwfTaskVos = wfTaskHandleService.findTaskOutVo(wfTaskVo.getRegistNo(),FlowNode.PrePay.name()); 
                          lWfTaskVo = lwfTaskVos.get(0);
                      }
                  }
                  try{
                      lWfTaskVo.setRiskCode(compensateVo.getRiskCode());
                      lWfTaskVo.setComCode(WebUserUtils.getComCode());
                      List<PrpLCompensateVo> compensateVoList = compensateTaskService.findCompListByClaimNo(compensateVo.getClaimNo(),"Y");
                      BigDecimal sumYAmt = new BigDecimal(0);
                      if(compensateVoList != null && compensateVoList.size() > 0){
                          for(PrpLCompensateVo pensateVo : compensateVoList){
                              sumYAmt = sumYAmt.add(DataUtils.NullToZero(pensateVo.getSumAmt()));
                          }
                      }
                      sumYAmt = sumYAmt.add(DataUtils.NullToZero(compensateVo.getSumAmt()));
                      lWfTaskVo.setMoney(sumYAmt);
                      
                      String isSubmitHeadOffice = CodeConstants.CommonConst.FALSE;
              		  if(VClaimType.VC_ADOPT.equals(action)){//?????????????????????????????????????????????????????????????????????
              			  Map<String,Object> params = new HashMap<String,Object>();
                  		  params.put("registNo",lWfTaskVo.getRegistNo());
	                  	  if(lWfTaskVo.getTaskId() == null){
	                    	 params.put("taskId",BigDecimal.ZERO.doubleValue());
	                	  } else{
	                    	 params.put("taskId",lWfTaskVo.getTaskId().doubleValue());
	                	  }
                  		  if(FlowNode.PrePayCI.equals(taskInNode)||FlowNode.PrePayBI.equals(taskInNode)){
                  			  params.put("currentNode",FlowNode.PrePayCI.getUpperNode());
                  		  }
                  		  isSubmitHeadOffice = managerService.isSubmitHeadOffice(params);	
              		  }
                      vPriceResVo = compensateHandleServiceIlogService.organizaForPrePay(lWfTaskVo,"2",callNode,FlowNode.PrePay.name(),userVo,isSubmitHeadOffice);
                  }
                  catch(Exception e){
                      e.printStackTrace();
                  }
               }
               if(FlowNode.PrePayWfCI.equals(taskInNode)||FlowNode.PrePayWfBI.equals(taskInNode)){
                  List<PrpLWfTaskVo> wfTaskVoList = wfTaskHandleService.findEndTask(wfTaskVo.getRegistNo(),handlerIdKey, FlowNode.PrePayWf);
                  callNode = "03";//????????????
                  PrpLWfTaskVo lWfTaskVo = null;
                  if(wfTaskVoList != null && wfTaskVoList.size() > 0 ){
                      lWfTaskVo = wfTaskVoList.get(0);
                  }else{
                      List<PrpLWfTaskVo> wfTaskVos = wfTaskHandleService.findEndTask(wfTaskVo.getRegistNo(),wfTaskVo.getClaimNo(), FlowNode.PrePayWf);
                      if(wfTaskVos != null && wfTaskVos.size() > 0 ){
                          lWfTaskVo = wfTaskVos.get(0);
                      }else{
                          List<PrpLWfTaskVo> lwfTaskVos = wfTaskHandleService.findTaskOutVo(wfTaskVo.getRegistNo(),FlowNode.PrePayWf.name()); 
                          lWfTaskVo = lwfTaskVos.get(0);
                      }
                  }
                  try{
                      lWfTaskVo.setRiskCode(compensateVo.getRiskCode());
                      lWfTaskVo.setComCode(WebUserUtils.getComCode());
                      List<PrpLCompensateVo> compensateVoList = compensateTaskService.findCompListByClaimNo(compensateVo.getClaimNo(),"Y");
                      BigDecimal sumYAmt = new BigDecimal(0);
                      if(compensateVoList != null && compensateVoList.size() > 0){
                          for(PrpLCompensateVo pensateVo : compensateVoList){
                              sumYAmt = sumYAmt.add(DataUtils.NullToZero(pensateVo.getSumAmt()));
                          }
                      }
                      sumYAmt = sumYAmt.add(DataUtils.NullToZero(compensateVo.getSumAmt()));
                      lWfTaskVo.setMoney(sumYAmt);
                      String isSubmitHeadOffice = CodeConstants.CommonConst.FALSE;
              		  if(VClaimType.VC_ADOPT.equals(action)){//?????????????????????????????????????????????????????????????????????
              			  Map<String,Object> params = new HashMap<String,Object>();
                  		  params.put("registNo",lWfTaskVo.getRegistNo());
                  		  if(lWfTaskVo.getTaskId() == null){
	                    	 params.put("taskId",BigDecimal.ZERO.doubleValue());
	                	  } else{
	                    	 params.put("taskId",lWfTaskVo.getTaskId().doubleValue());
	                	  }
                  		  if(FlowNode.PrePayCI.equals(taskInNode)||FlowNode.PrePayBI.equals(taskInNode)){
                  			  params.put("currentNode",FlowNode.PrePayCI.getUpperNode());
                  		  }
                  		  if(FlowNode.PrePayBI.equals(taskInNode)||FlowNode.PrePayWfBI.equals(taskInNode)){
                      		  params.put("currentNode",FlowNode.PrePayWfCI.getUpperNode());
                  		  }
                  		  isSubmitHeadOffice = managerService.isSubmitHeadOffice(params);	
              		  }
                      vPriceResVo = compensateHandleServiceIlogService.organizaForPrePay(lWfTaskVo,"2",callNode,FlowNode.VClaim.name(),userVo,isSubmitHeadOffice);
                  }
                  catch(Exception e){
                      e.printStackTrace();
                  }
               }
               ruleVo.setBackLevel(Integer.parseInt(vPriceResVo.getMinUndwrtNode()));
               ruleVo.setMaxLevel(Integer.parseInt(vPriceResVo.getMaxUndwrtNode()));
             }
		}else if(FlowNode.PadPay.equals(taskInNode)){//????????????
			ruleVo.setRiskCode(Risk.DQZ);
			ruleVo.setClassCode("11");
			ruleVo.setSumAmt(10000.0);
		    PrpLPadPayMainVo padPayVo = padPayService.getPadPayInfo(wfTaskVo.getRegistNo(),"");
		    List<PrpLWfTaskVo> wfTaskVoList = wfTaskHandleService.findEndTask(wfTaskVo.getRegistNo(),padPayVo.getCompensateNo(), FlowNode.PadPay);
		    PrpLWfTaskVo lWfTaskVo = null;
		    if(wfTaskVoList != null && wfTaskVoList.size() > 0 ){
		        lWfTaskVo = wfTaskVoList.get(0);
		    }else{
		        List<PrpLWfTaskVo> wfTaskVos = wfTaskHandleService.findTaskOutVo(wfTaskVo.getRegistNo(),FlowNode.PadPay.name()); 
		        lWfTaskVo = wfTaskVos.get(0);
		    }
            try{
            	 String isSubmitHeadOffice = CodeConstants.CommonConst.FALSE;
         		  if(VClaimType.VC_ADOPT.equals(action)){//?????????????????????????????????????????????????????????????????????
         			  Map<String,Object> params = new HashMap<String,Object>();
             		  params.put("registNo",lWfTaskVo.getRegistNo());
             		  if(lWfTaskVo.getTaskId() == null){
                    	 params.put("taskId",BigDecimal.ZERO.doubleValue());
                	  } else{
                    	 params.put("taskId",lWfTaskVo.getTaskId().doubleValue());
                	  }
              		  if(FlowNode.PrePayCI.equals(taskInNode)||FlowNode.PrePayBI.equals(taskInNode)){
              			  params.put("currentNode",FlowNode.PrePayCI.getUpperNode());
              		  }
             		 if(FlowNode.PadPay.equals(taskInNode)){
                 		  params.put("currentNode",FlowNode.PadPay.name());
             		  }
             		  isSubmitHeadOffice = managerService.isSubmitHeadOffice(params);	
         		  }
            	
                LIlogRuleResVo vPriceResVo = compensateHandleServiceIlogService.organizaForPadPay(padPayVo,"2","02",lWfTaskVo.getTaskId(),FlowNode.VClaim.name(),userVo,isSubmitHeadOffice);
                ruleVo.setBackLevel(Integer.parseInt(vPriceResVo.getMinUndwrtNode()));
                ruleVo.setMaxLevel(Integer.parseInt(vPriceResVo.getMaxUndwrtNode()));
            }
            catch(Exception e){
                e.printStackTrace();
            }
		}else{//??????
			PrpLClaimVo claimVo = claimTaskService.findClaimVoByClaimNo(wfTaskVo.getClaimNo());
			ruleVo.setRiskCode(claimVo.getRiskCode());
			ruleVo.setClassCode(Risk.DQZ.equals(claimVo.getRiskCode()) ? "11" : "12");
			ruleVo.setSumAmt(DataUtils.NullToZero(claimVo.getSumClaim().abs()).doubleValue());
			List<PrpLWfTaskVo> prpLWfTasks= wfTaskHandleService.queryTaskByAnyOrderOutTime(claimVo.getRegistNo(),claimVo.getClaimNo(), FlowNode.CancelAppJuPei.name(), CodeConstants.HandlerStatus.END);
			try{
                LIlogRuleResVo vPriceResVo = compensateHandleServiceIlogService.organizaForClaimCancelJuPei(claimVo,claimVo.getSumClaim().abs(),"2","05",prpLWfTasks.get(0).getTaskId(),FlowNode.VClaim.name(),userVo);
                ruleVo.setBackLevel(Integer.parseInt(vPriceResVo.getMinUndwrtNode()));
                ruleVo.setMaxLevel(Integer.parseInt(vPriceResVo.getMaxUndwrtNode()));
			}
            catch(Exception e){
                e.printStackTrace();
            }
		}
		PrpLConfigValueVo configRuleValueVo = ConfigUtil.findConfigByCode(CodeConstants.RULEVALIDFLAG,WebUserUtils.getComCode());
        if("1".equals(configRuleValueVo.getConfigValue())){
            ruleVo = claimRuleApiService.compToVClaim(ruleVo);
        }
		return ruleVo;
	}
	
	@RequestMapping("/loadNextPage.ajax")
	public ModelAndView loadNextPage(String taskId,String mapDatas){
		ModelAndView mav = new ModelAndView();
		PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(Double.parseDouble(taskId));

		if(mapDatas != null){
		    JSONObject jsonObj = JSONObject.fromObject(mapDatas);
	           if(!jsonObj.isNullObject()){
		            String certi = jsonObj.getString("CertiNodeCode");
		            if("Certi".equals(certi)){
		                wfTaskVo.setAssignUser(jsonObj.getString("assignUser"));
		                wfTaskVo.setTaskInUser(jsonObj.getString("taskInUser"));
		            } 
		    }
		}
		
		mav.addObject("wfTaskVo",wfTaskVo);
		mav.setViewName("verifyClaim/VerifyClaimEdit_loadNextPage");
		return mav;
	}
	
	/**
	 * ??????????????????
	 * @return
	 */
	@RequestMapping(value = "/acceptVClaimTask.do")
	@ResponseBody
	public AjaxResult acceptVClaimTask(Double flowTaskId) throws ParseException {
		AjaxResult ajaxResult = new AjaxResult();
		try{
			PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(flowTaskId);
			String compeNo=wfTaskVo.getHandlerIdKey();
			if(FlowNode.VClaim.equals(wfTaskVo.getNodeCode())){
				if(HandlerStatus.INIT.equals(wfTaskVo.getHandlerStatus())){// ???????????????
					// ????????????
					wfTaskHandleService.acceptTask(flowTaskId,WebUserUtils.getUserCode(),WebUserUtils.getComCode());
					// ????????????????????????
					wfTaskHandleService.tempSaveTask(flowTaskId,compeNo,WebUserUtils.getUserCode(),WebUserUtils.getComCode());
					
				}else{// ????????????
					throw new IllegalArgumentException("?????????????????????????????????"+wfTaskVo.getHandlerUser());
				}
			}else{
				throw new IllegalArgumentException("??????????????????");
			}
			ajaxResult.setStatus(HttpStatus.SC_OK);
			ajaxResult.setData(flowTaskId);
		}
		catch(Exception e){
			logger.debug("???????????????????????????"+e.getMessage());
			ajaxResult.setStatus(HttpStatus.SC_EXPECTATION_FAILED);
		}
		return ajaxResult;
	}
	
	
	/** ????????????
	 * @return
	 */
	@RequestMapping("/passValid.ajax")
	@ResponseBody
	public AjaxResult verifyClaimValid(String registNo) {
		AjaxResult ajaxResult = new AjaxResult();
		boolean falg = wfTaskHandleService.existTaskByNodeCode
				(registNo,FlowNode.Survey, null, "0");
		ajaxResult.setData(falg);
		return ajaxResult;
	}
	
	/** ?????????????????? @return */
	@RequestMapping("/verifyPadPayEdit.do")
	public ModelAndView verifyPadPayEdit() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("verifyClaim/VerifyPadPayEdit");
		return modelAndView;
	}

	/** ????????????????????????ajax??????
	 * @return
	 */
	@RequestMapping("/addPersRow.ajax")
	public ModelAndView addPersRow(String flag, int trIndex) {
		String tabFlag = flag;// flag??????1,2
		int index = trIndex;// ????????????
		ModelAndView modelAndView = new ModelAndView();
		if (tabFlag.equals("1")) {// 1-??????PersLossTbody
			modelAndView.addObject("trIndex", index);
			modelAndView.setViewName("verifyClaim/VerifyPadPayEdit_PersLossTbody");
		}
		if (tabFlag.equals("2")) {// 2-??????PersLossPeeTbody
			modelAndView.addObject("trIndex", index);
			modelAndView.setViewName("verifyClaim/VerifyPadPayEdit_PersLossPeeTbody");
		}
		return modelAndView;
	}

	/**
	 * ????????????????????????ajax??????
	 * @return 
	 */
	@RequestMapping("/addPreRow.ajax")
	public ModelAndView addRowInfo(String bodyName) {
		String tName = bodyName;
		ModelAndView modelAndView = new ModelAndView();
		if (tName.equals("IndemnityTbody")) {// ??????IndemnityTbody
			modelAndView.setViewName("verifyClaim/VerifyPrePayEdit_IndemnityTbody");
		}
		if (tName.equals("FeeTbody")) {// ??????FeeTbody
			modelAndView.setViewName("verifyClaim/VerifyPrePayEdit_FeeTbody");
		}
		return modelAndView;
	}
	@RequestMapping("/repairMoney.ajax")
	@ResponseBody
	public AjaxResult repairMoney(String claimNo) {
		AjaxResult ajaxResult = new AjaxResult();
		String sign="0";//???????????????????????????????????????
		PrpLFxqFavoreeVo reevo=new PrpLFxqFavoreeVo();
		List<PrpLFxqFavoreeVo>  reeListVo=payCustomService.findPrpLFxqFavoreeVoByclaimNo(claimNo);
		if(reeListVo!=null && reeListVo.size()>0){
			reevo=reeListVo.get(0);
		}
		//flag?????????????????????????????????seeFlag???????????????????????????????????????
		if(reevo!=null && "1".equals(reevo.getFlag()) && !"1".equals(reevo.getSeeFlag())){
			sign="1";
		}
		ajaxResult.setData(sign);
		return ajaxResult;
	}
	
	@RequestMapping("/existCompIn.ajax")
	@ResponseBody
	public AjaxResult existCompIn(String registNo,String riskCode,String claimNo) {
		AjaxResult ajaxResult = new AjaxResult();
		// ??????????????????0???????????????????????????????????????????????????
		boolean existComp = false;
		existComp = wfTaskHandleService.existTaskIn(riskCode,registNo);
		//?????????????????????????????????????????????????????????????????????????????????????????????0???
		PrpLClaimVo claimVo = claimTaskService.findClaimVoByClaimNo(claimNo);
		if(claimVo != null && claimVo.getCaseNo() != null){
			existComp = true;
		}
		ajaxResult.setData(existComp);
		return ajaxResult;
	}

	/**
	 * ????????????????????????????????????????????????????????????
	 * @param compensateNo
	 * @param policyNo
	 * @param userVo
	 */
	private void fxqRiskAuditInfo(String compensateNo,String policyNo,SysUserVo userVo){
		PrplcomcontextVo contextVo=compHandleService.findPrplcomcontextByCompensateNo(compensateNo,"H");
    	String amlUrl = SpringProperties.getProperty("dhic.aml.url");
        logger.info("amlUrl============================"+amlUrl);
        FxqCrmRiskService crmRiskService = new FxqCrmRiskService(amlUrl);
        BussRiskInfoAuditVo auditVo=new BussRiskInfoAuditVo();
        auditVo.setBussNo(compensateNo);
        auditVo.setRefBussNo(policyNo);
        auditVo.setDataType("2");
        if(contextVo!=null && "1".equals(contextVo.getFlag())){
        	auditVo.setIsRiskBuss("1");
        	auditVo.setAuditRemark(contextVo.getCauses());
        }else{
        	auditVo.setIsRiskBuss("0");
        }
        
       /* //????????????
        List<PrpLCMainVo> cmainVos= prpLCMainService.findPrpLCMainsByRegistNo(registNo);
        if(cmainVos!=null && cmainVos.size()>0){
        	for(PrpLCMainVo vo:cmainVos){
        		if(StringUtils.isNotBlank(riskCode) && riskCode.equals(vo.getRiskCode())){
        			 List<PrpLCInsuredVo> PrpLCInsuredList = vo.getPrpCInsureds();
        		            for(PrpLCInsuredVo cInsured:PrpLCInsuredList){
        		                if("1".equals(cInsured.getInsuredFlag())){
        		                	
        		                    
        		                }
        		               
        		            }
        		        }
        	}
        }*/
        crmRiskService.saveSusRiskAuditInfo(auditVo);
	}
	

	 /**
     * ???????????????????????????????????????
     * @return
     */
   private String fxInfoShow(String compensateNo){
    	SysUserVo userVo = WebUserUtils.getUser();
   	    String amlUrl = SpringProperties.getProperty("dhic.aml.saveurl");
        logger.info("amlUrl============================"+amlUrl);
        FxqAPIPanel fxqapipanel =new FxqAPIPanel(amlUrl);
        String fxinfo=fxqapipanel.loadDangerCheckRadio(userVo.getUserCode(),userVo.getComCode(),compensateNo,"C");
        logger.info("++++++++++++++++++++++++"+fxinfo);
        
    	return fxinfo;
    	
    }

   
   private SysCodeDictVo setReturnNextNodeByIlog(PrpLWfTaskVo wfTaskVo,String taskInNode){
       SysCodeDictVo dictVo1 = new SysCodeDictVo();
       String compensateNo = wfTaskVo.getHandlerIdKey();
       //ilog??????????????????????????????????????????
       //?????????????????????????????????????????? ???????????????????????????????????????????????????????????????????????????????????????????????????????????????
       //????????????????????????????????????????????????
       if(compensateNo.startsWith("Y") || compensateNo.startsWith("D")){//????????????
           dictVo1.setCodeCode(FlowNode.valueOf(taskInNode).toString());
           dictVo1.setCodeName(FlowNode.valueOf(taskInNode).getName());
       }else{
           PrpLCompensateVo compensateVo = compensateTaskService.findPrpLCompensateVoByPK(compensateNo); 
           //??????????????????????????????????????????
           if(compensateVo != null){
               //if(!"1".equals(compensateVo.getCreateUser())){//??????????????????
               if(!"AUTO".equals(compensateVo.getCreateUser())){//??????????????????
                   dictVo1.setCodeCode(FlowNode.valueOf(taskInNode).toString());
                   dictVo1.setCodeName(FlowNode.valueOf(taskInNode).getName());
               }else{
                   PrpLCertifyMainVo prpLCertifyMainVo = certifyService.findPrpLCertifyMainVo(wfTaskVo.getRegistNo());
                   if(!"1".equals(prpLCertifyMainVo.getAutoCertifyFlag())){//??????????????????
                       dictVo1.setCodeCode(FlowNode.valueOf("Certi").toString());
                       dictVo1.setCodeName(FlowNode.valueOf("Certi").getName());
                   } else{//????????????????????????
                       dictVo1.setCodeCode(FlowNode.valueOf("DLCar").toString());
                       dictVo1.setCodeName(FlowNode.valueOf("DLCar").getName());
                   }
               }
           }else{
               dictVo1.setCodeCode(FlowNode.valueOf(taskInNode).toString());
               dictVo1.setCodeName(FlowNode.valueOf(taskInNode).getName());
           }
       }
       return dictVo1;
   }

	@RequestMapping(value = "/checkEndCase.do")
	@ResponseBody
	public AjaxResult checkEndCase(Double taskId,String nextNode) {
       //???????????????????????????????????????
       //????????????????????????-->??????????????????
       AjaxResult ajaxResult = new AjaxResult();
       PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(taskId.doubleValue());
       Boolean existComp = false;
       existComp = wfTaskHandleService.existTaskInBySubNodeCode(wfTaskVo.getRegistNo(),"Certi");
       String remark = "";
       if(nextNode.equals("Certi")){
           remark = "???????????????????????????????????????????????????";
       }else{
           remark = "???????????????????????????????????????????????????";
       }
       
       String flags = "1";
       if(!existComp){//????????????
           PrpLCompensateVo compensateVo = compensateTaskService.findPrpLCompensateVoByPK(wfTaskVo.getHandlerIdKey());
           if("12".equals(compensateVo.getRiskCode().substring(0,2))){//???????????????????????????
               List<PrpLClaimVo> vos = claimService.findClaimListByRegistNo(wfTaskVo.getRegistNo());
               if(vos != null && vos.size() > 1){
                   String claimNo = "";
                   for(PrpLClaimVo vo : vos){
                       if("11".equals(vo.getRiskCode().substring(0,2))){//??????
                           claimNo = vo.getClaimNo();
                       }
                   }
                   PrpLEndCaseVo endCaseVo = endCasePubService.findEndCaseByClaimNo(claimNo);
                   if (endCaseVo != null && StringUtils.isNotBlank(endCaseVo.getClaimNo()) ) {//????????????
                       ajaxResult.setStatusText(remark);
                       flags = "0";
                   }
/*                   existComp = wfTaskHandleService.existTaskInBySubNodeCode(wfTaskVo.getRegistNo(),"CompeCI");
                   if(!existComp){//????????????????????????
                       if(CodeConstants.UnderWriteFlag.MANAL_UNDERWRITE.equals(compensateVo.getUnderwriteFlag())||
                               CodeConstants.UnderWriteFlag.AUTO_UNDERWRITE.equals(compensateVo.getUnderwriteFlag())){
                           ajaxResult.setStatusText("???????????????????????????????????????????????????");
                           flags = "0";
                       }
                   }*/
               }

           }else{//??????
               List<PrpLClaimVo> vos = claimService.findClaimListByRegistNo(wfTaskVo.getRegistNo());
               if(vos != null && vos.size() > 1){
                   String claimNo = "";
                   for(PrpLClaimVo vo : vos){
                       if("12".equals(vo.getRiskCode().substring(0,2))){//??????
                           claimNo = vo.getClaimNo();
                       }
                   }
                   List<PrpLReCaseVo> reCaseList = endCasePubService.findReCaseListByClaimNo(claimNo);
                   if (reCaseList != null && !reCaseList.isEmpty()) {//????????????
                       ajaxResult.setStatusText(remark);
                       flags = "0";
                   }
               }
           }
       }
       ajaxResult.setData(flags);
       ajaxResult.setStatus(HttpStatus.SC_OK);
       return ajaxResult;
   }

    @RequestMapping(value = "/coinsInfoView.ajax")
	@ResponseBody
	public ModelAndView coinsInfoView(
			@FormModel("compensateNo") String compensateNo,
			@FormModel("nodeCode") String nodeCode){
    	ModelAndView mv = new ModelAndView();
    	List<PrpLCoinsVo> prpLCoinsList = compensateTaskService.findPrpLCoinsByCompensateNo(compensateNo);
		if(prpLCoinsList!=null && prpLCoinsList.size()>0){
			mv.addObject("prpLCoinsList", prpLCoinsList);
			mv.addObject("coinsFlag", prpLCoinsList.get(0).getCoinsFlag());
			mv.addObject("calculateType", prpLCoinsList.get(0).getCalculateType());
		}
		mv.setViewName("compensate/CompensateEdit_Coins");
		
    	return mv;
    }
}
