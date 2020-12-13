/******************************************************************************
 * CREATETIME : 2016年5月30日 上午10:05:17
 ******************************************************************************/
package ins.sino.claimcar.platform.service;

import ins.framework.utils.Beans;
import ins.platform.common.service.facade.CodeTranService;
import ins.platform.utils.DataUtils;
import ins.platform.vo.SysCodeDictVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.PayFlagType;
import ins.sino.claimcar.carplatform.constant.RequestType;
import ins.sino.claimcar.carplatform.controller.PlatformController;
import ins.sino.claimcar.carplatform.service.CiClaimPlatformLogService;
import ins.sino.claimcar.carplatform.util.PlatformFactory;
import ins.sino.claimcar.carplatform.vo.CiClaimPlatformLogVo;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.PrpLCheckDutyVo;
import ins.sino.claimcar.claim.services.CompensateService;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claim.vo.PrpLPaymentVo;
import ins.sino.claimcar.constant.Risk;
import ins.sino.claimcar.manager.service.ManagerService;
import ins.sino.claimcar.manager.vo.PrpLPayCustomVo;
import ins.sino.claimcar.platform.util.SendPlatformUtil;
import ins.sino.claimcar.platform.vo.BiPaymentBasePartVo;
import ins.sino.claimcar.platform.vo.BiPaymentPayDataVo;
import ins.sino.claimcar.platform.vo.BiPaymentReqBodyVo;
import ins.sino.claimcar.platform.vo.CiPaymentBasePartVo;
import ins.sino.claimcar.platform.vo.CiPaymentPayDataVo;
import ins.sino.claimcar.platform.vo.CiPaymentReqBodyVo;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.subrogation.service.PlatLockService;
import ins.sino.claimcar.subrogation.vo.PrpLPlatLockVo;
import ins.sino.claimcar.subrogation.vo.PrpLRecoveryOrPayVo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ★XMSH
 */
@Service("sendPaymentToPlatformService")
public class SendPaymentToPlatformService {

	private Logger logger = LoggerFactory.getLogger(SendPaymentToPlatformService.class);

	@Autowired
	private ManagerService managerService;
	@Autowired
	private CompensateService compensateService;
	@Autowired
	private PolicyViewService policyViewService;
	@Autowired
	CiClaimPlatformLogService platformLogService;
	@Autowired
	private PlatLockService platLockService;
	@Autowired
	private CheckTaskService checkTaskService;
	@Autowired
	SendPaymentToSHPlatformService paymentToSH;
	@Autowired
    private CodeTranService codeTranService;
	@Autowired
	private RegistQueryService registQueryService;

	
	/**
	 * 发送--赔款支付，调用方法(全国平台)
	 * @param bussNo
	 * @param reqType
	 * @param comCode
	 * @throws Exception 
	 */
	public void sendPaymentToPlatform(CiClaimPlatformLogVo logVo) {
		if (RequestType.PaymentBI.getCode().equals(logVo.getRequestType())) {
			sendPaymentBIToPlatform(logVo.getBussNo());
		} else {
			sendPaymentCIToPlatform(logVo.getBussNo());
		}
	}
	
	public void sendPlatform(String compeNo) throws Exception {
		PrpLCompensateVo compensateVo = compensateService.findCompByPK(compeNo);
		
		String riskCode = compensateVo.getRiskCode();
		String policyType = Risk.DQZ.equals(riskCode) ? "11" : "12"; 
		String comCode = policyViewService.findPolicyComCode(compensateVo.getRegistNo(),policyType);
		if (comCode.startsWith("22")) {// 上海平台
			boolean flag = true;
			for (PrpLPaymentVo paymentVo : compensateVo.getPrpLPayments()) {
				if (!"1".equals(paymentVo.getPayStatus())) {// 存在未支付的赔款
					flag = false;
				}
			}
			if (flag) {// 所有的赔款都已支付，上传赔款支付确认平台
				paymentToSH.sendPaymentToSH(compeNo);
			}
		} else {
			if (Risk.DQZ.equals(compensateVo.getRiskCode())) {
				sendPaymentCIToPlatform(compeNo);
			} else {
				sendPaymentBIToPlatform(compeNo);
			}
		}
	}
	
	public CiClaimPlatformLogVo sendPaymentCIToPlatform(String compeNo){
		CiClaimPlatformLogVo returnLog = null;
		PrpLCompensateVo compensateVo = compensateService.findCompByPK(compeNo);
		String registNo = compensateVo.getRegistNo();
		PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
		// 联共保 从共，从联不交互平台
		if(CodeConstants.GBFlag.MINORCOMMON.equals(registVo.getIsGBFlag())||CodeConstants.GBFlag.MINORRELATION.equals(registVo.getIsGBFlag())){
			return null;
		}
		//保单机构
		String comCode = policyViewService.findPolicyComCode(registNo,"11");
		CiClaimPlatformLogVo logVo = platformLogService.findLogByBussNo(
				RequestType.RegistInfoCI.getCode(),registNo,comCode);
		if (logVo == null) {
			logger.info("赔款支付上传全国平台失败！该案件理赔编码不存在或报案未成功上传平台！");
		}

		// 投保确认码
		String validNo = policyViewService.findValidNo(registNo,"11");
		logger.info("投保确认码：" + validNo);
		
		
		PlatformController controller = PlatformFactory.getInstance(comCode,RequestType.PaymentCI);

		CiPaymentReqBodyVo bodyVo = new CiPaymentReqBodyVo();
		CiPaymentBasePartVo basePart = new CiPaymentBasePartVo();
		List<CiPaymentPayDataVo> payDataList = new ArrayList<CiPaymentPayDataVo>();

		basePart.setConfirmSequenceNo(validNo);
		basePart.setClaimCode(logVo.getClaimSeqNo());
		basePart.setReportNo(registNo);
		
		//赔付总金额（含施救费）修改，改为传全量的金额-修改，全国平台每次传已支付的金额，再上传再覆盖
		BigDecimal sumAmt = new BigDecimal(0);
//		List<PrpLCompensateVo> sumAmtList = compensateService.findCompensate(registNo,"N");
		List<PrpLCompensateVo> sumAmtList = compensateService.findCompensateByClaimno(compensateVo.getClaimNo(),"N");
		if(sumAmtList != null && !sumAmtList.isEmpty()){
			for(PrpLCompensateVo compe : sumAmtList){
				if(Risk.DQZ.equals(compe.getRiskCode())){//交强理算的
					if(compe.getPrpLPayments()!=null && !compe.getPrpLPayments().isEmpty()){
						for(PrpLPaymentVo payMent:compe.getPrpLPayments()){
							if("1".equals(payMent.getPayStatus())){
								sumAmt = sumAmt.add(DataUtils.NullToZero(payMent.getSumRealPay()));
							}
						}
					}
					//组织收款帐户明细列表
					setCiPaymentPayData(compe,payDataList);
				}
			}
		}
		basePart.setClaimAmount(sumAmt.doubleValue());
//		basePart.setClaimAmount(compensateVo.getSumAmt().doubleValue());
		basePart.setCompensateNo(compeNo);

		

		bodyVo.setBasePart(basePart);
		bodyVo.setPayDataList(payDataList);
		if(SendPlatformUtil.isMor(policyViewService.getPolicyInfoByType(registNo,"1"))){
			returnLog = controller.callPlatform(bodyVo);
		}
		return returnLog;
	}

	private void setCiPaymentPayData(PrpLCompensateVo compensateVo,List<CiPaymentPayDataVo> payDataList){
		String registNo = compensateVo.getRegistNo();
		for(PrpLPaymentVo paymentVo:compensateVo.getPrpLPayments()){
			if(!"1".equals(paymentVo.getPayStatus())){
				continue;
			}
			PrpLPayCustomVo payCustomVo = managerService.findPayCustomVoById(paymentVo.getPayeeId());
			CiPaymentPayDataVo payData = new CiPaymentPayDataVo();
			payData.setAccountNumber(payCustomVo.getAccountNo());
			payData.setBankAccount(payCustomVo.getBankOutlets());
//			payData.setBankAccount(payCustomVo.getBankNo());
			payData.setAccountName(payCustomVo.getPayeeName());
			String certifyType = payCustomVo.getCertifyType();
			 SysCodeDictVo sysdicVo = codeTranService.findTransCodeDictVo ("CertifyType",certifyType); 
			payData.setCentiType(sysdicVo==null?"99":sysdicVo.getProperty1());
			//CENTI_CODE	字符	20	CY	赔款收款人身份证/组织机构代码
			String certifyNo = payCustomVo.getCertifyNo();
			if (StringUtils.isNotBlank(certifyNo) && certifyNo.length() > 20) {
				certifyNo = certifyNo.substring(0,20);
			}
			payData.setCentiCode(certifyNo);
			
			String recoveryCode = "";
			if(PayFlagType.CLEAR_PAY.equals(paymentVo.getPayFlag())){//清付
				PrpLCheckDutyVo dutyVo = checkTaskService.findCheckDuty(registNo,Integer.parseInt(paymentVo.getItemId()));
				PrpLPlatLockVo platLockVo = platLockService.findPrpLPlatLockVoByLicenseNo(compensateVo.getRegistNo(),
						compensateVo.getPolicyNo(),PayFlagType.CLEAR_PAY,dutyVo.getLicenseNo());
				for(PrpLRecoveryOrPayVo recoveryOrPayVo : platLockVo.getPrpLRecoveryOrPays()){
					if(recoveryOrPayVo.getCompensateNo().equals(compensateVo.getCompensateNo())){
						CiPaymentPayDataVo payData1 = new CiPaymentPayDataVo();
						Beans.copy().from(payData).to(payData1);
						recoveryCode = recoveryOrPayVo.getRecoveryCode();
						payData1.setRecoveryCode(recoveryOrPayVo.getRecoveryCode());
						payData1.setClaimAmount(DataUtils.NullToZero
								(recoveryOrPayVo.getRecoveryOrPayAmount()).doubleValue());
						payData1.setPayTime(new Date());
						payDataList.add(payData1);
					}
				}
			}else if(PayFlagType.INSTEAD_PAY.equals(paymentVo.getPayFlag())){//代付/追偿
				PrpLCheckDutyVo dutyVo = checkTaskService.findCheckDuty(registNo,Integer.parseInt(paymentVo.getItemId()));
				PrpLPlatLockVo platLockVo = platLockService.findPrpLPlatLockVoByLicenseNo(compensateVo.getRegistNo(),
						compensateVo.getPolicyNo(),PayFlagType.INSTEAD_PAY,dutyVo.getLicenseNo());
				for(PrpLRecoveryOrPayVo recoveryOrPayVo : platLockVo.getPrpLRecoveryOrPays()){
					if(recoveryOrPayVo.getCompensateNo().equals(compensateVo.getCompensateNo())){
						recoveryCode = recoveryOrPayVo.getRecoveryCode();
						CiPaymentPayDataVo payData2 = new CiPaymentPayDataVo();
						Beans.copy().from(payData).to(payData2);
						recoveryCode = recoveryOrPayVo.getRecoveryCode();
						payData2.setRecoveryCode(recoveryOrPayVo.getRecoveryCode());
						payData2.setClaimAmount(DataUtils.NullToZero
								(recoveryOrPayVo.getRecoveryOrPayAmount()).doubleValue());
						payData2.setPayTime(new Date());
						payDataList.add(payData2);
					}
				}
			}else {
				payData.setRecoveryCode(recoveryCode);
				payData.setClaimAmount(paymentVo.getSumRealPay().doubleValue());
//				payData.setPayTime(paymentVo.getPayTime());
				payData.setPayTime(new Date());
				payDataList.add(payData);
			}
		}
	}
	
	public CiClaimPlatformLogVo sendPaymentBIToPlatform(String compeNo){
		CiClaimPlatformLogVo returnLog = null;
		PrpLCompensateVo compensateVo = compensateService.findCompByPK(compeNo);
		String registNo = compensateVo.getRegistNo();
		PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
		// 联共保 从共，从联不交互平台
		if(CodeConstants.GBFlag.MINORCOMMON.equals(registVo.getIsGBFlag())||CodeConstants.GBFlag.MINORRELATION.equals(registVo.getIsGBFlag())){
			return null;
		}
		//保单机构
		String comCode = policyViewService.findPolicyComCode(registNo,"12");
		CiClaimPlatformLogVo logVo = platformLogService.findLogByBussNo(
				RequestType.RegistInfoBI.getCode(),registNo,comCode);
		if (logVo == null) {
			logger.info("赔款支付上传全国平台失败！该案件理赔编码不存在或报案未成功上传平台！");
		}

		// 投保确认码
		String validNo = policyViewService.findValidNo(registNo,"12");
		logger.info("投保确认码：" + validNo);
		

		PlatformController controller = PlatformFactory.getInstance(comCode,RequestType.PaymentBI);

		BiPaymentReqBodyVo bodyVo = new BiPaymentReqBodyVo();
		BiPaymentBasePartVo basePart = new BiPaymentBasePartVo();
		List<BiPaymentPayDataVo> payDataList = new ArrayList<BiPaymentPayDataVo>();

		basePart.setClaimSequenceNo(logVo.getClaimSeqNo());
		basePart.setClaimNotificationNo(registNo);
		
		basePart.setConfirmSequenceNo(validNo);
		
		// 赔付总金额（含施救费）修改，改为传全量的金额
		BigDecimal sumAmt = new BigDecimal(0);
//		List<PrpLCompensateVo> sumAmtList = compensateService.findCompensate(registNo,"N");
		List<PrpLCompensateVo> sumAmtList = compensateService.findCompensateByClaimno(compensateVo.getClaimNo(),"N");
		if(sumAmtList!=null&& !sumAmtList.isEmpty()){
			for(PrpLCompensateVo compe : sumAmtList){
				if(!Risk.DQZ.equals(compe.getRiskCode())){//商业理算的
					if(compe.getPrpLPayments()!=null && !compe.getPrpLPayments().isEmpty()){
						for(PrpLPaymentVo payMent:compe.getPrpLPayments()){
							if("1".equals(payMent.getPayStatus())){
								sumAmt = sumAmt.add(DataUtils.NullToZero(payMent.getSumRealPay()));
							}
							
						}
					}
					//组织收款帐户明细列表
					setBiPaymentPayData(compe,payDataList);
				}
			}
		}
		basePart.setPayAmount(sumAmt.doubleValue());
//		basePart.setPayAmount(compensateVo.getSumAmt().doubleValue());
		// basePart.setBankName(bankName);
		// basePart.setAccountNumber(accountNumber);
		// basePart.setAccountName(accountName);
		basePart.setCompensateNo(compeNo);

		bodyVo.setBasePart(basePart);
		bodyVo.setPayDataList(payDataList);
		if(SendPlatformUtil.isMor(policyViewService.getPolicyInfoByType(registNo,"2"))){
			returnLog = controller.callPlatform(bodyVo);
		}
		return returnLog;
	}
	
	private void setBiPaymentPayData(PrpLCompensateVo compensateVo,List<BiPaymentPayDataVo> payDataList){
		String registNo = compensateVo.getRegistNo();
		for(PrpLPaymentVo paymentVo:compensateVo.getPrpLPayments()){
			if(!"1".equals(paymentVo.getPayStatus())){
				continue;
			}
			PrpLPayCustomVo payCustomVo = managerService.findPayCustomVoById(paymentVo.getPayeeId());
			BiPaymentPayDataVo payData = new BiPaymentPayDataVo();
			payData.setAccountNumber(payCustomVo.getAccountNo());
			payData.setBankName(payCustomVo.getBankOutlets());
//			payData.setBankName(payCustomVo.getBankName());
			payData.setAccountName(payCustomVo.getPayeeName());
			String certifyType = payCustomVo.getCertifyType();
             SysCodeDictVo sysdicVo = codeTranService.findTransCodeDictVo ("CertifyType",certifyType); 
             payData.setCentiType(sysdicVo==null?"99":sysdicVo.getProperty1());
			//CENTI_CODE	字符	20	CY	赔款收款人身份证/组织机构代码
			String certifyNo = payCustomVo.getCertifyNo();
			if (StringUtils.isNotBlank(certifyNo) && certifyNo.length() > 20) {
				certifyNo = certifyNo.substring(0,20);
			}
			payData.setCentiCode(certifyNo);
			
			String recoveryCode = "";
			if(PayFlagType.CLEAR_PAY.equals(paymentVo.getPayFlag())){//清付
				PrpLCheckDutyVo dutyVo = checkTaskService.findCheckDuty(registNo,Integer.parseInt(paymentVo.getItemId()));
				PrpLPlatLockVo platLockVo = platLockService.findPrpLPlatLockVoByLicenseNo(compensateVo.getRegistNo(),
						compensateVo.getPolicyNo(),PayFlagType.CLEAR_PAY,dutyVo.getLicenseNo());
				for(PrpLRecoveryOrPayVo recoveryOrPayVo : platLockVo.getPrpLRecoveryOrPays()){
					if(recoveryOrPayVo.getCompensateNo().equals(compensateVo.getCompensateNo())){
						BiPaymentPayDataVo payData1 = new BiPaymentPayDataVo();
						Beans.copy().from(payData).to(payData1);
						recoveryCode = recoveryOrPayVo.getRecoveryCode();
						payData1.setRecoveryCode(recoveryOrPayVo.getRecoveryCode());
						payData1.setPayAmount(DataUtils.NullToZero(recoveryOrPayVo.getRecoveryOrPayAmount()).doubleValue());
//						payData.setPayDate(paymentVo.getPayTime());
						payData1.setPayDate(new Date());
						payDataList.add(payData1);
					}
				}
				
			}else if(PayFlagType.INSTEAD_PAY.equals(paymentVo.getPayFlag())){//代付/追偿
			//	PrpLCheckDutyVo dutyVo = checkTaskService.findCheckDuty(registNo,Integer.parseInt(paymentVo.getItemId()));
//				PrpLPlatLockVo platLockVo = platLockService.findPrpLPlatLockVoByLicenseNo(compensateVo.getRegistNo(),
//						compensateVo.getPolicyNo(),PayFlagType.INSTEAD_PAY,dutyVo.getLicenseNo());
				List<PrpLPlatLockVo> platLockVos = platLockService.findPrpLPlatLockVoList(registNo,compensateVo.getPolicyNo(),PayFlagType.INSTEAD_PAY);
				List<PrpLRecoveryOrPayVo> payVoList = new ArrayList<PrpLRecoveryOrPayVo>();
				for (PrpLPlatLockVo platLockVo: platLockVos) {
					PrpLRecoveryOrPayVo tempPayVo = platLockService.findRecOrPayByLockId(platLockVo.getId());
					payVoList.add(tempPayVo);
				}
				double amt = 0d;
				for(PrpLRecoveryOrPayVo recoveryOrPayVo : payVoList){
					if(recoveryOrPayVo.getCompensateNo().equals(compensateVo.getCompensateNo())){
						BiPaymentPayDataVo recoveryPayData = new BiPaymentPayDataVo();
						Beans.copy().from(payData).to(recoveryPayData);
						recoveryCode = recoveryOrPayVo.getRecoveryCode();
						amt = DataUtils.NullToZero(recoveryOrPayVo.getRecoveryOrPayAmount()).doubleValue();
						recoveryPayData.setRecoveryCode(recoveryOrPayVo.getRecoveryCode());
						recoveryPayData.setPayAmount(DataUtils.NullToZero(recoveryOrPayVo.getRecoveryOrPayAmount()).doubleValue());
						// payData.setPayDate(paymentVo.getPayTime());
						recoveryPayData.setPayDate(new Date());
						payDataList.add(recoveryPayData);
					}
				}
			}else{//自付
//				BiPaymentPayDataVo payData3 = new BiPaymentPayDataVo();
//				Beans.copy().from(payData).to(payData3);
				payData.setPayAmount(DataUtils.NullToZero(paymentVo.getSumRealPay()).doubleValue());
//				payData.setPayDate(paymentVo.getPayTime());
				payData.setPayDate(new Date());
				payDataList.add(payData);
			}
//			payDataList.add(payData);
		}
	}
	
}
