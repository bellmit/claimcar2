package ins.sino.claimcar.ciitc.vo.compe;

import ins.sino.claimcar.ciitc.vo.CiitcReqHeadVo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Packet")
public class CiitcCompeReqVo  implements Serializable{

	private static final long serialVersionUID = 1L;
	@XStreamAlias("Head") 
    private CiitcReqHeadVo head;
	@XStreamAlias("Body") 
    private CiitcCompeReqBody body;
	
	public CiitcReqHeadVo getHead() {
		return head;
	}
	public void setHead(CiitcReqHeadVo head) {
		this.head = head;
	}
	public CiitcCompeReqBody getBody() {
		return body;
	}
	public void setBody(CiitcCompeReqBody body) {
		this.body = body;
	}
	
	
}
