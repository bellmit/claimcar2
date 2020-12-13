package ins.sino.claimcar.carchildren.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("TASKINFO")
public class TaskinfoVo implements Serializable{
	private static final long serialVersionUID = 1L;
	@XStreamAlias("NODENAME")
    private String nodeName;//节点名称
	@XStreamAlias("NODETYPE")
	private String nodeType;//节点类型
	@XStreamAlias("BUSSNO")
	private String bussNo;//业务号
	@XStreamAlias("WORKSTATUS")
	private String workStatus;//任务状态
	@XStreamAlias("TASKINTIME")
	private String taskInTime;//任务流入时间
	@XStreamAlias("HANDLERTIME")
	private String handlerTime;//任务受理时间
	@XStreamAlias("TASKOUTTIME")
	private String taskOutTime;//任务流出时间
	@XStreamAlias("HANDLERUSERNAME")
	private String handlerUserName;//操作人员名称
	@XStreamAlias("HANDLERUSER")
	private String handlerUser;//操作人员代码
	@XStreamAlias("TASKOUTUSERNAME")
	private String taskOutUserName;//提交人员名称
	@XStreamAlias("TASKOUTUSER")
	private String taskOutUser;//提交人员代码
	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	public String getNodeType() {
		return nodeType;
	}
	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}
	public String getBussNo() {
		return bussNo;
	}
	public void setBussNo(String bussNo) {
		this.bussNo = bussNo;
	}
	public String getWorkStatus() {
		return workStatus;
	}
	public void setWorkStatus(String workStatus) {
		this.workStatus = workStatus;
	}
	public String getTaskInTime() {
		return taskInTime;
	}
	public void setTaskInTime(String taskInTime) {
		this.taskInTime = taskInTime;
	}
	public String getHandlerTime() {
		return handlerTime;
	}
	public void setHandlerTime(String handlerTime) {
		this.handlerTime = handlerTime;
	}
	public String getTaskOutTime() {
		return taskOutTime;
	}
	public void setTaskOutTime(String taskOutTime) {
		this.taskOutTime = taskOutTime;
	}
	public String getHandlerUserName() {
		return handlerUserName;
	}
	public void setHandlerUserName(String handlerUserName) {
		this.handlerUserName = handlerUserName;
	}
	public String getHandlerUser() {
		return handlerUser;
	}
	public void setHandlerUser(String handlerUser) {
		this.handlerUser = handlerUser;
	}
	public String getTaskOutUserName() {
		return taskOutUserName;
	}
	public void setTaskOutUserName(String taskOutUserName) {
		this.taskOutUserName = taskOutUserName;
	}
	public String getTaskOutUser() {
		return taskOutUser;
	}
	public void setTaskOutUser(String taskOutUser) {
		this.taskOutUser = taskOutUser;
	}
	
	
}
