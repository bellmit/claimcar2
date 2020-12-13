package ins.sino.claimcar.flow.service.handle;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.service.CodeTranService;
import ins.framework.utils.Beans;
import ins.sino.claimcar.CodeConstants.WorkStatus;
import ins.sino.claimcar.check.vo.PrpLCheckCarVo;
import ins.sino.claimcar.check.vo.PrpLCheckPropVo;
import ins.sino.claimcar.check.vo.PrpLCheckVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.po.PrpLWfTaskQuery;
import ins.sino.claimcar.flow.service.WfTaskQueryService;
import ins.sino.claimcar.flow.service.WfTaskService;
import ins.sino.claimcar.flow.util.TaskExtMapUtils;
import ins.sino.claimcar.flow.vo.PrpLWfTaskQueryVo;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.flow.vo.WfTaskSubmitVo;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.schedule.vo.PrpLScheduleDefLossVo;
import ins.sino.claimcar.schedule.vo.PrpLScheduleTaskVo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("wfCheckService")
public class WfCheckService extends WfBaseHandleService {

	@Autowired
	private WfTaskService wfTaskService;
	
	@Autowired
	private WfTaskQueryService wfTaskQueryService;
	
	@Autowired
	private CodeTranService codeTranService;
	
	@Autowired
	private WfCertifyService wfCertifyService;

	@Autowired
	private WfClaimService wfClaimService;
	
	@Autowired
	DatabaseDao databaseDao;
	
	@Autowired
	PolicyViewService policyViewService;
	
	public List<PrpLWfTaskVo> submitCheckHandle(PrpLScheduleTaskVo scheduleVo,PrpLCheckVo checkVo,WfTaskSubmitVo submitVo) {
		String flowId = submitVo.getFlowId();

		// 更新处理完成的主节点信息
		super.updateWfEndNodeVo(flowId,FlowNode.Check);
		//
		this.updateWfTaskQueryVo(flowId,checkVo);
		// 初始化保存下一个主节点
		super.saveWfNextNodeVo(submitVo,FlowNode.DLoss);
		//更新taskQuery表信息
//		PrpLWfTaskQueryVo taskQueryVo = new PrpLWfTaskQueryVo();
//		taskQueryVo.setFlowId(flowId);
//		taskQueryVo.setMercyFlag(checkVo.getMercyFlag());//案件紧急程度
//		taskQueryVo.setReconcileFlag(checkVo.getReconcileFlag());//是否现场调解
//		
//		wfTaskQueryService.update(taskQueryVo);

		// 处理task信息
		List<PrpLWfTaskVo> taskInVoList = new ArrayList<PrpLWfTaskVo>();

		//根据serialNo存储查勘车辆信息
		Map<String,Map<String,String>> checkInfoMap = new HashMap<String,Map<String,String>>();
		List<PrpLCheckCarVo> prpLCheckCarVoList = checkVo.getPrpLCheckTask().getPrpLCheckCars();
		for(PrpLCheckCarVo prpLCheckCarVo:prpLCheckCarVoList){	
			String serialNo = prpLCheckCarVo.getSerialNo().toString();
			Map<String,String> mapTemp = new HashMap<String,String>();
			mapTemp.put("serialNo", serialNo.equals("1") ? "标的车" : "三者车");//序号 判断损失方
			mapTemp.put("license", prpLCheckCarVo.getPrpLCheckCarInfo().getLicenseNo());//车牌号码
			mapTemp.put("modelName", prpLCheckCarVo.getPrpLCheckCarInfo().getBrandName());
			mapTemp.put("lossFee", prpLCheckCarVo.getLossFee().toString());//估损金额
			checkInfoMap.put(serialNo, mapTemp);
		}
		
		//根据serialNo存储查勘财产损失信息
		List<PrpLCheckPropVo> prpLCheckPropList = checkVo.getPrpLCheckTask().getPrpLCheckProps();
		for(PrpLCheckPropVo prpLCheckPropVo:prpLCheckPropList){
			String serialNo = prpLCheckPropVo.getLossPartyId().toString();
			Map<String,String> mapTemp = new HashMap<String,String>();
			//判断损失方
			String itemCode = "1".equals(serialNo) ? "标的车" : "0".equals(serialNo) ? "地面" : "三者车";
			mapTemp.put("serialNo", itemCode);//序号
			Map<String,String> key = checkInfoMap.get(serialNo);
			mapTemp.put("license", "0".equals(serialNo) ? "地面" : key.get("license"));//车牌号码
			mapTemp.put("lossFee", prpLCheckPropVo.getLossFee().toString());//估损金额
			checkInfoMap.put(serialNo, mapTemp);
		}
		
		// 查询处理后会保存到定损调度表，后续节点的生成依据定损调度表
		List<PrpLScheduleDefLossVo> defLossVoList = scheduleVo.getPrpLScheduleDefLosses();
		FlowNode nextNode = null;
		String deflossType = null;// 1-车辆定损，2-财产定损
		//案件紧急程度标示覆盖
		Map<String,String> bussTagMap = new HashMap<String,String>();
		bussTagMap.put("mercyFlag",checkVo.getMercyFlag());
		if(StringUtils.isNotEmpty(submitVo.getIsMobileAccept())){
			bussTagMap.put("isMobileCase",submitVo.getIsMobileAccept());
		}
		for(PrpLScheduleDefLossVo defLossVo:defLossVoList){	
			PrpLWfTaskVo taskInVo = new PrpLWfTaskVo();
			taskInVo.setHandlerIdKey(defLossVo.getId().toString());
			String carName = defLossVo.getItemsName()+defLossVo.getItemsContent() + "";
			taskInVo.setItemName("1".equals(defLossVo.getDeflossType()) 
					? carName : defLossVo.getItemsName());
			taskInVo.setRegistNo(scheduleVo.getRegistNo());
			taskInVo.setAssignUser(defLossVo.getScheduledUsercode());
			taskInVo.setAssignCom(defLossVo.getScheduledComcode());
			//是否代查勘标识
			String subCheckFlag = wfTaskQueryService.getSubCheckFlag(scheduleVo.getRegistNo(), defLossVo.getScheduledUsercode(),null);
			taskInVo.setSubCheckFlag(subCheckFlag);
			deflossType = defLossVo.getDeflossType();
			nextNode = "1".equals(deflossType) ? FlowNode.DLCar : FlowNode.DLProp;
			super.setTaskInVo(taskInVo,submitVo,nextNode);
			//取得保存的查勘定损信息
			String serialNo = defLossVo.getSerialNo().toString();
			TaskExtMapUtils extMapUtil = new TaskExtMapUtils();
			extMapUtil.putXmlMap(checkInfoMap.get(serialNo));
			taskInVo.setBussTagMap(bussTagMap);//更新案件紧急程度标示
			taskInVo.setShowInfoXML(extMapUtil.getShowInfoXML());
			taskInVoList.add(taskInVo);

		}
		if(submitVo.getOthenNodes()!=null&&submitVo.getOthenNodes().length>0){
			for(FlowNode otherNode:submitVo.getOthenNodes()){// 发起其他节点，查勘是大案审核、单证收集
				PrpLWfTaskVo taskInVo = new PrpLWfTaskVo();
				taskInVo.setHandlerIdKey(checkVo.getId().toString());
				taskInVo.setRegistNo(checkVo.getRegistNo());
				super.setTaskInVo(taskInVo,submitVo,otherNode);
				//taskInVo.setNodeCode(otherNode.name());
				taskInVo.setBussTagMap(bussTagMap);//更新案件紧急程度标示
				//大案审核无需指定人
				taskInVo.setAssignUser("");
				taskInVoList.add(taskInVo);
			}
		}

		//自动发起单证收集
		// 初始化保存下一个主节点
		super.saveWfNextNodeVo(submitVo,FlowNode.Certi);
		PrpLWfTaskVo taskInVo = new PrpLWfTaskVo();
		taskInVo.setHandlerIdKey(checkVo.getId().toString());
		taskInVo.setItemName(null);
		taskInVo.setRegistNo(checkVo.getRegistNo());
		super.setTaskInVo(taskInVo,submitVo,FlowNode.Certi);
		if("1".equals(submitVo.getPingAnFlag())){
			taskInVo.setAssignUser("AUTO");
		}else{
			taskInVo.setAssignUser(null);
		}
		
		taskInVo.setAssignCom(submitVo.getComCode());
		// 案件紧急程度标示覆盖
		bussTagMap.put("isMobileCase","0");
		taskInVo.setBussTagMap(bussTagMap);
		taskInVoList.add(taskInVo);
		if(taskInVoList.size() > 0){
			PrpLWfTaskVo[] taskInVos = new PrpLWfTaskVo[taskInVoList.size()];
			wfTaskService.submitTask(submitVo.getFlowTaskId(),taskInVoList.toArray(taskInVos));
		}else{
			PrpLWfTaskVo taskTemp = new PrpLWfTaskVo();
			taskTemp.setHandlerIdKey(checkVo.getId().toString());
			taskTemp.setWorkStatus(WorkStatus.END);
			taskTemp.setTaskInTime(new Date());
			taskTemp.setTaskInUser(submitVo.getTaskInUser());
			wfTaskService.moveInToOut(submitVo.getFlowTaskId(), taskInVo);
		}
		if(!"1".equals(submitVo.getPingAnFlag())){
			wfClaimService.updateClaimByCheck(submitVo.getFlowId(),bussTagMap);
		}
		

		//自动发起单证收集
		//wfCertifyService.submitCertifyMain(checkVo,submitVo);
		//taskInVoList.add(certifyTaskVo);
		return taskInVoList;

	}

	/**
	 * 更新PrpLWfTaskQuery
	 * @param flowId
	 * @return
	 */
	private void updateWfTaskQueryVo(String flowId,PrpLCheckVo checkVo) {
		// 理赔流程查询信息
		PrpLWfTaskQuery wfTaskQueryPo = databaseDao.findByPK(PrpLWfTaskQuery.class, flowId);
		PrpLWfTaskQueryVo wfTaskQueryVo = new PrpLWfTaskQueryVo();
		String registNo = wfTaskQueryPo.getRegistNo();
		String policyNo = wfTaskQueryPo.getPolicyNo();
		Beans.copy().from(wfTaskQueryPo).to(wfTaskQueryVo);
		wfTaskQueryVo.setMercyFlag(checkVo.getMercyFlag());//案件紧急程度
		wfTaskQueryVo.setReconcileFlag(checkVo.getReconcileFlag());//是否现场调解
		//更新车牌号
		List<PrpLCheckCarVo> carVoList = checkVo.getPrpLCheckTask().getPrpLCheckCars();
		for(PrpLCheckCarVo carVo : carVoList){
			String licenseNo = carVo.getPrpLCheckCarInfo().getLicenseNo();
			if(carVo.getSerialNo()==1){
				wfTaskQueryVo.setLicenseNo(licenseNo);
				wfTaskQueryVo.setLicenseNoRev(StringUtils.reverse(licenseNo));
			}
		}
		List<PrpLCMainVo> cMainVos = policyViewService.getPolicyAllInfo(registNo);
		for(PrpLCMainVo cMainVo : cMainVos){
			if(!policyNo.equals(cMainVo.getPolicyNo())){
				wfTaskQueryVo.setPolicyNoLink(cMainVo.getPolicyNo());
			}
		}
		wfTaskQueryService.update(wfTaskQueryVo,"1");
	}
	
	/**
	 * 复查勘 提交 回核损，那个核损发起的 复勘 就提交回原来的核损任务
	 * @param checkVo
	 * @param submitVo
	 * @return
	 * @modified: ☆LiuPing(2016年6月1日 ): <br>
	 */
	public List<PrpLWfTaskVo> submitCheckReToVLoss(PrpLCheckVo checkVo,WfTaskSubmitVo submitVo) {
		// 先找到这个 复勘 是哪个核损发起的
		PrpLWfTaskVo parentTaskVo = getChkReNextNode(submitVo.getFlowTaskId().doubleValue());
		PrpLWfTaskVo taskInVo = parentTaskVo;
		super.setTaskInVo(taskInVo,submitVo,FlowNode.valueOf(parentTaskVo.getSubNodeCode()));

		List<PrpLWfTaskVo> taskInVoList = new ArrayList<PrpLWfTaskVo>();
		taskInVoList.add(taskInVo);
		PrpLWfTaskVo[] taskInVos = new PrpLWfTaskVo[taskInVoList.size()];
		wfTaskService.submitTask(submitVo.getFlowTaskId(),taskInVoList.toArray(taskInVos));
		return taskInVoList;
	}
	
	public PrpLWfTaskVo getChkReNextNode(Double flowTaskId) {
		PrpLWfTaskVo outTaskVo = null;
		PrpLWfTaskVo wfTaskVo = wfTaskService.queryTask(flowTaskId);
		if(FlowNode.VLoss.toString().equals(wfTaskVo.getNodeCode()) 
				&& !WorkStatus.TURN.equals(wfTaskVo.getWorkStatus())){
			outTaskVo = wfTaskVo;
		}else{
			outTaskVo = getChkReNextNode(wfTaskVo.getUpperTaskId().doubleValue());
		}
		return outTaskVo;
	}
}