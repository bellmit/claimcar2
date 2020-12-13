package ins.sino.claimcar.claim.vo;

import java.util.Date;

/**
 * Custom VO class of PO Smsmainhis
 */ 
public class SmsmainhisVo extends SmsmainhisVoBase implements java.io.Serializable {
	private static final long serialVersionUID = 1L; 
	private Date sendTimeStart;
	private Date sendTimeEnd;
	public Date getSendTimeStart() {
		return sendTimeStart;
	}
	public void setSendTimeStart(Date sendTimeStart) {
		this.sendTimeStart = sendTimeStart;
	}
	public Date getSendTimeEnd() {
		return sendTimeEnd;
	}
	public void setSendTimeEnd(Date sendTimeEnd) {
		this.sendTimeEnd = sendTimeEnd;
	}
	
	
}
