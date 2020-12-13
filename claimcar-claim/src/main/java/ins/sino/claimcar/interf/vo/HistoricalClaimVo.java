package ins.sino.claimcar.interf.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("HistoricalClaim")
public class HistoricalClaimVo {
	/*出险时间*/
	@XStreamAlias("ClaimDate")
	private String claimDate;
	/*结案时间*/
	@XStreamAlias("ClaimEndDate")
	private String claimEndDate;
	/*报案号*/
	@XStreamAlias("ClaimNumber")
	private String claimNumber;
	/*驾驶员*/
	@XStreamAlias("DriverName")
	private String driverName;
	/*出险地点*/
	@XStreamAlias("EventAddress")
	private String eventAddress;
	/*险别*/
	@XStreamAlias("InsuranceCategory")
	private String insuranceCategory;
	/*保单号*/
	@XStreamAlias("InsuranceNumber")
	private String insuranceNumber;
	/*保单结案金额*/
	@XStreamAlias("PaidAmount")
	private String paidAmount;
	/*赔付次数*/
	@XStreamAlias("PaidTimes")
	private String paidTimes;
	/*<报案时间*/
	@XStreamAlias("ReportDate")
	private String reportDate;
	/*报案人*/
	@XStreamAlias("Reporter")
	private String reporter;
   /* 报案人联系电话*/
	@XStreamAlias("ReporterTel")
	private String reporterTel;
	/*案件状态*/
	@XStreamAlias("Status")
	private String status;
	public String getClaimDate() {
		return claimDate;
	}
	public void setClaimDate(String claimDate) {
		this.claimDate = claimDate;
	}
	public String getClaimEndDate() {
		return claimEndDate;
	}
	public void setClaimEndDate(String claimEndDate) {
		this.claimEndDate = claimEndDate;
	}
	public String getClaimNumber() {
		return claimNumber;
	}
	public void setClaimNumber(String claimNumber) {
		this.claimNumber = claimNumber;
	}
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	public String getEventAddress() {
		return eventAddress;
	}
	public void setEventAddress(String eventAddress) {
		this.eventAddress = eventAddress;
	}
	public String getInsuranceCategory() {
		return insuranceCategory;
	}
	public void setInsuranceCategory(String insuranceCategory) {
		this.insuranceCategory = insuranceCategory;
	}
	public String getInsuranceNumber() {
		return insuranceNumber;
	}
	public void setInsuranceNumber(String insuranceNumber) {
		this.insuranceNumber = insuranceNumber;
	}
	public String getPaidAmount() {
		return paidAmount;
	}
	public void setPaidAmount(String paidAmount) {
		this.paidAmount = paidAmount;
	}
	public String getPaidTimes() {
		return paidTimes;
	}
	public void setPaidTimes(String paidTimes) {
		this.paidTimes = paidTimes;
	}
	public String getReportDate() {
		return reportDate;
	}
	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}
	public String getReporter() {
		return reporter;
	}
	public void setReporter(String reporter) {
		this.reporter = reporter;
	}
	public String getReporterTel() {
		return reporterTel;
	}
	public void setReporterTel(String reporterTel) {
		this.reporterTel = reporterTel;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
