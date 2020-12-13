package ins.sino.claimcar.regist.platform.spring;

import ins.framework.lang.Springs;
import ins.platform.common.service.facade.CodeTranService;
import ins.platform.common.util.CodeTranUtil;
import ins.platform.utils.ClaimBaseCoder;
import ins.platform.utils.HttpClientHander;
import ins.platform.vo.SysCodeDictVo;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.carinterface.service.ClaimInterfaceLogService;
import ins.sino.claimcar.carinterface.util.BusinessType;
import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.platform.util.SendPlatformUtil;
import ins.sino.claimcar.regist.platform.vo.CarRiskRegistReqVo;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.PrpLCMainService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLCItemCarVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistCarLossVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.trafficplatform.service.EarlyWarnService;
import ins.sino.claimcar.trafficplatform.service.RegistToCarRiskPaltformService;
import ins.sino.claimcar.trafficplatform.vo.CarRiskRegistBasePartReqVo;
import ins.sino.claimcar.trafficplatform.vo.CarRiskRegistBodyReqVo;
import ins.sino.claimcar.trafficplatform.vo.CarRiskRegistLossDataReqVo;
import ins.sino.claimcar.trafficplatform.vo.CarRiskRegistThirdVehicleDataReqVo;
import ins.sino.claimcar.trafficplatform.vo.GdResponseVo;
import ins.sino.claimcar.trafficplatform.vo.HnResponseVo;
import ins.sino.claimcar.trafficplatform.vo.RequestHeadVo;
import ins.sino.claimcar.trafficplatform.vo.ResponseHeadVo;
import ins.sino.claimcar.trafficplatform.vo.SdResponseVo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

/**
 * 报案送平台服务接口实现
 * @author Luwei
 * @CreateTime
 */
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"}, timeout = 1000000)
@Path("registToCarRiskPaltformService")
public class RegistToCarRiskPaltformServiceImpl implements RegistToCarRiskPaltformService {

	private Logger logger = LoggerFactory.getLogger(RegistToCarRiskPaltformServiceImpl.class);

	@Autowired
	private CodeTranService codeTranService;

	@Autowired
	RegistQueryService registQueryService;

	@Autowired
	PolicyViewService policyViewService;
	
	@Autowired
	PrpLCMainService prpLCMainService;
	
	@Autowired
	EarlyWarnService earlyWarnService;
	
    @Autowired
    private ClaimInterfaceLogService interfaceLogService;

	// 报案上传平台,try,catch报案上传平台失败不需要展示在页面
	@Override
	public void sendRegistToCarRiskPlatform(String registNo,SysUserVo userVo,Map<String,String> map)  {
		PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
		// 联共保 从共，从联不交互平台
		if(CodeConstants.GBFlag.MINORCOMMON.equals(registVo.getIsGBFlag())||CodeConstants.GBFlag.MINORRELATION.equals(registVo.getIsGBFlag())){
			return;
		}
		List<PrpLCMainVo> prpLCMains = policyViewService.getPolicyAllInfo(registNo);
		String warnswitch = SpringProperties.getProperty("WARN_SWITCH");// 62,10,50
		for(PrpLCMainVo cMainVo:prpLCMains){// 将信息传递平台
			//解决异步数据未落库问题
			if("1101".equals(cMainVo.getRiskCode())) {
				cMainVo.setClaimSequenceNo(map.get("11"));//交强理赔编码 
			}else { 
				cMainVo.setClaimSequenceNo(map.get("12"));//商业理赔编码
			}
			if(warnswitch.contains(cMainVo.getComCode().substring(0,2))){// prpLCMainVo.getComCode().startsWith("62")
		        try{
				sendRegistToCarRiskPlatformByCmain(registVo,cMainVo,userVo);
	            }catch(Exception e){
	            	logger.error("风险预警平台报错："+cMainVo.getRegistNo(), e);
				}
			}
		}
	}
	
	
	/**
	 * 交强报案推送平台
	 * @param prpLRegistVo
	 * @param prpLCMainVo
	 */
	public void sendRegistToCarRiskPlatformByCmain(PrpLRegistVo prpLRegistVo,PrpLCMainVo prpLCMainVo,SysUserVo userVo) {
		// 联共保 从共，从联不交互平台
		if(CodeConstants.GBFlag.MINORCOMMON.equals(prpLRegistVo.getIsGBFlag())||CodeConstants.GBFlag.MINORRELATION.equals(prpLRegistVo.getIsGBFlag())){
			return;
		}
		//PlatformController controller = PlatformFactory.getInstance(prpLCMainVo.getComCode(),RequestType.RegistInfoCI);
		// 请求body
		String xmlToSend = "";
		String xmlReturn = "";
		CarRiskRegistReqVo carRiskRegistReqVo = new CarRiskRegistReqVo();
		RequestHeadVo requestHeadVo = new RequestHeadVo();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		// 山东平台
		if(prpLCMainVo.getComCode().startsWith("62")&&SendPlatformUtil.isMor(prpLCMainVo)){
        String urlStr = SpringProperties.getProperty("SDWARN_URL");
        String userName = SpringProperties.getProperty("SDWARN_USER");
        String passWord = SpringProperties.getProperty("SDWARN_PW");
        SdResponseVo resVo = new SdResponseVo();
        try{
    		CarRiskRegistBodyReqVo carRiskRegistBodyReqVo = new CarRiskRegistBodyReqVo();
    		CarRiskRegistBasePartReqVo carRiskRegistBasePartReqVo = new CarRiskRegistBasePartReqVo();
    		List<CarRiskRegistThirdVehicleDataReqVo> carRiskRegistThirdVehicleDataReqs = new ArrayList<CarRiskRegistThirdVehicleDataReqVo>();
    		List<CarRiskRegistLossDataReqVo> carRiskRegistLossDataReqs = new ArrayList<CarRiskRegistLossDataReqVo>();
    
    		String dangerRemark = prpLRegistVo.getPrpLRegistExt().getDangerRemark();
    		if(StringUtils.isNotBlank(dangerRemark) && dangerRemark.length() > 1000){
    			dangerRemark = dangerRemark.substring(0,500);
    		}
    		if(StringUtils.isNotBlank(prpLCMainVo.getClaimSequenceNo())){
    		    carRiskRegistBasePartReqVo.setClaimCode(prpLCMainVo.getClaimSequenceNo());//理赔编码
    		}
    		//carRiskRegistBasePartReqVo.setClaimCode("50DHIC370018001528261580262879");//理赔编码
    		if(StringUtils.isNotBlank(prpLCMainVo.getValidNo())){
    		    carRiskRegistBasePartReqVo.setConfirmSequenceNo(prpLCMainVo.getValidNo());
            }
    		carRiskRegistBasePartReqVo.setPolicyNo(prpLCMainVo.getPolicyNo());
    		carRiskRegistBasePartReqVo.setReporterName(prpLRegistVo.getReportorName());
    		carRiskRegistBasePartReqVo.setReportNo(prpLRegistVo.getRegistNo());
    		//carRiskRegistBasePartReqVo.setReportTime(DateUtils.dateToStr(prpLRegistVo.getReportTime(),DateUtils.YMDHM));
    		String reportTime = sdf.format(prpLRegistVo.getReportTime()).trim();
    		reportTime = reportTime.replaceAll("-","");
    		reportTime = reportTime.replaceAll(" ","");
    		reportTime = reportTime.replaceAll(":","");
    		carRiskRegistBasePartReqVo.setReportTime(reportTime);
    		carRiskRegistBasePartReqVo.setReporterPhone(prpLRegistVo.getReportorPhone());
    		//待确认
    		/*carRiskRegistBasePartReqVo.setReporterCode("");//报案人证件类型
    		carRiskRegistBasePartReqVo.setReporterIdNo(prpLRegistVo.getReportorIdfNo());//报案人证件号码
    		 */
    		carRiskRegistBasePartReqVo.setDriverName(prpLRegistVo.getDriverName());
    		String damageTime = sdf.format(prpLRegistVo.getDamageTime()).trim();
    		damageTime = damageTime.replaceAll("-","");
    		damageTime = damageTime.replaceAll(" ","");
    		damageTime = damageTime.replaceAll(":","");
    	    carRiskRegistBasePartReqVo.setAccidentTime(damageTime);
    	    String address = prpLRegistVo.getDamageAddress();
            if(StringUtils.isEmpty(address)){
                address = prpLRegistVo.getPrpLRegistExt().getCheckAddress();
            }
            if(address.length() > 100){
                address = address.substring(0,100);
            }
    	    carRiskRegistBasePartReqVo.setAccidentPlace(address);
    	    String remark = prpLRegistVo.getPrpLRegistExt().getDangerRemark();
	        if(StringUtils.isNotBlank(remark) && remark.length() > 1000){
	            remark = remark.substring(0,1000);
	        }
    	    carRiskRegistBasePartReqVo.setAccidentDescription(remark);
    	    carRiskRegistBasePartReqVo.setDriverLicenseNo(prpLRegistVo.getDrivingNo());
            SysCodeDictVo sysVo = codeTranService.findTransCodeDictVo("DamageCode",prpLRegistVo.getDamageCode());
            if(sysVo==null){
                sysVo = codeTranService.findTransCodeDictVo("DamageCode2",prpLRegistVo.getDamageCode());
            }
            carRiskRegistBasePartReqVo.setAccidentCause(sysVo.getProperty1());// 出险原因
            //carRiskRegistBasePartReqVo.setLicensePlateNo(prpLRegistVo.getLicense());
            carRiskRegistBasePartReqVo.setAccidentLiability(getPlatDamageReason("IndemnityDuty",prpLRegistVo.getPrpLRegistExt().getObliGation()));
            carRiskRegistBasePartReqVo.setManageType(prpLRegistVo.getPrpLRegistExt().getManageType());
            String licenseNo = "";
    		String vehicleType = "99";
    		for(PrpLRegistCarLossVo prpLRegistCarLossVo:prpLRegistVo.getPrpLRegistCarLosses()){
    			if("1".equals(prpLRegistCarLossVo.getLossparty())){// 标的车
    				licenseNo = prpLRegistCarLossVo.getLicenseNo();
    				vehicleType = prpLRegistCarLossVo.getLicenseType();
    				if("25".equals(vehicleType)){//其他号牌特殊处理
    					vehicleType = "99";
    				}
    			}else{// 三者车
    			    CarRiskRegistThirdVehicleDataReqVo carRiskRegistThirdVehicleDataReqVo = new CarRiskRegistThirdVehicleDataReqVo();
    				String thLicenseNo = prpLRegistCarLossVo.getLicenseNo();
    				if(StringUtils.isNotBlank(thLicenseNo)){//三者车辆号牌号码和三者车辆号牌种类必须成对出现！
    				    carRiskRegistThirdVehicleDataReqVo.setLicensePlateNo(thLicenseNo);
    				    carRiskRegistThirdVehicleDataReqVo.setLicensePlateType("02");// TODO
    				}
    				carRiskRegistThirdVehicleDataReqVo.setDriverName(prpLRegistCarLossVo.getThriddrivername());
    				carRiskRegistThirdVehicleDataReqs.add(carRiskRegistThirdVehicleDataReqVo);
    			}
    		}
    		// 出险标的车号牌号码和出险标的车号牌种类必须成对出现！
    		if(StringUtils.isNotBlank(vehicleType)&&StringUtils.isNotBlank(licenseNo)){
    		    carRiskRegistBasePartReqVo.setLicensePlateType(vehicleType);
    		    carRiskRegistBasePartReqVo.setLicensePlateNo(licenseNo);
    		}
    		
    		CarRiskRegistLossDataReqVo registLossDataReqVo = new CarRiskRegistLossDataReqVo();//车
    		registLossDataReqVo.setLossFeeType("1");
    		carRiskRegistLossDataReqs.add(registLossDataReqVo);
    		if("1".equals(prpLRegistVo.getPrpLRegistExt().getIsPropLoss())){//财
    		    CarRiskRegistLossDataReqVo carRiskRegistLossDataReqVo = new CarRiskRegistLossDataReqVo();
                carRiskRegistLossDataReqVo.setLossFeeType("2");
                carRiskRegistLossDataReqs.add(carRiskRegistLossDataReqVo);
            }
            if("1".equals(prpLRegistVo.getPrpLRegistExt().getIsPersonLoss())){//人
                CarRiskRegistLossDataReqVo carRiskRegistLossDataReqVo = new CarRiskRegistLossDataReqVo();
                carRiskRegistLossDataReqVo.setLossFeeType("3");
                carRiskRegistLossDataReqs.add(carRiskRegistLossDataReqVo);
            }
    		
            carRiskRegistBodyReqVo.setCarRiskRegistBasePartReqVo(carRiskRegistBasePartReqVo);
            carRiskRegistBodyReqVo.setCarRiskRegistThirdVehicleDataReqs(carRiskRegistThirdVehicleDataReqs);
            carRiskRegistBodyReqVo.setCarRiskRegistLossDataReqs(carRiskRegistLossDataReqs);

            requestHeadVo.setRequestType("C0301");
            requestHeadVo.setUser(userName);
            requestHeadVo.setPassword(passWord);
            carRiskRegistReqVo.setBody(carRiskRegistBodyReqVo);
            carRiskRegistReqVo.setHead(requestHeadVo);
            xmlToSend = ClaimBaseCoder.objToXml(carRiskRegistReqVo);
            logger.info("xmlToSend======="+xmlToSend);
            xmlReturn = earlyWarnService.requestSDEW(xmlToSend,urlStr);
            logger.info("xmlReturn======="+xmlReturn);
            resVo = ClaimBaseCoder.xmlToObj(xmlReturn, SdResponseVo.class);
        }
        catch(Exception e){
            e.printStackTrace();
        }finally{
            //保存日志
            //待续发送报文
            ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
            String requestXml = "";
            String responseXml = "";
            xmlToSend = ClaimBaseCoder.objToXmlUtf(carRiskRegistReqVo);
            try{
                logVo.setBusinessType(BusinessType.SDEW_regist.name());
                logVo.setBusinessName(BusinessType.SDEW_regist.getName());
                logVo.setCompensateNo(prpLCMainVo.getClaimSequenceNo());
                XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
                stream.autodetectAnnotations(true);
                stream.setMode(XStream.NO_REFERENCES);
                stream.aliasSystemAttribute(null,"class");// 去掉 class属性
                requestXml = stream.toXML(carRiskRegistReqVo);
                responseXml = stream.toXML(resVo);
                ResponseHeadVo responseHeadVo = resVo.getHeadVo();
                if("1".equals(responseHeadVo.getResponseCode())){
                    logVo.setStatus("1");
                    logVo.setErrorCode("true");
                }else {
                    logVo.setStatus("0");
                    logVo.setErrorCode("false");
                }
                logVo.setErrorMessage(responseHeadVo.getErrorMessage());
            }
            catch(Exception e){
                logVo.setStatus("0");
                logVo.setErrorCode("false");
                e.printStackTrace();
            }
            finally{
                logVo.setRegistNo(prpLRegistVo.getRegistNo());
                logVo.setOperateNode(FlowNode.Regis.name());
                logVo.setComCode(userVo.getComCode());
                Date date = new Date();
                logVo.setRequestTime(date);
                logVo.setRequestUrl(urlStr);
                logVo.setCreateUser(userVo.getUserCode());
                logVo.setCreateTime(date);
                logVo.setRequestXml(requestXml);
                logVo.setResponseXml(responseXml);
                interfaceLogService.save(logVo);
            }
			}
			// 广东平台
		}else if(prpLCMainVo.getComCode().startsWith("10")&&SendPlatformUtil.isMor(prpLCMainVo)){
			Boolean isRegist = true;// 是否发送报文
			String errorMessage = null;// 发送报文管控-错误信息
			String urlStr = SpringProperties.getProperty("GDWARN_URL");// 广东平台地址
			GdResponseVo resVo = new GdResponseVo();
			try{
				CarRiskRegistBodyReqVo carRiskRegistBodyReqVo = new CarRiskRegistBodyReqVo();
				CarRiskRegistBasePartReqVo carRiskRegistBasePartReqVo = new CarRiskRegistBasePartReqVo();
				List<CarRiskRegistThirdVehicleDataReqVo> carRiskRegistThirdVehicleDataReqs = new ArrayList<CarRiskRegistThirdVehicleDataReqVo>();
				List<CarRiskRegistLossDataReqVo> carRiskRegistLossDataReqs = new ArrayList<CarRiskRegistLossDataReqVo>();

				String dangerRemark = prpLRegistVo.getPrpLRegistExt().getDangerRemark();
				if(StringUtils.isNotBlank(dangerRemark)&&dangerRemark.length()>1000){
					dangerRemark = dangerRemark.substring(0,500);
				}
				if(StringUtils.isNotBlank(prpLCMainVo.getClaimSequenceNo())){
					carRiskRegistBasePartReqVo.setClaimCode(prpLCMainVo.getClaimSequenceNo());// 理赔编码
				}
				if(StringUtils.isNotBlank(prpLCMainVo.getValidNo())){
					carRiskRegistBasePartReqVo.setConfirmSequenceNo(prpLCMainVo.getValidNo());
				}
				carRiskRegistBasePartReqVo.setPolicyNo(prpLCMainVo.getPolicyNo());
				carRiskRegistBasePartReqVo.setReporterName(prpLRegistVo.getReportorName());
				carRiskRegistBasePartReqVo.setReportNo(prpLRegistVo.getRegistNo());

				String reportTime = sdf.format(prpLRegistVo.getReportTime()).trim();
				reportTime = reportTime.replaceAll("-","");
				reportTime = reportTime.replaceAll(" ","");
				reportTime = reportTime.replaceAll(":","");
				carRiskRegistBasePartReqVo.setReportTime(reportTime);
				carRiskRegistBasePartReqVo.setReporterPhone(prpLRegistVo.getReportorPhone());
				carRiskRegistBasePartReqVo.setDriverName(prpLRegistVo.getDriverName());
				String damageTime = sdf.format(prpLRegistVo.getDamageTime()).trim();
				damageTime = damageTime.replaceAll("-","");
				damageTime = damageTime.replaceAll(" ","");
				damageTime = damageTime.replaceAll(":","");
				carRiskRegistBasePartReqVo.setAccidentTime(damageTime);
				String address = prpLRegistVo.getDamageAddress();
				if(StringUtils.isEmpty(address)){
					address = prpLRegistVo.getPrpLRegistExt().getCheckAddress();
				}
				if(address.length()>100){
					address = address.substring(0,100);
				}
				carRiskRegistBasePartReqVo.setAccidentPlace(address);
				String remark = prpLRegistVo.getPrpLRegistExt().getDangerRemark();
				if(StringUtils.isNotBlank(remark)&&remark.length()>1000){
					remark = remark.substring(0,1000);
				}
				carRiskRegistBasePartReqVo.setAccidentDescription(remark);
				carRiskRegistBasePartReqVo.setDriverLicenseNo(prpLRegistVo.getDrivingNo());
				SysCodeDictVo sysVo = codeTranService.findTransCodeDictVo("DamageCode",prpLRegistVo.getDamageCode());
				if(sysVo==null){
					sysVo = codeTranService.findTransCodeDictVo("DamageCode2",prpLRegistVo.getDamageCode());
				}
				carRiskRegistBasePartReqVo.setAccidentCause(sysVo.getProperty1());// 出险原因
				carRiskRegistBasePartReqVo.setAccidentLiability(getPlatDamageReason("IndemnityDuty",prpLRegistVo.getPrpLRegistExt().getObliGation()));
				carRiskRegistBasePartReqVo.setManageType(prpLRegistVo.getPrpLRegistExt().getManageType());
				String licenseNo = "";
				String vehicleType = "99";
				for(PrpLRegistCarLossVo prpLRegistCarLossVo:prpLRegistVo.getPrpLRegistCarLosses()){
					if("1".equals(prpLRegistCarLossVo.getLossparty())){// 标的车
						licenseNo = prpLRegistCarLossVo.getLicenseNo();
						vehicleType = prpLRegistCarLossVo.getLicenseType();
						if("25".equals(vehicleType)){// 其他号牌特殊处理
							vehicleType = "99";
						}
					}else{// 三者车
						CarRiskRegistThirdVehicleDataReqVo carRiskRegistThirdVehicleDataReqVo = new CarRiskRegistThirdVehicleDataReqVo();
						String thLicenseNo = prpLRegistCarLossVo.getLicenseNo();
						if(StringUtils.isNotBlank(thLicenseNo)){// 三者车辆号牌号码和三者车辆号牌种类必须成对出现！
							carRiskRegistThirdVehicleDataReqVo.setLicensePlateNo(thLicenseNo);
							carRiskRegistThirdVehicleDataReqVo.setLicensePlateType("02");// TODO
						}
						carRiskRegistThirdVehicleDataReqVo.setDriverName(prpLRegistCarLossVo.getThriddrivername());
						carRiskRegistThirdVehicleDataReqs.add(carRiskRegistThirdVehicleDataReqVo);
					}
				}
				// 出险标的车号牌号码和出险标的车号牌种类必须成对出现！
				if(StringUtils.isNotBlank(vehicleType)&&StringUtils.isNotBlank(licenseNo)){
					PrpLCItemCarVo itemcar = prpLCMainVo.getPrpCItemCars().get(0);// 这是承保的车辆信息
					if(itemcar!=null){
						if(licenseNo.equals(itemcar.getLicenseNo())&&vehicleType.equals(itemcar.getVehicleStyle())){
							isRegist = true;
						}else{
							errorMessage = "报案上传的出险标的车必须与承保车辆信息号牌号码、号牌种类一致";
						}
					}else{
						errorMessage = "没有承保车辆信息";
					}
					carRiskRegistBasePartReqVo.setLicensePlateType(vehicleType);
					carRiskRegistBasePartReqVo.setLicensePlateNo(licenseNo);
				}

				CarRiskRegistLossDataReqVo registLossDataReqVo = new CarRiskRegistLossDataReqVo();// 车
				registLossDataReqVo.setLossFeeType("1");
				carRiskRegistLossDataReqs.add(registLossDataReqVo);
				if("1".equals(prpLRegistVo.getPrpLRegistExt().getIsPropLoss())){// 财
					CarRiskRegistLossDataReqVo carRiskRegistLossDataReqVo = new CarRiskRegistLossDataReqVo();
					carRiskRegistLossDataReqVo.setLossFeeType("2");
					carRiskRegistLossDataReqs.add(carRiskRegistLossDataReqVo);
				}
				if("1".equals(prpLRegistVo.getPrpLRegistExt().getIsPersonLoss())){// 人
					CarRiskRegistLossDataReqVo carRiskRegistLossDataReqVo = new CarRiskRegistLossDataReqVo();
					carRiskRegistLossDataReqVo.setLossFeeType("3");
					carRiskRegistLossDataReqs.add(carRiskRegistLossDataReqVo);
				}

				carRiskRegistBodyReqVo.setCarRiskRegistBasePartReqVo(carRiskRegistBasePartReqVo);
				carRiskRegistBodyReqVo.setCarRiskRegistThirdVehicleDataReqs(carRiskRegistThirdVehicleDataReqs);
				carRiskRegistBodyReqVo.setCarRiskRegistLossDataReqs(carRiskRegistLossDataReqs);

				requestHeadVo.setRequestType("C0301");
				String user = SpringProperties.getProperty("GDWARN_USER");// 获取数据库配置用户名
				String passWord = SpringProperties.getProperty("GDWARN_PW");
				requestHeadVo.setUser(user);
				requestHeadVo.setPassword(passWord);
				carRiskRegistReqVo.setBody(carRiskRegistBodyReqVo);
				carRiskRegistReqVo.setHead(requestHeadVo);
				if(isRegist){// 出险标的车与承保标的车管控校验
					xmlToSend = ClaimBaseCoder.objToXml(carRiskRegistReqVo);
					logger.info("xmlToSend======="+xmlToSend);
					xmlReturn = earlyWarnService.requestSDEW(xmlToSend,urlStr);
					logger.info("xmlReturn======="+xmlReturn);
					resVo = ClaimBaseCoder.xmlToObj(xmlReturn,GdResponseVo.class);
				}else{
					logger.error(errorMessage);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			finally{
					// 保存日志
					// 待续发送报文
					ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
					String requestXml = "";
					String responseXml = "";
					xmlToSend = ClaimBaseCoder.objToXmlUtf(carRiskRegistReqVo);
					try{
						logVo.setBusinessType(BusinessType.GDEW_regist.name());
						logVo.setBusinessName(BusinessType.GDEW_regist.getName());
						logVo.setCompensateNo(prpLCMainVo.getClaimSequenceNo());
						XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
						stream.autodetectAnnotations(true);
						stream.setMode(XStream.NO_REFERENCES);
						stream.aliasSystemAttribute(null,"class");// 去掉 class属性
					requestXml = stream.toXML(carRiskRegistReqVo);
						responseXml = stream.toXML(resVo);
						ResponseHeadVo responseHeadVo = resVo.getHeadVo();
						if("1".equals(responseHeadVo.getResponseCode())){
							logVo.setStatus("1");
							logVo.setErrorCode("true");
						}else{
							logVo.setStatus("0");
							logVo.setErrorCode("false");
						}
						logVo.setErrorMessage(responseHeadVo.getErrorMessage());
					}catch(Exception e){
						logVo.setStatus("0");
						logVo.setErrorCode("false");
						e.printStackTrace();
					}
					finally{
						logVo.setRegistNo(prpLRegistVo.getRegistNo());
						logVo.setOperateNode(FlowNode.Regis.name());
						logVo.setComCode(userVo.getComCode());
						Date date = new Date();
						logVo.setRequestTime(date);
						logVo.setRequestUrl(urlStr);
						logVo.setCreateUser(userVo.getUserCode());
						logVo.setCreateTime(date);
						logVo.setRequestXml(requestXml);
						logVo.setResponseXml(responseXml);
					if(interfaceLogService==null){
						interfaceLogService = (ClaimInterfaceLogService)Springs.getBean(ClaimInterfaceLogService.class);
						interfaceLogService.save(logVo);
					}else{
						interfaceLogService.save(logVo);
					}
					}
			}
			// 河南平台
		}else if(prpLCMainVo.getComCode().startsWith("50")&&SendPlatformUtil.isMor(prpLCMainVo)){
			String urlStr = SpringProperties.getProperty("HNWARN_URL");
			HnResponseVo resVo = new HnResponseVo();
			try{
				CarRiskRegistBodyReqVo carRiskRegistBodyReqVo = new CarRiskRegistBodyReqVo();
				CarRiskRegistBasePartReqVo carRiskRegistBasePartReqVo = new CarRiskRegistBasePartReqVo();
				List<CarRiskRegistThirdVehicleDataReqVo> carRiskRegistThirdVehicleDataReqs = new ArrayList<CarRiskRegistThirdVehicleDataReqVo>();
				List<CarRiskRegistLossDataReqVo> carRiskRegistLossDataReqs = new ArrayList<CarRiskRegistLossDataReqVo>();

				String dangerRemark = prpLRegistVo.getPrpLRegistExt().getDangerRemark();
				if(StringUtils.isNotBlank(dangerRemark)&&dangerRemark.length()>1000){
					dangerRemark = dangerRemark.substring(0,500);
				}
				if(StringUtils.isNotBlank(prpLCMainVo.getClaimSequenceNo())){
					carRiskRegistBasePartReqVo.setClaimCode(prpLCMainVo.getClaimSequenceNo());// 理赔编码
				}
				// carRiskRegistBasePartReqVo.setClaimCode("50DHIC370018001528261580262879");//理赔编码
				if(StringUtils.isNotBlank(prpLCMainVo.getValidNo())){
					carRiskRegistBasePartReqVo.setConfirmSequenceNo(prpLCMainVo.getValidNo());
				}
				carRiskRegistBasePartReqVo.setPolicyNo(prpLCMainVo.getPolicyNo());
				carRiskRegistBasePartReqVo.setReporterName(prpLRegistVo.getReportorName());
				carRiskRegistBasePartReqVo.setReportNo(prpLRegistVo.getRegistNo());
				// carRiskRegistBasePartReqVo.setReportTime(DateUtils.dateToStr(prpLRegistVo.getReportTime(),DateUtils.YMDHM));
				String reportTime = sdf.format(prpLRegistVo.getReportTime()).trim();
				reportTime = reportTime.replaceAll("-","");
				reportTime = reportTime.replaceAll(" ","");
				reportTime = reportTime.replaceAll(":","");
				carRiskRegistBasePartReqVo.setReportTime(reportTime);
				carRiskRegistBasePartReqVo.setReporterPhone(prpLRegistVo.getReportorPhone());
				// 待确认
				/*carRiskRegistBasePartReqVo.setReporterCode("");//报案人证件类型
				carRiskRegistBasePartReqVo.setReporterIdNo(prpLRegistVo.getReportorIdfNo());//报案人证件号码
				 */
				carRiskRegistBasePartReqVo.setDriverName(prpLRegistVo.getDriverName());
				String damageTime = sdf.format(prpLRegistVo.getDamageTime()).trim();
				damageTime = damageTime.replaceAll("-","");
				damageTime = damageTime.replaceAll(" ","");
				damageTime = damageTime.replaceAll(":","");
				carRiskRegistBasePartReqVo.setAccidentTime(damageTime);
				String address = prpLRegistVo.getDamageAddress();
				if(StringUtils.isEmpty(address)){
					address = prpLRegistVo.getPrpLRegistExt().getCheckAddress();
				}
				if(address.length()>100){
					address = address.substring(0,100);
				}
				carRiskRegistBasePartReqVo.setAccidentPlace(address);
				String remark = prpLRegistVo.getPrpLRegistExt().getDangerRemark();
				if(StringUtils.isNotBlank(remark)&&remark.length()>1000){
					remark = remark.substring(0,1000);
				}
				carRiskRegistBasePartReqVo.setAccidentDescription(remark);
				carRiskRegistBasePartReqVo.setDriverLicenseNo(prpLRegistVo.getDrivingNo());
				SysCodeDictVo sysVo = codeTranService.findTransCodeDictVo("DamageCode",prpLRegistVo.getDamageCode());
				if(sysVo==null){
					sysVo = codeTranService.findTransCodeDictVo("DamageCode2",prpLRegistVo.getDamageCode());
				}
				carRiskRegistBasePartReqVo.setAccidentCause(sysVo.getProperty1());// 出险原因
				// carRiskRegistBasePartReqVo.setLicensePlateNo(prpLRegistVo.getLicense());
				carRiskRegistBasePartReqVo.setAccidentLiability(getPlatDamageReason("IndemnityDuty",prpLRegistVo.getPrpLRegistExt().getObliGation()));
				carRiskRegistBasePartReqVo.setManageType(prpLRegistVo.getPrpLRegistExt().getManageType());
				String licenseNo = "";
				String vehicleType = "99";
				for(PrpLRegistCarLossVo prpLRegistCarLossVo:prpLRegistVo.getPrpLRegistCarLosses()){
					if("1".equals(prpLRegistCarLossVo.getLossparty())){// 标的车
						licenseNo = prpLRegistCarLossVo.getLicenseNo();
						vehicleType = prpLRegistCarLossVo.getLicenseType();
						if("25".equals(vehicleType)){// 其他号牌特殊处理
							vehicleType = "99";
						}
					}else{// 三者车
						CarRiskRegistThirdVehicleDataReqVo carRiskRegistThirdVehicleDataReqVo = new CarRiskRegistThirdVehicleDataReqVo();
						String thLicenseNo = prpLRegistCarLossVo.getLicenseNo();
						if(StringUtils.isNotBlank(thLicenseNo)){// 三者车辆号牌号码和三者车辆号牌种类必须成对出现！
							carRiskRegistThirdVehicleDataReqVo.setLicensePlateNo(thLicenseNo);
							carRiskRegistThirdVehicleDataReqVo.setLicensePlateType("02");// TODO
						}
						carRiskRegistThirdVehicleDataReqVo.setDriverName(prpLRegistCarLossVo.getThriddrivername());
						carRiskRegistThirdVehicleDataReqs.add(carRiskRegistThirdVehicleDataReqVo);
					}
				}
				// 出险标的车号牌号码和出险标的车号牌种类必须成对出现！
				if(StringUtils.isNotBlank(vehicleType)&&StringUtils.isNotBlank(licenseNo)){
					carRiskRegistBasePartReqVo.setLicensePlateType(vehicleType);
					carRiskRegistBasePartReqVo.setLicensePlateNo(licenseNo);
				}

				CarRiskRegistLossDataReqVo registLossDataReqVo = new CarRiskRegistLossDataReqVo();// 车
				registLossDataReqVo.setLossFeeType("1");
				carRiskRegistLossDataReqs.add(registLossDataReqVo);
				if("1".equals(prpLRegistVo.getPrpLRegistExt().getIsPropLoss())){// 财
					CarRiskRegistLossDataReqVo carRiskRegistLossDataReqVo = new CarRiskRegistLossDataReqVo();
					carRiskRegistLossDataReqVo.setLossFeeType("2");
					carRiskRegistLossDataReqs.add(carRiskRegistLossDataReqVo);
				}
				if("1".equals(prpLRegistVo.getPrpLRegistExt().getIsPersonLoss())){// 人
					CarRiskRegistLossDataReqVo carRiskRegistLossDataReqVo = new CarRiskRegistLossDataReqVo();
					carRiskRegistLossDataReqVo.setLossFeeType("3");
					carRiskRegistLossDataReqs.add(carRiskRegistLossDataReqVo);
				}

				carRiskRegistBodyReqVo.setCarRiskRegistBasePartReqVo(carRiskRegistBasePartReqVo);
				carRiskRegistBodyReqVo.setCarRiskRegistThirdVehicleDataReqs(carRiskRegistThirdVehicleDataReqs);
				carRiskRegistBodyReqVo.setCarRiskRegistLossDataReqs(carRiskRegistLossDataReqs);

				requestHeadVo.setRequestType("C0301");
				String user = SpringProperties.getProperty("HNWARN_USER");// 获取数据库配置用户名
				String passWord = SpringProperties.getProperty("HNWARN_PW");
				requestHeadVo.setUser(user);
				requestHeadVo.setPassword(passWord);
				carRiskRegistReqVo.setBody(carRiskRegistBodyReqVo);
				carRiskRegistReqVo.setHead(requestHeadVo);
				xmlToSend = ClaimBaseCoder.objToXml(carRiskRegistReqVo);
				logger.info("xmlToSend======="+xmlToSend);
				xmlReturn = earlyWarnService.requestSDEW(xmlToSend,urlStr);
				logger.info("xmlReturn======="+xmlReturn);
				resVo = ClaimBaseCoder.xmlToObj(xmlReturn,HnResponseVo.class);
			}catch(Exception e){
				e.printStackTrace();
			}
			finally{
				// 保存日志
				// 待续发送报文
				ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
				String requestXml = "";
				String responseXml = "";
				xmlToSend = ClaimBaseCoder.objToXmlUtf(carRiskRegistReqVo);
				try{
					logVo.setBusinessType(BusinessType.HNEW_regist.name());
					logVo.setBusinessName(BusinessType.HNEW_regist.getName());
					logVo.setCompensateNo(prpLCMainVo.getClaimSequenceNo());
					XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
					stream.autodetectAnnotations(true);
					stream.setMode(XStream.NO_REFERENCES);
					stream.aliasSystemAttribute(null,"class");// 去掉 class属性
					requestXml = stream.toXML(carRiskRegistReqVo);
					responseXml = stream.toXML(resVo);
					ResponseHeadVo responseHeadVo = resVo.getHeadVo();
					if(responseHeadVo != null){
						if("1".equals(responseHeadVo.getResponseCode())){
							logVo.setStatus("1");
							logVo.setErrorCode("true");
						}else{
							logVo.setStatus("0");
							logVo.setErrorCode("false");
						}
					}else{
						logVo.setStatus("0");
						logVo.setErrorCode("false");
					}
					logVo.setErrorMessage(responseHeadVo.getErrorMessage());
				}catch(Exception e){
					logVo.setStatus("0");
					logVo.setErrorCode("false");
					e.printStackTrace();
				}
				finally{
					logVo.setRegistNo(prpLRegistVo.getRegistNo());
					logVo.setOperateNode(FlowNode.Regis.name());
					logVo.setComCode(userVo.getComCode());
					Date date = new Date();
					logVo.setRequestTime(date);
					logVo.setRequestUrl(urlStr);
					logVo.setCreateUser(userVo.getUserCode());
					logVo.setCreateTime(date);
					logVo.setRequestXml(requestXml);
					logVo.setResponseXml(responseXml);
					interfaceLogService.save(logVo);
				}
			}
        }
	}


	/**
	 * @param damageCode
	 * @return
	 */
	private String getPlatDamageReason(String codeType,String damageCode) {
		return CodeTranUtil.findTransCodeDictVo(codeType,damageCode).getProperty1();
	}

    @Override
    public void sendRegistToCarRiskPlatformRe(String registNo,String policyNo,SysUserVo userVo) {
        PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
        PrpLCMainVo policyInfo = policyViewService.findPolicyInfoByPaltform(registNo,policyNo);
        try{
            sendRegistToCarRiskPlatformByCmain(registVo,policyInfo,userVo);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
