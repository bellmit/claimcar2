package ins.sino.claimcar.moblie.msgNotified.vo;

import ins.sino.claimcar.flow.vo.MsgNotifiedBody;
import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckHead;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("PACKET")
public class MsgNotifiedPacket implements Serializable {

	/**  */
	private static final long serialVersionUID = -438062301365942938L;
	
	@XStreamAlias("HEAD")
	private MobileCheckHead head;
	
	@XStreamAlias("BODY")
	private MsgNotifiedBody body;

	public MobileCheckHead getHead() {
		return head;
	}

	public void setHead(MobileCheckHead head) {
		this.head = head;
	}

	public MsgNotifiedBody getBody() {
		return body;
	}

	public void setBody(MsgNotifiedBody body) {
		this.body = body;
	}

}
