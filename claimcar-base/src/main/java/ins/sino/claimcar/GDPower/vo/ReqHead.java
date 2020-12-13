package ins.sino.claimcar.GDPower.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @Description:
 * @Author: Gusheng Huang
 * @Date: Create in 上午11:50 19-2-19
 * @Modified By:
 */
@XStreamAlias("Head")
public class ReqHead {
    @XStreamAlias("RequestType")
    private String requestType;

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }
}
