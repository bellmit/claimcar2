package ins.sino.claimcar.selfHelpClaimCar.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("PACKET")
public class SelfCaseDetailsResVo  implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;

	@XStreamAlias("HEAD")
	private SelfCaseAcceptResponseHead head;

	@XStreamAlias("BODY")
	private SelfCaseDetailsResponseBody body;
	public SelfCaseAcceptResponseHead getHead() {
		return head;
	}

	public void setHead(SelfCaseAcceptResponseHead head) {
		this.head = head;
	}

	public SelfCaseDetailsResponseBody getBody() {
		return body;
	}

	public void setBody(SelfCaseDetailsResponseBody body) {
		this.body = body;
	}

	

}
