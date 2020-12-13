package ins.sino.claimcar.ciitc.vo.accident;

import ins.sino.claimcar.ciitc.vo.CiitcResHeadVo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("Packet")
public class CiitcAccidentResVo implements Serializable{
	private static final long serialVersionUID = 1L;
	@XStreamAlias("Head")
	private CiitcResHeadVo head;
	
	@XStreamAlias("Body")
	private CiitcAccidentResBody body;

	public CiitcResHeadVo getHead() {
		return head;
	}

	public void setHead(CiitcResHeadVo head) {
		this.head = head;
	}

	public CiitcAccidentResBody getBody() {
		return body;
	}

	public void setBody(CiitcAccidentResBody body) {
		this.body = body;
	}

	
	
}
