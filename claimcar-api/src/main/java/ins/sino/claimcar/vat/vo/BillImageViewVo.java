package ins.sino.claimcar.vat.vo;

import java.io.Serializable;
import java.util.List;

public class BillImageViewVo implements Serializable{
	private static final long serialVersionUID = 1L;
	private String sendTime;//推送时间
	private String systemType;//系统标识
	private List<BillImageViewSonVo> billImageDtoList;//发票信息集合
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
	public List<BillImageViewSonVo> getBillImageDtoList() {
		return billImageDtoList;
	}
	public void setBillImageDtoList(List<BillImageViewSonVo> billImageDtoList) {
		this.billImageDtoList = billImageDtoList;
	}
    
	
}
