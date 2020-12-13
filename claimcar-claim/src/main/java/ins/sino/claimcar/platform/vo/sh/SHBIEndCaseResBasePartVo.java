/******************************************************************************
 * CREATETIME : 2016年6月6日 下午8:06:55
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author ★XMSH
 */
public class SHBIEndCaseResBasePartVo {

	@XmlElement(name = "CLAIM_CODE")
	private String claimCode;// 理赔编码

	@XmlElement(name = "CLAIM_CONFIRM_CODE")
	private String claimConfirmCode;// 业务结案校验码

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
	 * @return 返回 claimConfirmCode 业务结案校验码
	 */
	public String getClaimConfirmCode() {
		return claimConfirmCode;
	}

	/**
	 * @param claimConfirmCode 要设置的 业务结案校验码
	 */
	public void setClaimConfirmCode(String claimConfirmCode) {
		this.claimConfirmCode = claimConfirmCode;
	}

}
