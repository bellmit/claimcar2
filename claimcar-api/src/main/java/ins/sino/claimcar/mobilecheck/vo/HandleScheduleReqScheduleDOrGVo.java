package ins.sino.claimcar.mobilecheck.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 手动调度请求接口 - 调度对象（理赔请求快赔）
 * @author zy
 *
 */
public class HandleScheduleReqScheduleDOrGVo implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;

	/** 报案号 */
	private String registNo;
	
	/** 调度类型 */
	private String scheduleType;
	
	/** 是否移动端案件 */
	private String isMobileCase;
	
	
	/** 出险时间 */
	private Date damageTime;
	
	/** 出险地点 */
	private String damagePlace;
	
	/** 被保险人姓名 */
	private String inuredName;
	
	/** 被保险人电话 */
	private String inuredPhone;
	
	/** 标的车牌号 */
	private String licenseNo;
	
	/** 联系人姓名 */
	private String linkerName;
	
	/** 联系人电话 */
	private String linkerPhone;
	
	/** 保单号 */
	private String policyNo;
	
	/** 事故车所在地省代码 */
	private String provinceCode;

	/** 事故车所在地城市代码 */
	private String cityCode;
	
	/** 事故车所在地区域代码 */
	private String regionCode;
	
	/** 报案人姓名 */
	private String reportorName;
	
	/** 报案人电话 */
	private String reportorPhone;

	
	/** 报案时间 */
	private Date reportTime;
	
	/** 坐标 */
	private String lngXlatY;
	
	/*** 调度对象 */
	private List<HandleScheduleReqScheduleItemDOrG> scheduleItemList;

	public String getRegistNo() {
		return registNo;
	}

	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}

	public String getScheduleType() {
		return scheduleType;
	}

	public void setScheduleType(String scheduleType) {
		this.scheduleType = scheduleType;
	}

	public String getIsMobileCase() {
		return isMobileCase;
	}

	public void setIsMobileCase(String isMobileCase) {
		this.isMobileCase = isMobileCase;
	}

	public Date getDamageTime() {
		return damageTime;
	}

	public void setDamageTime(Date damageTime) {
		this.damageTime = damageTime;
	}

	public String getDamagePlace() {
		return damagePlace;
	}

	public void setDamagePlace(String damagePlace) {
		this.damagePlace = damagePlace;
	}

	public String getInuredName() {
		return inuredName;
	}

	public void setInuredName(String inuredName) {
		this.inuredName = inuredName;
	}

	public String getInuredPhone() {
		return inuredPhone;
	}

	public void setInuredPhone(String inuredPhone) {
		this.inuredPhone = inuredPhone;
	}

	public String getLicenseNo() {
		return licenseNo;
	}

	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
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

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getRegionCode() {
		return regionCode;
	}

	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}

	public String getReportorName() {
		return reportorName;
	}

	public void setReportorName(String reportorName) {
		this.reportorName = reportorName;
	}

	public String getReportorPhone() {
		return reportorPhone;
	}

	public void setReportorPhone(String reportorPhone) {
		this.reportorPhone = reportorPhone;
	}

	public Date getReportTime() {
		return reportTime;
	}

	public void setReportTime(Date reportTime) {
		this.reportTime = reportTime;
	}

	public String getLngXlatY() {
		return lngXlatY;
	}

	public void setLngXlatY(String lngXlatY) {
		this.lngXlatY = lngXlatY;
	}

	public List<HandleScheduleReqScheduleItemDOrG> getScheduleItemList() {
		return scheduleItemList;
	}

	public void setScheduleItemList(
			List<HandleScheduleReqScheduleItemDOrG> scheduleItemList) {
		this.scheduleItemList = scheduleItemList;
	}


	
	
	
}
