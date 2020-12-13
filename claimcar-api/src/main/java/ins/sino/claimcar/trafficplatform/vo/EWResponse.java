package ins.sino.claimcar.trafficplatform.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Packet")
public class EWResponse implements Serializable {

	private static final long serialVersionUID = 5272687491480964121L;
	
	@XStreamAlias("Head")
	private EWResHead head;

	public EWResHead getHead() {
		return head;
	}

	public void setHead(EWResHead head) {
		this.head = head;
	}
}
