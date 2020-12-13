package ins.sino.claimcar.ilog.defloss.vo;


import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("root")
public class LIlogRuleResVo  implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("state")
	private String state; //关于state值的说明 0：交互出现异常1：交互正常
	

    
    @XStreamAlias("businessNo")
    private String businessNo; //报案号
    
    @XStreamAlias("underwriterflag")
    private String underwriterflag; //1：核价自动通过 2：核价转人工 3：默认转人工 4：当前无审核权限 5：审核通过
    
    @XStreamAlias("minUndwrtNode")
    private String minUndwrtNode; //最低审核通过级别
    
    @XStreamAlias("maxUndwrtNode")
    private String maxUndwrtNode; //分公司最高级
    
    @XStreamAlias("content")
    private String content; 
    
    @XStreamAlias("triggerInformationList")
    private List<triggerInformation> triggerInformation;//触发规则明细信息

    
    public String getState() {
        return state;
    }

    
    public void setState(String state) {
        this.state = state;
    }

    
    public String getBusinessNo() {
        return businessNo;
    }

    
    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo;
    }

    
    public String getUnderwriterflag() {
        return underwriterflag;
    }

    
    public void setUnderwriterflag(String underwriterfla) {
        this.underwriterflag = underwriterfla;
    }

    
    public String getMinUndwrtNode() {
        return minUndwrtNode;
    }

    
    public void setMinUndwrtNode(String minUndwrtNode) {
        this.minUndwrtNode = minUndwrtNode;
    }

    
    public String getMaxUndwrtNode() {
        return maxUndwrtNode;
    }

    
    public void setMaxUndwrtNode(String maxUndwrtNode) {
        this.maxUndwrtNode = maxUndwrtNode;
    }

    
    public List<triggerInformation> getTriggerInformation() {
        return triggerInformation;
    }

    
    public void setTriggerInformation(List<triggerInformation> triggerInformation) {
        this.triggerInformation = triggerInformation;
    }


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}

    
}
