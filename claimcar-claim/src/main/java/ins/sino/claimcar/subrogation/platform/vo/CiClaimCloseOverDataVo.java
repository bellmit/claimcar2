/******************************************************************************
* CREATETIME : 2016年3月16日 下午2:53:04
******************************************************************************/
package ins.sino.claimcar.subrogation.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

//损失赔偿情况列表(隶属于结案信息)
/**
 * @author ★YangKun
 * @CreateTime 2016年3月16日
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CiClaimCloseOverDataVo {
	/** 损失赔偿序号 **/ 
	@XmlElement(name="SERIAL_NO", required = true)
	private String serialNo; 

	/** 追偿/清付标志；参见代码 **/ 
	@XmlElement(name="RECOVERY_OR_PAY_FLAG", required = true)
	private String recoveryOrPayFlag; 

	/** 赔偿险种代码；参见代码 **/ 
	@XmlElement(name="COVERAGE_CODE", required = true)
	private String coverageCode; 

	/** 损失赔偿类型代码；参见代码 **/ 
	@XmlElement(name="CLAIM_FEE_TYPE", required = true)
	private String claimFeeType; 

	/** 赔偿责任比例 **/ 
	@XmlElement(name="LIABILITY_RATE")
	private String liabilityRate; 

	/** 赔偿金额 **/ 
	@XmlElement(name="CLAIM_AMOUNT", required = true)
	private Double claimAmount;

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getRecoveryOrPayFlag() {
		return recoveryOrPayFlag;
	}

	public void setRecoveryOrPayFlag(String recoveryOrPayFlag) {
		this.recoveryOrPayFlag = recoveryOrPayFlag;
	}

	public String getCoverageCode() {
		return coverageCode;
	}

	public void setCoverageCode(String coverageCode) {
		this.coverageCode = coverageCode;
	}

	public String getClaimFeeType() {
		return claimFeeType;
	}

	public void setClaimFeeType(String claimFeeType) {
		this.claimFeeType = claimFeeType;
	}

	public String getLiabilityRate() {
		return liabilityRate;
	}

	public void setLiabilityRate(String liabilityRate) {
		this.liabilityRate = liabilityRate;
	}

	public Double getClaimAmount() {
		return claimAmount;
	}

	public void setClaimAmount(Double claimAmount) {
		this.claimAmount = claimAmount;
	} 




}
