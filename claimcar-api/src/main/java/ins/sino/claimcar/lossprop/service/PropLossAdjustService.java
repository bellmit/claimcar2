package ins.sino.claimcar.lossprop.service;

import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropMainVo;

public interface PropLossAdjustService {

	/**
	 * 财产定损修改任务发起
	 */
	public abstract String propModifyLaunch(Long lossId, SysUserVo sysUserVo);

	/**
	 * 财产追加定损发起 
	 * ☆yangkun(2016年3月3日 下午9:19:32): <br>
	 */
	public abstract String propAdditionLaunch(Long id, SysUserVo userVo,
			String itemContent, String remark);

	/**
	 * 重新生成调度表 定损调度表
	 * ☆yangkun(2016年3月3日 下午9:35:19): <br>
	 */
	public abstract Long saveScheduleDefLossTask(
			PrpLdlossPropMainVo lossPropMainVo, SysUserVo userVo,
			String itemContent, String remark);

}