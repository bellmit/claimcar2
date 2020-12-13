package ins.sino.claimcar.carchild.check.service;


import ins.framework.lang.Springs;
import ins.platform.common.service.facade.AreaDictService;
import ins.platform.common.util.ConfigUtil;
import ins.platform.utils.DateUtils;
import ins.platform.vo.PrpLConfigValueVo;
import ins.platform.vo.SysAreaDictVo;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
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
import ins.sino.claimcar.mtainterface.check.vo.MTACarInfoVo;
import ins.sino.claimcar.mtainterface.check.vo.MTACheckInfoInitReqBodyVo;
import ins.sino.claimcar.mtainterface.check.vo.MTACheckInfoInitReqVo;
import ins.sino.claimcar.mtainterface.check.vo.MTACheckInfoInitResBodyVo;
import ins.sino.claimcar.mtainterface.check.vo.MTACheckInfoInitResVo;
import ins.sino.claimcar.mtainterface.check.vo.MTACheckTaskInfoVo;
import ins.sino.claimcar.mtainterface.check.vo.MTACheckVo;
import ins.sino.claimcar.mtainterface.check.vo.MTADriverVo;
import ins.sino.claimcar.mtainterface.check.vo.MTAPersonInfoVo;
import ins.sino.claimcar.mtainterface.check.vo.MTAPropInfoVo;
import ins.sino.claimcar.mtainterface.check.vo.MTASubrogationInfoVo;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.service.ScheduleTaskService;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistExtVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.schedule.service.ScheduleService;
import ins.sino.claimcar.schedule.vo.PrpLScheduleTaskVo;
import ins.sino.claimcar.subrogation.vo.PrpLSubrogationCarVo;
import ins.sino.claimcar.subrogation.vo.PrpLSubrogationPersonVo;

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

public class MTACheckInfoServiceImpl  implements ServiceInterface{

	private static Logger logger = LoggerFactory.getLogger(MTACheckInfoServiceImpl.class);
	
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
    
	public MTACheckTaskInfoVo setCheckInfo(MTACheckInfoInitResBodyVo checkInfoInitResBodyVo,MTACheckTaskInfoVo checkTaskInfo,CheckActionVo checkActionVo,PrpLWfTaskVo taskVo){

		List<MTACarInfoVo> carInfoVoList = new ArrayList<MTACarInfoVo>();
		List<MTAPropInfoVo> propInfoVoList = new ArrayList<MTAPropInfoVo>();
		List<MTAPersonInfoVo> personInfoList = new ArrayList<MTAPersonInfoVo>();
		List<MTASubrogationInfoVo> subrogationInfoList = new ArrayList<MTASubrogationInfoVo>();
		Map<String,String> serialNoMap = new HashMap<String,String>();
		PrpLRegistExtVo registExtVo = checkActionVo.getPrpLregistVo().getPrpLRegistExt();
		PrpLCheckVo prpLCheckVo = checkActionVo.getPrpLcheckVo();
		PrpLCheckDutyVo checkDutyVo = checkActionVo.getCheckDutyVo();
		PrpLCheckTaskVo checkTaskVo = prpLCheckVo.getPrpLCheckTask();
		List<PrpLDlossChargeVo> prpLDlossChargeVos = checkActionVo.getLossChargeVo();
		PrpLScheduleTaskVo prpLScheduleTaskVo = scheduleService.getScheduleTask(taskVo.getRegistNo(), CodeConstants.ScheduleStatus.CHECK_SCHEDULED);
		//checkActionVo.getPrp
		//查勘记录
		if(prpLCheckVo.getId()!=null ){
			checkTaskInfo.setCheckId(String.valueOf(prpLCheckVo.getId()));
		}
		checkTaskInfo.setTaskId(prpLScheduleTaskVo.getId().toString());
		checkTaskInfo.setRegistNo(prpLCheckVo.getRegistNo());
		if(prpLCheckVo.getSingleAccidentFlag().equals("0")){//不是单车
			checkTaskInfo.setIsunilAccident("0");
		}else{
			checkTaskInfo.setIsunilAccident("1");
		}
		checkTaskInfo.setDamageTypeCode(prpLCheckVo.getDamageTypeCode());
		checkTaskInfo.setManageTypeName(prpLCheckVo.getManageType());
		checkTaskInfo.setDamageCode(prpLCheckVo.getDamageCode());
		checkTaskInfo.setDamOtherCode(prpLCheckVo.getDamOtherCode());
		checkTaskInfo.setIsLoss(prpLCheckVo.getLossType());
		if("DM12".equals(prpLCheckVo.getDamageCode())){
			checkTaskInfo.setIsWater("1");
			checkTaskInfo.setWaterLevel(prpLCheckVo.getWaterLoggingLevel());
		}else{
			checkTaskInfo.setIsWater("0");
		}
		if("DM05".equals(prpLCheckVo.getDamageCode())){
			checkTaskInfo.setIsFire("1");
		}else{
			checkTaskInfo.setIsFire("0");
		}
		//如果电子地图关闭要组织查勘地址
		PrpLConfigValueVo configValueVoByMap = ConfigUtil.findConfigByCode(CodeConstants.SWITCHMAP);
		if(configValueVoByMap!=null && "1".equals(configValueVoByMap.getConfigValue())){//地图关闭
			String fullName = "";
            List<SysAreaDictVo> sysAreaDictVoList = areaDictService.findAreaCode(prpLScheduleTaskVo.getRegionCode());
            if(sysAreaDictVoList != null){
                SysAreaDictVo  sysAreaDictVo = sysAreaDictVoList.get(0);
                fullName = sysAreaDictVo.getFullName();
                if(!"".equals(fullName)){
                    fullName = fullName.replaceAll("-","");
                }
            }
            checkTaskInfo.setCheckSite(fullName + prpLScheduleTaskVo.getCheckareaName());//
		}else{
			checkTaskInfo.setCheckSite(checkTaskVo.getCheckAddress());
		}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		checkTaskInfo.setCheckDate(DateUtils.dateToStr(checkTaskVo.getCheckDate(), DateUtils.YToSec));
		checkTaskInfo.setCheckType(registExtVo.getCheckType());
		checkTaskInfo.setFirstSiteFlag(checkTaskVo.getFirstAddressFlag());
		checkTaskInfo.setExcessType(prpLCheckVo.getIsClaimSelf());
		checkTaskInfo.setIsIncludePerson(prpLCheckVo.getIsPersonLoss());
		if(checkActionVo.getCheckPropList()!=null && checkActionVo.getCheckPropList().size()>0){
			checkTaskInfo.setIsIncludeProp("1");
		}else{
			checkTaskInfo.setIsIncludeProp("0");
		}
		
		if(checkDutyVo!=null){
			checkTaskInfo.setIndemnityDuty(checkDutyVo.getIndemnityDuty());
			//checkTaskInfoVo.setIndemnityDutyRate(checkDutyVo.getIndemnityDutyRate().toString());//--
			checkTaskInfo.setIndemnityDutyRate(checkDutyVo.getIndemnityDuty());
		}
		//checkTaskInfo.setIndemnityDuty(checkDutyVo.getIndemnityDuty());//--
		//checkTaskInfo.set事故责任
		if(StringUtils.isNotBlank(checkTaskVo.getClaimText())){
			checkTaskInfo.setBigCase(checkTaskVo.getClaimText());
		}else{
			checkTaskInfo.setBigCase(null);
		}
		
		checkTaskInfo.setIsBigCase(prpLCheckVo.getMajorCaseFlag());
		//费用
		BigDecimal checkFree = new BigDecimal(0);
		if(prpLDlossChargeVos!=null && prpLDlossChargeVos.size()>0){
			for(PrpLDlossChargeVo vo :prpLDlossChargeVos){
				if(vo.getChargeFee()!=null){
					checkFree = checkFree.add(vo.getChargeFee());
				}
			}
		}
		checkTaskInfo.setCheckFee(checkFree.toString());
		checkTaskInfo.setSubrogateType(prpLCheckVo.getIsSubRogation());
		checkTaskInfo.setTextType(checkTaskVo.getContexts());//本次查勘报告
		//身份证
		if(StringUtils.isNotBlank(checkTaskVo.getCheckerIdfNo())){
			checkTaskInfo.setCheckIdentifyNumber(checkTaskVo.getCheckerIdfNo());
		}
		//手机号
		if(StringUtils.isNotBlank(checkTaskVo.getCheckerPhone())){
			checkTaskInfo.setCheckPhoneNumber(checkTaskVo.getCheckerPhone());
        }
		
	    //事故原因 查勘案件为上海案件时，希望移动查勘的查勘任务处理界面需增加录入事故原因，案件为上海承保保单出险时控制必录。
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
        if("22".equals(comCode)){//上海
            PrpLRegistVo registVo = registQueryService.findByRegistNo(prpLCheckVo.getRegistNo());
            if(StringUtils.isNotBlank(registVo.getAccidentReason())){
                checkTaskInfo.setAccidentReason(registVo.getAccidentReason());
            }else{//报案界面没有管控必录，这里为空取默认值01
                checkTaskInfo.setAccidentReason("01");
            } 
        }else{
            checkTaskInfo.setAccidentReason("");
        }
        
		PrpLCheckCarVo checkMainCarVo = checkActionVo.getCheckMainCarVo();
		List<PrpLCheckCarVo> checkThirdCarVos = checkActionVo.getCheckThirdCarList();
		
		//标的
		MTACarInfoVo carInfoVo = new MTACarInfoVo();
		MTADriverVo driverVo = new MTADriverVo();
		MTACheckVo checkVo = new MTACheckVo();
		PrpLCheckCarInfoVo prpLcheckMainCarVo = checkMainCarVo.getPrpLCheckCarInfo();
		PrpLCheckDriverVo prpLCheckDriverVo = checkMainCarVo.getPrpLCheckDriver();
		if(prpLcheckMainCarVo.getCarid()!=null){
			carInfoVo.setCarId(String.valueOf(prpLcheckMainCarVo.getCarid()));
		}
		carInfoVo.setIfObject("标的");
		carInfoVo.setSerialNo(String.valueOf(checkMainCarVo.getSerialNo()));
		carInfoVo.setLicenseNo(prpLcheckMainCarVo.getLicenseNo());
		carInfoVo.setLicenseType(prpLcheckMainCarVo.getLicenseType());
		if(StringUtils.isNotBlank(prpLcheckMainCarVo.getPlatformCarKindCode())){
			carInfoVo.setMotorTypeCode(prpLcheckMainCarVo.getPlatformCarKindCode());
		}else{
			carInfoVo.setMotorTypeCode("11");
		}
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
		if(prpLCheckDriverVo.getDriverValidDate()!=null){//驾驶证有效日期
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
		serialNoMap.put(maineSrialNo,1==checkMainCarVo.getSerialNo() ? "标的车("+licNo+")" : "三者车("+licNo+")");
		serialNoMap.put("0","地面/路人损失");
		checkVo.setIsLoss(checkMainCarVo.getLossFlag());
		if(checkMainCarVo.getLossFee()!=null){
			checkVo.setEstimatedLoss(checkMainCarVo.getLossFee().toString());
		}
		if(checkMainCarVo.getRescueFee() != null){
			checkVo.setRescueFee(checkMainCarVo.getRescueFee().toString());
		}
		
		//初始化duty
		PrpLCheckDutyVo checkDutyVo1=checkTaskService.findCheckDuty
				(checkMainCarVo.getRegistNo(),checkMainCarVo.getSerialNo());
		if(checkDutyVo1 != null){
			if(checkDutyVo1.getIndemnityDutyRate()!=null && checkDutyVo1.getIndemnityDutyRate()!=null){
				checkVo.setIndemnityDuty(checkDutyVo1.getIndemnityDuty());
				checkVo.setIndemnityDutyRate(checkDutyVo1.getIndemnityDuty());//添加责任比例
			}
		}
		
		checkVo.setKindCode(checkMainCarVo.getKindCode());
		checkVo.setLossPart(checkMainCarVo.getLossPart());
		carInfoVo.setCheck(checkVo);
		carInfoVoList.add(carInfoVo);
		//三者
		if(checkThirdCarVos != null && checkThirdCarVos.size()>0){
			for(PrpLCheckCarVo vo : checkThirdCarVos){
				MTACarInfoVo carThirdInfoVo = new MTACarInfoVo();
				MTADriverVo driverThirdVo = new MTADriverVo();
				MTACheckVo checkThirdVo = new MTACheckVo();
				PrpLCheckCarInfoVo prpLcheckThirdCarVo = vo.getPrpLCheckCarInfo();
				PrpLCheckDriverVo prpLCheckThirdDriverVo = vo.getPrpLCheckDriver();
				carThirdInfoVo.setIfObject("三者");
				if(prpLcheckThirdCarVo.getCarid()!=null){
					carThirdInfoVo.setCarId(String.valueOf(prpLcheckThirdCarVo.getCarid()));
				}
				carThirdInfoVo.setSerialNo(String.valueOf(vo.getSerialNo()));
				String thirdSerialNo = vo.getSerialNo().toString();
				String thirdLicNo = vo.getPrpLCheckCarInfo().getLicenseNo();
				serialNoMap.put(thirdSerialNo,1==vo.getSerialNo() ? "标的车("+thirdLicNo+")" : "三者车("+thirdLicNo+")");
				
					
				carThirdInfoVo.setLicenseNo(prpLcheckThirdCarVo.getLicenseNo());
				carThirdInfoVo.setLicenseType(prpLcheckThirdCarVo.getLicenseType());
				carThirdInfoVo.setEngineNo(prpLcheckThirdCarVo.getEngineNo());
				carThirdInfoVo.setFrameNo(prpLcheckThirdCarVo.getFrameNo());
				carThirdInfoVo.setVin(prpLcheckThirdCarVo.getVinNo());
				if(StringUtils.isNotBlank(prpLcheckThirdCarVo.getPlatformCarKindCode())){
					carThirdInfoVo.setMotorTypeCode(prpLcheckThirdCarVo.getPlatformCarKindCode());
				}else {
					carThirdInfoVo.setMotorTypeCode("11");
				}
				if(StringUtils.isNotBlank(prpLcheckThirdCarVo.getBiInsureComCode())){
					carThirdInfoVo.setInsurComcode(prpLcheckThirdCarVo.getBiInsureComCode());
				}else if(StringUtils.isNotBlank(prpLcheckThirdCarVo.getCiInsureComCode())){
					carThirdInfoVo.setInsurComcode(prpLcheckThirdCarVo.getCiInsureComCode());
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
		        if(prpLCheckThirdDriverVo.getDriverValidDate()!=null){//三者驾驶证有效日期
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
				
				//初始化duty
				PrpLCheckDutyVo checkDutyVos=checkTaskService.findCheckDuty
						(vo.getRegistNo(),vo.getSerialNo());
				if(checkDutyVos != null){
					if(checkDutyVos.getIndemnityDutyRate()!=null && checkDutyVos.getIndemnityDutyRate()!=null){
						checkThirdVo.setIndemnityDuty(checkDutyVos.getIndemnityDuty());
						checkThirdVo.setIndemnityDutyRate(checkDutyVos.getIndemnityDuty());//添加责任比例
					}
				}
				checkThirdVo.setKindCode(vo.getKindCode());
				checkThirdVo.setLossPart(vo.getLossPart());
				carThirdInfoVo.setCheck(checkThirdVo);
				carInfoVoList.add(carThirdInfoVo);
			}
		}
		checkTaskInfo.setCarInfo(carInfoVoList);
		
		
		
		//物损信息
		
		List<PrpLCheckPropVo> checkPropVos = checkActionVo.getCheckPropList();
		if(checkPropVos!=null && checkPropVos.size()>0){
			for(PrpLCheckPropVo vo :checkPropVos){
				MTAPropInfoVo propInfoVo = new MTAPropInfoVo();
				if(vo.getId()!=null){
					propInfoVo.setPropId(String.valueOf(vo.getId()));
				}
				if(vo.getLossPartyName().contains("地面") || vo.getLossPartyName().contains("路人损失") || 
						vo.getLossPartyName().contains("车")){
					propInfoVo.setLossType(vo.getLossPartyName());
				}else{
					if(registExtVo.getLicenseNo().equals(vo.getLossPartyName())){
						propInfoVo.setLossType("标的车("+vo.getLossPartyName()+")");
					}else{
						propInfoVo.setLossType("三者车("+vo.getLossPartyName()+")");
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
		checkTaskInfo.setPropInfo(propInfoVoList);
		
		//人伤
		List<PrpLCheckPersonVo> prpLCheckPersonVos = checkActionVo.getCheckPersonList();
		for(PrpLCheckPersonVo vo:prpLCheckPersonVos){
			MTAPersonInfoVo personInfoVo = new MTAPersonInfoVo();
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
		checkTaskInfo.setPersonInfo(personInfoList);
		
		//代位求偿
		if("1".equals(prpLCheckVo.getIsSubRogation()) && checkActionVo.getSubrogationMainVo()!=null){
			
			List<PrpLSubrogationCarVo> subrogationCarList = checkActionVo.getSubrogationMainVo().getPrpLSubrogationCars();
			List<PrpLSubrogationPersonVo> prpLSubrogationPersonList = checkActionVo.getSubrogationMainVo().getPrpLSubrogationPersons();
			//车辆代位
			if(subrogationCarList!=null && subrogationCarList.size()>0){
				for(PrpLSubrogationCarVo subrogtionCar:subrogationCarList){
					MTASubrogationInfoVo subrogationVo = new MTASubrogationInfoVo();
					subrogationVo.setCreateTime(DateUtils.dateToStr(subrogtionCar.getCreateTime(), DateUtils.YToSec));
					subrogationVo.setSubrogationType("0");
					subrogationVo.setCreateUser(subrogtionCar.getCreateUser());
					subrogationVo.setSerialNo(subrogtionCar.getSerialNo().toString());
					subrogationVo.setLinkerName(subrogtionCar.getLinkerName());
					subrogationVo.setLicenseNo(subrogtionCar.getLicenseNo());
					subrogationVo.setLicenseType(subrogtionCar.getLicenseType());
					subrogationVo.setVinNo(subrogtionCar.getVinNo());
					subrogationVo.setEngineNo(subrogtionCar.getEngineNo());
					subrogationVo.setBiInsurerCode(subrogtionCar.getBiInsurerCode());
					subrogationVo.setBiInsurerArea(subrogtionCar.getBiInsurerArea());
					subrogationVo.setCiInsurerArea(subrogtionCar.getCiInsurerArea());
					subrogationVo.setCiInsurerCode(subrogtionCar.getCiInsurerCode());
					subrogationInfoList.add(subrogationVo);
				}
			}
			//非车代位
			if(prpLSubrogationPersonList!=null && prpLSubrogationPersonList.size()>0){
				for(PrpLSubrogationPersonVo subrogationPersonVo:prpLSubrogationPersonList){
					MTASubrogationInfoVo subrogationVo = new MTASubrogationInfoVo();
					subrogationVo.setSubrogationType("1");
					subrogationVo.setCreateTime(DateUtils.dateToStr(subrogationPersonVo.getCreateTime(), DateUtils.YToSec));
					subrogationVo.setCreateUser(subrogationPersonVo.getCreateUser());
					subrogationVo.setSerialNo(subrogationPersonVo.getSerialNo().toString());
					subrogationVo.setName(subrogationPersonVo.getName());
					subrogationVo.setUnitName(subrogationPersonVo.getUnitName());
					subrogationVo.setPhone(subrogationPersonVo.getPhone());
					subrogationVo.setAddress(subrogationPersonVo.getAddress());
					subrogationVo.setZipno(subrogationPersonVo.getZipno());
					subrogationVo.setIdentifyNumber(subrogationPersonVo.getIdentifyNumber());
					subrogationVo.setInsuredInfo(subrogationPersonVo.getInsuredInfo());
					subrogationVo.setLawLinkerName(subrogationPersonVo.getLawLinkerName());
					subrogationVo.setUnitPhone(subrogationPersonVo.getUnitPhone());
					subrogationVo.setOtherInfo(subrogationPersonVo.getOtherInfo());
					subrogationInfoList.add(subrogationVo);
				}
			}
		}
		checkTaskInfo.setSubrogationInfo(subrogationInfoList);
		
		return checkTaskInfo;
		
	}

	@Override
	public MTACheckInfoInitResVo service(String arg0, Object arg1) {
		// TODO Auto-generated method stub
		init();
		MTACheckInfoInitResVo resVo = new MTACheckInfoInitResVo();
		MTACheckInfoInitResBodyVo checkInfoInitResBodyVo = new MTACheckInfoInitResBodyVo();
		MTACheckTaskInfoVo checkTaskInfoVo = new MTACheckTaskInfoVo();
		
		CarchildResponseHeadVo head = new CarchildResponseHeadVo();
		CarchildHeadVo carchildHeadVo = new CarchildHeadVo();
		XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
		stream.autodetectAnnotations(true);
		stream.setMode(XStream.NO_REFERENCES);
		stream.aliasSystemAttribute(null,"class");// 去掉 class属性
		String registNo = "";
		String nodeType="";//请求节点
		try{
			stream.processAnnotations(MTACheckInfoInitResVo.class);
			MTACheckInfoInitReqVo checkInfoInitReqVo = (MTACheckInfoInitReqVo) arg1;
			String xml = stream.toXML(checkInfoInitReqVo);
			logger.info("民太安查勘信息初始化接收报文: \n"+xml);
			MTACheckInfoInitReqBodyVo checkInfoInitReqBodyVo = checkInfoInitReqVo.getBody();
			carchildHeadVo = checkInfoInitReqVo.getHead();
			if (!"MTA_003".equals(carchildHeadVo.getRequestType())|| !"claim_user".equals(carchildHeadVo.getUser())|| !"claim_psd".equals(carchildHeadVo.getPassWord())) {
				throw new IllegalArgumentException(" 请求头参数错误  ");
			}
			if(StringUtils.isBlank(xml)){
				throw new IllegalArgumentException("报文为空");
			}
			if(!StringUtils.isNotBlank(carchildHeadVo.getRequestType())){
				throw new IllegalArgumentException("请求类型不能为空");
			}
			if(!StringUtils.isNotBlank(carchildHeadVo.getRequestType())){
				throw new IllegalArgumentException("用户名不能为空");		
			}
			if(!StringUtils.isNotBlank(carchildHeadVo.getRequestType())){
				throw new IllegalArgumentException("密码不能为空");
			}
			if(!StringUtils.isNotBlank(checkInfoInitReqBodyVo.getRegistNo())){
				throw new IllegalArgumentException("报案号不能为空");
			}
			registNo = checkInfoInitReqBodyVo.getRegistNo();
			nodeType=checkInfoInitReqBodyVo.getNodeType();
			if(!StringUtils.isNotBlank(checkInfoInitReqBodyVo.getNodeType())){
				throw new IllegalArgumentException("任务节点类型不能为空");
			}
			if(!StringUtils.isNotBlank(checkInfoInitReqBodyVo.getTaskId())){
				throw new IllegalArgumentException("任务ID不能为空");
			}
			if(!StringUtils.isNotBlank(checkInfoInitReqBodyVo.getHandlerCode())){
				throw new IllegalArgumentException("处理人代码不能为空");
			}
			if(!StringUtils.isNotBlank(checkInfoInitReqBodyVo.getScheduleObjectId())){
				throw new IllegalArgumentException("处理人员归属机构编码不能为空");
			}
			
			setCheckTaskInfoVo(checkTaskInfoVo, checkInfoInitReqBodyVo);

			List<PrpLWfTaskVo> prpLWfTaskVoList = wfTaskHandleService.findWftaskByRegistNoAndNodeCode(checkInfoInitReqBodyVo.getRegistNo(), FlowNode.Chk.toString());
			//流入时间降序排
			Collections.sort(prpLWfTaskVoList, new Comparator<PrpLWfTaskVo>() {
			@Override
			public int compare(PrpLWfTaskVo o1,PrpLWfTaskVo o2) {
					return o2.getTaskInTime().compareTo(o1.getTaskInTime());
				}
			});
			PrpLWfTaskVo taskVo = prpLWfTaskVoList.get(0);
			
			if("0".equals(taskVo.getHandlerStatus())){//未处理
				Long  scheduleTaskId=Long.parseLong(taskVo.getHandlerIdKey());
				// 查勘初始化
				SysUserVo userVo = new SysUserVo();
				userVo = scheduleTaskService.findPrpduserByUserCode(checkInfoInitReqBodyVo.getHandlerCode(), "1");
				userVo.setComCode(checkInfoInitReqBodyVo.getScheduleObjectId());
				userVo.setUserCode(checkInfoInitReqBodyVo.getHandlerCode());
				userVo.setUserName(checkInfoInitReqBodyVo.getHandlerName());
				userVo.setComName(checkInfoInitReqBodyVo.getScheduleObjectName());
				CheckActionVo checkActionVo = checkHandleService.initCheckBySchedule
						(scheduleTaskId,checkInfoInitReqBodyVo.getRegistNo(),userVo);
				
				// 保存
				Long checkId = checkHandleService.saveCheckOnAccept(checkActionVo,userVo);
				System.out.println(checkId);
				wfTaskHandleService.tempSaveTask(taskVo.getTaskId().doubleValue(),checkId.toString(),userVo.getUserCode(),userVo.getComCode());
				
				checkActionVo = checkHandleService.initCheckByCheck(checkId);
				checkTaskInfoVo = this.setCheckInfo(checkInfoInitResBodyVo,checkTaskInfoVo, checkActionVo,taskVo);
				checkInfoInitResBodyVo.setCheckTaskInfo(checkTaskInfoVo);
			}else{//正在处理已处理
				Long checkTaskId = Long.parseLong(taskVo.getHandlerIdKey());
				// 查勘初始化
				CheckActionVo checkActionVo = checkHandleService.initCheckByCheck(checkTaskId);
				checkTaskInfoVo = this.setCheckInfo(checkInfoInitResBodyVo,checkTaskInfoVo, checkActionVo,taskVo);
				checkInfoInitResBodyVo.setCheckTaskInfo(checkTaskInfoVo);
			}
			head.setResponseType(carchildHeadVo.getRequestType());
			head.setErrNo("1");
			head.setErrMsg("Success");
			resVo.setHead(head);
			resVo.setBody(checkInfoInitResBodyVo);
		}
		catch(Exception e){
			head.setResponseType(carchildHeadVo.getRequestType());
			head.setErrNo("0");
			head.setErrMsg(e.getMessage());
			resVo.setHead(head);
			logger.info("民太安查勘信息初始化异常信息：\n");
			e.printStackTrace();
		}
		stream.processAnnotations(CheckInfoInitResVo.class);
		logger.info("民太安查勘信息初始化返回报文=========：\n"+stream.toXML(resVo));
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

	private void setCheckTaskInfoVo(MTACheckTaskInfoVo checkTaskInfoVo,MTACheckInfoInitReqBodyVo checkInfoInitReqBodyVo){
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
		//TODO ItemnoName赋值车牌号
		checkTaskInfoVo.setItemnoName("");
		
	}
	
}
