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
	 * ??????????????????????????????
	 */
	@Override
	public void sendCaseCancleRegister(PrpLCMainVo prpLCMainVo,String cancleType,String reason,SysUserVo userVo) throws Exception {

		String registNo = prpLCMainVo.getRegistNo();
		String policyNo = prpLCMainVo.getPolicyNo();
		SdpoliceCancleVo cancleVo=new SdpoliceCancleVo();
		cancleVo = setParamsForSdCanclePoliceVo(prpLCMainVo,cancleType,reason);
		if(prpLCMainVo.getComCode().startsWith("62")){// ??????
			String url = SpringProperties.getProperty("SDWARN_URL");
			String requestXml = ClaimBaseCoder.objToXml(cancleVo);
			logger.info("???????????????????????????"+requestXml);
			String returnXml = "";
			try{
				returnXml = requestSdpolice(requestXml,url,200);
				logger.info("???????????????????????????"+returnXml);
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
				logVo.setOperateNode("????????????"); // ????????????
				logVo.setPolicyNo(policyNo); // ???????????????
				logVo.setCreateUser(userVo.getUserCode());
				logVo.setServiceType(cancleType);// ????????????
				logVo.setComCode(userVo.getComCode());
				logVo.setCompensateNo(reason);// ????????????
				logVo.setRequestUrl(url);
				logVo.setErrorCode(resHeadVo.getErrorCode());
				logVo.setErrorMessage(resHeadVo.getErrorMessage());
				this.logUtil(requestXml,returnXml,logVo,resHeadVo.getResponseCode(),resHeadVo.getErrorMessage(),"Cancle");
			}
		}else if(prpLCMainVo.getComCode().startsWith("10")){// ??????
			String url = SpringProperties.getProperty("GDWARN_URL");
			String requestXml = ClaimBaseCoder.objToXml(cancleVo);
			logger.info("???????????????????????????"+requestXml);
			String returnXml = "";
			try{
				returnXml = requestSdpolice(requestXml,url,200);
				logger.info("???????????????????????????"+returnXml);
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
				logVo.setOperateNode("????????????"); // ????????????
				logVo.setPolicyNo(policyNo); // ???????????????
				logVo.setCreateUser(userVo.getUserCode());
				logVo.setServiceType(cancleType);// ????????????
				logVo.setComCode(userVo.getComCode());
				logVo.setCompensateNo(reason);// ????????????
				logVo.setRequestUrl(url);
				logVo.setErrorCode(resHeadVo.getErrorCode());
				logVo.setErrorMessage(resHeadVo.getErrorMessage());
				this.gdLogUtil(requestXml,returnXml,logVo,resHeadVo.getResponseCode(),resHeadVo.getErrorMessage(),"Cancle");
			}
		}else if(prpLCMainVo.getComCode().startsWith("50")){// ??????
			String url = SpringProperties.getProperty("HNWARN_URL");
			String requestXml = ClaimBaseCoder.objToXml(cancleVo);
			logger.info("???????????????????????????"+requestXml);
			String returnXml = "";
			try{
				returnXml = requestSdpolice(requestXml,url,200);
				logger.info("???????????????????????????"+returnXml);
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
				logVo.setOperateNode("????????????"); // ????????????
				logVo.setPolicyNo(policyNo); // ???????????????
				logVo.setCreateUser(userVo.getUserCode());
				logVo.setServiceType(cancleType);// ????????????
				logVo.setComCode(userVo.getComCode());
				logVo.setCompensateNo(reason);// ????????????
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
		if(prpLCMainVo.getComCode().startsWith("62")){// ??????
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
            logVo.setRegistNo(sdpoliceReopenVo.getBodyVo().getBasePartVo().getClaimNotificationNo());//?????????
            logVo.setOperateNode("????????????"); //????????????
            logVo.setPolicyNo(policyNo);   //???????????????
            logVo.setCreateUser(userVo.getUserCode());
            logVo.setComCode(userVo.getComCode());
            logVo.setRequestUrl(url);
            this.logUtil(requestXml,returnXml,logVo,resHeadVo.getResponseCode(),resHeadVo.getErrorMessage(),"Reopen");
			}
		}else if(prpLCMainVo.getComCode().startsWith("10")){// ??????
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
				logVo.setRegistNo(sdpoliceReopenVo.getBodyVo().getBasePartVo().getClaimNotificationNo());// ?????????
				logVo.setOperateNode("????????????"); // ????????????
				logVo.setPolicyNo(policyNo); // ???????????????
				logVo.setCreateUser(userVo.getUserCode());
				logVo.setComCode(userVo.getComCode());
				logVo.setRequestUrl(url);
				this.gdLogUtil(requestXml,returnXml,logVo,resHeadVo.getResponseCode(),resHeadVo.getErrorMessage(),"Reopen");
			}
		}else if(prpLCMainVo.getComCode().startsWith("50")){// ??????
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
				logVo.setRegistNo(sdpoliceReopenVo.getBodyVo().getBasePartVo().getClaimNotificationNo());// ?????????
				logVo.setOperateNode("????????????"); // ????????????
				logVo.setPolicyNo(policyNo); // ???????????????
				logVo.setCreateUser(userVo.getUserCode());
				logVo.setComCode(userVo.getComCode());
				logVo.setRequestUrl(url);
				this.hnLogUtil(requestXml,returnXml,logVo,resHeadVo.getResponseCode(),resHeadVo.getErrorMessage(),"Reopen");
			}
		}

	}
	
	/**
	 * ??????????????????
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
            // ?????????????????????GET/POST???    
            httpUrlConn.setRequestMethod("POST"); 
            httpUrlConn.setRequestProperty("Content-Type", "text/xml;charset=GBK");
            httpUrlConn.setConnectTimeout(seconds * 1000);
	        
            httpUrlConn.connect();    
    
            String outputStr =requestXML;
            			
            OutputStream outputStream = httpUrlConn.getOutputStream();  		        
            // ???????????????????????????    
            if (null != outputStr) {    
                // ???????????????????????????????????????    outputStream.write
                outputStream.write(outputStr.getBytes("GBK"));    
            }    
    
            // ???????????????????????????????????????    
            InputStream inputStream = httpUrlConn.getInputStream();    
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "GBK");    
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);    
    
            String str = null;    
            while ((str = bufferedReader.readLine()) != null) {
            	
                buffer.append(str);    
            } 
            if (buffer.length() < 1) {
				throw new Exception("??????????????????????????????????????????");
			}
            bufferedReader.close();    
            inputStreamReader.close();    
            // ????????????  
            outputStream.flush();
            outputStream.close();
            inputStream.close();    
            inputStream = null;    
            httpUrlConn.disconnect(); 
            System.out.println(buffer);
            responseXml=buffer.toString();
            
        } catch (ConnectException ce) {
			throw new Exception("?????????????????????????????????????????????????????????",ce);
        } catch (Exception e) {
        	e.printStackTrace();
			throw new Exception("?????????????????????????????????????????????",e);
        	
        } finally {
			logger.warn("??????({})????????????{}ms", urlStr, System.currentTimeMillis() - t1);
		}    
        return responseXml;
	}
	
	/**
	 * ???????????? ??????
	 * @param registNo
	 * @param cancleType
	 * @param reason
	 * @param policyType
	 * @return
	 */
	private SdpoliceCancleVo setParamsForSdCanclePoliceVo(PrpLCMainVo prpLCMainVo,String cancleType,String reason){	
		String user = "";
		String password = "";
		if(prpLCMainVo.getComCode().startsWith("62")){// ??????
			user = SpringProperties.getProperty("SDWARN_USER");
			password = SpringProperties.getProperty("SDWARN_PW");
		}else if(prpLCMainVo.getComCode().startsWith("10")){// ??????
			user = SpringProperties.getProperty("GDWARN_USER");
			password = SpringProperties.getProperty("GDWARN_PW");
		}else if(prpLCMainVo.getComCode().startsWith("50")){// ??????
			user = SpringProperties.getProperty("HNWARN_USER");
			password = SpringProperties.getProperty("HNWARN_PW");
		}
		String registNo = prpLCMainVo.getRegistNo();
		String claimSeqNo = prpLCMainVo.getClaimSequenceNo();//????????????
		String validNo = prpLCMainVo.getValidNo();//???????????????

		SdpoliceCancleVo cancleVo = new SdpoliceCancleVo(); // ????????????VO
		RequestHeadVo headVo=new RequestHeadVo();
		headVo.setRequestType("C0307");//????????????
		headVo.setUser(user);// ????????????
		headVo.setPassword(password);//????????????
		RequestCancleBodyVo bodyVo=new RequestCancleBodyVo();
		RequestCancleBasePartVo basePartVo=new RequestCancleBasePartVo();
		basePartVo.setConfirmSequenceNo(validNo);//???????????????
		basePartVo.setClaimSequenceNo(claimSeqNo);//????????????
		basePartVo.setClaimNotificationNo(registNo);//?????????
		
		String desc = "";
		BigDecimal directClaimAmount = new BigDecimal("0");
		//??????????????????
		if ("registCancle".equals(cancleType)) {
			basePartVo.setCancelType("1");//????????????
			
			if("3".equals(reason)){
				reason = "51";
				desc = "??????????????????";
			}else if ("4".equals(reason)){
				reason = "07";
				desc = "????????????????????????";
			}else if ("21".equals(reason)) {//??????????????????
				reason = "21";
				desc = "????????????????????????";
			}else {
				reason = "99";
				desc = "??????";
			}

		}else {
			basePartVo.setCancelType("2");//????????????
			
			if("claimCancle".equals(cancleType)){
				if("0".equals(reason)||"6".equals(reason)||"7".equals(reason)){
					reason = "08";
					desc = "????????????";
				}else if ("2".equals(reason)) {
					reason = "07";
					desc = "????????????????????????";
				}else if ("3".equals(reason)) {
					reason = "06";
					desc = "????????????";
				}else if ("8".equals(reason)) {
					reason = "09";
					desc = "???????????????????????????????????????";
				}else {
					reason = "99";
					desc = "??????";
				}
			}			
			if("caseRefuse".equals(cancleType)){
				if("2".equals(reason)){
					reason = "08";
					desc = "????????????";
				}else if ("3".equals(reason)) {
					reason = "09";
					desc = "???????????????????????????????????????";
				}else {
					reason = "99";
					desc = "??????";
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
			
		basePartVo.setCancelCause(reason);//????????????
		basePartVo.setCancelDate(DateUtils.dateToStr(DateUtils.now(), DateUtils.YMDHM));//???????????????????????????
		basePartVo.setCancelDesc(desc);//????????????
		basePartVo.setDirectClaimAmount(String.valueOf(directClaimAmount));//???????????????????????????	
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
		if(prpLCMainVo.getComCode().startsWith("62")){// ??????
			user = SpringProperties.getProperty("SDWARN_USER");
			password = SpringProperties.getProperty("SDWARN_PW");
		}else if(prpLCMainVo.getComCode().startsWith("10")){// ??????
			user = SpringProperties.getProperty("GDWARN_USER");
			password = SpringProperties.getProperty("GDWARN_PW");
		}else if(prpLCMainVo.getComCode().startsWith("50")){// ??????
			user = SpringProperties.getProperty("HNWARN_USER");
			password = SpringProperties.getProperty("HNWARN_PW");
		}
		String claimSeqNo = prpLCMainVo.getClaimSequenceNo();//????????????
		String validNo = prpLCMainVo.getValidNo();//???????????????

		SdpoliceReopenVo reopenVo=new SdpoliceReopenVo();
		RequestHeadVo headVo=new RequestHeadVo();
		headVo.setRequestType("C0308");//????????????
		headVo.setUser(user);//????????????
		headVo.setPassword(password);//????????????
		RequestReopenBodyVo bodyVo=new RequestReopenBodyVo();
		RequestReopenBasePartVo basePartVo=new RequestReopenBasePartVo();
		basePartVo.setConfirmSequenceNo(validNo);//???????????????
		basePartVo.setClaimSequenceNo(claimSeqNo);//????????????
		basePartVo.setClaimNotificationNo(registNo);//?????????
		basePartVo.setReopenCause(reCaseVo.getOpenReason());//??????????????????
		Date reOpenDate = reCaseVo.getUpdateTime();//????????????
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
	 * ????????????????????????
	 * @param reqXml
	 * @param resXml
	 * @param logVo
	 * @param flag
	 * @param errorMsg
	 * @param interfaceType--??????????????????
	 */
	private void logUtil(String reqXml,String resXml, ClaimInterfaceLogVo logVo,String flag,String errorMsg,String interfaceType) {
        logger.info("===============????????????????????????===========");
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
	 * ????????????????????????
	 * 
	 * <pre></pre>
	 * @param reqXml
	 * @param resXml
	 * @param logVo
	 * @param flag
	 * @param errorMsg
	 * @param interfaceType
	 * @modified: ???zhousm(2018???9???14??? ??????2:21:32): <br>
	 */
	private void gdLogUtil(String reqXml,String resXml,ClaimInterfaceLogVo logVo,String flag,String errorMsg,String interfaceType) {
		logger.info("===============??????????????????????????????????????????===========");
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
	 * ????????????????????????
	 * 
	 * <pre></pre>
	 * @param reqXml
	 * @param resXml
	 * @param logVo
	 * @param flag
	 * @param errorMsg
	 * @param interfaceType
	 * @modified: ???zhousm(2018???9???14??? ??????2:25:30): <br>
	 */
	private void hnLogUtil(String reqXml,String resXml,ClaimInterfaceLogVo logVo,String flag,String errorMsg,String interfaceType) {
		logger.info("===============??????????????????????????????===========");
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
