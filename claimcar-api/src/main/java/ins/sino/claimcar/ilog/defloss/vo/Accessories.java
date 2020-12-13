package ins.sino.claimcar.ilog.defloss.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("accessories")
public class Accessories implements Serializable{
  
    private static final long serialVersionUID = 1L;

    @XStreamAlias("compCode")
    private String compCode; //配件代码
    
    @XStreamAlias("compName")
    private String compName; //配件名称
    
    @XStreamAlias("chgrefPrice")
    private String chgrefPrice; //系统价格
    
    @XStreamAlias("sumdefLoss")
    private String sumdefLoss; //定损价格
    
    @XStreamAlias("chglocPrice")
    private String chglocPrice; //本地价格
    
    @XStreamAlias("isStandard")
    private String isStandard; //是否标准点选
    
    @XStreamAlias("quantity")
    private String quantity; //数量

    
    public String getCompCode() {
        return compCode;
    }

    
    public void setCompCode(String compCode) {
        this.compCode = compCode;
    }

    
    public String getCompName() {
        return compName;
    }

    
    public void setCompName(String compName) {
        this.compName = compName;
    }

    
    public String getChgrefPrice() {
        return chgrefPrice;
    }

    
    public void setChgrefPrice(String chgrefPrice) {
        this.chgrefPrice = chgrefPrice;
    }

    
    public String getSumdefLoss() {
        return sumdefLoss;
    }

    
    public void setSumdefLoss(String sumdefLoss) {
        this.sumdefLoss = sumdefLoss;
    }

    
    public String getChglocPrice() {
        return chglocPrice;
    }

    
    public void setChglocPrice(String chglocPrice) {
        this.chglocPrice = chglocPrice;
    }

    
    public String getIsStandard() {
        return isStandard;
    }

    
    public void setIsStandard(String isStandard) {
        this.isStandard = isStandard;
    }

    
    public String getQuantity() {
        return quantity;
    }

    
    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
    
}
