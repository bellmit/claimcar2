package ins.sino.claimcar.selfHelpClaimCar.vo;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("BODY")
public class LockPolicyResBodyVo {
 @XStreamAlias("POLICYINFOLIST")
 private List<PolicyInfoResVo> policyInfoListVo;

public List<PolicyInfoResVo> getPolicyInfoListVo() {
	return policyInfoListVo;
}

public void setPolicyInfoListVo(List<PolicyInfoResVo> policyInfoListVo) {
	this.policyInfoListVo = policyInfoListVo;
}
 
}
