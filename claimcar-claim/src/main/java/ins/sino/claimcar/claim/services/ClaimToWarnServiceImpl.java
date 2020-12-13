package ins.sino.claimcar.claim.services;

import ins.platform.utils.ClaimBaseCoder;
import ins.platform.utils.DateUtils;
import ins.platform.utils.HttpClientHander;
import ins.sino.claimcar.carinterface.service.ClaimInterfaceLogService;
import ins.sino.claimcar.carinterface.util.BusinessType;
import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;
import ins.sino.claimcar.check.service.CheckHandleService;
import ins.sino.claimcar.check.vo.PrpLCheckCarVo;
import ins.sino.claimcar.claim.service.ClaimCancelService;
import ins.sino.claimcar.claim.service.ClaimService;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.claim.vo.PrpLcancelTraceVo;
import ins.sino.claimcar.claim.vo.PrpLrejectClaimTextVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistCarLossVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.trafficplatform.service.ClaimToWarnService;
import ins.sino.claimcar.trafficplatform.service.EarlyWarnService;
import ins.sino.claimcar.trafficplatform.vo.EWClaimBasePart;
import ins.sino.claimcar.trafficplatform.vo.EWClaimBody;
import ins.sino.claimcar.trafficplatform.vo.EWClaimRequest;
import ins.sino.claimcar.trafficplatform.vo.EWFalseCaseBaseInfo;
import ins.sino.claimcar.trafficplatform.vo.EWFalseCaseBody;
import ins.sino.claimcar.trafficplatform.vo.EWFalseCaseRequest;
import ins.sino.claimcar.trafficplatform.vo.EWReqHead;
import ins.sino.claimcar.trafficplatform.vo.EWResponse;
import ins.sino.claimcar.trafficplatform.vo.EWVehicleInfo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;

import com.alibaba.dubbo.config.annotation.Service;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;

@Service(protocol = {"dubbo"}, validation = "true" , registry = {"default"})
@Path(value = "claimToWarnService")
public class ClaimToWarnServiceImpl implements ClaimToWarnService {

	private static Logger logger = LoggerFactory.getLogger(ClaimToWarnServiceImpl.class);
	
	@Autowired
	PolicyViewService policyViewService;
	@Autowired
	ClaimService claimService;
	@Autowired
	EarlyWarnService earlyWarnService;
	@Autowired
    ClaimInterfaceLogService interfaceLogService;
	@Autowired
	RegistQueryService registQueryService;
	@Autowired
	ClaimCancelService claimCancelService;
	@Autowired
	CheckHandleService checkHandleService;
	
	@Override
	public void claimToWarn(String registNo,String policyNo) {
		List<PrpLCMainVo> cMainVoList = new ArrayList<PrpLCMainVo>();
		if(policyNo == null){
			cMainVoList = policyViewService.getPolicyAllInfo(registNo);
		}else{
			PrpLCMainVo cMainVo = policyViewService.getPolicyInfo(registNo, policyNo);
			cMainVoList.add(cMainVo);
		}
		String requestXML = "";
		String responseXML = "";
		for(PrpLCMainVo cMainVo:cMainVoList){
			//山东机构才上传
			if(cMainVo.getComCode().startsWith("62")){
				String urlStr = SpringProperties.getProperty("SDWARN_URL");
				PrpLClaimVo claimVo = claimService.findClaimVoByRegistNoAndPolicyNo(registNo,cMainVo.getPolicyNo());
				ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
				try{
					if(claimVo !=null){
						requestXML = getRequestXML(cMainVo, claimVo);
						logger.info("=================立案送山东预警请求报文"+requestXML);
						responseXML = earlyWarnService.requestSDEW(requestXML, urlStr);
						logger.info("=================立案送山东预警返回报文"+responseXML);
						if(responseXML!=null && !"".equals(responseXML)){
							EWResponse responseVo = ClaimBaseCoder.xmlToObj(responseXML, EWResponse.class);
							if("1".equals(responseVo.getHead().getResponseCode())){
								logVo.setStatus("1");
							}else{
								logVo.setStatus("0");
							}
							logVo.setErrorCode(responseVo.getHead().getErrorCode());
							if(StringUtils.isNotBlank(responseVo.getHead().getErrorMessage()) && responseVo.getHead().getErrorMessage().length()>=950){
								logVo.setErrorMessage(responseVo.getHead().getErrorMessage().substring(0, 949));
							}else {
								logVo.setErrorMessage(responseVo.getHead().getErrorMessage());
							}
							
						}else{
							logVo.setStatus("0");
							logVo.setErrorMessage("没有返回信息");
						}
					}
				}catch(Exception e){
					e.printStackTrace();
					logger.info("=================立案送山东预警报错"+e);
					logVo.setStatus("0");
					if(StringUtils.isNotBlank(e.getMessage()) && e.getMessage().length()>=950){
						logVo.setErrorMessage(e.getMessage().substring(0, 949));
					}else {
						logVo.setErrorMessage(e.getMessage());
					}
		            
				}finally{
					if(claimVo !=null){
						logVo.setRequestUrl(urlStr);
						logVo.setRequestXml(requestXML);
						logVo.setResponseXml(responseXML);
						logVo.setRegistNo(claimVo.getRegistNo());
						if("1101".equals(cMainVo.getRiskCode())){
							logVo.setBusinessType(BusinessType.SDEW_claim_CI.name());
				            logVo.setBusinessName(BusinessType.SDEW_claim_CI.getName());
						}else{
							logVo.setBusinessType(BusinessType.SDEW_claim_BI.name());
				            logVo.setBusinessName(BusinessType.SDEW_claim_BI.getName());
						}
			            logVo.setOperateNode(FlowNode.Claim.name());
			            logVo.setRequestTime(new Date());
			            logVo.setCreateTime(new Date());
			            logVo.setCreateUser(claimVo.getCreateUser());
			            logVo.setClaimNo(claimVo.getClaimNo());
			            logVo.setPolicyNo(cMainVo.getPolicyNo());
			            interfaceLogService.save(logVo);
					}
				}
			}else if(cMainVo.getComCode().startsWith("10")){// 广东平台
				String urlStr = SpringProperties.getProperty("GDWARN_URL");
				PrpLClaimVo claimVo = claimService.findClaimVoByRegistNoAndPolicyNo(registNo,cMainVo.getPolicyNo());
				ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
				try{
					if(claimVo!=null){
						requestXML = getRequestGDXML(cMainVo,claimVo);
						logger.info("=================立案送广东平台请求报文"+requestXML);
						responseXML = earlyWarnService.requestSDEW(requestXML,urlStr);
						logger.info("=================立案送广东平台返回报文"+responseXML);
						if(responseXML!=null&& !"".equals(responseXML)){
							EWResponse responseVo = ClaimBaseCoder.xmlToObj(responseXML,EWResponse.class);
							if("1".equals(responseVo.getHead().getResponseCode())){
								logVo.setStatus("1");
							}else{
								logVo.setStatus("0");
							}
							logVo.setErrorCode(responseVo.getHead().getErrorCode());
							if(StringUtils.isNotBlank(responseVo.getHead().getErrorMessage()) && responseVo.getHead().getErrorMessage().length()>=950){
								logVo.setErrorMessage(responseVo.getHead().getErrorMessage().substring(0, 949));
							}else {
								logVo.setErrorMessage(responseVo.getHead().getErrorMessage());
							}
						}else{
							logVo.setStatus("0");
							logVo.setErrorMessage("没有返回信息");
						}
					}
				}catch(Exception e){
					e.printStackTrace();
					logger.info("=================立案送广东平台报错"+e);
					logVo.setStatus("0");
					if(StringUtils.isNotBlank(e.getMessage()) && e.getMessage().length()>=950){
						logVo.setErrorMessage(e.getMessage().substring(0, 949));
					}else {
						logVo.setErrorMessage(e.getMessage());
					}
				}
				finally{
					if(claimVo!=null){
						logVo.setRequestUrl(urlStr);
						logVo.setRequestXml(requestXML);
						logVo.setResponseXml(responseXML);
						logVo.setRegistNo(claimVo.getRegistNo());
						if("1101".equals(cMainVo.getRiskCode())){
							logVo.setBusinessType(BusinessType.GDEW_claim_CI.name());
							logVo.setBusinessName(BusinessType.GDEW_claim_CI.getName());
						}else{
							logVo.setBusinessType(BusinessType.GDEW_claim_BI.name());
							logVo.setBusinessName(BusinessType.GDEW_claim_BI.getName());
						}
						logVo.setOperateNode(FlowNode.Claim.name());
						logVo.setRequestTime(new Date());
						logVo.setCreateTime(new Date());
						logVo.setCreateUser(claimVo.getCreateUser());
						logVo.setClaimNo(claimVo.getClaimNo());
						logVo.setPolicyNo(cMainVo.getPolicyNo());
						interfaceLogService.save(logVo);
					}
				}
			}else if(cMainVo.getComCode().startsWith("50")){// 河南交管所平台
				String urlStr = SpringProperties.getProperty("HNWARN_URL");
				PrpLClaimVo claimVo = claimService.findClaimVoByRegistNoAndPolicyNo(registNo,cMainVo.getPolicyNo());
				ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
				try{
					if(claimVo!=null){
						requestXML = getRequestHNXML(cMainVo,claimVo);
						logger.info("=================立案送河南平台请求报文"+requestXML);
						responseXML = earlyWarnService.requestSDEW(requestXML,urlStr);
						logger.info("=================立案送河南平台返回报文"+responseXML);
						if(responseXML!=null&& !"".equals(responseXML)){
							EWResponse responseVo = ClaimBaseCoder.xmlToObj(responseXML,EWResponse.class);
							if("1".equals(responseVo.getHead().getResponseCode())){
								logVo.setStatus("1");
							}else{
								logVo.setStatus("0");
							}
							logVo.setErrorCode(responseVo.getHead().getErrorCode());
							if(StringUtils.isNotBlank(responseVo.getHead().getErrorMessage()) && responseVo.getHead().getErrorMessage().length()>=950){
								logVo.setErrorMessage(responseVo.getHead().getErrorMessage().substring(0, 949));
							}else {
								logVo.setErrorMessage(responseVo.getHead().getErrorMessage());
							}
						}else{
							logVo.setStatus("0");
							logVo.setErrorMessage("没有返回信息");
						}
					}
				}catch(Exception e){
					e.printStackTrace();
					logger.info("=================立案送河南平台报错"+e);
					logVo.setStatus("0");
					if(StringUtils.isNotBlank(e.getMessage()) && e.getMessage().length()>=950){
						logVo.setErrorMessage(e.getMessage().substring(0, 949));
					}else {
						logVo.setErrorMessage(e.getMessage());
					}
				}
				finally{
					if(claimVo!=null){
						logVo.setRequestUrl(urlStr);
						logVo.setRequestXml(requestXML);
						logVo.setResponseXml(responseXML);
						logVo.setRegistNo(claimVo.getRegistNo());
						if("1101".equals(cMainVo.getRiskCode())){
							logVo.setBusinessType(BusinessType.HNEW_claim_CI.name());
							logVo.setBusinessName(BusinessType.HNEW_claim_CI.getName());
						}else{
							logVo.setBusinessType(BusinessType.HNEW_claim_BI.name());
							logVo.setBusinessName(BusinessType.HNEW_claim_BI.getName());
						}
						logVo.setOperateNode(FlowNode.Claim.name());
						logVo.setRequestTime(new Date());
						logVo.setCreateTime(new Date());
						logVo.setCreateUser(claimVo.getCreateUser());
						logVo.setClaimNo(claimVo.getClaimNo());
						logVo.setPolicyNo(cMainVo.getPolicyNo());
						interfaceLogService.save(logVo);
					}
				}
			}
		}
	}
	
	
	/**
	 * <pre></pre>
	 * @param cMainVo
	 * @param claimVo
	 * @return
	 * @modified: ☆zhousm(2018年9月13日 上午9:25:37): <br>
	 */
	private String getRequestHNXML(PrpLCMainVo cMainVo,PrpLClaimVo claimVo) {
		String requestXML = "";
		EWClaimRequest requestVo = new EWClaimRequest();
		EWReqHead head = new EWReqHead();
		EWClaimBody body = new EWClaimBody();
		EWClaimBasePart basePart = new EWClaimBasePart();
		String user = SpringProperties.getProperty("HNWARN_USER");
		String passWord = SpringProperties.getProperty("HNWARN_PW");
		XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
		stream.autodetectAnnotations(true);
		stream.setMode(XStream.NO_REFERENCES);
		stream.aliasSystemAttribute(null,"class");

		head.setRequestType("C0302");
		head.setUser(user);
		head.setPassWord(passWord);

		basePart.setClaimSequenceNo(cMainVo.getClaimSequenceNo());
		basePart.setConfirmSequenceNo(cMainVo.getValidNo());
		basePart.setClaimNotificationNo(cMainVo.getRegistNo());
		basePart.setClaimRegistrationNo(claimVo.getClaimNo());
		basePart.setClaimRegistrationTime(DateUtils.dateToStr(claimVo.getClaimTime(),DateUtils.YMDHM));
		basePart.setEstimatedLossAmount(claimVo.getSumDefLoss());

		body.setBasePart(basePart);
		requestVo.setHead(head);
		requestVo.setBody(body);

		requestXML = "<?xml version=\"1.0\" encoding=\"GBK\"?>"+stream.toXML(requestVo);

		return requestXML;
	}

	/**
	 * 组织广东的报文
	 * 
	 * <pre></pre>
	 * @param cMainVo
	 * @param claimVo
	 * @return
	 * @modified: ☆zhousm(2018年9月13日 上午9:11:39): <br>
	 */
	private String getRequestGDXML(PrpLCMainVo cMainVo,PrpLClaimVo claimVo) {
		String requestXML = "";
		EWClaimRequest requestVo = new EWClaimRequest();
		EWReqHead head = new EWReqHead();
		EWClaimBody body = new EWClaimBody();
		EWClaimBasePart basePart = new EWClaimBasePart();
		String user = SpringProperties.getProperty("GDWARN_USER");
		String passWord = SpringProperties.getProperty("GDWARN_PW");
		XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
		stream.autodetectAnnotations(true);
		stream.setMode(XStream.NO_REFERENCES);
		stream.aliasSystemAttribute(null,"class");

		head.setRequestType("C0302");
		head.setUser(user);
		head.setPassWord(passWord);

		basePart.setClaimSequenceNo(cMainVo.getClaimSequenceNo());
		basePart.setConfirmSequenceNo(cMainVo.getValidNo());
		basePart.setClaimNotificationNo(cMainVo.getRegistNo());
		basePart.setClaimRegistrationNo(claimVo.getClaimNo());
		basePart.setClaimRegistrationTime(DateUtils.dateToStr(claimVo.getClaimTime(),DateUtils.YMDHM));
		basePart.setEstimatedLossAmount(claimVo.getSumDefLoss());

		body.setBasePart(basePart);
		requestVo.setHead(head);
		requestVo.setBody(body);

		requestXML = "<?xml version=\"1.0\" encoding=\"GBK\"?>"+stream.toXML(requestVo);
		return requestXML;
	}

	/**
	 * 组织报文
	 * @param cMainVo
	 * @param claimVo
	 * @return
	 */
	public String getRequestXML(PrpLCMainVo cMainVo,PrpLClaimVo claimVo){
		String requestXML = "";
		EWClaimRequest requestVo = new EWClaimRequest();
		EWReqHead head = new EWReqHead();
		EWClaimBody body = new EWClaimBody();
		EWClaimBasePart basePart = new EWClaimBasePart();
		String user = SpringProperties.getProperty("SDWARN_USER");
		String passWord = SpringProperties.getProperty("SDWARN_PW");
		XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
		stream.autodetectAnnotations(true);
		stream.setMode(XStream.NO_REFERENCES);
		stream.aliasSystemAttribute(null,"class");
		
		head.setRequestType("C0302");
		head.setUser(user);
		head.setPassWord(passWord);
		
		basePart.setClaimSequenceNo(cMainVo.getClaimSequenceNo());
		basePart.setConfirmSequenceNo(cMainVo.getValidNo());
		basePart.setClaimNotificationNo(cMainVo.getRegistNo());
		basePart.setClaimRegistrationNo(claimVo.getClaimNo());
		basePart.setClaimRegistrationTime(DateUtils.dateToStr(claimVo.getClaimTime(), DateUtils.YMDHM));
		basePart.setEstimatedLossAmount(claimVo.getSumDefLoss());
		
		body.setBasePart(basePart);
		requestVo.setHead(head);
		requestVo.setBody(body);
		
		requestXML = "<?xml version=\"1.0\" encoding=\"GBK\"?>"+stream.toXML(requestVo);
		
		return requestXML;
	}
	
	@Override
	public void sendFalseCaseToEWByRegist(String cancelReason, String registNo,String policyNo){
		List<PrpLCMainVo> cMainVoList = new ArrayList<PrpLCMainVo>();
		if(policyNo == null){
			cMainVoList = policyViewService.getPolicyAllInfo(registNo);
		}else{
			PrpLCMainVo cMainVo = policyViewService.getPolicyInfo(registNo, policyNo);
			cMainVoList.add(cMainVo);
		}
		PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
		String urlStr = SpringProperties.getProperty("SDWARN_URL");
		for(PrpLCMainVo cMainVo:cMainVoList){
			String requestXML = "";
			String responseXML = "";
			ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
			try{
				requestXML = getReqXML(cMainVo, registVo);
				logger.info("=================重复/虚假案件标记送山东预警请求报文"+requestXML);
				responseXML = earlyWarnService.requestSDEW(requestXML, urlStr);
				logger.info("=================重复/虚假案件标记送山东预警返回报文"+responseXML);
				if(responseXML!=null && !"".equals(responseXML)){
					EWResponse responseVo = ClaimBaseCoder.xmlToObj(responseXML, EWResponse.class);
					if("1".equals(responseVo.getHead().getResponseCode())){
						logVo.setStatus("1");
					}else{
						logVo.setStatus("0");
					}
					logVo.setErrorCode(responseVo.getHead().getErrorCode());
					logVo.setErrorMessage(responseVo.getHead().getErrorMessage());
				}else{
					logVo.setStatus("0");
					logVo.setErrorMessage("没有返回信息");
				}
			}catch(Exception e){
				e.printStackTrace();
				logger.info("=================重复/虚假案件标记送山东预警报错"+e.getMessage());
				logVo.setStatus("0");
	            logVo.setErrorMessage(e.getMessage());
			}finally{
				logVo.setRequestUrl(urlStr);
				logVo.setRequestXml(requestXML);
				logVo.setResponseXml(responseXML);
				logVo.setRegistNo(registNo);
				if("1101".equals(cMainVo.getRiskCode())){
					logVo.setBusinessType(BusinessType.SDEW_riskImg_CI.name());
		            logVo.setBusinessName(BusinessType.SDEW_riskImg_CI.getName());
				}else{
					logVo.setBusinessType(BusinessType.SDEW_riskImg_BI.name());
		            logVo.setBusinessName(BusinessType.SDEW_riskImg_BI.getName());
				}
	            logVo.setOperateNode(FlowNode.Regis.name());
	            logVo.setRequestTime(new Date());
	            logVo.setCreateTime(new Date());
	            logVo.setCreateUser(registVo.getCreateUser());
	            logVo.setFlag("1");
	            logVo.setPolicyNo(cMainVo.getPolicyNo());
	            interfaceLogService.save(logVo);
			}
			
		}
		
	}
	
	public String getReqXML(PrpLCMainVo cMainVo,PrpLRegistVo registVo){
		String reqXML = "";
		EWFalseCaseRequest request = new EWFalseCaseRequest();
		EWFalseCaseBody body = new EWFalseCaseBody();
		EWReqHead head = new EWReqHead();
		EWFalseCaseBaseInfo baseInfo = new EWFalseCaseBaseInfo();
		List<EWVehicleInfo> vehicleInfoList = new ArrayList<EWVehicleInfo>();
		XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
		stream.autodetectAnnotations(true);
		stream.setMode(XStream.NO_REFERENCES);
		stream.aliasSystemAttribute(null,"class");
		String user = SpringProperties.getProperty("SDWARN_USER");
		String passWord = SpringProperties.getProperty("SDWARN_PW");
		List<PrpLRegistCarLossVo> carList =  registVo.getPrpLRegistCarLosses();
		
		head.setRequestType("C08");
		head.setUser(user);
		head.setPassWord(passWord);
		
		baseInfo.setClaimSequenceNo(cMainVo.getClaimSequenceNo());
		//报案注销类型只有是重复报案才送山东预警
		baseInfo.setClaimType("02");
//		baseInfo.setRemark(remark);
		
		for(PrpLRegistCarLossVo carVo:carList){
			EWVehicleInfo vehicleInfo = new EWVehicleInfo();
			vehicleInfo.setVehicleProperty("1".equals(carVo.getLossparty()) ? "1":"2");
			vehicleInfo.setLicensePlateNo(carVo.getLicenseNo());
			vehicleInfo.setVin(carVo.getFrameNo()==null ? "":carVo.getFrameNo());
			vehicleInfoList.add(vehicleInfo);
		}
		
		baseInfo.setVehicleInfoList(vehicleInfoList);
		body.setBaseInfo(baseInfo);
		request.setHead(head);
		request.setBody(body);
		reqXML = "<?xml version=\"1.0\" encoding=\"GBK\"?>"+stream.toXML(request);
		
		return reqXML;
	}
	
	@Override
	public void sendFalseCaseToEWByCancel(String handleIdKey){
		
		ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
		PrpLrejectClaimTextVo rejectTextVo = claimCancelService.findByCancelClaimTextId(new BigDecimal(handleIdKey));
		PrpLcancelTraceVo cancelTraceVo = claimCancelService.findByCancelTraceId(rejectTextVo.getPrplcancelTraceId());
		PrpLClaimVo claimVo = claimService.findByClaimNo(cancelTraceVo.getClaimNo());
		PrpLCMainVo cMainVo = policyViewService.getPolicyInfo(rejectTextVo.getRegistNo(), claimVo.getPolicyNo());
		//欺诈类型是重复索赔，或者注销原因是虚假赔案
		if((("3".equals(cancelTraceVo.getDealReasoon())||"4".equals(cancelTraceVo.getDealReasoon()))&&"02".equals(cancelTraceVo.getSwindleType()))||
				(("1".equals(cancelTraceVo.getDealReasoon())||"2".equals(cancelTraceVo.getDealReasoon()))&&"8".equals(cancelTraceVo.getApplyReason()))){
			String urlStr = SpringProperties.getProperty("SDWARN_URL");
			XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
			stream.autodetectAnnotations(true);
			stream.setMode(XStream.NO_REFERENCES);
			stream.aliasSystemAttribute(null,"class");
			String requestXML = "";
			String responseXML = "";
			try{
				requestXML = getReqXMLByCancel(cancelTraceVo, rejectTextVo,claimVo,cMainVo);
				logger.info("=================重复/虚假案件标记送山东预警请求报文"+requestXML);
				responseXML = earlyWarnService.requestSDEW(requestXML, urlStr);
				logger.info("=================重复/虚假案件标记送山东预警返回报文"+responseXML);
				if(responseXML!=null && !"".equals(responseXML)){
					EWResponse responseVo = ClaimBaseCoder.xmlToObj(responseXML, EWResponse.class);
					if("1".equals(responseVo.getHead().getResponseCode())){
						logVo.setStatus("1");
					}else{
						logVo.setStatus("0");
					}
					logVo.setErrorCode(responseVo.getHead().getErrorCode());
					logVo.setErrorMessage(responseVo.getHead().getErrorMessage());
				}else{
					logVo.setStatus("0");
					logVo.setErrorMessage("没有返回信息");
				}
			}catch(Exception e){
				e.printStackTrace();
				logger.info("=================重复/虚假案件标记送山东预警报错"+e.getMessage());
				logVo.setStatus("0");
	            logVo.setErrorMessage(e.getMessage());
			}finally{
				logVo.setRequestUrl(urlStr);
				logVo.setRequestXml(requestXML);
				logVo.setResponseXml(responseXML);
				logVo.setRegistNo(rejectTextVo.getRegistNo());
				if("1101".equals(cMainVo.getRiskCode())){
					logVo.setBusinessType(BusinessType.SDEW_riskImg_CI.name());
		            logVo.setBusinessName(BusinessType.SDEW_riskImg_CI.getName());
				}else{
					logVo.setBusinessType(BusinessType.SDEW_riskImg_BI.name());
		            logVo.setBusinessName(BusinessType.SDEW_riskImg_BI.getName());
				}
	            logVo.setOperateNode(FlowNode.VClaim.name());
	            logVo.setRequestTime(new Date());
	            logVo.setCreateTime(new Date());
	            logVo.setCreateUser(rejectTextVo.getOperatorCode());
	            logVo.setClaimNo(cancelTraceVo.getClaimNo());
	            logVo.setCompensateNo(handleIdKey);
	            logVo.setFlag("2");
	            interfaceLogService.save(logVo);
			}
		}
		
	}
	
	public String getReqXMLByCancel(PrpLcancelTraceVo cancelTraceVo,PrpLrejectClaimTextVo rejectTextVo,
			PrpLClaimVo claimVo,PrpLCMainVo cMainVo){
		String registNo = rejectTextVo.getRegistNo();
		String reqXML = "";
		EWFalseCaseRequest request = new EWFalseCaseRequest();
		EWFalseCaseBody body = new EWFalseCaseBody();
		EWReqHead head = new EWReqHead();
		EWFalseCaseBaseInfo baseInfo = new EWFalseCaseBaseInfo();
		List<EWVehicleInfo> vehicleInfoList = new ArrayList<EWVehicleInfo>();
		String urlStr = SpringProperties.getProperty("");
		XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
		stream.autodetectAnnotations(true);
		stream.setMode(XStream.NO_REFERENCES);
		stream.aliasSystemAttribute(null,"class");
		String user = SpringProperties.getProperty("SDWARN_USER");
		String passWord = SpringProperties.getProperty("SDWARN_PW");
		List<PrpLCheckCarVo> checkCarVoList = checkHandleService.findPrpLcheckCarVoByRegistNo(registNo);
		
		
		head.setRequestType("C08");
		head.setUser(user);
		head.setPassWord(passWord);
		
		baseInfo.setClaimSequenceNo(cMainVo.getClaimSequenceNo());
		if(("3".equals(cancelTraceVo.getDealReasoon())||"4".equals(cancelTraceVo.getDealReasoon()))&&"02".equals(cancelTraceVo.getSwindleType())){
			baseInfo.setClaimType("03");
		}else if(("1".equals(cancelTraceVo.getDealReasoon())||"2".equals(cancelTraceVo.getDealReasoon()))&&"8".equals(cancelTraceVo.getApplyReason())){
			baseInfo.setClaimType("04");
		}
		
		for(PrpLCheckCarVo checkCarVo:checkCarVoList){
			EWVehicleInfo vehicleInfo = new EWVehicleInfo();
			vehicleInfo.setVehicleProperty("1".equals(checkCarVo.getSerialNo()) ? "1":"2");
			vehicleInfo.setLicensePlateNo(checkCarVo.getPrpLCheckCarInfo().getLicenseNo());
			vehicleInfo.setVin(checkCarVo.getPrpLCheckCarInfo().getVinNo());
			vehicleInfoList.add(vehicleInfo);
		}
		baseInfo.setVehicleInfoList(vehicleInfoList);
		body.setBaseInfo(baseInfo);
		request.setHead(head);
		request.setBody(body);
		reqXML = "<?xml version=\"1.0\" encoding=\"GBK\"?>"+stream.toXML(request);
		
		return reqXML;
	}

}
