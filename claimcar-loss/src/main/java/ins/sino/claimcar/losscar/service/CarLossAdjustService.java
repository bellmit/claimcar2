package ins.sino.claimcar.losscar.service;

import ins.framework.utils.Beans;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.HandlerStatus;
import ins.sino.claimcar.CodeConstants.RadioValue;
import ins.sino.claimcar.CodeConstants.UnderWriteFlag;
import ins.sino.claimcar.claim.service.ClaimTaskService;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.endcase.service.EndCasePubService;
import ins.sino.claimcar.endcase.vo.PrpLReCaseVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.WfFlowQueryService;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.flow.vo.WfSimpleTaskVo;
import ins.sino.claimcar.flow.vo.WfTaskSubmitVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarInfoVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.schedule.service.ScheduleService;
import ins.sino.claimcar.schedule.vo.PrpLScheduleDefLossVo;
import ins.sino.claimcar.schedule.vo.PrpLScheduleTaskVo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("carLossAdjustService")
public class CarLossAdjustService {
	@Autowired
	private DeflossService deflossService;
	@Autowired
	private WfTaskHandleService taskHandleService;
	@Autowired
	private ScheduleService scheduleService;
	
	@Autowired
	private CompensateTaskService compensateTaskService;
	@Autowired 
	private ClaimTaskService claimTaskService;
	
	@Autowired
	private EndCasePubService endCasePubService;
		
	@Autowired
	WfFlowQueryService wfFlowQueryService;
	@Autowired
	ClaimTaskService claimService;
	/**
	 * 车辆修改定损发起任务
	 * ☆yangkun(2016年2月4日 下午2:29:33): <br>
	 * @param deflossVo 
	 */
	public String carModifyLaunch(Long lossId, SysUserVo userVo){
		String retStr ="ok"; 
		PrpLDlossCarMainVo lossCarMainVo = deflossService.findDeflossByPk(lossId);
		PrpLDlossCarInfoVo carInfoVo = deflossService.findDefCarInfoByPk(lossCarMainVo.getCarId());
		lossCarMainVo.setLossCarInfoVo(carInfoVo);
		
		if(taskHandleService.existTaskByNodeCode(lossCarMainVo.getRegistNo(),
				FlowNode.DLCarMod,lossCarMainVo.getId().toString(),"0")){
			return "已发起定损修改任务，请刷新页面";
		}
		
		//有正在处理的理算任务，不能发起定损修改 
		if(taskHandleService.existTaskByNodeCode(lossCarMainVo.getRegistNo(),
				FlowNode.Compe,"","0")){
			return "有正在处理的理算任务，不能发起定损修改";
		}else{
			List<PrpLCompensateVo> compensateList = compensateTaskService.findCompByRegistNo(lossCarMainVo.getRegistNo());
			for(PrpLCompensateVo compensateVo : compensateList){
				if(UnderWriteFlag.NORMAL.equals(compensateVo.getUnderwriteFlag()) 
						|| UnderWriteFlag.WAIT_UNDERWRITE.equals(compensateVo.getUnderwriteFlag())
						|| UnderWriteFlag.BACK_UNDERWRITE.equals(compensateVo.getUnderwriteFlag())){
					return "有未核赔的理算任务，不能发起定损修改";
				}
			}
		}
		
		//已结案未重开，不允许发起定损修改
		List<PrpLClaimVo> claimList = claimTaskService.findClaimListByRegistNo(lossCarMainVo.getRegistNo()); 
		boolean isNoEndCase = false;
		for(PrpLClaimVo claimVo : claimList){
			if(claimVo.getEndCaseTime()==null){
				isNoEndCase = true;
				break;
			}
		}
		
		if(!isNoEndCase){
			return "该案件已结案，请先重开赔案！";
		}
		//所有立案已注销或拒赔的案件，不能发起定损修改
		boolean isNoValidFlag= false;
		for(PrpLClaimVo claimVo : claimList){
			if("1".equals(claimVo.getValidFlag())){
				isNoValidFlag = true;
				break;
			}
		}
		if(!isNoValidFlag){
			return "立案已注销或拒赔的案件，不能发起定损修改";
		}
		
		String registNo = lossCarMainVo.getRegistNo();
		//取最后一个核损任务 taskId
		List<PrpLWfTaskVo> taskVoList = taskHandleService.findEndTask(registNo, lossId.toString(), FlowNode.VLCar);
		if(taskVoList == null || taskVoList.isEmpty()){
			return "数据异常";
		}
		PrpLWfTaskVo taskVo = taskVoList.get(0);
		//处理人员 用原定损人员处理		
		String flowFlag = lossCarMainVo.getFlowFlag() ;//DLChk
		if(FlowNode.DLChk.name().equals(flowFlag)){//如果是复检，直接赋值定损
			flowFlag = FlowNode.DLCar.name();
		}
		PrpLWfTaskVo defTaskVo = taskHandleService.findEndTask(registNo, lossId.toString(), FlowNode.valueOf(flowFlag)).get(0);
		taskVo.setHandlerUser(defTaskVo.getHandlerUser());
		taskVo.setHandlerCom(defTaskVo.getHandlerCom());
		//发起工作流任务
		this.submitNextTask(lossCarMainVo,taskVo,FlowNode.DLCarMod,userVo,lossCarMainVo.getId());
		//重置定损核价核损标志位
		lossCarMainVo.setVeriPriceFlag(CodeConstants.VeriFlag.INIT);
		lossCarMainVo.setUnderWriteFlag(CodeConstants.VeriFlag.INIT);
		lossCarMainVo.setDeflossSourceFlag(CodeConstants.defLossSourceFlag.MODIFYDEFLOSS);
		deflossService.updateDefloss(lossCarMainVo);
		
		return retStr;
	}
	
	/**
	 * 发起工作流任务
	 * ☆yangkun(2016年2月4日 下午2:29:33): <br>
	 */
	private PrpLWfTaskVo submitNextTask(PrpLDlossCarMainVo lossCarMainVo,PrpLWfTaskVo taskVo,FlowNode nextFlowNode,SysUserVo userVo,Long bussinessNo) {
		WfTaskSubmitVo taskSubmitVo = new WfTaskSubmitVo();
		taskSubmitVo.setCurrentNode(FlowNode.valueOf(taskVo.getNodeCode()));
		taskSubmitVo.setFlowId(taskVo.getFlowId());
		taskSubmitVo.setFlowTaskId(taskVo.getTaskId());
		taskSubmitVo.setNextNode(nextFlowNode);
		taskSubmitVo.setComCode(userVo.getComCode());
		taskSubmitVo.setTaskInUser(userVo.getUserCode());
		taskSubmitVo.setTaskInKey(lossCarMainVo.getId().toString());
		//指定原处理人
		taskSubmitVo.setAssignCom(taskVo.getHandlerCom());
		taskSubmitVo.setAssignUser(taskVo.getHandlerUser());
		
		WfSimpleTaskVo simpleTaskVo = new WfSimpleTaskVo();
		simpleTaskVo.setRegistNo(lossCarMainVo.getRegistNo());
		simpleTaskVo.setBussTag(taskVo.getBussTag());
		//simpleTaskVo.setShowInfoXml(taskVo.getShowInfoXML());
		simpleTaskVo.setHandlerIdKey(bussinessNo.toString());
		simpleTaskVo.setItemName(taskVo.getItemName());

		PrpLWfTaskVo wfTaskVo = taskHandleService.addSimpleTask(simpleTaskVo,taskSubmitVo);
		
		return wfTaskVo;
	}	
	
	/**
	 * 存在是否处理的任务 需要考虑修改
	 * @modified:
	 * ☆yangkun(2016年2月4日 下午2:33:04): <br>
	 */
	public Boolean checkExistsLoss(Long lossId){
		PrpLWfTaskVo taskVo = taskHandleService.queryTask(new Double(lossId));
		if(CodeConstants.HandlerStatus.INIT.equals(taskVo.getHandlerStatus())||
			CodeConstants.HandlerStatus.START.equals(taskVo.getHandlerStatus())||
			CodeConstants.HandlerStatus.DOING.equals(taskVo.getHandlerStatus())){
			return false;
		}
		
		return false;
	}

	/**
	 * 车辆追加定损发起任务
	 * ☆yangkun(2016年2月4日 下午2:29:33): <br>
	 * @param deflossVo 
	 */
	public String carAdditionLaunch(Long lossId, SysUserVo userVo) {
		String retStr ="ok";
		PrpLDlossCarMainVo lossCarMainVo = deflossService.findDeflossByPk(lossId);
		PrpLDlossCarInfoVo carInfoVo = deflossService.findDefCarInfoByPk(lossCarMainVo.getCarId());
		lossCarMainVo.setLossCarInfoVo(carInfoVo);		
		String registNo = lossCarMainVo.getRegistNo();
		//加管控
	/*	List<PrpLEndCaseVo> listVo = endCasePubService.queryAllByRegistNo(registNo);
		if(listVo!=null && listVo.size()>0){*/
			List<PrpLClaimVo> vos = claimService.findClaimListByRegistNo(registNo);
			if(vos!=null && vos.size()>0){
				for(PrpLClaimVo vo : vos){
					if(vo.getEndCaserCode() == null && vo.getCaseNo() == null){
						
						Map<String,String> queryMap = new HashMap<String,String>();
						queryMap.put("registNo", registNo);
						queryMap.put("claimNo", vo.getClaimNo());
						queryMap.put("checkStatus", "6");//审核通过
						//查找审核通过的重开赔案 立案号列表
						List<PrpLReCaseVo> prpLReCaseVoList = endCasePubService.findReCaseVoListByqueryMap(queryMap);
						if(prpLReCaseVoList!=null && prpLReCaseVoList.size()>0){//重开
							PrpLClaimVo prpLClaimVo = claimService.findClaimVoByClaimNo(prpLReCaseVoList.get(0).getClaimNo());
							String riskCode = prpLClaimVo.getRiskCode().substring(0, 2);
							String subNodeCode = "";
							if("12".equals(riskCode)){
								subNodeCode = FlowNode.CompeBI.toString();
							}else{
								subNodeCode = FlowNode.CompeCI.toString();
							}
							//in表
							List<PrpLWfTaskVo>  inList = wfFlowQueryService.findPrpWfTaskVo(registNo, FlowNode.Compe.toString());
							for(PrpLWfTaskVo wfTaskVo : inList){
								if(subNodeCode.equals(wfTaskVo.getSubNodeCode())){
									return "已存在理算任务，不能发起车辆追加定损";
								}
							}
							//out表
							String flags = "1";
							String newestCompNo = "";
							Map<String, String> compeUnderMap = compensateTaskService.getCompUnderWriteFlagByRegNo(registNo);
							PrpLWfTaskVo WfQueryVo = new PrpLWfTaskVo();
							WfQueryVo.setRegistNo(registNo);
							WfQueryVo.setSubNodeCode(subNodeCode);
							WfQueryVo.setWorkStatus(CodeConstants.WorkStatus.END);
							List<PrpLWfTaskVo>  outList = wfFlowQueryService.findTaskVoForQueryVo(WfQueryVo, RadioValue.RADIO_YES);
							if(outList!=null && outList.size()>0){
								for(int i = 0;i<outList.size();i++){//获取最新有效计算书号需要排除已注销的计算书
									if(!UnderWriteFlag.CANCELFLAG.equals(compeUnderMap.get(outList.get(i).getCompensateNo()))){
										newestCompNo = outList.get(i).getCompensateNo();
										break;
									}
								}
								for(PrpLReCaseVo prpLReCaseVo : prpLReCaseVoList){
									if(prpLReCaseVo.getCompensateNo().equals(newestCompNo)){
										flags = "0";
									}
								}
							}
							if("1".equals(flags)){
								return "已存在理算任务，不能发起车辆追加定损";
							}
						}
						
					}
				}
		//}
		}
				
		List<PrpLClaimVo> claimList = claimTaskService.findClaimListByRegistNo(lossCarMainVo.getRegistNo()); 
		boolean isNoEndCase = false;
		for(PrpLClaimVo claimVo : claimList){
			if(claimVo.getEndCaseTime()==null){
				isNoEndCase = true;
				break;
			}
		}
		
		if(!isNoEndCase){
			return "该案件已结案，请先重开赔案！";
		}
		
		Map<String,String> queryMap = new HashMap<String,String>();
		queryMap.put("registNo", registNo);
		queryMap.put("checkStatus", "6");//审核通过
		//查找审核通过的重开赔案 立案号列表
		List<String> claimNoList = endCasePubService.findPrpLReCaseVoList(queryMap);
		if(claimNoList == null || claimNoList.isEmpty()){
			return "只有重开赔案后才能追加定损！";
		}
		
		if(taskHandleService.existTaskByNodeCode(lossCarMainVo.getRegistNo(),
				FlowNode.DLCarAdd,lossCarMainVo.getId().toString(),"0")){
			return "已发起车辆追加定损任务！";
		}
		//如果存在 未理算的定损任务不能追加
		List<PrpLDlossCarMainVo> carList = deflossService.findLossCarNoComp(registNo, lossCarMainVo.getSerialNo());
		if(carList!=null && !carList.isEmpty()){
			return "存在未理算的定损任务，不能发起追加任务！";
		}
		
		//取最新的一个定损任务 taskId
		List<PrpLWfTaskVo> taskList = taskHandleService.findEndTask(registNo, lossId.toString(), FlowNode.DLCar);
		PrpLWfTaskVo taskVo = taskList.get(taskList.size()-1);//取第一个定损任务吧
		//重新生成调度表 定损调度表
		Long businessId = this.saveScheduleDefLossTask(lossCarMainVo,userVo);
		//发起工作流任务
		this.submitNextTask(lossCarMainVo,taskVo,FlowNode.DLCarAdd,userVo,businessId);
		
		lossCarMainVo.setReLossCarId(-1L);//借用为-1则发起了定损任务,追加核损通过后清空
		deflossService.updateDefloss(lossCarMainVo);
		
		return retStr;
	}
	
	/**
	 * 重新生成调度表 定损调度表
	 * ☆yangkun(2016年2月4日 下午4:14:03): <br>
	 */
	public Long saveScheduleDefLossTask(PrpLDlossCarMainVo lossCarMainVo,SysUserVo userVo){
		PrpLScheduleDefLossVo scheduleDefLossVo = scheduleService.findScheduleDefLossByPk(lossCarMainVo.getScheduleDeflossId());
		//从原定损调度表copy到新的
		PrpLScheduleDefLossVo scheduleDefVo = Beans.copyDepth().from(scheduleDefLossVo).to(PrpLScheduleDefLossVo.class);
		scheduleDefVo.setAddDeflossId(lossCarMainVo.getId());
		scheduleDefVo.setRemark("追加定损");
		scheduleDefVo.setSourceFlag(CodeConstants.ScheduleDefSource.SCHEDULEADD);
		scheduleDefVo.setUpdateTime(null);
		scheduleDefVo.setUpdateUser(null);
		scheduleDefVo.setCreateTime(new Date());
		scheduleDefVo.setCreateUser(userVo.getUserCode());
		scheduleDefVo.setId(null);
		
		//组织scheduleTaskVo数据
		PrpLScheduleTaskVo scheduleTaskVo = new PrpLScheduleTaskVo();
		scheduleTaskVo.setRegistNo(lossCarMainVo.getRegistNo());
		scheduleTaskVo.setScheduledTime(new Date());
		scheduleTaskVo.setScheduleType(CodeConstants.ScheduleType.DEFLOSS_SCHEDULE);
		scheduleTaskVo.setOperatorCode(userVo.getUserCode());
		scheduleTaskVo.setRemark("追加定损");
		scheduleTaskVo.setScheduledUsercode(lossCarMainVo.getHandlerCode());
		scheduleTaskVo.setCreateTime(new Date());
		scheduleTaskVo.setCreateUser(userVo.getUserCode());
		scheduleTaskVo.setId(null);
		
		List<PrpLScheduleDefLossVo> scheduleDefList = new ArrayList<PrpLScheduleDefLossVo>();
		scheduleDefList.add(scheduleDefVo);
		scheduleTaskVo.setPrpLScheduleDefLosses(scheduleDefList);
		Long schedId = scheduleService.saveScheduleTaskByVo(scheduleTaskVo);
		PrpLScheduleTaskVo scheduleTaskNewVo = scheduleService.findScheduleTaskVoByPK(schedId);
			
		return scheduleTaskNewVo.getPrpLScheduleDefLosses().get(0).getId();
	}
}
