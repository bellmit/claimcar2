package ins.sino.claimcar.claim.vo;


import java.io.Serializable;
import java.util.Map;

import ins.sino.claimcar.flow.constant.SubmitType;

public class SubmitNextVo implements Serializable{
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
	/** 意见 */
	private String auditStatus;
	
	private Map<String,String> nodeMap;

	public Map<String, String> getNodeMap() {
		return nodeMap;
	}

	public void setNodeMap(Map<String, String> nodeMap) {
		this.nodeMap = nodeMap;
	}

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

	


}

