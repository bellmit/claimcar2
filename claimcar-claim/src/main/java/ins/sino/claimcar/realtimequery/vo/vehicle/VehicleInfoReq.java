package ins.sino.claimcar.realtimequery.vo.vehicle;

import java.io.Serializable;

public class VehicleInfoReq implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userCode;   //用户代码
	private String passWord;   //用户密码
	private String vinNo;   //车架号
	private String carMark;  //号牌号码
	private String vehicleType;  //号码种类
	private String reportNo;  //报案号
	private String claimcomPany;  //公司代码
	private String areaCode;  //地区代码
	private String insurerUuid;  //流水号
	private String reportPhoneNo;  //报案电话号码
	private String driverLicenseNo;
	private String certiCode;
	private String certiType;
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public String getPassWord() {
		return passWord;
	}
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	public String getVinNo() {
		return vinNo;
	}
	public void setVinNo(String vinNo) {
		this.vinNo = vinNo;
	}
	public String getCarMark() {
		return carMark;
	}
	public void setCarMark(String carMark) {
		this.carMark = carMark;
	}
	public String getVehicleType() {
		return vehicleType;
	}
	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}
	public String getReportNo() {
		return reportNo;
	}
	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}
	public String getClaimcomPany() {
		return claimcomPany;
	}
	public void setClaimcomPany(String claimcomPany) {
		this.claimcomPany = claimcomPany;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public String getInsurerUuid() {
		return insurerUuid;
	}
	public void setInsurerUuid(String insurerUuid) {
		this.insurerUuid = insurerUuid;
	}
	public String getReportPhoneNo() {
		return reportPhoneNo;
	}
	public void setReportPhoneNo(String reportPhoneNo) {
		this.reportPhoneNo = reportPhoneNo;
	}
	public String getDriverLicenseNo() {
		return driverLicenseNo;
	}
	public void setDriverLicenseNo(String driverLicenseNo) {
		this.driverLicenseNo = driverLicenseNo;
	}
	public String getCertiCode() {
		return certiCode;
	}
	public void setCertiCode(String certiCode) {
		this.certiCode = certiCode;
	}
	public String getCertiType() {
		return certiType;
	}
	public void setCertiType(String certiType) {
		this.certiType = certiType;
	}
	
	
}
