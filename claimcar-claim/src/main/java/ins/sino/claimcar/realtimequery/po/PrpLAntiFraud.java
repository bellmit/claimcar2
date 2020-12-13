package ins.sino.claimcar.realtimequery.po;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@TableGenerator(name = "utiSequence", table = "UTISEQUENCE", pkColumnName = "GenName", valueColumnName = "GenValue", pkColumnValue = "PRPLANTIFRAUD_PK", allocationSize = 10)
@Table(name = "PRPLANTIFRAUD")
public class PrpLAntiFraud implements Serializable{
	private static final long serialVersionUID = 1L;
	private Long id;
	private String reportNo;  //报案号
	private String vinNo;  //车架号
	private String vehicleType;  //号码种类
	private String carMark;  //号牌号码
	private String riskType;  //风险类型代码
	private String riskSource;  //风险信息来源
	private Date Time;  //风险信息入库时间
	private String Name;  //人员姓名	
	private String certiType;  //证件类型	
	private String certiCode;  //证件号码	
	private String reportPhoneNo;  //电话号码	
	private Long upperId;  //维护一个父级关系表id
	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "utiSequence")
	@Column(name = "ID", unique = true, nullable = false, precision=13, scale=0)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Column(name = "REPORTNO", nullable = false, length=25)
	public String getReportNo() {
		return reportNo;
	}
	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}
	@Column(name = "VINNO", nullable = false, length=50)
	public String getVinNo() {
		return vinNo;
	}
	public void setVinNo(String vinNo) {
		this.vinNo = vinNo;
	}
	@Column(name = "VEHICLETYPE", nullable = false, length=2)
	public String getVehicleType() {
		return vehicleType;
	}
	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}
	@Column(name = "CARMARK", nullable = false, length=50)
	public String getCarMark() {
		return carMark;
	}
	public void setCarMark(String carMark) {
		this.carMark = carMark;
	}
	@Column(name = "RISKTYPE", nullable = false, length=2)
	public String getRiskType() {
		return riskType;
	}
	public void setRiskType(String riskType) {
		this.riskType = riskType;
	}
	@Column(name = "RISKSOURCE", nullable = false, length=2)
	public String getRiskSource() {
		return riskSource;
	}
	public void setRiskSource(String riskSource) {
		this.riskSource = riskSource;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TIME", length=20)
	public Date getTime() {
		return Time;
	}
	public void setTime(Date time) {
		Time = time;
	}
	@Column(name = "NAME", nullable = false, length=50)
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	@Column(name = "CERTITYPE", nullable = false, length=2)
	public String getCertiType() {
		return certiType;
	}
	public void setCertiType(String certiType) {
		this.certiType = certiType;
	}
	@Column(name = "CERTICODE", nullable = false, length=50)
	public String getCertiCode() {
		return certiCode;
	}
	public void setCertiCode(String certiCode) {
		this.certiCode = certiCode;
	}
	@Column(name = "REPORTPHONENO", nullable = false, length=11)
	public String getReportPhoneNo() {
		return reportPhoneNo;
	}
	public void setReportPhoneNo(String reportPhoneNo) {
		this.reportPhoneNo = reportPhoneNo;
	}
	@Column(name = "UPPERID", nullable = false, length=10)
	public Long getUpperId() {
		return upperId;
	}
	public void setUpperId(Long upperId) {
		this.upperId = upperId;
	}
	
}
