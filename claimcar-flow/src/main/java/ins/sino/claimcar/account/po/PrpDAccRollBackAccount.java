package ins.sino.claimcar.account.po;

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
@TableGenerator(name = "utiSequence", table = "UTISEQUENCE", pkColumnName = "GenName", valueColumnName = "GenValue", pkColumnValue = "PRPDACCROLLBACKACCOUNT_PK", allocationSize = 10)
@Table(name = "PRPDACCROLLBACKACCOUNT")
public class PrpDAccRollBackAccount implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String certiNo;
	private String payType;
	private String errorMessage;
	private String accountId;
	private String nameOfBank;
	private String accountName;
	private String bankCode;
	private String currency;
	private String accType;
	private String actType;
	private String provincial;
	private String city;
	private Date rollBackTime;
	private String status;
	private String rollbackCode;
	private String oldAccountId;
	private String chargeCode;

	private String accountCode;//银行账号
	private String errorType;//错误类型
	private String modifyType;//修改类型
	private String isAutoPay;//是否送资金，0-否，1-是
	
	private Long auditId;//公估费退票审核表对应的Id
	private String isHaveAudit;//是否有在公估退票审核中被退回过，3-有过，其它则没有
    private String auditFlag;
    private String infoFlag;
	/** 支付对象序号同payment.serialNo */
    private String serialNo;
	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "utiSequence") 
	@Column(name = "ID", unique = true, nullable = false, precision=13, scale=0)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Column(name = "CERTINO", length=25)
	public String getCertiNo() {
		return certiNo;
	}
	public void setCertiNo(String certiNo) {
		this.certiNo = certiNo;
	}
	@Column(name = "PAYTYPE", length=25)
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	@Column(name = "ERRORMESSAGE", length=500)
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	@Column(name = "ACCOUNTID", length=30)
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	@Column(name = "NAMEOFBANK", length=50)
	public String getNameOfBank() {
		return nameOfBank;
	}
	public void setNameOfBank(String nameOfBank) {
		this.nameOfBank = nameOfBank;
	}
	@Column(name = "ACCOUNTNAME", length=50)
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	@Column(name = "BANKCODE", length=20)
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	@Column(name = "CURRENCY", length=10)
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	@Column(name = "ACCTYPE", length=5)
	public String getAccType() {
		return accType;
	}
	public void setAccType(String accType) {
		this.accType = accType;
	}
	@Column(name = "ACTTYPE", length=5)
	public String getActType() {
		return actType;
	}
	public void setActType(String actType) {
		this.actType = actType;
	}
	@Column(name = "PROVINCIAL", length=10)
	public String getProvincial() {
		return provincial;
	}
	public void setProvincial(String provincial) {
		this.provincial = provincial;
	}
	@Column(name = "CITY", length=10)
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ROLLBACKTIME", length=7)
	public Date getRollBackTime() {
		return rollBackTime;
	}
	public void setRollBackTime(Date rollBackTime) {
		this.rollBackTime = rollBackTime;
	}
	@Column(name = "STATUS", length=5)
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Column(name = "ROLLBACKCODE", length=30)
	public String getRollbackCode() {
		return rollbackCode;
	}
	public void setRollbackCode(String rollbackCode) {
		this.rollbackCode = rollbackCode;
	}
	
	@Column(name = "OLDACCOUNTID", length=22)
	public String getOldAccountId() {
		return oldAccountId;
	}
	public void setOldAccountId(String oldAccountId) {
		this.oldAccountId = oldAccountId;
	}
	
	@Column(name = "CHARGECODE", nullable = false, length=3)
	public String getChargeCode() {
		return this.chargeCode;
	}

	public void setChargeCode(String chargeCode) {
		this.chargeCode = chargeCode;
	}


	
	@Column(name = "ACCOUNTCODE", length=35)
	public String getAccountCode() {
		return accountCode;
	}
	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}
	
	@Column(name = "ERRORTYPE", length=5)
	public String getErrorType() {
		return errorType;
	}
	public void setErrorType(String errorType) {
		this.errorType = errorType;
	}
	
	@Column(name = "MODIFYTYPE", length=5)
	public String getModifyType() {
		return modifyType;
	}
	public void setModifyType(String modifyType) {
		this.modifyType = modifyType;
	}
	
	@Column(name = "ISAUTOPAY", length=5)
	public String getIsAutoPay() {
		return isAutoPay;
	}
	public void setIsAutoPay(String isAutoPay) {
		this.isAutoPay = isAutoPay;
	}
	@Column(name = "AUDITID", length=10)
	public Long getAuditId() {
		return auditId;
	}
	public void setAuditId(Long auditId) {
		this.auditId = auditId;
	}
	@Column(name = "ISHAVEAUDIT", length=12)
	public String getIsHaveAudit() {
		return isHaveAudit;
	}
	
	public void setIsHaveAudit(String isHaveAudit) {
		this.isHaveAudit = isHaveAudit;
	}
	
	@Column(name = "AUDITFLAG", length=12)
	public String getAuditFlag() {
		return auditFlag;
	}
	public void setAuditFlag(String auditFlag) {
		this.auditFlag = auditFlag;
	}
	@Column(name = "INFOFLAG", length=12)
	public String getInfoFlag() {
		return infoFlag;
	}
	public void setInfoFlag(String infoFlag) {
		this.infoFlag = infoFlag;
	}
	@Column(name = "SERIALNO", length=4)
	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
}
