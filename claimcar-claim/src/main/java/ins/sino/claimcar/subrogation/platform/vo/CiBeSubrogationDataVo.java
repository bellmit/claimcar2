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
public class CiBeSubrogationDataVo {
	
	/** 追偿方保险公司 **/ 
	@XmlElement(name="INSURER_CODE", required = true)
	private String insurerCode; 

	/** 追偿方承保地区 **/ 
	@XmlElement(name="INSURER_AREA", required = true)
	private String insurerArea; 

	/** 追偿方保单险种类型 **/ 
	@XmlElement(name="COVERAGE_TYPE", required = true)
	private String coverageType; 

	/** 追偿方保单号 **/ 
	@XmlElement(name="POLICY_NO", required = true)
	private String policyNo; 

	/** 追偿方报案号 **/ 
	@XmlElement(name="REPORT_NO")
	private String reportNo; 

	/** 结算码 **/ 
	@XmlElement(name="RECOVERY_CODE", required = true)
	private String recoveryCode; 

	/** 结算码状态 **/ 
	@XmlElement(name="RECOVERY_CODE_STATUS", required = true)
	private String recoveryCodeStatus; 

	/** 锁定时间 **/ 
	@XmlElement(name="LOCKED_TIME")
	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	private Date lockedTime; 

	/** 追偿方案件状态 **/ 
	@XmlElement(name="CLAIM_STATUS")
	private String claimStatus; 

	/** 追偿方案件进展  **/ 
	@XmlElement(name="CLAIM_PROGRESS", required = true)
	private String claimProgress; 

	/** 追偿方车辆号牌号码 **/ 
	@XmlElement(name="CAR_MARK")
	private String carMark; 

	/** 追偿方车辆号牌种类 **/ 
	@XmlElement(name="VEHICLE_TYPE")
	private String vehicleType; 

	/** 追偿方车辆发动机号 **/ 
	@XmlElement(name="ENGINE_NO")
	private String engineNo; 

	/** 追偿方车辆VIN码 **/ 
	@XmlElement(name="RACK_NO")
	private String rackNo; 

	/** 责任对方理赔编码 **/ 
	@XmlElement(name="OPPOENT_CLAIM_CODE", required = true)
	private String oppoentClaimCode; 

	/** 责任对方报案号 **/ 
	@XmlElement(name="OPPOENT_REPORT_NO", required = true)
	private String oppoentReportNo;

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

	public String getReportNo() {
		return reportNo;
	}

	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}

	public String getRecoveryCode() {
		return recoveryCode;
	}

	public void setRecoveryCode(String recoveryCode) {
		this.recoveryCode = recoveryCode;
	}

	public String getRecoveryCodeStatus() {
		return recoveryCodeStatus;
	}

	public void setRecoveryCodeStatus(String recoveryCodeStatus) {
		this.recoveryCodeStatus = recoveryCodeStatus;
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

	public String getOppoentClaimCode() {
		return oppoentClaimCode;
	}

	public void setOppoentClaimCode(String oppoentClaimCode) {
		this.oppoentClaimCode = oppoentClaimCode;
	}

	public String getOppoentReportNo() {
		return oppoentReportNo;
	}

	public void setOppoentReportNo(String oppoentReportNo) {
		this.oppoentReportNo = oppoentReportNo;
	} 




	
	
}
