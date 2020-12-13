package ins.sino.claimcar.carchild.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("TASKINFO")
public class ScheduleTaskInfoVo implements Serializable {

    /**  */
    private static final long serialVersionUID = -6455467704590632485L;

    @XStreamAlias("REGISTNO")
    private String registNo;//报案号
    
    @XStreamAlias("TASKID")
    private String taskId;//任务ID
    
    @XStreamAlias("NODETYPE")
    private String nodeType;//任务节点类型
    
    @XStreamAlias("HANDLERNAME")
    private String handlerName;//公估师姓名
    
    @XStreamAlias("HANDLERPHONE")
    private String handlerPhone;//公估师电话
    
    @XStreamAlias("TRACELINK")
    private String traceLink;//公估师轨迹地图链接
    
    @XStreamAlias("OPTIONTYPE")
    private String optionType;//操作(1首次受理任务--2改派)
    
    @XStreamAlias("TIMESTAMP")
    //@XStreamConverter(value = SinosoftDateConverter.class, strings = {"yyyy-MM-dd HH:mm:ss"})
    private String timeStamp;//时间戳

    
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

    
    public String getHandlerName() {
        return handlerName;
    }

    
    public void setHandlerName(String handlerName) {
        this.handlerName = handlerName;
    }

    
    public String getHandlerPhone() {
        return handlerPhone;
    }

    
    public void setHandlerPhone(String handlerPhone) {
        this.handlerPhone = handlerPhone;
    }

    
    public String getTraceLink() {
        return traceLink;
    }

    
    public void setTraceLink(String traceLink) {
        this.traceLink = traceLink;
    }

    
    public String getOptionType() {
        return optionType;
    }

    
    public void setOptionType(String optionType) {
        this.optionType = optionType;
    }

    
    public String getTimeStamp() {
        return timeStamp;
    }

    
    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
    
}
