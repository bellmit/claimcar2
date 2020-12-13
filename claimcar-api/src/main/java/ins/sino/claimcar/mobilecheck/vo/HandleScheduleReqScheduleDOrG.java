package ins.sino.claimcar.mobilecheck.vo;

import ins.platform.utils.xstreamconverters.SinosoftDateConverter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

/**
 * 手动调度请求接口 - 调度对象（理赔请求快赔）
 * @author zy
 *
 */
@XStreamAlias("SCHEDULEITEM")
public class HandleScheduleReqScheduleDOrG implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;

	/** 报案号 */
	@XStreamAlias("REGISTNO")
	private String registNo;
	
	/** 调度类型 */
	@XStreamAlias("SCHEDULETYPE")
	private String scheduleType;
	
	/** 案件标示1.电话直赔2.微信自助理赔3.移动查勘案件*/
	@XStreamAlias("CASEFLAG")
	private String caseFlag;
	
	/** 订单号 案件标示为2时，必传 */
	@XStreamAlias("ORDERNO")
	private String orderNo;
	
	/** 是否移动端案件 */
	@XStreamAlias("ISMOBILECASE")
	private String isMobileCase;
	
	
	/** 出险时间 */
	@XStreamAlias("DAMAGETIME")
	@XStreamConverter(value = SinosoftDateConverter.class, strings = {"yyyy-MM-dd HH:mm:ss"})
	private Date damageTime;
	
	/** 出险地点 */
	@XStreamAlias("DAMAGEPLACE")
	private String damagePlace;
	
	/** 被保险人姓名 */
	@XStreamAlias("INUREDNAME")
	private String inuredName;
	
	/** 被保人证件类型 */
	@XStreamAlias("IDENTIFYTYPE")
	private String identifyType;
	
	/** 被保人证件号码 */
	@XStreamAlias("IDENTIFYNUMBER")
	private String identifyNumber;
	
	/** 被保险人电话 */
	@XStreamAlias("INUREDPHONE")
	private String inuredPhone;
	
	/** 标的车牌号 */
	@XStreamAlias("LICENSENO")
	private String licenseNo;
	
	/** 联系人姓名 */
	@XStreamAlias("LINKERNAME")
	private String linkerName;
	
	/** 联系人电话 */
	@XStreamAlias("LINKERPHONE")
	private String linkerPhone;
	
	/** 保单号 */
	@XStreamAlias("POLICYNO")
	private String policyNo;
	
	/** 商业险保单号 */
    @XStreamAlias("BUSIPOLICYNO")
    private String busiPolicyNo;
	
	/** 事故车所在地省代码 */
	@XStreamAlias("PROVINCECODE")
	private String provinceCode;

	/** 事故车所在地城市代码 */
	@XStreamAlias("CITYCODE")
	private String cityCode;
	
	/** 事故车所在地区域代码 */
	@XStreamAlias("REGIONCODE")
	private String regionCode;
	
	/** 报案人姓名 */
	@XStreamAlias("REPORTORNAME")
	private String reportorName;
	
	/** 报案人电话 */
	@XStreamAlias("REPORTORPHONE")
	private String reportorPhone;
	
	/** 报案时间 */
	@XStreamAlias("REPORTTIME")
	@XStreamConverter(value = SinosoftDateConverter.class, strings = {"yyyy-MM-dd HH:mm:ss"})
	private Date reportTime;
	
	/** 坐标 */
	@XStreamAlias("LNGXLATY")
	private String lngXlatY;
	
	/** 自定义区域编码 */
	@XStreamAlias("SELFDEFINAREACODE")
	private String selfDefinareaCode;
	
	/** 自助理赔 */
	/** 事故情形 */
	@XStreamAlias("ACCIDENTTYPE")
	private String accidentType;
	
	/** 出险原因 */
	@XStreamAlias("DAMAGECODE")
	private String damageCode;
	
	/** 天气*/
	@XStreamAlias("WEATHER")
	private String weather;
	
	/** 出险经过说明*/
	@XStreamAlias("ACCIDENTDESC")
	private String accidentDesc;
	
	/** 责任类型 */
	@XStreamAlias("DUTYTYPE")
	private String dutyType;
	
	/** 承保公司代码 */
	@XStreamAlias("INSCOMCODE")
	private String insComcode;
	
	/** 承保公司名称*/
	@XStreamAlias("INSCOMPANY")
	private String insCompany;
	
	/** 标的车架号（vin码）*/
	@XStreamAlias("FRAMENO")
	private String frameNo;
	
	/** 标的发动机*/
	@XStreamAlias("ENGINENO")
	private String engineNo;
	
	/** 车主姓名 */
	@XStreamAlias("CAROWNNAME")
	private String carownName;
	
	/** 车主电话 */
	@XStreamAlias("CAROWNPHONE")
	private String carownPhone;
	
	
	/** 业务类型 */
	@XStreamAlias("BUSINESSTYPE")
	private String busiNessType;
	
	/** 客户分类 */
	@XStreamAlias("CUSTOMTYPE")
	private String customType;
	
	/** 代理人编码 */
	@XStreamAlias("AGENTCODE")
	private String agentCode;
	
	/** 保单归属地 */
	@XStreamAlias("COMCODE")
	private String comCode;
	
	/*** 调度对象 */
	@XStreamAlias("TASKLIST")
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

	public String getIdentifyType() {
		return identifyType;
	}

	public void setIdentifyType(String identifyType) {
		this.identifyType = identifyType;
	}

	public String getIdentifyNumber() {
		return identifyNumber;
	}

	public void setIdentifyNumber(String identifyNumber) {
		this.identifyNumber = identifyNumber;
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

	
	
    public String getBusiPolicyNo() {
        return busiPolicyNo;
    }

    
    public void setBusiPolicyNo(String busiPolicyNo) {
        this.busiPolicyNo = busiPolicyNo;
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

	public String getSelfDefinareaCode() {
		return selfDefinareaCode;
	}

	public void setSelfDefinareaCode(String selfDefinareaCode) {
		this.selfDefinareaCode = selfDefinareaCode;
	}

	public String getAccidentType() {
		return accidentType;
	}

	public void setAccidentType(String accidentType) {
		this.accidentType = accidentType;
	}

	public String getDamageCode() {
		return damageCode;
	}

	public void setDamageCode(String damageCode) {
		this.damageCode = damageCode;
	}

	public String getWeather() {
		return weather;
	}

	public void setWeather(String weather) {
		this.weather = weather;
	}

	public String getAccidentDesc() {
		return accidentDesc;
	}

	public void setAccidentDesc(String accidentDesc) {
		this.accidentDesc = accidentDesc;
	}

	public String getDutyType() {
		return dutyType;
	}

	public void setDutyType(String dutyType) {
		this.dutyType = dutyType;
	}

	public String getInsComcode() {
		return insComcode;
	}

	public void setInsComcode(String insComcode) {
		this.insComcode = insComcode;
	}

	public String getInsCompany() {
		return insCompany;
	}

	public void setInsCompany(String insCompany) {
		this.insCompany = insCompany;
	}

	public String getFrameNo() {
		return frameNo;
	}

	public void setFrameNo(String frameNo) {
		this.frameNo = frameNo;
	}

	public String getEngineNo() {
		return engineNo;
	}

	public void setEngineNo(String engineNo) {
		this.engineNo = engineNo;
	}

	public String getCarownName() {
		return carownName;
	}

	public void setCarownName(String carownName) {
		this.carownName = carownName;
	}

	public String getCarownPhone() {
		return carownPhone;
	}

	public void setCarownPhone(String carownPhone) {
		this.carownPhone = carownPhone;
	}

	public String getCaseFlag() {
		return caseFlag;
	}

	public void setCaseFlag(String caseFlag) {
		this.caseFlag = caseFlag;
	}

	

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getBusiNessType() {
		return busiNessType;
	}

	public void setBusiNessType(String busiNessType) {
		this.busiNessType = busiNessType;
	}

	public String getCustomType() {
		return customType;
	}

	public void setCustomType(String customType) {
		this.customType = customType;
	}

	public String getAgentCode() {
		return agentCode;
	}

	public void setAgentCode(String agentCode) {
		this.agentCode = agentCode;
	}

	public String getComCode() {
		return comCode;
	}

	public void setComCode(String comCode) {
		this.comCode = comCode;
	}




	
	
	
}
