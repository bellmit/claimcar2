package ins.sino.claimcar.sms.service;

import java.util.Date;
import java.util.Map;

/**
 * 短信发送服务
 * 
 * @author ★<a href="mailto:huangyi@sinosoft.com.cn">HuangYi</a>
 * @Company www.sinosoft.com.cn
 * @Copyright Copyright (c) 2014-6-18
 * @since (2014-6-18 下午05:23:20): <br>
 */
public interface SmsService {


	/**
	 * 按内容发送
	 * 
	 * @param bussNo 业务号 （可为空）
	 * @param phoneNo 手机号
	 * @param message 短信内容
	 * @return
	 * @author ☆HuangYi(2014-7-2 上午10:20:18): <br>
	 */
	public boolean sendSMSContent(String bussNo,String phoneNo,String message);

	/**
	 * 按内容发送
	 * 
	 * @param bussNo 业务号 （可为空）
	 * @param phoneNo 手机号
	 * @param message 短信内容
	 * @param useCode 发送工号（可为空）
	 * @param comCode 发送机构（可为空）
	 * @return
	 * @author ☆HuangYi(2014-7-2 上午10:20:18): <br>
	 */
	public boolean sendSMSContent(String bussNo,String phoneNo,String message,String useCode,String comCode);
	
	/**
	 * 按内容发送
	 * 
	 * @param bussNo 业务号 （可为空）
	 * @param phoneNo 手机号
	 * @param message 短信内容
	 * @param useCode 发送工号（可为空）
	 * @param comCode 发送机构（可为空）
	 * @param sendTime 发送时间（可为空）
	 * @return
	 * @author ☆wurh(2017-2-22 上午10:20:18): <br>
	 */
	public boolean sendSMSContent(String bussNo,String phoneNo,String message,String useCode,String comCode,Date sendTime);

	/***
	 * 按模板发送
	 * 
	 * @param bussNo 业务号 （可为空）
	 * @param phoneNo 手机号 不能为空
	 * @param modelID 短信模板号 不能为空
	 * @param paramsMap 模板参数列表 如：map.put("bussNo", "0000000000");
	 * @return
	 * @author ☆HuangYi(2014-7-2 上午10:24:55): <br>
	 */
	public boolean sendSMSModel(String bussNo,String phoneNo,String modelID,Map<String,String> paramsMap);

	/***
	 * 按模板发送
	 * 
	 * @param bussNo 业务号 （可为空）
	 * @param phoneNo 手机号 不能为空
	 * @param modelID 短信模板号 不能为空
	 * @param useCode 发送工号（可为空）
	 * @param comCode 发送机构（可为空）
	 * @param paramsMap 模板参数列表 如：map.put("bussNo", "0000000000");
	 * @return
	 * @author ☆HuangYi(2014-7-2 上午10:24:55): <br>
	 */
	public boolean sendSMSModel(String bussNo,String phoneNo,String modelID,String useCode,String comCode,
								Map<String,String> paramsMap);
}
