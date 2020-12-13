package ins.sino.claimcar.claim.vo;

/**
 * Custom VO class of PO PrpLPayment
 */ 
public class PrpLPaymentVo extends PrpLPaymentVoBase implements java.io.Serializable {
	private static final long serialVersionUID = 1L; 
	// 只用于显示不保存的字段
		private String payeeName;// 收款人姓名
		private String accountNo;// 收款人账号
		private String bankName;// 银行行号
		private String payObjectKind;// 收款人类型
		private String compensateNo;
		private String claimNo;
		private String policyNo;
		private String registNo;//报案号
		private String invoiceType;
		private String addTaxRate;
		private String addTaxValue;
		private String noTaxValue;
		public String getCompensateNo() {
			return compensateNo;
		}


		public void setCompensateNo(String compensateNo) {
			this.compensateNo = compensateNo;
		}


		public String getClaimNo() {
			return claimNo;
		}


		public void setClaimNo(String claimNo) {
			this.claimNo = claimNo;
		}


		public String getPolicyNo() {
			return policyNo;
		}


		public void setPolicyNo(String policyNo) {
			this.policyNo = policyNo;
		}


		public String getPayObjectKind() {
			return payObjectKind;
		}

		
		public void setPayObjectKind(String payObjectKind) {
			this.payObjectKind = payObjectKind;
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


		public String getRegistNo() {
			return registNo;
		}


		public void setRegistNo(String registNo) {
			this.registNo = registNo;
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
      

		
}
