package ins.sino.claimcar.interf.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Vehicle")
public class VehicleVo {
    /*核定载重量*/
	@XStreamAlias("ApprovedCapacity")
	private String approvedCapacity;
	/*核定载客*/
	@XStreamAlias("ApprovedPassenger")
	private String approvedPassenger;
	/*车辆品牌*/
	@XStreamAlias("Brand")
	private String brand;
	/*车龄(以年为单位)*/
	@XStreamAlias("CarAge")
	private String carAge;
	/*排量*/
	@XStreamAlias("Displacement")
	private String displacement;
	/*行驶区域*/
	@XStreamAlias("DrivingArea")
	private String drivingArea;
	/*发动机号*/
	@XStreamAlias("EngineNumber")
	private String engineNumber;
	/*初次登记日期*/
	@XStreamAlias("FirstRegistrationDate")
	private String firstRegistrationDate;
	/*燃料类型*/
	@XStreamAlias("GasType")
	private String gasType;
	/*保险类型 0:无保 1:商业 2:交强 3:混保*/
	@XStreamAlias("InsuredFlag")
	private String insuredFlag;
	/*承保公司*/
	@XStreamAlias("Insurer")
	private String insurer;
	/*是否进口车*/
	@XStreamAlias("IsImported")
	private String isImported;
	/*是否标的车*/
	@XStreamAlias("IsMainCar")
	private String isMainCar;
	/*制造商*/
	@XStreamAlias("Manufacturer")
	private String manufacturer;
	/*行驶里程数*/
	@XStreamAlias("Mileage")
	private String mileage;
	/*车辆型号*/
	@XStreamAlias("Model")
	private String model;
	/*新车购置价*/
	@XStreamAlias("NewCarAmount")
	private String newCarAmount;
	/*车主*/
	@XStreamAlias("Owner")
	private String owner;
	/*车牌号*/
	@XStreamAlias("RegistrationNumber")
	private String registrationNumber;
	/*座位数*/
	@XStreamAlias("SeatCount")
	private String seatCount;
	/*使用性质*/
	@XStreamAlias("TypeOfUsage")
	private String typeOfUsage;
	/*来自保险公司的车辆ID*/
	@XStreamAlias("VehicleID")
	private String vehicleID;
	/*车价*/
	@XStreamAlias("VehiclePrice")
	private String vehiclePrice;
	/*车辆种类*/
	@XStreamAlias("VehicleType")
	private String vehicleType;
	/*VIN码*/
	@XStreamAlias("VIN")
	private String vin;
	/* */
	@XStreamAlias("NotificationNumber")
	private String notificationNumber;
	
	/*生产年份*/
	@XStreamAlias("YearOfProduction")
	private String yearOfProduction;
	public String getApprovedCapacity() {
		return approvedCapacity;
	}
	public void setApprovedCapacity(String approvedCapacity) {
		this.approvedCapacity = approvedCapacity;
	}
	public String getApprovedPassenger() {
		return approvedPassenger;
	}
	public void setApprovedPassenger(String approvedPassenger) {
		this.approvedPassenger = approvedPassenger;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getCarAge() {
		return carAge;
	}
	public void setCarAge(String carAge) {
		this.carAge = carAge;
	}
	public String getDisplacement() {
		return displacement;
	}
	public void setDisplacement(String displacement) {
		this.displacement = displacement;
	}
	public String getDrivingArea() {
		return drivingArea;
	}
	public void setDrivingArea(String drivingArea) {
		this.drivingArea = drivingArea;
	}
	public String getEngineNumber() {
		return engineNumber;
	}
	public void setEngineNumber(String engineNumber) {
		this.engineNumber = engineNumber;
	}
	public String getFirstRegistrationDate() {
		return firstRegistrationDate;
	}
	public void setFirstRegistrationDate(String firstRegistrationDate) {
		this.firstRegistrationDate = firstRegistrationDate;
	}
	public String getGasType() {
		return gasType;
	}
	public void setGasType(String gasType) {
		this.gasType = gasType;
	}
	public String getInsuredFlag() {
		return insuredFlag;
	}
	public void setInsuredFlag(String insuredFlag) {
		this.insuredFlag = insuredFlag;
	}
	public String getInsurer() {
		return insurer;
	}
	public void setInsurer(String insurer) {
		this.insurer = insurer;
	}
	public String getIsImported() {
		return isImported;
	}
	public void setIsImported(String isImported) {
		this.isImported = isImported;
	}
	public String getIsMainCar() {
		return isMainCar;
	}
	public void setIsMainCar(String isMainCar) {
		this.isMainCar = isMainCar;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public String getMileage() {
		return mileage;
	}
	public void setMileage(String mileage) {
		this.mileage = mileage;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getNewCarAmount() {
		return newCarAmount;
	}
	public void setNewCarAmount(String newCarAmount) {
		this.newCarAmount = newCarAmount;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getRegistrationNumber() {
		return registrationNumber;
	}
	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}
	public String getSeatCount() {
		return seatCount;
	}
	public void setSeatCount(String seatCount) {
		this.seatCount = seatCount;
	}
	public String getTypeOfUsage() {
		return typeOfUsage;
	}
	public void setTypeOfUsage(String typeOfUsage) {
		this.typeOfUsage = typeOfUsage;
	}
	public String getVehicleID() {
		return vehicleID;
	}
	public void setVehicleID(String vehicleID) {
		this.vehicleID = vehicleID;
	}
	public String getVehiclePrice() {
		return vehiclePrice;
	}
	public void setVehiclePrice(String vehiclePrice) {
		this.vehiclePrice = vehiclePrice;
	}
	public String getVehicleType() {
		return vehicleType;
	}
	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}
	public String getYearOfProduction() {
		return yearOfProduction;
	}
	public void setYearOfProduction(String yearOfProduction) {
		this.yearOfProduction = yearOfProduction;
	}
	public String getNotificationNumber() {
		return notificationNumber;
	}
	public void setNotificationNumber(String notificationNumber) {
		this.notificationNumber = notificationNumber;
	}
	
	
}
