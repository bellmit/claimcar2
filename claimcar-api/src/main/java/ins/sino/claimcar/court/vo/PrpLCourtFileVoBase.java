package ins.sino.claimcar.court.vo;


import java.io.Serializable;
import java.util.Date;

public class PrpLCourtFileVoBase implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String fileid;
	private String accidentno;
	private String caseno;
	private String wjmc;
	private Date scsj;
	private String wjurl;
	private String wjtype;
	private String dsrno;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFileid() {
		return fileid;
	}
	public void setFileid(String fileid) {
		this.fileid = fileid;
	}
	public String getAccidentno() {
		return accidentno;
	}
	public void setAccidentno(String accidentno) {
		this.accidentno = accidentno;
	}
	public String getCaseno() {
		return caseno;
	}
	public void setCaseno(String caseno) {
		this.caseno = caseno;
	}
	public String getWjmc() {
		return wjmc;
	}
	public void setWjmc(String wjmc) {
		this.wjmc = wjmc;
	}
	public Date getScsj() {
		return scsj;
	}
	public void setScsj(Date scsj) {
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
	public String getDsrno() {
		return dsrno;
	}
	public void setDsrno(String dsrno) {
		this.dsrno = dsrno;
	}
	
}
