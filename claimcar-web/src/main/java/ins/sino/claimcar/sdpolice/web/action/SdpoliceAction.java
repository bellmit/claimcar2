package ins.sino.claimcar.sdpolice.web.action;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ins.sino.claimcar.trafficplatform.service.SdpoliceService;
import ins.sino.claimcar.trafficplatform.vo.PrpLsdpoliceInfoVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
@Controller
@RequestMapping("/sdpolice")
public class SdpoliceAction {
	@Autowired
	SdpoliceService sdpoliceService;
	
	@RequestMapping(value="/sdpoliceInfoList.do")
	public ModelAndView sdpoliceInfoList(String registNo){
		ModelAndView mv=new ModelAndView();
		List<PrpLsdpoliceInfoVo> infoVoList=sdpoliceService.findPrpLsdpoliceInfoVoByRegistNo(registNo);
		List<String> warnMessageList=new ArrayList<String>();
		if(infoVoList!=null && infoVoList.size()>0){
			for(PrpLsdpoliceInfoVo infoVo:infoVoList){
				warnMessageList.add(infoVo.getWarnMessage());
			}
		}
	   Set<String> warnMessageSet=new HashSet<String>(warnMessageList);//去除重复的预警提示信息
	   mv.addObject("warnMessageSet",warnMessageSet);
	   
	   mv.setViewName("flowQuery/sdpoliceInfoShow");
	   return mv;
	}
	
}
