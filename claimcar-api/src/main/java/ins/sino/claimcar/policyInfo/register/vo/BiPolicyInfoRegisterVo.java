package ins.sino.claimcar.policyInfo.register.vo;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BiPolicyInfoRegisterVo implements Serializable{
	 private static final long serialVersionUID = 1L;
	 private CommercialClaimVo commercialClaim;//商业险理赔详细信息
	 private CommercialClaimOutDangerVo commercialClaimOutDangers;//标的车出险信息
	 private List<CommercialClaimPeerVo> commercialClaimPeers;//三者车出险信息
	public CommercialClaimVo getCommercialClaim() {
		return commercialClaim;
	}
	public void setCommercialClaim(CommercialClaimVo commercialClaim) {
		this.commercialClaim = commercialClaim;
	}
	
	public List<CommercialClaimPeerVo> getCommercialClaimPeers() {
		return commercialClaimPeers;
	}
	public void setCommercialClaimPeers(
			List<CommercialClaimPeerVo> commercialClaimPeers) {
		this.commercialClaimPeers = commercialClaimPeers;
	}
	public CommercialClaimOutDangerVo getCommercialClaimOutDangers() {
		return commercialClaimOutDangers;
	}
	public void setCommercialClaimOutDangers(
			CommercialClaimOutDangerVo commercialClaimOutDangers) {
		this.commercialClaimOutDangers = commercialClaimOutDangers;
	}
	 
	 
}
