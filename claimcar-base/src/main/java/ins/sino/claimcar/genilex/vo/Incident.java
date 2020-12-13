package ins.sino.claimcar.genilex.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Incident")
public class Incident implements Serializable {

	/**  */
	private static final long serialVersionUID = 6606450331852430142L;
	
	@XStreamAlias("DriverCode")
	private String driverCode;  //司机代码
	
    @XStreamAlias("DriverName")
    private String driverName;  //出险驾驶员姓名
    
    @XStreamAlias("LossTime")
    private String lossTime;  //出险时间
    
    @XStreamAlias("LossDesc")
    private String lossDesc;  //出险经过/损失情况
    
    @XStreamAlias("LossAreaProvince")
    private String lossAreaProvince;  //出险地点(省)
    
    @XStreamAlias("LossAreaCity")
    private String lossAreaCity;  //出险地点(市)
    
    @XStreamAlias("LossAreaRegion")
    private String lossAreaRegion;  //出险地点(区)
    
    @XStreamAlias("OcccurredCountryside")
    private String occcurredCountryside;  //出险地点所在片区乡镇
    
    @XStreamAlias("OccurrencyAreaCode")
    private String occurrencyAreaCode;  //出险区域类型
	   
    @XStreamAlias("OccurrencyRoute")
    private String occurrencyRoute;  //出险路段
    
    @XStreamAlias("DriverLicenseNo")
    private String driverLicenseNo;  //出险标的车驾驶证号码
    
    @XStreamAlias("LossCauseCode")
    private String lossCauseCode;  //出险原因码
    
    @XStreamAlias("LicensePlateNo")
    private String licensePlateNo;  //出险标的车号牌号码
    
    @XStreamAlias("LicensePlateType")
    private String licensePlateType;  //出险标的车号牌种类代码
    
    @XStreamAlias("IncidentTypeCode")
    private String incidentTypeCode;  //事故类型
    
    @XStreamAlias("OcccurredSpot")
    private String occcurredSpot;  //出险地点
    
    @XStreamAlias("OcccurredSpotAttr")
    private String occcurredSpotAttr;  //出险地点所属性质
    
    @XStreamAlias("PoliceTreatment")
    private String policeTreatment;  //是否有交警处理
    
    @XStreamAlias("SpotTypeCode")
    private String spotTypeCode;  //现场类型
    
    @XStreamAlias("CreateBy")
    private String createBy;  //创建者
    
    @XStreamAlias("CreateTime")
    private String createTime;  //创建日期
    
    @XStreamAlias("UpdateBy")
    private String updateBy;  //更新者
    
    @XStreamAlias("UpdateTime")
    private String updateTime;  //更新日期

    
    public String getDriverCode() {
        return driverCode;
    }

    
    public void setDriverCode(String driverCode) {
        this.driverCode = driverCode;
    }

    
    public String getDriverName() {
        return driverName;
    }

    
    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    
    public String getLossTime() {
        return lossTime;
    }

    
    public void setLossTime(String lossTime) {
        this.lossTime = lossTime;
    }

    
    public String getLossDesc() {
        return lossDesc;
    }

    
    public void setLossDesc(String lossDesc) {
        this.lossDesc = lossDesc;
    }

    
    public String getLossAreaProvince() {
        return lossAreaProvince;
    }

    
    public void setLossAreaProvince(String lossAreaProvince) {
        this.lossAreaProvince = lossAreaProvince;
    }

    
    public String getLossAreaCity() {
        return lossAreaCity;
    }

    
    public void setLossAreaCity(String lossAreaCity) {
        this.lossAreaCity = lossAreaCity;
    }

    
    public String getLossAreaRegion() {
        return lossAreaRegion;
    }

    
    public void setLossAreaRegion(String lossAreaRegion) {
        this.lossAreaRegion = lossAreaRegion;
    }

    
    public String getOcccurredCountryside() {
        return occcurredCountryside;
    }

    
    public void setOcccurredCountryside(String occcurredCountryside) {
        this.occcurredCountryside = occcurredCountryside;
    }

    
    public String getOccurrencyAreaCode() {
        return occurrencyAreaCode;
    }

    
    public void setOccurrencyAreaCode(String occurrencyAreaCode) {
        this.occurrencyAreaCode = occurrencyAreaCode;
    }

    
    public String getOccurrencyRoute() {
        return occurrencyRoute;
    }

    
    public void setOccurrencyRoute(String occurrencyRoute) {
        this.occurrencyRoute = occurrencyRoute;
    }

    
    public String getDriverLicenseNo() {
        return driverLicenseNo;
    }

    
    public void setDriverLicenseNo(String driverLicenseNo) {
        this.driverLicenseNo = driverLicenseNo;
    }

    
    public String getLossCauseCode() {
        return lossCauseCode;
    }

    
    public void setLossCauseCode(String lossCauseCode) {
        this.lossCauseCode = lossCauseCode;
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

    
    public String getIncidentTypeCode() {
        return incidentTypeCode;
    }

    
    public void setIncidentTypeCode(String incidentTypeCode) {
        this.incidentTypeCode = incidentTypeCode;
    }

    
    public String getOcccurredSpot() {
        return occcurredSpot;
    }

    
    public void setOcccurredSpot(String occcurredSpot) {
        this.occcurredSpot = occcurredSpot;
    }

    
    public String getOcccurredSpotAttr() {
        return occcurredSpotAttr;
    }

    
    public void setOcccurredSpotAttr(String occcurredSpotAttr) {
        this.occcurredSpotAttr = occcurredSpotAttr;
    }

    
    public String getPoliceTreatment() {
        return policeTreatment;
    }

    
    public void setPoliceTreatment(String policeTreatment) {
        this.policeTreatment = policeTreatment;
    }

    
    public String getSpotTypeCode() {
        return spotTypeCode;
    }

    
    public void setSpotTypeCode(String spotTypeCode) {
        this.spotTypeCode = spotTypeCode;
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
