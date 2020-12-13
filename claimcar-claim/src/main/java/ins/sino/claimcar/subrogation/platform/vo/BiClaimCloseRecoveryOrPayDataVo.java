/******************************************************************************
* CREATETIME : 2016年3月16日 下午2:53:04
******************************************************************************/
package ins.sino.claimcar.subrogation.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

//追偿/清付信息列表(隶属于结案信息)
/**
 * @author ★YangKun
 * @CreateTime 2016年3月16日
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class BiClaimCloseRecoveryOrPayDataVo {
	@XmlElement(name = "SerialNo", required = true)
	private String serialNo;//追偿/清付序号

	@XmlElement(name = "RecoveryOrPayFlag", required = true)
	private String recoveryOrPayFlag;//追偿/清付标志；参见代码

	@XmlElement(name = "RecoveryOrPayType", required = true)
	private String recoveryOrPayType;//追偿/清付类型；参见代码

	@XmlElement(name = "RecoveryOrPayMan")
	private String recoveryOrPayMan;//追偿/清付人

	@XmlElement(name = "RecoveryCode")
	private String recoveryCode;//结算码

	@XmlElement(name = "RecoveryOrPayAmount")
	private Double recoveryOrPayAmount;//追偿/清付金额

	@XmlElement(name = "RecoveryRemark")
	private String recoveryRemark;//清付备注


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
	 * @return 返回 recoveryOrPayFlag  追偿/清付标志；参见代码
	 */ 
	public String getRecoveryOrPayFlag(){ 
	    return recoveryOrPayFlag;
	}

	/** 
	 * @param recoveryOrPayFlag 要设置的 追偿/清付标志；参见代码
	 */ 
	public void setRecoveryOrPayFlag(String recoveryOrPayFlag){ 
	    this.recoveryOrPayFlag=recoveryOrPayFlag;
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
	 * @return 返回 recoveryOrPayMan  追偿/清付人
	 */ 
	public String getRecoveryOrPayMan(){ 
	    return recoveryOrPayMan;
	}

	/** 
	 * @param recoveryOrPayMan 要设置的 追偿/清付人
	 */ 
	public void setRecoveryOrPayMan(String recoveryOrPayMan){ 
	    this.recoveryOrPayMan=recoveryOrPayMan;
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
	 * @return 返回 recoveryOrPayAmount  追偿/清付金额
	 */ 
	public Double getRecoveryOrPayAmount(){ 
	    return recoveryOrPayAmount;
	}

	/** 
	 * @param recoveryOrPayAmount 要设置的 追偿/清付金额
	 */ 
	public void setRecoveryOrPayAmount(Double recoveryOrPayAmount){ 
	    this.recoveryOrPayAmount=recoveryOrPayAmount;
	}

	/** 
	 * @return 返回 recoveryRemark  清付备注
	 */ 
	public String getRecoveryRemark(){ 
	    return recoveryRemark;
	}

	/** 
	 * @param recoveryRemark 要设置的 清付备注
	 */ 
	public void setRecoveryRemark(String recoveryRemark){ 
	    this.recoveryRemark=recoveryRemark;
	}




}
