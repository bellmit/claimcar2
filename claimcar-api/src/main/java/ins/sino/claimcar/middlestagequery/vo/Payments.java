package ins.sino.claimcar.middlestagequery.vo;

/**
 * 赔款支付信息
 * @author j2eel
 *
 */
public class Payments implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	private String accountNo;     //收款银行账号
	private String accountName;   //账号名称/支付对象
	private String currency;      //币种
	private String sumRealPay;    //支付金额
	private String payTime;       //支付时间
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getSumRealPay() {
		return sumRealPay;
	}
	public void setSumRealPay(String sumRealPay) {
		this.sumRealPay = sumRealPay;
	}
	public String getPayTime() {
		return payTime;
	}
	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}
	
	
}
