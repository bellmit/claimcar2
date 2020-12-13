package ins.sino.claimcar.mobile.check.vo;

import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckBody;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * 报案基本信息（快赔请求理赔）
 * @author zjd
 *
 */
@XStreamAlias("BODY")
public class CheckInfoInitReqBodyVo extends MobileCheckBody implements Serializable{
	
	/**  */
	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("CHECKSTATUS")
	private String checkStatus; //案件状态
	
	@XStreamAlias("TASKID")
	private String taskId; //任务id
	
	@XStreamAlias("NODETYPE")
	private String nodeType; //调度节点
	
	@XStreamAlias("ITEMNO")
	private String itemNo; //标的序号
	
	@XStreamAlias("ITEMNONAME")
	private String itemnoName; //标的名称
	
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
	public String getCheckStatus() {
		return checkStatus;
	}
	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
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
	public String getItemnoName() {
		return itemnoName;
	}
	public void setItemnoName(String itemnoName) {
		this.itemnoName = itemnoName;
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
	
	
}
