package ins.sino.claimcar.regist.platform.spring;

import freemarker.template.utility.StringUtil;
import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.platform.common.service.facade.AreaDictService;
import ins.platform.common.service.facade.CodeTranService;
import ins.platform.common.util.CodeTranUtil;
import ins.platform.common.util.ConfigUtil;
import ins.platform.utils.DateUtils;
import ins.platform.vo.PrpLConfigValueVo;
import ins.platform.vo.SysCodeDictVo;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.ErrorCode;
import ins.sino.claimcar.CodeConstants.JobStatus;
import ins.sino.claimcar.carplatform.constant.RequestType;
import ins.sino.claimcar.carplatform.controller.PlatformController;
import ins.sino.claimcar.carplatform.po.CiClaimPlatformLog;
import ins.sino.claimcar.carplatform.service.CiClaimPlatformLogService;
import ins.sino.claimcar.carplatform.util.PlatformFactory;
import ins.sino.claimcar.carplatform.util.PlatformLogUtil;
import ins.sino.claimcar.carplatform.vo.CiClaimPlatformLogVo;
import ins.sino.claimcar.carplatform.vo.CiResponseHeadVo;
import ins.sino.claimcar.claim.service.InterfaceAsyncService;
import ins.sino.claimcar.constant.Risk;
import ins.sino.claimcar.platform.service.RegistToPaltformService;
import ins.sino.claimcar.platform.util.SendPlatformUtil;
import ins.sino.claimcar.regist.platform.vo.BiRegistBasePartReqVo;
import ins.sino.claimcar.regist.platform.vo.BiRegistBodyReqVo;
import ins.sino.claimcar.regist.platform.vo.BiRegistLossDataReqVo;
import ins.sino.claimcar.regist.platform.vo.BiRegistThirdVehicleDataReqVo;
import ins.sino.claimcar.regist.platform.vo.BiReportPhoneNoDataReqVo;
import ins.sino.claimcar.regist.platform.vo.CiRegistBasePartReqVo;
import ins.sino.claimcar.regist.platform.vo.CiRegistBodyReqVo;
import ins.sino.claimcar.regist.platform.vo.CiRegistLossDataReqVo;
import ins.sino.claimcar.regist.platform.vo.CiRegistThirdVehicleDataReqVo;
import ins.sino.claimcar.regist.platform.vo.CiRrportPhoneDataVo;
import ins.sino.claimcar.regist.platform.vo.sh.SHBIRegistReqBasePartVo;
import ins.sino.claimcar.regist.platform.vo.sh.SHBIRegistReqBodyVo;
import ins.sino.claimcar.regist.platform.vo.sh.SHBIRegistReqObjDataVo;
import ins.sino.claimcar.regist.platform.vo.sh.SHBIRegistReqThirdVehicleDataVo;
import ins.sino.claimcar.regist.platform.vo.sh.SHCIRegistReqBasePartVo;
import ins.sino.claimcar.regist.platform.vo.sh.SHCIRegistReqBodyVo;
import ins.sino.claimcar.regist.platform.vo.sh.SHCIRegistReqObjDataVo;
import ins.sino.claimcar.regist.po.PrpLRegist;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.PrpLCMainService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLCItemCarVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistCarLossVo;
import ins.sino.claimcar.regist.vo.PrpLRegistExtVo;
import ins.sino.claimcar.regist.vo.PrpLRegistPersonLossVo;
import ins.sino.claimcar.regist.vo.PrpLRegistPropLossVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.trafficplatform.service.RegistToCarRiskPaltformService;
import ins.sino.claimcar.trafficplatform.service.SdpoliceCaseService;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.Path;

import ins.sino.claimcar.utils.HttpUtils;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service; 

import org.springframework.core.SpringProperties;



/**
 * ?????????????????????????????????
 * @author Luwei
 * @CreateTime
 */
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"}, timeout = 1000000)
@Path("registToPaltformService")
public class RegistToPaltformServiceImpl implements RegistToPaltformService {

	private Logger logger = LoggerFactory.getLogger(RegistToPaltformServiceImpl.class);

	@Autowired
	private CodeTranService codeTranService;

	@Autowired
	RegistQueryService registQueryService;

	@Autowired
	PolicyViewService policyViewService;
	
	@Autowired
	PrpLCMainService prpLCMainService;
	
    @Autowired
    RegistToCarRiskPaltformService registToCarRiskPaltformService;

	@Autowired
	SdpoliceCaseService sdpoliceCaseService;
    
    @Autowired
    CiClaimPlatformLogService ciClaimPlatformLogService;
    
    @Autowired
    DatabaseDao databaseDao;

	@Autowired
	private AreaDictService areaDictService;
	
	@Autowired
	InterfaceAsyncService interfaceAsyncService;


	// ??????????????????,try,catch????????????????????????????????????????????????
	@Override
	public void sendRegistToPlatform(String registNo) {
		PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
		List<PrpLCMainVo> prpLCMains = policyViewService.getPolicyAllInfo(registNo);
		//????????????
		Map<String,String> queNoMap=new HashMap<String,String>();
		for(PrpLCMainVo cMainVo:prpLCMains){
			try{
				String comCode = cMainVo.getComCode();
				String riskCode = cMainVo.getRiskCode();
				// ????????????,// ????????????
				if(StringUtils.isNotBlank(comCode)&&"22".equals(comCode.substring(0,2))){
					if(Risk.DQZ.equals(riskCode)){
						sendRegistToSHPlatformCI(registVo,cMainVo);
					}else{
						sendRegistToSHPlatformBI(registVo,cMainVo);
					}
				}else{// ????????????
					if(Risk.DQZ.equals(riskCode)){
						String claimSeqNo=sendRegistToPlatformCI(registVo,cMainVo);
						queNoMap.put("11",claimSeqNo);//??????????????????
						
					}else{
						String claimSeqNo=sendRegistToPlatformBI(registVo,cMainVo);
						queNoMap.put("12",claimSeqNo);//??????????????????
					}
				}
			} catch (Exception e) {
				logger.info("????????????" + registNo + "?????????????????????", e);
				e.printStackTrace();
			}
		}
		// ????????????
        SysUserVo userVo = new SysUserVo();
        userVo.setComCode("00000000");
        userVo.setUserCode("0000000000");
        try {
        	interfaceAsyncService.sendSdRiskWarning(registNo, userVo,queNoMap);
        }catch(Exception e){
        	//??????????????????
        	logger.info("??????????????????-----------------???{}",e.getMessage());
        }
        
	}
	
	/**
	 * ????????????????????????
	 * @param CiClaimPlatformLogVo
	 */
	@Override
	public void uploadRegistToPaltform(CiClaimPlatformLogVo logVo) {
		String requestType = logVo.getRequestType();
		logger.debug("requestType:"+requestType);
		String registNo = logVo.getBussNo();
		String comCode = logVo.getComCode();
		PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
//		PrpLCMainVo prpLCMainVo = policyViewService.getPolicyInfo(registNo,registVo.getPolicyNo());
		List<PrpLCMainVo> prpLCMains = policyViewService.getPolicyAllInfo(registNo);

		for(PrpLCMainVo cMainVo:prpLCMains){
			String riskCode = cMainVo.getRiskCode();
			// ????????????
			if(StringUtils.isNotBlank(comCode)&&"22".equals(comCode.substring(0,2))){// ????????????
				if( !Risk.DQZ.equals(riskCode)&&RequestType.RegistInfoBI_SH.getCode().equals(requestType)){// ????????????
					sendRegistToSHPlatformBI(registVo,cMainVo);
				}
				if(Risk.DQZ.equals(riskCode)&&RequestType.RegistInfoCI_SH.getCode().equals(requestType)){// ????????????
					sendRegistToSHPlatformCI(registVo,cMainVo);
				}
			}
			if(StringUtils.isNotBlank(comCode)&& !"22".equals(comCode.substring(0,2))){// ????????????
				if( !Risk.DQZ.equals(riskCode)&&RequestType.RegistInfoBI.getCode().equals(requestType)){// ????????????
					sendRegistToPlatformBI(registVo,cMainVo);
				}
				if(Risk.DQZ.equals(riskCode)&&RequestType.RegistInfoCI.getCode().equals(requestType)){// ????????????
					sendRegistToPlatformCI(registVo,cMainVo);
				}
			}
		}
	}

	/**
	 * --
	 */
	@Override
	public void sendRegistCancelToPlatform(String registNo,String policyNo) {
		PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
		PrpLCMainVo cMainVo = policyViewService.getPolicyInfo(registNo,policyNo);
		if(cMainVo!=null&&cMainVo.getComCode().startsWith("22")){// ????????????
			if(Risk.DQZ.equals(cMainVo.getRiskCode())){
				sendRegistToSHPlatformCI(registVo,cMainVo);
			}else{
				sendRegistToSHPlatformBI(registVo,cMainVo);
			}
		}else{// ????????????
			if(Risk.DQZ.equals(cMainVo.getRiskCode())){
				sendRegistToPlatformCI(registVo,cMainVo);
			}else{
				sendRegistToPlatformBI(registVo,cMainVo);
			}
		}
	}

	/**
	 * ????????????????????????
	 * @param prpLRegistVo
	 * @param prpLCMainVo
	 */
	private String sendRegistToPlatformCI(PrpLRegistVo prpLRegistVo,PrpLCMainVo prpLCMainVo) {
		// ????????? ??????????????????????????????
		if(CodeConstants.GBFlag.MINORCOMMON.equals(prpLRegistVo.getIsGBFlag())||CodeConstants.GBFlag.MINORRELATION.equals(prpLRegistVo.getIsGBFlag())){
			return "";
		}
		PlatformController controller = PlatformFactory.getInstance(prpLCMainVo.getComCode(),RequestType.RegistInfoCI);
		PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.REGISTTOPALTFORMFLAG,prpLCMainVo.getComCode());
		// ??????body
		CiRegistBodyReqVo ciRegistBodyReqVo = new CiRegistBodyReqVo();
		CiRegistBasePartReqVo ciRegistBasePartReqVo = new CiRegistBasePartReqVo();
		List<CiRegistThirdVehicleDataReqVo> ciRegistThirdVehicleDataReqs = new ArrayList<CiRegistThirdVehicleDataReqVo>();
		//TODO xiaofei   ???????????????????????????--??????--??????
		 List<CiRegistLossDataReqVo> ciRegistLossDataReqs = new ArrayList<CiRegistLossDataReqVo>();

		SysCodeDictVo sysVo = codeTranService.findTransCodeDictVo("DamageCode",prpLRegistVo.getDamageCode());
		if(sysVo==null){
			sysVo = codeTranService.findTransCodeDictVo("DamageCode2",prpLRegistVo.getDamageCode());
		}
		ciRegistBasePartReqVo.setAccidentCause(sysVo.getProperty1());// ????????????
		String dangerRemark = prpLRegistVo.getPrpLRegistExt().getDangerRemark();
		if(StringUtils.isNotBlank(dangerRemark) && dangerRemark.length() > 1000){
			dangerRemark = dangerRemark.substring(0,500);
		}
		ciRegistBasePartReqVo.setAccidentDescription(prpLRegistVo.getPrpLRegistExt().getDangerRemark());
		ciRegistBasePartReqVo.setAccidentLiability(
				getPlatDamageReason("IndemnityDuty",prpLRegistVo.getPrpLRegistExt().getObliGation()));
		String address = prpLRegistVo.getDamageAddress();
		if(StringUtils.isEmpty(address)){
			address = prpLRegistVo.getPrpLRegistExt().getCheckAddress();
		}
		address = decodeAddress(address);
		if(address.length() > 80){
			address = address.substring(0,80);
		}
		ciRegistBasePartReqVo.setAccidentPlace(address);
		ciRegistBasePartReqVo.setAccidentTime(DateUtils.dateToStr(prpLRegistVo.getDamageTime(),DateUtils.YMDHM));
		ciRegistBasePartReqVo.setClaimCode("");// TODO ????????????
		ciRegistBasePartReqVo.setConfirmSequenceNo(prpLCMainVo.getValidNo());
		ciRegistBasePartReqVo.setDriverLicenseNo(prpLRegistVo.getDrivingNo());
		ciRegistBasePartReqVo.setDriverName(prpLRegistVo.getDriverName());
		ciRegistBasePartReqVo.setManageType(prpLRegistVo.getPrpLRegistExt().getManageType());
		ciRegistBasePartReqVo.setPayselfFlag(prpLRegistVo.getPrpLRegistExt().getIsClaimSelf());
		ciRegistBasePartReqVo.setPolicyNo(prpLCMainVo.getPolicyNo());
		ciRegistBasePartReqVo.setReporterName(prpLRegistVo.getReportorName());
		ciRegistBasePartReqVo.setReportNo(prpLRegistVo.getRegistNo());
		ciRegistBasePartReqVo.setReportTime(DateUtils.dateToStr(prpLRegistVo.getReportTime(),DateUtils.YMDHM));
		if("1".equals(configValueVo.getConfigValue())){
			if(StringUtils.isNotBlank(prpLRegistVo.getDamageMapCode())){
				// ??????????????????????????????????????????6???????????????????????????0
				String[] array = prpLRegistVo.getDamageMapCode().split(",");
				String coordinate = "";
				for(String str:array){
					if(str.split("\\.").length==1){
						str=str+".0";
					}
					while(str.split("\\.")[1].length() < 6){
						str = str + "0";
					}
					coordinate = coordinate + str + ",";
				}
				ciRegistBasePartReqVo.setCoordinate(coordinate.substring(0, coordinate.length()-1));
			}else{
				ciRegistBasePartReqVo.setCoordinate("-1,-1");
			}
			ciRegistBasePartReqVo.setCoordinateSystem("02");// ???????????????
		}

		String licenseNo = "";
		String vehicleType = "99";
		for(PrpLRegistCarLossVo prpLRegistCarLossVo:prpLRegistVo.getPrpLRegistCarLosses()){
			if("1".equals(prpLRegistCarLossVo.getLossparty())){// ?????????
				licenseNo = prpLRegistCarLossVo.getLicenseNo();
				vehicleType = prpLRegistCarLossVo.getLicenseType();
				/*if(StringUtils.isBlank(vehicleType)||"99".equals(vehicleType)){
					vehicleType = "25";
				}*/
				if("82".equals(vehicleType)){// ??????????????????????????????????????????
					vehicleType = "32";
				}
				if("81".equals(vehicleType)){// ??????????????????????????????????????????
					vehicleType = "31";
				}
				SysCodeDictVo dictVo = codeTranService.findTransCodeDictVo("LicenseKindCode",vehicleType);
				if(StringUtils.isBlank(vehicleType)||dictVo == null){
					// ?????????????????????????????????????????????????????????????????????????????????????????????
					vehicleType = "25";
				}
			}else{// ?????????
				CiRegistThirdVehicleDataReqVo ciRegistThirdVehicleDataReqVo = new CiRegistThirdVehicleDataReqVo();
				String thLicenseNo = prpLRegistCarLossVo.getLicenseNo();
				if(StringUtils.isNotBlank(thLicenseNo)){// ????????????????????????????????????????????????????????????????????????
					ciRegistThirdVehicleDataReqVo.setCarMark(toTrimLicno(thLicenseNo));
					ciRegistThirdVehicleDataReqVo.setVehicleType("02");// TODO
				}
				ciRegistThirdVehicleDataReqVo.setDriverName(prpLRegistCarLossVo.getThriddrivername());
				ciRegistThirdVehicleDataReqs.add(ciRegistThirdVehicleDataReqVo);
			}
		}
		// ??????????????????????????????????????????????????????????????????????????????
		if(StringUtils.isNotBlank(vehicleType)&&StringUtils.isNotBlank(licenseNo)){
			ciRegistBasePartReqVo.setVehicleType(vehicleType);
			ciRegistBasePartReqVo.setCarMark(toTrimLicno(licenseNo));
		}

		ciRegistBodyReqVo.setCiRegistBasePartReqVo(ciRegistBasePartReqVo);

		List<CiRrportPhoneDataVo> reportPhoneList = new ArrayList<CiRrportPhoneDataVo>();
		CiRrportPhoneDataVo reportPhoneVo = new CiRrportPhoneDataVo();
		reportPhoneVo.setReportPhoneNo(prpLRegistVo.getReportorPhone());
		reportPhoneList.add(reportPhoneVo);
		ciRegistBodyReqVo.setReportPhoneList(reportPhoneList);
		
		//???????????????????????????
		PrpLRegistExtVo registExtVo = prpLRegistVo.getPrpLRegistExt();
//		PrpLRegistVo registVo = registQueryService.findByRegistNo(prpLRegistVo.getRegistNo());
		String obligation = registExtVo.getObliGation();
		List<PrpLRegistPersonLossVo> personLossList = prpLRegistVo.getPrpLRegistPersonLosses();
		List<PrpLRegistPropLossVo> propLossList = prpLRegistVo.getPrpLRegistPropLosses();
		CiRegistLossDataReqVo lossDataReqVo = new CiRegistLossDataReqVo();
		boolean dieCountFlag = false;   //???????????????
		boolean injureCountFlag = false;  //???????????????
		boolean propLossFlag = false;    //???????????????
		if(personLossList != null && personLossList.size() > 0){
			for(PrpLRegistPersonLossVo personLoss : personLossList){
				if(personLoss.getDeathcount() != 0){
					dieCountFlag = true;
				}
				if(personLoss.getInjuredcount() != 0){
					injureCountFlag = true;
				}
			}
		}
		if(propLossList != null && propLossList.size() > 0){
			propLossFlag = true;
		}
		if(StringUtils.isNotBlank(obligation) && !"4".equals(obligation)){  // ??????
			if(dieCountFlag){
				lossDataReqVo.setClaimFeeType("1");
				ciRegistLossDataReqs.add(lossDataReqVo);
			}else{
				if(injureCountFlag){
					lossDataReqVo.setClaimFeeType("2");
					ciRegistLossDataReqs.add(lossDataReqVo);
				}
			}
			
			if(propLossFlag){
				CiRegistLossDataReqVo lossDataReqVos = new CiRegistLossDataReqVo();
				lossDataReqVos.setClaimFeeType("3");
				ciRegistLossDataReqs.add(lossDataReqVos);
			}
		}else{  //??????
			if(dieCountFlag){
				lossDataReqVo.setClaimFeeType("5");
				ciRegistLossDataReqs.add(lossDataReqVo);
			}else{
				if(injureCountFlag){
					lossDataReqVo.setClaimFeeType("6");
					ciRegistLossDataReqs.add(lossDataReqVo);
				}
			}
			
			if(propLossFlag){
				CiRegistLossDataReqVo lossDataReqVos = new CiRegistLossDataReqVo();
				lossDataReqVos.setClaimFeeType("7");
				ciRegistLossDataReqs.add(lossDataReqVos);
			}
		}
		ciRegistBodyReqVo.setCiRegistLossDataReqs(ciRegistLossDataReqs);

		ciRegistBodyReqVo.setCiRegistThirdVehicleDataReqs(ciRegistThirdVehicleDataReqs);
		String claimSeqNo = null;//????????????
		if(SendPlatformUtil.isMor(prpLCMainVo)){
		    CiClaimPlatformLogVo logVo = controller.callPlatform(ciRegistBodyReqVo);
		    logger.info("----------???????????????id"+logVo.getId()+"?????????????????????="+logVo.getBussNo()+"--------------------------------------");
			CiResponseHeadVo resHeadVo = controller.getHeadVo(CiResponseHeadVo.class);
	        if( !"0000".equals(resHeadVo.getErrorCode()) && !ErrorCode.CIRegistRepeat.equals(resHeadVo.getErrorCode())){
				logger.info("?????????????????????????????????");
	            // String msg = resHeadVo.getErrorDesc();
				// throw new BusinessException("????????????",msg);
	            return "";
	        } else{
	        	
	        	if(ErrorCode.CIRegistRepeat.equals(resHeadVo.getErrorCode())){
	        		String indexValue = "?????????????????????";
	        		String lastValue = "???";
	        		String errMsg = logVo.getErrorMessage();
	        		if(StringUtils.isNotBlank(errMsg)){
	        			int firstIndex = errMsg.indexOf(indexValue) + indexValue.length();
	        			int lastIndex = errMsg.lastIndexOf(lastValue);
	        			claimSeqNo = errMsg.substring(firstIndex,lastIndex);
	        			logVo.setClaimSeqNo(claimSeqNo);
	        			logVo.setStatus(JobStatus.SUCCEED);
	        			ciClaimPlatformLogService.platformLogUpdate(logVo);
	        		}
	        	} else{
	        		claimSeqNo = logVo.getClaimSeqNo();
	        	}
				// ??????cMainVo???????????????
	        	logger.info("----------main??????????????????????????????--------------------------------------claimSeqNo="+claimSeqNo);
	            prpLCMainVo.setClaimSequenceNo(claimSeqNo);
	            prpLCMainService.updatePrpLCMain(prpLCMainVo);
	            logger.info("----------main??????????????????????????????--------------------------------------claimSeqNo="+claimSeqNo);
	        }
		}
		
		// CiRegistBodyResVo ciRegistBodyResVo =
		// controller.getBodyVo(CiRegistBodyResVo.class);
		// this.saveResposeInfo(ciRegistBodyResVo.getCiRegistBasePartResVo().getClaimCode(),
		// prpLRegistVo, prpLCMainVo);
        return claimSeqNo;
	}

	/**
	 * ????????????????????????
	 * @param prpLRegistVo
	 * @param prpLCMainVo
	 */
	private String sendRegistToPlatformBI(PrpLRegistVo prpLRegistVo,PrpLCMainVo prpLCMainVo) {
		// ????????? ??????????????????????????????
		if(CodeConstants.GBFlag.MINORCOMMON.equals(prpLRegistVo.getIsGBFlag())||CodeConstants.GBFlag.MINORRELATION.equals(prpLRegistVo.getIsGBFlag())){
			return "";
		}
		PlatformController controller = PlatformFactory.getInstance(prpLCMainVo.getComCode(),RequestType.RegistInfoBI);
		PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.REGISTTOPALTFORMFLAG,prpLCMainVo.getComCode());
		BiRegistBodyReqVo biRegistBodyReqVo = new BiRegistBodyReqVo();
		BiRegistBasePartReqVo biRegistBasePartReqVo = new BiRegistBasePartReqVo();
		List<BiRegistThirdVehicleDataReqVo> biRegistThirdVehicleDataReqs = new ArrayList<BiRegistThirdVehicleDataReqVo>();
		//TODO xiaofei   ???????????????????????????--??????--??????
		 List<BiRegistLossDataReqVo> biRegistLossDataReqs = new ArrayList<BiRegistLossDataReqVo>();
		// List<BiSubrogationDataReqVo> biSubrogationDataReqs = new ArrayList<BiSubrogationDataReqVo>();
		List<BiReportPhoneNoDataReqVo> biReportPhoneNoDataReqVoList = new ArrayList<BiReportPhoneNoDataReqVo>();

		SysCodeDictVo sysVo = codeTranService.findTransCodeDictVo("DamageCode",prpLRegistVo.getDamageCode());
		if(sysVo==null){
			sysVo = codeTranService.findTransCodeDictVo("DamageCode2",prpLRegistVo.getDamageCode());
		}
		biRegistBasePartReqVo.setAccidentCause(sysVo.getProperty1());// ????????????
		String remark = prpLRegistVo.getPrpLRegistExt().getDangerRemark();
		if(StringUtils.isNotBlank(remark) && remark.length() > 500){
			remark = remark.substring(0,500);
		}
		biRegistBasePartReqVo.setAccidentDescription(remark);
		biRegistBasePartReqVo.setAccidentLiability(getPlatDamageReason("IndemnityDuty",prpLRegistVo.getPrpLRegistExt()
				.getObliGation()));
		String address = prpLRegistVo.getDamageAddress();
		if(StringUtils.isEmpty(address)){
			address = prpLRegistVo.getPrpLRegistExt().getCheckAddress();
		}
		address = decodeAddress(address);
		if(address.length() > 80){
			address = address.substring(0,80);
		}
		biRegistBasePartReqVo.setAccidentPlace(address);
		biRegistBasePartReqVo.setAccidentTime(DateUtils.dateToStr(prpLRegistVo.getDamageTime(),DateUtils.YMDHM));
		biRegistBasePartReqVo.setClaimCode("");// TODO ????????????
		biRegistBasePartReqVo.setConfirmSequenceNo(prpLCMainVo.getValidNo());// TODO ???????????????
		biRegistBasePartReqVo.setDriverLicenseNo(prpLRegistVo.getDrivingNo());
		biRegistBasePartReqVo.setDriverName(prpLRegistVo.getDriverName());
		biRegistBasePartReqVo.setManageType(prpLRegistVo.getPrpLRegistExt().getManageType());
		biRegistBasePartReqVo.setPayselfFlag(prpLRegistVo.getPrpLRegistExt().getIsClaimSelf());
		biRegistBasePartReqVo.setPolicyNo(prpLCMainVo.getPolicyNo());
		biRegistBasePartReqVo.setReporterName(prpLRegistVo.getReportorName());
		biRegistBasePartReqVo.setReportNo(prpLRegistVo.getRegistNo());
		biRegistBasePartReqVo.setReportTime(DateUtils.dateToStr(prpLRegistVo.getReportTime(),DateUtils.YMDHM));
		if("1".equals(configValueVo.getConfigValue())){
			if(StringUtils.isNotBlank(prpLRegistVo.getDamageMapCode())){
				// ??????????????????????????????????????????6???????????????????????????0
				String[] array = prpLRegistVo.getDamageMapCode().split(",");
				String coordinate = "";
				for(String str:array){
					if(str.split("\\.").length == 1){
						str=str+".0";
					}
					while(str.split("\\.")[1].length() < 6){
						str = str + "0";
					}
					coordinate = coordinate + str + ",";
				}
				biRegistBasePartReqVo.setCoordinate(coordinate.substring(0, coordinate.length()-1));
			}else{
				biRegistBasePartReqVo.setCoordinate("-1,-1");
			}
			biRegistBasePartReqVo.setCoordinateSystem("02");// ???????????????
		}

		String licenseNo = "";
		String vehicleType = "";
		for(PrpLRegistCarLossVo prpLRegistCarLossVo:prpLRegistVo.getPrpLRegistCarLosses()){
			if("1".equals(prpLRegistCarLossVo.getLossparty())){// ?????????
				licenseNo = prpLRegistCarLossVo.getLicenseNo();
				vehicleType = prpLRegistCarLossVo.getLicenseType();
				if("25".equals(vehicleType)){
					vehicleType = "99";
				}else{
					if("82".equals(vehicleType)){// ??????????????????????????????????????????
						vehicleType = "32";
					}
					if("81".equals(vehicleType)){// ??????????????????????????????????????????
						vehicleType = "31";
					}
					SysCodeDictVo dictVo = codeTranService.findTransCodeDictVo("LicenseKindCode",vehicleType);
					if(StringUtils.isBlank(vehicleType)||dictVo == null){
						// ?????????????????????????????????????????????????????????????????????????????????????????????
						vehicleType = "99";
					}
				}
			}else{// ?????????
				BiRegistThirdVehicleDataReqVo biRegistThirdVehicleDataReqVo = new BiRegistThirdVehicleDataReqVo();
				String thLicenseNo = prpLRegistCarLossVo.getLicenseNo();
				if(StringUtils.isNotBlank(thLicenseNo)){// ????????????????????????????????????????????????????????????????????????
					biRegistThirdVehicleDataReqVo.setCarMark(toTrimLicno(thLicenseNo));
					biRegistThirdVehicleDataReqVo.setVehicleType("02");
				}
				biRegistThirdVehicleDataReqVo.setDriverName(prpLRegistCarLossVo.getThriddrivername());
				biRegistThirdVehicleDataReqs.add(biRegistThirdVehicleDataReqVo);
			}
		}
		//???????????????????????????
		PrpLRegistExtVo registExtVo = prpLRegistVo.getPrpLRegistExt();
		String obligation = registExtVo.getObliGation();
		List<PrpLRegistPersonLossVo> personLossList = prpLRegistVo.getPrpLRegistPersonLosses();
		List<PrpLRegistPropLossVo> propLossList = prpLRegistVo.getPrpLRegistPropLosses();
		BiRegistLossDataReqVo biRegistLossDataVo = new BiRegistLossDataReqVo();
		boolean dieCountFlag = false;   //???????????????
		boolean injureCountFlag = false;  //???????????????
		boolean propLossFlag = false;    //???????????????
		if(personLossList != null && personLossList.size() > 0){
			for(PrpLRegistPersonLossVo personLoss : personLossList){
				if(personLoss.getDeathcount() != 0){
					dieCountFlag = true;
				}
				if(personLoss.getInjuredcount() != 0){
					injureCountFlag = true;
				}
			}
		}
		if(propLossList != null && propLossList.size() > 0){
			propLossFlag = true;
		}
//		if(StringUtils.isNotBlank(obligation) && !"4".equals(obligation)){  // ??????
		if(dieCountFlag){
			biRegistLossDataVo.setClaimFeeType("5");
			biRegistLossDataReqs.add(biRegistLossDataVo);
		}else{
			if(injureCountFlag){
				biRegistLossDataVo.setClaimFeeType("6");
				biRegistLossDataReqs.add(biRegistLossDataVo);
			}
		}
		
		if(propLossFlag){
			BiRegistLossDataReqVo biRegistLossDataVoa = new BiRegistLossDataReqVo();
			biRegistLossDataVoa.setClaimFeeType("2");
			biRegistLossDataReqs.add(biRegistLossDataVoa);
		}
		
		//????????????
		BiRegistLossDataReqVo biRegistLossDataVoss = new BiRegistLossDataReqVo();
		biRegistLossDataVoss.setClaimFeeType("1");
		biRegistLossDataReqs.add(biRegistLossDataVoss);

		
		
//		}else{  //??????
//			if(dieCountFlag){
//				biRegistLossDataVo.setClaimFeeType("5");
//				biRegistLossDataReqs.add(biRegistLossDataVo);
//
//			}else{
//				if(injureCountFlag){
//					biRegistLossDataVo.setClaimFeeType("6");
//					biRegistLossDataReqs.add(biRegistLossDataVo);
//				}
//			}
//			
//			if(propLossFlag){
//				biRegistLossDataVo.setClaimFeeType("7");
//				biRegistLossDataReqs.add(biRegistLossDataVo);
//			}
//		}
		biRegistBodyReqVo.setBiRegistLossDataReqs(biRegistLossDataReqs);

		String reportPhoneNo = prpLRegistVo.getReportorPhone();
		BiReportPhoneNoDataReqVo biReportPhoneNoDataReqVo = new BiReportPhoneNoDataReqVo();
		biReportPhoneNoDataReqVo.setReportPhoneNo(reportPhoneNo);
		biReportPhoneNoDataReqVoList.add(biReportPhoneNoDataReqVo);

		// ????????????????????????????????????????????????????????????????????????????????????
		if(StringUtils.isNotBlank(vehicleType)&&StringUtils.isNotBlank(licenseNo)){
			biRegistBasePartReqVo.setVehicleType(vehicleType);
			biRegistBasePartReqVo.setCarMark(toTrimLicno(licenseNo));
		}

		biRegistBodyReqVo.setBiRegistBasePartReqVo(biRegistBasePartReqVo);
		biRegistBodyReqVo.setBiRegistThirdVehicleDataReqs(biRegistThirdVehicleDataReqs);
		biRegistBodyReqVo.setBiReportPhoneNoDataReqVo(biReportPhoneNoDataReqVoList);
		String claimSeqNo = null;//????????????
        if(SendPlatformUtil.isMor(prpLCMainVo)){
            CiClaimPlatformLogVo logVo = controller.callPlatform(biRegistBodyReqVo);
            String errorCode = logVo.getErrorCode();
            if(StringUtils.isNotBlank(errorCode)&& !"0000".equals(errorCode) && !ErrorCode.BIRegistRepeat.equals(logVo.getErrorCode())){
				logger.info("?????????????????????????????????");
            }else{
            	
	        	if(ErrorCode.BIRegistRepeat.equals(logVo.getErrorCode())){
	        		String indexValue = "?????????????????????";
	        		String lastValue = "???";
	        		String errMsg = logVo.getErrorMessage();
	        		if(StringUtils.isNotBlank(errMsg)){
	        			int firstIndex = errMsg.indexOf(indexValue) + indexValue.length();
	        			int lastIndex = errMsg.lastIndexOf(lastValue);
	        			claimSeqNo = errMsg.substring(firstIndex,lastIndex);
	        			logVo.setClaimSeqNo(claimSeqNo);
	        			logVo.setStatus(JobStatus.SUCCEED);
	        			ciClaimPlatformLogService.platformLogUpdate(logVo);
	        		}
	        	} else{
	        		claimSeqNo = logVo.getClaimSeqNo();
	        	}
				// ??????cMainVo???????????????
                prpLCMainVo.setClaimSequenceNo(logVo.getClaimSeqNo());
                prpLCMainService.updatePrpLCMain(prpLCMainVo);
            }
        }
	
		// BiRegistBodyResVo biRegistBodyResVo = controller.getBodyVo(BiRegistBodyResVo.class);
        return claimSeqNo;
	}

	/**
	 * @param damageCode
	 * @return
	 */
	private String getPlatDamageReason(String codeType,String damageCode) {
		return CodeTranUtil.findTransCodeDictVo(codeType,damageCode).getProperty1();
	}

	
	// --------------------------------????????????-----------------------------------//
	
	/**
	 * ?????? -- ?????????????????????
	 * @param registNo
	 * @throws Exception
	 */
	private void sendRegistToSHPlatformCI(PrpLRegistVo registVo,PrpLCMainVo cMainVo){
		// ????????? ??????????????????????????????
		if(CodeConstants.GBFlag.MINORCOMMON.equals(registVo.getIsGBFlag())||CodeConstants.GBFlag.MINORRELATION.equals(registVo.getIsGBFlag())){
			return;
		}
		PlatformController controller = PlatformFactory.getInstance(cMainVo.getComCode(), RequestType.RegistInfoCI_SH);
		SHCIRegistReqBodyVo bodyVo = this.setBodyCI_SH(registVo, cMainVo);
		// ????????????
		CiClaimPlatformLogVo logVo = controller.callPlatform(bodyVo);
		//????????????????????????????????????
		if(logVo != null && StringUtils.isNotBlank(logVo.getErrorMessage()) 
				&& "??????(??????????????????????????????)".equals(logVo.getErrorMessage())){
			logVo.setStatus(JobStatus.SECEND);
			ciClaimPlatformLogService.platformLogUpdate(logVo);
		}
		// ??????cMainVo???????????????
		if(StringUtils.isNotBlank(logVo.getClaimSeqNo())){
		      cMainVo.setClaimSequenceNo(logVo.getClaimSeqNo());
		      prpLCMainService.updatePrpLCMain(cMainVo);
		}
	}

	/**
	 * ?????? -- ?????????????????????
	 * @param registNo
	 * @throws Exception
	 */
	private void sendRegistToSHPlatformBI(PrpLRegistVo registVo,PrpLCMainVo cMainVo) {
		// ????????? ??????????????????????????????
		if(CodeConstants.GBFlag.MINORCOMMON.equals(registVo.getIsGBFlag())||CodeConstants.GBFlag.MINORRELATION.equals(registVo.getIsGBFlag())){
			return;
		}
		PlatformController controller = PlatformFactory.getInstance(cMainVo.getComCode(), RequestType.RegistInfoBI_SH);
		SHBIRegistReqBodyVo bodyVo = this.setBodyBI_SH(registVo, cMainVo);
		// ????????????
		CiClaimPlatformLogVo logVo= controller.callPlatform(bodyVo);
		//????????????????????????????????????
		if(logVo != null && StringUtils.isNotBlank(logVo.getErrorMessage()) && "??????(??????????????????????????????)".equals(logVo.getErrorMessage())){
			logVo.setStatus(JobStatus.SUCCEED);
			ciClaimPlatformLogService.platformLogUpdate(logVo);
		}
		// ??????cMainVo???????????????
		if(StringUtils.isNotBlank(logVo.getClaimSeqNo())){
		    cMainVo.setClaimSequenceNo(logVo.getClaimSeqNo());
	        prpLCMainService.updatePrpLCMain(cMainVo);
		}
	}

	/**
	 * ????????????????????????
	 */
	private SHCIRegistReqBodyVo setBodyCI_SH(PrpLRegistVo registVo,PrpLCMainVo cMainVo) {
		logger.info("????????????"+registVo.getRegistNo()+" ??????????????????????????????????????????");
		PrpLRegistExtVo registExtVo = registVo.getPrpLRegistExt();
		PrpLCItemCarVo cItemCarVo = cMainVo.getPrpCItemCars().get(0);
		PrpLRegistCarLossVo carLossVo = null;
		for(PrpLRegistCarLossVo registCarLossVo:registVo.getPrpLRegistCarLosses()){
			if(CodeConstants.LossParty.TARGET.equals(registCarLossVo.getLossparty())){
				carLossVo = registCarLossVo;
			}
		}
		SHCIRegistReqBodyVo bodyVo = new SHCIRegistReqBodyVo();

		SHCIRegistReqBasePartVo basePartVo = new SHCIRegistReqBasePartVo();
		basePartVo.setConfirmSequenceNo(cMainVo.getValidNo());// ???????????????
		basePartVo.setClaimCode("");// ????????????
		basePartVo.setPolicyNo(registVo.getPolicyNo());
		basePartVo.setReportNo(registVo.getRegistNo());
		basePartVo.setSpecialReportNo("");
		basePartVo.setReportTime(registVo.getReportTime());
		basePartVo.setAccidentTime(registVo.getDamageTime());
		basePartVo.setCarMark(carLossVo!=null ? toTrimLicno(carLossVo.getLicenseNo()) : toTrimLicno(cItemCarVo.getLicenseNo()));
		String vehicleType = StringUtils.isEmpty(cItemCarVo.getLicenseKindCode())?"02":cItemCarVo.getLicenseKindCode();
		basePartVo.setVehicleType(vehicleType);
		SysCodeDictVo sysCodeDictVo = CodeTranUtil.findTransCodeDictVo("AccidentManageType",registExtVo.getManageType());
		basePartVo.setManageType("1".equals(registExtVo.getIsClaimSelf()) ? "5" : sysCodeDictVo.getProperty2());// ????????????????????????
		basePartVo.setReporter(registVo.getReportorName());
		basePartVo.setTel(registVo.getReportorPhone());
		basePartVo.setWeather("");
		basePartVo.setOwner(cItemCarVo.getCarOwner());
		basePartVo.setDriverName(registVo.getDriverName());
		basePartVo.setCertiType("01");
		basePartVo.setCertiCode(registVo.getDriverIdfNo());
		basePartVo.setLicenseNo("");
		basePartVo.setLicenseEffecturalDate(null);

		// ???????????????????????????
		String standardAddress = HttpUtils.getSHRoadInfo(registVo.getDamageAreaCode(), registVo.getDamageAddress(), registVo.getRegistNo());
		if (StringUtils.isBlank(standardAddress)) {
			basePartVo.setAccidentPlace(getStandardAddress(registVo.getDamageAreaCode(), registVo.getDamageAddress()));
		} else {
			basePartVo.setAccidentPlace(standardAddress);
		}
		// ????????????????????????  ?????????????????????????????????????????????
		String accidentPlaceMark = HttpUtils.getSHAccidentPlaceMark(registVo.getDamageAreaCode(), registVo.getDamageAddress(), registVo.getRegistNo());
		basePartVo.setAccidentPlaceMark(accidentPlaceMark);

		String policyPlace = StringUtils.isEmpty(registVo.getIsoffSite()) ? "1" : registVo.getIsoffSite();
		basePartVo.setPolicyPlace(policyPlace);
		basePartVo.setSubrogationFlag(registExtVo.getIsSubRogation());

		Integer personNum = 0;
		for(PrpLRegistPersonLossVo personLossVo:registVo.getPrpLRegistPersonLosses()){
			personNum += personLossVo.getInjuredcount()+personLossVo.getDeathcount();
		}
		basePartVo.setPersonNum(personNum.toString());
		basePartVo.setReporterid(registVo.getReportorIdfNo());
		
		/*????????? 2017-3-13  ?????? ??????????????????????????? ??????*/
		basePartVo.setAccidentDescription(registVo.getPrpLRegistExt().getDangerRemark());
		basePartVo.setAccidentReason(registVo.getAccidentReason());  
		
		List<SHCIRegistReqObjDataVo> objDataListVo = new ArrayList<SHCIRegistReqObjDataVo>();
		for(PrpLRegistCarLossVo lossVo:registVo.getPrpLRegistCarLosses()){
			if(CodeConstants.LossParty.THIRD.equals(lossVo.getLossparty())){
				SHCIRegistReqObjDataVo objDataVo = new SHCIRegistReqObjDataVo();
				objDataVo.setObjName(toTrimLicno(lossVo.getLicenseNo()));
				String type = StringUtils.isEmpty(lossVo.getLicenseType())?"02":lossVo.getLicenseType();
				objDataVo.setVehicleType(type);
				objDataVo.setObjType("1");
				objDataListVo.add(objDataVo);
			}
		}

		/*************************?????????????????????????????????????????????????????????**************************/
		// ??????????????????????????????
		PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.REGISTTOPALTFORMFLAG, cMainVo.getComCode());
		// ?????????????????????????????????????????????????????????????????????????????????????????????(-1, -1)
		if ("1".equals(configValueVo.getConfigValue())) {
			// ?????????
			basePartVo.setCoordinate(HttpUtils.getCoordinate(registVo.getDamageMapCode()));
			// 01-GPS?????????  02-???????????????  03-????????????????????????????????????  04-2000 ?????????????????????
			basePartVo.setCoordinateSystem("02");
		}
		/*************************?????????????????????????????????????????????????????????**************************/

		bodyVo.setRequest_basePartVo(basePartVo);
		bodyVo.setRequest_objDataVo(objDataListVo);
		bodyVo.setRequest_SubrogationDataVo(null);
		bodyVo.setDisputeDataVo(null);
		logger.info("????????????"+registVo.getRegistNo()+" ??????????????????????????????????????????");

		return bodyVo;
	}

	/**
	 * ????????????????????????
	 */
	private SHBIRegistReqBodyVo setBodyBI_SH(PrpLRegistVo registVo,PrpLCMainVo cMainVo) {
		PrpLRegistExtVo registExtVo = registVo.getPrpLRegistExt();
		PrpLCItemCarVo cItemCarVo = cMainVo.getPrpCItemCars().get(0);
		PrpLRegistCarLossVo carLossVo = null;
		for(PrpLRegistCarLossVo registCarLossVo:registVo.getPrpLRegistCarLosses()){
			if(CodeConstants.LossParty.TARGET.equals(registCarLossVo.getLossparty())){
				carLossVo = registCarLossVo;
			}
		}
		SHBIRegistReqBodyVo bodyVo = new SHBIRegistReqBodyVo();

		SHBIRegistReqBasePartVo basePartVo = new SHBIRegistReqBasePartVo();
		basePartVo.setConfirmSequenceNo(cMainVo.getValidNo());// ???????????????
		basePartVo.setClaimCode("");// ????????????
		basePartVo.setReportNo(registVo.getRegistNo());
		basePartVo.setAccidentTime(registVo.getDamageTime());
		basePartVo.setReportTime(registVo.getReportTime());
		basePartVo.setAccidentDescription(registExtVo.getDangerRemark());
		basePartVo.setCarMark(carLossVo!=null ? toTrimLicno(carLossVo.getLicenseNo()) : toTrimLicno(cItemCarVo.getLicenseNo()));
		String licenseKindCode = cItemCarVo.getLicenseKindCode();
		if(StringUtils.isEmpty(licenseKindCode)){
			licenseKindCode = "02";
		}
		basePartVo.setVehicleType(licenseKindCode);
		basePartVo.setPolicyNo(registVo.getPolicyNo());
		basePartVo.setReporter(registVo.getReportorName());
		SysCodeDictVo sysCodeDictVo = CodeTranUtil.findTransCodeDictVo("AccidentManageType",
				registExtVo.getManageType());
		basePartVo.setOptionType("1".equals(registExtVo.getIsClaimSelf()) ? "5" : sysCodeDictVo.getProperty2());// ????????????????????????
		basePartVo.setAccidentReason(registVo.getAccidentReason());// ???????????????????????????--??????
		basePartVo.setTel(registVo.getReportorPhone());
		basePartVo.setWeather("");
		basePartVo.setOwner(cItemCarVo.getCarOwner());
		basePartVo.setDriverName(registVo.getDriverName());
		basePartVo.setCertiType("01");
		basePartVo.setCertiCode(registVo.getDriverIdfNo());
		basePartVo.setLicenseNo("");
		basePartVo.setLicenseEffecturalDate(null);

		// ???????????????????????????
		String standardAddress = HttpUtils.getSHRoadInfo(registVo.getDamageAreaCode(), registVo.getDamageAddress(), registVo.getRegistNo());
		if (StringUtils.isBlank(standardAddress)) {
			basePartVo.setAccidentPlace(getStandardAddress(registVo.getDamageAreaCode(), registVo.getDamageAddress()));
		} else {
			basePartVo.setAccidentPlace(standardAddress);
		}
		// ????????????????????????  ?????????????????????????????????????????????
		String accidentPlaceMark = HttpUtils.getSHAccidentPlaceMark(registVo.getDamageAreaCode(), registVo.getDamageAddress(), registVo.getRegistNo());
		basePartVo.setAccidentPlaceMark(accidentPlaceMark);

		basePartVo.setPolicyPlace(StringUtils.isEmpty(registVo.getIsoffSite()) ? "1" : registVo.getIsoffSite());
		basePartVo.setSubrogationFlag(registExtVo.getIsSubRogation());

		Integer personNum = 0;
		for(PrpLRegistPersonLossVo personLossVo:registVo.getPrpLRegistPersonLosses()){
			personNum += personLossVo.getInjuredcount()+personLossVo.getDeathcount();
		}
		basePartVo.setPersonNum(personNum.toString());
		basePartVo.setReporterid(registVo.getReportorIdfNo());

		/*************************?????????????????????????????????????????????????????????**************************/
		// ??????????????????????????????
		PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.REGISTTOPALTFORMFLAG, cMainVo.getComCode());
		// ?????????????????????????????????????????????????????????????????????????????????????????????(-1, -1)
		if ("1".equals(configValueVo.getConfigValue())) {
			// ?????????
			basePartVo.setCoordinate(HttpUtils.getCoordinate(registVo.getDamageMapCode()));
			// 01-GPS?????????  02-???????????????  03-????????????????????????????????????  04-2000 ?????????????????????
			basePartVo.setCoordinateSystem("02");
		}
		/*************************?????????????????????????????????????????????????????????**************************/

		// ?????????????????????????????????
		List<SHBIRegistReqThirdVehicleDataVo> thirdVehicleDataListVo = new ArrayList<SHBIRegistReqThirdVehicleDataVo>();;
		for(PrpLRegistCarLossVo lossVo:registVo.getPrpLRegistCarLosses()){
			if(CodeConstants.LossParty.THIRD.equals(lossVo.getLossparty())){
				SHBIRegistReqThirdVehicleDataVo thirdVehicleDataVo = new SHBIRegistReqThirdVehicleDataVo();

				thirdVehicleDataVo.setCarMark(toTrimLicno(lossVo.getLicenseNo()));
				String type = StringUtils.isEmpty(lossVo.getLicenseType())?"02":lossVo.getLicenseType();
				thirdVehicleDataVo.setVehicleType(type);
				thirdVehicleDataVo.setVehicleCategory("");
				String InspolicyBI = lossVo.getInspolicybi();
				String company = StringUtils.isEmpty(InspolicyBI) ? "" 
						: CodeTranUtil.findTransCodeDictVo(
						"CIInsurerCompany",InspolicyBI).getProperty2();
				thirdVehicleDataVo.setCompanyCode(company);
				thirdVehicleDataVo.setPolicyNo("");
				thirdVehicleDataVo.setName(lossVo.getThriddrivername());
				thirdVehicleDataVo.setCertiCode("");
				thirdVehicleDataVo.setLicenseNo("");

				thirdVehicleDataListVo.add(thirdVehicleDataVo);
			}
		}

		// ??????????????????????????????
		List<SHBIRegistReqObjDataVo> objDataListVo = new ArrayList<SHBIRegistReqObjDataVo>();
		for(PrpLRegistCarLossVo lossVo:registVo.getPrpLRegistCarLosses()){
			if(CodeConstants.LossParty.TARGET.equals(lossVo.getLossparty())){
				SHBIRegistReqObjDataVo objDataVo = new SHBIRegistReqObjDataVo();
				objDataVo.setObjName(toTrimLicno(lossVo.getLicenseNo()));
				String type = StringUtils.isEmpty(lossVo.getLicenseType())?"02":lossVo.getLicenseType();
				objDataVo.setVehicleType(type);
				objDataVo.setObjType("1");
				objDataVo.setMainThird("1");

				objDataListVo.add(objDataVo);
			}
		}

		bodyVo.setRequest_basePartVo(basePartVo);
		bodyVo.setRequest_thirdVehicleDataVo(thirdVehicleDataListVo);
		bodyVo.setRequest_objDataVo(objDataListVo);
		bodyVo.setRequest_subrogationDataVo(null);
		bodyVo.setRequest_disputeDataVo(null);

		return bodyVo;
	}

	/**
	 * @param claimCode
	 * @param prpLRegistVo
	 * @param prpLCMainVo
	 */
//	private void saveResposeInfo(String claimCode,String registNo,PrpLCMainVo prpLCMainVo) {
//		CiClaimDemandVo ciClaimDemandVo = carPlatHandleService.findCiClaimDemandVoByRegistNo(registNo);
//		if(ciClaimDemandVo==null){
//			ciClaimDemandVo = new CiClaimDemandVo();
//		}
//		ciClaimDemandVo.setClaimCode(claimCode);
//		ciClaimDemandVo.setRegistNo(registNo);
//		ciClaimDemandVo.setValidNo(prpLCMainVo.getValidNo());
	// ciClaimDemandVo.setRegistUploadFlag("1");// ???????????????
//		ciClaimDemandVo.setRiskCode(prpLCMainVo.getRiskCode());
//		ciClaimDemandVo.setComCode(prpLCMainVo.getComCode());
//		carPlatHandleService.saveCiClaimDemandVo(ciClaimDemandVo);
//	}
	
	public String decodeAddress(String address){
		if(StringUtils.isNotBlank(address)){
			String[] array = null;
			if(address.contains("???")){
				array = address.split("???");
				if(array.length > 1){
					address = array[0]+"???-"+array[1];
				}
			}
			if(address.contains("???")){
				array = address.split("???");
				if(array.length > 1){
					address = array[0]+"???-"+array[1];
				}
			}
			if(address.contains("???")){
				array = address.split("???");
				if(array.length > 1){
					address = array[0]+"???-"+array[1];
				}
			}
			if(address.contains("???")){
				array = address.split("???");
				if(array.length > 1){
					address = array[0]+"???-"+array[1];
				}
			}
			if(address.contains("???")){
				array = address.split("???");
				if(array.length > 1){
					address = array[0]+"???-"+array[1];
				}
			}
		}
		
		return address;
	}

	/**
	 * ??????????????????????????????????????????????????? ??????????????????9201???????????? ???????????????
	 * 
	 * <pre></pre>
	 * @param bussArray
	 * @modified: ???LiYi(2018???10???10??? ??????7:21:50): <br>
	 */
	@Override
	public String  sendRegistToPlatform2(String[] bussArray) {
		StringBuffer sb = new StringBuffer();
		Set<String> bussArrSet = new HashSet<String>();
		for(String bussNo :bussArray ){
			bussArrSet.add(bussNo.trim());
		}
		List<String> bussNoArr =new ArrayList<String>();
		List<String> registNos = new ArrayList<String>();
		bussNoArr.addAll(bussArrSet);
		String[] requesttypes = new String[]{"V3101","50"};
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addIn("bussNo",bussNoArr);
		queryRule.addIn("requestType",requesttypes);
		QueryRule queryRule1 = QueryRule.getInstance();
		queryRule1.addIn("registNo",bussNoArr);
		try{
			List<CiClaimPlatformLog> allLog = databaseDao.findAll(CiClaimPlatformLog.class,queryRule);
			List<PrpLRegist> prpLRegists = databaseDao.findAll(PrpLRegist.class,queryRule1);
			for(String bussNo:bussNoArr){
				if(allLog!=null&& !allLog.isEmpty()){
					for(CiClaimPlatformLog ciClaimPlatformLog:allLog){
						// ???????????????????????????????????????
						registNos.add(ciClaimPlatformLog.getBussNo());
						if(bussNo.equals(ciClaimPlatformLog.getBussNo())){
							if("1".equals(ciClaimPlatformLog.getStatus()) || "2".equals(ciClaimPlatformLog.getStatus())){
								sb.append("????????????????????????????????????????????? ???"+ciClaimPlatformLog.getBussNo()+"\n");
							}else {
								CiClaimPlatformLogVo logVo = Beans.copyDepth().from(ciClaimPlatformLog).to(CiClaimPlatformLogVo.class);
								try{
									uploadRegistToPaltform(logVo);
									ciClaimPlatformLogService.platformLogUpdate(logVo.getId());
									sb.append("???????????? ???"+ciClaimPlatformLog.getBussNo()+"\n");
								}catch(Exception e){
									sb.append("???????????? ???"+ciClaimPlatformLog.getBussNo()+"\n");
								}
							}
						}
					}
				}
				if( !registNos.contains(bussNo)){
					// ?????????????????????
					List<PrpLCMainVo> prpLCMainVos = prpLCMainService.findPrpLCMainsByRegistNo(bussNo);
					if(prpLCMainVos!=null&& !prpLCMainVos.isEmpty()){
						for(PrpLCMainVo prpLCMainVo:prpLCMainVos){
							CiClaimPlatformLogVo ciClaimPlatformLogVo = new CiClaimPlatformLogVo();
							if(StringUtils.isNotBlank(prpLCMainVo.getClaimSequenceNo())){
								// ????????????????????????????????????????????????????????? prplcmain ??? claimSequenceNo
								ciClaimPlatformLogVo.setClaimSeqNo(prpLCMainVo.getClaimSequenceNo());
								ciClaimPlatformLogVo.setErrorCode("0000");
								ciClaimPlatformLogVo.setErrorMessage("??????");
								ciClaimPlatformLogVo.setComCode("00000000");
								ciClaimPlatformLogVo.setCreateUser("0000000000");
								ciClaimPlatformLogVo.setCreateTime(new Date());
								if(prpLCMainVo.getRiskCode().startsWith("12")){
									ciClaimPlatformLogVo.setRequestType(RequestType.RegistInfoBI.getCode());
									ciClaimPlatformLogVo.setRequestName(RequestType.RegistInfoBI.getName());
								}else{
									ciClaimPlatformLogVo.setRequestType(RequestType.RegistInfoCI.getCode());
									ciClaimPlatformLogVo.setRequestName(RequestType.RegistInfoCI.getName());
								}
								ciClaimPlatformLogVo.setBussNo(prpLCMainVo.getRegistNo());
								ciClaimPlatformLogVo.setStatus("2");
								ciClaimPlatformLogVo.setRequestTime(new Date());
								ciClaimPlatformLogVo.setResponseTime(new Date());
								CiClaimPlatformLogVo save = ciClaimPlatformLogService.save(ciClaimPlatformLogVo);
								ciClaimPlatformLogService.platformLogUpdate(save.getId());
								sb.append("???????????????"+prpLCMainVo.getRegistNo()+"\n");
							}else{
								// ????????????(RegistTaskFlag=1,0????????????)???????????????????????? prplcmain ?????? claimSequenceNo
								try{
									if(prpLRegists!=null&& !prpLRegists.isEmpty()){
										for(PrpLRegist prpLRegist:prpLRegists){
											if(bussNo.equals(prpLRegist.getRegistNo())){
												if("1".equals(prpLRegist.getRegistTaskFlag())){
													sendRegistToPlatform(prpLCMainVo.getRegistNo());
													sb.append("???????????????"+prpLCMainVo.getRegistNo());
												}else{
													sb.append("?????????????????????????????????????????????"+prpLCMainVo.getRegistNo()+"\n");
												}
											}
										}
									}
								}catch(Exception e){
									sb.append("????????????"+prpLCMainVo.getRegistNo()+"\n");
								}
							}
						}
					}else{
						sb.append("???????????? ???????????????????????????"+bussNo+"\n");
					}
				}
			}
		}catch(Exception e){
			logger.info("??????????????????????????????");
			e.printStackTrace();
		}
		return sb.toString();
	}
	//???????????????????????????
	private  String toTrimLicno(String licenseNo) {
		if(StringUtils.isNotBlank(licenseNo)){
			licenseNo = licenseNo.replace((char)12288,' ').replace(" " ,"");
			return licenseNo;
		}else{
			return licenseNo;
		}
		
		
	}

	private String getStandardAddress(String damageAreaCode, String damageAddress) {

		String[] addrArr = areaDictService.findAreaNameByAreaCode(damageAreaCode, null);
		StringBuilder address = new StringBuilder();
		if (addrArr.length > 0) {
			for (int i = 0; i < 5; i++) {
				if (i != 4) {
					if (i < addrArr.length) {
						address.append(addrArr[i]).append("-");
					} else {
						address.append(damageAddress).append("-");
					}
				} else {
					address.append(damageAddress);
				}
			}
		} else {
			address.append("????????????-????????????-????????????-????????????-????????????");
		}
		return address.toString();
	}
}
