package ins.sino.claimcar.pinganUnion.po;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @Description 数据下发通知对象
 * @Author liuys
 * @Date 2020/7/20 11:49
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@TableGenerator(name = "utiSequence", table = "UTISEQUENCE", pkColumnName = "GenName", valueColumnName = "GenValue", pkColumnValue = "PINGAN_DATANOTICE_PK", allocationSize = 1)
@Table(name = "PINGAN_DATANOTICE")
public class PingAnDataNotice implements Serializable {
    //主键
    private Long id;

    //数据通知环节,枚举类PingAnDataTypeEnum
    private String pushType;

    //各环节查询入参
    private String paramObj;

    //处理状态,枚举类DealStatusEnum
    private String status;

    //案件号
    private String reportNo;

    //备注
    private String remark;

    //创建时间
    private Date createTime;

    //更新时间
    private Date updateTime;

    //失败次数
    private Integer failTimes;

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "utiSequence")
    @Column(name = "ID", unique = true, nullable = false, precision=15, scale=0)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "PUSHTYPE", length=10)
    public String getPushType() {
        return pushType;
    }

    public void setPushType(String pushType) {
        this.pushType = pushType;
    }

    @Column(name = "PARAMOBJ", length=1000)
    public String getParamObj() {
        return paramObj;
    }

    public void setParamObj(String paramObj) {
        this.paramObj = paramObj;
    }

    @Column(name = "STATUS", length=1)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Column(name = "REPORTNO", length=25)
    public String getReportNo() {
        return reportNo;
    }

    public void setReportNo(String reportNo) {
        this.reportNo = reportNo;
    }

    @Column(name = "REMARK", length=200)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATETIME", length=7)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATETIME", length = 7)
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Column(name = "FAILTIMES", length=2)
    public Integer getFailTimes() {
        return failTimes;
    }

    public void setFailTimes(Integer failTimes) {
        this.failTimes = failTimes;
    }
}
