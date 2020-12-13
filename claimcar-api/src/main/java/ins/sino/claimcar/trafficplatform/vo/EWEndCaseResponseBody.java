package ins.sino.claimcar.trafficplatform.vo;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;


@XStreamAlias("Body")
public class EWEndCaseResponseBody implements Serializable{

	/**  */
	private static final long serialVersionUID = -5078370412227486079L;
	
	@XStreamAlias("PromptInfo")	
	private EWEndCasePromptInfo promptInfo;

	public EWEndCasePromptInfo getPromptInfo() {
		return promptInfo;
	}

	public void setPromptInfo(EWEndCasePromptInfo promptInfo) {
		this.promptInfo = promptInfo;
	}
	
}

