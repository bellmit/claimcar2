package ins.sino.claimcar.regist.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class CiRrportPhoneDataVo {
	
	@XmlElement(name="REPORT_PHONE_NO")
	private String reportPhoneNo;

	public String getReportPhoneNo() {
		return reportPhoneNo;
	}

	public void setReportPhoneNo(String reportPhoneNo) {
		this.reportPhoneNo = reportPhoneNo;
	}
	
	
}
