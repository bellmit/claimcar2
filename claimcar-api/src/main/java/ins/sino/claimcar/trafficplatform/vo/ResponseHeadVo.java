package ins.sino.claimcar.trafficplatform.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("Head")
public class ResponseHeadVo {
	@XStreamAlias("RequestType")
	private String requestType;//请求的接口类型代码

	@XStreamAlias("ResponseCode")
	private String responseCode;//返回信息标识代码

	@XStreamAlias("ErrorCode")
	private String errorCode;//返回信息标识代码

	@XStreamAlias("ErrorMessage")
	private String errorMessage;//错误说明

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
