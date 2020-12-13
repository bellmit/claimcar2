package ins.sino.claimcar.other.service;

import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.manager.vo.PrpLUserHolidayVo;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface HolidayManageService {

	/**
	 * 休假申请查询
	 */
	public abstract List<PrpLUserHolidayVo> findAllHolidayManage(
			PrpLUserHolidayVo prpLUserHolidayVo, int start, int length);

	/**
	 * 休假审核查询
	 */
	public abstract List<PrpLUserHolidayVo> findAllHolidayManageByHql(
			PrpLUserHolidayVo prpLUserHolidayVo, String handleStatus,
			Date timeEnd, Date timeStart, int start, int length,String userCode);

	/**
	 * 根据主键查询PrpLUserHolidayVo
	 * @param id
	 * @return PrpLUserHolidayVo
	 */
	public abstract PrpLUserHolidayVo findPrpLUserHolidayVoByPK(Long id);

	/**
	 * 保存或更新休假处理
	 * @param PrpLUserHolidayVo
	 * @return PrpLUserHoliday
	 */
	public abstract void saveOrUpdateHolidayManage(
			PrpLUserHolidayVo prpLUserHolidayVo,SysUserVo userVo);

	/**
	 * 查询正在休假期间的员工
	 * @return
	 */
	public abstract List<PrpLUserHolidayVo> findAssignUserHolidayListByTime();

	public abstract List<PrpLUserHolidayVo> findAllHolidayManageByUserCode(
			String userCode);

	public abstract Boolean existHoliday(String userCode);

	/**
	 * 根据userCode查询审核通过的申请
	 * @param userCode
	 * @return
	 */
	public abstract List<PrpLUserHolidayVo> findHolidayByUserCode(
			String userCode);

	/**
	 * 根据工号查询可移交的人员
	 * @param userCode
	 * @return
	 */
	public Map<String,Map> organizeMap(SysUserVo userVo);
	
	/**
	 * 撤销休假
	 * @param id
	 */
	public void cancelHoliday(String id,SysUserVo userVo);
}