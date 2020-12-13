/******************************************************************************
 * CREATETIME : 2016年5月23日 下午5:17:40
 ******************************************************************************/
package ins.sino.claimcar.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author ★XMSH
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CiVClaimBasePartVo {

	@XmlElement(name = "CONFIRM_SEQUENCE_NO", required = true)
	private String confirmSequenceNo;// 投保确认码

	@XmlElement(name = "CLAIM_CODE", required = true)
	private String claimCode;// 理赔编码

	@XmlElement(name = "REPORT_NO", required = true)
	private String reportNo;// 报案号

	@XmlElement(name = "PAY_SELF_FLAG", required = true)
	private String paySelfFlag;// 互碰自赔标志；参见代码
	
	@XmlElement(name = "ISINVOLVING", required = true)
	private String isInVoving; //是否涉诉

	public String getIsInVoving() {
		return isInVoving;
	}

	public void setIsInVoving(String isInVoving) {
		this.isInVoving = isInVoving;
	}

	/**
	 * @return 返回 confirmSequenceNo 投保确认码
	 */
	public String getConfirmSequenceNo() {
		return confirmSequenceNo;
	}

	/**
	 * @param confirmSequenceNo 要设置的 投保确认码
	 */
	public void setConfirmSequenceNo(String confirmSequenceNo) {
		this.confirmSequenceNo = confirmSequenceNo;
	}

	/**
	 * @return 返回 claimCode 理赔编码
	 */
	public String getClaimCode() {
		return claimCode;
	}

	/**
	 * @param claimCode 要设置的 理赔编码
	 */
	public void setClaimCode(String claimCode) {
		this.claimCode = claimCode;
	}

	/**
	 * @return 返回 reportNo 报案号
	 */
	public String getReportNo() {
		return reportNo;
	}

	/**
	 * @param reportNo 要设置的 报案号
	 */
	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}

	/**
	 * @return 返回 paySelfFlag 互碰自赔标志；参见代码
	 */
	public String getPaySelfFlag() {
		return paySelfFlag;
	}

	/**
	 * @param paySelfFlag 要设置的 互碰自赔标志；参见代码
	 */
	public void setPaySelfFlag(String paySelfFlag) {
		this.paySelfFlag = paySelfFlag;
	}

}
