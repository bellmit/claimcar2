/******************************************************************************
 * CREATETIME : 2016年5月25日 下午2:47:35
 ******************************************************************************/
package ins.sino.claimcar.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author ★XMSH
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class BiPaymentBasePartVo {

	@XmlElement(name = "ClaimSequenceNo", required = true)
	private String claimSequenceNo;// 理赔编号

	@XmlElement(name = "ClaimNotificationNo", required = true)
	private String claimNotificationNo;// 报案号

	@XmlElement(name = "ConfirmSequenceNo", required = true)
	private String confirmSequenceNo;// 投保确认码

	@XmlElement(name = "PayAmount", required = true)
	private Double payAmount;// 赔付总金额（含施救费）

	@XmlElement(name = "BankName")
	private String bankName;// 赔款支付开户行

	@XmlElement(name = "AccountNumber")
	private String accountNumber;// 赔款支付账户号

	@XmlElement(name = "AccountName")
	private String accountName;// 赔款支付账户名

	@XmlElement(name = "compeNo")
	private String compensateNo;// 计算书号

	/**
	 * @return 返回 claimSequenceNo 理赔编号
	 */
	public String getClaimSequenceNo() {
		return claimSequenceNo;
	}

	/**
	 * @param claimSequenceNo
	 *            要设置的 理赔编号
	 */
	public void setClaimSequenceNo(String claimSequenceNo) {
		this.claimSequenceNo = claimSequenceNo;
	}

	/**
	 * @return 返回 claimNotificationNo 报案号
	 */
	public String getClaimNotificationNo() {
		return claimNotificationNo;
	}

	/**
	 * @param claimNotificationNo
	 *            要设置的 报案号
	 */
	public void setClaimNotificationNo(String claimNotificationNo) {
		this.claimNotificationNo = claimNotificationNo;
	}

	/**
	 * @return 返回 confirmSequenceNo 投保确认码
	 */
	public String getConfirmSequenceNo() {
		return confirmSequenceNo;
	}

	/**
	 * @param confirmSequenceNo
	 *            要设置的 投保确认码
	 */
	public void setConfirmSequenceNo(String confirmSequenceNo) {
		this.confirmSequenceNo = confirmSequenceNo;
	}

	/**
	 * @return 返回 payAmount 赔付总金额（含施救费）
	 */
	public Double getPayAmount() {
		return payAmount;
	}

	/**
	 * @param payAmount
	 *            要设置的 赔付总金额（含施救费）
	 */
	public void setPayAmount(Double payAmount) {
		this.payAmount = payAmount;
	}

	/**
	 * @return 返回 bankName 赔款支付开户行
	 */
	public String getBankName() {
		return bankName;
	}

	/**
	 * @param bankName
	 *            要设置的 赔款支付开户行
	 */
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	/**
	 * @return 返回 accountNumber 赔款支付账户号
	 */
	public String getAccountNumber() {
		return accountNumber;
	}

	/**
	 * @param accountNumber
	 *            要设置的 赔款支付账户号
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
	 * @param accountName
	 *            要设置的 赔款支付账户名
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
