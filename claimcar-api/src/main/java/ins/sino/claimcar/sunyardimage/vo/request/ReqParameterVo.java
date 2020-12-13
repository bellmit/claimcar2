package ins.sino.claimcar.sunyardimage.vo.request;

import java.io.Serializable;

public class ReqParameterVo implements Serializable{
 private String sunIcmsIp;//ip
 private int socketNo;//socket端口
 private String key;//秘钥
 private String appCode;//节点代码
 private String id;//接入系统ID
public String getSunIcmsIp() {
	return sunIcmsIp;
}
public void setSunIcmsIp(String sunIcmsIp) {
	this.sunIcmsIp = sunIcmsIp;
}
public int getSocketNo() {
	return socketNo;
}
public void setSocketNo(int socketNo) {
	this.socketNo = socketNo;
}
public String getKey() {
	return key;
}
public void setKey(String key) {
	this.key = key;
}
public String getAppCode() {
	return appCode;
}
public void setAppCode(String appCode) {
	this.appCode = appCode;
}
public String getId() {
	return id;
}
public void setId(String id) {
	this.id = id;
}

 
}
