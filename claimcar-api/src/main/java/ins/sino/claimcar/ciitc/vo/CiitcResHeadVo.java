package ins.sino.claimcar.ciitc.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("Head") 
public class CiitcResHeadVo implements Serializable{
	 private static final long serialVersionUID = 1L;

	    @XStreamAlias("requestType") 
	    private String requestType;  //请求的接口类型代码
	    
	    @XStreamAlias("resCode")
	    private String resCode;    //返回状态标识：0-成功;1-失败
	    
	    @XStreamAlias("errorCode")
	    private String errorCode;   //错误代码
	    
	    @XStreamAlias("errorMessage")
	    private String errorMessage;    //错误说明

		public String getRequestType() {
			return requestType;
		}

		public void setRequestType(String requestType) {
			this.requestType = requestType;
		}

		public String getResCode() {
			return resCode;
		}

		public void setResCode(String resCode) {
			this.resCode = resCode;
		}

		public String getErrorCode() {
			return errorCode;
		}

		public void setErrorCode(String errorCode) {
			this.errorCode = errorCode;
		}

		public String getErrorMessage() {
			return errorMessage;
		}

		public void setErrorMessage(String errorMessage) {
			this.errorMessage = errorMessage;
		}
	    
	   
}
