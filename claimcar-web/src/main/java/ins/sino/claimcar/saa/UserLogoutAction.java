/******************************************************************************
* CREATETIME : 2016年8月8日 下午7:17:40
******************************************************************************/
package ins.sino.claimcar.saa;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <pre></pre>
 * @author ★LiuPing
 * @CreateTime 2016年8月8日
 */
@Controller
public class UserLogoutAction {

	private static Logger logger = LoggerFactory.getLogger(UserLogoutAction.class);
	/**
	 * 退出shiro
	 * @param userName
	 * @param model
	 * @modified: ☆LiuPing(2016年8月8日 ): <br>
	 */
	@RequestMapping(value = "/logoutShiro.do")
	@ResponseBody
	public String logoutShiro(HttpServletRequest request,HttpServletResponse respone) {
		Subject subject = org.apache.shiro.SecurityUtils.getSubject();
		String username = (String)subject.getPrincipal();
		subject.logout();
		request.getSession().invalidate();
		String path = request.getContextPath();
		respone.setCharacterEncoding("UTF-8");
		logger.info("***Shiro User ["+username+"] logout "+path);
		String msg = "用户["+username+"]退出系统 "+path;
		return msg;
	}

}
