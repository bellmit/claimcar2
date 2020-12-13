package ins.sino.claimcar.court.vo;

import java.io.Serializable;

public class CourtAgentInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	private String acciNo;
	private String personID;
	private String dlrzw;
	private String dlrName;
	private String dlrIDtype;
	private String dlrID;
	private String dlrPhone;
	public String getAcciNo() {
		return acciNo;
	}
	public void setAcciNo(String acciNo) {
		this.acciNo = acciNo;
	}
	public String getPersonID() {
		return personID;
	}
	public void setPersonID(String personID) {
		this.personID = personID;
	}
	public String getDlrzw() {
		return dlrzw;
	}
	public void setDlrzw(String dlrzw) {
		this.dlrzw = dlrzw;
	}
	public String getDlrName() {
		return dlrName;
	}
	public void setDlrName(String dlrName) {
		this.dlrName = dlrName;
	}
	public String getDlrIDtype() {
		return dlrIDtype;
	}
	public void setDlrIDtype(String dlrIDtype) {
		this.dlrIDtype = dlrIDtype;
	}
	public String getDlrID() {
		return dlrID;
	}
	public void setDlrID(String dlrID) {
		this.dlrID = dlrID;
	}
	public String getDlrPhone() {
		return dlrPhone;
	}
	public void setDlrPhone(String dlrPhone) {
		this.dlrPhone = dlrPhone;
	}
	
}
