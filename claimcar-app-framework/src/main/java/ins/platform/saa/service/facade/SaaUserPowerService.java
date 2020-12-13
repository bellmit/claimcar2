/******************************************************************************
* CREATETIME : 2016年6月27日 下午5:14:46
******************************************************************************/
package ins.platform.saa.service.facade;

import ins.platform.saa.schema.ResourceTask;
import ins.platform.saa.schema.SaaTask;
import ins.platform.saa.vo.SaaRoleTaskVo;
import ins.platform.saa.vo.SaaUserPowerVo;

import java.util.List;

/**
 * 用户权限控制
 * @author ★LiuPing
 * @CreateTime 2016年6月27日
 */
public interface SaaUserPowerService {

	/**
	 * 获取一个用户的所有权限
	 */
	public SaaUserPowerVo findUserPower(String userCode);

	/**
	 * 查找所有角色任务
	 * @return 所有角色任务
	 */
	public List<SaaRoleTaskVo> findAllSaaRoleTask();

	/**
	 * 获取旧系统的登录用信息
	 */
	public List<String> findUserLoginCom(String userCode);
	/**
	 * 清除缓存
	 */
	public void clearCache();
	
	/**
	 * 查找所有的任务
	 * @return
	 */
	public List<SaaTask> findAllSaaTask();
	
	/**
	 * 查找所有资源型URL
	 * @return
	 */
	public List<ResourceTask> findAllResUrl();
}
