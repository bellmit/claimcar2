package ins.sino.claimcar.claim.vo;

import ins.sino.claimcar.check.vo.PrpLCheckDutyVo;
import ins.sino.claimcar.check.vo.PrpLCheckVo;
import ins.sino.claimcar.regist.vo.PrpLCItemCarVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

import java.util.List;

public class ClaimVO  implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
private boolean isCiPolicy;//是否交强险出险
	
	private int deathCount;	//伤
	private int injuryCount;	//亡
	private Long claimDelayDays;//立案距报案天数
	//存在商业险计算书
	private Boolean existBiCompensate;
	//存在交强险计算书
	private Boolean existCiCompensate;
	
	private PrpLRegistVo prpLRegistVo;

	private PrpLCMainVo prpLCMainVo;

	private PrpLCItemCarVo prpLCItemCarVo;
	
	private PrpLCheckVo prpLCheckVo;
	
	private PrpLClaimVo prpLClaimVo;
	
	private PrpLCheckDutyVo prpLCheckDutyVo;
	
	private ClaimFeeCondition claimFeeCond;
	
	private int registCount;
	
	private List<ClaimFeeExt> claimFeeExts;
	
	/** 立案号 */
	private String claimNo;
	
	/** 保单号 */
	private String policyNo;
	
	/** 报案号 */
	private String registNo;
	
	/** */
	private String riskCode;
	
	/** 被保险人名称 */
	private String insuredName;
	
	/** 商业交强险标识 */
	private String policyTypeFlag;
	
	/** 估损金额 */
	private String sumClaim;

	public boolean isCiPolicy() {
		return isCiPolicy;
	}

	public void setCiPolicy(boolean isCiPolicy) {
		this.isCiPolicy = isCiPolicy;
	}

	public int getDeathCount() {
		return deathCount;
	}
	
	public void setDeathCount(int deathCount) {
		this.deathCount = deathCount;
	}

	public int getInjuryCount() {
		return injuryCount;
	}

	public void setInjuryCount(int injuryCount) {
		this.injuryCount = injuryCount;
	}
	
	public Long getClaimDelayDays() {
		return claimDelayDays;
	}

	public void setClaimDelayDays(Long claimDelayDays) {
		this.claimDelayDays = claimDelayDays;
	}

	public Boolean getExistBiCompensate() {
		return existBiCompensate;
	}

	public void setExistBiCompensate(Boolean existBiCompensate) {
		this.existBiCompensate = existBiCompensate;
	}

	public Boolean getExistCiCompensate() {
		return existCiCompensate;
	}

	public void setExistCiCompensate(Boolean existCiCompensate) {
		this.existCiCompensate = existCiCompensate;
	}

	public PrpLRegistVo getPrpLRegistVo() {
		return prpLRegistVo;
	}

	public void setPrpLRegistVo(PrpLRegistVo prpLRegistVo) {
		this.prpLRegistVo = prpLRegistVo;
	}

	public PrpLCMainVo getPrpLCMainVo() {
		return prpLCMainVo;
	}

	public void setPrpLCMainVo(PrpLCMainVo prpLCMainVo) {
		this.prpLCMainVo = prpLCMainVo;
	}

	public PrpLCItemCarVo getPrpLCItemCarVo() {
		return prpLCItemCarVo;
	}

	public void setPrpLCItemCarVo(PrpLCItemCarVo prpLCItemCarVo) {
		this.prpLCItemCarVo = prpLCItemCarVo;
	}

	public PrpLCheckVo getPrpLCheckVo() {
		return prpLCheckVo;
	}

	public void setPrpLCheckVo(PrpLCheckVo prpLCheckVo) {
		this.prpLCheckVo = prpLCheckVo;
	}
	
	public PrpLClaimVo getPrpLClaimVo() {
		return prpLClaimVo;
	}

	public void setPrpLClaimVo(PrpLClaimVo prpLClaimVo) {
		this.prpLClaimVo = prpLClaimVo;
	}

	public PrpLCheckDutyVo getPrpLCheckDutyVo() {
		return prpLCheckDutyVo;
	}

	public void setPrpLCheckDutyVo(PrpLCheckDutyVo prpLCheckDutyVo) {
		this.prpLCheckDutyVo = prpLCheckDutyVo;
	}

	public ClaimFeeCondition getClaimFeeCond() {
		return claimFeeCond;
	}

	public void setClaimFeeCond(ClaimFeeCondition claimFeeCond) {
		this.claimFeeCond = claimFeeCond;
	}

	public int getRegistCount() {
		return registCount;
	}

	public void setRegistCount(int registCount) {
		this.registCount = registCount;
	}

	public List<ClaimFeeExt> getClaimFeeExts() {
		return claimFeeExts;
	}

	public void setClaimFeeExts(List<ClaimFeeExt> claimFeeExts) {
		this.claimFeeExts = claimFeeExts;
	}
	
	public String getClaimNo() {
		return claimNo;
	}
	
	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getRegistNo() {
		return registNo;
	}

	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}

	public String getRiskCode() {
		return riskCode;
	}

	public void setRiskCode(String riskCode) {
		this.riskCode = riskCode;
	}

	public String getInsuredName() {
		return insuredName;
	}
	
	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}

	public String getPolicyTypeFlag() {
		return policyTypeFlag;
	}

	public void setPolicyTypeFlag(String policyTypeFlag) {
		this.policyTypeFlag = policyTypeFlag;
	}

	public String getSumClaim() {
		return sumClaim;
	}
	
	public void setSumClaim(String sumClaim) {
		this.sumClaim = sumClaim;
	}
	
}
