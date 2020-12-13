package ins.sino.claimcar.carchildren.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("PACKET")
public class RegistCancleReqVo implements Serializable{
	 private static final long serialVersionUID = 1L;
	 @XStreamAlias("HEAD") 
     private CarchildReqHeadVo head;
	 @XStreamAlias("BODY")
	 private RegistCancleReqBodyVo body;
	public CarchildReqHeadVo getHead() {
		return head;
	}
	public void setHead(CarchildReqHeadVo head) {
		this.head = head;
	}
	public RegistCancleReqBodyVo getBody() {
		return body;
	}
	public void setBody(RegistCancleReqBodyVo body) {
		this.body = body;
	}
	 
}
