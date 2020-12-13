package ins.sino.claimcar.policyInfo.register.vo;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CiPolicyInfoRegisterVo implements Serializable{
 private static final long serialVersionUID = 1L;
 
 private InsuranceClaimVo insuranceClaim;//交强险理赔详情信息
 private InsuranceClaimOutDangerVo insuranceClaimOutDangers;//出险车辆信息
 private List<InsuranceClaimPeerVo> insuranceClaimPeers;//交强险报案第三方车辆信息
public InsuranceClaimVo getInsuranceClaim() {
	return insuranceClaim;
}
public void setInsuranceClaim(InsuranceClaimVo insuranceClaim) {
	this.insuranceClaim = insuranceClaim;
}

public List<InsuranceClaimPeerVo> getInsuranceClaimPeers() {
	return insuranceClaimPeers;
}
public void setInsuranceClaimPeers(
		List<InsuranceClaimPeerVo> insuranceClaimPeers) {
	this.insuranceClaimPeers = insuranceClaimPeers;
}
public InsuranceClaimOutDangerVo getInsuranceClaimOutDangers() {
	return insuranceClaimOutDangers;
}
public void setInsuranceClaimOutDangers(
		InsuranceClaimOutDangerVo insuranceClaimOutDangers) {
	this.insuranceClaimOutDangers = insuranceClaimOutDangers;
}
 
 
 
}
