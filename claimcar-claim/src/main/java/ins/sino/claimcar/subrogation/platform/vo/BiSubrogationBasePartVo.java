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
public class BiSubrogationBasePartVo {


	@XmlElement(name = "RecoveryCode", required = true)
	private String recoveryCode;//结算码

	@XmlElement(name = "RecoveryCodeStatus", required = true)
	private String recoveryCodeStatus;//结算码状态代码

	@XmlElement(name = "ClaimSequenceNo", required = true)
	private String claimSequenceNo;//对方案件理赔编码

	@XmlElement(name = "ClaimStatus", required = true)
	private String claimStatus;//对方案件状态代码

	@XmlElement(name = "CancelCause")
	private String cancelCause;//对方案件注销原因

	@XmlElement(name = "ClaimProgress", required = true)
	private String claimProgress;//对方案件进展


	/** 
	 * @return 返回 recoveryCode  结算码
	 */ 
	public String getRecoveryCode(){ 
	    return recoveryCode;
	}

	/** 
	 * @param recoveryCode 要设置的 结算码
	 */ 
	public void setRecoveryCode(String recoveryCode){ 
	    this.recoveryCode=recoveryCode;
	}

	/** 
	 * @return 返回 recoveryCodeStatus  结算码状态代码
	 */ 
	public String getRecoveryCodeStatus(){ 
	    return recoveryCodeStatus;
	}

	/** 
	 * @param recoveryCodeStatus 要设置的 结算码状态代码
	 */ 
	public void setRecoveryCodeStatus(String recoveryCodeStatus){ 
	    this.recoveryCodeStatus=recoveryCodeStatus;
	}

	/** 
	 * @return 返回 claimSequenceNo  对方案件理赔编码
	 */ 
	public String getClaimSequenceNo(){ 
	    return claimSequenceNo;
	}

	/** 
	 * @param claimSequenceNo 要设置的 对方案件理赔编码
	 */ 
	public void setClaimSequenceNo(String claimSequenceNo){ 
	    this.claimSequenceNo=claimSequenceNo;
	}

	/** 
	 * @return 返回 claimStatus  对方案件状态代码
	 */ 
	public String getClaimStatus(){ 
	    return claimStatus;
	}

	/** 
	 * @param claimStatus 要设置的 对方案件状态代码
	 */ 
	public void setClaimStatus(String claimStatus){ 
	    this.claimStatus=claimStatus;
	}

	/** 
	 * @return 返回 cancelCause  对方案件注销原因
	 */ 
	public String getCancelCause(){ 
	    return cancelCause;
	}

	/** 
	 * @param cancelCause 要设置的 对方案件注销原因
	 */ 
	public void setCancelCause(String cancelCause){ 
	    this.cancelCause=cancelCause;
	}

	/** 
	 * @return 返回 claimProgress  对方案件进展
	 */ 
	public String getClaimProgress(){ 
	    return claimProgress;
	}

	/** 
	 * @param claimProgress 要设置的 对方案件进展
	 */ 
	public void setClaimProgress(String claimProgress){ 
	    this.claimProgress=claimProgress;
	}





	

}
