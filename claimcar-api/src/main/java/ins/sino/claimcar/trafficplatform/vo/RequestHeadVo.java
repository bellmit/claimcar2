package ins.sino.claimcar.trafficplatform.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("Head")
public class RequestHeadVo implements Serializable{
 private static final long serialVersionUID = 1L;
 
@XStreamAlias("RequestType")
private String requestType;//请求类型

@XStreamAlias("User")
private String user;//用户名

@XStreamAlias("Password")
private String password;//密码

public String getRequestType() {
	return requestType;
}
public void setRequestType(String requestType) {
	this.requestType = requestType;
}
public String getUser() {
	return user;
}
public void setUser(String user) {
	this.user = user;
}
public String getPassword() {
	return password;
}
public void setPassword(String password) {
	this.password = password;
}

}
