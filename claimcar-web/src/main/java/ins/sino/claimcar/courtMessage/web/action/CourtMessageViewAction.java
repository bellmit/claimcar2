package ins.sino.claimcar.courtMessage.web.action;



import ins.sino.claimcar.court.service.CourtService;
import ins.sino.claimcar.court.vo.PrpLCourtClaimVo;
import ins.sino.claimcar.court.vo.PrpLCourtCompensationVo;
import ins.sino.claimcar.court.vo.PrpLCourtConfirmVo;
import ins.sino.claimcar.court.vo.PrpLCourtIdentifyVo;
import ins.sino.claimcar.court.vo.PrpLCourtLitigationVo;
import ins.sino.claimcar.court.vo.PrpLCourtMediationVo;
import ins.sino.claimcar.court.vo.PrpLCourtMessageVo;
import ins.sino.claimcar.court.vo.PrpLCourtPartyVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;




@Controller
@RequestMapping("/courtMessage")
public class CourtMessageViewAction {
	
	@Autowired
	private CourtService  courtService;
	
	/**
	 * 查询诉讼信息
	 * @return
	 */
	@RequestMapping(value = "/initLitigation.do")
	public ModelAndView initSurvey(String registNo,int status) {
		ModelAndView mav = new ModelAndView();
		PrpLCourtMessageVo courtMessageVo = courtService.findCourtMessage(registNo,status);
		mav.addObject("courtMessageVo",courtMessageVo);
		mav.setViewName("courtMessage/CourtMessageView");
		return mav;
	}
	@RequestMapping(value="/viewCourtIdentify.do/{id}")
	public ModelAndView CourtIdentifyView(@PathVariable("id") Long id) {
		ModelAndView mav = new ModelAndView();
		PrpLCourtIdentifyVo courtIdentifyVo = courtService.findCourtIdentify(id);
		mav.addObject("courtIdentifyVo",courtIdentifyVo);
		mav.setViewName("courtMessage/CourtIdentifyView");
		return mav;
	}
	/**
	 * 查询高院当事人信息
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/viewCourtParty.do/{id}")
	public ModelAndView CourtPartyView(@PathVariable("id") Long id) {
		ModelAndView mav = new ModelAndView();
		PrpLCourtPartyVo courtPartyVo = courtService.findCourtParty(id);
		mav.addObject("courtPartyVo",courtPartyVo);
		mav.setViewName("courtMessage/CourtPartyView");
		return mav;
	}
	@RequestMapping(value = "viewCourtCompensation.do/{id}")
	public ModelAndView CourtCompensationView(@PathVariable("id") Long id) {
		ModelAndView mav = new ModelAndView();
		PrpLCourtCompensationVo courtCompensationVo = courtService.findCourtCompensate(id);
		mav.addObject("courtCompensationVo",courtCompensationVo);
		mav.setViewName("courtMessage/CourtCompensationView");
		return mav;
	}
	@RequestMapping(value = "viewCourtClaim.do/{id}")
	public ModelAndView CourtClaimView(@PathVariable("id") Long id) {
		ModelAndView mav = new ModelAndView();
		PrpLCourtClaimVo courtClaimVo = courtService.findCourtClaim(id);
		mav.addObject("courtClaimVo",courtClaimVo);
		mav.setViewName("courtMessage/CourtClaimView");
		return mav;
	}
	@RequestMapping(value = "viewCourtLitigation.do/{id}")
	public ModelAndView CourtLitigationView(@PathVariable("id") Long id) {
		ModelAndView mav = new ModelAndView();
		PrpLCourtLitigationVo courtLitigationVo = courtService.findCourtLitigation(id);
		mav.addObject("courtLitigationVo",courtLitigationVo);
		mav.setViewName("courtMessage/CourtLitigationView");
		return mav;
	}
	@RequestMapping(value = "viewCourtConfirm.do/{id}")
	public ModelAndView CourtConfirmView(@PathVariable("id") Long id) {
		ModelAndView mav = new ModelAndView();
		PrpLCourtConfirmVo courtConfirmVo = courtService.findCourtConfirm(id);
		mav.addObject("courtConfirmVo",courtConfirmVo);
		mav.setViewName("courtMessage/CourtConfirmView");
		return mav;
	}
	@RequestMapping(value = "viewCourtMediation.do/{id}")
	public ModelAndView CourtConfirmMediation(@PathVariable("id") Long id) {
		ModelAndView mav = new ModelAndView();
		PrpLCourtMediationVo courtMediationVo = courtService.findCourtMediation(id);
		mav.addObject("courtMediationVo",courtMediationVo);
		mav.setViewName("courtMessage/CourtMediationView");
		return mav;
	}
}
