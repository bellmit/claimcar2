package ins.sino.claimcar.selfHelpClaimCar.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("BODY")
public class SelfClaimResultReqBodyVo {
	@XStreamAlias("CASEINFO")
	private CaseInfoVo caseInfoVo;

	public CaseInfoVo getCaseInfoVo() {
		return caseInfoVo;
	}

	public void setCaseInfoVo(CaseInfoVo caseInfoVo) {
		this.caseInfoVo = caseInfoVo;
	}
	

}
