package ins.sino.claimcar.carchild.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 批单信息
 * <pre></pre>
 * @author ★LinYi
 */
@XStreamAlias("ENDORINFO")
public class EndorInfoVo implements Serializable{

    /**  */
    private static final long serialVersionUID = -5113087905278166929L;
    
    @XStreamAlias("ENDORNO")
    private String endorNo;//批单号
    
    @XStreamAlias("ENDORDATE")
    private String endorDate;//批改日期
    
    @XStreamAlias("ENDORVERIFIEDDATE")
    private String endorVerifiedDate;//批单审核通过日期
    
    @XStreamAlias("ENDORCONTENT")
    private String endorContent;//批改内容
    
    @XStreamAlias("AMOUNTCHANGE")
    private String amountChange;//保额变化量

    
    public String getEndorNo() {
        return endorNo;
    }

    
    public void setEndorNo(String endorNo) {
        this.endorNo = endorNo;
    }

    
    public String getEndorDate() {
        return endorDate;
    }

    
    public void setEndorDate(String endorDate) {
        this.endorDate = endorDate;
    }

    
    public String getEndorVerifiedDate() {
        return endorVerifiedDate;
    }

    
    public void setEndorVerifiedDate(String endorVerifiedDate) {
        this.endorVerifiedDate = endorVerifiedDate;
    }

    
    public String getEndorContent() {
        return endorContent;
    }

    
    public void setEndorContent(String endorContent) {
        this.endorContent = endorContent;
    }

    
    public String getAmountChange() {
        return amountChange;
    }

    
    public void setAmountChange(String amountChange) {
        this.amountChange = amountChange;
    }
    

}
