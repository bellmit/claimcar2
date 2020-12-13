package ins.sino.claimcar.sunyardimage.vo.common;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;


public class RootVo implements Serializable{
	@XStreamAlias("RESPONSE_CODE")
	private String responseCode;
	@XStreamAlias("RESPONSE_MSG")
	private String responseMsg;
	
	public String getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	public String getResponseMsg() {
		return responseMsg;
	}
	public void setResponseMsg(String responseMsg) {
		this.responseMsg = responseMsg;
	}

}
