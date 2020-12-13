/******************************************************************************
* CREATETIME : 2016年4月8日 下午7:05:30
******************************************************************************/
package ins.sino.claimcar.subrogation.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;


/**
 * @author ★YangKun
 * @CreateTime 2016年4月8日
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class BiRecoveryReturnConfirmDataVo {

	@XmlElement(name = "ClaimSequenceNo", required = true)
	private String claimSequenceNo;//理赔编号

	@XmlElement(name = "ClaimNotificationNo", required = true)
	private String claimNotificationNo;//报案号

	@XmlElement(name = "SerialNo", required = true)
	private String serialNo;//追偿/清付序号

	@XmlElement(name = "RecoveryOrPayType", required = true)
	private String recoveryOrPayType;//追偿/清付类型；参见代码

	@XmlElement(name = "PayMan")
	private String payMan;//清付人

	@XmlElement(name = "RecoveryCode")
	private String recoveryCode;//结算码

	@XmlElement(name = "RecoverAmount", required = true)
	private Double recoverAmount;//追回金额


	/** 
	 * @return 返回 claimSequenceNo  理赔编号
	 */ 
	public String getClaimSequenceNo(){ 
	    return claimSequenceNo;
	}

	/** 
	 * @param claimSequenceNo 要设置的 理赔编号
	 */ 
	public void setClaimSequenceNo(String claimSequenceNo){ 
	    this.claimSequenceNo=claimSequenceNo;
	}

	/** 
	 * @return 返回 claimNotificationNo  报案号
	 */ 
	public String getClaimNotificationNo(){ 
	    return claimNotificationNo;
	}

	/** 
	 * @param claimNotificationNo 要设置的 报案号
	 */ 
	public void setClaimNotificationNo(String claimNotificationNo){ 
	    this.claimNotificationNo=claimNotificationNo;
	}

	/** 
	 * @return 返回 serialNo  追偿/清付序号
	 */ 
	public String getSerialNo(){ 
	    return serialNo;
	}

	/** 
	 * @param serialNo 要设置的 追偿/清付序号
	 */ 
	public void setSerialNo(String serialNo){ 
	    this.serialNo=serialNo;
	}

	/** 
	 * @return 返回 recoveryOrPayType  追偿/清付类型；参见代码
	 */ 
	public String getRecoveryOrPayType(){ 
	    return recoveryOrPayType;
	}

	/** 
	 * @param recoveryOrPayType 要设置的 追偿/清付类型；参见代码
	 */ 
	public void setRecoveryOrPayType(String recoveryOrPayType){ 
	    this.recoveryOrPayType=recoveryOrPayType;
	}

	/** 
	 * @return 返回 payMan  清付人
	 */ 
	public String getPayMan(){ 
	    return payMan;
	}

	/** 
	 * @param payMan 要设置的 清付人
	 */ 
	public void setPayMan(String payMan){ 
	    this.payMan=payMan;
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

	/** 
	 * @return 返回 recoverAmount  追回金额
	 */ 
	public Double getRecoverAmount(){ 
	    return recoverAmount;
	}

	/** 
	 * @param recoverAmount 要设置的 追回金额
	 */ 
	public void setRecoverAmount(Double recoverAmount){ 
	    this.recoverAmount=recoverAmount;
	}



}
