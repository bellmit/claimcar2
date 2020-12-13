/******************************************************************************
 * CREATETIME : 2016年5月26日 下午6:00:24
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
public class SHCILossResSubrogationDataVo {

	/**
	 * 代位求偿码,平台生成的唯一标志，用于保险公司发起代位求偿清算请求
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
	private List<SHCILossResRecoveryDataVo> recoveryDataVo;// 代位求偿信息列表（多条）
	
	/**
	 * 被代位求偿信息列表（多条）BERECOVERY_LIST属于代位求偿码信息
	 */
	@XmlElementWrapper(name = "BERECOVERY_LIST")
	@XmlElement(name = "BERECOVERY_DATA")
	private List<SHCILossResBerecoveryDataVo> berecoveryDataVo;// 被代位求偿信息列表（多条）
	
	
	/**
	 * 争议信息列表（多条）属于代位求偿码信息
	 */
	@XmlElementWrapper(name = "REASON_LIST")
	@XmlElement(name = "REASON_DATA")
	private List<ShCILossResReasonDataVo> reasonDataVo;// 争议信息列表（多条）
	
	
	
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

}
