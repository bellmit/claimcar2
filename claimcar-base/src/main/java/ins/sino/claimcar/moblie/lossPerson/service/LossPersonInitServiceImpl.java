package ins.sino.claimcar.moblie.lossPerson.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import ins.framework.lang.Springs;
import ins.platform.common.service.facade.CodeTranService;
import ins.platform.common.util.CodeTranUtil;
import ins.platform.sysuser.service.facade.SysUserService;
import ins.platform.utils.ClaimBaseCoder;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants.CheckClass;
import ins.sino.claimcar.CodeConstants.HandlerStatus;
import ins.sino.claimcar.CodeConstants.WorkStatus;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.PrpLCheckDutyVo;
import ins.sino.claimcar.common.claimtext.service.ClaimTextService;
import ins.sino.claimcar.common.claimtext.vo.PrpLClaimTextVo;
import ins.sino.claimcar.common.losscharge.service.LossChargeService;
import ins.sino.claimcar.common.losscharge.vo.PrpLDlossChargeVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.PayCustomService;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.lossperson.service.PersTraceHandleService;
import ins.sino.claimcar.lossperson.service.PersTraceService;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersExtVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersHospitalVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersNurseVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersRaiseVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceFeeVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceMainVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceVo;
import ins.sino.claimcar.manager.service.ManagerService;
import ins.sino.claimcar.manager.vo.PrpLPayCustomVo;
import ins.sino.claimcar.manager.vo.PrpdIntermMainVo;
import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckHead;
import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckResponseHead;
import ins.sino.claimcar.moblie.loss.vo.InitDeflossReqBody;
import ins.sino.claimcar.moblie.lossPerson.vo.CertainsLossPerTaskRequestVo;
import ins.sino.claimcar.moblie.lossPerson.vo.FeeInfo;
import ins.sino.claimcar.moblie.lossPerson.vo.FeeInfoVos;
import ins.sino.claimcar.moblie.lossPerson.vo.InPantientInfoVo;
import ins.sino.claimcar.moblie.lossPerson.vo.LossPersonInitReqVo;
import ins.sino.claimcar.moblie.lossPerson.vo.LossPersonInitRespBodyVo;
import ins.sino.claimcar.moblie.lossPerson.vo.LossPersonInitRespVo;
import ins.sino.claimcar.moblie.lossPerson.vo.NurseInfoVo;
import ins.sino.claimcar.moblie.lossPerson.vo.PartInfoVo;
import ins.sino.claimcar.moblie.lossPerson.vo.PayeeInfoVo;
import ins.sino.claimcar.moblie.lossPerson.vo.PersonInfoVos;
import ins.sino.claimcar.moblie.lossPerson.vo.RaiseInfoVo;
import ins.sino.claimcar.moblie.lossPerson.vo.TraceInfoVo;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.service.ScheduleTaskService;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.sinosoft.api.service.ServiceInterface;


/**
 * 人伤跟踪信息初始化（移动查勘请求理赔）
 * @author j2eel
 *
 */
public class LossPersonInitServiceImpl implements ServiceInterface{
	
	@Autowired
	SysUserService sysUserService;
	@Autowired
	WfTaskHandleService wfTaskHandleService;
	@Autowired
	PersTraceHandleService persTraceHandleService;
	@Autowired
	PersTraceService persTraceService;
	@Autowired
	RegistQueryService registQueryService;
	@Autowired
	CheckTaskService checkTaskService;
	@Autowired
	ScheduleTaskService scheduleTaskService;
	@Autowired
	ManagerService managerService;
    @Autowired
    LossChargeService lossChargeService; 
	@Autowired
	PayCustomService payCustomService;
	@Autowired
	CodeTranService codeTranService;
	@Autowired
	ClaimTextService claimTextSerVice;
	
	private static Logger logger = LoggerFactory.getLogger(LossPersonInitServiceImpl.class);

	@Override
	public Object service(String arg0, Object arg1) {
		
		init();
		LossPersonInitReqVo reqVo = new LossPersonInitReqVo();
		MobileCheckHead reqHead = new MobileCheckHead();
		InitDeflossReqBody reqBody = new InitDeflossReqBody();
		LossPersonInitRespVo respVo = new LossPersonInitRespVo();
		MobileCheckResponseHead respHeadVo = new MobileCheckResponseHead();
		LossPersonInitRespBodyVo respBodyVo = new LossPersonInitRespBodyVo();
		
		try {
			 String reqXml = ClaimBaseCoder.objToXmlUtf(arg1);
			 logger.info("=======================> 人伤跟踪信息初始化请求报文： " + reqXml);
			
			 if(reqXml == null){
				 throw new IllegalArgumentException(" 请求报文为空！！！ ");
			 }
			 //校验报文
			 reqVo = (LossPersonInitReqVo) arg1;
			
			 reqHead = reqVo.getReqHeadVo();
			 if(!"021".equals(reqHead.getRequestType()) || !"claim_user".equals(reqHead.getUser()) || 
					 !"claim_psd".equals(reqHead.getPassWord())){
				 throw new IllegalArgumentException(" 请求头参数错误！ ");
			 }
			 reqBody = reqVo.getReqBodyVo();
			 //非空校验
			 checkReqBodyVo(reqBody);
			 
			 //
			 SysUserVo userVo = new SysUserVo();
			 userVo = sysUserService.findByUserCode(reqBody.getNextHandlerCode());
			 
			 userVo.setComCode(reqBody.getScheduleObjectId());
			 userVo.setUserCode(reqBody.getNextHandlerCode());
			 userVo.setUserName(reqBody.getNextHandlerName());
			 userVo.setComName(reqBody.getScheduleObjectName());
			 
			 Double flowTaskId = Double.valueOf(reqBody.getTaskId());
			 PrpLWfTaskVo taskVo = wfTaskHandleService.findTaskIn(flowTaskId);  //查询人伤任务
			 String registNo = reqBody.getRegistNo();
			 if(taskVo != null && taskVo.getTaskId() != null){
				 if((StringUtils.isNotBlank(taskVo.getHandlerUser()) && reqBody.getNextHandlerCode().equals(taskVo.getHandlerUser())) ||
						 (StringUtils.isNotBlank(taskVo.getAssignUser()) && taskVo.getAssignUser().equals(reqBody.getNextHandlerCode()))){
					 
					 //人伤移动端处理理赔修改处理标志，回写工作流的isMobileAccept
					 taskVo.setIsMobileAccept("1");
					 wfTaskHandleService.updateTaskIn(taskVo);
					 
					 //任务未接收，先接收
					 if("0".equals(taskVo.getWorkStatus()) && "PLFirst".equals(taskVo.getSubNodeCode())){
						 persTraceHandleService.acceptPersTraceTask(flowTaskId.toString(), registNo, userVo);
//						 taskVo.setHandlerIdKey(traceMainId.toString());
					 }else{
						 //后续人伤跟踪初始化不需要接收，把任务状态改成正在处理
						 taskVo.setHandlerStatus(HandlerStatus.DOING);
						 taskVo.setWorkStatus(WorkStatus.DOING);
						 wfTaskHandleService.updateTaskIn(taskVo);
					 }
					 
					 //组织返回报文
					 respBodyVo = setResBodyMethod(reqBody, flowTaskId);
					 respVo.setRespBodyVo(respBodyVo);
					 
					 respHeadVo.setResponseType("021");
					 respHeadVo.setResponseCode("YES");
					 respHeadVo.setResponseMessage("Success");
					 respVo.setRespHeadVo(respHeadVo);
					 
				 }else{
					 respHeadVo.setResponseType("021");
					 respHeadVo.setResponseCode("NO");
					 respHeadVo.setResponseMessage("该员工{" + reqBody.getNextHandlerCode() + "}没有处理案子的权限！！");
					 respVo.setRespHeadVo(respHeadVo);
				 }
			 }else{
				 respHeadVo.setResponseType("021");
				 respHeadVo.setResponseCode("NO");
				 respHeadVo.setResponseMessage("没有查询到该任务！！");
				 respVo.setRespHeadVo(respHeadVo);
			 }
			
		} catch (Exception e) {
			respHeadVo.setResponseType("021");
			respHeadVo.setResponseCode("NO");
			respHeadVo.setResponseMessage(e.getMessage());
			respVo.setRespHeadVo(respHeadVo);
			logger.info("人伤跟踪初始化接口报错！ " ,e);
		}
		
		String respXml = ClaimBaseCoder.objToXmlUtf(respVo);
		logger.info("人伤跟踪初始化返回报文: " + respXml);
		return respVo;
	}
	
	
	private void init(){
		if(sysUserService==null){
			sysUserService = (SysUserService)Springs.getBean(SysUserService.class);
		}
		if(wfTaskHandleService==null){
			wfTaskHandleService = (WfTaskHandleService)Springs.getBean(WfTaskHandleService.class);
		}
		if(persTraceHandleService==null){
			persTraceHandleService = (PersTraceHandleService)Springs.getBean(PersTraceHandleService.class);
		}
		if(persTraceService==null){
			persTraceService = (PersTraceService)Springs.getBean(PersTraceService.class);
		}
		if(registQueryService==null){
			registQueryService = (RegistQueryService)Springs.getBean(RegistQueryService.class);
		}
		if(checkTaskService==null){
			checkTaskService = (CheckTaskService)Springs.getBean(CheckTaskService.class);
		}
		if(scheduleTaskService==null){
			scheduleTaskService = (ScheduleTaskService)Springs.getBean(ScheduleTaskService.class);
		}
		if(managerService==null){
			managerService = (ManagerService)Springs.getBean(ManagerService.class);
		}
		if(lossChargeService==null){
			lossChargeService = (LossChargeService)Springs.getBean(LossChargeService.class);
		}
		if(payCustomService==null){
			payCustomService = (PayCustomService)Springs.getBean(PayCustomService.class);
		}
		if(codeTranService==null){
			codeTranService = (CodeTranService)Springs.getBean(CodeTranService.class);
		}
		if(claimTextSerVice==null){
			claimTextSerVice = (ClaimTextService)Springs.getBean(ClaimTextService.class);
		}
		
	}
	
	private void checkReqBodyVo(InitDeflossReqBody reqBody){
		
		if(StringUtils.isBlank(reqBody.getRegistNo())){
			throw new IllegalArgumentException(" 报案号为空！ ");
		}
		if(StringUtils.isBlank(reqBody.getCertainStatus())){
			throw new IllegalArgumentException(" 案件状态为空！ ");
		}
		if(StringUtils.isBlank(reqBody.getTaskId())){
			throw new IllegalArgumentException(" 任务ID为空！ ");
		}
		if(StringUtils.isBlank(reqBody.getSerialNo())){
			throw new IllegalArgumentException(" 序号为空！ ");
		}
		if(StringUtils.isBlank(reqBody.getNodeType())){
			throw new IllegalArgumentException(" 调度节点为空！ ");
		}
		if(StringUtils.isBlank(reqBody.getNextHandlerCode())){
			throw new IllegalArgumentException(" 处理人代码为空！ ");
		}
		if(StringUtils.isBlank(reqBody.getNextHandlerName())){
			throw new IllegalArgumentException(" 处理人姓名为空！ ");
		}
		if(StringUtils.isBlank(reqBody.getScheduleObjectId())){
			throw new IllegalArgumentException(" 处理人员归属机构编码为空！ ");
		}
		if(StringUtils.isBlank(reqBody.getScheduleObjectName())){
			throw new IllegalArgumentException(" 处理人员归属机构名称为空！ ");
		}

	}
	
	/**
	 * 人伤初始化并组装返回报文数据
	 * @param reqBody
	 * @return
	 * @throws Exception 
	 */
	private LossPersonInitRespBodyVo setResBodyMethod(InitDeflossReqBody reqBody, Double flowTaskId) throws Exception{
		LossPersonInitRespBodyVo respBodyVo = new LossPersonInitRespBodyVo();
		CertainsLossPerTaskRequestVo certainsTaskInfo = new CertainsLossPerTaskRequestVo();
		List<FeeInfoVos> feeInfoVoList = new ArrayList<FeeInfoVos>();
		List<PersonInfoVos> personInfoVoList = new ArrayList<PersonInfoVos>();
		
		PrpLDlossPersTraceMainVo persTraceMainVo = new PrpLDlossPersTraceMainVo();// 人伤主表
		PrpLClaimTextVo prpLClaimTextVo = new PrpLClaimTextVo();// 当前节点意见
		PrpLCheckDutyVo prpLCheckDutyVo = new PrpLCheckDutyVo();// 车辆事故责任表
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		PrpLWfTaskVo taskVo = wfTaskHandleService.findTaskIn(flowTaskId);
		
		String registNo = taskVo.getRegistNo();
		
		//人伤对象
		certainsTaskInfo.setRegistNo(reqBody.getRegistNo());
		certainsTaskInfo.setCertainsId(taskVo.getHandlerIdKey());
		certainsTaskInfo.setTaskId(taskVo.getTaskId() != null ? taskVo.getTaskId().toString() : "");
		certainsTaskInfo.setNodeType(taskVo.getNodeCode());
		
		if(taskVo.getHandlerUser() == null || taskVo.getHandlerUser().isEmpty()){
			certainsTaskInfo.setNextHandlerCode(taskVo.getAssignUser());
			String assign = CodeTranUtil.transCode("UserCode", taskVo.getAssignUser());
			if(assign.equals(taskVo.getAssignUser())){
				SysUserVo userVo = scheduleTaskService.findPrpduserByUserCode(taskVo.getAssignUser(),"");
				if(userVo != null && userVo.getUserName() != null){
					assign = userVo.getUserName();
				}
			}
			certainsTaskInfo.setNextHandlerName(assign);
			certainsTaskInfo.setScheduleObjectId(taskVo.getAssignCom()); //处理人员归属机构编码
			certainsTaskInfo.setScheduleObjectName(CodeTranUtil.transCode("ComCodeFull",taskVo.getAssignCom())); //处理人员归属机构名称
		}else{
			certainsTaskInfo.setNextHandlerCode(taskVo.getHandlerUser());
			String assignUser = CodeTranUtil.transCode("UserCode", taskVo.getHandlerUser());
			if(assignUser.equals(taskVo.getHandlerUser())){
				SysUserVo userVo = scheduleTaskService.findPrpduserByUserCode(taskVo.getHandlerUser(),"");
				if(userVo != null && userVo.getUserName() != null){
					assignUser = userVo.getUserName();
				}
			}
			certainsTaskInfo.setNextHandlerName(assignUser);
			certainsTaskInfo.setScheduleObjectId(taskVo.getHandlerCom());
			certainsTaskInfo.setScheduleObjectName(CodeTranUtil.transCode("ComCodeFull", taskVo.getHandlerCom()));
	    }
		
		//事故责任，责任比例
		prpLCheckDutyVo = checkTaskService.findCheckDuty(registNo, 1);
		if(prpLCheckDutyVo != null){
			certainsTaskInfo.setIndemnityDuty(prpLCheckDutyVo.getIndemnityDuty());
			certainsTaskInfo.setIndemnityDutyRate(prpLCheckDutyVo.getIndemnityDutyRate() != null ? prpLCheckDutyVo.getIndemnityDutyRate().toString() : "");
			//交强险责任类型
			certainsTaskInfo.setJqIndemnityDuty(prpLCheckDutyVo.getCiDutyFlag());
		}
		
		String userCode = reqBody.getNextHandlerCode();
		PrpdIntermMainVo intermMainVo = managerService.findIntermByUserCode(userCode);
		if(intermMainVo != null){
			certainsTaskInfo.setlossType(CheckClass.CHECKCLASS_Y);
		}else{
			certainsTaskInfo.setlossType(CheckClass.CHECKCLASS_N);
		}
		
		persTraceMainVo = persTraceService.findPersTraceMainVoById(Long.valueOf(taskVo.getHandlerIdKey()));
		
		if(FlowNode.PLFirst.name().equals(taskVo.getSubNodeCode())){
			certainsTaskInfo.setTracer(persTraceMainVo.getPlfName());
			certainsTaskInfo.setTracerIdentifyNo(persTraceMainVo.getPlfCertiCode());
		}else{
			certainsTaskInfo.setTracer(persTraceMainVo.getOperatorName());
			certainsTaskInfo.setTracerIdentifyNo(persTraceMainVo.getOperatorCertiCode());
		}
		//减损金额
		certainsTaskInfo.setDerogationisAmount(persTraceMainVo.getIsDeroAmout() != null ? persTraceMainVo.getIsDeroAmout().toString() : "");
		certainsTaskInfo.setInsideDerogationis(persTraceMainVo.getInSideDeroFlag());
		certainsTaskInfo.setIsDerogationis(persTraceMainVo.getIsDeroFlag());
		certainsTaskInfo.setIsSmallCase(persTraceMainVo.getIsMinorInjuryCases());
		certainsTaskInfo.setIsBigCase(persTraceMainVo.getMajorcaseFlag());
		//跟踪意见
		prpLClaimTextVo = claimTextSerVice.findClaimTextByNode(persTraceMainVo.getId(),taskVo.getSubNodeCode(),"0");// 暂存时flag=0，提交flag=1
		if(prpLClaimTextVo != null){
			certainsTaskInfo.setTrackOpinions(prpLClaimTextVo.getRemark());
		}
		
		respBodyVo.setTaskInfoVo(certainsTaskInfo);
	
		
		
		//首次人伤跟踪就不用组织人员信息等
//		if(!FlowNode.PLFirst.name().equals(taskVo.getSubNodeCode())){
		List<PrpLDlossPersTraceVo> prplDlossPersTraceVo = persTraceMainVo.getPrpLDlossPersTraces();
		//人员信息
		if(prplDlossPersTraceVo != null && prplDlossPersTraceVo.size() > 0){
			for(PrpLDlossPersTraceVo traceVo : prplDlossPersTraceVo){
				PersonInfoVos personInfo = new PersonInfoVos();
				List<PartInfoVo> partInfoList = new ArrayList<PartInfoVo>();
				List<InPantientInfoVo> inpantientInfoList = new ArrayList<InPantientInfoVo>();
				TraceInfoVo traceInfoVo = new TraceInfoVo();
				List<FeeInfo> feeInfoList = new ArrayList<FeeInfo>();
				List<NurseInfoVo> nurseInfoList = new ArrayList<NurseInfoVo>();
				List<RaiseInfoVo> raiseInfoList = new ArrayList<RaiseInfoVo>();
				
				personInfo.setPersonId((traceVo.getPrpLDlossPersInjured().getId().toString()));
				personInfo.setCaseType(traceVo.getPrpLDlossPersInjured().getTreatSituation());
				personInfo.setName(traceVo.getPrpLDlossPersInjured().getPersonName());
				personInfo.setCredentialsType(traceVo.getPrpLDlossPersInjured().getCertiType());
				personInfo.setCredentialsNo(traceVo.getPrpLDlossPersInjured().getCertiCode());
				personInfo.setPersonAttr(traceVo.getPrpLDlossPersInjured().getLossItemType());
				personInfo.setAge(traceVo.getPrpLDlossPersInjured().getPersonAge() != null ? traceVo.getPrpLDlossPersInjured().getPersonAge().toString() : "");
				personInfo.setSex(traceVo.getPrpLDlossPersInjured().getPersonSex());
				personInfo.setIndustryCode(traceVo.getPrpLDlossPersInjured().getTicCode());
				personInfo.setIndustryName(traceVo.getPrpLDlossPersInjured().getTicName());
				personInfo.setIncomeType(traceVo.getPrpLDlossPersInjured().getIncome());
				personInfo.setHukouType(traceVo.getPrpLDlossPersInjured().getDomicile());
				personInfo.setPhone(traceVo.getPrpLDlossPersInjured().getPhoneNumber());
				personInfo.setDamageType(traceVo.getPrpLDlossPersInjured().getWoundCode());
				personInfo.setLicenseNo(traceVo.getPrpLDlossPersInjured().getLicenseNo());
				personInfo.setIdentifyCompanyName(traceVo.getPrpLDlossPersInjured().getChkComName());
				personInfo.setIdentifyCompanyCode(traceVo.getPrpLDlossPersInjured().getChkComCode());
				personInfo.setLicenseNo(traceVo.getPrpLDlossPersInjured().getLicenseNo());
				
				
				//受伤部位信息
//					long injuredVoId = traceVo.getPrpLDlossPersInjured().getId();
				List<PrpLDlossPersExtVo> persExtVoList = traceVo.getPrpLDlossPersInjured().getPrpLDlossPersExts();
				if(persExtVoList != null && persExtVoList.size() > 0){
					for(PrpLDlossPersExtVo extVo : persExtVoList){
//							if(extVo..equals(injuredVoId)){
							PartInfoVo partInfo = new PartInfoVo();
							partInfo.setPart(extVo.getInjuredPart());
							partInfo.setDiagnosis(extVo.getInjuredDiag());
							partInfo.setTreatmentType(extVo.getTreatMethod());
							partInfo.setTreatmentWay(extVo.getTreatWay());
							partInfo.setPrognosis(extVo.getProgSituation());
							partInfo.setDisabilityDegree(extVo.getWoundGrade());
							partInfo.setDetail(extVo.getDiagDetail());
							partInfo.setRemark(extVo.getTrackRemark());
							
							partInfoList.add(partInfo);
							personInfo.setPartInfoVo(partInfoList);
//							}
					}
				}
				
				//住院信息
				List<PrpLDlossPersHospitalVo> hospitalVoList = traceVo.getPrpLDlossPersInjured().getPrpLDlossPersHospitals();
				if(hospitalVoList != null && hospitalVoList.size() > 0){
					for(PrpLDlossPersHospitalVo hospitalVo : hospitalVoList){
						InPantientInfoVo infoVo = new InPantientInfoVo();
						infoVo.setId(hospitalVo.getId().toString());
						if(hospitalVo.getInHospitalDate() != null){
							infoVo.setInTime(format.format(hospitalVo.getInHospitalDate()));
						}
						if(hospitalVo.getOutHospitalDate() != null){
							infoVo.setOutTime(format.format(hospitalVo.getOutHospitalDate()));
						}
						infoVo.setProvince(hospitalVo.getHospitalProvince());
						infoVo.setCity(hospitalVo.getHospitalCity());
						infoVo.setHospitalCode(hospitalVo.getHospitalCode());
						infoVo.setHospitalName(hospitalVo.getHospitalName());
						infoVo.setRemark(hospitalVo.getRemark());
						
						inpantientInfoList.add(infoVo);
						personInfo.setInPantientInfoVo(inpantientInfoList);
					}
				}
				
				//跟踪记录信息
				traceInfoVo.setId(traceVo.getId().toString());
				traceInfoVo.setTraceType(traceVo.getTraceForms());
				traceInfoVo.setContent(traceVo.getOperatorDesc());
				traceInfoVo.setIsClaim(traceVo.getEndFlag());
				
				//跟踪费用记录信息
				List<PrpLDlossPersTraceFeeVo> traceFeeList = traceVo.getPrpLDlossPersTraceFees();
				if(traceFeeList != null && traceFeeList.size() > 0){
					for(PrpLDlossPersTraceFeeVo feeVo : traceFeeList){
						FeeInfo feeInfo = new FeeInfo();
						feeInfo.setId(feeVo.getId().toString());
						feeInfo.setFeeName(feeVo.getFeeTypeName());
						feeInfo.setEstimatedLoss(feeVo.getReportFee() != null ? feeVo.getReportFee().toString() : "");
						feeInfo.setClaimAmount(feeVo.getRealFee() != null ? feeVo.getRealFee().toString() : "");
						//费用公式
//							feeInfo.setCostFormula(feeVo);
						feeInfo.setFixedLossAmount(feeVo.getDefloss() != null ? feeVo.getDefloss().toString() : "");
						feeInfo.setImpairmentAmount(feeVo.getDetractionfee() != null ? feeVo.getDetractionfee().toString() : "");
						feeInfo.setFeedEscription(feeVo.getRemark());
						
						feeInfoList.add(feeInfo);
						traceInfoVo.setFeeInfo(feeInfoList);
					}
				}
				personInfo.setTraceInfoVo(traceInfoVo);
				
				//护理人员信息
				List<PrpLDlossPersNurseVo> nurseList = traceVo.getPrpLDlossPersInjured().getPrpLDlossPersNurses();
				if(nurseList != null && nurseList.size() > 0){
					for(PrpLDlossPersNurseVo nurseVo : nurseList){
						NurseInfoVo info = new NurseInfoVo();
						info.setId(nurseVo.getId().toString());
						info.setName(nurseVo.getPayPersonName());
						info.setSex(nurseVo.getSex());
						info.setCareerCode(nurseVo.getOccupationCode());
						info.setCareerName(nurseVo.getOccupationName());
						info.setIncomeType(nurseVo.getIncome());
						info.setRelationShip(nurseVo.getRelationship());
						
						nurseInfoList.add(info);
						personInfo.setNurseInfoVo(nurseInfoList);
					}
				}
				
				//被抚养人员信息
				List<PrpLDlossPersRaiseVo> raiseList = traceVo.getPrpLDlossPersInjured().getPrpLDlossPersRaises();
				if(raiseList != null && raiseList.size() > 0){
					for(PrpLDlossPersRaiseVo raiseVo : raiseList){
						RaiseInfoVo raise = new RaiseInfoVo();
						raise.setId(raiseVo.getId().toString());
						raise.setName(raiseVo.getPayPersonName());
						raise.setAge(raiseVo.getAge() != null ? raiseVo.getAge().toString() : "");
						raise.setSex(raiseVo.getSex());
						raise.setHukouType(raiseVo.getDomicile());
						raise.setRelationShip(raiseVo.getRelationship());
						
						raiseInfoList.add(raise);
						personInfo.setRaiseInfoVo(raiseInfoList);
					}
				}
				personInfoVoList.add(personInfo);
				
			}
		}
		respBodyVo.setPersonInfoVo(personInfoVoList);
//		}
		
		//费用赔款信息
		List<PrpLDlossChargeVo> chargeVoList = lossChargeService.findLossChargeVoByRegistNo(registNo);
		if(chargeVoList != null && chargeVoList.size() > 0){
			for(PrpLDlossChargeVo chargeVo : chargeVoList){
				if("PLoss".equals(chargeVo.getBusinessType())){
					FeeInfoVos feeInfo = new FeeInfoVos();
					feeInfo.setFeeId(chargeVo.getId() != null ? chargeVo.getId().toString() : "");
					feeInfo.setKindCode(chargeVo.getKindCode());
					feeInfo.setFeeType(chargeVo.getChargeCode());
					feeInfo.setAccountId(chargeVo.getReceiverId() != null ? chargeVo.getReceiverId().toString() : "");
					feeInfo.setAmount(chargeVo.getChargeFee() != null ? chargeVo.getChargeFee().toString() : "");
					//费用赔款--收款人信息
					long id = chargeVo.getReceiverId();
					PrpLPayCustomVo customVo = payCustomService.findPayCustomVoById(id);
					if(customVo != null){
						PayeeInfoVo payeeInfo = new PayeeInfoVo();
						payeeInfo.setPayeeNature(customVo.getPayObjectType());
						payeeInfo.setIdentifyType(customVo.getCertifyType());
						payeeInfo.setIdentifyNumber(customVo.getCertifyNo());
						payeeInfo.setPayeeType(customVo.getPayObjectKind());
						payeeInfo.setName(customVo.getPayeeName());
						payeeInfo.setPubAndPrilogo(customVo.getPublicAndPrivate());
						payeeInfo.setAccountNo(customVo.getAccountNo());
						payeeInfo.setProvinceCode(customVo.getProvinceCode() != null ? customVo.getProvinceCode().toString() : "");
						payeeInfo.setCityCode(customVo.getCityCode() != null ? customVo.getCityCode().toString() : "");
						//收款方开户行
						payeeInfo.setBankName(codeTranService.findCodeName("BankCode_Old", customVo.getBankName()));
						payeeInfo.setBranchName(customVo.getBankOutlets());
						payeeInfo.setBankCode(customVo.getBankNo());
						payeeInfo.setTransferMode(customVo.getPriorityFlag());
						payeeInfo.setPhone(customVo.getPayeeMobile());
						payeeInfo.setUse(customVo.getPurpose());
						
						feeInfo.setPayeeInfoVo(payeeInfo);
						
					}
					
					feeInfoVoList.add(feeInfo);
				}
			}
		}
		respBodyVo.setFeeInfoVo(feeInfoVoList);
	
		return respBodyVo;
	}
}
