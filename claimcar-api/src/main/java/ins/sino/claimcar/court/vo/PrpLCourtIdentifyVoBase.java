package ins.sino.claimcar.court.vo;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class PrpLCourtIdentifyVoBase implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String appraisalno;
	private String accidentno;
	private String acciNo;
	private String applicantname;
	private Date applicantdate;
	private String appraisal;
	private Date rexeptiontime;
	private BigDecimal appraisalsum;
	private Date appraisaldate;
	private String appraisaladdr;
	private String appraiser;
	private String bappraiser;
	private String appraisalproj;
	private String appraisaltype;
	private String presence;
	private String casesummary;
	private String medicalsummary;
	private String analyexplain;
	private String appraisalopinion;
	private String scdj;
	private String appraisalrep;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getAppraisalno() {
		return appraisalno;
	}
	public void setAppraisalno(String appraisalno) {
		this.appraisalno = appraisalno;
	}
	public String getAccidentno() {
		return accidentno;
	}
	public void setAccidentno(String accidentno) {
		this.accidentno = accidentno;
	}
	public String getAcciNo() {
		return acciNo;
	}
	public void setAcciNo(String acciNo) {
		this.acciNo = acciNo;
	}
	public String getApplicantname() {
		return applicantname;
	}
	public void setApplicantname(String applicantname) {
		this.applicantname = applicantname;
	}
	public Date getApplicantdate() {
		return applicantdate;
	}
	public void setApplicantdate(Date applicantdate) {
		this.applicantdate = applicantdate;
	}
	public String getAppraisal() {
		return appraisal;
	}
	public void setAppraisal(String appraisal) {
		this.appraisal = appraisal;
	}
	public Date getRexeptiontime() {
		return rexeptiontime;
	}
	public void setRexeptiontime(Date rexeptiontime) {
		this.rexeptiontime = rexeptiontime;
	}
	public BigDecimal getAppraisalsum() {
		return appraisalsum;
	}
	public void setAppraisalsum(BigDecimal appraisalsum) {
		this.appraisalsum = appraisalsum;
	}
	public Date getAppraisaldate() {
		return appraisaldate;
	}
	public void setAppraisaldate(Date appraisaldate) {
		this.appraisaldate = appraisaldate;
	}
	public String getAppraisaladdr() {
		return appraisaladdr;
	}
	public void setAppraisaladdr(String appraisaladdr) {
		this.appraisaladdr = appraisaladdr;
	}
	public String getAppraiser() {
		return appraiser;
	}
	public void setAppraiser(String appraiser) {
		this.appraiser = appraiser;
	}
	public String getBappraiser() {
		return bappraiser;
	}
	public void setBappraiser(String bappraiser) {
		this.bappraiser = bappraiser;
	}
	public String getAppraisalproj() {
		return appraisalproj;
	}
	public void setAppraisalproj(String appraisalproj) {
		this.appraisalproj = appraisalproj;
	}
	public String getAppraisaltype() {
		return appraisaltype;
	}
	public void setAppraisaltype(String appraisaltype) {
		this.appraisaltype = appraisaltype;
	}
	public String getPresence() {
		return presence;
	}
	public void setPresence(String presence) {
		this.presence = presence;
	}
	public String getCasesummary() {
		return casesummary;
	}
	public void setCasesummary(String casesummary) {
		this.casesummary = casesummary;
	}
	public String getMedicalsummary() {
		return medicalsummary;
	}
	public void setMedicalsummary(String medicalsummary) {
		this.medicalsummary = medicalsummary;
	}
	public String getAnalyexplain() {
		return analyexplain;
	}
	public void setAnalyexplain(String analyexplain) {
		this.analyexplain = analyexplain;
	}
	public String getAppraisalopinion() {
		return appraisalopinion;
	}
	public void setAppraisalopinion(String appraisalopinion) {
		this.appraisalopinion = appraisalopinion;
	}
	public String getScdj() {
		return scdj;
	}
	public void setScdj(String scdj) {
		this.scdj = scdj;
	}
	public String getAppraisalrep() {
		return appraisalrep;
	}
	public void setAppraisalrep(String appraisalrep) {
		this.appraisalrep = appraisalrep;
	}
	
}
