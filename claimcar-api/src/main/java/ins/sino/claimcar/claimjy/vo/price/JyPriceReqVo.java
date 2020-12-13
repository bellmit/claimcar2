package ins.sino.claimcar.claimjy.vo.price;

import ins.sino.claimcar.claimjy.vo.JyReqHeadVo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("PACKET") 
public class JyPriceReqVo  implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("HEAD") 
    private JyReqHeadVo  head;

	@XStreamAlias("BODY") 
	private JyPriceReqBody body;

	public JyReqHeadVo getHead() {
		return head;
	}

	public void setHead(JyReqHeadVo head) {
		this.head = head;
	}

	public JyPriceReqBody getBody() {
		return body;
	}

	public void setBody(JyPriceReqBody body) {
		this.body = body;
	}
	
	
}
