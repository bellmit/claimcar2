package ins.sino.claimcar.carchildren.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("PACKET")
public class DealFlowInfoResVo implements Serializable{
	private static final long serialVersionUID = 1L;
	@XStreamAlias("HEAD")
	private CarchildResHeadVo head;
	@XStreamAlias("BODY")
	private DealFlowInfoResBody body;
	public CarchildResHeadVo getHead() {
		return head;
	}
	public void setHead(CarchildResHeadVo head) {
		this.head = head;
	}
	public DealFlowInfoResBody getBody() {
		return body;
	}
	public void setBody(DealFlowInfoResBody body) {
		this.body = body;
	}
	
}
