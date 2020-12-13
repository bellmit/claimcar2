package ins.sino.claimcar.carchildren.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("PACKET")
public class RegistCancleResVo implements Serializable{
	private static final long serialVersionUID = 1L;
	@XStreamAlias("HEAD")
	private CarchildResHeadVo head;
	public CarchildResHeadVo getHead() {
		return head;
	}
	public void setHead(CarchildResHeadVo head) {
		this.head = head;
	}
	
}
