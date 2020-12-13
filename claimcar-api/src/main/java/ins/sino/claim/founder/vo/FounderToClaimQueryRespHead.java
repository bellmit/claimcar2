package ins.sino.claim.founder.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.basic.BooleanConverter;

@XStreamAlias("HEAD")
public class FounderToClaimQueryRespHead implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("SystemCode")
	private String systemCode;
	
	@XStreamAlias("ResponseType")
	@XStreamConverter(value = BooleanConverter.class, booleans = {false}, strings = {"1","0"})
	private boolean responseType;
	
	@XStreamAlias("ErrorMessage")
	private String errorMessage;

	public String getSystemCode() {
		return systemCode;
	}

	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public boolean isResponseType() {
		return responseType;
	}

	public void setResponseType(boolean responseType) {
		this.responseType = responseType;
	}
	

}
