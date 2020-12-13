package ins.sino.claimcar.selfHelpClaimCar.vo;

import ins.sino.claimcar.selfHelpClaimCar.vo.ResponseHeadVo;
import ins.sino.claimcar.selfHelpClaimCar.vo.ResquestHeadVo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("PACKET")
public class SelfClaimRegisterResVo implements Serializable{

private static final long serialVersionUID = 1L;
	
	@XStreamAlias("HEAD")
	private ResponseHeadVo head;
	
	@XStreamAlias("BODY")
	private SelfClaimCaseInfoBodyResVo caseInfo;

	public ResponseHeadVo getHead() {
		return head;
	}

	public void setHead(ResponseHeadVo head) {
		this.head = head;
	}

	public SelfClaimCaseInfoBodyResVo getCaseInfo() {
		return caseInfo;
	}

	public void setCaseInfo(SelfClaimCaseInfoBodyResVo caseInfo) {
		this.caseInfo = caseInfo;
	}

	
	
}
