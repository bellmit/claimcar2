/******************************************************************************
 * CREATETIME : 2015年12月29日 下午12:39:56
 ******************************************************************************/
package ins.sino.claimcar.image.service.impl;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.Path;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.SpringProperties;

import com.alibaba.dubbo.config.annotation.Service;
import com.sunyard.insurance.encode.client.EncodeAccessParam;

import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.image.util.XstreamFactory;
import ins.sino.claimcar.sunyardimage.service.ImagesDownLoadService;
import ins.sino.claimcar.sunyardimage.vo.common.BaseDataVo;
import ins.sino.claimcar.sunyardimage.vo.common.ImageBaseRootVo;
import ins.sino.claimcar.sunyardimage.vo.request.ReqBatchNumVo;
import ins.sino.claimcar.sunyardimage.vo.request.ReqGetMetaDataNumVo;
import ins.sino.claimcar.sunyardimage.vo.request.ReqImageNumBaseRootVo;
import ins.sino.claimcar.sunyardimage.vo.response.NodeNumVo;
import ins.sino.claimcar.sunyardimage.vo.response.ResNumRootVo;
import ins.sino.claimcar.sunyardimage.vo.response.ResRootVo;


/**
 * 影像资源请求接口
 * <pre></pre>
 * @author ★zhujunde
 */
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"}, timeout = 600*1000)
@Path("imagesDownLoadService")
public class ImagesDownLoadServiceImpl implements ImagesDownLoadService {
    private Logger logger = LoggerFactory.getLogger(ImagesDownLoadServiceImpl.class);
  
	@Override
	public ResRootVo imagesDownLoad(ImageBaseRootVo vo,String url,String requestType) {
		String xmlToSend = XstreamFactory.objToXmlUtf(vo);
        logger.info("请求报文xmlToSend---------------------------"+xmlToSend);
		//准备参数
		String result="";
		PostMethod postMethod = null;
		HttpClient httpClient = new HttpClient();
		try{
			//创建post方法实例，并填入请求URL
			postMethod = new PostMethod(url);
			
			// 填入参数
			//指定编码格式
			postMethod.getParams().setContentCharset("utf-8");  
			//加入xml与请求时间、密钥
			String key = SpringProperties.getProperty("YX_DownLoadKey");
			String param = EncodeAccessParam.getEncodeParam("format=xml&code="+CodeConstants.YXDOWNLOADCODE+"&xml="+xmlToSend, 20*1000, key);
			postMethod.setParameter("data", param);
			//来源路径设置未SunECM
			String id = SpringProperties.getProperty("YX_DownLoadID");
			postMethod.setRequestHeader("Referer", id);  
			// httpClient实例执行postMethod
			int statusCode = httpClient.executeMethod(postMethod);
			System.out.println(statusCode);
			if(statusCode==HttpStatus.SC_OK){
			    byte[] bodydata = postMethod.getResponseBody();
			    //取得返回值
			    result = new String(bodydata);
			}else{
			}
		}catch (HttpException e) {
			//协议发生异常，URL不合法请检查URL！
			logger.info("影像资源请求接口=======",e);
		} catch (IOException e) {
			//请检查网络是否通畅，检查网线是否插好！
			logger.info("影像资源请求接口=======",e);
		} catch(Exception e){
			logger.info("影像资源请求接口=======",e);
		}finally {
			//释放连接，先释放post请求，再释放httpclient
			if (postMethod != null) {
				try {
					postMethod.releaseConnection();
				} catch (Exception e) {
					logger.info("影像资源请求接口释放post请求=======",e);
				}
			}
			if (httpClient != null) {
				try {
					((SimpleHttpConnectionManager) httpClient.getHttpConnectionManager()).shutdown();
				} catch (Exception e) {
					logger.info("影像资源请求接口释放httpclient=======",e);
				}
				httpClient = null;
			}
		}
		logger.info("影像资源请求接口返回报文======="+result);
		ResRootVo resVo = XstreamFactory.xmlToObjByIgnore(result, ResRootVo.class);
		
		return resVo;
		
	}

	@Override
	public ResNumRootVo getReqImageNum(SysUserVo user, String role, String bussNo,String assessorId,String url,String appName,String appCode) {
			//准备参数
			ReqImageNumBaseRootVo vo = new ReqImageNumBaseRootVo();
			BaseDataVo baseDataVo = new BaseDataVo();
			
			baseDataVo.setComCode(user.getComCode());
			baseDataVo.setOrgCode(user.getComCode());
			baseDataVo.setOrgName(user.getUserName());
			baseDataVo.setRoleCode(role);
			baseDataVo.setUserCode(user.getUserCode());
			baseDataVo.setUserName(user.getUserName());
			vo.setBaseDataVo(baseDataVo);
			ReqGetMetaDataNumVo metaDataVo = new  ReqGetMetaDataNumVo();
			ReqBatchNumVo batchVo = new ReqBatchNumVo();
			batchVo.setAppCode(appCode);
			batchVo.setAppName(appName);
			
			batchVo.setBusiNo(bussNo);
			
			batchVo.setCreateUser(user.getUserCode());
			metaDataVo.setReqBatchNumVo(batchVo);
			vo.setMetaDataVo(metaDataVo);
			String xmlToSend = XstreamFactory.objToXmlUtf(vo);
			logger.info("xmlToSend==============="+xmlToSend);
			StringBuilder sb = new StringBuilder("xml="+xmlToSend+"&code="+CodeConstants.YXSUMCODE+"&format=xml");
			String result="";
			PostMethod postMethod = null;
			HttpClient httpClient = null;
			try{
				String key = SpringProperties.getProperty("YX_DownLoadKey");
			    String param = EncodeAccessParam.getEncodeParam(sb.toString(), 1000, key);
				postMethod = new PostMethod(url);
				// 设置格式
				postMethod.getParams().setContentCharset("UTF-8");
				// 请求参数n
				postMethod.setParameter("data", param);
				//String id = SpringProperties.getProperty("YX_DownLoadID");
	            postMethod.setRequestHeader("Referer", "claimcar");
				httpClient = new HttpClient();
				httpClient.getParams().setSoTimeout(600000);
				// 执行postMethod
				int statusCode = httpClient.executeMethod(postMethod);
				if(statusCode==HttpStatus.SC_OK){
				    byte[] bodydata = postMethod.getResponseBody();
				    //取得返回值
				    result = new String(bodydata, "UTF-8");
				}else{
				}
			}catch (HttpException e) {
				//协议发生异常，URL不合法请检查URL！
				logger.info("协议发生异常，URL不合法请检查URL",e);
			} catch (IOException e) {
				//请检查网络是否通畅，检查网线是否插好！
				logger.info("请检查网络是否通畅，检查网线是否插好",e);
			} catch(Exception e){
				logger.info("影像资料统计接口报错：",e);
			}finally {
				if (postMethod != null) {
					try {
						postMethod.releaseConnection();
					} catch (Exception e) {
						logger.info("影像资料统计接口releaseConnection报错：",e);
					}
				}
				if (httpClient != null) {
					try {
						((SimpleHttpConnectionManager) httpClient.getHttpConnectionManager()).shutdown();
					} catch (Exception e) {
						logger.info("影像资料统计接口shutdown报错：",e);
					}
					httpClient = null;
				}
			}
			logger.info("影像资源请求接口返回报文======="+result);
			ResNumRootVo resVo =null;
			if(StringUtils.isNotBlank(result) && result.contains("<RESPONSE_CODE>200</RESPONSE_CODE>")) {
				resVo=XstreamFactory.xmlToObjByIgnore(result, ResNumRootVo.class);
			}
			
			/*Integer num = 0;
			if(resVo != null && resVo.getResReturnDataVo() != null && resVo.getResReturnDataVo().getNodeNumVos() != null
					&& resVo.getResReturnDataVo().getNodeNumVos().size() > 0){
				List<NodeNumVo> nodeNumVos = resVo.getResReturnDataVo().getNodeNumVos();
				
				for(NodeNumVo nodeNumVo : nodeNumVos){
					num += Integer.valueOf(nodeNumVo.getNum());
				}
			}*/
			return resVo;
}

}
