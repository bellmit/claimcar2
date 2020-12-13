package ins.sino.claimcar.court.vo;

import java.io.Serializable;

public class SeniorCourtQueryResVo implements Serializable {

	private static final long serialVersionUID = 1L;
	private CourtCaseInfo caseinfo;
	private CourtBasicInfo basicinfo;
	private String retCode;
	private String retMessage;
	private String jkptUuid;
	
	
	public CourtCaseInfo getCaseinfo() {
		return caseinfo;
	}
	public void setCaseinfo(CourtCaseInfo caseinfo) {
		this.caseinfo = caseinfo;
	}
	public CourtBasicInfo getBasicinfo() {
		return basicinfo;
	}
	public void setBasicinfo(CourtBasicInfo basicinfo) {
		this.basicinfo = basicinfo;
	}
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
	public String getJkptUuid() {
		return jkptUuid;
	}
	public void setJkptUuid(String jkptUuid) {
		this.jkptUuid = jkptUuid;
	}
	
	
	
}
