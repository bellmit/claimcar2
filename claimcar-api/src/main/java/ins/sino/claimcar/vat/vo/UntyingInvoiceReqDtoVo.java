package ins.sino.claimcar.vat.vo;

import java.io.Serializable;

public class UntyingInvoiceReqDtoVo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String invoiceId;//vat发票id

	public String getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}

	
}
