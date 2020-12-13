package ins.sino.claimcar.carchild.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;


@XStreamAlias("TASKINFO")
public class RevokeTaskInfoVo implements Serializable {

    /**  */
    private static final long serialVersionUID = 280521696559451059L;
    
    @XStreamAlias("TASKID")
    private String taskId;//任务ID
    
    @XStreamAlias("REGISTNO")
    private String registNo;//报案号
    
    @XStreamAlias("NODETYPE")
    private String nodeType;//调度节点
    
    @XStreamAlias("REVOKETYPE")
    private String revokeType;//撤销类型(1-调度改派--2-平级移交--3-定损注销--4-报案注销--5-立案注销)
    
    @XStreamAlias("REASON")
    private String reason;//撤销原因
    
    @XStreamAlias("REMARK")
    private String remark;//撤销备注
    
    @XStreamAlias("NEWTASKID")
    private String newTaskId;//新任务ID
    
    @XStreamAlias("NEWHANDLERUSER")
    private String newHandlerUser;//新任务处理人
    
    @XStreamAlias("TIMESTAMP")
    private String timeStamp;//时间戳

    
    public String getTaskId() {
        return taskId;
    }

    
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    
    public String getRegistNo() {
        return registNo;
    }

    
    public void setRegistNo(String registNo) {
        this.registNo = registNo;
    }

    
    public String getNodeType() {
        return nodeType;
    }

    
    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    
    public String getRevokeType() {
        return revokeType;
    }

    
    public void setRevokeType(String revokeType) {
        this.revokeType = revokeType;
    }

    
    public String getReason() {
        return reason;
    }

    
    public void setReason(String reason) {
        this.reason = reason;
    }

    
    public String getRemark() {
        return remark;
    }

    
    public void setRemark(String remark) {
        this.remark = remark;
    }

    
    
    
    public String getNewTaskId() {
        return newTaskId;
    }


    
    public void setNewTaskId(String newTaskId) {
        this.newTaskId = newTaskId;
    }


    
    public String getNewHandlerUser() {
        return newHandlerUser;
    }


    
    public void setNewHandlerUser(String newHandlerUser) {
        this.newHandlerUser = newHandlerUser;
    }


    public String getTimeStamp() {
        return timeStamp;
    }

    
    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
    
}
