package ins.sino.claimcar.trafficplatform.vo;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("VehicleData")
public class VehicleDataVo implements Serializable{
	private static final long serialVersionUID = 1L;
	 /**
	  * 损失车辆号牌号码
	  */
	 @XStreamAlias("LicensePlateNo")
	 private String licensePlateNo;
	 /**
	  * 损失车辆号牌种类代码
	  */
	 @XStreamAlias("LicensePlateType")
	 private String licensePlateType;
	 /**
	  * 损失车辆VIN码
	  */
	 @XStreamAlias("VIN")
	 private String vin;
	 /**
	  * 损失车辆发动机号
	  */
	 @XStreamAlias("EngineNo")
	 private String engineNo;
	 /**
	  * 损失车辆车辆型号
	  */
	 @XStreamAlias("Model")
	 private String Model;
	 /**
	  * 出险车辆驾驶员姓名
	  */
	 @XStreamAlias("DriverName")
	 private String driverName;
	 /**
	  * 出险驾驶员证件类型
	  */
	 @XStreamAlias("CertiType")
	 private String certiType;
	 /**
	  * 出险驾驶员证件号码
	  */
	 @XStreamAlias("CertiCode")
	 private String certiCode;
	 
	 /**
	  * 出险车辆驾驶证号码
	  */
	 @XStreamAlias("DriverLicenseNo")
	 private String driverLicenseNo;
	 /**
	  * 核损金额
	  */
	 @XStreamAlias("UnderDefLoss")
	 private String underDefLoss;
	 /**
	  * 车辆属性
	  */
	 @XStreamAlias("VehicleProperty")
	 private String vehicleProperty;
	 /**
	  * 是否盗抢
	  */
	 @XStreamAlias("IsRobber")
	 private String isRobber;
	 /**
	  * 现场类别
	  */
     @XStreamAlias("FieldType")
     private String fieldType;
     /**
      * 定损人员姓名
      */
     @XStreamAlias("EstimateName")
     private String estimateName;
     /**
      * 定损人员代码
      */
     @XStreamAlias("EstimateCode")
     private String estimateCode;
     
     /**
      * 定损人员身份证号码
      */
     @XStreamAlias("EstimateCertiCode")
     private String estimateCertiCode;
     /**
      * 核损人员姓名
      */
     @XStreamAlias("UnderWriteName")
     private String underWriteName;
     /**
      * 核损人员代码
      */
     @XStreamAlias("UnderWriteCode")
     private String UnderWriteCode;
     /**
      * 核损人员身份证号码
      */
     @XStreamAlias("UnderWriteCertiCode")
     private String underWriteCertiCode;
     /**
      *  车辆损失定损开始时间 ；精确到分钟
      */
     @XStreamAlias("EstimateStartTime")
     private String estimateStartTime;
	/**
	 * 车辆损失核损结束时间 ；精确到分钟
	 */
     @XStreamAlias("UnderEndTime")
     private String underEndTime;
     
     /**
      * 定损地点
      */
     @XStreamAlias("EstimateAddr")
     private String estimateAddr;
     
     /**
      * 残值回收预估金额
      */
     @XStreamAlias("Remnant")
     private String remnant;
     
     /**
      * 配件总工时
      */
     @XStreamAlias("TotalManHour")
     private String totalManHour;
     
     /**
      * 是否修理或更换配件 ,0 否 ,1 是
      */
     @XStreamAlias("IsChangeOrRepair")
     private String isChangeOrRepair;
     
     /**
      * 修理机构名称
      */
     @XStreamAlias("RepairFactoryName")
     private String repairFactoryName;
     
     /**
      * 修理机构组织机构代码
      */
     @XStreamAlias("RepairFactoryCertiCode")
     private String repairFactoryCertiCode;
     
     /**
      * 修理机构类型；参见代码待定
      */
     @XStreamAlias("RepairFactoryType")
     private String repairFactoryType;
     
     /**
      * 是否全损
      */
     @XStreamAlias("IsTotalLoss")
     private String isTotalLoss;
     
     /**
      * 是否玻璃单独破碎
      */
     @XStreamAlias("IsGlassBroken")
     private String isGlassBroken;
     
     /**
      * 是否属于无法找到第三方
      */
     @XStreamAlias("IsNotFindThird")
     private String isNotFindThird;
     
     /**
      * 车辆损失部位列表
      */
     private List<LossPartDataVo> lossPartDataList;
     
     /**
      * 车辆配件明细列表
      */
     private List<FittingDataVo> fittingDataList;
     
    

	public String getEngineNo() {
		return engineNo;
	}

	public void setEngineNo(String engineNo) {
		this.engineNo = engineNo;
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

	public String getIsRobber() {
		return isRobber;
	}

	public void setIsRobber(String isRobber) {
		this.isRobber = isRobber;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public String getEstimateName() {
		return estimateName;
	}

	public void setEstimateName(String estimateName) {
		this.estimateName = estimateName;
	}

	public String getEstimateCode() {
		return estimateCode;
	}

	public void setEstimateCode(String estimateCode) {
		this.estimateCode = estimateCode;
	}

	public String getEstimateCertiCode() {
		return estimateCertiCode;
	}

	public void setEstimateCertiCode(String estimateCertiCode) {
		this.estimateCertiCode = estimateCertiCode;
	}

	public String getEstimateStartTime() {
		return estimateStartTime;
	}

	public void setEstimateStartTime(String estimateStartTime) {
		this.estimateStartTime = estimateStartTime;
	}

	public String getEstimateAddr() {
		return estimateAddr;
	}

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

	public String getModel() {
		return Model;
	}

	public void setModel(String model) {
		Model = model;
	}

	public String getUnderDefLoss() {
		return underDefLoss;
	}

	public void setUnderDefLoss(String underDefLoss) {
		this.underDefLoss = underDefLoss;
	}

	public String getVehicleProperty() {
		return vehicleProperty;
	}

	public void setVehicleProperty(String vehicleProperty) {
		this.vehicleProperty = vehicleProperty;
	}

	public String getUnderWriteName() {
		return underWriteName;
	}

	public void setUnderWriteName(String underWriteName) {
		this.underWriteName = underWriteName;
	}

	public String getUnderWriteCode() {
		return UnderWriteCode;
	}

	public void setUnderWriteCode(String underWriteCode) {
		UnderWriteCode = underWriteCode;
	}

	public String getUnderWriteCertiCode() {
		return underWriteCertiCode;
	}

	public void setUnderWriteCertiCode(String underWriteCertiCode) {
		this.underWriteCertiCode = underWriteCertiCode;
	}

	public String getUnderEndTime() {
		return underEndTime;
	}

	public void setUnderEndTime(String underEndTime) {
		this.underEndTime = underEndTime;
	}

	public String getRemnant() {
		return remnant;
	}

	public void setRemnant(String remnant) {
		this.remnant = remnant;
	}

	public String getTotalManHour() {
		return totalManHour;
	}

	public void setTotalManHour(String totalManHour) {
		this.totalManHour = totalManHour;
	}

	public String getRepairFactoryName() {
		return repairFactoryName;
	}

	public void setRepairFactoryName(String repairFactoryName) {
		this.repairFactoryName = repairFactoryName;
	}

	public String getRepairFactoryCertiCode() {
		return repairFactoryCertiCode;
	}

	public void setRepairFactoryCertiCode(String repairFactoryCertiCode) {
		this.repairFactoryCertiCode = repairFactoryCertiCode;
	}

	public String getRepairFactoryType() {
		return repairFactoryType;
	}

	public void setRepairFactoryType(String repairFactoryType) {
		this.repairFactoryType = repairFactoryType;
	}

	public String getIsTotalLoss() {
		return isTotalLoss;
	}

	public void setIsTotalLoss(String isTotalLoss) {
		this.isTotalLoss = isTotalLoss;
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

	public List<LossPartDataVo> getLossPartDataList() {
		return lossPartDataList;
	}

	public void setLossPartDataList(List<LossPartDataVo> lossPartDataList) {
		this.lossPartDataList = lossPartDataList;
	}

	public List<FittingDataVo> getFittingDataList() {
		return fittingDataList;
	}

	public void setFittingDataList(List<FittingDataVo> fittingDataList) {
		this.fittingDataList = fittingDataList;
	}

	public void setEstimateAddr(String estimateAddr) {
		this.estimateAddr = estimateAddr;
	}

	public String getIsChangeOrRepair() {
		return isChangeOrRepair;
	}

	public void setIsChangeOrRepair(String isChangeOrRepair) {
		this.isChangeOrRepair = isChangeOrRepair;
	}
     
     
}
