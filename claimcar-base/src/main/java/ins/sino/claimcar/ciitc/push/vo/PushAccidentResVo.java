package ins.sino.claimcar.ciitc.push.vo;

import ins.sino.claimcar.ciitc.vo.CiitcResHeadVo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("Packet")
public class PushAccidentResVo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("Head")
	private CiitcResHeadVo head;
	
	@XStreamAlias("Body")
	private PushAccidentResBodyVo body;

	public CiitcResHeadVo getHead() {
		return head;
	}

	public void setHead(CiitcResHeadVo head) {
		this.head = head;
	}

	public PushAccidentResBodyVo getBody() {
		return body;
	}

	public void setBody(PushAccidentResBodyVo body) {
		this.body = body;
	}

}
