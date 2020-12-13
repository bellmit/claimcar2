package ins.sino.claimcar.trafficplatform.vo;


import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 山东预警报文
 * <pre></pre>
 * @author ★zhujunde
 */
@XStreamAlias("BasePart")
public class CarRiskRegistBasePartReqVo {
	
    @XStreamAlias("ConfirmSequenceNo")
	private String confirmSequenceNo;// 投保确认号
	
    @XStreamAlias("ClaimSequenceNo")
	private String claimCode;// 理赔编号
	
    @XStreamAlias("PolicyNo")
	private String policyNo;// 保单号
	
    @XStreamAlias("ClaimNotificationNo")
	private String reportNo;// 报案号
	
    @XStreamAlias("NotificationTime")
	private String reportTime;// 报案时间；格式YYYYMMDDHHMM
	
    @XStreamAlias("Reporter")
    private String reporterName;// 报案人姓名
    
    @XStreamAlias("ReporterPhone")
    private String reporterPhone;// 报案电话
    
    @XStreamAlias("ReporterCode")
    private String reporterCode;// 报案人证件类型

    @XStreamAlias("ReporterIdNo")
    private String reporterIdNo;// 报案人证件号码
    
    @XStreamAlias("DriverName")
    private String driverName;// 出险驾驶员姓名
    
    @XStreamAlias("LossTime")
	private String accidentTime;// 出险时间；格式YYYYMMDDHHMM
	
    @XStreamAlias("LossArea")
    private String accidentPlace;// 出险地点
    
    @XStreamAlias("LossDesc")
    private String accidentDescription;// 出险经过

    @XStreamAlias("DriverLicenseNo")
	private String driverLicenseNo;// 出险标的车驾驶证号码
	
    @XStreamAlias("LossCauseCode")
    private String accidentCause;// 出险原因；参见代码 
    
    
    @XStreamAlias("LicensePlateNo")
	private String licensePlateNo;// 出险承保车辆号牌种类；参见代码未上牌车辆可以为空
	
    @XStreamAlias("LicensePlateType")
	private String licensePlateType;// 互碰自赔标志；参见代码 出险地点
	
    @XStreamAlias("AccidentLiability")
	private String accidentLiability;// 事故责任划分代码；参见代码
	
    @XStreamAlias("OptionType")
    private String manageType;// 事故处理方式代码；参见代码

    
    public String getConfirmSequenceNo() {
        return confirmSequenceNo;
    }

    
    public void setConfirmSequenceNo(String confirmSequenceNo) {
        this.confirmSequenceNo = confirmSequenceNo;
    }

    
    public String getClaimCode() {
        return claimCode;
    }

    
    public void setClaimCode(String claimCode) {
        this.claimCode = claimCode;
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

    
    public String getReportTime() {
        return reportTime;
    }

    
    public void setReportTime(String reportTime) {
        this.reportTime = reportTime;
    }

    
    public String getReporterName() {
        return reporterName;
    }

    
    public void setReporterName(String reporterName) {
        this.reporterName = reporterName;
    }

    
    public String getReporterPhone() {
        return reporterPhone;
    }

    
    public void setReporterPhone(String reporterPhone) {
        this.reporterPhone = reporterPhone;
    }

    
    public String getReporterCode() {
        return reporterCode;
    }

    
    public void setReporterCode(String reporterCode) {
        this.reporterCode = reporterCode;
    }

    
    public String getReporterIdNo() {
        return reporterIdNo;
    }

    
    public void setReporterIdNo(String reporterIdNo) {
        this.reporterIdNo = reporterIdNo;
    }

    
    public String getDriverName() {
        return driverName;
    }

    
    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    
    public String getAccidentTime() {
        return accidentTime;
    }

    
    public void setAccidentTime(String accidentTime) {
        this.accidentTime = accidentTime;
    }

    
    public String getAccidentPlace() {
        return accidentPlace;
    }

    
    public void setAccidentPlace(String accidentPlace) {
        this.accidentPlace = accidentPlace;
    }

    
    public String getAccidentDescription() {
        return accidentDescription;
    }

    
    public void setAccidentDescription(String accidentDescription) {
        this.accidentDescription = accidentDescription;
    }

    
    public String getDriverLicenseNo() {
        return driverLicenseNo;
    }

    
    public void setDriverLicenseNo(String driverLicenseNo) {
        this.driverLicenseNo = driverLicenseNo;
    }

    
    public String getAccidentCause() {
        return accidentCause;
    }

    
    public void setAccidentCause(String accidentCause) {
        this.accidentCause = accidentCause;
    }

    
    public String getLicensePlateNo() {
        return licensePlateNo;
    }

    
    public void setLicensePlateNo(String licensePlateNo) {
        this.licensePlateNo = licensePlateNo;
    }

    
    public String getLicensePlateType() {
        return licensePlateType;
    }

    
    public void setLicensePlateType(String licensePlateType) {
        this.licensePlateType = licensePlateType;
    }

    
    public String getAccidentLiability() {
        return accidentLiability;
    }

    
    public void setAccidentLiability(String accidentLiability) {
        this.accidentLiability = accidentLiability;
    }

    
    public String getManageType() {
        return manageType;
    }

    
    public void setManageType(String manageType) {
        this.manageType = manageType;
    }
	
    
}
