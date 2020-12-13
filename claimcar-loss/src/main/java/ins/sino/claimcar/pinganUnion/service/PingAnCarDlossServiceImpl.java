package ins.sino.claimcar.pinganUnion.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.utils.Beans;
import ins.platform.common.service.facade.PingAnDictService;
import ins.platform.vo.PiccCodeDictVo;
import ins.sino.claimcar.CodeConstants.HandlerStatus;
import ins.sino.claimcar.CodeConstants.WorkStatus;
import ins.sino.claimcar.carplatform.util.StringUtil;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.PrpLCheckCarInfoVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.WfFlowQueryService;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.flow.vo.WfTaskSubmitVo;
import ins.sino.claimcar.losscar.po.PrpLDlossCarComp;
import ins.sino.claimcar.losscar.po.PrpLDlossCarInfo;
import ins.sino.claimcar.losscar.po.PrpLDlossCarMain;
import ins.sino.claimcar.losscar.po.PrpLDlossCarRepair;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarCompVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarInfoVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarRepairVo;
import ins.sino.claimcar.pinganUnion.enums.PingAnCodeTypeEnum;
import ins.sino.claimcar.pinganUnion.vo.PingAnDataNoticeVo;
import ins.sino.claimcar.pinganUnion.vo.ResultBean;
import ins.sino.claimcar.pinganVo.CarLossDetailVO;
import ins.sino.claimcar.pinganVo.CarLossVO;
import ins.sino.claimcar.pinganVo.CarObjectDTO;
import ins.sino.claimcar.pinganVo.RespCarDlossDataVo;
import ins.sino.claimcar.pinganVo.ThirdInfoVo;
import ins.sino.claimcar.regist.service.RegistService;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.schedule.service.ScheduleService;
import ins.sino.claimcar.schedule.vo.PrpLScheduleDefLossVo;
import javax.ws.rs.Path;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;

@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"},group="pingAnCarDlossService")
@Path("pingAnCarDlossService")
public class PingAnCarDlossServiceImpl implements PingAnHandleService{
	private static Logger logger = LoggerFactory.getLogger(PingAnCarDlossServiceImpl.class);
	private static Map<String,String> partMap=new HashMap<String,String>();
	@Autowired
	RegistService registService;
	@Autowired
	PingAnDictService pingAnDictService; 
	@Autowired
	DatabaseDao databaseDao;
	@Autowired
	CheckTaskService checkTaskService;
	@Autowired
	WfFlowQueryService wfFlowQueryService;
	@Autowired
	WfTaskHandleService wfTaskHandleService;
	@Autowired
	LossCarService lossCarService;
	@Autowired
	ScheduleService scheduleService;
	/**
	 * 损失部位
	 */
	static{
		partMap.put("0","4");
		partMap.put("1","1");
		partMap.put("2","5");
		partMap.put("3","8");
		partMap.put("4","7");
		partMap.put("5","10");
		partMap.put("6","6");
		partMap.put("7","9");
		partMap.put("8","2");
		partMap.put("9","3");
		
	};
	
	@Override
	public ResultBean pingAnHandle(String registNo, PingAnDataNoticeVo pingAnDataNoticeVo, String respData) {
		logger.info("车辆定损接口信息报文--------------------------------》{}",respData);
		ResultBean resultBean=ResultBean.success();
		Gson gson=new Gson();
		String licenceNo="";//车牌号
		PrpLDlossCarMainVo mainVo=new PrpLDlossCarMainVo();
		String comcode="";
		String flag="0";//0表示新的任务,1表示之前的旧任务
		String payFlag="0";//0，不是同次赔付，1-是同次赔付(同一辆车)
		JSONObject jsonObject=JSON.parseObject(pingAnDataNoticeVo.getParamObj());
		String payTimes=jsonObject.get("caseTimes").toString();//赔付次数
		//同车牌的已处理的核损任务
		PrpLWfTaskVo wfTaskVo=new PrpLWfTaskVo();
		try{
			if(StringUtils.isBlank(registNo)){
				throw new IllegalAccessException("-----------------报案号为空--------------------");
			}
			PrpLRegistVo prpLRegistVo=registService.findRegistByRegistNo(registNo);
			PrpLDlossCarMainVo prpLDlossCarMainVo=lossCarService.findLossCarMainByRegistNoAndPaId(registNo, pingAnDataNoticeVo.getId());
			if(prpLDlossCarMainVo!=null){
				flag="1";
			}
			
			comcode=prpLRegistVo.getComCode();
			RespCarDlossDataVo respCarDlossDataVo=gson.fromJson(respData, RespCarDlossDataVo.class);
				CarObjectDTO carObjectDTO=respCarDlossDataVo.getCarObjectDTO();
				licenceNo=carObjectDTO.getCarMark();//车牌号
				List<PrpLDlossCarMainVo> prpLDlossCarMainVos=lossCarService.findLossCarMainByRegistNo(registNo);
				if(prpLDlossCarMainVos!=null && prpLDlossCarMainVos.size()>0){
					for(PrpLDlossCarMainVo carmainVo:prpLDlossCarMainVos){
						if(payTimes.equals(carmainVo.getPayTimes()) && licenceNo.equals(carmainVo.getLicenseNo())){
							payFlag="1";
							mainVo=carmainVo;
							break;
						}
					}
				}
				PrpLDlossCarMainVo dlossCarMainVo=lossCarService.findLossCarMainByRegistNoAndLicenseNo(registNo, licenceNo);
				List<PrpLWfTaskVo> prpLWfTaskVos=wfFlowQueryService.findTaskVoForOutBySubNodeCode(registNo,FlowNode.VLCar_LV0.name());
				String carFlag="0";//是否同一辆车的是否已经核损过，0-未核损，1-已核损
				if(prpLWfTaskVos!=null && prpLWfTaskVos.size()>0){
					for(PrpLWfTaskVo taskVo:prpLWfTaskVos){
						if(StringUtils.isNotBlank(taskVo.getItemName()) && taskVo.getItemName().contains(licenceNo)){
							carFlag="1";
							wfTaskVo=taskVo;
							break;
						}
					}
				}
				if("0".equals(flag) && dlossCarMainVo!=null && "0".equals(carFlag)){
					throw new IllegalAccessException("------该车牌号上次定损任务还未完成，不允许处理本次定损任务。-----");
				}
				PrpLScheduleDefLossVo prpLScheduleDefLossVo=scheduleService.findCarDefLossByLicenseNo(registNo,licenceNo);
				CarLossVO carLossVO=respCarDlossDataVo.getCarLossVO();
				ThirdInfoVo thirdInfoVo=respCarDlossDataVo.getThirdInfo();
				if(thirdInfoVo==null){
					thirdInfoVo=new ThirdInfoVo();
				}
				//查勘车辆信息
				PrpLCheckCarInfoVo prpLCheckCarInfoVo=new PrpLCheckCarInfoVo();
				List<PrpLCheckCarInfoVo> prpLCheckCarInfoVos=checkTaskService.findPrpLCheckCarInfoVoListByOther(registNo,carObjectDTO.getCarMark());
				if(prpLCheckCarInfoVos!=null && prpLCheckCarInfoVos.size()>0){
					prpLCheckCarInfoVo=prpLCheckCarInfoVos.get(0);
				}
				if("1".equals(payFlag)){//同一辆车，同一次赔付次数里，做数据更新操作
					    PrpLDlossCarMainVo prpLDlossCarMain=new PrpLDlossCarMainVo();
					    prpLDlossCarMain.setId(mainVo.getId());
						prpLDlossCarMain.setRepairFactoryCode(carLossVO.getGarageCode());//修理厂代码
						prpLDlossCarMain.setRepairFactoryName(carLossVO.getGarageName());//修理厂名称
						prpLDlossCarMain.setHandlerIdNo(carObjectDTO.getLicenseNo());//定损员证件号
						prpLDlossCarMain.setDeflossDate(new Date());
						String lossParts="";
						if(StringUtil.isNotBlank(carLossVO.getLossPosition())){
							String[] parts=carLossVO.getLossPosition().split(";");
							if(parts!=null && parts.length>0){
								for(int i=0;i<parts.length;i++){
									if(i==0){
										lossParts=parts[i];
									}else{
										lossParts=lossParts+","+parts[i];
									}
								}
							}
						}
						prpLDlossCarMain.setLossPart(lossParts);//损失部位
						prpLDlossCarMain.setSumCompFee(StringUtils.isNotBlank(carLossVO.getMaterialAmount())?new BigDecimal(carLossVO.getMaterialAmount()):new BigDecimal(0));//定损换件金额
						prpLDlossCarMain.setSumRepairFee(StringUtils.isNotBlank(carLossVO.getManpowerAmount())?new BigDecimal(carLossVO.getManpowerAmount()):new BigDecimal(0));//定损修理金额
						prpLDlossCarMain.setSumVeriRepairFee(StringUtils.isNotBlank(carLossVO.getManpowerAgreedAmount())?new BigDecimal(carLossVO.getManpowerAgreedAmount()):new BigDecimal(0));//核损修理金额
						prpLDlossCarMain.setSumMatFee(new BigDecimal(0));//辅料费
						prpLDlossCarMain.setSumVeriCompFee(StringUtils.isNotBlank(carLossVO.getMaterialAgreedAmount())?new BigDecimal(carLossVO.getMaterialAgreedAmount()):new BigDecimal(0));//核损换件金额
						prpLDlossCarMain.setSumVeriMatFee(new BigDecimal(0));//核损辅料费
						prpLDlossCarMain.setSumRemnant(StringUtils.isNotBlank(carLossVO.getSurveyReduceAmount())?new BigDecimal(carLossVO.getSurveyReduceAmount()):new BigDecimal(0));//定损残值
						prpLDlossCarMain.setSumVeriRemnant(StringUtils.isNotBlank(carLossVO.getReduceAmount())?new BigDecimal(carLossVO.getReduceAmount()):new BigDecimal(0));//最终残值
						prpLDlossCarMain.setSumRescueFee(StringUtils.isNotBlank(carLossVO.getRescueFee())?new BigDecimal(carLossVO.getRescueFee()):new BigDecimal(0));//定损施救费
						prpLDlossCarMain.setSumVeriRescueFee(StringUtils.isNotBlank(carLossVO.getRescueFee())?new BigDecimal(carLossVO.getRescueFee()):new BigDecimal(0));//核损施救费
						prpLDlossCarMain.setSumManageFee(StringUtils.isNotBlank(carLossVO.getManageFee())?new BigDecimal(carLossVO.getManageFee()):new BigDecimal(0));//定损管理费
						prpLDlossCarMain.setSumVeriManage(StringUtils.isNotBlank(carLossVO.getManageAgreedFee())?new BigDecimal(carLossVO.getManageAgreedFee()):new BigDecimal(0));//核损管理费
						prpLDlossCarMain.setSumLossFee(StringUtils.isNotBlank(carLossVO.getTotalAmount())?new BigDecimal(carLossVO.getTotalAmount()):new BigDecimal(0));//定损总金额
						prpLDlossCarMain.setSumVeriLossFee(StringUtils.isNotBlank(carLossVO.getTotalAgreedAmount())?new BigDecimal(carLossVO.getTotalAgreedAmount()):new BigDecimal(0));//核损总金额
						prpLDlossCarMain.setCreateUser("AUTO");
						prpLDlossCarMain.setCreateTime(new Date());
						prpLDlossCarMain.setUnderWiteIdNo(carObjectDTO.getLicenseNo());
						prpLDlossCarMain.setUnderWriteCode("AUTO");
						prpLDlossCarMain.setUnderWriteEndDate(new Date());
						prpLDlossCarMain.setUnderWriteFlag("1");//核损通过
						int i=1;//配件数量标识
						int j=0;//工时数量标识
						//配件
						List<PrpLDlossCarCompVo> prpLDlossCarComps = new ArrayList<PrpLDlossCarCompVo>(0);
						//修理工时
						List<PrpLDlossCarRepairVo> prpLDlossCarRepairs = new ArrayList<PrpLDlossCarRepairVo>(0);
						if(respCarDlossDataVo.getCarLossDetailVOList()!=null && respCarDlossDataVo.getCarLossDetailVOList().size()>0){
							for(CarLossDetailVO carLossDetailVO:respCarDlossDataVo.getCarLossDetailVOList()){
								if("1".equals(carLossDetailVO.getLossType())){
									PrpLDlossCarCompVo prpLDlossCarComp=new PrpLDlossCarCompVo();
									prpLDlossCarComp.setRegistNo(registNo);//报案号
									prpLDlossCarComp.setSerialNo(i);
									prpLDlossCarComp.setLicenseNo(carObjectDTO.getCarMark());//车牌号
									prpLDlossCarComp.setCompCode(carLossDetailVO.getFitsCode());
									prpLDlossCarComp.setCompName(carLossDetailVO.getLossName());//配件名称
									if(StringUtils.isNotBlank(carLossDetailVO.getLossCount())){
										prpLDlossCarComp.setQuantity(Integer.valueOf(carLossDetailVO.getLossCount()));//损失数量
									}else{
										prpLDlossCarComp.setQuantity(0);
									}
									prpLDlossCarComp.setMaterialFee(StringUtils.isNotBlank(carLossDetailVO.getLossAmount())?new BigDecimal(carLossDetailVO.getLossAmount()):new BigDecimal(0));//定损单价
									prpLDlossCarComp.setSumDefLoss(prpLDlossCarComp.getMaterialFee().multiply(new BigDecimal(prpLDlossCarComp.getQuantity())));//定损总价
									if(StringUtils.isNotBlank(carLossDetailVO.getLossCount())){
										prpLDlossCarComp.setVeriQuantity(Integer.valueOf(carLossDetailVO.getLossCount()));//核损损失数量
									}else{
										prpLDlossCarComp.setVeriQuantity(0);
									}
									prpLDlossCarComp.setVeriMaterFee(StringUtils.isNotBlank(carLossDetailVO.getLossAgreeAmount())?new BigDecimal(carLossDetailVO.getLossAgreeAmount()):new BigDecimal(0));//核损单价
									prpLDlossCarComp.setSumVeriLoss(prpLDlossCarComp.getVeriMaterFee().multiply(new BigDecimal(prpLDlossCarComp.getVeriQuantity())));//核损总价
									prpLDlossCarComp.setOriginalId(carLossDetailVO.getOriginalCode());//原厂编码
									prpLDlossCarComp.setManageFeeRate(StringUtils.isNotBlank(carLossDetailVO.getFitsFeeRate())?new BigDecimal(carLossDetailVO.getFitsFeeRate()):null);//管理费比例
									prpLDlossCarComp.setManageSingleFee(StringUtils.isNotBlank(carLossDetailVO.getFitsFee())?new BigDecimal(carLossDetailVO.getFitsFee()):new BigDecimal(0));//管理费
									prpLDlossCarComps.add(prpLDlossCarComp);
									i++;
								}else if("2".equals(carLossDetailVO.getLossType())){
									PrpLDlossCarRepairVo prpLDlossCarRepair=new PrpLDlossCarRepairVo();
									prpLDlossCarRepair.setRegistNo(registNo);
									prpLDlossCarRepair.setSerialNo(j);
									prpLDlossCarRepair.setRepairFlag("0");//内修
									prpLDlossCarRepair.setLicenseNo(carObjectDTO.getCarMark());//车牌号
									prpLDlossCarRepair.setCompCode(carLossDetailVO.getFitsCode());
									prpLDlossCarRepair.setCompName(carLossDetailVO.getLossName());
									prpLDlossCarRepair.setManHour(StringUtils.isNotBlank(carLossDetailVO.getLossCount())?new BigDecimal(carLossDetailVO.getLossCount()):new BigDecimal(0));//数量
									prpLDlossCarRepair.setManHourUnitPrice(StringUtils.isNotBlank(carLossDetailVO.getLossAmount())?new BigDecimal(carLossDetailVO.getLossAmount()):new BigDecimal(0));//单价
									prpLDlossCarRepair.setManHourFee(prpLDlossCarRepair.getManHour().multiply(prpLDlossCarRepair.getManHourUnitPrice()));//定损工时总价
									prpLDlossCarRepair.setSumDefLoss(prpLDlossCarRepair.getManHour().multiply(prpLDlossCarRepair.getManHourUnitPrice()));//定损总价
									prpLDlossCarRepair.setVeriManHour(StringUtils.isNotBlank(carLossDetailVO.getLossCount())?new BigDecimal(carLossDetailVO.getLossCount()):new BigDecimal(0));//核损数量
									prpLDlossCarRepair.setVeriManUnitPrice(StringUtils.isNotBlank(carLossDetailVO.getLossAgreeAmount())?new BigDecimal(carLossDetailVO.getLossAgreeAmount()):new BigDecimal(0));//核损单价
									prpLDlossCarRepair.setVeriManHourFee(prpLDlossCarRepair.getVeriManHour().multiply(prpLDlossCarRepair.getVeriManUnitPrice()));//核损工时总价
									prpLDlossCarRepair.setSumVeriLoss(prpLDlossCarRepair.getVeriManHour().multiply(prpLDlossCarRepair.getVeriManUnitPrice()));//核损金额
									if("2".equals(carLossDetailVO.getFitType())){
										prpLDlossCarRepair.setSelfConfigFlag("1");//是否自定义配件
									}else{
										prpLDlossCarRepair.setSelfConfigFlag("0");
									}
									prpLDlossCarRepairs.add(prpLDlossCarRepair);
									j++;
								}else if("3".equals(carLossDetailVO.getLossType())){
									PrpLDlossCarRepairVo prpLDlossCarRepair=new PrpLDlossCarRepairVo();
									prpLDlossCarRepair.setRegistNo(registNo);
									prpLDlossCarRepair.setSerialNo(j);
									prpLDlossCarRepair.setRepairFlag("1");//外修
									prpLDlossCarRepair.setLicenseNo(carObjectDTO.getCarMark());//车牌号
									prpLDlossCarRepair.setCompCode(carLossDetailVO.getFitsCode());
									prpLDlossCarRepair.setCompName(carLossDetailVO.getLossName());
									prpLDlossCarRepair.setManHour(StringUtils.isNotBlank(carLossDetailVO.getLossCount())?new BigDecimal(carLossDetailVO.getLossCount()):new BigDecimal(0));//数量
									prpLDlossCarRepair.setManHourUnitPrice(StringUtils.isNotBlank(carLossDetailVO.getLossAmount())?new BigDecimal(carLossDetailVO.getLossAmount()):new BigDecimal(0));//单价
									prpLDlossCarRepair.setManHourFee(prpLDlossCarRepair.getManHour().multiply(prpLDlossCarRepair.getManHourUnitPrice()));//定损工时总价
									prpLDlossCarRepair.setSumDefLoss(prpLDlossCarRepair.getManHour().multiply(prpLDlossCarRepair.getManHourUnitPrice()));//定损总价
									prpLDlossCarRepair.setVeriManHour(StringUtils.isNotBlank(carLossDetailVO.getLossCount())?new BigDecimal(carLossDetailVO.getLossCount()):new BigDecimal(0));//核损数量
									prpLDlossCarRepair.setVeriManUnitPrice(StringUtils.isNotBlank(carLossDetailVO.getLossAgreeAmount())?new BigDecimal(carLossDetailVO.getLossAgreeAmount()):new BigDecimal(0));//核损单价
									prpLDlossCarRepair.setVeriManHourFee(prpLDlossCarRepair.getVeriManHour().multiply(prpLDlossCarRepair.getVeriManUnitPrice()));//核损工时总价
									prpLDlossCarRepair.setSumVeriLoss(prpLDlossCarRepair.getVeriManHour().multiply(prpLDlossCarRepair.getVeriManUnitPrice()));//核损金额
									if("2".equals(carLossDetailVO.getFitType())){
										prpLDlossCarRepair.setSelfConfigFlag("1");//是否自定义配件
									}else{
										prpLDlossCarRepair.setSelfConfigFlag("0");
									}
									prpLDlossCarRepairs.add(prpLDlossCarRepair);
									j++;
								}
								
							}
						}
						
						prpLDlossCarMain.setPrpLDlossCarComps(prpLDlossCarComps);
						prpLDlossCarMain.setPrpLDlossCarRepairs(prpLDlossCarRepairs);
						if(prpLScheduleDefLossVo!=null){
							prpLDlossCarMain.setScheduleDeflossId(prpLScheduleDefLossVo.getId());
						}
						PrpLDlossCarInfoVo prpLDlossCarInfo=new PrpLDlossCarInfoVo();
						prpLDlossCarInfo.setRegistNo(registNo);//报案号
						prpLDlossCarInfo.setLossCarKindCode("01");//定损车种
						prpLDlossCarInfo.setLicenseNo(carObjectDTO.getCarMark());//车牌号
						prpLDlossCarInfo.setEngineNo(carObjectDTO.getEngineNo());//发动机号
						prpLDlossCarInfo.setVinNo(carObjectDTO.getVin());//vin码
						prpLDlossCarInfo.setFrameNo(carObjectDTO.getVin());
						prpLDlossCarInfo.setCreateTime(new Date());
						prpLDlossCarInfo.setCreateUser("AUTO");
						prpLDlossCarInfo.setOperatorCode("AUTO");
						prpLDlossCarInfo.setValidFlag("1");
						PiccCodeDictVo  piccCodeDictVo3=pingAnDictService.getDictData(PingAnCodeTypeEnum.HPZL.getCodeType(),carObjectDTO.getLicensePlateType());//驾驶员证件类型
						prpLDlossCarInfo.setLicenseType(piccCodeDictVo3.getDhCodeCode());//号牌种类
						if("1".equals(prpLDlossCarMain.getDeflossCarType())){
							prpLDlossCarInfo.setCarOwner(prpLCheckCarInfoVo.getCarOwner());//车主
							prpLDlossCarInfo.setDriveName(carObjectDTO.getDriverName());//驾驶员
							prpLDlossCarInfo.setDrivingLicenseNo(carObjectDTO.getLicenseNo());//驾驶员证件号码
							PiccCodeDictVo  piccCodeDictVo2=pingAnDictService.getDictData(PingAnCodeTypeEnum.JSZLX.getCodeType(),carObjectDTO.getLicenseType());//驾驶员证件类型
							prpLDlossCarInfo.setIdentifyType(piccCodeDictVo2.getDhCodeCode());//驾驶证类型
							prpLDlossCarInfo.setCarKindCode(prpLCheckCarInfoVo.getPlatformCarKindCode());
						}else{
							prpLDlossCarInfo.setCarOwner(thirdInfoVo.getOwnerName());//车主
							prpLDlossCarInfo.setDriveName(thirdInfoVo.getDriverName());//驾驶员
							prpLDlossCarInfo.setDrivingLicenseNo(thirdInfoVo.getDriverLicenseNo());//驾驶员证件号码
							PiccCodeDictVo  piccCodeDictVo2=pingAnDictService.getDictData(PingAnCodeTypeEnum.JSZLX.getCodeType(),thirdInfoVo.getDriverLicenseType());//驾驶员证件类型
							prpLDlossCarInfo.setIdentifyType(piccCodeDictVo2.getDhCodeCode());//驾驶证类型
							prpLDlossCarInfo.setPlatformCarKindCode(prpLCheckCarInfoVo.getPlatformCarKindCode());
						}
						mainVo.setLossCarInfoVo(prpLDlossCarInfo);
						prpLDlossCarMain.setLossCarInfoVo(prpLDlossCarInfo);
					    lossCarService.updateAndSaveAndDelSons(prpLDlossCarMain);
				}else{//其它做新增操作
					PrpLDlossCarMain prpLDlossCarMain=new PrpLDlossCarMain();
					prpLDlossCarMain.setRegistNo(registNo);//报案号
					prpLDlossCarMain.setRiskCode(prpLRegistVo.getRiskCode());//险别
					prpLDlossCarMain.setLicenseNo(carObjectDTO.getCarMark());//车牌号
					prpLDlossCarMain.setComCode(comcode);
					prpLDlossCarMain.setMakeCom(comcode);
					prpLDlossCarMain.setPayTimes(jsonObject.get("caseTimes").toString());
					if(prpLDlossCarMainVo==null){
						prpLDlossCarMain.setLflag("L");//通赔
						prpLDlossCarMain.setMercyFlag("1");//案件紧急程度
						if("01".equals(carObjectDTO.getLossType())){
							prpLDlossCarMain.setDeflossCarType("1");//标的车
							prpLDlossCarMain.setSerialNo(1);
						}else{
							prpLDlossCarMain.setDeflossCarType("3");//三者车
							if(prpLDlossCarMainVos!=null && prpLDlossCarMainVos.size()>0){
								Integer serialNo = 2;
								for (PrpLDlossCarMainVo  dossCarMainVo : prpLDlossCarMainVos) {
									if("3".equals(dossCarMainVo.getDeflossCarType())){
										serialNo = serialNo+1;
									}
								}
								prpLDlossCarMain.setSerialNo(serialNo);
							}else{
								prpLDlossCarMain.setSerialNo(2);
							}
						}

						prpLDlossCarMain.setCetainLossType("01");//定损方式
						prpLDlossCarMain.setRepairFactoryCode(carLossVO.getGarageCode());//修理厂代码
						prpLDlossCarMain.setRepairFactoryName(carLossVO.getGarageName());//修理厂名称
						prpLDlossCarMain.setRepairFactoryType("003");//二类修理厂
						prpLDlossCarMain.setLossLevel("99");//其他
						prpLDlossCarMain.setHandlerCode("AUTO");
						prpLDlossCarMain.setHandlerName("AUTO");
						prpLDlossCarMain.setHandlerIdNo(carObjectDTO.getLicenseNo());//定损员证件号
						prpLDlossCarMain.setDeflossDate(new Date());
						String lossParts="";
						if(StringUtil.isNotBlank(carLossVO.getLossPosition())){
							String[] parts=carLossVO.getLossPosition().split(";");
							if(parts!=null && parts.length>0){
								for(int i=0;i<parts.length;i++){
									if(i==0){
										lossParts=parts[i];
									}else{
										lossParts=lossParts+","+parts[i];
									}
								}
							}
						}
						prpLDlossCarMain.setPaId(pingAnDataNoticeVo.getId());//平安联盟业务主表id
						prpLDlossCarMain.setValidFlag("1");
						prpLDlossCarMain.setLossPart(lossParts);//损失部位
						prpLDlossCarMain.setSumCompFee(StringUtils.isNotBlank(carLossVO.getMaterialAmount())?new BigDecimal(carLossVO.getMaterialAmount()):new BigDecimal(0));//定损换件金额
						prpLDlossCarMain.setSumRepairFee(StringUtils.isNotBlank(carLossVO.getManpowerAmount())?new BigDecimal(carLossVO.getManpowerAmount()):new BigDecimal(0));//定损修理金额
						prpLDlossCarMain.setSumVeriRepairFee(StringUtils.isNotBlank(carLossVO.getManpowerAgreedAmount())?new BigDecimal(carLossVO.getManpowerAgreedAmount()):new BigDecimal(0));//核损修理金额
						prpLDlossCarMain.setSumMatFee(new BigDecimal(0));//辅料费
						prpLDlossCarMain.setSumVeriCompFee(StringUtils.isNotBlank(carLossVO.getMaterialAgreedAmount())?new BigDecimal(carLossVO.getMaterialAgreedAmount()):new BigDecimal(0));//核损换件金额
						prpLDlossCarMain.setSumVeriMatFee(new BigDecimal(0));//核损辅料费
						//prpLDlossCarMain.setSumOutFee(sumOutFee);
						prpLDlossCarMain.setSumRemnant(StringUtils.isNotBlank(carLossVO.getSurveyReduceAmount())?new BigDecimal(carLossVO.getSurveyReduceAmount()):new BigDecimal(0));//定损残值
						prpLDlossCarMain.setSumVeriRemnant(StringUtils.isNotBlank(carLossVO.getReduceAmount())?new BigDecimal(carLossVO.getReduceAmount()):new BigDecimal(0));//最终残值
						prpLDlossCarMain.setSumRescueFee(StringUtils.isNotBlank(carLossVO.getRescueFee())?new BigDecimal(carLossVO.getRescueFee()):new BigDecimal(0));//定损施救费
						prpLDlossCarMain.setSumVeriRescueFee(StringUtils.isNotBlank(carLossVO.getRescueFee())?new BigDecimal(carLossVO.getRescueFee()):new BigDecimal(0));//核损施救费
						prpLDlossCarMain.setSumManageFee(StringUtils.isNotBlank(carLossVO.getManageFee())?new BigDecimal(carLossVO.getManageFee()):new BigDecimal(0));//定损管理费
						prpLDlossCarMain.setSumVeriManage(StringUtils.isNotBlank(carLossVO.getManageAgreedFee())?new BigDecimal(carLossVO.getManageAgreedFee()):new BigDecimal(0));//核损管理费
						prpLDlossCarMain.setSumLossFee(StringUtils.isNotBlank(carLossVO.getTotalAmount())?new BigDecimal(carLossVO.getTotalAmount()):new BigDecimal(0));//定损总金额
						prpLDlossCarMain.setSumVeriLossFee(StringUtils.isNotBlank(carLossVO.getTotalAgreedAmount())?new BigDecimal(carLossVO.getTotalAgreedAmount()):new BigDecimal(0));//核损总金额
						prpLDlossCarMain.setCreateUser("AUTO");
						prpLDlossCarMain.setCreateTime(new Date());
						prpLDlossCarMain.setUnderWiteIdNo(carObjectDTO.getLicenseNo());
						prpLDlossCarMain.setUnderWriteCode("AUTO");
						prpLDlossCarMain.setUnderWriteEndDate(new Date());
						prpLDlossCarMain.setUnderWriteFlag("1");//核损通过
						int i=1;//配件数量标识
						int j=0;//工时数量标识
						//配件
						List<PrpLDlossCarComp> prpLDlossCarComps = new ArrayList<PrpLDlossCarComp>(0);
						//修理工时
						List<PrpLDlossCarRepair> prpLDlossCarRepairs = new ArrayList<PrpLDlossCarRepair>(0);
						if(respCarDlossDataVo.getCarLossDetailVOList()!=null && respCarDlossDataVo.getCarLossDetailVOList().size()>0){
							for(CarLossDetailVO carLossDetailVO:respCarDlossDataVo.getCarLossDetailVOList()){
								if("1".equals(carLossDetailVO.getLossType())){
									PrpLDlossCarComp prpLDlossCarComp=new PrpLDlossCarComp();
									prpLDlossCarComp.setPrpLDlossCarMain(prpLDlossCarMain);//配件表
									prpLDlossCarComp.setRegistNo(registNo);//报案号
									prpLDlossCarComp.setSerialNo(i);
									prpLDlossCarComp.setLicenseNo(carObjectDTO.getCarMark());//车牌号
									prpLDlossCarComp.setCompCode(carLossDetailVO.getFitsCode());
									prpLDlossCarComp.setCompName(carLossDetailVO.getLossName());//配件名称
									if(StringUtils.isNotBlank(carLossDetailVO.getLossCount())){
										prpLDlossCarComp.setQuantity(Integer.valueOf(carLossDetailVO.getLossCount()));//损失数量
									}else{
										prpLDlossCarComp.setQuantity(0);
									}
									prpLDlossCarComp.setMaterialFee(StringUtils.isNotBlank(carLossDetailVO.getLossAmount())?new BigDecimal(carLossDetailVO.getLossAmount()):new BigDecimal(0));//定损单价
									prpLDlossCarComp.setSumDefLoss(prpLDlossCarComp.getMaterialFee().multiply(new BigDecimal(prpLDlossCarComp.getQuantity())));//定损总价
									if(StringUtils.isNotBlank(carLossDetailVO.getLossCount())){
										prpLDlossCarComp.setVeriQuantity(Integer.valueOf(carLossDetailVO.getLossCount()));//核损损失数量
									}else{
										prpLDlossCarComp.setVeriQuantity(0);
									}
									prpLDlossCarComp.setVeriMaterFee(StringUtils.isNotBlank(carLossDetailVO.getLossAgreeAmount())?new BigDecimal(carLossDetailVO.getLossAgreeAmount()):new BigDecimal(0));//核损单价
									prpLDlossCarComp.setSumVeriLoss(prpLDlossCarComp.getVeriMaterFee().multiply(new BigDecimal(prpLDlossCarComp.getVeriQuantity())));//核损总价
									prpLDlossCarComp.setOriginalId(carLossDetailVO.getOriginalCode());//原厂编码
									prpLDlossCarComp.setManageFeeRate(StringUtils.isNotBlank(carLossDetailVO.getFitsFeeRate())?new BigDecimal(carLossDetailVO.getFitsFeeRate()):null);//管理费比例
									prpLDlossCarComp.setManageSingleFee(StringUtils.isNotBlank(carLossDetailVO.getFitsFee())?new BigDecimal(carLossDetailVO.getFitsFee()):new BigDecimal(0));//管理费
									prpLDlossCarComps.add(prpLDlossCarComp);
									i++;
								}else if("2".equals(carLossDetailVO.getLossType())){
									PrpLDlossCarRepair prpLDlossCarRepair=new PrpLDlossCarRepair();
									prpLDlossCarRepair.setRegistNo(registNo);
									prpLDlossCarRepair.setPrpLDlossCarMain(prpLDlossCarMain);
									prpLDlossCarRepair.setSerialNo(j);
									prpLDlossCarRepair.setRepairFlag("0");//内修
									prpLDlossCarRepair.setLicenseNo(carObjectDTO.getCarMark());//车牌号
									prpLDlossCarRepair.setCompCode(carLossDetailVO.getFitsCode());
									prpLDlossCarRepair.setCompName(carLossDetailVO.getLossName());
									prpLDlossCarRepair.setManHour(StringUtils.isNotBlank(carLossDetailVO.getLossCount())?new BigDecimal(carLossDetailVO.getLossCount()):new BigDecimal(0));//数量
									prpLDlossCarRepair.setManHourUnitPrice(StringUtils.isNotBlank(carLossDetailVO.getLossAmount())?new BigDecimal(carLossDetailVO.getLossAmount()):new BigDecimal(0));//单价
									prpLDlossCarRepair.setManHourFee(prpLDlossCarRepair.getManHour().multiply(prpLDlossCarRepair.getManHourUnitPrice()));//定损工时总价
									prpLDlossCarRepair.setSumDefLoss(prpLDlossCarRepair.getManHour().multiply(prpLDlossCarRepair.getManHourUnitPrice()));//定损总价
									prpLDlossCarRepair.setVeriManHour(StringUtils.isNotBlank(carLossDetailVO.getLossCount())?new BigDecimal(carLossDetailVO.getLossCount()):new BigDecimal(0));//核损数量
									prpLDlossCarRepair.setVeriManUnitPrice(StringUtils.isNotBlank(carLossDetailVO.getLossAgreeAmount())?new BigDecimal(carLossDetailVO.getLossAgreeAmount()):new BigDecimal(0));//核损单价
									prpLDlossCarRepair.setVeriManHourFee(prpLDlossCarRepair.getVeriManHour().multiply(prpLDlossCarRepair.getVeriManUnitPrice()));//核损工时总价
									prpLDlossCarRepair.setSumVeriLoss(prpLDlossCarRepair.getVeriManHour().multiply(prpLDlossCarRepair.getVeriManUnitPrice()));//核损金额
									if("2".equals(carLossDetailVO.getFitType())){
										prpLDlossCarRepair.setSelfConfigFlag("1");//是否自定义配件
									}else{
										prpLDlossCarRepair.setSelfConfigFlag("0");
									}
									prpLDlossCarRepairs.add(prpLDlossCarRepair);
									j++;
								}else if("3".equals(carLossDetailVO.getLossType())){
									PrpLDlossCarRepair prpLDlossCarRepair=new PrpLDlossCarRepair();
									prpLDlossCarRepair.setRegistNo(registNo);
									prpLDlossCarRepair.setPrpLDlossCarMain(prpLDlossCarMain);
									prpLDlossCarRepair.setSerialNo(j);
									prpLDlossCarRepair.setRepairFlag("1");//外修
									prpLDlossCarRepair.setLicenseNo(carObjectDTO.getCarMark());//车牌号
									prpLDlossCarRepair.setCompCode(carLossDetailVO.getFitsCode());
									prpLDlossCarRepair.setCompName(carLossDetailVO.getLossName());
									prpLDlossCarRepair.setManHour(StringUtils.isNotBlank(carLossDetailVO.getLossCount())?new BigDecimal(carLossDetailVO.getLossCount()):new BigDecimal(0));//数量
									prpLDlossCarRepair.setManHourUnitPrice(StringUtils.isNotBlank(carLossDetailVO.getLossAmount())?new BigDecimal(carLossDetailVO.getLossAmount()):new BigDecimal(0));//单价
									prpLDlossCarRepair.setManHourFee(prpLDlossCarRepair.getManHour().multiply(prpLDlossCarRepair.getManHourUnitPrice()));//定损工时总价
									prpLDlossCarRepair.setSumDefLoss(prpLDlossCarRepair.getManHour().multiply(prpLDlossCarRepair.getManHourUnitPrice()));//定损总价
									prpLDlossCarRepair.setVeriManHour(StringUtils.isNotBlank(carLossDetailVO.getLossCount())?new BigDecimal(carLossDetailVO.getLossCount()):new BigDecimal(0));//核损数量
									prpLDlossCarRepair.setVeriManUnitPrice(StringUtils.isNotBlank(carLossDetailVO.getLossAgreeAmount())?new BigDecimal(carLossDetailVO.getLossAgreeAmount()):new BigDecimal(0));//核损单价
									prpLDlossCarRepair.setVeriManHourFee(prpLDlossCarRepair.getVeriManHour().multiply(prpLDlossCarRepair.getVeriManUnitPrice()));//核损工时总价
									prpLDlossCarRepair.setSumVeriLoss(prpLDlossCarRepair.getVeriManHour().multiply(prpLDlossCarRepair.getVeriManUnitPrice()));//核损金额
									if("2".equals(carLossDetailVO.getFitType())){
										prpLDlossCarRepair.setSelfConfigFlag("1");//是否自定义配件
									}else{
										prpLDlossCarRepair.setSelfConfigFlag("0");
									}
									prpLDlossCarRepairs.add(prpLDlossCarRepair);
									j++;
								}
								
							}
						}
						
						prpLDlossCarMain.setPrpLDlossCarComps(prpLDlossCarComps);
						prpLDlossCarMain.setPrpLDlossCarRepairs(prpLDlossCarRepairs);
						if(prpLScheduleDefLossVo!=null){
							prpLDlossCarMain.setScheduleDeflossId(prpLScheduleDefLossVo.getId());
						}
						databaseDao.save(PrpLDlossCarMain.class,prpLDlossCarMain);
						PrpLDlossCarInfo prpLDlossCarInfo=new PrpLDlossCarInfo();
						prpLDlossCarInfo.setRegistNo(registNo);//报案号
						prpLDlossCarInfo.setLossCarKindCode("01");//定损车种
						prpLDlossCarInfo.setLicenseNo(carObjectDTO.getCarMark());//车牌号
						prpLDlossCarInfo.setEngineNo(carObjectDTO.getEngineNo());//发动机号
						prpLDlossCarInfo.setVinNo(carObjectDTO.getVin());//vin码
						prpLDlossCarInfo.setFrameNo(carObjectDTO.getVin());
						prpLDlossCarInfo.setCreateTime(new Date());
						prpLDlossCarInfo.setCreateUser("AUTO");
						prpLDlossCarInfo.setOperatorCode("AUTO");
						prpLDlossCarInfo.setValidFlag("1");
						PiccCodeDictVo  piccCodeDictVo3=pingAnDictService.getDictData(PingAnCodeTypeEnum.HPZL.getCodeType(),carObjectDTO.getLicensePlateType());//驾驶员证件类型
						prpLDlossCarInfo.setLicenseType(piccCodeDictVo3.getDhCodeCode());//号牌种类
						if("1".equals(prpLDlossCarMain.getDeflossCarType())){
							prpLDlossCarInfo.setCarOwner(prpLCheckCarInfoVo.getCarOwner());//车主
							prpLDlossCarInfo.setDriveName(carObjectDTO.getDriverName());//驾驶员
							prpLDlossCarInfo.setDrivingLicenseNo(carObjectDTO.getLicenseNo());//驾驶员证件号码
							PiccCodeDictVo  piccCodeDictVo2=pingAnDictService.getDictData(PingAnCodeTypeEnum.JSZLX.getCodeType(),carObjectDTO.getLicenseType());//驾驶员证件类型
							prpLDlossCarInfo.setIdentifyType(piccCodeDictVo2.getDhCodeCode());//驾驶证类型
							prpLDlossCarInfo.setCarKindCode(prpLCheckCarInfoVo.getPlatformCarKindCode());
						}else{
							prpLDlossCarInfo.setCarOwner(thirdInfoVo.getOwnerName());//车主
							prpLDlossCarInfo.setDriveName(thirdInfoVo.getDriverName());//驾驶员
							prpLDlossCarInfo.setDrivingLicenseNo(thirdInfoVo.getDriverLicenseNo());//驾驶员证件号码
							PiccCodeDictVo  piccCodeDictVo2=pingAnDictService.getDictData(PingAnCodeTypeEnum.JSZLX.getCodeType(),thirdInfoVo.getDriverLicenseType());//驾驶员证件类型
							prpLDlossCarInfo.setIdentifyType(piccCodeDictVo2.getDhCodeCode());//驾驶证类型
							prpLDlossCarInfo.setPlatformCarKindCode(prpLCheckCarInfoVo.getPlatformCarKindCode());
						}
						databaseDao.save(PrpLDlossCarInfo.class,prpLDlossCarInfo);
						prpLDlossCarMain.setCarId(prpLDlossCarInfo.getId());
						PrpLDlossCarInfoVo prpLDlossCarInfoVo=new PrpLDlossCarInfoVo();
						Beans.copy().from(prpLDlossCarInfo).to(prpLDlossCarInfoVo);
						Beans.copy().from(prpLDlossCarMain).to(mainVo);
						mainVo.setLossCarInfoVo(prpLDlossCarInfoVo);
					}else{
						mainVo=prpLDlossCarMainVo;
					}
				}
				
			if("0".equals(payFlag)){//同一辆车的，同一次赔付，不更新工作流操作
				List<PrpLWfTaskVo> prpLWfTaskinVos=wfFlowQueryService.findPrpWfTaskVoForIn(registNo,FlowNode.DLoss.name(),FlowNode.DLCar.name());
				if("0".equals(flag) && dlossCarMainVo!=null && "1".equals(carFlag)){//定损过的车辆追加车辆
					PrpLWfTaskVo prpLWfTaskVo=new PrpLWfTaskVo();
					prpLWfTaskVo.setNodeCode(FlowNode.DLoss.name());
					prpLWfTaskVo.setTaskName(FlowNode.DLoss.getName());
					prpLWfTaskVo.setRegistNo(registNo);
					prpLWfTaskVo.setFlowId(wfTaskVo.getFlowId());
					prpLWfTaskVo.setSubNodeCode(FlowNode.DLCar.name());
					prpLWfTaskVo.setUpperTaskId(wfTaskVo.getTaskId());
					prpLWfTaskVo.setComCode(comcode);
					prpLWfTaskVo.setHandlerStatus(HandlerStatus.INIT);// 未处理
					prpLWfTaskVo.setWorkStatus(WorkStatus.INIT);// 未处理
					prpLWfTaskVo.setTaskInTime(new Date());
					prpLWfTaskVo.setTaskInUser("AUTO");
					prpLWfTaskVo.setTaskInKey(registNo);
					prpLWfTaskVo.setTaskInNode(FlowNode.Chk.name());
					prpLWfTaskVo.setAssignCom(comcode);
					prpLWfTaskVo.setAssignUser("AUTO");
					prpLWfTaskVo.setHandlerUser("AUTO");
					prpLWfTaskVo.setHandlerCom(comcode);
					prpLWfTaskVo.setHandlerTime(new Date());
					if("1".equals(mainVo.getDeflossCarType())){
						prpLWfTaskVo.setItemName("标的车"+licenceNo);
					}else{
						prpLWfTaskVo.setItemName("三者车"+licenceNo);
					}
					prpLWfTaskVo.setTaskOutUser("AUTO");
					prpLWfTaskVo.setTaskOutTime(new Date());
	                prpLWfTaskVo.setAcceptOffTime(null);
	                prpLWfTaskVo.setBussTag(prpLWfTaskVo.getBussTag());
	                prpLWfTaskVo.setShowInfoXML(prpLWfTaskVo.getShowInfoXML());
	                prpLWfTaskVo.setHandlerIdKey(mainVo.getId().toString());
	                PrpLWfTaskVo taskInVo=wfTaskHandleService.saveTaskIn(prpLWfTaskVo);
	                if(taskInVo.getTaskId()!=null){
						WfTaskSubmitVo taskSubmitVo = new WfTaskSubmitVo();
						taskSubmitVo.setCurrentNode(FlowNode.DLCar);
						taskSubmitVo.setAssignCom(comcode);
						taskSubmitVo.setFlowId(taskInVo.getFlowId());
						taskSubmitVo.setFlowTaskId(taskInVo.getTaskId());
						taskSubmitVo.setNextNode(FlowNode.valueOf(FlowNode.VLCar_LV0.name()));
						taskSubmitVo.setHandleruser("AUTO");
						taskSubmitVo.setComCode(comcode);
						taskSubmitVo.setTaskInUser("AUTO");
						taskSubmitVo.setTaskInKey(taskInVo.getTaskInKey());
						List<PrpLWfTaskVo> wfTaskVoList = wfTaskHandleService.submitLossCar(mainVo,taskSubmitVo);
						if(wfTaskVoList!=null && wfTaskVoList.size()>0){
							for(PrpLWfTaskVo taskVo:wfTaskVoList){
								if("VLoss".equals(taskVo.getNodeCode())){
									WfTaskSubmitVo submitVo = new WfTaskSubmitVo();
									submitVo.setCurrentNode(FlowNode.VLCar_LV0);
									submitVo.setAssignCom(comcode);
									submitVo.setFlowId(taskVo.getFlowId());
									submitVo.setFlowTaskId(taskVo.getTaskId());
									submitVo.setNextNode(FlowNode.valueOf(FlowNode.END.name()));
									submitVo.setHandleruser("AUTO");
									submitVo.setTaskInKey(taskVo.getTaskInKey());
									submitVo.setTaskInUser("AUTO");
									submitVo.setComCode(comcode);
									List<PrpLWfTaskVo> wtaskVoList = wfTaskHandleService.submitLossCar(mainVo,submitVo);
									break;
								}
							}
						}
					}
				}else if(dlossCarMainVo==null && (prpLWfTaskinVos==null || prpLWfTaskinVos.size()==0)){//新车辆的定损追加
					List<PrpLWfTaskVo> wftaskCheckVoList=wfFlowQueryService.findPrpWfTaskVoForOut(registNo, FlowNode.Check.name());
					if(wftaskCheckVoList!=null && wftaskCheckVoList.size()>0){
						PrpLWfTaskVo prpLWfTaskVo=new PrpLWfTaskVo();
						prpLWfTaskVo.setNodeCode(FlowNode.DLoss.name());
						prpLWfTaskVo.setTaskName(FlowNode.DLoss.getName());
						prpLWfTaskVo.setRegistNo(registNo);
						prpLWfTaskVo.setFlowId(wftaskCheckVoList.get(0).getFlowId());
						prpLWfTaskVo.setSubNodeCode(FlowNode.DLCar.name());
						prpLWfTaskVo.setUpperTaskId(wftaskCheckVoList.get(0).getTaskId());
						prpLWfTaskVo.setComCode(comcode);
						prpLWfTaskVo.setHandlerStatus(HandlerStatus.INIT);// 未处理
						prpLWfTaskVo.setWorkStatus(WorkStatus.INIT);// 未处理
						prpLWfTaskVo.setTaskInTime(new Date());
						prpLWfTaskVo.setTaskInUser("AUTO");
						prpLWfTaskVo.setTaskInKey(registNo);
						prpLWfTaskVo.setTaskInNode(FlowNode.Chk.name());
						prpLWfTaskVo.setAssignCom(comcode);
						prpLWfTaskVo.setAssignUser("AUTO");
						prpLWfTaskVo.setHandlerUser("AUTO");
						prpLWfTaskVo.setHandlerCom(comcode);
						prpLWfTaskVo.setHandlerTime(new Date());
						if("1".equals(mainVo.getDeflossCarType())){
							prpLWfTaskVo.setItemName("标的车"+licenceNo);
						}else{
							prpLWfTaskVo.setItemName("三者车"+licenceNo);
						}
						prpLWfTaskVo.setTaskOutUser("AUTO");
						prpLWfTaskVo.setTaskOutTime(new Date());
		                prpLWfTaskVo.setAcceptOffTime(null);
		                prpLWfTaskVo.setBussTag(wftaskCheckVoList.get(0).getBussTag());
		                prpLWfTaskVo.setShowInfoXML(wftaskCheckVoList.get(0).getShowInfoXML());
		                prpLWfTaskVo.setHandlerIdKey(mainVo.getId().toString());
		                PrpLWfTaskVo taskInVo=wfTaskHandleService.saveTaskIn(prpLWfTaskVo);
		                if(taskInVo.getTaskId()!=null){
							WfTaskSubmitVo taskSubmitVo = new WfTaskSubmitVo();
							taskSubmitVo.setCurrentNode(FlowNode.DLCar);
							taskSubmitVo.setAssignCom(comcode);
							taskSubmitVo.setFlowId(taskInVo.getFlowId());
							taskSubmitVo.setFlowTaskId(taskInVo.getTaskId());
							taskSubmitVo.setNextNode(FlowNode.valueOf(FlowNode.VLCar_LV0.name()));
							taskSubmitVo.setHandleruser("AUTO");
							taskSubmitVo.setComCode(comcode);
							taskSubmitVo.setTaskInUser("AUTO");
							taskSubmitVo.setTaskInKey(taskInVo.getTaskInKey());
							List<PrpLWfTaskVo> wfTaskVoList = wfTaskHandleService.submitLossCar(mainVo,taskSubmitVo);
							if(wfTaskVoList!=null && wfTaskVoList.size()>0){
								for(PrpLWfTaskVo taskVo:wfTaskVoList){
									if("VLoss".equals(taskVo.getNodeCode())){
										WfTaskSubmitVo submitVo = new WfTaskSubmitVo();
										submitVo.setCurrentNode(FlowNode.VLCar_LV0);
										submitVo.setAssignCom(comcode);
										submitVo.setFlowId(taskVo.getFlowId());
										submitVo.setFlowTaskId(taskVo.getTaskId());
										submitVo.setNextNode(FlowNode.valueOf(FlowNode.END.name()));
										submitVo.setHandleruser("AUTO");
										submitVo.setTaskInKey(taskVo.getTaskInKey());
										submitVo.setTaskInUser("AUTO");
										submitVo.setComCode(comcode);
										List<PrpLWfTaskVo> wtaskVoList = wfTaskHandleService.submitLossCar(mainVo,submitVo);
										break;
									}
								}
							}
						}
					}
				}else{//正常定损车辆
					List<PrpLWfTaskVo> taskinVos=wfFlowQueryService.findPrpWfTaskVoForIn(registNo,FlowNode.VLoss.name(),FlowNode.VLCar_LV0.name());
					if(prpLWfTaskinVos!=null && prpLWfTaskinVos.size()>0){//因为查询是倒叙的，由于先生成的工作流任务要先处理，则从最时间最早的开始匹配
						for(int i=prpLWfTaskinVos.size()-1;i>=0;i--){
							if(StringUtils.isNotBlank(prpLWfTaskinVos.get(i).getItemName()) && prpLWfTaskinVos.get(i).getItemName().contains(licenceNo)){
								prpLWfTaskinVos.get(i).setHandlerIdKey(mainVo.getId().toString());
								wfTaskHandleService.updateTaskIn(prpLWfTaskinVos.get(i));
								WfTaskSubmitVo taskSubmitVo = new WfTaskSubmitVo();
								taskSubmitVo.setCurrentNode(FlowNode.DLCar);
								taskSubmitVo.setAssignCom(comcode);
								taskSubmitVo.setFlowId(prpLWfTaskinVos.get(i).getFlowId());
								taskSubmitVo.setFlowTaskId(prpLWfTaskinVos.get(i).getTaskId());
								taskSubmitVo.setNextNode(FlowNode.valueOf(FlowNode.VLCar_LV0.name()));
								taskSubmitVo.setHandleruser("AUTO");
								taskSubmitVo.setComCode(comcode);
								taskSubmitVo.setTaskInUser("AUTO");
								taskSubmitVo.setTaskInKey(prpLWfTaskinVos.get(i).getTaskInKey());
								List<PrpLWfTaskVo> wfTaskVoList = wfTaskHandleService.submitLossCar(mainVo,taskSubmitVo);
								if(wfTaskVoList!=null && wfTaskVoList.size()>0){
									for(PrpLWfTaskVo taskVo:wfTaskVoList){
										if("VLoss".equals(taskVo.getNodeCode())){
											WfTaskSubmitVo submitVo = new WfTaskSubmitVo();
											submitVo.setCurrentNode(FlowNode.VLCar_LV0);
											submitVo.setAssignCom(comcode);
											submitVo.setFlowId(taskVo.getFlowId());
											submitVo.setFlowTaskId(taskVo.getTaskId());
											submitVo.setNextNode(FlowNode.valueOf(FlowNode.END.name()));
											submitVo.setHandleruser("AUTO");
											submitVo.setTaskInKey(taskVo.getTaskInKey());
											submitVo.setTaskInUser("AUTO");
											submitVo.setComCode(comcode);
											List<PrpLWfTaskVo> wtaskVoList = wfTaskHandleService.submitLossCar(mainVo,submitVo);
											break;
										}
									}
								}
								break;
							}
						}
					}else{//定损工作流处理成功，核损未成功。
						if(taskinVos!=null && taskinVos.size()>0){
							for(PrpLWfTaskVo taskVo:taskinVos){
								if(StringUtils.isNotBlank(taskVo.getItemName()) && taskVo.getItemName().contains(licenceNo)){
									WfTaskSubmitVo submitVo = new WfTaskSubmitVo();
									submitVo.setCurrentNode(FlowNode.VLCar_LV0);
									submitVo.setAssignCom(comcode);
									submitVo.setFlowId(taskVo.getFlowId());
									submitVo.setFlowTaskId(taskVo.getTaskId());
									submitVo.setNextNode(FlowNode.valueOf(FlowNode.END.name()));
									submitVo.setHandleruser("AUTO");
									submitVo.setTaskInKey(taskVo.getTaskInKey());
									submitVo.setTaskInUser("AUTO");
									submitVo.setComCode(comcode);
									List<PrpLWfTaskVo> wtaskVoList = wfTaskHandleService.submitLossCar(mainVo,submitVo);
									break;
								}
							}
						}
					}
				}
				
			}
			
		}catch(Exception e){
			logger.error("平安联盟车定损报错：",e);
			resultBean.fail("平安联盟车定损报错："+e.getMessage());
		}
		return resultBean;
	}
}
