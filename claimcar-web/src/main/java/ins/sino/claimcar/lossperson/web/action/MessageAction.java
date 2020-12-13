/******************************************************************************
 * CREATETIME : 2015年12月21日 上午10:21:07
 ******************************************************************************/
package ins.sino.claimcar.lossperson.web.action;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import freemarker.core.ParseException;

/**
 * 人伤首次跟踪和后续跟踪
 * 
 * @author ★XMSH
 */
@Controller
@RequestMapping("/message")
public class MessageAction {
	@RequestMapping(value = "/initMessage.do")
	public ModelAndView init() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("lossperson/Message/MessageEdit");
		return mav;
	}
	
	
	
	@RequestMapping(value = "/addCItemKind.ajax")
	@ResponseBody
	public ModelAndView addCItemKind() throws ParseException {
		System.out.println("----------------yyeyeyey");
		ModelAndView mv = new ModelAndView();
		mv.setViewName("lossperson/Message/AddMessage");
		return mv;
	}
}
