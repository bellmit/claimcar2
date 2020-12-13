package ins.sino.claimcar.GDPower.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @Description:
 * @Author: Gusheng Huang
 * @Date: Create in 上午11:09 19-2-20
 * @Modified By:
 */
@XStreamAlias("Packet")
public class GDPowerResponse {
    @XStreamAlias("Head")
    private ResHead head;
    @XStreamAlias("Body")
    private ResBody body;

    public ResHead getHead() {
        return head;
    }

    public void setHead(ResHead head) {
        this.head = head;
    }

    public ResBody getBody() {
        return body;
    }

    public void setBody(ResBody body) {
        this.body = body;
    }
}
