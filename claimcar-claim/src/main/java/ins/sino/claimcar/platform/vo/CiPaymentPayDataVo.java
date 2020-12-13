/******************************************************************************
 * CREATETIME : 2016年5月25日 下午2:33:05
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
public class CiPaymentPayDataVo {

	@XmlElement(name = "ACCOUNT_NUMBER")
	private String accountNumber;// 赔款收款账户号

	@XmlElement(name = "BANK_ACCOUNT")
	private String bankAccount;// 赔款收款开户行

	@XmlElement(name = "ACCOUNT_NAME")
	private String accountName;// 赔款收款账户名

	@XmlElement(name = "CENTI_TYPE")
	private String centiType;// 赔款收款人证件类型；参见代码

	@XmlElement(name = "CENTI_CODE")
	private String centiCode;// 赔款收款人身份证/组织机构代码

	@XmlElement(name = "RECOVERY_CODE")
	private String recoveryCode;// 结算码

	@XmlElement(name = "CLAIM_AMOUNT", required = true)
	private Double claimAmount;// 赔偿支付金额

	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	@XmlElement(name = "PAY_TIME", required = true)
	private Date payTime;// 赔偿支付时间 格式：YYYYMMDDHHMM

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
	 * @return 返回 bankAccount 赔款收款开户行
	 */
	public String getBankAccount() {
		return bankAccount;
	}

	/**
	 * @param bankAccount 要设置的 赔款收款开户行
	 */
	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
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
	 * @return 返回 centiType 赔款收款人证件类型；参见代码
	 */
	public String getCentiType() {
		return centiType;
	}

	/**
	 * @param centiType 要设置的 赔款收款人证件类型；参见代码
	 */
	public void setCentiType(String centiType) {
		this.centiType = centiType;
	}

	/**
	 * @return 返回 centiCode 赔款收款人身份证/组织机构代码
	 */
	public String getCentiCode() {
		return centiCode;
	}

	/**
	 * @param centiCode 要设置的 赔款收款人身份证/组织机构代码
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
	 * @return 返回 claimAmount 赔偿支付金额
	 */
	public Double getClaimAmount() {
		return claimAmount;
	}

	/**
	 * @param claimAmount 要设置的 赔偿支付金额
	 */
	public void setClaimAmount(Double claimAmount) {
		this.claimAmount = claimAmount;
	}

	/**
	 * @return 返回 payTime 赔偿支付时间 格式：YYYYMMDDHHMM
	 */
	public Date getPayTime() {
		return payTime;
	}

	/**
	 * @param payTime 要设置的 赔偿支付时间 格式：YYYYMMDDHHMM
	 */
	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

}
