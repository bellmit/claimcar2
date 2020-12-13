package ins.sino.claimcar.mobile.vo;

import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckResponseHead;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("PACKET")
public class RegistInfoResVo  implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;

	@XStreamAlias("HEAD")
	private MobileCheckResponseHead head;
	
	@XStreamAlias("BODY")
	private RegistInfoResBodyVo body;

	public RegistInfoResBodyVo getBody() {
		return body;
	}

	public void setBody(RegistInfoResBodyVo body) {
		this.body = body;
	}

	public MobileCheckResponseHead getHead() {
		return head;
	}

	public void setHead(MobileCheckResponseHead head) {
		this.head = head;
	}

	
}
