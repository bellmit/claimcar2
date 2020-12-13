package ins.sino.claimcar.check.web.action;

import ins.sino.claimcar.claim.service.SdwarnService;
import ins.sino.claimcar.claim.vo.PrpLwarnInfoVo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/warnAction")
public class WarnAction {

	private Logger logger = LoggerFactory.getLogger(WarnAction.class);
	
	@Autowired 
	SdwarnService sdwarnService;
	
	/*
	 * 显示山东预警信息
	 */
	@RequestMapping(value="/viewCheck.do")
	public ModelAndView viewCheck(String registNo) {
		ModelAndView mav = new ModelAndView();
		List<PrpLwarnInfoVo> wanDefLossVoBList=new ArrayList<PrpLwarnInfoVo>();
		Set<String> set=new HashSet<String>();//去重理赔编码,理赔阶段
		List<PrpLwarnInfoVo> wanDefLossVoList =sdwarnService.findPrpLwarnInfoVosByRegistNo(registNo);
		if(wanDefLossVoList!=null && wanDefLossVoList.size()>0){
			for(PrpLwarnInfoVo warninfoVo:wanDefLossVoList){
				if(set.add(warninfoVo.getClaimsequenceNo()+"_"+warninfoVo.getWarnstageCode())){//去重同一个理赔编码,理赔阶段的
					wanDefLossVoBList.add(warninfoVo);
				  }
			}
		}
		mav.addObject("wanDefLossVoList",wanDefLossVoBList);
		mav.setViewName("check/warning/Earlywarning");
		return mav;
	}
	
	
	/**
	 * 显示山东预警信息详情
	 * @throws Exception
	 */
	@RequestMapping(value = "/viewWarnInfo.do")
	@ResponseBody
	public ModelAndView viewWarnInfo(String claimsequenceNo,String warnstageCode){
		ModelAndView mav = new ModelAndView();
		List<PrpLwarnInfoVo> warnInfoDetails = sdwarnService.findwarnVosByclaimsequenceNo(claimsequenceNo, warnstageCode);
		mav.addObject("warnInfoDetails", warnInfoDetails);
		mav.setViewName("check/warning/WarningDetails");
		return mav;
	}
}
