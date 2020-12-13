/******************************************************************************
* CREATETIME : 2016年3月16日 下午12:10:37
******************************************************************************/
package ins.sino.claimcar.subrogation.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;


/**
 * @author ★YangKun
 * @CreateTime 2016年3月16日
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CiSubrogationClaimBaseVo {
	/** 结算码 **/ 
	@XmlElement(name="RECOVERY_CODE", required = true)
	private String recoveryCode;

	public String getRecoveryCode() {
		return recoveryCode;
	}

	public void setRecoveryCode(String recoveryCode) {
		this.recoveryCode = recoveryCode;
	} 

	
	
}
