package ins.sino.claimcar.losscar.vo;

import java.io.Serializable;

/**
 * 车辆定损查询结果vo，用于多个查询的查询结果
 * @author ★LiuPing
 * @CreateTime 2016年2月3日
 */
public class CarQueryReslutVo implements Serializable{
	private static final long serialVersionUID = 1L; 
	private Long lossId;// 定损任务号
	private String licenseNo;// 车牌号
	private String mercyFlag;//案件紧急程度
	private String customerLevel;//客户等级
	private String registNo;//报案号
	private String modelName;//车型名称
	private String deflossFlag;
	private String tpFlag;//通赔标示
	private String isMajorCase;//是否重大案件
	private String isSubRogation;//是否代位求偿
	private String isAlarm;//是否报警
	private String isClaimSelf;//是否互碰自赔
	private String insuredName;//被保险人
	

	public String getMercyFlag() {
		return mercyFlag;
	}
	public void setMercyFlag(String mercyFlag) {
		this.mercyFlag = mercyFlag;
	}
	public String getCustomerLevel() {
		return customerLevel;
	}
	public void setCustomerLevel(String customerLevel) {
		this.customerLevel = customerLevel;
	}
	public String getRegistNo() {
		return registNo;
	}
	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}
	
	public Long getLossId() {
		return lossId;
	}
	
	public void setLossId(Long lossId) {
		this.lossId = lossId;
	}
	
	public String getLicenseNo() {
		return licenseNo;
	}
	
	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}
	
	public String getModelName() {
		return modelName;
	}
	
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	
	public String getDeflossFlag() {
		return deflossFlag;
	}
	
	public void setDeflossFlag(String deflossFlag) {
		this.deflossFlag = deflossFlag;
	}
	
	public String getTpFlag() {
		return tpFlag;
	}
	
	public void setTpFlag(String tpFlag) {
		this.tpFlag = tpFlag;
	}
	
	public String getIsMajorCase() {
		return isMajorCase;
	}
	
	public void setIsMajorCase(String isMajorCase) {
		this.isMajorCase = isMajorCase;
	}
	
	public String getIsSubRogation() {
		return isSubRogation;
	}
	
	public void setIsSubRogation(String isSubRogation) {
		this.isSubRogation = isSubRogation;
	}
	
	public String getIsAlarm() {
		return isAlarm;
	}
	
	public void setIsAlarm(String isAlarm) {
		this.isAlarm = isAlarm;
	}
	
	public String getIsClaimSelf() {
		return isClaimSelf;
	}
	
	public void setIsClaimSelf(String isClaimSelf) {
		this.isClaimSelf = isClaimSelf;
	}
	public String getInsuredName() {
		return insuredName;
	}
	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}
	
	

}
