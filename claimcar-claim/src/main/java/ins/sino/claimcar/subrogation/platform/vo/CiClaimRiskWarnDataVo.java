/******************************************************************************
* CREATETIME : 2016年3月16日 下午2:53:04
******************************************************************************/
package ins.sino.claimcar.subrogation.platform.vo;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import ins.sino.claimcar.carplatform.adapter.DateXmlAdapter;


/**
 * @author ★YangKun
 * @CreateTime 2016年3月16日
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CiClaimRiskWarnDataVo {
	
	/** 保险公司 **/ 
	@XmlElement(name="INSURER_CODE", required = true)
	private String insurerCode; 

	/** 承保地区 **/ 
	@XmlElement(name="INSURER_AREA", required = true)
	private String insurerArea; 

	/** 车辆属性(本车/三者车) **/ 
	@XmlElement(name="VEHICLE_PROPERTY", required = true)
	private String vehicleProperty; 

	/** 报案时间 **/ 
	@XmlElement(name="REPORT_TIME", required = true)
	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	private Date reportTime; 

	/** 出险时间 **/ 
	@XmlElement(name="ACCIDENT_TIME", required = true)
	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	private Date accidentTime; 

	/** 出险地点 **/ 
	@XmlElement(name="ACCIDENT_PLACE", required = true)
	private String accidentPlace; 

	/** 出险经过 **/ 
	@XmlElement(name="ACCIDENT_DESCRIPTION")
	private String accidentDescription; 

	/** 案件状态代码 **/ 
	@XmlElement(name="CLAIM_STATUS", required = true)
	private String claimStatus; 

	/** 保单类型代码 **/ 
	@XmlElement(name="RISK_TYPE", required = true)
	private String riskType; 

	/** 号牌号码 **/ 
	@XmlElement(name="CAR_MARK")
	private String carMark; 

	/** 号牌种类代码 **/ 
	@XmlElement(name="VEHICLE_TYPE")
	private String vehicleType; 

	/** 发动机号 **/ 
	@XmlElement(name="ENGINE_NO")
	private String engineNo; 

	/** VIN码 **/ 
	@XmlElement(name="RACK_NO")
	private String rackNo;

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

	public Date getReportTime() {
		return reportTime;
	}

	public void setReportTime(Date reportTime) {
		this.reportTime = reportTime;
	}

	public Date getAccidentTime() {
		return accidentTime;
	}

	public void setAccidentTime(Date accidentTime) {
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

	public String getCarMark() {
		return carMark;
	}

	public void setCarMark(String carMark) {
		this.carMark = carMark;
	}

	public String getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	public String getEngineNo() {
		return engineNo;
	}

	public void setEngineNo(String engineNo) {
		this.engineNo = engineNo;
	}

	public String getRackNo() {
		return rackNo;
	}

	public void setRackNo(String rackNo) {
		this.rackNo = rackNo;
	} 


	
	
}
