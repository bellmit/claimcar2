package ins.sino.claimcar.platform.vo.sh;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class SHCiCertifyReqSubrogationDataVo {
	
	@XmlElement(name = "BERECOVERY_COM", required = true)
	private String beRecoveryCom;// 被追偿保险公司代码
	
	@XmlElement(name = "BERECOVERY_REPORT_NO", required = true)
	private String beRecoveryReportNo;// 被追偿保险公司报案号

	public String getBeRecoveryCom() {
		return beRecoveryCom;
	}

	public void setBeRecoveryCom(String beRecoveryCom) {
		this.beRecoveryCom = beRecoveryCom;
	}

	public String getBeRecoveryReportNo() {
		return beRecoveryReportNo;
	}

	public void setBeRecoveryReportNo(String beRecoveryReportNo) {
		this.beRecoveryReportNo = beRecoveryReportNo;
	}

}
