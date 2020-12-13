package ins.sino.claimcar.other.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class PrpLCheckmAuditVoBase implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private BigDecimal id;
	private String registNo;
	private Long backAccountId;
	private Long oldBankId;
	private String checkCode;
	private String accountName;
	private String accountCode;
	private String certifyNo;
	private String mobile;
	private String publicAndprivate;
	private String city;
	private String bankName;
	private String bankoutLets;
	private String bankNumber;
	private String status;
	private String remark;
	private Date createTime;
	private String createUser;
	private String auditOpinion;
	private String comCode;
	private String isautoAudit;
	private String bussNo;
	private Date updateTime;
	private String updateUser;
	private String isAutoPay;
	public BigDecimal getId() {
		return id;
	}
	public void setId(BigDecimal id) {
		this.id = id;
	}
	public String getRegistNo() {
		return registNo;
	}
	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}
	public Long getBackAccountId() {
		return backAccountId;
	}
	public void setBackAccountId(Long backAccountId) {
		this.backAccountId = backAccountId;
	}
	public Long getOldBankId() {
		return oldBankId;
	}
	public void setOldBankId(Long oldBankId) {
		this.oldBankId = oldBankId;
	}
	public String getCheckCode() {
		return checkCode;
	}
	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getAccountCode() {
		return accountCode;
	}
	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}
	public String getCertifyNo() {
		return certifyNo;
	}
	public void setCertifyNo(String certifyNo) {
		this.certifyNo = certifyNo;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getPublicAndprivate() {
		return publicAndprivate;
	}
	public void setPublicAndprivate(String publicAndprivate) {
		this.publicAndprivate = publicAndprivate;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getBankoutLets() {
		return bankoutLets;
	}
	public void setBankoutLets(String bankoutLets) {
		this.bankoutLets = bankoutLets;
	}
	public String getBankNumber() {
		return bankNumber;
	}
	public void setBankNumber(String bankNumber) {
		this.bankNumber = bankNumber;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public String getAuditOpinion() {
		return auditOpinion;
	}
	public void setAuditOpinion(String auditOpinion) {
		this.auditOpinion = auditOpinion;
	}
	public String getComCode() {
		return comCode;
	}
	public void setComCode(String comCode) {
		this.comCode = comCode;
	}
	public String getIsautoAudit() {
		return isautoAudit;
	}
	public void setIsautoAudit(String isautoAudit) {
		this.isautoAudit = isautoAudit;
	}
	public String getBussNo() {
		return bussNo;
	}
	public void setBussNo(String bussNo) {
		this.bussNo = bussNo;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	public String getIsAutoPay() {
		return isAutoPay;
	}
	public void setIsAutoPay(String isAutoPay) {
		this.isAutoPay = isAutoPay;
	}
     
}
