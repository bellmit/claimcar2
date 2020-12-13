package ins.sino.claimcar.ciitc.vo.compe;

import ins.sino.claimcar.ciitc.vo.CiitcResHeadVo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("Packet")
public class CiitcCompeResVo implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("Head") 
	private CiitcResHeadVo head;
	
	@XStreamAlias("Body") 
	private CiitcCompeResBody body;

	public CiitcCompeResBody getBody() {
		return body;
	}

	public void setBody(CiitcCompeResBody body) {
		this.body = body;
	}

	public CiitcResHeadVo getHead() {
		return head;
	}

	public void setHead(CiitcResHeadVo head) {
		this.head = head;
	}
	
	
	
}
