package ins.sino.claimcar.claim.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("Head")
public class ResHeadSdVo implements Serializable{
 @XStreamAlias("RequestType")
 private String requestType;
 @XStreamAlias("ResponseCode")
 private String responseCode;
 @XStreamAlias("ErrorCode")
 private String errorCode;
 @XStreamAlias("ErrorMessage")
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
