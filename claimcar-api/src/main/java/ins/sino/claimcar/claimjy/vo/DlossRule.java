package ins.sino.claimcar.claimjy.vo;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Rule")
@XmlAccessorType(XmlAccessType.FIELD)
public class DlossRule implements Serializable{
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "RuleCode")
	private int ruleCode;//标示唯一规则ID
	@XmlElement(name = "GroupType")
	private String groupType;//组别 案件风险、提示类、定损风险
	@XmlElement(name = "RuleType")
	private String ruleType;//风险类别
	@XmlElement(name = "RuleModel")
	private String ruleModel;//规则模型
	@XmlElement(name = "LossRuleScore")
	private int lossRuleScore;//规则分值
	@XmlElement(name = "RiskItemId")
	private String riskItemId;//触发项目ID 多个项目逗号分隔
	@XmlElement(name = "RiskItemName")
	private String riskItemName;//触发项目名称 多个项目逗号分隔
	@XmlElement(name = "RiskItemType")
	private String riskItemType;//触发项目类型
	
	public int getRuleCode() {
		return ruleCode;
	}
	public void setRuleCode(int ruleCode) {
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
	public int getLossRuleScore() {
		return lossRuleScore;
	}
	public void setLossRuleScore(int lossRuleScore) {
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
