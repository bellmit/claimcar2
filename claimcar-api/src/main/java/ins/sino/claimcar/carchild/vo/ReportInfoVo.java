package ins.sino.claimcar.carchild.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("REPORTINFO")
public class ReportInfoVo implements Serializable{

    /**  */
    private static final long serialVersionUID = 8031845918051765797L;
    
    @XStreamAlias("REGISTNO")
    private String registNo;//报案号
    
    @XStreamAlias("INSUREDNAME")
    private String insuredName;//被保险人
    
    @XStreamAlias("DRIVERNAME")
    private String driverName;//驾驶人
    
    @XStreamAlias("LICENSENO")
    private String licenseNo;//车牌号

    @XStreamAlias("BRANDNAME")
    private String brandName;//厂牌型号
    
    @XStreamAlias("REPORTORNAME")
    private String reportorName;//报案人
    
    @XStreamAlias("REPORTPHONENUMBER")
    private String reportPhoneNumber;//报案人电话
    
    @XStreamAlias("LINKERNAME")
    private String linkerName;//联系人
    
    @XStreamAlias("LINKERPHONENUMBER")
    private String linkerPhoneNumber;//联系人电话
    
    @XStreamAlias("EXIGENCEGREE")
    private String exigenceGree;//案件紧急类型
    
    @XStreamAlias("RISKWARNING")
    private String riskWarning;//风险提示信息
    
    @XStreamAlias("TELCALLTIMES")
    private int telCallTines;//报案电话出险次数
    
    @XStreamAlias("REPORTDATE")
    private String reportDate; //报案时间
    
    @XStreamAlias("DAMAGEDATE")
    private String damageDate; //出险时间
    
    @XStreamAlias("DAMAGECODE")
    private String damageCode;//出现原因
    
    @XStreamAlias("DAMAGEADDRESS")
    private String damageAdress;//出现地点
    
    @XStreamAlias("ACCIDENTDESC")
    private String accidentDesc;//出现经过说明

    
    public String getRegistNo() {
        return registNo;
    }

    
    public void setRegistNo(String registNo) {
        this.registNo = registNo;
    }

    
    public String getInsuredName() {
        return insuredName;
    }

    
    public void setInsuredName(String insuredName) {
        this.insuredName = insuredName;
    }

    
    public String getDriverName() {
        return driverName;
    }

    
    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    
    public String getLicenseNo() {
        return licenseNo;
    }

    
    public void setLicenseNo(String licenseNo) {
        this.licenseNo = licenseNo;
    }

    
    public String getBrandName() {
        return brandName;
    }

    
    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    
    public String getReportorName() {
        return reportorName;
    }

    
    public void setReportorName(String reportorName) {
        this.reportorName = reportorName;
    }

    
    public String getReportPhoneNumber() {
        return reportPhoneNumber;
    }

    
    public void setReportPhoneNumber(String reportPhoneNumber) {
        this.reportPhoneNumber = reportPhoneNumber;
    }

    
    public String getLinkerName() {
        return linkerName;
    }

    
    public void setLinkerName(String linkerName) {
        this.linkerName = linkerName;
    }

    
    public String getLinkerPhoneNumber() {
        return linkerPhoneNumber;
    }

    
    public void setLinkerPhoneNumber(String linkerPhoneNumber) {
        this.linkerPhoneNumber = linkerPhoneNumber;
    }

    
    public String getExigenceGree() {
        return exigenceGree;
    }

    
    public void setExigenceGree(String exigenceGree) {
        this.exigenceGree = exigenceGree;
    }

    
    public String getRiskWarning() {
        return riskWarning;
    }

    
    public void setRiskWarning(String riskWarning) {
        this.riskWarning = riskWarning;
    }

    
    public int getTelCallTines() {
        return telCallTines;
    }

    
    public void setTelCallTines(int telCallTines) {
        this.telCallTines = telCallTines;
    }
    
    
    public String getReportDate() {
        return reportDate;
    }

    
    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }

    
    public String getDamageDate() {
        return damageDate;
    }


    
    public void setDamageDate(String damageDate) {
        this.damageDate = damageDate;
    }


    public String getDamageCode() {
        return damageCode;
    }

    
    public void setDamageCode(String damageCode) {
        this.damageCode = damageCode;
    }

    
    public String getDamageAdress() {
        return damageAdress;
    }

    
    public void setDamageAdress(String damageAdress) {
        this.damageAdress = damageAdress;
    }

    
    public String getAccidentDesc() {
        return accidentDesc;
    }

    
    public void setAccidentDesc(String accidentDesc) {
        this.accidentDesc = accidentDesc;
    }

}
