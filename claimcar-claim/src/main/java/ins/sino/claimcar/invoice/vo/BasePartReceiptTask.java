package ins.sino.claimcar.invoice.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;


@XStreamAlias("BasePart")
public class BasePartReceiptTask implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@XStreamAlias("BusinessNo")
	private String businessNo;// 业务号
	@XStreamAlias("SerialNo")
	private int serialNo;// 序号	
	@XStreamAlias("Invoicetype")
	private String invoicetype;//发票类型
	@XStreamAlias("SumAmountNT")
	private double sumAmountNT;//不含税金额
	@XStreamAlias("TaxRate")
	private double taxRate;//税率
	@XStreamAlias("SumAmountTax")
	private double sumAmountTax;//税额
	@XStreamAlias("FeeType")
	private String feeType;//费用类型
	public String getBusinessNo() {
		return businessNo;
	}
	public void setBusinessNo(String businessNo) {
		this.businessNo = businessNo;
	}
	public String getFeeType() {
		return feeType;
	}
	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}
	public int getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(int serialNo) {
		this.serialNo = serialNo;
	}
	public String getInvoicetype() {
		return invoicetype;
	}
	public void setInvoicetype(String invoicetype) {
		this.invoicetype = invoicetype;
	}
	public double getSumAmountNT() {
		return sumAmountNT;
	}
	public void setSumAmountNT(double sumAmountNT) {
		this.sumAmountNT = sumAmountNT;
	}
	public double getTaxRate() {
		return taxRate;
	}
	public void setTaxRate(double taxRate) {
		this.taxRate = taxRate;
	}
	public double getSumAmountTax() {
		return sumAmountTax;
	}
	public void setSumAmountTax(double sumAmountTax) {
		this.sumAmountTax = sumAmountTax;
	}
	
	
}
