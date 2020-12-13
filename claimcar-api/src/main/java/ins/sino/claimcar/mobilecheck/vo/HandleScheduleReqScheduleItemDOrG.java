package ins.sino.claimcar.mobilecheck.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 手动调度请求接口 - 调度对象（理赔请求快赔）
 * @author zy
 *
 */
@XStreamAlias("TASKITEM")
public class HandleScheduleReqScheduleItemDOrG implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;

	/** 任务id */
	@XStreamAlias("TASKID")
	private String taskId;
	
	/** 原任务id */
	@XStreamAlias("ORIGINALTASKID")
	private String originalTaskId;
	
	/** 序号 */
    @XStreamAlias("SERIALNO")
    private String serialNo;
    
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
	
	/** 处理人代码 */
	@XStreamAlias("NEXTHANDLERCODE")
	private String nextHandlerCode;
	
	/** 处理人名称 */
	@XStreamAlias("NEXTHANDLERNAME")
	private String nextHandlerName;
	
	/** 处理人员归属机构编码 */
	@XStreamAlias("SCHEDULEOBJECTID")
	private String scheduleObjectId;
	
	/** 处理人员归属机构名称 */
	@XStreamAlias("SCHEDULEOBJECTNAME")
	private String scheduleObjectName;

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	
	
    public String getSerialNo() {
        return serialNo;
    }

    
    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
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

	public String getOriginalTaskId() {
		return originalTaskId;
	}

	public void setOriginalTaskId(String originalTaskId) {
		this.originalTaskId = originalTaskId;
	}
	

}
