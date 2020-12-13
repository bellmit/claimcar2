package ins.sino.claimcar.claim.web.action;


import ins.sino.claimcar.endcase.vo.PrpLuwNotionMainVo;
import ins.sino.claimcar.endcase.vo.PrpLuwNotionVo;
import ins.sino.claimcar.verifyclaim.service.VerifyClaimService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/history")
public class HistoryMessageAction{
	
	@Autowired
	VerifyClaimService  verifyClaimService;

	// 历史审核意见
	@RequestMapping(value = "/historyMessage.ajax")
	@ResponseBody
	public ModelAndView historyMessage(String registNo,String compensateNo) {
		PrpLuwNotionMainVo prpLuwNotionMainVo;
		ModelAndView mv = new ModelAndView();
//		String a=registNo;
//		String b=compensateNo;
		prpLuwNotionMainVo = verifyClaimService.queryUwNotion(registNo,compensateNo);
		if(prpLuwNotionMainVo!=null){
		 List<PrpLuwNotionVo> prpLuwNotionVoList =prpLuwNotionMainVo.getPrpLuwNotions();	
		 mv.addObject("prpLuwNotionVoList",prpLuwNotionVoList);
		 }
		mv.setViewName("uwNotion/PrpLuwNotion");
		return mv;
	}
}
