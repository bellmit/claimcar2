package ins.sino.claimcar.claim.vo;


/**
 * Custom VO class of PO PrpLcancelTrace
 */ 
public class PrpLcancelTraceVo extends PrpLcancelTraceVoBase implements java.io.Serializable {
	private static final long serialVersionUID = 1L; 
	private String flowTask;
	private double taskId;
	private String description;
	private String opinionName;
	private String opinionCode;
	private String registNo;
	private String remarks;
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	private double SumClaim;
	private String subNodeCode;
	private String FaQi;
	private String reasonCode;
	private String stationName;//岗位
	private String createUser; //操作人员
	public String getFlowTask() {
		return flowTask;
	}
	public void setFlowTask(String flowTask) {
		this.flowTask = flowTask;
	}
	public double getTaskId() {
		return taskId;
	}
	public void setTaskId(double taskId) {
		this.taskId = taskId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getOpinionName() {
		return opinionName;
	}
	public void setOpinionName(String opinionName) {
		this.opinionName = opinionName;
	}
	public String getOpinionCode() {
		return opinionCode;
	}
	public void setOpinionCode(String opinionCode) {
		this.opinionCode = opinionCode;
	}
	public String getRegistNo() {
		return registNo;
	}
	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}
	public double getSumClaim() {
		return SumClaim;
	}
	public void setSumClaim(double sumClaim) {
		SumClaim = sumClaim;
	}
	public String getSubNodeCode() {
		return subNodeCode;
	}
	public void setSubNodeCode(String subNodeCode) {
		this.subNodeCode = subNodeCode;
	}
	public String getFaQi() {
		return FaQi;
	}
	public void setFaQi(String faQi) {
		FaQi = faQi;
	}
	public String getReasonCode() {
		return reasonCode;
	}
	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}
	public String getStationName() {
		return stationName;
	}
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	
}
