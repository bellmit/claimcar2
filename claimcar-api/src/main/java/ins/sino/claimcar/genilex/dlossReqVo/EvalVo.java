package ins.sino.claimcar.genilex.dlossReqVo;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Eval")
public class EvalVo implements Serializable{
	private static final long serialVersionUID = 1L;
@XStreamAlias("ReportNo")
private String reportNo;//报案号
@XStreamAlias("isCompleteEval")
private String isCompleteEval;//是否全部定损完成
@XStreamAlias("EvalVehicles")
private List<EvalVehicleVo> evalVehicles;//车辆损失情况列表
@XStreamAlias("EvalProtects")
private List<EvalProtectVo> evalProtects;//财产损失情况列表
@XStreamAlias("EvalPersons")
private List<EvalPersonVo> EvalPersons;//人员伤亡情况列表
public String getReportNo() {
	return reportNo;
}
public void setReportNo(String reportNo) {
	this.reportNo = reportNo;
}
public String getIsCompleteEval() {
	return isCompleteEval;
}
public void setIsCompleteEval(String isCompleteEval) {
	this.isCompleteEval = isCompleteEval;
}
public List<EvalVehicleVo> getEvalVehicles() {
	return evalVehicles;
}
public void setEvalVehicles(List<EvalVehicleVo> evalVehicles) {
	this.evalVehicles = evalVehicles;
}
public List<EvalProtectVo> getEvalProtects() {
	return evalProtects;
}
public void setEvalProtects(List<EvalProtectVo> evalProtects) {
	this.evalProtects = evalProtects;
}
public List<EvalPersonVo> getEvalPersons() {
	return EvalPersons;
}
public void setEvalPersons(List<EvalPersonVo> evalPersons) {
	EvalPersons = evalPersons;
}
 
	 
	

  

}
