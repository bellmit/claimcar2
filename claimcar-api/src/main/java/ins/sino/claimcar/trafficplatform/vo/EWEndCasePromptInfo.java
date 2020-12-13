package ins.sino.claimcar.trafficplatform.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("PromptInfo")
public class EWEndCasePromptInfo implements Serializable {

	private static final long serialVersionUID = 5272687491480964121L;
	
	@XStreamAlias("ClaimSequenceNo")
	private String ClaimSequenceNo;
	
	@XStreamAlias("PromptInformation")
	private String promptInformation;

	
	public String getClaimSequenceNo() {
		return ClaimSequenceNo;
	}

	
	public void setClaimSequenceNo(String claimSequenceNo) {
		ClaimSequenceNo = claimSequenceNo;
	}

	
	public String getPromptInformation() {
		return promptInformation;
	}

	
	public void setPromptInformation(String promptInformation) {
		this.promptInformation = promptInformation;
	}

	
}
