/******************************************************************************
* CREATETIME : 2016年1月11日 下午5:40:28
******************************************************************************/
package ins.sino.claimcar.flow.service.handle;

import ins.framework.dao.database.DatabaseDao;
import ins.sino.claimcar.CodeConstants.FlowStatus;
import ins.sino.claimcar.CodeConstants.HandlerStatus;
import ins.sino.claimcar.CodeConstants.WorkStatus;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.po.PrpLWfTaskIn;
import ins.sino.claimcar.flow.service.WfMainService;
import ins.sino.claimcar.flow.service.WfNodeService;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfMainVo;
import ins.sino.claimcar.flow.vo.PrpLWfNodeVo;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.flow.vo.WfTaskSubmitVo;
import ins.sino.claimcar.schedule.vo.PrpLScheduleDefLossVo;
import ins.sino.claimcar.schedule.vo.PrpLScheduleTaskVo;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 工作流数据公用工具
 * @author ★LiuPing
 * @CreateTime 2016年1月11日
 */
@Service("wBaseHandleService")
public class WfBaseHandleService {

	protected static Logger logger = LoggerFactory.getLogger(WfBaseHandleService.class);

	@Autowired
	protected WfMainService wfMainService;
	@Autowired
	protected WfNodeService wfNodeService;
	
	@Autowired
	protected WfTaskHandleService wfTaskHandleService;
    @Autowired
    protected DatabaseDao databaseDao;
	/**
	 * 更新处理完成的节点 信息
	 * @param prpLRegistVo
	 * @param submitVo
	 * @param nowDate
	 * @param currentNodeCode
	 * @return
	 * @throws Exception
	 */
	protected void updateWfEndNodeVo(String flowId,FlowNode endNode) {

		endNode = endNode.getRootNode();
		PrpLWfMainVo wfMainVo = new PrpLWfMainVo();
		wfMainVo.setFlowId(flowId);
		wfMainVo.setLastNode(endNode.name());
		wfMainService.update(wfMainVo);

		Date now = new Date();
		PrpLWfNodeVo schedNodeVo = new PrpLWfNodeVo();
		schedNodeVo.setFlowId(flowId);
		schedNodeVo.setNodeCode(endNode.name());
		schedNodeVo.setHandlerStatus(HandlerStatus.END);
		schedNodeVo.setNodeOutTime(now);
		schedNodeVo.setUpdateTime(now);
		wfNodeService.update(schedNodeVo);

	}

	/**
	 * 注销节点，处理PrpLWfMainVo，和 PrpLWfNodeVo
	 * @param flowId
	 * @modified: ☆LiuPing(2016年2月26日 ): <br>
	 */
	protected void cancelWfNode(WfTaskSubmitVo submitVo,FlowNode node) {
		String flowId = submitVo.getFlowId();

		PrpLWfMainVo wfMainVo = new PrpLWfMainVo();
		wfMainVo.setFlowId(flowId);
		wfMainVo.setFlowStatus(FlowStatus.CANCEL);
		wfMainService.update(wfMainVo);

		Date now = new Date();
		PrpLWfNodeVo wfNodeVo = new PrpLWfNodeVo();
		wfNodeVo.setFlowId(flowId);
		wfNodeVo.setNodeCode(node.name());
		wfNodeVo.setHandlerStatus(HandlerStatus.CANCEL);
		wfNodeVo.setCancelTime(now);
		wfNodeVo.setCancelUser(submitVo.getTaskInUser());
		wfNodeService.update(wfNodeVo);

	}

	/**
	 * 初始化保存下一个流程主节点信息
	 * @param prpLRegistVo
	 * @param submitVo
	 * @param nowDate
	 * @param currentNodeCode
	 * @return
	 * @throws Exception
	 */
	protected void saveWfNextNodeVo(WfTaskSubmitVo submitVo,FlowNode nextNode) {
		if(nextNode!=FlowNode.END){
			PrpLWfNodeVo nextNodeVo = createWfNextNodeVo(submitVo,nextNode.getRootNode());
			wfNodeService.save(nextNodeVo);
		}
	}

	/**
	 * 设置下一个流程主节点信息
	 * @param prpLRegistVo
	 * @param submitVo
	 * @param nowDate
	 * @param currentNodeCode
	 * @return
	 * @throws Exception
	 */
	protected PrpLWfNodeVo createWfNextNodeVo(WfTaskSubmitVo submitVo,FlowNode node) {
		// 理赔流程主节点
		Date nowDate = new Date();
		PrpLWfNodeVo prpLWfNodeVo = new PrpLWfNodeVo();

		prpLWfNodeVo.setNodeCode(node.name());
		prpLWfNodeVo.setHandlerStatus(HandlerStatus.INIT);// 未处理

		prpLWfNodeVo.setFlowId(submitVo.getFlowId());
		prpLWfNodeVo.setNodeName(node.getName());
		prpLWfNodeVo.setNodeInTime(nowDate);
		prpLWfNodeVo.setUpdateTime(nowDate);
		prpLWfNodeVo.setUpdateUser(submitVo.getTaskInUser());
		return prpLWfNodeVo;
	}

	/**
	 * 根据 submitVo set PrpLWfTaskVo
	 * @param taskInVo
	 * @param submitVo
	 * @modified: ☆LiuPing(2016年1月11日 ): <br>
	 */
	protected void setTaskInVo(PrpLWfTaskVo taskInVo,WfTaskSubmitVo submitVo,FlowNode nextNode) {

		taskInVo.setNodeCode(nextNode.getRootNode().toString());
		taskInVo.setTaskName(nextNode.getName());
		taskInVo.setFlowId(submitVo.getFlowId());
		taskInVo.setSubNodeCode(nextNode.name());
		taskInVo.setUpperTaskId(submitVo.getFlowTaskId());
		taskInVo.setComCode(submitVo.getComCode());
		taskInVo.setHandlerStatus(HandlerStatus.INIT);// 未处理
		taskInVo.setWorkStatus(WorkStatus.INIT);// 未处理
		taskInVo.setTaskInTime(new Date());
		taskInVo.setTaskInUser(submitVo.getTaskInUser());
		taskInVo.setTaskInKey(submitVo.getTaskInKey());
		taskInVo.setTaskInNode(submitVo.getCurrentNode().name());
		
		if(StringUtils.isBlank(taskInVo.getAssignUser())){
			taskInVo.setAssignUser(submitVo.getAssignUser());
			taskInVo.setAssignCom(submitVo.getAssignCom());
		}
		if(StringUtils.isNotBlank(submitVo.getHandleruser())){
			taskInVo.setHandlerUser(submitVo.getHandleruser());
			taskInVo.setHandlerCom(submitVo.getComCode());
			taskInVo.setHandlerTime(submitVo.getHandlertime());
			taskInVo.setTaskOutUser(taskInVo.getHandlerUser());
			taskInVo.setTaskOutTime(taskInVo.getHandlerTime());
		}

		taskInVo.setAcceptOffTime(null);

	}

	public void setTaskBackInVo(PrpLWfTaskVo taskInVo,WfTaskSubmitVo submitVo,FlowNode nextNode,PrpLWfTaskVo taskBackVo) {
		setTaskInVo(taskInVo,submitVo,nextNode);
		taskInVo.setItemName(taskBackVo.getItemName());
		taskInVo.setAssignUser(taskBackVo.getHandlerUser());
		taskInVo.setAssignCom(taskBackVo.getHandlerCom());
		taskInVo.setHandlerIdKey(taskBackVo.getHandlerIdKey());
		taskInVo.setRegistNo(taskBackVo.getRegistNo());
		taskInVo.setWorkStatus(WorkStatus.BYBACK);// 被退回
	}

	/**
	 * 处理主节点的更新和保存
	 * @param currentNode
	 * @modified: ☆LiuPing(2016年1月20日 ): <br>
	 */
	protected void saveMainNode(FlowNode currentNode,FlowNode nextNode,WfTaskSubmitVo submitVo) {

		// 有上级取最上级的，取两次
		FlowNode endCurtNode = currentNode.getRootNode();

		FlowNode endNextNode = nextNode.getRootNode();

		if(endNextNode!=endCurtNode){// 主节点相同就不用处理主节点数据
			// 更新处理完成的主节点信息
			updateWfEndNodeVo(submitVo.getFlowId(),currentNode);
			// 初始化保存下一个主节点
			saveWfNextNodeVo(submitVo,endNextNode);
		}
	}
	
	//修改流程主表状态
	public void updatePrpLWfMainStatus(String flowId,String flowStatus){
		PrpLWfMainVo wfMainVo = new PrpLWfMainVo();
		wfMainVo.setFlowId(flowId);
		wfMainVo.setFlowStatus(flowStatus);
		wfMainService.update(wfMainVo);
	}
	
	public void updatePrplWFMainIsMobileCase(String flowId,String isMobileCase){
	    PrpLWfMainVo wfMainVo = new PrpLWfMainVo();
        wfMainVo.setFlowId(flowId);
        wfMainVo.setIsMobileCase(isMobileCase);
        wfMainService.update(wfMainVo);
	}
	
   public void rollBackFlow(PrpLScheduleTaskVo vo,String flowId){
       String registNo = vo.getRegistNo();
       //回滚工作流表
       //查勘
       List<PrpLWfTaskVo> checkTaskVoList = wfTaskHandleService.findWftaskByRegistNoAndNodeCode(registNo, FlowNode.Chk.toString());
       //流入时间降序排
       Collections.sort(checkTaskVoList, new Comparator<PrpLWfTaskVo>() {
       public int compare(PrpLWfTaskVo o1,PrpLWfTaskVo o2) {
               return o2.getTaskInTime().compareTo(o1.getTaskInTime());
           }
       });
       wfTaskHandleService.moveOutToIn(checkTaskVoList.get(0).getTaskId());
       //定损List<PrpLScheduleDefLossVo>
       for(PrpLScheduleDefLossVo scheduleDefLossVo : vo.getPrpLScheduleDefLosses()){
             List<PrpLWfTaskVo> wfTaskVoList1 = wfTaskHandleService.findInTask(registNo,scheduleDefLossVo.getId().toString(),FlowNode.DLCar.name());
             List<PrpLWfTaskVo> wfTaskVoList2 = wfTaskHandleService.findInTask(registNo,scheduleDefLossVo.getId().toString(),FlowNode.DLProp.name());
             //直接删除wfTaskVoList
             //删除旧的taskin
             if(wfTaskVoList1 != null && wfTaskVoList1.size() > 0 ){
                 databaseDao.deleteByPK(PrpLWfTaskIn.class,wfTaskVoList1.get(0).getTaskId());
             }
             if(wfTaskVoList2 != null && wfTaskVoList2.size() > 0 ){
                 databaseDao.deleteByPK(PrpLWfTaskIn.class,wfTaskVoList2.get(0).getTaskId());
             }
       }
       List<PrpLWfTaskVo> prpLWfTaskVoList = wfTaskHandleService.findWftaskByRegistNoAndNodeCode(registNo, FlowNode.Certi.toString());
       if(prpLWfTaskVoList != null && prpLWfTaskVoList.size() > 0 ){
           databaseDao.deleteByPK(PrpLWfTaskIn.class,prpLWfTaskVoList.get(0).getTaskId());
       }
    }
   public void rollBackNodeAndLWfMain(PrpLScheduleTaskVo vo,String flowId){
       boolean state = wfTaskHandleService.existTaskByNode(vo.getRegistNo(), FlowNode.DLoss.toString());            
       if(!state){
           //删除定损PrpLWfNode
           List<PrpLWfNodeVo> dLossNodeVoList = wfNodeService.findBydFlowIdAndNodeCode(flowId,FlowNode.DLoss.name());
           if(dLossNodeVoList != null && dLossNodeVoList.size() > 0 ){
               wfNodeService.delete(dLossNodeVoList.get(0).getId());
           }
       }
       //删除单证PrpLWfNode更新查勘状态
       List<PrpLWfNodeVo> certiNodeVoList = wfNodeService.findBydFlowIdAndNodeCode(flowId,FlowNode.Certi.name());
       if(certiNodeVoList != null && certiNodeVoList.size() > 0 ){
           wfNodeService.delete(certiNodeVoList.get(0).getId());
       }
       Date now = new Date();
       PrpLWfNodeVo schedNodeVo = new PrpLWfNodeVo();
       schedNodeVo.setFlowId(flowId);
       schedNodeVo.setNodeCode(FlowNode.Check.name());
       schedNodeVo.setHandlerStatus(HandlerStatus.INIT);
       schedNodeVo.setNodeOutTime(now);
       schedNodeVo.setUpdateTime(now);
       wfNodeService.update(schedNodeVo);
       //更新PrpLWfMain节点
       PrpLWfMainVo wfMainVo = new PrpLWfMainVo();
       wfMainVo.setFlowId(flowId);
       wfMainVo.setLastNode(FlowNode.Sched.name());
       wfMainService.update(wfMainVo);
   }
}
