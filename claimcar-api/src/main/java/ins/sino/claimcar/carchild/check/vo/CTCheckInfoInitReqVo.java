package ins.sino.claimcar.carchild.check.vo;

import ins.sino.claimcar.carchildCommon.vo.CarchildHeadVo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("PACKET")
public class CTCheckInfoInitReqVo implements Serializable{

	/**  */
	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("HEAD") 
	private CarchildHeadVo head;
	
	@XStreamAlias("BODY") 
	private CTCheckInfoInitReqBodyVo body;

	public CarchildHeadVo getHead() {
		return head;
	}

	public void setHead(CarchildHeadVo head) {
		this.head = head;
	}

	public CTCheckInfoInitReqBodyVo getBody() {
		return body;
	}

	public void setBody(CTCheckInfoInitReqBodyVo body) {
		this.body = body;
	}
	
}
