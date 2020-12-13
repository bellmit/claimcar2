/******************************************************************************
 * CREATETIME : 2016年6月1日 下午7:36:11
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author ★XMSH
 */
public class SHBIVClaimResFittingDataVo {

	@XmlElement(name = "OEM_PART_CODE")
	private String oemPartCode;// 原厂零件号

	@XmlElement(name = "RESPONSE_CODE", required = true)
	private String responseCode;// 返回类型代码

	@XmlElement(name = "ERROR_MESSAGE")
	private String errorMessage;// 错误描述

	/**
	 * @return 返回 oemPartCode 原厂零件号
	 */
	public String getOemPartCode() {
		return oemPartCode;
	}

	/**
	 * @param oemPartCode 要设置的 原厂零件号
	 */
	public void setOemPartCode(String oemPartCode) {
		this.oemPartCode = oemPartCode;
	}

	/**
	 * @return 返回 responseCode 返回类型代码
	 */
	public String getResponseCode() {
		return responseCode;
	}

	/**
	 * @param responseCode 要设置的 返回类型代码
	 */
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
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
