package ins.sino.claimcar.claimcarYJ.vo;

import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckResponseHead;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("PACKET")
public class ResYJImageVo  implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;

	@XStreamAlias("HEAD")
	private MobileCheckResponseHead head;

	@XStreamAlias("BODY")
	private ImageResBody body;
	
	public MobileCheckResponseHead getHead() {
		return head;
	}

	public void setHead(MobileCheckResponseHead head) {
		this.head = head;
	}

	public ImageResBody getBody() {
		return body;
	}

	public void setBody(ImageResBody body) {
		this.body = body;
	}



	
}
