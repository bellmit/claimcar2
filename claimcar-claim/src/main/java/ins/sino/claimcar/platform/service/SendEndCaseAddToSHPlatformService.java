/******************************************************************************
* CREATETIME : 2016年6月1日 下午4:15:45
******************************************************************************/
package ins.sino.claimcar.platform.service;

import ins.platform.common.service.facade.CodeTranService;
import ins.platform.utils.DataUtils;
import ins.platform.vo.SysCodeDictVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.KINDCODE;
import ins.sino.claimcar.carplatform.constant.RequestType;
import ins.sino.claimcar.carplatform.controller.PlatformController;
import ins.sino.claimcar.carplatform.service.CiClaimPlatformLogService;
import ins.sino.claimcar.carplatform.util.PlatformFactory;
import ins.sino.claimcar.carplatform.vo.CiClaimPlatformLogVo;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.PrpLCheckDutyVo;
import ins.sino.claimcar.check.vo.PrpLCheckVo;
import ins.sino.claimcar.claim.service.ClaimTaskService;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claim.vo.PrpLLossItemVo;
import ins.sino.claimcar.claim.vo.PrpLLossPersonFeeVo;
import ins.sino.claimcar.claim.vo.PrpLLossPersonVo;
import ins.sino.claimcar.claim.vo.PrpLLossPropVo;
import ins.sino.claimcar.constant.Risk;
import ins.sino.claimcar.endcase.service.EndCasePubService;
import ins.sino.claimcar.endcase.vo.PrpLEndCaseVo;
import ins.sino.claimcar.endcase.vo.PrpLReCaseVo;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarCompVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarInfoVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarRepairVo;
import ins.sino.claimcar.lossperson.service.PersTraceDubboService;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersExtVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersHospitalVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersInjuredVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceFeeVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceMainVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceVo;
import ins.sino.claimcar.lossprop.service.PropTaskService;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropFeeVo;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropMainVo;
import ins.sino.claimcar.platform.vo.sh.SHBIEndCaseAddReqBodyVo;
import ins.sino.claimcar.platform.vo.sh.SHBIEndCaseAddReqVehicleDataVo;
import ins.sino.claimcar.platform.vo.sh.SHCIEndCaseAddReqActuralRepairDataVo;
import ins.sino.claimcar.platform.vo.sh.SHCIEndCaseAddReqBasePartVo;
import ins.sino.claimcar.platform.vo.sh.SHCIEndCaseAddReqBodyVo;
import ins.sino.claimcar.platform.vo.sh.SHCIEndCaseAddReqCarLossPartDataVo;
import ins.sino.claimcar.platform.vo.sh.SHCIEndCaseAddReqClaimCoverListVo;
import ins.sino.claimcar.platform.vo.sh.SHCIEndCaseAddReqDetailListVo;
import ins.sino.claimcar.platform.vo.sh.SHCIEndCaseAddReqFittingDataVo;
import ins.sino.claimcar.platform.vo.sh.SHCIEndCaseAddReqHospitalInfoDataVo;
import ins.sino.claimcar.platform.vo.sh.SHCIEndCaseAddReqInjuryDataVo;
import ins.sino.claimcar.platform.vo.sh.SHCIEndCaseAddReqObjDataVo;
import ins.sino.claimcar.platform.vo.sh.SHCIEndCaseAddReqPersonDataVo;
import ins.sino.claimcar.platform.vo.sh.SHCIEndCaseAddReqPersonLossPartDataVo;
import ins.sino.claimcar.platform.vo.sh.SHCIEndCaseAddReqRepairDataVo;
import ins.sino.claimcar.platform.vo.sh.SHCIEndCaseAddReqVehicleDataVo;
import ins.sino.claimcar.platform.vo.sh.SHCIVClaimInjuryIdentifyInfoDataVo;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.reopencase.service.ReOpenCaseServiceImpl;

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
 * 结案追加--上海机构案件--送上海平台服务类
 * @author ★Luwei
 */
@Service("endCaseAddToSHPlatformService")
public class SendEndCaseAddToSHPlatformService {

	private Logger logger = LoggerFactory.getLogger(SendClaimToSHPlatformService.class);

	@Autowired
	CodeTranService codeTranService;
	@Autowired
	ClaimTaskService claimService;
	@Autowired
	ReOpenCaseServiceImpl reOpenCaseService;
	@Autowired
	PolicyViewService policyService;
	@Autowired
	RegistQueryService registService;
	@Autowired
	CompensateTaskService compensateTaskService;
	@Autowired
	CheckTaskService checkService;
	@Autowired
	PersTraceDubboService persTraceService;
	@Autowired
	LossCarService lossCarService;
	@Autowired
	PropTaskService propTaskService;
	@Autowired
	CiClaimPlatformLogService platformLogService;
	@Autowired
	PolicyViewService policyViewService;
	@Autowired
	EndCasePubService endCaseService;
	@Autowired
	private KindCodeTranService kindCodeTranService;
	
	/**
	 * 上海平台--补传结案追加（调用方法）
	 * @param reqType
	 * @param bussNo - 立案号
	 * @param comCode
	 * @throws Exception
	 */
	public void sendEndCaseAddTo_SHPlatform(CiClaimPlatformLogVo logVo){
		List<PrpLClaimVo> claimVos = claimService.findClaimListByRegistNo(logVo.getBussNo());
		if(claimVos!=null&&claimVos.size()>0&&"22".equals(logVo.getComCode().substring(0,2))){
			for(PrpLClaimVo tempClaimVo : claimVos){
				String riskCode = tempClaimVo.getRiskCode();
				String caseNo = tempClaimVo.getCaseNo();
				if(Risk.DQZ.equals(riskCode) && 
						logVo.getRequestType().equals(RequestType.EndCaseAddInfoCI_SH.getCode())){
					this.sendEndCaseAddToSH(caseNo);
				}
				if(!Risk.DQZ.equals(riskCode) && 
						logVo.getRequestType().equals(RequestType.EndCaseAddInfoBI_SH.getCode())){
					this.sendEndCaseAddToSH(caseNo);
				}
			}
		}
	}
	
	/**
	 * 发送
	 * @param endCaseNo
	 * @param claimNo
	 * @modified:
	 * ☆Luwei(2016年6月1日 下午4:25:25): <br>
	 */
	public void sendEndCaseAddToSH(String endCaseNo) {
		logger.info("=====endCaseNo====="+endCaseNo);
		PrpLEndCaseVo endCaseVo = endCaseService.findEndCaseByPK(null, endCaseNo);
		if(endCaseVo == null){
			logger.info("=====PrpLEndCaseVo====="+"未找到有效的结案表数据信息");
			return;
		}
		PrpLReCaseVo reCaseVo = findReCase(endCaseVo.getClaimNo());
//		PrpLReCaseVo reCaseVo = reOpenCaseService.findReCaseVoByEndCaseNo(endCaseNo);
//		PrpLClaimVo claimVo = claimService.findClaimVoByClaimNo(reCaseVo.getClaimNo());
		PrpLCompensateVo compensateVo = compensateTaskService.findCompByPK(endCaseVo.getCompensateNo());
		if(compensateVo == null){
			compensateVo = findCompensateByClaim(compensateVo,endCaseVo.getClaimNo());
		}
		String registNo = compensateVo.getRegistNo();
		PrpLRegistVo registVo = registService.findByRegistNo(registNo);
		// 联共保 从共，从联不交互平台
		if(CodeConstants.GBFlag.MINORCOMMON.equals(registVo.getIsGBFlag())||CodeConstants.GBFlag.MINORRELATION.equals(registVo.getIsGBFlag())){
			return;
		}
		String reqType = Risk.DQZ.equals(compensateVo.getRiskCode()) ? "11" : "12";
		String comCode = policyViewService.findPolicyComCode(registNo,reqType);
		
		String requestCode = Risk.DQZ.equals(compensateVo.getRiskCode()) ? 
				RequestType.RegistInfoCI_SH.getCode() : RequestType.RegistInfoBI_SH.getCode();
		CiClaimPlatformLogVo logVo = platformLogService.findLogByBussNo(requestCode,registNo,comCode);
		
		PlatformController controller = null;
		String claimSeqNo = logVo == null ? "" : logVo.getClaimSeqNo();
		if (reqType.equals("11")) {// 交强
			controller = PlatformFactory.getInstance(logVo.getComCode(),RequestType.EndCaseAddInfoCI_SH);
			SHCIEndCaseAddReqBodyVo request_BodyVo = this.setBodyCI_SH(claimSeqNo,compensateVo,reCaseVo);
			// 发送报文
			controller.callPlatform(request_BodyVo);
		} else {// 商业
			controller = PlatformFactory.getInstance(logVo.getComCode(),RequestType.EndCaseAddInfoBI_SH);
			SHBIEndCaseAddReqBodyVo request_BodyVo = this.setBodyBI_SH(claimSeqNo,compensateVo,reCaseVo);
			// 发送报文
			controller.callPlatform(request_BodyVo);
		}

		// 接收
//		CiResponseHeadVo resHeadVo = controller.getHeadVo(CiResponseHeadVo.class);
//		if (resHeadVo!=null&&!"1".equals(resHeadVo.getResponseCode())) {
//			logger.info("结案追加上传上海平台失败！");
//		}
	}
	
	private PrpLCompensateVo findCompensateByClaim(PrpLCompensateVo compensateVo,String claimNo){
		if(compensateVo == null){
			List<PrpLCompensateVo> compensateVoList = compensateTaskService.findCompListByClaimNo(claimNo,"N");
			if(compensateVoList !=null && compensateVoList.size() > 0){
				compensateVo = compensateVoList.get(0);
				compensateVo.setSumAmt(BigDecimal.ZERO);
				compensateVo.setSumFee(BigDecimal.ZERO);
			}else{
				compensateVo = null;
			}
		}
		return compensateVo;
	}
	
	private PrpLReCaseVo findReCase(String claimNo){
		PrpLReCaseVo reCaseVo = null;
		List<PrpLReCaseVo> reCaseVoList = reOpenCaseService.findReCaseByClaimNo(claimNo);
		if(reCaseVoList != null && reCaseVoList.size() > 0){
			reCaseVo = reCaseVoList.get(0);
		}else{
			reCaseVo = new PrpLReCaseVo();
			reCaseVo.setOpenReasonCode("1");
			reCaseVo.setOpenCaseDate(new Date());
		}
		return reCaseVo;
	}
	
	// 组织交强险报文
	public SHCIEndCaseAddReqBodyVo setBodyCI_SH(String claimSeqNo,PrpLCompensateVo compensateVo,PrpLReCaseVo reCaseVo) {
		String registNo = compensateVo.getRegistNo();
		PrpLRegistVo registVo = registService.findByRegistNo(registNo);
//		PrpLCheckDutyVo dutyVo = checkService.findCheckDuty(registNo,1);
		PrpLCheckVo checkVo = checkService.findCheckVoByRegistNo(registNo);
		PrpLCheckDutyVo checkDutyVo = checkService.findCheckDuty(registNo,1);
		SHCIEndCaseAddReqBodyVo bodyVo = new SHCIEndCaseAddReqBodyVo();
		
		//基本信息
		SHCIEndCaseAddReqBasePartVo baseVo = new SHCIEndCaseAddReqBasePartVo();
		baseVo.setClaimCode(claimSeqNo);
		String reasonCode = reCaseVo.getOpenReasonCode();
		baseVo.setReasonType("1".equals(reasonCode) ? reasonCode : "2");
		baseVo.setNumerationStartTime(compensateVo.getCreateTime());
		baseVo.setNumerationEndTime(compensateVo.getUpdateTime());
		baseVo.setAddTime(reCaseVo.getOpenCaseDate());
		baseVo.setRemark(registVo.getPrpLRegistExt().getRemark());
		baseVo.setEstimate(NullToZero(compensateVo.getSumPaidAmt()).doubleValue());
		baseVo.setRegistNo(registNo);
		
		double  underDefLoss = 0l;
		String isPersonInjured = "0"; 
		String isProtectLoss = "0";
		String isSingleAccident = "0";
		
		//追加损失赔偿情况
		List<SHCIEndCaseAddReqClaimCoverListVo> claimCoverListVo = new ArrayList<SHCIEndCaseAddReqClaimCoverListVo>();
		//车
		List<PrpLLossItemVo> lossItemVoList = compensateVo.getPrpLLossItems();
		if(lossItemVoList != null && lossItemVoList.size() > 0){
			for(PrpLLossItemVo lossItemVo : lossItemVoList){
				SHCIEndCaseAddReqClaimCoverListVo claimCoverVo = new SHCIEndCaseAddReqClaimCoverListVo();
				claimCoverVo.setPolicyNo(lossItemVo.getPolicyNo());
				claimCoverVo.setLiabilityRate("0".equals(checkDutyVo.getCiDutyFlag()) ? 5 : 1);
//				claimCoverVo.setClaimFeeType("1");
				claimCoverVo.setClaimFeeType("1".equals(lossItemVo.getLossType())?"8":"BZ".equals(lossItemVo.getKindCode())?"3":"7");
				if(isFeeUpdate(lossItemVo.getRiskCode())){
					claimCoverVo.setCoverageType("1");//强制三者险
				}
				//claimCoverVo.setCoverageCode(getRiskCodeVal(lossItemVo.getRiskCode(),lossItemVo.getKindCode()));
				claimCoverVo.setCoverageCode(kindCodeTranService.findTransCiCode("22",lossItemVo.getRiskCode(), "CovergeCodeSH", lossItemVo.getKindCode()));
				claimCoverVo.setLossAmount(NullToZero(lossItemVo.getSumLoss()).doubleValue());
				claimCoverVo.setClaimAmount(NullToZero(lossItemVo.getSumRealPay()).doubleValue());
				claimCoverVo.setSalvageFee(NullToZero(lossItemVo.getRescueFee()).doubleValue());
				//损失赔偿情况明细
				List<SHCIEndCaseAddReqDetailListVo> detailListVo = new ArrayList<SHCIEndCaseAddReqDetailListVo>();
				SHCIEndCaseAddReqDetailListVo detailVo = new SHCIEndCaseAddReqDetailListVo();
				detailVo.setClaimFeeType("1".equals(lossItemVo.getLossType())?"8":"BZ".equals(lossItemVo.getKindCode())?"3":"7");
				detailVo.setDetailFeeType("1");// 车辆
				detailVo.setClaimAmount(lossItemVo.getSumRealPay().doubleValue());
				detailListVo.add(detailVo);
				
				claimCoverVo.setDetailListVo(detailListVo);
				//
				claimCoverListVo.add(claimCoverVo);
			}
		}
		//财
		List<PrpLLossPropVo> lossPropVoList = compensateVo.getPrpLLossProps();
		if(lossPropVoList != null && lossPropVoList.size() > 0){
			for(PrpLLossPropVo lossPropVo : lossPropVoList){
				SHCIEndCaseAddReqClaimCoverListVo claimCoverVo = new SHCIEndCaseAddReqClaimCoverListVo();
				claimCoverVo.setPolicyNo(lossPropVo.getPolicyNo());
				claimCoverVo.setLiabilityRate(0);
				claimCoverVo.setClaimFeeType("3");
//				claimCoverVo.setCoverageType(isFeeUpdate(lossItemVo.getRiskCode()) ? "2" : "");
				if(isFeeUpdate(lossPropVo.getRiskCode())){
					claimCoverVo.setCoverageType("1");//强制三者险
				}
				//claimCoverVo.setCoverageCode(getRiskCodeVal(lossPropVo.getRiskCode(),lossPropVo.getKindCode()));
				claimCoverVo.setCoverageCode(kindCodeTranService.findTransCiCode("22",lossPropVo.getRiskCode(), "CovergeCodeSH", lossPropVo.getKindCode()));
				claimCoverVo.setLossAmount(NullToZero(lossPropVo.getSumLoss()).doubleValue());
				claimCoverVo.setClaimAmount(NullToZero(lossPropVo.getSumRealPay()).doubleValue());
				claimCoverVo.setSalvageFee(NullToZero(lossPropVo.getRescueFee()).doubleValue());
				//损失赔偿情况明细
				List<SHCIEndCaseAddReqDetailListVo> detailListVo = new ArrayList<SHCIEndCaseAddReqDetailListVo>();
				SHCIEndCaseAddReqDetailListVo detailVo = new SHCIEndCaseAddReqDetailListVo();
				detailVo.setClaimFeeType("3");
				detailVo.setDetailFeeType("9");// 其他
				detailVo.setClaimAmount(DataUtils.NullToZero(lossPropVo.getSumRealPay()).doubleValue());
				detailListVo.add(detailVo);
				
				claimCoverVo.setDetailListVo(detailListVo);
				//
				claimCoverListVo.add(claimCoverVo);
			}
		}
		
		//人
		List<PrpLLossPersonVo> lossPersonVoList = compensateVo.getPrpLLossPersons();
		if(lossPersonVoList != null && lossPersonVoList.size() > 0){
			for(PrpLLossPersonVo lossPersonVo : lossPersonVoList){
				SHCIEndCaseAddReqClaimCoverListVo claimCoverVo = new SHCIEndCaseAddReqClaimCoverListVo();
				claimCoverVo.setPolicyNo(lossPersonVo.getPolicyNo());
				claimCoverVo.setLiabilityRate(0);
				claimCoverVo.setClaimFeeType("2");
				claimCoverVo.setCoverageType("1");//强制三者险
//				claimCoverVo.setCoverageType(isFeeUpdate(lossItemVo.getRiskCode()) ? "2" : "");
				//claimCoverVo.setCoverageCode(getRiskCodeVal(lossPersonVo.getRiskCode(),lossPersonVo.getKindCode()));
				claimCoverVo.setCoverageCode(kindCodeTranService.findTransCiCode("22",lossPersonVo.getRiskCode(), "CovergeCodeSH", lossPersonVo.getKindCode()));
				claimCoverVo.setLossAmount(NullToZero(lossPersonVo.getSumLoss()).doubleValue());
				claimCoverVo.setClaimAmount(NullToZero(lossPersonVo.getSumRealPay()).doubleValue());
				claimCoverVo.setSalvageFee(0.0);
				//损失赔偿情况明细
				List<SHCIEndCaseAddReqDetailListVo> detailListVo = new ArrayList<SHCIEndCaseAddReqDetailListVo>();
				for(PrpLLossPersonFeeVo feeVo : lossPersonVo.getPrpLLossPersonFees()){
					SHCIEndCaseAddReqDetailListVo detailData = new SHCIEndCaseAddReqDetailListVo();
					if("0".equals(checkDutyVo.getCiDutyFlag())){// 无责
						detailData.setClaimFeeType("02".equals(feeVo.getLossItemNo()) ? "1" : "2");
						detailData.setLiabilityRate("5");
					}else{
						detailData.setClaimFeeType("02".equals(feeVo.getLossItemNo()) ? "5" : "6");
						detailData.setLiabilityRate("1");
					}
					detailData.setDetailFeeType(feeVo.getKindCode());// 其他
					detailData.setLossAmount(DataUtils.NullToZero(feeVo.getFeeLoss()).doubleValue());
					detailData.setClaimAmount(DataUtils.NullToZero(feeVo.getFeeRealPay()).doubleValue());
					detailListVo.add(detailData);
				}
				claimCoverVo.setDetailListVo(detailListVo);
				claimCoverListVo.add(claimCoverVo);
			}
		}
		
		
		// 人员损失情况（多条）
		PrpLDlossPersTraceMainVo persTraceMainVo = getPersTraceMainVo(lossPersonVoList);
		List<PrpLDlossPersTraceVo> persTraceVoList = null;
		if(persTraceMainVo != null){
			persTraceVoList = persTraceMainVo.getPrpLDlossPersTraces();
		}
		
		List<SHCIEndCaseAddReqPersonDataVo> personDataVos = new ArrayList<SHCIEndCaseAddReqPersonDataVo>();
		if(persTraceVoList != null && persTraceVoList.size() > 0){
			isPersonInjured ="1";
			for(PrpLDlossPersTraceVo dlossPersTraceVo : persTraceVoList){
				PrpLDlossPersInjuredVo persInjuredVo = dlossPersTraceVo.getPrpLDlossPersInjured();

				// 人员损失情况
				SHCIEndCaseAddReqPersonDataVo personDataVo = new SHCIEndCaseAddReqPersonDataVo();
				personDataVo.setPersonName(persInjuredVo.getPersonName());
				personDataVo.setPersonId(persInjuredVo.getCertiCode());
				personDataVo.setAge(persInjuredVo.getPersonAge().intValue());
				personDataVo.setLossAmount(NullToZero(dlossPersTraceVo.getSumVeriDefloss()).doubleValue());
				personDataVo.setMainThird("1".equals(dlossPersTraceVo.getLossFeeType()) ? "1" : "0");
				personDataVo.setSurveyType("0".equals(checkVo.getPrpLCheckTask().getFirstAddressFlag()) ? "3" : "1");
				personDataVo.setSurveyName(checkVo.getPrpLCheckTask().getChecker());
				personDataVo.setSurveyStartTime(checkVo.getCreateTime());
				personDataVo.setSurveyEndTime(checkVo.getChkSubmitTime());
				personDataVo.setSurveyPlace(checkVo.getPrpLCheckTask().getCheckAddress());
				personDataVo.setSurveyDes(checkVo.getPrpLCheckTask().getContexts());
				String codeCName = codeTranService.findCodeName("UserCodeSH",persTraceMainVo.getOperatorCode());
				if(StringUtils.isEmpty(codeCName)){
					codeCName = codeTranService.findCodeName("UserCodeSH",persTraceMainVo.getCreateUser());
				}
				personDataVo.setEstimateName(StringUtils.isNotEmpty(codeCName) ? codeCName : "GSU0000001");
				personDataVo.setEstimateStartTime(persTraceMainVo.getPlfSubTime());
				personDataVo.setEstimateEndTime(persTraceMainVo.getUndwrtFeeEndDate());
				personDataVo.setAssesorName(persTraceMainVo.getUndwrtFeeName());
				personDataVo.setAssesorStartTime(persTraceMainVo.getCreateTime());
				personDataVo.setAssesorEndTime(persTraceMainVo.getUpdateTime());
				personDataVo.setInjuryType("");
				//personDataVo.setInjuryLevel(persInjuredVo.getWoundCode());
				personDataVo.setMedicalType(persInjuredVo.getTreatSituation());
				personDataVo.setUnderWriteName(persTraceMainVo.getUndwrtFeeName());
				personDataVo.setUnderWriteCode(persTraceMainVo.getUndwrtFeeCode());
				personDataVo.setEstimateAddr("");
				Date addmissionTime = null;
				if(persInjuredVo.getPrpLDlossPersHospitals() != null && !persInjuredVo.getPrpLDlossPersHospitals().isEmpty()){
					addmissionTime = persInjuredVo.getPrpLDlossPersHospitals().get(0).getInHospitalDate();
				}
				personDataVo.setAddmissionTime(addmissionTime);
				personDataVo.setUnderDefloss(dlossPersTraceVo.getSumVeriDefloss().doubleValue());

				
				personDataVo.setCheckerCode(checkVo.getPrpLCheckTask().getCheckerCode());
				personDataVo.setCheckerCertiCode(checkVo.getPrpLCheckTask().getCheckerIdfNo());

				if(StringUtils.isBlank(persTraceMainVo.getOperatorCertiCode())){
					personDataVo.setEstimateCertiCode(persTraceMainVo.getPlfCertiCode());
				} else {
					personDataVo.setEstimateCertiCode(persTraceMainVo.getOperatorCertiCode());
				}
				personDataVo.setDeathTime(dlossPersTraceVo.getPrpLDlossPersInjured().getDeathTime());
				personDataVo.setCountryInjuryType(dlossPersTraceVo.getPrpLDlossPersInjured().getWoundCode());
				
				underDefLoss += NullToZero(dlossPersTraceVo.getSumVeriDefloss()).doubleValue();
				
				// 人员损失费用明细（多条）
				List<SHCIEndCaseAddReqPersonLossPartDataVo> partDataListVo = new ArrayList<SHCIEndCaseAddReqPersonLossPartDataVo>();
				for(PrpLDlossPersTraceFeeVo persTraceFeeVo:dlossPersTraceVo.getPrpLDlossPersTraceFees()){
					SHCIEndCaseAddReqPersonLossPartDataVo partDataVo = new SHCIEndCaseAddReqPersonLossPartDataVo();
					
					String shFeeType = persTraceFeeVo.getFeeTypeCode().length()==1 ? "0"+persTraceFeeVo.getFeeTypeCode() : persTraceFeeVo.getFeeTypeCode();
					partDataVo.setFeeType(shFeeType);// SHFeetype
					partDataVo.setLossAmount(NullToZero(persTraceFeeVo.getVeriDefloss()).doubleValue());
					partDataVo.setUnderDefLoss(NullToZero(persTraceFeeVo.getVeriDefloss()).doubleValue());
					partDataListVo.add(partDataVo);
				}
				personDataVo.setLossPartDataVo(partDataListVo);

				// 人员受伤部位（多条）
				List<SHCIEndCaseAddReqInjuryDataVo> injuryDataListVo = new ArrayList<SHCIEndCaseAddReqInjuryDataVo>();
				for(PrpLDlossPersExtVo persExtVo:persInjuredVo.getPrpLDlossPersExts()){
					SHCIEndCaseAddReqInjuryDataVo injuryDataVo = new SHCIEndCaseAddReqInjuryDataVo();
					injuryDataVo.setInjuryPart(persExtVo.getInjuredPart());//
					String injuryLevelCode =persExtVo.getWoundGrade();
					if(StringUtils.isBlank(persExtVo.getWoundGrade()) ||"10".equals(injuryLevelCode)){
						injuryLevelCode ="10";
					}else{
						injuryLevelCode ="0" + injuryLevelCode;
					}
					injuryDataVo.setInjuryLevelCode(injuryLevelCode); //伤残程度代码
					injuryDataListVo.add(injuryDataVo);
				}
				personDataVo.setInjuryDataVo(injuryDataListVo);

				// 人员治疗机构（多条）
				List<SHCIEndCaseAddReqHospitalInfoDataVo> hospitalInfoDataListVo = new ArrayList<SHCIEndCaseAddReqHospitalInfoDataVo>();
				for(PrpLDlossPersHospitalVo hospitalVo:persInjuredVo.getPrpLDlossPersHospitals()){
					SHCIEndCaseAddReqHospitalInfoDataVo hospitalInfoDataVo = new SHCIEndCaseAddReqHospitalInfoDataVo();
					hospitalInfoDataVo.setHospitalName(hospitalVo.getHospitalName());
					hospitalInfoDataVo.setHospitalFactoryCertiCode(hospitalVo.getHospitalCode());
					hospitalInfoDataListVo.add(hospitalInfoDataVo);
				}
				personDataVo.setHospitalInfoDataVo(hospitalInfoDataListVo);

				List<SHCIVClaimInjuryIdentifyInfoDataVo> ideInfoDataList = new ArrayList<SHCIVClaimInjuryIdentifyInfoDataVo>();
				SHCIVClaimInjuryIdentifyInfoDataVo injuryIdentifyInfoDataVo = new SHCIVClaimInjuryIdentifyInfoDataVo();
				injuryIdentifyInfoDataVo.setInjuryIdentifyName(persInjuredVo.getChkComName());
				injuryIdentifyInfoDataVo.setInjuryIdentifyCertiCode(persInjuredVo.getChkComCode());
				ideInfoDataList.add(injuryIdentifyInfoDataVo);
				personDataVo.setInjuryIdentifyInfoData(ideInfoDataList);
				//
				personDataVos.add(personDataVo);
			}
		}
		
		// 车辆损失情况（多条）[上传增量的数据]
		List<PrpLDlossCarMainVo> dlossCarMainVos = new ArrayList<PrpLDlossCarMainVo>();
		if(lossItemVoList != null && lossItemVoList.size() > 0){
			for(PrpLLossItemVo lossItemVo : lossItemVoList){
				PrpLDlossCarMainVo carMainVo = lossCarService.findLossCarMainById(lossItemVo.getDlossId());
				dlossCarMainVos.add(carMainVo);
			}
		}
		int carNum = 0;
		List<SHCIEndCaseAddReqVehicleDataVo> vehicleDataListVo = new ArrayList<SHCIEndCaseAddReqVehicleDataVo>();
		if(dlossCarMainVos != null && dlossCarMainVos.size() > 0){
			for(PrpLDlossCarMainVo dlossCarMainVo : dlossCarMainVos){
				//车辆（定损方式为无损失的不上传平台）定损注销不上传平台
				if(CodeConstants.CetainLossType.DEFLOSS_NULL.equals(dlossCarMainVo.getCetainLossType()) ||
						CodeConstants.VeriFlag.CANCEL.equals(dlossCarMainVo.getUnderWriteFlag())){
					continue;
				}
				//如果车辆核损金额为0，则不上传平台，要不然会影响上传损失率
				if(NullToZero(dlossCarMainVo.getSumVeriLossFee()).compareTo(BigDecimal.ZERO) == 0){
					continue;
				}
				carNum++;
				PrpLDlossCarInfoVo carInfoVo = dlossCarMainVo.getLossCarInfoVo();
				// 车辆损失情况
				SHCIEndCaseAddReqVehicleDataVo vehicleDataVo = new SHCIEndCaseAddReqVehicleDataVo();
				vehicleDataVo.setCarMark(carInfoVo.getLicenseNo());
				vehicleDataVo.setVehicleType(carInfoVo.getLicenseType()==null ? "02":carInfoVo.getLicenseType());
				vehicleDataVo.setLossAmount(NullToZero(dlossCarMainVo.getSumVeriLossFee()).doubleValue());//
				vehicleDataVo.setMainThird("1".equals(dlossCarMainVo.getDeflossCarType()) ? "1" : "0");//
				vehicleDataVo.setRobber("03".equals(dlossCarMainVo.getCetainLossType()) ? "1" : "0");
				vehicleDataVo.setSurveyType("1".equals(checkVo.getPrpLCheckTask().getFirstAddressFlag()) ? "1" : "3");
				vehicleDataVo.setSurveyName(checkVo.getPrpLCheckTask().getChecker());
				vehicleDataVo.setSurveyStartTime(checkVo.getCreateTime());
				vehicleDataVo.setSurveyEndTime(checkVo.getChkSubmitTime());
				vehicleDataVo.setSurveyPlace(checkVo.getPrpLCheckTask().getCheckAddress());
				vehicleDataVo.setSurveyDes(checkVo.getPrpLCheckTask().getContexts());
				String codeCName = codeTranService.findCodeName("UserCodeSH",dlossCarMainVo.getHandlerCode());
				if(StringUtils.isEmpty(codeCName)){
					codeCName = codeTranService.findCodeName("UserCodeSH",dlossCarMainVo.getCreateUser());
				}
				vehicleDataVo.setEstimateName(StringUtils.isNotEmpty(codeCName) ? codeCName : "GSU0000001");
//				vehicleDataVo.setEstimateName(dlossCarMainVo.getHandlerCode());
				vehicleDataVo.setEstimateStartTime(null);// 定损开始时间
				vehicleDataVo.setEstimateEndTime(null);// 定损结束时间
				vehicleDataVo.setAssesorName(dlossCarMainVo.getUnderWriteName());
				vehicleDataVo.setAssesorStartTime(null);// 核损开始时间
				vehicleDataVo.setAssesorEndTime(null);// 核损结束时间
				vehicleDataVo.setRemnant(0.0);
				vehicleDataVo.setCharg4eFee(0.0);
				Double repairSum = 0.0;
				for(PrpLDlossCarRepairVo carRepairVo:dlossCarMainVo.getPrpLDlossCarRepairs()){
					repairSum += NullToZero(carRepairVo.getSumVeriLoss()).doubleValue();//
				}
				vehicleDataVo.setTotalWorkingHour(repairSum);
				String changeOrRepairPart = "0";
				List<PrpLDlossCarRepairVo> carRepairVoList = dlossCarMainVo.getPrpLDlossCarRepairs();
				List<PrpLDlossCarCompVo> carCompVoList = dlossCarMainVo.getPrpLDlossCarComps();
				if((carRepairVoList != null && carRepairVoList.size() > 0) ||
						(carCompVoList != null && carCompVoList.size() > 0)){
					changeOrRepairPart = "1";
				}
				vehicleDataVo.setChangeOrRepairPart(changeOrRepairPart);
				vehicleDataVo.setJyVehicleCode(carInfoVo.getModelCode());
				vehicleDataVo.setClaimVehicleCode(carInfoVo.getModelCode());
				vehicleDataVo.setClaimVehicleName(carInfoVo.getModelName());
				String madeFactory = carInfoVo.getFactoryName();
				vehicleDataVo.setMadeFactory(StringUtils.isEmpty(madeFactory) ? "无" : madeFactory);
				vehicleDataVo.setVehicleBrandCode(carInfoVo.getBrandCode());
				vehicleDataVo.setVehicleCatenaCode(carInfoVo.getSeriCode());
				vehicleDataVo.setVehicleGroupCode(carInfoVo.getGroupCode());
				SysCodeDictVo codeVo = codeTranService.findTransCodeDictVo
						("RepairFactoryType",dlossCarMainVo.getRepairFactoryType());
				vehicleDataVo.setPriceSltCode(codeVo != null ? codeVo.getProperty2() : "99");
				
				String defineFlag = "0";
				if(carCompVoList != null && carCompVoList.size() > 0){
					if("1".equals(dlossCarMainVo.getPrpLDlossCarComps().get(0).getSelfConfigFlag())){
						defineFlag = "1";
					}
				}
				vehicleDataVo.setDefineFlag(defineFlag);
				vehicleDataVo.setVin(carInfoVo.getVinNo());
				vehicleDataVo.setEngineNo(carInfoVo.getEngineNo());
				vehicleDataVo.setModel(carInfoVo.getBrandName());
				vehicleDataVo.setDriverName(carInfoVo.getDriveName());
				//String certiType = "1".equals(carInfoVo.getIdentifyType())?"01":"99";
				SysCodeDictVo sysdictVo = codeTranService.findTransCodeDictVo
		                ("IdentifyType",carInfoVo.getIdentifyType());
				vehicleDataVo.setCertiType(sysdictVo==null?"99":sysdictVo.getProperty4());
				vehicleDataVo.setCertiCode(carInfoVo.getIdentifyNo());
				vehicleDataVo.setDriverLicenseNo(carInfoVo.getDrivingLicenseNo());
				vehicleDataVo.setTemporaryFlag("0");
				
				vehicleDataVo.setCheckerCode(checkVo.getPrpLCheckTask().getCheckerCode());
				vehicleDataVo.setCheckerCertiCode(checkVo.getPrpLCheckTask().getCheckerIdfNo());
				vehicleDataVo.setUnderWriteCode(dlossCarMainVo.getUnderWriteCode());
				vehicleDataVo.setUnderWriteCertiCode(dlossCarMainVo.getUnderWiteIdNo());
				vehicleDataVo.setUnderDefLoss(NullToZero(dlossCarMainVo.getSumVeriChargeFee()).doubleValue());
				
				underDefLoss += NullToZero(dlossCarMainVo.getSumVeriChargeFee()).doubleValue();

				// 车辆损失部位（多条）
				List<SHCIEndCaseAddReqCarLossPartDataVo> carLossPartDataListVo = new ArrayList<SHCIEndCaseAddReqCarLossPartDataVo>();
				String[] lossPartArr = dlossCarMainVo.getLossPart().split(",");
				for (int i = 0; i < lossPartArr.length; i++) {
					String lossPart = lossPartArr[i];
					SHCIEndCaseAddReqCarLossPartDataVo carLossPartData = new SHCIEndCaseAddReqCarLossPartDataVo();
					SysCodeDictVo sysVo = codeTranService.findTransCodeDictVo("LossPart",lossPart);
					carLossPartData.setLossPart(sysVo==null?"01":sysVo.getProperty3());
					carLossPartDataListVo.add(carLossPartData);
				}
				vehicleDataVo.setCarLossPartDataVo(carLossPartDataListVo);

				// 车辆配件明细（多条）
				List<SHCIEndCaseAddReqFittingDataVo> fittingDataListVo = new ArrayList<SHCIEndCaseAddReqFittingDataVo>();
				for(PrpLDlossCarCompVo carCompVo:dlossCarMainVo.getPrpLDlossCarComps()){// 配件
					SHCIEndCaseAddReqFittingDataVo dataVo = new SHCIEndCaseAddReqFittingDataVo();
					dataVo.setChangePartName(carCompVo.getCompName());
					dataVo.setChangePartNum(DataUtils.NullToZeroInt(carCompVo.getQuantity()).doubleValue());
					dataVo.setChangePartFee(NullToZero(carCompVo.getManHourUnitPrice()).doubleValue());
					dataVo.setChangePartTime(NullToZero(carCompVo.getManHour()).doubleValue());
					dataVo.setChangePartManpowerFee(NullToZero(carCompVo.getVeriManHourFee()).doubleValue());
					dataVo.setRepairPartName(null);
					dataVo.setRepairPartNum(null);
					dataVo.setRepairPartFee(null);
					dataVo.setRepairPartTime(null);
					dataVo.setRepairPartManpowerFee(null);
					dataVo.setRepairMethord(null);

					dataVo.setJyPartCode(carCompVo.getCompCode());
					dataVo.setOemPartCode(carCompVo.getOriginalId());
					dataVo.setDefineFlag("1".equals(carCompVo.getSelfConfigFlag()) ? "1" : "0");

					if(carCompVo.getSelfConfigFlag() != null && "1".equals(carCompVo.getSelfConfigFlag())){
						dataVo.setPriceType("02");
					}
					dataVo.setSubjionFlag("0");

					fittingDataListVo.add(dataVo);
				}
				//当有修理或更换配件的时候，更换配件名称、更换配件件数、更换配件材料费（单价）、更换配件人工费和修理配件名称、修理配件件数、修理配件材料费（单价）、修理配件人工费必须有一组非空
				for(PrpLDlossCarRepairVo carRepairVo:dlossCarMainVo.getPrpLDlossCarRepairs()){// 修理
					SHCIEndCaseAddReqFittingDataVo dataVo = new SHCIEndCaseAddReqFittingDataVo();
					dataVo.setChangePartName(carRepairVo.getCompName());
					dataVo.setChangePartNum(NullToZero(carRepairVo.getVeriMaterQuantity()).doubleValue());
					dataVo.setChangePartFee(NullToZero(carRepairVo.getVeriMaterUnitPrice()).doubleValue());
					dataVo.setChangePartTime(NullToZero(carRepairVo.getVeriManHour()).doubleValue());
					dataVo.setChangePartManpowerFee(NullToZero(carRepairVo.getVeriManUnitPrice()).doubleValue());
					dataVo.setRepairPartName(carRepairVo.getCompName());
					dataVo.setRepairPartNum(NullToZero(carRepairVo.getVeriMaterQuantity()).intValue());
					dataVo.setRepairPartFee(NullToZero(carRepairVo.getVeriMaterUnitPrice()).doubleValue());
					dataVo.setRepairPartTime(NullToZero(carRepairVo.getVeriManHour()).doubleValue());
					dataVo.setRepairPartManpowerFee(NullToZero(carRepairVo.getVeriManHourFee()).doubleValue());

					dataVo.setRepairMethord(carRepairVo.getRepairType());

					dataVo.setJyPartCode(carRepairVo.getCompCode());
					dataVo.setOemPartCode(carRepairVo.getPartCode());
					dataVo.setDefineFlag("1".equals(carRepairVo.getSelfConfigFlag()) ? "1" : "0");

					if(carRepairVo.getSelfConfigFlag() != null && "1".equals(carRepairVo.getSelfConfigFlag())){
						dataVo.setPriceType("02");
					}
					
					dataVo.setSubjionFlag("0");

					fittingDataListVo.add(dataVo);
				}

				vehicleDataVo.setFittingDataVo(fittingDataListVo);
				
				if("1".equals(changeOrRepairPart)){// 开票修理机构（多条）
					List<SHCIEndCaseAddReqRepairDataVo> repartDataVoList = new ArrayList<SHCIEndCaseAddReqRepairDataVo>();
					SHCIEndCaseAddReqRepairDataVo repartVo = new SHCIEndCaseAddReqRepairDataVo();
					repartVo.setRepairOrg("JY00008782");
					repartDataVoList.add(repartVo);
					vehicleDataVo.setRepairDataVo(repartDataVoList);
				}
				if("1".equals(changeOrRepairPart)){// 承修修理机构（多条）
					List<SHCIEndCaseAddReqActuralRepairDataVo> acturalRepairList = new ArrayList<SHCIEndCaseAddReqActuralRepairDataVo>();
					SHCIEndCaseAddReqActuralRepairDataVo repairDataVo = new SHCIEndCaseAddReqActuralRepairDataVo();
					repairDataVo.setActuralRepairOrg("JY00008782");
					acturalRepairList.add(repairDataVo);
					vehicleDataVo.setActuralRepairDataVo(acturalRepairList);
				}

//				// 开票修理机构（多条）
//				List<SHCIEndCaseAddReqRepairDataVo> repairDataListVo = new ArrayList<SHCIEndCaseAddReqRepairDataVo>();
//				for(PrpLDlossCarRepairVo repairVo:dlossCarMainVo.getPrpLDlossCarRepairs()){
//					SHCIEndCaseAddReqRepairDataVo repairDataVo = new SHCIEndCaseAddReqRepairDataVo();
//					repairDataVo.setRepairOrg(repairVo.getRepairCode());
//				}
//				vehicleDataVo.setRepairDataVo(repairDataListVo);
//
//				// 承修修理机构（多条）
//				vehicleDataVo.setActuralRepairDataVo(null);

				// 添加
				vehicleDataListVo.add(vehicleDataVo);
			}
		}
		
		
		// 物损损失情况（多条）
		List<SHCIEndCaseAddReqObjDataVo> objDataListVo = new ArrayList<SHCIEndCaseAddReqObjDataVo>();
		List<PrpLdlossPropMainVo> dlossPropVoList = new ArrayList<PrpLdlossPropMainVo>();
		if(lossPropVoList != null && lossPropVoList.size() > 0){
			for(PrpLLossPropVo lossPropVo : lossPropVoList){
				PrpLdlossPropMainVo prop = propTaskService.findPropMainVoById(lossPropVo.getDlossId());
				dlossPropVoList.add(prop);
			}
		}
		if(dlossPropVoList != null && dlossPropVoList.size() > 0){
			isProtectLoss ="1";			
			for(PrpLdlossPropMainVo propLossMainVo : dlossPropVoList){
				for(PrpLdlossPropFeeVo propFee:propLossMainVo.getPrpLdlossPropFees()){
					// 物损损失情况
					SHCIEndCaseAddReqObjDataVo objDataVo = new SHCIEndCaseAddReqObjDataVo();
					objDataVo.setObjectDesc(propFee.getLossItemName());
					objDataVo.setLossNum(propFee.getLossQuantity().intValue());
					objDataVo.setLossAmount(propFee.getVeriUnitPrice().doubleValue());
					objDataVo.setMainThird("1".equals(propLossMainVo.getLossType()) ? "1" : "0");
					objDataVo.setSurveyType("1".equals(checkVo.getPrpLCheckTask().getFirstAddressFlag()) ? "1" : "3");
					objDataVo.setSurveyName(checkVo.getPrpLCheckTask().getChecker());
					objDataVo.setSurveyStartTime(checkVo.getCreateTime());
					objDataVo.setSurveyEndTime(checkVo.getChkSubmitTime());
					objDataVo.setSurveyPlace(checkVo.getPrpLCheckTask().getCheckAddress());
					objDataVo.setSurveyDes(checkVo.getPrpLCheckTask().getContexts());
					String codeCName = codeTranService.findCodeName("UserCodeSH",propLossMainVo.getHandlerCode());
					if(StringUtils.isEmpty(codeCName)){
						codeCName = codeTranService.findCodeName("UserCodeSH",propLossMainVo.getCreateUser());
					}
					objDataVo.setEstimateName(StringUtils.isNotEmpty(codeCName) ? codeCName : "GSU0000001");
//					objDataVo.setEstimateName(propLossMainVo.getHandlerCode());
					objDataVo.setEstimateStartTime(propLossMainVo.getCreateTime());
					objDataVo.setEstimateEndTime(propLossMainVo.getUpdateTime());
					objDataVo.setAssesorName(propLossMainVo.getUnderWriteName());
					objDataVo.setAssesorStartTime(null);
					objDataVo.setAssesorEndTime(null);

					String protectProperty = "1";
					if("1".equals(propLossMainVo.getLossType())){
						protectProperty = "1";  //本车财产
					}else{
						protectProperty = "2";  // 车外财产
					}
					objDataVo.setProtectProperty(protectProperty);
					objDataVo.setCheckerCode(checkVo.getPrpLCheckTask().getCheckerCode());
					objDataVo.setCheckerCertiCode(checkVo.getPrpLCheckTask().getCheckerIdfNo());
					objDataVo.setUnderWriteCode(propLossMainVo.getUnderWriteCode());
					objDataVo.setUnderWriteCertiCode(propLossMainVo.getUnderWriteIdCard());
					objDataVo.setUnderDefLoss(NullToZero(propLossMainVo.getSumVeriLoss()).doubleValue());
					underDefLoss += NullToZero(propLossMainVo.getSumVeriLoss()).doubleValue();
					
					objDataListVo.add(objDataVo);
				}
			}
		}
		baseVo.setIsSingleAccident(carNum==1?"1":"0");
		baseVo.setIsPersonInjured(isPersonInjured);
		baseVo.setIsProtectLoss(isProtectLoss);
		baseVo.setUnderDefLoss(underDefLoss);
		bodyVo.setBasePartVo(baseVo);
		bodyVo.setClaimCoverListVo(claimCoverListVo);
		bodyVo.setVehicleDataVo(vehicleDataListVo);
		bodyVo.setObjDataVo(objDataListVo);
		bodyVo.setPersonDataVo(personDataVos);
		bodyVo.setDocDataVo(null);
		
		return bodyVo;
	}

	/**
	 * 获取增量的人伤
	 * @param lossPersonVoList
	 * @return
	 */
	private PrpLDlossPersTraceMainVo getPersTraceMainVo(List<PrpLLossPersonVo> lossPersonVoList){
		PrpLDlossPersTraceMainVo persTraceMainVo = null;
		if(lossPersonVoList != null && lossPersonVoList.size() > 0){
			persTraceMainVo = persTraceService.findPersTraceMainByPk(lossPersonVoList.get(0).getDlossId());
			List<PrpLDlossPersTraceVo> personVoList = new ArrayList<PrpLDlossPersTraceVo>();
			for(PrpLLossPersonVo lossPersonVo : lossPersonVoList){
				personVoList.add(persTraceService.findPersTraceByPK(lossPersonVo.getPersonId()));
			}
			if(persTraceMainVo != null){
				persTraceMainVo.setPrpLDlossPersTraces(personVoList);
			}
		}
		return persTraceMainVo;
	}
	
	// 组织商业险报文
	public SHBIEndCaseAddReqBodyVo setBodyBI_SH(String claimSeqNo,PrpLCompensateVo compensateVo,PrpLReCaseVo reCaseVo) {
//		PrpLReCaseVo reCaseVo = reOpenCaseService.findReCaseVoByEndCaseNo(endCaseNo);
		String registNo = compensateVo.getRegistNo();
//		PrpLCompensateVo compeVo = compensateTaskService.findCompByClaimNo(claimVo.getClaimNo());
		PrpLRegistVo registVo = registService.findByRegistNo(registNo);
//		PrpLCheckDutyVo dutyVo = checkService.findCheckDuty(registNo,1);
		PrpLCheckVo checkVo = checkService.findCheckVoByRegistNo(registNo);
		
		
		SHBIEndCaseAddReqBodyVo bodyVo = new SHBIEndCaseAddReqBodyVo();
		
		//基本信息
		SHCIEndCaseAddReqBasePartVo baseVo = new SHCIEndCaseAddReqBasePartVo();
		baseVo.setClaimCode(claimSeqNo);
		String reasonCode = reCaseVo.getOpenReasonCode();
		baseVo.setReasonType("1".equals(reasonCode) ? reasonCode : "2");
//		baseVo.setReasonType(reCaseVo.getOpenReasonCode());
		baseVo.setNumerationStartTime(compensateVo.getCreateTime());
		baseVo.setNumerationEndTime(compensateVo.getUpdateTime());
		baseVo.setAddTime(reCaseVo.getOpenCaseDate());
		baseVo.setRemark(registVo.getPrpLRegistExt().getRemark());
		baseVo.setEstimate(NullToZero(compensateVo.getSumPaidAmt()).doubleValue());
		baseVo.setRegistNo(registNo);
		
		String isPersonInjured = "0"; 
		String isProtectLoss = "0";
		double underDefLoss = 0L;
		
		//追加损失赔偿情况
		List<SHCIEndCaseAddReqClaimCoverListVo> claimCoverListVo = new ArrayList<SHCIEndCaseAddReqClaimCoverListVo>();
		// 车
		List<PrpLLossItemVo> lossItemVoList = compensateVo.getPrpLLossItems();
		if (lossItemVoList != null && lossItemVoList.size() > 0) {
			for (PrpLLossItemVo lossItemVo : lossItemVoList) {
				SHCIEndCaseAddReqClaimCoverListVo claimCoverVo = new SHCIEndCaseAddReqClaimCoverListVo();
				claimCoverVo.setPolicyNo(lossItemVo.getPolicyNo());
				int duty = lossItemVo.getDutyRate()==null?5:100D==lossItemVo.getDutyRate().doubleValue()?1:3;
				claimCoverVo.setLiabilityRate(duty);
				claimCoverVo.setClaimFeeType("1");
				
				//String coverageCode = getRiskCodeVal(lossItemVo.getRiskCode(), lossItemVo.getKindCode());
				String coverageCode = kindCodeTranService.findTransCiCode("22",lossItemVo.getRiskCode(), "CovergeCodeSH", lossItemVo.getKindCode());
				claimCoverVo.setCoverageCode(coverageCode);
				if(isFeeUpdate(lossItemVo.getRiskCode())){
					String coverageType = "1".equals(lossItemVo.getItemId()) ? "3" : "2";//本车3(商业车损险)，其他车2(商业三者险)
					if(coverageCode.startsWith("F")){
						claimCoverVo.setCoverageType("9");
					}else{
						claimCoverVo.setCoverageType(coverageType);
					}
				}
				claimCoverVo.setComCoverageCode(lossItemVo.getKindCode());
				claimCoverVo.setLossAmount(NullToZero(lossItemVo.getSumLoss()).doubleValue());
				claimCoverVo.setClaimAmount(NullToZero(lossItemVo.getSumRealPay()).doubleValue());
				claimCoverVo.setSalvageFee(NullToZero(lossItemVo.getRescueFee()).doubleValue());
				//
				if("A1".equals(lossItemVo.getKindCode())){
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
	            		claimCoverVo.setIsDeviceItem("0");
	            	}else{
	            		claimCoverVo.setIsDeviceItem("1");
	            	}
	            }

				claimCoverListVo.add(claimCoverVo);
			}
		}
		// 财
		List<PrpLLossPropVo> lossPropVoList = compensateVo.getPrpLLossProps();
		if (lossPropVoList != null && lossPropVoList.size() > 0) {
			for (PrpLLossPropVo lossPropVo : lossPropVoList) {
				if(CodeConstants.KINDCODE.KINDCODE_RS.equals(lossPropVo.getKindCode()) || CodeConstants.KINDCODE.KINDCODE_VS.equals(lossPropVo.getKindCode()) ||
						CodeConstants.KINDCODE.KINDCODE_DS.equals(lossPropVo.getKindCode()) || CodeConstants.KINDCODE.KINDCODE_DC.equals(lossPropVo.getKindCode())){
					continue;
				}
				SHCIEndCaseAddReqClaimCoverListVo claimCoverVo = new SHCIEndCaseAddReqClaimCoverListVo();
				claimCoverVo.setPolicyNo(lossPropVo.getPolicyNo());
				claimCoverVo.setLiabilityRate(0);
				claimCoverVo.setClaimFeeType("2");
				//String coverageCode = getRiskCodeVal(lossPropVo.getRiskCode(), lossPropVo.getKindCode());
				String coverageCode = kindCodeTranService.findTransCiCode("22",lossPropVo.getRiskCode(), "CovergeCodeSH", lossPropVo.getKindCode());
				if("X3".equals(lossPropVo.getKindCode())){
					claimCoverVo.setCoverageCode(kindCodeTranService.findTransCiCode("00", lossPropVo.getRiskCode(), "CovergeCode", lossPropVo.getKindCode()));
				}else{
					claimCoverVo.setCoverageCode(coverageCode);
				}
				
				if(isFeeUpdate(lossPropVo.getRiskCode())){
					String coverageType = "1".equals(lossPropVo.getItemId()) ? "3" : "2";//本车3(商业车损险)，其他车2(商业三者险)
					//ItemId等于空的是附加险
					if(coverageCode.startsWith("F")){
						claimCoverVo.setCoverageType("9");//附加险
					}else{
						claimCoverVo.setCoverageType(coverageType);//本车财传3，其他的传2
					}
				}
				if("X3".equals(lossPropVo.getKindCode())){
					claimCoverVo.setComCoverageCode("A1");
				}else{
					claimCoverVo.setComCoverageCode(lossPropVo.getKindCode());
				}
				
				
				claimCoverVo.setLossAmount(NullToZero(lossPropVo.getSumLoss()).doubleValue());
				claimCoverVo.setClaimAmount(NullToZero(lossPropVo.getSumRealPay()).doubleValue());
				claimCoverVo.setSalvageFee(NullToZero(lossPropVo.getRescueFee()).doubleValue());
				//为新增设备险时
				if("X3".equals(lossPropVo.getKindCode())){
					claimCoverVo.setIsDeviceItem("1");
				 }
				claimCoverListVo.add(claimCoverVo);
			}
		}
		// 人
		List<PrpLLossPersonVo> lossPersonVoList = compensateVo.getPrpLLossPersons();
		if (lossPersonVoList != null && lossPersonVoList.size() > 0) {
			for (PrpLLossPersonVo lossPersonVo : lossPersonVoList) {
				SHCIEndCaseAddReqClaimCoverListVo claimCoverVo = new SHCIEndCaseAddReqClaimCoverListVo();
				claimCoverVo.setPolicyNo(lossPersonVo.getPolicyNo());
				claimCoverVo.setLiabilityRate(0);
				claimCoverVo.setClaimFeeType("3");
				// claimCoverVo.setCoverageType(isFeeUpdate(lossItemVo.getRiskCode()) ? "2" : "");
				//String coverageCode = getRiskCodeVal(lossPersonVo.getRiskCode(),lossPersonVo.getKindCode());
				String coverageCode = kindCodeTranService.findTransCiCode("22",lossPersonVo.getRiskCode(), "CovergeCodeSH", lossPersonVo.getKindCode());
				claimCoverVo.setCoverageCode(coverageCode);
				if(isFeeUpdate(lossPersonVo.getRiskCode())){
					String coverageType = "1".equals(lossPersonVo.getItemId()) ? "3" : "2";//本车3(商业车损险)，其他车2(商业三者险)
					if(coverageCode.startsWith("F")){
						claimCoverVo.setCoverageType("9");//附加险
					}else{
						claimCoverVo.setCoverageType(coverageType);//
					}
				}
				claimCoverVo.setComCoverageCode(lossPersonVo.getKindCode());
				claimCoverVo.setLossAmount(NullToZero(lossPersonVo.getSumLoss()).doubleValue());
				claimCoverVo.setClaimAmount(NullToZero(lossPersonVo.getSumRealPay()).doubleValue());
				claimCoverVo.setSalvageFee(0.0);
				//
				claimCoverListVo.add(claimCoverVo);
			}
		}
		
		// 人员损失情况（多条）
		PrpLDlossPersTraceMainVo persTraceMainVo = getPersTraceMainVo(lossPersonVoList);
		
		List<PrpLDlossPersTraceVo> persTraceVoList = null;
		if(persTraceMainVo != null){
			persTraceVoList = persTraceMainVo.getPrpLDlossPersTraces();
		}
//		List<PrpLDlossPersTraceMainVo> persTraceMainVos = persTraceService.findPersTraceMainVoList(registNo);
		
		List<SHCIEndCaseAddReqPersonDataVo> personDataVos = new ArrayList<SHCIEndCaseAddReqPersonDataVo>();
		if(persTraceVoList != null && persTraceVoList.size() > 0){
			isPersonInjured = "1";
			for(PrpLDlossPersTraceVo dlossPersTraceVo : persTraceVoList){
				PrpLDlossPersInjuredVo persInjuredVo = dlossPersTraceVo.getPrpLDlossPersInjured();

				// 人员损失情况
				SHCIEndCaseAddReqPersonDataVo personDataVo = new SHCIEndCaseAddReqPersonDataVo();
				personDataVo.setPersonName(persInjuredVo.getPersonName());
				personDataVo.setPersonId(persInjuredVo.getCertiCode());
				personDataVo.setAge(NullToZero(persInjuredVo.getPersonAge()).intValue());
				personDataVo.setLossAmount(NullToZero(dlossPersTraceVo.getSumVeriDefloss()).doubleValue());
				personDataVo.setMainThird("1".equals(dlossPersTraceVo.getLossFeeType()) ? "1" : "0");
				personDataVo.setSurveyType("0".equals(checkVo.getPrpLCheckTask().getFirstAddressFlag()) ? "3" : "1");
				personDataVo.setSurveyName(checkVo.getPrpLCheckTask().getChecker());
				personDataVo.setSurveyStartTime(checkVo.getCreateTime());
				personDataVo.setSurveyEndTime(checkVo.getChkSubmitTime());
				personDataVo.setSurveyPlace(checkVo.getPrpLCheckTask().getCheckAddress());
				personDataVo.setSurveyDes(checkVo.getPrpLCheckTask().getContexts());
//				personDataVo.setEstimateName(persTraceMainVo.getOperatorName());
				String codeCName = codeTranService.findCodeName("UserCodeSH",persTraceMainVo.getOperatorCode());
				if(StringUtils.isEmpty(codeCName)){
					codeCName = codeTranService.findCodeName("UserCodeSH",persTraceMainVo.getCreateUser());
				}
				personDataVo.setEstimateName(StringUtils.isNotEmpty(codeCName) ? codeCName : "GSU0000001");
				personDataVo.setEstimateStartTime(persTraceMainVo.getPlfSubTime());
				personDataVo.setEstimateEndTime(persTraceMainVo.getUndwrtFeeEndDate());
				personDataVo.setAssesorName(persTraceMainVo.getUndwrtFeeName());
				personDataVo.setAssesorStartTime(persTraceMainVo.getCreateTime());
				personDataVo.setAssesorEndTime(persTraceMainVo.getUpdateTime());
				personDataVo.setInjuryType("");
			//	personDataVo.setInjuryLevel(persInjuredVo.getWoundCode());
				personDataVo.setMedicalType(persInjuredVo.getTreatSituation());
				personDataVo.setUnderWriteName(persTraceMainVo.getUndwrtFeeName());
				personDataVo.setUnderWriteCode(persTraceMainVo.getUndwrtFeeCode());
				personDataVo.setEstimateAddr("");
				Date inHospitalDate = null;
				if(persInjuredVo.getPrpLDlossPersHospitals() != null && persInjuredVo.getPrpLDlossPersHospitals().size() > 0){
					inHospitalDate = persInjuredVo.getPrpLDlossPersHospitals().get(0).getInHospitalDate();
				}
				personDataVo.setAddmissionTime(inHospitalDate);
				personDataVo.setUnderDefloss(NullToZero(dlossPersTraceVo.getSumVeriDefloss()).doubleValue());
				
				underDefLoss += dlossPersTraceVo.getSumVeriDefloss().doubleValue();
				personDataVo.setCheckerCode(checkVo.getPrpLCheckTask().getCheckerCode());
				personDataVo.setCheckerCertiCode(checkVo.getPrpLCheckTask().getCheckerIdfNo());
				if(StringUtils.isBlank(persTraceMainVo.getOperatorCertiCode())){
					personDataVo.setEstimateCertiCode(persTraceMainVo.getPlfCertiCode());
				} else {
					personDataVo.setEstimateCertiCode(persTraceMainVo.getOperatorCertiCode());
				}
				personDataVo.setDeathTime(dlossPersTraceVo.getPrpLDlossPersInjured().getDeathTime());
				personDataVo.setCountryInjuryType(dlossPersTraceVo.getPrpLDlossPersInjured().getWoundCode());

				// 人员损失费用明细（多条）
				List<SHCIEndCaseAddReqPersonLossPartDataVo> partDataListVo = new ArrayList<SHCIEndCaseAddReqPersonLossPartDataVo>();
				for(PrpLDlossPersTraceFeeVo persTraceFeeVo:dlossPersTraceVo.getPrpLDlossPersTraceFees()){
					SHCIEndCaseAddReqPersonLossPartDataVo partDataVo = new SHCIEndCaseAddReqPersonLossPartDataVo();
					partDataVo.setFeeType("");// SHFeetype
					partDataVo.setLossAmount(NullToZero(persTraceFeeVo.getVeriDefloss()).doubleValue());
					partDataVo.setUnderDefLoss(NullToZero(persTraceFeeVo.getVeriDefloss()).doubleValue()); //核损金额
					partDataListVo.add(partDataVo);
				}
				personDataVo.setLossPartDataVo(partDataListVo);

				// 人员受伤部位（多条）
				List<SHCIEndCaseAddReqInjuryDataVo> injuryDataListVo = new ArrayList<SHCIEndCaseAddReqInjuryDataVo>();
				for(PrpLDlossPersExtVo persExtVo:persInjuredVo.getPrpLDlossPersExts()){
					SHCIEndCaseAddReqInjuryDataVo injuryDataVo = new SHCIEndCaseAddReqInjuryDataVo();
					injuryDataVo.setInjuryPart(persExtVo.getInjuredPart());//
					String injuryLevelCode =persExtVo.getWoundGrade();
					if(StringUtils.isBlank(persExtVo.getWoundGrade()) ||"10".equals(injuryLevelCode)){
						injuryLevelCode ="10";
					}else{
						injuryLevelCode ="0" + injuryLevelCode;
					}
					injuryDataVo.setInjuryLevelCode(injuryLevelCode); //伤残程度代码
					injuryDataListVo.add(injuryDataVo);
				}
				personDataVo.setInjuryDataVo(injuryDataListVo);

				// 人员治疗机构（多条）
				List<SHCIEndCaseAddReqHospitalInfoDataVo> hospitalInfoDataListVo = new ArrayList<SHCIEndCaseAddReqHospitalInfoDataVo>();
				for(PrpLDlossPersHospitalVo hospitalVo:persInjuredVo.getPrpLDlossPersHospitals()){
					SHCIEndCaseAddReqHospitalInfoDataVo hospitalInfoDataVo = new SHCIEndCaseAddReqHospitalInfoDataVo();
					hospitalInfoDataVo.setHospitalName(hospitalVo.getHospitalName());
					hospitalInfoDataVo.setHospitalFactoryCertiCode(hospitalVo.getHospitalCode());  //治疗机构组织机构代码
					hospitalInfoDataListVo.add(hospitalInfoDataVo);
				}
				personDataVo.setHospitalInfoDataVo(hospitalInfoDataListVo);

				//伤残鉴定列表（多条）（隶属于人员损失情况）
				List<SHCIVClaimInjuryIdentifyInfoDataVo> ideInfoDataList = new ArrayList<SHCIVClaimInjuryIdentifyInfoDataVo>();
				SHCIVClaimInjuryIdentifyInfoDataVo injuryIdentifyInfoDataVo = new SHCIVClaimInjuryIdentifyInfoDataVo();
				injuryIdentifyInfoDataVo.setInjuryIdentifyName(persInjuredVo.getChkComName());
				injuryIdentifyInfoDataVo.setInjuryIdentifyCertiCode(persInjuredVo.getChkComCode());
				ideInfoDataList.add(injuryIdentifyInfoDataVo);
				personDataVo.setInjuryIdentifyInfoData(ideInfoDataList);
				//
				personDataVos.add(personDataVo);
			}
		}
		
		// 车辆损失情况（多条）[上传增量的数据]
		List<PrpLDlossCarMainVo> dlossCarMainVos = new ArrayList<PrpLDlossCarMainVo>();
		if (lossItemVoList != null && lossItemVoList.size() > 0) {
			for (PrpLLossItemVo lossItemVo : lossItemVoList) {
				PrpLDlossCarMainVo carMainVo = lossCarService.findLossCarMainById(lossItemVo.getDlossId());
				dlossCarMainVos.add(carMainVo);
			}
		}
		// 车辆损失情况（多条）
		List<SHBIEndCaseAddReqVehicleDataVo> vehicleDataListVo = new ArrayList<SHBIEndCaseAddReqVehicleDataVo>();
//		List<PrpLDlossCarMainVo> dlossCarMainVos = lossCarService.findLossCarMainByRegistNo(registNo);
		int carNum = 0;
		for(PrpLDlossCarMainVo dlossCarMainVo : dlossCarMainVos){
			//车辆（定损方式为无损失的不上传平台）定损注销不上传平台
			if(CodeConstants.CetainLossType.DEFLOSS_NULL.equals(dlossCarMainVo.getCetainLossType()) ||
					CodeConstants.VeriFlag.CANCEL.equals(dlossCarMainVo.getUnderWriteFlag())){
				continue;
			}
			//如果车辆核损金额为0，则不上传平台，要不然会影响上传损失率
			if(NullToZero(dlossCarMainVo.getSumVeriLossFee()).compareTo(BigDecimal.ZERO) == 0){
				continue;
			}
			carNum++;
			PrpLDlossCarInfoVo carInfoVo = dlossCarMainVo.getLossCarInfoVo();
			// 车辆损失情况
			SHBIEndCaseAddReqVehicleDataVo vehicleDataVo = new SHBIEndCaseAddReqVehicleDataVo();
			vehicleDataVo.setCarMark(carInfoVo.getLicenseNo());
			vehicleDataVo.setVehicleType(carInfoVo.getLicenseType()==null ? "02":carInfoVo.getLicenseType());
			vehicleDataVo.setLossAmount(dlossCarMainVo.getSumVeriLossFee().doubleValue());//
			vehicleDataVo.setMainThird("1".equals(dlossCarMainVo.getDeflossCarType()) ? "1" : "0");//
			vehicleDataVo.setRobber("03".equals(dlossCarMainVo.getCetainLossType()) ? "1" : "0");
			vehicleDataVo.setSurveyType("1".equals(checkVo.getPrpLCheckTask().getFirstAddressFlag()) ? "1" : "3");
			vehicleDataVo.setSurveyName(checkVo.getPrpLCheckTask().getChecker());
			vehicleDataVo.setSurveyStartTime(checkVo.getCreateTime());
			vehicleDataVo.setSurveyEndTime(checkVo.getChkSubmitTime());
			vehicleDataVo.setSurveyPlace(checkVo.getPrpLCheckTask().getCheckAddress());
			vehicleDataVo.setSurveyDes(checkVo.getPrpLCheckTask().getContexts());
			String codeCName = codeTranService.findCodeName("UserCodeSH",dlossCarMainVo.getHandlerCode());
			if(StringUtils.isEmpty(codeCName)){
				codeCName = codeTranService.findCodeName("UserCodeSH",dlossCarMainVo.getCreateUser());
			}
			vehicleDataVo.setEstimateName(StringUtils.isNotEmpty(codeCName) ? codeCName : "GSU0000001");
//			vehicleDataVo.setEstimateName(dlossCarMainVo.getHandlerCode());
			vehicleDataVo.setEstimateStartTime(null);// 定损开始时间
			vehicleDataVo.setEstimateEndTime(null);// 定损结束时间
			vehicleDataVo.setAssesorName(dlossCarMainVo.getUnderWriteName());
			vehicleDataVo.setAssesorStartTime(null);// 核损开始时间
			vehicleDataVo.setAssesorEndTime(null);// 核损结束时间
			vehicleDataVo.setRemnant(0.0);
			vehicleDataVo.setCharg4eFee(0.0);
			Double repairSum = 0.0;
			for(PrpLDlossCarRepairVo carRepairVo:dlossCarMainVo.getPrpLDlossCarRepairs()){
				repairSum += NullToZero(carRepairVo.getSumVeriLoss()).doubleValue();//
			}
			vehicleDataVo.setTotalWorkingHour(repairSum);
			
			String isRepairPart = "0";
			List<PrpLDlossCarRepairVo> carRepairVoList = dlossCarMainVo.getPrpLDlossCarRepairs();
			List<PrpLDlossCarCompVo> carCompVoList = dlossCarMainVo.getPrpLDlossCarComps();
			if((carRepairVoList != null && carRepairVoList.size() > 0) ||
					(carCompVoList != null && carCompVoList.size() > 0)){
				isRepairPart = "1";
			}
			vehicleDataVo.setChangeOrRepairPart(isRepairPart);
			vehicleDataVo.setJyVehicleCode(carInfoVo.getModelCode());
			vehicleDataVo.setClaimVehicleCode(carInfoVo.getModelCode());
			vehicleDataVo.setClaimVehicleName(carInfoVo.getModelName());
			String madeFactory = carInfoVo.getFactoryName();
			vehicleDataVo.setMadeFactory(StringUtils.isEmpty(madeFactory) ? "无" : madeFactory);
			vehicleDataVo.setVehicleBrandCode(carInfoVo.getBrandCode());
			vehicleDataVo.setVehicleCatenaCode(carInfoVo.getSeriCode());
			vehicleDataVo.setVehicleGroupCode(carInfoVo.getGroupCode());
			SysCodeDictVo codeVo = codeTranService.findTransCodeDictVo
					("RepairFactoryType",dlossCarMainVo.getRepairFactoryType());
			vehicleDataVo.setPriceSltCode(codeVo != null ? codeVo.getProperty2() : "99");
			String defineFlag = "0";
			if(dlossCarMainVo.getPrpLDlossCarComps()!=null && !dlossCarMainVo.getPrpLDlossCarComps().isEmpty()){
				if("1".equals(dlossCarMainVo.getPrpLDlossCarComps().get(0).getSelfConfigFlag())){
					defineFlag = "1";
				}
			}
			vehicleDataVo.setDefineFlag(defineFlag);
			vehicleDataVo.setVin(carInfoVo.getVinNo());
			vehicleDataVo.setEngineNo(carInfoVo.getEngineNo());
			vehicleDataVo.setModel(carInfoVo.getBrandName());
			vehicleDataVo.setDriverName(carInfoVo.getDriveName());
			//String certiType = "1".equals(carInfoVo.getIdentifyType())?"01":"99";
			SysCodeDictVo sysdictVo = codeTranService.findTransCodeDictVo
	                ("IdentifyType",carInfoVo.getIdentifyType());
			vehicleDataVo.setCertiType(sysdictVo==null?"99":sysdictVo.getProperty4());
			vehicleDataVo.setCertiCode(carInfoVo.getIdentifyNo());
			vehicleDataVo.setDriverLicenseNo(carInfoVo.getDrivingLicenseNo());
			vehicleDataVo.setTemporaryFlag("0");
			
			vehicleDataVo.setCheckerCode(checkVo.getPrpLCheckTask().getCheckerCode());
			vehicleDataVo.setCheckerCertiCode(checkVo.getPrpLCheckTask().getCheckerIdfNo());
			vehicleDataVo.setUnderWriteCode(dlossCarMainVo.getUnderWriteCode());
			vehicleDataVo.setUnderWriteCertiCode(dlossCarMainVo.getUnderWiteIdNo());
			vehicleDataVo.setUnderDefLoss(NullToZero(dlossCarMainVo.getSumVeriChargeFee()).doubleValue());
			vehicleDataVo.setIsTotalLoss(dlossCarMainVo.getIsTotalLoss());
			vehicleDataVo.setIsHotSinceDetonation(dlossCarMainVo.getIsHotSinceDetonation());
			vehicleDataVo.setIsWaterFlooded(dlossCarMainVo.getIsWaterFloaded());
			vehicleDataVo.setWaterFloodedLevel("1".equals(dlossCarMainVo.getIsWaterFloaded())?dlossCarMainVo.getWaterFloodedLevel():"");
			underDefLoss +=NullToZero(dlossCarMainVo.getSumVeriChargeFee()).doubleValue();

			// 车辆损失部位
			List<SHCIEndCaseAddReqCarLossPartDataVo> carLossPartDataListVo = new ArrayList<SHCIEndCaseAddReqCarLossPartDataVo>();
			String[] lossPartArr = dlossCarMainVo.getLossPart().split(",");
			for (int i = 0; i < lossPartArr.length; i++) {
				String lossPart = lossPartArr[i];
				SHCIEndCaseAddReqCarLossPartDataVo carLossPartData = new SHCIEndCaseAddReqCarLossPartDataVo();
				SysCodeDictVo sysVo = codeTranService.findTransCodeDictVo("LossPart",lossPart);
				carLossPartData.setLossPart(sysVo==null?"01":sysVo.getProperty3());
				carLossPartDataListVo.add(carLossPartData);
			}
			vehicleDataVo.setCarLossPartDataVo(carLossPartDataListVo);

			// 车辆配件明细（多条）
			List<SHCIEndCaseAddReqFittingDataVo> fittingDataListVo = new ArrayList<SHCIEndCaseAddReqFittingDataVo>();
			if(dlossCarMainVo.getPrpLDlossCarComps()!=null&&dlossCarMainVo.getPrpLDlossCarComps().size()>0){
				for(PrpLDlossCarCompVo carCompVo:dlossCarMainVo.getPrpLDlossCarComps()){// 配件
					SHCIEndCaseAddReqFittingDataVo dataVo = new SHCIEndCaseAddReqFittingDataVo();
					String repairName = carCompVo.getCompName();
					if(StringUtils.isEmpty(repairName)){
						repairName = "空";
					}else if(repairName.length() > 150){
						repairName = repairName.substring(0,74);
					}
					dataVo.setChangePartName(repairName);
					dataVo.setChangePartNum(DataUtils.NullToZeroInt(carCompVo.getQuantity()).doubleValue());
					dataVo.setChangePartFee(NullToZero(carCompVo.getManHourUnitPrice()).doubleValue());
					dataVo.setChangePartTime(NullToZero(carCompVo.getManHour()).doubleValue());
					dataVo.setChangePartManpowerFee(NullToZero(carCompVo.getVeriManHourFee()).doubleValue());
					dataVo.setRepairPartName(null);
					dataVo.setRepairPartNum(null);
					dataVo.setRepairPartFee(null);
					dataVo.setRepairPartTime(null);
					dataVo.setRepairPartManpowerFee(null);
					dataVo.setRepairMethord(null);

					dataVo.setJyPartCode(carCompVo.getCompCode());
					dataVo.setOemPartCode(StringUtils.isEmpty(carCompVo.getOriginalId()) ? "无" : carCompVo.getOriginalId());
					dataVo.setDefineFlag("1".equals(carCompVo.getSelfConfigFlag()) ? "1" : "0");

					dataVo.setSubjionFlag("0");
					dataVo.setPriceType("02");
					dataVo.setQualityType("05");

					fittingDataListVo.add(dataVo);
				}
			}
			if(dlossCarMainVo.getPrpLDlossCarRepairs()!=null&&dlossCarMainVo.getPrpLDlossCarRepairs().size()>0){
				for(PrpLDlossCarRepairVo carRepairVo:dlossCarMainVo.getPrpLDlossCarRepairs()){// 修理
					SHCIEndCaseAddReqFittingDataVo dataVo = new SHCIEndCaseAddReqFittingDataVo();
					String repairName = carRepairVo.getRepairName();
					if(StringUtils.isEmpty(repairName)){
						repairName = "空";
					}else if(repairName.length() > 150){
						repairName = repairName.substring(0,74);
					}
					dataVo.setChangePartName(repairName);
					dataVo.setChangePartNum(NullToZero(carRepairVo.getVeriMaterQuantity()).doubleValue());
					dataVo.setChangePartFee(NullToZero(carRepairVo.getVeriMaterUnitPrice()).doubleValue());
					dataVo.setChangePartTime(NullToZero(carRepairVo.getVeriManHour()).doubleValue());
					dataVo.setChangePartManpowerFee(NullToZero(carRepairVo.getVeriManUnitPrice()).doubleValue());
					dataVo.setRepairPartName(carRepairVo.getCompName());
					dataVo.setRepairPartNum(NullToZero(carRepairVo.getVeriMaterQuantity()).intValue());
					dataVo.setRepairPartFee(NullToZero(carRepairVo.getVeriMaterUnitPrice()).doubleValue());
					dataVo.setRepairPartTime(NullToZero(carRepairVo.getVeriManHour()).doubleValue());
					dataVo.setRepairPartManpowerFee(NullToZero(carRepairVo.getVeriManHourFee()).doubleValue());

					dataVo.setRepairMethord("09");

					dataVo.setJyPartCode(carRepairVo.getCompCode());
					dataVo.setOemPartCode(StringUtils.isEmpty(carRepairVo.getRepairId()) ? "无" : carRepairVo.getRepairId());
					dataVo.setDefineFlag("1".equals(carRepairVo.getSelfConfigFlag()) ? "1" : "0");

					dataVo.setSubjionFlag("0");//默认全量上次
					dataVo.setPriceType("02");
					dataVo.setQualityType("05");

					fittingDataListVo.add(dataVo);
				}
			}
			
			vehicleDataVo.setFittingDataVo(fittingDataListVo);

			if("1".equals(isRepairPart)){// 开票修理机构（多条）
				List<SHCIEndCaseAddReqRepairDataVo> repartDataVoList = new ArrayList<SHCIEndCaseAddReqRepairDataVo>();
				SHCIEndCaseAddReqRepairDataVo repartVo = new SHCIEndCaseAddReqRepairDataVo();
				repartVo.setRepairOrg("JY00008782");
				repartDataVoList.add(repartVo);
				vehicleDataVo.setRepairDataVo(repartDataVoList);
			}
			if("1".equals(isRepairPart)){// 承修修理机构（多条）
				List<SHCIEndCaseAddReqActuralRepairDataVo> acturalRepairList = new ArrayList<SHCIEndCaseAddReqActuralRepairDataVo>();
				SHCIEndCaseAddReqActuralRepairDataVo repairDataVo = new SHCIEndCaseAddReqActuralRepairDataVo();
				repairDataVo.setActuralRepairOrg("JY00008782");
				acturalRepairList.add(repairDataVo);
				vehicleDataVo.setActuralRepairDataVo(acturalRepairList);
			}
			
//			// 开票修理机构（多条）
//			List<SHCIEndCaseAddReqRepairDataVo> repairDataListVo = new ArrayList<SHCIEndCaseAddReqRepairDataVo>();
//			for(PrpLDlossCarRepairVo repairVo:dlossCarMainVo.getPrpLDlossCarRepairs()){
//				SHCIEndCaseAddReqRepairDataVo repairDataVo = new SHCIEndCaseAddReqRepairDataVo();
////				repairDataVo.setRepairOrg(repairVo.getRepairCode());
//				repairDataVo.setRepairOrg("JY00008782");
//			}
//			vehicleDataVo.setRepairDataVo(repairDataListVo);
//
//			// 承修修理机构（多条）
//			vehicleDataVo.setActuralRepairDataVo(null);
			
			//新加 是否玻璃单独破碎   是否属于无法找到第三方
			vehicleDataVo.setIsGlassBroken(StringUtils.isNotBlank(dlossCarMainVo.getIsGlassBroken())?dlossCarMainVo.getIsGlassBroken():"0");
			vehicleDataVo.setIsNotFindThird(StringUtils.isNotBlank(dlossCarMainVo.getIsNotFindThird())?dlossCarMainVo.getIsNotFindThird():"0");
			
			// 添加
			vehicleDataListVo.add(vehicleDataVo);
		}
		
		
		// 物损损失情况（多条）
		List<SHCIEndCaseAddReqObjDataVo> objDataListVo = new ArrayList<SHCIEndCaseAddReqObjDataVo>();
		List<PrpLdlossPropMainVo> dlossPropVoList = new ArrayList<PrpLdlossPropMainVo>();
		if(lossPropVoList != null && lossPropVoList.size() > 0){
			for(PrpLLossPropVo lossPropVo : lossPropVoList){
				PrpLdlossPropMainVo prop = propTaskService.findPropMainVoById(lossPropVo.getDlossId());
				dlossPropVoList.add(prop);
			}
		}
		for(PrpLdlossPropMainVo propLossMainVo : dlossPropVoList){
			isProtectLoss ="1";
			for(PrpLdlossPropFeeVo propFee:propLossMainVo.getPrpLdlossPropFees()){
				// 物损损失情况
				SHCIEndCaseAddReqObjDataVo objDataVo = new SHCIEndCaseAddReqObjDataVo();
				objDataVo.setObjectDesc(propFee.getLossItemName());
				objDataVo.setLossNum(NullToZero(propFee.getLossQuantity()).intValue());
				objDataVo.setLossAmount(NullToZero(propFee.getVeriUnitPrice()).doubleValue());
				objDataVo.setMainThird("1".equals(propLossMainVo.getLossType()) ? "1" : "0");
				objDataVo.setSurveyType("1".equals(checkVo.getPrpLCheckTask().getFirstAddressFlag()) ? "1" : "3");
				objDataVo.setSurveyName(checkVo.getPrpLCheckTask().getChecker());
				objDataVo.setSurveyStartTime(checkVo.getCreateTime());
				objDataVo.setSurveyEndTime(checkVo.getChkSubmitTime());
				objDataVo.setSurveyPlace(checkVo.getPrpLCheckTask().getCheckAddress());
				objDataVo.setSurveyDes(checkVo.getPrpLCheckTask().getContexts());
				String codeCName = codeTranService.findCodeName("UserCodeSH",propLossMainVo.getHandlerCode());
				if(StringUtils.isEmpty(codeCName)){
					codeCName = codeTranService.findCodeName("UserCodeSH",propLossMainVo.getCreateUser());
				}
				objDataVo.setEstimateName(StringUtils.isNotEmpty(codeCName) ? codeCName : "GSU0000001");
//				objDataVo.setEstimateName(propLossMainVo.getHandlerCode());
				objDataVo.setEstimateStartTime(propLossMainVo.getCreateTime());
				objDataVo.setEstimateEndTime(propLossMainVo.getUpdateTime());
				objDataVo.setAssesorName(propLossMainVo.getUnderWriteName());
				objDataVo.setAssesorStartTime(null);
				objDataVo.setAssesorEndTime(null);
				
				String protectProperty = "1";
				if("1".equals(propLossMainVo.getLossType())){
					protectProperty = "1";  //本车财产
				}else{
					protectProperty = "2";  // 车外财产
				}
				objDataVo.setProtectProperty(protectProperty);
				objDataVo.setCheckerCode(checkVo.getPrpLCheckTask().getCheckerCode());
				objDataVo.setCheckerCertiCode(checkVo.getPrpLCheckTask().getCheckerIdfNo());
				objDataVo.setUnderWriteCode(propLossMainVo.getUnderWriteCode());
				objDataVo.setUnderWriteCertiCode(propLossMainVo.getUnderWriteIdCard());
				objDataVo.setUnderDefLoss(NullToZero(propLossMainVo.getSumVeriLoss()).doubleValue());
				underDefLoss += NullToZero(propLossMainVo.getSumVeriLoss()).doubleValue();

				objDataListVo.add(objDataVo);
			}
		}
		baseVo.setIsSingleAccident(carNum==1?"1":"0");
		baseVo.setIsPersonInjured(isPersonInjured);
		baseVo.setIsProtectLoss(isProtectLoss);
		baseVo.setUnderDefLoss(underDefLoss);
		bodyVo.setBasePartVo(baseVo);
		bodyVo.setClaimCoverListVo(claimCoverListVo);
		bodyVo.setVehicleDataVo(vehicleDataListVo);
		bodyVo.setObjDataVo(objDataListVo);
		bodyVo.setPersonDataVo(personDataVos);
		bodyVo.setDocDataVo(null);
		
		return bodyVo;
	}
	
	
	public static BigDecimal NullToZero(BigDecimal strNum) {
		return strNum == null ? new BigDecimal("0") : strNum;
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
	
	private String getRiskCodeVal(String riskCode,String kindCode){
		String val = "0101200";
		if(!Risk.DQZ.equals(riskCode)&&!"BZ".equals(kindCode)){//商业
			if(isFeeUpdate(riskCode)){//费改前
				// B01 商三险A款 B
				if("B".equals(kindCode)){
					val = "B01";// B01 商三险A款 B
				}else if("A".equals(kindCode)){
					val = "C01";// C01 车损险A款 A
				}else if("G".equals(kindCode)){
					val = "D01";// D01 盗抢险A款 G
				}else if("D11".equals(kindCode) || "D12".equals(kindCode)){
					val = "E01";// 车上人员责任险A款 D11 D12
				}else{
					val = "F01";//找不到的都是其他 F01 附加险A款 其他
				}
			}else{//商改后
				if(Risk.DBA.equals(riskCode)){//1206
					if(KINDCODE.KINDCODE_A.equals(kindCode)){
						val = "0101200";
					}else if(KINDCODE.KINDCODE_B.equals(kindCode)){
						val = "0101600";
					}else if(KINDCODE.KINDCODE_D2.equals(kindCode)){
						val = "0101800";
					}else if(KINDCODE.KINDCODE_L.equals(kindCode)){
						val = "0101210";
					}else if(KINDCODE.KINDCODE_Z.equals(kindCode)){
						val = "0101310";
					}else if(KINDCODE.KINDCODE_G.equals(kindCode)){
						val = "0101500";
					}else if(KINDCODE.KINDCODE_X.equals(kindCode)){
						val = "0101260";
					}else{
						val = "0101200";
					}
				}else if(Risk.DBE.equals(riskCode)){//1207
					if(KINDCODE.KINDCODE_A.equals(kindCode)){
						val = "0102200";
					}else if(KINDCODE.KINDCODE_B.equals(kindCode)){
						val = "0102600";
					}else if(KINDCODE.KINDCODE_D2.equals(kindCode)){
						val = "0102800";
					}else if(KINDCODE.KINDCODE_Z.equals(kindCode)){
						val = "0102310";
					}else if(KINDCODE.KINDCODE_G.equals(kindCode)){
						val = "0102500";
					}else if(KINDCODE.KINDCODE_X.equals(kindCode)){
						val = "0102260";
					}else{
						val = "0102200";
					}
				}else if(Risk.DBC.equals(riskCode)){//1208
					if(KINDCODE.KINDCODE_A.equals(kindCode)){
						val = "0103200";
					}else if(KINDCODE.KINDCODE_G.equals(kindCode)){
						val = "0103500";
					}else if(kindCode.startsWith("D")){
						val = "0103701";
					}else{
						val = "0103600";
					}
				}else if(Risk.DBT.equals(riskCode)){
					if(KINDCODE.KINDCODE_A.equals(kindCode)){
						val = "0104200";
					}else{
						val = "0104600";
					}
				}
			}
		}else{//交强
			val = "A02";
		}
		return val;
	}
}
