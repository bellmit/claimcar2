package ins.sino.claimcar.mobilecheck.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 手动调度返回查勘定损员信息接口 - 调度对象（快赔请求理赔）
 * @author zy
 *
 */
@XStreamAlias("TASKITEM")
public class HandleScheduleBackReqScheduleItemDOrG implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;

	/** 任务id */
	@XStreamAlias("USERCODE")
	private String userCode;
	   
	/** 任务id */
	@XStreamAlias("TASKID")
	private String taskId;
	
	/** 调度节点 */
	@XStreamAlias("NODETYPE")
	private String nodeType;
	
	/** 标的序号 */
	@XStreamAlias("ITEMNO")
	private String itemNo;
	
	/** 标的名称 */
	@XStreamAlias("ITEMNONAME")
	private String itemName;
	
	/** 是否标的 */
	@XStreamAlias("ISOBJECT")
	private String isObject;
	
	/** 失败原因 */
	@XStreamAlias("ERRORREASON")
	private String errorReason;


	
	
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

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getIsObject() {
		return isObject;
	}

	public void setIsObject(String isObject) {
		this.isObject = isObject;
	}

	public String getErrorReason() {
		return errorReason;
	}

	public void setErrorReason(String errorReason) {
		this.errorReason = errorReason;
	}

	

	
}
