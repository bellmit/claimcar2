/******************************************************************************
* CREATETIME : 2016年3月16日 下午2:53:04
******************************************************************************/
package ins.sino.claimcar.subrogation.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

//损失赔偿情况列表(隶属于理算核赔信息)
/**
 * @author ★YangKun
 * @CreateTime 2016年3月16日
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class BiUndwrtWriteCoverDataVo {
	@XmlElement(name = "SerialNo", required = true)
	private String serialNo;//损失赔偿序号

	@XmlElement(name = "RecoveryOrPayFlag", required = true)
	private String recoveryOrPayFlag;//追偿/清付标志；参见代码

	@XmlElement(name = "CoverageCode", required = true)
	private String coverageCode;//赔偿险种代码；参见代码

	@XmlElement(name = "LossFeeType", required = true)
	private String lossFeeType;//损失赔偿类型；参见代码

	@XmlElement(name = "liabilityRate")
	private String liabilityRate;//赔偿责任比例

	@XmlElement(name = "ClaimAmount", required = true)
	private Double claimAmount;//赔款金额

	@XmlElement(name = "SalvageFee")
	private Double salvageFee;//施救费


	/** 
	 * @return 返回 serialNo  损失赔偿序号
	 */ 
	public String getSerialNo(){ 
	    return serialNo;
	}

	/** 
	 * @param serialNo 要设置的 损失赔偿序号
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
	 * @return 返回 coverageCode  赔偿险种代码；参见代码
	 */ 
	public String getCoverageCode(){ 
	    return coverageCode;
	}

	/** 
	 * @param coverageCode 要设置的 赔偿险种代码；参见代码
	 */ 
	public void setCoverageCode(String coverageCode){ 
	    this.coverageCode=coverageCode;
	}

	/** 
	 * @return 返回 lossFeeType  损失赔偿类型；参见代码
	 */ 
	public String getLossFeeType(){ 
	    return lossFeeType;
	}

	/** 
	 * @param lossFeeType 要设置的 损失赔偿类型；参见代码
	 */ 
	public void setLossFeeType(String lossFeeType){ 
	    this.lossFeeType=lossFeeType;
	}

	/** 
	 * @return 返回 liabilityRate  赔偿责任比例
	 */ 
	public String getLiabilityRate(){ 
	    return liabilityRate;
	}

	/** 
	 * @param liabilityRate 要设置的 赔偿责任比例
	 */ 
	public void setLiabilityRate(String liabilityRate){ 
	    this.liabilityRate=liabilityRate;
	}

	/** 
	 * @return 返回 claimAmount  赔款金额
	 */ 
	public Double getClaimAmount(){ 
	    return claimAmount;
	}

	/** 
	 * @param claimAmount 要设置的 赔款金额
	 */ 
	public void setClaimAmount(Double claimAmount){ 
	    this.claimAmount=claimAmount;
	}

	/** 
	 * @return 返回 salvageFee  施救费
	 */ 
	public Double getSalvageFee(){ 
	    return salvageFee;
	}

	/** 
	 * @param salvageFee 要设置的 施救费
	 */ 
	public void setSalvageFee(Double salvageFee){ 
	    this.salvageFee=salvageFee;
	}



}
