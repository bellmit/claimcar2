/******************************************************************************
* CREATETIME : 2016年2月1日 下午3:32:49
******************************************************************************/
package ins.platform.shiro.web.filter;

import ins.platform.shiro.web.token.UsernamePasswordCaptchaToken;
import ins.platform.sysuser.service.facade.SysUserService;
import ins.platform.vo.SysUserVo;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;

/**
 * <pre></pre>
 * @author ★LiuPing
 * @CreateTime 2016年2月1日
 */
public class MyAccessControlFilter extends FormAuthenticationFilter {

	private static final Logger log = LoggerFactory.getLogger(MyAccessControlFilter.class);
	private final static String AJAX_HEADER = "x-requested-with";
	private final static String XMLHTTPREQUEST = "XMLHttpRequest";

	@Autowired
	SysUserService sysUserService;

	public static final String DEFAULT_CAPTCHA_PARAM = "captcha";

	private String captchaParam = DEFAULT_CAPTCHA_PARAM;

	public String getCaptchaParam() {
		return captchaParam;
	}

	protected String getCaptcha(ServletRequest request) {
		return WebUtils.getCleanParam(request,getCaptchaParam());
	}

	@Override
	protected boolean onLoginSuccess(AuthenticationToken token,Subject subject,ServletRequest request,ServletResponse response) throws Exception {
		issueSuccessRedirect(request,response);

		// we handled the success redirect directly, prevent the chain from continuing:
		// UsernamePasswordToken authcToken = (UsernamePasswordToken) token;
		UsernamePasswordCaptchaToken authcToken = (UsernamePasswordCaptchaToken)token;
		String username = authcToken.getUsername();
		SysUserVo user = sysUserService.findByUserCode(username);
		if(user!=null){

		}
		System.out.println("==FormAuthenticationCaptchaFilter==onLoginSuccess===");
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		session.setAttribute("userCode",authcToken.getUsername());
		return false;
	}
	/* 
	 * @see org.apache.shiro.web.filter.AccessControlFilter#onAccessDenied(javax.servlet.ServletRequest, javax.servlet.ServletResponse)
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@Override
	protected boolean onAccessDenied(ServletRequest request,ServletResponse response) throws Exception {
		System.out.println("11111MyAccessControlFilter==onAccessDenied");

		if(isLoginRequest(request,response)){
			if(isLoginSubmission(request,response)){
				if(log.isTraceEnabled()){
					log.trace("Login submission detected.  Attempting to execute login.");
				}
				return executeLogin(request,response);
			}else{
				if(log.isTraceEnabled()){
					log.trace("Login page view.");
				}
				// allow them to see the login page ;)
				return true;
			}
		}else{
			if(log.isTraceEnabled()){
				log.trace("Attempting to access a path which requires authentication.  Forwarding to the "+"Authentication url ["+getLoginUrl()+"]");
			}
			HttpServletRequest httpReq = (HttpServletRequest)request;
			HttpServletResponse httpRes = (HttpServletResponse)response;

			if(isAjaxRequest(httpReq)){
				// ajax请求弹出提示
				httpRes.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				responseOutJson(response,"您尚未登录或登录时间过长,请重新登录!");
			}else{
				// 根据访问地址设置url
				String loginUrl = getLoginUrl();
				String thisReqUrl = "?service=http://"+request.getServerName()+":"+request.getServerPort()+httpReq.getContextPath()+"/cas";
				setLoginUrl(loginUrl+thisReqUrl);
				saveRequestAndRedirectToLogin(request,response);
			}
		}

		return false;
	}

	private static boolean isAjaxRequest(HttpServletRequest request) {
		String requestType = request.getHeader(AJAX_HEADER);
		return XMLHTTPREQUEST.equalsIgnoreCase(requestType);
	}

	private void responseOutJson(ServletResponse response,Object responseObj) {
		// 将实体对象转换为JSON Object转换
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		PrintWriter out = null;
		try{
			String jsonStr = JSON.toJSONString(responseObj);
			out = response.getWriter();
			out.append(jsonStr);
		}catch(IOException e){
			log.error("responseOutJson Error",e);
		}
		finally{
			if(out!=null){
				out.close();
			}
		}
	}

}
