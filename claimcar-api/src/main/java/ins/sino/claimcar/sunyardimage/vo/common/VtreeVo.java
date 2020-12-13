package ins.sino.claimcar.sunyardimage.vo.common;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

public class VtreeVo implements Serializable{
	@XStreamAsAttribute
	@XStreamAlias("APP_CODE")
	private String appCode;
	@XStreamAsAttribute
	@XStreamAlias("APP_NAME")
	private String appName;
	
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
}
