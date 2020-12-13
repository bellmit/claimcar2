package ins.sino.claimcar.payment.detail.vo;

import ins.platform.utils.xstreamconverters.SinosoftDoubleConverter;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

/**
 * 收款直付账号明细VO
 * 
 * @author ★<a href="mailto:huangyi@sinosoft.com.cn">HuangYi</a>
 * @Company www.sinosoft.com.cn
 * @Copyright Copyright (c) 2016年7月6日
 * @since (2016年7月6日 下午8:04:12): <br>
 */
@XStreamAlias("JlinkAccount")
public class JlinkAccountVo implements Serializable {

	private static final long serialVersionUID = -5505245044836566089L;
	@XStreamAlias("PayRefReason")
	private String payRefReason;// 字符 收付原因 代码
	@XStreamAlias("PlanFee")
	@XStreamConverter(value = SinosoftDoubleConverter.class,strings = {"0.00"})
	private Double planFee;// 货币 支付金额
	@XStreamAlias("AccountNo")
	private String accountNo;// 字符 收款人ID
	@XStreamAlias("PayReasonFlag")
	private String payReasonFlag;// 字符 是否直付例外（0：否1：是）
	@XStreamAlias("PayReason")
	private String payReason;// 字符 直付例外原因，当PayReasonFlag为1时必传
	@XStreamAlias("PayObject")
	private String payObject;// 字符 支付对象 代码
	@XStreamAlias("SubPayObject")
	private String subPayObject;// 字符 支付子对象 代码
	@XStreamAlias("CentiType")
	private String centiType;// 字符 赔款收款人证件类型 代码
	@XStreamAlias("CentiCode")
	private String centiCode;// 字符 赔款收款人证件号码
	@XStreamAlias("PublicPrivateFlag")
	private String publicPrivateFlag;//公私标志（0：对私1：对公）
	
	@XStreamAlias("AbstractContent")
	private String abstractContent;//摘要
	@XStreamAlias("IsAutoPay")
	private String isAutoPay;//是否自动支付（0：否1：是）
	@XStreamAlias("IsExpress")
	private String isExpress;//是否加急（0：否1：是）
	@XStreamAlias("IsFastReparation")
	private String isFastReparation;//是否快赔（0：否1：是）
	@XStreamAlias("Usage")
	private String usage;//用途
	@XStreamAlias("OtherRemark")
	private String otherRemark;//备注
	@XStreamAlias("MessageContent")
	private String messageContent;//短信内容
	@XStreamAlias("Telephone")
	private String telephone;//电话号码

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getPayReasonFlag() {
		return payReasonFlag;
	}

	public void setPayReasonFlag(String payReasonFlag) {
		this.payReasonFlag = payReasonFlag;
	}

	public String getPayReason() {
		return payReason;
	}

	public void setPayReason(String payReason) {
		this.payReason = payReason;
	}

	public String getPayObject() {
		return payObject;
	}

	public void setPayObject(String payObject) {
		this.payObject = payObject;
	}

	public String getSubPayObject() {
		return subPayObject;
	}

	public void setSubPayObject(String subPayObject) {
		this.subPayObject = subPayObject;
	}

	public String getCentiType() {
		return centiType;
	}

	public void setCentiType(String centiType) {
		this.centiType = centiType;
	}

	public String getCentiCode() {
		return centiCode;
	}

	public void setCentiCode(String centiCode) {
		this.centiCode = centiCode;
	}

	public String getPayRefReason() {
		return payRefReason;
	}

	public void setPayRefReason(String payRefReason) {
		this.payRefReason = payRefReason;
	}

	public Double getPlanFee() {
		return planFee;
	}

	public void setPlanFee(Double planFee) {
		this.planFee = planFee;
	}

	public String getPublicPrivateFlag() {
		return publicPrivateFlag;
	}

	public void setPublicPrivateFlag(String publicPrivateFlag) {
		this.publicPrivateFlag = publicPrivateFlag;
	}

	public String getAbstractContent() {
		return abstractContent;
	}

	public void setAbstractContent(String abstractContent) {
		this.abstractContent = abstractContent;
	}

	public String getIsAutoPay() {
		return isAutoPay;
	}

	public void setIsAutoPay(String isAutoPay) {
		this.isAutoPay = isAutoPay;
	}

	public String getIsExpress() {
		return isExpress;
	}

	public void setIsExpress(String isExpress) {
		this.isExpress = isExpress;
	}

	public String getIsFastReparation() {
		return isFastReparation;
	}

	public void setIsFastReparation(String isFastReparation) {
		this.isFastReparation = isFastReparation;
	}

	public String getUsage() {
		return usage;
	}

	public void setUsage(String usage) {
		this.usage = usage;
	}

	public String getOtherRemark() {
		return otherRemark;
	}

	public void setOtherRemark(String otherRemark) {
		this.otherRemark = otherRemark;
	}

	public String getMessageContent() {
		return messageContent;
	}

	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

}
