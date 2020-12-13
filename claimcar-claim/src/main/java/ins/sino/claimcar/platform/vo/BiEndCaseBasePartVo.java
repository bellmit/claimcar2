/******************************************************************************
 * CREATETIME : 2016年5月24日 下午5:20:30
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
public class BiEndCaseBasePartVo {

	@XmlElement(name = "ClaimSequenceNo", required = true)
	private String claimSequenceNo;// 理赔编号

	@XmlElement(name = "ConfirmSequenceNo", required = true)
	private String confirmSequenceNo;// 投保确认码

	@XmlElement(name = "ClaimNotificationNo", required = true)
	private String claimNotificationNo;// 报案号

	@XmlElement(name = "ClaimAmount ", required = true)
	private Double claimAmount;// 赔款总金额（含施救费）

	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	@XmlElement(name = "ClaimCloseTime", required = true)
	private Date claimCloseTime;// 结案时间；精确到分

	@XmlElement(name = "IsInsured", required = true)
	private String isInsured;// 是否属于保险责任；参见代码

	@XmlElement(name = "ClaimType", required = true)
	private String claimType;// 理赔类型；参见代码

	@XmlElement(name = "PayCause")
	private String payCause;// 垫付原因 参见代码

	@XmlElement(name = "RefuseCause")
	private String refuseCause;// 拒赔原因描述

	@XmlElement(name = "ClaimConfirmCode")
	private String claimConfirmCode;// 赔案结案校验码

	@XmlElement(name = "AccidentType", required = true)
	private String accidentType;// 保险事故分类；参见代码

	@XmlElement(name = "OtherFee")
	private Double otherFee;// 其他费用

	@XmlElement(name = "IsRefuseCase", required = true)
	private String isRefuseCase;// 是否拒赔案件；参见代码

	@XmlElement(name = "DirectClaimAmount", required = true)
	private Double directClaimAmount;// 直接理赔费用总金额

	@XmlElement(name = "IsTotalLoss", required = true)
	private String isTotalLoss;// 是否全损；参见代码
	
	@XmlElement(name = "FraudLogo", required = true)
	private String fraudLogo;  //欺诈标志
	
	@XmlElement(name = "FraudRecoverAmount", required = true)
	private Double fraudRecoverAmount;  //欺诈挽回损失金额

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
	 * @return 返回 claimAmount 赔款总金额（含施救费）
	 */
	public Double getClaimAmount() {
		return claimAmount;
	}

	/**
	 * @param claimAmount 要设置的 赔款总金额（含施救费）
	 */
	public void setClaimAmount(Double claimAmount) {
		this.claimAmount = claimAmount;
	}

	/**
	 * @return 返回 claimCloseTime 结案时间；精确到分
	 */
	public Date getClaimCloseTime() {
		return claimCloseTime;
	}

	/**
	 * @param claimCloseTime 要设置的 结案时间；精确到分
	 */
	public void setClaimCloseTime(Date claimCloseTime) {
		this.claimCloseTime = claimCloseTime;
	}

	/**
	 * @return 返回 isInsured 是否属于保险责任；参见代码
	 */
	public String getIsInsured() {
		return isInsured;
	}

	/**
	 * @param isInsured 要设置的 是否属于保险责任；参见代码
	 */
	public void setIsInsured(String isInsured) {
		this.isInsured = isInsured;
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
	 * @return 返回 payCause 垫付原因 参见代码
	 */
	public String getPayCause() {
		return payCause;
	}

	/**
	 * @param payCause 要设置的 垫付原因 参见代码
	 */
	public void setPayCause(String payCause) {
		this.payCause = payCause;
	}

	/**
	 * @return 返回 refuseCause 拒赔原因描述
	 */
	public String getRefuseCause() {
		return refuseCause;
	}

	/**
	 * @param refuseCause 要设置的 拒赔原因描述
	 */
	public void setRefuseCause(String refuseCause) {
		this.refuseCause = refuseCause;
	}

	/**
	 * @return 返回 claimConfirmCode 赔案结案校验码
	 */
	public String getClaimConfirmCode() {
		return claimConfirmCode;
	}

	/**
	 * @param claimConfirmCode 要设置的 赔案结案校验码
	 */
	public void setClaimConfirmCode(String claimConfirmCode) {
		this.claimConfirmCode = claimConfirmCode;
	}

	/**
	 * @return 返回 accidentType 保险事故分类；参见代码
	 */
	public String getAccidentType() {
		return accidentType;
	}

	/**
	 * @param accidentType 要设置的 保险事故分类；参见代码
	 */
	public void setAccidentType(String accidentType) {
		this.accidentType = accidentType;
	}

	/**
	 * @return 返回 otherFee 其他费用
	 */
	public Double getOtherFee() {
		return otherFee;
	}

	/**
	 * @param otherFee 要设置的 其他费用
	 */
	public void setOtherFee(Double otherFee) {
		this.otherFee = otherFee;
	}

	/**
	 * @return 返回 isRefuseCase 是否拒赔案件；参见代码
	 */
	public String getIsRefuseCase() {
		return isRefuseCase;
	}

	/**
	 * @param isRefuseCase 要设置的 是否拒赔案件；参见代码
	 */
	public void setIsRefuseCase(String isRefuseCase) {
		this.isRefuseCase = isRefuseCase;
	}

	/**
	 * @return 返回 directClaimAmount 直接理赔费用总金额
	 */
	public Double getDirectClaimAmount() {
		return directClaimAmount;
	}

	public String getFraudLogo() {
		return fraudLogo;
	}

	public void setFraudLogo(String fraudLogo) {
		this.fraudLogo = fraudLogo;
	}

	public Double getFraudRecoverAmount() {
		return fraudRecoverAmount;
	}

	public void setFraudRecoverAmount(Double fraudRecoverAmount) {
		this.fraudRecoverAmount = fraudRecoverAmount;
	}

	/**
	 * @param directClaimAmount 要设置的 直接理赔费用总金额
	 */
	public void setDirectClaimAmount(Double directClaimAmount) {
		this.directClaimAmount = directClaimAmount;
	}

	/**
	 * @return 返回 isTotalLoss 是否全损；参见代码
	 */
	public String getIsTotalLoss() {
		return isTotalLoss;
	}

	/**
	 * @param isTotalLoss 要设置的 是否全损；参见代码
	 */
	public void setIsTotalLoss(String isTotalLoss) {
		this.isTotalLoss = isTotalLoss;
	}

}
