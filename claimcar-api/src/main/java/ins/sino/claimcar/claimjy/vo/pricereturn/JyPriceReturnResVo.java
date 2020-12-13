package ins.sino.claimcar.claimjy.vo.pricereturn;


import ins.sino.claimcar.claimjy.vo.JyResHeadVo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("PACKET")
public class JyPriceReturnResVo  implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("HEAD")
	private JyResHeadVo head;

	public JyResHeadVo getHead() {
		return head;
	}

	public void setHead(JyResHeadVo head) {
		this.head = head;
	}
	
	
	

}
