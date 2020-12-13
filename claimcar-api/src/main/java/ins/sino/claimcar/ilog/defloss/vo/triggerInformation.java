package ins.sino.claimcar.ilog.defloss.vo;


import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("triggerInformation")
public class triggerInformation  implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("ruleLibrary")
	private String ruleLibrary; //规则库名称

    @XStreamAlias("rulePath")
    private String rulePath; //规则路径
    
    @XStreamAlias("ruleContent")
    private String ruleContent; //规则提示信息
    
    @XStreamAlias("ruleEntryDetail")
    private String ruleEntryDetail; //规则名称
    
    @XStreamAlias("ruleType")
    private String ruleType; //规则类型 01:车辆核价转人工 02:车辆核价自动通过 03:车辆核价权限 99:其它

    
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
