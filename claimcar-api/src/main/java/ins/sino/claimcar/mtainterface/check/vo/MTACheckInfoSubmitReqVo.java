package ins.sino.claimcar.mtainterface.check.vo;

import ins.sino.claimcar.carchildCommon.vo.CarchildHeadVo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("PACKET")
public class MTACheckInfoSubmitReqVo implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("HEAD") 
	private CarchildHeadVo head;
	
	@XStreamAlias("BODY") 
	private MTACheckInfoSubmitReqBodyVo body;

	public CarchildHeadVo getHead() {
		return head;
	}

	public void setHead(CarchildHeadVo head) {
		this.head = head;
	}

	public MTACheckInfoSubmitReqBodyVo getBody() {
		return body;
	}

	public void setBody(MTACheckInfoSubmitReqBodyVo body) {
		this.body = body;
	}
	
}
