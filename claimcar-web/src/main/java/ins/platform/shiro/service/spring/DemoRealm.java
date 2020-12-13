package ins.platform.shiro.service.spring;

import ins.platform.shiro.exception.PasswordEmptyException;
import ins.platform.shiro.exception.UsernameEmptyException;
import ins.platform.shiro.source.ShiroUser;
import ins.platform.shiro.web.token.UsernamePasswordCaptchaToken;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

public class DemoRealm extends AuthorizingRealm  {

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		info.addRole("admin");
		return info;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
		UsernamePasswordCaptchaToken token = (UsernamePasswordCaptchaToken) authcToken;
		String username = token.getUsername();
		if (username == null || "".equals(username)) {
			throw new UsernameEmptyException(
					"Username is null.");
		}
		if (token.getPassword() == null || "".equals(token.getPassword())){
			throw new PasswordEmptyException("Password is null.");
		}
		if((!"system".equals(username))){
			throw new IncorrectCredentialsException("Username or Password Error.");
		}
		
		return new SimpleAuthenticationInfo(new ShiroUser("system","系统"),"123456",getName());
		
	}
	
}
