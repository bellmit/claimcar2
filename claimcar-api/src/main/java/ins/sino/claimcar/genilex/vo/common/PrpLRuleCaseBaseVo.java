package ins.sino.claimcar.genilex.vo.common;

import ins.sino.claimcar.genilex.po.PrpLFraudScore;

public class PrpLRuleCaseBaseVo implements java.io.Serializable{

	private static final long serialVersionUID = 1L;

	private Long ruleCaseId;
	
    private PrpLFraudScore prpLFraudScore;
    
    private String caseNo;
    
    private String caseDesc;
    
    private String ruleNo;

    private String ruleDesc;

	public Long getRuleCaseId() {
		return ruleCaseId;
	}

	public void setRuleCaseId(Long ruleCaseId) {
		this.ruleCaseId = ruleCaseId;
	}

	public PrpLFraudScore getPrpLFraudScore() {
		return prpLFraudScore;
	}

	public void setPrpLFraudScore(PrpLFraudScore prpLFraudScore) {
		this.prpLFraudScore = prpLFraudScore;
	}

	public String getCaseNo() {
		return caseNo;
	}

	public void setCaseNo(String caseNo) {
		this.caseNo = caseNo;
	}

	public String getCaseDesc() {
		return caseDesc;
	}

	public void setCaseDesc(String caseDesc) {
		this.caseDesc = caseDesc;
	}

	public String getRuleNo() {
		return ruleNo;
	}

	public void setRuleNo(String ruleNo) {
		this.ruleNo = ruleNo;
	}

	public String getRuleDesc() {
		return ruleDesc;
	}

	public void setRuleDesc(String ruleDesc) {
		this.ruleDesc = ruleDesc;
	}
    
    
}
