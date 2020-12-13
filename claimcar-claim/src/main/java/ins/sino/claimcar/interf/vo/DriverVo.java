package ins.sino.claimcar.interf.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Driver")
public class DriverVo {
	/*驾驶员单位或家庭地址*/
	@XStreamAlias("Address")
	private String address;
	/*年龄*/
	@XStreamAlias("DriverAge")
	private String driverAge;
	/*驾照类型*/
	@XStreamAlias("DriverAllowedVehicleType")
	private String driverAllowedVehicleType;
	/*教育程度*/
	@XStreamAlias("DriverEducation")
	private String driverEducation;
	/*性别*/
	@XStreamAlias("DriverGender")
	private String driverGender;
	/*颁证机关*/
	@XStreamAlias("DriverLicenseInstitution")
	private String driverLicenseInstitution;
	/*联系电话*/
	@XStreamAlias("DriverTel")
	private String driverTel;
	/*初次领证日期*/
	@XStreamAlias("DrivingLicenseDate")
	private String drivingLicenseDate;
	/*驾驶证号码*/
	@XStreamAlias("DrivingLicenseNum")
	private String drivingLicenseNum;
	/*驾驶员姓名*/
	@XStreamAlias("Name")
	private String name;
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getDriverAge() {
		return driverAge;
	}
	public void setDriverAge(String driverAge) {
		this.driverAge = driverAge;
	}
	public String getDriverAllowedVehicleType() {
		return driverAllowedVehicleType;
	}
	public void setDriverAllowedVehicleType(String driverAllowedVehicleType) {
		this.driverAllowedVehicleType = driverAllowedVehicleType;
	}
	public String getDriverEducation() {
		return driverEducation;
	}
	public void setDriverEducation(String driverEducation) {
		this.driverEducation = driverEducation;
	}
	public String getDriverGender() {
		return driverGender;
	}
	public void setDriverGender(String driverGender) {
		this.driverGender = driverGender;
	}
	public String getDriverLicenseInstitution() {
		return driverLicenseInstitution;
	}
	public void setDriverLicenseInstitution(String driverLicenseInstitution) {
		this.driverLicenseInstitution = driverLicenseInstitution;
	}
	public String getDriverTel() {
		return driverTel;
	}
	public void setDriverTel(String driverTel) {
		this.driverTel = driverTel;
	}
	public String getDrivingLicenseDate() {
		return drivingLicenseDate;
	}
	public void setDrivingLicenseDate(String drivingLicenseDate) {
		this.drivingLicenseDate = drivingLicenseDate;
	}
	public String getDrivingLicenseNum() {
		return drivingLicenseNum;
	}
	public void setDrivingLicenseNum(String drivingLicenseNum) {
		this.drivingLicenseNum = drivingLicenseNum;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	

}
