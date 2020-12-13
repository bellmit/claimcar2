package ins.sino.claimcar.claimjy.vo.repair;

import ins.sino.claimcar.claimjy.vo.JyReqHeadVo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("PACKET")
public class JyFactoryBrandAndInfoPacketVo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("HEAD")
	private JyReqHeadVo head; 
	@XStreamAlias("BODY")
	private JyFactoryBrandAndInfoBodyVo body;
	
	public JyReqHeadVo getHead() {
		return head;
	}
	public void setHead(JyReqHeadVo head) {
		this.head = head;
	}
	public JyFactoryBrandAndInfoBodyVo getBody() {
		return body;
	}
	public void setBody(JyFactoryBrandAndInfoBodyVo body) {
		this.body = body;
	}
	
}
