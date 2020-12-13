package ins.sino.claimcar.claimjy.util;

import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JyHttpUtil {
	private static Logger LOGGER = LoggerFactory.getLogger(JyHttpUtil.class);

	public static String sendData(String requestXML, String urlStr, int seconds) throws Exception {
		long t1 = System.currentTimeMillis();
		String responseXml = "";
		HttpURLConnection httpUrlConn = null;
		OutputStream outputStream = null;
		BufferedReader bufferedReader = null;
		StringBuffer buffer = new StringBuffer();
		try {
			URL url = new URL(urlStr);
			httpUrlConn = (HttpURLConnection) url.openConnection();
			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			httpUrlConn.setRequestMethod("POST");// 设置请求方式（GET/POST）
			httpUrlConn.setRequestProperty("Content-Type","text/xml;charset=GBK");
			httpUrlConn.setConnectTimeout(seconds * 1000);
			httpUrlConn.setReadTimeout(seconds * 1000);
			httpUrlConn.setRequestProperty("Connection", "Keep-Alive");
			httpUrlConn.connect();
			String outputStr = requestXML;
			outputStream = httpUrlConn.getOutputStream();// 当有数据需要提交时
			if (null != outputStr) {
				outputStream.write(outputStr.getBytes("GBK"));// 注意编码格式，防止中文乱码 outputStream.write
			}
			// 将返回的输入流转换成字符串
			if (null != httpUrlConn.getInputStream()) {
				InputStream inputStream = httpUrlConn.getInputStream();
				bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "GBK"));
			} else {
				bufferedReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream("精友未返回报文".getBytes()), "UTF-8"));
			}
			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			if (buffer.length() < 1) {
				throw new Exception("精友返回数据失败");
			}		
			responseXml = buffer.toString();
		} catch (ConnectException ce) {
			throw new Exception("与精友连接失败，请稍候再试",ce);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("读取精友返回数据失败",e);
		} finally {
			if(outputStream != null) {
                outputStream.close();
            }
			if(bufferedReader != null){
				bufferedReader.close();
			}
			if(httpUrlConn != null){
				httpUrlConn.disconnect();
			}
			LOGGER.warn("接口({})调用耗时{}ms",urlStr,System.currentTimeMillis()-t1);
		}
		return responseXml;
	}
}
