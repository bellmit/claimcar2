package ins.sino.claimcar.founder.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * 获取电话号码
 * @author yzyuan
 *
 */
@XStreamAlias("OUTDATE")
public class RegistPhoneReqOutDateVo {
	
	@XStreamAlias("UserCode")
	private String userCode ;

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
}
