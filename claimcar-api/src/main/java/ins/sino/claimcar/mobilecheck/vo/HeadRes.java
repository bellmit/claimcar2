package ins.sino.claimcar.mobilecheck.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 快赔对接返回的头对象
 */
@XStreamAlias("HEAD")
public class HeadRes implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;

	@XStreamAlias("RESPONSETYPE")
	private String responseType;
	
	@XStreamAlias("RESPONSECODE")
	private String responseCode;
	
	@XStreamAlias("RESPONSEMESSAGE")
	private String errorMessage;
	
	

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

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

}
