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
		logger.info("车查勘接口信息报文--------------------------------》{}",respData);
		ResultBean resultBean=ResultBean.success();
		Gson gson = new Gson();
		Long checkId=null;
		try{
			if(StringUtils.isBlank(registNo)){
				throw new IllegalAccessException("------报案号为空-----");
			}
			RespCarCheckDataVo respCarCheckDataVo=gson.fromJson(respData,RespCarCheckDataVo.class);
			if(respCarCheckDataVo!=null){
			  if(respCarCheckDataVo.getCarObjectDTOList()!=null && respCarCheckDataVo.getCarObjectDTOList().size()>0){
				PrpLCheckVo prpLCheckVo=checkTaskService.findCheckVoByRegistNo(registNo);
				List<PrpLCMainVo> prpLCMainVos=prpLCMainService.findPrpLCMainsByRegistNo(registNo);
				if(prpLCheckVo==null){//保存
					//查勘主表
					PrpLCheck prpLCheck=new PrpLCheck();
					
					prpLCheck.setRegistNo(registNo);//报案号
						if(respCarCheckDataVo.getCarObjectDTOList().size()>1){ 
							prpLCheck.setSingleAccidentFlag("0");//是否单车事故
							prpLCheck.setDamageTypeCode("03");//多方
						}else{
							prpLCheck.setSingleAccidentFlag("1");
							prpLCheck.setDamageTypeCode("01");//单方事故
						}
						prpLCheck.setManageType("1");//事故处理类型
						PrpLRegistVo  prpLRegistVo=registService.findRegistByRegistNo(prpLCheck.getRegistNo());
						if(prpLRegistVo!=null){
							prpLCheck.setDamageCode(prpLRegistVo.getDamageCode());
							prpLCheck.setIsPropLoss(prpLRegistVo.getPrpLRegistExt().getIsPropLoss());
							prpLCheck.setIsPersonLoss(prpLRegistVo.getPrpLRegistExt().getIsPersonLoss());
							prpLCheck.setIsClaimSelf(prpLRegistVo.getPrpLRegistExt().getIsClaimSelf());//是否碰自赔
							prpLCheck.setIsSubRogation(prpLRegistVo.getPrpLRegistExt().getIsSubRogation());//代位求偿类型
						}else{
							prpLCheck.setDamageCode("DM99");
							prpLCheck.setIsPropLoss("0");//是否有物损
							prpLCheck.setIsPersonLoss("0");//是否包含人伤
							prpLCheck.setIsClaimSelf("0");//是否碰自赔
							prpLCheck.setIsSubRogation("0");//代位求偿类型
						}
						prpLCheck.setMercyFlag("0");//案件紧急程度
						prpLCheck.setLossType("0");//默认为否，下面逻辑在赋值
						prpLCheck.setCheckType("3");//查勘类型
						prpLCheck.setCheckClass("0");//查勘类别
						prpLCheck.setCreateUser("AUTO");
						prpLCheck.setCreateTime(new Date());
						prpLCheck.setUpdateUser("AUTO");
						prpLCheck.setUpdateTime(new Date());
						
					//查勘任务表
					PrpLCheckTask prpLCheckTask=new PrpLCheckTask();
					prpLCheckTask.setRegistNo(registNo);//报案号
					prpLCheckTask.setCheckerCode("AUTO");//查勘人
					prpLCheckTask.setChecker("AUTO");
					//先默认为0，未决接口会刷新
					prpLCheckTask.setSumRescueFee(new BigDecimal("0"));//总施救费
					prpLCheckTask.setSumLossCarFee(new BigDecimal("0"));//车估损金额
					prpLCheckTask.setSumLossPersnFee(new BigDecimal("0"));//人估损金额
					prpLCheckTask.setSumLossPropFee(new BigDecimal("0"));//财估损金额
					prpLCheckTask.setComCode(prpLRegistVo.getComCode());//归属机构
					prpLCheckTask.setMakeCom(prpLRegistVo.getComCode());//保单机构
					prpLCheckTask.setVerifyCheckFlag("0");//复勘标识
					prpLCheckTask.setValidFlag("1");
					prpLCheckTask.setCreateTime(new Date());
					prpLCheckTask.setCreateUser("AUTO");
					prpLCheckTask.setUpdateTime(new Date());
					prpLCheckTask.setUpdateUser("AUTO");
					prpLCheckTask.setIsTimeout("0");
					//案件信息
					if(respCarCheckDataVo.getCarSurveyDTOList()!=null && respCarCheckDataVo.getCarSurveyDTOList().size()>0){
						//查勘完成时间
						prpLCheck.setChkSubmitTime(StringFormatDate(respCarCheckDataVo.getCarSurveyDTOList().get(0).getCarSurveyDate()));
						prpLCheckTask.setLinkerName(respCarCheckDataVo.getCarSurveyDTOList().get(0).getSurveyorUm());//联系人
						prpLCheckTask.setCheckDate(StringFormatDate(respCarCheckDataVo.getCarSurveyDTOList().get(0).getCarSurveyDate()));//查勘时间
						prpLCheckTask.setCheckAddress(respCarCheckDataVo.getCarSurveyDTOList().get(0).getSurveyPlaceGPSX()+"_"+respCarCheckDataVo.getCarSurveyDTOList().get(0).getSurveyPlaceGPSY());//查勘地
						if("01".equals(respCarCheckDataVo.getCarSurveyDTOList().get(0).getSurveyMode())){
							prpLCheckTask.setFirstAddressFlag("1");//是否现场查勘
						}else{
							prpLCheckTask.setFirstAddressFlag("0");//是否现场查勘
						}
						if(StringUtils.isNotBlank(respCarCheckDataVo.getCarSurveyDTOList().get(0).getSurveyRemark()) && respCarCheckDataVo.getCarSurveyDTOList().get(0).getSurveyRemark().length()>950){
							prpLCheckTask.setContexts(respCarCheckDataVo.getCarSurveyDTOList().get(0).getSurveyRemark().substring(0, 949));//查勘说明
						}else{
							prpLCheckTask.setContexts(respCarCheckDataVo.getCarSurveyDTOList().get(0).getSurveyRemark());
						}
							
					}
					
					//查勘车辆
					List<PrpLCheckCar> prpLCheckCars = new ArrayList<PrpLCheckCar>(0);
					//车信息
					int i=2;
					for(CarObjectDTO carDto:respCarCheckDataVo.getCarObjectDTOList()){
						PrpLCheckCar prpLCheckCar=new PrpLCheckCar();
						prpLCheckCar.setRegistNo(registNo);
						prpLCheckCar.setPrpLCheckTask(prpLCheckTask);
						prpLCheckCar.setLossFlag("0");//损失标志
						prpLCheckCar.setCheckAdress(prpLRegistVo.getDamageAddress());
						prpLCheckCar.setCheckTime(prpLCheckTask.getCheckDate());
						prpLCheckCar.setLossFee(new BigDecimal(0));//估损先默认为0
						prpLCheckCar.setRescueFee(new BigDecimal(0));//施救费默认0
						prpLCheckCar.setLossPart("1");//损失部位默认为前
						prpLCheckCar.setValidFlag("1");
						prpLCheckCar.setFlag("1");
						prpLCheckCar.setCreateTime(new Date());
						prpLCheckCar.setCreateUser("AUTO");
						prpLCheckCar.setUpdateTime(new Date());
						prpLCheckCar.setUpdateUser("AUTO");
						prpLCheckCar.setRemark(carDto.getLossObjectNo());//损失对象号
						
						if("01".equals(carDto.getLossType())){//标的车
							if("Y".equals(carDto.getIsAllLoss())){
								prpLCheck.setLossType("1");//是否全损
							}else{
								prpLCheck.setLossType("0");
							}
							prpLCheckCar.setSerialNo(1);
						}else{
							prpLCheckCar.setSerialNo(i);
							i=i+1;
						}
						//查勘车辆信息
						PrpLCheckCarInfo  prpLCheckCarInfo=new PrpLCheckCarInfo();
						prpLCheckCarInfo.setRegistNo(registNo);//报案号
						prpLCheckCarInfo.setLicenseNo(carDto.getCarMark());//车牌号
						prpLCheckCarInfo.setEngineNo(carDto.getEngineNo());//发动机号
						prpLCheckCarInfo.setFrameNo(carDto.getVin());//车架号
						prpLCheckCarInfo.setVinNo(carDto.getVin());
						PiccCodeDictVo  piccCodeDictVo=pingAnDictService.getDictData(PingAnCodeTypeEnum.HPZL.getCodeType(), carDto.getLicensePlateType());
						prpLCheckCarInfo.setLicenseType(piccCodeDictVo.getDhCodeCode());//号牌种类
						if("01".equals(carDto.getLossType())){//标的车
							prpLCheckCarInfo.setInsurecomcode("DHIC");
							prpLCheckCarInfo.setStartDate(prpLCMainVos.get(0).getStartDate());
							prpLCheckCarInfo.setCarOwner(prpLCMainVos.get(0).getInsuredName());
							prpLCheckCarInfo.setEnrollDate(prpLCMainVos.get(0).getSignDate());//初登日期
							prpLCheckCarInfo.setPlatformCarKindCode("001");//车辆种类
						}else{
							prpLCheckCarInfo.setPlatformCarKindCode("11");//车辆种类
						}
						
						PrpLCheckDriver prpLCheckDriver=new PrpLCheckDriver();
						prpLCheckDriver.setRegistNo(registNo);//报案号
						prpLCheckDriver.setDriverName(carDto.getDriverName());
						if("01".equals(carDto.getLossType())){//标的车
							PiccCodeDictVo  piccCodeDictVo1=pingAnDictService.getDictData(PingAnCodeTypeEnum.ZJCX.getCodeType(), respCarCheckDataVo.getCarSurveyDTOList().get(0).getQuasiDrivingType());
							if(respCarCheckDataVo.getCarSurveyDTOList()!=null && respCarCheckDataVo.getCarSurveyDTOList().size()>0){
								prpLCheckDriver.setDrivingCarType(piccCodeDictVo1.getDhCodeCode());//准驾车型
							}
							
						}
						PiccCodeDictVo  piccCodeDictVo2=pingAnDictService.getDictData(PingAnCodeTypeEnum.JSZLX.getCodeType(),carDto.getLicenseType());
						prpLCheckDriver.setIdentifyType(piccCodeDictVo2.getDhCodeCode());//驾驶证类型
						prpLCheckDriver.setDrivingLicenseNo(carDto.getLicenseNo());//驾驶证号
						prpLCheckDriver.setIdentifyNumber(carDto.getLicenseNo());//证件号码
						prpLCheckDriver.setValidFlag("1");
						prpLCheckDriver.setPrpLCheckCar(prpLCheckCar);
						prpLCheckCar.setPrpLCheckTask(prpLCheckTask);
						prpLCheckCar.setPrpLCheckCarInfo(prpLCheckCarInfo);
						prpLCheckCar.setPrpLCheckDriver(prpLCheckDriver);
						prpLCheckCarInfo.setPrpLCheckCar(prpLCheckCar);
						prpLCheckCars.add(prpLCheckCar);
					}
					
					//当报案是否有物损为是时，则默认一条物损信息，方便后续流程生成
					List<PrpLCheckProp> prpLCheckProps=new ArrayList<PrpLCheckProp>();
					if("1".equals(prpLCheck.getIsPropLoss())){
						PrpLCheckProp prpLCheckProp=new PrpLCheckProp();
						prpLCheckProp.setRegistNo(registNo);
						prpLCheckProp.setPrpLCheckTask(prpLCheckTask);
						prpLCheckProp.setLossPartyId(0L);
						prpLCheckProp.setLossPartyName("默认");
						prpLCheckProp.setLossItemName("默认");
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
					//保存车查勘信息
					databaseDao.save(PrpLCheck.class, prpLCheck);
					checkId=prpLCheck.getId();
					//责任比例
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
					    		//保存车查勘责任比例
					    		databaseDao.save(PrpLCheckDuty.class, prpLCheckDuty);
					    		break;
				    		}
				    		
				    	}
				    }
					
				}else{//更新
					
				}
				
			}

			}
			PrpLSubrogationMainVo subMainVo=new PrpLSubrogationMainVo();
		    //代位信息
			subMainVo.setRegistNo(registNo);
			subMainVo.setSubrogationFlag("0");//非代位标志
			subMainVo.setValidFlag(ValidFlag.VALID);
			subMainVo.setCreateTime(new Date());
		    subMainVo.setCreateUser("AUTO");
			subrogationService.saveSubrogationInfo(subMainVo);
			if(resultBean.getSuccess()){
				resultBean=submitCheckToDloss(registNo,resultBean);
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.info("平安联盟车查勘报错：",e);
			resultBean.fail("平安联盟车查勘报错："+e.getMessage());
		}
		return resultBean;
	}
    
	/**
	 * 时间转换方法
	 * String 类型转换 Date类型
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
	 * 查勘提交定损的工作流
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
			// 车损保存调度定损准备
			List<PrpLCheckCarVo> checkCarVoList = checkTaskVo.getPrpLCheckCars();
			Long schTaskId = null;// 调度定损id
			if(checkCarVoList != null && checkCarVoList.size() > 0){
				int i = 1;
				for(PrpLCheckCarVo checkCarVo : checkCarVoList){
					// 判断是否已生成定损任务
					Integer serialNo = checkCarVo.getSerialNo();
					Long scheduleDefLossId = scheduleService.findCarDefLoss(checkCarVo.getRegistNo(),serialNo,ScheduleStatus.DEFLOSS_SCHEDULED);
					if(scheduleDefLossId == null){
						scheduleDefLossId = scheduleService.findCarDefLoss(checkCarVo.getRegistNo(),checkCarVo.getSerialNo(),ScheduleStatus.SCHEDULED_CHANGE);
					}
					if(scheduleDefLossId == null){
						PrpLScheduleDefLossVo defLossVo = null;
						if(serialNo!=1){// 三者车没有损失不生成定损任务,互碰自赔三者车生成定损任务
							// CodeConstants.YN01.N.equals(checkVo.getIsClaimSelf());//非互碰自赔
							// &&"0".equals(checkCarVo.getLossFlag())// 0-有损失 ，1-无损失-------2016年10月13日的需求，车辆无损失也要生成定损任务
							if(CodeConstants.YN01.N.equals(checkVo.getSingleAccidentFlag())){// 非单车事故
								// checkCarVo 转为 defLossVo
								defLossVo = checkHandleService.createDefLossFormCarVo(checkTaskId,checkCarVo,userCode);
								//this.setUserCode("1",defLossVo,defLossVos,checkCarVo,null);// ----------加入指定处理人代码
							}
						}else{
							defLossVo = checkHandleService.createDefLossFormCarVo(checkTaskId,checkCarVo,userCode);
							//this.setUserCode("1",defLossVo,defLossVos,checkCarVo,null);// ----------加入指定处理人代码
						}
						if(defLossVo == null) continue;
						carScheduleMap.put(serialNo+"车损"+i,defLossVo);
						i++ ;
					}else{
						// 更新scheduleDefLoss
						PrpLScheduleDefLossVo scheduleDefLossVo = scheduleService.findScheduleDefLossByPk(scheduleDefLossId);
						scheduleDefLossVo.setCheckLossId(checkVo.getId());
						scheduleDefLossVo.setLossCarId(checkCarVo.getCarid());
						scheduleService.updateScheduleDefLoss(scheduleDefLossVo);
						schTaskId = scheduleService.findTaskIdByDefLossId(scheduleDefLossId);
					}

				}
			}

			List<PrpLCheckPropVo> checkPropVoList = checkTaskVo.getPrpLCheckProps();
			// 保存标的序号对应的Long,PrpLScheduleDefLossVo
			Map<Long,PrpLScheduleDefLossVo> propScheduleMap = new HashMap<Long,PrpLScheduleDefLossVo>();
			if(checkPropVoList != null && checkPropVoList.size() > 0){
				Long lossPartyId = null;
				for(PrpLCheckPropVo checkPropVo : checkPropVoList){
					String isNoClaim = checkPropVo.getIsNoClaim();// 无需赔付不生成财产定损任务
					if(RadioValue.RADIO_YES.equals(isNoClaim)){
						continue;
					}
					lossPartyId = checkPropVo.getLossPartyId();
					PrpLScheduleDefLossVo defLossVo = propScheduleMap.get(lossPartyId);
					
					// 新需求，查勘未处理前，可以调度三者车损或者物损，查勘需要做管控
					boolean isExist = scheduleService.isExistDefLossTask(checkPropVo.getRegistNo(),
							lossPartyId.intValue(),"2",ScheduleStatus.DEFLOSS_SCHEDULED,ScheduleStatus.SCHEDULED_CHANGE);
					if(defLossVo == null && !isExist){
						// checkPropVo 转换为 defLossVo
						boolean kindFlag = checkHandleService.getCarKind(checkVo.getRegistNo());
						defLossVo = checkHandleService.createDefLossFormPropVo(checkTaskId,checkPropVo,userCode);
						//this.setUserCode("2",defLossVo,defLossVos,null,checkPropVo);// ----------加入指定处理人代码
						if(lossPartyId==1){// 损失方
							if(CodeConstants.RadioValue.RADIO_YES.equals(checkVo.getIsClaimSelf())){
								propScheduleMap.put(lossPartyId,defLossVo);
							}else{
								if(kindFlag){// 标的车承保车上货物险
									propScheduleMap.put(lossPartyId,defLossVo);
								}
							}
						}else{// 三者车 （互碰自赔生成三者车财产定损）
							propScheduleMap.put(lossPartyId,defLossVo);
//							if(CodeConstants.YN01.N.equals(checkVo.getIsClaimSelf())){
//							}
						}
					}
				}
			}
			
			// 再调用工作流接口(查勘提交)未处理
			List<PrpLWfTaskVo> prpLWfTaskinVos=  wfFlowQueryService.findTaskVoForInByNodeCode(registNo, FlowNode.Check.name());
			List<PrpLWfTaskVo> prpLWfTaskoutVos= wfFlowQueryService.findTaskVoForOutByNodeCode(registNo, FlowNode.Check.name());
			
			//当案件已处理工作流生成，就不再生成
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
					wfTaskSubmitVo.setPingAnFlag("1");//平安标志
					registRiskInfoService.writePrpLRegistRiskInfo(registNo,checkVo.getIsSubRogation(),userCode);
					checkTaskService.saveCheckDutyHis(registNo,"查勘提交");
					Long scheduleTaskId = null;
					try{
						// 最终组合为PrpLscheduleTaskVo 送给调度接口保存
						scheduleTaskId = checkHandleService.saveScheduleDefLossTask(checkVo.getId(),userCode,carScheduleMap,propScheduleMap,schTaskId);
						PrpLScheduleTaskVo scheduleTaskVo = findAllScheduleDefLossByCheck(scheduleTaskId);
						try{
							// 查勘提交 --> 刷立案数据
							//claimService.submitClaim(registNo,flowId);
						}catch(Exception e){
							// 回滚
							// for(PrpLWfTaskVo taskVo : wfTaskVo){
							// wfTaskHandleService.rollBackTask(taskVo); }
							logger.error("报案号"+ (checkVo == null ? null : checkVo.getRegistNo()) + "查勘提交刷立案失败！",e);
							throw new IllegalArgumentException("提交失败！查勘刷立案估损金额错误！<br/>",e);
						}
						wfTaskVo = wfTaskHandleService.submitCheck(scheduleTaskVo,checkVo,wfTaskSubmitVo);
                        
			            // 保存/更新意见列表
						PrpLClaimTextVo claimTextVo = checkHandleService.createClaimText(checkVo.getId(),AuditStatus.SUBMITCHECK,FlowNode.Chk.toString(),"1",userVo);
						claimTextService.saveOrUpdte(claimTextVo);
					}catch(Exception e){
						logger.error("报案号"+ (checkVo == null ? null : checkVo.getRegistNo()) + "查勘提交失败！",e);
						// 清空调度的数据
					    PrpLScheduleTaskVo vo = scheduleService.findScheduleTaskVoByPK(scheduleTaskId);
					    scheduleService.rollBackDefLossTask(vo);
						// 回滚工作流表
						// 查勘
					    wfTaskHandleService.rollBackFlow(vo,prpLWfTaskinVos.get(0).getFlowId());
					    wfTaskHandleService.rollBackNodeAndLWfMain(vo,prpLWfTaskinVos.get(0).getFlowId());

						throw new IllegalArgumentException("提交失败！查勘任务提交失败！<br/>"+e);
					}
				}else{
					resultBean.fail("平安联盟查勘未处理节点未生成！");
				}
		   }
			
			
		}catch(Exception e){
			resultBean.fail("平安联盟车查勘报错："+e.getMessage());
		}
		return resultBean;
	}
	
    //查询查勘生成的所有定损任务
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
