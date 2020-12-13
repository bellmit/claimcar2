package ins.sino.claimcar.genilex.vo.check;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("SurveryProtect")
public class SurveryProtect implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("SurveryID") 
	private String  surveryID;
	@XStreamAlias("SerialNo") 
	private String  serialNo;
	@XStreamAlias("ProtectName") 
	private String  protectName;
	@XStreamAlias("LossDesc") 
	private String  lossDesc;
	@XStreamAlias("LossNum") 
	private String  lossNum;
	@XStreamAlias("EstimatedLossAmount") 
	private String  estimatedLossAmount;
	@XStreamAlias("ProtectOwner") 
	private String  protectOwner;
	@XStreamAlias("ProtectProperty") 
	private String  protectProperty;
	@XStreamAlias("FieldType") 
	private String  fieldType;
	@XStreamAlias("CheckStartTime") 
	private String  checkStartTime;
	@XStreamAlias("CheckEndTime") 
	private String  checkEndTime;
	@XStreamAlias("CheckerName") 
	private String  checkerName;
	@XStreamAlias("CheckerCode") 
	private String  checkerCode;
	@XStreamAlias("CheckAddr") 
	private String  checkAddr;
	@XStreamAlias("CheckDesc") 
	private String  checkDesc;
	@XStreamAlias("CreateBy") 
	private String  createBy;
	@XStreamAlias("CreateTime") 
	private String  createTime;
	@XStreamAlias("UpdateBy") 
	private String  updateBy;
	@XStreamAlias("UpdateTime") 
	private String  updateTime;
	public String getSurveryID() {
		return surveryID;
	}
	public void setSurveryID(String surveryID) {
		this.surveryID = surveryID;
	}
	public String getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	public String getProtectName() {
		return protectName;
	}
	public void setProtectName(String protectName) {
		this.protectName = protectName;
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
	public String getEstimatedLossAmount() {
		return estimatedLossAmount;
	}
	public void setEstimatedLossAmount(String estimatedLossAmount) {
		this.estimatedLossAmount = estimatedLossAmount;
	}
	public String getProtectOwner() {
		return protectOwner;
	}
	public void setProtectOwner(String protectOwner) {
		this.protectOwner = protectOwner;
	}
	public String getProtectProperty() {
		return protectProperty;
	}
	public void setProtectProperty(String protectProperty) {
		this.protectProperty = protectProperty;
	}
	public String getFieldType() {
		return fieldType;
	}
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
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
