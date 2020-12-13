package ins.sino.claimcar.founder.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("PACKET")
public class RegistPhoneResVo {
	
	@XStreamAlias("HEAD")
	private RegistPhoneResHeadVo head;
	
	@XStreamAlias("BODY")
	private RegistPhoneResBodyVo body;

	public RegistPhoneResHeadVo getHead() {
		return head;
	}

	public void setHead(RegistPhoneResHeadVo head) {
		this.head = head;
	}

	public RegistPhoneResBodyVo getBody() {
		return body;
	}

	public void setBody(RegistPhoneResBodyVo body) {
		this.body = body;
	}
}
