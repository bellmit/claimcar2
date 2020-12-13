package ins.sino.claimcar.mtainterface.check.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("BODY")
public class MTACheckInfoInitReqBodyVo  implements Serializable{

private static final long serialVersionUID = -4623864062836013585L;
	
	@XStreamAlias("REGISTNO")
	private String registNo; //报案号
	
	@XStreamAlias("TASKID")
	private String taskId; //任务id
	
	@XStreamAlias("NODETYPE")
	private String nodeType; //任务节点类型
	
	@XStreamAlias("HANDLERCODE")
	private String handlerCode; //处理人代码
	
	@XStreamAlias("HANDLERNAME")
	private String handlerName; //处理人名称
	
	@XStreamAlias("SCHEDULEOBJECTID")
	private String scheduleObjectId; //处理人员归属机构编码
	
	@XStreamAlias("SCHEDULEOBJECTNAME")
	private String scheduleObjectName; //处理人员归属机构名称
	
	@XStreamAlias("TIMESTAMP")
	private String timeStamp; //接收时间

	public String getRegistNo() {
		return registNo;
	}

	public void setRegistNo(String registNo) {
		this.registNo = registNo;
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

	public String getHandlerCode() {
		return handlerCode;
	}

	public void setHandlerCode(String handlerCode) {
		this.handlerCode = handlerCode;
	}

	public String getHandlerName() {
		return handlerName;
	}

	public void setHandlerName(String handlerName) {
		this.handlerName = handlerName;
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

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	
}
