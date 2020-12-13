package ins.sino.claimcar.pinganUnion.dto;

import java.io.Serializable;

/**
 * @Description 险别未决信息
 * @Author liuys
 * @Date 2020/7/21 18:50
 */
public class DutyEstimateDTO implements Serializable {
    //险别未决主键
    private String idClmDutyEstimateHistory;
    //案件号
    private String reportNo;
    //赔付次数
    private String caseTimes;
    //赔案号
    private String caseNo;
    //保单号
    private String policyNo;
    //险种编码
    private String planCode;
    //险别编码
    private String dutyCode;
    //险别未决金额
    private String dutyEstimateAmount;
    //未决类型  01-案均未决 03-人工调整未决 05-延迟未决 06-延迟未决 08-立案时点未决 09-系统修正未决 10-核赔未决
    private String estimateType;

    public String getIdClmDutyEstimateHistory() {
        return idClmDutyEstimateHistory;
    }

    public void setIdClmDutyEstimateHistory(String idClmDutyEstimateHistory) {
        this.idClmDutyEstimateHistory = idClmDutyEstimateHistory;
    }

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

    public String getCaseNo() {
        return caseNo;
    }

    public void setCaseNo(String caseNo) {
        this.caseNo = caseNo;
    }

    public String getPolicyNo() {
        return policyNo;
    }

    public void setPolicyNo(String policyNo) {
        this.policyNo = policyNo;
    }

    public String getPlanCode() {
        return planCode;
    }

    public void setPlanCode(String planCode) {
        this.planCode = planCode;
    }

    public String getDutyCode() {
        return dutyCode;
    }

    public void setDutyCode(String dutyCode) {
        this.dutyCode = dutyCode;
    }

    public String getDutyEstimateAmount() {
        return dutyEstimateAmount;
    }

    public void setDutyEstimateAmount(String dutyEstimateAmount) {
        this.dutyEstimateAmount = dutyEstimateAmount;
    }

    public String getEstimateType() {
        return estimateType;
    }

    public void setEstimateType(String estimateType) {
        this.estimateType = estimateType;
    }
}
