package ins.sino.claimcar.court.vo;

import java.io.Serializable;

public class CourtFileInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	private String id;
	private String accidentNo;
	private String caseNo;
	private String wjmc;
	private String scsj;
	private String wjurl;
	private String wjtype;
	private String dsrNo;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAccidentNo() {
		return accidentNo;
	}
	public void setAccidentNo(String accidentNo) {
		this.accidentNo = accidentNo;
	}
	public String getCaseNo() {
		return caseNo;
	}
	public void setCaseNo(String caseNo) {
		this.caseNo = caseNo;
	}
	public String getWjmc() {
		return wjmc;
	}
	public void setWjmc(String wjmc) {
		this.wjmc = wjmc;
	}
	public String getScsj() {
		return scsj;
	}
	public void setScsj(String scsj) {
		this.scsj = scsj;
	}
	public String getWjurl() {
		return wjurl;
	}
	public void setWjurl(String wjurl) {
		this.wjurl = wjurl;
	}
	public String getWjtype() {
		return wjtype;
	}
	public void setWjtype(String wjtype) {
		this.wjtype = wjtype;
	}
	public String getDsrNo() {
		return dsrNo;
	}
	public void setDsrNo(String dsrNo) {
		this.dsrNo = dsrNo;
	}
	
	
}
