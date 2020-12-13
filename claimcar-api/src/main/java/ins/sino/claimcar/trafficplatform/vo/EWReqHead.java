package ins.sino.claimcar.trafficplatform.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Head")
public class EWReqHead implements Serializable{

	private static final long serialVersionUID = -6895555819573576510L;
	
	@XStreamAlias("RequestType")
	private String requestType; 	//<!-- 请求的接口类型代码-->
	
	@XStreamAlias("User")
	private String user; 	//<!-- 用户代码 -->
	
	@XStreamAlias("Password")
	private String passWord; 	//<!-- 用户密码 -->

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

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	
}
