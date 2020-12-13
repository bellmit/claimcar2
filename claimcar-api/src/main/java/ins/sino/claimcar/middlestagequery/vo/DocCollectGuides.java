package ins.sino.claimcar.middlestagequery.vo;

public class DocCollectGuides implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	private String serialNo;   //序号
	private String registNo;   //报案号
	private String policyNo;   //保单号
	private String riskCode;   //险种代码
	private String docCode;    //文件代码
	private String docName;    //文件名称
	public String getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	public String getRegistNo() {
		return registNo;
	}
	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}
	public String getPolicyNo() {
		return policyNo;
	}
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}
	public String getRiskCode() {
		return riskCode;
	}
	public void setRiskCode(String riskCode) {
		this.riskCode = riskCode;
	}
	public String getDocCode() {
		return docCode;
	}
	public void setDocCode(String docCode) {
		this.docCode = docCode;
	}
	public String getDocName() {
		return docName;
	}
	public void setDocName(String docName) {
		this.docName = docName;
	}
	
	
}
