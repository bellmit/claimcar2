package ins.sino.claimcar.moblie.loss.vo;

import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckHead;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("PACKET")
public class InitDeflossRequest implements Serializable{

	private static final long serialVersionUID = -5078370412227486079L;
	
	@XStreamAlias("HEAD") 
	private MobileCheckHead head;
	
	@XStreamAlias("BODY") 
	private InitDeflossReqBody body;

	public MobileCheckHead getHead() {
		return head;
	}

	public void setHead(MobileCheckHead head) {
		this.head = head;
	}

	public InitDeflossReqBody getBody() {
		return body;
	}

	public void setBody(InitDeflossReqBody body) {
		this.body = body;
	}
}
