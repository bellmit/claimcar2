/**
 * 
 */
package ins.sino.claimcar.sendSMSInterface.service.spring;

import ins.platform.utils.DateUtils;
import ins.sino.claimcar.other.service.SendSMSService;
import ins.sino.claimcar.sendSMSInterface.dto.SendContentDto;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.SpringProperties;

import com.alibaba.dubbo.config.annotation.Service;


/**
 * 短信发送类
 * 
 * @author LYK
 */
@Service(protocol = {"dubbo"}, validation = "true" , registry = {"default"})
@Path(value = "sendSMSService")
public class SendSMSServiceImpl implements SendSMSService{

	private static Logger logger = LoggerFactory.getLogger(SendSMSServiceImpl.class);
	/** 接口地址 **/
	private String apiWebUrl = SpringProperties.getProperty("SmsUrl")+"/SMS";
	private static final String systemCode = "CLAIMCAR";
	/** 发送成功 数据返回包含的串 **/
	private static String resultStr = "<RESPONSE_CODE>1</RESPONSE_CODE>";

//	public SendSMSServiceImpl(String url){
//		apiWebUrl = url;
//	}
	
	@Override
	public boolean sendSMSContent(String msgID,String bussNo,String phoneNo,String message,String useCode,String comCode,Date sendTime) {
		SendContentDto dto = new SendContentDto();
		//通信渠道
		String channel = SpringProperties.getProperty("Sms_Channel");
		dto.setMsgID(msgID);
		dto.setBussNo(bussNo);
		dto.setPhoneNo(phoneNo);
		dto.setMessage(message);
		dto.setOptCode(useCode);
		dto.setComCode(comCode);
		dto.setSendTime(sendTime);
		dto.setChannelType(channel);
		return sendSMSContent(dto);
	}

	public boolean sendSMSContent(SendContentDto dto) {
		boolean result = false;
		if ("".equals(apiWebUrl) || apiWebUrl == null) {
			return result;
		}
		try {
			String xml = packetS01(dto);
			logger.info("短信推送地址为：{}\n短信推送内容为：{}", apiWebUrl, xml);
			String resXml = "";
			resXml = request(xml, apiWebUrl);
			// ���ͷ���resXml <RESPONSE_CODE>1</RESPONSE_CODE>
			if (resXml.indexOf(resultStr) > -1) {
				result = true;
			}
		} catch (Exception e) {
			logger.info("推送短信平台失败！", e);
			e.printStackTrace();
			result = false;
		}
		return result;
	}

	/****
	 * 按内容发送数据打包
	 * 
	 * @param dto
	 * @return
	 */
	public String packetS01(SendContentDto dto) {
		StringBuffer xmlBuf = new StringBuffer();

		xmlBuf.append("<PACKET type=\"REQUEST\" version=\"1.0\">");
		xmlBuf.append(packetHead("S01"));
		xmlBuf.append("<BODY>");
		String msgID = dto.getMsgID();
		String bussNo = dto.getBussNo();
		if(msgID!=null&& !"".equals(msgID)){
			xmlBuf.append("<MSGID>"+msgID+"</MSGID>");
		}
		if(bussNo!=null&& !"".equals(bussNo)){
			xmlBuf.append("<BUSSNO>"+bussNo+"</BUSSNO>");
		}

		String phoneNo = dto.getPhoneNo();
		if(phoneNo!=null&& !"".equals(phoneNo)){
			xmlBuf.append("<PHONENO>"+phoneNo+"</PHONENO>");
		}
		String message = dto.getMessage();
		if(message!=null&& !"".equals(message)){
			xmlBuf.append("<MESSAGE>"+message+"</MESSAGE>");
		}

		String optCode = dto.getOptCode();
		if(optCode!=null&& !"".equals(optCode)){
			xmlBuf.append("<OPTCODE>"+optCode+"</OPTCODE>");
		}
		String remark = dto.getRemark();
		if(remark!=null&& !"".equals(remark)){
			xmlBuf.append("<REMARK>"+remark+"</REMARK>");
		}
		String custName = dto.getCustName();
		if(custName!=null&& !"".equals(custName)){
			xmlBuf.append("<CUSTNAME>"+custName+"</CUSTNAME>");
		}
		String comCode = dto.getComCode();
		if(comCode!=null&& !"".equals(comCode)){
			xmlBuf.append("<COMCODE>"+comCode+"</COMCODE>");
		}
		Date date = dto.getSendTime();
		if(date != null){
			String date_str=DateUtils.dateToStr(date, "yyyyMMddHHmm")+"00";
			xmlBuf.append("<SENDTIME>"+date_str+"</SENDTIME>");
		}
		String channelType = dto.getChannelType();
		if(channelType != null){
			xmlBuf.append("<CHANNELTYPE>"+channelType+"</CHANNELTYPE>");
		}
		
		// xmlBuf.append("<OPERATORCODE>"+dto.getOperatorCode()+"</OPERATORCODE>");

		xmlBuf.append("</BODY>");
		xmlBuf.append("</PACKET>");

		return xmlBuf.toString();
	}

	/***
	 * 请求传出数据
	 * 
	 * @param requestXML 传输xml串
	 * @param strURL 请求的url地址
	 * @return
	 * @throws Exception
	 * @modified: ☆LiangYouKu(2013-12-6 上午10:36:52): <br>
	 */
	public String request(String requestXML,String strURL) throws Exception {
		String returnXml = "";
		StringBuffer buffer = new StringBuffer();
		String strMessage = "";
		BufferedReader reader = null;
		URL url = null;
		HttpURLConnection connection = null;
		OutputStream outputStream = null;
		DataOutputStream out = null;
		try{
			url = new URL(strURL);
			connection = (HttpURLConnection)url.openConnection();
			connection.setConnectTimeout(10000);// 10s�������ϾͶϿ�
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			// post��ʽ����ʹ�û���
			connection.setUseCaches(false);
			// ���ñ������ӵ�Content-Type������Ϊtext/xml
			connection.setRequestProperty("Content-Type","text/xml;charset=GBK");
			// ά�ֳ�����
			connection.setRequestProperty("Connection","Keep-Alive");
			connection.setAllowUserInteraction(true);
			connection.connect();

			// xml��
			outputStream = connection.getOutputStream();
			out = new DataOutputStream(outputStream);
			out.write(requestXML.getBytes("GBK"));
			out.flush();
			if(connection.getResponseCode()==200){
				reader = new BufferedReader(new InputStreamReader(connection.getInputStream(),"GBK"));
				while(( strMessage = reader.readLine() )!=null){
					buffer.append(strMessage);
				}
			}
		}
		finally{
			if(out!=null){
				out.close();
			}
			if(outputStream!=null){
				outputStream.close();
			}
			if(reader!=null){
				reader.close();
			}
		}
		returnXml = buffer.toString();
		return returnXml;
	}
	
	/****
	 * 头打包
	 * 
	 * @param requestType 内容：S01 ,模板：S02
	 * @return
	 */
	public String packetHead(String requestType) {
		StringBuffer xmlBuf = new StringBuffer();
		xmlBuf.append("<HEAD>");
		xmlBuf.append("<REQUEST_TYPE>"+requestType+"</REQUEST_TYPE>");
		xmlBuf.append("<SYSTEM>"+systemCode+"</SYSTEM>");
		xmlBuf.append("</HEAD>");
		return xmlBuf.toString();
	}
}
