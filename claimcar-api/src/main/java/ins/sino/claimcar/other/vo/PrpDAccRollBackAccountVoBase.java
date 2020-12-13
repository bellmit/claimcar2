package ins.sino.claimcar.other.vo;

import java.util.Date;

public class PrpDAccRollBackAccountVoBase implements java.io.Serializable {

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
	private String isVerify;
	private String verifyUser;
	private Date verifyDate;
	private String verifyStatus;
	private String verifyText;
	private String verifyHandle;
	private String oldAccountId;
	private String chargeCode;
	private String accountCode;//银行账号
	private String errorType;//错误类型
	private String modifyType;//修改类型
	private String isAutoPay;//是否送资金，0-否，1-是
	private Long auditId;//公估费退票审核表对应的Id
	private String isHaveAudit;//是否有在公估退票审核中被退回过，3-有过，其它则没有
    private String auditFlag;//区分公估费退票的数据来源
    private String InfoFlag;
	/** 支付对象序号同payment.serialNo */
	private String serialNo;

	protected PrpDAccRollBackAccountVoBase(){

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCertiNo() {
		return certiNo;
	}

	public void setCertiNo(String certiNo) {
		this.certiNo = certiNo;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getNameOfBank() {
		return nameOfBank;
	}

	public void setNameOfBank(String nameOfBank) {
		this.nameOfBank = nameOfBank;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getAccType() {
		return accType;
	}

	public void setAccType(String accType) {
		this.accType = accType;
	}

	public String getActType() {
		return actType;
	}

	public void setActType(String actType) {
		this.actType = actType;
	}

	public String getProvincial() {
		return provincial;
	}

	public void setProvincial(String provincial) {
		this.provincial = provincial;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Date getRollBackTime() {
		return rollBackTime;
	}

	public void setRollBackTime(Date rollBackTime) {
		this.rollBackTime = rollBackTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getRollbackCode() {
		return rollbackCode;
	}

	public void setRollbackCode(String rollbackCode) {
		this.rollbackCode = rollbackCode;
	}

	public String getIsVerify() {
		return isVerify;
	}

	public void setIsVerify(String isVerify) {
		this.isVerify = isVerify;
	}

	public String getVerifyUser() {
		return verifyUser;
	}

	public void setVerifyUser(String verifyUser) {
		this.verifyUser = verifyUser;
	}

	public Date getVerifyDate() {
		return verifyDate;
	}

	public void setVerifyDate(Date verifyDate) {
		this.verifyDate = verifyDate;
	}

	public String getVerifyStatus() {
		return verifyStatus;
	}

	public void setVerifyStatus(String verifyStatus) {
		this.verifyStatus = verifyStatus;
	}

	public String getVerifyText() {
		return verifyText;
	}

	public void setVerifyText(String verifyText) {
		this.verifyText = verifyText;
	}

	public String getVerifyHandle() {
		return verifyHandle;
	}

	public void setVerifyHandle(String verifyHandle) {
		this.verifyHandle = verifyHandle;
	}

	public String getOldAccountId() {
		return oldAccountId;
	}

	public void setOldAccountId(String oldAccountId) {
		this.oldAccountId = oldAccountId;
	}

	public String getChargeCode() {
		return chargeCode;
	}

	public void setChargeCode(String chargeCode) {
		this.chargeCode = chargeCode;
	}



	public String getAccountCode() {
		return accountCode;
	}

	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}

	public String getErrorType() {
		return errorType;
	}

	public void setErrorType(String errorType) {
		this.errorType = errorType;
	}

	public String getModifyType() {
		return modifyType;
	}

	public void setModifyType(String modifyType) {
		this.modifyType = modifyType;
	}

	public String getIsAutoPay() {
		return isAutoPay;
	}

	public void setIsAutoPay(String isAutoPay) {
		this.isAutoPay = isAutoPay;
	}

	public Long getAuditId() {
		return auditId;
	}

	public void setAuditId(Long auditId) {
		this.auditId = auditId;
	}

	public String getIsHaveAudit() {
		return isHaveAudit;
	}

	public void setIsHaveAudit(String isHaveAudit) {
		this.isHaveAudit = isHaveAudit;
	}

	public String getAuditFlag() {
		return auditFlag;
	}

	public void setAuditFlag(String auditFlag) {
		this.auditFlag = auditFlag;
	}

	public String getInfoFlag() {
		return InfoFlag;
	}

	public void setInfoFlag(String infoFlag) {
		InfoFlag = infoFlag;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
}
