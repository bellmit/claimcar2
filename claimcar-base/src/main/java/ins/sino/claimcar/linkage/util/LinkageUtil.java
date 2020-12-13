/******************************************************************************
* CREATETIME : 2016年10月17日 下午6:05:34
******************************************************************************/
package ins.sino.claimcar.linkage.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.DocumentResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.SpringProperties;

/**
 * <pre>警保查询</pre>
 * @author ★Luwei
 */
public class LinkageUtil {
	
	private static Logger logger = LoggerFactory.getLogger(LinkageUtil.class);

	public static String requestLinkage(String url) throws Exception {
		long t1 = System.currentTimeMillis();
		//获取超时时间
		String seconds = SpringProperties.getProperty("HTTP_TIMEOUT");
		if(StringUtils.isBlank(seconds)){
			seconds = "20";
		}
		try {
			BufferedReader reader = null;
			HttpURLConnection connection = null;
			StringBuffer buffer = new StringBuffer();
			URL urlAdd = new URL(url);
//			URL urlAdd = new URL(URLEncoder.encode(url), "GBK");
			try {
				connection = (HttpURLConnection) urlAdd.openConnection();
				connection.setDoInput(true);
				connection.setDoOutput(true);
				connection.setRequestMethod("POST");
				// post方式不能使用缓存
				connection.setUseCaches(false);
				// 配置本次连接的Content-Type，配置为text/xml
				connection.setRequestProperty("Content-Type",
						"application/x-www-form-urlencoded;charset=utf-8");
				// 维持长连接
				connection.setRequestProperty("Connection", "Keep-Alive");
				connection.setConnectTimeout(Integer.valueOf(seconds) * 1000);
				connection.setReadTimeout(Integer.valueOf(seconds) * 1000);
				connection.setAllowUserInteraction(true);
				connection.connect();
			} catch (Exception ex) {
				throw new Exception("与警保联动连接失败，请稍候再试", ex);
			}
			try {
//				OutputStream outputStream = connection.getOutputStream();
//				DataOutputStream out = new DataOutputStream(outputStream);
//				out.write(9);
//				out.flush();
//				out.close();
				//connection.getInputStream();
				//new InputStreamReader(connection.getInputStream());
				
				reader = new BufferedReader(new InputStreamReader(
						connection.getInputStream(), "UTF-8"));
				String strLine = "";
				while ((strLine = reader.readLine()) != null) {
					buffer.append(strLine);
				}
				if (buffer.length() < 1) {
					throw new Exception("警保联动返回数据失败");
				}
			} catch (IOException e) {
				throw new Exception("读取警保联动返回数据失败", e);
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
	
	public static String toXml(Object headVo,Object bodyVo) throws JAXBException {
		Document packDoc = DocumentHelper.createDocument();
		Element rootElm = packDoc.addElement("Packet");
		packDoc.setXMLEncoding("GBK");
		rootElm.addAttribute("type","REQUEST");
		rootElm.addAttribute("version","1.0");

		DocumentResult headResult = new DocumentResult();
		JAXBContext headJaxbContext = JAXBContext.newInstance(headVo.getClass());
		Marshaller hreadJaxbMarshaller = headJaxbContext.createMarshaller();
		// output pretty printed
		// hreadJaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,true);
		// hreadJaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING,"GBK");
		hreadJaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT,true);// 是否省略xm头声明信息
		hreadJaxbMarshaller.marshal(headVo,headResult);
		Document headDoc = headResult.getDocument();

		DocumentResult bodyResult = new DocumentResult();
		JAXBContext jaxbContext = JAXBContext.newInstance(bodyVo.getClass());
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.marshal(bodyVo,bodyResult);
		Document bodyDoc = bodyResult.getDocument();

		rootElm.add(headDoc.getRootElement());
		rootElm.add(bodyDoc.getRootElement());

		String xml = packDoc.asXML();
		return xml;
	}
	 public static String loadJSON(String url,String param) {
    	 PrintWriter out = null;
         BufferedReader in = null;
        
         String result = "";
       //获取超时时间
 		String seconds = SpringProperties.getProperty("HTTP_TIMEOUT");
 		if(StringUtils.isBlank(seconds)){
 			seconds = "20";
 		}
         try {
             URL realUrl = new URL(url);
             // 打开和URL之间的连接
             URLConnection conn = realUrl.openConnection();
             // 设置通用的请求属性
             conn.setRequestProperty("accept", "*/*");
             conn.setRequestProperty("connection", "Keep-Alive");
             conn.setRequestProperty("user-agent",
                     "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
             // 发送POST请求必须设置如下两行
             conn.setDoOutput(true);
             conn.setDoInput(true);
             //设置超时时间
             conn.setConnectTimeout(Integer.valueOf(seconds) * 1000);
             conn.setReadTimeout(Integer.valueOf(seconds) * 1000);
             // 获取URLConnection对象对应的输出流
             out = new PrintWriter(conn.getOutputStream());
             // 发送请求参数
             out.print(param);
             // flush输出流的缓冲
             out.flush();
             // 定义BufferedReader输入流来读取URL的响应
             in = new BufferedReader(
                     new InputStreamReader(conn.getInputStream(),"UTF-8"));
             String line;
             while ((line = in.readLine()) != null) {
                 result += line;
             }
         } catch (Exception e) {
   
        	 logger.error("发送 POST 请求出现异常！", e.getMessage());
         }
         //使用finally块来关闭输出流、输入流
         finally{
             try{
                 if(out!=null){
                     out.close();
                 }
                 if(in!=null){
                     in.close();
                 }
             }
             catch(IOException ex){
                 ex.printStackTrace();
             }
         }
         return result;
     }    
}
