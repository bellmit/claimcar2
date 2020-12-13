package ins.sino.claimcar.genilex.vo.check;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("SurveryVehicle")
public class SurveryVehicle implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("SurveryID") 
	private String  surveryID = "";
	@XStreamAlias("LicensePlateNo") 
	private String  licensePlateNo = "";
	@XStreamAlias("LicensePlateType") 
	private String  licensePlateType = "";
	@XStreamAlias("VIN") 
	private String  vin = "";
	@XStreamAlias("EngineNo") 
	private String  engineNo = "";
	@XStreamAlias("Model") 
	private String  model = "";
	@XStreamAlias("ModelName") 
	private String  modelName = "";
	@XStreamAlias("DriverName") 
	private String  driverName = "";
	@XStreamAlias("DriverLicenseNo") 
	private String  driverLicenseNo = "";
	@XStreamAlias("VehicleProperty") 
	private String  vehicleProperty = "";
	@XStreamAlias("FieldType") 
	private String  fieldType = "";
	@XStreamAlias("EstimatedLossAmount") 
	private String  estimatedLossAmount = "";
	@XStreamAlias("CheckerName") 
	private String  checkerName = "";
	@XStreamAlias("CheckerCode") 
	private String  checkerCode = "";
	@XStreamAlias("CheckerCertiCode") 
	private String  checkerCertiCode = "";
	@XStreamAlias("CheckStartTime") 
	private String  checkStartTime = "";
	@XStreamAlias("CheckEndTime") 
	private String  checkEndTime = "";
	@XStreamAlias("CheckAddr") 
	private String  checkAddr = "";
	@XStreamAlias("CheckDesc") 
	private String  checkDesc = "";
	@XStreamAlias("IncidentTypeCode") 
	private String  incidentTypeCode = "";
	@XStreamAlias("LossCauseCode") 
	private String  lossCauseCode = "";
	@XStreamAlias("LossTime") 
	private String  lossTime = "";
	@XStreamAlias("OcccurredProvince") 
	private String  occcurredProvince = "";
	@XStreamAlias("OcccurredCity") 
	private String  occcurredCity = "";
	@XStreamAlias("OcccurredRegion") 
	private String  occcurredRegion = "";
	@XStreamAlias("OcccurredCountryside") 
	private String  occcurredCountryside = "";
	@XStreamAlias("OccurrencyAreaCode") 
	private String  occurrencyAreaCode = "";
	@XStreamAlias("OccurrencyRoute") 
	private String  occurrencyRoute = "";
	@XStreamAlias("RunningBegin") 
	private String  runningBegin = "";
	@XStreamAlias("RunningEnd") 
	private String  runningEnd = "";
	@XStreamAlias("OcccurredSpotAttr") 
	private String  occcurredSpotAttr = "";
	@XStreamAlias("OcccurredSpot") 
	private String  occcurredSpot = "";
	@XStreamAlias("IncidentDesc") 
	private String  incidentDesc = "";
	@XStreamAlias("ResponsibleDepartmentCode") 
	private String  responsibleDepartmentCode = "";
	@XStreamAlias("DamageDesc") 
	private String  damageDesc = "";
	@XStreamAlias("InvestigationDepartmentCode") 
	private String  investigationDepartmentCode = "";
	@XStreamAlias("InvestigationTime")
	private String  investigationTime = "";
	@XStreamAlias("InvestigationOpinion") 
	private String  investigationOpinion = "";
	@XStreamAlias("AtfaultTypeCode") 
	private String  atfaultTypeCode = "";
	@XStreamAlias("AtfaultProportion") 
	private String  atfaultProportion = "";
	@XStreamAlias("PointConstableName") 
	private String  pointConstableName = "";
	@XStreamAlias("PointConstableTel") 
	private String  pointConstableTel = "";
	@XStreamAlias("ReInvestigateInd") 
	private String  reInvestigateInd = "";
	@XStreamAlias("AppointedInd") 
	private String  appointedInd = "";
	@XStreamAlias("PermissionInd") 
	private String  permissionInd = "";
	@XStreamAlias("ValidlicenseInd") 
	private String  validlicenseInd = "";
	@XStreamAlias("OverloadingInd") 
	private String  overloadingInd = "";
	@XStreamAlias("ExistingunrepaireddamageInd") 
	private String  existingunrepaireddamageInd = "";
	@XStreamAlias("MatchedareaInd") 
	private String  matchedareaInd = "";
	@XStreamAlias("HighriskcargoInd") 
	private String  highriskcargoInd = "";
	@XStreamAlias("PurchaseAgentName") 
	private String  purchaseAgentName = "";
	@XStreamAlias("PurchaseAgentSpot") 
	private String  purchaseAgentSpot = "";
	@XStreamAlias("PurchaseAgentTel") 
	private String  purchaseAgentTel = "";
	@XStreamAlias("CircumstantialEvidenceDesc") 
	private String  circumstantialEvidenceDesc = "";
	@XStreamAlias("RegisteredvehInd") 
	private String  registeredvehInd = "";
	@XStreamAlias("HasDrivinglicenceInd") 
	private String  hasDrivinglicenceInd = "";
	@XStreamAlias("HasTaxnivoiceInd") 
	private String  hasTaxnivoiceInd = "";
	@XStreamAlias("ParkingchargeInd") 
	private String  parkingchargeInd = "";
	@XStreamAlias("ParkingcredenceInd") 
	private String  parkingcredenceInd = "";
	@XStreamAlias("AntitheftdeviceInd") 
	private String  antitheftdeviceInd = "";
	@XStreamAlias("AntitheftlockInd") 
	private String  antitheftlockInd = "";
	@XStreamAlias("AntitheftlockType") 
	private String  antitheftlockType = "";
	@XStreamAlias("MaintainroadfeeDuration") 
	private String  maintainroadfeeDuration = "";
	@XStreamAlias("LosedOriginalkeyInd") 
	private String  losedOriginalkeyInd = "";
	@XStreamAlias("HasOriginalorderformInd") 
	private String  hasOriginalorderformInd = "";
	@XStreamAlias("PayorMissing") 
	private String  payorMissing = "";
	@XStreamAlias("HandleTime") 
	private String  handleTime = "";
	@XStreamAlias("CollidedInd") 
	private String  collidedInd = "";
	@XStreamAlias("AccidentResponsibleType") 
	private String  accidentResponsibleType = "";
	@XStreamAlias("AtfaultThirdVehicleInd") 
	private String  atfaultThirdVehicleInd = "";
	@XStreamAlias("ClaimChannelsType") 
	private String  claimChannelsType = "";
	@XStreamAlias("RelateInjureFlag") 
	private String  relateInjureFlag = "";
	@XStreamAlias("DifferentPlaceSurvyFlag") 
	private String  differentPlaceSurvyFlag = "";
	@XStreamAlias("InsuranceAssessSurvyFlag") 
	private String  insuranceAssessSurvyFlag = "";
	@XStreamAlias("QuicklyClaimFlag") 
	private String  quicklyClaimFlag = "";
	@XStreamAlias("Actiontype") 
	private String  actiontype = "";
	@XStreamAlias("LossTypeInd1") 
	private String  lossTypeInd1 = "";
	@XStreamAlias("LossTypeInd2") 
	private String  lossTypeInd2 = "";
	@XStreamAlias("LossTypeInd3") 
	private String  lossTypeInd3 = "";
	@XStreamAlias("LossTypeInd4") 
	private String  lossTypeInd4 = "";
	@XStreamAlias("IsSingleAccident") 
	private String  isSingleAccident = "";
	@XStreamAlias("Remark") 
	private String  remark = "";
	@XStreamAlias("CreateBy") 
	private String  createBy = "";
	@XStreamAlias("CreateTime") 
	private String  createTime = "";
	@XStreamAlias("UpdateBy") 
	private String  updateBy = "";
	@XStreamAlias("UpdateTime") 
	private String  updateTime = "";
	
	public String getSurveryID() {
		return surveryID;
	}
	public void setSurveryID(String surveryID) {
		this.surveryID = surveryID;
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
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
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
	public String getEstimatedLossAmount() {
		return estimatedLossAmount;
	}
	public void setEstimatedLossAmount(String estimatedLossAmount) {
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
	public String getIncidentTypeCode() {
		return incidentTypeCode;
	}
	public void setIncidentTypeCode(String incidentTypeCode) {
		this.incidentTypeCode = incidentTypeCode;
	}
	public String getLossCauseCode() {
		return lossCauseCode;
	}
	public void setLossCauseCode(String lossCauseCode) {
		this.lossCauseCode = lossCauseCode;
	}
	public String getLossTime() {
		return lossTime;
	}
	public void setLossTime(String lossTime) {
		this.lossTime = lossTime;
	}
	public String getOcccurredProvince() {
		return occcurredProvince;
	}
	public void setOcccurredProvince(String occcurredProvince) {
		this.occcurredProvince = occcurredProvince;
	}
	public String getOcccurredCity() {
		return occcurredCity;
	}
	public void setOcccurredCity(String occcurredCity) {
		this.occcurredCity = occcurredCity;
	}
	public String getOcccurredRegion() {
		return occcurredRegion;
	}
	public void setOcccurredRegion(String occcurredRegion) {
		this.occcurredRegion = occcurredRegion;
	}
	public String getOcccurredCountryside() {
		return occcurredCountryside;
	}
	public void setOcccurredCountryside(String occcurredCountryside) {
		this.occcurredCountryside = occcurredCountryside;
	}
	public String getOccurrencyAreaCode() {
		return occurrencyAreaCode;
	}
	public void setOccurrencyAreaCode(String occurrencyAreaCode) {
		this.occurrencyAreaCode = occurrencyAreaCode;
	}
	public String getOccurrencyRoute() {
		return occurrencyRoute;
	}
	public void setOccurrencyRoute(String occurrencyRoute) {
		this.occurrencyRoute = occurrencyRoute;
	}
	public String getRunningBegin() {
		return runningBegin;
	}
	public void setRunningBegin(String runningBegin) {
		this.runningBegin = runningBegin;
	}
	public String getRunningEnd() {
		return runningEnd;
	}
	public void setRunningEnd(String runningEnd) {
		this.runningEnd = runningEnd;
	}
	public String getOcccurredSpotAttr() {
		return occcurredSpotAttr;
	}
	public void setOcccurredSpotAttr(String occcurredSpotAttr) {
		this.occcurredSpotAttr = occcurredSpotAttr;
	}
	public String getOcccurredSpot() {
		return occcurredSpot;
	}
	public void setOcccurredSpot(String occcurredSpot) {
		this.occcurredSpot = occcurredSpot;
	}
	public String getIncidentDesc() {
		return incidentDesc;
	}
	public void setIncidentDesc(String incidentDesc) {
		this.incidentDesc = incidentDesc;
	}
	public String getResponsibleDepartmentCode() {
		return responsibleDepartmentCode;
	}
	public void setResponsibleDepartmentCode(String responsibleDepartmentCode) {
		this.responsibleDepartmentCode = responsibleDepartmentCode;
	}
	public String getDamageDesc() {
		return damageDesc;
	}
	public void setDamageDesc(String damageDesc) {
		this.damageDesc = damageDesc;
	}
	public String getInvestigationDepartmentCode() {
		return investigationDepartmentCode;
	}
	public void setInvestigationDepartmentCode(String investigationDepartmentCode) {
		this.investigationDepartmentCode = investigationDepartmentCode;
	}
	public String getInvestigationTime() {
		return investigationTime;
	}
	public void setInvestigationTime(String investigationTime) {
		this.investigationTime = investigationTime;
	}
	public String getInvestigationOpinion() {
		return investigationOpinion;
	}
	public void setInvestigationOpinion(String investigationOpinion) {
		this.investigationOpinion = investigationOpinion;
	}
	public String getAtfaultTypeCode() {
		return atfaultTypeCode;
	}
	public void setAtfaultTypeCode(String atfaultTypeCode) {
		this.atfaultTypeCode = atfaultTypeCode;
	}
	public String getAtfaultProportion() {
		return atfaultProportion;
	}
	public void setAtfaultProportion(String atfaultProportion) {
		this.atfaultProportion = atfaultProportion;
	}
	public String getPointConstableName() {
		return pointConstableName;
	}
	public void setPointConstableName(String pointConstableName) {
		this.pointConstableName = pointConstableName;
	}
	public String getPointConstableTel() {
		return pointConstableTel;
	}
	public void setPointConstableTel(String pointConstableTel) {
		this.pointConstableTel = pointConstableTel;
	}
	public String getReInvestigateInd() {
		return reInvestigateInd;
	}
	public void setReInvestigateInd(String reInvestigateInd) {
		this.reInvestigateInd = reInvestigateInd;
	}
	public String getAppointedInd() {
		return appointedInd;
	}
	public void setAppointedInd(String appointedInd) {
		this.appointedInd = appointedInd;
	}
	public String getPermissionInd() {
		return permissionInd;
	}
	public void setPermissionInd(String permissionInd) {
		this.permissionInd = permissionInd;
	}
	public String getValidlicenseInd() {
		return validlicenseInd;
	}
	public void setValidlicenseInd(String validlicenseInd) {
		this.validlicenseInd = validlicenseInd;
	}
	public String getOverloadingInd() {
		return overloadingInd;
	}
	public void setOverloadingInd(String overloadingInd) {
		this.overloadingInd = overloadingInd;
	}
	public String getExistingunrepaireddamageInd() {
		return existingunrepaireddamageInd;
	}
	public void setExistingunrepaireddamageInd(String existingunrepaireddamageInd) {
		this.existingunrepaireddamageInd = existingunrepaireddamageInd;
	}
	public String getMatchedareaInd() {
		return matchedareaInd;
	}
	public void setMatchedareaInd(String matchedareaInd) {
		this.matchedareaInd = matchedareaInd;
	}
	public String getHighriskcargoInd() {
		return highriskcargoInd;
	}
	public void setHighriskcargoInd(String highriskcargoInd) {
		this.highriskcargoInd = highriskcargoInd;
	}
	public String getPurchaseAgentName() {
		return purchaseAgentName;
	}
	public void setPurchaseAgentName(String purchaseAgentName) {
		this.purchaseAgentName = purchaseAgentName;
	}
	public String getPurchaseAgentSpot() {
		return purchaseAgentSpot;
	}
	public void setPurchaseAgentSpot(String purchaseAgentSpot) {
		this.purchaseAgentSpot = purchaseAgentSpot;
	}
	public String getPurchaseAgentTel() {
		return purchaseAgentTel;
	}
	public void setPurchaseAgentTel(String purchaseAgentTel) {
		this.purchaseAgentTel = purchaseAgentTel;
	}
	public String getCircumstantialEvidenceDesc() {
		return circumstantialEvidenceDesc;
	}
	public void setCircumstantialEvidenceDesc(String circumstantialEvidenceDesc) {
		this.circumstantialEvidenceDesc = circumstantialEvidenceDesc;
	}
	public String getRegisteredvehInd() {
		return registeredvehInd;
	}
	public void setRegisteredvehInd(String registeredvehInd) {
		this.registeredvehInd = registeredvehInd;
	}
	public String getHasDrivinglicenceInd() {
		return hasDrivinglicenceInd;
	}
	public void setHasDrivinglicenceInd(String hasDrivinglicenceInd) {
		this.hasDrivinglicenceInd = hasDrivinglicenceInd;
	}
	public String getHasTaxnivoiceInd() {
		return hasTaxnivoiceInd;
	}
	public void setHasTaxnivoiceInd(String hasTaxnivoiceInd) {
		this.hasTaxnivoiceInd = hasTaxnivoiceInd;
	}
	public String getParkingchargeInd() {
		return parkingchargeInd;
	}
	public void setParkingchargeInd(String parkingchargeInd) {
		this.parkingchargeInd = parkingchargeInd;
	}
	public String getParkingcredenceInd() {
		return parkingcredenceInd;
	}
	public void setParkingcredenceInd(String parkingcredenceInd) {
		this.parkingcredenceInd = parkingcredenceInd;
	}
	public String getAntitheftdeviceInd() {
		return antitheftdeviceInd;
	}
	public void setAntitheftdeviceInd(String antitheftdeviceInd) {
		this.antitheftdeviceInd = antitheftdeviceInd;
	}
	public String getAntitheftlockInd() {
		return antitheftlockInd;
	}
	public void setAntitheftlockInd(String antitheftlockInd) {
		this.antitheftlockInd = antitheftlockInd;
	}
	public String getAntitheftlockType() {
		return antitheftlockType;
	}
	public void setAntitheftlockType(String antitheftlockType) {
		this.antitheftlockType = antitheftlockType;
	}
	public String getMaintainroadfeeDuration() {
		return maintainroadfeeDuration;
	}
	public void setMaintainroadfeeDuration(String maintainroadfeeDuration) {
		this.maintainroadfeeDuration = maintainroadfeeDuration;
	}
	public String getLosedOriginalkeyInd() {
		return losedOriginalkeyInd;
	}
	public void setLosedOriginalkeyInd(String losedOriginalkeyInd) {
		this.losedOriginalkeyInd = losedOriginalkeyInd;
	}
	public String getHasOriginalorderformInd() {
		return hasOriginalorderformInd;
	}
	public void setHasOriginalorderformInd(String hasOriginalorderformInd) {
		this.hasOriginalorderformInd = hasOriginalorderformInd;
	}
	public String getPayorMissing() {
		return payorMissing;
	}
	public void setPayorMissing(String payorMissing) {
		this.payorMissing = payorMissing;
	}
	public String getHandleTime() {
		return handleTime;
	}
	public void setHandleTime(String handleTime) {
		this.handleTime = handleTime;
	}
	public String getCollidedInd() {
		return collidedInd;
	}
	public void setCollidedInd(String collidedInd) {
		this.collidedInd = collidedInd;
	}
	public String getAccidentResponsibleType() {
		return accidentResponsibleType;
	}
	public void setAccidentResponsibleType(String accidentResponsibleType) {
		this.accidentResponsibleType = accidentResponsibleType;
	}
	public String getAtfaultThirdVehicleInd() {
		return atfaultThirdVehicleInd;
	}
	public void setAtfaultThirdVehicleInd(String atfaultThirdVehicleInd) {
		this.atfaultThirdVehicleInd = atfaultThirdVehicleInd;
	}
	public String getClaimChannelsType() {
		return claimChannelsType;
	}
	public void setClaimChannelsType(String claimChannelsType) {
		this.claimChannelsType = claimChannelsType;
	}
	public String getRelateInjureFlag() {
		return relateInjureFlag;
	}
	public void setRelateInjureFlag(String relateInjureFlag) {
		this.relateInjureFlag = relateInjureFlag;
	}
	public String getDifferentPlaceSurvyFlag() {
		return differentPlaceSurvyFlag;
	}
	public void setDifferentPlaceSurvyFlag(String differentPlaceSurvyFlag) {
		this.differentPlaceSurvyFlag = differentPlaceSurvyFlag;
	}
	public String getQuicklyClaimFlag() {
		return quicklyClaimFlag;
	}
	public String getInsuranceAssessSurvyFlag() {
		return insuranceAssessSurvyFlag;
	}
	public void setInsuranceAssessSurvyFlag(String insuranceAssessSurvyFlag) {
		this.insuranceAssessSurvyFlag = insuranceAssessSurvyFlag;
	}
	public void setQuicklyClaimFlag(String quicklyClaimFlag) {
		this.quicklyClaimFlag = quicklyClaimFlag;
	}
	public String getActiontype() {
		return actiontype;
	}
	public void setActiontype(String actiontype) {
		this.actiontype = actiontype;
	}
	public String getLossTypeInd1() {
		return lossTypeInd1;
	}
	public void setLossTypeInd1(String lossTypeInd1) {
		this.lossTypeInd1 = lossTypeInd1;
	}
	public String getLossTypeInd2() {
		return lossTypeInd2;
	}
	public void setLossTypeInd2(String lossTypeInd2) {
		this.lossTypeInd2 = lossTypeInd2;
	}
	public String getLossTypeInd3() {
		return lossTypeInd3;
	}
	public void setLossTypeInd3(String lossTypeInd3) {
		this.lossTypeInd3 = lossTypeInd3;
	}
	public String getLossTypeInd4() {
		return lossTypeInd4;
	}
	public void setLossTypeInd4(String lossTypeInd4) {
		this.lossTypeInd4 = lossTypeInd4;
	}
	public String getIsSingleAccident() {
		return isSingleAccident;
	}
	public void setIsSingleAccident(String isSingleAccident) {
		this.isSingleAccident = isSingleAccident;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	
	
}
