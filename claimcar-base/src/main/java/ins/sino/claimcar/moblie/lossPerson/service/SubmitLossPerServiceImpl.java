package ins.sino.claimcar.moblie.lossPerson.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.dubbo.common.utils.Assert;
import com.sinosoft.api.service.ServiceInterface;

import ins.framework.common.DateTime;
import ins.framework.exception.BusinessException;
import ins.framework.lang.Springs;
import ins.framework.utils.Beans;
import ins.platform.common.service.facade.CodeTranService;
import ins.platform.common.web.util.WebUserUtils;
import ins.platform.utils.ClaimBaseCoder;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.ValidFlag;
import ins.sino.claimcar.certify.service.CertifyIlogService;
import ins.sino.claimcar.certify.service.CertifyService;
import ins.sino.claimcar.check.vo.PrpLCheckDutyVo;
import ins.sino.claimcar.claim.service.ClaimTaskService;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.claim.service.InterfaceAsyncService;
import ins.sino.claimcar.common.claimtext.service.ClaimTextService;
import ins.sino.claimcar.common.claimtext.vo.PrpLClaimTextVo;
import ins.sino.claimcar.common.losscharge.service.LossChargeService;
import ins.sino.claimcar.common.losscharge.vo.PrpLDlossChargeVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.ilog.rule.service.IlogRuleService;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.lossperson.vo.SubmitNextVo;
import ins.sino.claimcar.lossperson.service.PersTraceHandleService;
import ins.sino.claimcar.lossperson.service.PersTraceService;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersExtVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersHospitalVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersInjuredVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersNurseVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersRaiseVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceFeeVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceMainVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceVo;
import ins.sino.claimcar.lossprop.service.PropTaskService;
import ins.sino.claimcar.manager.service.ManagerService;
import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckHead;
import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckResponseHead;
import ins.sino.claimcar.moblie.lossPerson.vo.CertainsLossPerTaskRequestVo;
import ins.sino.claimcar.moblie.lossPerson.vo.FeeInfo;
import ins.sino.claimcar.moblie.lossPerson.vo.FeeInfoVo;
import ins.sino.claimcar.moblie.lossPerson.vo.InPantientInfoVo;
import ins.sino.claimcar.moblie.lossPerson.vo.NurseInfoVo;
import ins.sino.claimcar.moblie.lossPerson.vo.PartInfoVo;
import ins.sino.claimcar.moblie.lossPerson.vo.PersonInfoVos;
import ins.sino.claimcar.moblie.lossPerson.vo.RaiseInfoVo;
import ins.sino.claimcar.moblie.lossPerson.vo.SubmitLossPerRequest;
import ins.sino.claimcar.moblie.lossPerson.vo.SubmitLossPerRespond;
import ins.sino.claimcar.moblie.lossPerson.vo.TraceInfoVo;
import ins.sino.claimcar.platform.service.CertifyToPaltformService;
import ins.sino.claimcar.platform.service.LossToPlatformService;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.ScheduleTaskService;
import ins.sino.claimcar.verifyclaim.service.VerifyClaimService;

public class SubmitLossPerServiceImpl implements ServiceInterface{
	private static Logger logger = LoggerFactory.getLogger(SubmitLossPerServiceImpl.class);
	
	@Autowired
	WfTaskHandleService wfTaskHandleService;
	@Autowired
	private PersTraceHandleService persTraceHandleService;
	@Autowired
	private PersTraceService persTraceService;
	@Autowired
	private ClaimTextService claimTextSerVice;
	@Autowired
	private LossChargeService lossChargeService;
	@Autowired
	LossToPlatformService lossToPlatformService;
	@Autowired
	ClaimTaskService claimTaskService;
	@Autowired
	InterfaceAsyncService interfaceAsyncService;
	@Autowired
    CertifyIlogService certifyIlogService;
	@Autowired
	CertifyToPaltformService certifyToPaltformService;
	@Autowired
    CompensateTaskService compensateTaskService;
    @Autowired
    VerifyClaimService verifyClaimService;
    @Autowired
    PolicyViewService policyViewService;
    @Autowired
    CertifyService certifyService;
    @Autowired
    CodeTranService codeTranService;
    @Autowired
    ScheduleTaskService scheduleTaskService;
    @Autowired
    private IlogRuleService ilogRuleService;
    
    @Autowired
	private PropTaskService propTaskService;
    
    @Autowired
    private ManagerService managerService;
	@Override
	public Object service(String arg0, Object arg1) {
		init();
		SubmitLossPerRespond resPacket = new SubmitLossPerRespond();
		MobileCheckResponseHead resHead = new MobileCheckResponseHead();
		try {
			String reqXml = ClaimBaseCoder.objToXmlUtf(arg1);
			logger.info("移动端人伤跟踪接口请求报文：{}" ,reqXml);
			SubmitLossPerRequest reqPacket = (SubmitLossPerRequest) arg1;
			Assert.notNull(reqPacket, " 请求信息为空  ");
			MobileCheckHead head = reqPacket.getHead();
			if (!"022".equals(head.getRequestType()) || !"claim_user".equals(head.getUser()) || !"claim_psd".equals(head.getPassWord())) {
				throw new IllegalArgumentException(" 请求头参数错误  ");
			}

			CertainsLossPerTaskRequestVo certainsTaskInfoVo = reqPacket.getBody().getCertainsTaskRequestVo();
			List<PersonInfoVos> personInfos = reqPacket.getBody().getPersonInfoVo();
			List<FeeInfoVo> feeInfos = reqPacket.getBody().getFeeInfos();

			// 人伤跟踪任务主信息
			PrpLDlossPersTraceMainVo prpLDlossPersTraceMainVo = new PrpLDlossPersTraceMainVo();

			PrpLClaimTextVo claimTextVo = new PrpLClaimTextVo();
			PrpLCheckDutyVo prpLCheckDutyVo = new PrpLCheckDutyVo();

			// 人伤跟踪信息
			List<PrpLDlossPersTraceVo> prpLDlossPersTraces = new ArrayList<PrpLDlossPersTraceVo>();
			// 费用明细
			List<PrpLDlossChargeVo> prpLDlossCharges = new ArrayList<PrpLDlossChargeVo>();

			SysUserVo userVo = new SysUserVo();
			
			//对报文进行非空校验
			checkRequest(certainsTaskInfoVo);
			
			// 封装数据
			logger.info("移动端人伤跟踪提交接口封装数据开始:========================>");
			organizeLossPersData(prpLDlossPersTraceMainVo, prpLDlossPersTraces,
					prpLDlossCharges, userVo, certainsTaskInfoVo, personInfos,
					feeInfos);

			String auditStatus = "";
			
			
			// 暂存人伤信息(移动端由于 提交/暂存 共用一个接口，所以每一次请求都要先 保存/更新 人伤信息)
			if (prpLDlossPersTraces.size() > 0) {
				for (PrpLDlossPersTraceVo persTraceVo : prpLDlossPersTraces) {
					persTraceHandleService.saveCasualtyInfo(persTraceVo, userVo);
				}
			}
			
			//判断提交类型
			if("2".equals(certainsTaskInfoVo.getOptionType())) {
				//如果是暂存，则可以结束
				resHead.setResponseType("022");
				resHead.setResponseCode("YES");
				resHead.setResponseMessage("Success");
			} else {
				//如果是提交，则先判断该节点是否已经提交过，避免重复提交
				PrpLWfTaskVo taskVo = wfTaskHandleService.findTaskIn(Double.parseDouble(certainsTaskInfoVo.getTaskId()));
				if(taskVo==null){
					throw new Exception("该任务已经提交，不能继续处理！");
				}

				// 校验查勘是否完成
				if(!"2".equals(certainsTaskInfoVo.getOptionType())) {
					
					List<PrpLWfTaskVo> wfChkTaskVoList = wfTaskHandleService.findEndTask(certainsTaskInfoVo.getRegistNo(),null,FlowNode.Chk);
					if(wfChkTaskVoList==null||wfChkTaskVoList.size()==0){
						throw new RuntimeException("查勘未完成，不能提交下一环节");
					}
				}
				
				//封装submitNextVo
				SubmitNextVo nextVo = new SubmitNextVo();
				
				if ("4".equals(certainsTaskInfoVo.getOptionType())) {
					auditStatus = CodeConstants.AuditStatus.SUBPLNEXT;
					nextVo.setIsMobileAccept(CodeConstants.YN01.Y);
				} else if ("5".equals(certainsTaskInfoVo.getOptionType())) {
					auditStatus = CodeConstants.AuditStatus.SUBPLVERIFY;
				} else if ("6".equals(certainsTaskInfoVo.getOptionType())) {
					auditStatus = CodeConstants.AuditStatus.SUBPLCHARGE;
				}
				
				claimTextVo.setRemark(certainsTaskInfoVo.getTrackOpinions());
				
				prpLCheckDutyVo.setRegistNo(certainsTaskInfoVo.getRegistNo());
				prpLCheckDutyVo.setCiDutyFlag(certainsTaskInfoVo.getJqIndemnityDuty());
				prpLCheckDutyVo.setIndemnityDuty(certainsTaskInfoVo.getIndemnityDuty());
				prpLCheckDutyVo.setIndemnityDutyRate(NullToZero(certainsTaskInfoVo.getIndemnityDutyRate()));
				prpLCheckDutyVo.setSerialNo(1);
				
				String currentNode = certainsTaskInfoVo.getNodeType();
				
				String majorcaseFlag = certainsTaskInfoVo.getIsBigCase();
				String caseProcessType = null;
				String flowTaskId = certainsTaskInfoVo.getTaskId();
				String isDeroFlag = certainsTaskInfoVo.getIsDerogationis();
				BigDecimal sumChargeFee = new BigDecimal(0);
				
				if(prpLDlossCharges.size()>0){
					for(PrpLDlossChargeVo charge:prpLDlossCharges){
						sumChargeFee = sumChargeFee.add(charge.getChargeFee());
					}
				}
				
				nextVo.setFlowTaskId(certainsTaskInfoVo.getTaskId());
				nextVo.setAuditStatus(auditStatus);
				// nextVo.setCurrentName(currentName);
				nextVo.setCurrentNode(currentNode);
				nextVo.setComCode(certainsTaskInfoVo.getScheduleObjectId());
				
				Date appointmentTime = null;
				String isAppointTime = "N";// 默认不显示预约时间
				Date PLFirstTime = null;
				int[] ArrDays = { 15, 30, 60, 90, 180, 360 };
				
//				prpLDlossPersTraceMainVo.setCaseProcessType(caseProcessType);
				prpLDlossPersTraceMainVo.setMajorcaseFlag(majorcaseFlag);
				prpLDlossPersTraceMainVo.setIsDeroFlag(isDeroFlag);
				prpLDlossPersTraceMainVo.setSumChargeFee(sumChargeFee);
				
				if (prpLDlossPersTraces != null && prpLDlossPersTraces.size() > 0) {
					for (PrpLDlossPersTraceVo persTraceVo : prpLDlossPersTraces) {
						if (ValidFlag.VALID.equals(persTraceVo.getValidFlag())
								&& ValidFlag.INVALID.equals(persTraceVo
										.getEndFlag())) {// 有效的且没有跟踪完毕显示预约时间
							isAppointTime = "Y";
						}
					}
				}
				
				int traceTimes = prpLDlossPersTraceMainVo.getTraceTimes() == null ? 0
						: prpLDlossPersTraceMainVo.getTraceTimes().intValue();
				if (FlowNode.PLFirst.name().equalsIgnoreCase(
						certainsTaskInfoVo.getNodeType())) {// 首次跟踪
					PLFirstTime = new Date();
					appointmentTime = new DateTime(PLFirstTime).addDay(ArrDays[0]);
				} else if (traceTimes < 6) {
					PLFirstTime = prpLDlossPersTraceMainVo.getPlfSubTime();
					appointmentTime = new DateTime(PLFirstTime)
							.addDay(ArrDays[traceTimes]);
				} else {
					PLFirstTime = new Date();
					appointmentTime = new DateTime(PLFirstTime).addDay(ArrDays[4]);
				}
				
				// 判断是否总公司有做过审核操作
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("registNo", prpLDlossPersTraceMainVo.getRegistNo());
				if (flowTaskId == null) {
					params.put("taskId", BigDecimal.ZERO.doubleValue());
				} else {
					params.put("taskId", Double.valueOf(flowTaskId));
				}
				String existHeadOffice = managerService.isSubmitHeadOffice(params);
				
				logger.info("移动端人伤跟踪提交接口组织提交信息开始:========================>");
				nextVo = persTraceHandleService.organizNextVo(
						prpLDlossPersTraceMainVo, nextVo, userVo, caseProcessType,
						existHeadOffice);
				
				//nextVo.setAssignCom(WebUserUtils.getComCode());
				nextVo.setAssignCom(certainsTaskInfoVo.getScheduleObjectId());
				boolean existPLBigTask = wfTaskHandleService.existTaskByNodeCode(
						prpLDlossPersTraceMainVo.getRegistNo(), FlowNode.PLBig,
						prpLDlossPersTraceMainVo.getId().toString(), "");
				if ("1".equals(majorcaseFlag) && !existPLBigTask) {// 发起大案审核
					nextVo.setOtherNodes(FlowNode.PLBig_LV1.name());
					nextVo.setOtherNodesName(FlowNode.PLBig_LV1.getName());
					nextVo.setOtherAssignUser("");
				}
				
				logger.info("移动端人伤跟踪提交接口提交人伤开始:========================>");	
				persTraceHandleService.saveOrSubmitPLEdit(prpLDlossPersTraceMainVo,
						prpLCheckDutyVo, prpLDlossCharges, claimTextVo, nextVo,
						userVo);
				
				resHead.setResponseType("022");
				resHead.setResponseCode("YES");
				resHead.setResponseMessage("Success");
			}
		} catch (Exception e) {
			resHead.setResponseType("022");
			resHead.setResponseCode("NO");
			resHead.setResponseMessage(e.getMessage());
			logger.info("移动端人伤跟踪提交接口报错信息： " ,e);
			e.printStackTrace();
		}

		resPacket.setHead(resHead);
		String resXml = ClaimBaseCoder.objToXmlUtf(resPacket);
		logger.info("=================移动端人伤跟踪提交接口返回报文：" + resXml);
		return resPacket;
	}
	
	//封装移动端传递的数据
	public void organizeLossPersData(PrpLDlossPersTraceMainVo prpLDlossPersTraceMainVo, 
			List<PrpLDlossPersTraceVo> prpLDlossPersTraces,
			List<PrpLDlossChargeVo> prpLDlossCharges, SysUserVo userVo,
			CertainsLossPerTaskRequestVo certainsTaskInfoVo,
			List<PersonInfoVos> personInfos, 
			List<FeeInfoVo> feeInfos)
			throws Exception {
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		userVo.setUserCode(certainsTaskInfoVo.getNextHandlerCode());
		userVo.setUserName(certainsTaskInfoVo.getNextHandlerName());
		userVo.setComCode(certainsTaskInfoVo.getScheduleObjectId());
		userVo.setComName(certainsTaskInfoVo.getScheduleObjectName());
		
		PrpLDlossPersTraceMainVo oldPrpLDlossPersTraceMainVo = persTraceService.findPersTraceMainVobyId(Long.decode(certainsTaskInfoVo.getCertainsId()));
		Beans.copy().from(oldPrpLDlossPersTraceMainVo).to(prpLDlossPersTraceMainVo);
		
		if(prpLDlossPersTraceMainVo.getId() == null) {
			prpLDlossPersTraceMainVo.setId(Long.decode(certainsTaskInfoVo.getCertainsId()));
		}
		prpLDlossPersTraceMainVo.setRegistNo(certainsTaskInfoVo.getRegistNo());
		prpLDlossPersTraceMainVo.setCaseProcessType(certainsTaskInfoVo.getHandlerType());
		prpLDlossPersTraceMainVo.setIsMinorInjuryCases(certainsTaskInfoVo.getIsSmallCase());
		prpLDlossPersTraceMainVo.setIsDeroFlag(certainsTaskInfoVo.getIsDerogationis());
		prpLDlossPersTraceMainVo.setPlfName(certainsTaskInfoVo.getTracer());
		prpLDlossPersTraceMainVo.setPlfCertiCode(certainsTaskInfoVo.getTracerIdentifyNo());
		prpLDlossPersTraceMainVo.setIsDeroAmout(NullToZero(certainsTaskInfoVo.getDerogationisAmount()));
		prpLDlossPersTraceMainVo.setInSideDeroFlag(certainsTaskInfoVo.getInsideDerogationis());
		prpLDlossPersTraceMainVo.setIsMinorInjuryCases(certainsTaskInfoVo.getIsSmallCase());
		
		//组装人伤相关信息
		if(personInfos != null && personInfos.size() > 0){
			for (PersonInfoVos personInfo : personInfos) {
				TraceInfoVo traceInfo = personInfo.getTraceInfoVo();
				//人伤跟踪信息
				PrpLDlossPersTraceVo prpLDlossPersTraceVo = new PrpLDlossPersTraceVo();
				//人伤人员信息
				PrpLDlossPersInjuredVo prpLDlossPersInjuredVo = new PrpLDlossPersInjuredVo();
				prpLDlossPersTraceVo.setRegistNo(certainsTaskInfoVo.getRegistNo());
				prpLDlossPersTraceVo.setTraceForms(traceInfo.getTraceType());
				prpLDlossPersTraceVo.setOperatorDesc(traceInfo.getContent());
				prpLDlossPersTraceVo.setEndFlag(traceInfo.getIsClaim());
				if(personInfo.getPersonId() != null && !"".equals(personInfo.getPersonId())){
					prpLDlossPersTraceVo.setId(Long.valueOf(personInfo.getPersonId()));
					prpLDlossPersInjuredVo.setId(Long.valueOf(personInfo.getPersonId()));
				}
				//操作类型：OPERATIONTYPE -- 3，注销伤亡人员，则人伤跟踪表validflag字段改为：0
				if(StringUtils.isNotBlank(personInfo.getOperationType()) && "3".equals(personInfo.getOperationType())) {
					prpLDlossPersTraceVo.setValidFlag("0");
				}
				prpLDlossPersTraceVo.setPersonName(personInfo.getName());
				if(StringUtils.isNotBlank(certainsTaskInfoVo.getCertainsId())) {
					prpLDlossPersTraceVo.setPersTraceMainId(Long.decode(certainsTaskInfoVo.getCertainsId()));
				}else {
					throw new IllegalArgumentException("理赔定损ID为空");
				}
				
				
				prpLDlossPersInjuredVo.setRegistNo(certainsTaskInfoVo.getRegistNo());
				prpLDlossPersInjuredVo.setTreatSituation(personInfo.getCaseType());
				prpLDlossPersInjuredVo.setPersonName(personInfo.getName());
				prpLDlossPersInjuredVo.setCertiType(personInfo.getCredentialsType());
				prpLDlossPersInjuredVo.setCertiCode(personInfo.getCredentialsNo());
				prpLDlossPersInjuredVo.setPersonAge(new BigDecimal(personInfo.getAge()));
				prpLDlossPersInjuredVo.setLossItemType(personInfo.getPersonAttr());
				prpLDlossPersInjuredVo.setPersonSex(personInfo.getSex());
				prpLDlossPersInjuredVo.setTicCode(personInfo.getIndustryCode());
				prpLDlossPersInjuredVo.setTicName(personInfo.getIndustryName());
				prpLDlossPersInjuredVo.setIncome(personInfo.getIncomeType());
				prpLDlossPersInjuredVo.setDomicile(personInfo.getHukouType());
				prpLDlossPersInjuredVo.setPhoneNumber(personInfo.getPhone());
				prpLDlossPersInjuredVo.setWoundCode(personInfo.getDamageType());
				prpLDlossPersInjuredVo.setChkComName(personInfo.getIdentifyCompanyName());
				prpLDlossPersInjuredVo.setChkComCode(personInfo.getIdentifyCompanyCode());
				prpLDlossPersInjuredVo.setLicenseNo(personInfo.getLicenseNo());
				if(StringUtils.isNotBlank(certainsTaskInfoVo.getIsObject())) {
					prpLDlossPersInjuredVo.setSerialNo(Integer.getInteger(certainsTaskInfoVo.getIsObject()));
				}else {
					prpLDlossPersInjuredVo.setSerialNo(3);
				}
				
				//受伤部位
				List<PrpLDlossPersExtVo> prpLDlossPersExts = new ArrayList<PrpLDlossPersExtVo>();
				
				List<PartInfoVo> partInfoList = personInfo.getPartInfoVo();
				
				if(partInfoList != null && partInfoList.size()>0){
					for(PartInfoVo partInfo:partInfoList){
						
						PrpLDlossPersExtVo prpLDlossPersExtVo = new PrpLDlossPersExtVo();
						if(partInfo.getId() != null && !"".equals(partInfo.getId())){
						   prpLDlossPersExtVo.setId(Long.decode(partInfo.getId()));
						}
						prpLDlossPersExtVo.setRegistNo(certainsTaskInfoVo.getRegistNo());
						prpLDlossPersExtVo.setInjuredPart(partInfo.getPart());
						prpLDlossPersExtVo.setInjuredDiag(partInfo.getDiagnosis());
						prpLDlossPersExtVo.setTreatMethod(partInfo.getTreatmentType());
						prpLDlossPersExtVo.setTreatWay(partInfo.getTreatmentWay());
						prpLDlossPersExtVo.setProgSituation(partInfo.getPrognosis());
						prpLDlossPersExtVo.setWoundGrade(partInfo.getDisabilityDegree());
						prpLDlossPersExtVo.setDiagDetail(partInfo.getDetail());
						prpLDlossPersExtVo.setTrackRemark(partInfo.getRemark());
						prpLDlossPersExts.add(prpLDlossPersExtVo);
					}
					
				}
				prpLDlossPersInjuredVo.setPrpLDlossPersExts(prpLDlossPersExts);
				
				//住院信息
				List<PrpLDlossPersHospitalVo> prpLDlossPersHospitals = new ArrayList<PrpLDlossPersHospitalVo>();
				
				List<InPantientInfoVo> inPantientInfoList = personInfo.getInPantientInfoVo();
				
				if(inPantientInfoList != null && inPantientInfoList.size()>0){
					for(InPantientInfoVo inPantientInfoVo:inPantientInfoList){
						PrpLDlossPersHospitalVo prpLDlossPersHospitalVo = new PrpLDlossPersHospitalVo();
						if(inPantientInfoVo.getId() != null && !"".equals(inPantientInfoVo.getId())){
							prpLDlossPersHospitalVo.setId(Long.decode(inPantientInfoVo.getId()));
						}
						prpLDlossPersHospitalVo.setRegistNo(certainsTaskInfoVo.getRegistNo());
						if(StringUtils.isNotEmpty(inPantientInfoVo.getInTime())) {
							prpLDlossPersHospitalVo.setInHospitalDate(format.parse(inPantientInfoVo.getInTime()));
						}
						if(StringUtils.isNotEmpty(inPantientInfoVo.getOutTime())) {
							prpLDlossPersHospitalVo.setOutHospitalDate(format.parse(inPantientInfoVo.getOutTime()));
						}
						prpLDlossPersHospitalVo.setHospitalProvince(inPantientInfoVo.getProvince());
						prpLDlossPersHospitalVo.setHospitalCity(inPantientInfoVo.getCity());
						prpLDlossPersHospitalVo.setHospitalCode(inPantientInfoVo.getHospitalCode());
						prpLDlossPersHospitalVo.setHospitalName(inPantientInfoVo.getHospitalName());
						prpLDlossPersHospitalVo.setRemark(inPantientInfoVo.getRemark());
						
						prpLDlossPersHospitals.add(prpLDlossPersHospitalVo);
					}
				}
				prpLDlossPersInjuredVo.setPrpLDlossPersHospitals(prpLDlossPersHospitals);
				
				//护理信息
				List<PrpLDlossPersNurseVo> prpLDlossPersNurses = new ArrayList<PrpLDlossPersNurseVo>();
				
				List<NurseInfoVo> nurseInfoList = personInfo.getNurseInfoVo();
				if(nurseInfoList != null && nurseInfoList.size()>0){
					for(NurseInfoVo nurseInfo:nurseInfoList){
						PrpLDlossPersNurseVo prpLDlossPersNurseVo = new PrpLDlossPersNurseVo();
						if(nurseInfo.getId() != null && !"".equals(nurseInfo.getId())){
						    prpLDlossPersNurseVo.setId(Long.decode(nurseInfo.getId()));
						}
						prpLDlossPersNurseVo.setRegistNo(certainsTaskInfoVo.getRegistNo());
						prpLDlossPersNurseVo.setPayPersonName(nurseInfo.getName());
						prpLDlossPersNurseVo.setSex(nurseInfo.getSex());
						prpLDlossPersNurseVo.setOccupationCode(nurseInfo.getCareerCode());
						prpLDlossPersNurseVo.setOccupationName(nurseInfo.getCareerName());
						prpLDlossPersNurseVo.setRelationship(nurseInfo.getRelationShip());
						prpLDlossPersNurseVo.setIncome(nurseInfo.getIncomeType());
						prpLDlossPersNurses.add(prpLDlossPersNurseVo);
					}
				}
				prpLDlossPersInjuredVo.setPrpLDlossPersNurses(prpLDlossPersNurses);
				
				//被监护人信息
				List<PrpLDlossPersRaiseVo> prpLDlossPersRaises = new ArrayList<PrpLDlossPersRaiseVo>();
				
				List<RaiseInfoVo> raiseInfoList = personInfo.getRaiseInfoVo();
				if(raiseInfoList != null && raiseInfoList.size()>0){
					for(RaiseInfoVo raiseInfo:raiseInfoList){
						PrpLDlossPersRaiseVo prpLDlossPersRaiseVo = new PrpLDlossPersRaiseVo();
						if(raiseInfo.getId() != null && !"".equals(raiseInfo.getId())){
							prpLDlossPersRaiseVo.setId(Long.decode(raiseInfo.getId()));
						}
						prpLDlossPersRaiseVo.setRegistNo(certainsTaskInfoVo.getRegistNo());
						prpLDlossPersRaiseVo.setPayPersonName(raiseInfo.getName());
						prpLDlossPersRaiseVo.setSex(raiseInfo.getSex());
						prpLDlossPersRaiseVo.setAge(NullToZero(raiseInfo.getAge()));
						prpLDlossPersRaiseVo.setDomicile(raiseInfo.getHukouType());
						prpLDlossPersRaiseVo.setRelationship(raiseInfo.getRelationShip());
						prpLDlossPersRaises.add(prpLDlossPersRaiseVo);
					}
				}
				prpLDlossPersInjuredVo.setPrpLDlossPersRaises(prpLDlossPersRaises);
				
				//跟踪的人伤费用
				List<PrpLDlossPersTraceFeeVo> prpLDlossPersTraceFees = new ArrayList<PrpLDlossPersTraceFeeVo>();
					
				List<FeeInfo> feeInfoList =  traceInfo.getFeeInfo();
				
				//总估损金额
				BigDecimal sumReportFee = new BigDecimal(0);
				//总索赔金额
				BigDecimal sumRealFee = new BigDecimal(0);
				//总定损金额
				BigDecimal sumdefLoss = new BigDecimal(0);
				//总减损金额
				BigDecimal sumdetractionfee = new BigDecimal(0);
				
				if (feeInfoList != null && feeInfoList.size() > 0) {
					for(FeeInfo feeInfo:feeInfoList){
						PrpLDlossPersTraceFeeVo lossPersTraceFee = new PrpLDlossPersTraceFeeVo();
						
						if(feeInfo.getId() != null && !"".equals(feeInfo.getId())){
							lossPersTraceFee.setId(Long.decode(feeInfo.getId()));
						}
						lossPersTraceFee.setRegistNo(certainsTaskInfoVo.getRegistNo());
						lossPersTraceFee.setFeeTypeCode(feeInfo.getFeeName());
						lossPersTraceFee.setFeeTypeName(feeInfo.getFeeName());
						lossPersTraceFee.setReportFee(NullToZero(feeInfo.getEstimatedLoss()));
						sumReportFee = sumReportFee.add(NullToZero(feeInfo.getEstimatedLoss()));
						lossPersTraceFee.setRealFee(NullToZero(feeInfo.getClaimAmount()));
						sumRealFee = sumRealFee.add(NullToZero(feeInfo.getClaimAmount()));
						//TODO liming 费用计算公式
						lossPersTraceFee.setDefloss(NullToZero(feeInfo.getFixedLossAmount()));
						sumdefLoss = sumdefLoss.add(NullToZero(feeInfo.getFixedLossAmount()));
						lossPersTraceFee.setDetractionfee(NullToZero(feeInfo.getImpairmentAmount()));
						sumdetractionfee = sumdetractionfee.add(NullToZero(feeInfo.getImpairmentAmount()));
						lossPersTraceFee.setRemark(feeInfo.getFeedEscription());
						prpLDlossPersTraceFees.add(lossPersTraceFee);
					}
					
				}
				
				prpLDlossPersTraceVo.setPrpLDlossPersInjured(prpLDlossPersInjuredVo);
				
				prpLDlossPersTraceVo.setPrpLDlossPersTraceFees(prpLDlossPersTraceFees);
				prpLDlossPersTraceVo.setSumdefLoss(sumdefLoss);
				prpLDlossPersTraceVo.setSumReportFee(sumReportFee);
				prpLDlossPersTraceVo.setSumRealFee(sumRealFee);
				prpLDlossPersTraceVo.setSumDetractionFee(sumdetractionfee);
				prpLDlossPersTraces.add(prpLDlossPersTraceVo);
			}

		}
		
		if(feeInfos != null && feeInfos.size() > 0){
			for(FeeInfoVo feeInfo:feeInfos){
				PrpLDlossChargeVo prpLDlossChargeVo = new PrpLDlossChargeVo();
				if(feeInfo.getFeeId() != null && !"".equals(feeInfo.getFeeId())){
				    prpLDlossChargeVo.setId(Long.valueOf(feeInfo.getFeeId()));
				}
				prpLDlossChargeVo.setRegistNo(certainsTaskInfoVo.getRegistNo());
				prpLDlossChargeVo.setBusinessType("PLoss");
				if(feeInfo.getFeeType() != null) {
					if("01".equals(feeInfo.getFeeType())) {
						prpLDlossChargeVo.setChargeName("诉讼费");
					}else if("02".equals(feeInfo.getFeeType())){
						prpLDlossChargeVo.setChargeName("律师费");
					}else if("03".equals(feeInfo.getFeeType())){
						prpLDlossChargeVo.setChargeName("差旅费");
					}else if("04".equals(feeInfo.getFeeType())){
						prpLDlossChargeVo.setChargeName("检测费");
					}else if("05".equals(feeInfo.getFeeType())){
						prpLDlossChargeVo.setChargeName("鉴定费");
					}else if("06".equals(feeInfo.getFeeType())){
						prpLDlossChargeVo.setChargeName("咨询顾问费");
					}else if("07".equals(feeInfo.getFeeType())){
						prpLDlossChargeVo.setChargeName("奖励费");
					}else if("99".equals(feeInfo.getFeeType())){
						prpLDlossChargeVo.setChargeName("其他");
					}
				}
				prpLDlossChargeVo.setKindCode(feeInfo.getKindCode());
				prpLDlossChargeVo.setChargeCode(feeInfo.getFeeType());
				if(StringUtils.isNotBlank(feeInfo.getAccountId())) {
					prpLDlossChargeVo.setReceiverId(Long.valueOf(feeInfo.getAccountId()));
				}
				prpLDlossChargeVo.setChargeFee(NullToZero(feeInfo.getAmount()));
				prpLDlossCharges.add(prpLDlossChargeVo);
			}
		}
		
	}
	
	
	
	private void init(){
		if(persTraceHandleService==null){
			persTraceHandleService = (PersTraceHandleService)Springs.getBean(PersTraceHandleService.class);
		}
		if(persTraceService==null){
			persTraceService = (PersTraceService)Springs.getBean(PersTraceService.class);
		}
		if(wfTaskHandleService==null){
			wfTaskHandleService = (WfTaskHandleService)Springs.getBean(WfTaskHandleService.class);
		}
		if(claimTextSerVice==null){
			claimTextSerVice = (ClaimTextService)Springs.getBean(ClaimTextService.class);
		}
		if(lossChargeService==null){
			lossChargeService = (LossChargeService)Springs.getBean(LossChargeService.class);
		}
		if(lossToPlatformService==null){
			lossToPlatformService = (LossToPlatformService)Springs.getBean(LossToPlatformService.class);
		}
		if(claimTaskService==null){
			claimTaskService = (ClaimTaskService)Springs.getBean(ClaimTaskService.class);
		}
		if(interfaceAsyncService==null){
			interfaceAsyncService = (InterfaceAsyncService)Springs.getBean(InterfaceAsyncService.class);
		}
		if(certifyIlogService==null){
			certifyIlogService = (CertifyIlogService)Springs.getBean(CertifyIlogService.class);
		}	
		if(certifyToPaltformService==null){
			certifyToPaltformService=(CertifyToPaltformService)Springs.getBean(CertifyToPaltformService.class);
		}
		if(compensateTaskService==null){
			compensateTaskService=(CompensateTaskService)Springs.getBean(CompensateTaskService.class);
		}
        if(verifyClaimService == null){
            verifyClaimService = (VerifyClaimService)Springs.getBean(VerifyClaimService.class);
        }
        if(policyViewService == null){
        	policyViewService = (PolicyViewService)Springs.getBean(PolicyViewService.class);
        }
		if(codeTranService == null) {
		    codeTranService = (CodeTranService)Springs.getBean(CodeTranService.class);
		}
		if(certifyService == null){
			certifyService = (CertifyService)Springs.getBean(CertifyService.class);
		}
		if(managerService == null){
			managerService = (ManagerService)Springs.getBean(ManagerService.class);
		}
	}
	
	
	/**
	 * 校验请求的参数
	 * @param certainsTaskInfoVo
	 */
	public void checkRequest(CertainsLossPerTaskRequestVo certainsTaskInfoVo){
		notNull(certainsTaskInfoVo.getRegistNo()," 报案号为空  ");
		notNull(certainsTaskInfoVo.getCertainsId()," 理赔定损ID为空  ");
		notNull(certainsTaskInfoVo.getTaskId()," 任务id为空  ");
		notNull(certainsTaskInfoVo.getNextHandlerCode()," 处理人代码为空  ");
		notNull(certainsTaskInfoVo.getNextHandlerName()," 处理人名称为空  ");
		notNull(certainsTaskInfoVo.getScheduleObjectId()," 处理人员归属机构编码为空  ");
		notNull(certainsTaskInfoVo.getScheduleObjectName()," 处理人员归属机构名称为空  ");
		notNull(certainsTaskInfoVo.getOptionType()," 操作类型为空  ");
		
/***************************************************新增的校验条件******************************************************/
		checkNull(certainsTaskInfoVo.getNodeType(), " 调度节点为空 ", certainsTaskInfoVo.getRegistNo());
		checkNull(certainsTaskInfoVo.getIndemnityDuty(), " 事故责任为空 ", certainsTaskInfoVo.getRegistNo());
		
	}
	
	
	public void checkNull(String str, String message, String registNo){
		if(StringUtils.isBlank(str)){
			//如果检验为空，则在日志中记录一下
			logger.info("=========>报案号：" + registNo + " --- " + message);
		}
	}
	
	public void notNull(String str,String message){
		if(StringUtils.isBlank(str)){
			throw new IllegalArgumentException(message);
		}
	}
	private void notExistLowercase(String str, String message){
		String regex=".*[a-z]+.*";
		Matcher m=Pattern.compile(regex).matcher(str);
		if(m.matches()){
			throw new IllegalArgumentException(message);
		}
	}
	private static BigDecimal NullToZero(String strNum) {
		if(strNum==null||strNum.equals("")){
			strNum = "0";
		}
		return new BigDecimal(strNum);
	}

}
