/******************************************************************************
 * CREATETIME : 2016年4月27日 上午9:56:37
 ******************************************************************************/
package ins.sino.claimcar.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 返回头部
 * @author ★Luwei
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "HEAD")
public class CICheckResHeadVo {

	/**  查勘登记响应   **/
	@XmlElement(name = "REQUEST_TYPE")
	private String requestType;
	
	@XmlElement(name = "RESPONSE_CODE")
	private String responseCode;
	
	@XmlElement(name = "ERROR_CODE")
	private String errorCode;
	
	@XmlElement(name = "ERROR_MESSAGE")
	private String errorMessage;

	
	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
}
