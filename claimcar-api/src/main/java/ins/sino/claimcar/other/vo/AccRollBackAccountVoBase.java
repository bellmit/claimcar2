package ins.sino.claimcar.other.vo;

public class AccRollBackAccountVoBase implements java.io.Serializable{
	
	private static final long serialVersionUID = 1L;
	private String certiNo;
	private String voucherNo;
	private String yearMonth;
	private String centerCode;
	private String suffixNo;
	private String payType;
	private String originalAccountNo;
	private String targetAccountNo;
	private String rollBackCode;
	private String rollBackTime;
	private String modifyCode;
	private String modifyTime;
	private String status;
	
	public String getCertiNo() {
		return certiNo;
	}
	public void setCertiNo(String certiNo) {
		this.certiNo = certiNo;
	}
	public String getVoucherNo() {
		return voucherNo;
	}
	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}
	public String getYearMonth() {
		return yearMonth;
	}
	public void setYearMonth(String yearMonth) {
		this.yearMonth = yearMonth;
	}
	public String getCenterCode() {
		return centerCode;
	}
	public void setCenterCode(String centerCode) {
		this.centerCode = centerCode;
	}
	public String getSuffixNo() {
		return suffixNo;
	}
	public void setSuffixNo(String suffixNo) {
		this.suffixNo = suffixNo;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getOriginalAccountNo() {
		return originalAccountNo;
	}
	public void setOriginalAccountNo(String originalAccountNo) {
		this.originalAccountNo = originalAccountNo;
	}
	public String getTargetAccountNo() {
		return targetAccountNo;
	}
	public void setTargetAccountNo(String targetAccountNo) {
		this.targetAccountNo = targetAccountNo;
	}
	public String getRollBackCode() {
		return rollBackCode;
	}
	public void setRollBackCode(String rollBackCode) {
		this.rollBackCode = rollBackCode;
	}
	public String getRollBackTime() {
		return rollBackTime;
	}
	public void setRollBackTime(String rollBackTime) {
		this.rollBackTime = rollBackTime;
	}
	public String getModifyCode() {
		return modifyCode;
	}
	public void setModifyCode(String modifyCode) {
		this.modifyCode = modifyCode;
	}
	public String getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

}
