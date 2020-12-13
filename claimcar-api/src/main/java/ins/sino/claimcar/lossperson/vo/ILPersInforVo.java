package ins.sino.claimcar.lossperson.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("triggerInformation")
public class ILPersInforVo implements Serializable {

	private static final long serialVersionUID = 8423652723600188374L;
	
	@XStreamAlias("ruleLibrary")
	private String ruleLibrary;
	@XStreamAlias("rulePath")
	private String rulePath;
	@XStreamAlias("ruleContent")
	private String ruleContent;
	@XStreamAlias("ruleEntryDetail")
	private String ruleEntryDetail;
	@XStreamAlias("ruleType")
	private String ruleType;
	
	public String getRuleLibrary() {
		return ruleLibrary;
	}
	public void setRuleLibrary(String ruleLibrary) {
		this.ruleLibrary = ruleLibrary;
	}
	public String getRulePath() {
		return rulePath;
	}
	public void setRulePath(String rulePath) {
		this.rulePath = rulePath;
	}
	public String getRuleContent() {
		return ruleContent;
	}
	public void setRuleContent(String ruleContent) {
		this.ruleContent = ruleContent;
	}
	public String getRuleEntryDetail() {
		return ruleEntryDetail;
	}
	public void setRuleEntryDetail(String ruleEntryDetail) {
		this.ruleEntryDetail = ruleEntryDetail;
	}
	public String getRuleType() {
		return ruleType;
	}
	public void setRuleType(String ruleType) {
		this.ruleType = ruleType;
	}
	
}
