package ins.sino.claim.powerGridCarClaimLog.vo;

import java.math.BigDecimal;
import java.util.Date;

public class GDPowerGridCarClaimVo {
	
	private String registNo;//报案号
	private String carNum;//车牌号
	private Date beThreatenedTime;//出险时间
	private String beThreatenedAddress;//出险地点
	private String beThreatenedReason;//出险原因
	private Date reportTime;//报案时间
	private String caseNumber;//立案号(理赔唯一案件号)
	private Date caseTime;//立案时间
	private Date closingTime;//结案时间
	private String endCaseNo;//结案号
	private String claimType;//理赔类型
	private String factory;//维修厂名称
	private BigDecimal claimAmount;//理赔金额
	
	public String getRegistNo() {
		return registNo;
	}
	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}
	public String getCarNum() {
		return carNum;
	}
	public void setCarNum(String carNum) {
		this.carNum = carNum;
	}
	public Date getBeThreatenedTime() {
		return beThreatenedTime;
	}
	public void setBeThreatenedTime(Date beThreatenedTime) {
		this.beThreatenedTime = beThreatenedTime;
	}
	public String getBeThreatenedAddress() {
		return beThreatenedAddress;
	}
	public void setBeThreatenedAddress(String beThreatenedAddress) {
		this.beThreatenedAddress = beThreatenedAddress;
	}
	public String getBeThreatenedReason() {
		return beThreatenedReason;
	}
	public void setBeThreatenedReason(String beThreatenedReason) {
		this.beThreatenedReason = beThreatenedReason;
	}
	public Date getReportTime() {
		return reportTime;
	}
	public void setReportTime(Date reportTime) {
		this.reportTime = reportTime;
	}
	public String getCaseNumber() {
		return caseNumber;
	}
	public void setCaseNumber(String caseNumber) {
		this.caseNumber = caseNumber;
	}
	public Date getCaseTime() {
		return caseTime;
	}
	public void setCaseTime(Date caseTime) {
		this.caseTime = caseTime;
	}
	public Date getClosingTime() {
		return closingTime;
	}
	public void setClosingTime(Date closingTime) {
		this.closingTime = closingTime;
	}
	public String getEndCaseNo() {
		return endCaseNo;
	}
	public void setEndCaseNo(String endCaseNo) {
		this.endCaseNo = endCaseNo;
	}
	public String getClaimType() {
		return claimType;
	}
	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}
	public String getFactory() {
		return factory;
	}
	public void setFactory(String factory) {
		this.factory = factory;
	}
	public BigDecimal getClaimAmount() {
		return claimAmount;
	}
	public void setClaimAmount(BigDecimal claimAmount) {
		this.claimAmount = claimAmount;
	}
}
