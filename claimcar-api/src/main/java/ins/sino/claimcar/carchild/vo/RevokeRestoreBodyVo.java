package ins.sino.claimcar.carchild.vo;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 撤销恢复
 * <pre></pre>
 * @author ★LinYi
 */
@XStreamAlias("BODY")
public class RevokeRestoreBodyVo implements Serializable{

    /**  */
    private static final long serialVersionUID = -7831400413768899519L;
    
    @XStreamAlias("TASKLIST")
    private List<RevokeRestoreTaskInfoVo> revokeRestoreTaskInfos;

    
    public List<RevokeRestoreTaskInfoVo> getRevokeRestoreTaskInfos() {
        return revokeRestoreTaskInfos;
    }

    
    public void setRevokeRestoreTaskInfos(List<RevokeRestoreTaskInfoVo> revokeRestoreTaskInfos) {
        this.revokeRestoreTaskInfos = revokeRestoreTaskInfos;
    }
    
}
