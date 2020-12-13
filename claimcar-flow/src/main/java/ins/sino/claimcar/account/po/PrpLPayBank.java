package ins.sino.claimcar.account.po;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@TableGenerator(name = "utiSequence", table = "UTISEQUENCE", pkColumnName = "GenName", valueColumnName = "GenValue", pkColumnValue = "PRPLPAYBANK_PK", allocationSize = 10)
@Table(name = "PRPLPAYBANK")
public class PrpLPayBank implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private BigDecimal id;
	private String policyNo;
	private String registNo;
	private String claimNo;
	private String riskCode;
	private String compensateNo;
	private String lossType;
	private String businessType;
	private String chargeCode;
	private String chargeName;
	private String accountsTypeCode;
	private String accountsTypeName;
	private String payObjectType;
	private String payee;
	private String payeeIDNumber;
	private BigDecimal payeeNumber;
	private String payeeMobile;
	private String bankName;
	private String bankOutlets;
	private String bankType;
	private String accountName;
	private String accountNo;
	private Long areaCode;
	
	private String bankNo;
	private String priorityFlag;
	private String purpose;
	private String validFlag;
	private String flag;
	private String remark;
	private String createUser;
	private Date createTime;
	private String updateUser;
	private Date updateTime;
	private String insuredName;
	private Date appTime;
	private Long payeeId;
	private String payType;
	private String payObjectKind;
	private Long provinceCode;
	private String province;
	private Long cityCode;
	private String city;
	private String publicAndPrivate;//公私标志
	private String summary;//摘要
	private String certifyType;
	private String repairFactoryId;//修理厂主表ID
	private String errorMessage;
	private String isAutoPay;//是否送资金，0-否，1-是
	private String errorType;

	//审核增加字段
	private String isVerify;
	private String verifyUser;
	private Date verifyDate;
	private String verifyStatus;
	private String verifyText;
	private String verifyHandle;
	private String accountId;

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "utiSequence")   //自动生成主键
	@Column(name = "ID", unique = true, nullable = false, scale=0)
	public BigDecimal getId() {
		return this.id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}

	@Column(name = "POLICYNO", length=24)
	public String getPolicyNo() {
		return this.policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	@Column(name = "REGISTNO", length=24)
	public String getRegistNo() {
		return this.registNo;
	}

	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}

	@Column(name = "CLAIMNO", length=24)
	public String getClaimNo() {
		return this.claimNo;
	}

	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}

	@Column(name = "RISKCODE", length=4)
	public String getRiskCode() {
		return this.riskCode;
	}

	public void setRiskCode(String riskCode) {
		this.riskCode = riskCode;
	}

	@Column(name = "COMPENSATENO", length=22)
	public String getCompensateNo() {
		return this.compensateNo;
	}

	public void setCompensateNo(String compensateNo) {
		this.compensateNo = compensateNo;
	}

	@Column(name = "LOSSTYPE", length=2)
	public String getLossType() {
		return this.lossType;
	}

	public void setLossType(String lossType) {
		this.lossType = lossType;
	}

	@Column(name = "BUSINESSTYPE", length=1)
	public String getBusinessType() {
		return this.businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	@Column(name = "CHARGECODE", length=7)
	public String getChargeCode() {
		return this.chargeCode;
	}

	public void setChargeCode(String chargeCode) {
		this.chargeCode = chargeCode;
	}

	@Column(name = "CHARGENAME", length=16)
	public String getChargeName() {
		return this.chargeName;
	}

	public void setChargeName(String chargeName) {
		this.chargeName = chargeName;
	}

	@Column(name = "ACCOUNTSTYPECODE", length=10)
	public String getAccountsTypeCode() {
		return this.accountsTypeCode;
	}

	public void setAccountsTypeCode(String accountsTypeCode) {
		this.accountsTypeCode = accountsTypeCode;
	}

	@Column(name = "ACCOUNTSTYPENAME", length=50)
	public String getAccountsTypeName() {
		return this.accountsTypeName;
	}

	public void setAccountsTypeName(String accountsTypeName) {
		this.accountsTypeName = accountsTypeName;
	}

	@Column(name = "PAYOBJECTTYPE", length=2)
	public String getPayObjectType() {
		return this.payObjectType;
	}

	public void setPayObjectType(String payObjectType) {
		this.payObjectType = payObjectType;
	}

	@Column(name = "PAYEE", length=50)
	public String getPayee() {
		return this.payee;
	}

	public void setPayee(String payee) {
		this.payee = payee;
	}

	@Column(name = "PAYEEIDNUMBER", length=25)
	public String getPayeeIDNumber() {
		return this.payeeIDNumber;
	}

	public void setPayeeIDNumber(String payeeIDNumber) {
		this.payeeIDNumber = payeeIDNumber;
	}

	@Column(name = "PAYEENUMBER", precision=20, scale=0)
	public BigDecimal getPayeeNumber() {
		return this.payeeNumber;
	}

	public void setPayeeNumber(BigDecimal payeeNumber) {
		this.payeeNumber = payeeNumber;
	}

	@Column(name = "PAYEEMOBILE", length=25)
	public String getPayeeMobile() {
		return this.payeeMobile;
	}

	public void setPayeeMobile(String payeeMobile) {
		this.payeeMobile = payeeMobile;
	}

	@Column(name = "BANKNAME", length=60)
	public String getBankName() {
		return this.bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	@Column(name = "BANKOUTLETS", length=100)
	public String getBankOutlets() {
		return this.bankOutlets;
	}

	public void setBankOutlets(String bankOutlets) {
		this.bankOutlets = bankOutlets;
	}

	@Column(name = "BANKTYPE", length=5)
	public String getBankType() {
		return this.bankType;
	}

	public void setBankType(String bankType) {
		this.bankType = bankType;
	}

	@Column(name = "ACCOUNTNAME", length=60)
	public String getAccountName() {
		return this.accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	@Column(name = "ACCOUNTNO", length=35)
	public String getAccountNo() {
		return this.accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}


	

	@Column(name = "BANKNO", length=30)
	public String getBankNo() {
		return this.bankNo;
	}

	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
	}

	@Column(name = "PRIORITYFLAG", length=2)
	public String getPriorityFlag() {
		return this.priorityFlag;
	}

	public void setPriorityFlag(String priorityFlag) {
		this.priorityFlag = priorityFlag;
	}

	@Column(name = "PURPOSE", length=60)
	public String getPurpose() {
		return this.purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	@Column(name = "VALIDFLAG", length=2)
	public String getValidFlag() {
		return this.validFlag;
	}

	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}

	@Column(name = "FLAG", length=10)
	public String getFlag() {
		return this.flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	@Column(name = "REMARK")
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "CREATEUSER", length=10)
	public String getCreateUser() {
		return this.createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATETIME", length=7)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "UPDATEUSER", length=10)
	public String getUpdateUser() {
		return this.updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATETIME", length=7)
	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Long getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(Long areaCode) {
		this.areaCode = areaCode;
	}

	@Column(name = "INSUREDNAME", length=10)
	public String getInsuredName() {
		return insuredName;
	}

	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "APPTIME", length=7)
	public Date getAppTime() {
		return appTime;
	}

	public void setAppTime(Date appTime) {
		this.appTime = appTime;
	}

	@Column(name = "PAYEEID", length=15)
	public Long getPayeeId() {
		return payeeId;
	}

	public void setPayeeId(Long payeeId) {
		this.payeeId = payeeId;
	}
	
	@Column(name = "ISVERIFY", length=2)
	public String getIsVerify() {
		return isVerify;
	}
	public void setIsVerify(String isVerify) {
		this.isVerify = isVerify;
	}
	
	@Column(name = "VERIFYUSER", length=30)
	public String getVerifyUser() {
		return verifyUser;
	}
	public void setVerifyUser(String verifyUser) {
		this.verifyUser = verifyUser;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "VERIFYDATE")
	public Date getVerifyDate() {
		return verifyDate;
	}
	public void setVerifyDate(Date verifyDate) {
		this.verifyDate = verifyDate;
	}
	
	@Column(name = "VERIFYSTATUS", length=5)
	public String getVerifyStatus() {
		return verifyStatus;
	}
	public void setVerifyStatus(String verifyStatus) {
		this.verifyStatus = verifyStatus;
	}
	
	@Column(name = "VERIFYTEXT", length=1000)
	public String getVerifyText() {
		return verifyText;
	}
	public void setVerifyText(String verifyText) {
		this.verifyText = verifyText;
	}
	
	@Column(name = "VERIFYHANDLE", length=10)
	public String getVerifyHandle() {
		return verifyHandle;
	}
	public void setVerifyHandle(String verifyHandle) {
		this.verifyHandle = verifyHandle;
	}

	@Column(name = "ACCOUNTID", length=22)
	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	@Column(name = "PAYTYPE", length=5)
	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getPayObjectKind() {
		return payObjectKind;
	}

	public void setPayObjectKind(String payObjectKind) {
		this.payObjectKind = payObjectKind;
	}
	
	@Column(name = "PROVINCECODE", precision=10, scale=0)
	public Long getProvinceCode() {
		return this.provinceCode;
	}

	public void setProvinceCode(Long provinceCode) {
		this.provinceCode = provinceCode;
	}
	
	@Column(name = "PROVINCE", length=20)
	public String getProvince() {
		return this.province;
	}

	public void setProvince(String province) {
		this.province = province;
	}
	
	@Column(name = "CITYCODE", precision=10, scale=0)
	public Long getCityCode() {
		return this.cityCode;
	}

	public void setCityCode(Long cityCode) {
		this.cityCode = cityCode;
	}

	@Column(name = "CITY", length=30)
	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	
	@Column(name = "PUBLICANDPRIVATE", length=10)
	public String getPublicAndPrivate() {
		return publicAndPrivate;
	}

	public void setPublicAndPrivate(String publicAndPrivate) {
		this.publicAndPrivate = publicAndPrivate;
	}
	
	@Column(name = "SUMMARY", length=2000)
	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}
	
	@Column(name = "CERTIFYTYPE", length=4)
	public String getCertifyType() {
		return this.certifyType;
	}

	public void setCertifyType(String certifyType) {
		this.certifyType = certifyType;
	}
	
	@Column(name = "REPAIRFACTORYID", length=20)
	public String getRepairFactoryId() {
		return repairFactoryId;
	}

	public void setRepairFactoryId(String repairFactoryId) {
		this.repairFactoryId = repairFactoryId;
	}

	@Column(name = "ERRORMESSAGE", length=500)
	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	@Column(name = "ISAUTOPAY", length=5)
	public String getIsAutoPay() {
		return isAutoPay;
	}

	public void setIsAutoPay(String isAutoPay) {
		this.isAutoPay = isAutoPay;
	}

	@Column(name = "ERRORTYPE", length=5)
	public String getErrorType() {
		return errorType;
	}

	public void setErrorType(String errorType) {
		this.errorType = errorType;
	}

}
