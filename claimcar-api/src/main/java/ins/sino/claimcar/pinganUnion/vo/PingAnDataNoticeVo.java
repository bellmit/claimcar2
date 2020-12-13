package ins.sino.claimcar.pinganUnion.vo;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Date;

/**
 * @Description 数据下发通知对象
 * @Author liuys
 * @Date 2020/7/20 11:49
 */
public class PingAnDataNoticeVo implements Serializable {
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPushType() {
        return pushType;
    }

    public void setPushType(String pushType) {
        this.pushType = pushType;
    }

    public String getParamObj() {
        return paramObj;
    }

    public void setParamObj(String paramObj) {
        this.paramObj = paramObj;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReportNo() {
        return reportNo;
    }

    public void setReportNo(String reportNo) {
        this.reportNo = reportNo;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getFailTimes() {
        return failTimes;
    }

    public void setFailTimes(Integer failTimes) {
        this.failTimes = failTimes;
    }
}
