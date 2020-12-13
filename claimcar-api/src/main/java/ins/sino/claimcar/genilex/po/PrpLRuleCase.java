package ins.sino.claimcar.genilex.po;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@TableGenerator(name = "utiSequence", table = "UTISEQUENCE", pkColumnName = "GenName", valueColumnName = "GenValue", pkColumnValue = "PRPLRULECASE_PK", allocationSize = 10)
@Table(name = "PRPLRULECASE")
public class PrpLRuleCase implements Serializable {

	/**  */
	private static final long serialVersionUID = 6606450331852430142L;
	
	private Long ruleCaseId;
	
    private PrpLFraudScore prpLFraudScore;//评分信息列表
    
    private String caseNo;//规则大类
    
    private String caseDesc;//大类描述
    
    private String ruleNo;//规则编号

    private String ruleDesc;//规则描述

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "utiSequence")
    @Column(name = "RULECASEID", unique = true, nullable = false, precision=0)
    public Long getRuleCaseId() {
		return ruleCaseId;
	}


	public void setRuleCaseId(Long ruleCaseId) {
		this.ruleCaseId = ruleCaseId;
	}

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="FRAUDSCOREID")
    public PrpLFraudScore getPrpLFraudScore() {
        return prpLFraudScore;
    }
    
	public void setPrpLFraudScore(PrpLFraudScore prpLFraudScore) {
        this.prpLFraudScore = prpLFraudScore;
    }

    @Column(name = "CASENO", length = 20)
    public String getCaseNo() {
        return caseNo;
    }

    
    public void setCaseNo(String caseNo) {
        this.caseNo = caseNo;
    }

    @Column(name = "CASEDESC", length = 3999)
    public String getCaseDesc() {
        return caseDesc;
    }

    
    public void setCaseDesc(String caseDesc) {
        this.caseDesc = caseDesc;
    }

    @Column(name = "RULENO", length = 20)
    public String getRuleNo() {
        return ruleNo;
    }

    
    public void setRuleNo(String ruleNo) {
        this.ruleNo = ruleNo;
    }

    @Column(name = "RULEDESC", length = 3999)
    public String getRuleDesc() {
        return ruleDesc;
    }

    
    public void setRuleDesc(String ruleDesc) {
    	this.ruleDesc = ruleDesc;
    }

    
    
}
