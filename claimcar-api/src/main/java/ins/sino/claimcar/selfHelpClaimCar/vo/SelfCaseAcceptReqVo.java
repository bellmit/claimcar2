package ins.sino.claimcar.selfHelpClaimCar.vo;

import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckHead;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 自助案件业务员受理接口
 * @author zhujunde
 *
 */
@XStreamAlias("PACKET")
public class SelfCaseAcceptReqVo  implements Serializable{

    private static final long serialVersionUID = 1L;

    @XStreamAlias("HEAD") 
    private MobileCheckHead head;
    
    @XStreamAlias("BODY") 
    private SelfCaseAcceptReqBodyVo body;

	public MobileCheckHead getHead() {
		return head;
	}

	public void setHead(MobileCheckHead head) {
		this.head = head;
	}

	public SelfCaseAcceptReqBodyVo getBody() {
		return body;
	}

	public void setBody(SelfCaseAcceptReqBodyVo body) {
		this.body = body;
	}

    
 
}
