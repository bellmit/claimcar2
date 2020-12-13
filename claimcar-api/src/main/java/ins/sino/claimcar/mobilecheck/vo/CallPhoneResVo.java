package ins.sino.claimcar.mobilecheck.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 查勘员号码更新返回vo
 * @author zjd
 *
 */
@XStreamAlias("PACKET")
public class CallPhoneResVo implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;

	@XStreamAsAttribute
	private String type = "REQUEST";
	
	@XStreamAsAttribute
	private String version = "1.0";
	
	@XStreamAlias("HEAD")
	private CallPhoneHeadRes head;
	
	@XStreamAlias("BODY")
	private CallPhoneBodyRes body;

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

	public CallPhoneHeadRes getHead() {
		return head;
	}

	public void setHead(CallPhoneHeadRes head) {
		this.head = head;
	}

	public CallPhoneBodyRes getBody() {
		return body;
	}

	public void setBody(CallPhoneBodyRes body) {
		this.body = body;
	}
	
}
