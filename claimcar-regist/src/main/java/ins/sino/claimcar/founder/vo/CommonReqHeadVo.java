package ins.sino.claimcar.founder.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 公共的请求头部vo（理赔请求客服系统）
 * @author Luwei
 */
@XStreamAlias("HEAD")
public class CommonReqHeadVo {

	@XStreamAlias("SystemCode")
	private String systemCode;

	@XStreamAlias("UserCode")
	private String userCode;

	@XStreamAlias("Password")
	private String password;

	
	public String getSystemCode() {
		return systemCode;
	}

	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
