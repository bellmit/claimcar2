package ins.sino.claimcar.founder.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 车险报案vo（理赔请求客服系统返回）
 * @author Luwei
 *
 */
@XStreamAlias("HEAD")
public class CarRegistResHeadVo {

	@XStreamAlias("SystemCode")
	private String systemCode;

	@XStreamAlias("ResponseType")
	private String responseType;

	@XStreamAlias("ErrorCode")
	private String errorCode;

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
