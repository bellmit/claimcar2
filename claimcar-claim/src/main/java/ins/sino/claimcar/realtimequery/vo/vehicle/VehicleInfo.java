package ins.sino.claimcar.realtimequery.vo.vehicle;

import java.io.Serializable;
import java.util.List;

public class VehicleInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String vehicleProperty;  //车辆属性
	private String vehicleType;  //号牌种类代码
	private String carMark;  //号牌号码
	private String engineNo;  //发动机号
	private String vinNo;  //车架号
	private List<DamageInfo> DamageInfo;  //车损信息
	private String vehocleModel;  //车辆型号	
	private String driverName;  //出险驾驶员姓名	
	private String certiType;  //出险驾驶员证件类型	
	private String certiCode;  //出险驾驶员证件号码
	private String driverLicenseNo;  //出险车辆驾驶证号码	
	public String getVehicleProperty() {
		return vehicleProperty;
	}
	public void setVehicleProperty(String vehicleProperty) {
		this.vehicleProperty = vehicleProperty;
	}
	public String getVehicleType() {
		return vehicleType;
	}
	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}
	public String getCarMark() {
		return carMark;
	}
	public void setCarMark(String carMark) {
		this.carMark = carMark;
	}
	public String getEngineNo() {
		return engineNo;
	}
	public void setEngineNo(String engineNo) {
		this.engineNo = engineNo;
	}
	public String getVinNo() {
		return vinNo;
	}
	public void setVinNo(String vinNo) {
		this.vinNo = vinNo;
	}
	public List<DamageInfo> getDamageInfo() {
		return DamageInfo;
	}
	public void setDamageInfo(List<DamageInfo> damageInfo) {
		DamageInfo = damageInfo;
	}
	public String getVehocleModel() {
		return vehocleModel;
	}
	public void setVehocleModel(String vehocleModel) {
		this.vehocleModel = vehocleModel;
	}
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	public String getCertiType() {
		return certiType;
	}
	public void setCertiType(String certiType) {
		this.certiType = certiType;
	}
	public String getCertiCode() {
		return certiCode;
	}
	public void setCertiCode(String certiCode) {
		this.certiCode = certiCode;
	}
	public String getDriverLicenseNo() {
		return driverLicenseNo;
	}
	public void setDriverLicenseNo(String driverLicenseNo) {
		this.driverLicenseNo = driverLicenseNo;
	}
	
	
	
}
