/******************************************************************************
* CREATETIME : 2016年3月16日 下午2:53:04
******************************************************************************/
package ins.sino.claimcar.subrogation.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

//追回款信息列表
/**
 * @author ★YangKun
 * @CreateTime 2016年3月16日
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CiSubrogationRecoveryConfirmDataVo {
	/** 清付人 **/ 
	@XmlElement(name="RECOVERY_MAN")
	private String recoveryMan; 

	/** 结算码 **/ 
	@XmlElement(name="RECOVERY_CODE")
	private String recoveryCode;

	public String getRecoveryMan() {
		return recoveryMan;
	}

	public void setRecoveryMan(String recoveryMan) {
		this.recoveryMan = recoveryMan;
	}

	public String getRecoveryCode() {
		return recoveryCode;
	}

	public void setRecoveryCode(String recoveryCode) {
		this.recoveryCode = recoveryCode;
	} 




}
