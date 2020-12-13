package ins.sino.claimcar.mobilecheck.util;


import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.SpringProperties;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class QuickClaimUtil {

	private static Logger logger = LoggerFactory.getLogger(QuickClaimUtil.class);
	
	
	public static String requestPlatform(String requestXML,String url) throws Exception {
		long t1 = System.currentTimeMillis();
		try {
			BufferedReader reader = null;
			HttpURLConnection connection = null;
			StringBuffer buffer = new StringBuffer();
			//URL urlAdd = new URL(url);
			URL urlAdd = new URL(url+URLEncoder.encode(requestXML, "utf-8"));
			//获取超时时间
			String seconds = SpringProperties.getProperty("HTTP_TIMEOUT");
			if(StringUtils.isBlank(seconds)){
				seconds = "20";
			}
			seconds = "20";

			try {
				connection = (HttpURLConnection) urlAdd.openConnection();
				connection.setDoInput(true);
				connection.setDoOutput(true);
				connection.setRequestMethod("POST");
				// post方式不能使用缓存
				connection.setUseCaches(false);
				// 配置本次连接的Content-Type，配置为text/xml
				connection.setRequestProperty("Content-Type",
						"text/xml;charset=utf-8");
				// 维持长连接
				connection.setRequestProperty("Connection", "Keep-Alive");
				connection.setConnectTimeout(Integer.valueOf(seconds) * 1000);
				connection.setReadTimeout(Integer.valueOf(seconds) * 1000);
				connection.setAllowUserInteraction(true);
				connection.connect();
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new Exception("与移动查勘连接失败，请稍候再试", ex);
			}
			try {
				OutputStream outputStream = connection.getOutputStream();
				DataOutputStream out = new DataOutputStream(outputStream);
				out.write(requestXML.getBytes("utf-8"));
				out.flush();
				out.close();
				reader = new BufferedReader(new InputStreamReader(
						connection.getInputStream(), "UTF-8"));
				String strLine = "";
				while ((strLine = reader.readLine()) != null) {
					buffer.append(strLine);
				}
				if (buffer.length() < 1) {
					throw new Exception("移动查勘返回数据失败");
				}
			} catch (IOException e) {
				e.printStackTrace();
				throw new Exception("读取移动查勘返回数据失败", e);
			} finally {
				if (reader != null) {
					reader.close();
				}
			}
			return buffer.toString();
		} finally {
			logger.warn("平台({})调用耗时{}ms", url, System.currentTimeMillis() - t1);
		}
	}

	/**
	 * MClaimPlatform_URL_IN   prplschedule/autoSchedule.do "PersonnelInformation" "ScheduleSubmit"
	 * @param requestXML
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static String requestScheduledPlatform(String requestXML,String url) throws Exception {
		long t1 = System.currentTimeMillis();
		try {
			BufferedReader reader = null;
			HttpURLConnection connection = null;
			StringBuffer buffer = new StringBuffer();
			URL urlAdd = new URL(url);
			//获取超时时间
			String seconds = SpringProperties.getProperty("HTTP_TIMEOUT");
			if(StringUtils.isBlank(seconds)){
				seconds = "20";
			}
			seconds = "20";
			try {
				connection = (HttpURLConnection) urlAdd.openConnection();
				connection.setDoInput(true);
				connection.setDoOutput(true);
				connection.setRequestMethod("POST");
				// post方式不能使用缓存
				connection.setUseCaches(false);
				// 配置本次连接的Content-Type，配置为text/xml
				//connection.setRequestProperty("Content-Type","text/xml;charset=utf-8");
				connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
				// 维持长连接
				connection.setRequestProperty("Connection", "Keep-Alive");
				connection.setConnectTimeout(Integer.valueOf(seconds) * 1000);
				connection.setReadTimeout(Integer.valueOf(seconds) * 1000);
				connection.setAllowUserInteraction(true);
				connection.connect();
			} catch (Exception ex) {
				logger.error("与移动查勘连接失败",ex);
				throw new Exception("与移动查勘连接失败，请稍候再试", ex);
			}
			try {
				OutputStream outputStream = connection.getOutputStream();
				DataOutputStream out = new DataOutputStream(outputStream);
				out.writeBytes("xml=" + URLEncoder.encode(requestXML, "utf-8"));
				out.flush();
				out.close();
				reader = new BufferedReader(new InputStreamReader(
						connection.getInputStream(), "UTF-8"));
				String strLine = "";
				while ((strLine = reader.readLine()) != null) {
					buffer.append(strLine);
				}
				if (buffer.length() < 1) {
					throw new Exception("移动查勘返回数据失败");
				}
			} catch (IOException e) {
				logger.error("读取移动查勘返回数据失败",e);
				throw new Exception("读取移动查勘返回数据失败", e);
			} finally {
				if (reader != null) {
					reader.close();
				}
				if(connection != null){
					connection.disconnect();
				}
			}
			return buffer.toString();
		} finally {
			logger.info("移动查勘平台({})调用耗时{}ms", url, System.currentTimeMillis() - t1);
		}
	}

	public static void main(String[] args) throws Exception {
		String a = QuickClaimUtil.requestScheduledPlatform("<DAMAGEADDRESS>上海市浦东新区</DAMAGEADDRESS>","http://localhost:9001/claimcar/mobileCheckDflossTestServlet");
		System.out.println(a);
	}
}
