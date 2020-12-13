package ins.sino.claimcar.claimjy.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * <pre></pre>
 * @author ★LiYi
 */
@XStreamAlias("PACKET")
public class JyDLChkReqVo implements Serializable{
	
	@XStreamAlias("HEAD")
	private JyReqHeadVo head;
	@XStreamAlias("BODY")
	private JyDLChkReqBodyVo body;

	public JyReqHeadVo getHead() {
		return head;
	}

	public void setHead(JyReqHeadVo head) {
		this.head = head;
	}

	public JyDLChkReqBodyVo getBody() {
		return body;
	}

	public void setBody(JyDLChkReqBodyVo body) {
		this.body = body;
	}

	public JyDLChkReqVo(JyReqHeadVo head,JyDLChkReqBodyVo body){
		super();
		this.head = head;
		this.body = body;
	}

	public JyDLChkReqVo(){
		super();
	}

}
