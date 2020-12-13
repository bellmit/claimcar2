package ins.sino.claimcar.genilex.vo.scoreVo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;


@XStreamAlias("PushFraud")
public class ScoreResVo implements Serializable{
    
    private static final long serialVersionUID = 1L;
    @XStreamAlias("PacketType") 
    private String PacketType = "REQUEST";
    
    @XStreamAlias("Requestor") 
    private Requestor requestor;
    
    @XStreamAlias("ResponseSummary") 
    private ResponseSummary responseSummary;

    
    public String getPacketType() {
        return PacketType;
    }

    
    public void setPacketType(String packetType) {
        PacketType = packetType;
    }

    
    public Requestor getRequestor() {
        return requestor;
    }

    
    public void setRequestor(Requestor requestor) {
        this.requestor = requestor;
    }

    
    public ResponseSummary getResponseSummary() {
        return responseSummary;
    }

    
    public void setResponseSummary(ResponseSummary responseSummary) {
        this.responseSummary = responseSummary;
    }

    
   
    
}
