package ins.sino.claimcar.vat.vo;

import java.io.Serializable;

public class UntyingInvoiceResDtoVo implements Serializable{
	private static final long serialVersionUID = 1L;
	private String invoiceId;//vat发票id
	private String openFlag;//批次号解绑标志
	private String describe;//批次号解绑描述
	
	public String getOpenFlag() {
		return openFlag;
	}
	public void setOpenFlag(String openFlag) {
		this.openFlag = openFlag;
	}
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	public String getInvoiceId() {
		return invoiceId;
	}
	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}
	
}
