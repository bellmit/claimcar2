package ins.sino.claimcar.mobilecheck.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 查勘员号码更新请求vo
 * @author zjd
 *
 */
@XStreamAlias("PACKET")
public class CallPhoneReqVo implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;

	@XStreamAsAttribute
	private String type = "REQUEST";
	
	@XStreamAsAttribute
	private String version = "1.0";
	
	@XStreamAlias("HEAD")
	private CallPhoneHeadReq head;
	
	@XStreamAlias("BODY")
	private CallPhoneBodyReq body;
	
	

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

	public CallPhoneHeadReq getHead() {
		return head;
	}

	public void setHead(CallPhoneHeadReq head) {
		this.head = head;
	}

	public CallPhoneBodyReq getBody() {
		return body;
	}

	public void setBody(CallPhoneBodyReq body) {
		this.body = body;
	}

	
}
