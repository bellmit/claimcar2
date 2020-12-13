package ins.sino.claimcar.claimjy.vo;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Rules")
@XmlAccessorType(XmlAccessType.FIELD)
public class DlossRules implements Serializable{
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "ReportAllRuleScore")
	private int reportAllRuleScore;//案件类总分数
	@XmlElement(name = "EvalAllRuleScore")
	private int evalAllRuleScore;//定损类总分数
	@XmlElement(name = "EvalRuleNumbers")
	private int evalRuleNumbers;//定损类总条数
	@XmlElement(name = "PointRuleNumbers")
	private int PointRuleNumbers;//定损人工费	
	@XmlElement(name = "Rule")
	private DlossRule dlossRule;
	
	public int getReportAllRuleScore() {
		return reportAllRuleScore;
	}
	public void setReportAllRuleScore(int reportAllRuleScore) {
		this.reportAllRuleScore = reportAllRuleScore;
	}
	public int getEvalAllRuleScore() {
		return evalAllRuleScore;
	}
	public void setEvalAllRuleScore(int evalAllRuleScore) {
		this.evalAllRuleScore = evalAllRuleScore;
	}
	public int getEvalRuleNumbers() {
		return evalRuleNumbers;
	}
	public void setEvalRuleNumbers(int evalRuleNumbers) {
		this.evalRuleNumbers = evalRuleNumbers;
	}
	public int getPointRuleNumbers() {
		return PointRuleNumbers;
	}
	public void setPointRuleNumbers(int pointRuleNumbers) {
		PointRuleNumbers = pointRuleNumbers;
	}
	public DlossRule getDlossRule() {
		return dlossRule;
	}
	public void setDlossRule(DlossRule dlossRule) {
		this.dlossRule = dlossRule;
	}
}
