package ins.sino.claimcar.platform.vo;


import ins.sino.claimcar.carplatform.adapter.DateXmlAdapter;
import java.util.Date;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
public class BIClaimCancelReqBasePartyVo {
	
	@XmlElement(name = "ClaimSequenceNo", required = true)
	private String claimSequenceNo;// 理赔编号
	
	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	@XmlElement(name = "CancelDate", required = true)
	private Date cancelDate;// 注销时间
	
	@XmlElement(name = "ConfirmSequenceNo", required = true)
	private String confirmSequenceNo;//投保确认码
	
	@XmlElement(name = "ClaimNotificationNo", required = true)
	private String reportNo;//报案号
	
	@XmlElement(name = "CancelType", required = true)
	private String cancelType;//注销类型
	
	@XmlElement(name = "CancelCause", required = true)
	private String cancelCause;//注销原因
	
	@XmlElement(name = "CancelDesc", required = false)
	private String cancelDesc;//注销描述
	
	@XmlElement(name = "DirectClaimAmount", required = true)
	private Double directClaimAmount;// 直接理赔费用总金额

	public String getClaimSequenceNo() {
		return claimSequenceNo;
	}

	public void setClaimSequenceNo(String claimSequenceNo) {
		this.claimSequenceNo = claimSequenceNo;
	}

	public Date getCancelDate() {
		return cancelDate;
	}

	public void setCancelDate(Date cancelDate) {
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

	public Double getDirectClaimAmount() {
		return directClaimAmount;
	}

	public void setDirectClaimAmount(Double directClaimAmount) {
		this.directClaimAmount = directClaimAmount;
	}
	

}
