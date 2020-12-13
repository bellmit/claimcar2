package ins.platform.shiro.web.filter;

import ins.platform.shiro.web.token.UsernamePasswordCaptchaToken;
import ins.platform.sysuser.service.facade.SysUserService;
import ins.platform.vo.SysUserVo;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class FormAuthenticationCaptchaFilter extends FormAuthenticationFilter {
	
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
 
//	protected AuthenticationToken createToken(
//				ServletRequest request, ServletResponse response) {
// 
//		String username = getUsername(request);
//		String password = getPassword(request);
//		String captcha = getCaptcha(request);
//		boolean rememberMe = isRememberMe(request);
//		String host = getHost(request);
//		return new UsernamePasswordCaptchaToken(username,
//				password != null?(password.toCharArray()):null, rememberMe, host, captcha);
//	}
	
//	@Override
//	protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
//		UsernamePasswordCaptchaToken token = (UsernamePasswordCaptchaToken)createToken(request, response);
//        if (token == null) {
//            String msg = "createToken method implementation returned null. A valid non-null AuthenticationToken " +
//                    "must be created in order to execute a login attempt.";
//            throw new IllegalStateException(msg);
//        }else{
//        	SysUserVo user = sysUserService.findByUserCode(token.getUsername());
//        	if(user != null && user.getLastLoginFailed() != null && user.getLastLoginFailed() >= 1){
//	   			 String captcha = token.getCaptcha();
//	   			 String exitCode = (String) SecurityUtils.getSubject().getSession()
//	   					 .getAttribute(CaptchaServlet.KEY_CAPTCHA);
//	   			 if (null == captcha || !captcha.equalsIgnoreCase(exitCode)) {
//	   				 return onLoginFailure(token,new CaptchaException("Error captcha"),request,response);
//	   			 }
//        	}
//        }
//        try {
//            Subject subject = getSubject(request, response);
//            subject.login(token);
//            return onLoginSuccess(token, subject, request, response);
//        } catch (AuthenticationException e) {
//            return onLoginFailure(token, e, request, response);
//        }
//    }
	
	@Override
	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject,
            ServletRequest request, ServletResponse response) throws Exception {
		issueSuccessRedirect(request, response);
		
		//we handled the success redirect directly, prevent the chain from continuing:
		// UsernamePasswordToken authcToken = (UsernamePasswordToken) token;
		UsernamePasswordCaptchaToken authcToken = (UsernamePasswordCaptchaToken)token;
		String username = authcToken.getUsername();
		SysUserVo user = sysUserService.findByUserCode(username);
		if(user!=null){

		}
		System.out.println("==FormAuthenticationCaptchaFilter==onLoginSuccess===");
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		session.setAttribute("userCode", authcToken.getUsername());
		return false;
	}
	
//	@Override
//    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
//		System.out.println("-----------------------------");
//		System.out.println(super.isAccessAllowed(request, response, mappedValue));
//		System.out.println(!isLoginRequest(request, response));
//		System.out.println(isPermissive(mappedValue));
//		if(mappedValue != null){
//			for(String str : (String[])mappedValue){
//				System.out.print(str + ",");
//			}
//		}else{
//			System.out.println(mappedValue);
//		}
//		System.out.println("-----------------------------");
//        return super.isAccessAllowed(request, response, mappedValue) ||
//                (!isLoginRequest(request, response) && isPermissive(mappedValue));
//    }
	
//	@Override
//	protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e,
//            ServletRequest request, ServletResponse response) {
//		setFailureAttribute(request, e);
//		//login failed, let request continue back to the login page:
//		String exception = e.getClass().getName();
//		UsernamePasswordCaptchaToken authcToken = (UsernamePasswordCaptchaToken) token;
//		String username = authcToken.getUsername();
//		SysUserVo user = null; 
//		if(username != null){		
//			user = sysUserService.findByUserCode(username);
//		}
//		if (user != null) {
//			if ("org.apache.shiro.authc.IncorrectCredentialsException".equals(exception)
//					|| "org.apache.shiro.authc.DisabledAccountException".equals(exception)
//					|| "org.apache.shiro.authc.ExpiredCredentialsException".equals(exception)
//					|| "org.apache.shiro.authc.AuthenticationException".equals(exception)
//					|| "ins.platform.common.web.exception.CaptchaException".equals(exception)) {
//
//				user.setLastLoginFailed(user.getLastLoginFailed() == null ? 1 : user.getLastLoginFailed() + 1);
//				user.setLastTime(new Date());
//				sysUserService.updateByUserCode(username, user);
//				request.setAttribute("loginFailedNum", user.getLastLoginFailed());
//			} else {
//				if (user != null) {
//					request.setAttribute("loginFailedNum", user.getLastLoginFailed());
//				} else {
//					request.setAttribute("loginFailedNum", 0);
//				}
//			} 
//		}
//		return true;
//	}
 
}
