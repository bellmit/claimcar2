package ins.sino.claimcar.mobile.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * 报案基本信息（快赔请求理赔）
 * @author zjd
 *
 */
@XStreamAlias("BODY")
public class RegistInfoResBodyVo  implements Serializable{
	
	/**
	 * 报案号
	 */
	@XStreamAlias("REGISTNO")
	private String registNo;
	/**
	 * 被保险人
	 */
	@XStreamAlias("INUREDNAME")
	private String inuredName;
	/**
	 * 是否VIP
	 */
	@XStreamAlias("ISVIP")
	private String isVip;
	/**
	 * 驾驶员
	 */
	@XStreamAlias("DRIVERNAME")
	private String driverName;
	/**
	 * 车牌号
	 */
	@XStreamAlias("LICENSENO")
	private String licenseNo;
	/**
	 * 厂牌型号
	 */
	@XStreamAlias("BRANDNAME")
	private String brandName;
	/**
	 * 报案人
	 */
	@XStreamAlias("REPORTORNAME")
	private String reportorName;
	/**
	 * 报案人电话
	 */
	@XStreamAlias("REPORTPHONENUMBER")
	private String reportPhoneNumber;
	/**
	 * 联系人
	 */
	@XStreamAlias("LINKERNAME")
	private String linkerName;
	/**
	 * 联系人电话
	 */
	@XStreamAlias("LINKERPHONENUMBER")
	private String linkerPhoneNumber;
	/**
	 * 案件紧急类型
	 */
	@XStreamAlias("EXIGENCEGREE")
	private String exigenCegree;
	/**
	 * 碰撞部位
	 */
	@XStreamAlias("COLLISIONSITE")
	private String collisionSite;
	/**
	 * 风险提示信息
	 */
	@XStreamAlias("RISKWARNING")
	private String riskWarning;
	/**
	 * 与被保险人关系
	 */
	@XStreamAlias("RELATIONS")
	private String relations;
	/**
	 * 报案时间
	 */
	@XStreamAlias("REPORTDATE")
	private String reportDate;
	/**
	 * 出险时间
	 */
	@XStreamAlias("DAMAGEDATE")
	private String damageDate;
	/**
	 * 出险原因
	 */
	@XStreamAlias("DAMAGECODE")
	private String damageCode;
	/**
	 * 出险地点
	 */
	@XStreamAlias("DAMAGEADDRESS")
	private String damageAddress;
	/**
	 * 出险经过说明
	 */
	@XStreamAlias("ACCIDENTDESC")
	private String accidentDesc;
	/**
	 * 现场报案
	 */
	@XStreamAlias("IsCurrentReport")
	private String isCurrentReport;
	/**
	 * 承保机构编码
	 */
	@XStreamAlias("InsuranceCompanyCode")
	private String inSuranceCompanyCode;
	/**
	 * 承保机构名称
	 */
	@XStreamAlias("InsuranceCompanyName")
	private String inSuranceCompanyName;
	/**
	 * 是否有车损
	 */
	@XStreamAlias("CarLossFlag")
	private String carLossFlag;
	
	public String getRegistNo() {
		return registNo;
	}
	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}
	public String getInuredName() {
		return inuredName;
	}
	public void setInuredName(String inuredName) {
		this.inuredName = inuredName;
	}
	public String getIsVip() {
		return isVip;
	}
	public void setIsVip(String isVip) {
		this.isVip = isVip;
	}
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	public String getLicenseNo() {
		return licenseNo;
	}
	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getReportorName() {
		return reportorName;
	}
	public void setReportorName(String reportorName) {
		this.reportorName = reportorName;
	}
	public String getReportPhoneNumber() {
		return reportPhoneNumber;
	}
	public void setReportPhoneNumber(String reportPhoneNumber) {
		this.reportPhoneNumber = reportPhoneNumber;
	}
	public String getLinkerName() {
		return linkerName;
	}
	public void setLinkerName(String linkerName) {
		this.linkerName = linkerName;
	}
	public String getLinkerPhoneNumber() {
		return linkerPhoneNumber;
	}
	public void setLinkerPhoneNumber(String linkerPhoneNumber) {
		this.linkerPhoneNumber = linkerPhoneNumber;
	}
	public String getExigenCegree() {
		return exigenCegree;
	}
	public void setExigenCegree(String exigenCegree) {
		this.exigenCegree = exigenCegree;
	}
	public String getCollisionSite() {
		return collisionSite;
	}
	public void setCollisionSite(String collisionSite) {
		this.collisionSite = collisionSite;
	}
	public String getRiskWarning() {
		return riskWarning;
	}
	public void setRiskWarning(String riskWarning) {
		this.riskWarning = riskWarning;
	}
	public String getRelations() {
		return relations;
	}
	public void setRelations(String relations) {
		this.relations = relations;
	}
	public String getReportDate() {
		return reportDate;
	}
	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}
	public String getDamageDate() {
		return damageDate;
	}
	public void setDamageDate(String damageDate) {
		this.damageDate = damageDate;
	}
	public String getDamageCode() {
		return damageCode;
	}
	public void setDamageCode(String damageCode) {
		this.damageCode = damageCode;
	}
	public String getDamageAddress() {
		return damageAddress;
	}
	public void setDamageAddress(String damageAddress) {
		this.damageAddress = damageAddress;
	}
	public String getAccidentDesc() {
		return accidentDesc;
	}
	public void setAccidentDesc(String accidentDesc) {
		this.accidentDesc = accidentDesc;
	}
	public String getIsCurrentReport() {
		return isCurrentReport;
	}
	public void setIsCurrentReport(String isCurrentReport) {
		this.isCurrentReport = isCurrentReport;
	}

	public String getInSuranceCompanyCode() {
		return inSuranceCompanyCode;
	}
	public void setInSuranceCompanyCode(String inSuranceCompanyCode) {
		this.inSuranceCompanyCode = inSuranceCompanyCode;
	}
	public String getInSuranceCompanyName() {
		return inSuranceCompanyName;
	}
	public void setInSuranceCompanyName(String inSuranceCompanyName) {
		this.inSuranceCompanyName = inSuranceCompanyName;
	}
	public String getCarLossFlag() {
		return carLossFlag;
	}
	public void setCarLossFlag(String carLossFlag) {
		this.carLossFlag = carLossFlag;
	}
	
	
	
	
}
