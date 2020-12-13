package ins.sino.claimcar.losscar.vo;

public class PrplLossRuleSubVoBase implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String ruleCode;
	private String groupType;
	private String ruleType;
	private String ruleModel;
	private Integer lossRuleScore;
	private String riskItemId;
	private String riskItemName;
	private String riskItemType;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getRuleCode() {
		return ruleCode;
	}
	public void setRuleCode(String ruleCode) {
		this.ruleCode = ruleCode;
	}
	public String getGroupType() {
		return groupType;
	}
	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}
	public String getRuleType() {
		return ruleType;
	}
	public void setRuleType(String ruleType) {
		this.ruleType = ruleType;
	}
	public String getRuleModel() {
		return ruleModel;
	}
	public void setRuleModel(String ruleModel) {
		this.ruleModel = ruleModel;
	}
	public Integer getLossRuleScore() {
		return lossRuleScore;
	}
	public void setLossRuleScore(Integer lossRuleScore) {
		this.lossRuleScore = lossRuleScore;
	}
	public String getRiskItemId() {
		return riskItemId;
	}
	public void setRiskItemId(String riskItemId) {
		this.riskItemId = riskItemId;
	}
	public String getRiskItemName() {
		return riskItemName;
	}
	public void setRiskItemName(String riskItemName) {
		this.riskItemName = riskItemName;
	}
	public String getRiskItemType() {
		return riskItemType;
	}
	public void setRiskItemType(String riskItemType) {
		this.riskItemType = riskItemType;
	}
}
