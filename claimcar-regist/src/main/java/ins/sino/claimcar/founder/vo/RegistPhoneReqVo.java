package ins.sino.claimcar.founder.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("PACKET")
public class RegistPhoneReqVo {

	@XStreamAlias("HEAD")
	private CommonReqHeadVo head;
	
	@XStreamAlias("BODY")
	private RegistPhoneReqBodyVo body;

	public CommonReqHeadVo getHead() {
		return head;
	}

	public void setHead(CommonReqHeadVo head) {
		this.head = head;
	}

	public RegistPhoneReqBodyVo getBody() {
		return body;
	}

	public void setBody(RegistPhoneReqBodyVo body) {
		this.body = body;
	}
}
