package ins.sino.claimcar.realtimequery.vo.vehicle;

import java.io.Serializable;

public class BasicsInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String reportNo;  //报案号
	private String claimcomPany; //保险公司
	private String insurerArea;  //承保地区
	private String reportTime;  //报案时间
	private String accidentTime;  //出险时间
	private String accidentPlace;  //出险地点
	private String accidentDescription;  //出险经过
	private String claimStatus;  //案件状态代码
	private String riskType;  //保单类型代码
	private String claimQueryNo;  //理赔编码
	private String sumUnderDefLoss;  //核损总金额
	public String getReportNo() {
		return reportNo;
	}
	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}
	public String getClaimcomPany() {
		return claimcomPany;
	}
	public void setClaimcomPany(String claimcomPany) {
		this.claimcomPany = claimcomPany;
	}
	public String getInsurerArea() {
		return insurerArea;
	}
	public void setInsurerArea(String insurerArea) {
		this.insurerArea = insurerArea;
	}
	public String getReportTime() {
		return reportTime;
	}
	public void setReportTime(String reportTime) {
		this.reportTime = reportTime;
	}
	public String getAccidentTime() {
		return accidentTime;
	}
	public void setAccidentTime(String accidentTime) {
		this.accidentTime = accidentTime;
	}
	public String getAccidentPlace() {
		return accidentPlace;
	}
	public void setAccidentPlace(String accidentPlace) {
		this.accidentPlace = accidentPlace;
	}
	public String getAccidentDescription() {
		return accidentDescription;
	}
	public void setAccidentDescription(String accidentDescription) {
		this.accidentDescription = accidentDescription;
	}
	public String getClaimStatus() {
		return claimStatus;
	}
	public void setClaimStatus(String claimStatus) {
		this.claimStatus = claimStatus;
	}
	public String getRiskType() {
		return riskType;
	}
	public void setRiskType(String riskType) {
		this.riskType = riskType;
	}
	public String getClaimQueryNo() {
		return claimQueryNo;
	}
	public void setClaimQueryNo(String claimQueryNo) {
		this.claimQueryNo = claimQueryNo;
	}
	public String getSumUnderDefLoss() {
		return sumUnderDefLoss;
	}
	public void setSumUnderDefLoss(String sumUnderDefLoss) {
		this.sumUnderDefLoss = sumUnderDefLoss;
	}
	
	
	
}
