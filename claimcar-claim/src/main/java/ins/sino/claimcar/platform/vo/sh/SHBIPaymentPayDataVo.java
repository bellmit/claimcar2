/******************************************************************************
 * CREATETIME : 2016年6月6日 下午2:53:55
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author ★XMSH
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SHBIPaymentPayDataVo {

	@XmlElement(name = "R_BANK_ACCOUNT")
	private String rBankAccount;// 赔款收款账户号

	@XmlElement(name = "R_BANK_NAME")
	private String rBankName;// 赔款收款开户行

	@XmlElement(name = "R_BANK_ACCOUNT_NAME")
	private String rBankAccountName;// 赔款收款账户名

	@XmlElement(name = "R_BANK_CENTICODE")
	private String rBankCenticode;// 赔款收款身份证

	@XmlElement(name = "CLAIM_CODE")
	private String claimCode;// 理赔编码

	@XmlElement(name = "CLAIM_ADD_CODE")
	private String claimAddCode;// 结案追加码

	@XmlElement(name = "CLAIM_AMOUNT")
	private Double claimAmount;// 赔偿金额

	@XmlElement(name = "CLAIM_TYPE", required = true)
	private String claimType;// 支付类型

	/**
	 * @return 返回 rBankAccount 赔款收款账户号
	 */
	public String getRBankAccount() {
		return rBankAccount;
	}

	/**
	 * @param rBankAccount 要设置的 赔款收款账户号
	 */
	public void setRBankAccount(String rBankAccount) {
		this.rBankAccount = rBankAccount;
	}

	/**
	 * @return 返回 rBankName 赔款收款开户行
	 */
	public String getRBankName() {
		return rBankName;
	}

	/**
	 * @param rBankName 要设置的 赔款收款开户行
	 */
	public void setRBankName(String rBankName) {
		this.rBankName = rBankName;
	}

	/**
	 * @return 返回 rBankAccountName 赔款收款账户名
	 */
	public String getRBankAccountName() {
		return rBankAccountName;
	}

	/**
	 * @param rBankAccountName 要设置的 赔款收款账户名
	 */
	public void setRBankAccountName(String rBankAccountName) {
		this.rBankAccountName = rBankAccountName;
	}

	/**
	 * @return 返回 rBankCenticode 赔款收款身份证
	 */
	public String getRBankCenticode() {
		return rBankCenticode;
	}

	/**
	 * @param rBankCenticode 要设置的 赔款收款身份证
	 */
	public void setRBankCenticode(String rBankCenticode) {
		this.rBankCenticode = rBankCenticode;
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
	 * @return 返回 claimAddCode 结案追加码
	 */
	public String getClaimAddCode() {
		return claimAddCode;
	}

	/**
	 * @param claimAddCode 要设置的 结案追加码
	 */
	public void setClaimAddCode(String claimAddCode) {
		this.claimAddCode = claimAddCode;
	}

	/**
	 * @return 返回 claimAmount 赔偿金额
	 */
	public Double getClaimAmount() {
		return claimAmount;
	}

	/**
	 * @param claimAmount 要设置的 赔偿金额
	 */
	public void setClaimAmount(Double claimAmount) {
		this.claimAmount = claimAmount;
	}

	/**
	 * @return 返回 claimType 支付类型
	 */
	public String getClaimType() {
		return claimType;
	}

	/**
	 * @param claimType 要设置的 支付类型
	 */
	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}

}
