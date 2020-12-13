package ins.sino.claimcar.trafficplatform.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("BasePart")
public class RequestReopenBasePartVo {
	@XStreamAlias("ConfirmSequenceNo")
	private String confirmSequenceNo;//投保确认码
	@XStreamAlias("ClaimSequenceNo")
	private String claimSequenceNo;//理赔编号
	@XStreamAlias("ClaimNotificationNo")
	private String claimNotificationNo;//报案号
	@XStreamAlias("ReopenCause")
	private String reopenCause;//重开原因
	@XStreamAlias("ReopenDate")
	private String reopenDate;//重开时间；精确到分
	public String getConfirmSequenceNo() {
		return confirmSequenceNo;
	}
	public void setConfirmSequenceNo(String confirmSequenceNo) {
		this.confirmSequenceNo = confirmSequenceNo;
	}
	public String getClaimSequenceNo() {
		return claimSequenceNo;
	}
	public void setClaimSequenceNo(String claimSequenceNo) {
		this.claimSequenceNo = claimSequenceNo;
	}
	public String getClaimNotificationNo() {
		return claimNotificationNo;
	}
	public void setClaimNotificationNo(String claimNotificationNo) {
		this.claimNotificationNo = claimNotificationNo;
	}
	public String getReopenCause() {
		return reopenCause;
	}
	public void setReopenCause(String reopenCause) {
		this.reopenCause = reopenCause;
	}
	public String getReopenDate() {
		return reopenDate;
	}
	public void setReopenDate(String reopenDate) {
		this.reopenDate = reopenDate;
	}
	

}
