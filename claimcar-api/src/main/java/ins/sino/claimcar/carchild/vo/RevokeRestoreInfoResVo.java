package ins.sino.claimcar.carchild.vo;

import ins.sino.claimcar.carchildCommon.vo.CarchildResponseHeadVo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 撤销恢复响应信息
 * <pre></pre>
 * @author ★LinYi
 */
@XStreamAlias("PACKET")
public class RevokeRestoreInfoResVo implements Serializable{

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
