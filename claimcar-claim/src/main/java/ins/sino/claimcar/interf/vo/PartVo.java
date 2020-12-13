package ins.sino.claimcar.interf.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/*配件列表*/
@XStreamAlias("Part")
public class PartVo {
	/*OEM码*/
	@XStreamAlias("Code")
	private String code;
	/*配件名称*/
	@XStreamAlias("Name")
	private String name;
	/*修理厂类别名*/
	@XStreamAlias("PriceType")
	private String priceType;
	/*定损单价*/
	@XStreamAlias("Price")
	private String price;
	/*定损数量，整数*/
	@XStreamAlias("Count")
	private String count;
	/*定损总价：定损单价*定损数量*/
	@XStreamAlias("TotalPrice")
	private String totalPrice;
	/*CE建议单价*/
	@XStreamAlias("CEPrice")
	private String cEPrice;
	/*CE建议数量，整数*/
	@XStreamAlias("CECount")
	private String cECount;
	/*CE建议总价：CE建议单价*CE建议数量*/
	@XStreamAlias("CETotalPrice")
	private String cETotalPrice;
	/*建议减损金额：定损总价-CE建议总价*/
	@XStreamAlias("SavingPrice")
	private String savingPrice;
	/*减损描述列表*/
	@XStreamAlias("SavingDescs")
	private SavingDescsVo savingDescsVo;
	/*Y: 存在减损
	N: 不存在减损*/
	@XStreamAlias("CESaving")
	private String cESaving;
	
	@XStreamAlias("PartPositionID")
	private String partPositionID;
	/*定损项目序号*/
	@XStreamAlias("OrderNum")
	private String orderNum;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPriceType() {
		return priceType;
	}
	public void setPriceType(String priceType) {
		this.priceType = priceType;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
	}
	public String getcEPrice() {
		return cEPrice;
	}
	public void setcEPrice(String cEPrice) {
		this.cEPrice = cEPrice;
	}
	public String getcECount() {
		return cECount;
	}
	public void setcECount(String cECount) {
		this.cECount = cECount;
	}
	public String getcETotalPrice() {
		return cETotalPrice;
	}
	public void setcETotalPrice(String cETotalPrice) {
		this.cETotalPrice = cETotalPrice;
	}
	public String getSavingPrice() {
		return savingPrice;
	}
	public void setSavingPrice(String savingPrice) {
		this.savingPrice = savingPrice;
	}
	public SavingDescsVo getSavingDescsVo() {
		return savingDescsVo;
	}
	public void setSavingDescsVo(SavingDescsVo savingDescsVo) {
		this.savingDescsVo = savingDescsVo;
	}
	public String getPartPositionID() {
		return partPositionID;
	}
	public void setPartPositionID(String partPositionID) {
		this.partPositionID = partPositionID;
	}
	public String getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	public String getcESaving() {
		return cESaving;
	}
	public void setcESaving(String cESaving) {
		this.cESaving = cESaving;
	}
    
}
