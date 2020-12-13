package ins.sino.claimcar.other.vo;

import java.util.Date;

/**
 * 	@author huanggs
 */
public class ShangHaiDataVo implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	
	//序号
	private String number;
	
	//报案号
	private String registNo;
	
	//险种代码
	private String riskCode;
	
	//报案时间
	private String registTime;
	
	//立案号
	private String claimNo;
	
	//注销类型
	private String cancelType;
	
	//理赔编号
	private String claimSeqNo;
	
	//总赔款包费用sumamt +SumFee
	private String sumPaid;  
	
	//实赔
	private String sumAmt;
	
	//结案时间
	private String EndCaseTime;
	
	//注销上传时间
	private String CancelUnderwriteTime;
	
	//结案上传时间
	private String EndUnderwriteTime;
	
	public String getRegistNo() {
		return registNo;
	}
	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}
	public String getRiskCode() {
		return riskCode;
	}
	public void setRiskCode(String riskCode) {
		this.riskCode = riskCode;
	}
	public String getRegistTime() {
		return registTime;
	}
	public void setRegistTime(String registTime) {
		this.registTime = registTime;
	}
	public String getClaimNo() {
		return claimNo;
	}
	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}
	public String getCancelType() {
		return cancelType;
	}
	public void setCancelType(String cancelType) {
		this.cancelType = cancelType;
	}
	public String getClaimSeqNo() {
		return claimSeqNo;
	}
	public void setClaimSeqNo(String claimSeqNo) {
		this.claimSeqNo = claimSeqNo;
	}
	public String getSumPaid() {
		return sumPaid;
	}
	public void setSumPaid(String sumPaid) {
		this.sumPaid = sumPaid;
	}
	public String getSumAmt() {
		return sumAmt;
	}
	public void setSumAmt(String sumAmt) {
		this.sumAmt = sumAmt;
	}
	public String getEndCaseTime() {
		return EndCaseTime;
	}
	public void setEndCaseTime(String endCaseTime) {
		this.EndCaseTime = endCaseTime;
	}
	public String getCancelUnderwriteTime() {
		return CancelUnderwriteTime;
	}
	public void setCancelUnderwriteTime(String cancelUnderwriteTime) {
		CancelUnderwriteTime = cancelUnderwriteTime;
	}
	public String getEndUnderwriteTime() {
		return EndUnderwriteTime;
	}
	public void setEndUnderwriteTime(String endUnderwriteTime) {
		EndUnderwriteTime = endUnderwriteTime;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	
	
}
