package ins.sino.claimcar.other.service;

import java.util.Date;

public interface SendSMSService {

	/****
	 * 按内容发送
	 * 
	 * @param msgID 短信编码（长度不能大于32， 也可为空）
	 * @param bussNo 业务号 （可为空）
	 * @param phoneNo 手机号
	 * @param message 短信内容
	 * @param useCode 发送工号（可为空）
	 * @param comCode 发送机构（可为空）
	 * @param sendTime 发送时间（可为空）
	 * @return
	 */
	public boolean sendSMSContent(String msgID,String bussNo,String phoneNo,String message,String useCode,String comCode,Date sendTime);
}
