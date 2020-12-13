package ins.sino.claimcar.policyLinkage.vo;

import java.io.Serializable;
import java.util.Date;

public class PolicyLinkResultVo implements Serializable {

	/** 
	 * niuqiang
	 *  */
	private static final long serialVersionUID = 1L;
	
	private String caseId;
	private String respUserName;
	private String respUserPhone;
	private String caseType;
	private Date accidentTime;
	private String driverName;
	private String phone;
	private String hphm;
	private String status;
	private String isResp;
	public String getCaseId() {
		return caseId;
	}
	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}
	public String getRespUserName() {
		return respUserName;
	}
	public void setRespUserName(String respUserName) {
		this.respUserName = respUserName;
	}
	public String getRespUserPhone() {
		return respUserPhone;
	}
	public void setRespUserPhone(String respUserPhone) {
		this.respUserPhone = respUserPhone;
	}
	public String getCaseType() {
		return caseType;
	}
	public void setCaseType(String caseType) {
		this.caseType = caseType;
	}
	public Date getAccidentTime() {
		return accidentTime;
	}
	public void setAccidentTime(Date accidentTime) {
		this.accidentTime = accidentTime;
	}
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getHphm() {
		return hphm;
	}
	public void setHphm(String hphm) {
		this.hphm = hphm;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getIsResp() {
		return isResp;
	}
	public void setIsResp(String isResp) {
		this.isResp = isResp;
	}
	
	

}
