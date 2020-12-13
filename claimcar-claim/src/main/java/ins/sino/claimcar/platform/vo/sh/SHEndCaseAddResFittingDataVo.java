/******************************************************************************
 * CREATETIME : 2016年6月1日 下午3:27:04
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * 配件信息（多条）
 * @author ★Luwei
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SHEndCaseAddResFittingDataVo {

	/**
	 * 原厂零件号
	 */
	@XmlElement(name = "OEM_PART_CODE", required = true)
	private String oemPartCode;// 原厂零件号

	/**
	 * 返回类型代码
	 */
	@XmlElement(name = "RESPONSE_CODE", required = true)
	private String responseCode;// 返回类型代码

	/**
	 * 错误描述
	 */
	@XmlElement(name = "ERROR_MESSAGE", required = true)
	private String errorMessage;// 错误描述

	public String getOemPartCode() {
		return oemPartCode;
	}

	public void setOemPartCode(String oemPartCode) {
		this.oemPartCode = oemPartCode;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
