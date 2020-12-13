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
public class BiBeSubrogationDataVo {
	/** 追偿方保险公司 **/ 
	@XmlElement(name="InsurerCode", required = true)
	private String insurerCode; 

	/** 追偿方承保地区 **/ 
	@XmlElement(name="InsurerArea", required = true)
	private String insurerArea; 

	/** 追偿方保单险种类型 **/ 
	@XmlElement(name="CoverageType", required = true)
	private String coverageType; 

	/** 追偿方保单号 **/ 
	@XmlElement(name="PolicyNo", required = true)
	private String policyNo; 

	/** 追偿方报案号 **/ 
	@XmlElement(name="ClaimNotificationNo")
	private String claimNotificationNo; 

	/** 结算码 **/ 
	@XmlElement(name="RecoveryCode", required = true)
	private String recoveryCode; 

	/** 锁定时间 **/ 
	@XmlElement(name="LockedTime")
	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	private Date lockedTime; 

	/** 追偿方案件状态 **/ 
	@XmlElement(name="ClaimStatus")
	private String claimStatus; 

	/** 追偿方案件进展 **/ 
	@XmlElement(name="ClaimProgress", required = true)
	private String claimProgress; 

	/** 结算码状态代码 **/ 
	@XmlElement(name="RecoveryCodeStatus", required = true)
	private String recoveryCodeStatus; 

	/** 追偿方车辆号牌号码 **/ 
	@XmlElement(name="LicensePlateNo")
	private String licensePlateNo; 

	/** 追偿方车辆号牌种类 **/ 
	@XmlElement(name="LicensePlateType")
	private String licensePlateType; 

	/** 追偿方车辆发动机号 **/ 
	@XmlElement(name="EngineNo")
	private String engineNo; 

	/** 追偿方车辆VIN码 **/ 
	@XmlElement(name="VIN")
	private String vin; 

	/** 责任对方理赔编码 **/ 
	@XmlElement(name="OppoentClaimSequenceNo", required = true)
	private String oppoentClaimSequenceNo; 

	/** 责任对方报案号 **/ 
	@XmlElement(name="OppoentClaimNotificationNo", required = true)
	private String oppoentClaimNotificationNo;

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

	public String getClaimNotificationNo() {
		return claimNotificationNo;
	}

	public void setClaimNotificationNo(String claimNotificationNo) {
		this.claimNotificationNo = claimNotificationNo;
	}

	public String getRecoveryCode() {
		return recoveryCode;
	}

	public void setRecoveryCode(String recoveryCode) {
		this.recoveryCode = recoveryCode;
	}

	public Date getLockedTime() {
		return lockedTime;
	}

	public void setLockedTime(Date lockedTime) {
		this.lockedTime = lockedTime;
	}

	public String getClaimStatus() {
		return claimStatus;
	}

	public void setClaimStatus(String claimStatus) {
		this.claimStatus = claimStatus;
	}

	public String getClaimProgress() {
		return claimProgress;
	}

	public void setClaimProgress(String claimProgress) {
		this.claimProgress = claimProgress;
	}

	public String getRecoveryCodeStatus() {
		return recoveryCodeStatus;
	}

	public void setRecoveryCodeStatus(String recoveryCodeStatus) {
		this.recoveryCodeStatus = recoveryCodeStatus;
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

	public String getvin() {
		return vin;
	}

	public void setvin(String vin) {
		this.vin = vin;
	}

	public String getOppoentClaimSequenceNo() {
		return oppoentClaimSequenceNo;
	}

	public void setOppoentClaimSequenceNo(String oppoentClaimSequenceNo) {
		this.oppoentClaimSequenceNo = oppoentClaimSequenceNo;
	}

	public String getOppoentClaimNotificationNo() {
		return oppoentClaimNotificationNo;
	}

	public void setOppoentClaimNotificationNo(String oppoentClaimNotificationNo) {
		this.oppoentClaimNotificationNo = oppoentClaimNotificationNo;
	} 


}
