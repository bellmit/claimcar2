package ins.sino.claimcar.realtimequery.service;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import ins.framework.utils.Beans;
import ins.platform.common.service.facade.BillNoService;
import ins.platform.vo.PrpLConfigValueVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.PrpLCheckDriverVo;
import ins.sino.claimcar.claim.service.ClaimTaskService;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.commom.vo.AesUtils;
import ins.sino.claimcar.constant.Risk;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.realtimequery.vo.PrpLAntiFraudVo;
import ins.sino.claimcar.realtimequery.vo.PrpLBasicsInfoVo;
import ins.sino.claimcar.realtimequery.vo.PrpLCasualtyInforVo;
import ins.sino.claimcar.realtimequery.vo.PrpLDamageInfoVo;
import ins.sino.claimcar.realtimequery.vo.PrpLInjuredDetailsVo;
import ins.sino.claimcar.realtimequery.vo.PrpLPropertyLossVo;
import ins.sino.claimcar.realtimequery.vo.PrpLRealTimeQueryVo;
import ins.sino.claimcar.realtimequery.vo.PrpLVehicleInfoVo;
import ins.sino.claimcar.realtimequery.vo.vehicle.AntiFraud;
import ins.sino.claimcar.realtimequery.vo.vehicle.BasicsInfo;
import ins.sino.claimcar.realtimequery.vo.vehicle.CasualtyInfor;
import ins.sino.claimcar.realtimequery.vo.vehicle.DamageInfo;
import ins.sino.claimcar.realtimequery.vo.vehicle.HistoryClaims;
import ins.sino.claimcar.realtimequery.vo.vehicle.InjuredDetails;
import ins.sino.claimcar.realtimequery.vo.vehicle.PersonHistoryClaims;
import ins.sino.claimcar.realtimequery.vo.vehicle.PersonInfoRes;
import ins.sino.claimcar.realtimequery.vo.vehicle.PersonInfoResBody;
import ins.sino.claimcar.realtimequery.vo.vehicle.PropertyLoss;
import ins.sino.claimcar.realtimequery.vo.vehicle.ReportPhoneHistoryClaims;
import ins.sino.claimcar.realtimequery.vo.vehicle.ReportPhoneInfoRes;
import ins.sino.claimcar.realtimequery.vo.vehicle.ReportPhoneInfoResBody;
import ins.sino.claimcar.realtimequery.vo.vehicle.ResponseErrorInfo;
import ins.sino.claimcar.realtimequery.vo.vehicle.TeleAgg;
import ins.sino.claimcar.realtimequery.vo.vehicle.VehicleInfo;
import ins.sino.claimcar.realtimequery.vo.vehicle.VehicleInfoReq;
import ins.sino.claimcar.realtimequery.vo.vehicle.VehicleInfoRes;
import ins.sino.claimcar.realtimequery.vo.vehicle.VehicleInfoResBody;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;
import org.apache.commons.lang.StringUtils;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;

@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path("vehicleInfoQueryService")
public class VehicleInfoQueryServiceImpl implements VehicleInfoQueryService {
	private Logger logger = LoggerFactory.getLogger(VehicleInfoQueryService.class);
	@Autowired
	PolicyViewService policyViewService;
	@Autowired
	ClaimTaskService claimTaskService;
	@Autowired
	RegistQueryService registQueryService;
	@Autowired
	BillNoService billNoService;
	@Autowired
	RealTimeQueryServiceImpl realTimeQueryServiceImpl;
	@Autowired
	WfTaskHandleService wfTaskHandleService;
	@Autowired
	CheckTaskService checkTaskService;
	/**
	 * 4.1.1.车辆信息查询接口
	 */
	@Override
	public void sendVehicleInfoQuery(PrpLWfTaskVo wfTaskVo, PrpLConfigValueVo configValueVo) {
		String registNo = wfTaskVo.getRegistNo();
		//Remark : DHIC440000-KzZ850W55nCW-440000   用户名-密码-地区
		if(StringUtils.isBlank(configValueVo.getRemark())){
			logger.info("获取开关配置信息失败！");
			return;
		}
		String strinfo[] = configValueVo.getRemark().split("-");
		String userCode = strinfo[0];
		String orl_password = strinfo[1];
		String areacode = strinfo[2];
		VehicleInfoReq vehicleInfoReq = new VehicleInfoReq();
		vehicleInfoReq.setReportNo(registNo);
		vehicleInfoReq.setClaimcomPany("DHIC");
		String password = this.base64Encryption(orl_password);
		if(StringUtils.isBlank(password)){
			logger.info("密码加密失败！");
			return;
		}
		vehicleInfoReq.setPassWord(password);
		vehicleInfoReq.setUserCode(userCode);
		vehicleInfoReq.setAreaCode(areacode);
		/**
		 * 取核赔页面保单基本信息中的车辆信息，根据初始化理赔页面的逻辑，组装对应车辆信息报文
		 */
		PrpLClaimVo claimVo = claimTaskService.findClaimVoByClaimNo(wfTaskVo.getClaimNo());
		String flag = "2";// 判断是商业还是交强计算书，需要传参1-交强，2-商业
		String node = wfTaskVo.getYwTaskType().toString();
		if(FlowNode.CancelAppJuPei.name().equals(node)){
			flag = "1101".equals(claimVo.getRiskCode()) ? "1" : "2";//拒赔根据立案表设置
		}else{
			flag = node.substring(node.length()-2,node.length()).equals("BI") ? "2" : "1";
		}
		PrpLCMainVo policyInfo = null;
		List<PrpLCMainVo> policyInfos = policyViewService.getPolicyAllInfo(registNo);
		for(PrpLCMainVo policy : policyInfos){
			if("1".equals(flag)&&Risk.DQZ.equals(policy.getRiskCode())){
				policyInfo = policy;//交强
			}
			if("2".equals(flag)&&!Risk.DQZ.equals(policy.getRiskCode())){
				policyInfo = policy;//商业
			}
			if(policyInfo == null && FlowNode.CancelAppJuPei.name().equals(node) && claimVo != null){
				if(claimVo.getRiskCode().equals(policy.getRiskCode())){
					policyInfo = policy;
				}
			}
		}
		if(policyInfo != null && policyInfo.getPrpCItemCars() != null && policyInfo.getPrpCItemCars().size() > 0){
			vehicleInfoReq.setVinNo(policyInfo.getPrpCItemCars().get(0).getFrameNo());
			vehicleInfoReq.setCarMark(policyInfo.getPrpCItemCars().get(0).getLicenseNo());
			//号码种类，大致和理赔数据字典 LicenseKindCode 编码相同
			String vehicleType = policyInfo.getPrpCItemCars().get(0).getLicenseKindCode();
			if(StringUtils.isNotBlank(vehicleType)){
				if("25".equals(vehicleType)){   //其他特殊处理
					vehicleType = "99";
				}
			}
			vehicleInfoReq.setVehicleType(vehicleType);
		}
		
		PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
		if(registVo != null && StringUtils.isNotBlank(registVo.getComCode())){
			String strDateFormat = "yyyyMMdd";
	        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
	        String dataStr = sdf.format(registVo.getReportTime()).toString();
	        String uuidStr = "01" + vehicleInfoReq.getClaimcomPany()+dataStr;
			String insurerUuid = billNoService.getRealTimeQueryNo(uuidStr);
			vehicleInfoReq.setInsurerUuid(insurerUuid);
		}
		
		String requestXML = JSON.toJSONString(vehicleInfoReq);
		logger.info(registNo +"车辆查询请求报文："+requestXML);
		String urlStr = SpringProperties.getProperty("QueryVehicle_URL");
		if(StringUtils.isBlank(urlStr)){
			logger.info("车辆查询请求不存在！");
			return;
		}
		long t1 = System.currentTimeMillis();
		InputStream inputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferedReader = null;
		OutputStream outputStream = null;
		HttpURLConnection httpUrlConn = null;
		StringBuffer buffer = new StringBuffer();
		try {    
            URL url = new URL(urlStr);
            httpUrlConn = (HttpURLConnection) url.openConnection();
            httpUrlConn.setDoOutput(true);    
            httpUrlConn.setDoInput(true);    
            httpUrlConn.setUseCaches(false);    
            // 设置请求方式（GET/POST）    
            httpUrlConn.setRequestMethod("POST"); 
            httpUrlConn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            httpUrlConn.setConnectTimeout(10 * 1000);
            httpUrlConn.connect();    
            outputStream = httpUrlConn.getOutputStream();  		        
            // 当有数据需要提交时    
            outputStream.write(requestXML.getBytes("UTF-8"));   
    
            // 将返回的输入流转换成字符串    
            inputStream = httpUrlConn.getInputStream();    
            inputStreamReader = new InputStreamReader(inputStream, "UTF-8");    
            bufferedReader = new BufferedReader(inputStreamReader);    
            String str = null;    
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);    
            } 
            if (buffer.length() < 1) {
				throw new Exception("返回数据失败!");
			}
            if(buffer.toString().contains("retCode")){
            	VehicleInfoRes vehicleInfoRes = JSON.parseObject(buffer.toString(), VehicleInfoRes.class);
            	if("00".equals(vehicleInfoRes.getRetCode())){
            		logger.info("车辆查询请求成功！");
            		//保存数据
            		VehicleInfoResBody vehicleInfoResBody = vehicleInfoRes.getData();
            		if(vehicleInfoResBody == null){
            			return;
            		}
            		PrpLRealTimeQueryVo realTimeQueryVo = new PrpLRealTimeQueryVo();
            		realTimeQueryVo.setUsAge(vehicleInfoResBody.getUsAge());
            		realTimeQueryVo.setReportNo(registNo);
            		realTimeQueryVo.setDisType("1");
            		realTimeQueryVo.setChangeTime(new Date());
            		realTimeQueryVo = realTimeQueryServiceImpl.savePrpLRealTimeQueryVo(realTimeQueryVo);
            		
            		List<AntiFraud> antiFraud = vehicleInfoResBody.getAntiFraud();
            		List<PrpLAntiFraudVo>  prpLAntiFraudVos = null;
            		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");//注意月份是MM
            		if(antiFraud != null && antiFraud.size() > 0){
            			prpLAntiFraudVos= new ArrayList<PrpLAntiFraudVo>();
            			PrpLAntiFraudVo prpLAntiFraudVo = null; 
            			for (AntiFraud anti : antiFraud) {
            				prpLAntiFraudVo = new PrpLAntiFraudVo();
            				Beans.copy().from(anti).to(prpLAntiFraudVo);
            				prpLAntiFraudVo.setUpperId(realTimeQueryVo.getId());
            			    Date date = null;
        					try {
        						date = simpleDateFormat.parse(anti.getTime());
        					} catch (ParseException e) {
        						// TODO Auto-generated catch block
        						e.printStackTrace();
        					}
        					prpLAntiFraudVo.setTime(date);
            				prpLAntiFraudVos.add(prpLAntiFraudVo);
						}
            		}
            		if(prpLAntiFraudVos != null && prpLAntiFraudVos.size() > 0){
            			realTimeQueryServiceImpl.saveprpLAntiFraudVos(prpLAntiFraudVos);
            		}
            		
            		List<HistoryClaims> historyClaims = vehicleInfoResBody.getHistoryClaims();
            		if(historyClaims != null && historyClaims.size() > 0){
            			for (HistoryClaims historyClaim : historyClaims) {
            				List<BasicsInfo> basicsInfos = historyClaim.getBasicsInfo();
            				List<PrpLBasicsInfoVo> prpLBasicsInfoVos = null;
            				if(basicsInfos != null && basicsInfos.size() > 0){
            					prpLBasicsInfoVos = new ArrayList<PrpLBasicsInfoVo>();
            					PrpLBasicsInfoVo prpLBasicsInfoVo = null;
            					for (BasicsInfo basicsInfo : basicsInfos) {
            						prpLBasicsInfoVo = new PrpLBasicsInfoVo();
            						Beans.copy().from(basicsInfo).to(prpLBasicsInfoVo);
            						prpLBasicsInfoVo.setUpperId(realTimeQueryVo.getId());
            						simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");//注意月份是MM
                    			    Date accidentdate = null;
                    			    Date reportDate = null;
                					try {
                						accidentdate = simpleDateFormat.parse(basicsInfo.getAccidentTime());
                						reportDate = simpleDateFormat.parse(basicsInfo.getReportTime());
                					} catch (ParseException e) {
                						// TODO Auto-generated catch block
                						e.printStackTrace();
                					}
                					prpLBasicsInfoVo.setAccidentTime(accidentdate);
                					prpLBasicsInfoVo.setReportTime(reportDate);
                					if(StringUtils.isNotBlank(basicsInfo.getSumUnderDefLoss())){
                						prpLBasicsInfoVo.setSumUnderDefLoss(new BigDecimal(basicsInfo.getSumUnderDefLoss()));
                					}
            						prpLBasicsInfoVos.add(prpLBasicsInfoVo);
								}
            				}
            				
            				List<VehicleInfo> vehicleInfos = historyClaim.getVehicleInfo();
            				List<PrpLVehicleInfoVo> prpLVehicleInfoVos = null;
            				if(vehicleInfos != null && vehicleInfos.size() > 0){
            					prpLVehicleInfoVos = new ArrayList<PrpLVehicleInfoVo>();
            					PrpLVehicleInfoVo prpLVehicleInfoVo = null;
            					for (VehicleInfo vehicleInfo : vehicleInfos) {
            						prpLVehicleInfoVo = new PrpLVehicleInfoVo();
            						Beans.copy().from(vehicleInfo).to(prpLVehicleInfoVo);
            						prpLVehicleInfoVo.setUpperId(realTimeQueryVo.getId());
            						List<PrpLDamageInfoVo> prpLDamageInfoVos = null;
            						if(vehicleInfo.getDamageInfo() != null && vehicleInfo.getDamageInfo().size() > 0){
            							prpLDamageInfoVos = new ArrayList<PrpLDamageInfoVo>();
            							PrpLDamageInfoVo prpLDamageInfoVo = null;
            							for (DamageInfo damageInfos : vehicleInfo.getDamageInfo()) {
            								prpLDamageInfoVo = new PrpLDamageInfoVo();
            								Beans.copy().from(damageInfos).to(prpLDamageInfoVo);
            								prpLDamageInfoVo.setUpperId(realTimeQueryVo.getId());
            								prpLDamageInfoVos.add(prpLDamageInfoVo);
										}
            						}
            						if(prpLDamageInfoVos != null && prpLDamageInfoVos.size() > 0){
            							prpLVehicleInfoVo.setPrpLDamageInfo(prpLDamageInfoVos);
            						}
            						prpLVehicleInfoVos.add(prpLVehicleInfoVo);
								}
            				}
            				
            				List<PropertyLoss> propertyLosss = historyClaim.getPropertyLoss();
            				List<PrpLPropertyLossVo> prpLPropertyLossVos = null;
            				if(propertyLosss != null && propertyLosss.size() > 0){
            					prpLPropertyLossVos = new ArrayList<PrpLPropertyLossVo>();
            					PrpLPropertyLossVo prpLPropertyLossVo = null;
            					for (PropertyLoss propertyLoss : propertyLosss) {
            						prpLPropertyLossVo = new PrpLPropertyLossVo();
            						Beans.copy().from(propertyLoss).to(prpLPropertyLossVo);
            						prpLPropertyLossVo.setUpperId(realTimeQueryVo.getId());
            						if(StringUtils.isNotBlank(propertyLoss.getUnderDefLoss())){
            							prpLPropertyLossVo.setUnderDefLoss(new BigDecimal(propertyLoss.getUnderDefLoss()));
                					}
            						prpLPropertyLossVos.add(prpLPropertyLossVo);
								}
            				}
            				
            				if(prpLBasicsInfoVos != null && prpLBasicsInfoVos.size() > 0){
            					realTimeQueryServiceImpl.saveBasicsInfo(prpLBasicsInfoVos);
            				}
            				if(prpLVehicleInfoVos != null && prpLVehicleInfoVos.size() > 0){
            					realTimeQueryServiceImpl.savePrpLVehicleInfoVos(prpLVehicleInfoVos);
            				}
            				if(prpLPropertyLossVos != null && prpLPropertyLossVos.size() > 0){
            					realTimeQueryServiceImpl.savePrpLPropertyLossVos(prpLPropertyLossVos);
            				}
						}
            			
            		}
            		
            	}else{
            		logger.info("车辆信息查询数据返回失败！原因："+ vehicleInfoRes.getRetMessage()+"---对应返回消息："+buffer.toString());
            	}
            }else{
            	ResponseErrorInfo responseErrorInfo = JSON.parseObject(buffer.toString(), ResponseErrorInfo.class);
            	logger.info("车辆信息查询数据返回失败！"+ buffer.toString());
            }
            
            
        }catch (Exception e) {
        	e.printStackTrace();
        }finally{
        	logger.warn("平台({})调用耗时{}ms",urlStr,System.currentTimeMillis()-t1);
        	// 释放资源 
        	httpUrlConn.disconnect(); 
        	try {
				bufferedReader.close();
				inputStreamReader.close();  
	            outputStream.flush();
	            outputStream.close();
	            inputStream.close(); 
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}    
        }
	}
	
	/**
	 * 4.1.1.人员信息查询接口
	 */
	@Override
	public void sendPersonInfoQuery(String registNo,PrpLConfigValueVo configValueVo) {
		//Remark : DHIC440000-KzZ850W55nCW-440000   用户名-密码-地区
		if(StringUtils.isBlank(configValueVo.getRemark())){
			logger.info("获取开关配置信息失败！");
			return;
		}
		String strinfo[] = configValueVo.getRemark().split("-");
		String userCode = strinfo[0];
		String orl_password = strinfo[1];
		String areacode = strinfo[2];
		VehicleInfoReq vehicleInfoReq = new VehicleInfoReq();
		vehicleInfoReq.setReportNo(registNo);
		vehicleInfoReq.setClaimcomPany("DHIC");
		String password = this.base64Encryption(orl_password);
		if(StringUtils.isBlank(password)){
			logger.info("密码加密失败！");
			return;
		}
		vehicleInfoReq.setPassWord(password);
		vehicleInfoReq.setUserCode(userCode);
		vehicleInfoReq.setAreaCode(areacode);
		
		PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
		if(registVo != null && StringUtils.isNotBlank(registVo.getComCode())){
			String strDateFormat = "yyyyMMdd";
	        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
	        String dataStr = sdf.format(registVo.getReportTime()).toString();
	        String uuidStr = "01" + vehicleInfoReq.getClaimcomPany()+dataStr;
			String insurerUuid = billNoService.getRealTimeQueryNo(uuidStr);
			vehicleInfoReq.setInsurerUuid(insurerUuid);
		}
		List<PrpLCheckDriverVo> prpLCheckDriverVoList = checkTaskService.findPrpLCheckDriverVoByRegistNo(registNo);
		if(prpLCheckDriverVoList != null && prpLCheckDriverVoList.size() > 0){
			vehicleInfoReq.setDriverLicenseNo(prpLCheckDriverVoList.get(0).getDrivingLicenseNo());
			if(StringUtils.isNotBlank(prpLCheckDriverVoList.get(0).getIdentifyType())){
				vehicleInfoReq.setCertiType(CodeConstants.identifyMap.get(prpLCheckDriverVoList.get(0).getIdentifyType()));
			}else{
				vehicleInfoReq.setCertiType("01");  
			}
			vehicleInfoReq.setCertiCode(prpLCheckDriverVoList.get(0).getIdentifyNumber());
		}
		String requestXML = JSON.toJSONString(vehicleInfoReq);
		logger.info(registNo +"人员查询请求报文："+requestXML);
		String urlStr = SpringProperties.getProperty("QueryPerson_URL");
		if(StringUtils.isBlank(urlStr)){
			logger.info("人员查询请求不存在！");
			return;
		}
		long t1 = System.currentTimeMillis();
		InputStream inputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferedReader = null;
		OutputStream outputStream = null;
		HttpURLConnection httpUrlConn = null;
		StringBuffer buffer = new StringBuffer();
		try {    
            URL url = new URL(urlStr);
            httpUrlConn = (HttpURLConnection) url.openConnection();
            httpUrlConn.setDoOutput(true);    
            httpUrlConn.setDoInput(true);    
            httpUrlConn.setUseCaches(false);    
            // 设置请求方式（GET/POST）    
            httpUrlConn.setRequestMethod("POST"); 
            httpUrlConn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            httpUrlConn.setConnectTimeout(10 * 1000);
            httpUrlConn.connect();    
            outputStream = httpUrlConn.getOutputStream();  		        
            // 当有数据需要提交时    
            outputStream.write(requestXML.getBytes("UTF-8"));   
    
            // 将返回的输入流转换成字符串    
            inputStream = httpUrlConn.getInputStream();    
            inputStreamReader = new InputStreamReader(inputStream, "UTF-8");    
            bufferedReader = new BufferedReader(inputStreamReader);    
            String str = null;    
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);    
            } 
            if (buffer.length() < 1) {
				throw new Exception("返回数据失败!");
			}
            if(buffer.toString().contains("retCode")){
            	PersonInfoRes personInfoRes = JSON.parseObject(buffer.toString(), PersonInfoRes.class);
            	if("00".equals(personInfoRes.getRetCode())){
            		logger.info("人员信息查询请求成功！");
            		//保存数据
            		PersonInfoResBody personInfoResBody = personInfoRes.getData();
            		if(personInfoResBody == null){
            			logger.info("人员信息查询数据为空！");
            			return;
            		}
            		PrpLRealTimeQueryVo realTimeQueryVo = new PrpLRealTimeQueryVo();
            		realTimeQueryVo.setUsAge(personInfoResBody.getUsAge());
            		realTimeQueryVo.setReportNo(registNo);
            		realTimeQueryVo.setDisType("2");
            		realTimeQueryVo.setChangeTime(new Date());
            		realTimeQueryVo = realTimeQueryServiceImpl.savePrpLRealTimeQueryVo(realTimeQueryVo);
            		
            		List<AntiFraud> antiFraud = personInfoResBody.getAntiFraud();
            		List<PrpLAntiFraudVo>  prpLAntiFraudVos = null;
            		if(antiFraud != null && antiFraud.size() > 0){
            			prpLAntiFraudVos= new ArrayList<PrpLAntiFraudVo>();
            			PrpLAntiFraudVo prpLAntiFraudVo = null; 
            			for (AntiFraud anti : antiFraud) {
            				prpLAntiFraudVo = new PrpLAntiFraudVo();
            				Beans.copy().from(anti).to(prpLAntiFraudVo);
            				prpLAntiFraudVo.setUpperId(realTimeQueryVo.getId());
            				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");//注意月份是MM
            			    Date date = null;
        					try {
        						date = simpleDateFormat.parse(anti.getTime());
        					} catch (ParseException e) {
        						// TODO Auto-generated catch block
        						e.printStackTrace();
        					}
        					prpLAntiFraudVo.setTime(date);
            				prpLAntiFraudVos.add(prpLAntiFraudVo);
						}
            		}
            		if(prpLAntiFraudVos != null && prpLAntiFraudVos.size() > 0){
            			realTimeQueryServiceImpl.saveprpLAntiFraudVos(prpLAntiFraudVos);
            		}
            		
            		List<PersonHistoryClaims> historyClaims = personInfoResBody.getHistoryClaims();
            		if(historyClaims != null && historyClaims.size() > 0){
            			for (PersonHistoryClaims historyClaim : historyClaims) {
            				List<BasicsInfo> basicsInfos = historyClaim.getBasicsInfo();
            				List<PrpLBasicsInfoVo> prpLBasicsInfoVos = null;
            				if(basicsInfos != null && basicsInfos.size() > 0){
            					prpLBasicsInfoVos = new ArrayList<PrpLBasicsInfoVo>();
            					PrpLBasicsInfoVo prpLBasicsInfoVo = null;
            					for (BasicsInfo basicsInfo : basicsInfos) {
            						prpLBasicsInfoVo = new PrpLBasicsInfoVo();
            						Beans.copy().from(basicsInfo).to(prpLBasicsInfoVo);
            						prpLBasicsInfoVo.setUpperId(realTimeQueryVo.getId());
            						SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");//注意月份是MM
                    			    Date accidentdate = null;
                    			    Date reportDate = null;
                					try {
                						accidentdate = simpleDateFormat.parse(basicsInfo.getAccidentTime());
                						reportDate = simpleDateFormat.parse(basicsInfo.getReportTime());
                					} catch (ParseException e) {
                						// TODO Auto-generated catch block
                						e.printStackTrace();
                					}
                					prpLBasicsInfoVo.setAccidentTime(accidentdate);
                					prpLBasicsInfoVo.setReportTime(reportDate);
                					if(StringUtils.isNotBlank(basicsInfo.getSumUnderDefLoss())){
                						prpLBasicsInfoVo.setSumUnderDefLoss(new BigDecimal(basicsInfo.getSumUnderDefLoss()));
                					}
            						prpLBasicsInfoVos.add(prpLBasicsInfoVo);
								}
            				}
            				
            				List<VehicleInfo> vehicleInfos = historyClaim.getVehicleInfo();
            				List<PrpLVehicleInfoVo> prpLVehicleInfoVos = null;
            				if(vehicleInfos != null && vehicleInfos.size() > 0){
            					prpLVehicleInfoVos = new ArrayList<PrpLVehicleInfoVo>();
            					PrpLVehicleInfoVo prpLVehicleInfoVo = null;
            					for (VehicleInfo vehicleInfo : vehicleInfos) {
            						prpLVehicleInfoVo = new PrpLVehicleInfoVo();
            						Beans.copy().from(vehicleInfo).to(prpLVehicleInfoVo);
            						prpLVehicleInfoVo.setUpperId(realTimeQueryVo.getId());
            						List<PrpLDamageInfoVo> prpLDamageInfoVos = null;
            						if(vehicleInfo.getDamageInfo() != null && vehicleInfo.getDamageInfo().size() > 0){
            							prpLDamageInfoVos = new ArrayList<PrpLDamageInfoVo>();
            							PrpLDamageInfoVo prpLDamageInfoVo = null;
            							for (DamageInfo damageInfos : vehicleInfo.getDamageInfo()) {
            								prpLDamageInfoVo = new PrpLDamageInfoVo();
            								Beans.copy().from(damageInfos).to(prpLDamageInfoVo);
            								prpLDamageInfoVos.add(prpLDamageInfoVo);
										}
            						}
            						if(prpLDamageInfoVos != null && prpLDamageInfoVos.size() > 0){
            							prpLVehicleInfoVo.setPrpLDamageInfo(prpLDamageInfoVos);
            						}
            						prpLVehicleInfoVos.add(prpLVehicleInfoVo);
								}
            				}
            				
            				List<CasualtyInfor> casualtyInfors = historyClaim.getCasualtyInfor();
            				List<PrpLCasualtyInforVo> prpLCasualtyInforVos = null;
            				if(casualtyInfors != null && casualtyInfors.size() > 0){
            					prpLCasualtyInforVos = new ArrayList<PrpLCasualtyInforVo>();
            					PrpLCasualtyInforVo prpLCasualtyInforVo = null;
            					for (CasualtyInfor casualtyInfor : casualtyInfors) {
            						prpLCasualtyInforVo = new PrpLCasualtyInforVo();
            						Beans.copy().from(casualtyInfor).to(prpLCasualtyInforVo);
            						prpLCasualtyInforVo.setUpperId(realTimeQueryVo.getId());
            						List<PrpLInjuredDetailsVo> prpLInjuredDetailsVos = null;
            						if(casualtyInfor.getInjuredDetails() != null && casualtyInfor.getInjuredDetails().size() > 0){
            							prpLInjuredDetailsVos = new ArrayList<PrpLInjuredDetailsVo>();
            							PrpLInjuredDetailsVo prpLInjuredDetailsVo = null;
            							for (InjuredDetails injuredDetails : casualtyInfor.getInjuredDetails()) {
            								prpLInjuredDetailsVo = new PrpLInjuredDetailsVo();
            								Beans.copy().from(injuredDetails).to(prpLInjuredDetailsVo);
            								prpLInjuredDetailsVo.setUpperId(realTimeQueryVo.getId());
            								prpLInjuredDetailsVos.add(prpLInjuredDetailsVo);
										}
            						}
            						if(prpLInjuredDetailsVos != null && prpLInjuredDetailsVos.size() > 0){
            							prpLCasualtyInforVo.setPrpLInjuredDetailsVo(prpLInjuredDetailsVos);
            						}
            						prpLCasualtyInforVos.add(prpLCasualtyInforVo);
								}
            				}
            				
            				List<PropertyLoss> propertyLosss = historyClaim.getPropertyLoss();
            				List<PrpLPropertyLossVo> prpLPropertyLossVos = null;
            				if(propertyLosss != null && propertyLosss.size() > 0){
            					prpLPropertyLossVos = new ArrayList<PrpLPropertyLossVo>();
            					PrpLPropertyLossVo prpLPropertyLossVo = null;
            					for (PropertyLoss propertyLoss : propertyLosss) {
            						prpLPropertyLossVo = new PrpLPropertyLossVo();
            						Beans.copy().from(propertyLoss).to(prpLPropertyLossVo);
            						prpLPropertyLossVo.setUpperId(realTimeQueryVo.getId());
            						if(StringUtils.isNotBlank(propertyLoss.getUnderDefLoss())){
            							prpLPropertyLossVo.setUnderDefLoss(new BigDecimal(propertyLoss.getUnderDefLoss()));
            						}
            						prpLPropertyLossVos.add(prpLPropertyLossVo);
								}
            				}
            				
            				if(prpLBasicsInfoVos != null && prpLBasicsInfoVos.size() > 0){
            					realTimeQueryServiceImpl.saveBasicsInfo(prpLBasicsInfoVos);
            				}
            				if(prpLVehicleInfoVos != null && prpLVehicleInfoVos.size() > 0){
            					realTimeQueryServiceImpl.savePrpLVehicleInfoVos(prpLVehicleInfoVos);
            				}
            				if(prpLCasualtyInforVos != null && prpLCasualtyInforVos.size() > 0){
            					realTimeQueryServiceImpl.saveprpLCasualtyInforVos(prpLCasualtyInforVos);
            				}
            				if(prpLPropertyLossVos != null && prpLPropertyLossVos.size() > 0){
            					realTimeQueryServiceImpl.savePrpLPropertyLossVos(prpLPropertyLossVos);
            				}
						}
            			
            		}
            		
            	}else{
            		logger.info("人员信息查询数据返回失败！原因："+ personInfoRes.getRetMessage()+"---对应返回消息："+buffer.toString());
            	}
            }else{
            	ResponseErrorInfo responseErrorInfo = JSON.parseObject(buffer.toString(), ResponseErrorInfo.class);
            	logger.info("人员信息查询数据返回失败！"+ buffer.toString());
            }
            
            
        }catch (Exception e) {
        	e.printStackTrace();
        }finally{
        	logger.warn("平台({})调用耗时{}ms",urlStr,System.currentTimeMillis()-t1);
        	// 释放资源 
        	httpUrlConn.disconnect(); 
        	try {
				bufferedReader.close();
				inputStreamReader.close();  
	            outputStream.flush();
	            outputStream.close();
	            inputStream.close(); 
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}    
        }
	}
	
	/**
	 * 4.1.1.报案电话信息查询接口
	 */
	@Override
	public void sendReportPhoneInfoQuery(String registNo,PrpLConfigValueVo configValueVo) {
		//Remark : DHIC440000-KzZ850W55nCW-440000   用户名-密码-地区
		if(StringUtils.isBlank(configValueVo.getRemark())){
			logger.info("获取开关配置信息失败！");
			return;
		}
		String strinfo[] = configValueVo.getRemark().split("-");
		String userCode = strinfo[0];
		String orl_password = strinfo[1];
		String areacode = strinfo[2];
		VehicleInfoReq vehicleInfoReq = new VehicleInfoReq();
		vehicleInfoReq.setReportNo(registNo);
		vehicleInfoReq.setClaimcomPany("DHIC");
		String password = this.base64Encryption(orl_password);
		if(StringUtils.isBlank(password)){
			logger.info("密码加密失败！");
			return;
		}
		vehicleInfoReq.setPassWord(password);
		vehicleInfoReq.setUserCode(userCode);
		vehicleInfoReq.setAreaCode(areacode);
		
		PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
		if(registVo != null && StringUtils.isNotBlank(registVo.getComCode())){
			String strDateFormat = "yyyyMMdd";
	        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
	        String dataStr = sdf.format(registVo.getReportTime()).toString();
	        String uuidStr = "01" + vehicleInfoReq.getClaimcomPany()+dataStr;
			String insurerUuid = billNoService.getRealTimeQueryNo(uuidStr);
			vehicleInfoReq.setInsurerUuid(insurerUuid);
			vehicleInfoReq.setReportPhoneNo(registVo.getReportorPhone());
		}
		
		String requestXML = JSON.toJSONString(vehicleInfoReq);
		logger.info(registNo +"报案电话信息查询请求报文："+requestXML);
		String urlStr = SpringProperties.getProperty("QueryPhone_URL");
		if(StringUtils.isBlank(urlStr)){
			logger.info("报案电话信息查询请求不存在！");
			return;
		}
		long t1 = System.currentTimeMillis();
		InputStream inputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferedReader = null;
		OutputStream outputStream = null;
		HttpURLConnection httpUrlConn = null;
		StringBuffer buffer = new StringBuffer();
		try {    
            URL url = new URL(urlStr);
            httpUrlConn = (HttpURLConnection) url.openConnection();
            httpUrlConn.setDoOutput(true);    
            httpUrlConn.setDoInput(true);    
            httpUrlConn.setUseCaches(false);    
            // 设置请求方式（GET/POST）    
            httpUrlConn.setRequestMethod("POST"); 
            httpUrlConn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            httpUrlConn.setConnectTimeout(10 * 1000);
            httpUrlConn.connect();    
            outputStream = httpUrlConn.getOutputStream();  		        
            // 当有数据需要提交时    
            outputStream.write(requestXML.getBytes("UTF-8"));   
    
            // 将返回的输入流转换成字符串    
            inputStream = httpUrlConn.getInputStream();    
            inputStreamReader = new InputStreamReader(inputStream, "UTF-8");    
            bufferedReader = new BufferedReader(inputStreamReader);    
            String str = null;    
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);    
            } 
            if (buffer.length() < 1) {
				throw new Exception("返回数据失败!");
			}
            if(buffer.toString().contains("retCode")){
            	ReportPhoneInfoRes reportPhoneInfoRes = JSON.parseObject(buffer.toString(), ReportPhoneInfoRes.class);
            	if("00".equals(reportPhoneInfoRes.getRetCode())){
            		//保存数据
            		ReportPhoneInfoResBody reportPhoneInfoResBody = reportPhoneInfoRes.getData();
            		if(reportPhoneInfoResBody == null){
            			logger.info("人员信息查询数据为空！");
            			return;
            		}
            		PrpLRealTimeQueryVo realTimeQueryVo = new PrpLRealTimeQueryVo();
            		realTimeQueryVo.setUsAge(reportPhoneInfoResBody.getUsAge());
            		realTimeQueryVo.setReportNo(registNo);
            		realTimeQueryVo.setDisType("3");
            		realTimeQueryVo.setChangeTime(new Date());
            		realTimeQueryVo = realTimeQueryServiceImpl.savePrpLRealTimeQueryVo(realTimeQueryVo);
            		
            		List<AntiFraud> antiFraud = reportPhoneInfoResBody.getAntiFraud();
            		List<PrpLAntiFraudVo>  prpLAntiFraudVos = null;
            		if(antiFraud != null && antiFraud.size() > 0){
            			prpLAntiFraudVos= new ArrayList<PrpLAntiFraudVo>();
            			PrpLAntiFraudVo prpLAntiFraudVo = null; 
            			for (AntiFraud anti : antiFraud) {
            				prpLAntiFraudVo = new PrpLAntiFraudVo();
            				Beans.copy().from(anti).to(prpLAntiFraudVo);
            				prpLAntiFraudVo.setUpperId(realTimeQueryVo.getId());
            				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");//注意月份是MM
            			    Date date = null;
        					try {
        						date = simpleDateFormat.parse(anti.getTime());
        					} catch (ParseException e) {
        						// TODO Auto-generated catch block
        						e.printStackTrace();
        					}
        					prpLAntiFraudVo.setTime(date);
            				prpLAntiFraudVos.add(prpLAntiFraudVo);
						}
            		}
            		if(prpLAntiFraudVos != null && prpLAntiFraudVos.size() > 0){
            			realTimeQueryServiceImpl.saveprpLAntiFraudVos(prpLAntiFraudVos);
            		}
            		
            		List<ReportPhoneHistoryClaims> historyClaims = reportPhoneInfoResBody.getHistoryClaims();
            		if(historyClaims != null && historyClaims.size() > 0){
            			for (ReportPhoneHistoryClaims historyClaim : historyClaims) {
            				List<BasicsInfo> basicsInfos = historyClaim.getBasicsInfo();
            				List<PrpLBasicsInfoVo> prpLBasicsInfoVos = null;
            				if(basicsInfos != null && basicsInfos.size() > 0){
            					prpLBasicsInfoVos = new ArrayList<PrpLBasicsInfoVo>();
            					PrpLBasicsInfoVo prpLBasicsInfoVo = null;
            					for (BasicsInfo basicsInfo : basicsInfos) {
            						prpLBasicsInfoVo = new PrpLBasicsInfoVo();
            						Beans.copy().from(basicsInfo).to(prpLBasicsInfoVo);
            						prpLBasicsInfoVo.setUpperId(realTimeQueryVo.getId());
            						SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");//注意月份是MM
                    			    Date accidentdate = null;
                    			    Date reportDate = null;
                					try {
                						accidentdate = simpleDateFormat.parse(basicsInfo.getAccidentTime());
                						reportDate = simpleDateFormat.parse(basicsInfo.getReportTime());
                					} catch (ParseException e) {
                						// TODO Auto-generated catch block
                						e.printStackTrace();
                					}
                					prpLBasicsInfoVo.setAccidentTime(accidentdate);
                					prpLBasicsInfoVo.setReportTime(reportDate);
                					if(StringUtils.isNotBlank(basicsInfo.getSumUnderDefLoss())){
                						prpLBasicsInfoVo.setSumUnderDefLoss(new BigDecimal(basicsInfo.getSumUnderDefLoss()));
                					}
            						prpLBasicsInfoVos.add(prpLBasicsInfoVo);
								}
            				}
            				
            				List<TeleAgg> teleAggList = historyClaim.getTeleAgg();
            				List<PrpLPropertyLossVo> prpLPropertyLossVos = null;
            				if(teleAggList != null && teleAggList.size() > 0){
            					prpLPropertyLossVos = new ArrayList<PrpLPropertyLossVo>();
            					PrpLPropertyLossVo prpLPropertyLossVo = null;
            					for (TeleAgg teleAgg : teleAggList) {
            						prpLPropertyLossVo = new PrpLPropertyLossVo();
            						Beans.copy().from(teleAgg).to(prpLPropertyLossVo);
            						prpLPropertyLossVo.setUpperId(realTimeQueryVo.getId());
            						prpLPropertyLossVos.add(prpLPropertyLossVo);
								}
            				}
            				
            				if(prpLBasicsInfoVos != null && prpLBasicsInfoVos.size() > 0){
            					realTimeQueryServiceImpl.saveBasicsInfo(prpLBasicsInfoVos);
            				}
            				if(prpLPropertyLossVos != null && prpLPropertyLossVos.size() > 0){
            					realTimeQueryServiceImpl.savePrpLPropertyLossVos(prpLPropertyLossVos);
            				}
						}
            			
            		}
            		
            	}else{
            		logger.info("报案电话查询数据返回失败！原因："+ reportPhoneInfoRes.getRetMessage()+"---对应返回消息："+buffer.toString());
            	}
            }else{
            	ResponseErrorInfo responseErrorInfo = JSON.parseObject(buffer.toString(), ResponseErrorInfo.class);
            	logger.info("报案电话查询数据返回失败！"+ buffer.toString());
            }
            
            
        }catch (Exception e) {
        	e.printStackTrace();
        }finally{
        	logger.warn("平台({})调用耗时{}ms",urlStr,System.currentTimeMillis()-t1);
        	// 释放资源 
        	httpUrlConn.disconnect(); 
        	try {
				bufferedReader.close();
				inputStreamReader.close();  
	            outputStream.flush();
	            outputStream.close();
	            inputStream.close(); 
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}    
        }
	}
	
	private String base64Encryption(String orl_password){
		String key = "1234567812345678";
        String iv = "0000000000000000";
        String strenc = "";
		try {
			strenc = AesUtils.encrypt(orl_password,key,iv); //加密
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return strenc;
	}
}
