package ins.sino.claimcar.selfHelpClaimCar.vo;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("BODY")
public class DlossAmoutConfirmBodyVo{
	@XStreamAlias("CASECARSTATELIST")
	private List<casecarStateVo> casecarStates;

	public List<casecarStateVo> getCasecarStates() {
		return casecarStates;
	}

	public void setCasecarStates(List<casecarStateVo> casecarStates) {
		this.casecarStates = casecarStates;
	}

	
}
