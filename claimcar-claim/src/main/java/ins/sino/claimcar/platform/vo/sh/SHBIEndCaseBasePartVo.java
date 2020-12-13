/******************************************************************************
 * CREATETIME : 2016年6月6日 下午7:25:26
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import ins.sino.claimcar.carplatform.adapter.DateXmlAdapterSH;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * @author ★XMSH
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SHBIEndCaseBasePartVo {

	@XmlElement(name = "CONFIRM_SEQUENCE_NO")
	private String confirmSequenceNo;// 投保确认码

	@XmlElement(name = "CLAIM_CODE")
	private String claimCode;// 理赔编码

	@XmlElement(name = "TOTAL_AMOUNT")
	private Double totalAmount;// 赔款总金额

	@XmlElement(name = "REPORT_NO")
	private String reportNo;// 报案号

	@XmlElement(name = "REGISTRATION_NO")
	private String registrationNo;// 立案号

	@XmlElement(name = "CLAIM_NO")
	private String claimNo;// 赔案号

	@XmlJavaTypeAdapter(DateXmlAdapterSH.class)
	@XmlElement(name = "REPORT_TIME")
	private Date reportTime;// 报案时间

	@XmlJavaTypeAdapter(DateXmlAdapterSH.class)
	@XmlElement(name = "REGISTRATION_DATE")
	private Date registrationDate;// 立案时间

	@XmlJavaTypeAdapter(DateXmlAdapterSH.class)
	@XmlElement(name = "ENDCASE_DATE")
	private Date endcaseDate;// 结案时间

	@XmlJavaTypeAdapter(DateXmlAdapterSH.class)
	@XmlElement(name = "DOC_START_TIME")
	private Date docStartTime;// 单证收集开始时间

	@XmlJavaTypeAdapter(DateXmlAdapterSH.class)
	@XmlElement(name = "DOC_END_TIME")
	private Date docEndTime;// 单证收集结束时间

	@XmlJavaTypeAdapter(DateXmlAdapterSH.class)
	@XmlElement(name = "NUMERATION_START_TIME")
	private Date numerationStartTime;// 厘算开始时间

	@XmlJavaTypeAdapter(DateXmlAdapterSH.class)
	@XmlElement(name = "NUMERATION_END_TIME")
	private Date numerationEndTime;// 厘算结束时间

	@XmlJavaTypeAdapter(DateXmlAdapterSH.class)
	@XmlElement(name = "ASSESOR_START_TIME")
	private Date assesorStartTime;// 核赔开始时间

	@XmlJavaTypeAdapter(DateXmlAdapterSH.class)
	@XmlElement(name = "ASSESOR_END_TIME")
	private Date assesorEndTime;// 核赔结束时间

	@XmlJavaTypeAdapter(DateXmlAdapterSH.class)
	@XmlElement(name = "PAY_TIME")
	private Date payTime;// 款项划付时间

	@XmlElement(name = "CAR_MARK")
	private String carMark;// 承保车辆号牌号码

	@XmlElement(name = "VEHICLE_TYPE", required = true)
	private String vehicleType;// 承保车辆号牌种类

	@XmlElement(name = "POLICY_NO")
	private String policyNo;// 保单号

	@XmlElement(name = "REPORTER")
	private String reporter;// 报案人姓名

	@XmlElement(name = "DRIVER_NAME")
	private String driverName;// 出险驾驶员姓名

	@XmlElement(name = "LICENSE_NO")
	private String licenseNo;// 出险驾驶员档案编号

	@XmlJavaTypeAdapter(DateXmlAdapterSH.class)
	@XmlElement(name = "LICENSE_EFFECTURAL_DATE")
	private Date licenseEffecturalDate;// 出险驾驶员驾驶证有效日期

	@XmlElement(name = "PERSON_NUM")
	private Double personNum;// 事故伤亡人数

	@XmlElement(name = "OTHER_AMOUNT")
	private Double otherAmount;// 其他费用直接理赔费用总金额

	@XmlElement(name = "SUBROGATION_FLAG", required = true)
	private String subrogationFlag;// 是否代位求偿标志

	@XmlElement(name = "IsTotalLoss", required = true)
	private String istotalloss;// 是否全损

	//事故信息
	@XmlJavaTypeAdapter(DateXmlAdapterSH.class)
	@XmlElement(name = "ACCIDENT_TIME", required = true)
	private Date accidentTime;// 出险时间

	@XmlElement(name = "ACCIDENT_PLACE", required = true)
	private String accidentPlace;// 出险地点

	@XmlElement(name = "ACCIDENT_DESCRIPTION", required = true)
	private String accidentDescription;// 出险经过
	
	@XmlElement(name = "LIABILITY", required = true)
	private String liability;// 事故责任划分
	
	@XmlElement(name = "OPTION_TYPE", required = true)
	private String optionType;// 事故处理类型
	
	@XmlElement(name = "ACCIDENT_REASON", required = true)
	private String accidentReason;// 事故原因

	@XmlElement(name = "ACCIDENT_PLACE_MARK")
	private String accidentPlaceMark;// 出险地点唯一标识
	
	/*牛强  2017-03-15 改*/
	@XmlElement(name="FraudLogo")
	private String fraudLogo; // 欺诈标志
	
	@XmlElement(name="FraudRecoverAmount")
	private double fraudRecoverAmount; // 欺诈玩会损失金额
	
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
	 * @return 返回 accidentTime。
	 */
	public Date getAccidentTime() {
		return accidentTime;
	}


	
	/**
	 * @param accidentTime 要设置的 accidentTime。
	 */
	public void setAccidentTime(Date accidentTime) {
		this.accidentTime = accidentTime;
	}


	
	/**
	 * @return 返回 accidentPlace。
	 */
	public String getAccidentPlace() {
		return accidentPlace;
	}


	
	/**
	 * @param accidentPlace 要设置的 accidentPlace。
	 */
	public void setAccidentPlace(String accidentPlace) {
		this.accidentPlace = accidentPlace;
	}


	
	/**
	 * @return 返回 accidentDescription。
	 */
	public String getAccidentDescription() {
		return accidentDescription;
	}


	
	/**
	 * @param accidentDescription 要设置的 accidentDescription。
	 */
	public void setAccidentDescription(String accidentDescription) {
		this.accidentDescription = accidentDescription;
	}


	
	/**
	 * @return 返回 liability。
	 */
	public String getLiability() {
		return liability;
	}


	
	/**
	 * @param liability 要设置的 liability。
	 */
	public void setLiability(String liability) {
		this.liability = liability;
	}


	
	/**
	 * @return 返回 optionType。
	 */
	public String getOptionType() {
		return optionType;
	}


	
	/**
	 * @param optionType 要设置的 optionType。
	 */
	public void setOptionType(String optionType) {
		this.optionType = optionType;
	}


	
	/**
	 * @return 返回 accidentReason。
	 */
	public String getAccidentReason() {
		return accidentReason;
	}


	
	/**
	 * @param accidentReason 要设置的 accidentReason。
	 */
	public void setAccidentReason(String accidentReason) {
		this.accidentReason = accidentReason;
	}


	
	/**
	 * @return 返回 accidentPlaceMark。
	 */
	public String getAccidentPlaceMark() {
		return accidentPlaceMark;
	}


	
	/**
	 * @param accidentPlaceMark 要设置的 accidentPlaceMark。
	 */
	public void setAccidentPlaceMark(String accidentPlaceMark) {
		this.accidentPlaceMark = accidentPlaceMark;
	}


	/**
	 * @return 返回 confirmSequenceNo。
	 */
	public String getConfirmSequenceNo() {
		return confirmSequenceNo;
	}

	
	/**
	 * @param confirmSequenceNo 要设置的 confirmSequenceNo。
	 */
	public void setConfirmSequenceNo(String confirmSequenceNo) {
		this.confirmSequenceNo = confirmSequenceNo;
	}

	
	/**
	 * @return 返回 claimCode。
	 */
	public String getClaimCode() {
		return claimCode;
	}

	
	/**
	 * @param claimCode 要设置的 claimCode。
	 */
	public void setClaimCode(String claimCode) {
		this.claimCode = claimCode;
	}

	
	/**
	 * @return 返回 totalAmount。
	 */
	public Double getTotalAmount() {
		return totalAmount;
	}

	
	/**
	 * @param totalAmount 要设置的 totalAmount。
	 */
	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	
	/**
	 * @return 返回 reportNo。
	 */
	public String getReportNo() {
		return reportNo;
	}

	
	/**
	 * @param reportNo 要设置的 reportNo。
	 */
	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}

	
	/**
	 * @return 返回 registrationNo。
	 */
	public String getRegistrationNo() {
		return registrationNo;
	}

	
	/**
	 * @param registrationNo 要设置的 registrationNo。
	 */
	public void setRegistrationNo(String registrationNo) {
		this.registrationNo = registrationNo;
	}

	
	/**
	 * @return 返回 claimNo。
	 */
	public String getClaimNo() {
		return claimNo;
	}

	
	/**
	 * @param claimNo 要设置的 claimNo。
	 */
	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}

	
	/**
	 * @return 返回 reportTime。
	 */
	public Date getReportTime() {
		return reportTime;
	}

	
	/**
	 * @param reportTime 要设置的 reportTime。
	 */
	public void setReportTime(Date reportTime) {
		this.reportTime = reportTime;
	}

	
	/**
	 * @return 返回 registrationDate。
	 */
	public Date getRegistrationDate() {
		return registrationDate;
	}

	
	/**
	 * @param registrationDate 要设置的 registrationDate。
	 */
	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	
	/**
	 * @return 返回 endcaseDate。
	 */
	public Date getEndcaseDate() {
		return endcaseDate;
	}

	
	/**
	 * @param endcaseDate 要设置的 endcaseDate。
	 */
	public void setEndcaseDate(Date endcaseDate) {
		this.endcaseDate = endcaseDate;
	}

	
	/**
	 * @return 返回 docStartTime。
	 */
	public Date getDocStartTime() {
		return docStartTime;
	}

	
	/**
	 * @param docStartTime 要设置的 docStartTime。
	 */
	public void setDocStartTime(Date docStartTime) {
		this.docStartTime = docStartTime;
	}

	
	/**
	 * @return 返回 docEndTime。
	 */
	public Date getDocEndTime() {
		return docEndTime;
	}

	
	/**
	 * @param docEndTime 要设置的 docEndTime。
	 */
	public void setDocEndTime(Date docEndTime) {
		this.docEndTime = docEndTime;
	}

	
	/**
	 * @return 返回 numerationStartTime。
	 */
	public Date getNumerationStartTime() {
		return numerationStartTime;
	}

	
	/**
	 * @param numerationStartTime 要设置的 numerationStartTime。
	 */
	public void setNumerationStartTime(Date numerationStartTime) {
		this.numerationStartTime = numerationStartTime;
	}

	
	/**
	 * @return 返回 numerationEndTime。
	 */
	public Date getNumerationEndTime() {
		return numerationEndTime;
	}

	
	/**
	 * @param numerationEndTime 要设置的 numerationEndTime。
	 */
	public void setNumerationEndTime(Date numerationEndTime) {
		this.numerationEndTime = numerationEndTime;
	}

	
	/**
	 * @return 返回 assesorStartTime。
	 */
	public Date getAssesorStartTime() {
		return assesorStartTime;
	}

	
	/**
	 * @param assesorStartTime 要设置的 assesorStartTime。
	 */
	public void setAssesorStartTime(Date assesorStartTime) {
		this.assesorStartTime = assesorStartTime;
	}

	
	/**
	 * @return 返回 assesorEndTime。
	 */
	public Date getAssesorEndTime() {
		return assesorEndTime;
	}

	
	/**
	 * @param assesorEndTime 要设置的 assesorEndTime。
	 */
	public void setAssesorEndTime(Date assesorEndTime) {
		this.assesorEndTime = assesorEndTime;
	}

	
	/**
	 * @return 返回 payTime。
	 */
	public Date getPayTime() {
		return payTime;
	}

	
	/**
	 * @param payTime 要设置的 payTime。
	 */
	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	
	/**
	 * @return 返回 carMark。
	 */
	public String getCarMark() {
		return carMark;
	}

	
	/**
	 * @param carMark 要设置的 carMark。
	 */
	public void setCarMark(String carMark) {
		this.carMark = carMark;
	}

	
	/**
	 * @return 返回 vehicleType。
	 */
	public String getVehicleType() {
		return vehicleType;
	}

	
	/**
	 * @param vehicleType 要设置的 vehicleType。
	 */
	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	
	/**
	 * @return 返回 policyNo。
	 */
	public String getPolicyNo() {
		return policyNo;
	}

	
	/**
	 * @param policyNo 要设置的 policyNo。
	 */
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	
	/**
	 * @return 返回 reporter。
	 */
	public String getReporter() {
		return reporter;
	}

	
	/**
	 * @param reporter 要设置的 reporter。
	 */
	public void setReporter(String reporter) {
		this.reporter = reporter;
	}

	
	/**
	 * @return 返回 driverName。
	 */
	public String getDriverName() {
		return driverName;
	}

	
	/**
	 * @param driverName 要设置的 driverName。
	 */
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	
	/**
	 * @return 返回 licenseNo。
	 */
	public String getLicenseNo() {
		return licenseNo;
	}

	
	/**
	 * @param licenseNo 要设置的 licenseNo。
	 */
	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}

	
	/**
	 * @return 返回 licenseEffecturalDate。
	 */
	public Date getLicenseEffecturalDate() {
		return licenseEffecturalDate;
	}

	
	/**
	 * @param licenseEffecturalDate 要设置的 licenseEffecturalDate。
	 */
	public void setLicenseEffecturalDate(Date licenseEffecturalDate) {
		this.licenseEffecturalDate = licenseEffecturalDate;
	}

	
	/**
	 * @return 返回 personNum。
	 */
	public Double getPersonNum() {
		return personNum;
	}

	
	/**
	 * @param personNum 要设置的 personNum。
	 */
	public void setPersonNum(Double personNum) {
		this.personNum = personNum;
	}

	
	/**
	 * @return 返回 otherAmount。
	 */
	public Double getOtherAmount() {
		return otherAmount;
	}

	
	/**
	 * @param otherAmount 要设置的 otherAmount。
	 */
	public void setOtherAmount(Double otherAmount) {
		this.otherAmount = otherAmount;
	}

	
	/**
	 * @return 返回 subrogationFlag。
	 */
	public String getSubrogationFlag() {
		return subrogationFlag;
	}

	
	/**
	 * @param subrogationFlag 要设置的 subrogationFlag。
	 */
	public void setSubrogationFlag(String subrogationFlag) {
		this.subrogationFlag = subrogationFlag;
	}

	
	/**
	 * @return 返回 istotalloss。
	 */
	public String getIstotalloss() {
		return istotalloss;
	}

	
	/**
	 * @param istotalloss 要设置的 istotalloss。
	 */
	public void setIstotalloss(String istotalloss) {
		this.istotalloss = istotalloss;
	}



	public String getFraudLogo() {
		return fraudLogo;
	}



	public void setFraudLogo(String fraudLogo) {
		this.fraudLogo = fraudLogo;
	}



	public double getFraudRecoverAmount() {
		return fraudRecoverAmount;
	}



	public void setFraudRecoverAmount(double fraudRecoverAmount) {
		this.fraudRecoverAmount = fraudRecoverAmount;
	}

	

}
