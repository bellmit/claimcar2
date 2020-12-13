package ins.sino.claimcar.genilex.vo.endCase;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("CoverItem")
public class CoverItem implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("RecoveryOrPayFlag") 
	private String  recoveryOrPayFlag;
	@XStreamAlias("CoverageCode") 
	private String  coverageCode;
	@XStreamAlias("LossFeeType") 
	private String  lossFeeType;
	@XStreamAlias("LiabilityRate") 
	private String  liabilityRate;
	@XStreamAlias("TaskStatus") 
	private String  taskStatus;
	@XStreamAlias("ClaimAmount") 
	private String  claimAmount;
	@XStreamAlias("SalvageFee") 
	private String  salvageFee;
	@XStreamAlias("Remark") 
	private String  remark;
	@XStreamAlias("CreateBy") 
	private String  createBy;
	@XStreamAlias("CreateTime") 
	private String  createTime;
	@XStreamAlias("UpdateBy") 
	private String  updateBy;
	@XStreamAlias("UpdateTime") 
	private String  UpdateTime;
	public String getRecoveryOrPayFlag() {
		return recoveryOrPayFlag;
	}
	public void setRecoveryOrPayFlag(String recoveryOrPayFlag) {
		this.recoveryOrPayFlag = recoveryOrPayFlag;
	}
	public String getCoverageCode() {
		return coverageCode;
	}
	public void setCoverageCode(String coverageCode) {
		this.coverageCode = coverageCode;
	}
	public String getLossFeeType() {
		return lossFeeType;
	}
	public void setLossFeeType(String lossFeeType) {
		this.lossFeeType = lossFeeType;
	}
	public String getLiabilityRate() {
		return liabilityRate;
	}
	public void setLiabilityRate(String liabilityRate) {
		this.liabilityRate = liabilityRate;
	}
	public String getTaskStatus() {
		return taskStatus;
	}
	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}
	public String getClaimAmount() {
		return claimAmount;
	}
	public void setClaimAmount(String claimAmount) {
		this.claimAmount = claimAmount;
	}
	public String getSalvageFee() {
		return salvageFee;
	}
	public void setSalvageFee(String salvageFee) {
		this.salvageFee = salvageFee;
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
		return UpdateTime;
	}
	public void setUpdateTime(String updateTime) {
		UpdateTime = updateTime;
	}

	
	
}
