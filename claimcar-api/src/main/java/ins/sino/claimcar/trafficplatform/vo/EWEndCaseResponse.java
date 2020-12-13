package ins.sino.claimcar.trafficplatform.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("Packet")
public class EWEndCaseResponse implements Serializable {

	private static final long serialVersionUID = 5272687491480964121L;
	
	
	@XStreamAsAttribute
	@XStreamAlias("type")
	private String type="REQUEST";
	
	@XStreamAsAttribute
	@XStreamAlias("version")
	private String version="1.0";
	
	@XStreamAlias("Head")
	private EWResHead head;
	
	@XStreamAlias("Body")	
	private EWEndCaseResponseBody body;

	public EWResHead getHead() {
		return head;
	}

	public void setHead(EWResHead head) {
		this.head = head;
	}

	public EWEndCaseResponseBody getBody() {
		return body;
	}

	public void setBody(EWEndCaseResponseBody body) {
		this.body = body;
	}

	
	
}
