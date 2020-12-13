package ins.sino.claimcar.selfHelpClaimCar.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("BODY")
public class SelfClaimDealFlowReqBodyVo {
	 @XStreamAlias("REGISTNO")
	 private String registNo;//报案号
	 @XStreamAlias("TIMESTAMP")
	 private String timestamp;//时间戳
	public String getRegistNo() {
		return registNo;
	}
	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	 


}
