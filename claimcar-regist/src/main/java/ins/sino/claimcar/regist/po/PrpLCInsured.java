package ins.sino.claimcar.regist.po;

// Generated by Hibernate Tools 3.2.5 (sinosoft version), Don't modify!
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * POJO Class PrpLCInsured
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "PRPLCINSURED")
public class PrpLCInsured implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private Long insuredId;
	private PrpLCMain prpLCMain;
	private String policyNo;
	private String riskCode;
	private String language;
	private String insuredType;
	private String insuredCode;
	private String insuredName;
	private String insuredAddress;
	private String insuredNature;
	private String insuredFlag;
	private String insuredIdentity;
	private Long relateSerialno;
	private String identifyType;
	private String identifyNumber;
	private String sex;
	private Long age;
	private String creditLevel;
	private String possessNature;
	private String businessSource;
	private String businessSort;
	private String occupationCode;
	private String educationCode;
	private String bank;
	private String accountName;
	private String account;
	private String linkerName;
	private String postAddress;
	private String postCode;
	private String phoneNumber;
	private String mobile;
	private String email;
	private BigDecimal benefitRate;
	private String benefitFlag;
	private String occupationGrade;
	private String validFlag;
	private String flag;
	private String remark;
	private String createUser;
	private Date createTime;
	private String updateUser;
	private Date updateTime;
	
	@Id
	@SequenceGenerator(name="sequence",sequenceName="SEQ_PRPLCINSURED",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="sequence")
	@Column(name = "INSUREDID", unique = true, nullable = false, scale=0)
	public Long getInsuredId() {
		return this.insuredId;
	}

	public void setInsuredId(Long insuredId) {
		this.insuredId = insuredId;
	}

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="PRPLCMAINID", nullable = false)
	public PrpLCMain getPrpLCMain() {
		return this.prpLCMain;
	}

	public void setPrpLCMain(PrpLCMain prpLCMain) {
		this.prpLCMain = prpLCMain;
	}

	@Column(name = "POLICYNO", nullable = false, length=22)
	public String getPolicyNo() {
		return this.policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	@Column(name = "RISKCODE", nullable = false, length=4)
	public String getRiskCode() {
		return this.riskCode;
	}

	public void setRiskCode(String riskCode) {
		this.riskCode = riskCode;
	}

	@Column(name = "LANGUAGE", length=1)
	public String getLanguage() {
		return this.language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	@Column(name = "INSUREDTYPE", length=1)
	public String getInsuredType() {
		return this.insuredType;
	}

	public void setInsuredType(String insuredType) {
		this.insuredType = insuredType;
	}

	@Column(name = "INSUREDCODE", length=20)
	public String getInsuredCode() {
		return this.insuredCode;
	}

	public void setInsuredCode(String insuredCode) {
		this.insuredCode = insuredCode;
	}

	@Column(name = "INSUREDNAME", length=120)
	public String getInsuredName() {
		return this.insuredName;
	}

	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}

	@Column(name = "INSUREDADDRESS")
	public String getInsuredAddress() {
		return this.insuredAddress;
	}

	public void setInsuredAddress(String insuredAddress) {
		this.insuredAddress = insuredAddress;
	}

	@Column(name = "INSUREDNATURE", length=12)
	public String getInsuredNature() {
		return this.insuredNature;
	}

	public void setInsuredNature(String insuredNature) {
		this.insuredNature = insuredNature;
	}

	@Column(name = "INSUREDFLAG", length=1)
	public String getInsuredFlag() {
		return this.insuredFlag;
	}

	public void setInsuredFlag(String insuredFlag) {
		this.insuredFlag = insuredFlag;
	}

	@Column(name = "INSUREDIDENTITY", length=2)
	public String getInsuredIdentity() {
		return this.insuredIdentity;
	}

	public void setInsuredIdentity(String insuredIdentity) {
		this.insuredIdentity = insuredIdentity;
	}

	@Column(name = "RELATESERIALNO", precision=15, scale=0)
	public Long getRelateSerialno() {
		return this.relateSerialno;
	}

	public void setRelateSerialno(Long relateSerialno) {
		this.relateSerialno = relateSerialno;
	}

	@Column(name = "IDENTIFYTYPE", length=2)
	public String getIdentifyType() {
		return this.identifyType;
	}

	public void setIdentifyType(String identifyType) {
		this.identifyType = identifyType;
	}

	@Column(name = "IDENTIFYNUMBER", length=20)
	public String getIdentifyNumber() {
		return this.identifyNumber;
	}

	public void setIdentifyNumber(String identifyNumber) {
		this.identifyNumber = identifyNumber;
	}

	@Column(name = "SEX", length=1)
	public String getSex() {
		return this.sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	@Column(name = "AGE", precision=15, scale=0)
	public Long getAge() {
		return this.age;
	}

	public void setAge(Long age) {
		this.age = age;
	}

	@Column(name = "CREDITLEVEL", length=1)
	public String getCreditLevel() {
		return this.creditLevel;
	}

	public void setCreditLevel(String creditLevel) {
		this.creditLevel = creditLevel;
	}

	@Column(name = "POSSESSNATURE", length=15)
	public String getPossessNature() {
		return this.possessNature;
	}

	public void setPossessNature(String possessNature) {
		this.possessNature = possessNature;
	}

	@Column(name = "BUSINESSSOURCE", length=5)
	public String getBusinessSource() {
		return this.businessSource;
	}

	public void setBusinessSource(String businessSource) {
		this.businessSource = businessSource;
	}

	@Column(name = "BUSINESSSORT", length=4)
	public String getBusinessSort() {
		return this.businessSort;
	}

	public void setBusinessSort(String businessSort) {
		this.businessSort = businessSort;
	}

	@Column(name = "OCCUPATIONCODE", length=7)
	public String getOccupationCode() {
		return this.occupationCode;
	}

	public void setOccupationCode(String occupationCode) {
		this.occupationCode = occupationCode;
	}

	@Column(name = "EDUCATIONCODE", length=4)
	public String getEducationCode() {
		return this.educationCode;
	}

	public void setEducationCode(String educationCode) {
		this.educationCode = educationCode;
	}

	@Column(name = "BANK", length=40)
	public String getBank() {
		return this.bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	@Column(name = "ACCOUNTNAME", length=60)
	public String getAccountName() {
		return this.accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	@Column(name = "ACCOUNT", length=20)
	public String getAccount() {
		return this.account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	@Column(name = "LINKERNAME", length=120)
	public String getLinkerName() {
		return this.linkerName;
	}

	public void setLinkerName(String linkerName) {
		this.linkerName = linkerName;
	}

	@Column(name = "POSTADDRESS")
	public String getPostAddress() {
		return this.postAddress;
	}

	public void setPostAddress(String postAddress) {
		this.postAddress = postAddress;
	}

	@Column(name = "POSTCODE", length=6)
	public String getPostCode() {
		return this.postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	@Column(name = "PHONENUMBER", length=30)
	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@Column(name = "MOBILE", length=15)
	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Column(name = "EMAIL", length=60)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "BENEFITRATE", precision=5)
	public BigDecimal getBenefitRate() {
		return this.benefitRate;
	}

	public void setBenefitRate(BigDecimal benefitRate) {
		this.benefitRate = benefitRate;
	}

	@Column(name = "BENEFITFLAG", length=1)
	public String getBenefitFlag() {
		return this.benefitFlag;
	}

	public void setBenefitFlag(String benefitFlag) {
		this.benefitFlag = benefitFlag;
	}

	@Column(name = "OCCUPATIONGRADE", length=1)
	public String getOccupationGrade() {
		return this.occupationGrade;
	}

	public void setOccupationGrade(String occupationGrade) {
		this.occupationGrade = occupationGrade;
	}

	@Column(name = "VALIDFLAG", length=1)
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

	@Column(name = "REMARK", length=200)
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
}