package ins.sino.claimcar.lossprop.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.ws.rs.Path;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;

import com.alibaba.dubbo.config.annotation.Service;

import ins.framework.service.CodeTranService;
import ins.framework.utils.Beans;
import ins.platform.common.util.ConfigUtil;
import ins.platform.utils.DataUtils;
import ins.platform.vo.PrpLConfigValueVo;
import ins.platform.vo.SysCodeDictVo;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.AcheckUnderWriteFlag;
import ins.sino.claimcar.CodeConstants.CheckTaskType;
import ins.sino.claimcar.CodeConstants.RadioValue;
import ins.sino.claimcar.CodeConstants.UnderWriteFlag;
import ins.sino.claimcar.check.service.CheckHandleService;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.PrpLCheckDutyVo;
import ins.sino.claimcar.check.vo.PrpLCheckPropVo;
import ins.sino.claimcar.check.vo.PrpLCheckTaskVo;
import ins.sino.claimcar.check.vo.PrpLCheckVo;
import ins.sino.claimcar.claim.service.ClaimTaskService;
import ins.sino.claimcar.common.claimtext.service.ClaimTextService;
import ins.sino.claimcar.common.claimtext.vo.PrpLClaimTextVo;
import ins.sino.claimcar.common.losscharge.service.LossChargeService;
import ins.sino.claimcar.common.losscharge.vo.PrpLDlossChargeHisVo;
import ins.sino.claimcar.common.losscharge.vo.PrpLDlossChargeVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.constant.SubmitType;
import ins.sino.claimcar.flow.service.AssignService;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpDNodeVo;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.flow.vo.WfTaskSubmitVo;
import ins.sino.claimcar.ilog.defloss.vo.LIlogRuleResVo;
import ins.sino.claimcar.ilog.rule.service.IlogRuleService;
import ins.sino.claimcar.ilogFinalpowerInfo.vo.IlogFinalPowerInfoVo;
import ins.sino.claimcar.losscar.service.DeflossHandleIlogService;
import ins.sino.claimcar.losscar.service.DeflossHandleService;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.lossperson.service.PersTraceDubboService;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersHospitalVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersInjuredVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceMainVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceVo;
import ins.sino.claimcar.lossprop.vo.DefCommonVo;
import ins.sino.claimcar.lossprop.vo.DeflossActionVo;
import ins.sino.claimcar.lossprop.vo.PropModifyVo;
import ins.sino.claimcar.lossprop.vo.PropQueryVo;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropFeeHisVo;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropFeeVo;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropMainHisVo;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropMainVo;
import ins.sino.claimcar.lossprop.vo.SubmitNextVo;
import ins.sino.claimcar.manager.service.ManagerService;
import ins.sino.claimcar.manager.vo.PrpLPayCustomVo;
import ins.sino.claimcar.manager.vo.PrpdCheckBankMainVo;
import ins.sino.claimcar.manager.vo.PrpdIntermMainVo;
import ins.sino.claimcar.manager.vo.PrpdIntermServerVo;
import ins.sino.claimcar.other.service.AcheckService;
import ins.sino.claimcar.other.service.AcheckTaskService;
import ins.sino.claimcar.other.service.AssessorDubboService;
import ins.sino.claimcar.other.vo.PrpLAcheckVo;
import ins.sino.claimcar.other.vo.PrpLAssessorVo;
import ins.sino.claimcar.other.vo.PrpLAutoVerifyVo;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLCItemKindVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.rule.service.ClaimRuleApiService;
import ins.sino.claimcar.rule.vo.LossPropToVerifyRuleVo;
import ins.sino.claimcar.schedule.service.ScheduleService;
import ins.sino.claimcar.schedule.vo.PrpLScheduleDefLossVo;


@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"}, timeout = 1000000)
@Path("propLossHandleService")
public class PropLossHandleServiceImpl implements PropLossHandleService {
	@Autowired
	private PropLossService propLossService;
	@Autowired
	private PropTaskService propTaskService;
	@Autowired
	private ScheduleService scheduleService;
	@Autowired
	CheckTaskService checkTaskService;
	@Autowired
	private RegistQueryService registQueryService;
	@Autowired
	private ClaimTextService claimTextService;
	@Autowired
	private LossChargeService lossChargeService;
	@Autowired
	private WfTaskHandleService wfTaskHandleService;
	@Autowired
	private PolicyViewService policyViewService;
	@Autowired
	private ManagerService managerService;
	@Autowired
	LossCarService lossCarService;
	@Autowired
	PersTraceDubboService persTraceDubboService;
	@Autowired
	CodeTranService codeTranService;
	@Autowired
	private ClaimTaskService claimTaskService;
	@Autowired
	ClaimRuleApiService claimRuleApiService;
	@Autowired
	AssignService assignService;
	@Autowired
	AssessorDubboService assessorService;
	@Autowired
	DeflossHandleService deflossHandleService;
	@Autowired
	DeflossHandleIlogService deflossHandleIlogService;
	@Autowired
	private IlogRuleService ilogRuleService;
	@Autowired
	private  CheckHandleService checkHandleService;
	@Autowired
	private AcheckService acheckService;
	@Autowired
    private AcheckTaskService acheckTaskService;
	private static Logger logger = LoggerFactory.getLogger(PropLossHandleServiceImpl.class);
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.lossprop.service.PropLossHandleService#propModfifyQuery(ins.sino.claimcar.lossprop.vo.PropQueryVo)
	 */
	@Override
	public List<PropModifyVo> propModfifyQuery(PropQueryVo queryVo){
			//
		return null;
	}
	
	
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.lossprop.service.PropLossHandleService#savePropMain(ins.sino.claimcar.lossprop.vo.DeflossActionVo, ins.platform.vo.SysUserVo)
	 */
	@Override
	public void savePropMain(DeflossActionVo deflossVo,SysUserVo sysUserVo){	
		PrpLdlossPropMainVo prpLdlossPropMainVo=deflossVo.getLossPropMainVo();
		if(StringUtils.isNotBlank(prpLdlossPropMainVo.getHandlerIdCard())){
			//?????????????????????????????????  ??????????????????????????????
			prpLdlossPropMainVo.setHandlerIdCard(prpLdlossPropMainVo.getHandlerIdCard().replaceAll("\\s*",""));
		}
		//??????
		List<PrpLdlossPropFeeVo> propFeeList = prpLdlossPropMainVo.getPrpLdlossPropFees();
		if(propFeeList!=null && !propFeeList.isEmpty()){
			for(PrpLdlossPropFeeVo fee:prpLdlossPropMainVo.getPrpLdlossPropFees()){
				if(null==fee.getRecycleFlag()){
					fee.setRecycleFlag("0");
				}
			}
		}
		PrpdIntermMainVo intermMainVo = managerService.findIntermByUserCode(sysUserVo.getUserCode());
		if(intermMainVo !=null){//TODO ????????????????????? ?????? ?????? ??????
			prpLdlossPropMainVo.setInterMediaryFlag("1");//????????????
			prpLdlossPropMainVo.setInterMediaryinfoId(intermMainVo.getIntermCode());//???????????????????????? prpdintermmain.intermCode
		}else{
			prpLdlossPropMainVo.setInterMediaryFlag("0");//????????????
			prpLdlossPropMainVo.setInterMediaryinfoId("");
		}
		
		//??????????????????
		if(prpLdlossPropMainVo.getId()!=null){
			prpLdlossPropMainVo.setUpdateTime(new Date());
			prpLdlossPropMainVo.setUpdateUser(deflossVo.getUserCode());
		}else{
			
			prpLdlossPropMainVo.setMakeCom(sysUserVo.getComCode());
			prpLdlossPropMainVo.setValidFlag("1");
			prpLdlossPropMainVo.setUnderWriteFlag(CodeConstants.VeriFlag.INIT);
			prpLdlossPropMainVo.setLossState("00");
			prpLdlossPropMainVo.setCreateTime(new Date());
			prpLdlossPropMainVo.setCreateUser(deflossVo.getUserCode());
		}
		propLossService.saveOrUpdatePropMain(prpLdlossPropMainVo,sysUserVo);
		
		//???????????? ?????? ????????????ReLossCarId ???????????????????????????
		if(CodeConstants.defLossSourceFlag.ADDTIONALDEFLOSS.equals(prpLdlossPropMainVo.getDeflossSourceFlag()) && 
				prpLdlossPropMainVo.getReLossPropId()!=null){
			PrpLdlossPropMainVo lossMainOldVo = propLossService.findPropMainVoById(prpLdlossPropMainVo.getReLossPropId());
			if(lossMainOldVo.getReLossPropId()!=null){
				lossMainOldVo.setReLossPropId(null);
				propLossService.updatePropMain(lossMainOldVo);
			}
		}
		
		if(intermMainVo != null){//????????????
			//?????????????????????
			PrpLAssessorVo assessorOld = assessorService.findAssessorByLossId(prpLdlossPropMainVo.getRegistNo(), CodeConstants.TaskType.PROP
					,prpLdlossPropMainVo.getSerialNo(), prpLdlossPropMainVo.getInterMediaryinfoId());
			if(assessorOld == null){
				PrpLAssessorVo assessorVo = new PrpLAssessorVo();
				PrpdIntermMainVo intermVo = managerService.findIntermByUserCode(sysUserVo.getUserCode());
				if(intermVo != null){ //else ??????????????????????????????????????????????????????
					assessorVo.setIntermId(intermVo.getId());
					assessorVo.setIntermNameDetail(intermVo.getIntermName());
				}else{
					assessorVo.setIntermId(null);
					assessorVo.setIntermNameDetail("??????????????????????????????????????????????????????");
				}
				assessorVo.setRegistNo(prpLdlossPropMainVo.getRegistNo());
				assessorVo.setLossId(prpLdlossPropMainVo.getId());
				assessorVo.setSerialNo(prpLdlossPropMainVo.getSerialNo().toString());
				assessorVo.setTaskType(CodeConstants.TaskType.PROP);
				assessorVo.setLossDate(prpLdlossPropMainVo.getDefLossDate());
				assessorVo.setIntermcode(intermMainVo.getIntermCode());
				assessorVo.setIntermname(codeTranService.transCode("GongGuPayCode",intermMainVo.getIntermCode()));
				assessorVo.setLicenseNo(prpLdlossPropMainVo.getLicense());
				assessorVo.setUnderWriteFlag(CodeConstants.AssessorUnderWriteFlag.Loss);
				assessorVo.setCreateTime(new Date());
				assessorVo.setCreateUser(sysUserVo.getUserCode());
				assessorVo.setAssessorFee(prpLdlossPropMainVo.getAssessorFee());
				assessorVo.setVeriLoss(prpLdlossPropMainVo.getSumVeriLoss());
				assessorVo.setComCode(sysUserVo.getComCode());
				assessorVo.setKindCode(checkHandleService.getCarKindCode(prpLdlossPropMainVo.getRegistNo()));
				assessorService.saveOrUpdatePrpLAssessor(assessorVo,sysUserVo);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.lossprop.service.PropLossHandleService#saveOrUpdateDefloss(ins.sino.claimcar.lossprop.vo.DeflossActionVo, ins.platform.vo.SysUserVo)
	 */
	@Override
	public void saveOrUpdateDefloss(DeflossActionVo deflossVo,SysUserVo sysUserVo){	
		this.savePropMain(deflossVo,sysUserVo);
		PrpLdlossPropMainVo prpLdlossPropMainVo=deflossVo.getLossPropMainVo();
		
		String currentNode=prpLdlossPropMainVo.getCurrentNode();
		List<PrpLDlossChargeVo> lossChargeVos=deflossVo.getLossChargeVos();
		this.saveClaimText(deflossVo,currentNode);
		if(lossChargeVos!=null && lossChargeVos.size()>0){
			Map<String,String> kindMap = new HashMap<String,String>(); 
			List<PrpLCItemKindVo> itemKinds = policyViewService.findItemKinds(prpLdlossPropMainVo.getRegistNo(),"Y");
			for(PrpLCItemKindVo itemKind :itemKinds){
				kindMap.put(itemKind.getKindCode(),itemKind.getKindName());
			}
			for(PrpLDlossChargeVo lossCharge : lossChargeVos){
				if(lossCharge.getId()!=null){
					lossCharge.setUpdateTime(new Date());
					lossCharge.setUpdateUser(deflossVo.getUserCode());
				}else{
					lossCharge.setBusinessId(prpLdlossPropMainVo.getId());
					lossCharge.setRegistNo(prpLdlossPropMainVo.getRegistNo());
					lossCharge.setBusinessType(FlowNode.DLProp.name());
					lossCharge.setKindName(kindMap.get(lossCharge.getKindCode()));
					lossCharge.setCreateTime(new Date());
					lossCharge.setCreateUser(deflossVo.getUserCode());
				}
				
			}
			lossChargeService.saveOrUpdte(lossChargeVos);
		}else{
			lossChargeService.delCharge(prpLdlossPropMainVo.getId(), FlowNode.DLProp.name());
		}
		
	}
	
	// TODO ??????????????? ????????????
	private List<PrpLWfTaskVo> submitNextTask(PrpLdlossPropMainVo lossPropMainVo,SubmitNextVo nextVo,SysUserVo sysUserVo) {
		WfTaskSubmitVo taskSubmitVo = new WfTaskSubmitVo();
		PrpLWfTaskVo taskVo = wfTaskHandleService.queryTask(Double.parseDouble(nextVo.getFlowTaskId()));
		Beans.copy().from(nextVo).to(taskSubmitVo);
		taskSubmitVo.setCurrentNode(FlowNode.valueOf(nextVo.getCurrentNode()));
		taskSubmitVo.setFlowId(taskVo.getFlowId());
		taskSubmitVo.setFlowTaskId(new BigDecimal(nextVo.getFlowTaskId()));
		taskSubmitVo.setNextNode(FlowNode.valueOf(nextVo.getNextNode()));
		taskSubmitVo.setHandleruser(sysUserVo.getUserCode());
		if("1".equals(nextVo.getAutoLossFlag())){
			taskSubmitVo.setHandleruser("AUTO");
			taskSubmitVo.setAssignUser("AUTO");
		}
		taskSubmitVo.setComCode(taskVo.getComCode());
		String comCode = nextVo.getComCode();
		
		//??????
		if(nextVo.getNextNode().startsWith("VLProp_LV")){//???????????????
			if(nextVo.getCurrentNode().startsWith("VLProp_LV")){//???????????????
				String nextUserCode = null;
				String nextComCode = null;
				StringBuffer nextNode = new StringBuffer(nextVo.getNextNode());
				StringBuffer node = new StringBuffer(nextVo.getCurrentNode());
				int nextLevel = Integer.valueOf(nextNode.delete(0, 9).toString());//???????????????
				int level = Integer.valueOf(node.delete(0, 9).toString());//???????????????
				if(level == 9 && "backLower".equals(nextVo.getAuditStatus())){//?????????????????????????????????
					PrpLWfTaskVo preTaskVo = wfTaskHandleService.findEndTask(lossPropMainVo.getRegistNo(), 
							String.valueOf(lossPropMainVo.getId()), FlowNode.valueOf(nextVo.getNextNode())).get(0);
					nextUserCode = preTaskVo.getHandlerUser();
					nextComCode = preTaskVo.getHandlerCom();
				}else{
					if("audit".equals(nextVo.getAuditStatus())&&nextLevel >= 9){//??????????????????
						nextUserCode = null;
						nextComCode = "00010000";
						taskSubmitVo.setHandleruser(null);
						taskSubmitVo.setHandlertime(null);
					}else{//????????????????????????????????????
						SysUserVo assUserVo = assignService.execute(taskSubmitVo.getNextNode(),comCode,sysUserVo, "");
						if(assUserVo == null){
							throw new IllegalArgumentException(FlowNode.valueOf(nextVo.getNextNode()).getName()+"??????????????? ???");
						}
						 nextUserCode = assUserVo.getUserCode();
						 nextComCode = assUserVo.getComCode();
					}
				}
				taskSubmitVo.setAssignCom(nextComCode);
				taskSubmitVo.setAssignUser(nextUserCode);
			}else{//???????????????
				//??????????????????????????????
				if(!"1".equals(nextVo.getAutoLossFlag())){
					List<PrpLWfTaskVo> taskList = wfTaskHandleService.findEndTask(lossPropMainVo.getRegistNo(), 
							lossPropMainVo.getId().toString(),FlowNode.VLProp);
					if(taskList!=null &&  !taskList.isEmpty()){
						PrpLWfTaskVo preTaskVo = taskList.get(0);
						if(nextVo.getNextNode().equals(preTaskVo.getSubNodeCode())){
							//???????????????????????????
							if(assignService.validUserCode(preTaskVo.getHandlerUser())){
								taskSubmitVo.setAssignCom(preTaskVo.getHandlerCom());
								taskSubmitVo.setAssignUser(preTaskVo.getHandlerUser());
							}
						}
					}
					
					if(StringUtils.isBlank(taskSubmitVo.getAssignUser())){
						SysUserVo assUserVo = assignService.execute(taskSubmitVo.getNextNode(),comCode,sysUserVo, "0");
						if(assUserVo == null){
							throw new IllegalArgumentException(FlowNode.valueOf(nextVo.getNextNode()).getName()+"??????????????? ???");
						}
						taskSubmitVo.setAssignCom(assUserVo.getComCode());
						taskSubmitVo.setAssignUser(assUserVo.getUserCode());
					}
				}
			}
		}else if(!taskSubmitVo.getNextNode().equals("END")){
			SysUserVo assUserVo = assignService.execute(taskSubmitVo.getNextNode(),comCode,sysUserVo, "");
			if(assUserVo == null){
				throw new IllegalArgumentException(FlowNode.valueOf(nextVo.getNextNode()).getName()+"??????????????? ???");
			}
			taskSubmitVo.setAssignCom(assUserVo.getComCode());
			taskSubmitVo.setAssignUser(assUserVo.getUserCode());
		}else{
			taskSubmitVo.setAssignCom(sysUserVo.getComCode());
			taskSubmitVo.setAssignUser(sysUserVo.getUserCode());
		}
		
		if(CodeConstants.AuditStatus.BACKLOSS.equals(lossPropMainVo.getAuditStatus())
				||CodeConstants.AuditStatus.BACKLOWER.equals(lossPropMainVo.getAuditStatus())){
			taskSubmitVo.setSubmitType(SubmitType.B);
			taskSubmitVo.setHandleIdKey(lossPropMainVo.getId().toString());
		}
		List<PrpLWfTaskVo> wfTaskVoList = new ArrayList<PrpLWfTaskVo>();
		try{
			wfTaskVoList = wfTaskHandleService.submitLossProp(lossPropMainVo,taskSubmitVo);
		}catch(Exception e){
			e.printStackTrace();
			
		}
		
		
		//???????????????  ???????????? ???????????? ??????????????? ???????????????????????? ??????????????????
		//CodeConstants.defLossSourceFlag.MODIFYDEFLOSS.equals(lossPropMainVo.getDeflossSourceFlag()) 
		if( CodeConstants.AuditStatus.SUBMITVLOSS.equals(lossPropMainVo.getAuditStatus())){
			List<PrpLWfTaskVo> comTaskList = lossCarService.modifyToSubMitComp(lossPropMainVo.getRegistNo(),sysUserVo);
			wfTaskVoList.addAll(comTaskList);
		}

		return wfTaskVoList;
	}	
	
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.lossprop.service.PropLossHandleService#submitNextNode(ins.sino.claimcar.lossprop.vo.PrpLdlossPropMainVo, ins.sino.claimcar.lossprop.vo.SubmitNextVo, ins.platform.vo.SysUserVo)
	 */
	@Override
	public List<PrpLWfTaskVo> submitNextNode(PrpLdlossPropMainVo lossPropMainVo,SubmitNextVo nextVo,SysUserVo sysUserVo) {
		logger.info("?????????={},????????????????????????????????????????????????UnderWriteFlag?????????",lossPropMainVo.getRegistNo());
		PrpLWfTaskVo taskVo = wfTaskHandleService.queryTask(Double.parseDouble(nextVo.getFlowTaskId()));
		if(CodeConstants.HandlerStatus.END.equals(taskVo.getHandlerStatus())){
			throw new IllegalArgumentException("???????????????????????????????????????");
		}
		
		PrpLClaimTextVo claimTextVo = claimTextService.findClaimTextByNode(lossPropMainVo.getId(),nextVo.getCurrentNode(),"0");		
		String currentNode=nextVo.getCurrentNode();
		String upperNode = FlowNode.valueOf(currentNode).getUpperNode();
		
		if(upperNode.equals(FlowNode.DLoss.name()) || FlowNode.valueOf(upperNode).getUpperNode().equals(FlowNode.DLoss.name())){//????????????
			lossPropMainVo.setDefEndDate(new Date());
		}else{
			logger.info("?????????registno={},?????????????????????????????????upperNode!={}",lossPropMainVo.getRegistNo(),FlowNode.DLoss.name());
			logger.info("??????UnderWriteFlag={}",CodeConstants.VeriFlag.PASS);
			lossPropMainVo.setUnderWriteFlag(CodeConstants.VeriFlag.PASS);
			lossPropMainVo.setUnderWriteEndDate(new Date());
		}
		for(PrpLdlossPropFeeVo prpLdlossPropFeeVo:lossPropMainVo.getPrpLdlossPropFees()){
			if("1".equals(prpLdlossPropFeeVo.getRecycleFlag())){
				lossPropMainVo.setRecycleFlag("1");
			}
		}
		propLossService.saveOrUpdatePropMain(lossPropMainVo,sysUserVo);
		
		//?????????????????????
		if(claimTextVo!=null){
			claimTextVo.setFlag("1");
			claimTextVo.setStatus(lossPropMainVo.getAuditStatus());
			claimTextVo.setOperatorCode(sysUserVo.getUserCode());
			claimTextVo.setOperatorName(sysUserVo.getUserName());
			claimTextVo.setComCode(sysUserVo.getComCode());
			claimTextService.saveOrUpdte(claimTextVo);
		}
		
		//this.saveLossIndex(lossPropMainVo,nextVo);
		//???????????????
		
		List<PrpLWfTaskVo> wfTaskVoList = submitNextTask(lossPropMainVo,nextVo,sysUserVo);
		//???????????????
		propLossService.savePropHis(lossPropMainVo);
		lossChargeService.saveChargeHis(lossPropMainVo.getId(), FlowNode.DLProp.name());
		
		//?????????????????????end??????
		if(nextVo.getAutoLossFlag()!=null&&"1".equals(nextVo.getAutoLossFlag())){
			wfTaskVoList = this.autoVerLossSubmitNext(lossPropMainVo, nextVo, wfTaskVoList.get(0), sysUserVo);
		}
		
		//??????????????? ??????PRPLASSESSOR
		if(CodeConstants.AuditStatus.SUBMITVLOSS.equals(lossPropMainVo.getAuditStatus())){
			if("1".equals(lossPropMainVo.getInterMediaryFlag())){//????????????
				//?????????????????????
				PrpLAssessorVo assessorOld = assessorService.findAssessorByLossId(lossPropMainVo.getRegistNo(), CodeConstants.TaskType.PROP,
						lossPropMainVo.getSerialNo(), lossPropMainVo.getInterMediaryinfoId());
				if(assessorOld != null &&  CodeConstants.AssessorUnderWriteFlag.Loss.equals(assessorOld.getUnderWriteFlag())){
					assessorOld.setUnderWriteFlag(CodeConstants.AssessorUnderWriteFlag.Verify);
					assessorOld.setLossDate(lossPropMainVo.getUnderWriteEndDate());
					
					String kindCode = "";
					if(lossPropMainVo.getSerialNo()==1){
						kindCode = CodeConstants.KINDCODE.KINDCODE_D2;
					}else{
						PrpLRegistVo registVo = registQueryService.findByRegistNo(lossPropMainVo.getRegistNo());
						if(CodeConstants.ReportType.BI.equals(registVo.getReportType())){
							kindCode = CodeConstants.KINDCODE.KINDCODE_B;
						}else{
							kindCode = CodeConstants.KINDCODE.KINDCODE_BZ;
						}
					}
					assessorOld.setAssessorFee(lossPropMainVo.getAssessorFee());
					assessorOld.setVeriLoss(lossPropMainVo.getSumVeriLoss());
					assessorOld.setKindCode(kindCode);
					assessorService.saveOrUpdatePrpLAssessor(assessorOld,sysUserVo);
				}
			}
			//???????????????????????????????????????
			if(StringUtils.isNotBlank(lossPropMainVo.getCheckCode())){
				acheckTaskService.addCheckFeeTaskOfDprop(lossPropMainVo, sysUserVo,"1");
			}
		}
		logger.info("????????????????????????????????????????????????UnderWriteFlag?????????");
		return wfTaskVoList;
		
	}
	
	@Override
	public void savePropLoss(PrpLdlossPropMainVo lossPropMainVo,SubmitNextVo nextVo,SysUserVo sysUserVo) {
		
		PrpLWfTaskVo taskVo = wfTaskHandleService.queryTask(Double.parseDouble(nextVo.getFlowTaskId()));
		if(CodeConstants.HandlerStatus.END.equals(taskVo.getHandlerStatus())){
			throw new IllegalArgumentException("???????????????????????????????????????");
		}
		
		PrpLClaimTextVo claimTextVo = claimTextService.findClaimTextByNode(lossPropMainVo.getId(),nextVo.getCurrentNode(),"0");
		if(CodeConstants.AuditStatus.SUBMITVLOSS.equals(lossPropMainVo.getAuditStatus())){
			lossPropMainVo.setUnderWriteFlag(CodeConstants.VeriFlag.PASS);
			lossPropMainVo.setUnderWriteEndDate(new Date());
		}else if(CodeConstants.AuditStatus.BACKLOSS.equals(lossPropMainVo.getAuditStatus())){
			lossPropMainVo.setUnderWriteFlag(CodeConstants.VeriFlag.BACK);
			lossPropMainVo.setUnderWriteEndDate(new Date());
		}else if(CodeConstants.AuditStatus.SUBMITLOSS.equals(lossPropMainVo.getAuditStatus())){
			lossPropMainVo.setDefEndDate(new Date());
		}
		
		for(PrpLdlossPropFeeVo prpLdlossPropFeeVo:lossPropMainVo.getPrpLdlossPropFees()){
			if("1".equals(prpLdlossPropFeeVo.getRecycleFlag())){
				lossPropMainVo.setRecycleFlag("1");
			}
		}
		propLossService.saveOrUpdatePropMain(lossPropMainVo,sysUserVo);
		
		//?????????????????????
		if(claimTextVo!=null){
			claimTextVo.setFlag("1");
			claimTextVo.setStatus(lossPropMainVo.getAuditStatus());
			claimTextVo.setOperatorCode(sysUserVo.getUserCode());
			claimTextVo.setOperatorName(sysUserVo.getUserName());
			claimTextVo.setComCode(sysUserVo.getComCode());
			claimTextService.saveOrUpdte(claimTextVo);
		}
		
		//???????????????
		propLossService.savePropHis(lossPropMainVo);
		lossChargeService.saveChargeHis(lossPropMainVo.getId(), FlowNode.DLProp.name());
	}
	
	@Override
	public List<PrpLWfTaskVo> submitTask(PrpLdlossPropMainVo lossPropMainVo,SubmitNextVo nextVo,SysUserVo sysUserVo){
		//???????????????
		List<PrpLWfTaskVo> wfTaskVoList = submitNextTask(lossPropMainVo,nextVo,sysUserVo);
		
		//?????????????????????end??????
		if(nextVo.getAutoLossFlag()!=null&&"1".equals(nextVo.getAutoLossFlag())){
			wfTaskVoList = this.autoVerLossSubmitNext(lossPropMainVo, nextVo, wfTaskVoList.get(0), sysUserVo);
		}
		
		//??????????????? ??????PRPLASSESSOR
		if(CodeConstants.AuditStatus.SUBMITVLOSS.equals(lossPropMainVo.getAuditStatus())){
			if("1".equals(lossPropMainVo.getInterMediaryFlag())){//????????????
				//?????????????????????
				PrpLAssessorVo assessorOld = assessorService.findAssessorByLossId(lossPropMainVo.getRegistNo(), CodeConstants.TaskType.PROP,
						lossPropMainVo.getSerialNo(), lossPropMainVo.getInterMediaryinfoId());
				if(assessorOld != null &&  CodeConstants.AssessorUnderWriteFlag.Loss.equals(assessorOld.getUnderWriteFlag())){
					assessorOld.setUnderWriteFlag(CodeConstants.AssessorUnderWriteFlag.Verify);
					assessorOld.setLossDate(lossPropMainVo.getUnderWriteEndDate());
					
					String kindCode = "";
					if(lossPropMainVo.getSerialNo()==1){
						kindCode = CodeConstants.KINDCODE.KINDCODE_D2;
					}else{
						PrpLRegistVo registVo = registQueryService.findByRegistNo(lossPropMainVo.getRegistNo());
						if(CodeConstants.ReportType.BI.equals(registVo.getReportType())){
							kindCode = CodeConstants.KINDCODE.KINDCODE_B;
						}else{
							kindCode = CodeConstants.KINDCODE.KINDCODE_BZ;
						}
					}
					assessorOld.setAssessorFee(lossPropMainVo.getAssessorFee());
					assessorOld.setVeriLoss(lossPropMainVo.getSumVeriLoss());
					assessorOld.setKindCode(kindCode);
					assessorService.saveOrUpdatePrpLAssessor(assessorOld,sysUserVo);
				}
			}
			
			//???????????????????????????????????????
			if(StringUtils.isNotBlank(lossPropMainVo.getCheckCode())){
				acheckTaskService.addCheckFeeTaskOfDprop(lossPropMainVo, sysUserVo, "1");
			}
		}
		
		return wfTaskVoList;
	}
	
	public List<PrpLWfTaskVo> autoVerLossSubmitNext(PrpLdlossPropMainVo lossPropMainVo,SubmitNextVo nextVo,PrpLWfTaskVo wfTaskVo,SysUserVo sysUserVo){
		logger.info("?????????registno={},??????????????????????????????????????????????????????UnderWriteFlag??????",lossPropMainVo.getRegistNo());
		lossPropMainVo.setUnderWriteFlag(CodeConstants.VeriFlag.PASS);
		lossPropMainVo.setUnderWriteEndDate(new Date());
		lossPropMainVo.setUnderWriteCode("autoLoss");
		lossPropMainVo.setUnderWriteName("TODO");
		lossPropMainVo.setAuditStatus(CodeConstants.AuditStatus.SUBMITVLOSS);
		List<PrpLDlossChargeVo> lossChargeVoList=
				lossChargeService.findLossChargeVos(lossPropMainVo.getRegistNo(), lossPropMainVo.getId(), FlowNode.DLProp.name());
		//??????????????????????????????????????????????????????????????????????????????
		if(nextVo.getNotModPrice()!=null && !nextVo.getNotModPrice()){
			lossPropMainVo.setSumVeriLoss(lossPropMainVo.getSumDefloss());
			lossPropMainVo.setSumVeriFee(lossPropMainVo.getSumLossFee());
			lossPropMainVo.setVerirescueFee(lossPropMainVo.getDefRescueFee());
			for(PrpLdlossPropFeeVo lossPropFeeVo:lossPropMainVo.getPrpLdlossPropFees()){
				//?????????????????????????????????????????????????????????
				lossPropFeeVo.setVeriLossQuantity(lossPropFeeVo.getLossQuantity());//??????
				lossPropFeeVo.setVeriUnitPrice(lossPropFeeVo.getUnitPrice());//??????
				lossPropFeeVo.setVeriRecylePrice(lossPropFeeVo.getRecyclePrice());//????????????
				lossPropFeeVo.setSumVeriLoss(lossPropFeeVo.getSumDefloss());
			}
			
			//??????PrpLDlossCharge????????????????????????
			for(PrpLDlossChargeVo lossChargeVo:lossChargeVoList){
				lossChargeVo.setVeriChargeFee(lossChargeVo.getChargeFee());
			}
		}
		sysUserVo.setUserCode("AUTO");
		sysUserVo.setUserName("AUTO");
		propLossService.saveOrUpdatePropMain(lossPropMainVo,sysUserVo);
		lossChargeService.saveOrUpdte(lossChargeVoList);
		
		nextVo.setCurrentNode(FlowNode.VLProp_LV0.name());
		PrpLClaimTextVo claimTextVo = new PrpLClaimTextVo();
		claimTextVo.setDescription("????????????");
		claimTextVo.setStatus(CodeConstants.AuditStatus.SUBMITVLOSS);
		claimTextVo.setNodeCode(FlowNode.VLProp_LV0.name());
		DeflossActionVo deflossVo = new DeflossActionVo();
		deflossVo.setLossPropMainVo(lossPropMainVo);
		deflossVo.setClaimTextVo(claimTextVo);
		deflossVo.setUserCode(sysUserVo.getUserCode());
		deflossVo.setUserName(sysUserVo.getUserName());
		deflossVo.setComCode(sysUserVo.getComCode());
		this.saveClaimText(deflossVo, nextVo.getCurrentNode());
		
		nextVo.setNextNode(FlowNode.END.name());
		nextVo.setFlowTaskId(wfTaskVo.getTaskId().toString());
		List<PrpLWfTaskVo> wfTaskVoList = submitNextTask(lossPropMainVo,nextVo,sysUserVo);
		//???????????????
		propLossService.savePropHis(lossPropMainVo);
		lossChargeService.saveChargeHis(lossPropMainVo.getId(), FlowNode.DLProp.name());
		logger.info("?????????registno={},?????????????????????????????????????????????,?????????????????????UnderWriteFlag={}",lossPropMainVo.getRegistNo(),lossPropMainVo.getUnderWriteFlag());
		return wfTaskVoList;
	}
		
	
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.lossprop.service.PropLossHandleService#saveClaimText(ins.sino.claimcar.lossprop.vo.DeflossActionVo, java.lang.String)
	 */
	@Override
	public  void saveClaimText(DeflossActionVo deflossVo,String nodeCode){
		PrpLdlossPropMainVo lossPropMainVo=deflossVo.getLossPropMainVo();
		PrpLClaimTextVo claimTextVo = deflossVo.getClaimTextVo();
		
		if(nodeCode.startsWith("DL")){//??????????????????
			claimTextVo.setSumLossFee(lossPropMainVo.getSumDefloss());
			claimTextVo.setRemark("??????????????????");
		}else{//??????????????????
			claimTextVo.setSumLossFee(lossPropMainVo.getSumVeriLoss());
			claimTextVo.setRemark("??????????????????");
		}
		
		if(claimTextVo.getId()!=null){//id?????????????????????
			claimTextVo.setUpdateTime(new Date());
			claimTextVo.setUpdateUser(deflossVo.getUserCode());
			
		}else{//id?????????????????????
			claimTextVo.setBussTaskId(lossPropMainVo.getId());
			claimTextVo.setRegistNo(lossPropMainVo.getRegistNo());
			claimTextVo.setTextType(CodeConstants.ClaimText.OPINION);
			claimTextVo.setNodeCode(nodeCode);//?????? ????????????
			claimTextVo.setBigNode(FlowNode.DLProp.name());
			claimTextVo.setOperatorCode(deflossVo.getUserCode());
			claimTextVo.setOperatorName(deflossVo.getUserName());
			claimTextVo.setComCode(deflossVo.getComCode());
//			claimTextVo.setComName(deflossVo.getComName());
			claimTextVo.setInputTime(new Date());
			claimTextVo.setCreateTime(new Date());
			claimTextVo.setCreateUser(deflossVo.getUserCode());		
		}
		if(nodeCode.equals(FlowNode.VLProp_LV0.name())){//????????????
			claimTextVo.setFlag("1");//???????????????0???1 ???????????????????????????
		}else{
			claimTextVo.setFlag("0");//???????????????0???1 ???????????????????????????
//			claimTextVo.setSumLossFee(lossPropMainVo.getSumLossFee()); 
			//??????????????? ????????????,?????????????????????????????????????????????
			claimTextVo.setStatus(CodeConstants.AuditStatus.SAVE);
		}
		
		claimTextService.saveOrUpdte(claimTextVo);
	}
	
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.lossprop.service.PropLossHandleService#initPropVerify(ins.sino.claimcar.lossprop.vo.DeflossActionVo, ins.platform.vo.SysUserVo)
	 */
	@Override
	public DeflossActionVo initPropVerify(DeflossActionVo deflossVo,SysUserVo userVo){
		Long businessId = deflossVo.getBusinessId();
		PrpLWfTaskVo taskVo = deflossVo.getTaskVo();
		PrpLRegistVo registVo = registQueryService.findByRegistNo(deflossVo.getRegistNo());
		PrpLdlossPropMainVo lossPropMainVo=propLossService.findPropMainVoById(businessId);
		List<PrpLClaimTextVo> claimTextVos = claimTextService.findClaimTextList(businessId,registVo.getRegistNo(),FlowNode.DLProp.name());
		String flag ="0";
		if(CodeConstants.HandlerStatus.END.equals(taskVo.getHandlerStatus())){
			flag="1";
		}
		
		if(CodeConstants.HandlerStatus.INIT.equals(taskVo.getHandlerStatus())){
			wfTaskHandleService.tempSaveTask(taskVo.getTaskId().doubleValue(),businessId.toString(),userVo.getUserCode(),userVo.getComCode());
		}
		//????????? ?????????
		String underWiteIdNo = lossPropMainVo.getUnderWriteIdCard();
		if(StringUtils.isBlank(underWiteIdNo)){
			underWiteIdNo = userVo.getIdentifyNumber();
		}
		if(StringUtils.isNotBlank(underWiteIdNo) && underWiteIdNo.length()>6){
			underWiteIdNo = underWiteIdNo.substring(0,6)+"************";
			lossPropMainVo.setUnderWriteIdCard(underWiteIdNo);
		}
		//???????????????
		if(StringUtils.isBlank(lossPropMainVo.getUnderWriteName())){
			lossPropMainVo.setUnderWriteName(userVo.getUserName());
		}
		//????????? ?????????
		String handlerIdNo=lossPropMainVo.getHandlerIdCard();
		if(StringUtils.isNotBlank(handlerIdNo) && handlerIdNo.length()>6){
			handlerIdNo=lossPropMainVo.getHandlerIdCard().substring(0,6)+"************";
			lossPropMainVo.setHandlerIdCard(handlerIdNo);
		}
		
		PrpLClaimTextVo claimTextVo = claimTextService.findClaimTextByNode(businessId,taskVo.getSubNodeCode(),flag); 

		List<PrpLDlossChargeVo> lossChargeVos = lossChargeService.findLossChargeVos(businessId,FlowNode.DLProp.name());
		lossPropMainVo.setFlowTaskId(taskVo.getTaskId().doubleValue());//?????????ID
		lossPropMainVo.setCurrentNode(taskVo.getSubNodeCode());//??????????????????
		lossPropMainVo.setHandlerStatus(taskVo.getHandlerStatus());
		
		if(claimTextVo==null){
			claimTextVo = new PrpLClaimTextVo();
			claimTextVo.setRegistNo(deflossVo.getRegistNo());
			claimTextVo.setTextType(CodeConstants.ClaimText.OPINION);
			claimTextVo.setNodeCode(FlowNode.VLProp.name());
			claimTextVo.setOperatorCode(deflossVo.getUserCode());
			claimTextVo.setOperatorName(deflossVo.getUserName());
		}
		deflossVo.setLossPropMainVo(lossPropMainVo);
		deflossVo.setClaimTextVo(claimTextVo);
		deflossVo.setClaimTextVos(claimTextVos);
		deflossVo.setLossChargeVos(lossChargeVos);
		
		// ????????????
		List<PrpLDlossCarMainVo> lossCarMainList = lossCarService.findLossCarMainByRegistNo(registVo.getRegistNo());
		List<PrpLdlossPropFeeVo> dlossPropFeeVos = propLossService.findPropVoListByRegistNo(registVo.getRegistNo());
		List<PrpLDlossPersTraceVo> traceList = this.findPersTraceList(registVo.getRegistNo());
		List<PrpLdlossPropMainVo> lossPropMainVos = propLossService.findPropMainByRegistNo(registVo.getRegistNo());
		//???????????????
		List<PrpLDlossPersTraceMainVo> lossPersMainList = 
				persTraceDubboService.findPersTraceMainVoList(registVo.getRegistNo());
		for(PrpLDlossPersTraceVo trace:traceList){
			trace.setRemark(lossPersMainList.get(0).getTraceTimes().toString());
		}
		deflossVo.setLossPropMainVos(lossPropMainVos);
		deflossVo.setLossCarMainList(lossCarMainList);
		deflossVo.setLossPropFeeVos(dlossPropFeeVos);
		deflossVo.setLossPersTraceList(traceList);
		
		return deflossVo;
	}
	
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.lossprop.service.PropLossHandleService#savePropVerify(ins.sino.claimcar.lossprop.vo.DeflossActionVo, ins.platform.vo.SysUserVo)
	 */
	@Override
	public void savePropVerify(DeflossActionVo deflossVo,SysUserVo sysUserVo){
		PrpLdlossPropMainVo lossPropMainVo=deflossVo.getLossPropMainVo();
		String currentNode=lossPropMainVo.getCurrentNode();
		lossPropMainVo.setUpdateTime(new Date());
		lossPropMainVo.setUpdateUser(deflossVo.getUserCode());
		
		lossPropMainVo.setUnderWriteCode(sysUserVo.getUserCode());
		lossPropMainVo.setUnderWriteCom(sysUserVo.getComCode());
		lossPropMainVo.setUnderWriteIdCard(sysUserVo.getIdentifyNumber());
		lossPropMainVo.setUnderWriteName(sysUserVo.getUserName());
		
		propLossService.saveOrUpdatePropMain(lossPropMainVo,sysUserVo);
		this.saveClaimText(deflossVo, currentNode);
		List<PrpLDlossChargeVo> lossChargeVos = deflossVo.getLossChargeVos();
		if(lossChargeVos!=null && lossChargeVos.size()>0){
			lossChargeService.saveOrUpdte(lossChargeVos);
		}
		wfTaskHandleService.tempSaveTask(lossPropMainVo.getFlowTaskId(),lossPropMainVo.getId().toString(),deflossVo.getUserCode(),deflossVo.getComCode());
		
		
	}
	
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.lossprop.service.PropLossHandleService#acceptDefloss(java.lang.Double, ins.sino.claimcar.lossprop.vo.DeflossActionVo, ins.platform.vo.SysUserVo)
	 */
	@Override
	public PrpLdlossPropMainVo acceptDefloss(Double flowTaskId,DeflossActionVo deflossVo,SysUserVo sysUserVo) {
		PrpLWfTaskVo taskVo = wfTaskHandleService.queryTask(flowTaskId);
		if(CodeConstants.HandlerStatus.DOING.equals(taskVo.getHandlerStatus())){
            throw new IllegalArgumentException("???????????????????????????????????????");
        }
		PrpLRegistVo registVo = registQueryService.findByRegistNo(taskVo.getRegistNo());
		deflossVo.setRegistNo(taskVo.getRegistNo());
		deflossVo.setBusinessId(Long.parseLong(taskVo.getHandlerIdKey()));
		deflossVo.setTaskVo(taskVo);
		
		this.getDefloss(deflossVo,registVo,sysUserVo);
		this.savePropMain(deflossVo,sysUserVo);
		
		String businessId = deflossVo.getLossPropMainVo().getId().toString();
		wfTaskHandleService.tempSaveTask(flowTaskId,businessId,sysUserVo.getUserCode(),sysUserVo.getComCode());
		return deflossVo.getLossPropMainVo();
	}
	
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.lossprop.service.PropLossHandleService#initPropDefloss(ins.sino.claimcar.lossprop.vo.DeflossActionVo, double, ins.platform.vo.SysUserVo)
	 */
	@Override
	public DeflossActionVo initPropDefloss(DeflossActionVo deflossVo,double flowTaskId,SysUserVo sysUserVo){
		PrpLWfTaskVo taskVo = wfTaskHandleService.queryTask(flowTaskId);
		Long businessId = Long.parseLong(taskVo.getHandlerIdKey());
		deflossVo.setBusinessId(Long.parseLong(taskVo.getHandlerIdKey()));
		deflossVo.setRegistNo(taskVo.getRegistNo());
		deflossVo.setTaskVo(taskVo);
		String status = taskVo.getHandlerStatus(); //????????????
		PrpLRegistVo registVo = registQueryService.findByRegistNo(taskVo.getRegistNo());
		deflossVo.setRegistVo(registVo);
		DefCommonVo commonVo = new DefCommonVo();
		
		if((FlowNode.DLProp.name().equals(taskVo.getSubNodeCode())
				|| FlowNode.DLPropAdd.name().equals(taskVo.getSubNodeCode()))&& 
				!CodeConstants.WorkStatus.BYBACK.equals(taskVo.getWorkStatus())&&
				CodeConstants.HandlerStatus.INIT.equals(status)){//???????????????
			
			this.getDefloss(deflossVo, registVo,sysUserVo);//??????????????? ?????????
			//???????????? ????????????
			if(FlowNode.DLProp.name().equals(taskVo.getSubNodeCode())){
				commonVo.setAcceptFlag("0");
			}
			

		}else{// ?????? ????????????????????????????????????
			String flag ="0";
			if(CodeConstants.HandlerStatus.END.equals(status)){
				flag="1";
			}
			
			PrpLdlossPropMainVo lossPropMainVo = propLossService.findPropMainVoById(businessId);
			lossPropMainVo.setHandlerCode(sysUserVo.getUserCode());
			if(lossPropMainVo.getHandlerCode()==null){
				lossPropMainVo.setHandlerIdCard(sysUserVo.getIdentifyNumber());
				lossPropMainVo.setHandlerName(sysUserVo.getUserName());
//				lossPropMainVo.setComCode(sysUserVo.getComCode());
			}
			List<PrpLClaimTextVo> claimTextVos = claimTextService.findClaimTextList(businessId,lossPropMainVo.getRegistNo(),FlowNode.DLProp.name());
			PrpLClaimTextVo claimTextVo = claimTextService.findClaimTextByNode(businessId,taskVo.getSubNodeCode(),flag); 
			List<PrpLDlossChargeVo> lossChargeVos = lossChargeService.findLossChargeVos(businessId,FlowNode.DLProp.name());
		
			deflossVo.setLossPropMainVo(lossPropMainVo);
			deflossVo.setClaimTextVo(claimTextVo);
			deflossVo.setClaimTextVos(claimTextVos);
			deflossVo.setClaimTextVo(claimTextVo);
			deflossVo.setLossChargeVos(lossChargeVos);	
			
			if(CodeConstants.HandlerStatus.INIT.equals(taskVo.getHandlerStatus())){
				wfTaskHandleService.tempSaveTask(flowTaskId,taskVo.getHandlerIdKey(),sysUserVo.getUserCode(),sysUserVo.getComCode());
			}
		}
		PrpLdlossPropMainVo lossPropMainVo=deflossVo.getLossPropMainVo();
		
		
		//???????????????????????????
		Map<String,String> kindMap=new HashMap<String, String>();
		//??????????????????????????????????????????
		List<PrpLCItemKindVo> itemKindVos=policyViewService.findItemKinds(lossPropMainVo.getRegistNo(), "Y");
		for(PrpLCItemKindVo itemKindVo:itemKindVos){
			kindMap.put(itemKindVo.getKindCode(), itemKindVo.getKindName());
		}
		
		//????????????????????????
//		if("1".equals(lossPropMainVo.getInterMediaryFlag())){
//			//????????????????????????
//			PrpdIntermMainVo intermMainVo=managerService.findIntermByCode(lossPropMainVo.getInterMediaryinfoId());
//			if(intermMainVo==null){
//				throw new IllegalArgumentException("????????????????????????");
//			}
//			List<PrpdIntermServerVo> intermServiceList=intermMainVo.getPrpdIntermServers();//??????????????????
//			StringBuffer intermStanders=new StringBuffer();
//			Map<String,String> intermMap=new HashMap<String, String>();
//			for(PrpdIntermServerVo serverVo:intermServiceList){
//				//?????????????????????????????????
//				intermStanders.append(serverVo.getServiceType()+"-"+serverVo.getFeeStandard()+",");
//
//				//serviceType-????????????????????????,feeStandard-????????????
//			}
//			intermMap.put("2", "????????????");
//			intermMap.put("3", "????????????");
//			deflossVo.setIntermMap(intermMap);
//			commonVo.setIntermStanders(intermStanders.toString());
//		}
		
		//????????????
		List<PrpLDlossCarMainVo> lossCarMainList = lossCarService.findLossCarMainByRegistNo(registVo.getRegistNo());
		List<PrpLdlossPropFeeVo> dlossPropFeeVos = propLossService.findPropVoListByRegistNo(registVo.getRegistNo());
		List<PrpLDlossPersTraceVo> traceList = this.findPersTraceList(registVo.getRegistNo());
		List<PrpLdlossPropMainVo> lossPropMainVos = propLossService.findPropMainByRegistNo(registVo.getRegistNo());
		deflossVo.setLossCarMainList(lossCarMainList);
		deflossVo.setLossPropFeeVos(dlossPropFeeVos);
		deflossVo.setLossPersTraceList(traceList);
		deflossVo.setLossPropMainVos(lossPropMainVos);
		
		if(lossPropMainVo.getSerialNo() ==1){//?????????
			PrpLCItemKindVo itemKindVo =policyViewService.findItemKindByKindCode(lossPropMainVo.getRegistNo(), CodeConstants.KINDCODE.KINDCODE_D2);//??????????????????????????????????????????
			//?????? ????????????
			if(itemKindVo!=null){
				commonVo.setAmount(itemKindVo.getAmount());
				lossPropMainVo.setHaveKindD("1");
			}
		}
		
		deflossVo.setKindMap(kindMap);
		commonVo.setHandleStatus(taskVo.getHandlerStatus());
		lossPropMainVo.setCurrentNode(taskVo.getSubNodeCode());
		lossPropMainVo.setFlowTaskId(taskVo.getTaskId().doubleValue());
		commonVo.setFlowTaskId(taskVo.getTaskId().doubleValue());
		commonVo.setFlowId(taskVo.getFlowId());
		commonVo.setDamageDate(registVo.getDamageTime());
		deflossVo.setCommonVo(commonVo);
		
		return deflossVo;
	}
	
	
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.lossprop.service.PropLossHandleService#getDefloss(ins.sino.claimcar.lossprop.vo.DeflossActionVo, ins.sino.claimcar.regist.vo.PrpLRegistVo, ins.platform.vo.SysUserVo)
	 */
	@Override
	public void getDefloss(DeflossActionVo deflossVo,PrpLRegistVo registVo,SysUserVo sysUserVo){
		PrpLdlossPropMainVo lossPropMainVo=new PrpLdlossPropMainVo();
		List<PrpLdlossPropFeeVo> lossPropFeeVos=new ArrayList<PrpLdlossPropFeeVo>();
		PrpLdlossPropFeeVo lossPropFeeVo=new PrpLdlossPropFeeVo();
		Long businessId = deflossVo.getBusinessId();
		lossPropMainVo.setRegistNo(registVo.getRegistNo());
		lossPropMainVo.setScheduleTaskId(businessId);
		PrpLScheduleDefLossVo scheduleDefLossVo=scheduleService.findScheduleDefLossByPk(businessId);
		Long addDeflossId=scheduleDefLossVo.getAddDeflossId();//????????????????????? ID
		Long checkLossId=scheduleDefLossVo.getCheckLossId();//?????? PrpLCheckProp.id
		
		if("3".equals(scheduleDefLossVo.getLossitemType())){//3?????????
			lossPropMainVo.setLicense("???????????????");
		}else{
			lossPropMainVo.setLicense(scheduleDefLossVo.getLicenseNo());
		}
		lossPropMainVo.setDefLossDate(new Date());
		lossPropMainVo.setLossType(scheduleDefLossVo.getLossitemType());
		lossPropMainVo.setClaimType("0");//TODO ??????????????????
		lossPropMainVo.setHandlerIdCard(sysUserVo.getIdentifyNumber());
		lossPropMainVo.setComCode(registVo.getComCode());
		lossPropMainVo.setHandlerCode(sysUserVo.getUserCode());
		lossPropMainVo.setHandlerName(sysUserVo.getUserName());
		lossPropMainVo.setMakeCom(deflossVo.getComCode());
		
		if (CodeConstants.ScheduleDefSource.SCHEDULEDEF.equals(scheduleDefLossVo.getSourceFlag())){ // ????????????????????????
			lossPropMainVo.setDeflossSourceFlag(CodeConstants.defLossSourceFlag.SCHEDULDEFLOSS);//????????????
			lossPropFeeVo.setLossItemName(scheduleDefLossVo.getItemsContent());//??????????????????
			lossPropFeeVo.setRegistNo(lossPropMainVo.getRegistNo());
			lossPropMainVo.setSerialNo(scheduleDefLossVo.getSerialNo());
			//lossPropFeeVo.setRiskCode(dlossPropMainVo.getRiskCode());
			lossPropFeeVos.add(lossPropFeeVo);
			lossPropMainVo.setPrpLdlossPropFees(lossPropFeeVos);
		}else if(CodeConstants.ScheduleDefSource.SCHEDULEADD.equals(scheduleDefLossVo.getSourceFlag())){//????????????
			PrpLdlossPropMainVo lossPropMainOldVo = propLossService.findPropMainVoById(addDeflossId);//?????????????????????
			
//			if(lossPropMainOldVo!=null){
//				lossPropMainVo=lossPropMainOldVo;//????????????????????????Vo
//			}
			//TODO ??????????????????????????????????????????lossPropMainOldVo=null ????????????????????????Vo
			lossPropMainVo.setDeflossSourceFlag(CodeConstants.defLossSourceFlag.ADDTIONALDEFLOSS);
			lossPropMainVo.setReLossPropId(lossPropMainOldVo.getId());
			lossPropMainVo.setMercyFlag(lossPropMainOldVo.getMercyFlag());
			lossPropMainVo.setSerialNo(lossPropMainOldVo.getSerialNo());
		}else{//SourceFlag==0 ???????????????
			lossPropMainVo.setDeflossSourceFlag(CodeConstants.defLossSourceFlag.SCHEDULDEFLOSS);
			
			if(scheduleDefLossVo.getLossitemType().equals("3")){//3?????????
				lossPropMainVo.setLicense("???????????????");
			}else{
				lossPropMainVo.setLicense(scheduleDefLossVo.getLicenseNo());
			}
			lossPropMainVo.setLossType(scheduleDefLossVo.getLossitemType());
			//?????????(???A3208K)
			PrpLCheckTaskVo checkTaskVo = checkTaskService.findCheckTaskVoById(checkLossId);
			List<PrpLCheckPropVo> checkPropVos = checkTaskVo.getPrpLCheckProps();
			lossPropMainVo.setSerialNo(scheduleDefLossVo.getSerialNo());
			for(PrpLCheckPropVo checkPropVo : checkPropVos){
				//??????????????????????????????????????????????????????????????? - luwei
				if(checkPropVo.getLossPartyId().toString().equals(scheduleDefLossVo.getSerialNo().toString()) 
						&& RadioValue.RADIO_NO.equals(checkPropVo.getIsNoClaim())){
					PrpLdlossPropFeeVo prpLdlossPropFeeVo = new PrpLdlossPropFeeVo();   
					prpLdlossPropFeeVo.setRegistNo(checkPropVo.getRegistNo()); 
					prpLdlossPropFeeVo.setLossItemName(checkPropVo.getLossItemName());
					prpLdlossPropFeeVo.setRiskCode(lossPropMainVo.getRiskCode());
					prpLdlossPropFeeVo.setLossQuantity(new BigDecimal(checkPropVo.getLossNum()));
					prpLdlossPropFeeVo.setSumDefloss(checkPropVo.getLossFee());
					prpLdlossPropFeeVo.setUnit(checkPropVo.getLossFeeType());
					lossPropFeeVos.add(prpLdlossPropFeeVo); 		
				}
			}	
			lossPropMainVo.setPrpLdlossPropFees(lossPropFeeVos);

		}
		
		DefCommonVo commonVo=new DefCommonVo();
		lossPropMainVo.setRiskCode(registVo.getRiskCode());
//		if(CodeConstants.LossParty.TARGET.equals(scheduleDefLossVo.getLossitemType())){//?????????
//			PrpLCItemKindVo itemKindVo =policyViewService.findItemKindByKindCode(scheduleDefLossVo.getRegistNo(), CodeConstants.KINDCODE.KINDCODE_D2);//??????????????????????????????????????????
//			//?????? ????????????
//			if(itemKindVo!=null){
//				commonVo.setAmount(itemKindVo.getAmount());
//				lossPropMainVo.setHaveKindD("1");
//			}
//		}
		PrpdIntermMainVo intermMainVo = managerService.findIntermByUserCode(sysUserVo.getUserCode());
		if(intermMainVo !=null){//TODO ????????????????????? ?????? ?????? ??????
			lossPropMainVo.setInterMediaryFlag("1");//????????????
			lossPropMainVo.setInterMediaryinfoId(intermMainVo.getIntermCode());//???????????????????????? prpdintermmain.intermCode
		}else{
			lossPropMainVo.setInterMediaryFlag("0");//????????????
		}
		
		deflossVo.setLossPropMainVo(lossPropMainVo);//????????????
		deflossVo.setCommonVo(commonVo);//????????????
		
		PrpLWfTaskVo taskVo = deflossVo.getTaskVo();
		if(FlowNode.DLPropAdd.name().equals(taskVo.getSubNodeCode())){
			//this.saveOrUpdateDefloss(deflossVo);
			this.savePropMain(deflossVo,sysUserVo);
			String propId = deflossVo.getLossPropMainVo().getId().toString();
			
			wfTaskHandleService.tempSaveTask(taskVo.getTaskId().doubleValue(),propId,deflossVo.getUserCode(),deflossVo.getComCode());
		}
		
	}
	
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.lossprop.service.PropLossHandleService#loadChargeTr(java.lang.String[], java.lang.String, java.lang.String)
	 */
	@Override
	public List<PrpLDlossChargeVo> loadChargeTr(String[] chargeTypes,String registNo,String intermCode){
		String kindCode = this.getKindCode(registNo);
		List<PrpLDlossChargeVo> lossChargeVos = new ArrayList<PrpLDlossChargeVo>();
		if(chargeTypes!=null){
			for(String chargeCode : chargeTypes){
				PrpLDlossChargeVo lossChargeVo = new PrpLDlossChargeVo();
				lossChargeVo.setChargeCode(chargeCode);
				lossChargeVo.setRegistNo(registNo);
				lossChargeVo.setBusinessType(FlowNode.DLCar.name());
				lossChargeVo.setChargeName(codeTranService.transCode("ChargeCode",chargeCode));
				lossChargeVo.setKindCode(kindCode);//????????????????????? --> ?????????????????? --> ????????????
				
				if(chargeCode.equals("13")){//?????????
					lossChargeVo.setServiceType("3");//??????????????????
					String comCode = "";
					PrpdIntermMainVo intermMainVo = managerService.findIntermByCode(intermCode,comCode);
					List<PrpdIntermServerVo> interServerList = intermMainVo.getPrpdIntermServers();
					for(PrpdIntermServerVo serverVo : interServerList){
						if("3".equals(serverVo.getServiceType())){
							lossChargeVo.setChargeStandard(new BigDecimal(serverVo.getFeeStandard()));
						}
					}
				}
				lossChargeVos.add(lossChargeVo);
			}
		}
		return lossChargeVos;
	}
	
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.lossprop.service.PropLossHandleService#getPolicyKindMap(java.lang.String)
	 */
	@Override
	public Map<String,String> getPolicyKindMap(String registNo){
		Map<String,String> kindMap = new HashMap<String,String>(); 
		List<PrpLCItemKindVo> itemKinds = policyViewService.findItemKinds(registNo,"Y");
		for(PrpLCItemKindVo itemKind :itemKinds){
			kindMap.put(itemKind.getKindCode(),itemKind.getKindName());
		}
		return kindMap;
	}
	
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.lossprop.service.PropLossHandleService#getKindCode(java.lang.String)
	 */
	@Override
	public String getKindCode(String registNo){
		Map<String,String> kindMap = this.getPolicyKindMap(registNo);
		PrpLCheckVo checkVo = checkTaskService.findCheckVoByRegistNo(registNo);
		String checkDamageCode = "";
		if("DM99".equals(checkVo.getDamageCode())){
			checkDamageCode = checkVo.getDamOtherCode();
		}else{
			checkDamageCode = checkVo.getDamageCode();
		}
		String kindCode = "";
		//????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
		if("DM02".equals(checkDamageCode)){//??????????????????
			if(kindMap.containsKey("F"))
				kindCode ="F";
		}else if("DM03".equals(checkDamageCode)){//????????????
			if(kindMap.containsKey("L"))
				kindCode ="L";
		}else if("DM04".equals(checkDamageCode)){//??????
			if(kindMap.containsKey("G"))
				kindCode ="G";
		}else if("DM05".equals(checkDamageCode)){//????????????
			if(kindMap.containsKey("Z"))
				kindCode ="Z";
		}else if(kindMap.containsKey("A")){
			kindCode ="A";
		}else{
			kindCode ="BZ";
		}
		return kindCode;
	}
	
	// ????????????????????????
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.lossprop.service.PropLossHandleService#getIntermStanders(java.lang.String)
	 */
	@Override
	public Map<String,String> getIntermStanders(String code) {
		Map<String,String> chargeStandard = new HashMap<String,String>();
		String comCode = "";
		PrpdIntermMainVo intermMainVo = managerService.findIntermByCode(code,comCode);
		if(intermMainVo==null){
			throw new IllegalArgumentException("?????????????????????");
		}
		for(PrpdIntermServerVo intermServerVo:intermMainVo.getPrpdIntermServers()){
			chargeStandard.put(intermServerVo.getFeeStandard(),
					intermMainVo.getIntermName()+"--"+intermServerVo.getFeeStandard());
		}
		chargeStandard.put("","");
		return chargeStandard;
	}
	
	//??????????????????Map
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.lossprop.service.PropLossHandleService#getFeeTypeCode()
	 */
	@Override
	public Map<String,String> getFeeTypeCode(){
		Map<String,String> feeMap = new HashMap<String,String>();
//		feeMap.put("","");
		feeMap.put("?????????","?????????");
		feeMap.put("?????????","?????????");
		return feeMap;
	}
	
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.lossprop.service.PropLossHandleService#initSubRisks(java.lang.String, java.lang.String[])
	 */
	@Override
	public List<PrpLCItemKindVo> initSubRisks(String registNo,String[] kindCodes){
		List<PrpLCItemKindVo> itemKindList = new ArrayList<PrpLCItemKindVo>();
		
		List<PrpLCItemKindVo> itemKinds = policyViewService.findItemKinds(registNo,"N");
		List<String> kindCodeList = new ArrayList<String>();
		if(kindCodes.length>0){
			for(String kindCode : kindCodes){
				kindCodeList.add(kindCode);
			}
		}
		kindCodeList.add("--");
		
		for(PrpLCItemKindVo itemKindVo : itemKinds){
			if(!kindCodeList.contains(itemKindVo.getKindCode())){
				itemKindList.add(itemKindVo);
			}
		}
		return itemKindList;
	}
	
	/**
	 * ??????????????????
	 */
	private Map<String,String> getNodePath(String currentNode,PrpLdlossPropMainVo lossPropMainVo ){
		Map<String,String> nextNodeMap = new HashMap<String,String>();
		String auditStatus = lossPropMainVo.getAuditStatus();
		
		if(FlowNode.DLProp.name().equals(currentNode)||FlowNode.DLPropMod.name().equals(currentNode) ||
				FlowNode.DLPropAdd.name().equals(currentNode)){// ?????????????????????
				nextNodeMap.put(FlowNode.VLProp_LV1.name(),FlowNode.VLProp_LV1.getName());
		}else{//??????
			//????????????????????????????????????????????????????????????--18886
//			if(wfTaskHandleService.existTaskByNodeCode(lossPropMainVo.getRegistNo(),FlowNode.ChkBig,null,"0")){
//				throw new IllegalArgumentException("?????????????????????????????????????????????");
//			}
			//TODO  ????????????????????????????????????????????????
			if(CodeConstants.AuditStatus.SUBMITVLOSS.equals(auditStatus)){//????????????
				nextNodeMap.put(FlowNode.END.name(),"????????????");
			}else if(CodeConstants.AuditStatus.AUDIT.equals(auditStatus)){//????????????
				//?????????????????? cu
				List<PrpDNodeVo> nodeList = wfTaskHandleService.findNode(FlowNode.VLProp.name(),currentNode,auditStatus);
				for(PrpDNodeVo nodeVo : nodeList ){
					nextNodeMap.put(nodeVo.getNodeCode(),nodeVo.getNodeName());
				}
				
			}else if(CodeConstants.AuditStatus.BACKLOSS.equals(auditStatus)){//????????????
				if(lossPropMainVo.getDeflossSourceFlag().equals("2")){//????????????
					nextNodeMap.put(FlowNode.DLPropAdd.name(),FlowNode.DLPropAdd.getName());
				}else if(lossPropMainVo.getDeflossSourceFlag().equals("3")){//????????????
					nextNodeMap.put(FlowNode.DLPropMod.name(),FlowNode.DLPropMod.getName());
				}else{
					nextNodeMap.put(FlowNode.DLProp.name(),FlowNode.DLProp.getName());
				}
					
			}else if(CodeConstants.AuditStatus.BACKLOWER.equals(auditStatus)){//????????????
				List<PrpDNodeVo> nodeList = wfTaskHandleService.findNode(FlowNode.VLProp.name(),currentNode,auditStatus);
				for(PrpDNodeVo nodeVo : nodeList ){
					nextNodeMap.put(nodeVo.getNodeCode(),nodeVo.getNodeName());
				}
				
			}
		}
		return nextNodeMap;
	}

	
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.lossprop.service.PropLossHandleService#organizeNextVo(java.lang.Long, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public SubmitNextVo organizeNextVo(Long lossMainId,String flowTaskId,String currentNode,String saveType,SysUserVo userVo,String isSubmitHeadOffice) {
	    boolean autoVeriFlag = false;  //????????????
		SubmitNextVo nextVo = new SubmitNextVo();
		PrpLWfTaskVo taskVo = wfTaskHandleService.queryTask(Double.parseDouble(flowTaskId));
		if(CodeConstants.HandlerStatus.END.equals(taskVo.getHandlerStatus())){
			throw new IllegalArgumentException("?????????????????????????????????????????????");
		}

		PrpLdlossPropMainVo lossPropMainVo = propLossService.findPropMainVoById(lossMainId);
		Map<String,String> nextNodeMap = new TreeMap<String,String>();

		// ??????ruleVo
		LossPropToVerifyRuleVo ruleVo = new LossPropToVerifyRuleVo();
		Double sumFee = lossPropMainVo.getSumDefloss().add(lossPropMainVo.getSumLossFee()).add(lossPropMainVo.getDefRescueFee()).doubleValue();
		ruleVo.setSumLossFee(sumFee);
		// ????????????????????????????????????????????????
		if(FlowNode.DLoss.name().equals(taskVo.getSubNodeCode())&&"3".equals(taskVo.getSubCheckFlag())){
			ruleVo.setComCode(lossPropMainVo.getComCode());
			nextVo.setComCode(lossPropMainVo.getComCode());
		}else{
			ruleVo.setComCode(userVo.getComCode());
			nextVo.setComCode(userVo.getComCode());
		}
		int maxLevel = 4;
		LIlogRuleResVo vPriceResVo = null;
		int backLevel = 0;
		PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.ILOGVALIDFLAG,userVo.getComCode());
		if("1".equals(configValueVo.getConfigValue())){
			try{
				vPriceResVo = deflossHandleIlogService.organizaVProperty(lossPropMainVo,"1",userVo,taskVo.getTaskId(),taskVo.getSubNodeCode(),isSubmitHeadOffice);
				if(vPriceResVo!=null&&"1".equals(vPriceResVo.getState())){
					
					String finalPowerFlag =  SpringProperties.getProperty("FINALPOWERFLAG");
		        	boolean finalAutoPass = true;
		        	if ("1".equals(finalPowerFlag)) {
		        		/*	???????????????????????? start  */
		        		IlogFinalPowerInfoVo powerInfoVo = ilogRuleService.findByUserCode(userVo.getUserCode());
						if (powerInfoVo != null) {
							BigDecimal gradePower = powerInfoVo.getGradeAmount();
							if (gradePower != null) {
								// ???????????????
								BigDecimal sumDefLoss = new BigDecimal("0");
								if (lossPropMainVo.getSumDefloss() != null) {
									sumDefLoss = lossPropMainVo.getSumDefloss();
								}
								// ????????????
								if (CodeConstants.defLossSourceFlag.ADDTIONALDEFLOSS.equals(lossPropMainVo.getDeflossSourceFlag())) {
									List<PrpLdlossPropMainVo> prpLdlossPropMainVos = propLossService.findLossPropBySerialNo(lossPropMainVo.getRegistNo(), lossPropMainVo.getSerialNo());
									for (PrpLdlossPropMainVo prpLdlossPropMainVo : prpLdlossPropMainVos) {
										if (UnderWriteFlag.MANAL_UNDERWRITE.equals(prpLdlossPropMainVo.getUnderWriteFlag())) {
											sumDefLoss = sumDefLoss
													.add(DataUtils.NullToZero(prpLdlossPropMainVo.getSumDefloss()))
													.add(DataUtils.NullToZero(prpLdlossPropMainVo.getDefRescueFee()))//????????????
													.add(DataUtils.NullToZero(prpLdlossPropMainVo.getSumLossFee()));//??????
										}
									}
								}

								sumDefLoss = sumDefLoss
										.add(DataUtils.NullToZero(lossPropMainVo.getDefRescueFee()))//????????????
										.add(DataUtils.NullToZero(lossPropMainVo.getSumLossFee()));//??????

								if (sumDefLoss.compareTo(gradePower) == 1) {
									finalAutoPass = false;
								}
							}
						} else {
							finalAutoPass = false;
						}
		        		/*	????????????????????????   end   */
		        	}
		            
					if(("1".equals(vPriceResVo.getUnderwriterflag())||"4".equals(vPriceResVo.getUnderwriterflag())) && finalAutoPass){
						// ????????????
					    autoVeriFlag = true;
					}else{
						try{
							vPriceResVo = deflossHandleIlogService.organizaVProperty(lossPropMainVo,"2",userVo,taskVo.getTaskId(),taskVo.getSubNodeCode(),isSubmitHeadOffice);
							if(vPriceResVo!=null&&"1".equals(vPriceResVo.getState())){
								backLevel = Integer.parseInt(vPriceResVo.getMinUndwrtNode());
								maxLevel = Integer.parseInt(vPriceResVo.getMaxUndwrtNode());
							}else{
								throw new IllegalArgumentException("ILOG??????????????????");
							}
						}
						catch(Exception e){
							e.printStackTrace();
							throw new IllegalArgumentException("ILOG??????????????????:"+e.getMessage()+"");
						}
					}
				}else{
					throw new IllegalArgumentException("ILOG??????????????????");
				}
			}
			catch(Exception e){
				e.printStackTrace();
				throw new IllegalArgumentException("ILOG??????????????????:"+e.getMessage()+"");
			}
		}
		
		PrpLConfigValueVo configRuleValueVo = ConfigUtil.findConfigByCode(CodeConstants.RULEVALIDFLAG,lossPropMainVo.getComCode());
        if("1".equals(configRuleValueVo.getConfigValue())){
//			if("audit".equals(saveType)){
//				ruleVo = claimRuleApiService.lossPropToVerifyRule(ruleVo);
//				backLevel = ruleVo.getBackLevel();
//				maxLevel = ruleVo.getMaxLevel();
//			}
			ruleVo = claimRuleApiService.lossPropToVerifyRule(ruleVo);
			backLevel = ruleVo.getBackLevel();
			maxLevel = ruleVo.getMaxLevel();
			if(backLevel>8){
				ruleVo.setTopComp("1");
			}
		}

		if("audit".equals(saveType)){// ????????????
			StringBuffer nodeLevel = new StringBuffer(currentNode);
			nodeLevel.delete(0,9);
			int level = Integer.valueOf(nodeLevel.toString());
			if(level<9){
				String nodeCode = "";
				if(level==maxLevel){// ???????????????????????????????????????
					level = 9;
					nodeCode = "VLProp_LV"+level;
					nextNodeMap.put(nodeCode,FlowNode.valueOf(nodeCode).getName());
					nextVo.setSubmitLevel(backLevel);
					nextVo.setMaxLevel(maxLevel);

					// nextVo.setSubmitLevel(ruleVo.getBackLevel());
					// nextVo.setMaxLevel(ruleVo.getMaxLevel());
				}else if(level<maxLevel){
					boolean haveUser = false;
					for(int i = level+1; i<=maxLevel; i++ ){
						nodeCode = "VLProp_LV"+i;
						haveUser = assignService.existsGradeUser(FlowNode.valueOf(nodeCode),userVo.getComCode());
						if(haveUser){
							nextNodeMap.put(nodeCode,FlowNode.valueOf(nodeCode).getName());
						}
					}
				}
			}else{
				level = level+1;
				String nodeCode = "VLProp_LV"+level;
				nextNodeMap.put(nodeCode,FlowNode.valueOf(nodeCode).getName());
			}
		}else if("backLower".equals(saveType)){// ????????????
			StringBuffer nodeLevel = new StringBuffer(currentNode);
			nodeLevel.delete(0,9);
			int level = Integer.valueOf(nodeLevel.toString());
			if(level>9){
				level = level-1;
				String nodeCode = "VLProp_LV"+level;
				nextNodeMap.put(nodeCode,FlowNode.valueOf(nodeCode).getName());
			}else if(level==9){// ???????????????????????????????????????
				String upperNode = FlowNode.VLProp.name();
				List<PrpDNodeVo> nodeList = wfTaskHandleService.findLowerNode(taskVo.getUpperTaskId().doubleValue(),taskVo.getSubNodeCode(),upperNode);
				SysCodeDictVo dictVo1 = new SysCodeDictVo();
				dictVo1.setCodeCode(nodeList.get(0).getNodeCode());
				dictVo1.setCodeName(nodeList.get(0).getNodeName());
				if(nodeList.size()>1){
					for(int i = 1; i<nodeList.size(); i++ ){
						if(nodeList.get(i-1).getNodeCode().startsWith("VLProp_LV")){
							int Level_1 = Integer.parseInt(dictVo1.getCodeCode().split("_LV")[1]);
							int Level_2 = Integer.parseInt(nodeList.get(i).getNodeCode().split("_LV")[1]);
							if(Level_1<Level_2){
								dictVo1.setCodeCode(nodeList.get(i).getNodeCode());
								dictVo1.setCodeName(nodeList.get(i).getNodeName());
							}
						}
					}
				}
				nextNodeMap.put(dictVo1.getCodeCode(),dictVo1.getCodeName());
			}else{
				String upperNode = FlowNode.VLProp.name();
				List<PrpDNodeVo> nodeList = wfTaskHandleService.findLowerNode(taskVo.getUpperTaskId().doubleValue(),taskVo.getSubNodeCode(),upperNode);
				for(PrpDNodeVo nodeVo:nodeList){
					nextNodeMap.put(nodeVo.getNodeCode(),nodeVo.getNodeName());
				}
			};
		}else if("backLoss".equals(saveType)||"submitVloss".equals(saveType)){
			// ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
			if ("submitVloss".equals(saveType) && userVo.getComCode() != null
					&& !(userVo.getComCode().startsWith(CodeConstants.comCodeBegin.headOfficeBegin1)
						|| userVo.getComCode().startsWith(CodeConstants.comCodeBegin.headOfficeBegin2))
					&& CodeConstants.CommonConst.TRUE.equals(isSubmitHeadOffice)) {
				throw new IllegalArgumentException("???????????????????????????????????????????????????????????????");
			}
			nextNodeMap = this.getNodePath(currentNode,lossPropMainVo);
		}else{// ?????????????????????
            Boolean isSimpleClaim = this.isSimpleClaim(lossPropMainVo,taskVo,userVo);
            Boolean notModPrice = this.notModPrice(lossPropMainVo);
            nextVo.setNotModPrice(notModPrice);
            if("1".equals(configValueVo.getConfigValue())&&autoVeriFlag&&"0".equals(configRuleValueVo.getConfigValue())){
                //1.ILOG????????????
                nextNodeMap.put("VLProp_LV0",FlowNode.valueOf("VLProp_LV0").getName());
                nextVo.setAutoLossFlag("1");
            }else if("1".equals(configRuleValueVo.getConfigValue())&&(sumFee==0||isSimpleClaim||notModPrice)){
                /*
                 * ???????????????????????????????????? 1.???????????????0??? 2.??????????????????????????? 3.??????????????????????????????????????????????????????????????????
                 */
                nextNodeMap.put("VLProp_LV0",FlowNode.valueOf("VLProp_LV0").getName());
                nextVo.setAutoLossFlag("1");
            }else{
                int level = backLevel;
                if(level>maxLevel){// ??????????????????????????????
                    level = maxLevel;
                }else{
                    boolean haveUser = false;
                    while(!haveUser&&level<maxLevel){// ????????????????????????????????????????????????
                        haveUser = assignService.existsGradeUser(FlowNode.valueOf("VLProp_LV"+level),nextVo.getComCode());
                        if( !haveUser){
                            level++ ;
                        }
                    }
                }
                String nodeCode = "VLProp_LV"+level;
                nextNodeMap.put(nodeCode,FlowNode.valueOf(nodeCode).getName());
                nextVo.setSubmitLevel(backLevel);
                nextVo.setMaxLevel(maxLevel);
            }	
		}

		nextVo.setNodeMap(nextNodeMap);

		String currentName = FlowNode.valueOf(currentNode).getName();
		nextVo.setCurrentName(currentName);
		nextVo.setCurrentNode(currentNode);
		nextVo.setTaskInKey(lossMainId.toString());
		nextVo.setRegistNo(lossPropMainVo.getRegistNo());
		nextVo.setFlowTaskId(flowTaskId);
		nextVo.setAuditStatus(lossPropMainVo.getAuditStatus());
		nextVo.setNodeMap(nextNodeMap);

		return nextVo;
	}
	
	/**
	 * ??????????????????????????????
	 * @return
	 */
	public Boolean isSimpleClaim(PrpLdlossPropMainVo lossPropMainVo,PrpLWfTaskVo taskVo,SysUserVo userVo){
		//?????????????????????
		if(taskVo.getTaskInNode().startsWith("VLProp_LV")){
			return false;
		}
		//??????????????????????????????????????????????????????????????????
		if(CodeConstants.defLossSourceFlag.MODIFYDEFLOSS.equals(lossPropMainVo.getDeflossSourceFlag())){
			PrpLWfTaskVo uppertaskVo = wfTaskHandleService.queryTask(taskVo.getUpperTaskId().doubleValue());
			if(!FlowNode.VLProp_LV0.name().equals(uppertaskVo.getSubNodeCode())){
				return false;
			}
		}
		BigDecimal sumFee = new BigDecimal("0");
		sumFee = sumFee.add(DataUtils.NullToZero(lossPropMainVo.getSumDefloss()));
		//????????????????????????
		if(CodeConstants.defLossSourceFlag.ADDTIONALDEFLOSS.equals(lossPropMainVo.getDeflossSourceFlag())){
			PrpLdlossPropMainVo upperLossVo = propLossService.findPropMainVoById(lossPropMainVo.getReLossPropId());
			if(upperLossVo != null){
				sumFee = sumFee.add(DataUtils.NullToZero(upperLossVo.getSumDefloss()));
			}
		}
		
		//?????????????????????????????????????????????????????????????????????????????????
		PrpLAutoVerifyVo prpLAutoVerifyVo = new PrpLAutoVerifyVo();
		prpLAutoVerifyVo.setUserCode(userVo.getUserCode());
		prpLAutoVerifyVo.setNode(taskVo.getSubNodeCode());
		prpLAutoVerifyVo.setMoney(sumFee);
		Boolean isAutoVerifyUser = deflossHandleService.isAutoVerifyUser(prpLAutoVerifyVo);
		if(!isAutoVerifyUser){
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see ins.sino.claimcar.lossprop.service.PropLossHandleService#getPayCustomMap(java.lang.String)
	 */
	@Override
	public Map<String,String> getPayCustomMap(String registNo){
		Map<String,String> map = new HashMap<String,String>();
		List<PrpLPayCustomVo> payCustomVos = managerService.findPayCustomVoByRegistNo(registNo);
		if(payCustomVos != null && payCustomVos.size() > 0){
			for(PrpLPayCustomVo payCustomVo : payCustomVos){
				String bankNo = payCustomVo.getAccountNo();
				String idNo;
				if(bankNo.length()<4){
					idNo = bankNo;
				}else{
					idNo = bankNo.substring(bankNo.length()-4,bankNo.length());
				}
				String value = payCustomVo.getPayeeName() + "--" + idNo;
				map.put(payCustomVo.getId().toString(),value);
			}
		}
		map.put("0","");
		return map;
	}
	
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.lossprop.service.PropLossHandleService#getReplacePayMap(java.lang.Long)
	 */
	@Override
	public Map<String,String> getReplacePayMap(Long id){
		PrpLPayCustomVo payCustomVo = managerService.findPayCustomVoById(id);
		return this.getPayCustomMap(payCustomVo.getRegistNo());
	}
	
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.lossprop.service.PropLossHandleService#findPersTraceList(java.lang.String)
	 */
	@Override
	public List<PrpLDlossPersTraceVo> findPersTraceList(String registNo){
		List<PrpLDlossPersTraceVo> traceList = new ArrayList<PrpLDlossPersTraceVo>();
		
		List<PrpLDlossPersTraceMainVo> lossPersMainList = persTraceDubboService.findPersTraceMainVoList(registNo);
		//??????????????????
		if(lossPersMainList!=null && lossPersMainList.size()>0){
			for(PrpLDlossPersTraceMainVo traceMainVo : lossPersMainList){
				List<PrpLDlossPersTraceVo> traces = traceMainVo.getPrpLDlossPersTraces();
				if(traces!=null && traces.size()>0){
					for(PrpLDlossPersTraceVo traceVo: traces){
						PrpLDlossPersInjuredVo persInjured = traceVo.getPrpLDlossPersInjured();
						List<PrpLDlossPersHospitalVo> hostitalList = persInjured.getPrpLDlossPersHospitals();
						java.util.Collections.sort(hostitalList, new Comparator<PrpLDlossPersHospitalVo>() {
							@Override
							public int compare(PrpLDlossPersHospitalVo o1, PrpLDlossPersHospitalVo o2) {
								return o1.getInHospitalDate().compareTo(o2.getInHospitalDate());
							}
						});
						if(hostitalList!=null && hostitalList.size()>0){
							persInjured.setHospitalCode(hostitalList.get(0).getHospitalCode());
							persInjured.setHospitalCity(hostitalList.get(0).getHospitalCity());
						}
						//?????????????????????
						int traceTimes = persTraceDubboService.getTraceTimes(registNo, persInjured.getId());
						traceVo.setRemark(String.valueOf(traceTimes));
						traceList.add(traceVo);
					}
				}	
			}
		}	
		
		return traceList;
	}


	/* (non-Javadoc)
	 * @see ins.sino.claimcar.lossprop.service.PropLossHandleService#deflossView(ins.sino.claimcar.lossprop.vo.DeflossActionVo, java.lang.String)
	 */
	@Override
	public DeflossActionVo deflossView(String lossId) {

		DeflossActionVo deflossVo = new DeflossActionVo();
		DefCommonVo commonVo = new DefCommonVo();
		PrpLdlossPropMainVo lossPropMainVo = propLossService.findPropMainVoById(Long.parseLong(lossId));
		PrpLRegistVo registVo = registQueryService.findByRegistNo(lossPropMainVo.getRegistNo());
		List<PrpLClaimTextVo> claimTextVos = claimTextService.findClaimTextList(lossPropMainVo.getId(),lossPropMainVo.getRegistNo(),FlowNode.DLProp.name());
		PrpLClaimTextVo claimTextVo = claimTextService.findClaimTextByNode(lossPropMainVo.getId(),FlowNode.DLProp.name(),"1"); 
		List<PrpLDlossChargeVo> lossChargeVos = lossChargeService.findLossChargeVos(lossPropMainVo.getId(),FlowNode.DLProp.name());
	
		deflossVo.setLossPropMainVo(lossPropMainVo);
		deflossVo.setClaimTextVo(claimTextVo);
		deflossVo.setClaimTextVos(claimTextVos);
		deflossVo.setClaimTextVo(claimTextVo);
		deflossVo.setLossChargeVos(lossChargeVos);	
		
		//???????????????????????????
		Map<String,String> kindMap=new HashMap<String, String>();
		//??????????????????????????????????????????
		List<PrpLCItemKindVo> itemKindVos=policyViewService.findItemKinds(lossPropMainVo.getRegistNo(), "Y");
		for(PrpLCItemKindVo itemKindVo:itemKindVos){
			kindMap.put(itemKindVo.getKindCode(), itemKindVo.getKindName());
		}
		
		//????????????????????????
		if("1".equals(lossPropMainVo.getInterMediaryFlag())){
			//????????????????????????
			String comCode = "";
			PrpdIntermMainVo intermMainVo=managerService.findIntermByCode(lossPropMainVo.getInterMediaryinfoId(),comCode);
			if(intermMainVo==null){
				throw new IllegalArgumentException("????????????????????????");
			}
			List<PrpdIntermServerVo> intermServiceList=intermMainVo.getPrpdIntermServers();//??????????????????
			StringBuffer intermStanders=new StringBuffer();
			Map<String,String> intermMap=new HashMap<String, String>();
			for(PrpdIntermServerVo serverVo:intermServiceList){
				//?????????????????????????????????
				intermStanders.append(serverVo.getServiceType()+"-"+serverVo.getFeeStandard()+",");

				//serviceType-????????????????????????,feeStandard-????????????
			}
			intermMap.put("2", "????????????");
			intermMap.put("3", "????????????");
			deflossVo.setIntermMap(intermMap);
			commonVo.setIntermStanders(intermStanders.toString());
		}
		
		//????????????
		List<PrpLDlossCarMainVo> lossCarMainList = lossCarService.findLossCarMainByRegistNo(registVo.getRegistNo());
		List<PrpLdlossPropFeeVo> dlossPropFeeVos = propLossService.findPropVoListByRegistNo(registVo.getRegistNo());
		List<PrpLDlossPersTraceVo> traceList = this.findPersTraceList(registVo.getRegistNo());
		deflossVo.setLossCarMainList(lossCarMainList);
		deflossVo.setLossPropFeeVos(dlossPropFeeVos);
		deflossVo.setLossPersTraceList(traceList);
		
		deflossVo.setKindMap(kindMap);
		commonVo.setHandleStatus("3");
//		lossPropMainVo.setCurrentNode(taskVo.getSubNodeCode());
//		commonVo.setFlowTaskId(taskVo.getTaskId().doubleValue());
//		commonVo.setFlowId(taskVo.getFlowId());
		commonVo.setDamageDate(registVo.getDamageTime());
		deflossVo.setCommonVo(commonVo);
		

		return deflossVo;
		
	}

	/* (non-Javadoc)
	 * @see ins.sino.claimcar.lossprop.service.PropLossHandleService#validDefloss(ins.sino.claimcar.lossprop.vo.DeflossActionVo)
	 */
	@Override
	public String validDefloss(DeflossActionVo deflossVo) {
		String retStr ="";
		PrpLdlossPropMainVo propMainVo=deflossVo.getLossPropMainVo();
		List<PrpLCheckDutyVo> checkDutyList = checkTaskService.findCheckDutyByRegistNo(propMainVo.getRegistNo());
		PrpLCheckDutyVo checkDutyVo =null;
		if(checkDutyList!=null && checkDutyList.size()>0){
			for(PrpLCheckDutyVo checkDuty : checkDutyList){
				if(checkDuty.getSerialNo()==1){
					checkDutyVo = checkDuty;
					break;
				}
			}
		}
		
		if(checkDutyVo!=null && "1".equals(checkDutyVo.getIsClaimSelf())){
			BigDecimal thisCarSumPaid = BigDecimal.ZERO;
			//???????????????
			thisCarSumPaid = thisCarSumPaid.add(DataUtils.NullToZero(propMainVo.getSumDefloss()))
					.add(DataUtils.NullToZero(propMainVo.getDefRescueFee()));
			List<PrpLDlossCarMainVo> lossCarMainList = lossCarService.findLossCarMainByRegistNo(propMainVo.getRegistNo());
			if(lossCarMainList!=null && !lossCarMainList.isEmpty()){
				for(PrpLDlossCarMainVo lossCar : lossCarMainList){
					if(lossCar.getSerialNo().equals(propMainVo.getSerialNo())){
						thisCarSumPaid = thisCarSumPaid.add(DataUtils.NullToZero(lossCar.getSumLossFee()))
								.add(DataUtils.NullToZero(lossCar.getSumRescueFee()));
					}
				}
			}
			
			List<PrpLdlossPropMainVo> lossPropMainList = propTaskService.findPropMainListByRegistNo(propMainVo.getRegistNo());
			if(lossPropMainList!=null && !lossPropMainList.isEmpty()){
				for(PrpLdlossPropMainVo otherPropMainVo : lossPropMainList){
					if(otherPropMainVo.getId().equals(propMainVo.getId())){
						continue;
					}
					
					if(otherPropMainVo.getSerialNo().equals(propMainVo.getSerialNo())){
						thisCarSumPaid = thisCarSumPaid.add(DataUtils.NullToZero(propMainVo.getSumDefloss()))
								.add(DataUtils.NullToZero(propMainVo.getDefRescueFee()));
					}
				}
			}
			
			if(thisCarSumPaid.compareTo(new BigDecimal("2000"))==1){
				return "????????????????????????????????????2000?????????????????????????????????";
			}
			
			if(propMainVo.getSerialNo()==0){
				BigDecimal otherLoss = propMainVo.getSumDefloss().add(DataUtils.NullToZero(propMainVo.getDefRescueFee()))
									.add(DataUtils.NullToZero(propMainVo.getSumLossFee()));
				if(otherLoss.compareTo(BigDecimal.ZERO)==1){
					return "???????????????????????????????????????????????????";
				}
			}
		}else{
			if("0".equals(propMainVo.getHaveKindD()) && propMainVo.getSerialNo()==1){//?????????  ??????????????????,????????????????????? ??????0??????
				BigDecimal thisCarSumPaid = DataUtils.NullToZero(propMainVo.getSumDefloss())
						.add(DataUtils.NullToZero(propMainVo.getDefRescueFee()))
						.add(DataUtils.NullToZero(propMainVo.getSumLossFee()));
					
				if(thisCarSumPaid.compareTo(BigDecimal.ZERO)==1){
					return "????????????????????????????????????????????????????????????????????????";
				}
			}
			
		}
		
		return retStr;
	}
	
	/**
	 * ??????????????????????????????????????????????????????true
	 * @param lossPropMainVo
	 * @return
	 */
	public Boolean notModPrice(PrpLdlossPropMainVo lossPropMainVo){
		//??????????????????
		if(!"3".equals(lossPropMainVo.getDeflossSourceFlag())){
			return false;
		}
		PrpLdlossPropMainHisVo propMainHisVo = propLossService.findPropHisByPropMainId(lossPropMainVo.getId());
		if(propMainHisVo == null){
			return false;
		}
		//??????????????????????????????????????????false
		if(NullToZero(propMainHisVo.getSumDefLoss()).compareTo(NullToZero(propMainHisVo.getSumVeriLoss()))!=0||
				NullToZero(propMainHisVo.getSumLossFee()).compareTo(NullToZero(propMainHisVo.getSumVeriFee()))!=0||
				NullToZero(propMainHisVo.getDefRescueFee()).compareTo(NullToZero(propMainHisVo.getVerirescueFee()))!=0){
			return false;
		}
			
		if(lossPropMainVo.getPrpLdlossPropFees()==null){
			return false;
		}
		BigDecimal sumDefloss = NullToZero(lossPropMainVo.getSumDefloss());
		BigDecimal sumDeflossHis = NullToZero(propMainHisVo.getSumDefLoss());
		if(sumDefloss.compareTo(sumDeflossHis)!=0 || 
				lossPropMainVo.getPrpLdlossPropFees().size()!=propMainHisVo.getPrpLdlossPropFeeHises().size()){
			return false;
		}
		if(propMainHisVo.getPrpLdlossPropFeeHises()!=null && propMainHisVo.getPrpLdlossPropFeeHises().size()>0){
			for(PrpLdlossPropFeeHisVo propFeeHisVo:propMainHisVo.getPrpLdlossPropFeeHises()){
				PrpLdlossPropFeeVo propFeeVo = propLossService.findLossPropFeeVoById(propFeeHisVo.getPropFeeId());
				if(propFeeVo == null){
					return false;
				}
				if(propFeeVo.getLossQuantity().compareTo(propFeeHisVo.getLossQuantity())!=0 ||
						propFeeVo.getUnitPrice().compareTo(propFeeHisVo.getUnitPrice())!=0 ||
						propFeeVo.getRecyclePrice().compareTo(propFeeHisVo.getRecyclePrice())!=0){
					return false;
				}
			}
		}
		//??????????????????????????????????????????
		BigDecimal sumLossFee = NullToZero(lossPropMainVo.getSumLossFee());
		BigDecimal sumLossFeeHis = NullToZero(propMainHisVo.getSumLossFee());
		if(sumLossFee.compareTo(sumLossFeeHis)!=0){
			return false;
		}
		List<PrpLDlossChargeVo> lossChargeVoList = lossChargeService.findLossChargeVos(lossPropMainVo.getId(), FlowNode.DLProp.name());
		if(lossChargeVoList != null && lossChargeVoList.size() > 0 ){
			for(PrpLDlossChargeVo lossChargeVo:lossChargeVoList){
				PrpLDlossChargeHisVo lossChargeHisVo = lossChargeService.findLossChargeHisVo(lossChargeVo.getId(), lossChargeVo.getBusinessId());
				if(lossChargeHisVo==null){
					return false;
				}
				if(NullToZero(lossChargeVo.getChargeFee()).compareTo(NullToZero(lossChargeHisVo.getChargeFee()))!=0){
					return false;
				}
				if(!lossChargeVo.getKindCode().equals(lossChargeHisVo.getKindCode())){
					return false;
				}
			}
		}
		//????????????????????????false
		if(NullToZero(lossPropMainVo.getDefRescueFee()).compareTo(NullToZero(propMainHisVo.getDefRescueFee()))!=0){
			return false;
		}
		
		return true;
	}
	
	@Override
	public void cancelProp(String id){
		logger.info("??????id={},???????????????????????????????????????UnderWriteFlag?????????.",id);
		PrpLdlossPropMainVo propMainVo = propLossService.findPropMainVoById(Long.valueOf(id));
		logger.info("??????id={}?????????????????????????????????UnderWriteFlag={}",id,CodeConstants.VeriFlag.CANCEL);
		propMainVo.setUnderWriteFlag(CodeConstants.VeriFlag.CANCEL);
		propMainVo.setValidFlag("0");
		propLossService.updatePropMain(propMainVo);
		logger.info("??????id={},???????????????????????????????????????UnderWriteFlag?????????",id);
	}
	
	private static BigDecimal NullToZero(BigDecimal strNum) {
		if(strNum==null){
			return new BigDecimal("0");
		}
		return strNum;
	}


	@Override
	public void saveAcheckFeeTask(PrpLdlossPropMainVo lossPropMainVo,SysUserVo userVo) {
		PrpLRegistVo prpLRegistVo=registQueryService.findByRegistNo(lossPropMainVo.getRegistNo());
		PrpdCheckBankMainVo prpdCheckBankMainVo=managerService.findCheckByUserCode(userVo.getUserCode());
		if(prpdCheckBankMainVo !=null){
			PrpLAcheckVo prpLAcheckOldVo=acheckService.findAcheckByLossId(lossPropMainVo.getRegistNo(),  CodeConstants.CheckTaskType.PROP, lossPropMainVo.getSerialNo(), prpdCheckBankMainVo.getCheckCode());
			if(prpLAcheckOldVo == null){
				PrpLAcheckVo acheckVo = new PrpLAcheckVo();
				acheckVo.setCheckmid(prpdCheckBankMainVo.getId());
				acheckVo.setCheckmnamedetail(prpdCheckBankMainVo.getCheckName());
				acheckVo.setRegistNo(lossPropMainVo.getRegistNo());
				acheckVo.setBussiId(lossPropMainVo.getId());
				acheckVo.setTaskType(CodeConstants.CheckTaskType.PROP);
				acheckVo.setUnderWriteFlag(CodeConstants.AcheckUnderWriteFlag.Loss);
				acheckVo.setSerialNo(lossPropMainVo.getSerialNo().toString());
				acheckVo.setLossDate(lossPropMainVo.getDefLossDate());
				acheckVo.setCheckcode(prpdCheckBankMainVo.getCheckCode());
				acheckVo.setCheckname(codeTranService.transCode("CheckPayCode",prpdCheckBankMainVo.getCheckCode()));
				acheckVo.setCreateTime(new Date());
				acheckVo.setCreateUser(userVo.getUserCode());
				acheckVo.setKindCode(checkHandleService.getCarKindCode(lossPropMainVo.getRegistNo()));
				acheckVo.setComCode(prpLRegistVo.getComCode());
				acheckVo.setVeriLoss(lossPropMainVo.getSumVeriLoss());
				acheckService.saveOrUpdatePrpLAcheck(acheckVo,userVo);
				lossPropMainVo.setCheckCode(prpdCheckBankMainVo.getCheckCode());
				propLossService.updatePropMain(lossPropMainVo);
			}
		}
			
	}
}
