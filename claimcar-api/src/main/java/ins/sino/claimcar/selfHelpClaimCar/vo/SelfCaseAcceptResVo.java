package ins.sino.claimcar.selfHelpClaimCar.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("PACKET")
public class SelfCaseAcceptResVo  implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;

	@XStreamAlias("HEAD")
	private SelfCaseAcceptResponseHead head;

	public SelfCaseAcceptResponseHead getHead() {
		return head;
	}

	public void setHead(SelfCaseAcceptResponseHead head) {
		this.head = head;
	}

	

}
