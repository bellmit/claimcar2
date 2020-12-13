package ins.sino.claimcar.claimcarYJ.vo;
public class ReqHeadYJVo implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	private String appId;//合作方唯一标识ID，由我方提供
	private String timeStamp;//格式为yyyyMMddHHmmss（北京时间）
	private String sign;//对上述所有字段序列化后并进行签名的值
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
     
}
