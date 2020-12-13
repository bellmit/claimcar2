package ins.sino.claimcar.genilex.vo.scoreVo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;


@XStreamAlias("soapenv:Envelope")
public class ScoreReqVo implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    @XStreamAsAttribute
    @XStreamAlias("xmlns:soapenv") 
    private String xmlnsSoapenv  = "http://schemas.xmlsoap.org/soap/envelope/";
    
    @XStreamAsAttribute
    @XStreamAlias("xmlns") 
    private String xmlns  = "http://soap.score.fraud.message.genilex.com";
    
    @XStreamAlias("soapenv:Header") 
    private ScoreReqHeadVo head;
    
    @XStreamAlias("soapenv:Body") 
    private ScoreReqBodyVo body;

    
    public String getXmlnsSoapenv() {
        return xmlnsSoapenv;
    }

    
    public void setXmlnsSoapenv(String xmlnsSoapenv) {
        this.xmlnsSoapenv = xmlnsSoapenv;
    }

    
    public String getXmlns() {
        return xmlns;
    }

    
    public void setXmlns(String xmlns) {
        this.xmlns = xmlns;
    }

    
    public ScoreReqHeadVo getHead() {
        return head;
    }

    
    public void setHead(ScoreReqHeadVo head) {
        this.head = head;
    }

    
    public ScoreReqBodyVo getBody() {
        return body;
    }

    
    public void setBody(ScoreReqBodyVo body) {
        this.body = body;
    }

    
    
    
}
