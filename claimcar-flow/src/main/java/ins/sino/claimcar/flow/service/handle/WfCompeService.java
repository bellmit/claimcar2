package ins.sino.claimcar.flow.service.handle;

import ins.sino.claimcar.CodeConstants.WorkStatus;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claim.vo.PrpLPadPayMainVo;
import ins.sino.claimcar.claim.vo.PrpLPadPayPersonVo;
import ins.sino.claimcar.claim.vo.PrplReplevyMainVo;
import ins.sino.claimcar.constant.Risk;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.WfTaskQueryService;
import ins.sino.claimcar.flow.service.WfTaskService;
import ins.sino.claimcar.flow.util.TaskExtMapUtils;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.flow.vo.WfSimpleTaskVo;
import ins.sino.claimcar.flow.vo.WfTaskSubmitVo;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 理算工作流服务处理
 * @author ★LiuPing
 * @CreateTime 2016年1月11日
 */
@Service("wfCompeService")
public class WfCompeService extends WfBaseHandleService {
	
	@Autowired
	private WfTaskService wfTaskService;
	@Autowired
	private WfTaskQueryService wfTaskQueryService;

	
	/**
	 * 理算提交核赔
	 * @param compVo
	 * @param flowId
	 * @return
	 * @modified: ☆LiuPing(2016年4月9日 ): <br>
	 */
	public PrpLWfTaskVo submitCompeHandle(PrpLCompensateVo compVo,WfTaskSubmitVo submitVo) {
		String flowId = submitVo.getFlowId();
		logger.info("报案号registno=" + (compVo == null ? null : compVo.getRegistNo()) +",计算书号="+(compVo == null ? null : compVo.getCompensateNo())+  "更新处理完成的主节点信息");
		// 更新处理完成的主节点信息
		super.updateWfEndNodeVo(flowId,FlowNode.Compe);
		logger.info("报案号registno=" + (compVo == null ? null : compVo.getRegistNo())+",计算书号="+(compVo == null ? null : compVo.getCompensateNo())+  "初始化保存下一个核赔主节点");
		// 初始化保存下一个主节点
		super.saveWfNextNodeVo(submitVo,submitVo.getNextNode());
		// 更新taskQuery表信息
		/*PrpLWfTaskQueryVo taskQueryVo = new PrpLWfTaskQueryVo();
		taskQueryVo.setFlowId(flowId);
		taskQueryVo.setMercyFlag(compVo.getMercyFlag());// 案件紧急程度
		taskQueryVo.setReconcileFlag(checkVo.getReconcileFlag());// 是否现场调解
		wfTaskQueryService.update(taskQueryVo);
		*/
		// 处理tag和showXml
		TaskExtMapUtils extMapUtil = new TaskExtMapUtils();
		extMapUtil.addTagMap(compVo,"caseType","recoveryFlag","lawsuitFlag","allowFlag");
		extMapUtil.addXmlMap(compVo,"sumAmt","sumFee");
		// 处理task信息
		PrpLWfTaskVo taskInVo = new PrpLWfTaskVo();
		super.setTaskInVo(taskInVo,submitVo,submitVo.getNextNode());
		taskInVo.setHandlerIdKey(compVo.getCompensateNo());
		taskInVo.setRegistNo(compVo.getRegistNo());
		taskInVo.setClaimNo(compVo.getClaimNo());
		taskInVo.setBussTagMap(extMapUtil.getBussTagMap());
		taskInVo.setCompensateNo(compVo.getCompensateNo());
		taskInVo.setShowInfoXML(extMapUtil.getShowInfoXML());
		taskInVo.setYwTaskType(submitVo.getCurrentNode().name());//记录进入核赔的节点

		//金额排序
		BigDecimal sumAmt = new BigDecimal(0);
		if(compVo.getSumAmt() != null){
			sumAmt = sumAmt.add(compVo.getSumAmt());
		}
		if(compVo.getSumFee() != null){
			sumAmt = sumAmt.add(compVo.getSumFee());
		}
		taskInVo.setMoney(sumAmt);
		wfTaskService.submitTask(submitVo.getFlowTaskId(),taskInVo);
		logger.info("报案号registno=" + (compVo == null ? null : compVo.getRegistNo())+ ",计算书号="+(compVo == null ? null : compVo.getCompensateNo())+  "完成理赔任务，将理算工作流信息从in移到out");
		return taskInVo;

	}
	
	/**
	 * 发起预付任务
	 * @param claimVo
	 * @param submitVo
	 * @return
	 * @modified:
	 * ☆XMSH(2016年4月14日 下午3:10:37): <br>
	 */
	public PrpLWfTaskVo createPrePayByClaim(PrpLClaimVo claimVo,WfTaskSubmitVo submitVo) {

		String flowId = submitVo.getFlowId();
		
		List<PrpLWfTaskVo> TaskVos = wfTaskService.queryTask(flowId,submitVo.getCurrentNode());
		PrpLWfTaskVo taskInVo = TaskVos.get(0);
		if("1101".equals(claimVo.getRiskCode())){
			submitVo.setCurrentNode(FlowNode.ClaimCI);
			super.setTaskInVo(taskInVo,submitVo,FlowNode.PrePayCI);
		}else{
			submitVo.setCurrentNode(FlowNode.ClaimBI);
			super.setTaskInVo(taskInVo,submitVo,FlowNode.PrePayBI);
		}
		taskInVo.setTaskId(null);
		taskInVo.setHandlerIdKey(claimVo.getClaimNo());
		taskInVo.setAssignUser(submitVo.getAssignUser());
		taskInVo.setAssignCom(submitVo.getAssignCom());
		taskInVo.setCompensateNo(submitVo.getHandleIdKey());
		taskInVo.setClaimNo(claimVo.getClaimNo());
		taskInVo.setRiskCode(claimVo.getRiskCode());
		taskInVo.setWorkStatus(WorkStatus.INIT);
		wfTaskService.saveTaskIn(taskInVo);
		
		return taskInVo;
	}
	
	/**
	 * 发起垫付任务
	 * @param claimVo
	 * @param submitVo
	 * @return
	 */
	public PrpLWfTaskVo createPadPayByClaim(PrpLClaimVo claimVo,WfTaskSubmitVo submitVo) {
		String flowId = submitVo.getFlowId();
		List<PrpLWfTaskVo> TaskVos = wfTaskService.queryTask(flowId,submitVo.getCurrentNode());
		PrpLWfTaskVo taskInVo = TaskVos.get(0);
		super.setTaskInVo(taskInVo,submitVo,submitVo.getNextNode());
//		taskInVo.setTaskId(null);
		taskInVo.setAssignCom(submitVo.getAssignCom());
		taskInVo.setAssignUser(null);
		taskInVo.setHandlerUser(null);
		taskInVo.setHandlerCom(null);
		taskInVo.setHandlerIdKey(claimVo.getClaimNo());
		taskInVo.setCompensateNo(submitVo.getHandleIdKey());
		taskInVo.setClaimNo(claimVo.getClaimNo());
		taskInVo.setRiskCode(claimVo.getRiskCode());
		taskInVo.setWorkStatus(WorkStatus.INIT);
		BigDecimal taskId = wfTaskService.saveTaskIn(taskInVo);
		taskInVo.setTaskId(taskId);
		return taskInVo;
	}
	
	/**
	 * 预付提交核赔
	 * @param compVo
	 * @param submitVo
	 * @return
	 * @modified:
	 * ☆XMSH(2016年4月14日 上午9:59:27): <br>
	 */
	public PrpLWfTaskVo submitPrePayHandle(PrpLCompensateVo compVo,WfTaskSubmitVo submitVo) {
//		String flowId = submitVo.getFlowId();

		// 更新处理完成的主节点信息
//		super.updateWfEndNodeVo(flowId,submitVo.getCurrentNode());
		// 初始化保存下一个主节点
		super.saveWfNextNodeVo(submitVo,submitVo.getNextNode());
		// 更新taskQuery表信息
		/*PrpLWfTaskQueryVo taskQueryVo = new PrpLWfTaskQueryVo();
		taskQueryVo.setFlowId(flowId);
		taskQueryVo.setMercyFlag(compVo.getMercyFlag());// 案件紧急程度
		taskQueryVo.setReconcileFlag(checkVo.getReconcileFlag());// 是否现场调解
		wfTaskQueryService.update(taskQueryVo);
		*/
		// 处理tag和showXml
		TaskExtMapUtils extMapUtil = new TaskExtMapUtils();
		extMapUtil.addTagMap(compVo,"caseType","recoveryFlag","lawsuitFlag","allowFlag");
		extMapUtil.addXmlMap(compVo,"sumAmt","sumFee");
		

		// 处理task信息
		PrpLWfTaskVo taskInVo = new PrpLWfTaskVo();
		super.setTaskInVo(taskInVo,submitVo,submitVo.getNextNode());
		taskInVo.setHandlerIdKey(compVo.getCompensateNo());
		taskInVo.setRegistNo(compVo.getRegistNo());
		taskInVo.setRiskCode(compVo.getRiskCode());
		taskInVo.setClaimNo(compVo.getClaimNo());
		taskInVo.setCompensateNo(compVo.getCompensateNo());
		taskInVo.setBussTagMap(extMapUtil.getBussTagMap());
		taskInVo.setShowInfoXML(extMapUtil.getShowInfoXML());
		taskInVo.setYwTaskType(submitVo.getCurrentNode().toString());

		//金额排序
		BigDecimal sumAmt = new BigDecimal(0);
		if(compVo.getSumAmt() != null){
			sumAmt = sumAmt.add(compVo.getSumAmt());
		}
		if(compVo.getSumFee() != null){
			sumAmt = sumAmt.add(compVo.getSumFee());
		}
		taskInVo.setMoney(sumAmt);
		
		wfTaskService.submitTask(submitVo.getFlowTaskId(),taskInVo);

		return taskInVo;

	}
	
	/**
	 * 垫付提交到核赔
	 * @param padPayVo
	 * @param submitVo
	 * @return
	 * @modified:
	 * ☆XMSH(2016年4月14日 上午10:23:01): <br>
	 */
	public PrpLWfTaskVo submitPadPayHandle(PrpLPadPayMainVo padPayVo,WfTaskSubmitVo submitVo) {
//		String flowId = submitVo.getFlowId();

		// 更新处理完成的主节点信息
//		super.updateWfEndNodeVo(flowId,FlowNode.PadPay);
		// 初始化保存下一个主节点
		super.saveWfNextNodeVo(submitVo,FlowNode.VClaim);
		// 更新taskQuery表信息
		/*PrpLWfTaskQueryVo taskQueryVo = new PrpLWfTaskQueryVo();
		taskQueryVo.setFlowId(flowId);
		taskQueryVo.setMercyFlag(compVo.getMercyFlag());// 案件紧急程度
		taskQueryVo.setReconcileFlag(checkVo.getReconcileFlag());// 是否现场调解
		wfTaskQueryService.update(taskQueryVo);
		*/
		// 处理tag和showXml
		TaskExtMapUtils extMapUtil = new TaskExtMapUtils();
		extMapUtil.addTagMap(padPayVo,"caseType","recoveryFlag","lawsuitFlag","allowFlag");
		extMapUtil.addXmlMap(padPayVo,"sumAmt","sumFee");
		// 处理task信息
		PrpLWfTaskVo taskInVo = new PrpLWfTaskVo();
		super.setTaskInVo(taskInVo,submitVo,submitVo.getNextNode());
		taskInVo.setHandlerIdKey(padPayVo.getCompensateNo().toString());
		taskInVo.setRegistNo(padPayVo.getRegistNo());
		taskInVo.setClaimNo(padPayVo.getClaimNo());
		taskInVo.setCompensateNo(padPayVo.getCompensateNo());
		taskInVo.setBussTagMap(extMapUtil.getBussTagMap());
		taskInVo.setShowInfoXML(extMapUtil.getShowInfoXML());
		taskInVo.setTaskOutNode(FlowNode.VClaim_LV1.toString());
		taskInVo.setItemName(padPayVo.getPrpLPadPayPersons().get(0).getLicenseNo());
		taskInVo.setRiskCode(Risk.DQZ);
		taskInVo.setYwTaskType(submitVo.getCurrentNode().toString());

		//金额排序
		BigDecimal sumAmt = new BigDecimal(0);
		if(padPayVo.getPrpLPadPayPersons()!=null){
			for(PrpLPadPayPersonVo vo : padPayVo.getPrpLPadPayPersons()){
				if(vo.getCostSum() != null){
					sumAmt = sumAmt.add(vo.getCostSum());
				}
			}
		}
		
		taskInVo.setMoney(sumAmt);
		
		wfTaskService.submitTask(submitVo.getFlowTaskId(),taskInVo);

		return taskInVo;

	}
	
	public PrpLWfTaskVo submitRecPay(PrplReplevyMainVo rePlevyVo,WfTaskSubmitVo submitVo) {
//		String flowId = submitVo.getFlowId();

		// 更新处理完成的主节点信息
//		super.updateWfEndNodeVo(flowId,FlowNode.RecPay);
		// 初始化保存下一个主节点
		super.saveWfNextNodeVo(submitVo,FlowNode.END);
		// 更新taskQuery表信息
		/*PrpLWfTaskQueryVo taskQueryVo = new PrpLWfTaskQueryVo();
		taskQueryVo.setFlowId(flowId);
		taskQueryVo.setMercyFlag(compVo.getMercyFlag());// 案件紧急程度
		taskQueryVo.setReconcileFlag(checkVo.getReconcileFlag());// 是否现场调解
		wfTaskQueryService.update(taskQueryVo);
		*/
		// 处理tag和showXml
		TaskExtMapUtils extMapUtil = new TaskExtMapUtils();
//		extMapUtil.addTagMap(rePlevyVo,"caseType","recoveryFlag");
		extMapUtil.addXmlMap(rePlevyVo,"replevyType","sumPlanReplevy");
		
		// 处理task信息
		PrpLWfTaskVo taskInVo = new PrpLWfTaskVo();
		super.setTaskInVo(taskInVo,submitVo,FlowNode.END);
		taskInVo.setClaimNo(rePlevyVo.getClaimNo());
		taskInVo.setHandlerIdKey(rePlevyVo.getId().toString());
		taskInVo.setRegistNo(rePlevyVo.getRegistNo());
//		taskInVo.setBussTagMap(extMapUtil.getBussTagMap());
		taskInVo.setShowInfoXML(extMapUtil.getShowInfoXML());

		wfTaskService.submitTask(submitVo.getFlowTaskId(),taskInVo);

		return taskInVo;
		
	}

	/**
	 * 发起理算任务
	 * @param claimVo
	 * @param submitVo
	 * @return
	 * @modified:
	 */
	public PrpLWfTaskVo createCompe(PrpLClaimVo claimVo,WfTaskSubmitVo submitVo) {

		String flowId = submitVo.getFlowId();
		//防止其他调用点出问题 重新赋值
		submitVo.setCurrentNode(FlowNode.Certi);
		List<PrpLWfTaskVo> TaskVos = wfTaskService.queryTask(flowId,submitVo.getCurrentNode());
		PrpLWfTaskVo taskInVo = TaskVos.get(0);
		if("1101".equals(claimVo.getRiskCode())){
			super.setTaskInVo(taskInVo,submitVo,FlowNode.CompeCI);
		}else{
			super.setTaskInVo(taskInVo,submitVo,FlowNode.CompeBI);
		}
		//理算到岗 去掉人员
//		taskInVo.setAssignCom(null);
		taskInVo.setAssignUser(null);
		System.out.println("kkfdfdfdfkk"+submitVo.getFlowTaskId());
		taskInVo.setTaskId(submitVo.getFlowTaskId());
		taskInVo.setHandlerIdKey(claimVo.getClaimNo());
		taskInVo.setClaimNo(claimVo.getClaimNo());
		taskInVo.setRiskCode(claimVo.getRiskCode());
		taskInVo.setWorkStatus(WorkStatus.INIT);
		taskInVo.setHandlerUser(null);
		taskInVo.setHandlerTime(null);
		wfTaskService.saveTaskIn(taskInVo);
		
		return taskInVo;
	}
	/**
	 * 发起理算冲销任务
	 * @param claimVo
	 * @param submitVo
	 * @return
	 * @modified:
	 */
	public PrpLWfTaskVo createPrePayWriteOff(PrpLCompensateVo newCompVo,WfTaskSubmitVo submitVo,WfSimpleTaskVo taskVo) {

		String flowId = submitVo.getFlowId();
		
		List<PrpLWfTaskVo> TaskVos = wfTaskService.queryTask(flowId,submitVo.getCurrentNode());
		PrpLWfTaskVo taskInVo = TaskVos.get(0);
		if("1101".equals(newCompVo.getRiskCode())){
			submitVo.setCurrentNode(FlowNode.ClaimCI);
			super.setTaskInVo(taskInVo,submitVo,FlowNode.CompeWfCI);
		}else{
			submitVo.setCurrentNode(FlowNode.ClaimBI);
			super.setTaskInVo(taskInVo,submitVo,FlowNode.CompeWfBI);
		}
		taskInVo.setTaskId(submitVo.getFlowTaskId());
		taskInVo.setHandlerIdKey(newCompVo.getClaimNo());
//		taskInVo.setCompensateNo(submitVo.getHandleIdKey());
		taskInVo.setClaimNo(newCompVo.getClaimNo());
		taskInVo.setRiskCode(newCompVo.getRiskCode());
		taskInVo.setWorkStatus(WorkStatus.INIT);
		taskInVo.setHandlerIdKey(taskVo.getHandlerIdKey());
		taskInVo.setItemName(taskVo.getItemName());
		taskInVo.setRegistNo(taskVo.getRegistNo());
		taskInVo.setBussTag(taskVo.getBussTag());
		taskInVo.setShowInfoXML(taskVo.getShowInfoXml());
		//taskInVo.setShowInfoXML(showInfoXML);
		wfTaskService.saveTaskIn(taskInVo);
		
		return taskInVo;
	}
	
}
