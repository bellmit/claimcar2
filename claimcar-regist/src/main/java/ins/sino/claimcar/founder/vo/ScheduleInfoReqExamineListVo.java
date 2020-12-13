package ins.sino.claimcar.founder.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 调度信息更新接口vo（理赔请求客服系统）
 * @author Luwei
 */
@XStreamAlias("Examine")
public class ScheduleInfoReqExamineListVo {

	/**
	 * 每个调度任务的唯一标示
	 */
	@XStreamAlias("ExamineId")
	private String examineId;

	/**
	 * 1、查勘调度2、标的定损调度3、三者定损调度4人伤定损调度 5财产定损调度
	 */
	@XStreamAlias("Type")
	private Integer type;

	/**
	 * 查勘处理机构码
	 */
	@XStreamAlias("ExamineDepartmentCode")
	private String examineDepartmentCode;

	/**
	 * 查勘处理机构
	 */
	@XStreamAlias("ExamineDepartmentName")
	private String examineDepartmentName;

	/**
	 * 查勘、定损地址
	 */
	@XStreamAlias("ExamineAddress")
	private String examineAddress;

	/**
	 * 查勘要点提示
	 */
	@XStreamAlias("ExaminePrompt")
	private String examinePrompt;

	/**
	 * 查勘人员码
	 */
	@XStreamAlias("ExamineCode")
	private String examineCode;

	/**
	 * 查勘人员
	 */
	@XStreamAlias("ExamineName")
	private String examineName;

	/**
	 * 损失金额
	 */
	@XStreamAlias("LossAmount")
	private Double lossAmount;

	/**
	 * 修理厂电话
	 */
	@XStreamAlias("Phone")
	private String phone;

	/**
	 * 案件状态
	 */
	@XStreamAlias("State")
	private String state;

	/**
	 * 通赔标志 0：否 1：是
	 */
	@XStreamAlias("Devolveflag")
	private boolean devolveflag;

	/**
	 * 调度座席ID，单点登录对应的客服座席工号
	 */
	@XStreamAlias("AgentId")
	private String agentId;

	/**
	 * 是否改派,是=1否= 0/null
	 */
	@XStreamAlias("IsReassignment")
	private String isReassignment;

	/**
	 * 客服系统电话标示
	 */
	@XStreamAlias("CallId")
	private String callId;
	
	/**
	 * 是否移动查勘1-是；0-否
	 */
	@XStreamAlias("IsSelf")
	private String isSelf;

	public String getExamineId() {
		return examineId;
	}

	public void setExamineId(String examineId) {
		this.examineId = examineId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getExamineDepartmentCode() {
		return examineDepartmentCode;
	}

	public void setExamineDepartmentCode(String examineDepartmentCode) {
		this.examineDepartmentCode = examineDepartmentCode;
	}

	public String getExamineDepartmentName() {
		return examineDepartmentName;
	}

	public void setExamineDepartmentName(String examineDepartmentName) {
		this.examineDepartmentName = examineDepartmentName;
	}

	public String getExamineAddress() {
		return examineAddress;
	}

	public void setExamineAddress(String examineAddress) {
		this.examineAddress = examineAddress;
	}

	public String getExaminePrompt() {
		return examinePrompt;
	}

	public void setExaminePrompt(String examinePrompt) {
		this.examinePrompt = examinePrompt;
	}

	public String getExamineCode() {
		return examineCode;
	}

	public void setExamineCode(String examineCode) {
		this.examineCode = examineCode;
	}

	public String getExamineName() {
		return examineName;
	}

	public void setExamineName(String examineName) {
		this.examineName = examineName;
	}

	public Double getLossAmount() {
		return lossAmount;
	}

	public void setLossAmount(Double lossAmount) {
		this.lossAmount = lossAmount;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public boolean isDevolveflag() {
		return devolveflag;
	}

	public void setDevolveflag(boolean devolveflag) {
		this.devolveflag = devolveflag;
	}

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public String getIsReassignment() {
		return isReassignment;
	}

	public void setIsReassignment(String isReassignment) {
		this.isReassignment = isReassignment;
	}

	public String getCallId() {
		return callId;
	}

	public void setCallId(String callId) {
		this.callId = callId;
	}
    
    public String getIsSelf() {
        return isSelf;
    }
    
    public void setIsSelf(String isSelf) {
        this.isSelf = isSelf;
    }
}
