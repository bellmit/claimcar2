package ins.sino.claimcar.subrogation.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *   报案信息VO
 * @author ★YangKun
 * @CreateTime 2016年3月18日
 */
public class SubrogationReportDataVo implements Serializable{
	private static final long serialVersionUID = 1L;
	/** 保单号 **/ 
	private String policyNo; 

	/** 报案号 **/ 
	private String reportNo; 

	/** 出险时间 **/ 
	private Date accidentTime; 

	/** 出险承保车辆号牌号码;未上牌车辆可为空 **/ 
	private String carMark; 

	/** 出险承保车辆号牌种类;未上牌车辆可以为空 **/ 
	private String vehicleType; 

	/** 出险地点 **/ 
	private String accidentPlace; 

	/** 报案时间；格式YYYYMMDDHHMM **/ 
	private Date reportTime; 

	/** 出险经过 **/ 
	private String accidentDescription; 

	/** 出险驾驶员姓名 **/ 
	private String driverName; 

	/** 报案人姓名 **/ 
	private String reporterName; 

	/** 事故处理方式代码；参见代码 **/ 
	private String manageType; 

	/** 事故责任划分代码；参见代码 **/ 
	private String accidentLiability;
	
	/**第三方车辆情况列表(隶属于报案信息)*/
	private List<ReportThirdVehicleDataVo> thirdVehicleDataList;
	
	/**损失情况列表(隶属于报案信息)*/
	private List<ReportLossDataVo> lossDataList;


	private String claimNotificationNo;//报案号

	private Date notificationTime;//报案时间；精确到分

	private String reporter;//报案人姓名


	private Date lossTime;//出险时间；精确到分

	private String lossArea;//出险地点

	private String lossDesc;//出险经过

	private String licensePlateNo;//出险标的车号牌号码

	private String licensePlateType;//出险标的车号牌种类代码；参见代码

	private String optionType;//事故处理方式；参见代码
	
	
	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getReportNo() {
		return reportNo;
	}

	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}

	public Date getAccidentTime() {
		return accidentTime;
	}

	public void setAccidentTime(Date accidentTime) {
		this.accidentTime = accidentTime;
	}

	public String getCarMark() {
		return carMark;
	}

	public void setCarMark(String carMark) {
		this.carMark = carMark;
	}

	public String getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	public String getAccidentPlace() {
		return accidentPlace;
	}

	public void setAccidentPlace(String accidentPlace) {
		this.accidentPlace = accidentPlace;
	}

	public Date getReportTime() {
		return reportTime;
	}

	public void setReportTime(Date reportTime) {
		this.reportTime = reportTime;
	}

	public String getAccidentDescription() {
		return accidentDescription;
	}

	public void setAccidentDescription(String accidentDescription) {
		this.accidentDescription = accidentDescription;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getReporterName() {
		return reporterName;
	}

	public void setReporterName(String reporterName) {
		this.reporterName = reporterName;
	}

	public String getManageType() {
		return manageType;
	}

	public void setManageType(String manageType) {
		this.manageType = manageType;
	}

	public String getAccidentLiability() {
		return accidentLiability;
	}

	public void setAccidentLiability(String accidentLiability) {
		this.accidentLiability = accidentLiability;
	}

	public List<ReportThirdVehicleDataVo> getThirdVehicleDataList() {
		return thirdVehicleDataList;
	}

	public void setThirdVehicleDataList(
			List<ReportThirdVehicleDataVo> thirdVehicleDataList) {
		this.thirdVehicleDataList = thirdVehicleDataList;
	}

	public List<ReportLossDataVo> getLossDataList() {
		return lossDataList;
	}

	public void setLossDataList(List<ReportLossDataVo> lossDataList) {
		this.lossDataList = lossDataList;
	}

	public String getClaimNotificationNo() {
		return claimNotificationNo;
	}

	public void setClaimNotificationNo(String claimNotificationNo) {
		this.claimNotificationNo = claimNotificationNo;
	}

	public Date getNotificationTime() {
		return notificationTime;
	}

	public void setNotificationTime(Date notificationTime) {
		this.notificationTime = notificationTime;
	}

	public String getReporter() {
		return reporter;
	}

	public void setReporter(String reporter) {
		this.reporter = reporter;
	}

	public Date getLossTime() {
		return lossTime;
	}

	public void setLossTime(Date lossTime) {
		this.lossTime = lossTime;
	}

	public String getLossArea() {
		return lossArea;
	}

	public void setLossArea(String lossArea) {
		this.lossArea = lossArea;
	}

	public String getLossDesc() {
		return lossDesc;
	}

	public void setLossDesc(String lossDesc) {
		this.lossDesc = lossDesc;
	}

	public String getLicensePlateNo() {
		return licensePlateNo;
	}

	public void setLicensePlateNo(String licensePlateNo) {
		this.licensePlateNo = licensePlateNo;
	}

	public String getLicensePlateType() {
		return licensePlateType;
	}

	public void setLicensePlateType(String licensePlateType) {
		this.licensePlateType = licensePlateType;
	}

	public String getOptionType() {
		return optionType;
	}

	public void setOptionType(String optionType) {
		this.optionType = optionType;
	}

	
	

}
