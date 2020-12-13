package ins.sino.claimcar.genilex.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("VehicleViolation")
public class VehicleViolation implements Serializable {

	/**被保险人信息  */
	private static final long serialVersionUID = 6606450331852430142L;
	
	@XStreamAlias("VehicleID")
	private String vehicleID;  //车辆ID
	
    @XStreamAlias("ViolationCode")
    private String violationCode;  //违法编号
    
    @XStreamAlias("ViolationRecordTypeCode")
    private String violationRecordTypeCode;  //违法行为代码
    
    @XStreamAlias("DecisionCode")
    private String decisionCode;  //决定书编号
    
    @XStreamAlias("DecisionTypeCode")
    private String decisionTypeCode;  //决定书类型
    
    @XStreamAlias("ViolationSequence")
    private String violationSequence;  //序号
    
    @XStreamAlias("DriverLicenseNo")
    private String driverLicenseNo;  //违法驾驶员驾驶证号
    
    @XStreamAlias("LicensePlateNo")
    private String licensePlateNo;  //号牌号码
	   
    @XStreamAlias("LicensePlateTypeCode")
    private String licensePlateTypeCode;  //号牌种类代码
    
    @XStreamAlias("ViolationPlace")
    private String violationPlace;  //违法地点
    
    @XStreamAlias("ViolationTime")
    private String violationTime;  //违法时间
    
    @XStreamAlias("RecognitionDate")
    private String recognitionDate;  //违法处理时间
    
    @XStreamAlias("JurisdictionAgencyCode")
    private String jurisdictionAgencyCode;  //违法处理机关代码
    
    @XStreamAlias("CreateBy")
    private String createBy;  //创建者
    
    @XStreamAlias("CreateTime")
    private String createTime;  //创建日期
    
    @XStreamAlias("UpdateBy")
    private String updateBy;  //更新者
    
    @XStreamAlias("UpdateTime")
    private String updateTime;  //更新日期

    
    public String getVehicleID() {
        return vehicleID;
    }

    
    public void setVehicleID(String vehicleID) {
        this.vehicleID = vehicleID;
    }

    
    public String getViolationCode() {
        return violationCode;
    }

    
    public void setViolationCode(String violationCode) {
        this.violationCode = violationCode;
    }

    
    public String getViolationRecordTypeCode() {
        return violationRecordTypeCode;
    }

    
    public void setViolationRecordTypeCode(String violationRecordTypeCode) {
        this.violationRecordTypeCode = violationRecordTypeCode;
    }

    
    public String getDecisionCode() {
        return decisionCode;
    }

    
    public void setDecisionCode(String decisionCode) {
        this.decisionCode = decisionCode;
    }

    
    public String getDecisionTypeCode() {
        return decisionTypeCode;
    }

    
    public void setDecisionTypeCode(String decisionTypeCode) {
        this.decisionTypeCode = decisionTypeCode;
    }

    
    public String getViolationSequence() {
        return violationSequence;
    }

    
    public void setViolationSequence(String violationSequence) {
        this.violationSequence = violationSequence;
    }

    
    public String getDriverLicenseNo() {
        return driverLicenseNo;
    }

    
    public void setDriverLicenseNo(String driverLicenseNo) {
        this.driverLicenseNo = driverLicenseNo;
    }

    
    public String getLicensePlateNo() {
        return licensePlateNo;
    }

    
    public void setLicensePlateNo(String licensePlateNo) {
        this.licensePlateNo = licensePlateNo;
    }

    
    public String getLicensePlateTypeCode() {
        return licensePlateTypeCode;
    }

    
    public void setLicensePlateTypeCode(String licensePlateTypeCode) {
        this.licensePlateTypeCode = licensePlateTypeCode;
    }

    
    public String getViolationPlace() {
        return violationPlace;
    }

    
    public void setViolationPlace(String violationPlace) {
        this.violationPlace = violationPlace;
    }

    
    public String getViolationTime() {
        return violationTime;
    }

    
    public void setViolationTime(String violationTime) {
        this.violationTime = violationTime;
    }

    
    public String getRecognitionDate() {
        return recognitionDate;
    }

    
    public void setRecognitionDate(String recognitionDate) {
        this.recognitionDate = recognitionDate;
    }

    
    public String getJurisdictionAgencyCode() {
        return jurisdictionAgencyCode;
    }

    
    public void setJurisdictionAgencyCode(String jurisdictionAgencyCode) {
        this.jurisdictionAgencyCode = jurisdictionAgencyCode;
    }

    
    public String getCreateBy() {
        return createBy;
    }

    
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    
    public String getCreateTime() {
        return createTime;
    }

    
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    
    public String getUpdateBy() {
        return updateBy;
    }

    
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    
    public String getUpdateTime() {
        return updateTime;
    }

    
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    
  
}
