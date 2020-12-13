package ins.sino.claimcar.court.vo;

public class SeniorCourtQueryReqVo implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	private String companyCode;		//保险公司代码
	private String areaCode;       //地区代码
	private String caseNo;         //报案号
	private String searchCode;     //查询码
	private String userName;    //用户名
	private String passWord;    //密码
	private String requestType;    //请求类型：02-高院信息查询接口
	private String jkptUuid;     //流水号
	
	
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
	public String getRequestType() {
		return requestType;
	}
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
	public String getJkptUuid() {
		return jkptUuid;
	}
	public void setJkptUuid(String jkptUuid) {
		this.jkptUuid = jkptUuid;
	}
	
	
}
