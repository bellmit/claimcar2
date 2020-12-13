package ins.sino.claimcar.court.vo;

public class CourtBasicInfo implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	private String companyCode;	//保险公司代码；详见保险公司机构代码
	private String areaCode;     //地区代码
	private String caseNo;       //报案号
	
	
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
	
	
}
