package ins.sino.claimcar.pinganUnion.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description 案件基本信息
 * @Author liuys
 * @Date 2020/7/21 15:29
 */
public class ReportInfoDTO implements Serializable {
    //报案号
    private String reportNo;
    //报案人
    private String reporterName;
    //报案时间
    private Date reportDate;
    //报案人来电号码
    private String reporterCallNo;
    //报案人登记号码
    private String reporterRegisterTel;
    //驾驶员
    private String driverName;
    //是否有物损 Y：是,N：否
    private String isCargoLoss;
    //是否有车损 Y：是,N：否
    private String isCarLoss;
    //是否有人伤 Y：是,N：否
    private String isInjured;
    //是否现场报案    Y：是,N：否
    private String reportOnPort;
    //是否三者保险公司代报案   Y：是,N：否
    private String isThirdAgentReport;
    //是否代位案件    Y：是,N：否
    private String isAgentCase;
    //备注
    private String remark;
    //推修修理厂ID
    private String repaireFactoryId;
    //推修修理厂名称
    private String repairFactoryName;
    //驾驶员性别 M:男 F女
    private String driverSex;
    //驾驶员身份证号码
    private String driveCardId;
    //报案来源 1:95511电话报案  8:好车主APP报案
    private String reportMode;

    public String getReportNo() {
        return reportNo;
    }

    public void setReportNo(String reportNo) {
        this.reportNo = reportNo;
    }

    public String getReporterName() {
        return reporterName;
    }

    public void setReporterName(String reporterName) {
        this.reporterName = reporterName;
    }

    public Date getReportDate() {
        return reportDate;
    }

    public void setReportDate(Date reportDate) {
        this.reportDate = reportDate;
    }

    public String getReporterCallNo() {
        return reporterCallNo;
    }

    public void setReporterCallNo(String reporterCallNo) {
        this.reporterCallNo = reporterCallNo;
    }

    public String getReporterRegisterTel() {
        return reporterRegisterTel;
    }

    public void setReporterRegisterTel(String reporterRegisterTel) {
        this.reporterRegisterTel = reporterRegisterTel;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getIsCargoLoss() {
        return isCargoLoss;
    }

    public void setIsCargoLoss(String isCargoLoss) {
        this.isCargoLoss = isCargoLoss;
    }

    public String getIsCarLoss() {
        return isCarLoss;
    }

    public void setIsCarLoss(String isCarLoss) {
        this.isCarLoss = isCarLoss;
    }

    public String getIsInjured() {
        return isInjured;
    }

    public void setIsInjured(String isInjured) {
        this.isInjured = isInjured;
    }

    public String getReportOnPort() {
        return reportOnPort;
    }

    public void setReportOnPort(String reportOnPort) {
        this.reportOnPort = reportOnPort;
    }

    public String getIsThirdAgentReport() {
        return isThirdAgentReport;
    }

    public void setIsThirdAgentReport(String isThirdAgentReport) {
        this.isThirdAgentReport = isThirdAgentReport;
    }

    public String getIsAgentCase() {
        return isAgentCase;
    }

    public void setIsAgentCase(String isAgentCase) {
        this.isAgentCase = isAgentCase;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRepaireFactoryId() {
        return repaireFactoryId;
    }

    public void setRepaireFactoryId(String repaireFactoryId) {
        this.repaireFactoryId = repaireFactoryId;
    }

    public String getRepairFactoryName() {
        return repairFactoryName;
    }

    public void setRepairFactoryName(String repairFactoryName) {
        this.repairFactoryName = repairFactoryName;
    }

    public String getDriverSex() {
        return driverSex;
    }

    public void setDriverSex(String driverSex) {
        this.driverSex = driverSex;
    }

    public String getDriveCardId() {
        return driveCardId;
    }

    public void setDriveCardId(String driveCardId) {
        this.driveCardId = driveCardId;
    }

    public String getReportMode() {
        return reportMode;
    }

    public void setReportMode(String reportMode) {
        this.reportMode = reportMode;
    }
}
