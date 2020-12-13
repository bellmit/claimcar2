package ins.sino.claimcar.founder.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("OUTDATE")
public class RegistPhoneResOutDateVo {
	
	@XStreamAlias("UserCode")
	private String userCode;
	
	@XStreamAlias("Phone")
	private String phone;

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

}
