package ins.sino.claimcar.genilex.vo.scoreVo;

import ins.platform.utils.xstreamconverters.SinosoftDateConverter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

@XStreamAlias("FraudScore")
public class FraudScore implements Serializable {

	/**  */
	private static final long serialVersionUID = 6606450331852430142L;
	
    @XStreamAlias("ReportNo") 
    private String reportNo;
    
    @XStreamAlias("ScoreDate")
    @XStreamConverter(value = SinosoftDateConverter.class, strings = {"yyyy-MM-dd HH:mm:ss"})
    private Date scoreDate;
    
    @XStreamAlias("ScoreNode") 
    private String scoreNode;
    
    @XStreamAlias("isInjured") 
    private String isInjured;
    
    @XStreamAlias("isSmallAmount") 
    private String isSmallAmount;
    
    @XStreamAlias("FraudScore") 
    private String fraudScore;
    
    @XStreamAlias("ReasonDesc") 
    private String reasonDesc;
    
    @XStreamAlias("RuleDesc") 
    private String ruleDesc;
    
	@XStreamAlias("Reference")
	private String reference;
	
    @XStreamAlias("RuleCases") 
    private List<RuleCase> suleCases;

    
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

    
    public List<RuleCase> getSuleCases() {
        return suleCases;
    }

    
    public void setSuleCases(List<RuleCase> suleCases) {
        this.suleCases = suleCases;
    }


	public String getReference() {
		return reference;
	}


	public void setReference(String reference) {
		this.reference = reference;
	}
    
    
}
