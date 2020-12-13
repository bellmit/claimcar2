package ins.sino.claimcar.other.vo;

import oracle.sql.DATE;

public class PrpJLinkAccountVoBase implements java.io.Serializable{
	
	private static final long serialVersionUID = 1L;
	private String linkNo;
	private String certiType;
	private String certiNo;
	private String payrefReason;
	private String claimNo;
	private String policyNo;
	private String comCode;
	private Double planFee;
	private String clientNo;
	private String accountNo;
	private String payReasonFlag;
	private String payReason;
	private DATE operateDate;
	private String operateCode;
	
	private String operateBranch;
	private String reMark;
	private String flag;
	private String sNo;
	private Double oriplanFee;
	private Double commission;
	private String licenseNo;
	private String parentPayObject;
	private String centiType;
	private String centiCode;
	private String subPayObject;
	
	protected PrpJLinkAccountVoBase(){
		
	}
	
	public String getLinkNo() {
		return linkNo;
	}
	public void setLinkNo(String linkNo) {
		this.linkNo = linkNo;
	}
	public String getCertiType() {
		return certiType;
	}
	public void setCertiType(String certiType) {
		this.certiType = certiType;
	}
	public String getCertiNo() {
		return certiNo;
	}
	public void setCertiNo(String certiNo) {
		this.certiNo = certiNo;
	}
	public String getPayrefReason() {
		return payrefReason;
	}
	public void setPayrefReason(String payrefReason) {
		this.payrefReason = payrefReason;
	}
	public String getClaimNo() {
		return claimNo;
	}
	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}
	public String getPolicyNo() {
		return policyNo;
	}
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}
	public String getComCode() {
		return comCode;
	}
	public void setComCode(String comCode) {
		this.comCode = comCode;
	}
	public Double getPlanFee() {
		return planFee;
	}
	public void setPlanFee(Double planFee) {
		this.planFee = planFee;
	}
	public String getClientNo() {
		return clientNo;
	}
	public void setClientNo(String clientNo) {
		this.clientNo = clientNo;
	}
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public String getPayReasonFlag() {
		return payReasonFlag;
	}
	public void setPayReasonFlag(String payReasonFlag) {
		this.payReasonFlag = payReasonFlag;
	}
	public String getPayReason() {
		return payReason;
	}
	public void setPayReason(String payReason) {
		this.payReason = payReason;
	}
	public DATE getOperateDate() {
		return operateDate;
	}
	public void setOperateDate(DATE operateDate) {
		this.operateDate = operateDate;
	}
	public String getOperateCode() {
		return operateCode;
	}
	public void setOperateCode(String operateCode) {
		this.operateCode = operateCode;
	}
	public String getOperateBranch() {
		return operateBranch;
	}
	public void setOperateBranch(String operateBranch) {
		this.operateBranch = operateBranch;
	}
	public String getReMark() {
		return reMark;
	}
	public void setReMark(String reMark) {
		this.reMark = reMark;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getsNo() {
		return sNo;
	}
	public void setsNo(String sNo) {
		this.sNo = sNo;
	}
	public Double getOriplanFee() {
		return oriplanFee;
	}
	public void setOriplanFee(Double oriplanFee) {
		this.oriplanFee = oriplanFee;
	}
	public Double getCommission() {
		return commission;
	}
	public void setCommission(Double commission) {
		this.commission = commission;
	}
	public String getLicenseNo() {
		return licenseNo;
	}
	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}
	public String getParentPayObject() {
		return parentPayObject;
	}
	public void setParentPayObject(String parentPayObject) {
		this.parentPayObject = parentPayObject;
	}
	public String getCentiType() {
		return centiType;
	}
	public void setCentiType(String centiType) {
		this.centiType = centiType;
	}
	public String getCentiCode() {
		return centiCode;
	}
	public void setCentiCode(String centiCode) {
		this.centiCode = centiCode;
	}
	public String getSubPayObject() {
		return subPayObject;
	}
	public void setSubPayObject(String subPayObject) {
		this.subPayObject = subPayObject;
	}

}
