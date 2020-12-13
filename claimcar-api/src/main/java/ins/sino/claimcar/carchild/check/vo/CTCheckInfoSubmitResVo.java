package ins.sino.claimcar.carchild.check.vo;

import ins.sino.claimcar.carchildCommon.vo.CarchildResponseHeadVo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("PACKET")
public class CTCheckInfoSubmitResVo implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("HEAD") 
	private CarchildResponseHeadVo head;

	public CarchildResponseHeadVo getHead() {
		return head;
	}

	public void setHead(CarchildResponseHeadVo head) {
		this.head = head;
	}
	
}
