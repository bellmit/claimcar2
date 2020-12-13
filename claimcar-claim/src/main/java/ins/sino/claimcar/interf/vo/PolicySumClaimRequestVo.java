package ins.sino.claimcar.interf.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("BasePart")
public class PolicySumClaimRequestVo implements Serializable {

	private static final long serialVersionUID = 1L;
	@XStreamAlias("RequestType")
	private String requestType;// 请求类型 0 未决 
	@XStreamAlias("PolicyNo")
	private String policyNo;// 字符 保单号
	
	
	public String getRequestType() {
		return requestType;
	}
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
	public String getPolicyNo() {
		return policyNo;
	}
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}
	
	
}
