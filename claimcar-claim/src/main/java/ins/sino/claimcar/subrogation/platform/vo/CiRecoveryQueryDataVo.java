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
public class CiRecoveryQueryDataVo {
	/** 结算码 **/ 
	@XmlElement(name="RECOVERY_CODE", required = true)
	private String recoveryCode; 

	/** 结算码状态 **/ 
	@XmlElement(name="RECOVERY_CODE_STATUS", required = true)
	private String recoveryCodeStatus; 

	/** 结算码失效时间 **/ 
	@XmlElement(name="FAILURE_TIME")
	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	private Date failureTime; 

	/** 结算码失效原因 **/ 
	@XmlElement(name="FAILURE_CAUSE")
	private String failureCause; 

	/** 本方追偿状态 **/ 
	@XmlElement(name="RECOVER_STATUS", required = true)
	private String recoverStatus; 

	/** 对方报案号 **/ 
	@XmlElement(name="REPORT_NO", required = true)
	private String reportNo; 

	/** 对方保单险种类型 **/ 
	@XmlElement(name="COVERAGE_TYPE", required = true)
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

	public String getReportNo() {
		return reportNo;
	}

	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}

	public String getCoverageType() {
		return coverageType;
	}

	public void setCoverageType(String coverageType) {
		this.coverageType = coverageType;
	} 


	
}
