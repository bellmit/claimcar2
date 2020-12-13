package ins.sino.claimcar.subrogation.vo;

import java.io.Serializable;

/**
 *  开始追偿确认查询返回页面VO
 * @author ★YangKun
 * @CreateTime 2016年3月18日
 */
public class ConfirmVo implements Serializable{

	/**  */
	private static final long serialVersionUID = 1L;
	private String registNo;   //本方报案号
	/** 对方保单险种类型 **/ 
	private String coverageType;
	
	/** 结算码状态 **/ 
	private String recoveryCodeStatus; 
	/** 锁定取消时间**/ 
	private String lockCancelTime;


	/** 锁定取消原因**/ 
	private String lockCancelReason;
	
	/** 车牌号码**/ 
	private String licenseNo;
	
	/** 车牌号码**/ 
	private String  licenseType;

	public String getRegistNo() {
		return registNo;
	}

	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}

	public String getCoverageType() {
		return coverageType;
	}

	public void setCoverageType(String coverageType) {
		this.coverageType = coverageType;
	}

	public String getRecoveryCodeStatus() {
		return recoveryCodeStatus;
	}

	public void setRecoveryCodeStatus(String recoveryCodeStatus) {
		this.recoveryCodeStatus = recoveryCodeStatus;
	}

	public String getLockCancelTime() {
		return lockCancelTime;
	}

	public void setLockCancelTime(String lockCancelTime) {
		this.lockCancelTime = lockCancelTime;
	}

	public String getLockCancelReason() {
		return lockCancelReason;
	}

	public void setLockCancelReason(String lockCancelReason) {
		this.lockCancelReason = lockCancelReason;
	}

	public String getLicenseNo() {
		return licenseNo;
	}

	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}

	public String getLicenseType() {
		return licenseType;
	}

	public void setLicenseType(String licenseType) {
		this.licenseType = licenseType;
	}


	

}
