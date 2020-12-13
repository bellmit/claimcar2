package ins.sino.claimcar.founder.vo;

import ins.platform.utils.xstreamconverters.SinosoftDateConverter;

import java.util.Date;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

/**
 * 车险报案vo（理赔请求客服系统-请求OUTDATE,体信息）
 * 
 * @author Luwei
 */
@XStreamAlias("OUTDATE")
public class CarRegistReqOutDateVo {

	/** 报案号 **/
	@XStreamAlias("ClmNo")
	private String clmNo;

	/** 保单号 **/
	@XStreamAlias("PolicyNo")
	private String policyNo;

	/** 保单起保日期yyyyMMdd **/
	@XStreamConverter(value = SinosoftDateConverter.class, strings = {"yyyyMMdd"})
	@XStreamAlias("PolicyBeginDate")
	private Date policyBeginDate;

	/** 保单止保日期yyyyMMdd **/
	@XStreamConverter(value = SinosoftDateConverter.class, strings = {"yyyyMMdd"})
	@XStreamAlias("PolicyEndDate")
	private Date policyEndDate;

	/** 关联保单号 **/
	@XStreamAlias("ConnectPolicyNo")
	private String connectPolicyNo;

	/** 联系人姓名 **/
	@XStreamAlias("LinkerName")
	private String linkerName;

	/** 联系人电话 **/
	@XStreamAlias("LinkerPhone")
	private String linkerPhone;

	/** 联系人地址 **/
	@XStreamAlias("LinkerAddress")
	private String linkerAddress;

	/** 报案时间yyyyMMddHHmmss **/
	@XStreamAlias("ReportTime")
	@XStreamConverter(value = SinosoftDateConverter.class, strings = {"yyyyMMddHHmmss"})
	private Date reportTime;

	/** 报损金额 **/
	@XStreamAlias("LossAmount")
	private Double lossAmount;

	/** 出险情况 **/
	@XStreamAlias("Peril")
	private String peril;

	/** 查勘地址 **/
	@XStreamAlias("ExamineAddress")
	private String examineAddress;

	/** 出险时间yyyyMMddHHmmss **/
	@XStreamAlias("DamageTime")
	@XStreamConverter(value = SinosoftDateConverter.class, strings = {"yyyyMMddHHmmss"})
	private Date damageTime;

	/** 车牌号码 **/
	@XStreamAlias("LicenseNo")
	private String licenseNo;

	/** 机构ID **/
	@XStreamAlias("DepartmentNo")
	private String departmentNo;

	/** 出险经过 **/
	@XStreamAlias("AccidentCourse")
	private String accidentCourse;

	/** 股东业务标识 **/
	@XStreamAlias("ShareholderType")
	private boolean shareholderType;

	/** 业务分类 **/
	@XStreamAlias("BusinessType")
	private String businessType;

	/** 其他业务标识 **/
	@XStreamAlias("OtherBusinessType")
	private String otherBusinessType;

	/** 发动机号 **/
	@XStreamAlias("EngineNo")
	private String engineNo;

	/** 车架号 **/
	@XStreamAlias("FrameNo")
	private String frameNo;

	/** 被保险人 **/
	@XStreamAlias("InsuredName")
	private String insuredName;

	/** 厂牌型号 **/
	@XStreamAlias("BrandName")
	private String brandName;

	/** 驾驶人姓名 **/
	@XStreamAlias("DriverName")
	private String driverName;

	/** 已出险次数 **/
	@XStreamAlias("PerilCount")
	private Integer perilCount;

	/** 盗抢定损 **/
	@XStreamAlias("RobberyFlag")
	private String robberyFlag;

	/** 是否涉及人员死亡事故 **/
	@XStreamAlias("IsPersonDeathAccident")
	private String isPersonDeathAccident;

	/** 是否玻璃案件 **/
	@XStreamAlias("IsGlass")
	private String isGlass;

	/** 是否现场案件 **/
	@XStreamAlias("IsScene")
	private String isScene;

	/** 接报案座席ID，单点登录对应的客服座席工号 **/
	@XStreamAlias("AgentId")
	private String agentId;

	/** 事故类型 **/
	@XStreamAlias("DamageTypeCode")
	private String damageTypeCode;

	/** 客服系统电话标示 **/
	@XStreamAlias("CallId")
	private String callId;

	/** 责任认定书 **/
	@XStreamAlias("SubCertiType")
	private String subCertiType;

	/** 是否第一现场报案 **/
	@XStreamAlias("FirstSiteFlag")
	private String firstSiteFlag;

	/** 出险原因 **/
	@XStreamAlias("DamageCode")
	private String damageCode;

	/** 报案开始时间**/
	@XStreamAlias("ReportBeginTime")
	@XStreamConverter(value = SinosoftDateConverter.class, strings = {"yyyyMMddHHmmss"})
	private Date reportBeginTime;
	
	/** 查勘员电话，如果是固话，区号和号码之间不要传“-”。查勘员如果是公估，取核心维护公估相对应的电话号码**/
	@XStreamAlias("ExaminePhone")
	private String examinePhone;
	
	/** 是否自助理赔案件 1-是，0-否**/
	@XStreamAlias("SelfClaimFlag")
	private String selfClaimFlag;
	
	/** 车辆座位数 **/
	@XStreamAlias("SeatNum")
	private String seatNum;
	
	/** 出险地址 **/
	@XStreamAlias("HappenedAddress")
	private String happenedAddress;
	
	public String getSeatNum() {
		return seatNum;
	}

	public void setSeatNum(String seatNum) {
		this.seatNum = seatNum;
	}

	public String getHappenedAddress() {
		return happenedAddress;
	}

	public void setHappenedAddress(String happenedAddress) {
		this.happenedAddress = happenedAddress;
	}

	public String getClmNo() {
		return clmNo;
	}

	public void setClmNo(String clmNo) {
		this.clmNo = clmNo;
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public Date getPolicyBeginDate() {
		return policyBeginDate;
	}

	public void setPolicyBeginDate(Date policyBeginDate) {
		this.policyBeginDate = policyBeginDate;
	}

	public Date getPolicyEndDate() {
		return policyEndDate;
	}

	public void setPolicyEndDate(Date policyEndDate) {
		this.policyEndDate = policyEndDate;
	}

	public String getConnectPolicyNo() {
		return connectPolicyNo;
	}

	public void setConnectPolicyNo(String connectPolicyNo) {
		this.connectPolicyNo = connectPolicyNo;
	}

	public String getLinkerName() {
		return linkerName;
	}

	public void setLinkerName(String linkerName) {
		this.linkerName = linkerName;
	}

	public String getLinkerPhone() {
		return linkerPhone;
	}

	public void setLinkerPhone(String linkerPhone) {
		this.linkerPhone = linkerPhone;
	}

	public String getLinkerAddress() {
		return linkerAddress;
	}

	public void setLinkerAddress(String linkerAddress) {
		this.linkerAddress = linkerAddress;
	}

	public Date getReportTime() {
		return reportTime;
	}

	public void setReportTime(Date reportTime) {
		this.reportTime = reportTime;
	}

	public Double getLossAmount() {
		return lossAmount;
	}

	public void setLossAmount(Double lossAmount) {
		this.lossAmount = lossAmount;
	}

	public String getPeril() {
		return peril;
	}

	public void setPeril(String peril) {
		this.peril = peril;
	}

	public String getExamineAddress() {
		return examineAddress;
	}

	public void setExamineAddress(String examineAddress) {
		this.examineAddress = examineAddress;
	}

	public Date getDamageTime() {
		return damageTime;
	}

	public void setDamageTime(Date damageTime) {
		this.damageTime = damageTime;
	}

	public String getLicenseNo() {
		return licenseNo;
	}

	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}

	public String getDepartmentNo() {
		return departmentNo;
	}

	public void setDepartmentNo(String departmentNo) {
		this.departmentNo = departmentNo;
	}

	public String getAccidentCourse() {
		return accidentCourse;
	}

	public void setAccidentCourse(String accidentCourse) {
		this.accidentCourse = accidentCourse;
	}

	public boolean isShareholderType() {
		return shareholderType;
	}

	public void setShareholderType(boolean shareholderType) {
		this.shareholderType = shareholderType;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getOtherBusinessType() {
		return otherBusinessType;
	}

	public void setOtherBusinessType(String otherBusinessType) {
		this.otherBusinessType = otherBusinessType;
	}

	public String getEngineNo() {
		return engineNo;
	}

	public void setEngineNo(String engineNo) {
		this.engineNo = engineNo;
	}

	public String getFrameNo() {
		return frameNo;
	}

	public void setFrameNo(String frameNo) {
		this.frameNo = frameNo;
	}

	public String getInsuredName() {
		return insuredName;
	}

	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public Integer getPerilCount() {
		return perilCount;
	}

	public void setPerilCount(Integer perilCount) {
		this.perilCount = perilCount;
	}

	public String getRobberyFlag() {
		return robberyFlag;
	}

	public void setRobberyFlag(String robberyFlag) {
		this.robberyFlag = robberyFlag;
	}

	public String getIsPersonDeathAccident() {
		return isPersonDeathAccident;
	}

	public void setIsPersonDeathAccident(String isPersonDeathAccident) {
		this.isPersonDeathAccident = isPersonDeathAccident;
	}

	public String getIsGlass() {
		return isGlass;
	}

	public void setIsGlass(String isGlass) {
		this.isGlass = isGlass;
	}

	public String getIsScene() {
		return isScene;
	}

	public void setIsScene(String isScene) {
		this.isScene = isScene;
	}

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public String getDamageTypeCode() {
		return damageTypeCode;
	}

	public void setDamageTypeCode(String damageTypeCode) {
		this.damageTypeCode = damageTypeCode;
	}

	public String getCallId() {
		return callId;
	}

	public void setCallId(String callId) {
		this.callId = callId;
	}

	/**
	 * @return 返回 subCertiType。
	 */
	public String getSubCertiType() {
		return subCertiType;
	}

	/**
	 * @param subCertiType 要设置的 subCertiType。
	 */
	public void setSubCertiType(String subCertiType) {
		this.subCertiType = subCertiType;
	}

	/**
	 * @return 返回 firstSiteFlag。
	 */
	public String getFirstSiteFlag() {
		return firstSiteFlag;
	}

	/**
	 * @param firstSiteFlag 要设置的 firstSiteFlag。
	 */
	public void setFirstSiteFlag(String firstSiteFlag) {
		this.firstSiteFlag = firstSiteFlag;
	}

	/**
	 * @return 返回 damageCode。
	 */
	public String getDamageCode() {
		return damageCode;
	}

	/**
	 * @param damageCode 要设置的 damageCode。
	 */
	public void setDamageCode(String damageCode) {
		this.damageCode = damageCode;
	}

	

	public Date getReportBeginTime() {
		return reportBeginTime;
	}

	public void setReportBeginTime(Date reportBeginTime) {
		this.reportBeginTime = reportBeginTime;
	}

	public String getExaminePhone() {
		return examinePhone;
	}

	public void setExaminePhone(String examinePhone) {
		this.examinePhone = examinePhone;
	}

	public String getSelfClaimFlag() {
		return selfClaimFlag;
	}

	public void setSelfClaimFlag(String selfClaimFlag) {
		this.selfClaimFlag = selfClaimFlag;
	}

	
}
