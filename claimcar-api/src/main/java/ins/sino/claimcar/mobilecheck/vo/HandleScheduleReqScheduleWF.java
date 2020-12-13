package ins.sino.claimcar.mobilecheck.vo;

import ins.platform.utils.xstreamconverters.SinosoftDateConverter;

import java.io.Serializable;
import java.util.Date;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

/**
 * 手动调度请求接口-数据部分（理赔请求快赔）
 * @author zy
 *
 */
@XStreamAlias("SCHEDULEWF")
public class HandleScheduleReqScheduleWF implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;

	/** 报案号 */
	@XStreamAlias("REGISTNO")
	private String registNo;
	
	/** 保单号 */
	@XStreamAlias("POLICYNO")
	private String policyNo;
	
	/** 出险时间 */
	@XStreamAlias("DAMAGETIME")
	@XStreamConverter(value = SinosoftDateConverter.class, strings = {"yyyy-MM-dd HH:mm:ss"})
	private Date damageTime;
	
	/** 调度开始时间 */
	@XStreamAlias("SCHEDULETIME")
	@XStreamConverter(value = SinosoftDateConverter.class, strings = {"yyyy-MM-dd"})
	private Date scheduleTime;
	
	/** 报案时间 */
	@XStreamAlias("REPORTTIME")
	@XStreamConverter(value = SinosoftDateConverter.class, strings = {"yyyy-MM-dd"})
	private Date reportTime;
	
	/** 车牌号 */
	@XStreamAlias("LICENSENO")
	private String licenseNo;
	
	/** 被保险人姓名 */
	@XStreamAlias("INUREDNAME")
	private String insuredName;
	
	/** 业务类型 */
	@XStreamAlias("BUSINESSTYPE")
	private String businessType;
	
	/** 客户分类 */
	@XStreamAlias("CUSTOMTYPE")
	private String customType;
	
	/** 案件类型 */
	@XStreamAlias("CASETYPE")
	private String caseType;
	
	/** 是否异地案件 */
	@XStreamAlias("ISELSEWHERE")
	private String isElseWhere;
	
	/** 代理人编码 */
	@XStreamAlias("AGENTCODE")
	private String agentCode;
	
	/** 保单归属地编码 */
	@XStreamAlias("COMCODE")
	private String comCode;
	

	public String getRegistNo() {
		return registNo;
	}

	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public Date getDamageTime() {
		return damageTime;
	}

	public void setDamageTime(Date damageTime) {
		this.damageTime = damageTime;
	}

	public Date getScheduleTime() {
		return scheduleTime;
	}

	public void setScheduleTime(Date scheduleTime) {
		this.scheduleTime = scheduleTime;
	}

	public Date getReportTime() {
		return reportTime;
	}

	public void setReportTime(Date reportTime) {
		this.reportTime = reportTime;
	}

	public String getLicenseNo() {
		return licenseNo;
	}

	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}

	public String getInsuredName() {
		return insuredName;
	}

	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getCustomType() {
		return customType;
	}

	public void setCustomType(String customType) {
		this.customType = customType;
	}

	public String getCaseType() {
		return caseType;
	}

	public void setCaseType(String caseType) {
		this.caseType = caseType;
	}

	public String getIsElseWhere() {
		return isElseWhere;
	}

	public void setIsElseWhere(String isElseWhere) {
		this.isElseWhere = isElseWhere;
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
