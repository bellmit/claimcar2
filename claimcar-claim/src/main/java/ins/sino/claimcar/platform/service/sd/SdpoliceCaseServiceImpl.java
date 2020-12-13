package ins.sino.claimcar.platform.service.sd;

import ins.platform.utils.ClaimBaseCoder;
import ins.platform.utils.DateUtils;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.carinterface.service.ClaimInterfaceLogService;
import ins.sino.claimcar.carinterface.util.BusinessType;
import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;
import ins.sino.claimcar.common.losscharge.service.LossChargeService;
import ins.sino.claimcar.common.losscharge.vo.PrpLDlossChargeVo;
import ins.sino.claimcar.endcase.service.ReOpenCaseService;
import ins.sino.claimcar.endcase.vo.PrpLReCaseVo;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.PrpLCMainService;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.trafficplatform.service.SdpoliceCaseService;
import ins.sino.claimcar.trafficplatform.vo.GdResponseVo;
import ins.sino.claimcar.trafficplatform.vo.HnResponseVo;
import ins.sino.claimcar.trafficplatform.vo.RequestCancleBasePartVo;
import ins.sino.claimcar.trafficplatform.vo.RequestCancleBodyVo;
import ins.sino.claimcar.trafficplatform.vo.RequestHeadVo;
import ins.sino.claimcar.trafficplatform.vo.RequestReopenBasePartVo;
import ins.sino.claimcar.trafficplatform.vo.RequestReopenBodyVo;
import ins.sino.claimcar.trafficplatform.vo.ResponseHeadVo;
import ins.sino.claimcar.trafficplatform.vo.SdResponseVo;
import ins.sino.claimcar.trafficplatform.vo.SdpoliceCancleVo;
import ins.sino.claimcar.trafficplatform.vo.SdpoliceReopenVo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;

import com.alibaba.dubbo.config.annotation.Service;


@Service(protocol = {"dubbo"}, validation = "true" , registry = {"default"} , timeout = 1000*60*10)
@Path("sdpoliceCaseService")
public class SdpoliceCaseServiceImpl implements SdpoliceCaseService{
	private static Logger logger = LoggerFactory.getLogger(SdpoliceCaseServiceImpl.class);
	
	@Autowired
	PrpLCMainService prpLCMainService;
	@Autowired
	PolicyViewService policyViewService;
	@Autowired
	ReOpenCaseService reOpenCaseService;
	@Autowired
    ClaimInterfaceLogService interfaceLogService;
	@Autowired
	LossChargeService lossChargeService;
	
	/**
	 * 山东预警（案件注销）
	 */
	@Override
	public void sendCaseCancleRegister(PrpLCMainVo prpLCMainVo,String cancleType,String reason,SysUserVo userVo) throws Exception {

		String registNo = prpLCMainVo.getRegistNo();
		String policyNo = prpLCMainVo.getPolicyNo();
		SdpoliceCancleVo cancleVo=new SdpoliceCancleVo();
		cancleVo = setParamsForSdCanclePoliceVo(prpLCMainVo,cancleType,reason);
		if(prpLCMainVo.getComCode().startsWith("62")){// 山东
			String url = SpringProperties.getProperty("SDWARN_URL");
			String requestXml = ClaimBaseCoder.objToXml(cancleVo);
			logger.info("发送平台的请求报文"+requestXml);
			String returnXml = "";
			try{
				returnXml = requestSdpolice(requestXml,url,200);
				logger.info("发送平台的返回报文"+returnXml);
			}catch(Exception e){
				e.printStackTrace();
			}
			finally{
				ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
				SdResponseVo sdResponseVo = new SdResponseVo();
				sdResponseVo = ClaimBaseCoder.xmlToObj(returnXml,SdResponseVo.class);
				ResponseHeadVo resHeadVo = new ResponseHeadVo();
				if(sdResponseVo!=null){
					resHeadVo = sdResponseVo.getHeadVo();
				}
				logVo.setRegistNo(registNo);
				logVo.setOperateNode("案件注销"); // 操作类型
				logVo.setPolicyNo(policyNo); // 保单号类型
				logVo.setCreateUser(userVo.getUserCode());
				logVo.setServiceType(cancleType);// 注销类型
				logVo.setComCode(userVo.getComCode());
				logVo.setCompensateNo(reason);// 注销原因
				logVo.setRequestUrl(url);
				logVo.setErrorCode(resHeadVo.getErrorCode());
				logVo.setErrorMessage(resHeadVo.getErrorMessage());
				this.logUtil(requestXml,returnXml,logVo,resHeadVo.getResponseCode(),resHeadVo.getErrorMessage(),"Cancle");
			}
		}else if(prpLCMainVo.getComCode().startsWith("10")){// 广东
			String url = SpringProperties.getProperty("GDWARN_URL");
			String requestXml = ClaimBaseCoder.objToXml(cancleVo);
			logger.info("发送平台的请求报文"+requestXml);
			String returnXml = "";
			try{
				returnXml = requestSdpolice(requestXml,url,200);
				logger.info("发送平台的返回报文"+returnXml);
			}catch(Exception e){
				e.printStackTrace();
			}
			finally{
				ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
				GdResponseVo gdResponseVo = new GdResponseVo();
				gdResponseVo = ClaimBaseCoder.xmlToObj(returnXml,GdResponseVo.class);
				ResponseHeadVo resHeadVo = new ResponseHeadVo();
				if(gdResponseVo!=null){
					resHeadVo = gdResponseVo.getHeadVo();
				}
				logVo.setRegistNo(registNo);
				logVo.setOperateNode("案件注销"); // 操作类型
				logVo.setPolicyNo(policyNo); // 保单号类型
				logVo.setCreateUser(userVo.getUserCode());
				logVo.setServiceType(cancleType);// 注销类型
				logVo.setComCode(userVo.getComCode());
				logVo.setCompensateNo(reason);// 注销原因
				logVo.setRequestUrl(url);
				logVo.setErrorCode(resHeadVo.getErrorCode());
				logVo.setErrorMessage(resHeadVo.getErrorMessage());
				this.gdLogUtil(requestXml,returnXml,logVo,resHeadVo.getResponseCode(),resHeadVo.getErrorMessage(),"Cancle");
			}
		}else if(prpLCMainVo.getComCode().startsWith("50")){// 河南
			String url = SpringProperties.getProperty("HNWARN_URL");
			String requestXml = ClaimBaseCoder.objToXml(cancleVo);
			logger.info("发送平台的请求报文"+requestXml);
			String returnXml = "";
			try{
				returnXml = requestSdpolice(requestXml,url,200);
				logger.info("发送平台的返回报文"+returnXml);
			}catch(Exception e){
				e.printStackTrace();
			}
			finally{
				ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
				HnResponseVo hnResponseVo = new HnResponseVo();
				hnResponseVo = ClaimBaseCoder.xmlToObj(returnXml,HnResponseVo.class);
				ResponseHeadVo resHeadVo = new ResponseHeadVo();
				if(hnResponseVo!=null){
					resHeadVo = hnResponseVo.getHeadVo();
				}
				logVo.setRegistNo(registNo);
				logVo.setOperateNode("案件注销"); // 操作类型
				logVo.setPolicyNo(policyNo); // 保单号类型
				logVo.setCreateUser(userVo.getUserCode());
				logVo.setServiceType(cancleType);// 注销类型
				logVo.setComCode(userVo.getComCode());
				logVo.setCompensateNo(reason);// 注销原因
				logVo.setRequestUrl(url);
				logVo.setErrorCode(resHeadVo.getErrorCode());
				logVo.setErrorMessage(resHeadVo.getErrorMessage());
				this.hnLogUtil(requestXml,returnXml,logVo,resHeadVo.getResponseCode(),resHeadVo.getErrorMessage(),"Cancle");
			}
		}

	}

	@Override
	public void sendReopenCaseRegister(String endCaseNo, String policyNo, SysUserVo userVo)throws Exception {
		PrpLReCaseVo reCaseVo = reOpenCaseService.findReCaseVoByEndCaseNo(endCaseNo);
		String registNo = reCaseVo.getRegistNo();
		PrpLCMainVo prpLCMainVo = prpLCMainService.findPrpLCMain(registNo,policyNo);
		SdpoliceReopenVo sdpoliceReopenVo=new SdpoliceReopenVo();
		sdpoliceReopenVo=setParamsForSdReopenPoliceVo(endCaseNo,policyNo,userVo);
		if(prpLCMainVo.getComCode().startsWith("62")){// 山东
			String url = SpringProperties.getProperty("SDWARN_URL");
			String requestXml = ClaimBaseCoder.objToXml(sdpoliceReopenVo);
			String returnXml = "";
		try{			
			returnXml=requestSdpolice(requestXml,url,200);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
			SdResponseVo sdResponseVo=new SdResponseVo();
			sdResponseVo=ClaimBaseCoder.xmlToObj(returnXml,SdResponseVo.class);
			ResponseHeadVo resHeadVo=new ResponseHeadVo();
			if(sdResponseVo!=null){
				resHeadVo=sdResponseVo.getHeadVo(); 
			}
            logVo.setRegistNo(sdpoliceReopenVo.getBodyVo().getBasePartVo().getClaimNotificationNo());//结案号
            logVo.setOperateNode("案件重开"); //操作类型
            logVo.setPolicyNo(policyNo);   //保单号类型
            logVo.setCreateUser(userVo.getUserCode());
            logVo.setComCode(userVo.getComCode());
            logVo.setRequestUrl(url);
            this.logUtil(requestXml,returnXml,logVo,resHeadVo.getResponseCode(),resHeadVo.getErrorMessage(),"Reopen");
			}
		}else if(prpLCMainVo.getComCode().startsWith("10")){// 广东
			String url = SpringProperties.getProperty("GDWARN_URL");
			String requestXml = ClaimBaseCoder.objToXml(sdpoliceReopenVo);
			String returnXml = "";
			try{
				returnXml = requestSdpolice(requestXml,url,200);
			}catch(Exception e){
				e.printStackTrace();
			}
			finally{
				ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
				GdResponseVo gdResponseVo = new GdResponseVo();
				gdResponseVo = ClaimBaseCoder.xmlToObj(returnXml,GdResponseVo.class);
				ResponseHeadVo resHeadVo = new ResponseHeadVo();
				if(gdResponseVo!=null){
					resHeadVo = gdResponseVo.getHeadVo();
				}
				logVo.setRegistNo(sdpoliceReopenVo.getBodyVo().getBasePartVo().getClaimNotificationNo());// 结案号
				logVo.setOperateNode("案件重开"); // 操作类型
				logVo.setPolicyNo(policyNo); // 保单号类型
				logVo.setCreateUser(userVo.getUserCode());
				logVo.setComCode(userVo.getComCode());
				logVo.setRequestUrl(url);
				this.gdLogUtil(requestXml,returnXml,logVo,resHeadVo.getResponseCode(),resHeadVo.getErrorMessage(),"Reopen");
			}
		}else if(prpLCMainVo.getComCode().startsWith("50")){// 河南
			String url = SpringProperties.getProperty("HNWARN_URL");
			String requestXml = ClaimBaseCoder.objToXml(sdpoliceReopenVo);
			String returnXml = "";
			try{
				returnXml = requestSdpolice(requestXml,url,200);
			}catch(Exception e){
				e.printStackTrace();
			}
			finally{
				ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
				HnResponseVo hnResponseVo = new HnResponseVo();
				hnResponseVo = ClaimBaseCoder.xmlToObj(returnXml,HnResponseVo.class);
				ResponseHeadVo resHeadVo = new ResponseHeadVo();
				if(hnResponseVo!=null){
					resHeadVo = hnResponseVo.getHeadVo();
				}
				logVo.setRegistNo(sdpoliceReopenVo.getBodyVo().getBasePartVo().getClaimNotificationNo());// 结案号
				logVo.setOperateNode("案件重开"); // 操作类型
				logVo.setPolicyNo(policyNo); // 保单号类型
				logVo.setCreateUser(userVo.getUserCode());
				logVo.setComCode(userVo.getComCode());
				logVo.setRequestUrl(url);
				this.hnLogUtil(requestXml,returnXml,logVo,resHeadVo.getResponseCode(),resHeadVo.getErrorMessage(),"Reopen");
			}
		}

	}
	
	/**
	 * 接口组装数据
	 * @param requestXML
	 * @param urlStr
	 * @param seconds
	 * @return
	 * @throws Exception
	 */
	private String requestSdpolice(String requestXML,String urlStr,int seconds) throws Exception {
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
            httpUrlConn.setRequestProperty("Content-Type", "text/xml;charset=GBK");
            httpUrlConn.setConnectTimeout(seconds * 1000);
	        
            httpUrlConn.connect();    
    
            String outputStr =requestXML;
            			
            OutputStream outputStream = httpUrlConn.getOutputStream();  		        
            // 当有数据需要提交时    
            if (null != outputStr) {    
                // 注意编码格式，防止中文乱码    outputStream.write
                outputStream.write(outputStr.getBytes("GBK"));    
            }    
    
            // 将返回的输入流转换成字符串    
            InputStream inputStream = httpUrlConn.getInputStream();    
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "GBK");    
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);    
    
            String str = null;    
            while ((str = bufferedReader.readLine()) != null) {
            	
                buffer.append(str);    
            } 
            if (buffer.length() < 1) {
				throw new Exception("平台风险预警系统返回数据失败");
			}
            bufferedReader.close();    
            inputStreamReader.close();    
            // 释放资源  
            outputStream.flush();
            outputStream.close();
            inputStream.close();    
            inputStream = null;    
            httpUrlConn.disconnect(); 
            System.out.println(buffer);
            responseXml=buffer.toString();
            
        } catch (ConnectException ce) {
			throw new Exception("与平台风险预警系统连接失败，请稍候再试",ce);
        } catch (Exception e) {
        	e.printStackTrace();
			throw new Exception("读平台风险预警系统返回数据失败",e);
        	
        } finally {
			logger.warn("接口({})调用耗时{}ms", urlStr, System.currentTimeMillis() - t1);
		}    
        return responseXml;
	}
	
	/**
	 * 案件注销 赋值
	 * @param registNo
	 * @param cancleType
	 * @param reason
	 * @param policyType
	 * @return
	 */
	private SdpoliceCancleVo setParamsForSdCanclePoliceVo(PrpLCMainVo prpLCMainVo,String cancleType,String reason){	
		String user = "";
		String password = "";
		if(prpLCMainVo.getComCode().startsWith("62")){// 山东
			user = SpringProperties.getProperty("SDWARN_USER");
			password = SpringProperties.getProperty("SDWARN_PW");
		}else if(prpLCMainVo.getComCode().startsWith("10")){// 广东
			user = SpringProperties.getProperty("GDWARN_USER");
			password = SpringProperties.getProperty("GDWARN_PW");
		}else if(prpLCMainVo.getComCode().startsWith("50")){// 河南
			user = SpringProperties.getProperty("HNWARN_USER");
			password = SpringProperties.getProperty("HNWARN_PW");
		}
		String registNo = prpLCMainVo.getRegistNo();
		String claimSeqNo = prpLCMainVo.getClaimSequenceNo();//理赔编码
		String validNo = prpLCMainVo.getValidNo();//投保确认码

		SdpoliceCancleVo cancleVo = new SdpoliceCancleVo(); // 公用一套VO
		RequestHeadVo headVo=new RequestHeadVo();
		headVo.setRequestType("C0307");//请求类型
		headVo.setUser(user);// 请求人员
		headVo.setPassword(password);//请求密码
		RequestCancleBodyVo bodyVo=new RequestCancleBodyVo();
		RequestCancleBasePartVo basePartVo=new RequestCancleBasePartVo();
		basePartVo.setConfirmSequenceNo(validNo);//投保确认码
		basePartVo.setClaimSequenceNo(claimSeqNo);//理赔编号
		basePartVo.setClaimNotificationNo(registNo);//报案号
		
		String desc = "";
		BigDecimal directClaimAmount = new BigDecimal("0");
		//案件注销类型
		if ("registCancle".equals(cancleType)) {
			basePartVo.setCancelType("1");//报案注销
			
			if("3".equals(reason)){
				reason = "51";
				desc = "客户放弃索赔";
			}else if ("4".equals(reason)){
				reason = "07";
				desc = "非保险责任内损失";
			}else if ("21".equals(reason)) {//保单取消关联
				reason = "21";
				desc = "保险公司注销案件";
			}else {
				reason = "99";
				desc = "其他";
			}

		}else {
			basePartVo.setCancelType("2");//立案注销
			
			if("claimCancle".equals(cancleType)){
				if("0".equals(reason)||"6".equals(reason)||"7".equals(reason)){
					reason = "08";
					desc = "免除责任";
				}else if ("2".equals(reason)) {
					reason = "07";
					desc = "非保险责任内损失";
				}else if ("3".equals(reason)) {
					reason = "06";
					desc = "合同无效";
				}else if ("8".equals(reason)) {
					reason = "09";
					desc = "伪造虚假单证或虚构保险事故";
				}else {
					reason = "99";
					desc = "其他";
				}
			}			
			if("caseRefuse".equals(cancleType)){
				if("2".equals(reason)){
					reason = "08";
					desc = "免除责任";
				}else if ("3".equals(reason)) {
					reason = "09";
					desc = "伪造虚假单证或虚构保险事故";
				}else {
					reason = "99";
					desc = "其他";
				}
			}
			
			List<PrpLDlossChargeVo> prpLDlossChargeVos = lossChargeService.findLossChargeVoByRegistNo(registNo);
			for (PrpLDlossChargeVo prpLDlossChargeVo : prpLDlossChargeVos) {
				if(prpLDlossChargeVo.getVeriChargeFee()!=null){
					directClaimAmount.add(prpLDlossChargeVo.getVeriChargeFee());
				}else {
					directClaimAmount.add(prpLDlossChargeVo.getChargeFee());
				}
			}

		}
			
		basePartVo.setCancelCause(reason);//注销原因
		basePartVo.setCancelDate(DateUtils.dateToStr(DateUtils.now(), DateUtils.YMDHM));//注销时间；精确到分
		basePartVo.setCancelDesc(desc);//拒赔理由
		basePartVo.setDirectClaimAmount(String.valueOf(directClaimAmount));//直接理赔费用总金额	
		bodyVo.setBasePartVo(basePartVo);
		cancleVo.setHeadVo(headVo);
		cancleVo.setBodyVo(bodyVo);
		return cancleVo;
	}
	
	
	private SdpoliceReopenVo setParamsForSdReopenPoliceVo(String endCaseNo ,String policyNo , SysUserVo userVo){

		PrpLReCaseVo reCaseVo = reOpenCaseService.findReCaseVoByEndCaseNo(endCaseNo);
		String registNo = reCaseVo.getRegistNo();
		PrpLCMainVo prpLCMainVo = prpLCMainService.findPrpLCMain(registNo, policyNo);
		String user = "";
		String password = "";
		if(prpLCMainVo.getComCode().startsWith("62")){// 山东
			user = SpringProperties.getProperty("SDWARN_USER");
			password = SpringProperties.getProperty("SDWARN_PW");
		}else if(prpLCMainVo.getComCode().startsWith("10")){// 广东
			user = SpringProperties.getProperty("GDWARN_USER");
			password = SpringProperties.getProperty("GDWARN_PW");
		}else if(prpLCMainVo.getComCode().startsWith("50")){// 河南
			user = SpringProperties.getProperty("HNWARN_USER");
			password = SpringProperties.getProperty("HNWARN_PW");
		}
		String claimSeqNo = prpLCMainVo.getClaimSequenceNo();//理赔编码
		String validNo = prpLCMainVo.getValidNo();//投保确认码

		SdpoliceReopenVo reopenVo=new SdpoliceReopenVo();
		RequestHeadVo headVo=new RequestHeadVo();
		headVo.setRequestType("C0308");//请求类型
		headVo.setUser(user);//请求人员
		headVo.setPassword(password);//请求密码
		RequestReopenBodyVo bodyVo=new RequestReopenBodyVo();
		RequestReopenBasePartVo basePartVo=new RequestReopenBasePartVo();
		basePartVo.setConfirmSequenceNo(validNo);//投保确认码
		basePartVo.setClaimSequenceNo(claimSeqNo);//理赔编码
		basePartVo.setClaimNotificationNo(registNo);//报案号
		basePartVo.setReopenCause(reCaseVo.getOpenReason());//重开赔案原因
		Date reOpenDate = reCaseVo.getUpdateTime();//重开时间
		if(reOpenDate == null){
			reOpenDate = reCaseVo.getDealCaseDate();
			if(reOpenDate == null){
				reOpenDate = new Date();
			}
		}
		basePartVo.setReopenDate(DateUtils.dateToStr(reOpenDate, DateUtils.YMDHM));
		bodyVo.setBasePartVo(basePartVo);
		reopenVo.setHeadVo(headVo);
		reopenVo.setBodyVo(bodyVo);
		return reopenVo;
	}
	
	/**
	 * 山东预警日志保存
	 * @param reqXml
	 * @param resXml
	 * @param logVo
	 * @param flag
	 * @param errorMsg
	 * @param interfaceType--接口请求类型
	 */
	private void logUtil(String reqXml,String resXml, ClaimInterfaceLogVo logVo,String flag,String errorMsg,String interfaceType) {
        logger.info("===============山东风险预警系统===========");
        logger.info(reqXml);
        logger.info(resXml);
        if("Cancle".endsWith(interfaceType)){
        	logVo.setBusinessType(BusinessType.SDEW_claimCancel.name());
            logVo.setBusinessName(BusinessType.SDEW_claimCancel.getName());
        }else if("Reopen".endsWith(interfaceType)){
        	logVo.setBusinessType(BusinessType.SDEW_reOpen.name());
            logVo.setBusinessName(BusinessType.SDEW_reOpen.getName());
        }
        logVo.setRequestXml(reqXml);
        logVo.setResponseXml(resXml);
        logVo.setCreateTime(new Date());
        logVo.setRequestTime(new Date());
        if("1".equals(flag)){
            logVo.setStatus("1");
            logVo.setErrorCode("true");
            logVo.setErrorMessage(errorMsg);
        }else{
            logVo.setStatus("0");
            logVo.setErrorCode("false");
            logVo.setErrorMessage(errorMsg);
        }
        interfaceLogService.save(logVo);
	 }

	/**
	 * 广东平台日志保存
	 * 
	 * <pre></pre>
	 * @param reqXml
	 * @param resXml
	 * @param logVo
	 * @param flag
	 * @param errorMsg
	 * @param interfaceType
	 * @modified: ☆zhousm(2018年9月14日 下午2:21:32): <br>
	 */
	private void gdLogUtil(String reqXml,String resXml,ClaimInterfaceLogVo logVo,String flag,String errorMsg,String interfaceType) {
		logger.info("===============广东车辆数据服务平台上传日志===========");
		logger.info(reqXml);
		logger.info(resXml);
		if("Cancle".endsWith(interfaceType)){
			logVo.setBusinessType(BusinessType.GDEW_claimCancel.name());
			logVo.setBusinessName(BusinessType.GDEW_claimCancel.getName());
		}else if("Reopen".endsWith(interfaceType)){
			logVo.setBusinessType(BusinessType.GDEW_reOpen.name());
			logVo.setBusinessName(BusinessType.GDEW_reOpen.getName());
		}
		logVo.setRequestXml(reqXml);
		logVo.setResponseXml(resXml);
		logVo.setCreateTime(new Date());
		logVo.setRequestTime(new Date());
		if("1".equals(flag)){
			logVo.setStatus("1");
			logVo.setErrorCode("true");
			logVo.setErrorMessage(errorMsg);
		}else{
			logVo.setStatus("0");
			logVo.setErrorCode("false");
			logVo.setErrorMessage(errorMsg);
		}
		interfaceLogService.save(logVo);
	}

	/**
	 * 河南平台日志保存
	 * 
	 * <pre></pre>
	 * @param reqXml
	 * @param resXml
	 * @param logVo
	 * @param flag
	 * @param errorMsg
	 * @param interfaceType
	 * @modified: ☆zhousm(2018年9月14日 下午2:25:30): <br>
	 */
	private void hnLogUtil(String reqXml,String resXml,ClaimInterfaceLogVo logVo,String flag,String errorMsg,String interfaceType) {
		logger.info("===============河南交管平台上传日志===========");
		logger.info(reqXml);
		logger.info(resXml);
		if("Cancle".endsWith(interfaceType)){
			logVo.setBusinessType(BusinessType.HNEW_claimCancel.name());
			logVo.setBusinessName(BusinessType.HNEW_claimCancel.getName());
		}else if("Reopen".endsWith(interfaceType)){
			logVo.setBusinessType(BusinessType.HNEW_reOpen.name());
			logVo.setBusinessName(BusinessType.HNEW_reOpen.getName());
		}
		logVo.setRequestXml(reqXml);
		logVo.setResponseXml(resXml);
		logVo.setCreateTime(new Date());
		logVo.setRequestTime(new Date());
		if("1".equals(flag)){
			logVo.setStatus("1");
			logVo.setErrorCode("true");
			logVo.setErrorMessage(errorMsg);
		}else{
			logVo.setStatus("0");
			logVo.setErrorCode("false");
			logVo.setErrorMessage(errorMsg);
		}
		interfaceLogService.save(logVo);
	}
}
