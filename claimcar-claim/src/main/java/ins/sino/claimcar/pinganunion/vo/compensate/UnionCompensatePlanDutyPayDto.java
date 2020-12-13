package ins.sino.claimcar.pinganunion.vo.compensate;

import java.io.Serializable;

/**
 * 平安联盟中心-理算查询接口-查询结果-责任赔付信息
 *
 * @author mfn
 * @date 2020/7/29 16:37
 */
public class UnionCompensatePlanDutyPayDto implements Serializable {

	private static final long serialVersionUID = 5744493294890964351L;

	/** 赔案号 是否非空：Y 编码：N */
	private String caseNo;
	/** 赔付次数 是否非空：Y 编码：N */
	private String caseTimes;
	/** 赔付类型 代码定义3.14 是否非空：Y 编码：Y */
	private String claimType;
	/** 预陪/垫付/追偿 次数 是否非空：N 编码：N */
	private String subTimes;
	/** 赔付批次号 是否非空：N 编码：N */
	private String idClmBatch;
	/** 险种代码 代码定义3.2 是否非空：Y 编码：Y */
	private String planCode;
	/** 险别代码 代码定义3.3 是否非空：Y 编码：Y */
	private String dutyCode;
	/** 赔偿限额 是否非空：N 编码：N */
	private String dutyPayLimit;
	/** 损失金额 是否非空：N 编码：N */
	private String lossAmount;
	/** 本次理算该险别下分摊的施救费金额 是否非空：N 编码：N */
	private String rescueFee;
	/** 单个赔偿限额 是否非空：N 编码：N */
	private String perIndemnityLimit;
	/** 保单累计赔付金额 是否非空：N 编码：N */
	private String historyPayAmount;
	/** 应负赔偿金额 是否非空：N 编码：N */
	private String selfAffordLossAmount;
	/** 调整金额 是否非空：N 编码：N */
	private String adjustAmount;
	/** 超限金额 是否非空：N 编码：N */
	private String consultAmount;
	/** 冲减金额 是否非空：N 编码：N */
	private String writeoffAmount;
	/** 责任系数 是否非空：N 编码：N */
	private String dutyRate;
	/** 责任免赔率 是否非空：N 编码：N */
	private String nopayRate;
	/** 绝对免赔率 是否非空：N 编码：N */
	private String absnopayRate;
	/** 绝对免赔额 是否非空：N 编码：N */
	private String absnopayAmount;
	/** 加扣免赔率 是否非空：N 编码：N */
	private String deductNopayRate;
	/** 责任赔付/预陪/垫付 金额 是否非空：N 编码：N */
	private String dutyPayAmount;
	/** 拒赔/注销金额 是否非空：N 编码：N */
	private String refuseAmount;
	/** 仲裁费 是否非空：N 编码：N */
	private String arbitrateFee;
	/** 诉讼费 是否非空：N 编码：N */
	private String lawsuitFee;
	/** 律师费 是否非空：N 编码：N */
	private String lawyerFee;
	/** 检验费 是否非空：N 编码：N */
	private String checkFee;
	/** 执行费 是否非空：N 编码：N */
	private String executeFee;
	/** 公估费 是否非空：N 编码：N */
	private String evaluationFee;
	/** 数量 是否非空：N 编码：N */
	private String quantity;
	/** 查勘费 是否非空：N 编码：N */
	private String surveyFee;
	/** 专家鉴定费 是否非空：N 编码：N */
	private String mavinAppraisalFee;
	/** 检验费 是否非空：N 编码：N */
	private String inspectFee;
	/** 调查取证费用 是否非空：N 编码：N */
	private String inquiryEvidenceFee;
	/** 其他 是否非空：N 编码：N */
	private String otherFee;
	/** 咨询费 是否非空：N 编码：N */
	private String consultFee;
	/** 租车费 是否非空：N 编码：N */
	private String carRentalFee;
	/** 检测费 是否非空：N 编码：N */
	private String detectFee;
	/** 差旅费 是否非空：N 编码：N */
	private String travelFee;
	/** 罚息收入 是否非空：N 编码：N */
	private String amercement;
	/** 查勘补助费 是否非空：N 编码：N */
	private String surveySubsidyFee;
	/** 公估费(外包) 是否非空：N 编码：N */
	private String evaluationOutFee;
	/** 咨询费(对公) 是否非空：N 编码：N */
	private String consultPubFee;
	/** 公证取证费 是否非空：N 编码：N */
	private String notarialFee;
	/** 理算依据 是否非空：N 编码：N */
	private String settleReason;
	/** 前置调查费 是否非空：N 编码：N */
	private String preInvestigateFee;
	/** 互碰自赔金额 是否非空：N 编码：N */
	private String collideSelfPayAmount;
	/** 无责代赔金额 是否非空：N 编码：N */
	private String advanceNodutyPayAmount;
	/** 主挂车赔偿限额 是否非空：N 编码：N */
	private String mainTrailerTayLimit;
	/** 本车交强险赔偿金额 是否非空：N 编码：N */
	private String selfForceFayAmount;
	/** 车损险其他交强险赔偿金额 是否非空：N 编码：N */
	private String vhlOtherForcePayAmount;
	/** 商业三者险其他交强险赔偿金额 是否非空：N 编码：N */
	private String thiOtherForcePayAmount;
	/** 已从第三方获得的赔偿金额 是否非空：N 编码：N */
	private String thirdPartyPayAmount;
	/** 本次理算三者车车损金额（非赔付金额） 是否非空：N 编码：N */
	private String thiCarLossAmount;
	/** 本次理算三者物物损金额（非赔付金额） 是否非空：N 编码：N */
	private String thiPropertyLossAmount;
	/** 本次理算三者人人伤金额（非赔付金额） 是否非空：N 编码：N */
	private String thiInjuredLossAmount;
	/** 行李损失金额 是否非空：N 编码：N */
	private String baggageLossAmount;
	/** 维修天数（赔付天数） 是否非空：N 编码：N */
	private String indemnityDays;
	/** 实际载客数 是否非空：N 编码：N */
	private String carryPassengers;
	/** 标的车定损金额 是否非空：N 编码：N */
	private String targetCarLossAmount;
	/** 本次人伤涉诉金额(不参与理算计算) 是否非空：N 编码：N */
	private String injuryLawPayAmount;
	/** 车险代位总赔付 是否非空：N 编码：N */
	private String carSubrogationPayAmount;
	/** 自负额 是否非空：N 编码：N */
	private String selfPayAmount;
	/** 代位总追偿额(=车险代位总赔付-自负额) 是否非空：N 编码：N */
	private String subrogationPayAmount;
	/** 通融金额 是否非空：N 编码：N */
	private String accommodationAmount;
	/** 合议金额 是否非空：N 编码：N */
	private String collegiateAmount;
	/** 新增设备赔款限额（全面型车损险保单） 是否非空：N 编码：N */
	private String newDevicePayLimit;
	/** 新增设备损失金额（全面型车损险） 是否非空：N 编码：N */
	private String newDeviceLossAmount;
	/** 新增设备赔付金额（全面型车损险） 是否非空：N 编码：N */
	private String newDevicePayAmount;
	/** 责任下车损赔付金额 是否非空：N 编码：N */
	private String thiCarPayAmount;
	/** 责任下物损赔付金额 是否非空：N 编码：N */
	private String thiPropertyPayAmount;
	/** 责任下人伤赔付金额 是否非空：N 编码：N */
	private String thiInjuredPayAmount;

	public String getCaseNo() {
		return caseNo;
	}

	public void setCaseNo(String caseNo) {
		this.caseNo = caseNo;
	}

	public String getCaseTimes() {
		return caseTimes;
	}

	public void setCaseTimes(String caseTimes) {
		this.caseTimes = caseTimes;
	}

	public String getClaimType() {
		return claimType;
	}

	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}

	public String getSubTimes() {
		return subTimes;
	}

	public void setSubTimes(String subTimes) {
		this.subTimes = subTimes;
	}

	public String getIdClmBatch() {
		return idClmBatch;
	}

	public void setIdClmBatch(String idClmBatch) {
		this.idClmBatch = idClmBatch;
	}

	public String getPlanCode() {
		return planCode;
	}

	public void setPlanCode(String planCode) {
		this.planCode = planCode;
	}

	public String getDutyCode() {
		return dutyCode;
	}

	public void setDutyCode(String dutyCode) {
		this.dutyCode = dutyCode;
	}

	public String getDutyPayLimit() {
		return dutyPayLimit;
	}

	public void setDutyPayLimit(String dutyPayLimit) {
		this.dutyPayLimit = dutyPayLimit;
	}

	public String getLossAmount() {
		return lossAmount;
	}

	public void setLossAmount(String lossAmount) {
		this.lossAmount = lossAmount;
	}

	public String getRescueFee() {
		return rescueFee;
	}

	public void setRescueFee(String rescueFee) {
		this.rescueFee = rescueFee;
	}

	public String getPerIndemnityLimit() {
		return perIndemnityLimit;
	}

	public void setPerIndemnityLimit(String perIndemnityLimit) {
		this.perIndemnityLimit = perIndemnityLimit;
	}

	public String getHistoryPayAmount() {
		return historyPayAmount;
	}

	public void setHistoryPayAmount(String historyPayAmount) {
		this.historyPayAmount = historyPayAmount;
	}

	public String getSelfAffordLossAmount() {
		return selfAffordLossAmount;
	}

	public void setSelfAffordLossAmount(String selfAffordLossAmount) {
		this.selfAffordLossAmount = selfAffordLossAmount;
	}

	public String getAdjustAmount() {
		return adjustAmount;
	}

	public void setAdjustAmount(String adjustAmount) {
		this.adjustAmount = adjustAmount;
	}

	public String getConsultAmount() {
		return consultAmount;
	}

	public void setConsultAmount(String consultAmount) {
		this.consultAmount = consultAmount;
	}

	public String getWriteoffAmount() {
		return writeoffAmount;
	}

	public void setWriteoffAmount(String writeoffAmount) {
		this.writeoffAmount = writeoffAmount;
	}

	public String getDutyRate() {
		return dutyRate;
	}

	public void setDutyRate(String dutyRate) {
		this.dutyRate = dutyRate;
	}

	public String getNopayRate() {
		return nopayRate;
	}

	public void setNopayRate(String nopayRate) {
		this.nopayRate = nopayRate;
	}

	public String getAbsnopayRate() {
		return absnopayRate;
	}

	public void setAbsnopayRate(String absnopayRate) {
		this.absnopayRate = absnopayRate;
	}

	public String getAbsnopayAmount() {
		return absnopayAmount;
	}

	public void setAbsnopayAmount(String absnopayAmount) {
		this.absnopayAmount = absnopayAmount;
	}

	public String getDeductNopayRate() {
		return deductNopayRate;
	}

	public void setDeductNopayRate(String deductNopayRate) {
		this.deductNopayRate = deductNopayRate;
	}

	public String getDutyPayAmount() {
		return dutyPayAmount;
	}

	public void setDutyPayAmount(String dutyPayAmount) {
		this.dutyPayAmount = dutyPayAmount;
	}

	public String getRefuseAmount() {
		return refuseAmount;
	}

	public void setRefuseAmount(String refuseAmount) {
		this.refuseAmount = refuseAmount;
	}

	public String getArbitrateFee() {
		return arbitrateFee;
	}

	public void setArbitrateFee(String arbitrateFee) {
		this.arbitrateFee = arbitrateFee;
	}

	public String getLawsuitFee() {
		return lawsuitFee;
	}

	public void setLawsuitFee(String lawsuitFee) {
		this.lawsuitFee = lawsuitFee;
	}

	public String getLawyerFee() {
		return lawyerFee;
	}

	public void setLawyerFee(String lawyerFee) {
		this.lawyerFee = lawyerFee;
	}

	public String getCheckFee() {
		return checkFee;
	}

	public void setCheckFee(String checkFee) {
		this.checkFee = checkFee;
	}

	public String getExecuteFee() {
		return executeFee;
	}

	public void setExecuteFee(String executeFee) {
		this.executeFee = executeFee;
	}

	public String getEvaluationFee() {
		return evaluationFee;
	}

	public void setEvaluationFee(String evaluationFee) {
		this.evaluationFee = evaluationFee;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getSurveyFee() {
		return surveyFee;
	}

	public void setSurveyFee(String surveyFee) {
		this.surveyFee = surveyFee;
	}

	public String getMavinAppraisalFee() {
		return mavinAppraisalFee;
	}

	public void setMavinAppraisalFee(String mavinAppraisalFee) {
		this.mavinAppraisalFee = mavinAppraisalFee;
	}

	public String getInspectFee() {
		return inspectFee;
	}

	public void setInspectFee(String inspectFee) {
		this.inspectFee = inspectFee;
	}

	public String getInquiryEvidenceFee() {
		return inquiryEvidenceFee;
	}

	public void setInquiryEvidenceFee(String inquiryEvidenceFee) {
		this.inquiryEvidenceFee = inquiryEvidenceFee;
	}

	public String getOtherFee() {
		return otherFee;
	}

	public void setOtherFee(String otherFee) {
		this.otherFee = otherFee;
	}

	public String getConsultFee() {
		return consultFee;
	}

	public void setConsultFee(String consultFee) {
		this.consultFee = consultFee;
	}

	public String getCarRentalFee() {
		return carRentalFee;
	}

	public void setCarRentalFee(String carRentalFee) {
		this.carRentalFee = carRentalFee;
	}

	public String getDetectFee() {
		return detectFee;
	}

	public void setDetectFee(String detectFee) {
		this.detectFee = detectFee;
	}

	public String getTravelFee() {
		return travelFee;
	}

	public void setTravelFee(String travelFee) {
		this.travelFee = travelFee;
	}

	public String getAmercement() {
		return amercement;
	}

	public void setAmercement(String amercement) {
		this.amercement = amercement;
	}

	public String getSurveySubsidyFee() {
		return surveySubsidyFee;
	}

	public void setSurveySubsidyFee(String surveySubsidyFee) {
		this.surveySubsidyFee = surveySubsidyFee;
	}

	public String getEvaluationOutFee() {
		return evaluationOutFee;
	}

	public void setEvaluationOutFee(String evaluationOutFee) {
		this.evaluationOutFee = evaluationOutFee;
	}

	public String getConsultPubFee() {
		return consultPubFee;
	}

	public void setConsultPubFee(String consultPubFee) {
		this.consultPubFee = consultPubFee;
	}

	public String getNotarialFee() {
		return notarialFee;
	}

	public void setNotarialFee(String notarialFee) {
		this.notarialFee = notarialFee;
	}

	public String getSettleReason() {
		return settleReason;
	}

	public void setSettleReason(String settleReason) {
		this.settleReason = settleReason;
	}

	public String getPreInvestigateFee() {
		return preInvestigateFee;
	}

	public void setPreInvestigateFee(String preInvestigateFee) {
		this.preInvestigateFee = preInvestigateFee;
	}

	public String getCollideSelfPayAmount() {
		return collideSelfPayAmount;
	}

	public void setCollideSelfPayAmount(String collideSelfPayAmount) {
		this.collideSelfPayAmount = collideSelfPayAmount;
	}

	public String getAdvanceNodutyPayAmount() {
		return advanceNodutyPayAmount;
	}

	public void setAdvanceNodutyPayAmount(String advanceNodutyPayAmount) {
		this.advanceNodutyPayAmount = advanceNodutyPayAmount;
	}

	public String getMainTrailerTayLimit() {
		return mainTrailerTayLimit;
	}

	public void setMainTrailerTayLimit(String mainTrailerTayLimit) {
		this.mainTrailerTayLimit = mainTrailerTayLimit;
	}

	public String getSelfForceFayAmount() {
		return selfForceFayAmount;
	}

	public void setSelfForceFayAmount(String selfForceFayAmount) {
		this.selfForceFayAmount = selfForceFayAmount;
	}

	public String getVhlOtherForcePayAmount() {
		return vhlOtherForcePayAmount;
	}

	public void setVhlOtherForcePayAmount(String vhlOtherForcePayAmount) {
		this.vhlOtherForcePayAmount = vhlOtherForcePayAmount;
	}

	public String getThiOtherForcePayAmount() {
		return thiOtherForcePayAmount;
	}

	public void setThiOtherForcePayAmount(String thiOtherForcePayAmount) {
		this.thiOtherForcePayAmount = thiOtherForcePayAmount;
	}

	public String getThirdPartyPayAmount() {
		return thirdPartyPayAmount;
	}

	public void setThirdPartyPayAmount(String thirdPartyPayAmount) {
		this.thirdPartyPayAmount = thirdPartyPayAmount;
	}

	public String getThiCarLossAmount() {
		return thiCarLossAmount;
	}

	public void setThiCarLossAmount(String thiCarLossAmount) {
		this.thiCarLossAmount = thiCarLossAmount;
	}

	public String getThiPropertyLossAmount() {
		return thiPropertyLossAmount;
	}

	public void setThiPropertyLossAmount(String thiPropertyLossAmount) {
		this.thiPropertyLossAmount = thiPropertyLossAmount;
	}

	public String getThiInjuredLossAmount() {
		return thiInjuredLossAmount;
	}

	public void setThiInjuredLossAmount(String thiInjuredLossAmount) {
		this.thiInjuredLossAmount = thiInjuredLossAmount;
	}

	public String getBaggageLossAmount() {
		return baggageLossAmount;
	}

	public void setBaggageLossAmount(String baggageLossAmount) {
		this.baggageLossAmount = baggageLossAmount;
	}

	public String getIndemnityDays() {
		return indemnityDays;
	}

	public void setIndemnityDays(String indemnityDays) {
		this.indemnityDays = indemnityDays;
	}

	public String getCarryPassengers() {
		return carryPassengers;
	}

	public void setCarryPassengers(String carryPassengers) {
		this.carryPassengers = carryPassengers;
	}

	public String getTargetCarLossAmount() {
		return targetCarLossAmount;
	}

	public void setTargetCarLossAmount(String targetCarLossAmount) {
		this.targetCarLossAmount = targetCarLossAmount;
	}

	public String getInjuryLawPayAmount() {
		return injuryLawPayAmount;
	}

	public void setInjuryLawPayAmount(String injuryLawPayAmount) {
		this.injuryLawPayAmount = injuryLawPayAmount;
	}

	public String getCarSubrogationPayAmount() {
		return carSubrogationPayAmount;
	}

	public void setCarSubrogationPayAmount(String carSubrogationPayAmount) {
		this.carSubrogationPayAmount = carSubrogationPayAmount;
	}

	public String getSelfPayAmount() {
		return selfPayAmount;
	}

	public void setSelfPayAmount(String selfPayAmount) {
		this.selfPayAmount = selfPayAmount;
	}

	public String getSubrogationPayAmount() {
		return subrogationPayAmount;
	}

	public void setSubrogationPayAmount(String subrogationPayAmount) {
		this.subrogationPayAmount = subrogationPayAmount;
	}

	public String getAccommodationAmount() {
		return accommodationAmount;
	}

	public void setAccommodationAmount(String accommodationAmount) {
		this.accommodationAmount = accommodationAmount;
	}

	public String getCollegiateAmount() {
		return collegiateAmount;
	}

	public void setCollegiateAmount(String collegiateAmount) {
		this.collegiateAmount = collegiateAmount;
	}

	public String getNewDevicePayLimit() {
		return newDevicePayLimit;
	}

	public void setNewDevicePayLimit(String newDevicePayLimit) {
		this.newDevicePayLimit = newDevicePayLimit;
	}

	public String getNewDeviceLossAmount() {
		return newDeviceLossAmount;
	}

	public void setNewDeviceLossAmount(String newDeviceLossAmount) {
		this.newDeviceLossAmount = newDeviceLossAmount;
	}

	public String getNewDevicePayAmount() {
		return newDevicePayAmount;
	}

	public void setNewDevicePayAmount(String newDevicePayAmount) {
		this.newDevicePayAmount = newDevicePayAmount;
	}

	public String getThiCarPayAmount() {
		return thiCarPayAmount;
	}

	public void setThiCarPayAmount(String thiCarPayAmount) {
		this.thiCarPayAmount = thiCarPayAmount;
	}

	public String getThiPropertyPayAmount() {
		return thiPropertyPayAmount;
	}

	public void setThiPropertyPayAmount(String thiPropertyPayAmount) {
		this.thiPropertyPayAmount = thiPropertyPayAmount;
	}

	public String getThiInjuredPayAmount() {
		return thiInjuredPayAmount;
	}

	public void setThiInjuredPayAmount(String thiInjuredPayAmount) {
		this.thiInjuredPayAmount = thiInjuredPayAmount;
	}

}
