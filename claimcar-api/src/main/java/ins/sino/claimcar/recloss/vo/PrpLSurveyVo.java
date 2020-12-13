package ins.sino.claimcar.recloss.vo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Custom VO class of PO PrpLSurvey
 */ 
public class PrpLSurveyVo extends PrpLSurveyVoBase implements java.io.Serializable {
	private static final long serialVersionUID = 1L; 
	
	private String flowId;
	private String scoreNode;//评分环节
	private Date scoreTime;//评分日期
	private String fraudScore;//反欺诈评分
	private String ruleDesc;//规则描述
	private String auxiliaryDesc;//辅助描述
	private String isFraud;//是否欺诈
	private BigDecimal amout;//减损金额
	private String impairmentCase;//是否整案减损
	private String fraudType;//增加欺诈类型
	private String externalSurvey;//是否转外部调查机构处理
	

	public String getFlowId() {
		return flowId;
	}

	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}

	public String getScoreNode() {
		return scoreNode;
	}

	public void setScoreNode(String scoreNode) {
		this.scoreNode = scoreNode;
	}

	public Date getScoreTime() {
		return scoreTime;
	}

	public void setScoreTime(Date scoreTime) {
		this.scoreTime = scoreTime;
	}

	public String getFraudScore() {
		return fraudScore;
	}

	public void setFraudScore(String fraudScore) {
		this.fraudScore = fraudScore;
	}

	public String getRuleDesc() {
		return ruleDesc;
	}

	public void setRuleDesc(String ruleDesc) {
		this.ruleDesc = ruleDesc;
	}

	public String getAuxiliaryDesc() {
		return auxiliaryDesc;
	}

	public void setAuxiliaryDesc(String auxiliaryDesc) {
		this.auxiliaryDesc = auxiliaryDesc;
	}

	public String getIsFraud() {
		return isFraud;
	}

	public void setIsFraud(String isFraud) {
		this.isFraud = isFraud;
	}

	public BigDecimal getAmout() {
		return amout;
	}

	public void setAmout(BigDecimal amout) {
		this.amout = amout;
	}

	public String getImpairmentCase() {
		return impairmentCase;
	}

	public void setImpairmentCase(String impairmentCase) {
		this.impairmentCase = impairmentCase;
	}

	public String getFraudType() {
		return fraudType;
	}

	public void setFraudType(String fraudType) {
		this.fraudType = fraudType;
	}

	public String getExternalSurvey() {
		return externalSurvey;
	}

	public void setExternalSurvey(String externalSurvey) {
		this.externalSurvey = externalSurvey;
	}
	
	
}
