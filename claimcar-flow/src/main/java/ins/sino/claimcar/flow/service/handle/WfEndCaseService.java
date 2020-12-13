package ins.sino.claimcar.flow.service.handle;

import java.math.BigDecimal;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.utils.Beans;
import ins.sino.claimcar.CodeConstants.FlowStatus;
import ins.sino.claimcar.CodeConstants.HandlerStatus;
import ins.sino.claimcar.CodeConstants.WorkStatus;
import ins.sino.claimcar.endcase.vo.PrpLEndCaseVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.po.PrpLWfTaskIn_NoGenId;
import ins.sino.claimcar.flow.po.PrpLWfTaskOut;
import ins.sino.claimcar.flow.service.WfTaskQueryService;
import ins.sino.claimcar.flow.service.WfTaskService;
import ins.sino.claimcar.flow.util.TaskExtMapUtils;
import ins.sino.claimcar.flow.vo.PrpLWfMainVo;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.flow.vo.WfSimpleTaskVo;
import ins.sino.claimcar.flow.vo.WfTaskSubmitVo;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 核赔工作流处理
 * @author ★LiuPing
 * @CreateTime 2016年1月11日
 */
@Service("wfEndCaseService")
public class WfEndCaseService extends WfBaseHandleService {
	
	@Autowired
	private WfTaskService wfTaskService;
	@Autowired
	private WfTaskQueryService wfTaskQueryService;
	@Autowired
	private DatabaseDao databaseDao;

	
	/**
	 * 结案
	 * @param compVo
	 * @param flowId
	 * @return
	 * @modified: ☆LiuPing(2016年4月9日 ): <br>
	 */
	public PrpLWfTaskVo submitEndCaseHandle(PrpLEndCaseVo endCaseVo,WfTaskSubmitVo submitVo) {
		String flowId = submitVo.getFlowId();

		// 更新处理完成的主节点信息
		super.updateWfEndNodeVo(flowId,FlowNode.EndCas);
		
		if(FlowStatus.END.equals(submitVo.getFlowStatus())){//都结案 回写流程主表
			super.updatePrpLWfMainStatus(flowId,FlowStatus.END);
		}

		// 更新taskQuery表信息
		/*PrpLWfTaskQueryVo taskQueryVo = new PrpLWfTaskQueryVo();
		taskQueryVo.setFlowId(flowId);
		taskQueryVo.setMercyFlag(compVo.getMercyFlag());// 案件紧急程度
		taskQueryVo.setReconcileFlag(checkVo.getReconcileFlag());// 是否现场调解
		wfTaskQueryService.update(taskQueryVo);
		*/
		// 处理tag和showXml
		TaskExtMapUtils extMapUtil = new TaskExtMapUtils();
		extMapUtil.addTagMap(endCaseVo,"isAutoEndCase","endcaseType");
		extMapUtil.addXmlMap(endCaseVo,"endCaseNo","regressNo");
		// 处理task信息
		PrpLWfTaskVo taskInVo = new PrpLWfTaskVo();
		super.setTaskInVo(taskInVo,submitVo,FlowNode.END);
		//taskInVo.setHandlerIdKey(compVo.getCompensateNo());
		taskInVo.setAcceptTime(endCaseVo.getEndCaseDate());//结案时间
		taskInVo.setHandlerIdKey(submitVo.getHandleIdKey());
		taskInVo.setCompensateNo(submitVo.getHandleIdKey());
		taskInVo.setRegistNo(endCaseVo.getRegistNo());
		taskInVo.setClaimNo(endCaseVo.getClaimNo());
		taskInVo.setBussTagMap(extMapUtil.getBussTagMap());
		taskInVo.setShowInfoXML(extMapUtil.getShowInfoXML());

		wfTaskService.submitTask(submitVo.getFlowTaskId(),taskInVo);
		return taskInVo;

	}
	
	public PrpLWfTaskVo createReOpenAppTask(WfSimpleTaskVo taskVo,WfTaskSubmitVo submitVo) {

		String flowId = submitVo.getFlowId();
		
		super.saveWfNextNodeVo(submitVo, FlowNode.ReOpenApp);
		
//		List<PrpLWfTaskVo> TaskVos = wfTaskService.queryTask(flowId,submitVo.getCurrentNode());
		PrpLWfTaskVo taskInVo = wfTaskService.queryTask(submitVo.getFlowTaskId().doubleValue());
		submitVo.setCurrentNode(FlowNode.EndCas);
		super.setTaskInVo(taskInVo,submitVo,FlowNode.ReOpenApp);
		
		taskInVo.setTaskId(null);
		taskInVo.setHandlerIdKey(taskVo.getHandlerIdKey());
		taskInVo.setCompensateNo(submitVo.getHandleIdKey());
		taskInVo.setRegistNo(taskVo.getRegistNo());
		taskInVo.setRiskCode(taskVo.getRiskCode());
		taskInVo.setClaimNo(taskVo.getClaimNo());
		BigDecimal taskId = wfTaskService.saveTaskIn(taskInVo);
		taskInVo.setTaskId(taskId);
		return taskInVo;
	}

	/**
	 * 
	 * @param taskVo
	 * @param submitVo
	 * @return
	 * @modified:
	 * ☆XMSH(2016年5月31日 上午11:57:02): <br>
	 */
	public PrpLWfTaskVo submitReOpenAppHandle(WfSimpleTaskVo taskVo,WfTaskSubmitVo submitVo) {
		// TODO Auto-generated method stub
		String flowId = submitVo.getFlowId();
		
		// 更新处理完成的主节点信息
		super.updateWfEndNodeVo(flowId,FlowNode.ReOpenApp);
		
		super.saveWfNextNodeVo(submitVo, submitVo.getNextNode());
		
		// 处理task信息
		PrpLWfTaskVo taskInVo = new PrpLWfTaskVo();
		super.setTaskInVo(taskInVo,submitVo,submitVo.getNextNode());
//		taskInVo.setHandlerIdKey(taskVo.getHandlerIdKey());
		taskInVo.setHandlerIdKey(submitVo.getHandleIdKey());
		taskInVo.setRegistNo(taskVo.getRegistNo());
		taskInVo.setRiskCode(taskVo.getRiskCode());
		taskInVo.setClaimNo(taskVo.getClaimNo());
//		taskInVo.setBussTagMap(submitVo.getBussTagMap());
//		taskInVo.setShowInfoXML(extMapUtil.getShowInfoXML());

		wfTaskService.submitTask(submitVo.getFlowTaskId(),taskInVo);
		return taskInVo;
	}

	/**
	 * 
	 * @param taskVo
	 * @param submitVo
	 * @return
	 * @modified:
	 * ☆XMSH(2016年5月31日 下午12:57:34): <br>
	 */
	public PrpLWfTaskVo submitReOpenHandle(BigDecimal flowTaskId,WfSimpleTaskVo taskVo,WfTaskSubmitVo submitVo) {
		// TODO Auto-generated method stub
		String flowId = submitVo.getFlowId();

		// 更新处理完成的主节点信息
		super.updateWfEndNodeVo(flowId,FlowNode.ReOpenVrf);
		
		super.updatePrpLWfMainStatus(flowId, FlowStatus.NORMAL);
		
		// 处理task信息
		PrpLWfTaskVo taskInVo = new PrpLWfTaskVo();
		super.setTaskInVo(taskInVo,submitVo,FlowNode.END);
		//taskInVo.setHandlerIdKey(compVo.getCompensateNo());
		taskInVo.setHandlerIdKey(submitVo.getHandleIdKey());
		taskInVo.setRegistNo(taskVo.getRegistNo());
		taskInVo.setClaimNo(taskVo.getClaimNo());
		taskInVo.setRiskCode(taskVo.getRiskCode());
//		taskInVo.setBussTagMap(extMapUtil.getBussTagMap());
//		taskInVo.setShowInfoXML(extMapUtil.getShowInfoXML());

		wfTaskService.submitTask(submitVo.getFlowTaskId(),taskInVo);
		moveOutToIn(flowTaskId);//理算工作流id
		return taskInVo;

	}
	
	/**
	 * 重开赔案将理算工作流节点从out表的数据转移到in表
	 * @param flowTaskId
	 * @param taskInVo
	 * @return
	 * @modified:
	 * ☆XMSH(2016年5月17日 上午9:25:21): <br>
	 */
	public void moveOutToIn(BigDecimal flowTaskId) {
		PrpLWfTaskOut oldTaskOutPo = databaseDao.findByPK(PrpLWfTaskOut.class,flowTaskId);
		PrpLWfTaskIn_NoGenId taskInPo = null;
		if(oldTaskOutPo!=null){// 等于null的时候说明已经转移了,要从out表找到他
			// 复制保存到taskin，
			taskInPo = Beans.copyDepth().from(oldTaskOutPo).to(PrpLWfTaskIn_NoGenId.class);
			taskInPo.setHandlerStatus(HandlerStatus.DOING);
			taskInPo.setWorkStatus(WorkStatus.DOING);
			taskInPo.setTaskId(oldTaskOutPo.getTaskId());
			if("AUTO".equals(oldTaskOutPo.getHandlerUser())){
				taskInPo.setHandlerUser("");
			}
			databaseDao.save(PrpLWfTaskIn_NoGenId.class,taskInPo);
			// 删除旧的taskout
			databaseDao.deleteByPK(PrpLWfTaskOut.class,flowTaskId);
		} 
	}

}
