package ins.sino.claimcar.claimjy.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * <pre></pre>
 * @author ★LiYi
 */
@XStreamAlias("PACKET")
public class JyDLChkReturnVo implements Serializable {
	private static final long serialVersionUID = -4882580480704170697L;
	@XStreamAlias("HEAD")
	private JyReturnReqHead head;
	@XStreamAlias("BODY")
	private JyDLChkReturnBodyVo body;

	public JyReturnReqHead getHead() {
		return head;
	}

	public void setHead(JyReturnReqHead head) {
		this.head = head;
	}

	public JyDLChkReturnBodyVo getBody() {
		return body;
	}

	public void setBody(JyDLChkReturnBodyVo body) {
		this.body = body;
	}

	public JyDLChkReturnVo(){
		super();
	}

}
