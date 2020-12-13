package ins.sino.claimcar.carchild.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("TASKINFO")
public class RevokeRestoreTaskInfoVo implements Serializable {

    /**  */
    private static final long serialVersionUID = -1761798014308868406L;

    @XStreamAlias("TASKID")
    private String taskId;//任务ID
    
    @XStreamAlias("REGISTNO")
    private String registNo;//报案号
    
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

    
    public String getTimeStamp() {
        return timeStamp;
    }

    
    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
    
}
