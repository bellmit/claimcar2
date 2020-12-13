package ins.sino.claimcar.ciitc.vo.accident;

import ins.sino.claimcar.ciitc.vo.CiitcReqHeadVo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Packet")
public class CiitcAccidentReqVo implements Serializable{
	 private static final long serialVersionUID = 1L;
	 @XStreamAlias("Head") 
     private CiitcReqHeadVo head;
	 @XStreamAlias("Body")
	 private CiitcAccidentReqBodyVo body;
	public CiitcReqHeadVo getHead() {
		return head;
	}
	public void setHead(CiitcReqHeadVo head) {
		this.head = head;
	}
	public CiitcAccidentReqBodyVo getBody() {
		return body;
	}
	public void setBody(CiitcAccidentReqBodyVo body) {
		this.body = body;
	}
	
	 
}
