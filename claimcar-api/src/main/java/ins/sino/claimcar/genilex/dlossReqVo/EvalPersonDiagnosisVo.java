package ins.sino.claimcar.genilex.dlossReqVo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("EvalPersonDiagnosis")
public class EvalPersonDiagnosisVo implements Serializable{
	private static final long serialVersionUID = 1L;
@XStreamAlias("EvalID")
private String evalID;//定核损ID
@XStreamAlias("Diagnosisid")
private String diagnosisid;//伤情诊断ID
@XStreamAlias("PersonNo")
private String personNo;//人员序号
@XStreamAlias("DiagnosisName")
private String diagnosisName;//诊断名称		
@XStreamAlias("SupplementaryDiagnosis")
private String supplementaryDiagnosis;//补充诊断	
@XStreamAlias("InjurySite")
private String injurySite;//损伤部位			
@XStreamAlias("Measure")
private String measure;//治疗措施		
@XStreamAlias("CreateBy")
private String createBy;//创建者		
@XStreamAlias("CreateTime")
private String createTime;//创建日期			
@XStreamAlias("UpdateBy")
private String updateBy;//更新者		
@XStreamAlias("UpdateTime")
private String updateTime;//更新日期
public String getEvalID() {
	return evalID;
}
public void setEvalID(String evalID) {
	this.evalID = evalID;
}
public String getDiagnosisid() {
	return diagnosisid;
}
public void setDiagnosisid(String diagnosisid) {
	this.diagnosisid = diagnosisid;
}
public String getPersonNo() {
	return personNo;
}
public void setPersonNo(String personNo) {
	this.personNo = personNo;
}
public String getDiagnosisName() {
	return diagnosisName;
}
public void setDiagnosisName(String diagnosisName) {
	this.diagnosisName = diagnosisName;
}
public String getSupplementaryDiagnosis() {
	return supplementaryDiagnosis;
}
public void setSupplementaryDiagnosis(String supplementaryDiagnosis) {
	this.supplementaryDiagnosis = supplementaryDiagnosis;
}
public String getInjurySite() {
	return injurySite;
}
public void setInjurySite(String injurySite) {
	this.injurySite = injurySite;
}
public String getMeasure() {
	return measure;
}
public void setMeasure(String measure) {
	this.measure = measure;
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
