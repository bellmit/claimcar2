package ins.sino.claimcar.claim.web.action;

import java.util.List;

import ins.sino.claimcar.claim.service.ClaimDLInfoService;
import ins.sino.claimcar.claim.vo.PrplcecheckResultVo;
import ins.sino.claimcar.claim.vo.PrplpriceSummaryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;



@Controller
@RequestMapping("/dlResultInfo")
public class DlResultInfoAction {
	@Autowired
	private ClaimDLInfoService claimDLInfoService;
	
	@RequestMapping("/infoShow.do")
	public ModelAndView dlinfoshow(String registNo){
		ModelAndView mv=new ModelAndView();
		List<PrplcecheckResultVo> prplcecheckResultVos=claimDLInfoService.findResultVoByRegistNo(registNo);
		mv.addObject("prplcecheckResultVos",prplcecheckResultVos);
		mv.setViewName("/dlclaim/dlinfoshow");
		return mv;
	}
	
	@RequestMapping("/ceResultInfo.do")
	public ModelAndView ceResultInfo(Long id){
		ModelAndView mv=new ModelAndView();
		PrplpriceSummaryVo prplpriceSummaryVo=new PrplpriceSummaryVo();
		PrplcecheckResultVo prplcecheckResultVo=claimDLInfoService.findResultVoById(id);
		if(prplcecheckResultVo!=null){
			prplpriceSummaryVo=prplcecheckResultVo.getPrplpriceSummary();
		}
		mv.addObject("prplpriceSummaryVo", prplpriceSummaryVo);
		mv.addObject("prplcecheckResultVo", prplcecheckResultVo);
		mv.setViewName("/dlclaim/ceResultInfo");
		return mv;
	}
	
}
