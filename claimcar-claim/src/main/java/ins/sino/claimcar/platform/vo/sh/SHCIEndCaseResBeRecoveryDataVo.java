/******************************************************************************
 * CREATETIME : 2016年6月6日 下午7:04:09
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author ★XMSH
 */
public class SHCIEndCaseResBeRecoveryDataVo {

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
