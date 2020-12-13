package ins.sino.claimcar.platform.vo.sh;

import javax.xml.bind.annotation.XmlElement;

public class SHCiCertifyResBeRecoveryDataVo {
	
	@XmlElement(name = "BERECOVERY_COM", required = false)
	private String beRevoceryCom;              //被追偿保险公司代码
	
	@XmlElement(name = "STATUS", required = false)
	private String status;
	
	@XmlElement(name = "BERECOVERY_REPORT_NO", required = false)
	private String beRevoceryReportNo;       //被追偿保险公司报案号

	public String getBeRevoceryCom() {
		return beRevoceryCom;
	}

	public void setBeRevoceryCom(String beRevoceryCom) {
		this.beRevoceryCom = beRevoceryCom;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getBeRevoceryReportNo() {
		return beRevoceryReportNo;
	}

	public void setBeRevoceryReportNo(String beRevoceryReportNo) {
		this.beRevoceryReportNo = beRevoceryReportNo;
	}

}
