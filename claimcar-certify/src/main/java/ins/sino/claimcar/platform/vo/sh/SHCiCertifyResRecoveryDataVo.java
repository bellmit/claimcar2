package ins.sino.claimcar.platform.vo.sh;

import javax.xml.bind.annotation.XmlElement;

public class SHCiCertifyResRecoveryDataVo {
	
	@XmlElement(name = "RECOVERY_COM", required = false)
	private String recoveryCom;
	
	@XmlElement(name = "RECOVERY_REPORT_NO", required = false)
	private String recoveryReportNo;
	
	@XmlElement(name = "STATUS", required = false)
	private String status;

	public String getRecoveryCom() {
		return recoveryCom;
	}

	public void setRecoveryCom(String recoveryCom) {
		this.recoveryCom = recoveryCom;
	}

	public String getRecoveryReportNo() {
		return recoveryReportNo;
	}

	public void setRecoveryReportNo(String recoveryReportNo) {
		this.recoveryReportNo = recoveryReportNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
