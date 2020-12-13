package ins.sino.claimcar.flow.service.handle;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.utils.Beans;
import ins.sino.claimcar.CodeConstants.FlowStatus;
import ins.sino.claimcar.CodeConstants.HandlerStatus;
import ins.sino.claimcar.CodeConstants.WorkStatus;
import ins.sino.claimcar.claim.vo.PrpLClaimCancelVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.po.PrpLWfTaskIn;
import ins.sino.claimcar.flow.po.PrpLWfTaskOut;
import ins.sino.claimcar.flow.service.WfTaskQueryService;
import ins.sino.claimcar.flow.service.WfTaskService;
import ins.sino.claimcar.flow.util.TaskExtMapUtils;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.flow.vo.WfSimpleTaskVo;
import ins.sino.claimcar.flow.vo.WfTaskSubmitVo;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 调度工作流服务处理
 * @author ★LiuPing
 * @CreateTime 2016年1月11日
 */
@Service("wfSimpleTaskService")
public class WfSimpleTaskService extends WfBaseHandleService {
	
	@Autowired
	private DatabaseDao databaseDao;
	@Autowired
	private WfTaskService wfTaskService;
	@Autowired
	private WfTaskQueryService wfTaskQueryService;
	@Autowired
	private LossCarService lossCarService;

	/**
	 * 添加一个周边任务
	 * @param taskVo
	 * @param submitVo
	 * @return
	 * @modified: ☆LiuPing(2016年1月30日 ): <br>
	 */
	public PrpLWfTaskVo addSimpleTask(WfSimpleTaskVo taskVo,WfTaskSubmitVo submitVo) {
		FlowNode nextNode = submitVo.getNextNode();
		PrpLWfTaskVo taskInVo = new PrpLWfTaskVo();
		taskInVo.setHandlerIdKey(taskVo.getHandlerIdKey());
		taskInVo.setItemName(taskVo.getItemName());
		taskInVo.setRegistNo(taskVo.getRegistNo());
		taskInVo.setBussTag(taskVo.getBussTag());
		taskInVo.setShowInfoXML(taskVo.getShowInfoXml());
		
		if(submitVo.getNextNode()==FlowNode.DLCarAdd || submitVo.getNextNode()==FlowNode.DLCarMod){
			
			PrpLDlossCarMainVo lossCarMainVo = lossCarService.findLossCarMainById(Long.parseLong(submitVo.getTaskInKey()));
			TaskExtMapUtils extMapUtil = new TaskExtMapUtils();
			extMapUtil.addXmlMap(lossCarMainVo.getLossCarInfoVo(),"licenseNo","modelName");
			if(lossCarMainVo.getSerialNo() > 1){
				lossCarMainVo.setSerialNo(3);
			}
			extMapUtil.addXmlMap(lossCarMainVo, "serialNo","sumLossFee");
			//extMapUtil.getShowInfoXMLMap().put("serialNo",lossCarMainVo.getSerialNo().toString());
			
			taskInVo.setShowInfoXML(extMapUtil.getShowInfoXML());
			//案件紧急程度标示覆盖
			extMapUtil.addTagMap(lossCarMainVo,"mercyFlag");
			taskInVo.setBussTagMap(extMapUtil.getBussTagMap());
		}

		//人伤费用修改获取业务标识
		if(submitVo.getNextNode()==FlowNode.PLNext){
			PrpLWfTaskIn oldTaskInPo = databaseDao.findByPK(PrpLWfTaskIn.class,submitVo.getFlowTaskId());
			if(oldTaskInPo==null){
				PrpLWfTaskOut oldTaskoutPo = databaseDao.findByPK(PrpLWfTaskOut.class,submitVo.getFlowTaskId());
				taskInVo.setShowInfoXML(oldTaskoutPo.getShowInfoXML());
				taskInVo.setBussTag(oldTaskoutPo.getBussTag());
			}else{
				taskInVo.setShowInfoXML(oldTaskInPo.getShowInfoXML());
				taskInVo.setBussTag(oldTaskInPo.getBussTag());
			}
		}
		super.setTaskInVo(taskInVo,submitVo,nextNode);

		BigDecimal taskId = wfTaskService.saveTaskIn(taskInVo);
		taskInVo.setTaskId(taskId);
		return taskInVo;
	}

	/**
	 * 结束一个周边任务
	 * @param taskVo
	 * @param submitVo
	 */
	public PrpLWfTaskVo submitSimpleTask(WfSimpleTaskVo taskVo,WfTaskSubmitVo submitVo) {
		PrpLWfTaskVo taskInVo = new PrpLWfTaskVo();
		taskInVo.setHandlerIdKey(taskVo.getHandlerIdKey());
		taskInVo.setItemName(taskVo.getItemName());
		taskInVo.setRegistNo(taskVo.getRegistNo());
		taskInVo.setRiskCode(taskVo.getRiskCode());
		if(taskVo.getClaimNo()!=null||taskVo.getClaimNo()!=""){
			taskInVo.setClaimNo(taskVo.getClaimNo());
		}
		super.setTaskInVo(taskInVo,submitVo,submitVo.getNextNode());
		if(StringUtils.isNotBlank(submitVo.getFlowStatus())){//周边审核节点 
			super.updatePrpLWfMainStatus(submitVo.getFlowId(), submitVo.getFlowStatus());
		}
		wfTaskService.submitTask(submitVo.getFlowTaskId(),taskInVo);
		return taskInVo;
	}

	/**
	 * 添加一个周边任务
	 * @param taskVo
	 * @param submitVo
	 * @return
	 */
	public PrpLWfTaskVo addCancelTask(WfSimpleTaskVo taskVo,WfTaskSubmitVo submitVo,PrpLClaimCancelVo pClaimCancelVo) {
		FlowNode nextNode = submitVo.getNextNode();
		PrpLWfTaskVo taskInVo = new PrpLWfTaskVo();
		taskInVo.setHandlerIdKey(taskVo.getHandlerIdKey());
		taskInVo.setItemName(taskVo.getItemName());
		taskInVo.setRegistNo(taskVo.getRegistNo());
		taskInVo.setBussTag(taskVo.getBussTag());

		//添加立案号

		TaskExtMapUtils extMapUtil = new TaskExtMapUtils();
		System.out.println("立案注销时间："+pClaimCancelVo.getClaimCancelTime());
		//extMapUtil.addTagMap(compVo,"caseType","recoveryFlag","lawsuitFlag","allowFlag");

		extMapUtil.addXmlMap(pClaimCancelVo,"claimCancelTime","applyReason","claimTime","recoverReason","claimRecoverTime");
		// 处理task信息
		//PrpLWfTaskVo taskInVo = new PrpLWfTaskVo();
		//taskInVo.setShowInfoXML(extMapUtil.getShowInfoXML());
		
		
		
		taskInVo.setShowInfoXML(extMapUtil.getShowInfoXML());
		taskInVo.setClaimNo(pClaimCancelVo.getClaimNo());
		super.setTaskInVo(taskInVo,submitVo,nextNode);
		taskInVo.setYwTaskType(submitVo.getCurrentNode().toString());

		BigDecimal taskId = wfTaskService.saveTaskIn(taskInVo);
		System.out.println("wfTaskServicewfTaskServicetaskId"+taskId);
		taskInVo.setTaskId(taskId);
		return taskInVo;
	}
	
	
	/** 注销拒赔总公司退回分公司任务回退处理 */
	public void backCancelHandle(WfTaskSubmitVo submitVo,PrpLClaimCancelVo pClaimCancelVo) {
		String flowId = submitVo.getFlowId();
		FlowNode currentNode = submitVo.getCurrentNode();
		FlowNode nextNode = submitVo.getNextNode();
		String handleIdKey = submitVo.getHandleIdKey();
		// 获得回退时上次处理已完成的节点
		PrpLWfTaskVo taskBackVo = wfTaskService.findBackOutVo(handleIdKey,submitVo.getFlowId(),nextNode);
		
		PrpLWfTaskVo taskInVo = new PrpLWfTaskVo();
		super.setTaskBackInVo(taskInVo,submitVo,nextNode,taskBackVo);
		taskInVo.setRemark(currentNode.name()+" Back To "+nextNode.name());
		taskInVo.setClaimNo(pClaimCancelVo.getClaimNo());
		TaskExtMapUtils extMapUtil = new TaskExtMapUtils();
		extMapUtil.addXmlMap(pClaimCancelVo,"claimCancelTime","applyReason","claimTime","recoverReason","claimRecoverTime");
		taskInVo.setShowInfoXML(extMapUtil.getShowInfoXML());
		System.out.println("==getFlowId=="+taskBackVo.getFlowId());
		//wfTaskService.submitTask(submitVo.getFlowTaskId(),taskInVo);
		this.submitTask(submitVo.getFlowTaskId(),taskInVo);
	}
	//设置分公司退回的状态
	public PrpLWfTaskVo backCancel(WfSimpleTaskVo taskVo,WfTaskSubmitVo submitVo) {
		PrpLWfTaskVo taskInVo = new PrpLWfTaskVo();
		taskInVo.setHandlerIdKey(taskVo.getHandlerIdKey());
		taskInVo.setItemName(taskVo.getItemName());
		taskInVo.setRegistNo(taskVo.getRegistNo());

		super.setTaskInVo(taskInVo,submitVo,submitVo.getNextNode());
		taskInVo.setHandlerStatus(HandlerStatus.INIT);// 未处理
		taskInVo.setWorkStatus(WorkStatus.BYBACK);// 已经退回
		wfTaskService.backCancel(submitVo.getFlowTaskId(),taskInVo);
		return taskInVo;
	}
	
	public void submitTask(BigDecimal flowTaskId,PrpLWfTaskVo... taskInVos) {
		// 找到旧的taskin数据
		if(flowTaskId==null){
			throw new IllegalArgumentException("flowTaskId 不能为空");
		}
			
		PrpLWfTaskOut outPo = moveInToOut(flowTaskId,taskInVos[0]);
		String bussTag = outPo.getBussTag();
		String taskInNode = outPo.getSubNodeCode();
		String nodeCode = outPo.getNodeCode();

		// 定损提交到核价、核价提交到核损时、核损提交时，核价、核损的taskinNode,还是要保存PrpLWfTaskOut的taskinNode；
		if(FlowNode.VPrice.equals(nodeCode) || FlowNode.VLoss.equals(nodeCode)){
			taskInNode = outPo.getTaskInNode();
		}

		// 保存新的taskin
		for(PrpLWfTaskVo taskInVo:taskInVos){
			if( !FlowNode.END.equals(taskInVo.getNodeCode())){
				bussTag = TaskExtMapUtils.joinBussTag(bussTag,taskInVo.getBussTagMap());
				/*PrpLWfTaskIn oldtaskIn = queryTaskInByHandKey(taskInVo.getFlowId(),taskInVo.getHandlerIdKey(),taskInVo.getSubNodeCode());
				if(oldtaskIn!=null){
					throw new IllegalStateException("此任务已提交,不能再次提交!");
				}*/
				PrpLWfTaskIn taskInPo = new PrpLWfTaskIn();
				Beans.copy().from(taskInVo).excludeNull().to(taskInPo);
				taskInPo.setBussTag(bussTag);
				taskInPo.setTaskInNode(taskInNode);
				taskInPo.setUpperTaskId(flowTaskId);
				databaseDao.save(PrpLWfTaskIn.class,taskInPo);
				
				taskInVo.setTaskId(taskInPo.getTaskId());
			}
		}

	}
	/** 将in数据转移到out,这里是根据下一个节点处理 */
	public PrpLWfTaskOut moveInToOut(BigDecimal flowTaskId,PrpLWfTaskVo taskInVo) {
		PrpLWfTaskIn oldTaskInPo = databaseDao.findByPK(PrpLWfTaskIn.class,flowTaskId);
		PrpLWfTaskOut taskOutPo = null;
		if(oldTaskInPo!=null){// 等于null的时候说明已经转移了,要从out表找到他
			// 复制保存到taskout，并更改任务完成状态
			taskOutPo = Beans.copyDepth().from(oldTaskInPo).to(PrpLWfTaskOut.class);
			taskOutPo.setHandlerStatus(HandlerStatus.END);
			taskOutPo.setWorkStatus(WorkStatus.END);
			if(taskInVo.getWorkStatus().equals(WorkStatus.BYBACK)){
				taskOutPo.setWorkStatus(WorkStatus.BACK);
			}else if(taskInVo.getWorkStatus().equals(WorkStatus.CANCEL)){
				taskOutPo.setWorkStatus(WorkStatus.CANCEL);
			}
			taskOutPo.setTaskOutNode(taskInVo.getSubNodeCode());
			taskOutPo.setTaskOutKey(taskInVo.getTaskInKey());
			taskOutPo.setTaskOutTime(taskInVo.getTaskInTime());
			taskOutPo.setTaskOutUser(taskInVo.getTaskInUser());
			taskOutPo.setShowInfoXML(taskInVo.getShowInfoXML());
			databaseDao.save(PrpLWfTaskOut.class,taskOutPo);
			// 删除旧的taskin
			databaseDao.deleteByPK(PrpLWfTaskIn.class,flowTaskId);
		} else {
			taskOutPo=databaseDao.findByPK(PrpLWfTaskOut.class,flowTaskId);
		}
		
		return taskOutPo;
	}
	/**
	 * 结束立案注销发起任务
	 * @param taskVo
	 * @param submitVo
	 */
	public PrpLWfTaskVo submitCancelTask(WfSimpleTaskVo taskVo,WfTaskSubmitVo submitVo,PrpLClaimCancelVo pClaimCancelVo) {
		PrpLWfTaskVo taskInVo = new PrpLWfTaskVo();
		taskInVo.setHandlerIdKey(taskVo.getHandlerIdKey());
		taskInVo.setItemName(taskVo.getItemName());
		taskInVo.setRegistNo(taskVo.getRegistNo());
		TaskExtMapUtils extMapUtil = new TaskExtMapUtils();

		extMapUtil.addXmlMap(pClaimCancelVo,"claimCancelTime","applyReason","claimTime","recoverReason","claimRecoverTime");
		// 处理task信息
		taskInVo.setShowInfoXML(extMapUtil.getShowInfoXML());
		taskInVo.setClaimNo(pClaimCancelVo.getClaimNo());
		super.setTaskInVo(taskInVo,submitVo,submitVo.getNextNode());
		wfTaskService.submitTaskForCancel(submitVo.getFlowTaskId(),taskInVo);
		return taskInVo;
	}
}
