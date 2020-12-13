package ins.sino.claimcar.carinterface.vo;

import java.util.Date;

/**
 * Custom VO class of PO ClaimInterfaceLog
 */
public class ClaimInterfaceLogVo extends ClaimInterfaceLogVoBase implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private Date requestTimeStart;

	private Date requestTimeEnd;

	private String userCode;

	public Date getRequestTimeStart() {
		return requestTimeStart;
	}

	public void setRequestTimeStart(Date requestTimeStart) {
		this.requestTimeStart = requestTimeStart;
	}

	public Date getRequestTimeEnd() {
		return requestTimeEnd;
	}

	public void setRequestTimeEnd(Date requestTimeEnd) {
		this.requestTimeEnd = requestTimeEnd;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
}
