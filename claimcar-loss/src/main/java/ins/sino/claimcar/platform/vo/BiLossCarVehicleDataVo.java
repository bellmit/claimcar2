package ins.sino.claimcar.platform.vo;

import ins.sino.claimcar.carplatform.adapter.DateXmlAdapter;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
public class BiLossCarVehicleDataVo {
	@XmlElement(name = "LicensePlateNo")
	private String licensePlateNo;//出险车辆号牌号码

	@XmlElement(name = "LicensePlateType")
	private String licensePlateType;//出险车辆号牌种类代码；参见代码

	@XmlElement(name = "EngineNo")
	private String engineNo;//出险车辆发动机号

	@XmlElement(name = "VIN", required = true)
	private String vIN;//出险车辆VIN码

	@XmlElement(name = "Model")
	private String model;//出险车辆车辆型号

	@XmlElement(name = "DriverName", required = true)
	private String driverName;//出险驾驶员姓名

	@XmlElement(name = "CertiType", required = true)
	private String certiType;//出险驾驶员证件类型；参见代码

	@XmlElement(name = "CertiCode", required = true)
	private String certiCode;//出险驾驶员证件号码，与“出险驾驶员证件类型”配套使用

	@XmlElement(name = "DriverLicenseNo", required = true)
	private String driverLicenseNo;//出险车辆驾驶证号码

	@XmlElement(name = "UnderDefLoss", required = true)
	private String underDefLoss;//核损金额

	@XmlElement(name = "VehicleProperty", required = true)
	private String vehicleProperty;//车辆属性；参见代码

	@XmlElement(name = "IsRobber", required = true)
	private String isRobber;//是否盗抢；参见代码

	@XmlElement(name = "FieldType", required = true)
	private String fieldType;//现场类别；参见代码

	@XmlElement(name = "EstimateName", required = true)
	private String estimateName;//定损人员姓名

	@XmlElement(name = "EstimateCode")
	private String estimateCode;//定损人员代码

	@XmlElement(name = "EstimateCertiCode", required = true)
	private String estimateCertiCode;//定损人员身份证号码

	@XmlElement(name = "UnderWriteName")
	private String underWriteName;//核损人员姓名

	@XmlElement(name = "UnderWriteCode")
	private String underWriteCode;//核损人员代码

	@XmlElement(name = "UnderWriteCertiCode")
	private String underWriteCertiCode;//核损人员身份证号码

	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	@XmlElement(name = "EstimateStartTime", required = true)
	private Date estimateStartTime;//车辆损失定损开始时间 ；精确到分钟

	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	@XmlElement(name = "UnderEndTime", required = true)
	private Date underEndTime;//车辆损失核损结束时间 ；精确到分钟

	@XmlElement(name = "EstimateAddr")
	private String estimateAddr;//定损地点

	@XmlElement(name = "Remnant")
	private String remnant;//残值回收预估金额

	@XmlElement(name = "TotalManHour")
	private String totalManHour;//配件总工时

	@XmlElement(name = "IsChangeOrRepair")
	private String isChangeOrRepair;//是否修理或更换配件；参见代码

	@XmlElement(name = "RepairFactoryName", required = true)
	private String repairFactoryName;//修理机构名称

	@XmlElement(name = "RepairFactoryCertiCode")
	private String repairFactoryCertiCode;//修理机构组织机构代码

	@XmlElement(name = "RepairFactoryType")
	private String repairFactoryType;//修理机构类型

	@XmlElement(name = "LossPartData")
	private List<BiLossCarLossPartDataVo> lossPartDataVos;//车辆损失部位列表

	@XmlElement(name = "FittingData")
	private List<BiLossCarFittingDataVo> fittingDataVos;//车辆配件明细列表
	
	@XmlElement(name = "ManHourAccessoriesData")
	private List<BiLossCarHourDataVo> lossCarHourDataVos; //工时/辅料明细列表

	@XmlElement(name = "IsTotalLoss", required = true)
	private String isTotalLoss;//是否全损；参见代码

	@XmlElement(name = "IsHotSinceDetonation", required = true)
	private String isHotSinceDetonation;//是否火自爆；参见代码

	@XmlElement(name = "IsWaterFlooded", required = true)
	private String isWaterFlooded;//是否水淹；参见代码

	@XmlElement(name = "WaterFloodedLevel")
	private String waterFloodedLevel;//水淹等级；参见代码
	
	@XmlElement(name = "MotorTypeCode")
	private String MotorTypeCode;//车辆种类；参见代码
	
	@XmlElement(name = "FittingTotalPrice")
	private String fittingTotalPrice;		//配件价格合计
	
	@XmlElement(name = "ManHourAccessoriesTotalPrice")
	private String manHourAccessoriesTotalPrice;  //工时/辅料费用合计
	
//	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	@XmlElement(name = "VehicleRegisterFirstDate")
	private String vehicleRegisterFirstDate;	//车辆初次登记日期
	
//	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	@XmlElement(name = "DrivingLicenseDate")
	private String drivingLicenseDate;		//机动车行驶证发证日期

	@XmlElement(name = "IsGlassBroken")
	private String isGlassBroken;  //是否玻璃单独破碎;《公用代码》2.1.79
	
	@XmlElement(name = "IsNotFindThird")
	private String isNotFindThird;  //是否属于无法找到第三方;《公用代码》2.1.79
	
	public String getFittingTotalPrice() {
		return fittingTotalPrice;
	}



	public void setFittingTotalPrice(String fittingTotalPrice) {
		this.fittingTotalPrice = fittingTotalPrice;
	}



	public String getManHourAccessoriesTotalPrice() {
		return manHourAccessoriesTotalPrice;
	}



	public void setManHourAccessoriesTotalPrice(String manHourAccessoriesTotalPrice) {
		this.manHourAccessoriesTotalPrice = manHourAccessoriesTotalPrice;
	}



	public String getVehicleRegisterFirstDate() {
		return vehicleRegisterFirstDate;
	}



	public void setVehicleRegisterFirstDate(String vehicleRegisterFirstDate) {
		this.vehicleRegisterFirstDate = vehicleRegisterFirstDate;
	}



	public String getDrivingLicenseDate() {
		return drivingLicenseDate;
	}



	public void setDrivingLicenseDate(String drivingLicenseDate) {
		this.drivingLicenseDate = drivingLicenseDate;
	}



	public String getMotorTypeCode() {
		return MotorTypeCode;
	}
	
	

	public List<BiLossCarHourDataVo> getLossCarHourDataVos() {
		return lossCarHourDataVos;
	}



	public void setLossCarHourDataVos(List<BiLossCarHourDataVo> lossCarHourDataVos) {
		this.lossCarHourDataVos = lossCarHourDataVos;
	}



	public void setMotorTypeCode(String motorTypeCode) {
		MotorTypeCode = motorTypeCode;
	}

	/** 
	 * @return 返回 licensePlateNo  出险车辆号牌号码
	 */ 
	public String getLicensePlateNo(){ 
	    return licensePlateNo;
	}

	/** 
	 * @param licensePlateNo 要设置的 出险车辆号牌号码
	 */ 
	public void setLicensePlateNo(String licensePlateNo){ 
	    this.licensePlateNo=licensePlateNo;
	}

	/** 
	 * @return 返回 licensePlateType  出险车辆号牌种类代码；参见代码
	 */ 
	public String getLicensePlateType(){ 
	    return licensePlateType;
	}

	/** 
	 * @param licensePlateType 要设置的 出险车辆号牌种类代码；参见代码
	 */ 
	public void setLicensePlateType(String licensePlateType){ 
	    this.licensePlateType=licensePlateType;
	}

	/** 
	 * @return 返回 engineNo  出险车辆发动机号
	 */ 
	public String getEngineNo(){ 
	    return engineNo;
	}

	/** 
	 * @param engineNo 要设置的 出险车辆发动机号
	 */ 
	public void setEngineNo(String engineNo){ 
	    this.engineNo=engineNo;
	}

	/** 
	 * @return 返回 vIN  出险车辆VIN码
	 */ 
	public String getVIN(){ 
	    return vIN;
	}

	/** 
	 * @param vIN 要设置的 出险车辆VIN码
	 */ 
	public void setVIN(String vIN){ 
	    this.vIN=vIN;
	}

	/** 
	 * @return 返回 model  出险车辆车辆型号
	 */ 
	public String getModel(){ 
	    return model;
	}

	/** 
	 * @param model 要设置的 出险车辆车辆型号
	 */ 
	public void setModel(String model){ 
	    this.model=model;
	}

	/** 
	 * @return 返回 driverName  出险驾驶员姓名
	 */ 
	public String getDriverName(){ 
	    return driverName;
	}

	/** 
	 * @param driverName 要设置的 出险驾驶员姓名
	 */ 
	public void setDriverName(String driverName){ 
	    this.driverName=driverName;
	}

	/** 
	 * @return 返回 certiType  出险驾驶员证件类型；参见代码
	 */ 
	public String getCertiType(){ 
	    return certiType;
	}

	/** 
	 * @param certiType 要设置的 出险驾驶员证件类型；参见代码
	 */ 
	public void setCertiType(String certiType){ 
	    this.certiType=certiType;
	}

	/** 
	 * @return 返回 certiCode  出险驾驶员证件号码，与“出险驾驶员证件类型”配套使用
	 */ 
	public String getCertiCode(){ 
	    return certiCode;
	}

	/** 
	 * @param certiCode 要设置的 出险驾驶员证件号码，与“出险驾驶员证件类型”配套使用
	 */ 
	public void setCertiCode(String certiCode){ 
	    this.certiCode=certiCode;
	}

	/** 
	 * @return 返回 driverLicenseNo  出险车辆驾驶证号码
	 */ 
	public String getDriverLicenseNo(){ 
	    return driverLicenseNo;
	}

	/** 
	 * @param driverLicenseNo 要设置的 出险车辆驾驶证号码
	 */ 
	public void setDriverLicenseNo(String driverLicenseNo){ 
	    this.driverLicenseNo=driverLicenseNo;
	}

	/** 
	 * @return 返回 underDefLoss  核损金额
	 */ 
	public String getUnderDefLoss(){ 
	    return underDefLoss;
	}

	/** 
	 * @param underDefLoss 要设置的 核损金额
	 */ 
	public void setUnderDefLoss(String underDefLoss){ 
	    this.underDefLoss=underDefLoss;
	}

	/** 
	 * @return 返回 vehicleProperty  车辆属性；参见代码
	 */ 
	public String getVehicleProperty(){ 
	    return vehicleProperty;
	}

	/** 
	 * @param vehicleProperty 要设置的 车辆属性；参见代码
	 */ 
	public void setVehicleProperty(String vehicleProperty){ 
	    this.vehicleProperty=vehicleProperty;
	}

	/** 
	 * @return 返回 isRobber  是否盗抢；参见代码
	 */ 
	public String getIsRobber(){ 
	    return isRobber;
	}

	/** 
	 * @param isRobber 要设置的 是否盗抢；参见代码
	 */ 
	public void setIsRobber(String isRobber){ 
	    this.isRobber=isRobber;
	}

	/** 
	 * @return 返回 fieldType  现场类别；参见代码
	 */ 
	public String getFieldType(){ 
	    return fieldType;
	}

	/** 
	 * @param fieldType 要设置的 现场类别；参见代码
	 */ 
	public void setFieldType(String fieldType){ 
	    this.fieldType=fieldType;
	}

	/** 
	 * @return 返回 estimateName  定损人员姓名
	 */ 
	public String getEstimateName(){ 
	    return estimateName;
	}

	/** 
	 * @param estimateName 要设置的 定损人员姓名
	 */ 
	public void setEstimateName(String estimateName){ 
	    this.estimateName=estimateName;
	}

	/** 
	 * @return 返回 estimateCode  定损人员代码
	 */ 
	public String getEstimateCode(){ 
	    return estimateCode;
	}

	/** 
	 * @param estimateCode 要设置的 定损人员代码
	 */ 
	public void setEstimateCode(String estimateCode){ 
	    this.estimateCode=estimateCode;
	}

	/** 
	 * @return 返回 estimateCertiCode  定损人员身份证号码
	 */ 
	public String getEstimateCertiCode(){ 
	    return estimateCertiCode;
	}

	/** 
	 * @param estimateCertiCode 要设置的 定损人员身份证号码
	 */ 
	public void setEstimateCertiCode(String estimateCertiCode){ 
	    this.estimateCertiCode=estimateCertiCode;
	}

	/** 
	 * @return 返回 underWriteName  核损人员姓名
	 */ 
	public String getUnderWriteName(){ 
	    return underWriteName;
	}

	/** 
	 * @param underWriteName 要设置的 核损人员姓名
	 */ 
	public void setUnderWriteName(String underWriteName){ 
	    this.underWriteName=underWriteName;
	}

	/** 
	 * @return 返回 underWriteCode  核损人员代码
	 */ 
	public String getUnderWriteCode(){ 
	    return underWriteCode;
	}

	/** 
	 * @param underWriteCode 要设置的 核损人员代码
	 */ 
	public void setUnderWriteCode(String underWriteCode){ 
	    this.underWriteCode=underWriteCode;
	}

	/** 
	 * @return 返回 underWriteCertiCode  核损人员身份证号码
	 */ 
	public String getUnderWriteCertiCode(){ 
	    return underWriteCertiCode;
	}

	/** 
	 * @param underWriteCertiCode 要设置的 核损人员身份证号码
	 */ 
	public void setUnderWriteCertiCode(String underWriteCertiCode){ 
	    this.underWriteCertiCode=underWriteCertiCode;
	}

	/** 
	 * @return 返回 estimateStartTime  车辆损失定损开始时间 ；精确到分钟
	 */ 
	public Date getEstimateStartTime(){ 
	    return estimateStartTime;
	}

	/** 
	 * @param estimateStartTime 要设置的 车辆损失定损开始时间 ；精确到分钟
	 */ 
	public void setEstimateStartTime(Date estimateStartTime){ 
	    this.estimateStartTime=estimateStartTime;
	}

	/** 
	 * @return 返回 underEndTime  车辆损失核损结束时间 ；精确到分钟
	 */ 
	public Date getUnderEndTime(){ 
	    return underEndTime;
	}

	/** 
	 * @param underEndTime 要设置的 车辆损失核损结束时间 ；精确到分钟
	 */ 
	public void setUnderEndTime(Date underEndTime){ 
	    this.underEndTime=underEndTime;
	}

	/** 
	 * @return 返回 estimateAddr  定损地点
	 */ 
	public String getEstimateAddr(){ 
	    return estimateAddr;
	}

	/** 
	 * @param estimateAddr 要设置的 定损地点
	 */ 
	public void setEstimateAddr(String estimateAddr){ 
	    this.estimateAddr=estimateAddr;
	}

	/** 
	 * @return 返回 remnant  残值回收预估金额
	 */ 
	public String getRemnant(){ 
	    return remnant;
	}

	/** 
	 * @param remnant 要设置的 残值回收预估金额
	 */ 
	public void setRemnant(String remnant){ 
	    this.remnant=remnant;
	}

	/** 
	 * @return 返回 totalManHour  配件总工时
	 */ 
	public String getTotalManHour(){ 
	    return totalManHour;
	}

	/** 
	 * @param totalManHour 要设置的 配件总工时
	 */ 
	public void setTotalManHour(String totalManHour){ 
	    this.totalManHour=totalManHour;
	}

	/** 
	 * @return 返回 isChangeOrRepair  是否修理或更换配件；参见代码
	 */ 
	public String getIsChangeOrRepair(){ 
	    return isChangeOrRepair;
	}

	/** 
	 * @param isChangeOrRepair 要设置的 是否修理或更换配件；参见代码
	 */ 
	public void setIsChangeOrRepair(String isChangeOrRepair){ 
	    this.isChangeOrRepair=isChangeOrRepair;
	}

	/** 
	 * @return 返回 repairFactoryName  修理机构名称
	 */ 
	public String getRepairFactoryName(){ 
	    return repairFactoryName;
	}

	/** 
	 * @param repairFactoryName 要设置的 修理机构名称
	 */ 
	public void setRepairFactoryName(String repairFactoryName){ 
	    this.repairFactoryName=repairFactoryName;
	}

	/** 
	 * @return 返回 repairFactoryCertiCode  修理机构组织机构代码
	 */ 
	public String getRepairFactoryCertiCode(){ 
	    return repairFactoryCertiCode;
	}

	/** 
	 * @param repairFactoryCertiCode 要设置的 修理机构组织机构代码
	 */ 
	public void setRepairFactoryCertiCode(String repairFactoryCertiCode){ 
	    this.repairFactoryCertiCode=repairFactoryCertiCode;
	}

	/** 
	 * @return 返回 repairFactoryType  修理机构类型
	 */ 
	public String getRepairFactoryType(){ 
	    return repairFactoryType;
	}

	/** 
	 * @param repairFactoryType 要设置的 修理机构类型
	 */ 
	public void setRepairFactoryType(String repairFactoryType){ 
	    this.repairFactoryType=repairFactoryType;
	}


	
	public List<BiLossCarLossPartDataVo> getLossPartDataVos() {
		return lossPartDataVos;
	}

	
	public void setLossPartDataVos(List<BiLossCarLossPartDataVo> lossPartDataVos) {
		this.lossPartDataVos = lossPartDataVos;
	}

	
	public List<BiLossCarFittingDataVo> getFittingDataVos() {
		return fittingDataVos;
	}

	
	public void setFittingDataVos(List<BiLossCarFittingDataVo> fittingDataVos) {
		this.fittingDataVos = fittingDataVos;
	}

	/** 
	 * @return 返回 isTotalLoss  是否全损；参见代码
	 */ 
	public String getIsTotalLoss(){ 
	    return isTotalLoss;
	}

	/** 
	 * @param isTotalLoss 要设置的 是否全损；参见代码
	 */ 
	public void setIsTotalLoss(String isTotalLoss){ 
	    this.isTotalLoss=isTotalLoss;
	}

	/** 
	 * @return 返回 isHotSinceDetonation  是否火自爆；参见代码
	 */ 
	public String getIsHotSinceDetonation(){ 
	    return isHotSinceDetonation;
	}

	/** 
	 * @param isHotSinceDetonation 要设置的 是否火自爆；参见代码
	 */ 
	public void setIsHotSinceDetonation(String isHotSinceDetonation){ 
	    this.isHotSinceDetonation=isHotSinceDetonation;
	}

	/** 
	 * @return 返回 isWaterFlooded  是否水淹；参见代码
	 */ 
	public String getIsWaterFlooded(){ 
	    return isWaterFlooded;
	}

	/** 
	 * @param isWaterFlooded 要设置的 是否水淹；参见代码
	 */ 
	public void setIsWaterFlooded(String isWaterFlooded){ 
	    this.isWaterFlooded=isWaterFlooded;
	}

	/** 
	 * @return 返回 waterFloodedLevel  水淹等级；参见代码
	 */ 
	public String getWaterFloodedLevel(){ 
	    return waterFloodedLevel;
	}

	/** 
	 * @param waterFloodedLevel 要设置的 水淹等级；参见代码
	 */ 
	public void setWaterFloodedLevel(String waterFloodedLevel){ 
	    this.waterFloodedLevel=waterFloodedLevel;
	}



	public String getIsGlassBroken() {
		return isGlassBroken;
	}



	public void setIsGlassBroken(String isGlassBroken) {
		this.isGlassBroken = isGlassBroken;
	}



	public String getIsNotFindThird() {
		return isNotFindThird;
	}



	public void setIsNotFindThird(String isNotFindThird) {
		this.isNotFindThird = isNotFindThird;
	}

	


}
