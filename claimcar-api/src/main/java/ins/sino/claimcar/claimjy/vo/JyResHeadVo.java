package ins.sino.claimcar.claimjy.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("HEAD")
public class JyResHeadVo implements Serializable{

    private static final long serialVersionUID = 1L;
    @XStreamAlias("ResponseCode")    //返回类型代码
    private String responseCode;
    @XStreamAlias("ErrorMessage")    //错误描述
    private String errorMessage;
    public String getResponseCode() {
    	return responseCode;
    }
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	  
}
