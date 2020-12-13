package ins.sino.claimcar.platform.vo.sh;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class SHCiCertifyReqDisputeDataVo {
	
	@XmlElement(name = "SUBROGATION_NO", required = false)
	private String subrogationNo;// 需要发起争议的代位求偿码
	
	@XmlElement(name = "DISPUTE_FLAG", required = false)
	private String disputeFlag;
	
	@XmlElement(name = "REASON", required = false)
	private String reason;

	public String getSubrogationNo() {
		return subrogationNo;
	}

	public void setSubrogationNo(String subrogationNo) {
		this.subrogationNo = subrogationNo;
	}

	public String getDisputeFlag() {
		return disputeFlag;
	}

	public void setDisputeFlag(String disputeFlag) {
		this.disputeFlag = disputeFlag;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

}
