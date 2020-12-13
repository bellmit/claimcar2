package ins.sino.claimcar.flow.service.handle;

import ins.framework.service.CodeTranService;
import ins.platform.utils.DataUtils;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.certify.service.CertifyPubService;
import ins.sino.claimcar.certify.vo.PrpLCertifyMainVo;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.PrpLCheckVo;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claim.vo.PrpLLossItemVo;
import ins.sino.claimcar.claim.vo.PrpLLossPersonFeeVo;
import ins.sino.claimcar.claim.vo.PrpLLossPersonVo;
import ins.sino.claimcar.claim.vo.PrpLLossPropVo;
import ins.sino.claimcar.common.claimtext.service.ClaimTextService;
import ins.sino.claimcar.common.claimtext.vo.PrpLClaimTextVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.WfFlowQueryService;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.service.WfTaskQueryService;
import ins.sino.claimcar.flow.service.WfTaskService;
import ins.sino.claimcar.flow.util.TaskExtMapUtils;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.flow.vo.WfTaskSubmitVo;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 核赔工作流处理
 * @author ★LiuPing
 * @CreateTime 2016年1月11日
 */
@Service("wfVClaimService")
public class WfVClaimService extends WfBaseHandleService {
    private static Logger logger = LoggerFactory.getLogger(WfVClaimService.class);
    
	@Autowired
	private WfTaskService wfTaskService;
	@Autowired
	private WfTaskQueryService wfTaskQueryService;
    @Autowired
    CompensateTaskService compensateTaskService;
    @Autowired
    private WfFlowQueryService wfFlowQueryService;
    @Autowired
    private CertifyPubService certifyPubService;
    @Autowired
    private CheckTaskService checkTaskService;
    @Autowired
    private WfTaskHandleService wfTaskHandleService;
    @Autowired
    private LossCarService lossCarService;
    @Autowired
    private ClaimTextService claimTextService;
    @Autowired
    private CodeTranService codeTranService;
	/**
	 * 核赔提交结案、追偿
	 * @param compVo
	 * @param flowId
	 * @return
	 * @throws Exception 
	 * @modified: ☆LiuPing(2016年4月9日 ): <br>
	 */
	public List<PrpLWfTaskVo> submitVClaimHandle(PrpLCompensateVo compVo,WfTaskSubmitVo submitVo) {
		Long currentDate = System.currentTimeMillis();
		logger.info("submitVClaimHandle begin... registNo={} ",compVo.getRegistNo());
		//String flowId = submitVo.getFlowId();
		// 更新处理完成的主节点信息
		//super.updateWfEndNodeVo(flowId,FlowNode.VClaim);
		// 初始化保存下一个主节点
		super.saveWfNextNodeVo(submitVo,submitVo.getNextNode());
		// 更新taskQuery表信息
		/*PrpLWfTaskQueryVo taskQueryVo = new PrpLWfTaskQueryVo();
		taskQueryVo.setFlowId(flowId);
		taskQueryVo.setMercyFlag(compVo.getMercyFlag());// 案件紧急程度
		taskQueryVo.setReconcileFlag(checkVo.getReconcileFlag());// 是否现场调解
		wfTaskQueryService.update(taskQueryVo);*/

		// 处理tag和showXml
		TaskExtMapUtils extMapUtil = new TaskExtMapUtils();
		extMapUtil.addTagMap(compVo,"caseType","recoveryFlag","lawsuitFlag","allowFlag");
		extMapUtil.addXmlMap(compVo,"sumAmt","sumFee");
		// 处理task信息
		PrpLWfTaskVo taskInVo = new PrpLWfTaskVo();
		super.setTaskInVo(taskInVo,submitVo,submitVo.getNextNode());
		taskInVo.setHandlerIdKey(compVo.getCompensateNo());
		taskInVo.setRegistNo(compVo.getRegistNo());
		taskInVo.setBussTagMap(extMapUtil.getBussTagMap());
		taskInVo.setCompensateNo(compVo.getCompensateNo());
		taskInVo.setClaimNo(compVo.getClaimNo());
		taskInVo.setShowInfoXML(extMapUtil.getShowInfoXML());
		
		List<PrpLWfTaskVo> taskInVoList = new ArrayList<PrpLWfTaskVo>();
		taskInVoList.add(taskInVo);

		if(submitVo.getOthenNodes()!=null&&submitVo.getOthenNodes().length>0){
			for(FlowNode otherNode:submitVo.getOthenNodes()){// 发起其他节点，查勘是大案审核
				PrpLWfTaskVo taskInOthVo = new PrpLWfTaskVo();
				taskInOthVo.setHandlerIdKey(compVo.getCompensateNo().toString());
				taskInOthVo.setRegistNo(compVo.getRegistNo());
				taskInOthVo.setClaimNo(compVo.getClaimNo());
				taskInOthVo.setBussTagMap(extMapUtil.getBussTagMap());
				taskInOthVo.setCompensateNo(compVo.getCompensateNo());
				super.setTaskInVo(taskInOthVo,submitVo,otherNode);
				taskInOthVo.setAssignUser(null);
				taskInOthVo.setAssignCom(submitVo.getAssignCom());
				taskInVoList.add(taskInOthVo);
			}
		}
		PrpLWfTaskVo[] taskInVos = new PrpLWfTaskVo[taskInVoList.size()];

//		wfTaskService.submitTask(submitVo.getFlowTaskId(),taskInVo);
		wfTaskService.submitTask(submitVo.getFlowTaskId(),taskInVoList.toArray(taskInVos));
		logger.info("submitVClaimHandle end... registNo={}, cost time={} ms ",compVo.getRegistNo(),System.currentTimeMillis()-currentDate);
		return taskInVoList;
	}
	
	
	/** 任务回退处理  ---  排除拒赔----*/
	public List<PrpLWfTaskVo> backLossHandle(WfTaskSubmitVo submitVo) {
		String handleIdKey = submitVo.getHandleIdKey();
		FlowNode currentNode = submitVo.getCurrentNode();
		FlowNode nextNode = submitVo.getNextNode();
		// 获得回退时上次处理已完成的节点
		PrpLWfTaskVo taskBackVo = wfTaskService.findBackOutTask(handleIdKey,nextNode);

		PrpLWfTaskVo taskInVo = new PrpLWfTaskVo();
		super.setTaskBackInVo(taskInVo,submitVo,nextNode,taskBackVo);
		String handlerUser = taskBackVo.getHandlerUser();
		if(StringUtils.isEmpty(handlerUser)){
			handlerUser = taskBackVo.getAssignUser();
		}
		taskInVo.setAssignUser(handlerUser);
		
		String handlerCom = taskBackVo.getHandlerCom();
		if(StringUtils.isEmpty(handlerCom)){
			handlerCom = taskBackVo.getAssignCom();
		}
		taskInVo.setAssignCom(handlerCom);
		taskInVo.setClaimNo(taskBackVo.getClaimNo());
		taskInVo.setCompensateNo(submitVo.getHandleIdKey());
		taskInVo.setRemark(currentNode.name()+" Back To "+nextNode.name());
		if(StringUtils.isNotBlank(taskBackVo.getYwTaskType())){
			taskInVo.setYwTaskType(taskBackVo.getYwTaskType());
		}

		wfTaskService.submitTask(submitVo.getFlowTaskId(),taskInVo);
		List<PrpLWfTaskVo> taskInVoList = new ArrayList<PrpLWfTaskVo>();
		taskInVoList.add(taskInVo);
		return taskInVoList;
	}
	
	
	/**
	 * 核赔提交上级
	 * @param compVo
	 * @param flowId
	 * @return
	 */
	public List<PrpLWfTaskVo> submitVClaimLevelHandle(PrpLWfTaskVo wfTaskVo,WfTaskSubmitVo submitVo) {
		//String flowId = submitVo.getFlowId();

		// 更新处理完成的主节点信息
		//super.updateWfEndNodeVo(flowId,FlowNode.VClaim);
		// 初始化保存下一个主节点
		super.saveWfNextNodeVo(submitVo,submitVo.getNextNode());
		// 更新taskQuery表信息
		/*PrpLWfTaskQueryVo taskQueryVo = new PrpLWfTaskQueryVo();
		taskQueryVo.setFlowId(flowId);
		taskQueryVo.setMercyFlag(compVo.getMercyFlag());// 案件紧急程度
		taskQueryVo.setReconcileFlag(checkVo.getReconcileFlag());// 是否现场调解
		wfTaskQueryService.update(taskQueryVo);
		*/
		// 处理tag和showXml
		//TaskExtMapUtils extMapUtil = new TaskExtMapUtils();
		//extMapUtil.addTagMap(compVo,"caseType","recoveryFlag","bussTagMap","lawsuitFlag","allowFlag");
		//extMapUtil.addXmlMap(compVo,"sumAmt","sumFee");
		// 处理task信息
		PrpLWfTaskVo taskInVo = new PrpLWfTaskVo();
		super.setTaskInVo(taskInVo,submitVo,submitVo.getNextNode());
		if(StringUtils.isBlank(taskInVo.getAssignUser())){
			taskInVo.setAssignUser(submitVo.getAssignUser());
			taskInVo.setAssignCom(submitVo.getAssignCom());
		}
		taskInVo.setTaskInKey(wfTaskVo.getTaskInKey());
		taskInVo.setHandlerIdKey(submitVo.getHandleIdKey());
		taskInVo.setRegistNo(wfTaskVo.getRegistNo());
		taskInVo.setBussTagMap(wfTaskVo.getBussTagMap());
		taskInVo.setCompensateNo(wfTaskVo.getCompensateNo());
		taskInVo.setClaimNo(wfTaskVo.getClaimNo());
		taskInVo.setShowInfoXML(wfTaskVo.getShowInfoXML());
		taskInVo.setItemName(taskInVo.getItemName());
		taskInVo.setYwTaskType(wfTaskVo.getYwTaskType());
		
		List<PrpLWfTaskVo> taskInVoList = new ArrayList<PrpLWfTaskVo>();
		taskInVoList.add(taskInVo);

		PrpLWfTaskVo[] taskInVos = new PrpLWfTaskVo[taskInVoList.size()];

//		wfTaskService.submitTask(submitVo.getFlowTaskId(),taskInVo);
		wfTaskService.submitTask(submitVo.getFlowTaskId(),taskInVoList.toArray(taskInVos));

		return taskInVoList;

	}

	   /** Ilog任务回退处理  ---  排除拒赔----*/
    public List<PrpLWfTaskVo> backLossHandleByIlog(PrpLWfTaskVo wfTaskVo,List<WfTaskSubmitVo> submitVos,SysUserVo sysUserVo) {
        /*
         * 核赔退回单证---->先生成理算节点挂在核赔下，再把理算节点变注销，再打开单证
         * 核赔退回单证---->先生成理算节点挂在核赔下，再把理算节点变注销，再发起标的车定损修改
         */
        PrpLWfTaskVo taskInVo = new PrpLWfTaskVo();
        for(WfTaskSubmitVo submitVo : submitVos){
            String handleIdKey = submitVo.getHandleIdKey();
            FlowNode currentNode = submitVo.getCurrentNode();
            
            PrpLWfTaskVo taskBackVo = new PrpLWfTaskVo();
            PrpLCompensateVo compensateVo = compensateTaskService.findPrpLCompensateVoByPK(handleIdKey);
            
            if("12".equals(compensateVo.getRiskCode().substring(0,2))){//商业
                // 获得回退时上次处理已完成的节点
                taskBackVo = wfTaskService.findBackOutTaskByIlog(handleIdKey,FlowNode.valueOf("CompeBI"));
            }else{//交强
                taskBackVo = wfTaskService.findBackOutTaskByIlog(handleIdKey,FlowNode.valueOf("CompeCI"));
            }
            FlowNode nextNode = FlowNode.valueOf(taskBackVo.getSubNodeCode());
            // 获得回退时上次处理已完成的节点
            //PrpLWfTaskVo taskBackVo = wfTaskService.findBackOutTaskByIlog(handleIdKey,nextNode);
            /*
             * 生成理算节点
             */
            super.setTaskBackInVo(taskInVo,submitVo,nextNode,taskBackVo);
            String handlerUser = taskBackVo.getHandlerUser();
            if(StringUtils.isEmpty(handlerUser)){
                handlerUser = taskBackVo.getAssignUser();
            }
            taskInVo.setAssignUser(handlerUser);
            
            String handlerCom = taskBackVo.getHandlerCom();
            if(StringUtils.isEmpty(handlerCom)){
                handlerCom = taskBackVo.getAssignCom();
            }
            taskInVo.setAssignCom(handlerCom);
            taskInVo.setClaimNo(taskBackVo.getClaimNo());
            taskInVo.setCompensateNo(submitVo.getHandleIdKey());
            taskInVo.setRemark(currentNode.name()+" Back To "+nextNode.name());
            if(StringUtils.isNotBlank(taskBackVo.getYwTaskType())){
                taskInVo.setYwTaskType(taskBackVo.getYwTaskType());
            }

            wfTaskService.submitTask(submitVo.getFlowTaskId(),taskInVo);
        }
        
        

        List<PrpLWfTaskVo> taskInVoList = new ArrayList<PrpLWfTaskVo>();
        taskInVoList.add(taskInVo);
        return taskInVoList;
    }
    public BigDecimal findTaskByCertify(String registNo,String checkId,String certifyId){
        List<PrpLWfTaskVo> wfTaskVoList = wfTaskHandleService.findEndTask(registNo, checkId, FlowNode.Certi);
        if(wfTaskVoList.size()<=0){
            List<PrpLWfTaskVo> wfTaskList = wfTaskHandleService.findEndTask(registNo, certifyId, FlowNode.Certi);
            if(wfTaskList.size()>0)
                return wfTaskList.get(0).getTaskId();
            else return null;
        }else
            return wfTaskVoList.get(0).getTaskId();
    }
    
    public void backToCertiOrDLCar(PrpLWfTaskVo wfTaskVo,List<WfTaskSubmitVo> submitVos,SysUserVo sysUserVo) {
    	logger.info("报案号" + (wfTaskVo == null? null : wfTaskVo.getRegistNo())  + "，flowtaskid=" + (wfTaskVo == null? null : wfTaskVo.getTaskId())  + "正在进行核赔退回单证或者车辆定损" );
        //===============把理算节点注销start====
        //SysUserVo sysUserVo = WebUserUtils.getUser();
        WfTaskSubmitVo submitVo = submitVos.get(0);
        String registNo = wfTaskVo.getRegistNo();
        List<PrpLWfTaskVo> voBIList = wfFlowQueryService.findPrpWfTaskVo(registNo, "Compe", "CompeBI");
        List<PrpLWfTaskVo> voCIList = wfFlowQueryService.findPrpWfTaskVo(registNo, "Compe", "CompeCI");
        List<PrpLWfTaskVo> voList = new ArrayList<PrpLWfTaskVo>();
        if(voBIList!=null && voBIList.size()>0){
            for(PrpLWfTaskVo vo : voBIList){
                if(CodeConstants.HandlerStatus.INIT.equals(vo.getHandlerStatus()) || CodeConstants.HandlerStatus.DOING.equals(vo.getHandlerStatus())||
                        CodeConstants.HandlerStatus.START.equals(vo.getHandlerStatus())){
                    voList.add(vo);
                }
            }
        }
        if(voCIList!=null && voCIList.size()>0){
            for(PrpLWfTaskVo vo : voCIList){
                if(CodeConstants.HandlerStatus.INIT.equals(vo.getHandlerStatus()) || CodeConstants.HandlerStatus.DOING.equals(vo.getHandlerStatus())||
                        CodeConstants.HandlerStatus.START.equals(vo.getHandlerStatus())){
                    voList.add(vo);
                }
            }
        }
        String backFlags = wfTaskVo.getBackFlags();
        for(PrpLWfTaskVo vo : voList){
            PrpLCompensateVo compVo = null;
            if(StringUtils.isNotBlank(vo.getCompensateNo())){
                compVo = compensateTaskService.findPrpLCompensateVoByPK(vo.getCompensateNo());
            }
            
            if (compVo != null && vo.getTaskId()!=null) {
            	logger.info("报案号" + registNo + "，工作流TaskId= "+  vo.getTaskId() + "注销计算书号=" +compVo.getCompensateNo() + "并且注销工作流任务");
                compensateTaskService.cancelCompensates(compVo, vo.getTaskId(),sysUserVo);
                backFlags = "2";
            } else if (compVo == null && vo.getTaskId() != null) {
				logger.info("报案号" + registNo + "，工作流TaskId= "+  vo.getTaskId() + "注销理算工作流任务");
                compensateTaskService.cancelCompensates(vo.getTaskId(),sysUserVo);
                backFlags = "2";
            } else {
                backFlags = "1";
                break;
            }
        }
        //===============把理算节点注销end=====
        if("2".equals(backFlags)){
            //打开单证
            if("Certi".equals(submitVo.getNextNode().name())){
                //打开单证节点start====
                PrpLCheckVo checkVo=checkTaskService.findCheckVoByRegistNo(registNo);
                PrpLCertifyMainVo certifyMainVo = certifyPubService.findPrpLCertifyMainVoByRegistNo(registNo);
                BigDecimal taskId=this.findTaskByCertify(registNo,String.valueOf(checkVo.getId()),String.valueOf(certifyMainVo.getId()));
                wfTaskHandleService.moveOutToIn(taskId);
                System.out.println(submitVo.getNextNode().getName()+submitVo.getNextNode().name());
                //打开单证节点end=====
            }else if("DLCar".equals(submitVo.getNextNode().name())){
                // 车辆定损发起
                List<PrpLDlossCarMainVo> lossCarMainVos = lossCarService.findLossCarMainBySerialNo(registNo, 1);
                List<PrpLDlossCarMainVo> carMainVos = new ArrayList<PrpLDlossCarMainVo>();
                for(PrpLDlossCarMainVo vo : lossCarMainVos){
                    if("3".equals(vo.getDeflossSourceFlag()) ||"1".equals(vo.getDeflossSourceFlag())){//定损修改，普通定损
                        carMainVos.add(vo);
                    }
                }
                if(carMainVos != null && carMainVos.size()==1){
/*                    sysUserVo.setUserCode(WebUserUtils.getUserCode());
                    sysUserVo.setUserName(WebUserUtils.getUserName());
                    sysUserVo.setComCode(WebUserUtils.getComCode());*/
                    String flags = lossCarService.carModifyLaunch(lossCarMainVos.get(0).getId(),sysUserVo);

                    // 保存意见PrpLDlossCarMainVo findLossCarMainById
                    PrpLDlossCarMainVo vo = lossCarMainVos.get(0);
                    String remarks = "退回定损";
                    PrpLClaimTextVo claimTextVo = new PrpLClaimTextVo();
                    claimTextVo.setBussTaskId(lossCarMainVos.get(0).getId());
                    claimTextVo.setBigNode(FlowNode.DLCar.toString());
                    claimTextVo.setNodeCode(FlowNode.DLCarMod.toString());
                    claimTextVo.setBussNo(vo.getRegistNo());
                    claimTextVo.setRegistNo(vo.getRegistNo());
                    claimTextVo.setTextType("2");
                    claimTextVo.setDescription(remarks);
                    claimTextVo.setOperatorCode(sysUserVo.getUserCode());
                    claimTextVo.setOperatorName(sysUserVo.getUserName());
                    claimTextVo.setCreateTime(new Date());
                    claimTextVo.setUpdateTime(new Date());
                    claimTextVo.setCreateUser(sysUserVo.getUserCode());
                    claimTextVo.setUpdateUser(sysUserVo.getUserCode());
                    claimTextVo.setFlag("1");
                    claimTextVo.setRemark(remarks);
                    claimTextVo.setStatus("理算退回定损");
                    claimTextVo.setInputTime(new Date());
                    claimTextVo.setComCode(sysUserVo.getComCode());
                    claimTextVo.setSumLossFee(vo.getSumVeriLossFee());
                    claimTextVo.setOpinionCode("理算退回定损");
                    claimTextService.saveOrUpdte(claimTextVo);
                }else if(lossCarMainVos != null && lossCarMainVos.size() > 1){
                    logger.info("定损主表数据有问题，请排查========="+carMainVos.get(0).getRegistNo());
                }
            }
        }

    	logger.info("报案号" + (wfTaskVo == null? null : wfTaskVo.getRegistNo())  + "，flowtaskid=" + (wfTaskVo == null? null : wfTaskVo.getTaskId())  + "结束核赔退回单证或者车辆定损" );
    }
}
