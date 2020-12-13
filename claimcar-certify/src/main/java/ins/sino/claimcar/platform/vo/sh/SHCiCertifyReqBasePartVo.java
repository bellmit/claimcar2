package ins.sino.claimcar.platform.vo.sh;

import ins.sino.claimcar.carplatform.adapter.DateXmlAdapterSH;
import java.util.Date;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
public class SHCiCertifyReqBasePartVo {

	@XmlElement(name = "CLAIM_CODE", required = true)
	private String claimCode;// 理赔编号

	@XmlElement(name = "REPORT_NO", required = true)
	private String reportNo;// 报案号

	@XmlJavaTypeAdapter(DateXmlAdapterSH.class)
	@XmlElement(name = "DOC_START_TIME", required = false)
	private Date docStartTime;// 单证收集开始时间 格式:YYYYMMDDHHMM

	@XmlJavaTypeAdapter(DateXmlAdapterSH.class)
	@XmlElement(name = "DOC_END_TIME", required = false)
	private Date docEndTime;// 单证收集结束时间 格式:YYYYMMDDHHMM

	@XmlElement(name = "SUBROGATION_FLAG", required = true)
	private String subrogationFlag;// 是否代为求偿标志

	public String getClaimCode() {
		return claimCode;
	}

	public void setClaimCode(String claimCode) {
		this.claimCode = claimCode;
	}

	public Date getDocStartTime() {
		return docStartTime;
	}

	public void setDocStartTime(Date docStartTime) {
		this.docStartTime = docStartTime;
	}

	public Date getDocEndTime() {
		return docEndTime;
	}

	public void setDocEndTime(Date docEndTime) {
		this.docEndTime = docEndTime;
	}

	public String getSubrogationFlag() {
		return subrogationFlag;
	}

	public void setSubrogationFlag(String subrogationFlag) {
		this.subrogationFlag = subrogationFlag;
	}

	public String getReportNo() {
		return reportNo;
	}

	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}

}
