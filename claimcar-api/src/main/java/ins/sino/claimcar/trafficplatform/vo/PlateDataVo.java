package ins.sino.claimcar.trafficplatform.vo;

public class PlateDataVo {
	private String plateType;//车牌类型	Y
    private String plateNo;//车牌号    Y
    private String driverName;//驾驶员   Y
    private String driverPhone;//驾驶员电话   Y
    private String garage;//修理厂   Y
    private String fixedAmount;//定损金额    Y
    private String dutyProportion;//责任占比    Y
    
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
	public String getGarage() {
		return garage;
	}
	public void setGarage(String garage) {
		this.garage = garage;
	}
	public String getFixedAmount() {
		return fixedAmount;
	}
	public void setFixedAmount(String fixedAmount) {
		this.fixedAmount = fixedAmount;
	}
	public String getDutyProportion() {
		return dutyProportion;
	}
	public void setDutyProportion(String dutyProportion) {
		this.dutyProportion = dutyProportion;
	}
    
}
