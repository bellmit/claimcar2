package ins.sino.claimcar.trafficplatform.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("BasePart")
public class RequestCancleBasePartVo {
	@XStreamAlias("ConfirmSequenceNo")
	private String confirmSequenceNo;//投保确认码
	@XStreamAlias("ClaimSequenceNo")
	private String claimSequenceNo;//理赔编号
	@XStreamAlias("ClaimNotificationNo")
	private String claimNotificationNo;//报案号
	@XStreamAlias("CancelType")
	private String cancelType;//案件注销类型
	@XStreamAlias("CancelCause")
	private String cancelCause;//注销原因
	@XStreamAlias("CancelDesc")
	private String cancelDesc;//注销描述
	@XStreamAlias("CancelDate")
	private String cancelDate;//注销时间；精确到分
	@XStreamAlias("DirectClaimAmount")
	private String DirectClaimAmount;//直接理赔费用总金额
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
	
	public String getCancelDate() {
		return cancelDate;
	}
	public void setCancelDate(String cancelDate) {
		this.cancelDate = cancelDate;
	}
	public String getDirectClaimAmount() {
		return DirectClaimAmount;
	}
	public void setDirectClaimAmount(String directClaimAmount) {
		DirectClaimAmount = directClaimAmount;
	}
	

}
