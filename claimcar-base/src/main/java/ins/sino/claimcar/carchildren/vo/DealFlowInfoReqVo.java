package ins.sino.claimcar.carchildren.vo;



import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("PACKET")
public class DealFlowInfoReqVo implements Serializable{
	 private static final long serialVersionUID = 1L;
	 
     @XStreamAlias("HEAD") 
     private CarchildReqHeadVo head;
     @XStreamAlias("BODY")
     private DealFlowInfoReqBodyVo body;
     
	public CarchildReqHeadVo getHead() {
		return head;
	}
	public void setHead(CarchildReqHeadVo head) {
		this.head = head;
	}
	public DealFlowInfoReqBodyVo getBody() {
		return body;
	}
	public void setBody(DealFlowInfoReqBodyVo body) {
		this.body = body;
	}
     
     
}
