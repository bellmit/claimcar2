package ins.sino.claimcar.regist.platform.vo;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * 商业报案请求平台信息BasePartVo类
 * @author dengkk
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class BiRegistBasePartReqVo {
	
	@XmlElement(name="ConfirmSequenceNo", required = true)
	private String confirmSequenceNo;// 投保确认号
	
	@XmlElement(name="ClaimSequenceNo")
	private String claimCode;// 理赔编号
	
	@XmlElement(name="PolicyNo", required = true)
	private String policyNo;// 保单号
	
	@XmlElement(name="ClaimNotificationNo", required = true)
	private String reportNo;// 报案号
	
	@XmlElement(name="LossTime", required = true)
	private String accidentTime;// 出险时间；格式YYYYMMDDHHMM
	
	@XmlElement(name="LicensePlateNo")
	private String carMark;// 出险承保车辆号牌号码；未上牌车辆可为空
	
	@XmlElement(name="LicensePlateType")
	private String vehicleType;// 出险承保车辆号牌种类；参见代码未上牌车辆可以为空
	
	@XmlElement(name="SubrogationFlag", required = true)
	private String payselfFlag;// 互碰自赔标志；参见代码 出险地点
	
	@XmlElement(name="LossArea", required = true)
	private String accidentPlace;// 出险地点
	
	@XmlElement(name="NotificationTime", required = true)
	private String reportTime;// 报案时间；格式YYYYMMDDHHMM
	
	@XmlElement(name="LossDesc", required = true)
	private String accidentDescription;// 出险经过
	
	@XmlElement(name="LossCauseCode", required = true)
	private String accidentCause;// 出险原因；参见代码 
	
	@XmlElement(name="DriverName")
	private String driverName;// 出险驾驶员姓名
	
	@XmlElement(name="DriverLicenseNo")
	private String driverLicenseNo;// 出险驾驶员证号码
	
	@XmlElement(name="Reporter", required = true)
	private String reporterName;// 报案人姓名
	
	@XmlElement(name="OptionType")
	private String manageType;// 事故处理方式代码；参见代码
	
	@XmlElement(name="AccidentLiability")
	private String accidentLiability;// 事故责任划分代码；参见代码
	
	@XmlElement(name="Version")
	private String version;// 标准地址库版本号
	
	@XmlElement(name="Coordinate")
	private String coordinate;// 坐标数据
	
	@XmlElement(name="CoordinateSystem")
	private String coordinateSystem;// 坐标系
	
	public String getConfirmSequenceNo() {
		return confirmSequenceNo;
	}
	public void setConfirmSequenceNo(String confirmSequenceNo) {
		this.confirmSequenceNo = confirmSequenceNo;
	}
	public String getClaimCode() {
		return claimCode;
	}
	public void setClaimCode(String claimCode) {
		this.claimCode = claimCode;
	}
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
	public String getPayselfFlag() {
		return payselfFlag;
	}
	public void setPayselfFlag(String payselfFlag) {
		this.payselfFlag = payselfFlag;
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
	public String getAccidentCause() {
		return accidentCause;
	}
	public void setAccidentCause(String accidentCause) {
		this.accidentCause = accidentCause;
	}
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	public String getDriverLicenseNo() {
		return driverLicenseNo;
	}
	public void setDriverLicenseNo(String driverLicenseNo) {
		this.driverLicenseNo = driverLicenseNo;
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
	public String getAccidentTime() {
		return accidentTime;
	}
	public void setAccidentTime(String accidentTime) {
		this.accidentTime = accidentTime;
	}
	public String getReportTime() {
		return reportTime;
	}
	public void setReportTime(String reportTime) {
		this.reportTime = reportTime;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getCoordinate() {
		return coordinate;
	}
	public void setCoordinate(String coordinate) {
		this.coordinate = coordinate;
	}
	public String getCoordinateSystem() {
		return coordinateSystem;
	}
	public void setCoordinateSystem(String coordinateSystem) {
		this.coordinateSystem = coordinateSystem;
	}
	
	

}
