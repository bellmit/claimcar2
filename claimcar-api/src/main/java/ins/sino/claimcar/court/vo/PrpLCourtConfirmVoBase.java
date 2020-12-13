package ins.sino.claimcar.court.vo;


import java.io.Serializable;
import java.util.Date;

public class PrpLCourtConfirmVoBase implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String acciNo;
	private String accidentno;
	private String zjhm;
	private String sqr;
	private Date sqsj;
	private String sqzt;
	private Date sfqrkssj;
	private Date sfqrjzsj;
	private String sfqrqk;
	private String sfqrjg;
	private String ah;
	private String slfy;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getAcciNo() {
		return acciNo;
	}
	public void setAcciNo(String acciNo) {
		this.acciNo = acciNo;
	}
	public String getAccidentno() {
		return accidentno;
	}
	public void setAccidentno(String accidentno) {
		this.accidentno = accidentno;
	}
	public String getZjhm() {
		return zjhm;
	}
	public void setZjhm(String zjhm) {
		this.zjhm = zjhm;
	}
	public String getSqr() {
		return sqr;
	}
	public void setSqr(String sqr) {
		this.sqr = sqr;
	}
	public Date getSqsj() {
		return sqsj;
	}
	public void setSqsj(Date sqsj) {
		this.sqsj = sqsj;
	}
	public String getSqzt() {
		return sqzt;
	}
	public void setSqzt(String sqzt) {
		this.sqzt = sqzt;
	}
	public Date getSfqrkssj() {
		return sfqrkssj;
	}
	public void setSfqrkssj(Date sfqrkssj) {
		this.sfqrkssj = sfqrkssj;
	}
	public Date getSfqrjzsj() {
		return sfqrjzsj;
	}
	public void setSfqrjzsj(Date sfqrjzsj) {
		this.sfqrjzsj = sfqrjzsj;
	}
	public String getSfqrqk() {
		return sfqrqk;
	}
	public void setSfqrqk(String sfqrqk) {
		this.sfqrqk = sfqrqk;
	}
	public String getSfqrjg() {
		return sfqrjg;
	}
	public void setSfqrjg(String sfqrjg) {
		this.sfqrjg = sfqrjg;
	}
	public String getAh() {
		return ah;
	}
	public void setAh(String ah) {
		this.ah = ah;
	}
	public String getSlfy() {
		return slfy;
	}
	public void setSlfy(String slfy) {
		this.slfy = slfy;
	}
	
}
