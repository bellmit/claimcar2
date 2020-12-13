/******************************************************************************
* CREATETIME : 2016年3月30日 上午11:53:34
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
public class BiLockedConfirmResBaseVo {

	/** 结算码 **/
	@XmlElement(name="RecoveryCode")
	private String recoveryCode;

	
	public String getRecoveryCode() {
		return recoveryCode;
	}

	
	public void setRecoveryCode(String recoveryCode) {
		this.recoveryCode = recoveryCode;
	}
	
	
}
