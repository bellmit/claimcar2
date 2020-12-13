package ins.sino.claimcar.carchildren.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("PACKET")
public class CaseCancleNopassReqVo {
	 @XStreamAlias("HEAD") 
     private CarchildReqHeadVo head;
     @XStreamAlias("BODY")
     private CaseCancleNopassReqBodyVo body;
     
	 public CarchildReqHeadVo getHead() {
		return head;
	}
	public void setHead(CarchildReqHeadVo head) {
		this.head = head;
	}
	public CaseCancleNopassReqBodyVo getBody() {
		return body;
	}
	public void setBody(CaseCancleNopassReqBodyVo body) {
		this.body = body;
	}
     
}
