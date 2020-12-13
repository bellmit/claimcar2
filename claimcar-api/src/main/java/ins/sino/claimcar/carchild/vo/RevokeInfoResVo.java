package ins.sino.claimcar.carchild.vo;

import ins.sino.claimcar.carchildCommon.vo.CarchildResponseHeadVo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 撤销信息响应信息
 * <pre></pre>
 * @author ★LinYi
 */
@XStreamAlias("PACKET")
public class RevokeInfoResVo implements Serializable{

    /**  */
    private static final long serialVersionUID = -7309440105445402452L;
    
    @XStreamAlias("HEAD")
    private CarchildResponseHeadVo head;

    
    public CarchildResponseHeadVo getHead() {
        return head;
    }

    
    public void setHead(CarchildResponseHeadVo head) {
        this.head = head;
    }
    

}
