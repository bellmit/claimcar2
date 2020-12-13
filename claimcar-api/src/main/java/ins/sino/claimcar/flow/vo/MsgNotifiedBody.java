package ins.sino.claimcar.flow.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("BODY")
public class MsgNotifiedBody implements Serializable {

	/**  */
	private static final long serialVersionUID = 2964968376557481068L;
	
	@XStreamAlias("REGISTNO")
	private String registNo;  //报案号
	
	@XStreamAlias("TYPE")
	private String type; //类型
	
	@XStreamAlias("NODETYPE")
	private String nodeType; //节点
	
	@XStreamAlias("ITEMNO")
	private String itemNo; //标的序号
	
	@XStreamAlias("ITEMNONAME")
	private String itemName; //标的名称
	
	@XStreamAlias("ISOBJECT")
	private String isObject; //是否标的

	@XStreamAlias("CHECKSTATUS")
	private String checkStatus; //案件状态
	
	@XStreamAlias("HANDLERCODE")
	private String handlerCode; //原始处理人编码
	
	@XStreamAlias("HANDLERNAME")
	private String handlerName; //原始处理人名称
	
	@XStreamAlias("NEXTHANDLERCODE")
	private String nextHandlerCode; //处理人员编码
	
	@XStreamAlias("NEXTHANDLERNAME")
	private String nextHandlerName; // 处理人名称
	
	@XStreamAlias("TASKID")
	private String taskId; // 任务ID
	
	@XStreamAlias("HANDOVERTASKREASON")
	private String handoverTaskReason;//平级移交原因

	public String getRegistNo() {
		return registNo;
	}

	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getIsObject() {
		return isObject;
	}

	public void setIsObject(String isObject) {
		this.isObject = isObject;
	}

	public String getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
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

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getHandoverTaskReason() {
		return handoverTaskReason;
	}

	public void setHandoverTaskReason(String handoverTaskReason) {
		this.handoverTaskReason = handoverTaskReason;
	}

}
