/******************************************************************************
* CREATETIME : 2016年1月8日 下午3:19:05
******************************************************************************/
package ins.platform.shiro.web.filter;

import ins.platform.sysuser.service.facade.SysUserService;
import ins.platform.vo.SysUserVo;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.cas.CasFilter;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * <pre></pre>
 * @author ★LiuPing
 * @CreateTime 2016年1月8日
 */
public class MyCasFilter extends CasFilter {

	private static Logger logger = LoggerFactory.getLogger(MyCasFilter.class);
	@Autowired
	private SysUserService sysUserService;

	@Override
	protected boolean onLoginSuccess(AuthenticationToken token,Subject subject,ServletRequest request,ServletResponse response) throws Exception {
		issueSuccessRedirect(request,response);

		//String username = (String)subject.getPrincipal();
		Session session = subject.getSession();
		session.setAttribute("user",subject.getPrincipal());
		return false;
	}

}
