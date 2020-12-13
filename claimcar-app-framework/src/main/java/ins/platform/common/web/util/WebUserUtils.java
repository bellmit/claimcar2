/******************************************************************************
* CREATETIME : 2016年1月8日 下午2:24:32
******************************************************************************/
package ins.platform.common.web.util;

import ins.platform.vo.SysUserVo;

import org.apache.shiro.subject.Subject;

/**
 * 系统登录信息，权限控制工具
 * @author ★LiuPing
 * @CreateTime 2016年1月8日
 */
public class WebUserUtils {

	public static SysUserVo getUser() {
		Subject subject = org.apache.shiro.SecurityUtils.getSubject();
		SysUserVo loginUser = (SysUserVo)subject.getPrincipal();
		return loginUser;
	}


	public static String getUserCode() {
		return getUser().getUserCode();
	}

	public static String getComCode() {
		return getUser().getComCode();
	}
	
	public static String getUserName() {
		return getUser().getUserName();
	}


}
