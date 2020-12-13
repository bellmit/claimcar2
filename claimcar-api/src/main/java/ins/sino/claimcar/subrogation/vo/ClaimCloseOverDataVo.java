/******************************************************************************
* CREATETIME : 2016年3月17日 下午7:39:34
******************************************************************************/
package ins.sino.claimcar.subrogation.vo;

import java.io.Serializable;


//损失赔信息列表偿情况 
/**
 * @author ★YangKun
 * @CreateTime 2016年3月17日
 */
public class ClaimCloseOverDataVo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	/** 损失赔偿序号 **/ 
	private String serialNo; 

	/** 追偿/清付标志；参见代码 **/ 
	private String recoveryOrPayFlag; 

	/** 赔偿险种代码；参见代码 **/ 
	private String coverageCode; 

	/** 损失赔偿类型代码；参见代码 **/ 
	private String claimFeeType; 

	/** 赔偿责任比例 **/ 
	private String liabilityRate; 

	/** 赔偿金额 **/ 
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
