/******************************************************************************
 * CREATETIME : 2016年5月24日 下午5:24:15
 ******************************************************************************/
package ins.sino.claimcar.platform.vo;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author ★XMSH
 */
public class BiEndCaseResBasePartVo {

	@XmlElement(name = "ClaimSequenceNo", required = true)
	private String claimSequenceNo;// 理赔编号

	@XmlElement(name = "ClaimConfirmCode", required = true)
	private String claimConfirmCode;// 赔案结案校验码

	/**
	 * @return 返回 claimSequenceNo 理赔编号
	 */
	public String getClaimSequenceNo() {
		return claimSequenceNo;
	}

	/**
	 * @param claimSequenceNo 要设置的 理赔编号
	 */
	public void setClaimSequenceNo(String claimSequenceNo) {
		this.claimSequenceNo = claimSequenceNo;
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
