package ins.sino.claimcar.founder.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 报案注销接口vo（理赔请求客服系统）
 * @author Luwei
 */
@XStreamAlias("PACKET")
public class RegistCancelReqVo {

	@XStreamAsAttribute
	private String type = "REQUEST";

	@XStreamAsAttribute
	private String version = "1.0";

	@XStreamAlias("HEAD")
	private CommonReqHeadVo head;

	@XStreamAlias("BODY")
	private RegistCancelBodyVo body;

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

	public CommonReqHeadVo getHead() {
		return head;
	}

	public void setHead(CommonReqHeadVo head) {
		this.head = head;
	}

	public RegistCancelBodyVo getBody() {
		return body;
	}

	public void setBody(RegistCancelBodyVo body) {
		this.body = body;
	}

}
