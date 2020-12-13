package ins.sino.claimcar.claimjy.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("HEAD") 
public class JyReqHeadVo implements Serializable{
	private static final long serialVersionUID = 1L;

    @XStreamAlias("UserCode") 
    private String  userCode;  //用户名
    
    @XStreamAlias("Password")
    private String passWord;   // 密码
    
    @XStreamAlias("RequestSourceCode")
    private String requestSourceCode;    //请求来源代码
    
    @XStreamAlias("RequestSourceName")
    private String requestSourceName;    //请求来源名称
    
    @XStreamAlias("RequestType")
    private String requestType;    //请求类型
    
    @XStreamAlias("OperatingTime")
    private String operatingTime;    //操作时间
    
    @XStreamAlias("RequestSource")
    private String requestSource;    //pc传0，安卓传1

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

	public String getRequestSourceCode() {
		return requestSourceCode;
	}

	public void setRequestSourceCode(String requestSourceCode) {
		this.requestSourceCode = requestSourceCode;
	}

	public String getRequestSourceName() {
		return requestSourceName;
	}

	public void setRequestSourceName(String requestSourceName) {
		this.requestSourceName = requestSourceName;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getOperatingTime() {
		return operatingTime;
	}

	public void setOperatingTime(String operatingTime) {
		this.operatingTime = operatingTime;
	}
}
