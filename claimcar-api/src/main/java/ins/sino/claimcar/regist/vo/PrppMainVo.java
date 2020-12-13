package ins.sino.claimcar.regist.vo;

/**
 * Custom VO class of PO VprppheadId
 */ 
public class PrppMainVo implements java.io.Serializable {
	private static final long serialVersionUID = 1L; 
	
	private String endorseNo;
	private String policyNo;
	private String classCode;
	private String riskCode;
	private String printNo;
	private String language;
	private String policyType;
	private String insuredCode;
	private String insuredName;
	private Long chgAmount;
	public String getEndorseNo() {
		return endorseNo;
	}
	public void setEndorseNo(String endorseNo) {
		this.endorseNo = endorseNo;
	}
	public String getPolicyNo() {
		return policyNo;
	}
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}
	public String getClassCode() {
		return classCode;
	}
	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}
	public String getRiskCode() {
		return riskCode;
	}
	public void setRiskCode(String riskCode) {
		this.riskCode = riskCode;
	}
	public String getPrintNo() {
		return printNo;
	}
	public void setPrintNo(String printNo) {
		this.printNo = printNo;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getPolicyType() {
		return policyType;
	}
	public void setPolicyType(String policyType) {
		this.policyType = policyType;
	}
	public String getInsuredCode() {
		return insuredCode;
	}
	public void setInsuredCode(String insuredCode) {
		this.insuredCode = insuredCode;
	}
	public String getInsuredName() {
		return insuredName;
	}
	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}
	public Long getChgAmount() {
		return chgAmount;
	}
	public void setChgAmount(Long chgAmount) {
		this.chgAmount = chgAmount;
	}
	
	
}
