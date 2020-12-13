package ins.sino.claimcar.selfHelpClaimCar.vo;

import ins.sino.claimcar.selfHelpClaimCar.vo.ResquestHeadVo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("PACKET")
public class SelfClaimRegisterReqVo implements Serializable{

	private static final long serialVersionUID = 1L;

	@XStreamAlias("HEAD")
	private ResquestHeadVo head;
	
	@XStreamAlias("BODY")
	private SelfClaimCaseInfoBodyVo body;

	public ResquestHeadVo getHead() {
		return head;
	}

	public void setHead(ResquestHeadVo head) {
		this.head = head;
	}

	public SelfClaimCaseInfoBodyVo getBody() {
		return body;
	}

	public void setBody(SelfClaimCaseInfoBodyVo body) {
		this.body = body;
	}
	
	
}
