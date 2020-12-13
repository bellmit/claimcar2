package ins.sino.claimcar.check.service.spring;

import ins.platform.common.util.CodeTranUtil;
import ins.platform.utils.ClaimBaseCoder;
import ins.platform.utils.DateUtils;
import ins.platform.utils.HttpClientHander;
import ins.sino.claimcar.carinterface.service.ClaimInterfaceLogService;
import ins.sino.claimcar.carinterface.util.BusinessType;
import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;
import ins.sino.claimcar.check.service.CheckHandleService;
import ins.sino.claimcar.check.vo.CheckActionVo;
import ins.sino.claimcar.check.vo.PrpLCheckCarInfoVo;
import ins.sino.claimcar.check.vo.PrpLCheckCarVo;
import ins.sino.claimcar.check.vo.PrpLCheckDriverVo;
import ins.sino.claimcar.check.vo.PrpLCheckDutyVo;
import ins.sino.claimcar.check.vo.PrpLCheckPersonVo;
import ins.sino.claimcar.check.vo.PrpLCheckPropVo;
import ins.sino.claimcar.check.vo.PrpLCheckTaskVo;
import ins.sino.claimcar.check.vo.PrpLCheckVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.platform.util.SendPlatformUtil;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.vo.PrpLCItemCarVo;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.trafficplatform.service.CheckToWarnService;
import ins.sino.claimcar.trafficplatform.service.EarlyWarnService;
import ins.sino.claimcar.trafficplatform.vo.EWCheckBasePart;
import ins.sino.claimcar.trafficplatform.vo.EWCheckBody;
import ins.sino.claimcar.trafficplatform.vo.EWCheckCarData;
import ins.sino.claimcar.trafficplatform.vo.EWCheckPersonData;
import ins.sino.claimcar.trafficplatform.vo.EWCheckPropData;
import ins.sino.claimcar.trafficplatform.vo.EWCheckRequest;
import ins.sino.claimcar.trafficplatform.vo.EWReqHead;
import ins.sino.claimcar.trafficplatform.vo.EWResponse;

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
@Path(value = "checkToWarnService")
public class CheckToWarnServiceImpl implements CheckToWarnService {

	private static Logger logger = LoggerFactory.getLogger(CheckToWarnServiceImpl.class);

	@Autowired
	EarlyWarnService earlyWarnService;
	@Autowired
	ClaimInterfaceLogService interfaceLogService;
	@Autowired
	PolicyViewService policyViewService;
	@Autowired
	CheckHandleService checkHandleService;
	@Autowired
	WfTaskHandleService wfTaskHandleService;
    @Autowired
    RegistQueryService registQueryService;
	@Override
	public void checkToWarn(Long checkTaskId,Long flowTaskId,String policyNo) throws Exception {
		CheckActionVo checkActionVo = checkHandleService.initCheckByCheck(checkTaskId);
		List<PrpLCMainVo> cMainVoList = new ArrayList<PrpLCMainVo>();
		if(policyNo==null){
			cMainVoList = checkActionVo.getPolicyInfo();
		}else{
			PrpLCMainVo cMainVo = policyViewService.getPolicyInfo(checkActionVo.getRegistNo(),policyNo);
			cMainVoList.add(cMainVo);
		}
		String urlStr = "";
		String requestXML = "";
		String responseXML = "";
		String ciBusinessType = "";
		String ciBusinessName = "";
		String biBusinessType = "";
		String biBusinessName = "";
		for(PrpLCMainVo cMainVo:cMainVoList){
			//山东机构才上传
			if(cMainVo.getComCode().startsWith("62")){
				urlStr = SpringProperties.getProperty("SDWARN_URL");
				if("1101".equals(cMainVo.getRiskCode())){
					ciBusinessType = BusinessType.SDEW_check_CI.name();
					ciBusinessName = BusinessType.SDEW_check_CI.getName();
				}else{
					biBusinessType = BusinessType.SDEW_check_BI.name();
					biBusinessName = BusinessType.SDEW_check_BI.getName();
				}
			}else if(cMainVo.getComCode().startsWith("10")){
				urlStr = SpringProperties.getProperty("GDWARN_URL");
				if("1101".equals(cMainVo.getRiskCode())){
					ciBusinessType = BusinessType.GDEW_check_CI.name();
					ciBusinessName = BusinessType.GDEW_check_CI.getName();
				}else{
					biBusinessType = BusinessType.GDEW_check_BI.name();
					biBusinessName = BusinessType.GDEW_check_BI.getName();
				}
			}else if(cMainVo.getComCode().startsWith("50")){
				urlStr = SpringProperties.getProperty("HNWARN_URL");
				if("1101".equals(cMainVo.getRiskCode())){
					ciBusinessType = BusinessType.HNEW_check_CI.name();
					ciBusinessName = BusinessType.HNEW_check_CI.getName();
				}else{
					biBusinessType = BusinessType.HNEW_check_BI.name();
					biBusinessName = BusinessType.HNEW_check_BI.getName();
				}
			}
			if(StringUtils.isNotBlank(ciBusinessType) || StringUtils.isNotBlank(biBusinessType)){
				ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
				try{
					requestXML = getRequestXML(checkActionVo, cMainVo, flowTaskId);
					logger.info("=================查勘送预警请求报文ComCode===="+cMainVo.getComCode()+requestXML);
					responseXML = earlyWarnService.requestSDEW(requestXML, urlStr);
					logger.info("=================查勘送预警返回报文ComCode===="+cMainVo.getComCode()+responseXML);
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
					logger.info("=================查勘送预警报错ComCode===="+cMainVo.getComCode()+e);
					logVo.setStatus("0");
		            logVo.setErrorMessage(e.getMessage());
				}finally{
					logVo.setRequestUrl(urlStr);
					logVo.setRequestXml(requestXML);
					logVo.setResponseXml(responseXML);
					logVo.setRegistNo(checkActionVo.getRegistNo());
					if("1101".equals(cMainVo.getRiskCode())){
						logVo.setBusinessType(ciBusinessType);
			            logVo.setBusinessName(ciBusinessName);
					}else{
						logVo.setBusinessType(biBusinessType);
			            logVo.setBusinessName(biBusinessName);
					}
					logVo.setComCode(cMainVo.getComCode());
		            logVo.setOperateNode(FlowNode.Check.name());
		            logVo.setRequestTime(new Date());
		            logVo.setCreateTime(new Date());
		            logVo.setCreateUser(checkActionVo.getPrpLcheckVo().getCreateUser());
		            logVo.setClaimNo(checkTaskId.toString());
		            logVo.setCompensateNo(flowTaskId.toString());
		            logVo.setPolicyNo(cMainVo.getPolicyNo());
		            interfaceLogService.save(logVo);
				}
			}
		}
	}

	public String getRequestXML(CheckActionVo checkActionVo,PrpLCMainVo cMainVo,Long flowTaskId) {
		XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
		stream.autodetectAnnotations(true);
		stream.setMode(XStream.NO_REFERENCES);
		stream.aliasSystemAttribute(null,"class");
		String user = "";
		String passWord = "";

		
		String requestXML = "";
		EWCheckRequest requestVo = new EWCheckRequest();
		EWReqHead head = new EWReqHead();
		EWCheckBody body = new EWCheckBody();
		EWCheckBasePart basePart = new EWCheckBasePart();
		List<EWCheckCarData> carDataList = new ArrayList<EWCheckCarData>();
		List<EWCheckPropData> propDataList = new ArrayList<EWCheckPropData>();
		List<EWCheckPersonData> personDataList = new ArrayList<EWCheckPersonData>();

		PrpLCheckDutyVo checkDutyVo = checkActionVo.getCheckDutyVo();
		PrpLCheckVo checkVo = checkActionVo.getPrpLcheckVo();
		PrpLRegistVo registVo = checkActionVo.getPrpLregistVo();
		PrpLCheckTaskVo checkTaskVo = checkVo.getPrpLCheckTask();
		List<PrpLCheckCarVo> checkCarList = checkTaskVo.getPrpLCheckCars();
		List<PrpLCheckPropVo> checkPropList = checkTaskVo.getPrpLCheckProps();
		List<PrpLCheckPersonVo> personList = checkTaskVo.getPrpLCheckPersons();
		PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(flowTaskId.doubleValue());
		// 头部
		if(cMainVo.getComCode().startsWith("62")){//山东
			user = SpringProperties.getProperty("SDWARN_USER");
			passWord = SpringProperties.getProperty("SDWARN_PW");
		}else if(cMainVo.getComCode().startsWith("10")){//广东
			user = SpringProperties.getProperty("GDWARN_USER");
			passWord = SpringProperties.getProperty("GDWARN_PW");
		}else if(cMainVo.getComCode().startsWith("50")){//河南
			user = SpringProperties.getProperty("HNWARN_USER");
			passWord = SpringProperties.getProperty("HNWARN_PW");
		}
		head.setRequestType("C0303");
		head.setUser(user);
		head.setPassWord(passWord);
		// 基本信息
		basePart.setClaimSequenceNo(cMainVo.getClaimSequenceNo());
		basePart.setClaimNotificationNo(cMainVo.getRegistNo());
		basePart.setConfirmSequenceNo(cMainVo.getValidNo());
		// basePart.setAccidentType(accidentType);
		basePart.setAccidentLiability(String.valueOf(Integer.valueOf(checkDutyVo.getIndemnityDuty())+1));
		basePart.setOptionType(checkVo.getManageType());
		basePart.setLossCauseCode(getLossCauseCode(checkVo.getDamageCode(),checkVo.getDamOtherCode()));
		// basePart.setSubCertiType(subCertiType);
		basePart.setIsSingleAccident(checkVo.getSingleAccidentFlag());
		//广东河南不用start
		if(!cMainVo.getComCode().startsWith("10") && !cMainVo.getComCode().startsWith("50")){
			basePart.setLongLaType("3");
			basePart.setLongitude(new BigDecimal(registVo.getDamageMapCode().split(",")[0]));
			basePart.setLatitude(new BigDecimal(registVo.getDamageMapCode().split(",")[1]));
		}
		//广东河南不用end
		// fix by LiYi 拿到标的车 ,判断是否上牌，默认上牌
		boolean registLicenseNo = true;
		List<PrpLCItemCarVo> prpCItemCars = cMainVo.getPrpCItemCars();
		if(prpCItemCars!=null&& !prpCItemCars.isEmpty()){
			PrpLCItemCarVo cItemCarVo = prpCItemCars.get(0);
			String otherNature = cItemCarVo.getOtherNature();
			if(StringUtils.isNotEmpty(otherNature)){
				String substring = otherNature.substring(6,7);
				if(substring.equals("1")){
					registLicenseNo = false;
				}
			}else{
				// otherNature为空 , 默认为未上牌
				registLicenseNo = false;
			}
		}else{
			registLicenseNo = false;
		}
		// 车辆信息
		for(PrpLCheckCarVo checkCarVo:checkCarList){
			PrpLCheckCarInfoVo checkCarInfo = checkCarVo.getPrpLCheckCarInfo();
			PrpLCheckDriverVo driver = checkCarVo.getPrpLCheckDriver();
			EWCheckCarData carData = new EWCheckCarData();
			// fix by LiYi 1为标的车
			if("1".equals(checkCarVo.getSerialNo().toString())){
				if( !registLicenseNo){
					carData.setLicensePlateNo("");
					carData.setLicensePlateType("");
				}else{
					carData.setLicensePlateNo(checkCarInfo.getLicenseNo());
					carData.setLicensePlateType(checkCarInfo.getLicenseType());
					String licenseType = checkCarInfo.getLicenseType();
					if(StringUtils.isBlank(licenseType)){
						licenseType = "02";
					}
					if("82".equals(licenseType)){
						licenseType = "32";//针对武警标的车取值的是保单表编码的调整
					}
					if("12".equals(cMainVo.getRiskCode().substring(0, 2))){//商业
						if("25".equals(licenseType)){
							carData.setLicensePlateType("99");
						}
					}else{//交强
						if("99".equals(licenseType)){
							carData.setLicensePlateType("25");
						}
					}
				}
			}else{
				carData.setLicensePlateNo(checkCarInfo.getLicenseNo());
				carData.setLicensePlateType(checkCarInfo.getLicenseType());
				String licenseType = checkCarInfo.getLicenseType();
				if(StringUtils.isBlank(licenseType)){
					licenseType = "02";
				}
				if("82".equals(licenseType)){
					licenseType = "32";//针对武警标的车取值的是保单表编码的调整
				}
				if("12".equals(cMainVo.getRiskCode().substring(0, 2))){//商业
					if("25".equals(licenseType)){
						carData.setLicensePlateType("99");
					}
				}else{//交强
					if("99".equals(licenseType)){
						carData.setLicensePlateType("25");
					}
				}
			}
			
			
			carData.setVin(checkCarInfo.getVinNo());
			carData.setEngineNo(checkCarInfo.getEngineNo());
			// carData.setModel(model);
			carData.setDriverName(driver.getDriverName());
			carData.setDriverLicenseNo(driver.getDrivingLicenseNo());
			carData.setVehicleProperty(checkCarVo.getSerialNo()==1 ? "1" : "2");
			if(cMainVo.getComCode().startsWith("10")){
				if(checkCarVo.getSerialNo() == 1){//车辆属性为“本车”时，号牌号码和号牌种类必须与承保车辆信息一致
					List<PrpLCItemCarVo> prpLCItemCarVos = registQueryService.findCItemCarByOther(cMainVo.getRegistNo(),cMainVo.getRiskCode().substring(0, 2));
					if(prpLCItemCarVos != null && prpLCItemCarVos.size() > 0 ){
						PrpLCItemCarVo prpLCItemCarVo = prpLCItemCarVos.get(0);
						carData.setLicensePlateNo(prpLCItemCarVo.getLicenseNo());
						carData.setLicensePlateType(prpLCItemCarVo.getLicenseKindCode());
						if("12".equals(cMainVo.getRiskCode().substring(0, 2))){//商业
							if("25".equals(prpLCItemCarVo.getLicenseKindCode())){
								carData.setLicensePlateType("99");
							}
						}else{//交强
							if("99".equals(prpLCItemCarVo.getLicenseKindCode())){
								carData.setLicensePlateType("25");
							}
						}
					}
				}
			}
			// 出险标的车号牌号码和出险标的车号牌种类必须成对出现！三者车也一样
			if(StringUtils.isBlank(carData.getLicensePlateType()) || StringUtils.isBlank(carData.getLicensePlateNo())){
				carData.setLicensePlateNo("");
				carData.setLicensePlateType("");
			}
			carData.setFieldType("2");
			carData.setEstimatedLossAmount(checkCarVo.getLossFee());
			carData.setCheckerName(checkTaskVo.getChecker());
			carData.setCheckerCode(checkTaskVo.getCheckerCode());
			carData.setCheckerCertiCode(checkTaskVo.getCheckerIdfNo());
			if(wfTaskVo.getHandlerTime()!=null){
				carData.setCheckStartTime(DateUtils.dateToStr(wfTaskVo.getHandlerTime(),DateUtils.YMDHM));
			}
			if(wfTaskVo.getTaskOutTime()!=null){
				carData.setCheckEndTime(DateUtils.dateToStr(wfTaskVo.getTaskOutTime(),DateUtils.YMDHM));
			}else{
				carData.setCheckEndTime(DateUtils.dateToStr(new Date(),DateUtils.YMDHM));
			}
			carData.setCheckAddr(checkTaskVo.getCheckAddress());
			// carData.setCheckDesc(checkDesc);
			carDataList.add(carData);
		}
		// 财产信息
		if(checkPropList!=null&&checkPropList.size()>0){
			for(PrpLCheckPropVo propVo:checkPropList){
				EWCheckPropData propData = new EWCheckPropData();
				propData.setProtectName(propVo.getLossItemName());
				propData.setLossNum(propVo.getLossNum());
				propData.setEstimatedLossAmount(propVo.getLossFee());
				propData.setProtectProperty("1".equals(propVo.getLossPartyId()) ? "1" : "2");
				propData.setFieldType("2");
				if(wfTaskVo.getHandlerTime()!=null){
					propData.setCheckStartTime(DateUtils.dateToStr(wfTaskVo.getHandlerTime(),DateUtils.YMDHM));
				}
				if(wfTaskVo.getTaskOutTime()!=null){
					propData.setCheckEndTime(DateUtils.dateToStr(wfTaskVo.getTaskOutTime(),DateUtils.YMDHM));
				}else{
					propData.setCheckEndTime(DateUtils.dateToStr(new Date(),DateUtils.YMDHM));
				}
				propData.setCheckerName(checkTaskVo.getChecker());
				propData.setCheckerCode(checkTaskVo.getCheckerCode());
				propData.setCheckerCertiCode(checkTaskVo.getCheckerIdfNo());
				propData.setCheckAddr(checkTaskVo.getCheckAddress());
				propDataList.add(propData);
			}
		}
		// 人伤信息
		if(personList!=null&&personList.size()>0){
			for(PrpLCheckPersonVo personVo:personList){
				EWCheckPersonData personData = new EWCheckPersonData();
				personData.setPersonPayType("2".equals(personVo.getPersonPayType()) ? "2" : "1");
				personData.setEstimatedLossAmount(personVo.getLossFee());
				personData.setPersonProperty(getPersonProperty(personVo.getPersonProp()));
				// personData.setTrafficType(trafficType);
				personData.setCheckerName(checkTaskVo.getChecker());
				personData.setCheckerCode(checkTaskVo.getCheckerCode());
				personData.setCheckerCertiCode(checkTaskVo.getCheckerIdfNo());
				if(wfTaskVo.getHandlerTime()!=null){
					personData.setCheckStartTime(DateUtils.dateToStr(wfTaskVo.getHandlerTime(),DateUtils.YMDHM));
				}
				if(wfTaskVo.getTaskOutTime()!=null){
					personData.setCheckEndTime(DateUtils.dateToStr(wfTaskVo.getTaskOutTime(),DateUtils.YMDHM));
				}else{
					personData.setCheckEndTime(DateUtils.dateToStr(new Date(),DateUtils.YMDHM));
				}
				personData.setCheckAddr(checkTaskVo.getCheckAddress());
				personData.setPersonName(personVo.getPersonName());
				personData.setCertiType(CodeTranUtil.findTransCodeDictVo("IdentifyType",personVo.getIdentifyType()).getProperty1());
				personData.setCertiCode(personVo.getIdNo());
				personDataList.add(personData);
			}
		}

		body.setBasePart(basePart);
		body.setCarDataList(carDataList);
		body.setPersonDataList(personDataList);
		body.setPropDataList(propDataList);
		requestVo.setHead(head);
		requestVo.setBody(body);
		requestXML = "<?xml version=\"1.0\" encoding=\"GBK\"?>"+stream.toXML(requestVo);

		return requestXML;
	}

	/**
	 * 获取出险原因
	 * @param damageCode
	 * @param damOtherCode
	 * @return
	 */
	public String getLossCauseCode(String damageCode,String damOtherCode) {
		String lossCauseCode = "";
		if("DM99".equals(damageCode)){
			lossCauseCode = CodeTranUtil.findTransCodeDictVo("DamageCode2",damOtherCode).getProperty1();
		}else{
			lossCauseCode = CodeTranUtil.findTransCodeDictVo("DamageCode",damageCode).getProperty1();
		}
		return lossCauseCode;
	}

	public String getPersonProperty(String str) {
		String personProperty = "";
		if("1".equals(str)){
			personProperty = "2";
		}else if("2".equals(str)){
			personProperty = "3";
		}else if("3".equals(str)){
			personProperty = "1";
		}
		return personProperty;
	}

}
