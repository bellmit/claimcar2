/******************************************************************************
* CREATETIME : 2015年12月18日 上午10:47:35
* FILE       : ins.sino.claimcar.losscar.service.ClaimFittingsInterService
******************************************************************************/
package ins.sino.claimcar.fitting.service;

import ins.framework.exception.BusinessException;
import ins.framework.service.CodeTranService;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.KINDCODE;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.PrpLCheckDutyVo;
import ins.sino.claimcar.fitting.util.OperXML;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.losscar.service.DeflossService;
import ins.sino.claimcar.losscar.vo.ClaimFittingVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarInfoVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.losscar.vo.PrpLJingYouLogVo;
import ins.sino.claimcar.losscar.vo.SendVo;
import ins.sino.claimcar.manager.service.ManagerService;
import ins.sino.claimcar.manager.vo.PrpLRepairFactoryVo;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLCItemKindVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;
import org.springframework.stereotype.Service;


/**
 * 发送请求获得精友的url
 * @author ★yangkun
 */
@Service("claimFittingInterService")
public class ClaimFittingInterService {
	
	private static Logger logger = LoggerFactory.getLogger(ClaimFittingInterService.class);
	//定损
	public static String DEFLOSSINTERFACE ="ClaimFittingInterfaceServlet";
	//核价
	public static String VERIPRICEINTERFACE ="ClaimAuditEvalInterfaceServlet";
	//核损
	public static String VERILOSSINTERFACE ="ClaimAuditLossEvalInterfaceServlet";
	
	public static String AGREEINTERFACE ="ClaimInterfaceServlet";
	
	
	@Autowired
	PolicyViewService policyViewService;
	@Autowired
	DeflossService deflossService;
	
	@Autowired
	CodeTranService codeTranService;
	@Autowired 
	CheckTaskService checkTaskService;
	@Autowired
	RegistQueryService registQueryService;
	@Autowired
	ManagerService managerService;
	
	/**
	 * 组织XML数据
	 * <pre></pre>
	 * @param claimFittingVo
	 * @throws Exception
	 * @modified:
	 * ☆yangkun(2015年12月18日 上午11:19:39): <br>
	 */
	public String sendXMLData(ClaimFittingVo claimFittingVo)throws Exception {
		
		this.organizeClaimFit(claimFittingVo);
		
		/*验证传参*/
		String errMsg = this.validParam(claimFittingVo);
		if(errMsg.trim().length()>0){
			throw new BusinessException(errMsg, true);
		}
		
		String jyUrl_web = claimFittingVo.getJyUrl();//取web层 地址
		// 创建XML
		String xmlData = createXML(claimFittingVo);
		
		logger.info(claimFittingVo.getOperateType()+",发送报文 = \n"+xmlData);
		claimFittingVo.setRequestXml(xmlData);
		
//		String JyUrl_In = SpringProperties.getProperty("JY_URL_IN");
		String returnParam=this.sendData(xmlData, claimFittingVo.getFittingsURL());
		claimFittingVo.setResponseXml(returnParam);
		if(returnParam==null || returnParam.trim().equals("")){
			claimFittingVo.setResponseXml("配件系统没有响应,请联系系统管理员!");
			this.createJingYouLog(claimFittingVo,"send","");
			throw new BusinessException("提交配件系统","配件系统没有响应,请联系系统管理员!");
		}
		String url = this.analysisErrorReasons(claimFittingVo,returnParam);
		//http://10.0.47.101:7013/pjbjbd/SaveServlet?fileName=ff808081581a3ee9015822b51e8800a5
		
		String[] urlArr = url.split("pjbjbd");
		String newUrl = jyUrl_web + urlArr[1];
		
		return newUrl;
	}
	
	public void organizeClaimFit(ClaimFittingVo claimFittingVo){
		
		PrpLDlossCarMainVo lossCarMainVo = deflossService.findDeflossByPk(Long.parseLong(claimFittingVo.getLossCarId()));
		PrpLDlossCarInfoVo carInfoVo = deflossService.findDefCarInfoByPk(lossCarMainVo.getCarId());
		lossCarMainVo.setLossCarInfoVo(carInfoVo);
		
		PrpLRegistVo registVo = registQueryService.findByRegistNo(lossCarMainVo.getRegistNo());
		if(StringUtils.isNotBlank(lossCarMainVo.getRepairFactoryCode())){
			PrpLRepairFactoryVo factoryVo = managerService.findRepariFactoryById(lossCarMainVo.getRepairFactoryCode());
			//配件折扣
			if(factoryVo==null){
				throw new IllegalArgumentException("未查询到该修理厂，请核实");
			}
			claimFittingVo.setFitDiscount(factoryVo.getCompRate());
			claimFittingVo.setRepairDiscount(factoryVo.getHourRate());
		}else{
			claimFittingVo.setFitDiscount(new BigDecimal("1"));
			claimFittingVo.setRepairDiscount(new BigDecimal("1"));
		}
		
		claimFittingVo.setRegistNo(lossCarMainVo.getRegistNo());
		claimFittingVo.setRepairCode(lossCarMainVo.getRepairFactoryCode());
		claimFittingVo.setPolicyNo(registVo.getPolicyNo());
		claimFittingVo.setInsurant(registVo.getPrpLRegistExt().getInsuredCode());
		claimFittingVo.setLossCarMainVo(lossCarMainVo);
		claimFittingVo.setCallType("certainloss");
		//复检流程加入
		if(FlowNode.DLChk.name().equals(lossCarMainVo.getFlowFlag())){
			claimFittingVo.setRecheck("1");
		}
		//核损标志
		if(CodeConstants.VeriFlag.BACK.equals(lossCarMainVo.getUnderWriteFlag())
			|| FlowNode.DLCarMod.name().equals(claimFittingVo.getNodeCode())){
			claimFittingVo.setAuditLossFlag("1");
		}
		//核价标志
		if(CodeConstants.VeriFlag.BACK.equals(lossCarMainVo.getVeriPriceFlag())){
			claimFittingVo.setAuditFlag("1");
		}
		claimFittingVo.setJgfaFlag(lossCarMainVo.getRepairFactoryType());
		
		//核损 增加是否有权限勾选复检
		if(claimFittingVo.getNodeCode().startsWith("VLCar_LV")){
//			int currencyLevel = Integer.parseInt(claimFittingVo.getNodeCode().substring("VLCar_LV".length()));
			if(CodeConstants.VeriFlag.NOSUBMIT.equals(lossCarMainVo.getVeriPriceFlag())){
				claimFittingVo.setReView("0");
			}else{
				claimFittingVo.setReView("1");	
//				if(lossCarMainVo.getVerifyLevel()!=null && currencyLevel >= lossCarMainVo.getVerifyLevel()){
//					claimFittingVo.setReView("1");		
//				}else{
//					claimFittingVo.setReView("0");
//				}
			}
		}
		
		String localAreaCode =claimFittingVo.getLocalAreaCode();
		if("00".equals(localAreaCode.substring(0,2))){
			localAreaCode = localAreaCode.substring(0,4) + "0000"; //取分公司机构代码
		}else{
			localAreaCode = localAreaCode.substring(0,2) + "000000"; //取分公司机构代码
		}
		String localAreaName=  codeTranService.transCode("ComCode",localAreaCode);
		claimFittingVo.setLocalAreaName(localAreaName);
		claimFittingVo.setLocalAreaCode(localAreaCode);
		
	}
	
	public void saveJingYouLog(PrpLDlossCarMainVo lossCarMainVo,String dataXml,String operateType){
		ClaimFittingVo claimFittingVo = new ClaimFittingVo();
		claimFittingVo.setRegistNo(lossCarMainVo.getRegistNo());
		claimFittingVo.setLossCarId(lossCarMainVo.getId().toString());
		claimFittingVo.setRequestXml(dataXml);
		claimFittingVo.setOperateType(operateType);
		
		this.createJingYouLog(claimFittingVo,"accp","");
	}
	
	
	/**
	 * 保存精友交互日志
	 * ☆yangkun(2016年2月20日 下午7:36:18): <br>
	 */
	public void createJingYouLog(ClaimFittingVo claimFittingVo,String logType,String reUrl) {
		String sendXml = claimFittingVo.getRequestXml();
		String reXml = claimFittingVo.getResponseXml();
		if( sendXml!=null && sendXml.length()>=3000){
			sendXml=sendXml.substring(0,2998);
		}
		if( reXml!=null && reXml.length()>=3000){
			reXml=reXml.substring(0,2998);
		}
		if( reUrl!=null && reUrl.length()>=3000){
			reUrl=reUrl.substring(0,2998);
		}
		
		PrpLJingYouLogVo logVo = new PrpLJingYouLogVo();
		logVo.setRegistno(claimFittingVo.getRegistNo());
		logVo.setUserCode(claimFittingVo.getOperatorCode());
		logVo.setLossNo(Long.parseLong(claimFittingVo.getLossCarId()));
		logVo.setOptType(claimFittingVo.getOperateType());
		logVo.setSendXml(sendXml);
		logVo.setLogType(logType);
		logVo.setReXml(reXml);
		logVo.setReUrl(reUrl);
		logVo.setCreateTime(new Date());
		
		deflossService.saveLog(logVo);
	}

	/**
	 * 核价同意定损交互,核损同意核价交互
	 * @throws Exception  核价 verifyPrice  核损verifyLoss
	 * @modified:
	 * ☆yangkun(2016年2月3日 上午11:46:39): <br>
	 */
	public String agreeToJy(String operateType,String registNo,String lossNo,String jyUrl) throws Exception{
		ClaimFittingVo claimFittingVo = new ClaimFittingVo();
		claimFittingVo.setRegistNo(registNo);
		claimFittingVo.setLossCarId(lossNo);
//		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		// 创建XML
		String xmlData = createAgreeXML(operateType,registNo,lossNo);
		if("verifyPrice".equals(operateType)){
			claimFittingVo.setOperateType("agreePrice");
		}else{
			claimFittingVo.setOperateType("agreeVeriy");
		}
		claimFittingVo.setRequestXml(xmlData);
		logger.info("同意核价或核损，发送报文 sendXml="+xmlData);
		
//		SendVo sendVo = new SendVo();
//		sendVo.setServerName(request.getServerName());
//		String jyUrl = getURL("JY_URL",sendVo);
		jyUrl = SpringProperties.getProperty("JY_URL_IN");//用内网的地址
		try{
			String returnParam=this.sendData(xmlData, jyUrl+"/"+AGREEINTERFACE);
			claimFittingVo.setResponseXml(returnParam);
			
			OperXML operXML = new OperXML();
			operXML.parserFromXMLString(returnParam);
			String RESPONSE_CODE = operXML.getElement("CODE").getValue();
			String ERROR_MESSAGE = operXML.getElement("MESSAGE").getValue();
			String returnCode = "0";
			if("000".equals(RESPONSE_CODE)){// 如果返回成功 
				returnCode="1";
				//记录日志
				this.createJingYouLog(claimFittingVo,"send","");
			}else{//失败抛出异常，及失败原因
				claimFittingVo.setResponseXml("提交配件系统出现错误,错误代码："+RESPONSE_CODE+"错误原因:"+ERROR_MESSAGE);
				this.createJingYouLog(claimFittingVo,"send","");;
				throw new BusinessException("提交配件系统出现错误,错误代码："+RESPONSE_CODE+"错误原因:"+ERROR_MESSAGE,true);
			}
			return returnCode;
		}catch(Exception e){
			claimFittingVo.setResponseXml(e.getMessage());
			this.createJingYouLog(claimFittingVo,"send","");;
			throw new BusinessException(e.getMessage(),true);
		}
		
		
	}
	/**
	 * Type 核价同意定损为1，核损同意为核价2
	 * ☆yangkun(2016年2月3日 上午11:51:13): <br>
	 */
	private String createAgreeXML(String operateType,String registNo,String lossNo) {
		StringBuffer xmlData=new StringBuffer();
		xmlData.append("<?xml version=\"1.0\" encoding=\"GBK\"?>");
		xmlData.append("<LossRtnRequestData>");
		xmlData.append("<LossNo>").append(lossNo).append("</LossNo>");//lossNO使用定损主表id
		xmlData.append("<CaseNo>").append(registNo).append("</CaseNo>");
		if("verifyPrice".equals(operateType)){
			xmlData.append("<Type>").append("1").append("</Type>");
		}else{
			xmlData.append("<Type>").append("2").append("</Type>");
		}
		xmlData.append("<URL>").append("url").append("</URL>");
		xmlData.append("</LossRtnRequestData>");
		
		return xmlData.toString();
	}


	private String createXML(ClaimFittingVo claimFittingVo) throws Exception{
		String xmlData =null;
		if("certa".equals(claimFittingVo.getOperateType())){
			this.initRealUrl(claimFittingVo,claimFittingVo.getOperateType());
			xmlData= this.makeCertaSendXMLData(claimFittingVo);
		}
		else if("verifyPrice".equals(claimFittingVo.getOperateType())){
			this.initRealUrl(claimFittingVo,claimFittingVo.getOperateType());
			xmlData= this.makeCheckPriceSendXMLData(claimFittingVo);
		}
		else if("verifyLoss".equals(claimFittingVo.getOperateType())){
			this.initRealUrl(claimFittingVo,claimFittingVo.getOperateType());
			xmlData= this.makeCheckLossSendXMLData(claimFittingVo);
		}
		return xmlData;
	}
	
	/**
	 * 核价请求交互
	 * ☆yangkun(2016年2月19日 下午6:57:24): <br>
	 */
	public String makeCheckPriceSendXMLData(ClaimFittingVo claimFittingVo) throws Exception {
		String registNo = claimFittingVo.getRegistNo();
		String lossNo = claimFittingVo.getLossCarId();
		String callType = claimFittingVo.getCallType();
		String operateType=claimFittingVo.getOperateType();
		String operatorCode = claimFittingVo.getOperatorCode();
		String operatorName = codeTranService.transCode("UserCode",operatorCode);
		String returnURL = claimFittingVo.getReturnURL();
		String refreshURL = claimFittingVo.getRefreshURL();
		String localAreaCode=claimFittingVo.getLocalAreaCode();
		String localName=claimFittingVo.getLocalAreaName();
		
		StringBuffer xmlData=new StringBuffer();
		xmlData.append("<?xml version=\"1.0\" encoding=\"GBK\"?>");
		xmlData.append("<LossReqRequestData>");
		xmlData.append("<LossReqHead>");
		xmlData.append("<UserCode>jy</UserCode>");
		xmlData.append("<Password>jy</Password>");
		xmlData.append("<RequestType>003</RequestType>");
		xmlData.append("<Power>1010</Power>");
		
		xmlData.append("<ReturnURL>").append(returnURL).append("?operateType=").append(operateType).append("</ReturnURL>");
		xmlData.append("<RefreshURL>").append(refreshURL).append("?registNo=").append(registNo).append("#lossNo=").append(lossNo).append("#callType=").append(callType);
		xmlData.append("#operateType=").append(operateType).append("</RefreshURL>");
		xmlData.append("</LossReqHead>");
		xmlData.append("<LossReqBody>");
		xmlData.append("<LossNo>").append(lossNo).append("</LossNo>");
		xmlData.append("<CaseNo>").append(registNo).append("</CaseNo>");
		xmlData.append("<AuditComCode>").append(localAreaCode).append("</AuditComCode>");
		xmlData.append("<AuditComName>").append(localName).append("</AuditComName>");
		xmlData.append("<AuditHandlerCode>").append(operatorCode).append("</AuditHandlerCode>");
		xmlData.append("<AuditHandlerName>").append(operatorName).append("</AuditHandlerName>");
		String rate= SpringProperties.getProperty("FITTINGS_CHECKPRICE_RATE");
		if(rate==null) rate="1.0";
		xmlData.append("<AuditPriceRate>").append(rate).append("</AuditPriceRate>");
		xmlData.append("<FitDiscount>").append(claimFittingVo.getFitDiscount()).append("</FitDiscount>");
		xmlData.append("<RepairDiscount>").append(claimFittingVo.getRepairDiscount()).append("</RepairDiscount>");
		
		xmlData.append("</LossReqBody>");
		xmlData.append("</LossReqRequestData>");
		
		return xmlData.toString();
	}
	
	/**
	 * 组织XML数据
	 * 核损请求交互
	 */
	public String makeCheckLossSendXMLData(ClaimFittingVo claimFittingVo) throws Exception {

		String registNo = claimFittingVo.getRegistNo();
		String lossNo = claimFittingVo.getLossCarId();
		String localAreaCode = claimFittingVo.getLocalAreaCode();
		String callType = claimFittingVo.getCallType();
		String operateType=claimFittingVo.getOperateType();
		
		String operatorCode = claimFittingVo.getOperatorCode();
		String operatorName = codeTranService.transCode("UserCode",operatorCode);
		String returnURL = claimFittingVo.getReturnURL();
		String refreshURL = claimFittingVo.getRefreshURL();

		StringBuffer xmlData=new StringBuffer();
		xmlData.append("<?xml version=\"1.0\" encoding=\"GBK\"?>");
		xmlData.append("<LossReqRequestData>");
		xmlData.append("<LossReqHead>");
		xmlData.append("<UserCode>jy</UserCode>");
		xmlData.append("<Password>jy</Password>");
		xmlData.append("<RequestType>003</RequestType>");
		xmlData.append("<Power>1111</Power>");
		
		xmlData.append("<ReturnURL>").append(returnURL).append("?operateType=").append(operateType).append("</ReturnURL>");
		xmlData.append("<RefreshURL>").append(refreshURL).append("?registNo=").append(registNo).append("#lossNo=").append(lossNo).append("#callType=").append(callType);
		xmlData.append("#operateType=").append(operateType).append("</RefreshURL>");
		xmlData.append("</LossReqHead>");
		xmlData.append("<LossReqBody>");
		xmlData.append("<LossNo>").append(lossNo).append("</LossNo>");
		xmlData.append("<CaseNo>").append(registNo).append("</CaseNo>");
		xmlData.append("<AuditLossComCode>"+localAreaCode+"</AuditLossComCode>");
		xmlData.append("<AuditLossComName>").append(claimFittingVo.getLocalAreaName()).append("</AuditLossComName>");
		xmlData.append("<AuditLossHandlerCode>"+operatorCode+"</AuditLossHandlerCode>");
		xmlData.append("<AuditLossHandlerName>"+operatorName+"</AuditLossHandlerName>");
		String rate= SpringProperties.getProperty("FITTINGS_CHECKPRICE_RATE");
		if( rate==null) rate="1.0";
		xmlData.append("<AuditLossPriceRate>").append(rate).append("</AuditLossPriceRate>");
		xmlData.append("<FitDiscount>").append(claimFittingVo.getFitDiscount()).append("</FitDiscount>");
		xmlData.append("<RepairDiscount>").append(claimFittingVo.getRepairDiscount()).append("</RepairDiscount>");
		
		//Review claimFittingVo.getReView()
		xmlData.append("<Review>").append(claimFittingVo.getReView()).append("</Review>");
		xmlData.append("</LossReqBody>");
		xmlData.append("</LossReqRequestData>");
		
		
		return xmlData.toString();
	}

	/**
	 *  对封装地址进行内外网封装
	 **/
	public void initRealUrl(ClaimFittingVo claimFittingVo,String operateType){
		SendVo sendVo = claimFittingVo.getSendVo();
		String claimUrl = "http://" + sendVo.getServerName()
			+ ":"+ sendVo.getServerPort()
			+ sendVo.getContextPath();
		String fittingsURL = SpringProperties.getProperty("JY_URL_IN");//getURL("JY_URL",sendVo);
		//暂时这么取 理赔内网地址 ，之后配置到fram的app文件中 
		String claimUrl_In = SpringProperties.getProperty("CLAIM_URL_IN");
		claimFittingVo.setReturnURL(claimUrl_In+"/fittings");
		claimFittingVo.setRefreshURL(claimUrl+"/reloadFittings");
		
		
		
		if("certa".equals(claimFittingVo.getOperateType())){
			claimFittingVo.setFittingsURL(fittingsURL+"/"+DEFLOSSINTERFACE);
		}
		else if("verifyPrice".equals(claimFittingVo.getOperateType())){
			claimFittingVo.setFittingsURL(fittingsURL+"/"+VERIPRICEINTERFACE);
		}
		else if("verifyLoss".equals(claimFittingVo.getOperateType())){
			claimFittingVo.setFittingsURL(fittingsURL+"/"+VERILOSSINTERFACE);
		}
		
	}
	
	/**
	 * 获得真实URL
	 * 
	 * @param sysconst
	 * @param request
	 * @return
	 */
//	private String getURL(String sysconst,SendVo sendVo) {
//		String rightURL = SpringProperties.getProperty(sysconst);
////		String outIP = SpringProperties.getProperty("OUT_IP");
////		String serverIP = sendVo.getServerName();
////		if(serverIP.equals(outIP)){// 是外网
////			rightURL = SpringProperties.getProperty(sysconst+"_OUT");
////		}else{
////			rightURL = url;
////		}
//		logger.debug(rightURL);
//		return rightURL;
//	}
	
	/**
	 * 定损请求交互
	 * ☆yangkun(2016年2月19日 下午5:08:02): <br>
	 */
	public String makeCertaSendXMLData(ClaimFittingVo claimFittingVo) throws Exception {
		PrpLDlossCarMainVo lossCarMainVo = claimFittingVo.getLossCarMainVo();
		PrpLDlossCarInfoVo carInfoVo = lossCarMainVo.getLossCarInfoVo();
		String lossCarId = claimFittingVo.getLossCarId();
		String registNo = claimFittingVo.getRegistNo();
		String localAreaCode = claimFittingVo.getLocalAreaCode();
		String callType = claimFittingVo.getCallType();
		String operateType=claimFittingVo.getOperateType();
		String operatorCode = claimFittingVo.getOperatorCode();
		String operatorName = codeTranService.transCode("UserCode",operatorCode);
		String returnURL = claimFittingVo.getReturnURL();
		String refreshURL = claimFittingVo.getRefreshURL();
		
		Integer serialNo = lossCarMainVo.getSerialNo();
		String licenseNo = carInfoVo.getLicenseNo();
		

		HashMap<String,String> itemKindMap = new HashMap<String,String>();
		PrpLCheckDutyVo checkDutyVo = checkTaskService.findCheckDuty(registNo,1);
		List<PrpLCItemKindVo> itemKinds = policyViewService.findItemKinds(registNo,null);
		PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
		if(serialNo==1){ 
			if("1".equals(checkDutyVo.getIsClaimSelf()) || CodeConstants.ReportType.CI.equals(registVo.getReportType())){
				itemKindMap.put(KINDCODE.KINDCODE_BZ,CodeConstants.KINDCODE_MAP.get(KINDCODE.KINDCODE_BZ));
			}else{
				if(lossCarMainVo.getLossFeeType()!=null){
					String kindCode ="";
					if(CodeConstants.ISNEWCLAUSECODE2020_MAP.get(lossCarMainVo.getRiskCode()) != null &&
							CodeConstants.ISNEWCLAUSECODE2020_MAP.get(lossCarMainVo.getRiskCode())){
						kindCode = CodeConstants.LossFee2020Kind_Map.get(lossCarMainVo.getRiskCode()+lossCarMainVo.getLossFeeType());
					}else{
						kindCode = CodeConstants.LossFeeKind_Map.get(lossCarMainVo.getLossFeeType());
					}
					
					itemKindMap.put(kindCode,CodeConstants.KINDCODE_MAP.get(kindCode));
				}else{
					itemKindMap.put(KINDCODE.KINDCODE_BZ,CodeConstants.KINDCODE_MAP.get(KINDCODE.KINDCODE_BZ));
				}
			}
		}else{
			for(PrpLCItemKindVo prpCitemKindVo:itemKinds){
				String kindCode = prpCitemKindVo.getKindCode();
				if("B".equals(kindCode) || "BZ".equals(kindCode)){
					itemKindMap.put(prpCitemKindVo.getKindCode(),prpCitemKindVo.getKindName());
				}
			}
		}
		
		String changeFlag="1";//是否可以换车型
		StringBuffer xmlData=new StringBuffer();
		xmlData.append("<?xml version=\"1.0\" encoding=\"GBK\"?>");
		xmlData.append("<LossReqRequestData>");
		xmlData.append("<LossReqHead>");
		xmlData.append("<UserCode>jy</UserCode>");
		xmlData.append("<Password>jy</Password>");
		xmlData.append("<RequestType>001</RequestType>");
		if("1".equals(claimFittingVo.getRecheck())){
			xmlData.append("<Power>111000111</Power>");//复检只有配件权限
		}else{
			xmlData.append("<Power>111110111</Power>");
		}
		
		xmlData.append("<AuditFlag>").append(claimFittingVo.getAuditFlag()).append("</AuditFlag>");//核价标记
		xmlData.append("<AuditLossFlag>").append(claimFittingVo.getAuditLossFlag()).append("</AuditLossFlag>");
		xmlData.append("<Recheck>").append(claimFittingVo.getRecheck()).append("</Recheck>");
		xmlData.append("<JgfaFlag>").append(claimFittingVo.getJgfaFlag()).append("</JgfaFlag>");
		xmlData.append("<ReturnURL>").append(returnURL).append("?operateType=").append(operateType).append("</ReturnURL>");
		xmlData.append("<RefreshURL>").append(refreshURL).append("?registNo=").append(registNo).append("#lossNo=").append(lossCarId).append("#callType=").append(callType);
		xmlData.append("#operateType=").append(operateType).append("</RefreshURL>");
		xmlData.append("<Change>").append(changeFlag).append("</Change>");
		xmlData.append("</LossReqHead>");
		
		xmlData.append("<LossInsureItem>");
		Iterator<String> keys = itemKindMap.keySet().iterator(); 
		while(keys.hasNext()){
			String itemCode = (String)keys.next();
			xmlData.append("<Item>");
			xmlData.append("<InsureTerm>").append(itemKindMap.get(itemCode)).append("</InsureTerm>");
			xmlData.append("<InsureTermCode>").append(itemCode).append("</InsureTermCode>");
			xmlData.append("</Item>");
		}
		xmlData.append("</LossInsureItem>");
		
		xmlData.append("<LossReqBody>");
		xmlData.append("<LossNo>").append(lossCarId).append("</LossNo>");//lossNO使用定损主表id
		xmlData.append("<CaseNo>").append(registNo).append("</CaseNo>");
		xmlData.append("<EstimateNo>").append(serialNo.toString()).append("</EstimateNo>");
		xmlData.append("<PlateNo>").append(licenseNo).append("</PlateNo>");
		xmlData.append("<MarkColor></MarkColor>");
		xmlData.append("<PlateColor></PlateColor>");
		xmlData.append("<EngineNo></EngineNo>");
		xmlData.append("<FrameNo></FrameNo>");
		//传入车型 到精友
		xmlData.append("<InsureVehicleName>"+carInfoVo.getModelName()+"</InsureVehicleName>");
		xmlData.append("<InsureVehicleCode>"+carInfoVo.getModelCode()+"</InsureVehicleCode>");
		xmlData.append("<ComCode>").append(localAreaCode).append("</ComCode>");
		xmlData.append("<Company>").append(claimFittingVo.getLocalAreaName()).append("</Company>");
		xmlData.append("<BranchComCode></BranchComCode>");
		xmlData.append("<BranchComName></BranchComName>");
		xmlData.append("<HandlerCode>").append(operatorCode).append("</HandlerCode>");
		xmlData.append("<HandlerName>").append(operatorName).append("</HandlerName>");
		xmlData.append("<ManHour>0</ManHour>");
		
		xmlData.append("<ManHour>0</ManHour>");
		xmlData.append("<FitDiscount>").append(claimFittingVo.getFitDiscount()).append("</FitDiscount>");
		xmlData.append("<RepairDiscount>").append(claimFittingVo.getRepairDiscount()).append("</RepairDiscount>");
		xmlData.append("<RepairCode>").append(claimFittingVo.getRepairCode()).append("</RepairCode>");
		xmlData.append("</LossReqBody>");
		xmlData.append("</LossReqRequestData>");
		
		return xmlData.toString();
	}
	
	//HttpServletRequest request
	private String sendData(String xmlData,String fittingURL) throws Exception {
		String SERVER_URL = fittingURL+"?Encoding=GBK";
		String seconds = SpringProperties.getProperty("HTTP_TIMEOUT");
		if(StringUtils.isBlank(seconds)){
			seconds = "20";
		}
		// 开启连接
		URL uploadServlet = new URL(SERVER_URL);
		URLConnection servletConnection = uploadServlet.openConnection();
		logger.debug("请求配件系统URL:"+servletConnection.getURL());
		// 设置连接参数
		servletConnection.setUseCaches(false);
		servletConnection.setDoOutput(true);
		servletConnection.setDoInput(true);
		servletConnection.setConnectTimeout(Integer.valueOf(seconds) * 1000);
		servletConnection.setReadTimeout(Integer.valueOf(seconds) * 1000);
		// 开启流，写入XML数据
		BufferedOutputStream output = new BufferedOutputStream(servletConnection.getOutputStream());
		System.out.println("degnbsfdsf \n"+xmlData);
		output.write(xmlData.getBytes("GBK"));
		output.close();

		try{
			// 接收返回参数
			DataInputStream input = null;
			input = new DataInputStream(servletConnection.getInputStream());
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			byte[] data = null;
			byte[] b = new byte[1024];
			int read = 0;
			while(( read = input.read(b) )>0){
				byteOut.write(b,0,read);
			}
			data = byteOut.toByteArray();
			String dataString = new String(data);
			logger.debug("接收dataString___:"+dataString);
//			logger.debug("接收icode___:"+input.read(b));
			input.close();
			return dataString;
		}catch(Exception e){
			logger.warn("数据已经发送，但没有返回结果！");
			throw e;
		}
	
	}
	
	public String analysisErrorReasons(ClaimFittingVo claimFittingVo,String returnParam) throws Exception {
		OperXML operXML = new OperXML();
		operXML.parserFromXMLString(returnParam);
		String RESPONSE_CODE = operXML.getElement("RESPONSE_CODE").getValue();
		String ERROR_MESSAGE = operXML.getElement("ERROR_MESSAGE").getValue();
		String url = "";
		if("000".equals(RESPONSE_CODE)){// 如果返回成功 重定向到定损系统返回的服务url
			url = operXML.getElement("URL").getValue();
			url = transRedirectURL(url,claimFittingVo.getSendVo());
			//记录日志
			this.createJingYouLog(claimFittingVo,"send",url);
		}
		else{//失败抛出异常，及失败原因
			//记录日志
			claimFittingVo.setResponseXml("提交配件系统出现错误,错误代码："+RESPONSE_CODE+"错误原因:"+ERROR_MESSAGE);
			this.createJingYouLog(claimFittingVo,"send",url);
			throw new BusinessException("提交配件系统出现错误,错误代码："+RESPONSE_CODE+"错误原因:"+ERROR_MESSAGE,true);
		}
		
		return url;
	}

	/** 验证请求参数*/
	private String validParam(ClaimFittingVo claimFittingVo){
		String errMsg=""; 
		if( isEmpty(claimFittingVo.getRegistNo())){ 
			 errMsg = "缺少报案号registNo,请与系统管理员联系";
		}
		else if( isEmpty(claimFittingVo.getLossCarId())){ 
			 errMsg ="缺少损失车辆定损代码lossCarId,请与系统管理员联系"; 
		}
		else if( isEmpty( claimFittingVo.getOperateType())){
			errMsg="缺少查询类型operateType,请与系统管理员联系";
		}
		else{}
		
		return errMsg;
	}
	
	/** 参数是否为空 */
	private boolean isEmpty(String value){
		boolean empty=false;
		if( value==null || value.trim().length()==0)
			empty=true;
		return empty;
	}
	
	/**
	 * 是外网的时候要转换url
	 * 
	 * @param url
	 * @param request
	 * @return
	 */
	private String transRedirectURL(String url,SendVo sendVo) {

		String redirectURL = "";
		String serverIP = sendVo.getServerName();
		String outIP = SpringProperties.getProperty("OUT_IP");
		if(serverIP.equals(outIP)){// 是外网
			int index = url.indexOf("/pjbjbd");
			redirectURL = "http://"+outIP+":"+SpringProperties.getProperty("OUT_Port")+url.substring(index);
		}else{
			redirectURL = url;
		}
		return redirectURL;
	}
	
	private String getProperties(String key) {
		Properties properties = new Properties();
		try {
			properties.load(this.getClass().getResourceAsStream("/config/appConfig.properties"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		String value = properties.getProperty(key);
		
		return value;
	}
	
	
	public static void main(String[] args) {
		ClaimFittingInterService service = new ClaimFittingInterService();
		String str = service.getProperties("FITTINGS_CHECKPRICE_RATE");
		
		System.out.println(str);
		
	}
}
