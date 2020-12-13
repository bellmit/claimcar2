package ins.sino.claimcar.flow.service.handle;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.utils.Beans;
import ins.sino.claimcar.CodeConstants.HandlerStatus;
import ins.sino.claimcar.CodeConstants.WorkStatus;
import ins.sino.claimcar.flow.po.PrpLWfTaskIn;
import ins.sino.claimcar.flow.po.PrpLWfTaskOut;
import ins.sino.claimcar.flow.service.WfTaskService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.flow.vo.WfTaskSubmitVo;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 任务移交service
 * @author dengkk
 *
 */
@Service("wfHandoverTaskService")
public class WfHandoverTaskService extends WfBaseHandleService {

	@Autowired
	private DatabaseDao databaseDao;
	
	@Autowired
	private WfTaskService wfTaskService;
	
	/**
	 * 任务移交
	 * 原来任务移到out表中，状态改为被移交。in表中新建一条原来一样数据初始状态任务
	 * @param submitVo
	 * @return
	 */
	public PrpLWfTaskVo handOverTask(WfTaskSubmitVo submitVo,String handoverTaskReason) {
		BigDecimal flowTaskId = submitVo.getFlowTaskId();
		PrpLWfTaskIn oldTaskInPo = databaseDao.findByPK(PrpLWfTaskIn.class,flowTaskId);
		PrpLWfTaskIn taskInPo = null;
		PrpLWfTaskOut taskOutPo = null;
		Date nowDate = new Date();
		//复制原来in数据到新in中
		String comCode = submitVo.getAssignCom();
		if("00000000".equals(comCode)){//总公司特殊处理
			comCode = "00010000";
		}
		taskInPo = Beans.copyDepth().from(oldTaskInPo).to(PrpLWfTaskIn.class);
		taskInPo.setTaskInTime(nowDate);
		taskInPo.setTaskInUser(submitVo.getTaskInUser());
		taskInPo.setAcceptTime(nowDate);
		taskInPo.setAssignCom(comCode);
		taskInPo.setAssignUser(submitVo.getAssignUser());
		taskInPo.setSubCheckFlag(submitVo.getSubCheckFlag());
		if(StringUtils.isNotBlank(oldTaskInPo.getHandlerUser())){
			taskInPo.setHandlerUser(submitVo.getAssignUser());
			taskInPo.setHandlerCom(comCode);
		}
		taskInPo.setUpperTaskId(oldTaskInPo.getTaskId());
		if(StringUtils.isBlank(oldTaskInPo.getHandoverTimes())){
			taskInPo.setHandoverTimes("1");//第一次移交
		}else{
			Integer times = Integer.parseInt(oldTaskInPo.getHandoverTimes());
			times++;
			taskInPo.setHandoverTimes(times.toString());
		}
		databaseDao.save(PrpLWfTaskIn.class,taskInPo);
		
		PrpLWfTaskVo prpLWfTaskVo = Beans.copyDepth().from(taskInPo).to(PrpLWfTaskVo.class);
		//保存工作流操作人员
		if(StringUtils.isNotBlank(taskInPo.getAssignUser())){
			wfTaskService.saveTaskUserInfo(taskInPo.getFlowId(), taskInPo.getAssignUser());
		}
		
		// 复制保存到taskout，并更改任务完成状态
		taskOutPo = Beans.copyDepth().from(oldTaskInPo).to(PrpLWfTaskOut.class);
		taskOutPo.setHandlerStatus(HandlerStatus.END);
		taskOutPo.setWorkStatus(WorkStatus.TURN);
		taskOutPo.setTaskOutTime(taskInPo.getTaskInTime());
		taskOutPo.setTaskOutUser(taskInPo.getTaskInUser());
		taskOutPo.setRemark(handoverTaskReason);
		databaseDao.save(PrpLWfTaskOut.class,taskOutPo);
		// 删除旧的taskin
		databaseDao.deleteByPK(PrpLWfTaskIn.class,flowTaskId);
        return prpLWfTaskVo;
	}
	
	/**
	 * 改派任务
	 * @param submitVo
	 */
	public void reassignmentTask(WfTaskSubmitVo submitVo,String handlerIdKey){
		BigDecimal flowTaskId = submitVo.getFlowTaskId();
		PrpLWfTaskIn oldTaskInPo = databaseDao.findByPK(PrpLWfTaskIn.class,flowTaskId);
		PrpLWfTaskIn taskInPo = null;
		PrpLWfTaskOut taskOutPo = null;
		Date nowDate = new Date();
		//复制原来in数据到新in中
		taskInPo = Beans.copyDepth().from(oldTaskInPo).to(PrpLWfTaskIn.class);
		taskInPo.setTaskInTime(nowDate);
		taskInPo.setTaskInUser(submitVo.getTaskInUser());
		taskInPo.setAcceptTime(nowDate);
		taskInPo.setAssignCom(submitVo.getAssignCom());
		taskInPo.setAssignUser(submitVo.getAssignUser());
		taskInPo.setUpperTaskId(oldTaskInPo.getTaskId());
		taskInPo.setSubCheckFlag(submitVo.getSubCheckFlag());
		if(StringUtils.isNotBlank(handlerIdKey)){
			taskInPo.setHandlerIdKey(handlerIdKey);
		}
		//车童回写标志
		if("0".equals(submitVo.getIsMobileAccept())){
		    taskInPo.setIsMobileAccept(submitVo.getIsMobileAccept());
		}
		databaseDao.save(PrpLWfTaskIn.class,taskInPo);
		//保存工作流操作人员
		if(StringUtils.isNotBlank(taskInPo.getAssignUser())){
			wfTaskService.saveTaskUserInfo(taskInPo.getFlowId(), taskInPo.getAssignUser());
		}
		
		// 复制保存到taskout，并更改任务完成状态
		taskOutPo = Beans.copyDepth().from(oldTaskInPo).to(PrpLWfTaskOut.class);
		taskOutPo.setHandlerStatus(HandlerStatus.END);
		taskOutPo.setWorkStatus(WorkStatus.CHANGE);
		taskOutPo.setTaskOutTime(taskInPo.getTaskInTime());
		taskOutPo.setTaskOutUser(taskInPo.getTaskInUser());
		databaseDao.save(PrpLWfTaskOut.class,taskOutPo);
		// 删除旧的taskin
		databaseDao.deleteByPK(PrpLWfTaskIn.class,flowTaskId);
	}
}
