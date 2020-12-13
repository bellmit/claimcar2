/******************************************************************************
* CREATETIME : 2016年3月16日 下午2:53:04
******************************************************************************/
package ins.sino.claimcar.subrogation.vo;



import java.io.Serializable;
import java.util.Date;



/**
 * 被代位信息查询 页面返回VO
 * @author ★YangKun
 * @CreateTime 2016年3月16日
 */
public class BeSubrogationVo implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	/** 追偿方保险公司 **/ 
	private String insurerCode; 

	/** 追偿方承保地区 **/ 
	private String insurerArea; 

	/** 追偿方保单险种类型 **/ 
	private String coverageType; 

	/** 追偿方保单号 **/ 
	private String policyNo; 

	/** 追偿方报案号 **/ 
	private String claimNotificationNo; 
	
	/** 结算码 **/ 
	private String recoveryCode; 

	/** 锁定时间 **/ 
	private Date lockedTime; 

	/** 追偿方案件状态 **/ 
	private String claimStatus; 
	
	/** 结算码状态代码 **/ 
	private String recoveryCodeStatus;

	/** 追偿方案件进展 **/ 
	private String claimProgress; 

	/** 追偿方车辆号牌号码 **/ 
	private String licensePlateNo; 

	/** 追偿方车辆号牌种类 **/ 
	private String licensePlateType; 

	/** 追偿方车辆发动机号 **/ 
	private String engineNo; 

	/** 追偿方车辆VIN码 **/ 
	private String vin; 

	/** 责任对方理赔编码 **/ 
	private String oppoentClaimSequenceNo; 

	/** 责任对方报案号 **/ 
	private String oppoentClaimNotificationNo;

	public String getClaimProgress() {
		return claimProgress;
	}

	public void setClaimProgress(String claimProgress) {
		this.claimProgress = claimProgress;
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

	public String getRecoveryCodeStatus() {
		return recoveryCodeStatus;
	}

	public void setRecoveryCodeStatus(String recoveryCodeStatus) {
		this.recoveryCodeStatus = recoveryCodeStatus;
	} 


	
}
