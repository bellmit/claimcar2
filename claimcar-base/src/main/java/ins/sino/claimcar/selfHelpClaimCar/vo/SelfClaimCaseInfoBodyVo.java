package ins.sino.claimcar.selfHelpClaimCar.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("BODY")
public class SelfClaimCaseInfoBodyVo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("CASEINFO")
	private SelfClaimCaseInfoVo autoClaimCaseInfoVo;

	public SelfClaimCaseInfoVo getAutoClaimCaseInfoVo() {
		return autoClaimCaseInfoVo;
	}

	public void setAutoClaimCaseInfoVo(SelfClaimCaseInfoVo autoClaimCaseInfoVo) {
		this.autoClaimCaseInfoVo = autoClaimCaseInfoVo;
	}
	
	
}
