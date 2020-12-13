package ins.sino.claimcar.vat.vo;

import java.io.Serializable;

public class ResmessageVo implements Serializable{

 private static final long serialVersionUID = 1L;
 
 public String code;//返回编码 0000--表示成功，9999--表示失败
 
 public String openBillNos;//撤回成功的发票号码
 
 public String closeBillNos;//不允许撤回的发票号码
 
 public String errormessage;//错误消息

public String getCode() {
	return code;
}

public void setCode(String code) {
	this.code = code;
}

public String getOpenBillNos() {
	return openBillNos;
}

public void setOpenBillNos(String openBillNos) {
	this.openBillNos = openBillNos;
}

public String getCloseBillNos() {
	return closeBillNos;
}

public void setCloseBillNos(String closeBillNos) {
	this.closeBillNos = closeBillNos;
}

public String getErrormessage() {
	return errormessage;
}

public void setErrormessage(String errormessage) {
	this.errormessage = errormessage;
}

 

}
