package ins.sino.claimcar.founder.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 调度信息更新接口vo（理赔请求客服系统）
 * @author Luwei
 */
@XStreamAlias("PACKET")
public class ScheduleInfoReqVo {

	@XStreamAlias("HEAD")
	private CommonReqHeadVo head;

	@XStreamAlias("BODY")
	private ScheduleInfoReqBodyVo body;

	/**
	 * @return
	 */
	public CommonReqHeadVo getHead() {
		return head;
	}

	public void setHead(CommonReqHeadVo head) {
		this.head = head;
	}

	public ScheduleInfoReqBodyVo getBody() {
		return body;
	}

	public void setBody(ScheduleInfoReqBodyVo body) {
		this.body = body;
	}

}
