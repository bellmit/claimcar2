package ins.sino.claimcar.carchild.check.vo;

import ins.sino.claimcar.carchildCommon.vo.CarchildHeadVo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("PACKET")
public class CTCheckInfoSubmitReqVo implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("HEAD") 
	private CarchildHeadVo head;
	
	@XStreamAlias("BODY") 
	private CTCheckInfoSubmitReqBodyVo body;

	public CarchildHeadVo getHead() {
		return head;
	}

	public void setHead(CarchildHeadVo head) {
		this.head = head;
	}

	public CTCheckInfoSubmitReqBodyVo getBody() {
		return body;
	}

	public void setBody(CTCheckInfoSubmitReqBodyVo body) {
		this.body = body;
	}
	
}
