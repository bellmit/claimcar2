package ins.sino.claimcar.interf.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("ClaimFeeVo")
public class ClaimFeeVo implements Serializable{
	private static final long serialVersionUID = 1L;

	@XStreamAlias("PolicyNo")
	private String policyNo;// 字符 保单号
	
	@XStreamAlias("SumClaim")
	private String sumClaim;// 未决赔款
	
	@XStreamAlias("SumRescueFee")
	private String sumRescueFee;// 施救费

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getSumClaim() {
		return sumClaim;
	}

	public void setSumClaim(String sumClaim) {
		this.sumClaim = sumClaim;
	}

	public String getSumRescueFee() {
		return sumRescueFee;
	}

	public void setSumRescueFee(String sumRescueFee) {
		this.sumRescueFee = sumRescueFee;
	}
	
	
}
