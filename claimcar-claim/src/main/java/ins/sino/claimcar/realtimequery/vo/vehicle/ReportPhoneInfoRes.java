package ins.sino.claimcar.realtimequery.vo.vehicle;

import java.io.Serializable;

public class ReportPhoneInfoRes implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String retCode;  //提交响应码	
	private String retMessage;  //响应码描述	
	private String insurerUuid;  //流水号
	private ReportPhoneInfoResBody data;
	public String getRetCode() {
		return retCode;
	}
	public void setRetCode(String retCode) {
		this.retCode = retCode;
	}
	public String getRetMessage() {
		return retMessage;
	}
	public void setRetMessage(String retMessage) {
		this.retMessage = retMessage;
	}
	public String getInsurerUuid() {
		return insurerUuid;
	}
	public void setInsurerUuid(String insurerUuid) {
		this.insurerUuid = insurerUuid;
	}
	public ReportPhoneInfoResBody getData() {
		return data;
	}
	public void setData(ReportPhoneInfoResBody data) {
		this.data = data;
	}
	
	
	
}
