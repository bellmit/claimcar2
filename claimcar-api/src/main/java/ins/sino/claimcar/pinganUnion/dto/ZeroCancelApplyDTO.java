package ins.sino.claimcar.pinganUnion.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description 零结注销信息
 * @Author liuys
 * @Date 2020/7/21 18:39
 */
public class ZeroCancelApplyDTO implements Serializable {
    //报案号
    private String reportNo;
    //赔付次数
    private String caseTimes;
    //申请次数
    private String applyTimes;
    //申请类型  1-零结 2-注销
    private String applyType;
    //申请原因
    private String applyReason;
    //申请详细原因
    private String applyReasonDetails;
    //申请人
    private String applyUm;
    //申请时间
    private Date applyDate;
    //审批人
    private String verifyUm;
    //审批时间
    private Date verifyDate;
    //审批意见1-同意; 2-退回
    private String verifyOptions;
    //审批意见说明
    private String verifyRemark;
    //审批流程是否完成  Y:是,N:否
    private String isCompleted;

    public String getReportNo() {
        return reportNo;
    }

    public void setReportNo(String reportNo) {
        this.reportNo = reportNo;
    }

    public String getCaseTimes() {
        return caseTimes;
    }

    public void setCaseTimes(String caseTimes) {
        this.caseTimes = caseTimes;
    }

    public String getApplyTimes() {
        return applyTimes;
    }

    public void setApplyTimes(String applyTimes) {
        this.applyTimes = applyTimes;
    }

    public String getApplyType() {
        return applyType;
    }

    public void setApplyType(String applyType) {
        this.applyType = applyType;
    }

    public String getApplyReason() {
        return applyReason;
    }

    public void setApplyReason(String applyReason) {
        this.applyReason = applyReason;
    }

    public String getApplyReasonDetails() {
        return applyReasonDetails;
    }

    public void setApplyReasonDetails(String applyReasonDetails) {
        this.applyReasonDetails = applyReasonDetails;
    }

    public String getApplyUm() {
        return applyUm;
    }

    public void setApplyUm(String applyUm) {
        this.applyUm = applyUm;
    }

    public Date getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(Date applyDate) {
        this.applyDate = applyDate;
    }

    public String getVerifyUm() {
        return verifyUm;
    }

    public void setVerifyUm(String verifyUm) {
        this.verifyUm = verifyUm;
    }

    public Date getVerifyDate() {
        return verifyDate;
    }

    public void setVerifyDate(Date verifyDate) {
        this.verifyDate = verifyDate;
    }

    public String getVerifyOptions() {
        return verifyOptions;
    }

    public void setVerifyOptions(String verifyOptions) {
        this.verifyOptions = verifyOptions;
    }

    public String getVerifyRemark() {
        return verifyRemark;
    }

    public void setVerifyRemark(String verifyRemark) {
        this.verifyRemark = verifyRemark;
    }

    public String getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(String isCompleted) {
        this.isCompleted = isCompleted;
    }
}
