package ins.sino.claimcar.genilex.dlossReqVo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("EvalProtect")
public class EvalProtectVo implements Serializable{
	private static final long serialVersionUID = 1L;
	@XStreamAlias("EvalID")
	private String evalID;//定核损ID
	@XStreamAlias("ItemCode")
	private String itemCode;//项目编码	
	@XStreamAlias("ItemName")
	private String itemName;//项目名称		
	@XStreamAlias("LossDesc")
	private String lossDesc;//损失描述	
	@XStreamAlias("LossNum")
	private String lossNum;//损失数量			
	@XStreamAlias("LossAmt")
	private String lossAmt;//损失金额			
	@XStreamAlias("EvalAmt")
	private String evalAmt;//定损金额	
	@XStreamAlias("EstiAmt")
	private String estiAmt;//核价金额		
	@XStreamAlias("ApprAmt")
	private String apprAmt;//核损金额			
	@XStreamAlias("RemainsPrice")
	private String remainsPrice;//残值			
	@XStreamAlias("RecoveryFlag")
	private String recoveryFlag;//是否需回收标志		
	@XStreamAlias("Owner")
	private String owner;//所有人/管理人		
	@XStreamAlias("TaskStatus")
	private String taskStatus;//任务状态		
	@XStreamAlias("LossType")
	private String lossType;//损失类型		
	@XStreamAlias("LossDetailType")
	private String lossDetailType;//损失类型细分			
	@XStreamAlias("ProtectProperty")
	private String protectProperty;//财产属性		
	@XStreamAlias("EstimateCode")
	private String estimateCode;//定损人员代码		
	@XStreamAlias("UnderWriteCode")
	private String underWriteCode;//核损人员代码		
	@XStreamAlias("EstimateAddr")
	private String estimateAddr;//定损地点			
	@XStreamAlias("Remark")
	private String remark;//备注		
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
	public String getLossDesc() {
		return lossDesc;
	}
	public void setLossDesc(String lossDesc) {
		this.lossDesc = lossDesc;
	}
	public String getLossNum() {
		return lossNum;
	}
	public void setLossNum(String lossNum) {
		this.lossNum = lossNum;
	}
	public String getLossAmt() {
		return lossAmt;
	}
	public void setLossAmt(String lossAmt) {
		this.lossAmt = lossAmt;
	}
	public String getEvalAmt() {
		return evalAmt;
	}
	public void setEvalAmt(String evalAmt) {
		this.evalAmt = evalAmt;
	}
	public String getEstiAmt() {
		return estiAmt;
	}
	public void setEstiAmt(String estiAmt) {
		this.estiAmt = estiAmt;
	}
	public String getApprAmt() {
		return apprAmt;
	}
	public void setApprAmt(String apprAmt) {
		this.apprAmt = apprAmt;
	}
	public String getRemainsPrice() {
		return remainsPrice;
	}
	public void setRemainsPrice(String remainsPrice) {
		this.remainsPrice = remainsPrice;
	}
	public String getRecoveryFlag() {
		return recoveryFlag;
	}
	public void setRecoveryFlag(String recoveryFlag) {
		this.recoveryFlag = recoveryFlag;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
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
	public String getProtectProperty() {
		return protectProperty;
	}
	public void setProtectProperty(String protectProperty) {
		this.protectProperty = protectProperty;
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
	public String getEstimateAddr() {
		return estimateAddr;
	}
	public void setEstimateAddr(String estimateAddr) {
		this.estimateAddr = estimateAddr;
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
