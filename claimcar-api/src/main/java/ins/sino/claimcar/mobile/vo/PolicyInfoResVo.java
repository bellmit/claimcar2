package ins.sino.claimcar.mobile.vo;

import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckResponseHead;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("PACKET")
public class PolicyInfoResVo  implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;

	@XStreamAlias("HEAD")
	private MobileCheckResponseHead head;
	
	@XStreamAlias("BODY")
	private PolicyInfoResBodyVo body;



	public MobileCheckResponseHead getHead() {
		return head;
	}

	public void setHead(MobileCheckResponseHead head) {
		this.head = head;
	}

	public PolicyInfoResBodyVo getBody() {
		return body;
	}

	public void setBody(PolicyInfoResBodyVo body) {
		this.body = body;
	}

	
}
