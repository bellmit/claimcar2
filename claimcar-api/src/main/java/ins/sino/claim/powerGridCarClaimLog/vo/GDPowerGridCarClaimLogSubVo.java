package ins.sino.claim.powerGridCarClaimLog.vo;

public class GDPowerGridCarClaimLogSubVo implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private String gdClaimLogId;
	private String registNo;
	private String claimNo;
	private String caseNo;
	
	public GDPowerGridCarClaimLogSubVo() {

	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getGdClaimLogId() {
		return gdClaimLogId;
	}
	
	public void setGdClaimLogId(String gdClaimLogId) {
		this.gdClaimLogId = gdClaimLogId;
	}
	
	public String getRegistNo() {
		return registNo;
	}
	
	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}
	
	public String getClaimNo() {
		return claimNo;
	}
	
	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}
	
	public String getCaseNo() {
		return caseNo;
	}
	
	public void setCaseNo(String caseNo) {
		this.caseNo = caseNo;
	}
	
}
