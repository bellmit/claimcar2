/******************************************************************************
* CREATETIME : 2016年9月27日 上午10:29:35
******************************************************************************/
package ins.sino.claimcar.lossperson.service.spring;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Path;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;

import com.alibaba.dubbo.config.annotation.Service;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.service.CodeTranService;
import ins.framework.utils.Beans;
import ins.platform.common.util.ConfigUtil;
import ins.platform.sysuser.service.facade.SysUserService;
import ins.platform.utils.DataUtils;
import ins.platform.utils.IDCardUtil;
import ins.platform.vo.PrpLConfigValueVo;
import ins.platform.vo.SysCodeDictVo;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.AcheckUnderWriteFlag;
import ins.sino.claimcar.CodeConstants.AssessorUnderWriteFlag;
import ins.sino.claimcar.CodeConstants.AuditStatus;
import ins.sino.claimcar.CodeConstants.CheckClass;
import ins.sino.claimcar.CodeConstants.CheckTaskType;
import ins.sino.claimcar.CodeConstants.ClaimText;
import ins.sino.claimcar.CodeConstants.HandlerStatus;
import ins.sino.claimcar.CodeConstants.LossState;
import ins.sino.claimcar.CodeConstants.ModifyFlag;
import ins.sino.claimcar.CodeConstants.PersVeriFlag;
import ins.sino.claimcar.CodeConstants.RadioValue;
import ins.sino.claimcar.CodeConstants.TaskType;
import ins.sino.claimcar.CodeConstants.UnderWriteFlag;
import ins.sino.claimcar.CodeConstants.ValidFlag;
import ins.sino.claimcar.certify.service.CertifyIlogService;
import ins.sino.claimcar.check.service.CheckHandleService;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.PrpLCheckCarVo;
import ins.sino.claimcar.check.vo.PrpLCheckCarVoBase;
import ins.sino.claimcar.check.vo.PrpLCheckDriverVo;
import ins.sino.claimcar.check.vo.PrpLCheckDutyVo;
import ins.sino.claimcar.check.vo.PrpLCheckPersonVo;
import ins.sino.claimcar.check.vo.PrpLCheckTaskVo;
import ins.sino.claimcar.check.vo.PrpLCheckVo;
import ins.sino.claimcar.claim.service.ClaimTaskService;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.claim.service.InterfaceAsyncService;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.common.claimtext.service.ClaimTextService;
import ins.sino.claimcar.common.claimtext.vo.PrpLClaimTextVo;
import ins.sino.claimcar.common.losscharge.service.LossChargeService;
import ins.sino.claimcar.common.losscharge.vo.PrpLDlossChargeVo;
import ins.sino.claimcar.endcase.service.EndCasePubService;
import ins.sino.claimcar.endcase.vo.PrpLReCaseVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.constant.SubmitType;
import ins.sino.claimcar.flow.service.AssignService;
import ins.sino.claimcar.flow.service.WfFlowQueryService;
import ins.sino.claimcar.flow.service.WfMainService;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.service.WfTaskQueryService;
import ins.sino.claimcar.flow.vo.PrpLWfMainVo;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.flow.vo.WfSimpleTaskVo;
import ins.sino.claimcar.flow.vo.WfTaskSubmitVo;
import ins.sino.claimcar.ilog.defloss.vo.LIlogRuleResVo;
import ins.sino.claimcar.ilog.rule.service.IlogRuleService;
import ins.sino.claimcar.ilogFinalpowerInfo.vo.IlogFinalPowerInfoVo;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarInfoVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.lossperson.po.PrpLDlossPersTraceMain;
import ins.sino.claimcar.lossperson.service.PersReqIlogService;
import ins.sino.claimcar.lossperson.service.PersTraceDubboService;
import ins.sino.claimcar.lossperson.service.PersTraceHandleService;
import ins.sino.claimcar.lossperson.service.PersTraceService;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersExtVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersInjuredVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceFeeVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceHisVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceMainVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceVo;
import ins.sino.claimcar.lossperson.vo.SubmitNextVo;
import ins.sino.claimcar.lossprop.service.PropTaskService;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropMainVo;
import ins.sino.claimcar.manager.service.ManagerService;
import ins.sino.claimcar.manager.vo.PrpdCheckBankMainVo;
import ins.sino.claimcar.manager.vo.PrpdIntermMainVo;
import ins.sino.claimcar.mobilecheck.service.MobileCheckService;
import ins.sino.claimcar.mobilecheck.service.SendMsgToMobileService;
import ins.sino.claimcar.mobilecheck.vo.HandleScheduleDOrGBackReqVo;
import ins.sino.claimcar.mobilecheck.vo.HandleScheduleDOrGReqVo;
import ins.sino.claimcar.mobilecheck.vo.HandleScheduleReqDOrGBody;
import ins.sino.claimcar.mobilecheck.vo.HandleScheduleReqScheduleDOrG;
import ins.sino.claimcar.mobilecheck.vo.HandleScheduleReqScheduleItemDOrG;
import ins.sino.claimcar.mobilecheck.vo.HeadReq;
import ins.sino.claimcar.other.service.AcheckService;
import ins.sino.claimcar.other.service.AcheckTaskService;
import ins.sino.claimcar.other.service.AssessorDubboService;
import ins.sino.claimcar.other.vo.PrpLAcheckVo;
import ins.sino.claimcar.other.vo.PrpLAssessorVo;
import ins.sino.claimcar.platform.service.LossToPlatformService;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.PrpLCMainService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.service.ScheduleTaskService;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.rule.service.ClaimRuleApiService;
import ins.sino.claimcar.rule.vo.VerifyPersonRuleVo;
import ins.sino.claimcar.schedule.vo.PrpDScheduleDOrGMainVo;
import ins.sino.claimcar.schedule.vo.PrpLScheduleItemsVo;
import ins.sino.claimcar.schedule.vo.PrpLScheduleTaskVo;


/**
 * @author ★XMSH
 */
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path("persTraceHandleService")
public class PersTraceHandleServiceSpringImpl implements PersTraceHandleService {
	
	private static Logger logger = LoggerFactory.getLogger(PersTraceHandleServiceSpringImpl.class);
	
	@Autowired
	private RegistQueryService registQueryService;
	@Autowired
	private PolicyViewService policyViewService;
	@Autowired
	private ManagerService managerService;
	@Autowired
	private PersTraceService persTraceService;
	@Autowired
	private WfTaskHandleService wfTaskHandleService;
	@Autowired
	private LossChargeService lossChargeService;
	@Autowired
	private ClaimTextService claimTextSerVice;
	@Autowired
	private AssignService assignService;
	@Autowired
	private CheckTaskService checkTaskService;
	@Autowired
	private CodeTranService codeTranService;
	@Autowired
	private AssessorDubboService assessorService;
	@Autowired
	private ClaimRuleApiService claimRuleApiService;
	@Autowired
	private LossToPlatformService lossToPlatformService;
	@Autowired
	private LossCarService lossCarService;
	@Autowired
	PropTaskService propTaskService;
	@Autowired
	private CompensateTaskService compensateTaskService;
	@Autowired
	private SysUserService sysUserService;
	
	@Autowired
	private ClaimTaskService claimService;
	
	@Autowired
	private EndCasePubService endCasePubService;
	
	@Autowired
	private WfFlowQueryService wfFlowQueryService;
	@Autowired
	private PersReqIlogService persReqIlogService;
	@Autowired
    CertifyIlogService certifyIlogService;
    @Autowired
    private DatabaseDao databaseDao;
    @Autowired
    private IlogRuleService ilogRuleService;
    @Autowired
    private AcheckService acheckService;
    @Autowired
	private CheckHandleService checkHandleService;
    @Autowired
    private AcheckTaskService acheckTaskService;
	@Autowired
	PersTraceDubboService persTraceDubboService;
	@Autowired
	MobileCheckService mobileCheckService;
	@Autowired
	PrpLCMainService prpLCMainService;
	@Autowired
	ScheduleTaskService scheduleTaskService;
	@Autowired
	WfTaskQueryService wfTaskQueryService;
    @Autowired
    WfMainService wfMainService;
    @Autowired
    SendMsgToMobileService sendMsgToMobileService;
    
	@Override
	public Long acceptPersTraceTask(String flowTaskId,String registNo,SysUserVo userVo) throws Exception {
		PrpLDlossPersTraceMainVo persTraceMainVo = new PrpLDlossPersTraceMainVo();
		PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(registNo);

		persTraceMainVo.setRegistNo(registNo);
		persTraceMainVo.setRiskCode(prpLRegistVo.getRiskCode());
		persTraceMainVo.setReportType(prpLRegistVo.getReportType());
		persTraceMainVo.setMercyFlag(prpLRegistVo.getMercyFlag());// 案件紧急程度
		persTraceMainVo.setTraceTimes(BigDecimal.ZERO);// 跟踪次数初始为0

		// 定损员是否是公估机构人员, prpduser的usercode 是prpdintermmain 公估表的INTERMCODE 公估公司代码
		// 1 通过prpduser.usercode 查找prpdintermmain 数据 ,不为空 则为公估定损
		PrpdIntermMainVo intermMainVo = managerService.findIntermByUserCode(userVo.getUserCode());
		boolean isIntermediary = intermMainVo==null?false:true;
		if(isIntermediary){
			persTraceMainVo.setIntermediaryFlag("1");// 公估定损
			persTraceMainVo.setIntermediaryInfoId(intermMainVo.getIntermCode());// 公估机构代码
		}else{
			persTraceMainVo.setIntermediaryFlag("0");// 司内定损
			persTraceMainVo.setPlfName(userVo.getUserName());// 首次跟踪人姓名
			persTraceMainVo.setPlfCertiCode(userVo.getIdentifyNumber());// 首次跟踪人身份证号
		}

		// 初始化理算状态和核损状态
		persTraceMainVo.setLossState(LossState.UN_COMPENSATE);// 未理算
		persTraceMainVo.setUnderwriteFlag(PersVeriFlag.INIT);// 未审核

		persTraceMainVo.setOperatorCode(userVo.getUserCode());
		persTraceMainVo.setPlCode(userVo.getUserCode());
		persTraceMainVo.setOperatorName(userVo.getUserName());
		persTraceMainVo.setOperatorCertiCode(userVo.getIdentifyNumber());
		Date date = new Date();
		persTraceMainVo.setCreateTime(date);
		persTraceMainVo.setCreateUser(userVo.getUserCode());
		persTraceMainVo.setUpdateTime(date);
		persTraceMainVo.setUpdateUser(userVo.getUserCode());
		
		Long traceMainId = persTraceService.saveOrUpdatePersTraceMain(persTraceMainVo);

		wfTaskHandleService.tempSaveTask(Double.parseDouble(flowTaskId),traceMainId.toString(),userVo.getUserCode(),userVo.getComCode());
		return traceMainId;
	}
	
	@Override
	public Long saveCasualtyInfo(PrpLDlossPersTraceVo persTraceVo,SysUserVo userVo) throws Exception {
		// 受伤人员身份证号码不能一样
		if("1".equals(persTraceVo.getPrpLDlossPersInjured().getCertiType())){
			String registNo = persTraceVo.getPrpLDlossPersInjured().getRegistNo();
			String certiCode = persTraceVo.getPrpLDlossPersInjured().getCertiCode();
			IDCardUtil idCardUtil = new IDCardUtil();
			if(idCardUtil.IDCardValidate(certiCode).getErrMsg() != null){//校验身份证格式是否正确
				throw new IllegalArgumentException(idCardUtil.IDCardValidate(certiCode).getErrMsg());
			}
			List<PrpLDlossPersInjuredVo> persInjuredVos = persTraceService.findPersInjuredVo(registNo,certiCode);
			boolean isExit = persInjuredVos != null && persInjuredVos.size() > 0;
			if(persTraceVo.getId()==null){// 新增人员
				if(isExit){
					if(!persTraceVo.getPersonName().equals(persInjuredVos.get(0).getPersonName())){//新需求：对同名且身份证相同的不校验
						throw new Exception("伤亡人员身份证号不能重复，请重新录入！");
					}
				}
			}else{
				if(isExit){
					for(PrpLDlossPersInjuredVo injuredVo:persInjuredVos){
						if(!persTraceVo.getPersonName().equals(injuredVo.getPersonName())){//新需求：对同名且身份证相同的不校验
							if(persTraceVo.getId().compareTo(injuredVo.getId())!=0){
								throw new Exception("伤亡人员身份证号不能重复，请重新录入！");
							}
						}
					}
				}
			}			
			
			//
			if(persTraceVo.getPersTraceMainId() != null){
				PrpLDlossPersTraceMainVo prpLDlossPersTraceMainVo = persTraceService.findPersTraceMainVoById(persTraceVo.getPersTraceMainId());
				if(prpLDlossPersTraceMainVo != null){
				String firstTrackPeople = prpLDlossPersTraceMainVo.getPlfCertiCode();  //人伤跟踪人
				String tracePeople = prpLDlossPersTraceMainVo.getOperatorCertiCode();  //人伤定损人
				String medicalAudit = prpLDlossPersTraceMainVo.getVerifyCertiCode();   //医疗审核人员
				if(StringUtils.isNotBlank(registNo)){
					PrpLCheckVo checkVo = checkTaskService.findCheckVoByRegistNo(registNo);
					List<PrpLDlossCarMainVo> carMainList = lossCarService.findLossCarMainByRegistNo(registNo);
					if(StringUtils.isNotBlank(persTraceVo.getPrpLDlossPersInjured().getCertiType()) 
							&& "1".equals(persTraceVo.getPrpLDlossPersInjured().getCertiType())){
						
						if(StringUtils.isNotBlank(certiCode)){
							if(StringUtils.isNotBlank(firstTrackPeople) && firstTrackPeople.equals(certiCode)){
								throw new Exception("人伤跟踪人身份证号与伤亡人员身份证号不能相同，请修改！！！");
							}
							if(StringUtils.isNotBlank(tracePeople) && tracePeople.equals(certiCode)){
								throw new Exception("人伤定损人身份证号与伤亡人员身份证号不能相同，请修改！！！");
							}
							if(StringUtils.isNotBlank(medicalAudit) && medicalAudit.equals(certiCode)){
								throw new Exception("医疗审核人员证件号码与伤亡人员身份证号不能相同， 请修改！！！");
							}
							
							//查勘
							if(checkVo != null){
								PrpLCheckTaskVo checkTaskVo = checkVo.getPrpLCheckTask();
								if(StringUtils.isNotBlank(checkTaskVo.getCheckerIdfNo()) && checkTaskVo.getCheckerIdfNo().equals(certiCode)){
									throw new Exception("查勘员证件号码与伤亡人员身份证号不能相同，请修改！！！");
								}
							}
							//定损
							if(carMainList != null && carMainList.size() > 0){
								for(PrpLDlossCarMainVo vo : carMainList){
									if(StringUtils.isNotBlank(vo.getHandlerIdNo()) && vo.getHandlerIdNo().equals(certiCode)){
										throw new Exception("车辆定损员证件号码与伤亡人员身份证号相同，请修改！！！");
									}
									
									if(StringUtils.isNotBlank(vo.getUnderWiteIdNo()) && vo.getUnderWiteIdNo().equals(certiCode)){
										throw new Exception("车辆核损员证件号码与伤亡人员身份证号相同，请修改！！！");
									}
								}
							}
						}
					}
				}
			}
			}
			
		}
		
		Long bussTaskId = persTraceService.saveOrUpdatePersTrace(persTraceVo,userVo);
		// 更新一遍数据主要是不排空费用
		// persTraceService.updatePersTraceSumAmount(persTraceVo);
		List<PrpLDlossPersTraceFeeVo> persTraceFeeVos = persTraceVo.getPrpLDlossPersTraceFees();
		for(PrpLDlossPersTraceFeeVo persTraceFeeVo:persTraceFeeVos){
			persTraceService.updatePersTraceFee(persTraceFeeVo);
		}
		return bussTaskId;
	}
	
	
	@Override
	public PrpLClaimTextVo saveOrSubmitPLEdit(PrpLDlossPersTraceMainVo prpLDlossPersTraceMainVo,
		   PrpLCheckDutyVo prpLCheckDutyVo,List<PrpLDlossChargeVo> prpLDlossChargeVos,
		PrpLClaimTextVo prpLClaimTextVo,SubmitNextVo submitNextVo,SysUserVo userVo) throws Exception {
		logger.info("报案号="+ (prpLDlossPersTraceMainVo == null? null:prpLDlossPersTraceMainVo.getRegistNo())+"进入保存或提交人伤跟踪回写prplcheckduty标志位ValidFlag的方法");
		logger.info("报案号="+ (prpLDlossPersTraceMainVo == null? null:prpLDlossPersTraceMainVo.getRegistNo())+"进入保存或提交人伤跟踪回写人伤核损标志位UnderwriteFlag的方法");
		Date date = new Date();
		String auditStatus = "";
		Double sumdefLoss = 0d;// 定损总金额
		WfTaskSubmitVo submitVo = new WfTaskSubmitVo();
		if(FlowNode.PLFirst.equals(submitNextVo.getCurrentNode())){//首次跟踪，则定损人和首次跟踪人信息一致
			if(prpLDlossPersTraceMainVo != null){
				prpLDlossPersTraceMainVo.setOperatorCertiCode(prpLDlossPersTraceMainVo.getPlfCertiCode());
				prpLDlossPersTraceMainVo.setOperatorName(prpLDlossPersTraceMainVo.getPlfName());
			}
		}
		if(submitNextVo.getAuditStatus()!=null&& !"".equals(submitNextVo.getAuditStatus())){// 提交
			auditStatus = submitNextVo.getAuditStatus();
			prpLClaimTextVo.setFlag("1");
		}else{// 暂存
			auditStatus = CodeConstants.AuditStatus.SAVE;
			prpLClaimTextVo.setFlag("0");
		}
		prpLDlossPersTraceMainVo.setAuditStatus(auditStatus);

		Long bussTaskId = prpLDlossPersTraceMainVo.getId();

		// 把跟踪人员赋值给主表
		String registNo = prpLDlossPersTraceMainVo.getRegistNo();
		List<PrpLDlossPersTraceVo> prpLDlossPersTraces = persTraceService.findPersTraceVo(registNo,bussTaskId);
		prpLDlossPersTraceMainVo.setPrpLDlossPersTraces(prpLDlossPersTraces);
		logger.info("报案号=" + (prpLDlossPersTraceMainVo == null? null:prpLDlossPersTraceMainVo.getRegistNo()) + "根据checkduty的id是否为空赋值ValidFlag，如果为空则赋值ValidFlag");
		// 保存交强险责任类型和事故责任比例
		if(prpLCheckDutyVo.getId()==null){
			prpLCheckDutyVo.setCreateUser(userVo.getUserCode());
			prpLCheckDutyVo.setCreateTime(date);
			logger.info("报案号=" + (prpLDlossPersTraceMainVo == null? null:prpLDlossPersTraceMainVo.getRegistNo()) + "赋值ValidFlag=" + ValidFlag.VALID);
			prpLCheckDutyVo.setValidFlag(ValidFlag.VALID);
		}else{
			prpLCheckDutyVo.setUpdateUser(userVo.getUserCode());
			prpLCheckDutyVo.setUpdateTime(date);
		}
		checkTaskService.saveCheckDuty(prpLCheckDutyVo);

		// 跟踪意见
		prpLClaimTextVo.setComCode(userVo.getComCode());
		String description = codeTranService.transCode("AuditStatus",auditStatus);
		prpLClaimTextVo.setDescription(description);
		prpLClaimTextVo.setStatus(auditStatus);
//		prpLClaimTextVo.setComName(userVo.getComName());
		prpLClaimTextVo.setBigNode(FlowNode.PLoss.name());
		if(prpLClaimTextVo.getId()==null){// 新增
			prpLClaimTextVo.setBussTaskId(bussTaskId);
			prpLClaimTextVo.setCreateUser(userVo.getUserCode());
			prpLClaimTextVo.setCreateTime(date);
			prpLClaimTextVo.setUpdateUser(userVo.getComCode());
			prpLClaimTextVo.setUpdateTime(date);
			prpLClaimTextVo.setOperatorCode(userVo.getUserCode());
			prpLClaimTextVo.setOperatorName(userVo.getUserName());
			prpLClaimTextVo.setInputTime(date);
		}else{
			prpLClaimTextVo.setUpdateUser(userVo.getComCode());
			prpLClaimTextVo.setUpdateTime(date);
		}
		
		//公估定损时新增一条公估信息
		if(CheckClass.CHECKCLASS_Y.equals(prpLDlossPersTraceMainVo.getIntermediaryFlag()) ){
			//判断是否有数据
			String diaryInfoId = prpLDlossPersTraceMainVo.getIntermediaryInfoId();
			PrpLAssessorVo assessorVo = assessorService.findAssessorByLossId(registNo,TaskType.PERS, null,diaryInfoId);
			if(assessorVo == null){
				assessorVo = new PrpLAssessorVo();
				PrpdIntermMainVo intermVo = managerService.findIntermByUserCode(userVo.getUserCode());
				if(intermVo != null){
					assessorVo.setIntermId(intermVo.getId());
					assessorVo.setIntermNameDetail(intermVo.getIntermName());
				}else{
					assessorVo.setIntermId(null);
					assessorVo.setIntermNameDetail("接收人和处理人不一致，且处理人不是公估人员");
				}
				PrpLRegistVo registVo = registQueryService.findByRegistNo(prpLDlossPersTraceMainVo.getRegistNo());
				if(prpLDlossPersTraceMainVo.getPrpLDlossPersTraces()!=null && prpLDlossPersTraceMainVo.getPrpLDlossPersTraces().size()>0){
					PrpLDlossPersTraceVo persTraceVo = prpLDlossPersTraceMainVo.getPrpLDlossPersTraces().get(0);
					if("1".equals(persTraceVo.getPrpLDlossPersInjured().getLossItemType())){//车外人员
						if("2".equals(registVo.getReportType())){
							assessorVo.setKindCode("BZ");
						}else{
							assessorVo.setKindCode("B");
						}
					}else if("2".equals(persTraceVo.getPrpLDlossPersInjured().getLossItemType())){//本车乘客
						assessorVo.setKindCode("D12");
					}else if("3".equals(persTraceVo.getPrpLDlossPersInjured().getLossItemType())){//本车司机
						assessorVo.setKindCode("D11");
					}
				}else{
					if("2".equals(registVo.getReportType())){
						assessorVo.setKindCode("BZ");
					}else{
						assessorVo.setKindCode("B");
					}
				}
				assessorVo.setRegistNo(registNo);
				assessorVo.setLossId(prpLDlossPersTraceMainVo.getId());
				assessorVo.setTaskType(TaskType.PERS);
				assessorVo.setLossDate(date);
				assessorVo.setIntermcode(diaryInfoId);
				assessorVo.setIntermname(codeTranService.transCode("GongGuPayCode",diaryInfoId));
				assessorVo.setUnderWriteFlag(AssessorUnderWriteFlag.Loss);
				assessorVo.setCreateTime(date);
				assessorVo.setCreateUser(userVo.getUserCode());
				
				assessorVo.setComCode(userVo.getComCode());
				BigDecimal veriLoss = new BigDecimal("0");
				if(prpLDlossPersTraces != null){
					for(PrpLDlossPersTraceVo prpLDlossPersTrace : prpLDlossPersTraces ){
						if("1".equals(prpLDlossPersTrace.getValidFlag())){
							veriLoss = veriLoss.add(DataUtils.NullToZero(prpLDlossPersTrace.getSumVeriDefloss()));
						}
					}
				}
				assessorVo.setAssessorFee(prpLDlossPersTraceMainVo.getAssessorFee());
				assessorVo.setVeriLoss(veriLoss);
			}else{
				assessorVo.setUpdateUser(userVo.getUserCode());
				assessorVo.setUpdateTime(date);
			}
			assessorService.saveOrUpdatePrpLAssessor(assessorVo,userVo);
		}
		//人伤查勘费的生成，subPLNext
		if(FlowNode.PLFirst.name().equals(submitNextVo.getCurrentNode()) || FlowNode.PLNext.name().equals(submitNextVo.getCurrentNode())){
			acheckTaskService.addCheckFeeTaskOfDpers(prpLDlossPersTraceMainVo, userVo, "0");
		}
		
		
		
		// 保存费用明细
		for(PrpLDlossChargeVo lossChargeVo:prpLDlossChargeVos){
			if(lossChargeVo.getBusinessId()==null){
				lossChargeVo.setBusinessId(bussTaskId);
			}
		}
		// 提交到工作流
		PrpLWfTaskVo taskVo = wfTaskHandleService.findTaskIn(Double.parseDouble(submitNextVo.getFlowTaskId()));
		if(taskVo==null){
			throw new Exception("该任务已经提交，不能继续处理！");
		}
		if(StringUtils.isNotBlank(submitNextVo.getAuditStatus())){// 提交
			Long traceMainId = prpLDlossPersTraceMainVo.getId();
			List<PrpLDlossPersTraceVo> persTraceVos = persTraceService.findPersTraceVo(registNo,traceMainId);
			if(persTraceVos!=null&&persTraceVos.size()>0){
				for(PrpLDlossPersTraceVo persTraceVo:persTraceVos){
					
					// 提交到人伤后续跟踪保存费用修改记录
					if("PLNext".equalsIgnoreCase(submitNextVo.getNextNode())){
						persTraceVo.setFlag("1");
					}else{
						persTraceVo.setFlag("0");
					}
					persTraceVo.setCreateTime(date);
					persTraceVo.setOperatorCode(userVo.getUserCode());
					persTraceVo.setOperatorTime(date);
					persTraceVo.setOperatorDesc(prpLClaimTextVo.getRemark());
					persTraceService.savePersTraceHis(persTraceVo);
					
					BigDecimal sumLoss = persTraceVo.getSumdefLoss();
					if(sumLoss==null){
						sumLoss = persTraceVo.getSumReportFee();
					}
					sumdefLoss += DataUtils.NullToZero(sumLoss).doubleValue();// 计算总定损金额
					persTraceVo.getSumReportFee();
				}
			}
			//审核级别金额加上费用金额
			sumdefLoss += DataUtils.NullToZero(prpLDlossPersTraceMainVo.getSumChargeFee()).doubleValue();

			String comCode = userVo.getComCode();
			//如果是公估案件，分配给承保地人员，否则取任务接收人的结构
			if("3".equals(taskVo.getSubCheckFlag())){
				comCode = registQueryService.findByRegistNo(registNo).getComCode();
			}
			
			prpLDlossPersTraceMainVo.setVerifyLevel(submitNextVo.getSubmitLevel());
			prpLDlossPersTraceMainVo.setMaxLevel(submitNextVo.getMaxLevel());
			submitVo.setNextNode(FlowNode.valueOf(submitNextVo.getNextNode()));
			//处理人
			if(AuditStatus.SUBPLNEXT.equalsIgnoreCase(auditStatus)){// 提交后续跟踪任务提交到自己
				submitVo.setAssignUser(userVo.getUserCode());
				submitVo.setAssignCom(userVo.getComCode());
			}else if(!FlowNode.PLCharge_LV0.name().equals(submitNextVo.getNextNode())){
				setAssignUser(registNo,traceMainId.toString(),submitVo,comCode,userVo);
			}

			submitVo.setTaskInKey(bussTaskId.toString());
			submitVo.setTaskInUser(userVo.getUserCode());
			submitVo.setFlowId(taskVo.getFlowId());
			submitVo.setFlowTaskId(taskVo.getTaskId());
			submitVo.setCurrentNode(FlowNode.valueOf(submitNextVo.getCurrentNode()));
			submitVo.setComCode(policyViewService.getPolicyComCode(registNo));
//			submitVo.setHandleruser(userVo.getUserCode());
			submitVo.setHandlertime(date);
			
			// 大案审核提交
			if(submitNextVo.getOtherNodes()!=null&&submitNextVo.getOtherNodes().length()>0){
				FlowNode[] otherNodes = new FlowNode[1];
				otherNodes[0] = FlowNode.valueOf(submitNextVo.getOtherNodes());
				submitVo.setOthenNodes(otherNodes);
			}
			
			List<PrpLWfTaskVo> taskInVoList = wfTaskHandleService.submitLossPerson(prpLDlossPersTraceMainVo,
					submitVo);// 提交人伤任务
			
			//人伤后续跟踪需要调度给移动端
			try {
				//判断是否为移动端案件
				PrpLWfMainVo wfmainVo = wfMainService.findPrpLWfMainVoByRegistNo(registNo);
				PrpLWfTaskVo taskVos = wfTaskHandleService.queryTask(Double.valueOf(submitNextVo.getFlowTaskId()));
				PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
				String userCode = taskVos.getAssignUser() != null ? taskVos.getAssignUser() : "0"; 
				if("1".equals(wfmainVo.getIsMobileCase()) || sendMsgToMobileService.isMobileCase(registVo, userCode)) {
					if("PLNext".equalsIgnoreCase(submitNextVo.getNextNode())){
						this.setReassignments(prpLDlossPersTraceMainVo, submitVo);
					}
				}
			}catch(Exception e) {
				logger.info("移动查勘返回数据失败------>"+ e);
			}
			
			// 自动审核提交
			if(FlowNode.PLCharge_LV0.name().equals(submitNextVo.getNextNode())){
				logger.info("工作流节点flowNode={},则赋值人伤核损标志位={}",FlowNode.PLCharge_LV0.name(),PersVeriFlag.CHARGEPASS);
				prpLDlossPersTraceMainVo.setUnderwriteFlag(PersVeriFlag.CHARGEPASS);
				prpLDlossPersTraceMainVo.setUndwrtFeeCode(userVo.getUserCode());
				prpLDlossPersTraceMainVo.setUndwrtFeeName(userVo.getUserName());
				prpLDlossPersTraceMainVo.setUndwrtFeeEndDate(new Date());
				prpLDlossPersTraceMainVo.setAuditStatus(AuditStatus.SUBMITCHARGE);
				prpLDlossPersTraceMainVo.setVerifyCertiCode(userVo.getIdentifyNumber());
				
				if(prpLDlossPersTraces!=null && prpLDlossPersTraces.size()>0){
					for(PrpLDlossPersTraceVo persTraceVo:prpLDlossPersTraces){
						persTraceVo.setSumVeriReportFee(persTraceVo.getSumReportFee());
						persTraceVo.setSumVeriRealFee(persTraceVo.getSumRealFee());
						persTraceVo.setSumVeriDetractionFee(persTraceVo.getSumDetractionFee());
						persTraceVo.setSumVeriDefloss(persTraceVo.getSumdefLoss());
						List<PrpLDlossPersTraceFeeVo> traceFeeVoList = persTraceVo.getPrpLDlossPersTraceFees();
						if(traceFeeVoList!=null && traceFeeVoList.size()>0){
							for(PrpLDlossPersTraceFeeVo traceFeeVo:traceFeeVoList){
								traceFeeVo.setVeriUnitAmount(traceFeeVo.getUnitAmount());
								traceFeeVo.setVeriWoundRate(traceFeeVo.getWoundRate());
								traceFeeVo.setVeriQuantity(traceFeeVo.getQuantity());
								traceFeeVo.setVeriReportFee(traceFeeVo.getReportFee());
								traceFeeVo.setVeriRealFee(traceFeeVo.getRealFee());
								traceFeeVo.setVeriDetractionFee(traceFeeVo.getDetractionfee());
								traceFeeVo.setVeriDefloss(traceFeeVo.getDefloss());
								traceFeeVo.setVeriRemark(traceFeeVo.getRemark());
							}
						}
						saveCasualtyInfo(persTraceVo, userVo);
					}
				}
				prpLDlossPersTraceMainVo.setPrpLDlossPersTraces(prpLDlossPersTraces);
				
				if(prpLDlossChargeVos!=null && prpLDlossChargeVos.size()>0){
					for(PrpLDlossChargeVo chargeVo:prpLDlossChargeVos){
						chargeVo.setVeriChargeFee(chargeVo.getChargeFee());
					}
				}

				submitVo.setTaskInKey(bussTaskId.toString());
				submitVo.setTaskInUser("AUTO");
				submitVo.setComCode(userVo.getComCode());
				submitVo.setNextNode(FlowNode.END);
				submitVo.setFlowId(taskInVoList.get(0).getFlowId());
				submitVo.setFlowTaskId(taskInVoList.get(0).getTaskId());
				submitVo.setCurrentNode(FlowNode.PLCharge_LV0);
				submitVo.setAssignCom(userVo.getComCode());
				submitVo.setOthenNodes(null);
				prpLDlossPersTraceMainVo.setUndwrtFeeCode("AUTO");
				prpLDlossPersTraceMainVo.setUndwrtFeeName("AUTO");
				wfTaskHandleService.submitLossPerson(prpLDlossPersTraceMainVo,submitVo);// 提交人伤任务
				PrpLDlossPersTraceMainVo traceMainVo=persTraceService.findPersTraceMainVoById(prpLDlossPersTraceMainVo.getId());
				prpLDlossPersTraceMainVo.setCheckCode(traceMainVo.getCheckCode());
				 //人伤审核通过写会查勘费审核通过标志 查勘费用 核损金额
				if(StringUtils.isNotBlank(prpLDlossPersTraceMainVo.getCheckCode())){
					 //人伤审核通过写会查勘费审核通过标志 查勘费用 核损金额
					if(StringUtils.isNotBlank(prpLDlossPersTraceMainVo.getCheckCode())){
						acheckTaskService.addCheckFeeTaskOfDpers(prpLDlossPersTraceMainVo, userVo,"1");
					}
				}
				
			}
			BigDecimal traceTimes = DataUtils.NullToZero(prpLDlossPersTraceMainVo.getTraceTimes()).add(new BigDecimal("1"));
			prpLDlossPersTraceMainVo.setTraceTimes(traceTimes);
		}else{
			wfTaskHandleService.tempSaveTask(Double.parseDouble(submitNextVo.getFlowTaskId()),
					prpLDlossPersTraceMainVo.getId().toString(),userVo.getUserCode(),
					userVo.getComCode());
		}
		lossChargeService.saveOrUpdte(prpLDlossChargeVos);
		prpLDlossPersTraceMainVo = persTraceService.calculateSumAmt(prpLDlossPersTraceMainVo);
		Long persTraceMainId = persTraceService.saveOrUpdatePersTraceMain(prpLDlossPersTraceMainVo);
		
		//需求--人伤意见列表，增加历次提交、上报或审核通过金额显示。
		setSumLossFeeToText(persTraceService.findPersTraceMainVoById(persTraceMainId),prpLClaimTextVo,RadioValue.RADIO_NO);
		claimTextSerVice.saveOrUpdte(prpLClaimTextVo);
		logger.info("报案号=" + (prpLDlossPersTraceMainVo == null? null:prpLDlossPersTraceMainVo.getRegistNo()) + "结束保存或提交人伤跟踪回写prplcheckduty标志位ValidFlag的方法");
		logger.info("报案号=" + (prpLDlossPersTraceMainVo == null? null:prpLDlossPersTraceMainVo.getRegistNo()) + "结束保存或提交人伤跟踪回写人伤核损标志位UnderwriteFlag的方法");
		return prpLClaimTextVo;
	}
	
	//flowType 0-跟踪 ，1-审核
	private void setSumLossFeeToText(PrpLDlossPersTraceMainVo persTrace, PrpLClaimTextVo prpLClaimTextVo ,String flowType){
		PrpLDlossPersTraceMainVo persTraceMainVo = persTraceService.calculateSumAmt(persTrace);
		
		String sumReportFee = StringUtils.isEmpty(persTraceMainVo.getSumReportFee()) 
				? "0" : persTraceMainVo.getSumReportFee();//总估损金额
		String sumdefLoss = StringUtils.isEmpty(persTraceMainVo.getSumdefLoss()) 
				? "0" : persTraceMainVo.getSumdefLoss();//总核定损金额
		
		String sumVeriReportFee = StringUtils.isEmpty(persTraceMainVo.getSumVeriReportFee()) 
				? "0" : persTraceMainVo.getSumVeriReportFee();//总估损金额(审核)
		String sumVeriDefloss = StringUtils.isEmpty(persTraceMainVo.getSumVeriDefloss()) 
				? "0" : persTraceMainVo.getSumVeriDefloss();//总核定损金额(审核)
		
		BigDecimal sumLoss = new BigDecimal(0);
		if ( RadioValue.RADIO_NO.equals(flowType) ) {//0-跟踪 
			if ( StringUtils.isNotBlank(sumdefLoss) && !sumdefLoss.startsWith("0") ) {
				sumLoss = sumLoss.add(DataUtils.NullToZero(new BigDecimal(sumdefLoss)));
			} else {
				sumLoss = sumLoss.add(DataUtils.NullToZero(new BigDecimal(sumReportFee)));
			}
		} else { //1-审核
			if ( StringUtils.isNotBlank(sumVeriDefloss) && !sumVeriDefloss.startsWith("0") ) {
				sumLoss = sumLoss.add(DataUtils.NullToZero(new BigDecimal(sumVeriDefloss)));
			} else {
				sumLoss = sumLoss.add(DataUtils.NullToZero(new BigDecimal(sumVeriReportFee)));
			}
		}
		prpLClaimTextVo.setSumLossFee(sumLoss);
	}
	
	private WfTaskSubmitVo setAssignUser(String registNo,String traceMainId,WfTaskSubmitVo submitVo,String comCode,SysUserVo userVo) throws Exception{
		PrpLWfTaskVo oldTaskVo = findTaskInByFirst(registNo,traceMainId.toString(),submitVo.getNextNode());
		if(oldTaskVo != null){
			boolean isHoliday = assignService.validUserCode(oldTaskVo.getHandlerUser());
			if(isHoliday){
				submitVo.setAssignUser(oldTaskVo.getHandlerUser());
				submitVo.setAssignCom(oldTaskVo.getHandlerCom());
			}else{
				//处理人轮询
				setAssign(submitVo, comCode,userVo);
			}
		}else{
			//处理人轮询
			setAssign(submitVo, comCode,userVo);
		}
		return submitVo;
	}
	
	//处理人轮询
	private void setAssign(WfTaskSubmitVo submitVo,String comCode,SysUserVo userVo){
		SysUserVo assignUserVo = assignService.execute(submitVo.getNextNode(),comCode,userVo, "0");
		if(assignUserVo == null){
			throw new IllegalArgumentException(submitVo.getNextNode().getName()+"未配置人员！");
		}
		submitVo.setAssignUser(assignUserVo.getUserCode());
		submitVo.setAssignCom(assignUserVo.getComCode());
	}
	
	@Override
	public String saveOrSubmitPLBig(PrpLDlossPersTraceMainVo prpLDlossPersTraceMainVo,SubmitNextVo submitNextVo,SysUserVo userVo) throws Exception {
		PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(Double.parseDouble(submitNextVo.getFlowTaskId()));

		WfSimpleTaskVo taskVo = new WfSimpleTaskVo();
		taskVo.setRegistNo(wfTaskVo.getRegistNo());
		taskVo.setHandlerIdKey(wfTaskVo.getHandlerIdKey());// 人伤主表id
		taskVo.setItemName(wfTaskVo.getItemName());

		String auditStatus = "";// 提交动作
		if( !StringUtils.isBlank(submitNextVo.getAuditStatus())){// 提交
			auditStatus = submitNextVo.getAuditStatus();
		}else{// 暂存
			auditStatus = CodeConstants.AuditStatus.SAVE;
		}
		Long bussTaskId = persTraceService.saveOrUpdatePersTraceMain(prpLDlossPersTraceMainVo);

		// 跟踪意见
		PrpLClaimTextVo prpLClaimTextVo = new PrpLClaimTextVo();
		prpLClaimTextVo.setRegistNo(wfTaskVo.getRegistNo());
		prpLClaimTextVo.setComCode(userVo.getComCode());
		prpLClaimTextVo.setDescription(prpLDlossPersTraceMainVo.getMajorcaseOption());
		prpLClaimTextVo.setRemark(prpLDlossPersTraceMainVo.getMajorcaseOption());
		prpLClaimTextVo.setStatus(auditStatus);
		prpLClaimTextVo.setTextType("1");
//		prpLClaimTextVo.setComName(userVo.getComName());
		prpLClaimTextVo.setFlag(CodeConstants.ValidFlag.VALID);
		prpLClaimTextVo.setBigNode(FlowNode.PLBig.name());
		prpLClaimTextVo.setNodeCode(submitNextVo.getCurrentNode());
		if(prpLClaimTextVo.getId()==null){// 新增
			prpLClaimTextVo.setBussTaskId(bussTaskId);
			prpLClaimTextVo.setCreateUser(userVo.getUserCode());
			prpLClaimTextVo.setCreateTime(new Date());
			prpLClaimTextVo.setUpdateUser(userVo.getComCode());
			prpLClaimTextVo.setUpdateTime(new Date());
			prpLClaimTextVo.setOperatorCode(userVo.getUserCode());
			prpLClaimTextVo.setOperatorName(userVo.getUserName());
			prpLClaimTextVo.setInputTime(new Date());
		}else{
			prpLClaimTextVo.setUpdateUser(userVo.getComCode());
			prpLClaimTextVo.setUpdateTime(new Date());
		}
		
		if(submitNextVo.getAuditStatus()!=null&& !"".equals(submitNextVo.getAuditStatus())){// 提交
			prpLClaimTextVo.setFlag("1");
		}else{// 暂存
			prpLClaimTextVo.setFlag("0");
		}

		// 提交到工作流
		if(submitNextVo.getAuditStatus()!=null&& !"".equals(submitNextVo.getAuditStatus())){// 提交

			WfTaskSubmitVo submitVo = new WfTaskSubmitVo();

			submitVo.setTaskInKey(bussTaskId.toString());
			submitVo.setTaskInUser(userVo.getUserCode());
			submitVo.setFlowId(wfTaskVo.getFlowId());
			submitVo.setFlowTaskId(wfTaskVo.getTaskId());
//			submitVo.setHandleruser(userVo.getUserCode());
//			submitVo.setHandlertime(new Date());
			
			submitVo.setCurrentNode(FlowNode.valueOf(submitNextVo.getCurrentNode()));
			submitVo.setComCode(policyViewService.getPolicyComCode(wfTaskVo.getRegistNo()));
			submitVo.setNextNode(FlowNode.valueOf(submitNextVo.getNextNode()));
			submitVo.setAssignUser(null);
			submitVo.setAssignCom(submitNextVo.getAssignCom());
			wfTaskHandleService.submitSimpleTask(taskVo,submitVo);
			claimTextSerVice.saveOrUpdte(prpLClaimTextVo);
		}else{
			wfTaskHandleService.tempSaveTask(Double.parseDouble(submitNextVo.getFlowTaskId()),prpLDlossPersTraceMainVo.getId().toString(),userVo.getUserCode(),userVo.getComCode());
		}
		return auditStatus;
	}
	
	@Override
	public void ActiveOrCancelPersTrace(String id,String validFlag) throws Exception {
		PrpLDlossPersTraceVo persTraceVo = persTraceService.findPersTraceVo(Long.decode(id));
		persTraceVo.setValidFlag(validFlag);
		persTraceService.AvtiveOrCanCelPersTrace(persTraceVo);
	}
	
	//新需求：人伤跟踪审核或费用审核通过，（人伤若有总公司审核通过后，那么下次的审核一定要经过总公司且只能总公司审核通过）
	private String setPersTraceMainToRemark(String currentNode,String auditStatus) {
		String remark = "0";
		if (AuditStatus.SUBMITVERIFY.equalsIgnoreCase(auditStatus) || AuditStatus.SUBMITCHARGE.equalsIgnoreCase(auditStatus)) {
			if (StringUtils.isNotBlank(currentNode) && (currentNode.startsWith("PLVerify") || currentNode.startsWith("PLCharge"))) {
				String[] nodeArray = currentNode.split("_LV");
				if (nodeArray != null && nodeArray[1] != null) {
					Long currentNodeLV = Long.parseLong(nodeArray[1]);
					if (currentNodeLV != null && currentNodeLV >= 9) {// 总公司大于等于九级
						remark = "1";// 总公司审核处理记录
					}
				}
			}
		}
		return remark;
	}
	
	@Override
	public String saveOrSubmitPLVerify(PrpLDlossPersTraceMainVo prpLDlossPersTraceMainVo,
										List<PrpLDlossPersTraceVo> prpLDlossPersTraceVos,
										List<PrpLDlossChargeVo> prpLDlossChargeVos,PrpLClaimTextVo prpLClaimTextVo,
										SubmitNextVo submitNextVo,SysUserVo userVo) throws Exception {
		logger.info("报案号={},进入保存或提交人伤审核环节回写标志位UnderwriteFlag的方法",prpLDlossPersTraceMainVo.getRegistNo());
		PrpLWfTaskVo taskVo = wfTaskHandleService.findTaskIn(Double.parseDouble(submitNextVo.getFlowTaskId()));
		if(taskVo==null){
			throw new Exception("该任务已经提交，不能继续处理！");
		}
		

		String auditStatus = "";// 提交动作
		if( !StringUtils.isBlank(submitNextVo.getAuditStatus())){// 提交
			auditStatus = submitNextVo.getAuditStatus();
		}else{// 暂存
			auditStatus = CodeConstants.AuditStatus.SAVE;
		}
		if(AuditStatus.SUBMITVERIFY.equalsIgnoreCase(auditStatus)){// 跟踪审核通过
			logger.info("报案号="+ prpLDlossPersTraceMainVo.getRegistNo() +",根据审核状态auditStatus="+ auditStatus +",赋值UnderwriteFlag={}",PersVeriFlag.VERIFYPASS);
			prpLDlossPersTraceMainVo.setUnderwriteFlag(PersVeriFlag.VERIFYPASS);
			prpLDlossPersTraceMainVo.setUnderwriteCode(userVo.getUserCode());
			prpLDlossPersTraceMainVo.setUnderwriteName(userVo.getUserName());
			prpLDlossPersTraceMainVo.setUnderwriteEndDate(new Date());
		}else if(AuditStatus.SUBMITCHARGE.equalsIgnoreCase(auditStatus)){
			logger.info("根据审核状态auditStatus={},赋值UnderwriteFlag={}",auditStatus,PersVeriFlag.CHARGEPASS);
			prpLDlossPersTraceMainVo.setUnderwriteFlag(PersVeriFlag.CHARGEPASS);
			prpLDlossPersTraceMainVo.setUndwrtFeeCode(userVo.getUserCode());
			prpLDlossPersTraceMainVo.setUndwrtFeeName(userVo.getUserName());
			prpLDlossPersTraceMainVo.setUndwrtFeeEndDate(new Date());
			
			for(PrpLDlossChargeVo chargeVo : prpLDlossChargeVos){//费用审核通过，费用标识
				chargeVo.setStatus("1");
			}
		}
		prpLDlossPersTraceMainVo.setAuditStatus(auditStatus);

		// 保存页面数据
		lossChargeService.saveOrUpdte(prpLDlossChargeVos);
		prpLDlossPersTraceMainVo.setPrpLDlossPersTraces(prpLDlossPersTraceVos);
		//记录人伤跟踪审核通过
		if(!RadioValue.RADIO_YES.equals(prpLDlossPersTraceMainVo.getRemark())){//remark为空或者为0时，设值
			prpLDlossPersTraceMainVo.setRemark(setPersTraceMainToRemark(submitNextVo.getCurrentNode(),auditStatus));
		}
		Long bussTaskId = persTraceService.saveOrUpdatePersTraceMain(prpLDlossPersTraceMainVo);
		PrpLDlossPersTraceMainVo persTraceMainVo = persTraceService.findPersTraceMainVoById(prpLDlossPersTraceMainVo.getId());

		for(PrpLDlossPersTraceVo perTraceVo:prpLDlossPersTraceVos){
			persTraceService.updatePersTraceVeriSumAmount(perTraceVo);// 保存金额统计-审核金额(不排空)
			for(PrpLDlossPersTraceFeeVo feeVo:perTraceVo.getPrpLDlossPersTraceFees()){
				if(AuditStatus.SUBMITCHARGE.equals(auditStatus)){//费用审核通过
					feeVo.setStatus("1");//费用审核通过
				}
				//下个节点是END或后续跟踪则重置费用修改标识
				if("PLNext".equalsIgnoreCase(submitNextVo.getNextNode())||"END".equalsIgnoreCase(submitNextVo.getNextNode())){
					feeVo.setModifyFlag(ModifyFlag.NONE);
				}
				persTraceService.updatePersTraceVeriFee(feeVo);// 保存本次跟踪记录-审核金额(不排空)
			}
		}

		// 跟踪意见
		prpLClaimTextVo.setComCode(userVo.getComCode());
		String description = codeTranService.transCode("AuditOpinion",prpLClaimTextVo.getOpinionCode());
		prpLClaimTextVo.setDescription(description);
		prpLClaimTextVo.setStatus(auditStatus);
		prpLClaimTextVo.setTextType("1");
//		prpLClaimTextVo.setComName(userVo.getComName());
		prpLClaimTextVo.setFlag(CodeConstants.ValidFlag.VALID);
		prpLClaimTextVo.setBigNode(FlowNode.PLoss.name());
		if(prpLClaimTextVo.getId()==null){// 新增
			prpLClaimTextVo.setBussTaskId(bussTaskId);
			prpLClaimTextVo.setCreateUser(userVo.getUserCode());
			prpLClaimTextVo.setCreateTime(new Date());
			prpLClaimTextVo.setUpdateUser(userVo.getComCode());
			prpLClaimTextVo.setUpdateTime(new Date());
			prpLClaimTextVo.setOperatorCode(userVo.getUserCode());
			prpLClaimTextVo.setOperatorName(userVo.getUserName());
			prpLClaimTextVo.setInputTime(new Date());
		}else{
			prpLClaimTextVo.setUpdateUser(userVo.getComCode());
			prpLClaimTextVo.setUpdateTime(new Date());
		}

		// 提交到工作流
		if(submitNextVo.getAuditStatus()!=null&& !"".equals(submitNextVo.getAuditStatus())){// 提交
			prpLClaimTextVo.setFlag("1");

			WfTaskSubmitVo submitVo = new WfTaskSubmitVo();

			if("backPLNext".equals(submitNextVo.getAuditStatus())){
				submitVo.setSubmitType(SubmitType.B);
				submitVo.setHandleIdKey(bussTaskId.toString());
			}
			//审核环节不新增轨迹，只修改轨迹
			List<PrpLDlossPersTraceHisVo> persTraceHisVos = persTraceService.findPersTraceHisVo(prpLDlossPersTraceMainVo.getId(),"0");
			if(persTraceHisVos != null && persTraceHisVos.size() > 0){
				for(PrpLDlossPersTraceHisVo persTraceHisVo : persTraceHisVos){
					persTraceHisVo.setUndwrtCode(userVo.getUserCode());
					persTraceHisVo.setUndwrtTime(new Date());
					persTraceHisVo.setUndwrtDesc(prpLClaimTextVo.getRemark());
					if("PLNext".equals(submitVo.getNextNode())||"END".equals(submitVo.getNextNode())){
						persTraceHisVo.setFlag("1");
					}
					persTraceService.updatePersTraceHis(persTraceHisVo);
				}
			}
			
			// 提交到上级或退回下级保存跟踪记录轨迹

			submitVo.setTaskInKey(bussTaskId.toString());
			submitVo.setTaskInUser(userVo.getUserCode());
			submitVo.setFlowId(taskVo.getFlowId());
			submitVo.setFlowTaskId(taskVo.getTaskId());
			submitVo.setCurrentNode(FlowNode.valueOf(submitNextVo.getCurrentNode()));
			submitVo.setNextNode(FlowNode.valueOf(submitNextVo.getNextNode()));
//			submitVo.setHandleruser(userVo.getUserCode());
			submitVo.setHandlertime(new Date());
			
			String registNo = persTraceMainVo.getRegistNo();
			String traceMainId = persTraceMainVo.getId().toString();
			submitVo.setComCode(policyViewService.getPolicyComCode(registNo));
			if(AuditStatus.SUBMITVERIFY.equalsIgnoreCase(auditStatus)||AuditStatus.BACKPLNEXT.equals(auditStatus)){
				//审核通过-->人伤后续跟踪（查找最近的一个人伤跟踪处理人员）
				PrpLWfTaskVo endTaskVo = findTaskIn(registNo,traceMainId,FlowNode.PLNext);
				submitVo.setAssignUser(endTaskVo.getHandlerUser());
				submitVo.setAssignCom(endTaskVo.getHandlerCom());
			}else if(!AuditStatus.SUBMITCHARGE.equals(auditStatus)){//费用审核通过不轮询
				StringBuffer nodeLevel = new StringBuffer(submitVo.getNextNode().toString());
				String comCode = userVo.getComCode();
				nodeLevel.delete(0, 11);
				if(nodeLevel.toString() != null && !"".equals(nodeLevel.toString())){
					int level = Integer.valueOf(nodeLevel.toString());//提交的等级
					if(level >= 9){
						comCode ="00010000";//总公司人员 机构用公司本部
					}
				}
				if(AuditStatus.BACKLOWER.equals(auditStatus)){//查询退回的处理人员
					PrpLWfTaskVo oldTaskVo = findTaskIn(registNo,traceMainId,submitVo.getNextNode());
					submitVo.setAssignUser(oldTaskVo.getHandlerUser());
					submitVo.setAssignCom(oldTaskVo.getHandlerCom());
				}else{
					if(CodeConstants.TOPCOM.equals(comCode)){//总公司不轮询
						submitVo.setAssignCom(CodeConstants.TOPCOM);
					}else{
//						boolean havUser =assignService.existsGradeUser(submitVo.getNextNode(), comCode);
//						if(!havUser){
//							throw new IllegalArgumentException(submitVo.getNextNode().getName()+"节点没有配置人员,请选择其他级别！");
//						}
						
						SysUserVo assignUserVo = assignService.execute(submitVo.getNextNode(),comCode,userVo, "");
						if(assignUserVo==null){
							if(Integer.valueOf(nodeLevel.toString())==persTraceMainVo.getMaxLevel()){
								String exception = "操作失败："+submitVo.getNextNode().getName()+"节点没有配置人员！";
//								if(Integer.valueOf(nodeLevel.toString())>=9){
//									exception = "操作失败：已经是最高级，不能提交上级！";
//								}
								throw new IllegalArgumentException(exception);
							}else{
								throw new IllegalArgumentException(submitVo.getNextNode().getName()+"节点没有配置人员,请选择其他级别！");
							}
						}
						submitVo.setAssignUser(assignUserVo.getUserCode());
						submitVo.setAssignCom(assignUserVo.getComCode());
					}
					
				}
			}
			wfTaskHandleService.submitLossPerson(persTraceMainVo,submitVo);// 提交人伤任务
			
			//人伤跟踪审核/费用审核退回后续跟踪不推送到移动端
			if(AuditStatus.BACKPLNEXT.equals(auditStatus)) {
				
			}else{
				//跟踪审核提交后,后续跟踪节点需要调度给移动端
				try {
					//判断是否为移动端案件
					PrpLWfMainVo wfmainVo = wfMainService.findPrpLWfMainVoByRegistNo(registNo);
					PrpLWfTaskVo taskVos = wfTaskHandleService.queryTask(Double.valueOf(submitNextVo.getFlowTaskId()));
					PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
					String userCode = taskVos.getAssignUser() != null ? taskVos.getAssignUser() : "0"; 
					if("1".equals(wfmainVo.getIsMobileCase()) || sendMsgToMobileService.isMobileCase(registVo, userCode)) {
						if("PLNext".equalsIgnoreCase(submitNextVo.getNextNode())){
							this.setReassignments(prpLDlossPersTraceMainVo, submitVo);
						}
					}
				}catch(Exception e) {
					logger.info("移动查勘返回数据失败------>"+e);
				}
			}

			if(AuditStatus.SUBMITVERIFY.equals(auditStatus)||AuditStatus.SUBMITCHARGE.equals(auditStatus)){// 审核通过时提交立案刷新
				
				//人伤审核通过写会公估费审核通过标志 公估费用 核损金额
				if(CheckClass.CHECKCLASS_Y.equals(persTraceMainVo.getIntermediaryFlag()) ){
					//判断是否有数据
					PrpLAssessorVo assessorVo = assessorService.findAssessorByLossId(persTraceMainVo.getRegistNo(), TaskType.PERS,persTraceMainVo.getIntermediaryInfoId());
					PrpLRegistVo registVo = registQueryService.findByRegistNo(persTraceMainVo.getRegistNo());
					if(persTraceMainVo.getPrpLDlossPersTraces()!=null && persTraceMainVo.getPrpLDlossPersTraces().size()>0){
						PrpLDlossPersTraceVo persTraceVo = persTraceMainVo.getPrpLDlossPersTraces().get(0);
						if("1".equals(persTraceVo.getPrpLDlossPersInjured().getLossItemType())){//车外人员
							if("2".equals(registVo.getReportType())){
								assessorVo.setKindCode("BZ");
							}else{
								assessorVo.setKindCode("B");
							}
						}else if("2".equals(persTraceVo.getPrpLDlossPersInjured().getLossItemType())){//本车乘客
							assessorVo.setKindCode("D12");
						}else if("3".equals(persTraceVo.getPrpLDlossPersInjured().getLossItemType())){//本车司机
							assessorVo.setKindCode("D11");
						}
					}else{
						if("2".equals(registVo.getReportType())){
							assessorVo.setKindCode("BZ");
						}else{
							assessorVo.setKindCode("B");
						}
					}
					
					BigDecimal veriLoss = new BigDecimal("0");
					if(prpLDlossPersTraceVos != null){
						for(PrpLDlossPersTraceVo prpLDlossPersTrace : prpLDlossPersTraceVos ){
							if("1".equals(prpLDlossPersTrace.getValidFlag())){
								veriLoss = veriLoss.add(DataUtils.NullToZero(prpLDlossPersTrace.getSumVeriDefloss()));
							}
						}
					}
					assessorVo.setAssessorFee(prpLDlossPersTraceMainVo.getAssessorFee());
					assessorVo.setVeriLoss(veriLoss);
					assessorVo.setUnderWriteFlag(AssessorUnderWriteFlag.Verify);
					assessorVo.setUpdateUser(userVo.getUserCode());
					assessorVo.setUpdateTime(new Date());
					assessorService.saveOrUpdatePrpLAssessor(assessorVo,userVo);
				}
				//人伤节点完成送平台(注：需要根据保单的comCode来选择送不同的平台--需修改，)
				logger.debug("registNo:"+persTraceMainVo.getRegistNo());
				//interfaceAsyncService.sendLossToPlatform(persTraceMainVo.getRegistNo(),null);
				lossToPlatformService.sendLossToPlatform(persTraceMainVo.getRegistNo(),null);
				
				if(PersVeriFlag.CHARGEADJUST.equals(persTraceMainVo.getFlag()) 
						&& AuditStatus.SUBMITCHARGE.equals(persTraceMainVo.getAuditStatus())){
					lossCarService.modifyToSubMitComp(persTraceMainVo.getRegistNo(),userVo);
				}
			}
		}else{
			prpLClaimTextVo.setFlag("0");
			wfTaskHandleService.tempSaveTask(Double.parseDouble(submitNextVo.getFlowTaskId()),persTraceMainVo.getId().toString(),userVo.getUserCode(),userVo.getComCode());
		}
		setSumLossFeeToText(persTraceMainVo,prpLClaimTextVo,RadioValue.RADIO_YES);
		claimTextSerVice.saveOrUpdte(prpLClaimTextVo);
		//人伤审核通过写会查勘费审核通过标志 查勘费用 核损金额
		if(StringUtils.isNotBlank(persTraceMainVo.getCheckCode()) && AuditStatus.SUBMITCHARGE.equals(auditStatus)){
			 //人伤审核通过写会查勘费审核通过标志 查勘费用 核损金额
			if(StringUtils.isNotBlank(persTraceMainVo.getCheckCode())){
				prpLDlossPersTraceMainVo.setCheckCode(persTraceMainVo.getCheckCode());
				acheckTaskService.addCheckFeeTaskOfDpers(prpLDlossPersTraceMainVo, userVo,"1");
			}
		}
		logger.info("registno={},结束保存或提交人伤审核环节回写标志位UnderwriteFlag的方法",prpLDlossPersTraceMainVo.getRegistNo());
		return auditStatus;
	}
	
	private PrpLWfTaskVo findTaskInByFirst(String registNo,String traceMainId,FlowNode nextNode){
		PrpLWfTaskVo oldTaskVo = null;
		List<PrpLWfTaskVo> endTaskVoList = wfTaskHandleService.findEndTask(registNo,traceMainId,nextNode);
		if(endTaskVoList != null && endTaskVoList.size() > 0){
			oldTaskVo = endTaskVoList.get(0);
		}
		return oldTaskVo;
	}
	
	private PrpLWfTaskVo findTaskIn(String registNo,String traceMainId,FlowNode nextNode) throws Exception{
		PrpLWfTaskVo oldTaskVo = null;
		List<PrpLWfTaskVo> endTaskVoList = wfTaskHandleService.findEndTask(registNo,traceMainId,nextNode);
		if(endTaskVoList == null || endTaskVoList.size() == 0){
			endTaskVoList = wfTaskHandleService.findEndTask(registNo,traceMainId,FlowNode.PLFirst);
		}
		if(endTaskVoList != null && endTaskVoList.size() > 0){
			oldTaskVo = endTaskVoList.get(0);
		}
		return oldTaskVo;
	}
	
	@Override
	public void submitPLChargeAdjust(Long persTraceMainId) throws Exception {
		PrpLDlossPersTraceMainVo persTraceMainVo = persTraceService.findPersTraceMainVoById(persTraceMainId);
		String registNo = persTraceMainVo.getRegistNo();
		List<PrpLDlossPersTraceVo> persTraceVos = persTraceService.findPersTraceVo(persTraceMainVo.getRegistNo(),persTraceMainId);
		persTraceMainVo.setPrpLDlossPersTraces(persTraceVos);

		if(wfTaskHandleService.existTaskByNodeCode(persTraceMainVo.getRegistNo(),FlowNode.PLNext,persTraceMainVo.getId().toString(),"0")){
			throw new Exception("已经发起了费用审核修改任务，请刷新后再试！");
		}
		List<PrpLWfTaskVo> inVos=wfTaskHandleService.findPrpLWfTaskInByRegistNo(registNo);
		   String signIndex="0";
		   if(inVos!=null && inVos.size()>0){
			   for(PrpLWfTaskVo invo:inVos){
				   if(StringUtils.isNotBlank(invo.getSubNodeCode()) && invo.getSubNodeCode().contains("PLCharge")){
					   signIndex="1";
				   }
			   }
		   }
		   if("1".equals(signIndex)){
			   throw new Exception("人伤费用审核有未完成任务，不能发起人伤费用审核修改！");
		   }
		   
		   List<PrpLWfTaskVo> plverifyinVos=wfTaskHandleService.findPrpLWfTaskInByRegistNo(registNo);
		   String signIndexss="0";
		   if(plverifyinVos!=null && plverifyinVos.size()>0){
			   for(PrpLWfTaskVo plverifyinvo:plverifyinVos){
				   if(StringUtils.isNotBlank(plverifyinvo.getSubNodeCode()) && plverifyinvo.getSubNodeCode().contains("PLVerify")){
					   signIndexss="1";
				   }
			   }
		   }
		   if("1".equals(signIndexss)){
			   throw new Exception("人伤跟踪审核有未完成任务，不能发起人伤费用审核修改！");
		   }
			
        //yzy--人伤费用修改发起控制
		List<PrpLClaimVo> vos = claimService.findClaimListByRegistNo(registNo);
		if(vos!=null && vos.size()>0){
			for(PrpLClaimVo vo : vos){
				if(StringUtils.isBlank(vo.getEndCaserCode()) && StringUtils.isBlank(vo.getCaseNo()) ){//重开后
					
					Map<String,String> queryMap = new HashMap<String,String>();
					queryMap.put("registNo", registNo);
					queryMap.put("claimNo", vo.getClaimNo());
					queryMap.put("checkStatus", "6");//重开需要审核通过--审核通过
					//查找审核通过的重开赔案 立案号列表
					List<PrpLReCaseVo> prpLReCaseVoList = endCasePubService.findReCaseVoListByqueryMap(queryMap);
					if(prpLReCaseVoList!=null && prpLReCaseVoList.size()>0){
						PrpLClaimVo prpLClaimVo = claimService.findClaimVoByClaimNo(prpLReCaseVoList.get(0).getClaimNo());
						String riskCode = prpLClaimVo.getRiskCode().substring(0, 2);
						String subNodeCode = "";
						if("12".equals(riskCode)){
							subNodeCode = FlowNode.CompeBI.toString();
						}else{
							subNodeCode = FlowNode.CompeCI.toString();
						}
						//in表
						List<PrpLWfTaskVo>  inList = wfFlowQueryService.findPrpWfTaskVo(registNo, FlowNode.Compe.toString());
						for(PrpLWfTaskVo wfTaskVo : inList){
							if(subNodeCode.equals(wfTaskVo.getSubNodeCode())){
								throw new Exception("已存在理算任务，不能发起人伤费用修改");
							}
						}
						//查询out表的最新有效理算节点的计算书号，是否是重开的计算书号，如果是，则表示重开后没有新的有效理算节点
						String flags = "1";
						String newestCompNo = "";
						Map<String, String> compeUnderMap = compensateTaskService.getCompUnderWriteFlagByRegNo(registNo);
						PrpLWfTaskVo WfQueryVo = new PrpLWfTaskVo();
						WfQueryVo.setRegistNo(registNo);
						WfQueryVo.setSubNodeCode(subNodeCode);
						WfQueryVo.setWorkStatus(CodeConstants.WorkStatus.END);
						List<PrpLWfTaskVo>  outList = wfFlowQueryService.findTaskVoForQueryVo(WfQueryVo, RadioValue.RADIO_YES);
						
						if(outList!=null && outList.size()>0){
							for(int i = 0;i<outList.size();i++){//获取最新有效计算书号需要排除已注销的计算书
								if(!UnderWriteFlag.CANCELFLAG.equals(compeUnderMap.get(outList.get(i).getCompensateNo()))){
									newestCompNo = outList.get(i).getCompensateNo();
									break;
								}
							}
							for(PrpLReCaseVo prpLReCaseVo : prpLReCaseVoList){
								if(prpLReCaseVo.getCompensateNo().equals(newestCompNo)){
									flags = "0";
								}
							}
						}
						if("1".equals(flags)){
							throw new Exception("已存在理算任务，不能发起人伤费用修改");
						}
					}else{//重开前
					    //prplwftaskin表存在理算任务
					    if(wfTaskHandleService.existTaskByNodeCode(registNo,FlowNode.Compe, "", "0")){
					        throw new Exception("已经存在有效的理算任务，不能发起费用审核修改！");
					    }
					    //prplwftaskout表存在理算任务
						if(wfTaskHandleService.existTaskByNodeCode(registNo,FlowNode.Compe, "", "1")){
						    List<PrpLCompensateVo> prpLCompensateList = compensateTaskService.findCompByRegistNo(registNo);
						    if(prpLCompensateList != null&&prpLCompensateList.size() > 0){
                                for(PrpLCompensateVo prpLCompensateVo:prpLCompensateList){
                                    if("N".equals(prpLCompensateVo.getCompensateType())){
                                        if(!"7".equals(prpLCompensateVo.getUnderwriteFlag())){
                                            throw new Exception("已经存在有效的理算任务，不能发起费用审核修改！");
                                        }
                                    }
                                }
                            }
						}		
					}	
				}	
			}
		}
		
		List<PrpLClaimVo> claimList = claimService.findClaimListByRegistNo(registNo); 
		boolean isNoEndCase = false;
		for(PrpLClaimVo claimVo : claimList){
			if(claimVo.getEndCaseTime()==null){
				isNoEndCase = true;
				break;
			}
		}
		
		if(!isNoEndCase){
			throw new Exception("该案件已结案，请先重开赔案！");
		}
		
		String sign="1";
		
		// 取最后一个核损的任务
		PrpLWfTaskVo taskVo = wfTaskHandleService.findEndTask(persTraceMainVo.getRegistNo(),persTraceMainId.toString(),FlowNode.PLoss).get(0);

		WfTaskSubmitVo taskSubmitVo = new WfTaskSubmitVo();
		taskSubmitVo.setCurrentNode(FlowNode.valueOf(taskVo.getNodeCode()));
		taskSubmitVo.setFlowId(taskVo.getFlowId());
		taskSubmitVo.setFlowTaskId(taskVo.getTaskId());
		taskSubmitVo.setNextNode(FlowNode.PLNext);
		taskSubmitVo.setComCode(policyViewService.getPolicyComCode(registNo));
		PrpLWfTaskVo oldTaskVo = findTaskIn(registNo,persTraceMainVo.getId().toString(),FlowNode.PLNext);
		taskSubmitVo.setTaskInUser(oldTaskVo.getHandlerUser());
		taskSubmitVo.setTaskInKey(persTraceMainVo.getId().toString());
		
		// 指定原出来人
		taskSubmitVo.setAssignCom(oldTaskVo.getHandlerCom());
		taskSubmitVo.setAssignUser(oldTaskVo.getHandlerUser());

		WfSimpleTaskVo simpleTaskVo = new WfSimpleTaskVo();
		simpleTaskVo.setRegistNo(registNo);
		simpleTaskVo.setHandlerIdKey(persTraceMainVo.getId().toString());

		wfTaskHandleService.addSimpleTask(simpleTaskVo,taskSubmitVo);

		// 核损标志--费用审核修改
		persTraceMainVo.setFlag(PersVeriFlag.CHARGEADJUST);

		persTraceService.saveOrUpdatePersTraceMain(persTraceMainVo);
	}

	
	private static BigDecimal NullToZero(BigDecimal strNum) {
		if(strNum==null){
			return new BigDecimal("0");
		}
		return strNum;
	}
	
	@Override
	public SubmitNextVo organizNextVo(PrpLDlossPersTraceMainVo persTraceMain,SubmitNextVo nextVo,
			SysUserVo userVo,String caseProcessType,String existHeadOffice){
		try{
			PrpLWfTaskVo taskVo = wfTaskHandleService.findTaskIn(Double.parseDouble(nextVo.getFlowTaskId()));
			LIlogRuleResVo ruleResVo = new LIlogRuleResVo();
			String auditStatus = nextVo.getAuditStatus();
			
			if(AuditStatus.SUBPLNEXT.equalsIgnoreCase(auditStatus)){// 提交后续跟踪任务提交到自己
				nextVo.setNextNode(FlowNode.PLNext.name());
				nextVo.setNextName(FlowNode.PLNext.getName());
				nextVo.setAssignUser(userVo.getUserCode());
				nextVo.setAssignName(userVo.getUserName());
				nextVo.setAssignCom(userVo.getComCode());
			}else{
				PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.ILOGVALIDFLAG,userVo.getComCode());
				PrpLConfigValueVo ruleConfigValueVo = ConfigUtil.findConfigByCode(CodeConstants.RULEVALIDFLAG,userVo.getComCode());
				
				if("1".equals(configValueVo.getConfigValue())){
					if(AuditStatus.SUBPLCHARGE.equalsIgnoreCase(auditStatus)){//提交费用审核 
						//请求ILOG
						ruleResVo = persReqIlogService.reqIlogByPers(persTraceMain, taskVo, nextVo, userVo, "1",existHeadOffice);
						
						String finalPowerFlag =  SpringProperties.getProperty("FINALPOWERFLAG");
			        	boolean finalAutoPass = true;
			        	if ("1".equals(finalPowerFlag) && "1".equals(ruleResVo.getUnderwriterflag())) {
							/** 兜底人员权限判断 start **/
							IlogFinalPowerInfoVo powerInfoVo = ilogRuleService.findByUserCode(userVo.getUserCode());
							
							if (powerInfoVo != null) {
								BigDecimal gradePower = powerInfoVo.getGradeAmount();
								if (gradePower != null) {
									List<PrpLDlossPersTraceVo> persTraceVoList = persTraceService.findPersTraceVoByRegistNo(persTraceMain.getRegistNo());
									// 总定损金额
									BigDecimal sumAmount = BigDecimal.ZERO;
									if (persTraceVoList != null && persTraceVoList.size() > 0) {
										// 统计人伤定损总金额
										for (PrpLDlossPersTraceVo persTraceVo : persTraceVoList) {
											if("1".equals(persTraceVo.getValidFlag())){
												if(taskVo.getSubNodeCode().startsWith(FlowNode.PLVerify.name())
														|| taskVo.getSubNodeCode().startsWith(FlowNode.PLCharge.name())){
													if(persTraceVo.getSumVeriDefloss() != null){
														sumAmount = sumAmount.add(NullToZero(persTraceVo.getSumVeriDefloss()));
													}else{
														sumAmount = sumAmount.add(NullToZero(persTraceVo.getSumVeriReportFee()));
													}
												} else {
													if(persTraceVo.getSumdefLoss() != null){
														sumAmount = sumAmount.add(NullToZero(persTraceVo.getSumdefLoss()));
													}else{
														sumAmount = sumAmount.add(NullToZero(persTraceVo.getSumReportFee()));
													}
												}
											}
										}
									}
									if (sumAmount.compareTo(gradePower) == 1) {
										finalAutoPass = false;
									}
								} else {
					    			finalAutoPass = false;
					    		}
							}
							/** 兜底人员权限判断  end  **/
						}
						
						if("0".equals(ruleResVo.getState())){
							throw new IllegalArgumentException("请求ILOG报错："+ruleResVo.getContent());
						}else if("1".equals(ruleResVo.getUnderwriterflag()) && finalAutoPass){
							nextVo.setNextNode(FlowNode.PLCharge_LV0.name());
							nextVo.setNextName(FlowNode.PLCharge_LV0.getName());
							nextVo.setAssignName("人伤费用自动审核岗");
						}else{
							ruleResVo = persReqIlogService.reqIlogByPers(persTraceMain, taskVo, nextVo, userVo, "2",existHeadOffice);
							Integer minLevel = Integer.valueOf(ruleResVo.getMinUndwrtNode());
							Integer maxLevel = Integer.valueOf(ruleResVo.getMaxUndwrtNode());
							boolean haveUser = false;
							while(!haveUser && minLevel<= maxLevel){//判断该级别是否有人，没人逐级上传
								haveUser = assignService.existsGradeUser(FlowNode.valueOf(FlowNode.PLCharge.name()+"_LV"+minLevel), userVo.getComCode());
								if(!haveUser){
									minLevel ++;
								}
							}
							nextVo.setSubmitLevel(minLevel);
							nextVo.setMaxLevel(maxLevel);
							if(minLevel > maxLevel){
								nextVo.setNextNode("PLCharge_LV"+maxLevel.toString());
							}else{
								nextVo.setNextNode("PLCharge_LV"+minLevel.toString());
							}
							nextVo.setNextName(FlowNode.valueOf(nextVo.getNextNode()).getName());
							nextVo.setAssignName("人伤费用审核岗");
						}
					}else if(AuditStatus.SUBPLVERIFY.equalsIgnoreCase(auditStatus)){// 提交跟踪审核
						ruleResVo = persReqIlogService.reqIlogByPers(persTraceMain, taskVo, nextVo, userVo, "2",existHeadOffice);
						Integer minLevel = Integer.valueOf(ruleResVo.getMinUndwrtNode());
						Integer maxLevel = Integer.valueOf(ruleResVo.getMaxUndwrtNode());
						boolean haveUser = false;
						while(!haveUser && minLevel<= maxLevel){//判断该级别是否有人，没人逐级上传
							haveUser = assignService.existsGradeUser(FlowNode.valueOf(FlowNode.PLVerify.name()+"_LV"+minLevel), userVo.getComCode());
							if(!haveUser){
								minLevel ++;
							}
						}
						nextVo.setSubmitLevel(minLevel);
						nextVo.setMaxLevel(maxLevel);
						if(minLevel > maxLevel){
							nextVo.setNextNode("PLVerify_LV"+maxLevel.toString());
						}else{
							nextVo.setNextNode("PLVerify_LV"+minLevel.toString());
						}
						nextVo.setNextName(FlowNode.valueOf(nextVo.getNextNode()).getName());
						nextVo.setAssignName("人伤跟踪审核岗");
					}
				}
				if("1".equals(ruleConfigValueVo.getConfigValue())){
					//走原规则代码
					setNextVoByRule(persTraceMain, nextVo, userVo);
				}
			}
			
		}catch(Exception e){
			logger.error("组织人伤提交路径请求ILOG失败！业务号："+persTraceMain.getRegistNo()+"错误信息：",e);
		}
		
		return nextVo;
	}
	
	public void setNextVoByRule(PrpLDlossPersTraceMainVo prpLDlossPersTraceMainVo,SubmitNextVo submitVo
			,SysUserVo userVo) throws Exception{
		String auditStatus = submitVo.getAuditStatus();
		Double sumdefLoss = 0d;// 定损总金额
		Long traceMainId = prpLDlossPersTraceMainVo.getId();
		List<PrpLDlossPersTraceVo> persTraceVos = persTraceService.findPersTraceVo(prpLDlossPersTraceMainVo.getRegistNo(),traceMainId);
		if(persTraceVos!=null&&persTraceVos.size()>0){
			for(PrpLDlossPersTraceVo persTraceVo:persTraceVos){
				if("1".equals(persTraceVo.getValidFlag())){
					BigDecimal sumLoss = persTraceVo.getSumdefLoss();
					if(sumLoss==null){
						sumLoss = persTraceVo.getSumReportFee();
					}
					sumdefLoss += DataUtils.NullToZero(sumLoss).doubleValue();// 计算总定损金额
				}
			}
		}
		//审核级别金额加上费用金额
		sumdefLoss += DataUtils.NullToZero(prpLDlossPersTraceMainVo.getSumChargeFee()).doubleValue();
		
		// 获取提交到的审核级别
		VerifyPersonRuleVo ruleVo = new VerifyPersonRuleVo();
		ruleVo.setRiskCode(prpLDlossPersTraceMainVo.getRiskCode());
		ruleVo.setComCode(userVo.getComCode());
		ruleVo.setClassCode(prpLDlossPersTraceMainVo.getRiskCode().substring(0,2));
		ruleVo.setSumLossFee(sumdefLoss);
//		ruleVo = claimRuleApiService.lossPersonToVerify(ruleVo);
//		int backLevel = ruleVo.getBackLevel();
//		if(backLevel > 8){
//			ruleVo.setTopComp("1");//总公司标识
//			comCode ="00010000";//总公司人员 机构用公司本部
//		}
		if(AuditStatus.SUBPLVERIFY.equalsIgnoreCase(auditStatus)){// 提交到跟踪审核
			ruleVo = claimRuleApiService.lossPersonToVerify(ruleVo);
			int backLevel = ruleVo.getBackLevel();
			boolean haveUser = false;
			int level = backLevel;
			while(!haveUser && level<= ruleVo.getMaxLevel()){//判断该级别是否有人，没人逐级上传
				haveUser = assignService.existsGradeUser(FlowNode.valueOf(FlowNode.PLVerify.name()+"_LV"+level), userVo.getComCode());
				if(!haveUser){
					if(level== ruleVo.getMaxLevel()){
						if(ruleVo.getComCode().startsWith("0001")){
							throw new Exception("总公司最高级别没有配置人员，请联系运维！");
						}else{
							throw new Exception("分公司最高级别没有配置人员，请联系运维！");
						}
					}
					level ++;
				}
			}
			
//			prpLDlossPersTraceMainVo.setVerifyLevel(level);
//			prpLDlossPersTraceMainVo.setMaxLevel(ruleVo.getMaxLevel());
			String nodeName = "";
			if(ruleVo.getBackLevel()>ruleVo.getMaxLevel()){// 提交到总公司级别的只能从分公司最高级开始提交
				nodeName = FlowNode.PLVerify.name()+"_LV"+ruleVo.getMaxLevel();
				submitVo.setNextNode(nodeName);
			}else{
				nodeName = FlowNode.PLVerify.name()+"_LV"+level;
				submitVo.setNextNode(nodeName);
			}
			submitVo.setNextName(FlowNode.valueOf(nodeName).getName());
			submitVo.setAssignName("人伤跟踪审核岗");
			submitVo.setSubmitLevel(ruleVo.getBackLevel());
			submitVo.setMaxLevel(ruleVo.getMaxLevel());
		}else if(AuditStatus.SUBPLCHARGE.equalsIgnoreCase(auditStatus)){// 提交到费用审核
			ruleVo = claimRuleApiService.lossPersonToPrice(ruleVo);
			int backLevel = ruleVo.getBackLevel();
			boolean haveUser = false;
			int level = backLevel;
			while(!haveUser && level<= ruleVo.getMaxLevel()){//判断该级别是否有人，没人逐级上传
				haveUser = assignService.existsGradeUser(FlowNode.valueOf(FlowNode.PLCharge.name()+"_LV"+level), userVo.getComCode());
				if(!haveUser){
					if(level== ruleVo.getMaxLevel()){
						if(ruleVo.getComCode().startsWith("0001")){
							throw new Exception("总公司最高级别没有配置人员，请联系运维！");
						}else{
							throw new Exception("分公司最高级别没有配置人员，请联系运维！");
						}
					}
					level ++;
				}
			}
			
//			prpLDlossPersTraceMainVo.setVerifyLevel(level);
//			prpLDlossPersTraceMainVo.setMaxLevel(ruleVo.getMaxLevel());
			String nodeName = "";
			if(ruleVo.getBackLevel()>ruleVo.getMaxLevel()){// 提交到总公司级别的只能从9级开始提交
				nodeName = FlowNode.PLCharge.name()+"_LV"+ruleVo.getMaxLevel();
				submitVo.setNextNode(nodeName);
			}else{
				nodeName = FlowNode.PLCharge.name()+"_LV"+level;
				submitVo.setNextNode(nodeName);
			}
			submitVo.setNextName(FlowNode.valueOf(nodeName).getName());
			submitVo.setAssignName("人伤费用审核岗");
			submitVo.setSubmitLevel(ruleVo.getBackLevel());
			submitVo.setMaxLevel(ruleVo.getMaxLevel());
		}
	}
	
	@Override
	public void autoPersSubmitForChe(PrpLWfTaskVo taskVo,PrpLCheckVo checkVo,SysUserVo userVo){
		try{
			Long traceMainId = Long.valueOf(taskVo.getHandlerIdKey());
			List<PrpLCheckPersonVo> checkPersonList = checkVo.getPrpLCheckTask().getPrpLCheckPersons();
			String registNo = taskVo.getRegistNo();
			userVo = sysUserService.findByUserCode(userVo.getUserCode());
			List<PrpLDlossChargeVo> prpLDlossChargeVos = new ArrayList<PrpLDlossChargeVo>();
			SubmitNextVo submitNextVo = new SubmitNextVo();
			
			//如果任务未接收，先接收
			if(HandlerStatus.INIT.equals(taskVo.getHandlerStatus())){
				traceMainId = acceptPersTraceTask(taskVo.getTaskId().toString(),registNo,userVo);
			}
			
			/*人伤跟踪赋值start	*/
			PrpLDlossPersTraceMainVo persTraceMainVo = persTraceService.findPersTraceMainVoById(traceMainId);
			PrpLClaimTextVo prpLClaimTextVo = claimTextSerVice.findClaimTextByNode(traceMainId,taskVo.getSubNodeCode(),"0");// 暂存时flag=0，提交flag=1
			
			if(prpLClaimTextVo==null||prpLClaimTextVo.getId()==null){
				prpLClaimTextVo = new PrpLClaimTextVo();
				prpLClaimTextVo.setRegistNo(registNo);
				prpLClaimTextVo.setTextType(ClaimText.OPINION);
				prpLClaimTextVo.setNodeCode(taskVo.getSubNodeCode());
				prpLClaimTextVo.setRemark("现场一次性调解");
				prpLClaimTextVo.setOpinionCode("1");
			}
			
			// 查询其他节点的交强险责任类型和事故责任比例--人伤只有标的车
			PrpLCheckDutyVo prpLCheckDutyVo = checkTaskService.findCheckDuty(registNo,1);
			if(prpLCheckDutyVo==null||prpLCheckDutyVo.getId()==null){
				prpLCheckDutyVo = new PrpLCheckDutyVo();
				prpLCheckDutyVo.setRegistNo(registNo);
				prpLCheckDutyVo.setSerialNo(1);
			}
			
			//人伤主表赋值
			persTraceMainVo.setCaseProcessType(checkVo.getPersHandleType());
			persTraceMainVo.setPlfCertiCode(userVo.getIdentifyNumber());
			persTraceMainVo.setPlfName(userVo.getUserName());
			persTraceMainVo.setPlfPhone(userVo.getMobile());
			persTraceMainVo.setIsMinorInjuryCases("0");
			persTraceMainVo.setMajorcaseFlag("0");

			
			//赋值人员信息
			for(PrpLCheckPersonVo checkPersonVo:checkPersonList){
				PrpLDlossPersTraceVo persTraceVo = new PrpLDlossPersTraceVo();
				PrpLDlossPersInjuredVo persInjuredVo =new PrpLDlossPersInjuredVo();
				
				persTraceVo.setPersonName(checkPersonVo.getPersonName());
				persTraceVo.setSumReportFee(checkPersonVo.getLossFee());
				persTraceVo.setSumRealFee(checkPersonVo.getLossFee());
				persTraceVo.setSumdefLoss(checkPersonVo.getLossFee());
				persTraceVo.setSumDetractionFee(BigDecimal.ZERO);
				persTraceVo.setSumVeriReportFee(checkPersonVo.getLossFee());
				persTraceVo.setSumVeriRealFee(checkPersonVo.getLossFee());
				persTraceVo.setSumVeriDetractionFee(BigDecimal.ZERO);
				persTraceVo.setSumVeriDefloss(checkPersonVo.getLossFee());
				persTraceVo.setTraceForms("5");
				persTraceVo.setOperatorDesc("现场一次性调解");
				persTraceVo.setEndFlag("1");
				persTraceVo.setRegistNo(registNo);
				persTraceVo.setPersTraceMainId(traceMainId);
				
				//人员受伤明细
				List<PrpLDlossPersExtVo> prpLDlossPersExts = new ArrayList<PrpLDlossPersExtVo>();
				PrpLDlossPersExtVo persExtVo = new PrpLDlossPersExtVo();
				persExtVo.setInjuredPart("99");
				persExtVo.setInjuredDiag("02");//伤情诊断：默认“软组织损伤”
				persExtVo.setRegistNo(registNo);
				prpLDlossPersExts.add(persExtVo);
				persInjuredVo.setPrpLDlossPersExts(prpLDlossPersExts);
				
				persInjuredVo.setPersonName(checkPersonVo.getPersonName());
				persInjuredVo.setLossItemType(checkPersonVo.getPersonProp());
				persInjuredVo.setPersonSex(checkPersonVo.getPersonSex());
				persInjuredVo.setCertiType(checkPersonVo.getIdentifyType());
				persInjuredVo.setCertiCode(checkPersonVo.getIdNo());
				persInjuredVo.setPersonAge(checkPersonVo.getPersonAge());
				persInjuredVo.setTicCode(checkPersonVo.getTicCode());
				persInjuredVo.setWoundCode("01");//默认简易人伤
				persInjuredVo.setTreatSituation("1");//默认“门诊治疗”
				persInjuredVo.setInjuryPart(checkPersonVo.getInjuredPart()!=null ? checkPersonVo.getInjuredPart():"99");//受伤部位默认其他
				persInjuredVo.setRegistNo(registNo);
				persInjuredVo.setSerialNo(Integer.valueOf(checkPersonVo.getLossPartyId().toString()));
				persInjuredVo.setLicenseNo(checkPersonVo.getLossPartyName());
				persTraceVo.setPrpLDlossPersInjured(persInjuredVo);
				
				List<PrpLDlossPersTraceFeeVo> traceFeeVoList = new ArrayList<PrpLDlossPersTraceFeeVo>();
				PrpLDlossPersTraceFeeVo traceFeeVo = new PrpLDlossPersTraceFeeVo();
				traceFeeVo.setFeeTypeCode("1");//默认医疗费
				traceFeeVo.setReportFee(checkPersonVo.getLossFee());
				traceFeeVo.setRealFee(checkPersonVo.getLossFee());
				traceFeeVo.setDefloss(checkPersonVo.getLossFee());
				traceFeeVo.setDetractionfee(BigDecimal.ZERO);
				traceFeeVo.setVeriReportFee(checkPersonVo.getLossFee());
				traceFeeVo.setVeriRealFee(checkPersonVo.getLossFee());
				traceFeeVo.setVeriDetractionFee(BigDecimal.ZERO);
				traceFeeVo.setVeriDefloss(checkPersonVo.getLossFee());
				traceFeeVo.setRegistNo(registNo);
				traceFeeVoList.add(traceFeeVo);
				persTraceVo.setPrpLDlossPersTraceFees(traceFeeVoList);
				//保存人员信息
				saveCasualtyInfo(persTraceVo, userVo);
			}
			
			submitNextVo.setNextNode(FlowNode.PLCharge_LV0.name());
			submitNextVo.setNextName(FlowNode.PLCharge_LV0.getName());
			submitNextVo.setAssignName("人伤费用自动审核");
			submitNextVo.setCurrentNode(taskVo.getSubNodeCode());
			submitNextVo.setCurrentName(FlowNode.valueOf(taskVo.getSubNodeCode()).getName());
			submitNextVo.setAssignCom(userVo.getComCode());
			submitNextVo.setAuditStatus("subPLCharge");
			submitNextVo.setComCode(userVo.getComCode());
			submitNextVo.setFlowTaskId(taskVo.getTaskId().toString());
			/*人伤跟踪赋值end */
			//人伤跟踪提交
			persTraceService.saveOrUpdatePersTraceMain(persTraceMainVo);
			persTraceMainVo = persTraceService.calculateSumAmt(persTraceMainVo);
			saveOrSubmitPLEdit(persTraceMainVo, prpLCheckDutyVo, prpLDlossChargeVos, prpLClaimTextVo, submitNextVo, userVo);
			
			
		}catch(Exception e){
			logger.error("查勘提交人伤自动审核失败！业务号："+taskVo.getRegistNo()+"报错信息：",e);
		}
	}
	
	private Boolean validAllVLossPass(String registNo){
        Boolean flag=false;
		// 所有定核损任务是否完成
		List<PrpLdlossPropMainVo> prpLdlossPropMainVoList = propTaskService.findPropMainListByRegistNo(registNo);
		List<PrpLDlossCarMainVo> prpLDlossCarMainVoList = lossCarService.findLossCarMainByRegistNo(registNo);
		List<PrpLDlossPersTraceMainVo> prpLDlossPersTraceMainVoList = persTraceService.findPersTraceMainVo(registNo);
		// 校验定损任务
		boolean vLoss = true;
		if(prpLdlossPropMainVoList!=null && prpLdlossPropMainVoList.size()>0){
			for(PrpLdlossPropMainVo prpLdlossPropMainVo:prpLdlossPropMainVoList){
				if(!CodeConstants.VeriFlag.PASS.equals(prpLdlossPropMainVo.getUnderWriteFlag()) &&
			    		!CodeConstants.VeriFlag.CANCEL.equals(prpLdlossPropMainVo.getUnderWriteFlag())){
			 	   vLoss = false;
			    }
			}
		}

		if(prpLDlossCarMainVoList!=null && prpLDlossCarMainVoList.size()>0){
			for(PrpLDlossCarMainVo prpLDlossCarMainVo:prpLDlossCarMainVoList){
				if(!CodeConstants.VeriFlag.PASS.equals(prpLDlossCarMainVo.getUnderWriteFlag()) &&
			    		!CodeConstants.VeriFlag.CANCEL.equals(prpLDlossCarMainVo.getUnderWriteFlag())){
				   vLoss = false;
			    }
			}
		}

		if(vLoss){// 判断未接收的定损任务 排除复检
			List<PrpLWfTaskVo> prpLWfTaskVoList = wfFlowQueryService.findUnAcceptTask(registNo,FlowNode.DLoss.name(),
					FlowNode.DLProp.name(),FlowNode.DLCar.name());
			if(prpLWfTaskVoList!=null&&prpLWfTaskVoList.size()>0){
				for(PrpLWfTaskVo prpLWfTaskVo:prpLWfTaskVoList){
					if( !prpLWfTaskVo.getSubNodeCode().equals(FlowNode.DLChk.name())){
						vLoss = false;
						break;
					}
				}
			}
		}
		 //校验人伤任务
		   boolean pLoss = true;
		   if(prpLDlossPersTraceMainVoList != null && prpLDlossPersTraceMainVoList.size()>0){
			   for(PrpLDlossPersTraceMainVo prpLDlossPersTraceMainVo:prpLDlossPersTraceMainVoList){
				   if(!prpLDlossPersTraceMainVo.getAuditStatus().equals(CodeConstants.AuditStatus.SUBMITCHARGE)){
					   pLoss = false;
				   }
			   } 
		   }
		   if(pLoss){//判断是否有未接收的人伤任务
			   List<PrpLWfTaskVo> prpLWfTaskVoList = wfFlowQueryService.findUnAcceptTask(registNo,FlowNode.PLoss.name());
			   if(prpLWfTaskVoList != null && prpLWfTaskVoList.size() > 0){
				   pLoss =  false;
			   }
		   }


		if(vLoss&&pLoss){// 所有定核损都已经审核通过，可以请求ILOG消息
			flag=true;
		}
		return flag;
	}
	
    public void cancelPerson(PrpLDlossPersTraceMainVo persTraceMainVo, BigDecimal flowTaskId,SysUserVo userVo) {
    	logger.info("报案号registno={}，进入人伤注销回写UnderwriteFlag的方法",persTraceMainVo.getRegistNo());
        persTraceMainVo.setUnderwriteFlag("7");// 修改计算书的核赔状态字段
       PrpLDlossPersTraceMain persTraceMain = new PrpLDlossPersTraceMain();
       Beans.copy().from(persTraceMainVo).to(persTraceMain);
       databaseDao.update(PrpLDlossPersTraceMain.class, persTraceMain);
       // 调用工作流注销方法
       wfTaskHandleService.cancelTask(userVo.getUserCode(), flowTaskId);
       logger.info("报案号registno={}，结束人伤注销回写UnderwriteFlag的方法",persTraceMainVo.getRegistNo());
   }
    
    /**
	 * 生成人伤查勘费的任务
	 * @param lossCarMainVo
	 * @param userVo
	 */
    @Override
	public void saveAcheckFeeTask(PrpLDlossPersTraceMainVo persTraceMainVo,SysUserVo userVo){
		PrpdCheckBankMainVo prpdCheckBankMainVo=managerService.findCheckByUserCode(userVo.getUserCode());
		PrpLRegistVo prpLRegistVo=registQueryService.findByRegistNo(persTraceMainVo.getRegistNo());
		if(prpdCheckBankMainVo !=null){
			PrpLAcheckVo prpLAcheckOldVo=acheckService.findAcheckByLossId(persTraceMainVo.getRegistNo(),CheckTaskType.PERS,null, prpdCheckBankMainVo.getCheckCode());
			if(prpLAcheckOldVo == null){
				PrpLAcheckVo acheckVo = new PrpLAcheckVo();
				acheckVo.setCheckmid(prpdCheckBankMainVo.getId());
				acheckVo.setRegistNo(persTraceMainVo.getRegistNo());
				acheckVo.setCheckmnamedetail(prpdCheckBankMainVo.getCheckName());
				acheckVo.setBussiId(persTraceMainVo.getId());
				acheckVo.setTaskType(CheckTaskType.PERS);
				acheckVo.setUnderWriteFlag(CodeConstants.AcheckUnderWriteFlag.Loss);
				acheckVo.setLossDate(persTraceMainVo.getCreateTime());
				acheckVo.setCheckcode(prpdCheckBankMainVo.getCheckCode());
				acheckVo.setCheckname(codeTranService.transCode("CheckPayCode",prpdCheckBankMainVo.getCheckCode()));
				acheckVo.setCreateTime(new Date());
				acheckVo.setCreateUser(userVo.getUserCode());
				PrpLRegistVo registVo = registQueryService.findByRegistNo(persTraceMainVo.getRegistNo());
				if(persTraceMainVo.getPrpLDlossPersTraces()!=null && persTraceMainVo.getPrpLDlossPersTraces().size()>0){
					PrpLDlossPersTraceVo persTraceVo = persTraceMainVo.getPrpLDlossPersTraces().get(0);
					if("1".equals(persTraceVo.getPrpLDlossPersInjured().getLossItemType())){//车外人员
						if("2".equals(registVo.getReportType())){
							acheckVo.setKindCode("BZ");
						}else{
							acheckVo.setKindCode("B");
						}
					}else if("2".equals(persTraceVo.getPrpLDlossPersInjured().getLossItemType())){//本车乘客
						acheckVo.setKindCode("D12");
					}else if("3".equals(persTraceVo.getPrpLDlossPersInjured().getLossItemType())){//本车司机
						acheckVo.setKindCode("D11");
					}
				}else{
					if("2".equals(registVo.getReportType())){
						acheckVo.setKindCode("BZ");
					}else{
						acheckVo.setKindCode("B");
					}
				}
				acheckVo.setComCode(prpLRegistVo.getComCode());
				BigDecimal veriLoss = new BigDecimal("0");
				if(persTraceMainVo.getPrpLDlossPersTraces() != null){
					for(PrpLDlossPersTraceVo prpLDlossPersTrace : persTraceMainVo.getPrpLDlossPersTraces() ){
						if("1".equals(prpLDlossPersTrace.getValidFlag())){
							veriLoss = veriLoss.add(DataUtils.NullToZero(prpLDlossPersTrace.getSumVeriDefloss()));
						}
					}
				}
				acheckVo.setVeriLoss(veriLoss);
				acheckService.saveOrUpdatePrpLAcheck(acheckVo,userVo);
				PrpLDlossPersTraceMainVo traceMainVo=new PrpLDlossPersTraceMainVo();
				traceMainVo.setCheckCode(prpdCheckBankMainVo.getCheckCode());
				
				traceMainVo.setId(persTraceMainVo.getId());
				persTraceService.saveOrUpdateTraceMain(traceMainVo);
			}
		}
	}
    
    
    
	// 理赔调度提交/改派提交接口（理赔请求快赔系统）调度初始化或者新增查勘
	public void setReassignments(PrpLDlossPersTraceMainVo prpLDlossPersTraceMainVo,WfTaskSubmitVo submitVo) throws Exception {	
		// 获取报案信息
		PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(prpLDlossPersTraceMainVo.getRegistNo());
		PrpLCMainVo vo = prpLCMainService.findPrpLCMain(prpLDlossPersTraceMainVo.getRegistNo(), prpLRegistVo.getPolicyNo());
		
		HandleScheduleDOrGReqVo reqVo = new HandleScheduleDOrGReqVo();
		
		HeadReq head = setHeadReq();// 设置头部信息
		
		
		HandleScheduleReqDOrGBody body = new HandleScheduleReqDOrGBody();
		HandleScheduleReqScheduleDOrG scheduleDOrG = new HandleScheduleReqScheduleDOrG();  //数据部分
		
        // 是否移动端案件
//		PrpLScheduleTaskVo selfScheduleTaskVo = scheduleTaskService.findScheduleTaskByRegistNo(prpLRegistVo.getRegistNo());
		scheduleDOrG.setRegistNo(prpLDlossPersTraceMainVo.getRegistNo());
		scheduleDOrG.setScheduleType("0"); //调度类型为：0 -- 提交
        scheduleDOrG.setCaseFlag("3");
        scheduleDOrG.setIsMobileCase("1");
        
        if(prpLRegistVo != null) {
        	scheduleDOrG.setDamageTime(prpLRegistVo.getDamageTime());
        	scheduleDOrG.setDamagePlace(prpLRegistVo.getDamageAddress());
        	scheduleDOrG.setInuredPhone(prpLRegistVo.getInsuredPhone());
        	scheduleDOrG.setLicenseNo(prpLRegistVo.getPrpLRegistExt().getLicenseNo());
        	scheduleDOrG.setReportorName(prpLRegistVo.getReportorName());
        	scheduleDOrG.setReportorPhone(prpLRegistVo.getReportorPhone());
        	scheduleDOrG.setReportTime(prpLRegistVo.getReportTime());
        }
        if(vo != null) {
        	scheduleDOrG.setInuredName(vo.getInsuredName());
        }

		List<HandleScheduleReqScheduleItemDOrG> scheduleItemListResult = new ArrayList<HandleScheduleReqScheduleItemDOrG>();
		HandleScheduleReqScheduleItemDOrG scheduleItemResult = new HandleScheduleReqScheduleItemDOrG();

		// 任务id
		String scheduleTaskId = "1";
		List<PrpLWfTaskVo> prpLWfTaskVoList = wfTaskHandleService.findPrpLWfTaskOutTimeDescByRegistNo(prpLRegistVo.getRegistNo());
		if(prpLWfTaskVoList!=null && prpLWfTaskVoList.size()>0){
//		// 流入时间降序排
//			Collections.sort(prpLWfTaskVoList, new Comparator<PrpLWfTaskVo>() {
//			@Override
//			public int compare(PrpLWfTaskVo o1,PrpLWfTaskVo o2) {
//					return o2.getTaskInTime().compareTo(o1.getTaskInTime());
//				}
//			});
			scheduleTaskId=prpLWfTaskVoList.get(0).getTaskId().toString();
		}
		scheduleItemResult.setOriginalTaskId(scheduleTaskId);
		
		//查询工作流表人伤节点taskId
		String personTaskId = "1";
		 PrpLWfTaskVo wfTaskVo = wfTaskHandleService.findWftaskInByRegistnoAndSubnode(prpLRegistVo.getRegistNo(), "PLNext");
		 if(wfTaskVo != null){
			 personTaskId = wfTaskVo.getTaskId().toString();
		 }
		 scheduleItemResult.setTaskId(personTaskId);
		 scheduleItemResult.setNodeType("PLoss");
		 scheduleItemResult.setItemNo("2");
		 
		 SysUserVo sysUser = scheduleTaskService.findPrpduserByUserCode(submitVo.getAssignUser(), "1");
		 
		 if(sysUser != null) {
			 scheduleItemResult.setNextHandlerName(sysUser.getUserName());
		 }
		 
		 if(StringUtils.isNotBlank(submitVo.getAssignUser())) {
			 scheduleItemResult.setNextHandlerCode(submitVo.getAssignUser());
		 }else {
			 if(sysUser != null) {
				 scheduleItemResult.setScheduleObjectId(sysUser.getUserCode());
			 }
		 }
		 if(StringUtils.isNotBlank(submitVo.getAssignCom())) {
			 scheduleItemResult.setScheduleObjectId(submitVo.getAssignCom());
		 }else {
			 if(sysUser != null) {
				 scheduleItemResult.setScheduleObjectId(sysUser.getComCode());
			 }
		 }
		 
		 List<SysCodeDictVo> sysCode = wfTaskQueryService.findPrpDcompanyByUserCode(submitVo.getAssignCom());
		 if(sysCode != null && sysCode.size() > 0) {
			 scheduleItemResult.setScheduleObjectName(sysCode.get(0).getCodeName());
		 }
		 
		 scheduleItemListResult.add(scheduleItemResult);
		 
		scheduleDOrG.setScheduleItemList(scheduleItemListResult);
		body.setScheduleDOrG(scheduleDOrG);
		reqVo.setHead(head);
		reqVo.setBody(body);
		
		//String xmlToSend = ClaimBaseCoder.objToXml(reqVo);
		String url = SpringProperties.getProperty("MClaimPlatform_URL_IN")+"prplschedule/claimSubmissionOrReassignment.do";
		HandleScheduleDOrGBackReqVo voS = mobileCheckService.getHandelScheduleDOrDUrl(reqVo,url);
	
	}
	
	
	/**
	 * 设置头部信息
	 * @return
	 */
	private HeadReq setHeadReq(){
		HeadReq head = new HeadReq();
		head.setRequestType("ScheduleSubmit");
		head.setPassword("mclaim_psd");
		head.setUser("mclaim_user");
		return head;
	}


	
}
