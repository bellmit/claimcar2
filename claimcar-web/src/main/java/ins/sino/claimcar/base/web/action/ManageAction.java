package ins.sino.claimcar.base.web.action;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/manage")
public class ManageAction {

	@RequestMapping("/taskTransferEdit.do")
	@ResponseBody
	public ModelAndView taskTransferEdit() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("base/manage/TaskTransferEdit");
		return modelAndView;
	}

	@RequestMapping("/holidayApplyEdit.do")
	@ResponseBody
	public ModelAndView holidayApplyEdit() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("base/manage/HolidayApplyEdit");
		return modelAndView;
	}
}
