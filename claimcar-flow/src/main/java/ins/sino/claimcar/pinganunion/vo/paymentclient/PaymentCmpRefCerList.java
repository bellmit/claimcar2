package ins.sino.claimcar.pinganunion.vo.paymentclient;

import java.io.Serializable;
/**
 * 
 * @Description: 反洗钱信息查询接口:隶属于paymentCmpRefCerList团体受益信息
 * @author: zhubin
 * @date: 2020年8月11日 下午4:34:24
 */
public class PaymentCmpRefCerList implements Serializable{
	private static final long serialVersionUID = 3259867694998691087L;
	private String idClmPaymentCmpRefCer;//主键：
	private String idClmPaymentClientInfo;//客户信息主键：
	private String representativeName;//代表人姓名：
	private String identityType;//身份类型：代码定义3.34
	private String clientCertificateType;//证件类型：代码定义3.10
	private String clientCertificateNo;//证件号码：
	private String validBeginDate;//证件有效起始日：
	private String validEndDate;//证件有效结束日：
	private String isValidLongTerm;//是否长期有效：Y-是,N-否
	private String beneficiaryAddress;//受益所有人地址：
	private String determineBeneficiaryWay;//判定受益所有人方式：代码定义3.31
	private String sharesVotePercent;//受益所有人持股数量或表决权占比：（0.00至100.00）
	private String clientCertificateCode;//团体跟个人类型：
	public String getIdClmPaymentCmpRefCer() {
		return idClmPaymentCmpRefCer;
	}
	public void setIdClmPaymentCmpRefCer(String idClmPaymentCmpRefCer) {
		this.idClmPaymentCmpRefCer = idClmPaymentCmpRefCer;
	}
	public String getIdClmPaymentClientInfo() {
		return idClmPaymentClientInfo;
	}
	public void setIdClmPaymentClientInfo(String idClmPaymentClientInfo) {
		this.idClmPaymentClientInfo = idClmPaymentClientInfo;
	}
	public String getRepresentativeName() {
		return representativeName;
	}
	public void setRepresentativeName(String representativeName) {
		this.representativeName = representativeName;
	}
	public String getIdentityType() {
		return identityType;
	}
	public void setIdentityType(String identityType) {
		this.identityType = identityType;
	}
	public String getClientCertificateType() {
		return clientCertificateType;
	}
	public void setClientCertificateType(String clientCertificateType) {
		this.clientCertificateType = clientCertificateType;
	}
	public String getClientCertificateNo() {
		return clientCertificateNo;
	}
	public void setClientCertificateNo(String clientCertificateNo) {
		this.clientCertificateNo = clientCertificateNo;
	}
	public String getValidBeginDate() {
		return validBeginDate;
	}
	public void setValidBeginDate(String validBeginDate) {
		this.validBeginDate = validBeginDate;
	}
	public String getValidEndDate() {
		return validEndDate;
	}
	public void setValidEndDate(String validEndDate) {
		this.validEndDate = validEndDate;
	}
	public String getIsValidLongTerm() {
		return isValidLongTerm;
	}
	public void setIsValidLongTerm(String isValidLongTerm) {
		this.isValidLongTerm = isValidLongTerm;
	}
	public String getBeneficiaryAddress() {
		return beneficiaryAddress;
	}
	public void setBeneficiaryAddress(String beneficiaryAddress) {
		this.beneficiaryAddress = beneficiaryAddress;
	}
	public String getDetermineBeneficiaryWay() {
		return determineBeneficiaryWay;
	}
	public void setDetermineBeneficiaryWay(String determineBeneficiaryWay) {
		this.determineBeneficiaryWay = determineBeneficiaryWay;
	}
	public String getSharesVotePercent() {
		return sharesVotePercent;
	}
	public void setSharesVotePercent(String sharesVotePercent) {
		this.sharesVotePercent = sharesVotePercent;
	}
	public String getClientCertificateCode() {
		return clientCertificateCode;
	}
	public void setClientCertificateCode(String clientCertificateCode) {
		this.clientCertificateCode = clientCertificateCode;
	}
	
}
