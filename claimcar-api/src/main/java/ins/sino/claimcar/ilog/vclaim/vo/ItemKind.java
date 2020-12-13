package ins.sino.claimcar.ilog.vclaim.vo;


import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("ItemKind")
public class ItemKind {

	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("kindCode")
	private String kindCode = ""; //险别代码

    @XStreamAlias("kindName")
    private String kindName = ""; //险别名称
    
    @XStreamAlias("itemNo")
    private String itemNo = ""; //标的序号
    
    @XStreamAlias("unitAmount")
    private String unitAmount = ""; //单位保险金额（赔偿限额）
    
    @XStreamAlias("unit")
    private String unit = ""; //数量单位

    @XStreamAlias("amount")
    private String amount = ""; //保险金额

    
    public String getKindCode() {
        return kindCode;
    }

    
    public void setKindCode(String kindCode) {
        this.kindCode = kindCode;
    }

    
    public String getKindName() {
        return kindName;
    }

    
    public void setKindName(String kindName) {
        this.kindName = kindName;
    }

    
    public String getItemNo() {
        return itemNo;
    }

    
    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    
    public String getUnitAmount() {
        return unitAmount;
    }

    
    public void setUnitAmount(String unitAmount) {
        this.unitAmount = unitAmount;
    }

    
    public String getUnit() {
        return unit;
    }

    
    public void setUnit(String unit) {
        this.unit = unit;
    }

    
    public String getAmount() {
        return amount;
    }

    
    public void setAmount(String amount) {
        this.amount = amount;
    }

    
    
}
