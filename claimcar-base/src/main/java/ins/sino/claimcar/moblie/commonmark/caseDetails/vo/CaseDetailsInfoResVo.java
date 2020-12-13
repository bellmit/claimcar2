package ins.sino.claimcar.moblie.commonmark.caseDetails.vo;

import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckResponseHead;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * 报案基本信息（快赔请求理赔）
 * @author zjd
 *
 */
@XStreamAlias("PACKET")
public class CaseDetailsInfoResVo  implements Serializable{
	
	/**  */
	private static final long serialVersionUID = 1L;

	@XStreamAlias("HEAD")
	private MobileCheckResponseHead head;
	
	@XStreamAlias("BODY") 
	private CaseDetailsInfoResBodyVo body;

	public MobileCheckResponseHead getHead() {
		return head;
	}

	public void setHead(MobileCheckResponseHead head) {
		this.head = head;
	}

    
    public CaseDetailsInfoResBodyVo getBody() {
        return body;
    }

    
    public void setBody(CaseDetailsInfoResBodyVo body) {
        this.body = body;
    }


	
}
