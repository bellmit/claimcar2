/******************************************************************************
 * CREATETIME : 2016年5月24日 上午10:15:03
 ******************************************************************************/
package ins.sino.claimcar.platform.vo;

import ins.sino.claimcar.carplatform.adapter.DateXmlAdapter;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * 理算核赔登记(商业) 
 * 理算信息
 * @author ★XMSH
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class BiVClaimAdjustmentDataVo {

	@XmlElement(name = "AdjustmentCode", required = true)
	private String adjustmentCode;// 理算编码
	
	@XmlElement(name = "UnderWriteDesc")
	private String underWriteDesc;// 核赔意见

	@XmlElement(name = "ClaimAmount", required = true)
	private Double claimAmount;// 赔偿金额(含施救费)

	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	@XmlElement(name = "UnderWriteEndTime", required = true)
	private Date underWriteEndTime;// 理算核赔通过时间；精确到分

	@XmlElement(name = "ClaimCoverData")
	private List<BiVClaimCoverDataVo> claimCoverDataList;// 损失赔偿情况列表

	@XmlElement(name = "RecoveryOrPayData")
	private List<BiVClaimRecoveryOrPayDataVo> recoveryOrPayDataList;// 追偿/清付列表

	/**
	 * @return 返回 adjustmentCode 理算编码
	 */
	public String getAdjustmentCode() {
		return adjustmentCode;
	}

	/**
	 * @param adjustmentCode 要设置的 理算编码
	 */
	public void setAdjustmentCode(String adjustmentCode) {
		this.adjustmentCode = adjustmentCode;
	}



	/**
	 * @return 返回 underWriteDesc 核赔意见
	 */
	public String getUnderWriteDesc() {
		return underWriteDesc;
	}

	/**
	 * @param underWriteDesc 要设置的 核赔意见
	 */
	public void setUnderWriteDesc(String underWriteDesc) {
		this.underWriteDesc = underWriteDesc;
	}

	/**
	 * @return 返回 claimAmount 赔偿金额(含施救费)
	 */
	public Double getClaimAmount() {
		return claimAmount;
	}

	/**
	 * @param claimAmount 要设置的 赔偿金额(含施救费)
	 */
	public void setClaimAmount(Double claimAmount) {
		this.claimAmount = claimAmount;
	}

	/**
	 * @return 返回 underWriteEndTime 理算核赔通过时间；精确到分
	 */
	public Date getUnderWriteEndTime() {
		return underWriteEndTime;
	}

	/**
	 * @param underWriteEndTime 要设置的 理算核赔通过时间；精确到分
	 */
	public void setUnderWriteEndTime(Date underWriteEndTime) {
		this.underWriteEndTime = underWriteEndTime;
	}

	/**
	 * @return 返回 claimCoverDataList。
	 */
	public List<BiVClaimCoverDataVo> getClaimCoverDataList() {
		return claimCoverDataList;
	}

	/**
	 * @param claimCoverDataList 要设置的 claimCoverDataList。
	 */
	public void setClaimCoverDataList(List<BiVClaimCoverDataVo> claimCoverDataList) {
		this.claimCoverDataList = claimCoverDataList;
	}

	/**
	 * @return 返回 recoveryOrPayDataList。
	 */
	public List<BiVClaimRecoveryOrPayDataVo> getRecoveryOrPayDataList() {
		return recoveryOrPayDataList;
	}

	/**
	 * @param recoveryOrPayDataList 要设置的 recoveryOrPayDataList。
	 */
	public void setRecoveryOrPayDataList(List<BiVClaimRecoveryOrPayDataVo> recoveryOrPayDataList) {
		this.recoveryOrPayDataList = recoveryOrPayDataList;
	}

}
