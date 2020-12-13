package com.sinosoft.sff.interf;

import java.io.Serializable;
import java.util.Date;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * �����VO
 * 
 * @author ��<a href="mailto:huangyi@sinosoft.com.cn">HuangYi</a>
 * @Company www.sinosoft.com.cn
 * @Copyright Copyright (c) 2016��7��6��
 * @since (2016��7��6�� ����8:11:16): <br>
 */
@XStreamAlias("AccRecAccountVo")
public class AccRecAccountVo implements Serializable {

	private static final long serialVersionUID = 1L;
	@XStreamAlias("AccountCode")
	private String AccountCode;// �����˺�
	@XStreamAlias("NameOfBank")
	private String NameOfBank;// ���������
	@XStreamAlias("AccountName")
	private String AccountName;//�˻����
	@XStreamAlias("BankCode")
	private String BankCode;// ���д���
	@XStreamAlias("Currency")
	private String Currency;//�˻�����
	@XStreamAlias("AccType")
	private String AccType;// �˻����� 
	@XStreamAlias("ActType")
	private String ActType;// �˻�����  
	@XStreamAlias("Provincial")
	private String Provincial;// ʡ
	@XStreamAlias("City")
	private String City;// ��
	@XStreamAlias("Telephone")
	private String Telephone;// ��ϵ�绰
	@XStreamAlias("ClientType")
	private String ClientType;// �ͻ�����
	@XStreamAlias("ClientNo")
	private String ClientNo;// �ͻ����� 
	@XStreamAlias("ClientName")
	private String ClientName;// �ͻ���� 
	@XStreamAlias("AccountType")
	private String AccountType;// �˺�����
	@XStreamAlias("DefaultFlag")
	private String DefaultFlag;// Ĭ���˺ű�־ 
	@XStreamAlias("CreateCode")
	private String CreateCode;// ������
	@XStreamAlias("CreateBranch")
	private String CreateBranch;// ������ 
	@XStreamAlias("CreateDate")
	private Date CreateDate;// ��������
	@XStreamAlias("ValidStatus")
	private String ValidStatus;// ���ñ�־
	@XStreamAlias("Flag")
	private String Flag;// ����flag
	@XStreamAlias("Remark")
	private String Remark;// ����Remark
	@XStreamAlias("IdentifyType")
	private String IdentifyType;// ֤������ 
	@XStreamAlias("IdentifyNumber")
	private String IdentifyNumber;// ֤������ 
	@XStreamAlias("AccountNo")
	private String AccountNo;//旧AccountNo
	@XStreamAlias("IsFastReparation")
	private String IsFastReparation;//是否快赔（0：否1：是）
	@XStreamAlias("Abstractcontent")
	private String abstractcontent;//摘要
	@XStreamAlias("IsAutoPay")
	private String isAutoPay;//是否送资金，0-否，1-是
	
	@XStreamAlias("PublicPrivateFlag")
	private String publicPrivateFlag;//是否对公对私
	
	public String getAccountCode() {
		return AccountCode;
	}
	public void setAccountCode(String accountCode) {
		AccountCode = accountCode;
	}
	public String getNameOfBank() {
		return NameOfBank;
	}
	public void setNameOfBank(String nameOfBank) {
		NameOfBank = nameOfBank;
	}
	public String getAccountName() {
		return AccountName;
	}
	public void setAccountName(String accountName) {
		AccountName = accountName;
	}
	public String getBankCode() {
		return BankCode;
	}
	public void setBankCode(String bankCode) {
		BankCode = bankCode;
	}
	public String getCurrency() {
		return Currency;
	}
	public void setCurrency(String currency) {
		Currency = currency;
	}
	public String getAccType() {
		return AccType;
	}
	public void setAccType(String accType) {
		AccType = accType;
	}
	public String getActType() {
		return ActType;
	}
	public void setActType(String actType) {
		ActType = actType;
	}
	public String getProvincial() {
		return Provincial;
	}
	public void setProvincial(String provincial) {
		Provincial = provincial;
	}
	public String getCity() {
		return City;
	}
	public void setCity(String city) {
		City = city;
	}
	public String getTelephone() {
		return Telephone;
	}
	public void setTelephone(String telephone) {
		Telephone = telephone;
	}
	public String getClientType() {
		return ClientType;
	}
	public void setClientType(String clientType) {
		ClientType = clientType;
	}
	public String getClientNo() {
		return ClientNo;
	}
	public void setClientNo(String clientNo) {
		ClientNo = clientNo;
	}
	public String getClientName() {
		return ClientName;
	}
	public void setClientName(String clientName) {
		ClientName = clientName;
	}
	public String getAccountType() {
		return AccountType;
	}
	public void setAccountType(String accountType) {
		AccountType = accountType;
	}
	public String getDefaultFlag() {
		return DefaultFlag;
	}
	public void setDefaultFlag(String defaultFlag) {
		DefaultFlag = defaultFlag;
	}
	public String getCreateCode() {
		return CreateCode;
	}
	public void setCreateCode(String createCode) {
		CreateCode = createCode;
	}
	public String getCreateBranch() {
		return CreateBranch;
	}
	public void setCreateBranch(String createBranch) {
		CreateBranch = createBranch;
	}
	public Date getCreateDate() {
		return CreateDate;
	}
	public void setCreateDate(Date date) {
		CreateDate = date;
	}
	public String getValidStatus() {
		return ValidStatus;
	}
	public void setValidStatus(String validStatus) {
		ValidStatus = validStatus;
	}
	public String getFlag() {
		return Flag;
	}
	public void setFlag(String flag) {
		Flag = flag;
	}
	public String getRemark() {
		return Remark;
	}
	public void setRemark(String remark) {
		Remark = remark;
	}
	public String getIdentifyType() {
		return IdentifyType;
	}
	public void setIdentifyType(String identifyType) {
		IdentifyType = identifyType;
	}
	public String getIdentifyNumber() {
		return IdentifyNumber;
	}
	public void setIdentifyNumber(String identifyNumber) {
		IdentifyNumber = identifyNumber;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getAccountNo() {
		return AccountNo;
	}
	public void setAccountNo(String accountNo) {
		AccountNo = accountNo;
	}
	public String getIsFastReparation() {
		return IsFastReparation;
	}
	public void setIsFastReparation(String isFastReparation) {
		IsFastReparation = isFastReparation;
	}
	public String getAbstractcontent() {
		return abstractcontent;
	}
	public void setAbstractcontent(String abstractcontent) {
		this.abstractcontent = abstractcontent;
	}
	public String getIsAutoPay() {
		return isAutoPay;
	}
	public void setIsAutoPay(String isAutoPay) {
		this.isAutoPay = isAutoPay;
	}
	public String getPublicPrivateFlag() {
		return publicPrivateFlag;
	}
	public void setPublicPrivateFlag(String publicPrivateFlag) {
		this.publicPrivateFlag = publicPrivateFlag;
	}
	
	
}
	