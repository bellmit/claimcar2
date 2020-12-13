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
public class BiRecoveryQueryDataVo {
	
	
	/** 结算码 **/ 
	@XmlElement(name="RecoveryCode", required = true)
	private String recoveryCode; 

	/** 结算码状态 **/ 
	@XmlElement(name="RecoveryCodeStatus", required = true)
	private String recoveryCodeStatus; 

	/** 结算码失效时间 **/ 
	@XmlElement(name="FailureTime")
	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	private Date failureTime; 

	/** 结算码失效原因 **/ 
	@XmlElement(name="FailureCause")
	private String failureCause; 

	/** 本方追偿状态 **/ 
	@XmlElement(name="RecoverStatus", required = true)
	private String recoverStatus; 

	/** 对方报案号 **/ 
	@XmlElement(name="ClaimNotificationNo", required = true)
	private String claimNotificationNo; 

	/** 对方保单险种类型 **/ 
	@XmlElement(name="CoverageType", required = true)
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
