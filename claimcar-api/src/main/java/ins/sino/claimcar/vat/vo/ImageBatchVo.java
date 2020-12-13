package ins.sino.claimcar.vat.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("BATCH")
public class ImageBatchVo implements Serializable{
	private static final long serialVersionUID = 1L;
	@XStreamAlias("APP_CODE")
	private String appCode;
	@XStreamAlias("APP_NAME")
	private String appName;
	@XStreamAlias("BUSI_NO")
	private String busiNo;
	public String getAppCode() {
		return appCode;
	}
	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getBusiNo() {
		return busiNo;
	}
	public void setBusiNo(String busiNo) {
		this.busiNo = busiNo;
	}
	
	
}
