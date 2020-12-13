package ins.sino.claimcar.lossperson.vo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


/**
 * Custom VO class of PO PrpLDlossPersTrace
 */ 
public class PrpLDlossPersTraceVo extends PrpLDlossPersTraceVoBase implements java.io.Serializable {
	private static final long serialVersionUID = 1L; 
	
	private List<PrpLDlossPersTraceHisVo> persTraceHises = new ArrayList<PrpLDlossPersTraceHisVo>();

	private BigDecimal invoiceFee; //发票金额
	public List<PrpLDlossPersTraceHisVo> getPersTraceHises() {
		return persTraceHises;
	}

	
	public void setPersTraceHises(List<PrpLDlossPersTraceHisVo> persTraceHises) {
		this.persTraceHises = persTraceHises;
	}


	public BigDecimal getInvoiceFee() {
		return invoiceFee;
	}


	public void setInvoiceFee(BigDecimal invoiceFee) {
		this.invoiceFee = invoiceFee;
	}
	
}
