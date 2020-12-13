/******************************************************************************
 * CREATETIME : 2016年5月23日 下午3:10:49
 ******************************************************************************/
package ins.sino.claimcar.platform.vo;

import ins.sino.claimcar.carplatform.adapter.DateXmlAdapter;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * @author ★XMSH
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CiCertifyBasePartVo {

	@XmlElement(name = "CONFIRM_SEQUENCE_NO", required = true)
	private String confirmSequenceNo;// 投保确认号

	@XmlElement(name = "CLAIM_CODE", required = true)
	private String claimCode;// 理赔编号

	@XmlElement(name = "REPORT_NO", required = true)
	private String reportNo;// 报案号

	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	@XmlElement(name = "DOC_END_TIME", required = true)
	private Date docEndTime;// 单证首次收集齐全时间 格式:YYYYMMDDHHMM

	@XmlElement(name = "PAY_SELF_FLAG", required = true)
	private String paySelfFlag;// 互碰自赔标志；参见代码

	/**
	 * @return 返回 confirmSequenceNo 投保确认号
	 */
	public String getConfirmSequenceNo() {
		return confirmSequenceNo;
	}

	/**
	 * @param confirmSequenceNo 要设置的 投保确认号
	 */
	public void setConfirmSequenceNo(String confirmSequenceNo) {
		this.confirmSequenceNo = confirmSequenceNo;
	}

	/**
	 * @return 返回 claimCode 理赔编号
	 */
	public String getClaimCode() {
		return claimCode;
	}

	/**
	 * @param claimCode 要设置的 理赔编号
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
	 * @return 返回 docEndTime 单证首次收集齐全时间 格式:YYYYMMDDHHMM
	 */
	public Date getDocEndTime() {
		return docEndTime;
	}

	/**
	 * @param docEndTime 要设置的 单证首次收集齐全时间 格式:YYYYMMDDHHMM
	 */
	public void setDocEndTime(Date docEndTime) {
		this.docEndTime = docEndTime;
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
