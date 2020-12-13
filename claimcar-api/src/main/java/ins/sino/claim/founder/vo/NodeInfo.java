package ins.sino.claim.founder.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("NodeInfo")
public class NodeInfo implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@XStreamAlias("FlowId")
	private String flowId;
	
	@XStreamAlias("RegistNo")
	private String registNo;
	
	@XStreamAlias("ReportTime")
	private String reportTime;
	
	@XStreamAlias("TaskId")
	private String taskId;
	
	@XStreamAlias("UpperTaskId")
	private String upperTaskId;
	
	@XStreamAlias("NodeCode")
	private String nodeCode;
	
	@XStreamAlias("SubNodeCode")
	private String subNodeCode;
	
	@XStreamAlias("TaskName")
	private String taskName;
	
	@XStreamAlias("ItemName")
	private String itemName;
	
	@XStreamAlias("ComCode")
	private String comCode;
	
	@XStreamAlias("RiskCode")
	private String riskCode;
	
	@XStreamAlias("HandlerStatus")
	private String handlerStatus;

	@XStreamAlias("TaskInTime")
	private String taskInTime;
	
	@XStreamAlias("HandlerUser")
	private String handlerUser;
	
	@XStreamAlias("HandlerType")
	private String handlerType;
	
	@XStreamAlias("HandlerCom")
	private String handlerCom;
	
	@XStreamAlias("TaskInNode")
	private String taskInNode;
	
	@XStreamAlias("TaskOutNode")
	private String TaskOutNode;
	
	@XStreamAlias("RepairFactoryName")
	private String repairFactoryName;

	
	
	public String getFlowId() {
		return flowId;
	}

	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}

	public String getRegistNo() {
		return registNo;
	}

	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}

	public String getReportTime() {
		return reportTime;
	}

	public void setReportTime(String reportTime) {
		this.reportTime = reportTime;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getUpperTaskId() {
		return upperTaskId;
	}

	public void setUpperTaskId(String upperTaskId) {
		this.upperTaskId = upperTaskId;
	}

	public String getNodeCode() {
		return nodeCode;
	}

	public void setNodeCode(String nodeCode) {
		this.nodeCode = nodeCode;
	}

	public String getSubNodeCode() {
		return subNodeCode;
	}

	public void setSubNodeCode(String subNodeCode) {
		this.subNodeCode = subNodeCode;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getComCode() {
		return comCode;
	}

	public void setComCode(String comCode) {
		this.comCode = comCode;
	}

	public String getRiskCode() {
		return riskCode;
	}

	public void setRiskCode(String riskCode) {
		this.riskCode = riskCode;
	}

	public String getHandlerStatus() {
		return handlerStatus;
	}

	public void setHandlerStatus(String handlerStatus) {
		this.handlerStatus = handlerStatus;
	}

	public String getTaskInTime() {
		return taskInTime;
	}

	public void setTaskInTime(String taskInTime) {
		this.taskInTime = taskInTime;
	}

	public String getHandlerUser() {
		return handlerUser;
	}

	public void setHandlerUser(String handlerUser) {
		this.handlerUser = handlerUser;
	}

	public String getHandlerType() {
		return handlerType;
	}

	public void setHandlerType(String handlerType) {
		this.handlerType = handlerType;
	}

	public String getHandlerCom() {
		return handlerCom;
	}

	public void setHandlerCom(String handlerCom) {
		this.handlerCom = handlerCom;
	}

	public String getTaskInNode() {
		return taskInNode;
	}

	public void setTaskInNode(String taskInNode) {
		this.taskInNode = taskInNode;
	}

	public String getTaskOutNode() {
		return TaskOutNode;
	}

	public void setTaskOutNode(String taskOutNode) {
		TaskOutNode = taskOutNode;
	}

	public String getRepairFactoryName() {
		return repairFactoryName;
	}

	public void setRepairFactoryName(String repairFactoryName) {
		this.repairFactoryName = repairFactoryName;
	}
	
	
	
	
	
}
