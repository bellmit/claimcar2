/******************************************************************************
 * CREATETIME : 2016年5月25日 下午2:55:54
 ******************************************************************************/
package ins.sino.claimcar.platform.vo;

import ins.sino.claimcar.carplatform.adapter.DateXmlAdapter;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * @author ★XMSH
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class BiPaymentPayDataVo {

	@XmlElement(name = "AccountNumber")
	private String accountNumber;// 赔款收款账户号

	@XmlElement(name = "BankName")
	private String bankName;// 赔款收款开户行

	@XmlElement(name = "AccountName")
	private String accountName;// 赔款收款账户名
	
	@XmlElement(name = "CentiType")
	private String centiType;  //赔款收款人证件类型
	
	@XmlElement(name = "CentiCode")
	private String centiCode;// 赔款收款 证件号码   

	@XmlElement(name = "RecoveryCode")
	private String recoveryCode;// 结算码

	@XmlElement(name = "PayAmount", required = true)
	private Double payAmount;// 赔偿支付金额（含施救费）

	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	@XmlElement(name = "PayDate", required = true)
	private Date payDate;// 赔偿支付时间；精确到分

	/**
	 * @return 返回 accountNumber 赔款收款账户号
	 */
	public String getAccountNumber() {
		return accountNumber;
	}

	/**
	 * @param accountNumber 要设置的 赔款收款账户号
	 */
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	/**
	 * @return 返回 bankName 赔款收款开户行
	 */
	public String getBankName() {
		return bankName;
	}

	/**
	 * @param bankName 要设置的 赔款收款开户行
	 */
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	/**
	 * @return 返回 accountName 赔款收款账户名
	 */
	public String getAccountName() {
		return accountName;
	}

	/**
	 * @param accountName 要设置的 赔款收款账户名
	 */
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	/**
	 * @return 返回 centiCode 赔款收款身份证/组织机构代码
	 */
	public String getCentiCode() {
		return centiCode;
	}

	/**
	 * @param centiCode 要设置的 赔款收款身份证/组织机构代码
	 */
	public void setCentiCode(String centiCode) {
		this.centiCode = centiCode;
	}

	/**
	 * @return 返回 recoveryCode 结算码
	 */
	public String getRecoveryCode() {
		return recoveryCode;
	}

	/**
	 * @param recoveryCode 要设置的 结算码
	 */
	public void setRecoveryCode(String recoveryCode) {
		this.recoveryCode = recoveryCode;
	}

	/**
	 * @return 返回 payAmount 赔偿支付金额（含施救费）
	 */
	public Double getPayAmount() {
		return payAmount;
	}

	/**
	 * @param payAmount 要设置的 赔偿支付金额（含施救费）
	 */
	public void setPayAmount(Double payAmount) {
		this.payAmount = payAmount;
	}

	/**
	 * @return 返回 payDate 赔偿支付时间；精确到分
	 */
	public Date getPayDate() {
		return payDate;
	}

	/**
	 * @param payDate 要设置的 赔偿支付时间；精确到分
	 */
	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}

	public String getCentiType() {
		return centiType;
	}

	public void setCentiType(String centiType) {
		this.centiType = centiType;
	}

}
