package ins.sino.claimcar.policeSz.service.spring;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.base.po.PrpLRegist;
import ins.sino.claimcar.base.po.PrpLRegistCarLoss;
import ins.sino.claimcar.carinterface.service.ClaimInterfaceLogService;
import ins.sino.claimcar.carinterface.util.BusinessType;
import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.service.RegistService;
import ins.sino.claimcar.regist.vo.PrpLRegistCarLossVo;
import ins.sino.claimcar.regist.vo.PrpLRegistExtVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.trafficplatform.service.SzpoliceRegistService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.Path;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tc.qrpay.openapi.sdk.OpenApiClient;
import com.tc.qrpay.openapi.sdk.protocol.OpenApiReq;
import com.tc.qrpay.openapi.sdk.protocol.OpenApiRsp;

@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"}, timeout = 1000*60*10)
@Path("szpoliceRegistService")
public class SzpoliceRegistServiceImpl implements SzpoliceRegistService {

	private static Logger logger = LoggerFactory.getLogger(SzpoliceRegistServiceImpl.class);
	@Autowired
	RegistQueryService registQueryService;
	@Autowired
	ClaimInterfaceLogService claimInterfaceLogService;
	@Autowired
	RegistService registService;
	@Autowired
	DatabaseDao databaseDao;

	/**
	 * ?????????????????????????????????:???????????????????????????????????? ????????????????????????sendRegistInfoToSZJB
	 */
	@Override
	public void reportCaseUpload(String logId) {
		SimpleDateFormat sdfToOracle = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdfToSend = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		ClaimInterfaceLogVo logVo = null;
		ClaimInterfaceLogVo alreadyLogVo = null;
		List<ClaimInterfaceLogVo> logVoList=new ArrayList<ClaimInterfaceLogVo>();
		try{
			List<PrpLRegistVo> prpLRegistVos = new ArrayList<PrpLRegistVo>();
			if("quartz".equals(logId)){// logId???????????????????????????
				Map<String,String> map = new HashMap<String,String>();
				map.put("registTaskFlag","7");// ???????????????????????????
				map.put("requestType1","SZReg_BI");// ???????????? SZReg_BI
				map.put("requestType2","SZReg_CI");// ???????????? SZReg_CI
				map.put("status1","1");// ????????????
				map.put("status2","2");// ????????????
				map.put("comCode","0002");// ????????????--??????--0002%
				Date endDate = new Date();
				long endTime = endDate.getTime();
				long startTime = endTime-24*60*60*1000;
				Date startDate = new Date(startTime);
				map.put("endDate",sdfToOracle.format(endDate));// ????????????
				map.put("startDate",sdfToOracle.format(startDate));// ????????????
				// map.put("startDate","2018-09-17 00:00:00");// ????????????
				// map.put("endDate","2018-09-18 00:00:00");// ????????????
				prpLRegistVos = registService.findPrpLRegistByHql(map);
			}else{// logId??????????????????
				alreadyLogVo = claimInterfaceLogService.findLogByPK(Long.parseLong(logId));
				String registNo = alreadyLogVo.getRegistNo();
				PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(registNo);
				prpLRegistVos.add(prpLRegistVo);
			}
			if(prpLRegistVos!=null&& !prpLRegistVos.isEmpty()){
				for(PrpLRegistVo prpLRegistVo:prpLRegistVos){
					// ????????? ??????????????????????????????
					if(CodeConstants.GBFlag.MINORCOMMON.equals(prpLRegistVo.getIsGBFlag())||CodeConstants.GBFlag.MINORRELATION.equals(prpLRegistVo.getIsGBFlag())){
						continue;
					}
					if(logVo==null){
						logVo = new ClaimInterfaceLogVo();
						String url = SpringProperties.getProperty("SDWARN_URL");
						String riskCode = prpLRegistVo.getRiskCode();
						logVo.setRequestUrl(url);
						logVo.setCreateTime(new Date());
						logVo.setCreateUser(prpLRegistVo.getCreateUser());
						if(riskCode.startsWith("12")){
							logVo.setBusinessType(BusinessType.SZReg_BI.name());
							logVo.setBusinessName(BusinessType.SZReg_BI.getName());
						}else{
							logVo.setBusinessType(BusinessType.SZReg_CI.name());
							logVo.setBusinessName(BusinessType.SZReg_CI.getName());
						}
						logVo.setComCode(prpLRegistVo.getComCode());
						logVo.setCreateTime(new Date());
						logVo.setRequestTime(new Date());
						logVo.setOperateNode("??????");
					}
					// ???????????????????????????????????????
					JSONObject jsonObject = getReqJson(prpLRegistVo);
					String reqJson = jsonObject.toString();
					logVo.setRequestXml(reqJson);
					logVo.setRegistNo(prpLRegistVo.getRegistNo());
					try{
						JSONObject resJSON = sendToSZJB(jsonObject);
						logVo.setCreateUser(prpLRegistVo.getCreateUser());
						logVo.setResponseXml(resJSON.toJSONString());
						if(resJSON !=null && "0000".equals(resJSON.get("returnCode"))){
							logVo.setStatus("1");
							logVo.setErrorCode("??????");
						}else{
							logVo.setStatus("0");
							logVo.setErrorMessage(resJSON.get("returnMsg").toString());
						}
					}catch(Exception e){
						logger.error(prpLRegistVo.getRegistNo()+"?????????????????????????????????"+e);
						logVo.setStatus("0");
						logVo.setErrorMessage(e.getMessage());
					}
					finally{
						// ????????????????????????????????????????????????
						if(alreadyLogVo!=null){
							claimInterfaceLogService.changeInterfaceLog(alreadyLogVo.getId());
						}
						logVoList.add(logVo);
						// ????????????????????????
						//claimInterfaceLogService.save(logVo);
					}
				}
			}
		}catch(Exception e){
			logger.error("?????????????????????????????????"+e);
		}finally{
			//??????????????????????????????
			if(logVoList!=null && logVoList.size()>0){
				claimInterfaceLogService.saveAll(logVoList);
			}
			
		}
	}

	/**
	 * ???????????????????????????????????????
	 * 
	 * <pre></pre>
	 * @param prpLRegistVo
	 * @return
	 * @modified: ???Wurh(2019???3???25??? ??????4:59:43): <br>
	 */
	public JSONObject getReqJson(PrpLRegistVo prpLRegistVo) {
		PrpLRegistExtVo registExtVo = prpLRegistVo.getPrpLRegistExt();
		List<PrpLRegistCarLossVo> prpLRegistCarLosses = prpLRegistVo.getPrpLRegistCarLosses();
		JSONObject jsonObject = new JSONObject();
		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		SimpleDateFormat sdfToSend = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		jsonObject.put("reportNo",prpLRegistVo.getRegistNo());// ?????????
		jsonObject.put("reportContent",prpLRegistVo.getPrpLRegistExt().getDangerRemark());// ???????????? ????????????
		jsonObject.put("reportAddress",prpLRegistVo.getDamageAddress());// ????????????
		jsonObject.put("insuranceCode","DHIC");// ??????????????????
		jsonObject.put("alarmName",prpLRegistVo.getReportorName());// ?????????
		jsonObject.put("alarmPhone",prpLRegistVo.getReportorPhone());// ???????????????
		jsonObject.put("reportTime",sdfToSend.format(prpLRegistVo.getReportTime()));// ????????????
		jsonObject.put("isInjured",
				CodeConstants.CommonConst.TRUE.equals(registExtVo.getIsPersonLoss()) ? CodeConstants.CommonConst.YES : CodeConstants.CommonConst.NO);// ???????????????
		jsonObject.put("isLocalSurvey",
				CodeConstants.CheckType.Scene.equals(registExtVo.getCheckType()) ? CodeConstants.CommonConst.YES : CodeConstants.CommonConst.NO);// ?????????????????????
		jsonObject.put("isCarDriving",
				CodeConstants.CommonConst.TRUE.equals(registExtVo.getIsCantravel()) ? CodeConstants.CommonConst.YES : CodeConstants.CommonConst.NO);// ???????????????????????????
		if(StringUtils.isNotBlank(registExtVo.getCheckAddress())){
			if(registExtVo.getCheckAddress().indexOf("???")!= -1){
				String[] strArray = registExtVo.getCheckAddress().split("???");
				jsonObject.put("surveyProvince",strArray[0]+"???");// ???
				if(strArray.length==2&&strArray[1].indexOf("???")!= -1){
					strArray = strArray[1].split("???");
					jsonObject.put("surveyCity",strArray[0]+"???");// ???
					if(strArray.length==2&&strArray[1].indexOf("???")!= -1){
						strArray = strArray[1].split("???");
						jsonObject.put("surveyArea",strArray[0]+"???");// ???
						if(strArray.length==2){
							jsonObject.put("surveyAddress",strArray[1]);// ????????????
						}else{
							jsonObject.put("surveyAddress","????????????");
						}
					}
				}
			}
		}
		jsonObject.put("surveyCoordinateSystem","2");
		if(StringUtils.isNotBlank(registExtVo.getCheckAddressMapCode())){
			String[] addressCode = registExtVo.getCheckAddressMapCode().split(",");
			if(addressCode.length==2){
				jsonObject.put("surveyLongitude",addressCode[0]);
				jsonObject.put("surveyLatitude",addressCode[1]);
			}
		}
		// ????????????
		if(prpLRegistCarLosses!=null&& !prpLRegistCarLosses.isEmpty()){
			for(PrpLRegistCarLossVo carLossVo:prpLRegistCarLosses){
				JSONObject json = new JSONObject();
				if(StringUtils.isBlank(carLossVo.getLicenseType())){
					json.put("plateType","99");
				}else{
					json.put("plateType",carLossVo.getLicenseType());// ????????????
				}
				json.put("plateNo",carLossVo.getLicenseNo());// ?????????
				jsonList.add(json);
			}
		}
		jsonObject.put("plateData",jsonList);

		return jsonObject;
	}



	/**
	 * ???????????? ??????????????????--???????????????
	 */
	@Override
	public void sendRegistInfoToSZJB(String registNo) {
		ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
		SimpleDateFormat sdfToSend = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String url = SpringProperties.getProperty("SDWARN_URL");
		if(StringUtils.isNotBlank(registNo)){
			PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(registNo);
			if(CodeConstants.GBFlag.MINORCOMMON.equals(prpLRegistVo.getIsGBFlag()) || CodeConstants.GBFlag.MINORRELATION.equals(prpLRegistVo.getIsGBFlag())){
				return ;
			}
			String riskCode = prpLRegistVo.getRiskCode();
			if(riskCode.startsWith("12")){
				logVo.setBusinessType(BusinessType.SZReg_BI.name());
				logVo.setBusinessName(BusinessType.SZReg_BI.getName());
			}else{
				logVo.setBusinessType(BusinessType.SZReg_CI.name());
				logVo.setBusinessName(BusinessType.SZReg_CI.getName());
			}
			logVo.setComCode(prpLRegistVo.getComCode());
			logVo.setCreateTime(new Date());
			logVo.setRequestTime(new Date());
			logVo.setOperateNode("??????");
			logVo.setCreateUser(prpLRegistVo.getCreateUser());
			logVo.setRequestUrl(url);
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("reportNo",prpLRegistVo.getRegistNo());// ?????????
			jsonObject.put("reportContent",prpLRegistVo.getPrpLRegistExt().getDangerRemark());// ???????????? ????????????
			jsonObject.put("reportAddress",prpLRegistVo.getDamageAddress());// ????????????
			jsonObject.put("insuranceCode","DHIC");// ??????????????????
			jsonObject.put("alarmName",prpLRegistVo.getReportorName());// ?????????
			jsonObject.put("alarmPhone",prpLRegistVo.getReportorPhone());// ???????????????
			jsonObject.put("reportTime",sdfToSend.format(prpLRegistVo.getReportTime()));// ????????????
			List<PrpLRegistCarLossVo> prpLRegistCarLosses = prpLRegistVo.getPrpLRegistCarLosses();
			List<JSONObject> jsonList = new ArrayList<JSONObject>();
			if(prpLRegistCarLosses!=null&& !prpLRegistCarLosses.isEmpty()){
				for(PrpLRegistCarLossVo carLossVo:prpLRegistCarLosses){
					JSONObject json = new JSONObject();
					if(StringUtils.isBlank(carLossVo.getLicenseType())){
						json.put("plateType","99");
					}else{
						json.put("plateType",carLossVo.getLicenseType());// ????????????
					}
					json.put("plateNo",carLossVo.getLicenseNo());// ?????????
					jsonList.add(json);
				}
			}
			String reqJson = jsonObject.toString();
			logVo.setRequestXml(reqJson);
			logVo.setRegistNo(prpLRegistVo.getRegistNo());
			try{
				JSONObject resJSON = sendToSZJB(jsonObject);
				logVo.setResponseXml(resJSON.toJSONString());
				if(resJSON !=null && "0000".equals(resJSON.get("returnCode"))){
					logVo.setErrorCode("true");
					logVo.setErrorMessage("??????");
					logVo.setStatus("1");
				}else{
					logVo.setErrorCode("false");
					logVo.setErrorMessage("??????");
					logVo.setStatus("0");
				}
			}catch(Exception e){
				logVo.setErrorCode("false");
				logVo.setErrorMessage("??????"+e.getMessage());
				logVo.setStatus("0");
				logger.error("???????????????????????????",e);
			}
			finally{
				claimInterfaceLogService.save(logVo);
			}
		}
	}

	private JSONObject sendToSZJB(JSONObject jsonObject){
		String gateway = SpringProperties.getProperty("SZWARN_URL");
		String appID = SpringProperties.getProperty("SZWARN_APPID");
		String appKey = SpringProperties.getProperty("SZWARN_APPKEY");
		String method = SpringProperties.getProperty("SZWARN_METHOD_REGIST");
		OpenApiClient open = new OpenApiClient(gateway,appID,appKey,true,true);
		OpenApiReq req = open.genReqJson(method,jsonObject.toJSONString());
		OpenApiRsp res = null;
		JSONObject resJsonObject=null;
		try{
			res = open.execute(req);
			String jsonString = res.toJsonString();
			resJsonObject = JSON.parseObject(jsonString);
		}catch(Exception e){
			logger.error("???????????????????????????",e);
		}
		
		
		return resJsonObject;
	}

	// public void batchSendToSZJB(String[] bussNoArray) {
	// Set<String> set = new HashSet<String>();
	// for(String registno:bussNoArray){
	// set.add(registno.trim());
	// }
	// String[] array = set.toArray(new String[set.size()]);
	//
	// String url = SpringProperties.getProperty("SDWARN_URL");
	// SimpleDateFormat sdfToSend = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	// for(String registNo:array){
	// ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
	// PrpLRegistVo prpLRegistVo = registService.findRegistByRegistNo(registNo);
	// if(prpLRegistVo!=null){
	// if(prpLRegistVo.getComCode().startsWith("0002")){
	// String riskCode = prpLRegistVo.getRiskCode();
	// if(riskCode.startsWith("12")){
	// logVo.setBusinessType(BusinessType.SZReg_BI.name());
	// logVo.setBusinessName(BusinessType.SZReg_BI.getName());
	// }else{
	// logVo.setBusinessType(BusinessType.SZReg_CI.name());
	// logVo.setBusinessName(BusinessType.SZReg_CI.getName());
	// }
	// logVo.setComCode(prpLRegistVo.getComCode());
	// logVo.setCreateTime(new Date());
	// logVo.setRequestTime(new Date());
	// logVo.setOperateNode("??????");
	// logVo.setCreateUser(prpLRegistVo.getCreateUser());
	// logVo.setRequestUrl(url);
	// JSONObject jsonObject = new JSONObject();
	// jsonObject.put("reportNo",prpLRegistVo.getRegistNo());// ?????????
	// jsonObject.put("reportContent",prpLRegistVo.getPrpLRegistExt().getDangerRemark());// ???????????? ????????????
	// jsonObject.put("reportAddress",prpLRegistVo.getDamageAddress());// ????????????
	// jsonObject.put("insuranceCode","DHIC");// ??????????????????
	// jsonObject.put("alarmName",prpLRegistVo.getReportorName());// ?????????
	// jsonObject.put("alarmPhone",prpLRegistVo.getReportorPhone());// ???????????????
	// jsonObject.put("reportTime",sdfToSend.format(prpLRegistVo.getReportTime()));// ????????????
	// List<PrpLRegistCarLossVo> prpLRegistCarLosses = prpLRegistVo.getPrpLRegistCarLosses();
	// List<JSONObject> jsonList = new ArrayList<JSONObject>();
	// if(prpLRegistCarLosses!=null&& !prpLRegistCarLosses.isEmpty()){
	// for(PrpLRegistCarLossVo carLossVo:prpLRegistCarLosses){
	// JSONObject json = new JSONObject();
	// if(StringUtils.isBlank(carLossVo.getLicenseType())){
	// json.put("plateType","99");
	// }else{
	// json.put("plateType",carLossVo.getLicenseNo());// ????????????
	// }
	// json.put("plateNo",carLossVo.getLicenseNo());// ?????????
	// jsonList.add(json);
	// }
	// }
	// String reqJson = jsonObject.toString();
	// logVo.setRequestXml(reqJson);
	// logVo.setRegistNo(prpLRegistVo.getRegistNo());
	// JSONObject resJSON = null;
	// try{
	// resJSON = sendToSZJB(jsonObject);
	// logVo.setResponseXml(resJSON.toJSONString());
	// if("0000".equals(resJSON.get("returnCode"))){
	// logVo.setErrorCode("true");
	// logVo.setErrorMessage("??????");
	// logVo.setStatus("2");
	// }else{
	// logVo.setErrorCode("false");
	// logVo.setErrorMessage("??????");
	// logVo.setStatus("0");
	// }
	// }catch(Exception e){
	// logVo.setErrorCode("false");
	// logVo.setErrorMessage("??????"+e.getMessage());
	// logVo.setStatus("0");
	// }
	// finally{
	// boolean flag = false;
	// if("0000".equals(resJSON.get("returnCode"))){
	// List<ClaimInterfaceLogVo> changeStatus = claimInterfaceLogService.findLogByRegistNo(logVo.getRegistNo());
	// if(changeStatus!=null&& !changeStatus.isEmpty()){
	// for(ClaimInterfaceLogVo logvo:changeStatus){
	// if(logvo.getBusinessType().equals(logVo.getBusinessType())){
	// claimInterfaceLogService.changeInterfaceLog(logvo.getId());
	// flag = true;
	// }
	// }
	// if( !flag){
	// logVo.setStatus("2");
	// claimInterfaceLogService.save(logVo);
	// }
	// }
	// }else{
	// claimInterfaceLogService.save(logVo);
	// }
	// }
	// }
	// }
	// }
	// }
	public String batchSendToSZJB(String[] bussNoArray) {
		String url = SpringProperties.getProperty("SDWARN_URL");
		SimpleDateFormat sdfToSend = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		StringBuffer sb = new StringBuffer();
		List<String> registNoList = new ArrayList<String>();
		Set<String> set = new HashSet<String>();
		for(String registno:bussNoArray){
			set.add(registno.trim());
		}
		String[] array = set.toArray(new String[set.size()]);
		QueryRule query = QueryRule.getInstance();
		query.addIn("registNo",array);
		query.addLike("comCode","0002%");
		query.addIsNull("accidentNo");
		List<PrpLRegist> prpLRegists = databaseDao.findAll(PrpLRegist.class,query);
		if(prpLRegists!=null&& !prpLRegists.isEmpty()){
			for(PrpLRegist prpLRegist:prpLRegists){
				registNoList.add(prpLRegist.getRegistNo());

			}
		}
		for(String registNo:array){
			if( !registNoList.contains(registNo)){
				sb.append(registNo+" : ??????????????????????????? ??? ???????????????????????????????????????\n");
			}
		}
		if(prpLRegists!=null&& !prpLRegists.isEmpty()){
			for(PrpLRegist prpLRegistVo:prpLRegists){
				if(CodeConstants.GBFlag.MINORCOMMON.equals(prpLRegistVo.getIsGBFlag()) || CodeConstants.GBFlag.MINORRELATION.equals(prpLRegistVo.getIsGBFlag())){
					continue;
				}
				ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
				String riskCode = prpLRegistVo.getRiskCode();
				if(riskCode.startsWith("12")){
					logVo.setBusinessType(BusinessType.SZReg_BI.name());
					logVo.setBusinessName(BusinessType.SZReg_BI.getName());
				}else{
					logVo.setBusinessType(BusinessType.SZReg_CI.name());
					logVo.setBusinessName(BusinessType.SZReg_CI.getName());
				}
				logVo.setComCode(prpLRegistVo.getComCode());
				logVo.setCreateTime(new Date());
				logVo.setRequestTime(new Date());
				logVo.setOperateNode("??????");
				logVo.setCreateUser("0000000000");
				logVo.setRequestUrl(url);
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("reportNo",prpLRegistVo.getRegistNo());// ?????????
				jsonObject.put("reportContent",prpLRegistVo.getPrpLRegistExt().getDangerRemark());// ???????????? ????????????
				jsonObject.put("reportAddress",prpLRegistVo.getDamageAddress());// ????????????
				jsonObject.put("insuranceCode","DHIC");// ??????????????????
				jsonObject.put("alarmName",prpLRegistVo.getReportorName());// ?????????
				jsonObject.put("alarmPhone",prpLRegistVo.getReportorPhone());// ???????????????
				jsonObject.put("reportTime",sdfToSend.format(prpLRegistVo.getReportTime()));// ????????????
				List<PrpLRegistCarLoss> prpLRegistCarLosses = prpLRegistVo.getPrpLRegistCarLosses();
				List<JSONObject> jsonList = new ArrayList<JSONObject>();
				if(prpLRegistCarLosses!=null&& !prpLRegistCarLosses.isEmpty()){
					for(PrpLRegistCarLoss carLossVo:prpLRegistCarLosses){
						JSONObject json = new JSONObject();
						if(StringUtils.isBlank(carLossVo.getLicenseType())){
							json.put("plateType","99");
						}else{
							json.put("plateType",carLossVo.getLicenseType());// ????????????
						}
						json.put("plateNo",carLossVo.getLicenseNo());// ?????????
						jsonList.add(json);
					}
				}
				String reqJson = jsonObject.toString();
				logVo.setRequestXml(reqJson);
				logVo.setRegistNo(prpLRegistVo.getRegistNo());
				JSONObject resJSON = null;
				try{
					resJSON = sendToSZJB(jsonObject);
					logVo.setResponseXml(resJSON.toJSONString());
					if(resJSON !=null && "0000".equals(resJSON.get("returnCode"))){
						logVo.setErrorCode("true");
						logVo.setErrorMessage("??????");
						logVo.setStatus("2");
						sb.append(prpLRegistVo.getRegistNo()+" : ???????????????\n");
					}else{
						logVo.setErrorCode("false");
						logVo.setErrorMessage("??????");
						logVo.setStatus("0");
						sb.append(prpLRegistVo.getRegistNo()+" : ???????????????\n");
					}
				}catch(Exception e){
					logVo.setErrorCode("false");
					logVo.setErrorMessage("??????"+e.getMessage());
					logVo.setStatus("0");
					sb.append(prpLRegistVo.getRegistNo()+" : ???????????????\n");
					logger.error("???????????????????????????",e);
				}
				finally{
					boolean flag = false;
					if(resJSON !=null && "0000".equals(resJSON.get("returnCode"))){
						List<ClaimInterfaceLogVo> changeStatus = claimInterfaceLogService.findLogByRegistNo(logVo.getRegistNo());
						if(changeStatus!=null&& !changeStatus.isEmpty()){
							for(ClaimInterfaceLogVo logvo:changeStatus){
								if(logvo.getBusinessType().equals(logVo.getBusinessType())){
									claimInterfaceLogService.changeInterfaceLog(logvo.getId());
									flag = true;
								}
							}
							if( !flag){
								logVo.setStatus("2");
								claimInterfaceLogService.save(logVo);
							}
						}
					}else{
						claimInterfaceLogService.save(logVo);
					}
				}

			}
		}
		return sb.toString();
	}
}
