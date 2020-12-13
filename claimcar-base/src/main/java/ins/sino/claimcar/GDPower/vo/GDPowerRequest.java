package ins.sino.claimcar.GDPower.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @Description:
 * @Author: Gusheng Huang
 * @Date: Create in 上午11:51 19-2-19
 * @Modified By:
 */
@XStreamAlias("Packet")
public class GDPowerRequest {
    @XStreamAlias("Head")
    private ReqHead reqHead;

    @XStreamAlias("Body")
    private ReqBody reqBody;

    public ReqHead getReqHead() {
        return reqHead;
    }

    public void setReqHead(ReqHead reqHead) {
        this.reqHead = reqHead;
    }

    public ReqBody getReqBody() {
        return reqBody;
    }

    public void setReqBody(ReqBody reqBody) {
        this.reqBody = reqBody;
    }
}
