package ins.sino.claimcar.moblie.msgNotified.vo;

import ins.sino.claimcar.flow.vo.MsgNotifiedResBody;
import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckResponseHead;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("PACKET")
public class MsgNotifiedResPacket implements Serializable {

	/**  */
	private static final long serialVersionUID = -3716951437591835305L;
	
	@XStreamAlias("HEAD")
	MobileCheckResponseHead head;
	@XStreamAlias("BODY")
	MsgNotifiedResBody resBody;

	public MobileCheckResponseHead getHead() {
		return head;
	}

	public void setHead(MobileCheckResponseHead head) {
		this.head = head;
	}

	public MsgNotifiedResBody getResBody() {
		return resBody;
	}

	public void setResBody(MsgNotifiedResBody resBody) {
		this.resBody = resBody;
	}
	
}
