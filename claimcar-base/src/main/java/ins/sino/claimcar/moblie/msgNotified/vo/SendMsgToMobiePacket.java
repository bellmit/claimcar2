package ins.sino.claimcar.moblie.msgNotified.vo;

import ins.sino.claimcar.flow.vo.MsgNotifiedBody;
import ins.sino.claimcar.flow.vo.NotifyMobileBody;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("PACKET")
public class SendMsgToMobiePacket implements Serializable {

    /**  */
    private static final long serialVersionUID = -6221673560424896923L;
    
    @XStreamAlias("HEAD")
    SendMsgToMobileHead head;

    @XStreamAlias("BODY")
    private NotifyMobileBody body;

    
    public SendMsgToMobileHead getHead() {
        return head;
    }

    
    public void setHead(SendMsgToMobileHead head) {
        this.head = head;
    }

    
    public NotifyMobileBody getBody() {
        return body;
    }

    
    public void setBody(NotifyMobileBody body) {
        this.body = body;
    }
    
    
}
