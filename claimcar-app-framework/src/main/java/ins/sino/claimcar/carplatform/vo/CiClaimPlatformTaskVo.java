package ins.sino.claimcar.carplatform.vo;

import java.util.Date;

public class CiClaimPlatformTaskVo extends CiClaimPlatformTaskVoBase implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private String validNo;
	private String comCode;
	private Date requestTimeStart;// 操作开始日期
	private Date requestTimeEnd;// 操作日期
	private String userCode;
	
	public String getValidNo() {
		return validNo;
	}
	
	public void setValidNo(String validNo) {
		this.validNo = validNo;
	}

	
	public String getComCode() {
		return comCode;
	}

	
	public void setComCode(String comCode) {
		this.comCode = comCode;
	}

	
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
