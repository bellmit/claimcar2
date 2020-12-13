package ins.sino.claimcar.claim.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("Head")
public class RequestSdHeadVo implements Serializable{
	@XStreamAlias("RequestType")
	private String requestType;
	@XStreamAlias("User")
	private String user;
	@XStreamAlias("Password")
	private String password;
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
