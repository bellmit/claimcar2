package ins.sino.claimcar.sdpolice.policeInfoVo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("WarnInfo")
public class WarnInfoVo {
	@XStreamAlias("WarnMessage")	
	private String warnMessage;//预警信息
	@XStreamAlias("SerialNumber")
	private String serialNumber;
	
	public String getWarnMessage() {
		return warnMessage;
	}

	public void setWarnMessage(String warnMessage) {
		this.warnMessage = warnMessage;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
}
