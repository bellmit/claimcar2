package ins.sino.claimcar.carplatform.controller;

import ins.platform.saa.util.CodeConstants;
import ins.sino.claimcar.carplatform.constant.PlatfromType;
import ins.sino.claimcar.carplatform.constant.RequestType;
import ins.sino.claimcar.carplatform.util.PlatformLogUtil;
import ins.sino.claimcar.carplatform.util.PlatformMarshaller;
import ins.sino.claimcar.carplatform.util.PlatformUnMarshaller;
import ins.sino.claimcar.carplatform.util.XmlUtil;
import ins.sino.claimcar.carplatform.vo.CiClaimPlatformLogVo;
import ins.sino.claimcar.carplatform.vo.CiClaimPlatformTaskVo;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.SpringProperties;

/**
 * 平台交互控制中心
 */
public abstract class PlatformController {

	private static Logger logger = LoggerFactory.getLogger(PlatformController.class);

	private String comCode = null;
	private String areaCode = null;
	private RequestType requestType;
	private PlatformUnMarshaller umMarsh = null;

	public PlatformController(String comCode,RequestType requestType){
		this.comCode = comCode;
		this.requestType = requestType;
	}
	
	public PlatformController(String comCode,String areaCode,RequestType requestType){
		this.comCode = comCode;
		this.areaCode = areaCode;
		this.requestType = requestType;
	}

	protected abstract Object getHeadVo(RequestType requestType,String... params);

	protected abstract String getUrl(String comCode,RequestType requestType);

	/**
	 * 【【平安联盟对接上传平台方法】】
	 * @param comCode
	 * @return
	 */
	protected abstract Map<String,String> getUrlAndUserAndPassword(String comCode);

	/**
	 * 初始化平台日志Vo，根据不同平台，得到ClaimSeqNo，BussNo
	 * @param comCode
	 * @param requestType
	 * @param bodyVo
	 * @return
	 * @modified: ☆LiuPing(2016年6月24日 ): <br>
	 */
	protected abstract CiClaimPlatformLogVo initPlatformLogVo(RequestType requestType,Object bodyVo);

	/**
	 * 【【平安联盟对接上传平台方法】】
	 * 初始化平台日志Vo，根据不同平台，得到ClaimSeqNo，BussNo(平安联盟对接)
	 * @param requestType
	 * @param reqDataMap
	 * @return
	 * @modified: ☆LiuPing(2016年6月24日 ): <br>
	 */
	protected abstract CiClaimPlatformLogVo initPlatformLogVoForPingAn(RequestType requestType,Map<String,String> reqDataMap);

	/**
	 * 发送平台
	 * @param bodyVo
	 * @throws Exception
	 * @modified: ☆LiuPing(2016年4月5日 ): <br>
	 */
	public CiClaimPlatformLogVo callPlatform(Object bodyVo) {

		String url = getUrl(comCode,requestType);
		Object headVo = null ;
		
		PlatfromType platfromType = requestType.getPlatformType();
		if(platfromType==PlatfromType.CA || platfromType ==PlatfromType.HS){
			headVo = getHeadVo(requestType,comCode,areaCode);
		}else{
			headVo = getHeadVo(requestType,comCode);
		}
		
		CiClaimPlatformLogVo logVo = initPlatformLogVo(requestType,bodyVo);
		logVo.setComCode(comCode);
		logVo.setAreaCode(areaCode);
		Date currentDate = new Date();
		logger.info("\n送平台 start...业务号："+logVo.getBussNo()+";类型："+logVo.getRequestType()+";类型名称："+logVo.getRequestName());
		
		try {
			PlatformMarshaller marsh = new PlatformMarshaller(headVo, bodyVo);
			String requestXML = marsh.toXml();
			logVo.setRequestXml(requestXML);
			logVo.setRequestTime(new Date());
			logger.info("\n业务号：" + logVo.getBussNo() + ";" + requestType.getName() + ",请求平台报文requestXML=： \n" + requestXML);
			String seconds = SpringProperties.getProperty("HTTP_TIMEOUT");
			if (StringUtils.isBlank(seconds)) {
				seconds = "20";
			}
			String responseXml = requestPlatform(requestXML, url, Integer.valueOf(seconds));
			logger.info("\n业务号：" + logVo.getBussNo() + ";" + requestType.getName() + ",平台返回报文responseXml=： \n" + responseXml);
			logger.info("送平台 end, 耗时 " + (System.currentTimeMillis() - currentDate.getTime()) + "毫秒，业务号："
					+ logVo.getBussNo() + ";类型：" + logVo.getRequestType() + "类型名称：" + logVo.getRequestName());

			logVo.setResponseXml(responseXml);
			logVo.setResponseTime(new Date());

			logVo.setStatus("1");// 成功
			// 解析结果
			umMarsh = new PlatformUnMarshaller(requestType);
			umMarsh.parseXml(responseXml);

			String errorCode = umMarsh.getErrorCode();
			if(StringUtils.isNotBlank(errorCode)){
				logVo.setStatus("0");// 失败
				logVo.setErrorCode(errorCode);
				logVo.setErrorMessage(umMarsh.getErrorMessage());
//				throw new IllegalArgumentException("平台返回异常信息:"+logVo.getErrorMessage());
			}
			
			//保存报案的理赔编码
			String claimSeqNo = umMarsh.getClaimSeqNo();
			if (StringUtils.isNotBlank(claimSeqNo)) {
				logVo.setClaimSeqNo(claimSeqNo);
			}
			//保存赔案结案校验码
			logVo.setRemark(umMarsh.getClaimConfirmCode());
			
		}catch(IllegalArgumentException e){
			throw e;
		}catch(Exception e){
			logger.info("平台交互异常：RequestType="+requestType.name()+" 报案号："+logVo.getBussNo()+",error: ", e);
			logVo.setStatus("0");// 失败
			logVo.setErrorMessage("系统异常"+e.getMessage());
		} finally {
			logger.info("平台日志开始保存-------------------------------------------------------finally开始：》");
			// 上海代位求偿查询平台失败，不存日志
			if (logVo.getComCode().startsWith("22") && "0".equals(logVo.getStatus())
					&& RequestType.SubrogationClaim_SH.getCode().equals(logVo.getRequestType())) {
				logger.info("平台日志开始保存-------------------------------------------------------上海开始：》");
			} else {
				try {
					logger.info("平台日志开始保存-------------------------------------------------------》：RequestType="+requestType.name()+" 报案号："+logVo.getBussNo());
					PlatformLogUtil.saveLog(logVo);
					logger.info("平台日志保存完毕-------------------------------------------------------》：RequestType="+requestType.name()+" 报案号："+logVo.getBussNo()+" 保存状态 ："+logVo.getStatus());
				}catch(Exception e) {
					logger.info("平台日志保存失败：RequestType="+requestType.name()+" 报案号："+logVo.getBussNo()+",error", e);
				}
				
			}
		}
		return logVo;
	}

	/**
	 * 发送平台(平安联盟对接)
	 * @param reqData
	 * @throws Exception
	 * @modified: ☆LiuPing(2016年4月5日 ): <br>
	 */
	public CiClaimPlatformLogVo callPlatformForPingAn(String reqData, Map<String,String> reqDataMap) throws DocumentException {
		//获取请求URL和用户名及密码
		Map<String,String> headMap = getUrlAndUserAndPassword(comCode);
		String url = headMap.get("url");
		String user = headMap.get("user");
		String password = headMap.get("password");
		//替换报文默认用户密码
		String defaultUser = SpringProperties.getProperty("pingan.defaultUser");
		String defaultPwd = SpringProperties.getProperty("pingan.defaultPwd");
		String requestXML = reqData.replace(defaultUser,user).replace(defaultPwd, password);//替换默认用户密码
		CiClaimPlatformLogVo logVo = initPlatformLogVoForPingAn(requestType,reqDataMap);
		logVo.setComCode(comCode);
		logVo.setAreaCode(areaCode);
		Date currentDate = new Date();
		logger.info("\n送平台 start...业务号："+logVo.getBussNo()+";类型："+logVo.getRequestType()+";类型名称："+logVo.getRequestName());

		try {
			logVo.setRequestXml(requestXML);
			logVo.setRequestTime(new Date());
			logger.info("\n业务号：" + logVo.getBussNo() + ";" + requestType.getName() + ",请求平台报文requestXML=： \n" + requestXML);
			String seconds = SpringProperties.getProperty("HTTP_TIMEOUT");
			if (StringUtils.isBlank(seconds)) {
				seconds = "20";
			}
			String responseXml = requestPlatform(requestXML, url, Integer.valueOf(seconds));
			logger.info("\n业务号：" + logVo.getBussNo() + ";" + requestType.getName() + ",平台返回报文responseXml=： \n" + responseXml);
			logger.info("送平台 end, 耗时 " + (System.currentTimeMillis() - currentDate.getTime()) + "毫秒，业务号："
					+ logVo.getBussNo() + ";类型：" + logVo.getRequestType() + "类型名称：" + logVo.getRequestName());

			logVo.setResponseXml(responseXml);
			logVo.setResponseTime(new Date());

			logVo.setStatus("1");// 成功
			// 解析结果
			umMarsh = new PlatformUnMarshaller(requestType);
			umMarsh.parseXml(responseXml);

			String errorCode = umMarsh.getErrorCode();
			if(StringUtils.isNotBlank(errorCode)){
				logVo.setStatus("0");// 失败
				logVo.setErrorCode(errorCode);
				logVo.setErrorMessage(umMarsh.getErrorMessage());
//				throw new IllegalArgumentException("平台返回异常信息:"+logVo.getErrorMessage());
			}

			//保存报案的理赔编码
			String claimSeqNo = umMarsh.getClaimSeqNo();
			if (StringUtils.isNotBlank(claimSeqNo)) {
				logVo.setClaimSeqNo(claimSeqNo);
			}
			//保存赔案结案校验码
			logVo.setRemark(umMarsh.getClaimConfirmCode());

		}catch(IllegalArgumentException e){
			throw e;
		}catch(Exception e){
			logger.info("平台交互异常：RequestType="+requestType.name()+" 报案号："+logVo.getBussNo()+",error: ", e);
			logVo.setStatus("0");// 失败
			logVo.setErrorMessage("系统异常"+e.getMessage());
		} finally {
			// 上海代位求偿查询平台失败，不存日志
			if (logVo.getComCode().startsWith("22") && "0".equals(logVo.getStatus())
					&& RequestType.SubrogationClaim_SH.getCode().equals(logVo.getRequestType())) {

			} else {
				PlatformLogUtil.saveLog(logVo);
			}
		}
		return logVo;
	}
	
	/**
	 * 发送平台，更新platformTask表
	 * @param bodyVo
	 * @param platformTaskVo
	 * @return
	 */
	public CiClaimPlatformLogVo callPlatform(Object bodyVo,CiClaimPlatformTaskVo platformTaskVo) {

		String url = getUrl(comCode,requestType);
		Object headVo = null ;
		
		PlatfromType platfromType = requestType.getPlatformType();
		if(platfromType==PlatfromType.CA || platfromType ==PlatfromType.HS){
			headVo = getHeadVo(requestType,comCode,areaCode);
		}else{
			headVo = getHeadVo(requestType,comCode);
		}
		
		CiClaimPlatformLogVo logVo = initPlatformLogVo(requestType,bodyVo);
		logVo.setComCode(comCode);
		logVo.setAreaCode(areaCode);
		Date currentDate = new Date();
		logger.info("送平台 start...业务号："+logVo.getBussNo()+";类型："+logVo.getRequestType()+";类型名称："+logVo.getRequestName());
		
		try{
			PlatformMarshaller marsh = new PlatformMarshaller(headVo,bodyVo);
			String requestXML = marsh.toXml();
			logVo.setRequestXml(requestXML);
			logVo.setRequestTime(new Date());
			logger.info("业务号："+logVo.getBussNo()+";"+requestType.getName()+",requestXML=： \n"+requestXML);
			String seconds = SpringProperties.getProperty("HTTP_TIMEOUT");
			if(StringUtils.isBlank(seconds)){
				seconds = "20";
			}
			String responseXml = requestPlatform(requestXML,url,Integer.valueOf(seconds));
			logger.info("业务号："+logVo.getBussNo()+";"+requestType.getName()+",responseXml=： \n"+responseXml);
			logger.info("送平台 end, 耗时 "+( System.currentTimeMillis()-currentDate.getTime() )+
					"毫秒，业务号："+logVo.getBussNo()+";类型："+logVo.getRequestType()+"类型名称："+logVo.getRequestName());
			
			logVo.setResponseXml(responseXml);
			logVo.setResponseTime(new Date());

			logVo.setStatus("1");// 成功
			// 解析结果
			umMarsh = new PlatformUnMarshaller(requestType);
			umMarsh.parseXml(responseXml);

			String errorCode = umMarsh.getErrorCode();
			if(StringUtils.isNotBlank(errorCode)){
				logVo.setStatus("0");// 失败
				logVo.setErrorCode(errorCode);
				logVo.setErrorMessage(umMarsh.getErrorMessage());
//				throw new IllegalArgumentException("平台返回异常信息:"+logVo.getErrorMessage());
			}
			
			//保存报案的理赔编码
			String claimSeqNo = umMarsh.getClaimSeqNo();
			if (StringUtils.isNotBlank(claimSeqNo)) {
				logVo.setClaimSeqNo(claimSeqNo);
			}
			//保存赔案结案校验码
			logVo.setRemark(umMarsh.getClaimConfirmCode());
			
		}catch(IllegalArgumentException e){
			throw e;
		}catch(Exception e){
			logger.error("平台交互异常：RequestType="+requestType.name()+",error"+e.getMessage());
			logVo.setStatus("0");// 失败
			logVo.setErrorMessage("系统异常"+e.getMessage());
		}
		finally{
			if(platformTaskVo!=null){
				logVo.setTaskId(platformTaskVo.getId());
				platformTaskVo.setStatus(logVo.getStatus());
				platformTaskVo.setRemark(logVo.getErrorMessage());
				platformTaskVo.setOperateStatus(CodeConstants.OperateStatus.OFF);
				platformTaskVo.setRedoTimes(platformTaskVo.getRedoTimes()+1);
				platformTaskVo.setLastDate(new Date());
				platformTaskVo.setEndDate(new Date());
				PlatformLogUtil.updatePlatformTask(platformTaskVo);
			}
			//上海代位求偿查询平台失败，不存日志
			if(logVo.getComCode().startsWith("22") && "0".equals(logVo.getStatus())
					&& RequestType.SubrogationClaim_SH.getCode().equals(logVo.getRequestType())){
				
			}else{
						PlatformLogUtil.saveLog(logVo);
					}
		}
		return logVo;
	}

	public <T> T getHeadVo(Class<T> valueType) {
		return umMarsh.getHeadVo(valueType);
	}
	
	public <T> T getBodyVo(Class<T> valueType) {
		return umMarsh.getBodyVo(valueType);
	}

	private String requestPlatform(String requestXML,String urlStr,int seconds) throws Exception {
		long t1 = System.currentTimeMillis();
		BufferedReader reader = null;
		HttpURLConnection connection = null;
		StringBuffer buffer = new StringBuffer();
		DataOutputStream dataOutputStream = null;
		OutputStream outputStream = null;
		InputStream inputStream = null;
		InputStreamReader inputStreamReader = null;
		try {
			URL url = new URL(urlStr);
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
				connection.setRequestProperty("Connection", "close");
				connection.setConnectTimeout(seconds * 1000);
				connection.setReadTimeout(seconds * 1000);
				connection.setAllowUserInteraction(true);
				logger.info("requestPlatform connecting...");
				connection.connect();
				logger.info("requestPlatform connected...");
			} catch (Exception ex) {
				throw new Exception("与平台连接失败，请稍候再试", ex);
			}

			try {
				outputStream = connection.getOutputStream();
				dataOutputStream = new DataOutputStream(outputStream);
				dataOutputStream.write(requestXML.getBytes("GBK"));
				dataOutputStream.flush();
				dataOutputStream.close();

				inputStream = connection.getInputStream();
				inputStreamReader = new InputStreamReader(inputStream,"GBK");
				reader = new BufferedReader(inputStreamReader);
				String strLine = "";
				while ((strLine = reader.readLine()) != null) {
					buffer.append(strLine);
				}
				reader.close();
				connection.disconnect();
				if (buffer.length() < 1) {
					throw new Exception("平台返回数据失败");
				}
			} catch (IOException e) {
				logger.error("读取平台返回数据失败",e);
				throw new Exception("读取平台返回数据失败", e);
			} finally {
				if(outputStream!=null){
					outputStream.close();
				}
				if(dataOutputStream!=null){
					dataOutputStream.close();
				}
				if(inputStream!=null){
					inputStream.close();
				}
				if(inputStreamReader!=null){
					inputStreamReader.close();
				}
				if (reader != null) {
					reader.close();
				}
				if(connection!=null){
					connection.disconnect();
				}
			}
			return buffer.toString();
		} finally {
			logger.warn("平台({})调用耗时{}ms",urlStr,System.currentTimeMillis()-t1);
		}
	}

}
