/******************************************************************************
 * CREATETIME : 2016年5月27日 上午9:25:19
 ******************************************************************************/
package ins.sino.claimcar.platform.service;

import ins.platform.common.service.facade.CodeTranService;
import ins.platform.saa.util.CodeConstants.AccLiabilityCIPla;
import ins.platform.saa.util.CodeConstants.ClaimFeeTypeBIPla;
import ins.platform.saa.util.CodeConstants.ClaimFeeTypeCIPla;
import ins.platform.saa.util.CodeConstants.ClaimTypeCIPla;
import ins.platform.saa.util.CodeConstants.RecoveryOrPayTypeBIPla;
import ins.platform.utils.DataUtils;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.ClaimType;
import ins.sino.claimcar.CodeConstants.CompCaseType;
import ins.sino.claimcar.CodeConstants.DutyType;
import ins.sino.claimcar.CodeConstants.FeeTypeCode;
import ins.sino.claimcar.CodeConstants.KINDCODE;
import ins.sino.claimcar.CodeConstants.LossTypeCarComp;
import ins.sino.claimcar.CodeConstants.LossTypePersComp;
import ins.sino.claimcar.CodeConstants.NotPayCause;
import ins.sino.claimcar.CodeConstants.PayFlagType;
import ins.sino.claimcar.CodeConstants.PolicyType;
import ins.sino.claimcar.CodeConstants.PropType;
import ins.sino.claimcar.CodeConstants.YN01;
import ins.sino.claimcar.carplatform.constant.RequestType;
import ins.sino.claimcar.carplatform.controller.PlatformController;
import ins.sino.claimcar.carplatform.service.CiClaimPlatformLogService;
import ins.sino.claimcar.carplatform.service.CiClaimPlatformTaskService;
import ins.sino.claimcar.carplatform.util.PlatformFactory;
import ins.sino.claimcar.carplatform.vo.CiClaimPlatformLogVo;
import ins.sino.claimcar.carplatform.vo.CiClaimPlatformTaskVo;
import ins.sino.claimcar.certify.service.CertifyPubService;
import ins.sino.claimcar.certify.vo.PrpLCertifyMainVo;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.PrpLCheckDutyVo;
import ins.sino.claimcar.check.vo.PrpLCheckVo;
import ins.sino.claimcar.claim.service.ClaimCancelService;
import ins.sino.claimcar.claim.services.CompensateService;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claim.vo.PrpLLossItemVo;
import ins.sino.claimcar.claim.vo.PrpLLossPersonFeeVo;
import ins.sino.claimcar.claim.vo.PrpLLossPersonVo;
import ins.sino.claimcar.claim.vo.PrpLLossPropVo;
import ins.sino.claimcar.claim.vo.PrpLPadPayMainVo;
import ins.sino.claimcar.claim.vo.PrpLcancelTraceVo;
import ins.sino.claimcar.constant.Risk;
import ins.sino.claimcar.endcase.service.EndCaseService;
import ins.sino.claimcar.endcase.vo.PrpLEndCaseVo;
import ins.sino.claimcar.lossperson.service.PersTraceDubboService;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersInjuredVo;
import ins.sino.claimcar.pay.service.PadPayPubService;
import ins.sino.claimcar.platform.util.SendPlatformUtil;
import ins.sino.claimcar.platform.vo.BiEndCaseBasePartVo;
import ins.sino.claimcar.platform.vo.BiEndCaseClaimCoverDataVo;
import ins.sino.claimcar.platform.vo.BiEndCaseFraudTypeDataVo;
import ins.sino.claimcar.platform.vo.BiEndCaseRecoveryOrPayDataVo;
import ins.sino.claimcar.platform.vo.BiEndCaseReqBodyVo;
import ins.sino.claimcar.platform.vo.CiEndCaseBasePartVo;
import ins.sino.claimcar.platform.vo.CiEndCaseClaimCoverDataVo;
import ins.sino.claimcar.platform.vo.CiEndCaseFraudTypeDataVo;
import ins.sino.claimcar.platform.vo.CiEndCaseRecoveryOrPayDataVo;
import ins.sino.claimcar.platform.vo.CiEndCaseReqBodyVo;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLCItemKindVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.subrogation.service.SubrogationService;
import ins.sino.claimcar.subrogation.vo.PrpLPlatLockVo;
import ins.sino.claimcar.subrogation.vo.PrpLRecoveryOrPayVo;
import ins.sino.claimcar.subrogation.vo.PrpLSubrogationMainVo;
import ins.sino.claimcar.subrogation.vo.PrpLSubrogationPersonVo;

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
 * 结案
 * @author ★XMSH
 */
@Service("sendEndCaseToPlatformService")
public class SendEndCaseToPlatformService {

	private Logger logger = LoggerFactory.getLogger(SendEndCaseToPlatformService.class);

	@Autowired
	private CompensateService compensateService;
	@Autowired
	private PadPayPubService padPayPubService;
	@Autowired
	private CheckTaskService checkTaskService;
	@Autowired
	private PersTraceDubboService persTraceDubboService;
	@Autowired
	private SubrogationService subrogationService;
	@Autowired
	CiClaimPlatformLogService platformLogService;
	@Autowired
	PolicyViewService policyViewService;
	@Autowired
	EndCaseService endCaseService;
	@Autowired
	CodeTranService codeTranService;
	@Autowired
	private ClaimCancelService claimCancelService;
	@Autowired
	private CertifyPubService certifyPubService;
	@Autowired
	private KindCodeTranService kindCodeTranService;
	@Autowired
	private RegistQueryService registQueryService;
	@Autowired
	private CiClaimPlatformTaskService ciClaimPlatformTaskService;
	/**
	 * 发送-- 结案，调用方法
	 * @param bussNo
	 * @param reqType
	 * @throws Exception 
	 */
	public void sendEndCaseToPlatform(PrpLEndCaseVo endVo,CiClaimPlatformTaskVo platformTaskVo){
		logger.info("结案送车险平台-计算书号："+endVo.getCompensateNo()+"---报案号："+endVo.getRegistNo());
		String registNo = endVo.getRegistNo();
		PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
		// 联共保 从共，从联不交互平台
		if(CodeConstants.GBFlag.MINORCOMMON.equals(registVo.getIsGBFlag())||CodeConstants.GBFlag.MINORRELATION.equals(registVo.getIsGBFlag())){
			return;
		}
		String compeNo = endVo.getCompensateNo();
		if(Risk.DQZ.equals(endVo.getRiskCode())){
			sendEndCaseCIToPlatform(registNo,compeNo,endVo.getEndCaseDate(),endVo,platformTaskVo);
		}else{
			sendEndCaseBIToPlatform(registNo,compeNo,endVo.getEndCaseDate(),endVo,platformTaskVo);
		}
	}
	
	
	//全国 - 交强结案送平台
	private void sendEndCaseCIToPlatform(String registNo,String compensateNo,Date endDate,PrpLEndCaseVo endVo,CiClaimPlatformTaskVo platformTaskVo){
		List<PrpLCompensateVo> compensateVoList = findCompensateVoList(endVo.getClaimNo(),endVo.getRiskCode());
		String comCode = policyViewService.findPolicyComCode(registNo,PolicyType.POLICY_DZA);
		CiClaimPlatformLogVo logVo = platformLogService.findLogByBussNo
				(RequestType.RegistInfoCI.getCode(),registNo,comCode);
		if (logVo == null) {
			logger.error("交强结案上传全国平台失败！该案件理赔编码不存在或报案未成功上传平台！");
		}
		//投保确认码
		String validNo = policyViewService.findValidNo(registNo,PolicyType.POLICY_DZA);
		logger.info("投保确认码："+validNo);
		String claimSeqNo = logVo==null?"":logVo.getClaimSeqNo();
		logger.info("理赔编码："+claimSeqNo);
		
		
		PrpLCompensateVo compensateVo = compensateService.findCompByPK(compensateNo);
		if(compensateVo==null){
			compensateVo = new PrpLCompensateVo();
			compensateVo.setSumAmt(BigDecimal.ZERO);
			compensateVo.setSumFee(BigDecimal.ZERO);
			if(compensateVoList!=null&&compensateVoList.size()>0){
				compensateVo.setCaseType(compensateVoList.get(0).getCaseType());
			}
		}
		PrpLCheckDutyVo checkDutyVo = checkTaskService.findCheckDuty(registNo,1);
		List<PrpLDlossPersInjuredVo> persInjuredVoList = persTraceDubboService.findPersInjuredByRegistNo(registNo);

		PlatformController controller = PlatformFactory.getInstance(comCode,RequestType.EndCaseCI);

		//Body
		CiEndCaseReqBodyVo bodyVo = new CiEndCaseReqBodyVo();
		//BasePart
		CiEndCaseBasePartVo basePart = new CiEndCaseBasePartVo();
		basePart.setConfirmSequenceNo(validNo);
		basePart.setClaimCode(claimSeqNo);
		basePart.setReportNo(registNo);
		if(endDate == null){
			endDate = new Date();
		}
		//核赔通过送平台时间和结案通过送平台时间相同，把结案完成送平台时间加上10秒
		endDate.setTime(endDate.getTime() + 1 * 10 * 1000);// 加上1分钟以后的时间
		basePart.setEndcaseDate(endDate);
		basePart.setInsured(YN01.Y);
		
		String claimType = ClaimTypeCIPla.Nomal_case;
		if(DutyType.CIINDEMDUTY_N.equals(checkDutyVo.getCiDutyFlag())){// 无责
			claimType = ClaimTypeCIPla.Noduty_case;
		}
		// 是否存在垫付
		PrpLPadPayMainVo padPayMainVo = padPayPubService.findPadPay(registNo);
		if( padPayMainVo != null) {
			claimType = ClaimTypeCIPla.PadPay_case;
		}
		basePart.setClaimType(claimType);
		String accidentDeath = YN01.N;
		if(DutyType.CIINDEMDUTY_Y.equals(checkDutyVo.getCiDutyFlag())){
			basePart.setLiabilityAmount(AccLiabilityCIPla.FULL_DUTY);
			if(persInjuredVoList!=null&&persInjuredVoList.size()>0){
				int carSerialno =1;
				for(PrpLDlossPersInjuredVo injuredVo:persInjuredVoList){
					if(FeeTypeCode.MEDICAL_EXPENSES.equals(injuredVo.getWoundCode()) && carSerialno!=injuredVo.getSerialNo()){
						accidentDeath = YN01.Y;
						break;
					}
				}
			}
		}else{
			basePart.setLiabilityAmount(AccLiabilityCIPla.NO_DUTY);
		}
		basePart.setAccidentDeath(accidentDeath);
		basePart.setPaySelfFlag(CompCaseType.SELF_CASE.equals(compensateVo.getCaseType()) ? YN01.Y : YN01.N);
		basePart.setPayCause("");//垫付原因
		// 重开后的案子再次结案需赔案结案校验码
		List<PrpLEndCaseVo> endCaseVoList = endCaseService.searchEndCaseVo(registNo, endVo.getClaimNo());
		if(endCaseVoList.size()>1){
			basePart.setClaimConfirmCode(endCaseVoList.get(1).getConfirmCode());
		}
		basePart.setAccidentType("100");//必传 默认-100-交通事故类
		basePart.setIsRefuseCase(YN01.N);
		
		BigDecimal otherFee = BigDecimal.ZERO;
		if(compensateVoList!=null){
			for (PrpLCompensateVo prpLCompensateVo : compensateVoList) {
				otherFee = otherFee.add(DataUtils.NullToZero(prpLCompensateVo.getSumFee()));
			}
		}	
		
		PrpLCertifyMainVo certifyMainVo = certifyPubService.findPrpLCertifyMainVoByRegistNo(registNo);
		if(certifyMainVo != null){
			String isJQFraud=certifyMainVo.getIsJQFraud()!=null?certifyMainVo.getIsJQFraud():"0";
		    basePart.setIsRefuseCase(isJQFraud.toString()); //是否为拒赔案件
		    if(YN01.Y.equals(certifyMainVo.getIsJQFraud())){
		    	if(StringUtils.isNotBlank(certifyMainVo.getFraudRefuseReason())){
					basePart.setRefuseCause(certifyMainVo.getFraudRefuseReason());
				}else if(StringUtils.isNotBlank(certifyMainVo.getNewNotpaycause()) && !NotPayCause.QT.equals(certifyMainVo.getNewNotpaycause())){
					basePart.setRefuseCause(codeTranService.findCodeName("DZNOTPAY",certifyMainVo.getNewNotpaycause()));//拒赔原因描述
				}else if(NotPayCause.QT.equals(certifyMainVo.getNewNotpaycause())){
					basePart.setRefuseCause(certifyMainVo.getOthernotPaycause());
				}
		    }
			
			
			//basePart.setRefuseCause(certifyMainVo.getFraudRefuseReason());//拒赔原因描述
			if(YN01.Y.equals(certifyMainVo.getIsFraud())){
				 basePart.setFraudLogo(certifyMainVo.getFraudLogo());
				 basePart.setFraudRecoverAmount(certifyMainVo.getFraudRecoverAmount().doubleValue());
				 List<CiEndCaseFraudTypeDataVo> fraudTypeList = new ArrayList<CiEndCaseFraudTypeDataVo>();
					CiEndCaseFraudTypeDataVo fraudType = new CiEndCaseFraudTypeDataVo();
					fraudType.setFraudType(certifyMainVo.getFraudType());
					fraudTypeList.add(fraudType);
					bodyVo.setFraudTypeDataList(fraudTypeList);
			}
		}
		
	

		List<CiEndCaseClaimCoverDataVo> claimCoverList = new ArrayList<CiEndCaseClaimCoverDataVo>();
		BigDecimal noresInsteadAmount = BigDecimal.ZERO;//无责代赔金额
		BigDecimal claimAmount = BigDecimal.ZERO;//总赔款金额
		if(compensateVoList != null && !compensateVoList.isEmpty()){
			for(PrpLCompensateVo compensate : compensateVoList){
//				claimAmount = claimAmount.add(DataUtils.NullToZero(compensate.getSumAmt()));
				//车
				List<PrpLLossItemVo> lossItemVoList = compensate.getPrpLLossItems();
				if(lossItemVoList != null && !lossItemVoList.isEmpty()){
					for(PrpLLossItemVo itemVo : lossItemVoList){// 车
						if(PayFlagType.NODUTY_INSTEAD_PAY.equals(itemVo.getPayFlag())){//无责代赔
							noresInsteadAmount = noresInsteadAmount.add(DataUtils.NullToZero(itemVo.getSumRealPay()));
						}else{//如果车是无责代赔，则不添加到claimCoverData
							CiEndCaseClaimCoverDataVo claimCoverData = new CiEndCaseClaimCoverDataVo();
							// 2 清付 3 自付
							if(CodeConstants.PayFlagType.CLEAR_PAY.equals(itemVo.getPayFlag())){
								claimCoverData.setRecoveryOrPayFlag(PayFlagType.CLEAR_PAY);
							}else{
								claimCoverData.setRecoveryOrPayFlag(PayFlagType.COMPENSATE_PAY);
							}
							claimCoverData.setCoverageCode("100");//默认-100-交强险
							if(YN01.Y.equals(checkDutyVo.getCiDutyFlag())){// 有责
								claimCoverData.setClaimFeeType(ClaimFeeTypeCIPla.PROPLOSS);
								claimCoverData.setLiabilityRate("1");
							}else{// 车无责
								claimCoverData.setClaimFeeType(ClaimFeeTypeCIPla.PROPLOSS_NODUTY);
								claimCoverData.setLiabilityRate("0");
							}
							claimCoverData.setClaimAmount(DataUtils.NullToZero(itemVo.getSumRealPay()).doubleValue());
							claimAmount = claimAmount.add(DataUtils.NullToZero(itemVo.getSumRealPay()));
							Double salFee = LossTypeCarComp.THIRDPARTY_CAR_LOSS.equals(itemVo.getLossType()) ? 
									itemVo.getRescueFee().doubleValue() : 0d;
							claimCoverData.setSalvageFee(salFee);
							claimCoverList.add(claimCoverData);
						}
					}
					
					//财产
					List<PrpLLossPropVo> lossPropVoList = compensate.getPrpLLossProps();
					if(lossPropVoList != null && !lossPropVoList.isEmpty()){
						for(PrpLLossPropVo propVo : lossPropVoList){
							CiEndCaseClaimCoverDataVo claimCoverData = new CiEndCaseClaimCoverDataVo();
							claimCoverData.setRecoveryOrPayFlag(ClaimType.EACHHIT_SELFLOSS_CICASE_SUB.equals(compensateVo.getCaseType()) ? 
																	PayFlagType.CLEAR_PAY : PayFlagType.COMPENSATE_PAY);
							claimCoverData.setCoverageCode("100");//默认-100-交强险

							if(YN01.Y.equals(checkDutyVo.getCiDutyFlag())){// 有责
								claimCoverData.setClaimFeeType(ClaimFeeTypeCIPla.PROPLOSS);
								claimCoverData.setLiabilityRate("1");
							}else{// 无责
								claimCoverData.setClaimFeeType(ClaimFeeTypeCIPla.PROPLOSS_NODUTY);
								claimCoverData.setLiabilityRate("0");
							}
							claimCoverData.setClaimAmount(DataUtils.NullToZero(propVo.getSumRealPay()).doubleValue());
							claimAmount = claimAmount.add(DataUtils.NullToZero(propVo.getSumRealPay()));
							Double salFee = "3".equals(propVo.getLossType()) 
									? propVo.getRescueFee().doubleValue() : 0d;
							claimCoverData.setSalvageFee(salFee);
							claimCoverList.add(claimCoverData);
						}
					}
					
					//人
					List<PrpLLossPersonVo> lossPersoVoList = compensate.getPrpLLossPersons();
					if(lossPersoVoList != null && !lossPersoVoList.isEmpty()){
						for(PrpLLossPersonVo personVo : lossPersoVoList){// 人
							for(PrpLLossPersonFeeVo feeVo:personVo.getPrpLLossPersonFees()){
								CiEndCaseClaimCoverDataVo claimCoverData = new CiEndCaseClaimCoverDataVo();
								claimCoverData.setRecoveryOrPayFlag(ClaimType.EACHHIT_SELFLOSS_CICASE_SUB.equals(compensateVo.getCaseType()) ? 
																		PayFlagType.CLEAR_PAY : PayFlagType.COMPENSATE_PAY);
								claimCoverData.setCoverageCode("100");

								if(YN01.Y.equals(checkDutyVo.getCiDutyFlag())){// 有责
									claimCoverData.setClaimFeeType(LossTypePersComp.PERSON_LOSS_DEATHDIS.equals(feeVo.getLossItemNo()) ? 
											ClaimFeeTypeCIPla.DEATHLOSS : ClaimFeeTypeCIPla.MEDILOSS);
									claimCoverData.setLiabilityRate("1");
								}else{// 无责
									claimCoverData.setClaimFeeType(LossTypePersComp.PERSON_LOSS_DEATHDIS.equals(feeVo.getLossItemNo()) ? 
											ClaimFeeTypeCIPla.DEATHLOSS_NODUTY : ClaimFeeTypeCIPla.MEDILOSS_NODUTY);
									claimCoverData.setLiabilityRate("0");
								}
								claimCoverData.setClaimAmount(DataUtils.NullToZero(feeVo.getFeeRealPay()).doubleValue());
								claimAmount = claimAmount.add(DataUtils.NullToZero(feeVo.getFeeRealPay()));
								claimCoverList.add(claimCoverData);
							}
						}
					}
				}
			}
//			claimAmount = claimAmount.subtract(noresInsteadAmount);//总赔款金额不包含无责代赔金额
		}
		
		// 追偿/清付信息列表
		List<CiEndCaseRecoveryOrPayDataVo> recoveryOrPayList = new ArrayList<CiEndCaseRecoveryOrPayDataVo>();
		List<PrpLPlatLockVo> prpLPlatLockListVos = subrogationService.findPlatLockVoByPayFlag(registNo);
		int i = 1;
		if(prpLPlatLockListVos!=null&&prpLPlatLockListVos.size()>0){
			for(PrpLPlatLockVo prpLPlatLockVo:prpLPlatLockListVos){
				if(prpLPlatLockVo.getPrpLRecoveryOrPays()!=null&&
						prpLPlatLockVo.getPrpLRecoveryOrPays().size()>0){
					for(PrpLRecoveryOrPayVo prpLRecoveryOrPayVo:prpLPlatLockVo.getPrpLRecoveryOrPays()){
						if(compensateVo.getCompensateNo().equals(prpLRecoveryOrPayVo.getCompensateNo())){
							CiEndCaseRecoveryOrPayDataVo recoveryOrPayData = new CiEndCaseRecoveryOrPayDataVo();
							Integer serNo = prpLRecoveryOrPayVo.getSerialNo();
							if(serNo == null){
								serNo = i; i++;
							}
							recoveryOrPayData.setSerialNo(serNo.toString());
							recoveryOrPayData.setRecoveryOrPayFlag(prpLPlatLockVo.getRecoveryOrPayFlag());
							recoveryOrPayData.setRecoveryOrPayType(prpLPlatLockVo.getRecoveryOrPayType());
							// recoveryOrPayData.setRecoveryOrPayMan(recoveryOrPayMan);
							recoveryOrPayData.setRecoveryCode(prpLRecoveryOrPayVo.getRecoveryCode());
							recoveryOrPayData.setRecoveryOrPayAmount(DataUtils.NullToZero
									(prpLRecoveryOrPayVo.getRecoveryOrPayAmount()).doubleValue());

							recoveryOrPayList.add(recoveryOrPayData);
						}
					}
				}
			}
		}
		
		// 如果交强险的零赔付，加入一条零数据
		if (claimCoverList.size() == 0) {
			CiEndCaseClaimCoverDataVo claimCoverData = new CiEndCaseClaimCoverDataVo();
			claimCoverData.setClaimAmount(claimAmount.doubleValue());
			claimCoverData.setClaimFeeType(ClaimFeeTypeCIPla.PROPLOSS);//交强财产损失
			claimCoverData.setCoverageCode("100");
			claimCoverData.setLiabilityRate("0");
			claimCoverData.setRecoveryOrPayFlag(PayFlagType.COMPENSATE_PAY);
			claimCoverData.setSalvageFee(0.0);
			claimCoverList.add(claimCoverData);
		}
		
		basePart.setNoresInsteadAmount(noresInsteadAmount.doubleValue());
		//修复结案欺诈类型为 欺诈放弃索赔或者为欺诈拒绝赔付时,赔款金额必须为0
		if(YN01.Y.equals(certifyMainVo.getIsFraud()) && (CodeConstants.Fraudlogo.Fraudlogo_01.equals(certifyMainVo.getFraudLogo()) || CodeConstants.Fraudlogo.Fraudlogo_02.equals(certifyMainVo.getFraudLogo())) ){
			basePart.setClaimAmount(0.00);
		 }else{
			basePart.setClaimAmount(claimAmount.setScale(4,BigDecimal.ROUND_HALF_UP).doubleValue());
		}
		
		bodyVo.setBasePart(basePart);
		bodyVo.setClaimCoverDataList(claimCoverList);
		bodyVo.setRecoveryOrPayDataList(recoveryOrPayList);

		if(SendPlatformUtil.isMor(policyViewService.getPolicyInfoByType(registNo,"1"))){
			CiClaimPlatformLogVo endCaseLogVo = controller.callPlatform(bodyVo,platformTaskVo);
			if(YN01.Y.equals(endCaseLogVo.getStatus())){
				endCaseService.saveConfirmCode(endVo.getId(), endCaseLogVo.getRemark());
			}
		}
	}
	
	//获取有效的理算数据
	private List<PrpLCompensateVo> findCompensateVoList(String claimNo,String riskCode){
		List<PrpLCompensateVo> list = new ArrayList<PrpLCompensateVo>();
		List<PrpLCompensateVo> temp = compensateService.findCompensateByClaimno(claimNo,"N");
		if(temp != null && !temp.isEmpty()){
			for(PrpLCompensateVo t : temp){
				if(StringUtils.isNotBlank(riskCode) && riskCode.equals(t.getRiskCode()) ){
					list.add(t);
				}
			}
		}
		return list;
	}

	//全国 - 商业结案送平台
	private void sendEndCaseBIToPlatform(String registNo,String compensateNo,Date endCaseDate,PrpLEndCaseVo endVo,CiClaimPlatformTaskVo platformTaskVo){
		List<PrpLCompensateVo> compensateVoList = findCompensateVoList(endVo.getClaimNo(),endVo.getRiskCode());
//		List<PrpLCompensateVo> compensateVoList = compensateService.findCompListByClaimNo(endVo.getClaimNo(),"N");
		String comCode = policyViewService.findPolicyComCode(registNo,PolicyType.POLICY_DAA);
		CiClaimPlatformLogVo logVo = platformLogService.findLogByBussNo
				(RequestType.RegistInfoBI.getCode(),registNo,comCode);
		if (logVo == null) {
			logger.error("商业结案上传全国平台失败！该案件理赔编码不存在或报案未成功上传平台！");
		}
		String claimSeqNo = logVo == null ? "" : logVo.getClaimSeqNo();
		//投保确认码
		String validNo = policyViewService.findValidNo(registNo,PolicyType.POLICY_DAA);
		logger.debug("投保确认码："+validNo);
		PrpLCompensateVo compensateVo = compensateService.findCompByPK(compensateNo);
		PrpLCheckVo checkVo = checkTaskService.findCheckVoByRegistNo(registNo);
		if(compensateVo==null){
			compensateVo = new PrpLCompensateVo();
			// 理算冲销核赔0结案没有计算书数据  新建一个并按照案件信息赋值
			compensateVo.setSumAmt(BigDecimal.ZERO);
			compensateVo.setSumFee(BigDecimal.ZERO);
			if(compensateVoList!=null&&compensateVoList.size()>0){
				compensateVo.setCaseType(compensateVoList.get(0).getCaseType());
				compensateVo.setAllLossFlag(compensateVoList.get(0).getAllLossFlag());
			}
			compensateVo.setClaimNo(endVo.getClaimNo());
			compensateVo.setRiskCode(endVo.getRiskCode());
			
		}
		PrpLCheckDutyVo checkDutyVo = checkTaskService.findCheckDuty(registNo,1);
//		List<PrpLDlossPersInjuredVo> persInjuredVoList = persTraceDubboService.findPersInjuredByRegistNo(registNo);

		PlatformController controller = PlatformFactory.getInstance(comCode,RequestType.EndCaseBI);

		//Body
		BiEndCaseReqBodyVo bodyVo = new BiEndCaseReqBodyVo();
		
		//BasePart
		BiEndCaseBasePartVo basePart = new BiEndCaseBasePartVo();
		basePart.setClaimSequenceNo(claimSeqNo);
		basePart.setConfirmSequenceNo(validNo);
		basePart.setClaimNotificationNo(registNo);
		PrpLCertifyMainVo certifyMainVo = certifyPubService.findPrpLCertifyMainVoByRegistNo(registNo);
		if(certifyMainVo != null){
			String isSYFraud=certifyMainVo.getIsSYFraud()!=null?certifyMainVo.getIsSYFraud():"0";
			basePart.setIsRefuseCase(isSYFraud); //是否为拒赔案件
			if(YN01.Y.equals(certifyMainVo.getIsSYFraud())){
				if(StringUtils.isNotBlank(certifyMainVo.getFraudRefuseReason())){
					basePart.setRefuseCause(certifyMainVo.getFraudRefuseReason());
				}else if(StringUtils.isNotBlank(certifyMainVo.getNewNotpaycause()) && !NotPayCause.QT.equals(certifyMainVo.getNewNotpaycause())){
					basePart.setRefuseCause(codeTranService.findCodeName("DZNOTPAY",certifyMainVo.getNewNotpaycause()));//拒赔原因描述
				}else if(NotPayCause.QT.equals(certifyMainVo.getNewNotpaycause())){
					basePart.setRefuseCause(certifyMainVo.getOthernotPaycause());
				}
			}
			
			
			if(YN01.Y.equals(certifyMainVo.getIsFraud())){
				 basePart.setFraudLogo(certifyMainVo.getFraudLogo());
				 basePart.setFraudRecoverAmount(certifyMainVo.getFraudRecoverAmount().doubleValue());
				 List<BiEndCaseFraudTypeDataVo> fraudTypeList = new ArrayList<BiEndCaseFraudTypeDataVo>();
				 BiEndCaseFraudTypeDataVo fraudType = new BiEndCaseFraudTypeDataVo();
					fraudType.setFraudType(certifyMainVo.getFraudType());
					fraudTypeList.add(fraudType);
					bodyVo.setFraudTypeData(fraudTypeList);
			}
		}
//		//修复结案欺诈类型为 欺诈放弃索赔或者为欺诈拒绝赔付时,赔款金额必须为0
//		if(YN01.Y.equals(certifyMainVo.getIsFraud()) && (CodeConstants.Fraudlogo.Fraudlogo_01.equals(certifyMainVo.getFraudLogo()) || CodeConstants.Fraudlogo.Fraudlogo_02.equals(certifyMainVo.getFraudLogo())) ){
//			basePart.setClaimAmount(0.00);
//		}else{
//			basePart.setClaimAmount(calculateAmt(compensateVoList));
//		}
		
		if(endCaseDate == null){
			endCaseDate = new Date();
		}
		//核赔通过送平台时间和结案通过送平台时间相同，把结案完成送平台时间加上10秒
		endCaseDate.setTime(endCaseDate.getTime() + 1 * 10 * 1000);// 加上1分钟以后的时间
		basePart.setClaimCloseTime(endCaseDate);
		basePart.setIsInsured(YN01.Y);

		String claimType = ClaimTypeCIPla.Nomal_case;
		if(DutyType.CIINDEMDUTY_N.equals(checkDutyVo.getCiDutyFlag())){// 无责
			claimType = ClaimTypeCIPla.Noduty_case;
		}
		// 是否存在垫付
		PrpLPadPayMainVo padPayMainVo = padPayPubService.findPadPay(registNo);
		if (padPayMainVo != null) {
			claimType = ClaimTypeCIPla.PadPay_case;
		}
		basePart.setClaimType(claimType);
		basePart.setPayCause("");//垫付原因
		
		
		basePart.setClaimConfirmCode("");
		basePart.setAccidentType("100");//必传 默认-100-交通事故类
		basePart.setDirectClaimAmount(compensateVo.getSumFee().doubleValue());
		basePart.setIsTotalLoss(checkVo.getLossType());
		
		List<PrpLEndCaseVo> endCaseVoList = endCaseService.searchEndCaseVo(registNo, endVo.getClaimNo());
		if(endCaseVoList.size()>1){
			basePart.setClaimConfirmCode(endCaseVoList.get(1).getConfirmCode());
		}
		BigDecimal specialAmount = BigDecimal.ZERO;//特约总金额
		BigDecimal claimAmount = BigDecimal.ZERO;//总赔款金额
		List<BiEndCaseClaimCoverDataVo> claimCoverList = new ArrayList<BiEndCaseClaimCoverDataVo>();
		if(compensateVoList != null && !compensateVoList.isEmpty()){
			for(PrpLCompensateVo compensate : compensateVoList){
				
				// 车
				List<PrpLLossItemVo> lossItemVoList = compensate.getPrpLLossItems();
				if(lossItemVoList != null && !lossItemVoList.isEmpty()){
					for(PrpLLossItemVo itemVo : lossItemVoList){
						BiEndCaseClaimCoverDataVo claimCoverData = new BiEndCaseClaimCoverDataVo();
						Double rate = itemVo.getDutyRate()==null ? 0 : itemVo.getDutyRate().doubleValue()/100;
						claimCoverData.setLiabilityRate(rate.toString());
						claimCoverData.setLossFeeType(ClaimFeeTypeBIPla.CARLOSS);
						claimCoverData.setCoverageCode(kindCodeTranService.findTransCiCode("00", itemVo.getRiskCode(), "CovergeCode", itemVo.getKindCode()));
						claimCoverData.setClaimAmount(itemVo.getSumRealPay().doubleValue());
						claimAmount = claimAmount.add(DataUtils.NullToZero(itemVo.getSumRealPay()));
						String payFlag = itemVo.getPayFlag();
						if(PayFlagType.NODUTY_INSTEAD_PAY.equals(payFlag)){
							payFlag = PayFlagType.COMPENSATE_PAY;
						}
						claimCoverData.setRecoveryOrPayFlag(payFlag);
						Double salvageFee = "3".equals(itemVo.getLossType()) 
								? itemVo.getRescueFee().doubleValue() : 0d;
						claimCoverData.setSalvageFee(salvageFee);
						if(KINDCODE.KINDCODE_A1.equals(itemVo.getKindCode())){
				            	String sign = YN01.N;//sign附加险是否有新增设备险，0无，1有
				            	if(compensateVo.getPrpLLossProps()!=null && compensateVo.getPrpLLossProps().size()>0){
				            		for(PrpLLossPropVo propVo : compensateVo.getPrpLLossProps()){
				            			if(PropType.OTHLOSS.equals(propVo.getPropType())){
				            				if(KINDCODE.KINDCODE_X3.equals(propVo.getKindCode())){
				            					sign=YN01.Y;
				            					break;
				            				}
				            			}
				            		}
				            	}
				            	if(YN01.N.equals(sign)){
				            		claimCoverData.setIsDeviceItem(YN01.N);
				            	}else{
				            		claimCoverData.setIsDeviceItem(YN01.Y);
				            	}
				            }
						 
						claimCoverList.add(claimCoverData);
					}
				}
				
				//财产
				List<PrpLLossPropVo> lossPropVoList = compensate.getPrpLLossProps();
				if(lossPropVoList != null && !lossPropVoList.isEmpty()){
					for(PrpLLossPropVo propVo : lossPropVoList){// 
						if(CodeConstants.KINDCODE.KINDCODE_RS.equals(propVo.getKindCode()) || CodeConstants.KINDCODE.KINDCODE_VS.equals(propVo.getKindCode()) ||
								CodeConstants.KINDCODE.KINDCODE_DS.equals(propVo.getKindCode()) || CodeConstants.KINDCODE.KINDCODE_DC.equals(propVo.getKindCode())){
							specialAmount=specialAmount.add(DataUtils.NullToZero(propVo.getSumRealPay()));
							continue;
						}
						BiEndCaseClaimCoverDataVo claimCoverData = new BiEndCaseClaimCoverDataVo();
						Double rate = propVo.getDutyRate()==null ? 0 : propVo.getDutyRate().doubleValue()/100;
						claimCoverData.setLiabilityRate(rate.toString());
						claimCoverData.setLossFeeType(ClaimFeeTypeBIPla.PROPLOSS);
						claimCoverData.setCoverageCode(kindCodeTranService.findTransCiCode("00", propVo.getRiskCode(), "CovergeCode", propVo.getKindCode()));
						claimCoverData.setClaimAmount(propVo.getSumRealPay().doubleValue());
						claimAmount = claimAmount.add(DataUtils.NullToZero(propVo.getSumRealPay()));
						claimCoverData.setRecoveryOrPayFlag(PayFlagType.COMPENSATE_PAY);
						Double salvageFee = "3".equals(propVo.getLossType()) 
								? propVo.getRescueFee().doubleValue() : 0d;
						claimCoverData.setSalvageFee(salvageFee);
						if(KINDCODE.KINDCODE_X3.equals(propVo.getKindCode())){
							 claimCoverData.setIsDeviceItem(YN01.Y);
						 }
						claimCoverList.add(claimCoverData);
					}
				}
				
				//人
				List<PrpLLossPersonVo> lossPersoVoList = compensate.getPrpLLossPersons();
				if(lossPersoVoList != null && !lossPersoVoList.isEmpty()){
					for(PrpLLossPersonVo personVo : lossPersoVoList){// 
						for(PrpLLossPersonFeeVo feeVo : personVo.getPrpLLossPersonFees()){
							BiEndCaseClaimCoverDataVo claimCoverData = new BiEndCaseClaimCoverDataVo();
							Double rate = feeVo.getDutyRate()==null ? 0 : feeVo.getDutyRate().doubleValue()/100;
							claimCoverData.setLiabilityRate(rate.toString());
							claimCoverData.setLossFeeType(LossTypePersComp.PERSON_LOSS_DEATHDIS.equals(feeVo.getLossItemNo()) ? 
									ClaimFeeTypeBIPla.DEATHLOSS : ClaimFeeTypeBIPla.MEDHLOSS);
							claimCoverData.setCoverageCode(kindCodeTranService.findTransCiCode("00", personVo.getRiskCode(), "CovergeCode", personVo.getKindCode()));
							claimCoverData.setRecoveryOrPayFlag(PayFlagType.COMPENSATE_PAY);
							claimCoverData.setSalvageFee(0d);
							claimCoverData.setClaimAmount(feeVo.getFeeRealPay().doubleValue());
							claimAmount = claimAmount.add(DataUtils.NullToZero(feeVo.getFeeRealPay()));
							claimCoverList.add(claimCoverData);
						}
					}
				}
				//
			}
		}
		
		// 追偿/清付信息列表
		List<BiEndCaseRecoveryOrPayDataVo> recoveryOrPayList = new ArrayList<BiEndCaseRecoveryOrPayDataVo>();
		List<PrpLPlatLockVo> prpLPlatLockListVos = subrogationService.findPlatLockVoByPayFlag(registNo);
		int i = 1;
		if (prpLPlatLockListVos != null && prpLPlatLockListVos.size() > 0) {
			for (PrpLPlatLockVo prpLPlatLockVo : prpLPlatLockListVos) {
				List<PrpLRecoveryOrPayVo> payVoList = prpLPlatLockVo.getPrpLRecoveryOrPays();
				if (payVoList != null && payVoList.size() > 0) {
					for (PrpLRecoveryOrPayVo prpLRecoveryOrPayVo : prpLPlatLockVo.getPrpLRecoveryOrPays()) {
						if(compensateVo.getCompensateNo().equals(prpLRecoveryOrPayVo.getCompensateNo())){
							BiEndCaseRecoveryOrPayDataVo recoveryOrPayData = new BiEndCaseRecoveryOrPayDataVo();
							Integer serNo = prpLRecoveryOrPayVo.getSerialNo();
							if(serNo == null){
								serNo = i; i++;
							}
							recoveryOrPayData.setSerialNo(serNo.toString());
							recoveryOrPayData.setRecoveryOrPayFlag(prpLPlatLockVo.getRecoveryOrPayFlag());
							recoveryOrPayData.setRecoveryOrPayType(prpLPlatLockVo.getRecoveryOrPayType());
							// recoveryOrPayData.setRecoveryOrPayMan(recoveryOrPayMan);
							recoveryOrPayData.setRecoveryCode(prpLRecoveryOrPayVo.getRecoveryCode());
							recoveryOrPayData.setRecoveryOrPayAmount(DataUtils.NullToZero
									(prpLRecoveryOrPayVo.getRecoveryOrPayAmount()).doubleValue());

							recoveryOrPayList.add(recoveryOrPayData);
						}
					}
				}

			}
		}
		
		// 非机动车代位信息组织
		PrpLSubrogationMainVo subMainVo = subrogationService.find(registNo);
		List<PrpLSubrogationPersonVo> personVoList = new ArrayList<PrpLSubrogationPersonVo>();
		if(subMainVo != null && CodeConstants.CommonConst.TRUE.equals(subMainVo.getSubrogationFlag())){
			personVoList = subMainVo.getPrpLSubrogationPersons();
		}
		if (personVoList != null && personVoList.size() > 0) {
			for (PrpLSubrogationPersonVo personVo : personVoList){
				BiEndCaseRecoveryOrPayDataVo recoveryOrPayData = new BiEndCaseRecoveryOrPayDataVo();
				recoveryOrPayData.setSerialNo(i+++"");
				recoveryOrPayData.setRecoveryOrPayFlag(CodeConstants.PayFlagType.INSTEAD_PAY);
				recoveryOrPayData.setRecoveryOrPayType(RecoveryOrPayTypeBIPla.DEATHLOSS);// 致害人
				recoveryOrPayData.setRecoveryOrPayMan(personVo.getName());
				recoveryOrPayData.setRecoveryOrPayAmount(DataUtils.NullToZero(personVo.getThisPaid()).doubleValue());
				recoveryOrPayList.add(recoveryOrPayData);
			}
		}
		
		//修复结案欺诈类型为 欺诈放弃索赔或者为欺诈拒绝赔付时,赔款金额必须为0
		if(YN01.Y.equals(certifyMainVo.getIsFraud()) && (CodeConstants.Fraudlogo.Fraudlogo_01.equals(certifyMainVo.getFraudLogo()) || CodeConstants.Fraudlogo.Fraudlogo_02.equals(certifyMainVo.getFraudLogo())) ){
			basePart.setClaimAmount(0.00);
		}else{
			basePart.setClaimAmount(claimAmount.setScale(4,BigDecimal.ROUND_HALF_UP).doubleValue());
		}

		
		bodyVo.setBasePart(basePart);
		// 如果商业险的零赔付，加入一条零数据
		if(claimCoverList.size()==0){
			BiEndCaseClaimCoverDataVo claimCoverData = new BiEndCaseClaimCoverDataVo();
			claimCoverData.setClaimAmount(calculateAmt(compensateVoList,specialAmount));
			claimCoverData.setLossFeeType(ClaimFeeTypeBIPla.CARLOSS);
			
			//String codeType = !isFeeUpdate(compensateVo.getRiskCode())?"CoverageCodePremium":"CoverageCode";//CoverageCode-否
			String initVal = !isFeeUpdate(compensateVo.getRiskCode())?"0101200":"200";
			
			PrpLCMainVo cMainVo = policyViewService.getPrpLCMainByRegistNoAndPolicyNo(registNo,compensateVo.getPolicyNo());//根据保单号获取唯一保单
			if(cMainVo != null){
				PrpLCItemKindVo kind = cMainVo.getPrpCItemKinds().get(0);
				claimCoverData.setCoverageCode(kindCodeTranService.findTransCiCode("00", compensateVo.getRiskCode(), "CovergeCode", kind.getKindCode()));
			}
			if(StringUtils.isBlank(claimCoverData.getCoverageCode())){
				claimCoverData.setCoverageCode(initVal);
			}
			claimCoverData.setLiabilityRate("1.00");
			claimCoverData.setRecoveryOrPayFlag(PayFlagType.COMPENSATE_PAY);
			claimCoverData.setSalvageFee(0.0);
			claimCoverList.add(claimCoverData);
		}
		bodyVo.setClaimCoverDataList(claimCoverList);
		bodyVo.setRecoveryOrPayDataList(recoveryOrPayList);

		if(SendPlatformUtil.isMor(policyViewService.getPolicyInfoByType(registNo,"2"))){
			CiClaimPlatformLogVo endCaseLogVo = controller.callPlatform(bodyVo,platformTaskVo);
			if(YN01.Y.equals(endCaseLogVo.getStatus())){
				endCaseService.saveConfirmCode(endVo.getId(), endCaseLogVo.getRemark());
			}
		}
		//结案送平台成功，需保存赔案结案校验码，重开赔案后，再次结案送平台需要传该字段
	}
	
	//计算交强或者商业的总赔款金额
	private double calculateAmt(List<PrpLCompensateVo> list, BigDecimal specialAmount){
		BigDecimal sumAmt = new BigDecimal(0);
		if(list != null && !list.isEmpty()){
			for(PrpLCompensateVo compensateVo : list){
				sumAmt = sumAmt.add(DataUtils.NullToZero(compensateVo.getSumAmt()));
			}
		}
		sumAmt = sumAmt.subtract(DataUtils.NullToZero(specialAmount));
		logger.info("===sumAmt==="+sumAmt);
		return sumAmt.setScale(4,BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	/**
	 * 判断是否费改后的险种
	 * <pre></pre>
	 * @param riskCode
	 * @return false-否，true-是
	 * @modified:
	 * ☆Luwei(2016年9月7日 下午5:54:20): <br>
	 */
	private boolean isFeeUpdate(String riskCode){
		boolean returnVal = false;
		if("1201".equals(riskCode)){
			returnVal = true;
		}else if("1202".equals(riskCode)){
			returnVal = true;
		}else if("1203".equals(riskCode)){
			returnVal = true;
		}else if("1204".equals(riskCode)){
			returnVal = true;
		}else if("1205".equals(riskCode)){
			returnVal = true;
		}else{
			returnVal = false;
		}
		return returnVal;
	}
	
	/**
	 * 结案保存平台定时任务表
	 * @param endVo
	 */
	public void savePlatformTask(PrpLEndCaseVo endVo){
		String registNo = endVo.getRegistNo();
		CiClaimPlatformTaskVo platformTaskVo = new CiClaimPlatformTaskVo();
		PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
		PrpLCMainVo prpLCmainVo = policyViewService.getPolicyInfo(endVo.getRegistNo(),endVo.getPolicyNo());
		
		// 联共保 从共，从联不交互平台
		if(CodeConstants.GBFlag.MINORCOMMON.equals(registVo.getIsGBFlag())||CodeConstants.GBFlag.MINORRELATION.equals(registVo.getIsGBFlag())){
			return;
		}
		platformTaskVo.setRegistNo(endVo.getRegistNo());
		platformTaskVo.setBussNo(endVo.getEndCaseNo());
		platformTaskVo.setClaimSeqNo(prpLCmainVo.getClaimSequenceNo());
		if(registVo.getComCode().startsWith("22")){//上海
			platformTaskVo.setTaskLevel(CodeConstants.PlatformTaskLevel_SH.EndCase);
			if(Risk.DQZ.equals(prpLCmainVo.getRiskCode())){
				platformTaskVo.setRequestType(RequestType.EndCaseCI_SH.name());
				platformTaskVo.setRequestName(RequestType.EndCaseCI_SH.getName());
			}else{
				platformTaskVo.setRequestType(RequestType.EndCaseBI_SH.name());
				platformTaskVo.setRequestName(RequestType.EndCaseBI_SH.getName());
			}
		}else{//全国
			platformTaskVo.setTaskLevel(CodeConstants.PlatformTaskLevel_SH.EndCase);
			if(Risk.DQZ.equals(prpLCmainVo.getRiskCode())){
				platformTaskVo.setRequestType(RequestType.EndCaseCI.name());
				platformTaskVo.setRequestName(RequestType.EndCaseCI.getName());
			}else{
				platformTaskVo.setRequestType(RequestType.EndCaseBI.name());
				platformTaskVo.setRequestName(RequestType.EndCaseBI.getName());
			}
		}
		platformTaskVo.setStatus(CodeConstants.platformStatus.None);
		platformTaskVo.setTaskParams(endVo.getEndCaseNo()+","+CiClaimPlatformTaskVo.class.getName());
		platformTaskVo.setRedoTimes(0);
		platformTaskVo.setOperateStatus(CodeConstants.OperateStatus.OFF);
		ciClaimPlatformTaskService.savePlatformTask(platformTaskVo);
	}
}
