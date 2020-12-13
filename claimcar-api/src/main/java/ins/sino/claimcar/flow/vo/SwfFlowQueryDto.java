package ins.sino.claimcar.flow.vo;

import java.io.Serializable;



/**
 * 旧理赔工作流查询条件的Dto,json转换为旧理赔的WorkFlowQueryDto对象
 * @author ★LiuPing
 * @CreateTime 2016年7月25日
 */
public class SwfFlowQueryDto implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;
	/** 报案号 */
	private String registNo="";
	/** 保单号 */
	private String policyNo="";
	/** 险种代码 */
	private String riskCode="";
	/** 车牌号码 */
	private String licenseNo="";
	/** 案件状态 */
	private String caseType;
	/** 被保险人名称 */
	private String insuredName="";
	/** 操作时间 */
	private String operateDate;
	/** 是否注销 */
	private String cancelFlag;
	/** 案件状态 */
	private String status;
	/** 赔案号 */
	private String claimNo="";
	/** 报案号标志 */
	private String registNoSign;
	/** 保单号标志 */
	private String policyNoSign;
	/** 险种标志 */
	private String RiskCodeSign;
	
	/** 险种名称标志 */
	private String riskCodeNameSign;
	
	/** 险种名称 */
	private String riskCodeName;
	
	
	/** 车牌号标志 */
	private String licenseNoSign;
	/** 操作时间标志 */
	private String operateDateSign;
	/** 被保险人标志 */
	private String insuredNameSign;
	/** 赔案号标志 */
	private String claimNoSign;
	/** 赔款计算书号 */
	private String compensateNo;
	/** 赔款计算书号标志 */
	private String compensateNoSign;
	/** 投保人标志 */
	private String appliNameCodeSign;
	/** 核赔标志 */
	private String underWriteFlag;
	/** 归档号 */
	private String caseNo;
	/** 归档号标志 */
	private String caseNoSign;
	/** 承保机构 */
	private String comCode;
	/** 承保机构标志 */
	private String comCodeSign;
	/** 立案时间 */
	private String claimDate;
	/** 立案时间标志 */
	private String claimDateSign;
	/** 委托类型 */
	private String conSignType;
	/** 节点类型 */
	private String nodeType;
	/** 报案注销起始时间 */
	private String registStartCancelDate;
	/** 报案注销结束时间 */
	private String registEndCancelDate;
	
	private String damageStartDate;
	private String damageStartHour;
	
	private String estimateLoss;
	
	private String reportDate;
	
	private String reportDateSign;
	
	private String engineNo;
	
	private String thirdEngineNo;
	
	// 工作流查询查询用
	private String registNoLast="";
	private String policyNoLast="";
	private String claimNoLast="";
	private String licenseNoLast="";
	
	
	private String flowStatus="";
	private String nodeStatus="";
	
	private String estimateLossEnd="";
	private String damageEndDate="";
	private String reportEndDate="";
	
	public String getCompensateNo() {
		return compensateNo;
	}

	public void setCompensateNo(String compensateNo) {
		this.compensateNo = compensateNo;
	}

	public String getCompensateNoSign() {
		return compensateNoSign;
	}

	public void setCompensateNoSign(String compensateNoSign) {
		this.compensateNoSign = compensateNoSign;
	}

	public String getCancelFlag() {
		return cancelFlag;
	}

	public void setCancelFlag(String cancelFlag) {
		this.cancelFlag = cancelFlag;
	}

	public String getOperateDate() {
		return operateDate;
	}

	public void setOperateDate(String operateDate) {
		this.operateDate = operateDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCaseType() {
		return caseType;
	}

	public void setCaseType(String caseType) {
		this.caseType = caseType;
	}

	public String getInsuredName() {
		return insuredName;
	}

	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getRegistNo() {
		return registNo;
	}

	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}

	public String getRiskCode() {
		return riskCode;
	}

	public void setRiskCode(String riskCode) {
		this.riskCode = riskCode;
	}

	public String getLicenseNo() {
		return licenseNo;
	}

	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}

	public String getClaimNo() {
		return claimNo;
	}

	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}

	public String getInsuredNameSign() {
		return insuredNameSign;
	}

	public void setInsuredNameSign(String insuredNameSign) {
		this.insuredNameSign = insuredNameSign;
	}

	public String getLicenseNoSign() {
		return licenseNoSign;
	}

	public void setLicenseNoSign(String licenseNoSign) {
		this.licenseNoSign = licenseNoSign;
	}

	public String getOperateDateSign() {
		return operateDateSign;
	}

	public void setOperateDateSign(String operateDateSign) {
		this.operateDateSign = operateDateSign;
	}

	public String getPolicyNoSign() {
		return policyNoSign;
	}

	public void setPolicyNoSign(String policyNoSign) {
		this.policyNoSign = policyNoSign;
	}

	public String getRegistNoSign() {
		return registNoSign;
	}

	public void setRegistNoSign(String registNoSign) {
		this.registNoSign = registNoSign;
	}

	public String getRiskCodeSign() {
		return RiskCodeSign;
	}

	public void setRiskCodeSign(String riskCodeSign) {
		RiskCodeSign = riskCodeSign;
	}

	

	public String getRiskCodeNameSign() {
		return riskCodeNameSign;
	}

	public void setRiskCodeNameSign(String riskCodeNameSign) {
		this.riskCodeNameSign = riskCodeNameSign;
	}
	
	
	public String getRiskCodeName() {
		return riskCodeName;
	}

	public void setRiskCodeName(String riskCodeName) {
		this.riskCodeName = riskCodeName;
	}
	public String getClaimNoSign() {
		return claimNoSign;
	}

	public void setClaimNoSign(String claimNoSign) {
		this.claimNoSign = claimNoSign;
	}

	public String getUnderWriteFlag() {
		return underWriteFlag;
	}

	public void setUnderWriteFlag(String underWriteFlag) {
		this.underWriteFlag = underWriteFlag;
	}

	public String getCaseNo() {
		return caseNo;
	}

	public void setCaseNo(String caseNo) {
		this.caseNo = caseNo;
	}

	public String getCaseNoSign() {
		return caseNoSign;
	}

	public void setCaseNoSign(String caseNoSign) {
		this.caseNoSign = caseNoSign;
	}

	public String getClaimDate() {
		return claimDate;
	}

	public void setClaimDate(String claimDate) {
		this.claimDate = claimDate;
	}

	public String getClaimDateSign() {
		return claimDateSign;
	}

	public void setClaimDateSign(String claimDateSign) {
		this.claimDateSign = claimDateSign;
	}

	public String getComCode() {
		return comCode;
	}

	public void setComCode(String comCode) {
		this.comCode = comCode;
	}

	public String getComCodeSign() {
		return comCodeSign;
	}

	public void setComCodeSign(String comCodeSign) {
		this.comCodeSign = comCodeSign;
	}

	public String getConSignType() {
		return conSignType;
	}

	public void setConSignType(String conSignType) {
		this.conSignType = conSignType;
	}

	public String getNodeType() {
		return nodeType;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	public String getReportDate() {
		return reportDate;
	}

	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}

	public String getReportDateSign() {
		return reportDateSign;
	}

	public void setReportDateSign(String reportDateSign) {
		this.reportDateSign = reportDateSign;
	}

	public String getRegistEndCancelDate() {
		return registEndCancelDate;
	}

	public void setRegistEndCancelDate(String registEndCancelDate) {
		this.registEndCancelDate = registEndCancelDate;
	}

	public String getRegistStartCancelDate() {
		return registStartCancelDate;
	}

	public void setRegistStartCancelDate(String registStartCancelDate) {
		this.registStartCancelDate = registStartCancelDate;
	}

	public String getAppliNameCodeSign() {
		return appliNameCodeSign;
	}

	public void setAppliNameCodeSign(String appliNameCodeSign) {
		this.appliNameCodeSign = appliNameCodeSign;
	}

	public String getDamageStartDate() {
		return damageStartDate;
	}

	public void setDamageStartDate(String damageStartDate) {
		this.damageStartDate = damageStartDate;
	}

	public String getDamageStartHour() {
		return damageStartHour;
	}

	public void setDamageStartHour(String damageStartHour) {
		this.damageStartHour = damageStartHour;
	}

	public String getEstimateLoss() {
		return estimateLoss;
	}

	public void setEstimateLoss(String estimateLoss) {
		this.estimateLoss = estimateLoss;
	}

	
	/**
	 * @return 返回 registNoLast。
	 */
	public String getRegistNoLast() {
		return registNoLast;
	}

	
	/**
	 * @param registNoLast 要设置的 registNoLast。
	 */
	public void setRegistNoLast(String registNoLast) {
		this.registNoLast = registNoLast;
	}

	
	/**
	 * @return 返回 policyNoLast。
	 */
	public String getPolicyNoLast() {
		return policyNoLast;
	}

	
	/**
	 * @param policyNoLast 要设置的 policyNoLast。
	 */
	public void setPolicyNoLast(String policyNoLast) {
		this.policyNoLast = policyNoLast;
	}

	
	/**
	 * @return 返回 claimNoLast。
	 */
	public String getClaimNoLast() {
		return claimNoLast;
	}

	
	/**
	 * @param claimNoLast 要设置的 claimNoLast。
	 */
	public void setClaimNoLast(String claimNoLast) {
		this.claimNoLast = claimNoLast;
	}

	
	/**
	 * @return 返回 licenseNoLast。
	 */
	public String getLicenseNoLast() {
		return licenseNoLast;
	}

	
	/**
	 * @param licenseNoLast 要设置的 licenseNoLast。
	 */
	public void setLicenseNoLast(String licenseNoLast) {
		this.licenseNoLast = licenseNoLast;
	}

	
	/**
	 * @return 返回 flowStatus。
	 */
	public String getFlowStatus() {
		return flowStatus;
	}

	
	/**
	 * @param flowStatus 要设置的 flowStatus。
	 */
	public void setFlowStatus(String flowStatus) {
		this.flowStatus = flowStatus;
	}

	
	/**
	 * @return 返回 nodeStatus。
	 */
	public String getNodeStatus() {
		return nodeStatus;
	}

	
	/**
	 * @param nodeStatus 要设置的 nodeStatus。
	 */
	public void setNodeStatus(String nodeStatus) {
		this.nodeStatus = nodeStatus;
	}

	
	public String getEstimateLossEnd() {
		return estimateLossEnd;
	}

	
	public void setEstimateLossEnd(String estimateLossEnd) {
		this.estimateLossEnd = estimateLossEnd;
	}

	
	public String getDamageEndDate() {
		return damageEndDate;
	}

	
	public void setDamageEndDate(String damageEndDate) {
		this.damageEndDate = damageEndDate;
	}

	
	public String getReportEndDate() {
		return reportEndDate;
	}

	
	public void setReportEndDate(String reportEndDate) {
		this.reportEndDate = reportEndDate;
	}

	public String getEngineNo() {
		return engineNo;
	}

	public void setEngineNo(String engineNo) {
		this.engineNo = engineNo;
	}

	public String getThirdEngineNo() {
		return thirdEngineNo;
	}

	public void setThirdEngineNo(String thirdEngineNo) {
		this.thirdEngineNo = thirdEngineNo;
	}

	
	
}