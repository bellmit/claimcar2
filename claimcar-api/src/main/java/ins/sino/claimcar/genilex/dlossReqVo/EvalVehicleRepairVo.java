package ins.sino.claimcar.genilex.dlossReqVo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("EvalVehicleRepair")
public class EvalVehicleRepairVo implements Serializable{
	private static final long serialVersionUID = 1L;
@XStreamAlias("EvalID")
private String evalID;//定核损ID
@XStreamAlias("ItemCode")
private String itemCode;//项目编码
@XStreamAlias("ItemName")
private String itemName;//项目名称		
@XStreamAlias("RepairName")
private String repairName;//修理项目名称	
@XStreamAlias("RepairCode")
private String repairCode;//修理编码		
@XStreamAlias("EvalManHour")
private String evalManHour;//定损工时				
@XStreamAlias("EvalHourSum")
private String evalHourSum;//定损工时金额			
@XStreamAlias("EvalRepairSum")
private String evalRepairSum;//定损金额		
@XStreamAlias("EvalHourSumFirst")
private String evalHourSumFirst;//首次定损工时金额		
@XStreamAlias("EstiManHour")
private String estiManHour;//核价工时		
@XStreamAlias("EstiHourSum")
private String estiHourSum;//核价工时金额			
@XStreamAlias("EstiRepairSum")
private String estiRepairSum;//核价金额	
@XStreamAlias("ApprHourSum")
private String apprHourSum;//核损工时金额		
@XStreamAlias("ApprRepairSum")
private String apprRepairSum;//核损金额
@XStreamAlias("RepairlocalPrice")
private String repairlocalPrice;//本地价格	
@XStreamAlias("ReferenceHour")
private String referenceHour;//参考工时		
@XStreamAlias("ReferencePrice")
private String referencePrice;//参考工时费		
@XStreamAlias("EvalFloatRatio")
private String evalFloatRatio;//管理费率		
@XStreamAlias("IsFullPaint")
private String isFullPaint;//是否是全车喷漆		
@XStreamAlias("IsSubjoin")
private String isSubjoin;//是否增补		
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
public String getItemCode() {
	return itemCode;
}
public void setItemCode(String itemCode) {
	this.itemCode = itemCode;
}
public String getItemName() {
	return itemName;
}
public void setItemName(String itemName) {
	this.itemName = itemName;
}
public String getRepairName() {
	return repairName;
}
public void setRepairName(String repairName) {
	this.repairName = repairName;
}
public String getRepairCode() {
	return repairCode;
}
public void setRepairCode(String repairCode) {
	this.repairCode = repairCode;
}
public String getEvalManHour() {
	return evalManHour;
}
public void setEvalManHour(String evalManHour) {
	this.evalManHour = evalManHour;
}
public String getEvalHourSum() {
	return evalHourSum;
}
public void setEvalHourSum(String evalHourSum) {
	this.evalHourSum = evalHourSum;
}
public String getEvalRepairSum() {
	return evalRepairSum;
}
public void setEvalRepairSum(String evalRepairSum) {
	this.evalRepairSum = evalRepairSum;
}
public String getEvalHourSumFirst() {
	return evalHourSumFirst;
}
public void setEvalHourSumFirst(String evalHourSumFirst) {
	this.evalHourSumFirst = evalHourSumFirst;
}
public String getEstiManHour() {
	return estiManHour;
}
public void setEstiManHour(String estiManHour) {
	this.estiManHour = estiManHour;
}
public String getEstiHourSum() {
	return estiHourSum;
}
public void setEstiHourSum(String estiHourSum) {
	this.estiHourSum = estiHourSum;
}
public String getEstiRepairSum() {
	return estiRepairSum;
}
public void setEstiRepairSum(String estiRepairSum) {
	this.estiRepairSum = estiRepairSum;
}
public String getApprHourSum() {
	return apprHourSum;
}
public void setApprHourSum(String apprHourSum) {
	this.apprHourSum = apprHourSum;
}
public String getApprRepairSum() {
	return apprRepairSum;
}
public void setApprRepairSum(String apprRepairSum) {
	this.apprRepairSum = apprRepairSum;
}
public String getRepairlocalPrice() {
	return repairlocalPrice;
}
public void setRepairlocalPrice(String repairlocalPrice) {
	this.repairlocalPrice = repairlocalPrice;
}
public String getReferenceHour() {
	return referenceHour;
}
public void setReferenceHour(String referenceHour) {
	this.referenceHour = referenceHour;
}
public String getReferencePrice() {
	return referencePrice;
}
public void setReferencePrice(String referencePrice) {
	this.referencePrice = referencePrice;
}
public String getEvalFloatRatio() {
	return evalFloatRatio;
}
public void setEvalFloatRatio(String evalFloatRatio) {
	this.evalFloatRatio = evalFloatRatio;
}
public String getIsFullPaint() {
	return isFullPaint;
}
public void setIsFullPaint(String isFullPaint) {
	this.isFullPaint = isFullPaint;
}
public String getIsSubjoin() {
	return isSubjoin;
}
public void setIsSubjoin(String isSubjoin) {
	this.isSubjoin = isSubjoin;
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
