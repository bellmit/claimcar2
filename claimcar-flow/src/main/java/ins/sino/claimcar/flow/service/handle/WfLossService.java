package ins.sino.claimcar.flow.service.handle;

import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.constant.Risk;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.AssignService;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.service.WfTaskQueryService;
import ins.sino.claimcar.flow.service.WfTaskService;
import ins.sino.claimcar.flow.util.TaskExtMapUtils;
import ins.sino.claimcar.flow.vo.PrpLWfTaskQueryVo;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.flow.vo.WfSimpleTaskVo;
import ins.sino.claimcar.flow.vo.WfTaskSubmitVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersHospitalVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceMainVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceVo;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropMainVo;
import ins.sino.claimcar.recloss.service.RecLossService;
import ins.sino.claimcar.recloss.vo.PrpLRecLossVo;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 调度工作流服务处理
 * @author ★LiuPing
 * @CreateTime 2016年1月11日
 */
@Service("wfLossService")
public class WfLossService extends WfBaseHandleService {
	
	@Autowired
	private WfTaskService wfTaskService;
	@Autowired
	private WfTaskQueryService wfTaskQueryService;
	@Autowired
	private WfSimpleTaskService wfSimpleTaskService;
	@Autowired
	private RecLossService recLossService;
	@Autowired
	private AssignService assignService;
	@Autowired
	WfTaskHandleService wfTaskHandleService;
	@Autowired
	PolicyViewService policyViewService;
	
	public List<PrpLWfTaskVo> submitLossCarHandle(PrpLDlossCarMainVo lossCarMainVo,WfTaskSubmitVo submitVo) {

		/*规则：
			定损提交：需要传当前节点代码，
			PrpLdLossCarMain 合计换件金额 =0 and 合计修理辅料费=0 进入核损，否则是先进入核价
			
			核价提交：需提供下一个提交动作（AuditStatus）及节点（来自flowNode）
			核价提交核损：
			核价自动通过到核损：
			核价回退定损：
			核价提交上级：
			核价退回下级：
			
			核损提交：
			核损退回定损
			核损提交复检
			核损提交复勘
			核损提交通过（END不触发下一个工作流或触发损余回收）
		*/

		FlowNode currentNode = submitVo.getCurrentNode();
		FlowNode nextNode = submitVo.getNextNode();
		PrpLWfTaskVo taskInVo = new PrpLWfTaskVo();
		taskInVo.setHandlerIdKey(lossCarMainVo.getId().toString());// 这里还是调度的id，借用一下方法moveInToOut取值的
		taskInVo.setItemName(lossCarMainVo.getLossCarInfoVo().getLicenseNo());
		taskInVo.setRegistNo(lossCarMainVo.getRegistNo());
		
		//更新taskQuery表信息
		PrpLWfTaskQueryVo taskQueryVo = new PrpLWfTaskQueryVo();
		taskQueryVo.setFlowId(submitVo.getFlowId());
		taskQueryVo.setMercyFlag(lossCarMainVo.getMercyFlag());//案件紧急程度
		//修改标的车
		if(lossCarMainVo.getDeflossCarType().equals("1")){
			if(lossCarMainVo.getLicenseNo() != null){
				taskQueryVo.setLicenseNo(lossCarMainVo.getLicenseNo());
				taskQueryVo.setLicenseNoRev(StringUtils.reverse(lossCarMainVo.getLicenseNo()));
			}
		}
		wfTaskQueryService.update(taskQueryVo,"0");
		super.saveMainNode(currentNode,nextNode,submitVo);

		super.setTaskInVo(taskInVo,submitVo,nextNode);
		taskInVo.setComCode(lossCarMainVo.getComCode());
		// 处理业务标记和扩展XML显示内容
		if(lossCarMainVo.getSerialNo()>1){
			lossCarMainVo.setSerialNo(3);
		}
		TaskExtMapUtils extMapUtil = new TaskExtMapUtils();
		extMapUtil.addXmlMap(lossCarMainVo.getLossCarInfoVo(),"licenseNo","modelName");
		extMapUtil.addXmlMap(lossCarMainVo, "serialNo","sumVeriLossFee");
		//extMapUtil.getShowInfoXMLMap().put("serialNo",lossCarMainVo.getSerialNo().toString());
		BigDecimal sumCompFee = lossCarMainVo.getSumCompFee();//合计换件金额
		BigDecimal sumMatFee = lossCarMainVo.getSumMatFee();//合计修理辅料费
        if (sumCompFee == null){
        	sumCompFee = new BigDecimal(0);
		}
		if (sumMatFee == null){
			sumMatFee = new BigDecimal(0);
		}
		//待核价金额
		extMapUtil.getShowInfoXMLMap().put("sumLossFee",sumCompFee.add(sumMatFee).toString());
		
		
		BigDecimal sumVeripLoss = lossCarMainVo.getSumVeripLoss();
		BigDecimal sumLossFee = lossCarMainVo.getSumLossFee();
		BigDecimal sumLossFees = lossCarMainVo.getSumLossFee();//定损金额
		
		BigDecimal sumchargefee = lossCarMainVo.getSumChargeFee();//费用金额
		BigDecimal sumsubriskfee = lossCarMainVo.getSumSubRiskFee();//附加险金额
		if (sumLossFees == null){
			sumLossFees = new BigDecimal(0);
		}
		if (sumchargefee == null){
			sumchargefee = new BigDecimal(0);
		}
		if (sumsubriskfee == null){
			sumsubriskfee = new BigDecimal(0);
		}
		//System.out.println(nextNode.getUpperNode()+nextNode.getUpperNode().toString()+nextNode.getRootNode());
			if(FlowNode.VPCar.toString().equals(nextNode.getUpperNode())){//核价
				sumLossFees = sumCompFee.add(sumMatFee);
				taskInVo.setMoney(sumLossFees);//金额排序
			}else{//核损
				sumLossFees = sumLossFees.add(sumchargefee).add(sumsubriskfee);
				taskInVo.setMoney(sumLossFees);//金额排序
				
			}
		if(sumVeripLoss != null){
			BigDecimal sumRepairFee  = lossCarMainVo.getSumRepairFee();//合计修理金额
			BigDecimal sumOutFee = lossCarMainVo.getSumOutFee();//合计外修金额
			if (sumRepairFee == null){
				sumRepairFee = new BigDecimal(0);
			}
			if (sumOutFee == null){
				sumOutFee = new BigDecimal(0);
			}
			//核价金额+修理金额+外修金额 = 待核损金额 sumVeriLoss
			extMapUtil.getShowInfoXMLMap().put("sumVeripLoss",sumVeripLoss.add(sumRepairFee).add(sumOutFee).add(sumchargefee).add(sumsubriskfee).toString());
		}else{
			if (sumLossFee == null){
				sumLossFee = new BigDecimal(0);
			}
			//定损金额 = 待核损金额
			extMapUtil.getShowInfoXMLMap().put("sumVeripLoss",sumLossFee.toString());
		}
		taskInVo.setShowInfoXML(extMapUtil.getShowInfoXML());
		//案件紧急程度标示覆盖
		extMapUtil.addTagMap(lossCarMainVo,"mercyFlag","isMobileCase");
		taskInVo.setBussTagMap(extMapUtil.getBussTagMap());
		
		wfTaskService.submitTask(submitVo.getFlowTaskId(),taskInVo);
		//核损结束 
		if(FlowNode.END.equals(taskInVo.getSubNodeCode())){
			taskInVo.setTaskId(submitVo.getFlowTaskId());
		}

		List<PrpLWfTaskVo> taskInVoList = new ArrayList<PrpLWfTaskVo>();
		taskInVoList.add(taskInVo);
		if(taskInVo.getNodeCode().equals(FlowNode.END.toString()) 
				&& !CodeConstants.defLossSourceFlag.ADDTIONALDEFLOSS.equals(lossCarMainVo.getDeflossSourceFlag()) 
				&& "1".equals(lossCarMainVo.getRecycleFlag())
				){//核损通过并且核损任务不是定损追加
			PrpLWfTaskVo prpLWfTaskVo = this.recLossSubmit(lossCarMainVo,null,submitVo,taskInVo);;
			if(prpLWfTaskVo != null){
				taskInVoList.add(prpLWfTaskVo);
			}
		}
		return taskInVoList;
	}

	public List<PrpLWfTaskVo> submitLossPropHandle(PrpLdlossPropMainVo lossPropMainVo,WfTaskSubmitVo submitVo) {

		FlowNode currentNode = submitVo.getCurrentNode();
		FlowNode nextNode = submitVo.getNextNode();

		PrpLWfTaskVo taskInVo = new PrpLWfTaskVo();
		taskInVo.setHandlerIdKey(lossPropMainVo.getId().toString());// 这里还是调度的id，借用一下方法moveInToOut取值的
		taskInVo.setItemName(lossPropMainVo.getLicense());
		taskInVo.setRegistNo(lossPropMainVo.getRegistNo());

		super.saveMainNode(currentNode,nextNode,submitVo);

		super.setTaskInVo(taskInVo,submitVo,nextNode);

		// 处理业务标记和扩展XML显示内容
		if(lossPropMainVo.getSerialNo()>1){
			lossPropMainVo.setSerialNo(3);
		}
		
		BigDecimal sumDefloss = lossPropMainVo.getSumDefloss();
		BigDecimal sumLossFee = lossPropMainVo.getSumLossFee();
		if (sumDefloss == null){
			sumDefloss = new BigDecimal(0);
		}
		if (sumLossFee == null){
			sumLossFee = new BigDecimal(0);
		}
		sumDefloss = sumDefloss.add(sumLossFee);
		taskInVo.setMoney(sumDefloss);//金额排序
		lossPropMainVo.setSumDefloss(sumDefloss);
		
		TaskExtMapUtils extMapUtil = new TaskExtMapUtils();
		extMapUtil.addXmlMap(lossPropMainVo, "license","serialNo");
		extMapUtil.addXmlMap(lossPropMainVo, "sumDefloss","sumVeriLoss");
		taskInVo.setShowInfoXML(extMapUtil.getShowInfoXML());

		
		
		wfTaskService.submitTask(submitVo.getFlowTaskId(),taskInVo);
		
		List<PrpLWfTaskVo> taskInVoList = new ArrayList<PrpLWfTaskVo>();
		taskInVoList.add(taskInVo);
		if(taskInVo.getNodeCode().equals(FlowNode.END.toString()) && !"2".equals(lossPropMainVo.getDeflossSourceFlag()) && "1".equals(lossPropMainVo.getRecycleFlag())){//TODO 核损通过并且核损任务不是定损追加
			Boolean bl = wfTaskHandleService.existTaskByNodeCode(lossPropMainVo.getRegistNo(), FlowNode.RecLoss, String.valueOf(lossPropMainVo.getId()), "");
			if(!("3".equals(lossPropMainVo.getDeflossSourceFlag()) && bl)){//已发起损余回收的话，则定损修改不能发起
				PrpLWfTaskVo prpLWfTaskVo = this.recLossSubmit(null,lossPropMainVo,submitVo,taskInVo);
				if(prpLWfTaskVo != null){
					taskInVoList.add(prpLWfTaskVo);
				}
			}
		}
		return taskInVoList;
	}

	public List<PrpLWfTaskVo> submitLossPersonHandle(PrpLDlossPersTraceMainVo lossPersonMainVo,WfTaskSubmitVo submitVo) {


		FlowNode currentNode = submitVo.getCurrentNode();
		FlowNode nextNode = submitVo.getNextNode();

		PrpLWfTaskVo taskInVo = new PrpLWfTaskVo();
		taskInVo.setHandlerIdKey(lossPersonMainVo.getId().toString());// 这里还是调度的id，借用一下方法moveInToOut取值的
		taskInVo.setRegistNo(lossPersonMainVo.getRegistNo());

		super.saveMainNode(currentNode,nextNode,submitVo);


		super.setTaskInVo(taskInVo,submitVo,nextNode);
		
		/*更新taskQuery信息start*/
		PrpLWfTaskQueryVo taskQueryVo = new PrpLWfTaskQueryVo();
		taskQueryVo.setFlowId(submitVo.getFlowId());
		//预约时间
		taskQueryVo.setAppointmentTime(lossPersonMainVo.getAppointmentTime());
		//人伤人员信息和医院信息
		StringBuffer lossPersonInfo = new StringBuffer("");
		if(lossPersonMainVo.getPrpLDlossPersTraces()!=null&&lossPersonMainVo.getPrpLDlossPersTraces().size()>0){
			for(PrpLDlossPersTraceVo  prpLDlossPersTraceVo:lossPersonMainVo.getPrpLDlossPersTraces()){
				for(PrpLDlossPersHospitalVo prpLDlossPersHospitalVo :prpLDlossPersTraceVo.getPrpLDlossPersInjured().getPrpLDlossPersHospitals()){
					String personName = prpLDlossPersTraceVo.getPrpLDlossPersInjured().getPersonName();
					String hospitalName = prpLDlossPersHospitalVo.getHospitalName();
					if(!StringUtils.isBlank(personName)){
						lossPersonInfo.append(personName);
						lossPersonInfo.append(",");
					}
					if(!StringUtils.isBlank(hospitalName)){
						lossPersonInfo.append(hospitalName);
						lossPersonInfo.append(";");
					}
				}
			}
		}
		taskQueryVo.setLossPersonInfo(lossPersonInfo.toString());
		wfTaskQueryService.update(taskQueryVo,"0");
		/*更新taskQuery信息end*/
		
		taskInVo.setShowInfoXML("");
		BigDecimal sumLossFee = new BigDecimal(0);
		if(lossPersonMainVo.getPrpLDlossPersTraces()!=null && lossPersonMainVo.getPrpLDlossPersTraces().size() > 0){
			for(PrpLDlossPersTraceVo vo : lossPersonMainVo.getPrpLDlossPersTraces()){
				if(vo.getSumdefLoss()!=null){
					sumLossFee = sumLossFee.add(vo.getSumdefLoss());
				}else{
					sumLossFee = sumLossFee.add(vo.getSumReportFee());
				}
			}
		}
		if(lossPersonMainVo.getSumChargeFee() != null){//费用
			sumLossFee = sumLossFee.add(lossPersonMainVo.getSumChargeFee());
		}
		taskInVo.setMoney(sumLossFee);//金额排序
		List<PrpLWfTaskVo> taskInVoList = new ArrayList<PrpLWfTaskVo>();
		taskInVoList.add(taskInVo);

		if(submitVo.getOthenNodes()!=null&&submitVo.getOthenNodes().length>0){
			for(FlowNode otherNode:submitVo.getOthenNodes()){// 发起其他节点，查勘是大案审核
				PrpLWfTaskVo taskInOthVo = new PrpLWfTaskVo();
				taskInOthVo.setHandlerIdKey(lossPersonMainVo.getId().toString());
				taskInOthVo.setRegistNo(lossPersonMainVo.getRegistNo());
				super.setTaskInVo(taskInOthVo,submitVo,otherNode);
				//大案审核直接去保单的机构
				if(FlowNode.PLBig.name().equals(otherNode.getUpperNode())){
					List<PrpLCMainVo> cMainVoList = policyViewService.getPolicyAllInfo(lossPersonMainVo.getRegistNo());
					String biComCode = "";
					String ciComCode = "";
					if(cMainVoList != null && !cMainVoList.isEmpty()){
						for(PrpLCMainVo cMainVo : cMainVoList){
							if(!Risk.DQZ.equals(cMainVo.getRiskCode())){//商业
								biComCode = cMainVo.getComCode();
								break;
							}
							if(Risk.DQZ.equals(cMainVo.getRiskCode())){//交强
								ciComCode = cMainVo.getComCode();
							}
						}
					}
					if(StringUtils.isNotBlank(biComCode)){
						taskInOthVo.setAssignCom(biComCode);
					}else if(StringUtils.isNotBlank(ciComCode)){
						taskInOthVo.setAssignCom(ciComCode);
					}
				}
				taskInOthVo.setAssignUser("");
				taskInVoList.add(taskInOthVo);
			}
		}
		
		PrpLWfTaskVo[] taskInVos = new PrpLWfTaskVo[taskInVoList.size()];
		wfTaskService.submitTask(submitVo.getFlowTaskId(),taskInVoList.toArray(taskInVos));

		return taskInVoList;
	}

	/** 车辆定损退回 */
	public List<PrpLWfTaskVo> backCarLossHandle(PrpLDlossCarMainVo lossCarMainVo, WfTaskSubmitVo submitVo) {
		String handleIdKey = submitVo.getHandleIdKey();
		FlowNode currentNode = submitVo.getCurrentNode();
		FlowNode nextNode = submitVo.getNextNode();
		// 获得回退时上次处理已完成的节点
		PrpLWfTaskVo taskBackVo = wfTaskService.findBackOutVo(handleIdKey,submitVo.getFlowId(),nextNode);

		PrpLWfTaskVo taskInVo = new PrpLWfTaskVo();
		super.setTaskBackInVo(taskInVo,submitVo,nextNode,taskBackVo);
		taskInVo.setRemark(currentNode.name()+" Back To "+nextNode.name());
		// 处理业务标记和扩展XML显示内容
		
		if(lossCarMainVo.getSerialNo()>1){
			lossCarMainVo.setSerialNo(3);
		}
		TaskExtMapUtils extMapUtil = new TaskExtMapUtils();
		extMapUtil.addXmlMap(lossCarMainVo.getLossCarInfoVo(),"licenseNo","modelName");
		extMapUtil.addXmlMap(lossCarMainVo, "serialNo","sumLossFee");
		//extMapUtil.getShowInfoXMLMap().put("serialNo",lossCarMainVo.getSerialNo().toString());
		
		taskInVo.setShowInfoXML(extMapUtil.getShowInfoXML());
		//案件紧急程度标示覆盖
		extMapUtil.addTagMap(lossCarMainVo,"mercyFlag");
		taskInVo.setBussTagMap(extMapUtil.getBussTagMap());
		
		wfTaskService.submitTask(submitVo.getFlowTaskId(),taskInVo);
		List<PrpLWfTaskVo> taskInVoList = new ArrayList<PrpLWfTaskVo>();
		taskInVoList.add(taskInVo);
		return taskInVoList;
	}
	
	/** 任务回退处理 */
	public List<PrpLWfTaskVo> backLossHandle(WfTaskSubmitVo submitVo) {
		String handleIdKey = submitVo.getHandleIdKey();
		FlowNode currentNode = submitVo.getCurrentNode();
		FlowNode nextNode = submitVo.getNextNode();
		// 获得回退时上次处理已完成的节点
		PrpLWfTaskVo taskBackVo = wfTaskService.findBackOutVo(handleIdKey,submitVo.getFlowId(),nextNode);

		PrpLWfTaskVo taskInVo = new PrpLWfTaskVo();
		super.setTaskBackInVo(taskInVo,submitVo,nextNode,taskBackVo);
		taskInVo.setRemark(currentNode.name()+" Back To "+nextNode.name());
		if(FlowNode.DLProp.equals(nextNode)||FlowNode.DLPropMod.equals(nextNode)
				||FlowNode.DLPropAdd.equals(nextNode)){//退回定损
			taskInVo.setShowInfoXML(taskBackVo.getShowInfoXML());
		}else if(FlowNode.VLProp.equals(nextNode.getUpperNode())){//核损退回
			taskInVo.setShowInfoXML(taskBackVo.getShowInfoXML());
		}else if(FlowNode.PLNext.equals(nextNode)){//退回人伤后续跟踪
			taskInVo.setAssignUser(taskBackVo.getHandlerUser());
			taskInVo.setAssignCom(taskBackVo.getHandlerCom());
			taskInVo.setHandlerIdKey(taskBackVo.getHandlerIdKey());
		}

		wfTaskService.submitTask(submitVo.getFlowTaskId(),taskInVo);
		List<PrpLWfTaskVo> taskInVoList = new ArrayList<PrpLWfTaskVo>();
		taskInVoList.add(taskInVo);
		return taskInVoList;
	}
	
	/**
	 * 核损通过发起损余回收任务
	 * @param lossCarMainVo
	 * @param lossPropMainVo
	 * @param submitVo
	 * @return
	 */
	private PrpLWfTaskVo recLossSubmit(PrpLDlossCarMainVo lossCarMainVo,PrpLdlossPropMainVo lossPropMainVo,WfTaskSubmitVo submitVo,PrpLWfTaskVo taskInVo){
		WfSimpleTaskVo taskVo = new WfSimpleTaskVo();
		PrpLRecLossVo prpLRecLossVo = null;
		if(lossCarMainVo != null){
			taskVo.setRegistNo(lossCarMainVo.getRegistNo());
			taskVo.setItemName(lossCarMainVo.getLossCarInfoVo().getLicenseNo());
			prpLRecLossVo = recLossService.findPrpLRecLoss(lossCarMainVo.getRegistNo(), "1", lossCarMainVo.getId());
			if(prpLRecLossVo != null){
				prpLRecLossVo = recLossService.initUpdateCarRecLossInfo(lossCarMainVo, prpLRecLossVo, submitVo.getTaskInUser());
			}else{
				prpLRecLossVo = recLossService.createCarPrpLRecLossVo(lossCarMainVo,submitVo.getTaskInUser());
			}
			if(prpLRecLossVo == null){
				return null;
			}
			taskVo.setHandlerIdKey(prpLRecLossVo.getPrpLRecLossId());//损余回收ID
			submitVo.setNextNode(FlowNode.RecLossCar);//车损损余回收
		}else if(lossPropMainVo != null){
			taskVo.setRegistNo(lossPropMainVo.getRegistNo());
			taskVo.setItemName(lossPropMainVo.getLicense());
			prpLRecLossVo = recLossService.findPrpLRecLoss(lossPropMainVo.getRegistNo(), "2", lossPropMainVo.getId());
			if(prpLRecLossVo != null){
				prpLRecLossVo = recLossService.initUpdatePropRecLossInfo(lossPropMainVo, prpLRecLossVo, submitVo.getTaskInUser());
			}else{
				prpLRecLossVo = recLossService.createPropPrpLRecLossVo(lossPropMainVo,submitVo.getTaskInUser());
			}
			if(prpLRecLossVo == null){
				return null;
			}
			taskVo.setHandlerIdKey(prpLRecLossVo.getPrpLRecLossId());//损余回收ID
			submitVo.setNextNode(FlowNode.RecLossProp);//物损损余回收
		}
		
		//案件标示
		PrpLWfTaskVo prpLWfTask = wfTaskService.queryTask(submitVo.getFlowTaskId().doubleValue());
		taskVo.setBussTag(prpLWfTask.getBussTag());
		taskVo.setShowInfoXml(taskInVo.getShowInfoXML());
		
		//损余回收到岗
		submitVo.setAssignCom(submitVo.getComCode());
		submitVo.setAssignUser("");
		
		PrpLWfTaskVo prpLWfTaskVo = wfSimpleTaskService.addSimpleTask(taskVo,submitVo);
		
		recLossService.saveOrUpdate(prpLRecLossVo);
		return prpLWfTaskVo;
	}

	
}
