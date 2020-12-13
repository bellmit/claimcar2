package ins.sino.claimcar.moblie.commonmark.vo;

import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckResponseHead;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("PACKET")
public class MCCommonRemarksPacket implements Serializable {

	/**  */
	private static final long serialVersionUID = 5272687491480964121L;
	
	@XStreamAlias("HEAD")
	private MobileCheckResponseHead head;
	
	@XStreamAlias("BODY")
	private MCCommonRemarksBody body;

	public MobileCheckResponseHead getHead() {
		return head;
	}

	public void setHead(MobileCheckResponseHead head) {
		this.head = head;
	}

	public MCCommonRemarksBody getBody() {
		return body;
	}

	public void setBody(MCCommonRemarksBody body) {
		this.body = body;
	}
	
}
