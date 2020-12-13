package ins.sino.claimcar.genilex.po;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@TableGenerator(name = "utiSequence", table = "UTISEQUENCE", pkColumnName = "GenName", valueColumnName = "GenValue", pkColumnValue = "PRPLFRAUDSCORE_PK", allocationSize = 10)
@Table(name = "PRPLFRAUDSCORE")
public class PrpLFraudScore implements Serializable {

	/**  */
	private static final long serialVersionUID = 6606450331852430142L;
	
	private Long fraudScoreID;
	
    private String reportNo;
    
    private Date scoreDate;//评分标准
    
    private String scoreNode;//评分环节
    
    private String isInjured;//是否人伤
    
    private String isSmallAmount;//是否小额
    
    private String fraudScore;//反欺诈评分
    
    private String reasonDesc;//辅助描述
    
    private String ruleDesc;//规则描述
    
    private List<PrpLRuleCase> suleCases;//规则列表

    private Date createTime;
    

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "utiSequence")
    @Column(name = "FRAUDSCOREID", unique = true, nullable = false, scale=0)
    public Long getFraudScoreID() {
		return fraudScoreID;
	}


	public void setFraudScoreID(Long fraudScoreID) {
		this.fraudScoreID = fraudScoreID;
	}

    @Column(name = "REPORTNO", length = 30)
    public String getReportNo() {
        return reportNo;
    }

	public void setReportNo(String reportNo) {
        this.reportNo = reportNo;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "SCOREDATE", length = 7)
    public Date getScoreDate() {
        return scoreDate;
    }

    
    public void setScoreDate(Date scoreDate) {
        this.scoreDate = scoreDate;
    }

    @Column(name = "SCORENODE", length = 3)
    public String getScoreNode() {
        return scoreNode;
    }

    
    public void setScoreNode(String scoreNode) {
        this.scoreNode = scoreNode;
    }

    @Column(name = "ISINJURED", length = 1)
    public String getIsInjured() {
        return isInjured;
    }

    
    public void setIsInjured(String isInjured) {
        this.isInjured = isInjured;
    }

    @Column(name = "ISSMALLAMOUNT", length = 1)
    public String getIsSmallAmount() {
        return isSmallAmount;
    }

    
    public void setIsSmallAmount(String isSmallAmount) {
        this.isSmallAmount = isSmallAmount;
    }

    @Column(name = "FRAUDSCORE", length = 3999)
    public String getFraudScore() {
        return fraudScore;
    }

    
    public void setFraudScore(String fraudScore) {
        this.fraudScore = fraudScore;
    }

    @Column(name = "REASONDESC", length = 3999)
    public String getReasonDesc() {
        return reasonDesc;
    }

    
    public void setReasonDesc(String reasonDesc) {
        this.reasonDesc = reasonDesc;
    }

    @Column(name = "RULEDESC", length = 3999)
    public String getRuleDesc() {
        return ruleDesc;
    }

    
    public void setRuleDesc(String ruleDesc) {
        this.ruleDesc = ruleDesc;
    }
    
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATETIME", length = 7)
    public Date getCreateTime() {
		return createTime;
	}


	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}


	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "prpLFraudScore")
    public List<PrpLRuleCase> getSuleCases() {
        return suleCases;
    }

    
    public void setSuleCases(List<PrpLRuleCase> suleCases) {
        this.suleCases = suleCases;
    }


    
    
}
