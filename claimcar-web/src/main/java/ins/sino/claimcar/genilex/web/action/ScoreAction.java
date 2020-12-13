package ins.sino.claimcar.genilex.web.action;



import java.util.ArrayList;
import java.util.List;

import ins.sino.claimcar.genilex.ReportInfoService;
import ins.sino.claimcar.genilex.po.PrpLRuleCase;
import ins.sino.claimcar.genilex.vo.common.PrpLFraudScoreVo;
import ins.sino.claimcar.genilex.vo.common.PrpLRuleCaseVo;
import ins.sino.claimcar.regist.service.PrpLCMainService;
import ins.sino.claimcar.regist.service.RegistService;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistCarLossVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/scoreAction")
public class ScoreAction {
	private static Logger logger = LoggerFactory.getLogger(ScoreAction.class);
	@Autowired
	private ReportInfoService reportInfoService;
	@Autowired
	RegistService registService;
	@Autowired
    PrpLCMainService prpLCMainService;
	
	@RequestMapping("/scoreView.do")
	public ModelAndView scoreView(String registNo){
		ModelAndView mv=new ModelAndView();
		PrpLRegistVo prplregistVo=registService.findRegistByRegistNo(registNo);
		List<PrpLCMainVo> cmainVos=prpLCMainService.findPrpLCMainsByRegistNo(registNo);
		
		List<PrpLRegistCarLossVo> carLosss=prplregistVo.getPrpLRegistCarLosses();
		PrpLFraudScoreVo registSocreVo=reportInfoService.findPrpLFraudScoreVoByRegistNo(registNo,"C001");
		PrpLFraudScoreVo checkSocreVo=reportInfoService.findPrpLFraudScoreVoByRegistNo(registNo,"C002");
		PrpLFraudScoreVo dlossSocreVo=reportInfoService.findPrpLFraudScoreVoByRegistNo(registNo,"C003");
		List<PrpLFraudScoreVo> scoreVos=new ArrayList<PrpLFraudScoreVo>();
		if(registSocreVo!=null){
			scoreVos.add(registSocreVo);
		}
		if(checkSocreVo!=null){
			scoreVos.add(checkSocreVo);
		}
		if(dlossSocreVo!=null){
			scoreVos.add(dlossSocreVo);
		}
		String carLicenseNo="";//标的车车牌号
		if(carLosss!=null && carLosss.size()>0){
			for(PrpLRegistCarLossVo carlossVo:carLosss){
				if("1".equals(carlossVo.getLossparty())){
					carLicenseNo=carlossVo.getLicenseNo();
					break;
				}
			}
		}
		String insuredName="";//被保险人
		if(cmainVos!=null && cmainVos.size()>0){
			for(PrpLCMainVo cmainVo:cmainVos){
				insuredName=cmainVo.getInsuredName();
				break;
			}
		}
		mv.addObject("insuredName",insuredName);
		mv.addObject("carLicenseNo",carLicenseNo);
		mv.addObject("registNo",registNo);
		mv.addObject("scoreVos",scoreVos);
		mv.setViewName("genilex/scoreView");
		return mv;
	}
	@RequestMapping("/ruleShow.do")
	public ModelAndView ruleShow(Long fraudScoreID){
		ModelAndView mv=new ModelAndView();
		PrpLFraudScoreVo  prpLFraudScoreVo =reportInfoService.findPrpLFraudScoreById(fraudScoreID);
		List<PrpLRuleCaseVo> rulecases=new ArrayList<PrpLRuleCaseVo>();
		PrpLRuleCaseVo caseVo=new PrpLRuleCaseVo();
		if(prpLFraudScoreVo!=null){
			rulecases=prpLFraudScoreVo.getSuleCases();
			if(rulecases!=null && rulecases.size()>0){
				caseVo=rulecases.get(0);
			}
		}
		
		mv.addObject("caseVo",caseVo);
		mv.setViewName("genilex/ruleView");
		
		return mv;
		
	}

}
