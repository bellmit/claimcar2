package ins.sino.claimcar.platform.vo;

import ins.sino.claimcar.carplatform.adapter.DateXmlAdapter;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
public class CarInfoHistoryResBasePartDataVo {
	@XmlElement(name = "ClaimNotificationNo", required = true)
	private String claimNotificationNo;// 报案号
	
	@XmlElement(name = "InsurerCode", required = true)
	private String insurerCode;//保险公司；参加代码
	
	@XmlElement(name = "InsurerArea", required = true)
	private String insurerArea;//承保地区；参加代码
	
	@XmlElement(name = "VehicleProperty", required = true)
	private String vehicleProperty;//本车/三者车,车辆属性；参加代码
	
	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	@XmlElement(name = "NotificationTime", required = true)
	private Date notificationTime;//报案时间；精确到分
	
	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	@XmlElement(name = "LossTime", required = true)
	private Date lossTime;//出险时间；精确到分
	
    @XmlElement(name = "LossArea", required = true)
	private String lossArea;//出险地点
    
    @XmlElement(name = "LossDesc")
    private String lossDesc;//出险经过
    
    @XmlElement(name = "ClaimStatus",required = true)
    private String claimStatus;//案件状态代码；参见代码
    
    @XmlElement(name = "RiskType",required = true)
    private String riskType;//保单类型代码；参见代码
    
    @XmlElement(name = "LicensePlateType")
	private String licensePlateType;// 号牌种类代码；参见代码
    
    @XmlElement(name = "LicensePlateNo")
	private String licensePlateNo;// 号牌号码
    
	@XmlElement(name = "EngineNo")
	private String engineNo;// 发动机号
	
	@XmlElement(name = "VIN")
	private String vin;// VIN码
	
	@XmlElement(name = "AreaCode")
	private String areaCode;// 地区代码;参加代码

	/**
	 * @return the claimNotificationNo
	 */
	public String getClaimNotificationNo() {
		return claimNotificationNo;
	}

	/**
	 * @param claimNotificationNo the claimNotificationNo to set
	 */
	public void setClaimNotificationNo(String claimNotificationNo) {
		this.claimNotificationNo = claimNotificationNo;
	}

	/**
	 * @return the insurerCode
	 */
	public String getInsurerCode() {
		return insurerCode;
	}

	/**
	 * @param insurerCode the insurerCode to set
	 */
	public void setInsurerCode(String insurerCode) {
		this.insurerCode = insurerCode;
	}

	/**
	 * @return the insurerArea
	 */
	public String getInsurerArea() {
		return insurerArea;
	}

	/**
	 * @param insurerArea the insurerArea to set
	 */
	public void setInsurerArea(String insurerArea) {
		this.insurerArea = insurerArea;
	}

	/**
	 * @return the vehicleProperty
	 */
	public String getVehicleProperty() {
		return vehicleProperty;
	}

	/**
	 * @param vehicleProperty the vehicleProperty to set
	 */
	public void setVehicleProperty(String vehicleProperty) {
		this.vehicleProperty = vehicleProperty;
	}

	/**
	 * @return the notificationTime
	 */
	public Date getNotificationTime() {
		return notificationTime;
	}

	/**
	 * @param notificationTime the notificationTime to set
	 */
	public void setNotificationTime(Date notificationTime) {
		this.notificationTime = notificationTime;
	}

	/**
	 * @return the lossTime
	 */
	public Date getLossTime() {
		return lossTime;
	}

	/**
	 * @param lossTime the lossTime to set
	 */
	public void setLossTime(Date lossTime) {
		this.lossTime = lossTime;
	}

	/**
	 * @return the lossArea
	 */
	public String getLossArea() {
		return lossArea;
	}

	/**
	 * @param lossArea the lossArea to set
	 */
	public void setLossArea(String lossArea) {
		this.lossArea = lossArea;
	}

	/**
	 * @return the lossDesc
	 */
	public String getLossDesc() {
		return lossDesc;
	}

	/**
	 * @param lossDesc the lossDesc to set
	 */
	public void setLossDesc(String lossDesc) {
		this.lossDesc = lossDesc;
	}

	/**
	 * @return the claimStatus
	 */
	public String getClaimStatus() {
		return claimStatus;
	}

	/**
	 * @param claimStatus the claimStatus to set
	 */
	public void setClaimStatus(String claimStatus) {
		this.claimStatus = claimStatus;
	}

	/**
	 * @return the riskType
	 */
	public String getRiskType() {
		return riskType;
	}

	/**
	 * @param riskType the riskType to set
	 */
	public void setRiskType(String riskType) {
		this.riskType = riskType;
	}

	/**
	 * @return the licensePlateType
	 */
	public String getLicensePlateType() {
		return licensePlateType;
	}

	/**
	 * @param licensePlateType the licensePlateType to set
	 */
	public void setLicensePlateType(String licensePlateType) {
		this.licensePlateType = licensePlateType;
	}

	/**
	 * @return the licensePlateNo
	 */
	public String getLicensePlateNo() {
		return licensePlateNo;
	}

	/**
	 * @param licensePlateNo the licensePlateNo to set
	 */
	public void setLicensePlateNo(String licensePlateNo) {
		this.licensePlateNo = licensePlateNo;
	}

	/**
	 * @return the engineNo
	 */
	public String getEngineNo() {
		return engineNo;
	}

	/**
	 * @param engineNo the engineNo to set
	 */
	public void setEngineNo(String engineNo) {
		this.engineNo = engineNo;
	}

	/**
	 * @return the vin
	 */
	public String getVin() {
		return vin;
	}

	/**
	 * @param vin the vin to set
	 */
	public void setVin(String vin) {
		this.vin = vin;
	}

	/**
	 * @return the areaCode
	 */
	public String getAreaCode() {
		return areaCode;
	}

	/**
	 * @param areaCode the areaCode to set
	 */
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
}
