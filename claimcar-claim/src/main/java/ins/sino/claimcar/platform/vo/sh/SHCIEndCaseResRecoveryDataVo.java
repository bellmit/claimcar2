/******************************************************************************
 * CREATETIME : 2016年6月6日 下午7:00:44
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author ★XMSH
 */
public class SHCIEndCaseResRecoveryDataVo {

	@XmlElement(name = "RECOVERY_REPORT_NO")
	private String recoveryReportNo;// 追偿保险公司案件

	@XmlElement(name = "STATUS", required = true)
	private String status;// 追偿保险公司案件状态

	/**
	 * @return 返回 recoveryReportNo 追偿保险公司案件
	 */
	public String getRecoveryReportNo() {
		return recoveryReportNo;
	}

	/**
	 * @param recoveryReportNo 要设置的 追偿保险公司案件
	 */
	public void setRecoveryReportNo(String recoveryReportNo) {
		this.recoveryReportNo = recoveryReportNo;
	}

	/**
	 * @return 返回 status 追偿保险公司案件状态
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status 要设置的 追偿保险公司案件状态
	 */
	public void setStatus(String status) {
		this.status = status;
	}

}
