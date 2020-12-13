package ins.sino.claimcar.platform.vo;

import ins.sino.claimcar.carplatform.adapter.DateXmlAdapter;

import java.util.Date;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
public class CIClaimCancelReqBasePartyVo {

	@XmlElement(name = "CLAIM_CODE", required = true)
	private String claimCode;// Y

	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	@XmlElement(name = "CANCEL_DATE", required = true)
	private Date cancelDate;// Y

	@XmlElement(name = "CONFIRM_SEQUENCE_NO", required = true)
	private String confirmSequenceNo;// Y

	@XmlElement(name = "REPORT_NO", required = true)
	private String reportNo;// Y

	@XmlElement(name = "CANCEL_TYPE", required = true)
	private String cancelType;// Y

	@XmlElement(name = "CANCEL_CAUSE", required = true)
	private String cancelCause;// Y

	@XmlElement(name = "CANCEL_DESC", required = false)
	private String cancelDesc;// N

	public String getClaimCode() {
		return claimCode;
	}

	public void setClaimCode(String claimCode) {
		this.claimCode = claimCode;
	}

	public Date getCancelTime() {
		return cancelDate;
	}

	public void setCancelTime(Date cancelDate) {
		this.cancelDate = cancelDate;
	}

	public String getConfirmSequenceNo() {
		return confirmSequenceNo;
	}

	public void setConfirmSequenceNo(String confirmSequenceNo) {
		this.confirmSequenceNo = confirmSequenceNo;
	}

	public String getReportNo() {
		return reportNo;
	}

	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}

	public String getCancelType() {
		return cancelType;
	}

	public void setCancelType(String cancelType) {
		this.cancelType = cancelType;
	}

	public String getCancelCause() {
		return cancelCause;
	}

	public void setCancelCause(String cancelCause) {
		this.cancelCause = cancelCause;
	}

	public String getCancelDesc() {
		return cancelDesc;
	}

	public void setCancelDesc(String cancelDesc) {
		this.cancelDesc = cancelDesc;
	}

}
