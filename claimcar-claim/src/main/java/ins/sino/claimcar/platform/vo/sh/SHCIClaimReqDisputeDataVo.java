/******************************************************************************
 * CREATETIME : 2016年5月30日 下午12:51:31
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * 争议信息列表（多条）为空
 * @author ★Luwei
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SHCIClaimReqDisputeDataVo {

	/**
	 * 代位求偿码,需要发起争议的代位求偿码
	 */
	@XmlElement(name = "SUBROGATION_NO")
	private String subrogationNo;// 代位求偿码

	/**
	 * 是否争议标志
	 */
	@XmlElement(name = "DISPUTE_FLAG")
	private String disputeFlag;// 是否争议标志

	/**
	 * 争议理由
	 */
	@XmlElement(name = "REASON")
	private String reason;// 争议理由

	public String getSubrogationNo() {
		return subrogationNo;
	}

	public void setSubrogationNo(String subrogationNo) {
		this.subrogationNo = subrogationNo;
	}

	public String getDisputeFlag() {
		return disputeFlag;
	}

	public void setDisputeFlag(String disputeFlag) {
		this.disputeFlag = disputeFlag;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
}
