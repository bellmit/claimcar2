package ins.sino.claimcar.carchild.vo;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 撤销信息
 * <pre></pre>
 * @author ★LinYi
 */
@XStreamAlias("BODY")
public class RevokeBodyVo implements Serializable{

    /**  */
    private static final long serialVersionUID = -7831400413768899519L;
    
    @XStreamAlias("TASKLIST")
    private List<RevokeTaskInfoVo> revokeTaskInfos;

    
    public List<RevokeTaskInfoVo> getRevokeTaskInfos() {
        return revokeTaskInfos;
    }
    
    
    public void setRevokeTaskInfos(List<RevokeTaskInfoVo> revokeTaskInfos) {
        this.revokeTaskInfos = revokeTaskInfos;
    }

}
