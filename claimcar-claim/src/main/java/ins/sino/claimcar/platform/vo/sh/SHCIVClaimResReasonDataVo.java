/******************************************************************************
 * CREATETIME : 2016年5月31日 下午7:42:03
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
public class SHCIVClaimResReasonDataVo {

	@XmlElement(name = "COMPANY_ID", required = true)
	private String companyId;// 保险公司代码

	@XmlElement(name = "REASON")
	private String reason;// 争议理由

	@XmlElement(name = "DISPUTE_TIME")
	private String disputeTime;// 争议时间

	/**
	 * @return 返回 companyId 保险公司代码
	 */
	public String getCompanyId() {
		return companyId;
	}

	/**
	 * @param companyId 要设置的 保险公司代码
	 */
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
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

	/**
	 * @return 返回 disputeTime 争议时间
	 */
	public String getDisputeTime() {
		return disputeTime;
	}

	/**
	 * @param disputeTime 要设置的 争议时间
	 */
	public void setDisputeTime(String disputeTime) {
		this.disputeTime = disputeTime;
	}

}
