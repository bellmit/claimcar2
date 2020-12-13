package ins.sino.claimcar.genilex.vo.scoreVo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("RuleCase")
public class RuleCase implements Serializable {

	/**  */
	private static final long serialVersionUID = 6606450331852430142L;
	
    @XStreamAlias("CaseNo") 
    private String caseNo;
    
    @XStreamAlias("CaseDesc") 
    private String caseDesc;
    
    @XStreamAlias("RuleNo") 
    private String ruleNo;

    @XStreamAlias("RuleDesc") 
    private String RuleDesc;

    
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
        return RuleDesc;
    }

    
    public void setRuleDesc(String ruleDesc) {
        RuleDesc = ruleDesc;
    }
    
    
}
