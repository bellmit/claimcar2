package ins.sino.claimcar.claim.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("WarnInfo")
public class WarnInfoSdVo implements Serializable{

 @XStreamAlias("SerialNumber")
 private String serialNumber;//序号
 @XStreamAlias("WarnMessage")
 private String warnMessage;//预警话术
public String getSerialNumber() {
	return serialNumber;
}
public void setSerialNumber(String serialNumber) {
	this.serialNumber = serialNumber;
}
public String getWarnMessage() {
	return warnMessage;
}
public void setWarnMessage(String warnMessage) {
	this.warnMessage = warnMessage;
}
 
}
