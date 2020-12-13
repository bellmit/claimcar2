package ins.sino.claimcar.check.vo.history;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
/**
 * 返回头部
 * @author ★Luwei
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Head")
public class CarInfoHistoryResHeadVo implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	   /** 风险预警理赔信息查询   **/
	    @XmlElement(name = "RequestType")
		private String requestType;
		
		@XmlElement(name = "ResponseCode")
		private String responseCode;
		
		@XmlElement(name = "ErrorCode")
		private String errorCode;
		
		@XmlElement(name = "ErrorMessage")
		private String errorMessage;

		
		public String getRequestType() {
			return requestType;
		}

		public void setRequestType(String requestType) {
			this.requestType = requestType;
		}

		public String getResponseCode() {
			return responseCode;
		}

		public void setResponseCode(String responseCode) {
			this.responseCode = responseCode;
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


