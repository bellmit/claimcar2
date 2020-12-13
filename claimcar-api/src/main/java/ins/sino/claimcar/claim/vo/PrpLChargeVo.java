package ins.sino.claimcar.claim.vo;

import java.util.Date;

/**
 * Custom VO class of PO PrpLCharge
 */ 
public class PrpLChargeVo extends PrpLChargeVoBase implements java.io.Serializable {
	private static final long serialVersionUID = 1L; 
	// 只用于显示不保存的字段
		private String payeeName;// 收款人姓名
		private String accountNo;// 收款人账号
		private String bankName;// 银行行号
		private String payeeIdfNo;// 证件号码
		private String invoiceType;
		private String addTaxRate;
		private String addTaxValue;
		private String noTaxValue;
		private Date payTime;
		private String summary;
		private String payObjectKind;
		public String getPayeeIdfNo() {
			return payeeIdfNo;
		}

		public void setPayeeIdfNo(String payeeIdfNo) {
			this.payeeIdfNo = payeeIdfNo;
		}

		public String getPayeeName() {
			return payeeName;
		}

		public void setPayeeName(String payeeName) {
			this.payeeName = payeeName;
		}

		public String getAccountNo() {
			return accountNo;
		}

		public void setAccountNo(String accountNo) {
			this.accountNo = accountNo;
		}

		public String getBankName() {
			return bankName;
		}

		public void setBankName(String bankName) {
			this.bankName = bankName;
		}

		public String getInvoiceType() {
			return invoiceType;
		}

		public void setInvoiceType(String invoiceType) {
			this.invoiceType = invoiceType;
		}

		public String getAddTaxRate() {
			return addTaxRate;
		}

		public void setAddTaxRate(String addTaxRate) {
			this.addTaxRate = addTaxRate;
		}

		public String getAddTaxValue() {
			return addTaxValue;
		}

		public void setAddTaxValue(String addTaxValue) {
			this.addTaxValue = addTaxValue;
		}

		public String getNoTaxValue() {
			return noTaxValue;
		}

		public void setNoTaxValue(String noTaxValue) {
			this.noTaxValue = noTaxValue;
		}

		public Date getPayTime() {
			return payTime;
		}

		public void setPayTime(Date payTime) {
			this.payTime = payTime;
		}

		public String getSummary() {
			return summary;
		}

		public void setSummary(String summary) {
			this.summary = summary;
		}

		public String getPayObjectKind() {
			return payObjectKind;
		}

		public void setPayObjectKind(String payObjectKind) {
			this.payObjectKind = payObjectKind;
		}
		
		
}
