package ins.sino.claimcar.court.po;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@TableGenerator(name = "utiSequence", table = "UTISEQUENCE", pkColumnName = "GenName", valueColumnName = "GenValue", pkColumnValue = "PK_PRPLCOURTACCIDENT", allocationSize = 10)
@Table(name = "PRPLCOURTACCIDENT")
public class PrpLCourtAccident implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private PrpLCourtMessage prpLCourtMessage;
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
	
	@Id
	@Column(name = "ID", unique = true, nullable = false, precision=12, scale=0)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@OneToOne(fetch=FetchType.LAZY)
	@PrimaryKeyJoinColumn
	public PrpLCourtMessage getPrpLCourtMessage() {
		return prpLCourtMessage;
	}
	public void setPrpLCourtMessage(PrpLCourtMessage prpLCourtMessage) {
		this.prpLCourtMessage = prpLCourtMessage;
	}
	@Column(name = "DUTYNO")
	public String getDutyNo() {
		return dutyNo;
	}
	public void setDutyNo(String dutyNo) {
		this.dutyNo = dutyNo;
	}
	@Column(name = "ACCINO")
	public String getAcciNo() {
		return acciNo;
	}
	public void setAcciNo(String acciNo) {
		this.acciNo = acciNo;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "REPORTDATE", nullable = false, length=7)
	public Date getReportDate() {
		return reportDate;
	}
	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}
	@Column(name = "ACCIADDRESS")
	public String getAcciAddress() {
		return acciAddress;
	}
	public void setAcciAddress(String acciAddress) {
		this.acciAddress = acciAddress;
	}
	@Column(name = "ACCIRESULT")
	public String getAcciResult() {
		return acciResult;
	}
	public void setAcciResult(String acciResult) {
		this.acciResult = acciResult;
	}
	@Column(name = "ACCIDESCRIBE")
	public String getAcciDescribe() {
		return acciDescribe;
	}
	public void setAcciDescribe(String acciDescribe) {
		this.acciDescribe = acciDescribe;
	}
	@Column(name = "ACCITYPE")
	public String getAcciType() {
		return acciType;
	}
	public void setAcciType(String acciType) {
		this.acciType = acciType;
	}
	@Column(name = "DSFQK")
	public String getDsfqk() {
		return dsfqk;
	}
	public void setDsfqk(String dsfqk) {
		this.dsfqk = dsfqk;
	}
	@Column(name = "JBSS")
	public String getJbss() {
		return jbss;
	}
	public void setJbss(String jbss) {
		this.jbss = jbss;
	}
	@Column(name = "BARYJ")
	public String getBaryj() {
		return baryj;
	}
	public void setBaryj(String baryj) {
		this.baryj = baryj;
	}
	@Column(name = "RDNR")
	public String getRdnr() {
		return rdnr;
	}
	public void setRdnr(String rdnr) {
		this.rdnr = rdnr;
	}
	@Column(name = "WEATHER")
	public String getWeather() {
		return weather;
	}
	public void setWeather(String weather) {
		this.weather = weather;
	}
	@Column(name = "HIGHSPEED")
	public String getHighSpeed() {
		return highSpeed;
	}
	public void setHighSpeed(String highSpeed) {
		this.highSpeed = highSpeed;
	}
	@Column(name = "DATASOURCE")
	public String getDataSource() {
		return dataSource;
	}
	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}
	
	
}
