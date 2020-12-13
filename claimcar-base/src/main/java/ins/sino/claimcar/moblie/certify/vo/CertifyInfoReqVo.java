package ins.sino.claimcar.moblie.certify.vo;

import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckHead;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * 案详情查询（快赔请求理赔）
 * @author zjd
 *
 */
@XStreamAlias("PACKET")
public class CertifyInfoReqVo  implements Serializable{
	
	/**  */
	private static final long serialVersionUID = 1L;

	@XStreamAlias("HEAD") 
	private MobileCheckHead head;
	
	@XStreamAlias("BODY") 
	private CertifyBody body;

	public MobileCheckHead getHead() {
		return head;
	}

	public void setHead(MobileCheckHead head) {
		this.head = head;
	}

    
    public CertifyBody getBody() {
        return body;
    }

    
    public void setBody(CertifyBody body) {
        this.body = body;
    }

	
	
}
