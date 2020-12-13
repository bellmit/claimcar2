package ins.sino.claimcar.trafficplatform.vo;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
@XStreamAlias("PersonData")
public class PersonDataVo implements Serializable{
	
private static final long serialVersionUID = 1L;
	/**
	 * 伤亡人员姓名
	 */
	@XStreamAlias("PersonName")
	private String personName;
	
	/**
	 * 伤亡人员证件类型
	 */
	@XStreamAlias("CertiType")
	private String certiType;
	
	/**
	 * 伤亡人员证件号码
	 */
	@XStreamAlias("CertiCode")
	private String certiCode;
	
	/**
	 * 人员属性
	 */
	@XStreamAlias("PersonProperty")
	private String personProperty;
	
	/**
	 * 伤亡人员医疗类型
	 */
	@XStreamAlias("MedicalType")
	private String medicalType;
	
	/**
	 * 入院时间；精确到天
	 */
	@XStreamAlias("AddmissionTime")
	private String addmissionTime;
	
	/**
	 *伤情类别
	 */
	@XStreamAlias("InjuryType")
	private String injuryType;
	
	/**
	 * 伤残程度
	 */
	@XStreamAlias("InjuryLevel")
	private String injuryLevel;
	
	/**
	 * 死亡时间
	 */
	@XStreamAlias("DeathTime")
	private String deathTime;
	
	/**
	 * 核损金额
	 */
	@XStreamAlias("UnderDefLoss")
	private String underDefLoss;
	
	/**
	 * 人伤跟踪人员姓名
	 */
	@XStreamAlias("EstimateName")
	private String estimateName;
	
	/**
	 * 人伤跟踪代码
	 */
	@XStreamAlias("EstimateCode")
	private String estimateCode;
	
	/**
	 * 人伤跟踪人员身份证号码
	 */
	@XStreamAlias("EstimateCertiCode")
	private String estimateCertiCode;
	
	/**
	 * 医疗审核人员姓名
	 */
	@XStreamAlias("UnderWriteName")
	private String underWriteName;
	
	/**
	 * 医疗审核人员代码
	 */
	@XStreamAlias("UnderWriteCode")
	private String underWriteCode;
	
	/**
	 * 医疗审核人员身份证号码
	 */
	@XStreamAlias("UnderWriteCertiCode")
	private String underWriteCertiCode;
	
	/**
	 * 人伤跟踪开始时间 ；精确到分钟
	 */
	@XStreamAlias("EstimateStartTime")
	private String estimateStartTime;
	
	/**
	 * 医疗审核结束时间 ；精确到分钟
	 */
	@XStreamAlias("UnderEndTime")
	private String underEndTime;
	
	/**
	 * 定损地点
	 */
	@XStreamAlias("EstimateAddr")
	private String estimateAddr;
	/**
	 * 人员损失费用明细列表
	 */
	private List<LossFeeDataVo> LossFeeDataList;
	/**
	 * 受伤情况列表
	 */
	private List<InjuryDataVo> InjuryDataList;
	/**
	 * 医疗机构列表
	 */
	private List<HospitalInfoDataVo> HospitalInfoDataList;
	
	/**
	 * 伤残鉴定机构列表
	 */
	private List<InjuryIdentifyInfoDataVo> injuryIdentifyInfoDataList;//伤残鉴定机构列表
	public String getPersonName() {
		return personName;
	}
	public void setPersonName(String personName) {
		this.personName = personName;
	}
	public String getCertiType() {
		return certiType;
	}
	public void setCertiType(String certiType) {
		this.certiType = certiType;
	}
	public String getCertiCode() {
		return certiCode;
	}
	public void setCertiCode(String certiCode) {
		this.certiCode = certiCode;
	}
	public String getPersonProperty() {
		return personProperty;
	}
	public void setPersonProperty(String personProperty) {
		this.personProperty = personProperty;
	}
	public String getMedicalType() {
		return medicalType;
	}
	public void setMedicalType(String medicalType) {
		this.medicalType = medicalType;
	}
	public String getAddmissionTime() {
		return addmissionTime;
	}
	public void setAddmissionTime(String addmissionTime) {
		this.addmissionTime = addmissionTime;
	}
	public String getInjuryType() {
		return injuryType;
	}
	public void setInjuryType(String injuryType) {
		this.injuryType = injuryType;
	}
	public String getInjuryLevel() {
		return injuryLevel;
	}
	public void setInjuryLevel(String injuryLevel) {
		this.injuryLevel = injuryLevel;
	}
	public String getDeathTime() {
		return deathTime;
	}
	public void setDeathTime(String deathTime) {
		this.deathTime = deathTime;
	}
	public String getUnderDefLoss() {
		return underDefLoss;
	}
	public void setUnderDefLoss(String underDefLoss) {
		this.underDefLoss = underDefLoss;
	}
	public String getEstimateName() {
		return estimateName;
	}
	public void setEstimateName(String estimateName) {
		this.estimateName = estimateName;
	}
	public String getEstimateCode() {
		return estimateCode;
	}
	public void setEstimateCode(String estimateCode) {
		this.estimateCode = estimateCode;
	}
	public String getEstimateCertiCode() {
		return estimateCertiCode;
	}
	public void setEstimateCertiCode(String estimateCertiCode) {
		this.estimateCertiCode = estimateCertiCode;
	}
	public String getUnderWriteName() {
		return underWriteName;
	}
	public void setUnderWriteName(String underWriteName) {
		this.underWriteName = underWriteName;
	}
	public String getUnderWriteCode() {
		return underWriteCode;
	}
	public void setUnderWriteCode(String underWriteCode) {
		this.underWriteCode = underWriteCode;
	}
	public String getUnderWriteCertiCode() {
		return underWriteCertiCode;
	}
	public void setUnderWriteCertiCode(String underWriteCertiCode) {
		this.underWriteCertiCode = underWriteCertiCode;
	}
	public String getEstimateStartTime() {
		return estimateStartTime;
	}
	public void setEstimateStartTime(String estimateStartTime) {
		this.estimateStartTime = estimateStartTime;
	}
	public String getUnderEndTime() {
		return underEndTime;
	}
	public void setUnderEndTime(String underEndTime) {
		this.underEndTime = underEndTime;
	}
	public String getEstimateAddr() {
		return estimateAddr;
	}
	public void setEstimateAddr(String estimateAddr) {
		this.estimateAddr = estimateAddr;
	}
	public List<LossFeeDataVo> getLossFeeDataList() {
		return LossFeeDataList;
	}
	public void setLossFeeDataList(List<LossFeeDataVo> lossFeeDataList) {
		LossFeeDataList = lossFeeDataList;
	}
	public List<InjuryDataVo> getInjuryDataList() {
		return InjuryDataList;
	}
	public void setInjuryDataList(List<InjuryDataVo> injuryDataList) {
		InjuryDataList = injuryDataList;
	}
	public List<HospitalInfoDataVo> getHospitalInfoDataList() {
		return HospitalInfoDataList;
	}
	public void setHospitalInfoDataList(
			List<HospitalInfoDataVo> hospitalInfoDataList) {
		HospitalInfoDataList = hospitalInfoDataList;
	}
	public List<InjuryIdentifyInfoDataVo> getInjuryIdentifyInfoDataList() {
		return injuryIdentifyInfoDataList;
	}
	public void setInjuryIdentifyInfoDataList(
			List<InjuryIdentifyInfoDataVo> injuryIdentifyInfoDataList) {
		this.injuryIdentifyInfoDataList = injuryIdentifyInfoDataList;
	}

	
}
