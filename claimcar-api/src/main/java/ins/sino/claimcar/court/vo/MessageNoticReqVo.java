package ins.sino.claimcar.court.vo;

import java.io.Serializable;

public class MessageNoticReqVo implements Serializable {

	private static final long serialVersionUID = 1L;
	private String companyCode;
	private String areaCode;
	private String caseNo;
	private String searchCode;
	private String dutyNo;
	private String acciNo;
	private String reportDate;
	private String acciAddress;
	private String acciResult;
	private String acciType;
	private String jkptUuid;
	private String userName;
	private String passWord;
	
	public String getCompanyCode() {
		return companyCode;
	}
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public String getCaseNo() {
		return caseNo;
	}
	public void setCaseNo(String caseNo) {
		this.caseNo = caseNo;
	}
	public String getSearchCode() {
		return searchCode;
	}
	public void setSearchCode(String searchCode) {
		this.searchCode = searchCode;
	}
	public String getDutyNo() {
		return dutyNo;
	}
	public void setDutyNo(String dutyNo) {
		this.dutyNo = dutyNo;
	}
	public String getAcciNo() {
		return acciNo;
	}
	public void setAcciNo(String acciNo) {
		this.acciNo = acciNo;
	}
	public String getReportDate() {
		return reportDate;
	}
	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}
	public String getAcciAddress() {
		return acciAddress;
	}
	public void setAcciAddress(String acciAddress) {
		this.acciAddress = acciAddress;
	}
	public String getAcciResult() {
		return acciResult;
	}
	public void setAcciResult(String acciResult) {
		this.acciResult = acciResult;
	}
	public String getAcciType() {
		return acciType;
	}
	public void setAcciType(String acciType) {
		this.acciType = acciType;
	}
	public String getJkptUuid() {
		return jkptUuid;
	}
	public void setJkptUuid(String jkptUuid) {
		this.jkptUuid = jkptUuid;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassWord() {
		return passWord;
	}
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	
}
