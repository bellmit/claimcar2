package ins.sino.claimcar.lossperson.po;

// Generated by Hibernate Tools 3.2.5 (sinosoft version), Don't modify!
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * POJO Class PrpLDlossPersInjured
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "PRPLDLOSSPERSINJURED")
public class PrpLDlossPersInjured implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private PrpLDlossPersTrace prpLDlossPersTrace;
	private String registNo;
	private Long thirdPartyId;
	private String riskCode;
	private String treatSituation;
	private String personName;
	private Long personId;
	private String certiType;
	private String certiCode;
	private String lossItemType;
	private String phoneNumber;
	private BigDecimal personAge;
	private String personSex;
	private String ticCode;
	private String ticName;
	private String income;
	private String domicile;
	private String woundCode;
	private String licenseNo;
	private String injuryPart;
	private Date inputTime;
	private String remark;
	private String flag;
	private String validFlag;
	private String createUser;
	private Date createTime;
	private String updateUser;
	private Date updateTime;
	private Date deathTime;
	private String chkComCode;
	private String chkComName;
	private Integer serialNo;
	private String appraisaCity;
	private String idClmChannelProcess;//???????????????

	private List<PrpLDlossPersExt> prpLDlossPersExts = new ArrayList<PrpLDlossPersExt>(0);
	private List<PrpLDlossPersNurse> prpLDlossPersNurses = new ArrayList<PrpLDlossPersNurse>(0);
	private List<PrpLDlossPersHospital> prpLDlossPersHospitals = new ArrayList<PrpLDlossPersHospital>(0);
	private List<PrpLDlossPersRaise> prpLDlossPersRaises = new ArrayList<PrpLDlossPersRaise>(0);

	@GenericGenerator(name="generator", strategy="foreign", parameters=@Parameter(name="property", value="prpLDlossPersTrace"))@Id @GeneratedValue(generator="generator")
	@Column(name = "ID", unique = true, nullable = false, precision=14, scale=0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@OneToOne(cascade=CascadeType.ALL,fetch=FetchType.LAZY)@PrimaryKeyJoinColumn
	public PrpLDlossPersTrace getPrpLDlossPersTrace() {
		return this.prpLDlossPersTrace;
	}

	public void setPrpLDlossPersTrace(PrpLDlossPersTrace prpLDlossPersTrace) {
		this.prpLDlossPersTrace = prpLDlossPersTrace;
	}

	@Column(name = "REGISTNO", nullable = false, length=30)
	public String getRegistNo() {
		return this.registNo;
	}

	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}

	@Column(name = "THIRDPARTYID", precision=14, scale=0)
	public Long getThirdPartyId() {
		return this.thirdPartyId;
	}

	public void setThirdPartyId(Long thirdPartyId) {
		this.thirdPartyId = thirdPartyId;
	}

	@Column(name = "RISKCODE", length=4)
	public String getRiskCode() {
		return this.riskCode;
	}

	public void setRiskCode(String riskCode) {
		this.riskCode = riskCode;
	}

	@Column(name = "TREATSITUATION", length=1)
	public String getTreatSituation() {
		return this.treatSituation;
	}

	public void setTreatSituation(String treatSituation) {
		this.treatSituation = treatSituation;
	}

	@Column(name = "PERSONNAME", length=320)
	public String getPersonName() {
		return this.personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	@Column(name = "PERSONID", precision=14, scale=0)
	public Long getPersonId() {
		return this.personId;
	}

	public void setPersonId(Long personId) {
		this.personId = personId;
	}

	@Column(name = "CERTITYPE", length=2)
	public String getCertiType() {
		return this.certiType;
	}

	public void setCertiType(String certiType) {
		this.certiType = certiType;
	}

	@Column(name = "CERTICODE", length=20)
	public String getCertiCode() {
		return this.certiCode;
	}

	public void setCertiCode(String certiCode) {
		this.certiCode = certiCode;
	}

	@Column(name = "LOSSITEMTYPE", length=3)
	public String getLossItemType() {
		return this.lossItemType;
	}

	public void setLossItemType(String lossItemType) {
		this.lossItemType = lossItemType;
	}

	@Column(name = "PHONENUMBER", length=30)
	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@Column(name = "PERSONAGE", precision=38, scale=0)
	public BigDecimal getPersonAge() {
		return this.personAge;
	}

	public void setPersonAge(BigDecimal personAge) {
		this.personAge = personAge;
	}

	@Column(name = "PERSONSEX", length=1)
	public String getPersonSex() {
		return this.personSex;
	}

	public void setPersonSex(String personSex) {
		this.personSex = personSex;
	}

	@Column(name = "TICCODE", length=10)
	public String getTicCode() {
		return this.ticCode;
	}

	public void setTicCode(String ticCode) {
		this.ticCode = ticCode;
	}

	@Column(name = "TICNAME", length=100)
	public String getTicName() {
		return this.ticName;
	}

	public void setTicName(String ticName) {
		this.ticName = ticName;
	}

	@Column(name = "INCOME", length=2)
	public String getIncome() {
		return this.income;
	}

	public void setIncome(String income) {
		this.income = income;
	}

	@Column(name = "DOMICILE", length=10)
	public String getDomicile() {
		return this.domicile;
	}

	public void setDomicile(String domicile) {
		this.domicile = domicile;
	}

	@Column(name = "WOUNDCODE", length=10)
	public String getWoundCode() {
		return this.woundCode;
	}

	public void setWoundCode(String woundCode) {
		this.woundCode = woundCode;
	}

	@Column(name = "LICENSENO", length=20)
	public String getLicenseNo() {
		return this.licenseNo;
	}

	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}

	@Column(name = "INJURYPART", length=20)
	public String getInjuryPart() {
		return this.injuryPart;
	}

	public void setInjuryPart(String injuryPart) {
		this.injuryPart = injuryPart;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "INPUTTIME", length=7)
	public Date getInputTime() {
		return this.inputTime;
	}

	public void setInputTime(Date inputTime) {
		this.inputTime = inputTime;
	}

	@Column(name = "REMARK", length=1024)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "FLAG", length=10)
	public String getFlag() {
		return this.flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	@Column(name = "VALIDFLAG", length=1)
	public String getValidFlag() {
		return this.validFlag;
	}

	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
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

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DEATHTIME", length=7)
	public Date getDeathTime() {
		return this.deathTime;
	}

	public void setDeathTime(Date deathTime) {
		this.deathTime = deathTime;
	}

	@Column(name = "CHKCOMCODE", length=20)
	public String getChkComCode() {
		return this.chkComCode;
	}

	public void setChkComCode(String chkComCode) {
		this.chkComCode = chkComCode;
	}

	@Column(name = "CHKCOMNAME", length=30)
	public String getChkComName() {
		return this.chkComName;
	}

	public void setChkComName(String chkComName) {
		this.chkComName = chkComName;
	}

	@Column(name = "SERIALNO", length=5)
	public Integer getSerialNo() {
		return serialNo;
	}
	
	public void setSerialNo(Integer serialNo) {
		this.serialNo = serialNo;
	}
	
	@Column(name = "APPRAISACITY", length=15)
	public String getAppraisaCity() {
		return appraisaCity;
	}

	public void setAppraisaCity(String appraisaCity) {
		this.appraisaCity = appraisaCity;
	}

	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="prpLDlossPersInjured")
	@OrderBy(value="id")
	public List<PrpLDlossPersExt> getPrpLDlossPersExts() {
		return this.prpLDlossPersExts;
	}

	public void setPrpLDlossPersExts(List<PrpLDlossPersExt> prpLDlossPersExts) {
		this.prpLDlossPersExts = prpLDlossPersExts;
	}

    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="prpLDlossPersInjured")
    @OrderBy(value="id")
	public List<PrpLDlossPersNurse> getPrpLDlossPersNurses() {
		return this.prpLDlossPersNurses;
	}

	public void setPrpLDlossPersNurses(List<PrpLDlossPersNurse> prpLDlossPersNurses) {
		this.prpLDlossPersNurses = prpLDlossPersNurses;
	}

    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="prpLDlossPersInjured")
    @OrderBy(value="id")
	public List<PrpLDlossPersHospital> getPrpLDlossPersHospitals() {
		return this.prpLDlossPersHospitals;
	}

	public void setPrpLDlossPersHospitals(List<PrpLDlossPersHospital> prpLDlossPersHospitals) {
		this.prpLDlossPersHospitals = prpLDlossPersHospitals;
	}

    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="prpLDlossPersInjured")
    @OrderBy(value="id")
	public List<PrpLDlossPersRaise> getPrpLDlossPersRaises() {
		return this.prpLDlossPersRaises;
	}

	public void setPrpLDlossPersRaises(List<PrpLDlossPersRaise> prpLDlossPersRaises) {
		this.prpLDlossPersRaises = prpLDlossPersRaises;
	}
	@Column(name = "IDCLMCHANNELPROCESS", length=32)
	public String getIdClmChannelProcess() {
		return idClmChannelProcess;
	}

	public void setIdClmChannelProcess(String idClmChannelProcess) {
		this.idClmChannelProcess = idClmChannelProcess;
	}
}
