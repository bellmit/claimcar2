package ins.sino.claimcar.pinganUnion.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.utils.Beans;
import ins.platform.common.service.facade.PingAnDictService;
import ins.platform.vo.PiccCodeDictVo;
import ins.sino.claimcar.CodeConstants.AuditStatus;
import ins.sino.claimcar.CodeConstants.PersVeriFlag;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.WfFlowQueryService;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.flow.vo.WfTaskSubmitVo;
import ins.sino.claimcar.lossperson.po.PrpLDlossPersExt;
import ins.sino.claimcar.lossperson.po.PrpLDlossPersHospital;
import ins.sino.claimcar.lossperson.po.PrpLDlossPersInjured;
import ins.sino.claimcar.lossperson.po.PrpLDlossPersRaise;
import ins.sino.claimcar.lossperson.po.PrpLDlossPersTrace;
import ins.sino.claimcar.lossperson.po.PrpLDlossPersTraceFee;
import ins.sino.claimcar.lossperson.po.PrpLDlossPersTraceMain;
import ins.sino.claimcar.lossperson.service.PersTraceService;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersExtVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersHospitalVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersInjuredVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersRaiseVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceFeeVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceMainVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceVo;
import ins.sino.claimcar.pinganUnion.enums.PingAnCodeTypeEnum;
import ins.sino.claimcar.pinganUnion.vo.PingAnDataNoticeVo;
import ins.sino.claimcar.pinganUnion.vo.ResultBean;
import ins.sino.claimcar.pinganVo.ClmPersonFosterVo;
import ins.sino.claimcar.pinganVo.ClmPersonHospitalVo;
import ins.sino.claimcar.pinganVo.ClmPersonLossDiagnoseVo;
import ins.sino.claimcar.pinganVo.ClmPersonLossImpairmentVo;
import ins.sino.claimcar.pinganVo.ClmPersonLossVo;
import ins.sino.claimcar.pinganVo.ClmPersonObjectVo;
import ins.sino.claimcar.pinganVo.OtherBaseInfoVo;
import ins.sino.claimcar.pinganVo.RespPerSonDlossDataVo;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistService;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

import javax.ws.rs.Path;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;

@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"},group="pingAnPersonDlossService")
@Path("pingAnPersonDlossService")
public class PingAnPersonDlossServiceImpl implements PingAnHandleService{
	private static Logger logger = LoggerFactory.getLogger(PingAnPersonDlossServiceImpl.class);
	@Autowired
	PingAnDictService pingAnDictService;
	@Autowired
	DatabaseDao databaseDao;
	@Autowired
	PersTraceService persTraceService;
	@Autowired
	RegistService registService;
	@Autowired
	WfTaskHandleService wfTaskHandleService;
	@Autowired
	WfFlowQueryService wfFlowQueryService;
	@Autowired
	PolicyViewService policyViewService;
	@Override
	public ResultBean pingAnHandle(String registNo, PingAnDataNoticeVo pingAnDataNoticeVo, String respData) {
		logger.info("??????????????????????????????--------------------------------???{}",respData);
		ResultBean resultBean=ResultBean.success();
		Gson gson = new Gson();
		String comcode="";
		Long bussTaskId=null;
		Long traceId=null;//?????????id
		JSONObject jsonObject=JSON.parseObject(pingAnDataNoticeVo.getParamObj());
		String payTimes=jsonObject.get("caseTimes").toString();//????????????
		String addflag="0";//????????????????????????1--??????0--???
		try{
			logger.info("???????????????????????????----------------------------??????{}",respData);
			RespPerSonDlossDataVo respPerSonDlossDataVo=JSON.parseObject(respData, RespPerSonDlossDataVo.class);
			if(respPerSonDlossDataVo!=null){
				PrpLRegistVo prpLRegistVo=registService.findRegistByRegistNo(registNo);
				comcode=prpLRegistVo.getComCode();
				
				List<PrpLDlossPersTraceMainVo> persTraceMainVos= persTraceService.findPersTraceMainVo(registNo);
				List<PrpLDlossPersTraceVo> prpLDlossPersTraceVos=persTraceService.findPersTraceVoByRegistNo(registNo);
				ClmPersonObjectVo clmPersonObjectVo=respPerSonDlossDataVo.getClmPersonObject();
				//??????????????????????????????????????????????????????????????????????????????????????????
				if(prpLDlossPersTraceVos!=null && prpLDlossPersTraceVos.size()>0){
					for(PrpLDlossPersTraceVo traceVo:prpLDlossPersTraceVos){
						PrpLDlossPersInjuredVo persInjuredVo = traceVo.getPrpLDlossPersInjured();
						if(persInjuredVo != null && clmPersonObjectVo != null && payTimes.equals(traceVo.getPayTimes()) && clmPersonObjectVo.getIdClmChannelProcess().equals(persInjuredVo.getIdClmChannelProcess())){
							addflag="1";
							traceId=traceVo.getId();
							break;
						}
					}
				}
				//?????????????????????????????????
				if(persTraceMainVos==null || persTraceMainVos.size()==0){//????????????
					if(clmPersonObjectVo==null){
						clmPersonObjectVo=new ClmPersonObjectVo();
					}
					ClmPersonLossVo clmPersonLossVo=respPerSonDlossDataVo.getClmPersonLoss();
					if(clmPersonLossVo==null){
						clmPersonLossVo=new ClmPersonLossVo();
					}
					OtherBaseInfoVo otherBaseInfoVo=respPerSonDlossDataVo.getOtherBaseInfo();
					if(otherBaseInfoVo==null){
						otherBaseInfoVo=new OtherBaseInfoVo();
					}
					PrpLDlossPersTraceMain prpLDlossPersTraceMain=new PrpLDlossPersTraceMain();
					prpLDlossPersTraceMain.setRegistNo(registNo);
					prpLDlossPersTraceMain.setRiskCode(prpLRegistVo.getRiskCode());
					prpLDlossPersTraceMain.setMercyFlag("1");//????????????
					prpLDlossPersTraceMain.setComCode(comcode);
					prpLDlossPersTraceMain.setMakeCom(comcode);
					prpLDlossPersTraceMain.setValidFlag("1");
					prpLDlossPersTraceMain.setCaseProcessType("01");//????????????
					prpLDlossPersTraceMain.setIntermediaryFlag("0");//??????????????????
					prpLDlossPersTraceMain.setMajorcaseFlag("0");//??????????????????
					prpLDlossPersTraceMain.setOperatorCode("AUTO");
					prpLDlossPersTraceMain.setOperatorName("AUTO");//?????????
					prpLDlossPersTraceMain.setUnderwriteFlag("1");
					prpLDlossPersTraceMain.setUnderwriteCode("AUTO");//?????????
					prpLDlossPersTraceMain.setUnderwriteEndDate(new Date());
					prpLDlossPersTraceMain.setUndwrtFeeCode("AUTO");//???????????????
					prpLDlossPersTraceMain.setUndwrtFeeName("AUTO");
					prpLDlossPersTraceMain.setUndwrtFeeEndDate(new Date());//
					prpLDlossPersTraceMain.setCreateTime(new Date());
					prpLDlossPersTraceMain.setCreateUser("AUTO");
					prpLDlossPersTraceMain.setAuditStatus("submitCharge");
					prpLDlossPersTraceMain.setUpdateTime(new Date());
					prpLDlossPersTraceMain.setReportType("3");//????????????
					prpLDlossPersTraceMain.setPlfName("AUTO");
					prpLDlossPersTraceMain.setTraceTimes(new BigDecimal(1));//???????????????????????????jsonObject.get("caseTimes").toString()jsonObject.get("caseTimes").toString()
					prpLDlossPersTraceMain.setPlCode("AUTO");
					databaseDao.save(PrpLDlossPersTraceMain.class, prpLDlossPersTraceMain);
					bussTaskId=prpLDlossPersTraceMain.getId();
					PrpLDlossPersTrace prpLDlossPersTrace=new PrpLDlossPersTrace();
					prpLDlossPersTrace.setPersTraceMainId(prpLDlossPersTraceMain.getId());
					prpLDlossPersTrace.setRegistNo(registNo);
					prpLDlossPersTrace.setPayTimes(payTimes);
					prpLDlossPersTrace.setPersonName(clmPersonObjectVo.getPersonName());//????????????
					prpLDlossPersTrace.setLossFeeType("1");//????????????
					prpLDlossPersTrace.setTraceForms("2");//????????????
					prpLDlossPersTrace.setEndFlag("1");//??????????????????
					prpLDlossPersTrace.setSumReportFee(StringUtils.isBlank(clmPersonLossVo.getLossSum())?new BigDecimal(0):new BigDecimal(clmPersonLossVo.getLossSum()));//???????????????
					prpLDlossPersTrace.setSumRealFee(StringUtils.isBlank(clmPersonLossVo.getCustomerClaimAmount())?new BigDecimal(0):new BigDecimal(clmPersonLossVo.getCustomerClaimAmount()));//??????????????????
					prpLDlossPersTrace.setSumdefLoss(StringUtils.isBlank(clmPersonLossVo.getLossSum())?new BigDecimal(0):new BigDecimal(clmPersonLossVo.getLossSum()));//??????????????????
					prpLDlossPersTrace.setSumDetractionFee(new BigDecimal(0));//????????????
					prpLDlossPersTrace.setSumVeriReportFee(StringUtils.isBlank(clmPersonLossVo.getLossSum())?new BigDecimal(0):new BigDecimal(clmPersonLossVo.getLossSum()));//??????
					prpLDlossPersTrace.setSumVeriRealFee(StringUtils.isBlank(clmPersonLossVo.getCustomerClaimAmount())?new BigDecimal(0):new BigDecimal(clmPersonLossVo.getCustomerClaimAmount()));
					prpLDlossPersTrace.setSumVeriDefloss(StringUtils.isBlank(clmPersonLossVo.getLossSum())?new BigDecimal(0):new BigDecimal(clmPersonLossVo.getLossSum()));
					prpLDlossPersTrace.setSumVeriDetractionFee(new BigDecimal(0));
					prpLDlossPersTrace.setOperatorCode("AUTO");
					prpLDlossPersTrace.setComCode(prpLRegistVo.getComCode());//????????????
					prpLDlossPersTrace.setValidFlag("1");//????????????
					prpLDlossPersTrace.setCreateUser("AUTO");
					prpLDlossPersTrace.setCreateTime(new Date());
					prpLDlossPersTrace.setUpdateUser("AUTO");
					prpLDlossPersTrace.setUpdateTime(new Date());
					//????????????????????????
					PrpLDlossPersInjured prpLDlossPersInjured=new PrpLDlossPersInjured();
					prpLDlossPersInjured.setIdClmChannelProcess(clmPersonObjectVo.getIdClmChannelProcess());//?????????
					prpLDlossPersInjured.setPrpLDlossPersTrace(prpLDlossPersTrace);
					prpLDlossPersInjured.setRegistNo(registNo);//?????????
					prpLDlossPersInjured.setTreatSituation(clmPersonObjectVo.getCureType());//????????????
					prpLDlossPersInjured.setPersonName(clmPersonObjectVo.getPersonName());//????????????
					PiccCodeDictVo  piccCodeDictVo2=pingAnDictService.getDictData(PingAnCodeTypeEnum.JSZLX.getCodeType(),clmPersonObjectVo.getPersonCertificateType());//
					prpLDlossPersInjured.setCertiType(piccCodeDictVo2.getDhCodeCode());//????????????
					prpLDlossPersInjured.setCertiCode(clmPersonObjectVo.getPersonCertificateNo());//????????????
					prpLDlossPersInjured.setLossItemType("1");//????????????
					prpLDlossPersInjured.setPhoneNumber(clmPersonObjectVo.getPersonPhone());//????????????
					prpLDlossPersInjured.setPersonAge(StringUtils.isBlank(clmPersonObjectVo.getPersonAge())?new BigDecimal(0):new BigDecimal(clmPersonObjectVo.getPersonAge()));//??????
					if("M".equals(clmPersonObjectVo.getPersonSex())){
						prpLDlossPersInjured.setPersonSex("1");//??????
					}else{
						prpLDlossPersInjured.setPersonSex("2");
					 }
					if(StringUtils.isNotBlank(otherBaseInfoVo.getPersonEarn())){
						if(Double.valueOf(otherBaseInfoVo.getPersonEarn())<=5000){
							prpLDlossPersInjured.setIncome("01");;//????????????
						}else if(5000<Double.valueOf(otherBaseInfoVo.getPersonEarn()) && Double.valueOf(otherBaseInfoVo.getPersonEarn())<=10000){
							prpLDlossPersInjured.setIncome("02");;//??????
						}else if(10000<Double.valueOf(otherBaseInfoVo.getPersonEarn()) && Double.valueOf(otherBaseInfoVo.getPersonEarn())<=20000){
							prpLDlossPersInjured.setIncome("03");;//??????
						}else{
							prpLDlossPersInjured.setIncome("04");;//??????
						}
					}
					if("1".equals(clmPersonObjectVo.getCureType())){
						prpLDlossPersInjured.setWoundCode("01");//????????????
					}else if("2".equals(clmPersonObjectVo.getCureType())){
						prpLDlossPersInjured.setWoundCode("02");//????????????
					}else if("3".equals(clmPersonObjectVo.getCureType())){
						prpLDlossPersInjured.setWoundCode("03");//??????
					}
					if("1".equals(clmPersonObjectVo.getPersonResidentStandard())){
						prpLDlossPersInjured.setDomicile("2");//????????????
					}else if("2".equals(clmPersonObjectVo.getPersonResidentStandard())){
						prpLDlossPersInjured.setDomicile("1");//????????????
					}else{
						prpLDlossPersInjured.setDomicile("3");//?????????????????????
					}
					prpLDlossPersInjured.setInjuryPart("??????");//????????????
					prpLDlossPersInjured.setValidFlag("1");//????????????
					prpLDlossPersInjured.setCreateUser("AUTO");
					prpLDlossPersInjured.setCreateTime(new Date());
					//????????????
					List<ClmPersonLossImpairmentVo>  clmPersonLossImpairmentVos=respPerSonDlossDataVo.getClmPersonLossImpairmentList();
					if(clmPersonLossImpairmentVos!=null && clmPersonLossImpairmentVos.size()>0){
						prpLDlossPersInjured.setChkComCode(clmPersonLossImpairmentVos.get(0).getAppraisalDepartment());//??????????????????
						prpLDlossPersInjured.setAppraisaCity(clmPersonLossImpairmentVos.get(0).getCityMark());//?????????
					}
					prpLDlossPersTrace.setPrpLDlossPersInjured(prpLDlossPersInjured);
					List<PrpLDlossPersTraceFee> prpLDlossPersTraceFees = new ArrayList<PrpLDlossPersTraceFee>();
					//??????????????????
					PrpLDlossPersTraceFee OtherTraceFee=new PrpLDlossPersTraceFee();
					//?????????
					if(StringUtils.isNotBlank(clmPersonLossVo.getMedicalFee()) && new BigDecimal(clmPersonLossVo.getMedicalFee()).doubleValue()>0){
						PrpLDlossPersTraceFee  prpLDlossPersTraceFee=makePersonFee(clmPersonLossVo.getMedicalFee(),"medical_fee",prpLDlossPersTrace,registNo);
						if("17".equals(prpLDlossPersTraceFee.getFeeTypeCode())){
							if(StringUtils.isNotBlank(OtherTraceFee.getFeeTypeCode())){
								commompart(OtherTraceFee,prpLDlossPersTraceFee);
							}else{
								OtherTraceFee=prpLDlossPersTraceFee;
							}
						}else{
							prpLDlossPersTraceFees.add(prpLDlossPersTraceFee);
						}
						
					}
					//?????????
					if(StringUtils.isNotBlank(clmPersonLossVo.getLawsuitFee()) && new BigDecimal(clmPersonLossVo.getLawsuitFee()).doubleValue()>0){
						PrpLDlossPersTraceFee  prpLDlossPersTraceFee=makePersonFee(clmPersonLossVo.getLawsuitFee(),"law_fee",prpLDlossPersTrace,registNo);
						if("17".equals(prpLDlossPersTraceFee.getFeeTypeCode())){
							if(StringUtils.isNotBlank(OtherTraceFee.getFeeTypeCode())){
								commompart(OtherTraceFee,prpLDlossPersTraceFee);
							}else{
								OtherTraceFee=prpLDlossPersTraceFee;
							}
						}else{
							prpLDlossPersTraceFees.add(prpLDlossPersTraceFee);
						}
					}
					//?????????
					if(StringUtils.isNotBlank(clmPersonLossVo.getDiningFee()) && new BigDecimal(clmPersonLossVo.getDiningFee()).doubleValue()>0){
						PrpLDlossPersTraceFee  prpLDlossPersTraceFee=makePersonFee(clmPersonLossVo.getDiningFee(),"dining_fee",prpLDlossPersTrace,registNo);
						if("17".equals(prpLDlossPersTraceFee.getFeeTypeCode())){
							if(StringUtils.isNotBlank(OtherTraceFee.getFeeTypeCode())){
								commompart(OtherTraceFee,prpLDlossPersTraceFee);
							}else{
								OtherTraceFee=prpLDlossPersTraceFee;
							}
						}else{
							prpLDlossPersTraceFees.add(prpLDlossPersTraceFee);
						}
					}
					//?????????
					if(StringUtils.isNotBlank(clmPersonLossVo.getSubsequentTreatment())&& new BigDecimal(clmPersonLossVo.getSubsequentTreatment()).doubleValue()>0){
						PrpLDlossPersTraceFee  prpLDlossPersTraceFee=makePersonFee(clmPersonLossVo.getSubsequentTreatment(),"subsequent_treatment",prpLDlossPersTrace,registNo);
						if("17".equals(prpLDlossPersTraceFee.getFeeTypeCode())){
							if(StringUtils.isNotBlank(OtherTraceFee.getFeeTypeCode())){
								commompart(OtherTraceFee,prpLDlossPersTraceFee);
							}else{
								OtherTraceFee=prpLDlossPersTraceFee;
							}
						}else{
							prpLDlossPersTraceFees.add(prpLDlossPersTraceFee);
						}
					}
					//?????????
					if(StringUtils.isNotBlank(clmPersonLossVo.getNutritionFee()) && new BigDecimal(clmPersonLossVo.getNutritionFee()).doubleValue()>0){
						PrpLDlossPersTraceFee  prpLDlossPersTraceFee=makePersonFee(clmPersonLossVo.getNutritionFee(),"nutrition_fee",prpLDlossPersTrace,registNo);
						if("17".equals(prpLDlossPersTraceFee.getFeeTypeCode())){
							if(StringUtils.isNotBlank(OtherTraceFee.getFeeTypeCode())){
								commompart(OtherTraceFee,prpLDlossPersTraceFee);
							}else{
								OtherTraceFee=prpLDlossPersTraceFee;
							}
						}else{
							prpLDlossPersTraceFees.add(prpLDlossPersTraceFee);
						}
					}
					//?????????
					if(StringUtils.isNotBlank(clmPersonLossVo.getWorkingCompensation()) && new BigDecimal(clmPersonLossVo.getWorkingCompensation()).doubleValue()>0){
						PrpLDlossPersTraceFee  prpLDlossPersTraceFee=makePersonFee(clmPersonLossVo.getWorkingCompensation(),"working_compensation",prpLDlossPersTrace,registNo);
						if("17".equals(prpLDlossPersTraceFee.getFeeTypeCode())){
							if(StringUtils.isNotBlank(OtherTraceFee.getFeeTypeCode())){
								commompart(OtherTraceFee,prpLDlossPersTraceFee);
							}else{
								OtherTraceFee=prpLDlossPersTraceFee;
							}
						}else{
							prpLDlossPersTraceFees.add(prpLDlossPersTraceFee);
						}
					}
					//?????????
					if(StringUtils.isNotBlank(clmPersonLossVo.getNursingFee()) && new BigDecimal(clmPersonLossVo.getNursingFee()).doubleValue()>0){
						PrpLDlossPersTraceFee  prpLDlossPersTraceFee=makePersonFee(clmPersonLossVo.getNursingFee(),"nursing_fee",prpLDlossPersTrace,registNo);
						if("17".equals(prpLDlossPersTraceFee.getFeeTypeCode())){
							if(StringUtils.isNotBlank(OtherTraceFee.getFeeTypeCode())){
								commompart(OtherTraceFee,prpLDlossPersTraceFee);
							}else{
								OtherTraceFee=prpLDlossPersTraceFee;
							}
						}else{
							prpLDlossPersTraceFees.add(prpLDlossPersTraceFee);
						}
					}
					//?????????
					if(StringUtils.isNotBlank(clmPersonLossVo.getTrafficFee()) && new BigDecimal(clmPersonLossVo.getTrafficFee()).doubleValue()>0){
						PrpLDlossPersTraceFee  prpLDlossPersTraceFee=makePersonFee(clmPersonLossVo.getTrafficFee(),"traffic_fee",prpLDlossPersTrace,registNo);
						if("17".equals(prpLDlossPersTraceFee.getFeeTypeCode())){
							if(StringUtils.isNotBlank(OtherTraceFee.getFeeTypeCode())){
								commompart(OtherTraceFee,prpLDlossPersTraceFee);
							}else{
								OtherTraceFee=prpLDlossPersTraceFee;
							}
						}else{
							prpLDlossPersTraceFees.add(prpLDlossPersTraceFee);
						}
					}
					//???????????????
					if(StringUtils.isNotBlank(clmPersonLossVo.getDeformityCompensateFee()) && new BigDecimal(clmPersonLossVo.getDeformityCompensateFee()).doubleValue()>0){
						PrpLDlossPersTraceFee  prpLDlossPersTraceFee=makePersonFee(clmPersonLossVo.getDeformityCompensateFee(),"deformity_compensate_fee",prpLDlossPersTrace,registNo);
						if("17".equals(prpLDlossPersTraceFee.getFeeTypeCode())){
							if(StringUtils.isNotBlank(OtherTraceFee.getFeeTypeCode())){
								commompart(OtherTraceFee,prpLDlossPersTraceFee);
							}else{
								OtherTraceFee=prpLDlossPersTraceFee;
							}
						}else{
							prpLDlossPersTraceFees.add(prpLDlossPersTraceFee);
						}
					}
					//???????????????
					if(StringUtils.isNotBlank(clmPersonLossVo.getDeathIndemnity())&& new BigDecimal(clmPersonLossVo.getDeathIndemnity()).doubleValue()>0){
						PrpLDlossPersTraceFee  prpLDlossPersTraceFee=makePersonFee(clmPersonLossVo.getDeathIndemnity(),"death_indemnity",prpLDlossPersTrace,registNo);
						if("17".equals(prpLDlossPersTraceFee.getFeeTypeCode())){
							if(StringUtils.isNotBlank(OtherTraceFee.getFeeTypeCode())){
								commompart(OtherTraceFee,prpLDlossPersTraceFee);
							}else{
								OtherTraceFee=prpLDlossPersTraceFee;
							}
						}else{
							prpLDlossPersTraceFees.add(prpLDlossPersTraceFee);
						}
					}
					//?????????
					if(StringUtils.isNotBlank(clmPersonLossVo.getFuneralFee())&& new BigDecimal(clmPersonLossVo.getFuneralFee()).doubleValue()>0){
						PrpLDlossPersTraceFee  prpLDlossPersTraceFee=makePersonFee(clmPersonLossVo.getFuneralFee(),"funeral_fee",prpLDlossPersTrace,registNo);
						if("17".equals(prpLDlossPersTraceFee.getFeeTypeCode())){
							if(StringUtils.isNotBlank(OtherTraceFee.getFeeTypeCode())){
								commompart(OtherTraceFee,prpLDlossPersTraceFee);
							}else{
								OtherTraceFee=prpLDlossPersTraceFee;
							}
						}else{
							prpLDlossPersTraceFees.add(prpLDlossPersTraceFee);
						}
					}
					//?????????
					if(StringUtils.isNotBlank(clmPersonLossVo.getFosterFee()) && new BigDecimal(clmPersonLossVo.getFosterFee()).doubleValue()>0){
						PrpLDlossPersTraceFee  prpLDlossPersTraceFee=makePersonFee(clmPersonLossVo.getFosterFee(),"foster_fee",prpLDlossPersTrace,registNo);
						if("17".equals(prpLDlossPersTraceFee.getFeeTypeCode())){
							if(StringUtils.isNotBlank(OtherTraceFee.getFeeTypeCode())){
								commompart(OtherTraceFee,prpLDlossPersTraceFee);
							}else{
								OtherTraceFee=prpLDlossPersTraceFee;
							}
						}else{
							prpLDlossPersTraceFees.add(prpLDlossPersTraceFee);
						}
					}
					//?????????
					if(StringUtils.isNotBlank(clmPersonLossVo.getAccommodationFee())&& new BigDecimal(clmPersonLossVo.getAccommodationFee()).doubleValue()>0){
						PrpLDlossPersTraceFee  prpLDlossPersTraceFee=makePersonFee(clmPersonLossVo.getAccommodationFee(),"accommodation_fee",prpLDlossPersTrace,registNo);
						if("17".equals(prpLDlossPersTraceFee.getFeeTypeCode())){
							if(StringUtils.isNotBlank(OtherTraceFee.getFeeTypeCode())){
								commompart(OtherTraceFee,prpLDlossPersTraceFee);
							}else{
								OtherTraceFee=prpLDlossPersTraceFee;
							}
						}else{
							prpLDlossPersTraceFees.add(prpLDlossPersTraceFee);
						}
					}
					//???????????????
					if(StringUtils.isNotBlank(clmPersonLossVo.getEmotionalCompensationFee()) && new BigDecimal(clmPersonLossVo.getEmotionalCompensationFee()).doubleValue()>0){
						PrpLDlossPersTraceFee  prpLDlossPersTraceFee=makePersonFee(clmPersonLossVo.getEmotionalCompensationFee(),"emotional_compensation_fee",prpLDlossPersTrace,registNo);
						if("17".equals(prpLDlossPersTraceFee.getFeeTypeCode())){
							if(StringUtils.isNotBlank(OtherTraceFee.getFeeTypeCode())){
								commompart(OtherTraceFee,prpLDlossPersTraceFee);
							}else{
								OtherTraceFee=prpLDlossPersTraceFee;
							}
						}else{
							prpLDlossPersTraceFees.add(prpLDlossPersTraceFee);
						}
					}
					//???????????????
					if(StringUtils.isNotBlank(clmPersonLossVo.getDeformityFee()) && new BigDecimal(clmPersonLossVo.getDeformityFee()).doubleValue()>0){
						PrpLDlossPersTraceFee  prpLDlossPersTraceFee=makePersonFee(clmPersonLossVo.getDeformityFee(),"deformity_fee",prpLDlossPersTrace,registNo);
						if("17".equals(prpLDlossPersTraceFee.getFeeTypeCode())){
							if(StringUtils.isNotBlank(OtherTraceFee.getFeeTypeCode())){
								commompart(OtherTraceFee,prpLDlossPersTraceFee);
							}else{
								OtherTraceFee=prpLDlossPersTraceFee;
							}
						}else{
							prpLDlossPersTraceFees.add(prpLDlossPersTraceFee);
						}
					}
					//?????????(??????)
					if(StringUtils.isNotBlank(clmPersonLossVo.getCheckupFee()) && new BigDecimal(clmPersonLossVo.getCheckupFee()).doubleValue()>0){
						PrpLDlossPersTraceFee  prpLDlossPersTraceFee=makePersonFee(clmPersonLossVo.getCheckupFee(),"checkup_fee",prpLDlossPersTrace,registNo);
						if("17".equals(prpLDlossPersTraceFee.getFeeTypeCode())){
							if(StringUtils.isNotBlank(OtherTraceFee.getFeeTypeCode())){
								commompart(OtherTraceFee,prpLDlossPersTraceFee);
							}else{
								OtherTraceFee=prpLDlossPersTraceFee;
							}
						}else{
							prpLDlossPersTraceFees.add(prpLDlossPersTraceFee);
						}
					}
					//?????????(??????)
					if(StringUtils.isNotBlank(clmPersonLossVo.getLawFee()) && new BigDecimal(clmPersonLossVo.getLawFee()).doubleValue()>0){
						PrpLDlossPersTraceFee  prpLDlossPersTraceFee=makePersonFee(clmPersonLossVo.getLawFee(),"law_fee",prpLDlossPersTrace,registNo);
						if("17".equals(prpLDlossPersTraceFee.getFeeTypeCode())){
							if(StringUtils.isNotBlank(OtherTraceFee.getFeeTypeCode())){
								commompart(OtherTraceFee,prpLDlossPersTraceFee);
							}else{
								OtherTraceFee=prpLDlossPersTraceFee;
							}
						}else{
							prpLDlossPersTraceFees.add(prpLDlossPersTraceFee);
						}
					}
					//????????????
					if(StringUtils.isNotBlank(clmPersonLossVo.getBaggageFee()) && new BigDecimal(clmPersonLossVo.getBaggageFee()).doubleValue()>0){
						PrpLDlossPersTraceFee  prpLDlossPersTraceFee=makePersonFee(clmPersonLossVo.getBaggageFee(),"baggage_fee",prpLDlossPersTrace,registNo);
						if("17".equals(prpLDlossPersTraceFee.getFeeTypeCode())){
							if(StringUtils.isNotBlank(OtherTraceFee.getFeeTypeCode())){
								commompart(OtherTraceFee,prpLDlossPersTraceFee);
							}else{
								OtherTraceFee=prpLDlossPersTraceFee;
							}
						}else{
							prpLDlossPersTraceFees.add(prpLDlossPersTraceFee);
						}
					}
					//????????????
					if(StringUtils.isNotBlank(clmPersonLossVo.getOtherFee()) && new BigDecimal(clmPersonLossVo.getOtherFee()).doubleValue()>0){
						PrpLDlossPersTraceFee  prpLDlossPersTraceFee=makePersonFee(clmPersonLossVo.getOtherFee(),"other_fee",prpLDlossPersTrace,registNo);
						if("17".equals(prpLDlossPersTraceFee.getFeeTypeCode())){
							if(StringUtils.isNotBlank(OtherTraceFee.getFeeTypeCode())){
								commompart(OtherTraceFee,prpLDlossPersTraceFee);
							}else{
								OtherTraceFee=prpLDlossPersTraceFee;
							}
						}else{
							prpLDlossPersTraceFees.add(prpLDlossPersTraceFee);
						}
					}
					if(StringUtils.isNotBlank(OtherTraceFee.getFeeTypeCode())){
						prpLDlossPersTraceFees.add(OtherTraceFee);
					}
					//????????????
					prpLDlossPersTrace.setPrpLDlossPersTraceFees(prpLDlossPersTraceFees);
					//??????????????????
					List<PrpLDlossPersExt> prpLDlossPersExts = new ArrayList<PrpLDlossPersExt>(0);
					List<PrpLDlossPersHospital> prpLDlossPersHospitals = new ArrayList<PrpLDlossPersHospital>(0);
					List<PrpLDlossPersRaise> prpLDlossPersRaises = new ArrayList<PrpLDlossPersRaise>(0);
					//????????????
					List<ClmPersonLossDiagnoseVo> clmPersonLossDiagnoseVos=respPerSonDlossDataVo.getClmPersonLossDiagnoseList();
					if(clmPersonLossDiagnoseVos!=null && clmPersonLossDiagnoseVos.size()>0){
						for(ClmPersonLossDiagnoseVo vo:clmPersonLossDiagnoseVos){
							PrpLDlossPersExt prpLDlossPersExt=new PrpLDlossPersExt();
							prpLDlossPersExt.setPrpLDlossPersInjured(prpLDlossPersInjured);
							prpLDlossPersExt.setRegistNo(registNo);
							prpLDlossPersExt.setInjuredPart("99");//????????????
							prpLDlossPersExt.setInjuredDiag("09");//????????????
							prpLDlossPersExt.setDiagDetail(vo.getPartOneName()+"_"+vo.getPartTwoName()+"_"+vo.getSurgicalName());//????????????
							prpLDlossPersExt.setValidFlag("1");//??????
							prpLDlossPersExt.setCreateUser("AUTO");
							prpLDlossPersExt.setCreateTime(new Date());
							prpLDlossPersExts.add(prpLDlossPersExt);
						}
						prpLDlossPersInjured.setPrpLDlossPersExts(prpLDlossPersExts);
					}
					//??????????????????
					List<ClmPersonFosterVo> clmPersonFosterVos=respPerSonDlossDataVo.getClmPersonFosterList();
					if(clmPersonFosterVos!=null && clmPersonFosterVos.size()>0){
						for(ClmPersonFosterVo vo: clmPersonFosterVos){
							PrpLDlossPersRaise prpLDlossPersRaise=new PrpLDlossPersRaise();
							prpLDlossPersRaise.setRegistNo(registNo);
							prpLDlossPersRaise.setPrpLDlossPersInjured(prpLDlossPersInjured);
							prpLDlossPersRaise.setPayPersonName(vo.getFosterName());//????????????
							if("F".equals(vo.getFosterSex())){
								prpLDlossPersRaise.setSex("1");//?????????
							}else{
								prpLDlossPersRaise.setSex("2");//?????????
							}
							prpLDlossPersRaise.setAge(StringUtils.isNotBlank(vo.getFosterAge())?new BigDecimal(vo.getFosterAge()):null);
							if("1".equals(vo.getFosterResidentStandard())){
								prpLDlossPersRaise.setDomicile("2");//????????????
							}else if("2".equals(vo.getFosterResidentStandard())){
								prpLDlossPersRaise.setDomicile("1");//????????????
							}else{
								prpLDlossPersRaise.setDomicile("3");//?????????????????????
							}
							prpLDlossPersRaise.setRelationship(vo.getFosterRelation());
							prpLDlossPersRaise.setInputTime(new Date());//????????????
							prpLDlossPersRaise.setValidFlag("1");//??????
							prpLDlossPersRaises.add(prpLDlossPersRaise);
							
						}
						prpLDlossPersInjured.setPrpLDlossPersRaises(prpLDlossPersRaises);
					}
					//????????????
					List<ClmPersonHospitalVo> clmPersonHospitalVos=respPerSonDlossDataVo.getClmPersonHospitalList();
					if(clmPersonHospitalVos!=null && clmPersonHospitalVos.size()>0){
						for(ClmPersonHospitalVo vo:clmPersonHospitalVos){
							PrpLDlossPersHospital prpLDlossPersHospital=new PrpLDlossPersHospital();
							prpLDlossPersHospital.setPrpLDlossPersInjured(prpLDlossPersInjured);
							prpLDlossPersHospital.setRegistNo(registNo);//?????????
							if(StringUtils.isNotBlank(vo.getStartDate())){
								prpLDlossPersHospital.setInHospitalDate(StringtoDate(vo.getStartDate()));//??????????????????
							}
							if(StringUtils.isNotBlank(vo.getEndDate())){
								prpLDlossPersHospital.setOutHospitalDate(StringtoDate(vo.getEndDate()));//????????????
							}
							//prpLDlossPersHospital.setHospitalCode(vo.getHospitalCode());//????????????
							prpLDlossPersHospital.setHospitalName(vo.getHospitalName());//????????????
							prpLDlossPersHospital.setValidFlag("1");
							prpLDlossPersHospital.setCreateTime(new Date());
							prpLDlossPersHospital.setCreateUser("AUTO");
							prpLDlossPersHospitals.add(prpLDlossPersHospital);
						}
						prpLDlossPersInjured.setPrpLDlossPersHospitals(prpLDlossPersHospitals);
						
					}
					databaseDao.save(PrpLDlossPersTrace.class, prpLDlossPersTrace);
					
				}else if("1".equals(addflag)){//??????????????????????????????
					if(clmPersonObjectVo==null){
						clmPersonObjectVo=new ClmPersonObjectVo();
					}
					ClmPersonLossVo clmPersonLossVo=respPerSonDlossDataVo.getClmPersonLoss();
					if(clmPersonLossVo==null){
						clmPersonLossVo=new ClmPersonLossVo();
					}
					OtherBaseInfoVo otherBaseInfoVo=respPerSonDlossDataVo.getOtherBaseInfo();
					if(otherBaseInfoVo==null){
						otherBaseInfoVo=new OtherBaseInfoVo();
					}
					PrpLDlossPersTraceVo  prpLDlossPersTraceVo=new PrpLDlossPersTraceVo();
					bussTaskId=persTraceMainVos.get(0).getId();
					prpLDlossPersTraceVo.setId(traceId);
					prpLDlossPersTraceVo.setRegistNo(registNo);
					prpLDlossPersTraceVo.setPersonName(clmPersonObjectVo.getPersonName());//????????????
					prpLDlossPersTraceVo.setLossFeeType("1");//????????????
					prpLDlossPersTraceVo.setTraceForms("2");//????????????
					prpLDlossPersTraceVo.setEndFlag("8");//??????????????????
					prpLDlossPersTraceVo.setSumReportFee(StringUtils.isBlank(clmPersonLossVo.getLossSum())?new BigDecimal(0):new BigDecimal(clmPersonLossVo.getLossSum()));//???????????????
					prpLDlossPersTraceVo.setSumRealFee(StringUtils.isBlank(clmPersonLossVo.getCustomerClaimAmount())?new BigDecimal(0):new BigDecimal(clmPersonLossVo.getCustomerClaimAmount()));//??????????????????
					prpLDlossPersTraceVo.setSumdefLoss(StringUtils.isBlank(clmPersonLossVo.getLossSum())?new BigDecimal(0):new BigDecimal(clmPersonLossVo.getLossSum()));//??????????????????
					prpLDlossPersTraceVo.setSumDetractionFee(new BigDecimal(0));//????????????
					prpLDlossPersTraceVo.setSumVeriReportFee(StringUtils.isBlank(clmPersonLossVo.getLossSum())?new BigDecimal(0):new BigDecimal(clmPersonLossVo.getLossSum()));//??????
					prpLDlossPersTraceVo.setSumVeriRealFee(StringUtils.isBlank(clmPersonLossVo.getCustomerClaimAmount())?new BigDecimal(0):new BigDecimal(clmPersonLossVo.getCustomerClaimAmount()));
					prpLDlossPersTraceVo.setSumVeriDefloss(StringUtils.isBlank(clmPersonLossVo.getLossSum())?new BigDecimal(0):new BigDecimal(clmPersonLossVo.getLossSum()));
					prpLDlossPersTraceVo.setSumVeriDetractionFee(new BigDecimal(0));
					prpLDlossPersTraceVo.setOperatorCode("AUTO");
					prpLDlossPersTraceVo.setComCode(prpLRegistVo.getComCode());//????????????
					prpLDlossPersTraceVo.setValidFlag("1");//????????????
					prpLDlossPersTraceVo.setCreateUser("AUTO");
					prpLDlossPersTraceVo.setCreateTime(new Date());
					prpLDlossPersTraceVo.setUpdateUser("AUTO");
					prpLDlossPersTraceVo.setUpdateTime(new Date());
					//????????????????????????
					PrpLDlossPersInjuredVo prpLDlossPersInjuredVo=new PrpLDlossPersInjuredVo();
					prpLDlossPersInjuredVo.setIdClmChannelProcess(clmPersonObjectVo.getIdClmChannelProcess());//?????????
					prpLDlossPersInjuredVo.setId(traceId);
					prpLDlossPersInjuredVo.setRegistNo(registNo);//?????????
					prpLDlossPersInjuredVo.setTreatSituation(clmPersonObjectVo.getCureType());//????????????
					prpLDlossPersInjuredVo.setPersonName(clmPersonObjectVo.getPersonName());//????????????
					PiccCodeDictVo  piccCodeDictVo2=pingAnDictService.getDictData(PingAnCodeTypeEnum.JSZLX.getCodeType(),clmPersonObjectVo.getPersonCertificateType());//
					prpLDlossPersInjuredVo.setCertiType(piccCodeDictVo2.getDhCodeCode());//????????????
					prpLDlossPersInjuredVo.setCertiCode(clmPersonObjectVo.getPersonCertificateNo());//????????????
					prpLDlossPersInjuredVo.setLossItemType("1");//????????????
					prpLDlossPersInjuredVo.setPhoneNumber(clmPersonObjectVo.getPersonPhone());//????????????
					prpLDlossPersInjuredVo.setPersonAge(StringUtils.isBlank(clmPersonObjectVo.getPersonAge())?new BigDecimal(0):new BigDecimal(clmPersonObjectVo.getPersonAge()));//??????
					if("M".equals(clmPersonObjectVo.getPersonSex())){
						prpLDlossPersInjuredVo.setPersonSex("1");//??????
					}else{
						prpLDlossPersInjuredVo.setPersonSex("2");
					 }
					if(StringUtils.isNotBlank(otherBaseInfoVo.getPersonEarn())){
						if(Double.valueOf(otherBaseInfoVo.getPersonEarn())<=5000){
							prpLDlossPersInjuredVo.setIncome("01");;//????????????
						}else if(5000<Double.valueOf(otherBaseInfoVo.getPersonEarn()) && Double.valueOf(otherBaseInfoVo.getPersonEarn())<=10000){
							prpLDlossPersInjuredVo.setIncome("02");;//??????
						}else if(10000<Double.valueOf(otherBaseInfoVo.getPersonEarn()) && Double.valueOf(otherBaseInfoVo.getPersonEarn())<=20000){
							prpLDlossPersInjuredVo.setIncome("03");;//??????
						}else{
							prpLDlossPersInjuredVo.setIncome("04");;//??????
						}
					}
					if("1".equals(clmPersonObjectVo.getCureType())){
						prpLDlossPersInjuredVo.setWoundCode("01");//????????????
					}else if("2".equals(clmPersonObjectVo.getCureType())){
						prpLDlossPersInjuredVo.setWoundCode("02");//????????????
					}else if("3".equals(clmPersonObjectVo.getCureType())){
						prpLDlossPersInjuredVo.setWoundCode("03");//??????
					}
					if("1".equals(clmPersonObjectVo.getPersonResidentStandard())){
						prpLDlossPersInjuredVo.setDomicile("2");//????????????
					}else if("2".equals(clmPersonObjectVo.getPersonResidentStandard())){
						prpLDlossPersInjuredVo.setDomicile("1");//????????????
					}else{
						prpLDlossPersInjuredVo.setDomicile("3");//?????????????????????
					}
					prpLDlossPersInjuredVo.setInjuryPart("??????");//????????????
					prpLDlossPersInjuredVo.setValidFlag("1");//????????????
					prpLDlossPersInjuredVo.setCreateUser("AUTO");
					prpLDlossPersInjuredVo.setCreateTime(new Date());
					//????????????
					List<ClmPersonLossImpairmentVo>  clmPersonLossImpairmentVos=respPerSonDlossDataVo.getClmPersonLossImpairmentList();
					if(clmPersonLossImpairmentVos!=null && clmPersonLossImpairmentVos.size()>0){
						prpLDlossPersInjuredVo.setChkComCode(clmPersonLossImpairmentVos.get(0).getAppraisalDepartment());//??????????????????
						prpLDlossPersInjuredVo.setAppraisaCity(clmPersonLossImpairmentVos.get(0).getCityMark());//?????????
					}
					List<PrpLDlossPersTraceFeeVo> prpLDlossPersTraceFees = new ArrayList<PrpLDlossPersTraceFeeVo>();
					//??????????????????
					PrpLDlossPersTraceFeeVo OtherTraceFee=new PrpLDlossPersTraceFeeVo();
					//?????????
					if(StringUtils.isNotBlank(clmPersonLossVo.getMedicalFee()) && new BigDecimal(clmPersonLossVo.getMedicalFee()).doubleValue()>0){
						PrpLDlossPersTraceFeeVo  prpLDlossPersTraceFee=makePersonFeeVo(clmPersonLossVo.getMedicalFee(),"medical_fee",registNo);
						if("17".equals(prpLDlossPersTraceFee.getFeeTypeCode())){
							if(StringUtils.isNotBlank(OtherTraceFee.getFeeTypeCode())){
								commompartVo(OtherTraceFee,prpLDlossPersTraceFee);
							}else{
								OtherTraceFee=prpLDlossPersTraceFee;
							}
						}else{
							prpLDlossPersTraceFees.add(prpLDlossPersTraceFee);
						}
						
					}
					//?????????
					if(StringUtils.isNotBlank(clmPersonLossVo.getLawsuitFee())&& new BigDecimal(clmPersonLossVo.getLawsuitFee()).doubleValue()>0){
						PrpLDlossPersTraceFeeVo  prpLDlossPersTraceFee=makePersonFeeVo(clmPersonLossVo.getLawsuitFee(),"law_fee",registNo);
						if("17".equals(prpLDlossPersTraceFee.getFeeTypeCode())){
							if(StringUtils.isNotBlank(OtherTraceFee.getFeeTypeCode())){
								commompartVo(OtherTraceFee,prpLDlossPersTraceFee);
							}else{
								OtherTraceFee=prpLDlossPersTraceFee;
							}
						}else{
							prpLDlossPersTraceFees.add(prpLDlossPersTraceFee);
						}
					}
					//?????????
					if(StringUtils.isNotBlank(clmPersonLossVo.getDiningFee()) && new BigDecimal(clmPersonLossVo.getDiningFee()).doubleValue()>0){
						PrpLDlossPersTraceFeeVo  prpLDlossPersTraceFee=makePersonFeeVo(clmPersonLossVo.getDiningFee(),"dining_fee",registNo);
						if("17".equals(prpLDlossPersTraceFee.getFeeTypeCode())){
							if(StringUtils.isNotBlank(OtherTraceFee.getFeeTypeCode())){
								commompartVo(OtherTraceFee,prpLDlossPersTraceFee);
							}else{
								OtherTraceFee=prpLDlossPersTraceFee;
							}
						}else{
							prpLDlossPersTraceFees.add(prpLDlossPersTraceFee);
						}
					}
					//?????????
					if(StringUtils.isNotBlank(clmPersonLossVo.getSubsequentTreatment()) && new BigDecimal(clmPersonLossVo.getSubsequentTreatment()).doubleValue()>0){
						PrpLDlossPersTraceFeeVo  prpLDlossPersTraceFee=makePersonFeeVo(clmPersonLossVo.getSubsequentTreatment(),"subsequent_treatment",registNo);
						if("17".equals(prpLDlossPersTraceFee.getFeeTypeCode())){
							if(StringUtils.isNotBlank(OtherTraceFee.getFeeTypeCode())){
								commompartVo(OtherTraceFee,prpLDlossPersTraceFee);
							}else{
								OtherTraceFee=prpLDlossPersTraceFee;
							}
						}else{
							prpLDlossPersTraceFees.add(prpLDlossPersTraceFee);
						}
					}
					//?????????
					if(StringUtils.isNotBlank(clmPersonLossVo.getNutritionFee()) && new BigDecimal(clmPersonLossVo.getNutritionFee()).doubleValue()>0){
						PrpLDlossPersTraceFeeVo  prpLDlossPersTraceFee=makePersonFeeVo(clmPersonLossVo.getNutritionFee(),"nutrition_fee",registNo);
						if("17".equals(prpLDlossPersTraceFee.getFeeTypeCode())){
							if(StringUtils.isNotBlank(OtherTraceFee.getFeeTypeCode())){
								commompartVo(OtherTraceFee,prpLDlossPersTraceFee);
							}else{
								OtherTraceFee=prpLDlossPersTraceFee;
							}
						}else{
							prpLDlossPersTraceFees.add(prpLDlossPersTraceFee);
						}
					}
					//?????????
					if(StringUtils.isNotBlank(clmPersonLossVo.getWorkingCompensation()) && new BigDecimal(clmPersonLossVo.getWorkingCompensation()).doubleValue()>0){
						PrpLDlossPersTraceFeeVo  prpLDlossPersTraceFee=makePersonFeeVo(clmPersonLossVo.getWorkingCompensation(),"working_compensation",registNo);
						if("17".equals(prpLDlossPersTraceFee.getFeeTypeCode())){
							if(StringUtils.isNotBlank(OtherTraceFee.getFeeTypeCode())){
								commompartVo(OtherTraceFee,prpLDlossPersTraceFee);
							}else{
								OtherTraceFee=prpLDlossPersTraceFee;
							}
						}else{
							prpLDlossPersTraceFees.add(prpLDlossPersTraceFee);
						}
					}
					//?????????
					if(StringUtils.isNotBlank(clmPersonLossVo.getNursingFee()) && new BigDecimal(clmPersonLossVo.getNursingFee()).doubleValue()>0){
						PrpLDlossPersTraceFeeVo  prpLDlossPersTraceFee=makePersonFeeVo(clmPersonLossVo.getNursingFee(),"nursing_fee",registNo);
						if("17".equals(prpLDlossPersTraceFee.getFeeTypeCode())){
							if(StringUtils.isNotBlank(OtherTraceFee.getFeeTypeCode())){
								commompartVo(OtherTraceFee,prpLDlossPersTraceFee);
							}else{
								OtherTraceFee=prpLDlossPersTraceFee;
							}
						}else{
							prpLDlossPersTraceFees.add(prpLDlossPersTraceFee);
						}
					}
					//?????????
					if(StringUtils.isNotBlank(clmPersonLossVo.getTrafficFee()) && new BigDecimal(clmPersonLossVo.getTrafficFee()).doubleValue()>0){
						PrpLDlossPersTraceFeeVo  prpLDlossPersTraceFee=makePersonFeeVo(clmPersonLossVo.getTrafficFee(),"traffic_fee",registNo);
						if("17".equals(prpLDlossPersTraceFee.getFeeTypeCode())){
							if(StringUtils.isNotBlank(OtherTraceFee.getFeeTypeCode())){
								commompartVo(OtherTraceFee,prpLDlossPersTraceFee);
							}else{
								OtherTraceFee=prpLDlossPersTraceFee;
							}
						}else{
							prpLDlossPersTraceFees.add(prpLDlossPersTraceFee);
						}
					}
					//???????????????
					if(StringUtils.isNotBlank(clmPersonLossVo.getDeformityCompensateFee()) && new BigDecimal(clmPersonLossVo.getDeformityCompensateFee()).doubleValue()>0){
						PrpLDlossPersTraceFeeVo  prpLDlossPersTraceFee=makePersonFeeVo(clmPersonLossVo.getDeformityCompensateFee(),"deformity_compensate_fee",registNo);
						if("17".equals(prpLDlossPersTraceFee.getFeeTypeCode())){
							if(StringUtils.isNotBlank(OtherTraceFee.getFeeTypeCode())){
								commompartVo(OtherTraceFee,prpLDlossPersTraceFee);
							}else{
								OtherTraceFee=prpLDlossPersTraceFee;
							}
						}else{
							prpLDlossPersTraceFees.add(prpLDlossPersTraceFee);
						}
					}
					//???????????????
					if(StringUtils.isNotBlank(clmPersonLossVo.getDeathIndemnity()) && new BigDecimal(clmPersonLossVo.getDeathIndemnity()).doubleValue()>0){
						PrpLDlossPersTraceFeeVo  prpLDlossPersTraceFee=makePersonFeeVo(clmPersonLossVo.getDeathIndemnity(),"death_indemnity",registNo);
						if("17".equals(prpLDlossPersTraceFee.getFeeTypeCode())){
							if(StringUtils.isNotBlank(OtherTraceFee.getFeeTypeCode())){
								commompartVo(OtherTraceFee,prpLDlossPersTraceFee);
							}else{
								OtherTraceFee=prpLDlossPersTraceFee;
							}
						}else{
							prpLDlossPersTraceFees.add(prpLDlossPersTraceFee);
						}
					}
					//?????????
					if(StringUtils.isNotBlank(clmPersonLossVo.getFuneralFee()) && new BigDecimal(clmPersonLossVo.getFuneralFee()).doubleValue()>0){
						PrpLDlossPersTraceFeeVo  prpLDlossPersTraceFee=makePersonFeeVo(clmPersonLossVo.getFuneralFee(),"funeral_fee",registNo);
						if("17".equals(prpLDlossPersTraceFee.getFeeTypeCode())){
							if(StringUtils.isNotBlank(OtherTraceFee.getFeeTypeCode())){
								commompartVo(OtherTraceFee,prpLDlossPersTraceFee);
							}else{
								OtherTraceFee=prpLDlossPersTraceFee;
							}
						}else{
							prpLDlossPersTraceFees.add(prpLDlossPersTraceFee);
						}
					}
					//?????????
					if(StringUtils.isNotBlank(clmPersonLossVo.getFosterFee()) && new BigDecimal(clmPersonLossVo.getFosterFee()).doubleValue()>0){
						PrpLDlossPersTraceFeeVo  prpLDlossPersTraceFee=makePersonFeeVo(clmPersonLossVo.getFosterFee(),"foster_fee",registNo);
						if("17".equals(prpLDlossPersTraceFee.getFeeTypeCode())){
							if(StringUtils.isNotBlank(OtherTraceFee.getFeeTypeCode())){
								commompartVo(OtherTraceFee,prpLDlossPersTraceFee);
							}else{
								OtherTraceFee=prpLDlossPersTraceFee;
							}
						}else{
							prpLDlossPersTraceFees.add(prpLDlossPersTraceFee);
						}
					}
					//?????????
					if(StringUtils.isNotBlank(clmPersonLossVo.getAccommodationFee()) && new BigDecimal(clmPersonLossVo.getAccommodationFee()).doubleValue()>0){
						PrpLDlossPersTraceFeeVo  prpLDlossPersTraceFee=makePersonFeeVo(clmPersonLossVo.getAccommodationFee(),"accommodation_fee",registNo);
						if("17".equals(prpLDlossPersTraceFee.getFeeTypeCode())){
							if(StringUtils.isNotBlank(OtherTraceFee.getFeeTypeCode())){
								commompartVo(OtherTraceFee,prpLDlossPersTraceFee);
							}else{
								OtherTraceFee=prpLDlossPersTraceFee;
							}
						}else{
							prpLDlossPersTraceFees.add(prpLDlossPersTraceFee);
						}
					}
					//???????????????
					if(StringUtils.isNotBlank(clmPersonLossVo.getEmotionalCompensationFee()) && new BigDecimal(clmPersonLossVo.getEmotionalCompensationFee()).doubleValue()>0){
						PrpLDlossPersTraceFeeVo  prpLDlossPersTraceFee=makePersonFeeVo(clmPersonLossVo.getEmotionalCompensationFee(),"emotional_compensation_fee",registNo);
						if("17".equals(prpLDlossPersTraceFee.getFeeTypeCode())){
							if(StringUtils.isNotBlank(OtherTraceFee.getFeeTypeCode())){
								commompartVo(OtherTraceFee,prpLDlossPersTraceFee);
							}else{
								OtherTraceFee=prpLDlossPersTraceFee;
							}
						}else{
							prpLDlossPersTraceFees.add(prpLDlossPersTraceFee);
						}
					}
					//???????????????
					if(StringUtils.isNotBlank(clmPersonLossVo.getDeformityFee()) && new BigDecimal(clmPersonLossVo.getDeformityFee()).doubleValue()>0){
						PrpLDlossPersTraceFeeVo  prpLDlossPersTraceFee=makePersonFeeVo(clmPersonLossVo.getDeformityFee(),"deformity_fee",registNo);
						if("17".equals(prpLDlossPersTraceFee.getFeeTypeCode())){
							if(StringUtils.isNotBlank(OtherTraceFee.getFeeTypeCode())){
								commompartVo(OtherTraceFee,prpLDlossPersTraceFee);
							}else{
								OtherTraceFee=prpLDlossPersTraceFee;
							}
						}else{
							prpLDlossPersTraceFees.add(prpLDlossPersTraceFee);
						}
					}
					//?????????(??????)
					if(StringUtils.isNotBlank(clmPersonLossVo.getCheckupFee()) && new BigDecimal(clmPersonLossVo.getCheckupFee()).doubleValue()>0){
						PrpLDlossPersTraceFeeVo  prpLDlossPersTraceFee=makePersonFeeVo(clmPersonLossVo.getCheckupFee(),"checkup_fee",registNo);
						if("17".equals(prpLDlossPersTraceFee.getFeeTypeCode())){
							if(StringUtils.isNotBlank(OtherTraceFee.getFeeTypeCode())){
								commompartVo(OtherTraceFee,prpLDlossPersTraceFee);
							}else{
								OtherTraceFee=prpLDlossPersTraceFee;
							}
						}else{
							prpLDlossPersTraceFees.add(prpLDlossPersTraceFee);
						}
					}
					//?????????(??????)
					if(StringUtils.isNotBlank(clmPersonLossVo.getLawFee()) && new BigDecimal(clmPersonLossVo.getLawFee()).doubleValue()>0){
						PrpLDlossPersTraceFeeVo  prpLDlossPersTraceFee=makePersonFeeVo(clmPersonLossVo.getLawFee(),"law_fee",registNo);
						if("17".equals(prpLDlossPersTraceFee.getFeeTypeCode())){
							if(StringUtils.isNotBlank(OtherTraceFee.getFeeTypeCode())){
								commompartVo(OtherTraceFee,prpLDlossPersTraceFee);
							}else{
								OtherTraceFee=prpLDlossPersTraceFee;
							}
						}else{
							prpLDlossPersTraceFees.add(prpLDlossPersTraceFee);
						}
					}
					//????????????
					if(StringUtils.isNotBlank(clmPersonLossVo.getBaggageFee()) && new BigDecimal(clmPersonLossVo.getBaggageFee()).doubleValue()>0){
						PrpLDlossPersTraceFeeVo  prpLDlossPersTraceFee=makePersonFeeVo(clmPersonLossVo.getBaggageFee(),"baggage_fee",registNo);
						if("17".equals(prpLDlossPersTraceFee.getFeeTypeCode())){
							if(StringUtils.isNotBlank(OtherTraceFee.getFeeTypeCode())){
								commompartVo(OtherTraceFee,prpLDlossPersTraceFee);
							}else{
								OtherTraceFee=prpLDlossPersTraceFee;
							}
						}else{
							prpLDlossPersTraceFees.add(prpLDlossPersTraceFee);
						}
					}
					//????????????
					if(StringUtils.isNotBlank(clmPersonLossVo.getOtherFee()) && new BigDecimal(clmPersonLossVo.getOtherFee()).doubleValue()>0){
						PrpLDlossPersTraceFeeVo  prpLDlossPersTraceFee=makePersonFeeVo(clmPersonLossVo.getOtherFee(),"other_fee",registNo);
						if("17".equals(prpLDlossPersTraceFee.getFeeTypeCode())){
							if(StringUtils.isNotBlank(OtherTraceFee.getFeeTypeCode())){
								commompartVo(OtherTraceFee,prpLDlossPersTraceFee);
							}else{
								OtherTraceFee=prpLDlossPersTraceFee;
							}
						}else{
							prpLDlossPersTraceFees.add(prpLDlossPersTraceFee);
						}
					}
					if(StringUtils.isNotBlank(OtherTraceFee.getFeeTypeCode())){
						prpLDlossPersTraceFees.add(OtherTraceFee);
					}
					//????????????
					prpLDlossPersTraceVo.setPrpLDlossPersTraceFees(prpLDlossPersTraceFees);
					//??????????????????
					List<PrpLDlossPersExtVo> prpLDlossPersExts = new ArrayList<PrpLDlossPersExtVo>(0);
					List<PrpLDlossPersHospitalVo> prpLDlossPersHospitals = new ArrayList<PrpLDlossPersHospitalVo>(0);
					List<PrpLDlossPersRaiseVo> prpLDlossPersRaises = new ArrayList<PrpLDlossPersRaiseVo>(0);
					//????????????
					List<ClmPersonLossDiagnoseVo> clmPersonLossDiagnoseVos=respPerSonDlossDataVo.getClmPersonLossDiagnoseList();
					if(clmPersonLossDiagnoseVos!=null && clmPersonLossDiagnoseVos.size()>0){
						for(ClmPersonLossDiagnoseVo vo:clmPersonLossDiagnoseVos){
							PrpLDlossPersExtVo prpLDlossPersExt=new PrpLDlossPersExtVo();
							prpLDlossPersExt.setRegistNo(registNo);
							prpLDlossPersExt.setInjuredPart("99");//????????????
							prpLDlossPersExt.setInjuredDiag("09");//????????????
							prpLDlossPersExt.setDiagDetail(vo.getPartOneName()+"_"+vo.getPartTwoName()+"_"+vo.getSurgicalName());//????????????
							prpLDlossPersExt.setValidFlag("1");//??????
							prpLDlossPersExt.setCreateUser("AUTO");
							prpLDlossPersExt.setCreateTime(new Date());
							prpLDlossPersExts.add(prpLDlossPersExt);
						}
						prpLDlossPersInjuredVo.setPrpLDlossPersExts(prpLDlossPersExts);
					}
					//??????????????????
					List<ClmPersonFosterVo> clmPersonFosterVos=respPerSonDlossDataVo.getClmPersonFosterList();
					if(clmPersonFosterVos!=null && clmPersonFosterVos.size()>0){
						for(ClmPersonFosterVo vo: clmPersonFosterVos){
							PrpLDlossPersRaiseVo prpLDlossPersRaise=new PrpLDlossPersRaiseVo();
							prpLDlossPersRaise.setRegistNo(registNo);
							prpLDlossPersRaise.setPayPersonName(vo.getFosterName());//????????????
							if("F".equals(vo.getFosterSex())){
								prpLDlossPersRaise.setSex("1");//?????????
							}else{
								prpLDlossPersRaise.setSex("2");//?????????
							}
							prpLDlossPersRaise.setAge(StringUtils.isNotBlank(vo.getFosterAge())?new BigDecimal(vo.getFosterAge()):null);
							if("1".equals(vo.getFosterResidentStandard())){
								prpLDlossPersRaise.setDomicile("2");//????????????
							}else if("2".equals(vo.getFosterResidentStandard())){
								prpLDlossPersRaise.setDomicile("1");//????????????
							}else{
								prpLDlossPersRaise.setDomicile("3");//?????????????????????
							}
							prpLDlossPersRaise.setRelationship(vo.getFosterRelation());
							prpLDlossPersRaise.setInputTime(new Date());//????????????
							prpLDlossPersRaise.setValidFlag("1");//??????
							prpLDlossPersRaises.add(prpLDlossPersRaise);
							
						}
						prpLDlossPersInjuredVo.setPrpLDlossPersRaises(prpLDlossPersRaises);
					}
					//????????????
					List<ClmPersonHospitalVo> clmPersonHospitalVos=respPerSonDlossDataVo.getClmPersonHospitalList();
					if(clmPersonHospitalVos!=null && clmPersonHospitalVos.size()>0){
						for(ClmPersonHospitalVo vo:clmPersonHospitalVos){
							PrpLDlossPersHospitalVo prpLDlossPersHospital=new PrpLDlossPersHospitalVo();
							prpLDlossPersHospital.setRegistNo(registNo);//?????????
							if(StringUtils.isNotBlank(vo.getStartDate())){
								prpLDlossPersHospital.setInHospitalDate(StringtoDate(vo.getStartDate()));//??????????????????
							}
							if(StringUtils.isNotBlank(vo.getEndDate())){
								prpLDlossPersHospital.setOutHospitalDate(StringtoDate(vo.getEndDate()));//????????????
							}
							//prpLDlossPersHospital.setHospitalCode(vo.getHospitalCode());//????????????
							prpLDlossPersHospital.setHospitalName(vo.getHospitalName());//????????????
							prpLDlossPersHospital.setValidFlag("1");
							prpLDlossPersHospital.setCreateTime(new Date());
							prpLDlossPersHospital.setCreateUser("AUTO");
							prpLDlossPersHospitals.add(prpLDlossPersHospital);
						}
						prpLDlossPersInjuredVo.setPrpLDlossPersHospitals(prpLDlossPersHospitals);
						
					}
					persTraceService.updateAndsaveAndDelSonsPrpLDlossPersTrace(prpLDlossPersTraceVo);
					persTraceService.updateAndsaveAndDelSonsPrpLDlossPersInjured(prpLDlossPersInjuredVo);
				}else{
					if(clmPersonObjectVo==null){
						clmPersonObjectVo=new ClmPersonObjectVo();
					}
					ClmPersonLossVo clmPersonLossVo=respPerSonDlossDataVo.getClmPersonLoss();
					if(clmPersonLossVo==null){
						clmPersonLossVo=new ClmPersonLossVo();
					}
					OtherBaseInfoVo otherBaseInfoVo=respPerSonDlossDataVo.getOtherBaseInfo();
					if(otherBaseInfoVo==null){
						otherBaseInfoVo=new OtherBaseInfoVo();
					}
					bussTaskId=persTraceMainVos.get(0).getId();
					PrpLDlossPersTrace prpLDlossPersTrace=new PrpLDlossPersTrace();
					prpLDlossPersTrace.setPersTraceMainId(persTraceMainVos.get(0).getId());
					prpLDlossPersTrace.setRegistNo(registNo);
					prpLDlossPersTrace.setPersonName(clmPersonObjectVo.getPersonName());//????????????
					prpLDlossPersTrace.setLossFeeType("1");//????????????
					prpLDlossPersTrace.setTraceForms("2");//????????????
					prpLDlossPersTrace.setEndFlag("1");//??????????????????
					prpLDlossPersTrace.setSumReportFee(StringUtils.isBlank(clmPersonLossVo.getLossSum())?new BigDecimal(0):new BigDecimal(clmPersonLossVo.getLossSum()));//???????????????
					prpLDlossPersTrace.setSumRealFee(StringUtils.isBlank(clmPersonLossVo.getCustomerClaimAmount())?new BigDecimal(0):new BigDecimal(clmPersonLossVo.getCustomerClaimAmount()));//??????????????????
					prpLDlossPersTrace.setSumdefLoss(StringUtils.isBlank(clmPersonLossVo.getLossSum())?new BigDecimal(0):new BigDecimal(clmPersonLossVo.getLossSum()));//??????????????????
					prpLDlossPersTrace.setSumDetractionFee(new BigDecimal(0));//????????????
					prpLDlossPersTrace.setSumVeriReportFee(StringUtils.isBlank(clmPersonLossVo.getLossSum())?new BigDecimal(0):new BigDecimal(clmPersonLossVo.getLossSum()));//??????
					prpLDlossPersTrace.setSumVeriRealFee(StringUtils.isBlank(clmPersonLossVo.getCustomerClaimAmount())?new BigDecimal(0):new BigDecimal(clmPersonLossVo.getCustomerClaimAmount()));
					prpLDlossPersTrace.setSumVeriDefloss(StringUtils.isBlank(clmPersonLossVo.getLossSum())?new BigDecimal(0):new BigDecimal(clmPersonLossVo.getLossSum()));
					prpLDlossPersTrace.setSumVeriDetractionFee(new BigDecimal(0));
					prpLDlossPersTrace.setOperatorCode("AUTO");
					prpLDlossPersTrace.setComCode(prpLRegistVo.getComCode());//????????????
					prpLDlossPersTrace.setValidFlag("1");//????????????
					prpLDlossPersTrace.setCreateUser("AUTO");
					prpLDlossPersTrace.setCreateTime(new Date());
					prpLDlossPersTrace.setUpdateUser("AUTO");
					prpLDlossPersTrace.setUpdateTime(new Date());
					//????????????????????????
					PrpLDlossPersInjured prpLDlossPersInjured=new PrpLDlossPersInjured();
					prpLDlossPersInjured.setIdClmChannelProcess(clmPersonObjectVo.getIdClmChannelProcess());//?????????
					prpLDlossPersInjured.setPrpLDlossPersTrace(prpLDlossPersTrace);
					prpLDlossPersInjured.setRegistNo(registNo);//?????????
					prpLDlossPersInjured.setTreatSituation(clmPersonObjectVo.getCureType());//????????????
					prpLDlossPersInjured.setPersonName(clmPersonObjectVo.getPersonName());//????????????
					PiccCodeDictVo  piccCodeDictVo2=pingAnDictService.getDictData(PingAnCodeTypeEnum.JSZLX.getCodeType(),clmPersonObjectVo.getPersonCertificateType());//
					prpLDlossPersInjured.setCertiType(piccCodeDictVo2.getDhCodeCode());//????????????
					prpLDlossPersInjured.setCertiCode(clmPersonObjectVo.getPersonCertificateNo());//????????????
					prpLDlossPersInjured.setLossItemType("1");//????????????
					prpLDlossPersInjured.setPhoneNumber(clmPersonObjectVo.getPersonPhone());//????????????
					prpLDlossPersInjured.setPersonAge(StringUtils.isBlank(clmPersonObjectVo.getPersonAge())?new BigDecimal(0):new BigDecimal(clmPersonObjectVo.getPersonAge()));//??????
					if("M".equals(clmPersonObjectVo.getPersonSex())){
						prpLDlossPersInjured.setPersonSex("1");//??????
					}else{
						prpLDlossPersInjured.setPersonSex("2");
					 }
					if(StringUtils.isNotBlank(otherBaseInfoVo.getPersonEarn())){
						if(Double.valueOf(otherBaseInfoVo.getPersonEarn())<=5000){
							prpLDlossPersInjured.setIncome("01");;//????????????
						}else if(5000<Double.valueOf(otherBaseInfoVo.getPersonEarn()) && Double.valueOf(otherBaseInfoVo.getPersonEarn())<=10000){
							prpLDlossPersInjured.setIncome("02");;//??????
						}else if(10000<Double.valueOf(otherBaseInfoVo.getPersonEarn()) && Double.valueOf(otherBaseInfoVo.getPersonEarn())<=20000){
							prpLDlossPersInjured.setIncome("03");;//??????
						}else{
							prpLDlossPersInjured.setIncome("04");;//??????
						}
					}
					if("1".equals(clmPersonObjectVo.getCureType())){
						prpLDlossPersInjured.setWoundCode("01");//????????????
					}else if("2".equals(clmPersonObjectVo.getCureType())){
						prpLDlossPersInjured.setWoundCode("02");//????????????
					}else if("3".equals(clmPersonObjectVo.getCureType())){
						prpLDlossPersInjured.setWoundCode("03");//??????
					}
					if("1".equals(clmPersonObjectVo.getPersonResidentStandard())){
						prpLDlossPersInjured.setDomicile("2");//????????????
					}else if("2".equals(clmPersonObjectVo.getPersonResidentStandard())){
						prpLDlossPersInjured.setDomicile("1");//????????????
					}else{
						prpLDlossPersInjured.setDomicile("3");//?????????????????????
					}
					prpLDlossPersInjured.setInjuryPart("??????");//????????????
					prpLDlossPersInjured.setValidFlag("1");//????????????
					prpLDlossPersInjured.setCreateUser("AUTO");
					prpLDlossPersInjured.setCreateTime(new Date());
					//????????????
					List<ClmPersonLossImpairmentVo>  clmPersonLossImpairmentVos=respPerSonDlossDataVo.getClmPersonLossImpairmentList();
					if(clmPersonLossImpairmentVos!=null && clmPersonLossImpairmentVos.size()>0){
						prpLDlossPersInjured.setChkComCode(clmPersonLossImpairmentVos.get(0).getAppraisalDepartment());//??????????????????
						prpLDlossPersInjured.setAppraisaCity(clmPersonLossImpairmentVos.get(0).getCityMark());//?????????
					}
					prpLDlossPersTrace.setPrpLDlossPersInjured(prpLDlossPersInjured);
					List<PrpLDlossPersTraceFee> prpLDlossPersTraceFees = new ArrayList<PrpLDlossPersTraceFee>();
					//??????????????????
					PrpLDlossPersTraceFee OtherTraceFee=new PrpLDlossPersTraceFee();
					//?????????
					if(StringUtils.isNotBlank(clmPersonLossVo.getMedicalFee()) && new BigDecimal(clmPersonLossVo.getMedicalFee()).doubleValue()>0){
						PrpLDlossPersTraceFee  prpLDlossPersTraceFee=makePersonFee(clmPersonLossVo.getMedicalFee(),"medical_fee",prpLDlossPersTrace,registNo);
						if("17".equals(prpLDlossPersTraceFee.getFeeTypeCode())){
							if(StringUtils.isNotBlank(OtherTraceFee.getFeeTypeCode())){
								commompart(OtherTraceFee,prpLDlossPersTraceFee);
							}else{
								OtherTraceFee=prpLDlossPersTraceFee;
							}
						}else{
							prpLDlossPersTraceFees.add(prpLDlossPersTraceFee);
						}
						
					}
					//?????????
					if(StringUtils.isNotBlank(clmPersonLossVo.getLawsuitFee())&& new BigDecimal(clmPersonLossVo.getLawsuitFee()).doubleValue()>0){
						PrpLDlossPersTraceFee  prpLDlossPersTraceFee=makePersonFee(clmPersonLossVo.getLawsuitFee(),"law_fee",prpLDlossPersTrace,registNo);
						if("17".equals(prpLDlossPersTraceFee.getFeeTypeCode())){
							if(StringUtils.isNotBlank(OtherTraceFee.getFeeTypeCode())){
								commompart(OtherTraceFee,prpLDlossPersTraceFee);
							}else{
								OtherTraceFee=prpLDlossPersTraceFee;
							}
						}else{
							prpLDlossPersTraceFees.add(prpLDlossPersTraceFee);
						}
					}
					//?????????
					if(StringUtils.isNotBlank(clmPersonLossVo.getDiningFee()) && new BigDecimal(clmPersonLossVo.getDiningFee()).doubleValue()>0){
						PrpLDlossPersTraceFee  prpLDlossPersTraceFee=makePersonFee(clmPersonLossVo.getDiningFee(),"dining_fee",prpLDlossPersTrace,registNo);
						if("17".equals(prpLDlossPersTraceFee.getFeeTypeCode())){
							if(StringUtils.isNotBlank(OtherTraceFee.getFeeTypeCode())){
								commompart(OtherTraceFee,prpLDlossPersTraceFee);
							}else{
								OtherTraceFee=prpLDlossPersTraceFee;
							}
						}else{
							prpLDlossPersTraceFees.add(prpLDlossPersTraceFee);
						}
					}
					//?????????
					if(StringUtils.isNotBlank(clmPersonLossVo.getSubsequentTreatment()) && new BigDecimal(clmPersonLossVo.getSubsequentTreatment()).doubleValue()>0){
						PrpLDlossPersTraceFee  prpLDlossPersTraceFee=makePersonFee(clmPersonLossVo.getSubsequentTreatment(),"subsequent_treatment",prpLDlossPersTrace,registNo);
						if("17".equals(prpLDlossPersTraceFee.getFeeTypeCode())){
							if(StringUtils.isNotBlank(OtherTraceFee.getFeeTypeCode())){
								commompart(OtherTraceFee,prpLDlossPersTraceFee);
							}else{
								OtherTraceFee=prpLDlossPersTraceFee;
							}
						}else{
							prpLDlossPersTraceFees.add(prpLDlossPersTraceFee);
						}
					}
					//?????????
					if(StringUtils.isNotBlank(clmPersonLossVo.getNutritionFee()) && new BigDecimal(clmPersonLossVo.getNutritionFee()).doubleValue()>0){
						PrpLDlossPersTraceFee  prpLDlossPersTraceFee=makePersonFee(clmPersonLossVo.getNutritionFee(),"nutrition_fee",prpLDlossPersTrace,registNo);
						if("17".equals(prpLDlossPersTraceFee.getFeeTypeCode())){
							if(StringUtils.isNotBlank(OtherTraceFee.getFeeTypeCode())){
								commompart(OtherTraceFee,prpLDlossPersTraceFee);
							}else{
								OtherTraceFee=prpLDlossPersTraceFee;
							}
						}else{
							prpLDlossPersTraceFees.add(prpLDlossPersTraceFee);
						}
					}
					//?????????
					if(StringUtils.isNotBlank(clmPersonLossVo.getWorkingCompensation()) && new BigDecimal(clmPersonLossVo.getWorkingCompensation()).doubleValue()>0){
						PrpLDlossPersTraceFee  prpLDlossPersTraceFee=makePersonFee(clmPersonLossVo.getWorkingCompensation(),"working_compensation",prpLDlossPersTrace,registNo);
						if("17".equals(prpLDlossPersTraceFee.getFeeTypeCode())){
							if(StringUtils.isNotBlank(OtherTraceFee.getFeeTypeCode())){
								commompart(OtherTraceFee,prpLDlossPersTraceFee);
							}else{
								OtherTraceFee=prpLDlossPersTraceFee;
							}
						}else{
							prpLDlossPersTraceFees.add(prpLDlossPersTraceFee);
						}
					}
					//?????????
					if(StringUtils.isNotBlank(clmPersonLossVo.getNursingFee()) && new BigDecimal(clmPersonLossVo.getNursingFee()).doubleValue()>0){
						PrpLDlossPersTraceFee  prpLDlossPersTraceFee=makePersonFee(clmPersonLossVo.getNursingFee(),"nursing_fee",prpLDlossPersTrace,registNo);
						if("17".equals(prpLDlossPersTraceFee.getFeeTypeCode())){
							if(StringUtils.isNotBlank(OtherTraceFee.getFeeTypeCode())){
								commompart(OtherTraceFee,prpLDlossPersTraceFee);
							}else{
								OtherTraceFee=prpLDlossPersTraceFee;
							}
						}else{
							prpLDlossPersTraceFees.add(prpLDlossPersTraceFee);
						}
					}
					//?????????
					if(StringUtils.isNotBlank(clmPersonLossVo.getTrafficFee()) && new BigDecimal(clmPersonLossVo.getTrafficFee()).doubleValue()>0){
						PrpLDlossPersTraceFee  prpLDlossPersTraceFee=makePersonFee(clmPersonLossVo.getTrafficFee(),"traffic_fee",prpLDlossPersTrace,registNo);
						if("17".equals(prpLDlossPersTraceFee.getFeeTypeCode())){
							if(StringUtils.isNotBlank(OtherTraceFee.getFeeTypeCode())){
								commompart(OtherTraceFee,prpLDlossPersTraceFee);
							}else{
								OtherTraceFee=prpLDlossPersTraceFee;
							}
						}else{
							prpLDlossPersTraceFees.add(prpLDlossPersTraceFee);
						}
					}
					//???????????????
					if(StringUtils.isNotBlank(clmPersonLossVo.getDeformityCompensateFee()) && new BigDecimal(clmPersonLossVo.getDeformityCompensateFee()).doubleValue()>0){
						PrpLDlossPersTraceFee  prpLDlossPersTraceFee=makePersonFee(clmPersonLossVo.getDeformityCompensateFee(),"deformity_compensate_fee",prpLDlossPersTrace,registNo);
						if("17".equals(prpLDlossPersTraceFee.getFeeTypeCode())){
							if(StringUtils.isNotBlank(OtherTraceFee.getFeeTypeCode())){
								commompart(OtherTraceFee,prpLDlossPersTraceFee);
							}else{
								OtherTraceFee=prpLDlossPersTraceFee;
							}
						}else{
							prpLDlossPersTraceFees.add(prpLDlossPersTraceFee);
						}
					}
					//???????????????
					if(StringUtils.isNotBlank(clmPersonLossVo.getDeathIndemnity()) && new BigDecimal(clmPersonLossVo.getDeathIndemnity()).doubleValue()>0){
						PrpLDlossPersTraceFee  prpLDlossPersTraceFee=makePersonFee(clmPersonLossVo.getDeathIndemnity(),"death_indemnity",prpLDlossPersTrace,registNo);
						if("17".equals(prpLDlossPersTraceFee.getFeeTypeCode())){
							if(StringUtils.isNotBlank(OtherTraceFee.getFeeTypeCode())){
								commompart(OtherTraceFee,prpLDlossPersTraceFee);
							}else{
								OtherTraceFee=prpLDlossPersTraceFee;
							}
						}else{
							prpLDlossPersTraceFees.add(prpLDlossPersTraceFee);
						}
					}
					//?????????
					if(StringUtils.isNotBlank(clmPersonLossVo.getFuneralFee()) && new BigDecimal(clmPersonLossVo.getFuneralFee()).doubleValue()>0){
						PrpLDlossPersTraceFee  prpLDlossPersTraceFee=makePersonFee(clmPersonLossVo.getFuneralFee(),"funeral_fee",prpLDlossPersTrace,registNo);
						if("17".equals(prpLDlossPersTraceFee.getFeeTypeCode())){
							if(StringUtils.isNotBlank(OtherTraceFee.getFeeTypeCode())){
								commompart(OtherTraceFee,prpLDlossPersTraceFee);
							}else{
								OtherTraceFee=prpLDlossPersTraceFee;
							}
						}else{
							prpLDlossPersTraceFees.add(prpLDlossPersTraceFee);
						}
					}
					//?????????
					if(StringUtils.isNotBlank(clmPersonLossVo.getFosterFee()) && new BigDecimal(clmPersonLossVo.getFosterFee()).doubleValue()>0){
						PrpLDlossPersTraceFee  prpLDlossPersTraceFee=makePersonFee(clmPersonLossVo.getFosterFee(),"foster_fee",prpLDlossPersTrace,registNo);
						if("17".equals(prpLDlossPersTraceFee.getFeeTypeCode())){
							if(StringUtils.isNotBlank(OtherTraceFee.getFeeTypeCode())){
								commompart(OtherTraceFee,prpLDlossPersTraceFee);
							}else{
								OtherTraceFee=prpLDlossPersTraceFee;
							}
						}else{
							prpLDlossPersTraceFees.add(prpLDlossPersTraceFee);
						}
					}
					//?????????
					if(StringUtils.isNotBlank(clmPersonLossVo.getAccommodationFee()) && new BigDecimal(clmPersonLossVo.getAccommodationFee()).doubleValue()>0){
						PrpLDlossPersTraceFee  prpLDlossPersTraceFee=makePersonFee(clmPersonLossVo.getAccommodationFee(),"accommodation_fee",prpLDlossPersTrace,registNo);
						if("17".equals(prpLDlossPersTraceFee.getFeeTypeCode())){
							if(StringUtils.isNotBlank(OtherTraceFee.getFeeTypeCode())){
								commompart(OtherTraceFee,prpLDlossPersTraceFee);
							}else{
								OtherTraceFee=prpLDlossPersTraceFee;
							}
						}else{
							prpLDlossPersTraceFees.add(prpLDlossPersTraceFee);
						}
					}
					//???????????????
					if(StringUtils.isNotBlank(clmPersonLossVo.getEmotionalCompensationFee()) && new BigDecimal(clmPersonLossVo.getEmotionalCompensationFee()).doubleValue()>0){
						PrpLDlossPersTraceFee  prpLDlossPersTraceFee=makePersonFee(clmPersonLossVo.getEmotionalCompensationFee(),"emotional_compensation_fee",prpLDlossPersTrace,registNo);
						if("17".equals(prpLDlossPersTraceFee.getFeeTypeCode())){
							if(StringUtils.isNotBlank(OtherTraceFee.getFeeTypeCode())){
								commompart(OtherTraceFee,prpLDlossPersTraceFee);
							}else{
								OtherTraceFee=prpLDlossPersTraceFee;
							}
						}else{
							prpLDlossPersTraceFees.add(prpLDlossPersTraceFee);
						}
					}
					//???????????????
					if(StringUtils.isNotBlank(clmPersonLossVo.getDeformityFee()) && new BigDecimal(clmPersonLossVo.getDeformityFee()).doubleValue()>0){
						PrpLDlossPersTraceFee  prpLDlossPersTraceFee=makePersonFee(clmPersonLossVo.getDeformityFee(),"deformity_fee",prpLDlossPersTrace,registNo);
						if("17".equals(prpLDlossPersTraceFee.getFeeTypeCode())){
							if(StringUtils.isNotBlank(OtherTraceFee.getFeeTypeCode())){
								commompart(OtherTraceFee,prpLDlossPersTraceFee);
							}else{
								OtherTraceFee=prpLDlossPersTraceFee;
							}
						}else{
							prpLDlossPersTraceFees.add(prpLDlossPersTraceFee);
						}
					}
					//?????????(??????)
					if(StringUtils.isNotBlank(clmPersonLossVo.getCheckupFee()) && new BigDecimal(clmPersonLossVo.getCheckupFee()).doubleValue()>0){
						PrpLDlossPersTraceFee  prpLDlossPersTraceFee=makePersonFee(clmPersonLossVo.getCheckupFee(),"checkup_fee",prpLDlossPersTrace,registNo);
						if("17".equals(prpLDlossPersTraceFee.getFeeTypeCode())){
							if(StringUtils.isNotBlank(OtherTraceFee.getFeeTypeCode())){
								commompart(OtherTraceFee,prpLDlossPersTraceFee);
							}else{
								OtherTraceFee=prpLDlossPersTraceFee;
							}
						}else{
							prpLDlossPersTraceFees.add(prpLDlossPersTraceFee);
						}
					}
					//?????????(??????)
					if(StringUtils.isNotBlank(clmPersonLossVo.getLawFee()) && new BigDecimal(clmPersonLossVo.getLawFee()).doubleValue()>0){
						PrpLDlossPersTraceFee  prpLDlossPersTraceFee=makePersonFee(clmPersonLossVo.getLawFee(),"law_fee",prpLDlossPersTrace,registNo);
						if("17".equals(prpLDlossPersTraceFee.getFeeTypeCode())){
							if(StringUtils.isNotBlank(OtherTraceFee.getFeeTypeCode())){
								commompart(OtherTraceFee,prpLDlossPersTraceFee);
							}else{
								OtherTraceFee=prpLDlossPersTraceFee;
							}
						}else{
							prpLDlossPersTraceFees.add(prpLDlossPersTraceFee);
						}
					}
					//????????????
					if(StringUtils.isNotBlank(clmPersonLossVo.getBaggageFee()) && new BigDecimal(clmPersonLossVo.getBaggageFee()).doubleValue()>0){
						PrpLDlossPersTraceFee  prpLDlossPersTraceFee=makePersonFee(clmPersonLossVo.getBaggageFee(),"baggage_fee",prpLDlossPersTrace,registNo);
						if("17".equals(prpLDlossPersTraceFee.getFeeTypeCode())){
							if(StringUtils.isNotBlank(OtherTraceFee.getFeeTypeCode())){
								commompart(OtherTraceFee,prpLDlossPersTraceFee);
							}else{
								OtherTraceFee=prpLDlossPersTraceFee;
							}
						}else{
							prpLDlossPersTraceFees.add(prpLDlossPersTraceFee);
						}
					}
					//????????????
					if(StringUtils.isNotBlank(clmPersonLossVo.getOtherFee()) && new BigDecimal(clmPersonLossVo.getOtherFee()).doubleValue()>0){
						PrpLDlossPersTraceFee  prpLDlossPersTraceFee=makePersonFee(clmPersonLossVo.getOtherFee(),"other_fee",prpLDlossPersTrace,registNo);
						if("17".equals(prpLDlossPersTraceFee.getFeeTypeCode())){
							if(StringUtils.isNotBlank(OtherTraceFee.getFeeTypeCode())){
								commompart(OtherTraceFee,prpLDlossPersTraceFee);
							}else{
								OtherTraceFee=prpLDlossPersTraceFee;
							}
						}else{
							prpLDlossPersTraceFees.add(prpLDlossPersTraceFee);
						}
					}
					if(StringUtils.isNotBlank(OtherTraceFee.getFeeTypeCode())){
						prpLDlossPersTraceFees.add(OtherTraceFee);
					}
					//????????????
					prpLDlossPersTrace.setPrpLDlossPersTraceFees(prpLDlossPersTraceFees);
					//??????????????????
					List<PrpLDlossPersExt> prpLDlossPersExts = new ArrayList<PrpLDlossPersExt>(0);
					List<PrpLDlossPersHospital> prpLDlossPersHospitals = new ArrayList<PrpLDlossPersHospital>(0);
					List<PrpLDlossPersRaise> prpLDlossPersRaises = new ArrayList<PrpLDlossPersRaise>(0);
					//????????????
					List<ClmPersonLossDiagnoseVo> clmPersonLossDiagnoseVos=respPerSonDlossDataVo.getClmPersonLossDiagnoseList();
					if(clmPersonLossDiagnoseVos!=null && clmPersonLossDiagnoseVos.size()>0){
						for(ClmPersonLossDiagnoseVo vo:clmPersonLossDiagnoseVos){
							PrpLDlossPersExt prpLDlossPersExt=new PrpLDlossPersExt();
							prpLDlossPersExt.setPrpLDlossPersInjured(prpLDlossPersInjured);
							prpLDlossPersExt.setRegistNo(registNo);
							prpLDlossPersExt.setInjuredPart("99");//????????????
							prpLDlossPersExt.setInjuredDiag("09");//????????????
							prpLDlossPersExt.setDiagDetail(vo.getPartOneName()+"_"+vo.getPartTwoName()+"_"+vo.getSurgicalName());//????????????
							prpLDlossPersExt.setValidFlag("1");//??????
							prpLDlossPersExt.setCreateUser("AUTO");
							prpLDlossPersExt.setCreateTime(new Date());
							prpLDlossPersExts.add(prpLDlossPersExt);
						}
						prpLDlossPersInjured.setPrpLDlossPersExts(prpLDlossPersExts);
					}
					//??????????????????
					List<ClmPersonFosterVo> clmPersonFosterVos=respPerSonDlossDataVo.getClmPersonFosterList();
					if(clmPersonFosterVos!=null && clmPersonFosterVos.size()>0){
						for(ClmPersonFosterVo vo: clmPersonFosterVos){
							PrpLDlossPersRaise prpLDlossPersRaise=new PrpLDlossPersRaise();
							prpLDlossPersRaise.setRegistNo(registNo);
							prpLDlossPersRaise.setPrpLDlossPersInjured(prpLDlossPersInjured);
							prpLDlossPersRaise.setPayPersonName(vo.getFosterName());//????????????
							if("F".equals(vo.getFosterSex())){
								prpLDlossPersRaise.setSex("1");//?????????
							}else{
								prpLDlossPersRaise.setSex("2");//?????????
							}
							prpLDlossPersRaise.setAge(StringUtils.isNotBlank(vo.getFosterAge())?new BigDecimal(vo.getFosterAge()):null);
							if("1".equals(vo.getFosterResidentStandard())){
								prpLDlossPersRaise.setDomicile("2");//????????????
							}else if("2".equals(vo.getFosterResidentStandard())){
								prpLDlossPersRaise.setDomicile("1");//????????????
							}else{
								prpLDlossPersRaise.setDomicile("3");//?????????????????????
							}
							prpLDlossPersRaise.setRelationship(vo.getFosterRelation());
							prpLDlossPersRaise.setInputTime(new Date());//????????????
							prpLDlossPersRaise.setValidFlag("1");//??????
							prpLDlossPersRaises.add(prpLDlossPersRaise);
							
						}
						prpLDlossPersInjured.setPrpLDlossPersRaises(prpLDlossPersRaises);
					}
					//????????????
					List<ClmPersonHospitalVo> clmPersonHospitalVos=respPerSonDlossDataVo.getClmPersonHospitalList();
					if(clmPersonHospitalVos!=null && clmPersonHospitalVos.size()>0){
						for(ClmPersonHospitalVo vo:clmPersonHospitalVos){
							PrpLDlossPersHospital prpLDlossPersHospital=new PrpLDlossPersHospital();
							prpLDlossPersHospital.setPrpLDlossPersInjured(prpLDlossPersInjured);
							prpLDlossPersHospital.setRegistNo(registNo);//?????????
							if(StringUtils.isNotBlank(vo.getStartDate())){
								prpLDlossPersHospital.setInHospitalDate(StringtoDate(vo.getStartDate()));//??????????????????
							}
							if(StringUtils.isNotBlank(vo.getEndDate())){
								prpLDlossPersHospital.setOutHospitalDate(StringtoDate(vo.getEndDate()));//????????????
							}
							//prpLDlossPersHospital.setHospitalCode(vo.getHospitalCode());//????????????
							prpLDlossPersHospital.setHospitalName(vo.getHospitalName());//????????????
							prpLDlossPersHospital.setValidFlag("1");
							prpLDlossPersHospital.setCreateTime(new Date());
							prpLDlossPersHospital.setCreateUser("AUTO");
							prpLDlossPersHospitals.add(prpLDlossPersHospital);
						}
						prpLDlossPersInjured.setPrpLDlossPersHospitals(prpLDlossPersHospitals);
						
					}
					databaseDao.save(PrpLDlossPersTrace.class, prpLDlossPersTrace);
				}
				
			}
			PrpLDlossPersTraceMainVo prpLDlossPersTraceMainVo=persTraceService.findPersTraceMainVobyId(bussTaskId);
			// ??????????????????
			PrpLWfTaskVo taskVo =null;
			List<PrpLWfTaskVo> prpLWfTaskVos=wfFlowQueryService.findPrpWfTaskVoForIn(registNo,FlowNode.PLoss.name(),FlowNode.PLFirst.name());
			List<PrpLWfTaskVo> taskVos=wfFlowQueryService.findPrpWfTaskVoForIn(registNo,FlowNode.PLoss.name(),FlowNode.PLCharge_LV0.name());
			if(prpLWfTaskVos!=null && prpLWfTaskVos.size()>0){
				prpLWfTaskVos.get(0).setHandlerIdKey(prpLDlossPersTraceMainVo.getId().toString());
				wfTaskHandleService.updateTaskIn(prpLWfTaskVos.get(0));
				taskVo=prpLWfTaskVos.get(0);
				WfTaskSubmitVo submitVo = new WfTaskSubmitVo();
				
				submitVo.setAssignUser("AUTO");
				submitVo.setAssignCom(comcode);
				submitVo.setTaskInKey(bussTaskId.toString());
				submitVo.setTaskInUser("AUTO");
				submitVo.setFlowId(taskVo.getFlowId());
				submitVo.setFlowTaskId(taskVo.getTaskId());
				submitVo.setCurrentNode(FlowNode.PLFirst);
				submitVo.setComCode(comcode);
				submitVo.setNextNode(FlowNode.PLCharge_LV0);
				submitVo.setHandlertime(new Date());
				
				List<PrpLWfTaskVo> taskInVoList = wfTaskHandleService.submitLossPerson(prpLDlossPersTraceMainVo,submitVo);// ??????????????????
				if(taskInVoList!=null && taskInVoList.size()>0){
					// ??????????????????
					logger.info("???????????????flowNode={},??????????????????????????????={}",FlowNode.PLCharge_LV0.name(),PersVeriFlag.CHARGEPASS);
					prpLDlossPersTraceMainVo.setUnderwriteFlag(PersVeriFlag.CHARGEPASS);
					prpLDlossPersTraceMainVo.setUndwrtFeeCode("AUTO");
					prpLDlossPersTraceMainVo.setUndwrtFeeName("AUTO");
					prpLDlossPersTraceMainVo.setUndwrtFeeEndDate(new Date());
					prpLDlossPersTraceMainVo.setAuditStatus(AuditStatus.SUBMITCHARGE);
					WfTaskSubmitVo submitVLVo = new WfTaskSubmitVo();
					submitVLVo.setTaskInKey(bussTaskId.toString());
					submitVLVo.setTaskInUser("AUTO");
					submitVLVo.setComCode(comcode);
					submitVLVo.setNextNode(FlowNode.END);
					submitVLVo.setFlowId(taskInVoList.get(0).getFlowId());
					submitVLVo.setFlowTaskId(taskInVoList.get(0).getTaskId());
					submitVLVo.setCurrentNode(FlowNode.PLCharge_LV0);
					submitVLVo.setAssignCom(comcode);
					submitVLVo.setAssignUser("AUTO");
					submitVLVo.setOthenNodes(null);
					submitVLVo.setHandlertime(new Date());
					prpLDlossPersTraceMainVo.setUndwrtFeeCode("AUTO");
					prpLDlossPersTraceMainVo.setUndwrtFeeName("AUTO");
					wfTaskHandleService.submitLossPerson(prpLDlossPersTraceMainVo,submitVLVo);// ??????????????????
				}
				
			}else{//
				if(taskVos!=null && taskVos.size()>0){
					// ??????????????????
					logger.info("???????????????flowNode={},??????????????????????????????={}",FlowNode.PLCharge_LV0.name(),PersVeriFlag.CHARGEPASS);
					prpLDlossPersTraceMainVo.setUnderwriteFlag(PersVeriFlag.CHARGEPASS);
					prpLDlossPersTraceMainVo.setUndwrtFeeCode("AUTO");
					prpLDlossPersTraceMainVo.setUndwrtFeeName("AUTO");
					prpLDlossPersTraceMainVo.setUndwrtFeeEndDate(new Date());
					prpLDlossPersTraceMainVo.setAuditStatus(AuditStatus.SUBMITCHARGE);
					WfTaskSubmitVo submitVLVo = new WfTaskSubmitVo();
					submitVLVo.setTaskInKey(bussTaskId.toString());
					submitVLVo.setTaskInUser("AUTO");
					submitVLVo.setComCode(comcode);
					submitVLVo.setNextNode(FlowNode.END);
					submitVLVo.setFlowId(taskVos.get(0).getFlowId());
					submitVLVo.setFlowTaskId(taskVos.get(0).getTaskId());
					submitVLVo.setCurrentNode(FlowNode.PLCharge_LV0);
					submitVLVo.setAssignCom(comcode);
					submitVLVo.setAssignUser("AUTO");
					submitVLVo.setOthenNodes(null);
					submitVLVo.setHandlertime(new Date());
					prpLDlossPersTraceMainVo.setUndwrtFeeCode("AUTO");
					prpLDlossPersTraceMainVo.setUndwrtFeeName("AUTO");
					wfTaskHandleService.submitLossPerson(prpLDlossPersTraceMainVo,submitVLVo);// ??????????????????
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.info("??????????????????????????????",e);
			resultBean.fail("??????????????????????????????"+e.getMessage());
		}
		return resultBean;
	}
	
  /**
   * ??????????????????????????????
   * @param feeAmout
   * @param FeeType
   * @param prpLDlossPersTrace
   * @param registNo
   * @param clmPersonLossVo
   * @return
   */
  private PrpLDlossPersTraceFee makePersonFee(String feeAmout,String FeeType,PrpLDlossPersTrace prpLDlossPersTrace,String registNo){
	    PrpLDlossPersTraceFee prpLDlossPersTraceFee=new PrpLDlossPersTraceFee();//?????????
		prpLDlossPersTraceFee.setPrpLDlossPersTrace(prpLDlossPersTrace);
		prpLDlossPersTraceFee.setRegistNo(registNo);
		PiccCodeDictVo piccCodeDictVo=pingAnDictService.getDictData(PingAnCodeTypeEnum.RSJBFYLX.name(),FeeType);
		prpLDlossPersTraceFee.setFeeTypeCode(piccCodeDictVo.getDhCodeCode());
		prpLDlossPersTraceFee.setFeeTypeName(piccCodeDictVo.getDhCodeName());
		prpLDlossPersTraceFee.setReportFee(new BigDecimal(feeAmout));//??????
		prpLDlossPersTraceFee.setRealFee(new BigDecimal(feeAmout));//????????????
		prpLDlossPersTraceFee.setDetractionfee(new BigDecimal(0));//????????????
		prpLDlossPersTraceFee.setDefloss(new BigDecimal(feeAmout));//????????????
		prpLDlossPersTraceFee.setVeriReportFee(new BigDecimal(feeAmout));//????????????
		prpLDlossPersTraceFee.setVeriRealFee(new BigDecimal(feeAmout));//??????????????????
		prpLDlossPersTraceFee.setVeriDetractionFee(new BigDecimal(0));//??????????????????
		prpLDlossPersTraceFee.setVeriDefloss(new BigDecimal(feeAmout));//??????????????????
		prpLDlossPersTraceFee.setCreateUser("AUTO");
		prpLDlossPersTraceFee.setCreateTime(new Date());
		prpLDlossPersTraceFee.setUpdateUser("AUTO");
		prpLDlossPersTraceFee.setUpdateTime(new Date());
		prpLDlossPersTraceFee.setStatus("1");
		return prpLDlossPersTraceFee;
  }
  
  /**
   * ??????????????????????????????
   * @param feeAmout
   * @param FeeType
   * @param prpLDlossPersTrace
   * @param registNo
   * @param clmPersonLossVo
   * @return
   */
  private PrpLDlossPersTraceFeeVo makePersonFeeVo(String feeAmout,String FeeType,String registNo){
	    PrpLDlossPersTraceFeeVo prpLDlossPersTraceFee=new PrpLDlossPersTraceFeeVo();//?????????
		prpLDlossPersTraceFee.setRegistNo(registNo);
		PiccCodeDictVo piccCodeDictVo=pingAnDictService.getDictData(PingAnCodeTypeEnum.RSJBFYLX.name(),FeeType);
		prpLDlossPersTraceFee.setFeeTypeCode(piccCodeDictVo.getDhCodeCode());
		prpLDlossPersTraceFee.setFeeTypeName(piccCodeDictVo.getDhCodeName());
		prpLDlossPersTraceFee.setReportFee(new BigDecimal(feeAmout));//??????
		prpLDlossPersTraceFee.setRealFee(new BigDecimal(feeAmout));//????????????
		prpLDlossPersTraceFee.setDetractionfee(new BigDecimal(0));//????????????
		prpLDlossPersTraceFee.setDefloss(new BigDecimal(feeAmout));//????????????
		prpLDlossPersTraceFee.setVeriReportFee(new BigDecimal(feeAmout));//????????????
		prpLDlossPersTraceFee.setVeriRealFee(new BigDecimal(feeAmout));//??????????????????
		prpLDlossPersTraceFee.setVeriDetractionFee(new BigDecimal(0));//??????????????????
		prpLDlossPersTraceFee.setVeriDefloss(new BigDecimal(feeAmout));//??????????????????
		prpLDlossPersTraceFee.setCreateUser("AUTO");
		prpLDlossPersTraceFee.setCreateTime(new Date());
		prpLDlossPersTraceFee.setUpdateUser("AUTO");
		prpLDlossPersTraceFee.setUpdateTime(new Date());
		prpLDlossPersTraceFee.setStatus("1");
		return prpLDlossPersTraceFee;
  }
  /**
   * ????????????
   * @param OtherTraceFee
   * @param prpLDlossPersTraceFee
   */
  private void commompart(PrpLDlossPersTraceFee OtherTraceFee,PrpLDlossPersTraceFee prpLDlossPersTraceFee){
	    OtherTraceFee.setReportFee(OtherTraceFee.getReportFee().add(prpLDlossPersTraceFee.getReportFee()));//??????
		OtherTraceFee.setRealFee(OtherTraceFee.getRealFee().add(prpLDlossPersTraceFee.getRealFee()));//????????????
		OtherTraceFee.setDetractionfee(OtherTraceFee.getDetractionfee().add(prpLDlossPersTraceFee.getDetractionfee()));//????????????
		OtherTraceFee.setDefloss(OtherTraceFee.getDefloss().add(prpLDlossPersTraceFee.getDefloss()));//????????????
		OtherTraceFee.setVeriReportFee(OtherTraceFee.getVeriReportFee().add(prpLDlossPersTraceFee.getVeriReportFee()));//????????????
		OtherTraceFee.setVeriRealFee(OtherTraceFee.getVeriRealFee().add(prpLDlossPersTraceFee.getVeriRealFee()));//??????????????????
		OtherTraceFee.setVeriDetractionFee(OtherTraceFee.getVeriDetractionFee().add(prpLDlossPersTraceFee.getVeriDetractionFee()));//??????????????????
		OtherTraceFee.setVeriDefloss(OtherTraceFee.getVeriDefloss().add(prpLDlossPersTraceFee.getVeriDefloss()));//??????????????????
  }
  /**
   * ????????????
   * @param OtherTraceFee
   * @param prpLDlossPersTraceFee
   */
  private void commompartVo(PrpLDlossPersTraceFeeVo OtherTraceFee,PrpLDlossPersTraceFeeVo prpLDlossPersTraceFee){
	    OtherTraceFee.setReportFee(OtherTraceFee.getReportFee().add(prpLDlossPersTraceFee.getReportFee()));//??????
		OtherTraceFee.setRealFee(OtherTraceFee.getRealFee().add(prpLDlossPersTraceFee.getRealFee()));//????????????
		OtherTraceFee.setDetractionfee(OtherTraceFee.getDetractionfee().add(prpLDlossPersTraceFee.getDetractionfee()));//????????????
		OtherTraceFee.setDefloss(OtherTraceFee.getDefloss().add(prpLDlossPersTraceFee.getDefloss()));//????????????
		OtherTraceFee.setVeriReportFee(OtherTraceFee.getVeriReportFee().add(prpLDlossPersTraceFee.getVeriReportFee()));//????????????
		OtherTraceFee.setVeriRealFee(OtherTraceFee.getVeriRealFee().add(prpLDlossPersTraceFee.getVeriRealFee()));//??????????????????
		OtherTraceFee.setVeriDetractionFee(OtherTraceFee.getVeriDetractionFee().add(prpLDlossPersTraceFee.getVeriDetractionFee()));//??????????????????
		OtherTraceFee.setVeriDefloss(OtherTraceFee.getVeriDefloss().add(prpLDlossPersTraceFee.getVeriDefloss()));//??????????????????
  }
  /**
 	 * ??????????????????
 	 *  String ??????????????????Date
 	 * @param strDate
 	 * @return
 	 * @throws ParseException
 	 */
 	private static Date StringtoDate(String strDate){
 		Date str=null;
 		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
 		  if(strDate!=null){
 			  try {
 				str=format.parse(strDate);
 			} catch (Exception e) {
 				e.printStackTrace();
 			}
 		}
 		return str;
 	}
}
