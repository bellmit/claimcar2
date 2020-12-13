/******************************************************************************
 * CREATETIME : 2016年8月7日 下午12:59:01
 ******************************************************************************/
package ins.framework.web.bind.method;

import ins.platform.vo.SysUserVo;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

 

/**
 * 当前登录用户绑定到SpringMVC的参数中
 * @author ★LiuPing
 * @CreateTime 2016年8月7日
 */
public class SysUserMethodArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		// 如果入参是SaaUserVo对象才出来
		return parameter.getParameterType().isAssignableFrom(SysUserVo.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter,ModelAndViewContainer mavContainer,NativeWebRequest webRequest,WebDataBinderFactory binderFactory) throws Exception {
		Subject subject = SecurityUtils.getSubject();
		SysUserVo loginUser = (SysUserVo)subject.getPrincipal();
		return loginUser;
	}

}
