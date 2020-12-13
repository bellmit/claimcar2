/******************************************************************************
 * CREATETIME : 2016年5月24日 下午5:18:09
 ******************************************************************************/
package ins.sino.claimcar.platform.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author ★XMSH
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Body")
public class BiEndCaseReqBodyVo {

	@XmlElement(name = "BasePart")
	private BiEndCaseBasePartVo basePart;

	@XmlElement(name = "ClaimCoverData")
	private List<BiEndCaseClaimCoverDataVo> claimCoverDataList;

	@XmlElement(name = "RecoveryOrPayData")
	private List<BiEndCaseRecoveryOrPayDataVo> recoveryOrPayDataList;
	
	@XmlElement(name = "FraudTypeData")
	private List<BiEndCaseFraudTypeDataVo> FraudTypeData;

	/**
	 * @return 返回 basePart。
	 */
	public BiEndCaseBasePartVo getBasePart() {
		return basePart;
	}

	/**
	 * @param basePart 要设置的 basePart。
	 */
	public void setBasePart(BiEndCaseBasePartVo basePart) {
		this.basePart = basePart;
	}

	/**
	 * @return 返回 claimCoverDataList。
	 */
	public List<BiEndCaseClaimCoverDataVo> getClaimCoverDataList() {
		return claimCoverDataList;
	}

	/**
	 * @param claimCoverDataList 要设置的 claimCoverDataList。
	 */
	public void setClaimCoverDataList(List<BiEndCaseClaimCoverDataVo> claimCoverDataList) {
		this.claimCoverDataList = claimCoverDataList;
	}

	/**
	 * @return 返回 recoveryOrPayDataList。
	 */
	public List<BiEndCaseRecoveryOrPayDataVo> getRecoveryOrPayDataList() {
		return recoveryOrPayDataList;
	}

	/**
	 * @param recoveryOrPayDataList 要设置的 recoveryOrPayDataList。
	 */
	public void setRecoveryOrPayDataList(List<BiEndCaseRecoveryOrPayDataVo> recoveryOrPayDataList) {
		this.recoveryOrPayDataList = recoveryOrPayDataList;
	}

	public List<BiEndCaseFraudTypeDataVo> getFraudTypeData() {
		return FraudTypeData;
	}

	public void setFraudTypeData(List<BiEndCaseFraudTypeDataVo> fraudTypeData) {
		FraudTypeData = fraudTypeData;
	}

}
