package ins.sino.claimcar.claimyj.vo;

public class DlossCarCompVo implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	/** 配件名称*/
	private String partName;
	/** 配件编号*/
	private String partNo;
	/** 配件ID*/
	private String partId;
	/** 配件估损单价*/
	private String unitPrice = "";
	public String getPartName() {
		return partName;
	}
	public void setPartName(String partName) {
		this.partName = partName;
	}
	public String getPartNo() {
		return partNo;
	}
	public void setPartNo(String partNo) {
		this.partNo = partNo;
	}
	public String getPartId() {
		return partId;
	}
	public void setPartId(String partId) {
		this.partId = partId;
	}
	public String getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(String unitPrice) {
		this.unitPrice = unitPrice;
	}
	
}
