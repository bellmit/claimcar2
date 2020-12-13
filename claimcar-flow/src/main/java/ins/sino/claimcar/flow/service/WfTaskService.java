/******************************************************************************
* CREATETIME : 2015年12月17日 下午3:26:39
******************************************************************************/
package ins.sino.claimcar.flow.service;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.platform.common.service.facade.BaseDaoService;
import ins.platform.common.service.spring.ProcWork;
import ins.platform.utils.DataUtils;
import ins.platform.utils.SqlJoinUtils;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.HandlerStatus;
import ins.sino.claimcar.CodeConstants.WorkStatus;
import ins.sino.claimcar.claim.service.CompensateHandleServiceIlogService;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claim.vo.PrplReplevyMainVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.po.PrpLWfMain;
import ins.sino.claimcar.flow.po.PrpLWfTaskIn;
import ins.sino.claimcar.flow.po.PrpLWfTaskIn_NoGenId;
import ins.sino.claimcar.flow.po.PrpLWfTaskOut;
import ins.sino.claimcar.flow.po.PrpLWfTaskUserInfo;
import ins.sino.claimcar.flow.util.TaskExtMapUtils;
import ins.sino.claimcar.flow.vo.PrpDNodeVo;
import ins.sino.claimcar.flow.vo.PrpLWfMainVo;
import ins.sino.claimcar.flow.vo.PrpLWfTaskUserInfoVo;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.flow.vo.WfTaskQueryResultVo;
import ins.sino.claimcar.ilog.defloss.vo.LIlogRuleResVo;
import ins.sino.claimcar.losscar.service.DeflossHandleIlogService;
import ins.sino.claimcar.losscar.service.DeflossHandleService;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.lossperson.service.PersReqIlogService;
import ins.sino.claimcar.lossperson.service.PersTraceService;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceMainVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceVo;
import ins.sino.claimcar.lossprop.service.PropLossService;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropMainVo;
import ins.sino.claimcar.manager.po.PrpdIntermMain;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.rule.service.ClaimRuleApiService;
import ins.sino.claimcar.rule.vo.LossPropToVerifyRuleVo;
import ins.sino.claimcar.rule.vo.VerifyClaimRuleVo;
import ins.sino.claimcar.rule.vo.VerifyLossRuleVo;
import ins.sino.claimcar.rule.vo.VerifyPersonRuleVo;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 * @author Dengkk
 * @CreateTime 2015年12月17日
 */
@Service(value = "wfTaskService")
public class WfTaskService {

	private static Logger logger = LoggerFactory.getLogger(WfTaskService.class);


	@Autowired
	private DatabaseDao databaseDao;
	@Autowired
	private SessionFactory sessionFactory;

    @Autowired
    private CompensateTaskService compensateTaskService;
    @Autowired
    WfTaskHandleService wfTaskHandleService;
    @Autowired
    DeflossHandleService deflossHandleService;
    @Autowired
    ClaimRuleApiService claimRuleApiService;
	@Autowired
	private PolicyViewService policyViewService;
    @Autowired
    private PersTraceService persTraceService;
    @Autowired
    private PropLossService propLossService;
    @Autowired
    LossCarService lossCarService;
	@Autowired
	private WfMainService wfMainService;
	@Autowired
	DeflossHandleIlogService deflossHandleIlogService;
	@Autowired
	PersReqIlogService persReqIlogService;
	@Autowired
	CompensateHandleServiceIlogService compensateHandleServiceIlogService;
	@Autowired
	BaseDaoService baseDaoService;
	/**
	 * 保存taskIn信息,
	 * @param taskInVo
	 * @modified: ☆LiuPing(2016年1月9日 下午3:45:31): <br>
	 */
	public BigDecimal saveTaskIn(PrpLWfTaskVo taskInVo) {
		PrpLWfTaskIn po = new PrpLWfTaskIn();
		Beans.copy().from(taskInVo).to(po);
		databaseDao.save(PrpLWfTaskIn.class,po);
		
		logger.debug("saveTaskIn.po.id="+po.getTaskId());
		
		taskInVo.setTaskId(po.getTaskId());
		//保存工作流操作人员
		if(StringUtils.isNotBlank(taskInVo.getAssignUser())){
			saveTaskUserInfo(taskInVo.getFlowId(), taskInVo.getAssignUser());
		}
		return po.getTaskId();
	}
    
	/**
	 * 保存taskIn信息,
	 * @param taskInVo
	 */
	public PrpLWfTaskVo saveprplwfTaskIn(PrpLWfTaskVo taskInVo) {
		PrpLWfTaskIn po = new PrpLWfTaskIn();
		Beans.copy().from(taskInVo).to(po);
		databaseDao.save(PrpLWfTaskIn.class,po);
		
		logger.debug("saveTaskIn.po.id="+po.getTaskId());
		
		taskInVo.setTaskId(po.getTaskId());
		//保存工作流操作人员
		if(StringUtils.isNotBlank(taskInVo.getAssignUser())){
			this.saveTaskUserInfo(taskInVo.getFlowId(), taskInVo.getAssignUser());
		}
		return taskInVo;
	}

	
	
	
	/**
	 * 更新 TaskIn
	 * @param taskInVo
	 * @modified: ☆LiuPing(2016年4月9日 ): <br>
	 */
	public void updateTaskIn(PrpLWfTaskVo taskInVo) {
		PrpLWfTaskIn po = null;
		po = databaseDao.findByPK(PrpLWfTaskIn.class,taskInVo.getTaskId());
		if(po!=null){
			Beans.copy().from(taskInVo).to(po);
			databaseDao.update(PrpLWfTaskIn.class,po);
		}
	}
	
	/**
	 * 更新TaskOut
	 * <pre></pre>
	 * @param taskInVo
	 * @modified:
	 * ☆LinYi(2017年9月19日 下午5:36:00): <br>
	 */
	public void updateTaskOut(PrpLWfTaskVo taskInVo) {
        PrpLWfTaskOut po = null;
        po = databaseDao.findByPK(PrpLWfTaskOut.class,taskInVo.getTaskId());
        if(po!=null){
            Beans.copy().from(taskInVo).to(po);
            databaseDao.update(PrpLWfTaskOut.class,po);
        }
    }
	
	/**
	 * 接收任务，将任务变成已接收为处理状态
	 * @return
	 * @modified: ☆LiuPing(2016年1月12日 ): <br>
	 */
	public PrpLWfTaskVo acceptTask(Double flowTaskId,String assignUser,String assignCom) {
		// 找到旧的taskin数据
		PrpLWfTaskIn oldTaskInPo = databaseDao.findByPK(PrpLWfTaskIn.class,new BigDecimal(flowTaskId));
		if(oldTaskInPo!=null){
			oldTaskInPo.setAssignUser(assignUser);
			oldTaskInPo.setAssignCom(assignCom);
			oldTaskInPo.setHandlerStatus(HandlerStatus.START);
			oldTaskInPo.setWorkStatus(WorkStatus.START);
			oldTaskInPo.setAcceptTime(new Date());
			databaseDao.update(PrpLWfTaskIn.class,oldTaskInPo);
		}
		PrpLWfTaskVo taskVo = Beans.copyDepth().from(oldTaskInPo).to(PrpLWfTaskVo.class);
		return taskVo;
	}

	/**
	 * 暂存任务，将任务变成处理中状态
	 * @param flowTaskId
	 * @param handlerIdKey
	 * @param handlerUser
	 * @param handlerCom
	 * @return
	 * @modified: ☆LiuPing(2016年1月12日 ): <br>
	 */
	public PrpLWfTaskVo tempSaveTask(Double flowTaskId,String handlerIdKey,String handlerUser,String handlerCom) {
		// 找到旧的taskin数据
		PrpLWfTaskIn oldTaskInPo = databaseDao.findByPK(PrpLWfTaskIn.class,new BigDecimal(flowTaskId));
		if(oldTaskInPo!=null&&oldTaskInPo.getHandlerTime()==null){// 防止多次暂存修改数据
			oldTaskInPo.setHandlerIdKey(handlerIdKey);
			oldTaskInPo.setHandlerUser(handlerUser);
			oldTaskInPo.setHandlerCom(handlerCom);
			oldTaskInPo.setHandlerTime(new Date());
			//人伤退回标识
			if(FlowNode.PLNext.equals(oldTaskInPo.getSubNodeCode()) && oldTaskInPo.getWorkStatus().equals("6")){
				TaskExtMapUtils extMapUtil = new TaskExtMapUtils();
				PrpLWfTaskVo taskInVo = new PrpLWfTaskVo();
				WfTaskQueryResultVo wfTaskQueryResultVo = new WfTaskQueryResultVo();
				wfTaskQueryResultVo.setBackFlags("1");
				extMapUtil.addTagMap(wfTaskQueryResultVo,"backFlags");
				taskInVo.setBussTagMap(extMapUtil.getBussTagMap());
				String bussTag = TaskExtMapUtils.joinBussTag(oldTaskInPo.getBussTag(),taskInVo.getBussTagMap());
				oldTaskInPo.setBussTag(bussTag);
			}
			oldTaskInPo.setHandlerStatus(HandlerStatus.DOING);
			oldTaskInPo.setWorkStatus(WorkStatus.DOING);
			if(StringUtils.isBlank(oldTaskInPo.getAssignUser())){//到岗人员写入值
				oldTaskInPo.setAssignUser(handlerUser);
				oldTaskInPo.setAssignCom(handlerCom);
			}
			databaseDao.update(PrpLWfTaskIn.class,oldTaskInPo);
			//保存工作流操作人员
			if(StringUtils.isNotBlank(oldTaskInPo.getHandlerUser())){
				saveTaskUserInfo(oldTaskInPo.getFlowId(), oldTaskInPo.getHandlerUser());
			}
		}
		PrpLWfTaskVo taskVo = Beans.copyDepth().from(oldTaskInPo).to(PrpLWfTaskVo.class);
		return taskVo;
	}
	
	/**
	 * @param flowTaskId
	 * @param itemName
	 * @return
	 * @modified: ☆YangKun(2016年6月15日 上午10:33:31): <br>
	 */
	public PrpLWfTaskVo tempSaveTask(Double flowTaskId,String itemName) {
		// 找到旧的taskin数据
		PrpLWfTaskIn oldTaskInPo = databaseDao.findByPK(PrpLWfTaskIn.class,new BigDecimal(flowTaskId));
		if(oldTaskInPo!=null){// 防止多次暂存修改数据
			logger.info("itemName==============================================="+itemName);
			System.out.println("itemName==============================================="+itemName);
			oldTaskInPo.setItemName(itemName);
			databaseDao.update(PrpLWfTaskIn.class,oldTaskInPo);
			//保存工作流操作人员
			if(StringUtils.isNotBlank(oldTaskInPo.getHandlerUser())){
				saveTaskUserInfo(oldTaskInPo.getFlowId(), oldTaskInPo.getHandlerUser());
			}
		}
		PrpLWfTaskVo taskVo = Beans.copyDepth().from(oldTaskInPo).to(PrpLWfTaskVo.class);
		return taskVo;
	}
	
	/**
	 * 更新prplwftaskout表的handlerIdKey
	 * @param flowTaskId
	 * @param handlerIdKey
	 */
	public void updateTaskOut(Double flowTaskId,String handlerIdKey){
		PrpLWfTaskOut oldTaskOutPo = databaseDao.findByPK(PrpLWfTaskOut.class,new BigDecimal(flowTaskId));
		if(oldTaskOutPo!=null){
			oldTaskOutPo.setHandlerIdKey(handlerIdKey);
			databaseDao.update(PrpLWfTaskOut.class,oldTaskOutPo);
		}
	}

	/**
	 * 任务处理完成，提交任务信息, 将原taskin的信息复制为处理完成后，转移到taskOut
	 * @param flowTaskId 原taskid,
	 * @param taskInVos
	 * @modified: ☆LiuPing(2016年1月9日 下午3:44:44): <br>
	 */
	public void submitTask(BigDecimal flowTaskId,PrpLWfTaskVo... taskInVos) {
		// 找到旧的taskin数据
		if(flowTaskId==null){
			throw new IllegalArgumentException("flowTaskId 不能为空");
		}
		logger.info("itemName================================="+taskInVos[0].getItemName());
		PrpLWfTaskOut outPo = moveInToOut(flowTaskId,taskInVos[0]);
		String bussTag = outPo.getBussTag();
		String taskInNode = outPo.getSubNodeCode();
//		String nodeCode = outPo.getNodeCode();

//		// 定损提交到核价、核价提交到核损时、核损提交时，核价、核损的taskinNode,还是要保存PrpLWfTaskOut的taskinNode；
//		if(FlowNode.VPrice.equals(nodeCode) || FlowNode.VLoss.equals(nodeCode)){
//			taskInNode = outPo.getTaskInNode();
//		}

		// 保存新的taskin
		for(PrpLWfTaskVo taskInVo:taskInVos){
			if( !FlowNode.END.equals(taskInVo.getNodeCode())){
				bussTag = TaskExtMapUtils.joinBussTag(bussTag,taskInVo.getBussTagMap());
				PrpLWfTaskIn oldtaskIn = queryTaskInByHandKey(taskInVo.getFlowId(),taskInVo.getHandlerIdKey(),taskInVo.getSubNodeCode());
				if(oldtaskIn!=null){
					throw new IllegalStateException("此任务已提交,不能再次提交!");
				}
				PrpLWfTaskIn taskInPo = new PrpLWfTaskIn();
				Beans.copy().from(taskInVo).excludeNull().to(taskInPo);
				taskInPo.setBussTag(bussTag);
				taskInPo.setTaskInNode(taskInNode);
				taskInPo.setUpperTaskId(flowTaskId);
				//设置金额
				if(taskInVo.getMoney() != null){
					taskInPo.setMoney(taskInVo.getMoney());
				}
				databaseDao.save(PrpLWfTaskIn.class,taskInPo);
				//保存工作流操作人员
				if(StringUtils.isNotBlank(taskInPo.getAssignUser())){
					saveTaskUserInfo(taskInPo.getFlowId(), taskInPo.getAssignUser());
				}
				
				taskInVo.setTaskId(taskInPo.getTaskId());
			}
		}

	}

	/**
	 * 报案注销，立案未处理前可以注销，将这个案件所以未处理的和正在处理的节点变为已注销
	 * @modified: ☆LiuPing(2016年2月26日 ): <br>
	 */
	public void cancelRegist(String flowId,String cancelUser) {
		if(flowId==null){
			throw new IllegalArgumentException("flowId 不能为空");
		}
		//跟新报案状态
		QueryRule rule = QueryRule.getInstance();
		rule.addEqual("flowId",flowId);
		rule.addEqual("nodeCode",FlowNode.Regis.name());
		List<PrpLWfTaskOut> PrpLWfTaskOuts = databaseDao.findAll(PrpLWfTaskOut.class,rule);
		PrpLWfTaskOut registInPo = PrpLWfTaskOuts.get(0);
		registInPo.setHandlerStatus(HandlerStatus.CANCEL);
		registInPo.setWorkStatus(WorkStatus.CANCEL);
		databaseDao.update(PrpLWfTaskOut.class,registInPo);
		
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("flowId",flowId);
		List<PrpLWfTaskIn> allTaskIn = databaseDao.findAll(PrpLWfTaskIn.class,queryRule);
		for(PrpLWfTaskIn oldTaskInPo:allTaskIn){
			PrpLWfTaskOut taskOutPo = new PrpLWfTaskOut();
			Beans.copy().from(oldTaskInPo).to(taskOutPo);
			taskOutPo.setHandlerStatus(HandlerStatus.CANCEL);
			taskOutPo.setWorkStatus(WorkStatus.CANCEL);
			taskOutPo.setTaskOutNode(FlowNode.Cancel.name());
			taskOutPo.setTaskOutKey(taskOutPo.getTaskInKey());
			taskOutPo.setTaskOutTime(new Date());
			taskOutPo.setTaskOutUser(cancelUser);
			databaseDao.save(PrpLWfTaskOut.class,taskOutPo);
			// 删除旧的taskin
			databaseDao.deleteByPK(PrpLWfTaskIn.class,oldTaskInPo.getTaskId());
		}

	}

	public void moveTaskToOut(PrpLWfTaskVo taskVo) {
		BigDecimal flowTaskId = taskVo.getTaskId();
		PrpLWfTaskIn oldTaskInPo = databaseDao.findByPK(PrpLWfTaskIn.class,flowTaskId);
		if(oldTaskInPo!=null){// 等于null的时候说明已经转移了,要从out表找到他
			// 复制保存到taskout，并更改任务完成状态
			PrpLWfTaskOut taskOutPo = Beans.copyDepth().from(taskVo).to(PrpLWfTaskOut.class);
			databaseDao.save(PrpLWfTaskOut.class,taskOutPo);
			// 删除旧的taskin
			databaseDao.deleteByPK(PrpLWfTaskIn.class,flowTaskId);
		}

	}
	

	/** 将in数据转移到out,这里是根据下一个节点处理 */
	public PrpLWfTaskOut moveInToOut(BigDecimal flowTaskId,PrpLWfTaskVo taskInVo) {
		PrpLWfTaskIn oldTaskInPo = databaseDao.findByPK(PrpLWfTaskIn.class,flowTaskId);
		PrpLWfTaskOut taskOutPo = null;
		if(oldTaskInPo!=null){// 等于null的时候说明已经转移了,要从out表找到他
			// 复制保存到taskout，并更改任务完成状态
			taskOutPo = Beans.copyDepth().from(oldTaskInPo).to(PrpLWfTaskOut.class);
			taskOutPo.setHandlerStatus(HandlerStatus.END);
			taskOutPo.setWorkStatus(WorkStatus.END);
			if(taskInVo.getWorkStatus().equals(WorkStatus.BYBACK)){
				taskOutPo.setWorkStatus(WorkStatus.BACK);
			}else if(taskInVo.getWorkStatus().equals(WorkStatus.CANCEL)){
				taskOutPo.setWorkStatus(WorkStatus.CANCEL);
			}
			if("CompeWfZero".equals(taskInVo.getCompensateNo())){
				taskOutPo.setHandlerIdKey("CompeWfZero");
			}
			if(StringUtils.isNotBlank(taskInVo.getRiskCode())) taskOutPo.setRiskCode(taskInVo.getRiskCode());
			taskOutPo.setTaskOutNode(taskInVo.getSubNodeCode());
			taskOutPo.setTaskOutKey(taskInVo.getTaskInKey());
			taskOutPo.setClaimNo(taskInVo.getClaimNo());
			taskOutPo.setCompensateNo(taskInVo.getCompensateNo());

			if(StringUtils.isBlank(oldTaskInPo.getAssignUser())){//到岗人员写入值
				oldTaskInPo.setAssignUser(taskInVo.getTaskInUser());
			}
		    if(StringUtils.isBlank(taskOutPo.getHandlerUser())){
		    	taskOutPo.setHandlerUser(taskInVo.getTaskInUser());
				taskOutPo.setHandlerTime(taskInVo.getTaskInTime());
		    }
			taskOutPo.setTaskOutUser(taskInVo.getTaskInUser());
			taskOutPo.setTaskOutTime(taskInVo.getTaskInTime());
			if(StringUtils.isNotBlank(taskInVo.getShowInfoXML())){
				taskOutPo.setShowInfoXML(taskInVo.getShowInfoXML());
			}
			if(StringUtils.isNotBlank(taskInVo.getYwTaskType())){
				taskOutPo.setYwTaskType(taskInVo.getYwTaskType());
			}
			
			//人伤退回标识
			if(FlowNode.PLNext.equals(taskOutPo.getSubNodeCode())){
				TaskExtMapUtils extMapUtil = new TaskExtMapUtils();
				PrpLWfTaskVo wfTaskVo = new PrpLWfTaskVo();
				WfTaskQueryResultVo wfTaskQueryResultVo = new WfTaskQueryResultVo();
				wfTaskQueryResultVo.setBackFlags("0");
				extMapUtil.addTagMap(wfTaskQueryResultVo,"backFlags");
				wfTaskVo.setBussTagMap(extMapUtil.getBussTagMap());
				String bussTag = TaskExtMapUtils.joinBussTag(taskOutPo.getBussTag(),wfTaskVo.getBussTagMap());
				taskOutPo.setBussTag(bussTag);
			}
			
			databaseDao.save(PrpLWfTaskOut.class,taskOutPo);
			// 删除旧的taskin
			databaseDao.deleteByPK(PrpLWfTaskIn.class,flowTaskId);
			//保存工作流操作人员
			if(StringUtils.isNotBlank(taskOutPo.getTaskOutUser())){
				saveTaskUserInfo(taskOutPo.getFlowId(), taskOutPo.getTaskOutUser());
			}
		} else {
			taskOutPo=databaseDao.findByPK(PrpLWfTaskOut.class,flowTaskId);
		}
		
		return taskOutPo;
	}


	/**
	 * 已提交任务回滚，从out表回退到in表，
	 * @param taskVo
	 * @return
	 * @modified: ☆LiuPing(2016年6月24日 ): <br>
	 */
	public PrpLWfTaskVo rollBackTask(PrpLWfTaskVo backTaskVo) {//当前还没处理完
		// 从taskIn删除backTaskVo.
		BigDecimal backInTaskId = backTaskVo.getTaskId();
		BigDecimal oldOutTaskId = backTaskVo.getUpperTaskId();
		databaseDao.deleteByPK(PrpLWfTaskIn.class,backInTaskId);
		// 从taskout表找到backTaskVo.upperTaskId 任务 oldOutPo;
		PrpLWfTaskOut oldOutPo = databaseDao.findByPK(PrpLWfTaskOut.class,oldOutTaskId);

		// 将 oldOutPo 转换为oldInPo
		PrpLWfTaskIn oldInPo = new PrpLWfTaskIn();
		Beans.copy().from(oldOutPo).to(oldInPo);
		oldInPo.setTaskOutNode(null);
		oldInPo.setHandlerStatus(HandlerStatus.DOING);
		oldInPo.setWorkStatus(WorkStatus.DOING);

		// 保存oldInPo,删除outVo
		databaseDao.save(PrpLWfTaskIn.class,oldInPo);
		databaseDao.deleteByPK(PrpLWfTaskOut.class,oldOutTaskId);

		// oldInPo to vo
		PrpLWfTaskVo taskVo = Beans.copyDepth().from(oldInPo).to(PrpLWfTaskVo.class);
		return taskVo;
	}

	/**
	 * 已提交任务回滚，从out表回退到in表,回退当前节点
	 * <pre></pre>
	 * @param backTaskVo
	 * @return
	 * @modified:
	 * ☆zhujunde(2018年4月18日 下午3:52:29): <br>
	 */
   public PrpLWfTaskVo rollBackTaskByEndVo(PrpLWfTaskVo backTaskVo) {//当前vo已经处理
        
       BigDecimal oldOutTaskId = backTaskVo.getTaskId();
        // 从taskout表找到backTaskVo.TaskId 任务 oldOutPo;
        PrpLWfTaskOut oldOutPo = databaseDao.findByPK(PrpLWfTaskOut.class,oldOutTaskId);

        // 将 oldOutPo 转换为oldInPo
        PrpLWfTaskIn oldInPo = new PrpLWfTaskIn();
        Beans.copy().from(oldOutPo).to(oldInPo);
        oldInPo.setTaskOutNode(null);
        oldInPo.setHandlerStatus(HandlerStatus.DOING);
        oldInPo.setWorkStatus(WorkStatus.DOING);

        // 保存oldInPo,删除outVo
        databaseDao.save(PrpLWfTaskIn.class,oldInPo);
        databaseDao.deleteByPK(PrpLWfTaskOut.class,oldOutTaskId);

        // oldInPo to vo
        PrpLWfTaskVo taskVo = Beans.copyDepth().from(oldInPo).to(PrpLWfTaskVo.class);
        return taskVo;
    }
	   
	/**
	 * 查询一个任务
	 * @param flowTaskId
	 * @return
	 * @modified: ☆LiuPing(2016年1月19日 ): <br>
	 */
	public PrpLWfTaskVo queryTask(Double flowTaskId) {
		PrpLWfTaskIn oldTaskInPo = databaseDao.findByPK(PrpLWfTaskIn.class,new BigDecimal(flowTaskId));
		PrpLWfTaskVo taskVo = null;
		if(oldTaskInPo!=null){
			taskVo = Beans.copyDepth().from(oldTaskInPo).to(PrpLWfTaskVo.class);
		}else{
			PrpLWfTaskOut oldTaskOutPo = databaseDao.findByPK(PrpLWfTaskOut.class,new BigDecimal(flowTaskId));
			taskVo = Beans.copyDepth().from(oldTaskOutPo).to(PrpLWfTaskVo.class);
		}
		return taskVo;
	}
	
	/**
	 * 查询taskIn表的任务，未提交的任务
	 * @param flowTaskId
	 * @return
	 * @throws Exception
	 * @modified: ☆XMSH(2016年7月28日 下午3:58:03): <br>
	 */
	public PrpLWfTaskVo findTaskIn(Double flowTaskId) throws Exception {
		PrpLWfTaskIn oldTaskInPo = databaseDao.findByPK(PrpLWfTaskIn.class,new BigDecimal(flowTaskId));
		PrpLWfTaskVo taskVo = null;
		if(oldTaskInPo!=null){
			taskVo = Beans.copyDepth().from(oldTaskInPo).to(PrpLWfTaskVo.class);
		}
		return taskVo;
	}

	/**
	 * 根据工作流和子节点找到任务
	 * @param flowId
	 * @param subNode
	 * @return
	 * @modified: ☆LiuPing(2016年4月9日 ): <br>
	 */
	public List<PrpLWfTaskVo> queryTask(String flowId,FlowNode subNode) {
		List<PrpLWfTaskVo> prpLWfTaskVos = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("flowId",flowId);
		queryRule.addEqual("subNodeCode",subNode.name());
		List<PrpLWfTaskIn> prpLWfTaskIns = databaseDao.findAll(PrpLWfTaskIn.class,queryRule);
		if(prpLWfTaskIns!=null&& !prpLWfTaskIns.isEmpty()){
			prpLWfTaskVos = Beans.copyDepth().from(prpLWfTaskIns).toList(PrpLWfTaskVo.class);
		}else{
			List<PrpLWfTaskOut> prpLWfTaskOuts = databaseDao.findAll(PrpLWfTaskOut.class,queryRule);
			if(prpLWfTaskOuts!=null) prpLWfTaskVos = Beans.copyDepth().from(prpLWfTaskOuts).toList(PrpLWfTaskVo.class);
		}
		return prpLWfTaskVos;
	}

	public PrpLWfTaskIn queryTaskInByHandKey(String flowId,String handlerIdKey,String subNodeCode) {
		
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("flowId",flowId);
		queryRule.addEqual("handlerIdKey",handlerIdKey);
		queryRule.addEqual("subNodeCode",subNodeCode);
		
		PrpLWfTaskIn oldtaskPo = databaseDao.findUnique(PrpLWfTaskIn.class,queryRule);
		
		return oldtaskPo;
	}
	
	/**
	 * 查找退回的节点
	 * @return
	 * @modified: ☆LiuPing(2016年1月19日 ): <br>
	 */
	public PrpLWfTaskVo findBackOutVo(String handleIdKey,String flowId,FlowNode backNode) {
		String hql = " FROM PrpLWfTaskOut p Where handlerIdKey=? and subNodeCode=? and flowId=? Order By taskOutTime DESC ";
		List<PrpLWfTaskOut> taskPoList = databaseDao.findTopByHql(PrpLWfTaskOut.class,hql,1,handleIdKey,backNode.name(),flowId);
		PrpLWfTaskVo taskVo = null;
		// liuping 2016-5-11 人伤审核退回时，界面上面是退回后续跟踪，但是首次人伤跟踪提交审核后还没有后续跟踪节点，所以这里需要再查询一次人伤首次跟踪的节点
		if(taskPoList.isEmpty()&&backNode.equals(FlowNode.PLNext)){//
			taskPoList = databaseDao.findTopByHql(PrpLWfTaskOut.class,hql,1,handleIdKey,FlowNode.PLFirst.name(),flowId);
		}
		if(taskPoList.isEmpty()){
			throw new IllegalStateException("没有找到对应的回退节点。业务为号为: "+handleIdKey+" ,退回节点: "+backNode.name());
		}else{
			taskVo = Beans.copyDepth().from(taskPoList.get(0)).to(PrpLWfTaskVo.class);
		}

		return taskVo;
	}
	
	/**
	 * 垫付、预付、理算查找退回的节点（计算书号、节点）--配出拒赔
	 * @return
	 */
	public PrpLWfTaskVo findBackOutTask(String handleIdKey,FlowNode backNode) {
		String node = backNode.name();
		String jupei = FlowNode.CancelAppJuPei.name();
		String claim_ci = FlowNode.ClaimCI.name();
		String claim_bi = FlowNode.ClaimBI.name();
		boolean cancel = jupei.equals(node)||claim_bi.equals(node)||claim_ci.equals(node);
		String hql;
		if(backNode.equals("PadPay")){
			hql = " FROM PrpLWfTaskOut p Where handlerIdKey=? and subNodeCode=? Order By taskOutTime DESC ";
		}else if(cancel){
			hql = " FROM PrpLWfTaskOut p Where claimNo=? and subNodeCode=? Order By taskOutTime DESC ";
		}else{
			hql = " FROM PrpLWfTaskOut p Where compensateNo=? and subNodeCode=? Order By taskOutTime DESC ";
		}
		List<PrpLWfTaskOut> taskPoList = databaseDao.findTopByHql(PrpLWfTaskOut.class,hql,1,handleIdKey,backNode.name());
		PrpLWfTaskVo taskVo = null;
		// liuping 2016-5-11 人伤审核退回时，界面上面是退回后续跟踪，但是首次人伤跟踪提交审核后还没有后续跟踪节点，所以这里需要再查询一次人伤首次跟踪的节点
		if(taskPoList.isEmpty()&&backNode.equals(FlowNode.PLNext)){//
			taskPoList = databaseDao.findTopByHql(PrpLWfTaskOut.class,hql,1,handleIdKey,FlowNode.PLFirst.name());
		}
		if(!taskPoList.isEmpty()){
			taskVo = Beans.copyDepth().from(taskPoList.get(0)).to(PrpLWfTaskVo.class);
		}else{
			String sql = " FROM PrpLWfTaskOut p Where handlerIdKey=? and subNodeCode=? Order By taskOutTime DESC ";
			taskPoList = databaseDao.findTopByHql(PrpLWfTaskOut.class,sql,1,handleIdKey,backNode.name());
			if(taskPoList.isEmpty()){
				throw new IllegalStateException("没有找到对应的回退节点。业务为号为: "+handleIdKey+" ,退回节点: "+backNode.name());
			}else{
				taskVo = Beans.copyDepth().from(taskPoList.get(0)).to(PrpLWfTaskVo.class);
			}
		}

		return taskVo;
	}

	/**
	 * 查询已处理节点的节点,如果这节点在PrpLWfTaskIn有数据表示这个节点还没出来完，直接返回Null
	 * @param registNo
	 * @param handlerIdKey 可为空，为空要检查In表数据
	 * @param nodeCode 请填写子节点，例如查勘，入参为 Chk
	 * @return
	 * @modified: ☆LiuPing(2016年1月22日 ): <br>
	 */
	public List<PrpLWfTaskVo> findEndTask(String registNo,String handlerIdKey,FlowNode nodeCode) {
		String prantNode = nodeCode.getUpperNode();
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		if(StringUtils.isNotBlank(handlerIdKey)){
			queryRule.addEqual("handlerIdKey",handlerIdKey);
		}
		
		if(StringUtils.isNotBlank(prantNode)){
			queryRule.addEqual("nodeCode",nodeCode.getRootNode().name());
			if(FlowNode.ChkBig == nodeCode || FlowNode.PLBig == nodeCode || FlowNode.PLVerify == nodeCode
					|| FlowNode.VLProp == nodeCode || FlowNode.VLCar == nodeCode || FlowNode.VPCar == nodeCode){
				queryRule.addLike("subNodeCode",nodeCode.name()+"%");
			}else{
				queryRule.addEqual("subNodeCode",nodeCode.name());
			}
			
		}else{
			queryRule.addEqual("nodeCode",nodeCode.name());
		}

		if(StringUtils.isBlank(handlerIdKey)){
			List<PrpLWfTaskIn> prpLWfTaskIns = databaseDao.findTop(PrpLWfTaskIn.class,queryRule,1);
			if(prpLWfTaskIns!=null&& !prpLWfTaskIns.isEmpty()){// In表有数据，直接返回空
				return null;
			}
		}
		queryRule.addDescOrder("taskOutTime");
		List<PrpLWfTaskOut> prpLWfTaskOuts = databaseDao.findAll(PrpLWfTaskOut.class,queryRule);
		List<PrpLWfTaskVo> prpLWfTaskOutVos = new ArrayList<PrpLWfTaskVo>();
		if(prpLWfTaskOuts !=null && !prpLWfTaskOuts.isEmpty()){
			prpLWfTaskOutVos = Beans.copyDepth().from(prpLWfTaskOuts).toList(PrpLWfTaskVo.class);
		}else{
			//查询prpLWfTaskOuts为空时，可能是旧理赔数据，按照旧理赔条件查询
			QueryRule oldClaimRule = QueryRule.getInstance();
			oldClaimRule.addEqual("registNo",registNo);
			if(StringUtils.isNotBlank(handlerIdKey)){
				oldClaimRule.addEqual("compensateNo",handlerIdKey);
			}
			if(StringUtils.isNotBlank(prantNode)){
				oldClaimRule.addEqual("nodeCode",nodeCode.getRootNode().name());
				if(FlowNode.ChkBig == nodeCode || FlowNode.PLBig == nodeCode || FlowNode.PLVerify == nodeCode
						|| FlowNode.VLProp == nodeCode || FlowNode.VLCar == nodeCode || FlowNode.VPCar == nodeCode){
					oldClaimRule.addLike("subNodeCode",nodeCode.name()+"%");
				}else{
					oldClaimRule.addEqual("subNodeCode",nodeCode.name());
				}
				
			}else{
				oldClaimRule.addEqual("nodeCode",nodeCode.name());
			}
			prpLWfTaskOuts = databaseDao.findAll(PrpLWfTaskOut.class,oldClaimRule);
			prpLWfTaskOutVos = Beans.copyDepth().from(prpLWfTaskOuts).toList(PrpLWfTaskVo.class);
		}
		return prpLWfTaskOutVos;
	}

	/**
	 * 根据报案号查询某个节点未完成的节点, -----------没有用到
	 * @return
	 * @modified: ☆LiuPing(2016年1月30日 ): <br>
	 */
	/*
	public List<PrpLWfTaskVo> findTaskIn(String registNo,FlowNode nodeCode) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		String prantNode = nodeCode.getUpperNode();

		if(StringUtils.isNotBlank(prantNode)){
			queryRule.addEqual("nodeCode",nodeCode.getRootNode().name());
			queryRule.addEqual("subNodeCode",nodeCode.name());
		}else{
			queryRule.addEqual("nodeCode",nodeCode.name());
		}

		List<PrpLWfTaskIn> prpLWfTaskIns = databaseDao.findAll(PrpLWfTaskIn.class,queryRule);
		List<PrpLWfTaskVo> prpLWfTaskInVos = Beans.copyDepth().from(prpLWfTaskIns).toList(PrpLWfTaskVo.class);
		return prpLWfTaskInVos;
	}
	*/
	public PrpLWfTaskVo updateRegistTask(String registNo,String bussTag,String showInfoXML) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		queryRule.addEqual("nodeCode",FlowNode.Regis.name());
		List<PrpLWfTaskIn> prpLWfTaskIns = databaseDao.findAll(PrpLWfTaskIn.class,queryRule);
		List<PrpLWfTaskOut> PrpLWfTaskOuts = databaseDao.findAll(PrpLWfTaskOut.class,queryRule);
		PrpLWfTaskVo taskVo = null;
		if(prpLWfTaskIns.size() > 0 && prpLWfTaskIns != null){
			PrpLWfTaskIn registInPo = prpLWfTaskIns.get(0);
			registInPo.setBussTag(bussTag);
			registInPo.setShowInfoXML(showInfoXML);
			databaseDao.update(PrpLWfTaskIn.class,registInPo);
			taskVo = Beans.copyDepth().from(registInPo).to(PrpLWfTaskVo.class);
		}else{
			PrpLWfTaskOut registInPo = PrpLWfTaskOuts.get(0);
			registInPo.setBussTag(bussTag);
			registInPo.setShowInfoXML(showInfoXML);
			databaseDao.update(PrpLWfTaskOut.class,registInPo);
			taskVo = Beans.copyDepth().from(registInPo).to(PrpLWfTaskVo.class);
		}
/*		PrpLWfTaskIn registInPo = prpLWfTaskIns.get(0);
		registInPo.setBussTag(bussTag);
		registInPo.setShowInfoXML(showInfoXML);
		databaseDao.update(PrpLWfTaskIn.class,registInPo);
		PrpLWfTaskVo taskVo = Beans.copyDepth().from(registInPo).to(PrpLWfTaskVo.class);*/
		return taskVo;
	}
	
	/**
	 * 是否存在该节点 workStatus 0 表示查询 PrpLWfTaskIn表 ，1 表示查询 已处理的数据 PrpLWfTaskout表 ,"" 都需要查询 ☆yangkun(2016年2月23日 下午4:21:20): <br>
	 */
	public Boolean existTaskByNodeCode(String registNo,String taskInKey, FlowNode nodeCode,String workStatus) {
		String prantNode = nodeCode.getUpperNode();
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		
		if(StringUtils.isNotBlank(prantNode)){
			queryRule.addEqual("nodeCode",nodeCode.getRootNode().name());
			if(FlowNode.ChkBig == nodeCode || FlowNode.PLBig == nodeCode || FlowNode.PLVerify == nodeCode
					|| FlowNode.VLProp == nodeCode || FlowNode.VLCar == nodeCode || FlowNode.VPCar == nodeCode){
				queryRule.addLike("subNodeCode",nodeCode.name()+"%");
			}else{
				queryRule.addEqual("subNodeCode",nodeCode.name());
			}
		}else{
			queryRule.addEqual("nodeCode",nodeCode.name());
		}
		if(StringUtils.isNotBlank(taskInKey)){
			queryRule.addEqual("taskInKey",taskInKey);
		}
		
		if(StringUtils.isBlank(workStatus) || "0".equals(workStatus) ){
			List<PrpLWfTaskIn> prpLWfTaskIns = databaseDao.findTop(PrpLWfTaskIn.class,queryRule,1);
			if(prpLWfTaskIns!=null&& !prpLWfTaskIns.isEmpty()){// In表有数据，
				return true;
			}
		}
		if(StringUtils.isBlank(workStatus) || "1".equals(workStatus) ){
			queryRule.addNotEqual("handlerStatus",HandlerStatus.CANCEL);
			queryRule.addNotEqual("workStatus",WorkStatus.CANCEL);
			List<PrpLWfTaskOut> prpLWfTaskOuts = databaseDao.findAll(PrpLWfTaskOut.class,queryRule);
			if(prpLWfTaskOuts!=null&& !prpLWfTaskOuts.isEmpty()){// In表有数据，
				return true;
			}
		}
		return false;
	}

	/**
	 * 注销单个任务，将任务移动out表，变为已注销
	 * @param flowTaskId
	 * @modified: ☆LiuPing(2016年3月1日 ): <br>
	 */
	public void cancelTask(String userCode,BigDecimal... flowTaskId)  {
		try {
			for(BigDecimal taskId:flowTaskId){
				PrpLWfTaskIn oldTaskInPo = databaseDao.findByPK(PrpLWfTaskIn.class,taskId);
				PrpLWfTaskOut taskOutPo = null;
				if(oldTaskInPo!=null){// 等于null的时候说明已经转移了,要从out表找到他
					// 复制保存到taskout，并更改任务完成状态
					taskOutPo = Beans.copyDepth().from(oldTaskInPo).to(PrpLWfTaskOut.class);
					taskOutPo.setHandlerStatus(HandlerStatus.CANCEL);
					taskOutPo.setWorkStatus(WorkStatus.CANCEL);
					taskOutPo.setTaskOutNode(FlowNode.END.name());
					taskOutPo.setTaskOutKey(oldTaskInPo.getTaskInKey());
					taskOutPo.setTaskOutTime(new Date());
					taskOutPo.setTaskOutUser(userCode);
					taskOutPo.setAssignUser(userCode);
					taskOutPo.setHandlerUser(userCode);
					databaseDao.save(PrpLWfTaskOut.class,taskOutPo);
					// 删除旧的taskin
					databaseDao.deleteByPK(PrpLWfTaskIn.class,taskId);
				}
			}
		} catch (Exception e) {
			logger.info("userCode:" + userCode + "操作flowTaskId==" + flowTaskId + "的任务注销，在将wftaskin表数据移动到wftaskout表的过程中失败！", e);
		}
	}
	
	/**
	 * 退回下级
	 * @param uppernode
	 * @param currencyNode
	 * @param nextFlag
	 * @return
	 * @modified: ☆YangKun(2016年4月15日 下午3:13:52): <br>
	 */
	public List<PrpDNodeVo> findLowerNode(Double taskId,String currencyNode,String upperNode) {
		List<PrpDNodeVo> nodeList = new ArrayList<PrpDNodeVo>();
		Map<String,String> lowerMap = new HashMap<String,String>();
		nodeList = this.findLowerNode(nodeList,lowerMap,taskId,upperNode);
		Iterator<PrpDNodeVo> iter = nodeList.iterator();  
		while (iter.hasNext()){
			PrpDNodeVo nodeVo = iter.next();  
			String[] node = currencyNode.split("_LV");
			String[] subNode = nodeVo.getNodeCode().split("_LV");
			int level = Integer.parseInt(node[1]);//当前审核等级截取LV之后的数字
			int subLevel = Integer.parseInt(subNode[1]);
			if(level>9&&level<=12){//总公司逐级退回
				if(level-subLevel!=1) iter.remove();
			}else if(level<=subLevel){//排除本级和更高等级
				iter.remove();
			}
		}
		
		return nodeList;
	}
	
	private List<PrpDNodeVo> findLowerNode(List<PrpDNodeVo> nodeList,Map<String,String> lowerMap,Double taskId,String upperNode){
		PrpLWfTaskVo taskVo = this.queryTask(taskId);
		String nodeCode = taskVo.getSubNodeCode();
		if(taskVo !=null && taskVo.getSubNodeCode().contains(upperNode)){
			if(!lowerMap.containsKey(taskVo.getSubNodeCode())){
				lowerMap.put(taskVo.getSubNodeCode(),taskVo.getTaskName());
				PrpDNodeVo node = new PrpDNodeVo();
				node.setNodeCode(nodeCode);
				node.setNodeName(taskVo.getTaskName());
					
				nodeList.add(node);
			}
			this.findLowerNode(nodeList,lowerMap,taskVo.getUpperTaskId().doubleValue(),upperNode);
		}else{
			return nodeList;
		}
		
		return nodeList;
	}



	/**
	 * 查询立案的taskid
	 */
	public PrpLWfTaskVo queryTaskId(String flowId,String subNodeCode) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("flowId", flowId);
		queryRule.addEqual("subNodeCode", subNodeCode);
		PrpLWfTaskIn oldTaskInPo = databaseDao.findUnique(PrpLWfTaskIn.class,
				queryRule);

		PrpLWfTaskVo taskVo = null;
		if(oldTaskInPo!=null){
			taskVo = Beans.copyDepth().from(oldTaskInPo).to(PrpLWfTaskVo.class);
		}else{
			PrpLWfTaskOut oldTaskOutPo = databaseDao.findUnique(PrpLWfTaskOut.class,queryRule);
			taskVo = Beans.copyDepth().from(oldTaskOutPo).to(PrpLWfTaskVo.class);
		}
		return taskVo;
	}
	
	
	public void backCancel(BigDecimal flowTaskId,PrpLWfTaskVo... taskInVos) {
		// 找到旧的taskin数据
		if(flowTaskId==null){
			throw new IllegalArgumentException("flowTaskId 不能为空");
		}
		PrpLWfTaskIn oldTaskInPo = databaseDao.findByPK(PrpLWfTaskIn.class,flowTaskId);
		PrpLWfTaskOut taskOutPo = null;
		if(oldTaskInPo!=null){// 等于null的时候说明已经转移了,要从out表找到他
			// 复制保存到taskout，并更改任务完成状态
			taskOutPo = Beans.copyDepth().from(oldTaskInPo).to(PrpLWfTaskOut.class);
			taskOutPo.setHandlerStatus(HandlerStatus.END);
			taskOutPo.setWorkStatus(WorkStatus.END);
			if(taskInVos[0].getWorkStatus().equals(WorkStatus.BYBACK)){
				taskOutPo.setWorkStatus(WorkStatus.BACK);
			}else if(taskInVos[0].getWorkStatus().equals(WorkStatus.CANCEL)){
				taskOutPo.setWorkStatus(WorkStatus.CANCEL);
			}
			taskOutPo.setTaskOutNode(taskInVos[0].getSubNodeCode());
			taskOutPo.setTaskOutKey(taskInVos[0].getTaskInKey());
			taskOutPo.setTaskOutTime(taskInVos[0].getTaskInTime());
			taskOutPo.setTaskOutUser(taskInVos[0].getTaskInUser());
			databaseDao.save(PrpLWfTaskOut.class,taskOutPo);
			PrpLWfTaskIn po = null;
			po = databaseDao.findByPK(PrpLWfTaskIn.class,flowTaskId);
			if(po!=null){
				//Beans.copy().from(taskInVo).to(po);
				po.setWorkStatus(WorkStatus.BYBACK);
				po.setHandlerStatus(HandlerStatus.INIT);
				databaseDao.update(PrpLWfTaskIn.class,po);
			}
		} else {
			taskOutPo=databaseDao.findByPK(PrpLWfTaskOut.class,flowTaskId);
		}
	

	}

	/**
	 * 恢复注销单个任务，将任务移动in表
	 * @param flowTaskId
	 * @modified: ☆zhujunde: <br>
	 */
	public void cancelTaskRecover(String userCode,BigDecimal... flowTaskId) {

		for(BigDecimal taskId:flowTaskId){
			PrpLWfTaskOut  oldTaskOutPo = databaseDao.findByPK(PrpLWfTaskOut.class,taskId);
	/*		PrpLWfTaskIn taskInPo = null;
			if(oldTaskOutPo!=null){// 等于null的时候说明已经转移了,要从out表找到他
				// 复制保存到taskin
				taskInPo = Beans.copyDepth().from(oldTaskOutPo).to(PrpLWfTaskIn.class);
				taskInPo.setHandlerStatus(HandlerStatus.INIT);
				taskInPo.setWorkStatus(WorkStatus.INIT);
				taskInPo.setTaskOutNode(FlowNode.END.name());
				taskInPo.setTaskInKey(oldTaskOutPo.getTaskInKey());
				taskInPo.setTaskInTime(new Date());
				taskInPo.setTaskInUser(userCode);
				databaseDao.save(PrpLWfTaskIn.class,taskInPo);
				// 删除旧的taskout
				databaseDao.deleteByPK(PrpLWfTaskOut.class,taskId);
			}*/
			if(oldTaskOutPo!=null){// 等于null的时候说明已经转移了,要从out表找到他
				// 复制保存到taskin
				oldTaskOutPo.setHandlerStatus(HandlerStatus.END);
				oldTaskOutPo.setWorkStatus(WorkStatus.END);
				oldTaskOutPo.setTaskOutNode(FlowNode.END.name());
				//oldTaskOutPo.setTaskInTime(new Date());
				//oldTaskOutPo.setTaskInUser(userCode);
				//oldTaskOutPo.setTaskOutUser(userCode);
				databaseDao.update(PrpLWfTaskOut.class,oldTaskOutPo);
			}
		}
	}

	/**
	 * 立案注销
	 * @param flowTaskId
	 * @modified: ☆zhujunde: <br>
	 */
	public void cancelTaskClaim(String userCode,BigDecimal... flowTaskId) {

		for(BigDecimal taskId:flowTaskId){
			PrpLWfTaskOut taskOutPo = databaseDao.findByPK(PrpLWfTaskOut.class,taskId);
			//PrpLWfTaskOut taskOutPo = null;
			if(taskOutPo!=null){
				taskOutPo.setHandlerStatus(HandlerStatus.CANCEL);
				taskOutPo.setWorkStatus(WorkStatus.CANCEL);
				taskOutPo.setTaskOutNode(FlowNode.END.name());
				//taskOutPo.setTaskOutTime(new Date());
				//taskOutPo.setTaskOutUser(userCode);
				//taskOutPo.setAssignUser(userCode);
				databaseDao.update(PrpLWfTaskOut.class,taskOutPo);
			}
			}
		}

	/**
	 * 暂存任务，将任务变成处理中状态
	 * @param flowTaskId
	 * @param handlerIdKey
	 * @param handlerUser
	 * @param handlerCom
	 * @return
	 * @modified: ☆LiuPing(2016年1月12日 ): <br>
	 */
	public PrpLWfTaskVo tempSaveTaskComp(Double flowTaskId,String handlerIdKey,String handlerUser,String handlerCom) {
		// 找到旧的taskin数据
		PrpLWfTaskIn oldTaskInPo = databaseDao.findByPK(PrpLWfTaskIn.class,new BigDecimal(flowTaskId));
		if(oldTaskInPo!=null){// 可以多次暂存修改数据
			oldTaskInPo.setHandlerIdKey(handlerIdKey);
			oldTaskInPo.setHandlerUser(handlerUser);
			oldTaskInPo.setHandlerCom(handlerCom);
			oldTaskInPo.setHandlerTime(new Date());
			oldTaskInPo.setHandlerStatus(HandlerStatus.DOING);
			oldTaskInPo.setWorkStatus(WorkStatus.DOING);
			
			Date currentDate = new Date();
			logger.info("oldTaskInPo.setCompensateNo start...计算书号="+handlerIdKey);
			if(oldTaskInPo.getNodeCode().equals("Compe")){
				logger.info("oldTaskInPo.setCompensateNo doing...计算书号="+handlerIdKey);
				oldTaskInPo.setCompensateNo(handlerIdKey);
			}
			logger.info("oldTaskInPo.setCompensateNo end, 耗时 "+( System.currentTimeMillis()-currentDate.getTime() )+"毫秒");
			databaseDao.update(PrpLWfTaskIn.class,oldTaskInPo);
			//保存工作流操作人员
			if(StringUtils.isNotBlank(oldTaskInPo.getHandlerUser())){
				saveTaskUserInfo(oldTaskInPo.getFlowId(), oldTaskInPo.getHandlerUser());
			}
		}
		PrpLWfTaskVo taskVo = Beans.copyDepth().from(oldTaskInPo).to(PrpLWfTaskVo.class);
		return taskVo;
	}

	/**
	 * 查询当前任务taskId是由哪个任务发起的,一般用于某些任务被退回后，又要提交回那个任务
	 * @param flowId
	 * @param taskId
	 * @return
	 * @modified: ☆LiuPing(2016年6月1日 ): <br>
	 */
	public PrpLWfTaskVo findParentTask(String flowId,BigDecimal taskId) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("flowId",flowId);
		queryRule.addEqual("taskId",taskId);
		// 这个方法 一般用于当前任务提交回以前的节点，所以当前任务只在PrpLWfTaskIn表
		PrpLWfTaskIn taskInPo = databaseDao.findUnique(PrpLWfTaskIn.class,queryRule);
		if(taskInPo==null) return null;
		// 得到父任务的ID
		BigDecimal parentTaskId = taskInPo.getUpperTaskId();
		// 查询父任务信息
		QueryRule parentQryRule = QueryRule.getInstance();
		parentQryRule.addEqual("flowId",flowId);
		parentQryRule.addEqual("taskId",parentTaskId);
		PrpLWfTaskIn prantTaskInPo = databaseDao.findUnique(PrpLWfTaskIn.class,parentQryRule);
		PrpLWfTaskVo taskVo = null;
		if(prantTaskInPo!=null){
			taskVo = Beans.copyDepth().from(prantTaskInPo).to(PrpLWfTaskVo.class);
		}else{
			PrpLWfTaskOut oldTaskOutPo = databaseDao.findUnique(PrpLWfTaskOut.class,parentQryRule);
			taskVo = Beans.copyDepth().from(oldTaskOutPo).to(PrpLWfTaskVo.class);
		}
		return taskVo;
	}
	
	public void submitCancelTask(BigDecimal flowTaskId,PrpLWfTaskVo... taskInVos) {
		// 找到旧的taskin数据
		if(flowTaskId==null){
			throw new IllegalArgumentException("flowTaskId 不能为空");
		}
			
		PrpLWfTaskOut outPo = moveInToOut(flowTaskId,taskInVos[0]);
		String bussTag = outPo.getBussTag();
		String taskInNode = outPo.getSubNodeCode();
		String nodeCode = outPo.getNodeCode();

		// 定损提交到核价、核价提交到核损时、核损提交时，核价、核损的taskinNode,还是要保存PrpLWfTaskOut的taskinNode；
		if(FlowNode.VPrice.equals(nodeCode) || FlowNode.VLoss.equals(nodeCode)){
			taskInNode = outPo.getTaskInNode();
		}

		// 保存新的taskin
		for(PrpLWfTaskVo taskInVo:taskInVos){
			if( !FlowNode.END.equals(taskInVo.getNodeCode())){
				bussTag = TaskExtMapUtils.joinBussTag(bussTag,taskInVo.getBussTagMap());
				PrpLWfTaskIn oldtaskIn = queryTaskInByHandKey(taskInVo.getFlowId(),taskInVo.getHandlerIdKey(),taskInVo.getSubNodeCode());
				if(oldtaskIn!=null){
					throw new IllegalStateException("此任务已提交,不能再次提交!");
				}
				PrpLWfTaskIn taskInPo = new PrpLWfTaskIn();
				Beans.copy().from(taskInVo).excludeNull().to(taskInPo);
				taskInPo.setBussTag(bussTag);
				taskInPo.setTaskInNode(taskInNode);
				taskInPo.setUpperTaskId(flowTaskId);
				databaseDao.save(PrpLWfTaskIn.class,taskInPo);
				//保存工作流操作人员
				if(StringUtils.isNotBlank(taskInPo.getAssignUser())){
					saveTaskUserInfo(taskInPo.getFlowId(), taskInPo.getAssignUser());
				}
				taskInVo.setTaskId(taskInPo.getTaskId());
			}
		}

	}
	//立案注销
	public void submitTaskForCancel(BigDecimal flowTaskId,PrpLWfTaskVo... taskInVos) {
		// 找到旧的taskin数据
		if(flowTaskId==null){
			throw new IllegalArgumentException("flowTaskId 不能为空");
		}
		String bussTag = "";
		PrpLWfTaskOut outPo = moveInToOutForCancel(flowTaskId,taskInVos[0]);
	/*	if(outPo.getBussTag()!=null){
		bussTag = outPo.getBussTag();
		}*/
		String taskInNode = outPo.getSubNodeCode();
		String nodeCode = outPo.getNodeCode();

		// 定损提交到核价、核价提交到核损时、核损提交时，核价、核损的taskinNode,还是要保存PrpLWfTaskOut的taskinNode；
		if(FlowNode.VPrice.equals(nodeCode) || FlowNode.VLoss.equals(nodeCode)){
			taskInNode = outPo.getTaskInNode();
		}

		// 保存新的taskin
		for(PrpLWfTaskVo taskInVo:taskInVos){
			if( !FlowNode.END.equals(taskInVo.getNodeCode())){
				bussTag = TaskExtMapUtils.joinBussTag(bussTag,taskInVo.getBussTagMap());
				PrpLWfTaskIn oldtaskIn = queryTaskInByHandKey(taskInVo.getFlowId(),taskInVo.getHandlerIdKey(),taskInVo.getSubNodeCode());
				if(oldtaskIn!=null){
					throw new IllegalStateException("此任务已提交,不能再次提交!");
				}
				PrpLWfTaskIn taskInPo = new PrpLWfTaskIn();
				Beans.copy().from(taskInVo).excludeNull().to(taskInPo);
				taskInPo.setBussTag(bussTag);
				taskInPo.setTaskInNode(taskInNode);
				taskInPo.setUpperTaskId(flowTaskId);
				databaseDao.save(PrpLWfTaskIn.class,taskInPo);
				//保存工作流操作人员
				if(StringUtils.isNotBlank(taskInPo.getAssignUser())){
					saveTaskUserInfo(taskInPo.getFlowId(), taskInPo.getAssignUser());
				}
				taskInVo.setTaskId(taskInPo.getTaskId());
			}
		}

	}
	/** 将in数据转移到out,这里是根据下一个节点处理 */
	public PrpLWfTaskOut moveInToOutForCancel(BigDecimal flowTaskId,PrpLWfTaskVo taskInVo) {
		PrpLWfTaskIn oldTaskInPo = databaseDao.findByPK(PrpLWfTaskIn.class,flowTaskId);
		PrpLWfTaskOut taskOutPo = null;
		if(oldTaskInPo!=null){// 等于null的时候说明已经转移了,要从out表找到他
			// 复制保存到taskout，并更改任务完成状态
			taskOutPo = Beans.copyDepth().from(oldTaskInPo).to(PrpLWfTaskOut.class);
			taskOutPo.setHandlerStatus(HandlerStatus.END);
			taskOutPo.setWorkStatus(WorkStatus.END);
			if(taskInVo.getWorkStatus().equals(WorkStatus.BYBACK)){
				taskOutPo.setWorkStatus(WorkStatus.BACK);
			}else if(taskInVo.getWorkStatus().equals(WorkStatus.CANCEL)){
				taskOutPo.setWorkStatus(WorkStatus.CANCEL);
			}
			taskOutPo.setHandlerIdKey(taskInVo.getHandlerIdKey());
			taskOutPo.setTaskOutNode(taskInVo.getSubNodeCode());
			taskOutPo.setTaskOutKey(taskInVo.getTaskInKey());
			taskOutPo.setTaskOutTime(taskInVo.getTaskInTime());
			taskOutPo.setTaskOutUser(taskInVo.getTaskInUser());
			taskOutPo.setShowInfoXML(taskInVo.getShowInfoXML());
			//设置提交人
			taskOutPo.setHandlerUser(taskInVo.getHandlerUser());
			taskOutPo.setHandlerCom(taskInVo.getHandlerCom());
			databaseDao.save(PrpLWfTaskOut.class,taskOutPo);
			// 删除旧的taskin
			databaseDao.deleteByPK(PrpLWfTaskIn.class,flowTaskId);
			//保存工作流操作人员
			if(StringUtils.isNotBlank(taskOutPo.getTaskOutUser())){
				saveTaskUserInfo(taskOutPo.getFlowId(), taskOutPo.getTaskOutUser());
			}
		} else {
			taskOutPo=databaseDao.findByPK(PrpLWfTaskOut.class,flowTaskId);
		}
		
		return taskOutPo;
	}
	public List<PrpLWfTaskVo> findCanCelTask(String claimNo, FlowNode nodeCode) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("claimNo",claimNo);
		queryRule.addLike("subNodeCode",nodeCode.name());
		//queryRule.addDescOrder("taskOutTime");
		List<PrpLWfTaskIn> prpLWfTaskOuts = databaseDao.findAll(PrpLWfTaskIn.class,queryRule);
		List<PrpLWfTaskVo> prpLWfTaskOutVos = Beans.copyDepth().from(prpLWfTaskOuts).toList(PrpLWfTaskVo.class);
		return prpLWfTaskOutVos;
	}
	
	
	public List<PrpLWfTaskVo> findCompeByRegistNo(String registNo,String subNodecode) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		queryRule.addEqual("subNodeCode",subNodecode);
		queryRule.addNotEqual("workStatus", "9");
		
		//queryRule.addEqual("nodeCode","Certi");
		List<PrpLWfTaskIn> prpLWfTaskIns = databaseDao.findAll(PrpLWfTaskIn.class,queryRule);
		if(prpLWfTaskIns==null){
			List<PrpLWfTaskOut> prpLWfTaskOuts = databaseDao.findAll(PrpLWfTaskOut.class,queryRule);
			List<PrpLWfTaskVo> prpLWfTaskVo = Beans.copyDepth().from(prpLWfTaskOuts).toList(PrpLWfTaskVo.class);
			return prpLWfTaskVo;
		}else{
		List<PrpLWfTaskVo> prpLWfTaskVo = Beans.copyDepth().from(prpLWfTaskIns).toList(PrpLWfTaskVo.class);
		return prpLWfTaskVo;
		}
	}
	//通过报案号查询prpwftaskIn表的数据
	public  List<PrpLWfTaskVo> findPrpLWfTaskInByRegistNo(String registNo){
		List<PrpLWfTaskVo> prpLWfTaskVos=null;
		QueryRule queryRule =QueryRule.getInstance();
		queryRule.addEqual("registNo", registNo);
		List<PrpLWfTaskIn> prpLWfTaskIns = databaseDao.findAll(PrpLWfTaskIn.class,queryRule);
		if(prpLWfTaskIns !=null && prpLWfTaskIns.size()>0){
		prpLWfTaskVos = Beans.copyDepth().from(prpLWfTaskIns).toList(PrpLWfTaskVo.class);
		
		}
		return prpLWfTaskVos;
	}
	
	//通过报案号查询prpwftaskIn表的数据,按照任务流入时间降序排列
	public  List<PrpLWfTaskVo> findPrpLWfTaskInTimeDescByRegistNo(String registNo){
		List<PrpLWfTaskVo> prpLWfTaskVos=null;
		QueryRule queryRule =QueryRule.getInstance();
		queryRule.addEqual("registNo", registNo);
		queryRule.addDescOrder("taskInTime");
		List<PrpLWfTaskIn> prpLWfTaskIns = databaseDao.findAll(PrpLWfTaskIn.class,queryRule);
		if(prpLWfTaskIns !=null && prpLWfTaskIns.size()>0){
		prpLWfTaskVos = Beans.copyDepth().from(prpLWfTaskIns).toList(PrpLWfTaskVo.class);
		
		}
		return prpLWfTaskVos;
	}

	//通过报案号查询prpwftaskout表的数据
	public  List<PrpLWfTaskVo> findPrpLWfTaskOutByRegistNo(String registNo){
		List<PrpLWfTaskVo> prpLWfTaskVos=null;
		QueryRule queryRule =QueryRule.getInstance();
		queryRule.addEqual("registNo", registNo);
		List<PrpLWfTaskOut> prpLWfTaskOuts = databaseDao.findAll(PrpLWfTaskOut.class,queryRule);
		if(prpLWfTaskOuts !=null && prpLWfTaskOuts.size()>0){
		prpLWfTaskVos = Beans.copyDepth().from(prpLWfTaskOuts).toList(PrpLWfTaskVo.class);
		
		}
		return prpLWfTaskVos;
	}
	
	//通过报案号查询prpwftaskout表的数据,按照任务流入时间降序排列
	public  List<PrpLWfTaskVo> findPrpLWfTaskOutTimeDescByRegistNo(String registNo){
		List<PrpLWfTaskVo> prpLWfTaskVos=null;
		QueryRule queryRule =QueryRule.getInstance();
		queryRule.addEqual("registNo", registNo);
		queryRule.addDescOrder("taskInTime");
		List<PrpLWfTaskOut> prpLWfTaskOuts = databaseDao.findAll(PrpLWfTaskOut.class,queryRule);
		if(prpLWfTaskOuts !=null && prpLWfTaskOuts.size()>0){
		prpLWfTaskVos = Beans.copyDepth().from(prpLWfTaskOuts).toList(PrpLWfTaskVo.class);
		
		}
		return prpLWfTaskVos;
	}
	
	public BigDecimal deleteByRegist(PrpLRegistVo registVo){
		QueryRule queryRule =QueryRule.getInstance();
		queryRule.addEqual("flowId", registVo.getFlowId());
		queryRule.addEqual("registNo", registVo.getRegistNo());
		queryRule.addEqual("nodeCode", FlowNode.Claim.name());
		List<PrpLWfTaskIn> prpLWfTaskIns = databaseDao.findAll(PrpLWfTaskIn.class,queryRule);
		if(prpLWfTaskIns!=null&&prpLWfTaskIns.size()>0){
			for(PrpLWfTaskIn prpLWfTaskIn:prpLWfTaskIns){
				databaseDao.deleteByPK(PrpLWfTaskIn.class,prpLWfTaskIn.getTaskId());
			}
		}
		if(prpLWfTaskIns!=null&&prpLWfTaskIns.size()>0){
			return prpLWfTaskIns.get(0).getUpperTaskId();
		}else{
			return null;
		}
	}
	
	public BigDecimal findByRegist(PrpLRegistVo registVo){
		QueryRule queryRule =QueryRule.getInstance();
		queryRule.addEqual("flowId", registVo.getFlowId());
		queryRule.addEqual("registNo", registVo.getRegistNo());
		queryRule.addEqual("nodeCode", FlowNode.Claim.name());
		List<PrpLWfTaskIn> prpLWfTaskIns = databaseDao.findAll(PrpLWfTaskIn.class,queryRule);
		List<PrpLWfTaskOut> PrpLWfTaskOuts = databaseDao.findAll(PrpLWfTaskOut.class,queryRule);
		if(prpLWfTaskIns!=null&&prpLWfTaskIns.size()>0){
			return prpLWfTaskIns.get(0).getUpperTaskId();
		}else if(PrpLWfTaskOuts!=null&&PrpLWfTaskOuts.size()>0){
			return PrpLWfTaskOuts.get(0).getUpperTaskId();
		}else{
			return null;
		}
	}
	public String findByRegistNoAndNode(PrpLRegistVo registVo,String nodeCode) {
	QueryRule queryRule =QueryRule.getInstance();
	//queryRule.addEqual("flowId", registVo.getFlowId());
	queryRule.addEqual("registNo", registVo.getRegistNo());
	queryRule.addEqual("nodeCode", nodeCode);
	List<PrpLWfTaskIn> prpLWfTaskIns = databaseDao.findAll(PrpLWfTaskIn.class,queryRule);
	if(prpLWfTaskIns != null && prpLWfTaskIns.size() > 0){
		return prpLWfTaskIns.get(0).getWorkStatus();
	}else{
		List<PrpLWfTaskOut> prpLWfTaskOuts = databaseDao.findAll(PrpLWfTaskOut.class,queryRule);
		if(prpLWfTaskOuts!=null && prpLWfTaskOuts.size()>0){
			return prpLWfTaskOuts.get(0).getWorkStatus();
		}else{
			return "";
		}
		
	}
	}
	
	public List<PrpLWfTaskIn> findListByRegistNoAndNode(String registNo,String nodeCode) {
	QueryRule queryRule =QueryRule.getInstance();
	//queryRule.addEqual("flowId", registVo.getFlowId());
	queryRule.addEqual("registNo", registNo);
	queryRule.addEqual("nodeCode", nodeCode);
	List<PrpLWfTaskIn> prpLWfTaskIns = databaseDao.findAll(PrpLWfTaskIn.class,queryRule);
		if(prpLWfTaskIns != null && prpLWfTaskIns.size() > 0){
			return prpLWfTaskIns;
		}
		return null;
	}	
	/**
	 * taskIn 表是否存在节点
	 * @param registNo
	 * @param nodeCodeList
	 * @return
	 * @modified: ☆YangKun(2016年7月15日 下午1:05:25): <br>
	 */
	public Boolean existTaskByNodeList(String registNo, List<String> nodeCodeList) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		queryRule.addIn("nodeCode",nodeCodeList);

		List<PrpLWfTaskIn> prpLWfTaskIns = databaseDao.findAll(PrpLWfTaskIn.class,queryRule);
		if(prpLWfTaskIns!=null&& !prpLWfTaskIns.isEmpty()){// In表有数据，
			return true;
		}
		
		return false;
	}
	/**
	 * taskIn 表是否存在未处理节点 根据subnodecode判断
	 * @param registNo
	 * @param nodeCodeList
	 * @return
	 * @author WLL
	 */
	public Boolean existTaskInBySubNodeCode(String registNo, String subNodeCode) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		queryRule.addIn("subNodeCode",subNodeCode);

		List<PrpLWfTaskIn> prpLWfTaskIns = databaseDao.findAll(PrpLWfTaskIn.class,queryRule);
		if(prpLWfTaskIns!=null&& !prpLWfTaskIns.isEmpty()){// In表有数据，
			return true;
		}
		
		return false;
	}
	
	/**
	 * taskIn 表是否存在未处理节点 根据nodeCode判断
	 * @param registNo
	 * @param nodeCodeList
	 * @return
	 * @author 
	 */
	public Boolean existTaskInByNodeCode(String registNo, String nodeCode) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		queryRule.addIn("nodeCode",nodeCode);

		List<PrpLWfTaskIn> prpLWfTaskIns = databaseDao.findAll(PrpLWfTaskIn.class,queryRule);
		if(prpLWfTaskIns!=null&& !prpLWfTaskIns.isEmpty()){// In表有数据，
			return true;
		}
		
		return false;
	}
	
	
	/**
	 * taskIn 表是否存在节点
	 * @param registNo
	 * @param nodeCodeList
	 * @return
	 * @modified: ☆YangKun(2016年7月15日 下午1:05:25): <br>
	 */
	public Boolean existTaskByNode(String registNo, String nodeCode) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		queryRule.addEqual("nodeCode",nodeCode);

		List<PrpLWfTaskIn> prpLWfTaskIns = databaseDao.findAll(PrpLWfTaskIn.class,queryRule);
		List<PrpLWfTaskOut> prpLWfTaskOuts = databaseDao.findAll(PrpLWfTaskOut.class,queryRule);
		if(prpLWfTaskIns!=null&& !prpLWfTaskIns.isEmpty()){// In表有数据，
			return true;
		}else{
			if(prpLWfTaskOuts!=null && !prpLWfTaskOuts.isEmpty()){
				return true;
			}else{
				return false;
			}
		}
	}

	/**
	 * taskIn和out 是否存在存在未注销节点
	 * @param registNo
	 * @param nodeCodeList
	 * @return
	 * @modified: ☆zjd(2016年7月15日 下午1:05:25): <br>
	 */
	public Boolean existCancelByNode(String registNo, String nodeCode,String handlerStatus) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		queryRule.addEqual("subNodeCode",nodeCode);
		List<PrpLWfTaskIn> prpLWfTaskIns = databaseDao.findAll(PrpLWfTaskIn.class,queryRule);
		
		if(prpLWfTaskIns!=null&& !prpLWfTaskIns.isEmpty()){// In表有数据，
			return true;
		}else{
			queryRule.addEqual("handlerStatus",handlerStatus);
			List<PrpLWfTaskOut> prpLWfTaskOuts = databaseDao.findAll(PrpLWfTaskOut.class,queryRule);
			if(prpLWfTaskOuts!=null&& !prpLWfTaskOuts.isEmpty()){
				return true;
			}else{
				return false;
			}
		}
	}
	
	/**
	 * taskIn 是否存在存在未注销节点
	 * @param registNo
	 * @param nodeCodeList
	 * @return
	 * @modified: ☆zjd(2016年7月15日 下午1:05:25): <br>
	 */
	public Boolean existCancelByNAndH(String registNo, String nodeCode) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		queryRule.addEqual("subNodeCode",nodeCode);
		List<PrpLWfTaskIn> prpLWfTaskIns = databaseDao.findAll(PrpLWfTaskIn.class,queryRule);
		
		if(prpLWfTaskIns!=null&& !prpLWfTaskIns.isEmpty()){// In表有数据，
			return true;
		}else{
			queryRule.addDescOrder("taskInTime");
			List<PrpLWfTaskOut> prpLWfTaskOuts = databaseDao.findAll(PrpLWfTaskOut.class,queryRule);
			if(prpLWfTaskOuts!=null&& !prpLWfTaskOuts.isEmpty()){
				if(!WorkStatus.CANCEL.equals(prpLWfTaskOuts.get(0).getWorkStatus())){
					return true;
				}else{
					return false;
				}
				
			}else{
				return false;
			}
		}
	}
	
	/**
	 * 暂存追偿任务，将任务变成处理中状态
	 * @param flowTaskId
	 * @param handlerIdKey
	 * @param handlerUser
	 * @param handlerCom
	 * @param replevyMainVo
	 * @return
	 */
	public PrpLWfTaskVo tempSaveTaskByRecPay(Double flowTaskId,String handlerIdKey,String handlerUser,String handlerCom,PrplReplevyMainVo replevyMainVo) {
		// 找到旧的taskin数据
		PrpLWfTaskIn oldTaskInPo = databaseDao.findByPK(PrpLWfTaskIn.class,new BigDecimal(flowTaskId));
		TaskExtMapUtils extMapUtil = new TaskExtMapUtils();
		extMapUtil.addXmlMap(replevyMainVo,"replevyType","sumPlanReplevy");
		oldTaskInPo.setShowInfoXML(extMapUtil.getShowInfoXML());
		if(oldTaskInPo!=null&&oldTaskInPo.getHandlerTime()==null){// 防止多次暂存修改数据
			oldTaskInPo.setHandlerIdKey(handlerIdKey);
			oldTaskInPo.setHandlerUser(handlerUser);
			oldTaskInPo.setHandlerCom(handlerCom);
			oldTaskInPo.setHandlerTime(new Date());
			oldTaskInPo.setHandlerStatus(HandlerStatus.DOING);
			oldTaskInPo.setWorkStatus(WorkStatus.DOING);
			
			databaseDao.update(PrpLWfTaskIn.class,oldTaskInPo);
			//保存工作流操作人员
			if(StringUtils.isNotBlank(oldTaskInPo.getHandlerUser())){
				saveTaskUserInfo(oldTaskInPo.getFlowId(),oldTaskInPo.getHandlerUser());
			}
		}
		PrpLWfTaskVo taskVo = Beans.copyDepth().from(oldTaskInPo).to(PrpLWfTaskVo.class);
		return taskVo;
	}
	
	/**
	 * 通过taskIds集合查找
	 * @param taskIds
	 * @return
	 */
	public List<PrpLWfTaskVo> queryTaskList(List<BigDecimal> taskIds){
		List<PrpLWfTaskVo> prpLWfTaskVos = new ArrayList<PrpLWfTaskVo>();
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addIn("taskId", taskIds);
		List<PrpLWfTaskIn> prpLWfTaskIns = databaseDao.findAll(PrpLWfTaskIn.class,queryRule);
		List<PrpLWfTaskOut> prpLWfTaskOuts = databaseDao.findAll(PrpLWfTaskOut.class,queryRule);
		if(prpLWfTaskIns!=null && !prpLWfTaskIns.isEmpty()){
			prpLWfTaskVos.addAll(Beans.copyDepth().from(prpLWfTaskIns).toList(PrpLWfTaskVo.class));
		}
		if(prpLWfTaskOuts!=null && !prpLWfTaskOuts.isEmpty()){
			prpLWfTaskVos.addAll(Beans.copyDepth().from(prpLWfTaskOuts).toList(PrpLWfTaskVo.class));
		}
		return prpLWfTaskVos;
	}
	
	/**
	 * 同一节点下不同任务id查找任务集合
	 * @param handlerIdKeys
	 * @param nodeCode
	 * @return
	 */
	public List<PrpLWfTaskVo> queryTaskList(List<String> handlerIdKeys,String nodeCode,String registNo){
		List<PrpLWfTaskVo> prpLWfTaskVos = new ArrayList<PrpLWfTaskVo>();
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addIn("handlerIdKey", handlerIdKeys);
		queryRule.addEqual("nodeCode", nodeCode);
		queryRule.addEqual("registNo", registNo);
		List<PrpLWfTaskIn> prpLWfTaskIns = databaseDao.findAll(PrpLWfTaskIn.class,queryRule);
		List<PrpLWfTaskOut> prpLWfTaskOuts = databaseDao.findAll(PrpLWfTaskOut.class,queryRule);
		if(prpLWfTaskIns!=null && !prpLWfTaskIns.isEmpty()){
			prpLWfTaskVos.addAll(Beans.copyDepth().from(prpLWfTaskIns).toList(PrpLWfTaskVo.class));
		}
		if(prpLWfTaskOuts!=null && !prpLWfTaskOuts.isEmpty()){
			prpLWfTaskVos.addAll(Beans.copyDepth().from(prpLWfTaskOuts).toList(PrpLWfTaskVo.class));
		}
		return prpLWfTaskVos;
	}
	//findByRegist
	public BigDecimal deleteByRegists(PrpLRegistVo registVo,String subNodeCode){
		QueryRule queryRule1 =QueryRule.getInstance();
		queryRule1.addEqual("flowId", registVo.getFlowId());
		queryRule1.addEqual("registNo", registVo.getRegistNo());
		queryRule1.addEqual("nodeCode", FlowNode.Claim.name());
		BigDecimal flags = null;
		List<PrpLWfTaskIn> prpLWfTaskInes = databaseDao.findAll(PrpLWfTaskIn.class,queryRule1);
		//if(prpLWfTaskInes!=null || PrpLWfTaskOuts)
		if(prpLWfTaskInes != null &&prpLWfTaskInes.size()>0){
			if(prpLWfTaskInes.get(0).getSubNodeCode().equals(subNodeCode)){//生成相反的立案
				flags =  prpLWfTaskInes.get(0).getUpperTaskId();
			}
		}
		QueryRule queryRule =QueryRule.getInstance();
		queryRule.addEqual("flowId", registVo.getFlowId());
		queryRule.addEqual("registNo", registVo.getRegistNo());
		queryRule.addEqual("nodeCode", FlowNode.Claim.name());
		queryRule.addEqual("subNodeCode", subNodeCode);
		List<PrpLWfTaskIn> prpLWfTaskIns = databaseDao.findAll(PrpLWfTaskIn.class,queryRule);
		if(prpLWfTaskIns!=null&&prpLWfTaskIns.size()>0){
			for(PrpLWfTaskIn prpLWfTaskIn:prpLWfTaskIns){
				databaseDao.deleteByPK(PrpLWfTaskIn.class,prpLWfTaskIn.getTaskId());
			}
		}
		/*if(prpLWfTaskIns!=null&&prpLWfTaskIns.size()>0){
			return prpLWfTaskIns.get(0).getUpperTaskId();
		}else{
			return null;
		}*/
		QueryRule queryRule2 =QueryRule.getInstance();
		queryRule2.addEqual("flowId", registVo.getFlowId());
		queryRule2.addEqual("registNo", registVo.getRegistNo());
		queryRule2.addEqual("nodeCode", FlowNode.Claim.name());
		prpLWfTaskInes = databaseDao.findAll(PrpLWfTaskIn.class,queryRule2);
		if(prpLWfTaskInes != null &&prpLWfTaskInes.size()>0){
			flags = null;
		}
		logger.info("删除商业flags========================================================="+flags);
		logger.info("删除商业flags========================================================="+flags);
		return flags;
	}
	
	/**
	 * 保存工作流操作人员信息
	 * @param flowTaskId
	 * @param userCode
	 */
	public void saveTaskUserInfo(String flowId,String userCode){
		PrpLWfTaskUserInfo prpLWfTaskUserInfo = new PrpLWfTaskUserInfo();
		prpLWfTaskUserInfo.setFlowId(flowId);
		prpLWfTaskUserInfo.setUserCode(userCode);
		prpLWfTaskUserInfo.setInputTime(new Date());
		databaseDao.save(PrpLWfTaskUserInfo.class, prpLWfTaskUserInfo);
	}
	
	/**
	 * 更新PrpLWfTaskUserInfoVo
	 * <pre></pre>
	 * @param fTaskUserInfoVo
	 * @modified:
	 * ☆zhujunde(2019年4月8日 下午2:42:03): <br>
	 */
	public void updateTaskUserInfo(PrpLWfTaskUserInfoVo fTaskUserInfoVo) {
		PrpLWfTaskUserInfo po = null;
		po = databaseDao.findByPK(PrpLWfTaskUserInfo.class,fTaskUserInfoVo.getId());
		if(po!=null){
			Beans.copy().from(fTaskUserInfoVo).to(po);
			databaseDao.update(PrpLWfTaskUserInfo.class,po);
		}
	}
	
	/**
	 * 查询PrpLWfTaskUserInfoVo
	 * <pre></pre>
	 * @param flowId
	 * @return
	 * @modified:
	 * ☆zhujunde(2019年4月8日 下午2:53:29): <br>
	 */
	public List<PrpLWfTaskUserInfoVo> queryTaskUserInfo(String flowId) {
		List<PrpLWfTaskUserInfoVo> prpLWfTaskUserInfoVoList = new ArrayList<PrpLWfTaskUserInfoVo>();
		QueryRule queryRule =QueryRule.getInstance();
		queryRule.addEqual("flowId", flowId);
		queryRule.addDescOrder("id");
		List<PrpLWfTaskUserInfo> poList = databaseDao.findAll(PrpLWfTaskUserInfo.class,queryRule);
		prpLWfTaskUserInfoVoList = Beans.copyDepth().from(poList).toList(PrpLWfTaskUserInfoVo.class);
		return prpLWfTaskUserInfoVoList;
	}
	/*private String findFlowIdByTaksId(BigDecimal flowTaskId){
		QueryRule queryRule =QueryRule.getInstance();
		queryRule.addEqual("taskId", flowTaskId);
		PrpLWfTaskIn prpLWfTaskIn = databaseDao.findByPK(PrpLWfTaskIn.class, queryRule);
	    if(prpLWfTaskIn != null){
	    	return prpLWfTaskIn.getFlowId();
	    }else{
	    	QueryRule queryRule1 =QueryRule.getInstance();
			queryRule1.addEqual("taskId", flowTaskId);
			PrpLWfTaskOut prpLWfTaskOut = databaseDao.findByPK(PrpLWfTaskOut.class, queryRule1);
	        if(prpLWfTaskOut != null){
	        	return prpLWfTaskOut.getFlowId();
	        }else{
	        	return "";
	        }
	    }
	}*/
	
	
	/**
	 * 注销单个任务，将任务移动out表，变为已注销
	 * @param flowTaskId
	 * @modified: ☆LiuPing(2016年3月1日 ): <br>
	 */
	public void cancelTaskForOther(String registNo, String userCode) {

		//根据报案号查询in表的所有数据
		List<PrpLWfTaskVo> list = findPrpLWfTaskInByRegistNo(registNo);
		//for(BigDecimal taskId:PrpLWfTaskVo.get){
		if(list!=null && list.size()>0){
			for(int i=0;i<list.size();i++){
				PrpLWfTaskIn oldTaskInPo = databaseDao.findByPK(PrpLWfTaskIn.class,list.get(i).getTaskId());
				PrpLWfTaskOut taskOutPo = null;
				if(oldTaskInPo!=null){// 等于null的时候说明已经转移了,要从out表找到他
					// 复制保存到taskout，并更改任务完成状态
					taskOutPo = Beans.copyDepth().from(oldTaskInPo).to(PrpLWfTaskOut.class);
					taskOutPo.setFlag(taskOutPo.getWorkStatus());
					taskOutPo.setWorkStatus(WorkStatus.PAUSE);
					//taskOutPo.setTaskOutNode(FlowNode.END.name());
					taskOutPo.setTaskOutKey(oldTaskInPo.getTaskInKey());
					taskOutPo.setTaskOutTime(new Date());
					taskOutPo.setTaskOutUser(userCode);
					databaseDao.save(PrpLWfTaskOut.class,taskOutPo);
					// 删除旧的taskin
					databaseDao.deleteByPK(PrpLWfTaskIn.class,list.get(i).getTaskId());
				}
			}
		}
	}
	
	/**
	 * 恢复单个任务，将任务移动in表，变为原来的状态
	 * @modified: ☆zhujunde(2016年3月1日 ): <br>
	 */
	public void recoverClaimForOther(String registNo, String userCode,BigDecimal flowTaskId) {

		//根据报案号查询in表的所有数据
		List<PrpLWfTaskVo> list = findPrpLWfTaskOutByRegistNo(registNo);
		//for(BigDecimal taskId:PrpLWfTaskVo.get){
		if(list!=null && list.size()>0){
			for(int i=0;i<list.size();i++){
				if(!flowTaskId.equals(list.get(i).getTaskId())){
					PrpLWfTaskOut oldTaskOutPo = databaseDao.findByPK(PrpLWfTaskOut.class,list.get(i).getTaskId());
					PrpLWfTaskIn taskInPo = null;
					if(oldTaskOutPo!=null){// 等于null的时候说明已经转移了,要从out表找到他
						// 复制保存到taskout，并更改任务完成状态
						taskInPo = Beans.copyDepth().from(oldTaskOutPo).to(PrpLWfTaskIn.class);
						if(taskInPo.getFlag()!=null){
							taskInPo.setWorkStatus(taskInPo.getFlag());
							taskInPo.setTaskInKey(oldTaskOutPo.getTaskOutKey());
							databaseDao.save(PrpLWfTaskIn.class,taskInPo);
							// 删除旧的taskin
							databaseDao.deleteByPK(PrpLWfTaskOut.class,list.get(i).getTaskId());
						}
					}
				}
			}
		}
	}
	
	
	/**
	 * 查询工作流状态
	 * @param flowTaskId
	 * @return
	 * @modified: ☆LiuPing(2016年1月19日 ): <br>
	 */
	public PrpLWfTaskVo queryTaskForHandlerStatus(Double flowTaskId) {
		PrpLWfTaskIn oldTaskInPo = databaseDao.findByPK(PrpLWfTaskIn.class,new BigDecimal(flowTaskId));
		String handlerStatus = "";
		if(oldTaskInPo!=null){
			PrpLWfTaskVo taskVo = Beans.copyDepth().from(oldTaskInPo).to(PrpLWfTaskVo.class);
			return taskVo;
			//handlerStatus = oldTaskInPo.getHandlerStatus();
		}else{
			PrpLWfTaskOut oldTaskOutPo = databaseDao.findByPK(PrpLWfTaskOut.class,new BigDecimal(flowTaskId));
			PrpLWfTaskVo taskVo = Beans.copyDepth().from(oldTaskOutPo).to(PrpLWfTaskVo.class);
			return taskVo;
			//handlerStatus = oldTaskOutPo.getHandlerStatus();
		}
		//return handlerStatus;
	}
	
	
	/**
	 * 根据报案号，立案号，节点，工作流状态查询唯一taskid
	 * <pre></pre>
	 * @param registNo
	 * @param claimNo
	 * @param subNodeCode
	 * @param handlerStatus
	 * @return
	 * @modified:
	 * ☆zhujunde(2018年3月1日 上午11:02:22): <br>
	 */
	public PrpLWfTaskVo queryTaskByAny(String registNo,String claimNo,String subNodeCode,String handlerStatus) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("claimNo",claimNo);
		queryRule.addEqual("subNodeCode",subNodeCode);
		queryRule.addEqual("handlerStatus",handlerStatus);
		queryRule.addDescOrder("taskInTime");
		PrpLWfTaskIn prpLWfTaskIns = databaseDao.findUnique(PrpLWfTaskIn.class,queryRule);
		if(prpLWfTaskIns!=null){
			PrpLWfTaskVo taskVo = Beans.copyDepth().from(prpLWfTaskIns).to(PrpLWfTaskVo.class);
			return taskVo;
		}else{
			PrpLWfTaskOut prpLWfTaskOut = databaseDao.findUnique(PrpLWfTaskOut.class,queryRule);
			PrpLWfTaskVo taskVo = Beans.copyDepth().from(prpLWfTaskOut).to(PrpLWfTaskVo.class);
			return taskVo;
		}
		
	}
	
	/**
	 * 将工作流节点从out表的数据转移到in表
	 * @param flowTaskId
	 * @param taskInVo
	 * @return
	 * @modified:
	 * ☆XMSH(2016年5月17日 上午9:25:21): <br>
	 */
	public void moveOutToIn(BigDecimal flowTaskId) {
		PrpLWfTaskOut oldTaskOutPo = databaseDao.findByPK(PrpLWfTaskOut.class,flowTaskId);
		PrpLWfTaskIn_NoGenId taskInPo = null;
		if(oldTaskOutPo!=null){// 等于null的时候说明已经转移了,要从out表找到他
			// 复制保存到taskin，
			taskInPo = Beans.copyDepth().from(oldTaskOutPo).to(PrpLWfTaskIn_NoGenId.class);
			taskInPo.setHandlerStatus(HandlerStatus.DOING);
			taskInPo.setWorkStatus(WorkStatus.DOING);
			taskInPo.setTaskId(oldTaskOutPo.getTaskId());
//			taskInPo.setTaskOutNode(null);
			
			databaseDao.save(PrpLWfTaskIn_NoGenId.class,taskInPo);
			// 删除旧的taskout
			databaseDao.deleteByPK(PrpLWfTaskOut.class,flowTaskId);
		} 
	}
	
	public PrpLWfTaskVo findWftaskInByRegistNoAndSubNodeCode(String registNo,String subNodeCode) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		queryRule.addEqual("subNodeCode",subNodeCode);
		
		List<PrpLWfTaskIn> prpLWfTaskIns = databaseDao.findAll(PrpLWfTaskIn.class,queryRule);
		if(prpLWfTaskIns != null && prpLWfTaskIns.size() > 0){
			PrpLWfTaskVo prpLWfTaskVo = Beans.copyDepth().from(prpLWfTaskIns.get(0)).to(PrpLWfTaskVo.class);
			return prpLWfTaskVo;
		}
		return null;
		
	}
	
	public List<PrpLWfTaskVo> findWftaskByRegistNoAndNodeCode(String registNo,String subNodeCode){
		QueryRule query=QueryRule.getInstance();
		query.addEqual("registNo",registNo);
		query.addEqual("subNodeCode",subNodeCode);
		List<PrpLWfTaskVo> prpLWfTaskVos=new ArrayList<PrpLWfTaskVo>();
		List<PrpLWfTaskIn> prpLWfTaskIns = databaseDao.findAll(PrpLWfTaskIn.class,query);
		if(prpLWfTaskIns!=null&&prpLWfTaskIns.size()>0){
			  for(PrpLWfTaskIn prpIn:prpLWfTaskIns){
				  PrpLWfTaskVo prpLWfTaskVo=new PrpLWfTaskVo();
				  prpLWfTaskVo=Beans.copyDepth().from(prpIn).to(PrpLWfTaskVo.class);
				  prpLWfTaskVos.add(prpLWfTaskVo);
			  }
			
		}else{
			List<PrpLWfTaskOut> prpLWfTaskOuts = databaseDao.findAll(PrpLWfTaskOut.class,query);
			if(prpLWfTaskOuts!=null && prpLWfTaskOuts.size()>0){
				for(PrpLWfTaskOut prpout:prpLWfTaskOuts){
				  PrpLWfTaskVo prpLWfTaskVo=new PrpLWfTaskVo();
				  prpLWfTaskVo=Beans.copyDepth().from(prpout).to(PrpLWfTaskVo.class);
				  prpLWfTaskVos.add(prpLWfTaskVo);
			    }
			
			}
		 }
		return prpLWfTaskVos;
	 }
	

    public List<PrpLWfTaskVo> findEndTaskAndNotBack(String registNo,String handlerIdKey,FlowNode nodeCode) {
        String prantNode = nodeCode.getUpperNode();
        QueryRule queryRule = QueryRule.getInstance();
        queryRule.addEqual("registNo",registNo);
        if(StringUtils.isNotBlank(handlerIdKey)){
            queryRule.addEqual("handlerIdKey",handlerIdKey);
        }
        
        if(StringUtils.isNotBlank(prantNode)){
            queryRule.addEqual("nodeCode",nodeCode.getRootNode().name());
            if(FlowNode.ChkBig == nodeCode || FlowNode.PLBig == nodeCode || FlowNode.PLVerify == nodeCode
                    || FlowNode.VLProp == nodeCode || FlowNode.VLCar == nodeCode || FlowNode.VPCar == nodeCode){
                queryRule.addLike("subNodeCode",nodeCode.name()+"%");
            }else{
                queryRule.addEqual("subNodeCode",nodeCode.name());
            }
            
        }else{
            queryRule.addEqual("nodeCode",nodeCode.name());
        }
        queryRule.addDescOrder("taskOutTime");
        List<PrpLWfTaskOut> prpLWfTaskOuts = databaseDao.findAll(PrpLWfTaskOut.class,queryRule);
        List<PrpLWfTaskVo> prpLWfTasks = Beans.copyDepth().from(prpLWfTaskOuts).toList(PrpLWfTaskVo.class);
        List<PrpLWfTaskVo> prpLWfTaskOutVos = new ArrayList<PrpLWfTaskVo>();
        for(PrpLWfTaskVo prpLWfTaskVo:prpLWfTasks){
            if(!("5".equals(prpLWfTaskVo.getWorkStatus()))){
                prpLWfTaskOutVos.add(prpLWfTaskVo);
            }
        }
        return prpLWfTaskOutVos;
    }
    /**
     * 恢复单个任务，将任务移动in表，变为原来的状态
     * @modified: ☆zhujunde(2016年3月1日 ): <br>
     */
/*    public void recoverFlow(String registNo, String userCode) {

        //根据报案号查询in表的所有数据
        List<PrpLWfTaskVo> list = findPrpLWfTaskOutByRegistNo(registNo);
        //for(BigDecimal taskId:PrpLWfTaskVo.get){
        if(list!=null && list.size()>0){
            for(int i=0;i<list.size();i++){
                PrpLWfTaskOut oldTaskOutPo = databaseDao.findByPK(PrpLWfTaskOut.class,list.get(i).getTaskId());
                PrpLWfTaskIn taskInPo = null;
                if(oldTaskOutPo!=null){// 等于null的时候说明已经转移了,要从out表找到他
                    // 复制保存到taskout，并更改任务完成状态
                    taskInPo = Beans.copyDepth().from(oldTaskOutPo).to(PrpLWfTaskIn.class);
                    if(taskInPo.getFlag()!=null){
                        taskInPo.setWorkStatus(taskInPo.getFlag());
                        taskInPo.setTaskInKey(oldTaskOutPo.getTaskOutKey());
                        databaseDao.save(PrpLWfTaskIn.class,taskInPo);
                        // 删除旧的taskin
                        databaseDao.deleteByPK(PrpLWfTaskOut.class,list.get(i).getTaskId());
                    }
                }
            }
        }
    }*/
	
	/**
     * 
     * 查询in表数据，根据流入时间排序
     * @param registNo
     * @param handlerIdKey
     * @param nodeCode
     * @return
     * @modified:
     * ☆zhujunde(2017年6月15日 下午3:42:58): <br>
     */
    public List<PrpLWfTaskVo> findInTask(String registNo,String handlerIdKey,String subNodeCode) {
        QueryRule queryRule = QueryRule.getInstance();
        queryRule.addEqual("registNo",registNo);
        if(StringUtils.isNotBlank(handlerIdKey)){
            queryRule.addEqual("handlerIdKey",handlerIdKey);
        }
        
        if(StringUtils.isNotBlank(subNodeCode)){
            queryRule.addEqual("subNodeCode",subNodeCode);
        }
        queryRule.addDescOrder("taskInTime");
        
        List<PrpLWfTaskIn> prpLWfTaskIns = databaseDao.findAll(PrpLWfTaskIn.class,queryRule);
        List<PrpLWfTaskVo> prpLWfTaskInVos = Beans.copyDepth().from(prpLWfTaskIns).toList(PrpLWfTaskVo.class);
        return prpLWfTaskInVos;
    }

    public List<PrpLWfTaskVo> findInTaskbyCompensateNo(String registNo,String compensateNo,String subNodeCode) {
        QueryRule queryRule = QueryRule.getInstance();
        queryRule.addEqual("registNo",registNo);
        if(StringUtils.isNotBlank(compensateNo)){
            queryRule.addEqual("compensateNo",compensateNo);
        }
        
        if(StringUtils.isNotBlank(subNodeCode)){
            queryRule.addEqual("subNodeCode",subNodeCode);
        }
        queryRule.addDescOrder("taskInTime");
        
        List<PrpLWfTaskIn> prpLWfTaskIns = databaseDao.findAll(PrpLWfTaskIn.class,queryRule);
        List<PrpLWfTaskVo> prpLWfTaskInVos = Beans.copyDepth().from(prpLWfTaskIns).toList(PrpLWfTaskVo.class);
        return prpLWfTaskInVos;
    }
    
    /**
     * 根据taskid更新In表的标志位
     * <pre></pre>
     * @param taskId
     * @param flag
     * @return
     * @modified:
     * ☆WLL(2017年8月28日 下午4:22:29): <br>
     */
    public PrpLWfTaskVo updateTaskInFlag(Double taskId,String flag){
    	PrpLWfTaskVo taskInVo = null;
    	BigDecimal id = BigDecimal.valueOf(taskId);
    	if(taskId != null){
    		PrpLWfTaskIn taskInPo = databaseDao.findByPK(PrpLWfTaskIn.class,id);
    		if(taskInPo != null){
    			taskInPo.setFlag(flag);//更新flag标志位
    			Beans.copy().from(taskInPo).to(taskInVo);
    		}
    	}
    	return taskInVo;
    }

    
 

    
    public void updateIsMobileCaseByFlowId(List<PrpLWfTaskVo> taskInVoList) {
        for(PrpLWfTaskVo taskInVo : taskInVoList){
            PrpLWfTaskOut outPo = null;
            PrpLWfTaskIn inPo = null;
            outPo = databaseDao.findByPK(PrpLWfTaskOut.class,taskInVo.getTaskId());
            if(outPo != null){
                outPo.setIsMobileAccept(taskInVo.getIsMobileAccept());
                databaseDao.update(PrpLWfTaskOut.class,outPo);
            }else{
                inPo = databaseDao.findByPK(PrpLWfTaskIn.class,taskInVo.getTaskId());
                if(inPo != null){
                    inPo.setIsMobileAccept(taskInVo.getIsMobileAccept());
                    databaseDao.update(PrpLWfTaskIn.class,inPo);
                }
            }
        }
    }
    
    /**
     * 
     * 查询in表数据，根据流入时间排序
     * @param registNo
     * @param handlerIdKey
     * @param nodeCode
     * @return
     * @modified:
     * ☆zhujunde(2017年6月15日 下午3:42:58): <br>
     */
    public List<PrpLWfTaskVo> findInTaskByOther(String registNo,String handlerIdKey,String nodeCode) {
        QueryRule queryRule = QueryRule.getInstance();
        queryRule.addEqual("registNo",registNo);
        if(StringUtils.isNotBlank(handlerIdKey)){
            queryRule.addEqual("handlerIdKey",handlerIdKey);
        }
        
        if(StringUtils.isNotBlank(nodeCode)){
            queryRule.addEqual("nodeCode",nodeCode);
        }
        queryRule.addDescOrder("taskInTime");
        
        List<PrpLWfTaskIn> prpLWfTaskIns = databaseDao.findAll(PrpLWfTaskIn.class,queryRule);
		List<PrpLWfTaskVo> prpLWfTaskInVos = new ArrayList<PrpLWfTaskVo>();
        if (prpLWfTaskIns != null && prpLWfTaskIns.size() > 0) {
			prpLWfTaskInVos = Beans.copyDepth().from(prpLWfTaskIns).toList(PrpLWfTaskVo.class);
		}
        return prpLWfTaskInVos;
    }
    /**
     * 
     * 查询out表数据，根据流入时间排序
     * @param registNo
     * @param handlerIdKey
     * @param nodeCode
     * @return
     * @modified:
     * ☆zhujunde(2017年6月15日 下午3:42:58): <br>
     */
    public List<PrpLWfTaskVo> findTaskByReOpen(String registNo,String taskInKey,String nodeCode) {
        QueryRule queryRule = QueryRule.getInstance();
        queryRule.addEqual("registNo",registNo);
        if(StringUtils.isNotBlank(taskInKey)){
            queryRule.addEqual("taskInKey",taskInKey);
        }
        if(StringUtils.isNotBlank(nodeCode)){
            queryRule.addEqual("subNodeCode",nodeCode);
        }
        queryRule.addDescOrder("taskId");
        List<PrpLWfTaskOut> prpLWfTaskOuts = databaseDao.findAll(PrpLWfTaskOut.class,queryRule);
        List<PrpLWfTaskVo> prpLWfTaskInVos = Beans.copyDepth().from(prpLWfTaskOuts).toList(PrpLWfTaskVo.class);
        return prpLWfTaskInVos;
    }

    /**
     * 通过flowId和节点查询out表或in表（flag=1表示查in表，flag=2表示查out表）
     * @param flowId
     * @param nodeCode
     * @param flag
     * @return
     */
    public List<PrpLWfTaskVo> findPrpLWfTaskByFlowIdAndnodeCode(String flowId, String flag,List<String> nodeCodes){
    	QueryRule rule=QueryRule.getInstance();
    	rule.addEqual("flowId",flowId);
    	rule.addIn("nodeCode",nodeCodes);
    	rule.addAscOrder("taskInTime");
    	List<PrpLWfTaskVo> prpLWfTaskInVos =new ArrayList<PrpLWfTaskVo>();
    	if("1".equals(flag)){
    		List<PrpLWfTaskIn> prpLWfTaskIns = databaseDao.findAll(PrpLWfTaskIn.class,rule);
    		prpLWfTaskInVos = Beans.copyDepth().from(prpLWfTaskIns).toList(PrpLWfTaskVo.class);
    	}else if("2".equals(flag)){
    		List<PrpLWfTaskOut> prpLWfTaskOuts = databaseDao.findAll(PrpLWfTaskOut.class,rule);
    		prpLWfTaskInVos = Beans.copyDepth().from(prpLWfTaskOuts).toList(PrpLWfTaskVo.class);
    	}
    	return prpLWfTaskInVos;
    }
    /**
     * 通过registNo和节点查询out表或in表（flag=1表示查in表，flag=2表示查out表）
     * @param flowId
     * @param nodeCode
     * @param flag
     * @return
     */
    public List<PrpLWfTaskVo> findPrpLWfTaskByregistNoAndnodeCode(String registNo, String flag,List<String> nodeCodes){
    	QueryRule rule=QueryRule.getInstance();
    	rule.addEqual("registNo",registNo);
    	rule.addIn("nodeCode",nodeCodes);
    	rule.addAscOrder("taskInTime");
    	List<PrpLWfTaskVo> prpLWfTaskInVos =new ArrayList<PrpLWfTaskVo>();
    	if("1".equals(flag)){
    		List<PrpLWfTaskIn> prpLWfTaskIns = databaseDao.findAll(PrpLWfTaskIn.class,rule);
    		prpLWfTaskInVos = Beans.copyDepth().from(prpLWfTaskIns).toList(PrpLWfTaskVo.class);
    	}else if("2".equals(flag)){
    		List<PrpLWfTaskOut> prpLWfTaskOuts = databaseDao.findAll(PrpLWfTaskOut.class,rule);
    		prpLWfTaskInVos = Beans.copyDepth().from(prpLWfTaskOuts).toList(PrpLWfTaskVo.class);
    	}
    	return prpLWfTaskInVos;
    }
    /**
     * 
     * <pre></pre>
     * @param handleIdKey
     * @param backNode
     * @return
     * @modified:
     * ☆zhujunde(2018年1月23日 下午5:58:38): <br>
     */
    public PrpLWfTaskVo findBackOutTaskByIlog(String handleIdKey,FlowNode backNode) {
        String hql;
        hql = " FROM PrpLWfTaskOut p Where compensateNo=? and subNodeCode=? Order By taskOutTime DESC ";
        List<PrpLWfTaskOut> taskPoList = databaseDao.findTopByHql(PrpLWfTaskOut.class,hql,1,handleIdKey,backNode.name());
        PrpLWfTaskVo taskVo = null;
        if(!taskPoList.isEmpty()){
            taskVo = Beans.copyDepth().from(taskPoList.get(0)).to(PrpLWfTaskVo.class);
        }else{
            String sql = " FROM PrpLWfTaskOut p Where handlerIdKey=? and subNodeCode=? Order By taskOutTime DESC ";
            taskPoList = databaseDao.findTopByHql(PrpLWfTaskOut.class,sql,1,handleIdKey,backNode.name());
            if(taskPoList.isEmpty()){
                throw new IllegalStateException("没有找到对应的回退节点。业务为号为: "+handleIdKey+" ,退回节点: "+backNode.name());
            }else{
                taskVo = Beans.copyDepth().from(taskPoList.get(0)).to(PrpLWfTaskVo.class);
            }
        }
		return taskVo;
    }

    
    public PrpLWfTaskVo findOutWfTaskVo(String nodeCode,String registNo){
        QueryRule queryRule = QueryRule.getInstance();
        queryRule.addEqual("registNo",registNo);
        queryRule.addEqual("nodeCode",nodeCode);
        queryRule.addAscOrder("taskOutTime");
        List<PrpLWfTaskOut> prpLWfTaskOuts =databaseDao.findAll(PrpLWfTaskOut.class,queryRule);
        PrpLWfTaskVo taskOutVo=null;
        if(prpLWfTaskOuts!=null && prpLWfTaskOuts.size()>0){
            taskOutVo=new PrpLWfTaskVo();
            PrpLWfTaskOut taskOut=prpLWfTaskOuts.get(0);
            Beans.copy().from(taskOut).to(taskOutVo);
        }
        
       return taskOutVo;
       
   }
    
    public List<PrpLWfTaskVo> findTaskInVo(String registNo,String compensateNo,String nodeCode) {
        QueryRule queryRule = QueryRule.getInstance();
        queryRule.addEqual("registNo",registNo);
        queryRule.addEqual("compensateNo",compensateNo);
        queryRule.addEqual("nodeCode",nodeCode);
       
        queryRule.addDescOrder("taskInTime");
        
        List<PrpLWfTaskIn> prpLWfTaskIns = databaseDao.findAll(PrpLWfTaskIn.class,queryRule);
        List<PrpLWfTaskVo> prpLWfTaskInVos = Beans.copyDepth().from(prpLWfTaskIns).toList(PrpLWfTaskVo.class);
        return prpLWfTaskInVos;
    }
    
    public List<PrpLWfTaskVo> findTaskOutVo(String registNo,String nodeCode) {
        QueryRule queryRule = QueryRule.getInstance();
        queryRule.addEqual("registNo",registNo);
        
        queryRule.addEqual("nodeCode",nodeCode);
        
        queryRule.addDescOrder("taskOutTime");
        
        List<PrpLWfTaskOut> prpLWfTaskOuts = databaseDao.findAll(PrpLWfTaskOut.class,queryRule);
        List<PrpLWfTaskVo> prpLWfTaskOutVos = Beans.copyDepth().from(prpLWfTaskOuts).toList(PrpLWfTaskVo.class);
        return prpLWfTaskOutVos;
    }
    
    public List<PrpLWfTaskVo> findInTaskVo(String registNo,String compensateNo,String nodeCode) {
        QueryRule queryRule = QueryRule.getInstance();
        queryRule.addEqual("registNo",registNo);
        if(StringUtils.isNotBlank(compensateNo)){
            queryRule.addEqual("compensateNo",compensateNo);
        }
        
        if(StringUtils.isNotBlank(nodeCode)){
            queryRule.addEqual("nodeCode",nodeCode);
        }
        queryRule.addDescOrder("taskInTime");
        
        List<PrpLWfTaskIn> prpLWfTaskIns = databaseDao.findAll(PrpLWfTaskIn.class,queryRule);
        List<PrpLWfTaskVo> prpLWfTaskInVos = Beans.copyDepth().from(prpLWfTaskIns).toList(PrpLWfTaskVo.class);
        return prpLWfTaskInVos;
    }
    
    public List<PrpLWfTaskVo> findOutTaskVo(String registNo,String compensateNo,String nodeCode) {
        QueryRule queryRule = QueryRule.getInstance();
        queryRule.addEqual("registNo",registNo);
        if(StringUtils.isNotBlank(compensateNo)){
            queryRule.addEqual("compensateNo",compensateNo);
        }
        
        if(StringUtils.isNotBlank(nodeCode)){
            queryRule.addEqual("nodeCode",nodeCode);
        }
        queryRule.addDescOrder("taskOutTime");
        
        List<PrpLWfTaskOut> prpLWfTaskOuts = databaseDao.findAll(PrpLWfTaskOut.class,queryRule);
        List<PrpLWfTaskVo> prpLWfTaskOutVos = Beans.copyDepth().from(prpLWfTaskOuts).toList(PrpLWfTaskVo.class);
        return prpLWfTaskOutVos;
    }

    public PrpLWfTaskVo findLastVlossTask(String registNo){
    	PrpLWfTaskVo taskVo=null;
    	 QueryRule queryRule = QueryRule.getInstance();
    	 queryRule.addEqual("registNo",registNo);
    	 queryRule.addEqual("nodeCode","VLoss");
    	 queryRule.addDescOrder("taskOutTime");
    	 List<PrpLWfTaskOut> prpLWfTaskOuts = databaseDao.findAll(PrpLWfTaskOut.class,queryRule);
    	 if(prpLWfTaskOuts!=null && prpLWfTaskOuts.size()>0){
    		 List<PrpLWfTaskVo> prpLWfTaskOutVos = Beans.copyDepth().from(prpLWfTaskOuts).toList(PrpLWfTaskVo.class);
    		 taskVo=prpLWfTaskOutVos.get(0);
    	 }
    	return taskVo;
    }
    
    public List<PrpLWfTaskVo> queryTaskByAnyOrderOutTime(String registNo,String claimNo,String subNodeCode,String handlerStatus) {
        QueryRule queryRule = QueryRule.getInstance();
        queryRule.addEqual("claimNo",claimNo);
        queryRule.addEqual("subNodeCode",subNodeCode);
        queryRule.addEqual("handlerStatus",handlerStatus);
        queryRule.addDescOrder("taskOutTime");
        List<PrpLWfTaskOut> prpLWfTaskOuts = databaseDao.findAll(PrpLWfTaskOut.class,queryRule);
        List<PrpLWfTaskVo> prpLWfTaskOutVos = Beans.copyDepth().from(prpLWfTaskOuts).toList(PrpLWfTaskVo.class);
        return prpLWfTaskOutVos;
        
    }
    
    /**
     * 旧理赔重开生成工作流
     * @param registNo
     * @return
     */
    public Boolean generateFlow(String registNo){
    	PrpLWfMainVo wfMainVo = wfMainService.findPrpLWfMainVoByRegistNo(registNo);
    	if(wfMainVo==null){
    		// 调用存储过程Flow_Package.SaveForAllFlow
    		Long start = System.currentTimeMillis();
    		Session session = sessionFactory.getCurrentSession();
    		session.doWork(new ProcWork("{call M_MIGRATION_CLAIMFLOW(?)}",registNo){
    			@Override
    			public void execute(Connection conn) throws SQLException {
    				CallableStatement statement = conn.prepareCall(this.getProSql());
    				//为存储过程设置参数
    				statement.setString(1, this.getPro().get("1").toString());
    				statement.execute();
    			}
    		});
    		Long end = System.currentTimeMillis();
    		logger.info("旧理赔重开生成工作流"+(end-start));
    	}
		QueryRule queryRule = QueryRule.getInstance();
        queryRule.addEqual("registNo",registNo);
        List<PrpLWfMain> prpLWfMains = databaseDao.findAll(PrpLWfMain.class,queryRule);
        if(prpLWfMains != null && prpLWfMains.size() >0){
        	return true;
        }else{
        	return false;
        }
    	
    }
    public Boolean generateFlowLevel(String registNo){
    	try {

	        List<PrpLDlossCarMainVo> lossCarMainVos = lossCarService.findLossCarMainByRegistNo(registNo);
	        if(lossCarMainVos != null && lossCarMainVos.size() > 0){
	            for(PrpLDlossCarMainVo mainVo : lossCarMainVos){
	            	LIlogRuleResVo vPriceResVo = deflossHandleIlogService.organizaOldVehicleLoss(mainVo,"2");
	            	if(vPriceResVo != null && StringUtils.isNotBlank(vPriceResVo.getMinUndwrtNode())){
		            	int backLevel = Integer.parseInt(vPriceResVo.getMinUndwrtNode());
		                int level = backLevel;
		                String  subNodeCode = "VLCar_LV"+level;
		                //车核损级别
		                List<PrpLWfTaskVo> taskList = wfTaskHandleService.findEndTask(registNo,mainVo.getId().toString(),FlowNode.VLoss);
		                if(taskList != null && taskList.size() > 0 ){
		                    for(PrpLWfTaskVo wfTaskVo : taskList){
		                        wfTaskVo.setSubNodeCode(subNodeCode);
		                        wfTaskVo.setTaskName(FlowNode.valueOf(subNodeCode).getName());
		                        this.updateTaskOut(wfTaskVo);
		                    }
		                }
		                List<PrpLWfTaskVo> prpLWfTaskVos = wfTaskHandleService.findInTask(registNo,mainVo.getId().toString(),"VLCar_LV1");
		                if(prpLWfTaskVos != null && prpLWfTaskVos.size() > 0 ){
		                    for(PrpLWfTaskVo wfTaskVo : prpLWfTaskVos){
		                        wfTaskVo.setSubNodeCode(subNodeCode);
		                        wfTaskVo.setTaskName(FlowNode.valueOf(subNodeCode).getName());
		                        this.updateTaskIn(wfTaskVo);
		                    }
		                }
	            	}
	                //车核价级别
	                vPriceResVo = deflossHandleIlogService.organizaOldOrganizaVprice(mainVo,"2");
	                if(vPriceResVo != null && StringUtils.isNotBlank(vPriceResVo.getMinUndwrtNode())){
		                int backLevel = Integer.parseInt(vPriceResVo.getMinUndwrtNode());
		                int level = backLevel;
		                String subNodeCode = "VPCar_LV"+level;
	                    
		                List<PrpLWfTaskVo> taskList1 = wfTaskHandleService.findEndTask(registNo,mainVo.getId().toString(),FlowNode.VPrice);
		                if(taskList1 != null && taskList1.size() > 0 ){
		                    for(PrpLWfTaskVo wfTaskVo : taskList1){
		                        wfTaskVo.setSubNodeCode(subNodeCode);
		                        wfTaskVo.setTaskName(FlowNode.valueOf(subNodeCode).getName());
		                        this.updateTaskOut(wfTaskVo);
		                    }
		                }
		                List<PrpLWfTaskVo> prpLWfTaskVos1 = wfTaskHandleService.findInTask(registNo,mainVo.getId().toString(),"VPCar_LV1");
		                if(prpLWfTaskVos1 != null && prpLWfTaskVos1.size() > 0 ){
		                    for(PrpLWfTaskVo wfTaskVo : prpLWfTaskVos1){
		                        wfTaskVo.setSubNodeCode(subNodeCode);
		                        wfTaskVo.setTaskName(FlowNode.valueOf(subNodeCode).getName());
		                        this.updateTaskIn(wfTaskVo);
		                    }
		                }
	                }
	            }
	        }
	        
	        //财核损级别
	        List<PrpLdlossPropMainVo> lossPropMainVos = propLossService.findPropMainByRegistNo(registNo);
	        if(lossPropMainVos != null && lossPropMainVos.size() > 0){
	            for(PrpLdlossPropMainVo mainVo : lossPropMainVos){
	                LIlogRuleResVo vPriceResVo = deflossHandleIlogService.organizaOldVProperty(mainVo,"2");
	                if(vPriceResVo != null && StringUtils.isNotBlank(vPriceResVo.getMinUndwrtNode())){
		                int backLevel = Integer.parseInt(vPriceResVo.getMinUndwrtNode());
		                int level = backLevel;
		                String subNodeCode = "VLProp_LV"+level;
		                List<PrpLWfTaskVo> taskList = wfTaskHandleService.findEndTask(registNo,mainVo.getId().toString(),FlowNode.VLoss);
		                if(taskList != null && taskList.size() > 0 ){
		                    for(PrpLWfTaskVo wfTaskVo : taskList){
		                        wfTaskVo.setSubNodeCode(subNodeCode);
		                        wfTaskVo.setTaskName(FlowNode.valueOf(subNodeCode).getName());
		                        this.updateTaskOut(wfTaskVo);
		                    }
		                }
		                List<PrpLWfTaskVo> prpLWfTaskVos = wfTaskHandleService.findInTask(registNo,mainVo.getId().toString(),"VLProp_LV1");
		                if(prpLWfTaskVos != null && prpLWfTaskVos.size() > 0 ){
		                    for(PrpLWfTaskVo wfTaskVo : prpLWfTaskVos){
		                        wfTaskVo.setSubNodeCode(subNodeCode);
		                        wfTaskVo.setTaskName(FlowNode.valueOf(subNodeCode).getName());
		                        this.updateTaskIn(wfTaskVo);
		                    }
		                }
	                }
	            }
	        }
	
	        //人伤费用审核级别
	        List<PrpLDlossPersTraceMainVo> prpLDlossPersTraceMainVoList = persTraceService.findPersTraceMainVo(registNo);
	        if(prpLDlossPersTraceMainVoList != null && prpLDlossPersTraceMainVoList.size() > 0){
	            for(PrpLDlossPersTraceMainVo prpLDlossPersTraceMainVo : prpLDlossPersTraceMainVoList){
	                Double sumdefLoss = 0d;// 定损总金额
	                Long traceMainId = prpLDlossPersTraceMainVo.getId();
	                List<PrpLDlossPersTraceVo> persTraceVos = persTraceService.findPersTraceVo(registNo,traceMainId);
	                if(persTraceVos!=null&&persTraceVos.size()>0){
	                    for(PrpLDlossPersTraceVo persTraceVo:persTraceVos){
	                        if("1".equals(persTraceVo.getValidFlag())){
	                            BigDecimal sumLoss = persTraceVo.getSumdefLoss();
	                            if(sumLoss==null){
	                                sumLoss = persTraceVo.getSumReportFee();
	                            }
	                            sumdefLoss += DataUtils.NullToZero(sumLoss).doubleValue();// 计算总定损金额
	                        }
	                    }
	                }
	                //审核级别金额加上费用金额
	                sumdefLoss += DataUtils.NullToZero(prpLDlossPersTraceMainVo.getSumChargeFee()).doubleValue();
	                
	                // 获取提交到的审核级别
	                VerifyPersonRuleVo ruleVo = new VerifyPersonRuleVo();
	                ruleVo.setRiskCode(prpLDlossPersTraceMainVo.getRiskCode());
	                String policyComCode = policyViewService.getPolicyComCode(registNo);
	                LIlogRuleResVo vPriceResVo = persReqIlogService.reqIlogByOldPers(prpLDlossPersTraceMainVo,policyComCode,"2");
	                if(vPriceResVo != null && StringUtils.isNotBlank(vPriceResVo.getMinUndwrtNode())){
	               		int backLevel = Integer.parseInt(vPriceResVo.getMinUndwrtNode());
	 	                int level = backLevel;
	 	                String nodeName = "";
	 	                nodeName = FlowNode.PLCharge.name()+"_LV"+level;
		                List<PrpLWfTaskVo> taskList = wfTaskHandleService.findEndTask(registNo,traceMainId.toString(),FlowNode.PLoss);
		                if(taskList != null && taskList.size() > 0 ){
		                    for(PrpLWfTaskVo wfTaskVo : taskList){
		                    	if(!FlowNode.PLNext.name().equals(wfTaskVo.getSubNodeCode()) && !FlowNode.PLFirst.name().equals(wfTaskVo.getSubNodeCode()) ){
		                    		wfTaskVo.setSubNodeCode(nodeName);
			                        wfTaskVo.setTaskName(FlowNode.valueOf(nodeName).getName());
			                        this.updateTaskOut(wfTaskVo);
		                    	}
		                    }
		                }
		                List<PrpLWfTaskVo> prpLWfTaskVos = wfTaskHandleService.findInTask(registNo,traceMainId.toString(),"PLCharge_LV1");
		                if(prpLWfTaskVos != null && prpLWfTaskVos.size() > 0 ){
		                    for(PrpLWfTaskVo wfTaskVo : prpLWfTaskVos){
		                        wfTaskVo.setSubNodeCode(nodeName);
		                        wfTaskVo.setTaskName(FlowNode.valueOf(nodeName).getName());
		                        this.updateTaskIn(wfTaskVo);
		                    }
		                }
	                }
	            }
	        }
	        
	        List<PrpLCompensateVo> compList = new ArrayList<PrpLCompensateVo>();
	        compList = compensateTaskService.queryCompensate(registNo,"N");
	        if(compList !=null && compList.size() > 0){
	            for(PrpLCompensateVo compensateVo : compList){
	                List<PrpLWfTaskVo> taskList = wfTaskHandleService.findEndTask(registNo,compensateVo.getCompensateNo(),FlowNode.VClaim);
	                if(taskList != null && taskList.size() > 0 ){
	                    for(PrpLWfTaskVo wfTaskVo : taskList){
	                    	PrpLWfTaskVo taskVo = wfTaskHandleService.queryTask(wfTaskVo.getUpperTaskId().doubleValue());
	            			String callNode = "00";
	            			if(taskVo.getNodeCode().equals(FlowNode.CompeWf.name())){
	            				callNode = "04";
	            			}
	            			String subNodeCode = "";
	            			try{
	            				LIlogRuleResVo vPriceResVo = compensateHandleServiceIlogService.organizaForOldCompensate(compensateVo.getCompensateNo(),"2",callNode);
	            				if(vPriceResVo != null && StringUtils.isNotBlank(vPriceResVo.getMinUndwrtNode())){
	            					int SubmitLevel = Integer.parseInt(vPriceResVo.getMinUndwrtNode());
		        	                if("1101".equals(compensateVo.getRiskCode())){
		        	                    subNodeCode = "VClaim_CI_LV" + SubmitLevel;
		        	                }else{
		        	                    subNodeCode = "VClaim_BI_LV" + SubmitLevel;
		        	                }
		        	                wfTaskVo.setSubNodeCode(subNodeCode);
			                        wfTaskVo.setTaskName(FlowNode.valueOf(subNodeCode).getName());
			                        this.updateTaskOut(wfTaskVo);
	            				}
	            			}catch(Exception e){
								e.printStackTrace();
							}
	                    }
	                }
	                List<PrpLWfTaskVo> prpLWfTaskVos = wfTaskHandleService.findInTaskByOther(registNo,compensateVo.getCompensateNo(),"VClaim");
	                if(prpLWfTaskVos != null && prpLWfTaskVos.size() > 0 ){
	                    for(PrpLWfTaskVo wfTaskVo : prpLWfTaskVos){
	                    	PrpLWfTaskVo taskVo = wfTaskHandleService.queryTask(wfTaskVo.getUpperTaskId().doubleValue());
	            			String callNode = "00";
	            			if(taskVo.getNodeCode().equals(FlowNode.CompeWf.name())){
	            				callNode = "04";
	            			}
	            			String subNodeCode = "";
	            			try{
	            				LIlogRuleResVo vPriceResVo = compensateHandleServiceIlogService.organizaForOldCompensate(compensateVo.getCompensateNo(),"2",callNode);
	            				if(vPriceResVo != null && StringUtils.isNotBlank(vPriceResVo.getMinUndwrtNode())){
	            					int SubmitLevel = Integer.parseInt(vPriceResVo.getMinUndwrtNode());
		        	                if("1101".equals(compensateVo.getRiskCode())){
		        	                    subNodeCode = "VClaim_CI_LV" + SubmitLevel;
		        	                }else{
		        	                    subNodeCode = "VClaim_BI_LV" + SubmitLevel;
		        	                }
		        	                wfTaskVo.setSubNodeCode(subNodeCode);
			                        wfTaskVo.setTaskName(FlowNode.valueOf(subNodeCode).getName());
			                        this.updateTaskIn(wfTaskVo);
	            				}
	            			}catch(Exception e){
								e.printStackTrace();
							}
	                    }
	                }
	            }
	        }
	        List<PrpLCompensateVo> compYList = new ArrayList<PrpLCompensateVo>();
	        compYList = compensateTaskService.queryCompensate(registNo,"Y");
	        if(compYList !=null && compYList.size() > 0){
	            for(PrpLCompensateVo compensateVo : compYList){
	                //预付
	                
	            	String policyComCode = policyViewService.getPolicyComCode(registNo);
	                String riskCode = compensateVo.getRiskCode();
	               
	                double sumAmt = DataUtils.NullToZero(compensateVo.getSumAmt()).doubleValue();
	                if(sumAmt < 0){
	                    sumAmt = sumAmt*(-1);
	                }
	                
	                PrpLWfTaskVo prpLWfTaskVo = new PrpLWfTaskVo();
	                prpLWfTaskVo.setRegistNo(registNo);
	                prpLWfTaskVo.setComCode(policyComCode);
	                prpLWfTaskVo.setRiskCode(compensateVo.getRiskCode());
	                prpLWfTaskVo.setMoney(new BigDecimal(sumAmt));
	                prpLWfTaskVo.setCompensateNo(compensateVo.getCompensateNo());
	                String subNodeCode = "";
	                try{
	                	LIlogRuleResVo vPriceResVo = compensateHandleServiceIlogService.organizaForOldPrePay(prpLWfTaskVo,"2");
	                	if(vPriceResVo != null && StringUtils.isNotBlank(vPriceResVo.getMinUndwrtNode())){
	                		Integer backLevel = Integer.parseInt(vPriceResVo.getMinUndwrtNode());
				            if(backLevel>=9){
			                    backLevel = Integer.parseInt(vPriceResVo.getMaxUndwrtNode());
			                }
			                if("1101".equals(riskCode)){
			                    subNodeCode = "VClaim_CI_LV"+backLevel;
			                }else{
			                    subNodeCode = "VClaim_BI_LV"+backLevel;
			                }
	                	}
					}catch(Exception e){
						e.printStackTrace();
					}
	                
	                List<PrpLWfTaskVo> taskList = wfTaskHandleService.findEndTask(registNo,compensateVo.getCompensateNo(),FlowNode.VClaim);
	                if(taskList != null && taskList.size() > 0 ){
	                    for(PrpLWfTaskVo wfTaskVo : taskList){
	                    	if(StringUtils.isNotBlank(subNodeCode)){
	                    		wfTaskVo.setSubNodeCode(subNodeCode);
		                        wfTaskVo.setTaskName(FlowNode.valueOf(subNodeCode).getName());
		                        this.updateTaskOut(wfTaskVo);
	                    	}
	                    }
	                }
	                List<PrpLWfTaskVo> prpLWfTaskVos = wfTaskHandleService.findInTaskByOther(registNo,compensateVo.getCompensateNo(),"VClaim");
	                if(prpLWfTaskVos != null && prpLWfTaskVos.size() > 0 ){
	                    for(PrpLWfTaskVo wfTaskVo : prpLWfTaskVos){
	                    	if(StringUtils.isNotBlank(subNodeCode)){
	                    		wfTaskVo.setSubNodeCode(subNodeCode);
		                        wfTaskVo.setTaskName(FlowNode.valueOf(subNodeCode).getName());
		                        this.updateTaskIn(wfTaskVo);
	                    	}
	                    }
	                }
	            }
	        }
		} catch (Exception e) {
			return false;
		}
    	return true;
    }
    
    public List<PrpLWfTaskVo> findAllTaskVo(String registNo,
			BigDecimal upperTaskId, String subNodeCode) {
        QueryRule queryRule = QueryRule.getInstance();
        queryRule.addEqual("registNo",registNo);
        queryRule.addEqual("upperTaskId",upperTaskId);
        queryRule.addEqual("subNodeCode",subNodeCode);
        queryRule.addDescOrder("taskOutTime");
        List<PrpLWfTaskOut> prpLWfTaskOuts = databaseDao.findAll(PrpLWfTaskOut.class,queryRule);
        List<PrpLWfTaskVo> prpLWfTaskOutVos = null;
        if(prpLWfTaskOuts != null && !prpLWfTaskOuts.isEmpty()){
        	prpLWfTaskOutVos = Beans.copyDepth().from(prpLWfTaskOuts).toList(PrpLWfTaskVo.class);
		}else{
			QueryRule queryRule1 = QueryRule.getInstance();
			queryRule1.addEqual("registNo",registNo);
			queryRule1.addEqual("upperTaskId",upperTaskId);
			queryRule1.addEqual("subNodeCode",subNodeCode);
			queryRule1.addDescOrder("taskInTime");
			List<PrpLWfTaskIn> prpLWfTaskIns = databaseDao.findAll(PrpLWfTaskIn.class,queryRule1);
			prpLWfTaskOutVos = Beans.copyDepth().from(prpLWfTaskIns).toList(PrpLWfTaskVo.class);
        }
        return prpLWfTaskOutVos;
    }
    
    /**
     * 在业务冲销前检查时调用，屏蔽移交节点
	 * 是否存在该节点 workStatus 0 表示查询 PrpLWfTaskIn表 ，1 表示查询 已处理的数据 PrpLWfTaskout表 ,"" 都需要查询 ☆yangkun(2016年2月23日 下午4:21:20): <br>
	 */
	public Boolean existTaskByNodeCodeYJ(String registNo,String taskInKey, FlowNode nodeCode,String workStatus) {
		//0表示没有被注销的理算，1表示存在没有被注销的理算
		List<PrpLCompensateVo> compensates=compensateTaskService.queryValidCompensate(registNo,"N");
		if(compensates!=null && compensates.size()>0){
			return true;
		} else{
			List<PrpLWfTaskIn> prpLWfTaskIns = findListByRegistNoAndNode(registNo,"Compe");
			if(prpLWfTaskIns!=null){
				return true;
			}
		}
		return false;
	}
	/**
	 * 是否存在除单证之外的未处理完的节点
	 * <pre></pre>
	 * @param ristCode
	 * @param registNo
	 * @return
	 * @modified:
	 * ☆yzy(2019年3月5日 下午4:07:31): <br>
	 */
	public Boolean existTaskIn(String riskCode,String registNo){
		SqlJoinUtils sqlUtil = new SqlJoinUtils();
		sqlUtil.append(" select * from PrpLWfTaskIn taskIn where 1=1 AND taskIn.registNo = ? ");
		sqlUtil.addParamValue(registNo);
		if("1101".equals(riskCode)){
			 sqlUtil.append(" AND taskIn.subNodeCode not in (?,?) and taskIn.subNodeCode not like ? ");
			 sqlUtil.addParamValue(FlowNode.Certi.name());
			 sqlUtil.addParamValue(FlowNode.CompeBI.name());
			 sqlUtil.addParamValue("VClaim%");
			 String sql=sqlUtil.getSql();
			 Object[] params= sqlUtil.getParamValues();
			 List<PrpLWfTaskIn> taskIns= baseDaoService.getAllBySql(PrpLWfTaskIn.class,sql,params);
			 if(taskIns!=null && taskIns.size()>0){
				 return true;
			 }
		}else{
			 sqlUtil.append(" AND taskIn.subNodeCode not in (?,?) and taskIn.subNodeCode not like ? ");
			 sqlUtil.addParamValue(FlowNode.Certi.name());
			 sqlUtil.addParamValue(FlowNode.CompeCI.name());
			 sqlUtil.addParamValue("VClaim%");
			 String sql=sqlUtil.getSql();
			 Object[] params= sqlUtil.getParamValues();
			 List<PrpLWfTaskIn> taskIns= baseDaoService.getAllBySql(PrpLWfTaskIn.class,sql,params);
			 if(taskIns!=null && taskIns.size()>0){
				 return true;
			 }
		}
		return false;
	}

	/**
	 * 根据taskid查询out表数据
	 * @param taskId
	 * @return
	 * @modified:
	 * ☆XiaoHuYao(2019年8月29日 下午4:37:30): <br>
	 */
	public PrpLWfTaskVo findoutTaskVoByTaskId(Double taskId) {
		 QueryRule queryRule = QueryRule.getInstance();
		 queryRule.addEqual("taskId",new BigDecimal(taskId));
		 PrpLWfTaskOut wftaskout = databaseDao.findUnique(PrpLWfTaskOut.class,queryRule);
		 PrpLWfTaskVo taskVo = null;
		 if(wftaskout != null){
			taskVo = Beans.copyDepth().from(wftaskout).to(PrpLWfTaskVo.class);
		 }
		return taskVo;
	}

	/**
	 * 按条件查詢是否有总红
	 * @param params
	 * @return
	 * @modified:
	 * ☆XiaoHuYao(2019年8月29日 下午5:41:39): <br>
	 */
	public Boolean findOutTaskVos(Map<String,Object> params) {
		SqlJoinUtils sqlUtil = new SqlJoinUtils();

		String registNo = (String)params.get("registNo");

		List<String> subnodecodes = (List<String>)params.get("subnodecodes");
		String handlerStatus = (String)params.get("handlerStatus");
		String[] workStatusList = (String[])params.get("workStatusList");
		String[] handeleComcodes = (String[]) params.get("handeleComcodes");
		
	
		sqlUtil.append(" select * from PrpLWfTaskOut taskout where 1=1 AND taskout.registNo = ? ");
		sqlUtil.addParamValue(registNo);
		
		if(params.get("compensateNo") != null){
			String compensateNo = (String)params.get("compensateNo");
			sqlUtil.append(" and compensateNo = ? ");
			sqlUtil.addParamValue(compensateNo);
		}
		if(params.get("handlerIdKey") != null){
			String  handlerIdKey = (String)params.get("handlerIdKey");
			sqlUtil.append(" and handlerIdKey = ? ");
			sqlUtil.addParamValue(handlerIdKey);
		}
		if(handlerStatus != null){
			sqlUtil.append(" and handlerStatus = ? ");
			sqlUtil.addParamValue(handlerStatus);
		}
		if(subnodecodes != null && !subnodecodes.isEmpty()){
			sqlUtil.append(" and ( ");
			for(int i = 0;i< subnodecodes.size();i++){
				if(i != 0){
					sqlUtil.append(" or ");
					
				}
				sqlUtil.append(" subNodeCode like ? ");
				sqlUtil.addParamValue(subnodecodes.get(i) + "%");
			}
			sqlUtil.append(" ) ");
		}
		
		if(workStatusList != null && workStatusList.length != 0){
			sqlUtil.append(" and workStatus in (?,?) ");
			for(String workStatus : workStatusList){
				sqlUtil.addParamValue(workStatus);	
			}
		}

		if(handeleComcodes != null && handeleComcodes.length != 0){
			sqlUtil.append(" and ( ");
			for(int i=0;i<handeleComcodes.length;i++){
				if(i != 0){
					sqlUtil.append(" or ");
					
				}
				sqlUtil.append(" handlerCom like ? ");
				sqlUtil.addParamValue(handeleComcodes[i] + "%");	
			}
			sqlUtil.append(" ) ");
		}
		logger.info("按条件查询工作流 sql ={},查询参数={}",sqlUtil.getSql(),sqlUtil.getParamValues());
		List<PrpLWfTaskOut> prpLWfTaskOuts = baseDaoService.getAllBySql(PrpLWfTaskOut.class,sqlUtil.getSql(),sqlUtil.getParamValues());
		if(prpLWfTaskOuts!= null && !prpLWfTaskOuts.isEmpty()){
			return true;
		}
	
		return false;
	}

}
