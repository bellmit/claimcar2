package ins.sino.claimcar.subrogation.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * 查询页面VO
 * @author ★YangKun
 * @CreateTime 2016年3月18日
 */
public class SubrogationQueryVo implements Serializable{

	
	private static final long serialVersionUID = 1L;
	/** 报案号 */
	private String registNo;
	/** 车牌号 */
	private String licenseNo;
	/** 险种  */
	private String riskCodeSub;
	/** 结算码 */
	private String recoveryCode;
	/** 保单号  */
	private String policyNo;
	/** 机构代码  */
	private String comCode;
	/** 追偿方保险公司  */
	private String insurerCode;
	
	private String areaCode;
	/** 立案号  */
	private String claimCode;
	
	/** 本方理赔编号  **/
	private String claimSequenceNo;
	/** 本方投保确认码 **/
	private String confirmSequenceNo;
	/** 锁定时间起始 **/
	private Date lockedTimeStart;
	/** 锁定时间起始 **/
	private Date lockedTimeEnd;
	
	/**本方追偿状态**/
	private String recoverStatus; 
	/**对方报案号 **/
	private String oppoentRegistNo;
	/**对方保单险种类型 **/
	private String oppentPolicyType;
	/**责任对方车辆车牌号码 **/
	private String oppoentLincenseNo;
	/**责任对方车辆车牌种类  **/
	private String oppoentLincenseType;
	/**责任对方车辆发动机号  **/
	private String oppoentEngineNo;
	/** 责任对方车辆VIN码 **/
	private String oppoentVinNo;
	/** 责任对方保险公司 **/
	private String oppoentInsureCode;
	/**责任对方承保地区  **/
	private String oppoentAreaCode;
	/** 责任对方保单号 **/
	private String oppoentPolicyNo;
	
	
	//待结算查询vo
	/** 结算码状态 **/
	private String recoveryCodeStatus;
	/** 清付险别**/
	private String coverageType;
	/** 追偿金额min **/
	private String recoveryAmountMin;
	/** 追偿金额max **/
	private  String recoveryAmountMax;
	/** 清付金额min **/
	private String payAmountMin;
	/** 清付金额max **/
	private String payAmountMax;
	/** 清付开始时间 **/
	private Date payStartTime;
	/** 清付结束时间 **/
	private Date payEndTime;
	/** 是否跨省**/
	private String acrossProvinceFlag;
	
	
	
	public String getRegistNo() {
		return registNo;
	}
	
	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}
	
	public String getLicenseNo() {
		return licenseNo;
	}
	
	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}
	
	public String getRiskCodeSub() {
		return riskCodeSub;
	}
	
	public void setRiskCodeSub(String riskCodeSub) {
		this.riskCodeSub = riskCodeSub;
	}
	
	public String getRecoveryCode() {
		return recoveryCode;
	}
	
	public void setRecoveryCode(String recoveryCode) {
		this.recoveryCode = recoveryCode;
	}
	
	public String getPolicyNo() {
		return policyNo;
	}
	
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}
	
	public String getComCode() {
		return comCode;
	}
	
	public void setComCode(String comCode) {
		this.comCode = comCode;
	}
	
	public String getInsurerCode() {
		return insurerCode;
	}
	
	public void setInsurerCode(String insurerCode) {
		this.insurerCode = insurerCode;
	}
	
	public String getClaimCode() {
		return claimCode;
	}
	
	public void setClaimCode(String claimCode) {
		this.claimCode = claimCode;
	}
	
	public String getConfirmSequenceNo() {
		return confirmSequenceNo;
	}
	
	public void setConfirmSequenceNo(String confirmSequenceNo) {
		this.confirmSequenceNo = confirmSequenceNo;
	}
	
	public Date getLockedTimeStart() {
		return lockedTimeStart;
	}
	
	public void setLockedTimeStart(Date lockedTimeStart) {
		this.lockedTimeStart = lockedTimeStart;
	}
	
	public Date getLockedTimeEnd() {
		return lockedTimeEnd;
	}
	
	public void setLockedTimeEnd(Date lockedTimeEnd) {
		this.lockedTimeEnd = lockedTimeEnd;
	}
	
	public String getRecoverStatus() {
		return recoverStatus;
	}
	
	public void setRecoverStatus(String recoverStatus) {
		this.recoverStatus = recoverStatus;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	
	public String getOppoentRegistNo() {
		return oppoentRegistNo;
	}

	
	public void setOppoentRegistNo(String oppoentRegistNo) {
		this.oppoentRegistNo = oppoentRegistNo;
	}

	
	public String getOppentPolicyType() {
		return oppentPolicyType;
	}

	
	public void setOppentPolicyType(String oppentPolicyType) {
		this.oppentPolicyType = oppentPolicyType;
	}

	public String getClaimSequenceNo() {
		return claimSequenceNo;
	}

	public void setClaimSequenceNo(String claimSequenceNo) {
		this.claimSequenceNo = claimSequenceNo;
	}

	
	public String getOppoentLincenseNo() {
		return oppoentLincenseNo;
	}

	
	public void setOppoentLincenseNo(String oppoentLincenseNo) {
		this.oppoentLincenseNo = oppoentLincenseNo;
	}

	
	public String getOppoentLincenseType() {
		return oppoentLincenseType;
	}

	
	public void setOppoentLincenseType(String oppoentLincenseType) {
		this.oppoentLincenseType = oppoentLincenseType;
	}

	
	public String getOppoentEngineNo() {
		return oppoentEngineNo;
	}

	
	public void setOppoentEngineNo(String oppoentEngineNo) {
		this.oppoentEngineNo = oppoentEngineNo;
	}

	
	public String getOppoentVinNo() {
		return oppoentVinNo;
	}

	
	public void setOppoentVinNo(String oppoentVinNo) {
		this.oppoentVinNo = oppoentVinNo;
	}

	
	public String getOppoentInsureCode() {
		return oppoentInsureCode;
	}

	
	public void setOppoentInsureCode(String oppoentInsureCode) {
		this.oppoentInsureCode = oppoentInsureCode;
	}

	
	public String getOppoentAreaCode() {
		return oppoentAreaCode;
	}

	
	public void setOppoentAreaCode(String oppoentAreaCode) {
		this.oppoentAreaCode = oppoentAreaCode;
	}

	
	public String getOppoentPolicyNo() {
		return oppoentPolicyNo;
	}

	
	public void setOppoentPolicyNo(String oppoentPolicyNo) {
		this.oppoentPolicyNo = oppoentPolicyNo;
	}
	
	public String getRecoveryAmountMin() {
		return recoveryAmountMin;
	}

	
	public void setRecoveryAmountMin(String recoveryAmountMin) {
		this.recoveryAmountMin = recoveryAmountMin;
	}

	
	public String getRecoveryAmountMax() {
		return recoveryAmountMax;
	}

	
	public void setRecoveryAmountMax(String recoveryAmountMax) {
		this.recoveryAmountMax = recoveryAmountMax;
	}

	
	public String getPayAmountMin() {
		return payAmountMin;
	}

	
	public void setPayAmountMin(String payAmountMin) {
		this.payAmountMin = payAmountMin;
	}

	
	public String getPayAmountMax() {
		return payAmountMax;
	}

	
	public void setPayAmountMax(String payAmountMax) {
		this.payAmountMax = payAmountMax;
	}

	public Date getPayStartTime() {
		return payStartTime;
	}

	
	public void setPayStartTime(Date payStartTime) {
		this.payStartTime = payStartTime;
	}

	
	public Date getPayEndTime() {
		return payEndTime;
	}

	
	public void setPayEndTime(Date payEndTime) {
		this.payEndTime = payEndTime;
	}

	
	public String getAcrossProvinceFlag() {
		return acrossProvinceFlag;
	}

	
	public void setAcrossProvinceFlag(String acrossProvinceFlag) {
		this.acrossProvinceFlag = acrossProvinceFlag;
	}

	public String getRecoveryCodeStatus() {
		return recoveryCodeStatus;
	}

	public void setRecoveryCodeStatus(String recoveryCodeStatus) {
		this.recoveryCodeStatus = recoveryCodeStatus;
	}

	public String getCoverageType() {
		return coverageType;
	}

	public void setCoverageType(String coverageType) {
		this.coverageType = coverageType;
	}
	
	
	
}
