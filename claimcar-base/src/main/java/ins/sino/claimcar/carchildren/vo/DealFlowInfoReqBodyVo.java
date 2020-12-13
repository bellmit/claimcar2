package ins.sino.claimcar.carchildren.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("BODY")
public class DealFlowInfoReqBodyVo implements Serializable{
	private static final long serialVersionUID = 1L;
	//报案号
	@XStreamAlias("REGISTNO")
	private String registNo;
	//时间戳
	@XStreamAlias("TIMESTAMP")
	private String timestamp;
	
	public String getRegistNo() {
		return registNo;
	}
	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
}
