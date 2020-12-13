package ins.sino.claimcar.moblie.loss.vo;

import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckResponseHead;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("PACKET")
public class SubmitDeflossRespond {

	private static final long serialVersionUID = 5272687491480964121L;
	
	@XStreamAlias("HEAD")
	private MobileCheckResponseHead head;

	public MobileCheckResponseHead getHead() {
		return head;
	}

	public void setHead(MobileCheckResponseHead head) {
		this.head = head;
	}
	
	
}
