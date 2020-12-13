package ins.sino.claimcar.carchild.util;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import net.chetong.sdk.client.ChetongHttpClient;
import net.chetong.sdk.model.RequestObject;
import net.chetong.sdk.vo.CommonRequestVo;
import net.chetong.sdk.vo.CommonResultVO;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.SpringProperties;

public class CarchildUtil {

	private static Logger logger = LoggerFactory.getLogger(CarchildUtil.class);
	
	
	public static String requestCarchild(String requestXML,String url,String sourceType) throws Exception {
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

			try {
				connection = (HttpURLConnection) urlAdd.openConnection();
				connection.setDoInput(true);
				connection.setDoOutput(true);
				connection.setRequestMethod("POST");
				connection.setRequestProperty("Content-type","application/xml;charset=UTF-8");
				connection.setRequestProperty("X-User-Mobile","18800001111");
				connection.setRequestProperty("X-User-Token","123456");
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
				throw new Exception("与"+sourceType+"连接失败，请稍候再试", ex);
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
					throw new Exception(sourceType+"返回数据失败");
				}
			} catch (IOException e) {
				e.printStackTrace();
				throw new Exception("读取"+sourceType+"返回数据失败", e);
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
	
	
	public static String requestPlatform(String requestXML,String urlStr,String sign) throws Exception {
        long t1 = System.currentTimeMillis();
        String responseXml="";
        StringBuffer buffer = new StringBuffer();    
        //获取超时时间
		String seconds = SpringProperties.getProperty("HTTP_TIMEOUT");
		if(StringUtils.isBlank(seconds)){
			seconds = "20";
		}
            try {    
             
                URL url = new URL(urlStr);
                HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();
                httpUrlConn.setDoOutput(true);    
                httpUrlConn.setDoInput(true);    
                httpUrlConn.setUseCaches(false);    
                // 设置请求方式（GET/POST）    
                httpUrlConn.setRequestMethod("POST"); 
                httpUrlConn.setConnectTimeout(Integer.valueOf(seconds) * 1000);
                httpUrlConn.setReadTimeout(Integer.valueOf(seconds) * 1000);
                httpUrlConn.setRequestProperty("Content-type","application/xml;charset=UTF-8");
                if(sign.contains("MTA")){
                	httpUrlConn.setRequestProperty("X-User-Mobile","18800001111");
                    httpUrlConn.setRequestProperty("X-User-Token","123456");
                }
                
                httpUrlConn.connect();    
        
                String outputStr =requestXML;
                            
                OutputStream outputStream = httpUrlConn.getOutputStream();                  
                // 当有数据需要提交时    
                if (null != outputStr) {    
                    // 注意编码格式，防止中文乱码    outputStream.write
                    outputStream.write(outputStr.getBytes("utf-8"));    
                }    
        
                // 将返回的输入流转换成字符串    
                InputStream inputStream = httpUrlConn.getInputStream();    
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");    
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);    
                
                String str = null;    
                while ((str = bufferedReader.readLine()) != null) {
                    
                    buffer.append(str);    
                } 
                if (buffer.length() < 1) {
                    throw new Exception("返回数据失败");
                }
                bufferedReader.close();    
                inputStreamReader.close();    
                // 释放资源  
                outputStream.flush();
                outputStream.close();
                inputStream.close();    
                inputStream = null;    
                httpUrlConn.disconnect(); 
                responseXml=buffer.toString();
                
            } catch (ConnectException ce) {
                throw new Exception("连接失败，请稍候再试", ce);
            } catch (Exception e) {
                e.printStackTrace();
                throw new Exception("读取返回数据失败", e);
                
            } finally {
                //logger.warn("接口({})调用耗时{}ms", urlStr, System.currentTimeMillis() - t1);
            }    
            return responseXml;
    }

    /*
    * 对接车童网接口调用封装方法
    * */
    public static String requestPlatformForCT(String requestXML,String urlStr,String type) throws Exception {
        String env = SpringProperties.getProperty("CT_ENV");
        String appkey = SpringProperties.getProperty("CT_APPKEY");
        String privateKey = SpringProperties.getProperty("CT_PRIVATEKEY");

        ChetongHttpClient httpClient = new ChetongHttpClient(env, appkey,
                privateKey, "utf-8", (System.currentTimeMillis() / 1000l) + "", 10*1000);
        // 请求体
        CommonRequestVo requestVo = new CommonRequestVo();
        //调用服务名：“dhDockingService.saveReportInformation”
        requestVo.setServiceName(type);
        logger.info(urlStr+"ServiceName---------------------------"+type);
        RequestObject requestObject = new RequestObject();
        //请求参数，xml需转换成String
        requestObject.setParameter(requestXML);
        //logger.info(urlStr+"提交xmlToSend---------------------------"+requestXML);
        requestVo.setParams(requestObject);
        String xmlReturn = "";

        // 发送请求与返回结果
        CommonResultVO responseVo = new CommonResultVO("445","返回数据为null");
        try {
            responseVo = httpClient.call(requestVo,urlStr);
            logger.info(urlStr+"返回状态码---------------------------"+responseVo.getResultCode()+responseVo.getResultMsg());
            xmlReturn = responseVo.getResultObject().toString();
            xmlReturn = new String(xmlReturn.getBytes(), "UTF-8");
            //logger.info(urlStr+"返回---------------------------"+xmlReturn);
        } catch (Exception e) {
            xmlReturn = responseVo.getResultCode()+responseVo.getResultMsg();
            e.printStackTrace();
        }
        return xmlReturn;
    }
	
	public static String requestPlatformByCode(String requestXML,String urlStr,int seconds,String sign,String code) throws Exception {
        long t1 = System.currentTimeMillis();
        String responseXml="";
          StringBuffer buffer = new StringBuffer();    
            try {    
             
                URL url = new URL(urlStr);
                HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();
                httpUrlConn.setDoOutput(true);    
                httpUrlConn.setDoInput(true);    
                httpUrlConn.setUseCaches(false);    
                // 设置请求方式（GET/POST）    
                httpUrlConn.setRequestMethod("POST"); 
                httpUrlConn.setConnectTimeout(seconds * 1000);
                httpUrlConn.setRequestProperty("Content-type","application/xml;charset="+code);
                if(sign.contains("MTA")){
                	httpUrlConn.setRequestProperty("X-User-Mobile","18800001111");
                    httpUrlConn.setRequestProperty("X-User-Token","123456");
                }
                
                httpUrlConn.connect();    
        
                String outputStr =requestXML;
                            
                OutputStream outputStream = httpUrlConn.getOutputStream();                  
                // 当有数据需要提交时    
                if (null != outputStr) {    
                    // 注意编码格式，防止中文乱码    outputStream.write
                    outputStream.write(outputStr.getBytes(code));    
                }    
        
                // 将返回的输入流转换成字符串    
                InputStream inputStream = httpUrlConn.getInputStream();    
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, code);    
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);    
                
                String str = null;    
                while ((str = bufferedReader.readLine()) != null) {
                    
                    buffer.append(str);    
                } 
                if (buffer.length() < 1) {
                    throw new Exception("返回数据失败");
                }
                bufferedReader.close();    
                inputStreamReader.close();    
                // 释放资源  
                outputStream.flush();
                outputStream.close();
                inputStream.close();    
                inputStream = null;    
                httpUrlConn.disconnect(); 
                responseXml=buffer.toString();
                
            } catch (ConnectException ce) {
                throw new Exception("连接失败，请稍候再试", ce);
            } catch (Exception e) {
                e.printStackTrace();
                throw new Exception("读取返回数据失败", e);
                
            } finally {
                //logger.warn("接口({})调用耗时{}ms", urlStr, System.currentTimeMillis() - t1);
            }    
            return responseXml;
    }
}
