package ins.platform.shiro.service.spring;

import ins.platform.saa.service.facade.SaaUserPowerService;
import ins.platform.saa.vo.SaaUserPowerVo;
import ins.platform.sysuser.service.facade.SysUserService;
import ins.platform.vo.SysUserVo;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.cas.CasAuthenticationException;
import org.apache.shiro.cas.CasRealm;
import org.apache.shiro.cas.CasToken;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.StringUtils;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.validation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;

public class MyCasRealm extends CasRealm {

	private static Logger log = LoggerFactory.getLogger(MyCasRealm.class);

	@Autowired
	private SaaUserPowerService saaUserPowerService;
	@Autowired
	private SysUserService sysUserService;

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		CasToken casToken = (CasToken)token;
		if(token==null){
			return null;
		}

		String ticket = (String)casToken.getCredentials();
		if( !StringUtils.hasText(ticket)){
			return null;
		}

		TicketValidator ticketValidator = createTicketValidator();

		try{
			// contact CAS server to validate service ticket
			String casService = SpringProperties.getProperty("cas.webapp.local")+"/cas";
			Assertion casAssertion = ticketValidator.validate(ticket, casService);
			// get principal, user id and attributes
			AttributePrincipal casPrincipal = casAssertion.getPrincipal();
			String userId = casPrincipal.getName();
			log.debug("Validate ticket : {} in CAS server : {} to retrieve user : {}",new Object[]{ticket,getCasServerUrlPrefix(),userId});

			Map<String,Object> attributes = casPrincipal.getAttributes();
			// refresh authentication token (user id + remember me)
			casToken.setUserId(userId);
			String rememberMeAttributeName = getRememberMeAttributeName();
			String rememberMeStringValue = (String)attributes.get(rememberMeAttributeName);
			boolean isRemembered = rememberMeStringValue!=null&&Boolean.parseBoolean(rememberMeStringValue);
			if(isRemembered){
				casToken.setRememberMe(true);
			}
			// create simple authentication info
			 
			SysUserVo userVo = sysUserService.findByUserCode(userId);
			if(null==userVo){
				throw new UnknownAccountException("No account found for user ["+userId+"]");
			}

			return new SimpleAuthenticationInfo(userVo,ticket,getName());
		}catch(TicketValidationException e){
			throw new CasAuthenticationException("Unable to validate ticket ["+ticket+"]",e);
		}
	}

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		SysUserVo userVo = (SysUserVo)principals.getPrimaryPrincipal();
		log.info("\n==user==MyCasRealm doGetAuthorizationInfo==="+userVo.getUserCode());
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

		SaaUserPowerVo userPowerVo = saaUserPowerService.findUserPower(userVo.getUserCode());

		List<String> taskList = userPowerVo.getTaskList();
		info.addStringPermissions(userPowerVo.getTaskList());
		List<String> roleList = userPowerVo.getRoleList();
		//bug 29962 理赔默认菜单问题 1、没有开通理赔权限的不让登录理赔系统   saa_usergrade中不存在工号的任何记录，则视为没有理赔权限
		if (null == roleList || roleList.isEmpty()) {
			throw new UnauthorizedException();
		}
		info.addRoles(roleList);
		info.addRoles(taskList);// 将taskCode 也放到role里面，还在界面使用 shiro:hasAnyRoles 标签
		log.info("\n user tasklist:"+Arrays.toString(taskList.toArray()));

		return info;
	
    }

	@Override
	protected TicketValidator createTicketValidator() {
		String urlPrefix = SpringProperties.getProperty("cas.ticket");
		if ("saml".equalsIgnoreCase(getValidationProtocol())) {
			return new Saml11TicketValidator(urlPrefix);
		}
		return new Cas20ServiceTicketValidator(urlPrefix);
	}
}
