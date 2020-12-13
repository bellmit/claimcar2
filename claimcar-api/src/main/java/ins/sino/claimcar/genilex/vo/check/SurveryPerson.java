package ins.sino.claimcar.genilex.vo.check;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("SurveryPerson")
public class SurveryPerson implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("SurveryID") 
	private String  surveryID;
	@XStreamAlias("SerialNo") 
	private String  serialNo;
	@XStreamAlias("PersonPayType") 
	private String  personPayType;
	@XStreamAlias("EstimatedLossAmount") 
	private String  estimatedLossAmount;
	@XStreamAlias("PersonProperty") 
	private String  personProperty;
	@XStreamAlias("TrafficType") 
	private String  trafficType;
	@XStreamAlias("CheckerName") 
	private String  checkerName;
	@XStreamAlias("CheckerCode") 
	private String  checkerCode;
	@XStreamAlias("CheckStartTime") 
	private String  checkStartTime;
	@XStreamAlias("CheckEndTime") 
	private String  checkEndTime;
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
	public String getPersonPayType() {
		return personPayType;
	}
	public void setPersonPayType(String personPayType) {
		this.personPayType = personPayType;
	}
	public String getEstimatedLossAmount() {
		return estimatedLossAmount;
	}
	public void setEstimatedLossAmount(String estimatedLossAmount) {
		this.estimatedLossAmount = estimatedLossAmount;
	}
	public String getPersonProperty() {
		return personProperty;
	}
	public void setPersonProperty(String personProperty) {
		this.personProperty = personProperty;
	}
	public String getTrafficType() {
		return trafficType;
	}
	public void setTrafficType(String trafficType) {
		this.trafficType = trafficType;
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
