package ins.sino.claimcar.claim.vo;

/**
 * Custom VO class of PO PrpLPadPayPerson
 */ 
public class PrpLPadPayPersonVo extends PrpLPadPayPersonVoBase implements java.io.Serializable {
	private static final long serialVersionUID = 1L; 
	private String injuredName;
	private String invoiceType;
	private String addTaxRate;
	private String addTaxValue;
	private String noTaxValue;
	public String getInjuredName() {
		return injuredName;
	}
	public void setInjuredName(String injuredName) {
		this.injuredName = injuredName;
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
