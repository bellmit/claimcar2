package ins.sino.claimcar.founder.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 车险报案vo（理赔请求客服系统）
 * @author Luwei
 */
@XStreamAlias("PACKET")
public class RegistCancelCtAtnReqVo {

	@XStreamAlias("HEAD")
	private CommonReqHeadVo head;

	@XStreamAlias("BODY")
	private RegistCancelCtAtnReqBodyVo body;

	public CommonReqHeadVo getHead() {
		return head;
	}

	public void setHead(CommonReqHeadVo head) {
		this.head = head;
	}

    
    public RegistCancelCtAtnReqBodyVo getBody() {
        return body;
    }

    
    public void setBody(RegistCancelCtAtnReqBodyVo body) {
        this.body = body;
    }

	
}
