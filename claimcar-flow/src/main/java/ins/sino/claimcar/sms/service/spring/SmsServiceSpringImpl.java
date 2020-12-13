package ins.sino.claimcar.sms.service.spring;

import ins.sino.claimcar.other.service.SendSMSService;
import ins.sino.claimcar.sms.service.SmsService;

import java.util.Date;
import java.util.Map;

import javax.ws.rs.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;

import com.alibaba.dubbo.config.annotation.Service;
import com.sinosoft.api.SMSInterFace.SendSMSInterFace;

/**
 * 短信发送服务实例
 * 
 * @author ★<a href="mailto:huangyi@sinosoft.com.cn">HuangYi</a>
 * @Company www.sinosoft.com.cn
 * @Copyright Copyright (c) 2014-7-2
 * @since (2014-7-2 上午10:28:34): <br>
 */
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path("smsService")
public class SmsServiceSpringImpl implements SmsService {

	private static final String urlSuffix = "/SMS";
	private static final String systemCode = "CLAIMCAR";
	
	private SendSMSInterFace smsApi;// 远程发送短信接口
	
	@Autowired
	SendSMSService sendSMSService;

	public SmsServiceSpringImpl(){
		
		String url = SpringProperties.getProperty("SmsUrl")+urlSuffix;
		smsApi = new SendSMSInterFace(url);
	}


	@Override
	public boolean sendSMSContent(String bussNo,String phoneNo,String message) {
		return smsApi.sendSMSContent(systemCode,null,bussNo,phoneNo,message);
	}


	@Override
	public boolean sendSMSModel(String bussNo,String phoneNo,String modelID,Map<String,String> paramsMap) {
		return smsApi.sendSMSModel(systemCode,null,bussNo,phoneNo,modelID,paramsMap);
	}

	@Override
	public boolean sendSMSContent(String bussNo,String phoneNo,String message,String useCode,String comCode) {
		return smsApi.sendSMSContent(systemCode,null,bussNo,phoneNo,message,useCode,comCode);
	}
	
	@Override
	public boolean sendSMSContent(String bussNo,String phoneNo,String message,String useCode,String comCode,Date sendTime) {
		return sendSMSService.sendSMSContent(null,bussNo,phoneNo,message,useCode,comCode,sendTime);
	}

	@Override
	public boolean sendSMSModel(String bussNo,String phoneNo,String modelID,String useCode,String comCode,
								Map<String,String> paramsMap) {
		return smsApi.sendSMSModel(systemCode,null,bussNo,phoneNo,modelID,useCode,comCode,paramsMap);
	}

}
