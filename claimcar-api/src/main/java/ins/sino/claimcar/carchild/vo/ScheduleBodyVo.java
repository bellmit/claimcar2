package ins.sino.claimcar.carchild.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("BODY")
public class ScheduleBodyVo implements Serializable {

    /**  */
    private static final long serialVersionUID = 3970499206715299823L;

    @XStreamAlias("TASKINFO")
    private ScheduleTaskInfoVo taskInfo;

    
    public ScheduleTaskInfoVo getTaskInfo() {
        return taskInfo;
    }

    
    public void setTaskInfo(ScheduleTaskInfoVo taskInfo) {
        this.taskInfo = taskInfo;
    }
    
    
}
