package ins.sino.claimcar.court.vo;


import java.io.Serializable;
import java.util.Date;

public class PrpLCourtAccidentVoBase implements Serializable {

private static final long serialVersionUID = 1L;
	
	private Long id;
	private String dutyNo;
	private String acciNo;
	private Date reportDate;
	private String acciAddress;
	private String acciResult;
	private String acciDescribe;
	private String acciType;
	private String dsfqk;
	private String jbss;
	private String baryj;
	private String rdnr;
	private String weather;
	private String highSpeed;
	private String dataSource;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDutyNo() {
		return dutyNo;
	}
	public void setDutyNo(String dutyNo) {
		this.dutyNo = dutyNo;
	}
	public String getAcciNo() {
		return acciNo;
	}
	public void setAcciNo(String acciNo) {
		this.acciNo = acciNo;
	}
	public Date getReportDate() {
		return reportDate;
	}
	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}
	public String getAcciAddress() {
		return acciAddress;
	}
	public void setAcciAddress(String acciAddress) {
		this.acciAddress = acciAddress;
	}
	public String getAcciResult() {
		return acciResult;
	}
	public void setAcciResult(String acciResult) {
		this.acciResult = acciResult;
	}
	public String getAcciDescribe() {
		return acciDescribe;
	}
	public void setAcciDescribe(String acciDescribe) {
		this.acciDescribe = acciDescribe;
	}
	public String getAcciType() {
		return acciType;
	}
	public void setAcciType(String acciType) {
		this.acciType = acciType;
	}
	public String getDsfqk() {
		return dsfqk;
	}
	public void setDsfqk(String dsfqk) {
		this.dsfqk = dsfqk;
	}
	public String getJbss() {
		return jbss;
	}
	public void setJbss(String jbss) {
		this.jbss = jbss;
	}
	public String getBaryj() {
		return baryj;
	}
	public void setBaryj(String baryj) {
		this.baryj = baryj;
	}
	public String getRdnr() {
		return rdnr;
	}
	public void setRdnr(String rdnr) {
		this.rdnr = rdnr;
	}
	public String getWeather() {
		return weather;
	}
	public void setWeather(String weather) {
		this.weather = weather;
	}
	public String getHighSpeed() {
		return highSpeed;
	}
	public void setHighSpeed(String highSpeed) {
		this.highSpeed = highSpeed;
	}
	public String getDataSource() {
		return dataSource;
	}
	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}
	
}
