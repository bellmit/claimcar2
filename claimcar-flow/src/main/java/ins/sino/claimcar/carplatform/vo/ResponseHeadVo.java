/******************************************************************************
* CREATETIME : 2016年3月14日 上午10:35:56
******************************************************************************/
package ins.sino.claimcar.carplatform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * 返回头部
 * @author ★LiuPing
 * @CreateTime 2016年3月14日
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "HEAD")
public class ResponseHeadVo {
	
	@XmlElement(name="REQUEST_TYPE")
	private String requestType;
	@XmlElement(name="RESPONSE_CODE")
	private String responseCode;
	@XmlElement(name="ERROR_CODE")
	private String errorCode;
	@XmlElement(name="ERROR_MESSAGE")
	private String errorDesc;
	
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

	public String getErrorDesc() {
		return errorDesc;
	}

	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
	}
}
