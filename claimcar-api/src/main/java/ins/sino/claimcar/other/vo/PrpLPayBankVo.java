package ins.sino.claimcar.other.vo;

import java.util.Date;

/**
 * Custom VO class of PO PrpLPayBank
 */ 
public class PrpLPayBankVo extends PrpLPayBankVoBase implements java.io.Serializable {
	private static final long serialVersionUID = 1L; 

	//业务号
	private String bussNo;
	//prpdaccrollbackaccount表Id
    private String backaccountId;
    private Date vClaimTime;
	private String appUser;
	private Date endCaseTime;
	private String insuredCode;
	private String settleNo;
	private String intermCode;//公估机构
	private String auditId;//公估费退票审核表Id
	
	private String checkCode; //查勘机构
	

	
	public String getCheckCode() {
		return checkCode;
	}

	
	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}

	public String getBussNo() {
		return bussNo;
	}

	public void setBussNo(String bussNo) {
		this.bussNo = bussNo;
	}

	public String getBackaccountId() {
		return backaccountId;
	}

	public void setBackaccountId(String backaccountId) {
		this.backaccountId = backaccountId;
	}

	
	
	public Date getvClaimTime() {
		return vClaimTime;
	}
	public void setvClaimTime(Date vClaimTime) {
		this.vClaimTime = vClaimTime;
	}
	public String getAppUser() {
		return appUser;
	}
	public void setAppUser(String appUser) {
		this.appUser = appUser;
	}
	public Date getEndCaseTime() {
		return endCaseTime;
	}
	public void setEndCaseTime(Date endCaseTime) {
		this.endCaseTime = endCaseTime;
	}
	public String getInsuredCode() {
		return insuredCode;
	}
	public void setInsuredCode(String insuredCode) {
		this.insuredCode = insuredCode;
	}
	public String getSettleNo() {
		return settleNo;
	}
	public void setSettleNo(String settleNo) {
		this.settleNo = settleNo;
	}


	public String getIntermCode() {
		return intermCode;
	}

	public void setIntermCode(String intermCode) {
		this.intermCode = intermCode;
	}


	public String getAuditId() {
		return auditId;
	}

	public void setAuditId(String auditId) {
		this.auditId = auditId;
	}
	
	


}
