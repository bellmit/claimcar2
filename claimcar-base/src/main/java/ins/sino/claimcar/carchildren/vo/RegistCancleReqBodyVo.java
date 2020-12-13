package ins.sino.claimcar.carchildren.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("BODY")
public class RegistCancleReqBodyVo implements Serializable{
	 private static final long serialVersionUID = 1L;
	@XStreamAlias("REGISTNO")
	private String registNo;
	@XStreamAlias("USERCODE")
	private String usercode;
	@XStreamAlias("CANCELDATE")
	private String cancelDate;
	@XStreamAlias("REASON")
	private String reason;
	@XStreamAlias("REMARK")
	private String remark;
	@XStreamAlias("TIMESTAMP")
	private String timestamp;
	public String getRegistNo() {
		return registNo;
	}
	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}
	public String getUsercode() {
		return usercode;
	}
	public void setUsercode(String usercode) {
		this.usercode = usercode;
	}
	public String getCancelDate() {
		return cancelDate;
	}
	public void setCancelDate(String cancelDate) {
		this.cancelDate = cancelDate;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
	
	
	

}
