package ins.sino.claimcar.claimcarYJ.vo;

public class CarspareAddVo implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	private String thirdPartEnquiryId;//三方分配的零件ID
	private String partcode;//零配件原厂编码
	private String partname;//零配件原厂名称
	private String partnum;//换件数量
	private String quotationAmount;//询价单价
	private String parttype;//询价配件品质
	private String partRemark;//配件备注
	public String getThirdPartEnquiryId() {
		return thirdPartEnquiryId;
	}
	public void setThirdPartEnquiryId(String thirdPartEnquiryId) {
		this.thirdPartEnquiryId = thirdPartEnquiryId;
	}
	public String getPartcode() {
		return partcode;
	}
	public void setPartcode(String partcode) {
		this.partcode = partcode;
	}
	public String getPartname() {
		return partname;
	}
	public void setPartname(String partname) {
		this.partname = partname;
	}
	public String getPartnum() {
		return partnum;
	}
	public void setPartnum(String partnum) {
		this.partnum = partnum;
	}
	public String getQuotationAmount() {
		return quotationAmount;
	}
	public void setQuotationAmount(String quotationAmount) {
		this.quotationAmount = quotationAmount;
	}
	public String getParttype() {
		return parttype;
	}
	public void setParttype(String parttype) {
		this.parttype = parttype;
	}
	public String getPartRemark() {
		return partRemark;
	}
	public void setPartRemark(String partRemark) {
		this.partRemark = partRemark;
	}
	
}
