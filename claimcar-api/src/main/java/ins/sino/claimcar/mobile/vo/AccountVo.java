package ins.sino.claimcar.mobile.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("ACCOUNT")
public class AccountVo  implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;


	/*
	 * 收款人类型
	 */
	@XStreamAlias("PAYEETYPE")
	private String payeeType;
	
	/*
	 * 姓名
	 */
	@XStreamAlias("NAME")
	private String name;
	
	/*
	 * 身份证号码
	 */
	@XStreamAlias("IDENTIFYNUMBER")
	private String identifyNumber;
	/*
	 * 开户行
	 */
	@XStreamAlias("BANKNAME")
	private String bankName;
	/*
	 * 账户名
	 */
	@XStreamAlias("ACCOUNTNAME")
	private String accountName;
	/*
	 * 银行账户
	 */
	@XStreamAlias("ACCOUNTNO")
	private String accountNo;
	/*
	 * 联系电话
	 */
	@XStreamAlias("PHONE")
	private String phone;

	public String getPayeeType() {
		return payeeType;
	}
	public void setPayeeType(String payeeType) {
		this.payeeType = payeeType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIdentifyNumber() {
		return identifyNumber;
	}
	public void setIdentifyNumber(String identifyNumber) {
		this.identifyNumber = identifyNumber;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	
}
