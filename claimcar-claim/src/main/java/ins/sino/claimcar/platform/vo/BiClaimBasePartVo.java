/******************************************************************************
 * CREATETIME : 2016年5月23日 上午11:34:21
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
public class BiClaimBasePartVo {

	@XmlElement(name = "ClaimSequenceNo", required = true)
	private String claimSequenceNo;// 理赔编号

	@XmlElement(name = "ConfirmSequenceNo", required = true)
	private String confirmSequenceNo;// 投保确认码

	@XmlElement(name = "ClaimNotificationNo", required = true)
	private String claimNotificationNo;// 报案号

	@XmlElement(name = "ClaimRegistrationNo", required = true)
	private String claimRegistrationNo;// 立案号

	@XmlElement(name = "ClaimType")
	private String claimType;// 理赔类型；参见代码

	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	@XmlElement(name = "ClaimRegistrationTime", required = true)
	private Date claimRegistrationTime;// 立案时间；格式：精确到分钟

	@XmlElement(name = "EstimatedLossAmount", required = true)
	private String estimatedLossAmount;// 估损金额

	@XmlElement(name = "SubrogationFlag", required = true)
	private String subrogationFlag;// 是否要求代位；参见代码

	/**
	 * @return 返回 claimSequenceNo 理赔编号
	 */
	public String getClaimSequenceNo() {
		return claimSequenceNo;
	}

	/**
	 * @param claimSequenceNo 要设置的 理赔编号
	 */
	public void setClaimSequenceNo(String claimSequenceNo) {
		this.claimSequenceNo = claimSequenceNo;
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
	 * @return 返回 claimNotificationNo 报案号
	 */
	public String getClaimNotificationNo() {
		return claimNotificationNo;
	}

	/**
	 * @param claimNotificationNo 要设置的 报案号
	 */
	public void setClaimNotificationNo(String claimNotificationNo) {
		this.claimNotificationNo = claimNotificationNo;
	}

	/**
	 * @return 返回 claimRegistrationNo 立案号
	 */
	public String getClaimRegistrationNo() {
		return claimRegistrationNo;
	}

	/**
	 * @param claimRegistrationNo 要设置的 立案号
	 */
	public void setClaimRegistrationNo(String claimRegistrationNo) {
		this.claimRegistrationNo = claimRegistrationNo;
	}

	/**
	 * @return 返回 claimType 理赔类型；参见代码
	 */
	public String getClaimType() {
		return claimType;
	}

	/**
	 * @param claimType 要设置的 理赔类型；参见代码
	 */
	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}

	/**
	 * @return 返回 claimRegistrationTime 立案时间；格式：精确到分钟
	 */
	public Date getClaimRegistrationTime() {
		return claimRegistrationTime;
	}

	/**
	 * @param claimRegistrationTime 要设置的 立案时间；格式：精确到分钟
	 */
	public void setClaimRegistrationTime(Date claimRegistrationTime) {
		this.claimRegistrationTime = claimRegistrationTime;
	}

	/**
	 * @return 返回 estimatedLossAmount 估损金额
	 */
	public String getEstimatedLossAmount() {
		return estimatedLossAmount;
	}

	/**
	 * @param estimatedLossAmount 要设置的 估损金额
	 */
	public void setEstimatedLossAmount(String estimatedLossAmount) {
		this.estimatedLossAmount = estimatedLossAmount;
	}

	/**
	 * @return 返回 subrogationFlag 是否要求代位；参见代码
	 */
	public String getSubrogationFlag() {
		return subrogationFlag;
	}

	/**
	 * @param subrogationFlag 要设置的 是否要求代位；参见代码
	 */
	public void setSubrogationFlag(String subrogationFlag) {
		this.subrogationFlag = subrogationFlag;
	}

}
