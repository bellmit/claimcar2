package ins.sino.claimcar.interf.vo;

import ins.platform.utils.xstreamconverters.SinosoftDateConverter;

import java.io.Serializable;
import java.util.Date;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

@XStreamAlias("accrollbackaccountMain")
public class AccRollBackVo implements Serializable {

	private static final long serialVersionUID = 1L;
	@XStreamAlias("CertiNo")
	private String certiNo;// 业务号（计算书号、预赔款）
	@XStreamAlias("paytype")
	private String payType;//收付原因
	@XStreamAlias("ErrorMessage")
	private String errorMessage;//退票原因
	@XStreamAlias("AccountId")
	private String accountId;//核心账户ID
	@XStreamAlias("NameOfBank")
	private String nameOfBank;//开户行名称
	@XStreamAlias("AccountName")
	private String accountName;//账户名称
	@XStreamAlias("BankCode")
	private String bankCode;//银行代码
	@XStreamAlias("Currency")
	private String currency;//账户币种
	@XStreamAlias("AccType")
	private String accType;//账户类型 
	@XStreamAlias("ActType")
	private String actType;//账户性质 
	@XStreamAlias("Provincial")
	private String provincial;//省
	@XStreamAlias("City")
	private String city;//县
	@XStreamAlias("RollBackTime")
	private String rollBackTime;//退票时间
	@XStreamAlias("Status")
	private String status;//状态码
	@XStreamAlias("RollbackCode")
	private String rollbackCode;//申请人
	@XStreamAlias("ErrorType")
	private String errorType;//错误类型
	@XStreamAlias("ModifyType")
	private String modifyType;//修改类型
	@XStreamAlias("AccountCode")
	private String accountCode;//银行账号
	@XStreamAlias("IsAutoPay")
	private String isAutoPay;//是否送资金，0-否，1-是
	
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
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
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
	public String getRollBackTime() {
		return rollBackTime;
	}
	public void setRollBackTime(String rollBackTime) {
		this.rollBackTime = rollBackTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCertiNo() {
		return certiNo;
	}
	public void setCertiNo(String certiNo) {
		this.certiNo = certiNo;
	}
	public String getRollbackCode() {
		return rollbackCode;
	}
	public void setRollbackCode(String rollbackCode) {
		this.rollbackCode = rollbackCode;
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
	public String getAccountCode() {
		return accountCode;
	}
	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}
	public String getIsAutoPay() {
		return isAutoPay;
	}
	public void setIsAutoPay(String isAutoPay) {
		this.isAutoPay = isAutoPay;
	}
}
