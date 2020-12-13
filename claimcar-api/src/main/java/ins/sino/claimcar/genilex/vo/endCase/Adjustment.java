package ins.sino.claimcar.genilex.vo.endCase;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Adjustment")
public class Adjustment implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("ReportNo") 
	private String  reportNo;
	@XStreamAlias("ClaimNo") 
	private String  claimNo;
	@XStreamAlias("AdjustmentCode") 
	private String  adjustmentCode;
	@XStreamAlias("OtherFee") 
	private String  otherFee;
	@XStreamAlias("UnderWriteDesc") 
	private String  underWriteDesc;
	@XStreamAlias("TaskStatus") 
	private String  taskStatus;
	@XStreamAlias("ClaimAmount") 
	private String  claimAmount;
	@XStreamAlias("UnderWriteEndTime") 
	private String  underWriteEndTime;
	@XStreamAlias("PaymentTypeCode") 
	private String  paymentTypeCode;
	@XStreamAlias("AtfaultTypeCode") 
	private String  atfaultTypeCode;
	@XStreamAlias("AtfaultProportion") 
	private String  atfaultProportion;
	@XStreamAlias("ReceivedPremiumProportion") 
	private String  receivedPremiumProportion;
	@XStreamAlias("CurrencyCode") 
	private String  currencyCode;
	@XStreamAlias("DepositedAmt") 
	private String  depositedAmt;
	@XStreamAlias("ReplevyedAmt") 
	private String  replevyedAmt;
	@XStreamAlias("OthersPaymentAmt") 
	private String  othersPaymentAmt;
	@XStreamAlias("TotalFeePaymentAmt") 
	private String  totalFeePaymentAmt;
	@XStreamAlias("TotalFeePrepayAmt") 
	private String  totalFeePrepayAmt;
	@XStreamAlias("TotalLossPaymentAmt") 
	private String  totalLossPaymentAmt;
	@XStreamAlias("AccumullossPrepaymentAmt") 
	private String  accumullossPrepaymentAmt;
	@XStreamAlias("TotalPaymentAmt") 
	private String  totalPaymentAmt;
	@XStreamAlias("CurrentPaymentAmt") 
	private String  currentPaymentAmt;
	@XStreamAlias("RefuseDesc") 
	private String  refuseDesc;
	@XStreamAlias("IndemnityPaymentType") 
	private String  indemnityPaymentType;
	@XStreamAlias("IndemnityPaymentExpProc") 
	private String  indemnityPaymentExpProc;
	@XStreamAlias("IsSubrogation") 
	private String  isSubrogation;
	@XStreamAlias("IsLawsuit") 
	private String  isLawsuit;
	@XStreamAlias("SuperadditionType") 
	private String  superadditionType;
	@XStreamAlias("SuperadditionReason") 
	private String  superadditionReason;
	@XStreamAlias("SubrogationType") 
	private String  subrogationType;
	@XStreamAlias("TotalFeeSuperaddition") 
	private String  totalFeeSuperaddition;
	@XStreamAlias("TotalPerSuperaddition") 
	private String  totalPerSuperaddition;
	@XStreamAlias("TotalLossSuperaddition") 
	private String  totalLossSuperaddition;
	@XStreamAlias("EndcaseTimes") 
	private String  endcaseTimes;
	@XStreamAlias("PaycountStartTime") 
	private String  paycountStartTime;
	@XStreamAlias("EndcaseTime") 
	private String  endcaseTime;
	@XStreamAlias("CurrentFinalAmount") 
	private String  currentFinalAmount;
	@XStreamAlias("AccommodateAmt") 
	private String  accommodateAmt;
	@XStreamAlias("SignsOfFraud") 
	private String  signsOfFraud;
	@XStreamAlias("Remark") 
	private String  remark;
	@XStreamAlias("CreateBy") 
	private String  createBy;
	@XStreamAlias("CreateTime") 
	private String  createTime;
	@XStreamAlias("UpdateBy") 
	private String  updateBy;
	@XStreamAlias("UpdateTime") 
	private String  updateTime;
	@XStreamAlias("CoverItems") 
	private List<CoverItem> coverItems;
	
	public String getReportNo() {
		return reportNo;
	}
	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}
	public String getClaimNo() {
		return claimNo;
	}
	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}
	public String getAdjustmentCode() {
		return adjustmentCode;
	}
	public void setAdjustmentCode(String adjustmentCode) {
		this.adjustmentCode = adjustmentCode;
	}
	public String getOtherFee() {
		return otherFee;
	}
	public void setOtherFee(String otherFee) {
		this.otherFee = otherFee;
	}
	public String getUnderWriteDesc() {
		return underWriteDesc;
	}
	public void setUnderWriteDesc(String underWriteDesc) {
		this.underWriteDesc = underWriteDesc;
	}
	public String getTaskStatus() {
		return taskStatus;
	}
	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}
	public String getClaimAmount() {
		return claimAmount;
	}
	public void setClaimAmount(String claimAmount) {
		this.claimAmount = claimAmount;
	}
	public String getUnderWriteEndTime() {
		return underWriteEndTime;
	}
	public void setUnderWriteEndTime(String underWriteEndTime) {
		this.underWriteEndTime = underWriteEndTime;
	}
	public String getPaymentTypeCode() {
		return paymentTypeCode;
	}
	public void setPaymentTypeCode(String paymentTypeCode) {
		this.paymentTypeCode = paymentTypeCode;
	}
	public String getAtfaultTypeCode() {
		return atfaultTypeCode;
	}
	public void setAtfaultTypeCode(String atfaultTypeCode) {
		this.atfaultTypeCode = atfaultTypeCode;
	}
	public String getAtfaultProportion() {
		return atfaultProportion;
	}
	public void setAtfaultProportion(String atfaultProportion) {
		this.atfaultProportion = atfaultProportion;
	}
	public String getReceivedPremiumProportion() {
		return receivedPremiumProportion;
	}
	public void setReceivedPremiumProportion(String receivedPremiumProportion) {
		this.receivedPremiumProportion = receivedPremiumProportion;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public String getDepositedAmt() {
		return depositedAmt;
	}
	public void setDepositedAmt(String depositedAmt) {
		this.depositedAmt = depositedAmt;
	}
	public String getReplevyedAmt() {
		return replevyedAmt;
	}
	public void setReplevyedAmt(String replevyedAmt) {
		this.replevyedAmt = replevyedAmt;
	}
	public String getOthersPaymentAmt() {
		return othersPaymentAmt;
	}
	public void setOthersPaymentAmt(String othersPaymentAmt) {
		this.othersPaymentAmt = othersPaymentAmt;
	}
	public String getTotalFeePaymentAmt() {
		return totalFeePaymentAmt;
	}
	public void setTotalFeePaymentAmt(String totalFeePaymentAmt) {
		this.totalFeePaymentAmt = totalFeePaymentAmt;
	}
	public String getTotalFeePrepayAmt() {
		return totalFeePrepayAmt;
	}
	public void setTotalFeePrepayAmt(String totalFeePrepayAmt) {
		this.totalFeePrepayAmt = totalFeePrepayAmt;
	}
	public String getTotalLossPaymentAmt() {
		return totalLossPaymentAmt;
	}
	public void setTotalLossPaymentAmt(String totalLossPaymentAmt) {
		this.totalLossPaymentAmt = totalLossPaymentAmt;
	}
	public String getAccumullossPrepaymentAmt() {
		return accumullossPrepaymentAmt;
	}
	public void setAccumullossPrepaymentAmt(String accumullossPrepaymentAmt) {
		this.accumullossPrepaymentAmt = accumullossPrepaymentAmt;
	}
	public String getTotalPaymentAmt() {
		return totalPaymentAmt;
	}
	public void setTotalPaymentAmt(String totalPaymentAmt) {
		this.totalPaymentAmt = totalPaymentAmt;
	}
	public String getCurrentPaymentAmt() {
		return currentPaymentAmt;
	}
	public void setCurrentPaymentAmt(String currentPaymentAmt) {
		this.currentPaymentAmt = currentPaymentAmt;
	}
	public String getRefuseDesc() {
		return refuseDesc;
	}
	public void setRefuseDesc(String refuseDesc) {
		this.refuseDesc = refuseDesc;
	}
	public String getIndemnityPaymentType() {
		return indemnityPaymentType;
	}
	public void setIndemnityPaymentType(String indemnityPaymentType) {
		this.indemnityPaymentType = indemnityPaymentType;
	}
	public String getIndemnityPaymentExpProc() {
		return indemnityPaymentExpProc;
	}
	public void setIndemnityPaymentExpProc(String indemnityPaymentExpProc) {
		this.indemnityPaymentExpProc = indemnityPaymentExpProc;
	}
	public String getIsSubrogation() {
		return isSubrogation;
	}
	public void setIsSubrogation(String isSubrogation) {
		this.isSubrogation = isSubrogation;
	}
	public String getIsLawsuit() {
		return isLawsuit;
	}
	public void setIsLawsuit(String isLawsuit) {
		this.isLawsuit = isLawsuit;
	}
	public String getSuperadditionType() {
		return superadditionType;
	}
	public void setSuperadditionType(String superadditionType) {
		this.superadditionType = superadditionType;
	}
	public String getSuperadditionReason() {
		return superadditionReason;
	}
	public void setSuperadditionReason(String superadditionReason) {
		this.superadditionReason = superadditionReason;
	}
	public String getSubrogationType() {
		return subrogationType;
	}
	public void setSubrogationType(String subrogationType) {
		this.subrogationType = subrogationType;
	}
	public String getTotalFeeSuperaddition() {
		return totalFeeSuperaddition;
	}
	public void setTotalFeeSuperaddition(String totalFeeSuperaddition) {
		this.totalFeeSuperaddition = totalFeeSuperaddition;
	}
	public String getTotalPerSuperaddition() {
		return totalPerSuperaddition;
	}
	public void setTotalPerSuperaddition(String totalPerSuperaddition) {
		this.totalPerSuperaddition = totalPerSuperaddition;
	}
	public String getTotalLossSuperaddition() {
		return totalLossSuperaddition;
	}
	public void setTotalLossSuperaddition(String totalLossSuperaddition) {
		this.totalLossSuperaddition = totalLossSuperaddition;
	}
	public String getEndcaseTimes() {
		return endcaseTimes;
	}
	public void setEndcaseTimes(String endcaseTimes) {
		this.endcaseTimes = endcaseTimes;
	}
	public String getPaycountStartTime() {
		return paycountStartTime;
	}
	public void setPaycountStartTime(String paycountStartTime) {
		this.paycountStartTime = paycountStartTime;
	}
	public String getEndcaseTime() {
		return endcaseTime;
	}
	public void setEndcaseTime(String endcaseTime) {
		this.endcaseTime = endcaseTime;
	}
	public String getCurrentFinalAmount() {
		return currentFinalAmount;
	}
	public void setCurrentFinalAmount(String currentFinalAmount) {
		this.currentFinalAmount = currentFinalAmount;
	}
	public String getAccommodateAmt() {
		return accommodateAmt;
	}
	public void setAccommodateAmt(String accommodateAmt) {
		this.accommodateAmt = accommodateAmt;
	}
	public String getSignsOfFraud() {
		return signsOfFraud;
	}
	public void setSignsOfFraud(String signsOfFraud) {
		this.signsOfFraud = signsOfFraud;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public List<CoverItem> getCoverItems() {
		return coverItems;
	}
	public void setCoverItems(List<CoverItem> coverItems) {
		this.coverItems = coverItems;
	}
	
	
	
}
