package ins.sino.claimcar.mobile.check.vo;

import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckHead;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * 
 * 收款人信息（快赔请求理赔）
 * @author ★zhujunde
 */
@XStreamAlias("PACKET")
public class AccountInfoSubmitReqVo  implements Serializable{
	
	/**  */
	private static final long serialVersionUID = 1L;

	@XStreamAlias("HEAD") 
	private MobileCheckHead head;
	
	@XStreamAlias("BODY") 
	private AccountInfoSubmitReqBodyVo body;

	public MobileCheckHead getHead() {
		return head;
	}

	public void setHead(MobileCheckHead head) {
		this.head = head;
	}

    
    public AccountInfoSubmitReqBodyVo getBody() {
        return body;
    }

    
    public void setBody(AccountInfoSubmitReqBodyVo body) {
        this.body = body;
    }
	
	
}
