package ins.sino.claim.founder.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 理赔系统返回客服系统报文
 * @author j2eel
 *
 */
@XStreamAlias("PACKET")
public class FounderToClaimQueryRespVo implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("HEAD")
	private FounderToClaimQueryRespHead head;

	@XStreamAlias("BODY")
	private FounderToClaimQueryRespBody body;

	public FounderToClaimQueryRespHead getHead() {
		return head;
	}

	public void setHead(FounderToClaimQueryRespHead head) {
		this.head = head;
	}

	public FounderToClaimQueryRespBody getBody() {
		return body;
	}

	public void setBody(FounderToClaimQueryRespBody body) {
		this.body = body;
	}
	
}
