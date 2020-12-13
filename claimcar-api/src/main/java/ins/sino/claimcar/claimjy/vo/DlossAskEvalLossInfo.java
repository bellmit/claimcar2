package ins.sino.claimcar.claimjy.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("EvalLossInfo")
public class DlossAskEvalLossInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	@XStreamAlias("DmgVhclId")
	private String dmgVhclId;//车损标的主键 理赔系统中车损标的主键
	@XStreamAlias("LossNo")
	private String lossNo;//定损单号 对应理赔系统查勘定损编号
	@XStreamAlias("ReportCode")
	private String reportCode;//报案号
	@XStreamAlias("PlateNo")
	private String plateNo;//车牌号
	@XStreamAlias("MarkColor")
	private String markColor;//车牌号
	@XStreamAlias("EngineNo")
	private String engineNo;//发动机号
	@XStreamAlias("VinNo")
	private String vinNo;//VIN码
	@XStreamAlias("InsureVehicleCode")
	private String insureVehicleCode = "";//承保车型编码
	@XStreamAlias("InsureVehicle")
	private String insureVehicle = "";//承保车型名称
	@XStreamAlias("ComCode")
	private String comCode;//定损员所属分公司代码
	@XStreamAlias("Company")
	private String company = "";//定损员所属分公司名称
	@XStreamAlias("BranchComCode")
	private String branchComCode;//定损员所属中支代码
	@XStreamAlias("BranchComName")
	private String branchComName = "";//定损员所属中支名称
	@XStreamAlias("EvalHandlerCode")
	private String evalHandlerCode;//定损员代码
	@XStreamAlias("EvalHandlerName")
	private String evalHandlerName;//定损员名称
	@XStreamAlias("DriverName")
	private String driverName;//驾驶员
	@XStreamAlias("IsSubjectVehicle")
	private String isSubjectVehicle = "0";//是否标的车 1-是 0-否
	@XStreamAlias("EnrolDate")
	private String enrolDate;//初登日期
	@XStreamAlias("EvalTypeCode")
	private String evalTypeCode = "01";//定损方式  01-修复定损[默认传01]02-推定全损 03-实际全损 04-协议定损
	@XStreamAlias("EvalRemark")
	private String evalRemark = "";//定损单备注
	@XStreamAlias("RepairFactoryID")
	private String repairFactoryID = "";//送修修理厂ID
	@XStreamAlias("RepairFactoryCode")
	private String RepairFactoryCode = "";//送修修理厂编码
	@XStreamAlias("RepairFactoryName")
	private String repairFactoryName = "";//送修修理厂名称
	@XStreamAlias("RepairFactoryType")
	private String repairFactoryType = "";//送修修理厂类型
	@XStreamAlias("ChangeCarFlag")
	private String changeCarFlag = "1";//是否可以换车 1-可以 0-不可以
	@XStreamAlias("CarKindCode")
	private String CarKindCode;//车辆种类
	
	
	public String getCarKindCode() {
		return CarKindCode;
	}
	
	public void setCarKindCode(String carKindCode) {
		CarKindCode = carKindCode;
	}
	public String getDmgVhclId() {
		return dmgVhclId;
	}
	public void setDmgVhclId(String dmgVhclId) {
		this.dmgVhclId = dmgVhclId;
	}
	public String getLossNo() {
		return lossNo;
	}
	public void setLossNo(String lossNo) {
		this.lossNo = lossNo;
	}
	public String getReportCode() {
		return reportCode;
	}
	public void setReportCode(String reportCode) {
		this.reportCode = reportCode;
	}
	public String getPlateNo() {
		return plateNo;
	}
	public void setPlateNo(String plateNo) {
		this.plateNo = plateNo;
	}
	public String getMarkColor() {
		return markColor;
	}
	public void setMarkColor(String markColor) {
		this.markColor = markColor;
	}
	public String getEngineNo() {
		return engineNo;
	}
	public void setEngineNo(String engineNo) {
		this.engineNo = engineNo;
	}
	public String getVinNo() {
		return vinNo;
	}
	public void setVinNo(String vinNo) {
		this.vinNo = vinNo;
	}
	public String getInsureVehicleCode() {
		return insureVehicleCode;
	}
	public void setInsureVehicleCode(String insureVehicleCode) {
		this.insureVehicleCode = insureVehicleCode;
	}
	public String getInsureVehicle() {
		return insureVehicle;
	}
	public void setInsureVehicle(String insureVehicle) {
		this.insureVehicle = insureVehicle;
	}
	public String getComCode() {
		return comCode;
	}
	public void setComCode(String comCode) {
		this.comCode = comCode;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getBranchComCode() {
		return branchComCode;
	}
	public void setBranchComCode(String branchComCode) {
		this.branchComCode = branchComCode;
	}
	public String getBranchComName() {
		return branchComName;
	}
	public void setBranchComName(String branchComName) {
		this.branchComName = branchComName;
	}
	public String getEvalHandlerCode() {
		return evalHandlerCode;
	}
	public void setEvalHandlerCode(String evalHandlerCode) {
		this.evalHandlerCode = evalHandlerCode;
	}
	public String getEvalHandlerName() {
		return evalHandlerName;
	}
	public void setEvalHandlerName(String evalHandlerName) {
		this.evalHandlerName = evalHandlerName;
	}
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	public String getIsSubjectVehicle() {
		return isSubjectVehicle;
	}
	public void setIsSubjectVehicle(String isSubjectVehicle) {
		this.isSubjectVehicle = isSubjectVehicle;
	}
	public String getEnrolDate() {
		return enrolDate;
	}
	public void setEnrolDate(String enrolDate) {
		this.enrolDate = enrolDate;
	}
	public String getEvalTypeCode() {
		return evalTypeCode;
	}
	public void setEvalTypeCode(String evalTypeCode) {
		this.evalTypeCode = evalTypeCode;
	}
	public String getEvalRemark() {
		return evalRemark;
	}
	public void setEvalRemark(String evalRemark) {
		this.evalRemark = evalRemark;
	}
	public String getRepairFactoryID() {
		return repairFactoryID;
	}
	public void setRepairFactoryID(String repairFactoryID) {
		this.repairFactoryID = repairFactoryID;
	}
	public String getRepairFactoryCode() {
		return RepairFactoryCode;
	}
	public void setRepairFactoryCode(String repairFactoryCode) {
		RepairFactoryCode = repairFactoryCode;
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
	public String getChangeCarFlag() {
		return changeCarFlag;
	}
	public void setChangeCarFlag(String changeCarFlag) {
		this.changeCarFlag = changeCarFlag;
	}
}
