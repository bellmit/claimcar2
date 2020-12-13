package ins.sino.claimcar.claimjy.vo;


import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * <pre></pre>
 * @author ★LiYi
 */
@XStreamAlias("LossReporting")
public class LossReportingVo implements Serializable {
	private static final long serialVersionUID = 1892312751011082224L;
	@XStreamAlias("Id")
	private String id;
	@XStreamAlias("ReportCode")
	private String reportCode;
	@XStreamAlias("AccidentCauseCode")
	private String accidentCauseCode;
	@XStreamAlias("AccidentCauseName")
	private String accidentCauseName;
	@XStreamAlias("AccidentDutyCode")
	private String accidentDutyCode;
	@XStreamAlias("AccidentDutyName")
	private String accidentDutyName;
	@XStreamAlias("ReportPersonName")
	private String reportPersonName;
	@XStreamAlias("ReportMoblePhone")
	private String reportMoblePhone;
	@XStreamAlias("ReportTime")
	private String reportTime;
	@XStreamAlias("AccidentTime")
	private String accidentTime;
	@XStreamAlias("IsCurrentReport")
	private String isCurrentReport;
	@XStreamAlias("ManageType")
	private String manageType;
	@XStreamAlias("AccidentReasonCode")
	private String accidentReasonCode;
	@XStreamAlias("AccidentReasonName")
	private String accidentReasonName;
	@XStreamAlias("HugeType")
	private String hugeType;
	@XStreamAlias("HugeTypeName")
	private String hugeTypeName;
	@XStreamAlias("HugeCode")
	private String hugeCode;
	@XStreamAlias("HugeName")
	private String hugeName;
	@XStreamAlias("InsuranceCompanyCode")
	private String insuranceCompanyCode;
	@XStreamAlias("InsuranceCompanyName")
	private String insuranceCompanyName;
	@XStreamAlias("ReportType")
	private String reportType;
	@XStreamAlias("AccidentPlace")
	private String accidentPlace;
	@XStreamAlias("AccidentCourse")
	private String accidentCourse;
	@XStreamAlias("CarLossFlag")
	private String carLossFlag;
	@XStreamAlias("InjureLossFlag")
	private String injureLossFlag;
	@XStreamAlias("CargoLossFlag")
	private String cargoLossFlag;
	@XStreamAlias("ThiefLossFlag")
	private String thiefLossFlag;
	@XStreamAlias("InjureNum")
	private String injureNum;
	@XStreamAlias("AccidentArea")
	private String accidentArea;
	@XStreamAlias("AccidentWeather")
	private String accidentWeather;
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getReportCode() {
		return reportCode;
	}

	public void setReportCode(String reportCode) {
		this.reportCode = reportCode;
	}

	public String getAccidentCauseCode() {
		return accidentCauseCode;
	}

	public void setAccidentCauseCode(String accidentCauseCode) {
		this.accidentCauseCode = accidentCauseCode;
	}

	public String getAccidentCauseName() {
		return accidentCauseName;
	}

	public void setAccidentCauseName(String accidentCauseName) {
		this.accidentCauseName = accidentCauseName;
	}

	public String getAccidentDutyCode() {
		return accidentDutyCode;
	}

	public void setAccidentDutyCode(String accidentDutyCode) {
		this.accidentDutyCode = accidentDutyCode;
	}

	public String getAccidentDutyName() {
		return accidentDutyName;
	}

	public void setAccidentDutyName(String accidentDutyName) {
		this.accidentDutyName = accidentDutyName;
	}

	public String getReportPersonName() {
		return reportPersonName;
	}

	public void setReportPersonName(String reportPersonName) {
		this.reportPersonName = reportPersonName;
	}

	public String getReportMoblePhone() {
		return reportMoblePhone;
	}

	public void setReportMoblePhone(String reportMoblePhone) {
		this.reportMoblePhone = reportMoblePhone;
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

	public String getIsCurrentReport() {
		return isCurrentReport;
	}

	public void setIsCurrentReport(String isCurrentReport) {
		this.isCurrentReport = isCurrentReport;
	}

	public String getManageType() {
		return manageType;
	}

	public void setManageType(String manageType) {
		this.manageType = manageType;
	}

	public String getAccidentReasonCode() {
		return accidentReasonCode;
	}

	public void setAccidentReasonCode(String accidentReasonCode) {
		this.accidentReasonCode = accidentReasonCode;
	}

	public String getAccidentReasonName() {
		return accidentReasonName;
	}

	public void setAccidentReasonName(String accidentReasonName) {
		this.accidentReasonName = accidentReasonName;
	}

	public String getHugeType() {
		return hugeType;
	}

	public void setHugeType(String hugeType) {
		this.hugeType = hugeType;
	}

	public String getHugeTypeName() {
		return hugeTypeName;
	}

	public void setHugeTypeName(String hugeTypeName) {
		this.hugeTypeName = hugeTypeName;
	}

	public String getHugeCode() {
		return hugeCode;
	}

	public void setHugeCode(String hugeCode) {
		this.hugeCode = hugeCode;
	}

	public String getHugeName() {
		return hugeName;
	}

	public void setHugeName(String hugeName) {
		this.hugeName = hugeName;
	}

	public String getInsuranceCompanyCode() {
		return insuranceCompanyCode;
	}

	public void setInsuranceCompanyCode(String insuranceCompanyCode) {
		this.insuranceCompanyCode = insuranceCompanyCode;
	}

	public String getInsuranceCompanyName() {
		return insuranceCompanyName;
	}

	public void setInsuranceCompanyName(String insuranceCompanyName) {
		this.insuranceCompanyName = insuranceCompanyName;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public String getAccidentPlace() {
		return accidentPlace;
	}

	public void setAccidentPlace(String accidentPlace) {
		this.accidentPlace = accidentPlace;
	}

	public String getAccidentCourse() {
		return accidentCourse;
	}

	public void setAccidentCourse(String accidentCourse) {
		this.accidentCourse = accidentCourse;
	}

	public String getCarLossFlag() {
		return carLossFlag;
	}

	public void setCarLossFlag(String carLossFlag) {
		this.carLossFlag = carLossFlag;
	}

	public String getInjureLossFlag() {
		return injureLossFlag;
	}

	public void setInjureLossFlag(String injureLossFlag) {
		this.injureLossFlag = injureLossFlag;
	}

	public String getCargoLossFlag() {
		return cargoLossFlag;
	}

	public void setCargoLossFlag(String cargoLossFlag) {
		this.cargoLossFlag = cargoLossFlag;
	}

	public String getThiefLossFlag() {
		return thiefLossFlag;
	}

	public void setThiefLossFlag(String thiefLossFlag) {
		this.thiefLossFlag = thiefLossFlag;
	}

	public String getInjureNum() {
		return injureNum;
	}

	public void setInjureNum(String injureNum) {
		this.injureNum = injureNum;
	}

	public String getAccidentArea() {
		return accidentArea;
	}

	public void setAccidentArea(String accidentArea) {
		this.accidentArea = accidentArea;
	}

	public String getAccidentWeather() {
		return accidentWeather;
	}

	public void setAccidentWeather(String accidentWeather) {
		this.accidentWeather = accidentWeather;
	}

	
}

