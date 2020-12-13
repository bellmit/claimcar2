package ins.sino.claimcar.moblie.lossPerson.vo;

import java.io.Serializable;

import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckResponseHead;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("PACKET")
public class SubmitLossPerRespond implements Serializable{

	private static final long serialVersionUID = 5272687491480968131L;
	
	@XStreamAlias("HEAD")
	private MobileCheckResponseHead head;

	public MobileCheckResponseHead getHead() {
		return head;
	}

	public void setHead(MobileCheckResponseHead head) {
		this.head = head;
	}
	
	
}
