package ins.sino.claimcar.carchild.vo;

import ins.sino.claimcar.carchildCommon.vo.CarchildHeadVo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 撤销恢复（理赔请求车童网）
 * <pre></pre>
 * @author ★LinYi
 */
@XStreamAlias("PACKET")
public class RevokeRestoreInfoReqVo implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    @XStreamAlias("HEAD") 
    private CarchildHeadVo head;
    
    @XStreamAlias("BODY") 
    private RevokeRestoreBodyVo body;

    
    public CarchildHeadVo getHead() {
        return head;
    }

    
    public void setHead(CarchildHeadVo head) {
        this.head = head;
    }


    
    public RevokeRestoreBodyVo getBody() {
        return body;
    }


    
    public void setBody(RevokeRestoreBodyVo body) {
        this.body = body;
    }
    
}
