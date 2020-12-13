package ins.sino.claimcar.trafficplatform.vo;

public class PlateData implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	private Long id;
	private String vinNo;//车架号
	private String plateType;//车牌类型
	private String plateNo;//车牌号
	private String driverName;//驾驶人姓名
	private String driverPhone;//驾驶人电话
	private String dutyProportion;//责任占比
	private String isNorDriving;//车辆是否能够行驶
	private String liabilityInsuranceName;//交强险承保公司名称
	private String liabilityInsuranceNo;//交强险保单号
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getVinNo() {
		return vinNo;
	}
	public void setVinNo(String vinNo) {
		this.vinNo = vinNo;
	}
	public String getPlateType() {
		return plateType;
	}
	public void setPlateType(String plateType) {
		this.plateType = plateType;
	}
	public String getPlateNo() {
		return plateNo;
	}
	public void setPlateNo(String plateNo) {
		this.plateNo = plateNo;
	}
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	public String getDriverPhone() {
		return driverPhone;
	}
	public void setDriverPhone(String driverPhone) {
		this.driverPhone = driverPhone;
	}
	public String getDutyProportion() {
		return dutyProportion;
	}
	public void setDutyProportion(String dutyProportion) {
		this.dutyProportion = dutyProportion;
	}
	public String getIsNorDriving() {
		return isNorDriving;
	}
	public void setIsNorDriving(String isNorDriving) {
		this.isNorDriving = isNorDriving;
	}
	public String getLiabilityInsuranceName() {
		return liabilityInsuranceName;
	}
	public void setLiabilityInsuranceName(String liabilityInsuranceName) {
		this.liabilityInsuranceName = liabilityInsuranceName;
	}
	public String getLiabilityInsuranceNo() {
		return liabilityInsuranceNo;
	}
	public void setLiabilityInsuranceNo(String liabilityInsuranceNo) {
		this.liabilityInsuranceNo = liabilityInsuranceNo;
	}
	
	
}
