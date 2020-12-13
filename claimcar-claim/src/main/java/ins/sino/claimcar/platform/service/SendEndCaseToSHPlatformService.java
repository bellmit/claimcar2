/******************************************************************************
* CREATETIME : 2016年6月13日 上午10:03:02
******************************************************************************/
package ins.sino.claimcar.platform.service;

import ins.platform.common.service.facade.AreaDictService;
import ins.platform.common.service.facade.CodeTranService;
import ins.platform.common.util.ConfigUtil;
import ins.platform.utils.DataUtils;
import ins.platform.vo.PrpLConfigValueVo;
import ins.platform.vo.SysCodeDictVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.carplatform.constant.RequestType;
import ins.sino.claimcar.carplatform.controller.PlatformController;
import ins.sino.claimcar.carplatform.service.CiClaimPlatformLogService;
import ins.sino.claimcar.carplatform.util.PlatformFactory;
import ins.sino.claimcar.carplatform.vo.CiClaimPlatformLogVo;
import ins.sino.claimcar.carplatform.vo.CiClaimPlatformTaskVo;
import ins.sino.claimcar.certify.service.CertifyPubService;
import ins.sino.claimcar.certify.vo.PrpLCertifyMainVo;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.PrpLCheckDutyVo;
import ins.sino.claimcar.check.vo.PrpLCheckVo;
import ins.sino.claimcar.claim.service.ClaimCancelService;
import ins.sino.claimcar.claim.service.ClaimTaskService;
import ins.sino.claimcar.claim.services.CompensateService;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claim.vo.PrpLLossItemVo;
import ins.sino.claimcar.claim.vo.PrplReplevyDetailVo;
import ins.sino.claimcar.claim.vo.PrplReplevyMainVo;
import ins.sino.claimcar.constant.Risk;
import ins.sino.claimcar.endcase.service.EndCasePubService;
import ins.sino.claimcar.endcase.service.EndCaseService;
import ins.sino.claimcar.endcase.vo.PrpLEndCaseVo;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.pay.service.RecPayService;
import ins.sino.claimcar.platform.vo.sh.SHBIEndCaseBasePartVo;
import ins.sino.claimcar.platform.vo.sh.SHBIEndCaseObjDataVo;
import ins.sino.claimcar.platform.vo.sh.SHBIEndCaseRecoveryDataVo;
import ins.sino.claimcar.platform.vo.sh.SHBIEndCaseReqBodyVo;
import ins.sino.claimcar.platform.vo.sh.SHBIEndCaseThirdVehicleDataVo;
import ins.sino.claimcar.platform.vo.sh.SHCIEndCaseBasePartVo;
import ins.sino.claimcar.platform.vo.sh.SHCIEndCaseFraudTypeDataVo;
import ins.sino.claimcar.platform.vo.sh.SHCIEndCaseRecoveryDataVo;
import ins.sino.claimcar.platform.vo.sh.SHCIEndCaseReqBodyVo;
import ins.sino.claimcar.platform.vo.sh.SHCIEndCaseThirdVehicleDataVo;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLCItemCarVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.subrogation.service.SubrogationService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import ins.sino.claimcar.utils.HttpUtils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 结案
 * @author ★XMSH
 */
@Service("sendEndCaseToSHPlatformService")
public class SendEndCaseToSHPlatformService {

	private Logger logger = LoggerFactory.getLogger(SendEndCaseToSHPlatformService.class);
	
	@Autowired
	private CompensateService compensateService;
	@Autowired
	private RegistQueryService registQueryService;
	@Autowired
	private ClaimTaskService claimTaskService;
	@Autowired
	private EndCasePubService endCasePubService;
	@Autowired
	private CertifyPubService certifyPubService;
	@Autowired
	private SubrogationService subrogationService;
	@Autowired
	private RecPayService recPayService;
	@Autowired
	CiClaimPlatformLogService platformLogService;
	@Autowired
	PolicyViewService policyViewService;
	@Autowired
	private CheckTaskService checkTaskService;
	@Autowired
	EndCaseService endCaseService;
	@Autowired
	private LossCarService lossCarService;
	@Autowired
	private CodeTranService codeTranService;
	@Autowired
	SendEndCaseAddToSHPlatformService sendEndCaseAddToSH;
	@Autowired
	ClaimCancelService claimCancelService;
	@Autowired
	private AreaDictService areaDictService;
	
	/**
	 * 发送-- 结案，补传调用方法(上海平台)
	 * @param bussNo
	 * @param reqType
	 * @throws Exception 
	 */
	public void sendEndCaseToSH(String registNo,String claimNo,CiClaimPlatformTaskVo platformTaskVo){
		PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
		// 联共保 从共，从联不交互平台
		if(CodeConstants.GBFlag.MINORCOMMON.equals(registVo.getIsGBFlag())||CodeConstants.GBFlag.MINORRELATION.equals(registVo.getIsGBFlag())){
			return;
		}
		logger.debug(registNo);
		PrpLEndCaseVo endVo = endCaseService.queryEndCaseVo(registNo,claimNo);
		if(endVo != null){
			PrpLCompensateVo compensateVo = compensateService.findCompByPK(endVo.getCompensateNo());
			compensateVo = findCompensateByClaim(compensateVo,claimNo);
			if(compensateVo == null){
				logger.info("========发送平台结案提交调用方法(金额确认)失败，没有找到计算书信息(理算信息)=========");
				return;
			}
			String policyType = Risk.DQZ.equals(endVo.getRiskCode()) ? "11" : "12";
			String comCode = policyViewService.findPolicyComCode(registNo,policyType);
			if(Risk.DQZ.equals(endVo.getRiskCode())){
				sendEndCaseCIToSH(compensateVo,comCode,platformTaskVo);
			}else{
				sendEndCaseBIToSH(compensateVo,comCode,platformTaskVo);
			}
		}
	}
	
	/**
	 * 发送平台结案提交调用方法(金额确认)
	 * @param compensateNo
	 */
	public void sendEndCaseToSHBySubmit(PrpLEndCaseVo endCaseVo,CiClaimPlatformTaskVo platformTaskVo){
		if(endCaseVo == null){
			logger.info("========发送平台结案提交调用方法(金额确认)失败，没有找到结案信息=========");
			return;
		}
		PrpLCompensateVo compensateVo = compensateService.findCompByPK(endCaseVo.getCompensateNo());
		compensateVo = findCompensateByClaim(compensateVo,endCaseVo.getClaimNo());
		if(compensateVo == null){
			logger.info("========发送平台结案提交调用方法(金额确认)失败，因为没有找到计算书信息(理算信息)========="+endCaseVo.getClaimNo());
			return;
		}
		String registNo = compensateVo.getRegistNo();
		String riskCode = compensateVo.getRiskCode();
		String reqType = Risk.DQZ.equals(riskCode) 
				? RequestType.EndCaseCI_SH.getCode() : RequestType.EndCaseBI_SH.getCode();
		String policyType = Risk.DQZ.equals(riskCode) ? "11" : "12";
		String comCode = policyViewService.findPolicyComCode(registNo,policyType);
		CiClaimPlatformLogVo logVo = platformLogService.findLogByBussNo(reqType,endCaseVo.getClaimNo(),comCode);
		if(logVo == null){//结案送上海，不存在上传成功的记录
			//上海平台重开赔案后，不用再次送平台的金额确认（原结案）
			if(Risk.DQZ.equals(riskCode)){
				sendEndCaseCIToSH(compensateVo,comCode,platformTaskVo);
			}else{
				sendEndCaseBIToSH(compensateVo,comCode,platformTaskVo);
			}
		}else{
			//存在成功的结案记录，送结案追加
			sendEndCaseAddToSH.sendEndCaseAddToSH(endCaseVo.getEndCaseNo());
		}
		
	}
	
	private PrpLCompensateVo findCompensateByClaim(PrpLCompensateVo compensateVo,String claimNo){
		if(compensateVo == null){
			List<PrpLCompensateVo> compensateVoList = compensateService.findCompListByClaimNo(claimNo,"N");
			if(compensateVoList !=null && compensateVoList.size() > 0){
				compensateVo = compensateVoList.get(0);
				compensateVo.setSumAmt(BigDecimal.ZERO);
				compensateVo.setSumFee(BigDecimal.ZERO);
			}else{
				compensateVo = null;
			}
		}
		return compensateVo;
	}
	
	public void sendEndCaseCIToSH(PrpLCompensateVo compensateVo,String comCode,CiClaimPlatformTaskVo platformTaskVo){
		String registNo = compensateVo.getRegistNo();
		CiClaimPlatformLogVo logVo = platformLogService.findLogByBussNo
				(RequestType.RegistInfoCI_SH.getCode(),registNo,comCode);
		if (logVo == null) {
			logger.info("交强结案上传全国平台失败！该案件理赔编码不存在或报案未成功上传平台！");
		}
		//投保确认码
		String validNo = policyViewService.findValidNo(registNo,"11");
		logger.info("投保确认码："+validNo);
		String claimSeqNo = logVo==null?"":logVo.getClaimSeqNo();
		logger.info("理赔编码："+claimSeqNo);
		//取标的车信息
		PrpLCMainVo prpLCMainVo=new PrpLCMainVo();
		List<PrpLCMainVo> cMainVoList = policyViewService.getPolicyAllInfo(registNo);
		if(cMainVoList!=null && cMainVoList.size()>0){
			for(PrpLCMainVo cmainVo:cMainVoList){
				if("1101".equals(cmainVo.getRiskCode())){
					prpLCMainVo=cmainVo;
					break;
				}else{
					prpLCMainVo=cmainVo;
				}
			}
		}
		PrpLCItemCarVo prpLCItemCarVo=new PrpLCItemCarVo();
		if(prpLCMainVo.getPrpCItemCars()!=null && prpLCMainVo.getPrpCItemCars().size()>0){
			prpLCItemCarVo=prpLCMainVo.getPrpCItemCars().get(0);
		}
		
		PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
		PrpLClaimVo claimVo = claimTaskService.findClaimVoByClaimNo(compensateVo.getClaimNo());
		PrpLCertifyMainVo certifyMainVo = certifyPubService.findPrpLCertifyMainVoByRegistNo(registNo);
		PrplReplevyMainVo replevyMainVo = recPayService.findPrplReplevyMainVoByClaimNo(compensateVo.getClaimNo());
		PrpLCheckVo checkVo = checkTaskService.findCheckVoByRegistNo(registNo);
		//PrpLcancelTraceVo prpLcancelTraceVo = claimCancelService.findByClaimNo(claimVo.getClaimNo());
		PlatformController controller = PlatformFactory.getInstance(comCode,RequestType.EndCaseCI_SH);
		
		SHCIEndCaseReqBodyVo bodyVo = new SHCIEndCaseReqBodyVo();
		
		SHCIEndCaseBasePartVo basePart = new SHCIEndCaseBasePartVo();
		basePart.setConfirmSequenceNo(validNo);
		basePart.setClaimCode(claimSeqNo);
		basePart.setClaimAmount(compensateVo.getSumAmt().doubleValue());
		basePart.setReportNo(registNo);
		basePart.setRegistrationNo(compensateVo.getClaimNo());
		basePart.setClaimNo(compensateVo.getCompensateNo());
		basePart.setReportTime(registVo.getReportTime());
		basePart.setRegistrationDate(claimVo.getClaimTime());
		basePart.setEndcaseDate(claimVo.getEndCaseTime());
		basePart.setDocStartTime(certifyMainVo.getStartTime());
		basePart.setDocEndTime(certifyMainVo.getEndTime()==null
		?certifyMainVo.getUpdateTime():certifyMainVo.getEndTime());
		basePart.setNumerationStartTime(compensateVo.getCreateTime());
		basePart.setNumerationEndTime(compensateVo.getUnderwriteDate());
		basePart.setAssesorStartTime(compensateVo.getUnderwriteDate());
		basePart.setAssesorEndTime(compensateVo.getUnderwriteDate());
//		basePart.setPayTime(payTime);
		basePart.setInsured("1");
//		basePart.setClaimType(claimVo.getClaimType());
		basePart.setClaimType("1");
//		basePart.setPayCause(payCause);
	    basePart.setRefuseCause(certifyMainVo.getFraudRefuseReason());
		if("1".equals(certifyMainVo.getIsFraud())){
			basePart.setFraudLogo(certifyMainVo.getFraudLogo());
			basePart.setFraudRecoverAmount(DataUtils.NullToZero(certifyMainVo.getFraudRecoverAmount()).doubleValue());
		}
		basePart.setAccidentReason(checkVo.getAccidentReason());
		
		List<PrpLDlossCarMainVo> lossCarMainVos = lossCarService.findLossCarMainByRegistNo(registNo);
		for (PrpLDlossCarMainVo lossCarMainVo : lossCarMainVos) {
			if ("1".equals(lossCarMainVo.getDeflossCarType())) {
				//若是上海保单，号牌种类为挂、学、警、领车牌则控制末尾不能录入中文，车牌号长度不能超过6位
				if(lossCarMainVo.getLossCarInfoVo().getLicenseNo()!=null&&lossCarMainVo.getLossCarInfoVo().getLicenseNo().length()>=6&&
						("15".equals(lossCarMainVo.getLossCarInfoVo().getLicenseType())||"16".equals(lossCarMainVo.getLossCarInfoVo().getLicenseType())||
						"17".equals(lossCarMainVo.getLossCarInfoVo().getLicenseType())||"23".equals(lossCarMainVo.getLossCarInfoVo().getLicenseType())||
						"31".equals(lossCarMainVo.getLossCarInfoVo().getLicenseType())||"04".equals(lossCarMainVo.getLossCarInfoVo().getLicenseType())||
						"10".equals(lossCarMainVo.getLossCarInfoVo().getLicenseType()))){
					if(StringUtils.isNotBlank(prpLCItemCarVo.getLicenseNo())){
						basePart.setCarMark(prpLCItemCarVo.getLicenseNo().substring(0, 6));
					}else{
						basePart.setCarMark(lossCarMainVo.getLossCarInfoVo().getLicenseNo().substring(0, 6));
					}
					
				}else{
					
				 basePart.setCarMark(prpLCItemCarVo.getLicenseNo());
					
					
				}
				
				basePart.setVehicleType(prpLCItemCarVo.getLicenseKindCode());
				basePart.setDriverName(lossCarMainVo.getLossCarInfoVo().getDriveName());
				
				SysCodeDictVo sysdictVo = codeTranService.findTransCodeDictVo
				("IdentifyType",lossCarMainVo.getLossCarInfoVo().getIdentifyType());
				String certiType = sysdictVo==null?"01":sysdictVo.getProperty4();
				basePart.setCertiType(certiType);
				basePart.setCertiCode(lossCarMainVo.getLossCarInfoVo().getIdentifyNo());
				basePart.setLicenseNo(lossCarMainVo.getLossCarInfoVo().getDrivingLicenseNo());
//				basePart.setLicenseEffecturalDate(licenseEffecturalDate);
			}
		}
//		basePart.setPersonNum(personNum);
		basePart.setPolicyNo(registVo.getPolicyNo());
//		basePart.setRoadAccident(roadAccident);
//		basePart.setOtherAmount(otherAmount);
//		basePart.setSubrogationFlag(subrogationFlag);
		//事故信息
		basePart.setAccidentTime(registVo.getDamageTime());
		String standardAddress = HttpUtils.getSHRoadInfo(registVo.getDamageAreaCode(), registVo.getDamageAddress(), registVo.getRegistNo());
		if (StringUtils.isBlank(standardAddress)) {
			basePart.setAccidentPlace(getStandardAddress(registVo.getDamageAreaCode(), registVo.getDamageAddress()));
		} else {
			basePart.setAccidentPlace(standardAddress);
		}
		basePart.setAccidentDescription(registVo.getDamageCode());
		basePart.setManageDepartment(registVo.getComCode());
		// 上海保单上海出险  从标准地址库中获取地名唯一标识
		String accidentPlaceMark = HttpUtils.getSHAccidentPlaceMark(registVo.getDamageAreaCode(), registVo.getDamageAddress(), registVo.getRegistNo());
		basePart.setAccidentPlaceMark(accidentPlaceMark);

		/*************************标准地址库版本号、坐标数据、坐标系代码**************************/
		// 获取报案上传平台开关
		PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.REGISTTOPALTFORMFLAG, "22000000");
		// 如果开关打开，需要组织坐标数据及坐标系代码，坐标数据为空时，传(-1, -1)
		if ("1".equals(configValueVo.getConfigValue())) {
			// 经纬度
			basePart.setCoordinate(HttpUtils.getCoordinate(registVo.getDamageMapCode()));
			// 01-GPS坐标系  02-百度坐标系  03-火星坐标系，如高德、腾讯  04-2000 国家大地坐标系
			basePart.setCoordinateSystem("02");
		}
		/*************************标准地址库版本号、坐标数据、坐标系代码**************************/
		
		PrpLCheckDutyVo checkDutyVo = checkTaskService.findCheckDuty(registNo,1);
		SysCodeDictVo sysVo = 
		codeTranService.findTransCodeDictVo("IndemnityDuty",checkDutyVo.getIndemnityDuty());
		basePart.setLiabilityAmount(sysVo==null?"9":sysVo.getProperty1());
//		basePart.setManageType(manageType);
//		basePart.setAccidentPlaceMark(accidentPlaceMark);
		List<SHCIEndCaseThirdVehicleDataVo> thirdVehicleList = new ArrayList<SHCIEndCaseThirdVehicleDataVo>();
		for(PrpLLossItemVo itemVo : compensateVo.getPrpLLossItems()){
			if(StringUtils.isNotBlank(itemVo.getLossType())&&
					"3".equals(itemVo.getLossType())){
				SHCIEndCaseThirdVehicleDataVo thirdVehicleData = new SHCIEndCaseThirdVehicleDataVo();
				thirdVehicleData.setCarMark(itemVo.getItemName());
//				thirdVehicleData.setVvehicleType(vvehicleType);
//				thirdVehicleData.setNirresponsibilityCompany(nirresponsibilityCompany);
//				thirdVehicleData.setAdvanceAmount(advanceAmount);
				thirdVehicleList.add(thirdVehicleData);
			}
		}
		
		List<SHCIEndCaseRecoveryDataVo> recoveryList = new ArrayList<SHCIEndCaseRecoveryDataVo>();
		for(PrplReplevyDetailVo detailVo:replevyMainVo.getPrplReplevyDetails()){
			SHCIEndCaseRecoveryDataVo recoveryData = new SHCIEndCaseRecoveryDataVo();
//			recoveryData.setBerecoveryReportNo(detailVo.get);
			recoveryData.setRecoveryAmount(detailVo.getReplevyFee().doubleValue());
			
			recoveryList.add(recoveryData);
		}
		
		// 欺诈类型
		if("1".equals(certifyMainVo.getIsFraud())){
			List<SHCIEndCaseFraudTypeDataVo>  fraudTypeDataList = new ArrayList<SHCIEndCaseFraudTypeDataVo>();
			SHCIEndCaseFraudTypeDataVo fraudTypeDataVo = new SHCIEndCaseFraudTypeDataVo();
			fraudTypeDataVo.setFraudType(certifyMainVo.getFraudType());
			fraudTypeDataList.add(fraudTypeDataVo);
			bodyVo.setFraudTypeData(fraudTypeDataList);
		}
		
		bodyVo.setBasePart(basePart);
		bodyVo.setThirdVehicleList(thirdVehicleList);
		bodyVo.setRecoveryList(recoveryList);
		bodyVo.setDisputeList(null);
		
		controller.callPlatform(bodyVo,platformTaskVo);
	}
	
	public void sendEndCaseBIToSH(PrpLCompensateVo compensateVo,String comCode,CiClaimPlatformTaskVo platformTaskVo) {
		String registNo = compensateVo.getRegistNo();
		CiClaimPlatformLogVo logVo = platformLogService.findLogByBussNo
				(RequestType.RegistInfoBI_SH.getCode(),registNo,comCode);
		if (logVo == null) {
			logger.info("商业结案上传上海平台失败！该案件理赔编码不存在或报案未成功上传平台！");
		}
		String claimSeqNo = logVo == null ? "" : logVo.getClaimSeqNo();
		//投保确认码
		String validNo = policyViewService.findValidNo(registNo,"12");
		logger.info("投保确认码："+validNo);
		//取标的车信息
		PrpLCMainVo prpLCMainVo=new PrpLCMainVo();
		List<PrpLCMainVo> cMainVoList = policyViewService.getPolicyAllInfo(registNo);
		if(cMainVoList!=null && cMainVoList.size()>0){
			for(PrpLCMainVo cmainVo:cMainVoList){
				if(!("1101".equals(cmainVo.getRiskCode()))){
					prpLCMainVo=cmainVo;
					break;
				}else{
					prpLCMainVo=cmainVo;
				}
			}
		}
		PrpLCItemCarVo prpLCItemCarVo=new PrpLCItemCarVo();
		if(prpLCMainVo.getPrpCItemCars()!=null && prpLCMainVo.getPrpCItemCars().size()>0){
			prpLCItemCarVo=prpLCMainVo.getPrpCItemCars().get(0);
		}
		
		PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
		PrpLClaimVo claimVo = claimTaskService.findClaimVoByClaimNo(compensateVo.getClaimNo());
		PrpLCertifyMainVo certifyMainVo = certifyPubService.findPrpLCertifyMainVoByRegistNo(registNo);
		PrplReplevyMainVo replevyMainVo = recPayService.findPrplReplevyMainVoByClaimNo(compensateVo.getClaimNo());
		//PrpLcancelTraceVo prpLcancelTraceVo = claimCancelService.findByClaimNo(claimVo.getClaimNo());
		PrpLCheckVo checkVo = checkTaskService.findCheckVoByRegistNo(registNo);
		PlatformController controller = PlatformFactory.getInstance(
				comCode, RequestType.EndCaseBI_SH);

		SHBIEndCaseReqBodyVo bodyVo = new SHBIEndCaseReqBodyVo();

		SHBIEndCaseBasePartVo basePart = new SHBIEndCaseBasePartVo();
		basePart.setConfirmSequenceNo(validNo);
		basePart.setClaimCode(claimSeqNo);
		basePart.setTotalAmount(compensateVo.getSumAmt().doubleValue());
		basePart.setReportNo(registNo);
		basePart.setRegistrationNo(compensateVo.getClaimNo());
		basePart.setClaimNo(compensateVo.getCompensateNo());
		
		basePart.setReportTime(registVo.getReportTime());
		basePart.setRegistrationDate(claimVo.getClaimTime());
		basePart.setEndcaseDate(claimVo.getEndCaseTime());
		basePart.setDocStartTime(certifyMainVo.getStartTime());
		basePart.setDocEndTime(certifyMainVo.getEndTime()==null
		?certifyMainVo.getUpdateTime():certifyMainVo.getEndTime());
		basePart.setNumerationStartTime(compensateVo.getCreateTime());
		basePart.setNumerationEndTime(compensateVo.getUnderwriteDate());
		basePart.setAssesorStartTime(compensateVo.getUnderwriteDate());
		basePart.setAssesorEndTime(compensateVo.getUnderwriteDate());
		
		if("1".equals(certifyMainVo.getIsFraud())){
			basePart.setFraudLogo(certifyMainVo.getFraudLogo());
			basePart.setFraudRecoverAmount(DataUtils.NullToZero(certifyMainVo.getFraudRecoverAmount()).doubleValue());
		}
		basePart.setAccidentReason(checkVo.getAccidentReason() == null ? "09" : checkVo.getAccidentReason());//上海商业平台事故原因必传
		basePart.setReporter(registVo.getReportorName());
		// basePart.setPayTime(payTime);
//		basePart.setClaimType(claimVo.getClaimType());
//		basePart.setClaimType("1");
		// basePart.setPayCause(payCause);
		// basePart.setRefuseCause(certifyMainVo.getFraudRefuseReason());
		List<PrpLDlossCarMainVo> lossCarMainVos = lossCarService.findLossCarMainByRegistNo(registNo);
		for (PrpLDlossCarMainVo lossCarMainVo : lossCarMainVos) {
			if ("1".equals(lossCarMainVo.getDeflossCarType())) {
				//若是上海保单，号牌种类为挂、学、警、领车牌则控制末尾不能录入中文，车牌号长度不能超过6位
				if(lossCarMainVo.getLossCarInfoVo().getLicenseNo()!=null&&lossCarMainVo.getLossCarInfoVo().getLicenseNo().length()>=6&&
						("15".equals(lossCarMainVo.getLossCarInfoVo().getLicenseType())||"16".equals(lossCarMainVo.getLossCarInfoVo().getLicenseType())||
						"17".equals(lossCarMainVo.getLossCarInfoVo().getLicenseType())||"23".equals(lossCarMainVo.getLossCarInfoVo().getLicenseType())||
						"31".equals(lossCarMainVo.getLossCarInfoVo().getLicenseType())||"04".equals(lossCarMainVo.getLossCarInfoVo().getLicenseType())||
						"10".equals(lossCarMainVo.getLossCarInfoVo().getLicenseType()))){
					if(StringUtils.isNotBlank(prpLCItemCarVo.getLicenseNo())){
						basePart.setCarMark(prpLCItemCarVo.getLicenseNo().substring(0, 6));
					}else{
						basePart.setCarMark(lossCarMainVo.getLossCarInfoVo().getLicenseNo().substring(0, 6));
					}
					
				}else{
					
				 basePart.setCarMark(prpLCItemCarVo.getLicenseNo());
					
					
				}
				
				basePart.setVehicleType(prpLCItemCarVo.getLicenseKindCode());
				basePart.setDriverName(lossCarMainVo.getLossCarInfoVo().getDriveName());
				
//				SysCodeDictVo sysVo = codeTranService.findTransCodeDictVo
//				("IdentifyType",lossCarMainVo.getLossCarInfoVo().getIdentifyType());
//				String certiType = sysVo==null?"01":sysVo.getProperty3();
				basePart.setLicenseNo(lossCarMainVo.getLossCarInfoVo().getDrivingLicenseNo());
//				basePart.setLicenseEffecturalDate(licenseEffecturalDate);
				basePart.setSubrogationFlag(claimVo.getIsSubRogation());
				basePart.setIstotalloss(claimVo.getIsTotalLoss());
			}
		}
		// basePart.setLicenseEffecturalDate(licenseEffecturalDate);
		// basePart.setPersonNum(personNum);
		basePart.setPolicyNo(registVo.getPolicyNo());
		// basePart.setRoadAccident(roadAccident);
		basePart.setOtherAmount(compensateVo.getSumAmt().doubleValue());
		
		
		// 事故信息
		basePart.setAccidentTime(registVo.getDamageTime());
		String standardAddress = HttpUtils.getSHRoadInfo(registVo.getDamageAreaCode(), registVo.getDamageAddress(), registVo.getRegistNo());
		if (StringUtils.isBlank(standardAddress)) {
			basePart.setAccidentPlace(getStandardAddress(registVo.getDamageAreaCode(), registVo.getDamageAddress()));
		} else {
			basePart.setAccidentPlace(standardAddress);
		}
		basePart.setAccidentDescription(registVo.getDamageCode());
		PrpLCheckDutyVo checkDutyVo = checkTaskService.findCheckDuty(registNo,1);
		SysCodeDictVo sysVo = 
		codeTranService.findTransCodeDictVo("IndemnityDuty",checkDutyVo.getIndemnityDuty());
		basePart.setLiability(sysVo==null?"9":sysVo.getProperty1());
		basePart.setOptionType(registVo.getPrpLRegistExt().getManageType());
		// 上海保单上海出险  从标准地址库中获取地名唯一标识
		String accidentPlaceMark = HttpUtils.getSHAccidentPlaceMark(registVo.getDamageAreaCode(), registVo.getDamageAddress(), registVo.getRegistNo());
		basePart.setAccidentPlaceMark(accidentPlaceMark);

		/*************************标准地址库版本号、坐标数据、坐标系代码**************************/
		// 获取报案上传平台开关
		PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.REGISTTOPALTFORMFLAG, "22000000");
		// 如果开关打开，需要组织坐标数据及坐标系代码，坐标数据为空时，传(-1, -1)
		if ("1".equals(configValueVo.getConfigValue())) {
			// 经纬度
			basePart.setCoordinate(HttpUtils.getCoordinate(registVo.getDamageMapCode()));
			// 01-GPS坐标系  02-百度坐标系  03-火星坐标系，如高德、腾讯  04-2000 国家大地坐标系
			basePart.setCoordinateSystem("02");
		}
		/*************************标准地址库版本号、坐标数据、坐标系代码**************************/

		// basePart.setLiabilityAmount(liabilityAmount);
		// basePart.setManageType(manageType);
		// basePart.setAccidentPlaceMark(accidentPlaceMark);
		List<SHBIEndCaseThirdVehicleDataVo> thirdVehicleList = new ArrayList<SHBIEndCaseThirdVehicleDataVo>();
		for (PrpLLossItemVo itemVo : compensateVo.getPrpLLossItems()) {
			if (!"0".equals(itemVo.getItemId())
					&& !"1".equals(itemVo.getItemId())) {
				SHBIEndCaseThirdVehicleDataVo thirdVehicleData = new SHBIEndCaseThirdVehicleDataVo();
				thirdVehicleData.setCarMark(itemVo.getItemName());
				// thirdVehicleData.setVvehicleType(vvehicleType);
				// thirdVehicleData.setNirresponsibilityCompany(nirresponsibilityCompany);
				// thirdVehicleData.setAdvanceAmount(advanceAmount);
				thirdVehicleList.add(thirdVehicleData);
			}
		}
		
		//损失情况列表（多条）
		List<SHBIEndCaseObjDataVo> objDataVoList = new ArrayList<SHBIEndCaseObjDataVo>();
		for (PrpLDlossCarMainVo lossCarMainVo : lossCarMainVos) {
			if ("1".equals(lossCarMainVo.getDeflossCarType())) {
				SHBIEndCaseObjDataVo objDataVo = new SHBIEndCaseObjDataVo();
				objDataVo.setObjName(lossCarMainVo.getLicenseNo());
				if(lossCarMainVo.getLossCarInfoVo().getLicenseType()==null){
					objDataVo.setVehicleType("02");
				}else{
					objDataVo.setVehicleType(lossCarMainVo.getLossCarInfoVo().getLicenseType());
				}
				objDataVo.setObjType("1");
				objDataVo.setMainThird("1");
				
			}
		}

		List<SHBIEndCaseRecoveryDataVo> recoveryList = new ArrayList<SHBIEndCaseRecoveryDataVo>();
		for (PrplReplevyDetailVo detailVo : replevyMainVo.getPrplReplevyDetails()) {
			SHBIEndCaseRecoveryDataVo recoveryData = new SHBIEndCaseRecoveryDataVo();
			// recoveryData.setBerecoveryReportNo(detailVo.get);
			recoveryData.setRecoveryAmount(detailVo.getReplevyFee().doubleValue());
			recoveryList.add(recoveryData);
		}

		//欺诈类型
		if("1".equals(certifyMainVo.getIsFraud())){
			List<SHCIEndCaseFraudTypeDataVo>  fraudTypeDataList = new ArrayList<SHCIEndCaseFraudTypeDataVo>();
			SHCIEndCaseFraudTypeDataVo fraudTypeDataVo = new SHCIEndCaseFraudTypeDataVo();
			fraudTypeDataVo.setFraudType(certifyMainVo.getFraudType());
			fraudTypeDataList.add(fraudTypeDataVo);
			bodyVo.setFraudTypeData(fraudTypeDataList);
		}
		
		bodyVo.setBasePart(basePart);
		bodyVo.setThirdVehicleList(thirdVehicleList);
		bodyVo.setObjList(objDataVoList);
		bodyVo.setRecoveryList(recoveryList);
		bodyVo.setDisputeList(null);

		controller.callPlatform(bodyVo,platformTaskVo);
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
			address.append("未知地址-未知地址-未知地址-未知地址-未知地址");
		}
		return address.toString();
	}
}
