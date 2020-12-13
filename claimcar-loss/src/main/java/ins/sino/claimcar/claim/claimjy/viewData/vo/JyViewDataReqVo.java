package ins.sino.claimcar.claim.claimjy.viewData.vo;

import ins.sino.claimcar.claimjy.vo.JyReqHeadVo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("PACKET")
public class JyViewDataReqVo implements Serializable{
	private static final long serialVersionUID = 1L;
	@XStreamAlias("HEAD") 
    private JyReqHeadVo head;
	@XStreamAlias("BODY")
	private JyViewDataReqBodyVo body;
	public JyReqHeadVo getHead() {
		return head;
	}
	public void setHead(JyReqHeadVo head) {
		this.head = head;
	}
	public JyViewDataReqBodyVo getBody() {
		return body;
	}
	public void setBody(JyViewDataReqBodyVo body) {
		this.body = body;
	}
	
	
	 
}
