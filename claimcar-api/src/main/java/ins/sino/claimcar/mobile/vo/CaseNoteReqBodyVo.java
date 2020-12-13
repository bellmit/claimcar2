package ins.sino.claimcar.mobile.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * 
 * <pre>案件备注（快赔请求理赔）</pre>
 * @author ★zhujunde
 */
@XStreamAlias("BODY")
public class CaseNoteReqBodyVo  implements Serializable{
	
	/**  */
    private static final long serialVersionUID = 1L;
    
    /**
     * 数据部分
     */
    @XStreamAlias("REMARK")
    private RemarkInfo remark;

    
    public RemarkInfo getRemark() {
        return remark;
    }

    
    public void setRemark(RemarkInfo remark) {
        this.remark = remark;
    }
    

}
