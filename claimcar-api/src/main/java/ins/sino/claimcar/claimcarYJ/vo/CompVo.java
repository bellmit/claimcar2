package ins.sino.claimcar.claimcarYJ.vo;

public class CompVo implements java.io.Serializable{
	private String thirdpartenquiryid;//三方分配的零件id
	private String partcode;//零配件原厂图号编码
	private String partName;//零配件原厂名称
	private String partnum;//换件数量
	private String quotationamount;//供货协商单价
	private String parttype;//供货配件品质
	private String partremark;//配件备注
	public String getThirdpartenquiryid() {
		return thirdpartenquiryid;
	}
	public void setThirdpartenquiryid(String thirdpartenquiryid) {
		this.thirdpartenquiryid = thirdpartenquiryid;
	}
	public String getPartcode() {
		return partcode;
	}
	public void setPartcode(String partcode) {
		this.partcode = partcode;
	}
	public String getPartName() {
		return partName;
	}
	public void setPartName(String partName) {
		this.partName = partName;
	}
	public String getPartnum() {
		return partnum;
	}
	public void setPartnum(String partnum) {
		this.partnum = partnum;
	}
	public String getQuotationamount() {
		return quotationamount;
	}
	public void setQuotationamount(String quotationamount) {
		this.quotationamount = quotationamount;
	}
	public String getParttype() {
		return parttype;
	}
	public void setParttype(String parttype) {
		this.parttype = parttype;
	}
	public String getPartremark() {
		return partremark;
	}
	public void setPartremark(String partremark) {
		this.partremark = partremark;
	}
	
	
}
