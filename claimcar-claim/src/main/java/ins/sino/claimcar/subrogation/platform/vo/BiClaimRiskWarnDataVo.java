/******************************************************************************
* CREATETIME : 2016年3月17日 下午7:39:34
******************************************************************************/
package ins.sino.claimcar.subrogation.platform.vo;

import java.util.Date;

import ins.sino.claimcar.carplatform.adapter.DateXmlAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * @author ★YangKun
 * @CreateTime 2016年3月17日
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class BiClaimRiskWarnDataVo {
	/** 报案号 **/ 
	@XmlElement(name="ClaimNotificationNo", required = true)
	private String claimNotificationNo; 

	/** 保险公司 **/ 
	@XmlElement(name="InsurerCode", required = true)
	private String insurerCode; 

	/** 承保地区 **/ 
	@XmlElement(name="InsurerArea", required = true)
	private String insurerArea; 

	/** 本车/三者车，车辆属性 **/ 
	@XmlElement(name="VehicleProperty", required = true)
	private String vehicleProperty; 

	/** 报案时间 **/ 
	@XmlElement(name="NotificationTime", required = true)
	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	private Date notificationTime; 

	/** 出险时间 **/ 
	@XmlElement(name="LossTime", required = true)
	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	private Date lossTime; 

	/** 出险地点 **/ 
	@XmlElement(name="LossArea", required = true)
	private String lossArea; 

	/** 出险经过 **/ 
	@XmlElement(name="LossDesc")
	private String lossDesc; 

	/** 案件状态代码 **/ 
	@XmlElement(name="ClaimStatus", required = true)
	private String claimStatus; 

	/** 保单类型代码 **/ 
	@XmlElement(name="RiskType", required = true)
	private String riskType; 

	/** 号牌种类代码 **/ 
	@XmlElement(name="LicensePlateType")
	private String licensePlateType; 

	/** 号牌号码 **/ 
	@XmlElement(name="LicensePlateNo")
	private String licensePlateNo; 

	/** 发动机号 **/ 
	@XmlElement(name="EngineNo")
	private String engineNo; 

	/** VIN码 **/ 
	@XmlElement(name="VIN")
	private String vin;

	public String getClaimNotificationNo() {
		return claimNotificationNo;
	}

	public void setClaimNotificationNo(String claimNotificationNo) {
		this.claimNotificationNo = claimNotificationNo;
	}

	public String getInsurerCode() {
		return insurerCode;
	}

	public void setInsurerCode(String insurerCode) {
		this.insurerCode = insurerCode;
	}

	public String getInsurerArea() {
		return insurerArea;
	}

	public void setInsurerArea(String insurerArea) {
		this.insurerArea = insurerArea;
	}

	public String getVehicleProperty() {
		return vehicleProperty;
	}

	public void setVehicleProperty(String vehicleProperty) {
		this.vehicleProperty = vehicleProperty;
	}

	public Date getNotificationTime() {
		return notificationTime;
	}

	public void setNotificationTime(Date notificationTime) {
		this.notificationTime = notificationTime;
	}

	public Date getLossTime() {
		return lossTime;
	}

	public void setLossTime(Date lossTime) {
		this.lossTime = lossTime;
	}

	public String getLossArea() {
		return lossArea;
	}

	public void setLossArea(String lossArea) {
		this.lossArea = lossArea;
	}

	public String getLossDesc() {
		return lossDesc;
	}

	public void setLossDesc(String lossDesc) {
		this.lossDesc = lossDesc;
	}

	public String getClaimStatus() {
		return claimStatus;
	}

	public void setClaimStatus(String claimStatus) {
		this.claimStatus = claimStatus;
	}

	public String getRiskType() {
		return riskType;
	}

	public void setRiskType(String riskType) {
		this.riskType = riskType;
	}

	public String getLicensePlateType() {
		return licensePlateType;
	}

	public void setLicensePlateType(String licensePlateType) {
		this.licensePlateType = licensePlateType;
	}

	public String getLicensePlateNo() {
		return licensePlateNo;
	}

	public void setLicensePlateNo(String licensePlateNo) {
		this.licensePlateNo = licensePlateNo;
	}

	public String getEngineNo() {
		return engineNo;
	}

	public void setEngineNo(String engineNo) {
		this.engineNo = engineNo;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	} 






}
