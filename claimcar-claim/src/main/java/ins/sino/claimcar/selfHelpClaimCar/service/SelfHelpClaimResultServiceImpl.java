package ins.sino.claimcar.selfHelpClaimCar.service;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Path;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;

import com.alibaba.dubbo.config.annotation.Service;

import ins.framework.lang.Springs;
import ins.platform.utils.ClaimBaseCoder;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.carinterface.service.ClaimInterfaceLogService;
import ins.sino.claimcar.carinterface.util.BusinessType;
import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.PrpLCheckVo;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claim.vo.PrpLLossItemVo;
import ins.sino.claimcar.common.claimtext.service.ClaimTextService;
import ins.sino.claimcar.common.claimtext.vo.PrpLClaimTextVo;
import ins.sino.claimcar.losscar.service.DeflossHandleService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLRegistCarLossVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.selfHelpClaimCar.vo.CaseCheckVo;
import ins.sino.claimcar.selfHelpClaimCar.vo.CaseInfoVo;
import ins.sino.claimcar.selfHelpClaimCar.vo.CasecarFeeVo;
import ins.sino.claimcar.selfHelpClaimCar.vo.CaseclaimrFeeVo;
import ins.sino.claimcar.selfHelpClaimCar.vo.ResponseHeadVo;
import ins.sino.claimcar.selfHelpClaimCar.vo.ResponseVo;
import ins.sino.claimcar.selfHelpClaimCar.vo.ResquestHeadVo;
import ins.sino.claimcar.selfHelpClaimCar.vo.SelfClaimResultReqBodyVo;
import ins.sino.claimcar.selfHelpClaimCar.vo.SelfClaimResultReqPacketVo;
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"},timeout = 1000000)
@Path("selfHelpClaimResultService")
public class SelfHelpClaimResultServiceImpl implements SelfHelpClaimResultService {
	private static Logger logger = LoggerFactory.getLogger(SelfHelpClaimResultServiceImpl.class);
	@Autowired
	CheckTaskService checkTaskService;
	@Autowired
	DeflossHandleService deflossHandleService;
	@Autowired
	ClaimTextService claimTextService;
	@Autowired
	CompensateTaskService compensateTaskService;
	@Autowired
	RegistQueryService registQueryService;
	@Autowired
    ClaimInterfaceLogService interfaceLogService;
	@Override
	public void sendSelfHelpClaimResult(String registNo, SysUserVo userVo,String status,String nodeFlag,String policyNo) {
		SelfClaimResultReqPacketVo packetVo=new SelfClaimResultReqPacketVo();
		ResponseVo resVo=new ResponseVo();//?????????
		ResponseHeadVo resHeadVo=new ResponseHeadVo();
		String url=SpringProperties.getProperty("selfclaim_url");//????????????
		url=url+"/claimResults/claimResults.do";
		String reqXml="";//??????xml
		String resXml="";//??????
		String timeout=SpringProperties.getProperty("HTTP_TIMEOUT");
		try {
			packetVo=evaluate(packetVo,registNo,status,nodeFlag,policyNo);
			reqXml=ClaimBaseCoder.objToXml(packetVo);
			logger.info("????????????????????????????????????xml--->"+reqXml);
			url = url+"?xml=";
			resXml=requestSelfClaim(reqXml,url,StringUtils.isBlank(timeout)?5:Integer.valueOf(timeout));
			resVo=ClaimBaseCoder.xmlToObj(resXml,ResponseVo.class);
			if(resVo!=null){
				resHeadVo=resVo.getResHeadVo();
			}
		    logger.info("????????????????????????????????????xml--->"+resXml);
			
		} catch (Exception e) {
			logger.info("????????????--->"+e.getMessage());
			e.printStackTrace();
		}finally{
			//????????????
			ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
            logVo.setRequestUrl(url);
            logVo.setRegistNo(registNo);
            logVo.setPolicyNo(policyNo);
            logVo.setCreateUser(userVo.getUserCode());
            logVo.setFlag(status);//????????????1.?????????2-?????????3-?????????4-??????
            String nodeName="";
            if("1".equals(nodeFlag)){
            	nodeName="??????";
            }else if("2".equals(nodeFlag)){
            	nodeName="??????";
            }else if("3".equals(nodeFlag)){
            	nodeName="??????";
            } else if("4".equals(nodeFlag)){
            	nodeName="????????????";
            }
            logVo.setOperateNode(nodeName);
            if("1".equals(status)){
	        	 logVo.setRemark("??????");
	         }else if("2".equals(status)){
	        	 logVo.setRemark("??????");
	         }else if("3".equals(status)){
	        	 logVo.setRemark("??????");
	         }else if("4".equals(status)){
	        	 logVo.setRemark("??????");
	         }
            logUtil(reqXml,resXml,logVo,resHeadVo.getErrno(),resHeadVo.getErrmsg());
		}
	}
    /**
     * ????????????????????????
     * @param vo
     * @param userVo
     * @param status
     * @param nodeFlag
     * @return
     */
	private SelfClaimResultReqPacketVo evaluate(SelfClaimResultReqPacketVo vo,String registNo,String status,String nodeFlag,String policyNo)throws Exception {
		ResquestHeadVo headVo=new ResquestHeadVo();
		headVo.setRequestType("SELFCLAIM_CORE_002");
		headVo.setUser("MCLAIM_USER");
		headVo.setPassWord("MCLAIM_PSD");
		vo.setReqHeadVo(headVo);
		SelfClaimResultReqBodyVo bodyVo=new SelfClaimResultReqBodyVo();
		PrpLRegistVo prpLRegistVo=registQueryService.findByRegistNo(registNo);
		CaseInfoVo caseInfoVo=new CaseInfoVo();
		caseInfoVo.setInscaseNo(registNo);
		caseInfoVo.setNoticeType(nodeFlag);
		if(prpLRegistVo!=null && prpLRegistVo.getPrpLRegistCarLosses()!=null && prpLRegistVo.getPrpLRegistCarLosses().size()>0){
			for(PrpLRegistCarLossVo carLossVo:prpLRegistVo.getPrpLRegistCarLosses()){
				if("1".equals(carLossVo.getLossparty())){
					caseInfoVo.setClaimcarNo(carLossVo.getLicenseNo());
					break;
				}
			}
		}
		caseInfoVo.setCpsType(status);
		caseInfoVo.setCaseType("1");
		if(!"4".equals(nodeFlag)){//??????????????????
			/*--------------------------????????????????????????--------------------------------*/
			CaseCheckVo caseCheckVo=new CaseCheckVo();//????????????
			PrpLCheckVo prpLCheckVo=checkTaskService.findCheckVoByRegistNo(registNo);
			if(prpLCheckVo!=null && prpLCheckVo.getPrpLCheckTask()!=null){
				caseCheckVo.setCheckerCode(prpLCheckVo.getPrpLCheckTask().getCheckerCode());//??????????????????
				caseCheckVo.setCheckerName(prpLCheckVo.getPrpLCheckTask().getChecker());//?????????????????????
				caseCheckVo.setCheckTime(DateFormatString(prpLCheckVo.getPrpLCheckTask().getCheckDate()));//????????????
				caseCheckVo.setCheckDesc(prpLCheckVo.getPrpLCheckTask().getContexts());//????????????
				caseCheckVo.setImgreView("1");//??????????????????--???????????????
			}
			caseInfoVo.setCaseCheckVo(caseCheckVo);
			/*--------------------------????????????????????????-------------------------------*/
			List<CasecarFeeVo> casecarFeeVos=new ArrayList<CasecarFeeVo>();
			List<PrpLDlossCarMainVo> lossCarMainVos=deflossHandleService.findLossCarMainByRegistNo(registNo);
			if(!"1".equals(nodeFlag) && lossCarMainVos!=null && lossCarMainVos.size()>0){
				for(PrpLDlossCarMainVo carMainVo:lossCarMainVos){
					CasecarFeeVo casecarFeeVo=new CasecarFeeVo();
					casecarFeeVo.setCasecarNo(carMainVo.getLicenseNo());
					casecarFeeVo.setFeepayMoney(carMainVo.getSumLossFee()==null?"0.00":carMainVo.getSumLossFee().toString());//????????????
					casecarFeeVo.setFeeTime(DateFormatString(carMainVo.getDeflossDate()));//????????????
					PrpLClaimTextVo claimTextVo=claimTextService.findClaimTextByLossCarMainIdAndNodeCode(carMainVo.getId(),"DLCar");
					if(claimTextVo!=null){
						casecarFeeVo.setFeeinstrucTions(claimTextVo.getDescription());
					}else{
						casecarFeeVo.setFeeinstrucTions("????????????");
					}
					if("1".equals(carMainVo.getDeflossCarType())){
						casecarFeeVo.setCasecarType("1");
					}else{
						casecarFeeVo.setCasecarType("3");
					}
					casecarFeeVos.add(casecarFeeVo);
				}
				
			}
			caseInfoVo.setCasecarFeeList(casecarFeeVos);//??????????????????
			/*--------------------------????????????????????????---------------------------------*/
			List<CaseclaimrFeeVo> caseClaimVoList=new ArrayList<CaseclaimrFeeVo>();
		     if("3".equals(nodeFlag)){//?????????????????????
		    	 CaseclaimrFeeVo cpsVo=new CaseclaimrFeeVo();
		    	 List<PrpLCompensateVo> compensateVos=compensateTaskService.findPrplCompensateByRegistNoAndPolicyNo(registNo, policyNo);
		    	 BigDecimal sumAmt=new BigDecimal("0");
		    	 if(compensateVos!=null && compensateVos.size()>0){
		    		 for(PrpLCompensateVo comVo:compensateVos){
		    			 sumAmt=sumAmt.add(comVo.getSumAmt());
		    		 }
		    	 }
		    	 if(StringUtils.isNotBlank(policyNo) && "1101".equals(policyNo.substring(11,15))){
		    		 cpsVo.setStipolicyNo(policyNo);
		    	 }else{
		    		 cpsVo.setBusipolicyNo(policyNo);
		    	 }
		    	 cpsVo.setCpsMoney(sumAmt.toString());
		    	 cpsVo.setCpsTime(DateFormatString(new Date()));
		    	 cpsVo.setCpsResult("????????????????????????"+sumAmt.toString());
		    	 caseClaimVoList.add(cpsVo);
		    }
		     caseInfoVo.setCaseclaimFeeList(caseClaimVoList);
		}
	     bodyVo.setCaseInfoVo(caseInfoVo);
	     vo.setReqHeadVo(headVo);
	     vo.setReqBodyVo(bodyVo);
		return vo;
	}
	
	/**
	 * ??????????????????
	 * Date ???????????? String??????
	 * @param strDate
	 * @return
	 * @throws ParseException
	 */
	private String DateFormatString(Date strDate){
		String str="";
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		  if(strDate!=null){
			  str=format.format(strDate);
		}
		return str;
	}
	
	/**
	 * ??????????????????
	 * @param requestXML
	 * @param urlStr
	 * @param seconds
	 * @return
	 * @throws Exception
	 */
	private String requestSelfClaim(String requestXML,String urlStr,int seconds) throws Exception {
	        long t1 = System.currentTimeMillis();
			try {
				BufferedReader reader = null;
				HttpURLConnection connection = null;
				StringBuffer buffer = new StringBuffer();
				//URL urlAdd = new URL(url);
				URL urlAdd = new URL(urlStr+URLEncoder.encode(requestXML, "utf-8"));

				try {
					connection = (HttpURLConnection) urlAdd.openConnection();
					connection.setDoInput(true);
					connection.setDoOutput(true);
					connection.setRequestMethod("POST");
					// post????????????????????????
					connection.setUseCaches(false);
					// ?????????????????????Content-Type????????????text/xml
					connection.setRequestProperty("Content-Type",
							"text/xml;charset=utf-8");
					// ???????????????
					connection.setRequestProperty("Connection", "Keep-Alive");
					connection.setConnectTimeout(seconds * 1000);
					connection.setAllowUserInteraction(true);
					connection.connect();
				} catch (Exception ex) {
					ex.printStackTrace();
					throw new Exception("????????????????????????????????????",ex);
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
						throw new Exception("????????????????????????????????????");
					}
				} catch (IOException e) {
					e.printStackTrace();
					throw new Exception("????????????????????????????????????", e);
				} finally {
					if (reader != null) {
						reader.close();
					}
				}
				return buffer.toString();
			} finally {
				logger.warn("??????({})????????????{}ms", urlStr, System.currentTimeMillis() - t1);
			}
	}
	
	/**
	 * ??????????????????????????????????????????
	 * @param reqXml
	 * @param resXml
	 * @param logVo
	 * @param flag
	 * @param errorMsg
	 */
	private  void logUtil(String reqXml,String  resXml,ClaimInterfaceLogVo logVo,String flag,String errorMsg){
	        try{
	            logger.info("<===============??????????????????===========>");
	            logVo.setBusinessType(BusinessType.SELFCLAIM_CORE_002.name());
	            logVo.setBusinessName(BusinessType.SELFCLAIM_CORE_002.getName());
	            logVo.setRequestXml(reqXml);
	            logVo.setResponseXml(resXml);
	            logVo.setCreateTime(new Date());
	            logVo.setRequestTime(new Date());
	            if("1".equals(flag)){
	                logVo.setStatus("1");
	            }else{
	                logVo.setStatus("0");
	                logVo.setErrorCode(flag);
	                logVo.setErrorMessage(errorMsg);
	                
	            }
	            

	        }catch(Exception e1){
	            e1.printStackTrace();
	        }finally{
	            if(interfaceLogService == null){
	                interfaceLogService = (ClaimInterfaceLogService)Springs.getBean(ClaimInterfaceLogService.class);
	            }
	            interfaceLogService.save(logVo);
	        }
	 }
}
