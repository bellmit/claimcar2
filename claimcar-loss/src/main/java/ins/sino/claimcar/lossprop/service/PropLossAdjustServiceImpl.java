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
			return "?????????????????????????????????????????????";
		}
		//???????????????????????????????????????????????????????????? TODO
		
		//????????????????????????????????????????????????????????? 
		if(taskHandleService.existTaskByNodeCode(lossPropMainVo.getRegistNo(),
				FlowNode.Compe,"","0")){
			return "?????????????????????????????????????????????????????????";
		}else{
			List<PrpLCompensateVo> compensateList = compensateTaskService.findCompByRegistNo(lossPropMainVo.getRegistNo());
			for(PrpLCompensateVo compensateVo : compensateList){
				if(UnderWriteFlag.NORMAL.equals(compensateVo.getUnderwriteFlag()) 
						|| UnderWriteFlag.WAIT_UNDERWRITE.equals(compensateVo.getUnderwriteFlag())
						|| UnderWriteFlag.BACK_UNDERWRITE.equals(compensateVo.getUnderwriteFlag())){
					return "??????????????????????????????????????????????????????";
				}
			}
		}
		
		//????????????????????????????????????????????????
		List<PrpLClaimVo> claimList = claimTaskService.findClaimListByRegistNo(lossPropMainVo.getRegistNo()); 
		boolean isNoEndCase = false;
		for(PrpLClaimVo claimVo : claimList){
			if(claimVo.getEndCaseTime()==null){
				isNoEndCase = true;
				break;
			}
		}
		
		if(!isNoEndCase){
			return "??????????????????????????????????????????";
		}
		//??????????????????????????????????????????????????????????????????
		boolean isNoValidFlag= false;
		for(PrpLClaimVo claimVo : claimList){
			if("1".equals(claimVo.getValidFlag())){
				isNoValidFlag = true;
				break;
			}
		}
		if(!isNoValidFlag){
			return "????????????????????????????????????????????????????????????";
		}
		//??????????????????????????? taskId
		List<PrpLWfTaskVo> taskVoList = taskHandleService.findEndTask(registNo, lossId.toString(), FlowNode.VLProp);
		if(taskVoList == null || taskVoList.isEmpty()){
			return "????????????";
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
		//???????????????
		this.submitNextTask(lossPropMainVo, taskVo, FlowNode.DLPropMod, sysUserVo,lossId);
		//???????????????????????????
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
		
		//?????????
		List<PrpLClaimVo> vos = claimService.findClaimListByRegistNo(registNo);
		if(vos!=null && vos.size()>0){
			for(PrpLClaimVo vo : vos){
				if(vo.getEndCaserCode() == null && vo.getCaseNo() == null){
					
					Map<String,String> queryMap = new HashMap<String,String>();
					queryMap.put("registNo", registNo);
					queryMap.put("claimNo", vo.getClaimNo());
					queryMap.put("checkStatus", "6");//????????????
					//????????????????????????????????? ???????????????
					List<PrpLReCaseVo> prpLReCaseVoList = endCasePubService.findReCaseVoListByqueryMap(queryMap);
					if(prpLReCaseVoList!=null && prpLReCaseVoList.size()>0){//??????
						PrpLClaimVo prpLClaimVo = claimService.findClaimVoByClaimNo(prpLReCaseVoList.get(0).getClaimNo());
						String riskCode = prpLClaimVo.getRiskCode().substring(0, 2);
						String subNodeCode = "";
						if("12".equals(riskCode)){
							subNodeCode = FlowNode.CompeBI.toString();
						}else{
							subNodeCode = FlowNode.CompeCI.toString();
						}
						//in???
						List<PrpLWfTaskVo>  inList = wfFlowQueryService.findPrpWfTaskVo(registNo, FlowNode.Compe.toString());
						for(PrpLWfTaskVo wfTaskVo : inList){
							if(subNodeCode.equals(wfTaskVo.getSubNodeCode())){
								return "??????????????????????????????????????????????????????";
							}
						}
						//out???
						String flags = "1";
						String newestCompNo = "";
						Map<String, String> compeUnderMap = compensateTaskService.getCompUnderWriteFlagByRegNo(registNo);
						PrpLWfTaskVo WfQueryVo = new PrpLWfTaskVo();
						WfQueryVo.setRegistNo(registNo);
						WfQueryVo.setSubNodeCode(subNodeCode);
						WfQueryVo.setWorkStatus(CodeConstants.WorkStatus.END);
						List<PrpLWfTaskVo>  outList = wfFlowQueryService.findTaskVoForQueryVo(WfQueryVo, RadioValue.RADIO_YES);
						if(outList!=null && outList.size()>0){
							for(int i = 0;i<outList.size();i++){//???????????????????????????????????????????????????????????????
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
							return "??????????????????????????????????????????????????????";
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
			return "??????????????????????????????????????????";
		}
		
		if(taskHandleService.existTaskByNodeCode(lossPropMainVo.getRegistNo(),
				FlowNode.DLPropAdd,lossPropMainVo.getId().toString(),"0")){
			return "????????????????????????????????????";
		}
		
		//???????????? ????????????????????????????????????
		List<PrpLdlossPropMainVo> propList = propLossService.findLossPropNoComp(registNo, lossPropMainVo.getSerialNo());
		if(propList!=null && !propList.isEmpty()){
			return "????????????????????????????????????????????????????????????";
		}
		
		Map<String,String> queryMap = new HashMap<String,String>();
		queryMap.put("registNo", registNo);
		queryMap.put("checkStatus", "6");//????????????
		//????????????????????????????????? ???????????????
		List<String> claimNoList = endCasePubService.findPrpLReCaseVoList(queryMap);
		if(claimNoList == null || claimNoList.isEmpty()){
			return "??????????????????????????????????????????";
		}
		
		//?????????????????????????????? taskId
		PrpLWfTaskVo taskVo = taskHandleService.findEndTask(registNo, lossPropMainVo.getId().toString(), FlowNode.DLProp).get(0);
		//????????????????????? ???????????????
		Long businessId = this.saveScheduleDefLossTask(lossPropMainVo,userVo,itemContent,remark);
		//deflossVo.setBusinessId(businessId);
		//?????????????????????
		this.submitNextTask(lossPropMainVo,taskVo,FlowNode.DLPropAdd,userVo,businessId);
		
		lossPropMainVo.setReLossPropId(-1L);//?????????1????????????????????????,???????????????????????????
		propLossService.updatePropMain(lossPropMainVo);
		
		return retStr;
	}
	
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.lossprop.service.PropLossAdjustService#saveScheduleDefLossTask(ins.sino.claimcar.lossprop.vo.PrpLdlossPropMainVo, ins.platform.vo.SysUserVo, java.lang.String, java.lang.String)
	 */
	@Override
	public Long saveScheduleDefLossTask(PrpLdlossPropMainVo lossPropMainVo,SysUserVo userVo,String itemContent, String remark){
		PrpLScheduleDefLossVo scheduleDefLossVo = scheduleService.findScheduleDefLossByPk(lossPropMainVo.getScheduleTaskId());
		//?????????????????????copy?????????
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
		
		//??????scheduleTaskVo??????
		PrpLScheduleTaskVo scheduleTaskVo = new PrpLScheduleTaskVo();
		scheduleTaskVo.setRegistNo(lossPropMainVo.getRegistNo());
		scheduleTaskVo.setScheduledTime(new Date());
		scheduleTaskVo.setScheduleType(CodeConstants.ScheduleType.DEFLOSS_SCHEDULE);
		scheduleTaskVo.setOperatorCode(userVo.getUserCode());
		scheduleTaskVo.setRemark("????????????");
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
		//??????????????????
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
