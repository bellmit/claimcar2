package ins.sino.claimcar.pinganUnion.service;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.utils.Beans;
import ins.platform.common.service.facade.PingAnDictService;
import ins.platform.vo.PiccCodeDictVo;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.AuditStatus;
import ins.sino.claimcar.CodeConstants.RadioValue;
import ins.sino.claimcar.CodeConstants.ScheduleStatus;
import ins.sino.claimcar.CodeConstants.ValidFlag;
import ins.sino.claimcar.check.po.PrpLCheck;
import ins.sino.claimcar.check.po.PrpLCheckCar;
import ins.sino.claimcar.check.po.PrpLCheckCarInfo;
import ins.sino.claimcar.check.po.PrpLCheckDriver;
import ins.sino.claimcar.check.po.PrpLCheckDuty;
import ins.sino.claimcar.check.po.PrpLCheckProp;
import ins.sino.claimcar.check.po.PrpLCheckTask;
import ins.sino.claimcar.check.service.CheckHandleService;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.PrpLCheckCarVo;
import ins.sino.claimcar.check.vo.PrpLCheckPropVo;
import ins.sino.claimcar.check.vo.PrpLCheckTaskVo;
import ins.sino.claimcar.check.vo.PrpLCheckVo;
import ins.sino.claimcar.common.claimtext.service.ClaimTextService;
import ins.sino.claimcar.common.claimtext.vo.PrpLClaimTextVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.WFMobileService;
import ins.sino.claimcar.flow.service.WfFlowQueryService;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.flow.vo.WfTaskSubmitVo;
import ins.sino.claimcar.pinganUnion.enums.PingAnCodeTypeEnum;
import ins.sino.claimcar.pinganUnion.vo.PingAnDataNoticeVo;
import ins.sino.claimcar.pinganUnion.vo.ResultBean;
import ins.sino.claimcar.pinganVo.CarObjectDTO;
import ins.sino.claimcar.pinganVo.RespCarCheckDataVo;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.PrpLCMainService;
import ins.sino.claimcar.regist.service.RegistRiskInfoService;
import ins.sino.claimcar.regist.service.RegistService;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.schedule.service.ScheduleService;
import ins.sino.claimcar.schedule.vo.PrpLScheduleDefLossVo;
import ins.sino.claimcar.schedule.vo.PrpLScheduleTaskVo;
import ins.sino.claimcar.subrogation.service.SubrogationService;
import ins.sino.claimcar.subrogation.vo.PrpLSubrogationMainVo;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Path;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.google.gson.Gson;
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"},group="pingAnCarCheckService")
@Path("pingAnCarCheckService")
public class PingAnCarCheckServiceImpl implements PingAnHandleService{
	private static Logger logger = LoggerFactory.getLogger(PingAnCarCheckServiceImpl.class);
	@Autowired
	RegistService registService;
	@Autowired
	PingAnDictService pingAnDictService;
	@Autowired
	CheckHandleService checkHandleService;
	@Autowired
	CheckTaskService checkTaskService;
	@Autowired
	PrpLCMainService prpLCMainService;
	@Autowired
	DatabaseDao databaseDao;
	@Autowired
	WfTaskHandleService wfTaskHandleService;
	@Autowired
	ScheduleService scheduleService;
	@Autowired
	RegistRiskInfoService registRiskInfoService;
	@Autowired
	PolicyViewService policyViewService;
	@Autowired
	ClaimTextService claimTextService;
	@Autowired
	private WfFlowQueryService wfFlowQueryService;
	@Autowired
    private WFMobileService wFMobileService;
	@Autowired
	SubrogationService subrogationService;
	@Override
	public ResultBean pingAnHandle(String registNo, PingAnDataNoticeVo pingAnDataNoticeVo, String respData) {
		logger.info("???????????????????????????--------------------------------???{}",respData);
		ResultBean resultBean=ResultBean.success();
		Gson gson = new Gson();
		Long checkId=null;
		try{
			if(StringUtils.isBlank(registNo)){
				throw new IllegalAccessException("------???????????????-----");
			}
			RespCarCheckDataVo respCarCheckDataVo=gson.fromJson(respData,RespCarCheckDataVo.class);
			if(respCarCheckDataVo!=null){
			  if(respCarCheckDataVo.getCarObjectDTOList()!=null && respCarCheckDataVo.getCarObjectDTOList().size()>0){
				PrpLCheckVo prpLCheckVo=checkTaskService.findCheckVoByRegistNo(registNo);
				List<PrpLCMainVo> prpLCMainVos=prpLCMainService.findPrpLCMainsByRegistNo(registNo);
				if(prpLCheckVo==null){//??????
					//????????????
					PrpLCheck prpLCheck=new PrpLCheck();
					
					prpLCheck.setRegistNo(registNo);//?????????
						if(respCarCheckDataVo.getCarObjectDTOList().size()>1){ 
							prpLCheck.setSingleAccidentFlag("0");//??????????????????
							prpLCheck.setDamageTypeCode("03");//??????
						}else{
							prpLCheck.setSingleAccidentFlag("1");
							prpLCheck.setDamageTypeCode("01");//????????????
						}
						prpLCheck.setManageType("1");//??????????????????
						PrpLRegistVo  prpLRegistVo=registService.findRegistByRegistNo(prpLCheck.getRegistNo());
						if(prpLRegistVo!=null){
							prpLCheck.setDamageCode(prpLRegistVo.getDamageCode());
							prpLCheck.setIsPropLoss(prpLRegistVo.getPrpLRegistExt().getIsPropLoss());
							prpLCheck.setIsPersonLoss(prpLRegistVo.getPrpLRegistExt().getIsPersonLoss());
							prpLCheck.setIsClaimSelf(prpLRegistVo.getPrpLRegistExt().getIsClaimSelf());//???????????????
							prpLCheck.setIsSubRogation(prpLRegistVo.getPrpLRegistExt().getIsSubRogation());//??????????????????
						}else{
							prpLCheck.setDamageCode("DM99");
							prpLCheck.setIsPropLoss("0");//???????????????
							prpLCheck.setIsPersonLoss("0");//??????????????????
							prpLCheck.setIsClaimSelf("0");//???????????????
							prpLCheck.setIsSubRogation("0");//??????????????????
						}
						prpLCheck.setMercyFlag("0");//??????????????????
						prpLCheck.setLossType("0");//????????????????????????????????????
						prpLCheck.setCheckType("3");//????????????
						prpLCheck.setCheckClass("0");//????????????
						prpLCheck.setCreateUser("AUTO");
						prpLCheck.setCreateTime(new Date());
						prpLCheck.setUpdateUser("AUTO");
						prpLCheck.setUpdateTime(new Date());
						
					//???????????????
					PrpLCheckTask prpLCheckTask=new PrpLCheckTask();
					prpLCheckTask.setRegistNo(registNo);//?????????
					prpLCheckTask.setCheckerCode("AUTO");//?????????
					prpLCheckTask.setChecker("AUTO");
					//????????????0????????????????????????
					prpLCheckTask.setSumRescueFee(new BigDecimal("0"));//????????????
					prpLCheckTask.setSumLossCarFee(new BigDecimal("0"));//???????????????
					prpLCheckTask.setSumLossPersnFee(new BigDecimal("0"));//???????????????
					prpLCheckTask.setSumLossPropFee(new BigDecimal("0"));//???????????????
					prpLCheckTask.setComCode(prpLRegistVo.getComCode());//????????????
					prpLCheckTask.setMakeCom(prpLRegistVo.getComCode());//????????????
					prpLCheckTask.setVerifyCheckFlag("0");//????????????
					prpLCheckTask.setValidFlag("1");
					prpLCheckTask.setCreateTime(new Date());
					prpLCheckTask.setCreateUser("AUTO");
					prpLCheckTask.setUpdateTime(new Date());
					prpLCheckTask.setUpdateUser("AUTO");
					prpLCheckTask.setIsTimeout("0");
					//????????????
					if(respCarCheckDataVo.getCarSurveyDTOList()!=null && respCarCheckDataVo.getCarSurveyDTOList().size()>0){
						//??????????????????
						prpLCheck.setChkSubmitTime(StringFormatDate(respCarCheckDataVo.getCarSurveyDTOList().get(0).getCarSurveyDate()));
						prpLCheckTask.setLinkerName(respCarCheckDataVo.getCarSurveyDTOList().get(0).getSurveyorUm());//?????????
						prpLCheckTask.setCheckDate(StringFormatDate(respCarCheckDataVo.getCarSurveyDTOList().get(0).getCarSurveyDate()));//????????????
						prpLCheckTask.setCheckAddress(respCarCheckDataVo.getCarSurveyDTOList().get(0).getSurveyPlaceGPSX()+"_"+respCarCheckDataVo.getCarSurveyDTOList().get(0).getSurveyPlaceGPSY());//?????????
						if("01".equals(respCarCheckDataVo.getCarSurveyDTOList().get(0).getSurveyMode())){
							prpLCheckTask.setFirstAddressFlag("1");//??????????????????
						}else{
							prpLCheckTask.setFirstAddressFlag("0");//??????????????????
						}
						if(StringUtils.isNotBlank(respCarCheckDataVo.getCarSurveyDTOList().get(0).getSurveyRemark()) && respCarCheckDataVo.getCarSurveyDTOList().get(0).getSurveyRemark().length()>950){
							prpLCheckTask.setContexts(respCarCheckDataVo.getCarSurveyDTOList().get(0).getSurveyRemark().substring(0, 949));//????????????
						}else{
							prpLCheckTask.setContexts(respCarCheckDataVo.getCarSurveyDTOList().get(0).getSurveyRemark());
						}
							
					}
					
					//????????????
					List<PrpLCheckCar> prpLCheckCars = new ArrayList<PrpLCheckCar>(0);
					//?????????
					int i=2;
					for(CarObjectDTO carDto:respCarCheckDataVo.getCarObjectDTOList()){
						PrpLCheckCar prpLCheckCar=new PrpLCheckCar();
						prpLCheckCar.setRegistNo(registNo);
						prpLCheckCar.setPrpLCheckTask(prpLCheckTask);
						prpLCheckCar.setLossFlag("0");//????????????
						prpLCheckCar.setCheckAdress(prpLRegistVo.getDamageAddress());
						prpLCheckCar.setCheckTime(prpLCheckTask.getCheckDate());
						prpLCheckCar.setLossFee(new BigDecimal(0));//??????????????????0
						prpLCheckCar.setRescueFee(new BigDecimal(0));//???????????????0
						prpLCheckCar.setLossPart("1");//????????????????????????
						prpLCheckCar.setValidFlag("1");
						prpLCheckCar.setFlag("1");
						prpLCheckCar.setCreateTime(new Date());
						prpLCheckCar.setCreateUser("AUTO");
						prpLCheckCar.setUpdateTime(new Date());
						prpLCheckCar.setUpdateUser("AUTO");
						prpLCheckCar.setRemark(carDto.getLossObjectNo());//???????????????
						
						if("01".equals(carDto.getLossType())){//?????????
							if("Y".equals(carDto.getIsAllLoss())){
								prpLCheck.setLossType("1");//????????????
							}else{
								prpLCheck.setLossType("0");
							}
							prpLCheckCar.setSerialNo(1);
						}else{
							prpLCheckCar.setSerialNo(i);
							i=i+1;
						}
						//??????????????????
						PrpLCheckCarInfo  prpLCheckCarInfo=new PrpLCheckCarInfo();
						prpLCheckCarInfo.setRegistNo(registNo);//?????????
						prpLCheckCarInfo.setLicenseNo(carDto.getCarMark());//?????????
						prpLCheckCarInfo.setEngineNo(carDto.getEngineNo());//????????????
						prpLCheckCarInfo.setFrameNo(carDto.getVin());//?????????
						prpLCheckCarInfo.setVinNo(carDto.getVin());
						PiccCodeDictVo  piccCodeDictVo=pingAnDictService.getDictData(PingAnCodeTypeEnum.HPZL.getCodeType(), carDto.getLicensePlateType());
						prpLCheckCarInfo.setLicenseType(piccCodeDictVo.getDhCodeCode());//????????????
						if("01".equals(carDto.getLossType())){//?????????
							prpLCheckCarInfo.setInsurecomcode("DHIC");
							prpLCheckCarInfo.setStartDate(prpLCMainVos.get(0).getStartDate());
							prpLCheckCarInfo.setCarOwner(prpLCMainVos.get(0).getInsuredName());
							prpLCheckCarInfo.setEnrollDate(prpLCMainVos.get(0).getSignDate());//????????????
							prpLCheckCarInfo.setPlatformCarKindCode("001");//????????????
						}else{
							prpLCheckCarInfo.setPlatformCarKindCode("11");//????????????
						}
						
						PrpLCheckDriver prpLCheckDriver=new PrpLCheckDriver();
						prpLCheckDriver.setRegistNo(registNo);//?????????
						prpLCheckDriver.setDriverName(carDto.getDriverName());
						if("01".equals(carDto.getLossType())){//?????????
							PiccCodeDictVo  piccCodeDictVo1=pingAnDictService.getDictData(PingAnCodeTypeEnum.ZJCX.getCodeType(), respCarCheckDataVo.getCarSurveyDTOList().get(0).getQuasiDrivingType());
							if(respCarCheckDataVo.getCarSurveyDTOList()!=null && respCarCheckDataVo.getCarSurveyDTOList().size()>0){
								prpLCheckDriver.setDrivingCarType(piccCodeDictVo1.getDhCodeCode());//????????????
							}
							
						}
						PiccCodeDictVo  piccCodeDictVo2=pingAnDictService.getDictData(PingAnCodeTypeEnum.JSZLX.getCodeType(),carDto.getLicenseType());
						prpLCheckDriver.setIdentifyType(piccCodeDictVo2.getDhCodeCode());//???????????????
						prpLCheckDriver.setDrivingLicenseNo(carDto.getLicenseNo());//????????????
						prpLCheckDriver.setIdentifyNumber(carDto.getLicenseNo());//????????????
						prpLCheckDriver.setValidFlag("1");
						prpLCheckDriver.setPrpLCheckCar(prpLCheckCar);
						prpLCheckCar.setPrpLCheckTask(prpLCheckTask);
						prpLCheckCar.setPrpLCheckCarInfo(prpLCheckCarInfo);
						prpLCheckCar.setPrpLCheckDriver(prpLCheckDriver);
						prpLCheckCarInfo.setPrpLCheckCar(prpLCheckCar);
						prpLCheckCars.add(prpLCheckCar);
					}
					
					//??????????????????????????????????????????????????????????????????????????????????????????
					List<PrpLCheckProp> prpLCheckProps=new ArrayList<PrpLCheckProp>();
					if("1".equals(prpLCheck.getIsPropLoss())){
						PrpLCheckProp prpLCheckProp=new PrpLCheckProp();
						prpLCheckProp.setRegistNo(registNo);
						prpLCheckProp.setPrpLCheckTask(prpLCheckTask);
						prpLCheckProp.setLossPartyId(0L);
						prpLCheckProp.setLossPartyName("??????");
						prpLCheckProp.setLossItemName("??????");
						prpLCheckProp.setLossNum("1");
						prpLCheckProp.setLossFee(new BigDecimal(0));
						prpLCheckProp.setValidFlag("1");
						prpLCheckProp.setCreateTime(new Date());
						prpLCheckProp.setCreateUser("AUTO");
						prpLCheckProp.setUpdateUser("AUTO");
						prpLCheckProp.setUpdateTime(new Date());
						prpLCheckProp.setPrpLCheckTask(prpLCheckTask);
						prpLCheckProps.add(prpLCheckProp);
					}
					prpLCheckTask.setPrpLCheckProps(prpLCheckProps);
					prpLCheckTask.setPrpLCheckCars(prpLCheckCars);
					prpLCheckTask.setPrpLCheck(prpLCheck);
					prpLCheck.setPrpLCheckTask(prpLCheckTask);
					//?????????????????????
					databaseDao.save(PrpLCheck.class, prpLCheck);
					checkId=prpLCheck.getId();
					//????????????
				    if(prpLCheckCars!=null && prpLCheckCars.size()>0){
				    	for(PrpLCheckCar prpLCheckCar:prpLCheckCars){
				    		PrpLCheckDuty prpLCheckDuty=new PrpLCheckDuty();
				    		if(1==prpLCheckCar.getSerialNo()){
				    			prpLCheckDuty.setCarType("1");
				    			prpLCheckDuty.setSerialNo(1);
				    			prpLCheckDuty.setValidFlag("1");
					    		prpLCheckDuty.setRegistNo(prpLCheckCar.getRegistNo());
					    		prpLCheckDuty.setCiDutyFlag("1");
					    		if(respCarCheckDataVo.getCarSurveyDTOList()!=null && respCarCheckDataVo.getCarSurveyDTOList().size()>0){
					    			if("0".equals(respCarCheckDataVo.getCarSurveyDTOList().get(0).getDutyCoefficient())){
					    				prpLCheckDuty.setIndemnityDuty("4");
					    				prpLCheckDuty.setCiDutyFlag("0");
					    			}else if("100".equals(respCarCheckDataVo.getCarSurveyDTOList().get(0).getDutyCoefficient())){
					    				prpLCheckDuty.setIndemnityDuty("0");
					    			}else if("50".equals(respCarCheckDataVo.getCarSurveyDTOList().get(0).getDutyCoefficient())){
					    				prpLCheckDuty.setIndemnityDuty("2");
					    			}else{
					    				if(StringUtils.isNotBlank(respCarCheckDataVo.getCarSurveyDTOList().get(0).getDutyCoefficient())){
					    					if(Integer.valueOf(respCarCheckDataVo.getCarSurveyDTOList().get(0).getDutyCoefficient())>50){
					    						prpLCheckDuty.setIndemnityDuty("1");
					    					}else{
					    						prpLCheckDuty.setIndemnityDuty("3");
					    					}
					    				}
					    			}
					    		}
					    		prpLCheckDuty.setIndemnityDutyRate(respCarCheckDataVo.getCarSurveyDTOList().get(0).getDutyCoefficient()!=null?new BigDecimal(respCarCheckDataVo.getCarSurveyDTOList().get(0).getDutyCoefficient()):null);
					    		prpLCheckDuty.setValidFlag("1");
					    		prpLCheckDuty.setCreateUser("AUTO");
					    		prpLCheckDuty.setCreateTime(new Date());
					    		prpLCheckDuty.setUpdateTime(new Date());
					    		prpLCheckDuty.setLicenseNo(prpLCheckCar.getPrpLCheckCarInfo().getLicenseNo());
					    		if(prpLCheck!=null && prpLCheck.getPrpLCheckTask().getPrpLCheckCars()!=null && prpLCheck.getPrpLCheckTask().getPrpLCheckCars().size()>0){
					    			for(PrpLCheckCar checkCar:prpLCheck.getPrpLCheckTask().getPrpLCheckCars()){
					    				if(1==checkCar.getSerialNo()){
					    					prpLCheckDuty.setCheckCarId(checkCar.getCarid());
					    				}
					    				
					    			}
					    		}
					    		//???????????????????????????
					    		databaseDao.save(PrpLCheckDuty.class, prpLCheckDuty);
					    		break;
				    		}
				    		
				    	}
				    }
					
				}else{//??????
					
				}
				
			}

			}
			PrpLSubrogationMainVo subMainVo=new PrpLSubrogationMainVo();
		    //????????????
			subMainVo.setRegistNo(registNo);
			subMainVo.setSubrogationFlag("0");//???????????????
			subMainVo.setValidFlag(ValidFlag.VALID);
			subMainVo.setCreateTime(new Date());
		    subMainVo.setCreateUser("AUTO");
			subrogationService.saveSubrogationInfo(subMainVo);
			if(resultBean.getSuccess()){
				resultBean=submitCheckToDloss(registNo,resultBean);
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.info("??????????????????????????????",e);
			resultBean.fail("??????????????????????????????"+e.getMessage());
		}
		return resultBean;
	}
    
	/**
	 * ??????????????????
	 * String ???????????? Date??????
	 * @param strDate
	 * @return
	 * @throws ParseException
	 */
	private Date StringFormatDate(String strDate) throws ParseException{
		Date date=null;
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		  if(StringUtils.isNotBlank(strDate)){
			date=format.parse(strDate);
		}
		return date;
	}
	
	/**
	 * ??????????????????????????????
	 */
	private ResultBean submitCheckToDloss(String registNo,ResultBean resultBean ){
		try{
			String userCode ="AUTO";
			PrpLRegistVo prpLRegistVo=registService.findRegistByRegistNo(registNo);
			String comCode = prpLRegistVo.getComCode();
			SysUserVo userVo=new SysUserVo();
			userVo.setComCode(comCode);
			userVo.setUserCode("AUTO");
			List<PrpLWfTaskVo> wfTaskVo = null;
			PrpLCheckVo checkVo = checkTaskService.findCheckVoByRegistNo(registNo);
			PrpLCheckTaskVo checkTaskVo = checkVo.getPrpLCheckTask();
			Long checkTaskId = checkTaskVo.getId();
			Map<String,PrpLScheduleDefLossVo> carScheduleMap = new HashMap<String,PrpLScheduleDefLossVo>();
			// ??????????????????????????????
			List<PrpLCheckCarVo> checkCarVoList = checkTaskVo.getPrpLCheckCars();
			Long schTaskId = null;// ????????????id
			if(checkCarVoList != null && checkCarVoList.size() > 0){
				int i = 1;
				for(PrpLCheckCarVo checkCarVo : checkCarVoList){
					// ?????????????????????????????????
					Integer serialNo = checkCarVo.getSerialNo();
					Long scheduleDefLossId = scheduleService.findCarDefLoss(checkCarVo.getRegistNo(),serialNo,ScheduleStatus.DEFLOSS_SCHEDULED);
					if(scheduleDefLossId == null){
						scheduleDefLossId = scheduleService.findCarDefLoss(checkCarVo.getRegistNo(),checkCarVo.getSerialNo(),ScheduleStatus.SCHEDULED_CHANGE);
					}
					if(scheduleDefLossId == null){
						PrpLScheduleDefLossVo defLossVo = null;
						if(serialNo!=1){// ??????????????????????????????????????????,???????????????????????????????????????
							// CodeConstants.YN01.N.equals(checkVo.getIsClaimSelf());//???????????????
							// &&"0".equals(checkCarVo.getLossFlag())// 0-????????? ???1-?????????-------2016???10???13??????????????????????????????????????????????????????
							if(CodeConstants.YN01.N.equals(checkVo.getSingleAccidentFlag())){// ???????????????
								// checkCarVo ?????? defLossVo
								defLossVo = checkHandleService.createDefLossFormCarVo(checkTaskId,checkCarVo,userCode);
								//this.setUserCode("1",defLossVo,defLossVos,checkCarVo,null);// ----------???????????????????????????
							}
						}else{
							defLossVo = checkHandleService.createDefLossFormCarVo(checkTaskId,checkCarVo,userCode);
							//this.setUserCode("1",defLossVo,defLossVos,checkCarVo,null);// ----------???????????????????????????
						}
						if(defLossVo == null) continue;
						carScheduleMap.put(serialNo+"??????"+i,defLossVo);
						i++ ;
					}else{
						// ??????scheduleDefLoss
						PrpLScheduleDefLossVo scheduleDefLossVo = scheduleService.findScheduleDefLossByPk(scheduleDefLossId);
						scheduleDefLossVo.setCheckLossId(checkVo.getId());
						scheduleDefLossVo.setLossCarId(checkCarVo.getCarid());
						scheduleService.updateScheduleDefLoss(scheduleDefLossVo);
						schTaskId = scheduleService.findTaskIdByDefLossId(scheduleDefLossId);
					}

				}
			}

			List<PrpLCheckPropVo> checkPropVoList = checkTaskVo.getPrpLCheckProps();
			// ???????????????????????????Long,PrpLScheduleDefLossVo
			Map<Long,PrpLScheduleDefLossVo> propScheduleMap = new HashMap<Long,PrpLScheduleDefLossVo>();
			if(checkPropVoList != null && checkPropVoList.size() > 0){
				Long lossPartyId = null;
				for(PrpLCheckPropVo checkPropVo : checkPropVoList){
					String isNoClaim = checkPropVo.getIsNoClaim();// ???????????????????????????????????????
					if(RadioValue.RADIO_YES.equals(isNoClaim)){
						continue;
					}
					lossPartyId = checkPropVo.getLossPartyId();
					PrpLScheduleDefLossVo defLossVo = propScheduleMap.get(lossPartyId);
					
					// ?????????????????????????????????????????????????????????????????????????????????????????????
					boolean isExist = scheduleService.isExistDefLossTask(checkPropVo.getRegistNo(),
							lossPartyId.intValue(),"2",ScheduleStatus.DEFLOSS_SCHEDULED,ScheduleStatus.SCHEDULED_CHANGE);
					if(defLossVo == null && !isExist){
						// checkPropVo ????????? defLossVo
						boolean kindFlag = checkHandleService.getCarKind(checkVo.getRegistNo());
						defLossVo = checkHandleService.createDefLossFormPropVo(checkTaskId,checkPropVo,userCode);
						//this.setUserCode("2",defLossVo,defLossVos,null,checkPropVo);// ----------???????????????????????????
						if(lossPartyId==1){// ?????????
							if(CodeConstants.RadioValue.RADIO_YES.equals(checkVo.getIsClaimSelf())){
								propScheduleMap.put(lossPartyId,defLossVo);
							}else{
								if(kindFlag){// ??????????????????????????????
									propScheduleMap.put(lossPartyId,defLossVo);
								}
							}
						}else{// ????????? ?????????????????????????????????????????????
							propScheduleMap.put(lossPartyId,defLossVo);
//							if(CodeConstants.YN01.N.equals(checkVo.getIsClaimSelf())){
//							}
						}
					}
				}
			}
			
			// ????????????????????????(????????????)?????????
			List<PrpLWfTaskVo> prpLWfTaskinVos=  wfFlowQueryService.findTaskVoForInByNodeCode(registNo, FlowNode.Check.name());
			List<PrpLWfTaskVo> prpLWfTaskoutVos= wfFlowQueryService.findTaskVoForOutByNodeCode(registNo, FlowNode.Check.name());
			
			//???????????????????????????????????????????????????
		    if(prpLWfTaskoutVos==null || prpLWfTaskoutVos.size()==0){
			   if(prpLWfTaskinVos!=null && prpLWfTaskinVos.size()>0){
				    prpLWfTaskinVos.get(0).setHandlerIdKey(checkVo.getId().toString());
				    wfTaskHandleService.updateTaskIn(prpLWfTaskinVos.get(0));
					WfTaskSubmitVo wfTaskSubmitVo = new WfTaskSubmitVo();
					wfTaskSubmitVo.setFlowTaskId(prpLWfTaskinVos.get(0).getTaskId());
					wfTaskSubmitVo.setFlowId(prpLWfTaskinVos.get(0).getFlowId());
					wfTaskSubmitVo.setCurrentNode(FlowNode.Check);
					wfTaskSubmitVo.setComCode(policyViewService.getPolicyComCode(checkVo.getRegistNo()));
					wfTaskSubmitVo.setTaskInUser(userCode);
					wfTaskSubmitVo.setTaskInKey(checkVo.getRegistNo());
					wfTaskSubmitVo.setAssignUser(userCode);
					wfTaskSubmitVo.setAssignCom(comCode);
					wfTaskSubmitVo.setIsMobileAccept("0");
					wfTaskSubmitVo.setPingAnFlag("1");//????????????
					registRiskInfoService.writePrpLRegistRiskInfo(registNo,checkVo.getIsSubRogation(),userCode);
					checkTaskService.saveCheckDutyHis(registNo,"????????????");
					Long scheduleTaskId = null;
					try{
						// ???????????????PrpLscheduleTaskVo ????????????????????????
						scheduleTaskId = checkHandleService.saveScheduleDefLossTask(checkVo.getId(),userCode,carScheduleMap,propScheduleMap,schTaskId);
						PrpLScheduleTaskVo scheduleTaskVo = findAllScheduleDefLossByCheck(scheduleTaskId);
						try{
							// ???????????? --> ???????????????
							//claimService.submitClaim(registNo,flowId);
						}catch(Exception e){
							// ??????
							// for(PrpLWfTaskVo taskVo : wfTaskVo){
							// wfTaskHandleService.rollBackTask(taskVo); }
							logger.error("?????????"+ (checkVo == null ? null : checkVo.getRegistNo()) + "??????????????????????????????",e);
							throw new IllegalArgumentException("???????????????????????????????????????????????????<br/>",e);
						}
						wfTaskVo = wfTaskHandleService.submitCheck(scheduleTaskVo,checkVo,wfTaskSubmitVo);
                        
			            // ??????/??????????????????
						PrpLClaimTextVo claimTextVo = checkHandleService.createClaimText(checkVo.getId(),AuditStatus.SUBMITCHECK,FlowNode.Chk.toString(),"1",userVo);
						claimTextService.saveOrUpdte(claimTextVo);
					}catch(Exception e){
						logger.error("?????????"+ (checkVo == null ? null : checkVo.getRegistNo()) + "?????????????????????",e);
						// ?????????????????????
					    PrpLScheduleTaskVo vo = scheduleService.findScheduleTaskVoByPK(scheduleTaskId);
					    scheduleService.rollBackDefLossTask(vo);
						// ??????????????????
						// ??????
					    wfTaskHandleService.rollBackFlow(vo,prpLWfTaskinVos.get(0).getFlowId());
					    wfTaskHandleService.rollBackNodeAndLWfMain(vo,prpLWfTaskinVos.get(0).getFlowId());

						throw new IllegalArgumentException("??????????????????????????????????????????<br/>"+e);
					}
				}else{
					resultBean.fail("?????????????????????????????????????????????");
				}
		   }
			
			
		}catch(Exception e){
			resultBean.fail("??????????????????????????????"+e.getMessage());
		}
		return resultBean;
	}
	
    //???????????????????????????????????????
	private PrpLScheduleTaskVo findAllScheduleDefLossByCheck(Long scheduleTaskId){
		PrpLScheduleTaskVo newScheduleTaskVo = new PrpLScheduleTaskVo();;
		PrpLScheduleTaskVo scheduleTaskVo = scheduleService.findScheduleTaskVoByPK(scheduleTaskId);
		
		List<PrpLScheduleDefLossVo> defLossVoList = scheduleService.findScheduleDefLossByCheck(scheduleTaskVo.getRegistNo());
		if (defLossVoList != null && !defLossVoList.isEmpty()) {
			Beans.copy().from(scheduleTaskVo).to(newScheduleTaskVo);
			newScheduleTaskVo.setPrpLScheduleDefLosses(defLossVoList);
		} else {
			List<PrpLScheduleDefLossVo> voList = new ArrayList<PrpLScheduleDefLossVo>();
			Beans.copy().from(scheduleTaskVo).to(newScheduleTaskVo);
			newScheduleTaskVo.setPrpLScheduleDefLosses(voList);
		}
		return newScheduleTaskVo;
	}
}
