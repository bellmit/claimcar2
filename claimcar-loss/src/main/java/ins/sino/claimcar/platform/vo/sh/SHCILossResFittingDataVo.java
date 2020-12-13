/******************************************************************************
 * CREATETIME : 2016年5月26日 下午5:47:53
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * 配件信息（多条）FITTING_LIST隶属于车型信息
 * @author ★Luwei
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SHCILossResFittingDataVo {

	/**
	 * 原厂零件号
	 */
	@XmlElement(name = "OEM_PART_CODE")
	private String oemPartCode;// 原厂零件号

	/**
	 * 返回类型代码
	 */
	@XmlElement(name = "RESPONSE_CODE")
	private String responseCode;// 返回类型代码

	/**
	 * 错误描述
	 */
	@XmlElement(name = "ERROR_MESSAGE")
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
