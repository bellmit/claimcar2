package ins.sino.claimcar.platform.service;

import ins.sino.claimcar.constant.Risk;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.SpringProperties;
import org.springframework.stereotype.Service;

/**
 * 发送请求服务实例
 * 
 */
@Service("platformService")
public class PlatformService {

	Logger logger = LoggerFactory.getLogger(getClass());

	public String requestPlatform(String requestXML, String comCode,
			String riskCode) throws Exception {
		// 通过comcode，riskcode取平台地址
		String url = null;
		if (Risk.isDQZ(riskCode) || comCode.startsWith("22")) {
			url = SpringProperties.getProperty("CI_" + comCode.substring(0, 2)
					+ "_URL");
		} else {
			url = SpringProperties.getProperty("BI_" + comCode.substring(0, 2)
					+ "_URL");
		}
		if (url == null || url.trim().length() == 0) {
			throw new Exception("机构【" + comCode + "】险种【" + riskCode
					+ "】平台URL不存在，请联系管理员配置");
		}

		return requestPlatform(requestXML, url);
	}

	public String requestCommission(String requestXML, String comCode)
			throws Exception {
		String url = SpringProperties.getProperty("COMMISSION_" + comCode.substring(0, 2)
				+ "_URL");
		if (url == null || url.trim().length() == 0) {
			throw new Exception("机构【" + comCode + "】平台URL不存在，请联系管理员配置");
		}
		return requestPlatform(requestXML, url);
	}

	public String requestPlatform(String requestXML, String url)
			throws Exception {
		return requestPlatform(requestXML,url,0);
	}

	public String requestCompare(String requestXML, String comCode)
			throws Exception {
		String url = SpringProperties.getProperty("COMPARE_" + comCode.substring(0, 2)
				+ "_URL");
		if (url == null || url.trim().length() == 0) {
			throw new Exception("机构【" + comCode + "】平台URL不存在，请联系管理员配置");
		}
		return requestPlatform(requestXML, url);
	}

	public String requestPlatform(String requestXML,String url,int seconds) throws Exception {
		long t1 = System.currentTimeMillis();
		try {
			BufferedReader reader = null;
			HttpURLConnection connection = null;
			StringBuffer buffer = new StringBuffer();
			URL urlAdd = new URL(url);
			try {
				connection = (HttpURLConnection) urlAdd.openConnection();
				connection.setDoInput(true);
				connection.setDoOutput(true);
				connection.setRequestMethod("POST");
				// post方式不能使用缓存
				connection.setUseCaches(false);
				// 配置本次连接的Content-Type，配置为text/xml
				connection.setRequestProperty("Content-Type",
						"text/xml;charset=GBK");
				// 维持长连接
				connection.setRequestProperty("Connection", "Keep-Alive");
				connection.setConnectTimeout(seconds * 1000);
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
				reader = new BufferedReader(new InputStreamReader(
						connection.getInputStream(), "GBK"));
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
			logger.warn("平台({})调用耗时{}ms", url, System.currentTimeMillis() - t1);
		}
	}

}
