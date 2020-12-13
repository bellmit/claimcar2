package ins.sino.claimcar.carchildren.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("PACKET")
public class CaseCancleNopassResVo {
	@XStreamAlias("HEAD") 
    private CarchildResHeadVo head;

	public CarchildResHeadVo getHead() {
		return head;
	}

	public void setHead(CarchildResHeadVo head) {
		this.head = head;
	}
	
}
