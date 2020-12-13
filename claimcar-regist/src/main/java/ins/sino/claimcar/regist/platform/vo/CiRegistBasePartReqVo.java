package ins.sino.claimcar.regist.platform.vo;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * 交强报案请求平台信息BasePartVo类
 * @author dengkk
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CiRegistBasePartReqVo {
	
	@XmlElement(name="CONFIRM_SEQUENCE_NO", required = true)
	private String confirmSequenceNo;// 投保确认号
	
	@XmlElement(name="CLAIM_CODE")
	private String claimCode;// 理赔编号
	
	@XmlElement(name="POLICY_NO", required = true)
	private String policyNo;// 保单号
	
	@XmlElement(name="REPORT_NO", required = true)
	private String reportNo;// 报案号
	
	@XmlElement(name="ACCIDENT_TIME", required = true)
	private String accidentTime;// 出险时间；格式YYYYMMDDHHMM
	
	@XmlElement(name="CAR_MARK")
	private String carMark;// 出险承保车辆号牌号码；未上牌车辆可为空
	
	@XmlElement(name="VEHICLE_TYPE")
	private String vehicleType;// 出险承保车辆号牌种类；参见代码未上牌车辆可以为空
	
	@XmlElement(name="PAY_SELF_FLAG", required = true)
	private String payselfFlag;// 互碰自赔标志；参见代码 出险地点
	
	@XmlElement(name="ACCIDENT_PLACE", required = true)
	private String accidentPlace;// 出险地点
	
	@XmlElement(name="REPORT_TIME", required = true)
	private String reportTime;// 报案时间；格式YYYYMMDDHHMM
	
	@XmlElement(name="ACCIDENT_DESCRIPTION", required = true)
	private String accidentDescription;// 出险经过
	
	@XmlElement(name="ACCIDENT_CAUSE", required = true)
	private String accidentCause;// 出险原因；参见代码 
	
	@XmlElement(name="DRIVER_NAME")
	private String driverName;// 出险驾驶员姓名
	
	@XmlElement(name="DRIVER_LICENSE_NO")
	private String driverLicenseNo;// 出险驾驶员证号码
	
	@XmlElement(name="REPORTER_NAME", required = true)
	private String reporterName;// 报案人姓名
	
	@XmlElement(name="MANAGE_TYPE")
	private String manageType;// 事故处理方式代码；参见代码
	
	@XmlElement(name="ACCIDENT_LIABILITY")
	private String accidentLiability;// 事故责任划分代码；参见代码
	
	@XmlElement(name="VERSION")
	private String version;// 标准地址库版本号
	
	@XmlElement(name="COORDINATE")
	private String coordinate;// 坐标数据
	
	@XmlElement(name="COORDINATE_SYSTEM")
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
	
	public String getAccidentTime() {
		return accidentTime;
	}
	public void setAccidentTime(String accidentTime) {
		this.accidentTime = accidentTime;
	}
	public void setReportTime(String reportTime) {
		this.reportTime = reportTime;
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
