package ins.sino.claimcar.mobileCheckCommon.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("HEAD")
public class MobileCheckResponseHead implements Serializable{

	/**  */
	private static final long serialVersionUID = -6895555819573576510L;
	
	@XStreamAlias("RESPONSETYPE")
	private String responseType; 	//<!-- 返回类型 -->
	
	@XStreamAlias("RESPONSECODE")
	private String responseCode;    // !--YES返回正确数据，NO返回错误数据 -->
	
	@XStreamAlias("RESPONSEMESSAGE")
	private String responseMessage;   // <!-- 正确或错误原因，中文信息-->

	public String getResponseType() {
		return responseType;
	}

	public void setResponseType(String responseType) {
		this.responseType = responseType;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}
	
	

}
