/******************************************************************************
* CREATETIME : 2016年1月8日 下午6:06:16
******************************************************************************/
package ins.sino.claimcar.flow.service.spring;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.certify.vo.PrpLCertifyMainVo;
import ins.sino.claimcar.check.vo.PrpLCheckVo;
import ins.sino.claimcar.claim.vo.PrpLClaimCancelVo;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claim.vo.PrpLPadPayMainVo;
import ins.sino.claimcar.claim.vo.PrplReplevyMainVo;
import ins.sino.claimcar.endcase.vo.PrpLEndCaseVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.constant.SubmitType;
import ins.sino.claimcar.flow.po.PrpLWfTaskIn;
import ins.sino.claimcar.flow.po.PrpLWfTaskOut;
import ins.sino.claimcar.flow.service.NodeService;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.service.WfTaskService;
import ins.sino.claimcar.flow.service.handle.WfAcheckService;
import ins.sino.claimcar.flow.service.handle.WfAssessorService;
import ins.sino.claimcar.flow.service.handle.WfCertifyService;
import ins.sino.claimcar.flow.service.handle.WfCheckService;
import ins.sino.claimcar.flow.service.handle.WfClaimService;
import ins.sino.claimcar.flow.service.handle.WfCompeService;
import ins.sino.claimcar.flow.service.handle.WfEndCaseService;
import ins.sino.claimcar.flow.service.handle.WfHandoverTaskService;
import ins.sino.claimcar.flow.service.handle.WfLossService;
import ins.sino.claimcar.flow.service.handle.WfRegistService;
import ins.sino.claimcar.flow.service.handle.WfScheduleService;
import ins.sino.claimcar.flow.service.handle.WfSimpleTaskService;
import ins.sino.claimcar.flow.service.handle.WfVClaimService;
import ins.sino.claimcar.flow.vo.PrpDNodeVo;
import ins.sino.claimcar.flow.vo.PrpLWfTaskUserInfoVo;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.flow.vo.WfSimpleTaskVo;
import ins.sino.claimcar.flow.vo.WfTaskSubmitVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceMainVo;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropMainVo;
import ins.sino.claimcar.other.vo.PrpLAcheckMainVo;
import ins.sino.claimcar.other.vo.PrpLAssessorMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.schedule.vo.PrpLScheduleTaskVo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Path;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;


/**
 * <pre></pre>
 * @author ★LiuPing
 * @CreateTime 2016年1月8日
 */
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path("wfTaskHandleService")
public class WfTaskHandleServiceSpringImpl implements WfTaskHandleService {
	private Logger logger = LoggerFactory.getLogger(WfTaskHandleServiceSpringImpl.class);

	private static Logger log = LoggerFactory.getLogger(WfTaskHandleServiceSpringImpl.class);

	@Autowired
	private WfTaskService wfTaskService;
	@Autowired
	private WfRegistService wfRegistService;
	@Autowired
	private WfScheduleService wfScheduleService;
	@Autowired
	private WfCheckService wfCheckService;
	@Autowired
	private WfLossService wfLossService;
	@Autowired 
	private NodeService nodeService;
	@Autowired
	private WfSimpleTaskService wfSimpleTaskService;
	@Autowired
	private WfHandoverTaskService wfHandoverTaskService;
	@Autowired
	private WfClaimService wfClaimService;
	@Autowired
	private WfCertifyService wfCertifyService;
	@Autowired
	private WfCompeService wfCompeService;
	@Autowired
	private WfVClaimService wfVClaimService;
	@Autowired
	private WfEndCaseService wfEndCaseService;
	@Autowired
	private WfAssessorService wfAssessorService;
	@Autowired
	private WfAcheckService wfAcheckService;
	
	@Autowired
	private DatabaseDao databaseDao;
	

	@Override
	public PrpLWfTaskVo queryTask(Double flowTaskId) {
		return wfTaskService.queryTask(flowTaskId);
	}
	
	@Override
	public PrpLWfTaskVo findTaskIn(Double flowTaskId) throws Exception {
		return wfTaskService.findTaskIn(flowTaskId);
	}

	@Override
	public PrpLWfTaskVo acceptTask(Double flowTaskId,String assignUser,String assignCom) {
		return wfTaskService.acceptTask(flowTaskId,assignUser,assignCom);
	}

	@Override
	public PrpLWfTaskVo tempSaveTask(Double flowTaskId,String handlerIdKey,String handlerUser,String handlerCom) {
		return wfTaskService.tempSaveTask(flowTaskId,handlerIdKey,handlerUser,handlerCom);
	}
	
	@Override
	public PrpLWfTaskVo tempSaveTaskByRecPay(Double flowTaskId,String handlerIdKey,String handlerUser,String handlerCom,PrplReplevyMainVo prplReplevyMainVo){
		return wfTaskService.tempSaveTaskByRecPay(flowTaskId,handlerIdKey,handlerUser,handlerCom,prplReplevyMainVo);
	}
	
	@Override
	public PrpLWfTaskVo tempSaveTask(Double flowTaskId,String itemName){
		return wfTaskService.tempSaveTask(flowTaskId,itemName);
	}
	
	@Override
	public void updateTaskOut(Double flowTaskId,String handlerIdKey){
		wfTaskService.updateTaskOut(flowTaskId, handlerIdKey);
	}
	
	
	@Override
	public PrpLWfTaskVo submitRegist(PrpLRegistVo registVo,WfTaskSubmitVo submitVo) {
		submitVo = checkSubmitVo(submitVo);
		if(SubmitType.TMP==submitVo.getSubmitType()){
			return wfRegistService.tempSaveRegistHandle(registVo,submitVo);
		}else if(SubmitType.U==submitVo.getSubmitType()){
			return wfRegistService.updateRegistHandle(registVo,submitVo);
		}else if(SubmitType.C==submitVo.getSubmitType()){
			return wfRegistService.cancelRegistHandle(registVo,submitVo);
		}

		return wfRegistService.submitRegistHandle(registVo,submitVo);
	}

	@Override
	public List<PrpLWfTaskVo> submitSchedule(PrpLRegistVo registVo,List<PrpLScheduleTaskVo> scheduleTaskVoList,WfTaskSubmitVo submitVo) {
		submitVo = checkSubmitVo(submitVo);
		return wfScheduleService.submitScheduleHandle(registVo,scheduleTaskVoList,submitVo);
	}

	@Override
	public List<PrpLWfTaskVo> submitCheck(PrpLScheduleTaskVo scheduleVo,PrpLCheckVo checkVo,WfTaskSubmitVo submitVo) {
		submitVo = checkSubmitVo(submitVo);
		if(submitVo.getCurrentNode()==FlowNode.ChkRe){
			return wfCheckService.submitCheckReToVLoss(checkVo,submitVo);
		}
		return wfCheckService.submitCheckHandle(scheduleVo,checkVo,submitVo);
	}

	@Override
	public List<PrpLWfTaskVo> submitLossCar(PrpLDlossCarMainVo lossCarMainVo,WfTaskSubmitVo submitVo) {
		submitVo = checkSubmitVo(submitVo);
		if(SubmitType.B==submitVo.getSubmitType()){
			return wfLossService.backCarLossHandle(lossCarMainVo,submitVo);
		}
		return wfLossService.submitLossCarHandle(lossCarMainVo,submitVo);
	}

	@Override
	public List<PrpLWfTaskVo> submitLossProp(PrpLdlossPropMainVo lossPropMainVo,WfTaskSubmitVo submitVo) {
		submitVo = checkSubmitVo(submitVo);
		if(SubmitType.B==submitVo.getSubmitType()){
			return wfLossService.backLossHandle(submitVo);
		}
		return wfLossService.submitLossPropHandle(lossPropMainVo,submitVo);
	}

	@Override
	public List<PrpLWfTaskVo> submitLossPerson(PrpLDlossPersTraceMainVo lossPersonMainVo,WfTaskSubmitVo submitVo) {
		submitVo = checkSubmitVo(submitVo);
		if(SubmitType.B==submitVo.getSubmitType()){
			return wfLossService.backLossHandle(submitVo);
		}
		return wfLossService.submitLossPersonHandle(lossPersonMainVo,submitVo);
	}

	private WfTaskSubmitVo checkSubmitVo(WfTaskSubmitVo submitVo) {
        if(submitVo!=null){
        	logger.info("-------------------------------------->不为空");
        }else{
        	logger.info("-------------------------------------->为空");
        }
		
		if(submitVo.getSubmitType()==null
				||submitVo.getFlowTaskId()==null
				||StringUtils.isBlank(submitVo.getFlowId())
				||StringUtils.isBlank(submitVo.getComCode())
				||StringUtils.isBlank(submitVo.getTaskInUser())
				||StringUtils.isBlank(submitVo.getTaskInKey())
				){
			logger.info("WfTaskSubmitVo 参数错误 :SubmitType:"+ submitVo.getSubmitType() 
					+  "FlowTaskId:" + submitVo.getFlowTaskId()
					+  "FlowId:" + submitVo.getFlowId()
					+  "ComCode：" + submitVo.getComCode()
					+  "TaskInUser:"+submitVo.getTaskInUser() 
					+  "TaskInKey"+ submitVo.getTaskInKey());
			throw new IllegalArgumentException("WfTaskSubmitVo 参数错误");
		}
		
//		if(StringUtils.isBlank(submitVo.getAssignUser())){
//			// TODO 轮询找到comcode下面的处理人
//			submitVo.setAssignUser(submitVo.getTaskInUser());
//			submitVo.setAssignCom(submitVo.getComCode());
//		}else if(StringUtils.isBlank(submitVo.getAssignCom())){
//			submitVo.setAssignCom(submitVo.getComCode());
//		}

		return submitVo;
	}

	@Override
	public List<PrpDNodeVo> findNode(String uppernode,String currencyNode,String nextFlag) {
		
		return nodeService.findeNode(uppernode,currencyNode,nextFlag);
	}

	@Override
	public List<PrpLWfTaskVo> findEndTask(String registNo,String handlerIdKey,FlowNode nodeCode) {
		return wfTaskService.findEndTask(registNo,handlerIdKey,nodeCode);
	}

	@Override
	public PrpLWfTaskVo addSimpleTask(WfSimpleTaskVo taskVo,WfTaskSubmitVo submitVo) {
		submitVo = checkSubmitVo(submitVo);
		return wfSimpleTaskService.addSimpleTask(taskVo,submitVo);
	}

	@Override
	public PrpLWfTaskVo submitSimpleTask(WfSimpleTaskVo taskVo,WfTaskSubmitVo submitVo) {
		submitVo = checkSubmitVo(submitVo);
		if(submitVo.getNextNode()==null){
			throw new IllegalArgumentException("WfTaskSubmitVo 参数错误,请提供[NextNode]");
		}
		if(SubmitType.B==submitVo.getSubmitType()){
			return wfLossService.backLossHandle(submitVo).get(0);
		}
	/*	if(FlowNode.CancelApp.equals(submitVo.getCurrentNode())||FlowNode.ReCanApp.equals(submitVo.getCurrentNode())){
			return wfSimpleTaskService.submitCancelTask(taskVo,submitVo);
		}
		if(FlowNode.Cancel.equals(submitVo.getCurrentNode().getRootNode())){
			return wfSimpleTaskService.submitCancelTask(taskVo,submitVo);
		}*/
		return wfSimpleTaskService.submitSimpleTask(taskVo,submitVo);
	}

	@Override
	public Boolean existTaskByNodeCode(String registNo,FlowNode nodeCode,String taskInKey,String workStatus) {
		return wfTaskService.existTaskByNodeCode(registNo,taskInKey,nodeCode,workStatus);
	}

	@Override
	public void cancelTask(String userCode,BigDecimal... flowTaskId) {
		wfTaskService.cancelTask(userCode,flowTaskId);
	}

	@Override
	public PrpLWfTaskVo handOverTask(WfTaskSubmitVo submitVo,String handoverTaskReason) {
		return wfHandoverTaskService.handOverTask(submitVo,handoverTaskReason);
	}

	@Override
	public PrpLWfTaskVo submitClaim(String flowId,PrpLClaimVo claimVo) {
		return wfClaimService.submitClaimHandle(flowId,claimVo);
	}

	@Override
	public List<PrpLWfTaskVo> submitCertify(PrpLCertifyMainVo certifyVo,WfTaskSubmitVo submitVo) {
		submitVo = checkSubmitVo(submitVo);
		return wfCertifyService.submitCertifyHandle(certifyVo,submitVo);
	}
	
	@Override
	public PrpLWfTaskVo submitCompe(PrpLCompensateVo compVo,WfTaskSubmitVo submitVo) {
		submitVo = checkSubmitVo(submitVo);
		return wfCompeService.submitCompeHandle(compVo,submitVo);
	}
	
	@Override
	public PrpLWfTaskVo addPrePayTask(PrpLClaimVo claimVo,WfTaskSubmitVo submitVo) {
		logger.info("报案号={},submitVo.getNextNode()={},进入产生预付/垫付节点方法",
				(claimVo == null? null:claimVo.getRegistNo()),(submitVo == null? null :submitVo.getNextNode()));
		submitVo = checkSubmitVo(submitVo);
		if(FlowNode.PadPay==(submitVo.getNextNode())){
			return wfCompeService.createPadPayByClaim(claimVo, submitVo);
		}
		logger.info("报案号={},submitVo.getNextNode()={},结束产生预付/垫付节点方法",
				(claimVo == null? null:claimVo.getRegistNo()),(submitVo == null? null :submitVo.getNextNode()));
		return wfCompeService.createPrePayByClaim(claimVo,submitVo);
	}
	
	@Override
	public PrpLWfTaskVo submitPrepay(PrpLCompensateVo compVo,WfTaskSubmitVo submitVo) {
		submitVo = checkSubmitVo(submitVo);
		return wfCompeService.submitPrePayHandle(compVo,submitVo);
	}
	
	@Override
	public PrpLWfTaskVo submitPadpay(PrpLPadPayMainVo padPayVo,WfTaskSubmitVo submitVo) {
		submitVo = checkSubmitVo(submitVo);
		return wfCompeService.submitPadPayHandle(padPayVo,submitVo);
	}
	
	public PrpLWfTaskVo submitRecPay(PrplReplevyMainVo rePlevyVo,WfTaskSubmitVo submitVo){
		submitVo = checkSubmitVo(submitVo);
		return wfCompeService.submitRecPay(rePlevyVo,submitVo);
	}

	@Override
	public List<PrpLWfTaskVo> submitVclaim(PrpLCompensateVo compVo,WfTaskSubmitVo submitVo) throws Exception {
		submitVo = checkSubmitVo(submitVo);
		if(submitVo.getNextNode()==null){
			log.info("WfTaskSubmitVo 参数错误,请提供[NextNode]");
			throw new IllegalArgumentException("WfTaskSubmitVo 参数错误,请提供[NextNode]");
		}
		return wfVClaimService.submitVClaimHandle(compVo,submitVo);
	}
	
	@Override
	public List<PrpLWfTaskVo> submitVclaimLevel(PrpLWfTaskVo wfTaskVo,WfTaskSubmitVo submitVo) {
		submitVo = checkSubmitVo(submitVo);
		if(submitVo.getNextNode()==null){
			throw new IllegalArgumentException("WfTaskSubmitVo 参数错误,请提供[NextNode]");
		}
		if(SubmitType.B==submitVo.getSubmitType()){
			return wfVClaimService.backLossHandle(submitVo);
		}
		return wfVClaimService.submitVClaimLevelHandle(wfTaskVo,submitVo);
	}

	@Override
	public PrpLWfTaskVo submitEndCase(PrpLEndCaseVo endCaseVo,WfTaskSubmitVo submitVo) {
		submitVo = checkSubmitVo(submitVo);
		return wfEndCaseService.submitEndCaseHandle(endCaseVo,submitVo);
	}
	
	@Override
	public PrpLWfTaskVo addReOpenAppTask(WfSimpleTaskVo taskVo,WfTaskSubmitVo submitVo) {
		// TODO Auto-generated method stub
		submitVo = checkSubmitVo(submitVo);
		return wfEndCaseService.createReOpenAppTask(taskVo,submitVo);
	}

	@Override
	public PrpLWfTaskVo submitReOpenApp(WfSimpleTaskVo taskVo,WfTaskSubmitVo submitVo) {
		// TODO Auto-generated method stub
		submitVo = checkSubmitVo(submitVo);
		return wfEndCaseService.submitReOpenAppHandle(taskVo,submitVo);
	}
	
	@Override
	public PrpLWfTaskVo submitReOpenTask(BigDecimal flowTaskId,WfSimpleTaskVo taskVo,WfTaskSubmitVo submitVo) {
		// TODO Auto-generated method stub
		submitVo = checkSubmitVo(submitVo);
		return wfEndCaseService.submitReOpenHandle(flowTaskId, taskVo,submitVo);
	}
	
	/**
	 * 查询立案的taskid
	 */
	@Override
	public PrpLWfTaskVo queryTaskId(String flowId,String riskCode) {
		return wfTaskService.queryTaskId(flowId,riskCode);
	}

	@Override
	public PrpLWfTaskVo addCancelTask(WfSimpleTaskVo taskVo,
			WfTaskSubmitVo submitVo,PrpLClaimCancelVo pClaimCancelVo) {
		submitVo = checkSubmitVo(submitVo);
		return wfSimpleTaskService.addCancelTask(taskVo,submitVo,pClaimCancelVo);
	}
	
	public void backCancelHandle(WfTaskSubmitVo submitVo,PrpLClaimCancelVo pClaimCancelVo){
		submitVo = checkSubmitVo(submitVo);
		wfSimpleTaskService.backCancelHandle(submitVo,pClaimCancelVo);
	}
	
	@Override
	public PrpLWfTaskVo backCancel(WfSimpleTaskVo taskVo,WfTaskSubmitVo submitVo) {
		submitVo = checkSubmitVo(submitVo);
		if(submitVo.getNextNode()==null){
			throw new IllegalArgumentException("WfTaskSubmitVo 参数错误,请提供[NextNode]");
		}
		return wfSimpleTaskService.backCancel(taskVo,submitVo);
	}

	@Override
	public List<PrpDNodeVo> findLowerNode(Double taskId,String currencyNode,String upperNode) {
		return wfTaskService.findLowerNode(taskId,currencyNode,upperNode);
	}

	@Override
	public void reassignmentTask(WfTaskSubmitVo submitVo, String handlerIdKey) {
		wfHandoverTaskService.reassignmentTask(submitVo, handlerIdKey);
	}
	
	public BigDecimal findTaskId(String claimNo,String subNodeCode){
		BigDecimal id = wfClaimService.findTaskId(claimNo, subNodeCode);
		return id;
	}

	@Override
	public void cancelTaskClaimRecover(WfTaskSubmitVo submitVo, String userCode, BigDecimal... flowTaskId) {
		// TODO Auto-generated method stub
		wfClaimService.cancelTaskClaimRecover(submitVo,userCode,flowTaskId);
		//wfTaskService.cancelTaskRecover(userCode,flowTaskId);
	}
	@Override
	public void cancelTaskClaim(WfTaskSubmitVo submitVo,String userCode, BigDecimal... flowTaskId) {
		// TODO Auto-generated method stub
	    wfClaimService.cancelTaskClaim(submitVo,userCode,flowTaskId);
		//wfTaskService.cancelTaskClaim(userCode,flowTaskId);
	}

	@Override
	public PrpLWfTaskVo addCompenTask(PrpLClaimVo claimVo,WfTaskSubmitVo submitVo) {
		submitVo = checkSubmitVo(submitVo);
		return wfCompeService.createCompe(claimVo,submitVo);
	}


	@Override
	public PrpLWfTaskVo addPrePayWriteOffTask(PrpLCompensateVo newCompVo,
			WfTaskSubmitVo submitVo, WfSimpleTaskVo taskVo) {
		submitVo = checkSubmitVo(submitVo);
		return wfCompeService.createPrePayWriteOff(newCompVo,submitVo,taskVo);
	}

	@Override
	public PrpLWfTaskVo tempSaveTaskByComp(Double flowTaskId,
			String handlerIdKey, String handlerUser, String handlerCom) {
		return wfTaskService.tempSaveTaskComp(flowTaskId,handlerIdKey,handlerUser,handlerCom);
	}

	@Override
	public List<PrpLWfTaskVo> findCanCelTask(String claimNo, FlowNode nodeCode) {
		return wfTaskService.findCanCelTask(claimNo,nodeCode);
	}

	@Override
	public List<PrpLWfTaskVo> findCompeByRegistNo(String registNo,String subNodecode) {
		return wfTaskService.findCompeByRegistNo(registNo,subNodecode);
	}

	@Override
	public PrpLWfTaskVo rollBackTask(PrpLWfTaskVo taskVo) {
		return wfTaskService.rollBackTask(taskVo);
	}

	//删除报案关联的保单生成立案节点
	@Override
	public BigDecimal deleteByRegist(PrpLRegistVo registVo) {

		BigDecimal upPerTaskId  = wfRegistService.deleteByRegist(registVo);
		return upPerTaskId;
	}
	
	public PrpLWfTaskVo submitClaimHandl(PrpLRegistVo registVo,WfTaskSubmitVo submitVo){
		return wfRegistService.submitClaimHandle(registVo, submitVo);
		
	}
	public PrpLWfTaskVo submitClaimHandls(PrpLRegistVo registVo,WfTaskSubmitVo submitVo){
		return wfRegistService.submitClaimHandles(registVo, submitVo);
		
	}
	public BigDecimal findByRegist(PrpLRegistVo registVo) {

		BigDecimal upPerTaskId  = wfRegistService.findByRegist(registVo);
		return upPerTaskId;
	}
	public String findByRegistNoAndNode(PrpLRegistVo registVo,String nodeCode) {
		return wfRegistService.findByRegistNoAndNode(registVo, nodeCode);
	}
	
	@Override
	public Boolean existTaskByNodeList(String registNo,List<String> nodeCodeList) {
		return wfTaskService.existTaskByNodeList(registNo,nodeCodeList);
	}

	@Override
	public Boolean existTaskByNode(String registNo, String nodeCode) {
		return wfTaskService.existTaskByNode(registNo,nodeCode);
	}
	
	@Override
	public PrpLWfTaskVo submitClaimTask(WfSimpleTaskVo taskVo,
			WfTaskSubmitVo submitVo, PrpLClaimCancelVo pClaimCancelVo) {
		if (FlowNode.CancelApp.equals(submitVo.getCurrentNode())
				|| FlowNode.ReCanApp.equals(submitVo.getCurrentNode())) {
			return wfSimpleTaskService.submitCancelTask(taskVo, submitVo,
					pClaimCancelVo);
		}
		if (FlowNode.Cancel.equals(submitVo.getCurrentNode().getRootNode())) {
			return wfSimpleTaskService.submitCancelTask(taskVo, submitVo,
					pClaimCancelVo);
		}
		return null;
	}

	@Override
	public Boolean existCancelByNode(String registNo, String nodeCode,
			String handlerstatus) {
		return wfTaskService.existCancelByNode(registNo,nodeCode,handlerstatus);
	}

	@Override
	public Boolean existCancelByNAndH(String registNo, String nodeCode) {
		return wfTaskService.existCancelByNAndH(registNo,nodeCode);
	}
	
	@Override
	public PrpLWfTaskVo addAcheckTask(PrpLAcheckMainVo prpLAcheckMainVo,WfTaskSubmitVo submitVo) {
		return wfAcheckService.addacheckTask(prpLAcheckMainVo, submitVo);
	}
	
	@Override
	public PrpLWfTaskVo submitAcheckTask(PrpLWfTaskVo taskVo,WfTaskSubmitVo submitVo){
		if(SubmitType.B==submitVo.getSubmitType()){
			return wfAcheckService.backAcheckHandle(submitVo);
		}
		return wfAcheckService.submitAcheckTask(taskVo,submitVo);
	}

	
	
	@Override
	public PrpLWfTaskVo addAssessorTask(PrpLAssessorMainVo prpLAssessorMainVo,WfTaskSubmitVo submitVo) {
		// TODO Auto-generated method stub
		return wfAssessorService.addAssessorTask(prpLAssessorMainVo,submitVo);
	}
	
	@Override
	public PrpLWfTaskVo submitAssessorTask(PrpLWfTaskVo taskVo,WfTaskSubmitVo submitVo){
		if(SubmitType.B==submitVo.getSubmitType()){
			return wfAssessorService.backAssessorHandle(submitVo);
		}
		return wfAssessorService.submitAssessorTask(taskVo,submitVo);
	}

	//删除报案关联的保单生成立案节点
	@Override
	public BigDecimal deleteByRegists(PrpLRegistVo registVo,String subNodeCode) {

		BigDecimal upPerTaskId  = wfRegistService.deleteByRegists(registVo,subNodeCode);
		return upPerTaskId;
	}
	
	@Override
	public  List<PrpLWfTaskVo> findPrpLWfTaskInByRegistNo(String registNo) {
		return wfTaskService.findPrpLWfTaskInByRegistNo(registNo);
	}

	@Override
	public List<PrpLWfTaskVo> findPrpLWfTaskInTimeDescByRegistNo(String registNo) {
		return wfTaskService.findPrpLWfTaskInTimeDescByRegistNo(registNo);
	}

	@Override
	public List<PrpLWfTaskVo> findPrpLWfTaskOutByRegistNo(String registNo) {
		return wfTaskService.findPrpLWfTaskOutByRegistNo(registNo);
	}

	@Override
	public List<PrpLWfTaskVo> findPrpLWfTaskOutTimeDescByRegistNo(String registNo) {
		return wfTaskService.findPrpLWfTaskOutTimeDescByRegistNo(registNo);
	}

	@Override
	public void cancelTaskForOther(String registNo, String userCode) {
		wfTaskService.cancelTaskForOther(registNo,userCode);
	}
	@Override
	public void recoverClaimForOther(String registNo, String userCode,BigDecimal flowTaskId) {
		wfTaskService.recoverClaimForOther(registNo,userCode,flowTaskId);
	}
	
	@Override
	public PrpLWfTaskVo queryTaskForHandlerStatus(Double handlerStatus) {
		return wfTaskService.queryTaskForHandlerStatus(handlerStatus);
	}
	
	public PrpLWfTaskVo queryTaskByAny(String registNo,String claimNo,String subNodeCode,String handlerStatus) {
		return wfTaskService.queryTaskByAny(registNo,claimNo, subNodeCode, handlerStatus);
	}

	@Override
	public void submitCertifyAfterEndCase(String registNo) {
		wfCertifyService.submitCertifyAfterEndCase(registNo);
		
	}

	public void moveOutToIn(BigDecimal flowTaskId) {
		wfTaskService.moveOutToIn(flowTaskId);
	}

	@Override
	public PrpLWfTaskVo findWftaskInByRegistnoAndSubnode(String registNo,
			String subNodeCode) {
		// TODO Auto-generated method stub
		return wfTaskService.findWftaskInByRegistNoAndSubNodeCode(registNo, subNodeCode);
	}

	@Override
	public List<PrpLWfTaskVo> findWftaskByRegistNoAndNodeCode(String RegistNo,
			String subNodeCode) {
		// TODO Auto-generated method stub
		return wfTaskService.findWftaskByRegistNoAndNodeCode(RegistNo, subNodeCode);
	}
	@Override
	public Boolean existTaskInBySubNodeCode(String registNo, String subNodeCode) {
		return wfTaskService.existTaskInBySubNodeCode(registNo,subNodeCode);
	}

	@Override
	public PrpLWfTaskVo findprplwftaskVoByNodeCodeAndflowId(String nodeCode,String flowId) {
	        QueryRule query=QueryRule.getInstance();
	        query.addEqual("flowId", flowId);
	        query.addEqual("nodeCode",nodeCode);
	        PrpLWfTaskVo taskvo=new PrpLWfTaskVo();
	        List<PrpLWfTaskOut> listout=databaseDao.findAll(PrpLWfTaskOut.class, query);
	        if(listout!=null && listout.size()>0){
	        	Beans.copy().from(listout.get(0)).to(taskvo);
	        }else{
	        	List<PrpLWfTaskIn> listin=databaseDao.findAll(PrpLWfTaskIn.class, query);
	        	if(listin!=null && listin.size()>0){
	        		Beans.copy().from(listin.get(0)).to(taskvo);
	        	}
	        }
	        
	        
		return taskvo;
	}


    @Override
    public List<PrpLCompensateVo> getPrplComepensate(List<PrpLCompensateVo> prpLCompensatesVos) {
        List<PrpLCompensateVo> compePo = new ArrayList<PrpLCompensateVo>();
        for(PrpLCompensateVo prpLCompensatesVo:prpLCompensatesVos){
            QueryRule queryRule = QueryRule.getInstance();
            queryRule.addEqual("compensateNo",prpLCompensatesVo.getCompensateNo());
            queryRule.addEqual("nodeCode","Compe");
            List<PrpLWfTaskOut> prpLWfTaskOuts = databaseDao.findAll(PrpLWfTaskOut.class,queryRule);
            for(PrpLWfTaskOut prpLWfTaskOut:prpLWfTaskOuts){
                if("3".equals(prpLWfTaskOut.getHandlerStatus())){
                    compePo.add(prpLCompensatesVo);
                }
            }
        }  
        return compePo;
    }

  /*  @Override
    public void recoverFlow(String registNo,String userCode) {
        wfTaskService.recoverFlow(registNo,userCode);
    }*/
    
    @Override
    public List<PrpLWfTaskVo> findInTask(String registNo,String handlerIdKey,String subNodeCode) {
        return wfTaskService.findInTask(registNo,handlerIdKey,subNodeCode);
    }
    
    @Override
    public List<PrpLWfTaskVo> findInTaskbyCompensateNo(String registNo,String compensateNo,String subNodeCode) {
        return wfTaskService.findInTaskbyCompensateNo(registNo,compensateNo,subNodeCode);
    }
    
    @Override
    public PrpLWfTaskVo updateTaskInFlag(Double taskId,String flag){
		return wfTaskService.updateTaskInFlag(taskId,flag);
	}
    
    @Override
    public void updateTaskByFlowId(PrpLWfTaskVo prpLWfTaskVo) throws Exception {
        wfTaskService.updateTaskIn(prpLWfTaskVo);
        wfTaskService.updateTaskOut(prpLWfTaskVo);
    }
    
    @Override
    public void updateTaskIn(PrpLWfTaskVo prpLWfTaskVo){
    	wfTaskService.updateTaskIn(prpLWfTaskVo);
    }
    
    @Override
    public void updateTaskOut(PrpLWfTaskVo prpLWfTaskVo){
    	wfTaskService.updateTaskOut(prpLWfTaskVo);
    }

    @Override
    public void updateIsMobileCaseByFlowId(List<PrpLWfTaskVo> prpLWfTaskVoList) throws Exception {
        wfTaskService.updateIsMobileCaseByFlowId(prpLWfTaskVoList);
    }

    @Override
    public List<PrpLWfTaskVo> findInTaskByOther(String registNo,String handlerIdKey,String nodeCode) {
        return wfTaskService.findInTaskByOther(registNo,handlerIdKey,nodeCode);
    }

	@Override
	public List<PrpLWfTaskVo> findPrpLWfTaskByFlowIdAndnodeCode(String flowId,String flag,List<String> nodeCodes) {
		
		return wfTaskService.findPrpLWfTaskByFlowIdAndnodeCode(flowId,flag,nodeCodes);
	}

	@Override
	public PrpLWfTaskVo findOutWfTaskVo(String nodeCode, String registNo) {
		
		return wfTaskService.findOutWfTaskVo(nodeCode,registNo);
	}
	 @Override
	 public List<PrpLWfTaskVo> findTaskInVo(String registNo,String compensateNo,String nodeCode) {
	        return wfTaskService.findTaskInVo(registNo,compensateNo,nodeCode);
	 }
	
	 @Override
	  public List<PrpLWfTaskVo> findTaskOutVo(String registNo,String nodeCode) {
	        return wfTaskService.findTaskOutVo(registNo,nodeCode);
	  }
    public void rollBackFlow(PrpLScheduleTaskVo vo,String flowId){
        wfAssessorService.rollBackFlow(vo,flowId);
    }
    public void rollBackNodeAndLWfMain(PrpLScheduleTaskVo vo,String flowId){
        wfAssessorService.rollBackNodeAndLWfMain(vo,flowId);
    }


	@Override
	public List<PrpLWfTaskVo> findPrpLWfTaskByregistNoAndnodeCode(String registNo,String flag,List<String> nodeCodes) {
		
		return wfTaskService.findPrpLWfTaskByregistNoAndnodeCode(registNo,flag,nodeCodes);
	}
    

    @Override
    public List<PrpLWfTaskVo> submitVclaimLevelByIlog(PrpLWfTaskVo wfTaskVo,List<WfTaskSubmitVo> submitVos,SysUserVo sysUserVo) {
        for(WfTaskSubmitVo submitVo : submitVos){
            submitVo = checkSubmitVo(submitVo);
        }
        return wfVClaimService.backLossHandleByIlog(wfTaskVo,submitVos,sysUserVo);
    }
    

    @Override 
    public void backToCertiOrDLCar(PrpLWfTaskVo wfTaskVo,List<WfTaskSubmitVo> submitVos,SysUserVo sysUserVo) {
        wfVClaimService.backToCertiOrDLCar(wfTaskVo,submitVos,sysUserVo);
    }

    @Override
    public List<PrpLWfTaskVo> findInTaskVo(String registNo,String compensateNo,String nodeCode) {
        return wfTaskService.findInTaskVo(registNo,compensateNo,nodeCode);
    }

    @Override
    public List<PrpLWfTaskVo> findOutTaskVo(String registNo,String compensateNo,String nodeCode) {
        return wfTaskService.findOutTaskVo(registNo,compensateNo,nodeCode);
    }
    

    @Override
    public PrpLWfTaskVo rollBackTaskByEndVo(PrpLWfTaskVo taskVo) {
        return wfTaskService.rollBackTaskByEndVo(taskVo);
    }
    
    @Override
    public PrpLWfTaskVo deteleTaskVo(PrpLWfTaskVo taskVo) {
        PrpLWfTaskIn fTaskIn = null;
        fTaskIn = databaseDao.findByPK(PrpLWfTaskIn.class,taskVo.getTaskId());
        if(fTaskIn != null){
            databaseDao.deleteByPK(PrpLWfTaskIn.class,taskVo.getTaskId());
        }else{
            databaseDao.deleteByPK(PrpLWfTaskOut.class,taskVo.getTaskId());
        }
        return taskVo;
    }

	@Override
	public PrpLWfTaskVo findLastVlossTask(String registNo) {
		return wfTaskService.findLastVlossTask(registNo);
	}

    @Override
    public List<PrpLWfTaskVo> queryTaskByAnyOrderOutTime(String registNo,String claimNo,String subNodeCode,String handlerStatus) {
        return wfTaskService.queryTaskByAnyOrderOutTime(registNo,claimNo,subNodeCode,handlerStatus);
    }

    @Override
	public Boolean existTaskInByNodeCode(String registNo, String nodeCode) {
		// TODO Auto-generated method stub
		return wfTaskService.existTaskInByNodeCode(registNo,nodeCode);
	}


	@Override
	public Boolean generateFlow(String registNo) {
		return wfTaskService.generateFlow(registNo);
	}

	@Override
	public Boolean generateFlowLevel(String registNo) {
		return wfTaskService.generateFlowLevel(registNo);
	}

	@Override
	public BigDecimal recPayLaunch(PrpLWfTaskVo prpLWfTaskVo) {
		BigDecimal saveTaskIn = wfTaskService.saveTaskIn(prpLWfTaskVo);
		return saveTaskIn;
	}

	@Override
	public List<PrpLWfTaskVo> findAllTaskVo(String registNo,
			BigDecimal upperTaskId, String subNodeCode) {
		return wfTaskService.findAllTaskVo(registNo, upperTaskId, subNodeCode);
	}
	
	@Override
	public Boolean existTaskByNodeCodeYJ(String registNo,FlowNode nodeCode,String taskInKey,String workStatus) {
		return wfTaskService.existTaskByNodeCodeYJ(registNo,taskInKey,nodeCode,workStatus);
	}

	@Override
	public Boolean existTaskIn(String riskCode,String registNo) {
		
		return wfTaskService.existTaskIn(riskCode,registNo);
	}

	@Override
	public void updateTaskUserInfo(PrpLWfTaskUserInfoVo fTaskUserInfoVo) {
		wfTaskService.updateTaskUserInfo(fTaskUserInfoVo);
	}

	@Override
	public List<PrpLWfTaskUserInfoVo> queryTaskUserInfo(String flowId) {
		return wfTaskService.queryTaskUserInfo(flowId);
	}
	
	@Override
	public Integer findTaskReOpenCount(PrpLWfTaskVo wfTaskVo) {
		Integer reOpenCount=0;
		if(!"ReOpenApp".equals(wfTaskVo.getSubNodeCode())){
			PrpLWfTaskVo prpLWfTask=wfTaskVo;
			List<PrpLWfTaskVo> prpLWfTaskInVos=wfTaskService.findTaskByReOpen(wfTaskVo.getRegistNo(), wfTaskVo.getTaskInKey(),null);
			for (PrpLWfTaskVo prpLWfTaskVo : prpLWfTaskInVos) {
				if(prpLWfTask.getUpperTaskId().equals(prpLWfTaskVo.getTaskId())){
					if("ReOpenApp".equals(prpLWfTaskVo.getSubNodeCode())){
						BigDecimal taskid=null;
						for (PrpLWfTaskVo prpLWfTask1 : prpLWfTaskInVos) {
							if("ReOpenApp".equals(prpLWfTask1.getSubNodeCode())){
								if(!prpLWfTaskVo.getTaskId().equals(prpLWfTask1.getTaskId())){
									reOpenCount++;
								}else{
									break;
								}
							}
						}
						break;
					}else{
						prpLWfTask=prpLWfTaskVo;
					}
				}
			}
		}else{
			List<PrpLWfTaskVo> prpLWfTaskInVos=wfTaskService.findTaskByReOpen(wfTaskVo.getRegistNo(), wfTaskVo.getTaskInKey(), "ReOpenApp");
			for (PrpLWfTaskVo prpLWfTaskVo : prpLWfTaskInVos) {
				if(!wfTaskVo.getTaskId().equals(prpLWfTaskVo.getTaskId())){
					reOpenCount++;
				}else{
					break;
				}
			}
		}
		return reOpenCount;
	}
	public boolean UpperbytaskId(PrpLWfTaskVo wfTask,PrpLWfTaskVo upWfTask) {
		if(wfTask.getUpperTaskId().equals(upWfTask.getTaskId())){
			if("ReOpenApp".equals(upWfTask.getSubNodeCode())){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}

	/* 
	 * @see ins.sino.claimcar.flow.service.WfTaskHandleService#findoutTaskVoByTaskId(java.lang.Long)
	 * @param taskId
	 * @return
	 */
	@Override
	public PrpLWfTaskVo findoutTaskVoByTaskId(Double taskId) {
		return 	wfTaskService.findoutTaskVoByTaskId(taskId);
	}

	/* 
	 * @see ins.sino.claimcar.flow.service.WfTaskHandleService#findOutTaskVos(java.lang.String, java.lang.String, java.util.List)
	 * @param registNo
	 * @param handlerIdKey
	 * @param subnodecodes
	 * @return
	 */
	@Override
	public Boolean findOutTaskVos(Map<String,Object> params) {
		return wfTaskService.findOutTaskVos(params);
	}

	@Override
	public PrpLWfTaskVo findOutWfTaskVo(String nodeCode, String registNo,String riskCode) {
		 QueryRule queryRule = QueryRule.getInstance();
	        queryRule.addEqual("registNo",registNo);
	        queryRule.addEqual("nodeCode",nodeCode);
	        if(StringUtils.isNotBlank(riskCode) && "1101".equals(riskCode)){
	        	 queryRule.addEqual("riskCode",riskCode);
	        }else{
	        	queryRule.addNotEqual("riskCode", "1101");
	        }
	       
	        queryRule.addAscOrder("taskOutTime");
	        List<PrpLWfTaskOut> prpLWfTaskOuts =databaseDao.findAll(PrpLWfTaskOut.class,queryRule);
	        PrpLWfTaskVo taskOutVo=null;
	        if(prpLWfTaskOuts!=null && prpLWfTaskOuts.size()>0){
	            taskOutVo=new PrpLWfTaskVo();
	            PrpLWfTaskOut taskOut=prpLWfTaskOuts.get(0);
	            Beans.copy().from(taskOut).to(taskOutVo);
	        }
	        return taskOutVo;
	}

	@Override
	public PrpLWfTaskVo saveTaskIn(PrpLWfTaskVo prpLWfTaskInVo) {
		return wfTaskService.saveprplwfTaskIn(prpLWfTaskInVo);
	}


}
