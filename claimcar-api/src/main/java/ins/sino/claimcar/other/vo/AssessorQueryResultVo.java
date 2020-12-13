package ins.sino.claimcar.other.vo;

import java.util.Date;

public class AssessorQueryResultVo  implements java.io.Serializable {
	private static final long serialVersionUID = 1L;


	private String registNo;
	private String intermCode;
	private String intermName;
	private String insureName;
	private String insureCode;
	private String policyNo;
	private Date lossDate;
	private String taskDetail;
	private String assessorId;
	private String kindCode;
    private String taskType;
    private Date endCaseTime;
    private String assessorFee;
    private String veriLoss;
    private String workStatus; //任务工作流状态
    private String claimNo;
    private String compensateNo;
    private long photoCount; 
    private String userName;
    private String isSurvey; //是否代查勘案件
    
    /**  */
    private Long bussId;
 
    
    
	
	public Long getBussId() {
		return bussId;
	}

	
	public void setBussId(Long bussId) {
		this.bussId = bussId;
	}

	public String getWorkStatus() {
		return workStatus;
	}

	public void setWorkStatus(String workStatus) {
		this.workStatus = workStatus;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public String getRegistNo() {
		return registNo;
	}

	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}

	public String getIntermCode() {
		return intermCode;
	}

	public void setIntermCode(String intermCode) {
		this.intermCode = intermCode;
	}

	public String getIntermName() {
		return intermName;
	}

	public void setIntermName(String intermName) {
		this.intermName = intermName;
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

	public String getAssessorId() {
		return assessorId;
	}

	public void setAssessorId(String assessorId) {
		this.assessorId = assessorId;
	}

	
	public String getKindCode() {
		return kindCode;
	}

	
	public void setKindCode(String kindCode) {
		this.kindCode = kindCode;
	}

	public Date getEndCaseTime() {
		return endCaseTime;
	}

	public void setEndCaseTime(Date endCaseTime) {
		this.endCaseTime = endCaseTime;
	}

	public String getAssessorFee() {
		return assessorFee;
	}

	public void setAssessorFee(String assessorFee) {
		this.assessorFee = assessorFee;
	}

	public String getVeriLoss() {
		return veriLoss;
	}

	public void setVeriLoss(String veriLoss) {
		this.veriLoss = veriLoss;
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
