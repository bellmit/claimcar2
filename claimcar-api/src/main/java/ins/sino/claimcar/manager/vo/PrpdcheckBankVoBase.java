package ins.sino.claimcar.manager.vo;

import java.io.Serializable;
import java.util.Date;

public class PrpdcheckBankVoBase implements Serializable{
	private static final long serialVersionUID = 1L;
	private Long id;
	private String accountName;
	private String accountNo;
	private String bankName;
	private String bankType;
	private String province;
	private String city;
	private String bankOutlets;
	private String bankNumber;
	private String mobile;
	private String updateUser;
	private Date createTime;
	private String createUser;
	private Date updateTime;
	private String certifyNo;
	private String accountId;
	private String vaildFlag;//该公估机构下的银行账号
	private String publicAndPrivate;//对公对私
	
	
	 protected PrpdcheckBankVoBase() {
			
	    }


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getAccountName() {
		return accountName;
	}


	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}


	public String getAccountNo() {
		return accountNo;
	}


	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}


	public String getBankName() {
		return bankName;
	}


	public void setBankName(String bankName) {
		this.bankName = bankName;
	}


	public String getBankType() {
		return bankType;
	}


	public void setBankType(String bankType) {
		this.bankType = bankType;
	}


	public String getProvince() {
		return province;
	}


	public void setProvince(String province) {
		this.province = province;
	}


	public String getCity() {
		return city;
	}


	public void setCity(String city) {
		this.city = city;
	}


	public String getBankOutlets() {
		return bankOutlets;
	}


	public void setBankOutlets(String bankOutlets) {
		this.bankOutlets = bankOutlets;
	}


	public String getBankNumber() {
		return bankNumber;
	}


	public void setBankNumber(String bankNumber) {
		this.bankNumber = bankNumber;
	}


	public String getMobile() {
		return mobile;
	}


	public void setMobile(String mobile) {
		this.mobile = mobile;
	}


	public String getUpdateUser() {
		return updateUser;
	}


	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
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


	public Date getUpdateTime() {
		return updateTime;
	}


	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}


	public String getCertifyNo() {
		return certifyNo;
	}


	public void setCertifyNo(String certifyNo) {
		this.certifyNo = certifyNo;
	}


	public String getAccountId() {
		return accountId;
	}


	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}


	public String getVaildFlag() {
		return vaildFlag;
	}


	public void setVaildFlag(String vaildFlag) {
		this.vaildFlag = vaildFlag;
	}


	public String getPublicAndPrivate() {
		return publicAndPrivate;
	}


	public void setPublicAndPrivate(String publicAndPrivate) {
		this.publicAndPrivate = publicAndPrivate;
	}
	 
	 
}
