package ins.sino.claimcar.claim.web.action;

import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.claim.service.ClaimTaskService;
import ins.sino.claimcar.claim.vo.PrpLClaimKindHisVo;
import ins.sino.claimcar.common.losscharge.service.LossChargeService;
import ins.sino.claimcar.common.losscharge.vo.PrpLDlossChargeVo;
import ins.sino.claimcar.regist.service.RegistService;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/update")
public class PrpLClaimkindHisAction {
	@Autowired
	ClaimTaskService claimTaskService;
	
	@Autowired
	RegistService registService;
	
	// 查看估损更新轨迹信息
	@RequestMapping(value = "/updateMessage.ajax")
	@ResponseBody
	public ModelAndView updatemessage(String registNo) {
		// 交强险立案号
		String claimNo1 = null;
		// 商业险立案号
		String claimNo2 = null;
		ModelAndView mv = new ModelAndView();
		List<PrpLClaimKindHisVo> prpLClaimKindHisList1=new ArrayList<PrpLClaimKindHisVo>(); 
		List<PrpLClaimKindHisVo> prpLClaimKindHisList = claimTaskService
				.findPrpLClaimKindHisByRegistNo(registNo);
		PrpLRegistVo registVo = registService.findRegistByRegistNo(registNo);
		if (prpLClaimKindHisList != null && prpLClaimKindHisList.size() > 0){
			for(int i=0;i<prpLClaimKindHisList.size();i++){
			 if(!CodeConstants.NOSUBRISK_MAP.containsKey(prpLClaimKindHisList.get(i).getKindCode()
						)
						&& !prpLClaimKindHisList.get(i).getKindCode().endsWith("M")){
				 prpLClaimKindHisList1.add(prpLClaimKindHisList.get(i));
				 
			 }
			 }
		
			// 对List里的对象根据更新次数顺序输出
			Collections.sort(prpLClaimKindHisList1, new Comparator<PrpLClaimKindHisVo>() {
						@Override
						public int compare(PrpLClaimKindHisVo o1,
								PrpLClaimKindHisVo o2) {
							return o1.getEstiTimes().compareTo(
									o2.getEstiTimes());
						}
					});
			for (PrpLClaimKindHisVo prpLClaimKindHisVo1 : prpLClaimKindHisList1) {
				if (prpLClaimKindHisVo1.getRiskCode().equals("1101")) {
					if (claimNo1 == null) {
						claimNo1 = prpLClaimKindHisVo1.getClaimNo();
					}
				} else {
					if (claimNo2 == null) {
						claimNo2 = prpLClaimKindHisVo1.getClaimNo();
					}
				}
			}

		}
		mv.addObject("claimNo1", claimNo1);
		mv.addObject("claimNo2", claimNo2);
		mv.addObject("prpLClaimKindHisList", prpLClaimKindHisList1);
		mv.addObject("riskCode", registVo.getRiskCode());
		mv.setViewName("loss-common/prplclaimkindHis");
		return mv;
	}
}
