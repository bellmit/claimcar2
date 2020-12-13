package ins.sino.claimcar.selfHelpClaimCar.vo;


import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("BODY")
public class LockPolicyNoReqBodyVo {
	@XStreamAlias("POLICYINFO")
	private PolicyInfoReqVo infoVo;

	public PolicyInfoReqVo getInfoVo() {
		return infoVo;
	}

	public void setInfoVo(PolicyInfoReqVo infoVo) {
		this.infoVo = infoVo;
	}
	

}
