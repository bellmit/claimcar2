package ins.sino.claimcar.pinganunion.vo.paymentclient;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
/**
 * 
 * @Description: 反洗钱信息查询接口
 * @author: zhubin
 * @date: 2020年8月11日 下午4:17:13
 */
public class PingAnPaymentClientDTO implements Serializable{
	private static final long serialVersionUID = -2478657397469414319L;
	private String idClmPaymentClientInfo;//支付客户信息主键：
	private String idClmPaymentInfo;//支付对象信息表主键：
	private String reportNo;//报案号：
	private String infoAttribute;//信息类型：代码定义3.28
	private String clientName;//客户名称：
	private String country;//国籍：
	private String sex;//性别：F女  M男
	private String carrer;//职业：
	private String address;//住所地/常住地/工作单位地址：
	private String contact;//联系方式：
	private String clientCertificateType;//证件类型：代码定义3.10
	private String clientCertificateNo;//证件号：
	private String businessScope;//经营范围：
	private String businessLicenceNo;//营业执照号码：
	private String taxCertificateNo;//税务登记证号码：
	private String orgCodeCertificateNo;//组织机构代码证号码：
	private String businessLicenceBeginDate;//营业执照有效期限起始日：
	private String businessLicenceEndDate;//营业执照有效期限结束日：
	private String taxValidBeginDate;//税务登记证有效期限起始日：
	private String taxValidEndDate;//税务登记证有效期限结束日：
	private String taxLongTerm;//税务登记证有效期限是否长期有效：Y-是,N-否
	private String businessLicenceLongTerm;//营业执照有效期限是否长期有效：Y-是,N-否
	private String orgValidBeginDate;//组织机构代码证有效期限起始日：
	private String orgValidEndDate;//组织机构代码证有效期限结束日：
	private String orgLongTerm;//组织机构代码证有效期限是否长期有效：Y-是,N-否
	private String unifiedSocialCreditCode;//统一社会信用代码：
	private String socialCreditValidBeginDate;//统一社会信用代码营业有效期限起始日：
	private String socialCreditValidEndDate;//统一社会信用代码营业有效期限结束日：
	private String socialCreditLongTerm;//统一社会信用代码营业期限是否长期有效：Y-是,N-否
	private String threeInOne;//是否三证合一：
	private String registeredCapital;//注册资本：
	private String clientCertLongTerm;//个人证件有效期是否长期有效：Y-是,N-否
	private String beneficiaryAddress;//受益所有人地址：
	private String orgPersonName;//团体联系人：
	private String orgPersonContact;//团体联系人电话：
	private String orgCerType;//团体证件类型：
	private String orgCerTypeExtend;//团体证件类型扩展：团体证件类型为其他时，才有值
	private String orgCerNo;//团体证件号：
	private String orgCerValidBeginDate;//团体证件有效起始日：
	private String orgCerValidEndDate;//团体证件有效结束日：
	private String orgCerIsValidLongTerm;//团体证件有效期限是否长期有效：Y-是,N-否：默认为N
	private String careerType;//职业类型：代码定义3.30
	private String careerTypeExtend;//职业类型扩展：职业类型其他时，才有值
	private String orgPersonAddress;//团体客户联系地址：
	private String wordCompany;//工作单位：
	private String annualIncome;//年收入,单位：万元：
	private String trade;//行业：
	private String stateAttribute;//国有属性：代码定义3.32
	private String registeredCapitalCurrency;//注册资本金币种：代码定义3.33
	private String countryCode;//国籍编码：
	@JSONField(serialzeFeatures=SerializerFeature.WriteMapNullValue, name="validBeginDate", format="yyyy-MM-dd hh:mm:ss")
	private Date validBeginDate;//证件有效期限起始日：
	@JSONField(serialzeFeatures=SerializerFeature.WriteMapNullValue, name="validEndDate", format="yyyy-MM-dd hh:mm:ss")
	private Date validEndDate;//证件有效期限结束日：
	private List<PaymentCmpRefCerList> paymentCmpRefCerList;//团体受益信息：
	public String getIdClmPaymentClientInfo() {
		return idClmPaymentClientInfo;
	}
	public void setIdClmPaymentClientInfo(String idClmPaymentClientInfo) {
		this.idClmPaymentClientInfo = idClmPaymentClientInfo;
	}
	public String getIdClmPaymentInfo() {
		return idClmPaymentInfo;
	}
	public void setIdClmPaymentInfo(String idClmPaymentInfo) {
		this.idClmPaymentInfo = idClmPaymentInfo;
	}
	public String getReportNo() {
		return reportNo;
	}
	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}
	public String getInfoAttribute() {
		return infoAttribute;
	}
	public void setInfoAttribute(String infoAttribute) {
		this.infoAttribute = infoAttribute;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getCarrer() {
		return carrer;
	}
	public void setCarrer(String carrer) {
		this.carrer = carrer;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
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
	public String getBusinessScope() {
		return businessScope;
	}
	public void setBusinessScope(String businessScope) {
		this.businessScope = businessScope;
	}
	public String getBusinessLicenceNo() {
		return businessLicenceNo;
	}
	public void setBusinessLicenceNo(String businessLicenceNo) {
		this.businessLicenceNo = businessLicenceNo;
	}
	public String getTaxCertificateNo() {
		return taxCertificateNo;
	}
	public void setTaxCertificateNo(String taxCertificateNo) {
		this.taxCertificateNo = taxCertificateNo;
	}
	public String getOrgCodeCertificateNo() {
		return orgCodeCertificateNo;
	}
	public void setOrgCodeCertificateNo(String orgCodeCertificateNo) {
		this.orgCodeCertificateNo = orgCodeCertificateNo;
	}
	public String getBusinessLicenceBeginDate() {
		return businessLicenceBeginDate;
	}
	public void setBusinessLicenceBeginDate(String businessLicenceBeginDate) {
		this.businessLicenceBeginDate = businessLicenceBeginDate;
	}
	public String getBusinessLicenceEndDate() {
		return businessLicenceEndDate;
	}
	public void setBusinessLicenceEndDate(String businessLicenceEndDate) {
		this.businessLicenceEndDate = businessLicenceEndDate;
	}
	public String getTaxValidBeginDate() {
		return taxValidBeginDate;
	}
	public void setTaxValidBeginDate(String taxValidBeginDate) {
		this.taxValidBeginDate = taxValidBeginDate;
	}
	public String getTaxValidEndDate() {
		return taxValidEndDate;
	}
	public void setTaxValidEndDate(String taxValidEndDate) {
		this.taxValidEndDate = taxValidEndDate;
	}
	public String getTaxLongTerm() {
		return taxLongTerm;
	}
	public void setTaxLongTerm(String taxLongTerm) {
		this.taxLongTerm = taxLongTerm;
	}
	public String getBusinessLicenceLongTerm() {
		return businessLicenceLongTerm;
	}
	public void setBusinessLicenceLongTerm(String businessLicenceLongTerm) {
		this.businessLicenceLongTerm = businessLicenceLongTerm;
	}
	public String getOrgValidBeginDate() {
		return orgValidBeginDate;
	}
	public void setOrgValidBeginDate(String orgValidBeginDate) {
		this.orgValidBeginDate = orgValidBeginDate;
	}
	public String getOrgValidEndDate() {
		return orgValidEndDate;
	}
	public void setOrgValidEndDate(String orgValidEndDate) {
		this.orgValidEndDate = orgValidEndDate;
	}
	public String getOrgLongTerm() {
		return orgLongTerm;
	}
	public void setOrgLongTerm(String orgLongTerm) {
		this.orgLongTerm = orgLongTerm;
	}
	public String getUnifiedSocialCreditCode() {
		return unifiedSocialCreditCode;
	}
	public void setUnifiedSocialCreditCode(String unifiedSocialCreditCode) {
		this.unifiedSocialCreditCode = unifiedSocialCreditCode;
	}
	public String getSocialCreditValidBeginDate() {
		return socialCreditValidBeginDate;
	}
	public void setSocialCreditValidBeginDate(String socialCreditValidBeginDate) {
		this.socialCreditValidBeginDate = socialCreditValidBeginDate;
	}
	public String getSocialCreditValidEndDate() {
		return socialCreditValidEndDate;
	}
	public void setSocialCreditValidEndDate(String socialCreditValidEndDate) {
		this.socialCreditValidEndDate = socialCreditValidEndDate;
	}
	public String getSocialCreditLongTerm() {
		return socialCreditLongTerm;
	}
	public void setSocialCreditLongTerm(String socialCreditLongTerm) {
		this.socialCreditLongTerm = socialCreditLongTerm;
	}
	public String getThreeInOne() {
		return threeInOne;
	}
	public void setThreeInOne(String threeInOne) {
		this.threeInOne = threeInOne;
	}
	public String getRegisteredCapital() {
		return registeredCapital;
	}
	public void setRegisteredCapital(String registeredCapital) {
		this.registeredCapital = registeredCapital;
	}
	public String getClientCertLongTerm() {
		return clientCertLongTerm;
	}
	public void setClientCertLongTerm(String clientCertLongTerm) {
		this.clientCertLongTerm = clientCertLongTerm;
	}
	public String getBeneficiaryAddress() {
		return beneficiaryAddress;
	}
	public void setBeneficiaryAddress(String beneficiaryAddress) {
		this.beneficiaryAddress = beneficiaryAddress;
	}
	public String getOrgPersonName() {
		return orgPersonName;
	}
	public void setOrgPersonName(String orgPersonName) {
		this.orgPersonName = orgPersonName;
	}
	public String getOrgPersonContact() {
		return orgPersonContact;
	}
	public void setOrgPersonContact(String orgPersonContact) {
		this.orgPersonContact = orgPersonContact;
	}
	public String getOrgCerType() {
		return orgCerType;
	}
	public void setOrgCerType(String orgCerType) {
		this.orgCerType = orgCerType;
	}
	public String getOrgCerTypeExtend() {
		return orgCerTypeExtend;
	}
	public void setOrgCerTypeExtend(String orgCerTypeExtend) {
		this.orgCerTypeExtend = orgCerTypeExtend;
	}
	public String getOrgCerNo() {
		return orgCerNo;
	}
	public void setOrgCerNo(String orgCerNo) {
		this.orgCerNo = orgCerNo;
	}
	public String getOrgCerValidBeginDate() {
		return orgCerValidBeginDate;
	}
	public void setOrgCerValidBeginDate(String orgCerValidBeginDate) {
		this.orgCerValidBeginDate = orgCerValidBeginDate;
	}
	public String getOrgCerValidEndDate() {
		return orgCerValidEndDate;
	}
	public void setOrgCerValidEndDate(String orgCerValidEndDate) {
		this.orgCerValidEndDate = orgCerValidEndDate;
	}
	public String getOrgCerIsValidLongTerm() {
		return orgCerIsValidLongTerm;
	}
	public void setOrgCerIsValidLongTerm(String orgCerIsValidLongTerm) {
		this.orgCerIsValidLongTerm = orgCerIsValidLongTerm;
	}
	public String getCareerType() {
		return careerType;
	}
	public void setCareerType(String careerType) {
		this.careerType = careerType;
	}
	public String getCareerTypeExtend() {
		return careerTypeExtend;
	}
	public void setCareerTypeExtend(String careerTypeExtend) {
		this.careerTypeExtend = careerTypeExtend;
	}
	public String getOrgPersonAddress() {
		return orgPersonAddress;
	}
	public void setOrgPersonAddress(String orgPersonAddress) {
		this.orgPersonAddress = orgPersonAddress;
	}
	public String getWordCompany() {
		return wordCompany;
	}
	public void setWordCompany(String wordCompany) {
		this.wordCompany = wordCompany;
	}
	public String getAnnualIncome() {
		return annualIncome;
	}
	public void setAnnualIncome(String annualIncome) {
		this.annualIncome = annualIncome;
	}
	public String getTrade() {
		return trade;
	}
	public void setTrade(String trade) {
		this.trade = trade;
	}
	public String getStateAttribute() {
		return stateAttribute;
	}
	public void setStateAttribute(String stateAttribute) {
		this.stateAttribute = stateAttribute;
	}
	public String getRegisteredCapitalCurrency() {
		return registeredCapitalCurrency;
	}
	public void setRegisteredCapitalCurrency(String registeredCapitalCurrency) {
		this.registeredCapitalCurrency = registeredCapitalCurrency;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	
	public Date getValidBeginDate() {
		return validBeginDate;
	}
	public void setValidBeginDate(Date validBeginDate) {
		this.validBeginDate = validBeginDate;
	}
	public Date getValidEndDate() {
		return validEndDate;
	}
	public void setValidEndDate(Date validEndDate) {
		this.validEndDate = validEndDate;
	}
	public List<PaymentCmpRefCerList> getPaymentCmpRefCerList() {
		return paymentCmpRefCerList;
	}
	public void setPaymentCmpRefCerList(
			List<PaymentCmpRefCerList> paymentCmpRefCerList) {
		this.paymentCmpRefCerList = paymentCmpRefCerList;
	}
	

}
