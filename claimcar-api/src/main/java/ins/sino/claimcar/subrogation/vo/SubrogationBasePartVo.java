/******************************************************************************
* CREATETIME : 2016年3月17日 下午7:39:34
******************************************************************************/
package ins.sino.claimcar.subrogation.vo;


//理赔基本信息
/**
 * @author ★YangKun
 * @CreateTime 2016年3月17日
 */
public class SubrogationBasePartVo  implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	/** 结算码 **/ 
	private String recoveryCode; 

	/** 结算码状态 **/ 
	private String recoveryCodeStatus; 

	/** 对方案件理赔编号 **/ 
	private String claimCode; 
	private String claimSequenceNo;//对方案件理赔编码
	/** 对方案件状态代码 **/ 
	private String claimStatus; 

	/** 对方案件注销原因 **/ 
	private String cancelCause; 

	/** 对方案件进展  **/ 
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

	public String getClaimSequenceNo() {
		return claimSequenceNo;
	}

	public void setClaimSequenceNo(String claimSequenceNo) {
		this.claimSequenceNo = claimSequenceNo;
	}
	
}
