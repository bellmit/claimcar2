package ins.sino.claimcar.trafficplatform.vo;


public class DispatchInfo implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private String reportNo;
	private String receiveTime;
	private String dispatchType;
	private String taskType;
	private String cancelReason;
	private String userId;
	private String surveyorCo;
	private String surveyorName;
	private String surveyorPhone;

	public String getReportNo() {
		return reportNo;
	}

	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}

	public String getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(String receiveTime) {
		this.receiveTime = receiveTime;
	}

	public String getDispatchType() {
		return dispatchType;
	}

	public void setDispatchType(String dispatchType) {
		this.dispatchType = dispatchType;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public String getCancelReason() {
		return cancelReason;
	}

	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSurveyorCo() {
		return surveyorCo;
	}

	public void setSurveyorCo(String surveyorCo) {
		this.surveyorCo = surveyorCo;
	}

	public String getSurveyorName() {
		return surveyorName;
	}

	public void setSurveyorName(String surveyorName) {
		this.surveyorName = surveyorName;
	}

	public String getSurveyorPhone() {
		return surveyorPhone;
	}

	public void setSurveyorPhone(String surveyorPhone) {
		this.surveyorPhone = surveyorPhone;
	}

}
