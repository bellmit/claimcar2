package ins.sino.claimcar.claim.vo;

import java.util.Date;

/**
 * Custom VO class of PO PrpsmsMessage
 */ 
public class PrpsmsMessageVo extends PrpsmsMessageVoBase implements java.io.Serializable {
	private static final long serialVersionUID = 1L; 
	private Date truesendTimeStart;
	private Date truesendTimeEnd;
	public Date getTruesendTimeStart() {
		return truesendTimeStart;
	}
	public void setTruesendTimeStart(Date truesendTimeStart) {
		this.truesendTimeStart = truesendTimeStart;
	}
	public Date getTruesendTimeEnd() {
		return truesendTimeEnd;
	}
	public void setTruesendTimeEnd(Date truesendTimeEnd) {
		this.truesendTimeEnd = truesendTimeEnd;
	}
	
	
}
