package ins.sino.claimcar.common.web.action;

import ins.platform.common.web.util.WebUserUtils;
import ins.platform.utils.DateUtils;
import ins.sino.claimcar.common.web.util.SSOIntfUtils;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.core.SpringProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexAction {

	@RequestMapping(value = "/login")
	public String fail(
			@RequestParam(value = FormAuthenticationFilter.DEFAULT_USERNAME_PARAM, required = false) String userName,
			Model model) {
		model.addAttribute(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM,userName);
		return "index/login";
	}

	@RequestMapping(value = "/workbench.do")
	public String workbench(String userName,Model model) {
		model.addAttribute(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM,userName);
		return "index/workbench";
	}

	/**
	 * 登录旧单点登录系统
	 * @param userName
	 * @param model
	 * @return
	 * @modified: ☆LiuPing(2016年7月27日 ): <br>
	 */
	@RequestMapping(value = "/loginSSO.do")
	public String loginSSO(Model model) {
		String userCode=WebUserUtils.getUserCode();
		String invalidTime = new Long(DateUtils.addHours(new Date(),2).getTime()).toString();// 两个小时后过期
		String ywUkey = SSOIntfUtils.encode(new String[]{userCode,"claim",invalidTime});
		String ssoUrl = SpringProperties.getProperty("dhic.oldsso.url")+"/logon.do?actionType=loginYwUkey&ywUkey="+ywUkey;
		return "redirect:"+ssoUrl;
	}

	@RequestMapping(value = "/logout.do")
	public String logout(HttpServletRequest request,HttpServletResponse respone) {
		request.getSession().invalidate();
		return "/";
	}

}
