package ins.sino.claimcar.vat.vo;

import java.io.Serializable;

public class ResVatResultVo implements Serializable{
	
private static final long serialVersionUID = 1L;


private String resultCode;//处理结果
private String resultMsg;//处理结果描述
public String getResultCode() {
	return resultCode;
}
public void setResultCode(String resultCode) {
	this.resultCode = resultCode;
}
public String getResultMsg() {
	return resultMsg;
}
public void setResultMsg(String resultMsg) {
	this.resultMsg = resultMsg;
}
 /**
  * 返回信息
  * @param resultCode
  * @param resultMsg
  * @return
  */
 public static ResVatResultVo success(String resultCode,String resultMsg){
	 ResVatResultVo resVatResultVo=new ResVatResultVo();
	 resVatResultVo.setResultCode(resultCode);
	 resVatResultVo.setResultMsg(resultMsg);
	 return resVatResultVo;
 }

}
