package ins.sino.claimcar.ciitc.push.vo;

import ins.sino.claimcar.ciitc.vo.CiitcReqHeadVo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Packet")
public class PushAccidentReqVo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("Head") 
    private CiitcReqHeadVo head;
	
	@XStreamAlias("Body")
	private PushAccidentReqBodyVo body;
	
	public CiitcReqHeadVo getHead() {
		return head;
	}
	
	public void setHead(CiitcReqHeadVo head) {
		this.head = head;
	}
	
	public PushAccidentReqBodyVo getBody() {
		return body;
	}
	
	public void setBody(PushAccidentReqBodyVo body) {
		this.body = body;
	}
	
}
