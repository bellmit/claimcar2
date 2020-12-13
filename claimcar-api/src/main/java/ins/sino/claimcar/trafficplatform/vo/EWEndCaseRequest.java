package ins.sino.claimcar.trafficplatform.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;


@XStreamAlias("Packet")
public class EWEndCaseRequest implements Serializable{

	private static final long serialVersionUID = -5078370412227486079L;

	@XStreamAlias("Head") 
	private EWReqHead head;
	
	@XStreamAlias("Body") 
	private EWEndCaseRequestBody body;

	
	public EWReqHead getHead() {
		return head;
	}

	
	public void setHead(EWReqHead head) {
		this.head = head;
	}

	
	public EWEndCaseRequestBody getBody() {
		return body;
	}

	
	public void setBody(EWEndCaseRequestBody body) {
		this.body = body;
	}
	
	


}

