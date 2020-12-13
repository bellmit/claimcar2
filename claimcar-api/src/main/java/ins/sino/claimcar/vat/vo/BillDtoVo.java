package ins.sino.claimcar.vat.vo;

import java.io.Serializable;
public class BillDtoVo implements Serializable{

	private static final long serialVersionUID = 1L;
	private String idEcdinvoiceocr;//进项发票ID
	private String verifyFlag;//是否验真通过标识
	private String invoiceCode;//发票代码
	private String invoiceNo;//发票号码
	private String bussNo;//业务号	
	private String billingDate;//开票日期
	private String sellerName;//销方名称
	private String sellerIdentifiNumber;//销方纳税人识别号
	private String sumall;//不含税金额
	private String sumFee;//价税合计金额
	private String sumTaxFee;//税额
	private String taxRate;//税率
	private String flag;//是否增值税发票0-否，1-为真
	private String invoiceId;//vat发票id
	private String billsortName;//1-表示发票联，2-表示抵扣联
	public String getIdEcdinvoiceocr() {
		return idEcdinvoiceocr;
	}
	public void setIdEcdinvoiceocr(String idEcdinvoiceocr) {
		this.idEcdinvoiceocr = idEcdinvoiceocr;
	}
	public String getVerifyFlag() {
		return verifyFlag;
	}
	public void setVerifyFlag(String verifyFlag) {
		this.verifyFlag = verifyFlag;
	}
	public String getInvoiceCode() {
		return invoiceCode;
	}
	public void setInvoiceCode(String invoiceCode) {
		this.invoiceCode = invoiceCode;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public String getBussNo() {
		return bussNo;
	}
	public void setBussNo(String bussNo) {
		this.bussNo = bussNo;
	}
	public String getBillingDate() {
		return billingDate;
	}
	public void setBillingDate(String billingDate) {
		this.billingDate = billingDate;
	}
	public String getSellerName() {
		return sellerName;
	}
	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}
	public String getSellerIdentifiNumber() {
		return sellerIdentifiNumber;
	}
	public void setSellerIdentifiNumber(String sellerIdentifiNumber) {
		this.sellerIdentifiNumber = sellerIdentifiNumber;
	}
	public String getSumall() {
		return sumall;
	}
	public void setSumall(String sumall) {
		this.sumall = sumall;
	}
	public String getSumFee() {
		return sumFee;
	}
	public void setSumFee(String sumFee) {
		this.sumFee = sumFee;
	}
	public String getSumTaxFee() {
		return sumTaxFee;
	}
	public void setSumTaxFee(String sumTaxFee) {
		this.sumTaxFee = sumTaxFee;
	}
	public String getTaxRate() {
		return taxRate;
	}
	public void setTaxRate(String taxRate) {
		this.taxRate = taxRate;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getInvoiceId() {
		return invoiceId;
	}
	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getBillsortName() {
		return billsortName;
	}
	public void setBillsortName(String billsortName) {
		this.billsortName = billsortName;
	}
	
}
