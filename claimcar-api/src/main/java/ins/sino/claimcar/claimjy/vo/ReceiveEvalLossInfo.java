package ins.sino.claimcar.claimjy.vo;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "EvalLossInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class ReceiveEvalLossInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "DmgVhclId")
	private String dmgVhclId;//车损标的主键 理赔系统中车损标的主键
	@XmlElement(name = "ReportCode")
	private String reportCode;//报案号
	@XmlElement(name = "LossNo")
	private String lossNo;//定损单号 对应理赔系统查勘定损编号
	@XmlElement(name = "VehCertainCode")
	private String vehCertainCode;//定损车型编码
	@XmlElement(name = "VehBrandCode")
	private String vehBrandCode;//定损品牌编码
	@XmlElement(name = "BrandName")
	private String brandName;//定损品牌名称
	@XmlElement(name = "VehCertainName")
	private String vehCertainName;//定损车型名称
	@XmlElement(name = "VehGroupCode")
	private String vehGroupCode;//车组编码
	@XmlElement(name = "GroupName")
	private String groupName;//车组名称
	@XmlElement(name = "SelfConfigFlag")
	private String selfConfigFlag;//自定义车型 标志是/否 1/0自定义车型时，定损车型编码可以不传
	@XmlElement(name = "SalvageFee")
	private String salvageFee;//定损施救费用 手输回传SUM_MATERIAL_FEE
	@XmlElement(name = "RemnantFee")
	private String remnantFee;//定损折扣残值 手输回传SUM_REMNANT
	@XmlElement(name = "ManageFee")
	private String manageFee;//定损管理费合计
	@XmlElement(name = "EvalPartSum")
	private String evalPartSum;//定损换件合计
	@XmlElement(name = "EvalRepairSum")
	private String evalRepairSum;//定损工时合计
	@XmlElement(name = "EvalMateSum")
	private String evalMateSum;//定损辅料合计
	@XmlElement(name = "SelfPaySum")
	private String selfPaySum;//自付合计
	@XmlElement(name = "OuterSum")
	private String outerSum;//外修合计
	@XmlElement(name = "DerogationSum")
	private String derogationSum;//减损合计
	@XmlElement(name = "SumLossAmount")
	private String sumLossAmount;//定损合计 定损合计=换件+修理+辅料+管理费+施救-残值
	@XmlElement(name = "HandlerCode")
	private String handlerCode;//定损员代码
	@XmlElement(name = "Remark")
	private String remark;//备注 定损整单备注
	@XmlElement(name = "PriceType")
	private String priceType;//价格类型 1-4S价格 2-市场价格
	@XmlElement(name = "VehFactoryName")
	private String vehFactoryName;//制造厂名称
	@XmlElement(name = "RepairFacID")
	private String repairFacID;//修理厂ID
	@XmlElement(name = "RepairFacType")
	private String repairFacType;//修理厂类型 0-综合修理厂 1-是4s店修理厂
	@XmlElement(name = "RepairFacCode")
	private String repairFacCode;//修理厂编码
	@XmlElement(name = "RepairFacName")
	private String repairFacName;//修理厂名称
	@XmlElement(name = "PartDiscountPercent")
	private String partDiscountPercent;//换件折扣
	@XmlElement(name = "RepairDiscountPercent")
	private String RepairDiscountPercent;//工时折扣
	@XmlElement(name = "VinNo")
	private String vinNo;//VIN码
	@XmlElement(name = "EngineNo")
	private String engineNo;//发动机号
	@XmlElement(name = "PlateNo")
	private String plateNo;//车牌号
	@XmlElement(name = "EnrolDate")
	private String enrolDate;//初登日期
	@XmlElement(name = "SelfEstiFlag")
	private String selfEstiFlag;//自核价标记
	@XmlElement(name = "InsuranceCode")
	private String insuranceCode;//险别代码 整单险别时需要，分项险别不需要
	@XmlElement(name = "InsuranceName")
	private String insuranceName;//险别名称 整单险别时需要，分项险别不需要
	@XmlElement(name = "MixCode")
	private String mixCode;//组织机构代码  修理厂属性
	@XmlElement(name = "RepairFacPhone")
	private String repairFacPhone;//修理厂电话
	@XmlElement(name = "VehicleSettingMode")
	private String vehicleSettingMode;//定型方式1:承保车型匹配方式 2:VIN码定型方式3:智能定型方式4:模糊定型方式5:VIN码定型出车型，但无理赔车型6:自定义车型
	@XmlElement(name = "ModelMatchFlag")
	private String modelMatchFlag;//定损车型与承保车型是否匹配
	@XmlElement(name = "EvalTypeCode")
	private String evalTypeCode;//定损方式01修复定损02推定全损03实际全损04协议定损05法院判决
	@XmlElement(name = "AccidentCauseCode")
	private String accidentCauseCode;//事故类型1碰撞2涉水3火自爆4盗抢
	@XmlElement(name = "FactoryQualification")
	private String factoryQualification;//修理厂资质
	@XmlElement(name = "FixedLossSite")
	private String fixedLossSite;//定损地点R01:第一现场,R02:事故停车场,R03:修理厂,R04保险公司,R05:快赔中心
	@XmlElement(name = "PushRepair")
	private String pushRepair;//推送单位1:是，0:否
	@XmlElement(name = "EvalAllLoseSum")
	private String evalAllLoseSum;//定全损定损手输金额
	@XmlElement(name = "EvalAllRemnantFeeSum")
	private String evalAllRemnantFeeSum;//定全损定损残值金额
	@XmlElement(name = "EvalAllLoseSalvSum")
	private String evalAllLoseSalvSum;//定全损定损手输施救费金额
	@XmlElement(name = "EvalAllLoseTotalSum")
	private String evalAllLoseTotalSum;//定全损定损金额合计
	@XmlElement(name = "ClmTms")
	private String clmTms;//
	public String getDmgVhclId() {
		return dmgVhclId;
	}
	public void setDmgVhclId(String dmgVhclId) {
		this.dmgVhclId = dmgVhclId;
	}
	public String getReportCode() {
		return reportCode;
	}
	public void setReportCode(String reportCode) {
		this.reportCode = reportCode;
	}
	public String getLossNo() {
		return lossNo;
	}
	public void setLossNo(String lossNo) {
		this.lossNo = lossNo;
	}
	public String getVehCertainCode() {
		return vehCertainCode;
	}
	public void setVehCertainCode(String vehCertainCode) {
		this.vehCertainCode = vehCertainCode;
	}
	public String getVehBrandCode() {
		return vehBrandCode;
	}
	public void setVehBrandCode(String vehBrandCode) {
		this.vehBrandCode = vehBrandCode;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getVehCertainName() {
		return vehCertainName;
	}
	public void setVehCertainName(String vehCertainName) {
		this.vehCertainName = vehCertainName;
	}
	public String getVehGroupCode() {
		return vehGroupCode;
	}
	public void setVehGroupCode(String vehGroupCode) {
		this.vehGroupCode = vehGroupCode;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getSelfConfigFlag() {
		return selfConfigFlag;
	}
	public void setSelfConfigFlag(String selfConfigFlag) {
		this.selfConfigFlag = selfConfigFlag;
	}
	public String getSalvageFee() {
		return salvageFee;
	}
	public void setSalvageFee(String salvageFee) {
		this.salvageFee = salvageFee;
	}
	public String getRemnantFee() {
		return remnantFee;
	}
	public void setRemnantFee(String remnantFee) {
		this.remnantFee = remnantFee;
	}
	public String getManageFee() {
		return manageFee;
	}
	public void setManageFee(String manageFee) {
		this.manageFee = manageFee;
	}
	public String getEvalPartSum() {
		return evalPartSum;
	}
	public void setEvalPartSum(String evalPartSum) {
		this.evalPartSum = evalPartSum;
	}
	public String getEvalRepairSum() {
		return evalRepairSum;
	}
	public void setEvalRepairSum(String evalRepairSum) {
		this.evalRepairSum = evalRepairSum;
	}
	public String getEvalMateSum() {
		return evalMateSum;
	}
	public void setEvalMateSum(String evalMateSum) {
		this.evalMateSum = evalMateSum;
	}
	public String getSelfPaySum() {
		return selfPaySum;
	}
	public void setSelfPaySum(String selfPaySum) {
		this.selfPaySum = selfPaySum;
	}
	public String getOuterSum() {
		return outerSum;
	}
	public void setOuterSum(String outerSum) {
		this.outerSum = outerSum;
	}
	public String getDerogationSum() {
		return derogationSum;
	}
	public void setDerogationSum(String derogationSum) {
		this.derogationSum = derogationSum;
	}
	public String getSumLossAmount() {
		return sumLossAmount;
	}
	public void setSumLossAmount(String sumLossAmount) {
		this.sumLossAmount = sumLossAmount;
	}
	public String getHandlerCode() {
		return handlerCode;
	}
	public void setHandlerCode(String handlerCode) {
		this.handlerCode = handlerCode;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getPriceType() {
		return priceType;
	}
	public void setPriceType(String priceType) {
		this.priceType = priceType;
	}
	public String getVehFactoryName() {
		return vehFactoryName;
	}
	public void setVehFactoryName(String vehFactoryName) {
		this.vehFactoryName = vehFactoryName;
	}
	public String getRepairFacID() {
		return repairFacID;
	}
	public void setRepairFacID(String repairFacID) {
		this.repairFacID = repairFacID;
	}
	public String getRepairFacType() {
		return repairFacType;
	}
	public void setRepairFacType(String repairFacType) {
		this.repairFacType = repairFacType;
	}
	public String getRepairFacCode() {
		return repairFacCode;
	}
	public void setRepairFacCode(String repairFacCode) {
		this.repairFacCode = repairFacCode;
	}
	public String getRepairFacName() {
		return repairFacName;
	}
	public void setRepairFacName(String repairFacName) {
		this.repairFacName = repairFacName;
	}
	public String getPartDiscountPercent() {
		return partDiscountPercent;
	}
	public void setPartDiscountPercent(String partDiscountPercent) {
		this.partDiscountPercent = partDiscountPercent;
	}
	public String getRepairDiscountPercent() {
		return RepairDiscountPercent;
	}
	public void setRepairDiscountPercent(String repairDiscountPercent) {
		RepairDiscountPercent = repairDiscountPercent;
	}
	public String getVinNo() {
		return vinNo;
	}
	public void setVinNo(String vinNo) {
		this.vinNo = vinNo;
	}
	public String getEngineNo() {
		return engineNo;
	}
	public void setEngineNo(String engineNo) {
		this.engineNo = engineNo;
	}
	public String getPlateNo() {
		return plateNo;
	}
	public void setPlateNo(String plateNo) {
		this.plateNo = plateNo;
	}
	public String getEnrolDate() {
		return enrolDate;
	}
	public void setEnrolDate(String enrolDate) {
		this.enrolDate = enrolDate;
	}
	public String getSelfEstiFlag() {
		return selfEstiFlag;
	}
	public void setSelfEstiFlag(String selfEstiFlag) {
		this.selfEstiFlag = selfEstiFlag;
	}
	public String getInsuranceCode() {
		return insuranceCode;
	}
	public void setInsuranceCode(String insuranceCode) {
		this.insuranceCode = insuranceCode;
	}
	public String getInsuranceName() {
		return insuranceName;
	}
	public void setInsuranceName(String insuranceName) {
		this.insuranceName = insuranceName;
	}
	public String getMixCode() {
		return mixCode;
	}
	public void setMixCode(String mixCode) {
		this.mixCode = mixCode;
	}
	public String getRepairFacPhone() {
		return repairFacPhone;
	}
	public void setRepairFacPhone(String repairFacPhone) {
		this.repairFacPhone = repairFacPhone;
	}
	public String getVehicleSettingMode() {
		return vehicleSettingMode;
	}
	public void setVehicleSettingMode(String vehicleSettingMode) {
		this.vehicleSettingMode = vehicleSettingMode;
	}
	public String getModelMatchFlag() {
		return modelMatchFlag;
	}
	public void setModelMatchFlag(String modelMatchFlag) {
		this.modelMatchFlag = modelMatchFlag;
	}
	public String getEvalTypeCode() {
		return evalTypeCode;
	}
	public void setEvalTypeCode(String evalTypeCode) {
		this.evalTypeCode = evalTypeCode;
	}
	public String getAccidentCauseCode() {
		return accidentCauseCode;
	}
	public void setAccidentCauseCode(String accidentCauseCode) {
		this.accidentCauseCode = accidentCauseCode;
	}
	public String getFactoryQualification() {
		return factoryQualification;
	}
	public void setFactoryQualification(String factoryQualification) {
		this.factoryQualification = factoryQualification;
	}
	public String getFixedLossSite() {
		return fixedLossSite;
	}
	public void setFixedLossSite(String fixedLossSite) {
		this.fixedLossSite = fixedLossSite;
	}
	public String getPushRepair() {
		return pushRepair;
	}
	public void setPushRepair(String pushRepair) {
		this.pushRepair = pushRepair;
	}
	public String getEvalAllLoseSum() {
		return evalAllLoseSum;
	}
	public void setEvalAllLoseSum(String evalAllLoseSum) {
		this.evalAllLoseSum = evalAllLoseSum;
	}
	public String getEvalAllRemnantFeeSum() {
		return evalAllRemnantFeeSum;
	}
	public void setEvalAllRemnantFeeSum(String evalAllRemnantFeeSum) {
		this.evalAllRemnantFeeSum = evalAllRemnantFeeSum;
	}
	public String getEvalAllLoseSalvSum() {
		return evalAllLoseSalvSum;
	}
	public void setEvalAllLoseSalvSum(String evalAllLoseSalvSum) {
		this.evalAllLoseSalvSum = evalAllLoseSalvSum;
	}
	public String getEvalAllLoseTotalSum() {
		return evalAllLoseTotalSum;
	}
	public void setEvalAllLoseTotalSum(String evalAllLoseTotalSum) {
		this.evalAllLoseTotalSum = evalAllLoseTotalSum;
	}
	public String getClmTms() {
		return clmTms;
	}
	public void setClmTms(String clmTms) {
		this.clmTms = clmTms;
	}
}
