package ins.sino.claimcar.selfHelpClaimCar.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("HEAD")
public class ResquestHeadVo {
	@XStreamAlias("REQUESTTYPE")
	private String requestType;//请求类型
	@XStreamAlias("USER")
	private String user;//用户名
	@XStreamAlias("PASSWORD")
	private String passWord;//用户密码
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
