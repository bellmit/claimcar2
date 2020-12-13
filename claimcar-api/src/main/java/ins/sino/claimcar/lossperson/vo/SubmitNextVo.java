package ins.sino.claimcar.lossperson.vo;

import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.constant.SubmitType;

import java.math.BigDecimal;


public class SubmitNextVo implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String registNo;
	
	// 必填参数
	/** 提交动作类型，默认为普通提交 */
	private SubmitType submitType = SubmitType.N;
	/** 工作流id */
	private String flowId;
	/** 当前的工作流TaskID，报案时为0 */
	private String flowTaskId;
	/** 归属处理机构 */
	private String comCode;
	/** 提交人 */
	private String taskInUser;
	/** 进入的关键业务号 */
	private String taskInKey;

	// 可选参数
	/** 指定处理人 */
	private String assignUser;
	private String assignName;
	/** 处理人机构 */
	private String assignCom;
	
	// 定损，核损，核损特殊参数
	/** 当前节点 */
	private String currentNode;
	
	private String currentName;
	/** 下一个节点 */
	private String nextNode;
	
	private String nextName;
	/** 提交到第几级 */
	private Integer submitLevel;
	/** 分公司最高等级 */
	private Integer maxLevel;
	/** 意见 */
	private String auditStatus;
	
	/** 其他周边节点，一个节点发起其他周边节点任务 */
	private String otherNodes;
	private String otherNodesName;
	private String otherAssignUser;
	private String otherAssignCom;
	/** 是否移动端处理 */
	private String isMobileAccept;

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

	
	public String getAssignName() {
		return assignName;
	}

	
	public void setAssignName(String assignName) {
		this.assignName = assignName;
	}

	public String getAssignCom() {
		return assignCom;
	}

	public void setAssignCom(String assignCom) {
		this.assignCom = assignCom;
	}

	public String getFlowTaskId() {
		return flowTaskId;
	}

	public void setFlowTaskId(String flowTaskId) {
		this.flowTaskId = flowTaskId;
	}

	public String getComCode() {
		return comCode;
	}

	public void setComCode(String comCode) {
		this.comCode = comCode;
	}

	
	public String getCurrentNode() {
		return currentNode;
	}

	
	public void setCurrentNode(String currentNode) {
		this.currentNode = currentNode;
	}

	
	public String getCurrentName() {
		return currentName;
	}

	
	public void setCurrentName(String currentName) {
		this.currentName = currentName;
	}

	
	public String getNextNode() {
		return nextNode;
	}

	
	public void setNextNode(String nextNode) {
		this.nextNode = nextNode;
	}

	
	public String getNextName() {
		return nextName;
	}

	
	public void setNextName(String nextName) {
		this.nextName = nextName;
	}

	
	public String getRegistNo() {
		return registNo;
	}

	
	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}

	
	public String getAuditStatus() {
		return auditStatus;
	}

	
	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}

	
	public String getOtherNodes() {
		return otherNodes;
	}

	
	public void setOtherNodes(String otherNodes) {
		this.otherNodes = otherNodes;
	}

	
	public String getOtherNodesName() {
		return otherNodesName;
	}

	
	public void setOtherNodesName(String otherNodesName) {
		this.otherNodesName = otherNodesName;
	}

	
	public String getOtherAssignUser() {
		return otherAssignUser;
	}

	
	public void setOtherAssignUser(String otherAssignUser) {
		this.otherAssignUser = otherAssignUser;
	}

	
	public String getOtherAssignCom() {
		return otherAssignCom;
	}

	
	public void setOtherAssignCom(String otherAssignCom) {
		this.otherAssignCom = otherAssignCom;
	}

	public Integer getMaxLevel() {
		return maxLevel;
	}

	public void setMaxLevel(Integer maxLevel) {
		this.maxLevel = maxLevel;
	}

	public String getIsMobileAccept() {
		return isMobileAccept;
	}

	public void setIsMobileAccept(String isMobileAccept) {
		this.isMobileAccept = isMobileAccept;
	}

	

}
