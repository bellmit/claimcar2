package ins.sino.claimcar.genilex.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;
import org.springframework.scheduling.annotation.Async;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;

import ins.platform.common.service.facade.AreaDictService;
import ins.platform.utils.ClaimBaseCoder;
import ins.platform.utils.DateUtils;
import ins.platform.utils.HttpClientHander;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants.ValidFlag;
import ins.sino.claimcar.carchild.util.CarchildUtil;
import ins.sino.claimcar.carinterface.service.ClaimInterfaceLogService;
import ins.sino.claimcar.carinterface.util.BusinessType;
import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;
import ins.sino.claimcar.carplatform.service.CiClaimPlatformLogService;
import ins.sino.claimcar.carplatform.vo.CiClaimPlatformLogVo;
import ins.sino.claimcar.certify.service.CertifyPubService;
import ins.sino.claimcar.certify.vo.PrpLCertifyDirectVo;
import ins.sino.claimcar.certify.vo.PrpLCertifyMainVo;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.PrpLCheckCarInfoVo;
import ins.sino.claimcar.check.vo.PrpLCheckCarVo;
import ins.sino.claimcar.check.vo.PrpLCheckCarVoBase;
import ins.sino.claimcar.check.vo.PrpLCheckDriverVo;
import ins.sino.claimcar.check.vo.PrpLCheckDutyVo;
import ins.sino.claimcar.check.vo.PrpLCheckPersonVo;
import ins.sino.claimcar.check.vo.PrpLCheckPropVo;
import ins.sino.claimcar.check.vo.PrpLCheckTaskVo;
import ins.sino.claimcar.check.vo.PrpLCheckVo;
import ins.sino.claimcar.check.vo.PrpLDisasterVo;
import ins.sino.claimcar.claim.service.ClaimTaskService;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.claim.vo.PrpLChargeVo;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claim.vo.PrpLLossItemVo;
import ins.sino.claimcar.claim.vo.PrpLLossPersonVo;
import ins.sino.claimcar.claim.vo.PrpLLossPropVo;
import ins.sino.claimcar.claim.vo.PrpLPadPayMainVo;
import ins.sino.claimcar.claim.vo.PrpLPadPayPersonVo;
import ins.sino.claimcar.claim.vo.PrpLPaymentVo;
import ins.sino.claimcar.endcase.service.EndCaseService;
import ins.sino.claimcar.endcase.vo.PrpLEndCaseVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.PayCustomService;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.genilex.ReportInfoService;
import ins.sino.claimcar.genilex.comResVo.SoapEnvelopeVo;
import ins.sino.claimcar.genilex.dlossService.ClaimJxService;
import ins.sino.claimcar.genilex.service.ClaimToGenilexService;
import ins.sino.claimcar.genilex.vo.check.CheckBody;
import ins.sino.claimcar.genilex.vo.check.CheckEntities;
import ins.sino.claimcar.genilex.vo.check.CheckPacket;
import ins.sino.claimcar.genilex.vo.check.CheckSoapenvVo;
import ins.sino.claimcar.genilex.vo.check.Survery;
import ins.sino.claimcar.genilex.vo.check.SurveryPerson;
import ins.sino.claimcar.genilex.vo.check.SurveryProtect;
import ins.sino.claimcar.genilex.vo.check.SurveryVehicle;
import ins.sino.claimcar.genilex.vo.common.AccountInfo;
import ins.sino.claimcar.genilex.vo.common.ClaimMain;
import ins.sino.claimcar.genilex.vo.common.FraudRequest;
import ins.sino.claimcar.genilex.vo.common.Requestor;
import ins.sino.claimcar.genilex.vo.endCase.Adjustment;
import ins.sino.claimcar.genilex.vo.endCase.AdvancedPay;
import ins.sino.claimcar.genilex.vo.endCase.AdvancedPayDetail;
import ins.sino.claimcar.genilex.vo.endCase.ClaimPay;
import ins.sino.claimcar.genilex.vo.endCase.ClaimPays;
import ins.sino.claimcar.genilex.vo.endCase.CloseCase;
import ins.sino.claimcar.genilex.vo.endCase.CoverItem;
import ins.sino.claimcar.genilex.vo.endCase.EndCaseBody;
import ins.sino.claimcar.genilex.vo.endCase.EndCaseEntities;
import ins.sino.claimcar.genilex.vo.endCase.EndCasePacket;
import ins.sino.claimcar.genilex.vo.endCase.EndCaseSoapenvVo;
import ins.sino.claimcar.genilex.vo.endCase.PayItem;
import ins.sino.claimcar.manager.vo.PrpLPayCustomVo;
import ins.sino.claimcar.pay.service.PadPayPubService;
import ins.sino.claimcar.pay.service.PadPayService;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistExtVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"},timeout = 1000000)
@Path("claimToGenilexService")
public class ClaimToGenilexServiceSpring implements ClaimToGenilexService {

	private static Logger logger = LoggerFactory.getLogger(ClaimToGenilexServiceSpring.class);
	
	@Autowired
	PolicyViewService policyViewService;
	@Autowired
	RegistQueryService registQueryService;
	@Autowired
	AreaDictService areaDictService;
	@Autowired
	CheckTaskService checkTaskService;
	@Autowired
	WfTaskHandleService wfTaskHandleService;
	@Autowired
	CertifyPubService certifyPubService;
	@Autowired
	CiClaimPlatformLogService ciClaimPlatformLogService;
	@Autowired
	PadPayPubService padPayPubService;
	@Autowired
	CompensateTaskService compensateTaskService;
	@Autowired
	ClaimTaskService claimTaskService;
	@Autowired
	PayCustomService payCustomService;
	@Autowired
	PadPayService padPayService;
	@Autowired
    private ClaimInterfaceLogService interfaceLogService;
	@Autowired
	RegistQueryService registService;
	@Autowired
    ReportInfoService reportInfoService;
	@Autowired
    ClaimJxService claimJxService;
	@Autowired
	EndCaseService endCaseService;
	@Autowired
	CompensateTaskService compesateService;
	
	@Override
	public void checkToGenilex(PrpLCheckVo checkVo,Long flowTaskId){
		
		/*声明实体类*/
		CheckSoapenvVo soapenvVo = new CheckSoapenvVo();
		CheckBody body = new CheckBody();
		CheckPacket checkPacket = new CheckPacket();
		SoapEnvelopeVo soapEnvelopeVo = new SoapEnvelopeVo();
		Requestor requestor = new Requestor();
		AccountInfo accountInfo = new AccountInfo();
		List<FraudRequest>  productRequests = new ArrayList<FraudRequest>();
		FraudRequest fraudRequest = new FraudRequest();
		CheckEntities  entities = new CheckEntities();
		List<Survery>  surverys = new ArrayList<Survery>();
		Survery survery = new Survery();
		List<ClaimMain>  claimMains = new ArrayList<ClaimMain>();//赔案主档
		List<SurveryVehicle> surveryVehicles = new ArrayList<SurveryVehicle>();//车辆查勘情况列表
		List<SurveryProtect> surveryProtects = new ArrayList<SurveryProtect>();//财产查勘情况列表
		List<SurveryPerson> surveryPersons = new ArrayList<SurveryPerson>();
		
		PrpLCheckTaskVo checkTaskVo = checkVo.getPrpLCheckTask();
		List<PrpLCheckCarVo> carVoList = checkVo.getPrpLCheckTask().getPrpLCheckCars();
		List<PrpLCheckPropVo> propVoList = checkTaskVo.getPrpLCheckProps();
		List<PrpLCheckPersonVo> personVoList = checkTaskVo.getPrpLCheckPersons();
		List<PrpLCMainVo> cMainVoList = policyViewService.getPolicyAllInfo(checkVo.getRegistNo());
		PrpLRegistVo registVo = registQueryService.findByRegistNo(checkVo.getRegistNo());
		PrpLRegistExtVo registExtVo = registVo.getPrpLRegistExt();
		Boolean unLocal = unLocal(cMainVoList, registExtVo);//是否异地出险
		PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(flowTaskId.doubleValue());
		Date date = new Date();
		List<PrpLCertifyDirectVo> prpLCertifyDirectList = certifyPubService.findCertifyDirectByRegistNo(checkVo.getRegistNo());
		BigDecimal sumLossFee = BigDecimal.ZERO;//查勘总金额
		PrpLDisasterVo disasterVo = checkTaskService.findDisasterVoByRegistNo(checkVo.getRegistNo());
		
		
		/*组织数据*/
		checkPacket.setPacketType("REQUEST");
		
		requestor.setReference(flowTaskId.toString());
		requestor.setTimestamp(DateUtils.dateToStr(new Date(), DateUtils.YToSec));
		requestor.setLineOfBusiness("PM");
		requestor.setPointOfRequest("POC");
		requestor.setTransactionType("C");
		requestor.setEchoEntities("True");
		requestor.setEchoProductRequests("False");
        String userName = SpringProperties.getProperty("GENILEX_USERNAME");
        String userPswd = SpringProperties.getProperty("GENILEX_USERPSWD");
        accountInfo.setUserName(userName);
        accountInfo.setUserPswd(userPswd);
		accountInfo.setCryptType("04");
		requestor.setAccountInfo(accountInfo);
		checkPacket.setRequestor(requestor);
		
		fraudRequest.setProductRequestId("01");
		fraudRequest.setReportNo(checkVo.getRegistNo());
		fraudRequest.setScoredType("01");
		fraudRequest.setServiceType("C002");
		productRequests.add(fraudRequest);
		checkPacket.setProductRequests(productRequests);
		
		survery.setReportNo(checkVo.getRegistNo());
		survery.setSelfSurveyType("1".equals(checkVo.getCheckType()) ? "1":"3");
		survery.setIsCompleteSurvery("true");

		for(PrpLCheckCarVo carVo:carVoList){
			SurveryVehicle surveryVehicle = new SurveryVehicle();//车辆查勘情况信息
			sumLossFee = sumLossFee.add(NullToZero(carVo.getLossFee()));
			PrpLCheckCarInfoVo carInfoVo = carVo.getPrpLCheckCarInfo();
			PrpLCheckDriverVo driverVo = carVo.getPrpLCheckDriver();
			PrpLCheckDutyVo dutyVo = checkTaskService.findCheckDuty(checkVo.getRegistNo(),carVo.getSerialNo());
			
			surveryVehicle.setSurveryID(carVo.getCarid().toString());
			surveryVehicle.setLicensePlateNo(carInfoVo.getLicenseNo());
			surveryVehicle.setLicensePlateType("25".equals(carInfoVo.getLicenseType())  ? "99":carInfoVo.getLicenseType());
			surveryVehicle.setVin(carInfoVo.getVinNo());
			surveryVehicle.setEngineNo(carInfoVo.getEngineNo());
			surveryVehicle.setModel(carInfoVo.getModelCode());
			surveryVehicle.setModelName(carInfoVo.getBrandName());
			surveryVehicle.setDriverName(driverVo.getDriverName());
			surveryVehicle.setDriverLicenseNo(driverVo.getDrivingLicenseNo());
			surveryVehicle.setVehicleProperty("1".equals(carVo.getSerialNo()) ?"1":"2");
			if(unLocal){
				if("3".equals(checkVo.getCheckType())){
					surveryVehicle.setFieldType("3");
				}else{
					surveryVehicle.setFieldType("2");
				}
			}else{
				if("3".equals(checkVo.getCheckType())){
					surveryVehicle.setFieldType("1");
				}else{
					surveryVehicle.setFieldType("0");
				}
			}
			surveryVehicle.setEstimatedLossAmount(carVo.getLossFee()!=null ? carVo.getLossFee().toString():"0");
			surveryVehicle.setCheckerName(checkTaskVo.getChecker());
			surveryVehicle.setCheckerCode(checkTaskVo.getCheckerCode());
			surveryVehicle.setCheckerCertiCode(checkTaskVo.getCheckerIdfNo());
			surveryVehicle.setCheckStartTime(DateUtils.dateToStr(checkTaskVo.getCheckDate(), DateUtils.YToSec));
			surveryVehicle.setCheckEndTime(DateUtils.dateToStr(carVo.getUpdateTime(), DateUtils.YToSec));
			surveryVehicle.setCheckAddr(checkTaskVo.getCheckAddress());
			surveryVehicle.setIncidentTypeCode(decode("damageCode", checkVo.getDamageCode()));
			surveryVehicle.setLossCauseCode(decode("damageCode", checkVo.getDamageCode()));
			surveryVehicle.setLossTime(DateUtils.dateToStr(registVo.getDamageTime(), DateUtils.YToSec));
			if(StringUtils.isNotEmpty(registVo.getDamageAreaCode())){
				surveryVehicle.setOcccurredProvince(registVo.getDamageAreaCode().substring(0, 2)+"0000");
				surveryVehicle.setOcccurredCity(registVo.getDamageAreaCode().substring(0, 4)+"00");
				surveryVehicle.setOcccurredRegion(registVo.getDamageAreaCode());
			}
			surveryVehicle.setOcccurredSpot(registVo.getDamageAddress());
			surveryVehicle.setIncidentDesc(registExtVo.getDangerRemark());
			surveryVehicle.setInvestigationDepartmentCode(checkTaskVo.getComCode());
			surveryVehicle.setInvestigationTime(DateUtils.dateToStr(checkTaskVo.getCheckDate(), DateUtils.YToSec));
			surveryVehicle.setInvestigationOpinion(checkTaskVo.getContexts());
			surveryVehicle.setAtfaultTypeCode(decode("indemnityDuty", dutyVo.getIndemnityDuty()));
			if(dutyVo.getIndemnityDutyRate()!=null){
				surveryVehicle.setAtfaultProportion(dutyVo.getIndemnityDutyRate().toString());
			}
			if(FlowNode.ChkRe.name().equals(wfTaskVo.getSubNodeCode())){
				surveryVehicle.setReInvestigateInd("1");
			}else{
				surveryVehicle.setReInvestigateInd("0");
			}
			if(driverVo.getDriverValidDate() != null){
				if(DateUtils.compareDays(date, driverVo.getDriverValidDate())<=0){
					surveryVehicle.setPermissionInd("1");
				}else{
					surveryVehicle.setPermissionInd("0");
				}
			}
			if(prpLCertifyDirectList!=null && prpLCertifyDirectList.size()>0){
				Map<String,String> map = new HashMap<String, String>();
				for(PrpLCertifyDirectVo vo:prpLCertifyDirectList){
					map.put(vo.getLossItemCode(), vo.getLossItemCode());
				}
				if(map.containsKey("C0404")){
					surveryVehicle.setRegisteredvehInd("1");
				}else{
					surveryVehicle.setRegisteredvehInd("0");
				}
				if(map.containsKey("C0803")){
					surveryVehicle.setHasDrivinglicenceInd("1");
				}else{
					surveryVehicle.setHasDrivinglicenceInd("0");
				}
			}else{
				surveryVehicle.setRegisteredvehInd("0");
				surveryVehicle.setHasDrivinglicenceInd("0");
			}
			surveryVehicle.setHandleTime(DateUtils.dateToStr(carVo.getCreateTime(), DateUtils.YToSec));
			surveryVehicle.setCollidedInd(checkVo.getIsClaimSelf());
			surveryVehicle.setAccidentResponsibleType(checkVo.getManageType()!=null ? "0"+checkVo.getManageType():"99");
			if(carVoList.size()>1){
				surveryVehicle.setAtfaultThirdVehicleInd("1");
				surveryVehicle.setLossTypeInd2("1");
				surveryVehicle.setIsSingleAccident("1");
			}else{
				surveryVehicle.setAtfaultThirdVehicleInd("0");
				surveryVehicle.setLossTypeInd2("0");
				surveryVehicle.setIsSingleAccident("0");
			}
			if(checkVo.getPrpLCheckTask().getPrpLCheckPersons()!=null && checkVo.getPrpLCheckTask().getPrpLCheckPersons().size()>0){
				surveryVehicle.setRelateInjureFlag("1");
				surveryVehicle.setLossTypeInd4("1");
			}else{
				surveryVehicle.setRelateInjureFlag("0");
				surveryVehicle.setLossTypeInd4("0");
			}
			if("0".equals(wfTaskVo.getSubCheckFlag())){
				surveryVehicle.setDifferentPlaceSurvyFlag("0");
			}else{
				surveryVehicle.setDifferentPlaceSurvyFlag("1");
			}
			surveryVehicle.setInsuranceAssessSurvyFlag(checkVo.getCheckClass());
			surveryVehicle.setQuicklyClaimFlag(registVo.getIsQuickCase()!=null ? registVo.getIsQuickCase():"0");
			surveryVehicle.setActiontype("0".equals(wfTaskVo.getIsMobileAccept()) ? "0":"1");
			surveryVehicle.setLossTypeInd1("1");
			if(checkVo.getPrpLCheckTask().getPrpLCheckProps()!=null && checkVo.getPrpLCheckTask().getPrpLCheckProps().size()>0){
				surveryVehicle.setLossTypeInd3("1");
			}else{
				surveryVehicle.setLossTypeInd3("0");
			}
			surveryVehicle.setCreateBy(carVo.getCreateUser());
			surveryVehicle.setCreateTime(DateUtils.dateToStr(carVo.getCreateTime(), DateUtils.YToSec));
			surveryVehicle.setUpdateBy(carVo.getUpdateUser());
			surveryVehicle.setUpdateTime(DateUtils.dateToStr(carVo.getUpdateTime(), DateUtils.YToSec));
			surveryVehicles.add(surveryVehicle);
		}
		survery.setSurveryVehicles(surveryVehicles);
		
		if(propVoList!=null && propVoList.size()>0){
			for(PrpLCheckPropVo vo:propVoList){
				SurveryProtect surveryProtect = new SurveryProtect();
				sumLossFee = sumLossFee.add(NullToZero(vo.getLossFee()));
				
				surveryProtect.setSurveryID(vo.getId().toString());
				surveryProtect.setSerialNo(vo.getLossPartyId().toString());
				surveryProtect.setProtectName(vo.getLossItemName());
				surveryProtect.setLossNum(vo.getLossNum());
				surveryProtect.setEstimatedLossAmount(vo.getLossFee().toString());
				if(vo.getLossPartyId()==1){
					surveryProtect.setProtectProperty("1");
				}else{
					surveryProtect.setProtectProperty("2");
				}
				surveryProtect.setCheckStartTime(DateUtils.dateToStr(checkTaskVo.getCheckDate(), DateUtils.YToSec));
				surveryProtect.setCheckEndTime(DateUtils.dateToStr(vo.getUpdateTime(), DateUtils.YToSec));
				surveryProtect.setCheckerName(checkTaskVo.getChecker());
				surveryProtect.setCheckerCode(checkTaskVo.getCheckerCode());
				surveryProtect.setCheckAddr(checkTaskVo.getCheckAddress());
				surveryProtect.setCreateBy(vo.getCreateUser());
				surveryProtect.setCreateTime(DateUtils.dateToStr(vo.getCreateTime(), DateUtils.YToSec));
				surveryProtect.setUpdateBy(vo.getUpdateUser());
				surveryProtect.setUpdateTime(DateUtils.dateToStr(vo.getUpdateTime(), DateUtils.YToSec));
				surveryProtects.add(surveryProtect);
			}
			survery.setSurveryProtects(surveryProtects);
		}
		
		if(personVoList!=null && personVoList.size()>0){
			for(PrpLCheckPersonVo vo:personVoList){
				SurveryPerson surveryPerson = new SurveryPerson();
				sumLossFee = sumLossFee.add(NullToZero(vo.getLossFee()));
				
				surveryPerson.setSurveryID(vo.getId().toString());
				surveryPerson.setSerialNo(vo.getLossPartyId().toString());
				surveryPerson.setPersonPayType("2".equals(vo.getPersonPayType()) ? "2":"1");
				surveryPerson.setEstimatedLossAmount(vo.getLossFee().toString());
				surveryPerson.setPersonProperty("1".equals(vo.getPersonProp()) ? "2":"1");
				surveryPerson.setCheckerName(checkTaskVo.getChecker());
				surveryPerson.setCheckerCode(checkTaskVo.getCheckerCode());
				surveryPerson.setCheckStartTime(DateUtils.dateToStr(checkTaskVo.getCheckDate(), DateUtils.YToSec));
				surveryPerson.setCheckEndTime(DateUtils.dateToStr(vo.getUpdateTime(), DateUtils.YToSec));
				surveryPerson.setCheckAddr(checkTaskVo.getCheckAddress());
				surveryPerson.setCreateBy(vo.getCreateUser());
				surveryPerson.setCreateTime(DateUtils.dateToStr(vo.getCreateTime(), DateUtils.YToSec));
				surveryPerson.setUpdateBy(vo.getUpdateUser());
				surveryPerson.setUpdateTime(DateUtils.dateToStr(vo.getUpdateTime(), DateUtils.YToSec));
				surveryPersons.add(surveryPerson);
			}
			survery.setSurveryPersons(surveryPersons);
		}
		surverys.add(survery);
		
		//赔案主档
		for(PrpLCMainVo cmainVo:cMainVoList){
			ClaimMain claimMain = new ClaimMain();
			claimMain.setReportNo(checkVo.getRegistNo());
			claimMain.setClaimNo(checkVo.getRegistNo()+"_"+cmainVo.getPolicyNo());
			List<PrpLClaimVo> claimVoList = claimTaskService.findprpLClaimVoListByRegistAndPolicyNo(cmainVo.getRegistNo(), cmainVo.getPolicyNo(),ValidFlag.VALID);
			PrpLClaimVo claimVo = null;
			if(claimVoList != null && claimVoList.size()>0){
				claimVo = claimVoList.get(0);
				claimMain.setRegisterAmt(claimVo.getSumClaim().toString());
				claimMain.setRegisterBy(claimVo.getCreateUser());
				claimMain.setRegisterTime(DateUtils.dateToStr(claimVo.getCreateTime(), DateUtils.YToSec));
			}
			
			for(PrpLCMainVo vo:cMainVoList){
				if("1101".equals(vo.getRiskCode())){
					CiClaimPlatformLogVo platformLogVo = ciClaimPlatformLogService.findLogByBussNo("50", checkVo.getRegistNo(), vo.getComCode());
					claimMain.setClaimSequenceNo(platformLogVo.getClaimSeqNo());
				}
			}
			if(cmainVo.getClaimTimes() != null){
				claimMain.setClaimTimes(cmainVo.getClaimTimes().toString());
			}else{
				claimMain.setClaimTimes("0");
			}
			claimMain.setClaimStatus("2");
			claimMain.setDepartmentCode(cmainVo.getComCode());
			claimMain.setPolicyNo(cmainVo.getPolicyNo());
			if(cmainVo.getEndorseTimes() != null){
				claimMain.setEdrPrjNo(cmainVo.getEndorseTimes().toString());
			}else{
				claimMain.setEdrPrjNo("0");
			}
			claimMain.setPlanCode(cmainVo.getRiskCode());
			claimMain.setLossTime(DateUtils.dateToStr(registVo.getDamageTime(), DateUtils.YToSec));
			claimMain.setReportTime(DateUtils.dateToStr(registVo.getReportTime(), DateUtils.YToSec));
			claimMain.setCatastropheLossInd(checkVo.getMajorCaseFlag());
			claimMain.setDepositInd("0");
			claimMain.setRegisterInd("0");
			if(sumLossFee.compareTo(BigDecimal.ZERO)>0){
				claimMain.setPaymentInd("1");
			}else{
				claimMain.setPaymentInd("0");
			}
			claimMain.setxEpaFlag("0");
			claimMain.setTotalPaymentAmt(sumLossFee.toString());
			claimMain.setPaymentCurrencyCode("CNY");
			if(disasterVo!=null && disasterVo.getId()!=null){
				claimMain.setHugeLossCode("1");
			}else{
				claimMain.setHugeLossCode("0");
			}
			claimMain.setCollidedThirdInd(checkVo.getIsClaimSelf());
			claimMain.setActiontype("0".equals(wfTaskVo.getIsMobileAccept()) ? "0":"1");
			claimMain.setSubrogationFlag(checkVo.getIsSubRogation());
			claimMain.setCreateBy(checkVo.getCreateUser());
			claimMain.setCreateTime(DateUtils.dateToStr(checkVo.getCreateTime(), DateUtils.YToSec));
			claimMain.setUpdateBy(checkVo.getUpdateUser());
			claimMain.setUpdateTime(DateUtils.dateToStr(checkVo.getUpdateTime(), DateUtils.YToSec));
			claimMains.add(claimMain);
		}
		entities.setSurverys(surverys);
		entities.setClaimMains(claimMains);
		checkPacket.setEntities(entities);
		body.setDataPacket(checkPacket);
		soapenvVo.setBody(body);
		
		XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
		stream.autodetectAnnotations(true);
		stream.setMode(XStream.NO_REFERENCES);
		stream.aliasSystemAttribute(null,"class");// 去掉 class属性
		
		//发送报文
		String requestXML = stream.toXML(soapenvVo);
		String responseXML = "";
		String url = SpringProperties.getProperty("GENILEX_URL");
		logger.info("查勘提交请求精励联讯发送报文---------------------------"+requestXML);
		ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
		try{
			responseXML = CarchildUtil.requestPlatform(requestXML,url,"");
			logger.info("查勘提交请求精励联讯返回报文---------------------------"+responseXML);
			soapEnvelopeVo = ClaimBaseCoder.xmlToObj(responseXML, SoapEnvelopeVo.class);
			if("C".equals(soapEnvelopeVo.getBodyVo().getDataPacketVo().getResponseSummaryVo().getOverallStatus())){
	             logVo.setStatus("1");
	             logVo.setErrorCode("true");
	         }else {
	             logVo.setStatus("0");
	             logVo.setErrorCode("false");
	         }
	         logVo.setErrorMessage(soapEnvelopeVo.getBodyVo().getDataPacketVo().getResponseSummaryVo().getOverallMessage());
		}catch (Exception e) {
			logger.info("---------------------------查勘提交请求精励报错"+e.getMessage());
			e.printStackTrace();
			logVo.setStatus("0");
            logVo.setErrorCode("false");
            logVo.setErrorMessage(e.getMessage());
		}finally{
			logVo.setBusinessType(BusinessType.GENILEX_Check.name());
	        logVo.setBusinessName(BusinessType.GENILEX_Check.getName());
	        logVo.setRegistNo(registVo.getRegistNo());
            logVo.setOperateNode(FlowNode.Check.name());
            logVo.setComCode(checkTaskVo.getComCode());
            logVo.setRequestTime(date);
            logVo.setRequestUrl(url);
            logVo.setCreateUser(checkVo.getCreateUser());
            logVo.setCreateTime(date);
            logVo.setRequestXml(requestXML);
            logVo.setResponseXml(responseXML);
            logVo.setCompensateNo(flowTaskId.toString());
            interfaceLogService.save(logVo);
		}
	}
	
	@Override
	public void endCaseToGenilex(PrpLEndCaseVo endCaseVo,PrpLCompensateVo compensateVo,String taskId){
		
		EndCaseSoapenvVo soapenvVo = new EndCaseSoapenvVo();
		EndCaseBody body = new EndCaseBody();
		EndCasePacket endCasePacket = new EndCasePacket();
		SoapEnvelopeVo soapEnvelopeVo = new SoapEnvelopeVo();
		Requestor  requestor = new Requestor();
		AccountInfo accountInfo = new AccountInfo();
		List<FraudRequest>  productRequests = new ArrayList<FraudRequest>();
		FraudRequest fraudRequest = new FraudRequest();
		EndCaseEntities entities = new EndCaseEntities();
		List<ClaimMain>  claimMains = new ArrayList<ClaimMain>();//赔案主档
		List<Adjustment> adjustmentas = new ArrayList<Adjustment>();
		List<CloseCase> closeCases = new ArrayList<CloseCase>();
		ClaimPays claimPays = new ClaimPays();
		List<AdvancedPay> advancedPays = new ArrayList<AdvancedPay>();
		
		
		PrpLCMainVo cmainVo = policyViewService.getPolicyInfo(endCaseVo.getRegistNo(), endCaseVo.getPolicyNo());
		PrpLCMainVo ciCMainVo = policyViewService.getRegistNoAndRiskCodeInfo(endCaseVo.getRegistNo(), "1101");
		PrpLRegistVo registVo = registQueryService.findByRegistNo(endCaseVo.getRegistNo());
		PrpLRegistExtVo registExtVo = registVo.getPrpLRegistExt();
		List<PrpLCompensateVo> prePayMainVoList = compensateTaskService.queryCompensateByOther(cmainVo.getRegistNo(),"Y",cmainVo.getPolicyNo(),"1");
		PrpLClaimVo claimVo = claimTaskService.findClaimVoByClaimNo(endCaseVo.getClaimNo());
		PrpLCertifyMainVo certifyVo = certifyPubService.findPrpLCertifyMainVoByRegistNo(endCaseVo.getRegistNo());
		PrpLDisasterVo disasterVo = checkTaskService.findDisasterVoByRegistNo(endCaseVo.getRegistNo());
		PrpLCheckVo checkVo = checkTaskService.findCheckVoByRegistNo(endCaseVo.getRegistNo());
		PrpLPadPayMainVo padPayVo = padPayService.getPadPayInfo(endCaseVo.getRegistNo(), endCaseVo.getClaimNo());
		PrpLCheckDutyVo checkDutyVo = checkTaskService.findCheckDuty(endCaseVo.getRegistNo(), 1);
		Date date = new Date();
		
		/*组织数据*/
		endCasePacket.setPacketType("REQUEST");
		
		requestor.setReference(taskId);
		requestor.setTimestamp(DateUtils.dateToStr(new Date(), DateUtils.YToSec));
		requestor.setLineOfBusiness("PM");
		requestor.setPointOfRequest("POC");
		requestor.setTransactionType("C");
		requestor.setEchoEntities("True");
		requestor.setEchoProductRequests("False");
		
        String userName = SpringProperties.getProperty("GENILEX_USERNAME");
        String userPswd = SpringProperties.getProperty("GENILEX_USERPSWD");
        accountInfo.setUserName(userName);
        accountInfo.setUserPswd(userPswd);
		accountInfo.setCryptType("04");
		requestor.setAccountInfo(accountInfo);
		endCasePacket.setRequestor(requestor);
		
		fraudRequest.setProductRequestId("01");
		fraudRequest.setReportNo(endCaseVo.getRegistNo());
		fraudRequest.setScoredType("01");
		fraudRequest.setServiceType("C008");
		productRequests.add(fraudRequest);
		endCasePacket.setProductRequests(productRequests);
		
		//赔案主档
		ClaimMain claimMain = new ClaimMain();
		claimMain.setReportNo(endCaseVo.getRegistNo());
		claimMain.setClaimNo(endCaseVo.getRegistNo()+"_"+endCaseVo.getPolicyNo());
		if(ciCMainVo!=null && ciCMainVo.getPolicyNo()!=null){
			CiClaimPlatformLogVo platformLogVo = ciClaimPlatformLogService.findLogByBussNo("50", endCaseVo.getRegistNo(), ciCMainVo.getComCode());
			claimMain.setClaimSequenceNo(platformLogVo.getClaimSeqNo());
		}
		if(cmainVo.getClaimTimes() != null){
			claimMain.setClaimTimes(cmainVo.getClaimTimes().toString());
		}else{
			claimMain.setClaimTimes("0");
		}
		claimMain.setClaimStatus("9");
		claimMain.setDepartmentCode(cmainVo.getComCode());
		claimMain.setPolicyNo(cmainVo.getPolicyNo());
		if(cmainVo.getEndorseTimes() != null){
			claimMain.setEdrPrjNo(cmainVo.getEndorseTimes().toString());
		}else{
			claimMain.setEdrPrjNo("0");
		}
		claimMain.setPlanCode(cmainVo.getRiskCode());
		claimMain.setLossTime(DateUtils.dateToStr(registVo.getDamageTime(), DateUtils.YToSec));
		claimMain.setReportTime(DateUtils.dateToStr(registVo.getReportTime(), DateUtils.YToSec));
		if(wfTaskHandleService.existTaskByNodeCode(registVo.getRegistNo(), FlowNode.ChkBig, registVo.getRegistNo(), "1")){
			claimMain.setCatastropheLossInd("1");
		}else{
			claimMain.setCatastropheLossInd("0");
		}
		if(padPayVo!=null && "1".equals(padPayVo.getUnderwriteFlag())){
			claimMain.setDepositInd("1");
			BigDecimal sumPadPayFee = BigDecimal.ZERO;
			for(PrpLPadPayPersonVo vo:padPayVo.getPrpLPadPayPersons()){
				sumPadPayFee = sumPadPayFee.add(NullToZero(vo.getCostSum()));
			}
			claimMain.setTotalDepositAmt(sumPadPayFee.toString());
			claimMain.setDepositCurrencyCode("CNY");
		}else{
			claimMain.setDepositInd("0");
		}
		claimMain.setRegisterInd("1");
		claimMain.setDocumentCompletedInd("1");
		if(prePayMainVoList!=null && prePayMainVoList.size()>0){
			BigDecimal sumPrePay = BigDecimal.ZERO;
			for(PrpLCompensateVo vo:prePayMainVoList){
				if("1".equals(vo.getUnderwriteFlag())){
					claimMain.setPrepayInd("1");
					sumPrePay = sumPrePay.add(NullToZero(vo.getSumAmt()));
				}
				claimMain.setTotalPrepayAmt(sumPrePay.toString());
				claimMain.setPrepayCurrencyCode("CNY");
			}
		}else{
			claimMain.setPrepayInd("0");
		}
		if(compensateVo!=null){
			claimMain.setGettingbackInd(compensateVo.getRecoveryFlag());
			claimMain.setLitigationInd(compensateVo.getLawsuitFlag());
			if(compensateVo.getSumAmt().compareTo(BigDecimal.ZERO)>0){
				claimMain.setPaymentInd("1");
			}else{
				claimMain.setPaymentInd("0");
			}
		}
		claimMain.setRegisterAmt(claimVo.getSumClaim().toString());
		claimMain.setRegisterCurrencyCode("CNY");
		claimMain.setxEpaFlag("0");
		claimMain.setTotalPaymentAmt(NullToZero(compensateVo.getSumAmt()).toString());
		claimMain.setPaymentCurrencyCode("CNY");
		claimMain.setRegisterBy(claimVo.getCreateUser());
		claimMain.setRegisterTime(DateUtils.dateToStr(claimVo.getCreateTime(), DateUtils.YToSec));
		claimMain.setEndcaseBy(endCaseVo.getCreateUser());
		claimMain.setEndcaseDepartmentCode(compensateVo.getComCode());
		claimMain.setEndcaseTime(DateUtils.dateToStr(endCaseVo.getEndCaseDate(), DateUtils.YToSec));
		claimMain.setEndcasePigeonholeNo(endCaseVo.getRegressNo());
		if("1101".equals(endCaseVo.getRiskCode())){
			claimMain.setRevokecaseInd("1".equals(certifyVo.getIsJQFraud()) ? "2":"Z");
		}else{
			claimMain.setRevokecaseInd("1".equals(certifyVo.getIsSYFraud()) ? "2":"Z");
		}
		
		if(disasterVo!=null && disasterVo.getId()!=null){
			claimMain.setHugeLossCode("1");
		}else{
			claimMain.setHugeLossCode("0");
		}
		claimMain.setCollidedThirdInd(checkVo.getIsClaimSelf());
		claimMain.setActiontype("0");
		claimMain.setSubrogationFlag(checkVo.getIsSubRogation());
		claimMain.setCreateBy(endCaseVo.getCreateUser());
		if(endCaseVo.getCreateTime()!=null){
			claimMain.setCreateTime(DateUtils.dateToStr(endCaseVo.getCreateTime(), DateUtils.YToSec));
		}
		claimMain.setUpdateBy(endCaseVo.getUpdateUser());
		if(endCaseVo.getUpdateTime()!=null){
			claimMain.setUpdateTime(DateUtils.dateToStr(endCaseVo.getUpdateTime(), DateUtils.YToSec));
		}
		claimMains.add(claimMain);
		entities.setClaimMains(claimMains);
		
		//理算核赔信息
		Adjustment adjustmenta = new Adjustment();
		adjustmenta.setReportNo(endCaseVo.getRegistNo());
		adjustmenta.setClaimNo(endCaseVo.getRegistNo()+"_"+endCaseVo.getPolicyNo());
		adjustmenta.setAdjustmentCode(endCaseVo.getCompensateNo());
		adjustmenta.setUnderWriteDesc("同意赔付");
		adjustmenta.setClaimAmount(NullToZero(compensateVo.getSumAmt()).toString());
		if(compensateVo.getUnderwriteDate()!=null){
			adjustmenta.setUnderWriteEndTime(DateUtils.dateToStr(compensateVo.getUnderwriteDate(), DateUtils.YToSec));
		}
		if(compensateVo.getSumAmt()==null || compensateVo.getSumAmt().compareTo(BigDecimal.ZERO)==0){
			adjustmenta.setPaymentTypeCode("7");
		}else{
			adjustmenta.setPaymentTypeCode("1");
		}
		adjustmenta.setAtfaultTypeCode(decode("indemnityDuty", compensateVo.getIndemnityDuty()));
		if(compensateVo.getIndemnityDutyRate()!=null){
			adjustmenta.setAtfaultProportion(compensateVo.getIndemnityDutyRate().toString());
		}
		adjustmenta.setCurrencyCode("CNY");
		adjustmenta.setTaskStatus("理算完成");
		adjustmenta.setDepositedAmt(NullToZero(compensateVo.getSumPreAmt()).toString());
		adjustmenta.setTotalFeePaymentAmt(NullToZero(compensateVo.getSumFee()).toString());
		adjustmenta.setTotalFeePrepayAmt(NullToZero(compensateVo.getSumPreFee()).toString());
		adjustmenta.setTotalLossPaymentAmt((NullToZero(compensateVo.getSumAmt()).add(NullToZero(compensateVo.getSumFee()))).toString());
		adjustmenta.setAccumullossPrepaymentAmt(NullToZero(compensateVo.getSumPreAmt()).toString());
		adjustmenta.setTotalPaymentAmt((NullToZero(compensateVo.getSumAmt()).add(NullToZero(compensateVo.getSumFee()))).toString());
		
		BigDecimal sumPayByInsured = BigDecimal.ZERO;
		List<String> paymentList = new ArrayList<String>();
		if(compensateVo.getPrpLPayments()!=null && compensateVo.getPrpLPayments().size()>0){
			for(PrpLPaymentVo vo:compensateVo.getPrpLPayments()){
				PrpLPayCustomVo customVo = payCustomService.findPayCustomVoById(vo.getPayeeId());
				if("2".equals(customVo.getPayObjectKind())){
					sumPayByInsured = sumPayByInsured.add(NullToZero(vo.getSumRealPay()));
					paymentList.add("2");
				}else{
					paymentList.add("1");
				}
			}
		}
		if(compensateVo.getPrpLCharges()!=null && compensateVo.getPrpLCharges().size()>0){
			for(PrpLChargeVo vo:compensateVo.getPrpLCharges()){
				PrpLPayCustomVo customVo = payCustomService.findPayCustomVoById(vo.getPayeeId());
				if("2".equals(customVo.getPayObjectKind())){
					sumPayByInsured = sumPayByInsured.add(NullToZero(vo.getFeeRealAmt()));
					paymentList.add("2");
				}else{
					paymentList.add("1");
				}
			}
		}
		adjustmenta.setCurrentPaymentAmt(sumPayByInsured.toString());
		if(paymentList.contains("2") && paymentList.contains("1")){
			adjustmenta.setIndemnityPaymentType("2");
		}else if(paymentList.contains("2")){
			adjustmenta.setIndemnityPaymentType("0");
		}else if(paymentList.contains("1")){
			adjustmenta.setIndemnityPaymentType("1");
		}else{
			adjustmenta.setIndemnityPaymentType("3");
		}
		adjustmenta.setIsSubrogation("3".equals(compensateVo.getCaseType()) ? "1":"0");
		adjustmenta.setIsLawsuit(compensateVo.getLawsuitFlag());
		adjustmenta.setEndcaseTimes("1");
		adjustmenta.setPaycountStartTime(DateUtils.dateToStr(compensateVo.getCreateTime(), DateUtils.YToSec));
		adjustmenta.setEndcaseTime(DateUtils.dateToStr(endCaseVo.getEndCaseDate(), DateUtils.YToSec));
		adjustmenta.setCurrentFinalAmount(NullToZero(compensateVo.getSumAmt()).add(NullToZero(compensateVo.getSumFee())).toString());
		adjustmenta.setSignsOfFraud("1".equals(certifyVo.getIsFraud()) ? "02":"03");
		adjustmenta.setCreateBy(endCaseVo.getCreateUser());
		adjustmenta.setCreateTime(DateUtils.dateToStr(date, DateUtils.YToSec));
		adjustmenta.setUpdateBy(endCaseVo.getCreateUser());
		adjustmenta.setUpdateTime(DateUtils.dateToStr(date, DateUtils.YToSec));
		
		//理算险种信息
		List<CoverItem> coverItems = new ArrayList<CoverItem>();
		if(compensateVo.getPrpLLossItems()!=null && compensateVo.getPrpLLossItems().size()>0){
			for(PrpLLossItemVo vo:compensateVo.getPrpLLossItems()){
				CoverItem coverItem = new CoverItem();
				coverItem.setRecoveryOrPayFlag("4".equals(vo.getPayFlag()) ? "1":vo.getPayFlag());
				coverItem.setCoverageCode(decode("kindCode", vo.getKindCode()));
				coverItem.setLossFeeType("1");
				if(vo.getDutyRate()!=null){
					coverItem.setLiabilityRate(vo.getDutyRate().toString());
				}
				coverItem.setTaskStatus("P");
				coverItem.setClaimAmount(NullToZero(vo.getSumRealPay()).toString());
				coverItem.setSalvageFee(NullToZero(vo.getRescueFee()).toString());
				coverItem.setCreateBy(compensateVo.getCreateUser());
				coverItem.setCreateTime(DateUtils.dateToStr(date, DateUtils.YToSec));
				coverItem.setUpdateBy(compensateVo.getCreateUser());
				coverItem.setUpdateTime(DateUtils.dateToStr(date, DateUtils.YToSec));
				coverItems.add(coverItem);
			}
		}
		if(compensateVo.getPrpLLossProps()!=null && compensateVo.getPrpLLossProps().size()>0){
			for(PrpLLossPropVo vo:compensateVo.getPrpLLossProps()){
				CoverItem coverItem = new CoverItem();
				coverItem.setRecoveryOrPayFlag("3");
				coverItem.setCoverageCode(decode("kindCode", vo.getKindCode()));
				coverItem.setLossFeeType("2");
				if(vo.getDutyRate()!=null){
					coverItem.setLiabilityRate(vo.getDutyRate().toString());
				}
				coverItem.setTaskStatus("P");
				coverItem.setClaimAmount(NullToZero(vo.getSumRealPay()).toString());
				coverItem.setSalvageFee(NullToZero(vo.getRescueFee()).toString());
				coverItem.setCreateBy(compensateVo.getCreateUser());
				coverItem.setCreateTime(DateUtils.dateToStr(date, DateUtils.YToSec));
				coverItem.setUpdateBy(compensateVo.getCreateUser());
				coverItem.setUpdateTime(DateUtils.dateToStr(date, DateUtils.YToSec));
				coverItems.add(coverItem);
			}
		}
		if(compensateVo.getPrpLLossPersons()!=null && compensateVo.getPrpLLossPersons().size()>0){
			for(PrpLLossPersonVo vo:compensateVo.getPrpLLossPersons()){
				CoverItem coverItem = new CoverItem();
				coverItem.setRecoveryOrPayFlag("3");
				coverItem.setCoverageCode(decode("kindCode", vo.getKindCode()));
				coverItem.setLossFeeType("3");
				if(vo.getDutyRate()!=null){
					coverItem.setLiabilityRate(vo.getDutyRate().toString());
				}
				coverItem.setTaskStatus("P");
				coverItem.setClaimAmount(NullToZero(vo.getSumRealPay()).toString());
				coverItem.setCreateBy(compensateVo.getCreateUser());
				coverItem.setCreateTime(DateUtils.dateToStr(date, DateUtils.YToSec));
				coverItem.setUpdateBy(compensateVo.getCreateUser());
				coverItem.setUpdateTime(DateUtils.dateToStr(date, DateUtils.YToSec));
				coverItems.add(coverItem);
			}
		}
		if(compensateVo.getPrpLCharges()!=null && compensateVo.getPrpLCharges().size()>0){
			for(PrpLChargeVo vo:compensateVo.getPrpLCharges()){
				CoverItem coverItem = new CoverItem();
				coverItem.setRecoveryOrPayFlag("3");
				coverItem.setCoverageCode(decode("kindCode", vo.getKindCode()));
				coverItem.setLossFeeType("4");
				coverItem.setTaskStatus("P");
				coverItem.setClaimAmount(NullToZero(vo.getFeeAmt()).toString());
				coverItem.setCreateBy(compensateVo.getCreateUser());
				coverItem.setCreateTime(DateUtils.dateToStr(date, DateUtils.YToSec));
				coverItem.setUpdateBy(compensateVo.getCreateUser());
				coverItem.setUpdateTime(DateUtils.dateToStr(date, DateUtils.YToSec));
				coverItems.add(coverItem);
			}
		}
		adjustmenta.setCoverItems(coverItems);
		adjustmentas.add(adjustmenta);
		entities.setAdjustments(adjustmentas);
		
		//结案信息
		CloseCase closeCase = new CloseCase();
		closeCase.setReportNo(endCaseVo.getRegistNo());
		closeCase.setClaimNo(endCaseVo.getRegistNo()+"_"+endCaseVo.getPolicyNo());
		closeCase.setClaimAmount(NullToZero(compensateVo.getSumAmt()).toString());
		closeCase.setClaimCloseTime(DateUtils.dateToStr(endCaseVo.getEndCaseDate(), DateUtils.YToSec));
		closeCase.setAccidentType(decode("damageCode", registVo.getDamageCode()));
		closeCase.setOtherFee(NullToZero(compensateVo.getSumFee()).toString());
		if("1101".equals(endCaseVo.getRiskCode())){
			closeCase.setRevokecaseInd("1".equals(certifyVo.getIsJQFraud()) ? "2":"Z");
		}else{
			closeCase.setRevokecaseInd("1".equals(certifyVo.getIsSYFraud()) ? "2":"Z");
		}
		closeCase.setEndcaseBy(endCaseVo.getCreateUser());
		closeCase.setEndcaseDepartmentCode(compensateVo.getComCode());
		closeCase.setCreateBy(endCaseVo.getCreateUser());
		closeCase.setCreateTime(DateUtils.dateToStr(date, DateUtils.YToSec));
		closeCase.setUpdateBy(endCaseVo.getCreateUser());
		closeCase.setUpdateTime(DateUtils.dateToStr(date, DateUtils.YToSec));
		closeCases.add(closeCase);
		entities.setCloseCases(closeCases);
		
		//赔款支付信息
		ClaimPay claimPay = new ClaimPay();
		claimPay.setReportNo(endCaseVo.getRegistNo());
		claimPay.setClaimNo(endCaseVo.getRegistNo()+"_"+endCaseVo.getPolicyNo());
		claimPay.setPayAmount(NullToZero(compensateVo.getSumPaidAmt()).add(NullToZero(compensateVo.getSumPaidFee())).toString());
		claimPay.setPaymentType("2");
		claimPay.setCreateBy(endCaseVo.getCreateUser());
		claimPay.setCreateTime(DateUtils.dateToStr(date, DateUtils.YToSec));
		claimPay.setUpdateBy(endCaseVo.getCreateUser());
		claimPay.setUpdateTime(DateUtils.dateToStr(date, DateUtils.YToSec));
		List<PayItem> payItems = new ArrayList<PayItem>();
		if(compensateVo.getPrpLPayments()!=null && compensateVo.getPrpLPayments().size()>0){
			for(PrpLPaymentVo vo:compensateVo.getPrpLPayments()){
				PayItem payItem = new PayItem();
				PrpLPayCustomVo customVo = payCustomService.findPayCustomVoById(vo.getPayeeId());
				if(customVo==null || customVo.getId()==null){
					continue;
				}
				payItem.setSerialNo(vo.getSerialNo());
				payItem.setAccountNumber(customVo.getAccountNo());
				payItem.setBankName(customVo.getBankOutlets());
				payItem.setAccountName(customVo.getPayeeName());
				payItem.setCentiCode(customVo.getCertifyNo());
				payItem.setPayAmount(NullToZero(vo.getSumRealPay()).toString());
				if(vo.getPayTime()!=null){
					payItem.setPayDate(DateUtils.dateToStr(vo.getPayTime(), DateUtils.YToSec));
				}
				payItem.setCreateBy(endCaseVo.getCreateUser());
				payItem.setCreateTime(DateUtils.dateToStr(date, DateUtils.YToSec));
				payItem.setUpdateBy(endCaseVo.getCreateUser());
				payItem.setUpdateTime(DateUtils.dateToStr(date, DateUtils.YToSec));
				payItems.add(payItem);
			}
		}
		if(compensateVo.getPrpLCharges()!=null && compensateVo.getPrpLCharges().size()>0){
			for(PrpLChargeVo vo:compensateVo.getPrpLCharges()){
				PayItem payItem = new PayItem();
				PrpLPayCustomVo customVo = payCustomService.findPayCustomVoById(vo.getPayeeId());
				if(customVo==null || customVo.getId()==null){
					continue;
				}
				payItem.setSerialNo(vo.getSerialNo());
				payItem.setAccountNumber(customVo.getAccountNo());
				payItem.setBankName(customVo.getBankOutlets());
				payItem.setAccountName(customVo.getPayeeName());
				payItem.setCentiCode(customVo.getCertifyNo());
				payItem.setPayAmount(NullToZero(vo.getFeeRealAmt()).toString());
				if(vo.getPayTime()!=null){
					payItem.setPayDate(DateUtils.dateToStr(vo.getPayTime(), DateUtils.YToSec));
				}
				payItem.setCreateBy(endCaseVo.getCreateUser());
				payItem.setCreateTime(DateUtils.dateToStr(date, DateUtils.YToSec));
				payItem.setUpdateBy(endCaseVo.getCreateUser());
				payItem.setUpdateTime(DateUtils.dateToStr(date, DateUtils.YToSec));
				payItems.add(payItem);
			}
		}
		claimPay.setPayItems(payItems);
		claimPays.setClaimPay(claimPay);
		
		//垫付信息
		if(padPayVo!=null && "1".equals(padPayVo.getUnderwriteFlag())){
			AdvancedPay advancedPay = new AdvancedPay();
			List<PrpLPadPayPersonVo> personList = padPayVo.getPrpLPadPayPersons();
			
			advancedPay.setReportNo(endCaseVo.getRegistNo());
			advancedPay.setClaimNo(endCaseVo.getRegistNo()+"_"+endCaseVo.getPolicyNo());
			advancedPay.setLossTime(DateUtils.dateToStr(registVo.getDamageTime(), DateUtils.YToSec));
			for(PrpLCheckCarVo carVo:checkVo.getPrpLCheckTask().getPrpLCheckCars()){
				if("1".equals(carVo.getSerialNo())){
					advancedPay.setDriverName(carVo.getPrpLCheckDriver().getDriverName());
				}
			}
			advancedPay.setDutyInd(checkDutyVo.getCiDutyFlag());
			advancedPay.setPointConstableTel(padPayVo.getPolicePhone());
			advancedPay.setCreateBy(endCaseVo.getCreateUser());
			advancedPay.setCreateTime(DateUtils.dateToStr(date, DateUtils.YToSec));
			advancedPay.setUpdateBy(endCaseVo.getCreateUser());
			advancedPay.setUpdateTime(DateUtils.dateToStr(date, DateUtils.YToSec));
			if(personList!=null && personList.size()>0){
				List<AdvancedPayDetail> advancedPayDetails = new ArrayList<AdvancedPayDetail>();
				advancedPay.setAdvancedPayNumber(personList.size()+"");
				Integer i = 1;
				BigDecimal sumFee = BigDecimal.ZERO;
				for(PrpLPadPayPersonVo vo:personList){
					PrpLPayCustomVo customVo = payCustomService.findPayCustomVoById(vo.getPayeeId());
					AdvancedPayDetail advancedPayDetail = new AdvancedPayDetail();
					advancedPayDetail.setSerialNo(i.toString());
					advancedPayDetail.setInjuredName(vo.getPersonName());
					advancedPayDetail.setHospitalName(vo.getHospitalName());
					advancedPayDetail.setHospitalAccounts(customVo.getAccountNo());
					advancedPayDetail.setBankName(customVo.getBankOutlets());
					advancedPayDetail.setAdvancedPayAmt(NullToZero(vo.getCostSum()).toString());
					advancedPayDetail.setCreateBy(padPayVo.getCreateUser());
					advancedPayDetail.setCreateTime(DateUtils.dateToStr(padPayVo.getCreateTime(), DateUtils.YToSec));
					advancedPayDetail.setUpdateBy(padPayVo.getUpdateUser());
					advancedPayDetail.setUpdateTime(DateUtils.dateToStr(padPayVo.getUpdateTime(), DateUtils.YToSec));
					advancedPayDetails.add(advancedPayDetail);
					i++;
					sumFee = sumFee.add(NullToZero(vo.getCostSum()));
				}
				advancedPay.setAdvancedPayDetails(advancedPayDetails);
				advancedPay.setAdvancedPayAmt(sumFee.toString());
			}
			advancedPays.add(advancedPay);
			claimPays.setAdvancedPays(advancedPays);
		}
		entities.setClaimPays(claimPays);
		endCasePacket.setEntities(entities);
		body.setDataPacket(endCasePacket);
		soapenvVo.setBody(body);
		/*组织数据结束*/
		
		XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
		stream.autodetectAnnotations(true);
		stream.setMode(XStream.NO_REFERENCES);
		stream.aliasSystemAttribute(null,"class");// 去掉 class属性
		
		//发送报文
		String requestXML = stream.toXML(soapenvVo);
		String responseXML = "";
		String url = SpringProperties.getProperty("GENILEX_URL");
		logger.info("结案提交请求精励联讯发送报文---------------------------"+requestXML);
		ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
		try{
			responseXML = CarchildUtil.requestPlatform(requestXML,url,"");
			logger.info("结案提交请求精励联讯返回报文---------------------------"+responseXML);
			soapEnvelopeVo = ClaimBaseCoder.xmlToObj(responseXML, SoapEnvelopeVo.class);
			if("C".equals(soapEnvelopeVo.getBodyVo().getDataPacketVo().getResponseSummaryVo().getOverallStatus())){
	             logVo.setStatus("1");
	             logVo.setErrorCode("true");
	         }else {
	             logVo.setStatus("0");
	             logVo.setErrorCode("false");
	         }
	         logVo.setErrorMessage(soapEnvelopeVo.getBodyVo().getDataPacketVo().getResponseSummaryVo().getOverallMessage());
		}catch (Exception e) {
			logger.info("---------------------------结案提交请求精励报错"+e.getMessage());
			e.printStackTrace();
			logVo.setStatus("0");
            logVo.setErrorCode("false");
            logVo.setErrorMessage(e.getMessage());
		}finally{
			logVo.setBusinessType(BusinessType.GENILEX_EndCase.name());
	        logVo.setBusinessName(BusinessType.GENILEX_EndCase.getName());
	        logVo.setRegistNo(registVo.getRegistNo());
            logVo.setOperateNode(FlowNode.EndCas.name());
            logVo.setComCode(compensateVo.getComCode());
            logVo.setRequestTime(date);
            logVo.setRequestUrl(url);
            logVo.setCreateUser(checkVo.getCreateUser());
            logVo.setCreateTime(date);
            logVo.setRequestXml(requestXML);
            logVo.setResponseXml(responseXML);
            logVo.setClaimNo(endCaseVo.getClaimNo());
            logVo.setCompensateNo(compensateVo.getCompensateNo());
            logVo.setRemark(taskId);
            interfaceLogService.save(logVo);
		}
	}
	
	/**
	 * 判断是否异地出险
	 * @return
	 */
	public Boolean unLocal(List<PrpLCMainVo> prpLCMainVoList , PrpLRegistExtVo prpLRegistExtVo){
		Boolean unLocal = false;
		String code = areaDictService.findAreaList("areaCode",prpLRegistExtVo.getCheckAddressCode());
		String comCode = "";
        if(prpLCMainVoList.size()==2){
            for(PrpLCMainVo cMainVo:prpLCMainVoList){
                if(("12").equals(cMainVo.getRiskCode().substring(0, 2))){
                    comCode = cMainVo.getComCode();
                }
            }
        }else{
            comCode = prpLCMainVoList.get(0).getComCode();
        }
        if(comCode!=null && code!=null){
        	if("0002".equals(code.substring(0, 4))){//深圳
                if(!code.substring(0, 4).equals(comCode.substring(0, 4))){
                	unLocal = true;//是
                }else{
                	unLocal = false;//否
                }
            }else{
                if("0002".equals(comCode.substring(0, 4))){//添加深圳的单这种情况
                    if(!code.substring(0, 4).equals(comCode.substring(0, 4))){
                    	unLocal = true;//是
                    }else{
                    	unLocal = false;//否
                    }
                }else{
                    if(!code.substring(0, 2).equals(comCode.substring(0, 2))){
                    	unLocal = true;//是
                    }else{
                    	unLocal = false;//否
                    }
                }
            }
        }else{
        	unLocal = true;
        }
		
		return unLocal;
	}
	
	private String decode(String type,String value){
		
		if("damageCode".equals(type)){
			if("DM01".equals(value)){
				value = "01";
			}else if("DM02".equals(value)){
				value = "03";
			}else if("DM03".equals(value)){
				value = "07";
			}else if("DM04".equals(value)){
				value = "05";
			}else if("DM05".equals(value)){
				value = "09";
			}else if("DM51".equals(value)){
				value = "02";
			}else if("DM06".equals(value)){
				value = "08";
			}else if("DM07".equals(value)){
				value = "08";
			}else if("DM08".equals(value)){
				value = "06";
			}else if("DM09".equals(value)){
				value = "13";
			}else if("DM10".equals(value)){
				value = "06";
			}else if("DM12".equals(value)){
				value = "10";
			}else{
				value = "99";
			}
		}else if("indemnityDuty".equals(type)){
			if("0".equals(value)){
				value = "01";
			}else if("1".equals(value)){
				value = "02";
			}else if("2".equals(value)){
				value = "03";
			}else if("3".equals(value)){
				value = "04";
			}else if("4".equals(value)){
				value = "05";
			}
		}else if("kindCode".equals(type)){
			if("BZ".equals(value)){
				value = "0360-0357";
			}else if("A".equals(value)){
				value = "0370-030006";
			}else if("A1".equals(value)){
				value = "0370-030006";
			}else if("G".equals(value)){
				value = "0370-033005";
			}else if("B".equals(value)){
				value = "0370-030018";
			}else if("D11".equals(value)){
				value = "0370-033016";
			}else if("D12".equals(value)){
				value = "0370-033017";
			}else if("D2".equals(value)){
				value = "0370-030058";
			}else if("NZ".equals(value)){
				value = "0370-030058";
			}else if("L".equals(value)){
				value = "0370-030025";
			}else if("Z".equals(value) || "Z".equals(value)){
				value = "0370-030012";
			}else if("F".equals(value)){
				value = "0370-033009";
			}else if("X".equals(value) || "X3".equals(value)){
				value = "0370-030021";
			}else if("T".equals(value)){
				value = "0370-030042";
			}else if("RF".equals(value)){
				value = "0384-030112";
			}else if("X1".equals(value) || "X2".equals(value)){
				value = "0370-033007";
			}else if("K1".equals(value)){
				value = "0384-030117";
			}else if("K2".equals(value)){
				value = "0384-030118";
			}else if("Z3".equals(value)){
				value = "0384-030116";
			}else if("R".equals(value) || "SS".equals(value)){
				value = "0370-030015";
			}else if("M".equals(value)){
				value = "0370-033008";
			}else if("Y".equals(value)){
				value = "0311-030034";
			}else if("NT".equals(value)){
				value = "0384-030115";
			}
		}
		
		return value;
	}
	
	private static BigDecimal NullToZero(BigDecimal strNum) {
		return strNum==null ? new BigDecimal("0") : strNum;
	}
	
	@Override
	public String uploadGenilex(String[] registNoArray,SysUserVo userVo){
		StringBuffer strBuf = new StringBuffer();
		List<String> arrayType = new ArrayList<String>();
		arrayType.add(BusinessType.GENILEX_Regist.name());
		arrayType.add(BusinessType.GENILEX_Check.name());
		arrayType.add(BusinessType.GENILEX_Dloss.name());
		arrayType.add(BusinessType.GENILEX_EndCase.name());
		
		for(String registNo:registNoArray){
			try{
				registNo = registNo.replaceAll("\\s*","");
				Map<String,List<ClaimInterfaceLogVo>> map = interfaceLogService.searchByRegistNoAndType(registNo, arrayType);
				if(map.size()>0){
					//补传报案
					if(map.containsKey(BusinessType.GENILEX_Regist.name())){
						List<PrpLCMainVo> prpLCMainVoList = policyViewService.getPolicyAllInfo(registNo);
						PrpLRegistVo registVo = registService.findByRegistNo(registNo);
						for(ClaimInterfaceLogVo logVo:map.get(BusinessType.GENILEX_Regist.name())){
							if(prpLCMainVoList!=null && prpLCMainVoList.size()>0){
								reportInfoService.organizaForReport(registVo, userVo, prpLCMainVoList.get(0));
								interfaceLogService.changeInterfaceLog(logVo.getId());
							}
						}
					}
					//补传查勘
					if(map.containsKey(BusinessType.GENILEX_Check.name())){
						PrpLCheckVo checkVo = checkTaskService.findCheckVoByRegistNo(registNo);
						for(ClaimInterfaceLogVo logVo:map.get(BusinessType.GENILEX_Check.name())){
							this.checkToGenilex(checkVo, Long.valueOf(logVo.getCompensateNo()));
							interfaceLogService.changeInterfaceLog(logVo.getId());
						}
					}
					//补传定损
					if(map.containsKey(BusinessType.GENILEX_Dloss.name())){
						for(ClaimInterfaceLogVo logVo:map.get(BusinessType.GENILEX_Dloss.name())){
							claimJxService.sendDlossInfor(logVo.getRegistNo(),logVo.getFlag(), userVo);
							interfaceLogService.changeInterfaceLog(logVo.getId());
						}
					}
					//补传结案
					if(map.containsKey(BusinessType.GENILEX_EndCase.name())){
						for(ClaimInterfaceLogVo logVo:map.get(BusinessType.GENILEX_EndCase.name())){
							PrpLEndCaseVo endCaseVo = endCaseService.queryEndCaseVo(logVo.getRegistNo(), logVo.getClaimNo());
				        	PrpLCompensateVo compensateVo = compesateService.findCompByPK(logVo.getCompensateNo());
				        	this.endCaseToGenilex(endCaseVo, compensateVo,logVo.getRemark());
				        	interfaceLogService.changeInterfaceLog(logVo.getId());
						}
					}
					strBuf.append(registNo+" : 补传成功！\n");
				}else{
					strBuf.append(registNo+" : 查询不到失败的记录！\n");
				}
			}catch(Exception e){
				e.printStackTrace();
				logger.error("精励联讯批量补送失败========================报案号："+registNo,e.getMessage(),e);
				strBuf.append(registNo+" : 补传失败！\n");
			}
			strBuf.append("\n");
		}
		
		return strBuf.toString();
	}
	
//	public static void main(String args[]){
//		PrpLCheckVo checkVo = checkTaskService.findCheckVoByRegistNo("4110100201812060000193");
//		Long flowTaskId = Long.valueOf("135850");
//		this.checkToGenilex(checkVo, flowTaskId);
//	}
	
}
