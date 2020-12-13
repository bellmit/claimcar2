package ins.sino.claimcar.genilex.dlossReqVo;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("EvalVehicle")
public class EvalVehicleVo implements Serializable{
	private static final long serialVersionUID = 1L;
	@XStreamAlias("EvalID")
	  private String evalID;//定核损ID
	  @XStreamAlias("LicensePlateNo")
	  private String licensePlateNo;//损失车辆号牌号码
	  @XStreamAlias("LicensePlateType")
	  private String licensePlateType;//损失车辆号牌种类代码
	  @XStreamAlias("EngineNo")
	  private String engineNo;//损失车辆发动机号
	  @XStreamAlias("VIN")
	  private String vin;//损失车辆VIN码
	  @XStreamAlias("Model")
	  private String model;//车辆型号
	  @XStreamAlias("DriverName")
	  private String driverName;//出险驾驶员姓名
	  @XStreamAlias("CertiType")
	  private String certiType;//出险驾驶员证件类型
	  @XStreamAlias("CertiCode")
	  private String certiCode;//出险驾驶员证件号码
	  @XStreamAlias("DriverLicenseNo")
	  private String driverLicenseNo;//出险车辆驾驶证号码
	  @XStreamAlias("LossDriverRelationTele")
	  private String lossDriverRelationTele;//出险驾驶员联系电话
	  @XStreamAlias("UnderDefLoss")
	  private String underDefLoss;//核损金额
	  @XStreamAlias("TaskStatus")
	  private String taskStatus;//任务状态
	  @XStreamAlias("LossType")
	  private String lossType;//损失类型
	  @XStreamAlias("LossDetailType")
	  private String lossDetailType;//损失类型细分
	  @XStreamAlias("VehicleProperty")
	  private String vehicleProperty;//车辆属性
	  @XStreamAlias("IsRobber")
	  private String isRobber;//是否盗抢
	  @XStreamAlias("FieldType")
	  private String fieldType;//现场类别
	  @XStreamAlias("EstimateCode")
	  private String estimateCode;//定损人员代码
	  @XStreamAlias("UnderWriteCode")
	  private String underWriteCode;//核损人员代码
	  @XStreamAlias("EstimateStartTime")
	  private String estimateStartTime;//车辆损失定损开始时间
	  @XStreamAlias("UnderEndTime")
	  private String underEndTime;//车辆损失核损结束时间
	  @XStreamAlias("EstimateAddr")
	  private String estimateAddr;//定损地点
	  @XStreamAlias("Remnant")
	  private String remnant;//残值回收预估金额
	  @XStreamAlias("TotalManHour")
	  private String totalManHour;//配件总工时
	  @XStreamAlias("IsChangeOrRepair")
	  private String isChangeOrRepair;//是否修理或更换配件
	  @XStreamAlias("RepairFactoryCode")
	  private String repairFactoryCode;//修理机构代码/定损修理厂
	  @XStreamAlias("RepairFactoryName")
	  private String repairFactoryName;//修理机构名称/定损修理厂
	  @XStreamAlias("RepairFactoryType")
	  private String repairFactoryType;//修理机构类型/定损修理厂
	  @XStreamAlias("RepairFactoryCertiCode")
	  private String repairFactoryCertiCode;//修理机构组织机构代码
	  @XStreamAlias("IsSingleAccident")
	  private String isSingleAccident;//是否单车事故
	  @XStreamAlias("IsAllFlag")
	  private String isAllFlag;//是否按全损或包干修复定损
	  @XStreamAlias("IsAllLossFlag")
	  private String isAllLossFlag;//是否按全损定损
	  @XStreamAlias("IsAllRepairFlag")
	  private String isAllRepairFlag;//是否按包干修复定损
	  @XStreamAlias("Actualamt")
	  private String actualamt;//车辆出险时实际价值
	  @XStreamAlias("Sumamt")
	  private String sumamt;//全损或包干修复定损金额合计
	  @XStreamAlias("DoSalvageQuoted")
	  private String doSalvageQuoted;//是否进行残值报价
	  @XStreamAlias("Normalamt")
	  private String normalamt;//正常定损金额
	  @XStreamAlias("SalvageQuotedAmt")
	  private String salvageQuotedAmt;//残值报价金额
	  @XStreamAlias("DoAuction")
	  private String doAuction;//是否进行拍卖处理
	  @XStreamAlias("AuctionAmt")
	  private String auctionAmt;//拍卖金额
	  @XStreamAlias("FittingsDataProvider")
	  private String fittingsDataProvider;//定型来源
	  @XStreamAlias("CarOwnerName")
	  private String carOwnerName;//车主姓名 
	  @XStreamAlias("BrandCode")
	  private String brandCode;//厂牌车型代码 
	  @XStreamAlias("BrandName")
	  private String brandName;//厂牌车型名称 
	  @XStreamAlias("FrameNo")
	  private String frameNo;//车架号
	  @XStreamAlias("JyCarId")
	  private String jyCarId;//精友临时定损车辆信息表主键
	  @XStreamAlias("JyBrandId")
	  private String jyBrandId;//精友配件平台车型主键
	  @XStreamAlias("VehicleCategory")
	  private String vehicleCategory;//车辆种类代码
	  @XStreamAlias("ThirdCompanyCode")
	  private String thirdcompanycode;//第三方保险公司代码
	  @XStreamAlias("ThirdPolicyNo")
	  private String thirdPolicyNo;//第三方保单号码
	  @XStreamAlias("ThirdIaPolicyNo")
	  private String thirdIaPolicyNo;//强制险保单号
	  @XStreamAlias("ThirdIaInsurerCode")
	  private String thirdIaInsurerCode;//交强险承保公司
	  @XStreamAlias("ThirdCaPolicyNo")
	  private String thirdCaPolicyNo;//商业险保单号
	  @XStreamAlias("ThirdCaInsurerCode")
	  private String thirdCaInsurerCode;//商业三者险承保公司
	  @XStreamAlias("ThirdIaInsurerArea")
	  private String thirdIaInsurerArea;//交强险承保地区
	  @XStreamAlias("ThirdCaInsurerArea")
	  private String thirdCaInsurerArea;//商业三者险承保地区
	  @XStreamAlias("ServicingInvoiceCode")
	  private String servicingInvoiceCode;//维修发票号码
	  @XStreamAlias("InvoiceAmornt")
	  private String invoiceAmornt;//维修发票金额
	  @XStreamAlias("DerogationType")
	  private String derogationType;//减损类型
	  @XStreamAlias("DerogationAmt")
	  private String derogationAmt;//减损金额
	  @XStreamAlias("Remark")
	  private String remark;//备注
	  @XStreamAlias("CreateBy")
	  private String createBy;//创建者
	  @XStreamAlias("CreateTime")
	  private String createTime;//创建日期
	  @XStreamAlias("UpdateBy")
	  private String updateBy;//创建日期
	  @XStreamAlias("UpdateTime")
	  private String updateTime;//创建日期
	  @XStreamAlias("EvalVehicleChangeParts")
	  private List<EvalVehicleChangePartVo> evalVehicleChangeParts; //车辆更换配件明细列表
	  @XStreamAlias("EvalVehicleRepairs")
	  private List<EvalVehicleRepairVo> evalVehicleRepairs; //车辆维修明细列表
	  @XStreamAlias("EvalVehicleLossParts")
	  private List<EvalVehicleLossPartVo> evalVehicleLossParts;//车辆损失部位列表
	  @XStreamAlias("EvalVehicleMaterials")
	  private List<EvalVehicleMaterialVo> evalVehicleMaterials;//车辆维修辅料费列表
	public String getEvalID() {
		return evalID;
	}
	public void setEvalID(String evalID) {
		this.evalID = evalID;
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
	public String getEngineNo() {
		return engineNo;
	}
	public void setEngineNo(String engineNo) {
		this.engineNo = engineNo;
	}
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
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
	public String getLossDriverRelationTele() {
		return lossDriverRelationTele;
	}
	public void setLossDriverRelationTele(String lossDriverRelationTele) {
		this.lossDriverRelationTele = lossDriverRelationTele;
	}
	public String getUnderDefLoss() {
		return underDefLoss;
	}
	public void setUnderDefLoss(String underDefLoss) {
		this.underDefLoss = underDefLoss;
	}
	public String getTaskStatus() {
		return taskStatus;
	}
	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}
	public String getLossType() {
		return lossType;
	}
	public void setLossType(String lossType) {
		this.lossType = lossType;
	}
	public String getLossDetailType() {
		return lossDetailType;
	}
	public void setLossDetailType(String lossDetailType) {
		this.lossDetailType = lossDetailType;
	}
	public String getVehicleProperty() {
		return vehicleProperty;
	}
	public void setVehicleProperty(String vehicleProperty) {
		this.vehicleProperty = vehicleProperty;
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
	public String getEstimateCode() {
		return estimateCode;
	}
	public void setEstimateCode(String estimateCode) {
		this.estimateCode = estimateCode;
	}
	public String getUnderWriteCode() {
		return underWriteCode;
	}
	public void setUnderWriteCode(String underWriteCode) {
		this.underWriteCode = underWriteCode;
	}
	public String getEstimateStartTime() {
		return estimateStartTime;
	}
	public void setEstimateStartTime(String estimateStartTime) {
		this.estimateStartTime = estimateStartTime;
	}
	public String getUnderEndTime() {
		return underEndTime;
	}
	public void setUnderEndTime(String underEndTime) {
		this.underEndTime = underEndTime;
	}
	public String getEstimateAddr() {
		return estimateAddr;
	}
	public void setEstimateAddr(String estimateAddr) {
		this.estimateAddr = estimateAddr;
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
	public String getIsChangeOrRepair() {
		return isChangeOrRepair;
	}
	public void setIsChangeOrRepair(String isChangeOrRepair) {
		this.isChangeOrRepair = isChangeOrRepair;
	}
	public String getRepairFactoryCode() {
		return repairFactoryCode;
	}
	public void setRepairFactoryCode(String repairFactoryCode) {
		this.repairFactoryCode = repairFactoryCode;
	}
	public String getRepairFactoryName() {
		return repairFactoryName;
	}
	public void setRepairFactoryName(String repairFactoryName) {
		this.repairFactoryName = repairFactoryName;
	}
	public String getRepairFactoryType() {
		return repairFactoryType;
	}
	public void setRepairFactoryType(String repairFactoryType) {
		this.repairFactoryType = repairFactoryType;
	}
	public String getRepairFactoryCertiCode() {
		return repairFactoryCertiCode;
	}
	public void setRepairFactoryCertiCode(String repairFactoryCertiCode) {
		this.repairFactoryCertiCode = repairFactoryCertiCode;
	}
	public String getIsSingleAccident() {
		return isSingleAccident;
	}
	public void setIsSingleAccident(String isSingleAccident) {
		this.isSingleAccident = isSingleAccident;
	}
	public String getIsAllFlag() {
		return isAllFlag;
	}
	public void setIsAllFlag(String isAllFlag) {
		this.isAllFlag = isAllFlag;
	}
	public String getIsAllLossFlag() {
		return isAllLossFlag;
	}
	public void setIsAllLossFlag(String isAllLossFlag) {
		this.isAllLossFlag = isAllLossFlag;
	}
	public String getIsAllRepairFlag() {
		return isAllRepairFlag;
	}
	public void setIsAllRepairFlag(String isAllRepairFlag) {
		this.isAllRepairFlag = isAllRepairFlag;
	}
	public String getActualamt() {
		return actualamt;
	}
	public void setActualamt(String actualamt) {
		this.actualamt = actualamt;
	}
	public String getSumamt() {
		return sumamt;
	}
	public void setSumamt(String sumamt) {
		this.sumamt = sumamt;
	}
	public String getDoSalvageQuoted() {
		return doSalvageQuoted;
	}
	public void setDoSalvageQuoted(String doSalvageQuoted) {
		this.doSalvageQuoted = doSalvageQuoted;
	}
	public String getNormalamt() {
		return normalamt;
	}
	public void setNormalamt(String normalamt) {
		this.normalamt = normalamt;
	}
	public String getSalvageQuotedAmt() {
		return salvageQuotedAmt;
	}
	public void setSalvageQuotedAmt(String salvageQuotedAmt) {
		this.salvageQuotedAmt = salvageQuotedAmt;
	}
	public String getDoAuction() {
		return doAuction;
	}
	public void setDoAuction(String doAuction) {
		this.doAuction = doAuction;
	}
	public String getAuctionAmt() {
		return auctionAmt;
	}
	public void setAuctionAmt(String auctionAmt) {
		this.auctionAmt = auctionAmt;
	}
	public String getFittingsDataProvider() {
		return fittingsDataProvider;
	}
	public void setFittingsDataProvider(String fittingsDataProvider) {
		this.fittingsDataProvider = fittingsDataProvider;
	}
	public String getCarOwnerName() {
		return carOwnerName;
	}
	public void setCarOwnerName(String carOwnerName) {
		this.carOwnerName = carOwnerName;
	}
	public String getBrandCode() {
		return brandCode;
	}
	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getFrameNo() {
		return frameNo;
	}
	public void setFrameNo(String frameNo) {
		this.frameNo = frameNo;
	}
	public String getJyCarId() {
		return jyCarId;
	}
	public void setJyCarId(String jyCarId) {
		this.jyCarId = jyCarId;
	}
	public String getJyBrandId() {
		return jyBrandId;
	}
	public void setJyBrandId(String jyBrandId) {
		this.jyBrandId = jyBrandId;
	}
	public String getVehicleCategory() {
		return vehicleCategory;
	}
	public void setVehicleCategory(String vehicleCategory) {
		this.vehicleCategory = vehicleCategory;
	}
	public String getThirdcompanycode() {
		return thirdcompanycode;
	}
	public void setThirdcompanycode(String thirdcompanycode) {
		this.thirdcompanycode = thirdcompanycode;
	}
	public String getThirdPolicyNo() {
		return thirdPolicyNo;
	}
	public void setThirdPolicyNo(String thirdPolicyNo) {
		this.thirdPolicyNo = thirdPolicyNo;
	}
	public String getThirdIaPolicyNo() {
		return thirdIaPolicyNo;
	}
	public void setThirdIaPolicyNo(String thirdIaPolicyNo) {
		this.thirdIaPolicyNo = thirdIaPolicyNo;
	}
	public String getThirdIaInsurerCode() {
		return thirdIaInsurerCode;
	}
	public void setThirdIaInsurerCode(String thirdIaInsurerCode) {
		this.thirdIaInsurerCode = thirdIaInsurerCode;
	}
	public String getThirdCaPolicyNo() {
		return thirdCaPolicyNo;
	}
	public void setThirdCaPolicyNo(String thirdCaPolicyNo) {
		this.thirdCaPolicyNo = thirdCaPolicyNo;
	}
	public String getThirdCaInsurerCode() {
		return thirdCaInsurerCode;
	}
	public void setThirdCaInsurerCode(String thirdCaInsurerCode) {
		this.thirdCaInsurerCode = thirdCaInsurerCode;
	}
	public String getThirdIaInsurerArea() {
		return thirdIaInsurerArea;
	}
	public void setThirdIaInsurerArea(String thirdIaInsurerArea) {
		this.thirdIaInsurerArea = thirdIaInsurerArea;
	}
	public String getThirdCaInsurerArea() {
		return thirdCaInsurerArea;
	}
	public void setThirdCaInsurerArea(String thirdCaInsurerArea) {
		this.thirdCaInsurerArea = thirdCaInsurerArea;
	}
	public String getServicingInvoiceCode() {
		return servicingInvoiceCode;
	}
	public void setServicingInvoiceCode(String servicingInvoiceCode) {
		this.servicingInvoiceCode = servicingInvoiceCode;
	}
	public String getInvoiceAmornt() {
		return invoiceAmornt;
	}
	public void setInvoiceAmornt(String invoiceAmornt) {
		this.invoiceAmornt = invoiceAmornt;
	}
	public String getDerogationType() {
		return derogationType;
	}
	public void setDerogationType(String derogationType) {
		this.derogationType = derogationType;
	}
	public String getDerogationAmt() {
		return derogationAmt;
	}
	public void setDerogationAmt(String derogationAmt) {
		this.derogationAmt = derogationAmt;
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
	public List<EvalVehicleChangePartVo> getEvalVehicleChangeParts() {
		return evalVehicleChangeParts;
	}
	public void setEvalVehicleChangeParts(
			List<EvalVehicleChangePartVo> evalVehicleChangeParts) {
		this.evalVehicleChangeParts = evalVehicleChangeParts;
	}
	public List<EvalVehicleRepairVo> getEvalVehicleRepairs() {
		return evalVehicleRepairs;
	}
	public void setEvalVehicleRepairs(List<EvalVehicleRepairVo> evalVehicleRepairs) {
		this.evalVehicleRepairs = evalVehicleRepairs;
	}
	public List<EvalVehicleLossPartVo> getEvalVehicleLossParts() {
		return evalVehicleLossParts;
	}
	public void setEvalVehicleLossParts(
			List<EvalVehicleLossPartVo> evalVehicleLossParts) {
		this.evalVehicleLossParts = evalVehicleLossParts;
	}
	public List<EvalVehicleMaterialVo> getEvalVehicleMaterials() {
		return evalVehicleMaterials;
	}
	public void setEvalVehicleMaterials(
			List<EvalVehicleMaterialVo> evalVehicleMaterials) {
		this.evalVehicleMaterials = evalVehicleMaterials;
	}
      
	  
}
