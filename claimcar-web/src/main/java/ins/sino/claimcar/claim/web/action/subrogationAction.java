package ins.sino.claimcar.claim.web.action;



import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("/subrogation")
public class subrogationAction {

	@RequestMapping("/subrogationB.do")
	/*public ModelAndView ClaimEdit(String registNo,String nodeCode,String caseProcessType) {*/
	public ModelAndView subrogationB(String registNo,String nodeCode) {
	
		ModelAndView modelAndView = new ModelAndView();
			modelAndView.setViewName("subRogation/subrogationBQueryList");
		return modelAndView;
	}
	@RequestMapping("/subrogationL.do")
	public ModelAndView subrogationL(String registNo,String nodeCode) {
		ModelAndView modelAndView = new ModelAndView();
			modelAndView.setViewName("subRogation/subrogationLQueryList");
		return modelAndView;
	}
	@RequestMapping("/subrogationD.do")
	public ModelAndView subrogationD(String registNo,String nodeCode) {
	
		ModelAndView modelAndView = new ModelAndView();
			modelAndView.setViewName("subRogation/subrogationDQueryList");
		return modelAndView;
	}
	@RequestMapping("/subrogationJ.do")
	public ModelAndView subrogationJ(String registNo,String nodeCode) {
	
		ModelAndView modelAndView = new ModelAndView();
			modelAndView.setViewName("subRogation/subrogationJQueryList");
		return modelAndView;
	}
	@RequestMapping("/subrogationP.do")
	public ModelAndView subrogationP(String registNo,String nodeCode) {
	
		ModelAndView modelAndView = new ModelAndView();
			modelAndView.setViewName("subRogation/subrogationPQueryList");
		return modelAndView;
	}
	
	@RequestMapping("/show.do")
	public ModelAndView show() {
		ModelAndView modelAndView = new ModelAndView();
			modelAndView.setViewName("subRogation/ShowList");
		return modelAndView;
	}
	
	@RequestMapping("/baodan.do")
	public ModelAndView baodan() {
		ModelAndView modelAndView = new ModelAndView();
			modelAndView.setViewName("subRogation/baodanList");
		return modelAndView;
	}
}


