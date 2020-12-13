package ins.sino.claimcar.mobilecheck.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 查勘员号码更新的返回头对象
 */
@XStreamAlias("HEAD")
public class CallPhoneHeadRes implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;

	@XStreamAlias("SystemCode")
	private String systemCode;
	
	@XStreamAlias("ResponseType")
	private String responseType;
	
	@XStreamAlias("ErrorMessage")
	private String errorMessage;

	public String getSystemCode() {
		return systemCode;
	}

	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}

	public String getResponseType() {
		return responseType;
	}

	public void setResponseType(String responseType) {
		this.responseType = responseType;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}


	
}
