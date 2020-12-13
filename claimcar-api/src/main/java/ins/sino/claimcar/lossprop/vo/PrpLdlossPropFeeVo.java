package ins.sino.claimcar.lossprop.vo;

import java.math.BigDecimal;

/**
 * Custom VO class of PO PrpLdlossPropFee
 */ 
public class PrpLdlossPropFeeVo extends PrpLdlossPropFeeVoBase implements java.io.Serializable {
	private static final long serialVersionUID = 1L; 
	private BigDecimal invoiceFee;
	public BigDecimal getInvoiceFee() {
		return invoiceFee;
	}
	public void setInvoiceFee(BigDecimal invoiceFee) {
		this.invoiceFee = invoiceFee;
	}
	
}
