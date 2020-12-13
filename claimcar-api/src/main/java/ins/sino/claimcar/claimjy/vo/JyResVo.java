package ins.sino.claimcar.claimjy.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("PACKET")
public class JyResVo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("HEAD")
	private JyResHeadVo head;
	@XStreamAlias("BODY")
	private JyResBody body;
	
	public JyResHeadVo getHead() {
		return head;
	}
	public void setHead(JyResHeadVo head) {
		this.head = head;
	}
	public JyResBody getBody() {
		return body;
	}
	public void setBody(JyResBody body) {
		this.body = body;
	}
}
