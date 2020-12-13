package ins.sino.claimcar.middlestagequery.vo;

public class CarDefloss implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	private String deflossType;     //项目类型（本车、三者车、人伤损失、财产损失）
	private String deflossName;     //项目名称
	private String deflossNum;      //数量
	private String deflossPrice;    //单价
	private String deflossSumAmt;   //金额合计
	public String getDeflossType() {
		return deflossType;
	}
	public void setDeflossType(String deflossType) {
		this.deflossType = deflossType;
	}
	public String getDeflossName() {
		return deflossName;
	}
	public void setDeflossName(String deflossName) {
		this.deflossName = deflossName;
	}
	public String getDeflossNum() {
		return deflossNum;
	}
	public void setDeflossNum(String deflossNum) {
		this.deflossNum = deflossNum;
	}
	public String getDeflossPrice() {
		return deflossPrice;
	}
	public void setDeflossPrice(String deflossPrice) {
		this.deflossPrice = deflossPrice;
	}
	public String getDeflossSumAmt() {
		return deflossSumAmt;
	}
	public void setDeflossSumAmt(String deflossSumAmt) {
		this.deflossSumAmt = deflossSumAmt;
	}
	
	
}
