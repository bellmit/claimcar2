/******************************************************************************
* CREATETIME : 2016年3月30日 上午11:57:25
******************************************************************************/
package ins.sino.claimcar.subrogation.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;


/**
 * @author ★YangKun
 * @CreateTime 2016年3月30日
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class BiLockedCancelBaseVo {

	@XmlElement(name = "RecoveryCode", required = true)
	private String recoveryCode;//结算码

	@XmlElement(name = "FailureCause", required = true)
	private String failureCause;//结算码失效原因代码；参见代码


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
	 * @return 返回 failureCause  结算码失效原因代码；参见代码
	 */ 
	public String getFailureCause(){ 
	    return failureCause;
	}

	/** 
	 * @param failureCause 要设置的 结算码失效原因代码；参见代码
	 */ 
	public void setFailureCause(String failureCause){ 
	    this.failureCause=failureCause;
	}



}
