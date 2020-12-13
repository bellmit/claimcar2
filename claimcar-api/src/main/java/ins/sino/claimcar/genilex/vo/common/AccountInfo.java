package ins.sino.claimcar.genilex.vo.common;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("AccountInfo")
public class AccountInfo implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("UserName") 
	private String  userName;

	@XStreamAlias("UserPswd") 
	private String  userPswd;
	
	@XStreamAlias("CryptType") 
	private String  cryptType;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPswd() {
		return userPswd;
	}

	public void setUserPswd(String userPswd) {
		this.userPswd = userPswd;
	}

	public String getCryptType() {
		return cryptType;
	}

	public void setCryptType(String cryptType) {
		this.cryptType = cryptType;
	}

}
