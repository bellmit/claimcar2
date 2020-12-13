/******************************************************************************
* CREATETIME : 2015年12月14日 下午3:07:31
* FILE       : ins.sino.claimcar.schedule.service.ScheduleService
******************************************************************************/
package ins.sino.claimcar.schedule.service;

import ins.sino.claimcar.schedule.vo.PrpLScheduleDefLossVo;
import ins.sino.claimcar.schedule.vo.PrpLScheduleTaskVo;

import java.util.List;


/**
 * <pre></pre>
 * @author ★yangkun
 */
public interface ScheduleService {
	
	public PrpLScheduleDefLossVo  findScheduleDefLossByPk(Long id);
	
	public Long  findTaskIdByDefLossId(Long id);
	
	public PrpLScheduleTaskVo findScheduleTaskVoByPK(Long id);

	public Long saveScheduleTaskByVo(PrpLScheduleTaskVo personScheduleTaskVo);
	
	//public String findLicenseNoByRegistNoAndSerialNo(String registNo,String serialNo);
	public void updateScheduleDefLoss(PrpLScheduleDefLossVo scheduleDefLossVo);
	
	public Long findCarDefLoss(String registNo,Integer serialNo,String scheduleStatus);
	
	/**
	 * <pre>查勘提交时判断调度是否已生成车辆、财产定损任务</pre>
	 * @param registNo
	 * @param serialNo
	 * @param scheduleStatus
	 * @param deflossType  (1-车辆定损，2-财产定损)
	 * @return
	 * @modified:
	 * ☆Luwei(2017年3月6日 上午11:53:05): <br>
	 */
	public boolean isExistDefLossTask(String registNo,Integer serialNo,String deflossType,String... scheduleStatus);
	
	public PrpLScheduleDefLossVo findCarDefLossBySerialNo(String registNo,Integer serialNo);
	public PrpLScheduleDefLossVo findCarDefLossByLicenseNo(String registNo,String licenseNo);
	
	public List<PrpLScheduleDefLossVo> findPrpLScheduleDefLossList(String registNo);
	
	public List<PrpLScheduleDefLossVo> findScheduleDefLossList(String registNo,String deflossType);
	
	public PrpLScheduleTaskVo getScheduleTask(String registNo,String status);
	
	/**
	 * <pre>根据报案号查询所有查勘生成定损任务</pre>
	 * @param registNo
	 * @return List<PrpLScheduleDefLossVo>
	 * ☆Luwei(2017年3月30日 下午4:39:10): <br>
	 */
	public List<PrpLScheduleDefLossVo> findScheduleDefLossByCheck(String registNo);

	public void rollBackDefLossTask(PrpLScheduleTaskVo vo);
	
	public List<PrpLScheduleTaskVo> getScheduleTaskByregistNo(String registNo);
	/*public void saveScheduleTaskByList(List<PrpLScheduleTaskVo> prpLScheduleDefLossList);*/
    public void reassignmentes(PrpLScheduleTaskVo prpLScheduleTaskVo,String schType,List<PrpLScheduleDefLossVo> prpLScheduleDefLosses,String url) throws Exception;

    List<PrpLScheduleDefLossVo> findPrpLScheduleCarLossList(String registNo);
    
    /**
     * 查询最新查勘任务
     * <pre></pre>
     * @param registNo
     * @param isPersonFlag
     * @param scheduleType
     * @return
     * @modified:
     * ☆zhujunde(2017年11月9日 上午11:16:27): <br>
     */
    public PrpLScheduleTaskVo findScheduleTaskByOther(String registNo,String isPersonFlag,String scheduleType);
    
    public List<PrpLScheduleTaskVo> findScheduleTask(String registNo,String isPersonFlag,String scheduleType);
    
    
  /**
   * 判断当前工号有无自助查勘岗的Id----5195
   * <pre></pre>
   * @param userCode
   * @return
   * @modified:
   * ☆zhujunde(2019年1月29日 上午11:29:37): <br>
   */
    public String findSelfCheckPower(String userCode);

}
