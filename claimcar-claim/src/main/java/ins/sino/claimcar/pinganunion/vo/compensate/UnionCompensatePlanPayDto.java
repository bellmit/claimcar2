package ins.sino.claimcar.pinganunion.vo.compensate;

import java.io.Serializable;

/**
 * 平安联盟中心-理算查询接口-查询结果-险种赔付信息
 *
 * @author mfn
 * @date 2020/7/29 16:26
 */
public class UnionCompensatePlanPayDto implements Serializable {
	private static final long serialVersionUID = 2748893908886630572L;

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
	/** 险种代码 代码定义3.2 是否非空：N 编码：Y */
	private String planCode;
	/** 险种赔付/预陪/垫付/追偿金额 是否非空：N 编码：N */
	private String planPayAmount;
	/** 拒赔/注销金额 是否非空：N 编码：N */
	private String refuseAmount;
	/** 冲减金额 是否非空：N 编码：N */
	private String writeoffAmount;
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
	/** 备注 是否非空：N 编码：N */
	private String remark;
	/** 本次赔付垫支付转回金额 是否非空：N 编码：N */
	private String advanceReturnAmount;
	/** 超限金额 是否非空：N 编码：N */
	private String consultAmount;
	/** 追偿费用支出 是否非空：N 编码：N */
	private String chaseFeeOut;
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
	/** 代位追偿金额 是否非空：N 编码：N */
	private String subrogationAmount;
	/** 币种 (01-人民币 02-港币 03-美元) 是否非空：N 编码：Y */
	private String currencyCode;
	/** 罚息收入 是否非空：N 编码：N */
	private String amercement;
	/** 追偿费用转回 是否非空：N 编码：N */
	private String chaseFeeBack;
	/** 查勘补助费 是否非空：N 编码：N */
	private String surveySubsidyFee;
	/** 公估费(外包) 是否非空：N 编码：N */
	private String evaluationOutFee;
	/** 咨询费(对公) 是否非空：N 编码：N */
	private String consultPubFee;
	/** 国外代理费 是否非空：N 编码：N */
	private String agencyFee;
	/** 公证取证费 是否非空：N 编码：N */
	private String notarialFee;
	/** 前置调查费 是否非空：N 编码：N */
	private String preInvestigateFee;
	/** 归档时间 是否非空：N 编码：N */
	private String archiveDate;

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

	public String getPlanPayAmount() {
		return planPayAmount;
	}

	public void setPlanPayAmount(String planPayAmount) {
		this.planPayAmount = planPayAmount;
	}

	public String getRefuseAmount() {
		return refuseAmount;
	}

	public void setRefuseAmount(String refuseAmount) {
		this.refuseAmount = refuseAmount;
	}

	public String getWriteoffAmount() {
		return writeoffAmount;
	}

	public void setWriteoffAmount(String writeoffAmount) {
		this.writeoffAmount = writeoffAmount;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getAdvanceReturnAmount() {
		return advanceReturnAmount;
	}

	public void setAdvanceReturnAmount(String advanceReturnAmount) {
		this.advanceReturnAmount = advanceReturnAmount;
	}

	public String getConsultAmount() {
		return consultAmount;
	}

	public void setConsultAmount(String consultAmount) {
		this.consultAmount = consultAmount;
	}

	public String getChaseFeeOut() {
		return chaseFeeOut;
	}

	public void setChaseFeeOut(String chaseFeeOut) {
		this.chaseFeeOut = chaseFeeOut;
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

	public String getSubrogationAmount() {
		return subrogationAmount;
	}

	public void setSubrogationAmount(String subrogationAmount) {
		this.subrogationAmount = subrogationAmount;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getAmercement() {
		return amercement;
	}

	public void setAmercement(String amercement) {
		this.amercement = amercement;
	}

	public String getChaseFeeBack() {
		return chaseFeeBack;
	}

	public void setChaseFeeBack(String chaseFeeBack) {
		this.chaseFeeBack = chaseFeeBack;
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

	public String getAgencyFee() {
		return agencyFee;
	}

	public void setAgencyFee(String agencyFee) {
		this.agencyFee = agencyFee;
	}

	public String getNotarialFee() {
		return notarialFee;
	}

	public void setNotarialFee(String notarialFee) {
		this.notarialFee = notarialFee;
	}

	public String getPreInvestigateFee() {
		return preInvestigateFee;
	}

	public void setPreInvestigateFee(String preInvestigateFee) {
		this.preInvestigateFee = preInvestigateFee;
	}

	public String getArchiveDate() {
		return archiveDate;
	}

	public void setArchiveDate(String archiveDate) {
		this.archiveDate = archiveDate;
	}

}
