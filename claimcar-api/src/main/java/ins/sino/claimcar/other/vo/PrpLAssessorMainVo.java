package ins.sino.claimcar.other.vo;

/**
 * Custom VO class of PO PrpLAssessorMain
 */ 
public class PrpLAssessorMainVo extends PrpLAssessorMainVoBase implements java.io.Serializable {
	private static final long serialVersionUID = 1L; 
	
	private String comCode;
	private Long intermId;
	private String intermNameDetail;
	
	public String getComCode() {
		return comCode;
	}

	public void setComCode(String comCode) {
		this.comCode = comCode;
	}

	public Long getIntermId() {
		return intermId;
	}

	public void setIntermId(Long intermId) {
		this.intermId = intermId;
	}

	public String getIntermNameDetail() {
		return intermNameDetail;
	}

	public void setIntermNameDetail(String intermNameDetail) {
		this.intermNameDetail = intermNameDetail;
	}
	
	
	
}
