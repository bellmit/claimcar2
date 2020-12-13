package ins.sino.claimcar.trafficplatform.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * 山东预警-理算核赔登记-BasePart
 * <pre></pre>
 * @author ★WeiLanlei
 */
@XStreamAlias("BasePart")
public class EWVClaimBasePartVo {
	
	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("ClaimSequenceNo") 
	private String claimSequenceNo;// 理赔编号

	@XStreamAlias("ClaimNotificationNo")
	private String claimNotificationNo;// 报案号
	
	@XStreamAlias("ConfirmSequenceNo")
	private String confirmSequenceNo;// 投保确认码

	
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

	
	public String getConfirmSequenceNo() {
		return confirmSequenceNo;
	}

	
	public void setConfirmSequenceNo(String confirmSequenceNo) {
		this.confirmSequenceNo = confirmSequenceNo;
	}
	
	
}
