package ins.sino.claimcar.moblie.commonmark.caseDetails.vo;

import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckBody;
import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckHead;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * 案详情查询（快赔请求理赔）
 * @author zjd
 *
 */
@XStreamAlias("PACKET")
public class CaseDetailsInfoReqVo  implements Serializable{
	
	/**  */
	private static final long serialVersionUID = 1L;

	@XStreamAlias("HEAD") 
	private MobileCheckHead head;
	
	@XStreamAlias("BODY") 
	private MobileCheckBody body;

	public MobileCheckHead getHead() {
		return head;
	}

	public void setHead(MobileCheckHead head) {
		this.head = head;
	}

    
    public MobileCheckBody getBody() {
        return body;
    }

    
    public void setBody(MobileCheckBody body) {
        this.body = body;
    }

	
}
