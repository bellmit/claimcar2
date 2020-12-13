package ins.sino.claimcar.trafficplatform.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("Packet")
public class EWFalseCaseRequest implements Serializable {
	
	@XStreamAsAttribute
	@XStreamAlias("type")
	private String type="REQUEST";
	
	@XStreamAsAttribute
	@XStreamAlias("version")
	private String version="1.0";

	private static final long serialVersionUID = 5272687491480964121L;
	
	@XStreamAlias("Head")
	private EWReqHead head;
	
	@XStreamAlias("Body")
	private EWFalseCaseBody body;

	public EWReqHead getHead() {
		return head;
	}

	public void setHead(EWReqHead head) {
		this.head = head;
	}

	public EWFalseCaseBody getBody() {
		return body;
	}

	public void setBody(EWFalseCaseBody body) {
		this.body = body;
	}
	
}
