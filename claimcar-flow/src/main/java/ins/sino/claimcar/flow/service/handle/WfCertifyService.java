package ins.sino.claimcar.flow.service.handle;

import ins.sino.claimcar.CodeConstants.HandlerStatus;
import ins.sino.claimcar.CodeConstants.WorkStatus;
import ins.sino.claimcar.certify.service.CertifyPubService;
import ins.sino.claimcar.certify.vo.PrpLCertifyMainVo;
import ins.sino.claimcar.check.vo.PrpLCheckVo;
import ins.sino.claimcar.claim.service.ClaimTaskService;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.po.PrpLWfTaskIn;
import ins.sino.claimcar.flow.service.AssignService;
import ins.sino.claimcar.flow.service.WfTaskService;
import ins.sino.claimcar.flow.util.TaskExtMapUtils;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.flow.vo.WfTaskSubmitVo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("wfCertifyService")
public class WfCertifyService extends WfBaseHandleService{

	@Autowired
	private WfTaskService wfTaskService;
	
	@Autowired
	private CertifyPubService certifyPubService;
	
	@Autowired
	private ClaimTaskService claimTaskService;
	
	@Autowired
	private AssignService assignService;
	
	/*public PrpLWfTaskVo createCertifyNode(PrpLCheckVo checkVo,WfTaskSubmitVo submitVo) {
		// String flowId = submitVo.getFlowId();
		// 更新处理完成的主节点信息
		// super.updateWfEndNodeVo(flowId,FlowNode.Check);
		// 初始化保存下一个主节点
//		super.saveWfNextNodeVo(submitVo,FlowNode.Certi);
//
		PrpLWfTaskVo taskInVo = new PrpLWfTaskVo();
//		taskInVo.setHandlerIdKey(checkVo.getId().toString());
//		taskInVo.setItemName(null);
//		taskInVo.setRegistNo(checkVo.getRegistNo());
//		super.setTaskInVo(taskInVo,submitVo,FlowNode.Certi);
//		taskInVo.setAssignUser(null);
//		taskInVo.setAssignCom(null);
//		// 案件紧急程度标示覆盖
//		Map<String,String> bussTagMap = new HashMap<String,String>();
//		bussTagMap.put("mercyFlag",checkVo.getMercyFlag());
//		taskInVo.setBussTagMap(bussTagMap);
//		wfTaskService.submitTask(submitVo.getFlowTaskId(),taskInVo);
		submitCertifyMain(checkVo,submitVo);
		return taskInVo;
	}*/

	/**
	 * 单证提交，创建理算节点
	 * @param certifyVo
	 * @param submitVo
	 * @return
	 * @modified: ☆LiuPing(2016年4月9日 ): <br>
	 */
	public List<PrpLWfTaskVo> submitCertifyHandle(PrpLCertifyMainVo certifyVo,WfTaskSubmitVo submitVo) {
		String flowId = submitVo.getFlowId();
		String registNo = certifyVo.getRegistNo();

		// 更新处理完成的主节点信息
		super.updateWfEndNodeVo(flowId,FlowNode.Certi);
		// 初始化保存下一个主节点
		super.saveWfNextNodeVo(submitVo,FlowNode.Compe);

		// 更新taskQuery表信息
		/*PrpLWfTaskQueryVo taskQueryVo = new PrpLWfTaskQueryVo();
				taskQueryVo.setFlowId(flowId);
				..PrpLWfTaskQuery需要更新的信息
				wfTaskQueryService.update(taskQueryVo);
		*/
		List<PrpLWfTaskVo> compTaskVos = new ArrayList<PrpLWfTaskVo>();
		// 判断是否可以产生理算节点，（在单证保存时判断了）
		
		// 交强险理算，
		List<PrpLWfTaskVo> ciTaskVos = wfTaskService.queryTask(flowId,FlowNode.ClaimCI);
		if(ciTaskVos!=null&&ciTaskVos.size()>0){
			PrpLWfTaskVo claimTaskVo = ciTaskVos.get(0);
			String claimNo = claimTaskVo.getClaimNo();
			if(HandlerStatus.END.equals(claimTaskVo.getHandlerStatus()) && WorkStatus.END.equals(claimTaskVo.getWorkStatus())){
				PrpLClaimVo prpLClaimVo = claimTaskService.findClaimVoByClaimNo(claimNo);
				if(("1".equals(prpLClaimVo.getValidFlag())) 
						&& (StringUtils.isBlank(prpLClaimVo.getEndCaserCode()) 
							&& prpLClaimVo.getEndCaseTime() == null 
							&& StringUtils.isBlank(prpLClaimVo.getCaseNo()))){//注销拒赔的立案和存在结案的立案不发起理算
					PrpLWfTaskVo taskInVo = new PrpLWfTaskVo();
					taskInVo.setHandlerIdKey(claimTaskVo.getHandlerIdKey());
					taskInVo.setItemName(claimTaskVo.getItemName());
					taskInVo.setRegistNo(registNo);
					taskInVo.setBussTag(claimTaskVo.getBussTag());
					taskInVo.setShowInfoXML(claimTaskVo.getShowInfoXML());
					//添加调查标识
					TaskExtMapUtils extMapUtil = new TaskExtMapUtils();
					extMapUtil.addTagMap(certifyVo,"surveyFlag");
					extMapUtil.addTagMap(certifyVo,"isSYFraud");
					extMapUtil.addTagMap(certifyVo,"isJQFraud");
					taskInVo.setBussTagMap(extMapUtil.getBussTagMap());
					/*SysUserVo sysUserVo = assignService.execute(FlowNode.Compe, submitVo.getComCode());//轮询
					taskInVo.setAssignUser(sysUserVo.getUserCode());
					taskInVo.setAssignCom(sysUserVo.getComCode());*/
					super.setTaskInVo(taskInVo,submitVo,FlowNode.CompeCI);
					PrpLWfTaskIn oldtaskIn = wfTaskService.queryTaskInByHandKey(taskInVo.getFlowId(),taskInVo.getHandlerIdKey(),taskInVo.getSubNodeCode());
					if(oldtaskIn == null){//已经存在未处理或者正在处理的理算任务不再发起
						compTaskVos.add(taskInVo);
					}
				}
			}
		}
		// 商业险理算，先找到商业险立案信息
		List<PrpLWfTaskVo> biTaskVos = wfTaskService.queryTask(flowId,FlowNode.ClaimBI);
		if(biTaskVos!=null&&biTaskVos.size()>0){
			PrpLWfTaskVo claimTaskVo = biTaskVos.get(0);
			String claimNo = claimTaskVo.getClaimNo();
			if(HandlerStatus.END.equals(claimTaskVo.getHandlerStatus()) && WorkStatus.END.equals(claimTaskVo.getWorkStatus())){
				PrpLClaimVo prpLClaimVo = claimTaskService.findClaimVoByClaimNo(claimNo);
				if(("1".equals(prpLClaimVo.getValidFlag())) 
						&& (StringUtils.isBlank(prpLClaimVo.getEndCaserCode()) 
							&& prpLClaimVo.getEndCaseTime() == null 
							&& StringUtils.isBlank(prpLClaimVo.getCaseNo()))){//注销拒赔的立案和存在结案的立案不发起理算
					PrpLWfTaskVo taskInVo = new PrpLWfTaskVo();
					taskInVo.setHandlerIdKey(claimTaskVo.getHandlerIdKey());
					taskInVo.setItemName(claimTaskVo.getItemName());
					taskInVo.setRegistNo(registNo);
					taskInVo.setBussTag(claimTaskVo.getBussTag());
					taskInVo.setShowInfoXML(claimTaskVo.getShowInfoXML());
					//添加调查标识
					TaskExtMapUtils extMapUtil = new TaskExtMapUtils();
					extMapUtil.addTagMap(certifyVo,"surveyFlag");
					extMapUtil.addTagMap(certifyVo,"isSYFraud");
					extMapUtil.addTagMap(certifyVo,"isJQFraud");
					taskInVo.setBussTagMap(extMapUtil.getBussTagMap());
					/*SysUserVo sysUserVo = assignService.execute(FlowNode.Compe, submitVo.getComCode());//轮询
					taskInVo.setAssignUser(sysUserVo.getUserCode());
					taskInVo.setAssignCom(sysUserVo.getComCode());*/
					super.setTaskInVo(taskInVo,submitVo,FlowNode.CompeBI);
					PrpLWfTaskIn oldtaskIn = wfTaskService.queryTaskInByHandKey(taskInVo.getFlowId(),taskInVo.getHandlerIdKey(),taskInVo.getSubNodeCode());
					if(oldtaskIn == null){//已经存在未处理或者正在处理的理算任务不再发起
						compTaskVos.add(taskInVo);
					}
				}
			}
		}

		PrpLWfTaskVo[] taskInVos = new PrpLWfTaskVo[compTaskVos.size()];
		if(taskInVos.length == 0){
			throw new IllegalStateException("该案子没有有效的立案，单证不能提交。或者已生成理算任务，单证无需重复提交！");
		}
		wfTaskService.submitTask(submitVo.getFlowTaskId(),compTaskVos.toArray(taskInVos));

		return compTaskVos;
	}
	
	/**
	 * 提交单证主表信息
	 * @param checkVo
	 * @param submitVo
	 */
	public void submitCertifyMain(PrpLCheckVo checkVo,WfTaskSubmitVo submitVo){
		PrpLCertifyMainVo prpLCertifyMainVo = new PrpLCertifyMainVo();
		Date nowDate = new Date();
		prpLCertifyMainVo.setRegistNo(checkVo.getRegistNo());
		prpLCertifyMainVo.setStartTime(nowDate);
		prpLCertifyMainVo.setCollectFlag("0");//默认未收齐全
		prpLCertifyMainVo.setLawsuitFlag("0");//默认为否
		prpLCertifyMainVo.setValidFlag("1");//有效
		prpLCertifyMainVo.setCreateUser(submitVo.getTaskInUser());
		prpLCertifyMainVo.setCreateTime(nowDate);
		prpLCertifyMainVo.setUpdateUser(submitVo.getTaskInUser());
		prpLCertifyMainVo.setUpdateTime(nowDate);
		
		certifyPubService.submitCertify(prpLCertifyMainVo);
	}
	/**
	 * 理算冲销核赔0结案后将单证节点变成已处理
	 * <pre></pre>
	 * @modified:
	 * ☆WLL(2016年12月2日 下午5:05:16): <br>
	 */
	public void submitCertifyAfterEndCase(String registNo){
		// 查询出在In表的单证节点TaskInVo
		List<PrpLWfTaskVo> taskInVoList = wfTaskService.findPrpLWfTaskInByRegistNo(registNo);
		PrpLWfTaskVo taskInVo = null;
		if(taskInVoList!=null&&taskInVoList.size()>0){
			for(PrpLWfTaskVo vo : taskInVoList){
				if(FlowNode.Certi.name().equals(vo.getNodeCode())){
					taskInVo = vo;
				}
			}
		}
		// 调用moveInToOut方法
		if(taskInVo!=null){
			wfTaskService.moveInToOut(taskInVo.getTaskId(),taskInVo);
		}
		
	}
}
