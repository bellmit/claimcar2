/******************************************************************************
 * CREATETIME : 2016年5月26日 下午3:52:58
 ******************************************************************************/
package ins.sino.claimcar.platform.service;

import ins.platform.common.service.facade.CodeTranService;
import ins.platform.utils.DataUtils;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.PayFlagType;
import ins.sino.claimcar.carplatform.constant.RequestType;
import ins.sino.claimcar.carplatform.controller.PlatformController;
import ins.sino.claimcar.carplatform.service.CiClaimPlatformLogService;
import ins.sino.claimcar.carplatform.service.CiClaimPlatformTaskService;
import ins.sino.claimcar.carplatform.util.PlatformFactory;
import ins.sino.claimcar.carplatform.vo.CiClaimPlatformLogVo;
import ins.sino.claimcar.carplatform.vo.CiClaimPlatformTaskVo;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.PrpLCheckDutyVo;
import ins.sino.claimcar.claim.services.CompensateService;
import ins.sino.claimcar.claim.vo.*;
import ins.sino.claimcar.constant.Risk;
import ins.sino.claimcar.platform.util.SendPlatformUtil;
import ins.sino.claimcar.platform.vo.*;
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
import java.text.DecimalFormat;
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
@Service("sendVClaimToPlatformService")
public class SendVClaimToPlatformService {

	private Logger logger = LoggerFactory.getLogger(SendClaimToPlatformService.class);

	@Autowired
	private CompensateService compensateService;
	@Autowired
	private CheckTaskService checkTaskService;
	@Autowired
	private SubrogationService subrogationService;
	@Autowired
	private CodeTranService codeTranService;
	@Autowired
	private PolicyViewService policyViewService;
	@Autowired
	CiClaimPlatformLogService platformLogService;
	@Autowired
	private KindCodeTranService kindCodeTranService;
	@Autowired
	private RegistQueryService registQueryService;
	@Autowired
	CiClaimPlatformTaskService ciClaimPlatformTaskService;
	

//	public static void main(String[] args) {
//		Date underDate = new Date();
//		underDate.setTime(underDate.getTime() - 1 * 60 * 1000);// 减去1分钟以后的时间
//		System.out.println(underDate);
//	}
	
	/**
	 * 全国平台--交强理算核赔
	 * @param compeNo-计算书号
	 * @throws Exception
	 */
	public void sendVClaimCIToPlatform(String compeNo,CiClaimPlatformTaskVo platformTaskVo){
		PrpLCompensateVo compensateVo = compensateService.findCompByPK(compeNo);
		Long currentDate = System.currentTimeMillis();
		logger.info("sendVClaimCIToPlatform begin，registNo={}",compensateVo.getRegistNo());
		String registNo = compensateVo.getRegistNo();
		PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
		// 联共保 从共，从联不交互平台
		if(CodeConstants.GBFlag.MINORCOMMON.equals(registVo.getIsGBFlag())||CodeConstants.GBFlag.MINORRELATION.equals(registVo.getIsGBFlag())){
			return;
		}
		String comCode = policyViewService.findPolicyComCode(registNo,"11");
		CiClaimPlatformLogVo logVo = platformLogService.findLogByBussNo
				(RequestType.RegistInfoCI.getCode(),registNo,comCode);
		if (logVo == null) {
			logger.info("交强理算上传全国平台失败！该案件理赔编码不存在或报案未成功上传平台！");
		}
		String claimSeqNo = logVo == null ? "" : logVo.getClaimSeqNo();
		logger.info("理赔编码："+claimSeqNo);
		
		//投保确认码
		String validNo = policyViewService.findValidNo(registNo,"11");
		logger.info("投保确认码："+validNo);
		
		PrpLCheckDutyVo checkDutyVo = checkTaskService.findCheckDuty(registNo,1);
		PlatformController controller = PlatformFactory.getInstance(comCode,RequestType.VClaimCI);

		//Body
		CiVClaimReqBodyVo bodyVo = new CiVClaimReqBodyVo();
		CiVClaimBasePartVo basePart = new CiVClaimBasePartVo();
		basePart.setConfirmSequenceNo(validNo);
		basePart.setClaimCode(claimSeqNo);
		basePart.setReportNo(registNo);
		basePart.setPaySelfFlag("2".equals(compensateVo.getCaseType()) ? "1" : "0");
		basePart.setIsInVoving(compensateVo.getLawsuitFlag());//是否诉讼

		// 理算信息列表
		List<CiVClaimAdjustmentDataVo> adjustmentDataList = new ArrayList<CiVClaimAdjustmentDataVo>();
		CiVClaimAdjustmentDataVo adjustmentData = new CiVClaimAdjustmentDataVo();
		adjustmentData.setAdjustmentCode(compensateVo.getCompensateNo());
//		Double otherFee = compensateVo.getSumFee()==null
/*//				?0.0:compensateVo.getSumFee().doubleValue();
		adjustmentData.setOtherFee(DataUtils.NullToZero(compensateVo.getSumFee()).doubleValue());*/
		adjustmentData.setUnderWriteDes("核赔通过");
//		adjustmentData.setClaimAmount(DataUtils.NullToZero(compensateVo.getSumAmt()).doubleValue());
		BigDecimal claimAmount = BigDecimal.ZERO;//总赔款金额
		Date underDate = compensateVo.getUnderwriteDate();
		if(underDate == null){
			underDate = new Date();
		}
//		underDate.setTime(underDate.getTime() - 1 * 60 * 1000);// 减去1分钟以后的时间
		adjustmentData.setUnderWriteEndTime(underDate);
		

		// 损失赔偿情况列表
		String recoveryOrPayFlag="0";//是否有清付/追偿，0-无，1-有
		List<CiVClaimCoverDataVo> claimCoverList = new ArrayList<CiVClaimCoverDataVo>();
		for(PrpLLossItemVo itemVo:compensateVo.getPrpLLossItems()){// 车
			CiVClaimCoverDataVo claimCoverData = new CiVClaimCoverDataVo();
			// 2 清付 3 自付
			String payFlag = itemVo.getPayFlag();
			if(PayFlagType.NODUTY_INSTEAD_PAY.equals(itemVo.getPayFlag())){
				payFlag = "3";
			}
			if("1".equals(payFlag) || "2".equals(payFlag)){
				recoveryOrPayFlag="1";
			}
			claimCoverData.setRecoveryOrPayFlag(payFlag);
			claimCoverData.setCoverageCode("100");//------------
			if("1".equals(checkDutyVo.getCiDutyFlag())){// 有责
				claimCoverData.setClaimFeeType("3");
				claimCoverData.setLiabilityRate("1");
			}else{// 车无责
				claimCoverData.setClaimFeeType("7");
				claimCoverData.setLiabilityRate("0");
			}
			claimCoverData.setClaimAmount(DataUtils.NullToZero(itemVo.getSumRealPay()).doubleValue());
			claimAmount = claimAmount.add(DataUtils.NullToZero(itemVo.getSumRealPay()));
			if("3".equals(itemVo.getLossType())){
				claimCoverData.setSalvageFee(DataUtils.NullToZero(itemVo.getRescueFee()).doubleValue());
			}else{
				claimCoverData.setSalvageFee(0d);
			}
			claimCoverList.add(claimCoverData);
		}
		if(compensateVo.getPrpLLossProps()!=null&&compensateVo.getPrpLLossProps().size()>0){
			for(PrpLLossPropVo propVo:compensateVo.getPrpLLossProps()){// 财产
				CiVClaimCoverDataVo claimCoverData = new CiVClaimCoverDataVo();
				claimCoverData.setRecoveryOrPayFlag("3");
				
				claimCoverData.setCoverageCode("100");//------------

				if("1".equals(checkDutyVo.getCiDutyFlag())){// 有责
					claimCoverData.setClaimFeeType("3");
					claimCoverData.setLiabilityRate("1");
				}else{// 无责
					claimCoverData.setClaimFeeType("7");
					claimCoverData.setLiabilityRate("0");
				}
				claimCoverData.setClaimAmount(DataUtils.NullToZero(propVo.getSumRealPay()).doubleValue());
				claimAmount = claimAmount.add(DataUtils.NullToZero(propVo.getSumRealPay()));

				if("3".equals(propVo.getLossType())){
					claimCoverData.setSalvageFee(DataUtils.NullToZero(propVo.getRescueFee()).doubleValue());
				}else{
					claimCoverData.setSalvageFee(0d);
				}
				claimCoverList.add(claimCoverData);
			}
		}
		if(compensateVo.getPrpLLossPersons()!=null&&compensateVo.getPrpLLossPersons().size()>0){
			for(PrpLLossPersonVo personVo:compensateVo.getPrpLLossPersons()){// 人
				for(PrpLLossPersonFeeVo feeVo:personVo.getPrpLLossPersonFees()){
					CiVClaimCoverDataVo claimCoverData = new CiVClaimCoverDataVo();
					claimCoverData.setRecoveryOrPayFlag("3");
					claimCoverData.setCoverageCode("100");//------------

					if("1".equals(checkDutyVo.getCiDutyFlag())){// 有责
						claimCoverData.setClaimFeeType("02".equals(feeVo.getLossItemNo()) ? "1" : "2");
						claimCoverData.setLiabilityRate("1");
					}else{// 无责
						claimCoverData.setClaimFeeType("02".equals(feeVo.getLossItemNo()) ? "5" : "6");
						claimCoverData.setLiabilityRate("0");
					}
					claimCoverData.setClaimAmount(DataUtils.NullToZero(feeVo.getFeeRealPay()).doubleValue());
					claimAmount = claimAmount.add(DataUtils.NullToZero(feeVo.getFeeRealPay()));
					
					claimCoverData.setSalvageFee(0.0);
					// claimCoverData.setSalvageFee();
					claimCoverList.add(claimCoverData);
				}
			}
		}
		

		// 追偿/清付信息列表
		List<CiVClaimRecoveryOrPayDataVo> recoveryOrPayList = new ArrayList<CiVClaimRecoveryOrPayDataVo>();
		List<PrpLPlatLockVo> prpLPlatLockListVos = subrogationService.findPlatLockVoByPayFlag(registNo);
		Integer serNo = 1;
		if("1".equals(recoveryOrPayFlag)){
			if (prpLPlatLockListVos != null && prpLPlatLockListVos.size() > 0) {
				for (PrpLPlatLockVo prpLPlatLockVo : prpLPlatLockListVos) {
					for (PrpLRecoveryOrPayVo prpLRecoveryOrPayVo : prpLPlatLockVo.getPrpLRecoveryOrPays()) {
						if(compensateVo.getCompensateNo().equals(prpLRecoveryOrPayVo.getCompensateNo())){
							CiVClaimRecoveryOrPayDataVo recoveryOrPayData = new CiVClaimRecoveryOrPayDataVo();
//							Integer serNo = prpLRecoveryOrPayVo.getSerialNo();
//							if(serNo==null){
//								serNo = i; i++;
//							}
							recoveryOrPayData.setSerialNo(serNo.toString());serNo++;
							recoveryOrPayData.setRecoveryOrPayFlag(prpLPlatLockVo.getRecoveryOrPayFlag());
							recoveryOrPayData.setRecoveryOrPayType(prpLPlatLockVo.getRecoveryOrPayType());
							// recoveryOrPayData.setRecoveryOrPayMan(recoveryOrPayMan);
							recoveryOrPayData.setRecoveryCode(prpLRecoveryOrPayVo.getRecoveryCode());
//							Double payAmount = prpLRecoveryOrPayVo.getRealreOrPayAmount()==null?
//									0D:prpLRecoveryOrPayVo.getRealreOrPayAmount().doubleValue();
							recoveryOrPayData.setRecoveryOrPayAmount(DataUtils.NullToZero(prpLRecoveryOrPayVo.getRecoveryOrPayAmount()).doubleValue());
							recoveryOrPayList.add(recoveryOrPayData);
						}
					}
				}
			}
			
		}
		

		//如果交强险的零赔付，加入一条零数据
		if(claimCoverList.size()==0){
			CiVClaimCoverDataVo claimCoverData = new CiVClaimCoverDataVo();
			claimCoverData.setClaimAmount(0.0);
			claimCoverData.setClaimFeeType("1");
			claimCoverData.setCoverageCode("100");
			claimCoverData.setLiabilityRate("0");
			claimCoverData.setRecoveryOrPayFlag("3");
			claimCoverData.setSalvageFee(0.0);
			claimCoverList.add(claimCoverData);
		}
		// 损失赔偿情况列表 中赔款金额之和赋值给理算列表中的赔偿金额
		adjustmentData.setClaimAmount(claimAmount.doubleValue());
		adjustmentData.setCoverDataList(claimCoverList);
		adjustmentData.setRecoveryOrPayDataList(recoveryOrPayList);
		adjustmentDataList.add(adjustmentData);
		
		bodyVo.setBasePart(basePart);
		bodyVo.setAdjustmentDataList(adjustmentDataList);
		logger.info("sendVClaimCIToPlatform assemble xml end，registNo={}, cost time {} ms ",compensateVo.getRegistNo(),System.currentTimeMillis()-currentDate);
		if(SendPlatformUtil.isMor(policyViewService.getPolicyInfoByType(registNo,"1"))){
			controller.callPlatform(bodyVo,platformTaskVo);
		}
	}

	/**
	 * 全国平台--商业理算核赔
	 * @param compeNo-计算书号
	 * @throws Exception
	 */
	public void sendVClaimBIToPlatform(String compeNo,CiClaimPlatformTaskVo platformTaskVo){
		PrpLCompensateVo compensateVo = compensateService.findCompByPK(compeNo);
		Long currentDate = System.currentTimeMillis();
		logger.info("sendVClaimBIToPlatform begin，registNo={}",compensateVo.getRegistNo());
		String registNo = compensateVo.getRegistNo();
		PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
		// 联共保 从共，从联不交互平台
		if(CodeConstants.GBFlag.MINORCOMMON.equals(registVo.getIsGBFlag())||CodeConstants.GBFlag.MINORRELATION.equals(registVo.getIsGBFlag())){
			return;
		}
		//保单机构
		String comCode = policyViewService.findPolicyComCode(registNo,"12");
		CiClaimPlatformLogVo logVo = platformLogService.findLogByBussNo(
				RequestType.RegistInfoBI.getCode(),registNo,comCode);
		if (logVo == null) {
			logger.info("商业理算上传全国平台失败！该案件理赔编码不存在或报案未成功上传平台！");
		}
		String claimSeqNo = logVo == null ? "" : logVo.getClaimSeqNo();
		logger.info("理赔编码："+claimSeqNo);

		// 投保确认码
		String validNo = policyViewService.findValidNo(registNo,"12");
		logger.info("投保确认码：" + validNo);
		
//		PrpLCheckDutyVo checkDutyVo = checkTaskService.findCheckDuty(registNo,1);

		PlatformController controller = PlatformFactory.getInstance(comCode,RequestType.VClaimBI);

		BiVClaimReqBodyVo bodyVo = new BiVClaimReqBodyVo();
		BiVClaimBasePartVo basePart = new BiVClaimBasePartVo();
		basePart.setClaimSequenceNo(claimSeqNo);
		basePart.setClaimNotificationNo(registNo);
		basePart.setConfirmSequenceNo(validNo);
		basePart.setIsInVovling(compensateVo.getLawsuitFlag());//是否涉诉

		// 理算信息列表
		List<BiVClaimAdjustmentDataVo> adjustmentDataList = new ArrayList<BiVClaimAdjustmentDataVo>();
		BiVClaimAdjustmentDataVo adjustmentData = new BiVClaimAdjustmentDataVo();
		adjustmentData.setAdjustmentCode(compensateVo.getCompensateNo());
		adjustmentData.setUnderWriteDesc("核赔通过");
//		adjustmentData.setClaimAmount(DataUtils.NullToZero(compensateVo.getSumAmt()).doubleValue());
		BigDecimal claimAmount = BigDecimal.ZERO;//总赔款金额
		Date underDate = compensateVo.getUnderwriteDate();
		if(underDate == null){
			underDate = new Date();
		}
//		underDate.setTime(underDate.getTime() - 1 * 60 * 1000);// 减去1分钟以后的时间
		adjustmentData.setUnderWriteEndTime(underDate);

		// 损失赔偿情况列表
		String recoveryOrPayFlag="0";
		List<BiVClaimCoverDataVo> claimCoverList = new ArrayList<BiVClaimCoverDataVo>();
		for (PrpLLossItemVo itemVo : compensateVo.getPrpLLossItems()) {// 车
			BiVClaimCoverDataVo claimCoverData = new BiVClaimCoverDataVo();
//			claimCoverData.setRecoveryOrPayFlag(itemVo.getPayFlag());
			// 2 清付 3 自付
			String payFlag = itemVo.getPayFlag();
			if(PayFlagType.NODUTY_INSTEAD_PAY.equals(itemVo.getPayFlag())){
				payFlag = "3";
			}
			if("1".equals(payFlag) || "2".equals(payFlag)){
				recoveryOrPayFlag="1";
			}
			claimCoverData.setRecoveryOrPayFlag(payFlag);
//			boolean riskType = CodeConstants.ISNEWCLAUSECODE_MAP.get(itemVo.getRiskCode());
//			String codeType = riskType ? "CoverageCode" : "CoverageCodePremium";
//			String codeType = isFeeUpdate(itemVo.getRiskCode())?"CoverageCode":"CoverageCodePremium";//CoverageCode-否
//			SysCodeDictVo sysVo = codeTranService.findTransCodeDictVo(codeType,itemVo.getKindCode());
//			String initVal = isFeeUpdate(itemVo.getRiskCode())?"200":"0101200";
			claimCoverData.setCoverageCode(kindCodeTranService.findTransCiCode("00", itemVo.getRiskCode(), "CovergeCode", itemVo.getKindCode()));
			claimCoverData.setLossFeeType("1");
			
			Double rate = itemVo.getDutyRate().doubleValue()/100;
			claimCoverData.setLiabilityRate(rate.toString());
			claimCoverData.setSalvageFee(DataUtils.NullToZero(itemVo.getRescueFee()).doubleValue());
			String sum = new DecimalFormat("######0.00").format(DataUtils.NullToZero(itemVo.getSumLoss()));
			claimCoverData.setLossAmount(Double.parseDouble(sum));

			claimCoverData.setClaimAmount(DataUtils.NullToZero(itemVo.getSumRealPay()).doubleValue());
			claimAmount = claimAmount.add(DataUtils.NullToZero(itemVo.getSumRealPay()));

            if("A1".equals(itemVo.getKindCode())){
            	String sign="0";//sign附加险是否有新增设备险，0无，1有
            	if(compensateVo.getPrpLLossProps()!=null && compensateVo.getPrpLLossProps().size()>0){
            		for(PrpLLossPropVo propVo : compensateVo.getPrpLLossProps()){
            			if("9".equals(propVo.getPropType())){
            				if("X3".equals(propVo.getKindCode())){
            					sign="1";
            					break;
            				}
            			}
            		}
            	}
            	if("0".equals(sign)){
            		claimCoverData.setIsDeviceItem("0");
            	}else{
            		claimCoverData.setIsDeviceItem("1");
            	}
            }
           
			claimCoverList.add(claimCoverData);
		}
		if(compensateVo.getPrpLLossProps()!=null&&compensateVo.getPrpLLossProps().size()>0){
			for (PrpLLossPropVo propVo : compensateVo.getPrpLLossProps()) {// 财产
				if(CodeConstants.KINDCODE.KINDCODE_RS.equals(propVo.getKindCode()) || CodeConstants.KINDCODE.KINDCODE_VS.equals(propVo.getKindCode()) ||
						CodeConstants.KINDCODE.KINDCODE_DS.equals(propVo.getKindCode()) || CodeConstants.KINDCODE.KINDCODE_DC.equals(propVo.getKindCode())){
					continue;
				}
				BiVClaimCoverDataVo claimCoverData = new BiVClaimCoverDataVo();
				claimCoverData.setRecoveryOrPayFlag("3");// 字段被删除
//				boolean riskType = CodeConstants.ISNEWCLAUSECODE_MAP.get(propVo.getRiskCode());
//				String codeType = riskType ? "CoverageCode" : "CoverageCodePremium";
//				String codeType = isFeeUpdate(propVo.getRiskCode())?"CoverageCode":"CoverageCodePremium";//CoverageCode-否
//				SysCodeDictVo sysVo = codeTranService.findTransCodeDictVo(codeType,propVo.getKindCode());
//				String initVal = isFeeUpdate(propVo.getRiskCode())?"200":"0101200";
				claimCoverData.setCoverageCode(kindCodeTranService.findTransCiCode("00", propVo.getRiskCode(), "CovergeCode", propVo.getKindCode()));
				claimCoverData.setLossFeeType("2");
				Double liab = propVo.getDutyRate().doubleValue()/100;
				claimCoverData.setLiabilityRate(liab.toString());
//				Double salFee = propVo.getRescueFee()==null
//						?0.0:propVo.getRescueFee().doubleValue();
				claimCoverData.setSalvageFee(DataUtils.NullToZero(propVo.getRescueFee()).doubleValue());
				String sum = new DecimalFormat("######0.00").format(DataUtils.NullToZero(propVo.getSumLoss()));
				claimCoverData.setLossAmount(Double.parseDouble(sum));
				claimCoverData.setClaimAmount(DataUtils.NullToZero(propVo.getSumRealPay()).doubleValue());
				claimAmount = claimAmount.add(DataUtils.NullToZero(propVo.getSumRealPay()));
				 if("X3".equals(propVo.getKindCode())){
		            	claimCoverData.setIsDeviceItem("1");
					 }
				claimCoverList.add(claimCoverData);
			}
		}
		if(compensateVo.getPrpLLossPersons()!=null&&compensateVo.getPrpLLossPersons().size()>0){
			for (PrpLLossPersonVo personVo : compensateVo.getPrpLLossPersons()) {// 人
				for (PrpLLossPersonFeeVo feeVo : personVo.getPrpLLossPersonFees()) {
					BiVClaimCoverDataVo claimCoverData = new BiVClaimCoverDataVo();
					claimCoverData.setRecoveryOrPayFlag("3");
					// 待翻译
					// codeTranService.findTransCodeDictVo(codeType,code)
//					boolean riskType = CodeConstants.ISNEWCLAUSECODE_MAP.get(personVo.getRiskCode());
//					String codeType = riskType ? "CoverageCode" : "CoverageCodePremium";
//					String codeType = isFeeUpdate(personVo.getRiskCode())?"CoverageCode":"CoverageCodePremium";//CoverageCode-否
//					SysCodeDictVo sysVo = codeTranService.findTransCodeDictVo(codeType,personVo.getKindCode());
//					String initVal = isFeeUpdate(personVo.getRiskCode())?"200":"0101200";
					claimCoverData.setCoverageCode(kindCodeTranService.findTransCiCode("00", personVo.getRiskCode(), "CovergeCode", personVo.getKindCode()));
					
//					claimCoverData.setLossFeeType(personVo.getLossType());
					claimCoverData.setLossFeeType("6");
					Double liab = personVo.getDutyRate().doubleValue()/100;
					claimCoverData.setLiabilityRate(liab.toString());
					claimCoverData.setClaimAmount(DataUtils.NullToZero(feeVo.getFeeRealPay()).doubleValue());
					claimAmount = claimAmount.add(DataUtils.NullToZero(feeVo.getFeeRealPay()));
					claimCoverData.setSalvageFee(0.0);
					String sum = new DecimalFormat("######0.00").format(DataUtils.NullToZero(feeVo.getFeeLoss()));
					claimCoverData.setLossAmount(Double.parseDouble(sum));

					claimCoverList.add(claimCoverData);
				}
			}
		}
		
		// 追偿/清付信息列表 1-追偿，2--清付，3--自付
		List<BiVClaimRecoveryOrPayDataVo> recoveryOrPayList = new ArrayList<BiVClaimRecoveryOrPayDataVo>();
		List<PrpLPlatLockVo> prpLPlatLockListVos = subrogationService.findPlatLockVoByPayFlag(registNo);
		Integer serNo = 1;
		if("1".equals(recoveryOrPayFlag)){//有清付与追偿标志，此信息列表才能有值
			if (prpLPlatLockListVos != null && prpLPlatLockListVos.size() > 0) {
				for (PrpLPlatLockVo prpLPlatLockVo : prpLPlatLockListVos) {
					for (PrpLRecoveryOrPayVo prpLRecoveryOrPayVo : prpLPlatLockVo.getPrpLRecoveryOrPays()) {
						if(compensateVo.getCompensateNo().equals(prpLRecoveryOrPayVo.getCompensateNo())){
							BiVClaimRecoveryOrPayDataVo recoveryOrPayData = new BiVClaimRecoveryOrPayDataVo();
//							Integer serNo = prpLRecoveryOrPayVo.getSerialNo();
//							if(serNo==null){
//								serNo = i; i++;
//							}
							recoveryOrPayData.setSerialNo(serNo.toString());serNo++;
							recoveryOrPayData.setRecoveryOrPayFlag(prpLPlatLockVo.getRecoveryOrPayFlag());
							recoveryOrPayData.setRecoveryOrPayType(prpLPlatLockVo.getRecoveryOrPayType());
							// recoveryOrPayData.setRecoveryOrPayMan(recoveryOrPayMan);
							recoveryOrPayData.setRecoveryCode(prpLRecoveryOrPayVo.getRecoveryCode());
//							Double payAmount = prpLRecoveryOrPayVo.getRealreOrPayAmount()==null
//									?0D:prpLRecoveryOrPayVo.getRealreOrPayAmount().doubleValue();
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
				BiVClaimRecoveryOrPayDataVo recoveryOrPayData = new BiVClaimRecoveryOrPayDataVo();
				recoveryOrPayData.setSerialNo(serNo.toString());serNo++;
				recoveryOrPayData.setRecoveryOrPayFlag(CodeConstants.PayFlagType.INSTEAD_PAY);
				recoveryOrPayData.setRecoveryOrPayType("3");// 致害人
				recoveryOrPayData.setRecoveryOrPayMan(personVo.getName());
				recoveryOrPayData.setRecoveryOrPayAmount(DataUtils.NullToZero(personVo.getThisPaid()).doubleValue());
				recoveryOrPayList.add(recoveryOrPayData);
			}
		}
		
		// 如果商业险的零赔付，加入一条零数据
		if(claimCoverList.size()==0){
			BiVClaimCoverDataVo claimCoverData = new BiVClaimCoverDataVo();
			claimCoverData.setClaimAmount(0.0);
			claimCoverData.setLossFeeType("1");

			String codeType = !isFeeUpdate(compensateVo.getRiskCode()) ? "CoverageCodePremium" : "CoverageCode";// CoverageCode-否
			String initVal = !isFeeUpdate(compensateVo.getRiskCode()) ? "0101200" : "200";

			PrpLCMainVo prpLCMainVo = policyViewService.getRegistNoAndRiskCodeInfo(registNo, compensateVo.getRiskCode());
			if(prpLCMainVo!=null){
				PrpLCItemKindVo kind = prpLCMainVo.getPrpCItemKinds().get(0);
//				SysCodeDictVo sysVo = codeTranService.findTransCodeDictVo(codeType,kind.getKindCode());
				claimCoverData.setCoverageCode(kindCodeTranService.findTransCiCode("00", compensateVo.getRiskCode(), "CovergeCode", kind.getKindCode()));
			}
			if(StringUtils.isBlank(claimCoverData.getCoverageCode())){
				claimCoverData.setCoverageCode(initVal);
			}
			// claimCoverData.setCoverageCode("100");
			claimCoverData.setLiabilityRate("1.00");
			claimCoverData.setRecoveryOrPayFlag("3");
			claimCoverData.setLossAmount(0.0);
			claimCoverData.setSalvageFee(0.0);
			claimCoverList.add(claimCoverData);
		}

		// 损失赔偿情况列表 中赔款金额之和赋值给理算列表中的赔偿金额
		adjustmentData.setClaimAmount(claimAmount.doubleValue());
		adjustmentData.setClaimCoverDataList(claimCoverList);
		adjustmentData.setRecoveryOrPayDataList(recoveryOrPayList);
		adjustmentDataList.add(adjustmentData);

		bodyVo.setBasePart(basePart);
		bodyVo.setAdjustmentDataList(adjustmentDataList);
		logger.info("sendVClaimBIToPlatform assemble xml end，registNo={}, cost time {} ms ",compensateVo.getRegistNo(),System.currentTimeMillis()-currentDate);
		if(SendPlatformUtil.isMor(policyViewService.getPolicyInfoByType(registNo,"2"))){
			controller.callPlatform(bodyVo,platformTaskVo);
		}
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
	 * 保存核赔送平台的定时任务表
	 * @param compeVo
	 * @param policyComCode
	 */
	public void savePlatformTask(PrpLCompensateVo compeVo,String policyComCode){
		CiClaimPlatformTaskVo platformTaskVo = new CiClaimPlatformTaskVo();
		PrpLCMainVo prpLCmainVo = policyViewService.getPolicyInfo(compeVo.getRegistNo(),compeVo.getPolicyNo());
		
		platformTaskVo.setRegistNo(compeVo.getRegistNo());
		platformTaskVo.setBussNo(compeVo.getCompensateNo());
		platformTaskVo.setClaimSeqNo(prpLCmainVo.getClaimSequenceNo());
		if(policyComCode.startsWith("22")){//上海
			platformTaskVo.setTaskLevel(CodeConstants.PlatformTaskLevel_SH.VClaim);
			platformTaskVo.setTaskParams(compeVo.getCompensateNo()+","+policyComCode+","+CiClaimPlatformTaskVo.class.getName());
			if(Risk.DQZ.equals(prpLCmainVo.getRiskCode())){
				platformTaskVo.setRequestType(RequestType.VClaimCI_SH.name());
				platformTaskVo.setRequestName(RequestType.VClaimCI_SH.getName());
			}else{
				platformTaskVo.setRequestType(RequestType.VClaimBI_SH.name());
				platformTaskVo.setRequestName(RequestType.VClaimBI_SH.getName());
			}
		}else{//全国
			platformTaskVo.setTaskLevel(CodeConstants.PlatformTaskLevel.VClaim);
			platformTaskVo.setTaskParams(compeVo.getCompensateNo()+","+CiClaimPlatformTaskVo.class.getName());
			if(Risk.DQZ.equals(prpLCmainVo.getRiskCode())){
				platformTaskVo.setRequestType(RequestType.VClaimCI.name());
				platformTaskVo.setRequestName(RequestType.VClaimCI.getName());
			}else{
				platformTaskVo.setRequestType(RequestType.VClaimBI.name());
				platformTaskVo.setRequestName(RequestType.VClaimBI.getName());
			}
		}
		platformTaskVo.setStatus(CodeConstants.platformStatus.None);
		platformTaskVo.setRedoTimes(0);
		platformTaskVo.setOperateStatus(CodeConstants.OperateStatus.OFF);
		ciClaimPlatformTaskService.savePlatformTask(platformTaskVo);
	}
}
