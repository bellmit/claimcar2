package ins.sino.claimcar.policyLinkage.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("dataList")
public class CaseInfosDataListVo implements Serializable{

	/**  */
	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("accidentTime")
	private String accidentTime;
	
	@XStreamAlias("caseNumber")
	private String caseNumber;
	
	@XStreamAlias("caseType")
	private String caseType;
	
	@XStreamAlias("externaltype")
	private String externaltype;
	
	@XStreamAlias("hphm")
	private String hphm;
	
	@XStreamAlias("status")
	private String status;

	public String getAccidentTime() {
		return accidentTime;
	}

	public void setAccidentTime(String accidentTime) {
		this.accidentTime = accidentTime;
	}

	public String getCaseNumber() {
		return caseNumber;
	}

	public void setCaseNumber(String caseNumber) {
		this.caseNumber = caseNumber;
	}

	public String getCaseType() {
		return caseType;
	}

	public void setCaseType(String caseType) {
		this.caseType = caseType;
	}

	public String getExternaltype() {
		return externaltype;
	}

	public void setExternaltype(String externaltype) {
		this.externaltype = externaltype;
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
	
	

}
