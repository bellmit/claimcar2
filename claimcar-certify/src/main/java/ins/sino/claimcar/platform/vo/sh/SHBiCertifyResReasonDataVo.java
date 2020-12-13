package ins.sino.claimcar.platform.vo.sh;

import javax.xml.bind.annotation.XmlElement;

public class SHBiCertifyResReasonDataVo {
	
	@XmlElement(name = "COMPANY_ID", required = false)
	private String companyId;
	
	@XmlElement(name = "REASON", required = false)
	private String reason;
	
	@XmlElement(name = "DISPUTE_TIME", required = false)
	private String disputeTime;

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getDisputeTime() {
		return disputeTime;
	}

	public void setDisputeTime(String disputeTime) {
		this.disputeTime = disputeTime;
	}

}
