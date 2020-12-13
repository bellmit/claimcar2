package ins.sino.claimcar.subrogationsh.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * <pre></pre>
 * @author ★Luwei
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SubrogationSHCopyInformationBasePartVo {

	/**
	 * 报案号
	 */
	@XmlElement(name = "ReportNo")
	private String reportNo;

	/**
	 * 理赔编码
	 */
	@XmlElement(name = "ClaimCode")
	private String claimCode;

	public String getReportNo() {
		return reportNo;
	}

	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}

	public String getClaimCode() {
		return claimCode;
	}

	public void setClaimCode(String claimCode) {
		this.claimCode = claimCode;
	}
}
