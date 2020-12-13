package ins.sino.claimcar.interf.vo;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/*每一辆车的检测信息，可重复*/
@XStreamAlias("Claim")
public class ClaimVo {
	/*案件唯一标识*/
	@XStreamAlias("ClaimId")
	private String claimId;
	/*车牌号*/
	@XStreamAlias("VehicleRegistionNumber")
	private String vehicleRegistionNumber;
	/*是否主车，bool型*/
	@XStreamAlias("IsMainCar")
	private String isMainCar;
	/*品牌名称*/
	@XStreamAlias("VehicleBrandName")
	private String vehicleBrandName;
	/*车型名称*/
	@XStreamAlias("VehicleModelName")
	private String vehicleModelName;
	/*VIN码*/
	@XStreamAlias("VIN")
	private String vin;
	/*查勘员*/
	@XStreamAlias("CheckEmployee")
	private String checkEmployee;
	/*定损员*/
	@XStreamAlias("ConfirmLossEmployee")
	private String confirmLossEmployee;
	/*修理厂名称*/
	@XStreamAlias("RepairFactoryName")
	private String repairFactoryName;
	/*出险时间*/
	@XStreamAlias("EventDate")
	private String eventDate;
	/*定损时间*/
	@XStreamAlias("ConfirmLossDate")
	private String confirmLossDate;
	/*案件检测时间*/
	@XStreamAlias("CheckTime")
	private String checkTime;
	/*价格概述*/
	@XStreamAlias("PriceSummary")
	private PriceSummaryVo priceSummaryVo;
	/*配件列表*/
	@XStreamAlias("Parts")
	private List<PartVo> Parts;
	/*工时列表*/
	@XStreamAlias("Labors")
	private List<LaborResultVo> labors;
	/*欺诈风险列表*/
	@XStreamAlias("FraudRisks")
	private List<FraudRiskVo> fraudRisks;
	/*风险点提示列表*/
	@XStreamAlias("RiskPoints")
	private List<RiskPointVo> riskPoints;
	/*操作不规范提示列表*/
	@XStreamAlias("NonStandardOperations")
	private List<NonStandardOperationVo> nonStandardOperations;
	
	@XStreamAlias("VehicleID")
	private String vehicleID;
	
	@XStreamAlias("ClaimResult")
	private String claimResult;
    /*是否简易案件*/
	@XStreamAlias("IsSimpleClaim")
	private String isSimpleClaim;
	public String getClaimId() {
		return claimId;
	}
	public void setClaimId(String claimId) {
		this.claimId = claimId;
	}
	public String getVehicleRegistionNumber() {
		return vehicleRegistionNumber;
	}
	public void setVehicleRegistionNumber(String vehicleRegistionNumber) {
		this.vehicleRegistionNumber = vehicleRegistionNumber;
	}
	public String getIsMainCar() {
		return isMainCar;
	}
	public void setIsMainCar(String isMainCar) {
		this.isMainCar = isMainCar;
	}
	public String getVehicleBrandName() {
		return vehicleBrandName;
	}
	public void setVehicleBrandName(String vehicleBrandName) {
		this.vehicleBrandName = vehicleBrandName;
	}
	public String getVehicleModelName() {
		return vehicleModelName;
	}
	public void setVehicleModelName(String vehicleModelName) {
		this.vehicleModelName = vehicleModelName;
	}
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}
	public String getCheckEmployee() {
		return checkEmployee;
	}
	public void setCheckEmployee(String checkEmployee) {
		this.checkEmployee = checkEmployee;
	}
	public String getConfirmLossEmployee() {
		return confirmLossEmployee;
	}
	public void setConfirmLossEmployee(String confirmLossEmployee) {
		this.confirmLossEmployee = confirmLossEmployee;
	}
	public String getRepairFactoryName() {
		return repairFactoryName;
	}
	public void setRepairFactoryName(String repairFactoryName) {
		this.repairFactoryName = repairFactoryName;
	}
	public String getEventDate() {
		return eventDate;
	}
	public void setEventDate(String eventDate) {
		this.eventDate = eventDate;
	}
	public String getConfirmLossDate() {
		return confirmLossDate;
	}
	public void setConfirmLossDate(String confirmLossDate) {
		this.confirmLossDate = confirmLossDate;
	}
	public String getCheckTime() {
		return checkTime;
	}
	public void setCheckTime(String checkTime) {
		this.checkTime = checkTime;
	}
	public PriceSummaryVo getPriceSummaryVo() {
		return priceSummaryVo;
	}
	public void setPriceSummaryVo(PriceSummaryVo priceSummaryVo) {
		this.priceSummaryVo = priceSummaryVo;
	}
	public List<PartVo> getParts() {
		return Parts;
	}
	public void setParts(List<PartVo> parts) {
		Parts = parts;
	}
	public List<LaborResultVo> getLabors() {
		return labors;
	}
	public void setLabors(List<LaborResultVo> labors) {
		this.labors = labors;
	}
	public List<FraudRiskVo> getFraudRisks() {
		return fraudRisks;
	}
	public void setFraudRisks(List<FraudRiskVo> fraudRisks) {
		this.fraudRisks = fraudRisks;
	}
	public List<RiskPointVo> getRiskPoints() {
		return riskPoints;
	}
	public void setRiskPoints(List<RiskPointVo> riskPoints) {
		this.riskPoints = riskPoints;
	}
	public List<NonStandardOperationVo> getNonStandardOperations() {
		return nonStandardOperations;
	}
	public void setNonStandardOperations(
			List<NonStandardOperationVo> nonStandardOperations) {
		this.nonStandardOperations = nonStandardOperations;
	}
	
	
	public String getVehicleID() {
		return vehicleID;
	}
	public void setVehicleID(String vehicleID) {
		this.vehicleID = vehicleID;
	}
	public String getClaimResult() {
		return claimResult;
	}
	public void setClaimResult(String claimResult) {
		this.claimResult = claimResult;
	}
	public String getIsSimpleClaim() {
		return isSimpleClaim;
	}
	public void setIsSimpleClaim(String isSimpleClaim) {
		this.isSimpleClaim = isSimpleClaim;
	}
	
}
