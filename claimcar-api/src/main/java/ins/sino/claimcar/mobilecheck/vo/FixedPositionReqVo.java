package ins.sino.claimcar.mobilecheck.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 客户定位界面接口请求vo（理赔请求快赔）
 * @author zy
 *
 */
@XStreamAlias("PACKET")
public class FixedPositionReqVo implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;

	@XStreamAsAttribute
	private String type = "REQUEST";
	
	@XStreamAsAttribute
	private String version = "1.0";
	
	@XStreamAlias("HEAD")
	private HeadReq head;
	
	@XStreamAlias("BODY")
	private FixedPositionReqBody body;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public HeadReq getHead() {
		return head;
	}

	public void setHead(HeadReq head) {
		this.head = head;
	}

	public FixedPositionReqBody getBody() {
		return body;
	}

	public void setBody(FixedPositionReqBody body) {
		this.body = body;
	}
	
	
}
