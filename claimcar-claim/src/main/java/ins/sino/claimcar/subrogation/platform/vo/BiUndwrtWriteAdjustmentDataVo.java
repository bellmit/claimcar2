/******************************************************************************
* CREATETIME : 2016年3月16日 下午2:53:04
******************************************************************************/
package ins.sino.claimcar.subrogation.platform.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

//理算信息列表(隶属于理算核赔信息)
/**
 * @author ★YangKun
 * @CreateTime 2016年3月16日
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class BiUndwrtWriteAdjustmentDataVo {
	
	@XmlElement(name = "AdjustmentCode", required = true)
	private String adjustmentCode;//理算编码

	@XmlElement(name = "OtherFee")
	private Double otherFee;//其他费用

	@XmlElement(name = "UnderWriteDesc")
	private String underWriteDesc;//核赔意见

	@XmlElement(name = "ClaimAmount")
	private Double claimAmount;//赔偿金额

	/**	损失赔偿情况信息列表 */
	@XmlElement(name = "ClaimCoverData")
	private List<BiUndwrtWriteCoverDataVo> coverDataList;
	
	/**	追偿/清付信息列表  */
	@XmlElement(name = "RecoveryOrPayData")
	private List<BiUndwrtWriteRecoveryOrPayDataVo> recoveryOrPayDataList;
	/** 
	 * @return 返回 adjustmentCode  理算编码
	 */ 
	public String getAdjustmentCode(){ 
	    return adjustmentCode;
	}

	/** 
	 * @param adjustmentCode 要设置的 理算编码
	 */ 
	public void setAdjustmentCode(String adjustmentCode){ 
	    this.adjustmentCode=adjustmentCode;
	}

	/** 
	 * @return 返回 otherFee  其他费用
	 */ 
	public Double getOtherFee(){ 
	    return otherFee;
	}

	/** 
	 * @param otherFee 要设置的 其他费用
	 */ 
	public void setOtherFee(Double otherFee){ 
	    this.otherFee=otherFee;
	}

	/** 
	 * @return 返回 underWriteDesc  核赔意见
	 */ 
	public String getUnderWriteDesc(){ 
	    return underWriteDesc;
	}

	/** 
	 * @param underWriteDesc 要设置的 核赔意见
	 */ 
	public void setUnderWriteDesc(String underWriteDesc){ 
	    this.underWriteDesc=underWriteDesc;
	}

	/** 
	 * @return 返回 claimAmount  赔偿金额
	 */ 
	public Double getClaimAmount(){ 
	    return claimAmount;
	}

	/** 
	 * @param claimAmount 要设置的 赔偿金额
	 */ 
	public void setClaimAmount(Double claimAmount){ 
	    this.claimAmount=claimAmount;
	}

	public List<BiUndwrtWriteCoverDataVo> getCoverDataList() {
		return coverDataList;
	}

	public void setCoverDataList(List<BiUndwrtWriteCoverDataVo> coverDataList) {
		this.coverDataList = coverDataList;
	}

	public List<BiUndwrtWriteRecoveryOrPayDataVo> getRecoveryOrPayDataList() {
		return recoveryOrPayDataList;
	}

	public void setRecoveryOrPayDataList(
			List<BiUndwrtWriteRecoveryOrPayDataVo> recoveryOrPayDataList) {
		this.recoveryOrPayDataList = recoveryOrPayDataList;
	}



}
