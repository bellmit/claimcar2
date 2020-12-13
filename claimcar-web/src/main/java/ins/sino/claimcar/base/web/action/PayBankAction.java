package ins.sino.claimcar.base.web.action;

import ins.framework.web.AjaxResult;
import ins.framework.web.bind.annotation.FormModel;
import ins.platform.utils.DateUtils;
import ins.sino.claimcar.other.service.PayBankService;
import ins.sino.claimcar.other.vo.PrpLPayBankHisVo;
import ins.sino.claimcar.other.vo.PrpLPayBankVo;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/payBank")
public class PayBankAction {
	@Autowired
	PayBankService payBankService;
	
	/**
	 * 账号信息修改初始化
	 * @return
	 */
	@RequestMapping(value = "/payBankList.do", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView initPage() {
		Date endDate = new Date();
		Date startDate = DateUtils.addDays(endDate, -1);
		ModelAndView mav = new ModelAndView();
		mav.addObject("applyTimeStart", startDate);
		mav.addObject("applyTimeEnd", endDate);
		mav.setViewName("base/payBank/PayBankList");

		return mav;
	}
	
	@RequestMapping("/payBankList.do")
	@ResponseBody
	public ModelAndView msgParamList() {
		ModelAndView modelAndView = new ModelAndView();
		
		modelAndView.setViewName("base/payBank/PayBankList");
		return modelAndView;
	}
	
	@RequestMapping("/payBankEdit.do")
	@ResponseBody
	public ModelAndView payBankEdit(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		String pid = request.getParameter("pid");
		String flag=request.getParameter("flag");
		if(pid!=null&&flag.equals("undisposed")){
			Long id = Long.parseLong(pid);
			PrpLPayBankHisVo prpLPayBankHisVo = payBankService
					.findPayBankHisVoByPK(id);
			modelAndView.addObject("prpLPayBank", prpLPayBankHisVo);	
			
		}else if(pid!=null&&flag.equals("processed")){
			Long id = Long.parseLong(pid);
			PrpLPayBankVo prpLPayBankVo = payBankService
					.findPayBankVoByPK(id);
			modelAndView.addObject("prpLPayBank", prpLPayBankVo);
		}
		modelAndView.setViewName("base/payBank/PayBankEdit");
		return modelAndView;
	}
	
	/**
	 * 保存收款人银行信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/savePayBankInfo.do")
	@ResponseBody
	public AjaxResult savePayBankInfo(
			@FormModel("prpLPayBankVo") PrpLPayBankHisVo prpLPayBankHisVo) {
		AjaxResult ajaxResult = new AjaxResult();
		try{
			
			payBankService.saveOrUpdatePayBank(prpLPayBankHisVo);
			
			ajaxResult.setStatus(org.apache.http.HttpStatus.SC_OK);
			
		}catch(Exception e){
			ajaxResult.setStatusText(e.getMessage());
			e.printStackTrace();
		}
		return ajaxResult;
	}

}
