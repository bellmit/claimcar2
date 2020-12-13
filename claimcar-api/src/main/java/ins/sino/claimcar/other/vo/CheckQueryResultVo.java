package ins.sino.claimcar.other.vo;

import java.io.Serializable;
import java.util.Date;

public class CheckQueryResultVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String registNo;
	private String checkCode;
	private String checkName;
	private String insureName;
	private String insureCode;
	private String policyNo;
	private Date lossDate;
	private String taskDetail;
	private String acheckId;
	private String kindCode;
    private String taskType;
    private Date endCaseTime;
    private String checkFee;
    private String veriLoss;
    private String workStatus; //任务工作流状态
    private String claimNo;
    private String compensateNo;
    private long photoCount; 
    private String userName;
    private String isSurvey; //是否代查勘案件
    private Long mainId; //查勘、车损、财损、人伤主表id
	
	public Long getMainId() {
		return mainId;
	}
	
	public void setMainId(Long mainId) {
		this.mainId = mainId;
	}
	public String getRegistNo() {
		return registNo;
	}
	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}
	public String getCheckCode() {
		return checkCode;
	}
	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}
	public String getCheckName() {
		return checkName;
	}
	public void setCheckName(String checkName) {
		this.checkName = checkName;
	}
	public String getInsureName() {
		return insureName;
	}
	public void setInsureName(String insureName) {
		this.insureName = insureName;
	}
	public String getInsureCode() {
		return insureCode;
	}
	public void setInsureCode(String insureCode) {
		this.insureCode = insureCode;
	}
	public String getPolicyNo() {
		return policyNo;
	}
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}
	public Date getLossDate() {
		return lossDate;
	}
	public void setLossDate(Date lossDate) {
		this.lossDate = lossDate;
	}
	public String getTaskDetail() {
		return taskDetail;
	}
	public void setTaskDetail(String taskDetail) {
		this.taskDetail = taskDetail;
	}
	public String getAcheckId() {
		return acheckId;
	}
	public void setAcheckId(String acheckId) {
		this.acheckId = acheckId;
	}
	public String getKindCode() {
		return kindCode;
	}
	public void setKindCode(String kindCode) {
		this.kindCode = kindCode;
	}
	public String getTaskType() {
		return taskType;
	}
	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}
	public Date getEndCaseTime() {
		return endCaseTime;
	}
	public void setEndCaseTime(Date endCaseTime) {
		this.endCaseTime = endCaseTime;
	}
	 
	public String getCheckFee() {
		return checkFee;
	}
	public void setCheckFee(String checkFee) {
		this.checkFee = checkFee;
	}
	public String getVeriLoss() {
		return veriLoss;
	}
	public void setVeriLoss(String veriLoss) {
		this.veriLoss = veriLoss;
	}
	public String getWorkStatus() {
		return workStatus;
	}
	public void setWorkStatus(String workStatus) {
		this.workStatus = workStatus;
	}
	public String getClaimNo() {
		return claimNo;
	}
	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}
	public String getCompensateNo() {
		return compensateNo;
	}
	public void setCompensateNo(String compensateNo) {
		this.compensateNo = compensateNo;
	}
	public long getPhotoCount() {
		return photoCount;
	}
	public void setPhotoCount(long photoCount) {
		this.photoCount = photoCount;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getIsSurvey() {
		return isSurvey;
	}
	public void setIsSurvey(String isSurvey) {
		this.isSurvey = isSurvey;
	}

    
}
