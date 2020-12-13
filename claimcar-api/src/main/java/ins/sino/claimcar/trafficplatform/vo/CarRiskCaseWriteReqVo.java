package ins.sino.claimcar.trafficplatform.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Packet")
public class CarRiskCaseWriteReqVo implements Serializable{
	 private static final long serialVersionUID = 1L;
	 @XStreamAlias("Head") 
     private RequestHeadVo head;
	 @XStreamAlias("Body")
	 private CarRiskCaseWriteReqBodyVo body;

    
    public RequestHeadVo getHead() {
        return head;
    }

    
    public void setHead(RequestHeadVo head) {
        this.head = head;
    }

    public CarRiskCaseWriteReqBodyVo getBody() {
        return body;
    }
    
    public void setBody(CarRiskCaseWriteReqBodyVo body) {
        this.body = body;
    }
	
	 
}
