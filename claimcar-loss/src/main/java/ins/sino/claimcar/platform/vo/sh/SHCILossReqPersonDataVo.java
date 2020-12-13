/******************************************************************************
 * CREATETIME : 2016年5月26日 下午12:06:32
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import ins.sino.claimcar.carplatform.adapter.DateXmlAdapterDeath;
import ins.sino.claimcar.carplatform.adapter.DateXmlAdapterSH;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * 人员损失情况（多条）
 * @author ★Luwei
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SHCILossReqPersonDataVo {

	/**
	 * 人员损失费用明细（多条）LOSS_PART_LIST（隶属于人员损失情况）
	 */
	@XmlElementWrapper(name = "LOSS_PART_LIST")
	@XmlElement(name = "LOSS_PART_DATA")
	private List<SHCILossReqPersonLossPartDataVo> lossPartDataVo;// 人员损失费用明细（多条）LOSS_PART_LIST（隶属于人员损失情况）

	/**
	 * 人员受伤部位（多条）（隶属于人员损失情况）
	 */
	@XmlElementWrapper(name = "InjuryList")
	@XmlElement(name = "InjuryData")
	private List<SHCILossReqInjuryDataVo> injuryDataVo;// 人员受伤部位（多条）（隶属于人员损失情况）

	/**
	 * 人员治疗机构（多条）（隶属于人员损失情况）
	 */
	@XmlElementWrapper(name = "HospitalInfoList")
	@XmlElement(name = "HospitalInfoData")
	private List<SHCILossReqHospitalInfoDataVo> hospitalInfoDataVo;// 人员受伤部位（多条）（隶属于人员损失情况）

	
	public List<SHCILossReqInjuryIdentifyInfoDataVo> getIdentifyInfoDataVo() {
		return identifyInfoDataVo;
	}

	public void setIdentifyInfoDataVo(
			List<SHCILossReqInjuryIdentifyInfoDataVo> identifyInfoDataVo) {
		this.identifyInfoDataVo = identifyInfoDataVo;
	}

	@XStreamImplicit  //伤残鉴定列表（多条）（隶属于人员损失情况）
	private List<SHCILossReqInjuryIdentifyInfoDataVo>  identifyInfoDataVo;
	
	
	
	@XmlElement(name = "PERSON_NAME", required = true)
	private String personName;// 伤者姓名,指伤者的真实姓名

	@XmlElement(name = "PERSON_ID")
	private String personId;// 身份证号,

	@XmlElement(name = "AGE")
	private Integer age;// 年龄

	@XmlElement(name = "LOSS_AMOUNT", required = true)
	private String lossAmount;// 损失金额,指实际损失金额

	@XmlElement(name = "MAIN_THIRD", required = true)
	private String mainThird;// 是否承保车辆,《公用代码》1.1.52

	@XmlElement(name = "SURVEY_TYPE")
	private String surveyType;// 现场类别,《公用代码》1.1.77

	@XmlElement(name = "SURVEY_NAME")
	private String surveyName;// 查勘人员姓名

	@XmlJavaTypeAdapter(DateXmlAdapterSH.class)
	@XmlElement(name = "SURVEY_START_TIME")
	private Date surveyStartTime;// 查勘开始时间,YYYYMMDDHHMMSS

	@XmlJavaTypeAdapter(DateXmlAdapterSH.class)
	@XmlElement(name = "SURVEY_END_TIME")
	private Date surveyEndTime;// 查勘结束时间,YYYYMMDDHHMMSS

	@XmlElement(name = "SURVEY_PLACE")
	private String surveyPlace;// 查勘地点,

	@XmlElement(name = "SURVEY_DES")
	private String surveyDes;// 查勘情况说明,事故具体描述

	@XmlElement(name = "ESTIMATE_NAME", required = true)
	private String estimateName;// 定损人员代码,《公用代码》1.1.96

	@XmlJavaTypeAdapter(DateXmlAdapterSH.class)
	@XmlElement(name = "ESTIMATE_START_TIME")
	private Date estimateStartTime;// 定损开始时间,YYYYMMDDHHMMSS

	@XmlJavaTypeAdapter(DateXmlAdapterSH.class)
	@XmlElement(name = "ESTIMATE_END_TIME")
	private Date estimateEndTime;// 定损结束时间,YYYYMMDDHHMMSS

	@XmlElement(name = "ASSESOR_NAME")
	private String assesorName;// 核损人员姓名

	@XmlJavaTypeAdapter(DateXmlAdapterSH.class)
	@XmlElement(name = "ASSESOR_START_TIME")
	private Date assesorStartTime;// 核损开始时间,YYYYMMDDHHMMSS

	@XmlJavaTypeAdapter(DateXmlAdapterSH.class)
	@XmlElement(name = "ASSESOR_END_TIME")
	private Date assesorEndTime;// 核损结束时间,YYYYMMDDHHMMSS

	@XmlElement(name = "InjuryType")
	private String injuryType;// 伤情类别,《公用代码》1.1.79

	@XmlElement(name = "InjuryLevel")
	private String injuryLevel;// 伤残程度,《公用代码》1.1.80

	@XmlElement(name = "MedicalType", required = true)
	private String medicalType;// 伤亡人员医疗类型,门诊、住院、死亡《公用代码》1.1.124

	@XmlElement(name = "UnderWriteName")
	private String underWriteName;// 医疗审核人员姓名

	@XmlElement(name = "UnderWriteCode")
	private String underWriteCode;// 医疗审核人员代码

	@XmlElement(name = "EstimateAddr")
	private String estimateAddr;// 医疗审核地点

	@XmlJavaTypeAdapter(DateXmlAdapterSH.class)
	@XmlElement(name = "AddmissionTime")
	private Date addmissionTime;// 入院时间,精确到天@时间说明：保险公司核心系统中伤亡人员入院时间

	@XmlElement(name = "UnderDefLoss", required = true)
	private String underDefloss;// 医疗审核金额,@金额说明：保险公司核心系统中伤亡人员医疗审核通过时的金额
	
	@XmlElement(name = "CheckerCode")
	private String checkerCode;// 查勘人员代码  CheckerCertiCode
	
	
	@XmlElement(name = "CheckerCertiCode")
	private String checkerCertiCode;// 查勘人员身份证
	
	@XmlElement(name = "EstimateCertiCode")
	private String estimateCertiCode;// 人伤跟踪员身份证
	
	@XmlJavaTypeAdapter(DateXmlAdapterDeath.class)
	@XmlElement(name = "DeathTime")
	private Date deathTime;  // 死亡时间
	
	
	@XmlElement(name = "CountryInjuryType")
	private String countryInjuryType;   //全国伤情类别

	/**
	 * @return 返回 personName 伤者姓名,指伤者的真实姓名
	 */
	public String getPersonName() {
		return personName;
	}

	/**
	 * @param personName 要设置的 伤者姓名,指伤者的真实姓名
	 */
	public void setPersonName(String personName) {
		this.personName = personName;
	}

	/**
	 * @return 返回 personId 身份证号,
	 */
	public String getPersonId() {
		return personId;
	}

	/**
	 * @param personId 要设置的 身份证号,
	 */
	public void setPersonId(String personId) {
		this.personId = personId;
	}

	/**
	 * @return 返回 age 年龄
	 */
	public Integer getAge() {
		return age;
	}

	/**
	 * @param age 要设置的 年龄
	 */
	public void setAge(Integer age) {
		this.age = age;
	}

	/**
	 * @return 返回 lossAmount 损失金额,指实际损失金额
	 */
	public String getLossAmount() {
		return lossAmount;
	}

	/**
	 * @param lossAmount 要设置的 损失金额,指实际损失金额
	 */
	public void setLossAmount(String lossAmount) {
		this.lossAmount = lossAmount;
	}

	/**
	 * @return 返回 mainThird 是否承保车辆,《公用代码》1.1.52
	 */
	public String getMainThird() {
		return mainThird;
	}

	/**
	 * @param mainThird 要设置的 是否承保车辆,《公用代码》1.1.52
	 */
	public void setMainThird(String mainThird) {
		this.mainThird = mainThird;
	}

	/**
	 * @return 返回 surveyType 现场类别,《公用代码》1.1.77
	 */
	public String getSurveyType() {
		return surveyType;
	}

	/**
	 * @param surveyType 要设置的 现场类别,《公用代码》1.1.77
	 */
	public void setSurveyType(String surveyType) {
		this.surveyType = surveyType;
	}

	/**
	 * @return 返回 surveyName 查勘人员姓名
	 */
	public String getSurveyName() {
		return surveyName;
	}

	/**
	 * @param surveyName 要设置的 查勘人员姓名
	 */
	public void setSurveyName(String surveyName) {
		this.surveyName = surveyName;
	}

	/**
	 * @return 返回 surveyStartTime 查勘开始时间,YYYYMMDDHHMMSS
	 */
	public Date getSurveyStartTime() {
		return surveyStartTime;
	}

	/**
	 * @param surveyStartTime 要设置的 查勘开始时间,YYYYMMDDHHMMSS
	 */
	public void setSurveyStartTime(Date surveyStartTime) {
		this.surveyStartTime = surveyStartTime;
	}

	/**
	 * @return 返回 surveyEndTime 查勘结束时间,YYYYMMDDHHMMSS
	 */
	public Date getSurveyEndTime() {
		return surveyEndTime;
	}

	/**
	 * @param surveyEndTime 要设置的 查勘结束时间,YYYYMMDDHHMMSS
	 */
	public void setSurveyEndTime(Date surveyEndTime) {
		this.surveyEndTime = surveyEndTime;
	}

	/**
	 * @return 返回 surveyPlace 查勘地点,
	 */
	public String getSurveyPlace() {
		return surveyPlace;
	}

	/**
	 * @param surveyPlace 要设置的 查勘地点,
	 */
	public void setSurveyPlace(String surveyPlace) {
		this.surveyPlace = surveyPlace;
	}

	/**
	 * @return 返回 surveyDes 查勘情况说明,事故具体描述
	 */
	public String getSurveyDes() {
		return surveyDes;
	}

	/**
	 * @param surveyDes 要设置的 查勘情况说明,事故具体描述
	 */
	public void setSurveyDes(String surveyDes) {
		this.surveyDes = surveyDes;
	}

	/**
	 * @return 返回 estimateName 定损人员代码,《公用代码》1.1.96
	 */
	public String getEstimateName() {
		return estimateName;
	}

	/**
	 * @param estimateName 要设置的 定损人员代码,《公用代码》1.1.96
	 */
	public void setEstimateName(String estimateName) {
		this.estimateName = estimateName;
	}

	/**
	 * @return 返回 estimateStartTime 定损开始时间,YYYYMMDDHHMMSS
	 */
	public Date getEstimateStartTime() {
		return estimateStartTime;
	}

	/**
	 * @param estimateStartTime 要设置的 定损开始时间,YYYYMMDDHHMMSS
	 */
	public void setEstimateStartTime(Date estimateStartTime) {
		this.estimateStartTime = estimateStartTime;
	}

	/**
	 * @return 返回 estimateEndTime 定损结束时间,YYYYMMDDHHMMSS
	 */
	public Date getEstimateEndTime() {
		return estimateEndTime;
	}

	/**
	 * @param estimateEndTime 要设置的 定损结束时间,YYYYMMDDHHMMSS
	 */
	public void setEstimateEndTime(Date estimateEndTime) {
		this.estimateEndTime = estimateEndTime;
	}

	/**
	 * @return 返回 assesorName 核损人员姓名
	 */
	public String getAssesorName() {
		return assesorName;
	}

	/**
	 * @param assesorName 要设置的 核损人员姓名
	 */
	public void setAssesorName(String assesorName) {
		this.assesorName = assesorName;
	}

	/**
	 * @return 返回 assesorStartTime 核损开始时间,YYYYMMDDHHMMSS
	 */
	public Date getAssesorStartTime() {
		return assesorStartTime;
	}

	/**
	 * @param assesorStartTime 要设置的 核损开始时间,YYYYMMDDHHMMSS
	 */
	public void setAssesorStartTime(Date assesorStartTime) {
		this.assesorStartTime = assesorStartTime;
	}

	/**
	 * @return 返回 assesorEndTime 核损结束时间,YYYYMMDDHHMMSS
	 */
	public Date getAssesorEndTime() {
		return assesorEndTime;
	}

	/**
	 * @param assesorEndTime 要设置的 核损结束时间,YYYYMMDDHHMMSS
	 */
	public void setAssesorEndTime(Date assesorEndTime) {
		this.assesorEndTime = assesorEndTime;
	}

	/**
	 * @return 返回 injurytype 伤情类别,《公用代码》1.1.79
	 */
	public String getInjuryType() {
		return injuryType;
	}

	/**
	 * @param injurytype 要设置的 伤情类别,《公用代码》1.1.79
	 */
	public void setInjuryType(String injuryType) {
		this.injuryType = injuryType;
	}

	/**
	 * @return 返回 injurylevel 伤残程度,《公用代码》1.1.80
	 */
	public String getInjuryLevel() {
		return injuryLevel;
	}

	/**
	 * @param injurylevel 要设置的 伤残程度,《公用代码》1.1.80
	 */
	public void setInjuryLevel(String injuryLevel) {
		this.injuryLevel = injuryLevel;
	}

	/**
	 * @return 返回 medicaltype 伤亡人员医疗类型,门诊、住院、死亡《公用代码》1.1.124
	 */
	public String getMedicalType() {
		return medicalType;
	}

	/**
	 * @param medicaltype 要设置的 伤亡人员医疗类型,门诊、住院、死亡《公用代码》1.1.124
	 */
	public void setMedicalType(String medicalType) {
		this.medicalType = medicalType;
	}

	/**
	 * @return 返回 underwritename 医疗审核人员姓名
	 */
	public String getUnderWriteName() {
		return underWriteName;
	}

	/**
	 * @param underwritename 要设置的 医疗审核人员姓名
	 */
	public void setUnderWriteName(String underWriteName) {
		this.underWriteName = underWriteName;
	}

	/**
	 * @return 返回 underwritecode 医疗审核人员代码
	 */
	public String getUnderWriteCode() {
		return underWriteCode;
	}

	/**
	 * @param underwritecode 要设置的 医疗审核人员代码
	 */
	public void setUnderWriteCode(String underWriteCode) {
		this.underWriteCode = underWriteCode;
	}

	/**
	 * @return 返回 estimateaddr 医疗审核地点
	 */
	public String getEstimateAddr() {
		return estimateAddr;
	}

	/**
	 * @param estimateaddr 要设置的 医疗审核地点
	 */
	public void setEstimateAddr(String estimateAddr) {
		this.estimateAddr = estimateAddr;
	}

	/**
	 * @return 返回 addmissiontime 入院时间,精确到天@时间说明：保险公司核心系统中伤亡人员入院时间
	 */
	public Date getAddmissionTime() {
		return addmissionTime;
	}

	/**
	 * @param addmissiontime 要设置的 入院时间,精确到天@时间说明：保险公司核心系统中伤亡人员入院时间
	 */
	public void setAddmissionTime(Date addmissionTime) {
		this.addmissionTime = addmissionTime;
	}

	/**
	 * @return 返回 underdefloss 医疗审核金额,@金额说明：保险公司核心系统中伤亡人员医疗审核通过时的金额
	 */
	public String getUnderDefloss() {
		return underDefloss;
	}

	/**
	 * @param underdefloss 要设置的 医疗审核金额,@金额说明：保险公司核心系统中伤亡人员医疗审核通过时的金额
	 */
	public void setUnderDefloss(String underDefloss) {
		this.underDefloss = underDefloss;
	}

	public List<SHCILossReqPersonLossPartDataVo> getLossPartDataVo() {
		return lossPartDataVo;
	}

	public void setLossPartDataVo(List<SHCILossReqPersonLossPartDataVo> lossPartDataVo) {
		this.lossPartDataVo = lossPartDataVo;
	}

	public List<SHCILossReqInjuryDataVo> getInjuryDataVo() {
		return injuryDataVo;
	}

	public void setInjuryDataVo(List<SHCILossReqInjuryDataVo> injuryDataVo) {
		this.injuryDataVo = injuryDataVo;
	}

	public List<SHCILossReqHospitalInfoDataVo> getHospitalInfoDataVo() {
		return hospitalInfoDataVo;
	}

	public void setHospitalInfoDataVo(List<SHCILossReqHospitalInfoDataVo> hospitalInfoDataVo) {
		this.hospitalInfoDataVo = hospitalInfoDataVo;
	}

	public String getCheckerCode() {
		return checkerCode;
	}

	public void setCheckerCode(String checkerCode) {
		this.checkerCode = checkerCode;
	}

	public String getCheckerCertiCode() {
		return checkerCertiCode;
	}

	public void setCheckerCertiCode(String checkerCertiCode) {
		this.checkerCertiCode = checkerCertiCode;
	}

	public String getEstimateCertiCode() {
		return estimateCertiCode;
	}

	public void setEstimateCertiCode(String estimateCertiCode) {
		this.estimateCertiCode = estimateCertiCode;
	}

	public Date getDeathTime() {
		return deathTime;
	}

	public void setDeathTime(Date deathTime) {
		this.deathTime = deathTime;
	}

	public String getCountryInjuryType() {
		return countryInjuryType;
	}

	public void setCountryInjuryType(String countryInjuryType) {
		this.countryInjuryType = countryInjuryType;
	}

}
