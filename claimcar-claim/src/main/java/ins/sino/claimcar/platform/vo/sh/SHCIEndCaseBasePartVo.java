/******************************************************************************
 * CREATETIME : 2016年6月6日 下午3:59:57
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
public class SHCIEndCaseBasePartVo {

	@XmlElement(name = "CONFIRM_SEQUENCE_NO")
	private String confirmSequenceNo;// 投保确认码

	@XmlElement(name = "CLAIM_CODE")
	private String claimCode;// 理赔编码

	@XmlElement(name = "CLAIM_AMOUNT")
	private Double claimAmount;// 赔款总金额

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

	@XmlElement(name = "INSURED", required = true)
	private String insured;// 是否属于保险责任

	@XmlElement(name = "CLAIM_TYPE", required = true)
	private String claimType;// 理赔类型

	@XmlElement(name = "PAY_CAUSE", required = true)
	private String payCause;// 垫付原因

	@XmlElement(name = "REFUSE_CAUSE")
	private String refuseCause;// 拒赔原因

	@XmlElement(name = "CAR_MARK")
	private String carMark;// 出险车辆号牌号码

	@XmlElement(name = "VEHICLE_TYPE", required = true)
	private String vehicleType;// 出险车辆号牌种类

	@XmlElement(name = "DRIVER_NAME")
	private String driverName;// 出险驾驶员姓名

	@XmlElement(name = "CERTI_TYPE", required = true)
	private String certiType;// 出险驾驶员证件类型

	@XmlElement(name = "CERTI_CODE")
	private String certiCode;// 出险驾驶员证件号码

	@XmlElement(name = "LICENSE_NO")
	private String licenseNo;// 出险驾驶员档案编号

	@XmlJavaTypeAdapter(DateXmlAdapterSH.class)
	@XmlElement(name = "LICENSE_EFFECTURAL_DATE")
	private Date licenseEffecturalDate;// 出险驾驶员驾驶证有效日期

	@XmlElement(name = "PERSON_NUM")
	private Double personNum;// 事故伤亡人数

	@XmlElement(name = "POLICY_NO")
	private String policyNo;// 保单号码

	@XmlElement(name = "ROAD_ACCIDENT", required = true)
	private String roadAccident;// 是否道路交通事故

	@XmlElement(name = "OTHER_AMOUNT")
	private Double otherAmount;// 理赔费用

	@XmlElement(name = "SUBROGATION_FLAG", required = true)
	private String subrogationFlag;// 是否代位求偿标志

	@XmlJavaTypeAdapter(DateXmlAdapterSH.class)
	@XmlElement(name = "ACCIDENT_TIME")
	private Date accidentTime;// 出险时间

	@XmlElement(name = "ACCIDENT_PLACE")
	private String accidentPlace;// 出险地点

	@XmlElement(name = "ACCIDENT_DESCRIPTION")
	private String accidentDescription;// 出险经过

	@XmlElement(name = "MANAGE_DEPARTMENT", required = true)
	private String manageDepartment;// 事故处理部门

	@XmlElement(name = "LIABILITY_AMOUNT", required = true)
	private String liabilityAmount;// 事故责任划分

	@XmlElement(name = "MANAGE_TYPE", required = true)
	private String manageType;// 事故处理类型

	@XmlElement(name = "ACCIDENT_PLACE_MARK")
	private String accidentPlaceMark;// 出险地点唯一标识
	
	/*牛强  2017-03-15 改*/
	@XmlElement(name="FraudLogo")
	private String fraudLogo; // 欺诈标志
	
	@XmlElement(name="FraudRecoverAmount")
	private double fraudRecoverAmount; // 欺诈玩会损失金额
	
	@XmlElement(name="AccidentReason")
	private String accidentReason; // 事故原因
	
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
	 * @return 返回 claimAmount 赔款总金额
	 */
	public Double getClaimAmount() {
		return claimAmount;
	}

	/**
	 * @param claimAmount 要设置的 赔款总金额
	 */
	public void setClaimAmount(Double claimAmount) {
		this.claimAmount = claimAmount;
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
	 * @return 返回 registrationNo 立案号
	 */
	public String getRegistrationNo() {
		return registrationNo;
	}

	/**
	 * @param registrationNo 要设置的 立案号
	 */
	public void setRegistrationNo(String registrationNo) {
		this.registrationNo = registrationNo;
	}

	/**
	 * @return 返回 claimNo 赔案号
	 */
	public String getClaimNo() {
		return claimNo;
	}

	/**
	 * @param claimNo 要设置的 赔案号
	 */
	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
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
	 * @return 返回 registrationDate 立案时间
	 */
	public Date getRegistrationDate() {
		return registrationDate;
	}

	/**
	 * @param registrationDate 要设置的 立案时间
	 */
	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	/**
	 * @return 返回 endcaseDate 结案时间
	 */
	public Date getEndcaseDate() {
		return endcaseDate;
	}

	/**
	 * @param endcaseDate 要设置的 结案时间
	 */
	public void setEndcaseDate(Date endcaseDate) {
		this.endcaseDate = endcaseDate;
	}

	/**
	 * @return 返回 docStartTime 单证收集开始时间
	 */
	public Date getDocStartTime() {
		return docStartTime;
	}

	/**
	 * @param docStartTime 要设置的 单证收集开始时间
	 */
	public void setDocStartTime(Date docStartTime) {
		this.docStartTime = docStartTime;
	}

	/**
	 * @return 返回 docEndTime 单证收集结束时间
	 */
	public Date getDocEndTime() {
		return docEndTime;
	}

	/**
	 * @param docEndTime 要设置的 单证收集结束时间
	 */
	public void setDocEndTime(Date docEndTime) {
		this.docEndTime = docEndTime;
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
	 * @return 返回 payTime 款项划付时间
	 */
	public Date getPayTime() {
		return payTime;
	}

	/**
	 * @param payTime 要设置的 款项划付时间
	 */
	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	/**
	 * @return 返回 insured 是否属于保险责任
	 */
	public String getInsured() {
		return insured;
	}

	/**
	 * @param insured 要设置的 是否属于保险责任
	 */
	public void setInsured(String insured) {
		this.insured = insured;
	}

	/**
	 * @return 返回 claimType 理赔类型
	 */
	public String getClaimType() {
		return claimType;
	}

	/**
	 * @param claimType 要设置的 理赔类型
	 */
	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}

	/**
	 * @return 返回 payCause 垫付原因
	 */
	public String getPayCause() {
		return payCause;
	}

	/**
	 * @param payCause 要设置的 垫付原因
	 */
	public void setPayCause(String payCause) {
		this.payCause = payCause;
	}

	/**
	 * @return 返回 refuseCause 拒赔原因
	 */
	public String getRefuseCause() {
		return refuseCause;
	}

	/**
	 * @param refuseCause 要设置的 拒赔原因
	 */
	public void setRefuseCause(String refuseCause) {
		this.refuseCause = refuseCause;
	}

	/**
	 * @return 返回 carMark 出险车辆号牌号码
	 */
	public String getCarMark() {
		return carMark;
	}

	/**
	 * @param carMark 要设置的 出险车辆号牌号码
	 */
	public void setCarMark(String carMark) {
		this.carMark = carMark;
	}

	/**
	 * @return 返回 vehicleType 出险车辆号牌种类
	 */
	public String getVehicleType() {
		return vehicleType;
	}

	/**
	 * @param vehicleType 要设置的 出险车辆号牌种类
	 */
	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
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
	public Date getLicenseEffecturalDate() {
		return licenseEffecturalDate;
	}

	/**
	 * @param licenseEffecturalDate 要设置的 出险驾驶员驾驶证有效日期
	 */
	public void setLicenseEffecturalDate(Date licenseEffecturalDate) {
		this.licenseEffecturalDate = licenseEffecturalDate;
	}

	/**
	 * @return 返回 personNum 事故伤亡人数
	 */
	public Double getPersonNum() {
		return personNum;
	}

	/**
	 * @param personNum 要设置的 事故伤亡人数
	 */
	public void setPersonNum(Double personNum) {
		this.personNum = personNum;
	}

	/**
	 * @return 返回 policyNo 保单号码
	 */
	public String getPolicyNo() {
		return policyNo;
	}

	/**
	 * @param policyNo 要设置的 保单号码
	 */
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	/**
	 * @return 返回 roadAccident 是否道路交通事故
	 */
	public String getRoadAccident() {
		return roadAccident;
	}

	/**
	 * @param roadAccident 要设置的 是否道路交通事故
	 */
	public void setRoadAccident(String roadAccident) {
		this.roadAccident = roadAccident;
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
	 * @return 返回 manageDepartment 事故处理部门
	 */
	public String getManageDepartment() {
		return manageDepartment;
	}

	/**
	 * @param manageDepartment 要设置的 事故处理部门
	 */
	public void setManageDepartment(String manageDepartment) {
		this.manageDepartment = manageDepartment;
	}

	/**
	 * @return 返回 liabilityAmount 事故责任划分
	 */
	public String getLiabilityAmount() {
		return liabilityAmount;
	}

	/**
	 * @param liabilityAmount 要设置的 事故责任划分
	 */
	public void setLiabilityAmount(String liabilityAmount) {
		this.liabilityAmount = liabilityAmount;
	}

	/**
	 * @return 返回 manageType 事故处理类型
	 */
	public String getManageType() {
		return manageType;
	}

	/**
	 * @param manageType 要设置的 事故处理类型
	 */
	public void setManageType(String manageType) {
		this.manageType = manageType;
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

	public String getAccidentReason() {
		return accidentReason;
	}

	public void setAccidentReason(String accidentReason) {
		this.accidentReason = accidentReason;
	}

}
