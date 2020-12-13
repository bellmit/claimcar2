package ins.sino.claimcar.trafficplatform.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;


@XStreamAlias("Packet")
public class EWVClaimRequest implements Serializable{

	private static final long serialVersionUID = -5078370412227486079L;
	
	@XStreamAsAttribute
	@XStreamAlias("type")
	private String type="REQUEST";
	
	@XStreamAsAttribute
	@XStreamAlias("version")
	private String version="1.0";

	@XStreamAlias("Head") 
	private EWReqHead head;
	
	@XStreamAlias("Body") 
	private EWVClaimRequestBody body;

	
	public EWReqHead getHead() {
		return head;
	}

	
	public void setHead(EWReqHead head) {
		this.head = head;
	}

	
	public EWVClaimRequestBody getBody() {
		return body;
	}

	
	public void setBody(EWVClaimRequestBody body) {
		this.body = body;
	}
	
	


}

