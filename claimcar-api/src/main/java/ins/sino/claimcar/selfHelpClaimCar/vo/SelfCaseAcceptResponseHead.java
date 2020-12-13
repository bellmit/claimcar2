package ins.sino.claimcar.selfHelpClaimCar.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("HEAD")
public class SelfCaseAcceptResponseHead implements Serializable{

	/**  */
	private static final long serialVersionUID = -6895555819573576510L;
	
	@XStreamAlias("RESPONSETYPE")
	private String responseType; 	//<!-- 返回类型 -->
	
	@XStreamAlias("RESPONSECODE")
	private String errNo;    // 1：成功；其他：失败
	
	@XStreamAlias("RESPONSEMESSAGE")
	private String errMsg;   // 错误信息

	public String getResponseType() {
		return responseType;
	}

	public void setResponseType(String responseType) {
		this.responseType = responseType;
	}

	public String getErrNo() {
		return errNo;
	}

	public void setErrNo(String errNo) {
		this.errNo = errNo;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}


}
