package ins.sino.claimcar.trafficplatform.vo;


public class SdSzReqponseVo implements java.io.Serializable{
	
	private static final long serialVersionUID = 8423652723600188374L;

    private String appId;//合作方唯一标识ID，由我方提供
    private String serialNo;//通讯流水号
    private String timeStamp;//格式为yyyyMMddHHmmss（北京时间）
    private String method;//服务接口名称（见3.4接口列表中的请求方法名）
    private AccidentInfo data;//业务数据加密字符串（加密方式见”4.2对称加密”章节）
    private String sign;//对上述所有字段序列化后并进行签名的值（计算方式见”4.1签名算法”章节）
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}

	
	public AccidentInfo getData() {
		return data;
	}
	public void setData(AccidentInfo data) {
		this.data = data;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
    
  
    
}
