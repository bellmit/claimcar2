package ins.sino.claimcar.vat.vo;

import java.io.Serializable;
import java.util.List;

public class BindInvoiceReqDto implements Serializable{
	private static final long serialVersionUID = 1L;
	private String sendTime; 
	private String systemType;
	private List<PrpJBatchDto> prpJBatchDtoList;
	
	
	public String getSystemType() {
		return systemType;
	}
	public void setSystemType(String systemType) {
		this.systemType = systemType;
	}
	public List<PrpJBatchDto> getPrpJBatchDtoList() {
		return prpJBatchDtoList;
	}
	public void setPrpJBatchDtoList(List<PrpJBatchDto> prpJBatchDtoList) {
		this.prpJBatchDtoList = prpJBatchDtoList;
	}
	public String getSendTime() {
		return sendTime;
	}
	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}
    

}
