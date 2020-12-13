/******************************************************************************
 * CREATETIME : 2016年5月24日 下午5:23:29
 ******************************************************************************/
package ins.sino.claimcar.regist.platform.vo.sh;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

/**
 * 代位求偿码信息（多条）
 * @author ★Luwei
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SHCIRegistResSubrogationDataVo {

	/**
	 * 代位求偿码
	 */
	@XmlElement(name = "SUBROGATION_NO")
	private String subrogationNo;// 代位求偿码

	/**
	 * 是否代位求偿标志
	 */
	@XmlElement(name = "SUBROGATION_FLAG")
	private String subrogationFlag;// 是否代位求偿标志

	/**
	 * 是否代位求偿争议标志
	 */
	@XmlElement(name = "DISPUTE_FLAG")
	private String disputeFlag;// 是否代位求偿争议标志

	/**
	 * 代位求偿信息列表（多条）RECOVERY_LIST属于代位求偿码信息
	 */
	@XmlElementWrapper(name = "RECOVERY_LIST")
	@XmlElement(name = "RECOVERY_DATA")
	private List<SHCIRegistResRecoveryDataVo> recoveryDataVo;

	/**
	 * 被代位求偿信息列表（多条）
	 */
	@XmlElementWrapper(name = "BERECOVERY_LIST")
	@XmlElement(name = "BERECOVERY_DATA")
	private List<SHCIRegistResBerecoveryDataVo> berecoveryDataVo;

	/**
	 * 争议信息列表（多条）属于代位求偿码信息,BERECOVERY_LIST属于代位求偿码信息,
	 */
	@XmlElementWrapper(name = "REASON_LIST")
	@XmlElement(name = "REASON_DATA")
	private List<SHCIRegistResReasonDataVo> reasonDataVo;

	public String getSubrogationNo() {
		return subrogationNo;
	}

	public void setSubrogationNo(String subrogationNo) {
		this.subrogationNo = subrogationNo;
	}

	public String getSubrogationFlag() {
		return subrogationFlag;
	}

	public void setSubrogationFlag(String subrogationFlag) {
		this.subrogationFlag = subrogationFlag;
	}

	public String getDisputeFlag() {
		return disputeFlag;
	}

	public void setDisputeFlag(String disputeFlag) {
		this.disputeFlag = disputeFlag;
	}

	public List<SHCIRegistResRecoveryDataVo> getRecoveryDataVo() {
		return recoveryDataVo;
	}

	public void setRecoveryDataVo(List<SHCIRegistResRecoveryDataVo> recoveryDataVo) {
		this.recoveryDataVo = recoveryDataVo;
	}

	public List<SHCIRegistResBerecoveryDataVo> getBerecoveryDataVo() {
		return berecoveryDataVo;
	}

	public void setBerecoveryDataVo(List<SHCIRegistResBerecoveryDataVo> berecoveryDataVo) {
		this.berecoveryDataVo = berecoveryDataVo;
	}

	public List<SHCIRegistResReasonDataVo> getReasonDataVo() {
		return reasonDataVo;
	}

	public void setReasonDataVo(List<SHCIRegistResReasonDataVo> reasonDataVo) {
		this.reasonDataVo = reasonDataVo;
	}

}
