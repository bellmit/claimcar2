/******************************************************************************
 * CREATETIME : 2016年5月23日 上午9:48:18
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
public class CiClaimBasePartVo {

	@XmlElement(name = "CONFIRM_SEQUENCE_NO", required = true)
	private String confirmSequenceNo;// 投保确认号

	@XmlElement(name = "CLAIM_CODE", required = true)
	private String claimCode;// 理赔编号

	@XmlElement(name = "ESTIMATE_AMOUNT", required = true)
	private String estimateAmount;// 估损金额

	@XmlElement(name = "REGISTRATION_NO", required = true)
	private String registrationNo;// 立案号

	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	@XmlElement(name = "REGISTRATION_TIME", required = true)
	private Date registrationTime;// 立案时间；格式YYYYMMDDHHMM

	@XmlElement(name = "CLAIM_TYPE")
	private String claimType;// 理赔类型代码；参见代码

	@XmlElement(name = "PAY_SELF_FLAG", required = true)
	private String paySelfFlag;// 互碰自赔标志；参见代码

	@XmlElement(name = "REPORT_NO", required = true)
	private String reportNo;// 报案号

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
	 * @return 返回 estimateAmount 估损金额
	 */
	public String getEstimateAmount() {
		return estimateAmount;
	}

	/**
	 * @param estimateAmount 要设置的 估损金额
	 */
	public void setEstimateAmount(String estimateAmount) {
		this.estimateAmount = estimateAmount;
	}

	/**
	 * @return 返回 registrationNo 立案号
	 */
	public String getRegistrationNo() {
		return registrationNo;
	}

	/**
	 * @param registrationNo 要设置的 立案号
	 */
	public void setRegistrationNo(String registrationNo) {
		this.registrationNo = registrationNo;
	}

	/**
	 * @return 返回 registrationTime 立案时间；格式YYYYMMDDHHMM
	 */
	public Date getRegistrationTime() {
		return registrationTime;
	}

	/**
	 * @param registrationTime 要设置的 立案时间；格式YYYYMMDDHHMM
	 */
	public void setRegistrationTime(Date registrationTime) {
		this.registrationTime = registrationTime;
	}

	/**
	 * @return 返回 claimType 理赔类型代码；参见代码
	 */
	public String getClaimType() {
		return claimType;
	}

	/**
	 * @param claimType 要设置的 理赔类型代码；参见代码
	 */
	public void setClaimType(String claimType) {
		this.claimType = claimType;
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
}
