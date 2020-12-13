package ins.sino.claimcar.trafficplatform.vo;

import ins.platform.utils.xstreamconverters.SinosoftDateConverter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

@XStreamAlias("BasePart")
public class EWClaimBasePart implements Serializable {

	private static final long serialVersionUID = 8423652723600188374L;
	
	@XStreamAlias("ClaimSequenceNo")
	private String claimSequenceNo;  //理赔编号
	
	@XStreamAlias("ConfirmSequenceNo")
	private String confirmSequenceNo;  //投保确认码

	@XStreamAlias("ClaimNotificationNo")
	private String claimNotificationNo;  //报案号

	@XStreamAlias("ClaimRegistrationNo")
	private String claimRegistrationNo;  //立案号

	@XStreamAlias("ClaimRegistrationTime")
	private String claimRegistrationTime;  //立案时间；格式：精确到分钟

	@XStreamAlias("EstimatedLossAmount")
	private BigDecimal estimatedLossAmount;  //估损金额

	public String getClaimSequenceNo() {
		return claimSequenceNo;
	}

	public void setClaimSequenceNo(String claimSequenceNo) {
		this.claimSequenceNo = claimSequenceNo;
	}

	public String getConfirmSequenceNo() {
		return confirmSequenceNo;
	}

	public void setConfirmSequenceNo(String confirmSequenceNo) {
		this.confirmSequenceNo = confirmSequenceNo;
	}

	public String getClaimNotificationNo() {
		return claimNotificationNo;
	}

	public void setClaimNotificationNo(String claimNotificationNo) {
		this.claimNotificationNo = claimNotificationNo;
	}

	public String getClaimRegistrationNo() {
		return claimRegistrationNo;
	}

	public void setClaimRegistrationNo(String claimRegistrationNo) {
		this.claimRegistrationNo = claimRegistrationNo;
	}

	public BigDecimal getEstimatedLossAmount() {
		return estimatedLossAmount;
	}

	public void setEstimatedLossAmount(BigDecimal estimatedLossAmount) {
		this.estimatedLossAmount = estimatedLossAmount;
	}

	public String getClaimRegistrationTime() {
		return claimRegistrationTime;
	}

	public void setClaimRegistrationTime(String claimRegistrationTime) {
		this.claimRegistrationTime = claimRegistrationTime;
	}
	
}
