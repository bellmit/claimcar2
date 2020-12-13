package ins.sino.claimcar.mobilecheck.service.vo;

import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckResponseHead;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 2.5.6勘记录信息查询接口 返回报文
 * <pre></pre>
 * @author ★niuqiang
 */
@XStreamAlias("PACKET")
public class MC_Packet implements Serializable {

	/**  */
	private static final long serialVersionUID = -3953499806740729779L;

	@XStreamAlias("HEAD")
	private MobileCheckResponseHead head;
	
	@XStreamAlias("BODY")
	private MC_Body body;

	public MobileCheckResponseHead getHead() {
		return head;
	}

	public void setHead(MobileCheckResponseHead head) {
		this.head = head;
	}

	public MC_Body getBody() {
		return body;
	}

	public void setBody(MC_Body body) {
		this.body = body;
	}
	
	
}
