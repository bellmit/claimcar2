package ins.sino.claimcar.mobile.vo;

import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckHead;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * <pre>案件备注（快赔请求理赔）</pre>
 * @author ★zhujunde
 */
@XStreamAlias("PACKET")
public class CaseNoteReqVo  implements Serializable{

    private static final long serialVersionUID = 1L;

    @XStreamAlias("HEAD") 
    private MobileCheckHead head;
    
    @XStreamAlias("BODY") 
    private CaseNoteReqBodyVo body;

    
    public MobileCheckHead getHead() {
        return head;
    }

    
    public void setHead(MobileCheckHead head) {
        this.head = head;
    }

    
    public CaseNoteReqBodyVo getBody() {
        return body;
    }

    
    public void setBody(CaseNoteReqBodyVo body) {
        this.body = body;
    }
    
    
}
