package ins.sino.claimcar.mobilecheck.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;


/**
 * 3.7查勘员号码更新请求的头对象
 */
@XStreamAlias("HEAD")
public class CallPhoneHeadReq implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;

	@XStreamAlias("SystemCode")
	private String systemCode = "";
	@XStreamAlias("UserCode")
	private String userCode = "";
	@XStreamAlias("Password")
	private String passWord = "";
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
	public String getPassWord() {
		return passWord;
	}
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	


	
	

}
