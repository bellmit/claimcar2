package ins.sino.claimcar.founder.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 车险报案vo（理赔请求客服系统返回）
 * @author Luwei
 *
 */
@XStreamAlias("PACKET")
public class CarRegistResVo {

	@XStreamAlias("HEAD")
	private CarRegistResHeadVo head;

	@XStreamAlias("BODY")
	private CarRegistResBodyVo body;

	public CarRegistResHeadVo getHead() {
		return head;
	}

	public void setHead(CarRegistResHeadVo head) {
		this.head = head;
	}

	public CarRegistResBodyVo getBody() {
		return body;
	}

	public void setBody(CarRegistResBodyVo body) {
		this.body = body;
	}

}
