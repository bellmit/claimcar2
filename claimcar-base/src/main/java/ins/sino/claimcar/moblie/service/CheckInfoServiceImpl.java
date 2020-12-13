/******************************************************************************
 * CREATETIME : 2015年12月8日 上午10:19:01
 ******************************************************************************/
package ins.sino.claimcar.moblie.service;

import ins.framework.lang.Springs;
import ins.platform.utils.DateUtils;
import ins.platform.vo.SysUserVo;
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
import ins.sino.claimcar.common.losscharge.vo.PrpLDlossChargeVo;
import ins.sino.claimcar.flow.service.PayCustomService;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.manager.vo.PrpLPayCustomVo;
import ins.sino.claimcar.mobile.check.vo.AccountInfoVo;
import ins.sino.claimcar.mobile.check.vo.CarInfoVo;
import ins.sino.claimcar.mobile.check.vo.CheckExtendInfoVo;
import ins.sino.claimcar.mobile.check.vo.CheckInfoInitReqBodyVo;
import ins.sino.claimcar.mobile.check.vo.CheckInfoInitReqVo;
import ins.sino.claimcar.mobile.check.vo.CheckInfoInitResBodyVo;
import ins.sino.claimcar.mobile.check.vo.CheckInfoInitResVo;
import ins.sino.claimcar.mobile.check.vo.CheckTaskInfoVo;
import ins.sino.claimcar.mobile.check.vo.CheckVo;
import ins.sino.claimcar.mobile.check.vo.DriverVo;
import ins.sino.claimcar.mobile.check.vo.PersonInfoVo;
import ins.sino.claimcar.mobile.check.vo.PropInfoVo;
import ins.sino.claimcar.mobile.vo.FlowInfoInitReqVo;
import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckHead;
import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckResponseHead;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.service.ScheduleTaskService;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

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
 * 
 * 查勘信息初始化（快赔请求理赔）
 * @author ★zhujunde
 */
public class CheckInfoServiceImpl implements ServiceInterface {
	
	private static Logger logger = LoggerFactory.getLogger(CheckInfoServiceImpl.class);

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
    RegistQueryService registQueryService;
    @Autowired
    PolicyViewService policyViewService;
    
	public CheckTaskInfoVo setCheckInfo(CheckInfoInitResBodyVo checkInfoInitResBodyVo,CheckTaskInfoVo checkTaskInfoVo,CheckActionVo checkActionVo,PrpLWfTaskVo taskVo){

		List<CarInfoVo> carInfoVoList = new ArrayList<CarInfoVo>();
		List<PropInfoVo> propInfoVoList = new ArrayList<PropInfoVo>();
		List<PersonInfoVo> personInfoList = new ArrayList<PersonInfoVo>();
		List<AccountInfoVo> accountInfoList = new ArrayList<AccountInfoVo>();
		Map<String,String> serialNoMap = new HashMap<String,String>();
		PrpLCheckVo prpLCheckVo = checkActionVo.getPrpLcheckVo();
		PrpLCheckDutyVo checkDutyVo = checkActionVo.getCheckDutyVo();
		PrpLCheckTaskVo checkTaskVo = prpLCheckVo.getPrpLCheckTask();
		List<PrpLDlossChargeVo> prpLDlossChargeVos = checkActionVo.getLossChargeVo();
		//checkActionVo.getPrp
		//查勘记录
		if(prpLCheckVo.getId()!=null ){
			checkTaskInfoVo.setCheckId(String.valueOf(prpLCheckVo.getId()));
		}
		checkTaskInfoVo.setTaskId(taskVo.getTaskId().toString());
		checkTaskInfoVo.setRegistNo(prpLCheckVo.getRegistNo());
		if(prpLCheckVo.getSingleAccidentFlag().equals("0")){//不是单车
			checkTaskInfoVo.setIsunilAccident("0");
		}else{
			checkTaskInfoVo.setIsunilAccident("1");
		}
		checkTaskInfoVo.setDamageTypeCode(prpLCheckVo.getDamageTypeCode());
		checkTaskInfoVo.setManageTypeName(prpLCheckVo.getManageType());
		checkTaskInfoVo.setDamageCode(prpLCheckVo.getDamageCode());
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
                checkTaskInfoVo.setAccidentReason(registVo.getAccidentReason());
            }else{//报案界面没有管控必录，这里为空取默认值01
                checkTaskInfoVo.setAccidentReason("01");
            } 
        }else{
            checkTaskInfoVo.setAccidentReason("");
        }

		
		checkTaskInfoVo.setIsLoss(prpLCheckVo.getLossType());
		checkTaskInfoVo.setIsFire("");
		checkTaskInfoVo.setIsWater("");
		checkTaskInfoVo.setWaterLevel("");
		checkTaskInfoVo.setCheckSite(checkTaskVo.getCheckAddress());
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		checkTaskInfoVo.setCheckDate(DateUtils.dateToStr(checkTaskVo.getCheckDate(), DateUtils.YToSec));
		checkTaskInfoVo.setCheckType(prpLCheckVo.getCheckType());
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
		//checkInfoInitResBodyVo.set事故责任
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
		if(StringUtils.isNotBlank(prpLCheckVo.getNoDutyFlag())){
			checkTaskInfoVo.setNoDutyFlag(prpLCheckVo.getNoDutyFlag());//是否免责情形
		}else{
			checkTaskInfoVo.setNoDutyFlag("0");//是否免责情形
		}
		
		checkTaskInfoVo.setNoDutyReason(prpLCheckVo.getNoDutyReason());
		if("DM99".equals(prpLCheckVo.getDamageCode())){
		    checkTaskInfoVo.setDamOtherCode(prpLCheckVo.getDamOtherCode());//二级出险原因
		}
		//身份证
		if(StringUtils.isNotBlank(checkTaskVo.getCheckerIdfNo())){
		    checkTaskInfoVo.setCheckIdentifyNumber(checkTaskVo.getCheckerIdfNo());
		}
		//手机号
		if(StringUtils.isNotBlank(checkTaskVo.getCheckerPhone())){
		    checkTaskInfoVo.setCheckPhoneNumber(checkTaskVo.getCheckerPhone());
        }
		
		
		PrpLCheckCarVo checkMainCarVo = checkActionVo.getCheckMainCarVo();
		List<PrpLCheckCarVo> checkThirdCarVos = checkActionVo.getCheckThirdCarList();
		
		//标的
		CarInfoVo carInfoVo = new CarInfoVo();
		DriverVo driverVo = new DriverVo();
		CheckVo checkVo = new CheckVo();
		PrpLCheckCarInfoVo prpLcheckMainCarVo = checkMainCarVo.getPrpLCheckCarInfo();
		PrpLCheckDriverVo prpLCheckDriverVo = checkMainCarVo.getPrpLCheckDriver();
		if(prpLcheckMainCarVo.getCarid()!=null){
			carInfoVo.setCarId(String.valueOf(prpLcheckMainCarVo.getCarid()));
		}
		carInfoVo.setIfObject("标的");
		carInfoVo.setSerialNo(String.valueOf(checkMainCarVo.getSerialNo()));
		carInfoVo.setCarSerialNo("");
		carInfoVo.setLicenseNo(prpLcheckMainCarVo.getLicenseNo());
		carInfoVo.setLicenseType(prpLcheckMainCarVo.getLicenseType());
		if(StringUtils.isNotBlank(prpLcheckMainCarVo.getLicenseType())){
			carInfoVo.setLicenseType(prpLcheckMainCarVo.getLicenseType());
		}else{
			carInfoVo.setLicenseType("02");
		}
		if(StringUtils.isNotBlank(prpLcheckMainCarVo.getPlatformCarKindCode())){
			carInfoVo.setCarKindCode(prpLcheckMainCarVo.getPlatformCarKindCode());
		}else{
			carInfoVo.setCarKindCode("11");;
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
		driverVo.setDriverSerialNo("");
		driverVo.setCarSerialNo("");
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
		
		if(checkMainCarVo.getCarid()!=null){
			checkVo.setLossId(String.valueOf(checkMainCarVo.getCarid()));
		}
		checkVo.setCarId(String.valueOf(prpLcheckMainCarVo.getCarid()));
		checkVo.setLossSerialNo("");
		checkVo.setCarSerialNo("");
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
			if(checkDutyVo1.getIndemnityDutyRate()!=null && checkDutyVo1.getIndemnityDutyRate()!=null){
				checkVo.setIndemnityDuty(checkDutyVo1.getIndemnityDuty());
				checkVo.setIndemnityDutyRate(checkDutyVo1.getIndemnityDutyRate().toString());//添加责任比例
			}
		}
		
		/*if(checkMainCarVo.getIndemnityDutyRate()!=null){
			checkVo.setIndemnityDuty(checkMainCarVo.getIndemnityDutyRate().toString());
		}*/
		//checkVo.setEstimatedLoss(checkMainCarVo.getLossFee().toString());
		//checkVo.setIndemnityDuty(checkMainCarVo.getIndemnityDutyRate().toString());
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
				if(prpLcheckThirdCarVo.getCarid()!=null){
					carThirdInfoVo.setCarId(String.valueOf(prpLcheckThirdCarVo.getCarid()));
				}
				carThirdInfoVo.setSerialNo(String.valueOf(vo.getSerialNo()));
				carThirdInfoVo.setCarSerialNo("");
				String thirdSerialNo = vo.getSerialNo().toString();
				String thirdLicNo = vo.getPrpLCheckCarInfo().getLicenseNo();
				serialNoMap.put(thirdSerialNo,1==vo.getSerialNo() ? "标的车("+thirdLicNo+")" : "三者车("+thirdLicNo+")");
				
					
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
				driverThirdVo.setDriverSerialNo("");
				driverThirdVo.setCarSerialNo("");
				driverThirdVo.setCarId(String.valueOf(prpLcheckThirdCarVo.getCarid()));
				driverThirdVo.setDriverName(prpLCheckThirdDriverVo.getDriverName());
				driverThirdVo.setSex(prpLCheckThirdDriverVo.getDriverSex());
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
				checkThirdVo.setLossSerialNo("");
				checkThirdVo.setCarSerialNo("");
				checkThirdVo.setCarId(String.valueOf(prpLcheckThirdCarVo.getCarid()));
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
				/*checkThirdVo.setEstimatedLoss(vo.getLossFee().toString());
				checkThirdVo.setIndemnityDuty(vo.getIndemnityDutyRate().toString());*/
				if(vo.getLossFee()!=null){
					checkThirdVo.setEstimatedLoss(vo.getLossFee().toString());
				}
				
				//初始化duty
				PrpLCheckDutyVo checkDutyVos=checkTaskService.findCheckDuty
						(vo.getRegistNo(),vo.getSerialNo());
				if(checkDutyVos != null){
					if(checkDutyVos.getIndemnityDutyRate()!=null && checkDutyVos.getIndemnityDutyRate()!=null){
						checkThirdVo.setIndemnityDuty(checkDutyVos.getIndemnityDuty());
						checkThirdVo.setIndemnityDutyRate(checkDutyVos.getIndemnityDutyRate().toString());//添加责任比例
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
				if(vo.getId()!=null){
					propInfoVo.setPropId(String.valueOf(vo.getId()));
				}
				propInfoVo.setLossType(vo.getLossPartyName());
				propInfoVo.setLossName(vo.getLossItemName());
				propInfoVo.setLossNum(vo.getLossNum());
			////待确定
            	/*for(Map.Entry<String, String> serialNo : serialNoMap.entrySet()){
            		if(serialNo.getValue().contains(vo.getLossPartyName())){
            			propInfoVo.setPropSerialNo(serialNo.getKey());
            		}
            	}*/
            	propInfoVo.setPropSerialNo("");
				/*
				propInfoVo.setLossPrice(vo.getLossFeeType());
				propInfoVo.setSalvAgeamount("");
				propInfoVo.setPayAmount("");*/
            	if(vo.getLossFee()!=null){
            		propInfoVo.setPayAmount(vo.getLossFee().toString());
            	}
            	propInfoVo.setLossFeeType(vo.getLossFeeType());//损失单位
				propInfoVoList.add(propInfoVo);
			}
		}
		checkInfoInitResBodyVo.setPropInfo(propInfoVoList);
		List<PrpLCheckPersonVo> prpLCheckPersonVos = checkActionVo.getCheckPersonList();
		//人伤
		for(PrpLCheckPersonVo vo:prpLCheckPersonVos){
			PersonInfoVo personInfoVo = new PersonInfoVo();
			if(vo.getId()!=null){
				personInfoVo.setPersonId(String.valueOf(vo.getId()));
			}
			if(StringUtils.isNotBlank(prpLCheckVo.getReconcileFlag())){
				personInfoVo.setIsAdjust(prpLCheckVo.getReconcileFlag());
			}else{
				personInfoVo.setIsAdjust("1");
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
			personInfoVo.setIshospital("");
			personInfoVo.setDegree(vo.getPersonPayType());
			//personInfoVo.setSurverytpe(vo.getCheckDispose());checkType
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
			personInfoVo.setPersHandleType(prpLCheckVo.getPersHandleType());
		////待确定
        	/*for(Map.Entry<String, String> serialNo : serialNoMap.entrySet()){
        		if(StringUtils.isNotBlank(vo.getLossPartyName()) && vo.getLossPartyName().length()>2){//截取地面
        			String lossPartyName = vo.getLossPartyName().substring(0, 2);
        			if(serialNo.getValue().contains(lossPartyName)){
            			personInfoVo.setPersonSerialNo(serialNo.getKey());
            		}
        		}
        	}*/
        	personInfoVo.setPersonSerialNo("");
        	//添加受伤部位字段
        	if(StringUtils.isNotBlank(vo.getInjuredPart())){
        		personInfoVo.setPart(vo.getInjuredPart());
        	}else{
        		personInfoVo.setPart("");
        	}
			personInfoList.add(personInfoVo);
		}
		checkInfoInitResBodyVo.setPersonInfo(personInfoList);
		
		//收款人信息
		List<PrpLPayCustomVo> payCustomVos =  new ArrayList<PrpLPayCustomVo>();
		payCustomVos = payCustomService.findPayCustomVoList(prpLCheckVo.getRegistNo(),"2");//查询被保险人
		for(PrpLPayCustomVo prpLPayCustomVo : payCustomVos){
		    AccountInfoVo account = new AccountInfoVo();
		    //int i = 0 ;
		    account.setAccountId(String.valueOf(prpLPayCustomVo.getId()));
		   /* account.setAccountSerialNo(String.valueOf(i));
		    i++;*/
		    account.setAccountSerialNo("");
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
		
	/*	if(prpLDlossChargeVos!=null){
			for(PrpLDlossChargeVo prpLDlossChargeVo:prpLDlossChargeVos){
				PrpLPayCustomVo prpLPayCustomVo = payCustomService.findPayCustomVoById(prpLDlossChargeVo.getReceiverId());
				AccountInfoVo account = new AccountInfoVo();
				account.setPayeeType(prpLPayCustomVo.getPayObjectKind());
				account.setName(prpLPayCustomVo.getPayeeName());
				account.setIdentifyNumber(prpLPayCustomVo.getCertifyNo());
				account.setBankName(prpLPayCustomVo.getBankOutlets());
				account.setAccountName(prpLPayCustomVo.getPayeeName());
				account.setAccountNo(prpLPayCustomVo.getAccountNo());
				account.setPhone(prpLPayCustomVo.getPayeeMobile());
				accountInfoList.add(account);
			}
		}*/
		checkInfoInitResBodyVo.setAccountInfo(accountInfoList);
		
		//查勘扩展信息
		CheckExtendInfoVo extendInfo = new CheckExtendInfoVo();
		List<PrpLCheckExtVo> checkExtList = checkActionVo.getCheckExtList();
		if(checkExtList!=null && checkExtList.size()>0){
			for(PrpLCheckExtVo checkExtVo:checkExtList){
				if("保险车辆行驶证是否有效".equals(checkExtVo.getCheckExtName()) 
						&& StringUtils.isNotBlank(checkExtVo.getColumnValue())){
					extendInfo.setLicenseEffective(checkExtVo.getColumnValue());
				}
			}
		}
		checkInfoInitResBodyVo.setExtendInfo(extendInfo);
		
		return checkTaskInfoVo;
		
	}

	@Override
	public CheckInfoInitResVo service(String arg0, Object arg1) {
		// TODO Auto-generated method stub
		init();
		//返回的vo
		CheckInfoInitResVo resVo = new CheckInfoInitResVo();
		CheckInfoInitResBodyVo checkInfoInitResBodyVo = new CheckInfoInitResBodyVo();
		CheckTaskInfoVo checkTaskInfoVo = new CheckTaskInfoVo();
		
		MobileCheckResponseHead head = new MobileCheckResponseHead();
		MobileCheckHead mobileCheckHead = new MobileCheckHead();
		XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
		stream.autodetectAnnotations(true);
		stream.setMode(XStream.NO_REFERENCES);
		stream.aliasSystemAttribute(null,"class");// 去掉 class属性
		String registNo = "";
		try{
			
			stream.processAnnotations(CheckInfoInitReqVo.class);
			CheckInfoInitReqVo checkInfoInitReqVo = (CheckInfoInitReqVo) arg1;
			stream.processAnnotations(FlowInfoInitReqVo.class);
			String xml = stream.toXML(checkInfoInitReqVo);
			logger.info("移动查查勘信息初始化接收报文: \n"+xml);
			//CheckInfoInitReqVo checkInfoInitReqVo = (CheckInfoInitReqVo)stream.fromXML(xml);
			CheckInfoInitReqBodyVo checkInfoInitReqBodyVo = checkInfoInitReqVo.getBody();
			mobileCheckHead = checkInfoInitReqVo.getHead();
			if (!"007".equals(mobileCheckHead.getRequestType())|| !"claim_user".equals(mobileCheckHead.getUser())|| !"claim_psd".equals(mobileCheckHead.getPassWord())) {
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
			registNo = checkInfoInitReqBodyVo.getRegistNo();
			if(!StringUtils.isNotBlank(checkInfoInitReqBodyVo.getCheckStatus())){
				throw new IllegalArgumentException("案件状态不能为空");
			}
			if(!StringUtils.isNotBlank(checkInfoInitReqBodyVo.getNodeType())){
				throw new IllegalArgumentException("调度节点不能为空");
			}
			setCheckTaskInfoVo(checkTaskInfoVo, checkInfoInitReqBodyVo);
			PrpLWfTaskVo taskVo = wfTaskHandleService.queryTask(Double.valueOf(checkInfoInitReqBodyVo.getTaskId()));
			if("0".equals(taskVo.getHandlerStatus())){//未处理
				/*List<PrpLWfTaskVo> prpLWfTaskVoList = wfTaskHandleService.findWftaskByRegistNoAndNodeCode(checkInfoInitReqBodyVo.getRegistNo(), FlowNode.Chk.toString());
				if(prpLWfTaskVoList!=null && prpLWfTaskVoList.size()>0){
					//流入时间降序排
					Collections.sort(prpLWfTaskVoList, new Comparator<PrpLWfTaskVo>() {
					@Override
					public int compare(PrpLWfTaskVo o1,PrpLWfTaskVo o2) {
							return o2.getTaskInTime().compareTo(o1.getTaskInTime());
						}
					});
					Long  scheduleTaskId=Long.parseLong(prpLWfTaskVoList.get(0).getHandlerIdKey());*/
				
					//查勘不调移动端通知理赔接口，所以查勘初始化要回写工作流的isMobileAccept
					taskVo.setIsMobileAccept("1");
					wfTaskHandleService.updateTaskIn(taskVo);
				
					Long  scheduleTaskId = Long.parseLong(taskVo.getHandlerIdKey());
					// 查勘初始化
					SysUserVo userVo = new SysUserVo();
					userVo = scheduleTaskService.findPrpduserByUserCode(checkInfoInitReqBodyVo.getNextHandlerCode(), "1");
					userVo.setComCode(checkInfoInitReqBodyVo.getScheduleObjectId());
					userVo.setUserCode(checkInfoInitReqBodyVo.getNextHandlerCode());
					userVo.setUserName(checkInfoInitReqBodyVo.getNextHandlerName());
					userVo.setComName(checkInfoInitReqBodyVo.getScheduleObjectName());
				/*	SysUserVo userVo = new SysUserVo();
					userVo.setComCode(checkTaskInfoSubmitVo.getScheduleObjectId());
					userVo.setUserCode(checkTaskInfoSubmitVo.getNextHandlerCode());
					userVo.setUserName(checkTaskInfoSubmitVo.getNextHandlerName());
					userVo.setComName(checkTaskInfoSubmitVo.getScheduleObjectName());*/
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
			head.setResponseType(mobileCheckHead.getRequestType());
			head.setResponseCode("YES");
			head.setResponseMessage("Success");
			resVo.setHead(head);
			resVo.setBody(checkInfoInitResBodyVo);
		}catch(Exception e){
			head.setResponseType(mobileCheckHead.getRequestType());
			head.setResponseCode("NO");
			head.setResponseMessage(e.getMessage());
			resVo.setHead(head);
			logger.info("移动查勘信息初始化异常信息：\n");
			e.printStackTrace();
		}
		stream.processAnnotations(CheckInfoInitResVo.class);
		logger.info("移动查勘信息初始化返回报文=========：\n"+stream.toXML(resVo));
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
        if(policyViewService==null){
            policyViewService = (PolicyViewService)Springs.getBean(PolicyViewService.class);
        }
        if(registQueryService==null){
            registQueryService = (RegistQueryService)Springs.getBean(RegistQueryService.class);
        }        
	}
	
	private CheckTaskInfoVo setCheckTaskInfoVo(CheckTaskInfoVo checkTaskInfoVo,CheckInfoInitReqBodyVo checkInfoInitReqBodyVo){
		if(!StringUtils.isNotBlank(checkInfoInitReqBodyVo.getNextHandlerCode())){
			throw new IllegalArgumentException("处理人代码不能为空");
		}
		if(!StringUtils.isNotBlank(checkInfoInitReqBodyVo.getNextHandlerName())){
			throw new IllegalArgumentException("处理人名称不能为空");
		}
		if(!StringUtils.isNotBlank(checkInfoInitReqBodyVo.getScheduleObjectId())){
			throw new IllegalArgumentException("处理人员归属机构编码不能为空");
		}
		if(!StringUtils.isNotBlank(checkInfoInitReqBodyVo.getScheduleObjectName())){
			throw new IllegalArgumentException("处理人员归属机构名称不能为空");
		}
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
		if(StringUtils.isNotBlank(checkInfoInitReqBodyVo.getItemNo())){
			checkTaskInfoVo.setItemNo(checkInfoInitReqBodyVo.getItemNo());
		}else{
			checkTaskInfoVo.setItemNo(null);
		}
		if(StringUtils.isNotBlank(checkInfoInitReqBodyVo.getItemnoName())){
			checkTaskInfoVo.setItemnoName(checkInfoInitReqBodyVo.getItemnoName());
		}else{
			checkTaskInfoVo.setItemnoName(null);
		}
		if(StringUtils.isNotBlank(checkInfoInitReqBodyVo.getIsObject())){
			checkTaskInfoVo.setIsObject(checkInfoInitReqBodyVo.getIsObject());
		}else{
			checkTaskInfoVo.setIsObject(null);
		}
		return checkTaskInfoVo;
		
	}
}
