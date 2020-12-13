package ins.sino.claimcar.claim.claimjy.task.vo;

import ins.sino.claimcar.claimjy.vo.JyReqHeadVo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("PACKET")
public class JyTaskReqVo implements Serializable{
	private static final long serialVersionUID = 1L;
	@XStreamAlias("HEAD") 
    private JyReqHeadVo head;
	@XStreamAlias("BODY")
	private JyTaskReqBodyVo body;
	public JyReqHeadVo getHead() {
		return head;
	}
	public void setHead(JyReqHeadVo head) {
		this.head = head;
	}
	public JyTaskReqBodyVo getBody() {
		return body;
	}
	public void setBody(JyTaskReqBodyVo body) {
		this.body = body;
	}
	
	
	 
}
