package ins.sino.claimcar.platform.vo;

import ins.sino.claimcar.carplatform.adapter.DateXmlAdapter;
import ins.sino.claimcar.carplatform.adapter.DateXmlAdapterDeath;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * 人员损失情况信息
 * 
 * <pre></pre>
 * 
 * @author ★XMSH
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CiLossPersonDataVo {

	@XmlElement(name = "PERSON_NAME", required = true)
	private String personName;// 伤亡人员姓名

	@XmlElement(name = "CERTI_TYPE", required = true)
	private String certiType;// 伤亡人员证件类型；参见代码

	@XmlElement(name = "CERTI_CODE", required = true)
	private String certiCode;// 伤亡人员证件号码

	@XmlElement(name = "PERSON_PROPERTY", required = true)
	private String personProperty;// 人员属性(本车人员/外车人员)；参见代码

	@XmlElement(name = "MEDICAL_TYPE", required = true)
	private String medicalType;// 伤亡人员医疗类型；

	//@XmlJavaTypeAdapter(DateXmlAdapter.class)
	@XmlElement(name = "ADDMISSION_TIME")
	private String addmissionTime;// 入院时间 格式:YYYYMMDD

	@XmlElement(name = "INJURY_TYPE")
	private String injuryType;// 伤情类别代码；参见代码

	@XmlElement(name = "INJURY_LEVEL")
	private String injuryLevel;// 伤残程度

	@XmlElement(name = "UNDER_DEF_LOSS", required = true)
	private String underDefLoss;// 医疗审核金额

	@XmlElement(name = "ESTIMATE_NAME")
	private String estimateName;// 人伤跟踪人员姓名

	@XmlElement(name = "ESTIMATE_CODE")
	private String estimateCode;// 人伤跟踪人员代码

	@XmlElement(name = "UNDER_WRITE_NAME")
	private String underWriteName;// 医疗审核人员姓名

	@XmlElement(name = "UNDER_WRITE_CODE")
	private String underWriteCode;// 医疗审核人员代码

	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	@XmlElement(name = "ESTIMATE_START_TIME", required = true)
	private Date estimateStartTime;// 人伤跟踪开始时间 格式：YYYYMMDDHHMM

	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	@XmlElement(name = "UNDER_END_TIME", required = true)
	private Date underEndTime;// 医疗审核完成时间 格式：YYYYMMDDHHMM

	@XmlElement(name = "ESTIMATE_PLACE")
	private String estimatePlace;// 医疗审核地点

	@XmlElement(name = "ESTIMATE_CERTI_CODE")
	private String estimateCertiCode;// 人伤跟踪人员身份证号码

	@XmlElement(name = "UNDER_WRITE_CERTI_CODE")
	private String underWriteCertiCode;// 医疗审核人员身份证号码

	@XmlJavaTypeAdapter(DateXmlAdapterDeath.class)
	@XmlElement(name = "DEATH_TIME")
	private Date deathTime;// 死亡时间；精确到日

	@XmlElementWrapper(name = "LOSS_FEE_LIST")
	@XmlElement(name = "LOSS_FEE_DATA")
	private List<CiLossPersonLossFeeDataVo> lossPersonLossFeeDataVos;// 人员损失费用明细列表

	@XmlElementWrapper(name = "INJURY_LIST")
	@XmlElement(name = "INJURY_DATA")
	private List<CiLossPersonInjuryDataVo> lossPersonInjuryDataVos;// 受伤部位列表

	@XmlElementWrapper(name = "HOSPITAL_INFO_LIST")
	@XmlElement(name = "HOSPITAL_INFO_DATA")
	private List<CiLossPersonHospitalInfoDataVo> lossPersonHospitalInfoDataVos;// 医院信息列表

	@XmlElementWrapper(name = "INJURY_IDENTIFY_INFO_LIST")
	@XmlElement(name = "INJURY_IDENTIFY_INFO_DATA")
	private List<CiLossPersonInjuryIdentifyInfoDataVo> lossPersonInjuryIdentifyInfoDataVos;// 伤残鉴定机构列表

	/**
	 * @return 返回 personName 伤亡人员姓名
	 */
	public String getPersonName() {
		return personName;
	}

	/**
	 * @param personName 要设置的 伤亡人员姓名
	 */
	public void setPersonName(String personName) {
		this.personName = personName;
	}

	/**
	 * @return 返回 certiType 伤亡人员证件类型；参见代码
	 */
	public String getCertiType() {
		return certiType;
	}

	/**
	 * @param certiType 要设置的 伤亡人员证件类型；参见代码
	 */
	public void setCertiType(String certiType) {
		this.certiType = certiType;
	}

	/**
	 * @return 返回 certiCode 伤亡人员证件号码
	 */
	public String getCertiCode() {
		return certiCode;
	}

	/**
	 * @param certiCode 要设置的 伤亡人员证件号码
	 */
	public void setCertiCode(String certiCode) {
		this.certiCode = certiCode;
	}

	/**
	 * @return 返回 personProperty 人员属性(本车人员/外车人员)；参见代码
	 */
	public String getPersonProperty() {
		return personProperty;
	}

	/**
	 * @param personProperty 要设置的 人员属性(本车人员/外车人员)；参见代码
	 */
	public void setPersonProperty(String personProperty) {
		this.personProperty = personProperty;
	}

	/**
	 * @return 返回 medicalType 伤亡人员医疗类型；
	 */
	public String getMedicalType() {
		return medicalType;
	}

	/**
	 * @param medicalType 要设置的 伤亡人员医疗类型；
	 */
	public void setMedicalType(String medicalType) {
		this.medicalType = medicalType;
	}

	/**
	 * @return 返回 addmissionTime 入院时间 格式:YYYYMMDD
	 */
	public String getAddmissionTime() {
		return addmissionTime;
	}

	/**
	 * @param addmissionTime 要设置的 入院时间 格式:YYYYMMDD
	 */
	public void setAddmissionTime(String addmissionTime) {
		this.addmissionTime = addmissionTime;
	}

	/**
	 * @return 返回 injuryType 伤情类别代码；参见代码
	 */
	public String getInjuryType() {
		return injuryType;
	}

	/**
	 * @param injuryType 要设置的 伤情类别代码；参见代码
	 */
	public void setInjuryType(String injuryType) {
		this.injuryType = injuryType;
	}

	/**
	 * @return 返回 injuryLevel 伤残程度
	 */
	public String getInjuryLevel() {
		return injuryLevel;
	}

	/**
	 * @param injuryLevel 要设置的 伤残程度
	 */
	public void setInjuryLevel(String injuryLevel) {
		this.injuryLevel = injuryLevel;
	}

	/**
	 * @return 返回 underDefLoss 医疗审核金额
	 */
	public String getUnderDefLoss() {
		return underDefLoss;
	}

	/**
	 * @param underDefLoss 要设置的 医疗审核金额
	 */
	public void setUnderDefLoss(String underDefLoss) {
		this.underDefLoss = underDefLoss;
	}

	/**
	 * @return 返回 estimateName 人伤跟踪人员姓名
	 */
	public String getEstimateName() {
		return estimateName;
	}

	/**
	 * @param estimateName 要设置的 人伤跟踪人员姓名
	 */
	public void setEstimateName(String estimateName) {
		this.estimateName = estimateName;
	}

	/**
	 * @return 返回 estimateCode 人伤跟踪人员代码
	 */
	public String getEstimateCode() {
		return estimateCode;
	}

	/**
	 * @param estimateCode 要设置的 人伤跟踪人员代码
	 */
	public void setEstimateCode(String estimateCode) {
		this.estimateCode = estimateCode;
	}

	/**
	 * @return 返回 underWriteName 医疗审核人员姓名
	 */
	public String getUnderWriteName() {
		return underWriteName;
	}

	/**
	 * @param underWriteName 要设置的 医疗审核人员姓名
	 */
	public void setUnderWriteName(String underWriteName) {
		this.underWriteName = underWriteName;
	}

	/**
	 * @return 返回 underWriteCode 医疗审核人员代码
	 */
	public String getUnderWriteCode() {
		return underWriteCode;
	}

	/**
	 * @param underWriteCode 要设置的 医疗审核人员代码
	 */
	public void setUnderWriteCode(String underWriteCode) {
		this.underWriteCode = underWriteCode;
	}

	/**
	 * @return 返回 estimateStartTime 人伤跟踪开始时间 格式：YYYYMMDDHHMM
	 */
	public Date getEstimateStartTime() {
		return estimateStartTime;
	}

	/**
	 * @param estimateStartTime 要设置的 人伤跟踪开始时间 格式：YYYYMMDDHHMM
	 */
	public void setEstimateStartTime(Date estimateStartTime) {
		this.estimateStartTime = estimateStartTime;
	}

	/**
	 * @return 返回 underEndTime 医疗审核完成时间 格式：YYYYMMDDHHMM
	 */
	public Date getUnderEndTime() {
		return underEndTime;
	}

	/**
	 * @param underEndTime 要设置的 医疗审核完成时间 格式：YYYYMMDDHHMM
	 */
	public void setUnderEndTime(Date underEndTime) {
		this.underEndTime = underEndTime;
	}

	/**
	 * @return 返回 estimatePlace 医疗审核地点
	 */
	public String getEstimatePlace() {
		return estimatePlace;
	}

	/**
	 * @param estimatePlace 要设置的 医疗审核地点
	 */
	public void setEstimatePlace(String estimatePlace) {
		this.estimatePlace = estimatePlace;
	}

	/**
	 * @return 返回 estimateCertiCode 人伤跟踪人员身份证号码
	 */
	public String getEstimateCertiCode() {
		return estimateCertiCode;
	}

	/**
	 * @param estimateCertiCode 要设置的 人伤跟踪人员身份证号码
	 */
	public void setEstimateCertiCode(String estimateCertiCode) {
		this.estimateCertiCode = estimateCertiCode;
	}

	/**
	 * @return 返回 underWriteCertiCode 医疗审核人员身份证号码
	 */
	public String getUnderWriteCertiCode() {
		return underWriteCertiCode;
	}

	/**
	 * @param underWriteCertiCode 要设置的 医疗审核人员身份证号码
	 */
	public void setUnderWriteCertiCode(String underWriteCertiCode) {
		this.underWriteCertiCode = underWriteCertiCode;
	}

	/**
	 * @return 返回 deathTime 死亡时间；精确到日
	 */
	public Date getDeathTime() {
		return deathTime;
	}

	/**
	 * @param deathTime 要设置的 死亡时间；精确到日
	 */
	public void setDeathTime(Date deathTime) {
		this.deathTime = deathTime;
	}

	public List<CiLossPersonInjuryDataVo> getLossPersonInjuryDataVos() {
		return lossPersonInjuryDataVos;
	}

	public void setLossPersonInjuryDataVos(List<CiLossPersonInjuryDataVo> lossPersonInjuryDataVos) {
		this.lossPersonInjuryDataVos = lossPersonInjuryDataVos;
	}

	public List<CiLossPersonHospitalInfoDataVo> getLossPersonHospitalInfoDataVos() {
		return lossPersonHospitalInfoDataVos;
	}

	public void setLossPersonHospitalInfoDataVos(List<CiLossPersonHospitalInfoDataVo> lossPersonHospitalInfoDataVos) {
		this.lossPersonHospitalInfoDataVos = lossPersonHospitalInfoDataVos;
	}

	public List<CiLossPersonInjuryIdentifyInfoDataVo> getLossPersonInjuryIdentifyInfoDataVos() {
		return lossPersonInjuryIdentifyInfoDataVos;
	}

	public void setLossPersonInjuryIdentifyInfoDataVos(List<CiLossPersonInjuryIdentifyInfoDataVo> lossPersonInjuryIdentifyInfoDataVos) {
		this.lossPersonInjuryIdentifyInfoDataVos = lossPersonInjuryIdentifyInfoDataVos;
	}

	public List<CiLossPersonLossFeeDataVo> getLossPersonLossFeeDataVos() {
		return lossPersonLossFeeDataVos;
	}

	public void setLossPersonLossFeeDataVos(List<CiLossPersonLossFeeDataVo> lossPersonLossFeeDataVos) {
		this.lossPersonLossFeeDataVos = lossPersonLossFeeDataVos;
	}

}
