/******************************************************************************
 * CREATETIME : 2016年5月25日 下午2:30:50
 ******************************************************************************/
package ins.sino.claimcar.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author ★XMSH
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CiPaymentBasePartVo {

	@XmlElement(name = "CONFIRM_SEQUENCE_NO", required = true)
	private String confirmSequenceNo;// 投保确认码

	@XmlElement(name = "CLAIM_CODE", required = true)
	private String claimCode;// 理赔编码
	
	@XmlElement(name = "COMPE_NO", required = true)
	private String compensateNo;// 理赔编码

	@XmlElement(name = "REPORT_NO", required = true)
	private String reportNo;// 报案号

	@XmlElement(name = "CLAIM_AMOUNT", required = true)
	private Double claimAmount;// 赔偿总金额

	@XmlElement(name = "BANK_ACCOUNT")
	private String bankAccount;// 赔款支付开户行

	@XmlElement(name = "ACCOUNT_NUMBER")
	private String accountNumber;// 赔款支付账户号

	@XmlElement(name = "ACCOUNT_NAME")
	private String accountName;// 赔款支付账户名

	/**
	 * @return 返回 confirmSequenceNo 投保确认码
	 */
	public String getConfirmSequenceNo() {
		return confirmSequenceNo;
	}

	/**
	 * @param confirmSequenceNo 要设置的 投保确认码
	 */
	public void setConfirmSequenceNo(String confirmSequenceNo) {
		this.confirmSequenceNo = confirmSequenceNo;
	}

	/**
	 * @return 返回 claimCode 理赔编码
	 */
	public String getClaimCode() {
		return claimCode;
	}

	/**
	 * @param claimCode 要设置的 理赔编码
	 */
	public void setClaimCode(String claimCode) {
		this.claimCode = claimCode;
	}

	/**
	 * @return 返回 reportNo 报案号
	 */
	public String getReportNo() {
		return reportNo;
	}

	/**
	 * @param reportNo 要设置的 报案号
	 */
	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}

	/**
	 * @return 返回 claimAmount 赔偿总金额
	 */
	public Double getClaimAmount() {
		return claimAmount;
	}

	/**
	 * @param claimAmount 要设置的 赔偿总金额
	 */
	public void setClaimAmount(Double claimAmount) {
		this.claimAmount = claimAmount;
	}

	/**
	 * @return 返回 bankAccount 赔款支付开户行
	 */
	public String getBankAccount() {
		return bankAccount;
	}

	/**
	 * @param bankAccount 要设置的 赔款支付开户行
	 */
	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	/**
	 * @return 返回 accountNumber 赔款支付账户号
	 */
	public String getAccountNumber() {
		return accountNumber;
	}

	/**
	 * @param accountNumber 要设置的 赔款支付账户号
	 */
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	/**
	 * @return 返回 accountName 赔款支付账户名
	 */
	public String getAccountName() {
		return accountName;
	}

	/**
	 * @param accountName 要设置的 赔款支付账户名
	 */
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getCompensateNo() {
		return compensateNo;
	}

	public void setCompensateNo(String compensateNo) {
		this.compensateNo = compensateNo;
	}

	
}
