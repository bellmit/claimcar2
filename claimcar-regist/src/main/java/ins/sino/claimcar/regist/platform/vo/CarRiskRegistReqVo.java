package ins.sino.claimcar.regist.platform.vo;

import ins.sino.claimcar.trafficplatform.vo.CarRiskRegistBodyReqVo;
import ins.sino.claimcar.trafficplatform.vo.RequestHeadVo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 山东预警报文
 * <pre></pre>
 * @author ★zhujunde
 */
@XStreamAlias("Packet")
public class CarRiskRegistReqVo implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    @XStreamAlias("Head") 
    private RequestHeadVo head;
    
    @XStreamAlias("Body") 
    private CarRiskRegistBodyReqVo body;

    
    public RequestHeadVo getHead() {
        return head;
    }

    
    public void setHead(RequestHeadVo head) {
        this.head = head;
    }

    
    public CarRiskRegistBodyReqVo getBody() {
        return body;
    }

    
    public void setBody(CarRiskRegistBodyReqVo body) {
        this.body = body;
    }

    
 
}
