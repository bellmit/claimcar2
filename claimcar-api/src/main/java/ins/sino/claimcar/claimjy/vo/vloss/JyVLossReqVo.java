package ins.sino.claimcar.claimjy.vo.vloss;

import ins.sino.claimcar.claimjy.vo.JyReqHeadVo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("PACKET") 
public class JyVLossReqVo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("HEAD") 
    private JyReqHeadVo  head;
	
	@XStreamAlias("BODY") 
	private JyVLossReqBody body;

	public JyReqHeadVo getHead() {
		return head;
	}

	public void setHead(JyReqHeadVo head) {
		this.head = head;
	}

	public JyVLossReqBody getBody() {
		return body;
	}

	public void setBody(JyVLossReqBody body) {
		this.body = body;
	}
	
	
	

}
