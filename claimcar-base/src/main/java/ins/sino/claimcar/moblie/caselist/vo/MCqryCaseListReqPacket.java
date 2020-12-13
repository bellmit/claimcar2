package ins.sino.claimcar.moblie.caselist.vo;

import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckHead;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("PACKET")
public class MCqryCaseListReqPacket implements Serializable{

	/**  */
	private static final long serialVersionUID = -509503883975704673L;
	
	@XStreamAlias("HEAD")
	private MobileCheckHead head;
	
	@XStreamAlias("BODY")
	private MCqryCaseListReqBody body;

	public MobileCheckHead getHead() {
		return head;
	}

	public void setHead(MobileCheckHead head) {
		this.head = head;
	}

	public MCqryCaseListReqBody getBody() {
		return body;
	}

	public void setBody(MCqryCaseListReqBody body) {
		this.body = body;
	}
	

}
