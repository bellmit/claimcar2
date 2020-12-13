package ins.sino.claimcar.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "LOCKED_DATA")
public class CIReOpenAppResLockedListVo {
	
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

	public String getRecoverStatus() {
		return recoverStatus;
	}

	public void setRecoverStatus(String recoverStatus) {
		this.recoverStatus = recoverStatus;
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

	public String getReportNo() {
		return reportNo;
	}

	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
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

}
