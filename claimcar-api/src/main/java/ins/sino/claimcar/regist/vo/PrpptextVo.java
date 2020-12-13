package ins.sino.claimcar.regist.vo;

/**
 * Custom VO class of PO VprppheadId
 */ 
public class PrpptextVo implements java.io.Serializable {
	private static final long serialVersionUID = 1L; 
	
	private String endorseNo;
	private String policyNo;
	private String lineNo;
	private String endorseText;
	private String flag;
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
	public String getLineNo() {
		return lineNo;
	}
	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}
	public String getEndorseText() {
		return endorseText;
	}
	public void setEndorseText(String endorseText) {
		this.endorseText = endorseText;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	
	
}
