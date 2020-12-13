/******************************************************************************
* CREATETIME : 2016年3月17日 下午7:39:34
******************************************************************************/
package ins.sino.claimcar.subrogation.platform.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;


/**
 * @author ★YangKun
 * @CreateTime 2016年3月17日
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class BiLockedPolicyDataVo {
	/** 责任对方保险公司 **/ 
	@XmlElement(name="InsurerCode", required = true)
	private String insurerCode; 

	/** 责任对方承保地区 **/ 
	@XmlElement(name="InsurerArea", required = true)
	private String insurerArea; 

	/** 责任对方保单险种类型 **/ 
	@XmlElement(name="CoverageType", required = true)
	private String coverageType; 
	
	/** 责任对方保单号 **/ 
	@XmlElement(name="PolicyNo", required = true)
	private String policyNo; 

	/** 商三限额 **/ 
	@XmlElement(name="LimitAmount")
	private Double limitAmount;
	
	/** 节假日限额翻倍险限额 **/ 
	@XmlElement(name="FestivalAmount")
	private Double festivalAmount;

	/** 是否承保商三不计免赔 **/ 
	@XmlElement(name="IsInsuredCA")
	private String isInsuredCA; 

	/** 责任对方本车号牌号码 **/ 
	@XmlElement(name="LicensePlateNo")
	private String licensePlateNo; 

	/** 责任对方本车号牌种类 **/ 
	@XmlElement(name="LicensePlateType")
	private String licensePlateType; 

	/** 责任对方本车发动机号 **/ 
	@XmlElement(name="EngineNo")
	private String engineNo; 

	/** 责任对方本车VIN码 **/ 
	@XmlElement(name="VIN")
	private String vin; 

	/** 匹配次数 **/ 
	@XmlElement(name="MatchTimes")
	private String matchTimes;

	@XmlElement(name = "LockedNotifyData")
	private List<LockedNotifyData> prplLockedNotifies;
	
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

	public String getCoverageType() {
		return coverageType;
	}

	public void setCoverageType(String coverageType) {
		this.coverageType = coverageType;
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public Double getLimitAmount() {
		return limitAmount;
	}

	public void setLimitAmount(Double limitAmount) {
		this.limitAmount = limitAmount;
	}

	
	
	public Double getFestivalAmount() {
		return festivalAmount;
	}

	public void setFestivalAmount(Double festivalAmount) {
		this.festivalAmount = festivalAmount;
	}

	public String getIsInsuredCA() {
		return isInsuredCA;
	}

	public void setIsInsuredCA(String isInsuredCA) {
		this.isInsuredCA = isInsuredCA;
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

	public String getMatchTimes() {
		return matchTimes;
	}

	public void setMatchTimes(String matchTimes) {
		this.matchTimes = matchTimes;
	}

	
	public List<LockedNotifyData> getPrplLockedNotifies() {
		return prplLockedNotifies;
	}

	
	public void setPrplLockedNotifies(List<LockedNotifyData> prplLockedNotifies) {
		this.prplLockedNotifies = prplLockedNotifies;
	}
	
	

}
