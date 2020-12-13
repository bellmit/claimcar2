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
	 * 深圳市警保信息共享平台:报案信息上传请求接口补送 报案时上传方法为sendRegistInfoToSZJB
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
			if("quartz".equals(logId)){// logId为空，执行定时任务
				Map<String,String> map = new HashMap<String,String>();
				map.put("registTaskFlag","7");// 未报案注销的报案表
				map.put("requestType1","SZReg_BI");// 上传类型 SZReg_BI
				map.put("requestType2","SZReg_CI");// 上传类型 SZReg_CI
				map.put("status1","1");// 上传成功
				map.put("status2","2");// 上传成功
				map.put("comCode","0002");// 承保机构--深圳--0002%
				Date endDate = new Date();
				long endTime = endDate.getTime();
				long startTime = endTime-24*60*60*1000;
				Date startDate = new Date(startTime);
				map.put("endDate",sdfToOracle.format(endDate));// 结束时间
				map.put("startDate",sdfToOracle.format(startDate));// 开始时间
				// map.put("startDate","2018-09-17 00:00:00");// 开始时间
				// map.put("endDate","2018-09-18 00:00:00");// 结束时间
				prpLRegistVos = registService.findPrpLRegistByHql(map);
			}else{// logId不为空，补送
				alreadyLogVo = claimInterfaceLogService.findLogByPK(Long.parseLong(logId));
				String registNo = alreadyLogVo.getRegistNo();
				PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(registNo);
				prpLRegistVos.add(prpLRegistVo);
			}
			if(prpLRegistVos!=null&& !prpLRegistVos.isEmpty()){
				for(PrpLRegistVo prpLRegistVo:prpLRegistVos){
					// 联共保 从共，从联不交互平台
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
						logVo.setOperateNode("报案");
					}
					// 组织报案上传接口的报文对象
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
							logVo.setErrorCode("成功");
						}else{
							logVo.setStatus("0");
							logVo.setErrorMessage(resJSON.get("returnMsg").toString());
						}
					}catch(Exception e){
						logger.error(prpLRegistVo.getRegistNo()+"报案上报深圳警保失败："+e);
						logVo.setStatus("0");
						logVo.setErrorMessage(e.getMessage());
					}
					finally{
						// 把之前上传失败的日志标志为已补传
						if(alreadyLogVo!=null){
							claimInterfaceLogService.changeInterfaceLog(alreadyLogVo.getId());
						}
						logVoList.add(logVo);
						// 保存当前交互日志
						//claimInterfaceLogService.save(logVo);
					}
				}
			}
		}catch(Exception e){
			logger.error("深圳警保报案上传报错："+e);
		}finally{
			//批量保存当前交互日志
			if(logVoList!=null && logVoList.size()>0){
				claimInterfaceLogService.saveAll(logVoList);
			}
			
		}
	}

	/**
	 * 组织报案上传接口的报文对象
	 * 
	 * <pre></pre>
	 * @param prpLRegistVo
	 * @return
	 * @modified: ☆Wurh(2019年3月25日 下午4:59:43): <br>
	 */
	public JSONObject getReqJson(PrpLRegistVo prpLRegistVo) {
		PrpLRegistExtVo registExtVo = prpLRegistVo.getPrpLRegistExt();
		List<PrpLRegistCarLossVo> prpLRegistCarLosses = prpLRegistVo.getPrpLRegistCarLosses();
		JSONObject jsonObject = new JSONObject();
		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		SimpleDateFormat sdfToSend = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		jsonObject.put("reportNo",prpLRegistVo.getRegistNo());// 报案号
		jsonObject.put("reportContent",prpLRegistVo.getPrpLRegistExt().getDangerRemark());// 报案内容 出险经过
		jsonObject.put("reportAddress",prpLRegistVo.getDamageAddress());// 报案地址
		jsonObject.put("insuranceCode","DHIC");// 保险公司代码
		jsonObject.put("alarmName",prpLRegistVo.getReportorName());// 报案人
		jsonObject.put("alarmPhone",prpLRegistVo.getReportorPhone());// 报案人电话
		jsonObject.put("reportTime",sdfToSend.format(prpLRegistVo.getReportTime()));// 报案时间
		jsonObject.put("isInjured",
				CodeConstants.CommonConst.TRUE.equals(registExtVo.getIsPersonLoss()) ? CodeConstants.CommonConst.YES : CodeConstants.CommonConst.NO);// 是否有人伤
		jsonObject.put("isLocalSurvey",
				CodeConstants.CheckType.Scene.equals(registExtVo.getCheckType()) ? CodeConstants.CommonConst.YES : CodeConstants.CommonConst.NO);// 是否需现场查勘
		jsonObject.put("isCarDriving",
				CodeConstants.CommonConst.TRUE.equals(registExtVo.getIsCantravel()) ? CodeConstants.CommonConst.YES : CodeConstants.CommonConst.NO);// 车辆是否能正常行使
		if(StringUtils.isNotBlank(registExtVo.getCheckAddress())){
			if(registExtVo.getCheckAddress().indexOf("省")!= -1){
				String[] strArray = registExtVo.getCheckAddress().split("省");
				jsonObject.put("surveyProvince",strArray[0]+"省");// 省
				if(strArray.length==2&&strArray[1].indexOf("市")!= -1){
					strArray = strArray[1].split("市");
					jsonObject.put("surveyCity",strArray[0]+"市");// 市
					if(strArray.length==2&&strArray[1].indexOf("区")!= -1){
						strArray = strArray[1].split("区");
						jsonObject.put("surveyArea",strArray[0]+"区");// 区
						if(strArray.length==2){
							jsonObject.put("surveyAddress",strArray[1]);// 详细道路
						}else{
							jsonObject.put("surveyAddress","无名道路");
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
		// 车牌信息
		if(prpLRegistCarLosses!=null&& !prpLRegistCarLosses.isEmpty()){
			for(PrpLRegistCarLossVo carLossVo:prpLRegistCarLosses){
				JSONObject json = new JSONObject();
				if(StringUtils.isBlank(carLossVo.getLicenseType())){
					json.put("plateType","99");
				}else{
					json.put("plateType",carLossVo.getLicenseType());// 车牌类型
				}
				json.put("plateNo",carLossVo.getLicenseNo());// 车牌号
				jsonList.add(json);
			}
		}
		jsonObject.put("plateData",jsonList);

		return jsonObject;
	}



	/**
	 * 深圳警保 报案信息上传--报案时上传
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
			logVo.setOperateNode("报案");
			logVo.setCreateUser(prpLRegistVo.getCreateUser());
			logVo.setRequestUrl(url);
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("reportNo",prpLRegistVo.getRegistNo());// 报案号
			jsonObject.put("reportContent",prpLRegistVo.getPrpLRegistExt().getDangerRemark());// 报案内容 出险经过
			jsonObject.put("reportAddress",prpLRegistVo.getDamageAddress());// 报案地址
			jsonObject.put("insuranceCode","DHIC");// 保险公司代码
			jsonObject.put("alarmName",prpLRegistVo.getReportorName());// 报案人
			jsonObject.put("alarmPhone",prpLRegistVo.getReportorPhone());// 报案人电话
			jsonObject.put("reportTime",sdfToSend.format(prpLRegistVo.getReportTime()));// 报案时间
			List<PrpLRegistCarLossVo> prpLRegistCarLosses = prpLRegistVo.getPrpLRegistCarLosses();
			List<JSONObject> jsonList = new ArrayList<JSONObject>();
			if(prpLRegistCarLosses!=null&& !prpLRegistCarLosses.isEmpty()){
				for(PrpLRegistCarLossVo carLossVo:prpLRegistCarLosses){
					JSONObject json = new JSONObject();
					if(StringUtils.isBlank(carLossVo.getLicenseType())){
						json.put("plateType","99");
					}else{
						json.put("plateType",carLossVo.getLicenseType());// 车牌类型
					}
					json.put("plateNo",carLossVo.getLicenseNo());// 车牌号
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
					logVo.setErrorMessage("成功");
					logVo.setStatus("1");
				}else{
					logVo.setErrorCode("false");
					logVo.setErrorMessage("失败");
					logVo.setStatus("0");
				}
			}catch(Exception e){
				logVo.setErrorCode("false");
				logVo.setErrorMessage("失败"+e.getMessage());
				logVo.setStatus("0");
				logger.error("深圳警保上传失败：",e);
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
			logger.error("服务器连接失败！：",e);
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
	// logVo.setOperateNode("报案");
	// logVo.setCreateUser(prpLRegistVo.getCreateUser());
	// logVo.setRequestUrl(url);
	// JSONObject jsonObject = new JSONObject();
	// jsonObject.put("reportNo",prpLRegistVo.getRegistNo());// 报案号
	// jsonObject.put("reportContent",prpLRegistVo.getPrpLRegistExt().getDangerRemark());// 报案内容 出险经过
	// jsonObject.put("reportAddress",prpLRegistVo.getDamageAddress());// 报案地址
	// jsonObject.put("insuranceCode","DHIC");// 保险公司代码
	// jsonObject.put("alarmName",prpLRegistVo.getReportorName());// 报案人
	// jsonObject.put("alarmPhone",prpLRegistVo.getReportorPhone());// 报案人电话
	// jsonObject.put("reportTime",sdfToSend.format(prpLRegistVo.getReportTime()));// 报案时间
	// List<PrpLRegistCarLossVo> prpLRegistCarLosses = prpLRegistVo.getPrpLRegistCarLosses();
	// List<JSONObject> jsonList = new ArrayList<JSONObject>();
	// if(prpLRegistCarLosses!=null&& !prpLRegistCarLosses.isEmpty()){
	// for(PrpLRegistCarLossVo carLossVo:prpLRegistCarLosses){
	// JSONObject json = new JSONObject();
	// if(StringUtils.isBlank(carLossVo.getLicenseType())){
	// json.put("plateType","99");
	// }else{
	// json.put("plateType",carLossVo.getLicenseNo());// 车牌类型
	// }
	// json.put("plateNo",carLossVo.getLicenseNo());// 车牌号
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
	// logVo.setErrorMessage("成功");
	// logVo.setStatus("2");
	// }else{
	// logVo.setErrorCode("false");
	// logVo.setErrorMessage("失败");
	// logVo.setStatus("0");
	// }
	// }catch(Exception e){
	// logVo.setErrorCode("false");
	// logVo.setErrorMessage("失败"+e.getMessage());
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
				sb.append(registNo+" : 案件不属于深圳机构 或 事故编号不为空！补送失败！\n");
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
				logVo.setOperateNode("报案");
				logVo.setCreateUser("0000000000");
				logVo.setRequestUrl(url);
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("reportNo",prpLRegistVo.getRegistNo());// 报案号
				jsonObject.put("reportContent",prpLRegistVo.getPrpLRegistExt().getDangerRemark());// 报案内容 出险经过
				jsonObject.put("reportAddress",prpLRegistVo.getDamageAddress());// 报案地址
				jsonObject.put("insuranceCode","DHIC");// 保险公司代码
				jsonObject.put("alarmName",prpLRegistVo.getReportorName());// 报案人
				jsonObject.put("alarmPhone",prpLRegistVo.getReportorPhone());// 报案人电话
				jsonObject.put("reportTime",sdfToSend.format(prpLRegistVo.getReportTime()));// 报案时间
				List<PrpLRegistCarLoss> prpLRegistCarLosses = prpLRegistVo.getPrpLRegistCarLosses();
				List<JSONObject> jsonList = new ArrayList<JSONObject>();
				if(prpLRegistCarLosses!=null&& !prpLRegistCarLosses.isEmpty()){
					for(PrpLRegistCarLoss carLossVo:prpLRegistCarLosses){
						JSONObject json = new JSONObject();
						if(StringUtils.isBlank(carLossVo.getLicenseType())){
							json.put("plateType","99");
						}else{
							json.put("plateType",carLossVo.getLicenseType());// 车牌类型
						}
						json.put("plateNo",carLossVo.getLicenseNo());// 车牌号
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
						logVo.setErrorMessage("成功");
						logVo.setStatus("2");
						sb.append(prpLRegistVo.getRegistNo()+" : 补送成功！\n");
					}else{
						logVo.setErrorCode("false");
						logVo.setErrorMessage("失败");
						logVo.setStatus("0");
						sb.append(prpLRegistVo.getRegistNo()+" : 补送失败！\n");
					}
				}catch(Exception e){
					logVo.setErrorCode("false");
					logVo.setErrorMessage("失败"+e.getMessage());
					logVo.setStatus("0");
					sb.append(prpLRegistVo.getRegistNo()+" : 补送失败！\n");
					logger.error("深圳警保上传失败：",e);
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
