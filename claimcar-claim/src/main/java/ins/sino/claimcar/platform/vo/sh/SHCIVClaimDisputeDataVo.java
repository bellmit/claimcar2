/******************************************************************************
 * CREATETIME : 2016年5月31日 下午7:25:13
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * 争议信息
 * @author ★XMSH
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SHCIVClaimDisputeDataVo {

	@XmlElement(name = "SUBROGATION_NO")
	private String subrogationNo;// 代位求偿码

	@XmlElement(name = "DISPUTE_FLAG", required = true)
	private String disputeFlag;// 是否争议标志

	@XmlElement(name = "REASON")
	private String reason;// 争议理由

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
	 * @return 返回 disputeFlag 是否争议标志
	 */
	public String getDisputeFlag() {
		return disputeFlag;
	}

	/**
	 * @param disputeFlag 要设置的 是否争议标志
	 */
	public void setDisputeFlag(String disputeFlag) {
		this.disputeFlag = disputeFlag;
	}

	/**
	 * @return 返回 reason 争议理由
	 */
	public String getReason() {
		return reason;
	}

	/**
	 * @param reason 要设置的 争议理由
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}

}
