package ins.sino.claimcar.carchildren.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("HEAD")
public class CarchildResHeadVo implements Serializable{
	  private static final long serialVersionUID = 1L;
	  @XStreamAlias("RESPONSETYPE")
	  private String responsetype;
	  @XStreamAlias("ERRNO")
	  private String errNo;
	  @XStreamAlias("ERRMSG")
	  private String errMsg;
	public String getResponsetype() {
		return responsetype;
	}
	public void setResponsetype(String responsetype) {
		this.responsetype = responsetype;
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
