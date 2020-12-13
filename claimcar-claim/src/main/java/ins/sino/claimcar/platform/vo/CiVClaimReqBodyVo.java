/******************************************************************************
 * CREATETIME : 2016年5月23日 下午5:10:03
 ******************************************************************************/
package ins.sino.claimcar.platform.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author ★XMSH
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "BODY")
public class CiVClaimReqBodyVo {

	@XmlElement(name = "BASE_PART")
	private CiVClaimBasePartVo basePart;

	@XmlElementWrapper(name = "ADJUSTMENT_LIST")
	@XmlElement(name = "ADJUSTMENT_DATA")
	private List<CiVClaimAdjustmentDataVo> adjustmentDataList;

	// @XmlElementWrapper(name = "CLAIM_COVER_LIST")
	// @XmlElement(name = "CLAIM_COVER_DATA")
	// private List<CiVClaimCoverDataVo> coverDataList;
	//
	// @XmlElementWrapper(name = "RECOVERY_OR_PAY_LIST")
	// @XmlElement(name = "RECOVERY_OR_PAY_DATA")
	// private List<CiVClaimRecoveryOrPayDataVo> recoveryOrPayDataList;

	/**
	 * @return 返回 basePart。
	 */
	public CiVClaimBasePartVo getBasePart() {
		return basePart;
	}

	/**
	 * @param basePart
	 *            要设置的 basePart。
	 */
	public void setBasePart(CiVClaimBasePartVo basePart) {
		this.basePart = basePart;
	}

	/**
	 * @return 返回 adjustmentDataList。
	 */
	public List<CiVClaimAdjustmentDataVo> getAdjustmentDataList() {
		return adjustmentDataList;
	}

	/**
	 * @param adjustmentDataList
	 *            要设置的 adjustmentDataList。
	 */
	public void setAdjustmentDataList(
			List<CiVClaimAdjustmentDataVo> adjustmentDataList) {
		this.adjustmentDataList = adjustmentDataList;
	}

}
