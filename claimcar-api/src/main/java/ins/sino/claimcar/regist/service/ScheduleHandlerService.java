package ins.sino.claimcar.regist.service;

import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.flow.vo.WfTaskSubmitVo;
import ins.sino.claimcar.schedule.vo.PrpLScheduleDefLossVo;
import ins.sino.claimcar.schedule.vo.PrpLScheduleItemsVo;
import ins.sino.claimcar.schedule.vo.PrpLScheduleTaskVo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ScheduleHandlerService {

	/**
	 * 查勘调度保存
	 * @param prpLScheduleTaskVo
	 * @param prpLScheduleItemses 
	 * @param submitVo 
	 */
	public void saveScheduleItemTask(PrpLScheduleTaskVo prpLScheduleTaskVo,
			List<PrpLScheduleItemsVo> prpLScheduleItemses,
			WfTaskSubmitVo submitVo);

	public void updateScheduleStatus(PrpLScheduleTaskVo prpLScheduleTaskVo);

	/**
	 * 定损调度保存及提交工作流
	 * @param prpLScheduleTaskVo
	 * @param prpLScheduleDefLosses
	 * @param submitVo
	 * @return 
	 */
	public List<PrpLScheduleTaskVo> saveScheduleDefLossTask(PrpLScheduleTaskVo prpLScheduleTaskVo,
			List<PrpLScheduleDefLossVo> prpLScheduleDefLosses,
			WfTaskSubmitVo submitVo);

	/**
	 * 调度注销处理
	 * @param submitVo
	 * @param dLossIdList
	 * @param prpLWfTaskInVos 
	 * @param schType 
	 * @param registNo
	 */
	public String scheduleCancel(PrpLScheduleTaskVo newScheduleTaskVo,
			WfTaskSubmitVo submitVo, Map<Long,BigDecimal> idMap,
			List<PrpLWfTaskVo> prpLWfTaskInVos);

	/**
	 * 调度改派 一次只能改派一次
	 * @param prpLScheduleTaskVo
	 * @param prpLScheduleDefLosses
	 * @param submitVo
	 */
	public List<PrpLScheduleTaskVo> reassignmentDefLossTask(PrpLScheduleTaskVo newScheduleTaskVo,
			PrpLScheduleDefLossVo scheduleDefLossVo, WfTaskSubmitVo submitVo);

	public void reassignmentScheduleItemTask(
			PrpLScheduleTaskVo prpLScheduleTaskVo,
			List<PrpLScheduleItemsVo> prpLScheduleItemses,
			WfTaskSubmitVo submitVo);

	public void sendMsg(PrpLScheduleTaskVo prpLScheduleTaskVo,List<PrpLScheduleItemsVo> prpLScheduleItemses,
			SysUserVo sysUserVo, String scheduleType);

    public void sendMsgByReassignment(PrpLScheduleTaskVo prpLScheduleTaskVo,String schType,SysUserVo sysUserVo,String scheduleType);


}