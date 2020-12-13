package ins.sino.claimcar.genilex.vo.common;

import java.util.Date;
import java.util.List;

public class PrpLFraudScoreBaseVo implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
    private Long fraudScoreID;
	
    private String reportNo;
    
    private Date scoreDate;
    
    private String scoreNode;
    
    private String isInjured;
    
    private String isSmallAmount;
    
    private String fraudScore;
    
    private String reasonDesc;
    
    private String ruleDesc;
    
    private Date createTime;
    
    private List<PrpLRuleCaseVo> suleCases;

	public Long getFraudScoreID() {
		return fraudScoreID;
	}

	public void setFraudScoreID(Long fraudScoreID) {
		this.fraudScoreID = fraudScoreID;
	}

	public String getReportNo() {
		return reportNo;
	}

	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}

	public Date getScoreDate() {
		return scoreDate;
	}

	public void setScoreDate(Date scoreDate) {
		this.scoreDate = scoreDate;
	}

	public String getScoreNode() {
		return scoreNode;
	}

	public void setScoreNode(String scoreNode) {
		this.scoreNode = scoreNode;
	}

	public String getIsInjured() {
		return isInjured;
	}

	public void setIsInjured(String isInjured) {
		this.isInjured = isInjured;
	}

	public String getIsSmallAmount() {
		return isSmallAmount;
	}

	public void setIsSmallAmount(String isSmallAmount) {
		this.isSmallAmount = isSmallAmount;
	}

	public String getFraudScore() {
		return fraudScore;
	}

	public void setFraudScore(String fraudScore) {
		this.fraudScore = fraudScore;
	}

	public String getReasonDesc() {
		return reasonDesc;
	}

	public void setReasonDesc(String reasonDesc) {
		this.reasonDesc = reasonDesc;
	}

	public String getRuleDesc() {
		return ruleDesc;
	}

	public void setRuleDesc(String ruleDesc) {
		this.ruleDesc = ruleDesc;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public List<PrpLRuleCaseVo> getSuleCases() {
		return suleCases;
	}

	public void setSuleCases(List<PrpLRuleCaseVo> suleCases) {
		this.suleCases = suleCases;
	}

	

}
