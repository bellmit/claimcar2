package ins.sino.claimcar.genilex.vo.common;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("ClaimMain")
public class ClaimMain implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("ReportNo") 
	private String  reportNo;
	@XStreamAlias("ClaimNo") 
	private String  claimNo;
	@XStreamAlias("ClaimSequenceNo") 
	private String  claimSequenceNo;
	@XStreamAlias("ClaimTimes") 
	private String  claimTimes;
	@XStreamAlias("ClaimStatus") 
	private String  claimStatus;
	@XStreamAlias("DepartmentCode") 
	private String  departmentCode;
	@XStreamAlias("PolicyNo") 
	private String  policyNo;
	@XStreamAlias("EdrPrjNo") 
	private String  edrPrjNo;
	@XStreamAlias("PlanCode") 
	private String  planCode;
	@XStreamAlias("LossTime") 
	private String  lossTime;
	@XStreamAlias("ReportTime") 
	private String  reportTime;
	@XStreamAlias("CatastropheLossInd") 
	private String  catastropheLossInd;
	@XStreamAlias("DepositInd") 
	private String  depositInd;
	@XStreamAlias("RegisterInd") 
	private String  registerInd;
	@XStreamAlias("DocumentCompletedInd") 
	private String  documentCompletedInd;
	@XStreamAlias("PrepayInd") 
	private String  prepayInd;
	@XStreamAlias("GettingbackInd") 
	private String  gettingbackInd;
	@XStreamAlias("LitigationInd") 
	private String  litigationInd;
	@XStreamAlias("PaymentInd") 
	private String  paymentInd;
	@XStreamAlias("XEpaFlag") 
	private String  xEpaFlag;
	@XStreamAlias("ReportAmt") 
	private String  reportAmt;
	@XStreamAlias("ReportCurrencyCode") 
	private String  reportCurrencyCode;
	@XStreamAlias("TotalDepositAmt") 
	private String  totalDepositAmt;
	@XStreamAlias("DepositCurrencyCode") 
	private String  depositCurrencyCode;
	@XStreamAlias("RegisterAmt") 
	private String  registerAmt;
	@XStreamAlias("RegisterCurrencyCode") 
	private String  registerCurrencyCode;
	@XStreamAlias("TotalVerifiedAmt") 
	private String  totalVerifiedAmt;
	@XStreamAlias("VerifiedCurrencyCode") 
	private String  verifiedCurrencyCode;
	@XStreamAlias("TotalPrepayAmt") 
	private String  totalPrepayAmt;
	@XStreamAlias("PrepayCurrencyCode") 
	private String  prepayCurrencyCode;
	@XStreamAlias("TotalPaymentAmt") 
	private String  totalPaymentAmt;
	@XStreamAlias("PaymentCurrencyCode") 
	private String  paymentCurrencyCode;
	@XStreamAlias("RegisterBy") 
	private String  registerBy;
	@XStreamAlias("RegisterTime") 
	private String  registerTime;
	@XStreamAlias("EndcaseBy") 
	private String  endcaseBy;
	@XStreamAlias("EndcaseDepartmentCode") 
	private String  endcaseDepartmentCode;
	@XStreamAlias("EndcaseTime") 
	private String  endcaseTime;
	@XStreamAlias("EndcasePigeonholeNo") 
	private String  endcasePigeonholeNo;
	@XStreamAlias("EndcaseDesc") 
	private String  endcaseDesc;
	@XStreamAlias("RevokecaseInd") 
	private String  revokecaseInd;
	@XStreamAlias("RevokecaseBy") 
	private String  revokecaseBy;
	@XStreamAlias("RevokecaseTime") 
	private String  revokecaseTime;
	@XStreamAlias("RevokecaseDesc") 
	private String  revokecaseDesc;
	@XStreamAlias("HugeLossCode") 
	private String  hugeLossCode;
	@XStreamAlias("TotalVerifiedTime") 
	private String  totalVerifiedTime;
	@XStreamAlias("CollidedThirdInd") 
	private String  collidedThirdInd;
	@XStreamAlias("Actiontype") 
	private String  actiontype;
	@XStreamAlias("SubrogationFlag") 
	private String  subrogationFlag;
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
	public String getClaimSequenceNo() {
		return claimSequenceNo;
	}
	public void setClaimSequenceNo(String claimSequenceNo) {
		this.claimSequenceNo = claimSequenceNo;
	}
	public String getClaimTimes() {
		return claimTimes;
	}
	public void setClaimTimes(String claimTimes) {
		this.claimTimes = claimTimes;
	}
	public String getClaimStatus() {
		return claimStatus;
	}
	public void setClaimStatus(String claimStatus) {
		this.claimStatus = claimStatus;
	}
	public String getDepartmentCode() {
		return departmentCode;
	}
	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}
	public String getPolicyNo() {
		return policyNo;
	}
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}
	public String getEdrPrjNo() {
		return edrPrjNo;
	}
	public void setEdrPrjNo(String edrPrjNo) {
		this.edrPrjNo = edrPrjNo;
	}
	public String getPlanCode() {
		return planCode;
	}
	public void setPlanCode(String planCode) {
		this.planCode = planCode;
	}
	public String getLossTime() {
		return lossTime;
	}
	public void setLossTime(String lossTime) {
		this.lossTime = lossTime;
	}
	public String getReportTime() {
		return reportTime;
	}
	public void setReportTime(String reportTime) {
		this.reportTime = reportTime;
	}
	public String getCatastropheLossInd() {
		return catastropheLossInd;
	}
	public void setCatastropheLossInd(String catastropheLossInd) {
		this.catastropheLossInd = catastropheLossInd;
	}
	public String getDepositInd() {
		return depositInd;
	}
	public void setDepositInd(String depositInd) {
		this.depositInd = depositInd;
	}
	public String getRegisterInd() {
		return registerInd;
	}
	public void setRegisterInd(String registerInd) {
		this.registerInd = registerInd;
	}
	public String getDocumentCompletedInd() {
		return documentCompletedInd;
	}
	public void setDocumentCompletedInd(String documentCompletedInd) {
		this.documentCompletedInd = documentCompletedInd;
	}
	public String getPrepayInd() {
		return prepayInd;
	}
	public void setPrepayInd(String prepayInd) {
		this.prepayInd = prepayInd;
	}
	public String getGettingbackInd() {
		return gettingbackInd;
	}
	public void setGettingbackInd(String gettingbackInd) {
		this.gettingbackInd = gettingbackInd;
	}
	public String getLitigationInd() {
		return litigationInd;
	}
	public void setLitigationInd(String litigationInd) {
		this.litigationInd = litigationInd;
	}
	public String getPaymentInd() {
		return paymentInd;
	}
	public void setPaymentInd(String paymentInd) {
		this.paymentInd = paymentInd;
	}
	public String getxEpaFlag() {
		return xEpaFlag;
	}
	public void setxEpaFlag(String xEpaFlag) {
		this.xEpaFlag = xEpaFlag;
	}
	public String getReportAmt() {
		return reportAmt;
	}
	public void setReportAmt(String reportAmt) {
		this.reportAmt = reportAmt;
	}
	public String getReportCurrencyCode() {
		return reportCurrencyCode;
	}
	public void setReportCurrencyCode(String reportCurrencyCode) {
		this.reportCurrencyCode = reportCurrencyCode;
	}
	public String getTotalDepositAmt() {
		return totalDepositAmt;
	}
	public void setTotalDepositAmt(String totalDepositAmt) {
		this.totalDepositAmt = totalDepositAmt;
	}
	public String getDepositCurrencyCode() {
		return depositCurrencyCode;
	}
	public void setDepositCurrencyCode(String depositCurrencyCode) {
		this.depositCurrencyCode = depositCurrencyCode;
	}
	public String getRegisterAmt() {
		return registerAmt;
	}
	public void setRegisterAmt(String registerAmt) {
		this.registerAmt = registerAmt;
	}
	public String getRegisterCurrencyCode() {
		return registerCurrencyCode;
	}
	public void setRegisterCurrencyCode(String registerCurrencyCode) {
		this.registerCurrencyCode = registerCurrencyCode;
	}
	public String getTotalVerifiedAmt() {
		return totalVerifiedAmt;
	}
	public void setTotalVerifiedAmt(String totalVerifiedAmt) {
		this.totalVerifiedAmt = totalVerifiedAmt;
	}
	public String getVerifiedCurrencyCode() {
		return verifiedCurrencyCode;
	}
	public void setVerifiedCurrencyCode(String verifiedCurrencyCode) {
		this.verifiedCurrencyCode = verifiedCurrencyCode;
	}
	public String getTotalPrepayAmt() {
		return totalPrepayAmt;
	}
	public void setTotalPrepayAmt(String totalPrepayAmt) {
		this.totalPrepayAmt = totalPrepayAmt;
	}
	public String getPrepayCurrencyCode() {
		return prepayCurrencyCode;
	}
	public void setPrepayCurrencyCode(String prepayCurrencyCode) {
		this.prepayCurrencyCode = prepayCurrencyCode;
	}
	public String getTotalPaymentAmt() {
		return totalPaymentAmt;
	}
	public void setTotalPaymentAmt(String totalPaymentAmt) {
		this.totalPaymentAmt = totalPaymentAmt;
	}
	public String getPaymentCurrencyCode() {
		return paymentCurrencyCode;
	}
	public void setPaymentCurrencyCode(String paymentCurrencyCode) {
		this.paymentCurrencyCode = paymentCurrencyCode;
	}
	public String getRegisterBy() {
		return registerBy;
	}
	public void setRegisterBy(String registerBy) {
		this.registerBy = registerBy;
	}
	public String getRegisterTime() {
		return registerTime;
	}
	public void setRegisterTime(String registerTime) {
		this.registerTime = registerTime;
	}
	public String getEndcaseBy() {
		return endcaseBy;
	}
	public void setEndcaseBy(String endcaseBy) {
		this.endcaseBy = endcaseBy;
	}
	public String getEndcaseDepartmentCode() {
		return endcaseDepartmentCode;
	}
	public void setEndcaseDepartmentCode(String endcaseDepartmentCode) {
		this.endcaseDepartmentCode = endcaseDepartmentCode;
	}
	public String getEndcaseTime() {
		return endcaseTime;
	}
	public void setEndcaseTime(String endcaseTime) {
		this.endcaseTime = endcaseTime;
	}
	public String getEndcasePigeonholeNo() {
		return endcasePigeonholeNo;
	}
	public void setEndcasePigeonholeNo(String endcasePigeonholeNo) {
		this.endcasePigeonholeNo = endcasePigeonholeNo;
	}
	public String getEndcaseDesc() {
		return endcaseDesc;
	}
	public void setEndcaseDesc(String endcaseDesc) {
		this.endcaseDesc = endcaseDesc;
	}
	public String getRevokecaseInd() {
		return revokecaseInd;
	}
	public void setRevokecaseInd(String revokecaseInd) {
		this.revokecaseInd = revokecaseInd;
	}
	public String getRevokecaseBy() {
		return revokecaseBy;
	}
	public void setRevokecaseBy(String revokecaseBy) {
		this.revokecaseBy = revokecaseBy;
	}
	public String getRevokecaseTime() {
		return revokecaseTime;
	}
	public void setRevokecaseTime(String revokecaseTime) {
		this.revokecaseTime = revokecaseTime;
	}
	public String getRevokecaseDesc() {
		return revokecaseDesc;
	}
	public void setRevokecaseDesc(String revokecaseDesc) {
		this.revokecaseDesc = revokecaseDesc;
	}
	public String getHugeLossCode() {
		return hugeLossCode;
	}
	public void setHugeLossCode(String hugeLossCode) {
		this.hugeLossCode = hugeLossCode;
	}
	public String getTotalVerifiedTime() {
		return totalVerifiedTime;
	}
	public void setTotalVerifiedTime(String totalVerifiedTime) {
		this.totalVerifiedTime = totalVerifiedTime;
	}
	public String getCollidedThirdInd() {
		return collidedThirdInd;
	}
	public void setCollidedThirdInd(String collidedThirdInd) {
		this.collidedThirdInd = collidedThirdInd;
	}
	public String getActiontype() {
		return actiontype;
	}
	public void setActiontype(String actiontype) {
		this.actiontype = actiontype;
	}
	public String getSubrogationFlag() {
		return subrogationFlag;
	}
	public void setSubrogationFlag(String subrogationFlag) {
		this.subrogationFlag = subrogationFlag;
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
	
	
	
}
