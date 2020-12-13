package ins.sino.claimcar.flow.service.handle;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.sino.claimcar.CodeConstants.HandlerStatus;
import ins.sino.claimcar.CodeConstants.WorkStatus;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.constant.Risk;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.po.PrpLWfTaskIn;
import ins.sino.claimcar.flow.po.PrpLWfTaskOut;
import ins.sino.claimcar.flow.service.WfTaskQueryService;
import ins.sino.claimcar.flow.service.WfTaskService;
import ins.sino.claimcar.flow.util.TaskExtMapUtils;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.flow.vo.WfTaskSubmitVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 立案工作流服务处理
 * @author ★LiuPing
 * @CreateTime 2016年1月11日
 */
@Service("wfClaimService")
public class WfClaimService extends WfBaseHandleService {
	
	@Autowired
	private WfTaskService wfTaskService;
	@Autowired
	private WfTaskQueryService wfTaskQueryService;
	@Autowired
	private DatabaseDao databaseDao;
	
	/**
	 * 报案提交时调用根据报案信息，生成未处理的立案任务,如果交强商业同时有，会生成两个立案任务。
	 * @param lossCarMainVo
	 * @param submitVo
	 * @return
	 * @modified: ☆LiuPing(2016年3月26日 ): <br>
	 */
	public void createClaimByRegist(PrpLRegistVo registVo,WfTaskSubmitVo submitVo,PrpLWfTaskVo registTaskVo) {

		submitVo.setCurrentNode(FlowNode.Regis);

		// 需要生成交强险CI和商业险BI两个立案节点
		String biPolicyNo = null;
		String ciPolicyNo = null;

		String registPlyNo = registVo.getRegistNo();// 报案主表的的保单号，PS 报案时已经处理，如果交强商业同时报案，报案主表存的是商业险保单号，子表是交强险
		String registRisk = registVo.getRiskCode();
		if(registRisk.equals("1101")){
			ciPolicyNo = registPlyNo;
		}else{
			biPolicyNo = registPlyNo;
			ciPolicyNo = registVo.getPrpLRegistExt().getPolicyNoLink();
		}

		if(StringUtils.isNotBlank(biPolicyNo)){
			PrpLWfTaskVo taskInVo = registTaskVo;
			super.setTaskInVo(taskInVo,submitVo,FlowNode.ClaimBI);
			taskInVo.setTaskId(null);
			taskInVo.setHandlerIdKey(biPolicyNo);
			taskInVo.setRiskCode(registRisk);
			taskInVo.setWorkStatus(WorkStatus.VIRT);
			wfTaskService.saveTaskIn(taskInVo);
			logger.info("生成未处理的商业立案任务=============================end,biPolicyNo="+biPolicyNo+"registNo="+registVo.getRegistNo());
		}

		if(StringUtils.isNotBlank(ciPolicyNo)){
			PrpLWfTaskVo taskInVo = registTaskVo;
			super.setTaskInVo(taskInVo,submitVo,FlowNode.ClaimCI);
			taskInVo.setTaskId(null);
			taskInVo.setHandlerIdKey(ciPolicyNo);
			taskInVo.setRiskCode("1101");
			taskInVo.setWorkStatus(WorkStatus.VIRT);
			wfTaskService.saveTaskIn(taskInVo);
			logger.info("生成未处理的交强立案任务=============================end,ciPolicyNo="+ciPolicyNo+"registNo="+registVo.getRegistNo());
		}

	}


	/**
	 * 查勘节点提交时更新立案状态
	 * @param flowId
	 * @param bussTagMap
	 * @modified: ☆LiuPing(2016年4月9日 ): <br>
	 */
	public void updateClaimByCheck(String flowId,Map<String,String> bussTagMap) {
		List<PrpLWfTaskVo> biTaskVos = wfTaskService.queryTask(flowId,FlowNode.ClaimBI);
		if(biTaskVos!=null){
			for(PrpLWfTaskVo taskVo:biTaskVos){
				if( !WorkStatus.VIRT.equals(taskVo.getWorkStatus())) continue;// 仅虚拟状态的才能update
				String bussTag = TaskExtMapUtils.joinBussTag(taskVo.getBussTag(),bussTagMap);
				taskVo.setBussTag(bussTag);
				taskVo.setHandlerStatus(HandlerStatus.DOING);
				taskVo.setWorkStatus(WorkStatus.DOING);
				wfTaskService.updateTaskIn(taskVo);
			}
		}

		List<PrpLWfTaskVo> ciTaskVos = wfTaskService.queryTask(flowId,FlowNode.ClaimCI);
		if(ciTaskVos!=null){
			for(PrpLWfTaskVo taskVo:ciTaskVos){
				if( !WorkStatus.VIRT.equals(taskVo.getWorkStatus())) continue;// 仅虚拟状态的才能update
				String bussTag = TaskExtMapUtils.joinBussTag(taskVo.getBussTag(),bussTagMap);
				taskVo.setBussTag(bussTag);
				taskVo.setHandlerStatus(HandlerStatus.DOING);
				taskVo.setWorkStatus(WorkStatus.DOING);
				wfTaskService.updateTaskIn(taskVo);
			}
		}
	}

	/**
	 * 立案数据保存时提交立案节点
	 * @param flowId
	 * @param claimVo
	 * @return
	 * @modified: ☆LiuPing(2016年4月9日 ): <br>
	 */
	public PrpLWfTaskVo submitClaimHandle(String flowId,PrpLClaimVo claimVo) {
		FlowNode subNode = FlowNode.ClaimBI;
		if(Risk.DQZ.equals(claimVo.getRiskCode())){
			subNode = FlowNode.ClaimCI;
		}
		List<PrpLWfTaskVo> taskVos = null;
		QueryRule queryRule=QueryRule.getInstance();
		queryRule.addEqual("flowId",flowId);
		queryRule.addEqual("subNodeCode",subNode.name());
		List<PrpLWfTaskIn> prpLWfTaskIns = databaseDao.findAll(PrpLWfTaskIn.class,queryRule);
		if(prpLWfTaskIns!=null&& !prpLWfTaskIns.isEmpty()){
			taskVos = Beans.copyDepth().from(prpLWfTaskIns).toList(PrpLWfTaskVo.class);
		}
		if(taskVos!=null&&taskVos.size()>0){
			for(PrpLWfTaskVo taskVo:taskVos){
				TaskExtMapUtils extMapUtil = new TaskExtMapUtils();
				extMapUtil.addTagMap(claimVo,"mercyFlag","caseFlag","claimType");
				String bussTag = TaskExtMapUtils.joinBussTag(taskVo.getBussTag(),extMapUtil.getBussTagMap());
				taskVo.setBussTag(bussTag);
				taskVo.setHandlerStatus(HandlerStatus.END);
				taskVo.setWorkStatus(WorkStatus.END);
				taskVo.setHandlerIdKey(claimVo.getClaimNo());
				taskVo.setTaskOutKey(claimVo.getClaimNo());
				taskVo.setTaskOutNode(FlowNode.END.name());
				taskVo.setTaskOutTime(new Date());
				taskVo.setClaimNo(claimVo.getClaimNo());
				taskVo.setTaskOutUser(claimVo.getCreateUser());
				wfTaskService.moveTaskToOut(taskVo);
				return taskVo;
			}
		}
		return null;
	}
	
	//查询taskid
	public BigDecimal findTaskId(String claimNo,String subNodeCode){
		QueryRule queryRule=QueryRule.getInstance();
		queryRule.addEqual("claimNo", claimNo);
		queryRule.addEqual("subNodeCode", subNodeCode);
		List<PrpLWfTaskIn> prpLWfTaskInList= databaseDao.findAll(PrpLWfTaskIn.class,queryRule);
		
		if (prpLWfTaskInList.size()>0) {
			return prpLWfTaskInList.get(0).getTaskId();
			
		} else {
			List<PrpLWfTaskOut> prpLWfTaskOutList= databaseDao.findAll(PrpLWfTaskOut.class,queryRule);
			if (prpLWfTaskOutList.size()>0) {
				return prpLWfTaskOutList.get(0).getTaskId();
				
			} else{
				return null;
			}
		}
		
	
	}
	
	
	
	//生成一个立案，强制立案
	public void createClaimByPolicyNo(PrpLRegistVo registVo,WfTaskSubmitVo submitVo,PrpLWfTaskVo registTaskVo) {

		submitVo.setCurrentNode(FlowNode.Regis);
		String ciPolicyNo = registVo.getPolicyNo();
		if(("11").equals(registVo.getPolicyNo().substring(11, 13))){
			PrpLWfTaskVo taskInVo = registTaskVo;
			super.setTaskInVo(taskInVo,submitVo,FlowNode.ClaimCI);
			taskInVo.setTaskId(null);
			taskInVo.setHandlerIdKey(ciPolicyNo);
			taskInVo.setRiskCode("1101");
			taskInVo.setWorkStatus(WorkStatus.VIRT);
			wfTaskService.saveTaskIn(taskInVo);
		}else{
			PrpLWfTaskVo taskInVo = registTaskVo;
			super.setTaskInVo(taskInVo,submitVo,FlowNode.ClaimBI);
			taskInVo.setTaskId(null);
			taskInVo.setHandlerIdKey(ciPolicyNo);
			taskInVo.setRiskCode(registVo.getRiskCode());
			taskInVo.setWorkStatus(WorkStatus.VIRT);
			wfTaskService.saveTaskIn(taskInVo);
		}
	}
	
	/**
	 * 立案注销
	 * @param userCode
	 * @param flowTaskId
	 */
	public void cancelTaskClaim(WfTaskSubmitVo submitVo,String userCode,BigDecimal... flowTaskId){
		wfTaskService.cancelTaskClaim(userCode,flowTaskId);
		if(StringUtils.isNotBlank(submitVo.getFlowStatus())){
			super.updatePrpLWfMainStatus(submitVo.getFlowId(), submitVo.getFlowStatus());
		}
	}
	
	/**
	 * 立案注销恢复
	 * @param userCode
	 * @param flowTaskId
	 */
	public void cancelTaskClaimRecover(WfTaskSubmitVo submitVo,String userCode,BigDecimal... flowTaskId){
		wfTaskService.cancelTaskRecover(userCode,flowTaskId);
		super.updatePrpLWfMainStatus(submitVo.getFlowId(), submitVo.getFlowStatus());
	}
}
