package ins.sino.claim.powerGridCarClaimLog.vo;

import java.util.Date;

public class GDPowerGridCarClaimLogVo implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private Date sendTime;
	private String sendJson;
	private String responseJson;
	private Date returnTime;
	private String responseCode;
	private String errorMessage;
	private Long cost;
	private String reqtypeCode;
	private String reqtypeName;
	
	public GDPowerGridCarClaimLogVo() {

	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public Date getSendTime() {
		return sendTime;
	}
	
	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}
	
	public String getSendJson() {
		return sendJson;
	}
	
	public void setSendJson(String sendJson) {
		this.sendJson = sendJson;
	}
	
	public String getResponseJson() {
		return responseJson;
	}
	
	public void setResponseJson(String responseJson) {
		this.responseJson = responseJson;
	}
	
	public Date getReturnTime() {
		return returnTime;
	}
	
	public void setReturnTime(Date returnTime) {
		this.returnTime = returnTime;
	}
	
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
	
	public Long getCost() {
		return cost;
	}
	
	public void setCost(Long cost) {
		this.cost = cost;
	}
	
	public String getReqtypeCode() {
		return reqtypeCode;
	}
	
	public void setReqtypeCode(String reqtypeCode) {
		this.reqtypeCode = reqtypeCode;
	}
	
	public String getReqtypeName() {
		return reqtypeName;
	}
	
	public void setReqtypeName(String reqtypeName) {
		this.reqtypeName = reqtypeName;
	}
	
}
