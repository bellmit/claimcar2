/**
 * 
 */
package ins.sino.claimcar.platform.vo.sh;

import ins.sino.claimcar.carplatform.adapter.DateXmlAdapterSH;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * @author ★Wurh
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SHCIClaimCancelReqBasePartyVo {

	@XmlElement(name = "CLAIM_CODE", required = true)
	private String claimCode;// Y

	@XmlElement(name = "REPORT_NO", required = true)
	private String reportNo;// Y

	@XmlElement(name = "REJECT_REASON")
	private String rejectReason;// Y

	@XmlJavaTypeAdapter(DateXmlAdapterSH.class)
	@XmlElement(name = "COM_CANCEL_TIME", required = true)
	private Date comCancelTime;// Y

	@XmlElement(name = "DirectClaimAmount", required = true)
	private Double directclaimamount;// Y

	/**
	 * @return 返回 reportNo。
	 */
	public String getReportNo() {
		return reportNo;
	}

	/**
	 * @param reportNo 要设置的 reportNo。
	 */
	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}

	public String getClaimCode() {
		return claimCode;
	}

	public void setClaimCode(String claimCode) {
		this.claimCode = claimCode;
	}

	public String getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}

	public Date getComCancelTime() {
		return comCancelTime;
	}

	public void setComCancelTime(Date comCancelTime) {
		this.comCancelTime = comCancelTime;
	}

	public Double getDirectclaimamount() {
		return directclaimamount;
	}

	public void setDirectclaimamount(Double directclaimamount) {
		this.directclaimamount = directclaimamount;
	}

}
