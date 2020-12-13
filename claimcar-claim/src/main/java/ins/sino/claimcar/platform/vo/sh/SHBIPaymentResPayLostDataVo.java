/******************************************************************************
 * CREATETIME : 2016年6月6日 下午3:44:04
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author ★XMSH
 */
public class SHBIPaymentResPayLostDataVo {

	@XmlElement(name = "CLAIM_CODE")
	private String claimCode;// 理赔编码

	@XmlElement(name = "CLAIM_CONFIRM_CODE")
	private String claimConfirmCode;// 结案编码

	@XmlElement(name = "CLAIM_ADD_CODE")
	private String claimAddCode;// 结案追加码

	@XmlElement(name = "ERROR_MESSAGE")
	private String errorMessage;// 错误描述

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
	 * @return 返回 claimConfirmCode 结案编码
	 */
	public String getClaimConfirmCode() {
		return claimConfirmCode;
	}

	/**
	 * @param claimConfirmCode 要设置的 结案编码
	 */
	public void setClaimConfirmCode(String claimConfirmCode) {
		this.claimConfirmCode = claimConfirmCode;
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
	 * @return 返回 errorMessage 错误描述
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * @param errorMessage 要设置的 错误描述
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
