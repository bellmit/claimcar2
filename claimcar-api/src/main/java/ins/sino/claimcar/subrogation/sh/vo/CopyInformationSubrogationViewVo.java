package ins.sino.claimcar.subrogation.sh.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 代位求偿信息列表（多条）SubrogationList
 * @author ★Luwei
 */
public class CopyInformationSubrogationViewVo implements Serializable {

	/** */
	private static final long serialVersionUID = 1L;

	private List<CopyInformationCheckListVo> checkListVo;
	
	private String recoveryCode;// 清算码

	/** 1-待互审,2-无需互审,3-互审中,4-待清算,5-已支付,6-零清算,9-失效,10-待支付,11-延期,12-暂缓支付 */
	private String recoveryCodeStatus;// 清算码状态

	private Date failureTime;// 清算码失效时间

	/**
	 * 1-放弃追偿,2-锁定错误, 3-系统自动失效(非保险公司通过“锁定取消”主动选择，由其他功能引发的平台系统自动置为失效，例如：放弃追偿、案件注销等)
	 */
	private String failureCause;// 清算码失效原因代码

	private Date lockedTime;// 锁定时间

	private Date recoveryStartTime;// 开始追偿时间

	private String coverageCode;// 追偿/清付险种

	private String recoverAmount;// 追偿金额-追偿方发起追偿的金额

	private String compensateAmount;// 清付金额-被追偿方清付的金额

	// eq:AAIC01-安信农业保险公司,ABIC01-安邦保险公司,ACIC01-安诚保险公司
	private String insurerCode;// 追偿方保险公司

	private String insurerArea;// 追偿方承保地区

	/** 1-强制三者险,2-商业三者险,3-商业车损险,9-其它 */
	private String coverageType;// 追偿方保单险种类型

	private String policyNo;// 追偿方保单号

	private String claimNotificationNo;// 追偿方报案号

	/**
	 * 01-已报案,02-报案注销,03-已立案,04-立案注销,05 -已结案,10-拒赔,11-查勘/定损/核损, 11-重开,12-单证收集,13-厘算核赔,14-赔款支付,15-查勘/定损/核损注销,16-单证收集注销,17-厘算核赔注销
	 */
	private String claimStatus;// 追偿方案件状态

	// 同上
	private String claimProgress;// 追偿方案件进展

	private String licensePlateNo;// 追偿方车辆号牌号码

	private String licensePlateType;// 追偿方车辆号牌种类

	private String engineNo;// 追偿方车辆发动机号

	private String vin;// 追偿方车辆VIN码

	private String berecoveryInsurerCode;// 被追偿方保险公司

	private String berecoveryInsurerArea;// 被追偿方承保地区

	private String berecoveryCoverageType;// 被追偿方保单险种类型

	private String berecoveryPolicyNo;// 被追偿方保单号

	private String berecoveryClaimNotificationNo;// 被追偿方报案号

	private String berecoveryClaimStatus;// 被追偿方案件状态

	private String berecoveryClaimProgress;// 被追偿方案件进展

	private String berecoveryLicensePlateNo;// 被追偿方车辆号牌号码

	private String berecoveryLicensePlateType;// 被追偿方车辆号牌种类

	private String berecoveryEngineNo;// 被追偿方车辆发动机号

	private String berecoveryVin;// 被追偿方车辆VIN码

	
	//setters and getters
	
	public String getRecoveryCode() {
		return recoveryCode;
	}

	public void setRecoveryCode(String recoveryCode) {
		this.recoveryCode = recoveryCode;
	}

	public String getRecoveryCodeStatus() {
		return recoveryCodeStatus;
	}

	public void setRecoveryCodeStatus(String recoveryCodeStatus) {
		this.recoveryCodeStatus = recoveryCodeStatus;
	}

	public Date getFailureTime() {
		return failureTime;
	}

	public void setFailureTime(Date failureTime) {
		this.failureTime = failureTime;
	}

	public String getFailureCause() {
		return failureCause;
	}

	public void setFailureCause(String failureCause) {
		this.failureCause = failureCause;
	}

	public Date getLockedTime() {
		return lockedTime;
	}

	public void setLockedTime(Date lockedTime) {
		this.lockedTime = lockedTime;
	}

	public Date getRecoveryStartTime() {
		return recoveryStartTime;
	}

	public void setRecoveryStartTime(Date recoveryStartTime) {
		this.recoveryStartTime = recoveryStartTime;
	}

	public String getCoverageCode() {
		return coverageCode;
	}

	public void setCoverageCode(String coverageCode) {
		this.coverageCode = coverageCode;
	}

	public String getRecoverAmount() {
		return recoverAmount;
	}

	public void setRecoverAmount(String recoverAmount) {
		this.recoverAmount = recoverAmount;
	}

	public String getCompensateAmount() {
		return compensateAmount;
	}

	public void setCompensateAmount(String compensateAmount) {
		this.compensateAmount = compensateAmount;
	}

	public String getInsurerCode() {
		return insurerCode;
	}

	public void setInsurerCode(String insurerCode) {
		this.insurerCode = insurerCode;
	}

	public String getInsurerArea() {
		return insurerArea;
	}

	public void setInsurerArea(String insurerArea) {
		this.insurerArea = insurerArea;
	}

	public String getCoverageType() {
		return coverageType;
	}

	public void setCoverageType(String coverageType) {
		this.coverageType = coverageType;
	}

	

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getClaimNotificationNo() {
		return claimNotificationNo;
	}

	public void setClaimNotificationNo(String claimNotificationNo) {
		this.claimNotificationNo = claimNotificationNo;
	}

	public String getClaimStatus() {
		return claimStatus;
	}

	public void setClaimStatus(String claimStatus) {
		this.claimStatus = claimStatus;
	}

	public String getClaimProgress() {
		return claimProgress;
	}

	public void setClaimProgress(String claimProgress) {
		this.claimProgress = claimProgress;
	}

	public String getLicensePlateNo() {
		return licensePlateNo;
	}

	public void setLicensePlateNo(String licensePlateNo) {
		this.licensePlateNo = licensePlateNo;
	}

	public String getLicensePlateType() {
		return licensePlateType;
	}

	public void setLicensePlateType(String licensePlateType) {
		this.licensePlateType = licensePlateType;
	}

	public String getEngineNo() {
		return engineNo;
	}

	public void setEngineNo(String engineNo) {
		this.engineNo = engineNo;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public String getBerecoveryInsurerCode() {
		return berecoveryInsurerCode;
	}

	public void setBerecoveryInsurerCode(String berecoveryInsurerCode) {
		this.berecoveryInsurerCode = berecoveryInsurerCode;
	}

	public String getBerecoveryInsurerArea() {
		return berecoveryInsurerArea;
	}

	public void setBerecoveryInsurerArea(String berecoveryInsurerArea) {
		this.berecoveryInsurerArea = berecoveryInsurerArea;
	}

	public String getBerecoveryCoverageType() {
		return berecoveryCoverageType;
	}

	public void setBerecoveryCoverageType(String berecoveryCoverageType) {
		this.berecoveryCoverageType = berecoveryCoverageType;
	}

	public String getBerecoveryPolicyNo() {
		return berecoveryPolicyNo;
	}

	public void setBerecoveryPolicyNo(String berecoveryPolicyNo) {
		this.berecoveryPolicyNo = berecoveryPolicyNo;
	}

	public String getBerecoveryClaimNotificationNo() {
		return berecoveryClaimNotificationNo;
	}

	public void setBerecoveryClaimNotificationNo(String berecoveryClaimNotificationNo) {
		this.berecoveryClaimNotificationNo = berecoveryClaimNotificationNo;
	}

	public String getBerecoveryClaimStatus() {
		return berecoveryClaimStatus;
	}

	public void setBerecoveryClaimStatus(String berecoveryClaimStatus) {
		this.berecoveryClaimStatus = berecoveryClaimStatus;
	}

	public String getBerecoveryClaimProgress() {
		return berecoveryClaimProgress;
	}

	public void setBerecoveryClaimProgress(String berecoveryClaimProgress) {
		this.berecoveryClaimProgress = berecoveryClaimProgress;
	}

	public String getBerecoveryLicensePlateNo() {
		return berecoveryLicensePlateNo;
	}

	public void setBerecoveryLicensePlateNo(String berecoveryLicensePlateNo) {
		this.berecoveryLicensePlateNo = berecoveryLicensePlateNo;
	}

	public String getBerecoveryLicensePlateType() {
		return berecoveryLicensePlateType;
	}

	public void setBerecoveryLicensePlateType(String berecoveryLicensePlateType) {
		this.berecoveryLicensePlateType = berecoveryLicensePlateType;
	}

	

	public String getBerecoveryEngineNo() {
		return berecoveryEngineNo;
	}

	public void setBerecoveryEngineNo(String berecoveryEngineNo) {
		this.berecoveryEngineNo = berecoveryEngineNo;
	}

	public String getBerecoveryVin() {
		return berecoveryVin;
	}

	public void setBerecoveryVin(String berecoveryVin) {
		this.berecoveryVin = berecoveryVin;
	}
	
	public List<CopyInformationCheckListVo> getCheckListVo() {
		return checkListVo;
	}
	
	public void setCheckListVo(List<CopyInformationCheckListVo> checkListVo) {
		this.checkListVo = checkListVo;
	}

}
