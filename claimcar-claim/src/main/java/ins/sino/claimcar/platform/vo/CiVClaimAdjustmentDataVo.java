/******************************************************************************
 * CREATETIME : 2016年5月23日 下午5:20:15
 ******************************************************************************/
package ins.sino.claimcar.platform.vo;

import ins.sino.claimcar.carplatform.adapter.DateXmlAdapter;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * @author ★XMSH
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CiVClaimAdjustmentDataVo {

	@XmlElement(name = "ADJUSTMENT_CODE", required = true)
	private String adjustmentCode;// 理算编码

	/*@XmlElement(name = "OTHER_FEE")
	private Double otherFee;// 其他费用
*/
	@XmlElement(name = "UNDER_WRITE_DES")
	private String underWriteDes;// 核赔意见

	@XmlElement(name = "CLAIM_AMOUNT")
	private Double claimAmount;// 赔偿金额

	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	@XmlElement(name = "UNDER_WRITE_END_TIME", required = true)
	private Date underWriteEndTime;// 理算核赔通过时间 格式:YYYYMMDDHHMM

	@XmlElementWrapper(name = "CLAIM_COVER_LIST")
	@XmlElement(name = "CLAIM_COVER_DATA")
	private List<CiVClaimCoverDataVo> coverDataList;

	@XmlElementWrapper(name = "RECOVERY_OR_PAY_LIST")
	@XmlElement(name = "RECOVERY_OR_PAY_DATA")
	private List<CiVClaimRecoveryOrPayDataVo> recoveryOrPayDataList;

	public List<CiVClaimCoverDataVo> getCoverDataList() {
		return coverDataList;
	}

	public void setCoverDataList(List<CiVClaimCoverDataVo> coverDataList) {
		this.coverDataList = coverDataList;
	}

	public List<CiVClaimRecoveryOrPayDataVo> getRecoveryOrPayDataList() {
		return recoveryOrPayDataList;
	}

	public void setRecoveryOrPayDataList(
			List<CiVClaimRecoveryOrPayDataVo> recoveryOrPayDataList) {
		this.recoveryOrPayDataList = recoveryOrPayDataList;
	}

	/**
	 * @return 返回 adjustmentCode 理算编码
	 */
	public String getAdjustmentCode() {
		return adjustmentCode;
	}

	/**
	 * @param adjustmentCode
	 *            要设置的 理算编码
	 */
	public void setAdjustmentCode(String adjustmentCode) {
		this.adjustmentCode = adjustmentCode;
	}

	/**
	 * @return 返回 otherFee 其他费用
	 */
/*	public Double getOtherFee() {
		return otherFee;
	}
*/
	/**
	 * @param otherFee
	 *            要设置的 其他费用
	 */
/*	public void setOtherFee(Double otherFee) {
		this.otherFee = otherFee;
	}*/

	/**
	 * @return 返回 underWriteDes 核赔意见
	 */
	public String getUnderWriteDes() {
		return underWriteDes;
	}

	/**
	 * @param underWriteDes
	 *            要设置的 核赔意见
	 */
	public void setUnderWriteDes(String underWriteDes) {
		this.underWriteDes = underWriteDes;
	}

	/**
	 * @return 返回 claimAmount 赔偿金额
	 */
	public Double getClaimAmount() {
		return claimAmount;
	}

	/**
	 * @param claimAmount
	 *            要设置的 赔偿金额
	 */
	public void setClaimAmount(Double claimAmount) {
		this.claimAmount = claimAmount;
	}

	/**
	 * @return 返回 underWriteEndTime 理算核赔通过时间 格式:YYYYMMDDHHMM
	 */
	public Date getUnderWriteEndTime() {
		return underWriteEndTime;
	}

	/**
	 * @param underWriteEndTime
	 *            要设置的 理算核赔通过时间 格式:YYYYMMDDHHMM
	 */
	public void setUnderWriteEndTime(Date underWriteEndTime) {
		this.underWriteEndTime = underWriteEndTime;
	}

}
