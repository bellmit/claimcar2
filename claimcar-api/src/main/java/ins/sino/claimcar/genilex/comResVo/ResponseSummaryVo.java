package ins.sino.claimcar.genilex.comResVo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("ResponseSummary")
public class ResponseSummaryVo  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@XStreamAlias("TrackingNumber")
	private String trackingNumber;//追踪编号（用于客户追踪的GUID）
	@XStreamAlias("OverallStatus")
	private String overallStatus;//整体状况				
	@XStreamAlias("OverallMessage")
	private String overallMessage;//整体状况说明	
	@XStreamAlias("AccountNumberStatus")
	private AccountNumberStatusVo accountNumberStatusVo;//授权验证结果
	
	public String getTrackingNumber() {
		return trackingNumber;
	}
	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}
	public String getOverallStatus() {
		return overallStatus;
	}
	public void setOverallStatus(String overallStatus) {
		this.overallStatus = overallStatus;
	}
	public String getOverallMessage() {
		return overallMessage;
	}
	public void setOverallMessage(String overallMessage) {
		this.overallMessage = overallMessage;
	}
	public AccountNumberStatusVo getAccountNumberStatusVo() {
		return accountNumberStatusVo;
	}
	public void setAccountNumberStatusVo(AccountNumberStatusVo accountNumberStatusVo) {
		this.accountNumberStatusVo = accountNumberStatusVo;
	}				
  

}
