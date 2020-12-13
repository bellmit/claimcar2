package ins.sino.claimcar.platform.vo;

import ins.sino.claimcar.carplatform.adapter.DateXmlAdapter;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
public class CIReOpenAppReqBasePartVo {
	
	@XmlElement(name = "CONFIRM_SEQUENCE_NO", required = true)
	private String confirmSequenceNo;// 投保确认码
	
	@XmlElement(name = "CLAIM_CODE", required = true)
	private String claimCode;// 理赔编号
	
	@XmlElement(name = "REPORT_NO", required = true)
	private String registNo;// 报案号
	
	@XmlElement(name = "REOPEN_CAUSE", required = false)
	private String reOpenCause;// 重开原因
	
	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	@XmlElement(name = "REOPEN_DATE", required = true)
	private Date reOpenDate;// 重开时间；格式：精确到分钟

	public String getConfirmSequenceNo() {
		return confirmSequenceNo;
	}

	public void setConfirmSequenceNo(String confirmSequenceNo) {
		this.confirmSequenceNo = confirmSequenceNo;
	}

	public String getClaimCode() {
		return claimCode;
	}

	public void setClaimCode(String claimCode) {
		this.claimCode = claimCode;
	}

	public String getRegistNo() {
		return registNo;
	}

	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}

	public String getReOpenCause() {
		return reOpenCause;
	}

	public void setReOpenCause(String reOpenCause) {
		this.reOpenCause = reOpenCause;
	}

	public Date getReOpenDate() {
		return reOpenDate;
	}

	public void setReOpenDate(Date reOpenDate) {
		this.reOpenDate = reOpenDate;
	}

}
