package ins.sino.claimcar.mobilecheck.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 获取查勘定损员信息接口vo（理赔请求快赔）
 * @author zy
 *
 */
@XStreamAlias("PACKET")
public class PersonnelInformationReqVo implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;

	@XStreamAsAttribute
	private String type = "REQUEST";
	
	@XStreamAsAttribute
	private String version = "1.0";
	
	@XStreamAlias("HEAD")
	private HeadReq head;
	
	@XStreamAlias("BODY")
	private PersonnelInformationReqBody body;
	
	

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

	public PersonnelInformationReqBody getBody() {
		return body;
	}

	public void setBody(PersonnelInformationReqBody body) {
		this.body = body;
	}
	
}
