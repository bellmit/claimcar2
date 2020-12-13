/******************************************************************************
* CREATETIME : 2016年4月1日 下午2:59:52
******************************************************************************/
package ins.sino.claimcar.subrogation.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;


/**
 * @author ★YangKun
 * @CreateTime 2016年4月1日
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CiSubrogationBasePartVo {


	/** 结算码 **/ 
	@XmlElement(name="RECOVERY_CODE", required = true)
	private String recoveryCode; 

	/** 结算码状态 **/ 
	@XmlElement(name="RECOVERY_CODE_STATUS", required = true)
	private String recoveryCodeStatus; 

	/** 对方案件理赔编号 **/ 
	@XmlElement(name="CLAIM_CODE", required = true)
	private String claimCode; 

	/** 对方案件状态代码 **/ 
	@XmlElement(name="CLAIM_STATUS", required = true)
	private String claimStatus; 

	/** 对方案件注销原因 **/ 
	@XmlElement(name="CANCEL_CAUSE")
	private String cancelCause; 

	/** 对方案件进展  **/ 
	@XmlElement(name="CLAIM_PROGRESS", required = true)
	private String claimProgress;

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

	public String getClaimCode() {
		return claimCode;
	}

	public void setClaimCode(String claimCode) {
		this.claimCode = claimCode;
	}

	public String getClaimStatus() {
		return claimStatus;
	}

	public void setClaimStatus(String claimStatus) {
		this.claimStatus = claimStatus;
	}

	public String getCancelCause() {
		return cancelCause;
	}

	public void setCancelCause(String cancelCause) {
		this.cancelCause = cancelCause;
	}

	public String getClaimProgress() {
		return claimProgress;
	}

	public void setClaimProgress(String claimProgress) {
		this.claimProgress = claimProgress;
	} 


	

}
