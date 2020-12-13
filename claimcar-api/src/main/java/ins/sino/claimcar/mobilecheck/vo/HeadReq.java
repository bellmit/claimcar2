package ins.sino.claimcar.mobilecheck.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;


/**
 * 快赔对接请求的头对象
 */
@XStreamAlias("HEAD")
public class HeadReq implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;

	@XStreamAlias("REQUESTTYPE")
	private String requestType = "";
	@XStreamAlias("USER")
	private String user = "";
	@XStreamAlias("PASSWORD")
	private String password = "";

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
