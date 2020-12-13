/******************************************************************************
* CREATETIME : 2016年3月16日 下午2:53:04
******************************************************************************/
package ins.sino.claimcar.subrogation.vo;



import java.io.Serializable;
import java.util.Date;



/**
 * 结算码查询 页面返回VO
 * @author ★YangKun
 * @CreateTime 2016年3月16日
 */
public class RecoveryResultVo implements Serializable{

	private static final long serialVersionUID = 1L;
	
	/** 结算码 **/ 
	private String recoveryCode; 

	/** 结算码状态 **/ 
	private String recoveryCodeStatus; 

	/** 结算码失效时间 **/ 
	private Date failureTime; 

	/** 结算码失效原因 **/ 
	private String failureCause; 

	/** 本方追偿状态 **/ 
	private String recoverStatus; 

	/** 对方报案号 **/ 
	private String claimNotificationNo; 

	/** 对方保单险种类型 **/ 
	private String coverageType;

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

	public Date getFailureTime() {
		return failureTime;
	}

	public void setFailureTime(Date failureTime) {
		this.failureTime = failureTime;
	}

	public String getFailureCause() {
		return failureCause;
	}

	public void setFailureCause(String failureCause) {
		this.failureCause = failureCause;
	}

	public String getRecoverStatus() {
		return recoverStatus;
	}

	public void setRecoverStatus(String recoverStatus) {
		this.recoverStatus = recoverStatus;
	}
	
	public String getClaimNotificationNo() {
		return claimNotificationNo;
	}

	
	public void setClaimNotificationNo(String claimNotificationNo) {
		this.claimNotificationNo = claimNotificationNo;
	}

	public String getCoverageType() {
		return coverageType;
	}

	public void setCoverageType(String coverageType) {
		this.coverageType = coverageType;
	}
	
	
}
