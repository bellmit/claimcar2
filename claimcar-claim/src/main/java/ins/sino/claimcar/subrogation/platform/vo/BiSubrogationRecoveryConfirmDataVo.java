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
public class BiSubrogationRecoveryConfirmDataVo {
	@XmlElement(name = "RecoveryMan")
	private String recoveryMan;//清付人

	@XmlElement(name = "RecoveryCode")
	private String recoveryCode;//结算码


	/** 
	 * @return 返回 recoveryMan  清付人
	 */ 
	public String getRecoveryMan(){ 
	    return recoveryMan;
	}

	/** 
	 * @param recoveryMan 要设置的 清付人
	 */ 
	public void setRecoveryMan(String recoveryMan){ 
	    this.recoveryMan=recoveryMan;
	}

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




}
