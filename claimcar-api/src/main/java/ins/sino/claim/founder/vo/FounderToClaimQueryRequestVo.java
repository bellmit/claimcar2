package ins.sino.claim.founder.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("PACKET")
public class FounderToClaimQueryRequestVo implements Serializable{
	
	private static final long serialVersionUID = -5078370412227486079L;

	@XStreamAlias("HEAD")
	private String head;
	
	@XStreamAlias("BODY")
	private FounderToClaimQueryReqBody body;

	
	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public FounderToClaimQueryReqBody getBody() {
		return body;
	}

	public void setBody(FounderToClaimQueryReqBody body) {
		this.body = body;
	}
}
