/******************************************************************************
 * CREATETIME : 2016年5月31日 下午7:37:49
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * 配件信息
 * @author ★XMSH
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SHCIVClaimResFittingDataVo {

	@XmlElement(name = "OEM_PART_CODE")
	private String oemPartCode;// 原厂零件号

	@XmlElement(name = "RESPONSE_CODE", required = true)
	private String responseCode;// 1-

	@XmlElement(name = "ERROR_MESSAGE")
	private String errorMessage;// 1-

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
	 * @return 返回 responseCode 1-
	 */
	public String getResponseCode() {
		return responseCode;
	}

	/**
	 * @param responseCode 要设置的 1-
	 */
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	/**
	 * @return 返回 errorMessage 1-
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * @param errorMessage 要设置的 1-
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
