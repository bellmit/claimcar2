package ins.sino.claimcar.invoice.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.basic.BooleanConverter;

@XStreamAlias("Head")
public class HeadRemoteRes implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 请求代码 **/
	@XStreamAlias("RequestType")
	private String requestType;
	/** 响应类型 **/
	@XStreamAlias("ResponseCode")
	@XStreamConverter(value = BooleanConverter.class, booleans = {false}, strings = {"1","0"})
	private boolean responseCode;
	/** 错误说明,如果成功返回空 **/
	@XStreamAlias("ErrorMessage")
	private String errorMessage;

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public boolean isResponseCode() {
		return responseCode;
	}

	public void setResponseCode(boolean responseCode) {
		this.responseCode = responseCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
