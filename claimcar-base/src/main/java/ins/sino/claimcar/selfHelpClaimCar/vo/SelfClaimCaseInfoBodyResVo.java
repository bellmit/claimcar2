package ins.sino.claimcar.selfHelpClaimCar.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("BODY")
public class SelfClaimCaseInfoBodyResVo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("CASEINFO")
	private SelfClaimCaseCarInfoResVo autoClaimCaseCarInfoResVo;

	public SelfClaimCaseCarInfoResVo getAutoClaimCaseCarInfoResVo() {
		return autoClaimCaseCarInfoResVo;
	}

	public void setAutoClaimCaseCarInfoResVo(
			SelfClaimCaseCarInfoResVo autoClaimCaseCarInfoResVo) {
		this.autoClaimCaseCarInfoResVo = autoClaimCaseCarInfoResVo;
	}

	
	
}
