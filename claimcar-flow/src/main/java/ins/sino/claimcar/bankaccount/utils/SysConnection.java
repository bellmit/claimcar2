package ins.sino.claimcar.bankaccount.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

import org.springframework.core.SpringProperties;

/**
 * 发送请求服务
 * 
 * @author ★<a href="mailto:huangyi@sinosoft.com.cn">HuangYi</a>
 * @Company www.sinosoft.com.cn
 * @Copyright Copyright (c) 2014-7-2
 * @since (2014-7-2 下午05:17:50): <br>
 */
public class SysConnection {

	private final static int connectTimeout = 100;// 连接超时(单位秒)

	/**
	 * 发送数据(Post)
	 * 
	 * @param requestXML 发送的XML数据报文
	 * @param strURL 请求的地址
	 * @return
	 * @throws Exception
	 * @author ☆HuangYi(2014-7-2 下午05:17:14): <br>
	 */
	public static String requestPost(String requestXML,String strURL) throws Exception {
		StringBuffer buffer = new StringBuffer();
		String strMessage = "";
		BufferedReader reader = null;
		URL url = null;
		HttpURLConnection connection = null;
		try{
			url = new URL(strURL);
			connection = (HttpURLConnection)url.openConnection();
			connection.setConnectTimeout(connectTimeout*1000);// 100s内连不上就断开
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			// post方式不能使用缓存
			connection.setUseCaches(false);
			// 配置本次连接的Content-Type，配置为text/xml
			connection.setRequestProperty("Content-Type","text/xml;charset=GBK");
			// 维持长连接
			connection.setRequestProperty("Connection","Keep-Alive");
			connection.setAllowUserInteraction(true);
			connection.connect();

			// xml串
			OutputStream outputStream = connection.getOutputStream();
			DataOutputStream out = new DataOutputStream(outputStream);
			out.write(requestXML.getBytes("GBK"));
			out.flush();
			out.close();

			if(connection.getResponseCode()==200){
				reader = new BufferedReader(new InputStreamReader(connection.getInputStream(),"GBK"));
				while(( strMessage = reader.readLine() )!=null){
					buffer.append(strMessage);
				}
			}
		}
		catch(ConnectException e){
			throw new Exception("服务异常无法完成该操作");
		}
		catch(SocketTimeoutException e){
			throw new Exception("服务请求超时请重新操作");
		}
		finally{
			if(reader!=null){
				reader.close();
			}
		}
		String returnXml = buffer.toString();
		return returnXml;
	}

	/**
	 * 发送数据到apiweb接口平台服务
	 * 
	 * @param requestXML 发送的XML数据报文
	 * @param servicerCode 接口代码
	 * @return
	 * @throws Exception
	 * @author ☆HuangYi(2014-7-2 下午05:25:39): <br>
	 */
	public static String requestApiweb(String requestXML,String servicerCode) throws Exception {
		String strURL = SpringProperties.getProperty("ApiWebServer")+"/"+servicerCode;
		return requestPost(requestXML,strURL);
	}

	/**
	 * 发送数据到apiweb接口平台服务请求接口默认为SystemService
	 * 
	 * @param requestXML 发送的XML数据报文
	 * @param servicerCode 接口代码
	 * @return
	 * @throws Exception
	 * @author ☆HuangYi(2014-7-2 下午05:25:39): <br>
	 */
	public static String requestApiweb(String requestXML) throws Exception {
		String strURL = SpringProperties.getProperty("ApiWebServer")+"/SystemService";
		return requestPost(requestXML,strURL);
	}

}
