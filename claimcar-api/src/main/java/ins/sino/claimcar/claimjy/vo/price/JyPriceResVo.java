package ins.sino.claimcar.claimjy.vo.price;

import ins.sino.claimcar.claimjy.vo.JyResHeadVo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("PACKET")
public class JyPriceResVo  implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("HEAD")
	private JyResHeadVo head;
	
	@XStreamAlias("BODY")
	private JyPriceResBody body;

	public JyResHeadVo getHead() {
		return head;
	}

	public void setHead(JyResHeadVo head) {
		this.head = head;
	}

	public JyPriceResBody getBody() {
		return body;
	}

	public void setBody(JyPriceResBody body) {
		this.body = body;
	}

}
