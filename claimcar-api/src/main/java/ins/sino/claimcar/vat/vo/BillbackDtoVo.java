package ins.sino.claimcar.vat.vo;

import java.io.Serializable;

public class BillbackDtoVo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String invoiceId;
	private String causeMessage;
	
	public String getInvoiceId() {
		return invoiceId;
	}
	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}
	public String getCauseMessage() {
		return causeMessage;
	}
	public void setCauseMessage(String causeMessage) {
		this.causeMessage = causeMessage;
	}
	


}
