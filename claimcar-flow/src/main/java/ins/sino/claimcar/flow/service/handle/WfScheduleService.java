package ins.sino.claimcar.flow.service.handle;

import ins.platform.common.util.ConfigUtil;
import ins.platform.vo.PrpLConfigValueVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.commom.vo.StringUtils;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.WFMobileService;
import ins.sino.claimcar.flow.service.WfFlowService;
import ins.sino.claimcar.flow.service.WfTaskQueryService;
import ins.sino.claimcar.flow.service.WfTaskService;
import ins.sino.claimcar.flow.util.TaskExtMapUtils;
import ins.sino.claimcar.flow.vo.PrpLWfTaskQueryVo;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.flow.vo.WfTaskSubmitVo;
import ins.sino.claimcar.manager.service.ManagerService;
import ins.sino.claimcar.manager.vo.PrpdIntermMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistCarLossVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.schedule.vo.PrpLScheduleDefLossVo;
import ins.sino.claimcar.schedule.vo.PrpLScheduleItemsVo;
import ins.sino.claimcar.schedule.vo.PrpLScheduleTaskVo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 调度工作流服务处理
 * @author ★LiuPing
 * @CreateTime 2016年1月11日
 */
@Service("wfScheduleService")
public class WfScheduleService extends WfBaseHandleService {
	
	@Autowired
	private WfTaskService wfTaskService;
	@Autowired
	private WfTaskQueryService wfTaskQueryService;
	@Autowired
    ManagerService managerService;
	@Autowired
	WfFlowService wfFlowService;
	@Autowired
	WFMobileService wFMobileService;
	// TODO PrpLRegistVo 可以删除
	public List<PrpLWfTaskVo> submitScheduleHandle(PrpLRegistVo registVo,List<PrpLScheduleTaskVo> scheduleTaskVoList,WfTaskSubmitVo submitVo) {
		/*main更新最新node节点和更新时间，
		 * query表不更新，
		 * node表报案节点更新NodeOutTime时间、NodeStatus状态，node表增加查勘、人伤查勘、定损节点。
		 * in表中的调度任务信息转移到out表中处理完成并删除。
		 *  in表信息增加查勘、人伤查勘、定损信息
		 * */
	
		// PrpLWfTaskQueryVo taskQueryVo = new PrpLWfTaskQueryVo();

		String flowId = submitVo.getFlowId();
		String registNo = null;
		submitVo.setCurrentNode(FlowNode.Sched);
		String isMobileCase = "0";

		// 更新处理完成的主节点信息
		super.updateWfEndNodeVo(flowId,FlowNode.Sched);

		FlowNode nextNode = null;
		// 处理task信息
		List<PrpLWfTaskVo> taskInVoList = new ArrayList<PrpLWfTaskVo>();
		for(PrpLScheduleTaskVo scheduleVo:scheduleTaskVoList){
			registNo = scheduleVo.getRegistNo();

			List<PrpLScheduleItemsVo> scheduleItemsVos = scheduleVo.getPrpLScheduleItemses();

			if("1".equals(scheduleVo.getScheduleType())){// 1：查勘调度
				PrpLWfTaskVo taskInVo = new PrpLWfTaskVo();
				taskInVo.setRegistNo(registNo);
				if( !"1".equals(scheduleVo.getIsPersonFlag())){
					nextNode = FlowNode.Chk;// 普通查勘
					for(PrpLScheduleItemsVo scheduleItemsVo:scheduleItemsVos){
						if("1".equals(scheduleItemsVo.getSerialNo())){// 标的车
							taskInVo.setItemName(scheduleItemsVo.getItemsName());
							taskInVo.setHandlerIdKey(scheduleVo.getId().toString());// 调度到查勘时handlerIdKey取调度标的车损失项的
							//如果是自助查勘 那么将工作流表不指定查勘处理人
							if("1".equals(scheduleVo.getIsAutoCheck())){
								taskInVo.setAssignUser(""); 
							}else{
								taskInVo.setAssignUser(scheduleItemsVo.getScheduledUsercode()); // 调度指定了人，
							}
							taskInVo.setAssignCom(scheduleItemsVo.getScheduledComcode());
							isMobileCase = this.isMobileCase(scheduleItemsVo,registVo,flowId,scheduleVo.getIsAutoCheck());
							break;
						}
					}
				}else{
					nextNode = FlowNode.PLFirst;// 人伤首次跟踪
					// 调度发人伤跟踪时 List<PrpLScheduleItemsVo> 只有一条记录
					for(PrpLScheduleItemsVo scheduleItemsVo:scheduleItemsVos){
						taskInVo.setItemName(scheduleItemsVo.getItemsName());
						taskInVo.setHandlerIdKey(scheduleItemsVo.getId().toString());// 调度到人伤时handlerIdKey取调度子表的
						taskInVo.setAssignUser(scheduleItemsVo.getScheduledUsercode()); // 调度指定了人，
						taskInVo.setAssignCom(scheduleItemsVo.getScheduledComcode());
					}
				}
				registVo.setIsMobileCase(isMobileCase);
				super.setTaskInVo(taskInVo,submitVo,nextNode);
				// 处理业务标记和扩展XML显示内容
				TaskExtMapUtils extMapUtil = new TaskExtMapUtils();
				extMapUtil.addTagMap(registVo.getPrpLRegistExt(),"isClaimSelf","isAlarm","isSubRogation");
				extMapUtil.addTagMap(registVo,"tempRegistFlag","mercyFlag","customerLevel","isMajorCase","tpFlag","isoffSite","qdcaseType","isMobileCase","isQuickCase","selfClaimFlag");
				if(StringUtils.isNotBlank(registVo.getPrpLRegistExt().getLicenseNo())){
                    registVo.setLicense(registVo.getPrpLRegistExt().getLicenseNo());
                }
				extMapUtil.addXmlMap(registVo,"damageAddress","license");
				
				
				taskInVo.setBussTagMap(extMapUtil.getBussTagMap());
				taskInVo.setShowInfoXML(extMapUtil.getShowInfoXML());
				//taskInVo.setShowInfoXML("");
				//判断是否为代查勘案件
				String subCheckFlag = wfTaskQueryService.getSubCheckFlag(registNo, taskInVo.getAssignUser(),scheduleVo.getProvinceCityAreaCode());
				taskInVo.setSubCheckFlag(subCheckFlag);
				taskInVoList.add(taskInVo);
				
				try{
					PrpLWfTaskQueryVo taskQueryVo = wfFlowService.findPrpLWfTaskQueryByFlowId(flowId);
					if(!"0".equals(subCheckFlag)){
						taskQueryVo.setTpFlag("1");
						wfTaskQueryService.update(taskQueryVo, "0");
					}
				}catch(Exception e){
					e.printStackTrace();
				}

				// 初始化保存下一个主节点
				super.saveWfNextNodeVo(submitVo,nextNode);

			}else if("2".equals(scheduleVo.getScheduleType())){// 2：定损调度

				List<PrpLScheduleDefLossVo> defLossVoList = scheduleVo.getPrpLScheduleDefLosses();
				String deflossType = null;// 1-车辆定损，2-财产定损

				for(PrpLScheduleDefLossVo defLossVo:defLossVoList){
					PrpLWfTaskVo taskInVo = new PrpLWfTaskVo();
					taskInVo.setRegistNo(registNo);

					deflossType = defLossVo.getDeflossType();
					if("1".equals(deflossType)){
						nextNode = FlowNode.DLCar;
					}else{
						nextNode = FlowNode.DLProp;
					}
					taskInVo.setHandlerIdKey(defLossVo.getId().toString());
					taskInVo.setItemName(defLossVo.getItemsName());
					
					if(!CodeConstants.ScheduleDefSource.SCHEDULECHECK.equals(defLossVo.getSourceFlag())){
						taskInVo.setItemName(defLossVo.getItemsName()+defLossVo.getItemsContent());
					}
					
					taskInVo.setAssignUser(defLossVo.getScheduledUsercode()); // 调度指定了人，
					taskInVo.setAssignCom(defLossVo.getScheduledComcode());
					
					super.setTaskInVo(taskInVo,submitVo,nextNode);
					// 处理业务标记和扩展XML显示内容
					TaskExtMapUtils extMapUtil = new TaskExtMapUtils();
					extMapUtil.addTagMap(registVo.getPrpLRegistExt(),"isClaimSelf","isAlarm","isSubRogation");
					extMapUtil.addTagMap(registVo,"tempRegistFlag","mercyFlag","customerLevel","isMajorCase","tpFlag","isoffSite","qdcaseType");
					extMapUtil.addXmlMap(registVo,"damageAddress");
					for(PrpLRegistCarLossVo registV:registVo.getPrpLRegistCarLosses()){
						if(registV.getLicenseNo().equals(defLossVo.getLicenseNo())){//比较车牌号
							defLossVo.setModelName(registV.getBrandName());
						}
					}
					defLossVo.setLicense(defLossVo.getLicenseNo());
					if(defLossVo.getSerialNo() == 1||defLossVo.getSerialNo() == 0){
						defLossVo.setSerialNo(defLossVo.getSerialNo());
					}else {
						defLossVo.setSerialNo(3);
					}
					
					extMapUtil.addXmlMap(defLossVo,"serialNo","modelName","license");
					taskInVo.setBussTagMap(extMapUtil.getBussTagMap());
					taskInVo.setShowInfoXML(extMapUtil.getShowInfoXML());
					//taskInVo.setShowInfoXML("");
					//判断是否为代查勘案件
					String subCheckFlag = wfTaskQueryService.getSubCheckFlag(registNo, taskInVo.getAssignUser(),scheduleVo.getProvinceCityAreaCode());
					taskInVo.setSubCheckFlag(subCheckFlag);
					
					taskInVoList.add(taskInVo);
				}
				// 初始化保存下一个主节点
				super.saveWfNextNodeVo(submitVo,nextNode);
			}

		}

		PrpLWfTaskVo[] taskInVos = new PrpLWfTaskVo[taskInVoList.size()];
		// 调度改为正在处理
		wfTaskService.submitTask(submitVo.getFlowTaskId(),taskInVoList.toArray(taskInVos));
		return taskInVoList;
	}
	/**
	 * 判断是否为移动端案件（调度提交时调用）
	 * @param scheduleItemsVo
	 * @param registVo
	 * @param flowId
	 * @return
	 */
	private String isMobileCase(PrpLScheduleItemsVo scheduleItemsVo,PrpLRegistVo registVo,String flowId,String isAutoCheck) {
	    String isMobileCase="0";
	    PrpLConfigValueVo configValueMTACheckVo = ConfigUtil.findConfigByCode(CodeConstants.MTACheck,registVo.getComCode());
	    PrpLConfigValueVo configValueCTCheckVo = ConfigUtil.findConfigByCode(CodeConstants.CTCheck,registVo.getComCode());
	    PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.MobileCheck,registVo.getComCode());
	    boolean isFireOrRobber = false;  //出险原因是否为 盗抢、自燃
	    boolean isAssessorAcceapt = false;  //是否为公估案件
	    String damageCode = registVo.getDamageCode();
	    if( null != damageCode){
	        if("DM04".equals(damageCode) || "DM09".equals(damageCode)){
	        isFireOrRobber = true;  //出险原因为盗抢或者自然
	        }
	    }
	    //白名单
		Boolean isMobileWhileListCase = false;
		String userCode = scheduleItemsVo.getScheduledUsercode();
		isMobileWhileListCase = wFMobileService.findWhileListCase(CodeConstants.WhiteList.ISMOBILE,userCode);
		// 如果不是自助查勘案件
		if( !"1".equals(isAutoCheck)){   
			if(configValueVo!=null && "1".equals(configValueVo.getConfigValue()) && isMobileWhileListCase &&
					!isFireOrRobber){
				isMobileCase ="1";
			}else{
				PrpdIntermMainVo intermMainVo = managerService.findIntermByUserCode(userCode);
				if(intermMainVo!=null&&StringUtils.isNotBlank(userCode)){
			        isAssessorAcceapt = true;
			    }
			    if(configValueVo!=null && "1".equals(configValueVo.getConfigValue()) &&
			    		!isFireOrRobber && !isAssessorAcceapt){
			        isMobileCase ="1";
			    }
			    //判断是民太安还是车童
			    if(configValueCTCheckVo != null && "1".equals(configValueCTCheckVo.getConfigValue()) && 
			    		intermMainVo!=null && "0003".equals(intermMainVo.getIntermCode())){
			    	isMobileCase = "2";
			    }else if(configValueMTACheckVo != null && "1".equals(configValueMTACheckVo.getConfigValue()) && 
			    		intermMainVo!=null && "0005".equals(intermMainVo.getIntermCode())){
			    	isMobileCase = "3";
			    }
			}
		}
		
	    if("1".equals(registVo.getIsQuickCase())){
	        isMobileCase ="0";
	    }
	    super.updatePrplWFMainIsMobileCase(flowId,isMobileCase); //工作流主表 标识为 移动查勘案件
	    return isMobileCase;
	}
}
