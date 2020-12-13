/******************************************************************************
* CREATETIME : 2016年3月31日 下午8:32:36
******************************************************************************/
package ins.sino.claimcar.subrogation.vo;

import java.io.Serializable;
import java.util.Date;


/**
 * 追偿回馈确认返回vo
 * @author ★YangKun
 * @CreateTime 2016年3月31日
 */
public class RecoveryReturnConfirmVo implements Serializable{
	private static final long serialVersionUID = 1L;

	private String registNo;
	private  String policyNo;
	private Date reportTime;
	private String insuredCode;
	private String insuredName;
	private String licenseNo;
	private String recoveryCode;
	private String recoveryCodeStatus;
	
	public String getRecoveryCodeStatus() {
		return recoveryCodeStatus;
	}

	public void setRecoveryCodeStatus(String recoveryCodeStatus) {
		this.recoveryCodeStatus = recoveryCodeStatus;
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
	
	public Date getReportTime() {
		return reportTime;
	}
	
	public void setReportTime(Date reportTime) {
		this.reportTime = reportTime;
	}
	
	public String getInsuredName() {
		return insuredName;
	}
	
	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}
	
	public String getLicenseNo() {
		return licenseNo;
	}
	
	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}
	
	public String getRecoveryCode() {
		return recoveryCode;
	}
	
	public void setRecoveryCode(String recoveryCode) {
		this.recoveryCode = recoveryCode;
	}

	public String getInsuredCode() {
		return insuredCode;
	}

	public void setInsuredCode(String insuredCode) {
		this.insuredCode = insuredCode;
	}
	
	
}
