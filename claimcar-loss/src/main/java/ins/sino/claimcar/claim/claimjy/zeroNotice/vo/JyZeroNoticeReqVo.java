package ins.sino.claimcar.claim.claimjy.zeroNotice.vo;

import ins.sino.claimcar.claimjy.vo.JyReqHeadVo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("PACKET")
public class JyZeroNoticeReqVo implements Serializable{
	private static final long serialVersionUID = 1L;
	@XStreamAlias("HEAD") 
    private JyReqHeadVo head;
	@XStreamAlias("BODY")
	private JyZeroNoticeBodyVo body;
	public JyReqHeadVo getHead() {
		return head;
	}
	public void setHead(JyReqHeadVo head) {
		this.head = head;
	}
	public JyZeroNoticeBodyVo getBody() {
		return body;
	}
	public void setBody(JyZeroNoticeBodyVo body) {
		this.body = body;
	}

	
	
	 
	 
}
