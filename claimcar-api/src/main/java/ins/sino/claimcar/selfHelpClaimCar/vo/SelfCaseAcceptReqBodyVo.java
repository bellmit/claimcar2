package ins.sino.claimcar.selfHelpClaimCar.vo;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * 
 * 自助案件业务员受理接口
 * @author ★zhujunde
 */
@XStreamAlias("BODY")
public class SelfCaseAcceptReqBodyVo  implements Serializable{
	
    private static final long serialVersionUID = 1L;
    
    @XStreamAlias("CASECARSTATELIST")
    private List<CaseCarState> caseCarStates;

	public List<CaseCarState> getCaseCarStates() {
		return caseCarStates;
	}

	public void setCaseCarStates(List<CaseCarState> caseCarStates) {
		this.caseCarStates = caseCarStates;
	}



}
