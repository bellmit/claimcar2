package ins.sino.claimcar.carchild.check.service;

import ins.framework.lang.Springs;
import ins.platform.common.service.facade.AreaDictService;
import ins.platform.common.util.ConfigUtil;
import ins.platform.utils.DateUtils;
import ins.platform.vo.PrpLConfigValueVo;
import ins.platform.vo.SysAreaDictVo;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.carchild.check.vo.CTCarInfoVo;
import ins.sino.claimcar.carchild.check.vo.CTCheckInfoInitReqBodyVo;
import ins.sino.claimcar.carchild.check.vo.CTCheckInfoInitReqVo;
import ins.sino.claimcar.carchild.check.vo.CTCheckInfoInitResBodyVo;
import ins.sino.claimcar.carchild.check.vo.CTCheckInfoInitResVo;
import ins.sino.claimcar.carchild.check.vo.CTCheckTaskInfoVo;
import ins.sino.claimcar.carchild.check.vo.CTCheckVo;
import ins.sino.claimcar.carchild.check.vo.CTDriverVo;
import ins.sino.claimcar.carchild.check.vo.CTPersonInfoVo;
import ins.sino.claimcar.carchild.check.vo.CTPropInfoVo;
import ins.sino.claimcar.carchildCommon.vo.CarchildHeadVo;
import ins.sino.claimcar.carchildCommon.vo.CarchildResponseHeadVo;
import ins.sino.claimcar.check.service.CheckHandleService;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.CheckActionVo;
import ins.sino.claimcar.check.vo.PrpLCheckCarInfoVo;
import ins.sino.claimcar.check.vo.PrpLCheckCarVo;
import ins.sino.claimcar.check.vo.PrpLCheckDriverVo;
import ins.sino.claimcar.check.vo.PrpLCheckDutyVo;
import ins.sino.claimcar.check.vo.PrpLCheckPersonVo;
import ins.sino.claimcar.check.vo.PrpLCheckPropVo;
import ins.sino.claimcar.check.vo.PrpLCheckTaskVo;
import ins.sino.claimcar.check.vo.PrpLCheckVo;
import ins.sino.claimcar.common.losscharge.vo.PrpLDlossChargeVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.PayCustomService;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.mobile.check.vo.CheckInfoInitResVo;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.service.ScheduleTaskService;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistExtVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.schedule.service.ScheduleService;
import ins.sino.claimcar.schedule.vo.PrpLScheduleTaskVo;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.sinosoft.api.service.ServiceInterface;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;

public class CTCheckInfoServiceImpl implements ServiceInterface {
	
	private static Logger logger = LoggerFactory.getLogger(CTCheckInfoServiceImpl.class);

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
	private ScheduleService scheduleService;
	@Autowired
	AreaDictService areaDictService;
    @Autowired
    RegistQueryService registQueryService;
    @Autowired
    PolicyViewService policyViewService;
    
	public CTCheckTaskInfoVo setCheckInfo(CTCheckTaskInfoVo checkTaskInfoVo,CheckActionVo checkActionVo,PrpLWfTaskVo taskVo){

		List<CTCarInfoVo> carInfoVoList = new ArrayList<CTCarInfoVo>();
		List<CTPropInfoVo> propInfoVoList = new ArrayList<CTPropInfoVo>();
		List<CTPersonInfoVo> personInfoList = new ArrayList<CTPersonInfoVo>();
		Map<String,String> serialNoMap = new HashMap<String,String>();
		PrpLRegistExtVo registExtVo = checkActionVo.getPrpLregistVo().getPrpLRegistExt();
		PrpLCheckVo prpLCheckVo = checkActionVo.getPrpLcheckVo();
		PrpLCheckDutyVo checkDutyVo = checkActionVo.getCheckDutyVo();
		PrpLCheckTaskVo checkTaskVo = prpLCheckVo.getPrpLCheckTask();
		List<PrpLDlossChargeVo> prpLDlossChargeVos = checkActionVo.getLossChargeVo();
		PrpLScheduleTaskVo prpLScheduleTaskVo = scheduleService.getScheduleTask(taskVo.getRegistNo(), CodeConstants.ScheduleStatus.CHECK_SCHEDULED);
		//checkActionVo.getPrp
		//????????????
		if(prpLCheckVo.getId()!=null ){
			checkTaskInfoVo.setCheckId(String.valueOf(prpLCheckVo.getId()));
		}
		checkTaskInfoVo.setTaskId(prpLScheduleTaskVo.getId().toString());
		checkTaskInfoVo.setRegistNo(prpLCheckVo.getRegistNo());
		if(prpLCheckVo.getSingleAccidentFlag().equals("0")){//????????????
			checkTaskInfoVo.setIsunilAccident("0");
		}else{
			checkTaskInfoVo.setIsunilAccident("1");
		}
		checkTaskInfoVo.setDamageTypeCode(prpLCheckVo.getDamageTypeCode());
		checkTaskInfoVo.setManageTypeName(prpLCheckVo.getManageType());
		checkTaskInfoVo.setDamageCode(prpLCheckVo.getDamageCode());
		checkTaskInfoVo.setDamOtherCode(prpLCheckVo.getDamOtherCode());
		checkTaskInfoVo.setIsLoss(prpLCheckVo.getLossType());
		if("DM12".equals(prpLCheckVo.getDamageCode())){
			checkTaskInfoVo.setIsWater("1");
			checkTaskInfoVo.setWaterLevel(prpLCheckVo.getWaterLoggingLevel());
		}else{
			checkTaskInfoVo.setIsWater("0");
		}
		if("DM05".equals(prpLCheckVo.getDamageCode())){
			checkTaskInfoVo.setIsFire("1");
		}else{
			checkTaskInfoVo.setIsFire("0");
		}
		//?????????????????????????????????????????????
		PrpLConfigValueVo configValueVoByMap = ConfigUtil.findConfigByCode(CodeConstants.SWITCHMAP);
		if(configValueVoByMap!=null && "1".equals(configValueVoByMap.getConfigValue())){//????????????
			String fullName = "";
            List<SysAreaDictVo> sysAreaDictVoList = areaDictService.findAreaCode(prpLScheduleTaskVo.getRegionCode());
            if(sysAreaDictVoList != null){
                SysAreaDictVo  sysAreaDictVo = sysAreaDictVoList.get(0);
                fullName = sysAreaDictVo.getFullName();
                if(!"".equals(fullName)){
                    fullName = fullName.replaceAll("-","");
                }
            }
            checkTaskInfoVo.setCheckSite(fullName + prpLScheduleTaskVo.getCheckareaName());//
		}else{
			checkTaskInfoVo.setCheckSite(checkTaskVo.getCheckAddress());
		}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		checkTaskInfoVo.setCheckDate(DateUtils.dateToStr(checkTaskVo.getCheckDate(), DateUtils.YToSec));
		checkTaskInfoVo.setCheckType(registExtVo.getCheckType());
		checkTaskInfoVo.setFirstSiteFlag(checkTaskVo.getFirstAddressFlag());
		checkTaskInfoVo.setExcessType(prpLCheckVo.getIsClaimSelf());
		checkTaskInfoVo.setIsIncludePerson(prpLCheckVo.getIsPersonLoss());
		if(checkActionVo.getCheckPropList()!=null && checkActionVo.getCheckPropList().size()>0){
			checkTaskInfoVo.setIsIncludeProp("1");
		}else{
			checkTaskInfoVo.setIsIncludeProp("0");
		}
		
		if(checkDutyVo!=null){
			checkTaskInfoVo.setIndemnityDuty(checkDutyVo.getIndemnityDuty());
			//checkTaskInfoVo.setIndemnityDutyRate(checkDutyVo.getIndemnityDutyRate().toString());//--
			checkTaskInfoVo.setIndemnityDutyRate(checkDutyVo.getIndemnityDutyRate().toString());
		}
		//checkInfoInitResBodyVo.setIndemnityDuty(checkDutyVo.getIndemnityDuty());//--
		//checkInfoInitResBodyVo.set????????????
		if(StringUtils.isNotBlank(checkTaskVo.getClaimText())){
			checkTaskInfoVo.setBigCase(checkTaskVo.getClaimText());
		}else{
			checkTaskInfoVo.setBigCase(null);
		}
		
		checkTaskInfoVo.setIsBigCase(prpLCheckVo.getMajorCaseFlag());
		//??????
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
		checkTaskInfoVo.setTextType(checkTaskVo.getContexts());//??????????????????
		//?????????
		if(StringUtils.isNotBlank(checkTaskVo.getCheckerIdfNo())){
		    checkTaskInfoVo.setCheckIdentifyNumber(checkTaskVo.getCheckerIdfNo());
		}
		//?????????
		if(StringUtils.isNotBlank(checkTaskVo.getCheckerPhone())){
		    checkTaskInfoVo.setCheckPhoneNumber(checkTaskVo.getCheckerPhone());
        }
		
	    //???????????? ???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        List<PrpLCMainVo> policyInfo =  policyViewService.findPrpLCMainVoListByRegistNo(prpLCheckVo.getRegistNo());
        String comCode = "";
        for(PrpLCMainVo cmain : policyInfo){
            if(!"1101".equals(cmain.getRiskCode())){
                comCode = cmain.getComCode().substring(0, 2);
            }
            if("".equals(comCode)){
                comCode = cmain.getComCode().substring(0, 2);;
            }
        }
        if("22".equals(comCode)){//??????
            PrpLRegistVo registVo = registQueryService.findByRegistNo(prpLCheckVo.getRegistNo());
            if(StringUtils.isNotBlank(registVo.getAccidentReason())){
                checkTaskInfoVo.setAccidentReason(registVo.getAccidentReason());
            }else{//?????????????????????????????????????????????????????????01
                checkTaskInfoVo.setAccidentReason("01");
            } 
        }else{
            checkTaskInfoVo.setAccidentReason("");
        }
        
		
		PrpLCheckCarVo checkMainCarVo = checkActionVo.getCheckMainCarVo();
		List<PrpLCheckCarVo> checkThirdCarVos = checkActionVo.getCheckThirdCarList();
		
		//??????
		CTCarInfoVo carInfoVo = new CTCarInfoVo();
		CTDriverVo driverVo = new CTDriverVo();
		CTCheckVo checkVo = new CTCheckVo();
		PrpLCheckCarInfoVo prpLcheckMainCarVo = checkMainCarVo.getPrpLCheckCarInfo();
		PrpLCheckDriverVo prpLCheckDriverVo = checkMainCarVo.getPrpLCheckDriver();
		if(prpLcheckMainCarVo.getCarid()!=null){
			carInfoVo.setCarId(String.valueOf(prpLcheckMainCarVo.getCarid()));
		}
		carInfoVo.setIfObject("??????");
		carInfoVo.setSerialNo(String.valueOf(checkMainCarVo.getSerialNo()));
		carInfoVo.setLicenseNo(prpLcheckMainCarVo.getLicenseNo());
		carInfoVo.setLicenseType(prpLcheckMainCarVo.getLicenseType());
		if(StringUtils.isNotBlank(prpLcheckMainCarVo.getLicenseType())){
			carInfoVo.setLicenseType(prpLcheckMainCarVo.getLicenseType());
		}else{
			carInfoVo.setLicenseType("02");
		}
		carInfoVo.setEngineNo(prpLcheckMainCarVo.getEngineNo());
		carInfoVo.setFrameNo(prpLcheckMainCarVo.getFrameNo());
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
		if(prpLcheckMainCarVo.getEnrollDate()!=null){
			String enrollDate = formatter.format(prpLcheckMainCarVo.getEnrollDate());
			carInfoVo.setRegisteDate(enrollDate);  
		}
		carInfoVo.setOwern(prpLcheckMainCarVo.getCarOwner());
		carInfoVo.setLicenseColor(prpLcheckMainCarVo.getLicenseColor());
		carInfoVo.setColorCode(prpLcheckMainCarVo.getCarColorCode());
		carInfoVo.setVehiclemodleName(prpLcheckMainCarVo.getBrandName());
		if(StringUtils.isNotBlank(prpLcheckMainCarVo.getPlatformCarKindCode())){
			carInfoVo.setMotorTypeCode(prpLcheckMainCarVo.getPlatformCarKindCode());
		}else{
			carInfoVo.setMotorTypeCode("11");
		}
		
		if(prpLCheckDriverVo.getDriverId()!=null){
			driverVo.setDriverId(String.valueOf(prpLCheckDriverVo.getDriverId()));
		}
		driverVo.setCarId(String.valueOf(prpLcheckMainCarVo.getCarid()));
		driverVo.setDriverName(prpLCheckDriverVo.getDriverName());
		if(StringUtils.isNotBlank(prpLCheckDriverVo.getDriverSex())){
			driverVo.setGender(prpLCheckDriverVo.getDriverSex());
		}else{
			driverVo.setGender("1");
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
		if(prpLCheckDriverVo.getDriverValidDate()!=null){//?????????????????????
		    String identiEffecTiveDate = formatter.format(prpLCheckDriverVo.getDriverValidDate());
            driverVo.setIdentiEffecTiveDate(identiEffecTiveDate);
        }else{
            driverVo.setIdentiEffecTiveDate("");
        }
		
		carInfoVo.setDriver(driverVo);
		
		if(checkMainCarVo.getCarid()!=null){
			checkVo.setLossId(String.valueOf(checkMainCarVo.getCarid()));
		}
		checkVo.setCarId(String.valueOf(prpLcheckMainCarVo.getCarid()));
		String maineSrialNo =String.valueOf(checkMainCarVo.getSerialNo());
		String licNo = prpLcheckMainCarVo.getLicenseNo();
		serialNoMap.put(maineSrialNo,1==checkMainCarVo.getSerialNo() ? "?????????("+licNo+")" : "?????????("+licNo+")");
		serialNoMap.put("0","??????/????????????");
		checkVo.setIsLoss(checkMainCarVo.getLossFlag());
		if(checkMainCarVo.getLossFee()!=null){
			checkVo.setEstimatedLoss(checkMainCarVo.getLossFee().toString());
		}
		if(checkMainCarVo.getRescueFee() != null){
			checkVo.setRescueFee(checkMainCarVo.getRescueFee().toString());
		}
		
		//?????????duty
		PrpLCheckDutyVo checkDutyVo1=checkTaskService.findCheckDuty
				(checkMainCarVo.getRegistNo(),checkMainCarVo.getSerialNo());
		if(checkDutyVo1 != null){
			if(checkDutyVo1.getIndemnityDutyRate()!=null && checkDutyVo1.getIndemnityDutyRate()!=null){
				checkVo.setIndemnityDuty(checkDutyVo1.getIndemnityDuty());
				checkVo.setIndemnityDutyRate(checkDutyVo1.getIndemnityDutyRate().toString());//??????????????????
			}
		}
		
		checkVo.setKindCode(checkMainCarVo.getKindCode());
		checkVo.setLossPart(checkMainCarVo.getLossPart());
		carInfoVo.setCheck(checkVo);
		carInfoVoList.add(carInfoVo);
		//??????
		if(checkThirdCarVos != null && checkThirdCarVos.size()>0){
			for(PrpLCheckCarVo vo : checkThirdCarVos){
				CTCarInfoVo carThirdInfoVo = new CTCarInfoVo();
				CTDriverVo driverThirdVo = new CTDriverVo();
				CTCheckVo checkThirdVo = new CTCheckVo();
				PrpLCheckCarInfoVo prpLcheckThirdCarVo = vo.getPrpLCheckCarInfo();
				PrpLCheckDriverVo prpLCheckThirdDriverVo = vo.getPrpLCheckDriver();
				carThirdInfoVo.setIfObject("??????");
				if(prpLcheckThirdCarVo.getCarid()!=null){
					carThirdInfoVo.setCarId(String.valueOf(prpLcheckThirdCarVo.getCarid()));
				}
				carThirdInfoVo.setSerialNo(String.valueOf(vo.getSerialNo()));
				String thirdSerialNo = vo.getSerialNo().toString();
				String thirdLicNo = vo.getPrpLCheckCarInfo().getLicenseNo();
				serialNoMap.put(thirdSerialNo,1==vo.getSerialNo() ? "?????????("+thirdLicNo+")" : "?????????("+thirdLicNo+")");
				
					
				carThirdInfoVo.setLicenseNo(prpLcheckThirdCarVo.getLicenseNo());
				carThirdInfoVo.setLicenseType(prpLcheckThirdCarVo.getLicenseType());
				carThirdInfoVo.setEngineNo(prpLcheckThirdCarVo.getEngineNo());
				carThirdInfoVo.setFrameNo(prpLcheckThirdCarVo.getFrameNo());
				carThirdInfoVo.setVin(prpLcheckThirdCarVo.getVinNo());
				if(StringUtils.isNotBlank(prpLcheckThirdCarVo.getBiInsureComCode())){
					carThirdInfoVo.setInsurComcode(prpLcheckThirdCarVo.getBiInsureComCode());
				}else if(StringUtils.isNotBlank(prpLcheckThirdCarVo.getCiInsureComCode())){
					carThirdInfoVo.setInsurComcode(prpLcheckThirdCarVo.getCiInsureComCode());
				}
				if(StringUtils.isNotBlank(prpLcheckThirdCarVo.getPlatformCarKindCode())){
					carThirdInfoVo.setMotorTypeCode(prpLcheckThirdCarVo.getPlatformCarKindCode());
				}else{
					carThirdInfoVo.setMotorTypeCode("11");
				}
				//carThirdInfoVo.setInsurComcode(vo.getPrpLCheckCarInfo().getInsurecomcode());
				/*String enrollDates= formatter.format(vo.getPrpLCheckCarInfo().getEnrollDate());
				carThirdInfoVo.setRegisteDate(enrollDates);  */
				if(vo.getPrpLCheckCarInfo().getEnrollDate()!=null){
					String enrollDates= formatter.format(prpLcheckThirdCarVo.getEnrollDate());
					carThirdInfoVo.setRegisteDate(enrollDates); 
				}
				carThirdInfoVo.setOwern(prpLcheckThirdCarVo.getCarOwner());
				carThirdInfoVo.setLicenseColor(prpLcheckThirdCarVo.getLicenseColor());
				carThirdInfoVo.setColorCode(prpLcheckThirdCarVo.getCarColorCode());
				carThirdInfoVo.setVehiclemodleName(prpLcheckThirdCarVo.getBrandName());
				
				if(prpLCheckThirdDriverVo.getDriverId()!=null){
					driverThirdVo.setDriverId(String.valueOf(prpLCheckThirdDriverVo.getDriverId()));
				}
				driverThirdVo.setCarId(String.valueOf(prpLcheckThirdCarVo.getCarid()));
				driverThirdVo.setDriverName(prpLCheckThirdDriverVo.getDriverName());
				driverThirdVo.setGender(prpLCheckThirdDriverVo.getDriverSex());
				driverThirdVo.setDrivingCarType(prpLCheckThirdDriverVo.getDrivingCarType());
				driverThirdVo.setCertiType(prpLCheckThirdDriverVo.getIdentifyType());
				driverThirdVo.setIdentifyNumber(prpLCheckThirdDriverVo.getIdentifyNumber());
				driverThirdVo.setDrivingLicenseNo(prpLCheckThirdDriverVo.getDrivingLicenseNo());
				driverThirdVo.setPhoneNumber(prpLCheckThirdDriverVo.getLinkPhoneNumber());
				/*String acceptLicenseDates = formatter.format(vo.getPrpLCheckDriver().getAcceptLicenseDate());
				driverThirdVo.setLicenseDate(acceptLicenseDates);*/
				if(prpLCheckThirdDriverVo.getAcceptLicenseDate()!=null){
					String acceptLicenseDates = formatter.format(prpLCheckThirdDriverVo.getAcceptLicenseDate());
					driverThirdVo.setLicenseDate(acceptLicenseDates);
				}
				if(prpLCheckThirdDriverVo.getDriverAge()!=null){
				    driverThirdVo.setAge(String.valueOf(prpLCheckThirdDriverVo.getDriverAge()));
		        }else{
		            driverThirdVo.setAge("");
		        }
		        if(prpLCheckThirdDriverVo.getDriverValidDate()!=null){//???????????????????????????
		            String identiEffecTiveDate = formatter.format(prpLCheckThirdDriverVo.getDriverValidDate());
		            driverThirdVo.setIdentiEffecTiveDate(identiEffecTiveDate);
		        }else{
		            driverThirdVo.setIdentiEffecTiveDate("");
		        }
				carThirdInfoVo.setDriver(driverThirdVo);
				
				if(vo.getCarid()!=null){
					checkThirdVo.setLossId(String.valueOf(vo.getCarid()));
				}
				checkThirdVo.setCarId(String.valueOf(prpLcheckThirdCarVo.getCarid()));
				checkThirdVo.setIsLoss(vo.getLossFlag());
				if(vo.getLossFee()!=null){
					checkThirdVo.setEstimatedLoss(vo.getLossFee().toString());
				}
				if(vo.getRescueFee() != null){
					checkVo.setRescueFee(vo.getRescueFee().toString());
				}
				
				//?????????duty
				PrpLCheckDutyVo checkDutyVos=checkTaskService.findCheckDuty
						(vo.getRegistNo(),vo.getSerialNo());
				if(checkDutyVos != null){
					if(checkDutyVos.getIndemnityDutyRate()!=null && checkDutyVos.getIndemnityDutyRate()!=null){
						checkThirdVo.setIndemnityDuty(checkDutyVos.getIndemnityDuty());
						checkThirdVo.setIndemnityDutyRate(checkDutyVos.getIndemnityDutyRate().toString());//??????????????????
					}
				}
				checkThirdVo.setKindCode(vo.getKindCode());
				checkThirdVo.setLossPart(vo.getLossPart());
				carThirdInfoVo.setCheck(checkThirdVo);
				carInfoVoList.add(carThirdInfoVo);
			}
		}
		checkTaskInfoVo.setCarInfo(carInfoVoList);
		
		
		
		//????????????
		
		List<PrpLCheckPropVo> checkPropVos = checkActionVo.getCheckPropList();
		if(checkPropVos!=null && checkPropVos.size()>0){
			for(PrpLCheckPropVo vo :checkPropVos){
				CTPropInfoVo propInfoVo = new CTPropInfoVo();
				if(vo.getId()!=null){
					propInfoVo.setPropId(String.valueOf(vo.getId()));
				}
				if(vo.getLossPartyName().contains("??????") || vo.getLossPartyName().contains("????????????") || 
						vo.getLossPartyName().contains("???")){
					propInfoVo.setLossType(vo.getLossPartyName());
				}else{
					if(registExtVo.getLicenseNo().equals(vo.getLossPartyName())){
						propInfoVo.setLossType("?????????("+vo.getLossPartyName()+")");
					}else{
						propInfoVo.setLossType("?????????("+vo.getLossPartyName()+")");
					}
				}
				
				propInfoVo.setLossName(vo.getLossItemName());
				propInfoVo.setLossNum(vo.getLossNum());
				propInfoVo.setUnit(vo.getLossFeeType());
				propInfoVo.setIsNoClaim("1".equals(vo.getIsNoClaim()) ?"0":"1");
				propInfoVo.setLossDegreeCode(vo.getLossDegreeCode());
            	if(vo.getLossFee()!=null){
            		propInfoVo.setPayAmount(vo.getLossFee().toString());
            	}
				propInfoVoList.add(propInfoVo);
			}
		}
		checkTaskInfoVo.setPropInfo(propInfoVoList);
		List<PrpLCheckPersonVo> prpLCheckPersonVos = checkActionVo.getCheckPersonList();
		//??????
		for(PrpLCheckPersonVo vo:prpLCheckPersonVos){
			CTPersonInfoVo personInfoVo = new CTPersonInfoVo();
			if(vo.getId()!=null){
				personInfoVo.setPersonId(String.valueOf(vo.getId()));
			}
			if(StringUtils.isNotBlank(prpLCheckVo.getReconcileFlag())){
				personInfoVo.setIsAdjust(prpLCheckVo.getReconcileFlag());
			}else{
				personInfoVo.setIsAdjust("0");
			}
			personInfoVo.setLosstype(vo.getLossPartyName());
			personInfoVo.setName(vo.getPersonName());
			personInfoVo.setSex(vo.getPersonSex());
			personInfoVo.setPersonAttributes(vo.getPersonProp());
			personInfoVo.setCertiType(vo.getIdentifyType());
			personInfoVo.setIdentifyNumber(vo.getIdNo());
			if(vo.getPersonAge()!=null){
				personInfoVo.setAge(vo.getPersonAge().toString());
			}
			personInfoVo.setDegree(vo.getPersonPayType());
			personInfoVo.setSurveyType(prpLCheckVo.getCheckType());
			personInfoVo.setIndustry(vo.getTicCode());
			if(StringUtils.isNotBlank(vo.getHospital())){
			    personInfoVo.setTherapeuticagency(vo.getHospital());
			}else{
			    personInfoVo.setTherapeuticagency("");
			}
			if(vo.getLossFee() != null){
				personInfoVo.setSumClaim(vo.getLossFee().toString());
			}
			personInfoVo.setDegreedesc(vo.getWoundDetail());
			personInfoVo.setInjuredPart(vo.getInjuredPart());
			personInfoList.add(personInfoVo);
		}
		checkTaskInfoVo.setPersonInfo(personInfoList);
		
		
		return checkTaskInfoVo;
		
	}

	@Override
	public CTCheckInfoInitResVo service(String arg0, Object arg1) {
		// TODO Auto-generated method stub
		init();
		CTCheckInfoInitResVo resVo = new CTCheckInfoInitResVo();
		CTCheckInfoInitResBodyVo checkInfoInitResBodyVo = new CTCheckInfoInitResBodyVo();
		CTCheckTaskInfoVo checkTaskInfoVo = new CTCheckTaskInfoVo();
		
		CarchildResponseHeadVo head = new CarchildResponseHeadVo();
		CarchildHeadVo carchildHeadVo = new CarchildHeadVo();
		XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
		stream.autodetectAnnotations(true);
		stream.setMode(XStream.NO_REFERENCES);
		stream.aliasSystemAttribute(null,"class");// ?????? class??????
		String registNo = "";
		String nodeType="";//????????????
		try{
			stream.processAnnotations(CTCheckInfoInitResVo.class);
			CTCheckInfoInitReqVo checkInfoInitReqVo = (CTCheckInfoInitReqVo) arg1;
			String xml = stream.toXML(checkInfoInitReqVo);
			logger.info("??????????????????????????????????????????: \n"+xml);
			CTCheckInfoInitReqBodyVo checkInfoInitReqBodyVo = checkInfoInitReqVo.getBody();
			carchildHeadVo = checkInfoInitReqVo.getHead();
			if (!"CT_003".equals(carchildHeadVo.getRequestType())|| !"claim_user".equals(carchildHeadVo.getUser())|| !"claim_psd".equals(carchildHeadVo.getPassWord())) {
				throw new IllegalArgumentException(" ?????????????????????  ");
			}
			if(StringUtils.isBlank(xml)){
				throw new IllegalArgumentException("????????????");
			}
			if(!StringUtils.isNotBlank(carchildHeadVo.getRequestType())){
				throw new IllegalArgumentException("????????????????????????");
			}
			if(!StringUtils.isNotBlank(carchildHeadVo.getRequestType())){
				throw new IllegalArgumentException("?????????????????????");		
			}
			if(!StringUtils.isNotBlank(carchildHeadVo.getRequestType())){
				throw new IllegalArgumentException("??????????????????");
			}
			if(!StringUtils.isNotBlank(checkInfoInitReqBodyVo.getRegistNo())){
				throw new IllegalArgumentException("?????????????????????");
			}
			registNo = checkInfoInitReqBodyVo.getRegistNo();
			nodeType=checkInfoInitReqBodyVo.getNodeType();
			if(!StringUtils.isNotBlank(checkInfoInitReqBodyVo.getNodeType())){
				throw new IllegalArgumentException("??????????????????????????????");
			}
			if(!StringUtils.isNotBlank(checkInfoInitReqBodyVo.getTaskId())){
				throw new IllegalArgumentException("??????ID????????????");
			}
			if(!StringUtils.isNotBlank(checkInfoInitReqBodyVo.getHandlerCode())){
				throw new IllegalArgumentException("???????????????????????????");
			}
			if(!StringUtils.isNotBlank(checkInfoInitReqBodyVo.getScheduleObjectId())){
				throw new IllegalArgumentException("??????????????????????????????????????????");
			}
			
			setCheckTaskInfoVo(checkTaskInfoVo, checkInfoInitReqBodyVo);
			
			List<PrpLWfTaskVo> prpLWfTaskVoList = wfTaskHandleService.findWftaskByRegistNoAndNodeCode(checkInfoInitReqBodyVo.getRegistNo(), FlowNode.Chk.toString());
			if (prpLWfTaskVoList == null || prpLWfTaskVoList.isEmpty()){
				throw new IllegalArgumentException("???????????????????????????");
			}
			//?????????????????????
			Collections.sort(prpLWfTaskVoList, new Comparator<PrpLWfTaskVo>() {
			@Override
			public int compare(PrpLWfTaskVo o1,PrpLWfTaskVo o2) {
					return o2.getTaskInTime().compareTo(o1.getTaskInTime());
				}
			});
			PrpLWfTaskVo taskVo = prpLWfTaskVoList.get(0);
			
			if("0".equals(taskVo.getHandlerStatus())){//?????????
				Long  scheduleTaskId=Long.parseLong(taskVo.getHandlerIdKey());
				// ???????????????
				SysUserVo userVo = new SysUserVo();
				userVo = scheduleTaskService.findPrpduserByUserCode(checkInfoInitReqBodyVo.getHandlerCode(), "1");
				userVo.setComCode(checkInfoInitReqBodyVo.getScheduleObjectId());
				userVo.setUserCode(checkInfoInitReqBodyVo.getHandlerCode());
				userVo.setUserName(checkInfoInitReqBodyVo.getHandlerName());
				userVo.setComName(checkInfoInitReqBodyVo.getScheduleObjectName());
				CheckActionVo checkActionVo = checkHandleService.initCheckBySchedule
						(scheduleTaskId,checkInfoInitReqBodyVo.getRegistNo(),userVo);
				
				// ??????
				Long checkId = checkHandleService.saveCheckOnAccept(checkActionVo,userVo);
				System.out.println(checkId);
				wfTaskHandleService.tempSaveTask(taskVo.getTaskId().doubleValue(),checkId.toString(),userVo.getUserCode(),userVo.getComCode());
				
				checkActionVo = checkHandleService.initCheckByCheck(checkId);
				checkTaskInfoVo = this.setCheckInfo(checkTaskInfoVo, checkActionVo,taskVo);
				checkInfoInitResBodyVo.setCheckTaskInfo(checkTaskInfoVo);
			}else{//?????????????????????
					Long checkTaskId = Long.parseLong(taskVo.getHandlerIdKey());
					// ???????????????
					CheckActionVo checkActionVo = checkHandleService.initCheckByCheck(checkTaskId);
					checkTaskInfoVo = this.setCheckInfo(checkTaskInfoVo, checkActionVo,taskVo);
					checkInfoInitResBodyVo.setCheckTaskInfo(checkTaskInfoVo);
			
				}
				head.setResponseType(carchildHeadVo.getRequestType());
				head.setErrNo("1");
				head.setErrMsg("Success");
				resVo.setHead(head);
				resVo.setBody(checkInfoInitResBodyVo);
		}catch(Exception e){
			head.setResponseType(carchildHeadVo.getRequestType());
			head.setErrNo("0");
			head.setErrMsg(e.getMessage());
			resVo.setHead(head);
			logger.info("?????????????????????????????????????????????\n");
			e.printStackTrace();
		}
		stream.processAnnotations(CheckInfoInitResVo.class);
		logger.info("??????????????????????????????????????????=========???\n"+stream.toXML(resVo));
		return resVo;
	}
	
	private void init() {
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
		if(scheduleService==null){
			scheduleService = (ScheduleService)Springs.getBean(ScheduleService.class);
		}
		if(areaDictService==null){
			areaDictService = (AreaDictService)Springs.getBean(AreaDictService.class);
		}
        if(registQueryService==null){
            registQueryService = (RegistQueryService)Springs.getBean(RegistQueryService.class);
        }
        if(policyViewService==null){
            policyViewService = (PolicyViewService)Springs.getBean(PolicyViewService.class);
        }
	}

	private CTCheckTaskInfoVo setCheckTaskInfoVo(CTCheckTaskInfoVo checkTaskInfoVo,CTCheckInfoInitReqBodyVo checkInfoInitReqBodyVo){
		if(StringUtils.isNotBlank(checkInfoInitReqBodyVo.getTaskId())){
			checkTaskInfoVo.setTaskId(checkInfoInitReqBodyVo.getTaskId());
		}else{
			checkTaskInfoVo.setTaskId(null);
		}
		if(StringUtils.isNotBlank(checkInfoInitReqBodyVo.getNodeType())){
			checkTaskInfoVo.setNodeType(checkInfoInitReqBodyVo.getNodeType());
		}else{
			checkTaskInfoVo.setNodeType(null);
		}
		checkTaskInfoVo.setItemNo("1");
		//TODO ItemnoName???????????????
		checkTaskInfoVo.setItemnoName("");
		return checkTaskInfoVo;
		
	}
	
}
