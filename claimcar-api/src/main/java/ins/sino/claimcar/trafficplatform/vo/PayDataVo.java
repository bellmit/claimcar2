package ins.sino.claimcar.trafficplatform.vo;

public class PayDataVo {
	private String payNumber;//赔款计算书号  Y
	private String bankName;//收款人开户行名称  Y
	private String payeeNo;//收款人账户号 Y
	private String payeeName;//收款人账户名 Y
	private String certificateType;//收款人证件类型 Y
	private String certificateNo;//收款人证件号码 Y
	private String payeePhone;//收款人联系电话 Y
	private String payUnitAcount;//款项金额   Y
	private String payTime;//款项支付时间   Y  yyyy-MM-dd
	private String payDesc;//款项支付说明   N
	public String getPayNumber() {
		return payNumber;
	}
	public void setPayNumber(String payNumber) {
		this.payNumber = payNumber;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getPayeeNo() {
		return payeeNo;
	}
	public void setPayeeNo(String payeeNo) {
		this.payeeNo = payeeNo;
	}
	public String getPayeeName() {
		return payeeName;
	}
	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}
	public String getCertificateType() {
		return certificateType;
	}
	public void setCertificateType(String certificateType) {
		this.certificateType = certificateType;
	}
	public String getCertificateNo() {
		return certificateNo;
	}
	public void setCertificateNo(String certificateNo) {
		this.certificateNo = certificateNo;
	}
	public String getPayeePhone() {
		return payeePhone;
	}
	public void setPayeePhone(String payeePhone) {
		this.payeePhone = payeePhone;
	}
	public String getPayUnitAcount() {
		return payUnitAcount;
	}
	public void setPayUnitAcount(String payUnitAcount) {
		this.payUnitAcount = payUnitAcount;
	}
	public String getPayTime() {
		return payTime;
	}
	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}
	public String getPayDesc() {
		return payDesc;
	}
	public void setPayDesc(String payDesc) {
		this.payDesc = payDesc;
	}
	
	
}
