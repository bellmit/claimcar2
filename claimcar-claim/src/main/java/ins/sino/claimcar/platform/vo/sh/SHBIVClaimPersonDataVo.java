/******************************************************************************
 * CREATETIME : 2016年6月1日 下午5:01:05
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

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * @author ★XMSH
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SHBIVClaimPersonDataVo {

	@XmlElement(name = "PERSON_NAME")
	private String personName;// 伤者姓名

	@XmlElement(name = "PERSON_ID")
	private String personId;// 身份证号

	@XmlElement(name = "AGE")
	private Integer age;// 年龄

	@XmlElement(name = "LOSS_AMOUNT")
	private Double lossAmount;// 损失金额

	@XmlElement(name = "MAIN_THIRD", required = true)
	private String mainThird;// 是否承保车辆

	@XmlElement(name = "SURVEY_TYPE", required = true)
	private String surveyType;// 现场类别

	@XmlElement(name = "SURVEY_NAME")
	private String surveyName;// 查勘人员姓名

	@XmlElement(name = "SURVEY_START_TIME")
	@XmlJavaTypeAdapter(DateXmlAdapterSH.class)
	private Date surveyStartTime;// 查勘开始时间

	@XmlElement(name = "SURVEY_END_TIME")
	@XmlJavaTypeAdapter(DateXmlAdapterSH.class)
	private Date surveyEndTime;// 查勘结束时间

	@XmlElement(name = "SURVEY_PLACE")
	private String surveyPlace;// 查勘地点

	@XmlElement(name = "SURVEY_DES")
	private String surveyDes;// 查勘情况说明

	@XmlElement(name = "ESTIMATE_NAME", required = true)
	private String estimateName;// 定损人员代码

	@XmlElement(name = "ESTIMATE_START_TIME")
	@XmlJavaTypeAdapter(DateXmlAdapterSH.class)
	private Date estimateStartTime;// 定损开始时间

	@XmlElement(name = "ESTIMATE_END_TIME")
	@XmlJavaTypeAdapter(DateXmlAdapterSH.class)
	private Date estimateEndTime;// 定损结束时间

	@XmlElement(name = "ASSESOR_NAME")
	private String assesorName;// 核损人员姓名

	@XmlElement(name = "ASSESOR_START_TIME")
	@XmlJavaTypeAdapter(DateXmlAdapterSH.class)
	private Date assesorStartTime;// 核损开始时间

	@XmlElement(name = "ASSESOR_END_TIME")
	@XmlJavaTypeAdapter(DateXmlAdapterSH.class)
	private Date assesorEndTime;// 核损结束时间

	@XmlElement(name = "InjuryType", required = true)
	private String injurytype;// 伤情类别

	@XmlElement(name = "InjuryLevel", required = true)
	private String injurylevel;// 伤残程度

	@XmlElement(name = "MedicalType")
	private String medicaltype;// 伤亡人员医疗类型

	@XmlElement(name = "UnderWriteName")
	private String underwritename;// 医疗审核人员姓名

	@XmlElement(name = "UnderWriteCode")
	private String underwritecode;// 医疗审核人员代码

	@XmlElement(name = "EstimateAddr")
	private String estimateaddr;// 医疗审核地点

	@XmlElement(name = "AddmissionTime")
	@XmlJavaTypeAdapter(DateXmlAdapterSH.class)
	private Date addmissiontime;// 入院时间

	@XmlElement(name = "UnderDefLoss")
	private Double underdefloss;// 医疗审核金额
	
	/*牛强  2017-03-15 改*/
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
	

	@XmlElementWrapper(name = "LOSS_PART_LIST")
	@XmlElement(name = "LOSS_PART_DATA")
	private List<SHBIVClaimPersLossPartDataVo> lossPartList;

	@XmlElementWrapper(name = "InjuryList")
	@XmlElement(name = "InjuryData")
	private List<SHBIVClaimInjuryDataVo> injuryList;

	@XmlElementWrapper(name = "HospitalInfoList")
	@XmlElement(name = "HospitalInfoData")
	private List<SHBIVClaimHospitalInfoDataVo> hospitalInfoList;  
	
	//伤残鉴定列表（多条）（隶属于人员损失情况）
	@XmlElementWrapper(name = "InjuryIdentifyInfoList")
	@XStreamAlias("InjuryIdentifyInfoData")
	private List<SHCIVClaimInjuryIdentifyInfoDataVo> injuryIdentifyInfoData;

	/**
	 * @return 返回 personName 伤者姓名
	 */
	public String getPersonName() {
		return personName;
	}

	/**
	 * @param personName 要设置的 伤者姓名
	 */
	public void setPersonName(String personName) {
		this.personName = personName;
	}

	/**
	 * @return 返回 personId 身份证号
	 */
	public String getPersonId() {
		return personId;
	}

	/**
	 * @param personId 要设置的 身份证号
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
	 * @return 返回 lossAmount 损失金额
	 */
	public Double getLossAmount() {
		return lossAmount;
	}

	/**
	 * @param lossAmount 要设置的 损失金额
	 */
	public void setLossAmount(Double lossAmount) {
		this.lossAmount = lossAmount;
	}

	/**
	 * @return 返回 mainThird 是否承保车辆
	 */
	public String getMainThird() {
		return mainThird;
	}

	/**
	 * @param mainThird 要设置的 是否承保车辆
	 */
	public void setMainThird(String mainThird) {
		this.mainThird = mainThird;
	}

	/**
	 * @return 返回 surveyType 现场类别
	 */
	public String getSurveyType() {
		return surveyType;
	}

	/**
	 * @param surveyType 要设置的 现场类别
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
	 * @return 返回 surveyStartTime 查勘开始时间
	 */
	public Date getSurveyStartTime() {
		return surveyStartTime;
	}

	/**
	 * @param surveyStartTime 要设置的 查勘开始时间
	 */
	public void setSurveyStartTime(Date surveyStartTime) {
		this.surveyStartTime = surveyStartTime;
	}

	/**
	 * @return 返回 surveyEndTime 查勘结束时间
	 */
	public Date getSurveyEndTime() {
		return surveyEndTime;
	}

	/**
	 * @param surveyEndTime 要设置的 查勘结束时间
	 */
	public void setSurveyEndTime(Date surveyEndTime) {
		this.surveyEndTime = surveyEndTime;
	}

	/**
	 * @return 返回 surveyPlace 查勘地点
	 */
	public String getSurveyPlace() {
		return surveyPlace;
	}

	/**
	 * @param surveyPlace 要设置的 查勘地点
	 */
	public void setSurveyPlace(String surveyPlace) {
		this.surveyPlace = surveyPlace;
	}

	/**
	 * @return 返回 surveyDes 查勘情况说明
	 */
	public String getSurveyDes() {
		return surveyDes;
	}

	/**
	 * @param surveyDes 要设置的 查勘情况说明
	 */
	public void setSurveyDes(String surveyDes) {
		this.surveyDes = surveyDes;
	}

	/**
	 * @return 返回 estimateName 定损人员代码
	 */
	public String getEstimateName() {
		return estimateName;
	}

	/**
	 * @param estimateName 要设置的 定损人员代码
	 */
	public void setEstimateName(String estimateName) {
		this.estimateName = estimateName;
	}

	/**
	 * @return 返回 estimateStartTime 定损开始时间
	 */
	public Date getEstimateStartTime() {
		return estimateStartTime;
	}

	/**
	 * @param estimateStartTime 要设置的 定损开始时间
	 */
	public void setEstimateStartTime(Date estimateStartTime) {
		this.estimateStartTime = estimateStartTime;
	}

	/**
	 * @return 返回 estimateEndTime 定损结束时间
	 */
	public Date getEstimateEndTime() {
		return estimateEndTime;
	}

	/**
	 * @param estimateEndTime 要设置的 定损结束时间
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
	 * @return 返回 assesorStartTime 核损开始时间
	 */
	public Date getAssesorStartTime() {
		return assesorStartTime;
	}

	/**
	 * @param assesorStartTime 要设置的 核损开始时间
	 */
	public void setAssesorStartTime(Date assesorStartTime) {
		this.assesorStartTime = assesorStartTime;
	}

	/**
	 * @return 返回 assesorEndTime 核损结束时间
	 */
	public Date getAssesorEndTime() {
		return assesorEndTime;
	}

	/**
	 * @param assesorEndTime 要设置的 核损结束时间
	 */
	public void setAssesorEndTime(Date assesorEndTime) {
		this.assesorEndTime = assesorEndTime;
	}

	/**
	 * @return 返回 injurytype 伤情类别
	 */
	public String getInjurytype() {
		return injurytype;
	}

	/**
	 * @param injurytype 要设置的 伤情类别
	 */
	public void setInjurytype(String injurytype) {
		this.injurytype = injurytype;
	}

	/**
	 * @return 返回 injurylevel 伤残程度
	 */
	public String getInjurylevel() {
		return injurylevel;
	}

	/**
	 * @param injurylevel 要设置的 伤残程度
	 */
	public void setInjurylevel(String injurylevel) {
		this.injurylevel = injurylevel;
	}

	/**
	 * @return 返回 medicaltype 伤亡人员医疗类型
	 */
	public String getMedicaltype() {
		return medicaltype;
	}

	/**
	 * @param medicaltype 要设置的 伤亡人员医疗类型
	 */
	public void setMedicaltype(String medicaltype) {
		this.medicaltype = medicaltype;
	}

	/**
	 * @return 返回 underwritename 医疗审核人员姓名
	 */
	public String getUnderwritename() {
		return underwritename;
	}

	/**
	 * @param underwritename 要设置的 医疗审核人员姓名
	 */
	public void setUnderwritename(String underwritename) {
		this.underwritename = underwritename;
	}

	/**
	 * @return 返回 underwritecode 医疗审核人员代码
	 */
	public String getUnderwritecode() {
		return underwritecode;
	}

	/**
	 * @param underwritecode 要设置的 医疗审核人员代码
	 */
	public void setUnderwritecode(String underwritecode) {
		this.underwritecode = underwritecode;
	}

	/**
	 * @return 返回 estimateaddr 医疗审核地点
	 */
	public String getEstimateaddr() {
		return estimateaddr;
	}

	/**
	 * @param estimateaddr 要设置的 医疗审核地点
	 */
	public void setEstimateaddr(String estimateaddr) {
		this.estimateaddr = estimateaddr;
	}

	/**
	 * @return 返回 addmissiontime 入院时间
	 */
	public Date getAddmissiontime() {
		return addmissiontime;
	}

	/**
	 * @param addmissiontime 要设置的 入院时间
	 */
	public void setAddmissiontime(Date addmissiontime) {
		this.addmissiontime = addmissiontime;
	}

	/**
	 * @return 返回 underdefloss 医疗审核金额
	 */
	public Double getUnderdefloss() {
		return underdefloss;
	}

	/**
	 * @param underdefloss 要设置的 医疗审核金额
	 */
	public void setUnderdefloss(Double underdefloss) {
		this.underdefloss = underdefloss;
	}

	/**
	 * @return 返回 lossPartList。
	 */
	public List<SHBIVClaimPersLossPartDataVo> getLossPartList() {
		return lossPartList;
	}

	/**
	 * @param lossPartList 要设置的 lossPartList。
	 */
	public void setLossPartList(List<SHBIVClaimPersLossPartDataVo> lossPartList) {
		this.lossPartList = lossPartList;
	}

	/**
	 * @return 返回 injuryList。
	 */
	public List<SHBIVClaimInjuryDataVo> getInjuryList() {
		return injuryList;
	}

	/**
	 * @param injuryList 要设置的 injuryList。
	 */
	public void setInjuryList(List<SHBIVClaimInjuryDataVo> injuryList) {
		this.injuryList = injuryList;
	}

	/**
	 * @return 返回 hospitalInfoList。
	 */
	public List<SHBIVClaimHospitalInfoDataVo> getHospitalInfoList() {
		return hospitalInfoList;
	}

	/**
	 * @param hospitalInfoList 要设置的 hospitalInfoList。
	 */
	public void setHospitalInfoList(List<SHBIVClaimHospitalInfoDataVo> hospitalInfoList) {
		this.hospitalInfoList = hospitalInfoList;
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

	public List<SHCIVClaimInjuryIdentifyInfoDataVo> getInjuryIdentifyInfoData() {
		return injuryIdentifyInfoData;
	}

	public void setInjuryIdentifyInfoData(
			List<SHCIVClaimInjuryIdentifyInfoDataVo> injuryIdentifyInfoData) {
		this.injuryIdentifyInfoData = injuryIdentifyInfoData;
	}

}
