package ins.sino.claimcar.mobile.check.vo;

import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckHead;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * 报案基本信息（快赔请求理赔）
 * @author zjd
 *
 */
@XStreamAlias("PACKET")
public class CheckInfoSubmitReqVo  implements Serializable{
	
	/**  */
	private static final long serialVersionUID = 1L;

	@XStreamAlias("HEAD") 
	private MobileCheckHead head;
	
	@XStreamAlias("BODY") 
	private CheckInfoSubmitReqBodyVo body;

	public MobileCheckHead getHead() {
		return head;
	}

	public void setHead(MobileCheckHead head) {
		this.head = head;
	}

	public CheckInfoSubmitReqBodyVo getBody() {
		return body;
	}

	public void setBody(CheckInfoSubmitReqBodyVo body) {
		this.body = body;
	}
	
	
}
