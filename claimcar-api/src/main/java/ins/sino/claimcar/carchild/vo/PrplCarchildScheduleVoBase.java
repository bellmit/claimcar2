package ins.sino.claimcar.carchild.vo;

import java.util.Date;

public class PrplCarchildScheduleVoBase implements java.io.Serializable{

    /**  */
    private static final long serialVersionUID = 1L;

    private String registNo;// 报案号
    private String taskId;// 任务ID
    private String nodeType;// 任务节点类型
    private String handlerName;// 公估师姓名
    private String handlerPhone;// 公估师电话
    private String traceLink;// 公估师轨迹地图链接
    private String optionType;// 操作(1首次受理任务--2改派)
    private Date timeStamp;// 时间戳
    private String sourceType;//来源

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
 
    public Date getTimeStamp() {
        return timeStamp;
    }
   
    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }
    
    public String getSourceType() {
        return sourceType;
    }
    
    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }
    
}
