/******************************************************************************
 * CREATETIME : 2016年6月2日 上午9:51:13
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

/**
 * @author ★XMSH
 */
public class SHBIVClaimResSubrogationDataVo {

	@XmlElement(name = "SUBROGATION_NO")
	private String subrogationNo;// 代位求偿码

	@XmlElement(name = "SUBROGATION_FLAG", required = true)
	private String subrogationFlag;// 是否代位求偿标志

	@XmlElement(name = "DISPUTE_FLAG", required = true)
	private String disputeFlag;// 是否代位求偿争议标志

	@XmlElementWrapper(name = "RECOVERY_LIST")
	@XmlElement(name = "RECOVERY_DATA")
	private List<SHBIVClaimResRecoveryDataVo> recoveryList;

	@XmlElementWrapper(name = "BERECOVERY_LIST")
	@XmlElement(name = "BERECOVERY_DATA")
	private List<SHBIVClaimResBerecoveryDataVo> beRecoveryList;

	@XmlElementWrapper(name = "REASON_LIST")
	@XmlElement(name = "REASON_DATA")
	private List<SHBIVClaimResReasonDataVo> reasonList;

	/**
	 * @return 返回 subrogationNo 代位求偿码
	 */
	public String getSubrogationNo() {
		return subrogationNo;
	}

	/**
	 * @param subrogationNo 要设置的 代位求偿码
	 */
	public void setSubrogationNo(String subrogationNo) {
		this.subrogationNo = subrogationNo;
	}

	/**
	 * @return 返回 subrogationFlag 是否代位求偿标志
	 */
	public String getSubrogationFlag() {
		return subrogationFlag;
	}

	/**
	 * @param subrogationFlag 要设置的 是否代位求偿标志
	 */
	public void setSubrogationFlag(String subrogationFlag) {
		this.subrogationFlag = subrogationFlag;
	}

	/**
	 * @return 返回 disputeFlag 是否代位求偿争议标志
	 */
	public String getDisputeFlag() {
		return disputeFlag;
	}

	/**
	 * @param disputeFlag 要设置的 是否代位求偿争议标志
	 */
	public void setDisputeFlag(String disputeFlag) {
		this.disputeFlag = disputeFlag;
	}

	/**
	 * @return 返回 recoveryList。
	 */
	public List<SHBIVClaimResRecoveryDataVo> getRecoveryList() {
		return recoveryList;
	}

	/**
	 * @param recoveryList 要设置的 recoveryList。
	 */
	public void setRecoveryList(List<SHBIVClaimResRecoveryDataVo> recoveryList) {
		this.recoveryList = recoveryList;
	}

	/**
	 * @return 返回 beRecoveryList。
	 */
	public List<SHBIVClaimResBerecoveryDataVo> getBeRecoveryList() {
		return beRecoveryList;
	}

	/**
	 * @param beRecoveryList 要设置的 beRecoveryList。
	 */
	public void setBeRecoveryList(List<SHBIVClaimResBerecoveryDataVo> beRecoveryList) {
		this.beRecoveryList = beRecoveryList;
	}

	/**
	 * @return 返回 reasonList。
	 */
	public List<SHBIVClaimResReasonDataVo> getReasonList() {
		return reasonList;
	}

	/**
	 * @param reasonList 要设置的 reasonList。
	 */
	public void setReasonList(List<SHBIVClaimResReasonDataVo> reasonList) {
		this.reasonList = reasonList;
	}

}
