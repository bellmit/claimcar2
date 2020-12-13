package ins.sino.claimcar.mobileCheckCommon.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("PACKET")
public class MobileCheckRequest implements Serializable{

	/**  */
	private static final long serialVersionUID = -5078370412227486079L;

	@XStreamAlias("HEAD") 
	private MobileCheckHead head;
	
	@XStreamAlias("BODY") 
	private MobileCheckBody body;

	public MobileCheckHead getHead() {
		return head;
	}

	public void setHead(MobileCheckHead head) {
		this.head = head;
	}

	public MobileCheckBody getBody() {
		return body;
	}

	public void setBody(MobileCheckBody body) {
		this.body = body;
	}
}
