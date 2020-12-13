package ins.sino.claimcar.carchild.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("SCHEDULEITEM")
public class ScheduleItemVo implements Serializable{

    /**  */
    private static final long serialVersionUID = 8031845918051765797L;
    
    @XStreamAlias("TASKID")
    private String taskId;//任务ID
    
    @XStreamAlias("NODETYPE")
    private String nodeType;//调度节点
    
    @XStreamAlias("ITEMNONAME")
    private String itemNoName;//标的名称
    
    @XStreamAlias("ITEMNO")
    private String itemNo;//标的序号

    @XStreamAlias("DAMAGEADDRESS")
    private String damageAddress;//出险地点
    
    @XStreamAlias("NEXTHANDLERCODE")
    private String nextHandlerCode;//处理人代码
    
    @XStreamAlias("NEXTHANDLERNAME")
    private String nextHandlerName;//处理人名称
    
    @XStreamAlias("SCHEDULEOBJECTID")
    private String scheduleObjectId;//处理人员归属机构编码
    
    @XStreamAlias("SCHEDULEOBJECTNAME")
    private String scheduleObjectName;//处理人员归属机构名称

    
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

    
    public String getItemNoName() {
        return itemNoName;
    }

    
    public void setItemNoName(String itemNoName) {
        this.itemNoName = itemNoName;
    }

    
    public String getItemNo() {
        return itemNo;
    }

    
    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    
    public String getDamageAddress() {
        return damageAddress;
    }

    
    public void setDamageAddress(String damageAddress) {
        this.damageAddress = damageAddress;
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
