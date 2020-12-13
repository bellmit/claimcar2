package ins.sino.claimcar.mtainterface.check.vo;

import ins.sino.claimcar.carchild.check.vo.CTCheckInfoInitReqBodyVo;
import ins.sino.claimcar.carchildCommon.vo.CarchildHeadVo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("PACKET")
public class MTACheckInfoInitReqVo implements Serializable{

private static final long serialVersionUID = 1L;
	
	@XStreamAlias("HEAD") 
	private CarchildHeadVo head;
	
	@XStreamAlias("BODY") 
	private MTACheckInfoInitReqBodyVo body;

	public CarchildHeadVo getHead() {
		return head;
	}

	public void setHead(CarchildHeadVo head) {
		this.head = head;
	}

	public MTACheckInfoInitReqBodyVo getBody() {
		return body;
	}

	public void setBody(MTACheckInfoInitReqBodyVo body) {
		this.body = body;
	}
	
}
