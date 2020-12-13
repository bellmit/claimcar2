/******************************************************************************
 * CREATETIME : 2016年5月24日 下午3:35:00
 ******************************************************************************/
package ins.sino.claimcar.platform.vo;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author ★XMSH
 */
public class CiEndCaseResBasePartVo {

	@XmlElement(name = "CLAIM_CODE", required = true)
	private String claimCode;// 理赔编号

	@XmlElement(name = "CLAIM_CONFIRM_CODE", required = true)
	private String claimConfirmCode;// 赔案结案校验码

	/**
	 * @return 返回 claimCode 理赔编号
	 */
	public String getClaimCode() {
		return claimCode;
	}

	/**
	 * @param claimCode 要设置的 理赔编号
	 */
	public void setClaimCode(String claimCode) {
		this.claimCode = claimCode;
	}

	/**
	 * @return 返回 claimConfirmCode 赔案结案校验码
	 */
	public String getClaimConfirmCode() {
		return claimConfirmCode;
	}

	/**
	 * @param claimConfirmCode 要设置的 赔案结案校验码
	 */
	public void setClaimConfirmCode(String claimConfirmCode) {
		this.claimConfirmCode = claimConfirmCode;
	}

}
