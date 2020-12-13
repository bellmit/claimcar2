/******************************************************************************
 * CREATETIME : 2016年5月23日 上午10:47:11
 ******************************************************************************/
package ins.sino.claimcar.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author ★XMSH
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CiClaimResLockedDataVo {

	@XmlElement(name = "RECOVERY_CODE", required = true)
	private String recoveryCode;// 结算码

	@XmlElement(name = "RECOVERY_CODE_STATUS", required = true)
	private String recoveryCodeStatus;// 结算码状态；参见代码

	@XmlElement(name = "RECOVER_STATUS", required = true)
	private String recoverStatus;// 本方追偿状态；参见代码

	@XmlElement(name = "INSURER_CODE", required = true)
	private String insurerCode;// 对方保险公司代码；参见代码

	@XmlElement(name = "INSURER_AREA", required = true)
	private String insurerArea;// 对方承保地区代码；参见代码

	@XmlElement(name = "COVERAGE_TYPE", required = true)
	private String coverageType;// 对方保单险种类型；参见代码

	@XmlElement(name = "POLICY_NO", required = true)
	private String policyNo;// 对方保单号

	@XmlElement(name = "REPORT_NO", required = true)
	private String reportNo;// 对方报案号

	@XmlElement(name = "CLAIM_STATUS", required = true)
	private String claimStatus;// 对方案件状态代码；参见代码

	@XmlElement(name = "CLAIM_PROGRESS", required = true)
	private String claimProgress;// 对方案件进展 ；参见代码

	/**
	 * @return 返回 recoveryCode 结算码
	 */
	public String getRecoveryCode() {
		return recoveryCode;
	}

	/**
	 * @param recoveryCode 要设置的 结算码
	 */
	public void setRecoveryCode(String recoveryCode) {
		this.recoveryCode = recoveryCode;
	}

	/**
	 * @return 返回 recoveryCodeStatus 结算码状态；参见代码
	 */
	public String getRecoveryCodeStatus() {
		return recoveryCodeStatus;
	}

	/**
	 * @param recoveryCodeStatus 要设置的 结算码状态；参见代码
	 */
	public void setRecoveryCodeStatus(String recoveryCodeStatus) {
		this.recoveryCodeStatus = recoveryCodeStatus;
	}

	/**
	 * @return 返回 recoverStatus 本方追偿状态；参见代码
	 */
	public String getRecoverStatus() {
		return recoverStatus;
	}

	/**
	 * @param recoverStatus 要设置的 本方追偿状态；参见代码
	 */
	public void setRecoverStatus(String recoverStatus) {
		this.recoverStatus = recoverStatus;
	}

	/**
	 * @return 返回 insurerCode 对方保险公司代码；参见代码
	 */
	public String getInsurerCode() {
		return insurerCode;
	}

	/**
	 * @param insurerCode 要设置的 对方保险公司代码；参见代码
	 */
	public void setInsurerCode(String insurerCode) {
		this.insurerCode = insurerCode;
	}

	/**
	 * @return 返回 insurerArea 对方承保地区代码；参见代码
	 */
	public String getInsurerArea() {
		return insurerArea;
	}

	/**
	 * @param insurerArea 要设置的 对方承保地区代码；参见代码
	 */
	public void setInsurerArea(String insurerArea) {
		this.insurerArea = insurerArea;
	}

	/**
	 * @return 返回 coverageType 对方保单险种类型；参见代码
	 */
	public String getCoverageType() {
		return coverageType;
	}

	/**
	 * @param coverageType 要设置的 对方保单险种类型；参见代码
	 */
	public void setCoverageType(String coverageType) {
		this.coverageType = coverageType;
	}

	/**
	 * @return 返回 policyNo 对方保单号
	 */
	public String getPolicyNo() {
		return policyNo;
	}

	/**
	 * @param policyNo 要设置的 对方保单号
	 */
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	/**
	 * @return 返回 reportNo 对方报案号
	 */
	public String getReportNo() {
		return reportNo;
	}

	/**
	 * @param reportNo 要设置的 对方报案号
	 */
	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}

	/**
	 * @return 返回 claimStatus 对方案件状态代码；参见代码
	 */
	public String getClaimStatus() {
		return claimStatus;
	}

	/**
	 * @param claimStatus 要设置的 对方案件状态代码；参见代码
	 */
	public void setClaimStatus(String claimStatus) {
		this.claimStatus = claimStatus;
	}

	/**
	 * @return 返回 claimProgress 对方案件进展 ；参见代码
	 */
	public String getClaimProgress() {
		return claimProgress;
	}

	/**
	 * @param claimProgress 要设置的 对方案件进展 ；参见代码
	 */
	public void setClaimProgress(String claimProgress) {
		this.claimProgress = claimProgress;
	}

}
