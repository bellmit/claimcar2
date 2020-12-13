package ins.sino.claimcar.subrogation.vo;

import java.io.Serializable;

/**
 *  损失情况列表(隶属于报案信息)
 * @author ★YangKun
 * @CreateTime 2016年3月18日
 */
public class ReportLossDataVo implements Serializable{
	private static final long serialVersionUID = 1L;

	/** 损失项赔偿类型代码 **/ 
	private String claimFeeType;
	private String lossFeeType;//损失项赔偿类型代码；参见代码

	public String getClaimFeeType() {
		return claimFeeType;
	}

	public void setClaimFeeType(String claimFeeType) {
		this.claimFeeType = claimFeeType;
	}

	public String getLossFeeType() {
		return lossFeeType;
	}

	public void setLossFeeType(String lossFeeType) {
		this.lossFeeType = lossFeeType;
	}

	
}
