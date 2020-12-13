/******************************************************************************
* CREATETIME : 2016年3月17日 下午7:39:34
******************************************************************************/
package ins.sino.claimcar.subrogation.vo;

import java.io.Serializable;
import java.util.List;

//理算信息列表(隶属于理算核赔信息)
/**
 * @author ★YangKun
 * @CreateTime 2016年3月17日
 */
public class UndwrtWriteAdjustmentDataVo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	/** 理算编码 **/ 
	private String adjustmentCode; 

	/** 其他费用 **/ 
	private Double otherFee; 

	/** 核赔意见 **/ 
	private String underWriteDes; 
	private String underWriteDesc; 

	/** 赔偿金额 **/ 
	private Double claimAmount;

	/**	损失赔偿情况信息列表 */
	private List<UndwrtWriteCoverDataVo> coverDataList;
	
	/**	追偿/清付信息列表  */
	private List<UndwrtWriteRecoveryOrPayDataVo> recoveryOrPayDataList;
	
	public String getAdjustmentCode() {
		return adjustmentCode;
	}

	public void setAdjustmentCode(String adjustmentCode) {
		this.adjustmentCode = adjustmentCode;
	}

	public Double getOtherFee() {
		return otherFee;
	}

	public void setOtherFee(Double otherFee) {
		this.otherFee = otherFee;
	}

	

	public String getUnderWriteDesc() {
		return underWriteDesc;
	}

	public void setUnderWriteDesc(String underWriteDesc) {
		this.underWriteDesc = underWriteDesc;
	}

	public Double getClaimAmount() {
		return claimAmount;
	}

	public void setClaimAmount(Double claimAmount) {
		this.claimAmount = claimAmount;
	}

	public List<UndwrtWriteCoverDataVo> getCoverDataList() {
		return coverDataList;
	}

	public void setCoverDataList(List<UndwrtWriteCoverDataVo> coverDataList) {
		this.coverDataList = coverDataList;
	}

	public List<UndwrtWriteRecoveryOrPayDataVo> getRecoveryOrPayDataList() {
		return recoveryOrPayDataList;
	}

	public void setRecoveryOrPayDataList(
			List<UndwrtWriteRecoveryOrPayDataVo> recoveryOrPayDataList) {
		this.recoveryOrPayDataList = recoveryOrPayDataList;
	}

	public String getUnderWriteDes() {
		return underWriteDes;
	}

	public void setUnderWriteDes(String underWriteDes) {
		this.underWriteDes = underWriteDes;
	}

	

}
