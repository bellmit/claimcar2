package ins.sino.claimcar.claimjy.vo.pricereturn;

import ins.sino.claimcar.claimjy.vo.DlossReqHeadVo;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "PACKET")
@XmlAccessorType(XmlAccessType.FIELD)
public class JyPriceReturnReqVo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@XmlElement(name = "HEAD") 
	private DlossReqHeadVo head;
	
	@XmlElement(name = "BODY") 
	private JyPriceReturnReqBody body;

	public DlossReqHeadVo getHead() {
		return head;
	}

	public void setHead(DlossReqHeadVo head) {
		this.head = head;
	}

	public JyPriceReturnReqBody getBody() {
		return body;
	}

	public void setBody(JyPriceReturnReqBody body) {
		this.body = body;
	}
	
	

}
