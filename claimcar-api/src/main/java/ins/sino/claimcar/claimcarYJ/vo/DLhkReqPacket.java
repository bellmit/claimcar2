package ins.sino.claimcar.claimcarYJ.vo;

import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckHead;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("PACKET")
public class DLhkReqPacket implements Serializable{

	/**  */
	private static final long serialVersionUID = -509503883975704673L;
	
	@XStreamAlias("HEAD")
	private MobileCheckHead head;
	
	@XStreamAlias("BODY")
	private DLhkReqBody body;

	public MobileCheckHead getHead() {
		return head;
	}

	public void setHead(MobileCheckHead head) {
		this.head = head;
	}

	public DLhkReqBody getBody() {
		return body;
	}

	public void setBody(DLhkReqBody body) {
		this.body = body;
	}

	

}
