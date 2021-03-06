package ins.sino.claimcar.padpay.po;

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
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * POJO Class PrpLPadPayPerson
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@TableGenerator(name = "utiSequence", table = "UTISEQUENCE", pkColumnName = "GenName", valueColumnName = "GenValue", pkColumnValue = "PRPLPADPAYPERSON_PK", allocationSize = 10)
@Table(name = "PRPLPADPAYPERSON")
public class PrpLPadPayPerson implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private PrpLPadPayMain prpLPadPayMain;
	private String personName;
	private Integer personAge;
	private String personSex;
	private String personType;
	private String injuryType;
	private String personAddress;
	private String licenseNo;
	private String hospitalProvince;
	private String hospitalCity;
	private String hospitalCode;
	private String hospitalName;
	private String personIdfNo;
	private String feeNameCode;
	private String feeUpperCode;
	private BigDecimal costSum;
	private String riskCode;
	private String payeeName;
	private String otherFlag;
	private String otherCause;
	private String accountNo;
	private String bankName;
	private String flag;
	private String remark;
	private Long payeeId;
	private String payStatus;
	private String sendPayStatus;
	private String serialNo;
	private Date payTime;
	private String settleNo;
	private String summary;
	private String invoiceType;
	private String addTaxRate;
	private String addTaxValue;
	private String noTaxValue;
	/** ???????????????????????? */
	private String idClmPaymentResult;
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "utiSequence")
	@Column(name = "ID", unique = true, nullable = false, precision = 13, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PADPAYID")
	public PrpLPadPayMain getPrpLPadPayMain() {
		return this.prpLPadPayMain;
	}

	public void setPrpLPadPayMain(PrpLPadPayMain prpLPadPayMain) {
		this.prpLPadPayMain = prpLPadPayMain;
	}

	@Column(name = "PERSONNAME", length = 20)
	public String getPersonName() {
		return this.personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	@Column(name = "PERSONAGE", precision = 5, scale = 0)
	public Integer getPersonAge() {
		return this.personAge;
	}

	public void setPersonAge(Integer personAge) {
		this.personAge = personAge;
	}

	@Column(name = "PERSONSEX", length = 3)
	public String getPersonSex() {
		return this.personSex;
	}

	public void setPersonSex(String personSex) {
		this.personSex = personSex;
	}

	@Column(name = "PERSONTYPE", length = 15)
	public String getPersonType() {
		return this.personType;
	}

	public void setPersonType(String personType) {
		this.personType = personType;
	}

	@Column(name = "INJURYTYPE", length = 2)
	public String getInjuryType() {
		return this.injuryType;
	}

	public void setInjuryType(String injuryType) {
		this.injuryType = injuryType;
	}

	@Column(name = "PERSONADDRESS", length = 100)
	public String getPersonAddress() {
		return this.personAddress;
	}

	public void setPersonAddress(String personAddress) {
		this.personAddress = personAddress;
	}

	@Column(name = "LICENSENO", length = 15)
	public String getLicenseNo() {
		return this.licenseNo;
	}

	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}

	@Column(name = "HOSPITALPROVINCE", length = 10)
	public String getHospitalProvince() {
		return this.hospitalProvince;
	}

	public void setHospitalProvince(String hospitalProvince) {
		this.hospitalProvince = hospitalProvince;
	}

	@Column(name = "HOSPITALCITY", length = 10)
	public String getHospitalCity() {
		return this.hospitalCity;
	}

	public void setHospitalCity(String hospitalCity) {
		this.hospitalCity = hospitalCity;
	}

	@Column(name = "HOSPITALCODE", length = 10)
	public String getHospitalCode() {
		return this.hospitalCode;
	}

	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}

	@Column(name = "HOSPITALNAME", length = 120)
	public String getHospitalName() {
		return this.hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	@Column(name = "PERSONIDFNO", length = 30)
	public String getPersonIdfNo() {
		return this.personIdfNo;
	}

	public void setPersonIdfNo(String personIdfNo) {
		this.personIdfNo = personIdfNo;
	}

	@Column(name = "COSTSUM", precision = 14)
	public BigDecimal getCostSum() {
		return costSum;
	}

	public void setCostSum(BigDecimal costSum) {
		this.costSum = costSum;
	}

	@Column(name = "RISKCODE", length = 5)
	public String getRiskCode() {
		return this.riskCode;
	}

	public void setRiskCode(String riskCode) {
		this.riskCode = riskCode;
	}

	@Column(name = "PAYEENAME", length = 20)
	public String getPayeeName() {
		return this.payeeName;
	}

	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}

	@Column(name = "OTHERFLAG", length = 2)
	public String getOtherFlag() {
		return this.otherFlag;
	}

	public void setOtherFlag(String otherFlag) {
		this.otherFlag = otherFlag;
	}

	@Column(name = "OTHERCAUSE", length = 5)
	public String getOtherCause() {
		return this.otherCause;
	}

	public void setOtherCause(String otherCause) {
		this.otherCause = otherCause;
	}

	@Column(name = "FLAG", length = 10)
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

	@Column(name = "ACCOUNTNO", length = 50)
	public String getAccountNo() {
		return this.accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	@Column(name = "BANKNAME", length = 50)
	public String getBankName() {
		return this.bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	@Column(name = "FEENAMECODE", length = 10)
	public String getFeeNameCode() {
		return this.feeNameCode;
	}

	public void setFeeNameCode(String feeNameCode) {
		this.feeNameCode = feeNameCode;
	}

	@Column(name = "FEEUPPERCODE", length = 10)
	public String getFeeUpperCode() {
		return this.feeUpperCode;
	}

	public void setFeeUpperCode(String feeUpperCode) {
		this.feeUpperCode = feeUpperCode;
	}

	@Column(name = "PAYEEID", precision = 13, scale = 0)
	public Long getPayeeId() {
		return this.payeeId;
	}

	public void setPayeeId(Long payeeId) {
		this.payeeId = payeeId;
	}

	@Column(name = "PAYSTATUS", length = 3)
	public String getPayStatus() {
		return this.payStatus;
	}

	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}

	@Column(name = "SENDPAYSTATUS", length = 3)
	public String getSendPayStatus() {
		return this.sendPayStatus;
	}

	public void setSendPayStatus(String sendPayStatus) {
		this.sendPayStatus = sendPayStatus;
	}
	
	/**
	 * @return ?????? serialNo???
	 */
	@Column(name = "SERIALNO", length = 5)
	public String getSerialNo() {
		return this.serialNo;
	}

	/**
	 * @param serialNo ???????????? serialNo???
	 */
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PAYTIME", length=7)
	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	@Column(name = "SETTLENO", length=50)
	public String getSettleNo() {
		return settleNo;
	}

	public void setSettleNo(String settleNo) {
		this.settleNo = settleNo;
	}

	@Column(name = "SUMMARY", length=3000)
	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}
	@Column(name = "INVOICETYPE", length=10)
	public String getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}
	@Column(name = "ADDTAXRATE", length=10)
	public String getAddTaxRate() {
		return addTaxRate;
	}

	public void setAddTaxRate(String addTaxRate) {
		this.addTaxRate = addTaxRate;
	}
	@Column(name = "ADDTAXVALUE", length=10)
	public String getAddTaxValue() {
		return addTaxValue;
	}

	public void setAddTaxValue(String addTaxValue) {
		this.addTaxValue = addTaxValue;
	}
	@Column(name = "NOTAXVALUE", length=10)
	public String getNoTaxValue() {
		return noTaxValue;
	}

	public void setNoTaxValue(String noTaxValue) {
		this.noTaxValue = noTaxValue;
	}
	@Column(name = "IDCLMPAYMENTRESULT", length=50)
	public String getIdClmPaymentResult() { return idClmPaymentResult; }

	public void setIdClmPaymentResult(String idClmPaymentResult) { this.idClmPaymentResult = idClmPaymentResult; }
}
