package ins.sino.claimcar.middlestagequery.vo;

public class Compensates implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	private String compensateNo;	 //计算书号
	private String registNo;		 //报案号
	private String claimNo;			 //立案号
	private String policyNo;		 //保单号
	private String currency;		 //币种
	private String sumAmt;           //赔付金额
	private String compensateText;   //计算书内容
	public String getCompensateNo() {
		return compensateNo;
	}
	public void setCompensateNo(String compensateNo) {
		this.compensateNo = compensateNo;
	}
	public String getRegistNo() {
		return registNo;
	}
	public void setRegistNo(String registNo) {
		this.registNo = registNo;
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
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getSumAmt() {
		return sumAmt;
	}
	public void setSumAmt(String sumAmt) {
		this.sumAmt = sumAmt;
	}
	public String getCompensateText() {
		return compensateText;
	}
	public void setCompensateText(String compensateText) {
		this.compensateText = compensateText;
	}
	
	
}
