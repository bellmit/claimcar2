package ins.sino.claimcar.selfHelpClaimCar.vo;

import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckHead;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("PACKET")
public class SelfCaseDetailsRequest implements Serializable{

	/**  */
	private static final long serialVersionUID = -5078370412227486079L;

	@XStreamAlias("HEAD") 
	private MobileCheckHead head;
	
	@XStreamAlias("BODY") 
	private SelfCaseDetailsBody body;

	public MobileCheckHead getHead() {
		return head;
	}

	public void setHead(MobileCheckHead head) {
		this.head = head;
	}

	public SelfCaseDetailsBody getBody() {
		return body;
	}

	public void setBody(SelfCaseDetailsBody body) {
		this.body = body;
	}


}
