package ins.sino.claimcar.platform.service.spring;

import ins.platform.common.service.facade.CodeTranService;
import ins.platform.common.util.CodeTranUtil;
import ins.platform.vo.SysCodeDictVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.RadioValue;
import ins.sino.claimcar.carplatform.constant.RequestType;
import ins.sino.claimcar.carplatform.controller.PlatformController;
import ins.sino.claimcar.carplatform.service.CiClaimPlatformLogService;
import ins.sino.claimcar.carplatform.util.CodeConvertTool;
import ins.sino.claimcar.carplatform.util.PlatformFactory;
import ins.sino.claimcar.carplatform.vo.CiClaimPlatformLogVo;
import ins.sino.claimcar.certify.service.CertifyPubService;
import ins.sino.claimcar.certify.vo.PrpLCertifyDirectVo;
import ins.sino.claimcar.check.service.CheckHandleService;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.PrpLCheckCarInfoVo;
import ins.sino.claimcar.check.vo.PrpLCheckCarVo;
import ins.sino.claimcar.check.vo.PrpLCheckDriverVo;
import ins.sino.claimcar.check.vo.PrpLCheckDutyVo;
import ins.sino.claimcar.check.vo.PrpLCheckPersonVo;
import ins.sino.claimcar.check.vo.PrpLCheckPropVo;
import ins.sino.claimcar.check.vo.PrpLCheckTaskVo;
import ins.sino.claimcar.check.vo.PrpLCheckVo;
import ins.sino.claimcar.check.vo.history.CarInfoHistoryReqBasePartVo;
import ins.sino.claimcar.check.vo.history.CarInfoHistoryReqVo;
import ins.sino.claimcar.check.vo.history.CarInfoHistoryResBodyVo;
import ins.sino.claimcar.check.vo.history.CarInfoHistoryResHeadVo;
import ins.sino.claimcar.claim.service.ClaimTaskService;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.constant.Risk;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.platform.service.CheckToPlatformService;
import ins.sino.claimcar.platform.util.SendPlatformUtil;
import ins.sino.claimcar.platform.vo.BICheckReqBasePartVo;
import ins.sino.claimcar.platform.vo.BICheckReqBodyVo;
import ins.sino.claimcar.platform.vo.BICheckReqPersonDataVo;
import ins.sino.claimcar.platform.vo.BICheckReqProtectDataVo;
import ins.sino.claimcar.platform.vo.BICheckReqSubrogationDataVo;
import ins.sino.claimcar.platform.vo.BICheckReqVehicleDataVo;
import ins.sino.claimcar.platform.vo.CICheckReqBasePartVo;
import ins.sino.claimcar.platform.vo.CICheckReqBodyVo;
import ins.sino.claimcar.platform.vo.CICheckReqCarDataVo;
import ins.sino.claimcar.platform.vo.CICheckReqCarListVo;
import ins.sino.claimcar.platform.vo.CICheckReqPersonDataVo;
import ins.sino.claimcar.platform.vo.CICheckReqPersonListVo;
import ins.sino.claimcar.platform.vo.CICheckReqProtectDataVo;
import ins.sino.claimcar.platform.vo.CICheckReqProtectListVo;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLCItemCarVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.subrogation.service.SubrogationService;
import ins.sino.claimcar.subrogation.vo.PrpLSubrogationCarVo;
import ins.sino.claimcar.subrogation.vo.PrpLSubrogationMainVo;
import ins.sino.claimcar.subrogation.vo.PrpLSubrogationPersonVo;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Path;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

/**
 * 查勘送平台接口实现
 * 
 * @author Luwei
 * @CreateTime
 */
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"}, timeout = 1000000)
@Path("checkToPlatformService")
public class CheckToPaltformServiceImpl implements CheckToPlatformService {

	@Autowired
	CiClaimPlatformLogService ciClaimPlatformLogService;

	@Autowired
	CheckHandleService checkService;

	@Autowired
	CodeTranService codeTranService;

	@Autowired
	CheckTaskService checkTaskService;

	@Autowired
	SubrogationService subrogationService;

	@Autowired
	PolicyViewService policyViewService;

	@Autowired
	CertifyPubService certifyPubService;
	
	@Autowired
	ClaimTaskService claimTaskService;
	
	@Autowired
	RegistQueryService registQueryService;
	@Autowired
	LossCarService lossCarService;

	private Logger logger = LoggerFactory.getLogger(CheckToPaltformServiceImpl.class);

	@Override
	public void sendToPaltform(CiClaimPlatformLogVo logVo) throws Exception {
		String registNo = logVo.getBussNo();
		String requestType = logVo.getRequestType();
		String comCode = logVo.getComCode();

		// 保单关联与取消的问题，1-报案两张保单，查勘取消了一张，2-报案一张保单，查勘关联了另一张保单
		logger.info("registNo"+registNo);
		if( !"22".equals(comCode.substring(0,2))){// 查勘只有全国平台才上传，上海平台（查勘、定损、核损）
			// 查勘登记 --商业
			if(RequestType.CheckBI.getCode().equals(requestType)){
				sendCheckToPlatformBI(registNo);
			}else{// 查勘登记 --交强
				sendCheckToPlatformCI(registNo);
			}
		}
	}

	/**
	 * 查勘登记 交强送平台
	 * @param registNo
	 * @throws Exception
	 */
	@Override
	public void sendCheckToPlatformCI(String registNo) throws Exception {
		PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
		// 联共保 从共，从联不交互平台
		if(CodeConstants.GBFlag.MINORCOMMON.equals(registVo.getIsGBFlag())||CodeConstants.GBFlag.MINORRELATION.equals(registVo.getIsGBFlag())){
			return;
		}
		if(!SendPlatformUtil.isMor(policyViewService.getPolicyInfoByType(registNo,"1"))){
			return;
		}
		String comCode = policyViewService.findPolicyComCode(registNo,"11");
		CiClaimPlatformLogVo logVo = ciClaimPlatformLogService.findLogByBussNo
				(RequestType.RegistInfoCI.getCode(),registNo,comCode);
		if(logVo==null){
			logger.info("该案件的报案环节送平台不存在成功的记录信息！");
			// throw new IllegalArgumentException("该案件的报案环节送平台不存在成功的记录信息！");
		}
		PlatformController controller = PlatformFactory.getInstance(comCode,RequestType.CheckCI);

		// 组织报文，
		String claimSeqNo = logVo==null ? "" : logVo.getClaimSeqNo();
		CICheckReqBodyVo bodyVo = this.setRequestBodyCI(registNo,claimSeqNo);
		
		PrpLCMainVo cMainVo = policyViewService.getPolicyInfoByType(registNo,"1");
		if(SendPlatformUtil.isMor(cMainVo)){
			CiClaimPlatformLogVo log = controller.callPlatform(bodyVo);
			String errorCode = log!=null ? log.getErrorCode() : "";
			if(StringUtils.isNotEmpty(errorCode)&& !"0000".equals(errorCode)){
				logger.info("交强查勘登记上传平台失败！");
				// throw new IllegalArgumentException("商业-查勘登记上传平台失败！");
			}
		}
		// if (!"0000".equals(resHeadVo.getErrorCode())) {
		// logger.error("交强查勘登记上传平台失败！");
		// // throw new RuntimeException("交强查勘登记上传平台失败！");
		// }
		// CICheckResBodyVo resBodyVo =
		// controller.getBodyVo(CICheckResBodyVo.class);
		// 保存返回的信息
		// this.saveResposeInfoCI(resBodyVo);

	}

	/**
	 * 组织报文
	 * @param registNo ,CiClaimDemandVo
	 * @throws Exception 
	 * @throws ParseException
	 * @modified: ☆Luwei(2016年4月26日 下午3:32:39): <br>
	 */
	private CICheckReqBodyVo setRequestBodyCI(String registNo,String claimSeqNo) throws Exception {
		String validNo = policyViewService.findValidNo(registNo,"11");
		PrpLCheckVo checkVo = checkService.queryPrpLCheckVo(registNo);
		if(checkVo==null){
			logger.info("查勘还未进行处理！");
			return null;
		}
		PrpLCheckTaskVo checkTaskVo = checkVo.getPrpLCheckTask();
		List<PrpLCheckCarVo> checkCarVos = checkTaskVo.getPrpLCheckCars();
		List<PrpLCheckPropVo> checkPropVos = checkTaskVo.getPrpLCheckProps();
		List<PrpLCheckPersonVo> prpLCheckPersons = checkTaskVo.getPrpLCheckPersons();
		PrpLCheckDutyVo checkDutyVo = checkTaskService.findCheckDuty(registNo,1);

		CICheckReqBodyVo requestBodyVo = new CICheckReqBodyVo();

		// 基本信息vo
		CICheckReqBasePartVo reBasePartVo = new CICheckReqBasePartVo();

		reBasePartVo.setConfirmSequenceNo(validNo);// 投保确认号
		reBasePartVo.setClaimCode(claimSeqNo);// 理赔编号
		reBasePartVo.setReportNo(registNo);// 报案号
		// TODO xiaofei 保险事故分类 -- 缺失 -- 交强
		reBasePartVo.setAccidentType("100");//保险事故分类；(100-交通事故类)
		String duty = checkDutyVo.getIndemnityDuty();
		Long dut = Long.parseLong(duty)+1L;
		reBasePartVo.setAccidentLiability(dut.toString());// 事故责任划分代码；
		reBasePartVo.setManageType(checkVo.getManageType());// 事故处理方式代码；
		String damageCode = checkVo.getDamageCode();
		SysCodeDictVo sysVo = codeTranService.findTransCodeDictVo("DamageCode",damageCode);
		if(sysVo==null){
			sysVo = codeTranService.findTransCodeDictVo("DamageCode2",damageCode);
		}
		reBasePartVo.setAccidentCause(sysVo.getProperty1());// 出险原因代码；
		reBasePartVo.setPaySelfFlag(checkVo.getIsClaimSelf());// 互碰自赔标志；
		reBasePartVo.setIsSingleAccident(checkVo.getSingleAccidentFlag());// 是否单车事故
		reBasePartVo.setIsPersonInjured("0");// 是否包含人伤

		// 车辆查勘情况列表
		CICheckReqCarListVo reCarListVo = new CICheckReqCarListVo();
		List<CICheckReqCarDataVo> carDataVos = new ArrayList<CICheckReqCarDataVo>();
		if(checkCarVos!=null&&checkCarVos.size()>0){
			for(PrpLCheckCarVo checkCarVo:checkCarVos){
				// 三者车无损失不上传平台
//				if(CodeConstants.RadioValue.RADIO_YES.equals(checkCarVo.getLossFlag())&&checkCarVo.getSerialNo()!=1){// 0-有损失，1-无损失
//					continue;
//				}
				PrpLCheckCarInfoVo carInfoVo = checkCarVo.getPrpLCheckCarInfo();
				PrpLCheckDriverVo driverVo = checkCarVo.getPrpLCheckDriver();

				CICheckReqCarDataVo carDataVo = new CICheckReqCarDataVo();
				// carInfoVo.getLicenseNo()
				if(checkCarVo.getSerialNo()==1){//开始可能是未上牌车辆，到了定损节点的时候，也许已上车牌号
					if(StringUtils.isNotBlank(carInfoVo.getLicenseNo())){
						carDataVo.setCarMark(toTrimLicno(carInfoVo.getLicenseNo()));// 损失出险车辆号牌号码	
					}else{
						List<PrpLDlossCarMainVo> lossCarMainVos=lossCarService.findLossCarMainBySerialNo(checkCarVo.getRegistNo(),1);
						if(lossCarMainVos!=null && lossCarMainVos.size()>0){
							carDataVo.setCarMark(toTrimLicno(lossCarMainVos.get(0).getLicenseNo()));// 损失出险车辆号牌号码
						}
					}
					
				}else{
					carDataVo.setCarMark(toTrimLicno(carInfoVo.getLicenseNo()));// 损失出险车辆号牌号码	
				}
				
				// carInfoVo.getLicenseType()
				String licenseType = carInfoVo.getLicenseType();
				if("25".equals(licenseType) || "99".equals(licenseType) || StringUtils.isBlank(licenseType)){
					licenseType = "02";
				}
				if("82".equals(licenseType)){
					licenseType = "32";//针对武警标的车取值的是保单表编码的调整
				}
				if("81".equals(licenseType)) {
					licenseType = "31";
				}
				if("32".equals(licenseType)) {
					licenseType = "07";
				}
				
				if(checkCarVo.getSerialNo()== 1 ){//如果是标的则从承保表中进行转换
					PrpLCItemCarVo ciItemCarVo = registQueryService.findCItemCarByRegistNo(checkCarVo.getRegistNo());
					String platformCarKind = null;
					if("011".equals(ciItemCarVo.getCarType()) || "016".equals(ciItemCarVo.getCarType())){
						platformCarKind = CodeConvertTool.getVehicleCategory(ciItemCarVo.getCarType(),
								ciItemCarVo.getExhaustScale(),ciItemCarVo.getTonCount());
						if(StringUtils.isBlank(platformCarKind)){
							platformCarKind = CodeTranUtil.findTransCodeDictVo("VehicleTypeShow",ciItemCarVo.getCarType()).getProperty1();
						}
						carDataVo.setCarKind(platformCarKind);
					} else{
						carDataVo.setCarKind( CodeTranUtil.findTransCodeDictVo("VehicleTypeShow",ciItemCarVo.getCarType()).getProperty1());
					}

				} else{//如果是三者车则从查勘车辆信息表获取平台车辆种类
					carDataVo.setCarKind(carInfoVo.getPlatformCarKindCode());	
				}
				carDataVo.setVehicleType(licenseType);// 损失出险车辆号牌种类代码
				carDataVo.setRackNo(carInfoVo.getFrameNo());// 损失出险车辆VIN码
				carDataVo.setEngineNo(carInfoVo.getEngineNo());// 损失出险车辆发动机号
				carDataVo.setVehicleModel(carInfoVo.getBrandName());
				carDataVo.setDriverName(driverVo.getDriverName());
				carDataVo.setDriverLicenseNo(driverVo.getDrivingLicenseNo());
				carDataVo.setVehicleProperty(checkCarVo.getSerialNo()==1 ? "1" : "2");
				carDataVo.setFieldType("1".equals(checkTaskVo.getFirstAddressFlag()) ? "2" : "3");// 现场类别
				carDataVo.setCheckerCode(checkCarVo.getUpdateUser());
				String userName = codeTranService.transCode("UserCode",checkCarVo.getUpdateUser());
				carDataVo.setCheckerName(userName);
				carDataVo.setCheckStartTime(checkVo.getCreateTime());// 损失查勘调度开始时间
				carDataVo.setCheckEndTime(checkVo.getUpdateTime());// 损失查勘结束时间
				carDataVo.setCheckAddr(checkCarVo.getCheckAdress());
				carDataVo.setCheckDes(checkTaskVo.getContexts());
				carDataVo.setEstimateAmount(checkCarVo.getLossFee());// 估损金额
				carDataVo.setCheckerCertiCode(checkTaskVo.getCheckerIdfNo());// 查勘人员身份证号码
				carDataVos.add(carDataVo);
			}
			reCarListVo.setCarDataVo(carDataVos);
		}

		// 财产查勘情况列表
		CICheckReqProtectListVo reProtectListVo = null;
		if(checkPropVos!=null&&checkPropVos.size()>0&&CodeConstants.RadioValue.RADIO_YES
				.equals(checkVo.getIsPropLoss())){
			reProtectListVo = new CICheckReqProtectListVo();
			List<CICheckReqProtectDataVo> protectDataVos = new ArrayList<CICheckReqProtectDataVo>();

			for(PrpLCheckPropVo checkPropVo:checkPropVos){
				CICheckReqProtectDataVo protectDataVo = new CICheckReqProtectDataVo();

				String protecName = checkPropVo.getLossItemName();
				if(StringUtils.isEmpty(protecName)){
					protecName = "无";
				}
				protectDataVo.setProtectName(protecName);
				protectDataVo.setLossDesc(checkPropVo.getLossDegreeCode());
				protectDataVo.setLossNum(checkPropVo.getLossNum());
				protectDataVo.setEstimateAmount(checkPropVo.getLossFee());
				protectDataVo.setOwner(checkPropVo.getLossPartyName());
				protectDataVo.setProtectProperty(checkPropVo.getLossPartyId()==1 ? "1" : "2");// 财产属性
				protectDataVo.setFieldType("1".equals(checkTaskVo.getFirstAddressFlag()) ? "2" : "3");// 现场类别
				protectDataVo.setCheckerCode(checkPropVo.getCreateUser());
				String userName = codeTranService.transCode("UserCode",checkPropVo.getUpdateUser());
				protectDataVo.setCheckerName(userName);
				protectDataVo.setCheckStartTime(checkVo.getCreateTime());// 损失查勘调度开始时间
				protectDataVo.setCheckEndTime(checkVo.getUpdateTime());// 损失查勘结束时间
				protectDataVo.setCheckAddr(checkTaskVo.getCheckAddress());
				protectDataVo.setCheckDes(checkTaskVo.getContexts());
				protectDataVo.setCheckerCertiCode(checkTaskVo.getCheckerIdfNo());

				protectDataVos.add(protectDataVo);
			}
			reProtectListVo.setProtectDataVo(protectDataVos);
		}
		// 是否包含财损；
		if(!checkPropVos.isEmpty() && checkPropVos.size()>0 && CodeConstants.RadioValue.RADIO_YES.equals(checkVo.getIsPropLoss())){
			reBasePartVo.setIsProtectLoss("1");
		} else{
			reBasePartVo.setIsProtectLoss("0");
		}
		//TODO xiaofei 	人员查勘情况列表：估损金额 -- 缺失 -- 交强
		//人员损失情况列表
		CICheckReqPersonListVo ciCheckReqPersonList = new CICheckReqPersonListVo();
		List<CICheckReqPersonDataVo> personDataVoList = new ArrayList<CICheckReqPersonDataVo>();
		if(prpLCheckPersons != null && prpLCheckPersons.size() > 0){
			CICheckReqPersonDataVo personData = new CICheckReqPersonDataVo();
			for(PrpLCheckPersonVo checkPerson : prpLCheckPersons){
				personData.setEstimateAmount(checkPerson.getLossFee());
				
				personDataVoList.add(personData);
			}
		}
		ciCheckReqPersonList.setPersonDataVo(personDataVoList);
		
		requestBodyVo.setPersonListVo(ciCheckReqPersonList);
		requestBodyVo.setBasePartVo(reBasePartVo);// 基本信息
		requestBodyVo.setCarListVo(reCarListVo);// 车辆查勘情况列表
		requestBodyVo.setProtectListVo(reProtectListVo);// 财产查勘情况列表
		requestBodyVo.setPersonListVo(null);

		return requestBodyVo;
	}

	/**
	 * 查勘登记 商业送平台
	 * @param registNo
	 * @throws Exception
	 */
	@Override
	public void sendCheckToPlatformBI(String registNo) throws Exception {
		PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
		// 联共保 从共，从联不交互平台
		if(CodeConstants.GBFlag.MINORCOMMON.equals(registVo.getIsGBFlag())||CodeConstants.GBFlag.MINORRELATION.equals(registVo.getIsGBFlag())){
			return;
		}
		if(!SendPlatformUtil.isMor(policyViewService.getPolicyInfoByType(registNo,"2"))){
			return;
		}
		String comCode = policyViewService.findPolicyComCode(registNo,"12");
		CiClaimPlatformLogVo logVo = ciClaimPlatformLogService.findLogByBussNo(RequestType.RegistInfoBI.getCode(),registNo,
				comCode);
		if(logVo==null){
			logger.info("该案件的报案环节送平台不存在信息！");
		}
		PlatformController controller = PlatformFactory.getInstance(comCode,RequestType.CheckBI);

		// 组织报文，
		String claimSeqNo = logVo==null ? "" : logVo.getClaimSeqNo();
		BICheckReqBodyVo bodyVo = this.setRequestBodyBI(registNo,claimSeqNo);
		
		if(SendPlatformUtil.isMor(policyViewService.getPolicyInfoByType(registNo,"2"))){
			CiClaimPlatformLogVo log = controller.callPlatform(bodyVo);
			String errorCode = log!=null ? log.getErrorCode() : "";
			if(StringUtils.isNotEmpty(errorCode)&& !"0000".equals(errorCode)){
				logger.info("商业-查勘登记上传平台失败！");
			}
		}
		// BICheckResBodyVo resBodyVo = controller
		// .getBodyVo(BICheckResBodyVo.class);
		// 保存返回的信息
		// this.saveResposeInfoBI(resBodyVo);

	}

	/**
	 * 组织报文
	 * 
	 * @param registNo ,demandVo
	 * @throws Exception 
	 * @throws ParseException
	 * @modified: ☆Luwei(2016年4月26日 下午3:32:39): <br>
	 */
	private BICheckReqBodyVo setRequestBodyBI(String registNo,String claimSeqNo) throws Exception {
		String validNo = policyViewService.findValidNo(registNo,"12");
		PrpLCheckVo checkVo = checkService.queryPrpLCheckVo(registNo);
		if(checkVo==null){
			logger.info("查勘还未进行处理！");
			return null;
		}
		PrpLCheckTaskVo checkTaskVo = checkVo.getPrpLCheckTask();
		List<PrpLCheckCarVo> checkCarVos = checkTaskVo.getPrpLCheckCars();
		List<PrpLCheckPropVo> checkPropVos = checkTaskVo.getPrpLCheckProps();
		List<PrpLCheckPersonVo> prpLCheckPersons = checkTaskVo.getPrpLCheckPersons();
		PrpLCheckDutyVo checkDutyVo = checkTaskService.findCheckDuty(registNo,1);

		BICheckReqBodyVo requestBodyVo = new BICheckReqBodyVo();

		// 基本信息vo
		BICheckReqBasePartVo reBasePartVo = new BICheckReqBasePartVo();

		reBasePartVo.setClaimSequenceNo(claimSeqNo);// 理赔编号
		reBasePartVo.setClaimNotificationNo(registNo);// 报案号
		reBasePartVo.setConfirmSequenceNo(validNo);// 投保确认号
		// TODO xiaofei 保险事故分类 -- 缺失 -- 商业
		 reBasePartVo.setAccidentType("100");//保险事故分类；(100-交通事故类)
		String duty = checkDutyVo.getIndemnityDuty();
		Long dut = Long.parseLong(duty)+1L;
		reBasePartVo.setAccidentLiability(dut.toString());// 事故责任划分代码；
		reBasePartVo.setOptionType(checkVo.getManageType());// 事故处理方式代码；
		String damageCode = checkVo.getDamageCode();
		SysCodeDictVo sysVo = codeTranService.findTransCodeDictVo("DamageCode",damageCode);
		if(sysVo==null){
			sysVo = codeTranService.findTransCodeDictVo("DamageCode2",damageCode);
		}
		reBasePartVo.setLossCauseCode(sysVo.getProperty1());// 出险原因代码；
		reBasePartVo.setSubrogationFlag(checkVo.getIsSubRogation());
		reBasePartVo.setSubCertiType(getCertiType(registNo,"1"));//
		
		String subClaimFlag = getCertiType(registNo,"2");
		if (RadioValue.RADIO_NO.equals(checkVo.getIsSubRogation())) {
			subClaimFlag = RadioValue.RADIO_NO;
		}
		reBasePartVo.setSubClaimFlag(subClaimFlag);//
		reBasePartVo.setIsSingleAccident(checkVo.getSingleAccidentFlag());// 是否单车事故
		reBasePartVo.setIsPersonInjured("0");// 是否包含人伤

		// 车辆查勘情况列表
		List<BICheckReqVehicleDataVo> reVehicleDataVo = new ArrayList<BICheckReqVehicleDataVo>();
		if(checkCarVos!=null&&checkCarVos.size()>0){
			for(PrpLCheckCarVo checkCarVo:checkCarVos){
				// 三者车无损失不上传平台
//				if(CodeConstants.RadioValue.RADIO_YES.equals(checkCarVo.getLossFlag())&&checkCarVo.getSerialNo()!=1){// 0-有损失，1-无损失
//					continue;
//				}
				PrpLCheckCarInfoVo carInfoVo = checkCarVo.getPrpLCheckCarInfo();
				PrpLCheckDriverVo driverVo = checkCarVo.getPrpLCheckDriver();

				BICheckReqVehicleDataVo vehicleDataVo = new BICheckReqVehicleDataVo();
				if(checkCarVo.getSerialNo()==1){//开始可能是未上牌车辆，到了定损节点的时候，也许已上车牌号
					if(StringUtils.isNotBlank(carInfoVo.getLicenseNo())){
						vehicleDataVo.setLicensePlateNo(toTrimLicno(carInfoVo.getLicenseNo()));// 损失出险车辆号牌号码	
					}else{
						List<PrpLDlossCarMainVo> lossCarMainVos=lossCarService.findLossCarMainBySerialNo(checkCarVo.getRegistNo(),1);
						if(lossCarMainVos!=null && lossCarMainVos.size()>0){
							vehicleDataVo.setLicensePlateNo(toTrimLicno(lossCarMainVos.get(0).getLicenseNo()));// 损失出险车辆号牌号码
						}
					}
					
				}else{
					vehicleDataVo.setLicensePlateNo(toTrimLicno(carInfoVo.getLicenseNo()));// 损失出险车辆号牌号码	
				}
				String licenseType = carInfoVo.getLicenseType();
				if("25".equals(licenseType) || "99".equals(licenseType) || StringUtils.isBlank(licenseType)){
					licenseType = "02";
				}
				if("82".equals(licenseType)){
					licenseType = "32";//针对标的车取值的是保单表编码的调整
				}
				if("81".equals(licenseType)) {
					licenseType = "31";
				}
				if("32".equals(licenseType)) {
					licenseType = "07";
				}
				if(checkCarVo.getSerialNo() == 1 ){//如果是标的则从承保表中进行转换
					PrpLCItemCarVo ciItemCarVo = registQueryService.findCItemCarByRegistNo(checkCarVo.getRegistNo());
					String platformCarKind = null;
					if("011".equals(ciItemCarVo.getCarType()) || "016".equals(ciItemCarVo.getCarType())){
						platformCarKind = CodeConvertTool.getVehicleCategory(ciItemCarVo.getCarType(),
								ciItemCarVo.getExhaustScale(),ciItemCarVo.getTonCount());
						if(StringUtils.isBlank(platformCarKind)){
							platformCarKind = CodeTranUtil.findTransCodeDictVo("VehicleTypeShow",ciItemCarVo.getCarType()).getProperty1();
						}
						vehicleDataVo.setMotorTypeCode(platformCarKind);
					} else{
						vehicleDataVo.setMotorTypeCode(CodeTranUtil.findTransCodeDictVo("VehicleTypeShow",ciItemCarVo.getCarType()).getProperty1());
					}

				} else{//如果是三者车则从查勘车辆信息表获取平台车辆种类
					vehicleDataVo.setMotorTypeCode(carInfoVo.getPlatformCarKindCode());	
				}
				vehicleDataVo.setLicensePlateType(licenseType);// 出险车辆号牌种类代码
				vehicleDataVo.setVIN(carInfoVo.getVinNo());// 损失出险车辆VIN码
				vehicleDataVo.setEngineNo(carInfoVo.getEngineNo());// 损失出险车辆发动机号
				vehicleDataVo.setModel(carInfoVo.getBrandName());
				vehicleDataVo.setDriverName(driverVo.getDriverName());
				vehicleDataVo.setDriverLicenseNo(driverVo.getDrivingLicenseNo());
				vehicleDataVo.setVehicleProperty(checkCarVo.getSerialNo()==1 ? "1" : "2");// 车辆属性
				vehicleDataVo.setFieldType("1".equals(checkTaskVo.getFirstAddressFlag()) ? "2" : "3");// 现场类别
//				BigDecimal car = checkCarVo.getLossFee();
				vehicleDataVo.setEstimatedLossAmount(checkCarVo.getLossFee());// 估损金额
				vehicleDataVo.setCheckerCode(checkCarVo.getUpdateUser());
				String userName = codeTranService.transCode("UserCode",checkCarVo.getUpdateUser());
				vehicleDataVo.setCheckerName(userName);
				vehicleDataVo.setCheckerCertiCode(checkTaskVo.getCheckerIdfNo());// 查勘人员身份证号码

				vehicleDataVo.setCheckStartTime(checkVo.getCreateTime());
				vehicleDataVo.setCheckEndTime(checkVo.getUpdateTime());
				vehicleDataVo.setCheckAddr(checkCarVo.getCheckAdress());
				vehicleDataVo.setCheckDesc(checkTaskVo.getContexts());

				reVehicleDataVo.add(vehicleDataVo);
			}
		}

		// 财产查勘情况列表
		List<BICheckReqProtectDataVo> protectDataVo = null;
		if(checkPropVos!=null&&checkPropVos.size()>0&&CodeConstants.RadioValue.RADIO_YES
				.equals(checkVo.getIsPropLoss())){
			protectDataVo = new ArrayList<BICheckReqProtectDataVo>();
			for(PrpLCheckPropVo checkPropVo:checkPropVos){

				BICheckReqProtectDataVo protectData = new BICheckReqProtectDataVo();
				String protecName = checkPropVo.getLossItemName();
				if(StringUtils.isEmpty(protecName)){
					protecName = "无";
				}
				protectData.setProtectName(protecName);
				protectData.setLossDesc(checkPropVo.getLossDegreeCode());
				protectData.setLossNum(checkPropVo.getLossNum());
				protectData.setEstimatedLossAmount(checkPropVo.getLossFee());
				protectData.setOwner(checkPropVo.getLossPartyName());
				protectData.setProtectProperty(checkPropVo.getLossPartyId()==1 ? "1" : "2");// 车辆属性
				protectData.setFieldType("1".equals(checkTaskVo.getFirstAddressFlag()) ? "2" : "3");// 现场类别
				protectData.setCheckerCode(checkPropVo.getCreateUser());
				String userName = codeTranService.transCode("UserCode",checkPropVo.getUpdateUser());
				protectData.setCheckerName(userName);
				protectData.setCheckerCertiCode(checkTaskVo.getCheckerIdfNo());
				protectData.setCheckStartTime(checkVo.getCreateTime());//
				protectData.setCheckEndTime(checkVo.getUpdateTime());//
				protectData.setCheckAddr(checkTaskVo.getCheckAddress());
				protectData.setCheckDesc(checkTaskVo.getContexts());

				protectDataVo.add(protectData);
			}
		}
		// 是否包含财损；
		if(!checkPropVos.isEmpty() && checkPropVos.size()>0 && CodeConstants.RadioValue.RADIO_YES
				.equals(checkVo.getIsPropLoss())){
			reBasePartVo.setIsProtectLoss("1");
		} else{
			reBasePartVo.setIsProtectLoss("0");
		}
		//TODO xiaofei 人员查勘情况列表：估损金额 -- 缺失 -- 商业	
		//人员损失情况列表
		List<BICheckReqPersonDataVo> personDataVoList = new ArrayList<BICheckReqPersonDataVo>();
		if(prpLCheckPersons != null && prpLCheckPersons.size() > 0){
			BICheckReqPersonDataVo personData = new BICheckReqPersonDataVo();
			for(PrpLCheckPersonVo checkPerson : prpLCheckPersons){
				personData.setEstimatedLossAmount(checkPerson.getLossFee());
				
				personDataVoList.add(personData);
			}
		}
		requestBodyVo.setPersonDataVo(personDataVoList);

		
		
		// 代位信息列表
		List<BICheckReqSubrogationDataVo> subDataVos = null;
		PrpLSubrogationMainVo subroMainVo = subrogationService.find(registNo);
		if(CodeConstants.RadioValue.RADIO_YES.equals(checkVo.getIsSubRogation())&&subroMainVo!=null){
			subDataVos = new ArrayList<BICheckReqSubrogationDataVo>();

			List<PrpLSubrogationCarVo> subroCarVos = subroMainVo.getPrpLSubrogationCars();
			// 被代为车辆信息
			if(subroCarVos!=null&&subroCarVos.size()>0){
				for(PrpLSubrogationCarVo subroCarVo:subroCarVos){
					BICheckReqSubrogationDataVo subDataVo = new BICheckReqSubrogationDataVo();

					subDataVo.setLinkerName(subroCarVo.getLinkerName());
					subDataVo.setLicensePlateNo(toTrimLicno(subroCarVo.getLicenseNo()));
					subDataVo.setLicensePlateType(subroCarVo.getLicenseType());
					subDataVo.setVIN(subroCarVo.getVinNo());
					subDataVo.setEngineNo(subroCarVo.getEngineNo());
					// subDataVo.setCaInsurerCode(subroCarVo.getCiInsurerCode());
					// subDataVo.setCaInsurerCode(subroCarVo.getCiInsurerArea());
					// subDataVo.setIaInsurerCode(subroCarVo.getBiInsurerCode());
					// subDataVo.setIaInsurerArea(subroCarVo.getBiInsurerArea());

					subDataVos.add(subDataVo);
				}
			}

			List<PrpLSubrogationPersonVo> subPersonVos = subroMainVo.getPrpLSubrogationPersons();
			// 被代为人员信息
			if(subPersonVos!=null&&subPersonVos.size()>0){
				for(PrpLSubrogationPersonVo subPersonVo:subPersonVos){
					BICheckReqSubrogationDataVo subDataVo = new BICheckReqSubrogationDataVo();
					subDataVo.setLinkerName(subPersonVo.getName());

					subDataVos.add(subDataVo);
				}
			}

		}

		requestBodyVo.setBasePartVo(reBasePartVo);
		requestBodyVo.setVehicleDataVo(reVehicleDataVo);
		requestBodyVo.setProtectDataVo(protectDataVo);
		requestBodyVo.setPersonDataVo(null);
		requestBodyVo.setSubrogationDataVo(subDataVos);// 代位信息列表--

		return requestBodyVo;
	}

	private String getCertiType(String registNo,String reqVal) {
		String returnVal1 = "1";
		String returnVal2 = "0";
		List<PrpLCertifyDirectVo> certifyDirectVoList = 
				certifyPubService.findCertifyDirectByRegistNo(registNo);//索赔清单
		if ( certifyDirectVoList != null && !certifyDirectVoList.isEmpty() ) {
			for (PrpLCertifyDirectVo directVo : certifyDirectVoList) {
				String itemCode = directVo.getLossItemCode();
				if ( "C0201".equals(itemCode) ) {
					returnVal1 = "1";
				} else if ( "C0202".equals(itemCode) ) {
					returnVal1 = "2";
				} else if ( "C0203".equals(itemCode) ) {
					returnVal1 = "3";
				}
				if ( "C0102".equals(itemCode) ){//代位求偿案件索赔申请书
					returnVal2 = "1";
				}
			}
		}
		return "1".equals(reqVal) ? returnVal1 : returnVal2;
	}
	// /** 保存交强查勘送平台返回的信息 **/
	// private void saveResposeInfoCI(CICheckResBodyVo resBodyVo) {
	// // 风险信息
	// CICheckResRiskInfoVo riskInfoVo = resBodyVo.getRiskInfoVo();
	// if (riskInfoVo != null) {
	// // 系统提示信息
	// riskInfoVo.getRiskSystemInfo();
	// // 车辆风险类型提示信息
	// riskInfoVo.getVehicleRiskInfo();
	// // 人员风险类型提示信息
	// riskInfoVo.getPersonRiskInfo();
	// // 机构风险类型提示信息
	// riskInfoVo.getInstitutionRiskInfo();
	// }
	// // 锁定信息列表
	// List<CICheckResLockedListVo> loockedListVo = resBodyVo
	// .getLockesListVo();
	// if (loockedListVo != null && loockedListVo.size() > 0) {
	//
	// }
	// }


	// /** 保存交强查勘送平台返回的信息 **/
	// private void saveResposeInfoBI(BICheckResBodyVo resBodyVo) {
	// // 风险信息
	// BICheckResRiskInfoVo riskInfoVo = resBodyVo.getRiskInfoVo();
	// if (riskInfoVo != null) {
	// // 系统提示信息
	// riskInfoVo.getRiskSystemInfo();
	// // 车辆风险类型提示信息
	// riskInfoVo.getVehicleRiskInfoVo();
	// // 人员风险类型提示信息
	// riskInfoVo.getPersonRiskInfoVo();
	// // 机构风险类型提示信息
	// riskInfoVo.getInstituionRiskInfoVo();
	// }
	// // 锁定信息列表
	// List<BICheckResLockedDataVo> loockedListVo = resBodyVo
	// .getLockedDataVo();
	// if (loockedListVo != null && loockedListVo.size() > 0) {
	// // 锁定信息
	// }
	// }

	
	/* 
	 * @see ins.sino.claimcar.platform.service.
	 * CheckToPlatformService#thirdCarInfoQuery
	 * (java.lang.Long, java.lang.String)
	 * @param carId
	 * @param comCode
	 * @return
	 */
	@Override
	public CarInfoHistoryResBodyVo thirdCarInfoQuery(Long carId,String comCode) {
		PrpLCheckCarVo checkCarVo = checkService.findCheckCarById(carId);
		PrpLCheckCarInfoVo carInfoVo = checkCarVo.getPrpLCheckCarInfo();
//		PrpLCheckDriverVo driver = checkCarVo.getPrpLCheckDriver();

		/*
		 * //查询comCode机构代码 PrpLCheckVo checkVo = checkService.queryPrpLCheckVo(checkCarVo.getRegistNo()); PrpLCheckTaskVo checkTaskVo = checkVo.getPrpLCheckTask(); String comCode =
		 * checkTaskVo.getComCode();
		 */
		// 组装报文
		CarInfoHistoryReqVo reqVo = new CarInfoHistoryReqVo();

		CarInfoHistoryReqBasePartVo baseVo = new CarInfoHistoryReqBasePartVo();
		baseVo.setClaimNotificationNo(carInfoVo.getRegistNo());
		
		if(StringUtils.isNotEmpty(carInfoVo.getLicenseType()) && StringUtils.isNotEmpty(carInfoVo.getLicenseNo())){
		    baseVo.setLicensePlateType(carInfoVo.getLicenseType());
		    baseVo.setLicensePlateNo(toTrimLicno(carInfoVo.getLicenseNo()));
		}
		baseVo.setEngineNo(carInfoVo.getEngineNo());
		baseVo.setVin(carInfoVo.getVinNo());
		reqVo.setReqBasePartVo(baseVo);

		// 开始送平台
		PlatformController controller = PlatformFactory.getInstance(comCode,RequestType.ThirdCarInfo);
		controller.callPlatform(reqVo);

		CarInfoHistoryResBodyVo resListVo = null;

		CarInfoHistoryResHeadVo headVo = controller.getHeadVo(CarInfoHistoryResHeadVo.class);
		// 根据报文返回的头部信息判断平台返回数据是否成功
		if(headVo.getResponseCode().equals("1")&&headVo.getErrorCode().equals("0000")){
			// 平台返回数据成功
			resListVo = controller.getBodyVo(CarInfoHistoryResBodyVo.class);
		}

		// String resXml = logVo.getResponseXml();
		// System.out.println(resXml);
		// CarInfoHistoryResBodyVo resListVo = XstreamFactory.xmlToObj(resXml,CarInfoHistoryResBodyVo.class);

		return resListVo;
	}
	/**
	 * 查勘提交，查勘立案同时送平台（同步）
	 */
	@Override
	public void sendCheckSubmitToPlatform( String registNo ) throws Exception{
		
		/* 保单关联与取消的问题，1-报案两张保单，查勘取消了一张，2-报案一张保单，查勘关联了另一张保单 */
		List<PrpLCMainVo> cMainVos = policyViewService.getPolicyAllInfo( registNo );
		if ( cMainVos != null && cMainVos.size() > 0 )
		{
			for ( PrpLCMainVo cMainVo : cMainVos )
			{
				/* 查勘信息送平台,除上海机构 */
				if ( "22".equals( cMainVo.getComCode().substring( 0, 2 ) ) )
					continue;
				try {
					// 查勘信息送平台,除上海机构
					if(Risk.DQZ.equals(cMainVo.getRiskCode())) /* 交强保单 */
					{
						this.sendCheckToPlatformCI(registNo);
						//interfaceAsyncService.sendCheckToPlatformCI(registNo);
					} else {
						this.sendCheckToPlatformBI(registNo);
						//interfaceAsyncService.sendCheckToPlatformBI(registNo);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		}
		//立案送平台
		List<PrpLClaimVo> claimVoList = claimTaskService.findClaimListByRegistNo(registNo);
		if(claimVoList != null && !claimVoList.isEmpty()){
			for(PrpLClaimVo claimVo : claimVoList){
				if(claimVo != null){
					claimTaskService.sendClaimToPlatform(claimVo);
				}
			}
		}
	}
	
	//去掉全角与半角空格
	private  String toTrimLicno(String licenseNo) {
			if(StringUtils.isNotBlank(licenseNo)){
				licenseNo = licenseNo.replace((char)12288,' ').replace(" " ,"");
				return licenseNo;
				
			}else{
				return licenseNo;
			}
			
			
		}
}


	