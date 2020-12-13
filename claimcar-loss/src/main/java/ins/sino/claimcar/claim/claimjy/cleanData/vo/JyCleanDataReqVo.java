package ins.sino.claimcar.claim.claimjy.cleanData.vo;

import ins.sino.claimcar.claimjy.vo.JyReqHeadVo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("PACKET")
public class JyCleanDataReqVo implements Serializable{
	 private static final long serialVersionUID = 1L;
	 @XStreamAlias("HEAD") 
     private JyReqHeadVo head;
	 @XStreamAlias("BODY")
	 private JyCleanDataBodyVo body;
	public JyReqHeadVo getHead() {
		return head;
	}
	public void setHead(JyReqHeadVo head) {
		this.head = head;
	}
	public JyCleanDataBodyVo getBody() {
		return body;
	}
	public void setBody(JyCleanDataBodyVo body) {
		this.body = body;
	}
	
	 
	 
}
