package ins.sino.claimcar.lossprop.service;

import ins.framework.utils.Beans;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
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
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropMainVo;
import ins.sino.claimcar.schedule.service.ScheduleService;
import ins.sino.claimcar.schedule.vo.PrpLScheduleDefLossVo;
import ins.sino.claimcar.schedule.vo.PrpLScheduleTaskVo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Path;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"}, timeout = 1000000)
@Path("propLossAdjustService")
public class PropLossAdjustServiceImpl implements PropLossAdjustService {
	@Autowired
	private PropLossService propLossService;
	@Autowired
	private WfTaskHandleService taskHandleService;
	@Autowired
	private ScheduleService scheduleService;
	@Autowired
	private CompensateTaskService compensateTaskService;
	@Autowired
	private EndCasePubService endCasePubService;
	@Autowired
	WfFlowQueryService wfFlowQueryService;
	@Autowired
	ClaimTaskService claimService;
	@Autowired 
	private ClaimTaskService claimTaskService;
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.lossprop.service.PropLossAdjustService#propModifyLaunch(java.lang.Long, ins.platform.vo.SysUserVo)
	 */
	@Override
	public String propModifyLaunch(Long lossId,SysUserVo sysUserVo){
		String retStr ="ok"; 
		PrpLdlossPropMainVo lossPropMainVo=propLossService.findPropMainVoById(lossId);
		String registNo=lossPropMainVo.getRegistNo();
		
		if(taskHandleService.existTaskByNodeCode(registNo,FlowNode.DLPropMod,lossPropMainVo.getId().toString(),"0")){
			return "已发起定损修改任务，请刷新页面";
		}
		//有损余回收任务未处理完，不能发起定损修改 TODO
		
		//有正在处理的理算任务，不能发起定损修改 
		if(taskHandleService.existTaskByNodeCode(lossPropMainVo.getRegistNo(),
				FlowNode.Compe,"","0")){
			return "有正在处理的理算任务，不能发起定损修改";
		}else{
			List<PrpLCompensateVo> compensateList = compensateTaskService.findCompByRegistNo(lossPropMainVo.getRegistNo());
			for(PrpLCompensateVo compensateVo : compensateList){
				if(UnderWriteFlag.NORMAL.equals(compensateVo.getUnderwriteFlag()) 
						|| UnderWriteFlag.WAIT_UNDERWRITE.equals(compensateVo.getUnderwriteFlag())
						|| UnderWriteFlag.BACK_UNDERWRITE.equals(compensateVo.getUnderwriteFlag())){
					return "有未核赔的理算任务，不能发起定损修改";
				}
			}
		}
		
		//已结案未重开，不允许发起定损修改
		List<PrpLClaimVo> claimList = claimTaskService.findClaimListByRegistNo(lossPropMainVo.getRegistNo()); 
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
		//取最后一个核损任务 taskId
		List<PrpLWfTaskVo> taskVoList = taskHandleService.findEndTask(registNo, lossId.toString(), FlowNode.VLProp);
		if(taskVoList == null || taskVoList.isEmpty()){
			return "数据异常";
		}
		PrpLWfTaskVo taskVo = taskVoList.get(0);
		/*deflossVo.setBusinessId(lossId);*/
		FlowNode oldFlowNode = FlowNode.DLProp;
		if(CodeConstants.defLossSourceFlag.MODIFYDEFLOSS.equals(lossPropMainVo.getDeflossSourceFlag())) {
			oldFlowNode = FlowNode.DLPropMod;
		}else if(CodeConstants.defLossSourceFlag.ADDTIONALDEFLOSS.equals(lossPropMainVo.getDeflossSourceFlag())){
			oldFlowNode = FlowNode.DLPropAdd;
		}
		PrpLWfTaskVo defTaskVo = taskHandleService.findEndTask(registNo, lossId.toString(),oldFlowNode).get(0);

		taskVo.setHandlerUser(defTaskVo.getHandlerUser());
		taskVo.setHandlerCom(defTaskVo.getHandlerCom());
		//发起工作流
		this.submitNextTask(lossPropMainVo, taskVo, FlowNode.DLPropMod, sysUserVo,lossId);
		//重置定损核损标志位
		lossPropMainVo.setUnderWriteFlag(CodeConstants.VeriFlag.INIT);
		lossPropMainVo.setDeflossSourceFlag(CodeConstants.defLossSourceFlag.MODIFYDEFLOSS);
		propLossService.saveOrUpdatePropMain(lossPropMainVo,sysUserVo);
		
		return retStr;
	}
	
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.lossprop.service.PropLossAdjustService#propAdditionLaunch(java.lang.Long, ins.platform.vo.SysUserVo, java.lang.String, java.lang.String)
	 */
	@Override
	public String propAdditionLaunch(Long id,SysUserVo userVo,String itemContent, String remark) {
		String retStr ="ok";
		PrpLdlossPropMainVo lossPropMainVo = propLossService.findPropMainVoById(id);
		String registNo = lossPropMainVo.getRegistNo();
		
		//加管控
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
								return "已存在理算任务，不能发起财产追加定损";
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
							return "已存在理算任务，不能发起财产追加定损";
						}
					}
					
				}
			}
	//}
	}
		List<PrpLClaimVo> claimList = claimTaskService.findClaimListByRegistNo(lossPropMainVo.getRegistNo()); 
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
		
		if(taskHandleService.existTaskByNodeCode(lossPropMainVo.getRegistNo(),
				FlowNode.DLPropAdd,lossPropMainVo.getId().toString(),"0")){
			return "已发起财产追加定损任务！";
		}
		
		//如果存在 未理算的定损任务不能追加
		List<PrpLdlossPropMainVo> propList = propLossService.findLossPropNoComp(registNo, lossPropMainVo.getSerialNo());
		if(propList!=null && !propList.isEmpty()){
			return "存在未理算的定损任务，不能发起追加任务！";
		}
		
		Map<String,String> queryMap = new HashMap<String,String>();
		queryMap.put("registNo", registNo);
		queryMap.put("checkStatus", "6");//审核通过
		//查找审核通过的重开赔案 立案号列表
		List<String> claimNoList = endCasePubService.findPrpLReCaseVoList(queryMap);
		if(claimNoList == null || claimNoList.isEmpty()){
			return "只有重开赔案后才能追加定损！";
		}
		
		//取最新的一个定损任务 taskId
		PrpLWfTaskVo taskVo = taskHandleService.findEndTask(registNo, lossPropMainVo.getId().toString(), FlowNode.DLProp).get(0);
		//重新生成调度表 定损调度表
		Long businessId = this.saveScheduleDefLossTask(lossPropMainVo,userVo,itemContent,remark);
		//deflossVo.setBusinessId(businessId);
		//发起工作流任务
		this.submitNextTask(lossPropMainVo,taskVo,FlowNode.DLPropAdd,userVo,businessId);
		
		lossPropMainVo.setReLossPropId(-1L);//借用为1则发起了定损任务,追加核损通过后清空
		propLossService.updatePropMain(lossPropMainVo);
		
		return retStr;
	}
	
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.lossprop.service.PropLossAdjustService#saveScheduleDefLossTask(ins.sino.claimcar.lossprop.vo.PrpLdlossPropMainVo, ins.platform.vo.SysUserVo, java.lang.String, java.lang.String)
	 */
	@Override
	public Long saveScheduleDefLossTask(PrpLdlossPropMainVo lossPropMainVo,SysUserVo userVo,String itemContent, String remark){
		PrpLScheduleDefLossVo scheduleDefLossVo = scheduleService.findScheduleDefLossByPk(lossPropMainVo.getScheduleTaskId());
		//从原定损调度表copy到新的
		PrpLScheduleDefLossVo scheduleDefVo = Beans.copyDepth().from(scheduleDefLossVo).to(PrpLScheduleDefLossVo.class);
		scheduleDefVo.setAddDeflossId(lossPropMainVo.getId());
		scheduleDefVo.setRemark(remark);
		scheduleDefVo.setItemsContent(itemContent);
		scheduleDefVo.setSourceFlag(CodeConstants.ScheduleDefSource.SCHEDULEADD);
		scheduleDefVo.setUpdateTime(null);
		scheduleDefVo.setUpdateUser(null);
		scheduleDefVo.setCreateTime(new Date());
		scheduleDefVo.setCreateUser(userVo.getUserCode());
		scheduleDefVo.setId(null);
		
		//组织scheduleTaskVo数据
		PrpLScheduleTaskVo scheduleTaskVo = new PrpLScheduleTaskVo();
		scheduleTaskVo.setRegistNo(lossPropMainVo.getRegistNo());
		scheduleTaskVo.setScheduledTime(new Date());
		scheduleTaskVo.setScheduleType(CodeConstants.ScheduleType.DEFLOSS_SCHEDULE);
		scheduleTaskVo.setOperatorCode(userVo.getUserCode());
		scheduleTaskVo.setRemark("追加定损");
		scheduleTaskVo.setScheduledUsercode(lossPropMainVo.getHandlerCode());
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
	
	private PrpLWfTaskVo submitNextTask(PrpLdlossPropMainVo lossPropMainVo,PrpLWfTaskVo taskVo,FlowNode nextFlowNode,SysUserVo userVo,Long id) {
		WfTaskSubmitVo taskSubmitVo = new WfTaskSubmitVo();
		taskSubmitVo.setCurrentNode(FlowNode.valueOf(taskVo.getNodeCode()));
		taskSubmitVo.setFlowId(taskVo.getFlowId());
		taskSubmitVo.setFlowTaskId(taskVo.getTaskId());
		taskSubmitVo.setNextNode(nextFlowNode);
		taskSubmitVo.setComCode(userVo.getComCode());
		taskSubmitVo.setTaskInUser(userVo.getUserCode());
		taskSubmitVo.setTaskInKey(lossPropMainVo.getId().toString());
		//指定原出来人
		taskSubmitVo.setAssignCom(taskVo.getHandlerCom());
		taskSubmitVo.setAssignUser(taskVo.getHandlerUser());
		
		WfSimpleTaskVo simpleTaskVo = new WfSimpleTaskVo();
		simpleTaskVo.setBussTag(taskVo.getBussTag());
		//simpleTaskVo.setShowInfoXml(taskVo.getShowInfoXML());
		simpleTaskVo.setRegistNo(lossPropMainVo.getRegistNo());
		simpleTaskVo.setItemName(taskVo.getItemName());
		simpleTaskVo.setHandlerIdKey(id.toString());
		
//		simpleTaskVo.setBussTag(bussTag);
		simpleTaskVo.setShowInfoXml(taskVo.getShowInfoXML());

		PrpLWfTaskVo wfTaskVo = taskHandleService.addSimpleTask(simpleTaskVo,taskSubmitVo);
		
		return wfTaskVo;
	}	

}
