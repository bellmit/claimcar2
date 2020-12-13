/******************************************************************************
 * CREATETIME : 2016年6月6日 下午8:16:18
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author ★XMSH
 */
public class SHBIEndCaseResBeRecoveryDataVo {

	@XmlElement(name = "BERECOVERY_REPORT_NO")
	private String berecoveryReportNo;// 被追偿保险公司报案号

	@XmlElement(name = "STATUS", required = true)
	private String status;// 被追偿保险公司案件状态

	/**
	 * @return 返回 berecoveryReportNo 被追偿保险公司报案号
	 */
	public String getBerecoveryReportNo() {
		return berecoveryReportNo;
	}

	/**
	 * @param berecoveryReportNo 要设置的 被追偿保险公司报案号
	 */
	public void setBerecoveryReportNo(String berecoveryReportNo) {
		this.berecoveryReportNo = berecoveryReportNo;
	}

	/**
	 * @return 返回 status 被追偿保险公司案件状态
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status 要设置的 被追偿保险公司案件状态
	 */
	public void setStatus(String status) {
		this.status = status;
	}

}
