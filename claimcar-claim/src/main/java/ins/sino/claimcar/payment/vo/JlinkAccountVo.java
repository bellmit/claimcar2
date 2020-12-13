package ins.sino.claimcar.payment.vo;

import ins.platform.utils.xstreamconverters.SinosoftDoubleConverter;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

/**
 * �տ�ֱ���˺���ϸVO
 * 
 * @author ��<a href="mailto:huangyi@sinosoft.com.cn">HuangYi</a>
 * @Company www.sinosoft.com.cn
 * @Copyright Copyright (c) 2016��7��6��
 * @since (2016��7��6�� ����8:04:12): <br>
 */
@XStreamAlias("JlinkAccount")
public class JlinkAccountVo implements Serializable {

	private static final long serialVersionUID = -5505245044836566089L;
	@XStreamAlias("PayRefReason")
	private String payRefReason;// �ַ� �ո�ԭ�� ����
	@XStreamAlias("PlanFee")
	@XStreamConverter(value = SinosoftDoubleConverter.class,strings = {"0.00"})
	private Double planFee;// ���� ֧�����
	@XStreamAlias("AccountNo")
	private String accountNo;// �ַ� �տ���ID
	@XStreamAlias("PayReasonFlag")
	private String payReasonFlag;// �ַ� �Ƿ�ֱ�����⣨0����1���ǣ�
	@XStreamAlias("PayReason")
	private String payReason;// �ַ� ֱ������ԭ�򣬵�PayReasonFlagΪ1ʱ�ش�
	@XStreamAlias("PayObject")
	private String payObject;// �ַ� ֧������ ����
	@XStreamAlias("SubPayObject")
	private String subPayObject;// �ַ� ֧���Ӷ��� ����
	@XStreamAlias("CentiType")
	private String centiType;// �ַ� ����տ���֤������ ����
	@XStreamAlias("CentiCode")
	private String centiCode;// �ַ� ����տ���֤������
	
	@XStreamAlias("PublicPrivateFlag")
	private String publicPrivateFlag;//是否对公对私
	@XStreamAlias("AbstractContent") 
	private String abstractContent;//摘要
	@XStreamAlias("IsAutoPay")
	private String isAutoPay;//是否自动支付
	@XStreamAlias("IsExpress")
	private String isExpress;//是否加急
	@XStreamAlias("IsFastReparation")
	private String isFastReparation;//是否快赔
	@XStreamAlias("AccountCode")
	private String accountCode;//银行账号
	@XStreamAlias("AccountName")
	private String accountName;//银行户名
	
	

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

	public String getAccountCode() {
		return accountCode;
	}

	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	
	

}
