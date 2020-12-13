package ins.sino.claimcar.flow.service.handle;

import ins.sino.claimcar.CodeConstants.FlowStatus;
import ins.sino.claimcar.CodeConstants.HandlerStatus;
import ins.sino.claimcar.CodeConstants.WorkStatus;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.WfMainService;
import ins.sino.claimcar.flow.service.WfTaskService;
import ins.sino.claimcar.flow.vo.PrpLWfMainVo;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.flow.vo.WfTaskSubmitVo;
import ins.sino.claimcar.other.vo.PrpLAcheckMainVo;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("wfAcheckService")
public class WfAcheckService  extends WfBaseHandleService{

	@Autowired
	private WfTaskService wfTaskService;
	@Autowired
	private WfMainService wfMainService;

	public PrpLWfTaskVo addacheckTask(PrpLAcheckMainVo prpLAcheckMainVo,WfTaskSubmitVo submitVo) {

		PrpLWfMainVo wfMainVo = this.createWfMainVo(prpLAcheckMainVo,submitVo);

		PrpLWfTaskVo taskInVo = this.setWfTaskInVo(prpLAcheckMainVo,submitVo,FlowNode.CheckFee);
		// 发起的公估费任务直接变成正在处理
		taskInVo.setHandlerStatus(HandlerStatus.DOING);// 未处理
		taskInVo.setWorkStatus(WorkStatus.DOING);// 未处理
		taskInVo.setHandlerCom(submitVo.getAssignCom());
		taskInVo.setHandlerUser(submitVo.getTaskInUser());
		taskInVo.setHandlerTime(new Date());
		taskInVo.setHandlerMinutes(0);

		wfMainService.save(wfMainVo);

		BigDecimal TaskId = wfTaskService.saveTaskIn(taskInVo);
		taskInVo.setTaskId(TaskId);

		return taskInVo;
	}

	public PrpLWfTaskVo submitAcheckTask(PrpLWfTaskVo taskVo,WfTaskSubmitVo submitVo) {

		PrpLWfTaskVo taskInVo = new PrpLWfTaskVo();
		// 处理task信息
		super.setTaskInVo(taskInVo,submitVo,submitVo.getNextNode());
		taskInVo.setItemName(taskVo.getItemName());
		taskInVo.setRegistNo(taskVo.getRegistNo());
		taskInVo.setHandlerTime(submitVo.getHandlertime());
		taskInVo.setHandlerIdKey(submitVo.getHandleIdKey());

		wfTaskService.submitTask(submitVo.getFlowTaskId(),taskInVo);
		return taskInVo;
	}
	
	/** 任务回退处理 */
	public PrpLWfTaskVo backAcheckHandle(WfTaskSubmitVo submitVo) {
		String handleIdKey = submitVo.getHandleIdKey();
		FlowNode currentNode = submitVo.getCurrentNode();
		FlowNode nextNode = submitVo.getNextNode();
		// 获得回退时上次处理已完成的节点
		PrpLWfTaskVo taskBackVo = wfTaskService.findBackOutVo(handleIdKey,submitVo.getFlowId(),nextNode);

		PrpLWfTaskVo taskInVo = new PrpLWfTaskVo();
		super.setTaskBackInVo(taskInVo,submitVo,nextNode,taskBackVo);
		taskInVo.setRemark(currentNode.name()+" Back To "+nextNode.name());

		wfTaskService.submitTask(submitVo.getFlowTaskId(),taskInVo);
		return taskInVo;
	}

	private PrpLWfMainVo createWfMainVo(PrpLAcheckMainVo prpLAcheckMainVo,WfTaskSubmitVo submitVo) {
		// 理赔流程主信息
		PrpLWfMainVo wfMainVo = new PrpLWfMainVo();
		wfMainVo.setFlowId(prpLAcheckMainVo.getTaskId());
		wfMainVo.setRegistNo(prpLAcheckMainVo.getTaskId());
		wfMainVo.setPolicyNo(prpLAcheckMainVo.getTaskId());
		wfMainVo.setComCode(submitVo.getComCode());
		wfMainVo.setFlowStatus(FlowStatus.NORMAL);// 正常处理
		wfMainVo.setLastNode(FlowNode.CheckFeeQuery.name());
		wfMainVo.setStoreFlag(null);
		wfMainVo.setBusinessFlag("");
		wfMainVo.setComCodePly(submitVo.getComCode());
		return wfMainVo;
	}

	private PrpLWfTaskVo setWfTaskInVo(PrpLAcheckMainVo prpLAcheckMainVo,WfTaskSubmitVo submitVo,FlowNode node) {
		// 理赔流程正在处理任务节点
		
		PrpLWfTaskVo taskInVo = new PrpLWfTaskVo();
		taskInVo.setFlowId(prpLAcheckMainVo.getTaskId());
		taskInVo.setRegistNo(prpLAcheckMainVo.getTaskId());
		taskInVo.setHandlerIdKey(prpLAcheckMainVo.getId().toString());
		taskInVo.setTaskName(node.getName());
		taskInVo.setUpperTaskId(new BigDecimal(0));
		taskInVo.setNodeCode(node.getRootNode().name());
		taskInVo.setSubNodeCode(FlowNode.CheckFeeTaskQuery.name());
		taskInVo.setComCode(submitVo.getComCode());
		taskInVo.setHandlerStatus(HandlerStatus.DOING);// 正在处理
		taskInVo.setWorkStatus(WorkStatus.DOING);// 正在处理

		taskInVo.setTaskInTime(new Date());
		taskInVo.setTaskInUser(submitVo.getTaskInUser());
		taskInVo.setTaskInKey(prpLAcheckMainVo.getId().toString());

		taskInVo.setAssignUser(submitVo.getAssignUser());
		taskInVo.setAssignCom(submitVo.getAssignCom());
		taskInVo.setTaskInNode(node.name());

		return taskInVo;
	}
}
