package ins.sino.claimcar.carchild.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 险别信息
 * <pre></pre>
 * @author ★LinYi
 */
@XStreamAlias("KINDINFO")
public class KindInfoVo implements Serializable{

    /**  */
    private static final long serialVersionUID = -8156865260809839899L;

    @XStreamAlias("KINDNAME")
    private String kindName;//子险名称
    
    @XStreamAlias("KINDCODE")
    private String kindCode;//子险编码
    
    @XStreamAlias("AMOUNT")
    private BigDecimal amount;//保额最新值
    
    @XStreamAlias("DEDUCT")
    private String deduct;//免赔额
    
    @XStreamAlias("ISDEDUCT")
    private String isDeduct;//是否承保不计免赔

    
    public String getKindName() {
        return kindName;
    }

    
    public void setKindName(String kindName) {
        this.kindName = kindName;
    }

    
    public String getKindCode() {
        return kindCode;
    }

    
    public void setKindCode(String kindCode) {
        this.kindCode = kindCode;
    }

    
    public BigDecimal getAmount() {
        return amount;
    }

    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    
    public String getDeduct() {
        return deduct;
    }

    
    public void setDeduct(String deduct) {
        this.deduct = deduct;
    }

    
    public String getIsDeduct() {
        return isDeduct;
    }

    
    public void setIsDeduct(String isDeduct) {
        this.isDeduct = isDeduct;
    }
    
}
