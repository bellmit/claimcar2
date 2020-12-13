package ins.sino.claimcar.newpayment.vo;

import java.io.Serializable;
import java.util.List;


public class InvoiceTransReturnVo implements Serializable{


	private static final long serialVersionUID = 1L;
	/** 结算单号*/
	private String invoiceId;
	/** 推送时间*/
	private String sendTime;
	/** 系统标识*/
	private String systemType;
	/** 价税明细数据*/
	private List<InvoiceDetailVo> prpJecdInvoiceDtoList;
	public String getInvoiceId() {
		return invoiceId;
	}
	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}
	public String getSendTime() {
		return sendTime;
	}
	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}
	public String getSystemType() {
		return systemType;
	}
	public void setSystemType(String systemType) {
		this.systemType = systemType;
	}
	public List<InvoiceDetailVo> getPrpJecdInvoiceDtoList() {
		return prpJecdInvoiceDtoList;
	}
	public void setPrpJecdInvoiceDtoList(List<InvoiceDetailVo> prpJecdInvoiceDtoList) {
		this.prpJecdInvoiceDtoList = prpJecdInvoiceDtoList;
	} 
	
	
}
