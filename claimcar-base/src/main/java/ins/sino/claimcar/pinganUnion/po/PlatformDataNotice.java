package ins.sino.claimcar.pinganUnion.po;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @Description 上传平台推送记录对象
 * @Author liuys
 * @Date 2020/7/20 15:52
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@TableGenerator(name = "utiSequence", table = "UTISEQUENCE", pkColumnName = "GenName", valueColumnName = "GenValue", pkColumnValue = "PINGAN_PLATFORM_DATANOTICE_PK", allocationSize = 1)
@Table(name = "PINGAN_PLATFORM_DATANOTICE")
public class PlatformDataNotice implements Serializable {
    //主键
    private Long id;

    //头部报文
    private String head;

    //报文
    //private String reqData;

    //处理状态,枚举类DealStatusEnum
    private String status;

    //请求类型
    private String transType;

    //交易时间
    private Date transDate;

    //案件号
    private String reportNo;

    //保单号
    private String policyNo;

    //机构代码
    private String comCode;

    //险种类型
    private String riskType;

    //备注
    private String remark;

    //创建时间
    private Date createTime;

    //更新时间
    private Date updateTime;

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "utiSequence")
    @Column(name = "ID", unique = true, nullable = false, precision=15, scale=0)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "HEAD", length=100)
    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    /*@Column(name = "REQDATA")
    @Basic(fetch = FetchType.LAZY)
    @Type(type = "org.hibernate.type.MaterializedClobType")
    public String getReqData() {
        return reqData;
    }

    public void setReqData(String reqData) {
        this.reqData = reqData;
    }*/

    @Column(name = "STATUS", length=1)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Column(name = "TRANSTYPE", length=8)
    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TRANSDATE", length=7)
    public Date getTransDate() {
        return transDate;
    }

    public void setTransDate(Date transDate) {
        this.transDate = transDate;
    }

    @Column(name = "REPORTNO", length=25)
    public String getReportNo() {
        return reportNo;
    }

    public void setReportNo(String reportNo) {
        this.reportNo = reportNo;
    }

    @Column(name = "POLICYNO", length=25)
    public String getPolicyNo() {
        return policyNo;
    }

    public void setPolicyNo(String policyNo) {
        this.policyNo = policyNo;
    }

    @Column(name = "COMCODE", length=10)
    public String getComCode() {
        return comCode;
    }

    public void setComCode(String comCode) {
        this.comCode = comCode;
    }

    @Column(name = "RISKTYPE", length=6)
    public String getRiskType() {
        return riskType;
    }

    public void setRiskType(String riskType) {
        this.riskType = riskType;
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
}
