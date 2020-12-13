package ins.sino.claimcar.carchild.check.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("DRIVER")
public class CTDriverVo implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("CARID")
	private String carId; //理赔车辆ID
	
	@XStreamAlias("DRIVERID")
	private String driverId; //理赔驾驶员ID
	
	@XStreamAlias("DRIVERNAME")
	private String driverName; //驾驶人姓名
	
	@XStreamAlias("GENDER")
	private String gender; //性别
	
	@XStreamAlias("DRIVINGCARTYPE")
	private String drivingCarType; //准驾车型
	
	@XStreamAlias("CERTITYPE")
	private String certiType; //证件类型
	
	@XStreamAlias("IDENTIFYNUMBER")
	private String identifyNumber; //证件号码
	
	@XStreamAlias("DRIVINGLICENSENO")
	private String drivingLicenseNo; //驾驶证号码
	
	@XStreamAlias("PHONENUMBER")
	private String phoneNumber; //电话
	
	@XStreamAlias("LICENSEDATE")
	private String licenseDate; //初次领证日期
	
	@XStreamAlias("AGE")
    private String age; //年龄
	
	@XStreamAlias("IDENTIEFFECTIVEDATE")
    private String identiEffecTiveDate; //驾驶证有效日期

	public String getCarId() {
		return carId;
	}

	public void setCarId(String carId) {
		this.carId = carId;
	}

	public String getDriverId() {
		return driverId;
	}

	public void setDriverId(String driverId) {
		this.driverId = driverId;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getDrivingCarType() {
		return drivingCarType;
	}

	public void setDrivingCarType(String drivingCarType) {
		this.drivingCarType = drivingCarType;
	}

	public String getCertiType() {
		return certiType;
	}

	public void setCertiType(String certiType) {
		this.certiType = certiType;
	}

	public String getIdentifyNumber() {
		return identifyNumber;
	}

	public void setIdentifyNumber(String identifyNumber) {
		this.identifyNumber = identifyNumber;
	}

	public String getDrivingLicenseNo() {
		return drivingLicenseNo;
	}

	public void setDrivingLicenseNo(String drivingLicenseNo) {
		this.drivingLicenseNo = drivingLicenseNo;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getLicenseDate() {
		return licenseDate;
	}

	public void setLicenseDate(String licenseDate) {
		this.licenseDate = licenseDate;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getIdentiEffecTiveDate() {
		return identiEffecTiveDate;
	}

	public void setIdentiEffecTiveDate(String identiEffecTiveDate) {
		this.identiEffecTiveDate = identiEffecTiveDate;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}
}
