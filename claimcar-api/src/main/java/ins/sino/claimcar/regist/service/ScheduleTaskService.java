package ins.sino.claimcar.regist.service;

import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.check.vo.PrpLCheckVo;
import ins.sino.claimcar.mobilecheck.vo.HandleScheduleReqScheduleDOrG;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.schedule.vo.PrpDScheduleDOrGMainVo;
import ins.sino.claimcar.schedule.vo.PrpLScheduleDefLossVo;
import ins.sino.claimcar.schedule.vo.PrpLScheduleItemsVo;
import ins.sino.claimcar.schedule.vo.PrpLScheduleTaskVo;
import ins.sino.claimcar.schedule.vo.PrpLScheduleTasklogVo;

import java.util.List;

public interface ScheduleTaskService {

	/**
	 * 根据报案号查询scheduleTask。调度第一次初始化专用方法
	 * @param registNo
	 * @return
	 */
	public PrpLScheduleTaskVo findScheduleTaskByRegistNo(String registNo);

	/**
	 * 保存scheduleTask表信息
	 * @param personScheduleTaskVo
	 */
	public PrpLScheduleTaskVo saveScheduleTaskByVo(
			PrpLScheduleTaskVo scheduleTaskVo);

	/**
	 * 根据报案号查询查勘调度项
	 * @param registNo
	 * @return
	 */
	public List<PrpLScheduleItemsVo> getPrpLScheduleItemsesVoByRegistNo(
			String registNo);

	/**
	 * 根据报案号查询定损调度项
	 * @param registNo
	 * @return
	 */
	public List<PrpLScheduleDefLossVo> getPrpLScheduleDefLossesVoByRegistNo(
			String registNo);

	/**
	 * 根据定损调度id集合，获取定损调度项的集合
	 * @param registNo
	 * @return
	 */
	public List<PrpLScheduleDefLossVo> getPrpLScheduleDefLossesVoByIds(
			List<Long> defLossVoIds);

	public List<PrpLScheduleTaskVo> getPrpLScheduleTaskVoByIds(
			List<Long> taskVoIds);
	
	public PrpLScheduleTaskVo findPrpLScheduleTaskVoById(Long id);

	public List<PrpLScheduleItemsVo> getPrpLScheduleItemsVoByIds(
			List<Long> itemVoIds);

	public PrpLScheduleDefLossVo findPrpLScheduleDefLossVoById(Long id);

	public List<PrpLScheduleTasklogVo> findScheduleTaskLogsByRegistNo(
			String registNo);
	
	public PrpLScheduleDefLossVo findScheduleDefLossById(Long id);

	/**
	 * 定损调度项注销更新
	 * @param toUpdateIdList
	 */
	public void cancelDLossByDLossIds(PrpLScheduleTaskVo newScheduleTaskVo,
			List<Long> toUpdateIdList);

	/**
	 * 查勘调度项注销更新
	 * @param toUpdateIdList
	 */
	public void cancelItemByItemIds(List<Long> toUpdateIdList);
	
	
	public List<PrpLScheduleTaskVo> findScheduleTaskListByRegistNo(String registNo);
	
	public List<PrpLScheduleTasklogVo> getScheduleTaskLogs(List<PrpLScheduleTasklogVo> prpLScheduleTasklogs);
	
//	/**
//	 * 根据调度类型查询PrpLScheduleTaskVo
//	 * @param registNo
//	 * @param scheduleType
//	 * @return
//	 */
//	public PrpLScheduleTaskVo findScheduleTaskListByScheduleType(String registNo,String scheduleType);

	public void writePrplScheduleTasklog(String registNo,SysUserVo userVo,PrpLCheckVo checkVo,List<PrpLScheduleDefLossVo> defLossVos);

	//设置prplcheckduty 车辆事故责任比例置为无效
	public void setCheckDutyByRegistNo(String registNo,List<Long> toUpdateIdList);
	/*
	 * zjd
	 * 查询用户信息
	 */
	public SysUserVo findPrpduserByUserCode(String userCode,String validStatus);
	
	/**
	 * 
	 * <pre>根据报案号查询调度提交查勘的PrpLScheduleTask</pre>
	 * @param registNo
	 * @param isPersonFlag
	 * @return
	 * @modified:
	 * ☆zhujunde(2017年7月3日 上午10:53:07): <br>
	 */
    public PrpLScheduleTaskVo findCheckScheduleTaskByRegistNo(String registNo,String isPersonFlag);

    
    /**
     * 根据报案号,定损类别查询
     * @param registNo
     * @param deflossType
     * @return
     */
    public List<PrpLScheduleDefLossVo> getScheduleDefLossByLossType(String registNo,String deflossType);

    /**
     * 根据报案号，序号，定损类别查询
     * <pre></pre>
     * @param registNo
     * @param serialNo
     * @param deflossType
     * @return
     * @modified:
     * ☆zhujunde(2017年11月2日 上午10:36:45): <br>
     */
    public PrpLScheduleDefLossVo findPrpLScheduleDefLossVoByOther(String registNo,Integer serialNo,String deflossType);

    /**
     * 更新PrpLScheduleItems
     * @param prpLScheduleItemsVo
     */
    public void updatePrpLScheduleItems(PrpLScheduleItemsVo prpLScheduleItemsVo);
    
    /**
     * 更新主表PrpLScheduleTaskVo
     */
    public void updatePrpLScheduleTaskVo(PrpLScheduleTaskVo vo);
    
   /**
    * 设置业务类型，客户分类，代理人编码，保单归属地
    * @param prpDScheduleDOrGMainVo
    * @return
    */
	public HandleScheduleReqScheduleDOrG setScheduleDOrG(PrpDScheduleDOrGMainVo prpDScheduleDOrGMainVo);
}