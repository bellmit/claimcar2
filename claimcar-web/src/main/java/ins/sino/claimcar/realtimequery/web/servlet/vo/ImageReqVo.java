package ins.sino.claimcar.realtimequery.web.servlet.vo;

import java.io.Serializable;

public class ImageReqVo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String claimCode;
	private String claimCompany;
	private String pituresNo;
	private String insurerUuid;
	public String getClaimCode() {
		return claimCode;
	}
	public void setClaimCode(String claimCode) {
		this.claimCode = claimCode;
	}
	public String getClaimCompany() {
		return claimCompany;
	}
	public void setClaimCompany(String claimCompany) {
		this.claimCompany = claimCompany;
	}
	public String getPituresNo() {
		return pituresNo;
	}
	public void setPituresNo(String pituresNo) {
		this.pituresNo = pituresNo;
	}
	public String getInsurerUuid() {
		return insurerUuid;
	}
	public void setInsurerUuid(String insurerUuid) {
		this.insurerUuid = insurerUuid;
	}
	
	
	
}
