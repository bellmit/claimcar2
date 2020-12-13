package ins.sino.claimcar.selfHelpClaimCar.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("HEAD")
public class ResponseHeadVo {
 @XStreamAlias("RESPONSETYPE")	
 private String responseType;//响应类型
 @XStreamAlias("RESPONSECODE")
 private String errno;//1：成功；其他：失败
 @XStreamAlias("RESPONSEMESSAGE")
 private String errmsg;//错误信息
public String getResponseType() {
	return responseType;
}
public void setResponseType(String responseType) {
	this.responseType = responseType;
}
public String getErrno() {
	return errno;
}
public void setErrno(String errno) {
	this.errno = errno;
}
public String getErrmsg() {
	return errmsg;
}
public void setErrmsg(String errmsg) {
	this.errmsg = errmsg;
}
 
}
