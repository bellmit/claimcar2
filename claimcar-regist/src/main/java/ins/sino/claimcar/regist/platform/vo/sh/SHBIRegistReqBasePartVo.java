/******************************************************************************
 * CREATETIME : 2016年5月25日 上午9:36:07
 ******************************************************************************/
package ins.sino.claimcar.regist.platform.vo.sh;

import ins.sino.claimcar.carplatform.adapter.DateXmlAdapter;
import ins.sino.claimcar.carplatform.adapter.DateXmlAdapterSH;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * 商业报案请求平台信息-基本信息
 * @author ★Luwei
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SHBIRegistReqBasePartVo {

	@XmlElement(name = "CONFIRM_SEQUENCE_NO")
	private String confirmSequenceNo;// 投保确认码

	@XmlElement(name = "CLAIM_CODE")
	private String claimCode;// 理赔编码

	@XmlElement(name = "REPORT_NO", required = true)
	private String reportNo;// 报案号

	@XmlJavaTypeAdapter(DateXmlAdapterSH.class)
	@XmlElement(name = "ACCIDENT_TIME", required = true)
	private Date accidentTime;// 出险时间

	@XmlJavaTypeAdapter(DateXmlAdapterSH.class)
	@XmlElement(name = "REPORT_TIME", required = true)
	private Date reportTime;// 报案时间

	@XmlElement(name = "ACCIDENT_DESCRIPTION", required = true)
	private String accidentDescription;// 出险经过

	@XmlElement(name = "CAR_MARK")
	private String carMark;// 出险承保车辆号牌号码

	@XmlElement(name = "VEHICLE_TYPE")
	private String vehicleType;// 出险承保车辆号牌种类

	@XmlElement(name = "POLICY_NO", required = true)
	private String policyNo;// 保单号

	@XmlElement(name = "REPORTER", required = true)
	private String reporter;// 报案人姓名

	@XmlElement(name = "OPTION_TYPE")
	private String optionType;// 事故处理类型

	@XmlElement(name = "ACCIDENT_REASON")
	private String accidentReason;// 事故原因

	@XmlElement(name = "TEL", required = true)
	private String tel;// 报案人电话

	@XmlElement(name = "WEATHER")
	private String weather;// 天气

	@XmlElement(name = "OWNER")
	private String owner;// 车主

	@XmlElement(name = "DRIVER_NAME", required = true)
	private String driverName;// 出险驾驶员姓名

	@XmlElement(name = "CERTI_TYPE")
	private String certiType;// 出险驾驶员证件类型

	@XmlElement(name = "CERTI_CODE")
	private String certiCode;// 出险驾驶员证件号码

	@XmlElement(name = "LICENSE_NO")
	private String licenseNo;// 出险驾驶员档案编号
	
	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	@XmlElement(name = "LICENSE_EFFECTURAL_DATE")
	private Date licenseEffecturalDate;// 出险驾驶员驾驶证有效日期

	@XmlElement(name = "ACCIDENT_PLACE", required = true)
	private String accidentPlace;// 出险地点

	@XmlElement(name = "POLICY_PLACE", required = true)
	private String policyPlace;// 保单归属地

	@XmlElement(name = "ACCIDENT_PLACE_MARK")
	private String accidentPlaceMark;// 出险地点唯一标识

	@XmlElement(name = "SUBROGATION_FLAG")
	private String subrogationFlag;// 是否代位求偿标志

	@XmlElement(name = "PERSON_NUM")
	private String personNum;// 事故伤亡人数

	@XmlElement(name = "ReporterID")
	private String reporterid;// 报案人身份证号
	
	@XmlElement(name = "Version")
	private String version; // 标准地址库版本号
	
	@XmlElement(name = "Coordinate")
	private String coordinate; // 坐标数据
	
	@XmlElement(name = "CoordinateSystem")
	private String coordinateSystem; // 坐标系代码

	/**
	 * @return 返回标准地址库版本号
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version  标准地址库版本号
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * @return	返回坐标数据
	 */
	public String getCoordinate() {
		return coordinate;
	}

	/**
	 * @param coordinate  坐标数据
	 */
	public void setCoordinate(String coordinate) {
		this.coordinate = coordinate;
	}

	/**
	 * @return  返回坐标系编码
	 */
	public String getCoordinateSystem() {
		return coordinateSystem;
	}

	/**
	 * @param coordinateSystem  坐标系编码
	 */
	public void setCoordinateSystem(String coordinateSystem) {
		this.coordinateSystem = coordinateSystem;
	}

	/**
	 * @return 返回 confirmSequenceNo 投保确认码
	 */
	public String getConfirmSequenceNo() {
		return confirmSequenceNo;
	}

	/**
	 * @param confirmSequenceNo 要设置的 投保确认码
	 */
	public void setConfirmSequenceNo(String confirmSequenceNo) {
		this.confirmSequenceNo = confirmSequenceNo;
	}

	/**
	 * @return 返回 claimCode 理赔编码
	 */
	public String getClaimCode() {
		return claimCode;
	}

	/**
	 * @param claimCode 要设置的 理赔编码
	 */
	public void setClaimCode(String claimCode) {
		this.claimCode = claimCode;
	}

	/**
	 * @return 返回 reportNo 报案号
	 */
	public String getReportNo() {
		return reportNo;
	}

	/**
	 * @param reportNo 要设置的 报案号
	 */
	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}

	/**
	 * @return 返回 accidentTime 出险时间
	 */
	public Date getAccidentTime() {
		return accidentTime;
	}

	/**
	 * @param accidentTime 要设置的 出险时间
	 */
	public void setAccidentTime(Date accidentTime) {
		this.accidentTime = accidentTime;
	}

	/**
	 * @return 返回 reportTime 报案时间
	 */
	public Date getReportTime() {
		return reportTime;
	}

	/**
	 * @param reportTime 要设置的 报案时间
	 */
	public void setReportTime(Date reportTime) {
		this.reportTime = reportTime;
	}

	/**
	 * @return 返回 accidentDescription 出险经过
	 */
	public String getAccidentDescription() {
		return accidentDescription;
	}

	/**
	 * @param accidentDescription 要设置的 出险经过
	 */
	public void setAccidentDescription(String accidentDescription) {
		this.accidentDescription = accidentDescription;
	}

	/**
	 * @return 返回 carMark 出险承保车辆号牌号码
	 */
	public String getCarMark() {
		return carMark;
	}

	/**
	 * @param carMark 要设置的 出险承保车辆号牌号码
	 */
	public void setCarMark(String carMark) {
		this.carMark = carMark;
	}

	/**
	 * @return 返回 vehicleType 出险承保车辆号牌种类
	 */
	public String getVehicleType() {
		return vehicleType;
	}

	/**
	 * @param vehicleType 要设置的 出险承保车辆号牌种类
	 */
	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	/**
	 * @return 返回 policyNo 保单号
	 */
	public String getPolicyNo() {
		return policyNo;
	}

	/**
	 * @param policyNo 要设置的 保单号
	 */
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	/**
	 * @return 返回 reporter 报案人姓名
	 */
	public String getReporter() {
		return reporter;
	}

	/**
	 * @param reporter 要设置的 报案人姓名
	 */
	public void setReporter(String reporter) {
		this.reporter = reporter;
	}

	/**
	 * @return 返回 optionType 事故处理类型
	 */
	public String getOptionType() {
		return optionType;
	}

	/**
	 * @param optionType 要设置的 事故处理类型
	 */
	public void setOptionType(String optionType) {
		this.optionType = optionType;
	}

	/**
	 * @return 返回 accidentReason 事故原因
	 */
	public String getAccidentReason() {
		return accidentReason;
	}

	/**
	 * @param accidentReason 要设置的 事故原因
	 */
	public void setAccidentReason(String accidentReason) {
		this.accidentReason = accidentReason;
	}

	/**
	 * @return 返回 tel 报案人电话
	 */
	public String getTel() {
		return tel;
	}

	/**
	 * @param tel 要设置的 报案人电话
	 */
	public void setTel(String tel) {
		this.tel = tel;
	}

	/**
	 * @return 返回 weather 天气
	 */
	public String getWeather() {
		return weather;
	}

	/**
	 * @param weather 要设置的 天气
	 */
	public void setWeather(String weather) {
		this.weather = weather;
	}

	/**
	 * @return 返回 owner 车主
	 */
	public String getOwner() {
		return owner;
	}

	/**
	 * @param owner 要设置的 车主
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}

	/**
	 * @return 返回 driverName 出险驾驶员姓名
	 */
	public String getDriverName() {
		return driverName;
	}

	/**
	 * @param driverName 要设置的 出险驾驶员姓名
	 */
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	/**
	 * @return 返回 certiType 出险驾驶员证件类型
	 */
	public String getCertiType() {
		return certiType;
	}

	/**
	 * @param certiType 要设置的 出险驾驶员证件类型
	 */
	public void setCertiType(String certiType) {
		this.certiType = certiType;
	}

	/**
	 * @return 返回 certiCode 出险驾驶员证件号码
	 */
	public String getCertiCode() {
		return certiCode;
	}

	/**
	 * @param certiCode 要设置的 出险驾驶员证件号码
	 */
	public void setCertiCode(String certiCode) {
		this.certiCode = certiCode;
	}

	/**
	 * @return 返回 licenseNo 出险驾驶员档案编号
	 */
	public String getLicenseNo() {
		return licenseNo;
	}

	/**
	 * @param licenseNo 要设置的 出险驾驶员档案编号
	 */
	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}

	/**
	 * @return 返回 accidentPlace 出险地点
	 */
	public String getAccidentPlace() {
		return accidentPlace;
	}

	/**
	 * @param accidentPlace 要设置的 出险地点
	 */
	public void setAccidentPlace(String accidentPlace) {
		this.accidentPlace = accidentPlace;
	}

	/**
	 * @return 返回 policyPlace 保单归属地
	 */
	public String getPolicyPlace() {
		return policyPlace;
	}

	/**
	 * @param policyPlace 要设置的 保单归属地
	 */
	public void setPolicyPlace(String policyPlace) {
		this.policyPlace = policyPlace;
	}

	/**
	 * @return 返回 accidentPlaceMark 出险地点唯一标识
	 */
	public String getAccidentPlaceMark() {
		return accidentPlaceMark;
	}

	/**
	 * @param accidentPlaceMark 要设置的 出险地点唯一标识
	 */
	public void setAccidentPlaceMark(String accidentPlaceMark) {
		this.accidentPlaceMark = accidentPlaceMark;
	}

	/**
	 * @return 返回 subrogationFlag 是否代位求偿标志
	 */
	public String getSubrogationFlag() {
		return subrogationFlag;
	}

	/**
	 * @param subrogationFlag 要设置的 是否代位求偿标志
	 */
	public void setSubrogationFlag(String subrogationFlag) {
		this.subrogationFlag = subrogationFlag;
	}

	/**
	 * @return 返回 personNum 事故��亡人数
	 */
	public String getPersonNum() {
		return personNum;
	}

	/**
	 * @param personNum 要设置的 事故伤亡人数
	 */
	public void setPersonNum(String personNum) {
		this.personNum = personNum;
	}

	/**
	 * @return 返回 reporterid 报案人身份证号
	 */
	public String getReporterid() {
		return reporterid;
	}

	/**
	 * @param reporterid 要设置的 报案人身份证号
	 */
	public void setReporterid(String reporterid) {
		this.reporterid = reporterid;
	}

	
	public Date getLicenseEffecturalDate() {
		return licenseEffecturalDate;
	}

	
	public void setLicenseEffecturalDate(Date licenseEffecturalDate) {
		this.licenseEffecturalDate = licenseEffecturalDate;
	}

}
