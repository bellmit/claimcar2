package ins.sino.claimcar.founder.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * 车险理赔报案获取电话号码vo
 * @author yzyuan
 *
 */
@XStreamAlias("HEAD")
public class RegistPhoneResHeadVo {
	
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
