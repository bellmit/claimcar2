package ins.sino.claimcar.claimyj.vo;

public class DlossCarSpareVo implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	/** 三方分配的零件ID*/
	private String thirdPartEnquiryId;
	/** 零配件原厂图号编码*/
	private String partcode;
	/** 零配件原厂名称*/
	private String partname;
	/** 换件数量*/
	private String partnum = "0";
	/** 核损单价*/
	private String quotationAmount;
	/** 核损配件品质*/
	private String parttype;
	/** 配件备注*/
	private String partRemark;
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
