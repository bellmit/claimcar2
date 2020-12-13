package ins.sino.claimcar.claimcarYJ.Util.vo;


import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.SpringProperties;

import com.alibaba.dubbo.common.utils.StringUtils;

public class HttpClientJsonUtil {
    private static Logger logger = LoggerFactory.getLogger(HttpClientJsonUtil.class);

    public static String postUtf(String url,String requestContent,String charset) throws ClientProtocolException, IOException {
    	logger.info("请求报文=============="+requestContent);
    	String responContent = "";
    	HttpPost httpPost = new HttpPost(url);
        if(requestContent!=null&& !requestContent.trim().equals("")){
             StringEntity requestEntity = new StringEntity(requestContent,ContentType.create("application/json",charset));
             httpPost.setEntity(requestEntity);
        }
        DefaultHttpClient httpClient = new DefaultHttpClient();
        //获取超时时间
		String seconds = SpringProperties.getProperty("HTTP_TIMEOUT");
		if(StringUtils.isBlank(seconds)){
			seconds = "20";
		}
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,Integer.valueOf(seconds) * 1000);//连接时间
        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,Integer.valueOf(seconds) * 1000);//设置等待数据超时时间

        HttpResponse response = httpClient.execute(httpPost);
        HttpEntity httpEntity = response.getEntity();
        responContent = EntityUtils.toString(httpEntity);
        responContent = new String(responContent.getBytes(charset), charset);
		logger.info("返回报文=============="+responContent);
		return responContent;
    }
}