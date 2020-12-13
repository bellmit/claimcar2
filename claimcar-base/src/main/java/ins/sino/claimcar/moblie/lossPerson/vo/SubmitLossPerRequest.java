package ins.sino.claimcar.moblie.lossPerson.vo;

import java.io.Serializable;

import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckHead;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("PACKET")
public class SubmitLossPerRequest implements Serializable{

	private static final long serialVersionUID = -5078370412227486079L;
	
	@XStreamAlias("HEAD") 
	private MobileCheckHead head;
	
	@XStreamAlias("BODY")
	private SubmitLossPerRequestBody body;

	public MobileCheckHead getHead() {
		return head;
	}

	public void setHead(MobileCheckHead head) {
		this.head = head;
	}

	public SubmitLossPerRequestBody getBody() {
		return body;
	}

	public void setBody(SubmitLossPerRequestBody body) {
		this.body = body;
	}

	
	
}
