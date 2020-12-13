package ins.sino.claimcar.trafficplatform.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("VehicleData")
public class EWCheckCarData implements Serializable {

	private static final long serialVersionUID = 8423652723600188374L;
	
	@XStreamAlias("LicensePlateNo")
	private String licensePlateNo;  
	
	@XStreamAlias("LicensePlateType")
	private String licensePlateType;
	
	@XStreamAlias("VIN")
	private String vin;
	
	@XStreamAlias("EngineNo")
	private String engineNo;  
	
	@XStreamAlias("Model")
	private String model;
	
	@XStreamAlias("DriverName")
	private String driverName;
	
	@XStreamAlias("DriverLicenseNo")
	private String driverLicenseNo;  
	
	@XStreamAlias("VehicleProperty")
	private String vehicleProperty;
	
	@XStreamAlias("FieldType")
	private String fieldType;
	
	@XStreamAlias("EstimatedLossAmount")
	private BigDecimal estimatedLossAmount;
	
	@XStreamAlias("CheckerName")
	private String checkerName;
	
	@XStreamAlias("CheckerCode")
	private String checkerCode;
	
	@XStreamAlias("CheckerCertiCode")
	private String checkerCertiCode;
	
	@XStreamAlias("CheckStartTime")
	private String checkStartTime;
	
	@XStreamAlias("CheckEndTime")
	private String checkEndTime;
	
	@XStreamAlias("CheckAddr")
	private String checkAddr;
	
	@XStreamAlias("CheckDesc")
	private String checkDesc;

	public String getLicensePlateNo() {
		return licensePlateNo;
	}

	public void setLicensePlateNo(String licensePlateNo) {
		this.licensePlateNo = licensePlateNo;
	}

	public String getLicensePlateType() {
		return licensePlateType;
	}

	public void setLicensePlateType(String licensePlateType) {
		this.licensePlateType = licensePlateType;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public String getEngineNo() {
		return engineNo;
	}

	public void setEngineNo(String engineNo) {
		this.engineNo = engineNo;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getDriverLicenseNo() {
		return driverLicenseNo;
	}

	public void setDriverLicenseNo(String driverLicenseNo) {
		this.driverLicenseNo = driverLicenseNo;
	}

	public String getVehicleProperty() {
		return vehicleProperty;
	}

	public void setVehicleProperty(String vehicleProperty) {
		this.vehicleProperty = vehicleProperty;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public BigDecimal getEstimatedLossAmount() {
		return estimatedLossAmount;
	}

	public void setEstimatedLossAmount(BigDecimal estimatedLossAmount) {
		this.estimatedLossAmount = estimatedLossAmount;
	}

	public String getCheckerName() {
		return checkerName;
	}

	public void setCheckerName(String checkerName) {
		this.checkerName = checkerName;
	}

	public String getCheckerCode() {
		return checkerCode;
	}

	public void setCheckerCode(String checkerCode) {
		this.checkerCode = checkerCode;
	}

	public String getCheckerCertiCode() {
		return checkerCertiCode;
	}

	public void setCheckerCertiCode(String checkerCertiCode) {
		this.checkerCertiCode = checkerCertiCode;
	}

	public String getCheckStartTime() {
		return checkStartTime;
	}

	public void setCheckStartTime(String checkStartTime) {
		this.checkStartTime = checkStartTime;
	}

	public String getCheckEndTime() {
		return checkEndTime;
	}

	public void setCheckEndTime(String checkEndTime) {
		this.checkEndTime = checkEndTime;
	}

	public String getCheckAddr() {
		return checkAddr;
	}

	public void setCheckAddr(String checkAddr) {
		this.checkAddr = checkAddr;
	}

	public String getCheckDesc() {
		return checkDesc;
	}

	public void setCheckDesc(String checkDesc) {
		this.checkDesc = checkDesc;
	}
	
	
}
