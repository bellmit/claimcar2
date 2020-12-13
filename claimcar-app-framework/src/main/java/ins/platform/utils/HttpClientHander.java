/******************************************************************************
* CREATETIME : 2016年7月22日 上午11:27:20
******************************************************************************/
package ins.platform.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.SpringProperties;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 用json方式请求远程数据
 * @author ★LiuPing
 * @CreateTime 2016年7月22日
 */
public class HttpClientHander {

	private static Logger logger = LoggerFactory.getLogger(HttpClientHander.class);
	private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

	/**
	 * 将jsonMap放到JSONData请求，同时返回JSONObject
	 * @param httpUrl
	 * @param paramMap
	 * @param jsonMap
	 * @return
	 * @modified: ☆LiuPing(2016年7月24日 ): <br>
	 */
	public JSONObject doPostJSON(String httpUrl,Map<String,String> paramMap,Map<String,Object> jsonMap) {

		JSONObject responseJsonObj = new JSONObject();
		try{
			if(jsonMap!=null&&jsonMap.size()>0){
				JSONObject jsonObj = new JSONObject(jsonMap);
				String jsonData = jsonObj.toJSONString();
				jsonData = Base64EncodedUtil.encode(jsonData);
				if(paramMap==null) paramMap = new HashMap<String,String>();
				paramMap.put("JSONData",jsonData);
			}
			String responseData = doPost(httpUrl,paramMap);
			responseJsonObj = toJSON(responseData);
		}catch(IOException e){
			logger.error("请求url错误"+httpUrl,e);
			responseJsonObj.put("status","500");
			responseJsonObj.put("message",e.getMessage());
		}
		return responseJsonObj;
	}

	/**
	 * 发送HTTP post请求，将参数放到 paramMap里面
	 * @param httpUrl
	 * @param paramMap
	 * @return
	 * @throws IOException
	 * @modified: ☆LiuPing(2016年7月24日 ): <br>
	 */
	public String doPost(String httpUrl,Map<String,String> paramMap) throws IOException {
		String postData = URLEncodedUtils.format(paramMap,DEFAULT_CHARSET);
		String responseData = doPost(httpUrl,postData);
		return responseData;
	}

	public String doPost(String httpUrl,String postData) throws IOException {
		System.out.println(httpUrl+"?"+postData);
		InputStream inputStream = null;
		HttpURLConnection urlConnection = null;
		String responseData = null;
		try{
			URL url = new URL(httpUrl);
			//获取超时时间
			String seconds = SpringProperties.getProperty("HTTP_TIMEOUT");
			if(StringUtils.isBlank(seconds)){
				seconds = "20";
			}
			String readTime="30";
			urlConnection = (HttpURLConnection)url.openConnection();

			/* optional request header */
			urlConnection.setDoOutput(true);
			urlConnection.setDoInput(true);
			urlConnection.setUseCaches(false);
			urlConnection.setInstanceFollowRedirects(true);
			urlConnection.setRequestMethod("POST");
			urlConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded; charset="+DEFAULT_CHARSET.name());
			urlConnection.setConnectTimeout(Integer.valueOf(seconds) * 1000);
			urlConnection.setReadTimeout(Integer.valueOf(readTime) * 1000);

			// out put data
			DataOutputStream dataOut = new DataOutputStream(urlConnection.getOutputStream());
			dataOut.write(postData.getBytes(DEFAULT_CHARSET));
			// dataOut.writeBytes(jsonString);
			dataOut.flush();
			dataOut.close();
			// read response
			int statusCode = urlConnection.getResponseCode();
			if(statusCode==200){
				inputStream = new BufferedInputStream(urlConnection.getInputStream());
				responseData = inputStreamToString(inputStream);
			}
		}catch(IOException e){
			logger.error("请求url错误"+httpUrl,e);
			throw e;
		}
		finally{
			if(inputStream!=null){
				try{
					inputStream.close();
				}catch(IOException e){
					e.printStackTrace();
				}
			}
			if(urlConnection!=null){
				urlConnection.disconnect();
			}
		}
		return responseData;
	}
	
	public String reqPost(String url,String requestXML)  throws Exception {
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
				throw new Exception("连接失败，请稍候再试", ex);
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
					throw new Exception("接口返回数据失败");
				}
			} catch (IOException e) {
				e.printStackTrace();
				throw new Exception("读取接口返回数据失败", e);
			} finally {
				if (reader != null) {
					reader.close();
				}
			}
			return buffer.toString();
		} finally {
			logger.warn("接口调用耗时{}ms", url, System.currentTimeMillis() - t1);
		}
	}

	public String inputStreamToString(InputStream inputStream) throws IOException {
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream,DEFAULT_CHARSET);
		StringBuffer resultBuffer = new StringBuffer();
		BufferedReader reader = new BufferedReader(inputStreamReader);
		String line = null;
		while(( line = reader.readLine() )!=null){
			resultBuffer.append(line);
		}
		return resultBuffer.toString();
	}

	public JSONObject toJSON(String dataString) {
		JSONObject responseJsonObj = JSON.parseObject(dataString,JSONObject.class);
		return responseJsonObj;
	}

}
