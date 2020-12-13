/******************************************************************************
* CREATETIME : 2016年3月16日 下午2:53:04
******************************************************************************/
package ins.sino.claimcar.subrogation.platform.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

//理算信息列表(隶属于理算核赔信息)
/**
 * @author ★YangKun
 * @CreateTime 2016年3月16日
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CiUndwrtWriteAdjustmentDataVo {
	
	/** 理算编码 **/ 
	@XmlElement(name="ADJUSTMENT_CODE", required = true)
	private String adjustmentCode; 

	/** 其他费用 **/ 
	@XmlElement(name="OTHER_FEE")
	private Double otherFee; 

	/** 核赔意见 **/ 
	@XmlElement(name="UNDER_WRITE_DES")
	private String underWriteDes; 

	/** 赔偿金额 **/ 
	@XmlElement(name="CLAIM_AMOUNT")
	private Double claimAmount;
	
	/**	损失赔偿情况信息列表 */
	@XmlElementWrapper(name = "CLAIM_COVER_LIST")
	@XmlElement(name = "CLAIM_COVER_DATA")
	private List<CiUndwrtWriteCoverDataVo> coverDataList;
	
	/**	追偿/清付信息列表  */
	@XmlElementWrapper(name = "RECOVERY_OR_PAY_LIST")
	@XmlElement(name = "RECOVERY_OR_PAY_DATA")
	private List<CiUndwrtWriteRecoveryOrPayDataVo> recoveryOrPayDataList;
	
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

	public String getUnderWriteDes() {
		return underWriteDes;
	}

	public void setUnderWriteDes(String underWriteDes) {
		this.underWriteDes = underWriteDes;
	}

	public Double getClaimAmount() {
		return claimAmount;
	}

	public void setClaimAmount(Double claimAmount) {
		this.claimAmount = claimAmount;
	}

	public List<CiUndwrtWriteCoverDataVo> getCoverDataList() {
		return coverDataList;
	}

	public void setCoverDataList(List<CiUndwrtWriteCoverDataVo> coverDataList) {
		this.coverDataList = coverDataList;
	}

	public List<CiUndwrtWriteRecoveryOrPayDataVo> getRecoveryOrPayDataList() {
		return recoveryOrPayDataList;
	}

	public void setRecoveryOrPayDataList(
			List<CiUndwrtWriteRecoveryOrPayDataVo> recoveryOrPayDataList) {
		this.recoveryOrPayDataList = recoveryOrPayDataList;
	}

	
}
