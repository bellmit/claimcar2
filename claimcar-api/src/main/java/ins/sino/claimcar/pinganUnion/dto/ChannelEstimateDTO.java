package ins.sino.claimcar.pinganUnion.dto;

import java.io.Serializable;

/**
 * @Description 通道未决信息
 * @Author liuys
 * @Date 2020/7/21 18:51
 */
public class ChannelEstimateDTO implements Serializable {
    //险别未决主键
    private String idClmDutyEstimateHistory;
    //通道号
    private String idClmChannelProcess;
    //通道类型 01-标的车 02-三者车 03-标的物损 04-三者车内物损 05-三者车外物损 06-三者车内人 07-三者车外人 08-司机 09-乘客
    private String channelType;
    //赔案号
    private String caseNo;
    //保单号
    private String policyNo;
    //案件号
    private String reportNo;
    //赔付次数
    private String caseTimes;
    //通道未决金额
    private String channelEstimateAmount;
    //未决类型 01-案均未决 03-人工调整未决 05-延迟未决 06-延迟未决 08-立案时点未决 09-系统修正未决 10-核赔未决
    private String estimateType;

    public String getIdClmDutyEstimateHistory() {
        return idClmDutyEstimateHistory;
    }

    public void setIdClmDutyEstimateHistory(String idClmDutyEstimateHistory) {
        this.idClmDutyEstimateHistory = idClmDutyEstimateHistory;
    }

    public String getIdClmChannelProcess() {
        return idClmChannelProcess;
    }

    public void setIdClmChannelProcess(String idClmChannelProcess) {
        this.idClmChannelProcess = idClmChannelProcess;
    }

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
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

    public String getChannelEstimateAmount() {
        return channelEstimateAmount;
    }

    public void setChannelEstimateAmount(String channelEstimateAmount) {
        this.channelEstimateAmount = channelEstimateAmount;
    }

    public String getEstimateType() {
        return estimateType;
    }

    public void setEstimateType(String estimateType) {
        this.estimateType = estimateType;
    }
}
