package ins.sino.claimcar.carchild.schedule.po;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@TableGenerator(name = "utiSequence", table = "UTISEQUENCE", pkColumnName = "GenName", valueColumnName = "GenValue", pkColumnValue = "PRPLCARCHILDSCHEDULE_PK", allocationSize = 10)
@Table(name = "PRPLCARCHILDSCHEDULE")
public class PrplCarchildSchedule implements Serializable {

    /**  */
    private static final long serialVersionUID = 1L;
    private BigDecimal id;
    private String registNo;//报案号
    private String taskId;//任务ID
    private String nodeType;//任务节点类型
    private String handlerName;//公估师姓名
    private String handlerPhone;//公估师电话
    private String traceLink;//公估师轨迹地图链接
    private String optionType;//操作(1首次受理任务--2改派)
    private Date timeStamp;//时间戳
    private String sourceType;//来源
    
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "utiSequence") 
    @Column(name = "ID", unique = true, nullable = false, precision=0)
    public BigDecimal getId() {
        return this.id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }
    
    @Column(name = "REGISTNO", nullable = false, length=32)
    public String getRegistNo() {
        return this.registNo;
    }

    public void setRegistNo(String registNo) {
        this.registNo = registNo;
    }

    @Column(name = "TASKID", nullable = false, length=100)
    public String getTaskId() {
        return taskId;
    }

    
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @Column(name = "NODETYPE", nullable = false, length=10)
    public String getNodeType() {
        return nodeType;
    }

    
    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    @Column(name = "HANDLERNAME", nullable = false, length=100)
    public String getHandlerName() {
        return handlerName;
    }

    
    public void setHandlerName(String handlerName) {
        this.handlerName = handlerName;
    }

    @Column(name = "HANDLERPHONE", nullable = false, length=100)
    public String getHandlerPhone() {
        return handlerPhone;
    }

    
    public void setHandlerPhone(String handlerPhone) {
        this.handlerPhone = handlerPhone;
    }

    @Column(name = "TRACELINK", length=200)
    public String getTraceLink() {
        return traceLink;
    }

    
    public void setTraceLink(String traceLink) {
        this.traceLink = traceLink;
    }

    @Column(name = "OPTIONTYPE", nullable = false, length=10)
    public String getOptionType() {
        return optionType;
    }

    
    public void setOptionType(String optionType) {
        this.optionType = optionType;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TIMESTAMP", nullable = false)
    public Date getTimeStamp() {
        return timeStamp;
    }

    
    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    } 
       

    @Column(name = "SOURCETYPE", nullable = false, length=10)
    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }
    
}
