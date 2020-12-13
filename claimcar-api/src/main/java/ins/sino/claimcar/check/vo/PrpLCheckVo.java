package ins.sino.claimcar.check.vo;

/**
 * Custom VO class of PO PrpLCheck
 */ 
public class PrpLCheckVo extends PrpLCheckVoBase implements java.io.Serializable {

	private static final long serialVersionUID = 1L; 
	private String accidentReason; //事故原因  送上海平台
	
	public String getAccidentReason() {
		return accidentReason;
	}
	public void setAccidentReason(String accidentReason) {
		this.accidentReason = accidentReason;
	}
}
