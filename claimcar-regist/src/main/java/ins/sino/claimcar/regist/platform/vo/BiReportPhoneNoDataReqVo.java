package ins.sino.claimcar.regist.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * 商业报案请求平台信息BiReportPhoneNoDataReqVo类
 * @author dengkk
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class BiReportPhoneNoDataReqVo {
	/**
	 * 报案电话号码
	 */
	@XmlElement(name="ReportPhoneNo")
	private String reportPhoneNo;

	public String getReportPhoneNo() {
		return reportPhoneNo;
	}

	public void setReportPhoneNo(String reportPhoneNo) {
		this.reportPhoneNo = reportPhoneNo;
	}
	
}
