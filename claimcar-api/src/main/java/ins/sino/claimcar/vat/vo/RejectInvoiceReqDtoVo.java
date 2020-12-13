package ins.sino.claimcar.vat.vo;

import java.io.Serializable;
import java.util.List;

public class RejectInvoiceReqDtoVo implements Serializable{

	private static final long serialVersionUID = 1L;
	private String sendTime;
	private String systemType;
	private List<BillbackDtoVo> billbacksDtoList;
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
	public List<BillbackDtoVo> getBillbacksDtoList() {
		return billbacksDtoList;
	}
	public void setBillbacksDtoList(List<BillbackDtoVo> billbacksDtoList) {
		this.billbacksDtoList = billbacksDtoList;
	}
	
	



}
