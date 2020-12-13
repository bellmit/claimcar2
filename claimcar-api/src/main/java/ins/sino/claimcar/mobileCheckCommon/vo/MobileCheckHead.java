package ins.sino.claimcar.mobileCheckCommon.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("HEAD")
public class MobileCheckHead implements Serializable{
	/**  */
	private static final long serialVersionUID = 1L;

	@XStreamAlias("REQUESTTYPE") 
	private String  requestType;  //请求类型
	
	@XStreamAlias("USER")
	private String user;    //用户名
	
	@XStreamAlias("PASSWORD")
	private String passWord;   // 密码
	
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
