/******************************************************************************
 * CREATETIME : 2015年12月8日 上午10:19:01
 ******************************************************************************/
package ins.sino.claimcar.moblie.commonmark.service;

import ins.framework.lang.Springs;
import ins.platform.common.service.facade.CodeTranService;
import ins.platform.common.util.CodeTranUtil;
import ins.platform.utils.DataUtils;
import ins.platform.utils.DateUtils;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.FeeType;
import ins.sino.claimcar.CodeConstants.KINDCODE;
import ins.sino.claimcar.check.service.CheckHandleService;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.CheckActionVo;
import ins.sino.claimcar.check.vo.PrpLCheckCarInfoVo;
import ins.sino.claimcar.check.vo.PrpLCheckCarVo;
import ins.sino.claimcar.check.vo.PrpLCheckDriverVo;
import ins.sino.claimcar.check.vo.PrpLCheckDutyVo;
import ins.sino.claimcar.check.vo.PrpLCheckExtVo;
import ins.sino.claimcar.check.vo.PrpLCheckPersonVo;
import ins.sino.claimcar.check.vo.PrpLCheckPropVo;
import ins.sino.claimcar.check.vo.PrpLCheckTaskVo;
import ins.sino.claimcar.check.vo.PrpLCheckVo;
import ins.sino.claimcar.claim.service.ClaimService;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claim.vo.PrpLPadPayMainVo;
import ins.sino.claimcar.claim.vo.PrpLPadPayPersonVo;
import ins.sino.claimcar.claim.vo.PrpLPaymentVo;
import ins.sino.claimcar.claim.vo.PrpLPrePayVo;
import ins.sino.claimcar.common.claimtext.service.ClaimTextService;
import ins.sino.claimcar.common.claimtext.vo.PrpLClaimTextVo;
import ins.sino.claimcar.common.losscharge.service.LossChargeService;
import ins.sino.claimcar.common.losscharge.vo.PrpLDlossChargeVo;
import ins.sino.claimcar.constant.Risk;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.PayCustomService;
import ins.sino.claimcar.flow.service.WfMainService;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfMainVo;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.losscar.service.DeflossHandleService;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarCompVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarInfoVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMaterialVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarRepairVo;
import ins.sino.claimcar.lossperson.service.PersTraceService;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersExtVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersHospitalVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersNurseVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersRaiseVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceFeeVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceMainVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceVo;
import ins.sino.claimcar.manager.vo.PrpLPayCustomVo;
import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckBody;
import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckHead;
import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckRequest;
import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckResponseHead;
import ins.sino.claimcar.moblie.commonmark.caseDetails.vo.AccountInfoVo;
import ins.sino.claimcar.moblie.commonmark.caseDetails.vo.CarInfoVo;
import ins.sino.claimcar.moblie.commonmark.caseDetails.vo.CaseDetailsInfoResBodyVo;
import ins.sino.claimcar.moblie.commonmark.caseDetails.vo.CaseDetailsInfoResVo;
import ins.sino.claimcar.moblie.commonmark.caseDetails.vo.CaseInfoVo;
import ins.sino.claimcar.moblie.commonmark.caseDetails.vo.CertainsTaskInfoVo;
import ins.sino.claimcar.moblie.commonmark.caseDetails.vo.CertainsTaskInfosVo;
import ins.sino.claimcar.moblie.commonmark.caseDetails.vo.CheckInfoInitVo;
import ins.sino.claimcar.moblie.commonmark.caseDetails.vo.CheckVo;
import ins.sino.claimcar.moblie.commonmark.caseDetails.vo.CollisionInfoVo;
import ins.sino.claimcar.moblie.commonmark.caseDetails.vo.CompensateInfo;
import ins.sino.claimcar.moblie.commonmark.caseDetails.vo.DefLossAssistInfoVo;
import ins.sino.claimcar.moblie.commonmark.caseDetails.vo.DefLossEvalRepairSumInfoVo;
import ins.sino.claimcar.moblie.commonmark.caseDetails.vo.DefLossFeeInfoVo;
import ins.sino.claimcar.moblie.commonmark.caseDetails.vo.DefLossOutsideRepairtInfoVo;
import ins.sino.claimcar.moblie.commonmark.caseDetails.vo.DefLossPartInfoVo;
import ins.sino.claimcar.moblie.commonmark.caseDetails.vo.DefLossRepairInfoVo;
import ins.sino.claimcar.moblie.commonmark.caseDetails.vo.DriverVo;
import ins.sino.claimcar.moblie.commonmark.caseDetails.vo.ExtendInfoVo;
import ins.sino.claimcar.moblie.commonmark.caseDetails.vo.PersionInfo;
import ins.sino.claimcar.moblie.commonmark.caseDetails.vo.PersonInfoVo;
import ins.sino.claimcar.moblie.commonmark.caseDetails.vo.PropInfoVo;
import ins.sino.claimcar.moblie.lossPerson.vo.FeeInfo;
import ins.sino.claimcar.moblie.lossPerson.vo.FeeInfoVos;
import ins.sino.claimcar.moblie.lossPerson.vo.InPantientInfoVo;
import ins.sino.claimcar.moblie.lossPerson.vo.NurseInfoVo;
import ins.sino.claimcar.moblie.lossPerson.vo.PartInfoVo;
import ins.sino.claimcar.moblie.lossPerson.vo.PayeeInfoVo;
import ins.sino.claimcar.moblie.lossPerson.vo.PersonInfoVos;
import ins.sino.claimcar.moblie.lossPerson.vo.RaiseInfoVo;
import ins.sino.claimcar.moblie.lossPerson.vo.TraceInfoVo;
import ins.sino.claimcar.pay.service.PadPayPubService;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.service.ScheduleTaskService;
import ins.sino.claimcar.regist.vo.PrpLCItemCarVo;
import ins.sino.claimcar.regist.vo.PrpLCItemKindVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.schedule.vo.PrpLScheduleTaskVo;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;

import com.sinosoft.api.service.ServiceInterface;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;

/**
 * 案件详情查询（快赔请求理赔）
 * <pre></pre>
 * @author ★zhujunde
 */
public class CaseDetailsServiceImpl implements ServiceInterface {
	
	private static Logger logger = LoggerFactory.getLogger(CaseDetailsServiceImpl.class);

	@Autowired
	WfTaskHandleService wfTaskHandleService;
	@Autowired
	CheckHandleService checkHandleService;
	@Autowired
	CheckTaskService checkTaskService;
	@Autowired
	private PayCustomService payCustomService;
	@Autowired
	private ScheduleTaskService scheduleTaskService;
    @Autowired
    private DeflossHandleService deflossHandleService;
    @Autowired
    LossChargeService lossChargeService; 
    @Autowired
    LossCarService lossCarService;
    @Autowired
    ClaimTextService claimTextService;
    @Autowired
    PolicyViewService policyViewService;
    @Autowired
    RegistQueryService registQueryService;
    @Autowired
    WfMainService wfMainService;
	@Autowired
	CompensateTaskService compensateTaskService;
	@Autowired
	ClaimService claimService;
	@Autowired
	PadPayPubService padPayPubService;
	@Autowired
	PersTraceService persTraceService;
	@Autowired
	CodeTranService codeTranService;

	public CheckInfoInitVo setCheckInfo(CheckInfoInitVo checkInfoInitResBodyVo,CheckInfoInitVo checkTaskInfoVo,CheckActionVo checkActionVo){
	       
		List<CarInfoVo> carInfoVoList = new ArrayList<CarInfoVo>();
		List<PropInfoVo> propInfoVoList = new ArrayList<PropInfoVo>();
		List<PersonInfoVo> personInfoList = new ArrayList<PersonInfoVo>();
		List<AccountInfoVo> accountInfoList = new ArrayList<AccountInfoVo>();
		Map<String,String> serialNoMap = new HashMap<String,String>();
		PrpLCheckVo prpLCheckVo = checkActionVo.getPrpLcheckVo();
		PrpLCheckDutyVo checkDutyVo = checkActionVo.getCheckDutyVo();
		PrpLCheckTaskVo checkTaskVo = prpLCheckVo.getPrpLCheckTask();
		PrpLRegistVo prpLregistVo = checkActionVo.getPrpLregistVo();
		String registNo = prpLregistVo.getRegistNo();
		PrpLCheckTaskVo prpLcheckTaskVo = checkActionVo.getPrpLcheckTaskVo();
		
		List<PrpLDlossChargeVo> prpLDlossChargeVos = checkActionVo.getLossChargeVo();
		
		checkTaskInfoVo.setRegistNo(prpLCheckVo.getRegistNo());
		if(prpLCheckVo.getSingleAccidentFlag().equals("0")){
			checkTaskInfoVo.setIsunilAccident("0");
		}else{
			checkTaskInfoVo.setIsunilAccident("1");
		}
		checkTaskInfoVo.setDamageTypeCode(prpLCheckVo.getDamageTypeCode());
		checkTaskInfoVo.setManageTypeName(prpLCheckVo.getManageType());
		checkTaskInfoVo.setDamageCode(prpLCheckVo.getDamageCode());
		checkTaskInfoVo.setIsLoss(prpLCheckVo.getLossType());
		checkTaskInfoVo.setIsFire("");
		checkTaskInfoVo.setIsWater("");
		checkTaskInfoVo.setWaterLevel("");
		checkTaskInfoVo.setCheckSite(checkTaskVo.getCheckAddress());
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String checkDate = formatter.format(checkTaskVo.getCheckDate());
		checkTaskInfoVo.setCheckDate(checkDate);
		checkTaskInfoVo.setCheckType(prpLCheckVo.getCheckType());
		checkTaskInfoVo.setFirstSiteFlag(checkTaskVo.getFirstAddressFlag());
		checkTaskInfoVo.setExcessType(prpLCheckVo.getIsClaimSelf());
		checkTaskInfoVo.setIsIncludeProp(prpLCheckVo.getIsPropLoss());
		checkTaskInfoVo.setIsIncludePerson(prpLCheckVo.getIsPersonLoss());
		//标的
		CarInfoVo carInfoVo = new CarInfoVo();
		if(checkDutyVo!=null){
			checkTaskInfoVo.setIndemnityDuty(checkDutyVo.getIndemnityDuty());
			if(checkDutyVo.getIndemnityDutyRate() != null){
				checkTaskInfoVo.setIndemnityDutyRate(checkDutyVo.getIndemnityDutyRate().toString());
			}
			carInfoVo.setDutyType(checkDutyVo.getIndemnityDuty());//责任类型
		}
		if(StringUtils.isNotBlank(checkTaskVo.getClaimText())){
			checkTaskInfoVo.setBigCase(checkTaskVo.getClaimText());
		}else{
			checkTaskInfoVo.setBigCase(null);
		}
		
		checkTaskInfoVo.setIsBigCase(prpLCheckVo.getMajorCaseFlag());
		//费用
		BigDecimal checkFree = new BigDecimal(0);
		if(prpLDlossChargeVos!=null && prpLDlossChargeVos.size()>0){
			for(PrpLDlossChargeVo vo :prpLDlossChargeVos){
				if(vo.getChargeFee()!=null){
					checkFree = checkFree.add(vo.getChargeFee());
				}
			}
		}
		checkTaskInfoVo.setCheckFee(checkFree.toString());
		checkTaskInfoVo.setSubrogateType(prpLCheckVo.getIsSubRogation());
		checkTaskInfoVo.setTextType(checkTaskVo.getContexts());//本次查勘报告
		checkTaskInfoVo.setCheckOption(checkTaskVo.getContexts());//查勘意见
		checkTaskInfoVo.setNoDutyFlag(prpLCheckVo.getNoDutyFlag());//是否免责情形
		checkTaskInfoVo.setNoDutyReason(prpLCheckVo.getNoDutyReason());//免责原因
		if("DM99".equals(prpLCheckVo.getDamageCode())){
		    checkTaskInfoVo.setDamOtherCode(prpLCheckVo.getDamOtherCode()); //二级出险原因
		}
		checkTaskInfoVo.setCheckIdenTifyNumber(checkTaskVo.getCheckerIdfNo());
		checkTaskInfoVo.setCheckPhoneNumber(checkTaskVo.getCheckerPhone());
		checkTaskInfoVo.setCheckerCode(prpLcheckTaskVo.getCheckerCode());
		checkTaskInfoVo.setCheckerName(prpLcheckTaskVo.getChecker());
		

		
		PrpLCheckCarVo checkMainCarVo = checkActionVo.getCheckMainCarVo();
		List<PrpLCheckCarVo> checkThirdCarVos = checkActionVo.getCheckThirdCarList();
		
		
		DriverVo driverVo = new DriverVo();
		CheckVo checkVo = new CheckVo();
		PrpLCheckCarInfoVo prpLcheckMainCarVo = checkMainCarVo.getPrpLCheckCarInfo();
		PrpLCheckDriverVo prpLCheckDriverVo = checkMainCarVo.getPrpLCheckDriver();
		
		carInfoVo.setIfObject("标的");
		carInfoVo.setLicenseNo(prpLcheckMainCarVo.getLicenseNo());
		carInfoVo.setLicenseType(prpLcheckMainCarVo.getLicenseType());
		if(StringUtils.isNotBlank(prpLcheckMainCarVo.getLicenseType())){
			carInfoVo.setLicenseType(prpLcheckMainCarVo.getLicenseType());
		}else{
			carInfoVo.setLicenseType("02");
		}
		carInfoVo.setEngineNo(prpLcheckMainCarVo.getEngineNo());
		carInfoVo.setFrameNo(prpLcheckMainCarVo.getFrameNo());
		carInfoVo.setCarKindCode(prpLcheckMainCarVo.getPlatformCarKindCode());
		if(StringUtils.isNotBlank(prpLcheckMainCarVo.getVinNo())){
			carInfoVo.setVin(prpLcheckMainCarVo.getVinNo());
		}else{
			carInfoVo.setVin(prpLcheckMainCarVo.getFrameNo());
		}
		if(StringUtils.isNotBlank(prpLcheckMainCarVo.getInsurecomcode())){
			carInfoVo.setInsurComcode(prpLcheckMainCarVo.getInsurecomcode());
		}else{
			carInfoVo.setInsurComcode("DHIC");
		}
		
		if(prpLcheckMainCarVo.getEnrollDate() != null){
			String enrollDate = formatter.format(prpLcheckMainCarVo.getEnrollDate());
			carInfoVo.setRegisteDate(enrollDate);  
		}
		carInfoVo.setOwern(prpLcheckMainCarVo.getCarOwner());
		carInfoVo.setLicenseColor(prpLcheckMainCarVo.getLicenseColor());
		carInfoVo.setColorCode(prpLcheckMainCarVo.getCarColorCode());
		carInfoVo.setVehiclemodleName(prpLcheckMainCarVo.getBrandName());

		PrpLCItemCarVo prpLCItemCarVo = registQueryService.findCItemCarByRegistNo(registNo);
		if(prpLCItemCarVo != null && StringUtils.isNotBlank(prpLCItemCarVo.getCarOwner())){
			carInfoVo.setCarownPhone(prpLCItemCarVo.getCarOwner());//车主电话
		}
		carInfoVo.setInsCompany(prpLcheckMainCarVo.getInsurecomname());
		driverVo.setDriverName(prpLCheckDriverVo.getDriverName());
		if(StringUtils.isNotBlank(prpLCheckDriverVo.getDriverSex())){
			driverVo.setSex(prpLCheckDriverVo.getDriverSex());
		}else{
			driverVo.setSex("1");
		}
		if(StringUtils.isNotBlank(prpLCheckDriverVo.getDrivingCarType())){
			driverVo.setDrivingCarType(prpLCheckDriverVo.getDrivingCarType());
		}else{
			driverVo.setDrivingCarType("C1");
		}
		driverVo.setCertiType(prpLCheckDriverVo.getIdentifyType());
		driverVo.setIdentifyNumber(prpLCheckDriverVo.getIdentifyNumber());
		driverVo.setDrivingLicenseNo(prpLCheckDriverVo.getDrivingLicenseNo());
		driverVo.setPhoneNumber(prpLCheckDriverVo.getLinkPhoneNumber());
		if(prpLCheckDriverVo.getAcceptLicenseDate()!=null){
			String acceptLicenseDate = formatter.format(prpLCheckDriverVo.getAcceptLicenseDate());
			driverVo.setLicenseDate(acceptLicenseDate);
		}
		
		if(prpLCheckDriverVo.getDriverAge()!=null){
		    driverVo.setAge(String.valueOf(prpLCheckDriverVo.getDriverAge()));
		}else{
		    driverVo.setAge("");
		}
		if(prpLCheckDriverVo.getDriverValidDate()!=null){//驾驶证有效日期
		    String identiEffecTiveDate = formatter.format(prpLCheckDriverVo.getDriverValidDate());
            driverVo.setIdentiEffecTiveDate(identiEffecTiveDate);
        }else{
            driverVo.setIdentiEffecTiveDate("");
        }
		
		carInfoVo.setDriver(driverVo);
		
		
		String maineSrialNo =String.valueOf(checkMainCarVo.getSerialNo());
		String licNo = prpLcheckMainCarVo.getLicenseNo();
		serialNoMap.put(maineSrialNo,1==checkMainCarVo.getSerialNo() ? "标的车("+licNo+")" : "三者车("+licNo+")");
		serialNoMap.put("0","地面/路人损失");
		//新理赔有/无损失，有为0，无为1，所以做下转换
		if(StringUtils.isNotBlank(checkMainCarVo.getLossFlag())){
			if("1".equals(checkMainCarVo.getLossFlag())){
				checkVo.setIsLoss("0");
			}else if("0".equals(checkMainCarVo.getLossFlag())){
				checkVo.setIsLoss("1");
			}
		}else{
			checkVo.setIsLoss(checkMainCarVo.getLossFlag());
		}
		if(checkMainCarVo.getLossFee()!=null){
			checkVo.setEstimatedLoss(checkMainCarVo.getLossFee().toString());
		}
		
		//初始化duty
		PrpLCheckDutyVo checkDutyVo1=checkTaskService.findCheckDuty
				(checkMainCarVo.getRegistNo(),checkMainCarVo.getSerialNo());
		if(checkDutyVo1 != null){
			if(checkDutyVo1.getIndemnityDutyRate()!=null){
				checkVo.setIndemnityDuty(checkDutyVo1.getIndemnityDuty());
			}
		}
		checkVo.setKindCode(checkMainCarVo.getKindCode());
		checkVo.setLossPart(checkMainCarVo.getLossPart());
		carInfoVo.setCheck(checkVo);
		carInfoVoList.add(carInfoVo);
		//三者
		if(checkThirdCarVos != null && checkThirdCarVos.size()>0){
			for(PrpLCheckCarVo vo : checkThirdCarVos){
				CarInfoVo carThirdInfoVo = new CarInfoVo();
				DriverVo driverThirdVo = new DriverVo();
				CheckVo checkThirdVo = new CheckVo();
				PrpLCheckCarInfoVo prpLcheckThirdCarVo = vo.getPrpLCheckCarInfo();
				PrpLCheckDriverVo prpLCheckThirdDriverVo = vo.getPrpLCheckDriver();
				carThirdInfoVo.setIfObject("三者");
				
				String thirdSerialNo = vo.getSerialNo().toString();
				String thirdLicNo = vo.getPrpLCheckCarInfo().getLicenseNo();
				serialNoMap.put(thirdSerialNo,1==vo.getSerialNo() ? "标的车("+thirdLicNo+")" : "三者车("+thirdLicNo+")");
				
					
				carThirdInfoVo.setLicenseNo(prpLcheckThirdCarVo.getLicenseNo());
				carThirdInfoVo.setLicenseType(prpLcheckThirdCarVo.getLicenseType());
				carThirdInfoVo.setEngineNo(prpLcheckThirdCarVo.getEngineNo());
				carThirdInfoVo.setFrameNo(prpLcheckThirdCarVo.getFrameNo());
				carThirdInfoVo.setVin(prpLcheckThirdCarVo.getVinNo());
				carThirdInfoVo.setCarKindCode(prpLcheckThirdCarVo.getPlatformCarKindCode());
				if(StringUtils.isNotBlank(prpLcheckThirdCarVo.getBiInsureComCode())){
					carThirdInfoVo.setInsurComcode(prpLcheckThirdCarVo.getBiInsureComCode());
				}else if(StringUtils.isNotBlank(prpLcheckThirdCarVo.getCiInsureComCode())){
					carThirdInfoVo.setInsurComcode(prpLcheckThirdCarVo.getCiInsureComCode());
				}
				if(vo.getPrpLCheckCarInfo().getEnrollDate()!=null){
					String enrollDates= formatter.format(prpLcheckThirdCarVo.getEnrollDate());
					carThirdInfoVo.setRegisteDate(enrollDates); 
				}
				carThirdInfoVo.setOwern(prpLcheckThirdCarVo.getCarOwner());
				carThirdInfoVo.setLicenseColor(prpLcheckThirdCarVo.getLicenseColor());
				carThirdInfoVo.setColorCode(prpLcheckThirdCarVo.getCarColorCode());
				carThirdInfoVo.setVehiclemodleName(prpLcheckThirdCarVo.getBrandName());
				//初始化duty
				PrpLCheckDutyVo checkThirdDutyVo = checkTaskService.findCheckDuty(registNo,checkMainCarVo.getSerialNo());
				if(checkThirdDutyVo != null){
					carThirdInfoVo.setDutyType(checkThirdDutyVo.getIndemnityDuty());//责任类型
				}
				carThirdInfoVo.setCarownPhone("");//车主电话
				carThirdInfoVo.setInsCompany(prpLcheckThirdCarVo.getInsurecomname());
				
				driverThirdVo.setDriverName(prpLCheckThirdDriverVo.getDriverName());
				driverThirdVo.setSex(prpLCheckThirdDriverVo.getDriverSex());
				driverThirdVo.setDrivingCarType(prpLCheckThirdDriverVo.getDrivingCarType());
				driverThirdVo.setCertiType(prpLCheckThirdDriverVo.getIdentifyType());
				driverThirdVo.setIdentifyNumber(prpLCheckThirdDriverVo.getIdentifyNumber());
				driverThirdVo.setDrivingLicenseNo(prpLCheckThirdDriverVo.getDrivingLicenseNo());
				driverThirdVo.setPhoneNumber(prpLCheckThirdDriverVo.getLinkPhoneNumber());

				if(prpLCheckThirdDriverVo.getAcceptLicenseDate()!=null){
					String acceptLicenseDates = formatter.format(prpLCheckThirdDriverVo.getAcceptLicenseDate());
					driverThirdVo.setLicenseDate(acceptLicenseDates);
				}
				if(prpLCheckThirdDriverVo.getDriverAge()!=null){
				    driverThirdVo.setAge(String.valueOf(prpLCheckThirdDriverVo.getDriverAge()));
		        }else{
		            driverThirdVo.setAge("");
		        }
		        if(prpLCheckThirdDriverVo.getDriverValidDate()!=null){//三者驾驶证有效日期
		            String identiEffecTiveDate = formatter.format(prpLCheckThirdDriverVo.getDriverValidDate());
		            driverThirdVo.setIdentiEffecTiveDate(identiEffecTiveDate);
		        }else{
		            driverThirdVo.setIdentiEffecTiveDate("");
		        }
				carThirdInfoVo.setDriver(driverThirdVo);
				//新理赔有/无损失，有为0，无为1，所以做下转换
				if(StringUtils.isNotBlank(vo.getLossFlag())){
					if("1".equals(vo.getLossFlag())){
						checkThirdVo.setIsLoss("0");
					}else if("0".equals(vo.getLossFlag())){
						checkThirdVo.setIsLoss("1");
					}
				}else{
					checkThirdVo.setIsLoss(vo.getLossFlag());
				}
				if(vo.getLossFee()!=null){
					checkThirdVo.setEstimatedLoss(vo.getLossFee().toString());
				}
				
				//初始化duty
				PrpLCheckDutyVo checkDutyVos=checkTaskService.findCheckDuty
						(vo.getRegistNo(),vo.getSerialNo());
				if(checkDutyVos != null){
					if(checkDutyVos.getIndemnityDutyRate()!=null){
						checkThirdVo.setIndemnityDuty(checkDutyVos.getIndemnityDuty());
					}
				}
				checkThirdVo.setKindCode(vo.getKindCode());
				checkThirdVo.setLossPart(vo.getLossPart());
				carThirdInfoVo.setCheck(checkThirdVo);
				carInfoVoList.add(carThirdInfoVo);
			}
		}
		checkInfoInitResBodyVo.setCarInfo(carInfoVoList);
		
		
		
		//物损信息
		
		List<PrpLCheckPropVo> checkPropVos = checkActionVo.getCheckPropList();
		if(checkPropVos!=null && checkPropVos.size()>0){
			for(PrpLCheckPropVo vo :checkPropVos){
				PropInfoVo propInfoVo = new PropInfoVo();
				propInfoVo.setLossType(vo.getLossPartyName());
				propInfoVo.setLossName(vo.getLossItemName());
				propInfoVo.setLossNum(vo.getLossNum());
            	if(vo.getLossFee()!=null){
            		propInfoVo.setPayAmount(vo.getLossFee().toString());
            	}
            	propInfoVo.setLossFeeType(vo.getLossFeeType());
				propInfoVoList.add(propInfoVo);
			}
		}
		checkInfoInitResBodyVo.setPropInfo(propInfoVoList);
		List<PrpLCheckPersonVo> prpLCheckPersonVos = checkActionVo.getCheckPersonList();
		//人伤
		for(PrpLCheckPersonVo vo:prpLCheckPersonVos){
			PersonInfoVo personInfoVo = new PersonInfoVo();
			
			if(StringUtils.isNotBlank(prpLCheckVo.getReconcileFlag())){
				personInfoVo.setIsAdjust(prpLCheckVo.getReconcileFlag());
			}else{
				personInfoVo.setIsAdjust("1");
			}
			personInfoVo.setLosstype(vo.getLossPartyName());
			personInfoVo.setName(vo.getPersonName());
			personInfoVo.setSex(vo.getPersonSex());
			personInfoVo.setCertiType(vo.getIdentifyType());
			personInfoVo.setIdentifyNumber(vo.getIdNo());
			if(vo.getPersonAge()!=null){
				personInfoVo.setAge(vo.getPersonAge().toString());
			}
			personInfoVo.setIshospital("");
			personInfoVo.setDegree(vo.getPersonPayType());
			personInfoVo.setSurverytpe(prpLCheckVo.getCheckType());
			personInfoVo.setIndustry(vo.getTicCode());
			if(StringUtils.isNotBlank(vo.getHospital())){
			    personInfoVo.setTherapeuticagency(vo.getHospital());
			}else{
			    personInfoVo.setTherapeuticagency("");
			}
			personInfoVo.setTherapeuticagencyCode("");
			personInfoVo.setSumClaim(vo.getLossFee().toString());
			personInfoVo.setDegreedesc(vo.getWoundDetail());
			personInfoVo.setPersHandleType(prpLCheckVo.getPersHandleType());//案件处理类型
			personInfoList.add(personInfoVo);
		}
		checkInfoInitResBodyVo.setPersonInfo(personInfoList);
		
		//收款人信息
		List<PrpLPayCustomVo> payCustomVos =  new ArrayList<PrpLPayCustomVo>();
		payCustomVos = payCustomService.findPayCustomVoList(prpLCheckVo.getRegistNo(),"2");//查询被保险人
		for(PrpLPayCustomVo prpLPayCustomVo : payCustomVos){
		    AccountInfoVo account = new AccountInfoVo();
		    account.setPayeeNature(prpLPayCustomVo.getPayObjectType());
		    account.setIdentifyType(prpLPayCustomVo.getCertifyType());
            account.setPayeeType(prpLPayCustomVo.getPayObjectKind());
            account.setName(prpLPayCustomVo.getPayeeName());
            account.setPubandPrilogo(prpLPayCustomVo.getPublicAndPrivate());
            account.setIdentifyNumber(prpLPayCustomVo.getCertifyNo());
            account.setAccountNo(prpLPayCustomVo.getAccountNo());
            account.setProvinceCode(String.valueOf(prpLPayCustomVo.getProvinceCode()));
            account.setCityCode(String.valueOf(prpLPayCustomVo.getCityCode()));
            account.setBankName(prpLPayCustomVo.getBankName());
            account.setBranchName(prpLPayCustomVo.getBankOutlets());
            account.setBankCode(prpLPayCustomVo.getBankNo());
            account.setTransferMode(prpLPayCustomVo.getPriorityFlag());
            account.setPhone(prpLPayCustomVo.getPayeeMobile());
            account.setDigest(prpLPayCustomVo.getSummary());
            accountInfoList.add(account);
		}
		checkInfoInitResBodyVo.setAccountInfo(accountInfoList);
		
		
		ExtendInfoVo extendInfoVo = new ExtendInfoVo();
        List<PrpLCheckExtVo> checkExtVos=checkTaskService.findPrpLcheckExtVoByRegistNo(prpLCheckVo.getRegistNo());
        if(checkExtVos!=null && checkExtVos.size()>0){
            for(PrpLCheckExtVo prpLCheckExtVo:checkExtVos){
                if(StringUtils.isNotBlank(prpLCheckExtVo.getCheckExtName()) && prpLCheckExtVo.getCheckExtName().contains("行驶证是否有效")){
                    extendInfoVo.setLicenseEffective(prpLCheckExtVo.getColumnValue());
                }
            }
        }
        checkTaskInfoVo.setExtendInfoVo(extendInfoVo);
		
		return checkTaskInfoVo;
		
	}

	@Override
	public CaseDetailsInfoResVo service(String arg0, Object arg1) {
		// TODO Auto-generated method stub
		init();
		//返回的vo
		CaseDetailsInfoResVo resVo = new CaseDetailsInfoResVo();
		CaseDetailsInfoResBodyVo bodyVo = new CaseDetailsInfoResBodyVo();
		CheckInfoInitVo checkInfoInitResBodyVo = new CheckInfoInitVo();//查勘
		//CertainsTaskInfosVo CertainsTaskInfosVo = new CertainsTaskInfosVo();//定损
		
		MobileCheckResponseHead head = new MobileCheckResponseHead();
		MobileCheckHead mobileCheckHead = new MobileCheckHead();
		XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
		stream.autodetectAnnotations(true);
		stream.setMode(XStream.NO_REFERENCES);
		stream.aliasSystemAttribute(null,"class");// 去掉 class属性
		try{
			stream.processAnnotations(MobileCheckBody.class);
			MobileCheckRequest checkInfoInitReqVo = (MobileCheckRequest) arg1;
			stream.processAnnotations(MobileCheckRequest.class);
			String xml = stream.toXML(checkInfoInitReqVo);
			logger.info("案件详情查询接收报文: \n"+xml);
			//CheckInfoInitReqVo checkInfoInitReqVo = (CheckInfoInitReqVo)stream.fromXML(xml);
			MobileCheckBody checkInfoInitReqBodyVo = checkInfoInitReqVo.getBody();
			mobileCheckHead = checkInfoInitReqVo.getHead();
			if (!"015".equals(mobileCheckHead.getRequestType())|| !"claim_user".equals(mobileCheckHead.getUser())|| !"claim_psd".equals(mobileCheckHead.getPassWord())) {
				throw new IllegalArgumentException(" 请求头参数错误  ");
			}
			if(StringUtils.isBlank(xml)){
				throw new IllegalArgumentException("报文为空");
			}
			if(!StringUtils.isNotBlank(mobileCheckHead.getRequestType())){
				throw new IllegalArgumentException("请求类型不能为空");
			}
			if(!StringUtils.isNotBlank(mobileCheckHead.getRequestType())){
				throw new IllegalArgumentException("用户名不能为空");		
			}
			if(!StringUtils.isNotBlank(mobileCheckHead.getRequestType())){
				throw new IllegalArgumentException("密码不能为空");
			}
			if(!StringUtils.isNotBlank(checkInfoInitReqBodyVo.getRegistNo())){
				throw new IllegalArgumentException("报案号不能为空");
			}
			PrpLCheckVo vo = checkTaskService.findCheckVoByRegistNo(checkInfoInitReqBodyVo.getRegistNo());
			CheckActionVo checkActionVo = null;
			// 查勘初始化
			if(vo != null){
				checkActionVo = checkHandleService.initCheckByCheck(vo.getId());
				bodyVo = setCaseDetailsInfoResBody(bodyVo, checkActionVo);
			}
			if("Check".equals(checkInfoInitReqBodyVo.getClaimType())){
				// 查勘初始化
				if(checkActionVo != null){
					checkInfoInitResBodyVo = this.setCheckInfo(checkInfoInitResBodyVo,checkInfoInitResBodyVo, checkActionVo);
					bodyVo.setCheckInfoInitVo(checkInfoInitResBodyVo);
				}
			}else if("DLoss".equals(checkInfoInitReqBodyVo.getClaimType())){
				//定损
				CertainsTaskInfosVo certainsTaskInfosVo = new CertainsTaskInfosVo();
				List<CertainsTaskInfoVo> certainsTaskInfoVoList = setCertainsTaskInfosVo(certainsTaskInfosVo,checkInfoInitReqBodyVo);
				certainsTaskInfosVo.setCertainsTaskInfoVo(certainsTaskInfoVoList);
				bodyVo.setCertainsTaskInfosVo(certainsTaskInfosVo);
			}else{
				if(checkActionVo != null){
					checkInfoInitResBodyVo = this.setCheckInfo(checkInfoInitResBodyVo,checkInfoInitResBodyVo, checkActionVo);
					bodyVo.setCheckInfoInitVo(checkInfoInitResBodyVo);
				}
				//定损
				CertainsTaskInfosVo certainsTaskInfosVo = new CertainsTaskInfosVo();
				List<CertainsTaskInfoVo> certainsTaskInfoVoList = setCertainsTaskInfosVo(certainsTaskInfosVo,checkInfoInitReqBodyVo);
				certainsTaskInfosVo.setCertainsTaskInfoVo(certainsTaskInfoVoList);
				bodyVo.setCertainsTaskInfosVo(certainsTaskInfosVo);
				
				//组织人伤信息
				CaseDetailsInfoResBodyVo respBodyVo = setPersonInfo(checkInfoInitReqBodyVo);
				
				bodyVo.setPersionInfo(respBodyVo.getPersionInfo());
				bodyVo.setPersonInfoVo(respBodyVo.getPersonInfoVo());
				bodyVo.setFeeInfoVo(respBodyVo.getFeeInfoVo());
				
			}
			head.setResponseType(mobileCheckHead.getRequestType());
            head.setResponseCode("YES");
            head.setResponseMessage("Success");
            resVo.setHead(head);
			resVo.setBody(bodyVo);
		}
		catch(Exception e){
			head.setResponseType(mobileCheckHead.getRequestType());
			head.setResponseCode("NO");
			head.setResponseMessage(e.getMessage());
			resVo.setHead(head);
			logger.info("案件详情查询异常信息：\n");
			e.printStackTrace();
		}
		stream.processAnnotations(CaseDetailsInfoResVo.class);
		logger.info("案件详情查询返回报文=========：\n"+stream.toXML(resVo));
		return resVo;
	}

	private void init() {
		String url = SpringProperties.getProperty("MClaimPlatform_URL_IN");
		logger.info("移动查勘地址================="+url);
		if(payCustomService==null){
			payCustomService = (PayCustomService)Springs.getBean(PayCustomService.class);
		}
		if(wfTaskHandleService==null){
			wfTaskHandleService = (WfTaskHandleService)Springs.getBean(WfTaskHandleService.class);
		}
		if(checkHandleService==null){
			checkHandleService = (CheckHandleService)Springs.getBean(CheckHandleService.class);
		}
		if(checkTaskService==null){
			checkTaskService = (CheckTaskService)Springs.getBean(CheckTaskService.class);
		}
		if(scheduleTaskService==null){
			scheduleTaskService = (ScheduleTaskService)Springs.getBean(ScheduleTaskService.class);
		}
		if(deflossHandleService==null){
		    deflossHandleService = (DeflossHandleService)Springs.getBean(DeflossHandleService.class);
        }
		if(lossChargeService==null){
		    lossChargeService = (LossChargeService)Springs.getBean(LossChargeService.class);
        }
		if(lossCarService==null){
		    lossCarService = (LossCarService)Springs.getBean(LossCarService.class);
        }
		if(claimTextService==null){
		    claimTextService = (ClaimTextService)Springs.getBean(ClaimTextService.class);
        }
		if(policyViewService==null){
			policyViewService = (PolicyViewService)Springs.getBean(PolicyViewService.class);
        }
		if(registQueryService==null){
			registQueryService = (RegistQueryService)Springs.getBean(RegistQueryService.class);
        }
		if(wfMainService==null){
			wfMainService = (WfMainService)Springs.getBean(WfMainService.class);
        }
		if(compensateTaskService==null){
			compensateTaskService = (CompensateTaskService)Springs.getBean(CompensateTaskService.class);
        }
		if(claimService==null){
			claimService = (ClaimService)Springs.getBean(ClaimService.class);
        }
		if(padPayPubService==null){
			padPayPubService = (PadPayPubService)Springs.getBean(PadPayPubService.class);
        }
		if(persTraceService == null){
			persTraceService = (PersTraceService)Springs.getBean(PersTraceService.class);
		}
		if(codeTranService == null){
			codeTranService = (CodeTranService)Springs.getBean(CodeTranService.class);
		}
		
	}
	
	/**
	 * 组织人伤信息
	 * @param checkInfoInitReqBodyVo
	 * @return
	 */
	private CaseDetailsInfoResBodyVo setPersonInfo(MobileCheckBody mobileCheckBody){
		
		PersionInfo persionInfo = new PersionInfo();
		List<PersonInfoVos> personInfoVoList = new ArrayList<PersonInfoVos>();
		List<FeeInfoVos> feeInfoVoList = new ArrayList<FeeInfoVos>();
		CaseDetailsInfoResBodyVo resBodyVo = new CaseDetailsInfoResBodyVo();
		
		String registNo = mobileCheckBody.getRegistNo();
		List<PrpLDlossPersTraceMainVo> traceMainList = new ArrayList<PrpLDlossPersTraceMainVo>();
		if(StringUtils.isNotBlank(registNo)){
			traceMainList = persTraceService.findPersTraceMainVo(registNo);
		}
		
		//人伤信息
		if(traceMainList != null && traceMainList.size() > 0){
			persionInfo.setTracer(traceMainList.get(0).getPlfName());
			persionInfo.setTracerIdentifyNo(traceMainList.get(0).getPlfCertiCode());
			persionInfo.setIsSmallCase(traceMainList.get(0).getIsMinorInjuryCases());
			persionInfo.setIsDerogationIs(traceMainList.get(0).getIsDeroFlag());
			
			resBodyVo.setPersionInfo(persionInfo);
			
			List<PrpLDlossPersTraceVo> prplDlossPersTraceVo = persTraceService.findPersTraceVoByRegistNo(registNo);
			//人员信息
			if(prplDlossPersTraceVo != null && prplDlossPersTraceVo.size() > 0){
				for(PrpLDlossPersTraceVo traceVo : prplDlossPersTraceVo){
					PersonInfoVos personInfo = new PersonInfoVos();
					List<PartInfoVo> partInfoList = new ArrayList<PartInfoVo>();
					List<InPantientInfoVo> inpantientInfoList = new ArrayList<InPantientInfoVo>();
					TraceInfoVo traceInfoVo = new TraceInfoVo();
					List<FeeInfo> feeInfoList = new ArrayList<FeeInfo>();
					List<NurseInfoVo> nurseInfoList = new ArrayList<NurseInfoVo>();
					List<RaiseInfoVo> raiseInfoList = new ArrayList<RaiseInfoVo>();
					
					personInfo.setPersonId((traceVo.getPrpLDlossPersInjured().getPersonId() != null ? traceVo.getPrpLDlossPersInjured().getPersonId().toString() : ""));
					personInfo.setCaseType(traceVo.getPrpLDlossPersInjured().getTreatSituation());
					personInfo.setName(traceVo.getPrpLDlossPersInjured().getPersonName());
					personInfo.setCredentialsType(traceVo.getPrpLDlossPersInjured().getCertiType());
					personInfo.setCredentialsNo(traceVo.getPrpLDlossPersInjured().getCertiCode());
					personInfo.setPersonAttr(traceVo.getPrpLDlossPersInjured().getLossItemType());
					personInfo.setAge(traceVo.getPrpLDlossPersInjured().getPersonAge() != null ? traceVo.getPrpLDlossPersInjured().getPersonAge().toString() : "");
					personInfo.setSex(traceVo.getPrpLDlossPersInjured().getPersonSex());
					personInfo.setIndustryCode(traceVo.getPrpLDlossPersInjured().getTicCode());
					personInfo.setIndustryName(traceVo.getPrpLDlossPersInjured().getTicName());
					personInfo.setIncomeType(traceVo.getPrpLDlossPersInjured().getIncome());
					personInfo.setHukouType(traceVo.getPrpLDlossPersInjured().getDomicile());
					personInfo.setPhone(traceVo.getPrpLDlossPersInjured().getPhoneNumber());
					personInfo.setDamageType(traceVo.getPrpLDlossPersInjured().getWoundCode());
					personInfo.setLicenseNo(mobileCheckBody.getLicenseNo());
					personInfo.setIdentifyCompanyName(traceVo.getPrpLDlossPersInjured().getAppraisaCity());
					personInfo.setIdentifyCompanyCode(traceVo.getPrpLDlossPersInjured().getChkComCode());
					
					//受伤部位信息
					long injuredVoId = traceVo.getPrpLDlossPersInjured().getId();
					List<PrpLDlossPersExtVo> persExtVoList = traceVo.getPrpLDlossPersInjured().getPrpLDlossPersExts();
					if(persExtVoList != null && persExtVoList.size() > 0){
						for(PrpLDlossPersExtVo extVo : persExtVoList){
//							if(extVo..equals(injuredVoId)){
								PartInfoVo partInfo = new PartInfoVo();
								partInfo.setPart(extVo.getInjuredPart());
								partInfo.setDiagnosis(extVo.getInjuredDiag());
								partInfo.setTreatmentType(extVo.getTreatMethod());
								partInfo.setTreatmentWay(extVo.getTreatWay());
								partInfo.setPrognosis(extVo.getProgSituation());
								partInfo.setDisabilityDegree(extVo.getWoundGrade());
								partInfo.setDetail(extVo.getDiagDetail());
								partInfo.setRemark(extVo.getTrackRemark());
								
								partInfoList.add(partInfo);
								personInfo.setPartInfoVo(partInfoList);
//							}
						}
					}
					
					//住院信息
					List<PrpLDlossPersHospitalVo> hospitalVoList = traceVo.getPrpLDlossPersInjured().getPrpLDlossPersHospitals();
					if(hospitalVoList != null && hospitalVoList.size() > 0){
						for(PrpLDlossPersHospitalVo hospitalVo : hospitalVoList){
							InPantientInfoVo infoVo = new InPantientInfoVo();
							infoVo.setId(hospitalVo.getId().toString());
							infoVo.setInTime(hospitalVo.getInHospitalDate() != null ? hospitalVo.getInHospitalDate().toString() : "");
							infoVo.setOutTime(hospitalVo.getOutHospitalDate() != null ? hospitalVo.getOutHospitalDate().toString() : "");
							infoVo.setProvince(hospitalVo.getHospitalProvince());
							infoVo.setCity(hospitalVo.getHospitalCity());
							infoVo.setHospitalCode(hospitalVo.getHospitalCode());
							infoVo.setHospitalName(hospitalVo.getHospitalName());
							infoVo.setRemark(hospitalVo.getRemark());
							
							inpantientInfoList.add(infoVo);
							personInfo.setInPantientInfoVo(inpantientInfoList);
						}
					}
					
					//跟踪记录信息
					traceInfoVo.setId(traceVo.getId().toString());
					traceInfoVo.setTraceType(traceVo.getTraceForms());
					traceInfoVo.setContent(traceVo.getOperatorDesc());
					traceInfoVo.setIsClaim(traceVo.getEndFlag());
					
					//跟踪费用记录信息
					List<PrpLDlossPersTraceFeeVo> traceFeeList = traceVo.getPrpLDlossPersTraceFees();
					if(traceFeeList != null && traceFeeList.size() > 0){
						for(PrpLDlossPersTraceFeeVo feeVo : traceFeeList){
							FeeInfo feeInfo = new FeeInfo();
							feeInfo.setId(feeVo.getId().toString());
							feeInfo.setFeeName(feeVo.getFeeTypeName());
							feeInfo.setEstimatedLoss(feeVo.getReportFee() != null ? feeVo.getReportFee().toString() : "");
							feeInfo.setClaimAmount(feeVo.getRealFee() != null ? feeVo.getRealFee().toString() : "");
							//费用公式
//							feeInfo.setCostFormula(feeVo);
							feeInfo.setFixedLossAmount(feeVo.getDefloss() != null ? feeVo.getDefloss().toString() : "");
							feeInfo.setImpairmentAmount(feeVo.getDetractionfee() != null ? feeVo.getDetractionfee().toString() : "");
							feeInfo.setFeedEscription(feeVo.getRemark());
							
							feeInfoList.add(feeInfo);
							traceInfoVo.setFeeInfo(feeInfoList);
						}
					}
					personInfo.setTraceInfoVo(traceInfoVo);
					
					//护理人员信息
					List<PrpLDlossPersNurseVo> nurseList = traceVo.getPrpLDlossPersInjured().getPrpLDlossPersNurses();
					if(nurseList != null && nurseList.size() > 0){
						for(PrpLDlossPersNurseVo nurseVo : nurseList){
							NurseInfoVo info = new NurseInfoVo();
							info.setId(nurseVo.getId().toString());
							info.setName(nurseVo.getPayPersonName());
							info.setSex(nurseVo.getSex());
							info.setCareerCode(nurseVo.getOccupationCode());
							info.setCareerName(nurseVo.getOccupationName());
							info.setIncomeType(nurseVo.getIncome());
							info.setRelationShip(nurseVo.getRelationship());
							
							nurseInfoList.add(info);
							personInfo.setNurseInfoVo(nurseInfoList);
						}
					}
					
					//被抚养人员信息
					List<PrpLDlossPersRaiseVo> raiseList = traceVo.getPrpLDlossPersInjured().getPrpLDlossPersRaises();
					if(raiseList != null && raiseList.size() > 0){
						for(PrpLDlossPersRaiseVo raiseVo : raiseList){
							RaiseInfoVo raise = new RaiseInfoVo();
							raise.setId(raiseVo.getId().toString());
							raise.setName(raiseVo.getPayPersonName());
							raise.setAge(raiseVo.getAge() != null ? raiseVo.getAge().toString() : "");
							raise.setSex(raiseVo.getSex());
							raise.setHukouType(raiseVo.getDomicile());
							raise.setRelationShip(raiseVo.getRelationship());
							
							raiseInfoList.add(raise);
							personInfo.setRaiseInfoVo(raiseInfoList);
						}
					}
					personInfoVoList.add(personInfo);
					
				}
			}
			resBodyVo.setPersonInfoVo(personInfoVoList);
			
			//费用赔款信息
			List<PrpLDlossChargeVo> chargeVoList = lossChargeService.findLossChargeVoByRegistNo(registNo);
			if(chargeVoList != null && chargeVoList.size() > 0){
				for(PrpLDlossChargeVo chargeVo : chargeVoList){
					if("PLoss".equals(chargeVo.getBusinessType())){
						FeeInfoVos feeInfo = new FeeInfoVos();
						feeInfo.setFeeId(chargeVo.getId() != null ? chargeVo.getId().toString() : "");
						feeInfo.setKindCode(chargeVo.getKindCode());
						feeInfo.setFeeType(chargeVo.getChargeCode());
						feeInfo.setAccountId(chargeVo.getReceiverId() != null ? chargeVo.getReceiverId().toString() : "");
						feeInfo.setAmount(chargeVo.getChargeFee() != null ? chargeVo.getChargeFee().toString() : "");
						//费用赔款--收款人信息
						long id = chargeVo.getReceiverId();
						PrpLPayCustomVo customVo = payCustomService.findPayCustomVoById(id);
						if(customVo != null){
							PayeeInfoVo payeeInfo = new PayeeInfoVo();
							payeeInfo.setPayeeNature(customVo.getPayObjectType());
							payeeInfo.setIdentifyType(customVo.getCertifyType());
							payeeInfo.setIdentifyNumber(customVo.getCertifyNo());
							payeeInfo.setPayeeType(customVo.getPayObjectKind());
							payeeInfo.setName(customVo.getPayeeName());
							payeeInfo.setPubAndPrilogo(customVo.getPublicAndPrivate());
							payeeInfo.setAccountNo(customVo.getAccountNo());
							payeeInfo.setProvinceCode(customVo.getProvinceCode() != null ? customVo.getProvinceCode().toString() : "");
							payeeInfo.setCityCode(customVo.getCityCode() != null ? customVo.getCityCode().toString() : "");
							//收款方开户行
							payeeInfo.setBankName(codeTranService.findCodeName("BankCode_Old", customVo.getBankName()));
							payeeInfo.setBranchName(customVo.getBankOutlets());
							payeeInfo.setBankCode(customVo.getBankNo());
							payeeInfo.setTransferMode(customVo.getPriorityFlag());
							payeeInfo.setPhone(customVo.getPayeeMobile());
							payeeInfo.setUse(customVo.getPurpose());
							
							feeInfo.setPayeeInfoVo(payeeInfo);
							
						}
						
						feeInfoVoList.add(feeInfo);
					}
				}
			}
			resBodyVo.setFeeInfoVo(feeInfoVoList);
			
		}	
		
		return resBodyVo;
	}
	
	private List<CertainsTaskInfoVo> setCertainsTaskInfosVo(CertainsTaskInfosVo certainsTaskInfosVo,MobileCheckBody mobileCheckBody){
		String registNo = mobileCheckBody.getRegistNo();
	    certainsTaskInfosVo.setRegistNo(registNo);
		String licenseNo = "";
		Integer serialNo = null;
		if(StringUtils.isNotBlank(mobileCheckBody.getLicenseNo())){//车牌号
			licenseNo = mobileCheckBody.getLicenseNo();
		}
		if(StringUtils.isNotBlank(mobileCheckBody.getIfObject())){//类型
			if("标的".equals(mobileCheckBody.getIfObject())){
				serialNo = 1;
			}else if("三者".equals(mobileCheckBody.getIfObject())){
				serialNo = 2;
			}
		}
		List<PrpLDlossCarMainVo> lossCarMainVoList = lossCarService.findPrpLDlossCarMainVoByOther(registNo,serialNo,licenseNo);
	    List<CertainsTaskInfoVo> certainsTaskInfoVoList = new ArrayList<CertainsTaskInfoVo>();
	    for(PrpLDlossCarMainVo lossCarMainVo : lossCarMainVoList){
	    	//微信端请求时使用
	    	double wSumDirectSupply = 0.00;  //直供配件的金额
            double wSumManHourFee = 0.00;    //直供配件工时费
            double wSumNant = 0.00;          //直供配件残值
            double sumOutFee = 0.00;         //外修合计金额
	        CertainsTaskInfoVo certainsTaskInfoVo = new CertainsTaskInfoVo();
	        PrpLDlossCarInfoVo carInfoVo = lossCarService.findPrpLDlossCarInfoVoById(lossCarMainVo.getCarId());
	        //PrpLWfTaskVo taskVo = wfTaskHandleService.queryTask(Double.valueOf(flowTaskId));
	        //PrpLDlossCarInfoVo carInfoVo = deflossVo.getCarInfoVo();
	        //lossCarMainVo.getFlowFlag();
	        String subNodeCode = "";
	        FlowNode nodeCode = FlowNode.DLCar;
	        if(CodeConstants.defLossSourceFlag.SCHEDULDEFLOSS.equals(lossCarMainVo.getDeflossSourceFlag())){
	            subNodeCode = FlowNode.DLCar.toString();
	        }else if(CodeConstants.defLossSourceFlag.ADDTIONALDEFLOSS.equals(lossCarMainVo.getDeflossSourceFlag())){
	            subNodeCode = FlowNode.DLCarAdd.toString();
	            nodeCode = FlowNode.DLCarAdd;
	        }else{
	            subNodeCode = FlowNode.DLCarMod.toString();
	            nodeCode = FlowNode.DLCarMod;
	        }
	        PrpLClaimTextVo claimTextVo = null;
	        PrpLWfTaskVo fTaskVo = null;
	        List<PrpLWfTaskVo> prpLWfTaskInVos = wfTaskHandleService.findInTask(registNo,String.valueOf(lossCarMainVo.getId()),subNodeCode);
	        if(prpLWfTaskInVos!=null && prpLWfTaskInVos.size() >0){//in表有值取in表的
	        	fTaskVo = prpLWfTaskInVos.get(0);
	            claimTextVo = claimTextService.findClaimTextByNode(lossCarMainVo.getId(),subNodeCode,"0");
	        }else{//in表没有值取out表的
	            List<PrpLWfTaskVo> prpLWfTaskOutVos = wfTaskHandleService.findEndTask(registNo,String.valueOf(lossCarMainVo.getId()),nodeCode);
	            fTaskVo = prpLWfTaskOutVos.get(0);
	            claimTextVo = claimTextService.findClaimTextByNode(lossCarMainVo.getId(),subNodeCode,"1");
	        }
	        
	     /*   PrpLClaimTextVo claimTextVo = null;
	        if(prpLWfTaskVos!=null && prpLWfTaskVos.size() >0){
	            BigDecimal taskId = prpLWfTaskVos.get(0).getTaskId();
	            claimTextVo = claimTextService.findClaimTextByNode(taskId.longValue(),subNodeCode,"1");
	        }*/
	         
	        //PrpLClaimTextVo claimTextVo = deflossVo.getClaimTextVo();
	        
	        //组织定损主信息
	        //certainsTaskInfoVo.setRegistNo(taskVo.getRegistNo());
	        /*if(lossCarMainVo.getId()!=null){
	            certainsTaskInfoVo.setCertainsId(lossCarMainVo.getId().toString());
	        }
	        //certainsTaskInfoVo.setTaskId(taskVo.getTaskId().toString());
	        certainsTaskInfoVo.setNodeType("DLCar");
	        if("1".equals(lossCarMainVo.getDeflossCarType())){
	            certainsTaskInfoVo.setItemNo("1");
	            certainsTaskInfoVo.setIsObject("1");
	        }else{
	            certainsTaskInfoVo.setItemNo("2");
	            certainsTaskInfoVo.setIsObject("0");
	        }*/
	        /*certainsTaskInfoVo.setItemNoName(taskVo.getItemName());
	        if(taskVo.getHandlerUser()==null || taskVo.getHandlerUser().isEmpty()){
	            certainsTaskInfoVo.setNextHandlerCode(taskVo.getAssignUser());
	            certainsTaskInfoVo.setNextHandlerName(CodeTranUtil.transCode("UserCode", taskVo.getAssignUser()));
	            certainsTaskInfoVo.setScheduleObjectId(taskVo.getAssignCom());
	            certainsTaskInfoVo.setScheduleObjectName(CodeTranUtil.transCode("ComCodeFull", taskVo.getAssignCom()));
	        }else{
	            certainsTaskInfoVo.setNextHandlerCode(taskVo.getHandlerUser());
	            certainsTaskInfoVo.setNextHandlerName(CodeTranUtil.transCode("UserCode", taskVo.getHandlerUser()));
	            certainsTaskInfoVo.setScheduleObjectId(taskVo.getHandlerCom());
	            certainsTaskInfoVo.setScheduleObjectName(CodeTranUtil.transCode("ComCodeFull", taskVo.getHandlerCom()));
	        }
	        
	        certainsTaskInfoVo.setOptionType(taskVo.getWorkStatus());*/
	        certainsTaskInfosVo.setExigenceGree(lossCarMainVo.getMercyFlag());
	        certainsTaskInfoVo.setCertainsId(lossCarMainVo.getId().toString());
	        if("1".equals(lossCarMainVo.getDeflossCarType())){
				certainsTaskInfoVo.setIsObject("1");
			}else{
				certainsTaskInfoVo.setIsObject("0");
			}
	        certainsTaskInfoVo.setOwern(carInfoVo.getCarOwner());
	        if(carInfoVo.getEnrollDate()!=null){
	            certainsTaskInfoVo.setEnrollDate(DateUtils.dateToStr(carInfoVo.getEnrollDate(), DateUtils.YToSec));
	        }
	        certainsTaskInfoVo.setCarKindCode(carInfoVo.getPlatformCarKindCode());
	        certainsTaskInfoVo.setFrameNo(carInfoVo.getFrameNo());
	        certainsTaskInfoVo.setVin(carInfoVo.getVinNo());
	        certainsTaskInfoVo.setEngineNo(carInfoVo.getEngineNo());
	        certainsTaskInfoVo.setLicenseType(carInfoVo.getLicenseType());
	        certainsTaskInfoVo.setLicenseNo(carInfoVo.getLicenseNo());
	        certainsTaskInfoVo.setVehicleModleCode(carInfoVo.getModelCode());
	        certainsTaskInfoVo.setVehicleModleName(carInfoVo.getModelName());
	        certainsTaskInfoVo.setBrandName(carInfoVo.getBrandName());
	        certainsTaskInfoVo.setStIpolicyNo(carInfoVo.getCiPolicyNo());//交强险保单号
            certainsTaskInfoVo.setStInsurCom(carInfoVo.getCiInsureComCode());//交强承保机构
			List<PrpLCMainVo> cMainVoList = policyViewService.getPolicyAllInfo(registNo);
			if(cMainVoList != null && cMainVoList.size() > 0){
				for(PrpLCMainVo cMainVo : cMainVoList){
					if(cMainVo.getRiskCode().startsWith("12")){
						certainsTaskInfoVo.setSyPolicyNo(cMainVo.getPolicyNo());//商业保单
					}else{//交强
						if(StringUtils.isBlank(carInfoVo.getCiPolicyNo())){
							certainsTaskInfoVo.setStIpolicyNo(carInfoVo.getCiPolicyNo());//交强险保单号
						}
					}
				}
			}
			if(lossCarMainVo.getSerialNo()==1){
				PrpLCheckDutyVo checkDutyVo = checkTaskService.findCheckDuty(registNo,1);
				PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
				if("1".equals(checkDutyVo.getIsClaimSelf()) || CodeConstants.ReportType.CI.equals(registVo.getReportType())){
					certainsTaskInfoVo.setKindCode(KINDCODE.KINDCODE_BZ);
				}else{
					if(lossCarMainVo.getLossFeeType()!=null){
						String kindCode="";
						if(CodeConstants.ISNEWCLAUSECODE2020_MAP.get(lossCarMainVo.getRiskCode()) != null &&
								CodeConstants.ISNEWCLAUSECODE2020_MAP.get(lossCarMainVo.getRiskCode())){
							kindCode = CodeConstants.LossFee2020Kind_Map.get(lossCarMainVo.getRiskCode()+lossCarMainVo.getLossFeeType());
						}else{
							kindCode = CodeConstants.LossFeeKind_Map.get(lossCarMainVo.getLossFeeType());
						}
						
						certainsTaskInfoVo.setKindCode(kindCode);
					}else{
						certainsTaskInfoVo.setKindCode(KINDCODE.KINDCODE_BZ);
					}
				}
			}else{
				List<PrpLCItemKindVo> itemKinds = policyViewService.findItemKinds(registNo,null);
				for(PrpLCItemKindVo prpCitemKindVo:itemKinds){
					String kindCode = prpCitemKindVo.getKindCode();
					if("B".equals(kindCode) || "BZ".equals(kindCode)){
						certainsTaskInfoVo.setKindCode(kindCode);
					}
				}
			}
	        certainsTaskInfoVo.setCertainsComcode(lossCarMainVo.getMakeCom());//定损机构
	        certainsTaskInfoVo.setCertainsComname(CodeTranUtil.transCode("ComCodeFull", lossCarMainVo.getMakeCom()));//定损机构名称
	        String certainsCode = "";
	        if(StringUtils.isNotBlank(fTaskVo.getHandlerUser())){
	        	certainsCode = fTaskVo.getHandlerUser();
	        }else if(StringUtils.isNotBlank(fTaskVo.getAssignUser())){
	        	certainsCode = fTaskVo.getAssignUser();
	        }
	        certainsTaskInfoVo.setCertainsCode(certainsCode);//定损人员编码
	        String certainsName = CodeTranUtil.transCode("UserCode", certainsCode);
		    if(certainsName != null && certainsName.equals(certainsCode)){
		    	certainsName = scheduleTaskService.findPrpduserByUserCode(certainsCode,"").getUserName(); 
		    }
	        certainsTaskInfoVo.setCertainsName(certainsName);//定损人员名称
	        //定损方式默认修复定损
	        if(lossCarMainVo.getCetainLossType()==null || lossCarMainVo.getCetainLossType().isEmpty()){
	            certainsTaskInfoVo.setAssessType("01");
	        }else{
	            certainsTaskInfoVo.setAssessType(lossCarMainVo.getCetainLossType());
	        }
	        certainsTaskInfoVo.setAssessCarType(carInfoVo.getLossCarKindCode());
	        certainsTaskInfoVo.setRepairName(lossCarMainVo.getRepairFactoryName());
	        certainsTaskInfoVo.setRepairType(lossCarMainVo.getRepairFactoryType());
	        certainsTaskInfoVo.setRepairPhone(lossCarMainVo.getFactoryMobile());
	        certainsTaskInfoVo.setDegreeDamage(lossCarMainVo.getLossLevel());
	        certainsTaskInfoVo.setExcessType(lossCarMainVo.getIsClaimSelf());
	        certainsTaskInfoVo.setSumFitsFee(bigToString(lossCarMainVo.getSumCompFee()));
	        certainsTaskInfoVo.setAccessorFee(bigToString(lossCarMainVo.getSumMatFee()));
	        certainsTaskInfoVo.setSumrepairFee(bigToString(lossCarMainVo.getSumRepairFee()));
	        certainsTaskInfoVo.setSumSalvageFee(bigToString(lossCarMainVo.getSumRemnant()));
	        certainsTaskInfoVo.setCeraMount(bigToString(lossCarMainVo.getSumLossFee()));
	        certainsTaskInfoVo.setAssistFee(bigToString(lossCarMainVo.getSumRescueFee()));
	        certainsTaskInfoVo.setVirtualValue(bigToString(lossCarMainVo.getActualValue()));
	        certainsTaskInfoVo.setVerSumFitsFee(bigToString(lossCarMainVo.getSumVeriCompFee()));
	        certainsTaskInfoVo.setVerAccessorFee(bigToString(lossCarMainVo.getSumVeriMatFee()));
	        certainsTaskInfoVo.setVerSumRepairFee(bigToString(lossCarMainVo.getSumVeriRepairFee()));
	        certainsTaskInfoVo.setVerSumSalvageFee(bigToString(lossCarMainVo.getSumVeriRemnant()));
	       
	        if(claimTextVo!=null){
	            certainsTaskInfoVo.setLossOpinion(claimTextVo.getDescription());
	        }
	        certainsTaskInfoVo.setSumFeeAmount(bigToDouble(lossCarMainVo.getSumChargeFee()));
	        
            certainsTaskInfoVo.setFroceExclusions(lossCarMainVo.getIsCInotpayFlag());//交强拒赔
            certainsTaskInfoVo.setBusinessExclusions(lossCarMainVo.getIsBInotpayFlag());//商业拒赔
            certainsTaskInfoVo.setExclusionsReason(lossCarMainVo.getNotpayCause());//拒赔原因
            if("5".equals(lossCarMainVo.getNotpayCause())){
                certainsTaskInfoVo.setOtherExcreason(lossCarMainVo.getOtherNotpayCause());
            }
            certainsTaskInfoVo.setDocumentsComplete(lossCarMainVo.getDirectFlag());//单证齐全
            certainsTaskInfoVo.setIsSpecialOperation(lossCarMainVo.getIsSpecialcarFlag());//是否有特种车操作证
            certainsTaskInfoVo.setIsBusinessQuelification(lossCarMainVo.getIsBusinesscarFlag());//是否有营业车资格证
            certainsTaskInfoVo.setIsNoResponsibility(lossCarMainVo.getIsNodutypayFlag());//是否无责代赔
            //损失部位
            List<CollisionInfoVo> collisionInfoVos = new ArrayList<CollisionInfoVo>();
            if(StringUtils.isNotBlank(lossCarMainVo.getLossPart())){
            	String[] lossParts = lossCarMainVo.getLossPart().split(",");
            	for(int i = 0;i < lossParts.length; i++){
            		CollisionInfoVo collisionInfoVo = new CollisionInfoVo();
            		collisionInfoVo.setCollisionWay(lossParts[i]);
            		collisionInfoVos.add(collisionInfoVo);
            	}
            }
            certainsTaskInfoVo.setCollisionInfoVo(collisionInfoVos);
            certainsTaskInfoVo.setCaseTime(DateUtils.dateToStr(lossCarMainVo.getDefEndDate(), DateUtils.YToSec));//定损时间
	        //换件信息
	        List<DefLossPartInfoVo> defLossPartInfoVo = new ArrayList<DefLossPartInfoVo>();
	        for(PrpLDlossCarCompVo compVo:lossCarMainVo.getPrpLDlossCarComps()){
	        	    //微信端过来的请求，剔除直供标志的配件
	        		if((mobileCheckBody.getSystemType() != null && mobileCheckBody.getSystemType().equals("2")) && 
	        		   (compVo.getDirectSupplyFlag() != null && compVo.getDirectSupplyFlag().equals("1"))){
	        			logger.info("微信端请求的数据，剔除直供的换件信息！");
	        			
	        			//计算直供配件的金额
	        			wSumDirectSupply = wSumDirectSupply + (compVo.getSumDefLoss() == null? 0d:compVo.getSumDefLoss().doubleValue());
	        			//计算直供配件的残值金额
	        			wSumNant = wSumNant + (compVo.getRestFee()==null?0d:compVo.getRestFee().doubleValue());
	        			
	        		}else{
	        			DefLossPartInfoVo partInfoVo = new DefLossPartInfoVo();
	        			/* partInfoVo.setPartId(compVo.getIndId());
	                       partInfoVo.setSerialNo(compVo.getSerialNo());
	                       partInfoVo.setOriginalId(compVo.getOriginalId());*/
	        			partInfoVo.setPartId(compVo.getIndId());
	        			partInfoVo.setPartCode(compVo.getOriginalId());
	        			partInfoVo.setItemName(compVo.getCompName());
	        			partInfoVo.setSysguidePrice(bigToDouble(compVo.getSys4SPrice()));
	        			partInfoVo.setSysmarketPrice(bigToDouble(compVo.getSysMarketPrice()));
	        			partInfoVo.setLocalGuidePrice(bigToDouble(compVo.getRepairFactoryFee()));//本地市场原厂价
	        			partInfoVo.setLocalMarketPrice(bigToDouble(compVo.getNativeMarketPrice()));//本地区域市场价
	        			partInfoVo.setLocalApplicablePrice(null);//本地区域适用价
	        			partInfoVo.setLocalPrice(bigToDouble(compVo.getMaterialFee()));
	        			partInfoVo.setCount(compVo.getQuantity());
	        			partInfoVo.setMaterialFee(bigToDouble(compVo.getMaterialFee()));
	        			partInfoVo.setSelfconfigFlag(compVo.getSelfConfigFlag());
	        			partInfoVo.setItemCoverCode(compVo.getKindCode());
	        			partInfoVo.setRemark(compVo.getRemark());
	        			partInfoVo.setChgcompsetCode(compVo.getPriceType());
	        			partInfoVo.setFitbackFlag(compVo.getRecycleFlag());
	        			partInfoVo.setRemainsPrice(bigToDouble(compVo.getRestFee()));
	        			partInfoVo.setDetectedFlag("");//待检测标志
	        			partInfoVo.setDirectSupplyFlag("");//直供测标志
	        			partInfoVo.setDirectSupplier("");//直供商
	        			//精友2代上线后再传start
	        			partInfoVo.setManageSingleRate(compVo.getManageSingleRate());
	        			partInfoVo.setManageSingleFee(compVo.getManageSingleFee());
	        			partInfoVo.setRecyclePartFlag(compVo.getRecyclePartFlag());
	        			partInfoVo.setSelfPayRate(compVo.getSelfPayRate());
	        			//精友2代上线后再传end
	        			partInfoVo.setEvalpartSum(bigToDouble(compVo.getSumDefLoss()));
	        			partInfoVo.setIfWading(compVo.getWadFlag());
	        			partInfoVo.setLossFee2(bigToDouble(compVo.getSumVeriLoss()));
	        			partInfoVo.setRemark2(compVo.getVeriRemark());
	        			defLossPartInfoVo.add(partInfoVo);
	        			
	        			
	        		}
	        		
	        		//当是微信端请求时，在这里组织 修理费用
	        		if(mobileCheckBody.getSystemType() != null && mobileCheckBody.getSystemType().equals("2")){
	        			if(compVo.getDirectSupplyFlag() != null && compVo.getDirectSupplyFlag().equals("1")){
	        				logger.info("当是微信端请求数据，且为直供配件时，剔除掉工时费信息！");
	        				
	        				for(PrpLDlossCarRepairVo prpLDlossCarRepairVo : lossCarMainVo.getPrpLDlossCarRepairs()){
	        					if(compVo.getCompName().equals(prpLDlossCarRepairVo.getCompName())){
	        						
	        						//计算直供配件给工时费
	        						wSumManHourFee = wSumManHourFee + (prpLDlossCarRepairVo.getManHourFee()==null?0d:prpLDlossCarRepairVo.getManHourFee().doubleValue());
	        					}
	        				}
	        				
	        			}else{
	        				List<DefLossEvalRepairSumInfoVo> evalRepairSumInfoVos  = new ArrayList<DefLossEvalRepairSumInfoVo>();//修理合计
	        				for(PrpLDlossCarRepairVo prpLDlossCarRepairVo : lossCarMainVo.getPrpLDlossCarRepairs()){
	        					if(compVo.getCompName().equals(prpLDlossCarRepairVo.getCompName())){
	        						DefLossEvalRepairSumInfoVo defLossEvalRepairSumInfoVo = new DefLossEvalRepairSumInfoVo();
	        						
	        						defLossEvalRepairSumInfoVo.setDiscountRefPrice(prpLDlossCarRepairVo.getManHourUnitPrice());
	        						defLossEvalRepairSumInfoVo.setEvalRepairSum(prpLDlossCarRepairVo.getManHourFee());
	        						defLossEvalRepairSumInfoVo.setHourDiscount(new BigDecimal(0));
	        						defLossEvalRepairSumInfoVo.setItemCount(prpLDlossCarRepairVo.getManHour().intValue());
	        						defLossEvalRepairSumInfoVo.setReferencePrice(prpLDlossCarRepairVo.getManHourFee());
	        						defLossEvalRepairSumInfoVo.setWorkTypeCode(StringUtils.isNotBlank(prpLDlossCarRepairVo.getRepairCode())? prpLDlossCarRepairVo.getRepairCode():"QT");
	        						defLossEvalRepairSumInfoVo.setLossFee2(prpLDlossCarRepairVo.getVeriManHourFee());
	        						
	        						evalRepairSumInfoVos.add(defLossEvalRepairSumInfoVo);
	        					}
	        				}
	        				certainsTaskInfoVo.setEvalRepairSumInfoVo(evalRepairSumInfoVos);
	        			}
	        		}
	        }
	        certainsTaskInfoVo.setDefLossPartInfoVo(defLossPartInfoVo);
	        
	        //微信端过来的请求,剔除掉直供和外修金额后的定损金额
	        if(mobileCheckBody.getSystemType() != null && mobileCheckBody.getSystemType().equals("2")){
				String sumVeriAmount = String.format("%.2f", (((lossCarMainVo.getSumVeriLossFee() == null ? 0d : lossCarMainVo.getSumVeriLossFee().doubleValue())
						- wSumDirectSupply - wSumManHourFee - (lossCarMainVo.getSumOutFee() == null ? 0d:lossCarMainVo.getSumOutFee().doubleValue())) + wSumNant));
	        	certainsTaskInfoVo.setSumVeriAmount(sumVeriAmount);
	        }else{
	        	certainsTaskInfoVo.setSumVeriAmount(bigToString(lossCarMainVo.getSumVeriLossFee()));
	        }
	        
	        //修理信息
	        List<DefLossRepairInfoVo> repairInfoList = new ArrayList<DefLossRepairInfoVo>();
	    	List<DefLossOutsideRepairtInfoVo> defLossOutsideRepairtInfoVos = new ArrayList<DefLossOutsideRepairtInfoVo>();//外修信息
	        for(PrpLDlossCarRepairVo repairVo:lossCarMainVo.getPrpLDlossCarRepairs()){
	            DefLossRepairInfoVo repairInfo = new DefLossRepairInfoVo();
	            if(CodeConstants.RepairFlag.INNERREPAIR.equals(repairVo.getRepairFlag())){
		            repairInfo.setRepairId(repairVo.getRepairId());
		            repairInfo.setRepairModeCode(repairVo.getCompCode());  
		            repairInfo.setItemName(repairVo.getCompName());
		            repairInfo.setManPowerFee(bigToDouble(repairVo.getSumDefLoss()));
		            repairInfo.setManPowerrefFee("");//工时参考价格
		            repairInfo.setSelfConfigFlag(repairVo.getSelfConfigFlag());
		            repairInfo.setItemCoverCode(repairVo.getKindCode());
		            repairInfo.setRemark(repairVo.getRemark());
		            repairInfo.setEvalHour(NullToZero(repairVo.getManHour()));
		            repairInfo.setRepaiRunitPrice(NullToZero(repairVo.getManHourUnitPrice()));
		            if(repairVo.getLevelCode() != null){
		            	repairInfo.setRepairLevelCode(repairVo.getLevelCode().toString());
		            }
		            repairInfo.setRepairLevelName(repairVo.getLevelName());
		            repairInfo.setLossFee2(NullToDoubleZero(repairVo.getSumVeriLoss()));
		            repairInfo.setRemark2(repairVo.getVeriRemark());
		            repairInfoList.add(repairInfo);
	            }else if(CodeConstants.RepairFlag.OUTREPAIR.equals(repairVo.getRepairFlag())){    //外修
            		if((mobileCheckBody.getSystemType()!= null && mobileCheckBody.getSystemType().equals("2"))){
            		    //如果是小程序端请求，那么剔除外修信息
            			logger.info("该条数据是微信端的外修数据，剔除掉！");
            		}else{
            			DefLossOutsideRepairtInfoVo defLossOutsideRepairtInfoVo = new DefLossOutsideRepairtInfoVo();
            			defLossOutsideRepairtInfoVo.setOuterId(repairVo.getRepairId());
            			defLossOutsideRepairtInfoVo.setOuterName(repairVo.getCompName());
            			defLossOutsideRepairtInfoVo.setReferencePrice("");//参考工时费
            			defLossOutsideRepairtInfoVo.setRepairHandAddFlag(repairVo.getSelfConfigFlag());//自定义标记
            			defLossOutsideRepairtInfoVo.setRepairLevelCode("");//修理程度
            			defLossOutsideRepairtInfoVo.setEvalouterPirce(bigToDouble(repairVo.getManHourFee()));
            			defLossOutsideRepairtInfoVo.setDerogationPrice("");//外修项目减损金额
            			defLossOutsideRepairtInfoVo.setDerogationitemName(repairVo.getRepairName());//配件项目名称(外修项目减损关联配件项目名称)
            			defLossOutsideRepairtInfoVo.setDerogationItemCode(repairVo.getRepairCode());//配件零件号(外修项目减损关联配件零件号)
            			defLossOutsideRepairtInfoVo.setDerogationPriceType("");//配件价格类型
            			defLossOutsideRepairtInfoVo.setPartPrice(NullToZero(repairVo.getMaterialFee()));//配件金额
            			defLossOutsideRepairtInfoVo.setRepairFactoryId(repairVo.getRepairFactoryCode());//外修修理厂ID
            			defLossOutsideRepairtInfoVo.setRepairFactoryName(repairVo.getRepairFactoryName());//外修修理厂名称
            			defLossOutsideRepairtInfoVo.setRepairFactoryCode("");//外修修理厂代码
            			defLossOutsideRepairtInfoVo.setItemCoverCode(repairVo.getKindCode());
            			defLossOutsideRepairtInfoVo.setRemark(repairVo.getRemark());
            			defLossOutsideRepairtInfoVo.setRepairouterSum(NullToZero(repairVo.getSumDefLoss()));//外修费用小计金额
            			defLossOutsideRepairtInfoVo.setReferencePartPrice("");//外修配件参考价
            			defLossOutsideRepairtInfoVo.setPartAmount("");//外修配件数量
            			defLossOutsideRepairtInfoVo.setLossFee2(repairVo.getSumVeriLoss());//外修核损金额
            			defLossOutsideRepairtInfoVo.setOutitemAmount(repairVo.getMaterialQuantity());//外修数量
            			defLossOutsideRepairtInfoVos.add(defLossOutsideRepairtInfoVo);
            		}
	            }

	        }
	        certainsTaskInfoVo.setDefLossRepairInfoVo(repairInfoList);
	        certainsTaskInfoVo.setDefLossOutsideRepairtInfoVo(defLossOutsideRepairtInfoVos);
	        
	    	
	    	
	    	
	    	//精友2代上线后再传start
	        if(mobileCheckBody.getSystemType() != null && mobileCheckBody.getSystemType().equals("2")){
	        	//微信端过来的请求不在这里组装数据，要过滤掉直供的工时费
	        	logger.info("微信端的直供工时费数据，不在这里组织数据！");
	        }else{
	        	List<DefLossEvalRepairSumInfoVo> evalRepairSumInfoVos  = new ArrayList<DefLossEvalRepairSumInfoVo>();//修理合计
	        	for(PrpLDlossCarRepairVo prpLDlossCarRepairVo : lossCarMainVo.getPrpLDlossCarRepairs()){
	        		DefLossEvalRepairSumInfoVo defLossEvalRepairSumInfoVo = new DefLossEvalRepairSumInfoVo();
	        		
	        		defLossEvalRepairSumInfoVo.setDiscountRefPrice(prpLDlossCarRepairVo.getManHourUnitPrice());
	        		defLossEvalRepairSumInfoVo.setEvalRepairSum(prpLDlossCarRepairVo.getManHourFee());
	        		defLossEvalRepairSumInfoVo.setHourDiscount(new BigDecimal(0));
	        		defLossEvalRepairSumInfoVo.setItemCount(prpLDlossCarRepairVo.getManHour().intValue());
	        		defLossEvalRepairSumInfoVo.setReferencePrice(prpLDlossCarRepairVo.getManHourFee());
	        		defLossEvalRepairSumInfoVo.setWorkTypeCode(StringUtils.isNotBlank(prpLDlossCarRepairVo.getRepairCode())? prpLDlossCarRepairVo.getRepairCode():"QT");
	        		defLossEvalRepairSumInfoVo.setLossFee2(prpLDlossCarRepairVo.getVeriManHourFee());
	        		
	        		
	        		evalRepairSumInfoVos.add(defLossEvalRepairSumInfoVo);
	        	}
	        	certainsTaskInfoVo.setEvalRepairSumInfoVo(evalRepairSumInfoVos);
	        }
	    	 //精友2代上线后再传end
	    	List<DefLossAssistInfoVo> defLossAssistInfoVos  = new ArrayList<DefLossAssistInfoVo>();//辅料合计
	    	for(PrpLDlossCarMaterialVo material : lossCarMainVo.getPrpLDlossCarMaterials()){
	    		DefLossAssistInfoVo defLossAssistInfoVo = new DefLossAssistInfoVo();
	    		defLossAssistInfoVo.setAssistId(material.getAssistId());
	    		defLossAssistInfoVo.setItemName(material.getMaterialName());
	    		if(material.getAssisCount() != null){
	    			defLossAssistInfoVo.setCount(material.getAssisCount().toString());
	    		}else{
	    			defLossAssistInfoVo.setCount("0");
	    		}
	    		defLossAssistInfoVo.setMaterialFee(NullToZero(material.getUnitPrice()));
	    		defLossAssistInfoVo.setEvalmateSum(NullToZero(material.getMaterialFee()));
	    		defLossAssistInfoVo.setSelfConfigFlag(material.getSelfConfigFlag());
	    		defLossAssistInfoVo.setItemCoverCode(material.getKindCode());
	    		defLossAssistInfoVo.setRemark(material.getRemark());
	    		defLossAssistInfoVo.setLossFee2(material.getAuditLossMaterialFee());
	    		defLossAssistInfoVos.add(defLossAssistInfoVo);
	    	}
	    	certainsTaskInfoVo.setDefLossAssistInfoVo(defLossAssistInfoVos);
	        
	        
	        //定损费用
	        List<DefLossFeeInfoVo> lossFeeInfoList = new ArrayList<DefLossFeeInfoVo>();
	        List<PrpLDlossChargeVo> lossChargeVoList = lossChargeService.findLossChargeVos(lossCarMainVo.getId(), FlowNode.DLCar.name());
	        if(lossChargeVoList!=null && lossChargeVoList.size()>0){
	            for(PrpLDlossChargeVo chargeVo:lossChargeVoList){
	                DefLossFeeInfoVo lossFeeInfo = new DefLossFeeInfoVo();
	               // lossFeeInfo.setSerialNo(String.valueOf(chargeVo.getSerialNo()));
	                lossFeeInfo.setKindCode(chargeVo.getKindCode());
	                lossFeeInfo.setFeeType(chargeVo.getChargeCode());
	                lossFeeInfo.setFeeAmount(bigToDouble(chargeVo.getChargeFee()));
//	              lossFeeInfo.setAccountId();
	                //lossFeeInfo.setPayee(chargeVo.getReceiver());
	                //收款人
	                PrpLPayCustomVo prpLPayCustomVo = payCustomService.findPayCustomVoById(chargeVo.getReceiverId());
                    if(prpLPayCustomVo!=null){
                    	lossFeeInfo.setAccountId(prpLPayCustomVo.getAccountId());//理赔收款人ID
    	                lossFeeInfo.setPayeeType(prpLPayCustomVo.getPayObjectKind());
                        lossFeeInfo.setName(prpLPayCustomVo.getPayeeName());
                        lossFeeInfo.setIdentifyNumber(prpLPayCustomVo.getCertifyNo());
                        lossFeeInfo.setBankName(prpLPayCustomVo.getBankOutlets());
                        lossFeeInfo.setAccountName(prpLPayCustomVo.getPayeeName());
                        lossFeeInfo.setAccountNo(prpLPayCustomVo.getAccountNo());
                        lossFeeInfo.setPhone(prpLPayCustomVo.getPayeeMobile());
                    }
	                lossFeeInfoList.add(lossFeeInfo);
	            }
	            certainsTaskInfoVo.setDefLossFeeInfoVo(lossFeeInfoList);
	        }
	        certainsTaskInfoVoList.add(certainsTaskInfoVo);
	    }
        
        return certainsTaskInfoVoList;
        //resBody.setCertainsTaskInfoVo(certainsTaskInfoVo);
    }
	
	private static Double bigToDouble(BigDecimal strNum){
        if(strNum==null){
            return null;
        }
        return strNum.doubleValue();
    }
	private static Double NullToDoubleZero(BigDecimal strNum){
        if(strNum==null){
            return (double)0;
        }
        return strNum.doubleValue();
    }
	
   private static BigDecimal NullToZero(BigDecimal strNum) {
        if(strNum==null){
            return new BigDecimal("0");
        }
        return strNum;
    }
   private static String bigToString(BigDecimal strNum){
       if(strNum==null){
           return null;
       }
       return strNum.toString();
   }

   private Double sumpay(String registNo,List<PrpLCompensateVo> compeVos,String cpsType){
  	    //理算信息
		List<PrpLCompensateVo> PAY_CI = new ArrayList<PrpLCompensateVo>();//交强
		List<PrpLCompensateVo> PAY_BI = new ArrayList<PrpLCompensateVo>();//商业
		List<PrpLPrePayVo> prePay_ci = new ArrayList<PrpLPrePayVo>();// 交强预付
		List<PrpLPrePayVo> prePay_bi = new ArrayList<PrpLPrePayVo>();// 商业预付
		if(compeVos != null && compeVos.size() > 0){
			for(PrpLCompensateVo compeVo : compeVos){
				if("Y".equals(compeVo.getCompensateType())){
					String comNo = compeVo.getCompensateNo();
					List<PrpLPrePayVo> prePayVos = compensateTaskService.queryPrePay(comNo);
					if(prePayVos != null && prePayVos.size() > 0){
						for(PrpLPrePayVo prePayVo:prePayVos){
							if(Risk.DQZ.equals(prePayVo.getRiskCode())){// 交强
								prePay_ci.add(prePayVo);
							}else{
								prePay_bi.add(prePayVo);
							}
						}
					}
				}else{
					if(Risk.DQZ.equals(compeVo.getRiskCode())){
						PAY_CI.add(compeVo);
					}else{
						PAY_BI.add(compeVo);
					}
				}
			}
			
		}

		 // 垫付
	    PrpLPadPayMainVo padPayMainVo = padPayPubService.queryPadPay(registNo,null);
	    Double sumpad=calculatePad(padPayMainVo);
	    Double sumPay_CI=0.00;
	    Double sumPay_BI=0.00;
	    Double sum=0.00;
	    if("CI".equals(cpsType)){
	        sumPay_CI=calculateSum(PAY_CI,prePay_ci,"1")+sumpad;
	        sum=sumPay_CI;
	    }else{
	    	sumPay_BI=calculateSum(PAY_BI,prePay_bi,"1");
	    	sum=sumPay_BI;
	    }
	    return sum;
  }
	private Double calculateSum(List<PrpLCompensateVo> compeVoList,List<PrpLPrePayVo> prePay,String payType) {
		BigDecimal sumPay = new BigDecimal(0);
		if(compeVoList!=null&&compeVoList.size()>0){
		for(PrpLCompensateVo compeVo : compeVoList){
			if("1".equals(payType)){//赔款
				if(compeVo.getPrpLPayments()!=null && compeVo.getPrpLPayments().size()>0){
				for(PrpLPaymentVo sumRealPay : compeVo.getPrpLPayments()){// 实赔
					sumPay = sumPay.add(sumRealPay.getSumRealPay());
				}
				}
			}
			if("2".equals(payType)){//费用
				sumPay = sumPay.add(compeVo.getSumPaidFee());
			}
			for(PrpLPrePayVo per : prePay){// 预付
				String feeType = per.getFeeType();
				if("2".equals(payType)&&FeeType.FEE.equals(feeType)){//费用
					sumPay = sumPay.add(per.getPayAmt());
				}
				if("1".equals(payType)&&FeeType.PAY.equals(feeType)){//赔款
					sumPay = sumPay.add(per.getPayAmt());
				}
			}
		}
		}
		return sumPay.doubleValue();
	}
	
	private Double calculatePad(PrpLPadPayMainVo padPayMainVo){
		Double sumRealPay = 0.00;
		if(padPayMainVo != null){
			List<PrpLPadPayPersonVo> persons = padPayMainVo.getPrpLPadPayPersons();
			for(PrpLPadPayPersonVo person : persons){// 垫付
				sumRealPay += DataUtils.NullToZero(person.getCostSum()).doubleValue();
			}
		}
		return sumRealPay;
	}
	public CaseDetailsInfoResBodyVo setCaseDetailsInfoResBody(CaseDetailsInfoResBodyVo caseDetailsInfoResBodyVo,CheckActionVo checkActionVo){
		CaseInfoVo caseInfoVo = new CaseInfoVo();
		PrpLCheckDutyVo checkDutyVo = checkActionVo.getCheckDutyVo();
		PrpLRegistVo prpLregistVo = checkActionVo.getPrpLregistVo();
		String registNo = prpLregistVo.getRegistNo();
		//查询理算表
		List<PrpLCompensateVo> compeVos = compensateTaskService.findNotCancellCompensatevosByRegistNo(registNo);
		List<CompensateInfo> compensateInfos = new ArrayList<CompensateInfo>();
		for(PrpLCompensateVo lCompensateVo : compeVos){
			List<PrpLPaymentVo> paymentVos = lCompensateVo.getPrpLPayments();
			if(paymentVos != null && paymentVos.size() > 0){
				String stiPolicyNo = "";
				String busiPolicyNo = "";
				if(lCompensateVo.getRiskCode().startsWith("11")){
					stiPolicyNo = lCompensateVo.getPolicyNo();
				}else{
					busiPolicyNo = lCompensateVo.getPolicyNo();
				}
				for(PrpLPaymentVo paymentVo : paymentVos){
					CompensateInfo compensateInfo = new CompensateInfo();
					PrpLPayCustomVo payCustomVo = payCustomService.findPayCustomVoById(paymentVo.getPayeeId());
					compensateInfo.setStiPolicyNo(stiPolicyNo);
					compensateInfo.setBusiPolicyNo(busiPolicyNo);
					compensateInfo.setName(payCustomVo.getPayeeName());
					compensateInfo.setExceptionsLogo(paymentVo.getOtherFlag());
					compensateInfo.setExceptionReason(paymentVo.getOtherCause());
					compensateInfo.setAccountNo(payCustomVo.getAccountNo());
					compensateInfo.setBankName(payCustomVo.getBankName());					
					if(paymentVo.getSumRealPay() != null){
						compensateInfo.setCpsMoney(paymentVo.getSumRealPay().toString());
					}
					compensateInfo.setCpsClaimType(paymentVo.getPayStatus());
					compensateInfo.setCpsTime(DateUtils.dateToStr(paymentVo.getPayTime(), DateUtils.YToSec));
					compensateInfos.add(compensateInfo);
				}
			}
		}
		if(checkDutyVo!=null){
			caseInfoVo.setDutyType(checkDutyVo.getIndemnityDuty());
		}
		List<PrpLClaimVo> prpLClaimVoList = claimService.findClaimListByRegistNo(registNo);
		String sticpsType = "5";
		String busicpsType = "5";
		if(prpLClaimVoList != null && prpLClaimVoList.size() > 0){
			for(PrpLClaimVo lClaimVo : prpLClaimVoList){
				if(lClaimVo.getRiskCode().startsWith("11")){
					if("2".equals(lClaimVo.getValidFlag())){//拒赔
						sticpsType = "2";
					}else if("0".equals(lClaimVo.getValidFlag())){
						sticpsType = "4";
					}else{
						if(StringUtils.isNotBlank(lClaimVo.getCaseNo())){//结案
							//是否0结
							if(sumpay(registNo, compeVos, "CI").doubleValue()==0.0){
								sticpsType = "3";
							}else{
								sticpsType = "1";
							}
						}
					}
					caseInfoVo.setSticpsType(sticpsType);
				}else{
					if("2".equals(lClaimVo.getValidFlag())){//拒赔
						busicpsType = "2";
					}else if("0".equals(lClaimVo.getValidFlag())){
						busicpsType = "4";
					}else{
						if(StringUtils.isNotBlank(lClaimVo.getCaseNo())){//结案
							//是否0结
							if(sumpay(registNo, compeVos, "BI").doubleValue()==0.0){
								busicpsType = "3";
							}else{
								busicpsType = "1";
							}
						}
					}
					caseInfoVo.setBusicpsType(busicpsType);
				}
			}
		}
		caseInfoVo.setDamageTime(DateUtils.dateToStr(prpLregistVo.getDamageTime(), DateUtils.YToDay));
		caseInfoVo.setReportorName(prpLregistVo.getReportorName());
		caseInfoVo.setReportorPhone(prpLregistVo.getReportorPhone());
		PrpLWfMainVo lWfMainVo = wfMainService.findPrpLWfMainVoByRegistNo(registNo);
		caseInfoVo.setCaseStatus(lWfMainVo.getLastNode());
		PrpLScheduleTaskVo selfScheduleTaskVo = scheduleTaskService.findScheduleTaskByRegistNo(registNo);
		caseInfoVo.setCaseFlag("3");//案件标示
		if("0".equals(prpLregistVo.getSelfRegistFlag()) &&
        		"1".equals(selfScheduleTaskVo.getIsAutoCheck())){//电话直赔
			caseInfoVo.setCaseFlag("1");//案件标示
        }else if("1".equals(prpLregistVo.getSelfRegistFlag()) &&
        		"1".equals(selfScheduleTaskVo.getIsAutoCheck())){//微信自助理赔
        	caseInfoVo.setCaseFlag("2");//案件标示
        }
		
		caseDetailsInfoResBodyVo.setCompensateInfos(compensateInfos);//赔付信息
		caseDetailsInfoResBodyVo.setCaseInfoVo(caseInfoVo);//案件概述
		return caseDetailsInfoResBodyVo;
	}
}
