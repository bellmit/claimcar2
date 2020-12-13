package ins.sino.claimcar.mobile.check.vo;

import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckResponseHead;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * 
 * 收款人信息（快赔请求理赔）
 * @author ★zhujunde
 */
@XStreamAlias("PACKET")
public class AccountInfoInitResVo  implements Serializable{
	
	/**  */
	private static final long serialVersionUID = 1L;

	@XStreamAlias("HEAD")
	private MobileCheckResponseHead head;
	
	@XStreamAlias("BODY") 
	private AccountInfoResBodyVo body;

	public MobileCheckResponseHead getHead() {
		return head;
	}

	public void setHead(MobileCheckResponseHead head) {
		this.head = head;
	}

    
    public AccountInfoResBodyVo getBody() {
        return body;
    }

    
    public void setBody(AccountInfoResBodyVo body) {
        this.body = body;
    }

    
	
}
