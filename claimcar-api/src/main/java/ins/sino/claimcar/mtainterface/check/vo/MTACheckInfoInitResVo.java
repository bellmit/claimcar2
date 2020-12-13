package ins.sino.claimcar.mtainterface.check.vo;

import ins.sino.claimcar.carchildCommon.vo.CarchildResponseHeadVo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("PACKET")
public class MTACheckInfoInitResVo  implements Serializable{

private static final long serialVersionUID = 1L;
	
	@XStreamAlias("HEAD")
	private CarchildResponseHeadVo head;
	
	@XStreamAlias("BODY") 
	private MTACheckInfoInitResBodyVo body;

	public CarchildResponseHeadVo getHead() {
		return head;
	}

	public void setHead(CarchildResponseHeadVo head) {
		this.head = head;
	}

	public MTACheckInfoInitResBodyVo getBody() {
		return body;
	}

	public void setBody(MTACheckInfoInitResBodyVo body) {
		this.body = body;
	}
}
