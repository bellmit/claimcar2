/******************************************************************************
 * CREATETIME : 2016年5月30日 下午1:25:42
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

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
public class SHCIClaimResSubrogationDataVo {

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
	private List<SHCIClaimResRecoveryDataVo> response_RecoveryDataVo;

	/**
	 * 被代位求偿信息列表（多条）BERECOVERY_LIST属于代位求偿码信息
	 */
	@XmlElementWrapper(name = "BERECOVERY_LIST")
	@XmlElement(name = "BERECOVERY_DATA")
	private List<SHCIClaimResBerecoveryDataVo> response_BerecoveryDataVo;
	/**
	 * 争议信息列表（多条）属于代位求偿码信息
	 */
	@XmlElementWrapper(name = "REASON_LIST")
	@XmlElement(name = "REASON_DATA")
	private List<SHCIClaimResReasonDataVo> response_ReasonDataVo;

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

	public List<SHCIClaimResRecoveryDataVo> getResponse_RecoveryDataVo() {
		return response_RecoveryDataVo;
	}

	public void setResponse_RecoveryDataVo(List<SHCIClaimResRecoveryDataVo> response_RecoveryDataVo) {
		this.response_RecoveryDataVo = response_RecoveryDataVo;
	}

	public List<SHCIClaimResBerecoveryDataVo> getResponse_BerecoveryDataVo() {
		return response_BerecoveryDataVo;
	}

	public void setResponse_BerecoveryDataVo(List<SHCIClaimResBerecoveryDataVo> response_BerecoveryDataVo) {
		this.response_BerecoveryDataVo = response_BerecoveryDataVo;
	}

	public List<SHCIClaimResReasonDataVo> getResponse_ReasonDataVo() {
		return response_ReasonDataVo;
	}

	public void setResponse_ReasonDataVo(List<SHCIClaimResReasonDataVo> response_ReasonDataVo) {
		this.response_ReasonDataVo = response_ReasonDataVo;
	}

}
