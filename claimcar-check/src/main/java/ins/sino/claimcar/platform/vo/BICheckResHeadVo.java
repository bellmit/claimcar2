/******************************************************************************
* CREATETIME : 2016年4月29日 下午3:59:27
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
@XmlRootElement(name = "Head")
public class BICheckResHeadVo {

	/**  查勘登记响应   **/
	@XmlElement(name = "RequestType")
	private String requestType;
	
	@XmlElement(name = "ResponseCode")
	private String responseCode;
	
	@XmlElement(name = "ErrorCode")
	private String errorCode;
	
	@XmlElement(name = "ErrorMessage")
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
