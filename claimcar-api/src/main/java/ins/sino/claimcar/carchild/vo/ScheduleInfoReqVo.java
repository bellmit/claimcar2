package ins.sino.claimcar.carchild.vo;

import ins.sino.claimcar.carchildCommon.vo.CarchildHeadVo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 调度结果通知（车童网请求快赔）
 * <pre></pre>
 * @author ★LinYi
 */
@XStreamAlias("PACKET")
public class ScheduleInfoReqVo implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    @XStreamAlias("HEAD") 
    private CarchildHeadVo head;
    
    @XStreamAlias("BODY") 
    private ScheduleBodyVo body;

    
    public CarchildHeadVo getHead() {
        return head;
    }

    
    public void setHead(CarchildHeadVo head) {
        this.head = head;
    }

    
    public ScheduleBodyVo getBody() {
        return body;
    }

    
    public void setBody(ScheduleBodyVo body) {
        this.body = body;
    }
    
}
