package ins.sino.claimcar.genilex.dlossReqVo;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("EvalPerson")
public class EvalPersonVo implements Serializable{
	private static final long serialVersionUID = 1L;
@XStreamAlias("EvalID")
 private String evalID;//定核损ID
 @XStreamAlias("PersonID")
 private String personID;//伤亡人员编号			
 @XStreamAlias("TaskStatus")
 private String taskStatus;//任务状态			
 @XStreamAlias("LossType")
 private String lossType;//损失类型			
 @XStreamAlias("LossDetailType")
 private String lossDetailType;//损失类型细分				
 @XStreamAlias("PersonName")
 private String personName;//伤亡人员姓名
 @XStreamAlias("CertiType")
 private String certiType;//伤亡人员证件类型
 @XStreamAlias("CertiCode")
 private String certiCode;//伤亡人员证件号码		
 @XStreamAlias("PersonProperty")
 private String personProperty;//人员属性				
 @XStreamAlias("MedicalType")
 private String medicalType;//伤亡人员医疗类型			
 @XStreamAlias("AddmissionTime")
 private String addmissionTime;//入院时间			
 @XStreamAlias("InjuryType")
 private String injuryType;//伤情类别			
 @XStreamAlias("InjuryLevel")
 private String injuryLevel;//伤残程度			
 @XStreamAlias("MedicUnderDefLoss")
 private String medicUnderDefLoss;//医疗审核金额		
 @XStreamAlias("MedicEstimateName")
 private String medicEstimateName;//人伤跟踪人员姓名			
 @XStreamAlias("MedicEstimateCode")
 private String medicEstimateCode;//人伤跟踪人员代码			
 @XStreamAlias("MedicUnderWriteName")
 private String medicUnderWriteName;//医疗审核人员姓名				
 @XStreamAlias("MedicUnderWriteCode")
 private String medicUnderWriteCode;//医疗审核人员代码			
 @XStreamAlias("EstimateStartTime")
 private String estimateStartTime;//人伤跟踪开始时间	
 @XStreamAlias("UnderEndTime")
 private String underEndTime;//医疗审核完成时间				
 @XStreamAlias("EstimateAddr")
 private String estimateAddr;//医疗审核地点			
 @XStreamAlias("InjuredTypeCode")
 private String injuredTypeCode;//伤者类型			
 @XStreamAlias("InjuredGenderCode")
 private String injuredGenderCode;//伤者性别					
 @XStreamAlias("InjuredAge")
 private String injuredAge;//伤者年龄				
 @XStreamAlias("ResidentProvince")
 private String residentProvince;//户口所在省			
 @XStreamAlias("ResidentCity")
 private String residentCity;//户口所在市			
 @XStreamAlias("ResidentType")
 private String residentType;//户口类型				
 @XStreamAlias("LocalWorkTime")
 private String localWorkTime;//本地工作时间			
 @XStreamAlias("TradeName")
 private String tradeName;//所在行业			
 @XStreamAlias("InjuredWorkplace")
 private String injuredWorkplace;//伤者工作单位			    
 @XStreamAlias("DutyName")
 private String dutyName;//职务			
 @XStreamAlias("HasFixedIncome")
 private String hasFixedIncome;//有无固定收入			        
 @XStreamAlias("SalaryAmt")
 private String salaryAmt;//月平均收入			
 @XStreamAlias("FatalityInd")
 private String FatalityInd;//伤情判断			      
 @XStreamAlias("DiagnoseDesc")
 private String diagnoseDesc;//初步诊断		
 @XStreamAlias("EquipmentUsedDesc")
 private String equipmentUsedDesc;//医疗措施			
 @XStreamAlias("InHospitalDate")
 private String inHospitalDate;//就诊时间				
 @XStreamAlias("HospitalName")
 private String hospitalName;//就诊医院				
 @XStreamAlias("BedNo")
 private String bedNo;//科室床位			
 @XStreamAlias("PrincipalDoctorName")
 private String principalDoctorName;//主治医生				
 @XStreamAlias("DoctorName")
 private String doctorName;//访谈医生			
 @XStreamAlias("DoctorTel")
 private String doctorTel;//访谈医生联系电话				
 @XStreamAlias("NeedParamedicInd")
 private String needParamedicInd;//是否需要护理人员			
 @XStreamAlias("ParamedicNumber")
 private String paramedicNumber;//护理人员数			
 @XStreamAlias("ParamedicIdentity")
 private String paramedicIdentity;//护理人员身份					      
 @XStreamAlias("DisabledInd")
 private String disabledInd;//会否伤残			    
 @XStreamAlias("FatalityTypeCode")
 private String fatalityTypeCode;//伤残等级				
 @XStreamAlias("FamilyCase")
 private String familyCase;//家庭情况			  
 @XStreamAlias("FosterDesc")
 private String fosterDesc;//抚养义务				  
 @XStreamAlias("OtherCase")
 private String otherCase;//其它情况				  
 @XStreamAlias("EstimateCode")
 private String estimateCode;//定损人员代码				  
 @XStreamAlias("UnderWriteCode")
 private String underWriteCode;//核损人员代码			
 @XStreamAlias("Remark")
 private String remark;//备注			
 @XStreamAlias("CreateBy")
 private String createBy;//创建者			      
 @XStreamAlias("CreateTime")
 private String createTime;//创建日期				
 @XStreamAlias("UpdateBy")
 private String updateBy;//更新者			
 @XStreamAlias("UpdateTime")
 private String updateTime;//更新时间
 @XStreamAlias("PersonLossFees")
 private List<PersonLossFeeVo> personLossFees;//人员伤亡费用明细列表
 @XStreamAlias("EvalPersonInjurys")
 private List<EvalPersonInjuryVo> EvalPersonInjurys;//受伤情况列表
 @XStreamAlias("EvalPersonHospitals")
 private List<EvalPersonHospitalVo> evalPersonHospitals;//医疗机构列表列表
 @XStreamAlias("EvalPersonDiagnosiss")
 private List<EvalPersonDiagnosisVo> evalPersonDiagnosiss;//伤者诊断信息列表
public String getEvalID() {
	return evalID;
}
public void setEvalID(String evalID) {
	this.evalID = evalID;
}
public String getPersonID() {
	return personID;
}
public void setPersonID(String personID) {
	this.personID = personID;
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
public String getPersonName() {
	return personName;
}
public void setPersonName(String personName) {
	this.personName = personName;
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
public String getPersonProperty() {
	return personProperty;
}
public void setPersonProperty(String personProperty) {
	this.personProperty = personProperty;
}
public String getMedicalType() {
	return medicalType;
}
public void setMedicalType(String medicalType) {
	this.medicalType = medicalType;
}
public String getAddmissionTime() {
	return addmissionTime;
}
public void setAddmissionTime(String addmissionTime) {
	this.addmissionTime = addmissionTime;
}
public String getInjuryType() {
	return injuryType;
}
public void setInjuryType(String injuryType) {
	this.injuryType = injuryType;
}
public String getInjuryLevel() {
	return injuryLevel;
}
public void setInjuryLevel(String injuryLevel) {
	this.injuryLevel = injuryLevel;
}
public String getMedicUnderDefLoss() {
	return medicUnderDefLoss;
}
public void setMedicUnderDefLoss(String medicUnderDefLoss) {
	this.medicUnderDefLoss = medicUnderDefLoss;
}
public String getMedicEstimateName() {
	return medicEstimateName;
}
public void setMedicEstimateName(String medicEstimateName) {
	this.medicEstimateName = medicEstimateName;
}
public String getMedicEstimateCode() {
	return medicEstimateCode;
}
public void setMedicEstimateCode(String medicEstimateCode) {
	this.medicEstimateCode = medicEstimateCode;
}
public String getMedicUnderWriteName() {
	return medicUnderWriteName;
}
public void setMedicUnderWriteName(String medicUnderWriteName) {
	this.medicUnderWriteName = medicUnderWriteName;
}
public String getMedicUnderWriteCode() {
	return medicUnderWriteCode;
}
public void setMedicUnderWriteCode(String medicUnderWriteCode) {
	this.medicUnderWriteCode = medicUnderWriteCode;
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
public String getInjuredTypeCode() {
	return injuredTypeCode;
}
public void setInjuredTypeCode(String injuredTypeCode) {
	this.injuredTypeCode = injuredTypeCode;
}
public String getInjuredGenderCode() {
	return injuredGenderCode;
}
public void setInjuredGenderCode(String injuredGenderCode) {
	this.injuredGenderCode = injuredGenderCode;
}
public String getInjuredAge() {
	return injuredAge;
}
public void setInjuredAge(String injuredAge) {
	this.injuredAge = injuredAge;
}
public String getResidentProvince() {
	return residentProvince;
}
public void setResidentProvince(String residentProvince) {
	this.residentProvince = residentProvince;
}
public String getResidentCity() {
	return residentCity;
}
public void setResidentCity(String residentCity) {
	this.residentCity = residentCity;
}
public String getResidentType() {
	return residentType;
}
public void setResidentType(String residentType) {
	this.residentType = residentType;
}
public String getLocalWorkTime() {
	return localWorkTime;
}
public void setLocalWorkTime(String localWorkTime) {
	this.localWorkTime = localWorkTime;
}
public String getTradeName() {
	return tradeName;
}
public void setTradeName(String tradeName) {
	this.tradeName = tradeName;
}
public String getInjuredWorkplace() {
	return injuredWorkplace;
}
public void setInjuredWorkplace(String injuredWorkplace) {
	this.injuredWorkplace = injuredWorkplace;
}
public String getDutyName() {
	return dutyName;
}
public void setDutyName(String dutyName) {
	this.dutyName = dutyName;
}
public String getHasFixedIncome() {
	return hasFixedIncome;
}
public void setHasFixedIncome(String hasFixedIncome) {
	this.hasFixedIncome = hasFixedIncome;
}
public String getSalaryAmt() {
	return salaryAmt;
}
public void setSalaryAmt(String salaryAmt) {
	this.salaryAmt = salaryAmt;
}
public String getFatalityInd() {
	return FatalityInd;
}
public void setFatalityInd(String fatalityInd) {
	FatalityInd = fatalityInd;
}
public String getDiagnoseDesc() {
	return diagnoseDesc;
}
public void setDiagnoseDesc(String diagnoseDesc) {
	this.diagnoseDesc = diagnoseDesc;
}
public String getEquipmentUsedDesc() {
	return equipmentUsedDesc;
}
public void setEquipmentUsedDesc(String equipmentUsedDesc) {
	this.equipmentUsedDesc = equipmentUsedDesc;
}
public String getInHospitalDate() {
	return inHospitalDate;
}
public void setInHospitalDate(String inHospitalDate) {
	this.inHospitalDate = inHospitalDate;
}
public String getHospitalName() {
	return hospitalName;
}
public void setHospitalName(String hospitalName) {
	this.hospitalName = hospitalName;
}
public String getBedNo() {
	return bedNo;
}
public void setBedNo(String bedNo) {
	this.bedNo = bedNo;
}
public String getPrincipalDoctorName() {
	return principalDoctorName;
}
public void setPrincipalDoctorName(String principalDoctorName) {
	this.principalDoctorName = principalDoctorName;
}
public String getDoctorName() {
	return doctorName;
}
public void setDoctorName(String doctorName) {
	this.doctorName = doctorName;
}
public String getDoctorTel() {
	return doctorTel;
}
public void setDoctorTel(String doctorTel) {
	this.doctorTel = doctorTel;
}
public String getNeedParamedicInd() {
	return needParamedicInd;
}
public void setNeedParamedicInd(String needParamedicInd) {
	this.needParamedicInd = needParamedicInd;
}
public String getParamedicNumber() {
	return paramedicNumber;
}
public void setParamedicNumber(String paramedicNumber) {
	this.paramedicNumber = paramedicNumber;
}
public String getParamedicIdentity() {
	return paramedicIdentity;
}
public void setParamedicIdentity(String paramedicIdentity) {
	this.paramedicIdentity = paramedicIdentity;
}
public String getDisabledInd() {
	return disabledInd;
}
public void setDisabledInd(String disabledInd) {
	this.disabledInd = disabledInd;
}
public String getFatalityTypeCode() {
	return fatalityTypeCode;
}
public void setFatalityTypeCode(String fatalityTypeCode) {
	this.fatalityTypeCode = fatalityTypeCode;
}
public String getFamilyCase() {
	return familyCase;
}
public void setFamilyCase(String familyCase) {
	this.familyCase = familyCase;
}
public String getFosterDesc() {
	return fosterDesc;
}
public void setFosterDesc(String fosterDesc) {
	this.fosterDesc = fosterDesc;
}
public String getOtherCase() {
	return otherCase;
}
public void setOtherCase(String otherCase) {
	this.otherCase = otherCase;
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
public List<PersonLossFeeVo> getPersonLossFees() {
	return personLossFees;
}
public void setPersonLossFees(List<PersonLossFeeVo> personLossFees) {
	this.personLossFees = personLossFees;
}
public List<EvalPersonInjuryVo> getEvalPersonInjurys() {
	return EvalPersonInjurys;
}
public void setEvalPersonInjurys(List<EvalPersonInjuryVo> evalPersonInjurys) {
	EvalPersonInjurys = evalPersonInjurys;
}
public List<EvalPersonHospitalVo> getEvalPersonHospitals() {
	return evalPersonHospitals;
}
public void setEvalPersonHospitals(
		List<EvalPersonHospitalVo> evalPersonHospitals) {
	this.evalPersonHospitals = evalPersonHospitals;
}
public List<EvalPersonDiagnosisVo> getEvalPersonDiagnosiss() {
	return evalPersonDiagnosiss;
}
public void setEvalPersonDiagnosiss(
		List<EvalPersonDiagnosisVo> evalPersonDiagnosiss) {
	this.evalPersonDiagnosiss = evalPersonDiagnosiss;
}
		
 

}
