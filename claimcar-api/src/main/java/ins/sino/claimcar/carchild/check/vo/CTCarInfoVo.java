package ins.sino.claimcar.carchild.check.vo;


import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("CARINFO")
public class CTCarInfoVo implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("IFOBJECT")
	private String ifObject; //类别
	
	@XStreamAlias("CARID")
	private String carId; //理赔车辆ID
	
	@XStreamAlias("SERIALNO")
	private String serialNo; //序号
	
	@XStreamAlias("LICENSENO")
	private String licenseNo; //车牌号
	
	@XStreamAlias("LICENSETYPE")
	private String licenseType; //车牌种类
	
	@XStreamAlias("ENGINENO")
	private String engineNo; //发动机号
	
	@XStreamAlias("FRAMENO")
	private String frameNo; //车架号
	
	@XStreamAlias("VIN")
	private String vin; //VIN码
	
	@XStreamAlias("INSURCOMCODE")
	private String insurComcode; //承保公司
	
	@XStreamAlias("REGISTEDATE")
	private String registeDate; //初次登记日期
	
	@XStreamAlias("OWERN")
	private String owern; //车主
	
	@XStreamAlias("LICENSECOLOR")
	private String licenseColor; //号牌底色
	
	@XStreamAlias("COLORCODE")
	private String colorCode; //车身颜色
	
	@XStreamAlias("VEHICLEMODLENAME")
	private String vehiclemodleName; //车型名称
	
	@XStreamAlias("DRIVER")
	private CTDriverVo driver; //驾驶员信息
	
	@XStreamAlias("CHECK")
	private CTCheckVo check; //查勘信息
	
	@XStreamAlias("MOTORTYPECODE")
	private String motorTypeCode; //车辆种类



	public String getMotorTypeCode() {
		return motorTypeCode;
	}

	public void setMotorTypeCode(String motorTypeCode) {
		this.motorTypeCode = motorTypeCode;
	}

	public String getIfObject() {
		return ifObject;
	}

	public void setIfObject(String ifObject) {
		this.ifObject = ifObject;
	}

	public String getCarId() {
		return carId;
	}

	public void setCarId(String carId) {
		this.carId = carId;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getLicenseNo() {
		return licenseNo;
	}

	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}

	public String getLicenseType() {
		return licenseType;
	}

	public void setLicenseType(String licenseType) {
		this.licenseType = licenseType;
	}

	public String getEngineNo() {
		return engineNo;
	}

	public void setEngineNo(String engineNo) {
		this.engineNo = engineNo;
	}

	public String getFrameNo() {
		return frameNo;
	}

	public void setFrameNo(String frameNo) {
		this.frameNo = frameNo;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public String getInsurComcode() {
		return insurComcode;
	}

	public void setInsurComcode(String insurComcode) {
		this.insurComcode = insurComcode;
	}

	public String getRegisteDate() {
		return registeDate;
	}

	public void setRegisteDate(String registeDate) {
		this.registeDate = registeDate;
	}

	public String getOwern() {
		return owern;
	}

	public void setOwern(String owern) {
		this.owern = owern;
	}

	public String getLicenseColor() {
		return licenseColor;
	}

	public void setLicenseColor(String licenseColor) {
		this.licenseColor = licenseColor;
	}

	public String getColorCode() {
		return colorCode;
	}

	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}

	public String getVehiclemodleName() {
		return vehiclemodleName;
	}

	public void setVehiclemodleName(String vehiclemodleName) {
		this.vehiclemodleName = vehiclemodleName;
	}

	public CTDriverVo getDriver() {
		return driver;
	}

	public void setDriver(CTDriverVo driver) {
		this.driver = driver;
	}

	public CTCheckVo getCheck() {
		return check;
	}

	public void setCheck(CTCheckVo check) {
		this.check = check;
	}
}
