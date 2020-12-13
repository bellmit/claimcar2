/******************************************************************************
* CREATETIME : 2016年6月29日 下午12:21:14
******************************************************************************/
package ins.sino.claimcar.utils;

import ins.framework.lang.Springs;
import ins.platform.common.service.utils.ServiceUserUtils;
import ins.platform.saa.service.facade.SaaUserPowerService;
import ins.platform.saa.vo.SaaUserPowerVo;
import ins.sino.claimcar.flow.constant.FlowNode;

/**
 * <pre></pre>
 * @author ★LiuPing
 * @CreateTime 2016年6月29日
 */
public class SaaPowerUtils {

	/**
	 * 根据FlowNode判断当前系统登录人员有没有岗位
	 * @param node
	 * @return
	 * @modified: ☆LiuPing(2016年6月29日 ): <br>
	 */
	public static boolean hasRole(FlowNode node) {
		if(node.getRoleCode()==null) return true;// 为空不用判断
		SaaUserPowerService saaUserPowerService = Springs.getBean(SaaUserPowerService.class);
		SaaUserPowerVo userPowerVo = saaUserPowerService.findUserPower(ServiceUserUtils.getUserCode());
		if(userPowerVo==null) return false;
		return userPowerVo.hasRole(node.getRoleCode());
	}
	
	public static boolean hasTask(String userCode,String... taskCodes) {
		if(taskCodes==null) return false;// 为空不用判断
		SaaUserPowerService saaUserPowerService = Springs.getBean(SaaUserPowerService.class);
		SaaUserPowerVo userPowerVo = saaUserPowerService.findUserPower(userCode);
		if(userPowerVo==null) return false;
		return userPowerVo.hasTask(taskCodes);
	}
}
