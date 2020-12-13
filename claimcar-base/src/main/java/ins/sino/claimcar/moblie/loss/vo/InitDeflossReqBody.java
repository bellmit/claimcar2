package ins.sino.claimcar.moblie.loss.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("BODY")
public class InitDeflossReqBody implements Serializable{
	private static final long serialVersionUID = 475570135282512092L;
	@XStreamAlias("REGISTNO")
	private String registNo; //报案号
	@XStreamAlias("CERTAINSTATUS")
	private String certainStatus; //案件状态
	@XStreamAlias("TASKID")
	private String taskId; //任务id
	@XStreamAlias("NODETYPE")
	private String nodeType; //调度节点
	@XStreamAlias("ITEMNO")
	private String itemNo; //标的序号
	@XStreamAlias("ITEMNONAME")
	private String itemNoName; //标的名称
	@XStreamAlias("ISOBJECT")
	private String isObject; //是否标的
	@XStreamAlias("NEXTHANDLERCODE")
	private String nextHandlerCode; //处理人代码
	@XStreamAlias("NEXTHANDLERNAME")
	private String nextHandlerName; //处理人名称
	@XStreamAlias("SCHEDULEOBJECTID")
	private String scheduleObjectId; //处理人员归属机构编码
	@XStreamAlias("SCHEDULEOBJECTNAME")
	private String scheduleObjectName; //处理人员归属机构名称
	@XStreamAlias("SERIALNO")
	private String serialNo; //车辆序号
	public String getRegistNo() {
		return registNo;
	}

	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}

	public String getCertainStatus() {
		return certainStatus;
	}

	public void setCertainStatus(String certainStatus) {
		this.certainStatus = certainStatus;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getNodeType() {
		return nodeType;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	public String getItemNo() {
		return itemNo;
	}

	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}

	public String getItemNoName() {
		return itemNoName;
	}

	public void setItemNoName(String itemNoName) {
		this.itemNoName = itemNoName;
	}

	public String getIsObject() {
		return isObject;
	}

	public void setIsObject(String isObject) {
		this.isObject = isObject;
	}

	public String getNextHandlerCode() {
		return nextHandlerCode;
	}

	public void setNextHandlerCode(String nextHandlerCode) {
		this.nextHandlerCode = nextHandlerCode;
	}

	public String getNextHandlerName() {
		return nextHandlerName;
	}

	public void setNextHandlerName(String nextHandlerName) {
		this.nextHandlerName = nextHandlerName;
	}

	public String getScheduleObjectId() {
		return scheduleObjectId;
	}

	public void setScheduleObjectId(String scheduleObjectId) {
		this.scheduleObjectId = scheduleObjectId;
	}

	public String getScheduleObjectName() {
		return scheduleObjectName;
	}

	public void setScheduleObjectName(String scheduleObjectName) {
		this.scheduleObjectName = scheduleObjectName;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	
}
