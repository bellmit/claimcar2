package ins.sino.claimcar.realtimequery.po;


import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@TableGenerator(name = "utiSequence", table = "UTISEQUENCE", pkColumnName = "GenName", valueColumnName = "GenValue", pkColumnValue = "PRPLVEHICLEINFO_PK", allocationSize = 10)
@Table(name = "PRPLVEHICLEINFO")
public class PrpLVehicleInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	private Long id;
	private String vehicleProperty;  //车辆属性
	private String vehicleType;  //号牌种类代码
	private String carMark;  //号牌号码
	private String engineNo;  //发动机号
	private String vinNo;  //车架号
	private List<PrpLDamageInfo> prpLDamageInfo;  //车损信息
	private String vehocleModel;  //车辆型号	
	private String driverName;  //出险驾驶员姓名	
	private String certiType;  //出险驾驶员证件类型	
	private String certiCode;  //出险驾驶员证件号码
	private String driverLicenseNo;  //出险车辆驾驶证号码	
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
	@Column(name = "VEHICLEPROPERTY", nullable = false, length=2)
	public String getVehicleProperty() {
		return vehicleProperty;
	}
	public void setVehicleProperty(String vehicleProperty) {
		this.vehicleProperty = vehicleProperty;
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
	@Column(name = "ENGINENO", nullable = false, length=50)
	public String getEngineNo() {
		return engineNo;
	}
	public void setEngineNo(String engineNo) {
		this.engineNo = engineNo;
	}
	@Column(name = "VINNO", nullable = false, length=50)
	public String getVinNo() {
		return vinNo;
	}
	public void setVinNo(String vinNo) {
		this.vinNo = vinNo;
	}
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="prpLVehicleInfo")
	public List<PrpLDamageInfo> getPrpLDamageInfo() {
		return prpLDamageInfo;
	}
	public void setPrpLDamageInfo(List<PrpLDamageInfo> prpLDamageInfo) {
		this.prpLDamageInfo = prpLDamageInfo;
	}
	
	@Column(name = "VEHOCLEMODEL", nullable = false, length=20)
	public String getVehocleModel() {
		return vehocleModel;
	}
	public void setVehocleModel(String vehocleModel) {
		this.vehocleModel = vehocleModel;
	}
	@Column(name = "DRIVERNAME", nullable = false, length=100)
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	@Column(name = "CERTITYPE", nullable = false, length=2)
	public String getCertiType() {
		return certiType;
	}
	public void setCertiType(String certiType) {
		this.certiType = certiType;
	}
	@Column(name = "CERTICODE", nullable = false, length=30)
	public String getCertiCode() {
		return certiCode;
	}
	public void setCertiCode(String certiCode) {
		this.certiCode = certiCode;
	}
	@Column(name = "DRIVERLICENSENO", nullable = false, length=30)
	public String getDriverLicenseNo() {
		return driverLicenseNo;
	}
	public void setDriverLicenseNo(String driverLicenseNo) {
		this.driverLicenseNo = driverLicenseNo;
	}
	@Column(name = "UPPERID", nullable = false, length=13)
	public Long getUpperId() {
		return upperId;
	}
	public void setUpperId(Long upperId) {
		this.upperId = upperId;
	}
	
	
}
