/******************************************************************************
* CREATETIME : 2016年3月17日 下午7:39:34
******************************************************************************/
package ins.sino.claimcar.subrogation.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;


/**
 * @author ★YangKun
 * @CreateTime 2016年3月17日
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class BiPolicyRiskWarnDataVo {

	/** 保险公司 **/ 
	@XmlElement(name="InsurerCode")
	private String insurerCode; 

	/** 承保地区 **/ 
	@XmlElement(name="InsurerArea")
	private String insurerArea; 

	/** 保单险种类型 **/ 
	@XmlElement(name="CoverageType")
	private String coverageType; 

	/** 商三限额 **/ 
	@XmlElement(name="LimitAmount")
	private Double limitAmount; 
	
	/** 节假日限额翻倍险限额**/ 
	@XmlElement(name="FestivalAmount")
	private Double festivalAmount; 

	/** 是否承保商三不计免赔 **/ 
	@XmlElement(name="IsInsuredCA")
	private String insuredCA; 

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

	public String getInsurerCode() {
		return insurerCode;
	}

	public void setInsurerCode(String insurerCode) {
		this.insurerCode=insurerCode;
	}

	public String getInsurerArea() {
		return insurerArea;
	}

	public void setInsurerArea(String insurerArea) {
		this.insurerArea=insurerArea;
	}

	public String getCoverageType() {
		return coverageType;
	}

	public void setCoverageType(String coverageType) {
		this.coverageType=coverageType;
	}

	public Double getLimitAmount() {
		return limitAmount;
	}

	public void setLimitAmount(Double limitAmount) {
		this.limitAmount=limitAmount;
	}
	
	public String getInsuredCA() {
		return insuredCA;
	}
	
	public void setInsuredCA(String insuredCA) {
		this.insuredCA = insuredCA;
	}

	public String getLicensePlateType() {
		return licensePlateType;
	}

	public void setLicensePlateType(String licensePlateType) {
		this.licensePlateType=licensePlateType;
	}

	public String getLicensePlateNo() {
		return licensePlateNo;
	}

	public void setLicensePlateNo(String licensePlateNo) {
		this.licensePlateNo=licensePlateNo;
	}

	public String getEngineNo() {
		return engineNo;
	}

	public void setEngineNo(String engineNo) {
		this.engineNo=engineNo;
	}

	public String getVin() {
		return vin;
	}

	
	public void setVin(String vin) {
		this.vin = vin;
	}


}
