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
	 * ????????????
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
		logger.info("??????????????????????????????--------------------------------???{}",respData);
		ResultBean resultBean=ResultBean.success();
		Gson gson=new Gson();
		String licenceNo="";//?????????
		PrpLDlossCarMainVo mainVo=new PrpLDlossCarMainVo();
		String comcode="";
		String flag="0";//0??????????????????,1????????????????????????
		String payFlag="0";//0????????????????????????1-???????????????(????????????)
		JSONObject jsonObject=JSON.parseObject(pingAnDataNoticeVo.getParamObj());
		String payTimes=jsonObject.get("caseTimes").toString();//????????????
		//????????????????????????????????????
		PrpLWfTaskVo wfTaskVo=new PrpLWfTaskVo();
		try{
			if(StringUtils.isBlank(registNo)){
				throw new IllegalAccessException("-----------------???????????????--------------------");
			}
			PrpLRegistVo prpLRegistVo=registService.findRegistByRegistNo(registNo);
			PrpLDlossCarMainVo prpLDlossCarMainVo=lossCarService.findLossCarMainByRegistNoAndPaId(registNo, pingAnDataNoticeVo.getId());
			if(prpLDlossCarMainVo!=null){
				flag="1";
			}
			
			comcode=prpLRegistVo.getComCode();
			RespCarDlossDataVo respCarDlossDataVo=gson.fromJson(respData, RespCarDlossDataVo.class);
				CarObjectDTO carObjectDTO=respCarDlossDataVo.getCarObjectDTO();
				licenceNo=carObjectDTO.getCarMark();//?????????
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
				String carFlag="0";//?????????????????????????????????????????????0-????????????1-?????????
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
					throw new IllegalAccessException("------?????????????????????????????????????????????????????????????????????????????????-----");
				}
				PrpLScheduleDefLossVo prpLScheduleDefLossVo=scheduleService.findCarDefLossByLicenseNo(registNo,licenceNo);
				CarLossVO carLossVO=respCarDlossDataVo.getCarLossVO();
				ThirdInfoVo thirdInfoVo=respCarDlossDataVo.getThirdInfo();
				if(thirdInfoVo==null){
					thirdInfoVo=new ThirdInfoVo();
				}
				//??????????????????
				PrpLCheckCarInfoVo prpLCheckCarInfoVo=new PrpLCheckCarInfoVo();
				List<PrpLCheckCarInfoVo> prpLCheckCarInfoVos=checkTaskService.findPrpLCheckCarInfoVoListByOther(registNo,carObjectDTO.getCarMark());
				if(prpLCheckCarInfoVos!=null && prpLCheckCarInfoVos.size()>0){
					prpLCheckCarInfoVo=prpLCheckCarInfoVos.get(0);
				}
				if("1".equals(payFlag)){//???????????????????????????????????????????????????????????????
					    PrpLDlossCarMainVo prpLDlossCarMain=new PrpLDlossCarMainVo();
					    prpLDlossCarMain.setId(mainVo.getId());
						prpLDlossCarMain.setRepairFactoryCode(carLossVO.getGarageCode());//???????????????
						prpLDlossCarMain.setRepairFactoryName(carLossVO.getGarageName());//???????????????
						prpLDlossCarMain.setHandlerIdNo(carObjectDTO.getLicenseNo());//??????????????????
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
						prpLDlossCarMain.setLossPart(lossParts);//????????????
						prpLDlossCarMain.setSumCompFee(StringUtils.isNotBlank(carLossVO.getMaterialAmount())?new BigDecimal(carLossVO.getMaterialAmount()):new BigDecimal(0));//??????????????????
						prpLDlossCarMain.setSumRepairFee(StringUtils.isNotBlank(carLossVO.getManpowerAmount())?new BigDecimal(carLossVO.getManpowerAmount()):new BigDecimal(0));//??????????????????
						prpLDlossCarMain.setSumVeriRepairFee(StringUtils.isNotBlank(carLossVO.getManpowerAgreedAmount())?new BigDecimal(carLossVO.getManpowerAgreedAmount()):new BigDecimal(0));//??????????????????
						prpLDlossCarMain.setSumMatFee(new BigDecimal(0));//?????????
						prpLDlossCarMain.setSumVeriCompFee(StringUtils.isNotBlank(carLossVO.getMaterialAgreedAmount())?new BigDecimal(carLossVO.getMaterialAgreedAmount()):new BigDecimal(0));//??????????????????
						prpLDlossCarMain.setSumVeriMatFee(new BigDecimal(0));//???????????????
						prpLDlossCarMain.setSumRemnant(StringUtils.isNotBlank(carLossVO.getSurveyReduceAmount())?new BigDecimal(carLossVO.getSurveyReduceAmount()):new BigDecimal(0));//????????????
						prpLDlossCarMain.setSumVeriRemnant(StringUtils.isNotBlank(carLossVO.getReduceAmount())?new BigDecimal(carLossVO.getReduceAmount()):new BigDecimal(0));//????????????
						prpLDlossCarMain.setSumRescueFee(StringUtils.isNotBlank(carLossVO.getRescueFee())?new BigDecimal(carLossVO.getRescueFee()):new BigDecimal(0));//???????????????
						prpLDlossCarMain.setSumVeriRescueFee(StringUtils.isNotBlank(carLossVO.getRescueFee())?new BigDecimal(carLossVO.getRescueFee()):new BigDecimal(0));//???????????????
						prpLDlossCarMain.setSumManageFee(StringUtils.isNotBlank(carLossVO.getManageFee())?new BigDecimal(carLossVO.getManageFee()):new BigDecimal(0));//???????????????
						prpLDlossCarMain.setSumVeriManage(StringUtils.isNotBlank(carLossVO.getManageAgreedFee())?new BigDecimal(carLossVO.getManageAgreedFee()):new BigDecimal(0));//???????????????
						prpLDlossCarMain.setSumLossFee(StringUtils.isNotBlank(carLossVO.getTotalAmount())?new BigDecimal(carLossVO.getTotalAmount()):new BigDecimal(0));//???????????????
						prpLDlossCarMain.setSumVeriLossFee(StringUtils.isNotBlank(carLossVO.getTotalAgreedAmount())?new BigDecimal(carLossVO.getTotalAgreedAmount()):new BigDecimal(0));//???????????????
						prpLDlossCarMain.setCreateUser("AUTO");
						prpLDlossCarMain.setCreateTime(new Date());
						prpLDlossCarMain.setUnderWiteIdNo(carObjectDTO.getLicenseNo());
						prpLDlossCarMain.setUnderWriteCode("AUTO");
						prpLDlossCarMain.setUnderWriteEndDate(new Date());
						prpLDlossCarMain.setUnderWriteFlag("1");//????????????
						int i=1;//??????????????????
						int j=0;//??????????????????
						//??????
						List<PrpLDlossCarCompVo> prpLDlossCarComps = new ArrayList<PrpLDlossCarCompVo>(0);
						//????????????
						List<PrpLDlossCarRepairVo> prpLDlossCarRepairs = new ArrayList<PrpLDlossCarRepairVo>(0);
						if(respCarDlossDataVo.getCarLossDetailVOList()!=null && respCarDlossDataVo.getCarLossDetailVOList().size()>0){
							for(CarLossDetailVO carLossDetailVO:respCarDlossDataVo.getCarLossDetailVOList()){
								if("1".equals(carLossDetailVO.getLossType())){
									PrpLDlossCarCompVo prpLDlossCarComp=new PrpLDlossCarCompVo();
									prpLDlossCarComp.setRegistNo(registNo);//?????????
									prpLDlossCarComp.setSerialNo(i);
									prpLDlossCarComp.setLicenseNo(carObjectDTO.getCarMark());//?????????
									prpLDlossCarComp.setCompCode(carLossDetailVO.getFitsCode());
									prpLDlossCarComp.setCompName(carLossDetailVO.getLossName());//????????????
									if(StringUtils.isNotBlank(carLossDetailVO.getLossCount())){
										prpLDlossCarComp.setQuantity(Integer.valueOf(carLossDetailVO.getLossCount()));//????????????
									}else{
										prpLDlossCarComp.setQuantity(0);
									}
									prpLDlossCarComp.setMaterialFee(StringUtils.isNotBlank(carLossDetailVO.getLossAmount())?new BigDecimal(carLossDetailVO.getLossAmount()):new BigDecimal(0));//????????????
									prpLDlossCarComp.setSumDefLoss(prpLDlossCarComp.getMaterialFee().multiply(new BigDecimal(prpLDlossCarComp.getQuantity())));//????????????
									if(StringUtils.isNotBlank(carLossDetailVO.getLossCount())){
										prpLDlossCarComp.setVeriQuantity(Integer.valueOf(carLossDetailVO.getLossCount()));//??????????????????
									}else{
										prpLDlossCarComp.setVeriQuantity(0);
									}
									prpLDlossCarComp.setVeriMaterFee(StringUtils.isNotBlank(carLossDetailVO.getLossAgreeAmount())?new BigDecimal(carLossDetailVO.getLossAgreeAmount()):new BigDecimal(0));//????????????
									prpLDlossCarComp.setSumVeriLoss(prpLDlossCarComp.getVeriMaterFee().multiply(new BigDecimal(prpLDlossCarComp.getVeriQuantity())));//????????????
									prpLDlossCarComp.setOriginalId(carLossDetailVO.getOriginalCode());//????????????
									prpLDlossCarComp.setManageFeeRate(StringUtils.isNotBlank(carLossDetailVO.getFitsFeeRate())?new BigDecimal(carLossDetailVO.getFitsFeeRate()):null);//???????????????
									prpLDlossCarComp.setManageSingleFee(StringUtils.isNotBlank(carLossDetailVO.getFitsFee())?new BigDecimal(carLossDetailVO.getFitsFee()):new BigDecimal(0));//?????????
									prpLDlossCarComps.add(prpLDlossCarComp);
									i++;
								}else if("2".equals(carLossDetailVO.getLossType())){
									PrpLDlossCarRepairVo prpLDlossCarRepair=new PrpLDlossCarRepairVo();
									prpLDlossCarRepair.setRegistNo(registNo);
									prpLDlossCarRepair.setSerialNo(j);
									prpLDlossCarRepair.setRepairFlag("0");//??????
									prpLDlossCarRepair.setLicenseNo(carObjectDTO.getCarMark());//?????????
									prpLDlossCarRepair.setCompCode(carLossDetailVO.getFitsCode());
									prpLDlossCarRepair.setCompName(carLossDetailVO.getLossName());
									prpLDlossCarRepair.setManHour(StringUtils.isNotBlank(carLossDetailVO.getLossCount())?new BigDecimal(carLossDetailVO.getLossCount()):new BigDecimal(0));//??????
									prpLDlossCarRepair.setManHourUnitPrice(StringUtils.isNotBlank(carLossDetailVO.getLossAmount())?new BigDecimal(carLossDetailVO.getLossAmount()):new BigDecimal(0));//??????
									prpLDlossCarRepair.setManHourFee(prpLDlossCarRepair.getManHour().multiply(prpLDlossCarRepair.getManHourUnitPrice()));//??????????????????
									prpLDlossCarRepair.setSumDefLoss(prpLDlossCarRepair.getManHour().multiply(prpLDlossCarRepair.getManHourUnitPrice()));//????????????
									prpLDlossCarRepair.setVeriManHour(StringUtils.isNotBlank(carLossDetailVO.getLossCount())?new BigDecimal(carLossDetailVO.getLossCount()):new BigDecimal(0));//????????????
									prpLDlossCarRepair.setVeriManUnitPrice(StringUtils.isNotBlank(carLossDetailVO.getLossAgreeAmount())?new BigDecimal(carLossDetailVO.getLossAgreeAmount()):new BigDecimal(0));//????????????
									prpLDlossCarRepair.setVeriManHourFee(prpLDlossCarRepair.getVeriManHour().multiply(prpLDlossCarRepair.getVeriManUnitPrice()));//??????????????????
									prpLDlossCarRepair.setSumVeriLoss(prpLDlossCarRepair.getVeriManHour().multiply(prpLDlossCarRepair.getVeriManUnitPrice()));//????????????
									if("2".equals(carLossDetailVO.getFitType())){
										prpLDlossCarRepair.setSelfConfigFlag("1");//?????????????????????
									}else{
										prpLDlossCarRepair.setSelfConfigFlag("0");
									}
									prpLDlossCarRepairs.add(prpLDlossCarRepair);
									j++;
								}else if("3".equals(carLossDetailVO.getLossType())){
									PrpLDlossCarRepairVo prpLDlossCarRepair=new PrpLDlossCarRepairVo();
									prpLDlossCarRepair.setRegistNo(registNo);
									prpLDlossCarRepair.setSerialNo(j);
									prpLDlossCarRepair.setRepairFlag("1");//??????
									prpLDlossCarRepair.setLicenseNo(carObjectDTO.getCarMark());//?????????
									prpLDlossCarRepair.setCompCode(carLossDetailVO.getFitsCode());
									prpLDlossCarRepair.setCompName(carLossDetailVO.getLossName());
									prpLDlossCarRepair.setManHour(StringUtils.isNotBlank(carLossDetailVO.getLossCount())?new BigDecimal(carLossDetailVO.getLossCount()):new BigDecimal(0));//??????
									prpLDlossCarRepair.setManHourUnitPrice(StringUtils.isNotBlank(carLossDetailVO.getLossAmount())?new BigDecimal(carLossDetailVO.getLossAmount()):new BigDecimal(0));//??????
									prpLDlossCarRepair.setManHourFee(prpLDlossCarRepair.getManHour().multiply(prpLDlossCarRepair.getManHourUnitPrice()));//??????????????????
									prpLDlossCarRepair.setSumDefLoss(prpLDlossCarRepair.getManHour().multiply(prpLDlossCarRepair.getManHourUnitPrice()));//????????????
									prpLDlossCarRepair.setVeriManHour(StringUtils.isNotBlank(carLossDetailVO.getLossCount())?new BigDecimal(carLossDetailVO.getLossCount()):new BigDecimal(0));//????????????
									prpLDlossCarRepair.setVeriManUnitPrice(StringUtils.isNotBlank(carLossDetailVO.getLossAgreeAmount())?new BigDecimal(carLossDetailVO.getLossAgreeAmount()):new BigDecimal(0));//????????????
									prpLDlossCarRepair.setVeriManHourFee(prpLDlossCarRepair.getVeriManHour().multiply(prpLDlossCarRepair.getVeriManUnitPrice()));//??????????????????
									prpLDlossCarRepair.setSumVeriLoss(prpLDlossCarRepair.getVeriManHour().multiply(prpLDlossCarRepair.getVeriManUnitPrice()));//????????????
									if("2".equals(carLossDetailVO.getFitType())){
										prpLDlossCarRepair.setSelfConfigFlag("1");//?????????????????????
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
						prpLDlossCarInfo.setRegistNo(registNo);//?????????
						prpLDlossCarInfo.setLossCarKindCode("01");//????????????
						prpLDlossCarInfo.setLicenseNo(carObjectDTO.getCarMark());//?????????
						prpLDlossCarInfo.setEngineNo(carObjectDTO.getEngineNo());//????????????
						prpLDlossCarInfo.setVinNo(carObjectDTO.getVin());//vin???
						prpLDlossCarInfo.setFrameNo(carObjectDTO.getVin());
						prpLDlossCarInfo.setCreateTime(new Date());
						prpLDlossCarInfo.setCreateUser("AUTO");
						prpLDlossCarInfo.setOperatorCode("AUTO");
						prpLDlossCarInfo.setValidFlag("1");
						PiccCodeDictVo  piccCodeDictVo3=pingAnDictService.getDictData(PingAnCodeTypeEnum.HPZL.getCodeType(),carObjectDTO.getLicensePlateType());//?????????????????????
						prpLDlossCarInfo.setLicenseType(piccCodeDictVo3.getDhCodeCode());//????????????
						if("1".equals(prpLDlossCarMain.getDeflossCarType())){
							prpLDlossCarInfo.setCarOwner(prpLCheckCarInfoVo.getCarOwner());//??????
							prpLDlossCarInfo.setDriveName(carObjectDTO.getDriverName());//?????????
							prpLDlossCarInfo.setDrivingLicenseNo(carObjectDTO.getLicenseNo());//?????????????????????
							PiccCodeDictVo  piccCodeDictVo2=pingAnDictService.getDictData(PingAnCodeTypeEnum.JSZLX.getCodeType(),carObjectDTO.getLicenseType());//?????????????????????
							prpLDlossCarInfo.setIdentifyType(piccCodeDictVo2.getDhCodeCode());//???????????????
							prpLDlossCarInfo.setCarKindCode(prpLCheckCarInfoVo.getPlatformCarKindCode());
						}else{
							prpLDlossCarInfo.setCarOwner(thirdInfoVo.getOwnerName());//??????
							prpLDlossCarInfo.setDriveName(thirdInfoVo.getDriverName());//?????????
							prpLDlossCarInfo.setDrivingLicenseNo(thirdInfoVo.getDriverLicenseNo());//?????????????????????
							PiccCodeDictVo  piccCodeDictVo2=pingAnDictService.getDictData(PingAnCodeTypeEnum.JSZLX.getCodeType(),thirdInfoVo.getDriverLicenseType());//?????????????????????
							prpLDlossCarInfo.setIdentifyType(piccCodeDictVo2.getDhCodeCode());//???????????????
							prpLDlossCarInfo.setPlatformCarKindCode(prpLCheckCarInfoVo.getPlatformCarKindCode());
						}
						mainVo.setLossCarInfoVo(prpLDlossCarInfo);
						prpLDlossCarMain.setLossCarInfoVo(prpLDlossCarInfo);
					    lossCarService.updateAndSaveAndDelSons(prpLDlossCarMain);
				}else{//?????????????????????
					PrpLDlossCarMain prpLDlossCarMain=new PrpLDlossCarMain();
					prpLDlossCarMain.setRegistNo(registNo);//?????????
					prpLDlossCarMain.setRiskCode(prpLRegistVo.getRiskCode());//??????
					prpLDlossCarMain.setLicenseNo(carObjectDTO.getCarMark());//?????????
					prpLDlossCarMain.setComCode(comcode);
					prpLDlossCarMain.setMakeCom(comcode);
					prpLDlossCarMain.setPayTimes(jsonObject.get("caseTimes").toString());
					if(prpLDlossCarMainVo==null){
						prpLDlossCarMain.setLflag("L");//??????
						prpLDlossCarMain.setMercyFlag("1");//??????????????????
						if("01".equals(carObjectDTO.getLossType())){
							prpLDlossCarMain.setDeflossCarType("1");//?????????
							prpLDlossCarMain.setSerialNo(1);
						}else{
							prpLDlossCarMain.setDeflossCarType("3");//?????????
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

						prpLDlossCarMain.setCetainLossType("01");//????????????
						prpLDlossCarMain.setRepairFactoryCode(carLossVO.getGarageCode());//???????????????
						prpLDlossCarMain.setRepairFactoryName(carLossVO.getGarageName());//???????????????
						prpLDlossCarMain.setRepairFactoryType("003");//???????????????
						prpLDlossCarMain.setLossLevel("99");//??????
						prpLDlossCarMain.setHandlerCode("AUTO");
						prpLDlossCarMain.setHandlerName("AUTO");
						prpLDlossCarMain.setHandlerIdNo(carObjectDTO.getLicenseNo());//??????????????????
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
						prpLDlossCarMain.setPaId(pingAnDataNoticeVo.getId());//????????????????????????id
						prpLDlossCarMain.setValidFlag("1");
						prpLDlossCarMain.setLossPart(lossParts);//????????????
						prpLDlossCarMain.setSumCompFee(StringUtils.isNotBlank(carLossVO.getMaterialAmount())?new BigDecimal(carLossVO.getMaterialAmount()):new BigDecimal(0));//??????????????????
						prpLDlossCarMain.setSumRepairFee(StringUtils.isNotBlank(carLossVO.getManpowerAmount())?new BigDecimal(carLossVO.getManpowerAmount()):new BigDecimal(0));//??????????????????
						prpLDlossCarMain.setSumVeriRepairFee(StringUtils.isNotBlank(carLossVO.getManpowerAgreedAmount())?new BigDecimal(carLossVO.getManpowerAgreedAmount()):new BigDecimal(0));//??????????????????
						prpLDlossCarMain.setSumMatFee(new BigDecimal(0));//?????????
						prpLDlossCarMain.setSumVeriCompFee(StringUtils.isNotBlank(carLossVO.getMaterialAgreedAmount())?new BigDecimal(carLossVO.getMaterialAgreedAmount()):new BigDecimal(0));//??????????????????
						prpLDlossCarMain.setSumVeriMatFee(new BigDecimal(0));//???????????????
						//prpLDlossCarMain.setSumOutFee(sumOutFee);
						prpLDlossCarMain.setSumRemnant(StringUtils.isNotBlank(carLossVO.getSurveyReduceAmount())?new BigDecimal(carLossVO.getSurveyReduceAmount()):new BigDecimal(0));//????????????
						prpLDlossCarMain.setSumVeriRemnant(StringUtils.isNotBlank(carLossVO.getReduceAmount())?new BigDecimal(carLossVO.getReduceAmount()):new BigDecimal(0));//????????????
						prpLDlossCarMain.setSumRescueFee(StringUtils.isNotBlank(carLossVO.getRescueFee())?new BigDecimal(carLossVO.getRescueFee()):new BigDecimal(0));//???????????????
						prpLDlossCarMain.setSumVeriRescueFee(StringUtils.isNotBlank(carLossVO.getRescueFee())?new BigDecimal(carLossVO.getRescueFee()):new BigDecimal(0));//???????????????
						prpLDlossCarMain.setSumManageFee(StringUtils.isNotBlank(carLossVO.getManageFee())?new BigDecimal(carLossVO.getManageFee()):new BigDecimal(0));//???????????????
						prpLDlossCarMain.setSumVeriManage(StringUtils.isNotBlank(carLossVO.getManageAgreedFee())?new BigDecimal(carLossVO.getManageAgreedFee()):new BigDecimal(0));//???????????????
						prpLDlossCarMain.setSumLossFee(StringUtils.isNotBlank(carLossVO.getTotalAmount())?new BigDecimal(carLossVO.getTotalAmount()):new BigDecimal(0));//???????????????
						prpLDlossCarMain.setSumVeriLossFee(StringUtils.isNotBlank(carLossVO.getTotalAgreedAmount())?new BigDecimal(carLossVO.getTotalAgreedAmount()):new BigDecimal(0));//???????????????
						prpLDlossCarMain.setCreateUser("AUTO");
						prpLDlossCarMain.setCreateTime(new Date());
						prpLDlossCarMain.setUnderWiteIdNo(carObjectDTO.getLicenseNo());
						prpLDlossCarMain.setUnderWriteCode("AUTO");
						prpLDlossCarMain.setUnderWriteEndDate(new Date());
						prpLDlossCarMain.setUnderWriteFlag("1");//????????????
						int i=1;//??????????????????
						int j=0;//??????????????????
						//??????
						List<PrpLDlossCarComp> prpLDlossCarComps = new ArrayList<PrpLDlossCarComp>(0);
						//????????????
						List<PrpLDlossCarRepair> prpLDlossCarRepairs = new ArrayList<PrpLDlossCarRepair>(0);
						if(respCarDlossDataVo.getCarLossDetailVOList()!=null && respCarDlossDataVo.getCarLossDetailVOList().size()>0){
							for(CarLossDetailVO carLossDetailVO:respCarDlossDataVo.getCarLossDetailVOList()){
								if("1".equals(carLossDetailVO.getLossType())){
									PrpLDlossCarComp prpLDlossCarComp=new PrpLDlossCarComp();
									prpLDlossCarComp.setPrpLDlossCarMain(prpLDlossCarMain);//?????????
									prpLDlossCarComp.setRegistNo(registNo);//?????????
									prpLDlossCarComp.setSerialNo(i);
									prpLDlossCarComp.setLicenseNo(carObjectDTO.getCarMark());//?????????
									prpLDlossCarComp.setCompCode(carLossDetailVO.getFitsCode());
									prpLDlossCarComp.setCompName(carLossDetailVO.getLossName());//????????????
									if(StringUtils.isNotBlank(carLossDetailVO.getLossCount())){
										prpLDlossCarComp.setQuantity(Integer.valueOf(carLossDetailVO.getLossCount()));//????????????
									}else{
										prpLDlossCarComp.setQuantity(0);
									}
									prpLDlossCarComp.setMaterialFee(StringUtils.isNotBlank(carLossDetailVO.getLossAmount())?new BigDecimal(carLossDetailVO.getLossAmount()):new BigDecimal(0));//????????????
									prpLDlossCarComp.setSumDefLoss(prpLDlossCarComp.getMaterialFee().multiply(new BigDecimal(prpLDlossCarComp.getQuantity())));//????????????
									if(StringUtils.isNotBlank(carLossDetailVO.getLossCount())){
										prpLDlossCarComp.setVeriQuantity(Integer.valueOf(carLossDetailVO.getLossCount()));//??????????????????
									}else{
										prpLDlossCarComp.setVeriQuantity(0);
									}
									prpLDlossCarComp.setVeriMaterFee(StringUtils.isNotBlank(carLossDetailVO.getLossAgreeAmount())?new BigDecimal(carLossDetailVO.getLossAgreeAmount()):new BigDecimal(0));//????????????
									prpLDlossCarComp.setSumVeriLoss(prpLDlossCarComp.getVeriMaterFee().multiply(new BigDecimal(prpLDlossCarComp.getVeriQuantity())));//????????????
									prpLDlossCarComp.setOriginalId(carLossDetailVO.getOriginalCode());//????????????
									prpLDlossCarComp.setManageFeeRate(StringUtils.isNotBlank(carLossDetailVO.getFitsFeeRate())?new BigDecimal(carLossDetailVO.getFitsFeeRate()):null);//???????????????
									prpLDlossCarComp.setManageSingleFee(StringUtils.isNotBlank(carLossDetailVO.getFitsFee())?new BigDecimal(carLossDetailVO.getFitsFee()):new BigDecimal(0));//?????????
									prpLDlossCarComps.add(prpLDlossCarComp);
									i++;
								}else if("2".equals(carLossDetailVO.getLossType())){
									PrpLDlossCarRepair prpLDlossCarRepair=new PrpLDlossCarRepair();
									prpLDlossCarRepair.setRegistNo(registNo);
									prpLDlossCarRepair.setPrpLDlossCarMain(prpLDlossCarMain);
									prpLDlossCarRepair.setSerialNo(j);
									prpLDlossCarRepair.setRepairFlag("0");//??????
									prpLDlossCarRepair.setLicenseNo(carObjectDTO.getCarMark());//?????????
									prpLDlossCarRepair.setCompCode(carLossDetailVO.getFitsCode());
									prpLDlossCarRepair.setCompName(carLossDetailVO.getLossName());
									prpLDlossCarRepair.setManHour(StringUtils.isNotBlank(carLossDetailVO.getLossCount())?new BigDecimal(carLossDetailVO.getLossCount()):new BigDecimal(0));//??????
									prpLDlossCarRepair.setManHourUnitPrice(StringUtils.isNotBlank(carLossDetailVO.getLossAmount())?new BigDecimal(carLossDetailVO.getLossAmount()):new BigDecimal(0));//??????
									prpLDlossCarRepair.setManHourFee(prpLDlossCarRepair.getManHour().multiply(prpLDlossCarRepair.getManHourUnitPrice()));//??????????????????
									prpLDlossCarRepair.setSumDefLoss(prpLDlossCarRepair.getManHour().multiply(prpLDlossCarRepair.getManHourUnitPrice()));//????????????
									prpLDlossCarRepair.setVeriManHour(StringUtils.isNotBlank(carLossDetailVO.getLossCount())?new BigDecimal(carLossDetailVO.getLossCount()):new BigDecimal(0));//????????????
									prpLDlossCarRepair.setVeriManUnitPrice(StringUtils.isNotBlank(carLossDetailVO.getLossAgreeAmount())?new BigDecimal(carLossDetailVO.getLossAgreeAmount()):new BigDecimal(0));//????????????
									prpLDlossCarRepair.setVeriManHourFee(prpLDlossCarRepair.getVeriManHour().multiply(prpLDlossCarRepair.getVeriManUnitPrice()));//??????????????????
									prpLDlossCarRepair.setSumVeriLoss(prpLDlossCarRepair.getVeriManHour().multiply(prpLDlossCarRepair.getVeriManUnitPrice()));//????????????
									if("2".equals(carLossDetailVO.getFitType())){
										prpLDlossCarRepair.setSelfConfigFlag("1");//?????????????????????
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
									prpLDlossCarRepair.setRepairFlag("1");//??????
									prpLDlossCarRepair.setLicenseNo(carObjectDTO.getCarMark());//?????????
									prpLDlossCarRepair.setCompCode(carLossDetailVO.getFitsCode());
									prpLDlossCarRepair.setCompName(carLossDetailVO.getLossName());
									prpLDlossCarRepair.setManHour(StringUtils.isNotBlank(carLossDetailVO.getLossCount())?new BigDecimal(carLossDetailVO.getLossCount()):new BigDecimal(0));//??????
									prpLDlossCarRepair.setManHourUnitPrice(StringUtils.isNotBlank(carLossDetailVO.getLossAmount())?new BigDecimal(carLossDetailVO.getLossAmount()):new BigDecimal(0));//??????
									prpLDlossCarRepair.setManHourFee(prpLDlossCarRepair.getManHour().multiply(prpLDlossCarRepair.getManHourUnitPrice()));//??????????????????
									prpLDlossCarRepair.setSumDefLoss(prpLDlossCarRepair.getManHour().multiply(prpLDlossCarRepair.getManHourUnitPrice()));//????????????
									prpLDlossCarRepair.setVeriManHour(StringUtils.isNotBlank(carLossDetailVO.getLossCount())?new BigDecimal(carLossDetailVO.getLossCount()):new BigDecimal(0));//????????????
									prpLDlossCarRepair.setVeriManUnitPrice(StringUtils.isNotBlank(carLossDetailVO.getLossAgreeAmount())?new BigDecimal(carLossDetailVO.getLossAgreeAmount()):new BigDecimal(0));//????????????
									prpLDlossCarRepair.setVeriManHourFee(prpLDlossCarRepair.getVeriManHour().multiply(prpLDlossCarRepair.getVeriManUnitPrice()));//??????????????????
									prpLDlossCarRepair.setSumVeriLoss(prpLDlossCarRepair.getVeriManHour().multiply(prpLDlossCarRepair.getVeriManUnitPrice()));//????????????
									if("2".equals(carLossDetailVO.getFitType())){
										prpLDlossCarRepair.setSelfConfigFlag("1");//?????????????????????
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
						prpLDlossCarInfo.setRegistNo(registNo);//?????????
						prpLDlossCarInfo.setLossCarKindCode("01");//????????????
						prpLDlossCarInfo.setLicenseNo(carObjectDTO.getCarMark());//?????????
						prpLDlossCarInfo.setEngineNo(carObjectDTO.getEngineNo());//????????????
						prpLDlossCarInfo.setVinNo(carObjectDTO.getVin());//vin???
						prpLDlossCarInfo.setFrameNo(carObjectDTO.getVin());
						prpLDlossCarInfo.setCreateTime(new Date());
						prpLDlossCarInfo.setCreateUser("AUTO");
						prpLDlossCarInfo.setOperatorCode("AUTO");
						prpLDlossCarInfo.setValidFlag("1");
						PiccCodeDictVo  piccCodeDictVo3=pingAnDictService.getDictData(PingAnCodeTypeEnum.HPZL.getCodeType(),carObjectDTO.getLicensePlateType());//?????????????????????
						prpLDlossCarInfo.setLicenseType(piccCodeDictVo3.getDhCodeCode());//????????????
						if("1".equals(prpLDlossCarMain.getDeflossCarType())){
							prpLDlossCarInfo.setCarOwner(prpLCheckCarInfoVo.getCarOwner());//??????
							prpLDlossCarInfo.setDriveName(carObjectDTO.getDriverName());//?????????
							prpLDlossCarInfo.setDrivingLicenseNo(carObjectDTO.getLicenseNo());//?????????????????????
							PiccCodeDictVo  piccCodeDictVo2=pingAnDictService.getDictData(PingAnCodeTypeEnum.JSZLX.getCodeType(),carObjectDTO.getLicenseType());//?????????????????????
							prpLDlossCarInfo.setIdentifyType(piccCodeDictVo2.getDhCodeCode());//???????????????
							prpLDlossCarInfo.setCarKindCode(prpLCheckCarInfoVo.getPlatformCarKindCode());
						}else{
							prpLDlossCarInfo.setCarOwner(thirdInfoVo.getOwnerName());//??????
							prpLDlossCarInfo.setDriveName(thirdInfoVo.getDriverName());//?????????
							prpLDlossCarInfo.setDrivingLicenseNo(thirdInfoVo.getDriverLicenseNo());//?????????????????????
							PiccCodeDictVo  piccCodeDictVo2=pingAnDictService.getDictData(PingAnCodeTypeEnum.JSZLX.getCodeType(),thirdInfoVo.getDriverLicenseType());//?????????????????????
							prpLDlossCarInfo.setIdentifyType(piccCodeDictVo2.getDhCodeCode());//???????????????
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
				
			if("0".equals(payFlag)){//????????????????????????????????????????????????????????????
				List<PrpLWfTaskVo> prpLWfTaskinVos=wfFlowQueryService.findPrpWfTaskVoForIn(registNo,FlowNode.DLoss.name(),FlowNode.DLCar.name());
				if("0".equals(flag) && dlossCarMainVo!=null && "1".equals(carFlag)){//??????????????????????????????
					PrpLWfTaskVo prpLWfTaskVo=new PrpLWfTaskVo();
					prpLWfTaskVo.setNodeCode(FlowNode.DLoss.name());
					prpLWfTaskVo.setTaskName(FlowNode.DLoss.getName());
					prpLWfTaskVo.setRegistNo(registNo);
					prpLWfTaskVo.setFlowId(wfTaskVo.getFlowId());
					prpLWfTaskVo.setSubNodeCode(FlowNode.DLCar.name());
					prpLWfTaskVo.setUpperTaskId(wfTaskVo.getTaskId());
					prpLWfTaskVo.setComCode(comcode);
					prpLWfTaskVo.setHandlerStatus(HandlerStatus.INIT);// ?????????
					prpLWfTaskVo.setWorkStatus(WorkStatus.INIT);// ?????????
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
						prpLWfTaskVo.setItemName("?????????"+licenceNo);
					}else{
						prpLWfTaskVo.setItemName("?????????"+licenceNo);
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
				}else if(dlossCarMainVo==null && (prpLWfTaskinVos==null || prpLWfTaskinVos.size()==0)){//????????????????????????
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
						prpLWfTaskVo.setHandlerStatus(HandlerStatus.INIT);// ?????????
						prpLWfTaskVo.setWorkStatus(WorkStatus.INIT);// ?????????
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
							prpLWfTaskVo.setItemName("?????????"+licenceNo);
						}else{
							prpLWfTaskVo.setItemName("?????????"+licenceNo);
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
				}else{//??????????????????
					List<PrpLWfTaskVo> taskinVos=wfFlowQueryService.findPrpWfTaskVoForIn(registNo,FlowNode.VLoss.name(),FlowNode.VLCar_LV0.name());
					if(prpLWfTaskinVos!=null && prpLWfTaskinVos.size()>0){//???????????????????????????????????????????????????????????????????????????????????????????????????????????????
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
					}else{//????????????????????????????????????????????????
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
			logger.error("??????????????????????????????",e);
			resultBean.fail("??????????????????????????????"+e.getMessage());
		}
		return resultBean;
	}
}
