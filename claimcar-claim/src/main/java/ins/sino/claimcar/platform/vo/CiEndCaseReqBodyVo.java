/******************************************************************************
 * CREATETIME : 2016年5月24日 上午11:39:03
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
public class CiEndCaseReqBodyVo {

	@XmlElement(name = "BASE_PART")
	private CiEndCaseBasePartVo basePart;

	@XmlElementWrapper(name = "CLAIM_COVER_LIST")
	@XmlElement(name = "CLAIM_COVER_DATA")
	private List<CiEndCaseClaimCoverDataVo> claimCoverDataList;

	@XmlElementWrapper(name = "RECOVERY_OR_PAY_LIST")
	@XmlElement(name = "RECOVERY_OR_PAY_DATA")
	private List<CiEndCaseRecoveryOrPayDataVo> recoveryOrPayDataList;

	@XmlElementWrapper(name = "FRAUD_TYPE_LIST")
	@XmlElement(name = "FRAUD_TYPE_DATA")
	private List<CiEndCaseFraudTypeDataVo> fraudTypeDataList;

	/**
	 * @return 返回 basePart。
	 */
	public CiEndCaseBasePartVo getBasePart() {
		return basePart;
	}

	/**
	 * @param basePart 要设置的 basePart。
	 */
	public void setBasePart(CiEndCaseBasePartVo basePart) {
		this.basePart = basePart;
	}

	/**
	 * @return 返回 claimCoverDataList。
	 */
	public List<CiEndCaseClaimCoverDataVo> getClaimCoverDataList() {
		return claimCoverDataList;
	}

	/**
	 * @param claimCoverDataList 要设置的 claimCoverDataList。
	 */
	public void setClaimCoverDataList(List<CiEndCaseClaimCoverDataVo> claimCoverDataList) {
		this.claimCoverDataList = claimCoverDataList;
	}

	/**
	 * @return 返回 recoveryOrPayDataList。
	 */
	public List<CiEndCaseRecoveryOrPayDataVo> getRecoveryOrPayDataList() {
		return recoveryOrPayDataList;
	}

	/**
	 * @param recoveryOrPayDataList 要设置的 recoveryOrPayDataList。
	 */
	public void setRecoveryOrPayDataList(List<CiEndCaseRecoveryOrPayDataVo> recoveryOrPayDataList) {
		this.recoveryOrPayDataList = recoveryOrPayDataList;
	}

	/**
	 * @return 返回 fraudTypeDataList。
	 */
	public List<CiEndCaseFraudTypeDataVo> getFraudTypeDataList() {
		return fraudTypeDataList;
	}

	/**
	 * @param fraudTypeDataList 要设置的 fraudTypeDataList。
	 */
	public void setFraudTypeDataList(List<CiEndCaseFraudTypeDataVo> fraudTypeDataList) {
		this.fraudTypeDataList = fraudTypeDataList;
	}

}
