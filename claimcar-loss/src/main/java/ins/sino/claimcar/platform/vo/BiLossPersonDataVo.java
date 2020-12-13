package ins.sino.claimcar.platform.vo;

import ins.sino.claimcar.carplatform.adapter.DateXmlAdapter;
import ins.sino.claimcar.carplatform.adapter.DateXmlAdapterDeath;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
public class BiLossPersonDataVo {

	@XmlElement(name = "PersonName", required = true)
	private String personName;//伤亡人员姓名

	@XmlElement(name = "CertiType", required = true)
	private String certiType;//伤亡人员证件类型；参见代码

	@XmlElement(name = "CertiCode", required = true)
	private String certiCode;//伤亡人员证件号码

	@XmlElement(name = "PersonProperty", required = true)
	private String personProperty;//人员属性；参见代码

	@XmlElement(name = "MedicalType", required = true)
	private String medicalType;//伤亡人员医疗类型；参见代码

	//@XmlJavaTypeAdapter(DateXmlAdapter.class)
	@XmlElement(name = "AddmissionTime")
	private String addmissionTime;//入院时间；精确到天

	@XmlElement(name = "InjuryType", required = true)
	private String injuryType;//伤情类别代码；参见代码

	@XmlElement(name = "InjuryLevel")
	private String injuryLevel;//伤残程度

	@XmlElement(name = "UnderDefLoss", required = true)
	private String underDefLoss;//医疗审核金额

	@XmlElement(name = "EstimateName", required = true)
	private String estimateName;//人伤跟踪人员姓名

	@XmlElement(name = "EstimateCode")
	private String estimateCode;//人伤跟踪人员代码

	@XmlElement(name = "EstimateCertiCode", required = true)
	private String estimateCertiCode;//人伤跟踪人员身份证号

	@XmlElement(name = "UnderWriteName")
	private String underWriteName;//医疗审核人员姓名

	@XmlElement(name = "UnderWriteCode")
	private String underWriteCode;//医疗审核人员代码

	@XmlElement(name = "UnderWriteCertiCode")
	private String underWriteCertiCode;//医疗审核人员身份证号

	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	@XmlElement(name = "EstimateStartTime", required = true)
	private Date estimateStartTime;//人伤跟踪开始时间 ；精确到分钟

	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	@XmlElement(name = "UnderEndTime", required = true)
	private Date underEndTime;//医疗审核完成时间 ；精确到分钟

	@XmlElement(name = "EstimateAddr")
	private String estimateAddr;//医疗审核地点

	@XmlElement(name = "LossFeeData")
	private List<BiLossPersonLossFeeDataVo> lossFeeDataVos;//人员损失费用明细列表

	@XmlElement(name = "InjuryData")
	private List<BiLossPersonInjuryDataVo> injuryDataVos;//受伤情况

	@XmlElement(name = "HospitalInfoData")
	private List<BiLossPersonHospitalInfoDataVo> hospitalInfoDataVos;//医疗机构列表

	@XmlElement(name = "InjuryIdentifyInfoData")
	private List<BiLossPersonInjuryIdentifyInfoDataVo> injuryIdentifyInfoDataVos;//伤残鉴定机构列表

	@XmlJavaTypeAdapter(DateXmlAdapterDeath.class)
	@XmlElement(name = "DeathTime")
	private Date deathTime;//死亡时间；精确到日


	/** 
	 * @return 返回 personName  伤亡人员姓名
	 */ 
	public String getPersonName(){ 
	    return personName;
	}

	/** 
	 * @param personName 要设置的 伤亡人员姓名
	 */ 
	public void setPersonName(String personName){ 
	    this.personName=personName;
	}

	/** 
	 * @return 返回 certiType  伤亡人员证件类型；参见代码
	 */ 
	public String getCertiType(){ 
	    return certiType;
	}

	/** 
	 * @param certiType 要设置的 伤亡人员证件类型；参见代码
	 */ 
	public void setCertiType(String certiType){ 
	    this.certiType=certiType;
	}

	/** 
	 * @return 返回 certiCode  伤亡人员证件号码
	 */ 
	public String getCertiCode(){ 
	    return certiCode;
	}

	/** 
	 * @param certiCode 要设置的 伤亡人员证件号码
	 */ 
	public void setCertiCode(String certiCode){ 
	    this.certiCode=certiCode;
	}

	/** 
	 * @return 返回 personProperty  人员属性；参见代码
	 */ 
	public String getPersonProperty(){ 
	    return personProperty;
	}

	/** 
	 * @param personProperty 要设置的 人员属性；参见代码
	 */ 
	public void setPersonProperty(String personProperty){ 
	    this.personProperty=personProperty;
	}

	/** 
	 * @return 返回 medicalType  伤亡人员医疗类型；参见代码
	 */ 
	public String getMedicalType(){ 
	    return medicalType;
	}

	/** 
	 * @param medicalType 要设置的 伤亡人员医疗类型；参见代码
	 */ 
	public void setMedicalType(String medicalType){ 
	    this.medicalType=medicalType;
	}

	/** 
	 * @return 返回 addmissionTime  入院时间；精确到天
	 */ 
	public String getAddmissionTime(){ 
	    return addmissionTime;
	}

	/** 
	 * @param addmissionTime 要设置的 入院时间；精确到天
	 */ 
	public void setAddmissionTime(String addmissionTime){ 
	    this.addmissionTime=addmissionTime;
	}

	/** 
	 * @return 返回 injuryType  伤情类别代码；参见代码
	 */ 
	public String getInjuryType(){ 
	    return injuryType;
	}

	/** 
	 * @param injuryType 要设置的 伤情类别代码；参见代码
	 */ 
	public void setInjuryType(String injuryType){ 
	    this.injuryType=injuryType;
	}

	/** 
	 * @return 返回 injuryLevel  伤残程度
	 */ 
	public String getInjuryLevel(){ 
	    return injuryLevel;
	}

	/** 
	 * @param injuryLevel 要设置的 伤残程度
	 */ 
	public void setInjuryLevel(String injuryLevel){ 
	    this.injuryLevel=injuryLevel;
	}

	/** 
	 * @return 返回 underDefLoss  医疗审核金额
	 */ 
	public String getUnderDefLoss(){ 
	    return underDefLoss;
	}

	/** 
	 * @param underDefLoss 要设置的 医疗审核金额
	 */ 
	public void setUnderDefLoss(String underDefLoss){ 
	    this.underDefLoss=underDefLoss;
	}

	/** 
	 * @return 返回 estimateName  人伤跟踪人员姓名
	 */ 
	public String getEstimateName(){ 
	    return estimateName;
	}

	/** 
	 * @param estimateName 要设置的 人伤跟踪人员姓名
	 */ 
	public void setEstimateName(String estimateName){ 
	    this.estimateName=estimateName;
	}

	/** 
	 * @return 返回 estimateCode  人伤跟踪人员代码
	 */ 
	public String getEstimateCode(){ 
	    return estimateCode;
	}

	/** 
	 * @param estimateCode 要设置的 人伤跟踪人员代码
	 */ 
	public void setEstimateCode(String estimateCode){ 
	    this.estimateCode=estimateCode;
	}

	/** 
	 * @return 返回 estimateCertiCode  人伤跟踪人员身份证号
	 */ 
	public String getEstimateCertiCode(){ 
	    return estimateCertiCode;
	}

	/** 
	 * @param estimateCertiCode 要设置的 人伤跟踪人员身份证号
	 */ 
	public void setEstimateCertiCode(String estimateCertiCode){ 
	    this.estimateCertiCode=estimateCertiCode;
	}

	/** 
	 * @return 返回 underWriteName  医疗审核人员姓名
	 */ 
	public String getUnderWriteName(){ 
	    return underWriteName;
	}

	/** 
	 * @param underWriteName 要设置的 医疗审核人员姓名
	 */ 
	public void setUnderWriteName(String underWriteName){ 
	    this.underWriteName=underWriteName;
	}

	/** 
	 * @return 返回 underWriteCode  医疗审核人员代码
	 */ 
	public String getUnderWriteCode(){ 
	    return underWriteCode;
	}

	/** 
	 * @param underWriteCode 要设置的 医疗审核人员代码
	 */ 
	public void setUnderWriteCode(String underWriteCode){ 
	    this.underWriteCode=underWriteCode;
	}

	/** 
	 * @return 返回 underWriteCertiCode  医疗审核人员身份证号
	 */ 
	public String getUnderWriteCertiCode(){ 
	    return underWriteCertiCode;
	}

	/** 
	 * @param underWriteCertiCode 要设置的 医疗审核人员身份证号
	 */ 
	public void setUnderWriteCertiCode(String underWriteCertiCode){ 
	    this.underWriteCertiCode=underWriteCertiCode;
	}

	/** 
	 * @return 返回 estimateStartTime  人伤跟踪开始时间 ；精确到分钟
	 */ 
	public Date getEstimateStartTime(){ 
	    return estimateStartTime;
	}

	/** 
	 * @param estimateStartTime 要设置的 人伤跟踪开始时间 ；精确到分钟
	 */ 
	public void setEstimateStartTime(Date estimateStartTime){ 
	    this.estimateStartTime=estimateStartTime;
	}

	/** 
	 * @return 返回 underEndTime  医疗审核完成时间 ；精确到分钟
	 */ 
	public Date getUnderEndTime(){ 
	    return underEndTime;
	}

	/** 
	 * @param underEndTime 要设置的 医疗审核完成时间 ；精确到分钟
	 */ 
	public void setUnderEndTime(Date underEndTime){ 
	    this.underEndTime=underEndTime;
	}

	/** 
	 * @return 返回 estimateAddr  医疗审核地点
	 */ 
	public String getEstimateAddr(){ 
	    return estimateAddr;
	}

	/** 
	 * @param estimateAddr 要设置的 医疗审核地点
	 */ 
	public void setEstimateAddr(String estimateAddr){ 
	    this.estimateAddr=estimateAddr;
	}

	/** 
	 * @return 返回 deathTime  死亡时间；精确到日
	 */ 
	public Date getDeathTime(){ 
	    return deathTime;
	}

	/** 
	 * @param deathTime 要设置的 死亡时间；精确到日
	 */ 
	public void setDeathTime(Date deathTime){ 
	    this.deathTime=deathTime;
	}

	
	public List<BiLossPersonLossFeeDataVo> getLossFeeDataVos() {
		return lossFeeDataVos;
	}

	
	public void setLossFeeDataVos(List<BiLossPersonLossFeeDataVo> lossFeeDataVos) {
		this.lossFeeDataVos = lossFeeDataVos;
	}

	
	public List<BiLossPersonInjuryDataVo> getInjuryDataVos() {
		return injuryDataVos;
	}

	
	public void setInjuryDataVos(List<BiLossPersonInjuryDataVo> injuryDataVos) {
		this.injuryDataVos = injuryDataVos;
	}

	
	public List<BiLossPersonHospitalInfoDataVo> getHospitalInfoDataVos() {
		return hospitalInfoDataVos;
	}

	
	public void setHospitalInfoDataVos(List<BiLossPersonHospitalInfoDataVo> hospitalInfoDataVos) {
		this.hospitalInfoDataVos = hospitalInfoDataVos;
	}

	
	public List<BiLossPersonInjuryIdentifyInfoDataVo> getInjuryIdentifyInfoDataVos() {
		return injuryIdentifyInfoDataVos;
	}

	
	public void setInjuryIdentifyInfoDataVos(List<BiLossPersonInjuryIdentifyInfoDataVo> injuryIdentifyInfoDataVos) {
		this.injuryIdentifyInfoDataVos = injuryIdentifyInfoDataVos;
	}



}
