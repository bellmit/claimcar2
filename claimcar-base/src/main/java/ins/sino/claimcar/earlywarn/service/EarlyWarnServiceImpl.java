package ins.sino.claimcar.earlywarn.service;


import ins.sino.claimcar.trafficplatform.service.EarlyWarnService;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.ws.rs.Path;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.SpringProperties;

import com.alibaba.dubbo.config.annotation.Service;

@Service(protocol = {"dubbo"}, validation = "true" , registry = {"default"})
@Path(value = "earlyWarnService")
public class EarlyWarnServiceImpl implements EarlyWarnService{

	private static Logger logger = LoggerFactory.getLogger(EarlyWarnServiceImpl.class);
	
	@Override
	public String requestSDEW(String requestXML,String urlStr) throws Exception {
		long t1 = System.currentTimeMillis();
		try {
			BufferedReader reader = null;
			HttpURLConnection connection = null;
			StringBuffer buffer = new StringBuffer();
			URL url = new URL(urlStr);
			//获取超时时间
			String seconds = SpringProperties.getProperty("HTTP_TIMEOUT");
			if(StringUtils.isBlank(seconds)){
				seconds = "20";
			}
			try {
				connection = (HttpURLConnection)url.openConnection();
				connection.setDoInput(true);
				connection.setDoOutput(true);
				connection.setRequestMethod("POST");
				// post方式不能使用缓存
				connection.setUseCaches(false);
				// 配置本次连接的Content-Type，配置为text/xml
				connection.setRequestProperty("Content-Type","text/xml;charset=GBK");
				// 维持长连接
				connection.setRequestProperty("Connection", "Keep-Alive");
				connection.setConnectTimeout(Integer.valueOf(seconds) * 1000);
				connection.setReadTimeout(Integer.valueOf(seconds) * 1000);
				connection.setAllowUserInteraction(true);
				connection.connect();
			} catch (Exception ex) {
				throw new Exception("与平台连接失败，请稍候再试", ex);
			}

			try {
				OutputStream outputStream = connection.getOutputStream();
				DataOutputStream out = new DataOutputStream(outputStream);
				out.write(requestXML.getBytes("GBK"));
				out.flush();
				out.close();
				reader = new BufferedReader(new InputStreamReader(connection.getInputStream(),"GBK"));
				String strLine = "";
				while ((strLine = reader.readLine()) != null) {
					buffer.append(strLine);
				}
				if (buffer.length() < 1) {
					throw new Exception("平台返回数据失败");
				}
			} catch (IOException e) {
				throw new Exception("读取平台返回数据失败", e);
			} finally {
				if (reader != null) {
					reader.close();
				}
			}
			return buffer.toString();
		} finally {
			logger.warn("平台({})调用耗时{}ms",urlStr,System.currentTimeMillis()-t1);
		}
	}

}
