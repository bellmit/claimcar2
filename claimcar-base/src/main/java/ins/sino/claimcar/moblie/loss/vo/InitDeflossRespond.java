package ins.sino.claimcar.moblie.loss.vo;

import java.io.Serializable;

import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckResponseHead;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("PACKET")
public class InitDeflossRespond implements Serializable{
	private static final long serialVersionUID = -8883161640950238423L;
	@XStreamAlias("HEAD")
	private MobileCheckResponseHead head;
	@XStreamAlias("BODY")
	private InitDeflossResBody body;

	public MobileCheckResponseHead getHead() {
		return head;
	}

	public void setHead(MobileCheckResponseHead head) {
		this.head = head;
	}

	public InitDeflossResBody getBody() {
		return body;
	}

	public void setBody(InitDeflossResBody body) {
		this.body = body;
	}
}
