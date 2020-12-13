package ins.sino.claimcar.platform.vo;

import ins.sino.claimcar.carplatform.adapter.DateXmlAdapter;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * 车辆损失情况信息
 * 
 * <pre></pre>
 * 
 * @author ★XMSH
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CiLossCarDataVo {

	@XmlElement(name = "CAR_MARK")
	private String carMark;// 损失出险车辆号牌号码

	@XmlElement(name = "VEHICLE_TYPE")
	private String vehicleType;// 损失出险车辆号牌种类代码；参见代码

	@XmlElement(name = "ENGINE_NO")
	private String engineNo;// 损失出险车辆发动机号

	@XmlElement(name = "RACK_NO")
	private String rackNo;// 损失出险车辆VIN码

	@XmlElement(name = "VEHICLE_MODEL")
	private String vehicleModel;// 车辆型号

	@XmlElement(name = "DRIVER_NAME")
	private String driverName;// 出险驾驶员姓名

	@XmlElement(name = "CERTI_TYPE")
	private String certiType;// 出险驾驶员证件类型；参见代码

	@XmlElement(name = "CERTI_CODE")
	private String certiCode;// 出险驾驶员证件号码

	@XmlElement(name = "DRIVER_LICENSE_NO")
	private String driverLicenseNo;// 出险车辆驾驶证号码

	@XmlElement(name = "UNDER_DEF_LOSS", required = true)
	private String underDefLoss;// 核损金额

	@XmlElement(name = "VEHICLE_PROPERTY", required = true)
	private String vehicleProperty;// 车辆属性(本车/三者车)；参见代码

	@XmlElement(name = "IS_ROBBER", required = true)
	private String isRobber;// 是否盗抢；参见代码

	@XmlElement(name = "FIELD_TYPE", required = true)
	private String fieldType;// 现场类别；参见代码

	@XmlElement(name = "ESTIMATE_NAME")
	private String estimateName;// 定损人员姓名

	@XmlElement(name = "ESTIMATE_CODE")
	private String estimateCode;// 定损人员代码

	@XmlElement(name = "UNDER_WRITE_NAME")
	private String underWriteName;// 核损人员姓名

	@XmlElement(name = "UNDER_WRITE_CODE")
	private String underWriteCode;// 核损人员代码

	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	@XmlElement(name = "ESTIMATE_START_TIME", required = true)
	private Date estimateStartTime;// 车辆损失定损开始时间 格式：YYYYMMDDHHMM

	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	@XmlElement(name = "UNDER_END_TIME", required = true)
	private Date underEndTime;// 车辆损失核损结束时间 格式：YYYYMMDDHHMM

	@XmlElement(name = "ESTIMATE_PLACE")
	private String estimatePlace;// 定损地点

	@XmlElement(name = "REMNANT")
	private String remnant;// 残值回收预估金额

	@XmlElement(name = "TOTAL_MAN_HOUR")
	private String totalManHour;// 配件总工时

	@XmlElement(name = "IS_CHANGE_OR_REPAIR")
	private String isChangeOrRepair;// 是否修理或更换配件；参见代码

	@XmlElement(name = "REPAIR_FACTORY_NAME")
	private String repairFactoryName;// 修理机构名称

	@XmlElement(name = "REPAIR_FACTORY_CODE")
	private String repairFactoryCode;// 修理机构组织机构代码

	@XmlElement(name = "REPAIR_FACTORY_TYPE")
	private String repairFactoryType;// 修理机构类型

	@XmlElement(name = "ESTIMATE_CERTI_CODE")
	private String estimateCertiCode;// 定损人员身份证号码

	@XmlElement(name = "CARKIND")
	private String carKind; //车辆种类
	

	@XmlElement(name = "UNDER_WRITE_CERTI_CODE")
	private String underWriteCertiCode;// 核损人员身份证号码
	
	@XmlElement(name = "FITTING_TOTAL_PRICE")
	private String fittingTotalPrice;		//配件价格合计
	
	@XmlElement(name = "MAN_HOUR_ACCESSORIES_TOTAL_PRICE")
	private String manHourAccessoriesTotalPrice;  //工时/辅料费用合计
	
//	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	@XmlElement(name = "VEHICLE_REGISTER_FIRST_DATE")
	private String vehicleRegisterFirstDate;	//车辆初次登记日期
	
//	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	@XmlElement(name = "DRIVING_LICENSE_DATE")
	private String drivingLicenseDate;		//机动车行驶证发证日期
	

	@XmlElementWrapper(name = "LOSS_PART_LIST")
	@XmlElement(name = "LOSS_PART_DATA")
	private List<CiLossCarLossPartDataVo> lossCarLossPartDataVos;// 车辆损失部位列表

	@XmlElementWrapper(name = "FITTING_LIST")
	@XmlElement(name = "FITTING_DATA")
	private List<CiLossCarFittingDataVo> lossCarFittingDataVos;// 车辆配件明细列表
	
	@XmlElementWrapper(name = "MAN_HOUR_ACCESSORIES_LIST")
	@XmlElement(name = "MAN_HOUR_ACCESSORIES_DATA")
	private List<CiLossCarHourDataVo> lossCarHourDataVos;      //工时/辅料明细列表
	
	
	
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

	public List<CiLossCarHourDataVo> getLossCarHourDataVos() {
		return lossCarHourDataVos;
	}

	public void setLossCarHourDataVos(List<CiLossCarHourDataVo> lossCarHourDataVos) {
		this.lossCarHourDataVos = lossCarHourDataVos;
	}

	public String getCarKind() {
		return carKind;
	}

	public void setCarKind(String carKind) {
		this.carKind = carKind;
	}

	/**
	 * @return 返回 carMark 损失出险车辆号牌号码
	 */
	public String getCarMark() {
		return carMark;
	}

	/**
	 * @param carMark 要设置的 损失出险车辆号牌号码
	 */
	public void setCarMark(String carMark) {
		this.carMark = carMark;
	}

	/**
	 * @return 返回 vehicleType 损失出险车辆号牌种类代码；参见代码
	 */
	public String getVehicleType() {
		return vehicleType;
	}

	/**
	 * @param vehicleType 要设置的 损失出险车辆号牌种类代码；参见代码
	 */
	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	/**
	 * @return 返回 engineNo 损失出险车辆发动机号
	 */
	public String getEngineNo() {
		return engineNo;
	}

	/**
	 * @param engineNo 要设置的 损失出险车辆发动机号
	 */
	public void setEngineNo(String engineNo) {
		this.engineNo = engineNo;
	}

	/**
	 * @return 返回 rackNo 损失出险车辆VIN码
	 */
	public String getRackNo() {
		return rackNo;
	}

	/**
	 * @param rackNo 要设置的 损失出险车辆VIN码
	 */
	public void setRackNo(String rackNo) {
		this.rackNo = rackNo;
	}

	/**
	 * @return 返回 vehicleModel 车辆型号
	 */
	public String getVehicleModel() {
		return vehicleModel;
	}

	/**
	 * @param vehicleModel 要设置的 车辆型号
	 */
	public void setVehicleModel(String vehicleModel) {
		this.vehicleModel = vehicleModel;
	}

	/**
	 * @return 返回 driverName 出险驾驶员姓名
	 */
	public String getDriverName() {
		return driverName;
	}

	/**
	 * @param driverName 要设置的 出险驾驶员姓名
	 */
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	/**
	 * @return 返回 certiType 出险驾驶员证件类型；参见代码
	 */
	public String getCertiType() {
		return certiType;
	}

	/**
	 * @param certiType 要设置的 出险驾驶员证件类型；参见代码
	 */
	public void setCertiType(String certiType) {
		this.certiType = certiType;
	}

	/**
	 * @return 返回 certiCode 出险驾驶员证件号码
	 */
	public String getCertiCode() {
		return certiCode;
	}

	/**
	 * @param certiCode 要设置的 出险驾驶员证件号码
	 */
	public void setCertiCode(String certiCode) {
		this.certiCode = certiCode;
	}

	/**
	 * @return 返回 driverLicenseNo 出险车辆驾驶证号码
	 */
	public String getDriverLicenseNo() {
		return driverLicenseNo;
	}

	/**
	 * @param driverLicenseNo 要设置的 出险车辆驾驶证号码
	 */
	public void setDriverLicenseNo(String driverLicenseNo) {
		this.driverLicenseNo = driverLicenseNo;
	}

	/**
	 * @return 返回 underDefLoss 核损金额
	 */
	public String getUnderDefLoss() {
		return underDefLoss;
	}

	/**
	 * @param underDefLoss 要设置的 核损金额
	 */
	public void setUnderDefLoss(String underDefLoss) {
		this.underDefLoss = underDefLoss;
	}

	/**
	 * @return 返回 vehicleProperty 车辆属性(本车/三者车)；参见代码
	 */
	public String getVehicleProperty() {
		return vehicleProperty;
	}

	/**
	 * @param vehicleProperty 要设置的 车辆属性(本车/三者车)；参见代码
	 */
	public void setVehicleProperty(String vehicleProperty) {
		this.vehicleProperty = vehicleProperty;
	}

	/**
	 * @return 返回 isRobber 是否盗抢；参见代码
	 */
	public String getIsRobber() {
		return isRobber;
	}

	/**
	 * @param isRobber 要设置的 是否盗抢；参见代码
	 */
	public void setIsRobber(String isRobber) {
		this.isRobber = isRobber;
	}

	/**
	 * @return 返回 fieldType 现场类别；参见代码
	 */
	public String getFieldType() {
		return fieldType;
	}

	/**
	 * @param fieldType 要设置的 现场类别；参见代码
	 */
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	/**
	 * @return 返回 estimateName 定损人员姓名
	 */
	public String getEstimateName() {
		return estimateName;
	}

	/**
	 * @param estimateName 要设置的 定损人员姓名
	 */
	public void setEstimateName(String estimateName) {
		this.estimateName = estimateName;
	}

	/**
	 * @return 返回 estimateCode 定损人员代码
	 */
	public String getEstimateCode() {
		return estimateCode;
	}

	/**
	 * @param estimateCode 要设置的 定损人员代码
	 */
	public void setEstimateCode(String estimateCode) {
		this.estimateCode = estimateCode;
	}

	/**
	 * @return 返回 underWriteName 核损人员姓名
	 */
	public String getUnderWriteName() {
		return underWriteName;
	}

	/**
	 * @param underWriteName 要设置的 核损人员姓名
	 */
	public void setUnderWriteName(String underWriteName) {
		this.underWriteName = underWriteName;
	}

	/**
	 * @return 返回 underWriteCode 核损人员代码
	 */
	public String getUnderWriteCode() {
		return underWriteCode;
	}

	/**
	 * @param underWriteCode 要设置的 核损人员代码
	 */
	public void setUnderWriteCode(String underWriteCode) {
		this.underWriteCode = underWriteCode;
	}

	/**
	 * @return 返回 estimateStartTime 车辆损失定损开始时间 格式：YYYYMMDDHHMM
	 */
	public Date getEstimateStartTime() {
		return estimateStartTime;
	}

	/**
	 * @param estimateStartTime 要设置的 车辆损失定损开始时间 格式：YYYYMMDDHHMM
	 */
	public void setEstimateStartTime(Date estimateStartTime) {
		this.estimateStartTime = estimateStartTime;
	}

	/**
	 * @return 返回 underEndTime 车辆损失核损结束时间 格式：YYYYMMDDHHMM
	 */
	public Date getUnderEndTime() {
		return underEndTime;
	}

	/**
	 * @param underEndTime 要设置的 车辆损失核损结束时间 格式：YYYYMMDDHHMM
	 */
	public void setUnderEndTime(Date underEndTime) {
		this.underEndTime = underEndTime;
	}

	/**
	 * @return 返回 estimatePlace 定损地点
	 */
	public String getEstimatePlace() {
		return estimatePlace;
	}

	/**
	 * @param estimatePlace 要设置的 定损地点
	 */
	public void setEstimatePlace(String estimatePlace) {
		this.estimatePlace = estimatePlace;
	}

	/**
	 * @return 返回 remnant 残值回收预估金额
	 */
	public String getRemnant() {
		return remnant;
	}

	/**
	 * @param remnant 要设置的 残值回收预估金额
	 */
	public void setRemnant(String remnant) {
		this.remnant = remnant;
	}

	/**
	 * @return 返回 totalManHour 配件总工时
	 */
	public String getTotalManHour() {
		return totalManHour;
	}

	/**
	 * @param totalManHour 要设置的 配件总工时
	 */
	public void setTotalManHour(String totalManHour) {
		this.totalManHour = totalManHour;
	}

	/**
	 * @return 返回 isChangeOrRepair 是否修理或更换配件；参见代码
	 */
	public String getIsChangeOrRepair() {
		return isChangeOrRepair;
	}

	/**
	 * @param isChangeOrRepair 要设置的 是否修理或更换配件；参见代码
	 */
	public void setIsChangeOrRepair(String isChangeOrRepair) {
		this.isChangeOrRepair = isChangeOrRepair;
	}

	/**
	 * @return 返回 repairFactoryName 修理机构名称
	 */
	public String getRepairFactoryName() {
		return repairFactoryName;
	}

	/**
	 * @param repairFactoryName 要设置的 修理机构名称
	 */
	public void setRepairFactoryName(String repairFactoryName) {
		this.repairFactoryName = repairFactoryName;
	}

	/**
	 * @return 返回 repairFactoryCode 修理机构组织机构代码
	 */
	public String getRepairFactoryCode() {
		return repairFactoryCode;
	}

	/**
	 * @param repairFactoryCode 要设置的 修理机构组织机构代码
	 */
	public void setRepairFactoryCode(String repairFactoryCode) {
		this.repairFactoryCode = repairFactoryCode;
	}

	/**
	 * @return 返回 repairFactoryType 修理机构类型
	 */
	public String getRepairFactoryType() {
		return repairFactoryType;
	}

	/**
	 * @param repairFactoryType 要设置的 修理机构类型
	 */
	public void setRepairFactoryType(String repairFactoryType) {
		this.repairFactoryType = repairFactoryType;
	}

	/**
	 * @return 返回 estimateCertiCode 定损人员身份证号码
	 */
	public String getEstimateCertiCode() {
		return estimateCertiCode;
	}

	/**
	 * @param estimateCertiCode 要设置的 定损人员身份证号码
	 */
	public void setEstimateCertiCode(String estimateCertiCode) {
		this.estimateCertiCode = estimateCertiCode;
	}

	/**
	 * @return 返回 underWriteCertiCode 核损人员身份证号码
	 */
	public String getUnderWriteCertiCode() {
		return underWriteCertiCode;
	}

	/**
	 * @param underWriteCertiCode 要设置的 核损人员身份证号码
	 */
	public void setUnderWriteCertiCode(String underWriteCertiCode) {
		this.underWriteCertiCode = underWriteCertiCode;
	}

	public List<CiLossCarLossPartDataVo> getLossCarLossPartDataVos() {
		return lossCarLossPartDataVos;
	}

	public void setLossCarLossPartDataVos(List<CiLossCarLossPartDataVo> lossCarLossPartDataVos) {
		this.lossCarLossPartDataVos = lossCarLossPartDataVos;
	}

	public List<CiLossCarFittingDataVo> getLossCarFittingDataVos() {
		return lossCarFittingDataVos;
	}

	public void setLossCarFittingDataVos(List<CiLossCarFittingDataVo> lossCarFittingDataVos) {
		this.lossCarFittingDataVos = lossCarFittingDataVos;
	}

}
