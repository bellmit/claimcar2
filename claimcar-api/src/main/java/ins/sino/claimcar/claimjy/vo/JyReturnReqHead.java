package ins.sino.claimcar.claimjy.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("HEAD")
public class JyReturnReqHead  implements Serializable{
	private static final long serialVersionUID = 1L;
	@XStreamAlias("UserCode") 
    private String  userCode;  //用户名
    @XStreamAlias("Password")
    private String passWord;   // 密码  
    @XStreamAlias("RequestType")
    private String requestType;    //请求类型

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

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
    

    

    
	

}
