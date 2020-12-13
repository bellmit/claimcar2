package ins.sino.claimcar.mail.vo;

import ins.sino.claimcar.regist.vo.PrpLCItemKindVo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class mailParam implements java.io.Serializable{
	private static final long serialVersionUID = 1L; 
	
	String registNo;
	String comCode;
	String policyNo;
	String kindCode;
	String amount;
	String dangerRemark;
	String checkOpinion;
	String reportOpinion;
	String verifyOpinion;
	BigDecimal DLAmount;
	BigDecimal PLAmount;
	private List<PrpLCItemKindVo> prpCItemKinds = new ArrayList<PrpLCItemKindVo>(0);//报案时保单险别表
	
	public String getRegistNo() {
		return registNo;
	}
	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}
	public String getComCode() {
		return comCode;
	}
	public void setComCode(String comCode) {
		this.comCode = comCode;
	}
	public String getPolicyNo() {
		return policyNo;
	}
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}
	public String getKindCode() {
		return kindCode;
	}
	public void setKindCode(String kindCode) {
		this.kindCode = kindCode;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getDangerRemark() {
		return dangerRemark;
	}
	public void setDangerRemark(String dangerRemark) {
		this.dangerRemark = dangerRemark;
	}
	public String getCheckOpinion() {
		return checkOpinion;
	}
	public void setCheckOpinion(String checkOpinion) {
		this.checkOpinion = checkOpinion;
	}
	public String getReportOpinion() {
		return reportOpinion;
	}
	public void setReportOpinion(String reportOpinion) {
		this.reportOpinion = reportOpinion;
	}
	public String getVerifyOpinion() {
		return verifyOpinion;
	}
	public void setVerifyOpinion(String verifyOpinion) {
		this.verifyOpinion = verifyOpinion;
	}
	public List<PrpLCItemKindVo> getPrpCItemKinds() {
		return prpCItemKinds;
	}
	public void setPrpCItemKinds(List<PrpLCItemKindVo> prpCItemKinds) {
		this.prpCItemKinds = prpCItemKinds;
	}
	public BigDecimal getDLAmount() {
		return DLAmount;
	}
	public void setDLAmount(BigDecimal dLAmount) {
		DLAmount = dLAmount;
	}
	public BigDecimal getPLAmount() {
		return PLAmount;
	}
	public void setPLAmount(BigDecimal pLAmount) {
		PLAmount = pLAmount;
	}
	
}
