/******************************************************************************
 * CREATETIME : 2016年6月1日 上午11:43:01
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import java.util.Date;

import ins.sino.claimcar.carplatform.adapter.DateXmlAdapterSH;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * @author ★XMSH
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SHBIVClaimBasePartVo {

	@XmlElement(name = "REQUEST_TYPE", required = true)
	private String requestType;// 请求类型

	@XmlElement(name = "USER")
	private String user;// 用户名

	@XmlElement(name = "PASSWORD")
	private String password;// 密码

	@XmlElement(name = "CLAIM_CODE")
	private String claimCode;// 理赔编码

	@XmlElement(name = "NUMERATION_START_TIME")
	@XmlJavaTypeAdapter(DateXmlAdapterSH.class)
	private Date numerationStartTime;// 厘算开始时间

	@XmlElement(name = "NUMERATION_END_TIME")
	@XmlJavaTypeAdapter(DateXmlAdapterSH.class)
	private Date numerationEndTime;// 厘算结束时间

	@XmlElement(name = "ASSESOR_START_TIME")
	@XmlJavaTypeAdapter(DateXmlAdapterSH.class)
	private Date assesorStartTime;// 核赔开始时间

	@XmlElement(name = "ASSESOR_END_TIME")
	@XmlJavaTypeAdapter(DateXmlAdapterSH.class)
	private Date assesorEndTime;// 核赔结束时间

	@XmlElement(name = "OTHER_AMOUNT")
	private Double otherAmount;// 理赔费用

	@XmlElement(name = "ASSESOR_DES", required = true)
	private String assesorDes;// 核赔意见

	@XmlElement(name = "ASSESOR_AMOUNT")
	private Double assesorAmount;// 总核赔金额

	@XmlElement(name = "DRIVER_NAME")
	private String driverName;// 出险驾驶员姓名

	@XmlElement(name = "CERTI_TYPE", required = true)
	private String certiType;// 出险驾驶员证件类型

	@XmlElement(name = "CERTI_CODE")
	private String certiCode;// 出险驾驶员证件号码

	@XmlElement(name = "LICENSE_NO")
	private String licenseNo;// 出险驾驶员档案编号

	@XmlElement(name = "LICENSE_EFFECTURAL_DATE")
	private String licenseEffecturalDate;// 出险驾驶员驾驶证有效日期

	@XmlElement(name = "ACCIDENT_PLACE")
	private String accidentPlace;// 出险地点

	@XmlElement(name = "PROPORTIONA_CLAIM", required = true)
	private String proportionaClaim;// 是否比例赔付

	@XmlElement(name = "ACCIDENT_PLACE_MARK")
	private String accidentPlaceMark;// 出险地点唯一标识

	@XmlElement(name = "SUBROGATION_FLAG", required = true)
	private String subrogationFlag;// 是否代位求偿标志

	@XmlElement(name = "REPORT_NO")
	private String reportNo;// 报案号

	@XmlElement(name = "Remark")
	private String remark;// 案件备注
	
	/*牛强  2017-03-15 改*/
	@XmlElement(name="IsSingleAccident")
	private String isSingleAccident; //是否单车事故
	
	@XmlElement(name="IsPersonInjured")
	private String isPersonInjured; //是否包含人伤
	
	@XmlElement(name="IsProtectLoss")
	private String isProtectLoss; //是否包含财损
	
	@XmlElement(name="UnderDefLoss")
	private double underDefLoss; //核损总金额
	
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
	 * @param version 标准地址库版本号
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * @return 返回坐标数据
	 */
	public String getCoordinate() {
		return coordinate;
	}

	/**
	 * @param coordinate 坐标系数据
	 */
	public void setCoordinate(String coordinate) {
		this.coordinate = coordinate;
	}

	/**
	 * @return 返回坐标系代码
	 */
	public String getCoordinateSystem() {
		return coordinateSystem;
	}

	/**
	 * @param coordinateSystem	坐标系代码
	 */
	public void setCoordinateSystem(String coordinateSystem) {
		this.coordinateSystem = coordinateSystem;
	}

	/**
	 * @return 返回 requestType 请求类型
	 */
	public String getRequestType() {
		return requestType;
	}

	/**
	 * @param requestType 要设置的 请求类型
	 */
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	/**
	 * @return 返回 user 用户名
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @param user 要设置的 用户名
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * @return 返回 password 密码
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password 要设置的 密码
	 */
	public void setPassword(String password) {
		this.password = password;
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
	 * @return 返回 numerationStartTime 厘算开始时间
	 */
	public Date getNumerationStartTime() {
		return numerationStartTime;
	}

	/**
	 * @param numerationStartTime 要设置的 厘算开始时间
	 */
	public void setNumerationStartTime(Date numerationStartTime) {
		this.numerationStartTime = numerationStartTime;
	}

	/**
	 * @return 返回 numerationEndTime 厘算结束时间
	 */
	public Date getNumerationEndTime() {
		return numerationEndTime;
	}

	/**
	 * @param numerationEndTime 要设置的 厘算结束时间
	 */
	public void setNumerationEndTime(Date numerationEndTime) {
		this.numerationEndTime = numerationEndTime;
	}

	/**
	 * @return 返回 assesorStartTime 核赔开始时间
	 */
	public Date getAssesorStartTime() {
		return assesorStartTime;
	}

	/**
	 * @param assesorStartTime 要设置的 核赔开始时间
	 */
	public void setAssesorStartTime(Date assesorStartTime) {
		this.assesorStartTime = assesorStartTime;
	}

	/**
	 * @return 返回 assesorEndTime 核赔结束时间
	 */
	public Date getAssesorEndTime() {
		return assesorEndTime;
	}

	/**
	 * @param assesorEndTime 要设置的 核赔结束时间
	 */
	public void setAssesorEndTime(Date assesorEndTime) {
		this.assesorEndTime = assesorEndTime;
	}

	/**
	 * @return 返回 otherAmount 理赔费用
	 */
	public Double getOtherAmount() {
		return otherAmount;
	}

	/**
	 * @param otherAmount 要设置的 理赔费用
	 */
	public void setOtherAmount(Double otherAmount) {
		this.otherAmount = otherAmount;
	}

	/**
	 * @return 返回 assesorDes 核赔意见
	 */
	public String getAssesorDes() {
		return assesorDes;
	}

	/**
	 * @param assesorDes 要设置的 核赔意见
	 */
	public void setAssesorDes(String assesorDes) {
		this.assesorDes = assesorDes;
	}

	/**
	 * @return 返回 assesorAmount 总核赔金额
	 */
	public Double getAssesorAmount() {
		return assesorAmount;
	}

	/**
	 * @param assesorAmount 要设置的 总核赔金额
	 */
	public void setAssesorAmount(Double assesorAmount) {
		this.assesorAmount = assesorAmount;
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
	 * @return 返回 licenseEffecturalDate 出险驾驶员驾驶证有效日期
	 */
	public String getLicenseEffecturalDate() {
		return licenseEffecturalDate;
	}

	/**
	 * @param licenseEffecturalDate 要设置的 出险驾驶员驾驶证有效日期
	 */
	public void setLicenseEffecturalDate(String licenseEffecturalDate) {
		this.licenseEffecturalDate = licenseEffecturalDate;
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
	 * @return 返回 proportionaClaim 是否比例赔付
	 */
	public String getProportionaClaim() {
		return proportionaClaim;
	}

	/**
	 * @param proportionaClaim 要设置的 是否比例赔付
	 */
	public void setProportionaClaim(String proportionaClaim) {
		this.proportionaClaim = proportionaClaim;
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
	 * @return 返回 temporaryflag 是否临时车辆
	 */
	public String getReportNo() {
		return reportNo;
	}

	/**
	 * @param temporaryflag 要设置的 是否临时车辆
	 */
	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}

	/**
	 * @return 返回 remark 案件备注
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark 要设置的 案件备注
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getIsSingleAccident() {
		return isSingleAccident;
	}

	public void setIsSingleAccident(String isSingleAccident) {
		this.isSingleAccident = isSingleAccident;
	}

	public String getIsPersonInjured() {
		return isPersonInjured;
	}

	public void setIsPersonInjured(String isPersonInjured) {
		this.isPersonInjured = isPersonInjured;
	}

	public String getIsProtectLoss() {
		return isProtectLoss;
	}

	public void setIsProtectLoss(String isProtectLoss) {
		this.isProtectLoss = isProtectLoss;
	}

	public double getUnderDefLoss() {
		return underDefLoss;
	}

	public void setUnderDefLoss(double underDefLoss) {
		this.underDefLoss = underDefLoss;
	}

}
