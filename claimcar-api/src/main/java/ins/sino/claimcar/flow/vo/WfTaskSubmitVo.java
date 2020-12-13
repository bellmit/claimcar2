package ins.sino.claimcar.flow.vo;

import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.constant.SubmitType;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 提交工作流时使用的Vo
 * @author ★LiuPing
 * @CreateTime 2016年1月8日
 */
public class WfTaskSubmitVo implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// 必填参数
	/** 提交动作类型，默认为普通提交 */
	private SubmitType submitType = SubmitType.N;
	/** 工作流id */
	private String flowId;
	/** 当前的工作流TaskID，报案时为0 */
	private BigDecimal flowTaskId;
	/** 归属处理机构 */
	private String comCode;
	/** 提交人 */
	private String taskInUser;
	/** 进入的关键业务号 */
	private String taskInKey;
	/** 退回用到 */
	private String handleIdKey;

	// 可选参数
	/** 指定处理人 */
	private String assignUser;
	/** 处理人机构 */
	private String assignCom;

	// 定损，核损，核损特殊参数
	/** 当前节点 */
	private FlowNode currentNode;
	/** 下一个节点 */
	private FlowNode nextNode;
	/** 提交到第几级 */
	private Integer submitLevel;

	/** 其他周边节点，一个节点发起其他周边节点任务 */
	private FlowNode[] othenNodes;
	
	private String  handleruser;
	private Date handlertime;
	private String isMobileAccept; //是否为移动端处理
	private String subCheckFlag;//代查勘标识，0-正常案件，1-司内代查勘，2-公估代查勘，3-公估案件
	
	private String licenseNo;
	private String pingAnFlag="0";
	/**
	 * 案件状态
	 */
	private String flowStatus;

	public String getFlowId() {
		return flowId;
	}

	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}

	public SubmitType getSubmitType() {
		return submitType;
	}

	public void setSubmitType(SubmitType submitType) {
		this.submitType = submitType;
	}

	public Integer getSubmitLevel() {
		return submitLevel;
	}

	public void setSubmitLevel(Integer submitLevel) {
		this.submitLevel = submitLevel;
	}

	public String getTaskInUser() {
		return taskInUser;
	}

	public void setTaskInUser(String taskInUser) {
		this.taskInUser = taskInUser;
	}

	public String getTaskInKey() {
		return taskInKey;
	}

	public void setTaskInKey(String taskInKey) {
		this.taskInKey = taskInKey;
	}

	public String getAssignUser() {
		return assignUser;
	}

	public void setAssignUser(String assignUser) {
		this.assignUser = assignUser;
	}

	public String getAssignCom() {
		return assignCom;
	}

	public void setAssignCom(String assignCom) {
		this.assignCom = assignCom;
	}

	public BigDecimal getFlowTaskId() {
		return flowTaskId;
	}

	public void setFlowTaskId(BigDecimal flowTaskId) {
		this.flowTaskId = flowTaskId;
	}

	public String getComCode() {
		return comCode;
	}

	public void setComCode(String comCode) {
		this.comCode = comCode;
	}

	public FlowNode getCurrentNode() {
		return currentNode;
	}

	public void setCurrentNode(FlowNode currentNode) {
		this.currentNode = currentNode;
	}

	public FlowNode getNextNode() {
		return nextNode;
	}

	public void setNextNode(FlowNode nextNode) {
		this.nextNode = nextNode;
	}

	public FlowNode[] getOthenNodes() {
		return othenNodes;
	}

	public void setOthenNodes(FlowNode[] othenNodes) {
		this.othenNodes = othenNodes;
	}

	public String getHandleIdKey() {
		return handleIdKey;
	}

	public void setHandleIdKey(String handleIdKey) {
		this.handleIdKey = handleIdKey;
	}

	public String getHandleruser() {
		return handleruser;
	}

	public void setHandleruser(String handleruser) {
		this.handleruser = handleruser;
	}

	public Date getHandlertime() {
		return handlertime;
	}

	public void setHandlertime(Date handlertime) {
		this.handlertime = handlertime;
	}

	public String getFlowStatus() {
		return flowStatus;
	}

	public void setFlowStatus(String flowStatus) {
		this.flowStatus = flowStatus;
	}

	public String getIsMobileAccept() {
		return isMobileAccept;
	}

	public void setIsMobileAccept(String isMobileAccept) {
		this.isMobileAccept = isMobileAccept;
	}

	public String getSubCheckFlag() {
		return subCheckFlag;
	}

	public void setSubCheckFlag(String subCheckFlag) {
		this.subCheckFlag = subCheckFlag;
	}

	public String getLicenseNo() {
		return licenseNo;
	}

	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}

	public String getPingAnFlag() {
		return pingAnFlag;
	}

	public void setPingAnFlag(String pingAnFlag) {
		this.pingAnFlag = pingAnFlag;
	}

	


}
