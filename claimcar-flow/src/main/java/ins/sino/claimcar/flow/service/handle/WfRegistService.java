package ins.sino.claimcar.flow.service.handle;

import ins.framework.utils.Beans;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.FlowStatus;
import ins.sino.claimcar.CodeConstants.HandlerStatus;
import ins.sino.claimcar.CodeConstants.WorkStatus;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.WfMainService;
import ins.sino.claimcar.flow.service.WfNodeService;
import ins.sino.claimcar.flow.service.WfTaskQueryService;
import ins.sino.claimcar.flow.service.WfTaskService;
import ins.sino.claimcar.flow.util.TaskExtMapUtils;
import ins.sino.claimcar.flow.vo.PrpLWfMainVo;
import ins.sino.claimcar.flow.vo.PrpLWfNodeVo;
import ins.sino.claimcar.flow.vo.PrpLWfTaskQueryVo;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.flow.vo.WfTaskSubmitVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 报案提交工作流处理
 * @author ★LiuPing
 * @CreateTime 2016年1月08日
 */
@Service("wfRegistService")
public class WfRegistService extends WfBaseHandleService {

	@Autowired
	private WfTaskService wfTaskService;
	@Autowired
	private WfMainService wfMainService;
	@Autowired
	private WfNodeService wfNodeService;
	@Autowired
	private WfTaskQueryService wfTaskQueryService;
	@Autowired
	private WfClaimService wfClaimService;
	
	/**
	 * 报案首次暂存 ☆LiuPing(2016年1月30日 ): <br>
	 */
	public PrpLWfTaskVo tempSaveRegistHandle(PrpLRegistVo registVo,WfTaskSubmitVo submitVo) {

		PrpLWfMainVo wfMainVo = null;
		PrpLWfTaskQueryVo taskQueryVo = null;

		PrpLWfNodeVo registNodeVo = null;
		PrpLWfTaskVo taskRegisInVo = null;

		// 设置相关信息start
		/*报案暂存，将报案节点数据初次保存到 main query，taskin*/
		wfMainVo = this.createWfMainVo(registVo,submitVo);
		taskQueryVo = this.createWfTaskQueryVo(registVo,submitVo);
		registNodeVo = super.createWfNextNodeVo(submitVo,FlowNode.Regis);

		taskRegisInVo = this.setWfTaskInVo(registVo,submitVo,FlowNode.Regis);
		// 报案节点直接变为正在处理，
		taskRegisInVo.setHandlerStatus(HandlerStatus.DOING);// 正在处理
		taskRegisInVo.setWorkStatus(WorkStatus.DOING);// 正在处理
		taskRegisInVo.setHandlerCom(submitVo.getAssignCom());
		taskRegisInVo.setHandlerUser("ANYONE");//报案案件查询特殊处理,taskIn表assignUser和handlerUser赋值为ANYONE,允许全部报案权限工号查询
		taskRegisInVo.setHandlerTime(new Date());
		taskRegisInVo.setHandlerMinutes(0);


		wfMainService.save(wfMainVo);
		wfNodeService.save(registNodeVo);
		wfTaskQueryService.save(taskQueryVo);

		// 保存报案
		BigDecimal registTaskId = wfTaskService.saveTaskIn(taskRegisInVo);
		taskRegisInVo.setTaskId(registTaskId);

		return taskRegisInVo;
	}

	/**
	 * 报案提交
	 * @modified: ☆LiuPing(2016年1月30日 ): <br>
	 */
	public PrpLWfTaskVo submitRegistHandle(PrpLRegistVo registVo,WfTaskSubmitVo submitVo) {
		PrpLWfMainVo wfMainVo = null;
		PrpLWfTaskQueryVo taskQueryVo = null;

		PrpLWfNodeVo nextNodeVo = null;
		PrpLWfTaskVo taskInVo = null;
		String registNo = registVo.getRegistNo();
		// 设置相关信息start
		/*报案提交要重新获取main，task，进行update处理*/
		super.updateWfEndNodeVo(submitVo.getFlowId(),FlowNode.Regis);

		wfMainVo = this.createWfMainVo(registVo,submitVo);
		taskQueryVo = this.createWfTaskQueryVo(registVo,submitVo);
		nextNodeVo = super.createWfNextNodeVo(submitVo,FlowNode.Sched);

		taskInVo = this.setWfTaskInVo(registVo,submitVo,FlowNode.Sched);

		wfMainService.update(wfMainVo);
		wfTaskQueryService.update(taskQueryVo,"1");
		wfNodeService.save(nextNodeVo);

		// 报案提交时需要把报案暂存时的xml和bussTag更新一下。
		PrpLWfTaskVo registTaskInVo = wfTaskService.updateRegistTask(registNo,taskInVo.getBussTag(),taskInVo.getShowInfoXML());

		BigDecimal registTaskId = registTaskInVo.getTaskId();
		// 提交报案
		wfTaskService.submitTask(registTaskId,taskInVo);

		// 提交报案后，根据报案信息创建两个虚拟的立案节点
		BigDecimal taskId = taskInVo.getTaskId();
		submitVo.setFlowTaskId(registTaskId);
		wfClaimService.createClaimByRegist(registVo,submitVo,taskInVo);
		//设置taskId
		taskInVo.setTaskId(taskId);
		

		return taskInVo;
	}
	
	/**
	 * 理赔流程主信息设置
	 * @param registVo
	 * @param submitVo
	 * @param nowDate
	 * @return
	 */
	private PrpLWfMainVo createWfMainVo(PrpLRegistVo registVo,WfTaskSubmitVo submitVo) {
		//理赔流程主信息
		PrpLWfMainVo wfMainVo = new PrpLWfMainVo();
		wfMainVo.setFlowId(registVo.getFlowId());
		wfMainVo.setRegistNo(registVo.getRegistNo());
		wfMainVo.setPolicyNo(registVo.getPolicyNo());
		wfMainVo.setPolicyNoLink(registVo.getPrpLRegistExt().getPolicyNoLink());
		wfMainVo.setRiskCode(registVo.getRiskCode());
		wfMainVo.setComCode(registVo.getComCode());
		wfMainVo.setFlowStatus(FlowStatus.NORMAL);// 正常处理
		wfMainVo.setLastNode(FlowNode.Regis.name());
		wfMainVo.setStoreFlag(null);
		wfMainVo.setRegistTime(registVo.getReportTime());
		wfMainVo.setLicenseNo(registVo.getPrpLRegistExt().getLicenseNo());
		wfMainVo.setFrameNo(registVo.getPrpLRegistExt().getFrameNo());
		wfMainVo.setReportorName(registVo.getReportorName());
		wfMainVo.setReportorPhone(registVo.getReportorPhone());
		//业务属性
		wfMainVo.setBusinessFlag("");
		wfMainVo.setComCodePly(registVo.getComCode());
		return wfMainVo;
	}
	
	/**
	 * 设置理赔流程查询信息
	 * @param registVo
	 * @param submitVo
	 * @param nowDate
	 * @return
	 */
	private PrpLWfTaskQueryVo createWfTaskQueryVo(PrpLRegistVo registVo,WfTaskSubmitVo submitVo) {
		// 理赔流程查询信息
		String policyNo = registVo.getPolicyNo();
		String policyNoLink = registVo.getPrpLRegistExt().getPolicyNoLink();

		PrpLWfTaskQueryVo taskQueryVo = new PrpLWfTaskQueryVo();
		taskQueryVo.setFlowId(registVo.getFlowId());
		String registNo = registVo.getRegistNo();
		taskQueryVo.setRegistNo(registNo);
		taskQueryVo.setRegistNoRev(StringUtils.reverse(registNo));
		taskQueryVo.setPolicyNo(policyNo);
		taskQueryVo.setInsuredName(registVo.getPrpLRegistExt().getInsuredName());
		taskQueryVo.setInsuredCode(registVo.getPrpLRegistExt().getInsuredCode());
		taskQueryVo.setPolicyNoRev(StringUtils.reverse(policyNo));
		taskQueryVo.setPolicyNoLink(policyNoLink);
		taskQueryVo.setLicenseNo(registVo.getPrpLRegistExt().getLicenseNo());
		taskQueryVo.setLicenseNoRev(StringUtils.reverse(taskQueryVo.getLicenseNo()));

		taskQueryVo.setMercyFlag(registVo.getMercyFlag());
		taskQueryVo.setCustomerLevel(registVo.getCustomerLevel());
		taskQueryVo.setIsOnSitReport(registVo.getPrpLRegistExt().getIsOnSitReport());
		taskQueryVo.setFrameNo(registVo.getPrpLRegistExt().getFrameNo());
		taskQueryVo.setFrameNoRev(StringUtils.reverse(taskQueryVo.getFrameNo()));

		taskQueryVo.setReporterName(registVo.getReportorName());
		taskQueryVo.setReporterPhone(registVo.getReportorPhone());
		taskQueryVo.setReportTime(registVo.getReportTime());
		taskQueryVo.setDamageTime(registVo.getDamageTime());
		taskQueryVo.setComCodePly(registVo.getComCode());
		taskQueryVo.setRiskCode(registVo.getRiskCode());
		taskQueryVo.setTempRegistFlag(registVo.getTempRegistFlag());
		taskQueryVo.setDamageAddress(registVo.getDamageAddress());
		taskQueryVo.setTpFlag(registVo.getTpFlag());
		taskQueryVo.setIsMajorCase(registVo.getIsMajorCase());
		taskQueryVo.setIsSubRogation(registVo.getPrpLRegistExt().getIsSubRogation());
		String policyType = null;
		if(StringUtils.isNotBlank(policyNoLink)){
			policyType = "商业+交强";
		}else if("1101".equals(registVo.getRiskCode())){
			policyType = "交强";
		}else{
			policyType = "商业";
		}
		taskQueryVo.setPolicyType(policyType);
		return taskQueryVo;
	}
	
	/**
	 * 报案taskout信息写入
	 * @param registVo
	 * @param submitVo
	 * @param nowDate
	 * @return
	 */
	private PrpLWfTaskVo setWfTaskInVo(PrpLRegistVo registVo,WfTaskSubmitVo submitVo,FlowNode node) {
		//理赔流程正在处理任务节点
		PrpLWfTaskVo taskInVo = new PrpLWfTaskVo();
		taskInVo.setFlowId(registVo.getFlowId());
		taskInVo.setHandlerIdKey(registVo.getRegistNo());
		taskInVo.setTaskName(node.getName());
		taskInVo.setUpperTaskId(new BigDecimal(0));
		taskInVo.setNodeCode(node.name());
		taskInVo.setSubNodeCode(node.name());
		taskInVo.setItemName(registVo.getPrpLRegistExt().getLicenseNo());
		taskInVo.setRegistNo(registVo.getRegistNo());
		//修改comcode,取registVo无保单是错误的，所以用submitVo.getComCode()
		taskInVo.setComCode(submitVo.getComCode());
		taskInVo.setRiskCode(registVo.getRiskCode());
		taskInVo.setHandlerStatus(HandlerStatus.INIT);// 正在处理
		taskInVo.setWorkStatus(WorkStatus.INIT);// 正在处理

		taskInVo.setTaskInTime(new Date());
		taskInVo.setTaskInUser(submitVo.getTaskInUser());
		taskInVo.setTaskInKey(registVo.getRegistNo());
		
		taskInVo.setAssignUser(submitVo.getAssignUser());
		taskInVo.setAssignCom(submitVo.getAssignCom());
		taskInVo.setTaskInNode(node.name());


		// 处理业务标记和扩展XML显示内容
		TaskExtMapUtils extMapUtil = new TaskExtMapUtils();
		extMapUtil.addTagMap(registVo.getPrpLRegistExt(),"isClaimSelf","isAlarm","isSubRogation");
		extMapUtil.addTagMap(registVo,"tempRegistFlag","mercyFlag","customerLevel","isMajorCase","tpFlag","qdcaseType","isQuickCase","isGBFlag");
		extMapUtil.addTagMap(registVo,"tempRegistFlag","mercyFlag","customerLevel","isMajorCase","tpFlag","qdcaseType","isQuickCase","selfClaimFlag");
		
		if(CodeConstants.GBFlag.MAJORCOMMON.equals(registVo.getIsGBFlag()) || CodeConstants.GBFlag.MINORCOMMON.equals(registVo.getIsGBFlag()) || CodeConstants.GBFlag.MAJORRELATION.equals(registVo.getIsGBFlag()) || CodeConstants.GBFlag.MINORRELATION.equals(registVo.getIsGBFlag())){
			PrpLRegistVo registVo2 = new PrpLRegistVo();
			Beans.copy().from(registVo).to(registVo2);
			registVo2.setIsGBFlag("1");
			extMapUtil.addTagMap(registVo2,"isGBFlag");
		}
		registVo.setLicense(registVo.getPrpLRegistExt().getLicenseNo());
		extMapUtil.addXmlMap(registVo,"damageAddress","license");
		taskInVo.setBussTag(extMapUtil.getBussTag());
		taskInVo.setShowInfoXML(extMapUtil.getShowInfoXML());

		return taskInVo;
	}

	/**
	 * 更新报案节点数据
	 * @param registVo
	 * @param submitVo
	 * @return
	 * @modified: ☆LiuPing(2016年2月26日 ): <br>
	 */
	public PrpLWfTaskVo updateRegistHandle(PrpLRegistVo registVo,WfTaskSubmitVo submitVo) {
		PrpLWfMainVo wfMainVo = null;
		PrpLWfTaskQueryVo taskQueryVo = null;

		PrpLWfTaskVo taskInVo = null;
		String registNo = registVo.getRegistNo();

		wfMainVo = this.createWfMainVo(registVo,submitVo);
		taskQueryVo = this.createWfTaskQueryVo(registVo,submitVo);
		taskInVo = this.setWfTaskInVo(registVo,submitVo,FlowNode.Sched);

		wfMainService.update(wfMainVo);
		wfTaskQueryService.update(taskQueryVo,"1");

		// 报案暂存时的xml和bussTag更新一下。
		PrpLWfTaskVo registTaskInVo = wfTaskService.updateRegistTask(registNo,taskInVo.getBussTag(),taskInVo.getShowInfoXML());

		return registTaskInVo;
	}

	/**
	 * 注销报案
	 * @param registVo
	 * @param submitVo
	 * @return
	 * @modified: ☆LiuPing(2016年2月26日 ): <br>
	 */
	public PrpLWfTaskVo cancelRegistHandle(PrpLRegistVo registVo,WfTaskSubmitVo submitVo) {
		super.cancelWfNode(submitVo,FlowNode.Regis);
		String flowId = registVo.getFlowId();

		PrpLWfTaskVo taskInVo = this.setWfTaskInVo(registVo,submitVo,FlowNode.Cancel);
		wfTaskService.cancelRegist(flowId,submitVo.getTaskInUser());
		return taskInVo;
	}
	/**
	 * 删除立案
	 * @param registVo
	 * @param submitVo
	 * @return
	 * @modified: zjd
	 */
	public BigDecimal deleteByRegist(PrpLRegistVo registVo) {
		
		return wfTaskService.deleteByRegist(registVo);
	}
	/**
	 * 无保单关联与取消增加立案节点
	 */
	public PrpLWfTaskVo submitClaimHandle(PrpLRegistVo registVo,WfTaskSubmitVo submitVo) {
		PrpLWfTaskVo taskInVo = null;
		taskInVo = this.setWfTaskInVo(registVo,submitVo,FlowNode.Sched);

		//根据报案信息创建两个虚拟的立案节点
		BigDecimal taskId = taskInVo.getTaskId();
		wfClaimService.createClaimByRegist(registVo,submitVo,taskInVo);
		//设置taskId
		taskInVo.setTaskId(taskId);
		return taskInVo;
	}
	public PrpLWfTaskVo submitClaimHandles(PrpLRegistVo registVo,WfTaskSubmitVo submitVo) {
		PrpLWfTaskVo taskInVo = null;
		taskInVo = this.setWfTaskInVo(registVo,submitVo,FlowNode.Sched);

		//根据报案信息创建虚拟的立案节点
		BigDecimal taskId = taskInVo.getTaskId();
		wfClaimService.createClaimByPolicyNo(registVo,submitVo,taskInVo);
		//设置taskId
		taskInVo.setTaskId(taskId);
		return taskInVo;
	}
	public BigDecimal findByRegist(PrpLRegistVo registVo) {
		
		return wfTaskService.findByRegist(registVo);
	}
	public String findByRegistNoAndNode(PrpLRegistVo registVo,String nodeCode) {
		return wfTaskService.findByRegistNoAndNode(registVo, nodeCode);
	}
	/**
	 * 删除立案
	 * @param registVo
	 * @param submitVo
	 * @return
	 * @modified: zjd
	 */
	public BigDecimal deleteByRegists(PrpLRegistVo registVo,String subNodeCode) {
		
		return wfTaskService.deleteByRegists(registVo,subNodeCode);
	}
}
