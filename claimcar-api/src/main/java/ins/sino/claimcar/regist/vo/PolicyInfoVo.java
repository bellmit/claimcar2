/******************************************************************************
 * CREATETIME : 2015年12月1日 下午5:20:43
 ******************************************************************************/
package ins.sino.claimcar.regist.vo;

import ins.platform.common.apc.annotation.ParamConfig;
import java.util.Date;

/**
 * <pre>保单查询界面vo</pre>
 * @author ★ZhangYu
 */
@ParamConfig
public class PolicyInfoVo implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private String registNo;
	
	@ParamConfig
	private String policyNo;
	private String relatedPolicyNo;
	private String tempRegistFlag;// 0-非临时报案 1-临时报案
	@ParamConfig
	private String licenseNo;
	@ParamConfig
	private String licenseColor;
	private String comCode;
	@ParamConfig
	private String insuredName;
	@ParamConfig
	private String insuredIdNo;
	@ParamConfig
	private String vinNo;
	private String brandName;
	@ParamConfig
	private String frameNo;
	@ParamConfig
	private String engineNo;
	@ParamConfig
	private Date damageTime;
	private Date startDate;
	private Date endDate;
	private String riskType;
	private String validFlag;// 0-无效，1有效，2-未起保
	@ParamConfig
	private String onlyValid;
	private String checkFlag;// 1保单号，2车牌号，3被保险人，4发动机号，5车架号，6VIN码
	private String associatedNo;// 关联保单号
	private String underWriteFlag;// 核保标志
	private String sises;
	private String CallId;//客服系统电话标示
	private int counts;
	private String othFlag;//终保
	private Long startHour;
	private Date startDateHour;
	private Long endHour;
	private Date endDateHour;
	//zhubin 添加用于资助理赔保单绑定的字段
	private String identifyType; //证件类型
	private String identifyNumber;  //证件号码
	private String serviceMobile;//修理厂手机号码
	/**
	 * @return 返回 callId。
	 */
	public String getCallId() {
		return CallId;
	}

	/**
	 * @param callId 要设置的 callId。
	 */
	public void setCallId(String callId) {
		CallId = callId;
	}

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

	public String getRelatedPolicyNo() {
		return relatedPolicyNo;
	}

	public void setRelatedPolicyNo(String relatedPolicyNo) {
		this.relatedPolicyNo = relatedPolicyNo;
	}

	public String getTempRegistFlag() {
		return tempRegistFlag;
	}

	public void setTempRegistFlag(String tempRegistFlag) {
		this.tempRegistFlag = tempRegistFlag;
	}

	public String getLicenseNo() {
		return licenseNo;
	}

	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}

	public String getLicenseColor() {
		return licenseColor;
	}

	public void setLicenseColor(String licenseColor) {
		this.licenseColor = licenseColor;
	}

	public String getComCode() {
		return comCode;
	}

	public void setComCode(String comCode) {
		this.comCode = comCode;
	}

	public String getInsuredName() {
		return insuredName;
	}

	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}

	public String getInsuredIdNo() {
		return insuredIdNo;
	}

	public void setInsuredIdNo(String insuredIdNo) {
		this.insuredIdNo = insuredIdNo;
	}

	public String getVinNo() {
		return vinNo;
	}

	public void setVinNo(String vinNo) {
		this.vinNo = vinNo;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
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

	public Date getDamageTime() {
		return damageTime;
	}

	public void setDamageTime(Date damageTime) {
		this.damageTime = damageTime;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getRiskType() {
		return riskType;
	}

	public void setRiskType(String riskType) {
		this.riskType = riskType;
	}

	public String getValidFlag() {
		return validFlag;
	}

	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}

	public String getOnlyValid() {
		return onlyValid;
	}

	public void setOnlyValid(String onlyValid) {
		this.onlyValid = onlyValid;
	}

	public String getCheckFlag() {
		return checkFlag;
	}

	public void setCheckFlag(String checkFlag) {
		this.checkFlag = checkFlag;
	}

	public String getAssociatedNo() {
		return associatedNo;
	}

	public void setAssociatedNo(String associatedNo) {
		this.associatedNo = associatedNo;
	}

	public String getUnderWriteFlag() {
		return underWriteFlag;
	}

	public void setUnderWriteFlag(String underWriteFlag) {
		this.underWriteFlag = underWriteFlag;
	}

	public String getSises() {
		return sises;
	}

	public void setSises(String sises) {
		this.sises = sises;
	}

	public int getCounts() {
		return counts;
	}

	public void setCounts(int counts) {
		this.counts = counts;
	}

	public String getOthFlag() {
		return othFlag;
	}

	public void setOthFlag(String othFlag) {
		this.othFlag = othFlag;
	}

	public Long getStartHour() {
		return startHour;
	}

	public void setStartHour(Long startHour) {
		this.startHour = startHour;
	}

	public Date getStartDateHour() {
		return startDateHour;
	}

	public void setStartDateHour(Date startDateHour) {
		this.startDateHour = startDateHour;
	}

	public Long getEndHour() {
		return endHour;
	}

	public void setEndHour(Long endHour) {
		this.endHour = endHour;
	}

	public Date getEndDateHour() {
		return endDateHour;
	}

	public void setEndDateHour(Date endDateHour) {
		this.endDateHour = endDateHour;
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

	public String getServiceMobile() {
		return serviceMobile;
	}

	public void setServiceMobile(String serviceMobile) {
		this.serviceMobile = serviceMobile;
	}
	
}
