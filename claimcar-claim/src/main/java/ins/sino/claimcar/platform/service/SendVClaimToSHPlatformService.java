/******************************************************************************
 * CREATETIME : 2016年6月2日 上午10:17:07
 ******************************************************************************/
package ins.sino.claimcar.platform.service;

import ins.platform.common.service.facade.AreaDictService;
import ins.platform.common.service.facade.CodeTranService;
import ins.platform.common.util.ConfigUtil;
import ins.platform.utils.DataUtils;
import ins.platform.vo.PrpLConfigValueVo;
import ins.platform.vo.SysCodeDictVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.KINDCODE;
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
import ins.sino.claimcar.claim.service.ClaimTaskService;
import ins.sino.claimcar.claim.services.CompensateService;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claim.vo.PrpLLossItemVo;
import ins.sino.claimcar.claim.vo.PrpLLossPersonFeeVo;
import ins.sino.claimcar.claim.vo.PrpLLossPersonVo;
import ins.sino.claimcar.claim.vo.PrpLLossPropVo;
import ins.sino.claimcar.constant.Risk;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarCompVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarRepairVo;
import ins.sino.claimcar.lossperson.service.PersTraceDubboService;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersExtVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersHospitalVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceFeeVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceMainVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceVo;
import ins.sino.claimcar.lossprop.service.PropTaskService;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropFeeVo;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropMainVo;
import ins.sino.claimcar.platform.vo.sh.SHBIVClaimActuralRepairDataVo;
import ins.sino.claimcar.platform.vo.sh.SHBIVClaimBasePartVo;
import ins.sino.claimcar.platform.vo.sh.SHBIVClaimCarLossPartDataVo;
import ins.sino.claimcar.platform.vo.sh.SHBIVClaimCoverDataVo;
import ins.sino.claimcar.platform.vo.sh.SHBIVClaimFittingDataVo;
import ins.sino.claimcar.platform.vo.sh.SHBIVClaimHospitalInfoDataVo;
import ins.sino.claimcar.platform.vo.sh.SHBIVClaimInjuryDataVo;
import ins.sino.claimcar.platform.vo.sh.SHBIVClaimObjDataVo;
import ins.sino.claimcar.platform.vo.sh.SHBIVClaimPersLossPartDataVo;
import ins.sino.claimcar.platform.vo.sh.SHBIVClaimPersonDataVo;
import ins.sino.claimcar.platform.vo.sh.SHBIVClaimRepartDataVo;
import ins.sino.claimcar.platform.vo.sh.SHBIVClaimReqBodyVo;
import ins.sino.claimcar.platform.vo.sh.SHBIVClaimVehicleDataVo;
import ins.sino.claimcar.platform.vo.sh.SHCIVClaimActuralRepairDataVo;
import ins.sino.claimcar.platform.vo.sh.SHCIVClaimBasePartVo;
import ins.sino.claimcar.platform.vo.sh.SHCIVClaimCarLossPartDataVo;
import ins.sino.claimcar.platform.vo.sh.SHCIVClaimCoverDataVo;
import ins.sino.claimcar.platform.vo.sh.SHCIVClaimDetailDataVo;
import ins.sino.claimcar.platform.vo.sh.SHCIVClaimFittingDataVo;
import ins.sino.claimcar.platform.vo.sh.SHCIVClaimHospitalInfoDataVo;
import ins.sino.claimcar.platform.vo.sh.SHCIVClaimInjuryDataVo;
import ins.sino.claimcar.platform.vo.sh.SHCIVClaimInjuryIdentifyInfoDataVo;
import ins.sino.claimcar.platform.vo.sh.SHCIVClaimObjDataVo;
import ins.sino.claimcar.platform.vo.sh.SHCIVClaimPersLossPartDataVo;
import ins.sino.claimcar.platform.vo.sh.SHCIVClaimPersonDataVo;
import ins.sino.claimcar.platform.vo.sh.SHCIVClaimRepairDataVo;
import ins.sino.claimcar.platform.vo.sh.SHCIVClaimReqBodyVo;
import ins.sino.claimcar.platform.vo.sh.SHCIVClaimVehicleDataVo;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ins.sino.claimcar.utils.HttpUtils;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ★XMSH
 */
@Service("sendVClaimToSHPlatformService")
public class SendVClaimToSHPlatformService {

	private Logger logger = LoggerFactory.getLogger(SendVClaimToSHPlatformService.class);
	@Autowired
	private CompensateService compensateService;
	@Autowired
	private LossCarService lossCarService;
	@Autowired
	private RegistQueryService registQueryService;
	@Autowired
	private CheckTaskService checkTaskService;
	@Autowired
	private ClaimTaskService claimTaskService;
	@Autowired
	private PersTraceDubboService persTraceDubboService;
	@Autowired
	private PolicyViewService policyViewService;
	@Autowired
	CiClaimPlatformLogService platformLogService;
	@Autowired
	PropTaskService propTaskService;
	@Autowired
	private CodeTranService codeTranService;
	@Autowired
	private CertifyPubService certifyPubService;
	@Autowired
	private KindCodeTranService kindCodeTranService;
	@Autowired
	private AreaDictService areaDictService;
	
	/**
	 * 核赔发送上海平台调用方法
	 */
	public void sendVClaimToSH(PrpLCompensateVo compensateVo,String comCode,CiClaimPlatformTaskVo platformTaskVo){
		if(compensateVo == null){
			return;
		}
		String registNo = compensateVo.getRegistNo();
		String riskCode = compensateVo.getRiskCode();
		String reqType = Risk.DQZ.equals(riskCode) 
				? RequestType.VClaimCI_SH.getCode() : RequestType.VClaimBI_SH.getCode();
		CiClaimPlatformLogVo logVo = platformLogService.findLogByBussNo(reqType, registNo, comCode);
		if(logVo == null){//上海平台，重开后不用再次上传
			if (Risk.DQZ.equals(riskCode)) {// 交强
				sendVClaimCIToSHPlatform(compensateVo,platformTaskVo);
			} else {// 商业
				sendVClaimBIToSHPlatform(compensateVo,platformTaskVo);
			}
		}
	}
	
	/**
	 * @param compeNo--计算书号
	 */
	public void sendVClaimCIToSHPlatform(PrpLCompensateVo compensateVo,CiClaimPlatformTaskVo platformTaskVo) {
		Long currentDate = System.currentTimeMillis();
		logger.info("sendVClaimCIToSHPlatform begin，registNo={}",compensateVo.getRegistNo());
		String registNo = compensateVo.getRegistNo();
		PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
		// 联共保 从共，从联不交互平台
		if(CodeConstants.GBFlag.MINORCOMMON.equals(registVo.getIsGBFlag())||CodeConstants.GBFlag.MINORRELATION.equals(registVo.getIsGBFlag())){
			return;
		}
		String comCode = policyViewService.findPolicyComCode(registNo, "11");
		CiClaimPlatformLogVo logVo = platformLogService.findLogByBussNo(
				RequestType.RegistInfoCI_SH.getCode(), registNo, comCode);
		if (logVo == null) {
			logger.info("交强理算核赔上传上海平台失败！该案件理赔编码不存在或报案未成功上传平台！");
		}

		List<PrpLDlossCarMainVo> lossCarMainVos = lossCarService.findLossCarMainByRegistNo(registNo);
		PrpLCheckDutyVo checkDutyVo = checkTaskService.findCheckDuty(registNo,1);
		PrpLClaimVo claimVo = claimTaskService.findClaimVoByClaimNo(compensateVo.getClaimNo());

		PrpLCheckVo checkVo = checkTaskService.findCheckVoByRegistNo(registNo);
		List<PrpLDlossPersTraceMainVo> traceMainVos = persTraceDubboService.findPersTraceMainVoList(registNo);
		
		List<PrpLdlossPropMainVo> propVoList = propTaskService.findPropMainListByRegistNo(registNo);

		//送平台
		PlatformController controller = PlatformFactory.getInstance(comCode,RequestType.VClaimCI_SH);

		SHCIVClaimReqBodyVo bodyVo = new SHCIVClaimReqBodyVo();

		SHCIVClaimBasePartVo basePart = new SHCIVClaimBasePartVo();
		basePart.setClaimCode(logVo==null?"":logVo.getClaimSeqNo());
		basePart.setNumerationStartTime(compensateVo.getCreateTime());
		basePart.setNumerationEndTime(compensateVo.getUpdateTime());
		// basePart.setAssesorStartTime(assesorStartTime);
		basePart.setAssesorEndTime(compensateVo.getUnderwriteDate());
		basePart.setOtherAmount(DataUtils.NullToZero(compensateVo.getSumFee()).doubleValue());
		basePart.setAssesorDes("1");
		PrpLCertifyMainVo certifyMainVo = certifyPubService.findPrpLCertifyMainVoByRegistNo(registNo);
		if(certifyMainVo != null && "1".equals(certifyMainVo.getIsFraud())){
			basePart.setAssesorDes("3"); //核赔意见 拒赔
		}
		
		basePart.setAssesorAmount(DataUtils.NullToZero(compensateVo.getSumAmt()).doubleValue());

		for (PrpLDlossCarMainVo lossCarMainVo : lossCarMainVos) {
			if ("1".equals(lossCarMainVo.getDeflossCarType())) {
				basePart.setDriverName(lossCarMainVo.getLossCarInfoVo().getDriveName());
			//	String certiType = "1".equals(lossCarMainVo.getLossCarInfoVo().getIdentifyType())?"01":"99";
		        SysCodeDictVo sysdicVo = codeTranService.findTransCodeDictVo
                        ("IdentifyType",lossCarMainVo.getLossCarInfoVo().getIdentifyType());
				basePart.setCertiType(sysdicVo==null?"99":sysdicVo.getProperty4());
				basePart.setCertiCode(lossCarMainVo.getLossCarInfoVo().getIdentifyNo());
				//若是上海保单，号牌种类为挂、学、警、领车牌则控制末尾不能录入中文，车牌号长度不能超过6位
				if(lossCarMainVo.getLossCarInfoVo().getLicenseNo()!=null&&lossCarMainVo.getLossCarInfoVo().getLicenseNo().length()>=6&&
						("15".equals(lossCarMainVo.getLossCarInfoVo().getLicenseType())||"16".equals(lossCarMainVo.getLossCarInfoVo().getLicenseType())||
						"17".equals(lossCarMainVo.getLossCarInfoVo().getLicenseType())||"23".equals(lossCarMainVo.getLossCarInfoVo().getLicenseType())||
						"31".equals(lossCarMainVo.getLossCarInfoVo().getLicenseType())||"04".equals(lossCarMainVo.getLossCarInfoVo().getLicenseType())||
						"10".equals(lossCarMainVo.getLossCarInfoVo().getLicenseType()))){
					
					basePart.setLicenseNo(lossCarMainVo.getLossCarInfoVo().getLicenseNo().substring(0, 6));
				}else{
					basePart.setLicenseNo(lossCarMainVo.getLossCarInfoVo().getLicenseNo());
				}
				// 出险地址
				String standardAddress = HttpUtils.getSHRoadInfo(registVo.getDamageAreaCode(), registVo.getDamageAddress(), registVo.getRegistNo());
				if (StringUtils.isBlank(standardAddress)) {
					basePart.setAccidentPlace(getStandardAddress(registVo.getDamageAreaCode(), registVo.getDamageAddress()));
				} else {
					basePart.setAccidentPlace(standardAddress);
			}
		}
		}
		basePart.setProportionaClaim("0");// 是否比例赔付
		basePart.setReportNo(registNo);
		basePart.setSubrogationFlag(claimVo.getIsSubRogation());
		basePart.setRemark(registVo.getPrpLRegistExt().getRemark());
		
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

		double  underDefLoss = 0l;  //核损金额
		String isPersonInjured = "0";  //是否包含人伤
		String isProtectLoss = "0";   // 是否包含财损
		String isSingleAccident = "1";  //是否单车事故
		

		BigDecimal assesorAmount = new BigDecimal(0);
		List<SHCIVClaimCoverDataVo> claimCoverList = new ArrayList<SHCIVClaimCoverDataVo>();
		// 车辆损失赔偿情况
		//理赔同一险种的损失赔偿类型不能有重复.
		Map<String,PrpLLossItemVo> itemMap = new HashMap<String,PrpLLossItemVo>();
		for (PrpLLossItemVo itemVo : compensateVo.getPrpLLossItems()) {
			if (!"4".equals(itemVo.getPayFlag())) {
				if (!itemMap.containsKey(itemVo.getKindCode())) {
					itemMap.put(itemVo.getKindCode(),itemVo);
				} else {//已存在的把金额相加
					PrpLLossItemVo oldItemVo = itemMap.get(itemVo.getKindCode());
					oldItemVo.setSumLoss(DataUtils.NullToZero(oldItemVo.getSumLoss())
							.add(DataUtils.NullToZero(itemVo.getSumLoss())));
					oldItemVo.setSumRealPay(DataUtils.NullToZero(oldItemVo.getSumRealPay())
							.add(DataUtils.NullToZero(itemVo.getSumRealPay())));
				}
			}
			
		}
		if (!itemMap.isEmpty()) {
			for (Map.Entry<String,PrpLLossItemVo> entry : itemMap.entrySet()) {
				PrpLLossItemVo itemVo = entry.getValue();
				assesorAmount = assesorAmount.add(DataUtils.NullToZero(itemVo.getSumRealPay()));
				SHCIVClaimCoverDataVo carClaimCoverData = new SHCIVClaimCoverDataVo();
				// claimCoverData.setPolicyNo(policyNo);
				carClaimCoverData.setLiabilityRate("0".equals(checkDutyVo.getCiDutyFlag())?"5":"1");
				
				carClaimCoverData.setClaimFeeType("1".equals(itemVo.getLossType())?"8":"BZ".equals(itemVo.getKindCode())?"3":"7");
				
				if(isFeeUpdate(itemVo.getRiskCode())){
					carClaimCoverData.setCoverageType("1");
				}
				carClaimCoverData.setCoverageCode("A02");
				carClaimCoverData.setLossAmount(DataUtils.NullToZero(itemVo.getSumLoss()).doubleValue());//当前业务时间点的险种未决赔款金额
				carClaimCoverData.setClaimAmount(DataUtils.NullToZero(itemVo.getSumRealPay()).doubleValue());//赔款金额
				carClaimCoverData.setSalvagefee(0.0);
				
				//损失赔偿情况明细（多条）DETAIL_LIST（隶属于损失赔偿情况）
				List<SHCIVClaimDetailDataVo> carDetailList = new ArrayList<SHCIVClaimDetailDataVo>();
				SHCIVClaimDetailDataVo detailData = new SHCIVClaimDetailDataVo();
				detailData.setLiabilityRate("0".equals(checkDutyVo.getCiDutyFlag())?"5":"1");
				detailData.setClaimFeeType("1".equals(itemVo.getLossType())?"8":"BZ".equals(itemVo.getKindCode())?"3":"7");
				detailData.setDetailFeeType("1");// 车辆
				// detailData.setLossAmount(lossAmount);
				detailData.setClaimAmount(itemVo.getSumRealPay().doubleValue());
				carDetailList.add(detailData);
				
				carClaimCoverData.setDetailList(carDetailList);
				
				claimCoverList.add(carClaimCoverData);
			}
		}
		
		// 财产损失赔偿情况
		List<PrpLLossPropVo> propLossList = compensateVo.getPrpLLossProps();
		if (propLossList != null && propLossList.size() > 0) {
			for (PrpLLossPropVo propVo : propLossList) {// 财产
				if(!"9".equals(propVo.getPropType())){//PropType=9的不是财产
					assesorAmount = assesorAmount.add(DataUtils.NullToZero(propVo.getSumRealPay()));
					SHCIVClaimCoverDataVo propClaimCoverData = new SHCIVClaimCoverDataVo();
					// propClaimCoverData.setPolicyNo(policyNo);
					if ("0".equals(checkDutyVo.getCiDutyFlag())) {// 无责
						propClaimCoverData.setLiabilityRate("5");
						propClaimCoverData.setClaimFeeType("7");
					} else {
						propClaimCoverData.setLiabilityRate("1");
						propClaimCoverData.setClaimFeeType("3");
					}
					if(isFeeUpdate(propVo.getRiskCode())){
						propClaimCoverData.setCoverageType("1");//强制三者险
					}
					propClaimCoverData.setCoverageCode("A02");
					propClaimCoverData.setLossAmount(DataUtils.NullToZero(propVo.getSumLoss()).doubleValue());
					propClaimCoverData.setClaimAmount(DataUtils.NullToZero(propVo.getSumRealPay()).doubleValue());//财的赔款金额
//					assesorAmount += DataUtils.NullToZero(propVo.getSumRealPay()).doubleValue();
					propClaimCoverData.setSalvagefee(0.0);

					List<SHCIVClaimDetailDataVo> propDetailList = new ArrayList<SHCIVClaimDetailDataVo>();
					SHCIVClaimDetailDataVo detailData = new SHCIVClaimDetailDataVo();
					if ("0".equals(checkDutyVo.getCiDutyFlag())) {// 无责
						detailData.setClaimFeeType("7");
						detailData.setLiabilityRate("5");
					} else {
						detailData.setClaimFeeType("3");
						detailData.setLiabilityRate("1");
					}
					detailData.setDetailFeeType("9");// 其他
					// detailData.setLossAmount(lossAmount);
					// Double amount =
					// propVo.getSumRealPay()==null?0D:propVo.getSumRealPay().doubleValue();
					detailData.setClaimAmount(DataUtils.NullToZero(propVo.getSumRealPay()).doubleValue());
					propDetailList.add(detailData);

					propClaimCoverData.setDetailList(propDetailList);
					
					claimCoverList.add(propClaimCoverData);
				}
			}
		}

		// 人伤损失赔偿情况
		//理赔同一险种的损失赔偿类型不能有重复.
		Map<String,PrpLLossPersonVo>  personLossMap = new HashMap<String, PrpLLossPersonVo>();
		if(compensateVo.getPrpLLossPersons()!=null&&compensateVo.getPrpLLossPersons().size()>0){
			for(PrpLLossPersonVo personVo:compensateVo.getPrpLLossPersons()){// 人
				if(!personLossMap.containsKey(personVo.getKindCode())){
					personLossMap.put(personVo.getKindCode(), personVo);
				}else{
					//合并主表
					PrpLLossPersonVo oldPersonLoss = personLossMap.get(personVo.getKindCode());
					oldPersonLoss.setSumLoss(DataUtils.NullToZero(oldPersonLoss.getSumLoss())
							.add(DataUtils.NullToZero(personVo.getSumLoss())));  //未决估计赔款
					oldPersonLoss.setSumRealPay(DataUtils.NullToZero(oldPersonLoss.getSumRealPay())
							.add(DataUtils.NullToZero(personVo.getSumRealPay())));  //赔款金额
					
					// 合并子表
					for (PrpLLossPersonFeeVo oldPersonFeeVo : oldPersonLoss.getPrpLLossPersonFees()) {
						for (PrpLLossPersonFeeVo personFeeVo : personVo.getPrpLLossPersonFees()) {
							if ("02".equals(personFeeVo.getLossItemNo())&& "02".equals(oldPersonFeeVo.getLossItemNo())) {
								oldPersonFeeVo.setFeeLoss(DataUtils.NullToZero(oldPersonFeeVo.getFeeLoss())
										.add(DataUtils.NullToZero(personFeeVo.getFeeLoss())));
								oldPersonFeeVo.setFeeRealPay(DataUtils.NullToZero(oldPersonFeeVo.getFeeRealPay())
										.add(DataUtils.NullToZero(personFeeVo.getFeeRealPay())));
							}
							if ("03".equals(personFeeVo.getLossItemNo())&& "03".equals(oldPersonFeeVo.getLossItemNo())) {
								oldPersonFeeVo.setFeeLoss(DataUtils.NullToZero(oldPersonFeeVo.getFeeLoss())
										.add(DataUtils.NullToZero(personFeeVo.getFeeLoss())));
								oldPersonFeeVo.setFeeRealPay(DataUtils.NullToZero(oldPersonFeeVo.getFeeRealPay())
										.add(DataUtils.NullToZero(personFeeVo.getFeeRealPay())));
							}
						}
					}
					
				}
			}
		}
		
		if (!personLossMap.isEmpty()) {
			for (Map.Entry<String,PrpLLossPersonVo> entry : personLossMap.entrySet()) {
				PrpLLossPersonVo personVo = entry.getValue();// 人
				assesorAmount = assesorAmount.add(DataUtils.NullToZero(personVo.getSumRealPay()));
				SHCIVClaimCoverDataVo persClaimCoverData = new SHCIVClaimCoverDataVo();
				// persClaimCoverData.setPolicyNo(policyNo);
				if("0".equals(checkDutyVo.getCiDutyFlag())){// 无责
					persClaimCoverData.setLiabilityRate("5");
					persClaimCoverData.setClaimFeeType("7");
				}else{
					persClaimCoverData.setLiabilityRate("1");
					persClaimCoverData.setClaimFeeType("3");
				}
				if(isFeeUpdate(personVo.getRiskCode())){
//					persClaimCoverData.setCoverageType("1");//强制三者险
				}
				persClaimCoverData.setCoverageType("1");//强制三者险
				persClaimCoverData.setCoverageCode("A02");
				persClaimCoverData.setLossAmount(DataUtils.NullToZero(personVo.getSumLoss()).doubleValue());// 人伤的未决
				persClaimCoverData.setClaimAmount(DataUtils.NullToZero(personVo.getSumRealPay()).doubleValue());//人伤赔款金额
				persClaimCoverData.setSalvagefee(0.0);

				List<SHCIVClaimDetailDataVo> persDetailList = new ArrayList<SHCIVClaimDetailDataVo>();
				for(PrpLLossPersonFeeVo feeVo:personVo.getPrpLLossPersonFees()){
					SHCIVClaimDetailDataVo detailData = new SHCIVClaimDetailDataVo();
					if("0".equals(checkDutyVo.getCiDutyFlag())){// 无责
						detailData.setClaimFeeType("02".equals(feeVo.getLossItemNo()) ? "1" : "2");
						detailData.setLiabilityRate("5");
					}else{
						detailData.setClaimFeeType("02".equals(feeVo.getLossItemNo()) ? "5" : "6");
						detailData.setLiabilityRate("1");
					}
					detailData.setDetailFeeType(feeVo.getKindCode());// 其他
					detailData.setLossAmount(DataUtils.NullToZero(feeVo.getFeeLoss()).doubleValue());
					detailData.setClaimAmount(DataUtils.NullToZero(feeVo.getFeeRealPay()).doubleValue());
					persDetailList.add(detailData);
				}

				persClaimCoverData.setDetailList(persDetailList);
				claimCoverList.add(persClaimCoverData);
			}
		}
		
		basePart.setAssesorAmount(DataUtils.NullToZero(assesorAmount).doubleValue());
		
		//没有险种信息，0结案的情况 （5502010088_总赔款金额和各险种赔款金额之和不一致）
		if(claimCoverList == null || claimCoverList.size() == 0){
			SHCIVClaimCoverDataVo claimCoverData = new SHCIVClaimCoverDataVo();
			claimCoverData.setLiabilityRate("5");
			claimCoverData.setClaimFeeType("8");
			claimCoverData.setCoverageType("1");//强制三者险
			claimCoverData.setCoverageCode("A02");//A02
			claimCoverData.setLossAmount(0.0);
			claimCoverData.setClaimAmount(0.0);
			claimCoverData.setSalvagefee(0.0);
			
			List<SHCIVClaimDetailDataVo> otherDetailList = new ArrayList<SHCIVClaimDetailDataVo>();
			SHCIVClaimDetailDataVo detailData = new SHCIVClaimDetailDataVo();
			detailData.setClaimFeeType("8");
			detailData.setLiabilityRate("5");
			detailData.setDetailFeeType("9");// 其他
			detailData.setLossAmount(0.0);
			detailData.setClaimAmount(0.0);
			otherDetailList.add(detailData);
			
			claimCoverData.setDetailList(otherDetailList);
			claimCoverList.add(claimCoverData);
		}

		// 人员损失情况
		List<SHCIVClaimPersonDataVo> personList = new ArrayList<SHCIVClaimPersonDataVo>();
		if(traceMainVos!=null&&traceMainVos.size()>0){
			for (PrpLDlossPersTraceMainVo persTraceMainVo : traceMainVos) {
				List<PrpLDlossPersTraceVo> persTraceVoList = persTraceMainVo.getPrpLDlossPersTraces();
				if(persTraceVoList != null && !persTraceVoList.isEmpty()){
					isPersonInjured = "1";
					for (PrpLDlossPersTraceVo persTraceVo : persTraceMainVo.getPrpLDlossPersTraces()) {
						SHCIVClaimPersonDataVo personData = new SHCIVClaimPersonDataVo();
						personData.setPersonName(persTraceVo.getPrpLDlossPersInjured().getPersonName());
						if ("1".equals(persTraceVo.getPrpLDlossPersInjured().getCertiType())) {
							personData.setPersonId(persTraceVo.getPrpLDlossPersInjured().getCertiCode());
						}
						personData.setAge(persTraceVo.getPrpLDlossPersInjured().getPersonAge().intValue());
						Double lossAmount = DataUtils.NullToZero(persTraceVo.getSumdefLoss()).doubleValue();
						for(PrpLLossPersonVo personVo : compensateVo.getPrpLLossPersons()){
							if(personVo.getPersonId().longValue() == persTraceVo.getId().longValue()){
								lossAmount = DataUtils.NullToZero(personVo.getSumRealPay()).doubleValue();
							}
						}
						personData.setLossAmount(lossAmount);
						personData.setMainThird("1".equals(persTraceVo.getPrpLDlossPersInjured().getSerialNo()+"") ? "1" : "0");
						personData.setSurveyType("1".equals(checkVo.getPrpLCheckTask().getFirstAddressFlag()) ? "1" : "3");
						personData.setSurveyName(checkVo.getPrpLCheckTask().getChecker());
						personData.setSurveyStartTime(checkVo.getCreateTime());
						personData.setSurveyEndTime(checkVo.getChkSubmitTime());
						personData.setSurveyPlace(checkVo.getPrpLCheckTask().getCheckAddress());
						personData.setSurveyDes(checkVo.getPrpLCheckTask().getContexts());
//						personData.setEstimateName(persTraceMainVo.getCreateUser());
						String codeCName = codeTranService.findCodeName("UserCodeSH",persTraceMainVo.getCreateUser());
						if(StringUtils.isEmpty(codeCName)){
							codeCName = codeTranService.findCodeName("UserCodeSH",persTraceMainVo.getOperatorCode());
						}
						personData.setEstimateName(StringUtils.isNotEmpty(codeCName) ? codeCName : "GSU0000001");
						personData.setEstimateStartTime(persTraceMainVo.getPlfSubTime());
						personData.setEstimateEndTime(persTraceMainVo.getUnderwriteEndDate());
						personData.setAssesorName(persTraceMainVo.getUnderwriteName());
						// personData.setAssesorStartTime(assesorStartTime);
						// personData.setAssesorEndTime(assesorEndTime);
						// personData.setInjurytype(injurytype);
						// personData.setInjurylevel(injurylevel);
						personData.setMedicaltype(persTraceVo.getPrpLDlossPersInjured().getTreatSituation());
						personData.setUnderwritename(persTraceMainVo.getUnderwriteName());
						personData.setUnderwritecode(persTraceMainVo.getUnderwriteCode());
						// personData.setEstimateaddr(estimateaddr);
						// personData.setAddmissiontime(addmissiontime);
//						Double sumDef = persTraceVo.getSumVeriDefloss()==null?0D:persTraceVo.getSumVeriDefloss().doubleValue();
						personData.setUnderdefloss(DataUtils.NullToZero(persTraceVo.getSumVeriDefloss()).doubleValue());
						
						//人员损失情况
						personData.setCheckerCode(checkVo.getPrpLCheckTask().getCheckerCode());
						personData.setCheckerCertiCode(checkVo.getPrpLCheckTask().getCheckerIdfNo());
						if(StringUtils.isBlank(persTraceMainVo.getOperatorCertiCode())){
							personData.setEstimateCertiCode(persTraceMainVo.getPlfCertiCode());
						} else {
							personData.setEstimateCertiCode(persTraceMainVo.getOperatorCertiCode());
						}
						personData.setDeathTime(persTraceVo.getPrpLDlossPersInjured().getDeathTime());
						personData.setCountryInjuryType(persTraceVo.getPrpLDlossPersInjured().getWoundCode());
						
						underDefLoss += DataUtils.NullToZero(persTraceVo.getSumVeriDefloss()).doubleValue();
						// 人员损失费用明细
						List<SHCIVClaimPersLossPartDataVo> lossPartList = new ArrayList<SHCIVClaimPersLossPartDataVo>();
						for (PrpLDlossPersTraceFeeVo feeVo : persTraceVo.getPrpLDlossPersTraceFees()) {
							SHCIVClaimPersLossPartDataVo lossPartData = new SHCIVClaimPersLossPartDataVo();
							String shFeeType = feeVo.getFeeTypeCode().length()==1?"0"+feeVo.getFeeTypeCode():feeVo.getFeeTypeCode();
							lossPartData.setFeeType(shFeeType);// 暂时使用全国平台的代码
							lossPartData.setLossAmount(DataUtils.NullToZero(feeVo.getVeriDefloss()).doubleValue());
							
							lossPartData.setUnderDefLoss(DataUtils.NullToZero(feeVo.getVeriDefloss()).doubleValue());
							lossPartList.add(lossPartData);
						}
						personData.setLossPartList(lossPartList);
						// 人员受伤部位
						List<SHCIVClaimInjuryDataVo> injuryList = new ArrayList<SHCIVClaimInjuryDataVo>();
						for (PrpLDlossPersExtVo extVo : persTraceVo.getPrpLDlossPersInjured().getPrpLDlossPersExts()) {
							SHCIVClaimInjuryDataVo injuryData = new SHCIVClaimInjuryDataVo();
							injuryData.setInjurypart(extVo.getInjuredPart());
							//injuryData.setInjuryLevelCode(StringUtils.isBlank(extVo.getWoundGrade())?"10":extVo.getWoundGrade());
							String injuryLevelCode =extVo.getWoundGrade();
							if(StringUtils.isBlank(extVo.getWoundGrade()) ||"10".equals(injuryLevelCode)){
								injuryLevelCode ="10";
							}else{
								injuryLevelCode ="0" + injuryLevelCode;
							}
							injuryData.setInjuryLevelCode(injuryLevelCode); //伤残程度代码
							injuryList.add(injuryData);
						}
						personData.setInjuryList(injuryList);
						// 人员治疗机构
						if("2".equals(persTraceVo.getPrpLDlossPersInjured().getTreatSituation())){
						    List<SHCIVClaimHospitalInfoDataVo> hospitalInfoList = new ArrayList<SHCIVClaimHospitalInfoDataVo>();
	                        for (PrpLDlossPersHospitalVo hospitalVo : persTraceVo.getPrpLDlossPersInjured().getPrpLDlossPersHospitals()) {
	                            SHCIVClaimHospitalInfoDataVo hospitalInfoData = new SHCIVClaimHospitalInfoDataVo();
	                            hospitalInfoData.setHospitalname(hospitalVo.getHospitalName());
	                            hospitalInfoData.setHospitalFactoryCertiCode(hospitalVo.getHospitalCode());
	                            hospitalInfoList.add(hospitalInfoData);
	                        }
	                        personData.setHospitalInfoList(hospitalInfoList);
						}
						
						
						  //伤残鉴定列表
						if("02".equals(persTraceVo.getPrpLDlossPersInjured().getWoundCode())){
							List<SHCIVClaimInjuryIdentifyInfoDataVo> ideInfoDataList = new ArrayList<SHCIVClaimInjuryIdentifyInfoDataVo>();
							SHCIVClaimInjuryIdentifyInfoDataVo injuryIdentifyInfoDataVo = new SHCIVClaimInjuryIdentifyInfoDataVo();
							injuryIdentifyInfoDataVo.setInjuryIdentifyName(persTraceVo.getPrpLDlossPersInjured().getChkComName());
							injuryIdentifyInfoDataVo.setInjuryIdentifyCertiCode(persTraceVo.getPrpLDlossPersInjured().getChkComCode());
							ideInfoDataList.add(injuryIdentifyInfoDataVo);
							personData.setInjuryIdentifyInfoData(ideInfoDataList);
						}
						personList.add(personData);
					}
				}
			}
		}
		int carNum = 0;
		// 车辆损失情况
		List<SHCIVClaimVehicleDataVo> vehicleList = new ArrayList<SHCIVClaimVehicleDataVo>();
		for (PrpLDlossCarMainVo lossCarMainVo : lossCarMainVos) {
			//车辆（定损方式为无损失的不上传平台）定损注销不上传平台
			if(CodeConstants.CetainLossType.DEFLOSS_NULL.equals(lossCarMainVo.getCetainLossType()) ||
					CodeConstants.VeriFlag.CANCEL.equals(lossCarMainVo.getUnderWriteFlag())){
				continue;
			}
			//如果车辆核损金额为0，则不上传平台，要不然会影响上传损失率
			if(DataUtils.NullToZero(lossCarMainVo.getSumVeriLossFee()).compareTo(BigDecimal.ZERO) == 0){
				continue;
			}
			carNum++;
			SHCIVClaimVehicleDataVo vehicleData = new SHCIVClaimVehicleDataVo();
			//若是上海保单，号牌种类为挂、学、警、领车牌则控制末尾不能录入中文，车牌号长度不能超过6位
			if(lossCarMainVo.getLossCarInfoVo().getLicenseNo()!=null&&lossCarMainVo.getLossCarInfoVo().getLicenseNo().length()>=6&&
					("15".equals(lossCarMainVo.getLossCarInfoVo().getLicenseType())||"16".equals(lossCarMainVo.getLossCarInfoVo().getLicenseType())||
					"17".equals(lossCarMainVo.getLossCarInfoVo().getLicenseType())||"23".equals(lossCarMainVo.getLossCarInfoVo().getLicenseType())||
					"31".equals(lossCarMainVo.getLossCarInfoVo().getLicenseType())||"04".equals(lossCarMainVo.getLossCarInfoVo().getLicenseType())||
					"10".equals(lossCarMainVo.getLossCarInfoVo().getLicenseType()))){
				
				vehicleData.setCarMark(lossCarMainVo.getLossCarInfoVo().getLicenseNo().substring(0, 6));
			}else{
				vehicleData.setCarMark(lossCarMainVo.getLossCarInfoVo().getLicenseNo());
			}
			if(lossCarMainVo.getLossCarInfoVo().getLicenseType()==null){
				vehicleData.setVehicleType("02");
			}else{
				vehicleData.setVehicleType(lossCarMainVo.getLossCarInfoVo().getLicenseType());
			}
			
			Double lossAmount = DataUtils.NullToZero(lossCarMainVo.getSumVeriLossFee()).doubleValue();
			for(PrpLLossItemVo itemVo : compensateVo.getPrpLLossItems()){
				if(itemVo.getDlossId().longValue() == lossCarMainVo.getId().longValue()){
					lossAmount = DataUtils.NullToZero(itemVo.getSumRealPay()).doubleValue();
				}
			}
			vehicleData.setLossAmount(lossAmount);
			vehicleData.setMainThird("1".equals(lossCarMainVo.getDeflossCarType()) ? "1" : "0");
			vehicleData.setRobber("03".equals(lossCarMainVo.getCetainLossType()) ? "1": "0");
			vehicleData.setSurveyType("1".equals(checkVo.getPrpLCheckTask().getFirstAddressFlag()) ? "1" : "3");
			vehicleData.setSurveyName(checkVo.getPrpLCheckTask().getChecker());
			vehicleData.setSurveyStartTime(checkVo.getCreateTime());
			vehicleData.setSurveyEndTime(checkVo.getChkSubmitTime());
			vehicleData.setSurveyPlace(checkVo.getPrpLCheckTask().getCheckAddress());
			vehicleData.setSurveyDes(checkVo.getPrpLCheckTask().getContexts());
//			vehicleData.setEstimateName(lossCarMainVo.getHandlerCode());
			String codeCName = codeTranService.findCodeName("UserCodeSH",lossCarMainVo.getHandlerCode());
			if(StringUtils.isEmpty(codeCName)){
				codeCName = codeTranService.findCodeName("UserCodeSH",lossCarMainVo.getCreateUser());
			}
			vehicleData.setEstimateName(StringUtils.isNotEmpty(codeCName) ? codeCName : "GSU0000001");
//			vehicleData.setEstimateStartTime(lossCarMainVo.getDeflossDate());
//			vehicleData.setEstimateEndTime(lossCarMainVo.getDefEndDate());
			vehicleData.setAssesorName(lossCarMainVo.getUnderWriteName());
			// vehicleData.setAssesorStartTime(assesorStartTime);
			// vehicleData.setAssesorEndTime(assesorEndTime);
			vehicleData.setRemnant(0d);
			vehicleData.setChargeFee(0d);
            //控制精度
			BigDecimal totalWorkingHour = new BigDecimal("0");
			String changeOrRepairPart = "0";
			if (lossCarMainVo.getPrpLDlossCarRepairs() != null && lossCarMainVo.getPrpLDlossCarRepairs().size() > 0) {
				for (PrpLDlossCarRepairVo repairVo : lossCarMainVo.getPrpLDlossCarRepairs()) {
					totalWorkingHour= totalWorkingHour.add(DataUtils.NullToZero(repairVo.getSumVeriLoss()));
				}
				vehicleData.setTotalWorkingHour(totalWorkingHour.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
				changeOrRepairPart = "1";
			}
			if (lossCarMainVo.getPrpLDlossCarComps() != null && lossCarMainVo.getPrpLDlossCarComps().size() > 0) {
				changeOrRepairPart = "1";
			}
			vehicleData.setChangeOrRepairPart(changeOrRepairPart);
			vehicleData.setJyVehicleCode(lossCarMainVo.getLossCarInfoVo().getModelCode());
			vehicleData.setClaimVehicleName(lossCarMainVo.getLossCarInfoVo().getModelName());
			vehicleData.setClaimVehicleCode(lossCarMainVo.getLossCarInfoVo().getModelCode());
			String factory = lossCarMainVo.getRepairFactoryName();
			if(StringUtils.isEmpty(factory)){
				factory = "修理厂为空";
			}
			vehicleData.setMadeFactory(factory);
			vehicleData.setVehicleBrandCode(lossCarMainVo.getLossCarInfoVo().getBrandCode());
			vehicleData.setVehicleCatenaCode(lossCarMainVo.getLossCarInfoVo().getSeriCode());
			vehicleData.setVehicleGroupCode(lossCarMainVo.getLossCarInfoVo().getGroupCode());
			
			String sltCode = "";
			if("001".equals(lossCarMainVo.getRepairFactoryType())){
				sltCode = "01";
			}else if("002".equals(lossCarMainVo.getRepairFactoryType())){
				sltCode = "02";
			}else{
				sltCode = "99";
			}
			vehicleData.setPriceSltCode(sltCode);
			vehicleData.setDefineFlag("0");
			vehicleData.setVin(lossCarMainVo.getLossCarInfoVo().getVinNo());
			vehicleData.setEngineno(lossCarMainVo.getLossCarInfoVo().getEngineNo());
			vehicleData.setModel(lossCarMainVo.getLossCarInfoVo().getModelCode());
			vehicleData.setDrivername(lossCarMainVo.getLossCarInfoVo().getDriveName());
		//	String certiType = "1".equals(lossCarMainVo.getLossCarInfoVo().getIdentifyType())?"01":"99";
			  SysCodeDictVo sysdicVo = codeTranService.findTransCodeDictVo
                      ("IdentifyType",lossCarMainVo.getLossCarInfoVo().getIdentifyType());			
			vehicleData.setCertitype(sysdicVo==null?"99":sysdicVo.getProperty4());
			vehicleData.setCerticode(lossCarMainVo.getLossCarInfoVo().getIdentifyNo());
			vehicleData.setDriverlicenseno(lossCarMainVo.getLossCarInfoVo().getDrivingLicenseNo());
			vehicleData.setTemporaryflag("0");
			
			vehicleData.setCheckerCode(checkVo.getPrpLCheckTask().getCheckerCode());
			vehicleData.setCheckerCertiCode(checkVo.getPrpLCheckTask().getCheckerIdfNo());
			vehicleData.setUnderWriteCode(lossCarMainVo.getUnderWriteCode());
			vehicleData.setUnderWriteCertiCode(lossCarMainVo.getUnderWiteIdNo());
			vehicleData.setUnderDefLoss(DataUtils.NullToZero(lossCarMainVo.getSumVeriLossFee()).doubleValue());
			
			underDefLoss += DataUtils.NullToZero(lossCarMainVo.getSumVeriLossFee()).doubleValue();
			
			// 车辆损失部位
			List<SHCIVClaimCarLossPartDataVo> carLossPartList = new ArrayList<SHCIVClaimCarLossPartDataVo>();
			String[] lossPartArr = lossCarMainVo.getLossPart().split(",");
			for (int i = 0; i < lossPartArr.length; i++) {
				String lossPart = lossPartArr[i];
				SHCIVClaimCarLossPartDataVo carLossPartData = new SHCIVClaimCarLossPartDataVo();
				SysCodeDictVo sysVo = codeTranService.findTransCodeDictVo("LossPart",lossPart);
				carLossPartData.setLossPart(sysVo==null?"01":sysVo.getProperty3());
				carLossPartList.add(carLossPartData);
			}
			vehicleData.setLossPartList(carLossPartList);
			
			// 车辆配件明细
			List<SHCIVClaimFittingDataVo> fittingList = new ArrayList<SHCIVClaimFittingDataVo>();
			for (PrpLDlossCarCompVo compVo : lossCarMainVo.getPrpLDlossCarComps()) {
				SHCIVClaimFittingDataVo fittingData = new SHCIVClaimFittingDataVo();
				fittingData.setChangePartName(StringUtils.isEmpty(compVo.getCompName()) ? "空" : compVo.getCompName());
				fittingData.setChangePartNum(compVo.getQuantity()==null ? 0 : compVo.getQuantity());
				fittingData.setChangePartFee(DataUtils.NullToZero(compVo.getVeriMaterFee()).doubleValue());
				fittingData.setChangePartTime(DataUtils.NullToZero(compVo.getManHour()).doubleValue());
				fittingData.setChangePartManpowerFee(DataUtils.NullToZero(compVo.getVeriManHourFee()).doubleValue());
				fittingData.setJyPartCode(compVo.getCompCode());
				fittingData.setOemPartCode(StringUtils.isEmpty(compVo.getOriginalId()) ? "无" : compVo.getOriginalId());
				fittingData.setDefineFlag("1".equals(compVo.getSelfConfigFlag()) ? "1": "0");
				fittingData.setSubjionFlag("0");// 默认全量上次
				fittingData.setPriceType("02");
				fittingData.setQualityType("05");
				fittingList.add(fittingData);
			}
			
			if("1".equals(changeOrRepairPart)){
				List<SHCIVClaimRepairDataVo> repartDataVoList = new ArrayList<SHCIVClaimRepairDataVo>();
				SHCIVClaimRepairDataVo repartVo = new SHCIVClaimRepairDataVo();
				repartVo.setRepairOrg("JY00008782");
				repartDataVoList.add(repartVo);
				vehicleData.setRepairList(repartDataVoList);
			}
			if("1".equals(changeOrRepairPart)){
				List<SHCIVClaimActuralRepairDataVo> acturalRepairList = new ArrayList<SHCIVClaimActuralRepairDataVo>();
				SHCIVClaimActuralRepairDataVo repairDataVo = new SHCIVClaimActuralRepairDataVo();
				repairDataVo.setActuralrepairorg("JY00008782");
				acturalRepairList.add(repairDataVo);
				vehicleData.setActuralRepairList(acturalRepairList);
			}
			if(lossCarMainVo.getPrpLDlossCarRepairs()!=null&&lossCarMainVo.getPrpLDlossCarRepairs().size()>0){
				for (PrpLDlossCarRepairVo repairVo : lossCarMainVo.getPrpLDlossCarRepairs()) {
					SHCIVClaimFittingDataVo fittingData = new SHCIVClaimFittingDataVo();
					 fittingData.setRepairPartName(StringUtils.isEmpty(repairVo.getRepairName()) ? "空" : repairVo.getRepairName());
					 fittingData.setRepairPartNum(lossCarMainVo.getPrpLDlossCarRepairs().size());
					 fittingData.setRepairPartFee(repairVo.getSumDefLoss()==null?0.0:repairVo.getSumDefLoss().doubleValue());
					 fittingData.setRepairPartTime(repairVo.getManHour()==null?0.0:repairVo.getManHour().doubleValue());
					 fittingData.setRepairPartManpowerFee(repairVo.getManHourFee()==null?0.0:repairVo.getManHourFee().doubleValue());
					 fittingData.setRepairMethord("09");
					 fittingData.setJyPartCode(repairVo.getRepairCode());
					 fittingData.setOemPartCode(StringUtils.isEmpty(repairVo.getRepairId()) ? "无" : repairVo.getRepairId());
					 fittingData.setDefineFlag("1".equals(repairVo.getSelfConfigFlag())?"1":"0");
					 fittingData.setSubjionFlag("0");//默认全量上次
					 fittingData.setPriceType("02");
					 fittingData.setQualityType("05");
					fittingList.add(fittingData);
				}
			}
			
			vehicleData.setFittingList(fittingList);
			
			vehicleList.add(vehicleData);
		}
		
		//物损
		List<SHCIVClaimObjDataVo> objDataVoList = new ArrayList<SHCIVClaimObjDataVo>();
		if(propVoList!=null&&propVoList.size()>0){
			for(PrpLdlossPropMainVo propVo : propVoList){
				if(propVo.getPrpLdlossPropFees()!=null&&propVo.getPrpLdlossPropFees().size()>0){
					isProtectLoss = "1";
					for(PrpLdlossPropFeeVo feeVo : propVo.getPrpLdlossPropFees()){
						SHCIVClaimObjDataVo objDataVo = new SHCIVClaimObjDataVo();
						objDataVo.setObjectDesc(feeVo.getLossItemName());
						objDataVo.setLossNum(DataUtils.NullToZero(feeVo.getLossQuantity()).intValue());
						objDataVo.setLossAmount(DataUtils.NullToZero(feeVo.getUnitPrice()).doubleValue());
						objDataVo.setMainThird("1".equals(propVo.getLossType())?"1":"0");
						objDataVo.setSurveyType("1");
						objDataVo.setSurveyName(propVo.getHandlerName());
						objDataVo.setSurveyStartTime(propVo.getCreateTime());
						objDataVo.setSurveyEndTime(propVo.getUpdateTime());
						objDataVo.setSurveyPlace("");
						objDataVo.setSurveyDes(propVo.getVerirescueOpinion());
						String codeCName = codeTranService.findCodeName("UserCodeSH",propVo.getHandlerCode());
						if(StringUtils.isEmpty(codeCName)){
							codeCName = codeTranService.findCodeName("UserCodeSH",propVo.getCreateUser());
						}
						objDataVo.setEstimateName(StringUtils.isNotEmpty(codeCName) ? codeCName : "GSU0000001");
//						objDataVo.setEstimateName(propVo.getHandlerCode());
						objDataVo.setEstimateStartTime(propVo.getUpdateTime());
						objDataVo.setEstimateEndTime(propVo.getUpdateTime());
						objDataVo.setAssesorName(propVo.getUnderWriteCode());
						objDataVo.setAssesorStartTime(propVo.getUpdateTime());
						objDataVo.setAssesorEndTime(propVo.getUnderWriteEndDate());
						
						String protectProperty = "1";
						if("1".equals(propVo.getLossType())){
							protectProperty = "1";  //本车财产
						}else{
							protectProperty = "2";  // 车外财产
						}
						objDataVo.setProtectProperty(protectProperty);
						objDataVo.setCheckerCode(checkVo.getPrpLCheckTask().getCheckerCode());
						objDataVo.setCheckerCertiCode(checkVo.getPrpLCheckTask().getCheckerIdfNo());
						objDataVo.setUnderWriteCode(propVo.getUnderWriteCode());
						objDataVo.setUnderWriteCertiCode(propVo.getUnderWriteIdCard());
						objDataVo.setUnderDefLoss(DataUtils.NullToZero(propVo.getSumVeriLoss()).doubleValue());
						underDefLoss += DataUtils.NullToZero(propVo.getSumVeriLoss()).doubleValue();
						
						objDataVoList.add(objDataVo);
					}
				}
			}
		}
		if(carNum > 1){
			isSingleAccident = "0";  //是否单车事故
		}
		basePart.setIsSingleAccident(isSingleAccident);
		basePart.setIsPersonInjured(isPersonInjured);
		basePart.setIsProtectLoss(isProtectLoss);
		basePart.setUnderDefLoss((double)Math.round(underDefLoss*100)/100);
		bodyVo.setBasePart(basePart);
		bodyVo.setClaimCoverList(claimCoverList);
		bodyVo.setPersonList(personList);
		bodyVo.setVehicleList(vehicleList);
		bodyVo.setObjList(objDataVoList);
		bodyVo.setDisputeList(null);
		bodyVo.setSubrogationList(null);
		logger.info("sendVClaimCIToSHPlatform assemble xml end，registNo={}, cost time {} ms ",compensateVo.getRegistNo(),System.currentTimeMillis()-currentDate);
		controller.callPlatform(bodyVo,platformTaskVo);
	}
	
	
	/**
	 * 送核赔到上海平台
	 * @param compeNo--计算书号
	 */
	public void sendVClaimBIToSHPlatform(PrpLCompensateVo compensateVo,CiClaimPlatformTaskVo platformTaskVo) {
//		PrpLCompensateVo compensateVo = compensateService.findCompByPK(compeNo);
		Long currentDate = System.currentTimeMillis();
		logger.info("sendVClaimBIToSHPlatform begin，registNo={}",compensateVo.getRegistNo());
		String registNo = compensateVo.getRegistNo();
		PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
		// 联共保 从共，从联不交互平台
		if(CodeConstants.GBFlag.MINORCOMMON.equals(registVo.getIsGBFlag())||CodeConstants.GBFlag.MINORRELATION.equals(registVo.getIsGBFlag())){
			return;
		}
		String comCode = policyViewService.findPolicyComCode(registNo, "12");
		CiClaimPlatformLogVo logVo = platformLogService.findLogByBussNo(
				RequestType.RegistInfoBI_SH.getCode(),registNo,comCode);
		if (logVo == null) {
			logger.info("商业理算核赔上传上海平台失败！该案件理赔编码不存在或报案未成功上传平台！");
		}
//		PrpLCompensateVo compeVo = compensateService.findCompByClaimNo(
//				compensateVo.getClaimNo(), "N");

		List<PrpLDlossCarMainVo> lossCarMainVos = lossCarService.findLossCarMainByRegistNo(registNo);
//		PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
//		PrpLCheckDutyVo checkDutyVo = checkTaskService.findCheckDuty(registNo,1);
		PrpLClaimVo claimVo = claimTaskService.findClaimVoByClaimNo(compensateVo.getClaimNo());

		PrpLCheckVo checkVo = checkTaskService.findCheckVoByRegistNo(registNo);
		List<PrpLDlossPersTraceMainVo> traceMainVos = persTraceDubboService.findPersTraceMainVoList(registNo);
		
		List<PrpLdlossPropMainVo> propVoList = propTaskService.findPropMainListByRegistNo(registNo);

		//送平台
		PlatformController controller = PlatformFactory.getInstance(comCode,RequestType.VClaimBI_SH);

		SHBIVClaimReqBodyVo bodyVo = new SHBIVClaimReqBodyVo();

		SHBIVClaimBasePartVo basePart = new SHBIVClaimBasePartVo();
		basePart.setClaimCode(logVo==null?"":logVo.getClaimSeqNo());
		basePart.setNumerationStartTime(compensateVo.getCreateTime());
		basePart.setNumerationEndTime(compensateVo.getUpdateTime());
		// basePart.setAssesorStartTime(assesorStartTime);
		basePart.setAssesorEndTime(compensateVo.getUnderwriteDate());
		basePart.setOtherAmount(DataUtils.NullToZero(compensateVo.getSumFee()).doubleValue());
		basePart.setAssesorDes("1");
		PrpLCertifyMainVo certifyMainVo = certifyPubService.findPrpLCertifyMainVoByRegistNo(registNo);
		if(certifyMainVo != null && "1".equals(certifyMainVo.getIsFraud())){
			basePart.setAssesorDes("3"); //核赔意见 拒赔
		}
		int carNum = 0;
		String isPersonInjured = "0"; 
		String isProtectLoss = "0";
		double underDefLoss = 0L;
		
//		Double assesorAmount = 0.0;//计算总核赔金额（总赔款金额和各险种赔款金额之和要一致）
		BigDecimal amount = new BigDecimal(0);
//		basePart.setAssesorAmount(DataUtils.NullToZero(compensateVo.getSumAmt()).doubleValue());

		for (PrpLDlossCarMainVo lossCarMainVo : lossCarMainVos) {
			if ("1".equals(lossCarMainVo.getDeflossCarType())) {
				basePart.setDriverName(lossCarMainVo.getLossCarInfoVo().getDriveName());
				//String certiType = "1".equals(lossCarMainVo.getLossCarInfoVo().getIdentifyType())?"01":"99";
				  SysCodeDictVo sysdicVo = codeTranService.findTransCodeDictVo
	                      ("IdentifyType",lossCarMainVo.getLossCarInfoVo().getIdentifyType());  
				basePart.setCertiType(sysdicVo==null?"99":sysdicVo.getProperty4());
				basePart.setCertiCode(lossCarMainVo.getLossCarInfoVo().getIdentifyNo());
				//若是上海保单，号牌种类为挂、学、警、领车牌则控制末尾不能录入中文，车牌号长度不能超过6位
				if(lossCarMainVo.getLossCarInfoVo().getLicenseNo()!=null&&lossCarMainVo.getLossCarInfoVo().getLicenseNo().length()>=6&&
						("15".equals(lossCarMainVo.getLossCarInfoVo().getLicenseType())||"16".equals(lossCarMainVo.getLossCarInfoVo().getLicenseType())||
						"17".equals(lossCarMainVo.getLossCarInfoVo().getLicenseType())||"23".equals(lossCarMainVo.getLossCarInfoVo().getLicenseType())||
						"31".equals(lossCarMainVo.getLossCarInfoVo().getLicenseType())||"04".equals(lossCarMainVo.getLossCarInfoVo().getLicenseType())||
						"10".equals(lossCarMainVo.getLossCarInfoVo().getLicenseType()))){
					
					basePart.setLicenseNo(lossCarMainVo.getLossCarInfoVo().getLicenseNo().substring(0, 6));
				}else{
					basePart.setLicenseNo(lossCarMainVo.getLossCarInfoVo().getLicenseNo());
				}

				// 出险地址
				String standardAddress = HttpUtils.getSHRoadInfo(registVo.getDamageAreaCode(), registVo.getDamageAddress(), registVo.getRegistNo());
				if (StringUtils.isBlank(standardAddress)) {
					basePart.setAccidentPlace(getStandardAddress(registVo.getDamageAreaCode(), registVo.getDamageAddress()));
				} else {
					basePart.setAccidentPlace(standardAddress);
			}
		}
		}
		basePart.setProportionaClaim("0");// 是否比例赔付
		// basePart.setAccidentPlaceMark(accidentPlaceMark);
		basePart.setReportNo(registNo);
		basePart.setSubrogationFlag(claimVo.getIsSubRogation());
		basePart.setRemark(registVo.getPrpLRegistExt().getRemark());
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

		List<SHBIVClaimCoverDataVo> claimCoverList = new ArrayList<SHBIVClaimCoverDataVo>();
		//车
		if(compensateVo.getPrpLLossItems()!=null&&compensateVo.getPrpLLossItems().size()>0){
			for(PrpLLossItemVo itemVo : compensateVo.getPrpLLossItems()){
				amount = amount.add(DataUtils.NullToZero(itemVo.getSumRealPay()));
				// 车辆损失赔偿情况
				SHBIVClaimCoverDataVo carClaimCoverData = new SHBIVClaimCoverDataVo();
				// claimCoverData.setPolicyNo(policyNo);
				String duty = itemVo.getDutyRate()==null ? "5" : 100D==itemVo.getDutyRate().doubleValue() ? "1" : "3";
				carClaimCoverData.setLiabilityRate(duty);
				carClaimCoverData.setClaimFeeType("1");
				String coverageCode = kindCodeTranService.findTransCiCode("22",itemVo.getRiskCode(),"CovergeCodeSH",itemVo.getKindCode());
				carClaimCoverData.setCoverageCode(coverageCode);
				if(isFeeUpdate(itemVo.getRiskCode())){
					String coverageType = "1".equals(itemVo.getItemId()) ? "3" : "2";// 本车3(商业车损险)，其他车2(商业三者险)
					if(coverageCode.startsWith("F")){
						carClaimCoverData.setCoverageType("9");
					}else{
						carClaimCoverData.setCoverageType(coverageType);
					}
				}

				carClaimCoverData.setComCoverageCode(itemVo.getKindCode());
				carClaimCoverData.setLossAmount(DataUtils.NullToZero(itemVo.getSumLoss()).doubleValue());
				carClaimCoverData.setClaimAmount(DataUtils.NullToZero(itemVo.getSumRealPay()).doubleValue());

//					assesorAmount += DataUtils.NullToZero(itemVo.getSumRealPay()).doubleValue();
				carClaimCoverData.setSalvagefee(DataUtils.NullToZero(itemVo.getRescueFee()).doubleValue());

				if("A1".equals(itemVo.getKindCode())){
					String sign = "0";// sign附加险是否有新增设备险，0无，1有
					if(compensateVo.getPrpLLossProps()!=null&&compensateVo.getPrpLLossProps().size()>0){
						for(PrpLLossPropVo propVo:compensateVo.getPrpLLossProps()){
							if("9".equals(propVo.getPropType())){
								if("X3".equals(propVo.getKindCode())){
									sign = "1";
									break;
								}
							}
						}
					}
					if("0".equals(sign)){
						carClaimCoverData.setIsDeviceItem("0");
					}else{
						carClaimCoverData.setIsDeviceItem("1");
					}
				}

				claimCoverList.add(carClaimCoverData);
			}
		}
		// 财
		if (compensateVo.getPrpLLossProps() != null && compensateVo.getPrpLLossProps().size() > 0) {
			for (PrpLLossPropVo propVo : compensateVo.getPrpLLossProps()) {
				if(!"9".equals(propVo.getPropType())){//proptype=9的不是财产
					if(CodeConstants.KINDCODE.KINDCODE_RS.equals(propVo.getKindCode()) || CodeConstants.KINDCODE.KINDCODE_VS.equals(propVo.getKindCode()) ||
							CodeConstants.KINDCODE.KINDCODE_DS.equals(propVo.getKindCode()) || CodeConstants.KINDCODE.KINDCODE_DC.equals(propVo.getKindCode())){
						continue;
					}
					amount = amount.add(DataUtils.NullToZero(propVo.getSumRealPay()));
					// 车辆损失赔偿情况
					SHBIVClaimCoverDataVo carClaimCoverData = new SHBIVClaimCoverDataVo();
					// claimCoverData.setPolicyNo(policyNo);
					carClaimCoverData.setLiabilityRate(null);
					carClaimCoverData.setClaimFeeType("2");
					carClaimCoverData.setComCoverageCode(propVo.getKindCode());
					String coverageCode = kindCodeTranService.findTransCiCode("22", propVo.getRiskCode(), "CovergeCodeSH", propVo.getKindCode());
					carClaimCoverData.setCoverageCode(coverageCode);
					if(isFeeUpdate(propVo.getRiskCode())){
						String coverageType = "1".equals(propVo.getItemId()) ? "3" : "2";//本车3(商业车损险)，其他车2(商业三者险)
						//ItemId等于空的是附加险
						if(coverageCode.startsWith("F")){
							carClaimCoverData.setCoverageType("9");//附加险
						}else{
							carClaimCoverData.setCoverageType(coverageType);//本车财传3，其他的传2
						}
					}
					
					carClaimCoverData.setLossAmount(DataUtils.NullToZero(propVo.getSumLoss()).doubleValue());
					carClaimCoverData.setClaimAmount(DataUtils.NullToZero(propVo.getSumRealPay()).doubleValue());
					
//					assesorAmount += DataUtils.NullToZero(propVo.getSumRealPay()).doubleValue();
//					Double sal = propVo.getRescueFee() == null ? 0.0 : propVo.getRescueFee().doubleValue();
					carClaimCoverData.setSalvagefee(DataUtils.NullToZero(propVo.getRescueFee()).doubleValue());
					claimCoverList.add(carClaimCoverData);
				}else{
					if("X3".equals(propVo.getKindCode())){
						if(CodeConstants.KINDCODE.KINDCODE_RS.equals(propVo.getKindCode()) || CodeConstants.KINDCODE.KINDCODE_VS.equals(propVo.getKindCode()) ||
								CodeConstants.KINDCODE.KINDCODE_DS.equals(propVo.getKindCode()) || CodeConstants.KINDCODE.KINDCODE_DC.equals(propVo.getKindCode())){
							continue;
						}
						amount = amount.add(DataUtils.NullToZero(propVo.getSumRealPay()));
						// 车辆损失赔偿情况
						SHBIVClaimCoverDataVo carClaimCoverData = new SHBIVClaimCoverDataVo();
						// claimCoverData.setPolicyNo(policyNo);
						carClaimCoverData.setLiabilityRate(null);
						carClaimCoverData.setClaimFeeType("2");
						carClaimCoverData.setComCoverageCode(propVo.getKindCode());
						String coverageCode = kindCodeTranService.findTransCiCode("22",propVo.getRiskCode(),"CovergeCodeSH",propVo.getKindCode());
						carClaimCoverData.setCoverageCode(coverageCode);
						if(isFeeUpdate(propVo.getRiskCode())){
							String coverageType = "1".equals(propVo.getItemId()) ? "3" : "2";// 本车3(商业车损险)，其他车2(商业三者险)
							// ItemId等于空的是附加险
							if(coverageCode.startsWith("F")){
								carClaimCoverData.setCoverageType("9");// 附加险
							}else{
								carClaimCoverData.setCoverageType(coverageType);// 本车财传3，其他的传2
							}
						}

						carClaimCoverData.setLossAmount(DataUtils.NullToZero(propVo.getSumLoss()).doubleValue());
						carClaimCoverData.setClaimAmount(DataUtils.NullToZero(propVo.getSumRealPay()).doubleValue());

						// assesorAmount += DataUtils.NullToZero(propVo.getSumRealPay()).doubleValue();
						// Double sal = propVo.getRescueFee() == null ? 0.0 : propVo.getRescueFee().doubleValue();
						carClaimCoverData.setSalvagefee(DataUtils.NullToZero(propVo.getRescueFee()).doubleValue());

						carClaimCoverData.setIsDeviceItem("1");// 是否新增设备

						claimCoverList.add(carClaimCoverData);
					}
				}
			}
		}
		
		// 人
		if (compensateVo.getPrpLLossPersons() != null && compensateVo.getPrpLLossPersons().size() > 0) {
			for (PrpLLossPersonVo personVo : compensateVo.getPrpLLossPersons()) {
				// 车辆损失赔偿情况
				SHBIVClaimCoverDataVo carClaimCoverData = new SHBIVClaimCoverDataVo();
				amount = amount.add(DataUtils.NullToZero(personVo.getSumRealPay()));
				// claimCoverData.setPolicyNo(policyNo);
				carClaimCoverData.setLiabilityRate(null);
				carClaimCoverData.setClaimFeeType("3");
				carClaimCoverData.setComCoverageCode(personVo.getKindCode());
				String coverageCode = kindCodeTranService.findTransCiCode("22", personVo.getRiskCode(), "CovergeCodeSH", personVo.getKindCode());
				carClaimCoverData.setCoverageCode(coverageCode);
				if(isFeeUpdate(personVo.getRiskCode())){
					String coverageType = "1".equals(personVo.getItemId()) ? "3" : "2";//本车3(商业车损险)，其他车2(商业三者险)
					if(coverageCode.startsWith("F")){
						carClaimCoverData.setCoverageType("9");//附加险
					}else{
						carClaimCoverData.setCoverageType(coverageType);//
					}
				}

				carClaimCoverData.setLossAmount(DataUtils.NullToZero(personVo.getSumLoss()).doubleValue());
				carClaimCoverData.setClaimAmount(DataUtils.NullToZero(personVo.getSumRealPay()).doubleValue());
				carClaimCoverData.setSalvagefee(0.0);
				
				claimCoverList.add(carClaimCoverData);
			}
		}
//		basePart.setAssesorAmount(assesorAmount);
		basePart.setAssesorAmount(amount.doubleValue());
		
		//没有险种信息，0结案的情况 （5502010088_总赔款金额和各险种赔款金额之和不一致）
		if(claimCoverList==null||claimCoverList.size()==0){
			SHBIVClaimCoverDataVo claimCoverData = new SHBIVClaimCoverDataVo();
			claimCoverData.setLiabilityRate("5");
			claimCoverData.setClaimFeeType("1");
			if(isFeeUpdate(compensateVo.getRiskCode())){
				claimCoverData.setCoverageType("2");// 本车2(商业三者)
				claimCoverData.setCoverageCode(kindCodeTranService.findTransCiCode("22",compensateVo.getRiskCode(), "CovergeCodeSH", "B"));// B01
			}else{
				//claimCoverData.setCoverageCode(getRiskCodeVal(compensateVo.getRiskCode(),"B"));//
				claimCoverData.setCoverageCode(kindCodeTranService.findTransCiCode("22",compensateVo.getRiskCode(), "CovergeCodeSH", "B"));// B01
			}
			claimCoverData.setComCoverageCode("B");//
			claimCoverData.setLossAmount(0.0);
			claimCoverData.setClaimAmount(0.0);
			claimCoverData.setSalvagefee(0.0);

			claimCoverList.add(claimCoverData);
		}

		// 人员损失情况
		List<SHBIVClaimPersonDataVo> personList = new ArrayList<SHBIVClaimPersonDataVo>();
		if(traceMainVos!=null&&traceMainVos.size()>0){
			for (PrpLDlossPersTraceMainVo persTraceMainVo : traceMainVos) {
				List<PrpLDlossPersTraceVo> persTraceVoList = persTraceMainVo.getPrpLDlossPersTraces();
				if(persTraceVoList != null && !persTraceVoList.isEmpty()){
					isPersonInjured ="1";
					for (PrpLDlossPersTraceVo persTraceVo : persTraceMainVo.getPrpLDlossPersTraces()) {
						
						SHBIVClaimPersonDataVo personData = new SHBIVClaimPersonDataVo();
						personData.setPersonName(persTraceVo.getPrpLDlossPersInjured().getPersonName());
						if ("1".equals(persTraceVo.getPrpLDlossPersInjured().getCertiType())) {
							personData.setPersonId(persTraceVo.getPrpLDlossPersInjured().getCertiCode());
						}
						personData.setAge(persTraceVo.getPrpLDlossPersInjured().getPersonAge().intValue());
						Double lossAmount = DataUtils.NullToZero(persTraceVo.getSumVeriDefloss()).doubleValue();
						for(PrpLLossPersonVo personVo : compensateVo.getPrpLLossPersons()){
							if(personVo.getPersonId().longValue() == persTraceVo.getId().longValue()){
								lossAmount = DataUtils.NullToZero(personVo.getSumRealPay()).doubleValue();
							}
						}
						personData.setLossAmount(lossAmount);
						personData.setMainThird("1".equals(persTraceVo.getPrpLDlossPersInjured().getSerialNo()) ? "1" : "0");
						personData.setSurveyType("1".equals(checkVo.getPrpLCheckTask().getFirstAddressFlag()) ? "1" : "3");
						personData.setSurveyName(checkVo.getPrpLCheckTask().getChecker());
						personData.setSurveyStartTime(checkVo.getCreateTime());
						personData.setSurveyEndTime(checkVo.getChkSubmitTime());
						personData.setSurveyPlace(checkVo.getPrpLCheckTask().getCheckAddress());
						personData.setSurveyDes(checkVo.getPrpLCheckTask().getContexts());
//						personData.setEstimateName(persTraceMainVo.getCreateUser());
						String codeCName = codeTranService.findCodeName("UserCodeSH",persTraceMainVo.getCreateUser());
						if(StringUtils.isEmpty(codeCName)){
							codeCName = codeTranService.findCodeName("UserCodeSH",persTraceMainVo.getOperatorCode());
						}
						personData.setEstimateName(StringUtils.isNotEmpty(codeCName) ? codeCName : "GSU0000001");
						personData.setEstimateStartTime(persTraceMainVo.getPlfSubTime());
						personData.setEstimateEndTime(persTraceMainVo.getUnderwriteEndDate());
						personData.setAssesorName(persTraceMainVo.getUnderwriteName());
						// personData.setAssesorStartTime(assesorStartTime);
						// personData.setAssesorEndTime(assesorEndTime);
						// personData.setInjurytype(injurytype);
						// personData.setInjurylevel(injurylevel);
						personData.setMedicaltype(persTraceVo.getPrpLDlossPersInjured().getTreatSituation());
						personData.setUnderwritename(persTraceMainVo.getUnderwriteName());
						personData.setUnderwritecode(persTraceMainVo.getUnderwriteCode());
						// personData.setEstimateaddr(estimateaddr);
						// personData.setAddmissiontime(addmissiontime);
						personData.setUnderdefloss(DataUtils.NullToZero(persTraceVo.getSumVeriDefloss()).doubleValue());
						
						underDefLoss += DataUtils.NullToZero(persTraceVo.getSumVeriDefloss()).doubleValue();
						personData.setCheckerCode(checkVo.getPrpLCheckTask().getCheckerCode());
						personData.setCheckerCertiCode(checkVo.getPrpLCheckTask().getCheckerIdfNo());
						if(StringUtils.isBlank(persTraceMainVo.getOperatorCertiCode())){
							personData.setEstimateCertiCode(persTraceMainVo.getPlfCertiCode());
						} else {
							personData.setEstimateCertiCode(persTraceMainVo.getOperatorCertiCode());
						}
						personData.setDeathTime(persTraceVo.getPrpLDlossPersInjured().getDeathTime());
						personData.setCountryInjuryType(persTraceVo.getPrpLDlossPersInjured().getWoundCode());
						
						// 人员损失费用明细
						List<SHBIVClaimPersLossPartDataVo> lossPartList = new ArrayList<SHBIVClaimPersLossPartDataVo>();
						List<PrpLDlossPersTraceFeeVo> persTraceFeeVoList = persTraceVo.getPrpLDlossPersTraceFees();
						if(persTraceFeeVoList!=null&&persTraceFeeVoList.size()>0){
							for (PrpLDlossPersTraceFeeVo feeVo : persTraceVo.getPrpLDlossPersTraceFees()) {
								SHBIVClaimPersLossPartDataVo lossPartData = new SHBIVClaimPersLossPartDataVo();
								String shFeeType = feeVo.getFeeTypeCode().length()==1
										?"0"+feeVo.getFeeTypeCode():feeVo.getFeeTypeCode();
								lossPartData.setFeeType(shFeeType);// 暂时使用全国平台的代码
								lossPartData.setLossAmount(DataUtils.NullToZero(feeVo.getVeriDefloss()).doubleValue());
								lossPartData.setUnderDefLoss(DataUtils.NullToZero(feeVo.getVeriDefloss()).doubleValue()); //核损金额
								lossPartList.add(lossPartData);
							}
						}
						personData.setLossPartList(lossPartList);
						
						// 人员受伤部位
						List<SHBIVClaimInjuryDataVo> injuryList = new ArrayList<SHBIVClaimInjuryDataVo>();
						List<PrpLDlossPersExtVo> persExtVoList = persTraceVo.getPrpLDlossPersInjured().getPrpLDlossPersExts();
						if(persExtVoList!=null&&persExtVoList.size()>0){
							for (PrpLDlossPersExtVo extVo : persExtVoList) {
								SHBIVClaimInjuryDataVo injuryData = new SHBIVClaimInjuryDataVo();
								injuryData.setInjurypart(extVo.getInjuredPart());
								//injuryData.setInjuryLevelCode(StringUtils.isBlank(extVo.getWoundGrade())?"10":extVo.getWoundGrade()); //伤残程度代码
								String injuryLevelCode =extVo.getWoundGrade();
								if(StringUtils.isBlank(extVo.getWoundGrade()) ||"10".equals(injuryLevelCode)){
									injuryLevelCode ="10";
								}else{
									injuryLevelCode ="0" + injuryLevelCode;
								}
								injuryData.setInjuryLevelCode(injuryLevelCode); //伤残程度代码
								injuryList.add(injuryData);
							}
						}
						personData.setInjuryList(injuryList);
						
						// 人员治疗机构
						if("2".equals(persTraceVo.getPrpLDlossPersInjured().getTreatSituation())){
						    List<SHBIVClaimHospitalInfoDataVo> hospitalInfoList = new ArrayList<SHBIVClaimHospitalInfoDataVo>();
	                        List<PrpLDlossPersHospitalVo> hospitalVoList = persTraceVo.getPrpLDlossPersInjured().getPrpLDlossPersHospitals();
	                        if(hospitalVoList!=null&&hospitalVoList.size()>0){
	                            for (PrpLDlossPersHospitalVo hospitalVo : hospitalVoList) {
	                                SHBIVClaimHospitalInfoDataVo hospitalInfoData = new SHBIVClaimHospitalInfoDataVo();
	                                hospitalInfoData.setHospitalname(hospitalVo.getHospitalName());
	                                hospitalInfoData.setHospitalFactoryCertiCode(hospitalVo.getHospitalCode());  //治疗机构组织机构代码
	                                
	                                hospitalInfoList.add(hospitalInfoData);
	                            }
	                        }
	                        personData.setHospitalInfoList(hospitalInfoList);
						}
						
						
						 //伤残鉴定列表（多条）（隶属于人员损失情况）
						if("02".equals(persTraceVo.getPrpLDlossPersInjured().getWoundCode())){
							List<SHCIVClaimInjuryIdentifyInfoDataVo> ideInfoDataList = new ArrayList<SHCIVClaimInjuryIdentifyInfoDataVo>();
							SHCIVClaimInjuryIdentifyInfoDataVo injuryIdentifyInfoDataVo = new SHCIVClaimInjuryIdentifyInfoDataVo();
							injuryIdentifyInfoDataVo.setInjuryIdentifyName(persTraceVo.getPrpLDlossPersInjured().getChkComName());
							injuryIdentifyInfoDataVo.setInjuryIdentifyCertiCode(persTraceVo.getPrpLDlossPersInjured().getChkComCode());
							ideInfoDataList.add(injuryIdentifyInfoDataVo);
							personData.setInjuryIdentifyInfoData(ideInfoDataList);
						}
						personList.add(personData);
					}
				}
			}
		}
		
		// 车辆损失情况
		List<SHBIVClaimVehicleDataVo> vehicleList = new ArrayList<SHBIVClaimVehicleDataVo>();
		if(lossCarMainVos!=null&&lossCarMainVos.size()>0){
			for (PrpLDlossCarMainVo lossCarMainVo : lossCarMainVos) {
				//车辆（定损方式为无损失的不上传平台）定损注销不上传平台
				if(CodeConstants.CetainLossType.DEFLOSS_NULL.equals(lossCarMainVo.getCetainLossType()) ||
						CodeConstants.VeriFlag.CANCEL.equals(lossCarMainVo.getUnderWriteFlag())){
					continue;
				}
				//如果车辆核损金额为0，则不上传平台，要不然会影响上传损失率
				if(DataUtils.NullToZero(lossCarMainVo.getSumVeriLossFee()).compareTo(BigDecimal.ZERO) == 0){
					continue;
				}
				carNum++;
				SHBIVClaimVehicleDataVo vehicleData = new SHBIVClaimVehicleDataVo();
				//若是上海保单，号牌种类为挂、学、警、领车牌则控制末尾不能录入中文，车牌号长度不能超过6位
				if(lossCarMainVo.getLossCarInfoVo().getLicenseNo()!=null&&lossCarMainVo.getLossCarInfoVo().getLicenseNo().length()>=6&&
						("15".equals(lossCarMainVo.getLossCarInfoVo().getLicenseType())||"16".equals(lossCarMainVo.getLossCarInfoVo().getLicenseType())||
						"17".equals(lossCarMainVo.getLossCarInfoVo().getLicenseType())||"23".equals(lossCarMainVo.getLossCarInfoVo().getLicenseType())||
						"31".equals(lossCarMainVo.getLossCarInfoVo().getLicenseType())||"04".equals(lossCarMainVo.getLossCarInfoVo().getLicenseType())||
						"10".equals(lossCarMainVo.getLossCarInfoVo().getLicenseType()))){
					
					vehicleData.setCarMark(lossCarMainVo.getLossCarInfoVo().getLicenseNo().substring(0, 6));
				}else{
					vehicleData.setCarMark(lossCarMainVo.getLossCarInfoVo().getLicenseNo());
				}
				if(lossCarMainVo.getLossCarInfoVo().getLicenseType()==null){
					vehicleData.setVehicleType("02");
				}else{
					vehicleData.setVehicleType(lossCarMainVo.getLossCarInfoVo().getLicenseType());
				}
				Double lossAmount = DataUtils.NullToZero(lossCarMainVo.getSumVeriLossFee()).doubleValue();
				for(PrpLLossItemVo itemVo : compensateVo.getPrpLLossItems()){
					if(itemVo.getDlossId().longValue() == lossCarMainVo.getId().longValue()){
						lossAmount = DataUtils.NullToZero(itemVo.getSumRealPay()).doubleValue();
					}
				}
				vehicleData.setLossAmount(lossAmount);
				vehicleData.setMainThird("1".equals(lossCarMainVo.getDeflossCarType()) ? "1" : "0");
				vehicleData.setRobber("03".equals(lossCarMainVo.getCetainLossType()) ? "1": "0");
				vehicleData.setSurveyType("1".equals(checkVo.getPrpLCheckTask().getFirstAddressFlag()) ? "1" : "3");
				vehicleData.setSurveyName(checkVo.getPrpLCheckTask().getChecker());
				vehicleData.setSurveyStartTime(checkVo.getCreateTime());
				vehicleData.setSurveyEndTime(checkVo.getChkSubmitTime());
				vehicleData.setSurveyPlace(checkVo.getPrpLCheckTask().getCheckAddress());
				vehicleData.setSurveyDes(checkVo.getPrpLCheckTask().getContexts());
				String codeCName = codeTranService.findCodeName("UserCodeSH",lossCarMainVo.getHandlerCode());
				if(StringUtils.isEmpty(codeCName)){
					codeCName = codeTranService.findCodeName("UserCodeSH",lossCarMainVo.getCreateUser());
				}
				vehicleData.setEstimateName(StringUtils.isNotEmpty(codeCName) ? codeCName : "GSU0000001");
				vehicleData.setAssesorName(lossCarMainVo.getUnderWriteName());
				vehicleData.setRemnant(0d);
				vehicleData.setChargeFee(0d);

				BigDecimal totalWorkingHour = new BigDecimal("0");
				String changeOrRepairPart = "0";
				if (lossCarMainVo.getPrpLDlossCarRepairs() != null && lossCarMainVo.getPrpLDlossCarRepairs().size() > 0) {
					for (PrpLDlossCarRepairVo repairVo : lossCarMainVo.getPrpLDlossCarRepairs()) {
						totalWorkingHour = totalWorkingHour.add(DataUtils.NullToZero(repairVo.getSumVeriLoss()));
					}
					
					Double workingHour = totalWorkingHour.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
					//判断小数点个数
					int i = (workingHour + "").length()-(workingHour + "").indexOf(".")-1;
					if(i > 2){
						DecimalFormat df = new DecimalFormat("#.00");
						vehicleData.setTotalWorkingHour(Double.parseDouble(df.format(workingHour)));
					}else{
						vehicleData.setTotalWorkingHour(workingHour);
					}

//					vehicleData.setTotalWorkingHour(totalWorkingHour.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
					changeOrRepairPart = "1";
				}
				if (lossCarMainVo.getPrpLDlossCarComps() != null&& lossCarMainVo.getPrpLDlossCarComps().size() > 0) {
					changeOrRepairPart = "1";
				}
				vehicleData.setChangeOrRepairPart(changeOrRepairPart);
				vehicleData.setJyVehicleCode(lossCarMainVo.getLossCarInfoVo().getModelCode());
				vehicleData.setClaimVehicleName(lossCarMainVo.getLossCarInfoVo().getModelName());
				vehicleData.setClaimVehicleCode(lossCarMainVo.getLossCarInfoVo().getModelCode());
				String factory = lossCarMainVo.getRepairFactoryName();
				if(StringUtils.isEmpty(factory)){
					factory = "修理厂为空";
				}
				vehicleData.setMadeFactory(factory);
				vehicleData.setVehicleBrandCode(lossCarMainVo.getLossCarInfoVo().getBrandCode());
				vehicleData.setVehicleCatenaCode(lossCarMainVo.getLossCarInfoVo().getSeriCode());
				vehicleData.setVehicleGroupCode(lossCarMainVo.getLossCarInfoVo().getGroupCode());
				String sltCode = "";
				if("001".equals(lossCarMainVo.getRepairFactoryType())){
					sltCode = "01";
				}else if("002".equals(lossCarMainVo.getRepairFactoryType())){
					sltCode = "02";
				}else{
					sltCode = "99";
				}
				vehicleData.setPriceSltCode(sltCode);
				vehicleData.setDefineFlag("0");
				vehicleData.setVin(lossCarMainVo.getLossCarInfoVo().getVinNo());
				vehicleData.setEngineno(lossCarMainVo.getLossCarInfoVo().getEngineNo());
				vehicleData.setModel(lossCarMainVo.getLossCarInfoVo().getModelCode());
				vehicleData.setDrivername(lossCarMainVo.getLossCarInfoVo().getDriveName());
				//String certiType = "1".equals(lossCarMainVo.getLossCarInfoVo().getIdentifyType())?"01":"99";
				 SysCodeDictVo sysdicVo = codeTranService.findTransCodeDictVo
                         ("IdentifyType",lossCarMainVo.getLossCarInfoVo().getIdentifyType()); 
				vehicleData.setCertitype(sysdicVo==null?"99":sysdicVo.getProperty4());
				vehicleData.setCerticode(lossCarMainVo.getLossCarInfoVo().getIdentifyNo());
				vehicleData.setDriverlicenseno(lossCarMainVo.getLossCarInfoVo().getDrivingLicenseNo());
				vehicleData.setTemporaryFlag("0");
				
				vehicleData.setCheckerCode(checkVo.getPrpLCheckTask().getCheckerCode());
				vehicleData.setCheckerCertiCode(checkVo.getPrpLCheckTask().getCheckerIdfNo());
				vehicleData.setUnderWriteCode(lossCarMainVo.getUnderWriteCode());
				vehicleData.setUnderWriteCertiCode(lossCarMainVo.getUnderWiteIdNo());
				vehicleData.setUnderDefLoss(DataUtils.NullToZero(lossCarMainVo.getSumVeriLossFee()).doubleValue());
				vehicleData.setIsTotalLoss(checkVo.getLossType());
				vehicleData.setIsHotSinceDetonation(lossCarMainVo.getIsHotSinceDetonation());
				vehicleData.setIsWaterFlooded(lossCarMainVo.getIsWaterFloaded());
				vehicleData.setWaterFloodedLevel("1".equals(lossCarMainVo.getIsWaterFloaded())?lossCarMainVo.getWaterFloodedLevel():"");
				underDefLoss +=DataUtils.NullToZero(lossCarMainVo.getSumVeriLossFee()).doubleValue();
				
				// 车辆损失部位
				List<SHBIVClaimCarLossPartDataVo> carLossPartList = new ArrayList<SHBIVClaimCarLossPartDataVo>();
				String[] lossPartArr = lossCarMainVo.getLossPart().split(",");
				for (int i = 0; i < lossPartArr.length; i++) {
					String lossPart = lossPartArr[i];
					SHBIVClaimCarLossPartDataVo carLossPartData = new SHBIVClaimCarLossPartDataVo();
					SysCodeDictVo sysVo = codeTranService.findTransCodeDictVo("LossPart",lossPart);
					carLossPartData.setLossPart(sysVo==null?"01":sysVo.getProperty3());
					carLossPartList.add(carLossPartData);
				}
				vehicleData.setLossPartList(carLossPartList);
				// 车辆配件明细
				List<SHBIVClaimFittingDataVo> fittingList = new ArrayList<SHBIVClaimFittingDataVo>();
				if(lossCarMainVo.getPrpLDlossCarComps()!=null&&lossCarMainVo.getPrpLDlossCarComps().size()>0){
					for (PrpLDlossCarCompVo compVo : lossCarMainVo.getPrpLDlossCarComps()) {
						SHBIVClaimFittingDataVo fittingData = new SHBIVClaimFittingDataVo();
						fittingData.setChangePartName(StringUtils.isEmpty(compVo.getCompName()) ? "空" : compVo.getCompName());
						fittingData.setChangePartNum(compVo.getQuantity()==null ? 0 : compVo.getQuantity());
						fittingData.setChangePartFee(compVo.getVeriMaterFee()==null?0D:compVo.getVeriMaterFee().doubleValue());
						fittingData.setChangePartTime(compVo.getManHour()==null?0D:compVo.getManHour().doubleValue());
						fittingData.setChangePartManpowerFee(compVo.getVeriManHourFee()==null?0D:compVo.getVeriManHourFee().doubleValue());
						fittingData.setJyPartCode(compVo.getCompCode());
						fittingData.setOemPartCode(StringUtils.isEmpty(compVo.getOriginalId()) ? "无" : compVo.getOriginalId());
						fittingData.setDefineFlag("1".equals(compVo.getSelfConfigFlag()) ? "1": "0");
						fittingData.setSubjionFlag("0");// 默认全量上次
						fittingData.setPriceType("02");
						fittingData.setQualityType("05");
						fittingList.add(fittingData);
					}
					if("1".equals(changeOrRepairPart)){
						List<SHBIVClaimRepartDataVo> repartDataVoList = new ArrayList<SHBIVClaimRepartDataVo>();
						SHBIVClaimRepartDataVo repartVo = new SHBIVClaimRepartDataVo();
						repartVo.setRepairOrg("JY00008782");
						repartDataVoList.add(repartVo);
						vehicleData.setRepairList(repartDataVoList);
					}
					if("1".equals(changeOrRepairPart)){
						List<SHBIVClaimActuralRepairDataVo> acturalRepairList = new ArrayList<SHBIVClaimActuralRepairDataVo>();
						SHBIVClaimActuralRepairDataVo repairDataVo = new SHBIVClaimActuralRepairDataVo();
						repairDataVo.setActuralrepairorg("JY00008782");
						acturalRepairList.add(repairDataVo);
						vehicleData.setActuralRepairList(acturalRepairList);
					}
				}
				
				if(lossCarMainVo.getPrpLDlossCarRepairs()!=null&&lossCarMainVo.getPrpLDlossCarRepairs().size()>0){
					for (PrpLDlossCarRepairVo repairVo : lossCarMainVo.getPrpLDlossCarRepairs()) {
						SHBIVClaimFittingDataVo fittingData = new SHBIVClaimFittingDataVo();
						 fittingData.setRepairPartName(StringUtils.isEmpty(repairVo.getRepairName()) ? "空" : repairVo.getRepairName());
						 fittingData.setRepairPartNum(lossCarMainVo.getPrpLDlossCarRepairs().size());
						 fittingData.setRepairPartFee(repairVo.getSumDefLoss()==null?0D:repairVo.getSumDefLoss().doubleValue());
						 fittingData.setRepairPartTime(repairVo.getManHour()==null?0D:repairVo.getManHour().doubleValue());
						 fittingData.setRepairPartManpowerFee(repairVo.getManHourFee()==null?0D:repairVo.getManHourFee().doubleValue());
						 fittingData.setRepairMethord("09");
						 fittingData.setJyPartCode(repairVo.getRepairCode());
						 fittingData.setOemPartCode(StringUtils.isEmpty(repairVo.getRepairId()) ? "无" : repairVo.getRepairId());
						 fittingData.setDefineFlag("1".equals(repairVo.getSelfConfigFlag())?"1":"0");
						 fittingData.setSubjionFlag("0");//默认全量上次
						 fittingData.setPriceType("02");
						 fittingData.setQualityType("05");
						fittingList.add(fittingData);
					}
					
					if(vehicleData.getRepairList()==null ||vehicleData.getRepairList().size() ==0){
						if("1".equals(changeOrRepairPart)){
							List<SHBIVClaimRepartDataVo> repartDataVoList = new ArrayList<SHBIVClaimRepartDataVo>();
							SHBIVClaimRepartDataVo repartVo = new SHBIVClaimRepartDataVo();
							repartVo.setRepairOrg("JY00008782");
							repartDataVoList.add(repartVo);
							vehicleData.setRepairList(repartDataVoList);
						}
					}
					
					if(vehicleData.getActuralRepairList() == null || vehicleData.getActuralRepairList().size() == 0 ){
						if("1".equals(changeOrRepairPart)){
							List<SHBIVClaimActuralRepairDataVo> acturalRepairList = new ArrayList<SHBIVClaimActuralRepairDataVo>();
							SHBIVClaimActuralRepairDataVo repairDataVo = new SHBIVClaimActuralRepairDataVo();
							repairDataVo.setActuralrepairorg("JY00008782");
							acturalRepairList.add(repairDataVo);
							vehicleData.setActuralRepairList(acturalRepairList);
						}
					}
					
					
				}
				
				vehicleData.setFittingList(fittingList);
				
				//新加 是否玻璃单独破碎   是否属于无法找到第三方
				vehicleData.setIsGlassBroken(StringUtils.isNotBlank(lossCarMainVo.getIsGlassBroken())?lossCarMainVo.getIsGlassBroken():"0");
				vehicleData.setIsNotFindThird(StringUtils.isNotBlank(lossCarMainVo.getIsNotFindThird())?lossCarMainVo.getIsNotFindThird():"0");
				
				vehicleList.add(vehicleData);
			}
		}
		
		
		
		//物损
		List<SHBIVClaimObjDataVo> objDataVoList = new ArrayList<SHBIVClaimObjDataVo>();
		if(propVoList!=null&&propVoList.size()>0){
			for(PrpLdlossPropMainVo propVo : propVoList){
				if(propVo.getPrpLdlossPropFees()!=null&&propVo.getPrpLdlossPropFees().size()>0){
					isProtectLoss = "1";// 应该判断财损子表存在才是有财损
					for(PrpLdlossPropFeeVo feeVo : propVo.getPrpLdlossPropFees()){
						SHBIVClaimObjDataVo objDataVo = new SHBIVClaimObjDataVo();
						objDataVo.setObjectDesc(feeVo.getLossItemName());
						objDataVo.setLossNum(DataUtils.NullToZero(feeVo.getLossQuantity()).intValue());
						objDataVo.setLossAmount(DataUtils.NullToZero(feeVo.getUnitPrice()).doubleValue());
						objDataVo.setMainThird("1".equals(propVo.getLossType())?"1":"0");
						objDataVo.setSurveyType("1");
						objDataVo.setSurveyName(propVo.getHandlerName());
						objDataVo.setSurveyStartTime(propVo.getCreateTime());
						objDataVo.setSurveyEndTime(propVo.getUpdateTime());
						objDataVo.setSurveyPlace("");
						objDataVo.setSurveyDes(propVo.getVerirescueOpinion());
						String codeCName = codeTranService.findCodeName("UserCodeSH",propVo.getHandlerCode());
						if(StringUtils.isEmpty(codeCName)){
							codeCName = codeTranService.findCodeName("UserCodeSH",propVo.getCreateUser());
						}
						objDataVo.setEstimateName(StringUtils.isNotEmpty(codeCName) ? codeCName : "GSU0000001");
//						objDataVo.setEstimateName(propVo.getHandlerCode());
						objDataVo.setEstimateStartTime(propVo.getUpdateTime());
						objDataVo.setEstimateEndTime(propVo.getUpdateTime());
						objDataVo.setAssesorName(propVo.getUnderWriteCode());
						objDataVo.setAssesorStartTime(propVo.getUpdateTime());
						objDataVo.setAssesorEndTime(propVo.getUnderWriteEndDate());
						
						String protectProperty = "1";
						if("1".equals(propVo.getLossType())){
							protectProperty = "1";  //本车财产
						}else{
							protectProperty = "2";  // 车外财产
						}
						objDataVo.setProtectProperty(protectProperty);
						objDataVo.setCheckerCode(checkVo.getPrpLCheckTask().getCheckerCode());
						objDataVo.setCheckerCertiCode(checkVo.getPrpLCheckTask().getCheckerIdfNo());
						objDataVo.setUnderWriteCode(propVo.getUnderWriteCode());
						objDataVo.setUnderWriteCertiCode(propVo.getUnderWriteIdCard());
						objDataVo.setUnderDefLoss(DataUtils.NullToZero(propVo.getSumVeriLoss()).doubleValue());
						underDefLoss += DataUtils.NullToZero(propVo.getSumVeriLoss()).doubleValue();
						
						objDataVoList.add(objDataVo);
					}
				}
			}
		}
		
		basePart.setIsPersonInjured(isPersonInjured);
		basePart.setIsProtectLoss(isProtectLoss);
		bodyVo.setBasePart(basePart);
		basePart.setIsSingleAccident(carNum==1?"1":"0");
		basePart.setUnderDefLoss((double)Math.round(underDefLoss*100)/100);
		bodyVo.setClaimCoverList(claimCoverList);
		bodyVo.setPersonList(personList);
		bodyVo.setVehicleList(vehicleList);
		bodyVo.setObjList(objDataVoList);
		bodyVo.setDisputeList(null);
		bodyVo.setSubrogationList(null);
		logger.info("sendVClaimBIToSHPlatform assemble xml end，registNo={}, cost time {} ms ",compensateVo.getRegistNo(),System.currentTimeMillis()-currentDate);
		controller.callPlatform(bodyVo,platformTaskVo);
	}
	
	/**
	 * 判断是否费改后的险种
	 * <pre></pre>
	 * @param riskCode
	 * @return false-商改后，true-商改前
	 * @modified:
	 * ☆Luwei(2016年9月7日 下午5:54:20): <br>
	 */
	private boolean isFeeUpdate(String riskCode){
		boolean returnVal = false;//默认商改后
		if("1201".equals(riskCode)){
			returnVal = true;//商改前
		}else if("1202".equals(riskCode)){
			returnVal = true;
		}else if("1203".equals(riskCode)){
			returnVal = true;
		}else if("1204".equals(riskCode)){
			returnVal = true;
		}else if("1205".equals(riskCode)){
			returnVal = true;
		}else{
			returnVal = false;
		}
		return returnVal;
	}
	
	private String getRiskCodeVal(String riskCode,String kindCode){
		String val = "0101200";
		if(!Risk.DQZ.equals(riskCode)&&!"BZ".equals(kindCode)){//商业
			if(isFeeUpdate(riskCode)){//费改前
				// B01 商三险A款 B
				if("B".equals(kindCode)){
					val = "B01";// B01 商三险A款 B
				}else if("A".equals(kindCode)){
					val = "C01";// C01 车损险A款 A
				}else if("G".equals(kindCode)){
					val = "D01";// D01 盗抢险A款 G
				}else if("D11".equals(kindCode) || "D12".equals(kindCode)){
					val = "E01";// 车上人员责任险A款 D11 D12
				}else{
					val = "F01";//找不到的都是其他 F01 附加险A款 其他
				}
			}else{//商改后
				if(Risk.DBA.equals(riskCode)){//1206
					if(KINDCODE.KINDCODE_A.equals(kindCode)){
						val = "0101200";
					}else if(KINDCODE.KINDCODE_B.equals(kindCode)){
						val = "0101600";
					}else if(KINDCODE.KINDCODE_D11.equals(kindCode)){
						val = "0101701";
					}else if(KINDCODE.KINDCODE_D12.equals(kindCode)){
						val = "0101702";
					}else if(KINDCODE.KINDCODE_F.equals(kindCode)){
						val = "0101231";
					}else if(KINDCODE.KINDCODE_G.equals(kindCode)){
						val = "0101500";
					}else if(KINDCODE.KINDCODE_L.equals(kindCode)){
						val = "0101210";
					}else if(KINDCODE.KINDCODE_NT.equals(kindCode)){
						val = "0102250"; 
					}else if(KINDCODE.KINDCODE_RF.equals(kindCode)){
						val = "0102230"; 
					}else if(KINDCODE.KINDCODE_SS.equals(kindCode)){
						val = "0102610"; 
					}else if(KINDCODE.KINDCODE_X.equals(kindCode)){
						val = "0102260";
					}else if(KINDCODE.KINDCODE_X2.equals(kindCode)){
						val = "0101290";
					}else if(KINDCODE.KINDCODE_Z.equals(kindCode)){
						val = "0101310";
					}else if(KINDCODE.KINDCODE_Z3.equals(kindCode)){
						val = "0101252"; 
					}else{
						val = "0101600";
					}
				}else if(Risk.DBE.equals(riskCode)){//1207
					if(KINDCODE.KINDCODE_A.equals(kindCode)){
						val = "0102200"; 
					}else if(KINDCODE.KINDCODE_B.equals(kindCode)){
						val = "0102600";
					}else if(KINDCODE.KINDCODE_D2.equals(kindCode)){
						val = "0102800";
					}else if(KINDCODE.KINDCODE_Z.equals(kindCode)){
						val = "0102310";
					}else if(KINDCODE.KINDCODE_G.equals(kindCode)){
						val = "0102500";
					}else if(KINDCODE.KINDCODE_F.equals(kindCode)){
						val = "0102231";
					}else if(KINDCODE.KINDCODE_D11.equals(kindCode)){
						val = "0101701"; 
					}else if(KINDCODE.KINDCODE_D12.equals(kindCode)){
						val = "0101702";
					}else if(KINDCODE.KINDCODE_K1.equals(kindCode)){
						val = "0102253";
					}else if(KINDCODE.KINDCODE_K2.equals(kindCode)){
						val = "0102254";
					}else if(KINDCODE.KINDCODE_NT.equals(kindCode)){
						val = "0102250"; 
					}else if(KINDCODE.KINDCODE_RF.equals(kindCode)){
						val = "0102230"; 
					}else if(KINDCODE.KINDCODE_SS.equals(kindCode)){
						val = "0102610"; 
					}else if(KINDCODE.KINDCODE_Z3.equals(kindCode)){
						val = "0102252"; 
					}else if(KINDCODE.KINDCODE_X.equals(kindCode)){
						val = "0102260";
					}else{
						val = "0102600";
					}
				}else if(Risk.DBC.equals(riskCode)){//1208
					if(KINDCODE.KINDCODE_A.equals(kindCode)){  //摩托车、拖拉机损失保险
						val = "0103200";
					}else if("AM".equals(kindCode)){ //摩托车、拖拉机损失不计免赔险
						val = "0103911";
					}else if(KINDCODE.KINDCODE_B.equals(kindCode)){ //第三者责任保险
						val = "0103600";
					}else if("BM".equals(kindCode)){ //第三者责任不计免赔险
						val = "0103912";
					}else if(KINDCODE.KINDCODE_D11.equals(kindCode)){  //车上人员责任险(司机)
						val = "0103701"; 
					}else if(KINDCODE.KINDCODE_D12.equals(kindCode)){  //车上人员责任险(乘客)
						val = "0103702";
					}else if(KINDCODE.KINDCODE_G.equals(kindCode)){  //盗抢险
						val = "0103500";
					}else if(KINDCODE.KINDCODE_NT.equals(kindCode)){  //摩托车、拖拉机损失无法找到第三方特约险
						val = "0103250";
					}else{
						val = "0103600";
					}
				}else if(Risk.DBT.equals(riskCode)){ //1209 
					if(KINDCODE.KINDCODE_A.equals(kindCode)){ //机动车损失保险
						val = "0104200";
					}else if(KINDCODE.KINDCODE_B.equals(kindCode)){ //第三者责任保险
						val = "0102600";
					}else if(KINDCODE.KINDCODE_D11.equals(kindCode)){  //车上人员责任险(司机)
						val = "0101701"; 
					}else if(KINDCODE.KINDCODE_D12.equals(kindCode)){  //车上人员责任险(乘客)
						val = "0101702";
					}else if(KINDCODE.KINDCODE_NT.equals(kindCode)){  //机动车损失无法找到第三方特约险
						val = "0104250";
					}else{
						val = "0104600";
					}
				}
			}
		}else{//交强
			val = "A02";
		}
		return val;
	}
	//

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
