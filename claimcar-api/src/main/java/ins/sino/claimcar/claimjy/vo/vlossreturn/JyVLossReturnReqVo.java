package ins.sino.claimcar.claimjy.vo.vlossreturn;

import ins.sino.claimcar.claimjy.vo.DlossReqHeadVo;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "PACKET")
@XmlAccessorType(XmlAccessType.FIELD)
public class JyVLossReturnReqVo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@XmlElement(name = "HEAD") 
	private DlossReqHeadVo head;
	
	@XmlElement(name = "BODY") 
	private JyVLossReturnReqBody body;

	public DlossReqHeadVo getHead() {
		return head;
	}

	public void setHead(DlossReqHeadVo head) {
		this.head = head;
	}

	public JyVLossReturnReqBody getBody() {
		return body;
	}

	public void setBody(JyVLossReturnReqBody body) {
		this.body = body;
	}
	
	

}
