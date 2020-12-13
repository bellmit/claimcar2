package ins.sino.claimcar.carchild.vo;

import ins.sino.claimcar.carchildCommon.vo.CarchildHeadVo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 报案（理赔请求车童网）
 * <pre></pre>
 * @author ★LinYi
 */
@XStreamAlias("PACKET")
public class RegistInfoReqVo implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    @XStreamAlias("HEAD") 
    private CarchildHeadVo head;
    
    @XStreamAlias("BODY") 
    private RegistBodyVo body;

    
    public CarchildHeadVo getHead() {
        return head;
    }

    
    public void setHead(CarchildHeadVo head) {
        this.head = head;
    }

    
    public RegistBodyVo getBody() {
        return body;
    }

    
    public void setBody(RegistBodyVo body) {
        this.body = body;
    }
      
}
