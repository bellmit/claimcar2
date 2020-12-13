package ins.sino.claimcar.founder.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 调度信息更新接口vo（理赔请求客服系统）
 * @author Luwei
 */
@XStreamAlias("INPUTDATA")
public class RegistCancelCtAtnInputdataVo {
    @XStreamAlias("ClmNo")
    private String ClmNo;

    
    public String getClmNo() {
        return ClmNo;
    }

    
    public void setClmNo(String clmNo) {
        ClmNo = clmNo;
    }
    
    
}
