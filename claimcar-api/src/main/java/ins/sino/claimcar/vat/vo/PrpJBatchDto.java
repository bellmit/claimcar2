package ins.sino.claimcar.vat.vo;

import java.io.Serializable;
import java.util.List;

public class PrpJBatchDto implements Serializable{
    private static final long serialVersionUID = 1L;
	private String invoiceId;
	private List<PrpJecdInvoiceDto> prpJecdInvoiceDtoList;
	public String getInvoiceId() {
		return invoiceId;
	}
	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}
	public List<PrpJecdInvoiceDto> getPrpJecdInvoiceDtoList() {
		return prpJecdInvoiceDtoList;
	}
	public void setPrpJecdInvoiceDtoList(
			List<PrpJecdInvoiceDto> prpJecdInvoiceDtoList) {
		this.prpJecdInvoiceDtoList = prpJecdInvoiceDtoList;
	}
    

}
